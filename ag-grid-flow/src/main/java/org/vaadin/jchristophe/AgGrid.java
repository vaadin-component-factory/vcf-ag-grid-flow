package org.vaadin.jchristophe;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.ClientCallable;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.dependency.JavaScript;
import com.vaadin.flow.component.dependency.NpmPackage;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.data.binder.Setter;
import com.vaadin.flow.data.provider.DataGenerator;
import com.vaadin.flow.data.provider.DataProvider;
import com.vaadin.flow.data.provider.Query;
import com.vaadin.flow.data.provider.QuerySortOrder;
import com.vaadin.flow.data.provider.QuerySortOrderBuilder;
import com.vaadin.flow.data.provider.SortDirection;
import com.vaadin.flow.function.SerializableComparator;
import com.vaadin.flow.function.SerializableConsumer;
import com.vaadin.flow.function.SerializableFunction;
import com.vaadin.flow.function.ValueProvider;
import com.vaadin.flow.internal.JsonSerializer;
import com.vaadin.flow.internal.ReflectTools;
import elemental.json.Json;
import elemental.json.JsonArray;
import elemental.json.JsonObject;
import elemental.json.JsonValue;
import elemental.json.impl.JreJsonFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.BinaryOperator;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@NpmPackage(value = "@ag-grid-community/core",version = "23.1.0")
@NpmPackage(value = "@ag-grid-community/infinite-row-model",version = "23.1.0")
@JavaScript("frontend://ag-connector.js")
@CssImport("@ag-grid-community/core/dist/styles/ag-theme-alpine.min.css")
@CssImport("@ag-grid-community/core/dist/styles/ag-grid.min.css")
public class AgGrid<T> extends Div {

    private static final Logger log = LoggerFactory.getLogger(AgGrid.class);

    private final List<AbstractColumn<T>> columnsDefs = new ArrayList<>();

    public AgGrid() {
        setWidthFull();
        initConnector();
        addClassName("ag-theme-alpine");
    }

    private void initConnector() {
        runBeforeClientResponse(ui -> ui.getPage().executeJs(
                "window.Vaadin.Flow.agGridConnector.initLazy($0)",  getElement()));
    }

    private void runBeforeClientResponse(SerializableConsumer<UI> command) {
        getElement().getNode().runWhenAttached(ui -> ui
                .beforeClientResponse(this, context -> command.accept(ui)));
    }

    private DataProvider<T, ?> dataProvider;

    public void setDataProvider(DataProvider<T, ?> dataProvider) {
        this.dataProvider = dataProvider;
        runBeforeClientResponse(ui -> getElement()
                .callJsFunction("$connector.setDataSource"));
    }

    @ClientCallable
    public void requestClientPage(int startRow, int endRow, JsonArray sortModel, JsonObject filterModel) {
        log.debug("requestClientPage {}, {}", startRow, endRow);
        int requestedPageSize = endRow - startRow;
        QuerySortOrderBuilder querySortOrderBuilder = new QuerySortOrderBuilder();
        for (int i = 0; i < sortModel.length(); i++) {
            JsonValue jsonValue = sortModel.get(i);
            if (jsonValue instanceof JsonObject) {
                JsonObject sortColumnModel = (JsonObject) jsonValue;
                String key = sortColumnModel.getString("colId");
                if ("asc".equals(sortColumnModel.getString("sort"))) {
                    querySortOrderBuilder.thenAsc(key);
                } else {
                    querySortOrderBuilder.thenDesc(key);
                }
            }
        }

        List<QuerySortOrder> sortOrders = querySortOrderBuilder.build();
        Stream<T> fetch = dataProvider.fetch(new Query<>(startRow, requestedPageSize, sortOrders, createSortingComparator(sortOrders), null));

        // todo jcg the size of the dataprovider is called foreach page request
        // should be avoided if it's not a ListDataProvider
        int rowCount = dataProvider.size(new Query<>(startRow, requestedPageSize, sortOrders, createSortingComparator(sortOrders), null));
        runBeforeClientResponse(ui -> getElement()
                .callJsFunction("$connector.setRowCount", rowCount));
        List<T> page = fetch.collect(Collectors.toList());
        // lastRow is boolean - isMaxRowFound
        final int lastRow = (page.size() < requestedPageSize)?startRow + page.size():-1;
        runBeforeClientResponse(ui -> getElement()
                .callJsFunction("$connector.setCurrentClientPage", convertListToJsonArray(page), lastRow, startRow));

    }

