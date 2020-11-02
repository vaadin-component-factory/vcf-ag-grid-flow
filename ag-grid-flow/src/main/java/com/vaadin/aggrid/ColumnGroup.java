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
