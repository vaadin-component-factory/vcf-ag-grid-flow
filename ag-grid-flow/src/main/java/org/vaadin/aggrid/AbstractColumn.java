package org.vaadin.aggrid;

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
