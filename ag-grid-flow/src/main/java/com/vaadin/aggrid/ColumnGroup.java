package com.vaadin.aggrid;

/*
 * #%L
 * ag-grid-flow
 * %%
 * Copyright (C) 2020 Vaadin Ltd
 * %%
 * This program is available under Commercial Vaadin Add-On License 3.0
 * (CVALv3).
 * 
 * See the file license.html distributed with this software for more
 * information about licensing.
 * 
 * You should have received a copy of the CVALv3 along with this program.
 * If not, see <http://vaadin.com/license/cval-3>.
 * #L%
 */

import com.vaadin.flow.function.ValueProvider;

import java.util.ArrayList;
import java.util.List;

/**
 * @author jcgueriaud
 */
public class ColumnGroup<T> extends AbstractColumn<T, ColumnGroup<T>> {

    private List<Column<T>> children = new ArrayList<>();

    public Column<T> addColumn(String key, ValueProvider<T, ?> valueProvider) {
        Column<T> column = new Column<>(key, valueProvider);
        children.add(column);

        column.setComparator(((a, b) -> AgGrid.compareMaybeComparables(
                valueProvider.apply(a), valueProvider.apply(b))));
        return column;
    }

    public List<Column<T>> getChildren() {
        return children;
    }

    public void setChildren(List<Column<T>> children) {
        this.children = children;
    }
}
