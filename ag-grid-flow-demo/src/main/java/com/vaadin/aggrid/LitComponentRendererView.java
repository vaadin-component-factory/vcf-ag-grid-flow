package com.vaadin.aggrid;

import com.vaadin.aggrid.bean.Person;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.JsModule;
import com.vaadin.flow.component.dependency.Uses;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.provider.DataProvider;
import com.vaadin.flow.router.Route;
import com.vaadin.aggrid.bean.PersonUtil;

import java.util.Locale;

/**
 * Here is another example of different renderers for cells
 * <p>This example is using lit-element but it can be renderer as html or polymer.
 * </p>
 * <p>
 *  The idea is to compare a vaadin-grid and ag-grid with the same renderers
 * </p>
 * @author jcgueriaud
 */
@JsModule("./src/textfield-lit-renderer.js")
@JsModule("./src/button-lit-renderer.js")
@JsModule("./src/icon-lit-renderer.js")
@Uses(Button.class)
@Uses(Icon.class)
@Uses(TextField.class)
@Route(value = "lit-component-renderer", layout = MainLayout.class)
public class LitComponentRendererView extends Div {

    public LitComponentRendererView() {
        setSizeFull();
        AgGrid<Person> grid = buildAdvancedGrid();
        grid.refreshColumnDefs();
        grid.setDataProvider(DataProvider.ofCollection(PersonUtil.buildPersons()));
        add(grid);
    }

    private AgGrid<Person> buildAdvancedGrid() {
        AgGrid<Person> grid = new AgGrid<>();
        grid.setSizeFull();
        grid.addColumn("id",Person::getId)
                .setHeader("Id")
                .setFlex(1)
                .setSortable(true);
        grid.addColumn("firstname",Person::getFirstName)
                .setHeader("FirstName")
                .setFlex(1)
                .setCellRendererFramework("button-lit-renderer")
                .setSortable(true);
        grid.addColumn("lastname",Person::getLastName)
                .setHeader("LastName")
                .setFlex(1)
                .setCellRendererFramework("textfield-lit-renderer")
                .setSortable(true);
        grid.addColumn("age",person ->
            ((person.getId() % 2 == 0)? VaadinIcon.COMPRESS_SQUARE:VaadinIcon.COMPRESS)
                    .name().toLowerCase(Locale.ENGLISH).replace('_', '-')
        )
                .setHeader("Age")
                .setFlex(1)
                .setCellRendererFramework("icon-lit-renderer")
                .setSortable(true);
        return grid;
    }

}
