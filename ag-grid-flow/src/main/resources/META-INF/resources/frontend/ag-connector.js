import {InfiniteRowModelModule} from '@ag-grid-community/infinite-row-model';
import { Grid } from '@ag-grid-community/core';

if (typeof window.Vaadin.Flow.agGridRenderers === 'undefined') {
    window.Vaadin.Flow.agGridRenderers = [];
}


window.Vaadin.Flow.agGridConnector = {
    renderers: [],
    initLazy: function (c) {
        // Check whether the connector was already initialized
        if (c.$connector) {
            return;
        }

        c.$connector = {
            //
            currentPageParams : [],
            //// functions
            addItemClickListener : function() {

                this.agGrid.gridOptions.api.addEventListener('cellClicked', function myRowClickedHandler(event) {
                    c.$server.cellClicked(event.column.colDef.field, event.rowIndex,
                        {
                            screenX: event.event.screenX, screenY: event.event.screenY,
                            clientX: event.event.clientX, clientY: event.event.clientY,
                            detail: event.event.detail, button: event.event.button,
                            ctrlKey: event.event.ctrlKey, shiftKey: event.event.shiftKey,
                            altKey: event.event.altKey, metaKey: event.event.metaKey
                        });
                });
            },
            setColumnDefs : function(columnDefs) {
                // transform the configuration (name of the attribute cellRenderer)
                // to a javascript function
                columnDefs.forEach(el => { if (el.cellRenderer != null) {
                    console.log("ag-grid " + el.cellRenderer);
                    el.cellRenderer = window.Vaadin.Flow.agGridRenderers[el.cellRenderer];
                }});

                this.agGrid.gridOptions.api.setColumnDefs(columnDefs);
            },
            setSelectionMode : function(rowSelection) {
                //debugger;
                this.agGrid.gridOptions.rowSelection = rowSelection;
                this.agGrid.gridOptions.api.addEventListener('selectionChanged', function selectionChangedHandler(event) {
                    c.$server.selectionChanged();
                });
                this.agGrid.gridOptions.api.addEventListener('rowSelected', function rowSelectedHandler(event) {
                    c.$server.rowSelected();
                });
            },

            //// https://www.ag-grid.com/javascript-grid-api/#scrolling
            scrollTo : function(rowIndex) {
                this.agGrid.gridOptions.api.ensureIndexVisible(rowIndex);
            },

            setRowCount : function(rowCount) {
                this.agGrid.gridOptions.api.setInfiniteRowCount(rowCount);
            },

            setDataSource : function() {
                var dataSource = {
                    rowCount: null, // behave as infinite scroll
                    getRows: function (params) {
                        // debugger;
                        console.log('ag-grid getRows for ' + params.startRow + ' to ' + params.endRow);
                        c.$server.requestClientPage(params.startRow, params.endRow, params.sortModel, params.filterModel);
                        c.$connector.currentPageParams[params.startRow] = params;
                    }
                };

                this.agGrid.gridOptions.api.setDatasource(dataSource);
            },

            setCurrentClientPage : function(rowsThisPage, lastRow, startRow) {
                if (c.$connector.currentPageParams[startRow] != null) {
                    c.$connector.currentPageParams[startRow].successCallback(rowsThisPage, lastRow);
                    c.$connector.currentPageParams[startRow] = null;
                }
            },

        };
        // let the grid know which columns and what data to use
        var gridOptions = {
            rowModelType : 'infinite',
            //cacheBlockSize: 30,
            // no impact on the client side
            //animateRows: true,
            // todo jcg by default its 100, no performance changes on the client side
           /* cacheBlockSize: 1000,*/
        };

        /** no scrolling issue with IE11 and use autopagination **/
        if (window.document.documentMode) {
            // Do IE stuff
            gridOptions.paginationAutoPageSize = true;
            //gridOptions.paginationPageSize = 30;
            gridOptions.pagination = true;
            //gridOptions.cacheBlockSize = 60;
        }
        c.$connector.agGrid = new Grid(c, gridOptions, { modules: [InfiniteRowModelModule]});

        c.$connector.agGrid.gridOptions.api.addEventListener('cellValueChanged', function cellValueChangedHandler(event) {
            c.$server.cellValueChanged(event.column.colDef.field, event.rowIndex, event.oldValue, event.newValue);
        });

    }
}