    protected SerializableComparator<T> createSortingComparator(List<QuerySortOrder> sortOrders) {
        BinaryOperator<SerializableComparator<T>> operator = (comparator1, comparator2) -> {
            Comparator var10000 = comparator1.thenComparing(comparator2);
            return var10000::compare;
        };
        return sortOrders.stream().map((order) -> {
            return getColumnByKey(order.getSorted()).getComparator(order.getDirection());
        }).reduce(operator).orElse(null);
    }

    private JsonArray convertListToJsonArray(List<T> data) {
        JsonArray array = Json.createArray();
        for (int i = 0; i < data.size(); i++) {
            JsonObject jsonObject = Json.createObject();
            for (AbstractColumn<T> column : columnsDefs) {
                if (column instanceof Column) {
                    ((Column<T>)column).getDataGenerator().generateData(data.get(i), jsonObject);
                } else if (column instanceof ColumnGroup){
                    for (Column<T> child : ((ColumnGroup<T>) column).getChildren()) {
                        child.getDataGenerator().generateData(data.get(i), jsonObject);
                    }
                }
            }
            array.set(i,jsonObject);
        }
        log.debug("convertListToJsonArray {}", data.size());
        return array;
    }

    /**
     * Gets an unmodifiable list of all {@link Grid.Column}s currently in this
     * {@link Grid}.
     * <p>
     * <strong>Note:</strong> the order of the list returned by this method might not be
     * correct.
     *
     * @return unmodifiable list of columns
     */
    public List<Column<T>> getColumns() {
        List<Column<T>> ret = new ArrayList<>();
        columnsDefs.forEach(column -> appendChildColumns(ret, column));
        return Collections.unmodifiableList(ret);
    }

    public List<AbstractColumn<T>> getTopLevelColumns() {
        List<AbstractColumn<T>> ret = new ArrayList<>(columnsDefs);
        return Collections.unmodifiableList(ret);
    }

    private void appendChildColumns(List<Column<T>> ret, AbstractColumn<T> column) {
        if (column instanceof Column) {
            ret.add((Column<T>) column);
        } else if (column instanceof ColumnGroup) {
            for (AbstractColumn<T> child : ((ColumnGroup<T>) column).getChildren()) {
                appendChildColumns(ret, child);
            }
        }
    }

    ///// JAVA API copied from vaadin grid
    public ColumnGroup<T> addColumnGroup() {
        ColumnGroup<T> column = new ColumnGroup<>();
        columnsDefs.add(column);
        return column;
    }

    public Column<T> addColumn(String key, ValueProvider<T, ?> valueProvider) {
        Column<T> column = new Column<>(key, valueProvider);
        columnsDefs.add(column);

        column.comparator = ((a, b) -> compareMaybeComparables(
                valueProvider.apply(a), valueProvider.apply(b)));
        return column;
    }

    private JreJsonFactory jsonFactory;

    private JreJsonFactory getJsonFactory() {
        if (jsonFactory == null) {
            jsonFactory = new JreJsonFactory();
        }

        return jsonFactory;
    }

