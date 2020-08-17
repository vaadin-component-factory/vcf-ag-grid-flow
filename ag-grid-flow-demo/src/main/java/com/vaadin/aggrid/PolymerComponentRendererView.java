package com.vaadin.aggrid;

import com.vaadin.aggrid.bean.Person;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.JsModule;
import com.vaadin.flow.component.dependency.Uses;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.provider.DataProvider;
import com.vaadin.flow.router.Route;
import com.vaadin.aggrid.bean.PersonUtil;

import java.util.Locale;

/**
 * Here is another example of different renderers for cells
 * <p>This example is using polymer-element but it can be renderer as html or polymer.
 * </p>
 * <p>
 *  The idea is to compare a vaadin-grid and ag-grid with the same renderers lit or polymer
 * </p>
 * @author jcgueriaud
 */
@JsModule("./src/textfield-polymer-renderer.js")
@JsModule("./src/button-polymer-renderer.js")
@JsModule("./src/icon-polymer-renderer.js")
@Uses(Button.class)
@Uses(Icon.class)
@Uses(TextField.class)
@Route(value = "polymer-component-renderer", layout = MainLayout.class)
public class PolymerComponentRendererView extends Div {

    public PolymerComponentRendererView() {
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
                .setFlex(1)
                .setHeader("Id")
                .setSortable(true);
        grid.addColumn("firstname",Person::getFirstName)
                .setHeader("FirstName")
                .setFlex(1)
                .setCellRendererFramework("button-polymer-renderer")
                .setSortable(true);
        grid.addColumn("lastname",Person::getLastName)
                .setHeader("LastName")
                .setFlex(1)
                .setCellRendererFramework("textfield-polymer-renderer")
                .setSortable(true);
        grid.addColumn("age",person ->
            ((person.getId() % 2 == 0)? VaadinIcon.COMPRESS_SQUARE:VaadinIcon.COMPRESS)
                    .name().toLowerCase(Locale.ENGLISH).replace('_', '-')
        )
                .setFlex(1)
                .setHeader("Age")
                .setCellRendererFramework("icon-polymer-renderer")
                .setSortable(true);
        return grid;
    }


    private void currencyClicked(Person person) {
        Notification.show("Person clicked "+ person.toString());
    }

}
