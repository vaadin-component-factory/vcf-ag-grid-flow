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

import com.fasterxml.jackson.annotation.JsonProperty;

@SuppressWarnings("unchecked")
public abstract class AbstractColumn<T, ColumnType> {

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

    private int flex = 0;

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

    public ColumnType setHeader(String header) {
        this.header = header;
        return (ColumnType) this;
    }

    public String getHeaderClass() {
        return headerClass;
    }

    public ColumnType setHeaderClass(String headerClass) {
        this.headerClass = headerClass;
        return (ColumnType) this;
    }

    public FrozenColumn getFrozen() {
        return frozen;
    }

    public ColumnType setFrozen(FrozenColumn frozen) {
        this.frozen = frozen;
        return (ColumnType) this;
    }

    public ColumnType setFrozen(boolean frozen) {
        this.frozen = FrozenColumn.left;
        return (ColumnType) this;
    }

    public boolean isSortable() {
        return sortable;
    }

    public ColumnType setSortable(boolean sortable) {
        this.sortable = sortable;
        return (ColumnType) this;
    }

    public boolean isAutoWidth() {
        return autoWidth;
    }

    public ColumnType setAutoWidth(boolean autoWidth) {
        this.autoWidth = autoWidth;
        return (ColumnType) this;
    }

    public boolean isCheckboxSelection() {
        return checkboxSelection;
    }

    public ColumnType setCheckboxSelection(boolean checkboxSelection) {
        this.checkboxSelection = checkboxSelection;
        return (ColumnType) this;
    }

    public int getFlex() {
        return flex;
    }

    public ColumnType setFlex(int flex) {
        this.flex = flex;
        return (ColumnType) this;
    }
}