    public void refreshColumnDefs() {
        try {
            JsonValue jsonValue = getJsonFactory().parse(objectMapper.writeValueAsString(columnsDefs));

            runBeforeClientResponse(ui -> getElement()
                    .callJsFunction("$connector.setColumnDefs", jsonValue));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }
    private ObjectMapper objectMapper = new ObjectMapper();

    private Column<T> getColumnByKey(String columnId) {
        for (Column<T> column : getColumns()) {
            if (columnId.equals(column.getColumnKey())) {
                return column;
            }
        }
        return null;
    }

    public static abstract class AbstractColumn<T> {


        public enum FrozenColumn {
            left("left"),
            right("right");

            private String value;

            FrozenColumn(String value) {
                this.value = value;
            }

            public String toString() {
                return value;
            }
        }

        @JsonProperty("headerName")
        private String header;

        @JsonProperty("pinned")
        private FrozenColumn frozen;

        private boolean sortable;

        private boolean checkboxSelection;

        private boolean autoWidth;

        private String headerClass;

        public String getHeader() {
            return header;
        }

        public AbstractColumn<T> setHeader(String header) {
            this.header = header;
            return this;
        }

        public String getHeaderClass() {
            return headerClass;
        }

        public AbstractColumn<T> setHeaderClass(String headerClass) {
            this.headerClass = headerClass;
            return this;
        }

        public FrozenColumn getFrozen() {
            return frozen;
        }

        public AbstractColumn<T> setFrozen(FrozenColumn frozen) {
            this.frozen = frozen;
            return this;
        }

        public AbstractColumn<T> setFrozen(boolean frozen) {
            this.frozen = FrozenColumn.left;
            return this;
        }

        public boolean isSortable() {
            return sortable;
        }

        public AbstractColumn<T> setSortable(boolean sortable) {
            this.sortable = sortable;
            return this;
        }

        public boolean isAutoWidth() {
            return autoWidth;
        }

        public AbstractColumn<T> setAutoWidth(boolean autoWidth) {
            this.autoWidth = autoWidth;
            return this;
        }

        public boolean isCheckboxSelection() {
            return checkboxSelection;
        }

        public AbstractColumn<T> setCheckboxSelection(boolean checkboxSelection) {
            this.checkboxSelection = checkboxSelection;
            return this;
        }
    }

    public static class ColumnGroup<T> extends AbstractColumn<T> {

        private List<Column<T>> children = new ArrayList<>();

        public Column<T> addColumn(String key, ValueProvider<T, ?> valueProvider) {
            Column<T> column = new Column<>(key, valueProvider);
            children.add(column);
            return column;
        }

        public List<Column<T>> getChildren() {
            return children;
        }

        public void setChildren(List<Column<T>> children) {
            this.children = children;
        }
    }

    public static class Column<T> extends AbstractColumn<T> {

        public enum FilterColumn {
            text("agTextColumnFilter"),
            number("agNumberColumnFilter"),
            date("agDateColumnFilter");

            private String value;

            FilterColumn(String value) {
                this.value = value;
            }

            public String toString() {
                return value;
            }
        }
        @JsonProperty("field")
        private String columnKey; // defined and used by the user

        private boolean sortingEnabled;

        private SerializableComparator<T> comparator;

        private ValueProvider<T, ?> valueProvider;

        /// todo JCG String is hardcoded, to change
        private Setter<T, String> setter;

        private SerializableFunction<T, String> classNameGenerator = item -> null;

        private DataGenerator<T> dataGenerator;

        private Integer width;

        private String cellRenderer;

        private String cellEditor;
        private Boolean editable = false;

        private boolean resizable;
        private String cellClass;
        private Map<String,String> cellClassRules = new HashMap<>();

        private String filter;


        public Column(String columnKey, ValueProvider<T, ?> valueProvider) {
            this.columnKey = columnKey;
            this.valueProvider = valueProvider;
            dataGenerator = (item, data) -> data.put(columnKey,
                    JsonSerializer.toJson(valueProvider.apply(item)));
        }

        public boolean isResizable() {
            return resizable;
        }

        public void setResizable(boolean resizable) {
            this.resizable = resizable;
        }

        public String getCellClass() {
            return cellClass;
        }

        public void setCellClass(String cellClass) {
            this.cellClass = cellClass;
        }

        public Integer getWidth() {
            return width;
        }

        public void setWidth(Integer width) {
            this.width = width;
        }

        public String getCellRenderer() {
            return cellRenderer;
        }

        public Column<T> setCellRenderer(String cellRenderer) {
            this.cellRenderer = cellRenderer;
            return this;
        }

        public String getColumnKey() {
            return columnKey;
        }

        public void setColumnKey(String columnKey) {
            this.columnKey = columnKey;
        }

        public boolean isSortingEnabled() {
            return sortingEnabled;
        }

        public void setSortingEnabled(boolean sortingEnabled) {
            this.sortingEnabled = sortingEnabled;
        }

        public SerializableComparator<T> getComparator(
                SortDirection sortDirection) {
            Objects.requireNonNull(comparator,
                    "No comparator defined for sorted column.");
            setSortable(true);
            boolean reverse = sortDirection != SortDirection.ASCENDING;
            return reverse ? comparator.reversed()::compare : comparator;
        }

        public <V extends Comparable<? super V>> Column<T> setComparator(
                ValueProvider<T, V> keyExtractor) {
            Objects.requireNonNull(keyExtractor,
                    "Key extractor must not be null");
            setComparator(Comparator.comparing(keyExtractor,
                    Comparator.nullsLast(Comparator.naturalOrder())));
            return this;
        }

        public Column<T> setComparator(Comparator<T> comparator) {
            Objects.requireNonNull(comparator, "Comparator must not be null");
            setSortable(true);
            this.comparator = comparator::compare;
            return this;
        }
        public ValueProvider<T, ?> getValueProvider() {
            return valueProvider;
        }

        public void setValueProvider(ValueProvider<T, ?> valueProvider) {
            this.valueProvider = valueProvider;
        }

        public SerializableFunction<T, String> getClassNameGenerator() {
            return classNameGenerator;
        }

        public void setClassNameGenerator(SerializableFunction<T, String> classNameGenerator) {
            this.classNameGenerator = classNameGenerator;
        }

        public DataGenerator<T> getDataGenerator() {
            return dataGenerator;
        }

        public void setCellClassRules(String cellClass, String cellClassRules) {
            this.cellClassRules.put(cellClass, cellClassRules);
        }

        public Map<String, String> getCellClassRules() {
            return cellClassRules;
        }

        public String getFilter() {
            return filter;
        }

        public Column<T> setFilter(String filter) {
            this.filter = filter.toString();
            return this;
        }
        public String getCellEditor() {
            return cellEditor;
        }

        //* TODO JCG Requires a Setter to set the value on the server side
        // when the cell value has been edited **/
        public Column<T> setCellEditor(String cellEditor) {
            this.cellEditor = cellEditor;
            if (cellEditor != null) {
                editable = true;
            }
            return this;
        }

        public Boolean getEditable() {
            return editable;
        }

        public Setter<T, String> getSetter() {
            return setter;
        }

        public Column<T> setSetter(Setter<T, String> setter) {
            this.setter = setter;
            return this;
        }
    }

    /**
     * From the grid
     */

    protected static int compareMaybeComparables(Object a, Object b) {
        if (hasCommonComparableBaseType(a, b)) {
            return compareComparables(a, b);
        }
        return compareComparables(Objects.toString(a, ""),
                Objects.toString(b, ""));
    }

    private static boolean hasCommonComparableBaseType(Object a, Object b) {
        if (a instanceof Comparable<?> && b instanceof Comparable<?>) {
            Class<?> aClass = a.getClass();
            Class<?> bClass = b.getClass();

            if (aClass == bClass) {
                return true;
            }

            Class<?> baseType = ReflectTools.findCommonBaseType(aClass, bClass);
            if (Comparable.class.isAssignableFrom(baseType)) {
                return true;
            }
        }
        if ((a == null && b instanceof Comparable<?>)
                || (b == null && a instanceof Comparable<?>)) {
            return true;
        }

        return false;
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    private static int compareComparables(Object a, Object b) {
        return ((Comparator) Comparator.nullsLast(Comparator.naturalOrder()))
                .compare(a, b);
    }

    /// click item listener ///

    private ComponentEventListener<ItemClickEvent<T>> itemClickEventComponentEventListener;

    /**
     * Click event for the grid
     * @param <T> Type of the row
     */
    public static class ItemClickEvent<T> extends ClickEvent<AgGrid<T>> {

        private final T item;

        private final String columnKey;

        /**
         * Creates a new item click event.
         *
         * @param source
         *            the component that fired the event
         * @param fromClient
         *            <code>true</code> if the event was originally fired on the
         *            client, <code>false</code> if the event originates from
         *            server-side logic
         * @param item the item
         * @param columnId
         *            the internal id of the column associated with
         *            the click event
         * @param screenX
         *            the x coordinate of the click event, relative to the upper
         *            left corner of the screen, -1 if unknown
         * @param screenY
         *            the y coordinate of the click event, relative to the upper
         *            left corner of the screen, -i if unknown
         * @param clientX
         *            the x coordinate of the click event, relative to the upper
         *            left corner of the browser viewport, -1 if unknown
         * @param clientY
         *            the y coordinate of the click event, relative to the upper
         *            left corner of the browser viewport, -1 if unknown
         * @param clickCount
         *            the number of consecutive clicks recently recorded
         * @param button
         *            the id of the pressed mouse button
         * @param ctrlKey
         *            <code>true</code> if the control key was down when the event
         *            was fired, <code>false</code> otherwise
         * @param shiftKey
         *            <code>true</code> if the shift key was down when the event was
         *            fired, <code>false</code> otherwise
         * @param altKey
         *            <code>true</code> if the alt key was down when the event was
         *            fired, <code>false</code> otherwise
         * @param metaKey
         *            <code>true</code> if the meta key was down when the event was
         *            fired, <code>false</code> otherwise
         *
         */
        public ItemClickEvent(AgGrid<T> source, boolean fromClient, T item, String columnId, int screenX, int screenY,
                              int clientX, int clientY, int clickCount, int button, boolean ctrlKey, boolean shiftKey, boolean altKey,
                              boolean metaKey) {
            super(source, fromClient, screenX, screenY, clientX, clientY,
                    clickCount, button, ctrlKey, shiftKey, altKey, metaKey);
            this.item = item;
            columnKey = columnId;
        }

        /**
         * Gets the clicked item.
         *
         * @return the clicked item
         */
        public T getItem() {
            return item;
        }

        /**
         * Gets the column key that was clicked.
         *
         * @return the clicked column key, not {@code null}
         */
        public String getColumnKey() {
            return columnKey;
        }
    }

    public void addItemClickListener(
            ComponentEventListener<ItemClickEvent<T>> listener) {
        itemClickEventComponentEventListener = listener;
        runBeforeClientResponse(ui -> getElement()
                .callJsFunction("$connector.addItemClickListener"));
    }

    @ClientCallable
    private void cellClicked(String colId, int rowIndex, JsonObject clickEvent) {
        // fetch the row
        log.warn("cellCLicked does not managed the sort and filter");
        Stream<T> fetch = dataProvider.fetch(new Query<>(rowIndex, 1, null, null, null));
        fetch.findFirst().ifPresent(rowData -> {
            ItemClickEvent<T> itemClickEvent = new ItemClickEvent<>(this, true, rowData, colId,
                    (int) clickEvent.getNumber("screenX"),
                    (int) clickEvent.getNumber("screenY"),
                    (int) clickEvent.getNumber("clientX"),
                    (int) clickEvent.getNumber("clientY"),
                    (int) clickEvent.getNumber("detail"),
                    (int) clickEvent.getNumber("button"),
                    clickEvent.getBoolean("ctrlKey"),
                    clickEvent.getBoolean("shiftKey"),
                    clickEvent.getBoolean("altKey"),
                    clickEvent.getBoolean("metaKey"));
            itemClickEventComponentEventListener.onComponentEvent(itemClickEvent);
        });
    }
    //// Selection mode ////
    public enum SelectionMode {
        SINGLE("single"),
        MULTI("multiple"),
        NONE(null);

        private String value;

        SelectionMode(String value) {
            this.value = value;
        }

        public String toString() {
            return value;
        }
    }

    private SelectionMode selectionMode;

    public SelectionMode getSelectionMode() {
        return selectionMode;
    }

    public AgGrid<T> setSelectionMode(SelectionMode selectionMode) {
        this.selectionMode = selectionMode;

        runBeforeClientResponse(ui -> getElement()
                .callJsFunction("$connector.setSelectionMode", selectionMode.toString()));
        return this;
    }

    @ClientCallable
    private void selectionChanged() {
        log.warn("selectionChanged");
    }

    @ClientCallable
    private void rowSelected() {
        log.warn("rowSelected");
    }

    @ClientCallable
    private void cellValueChanged(String colId, int rowIndex, String oldValue, String newValue) {
        // fetch the row
        log.warn("cellValueChanged does not managed the sort and filter");
        Stream<T> fetch = dataProvider.fetch(new Query<>(rowIndex, 1, null, null, null));
        fetch.findFirst().ifPresent(rowData -> {
            getColumnByKey(colId).getSetter().accept(rowData, newValue);
            log.warn("Value updated from {} to {} for colId {}", oldValue,newValue, colId);
        });
    }

    public void scrollTo(int rowIndex) {
        runBeforeClientResponse(ui -> getElement()
                .callJsFunction("$connector.scrollTo", rowIndex));
    }

}
