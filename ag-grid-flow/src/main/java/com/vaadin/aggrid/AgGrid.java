package com.vaadin.aggrid;

/*
 * #%L
 * ag-grid-flow
 * %%
 * Copyright (C) 2020 Vaadin Ltd
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */

import com.vaadin.flow.component.*;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.dependency.JsModule;
import com.vaadin.flow.component.dependency.NpmPackage;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.data.provider.*;
import com.vaadin.flow.function.SerializableComparator;
import com.vaadin.flow.function.SerializableConsumer;
import com.vaadin.flow.function.ValueProvider;
import com.vaadin.flow.internal.JsonUtils;
import com.vaadin.flow.internal.ReflectTools;
import com.vaadin.flow.shared.Registration;
import elemental.json.Json;
import elemental.json.JsonArray;
import elemental.json.JsonObject;
import elemental.json.JsonValue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.function.BinaryOperator;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Wrapper for ag-grid javascript component
 *
 * @param <T> the grid bean type
 */
@NpmPackage(value = "@ag-grid-community/styles",version = "29.1.0")
@NpmPackage(value = "@ag-grid-community/core",version = "29.1.0")
@NpmPackage(value = "@ag-grid-community/polymer",version = "26.2.2")
@NpmPackage(value = "@ag-grid-community/infinite-row-model",version = "29.1.0")
@JsModule("./ag-connector.js")
@CssImport("@ag-grid-community/styles/ag-grid.min.css")
@CssImport("@ag-grid-community/styles/ag-theme-alpine.min.css")
public class AgGrid<T> extends Div {

    private static final Logger log = LoggerFactory.getLogger(AgGrid.class);

    private final List<AbstractColumn<T, ?>> columnsDefs = new ArrayList<>();
    private Registration dataProviderListener;
    private ComponentEventListener<ItemClickEvent<T>> itemClickEventComponentEventListener;
    private DataProvider<T, ?> dataProvider;
    private List<QuerySortOrder> sortOrders;

    /**
     *
     */
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

    public DataProvider<T, ?> getDataProvider() {
        return dataProvider;
    }

    public void setDataProvider(DataProvider<T, ?> dataProvider) {
        this.dataProvider = dataProvider;
        runBeforeClientResponse(ui -> getElement()
                .callJsFunction("$connector.setDataSource"));
        setupDataProviderListener(dataProvider);
    }

    @Override
    protected void onAttach(AttachEvent attachEvent) {
        super.onAttach(attachEvent);
        DataProvider<T, ?> dataProvider = getDataProvider();
        if (dataProvider != null && dataProviderListener == null) {
            setupDataProviderListener(dataProvider);
        }
    }

    @Override
    protected void onDetach(DetachEvent detachEvent) {
        if (dataProviderListener != null) {
            dataProviderListener.remove();
            dataProviderListener = null;
        }
        super.onDetach(detachEvent);
    }

    private <C> void setupDataProviderListener(DataProvider<T, C> dataProvider) {
        if (dataProviderListener != null) {
            dataProviderListener.remove();
        }
        dataProviderListener = dataProvider.addDataProviderListener(e -> {
            runBeforeClientResponse(ui -> getElement()
                .callJsFunction("$connector.refreshAll"));
        });
    }

    @ClientCallable
    public JsonObject requestClientPage(int startRow, int endRow, JsonArray sortModel, JsonObject filterModel) {
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

        sortOrders = querySortOrderBuilder.build();
        Stream<T> fetch = dataProvider.fetch(new Query<>(startRow, requestedPageSize, sortOrders, createSortingComparator(sortOrders), null));

        // should be avoided if it's not a ListDataProvider
        if (dataProvider instanceof ListDataProvider) {
            int rowCount = dataProvider.size(new Query<>(startRow, requestedPageSize, sortOrders, createSortingComparator(sortOrders), null));
            runBeforeClientResponse(ui -> getElement()
                    .callJsFunction("$connector.setRowCount", rowCount));
        }
        List<T> page = fetch.collect(Collectors.toList());
        // lastRow is boolean - isMaxRowFound
        final int lastRow = (page.size() < requestedPageSize)?startRow + page.size():-1;
        JsonObject jsonObject = Json.createObject();
        jsonObject.put("lastRow", lastRow);
        jsonObject.put("startRow", startRow);
        jsonObject.put("page", convertListToJsonArray(page));
        return jsonObject;

    }

    protected SerializableComparator<T> createSortingComparator(List<QuerySortOrder> sortOrders) {
        BinaryOperator<SerializableComparator<T>> operator = (comparator1, comparator2) -> {
            Comparator<T> var10000 = comparator1.thenComparing(comparator2);
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
            for (AbstractColumn<T, ?> column : columnsDefs) {
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

    public List<AbstractColumn<T, ?>> getTopLevelColumns() {
        List<AbstractColumn<T, ?>> ret = new ArrayList<>(columnsDefs);
        return Collections.unmodifiableList(ret);
    }

    private void appendChildColumns(List<Column<T>> ret, AbstractColumn<T, ?> column) {
        if (column instanceof Column) {
            ret.add((Column<T>) column);
        } else if (column instanceof ColumnGroup) {
            for (AbstractColumn<T, ?> child : ((ColumnGroup<T>) column).getChildren()) {
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

        column.setComparator(((a, b) -> compareMaybeComparables(
                valueProvider.apply(a), valueProvider.apply(b))));
        return column;
    }

    public void refreshColumnDefs() {
        runBeforeClientResponse(ui -> getElement()
                .callJsFunction("$connector.setColumnDefs", JsonUtils.listToJson(columnsDefs)));
    }

    private Column<T> getColumnByKey(String columnId) {
        for (Column<T> column : getColumns()) {
            if (columnId.equals(column.getColumnKey())) {
                return column;
            }
        }
        return null;
    }

    // From the grid
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

    /**
     * Adds an item click listener to this component.
     *
     * @param listener
     *            the listener to add, not <code>null</code>
     *
     */
    public void addItemClickListener(
            ComponentEventListener<ItemClickEvent<T>> listener) {
        itemClickEventComponentEventListener = listener;
        runBeforeClientResponse(ui -> getElement()
                .callJsFunction("$connector.addItemClickListener"));
    }

    @ClientCallable
    private void cellClicked(String colId, int rowIndex, JsonObject clickEvent) {
        // fetch the row
        Stream<T> fetch = fetchRowItemFromRowIndex(rowIndex);
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

    private Stream<T> fetchRowItemFromRowIndex(int rowIndex) {
        log.warn("fetchRowItemFromRowIndex does not managed filter");
        return dataProvider.fetch(new Query<>(rowIndex, 1, sortOrders, createSortingComparator(sortOrders), null));
    }
    //// Selection mode ////
 /*   public enum SelectionMode {
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
*/
    @ClientCallable
    private void cellRendererFrameworkCallback(String colId, int rowIndex, String action) {
        Stream<T> fetch = fetchRowItemFromRowIndex(rowIndex);
        fetch.findFirst().ifPresent(rowData -> {
            getColumnByKey(colId).runActionHandler(action, rowData);
        });
    }


    /**
     * Scrolls to the given row index. Scrolls so that the row is shown at the
     * start of the visible area whenever possible.
     *
     *
     * @param rowIndex
     *            zero based index of the item to scroll to in the current view.
     */
    public void scrollToIndex(int rowIndex) {
        runBeforeClientResponse(ui -> getElement()
                .callJsFunction("$connector.scrollTo", rowIndex));
    }

}
