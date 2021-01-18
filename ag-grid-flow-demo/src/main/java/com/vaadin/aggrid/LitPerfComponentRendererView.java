package com.vaadin.aggrid;

import com.vaadin.aggrid.bean.Person;
import com.vaadin.aggrid.bean.PersonUtil;
import com.vaadin.flow.component.dependency.JsModule;
import com.vaadin.flow.component.dependency.Uses;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.provider.DataProvider;
import com.vaadin.flow.router.Route;

/**
 * Here is another example of different renderers for cells
 * <p>This example is using lit-element but it can be renderer as html or polymer.
 * </p>
 * <p>
 *  The idea is to compare a vaadin-grid and ag-grid with the same renderers
 * </p>
 * @author jcgueriaud
 */
@JsModule("./src/icon-lit-renderer.js")
@Uses(TextField.class)
@Route(value = "perf-lit-component-renderer", layout = MainLayout.class)
public class LitPerfComponentRendererView extends Div {

    public LitPerfComponentRendererView() {
        setSizeFull();
        AgGrid<Person> grid = buildAdvancedGrid();
        grid.refreshColumnDefs();
        grid.setDataProvider(DataProvider.ofCollection(PersonUtil.buildPersons()));
        add(grid);
    }

    private AgGrid<Person> buildAdvancedGrid() {
        AgGrid<Person> grid = new AgGrid<>();
        grid.setSizeFull();
        for (int i = 0; i < 30; i++) {
            grid.addColumn("firstname" +i,Person::getFirstName)
                .setHeader("FirstName")
                .setCellRendererFramework("textfield-lit-renderer");
        }
        return grid;
    }

}
