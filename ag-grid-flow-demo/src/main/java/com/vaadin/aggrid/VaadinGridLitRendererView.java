package com.vaadin.aggrid;

import com.vaadin.aggrid.bean.Person;
import com.vaadin.aggrid.bean.PersonUtil;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.data.provider.DataProvider;
import com.vaadin.flow.data.renderer.LitRenderer;
import com.vaadin.flow.router.Route;

import java.util.Locale;

/**
 * Here is another example of different renderers for cells
 * <p>This example is using template renderer.
 * </p>
 * <p>
 *  The idea is to compare a vaadin-grid and ag-grid with the same renderers lit or polymer
 * </p>
 *
 * @author jcgueriaud
 */
@Route(value = "lit-renderer", layout = MainLayout.class)
public class VaadinGridLitRendererView extends Div {

    public VaadinGridLitRendererView() {
        setSizeFull();
        Grid<Person> grid = buildAdvancedGrid();
        grid.setDataProvider(DataProvider.ofCollection(PersonUtil.buildPersons()));
        add(grid);
    }

    private Grid<Person> buildAdvancedGrid() {
        Grid<Person> grid = new Grid<>();
        grid.setSizeFull();
        grid.addColumn(Person::getId)
                .setFrozen(true)
                .setHeader("Id").setKey("id")
                .setSortable(true);
        grid.addColumn(LitRenderer.<Person>of("<vaadin-button>${item.firstname}</vaadin-button>")
                .withProperty("firstname", Person::getFirstName))
                .setHeader("FirstName").setKey("firstname")
                .setSortable(true);
        grid.addColumn(LitRenderer.<Person>of("<vaadin-text-field .value=\"${item.lastname}\" />")
                .withProperty("lastname", Person::getLastName))
                .setKey("lastname")
                .setHeader("LastName")
                .setSortable(true);
        grid.addColumn(LitRenderer.<Person>of("<vaadin-icon .icon=\"${item.icon}\"></vaadin-icon>")
                .withProperty("icon", person -> "vaadin:" +
                        ((person.getId() % 2 == 0)? VaadinIcon.COMPRESS_SQUARE:VaadinIcon.COMPRESS)
                                .name().toLowerCase(Locale.ENGLISH).replace('_', '-')))
                .setHeader("Age")
                .setKey("age")
                .setSortable(true);
        return grid;
    }
}
