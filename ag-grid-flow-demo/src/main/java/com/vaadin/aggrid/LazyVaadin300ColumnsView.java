package com.vaadin.aggrid;

import com.vaadin.aggrid.bean.Person;
import com.vaadin.aggrid.bean.PersonUtil;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.data.provider.DataProvider;
import com.vaadin.flow.router.Route;

@Route(value = "vaadinlazy300", layout = MainLayout.class)
public class LazyVaadin300ColumnsView extends Div {

    public LazyVaadin300ColumnsView() {
        setSizeFull();
        Grid<Person> grid = buildAdvancedGrid();
        grid.setItems(DataProvider.ofCollection(PersonUtil.buildPersons()));
        add(grid);
    }

    private Grid<Person> buildAdvancedGrid() {
        Grid<Person> grid = new Grid<>();
        grid.setSizeFull();
        // grid.setColumnRendering(ColumnRendering.LAZY); Vaadin 24.1
        grid.addColumn(Person::getId)
                .setFrozen(true)
                .setHeader("Id").setKey("id")
                .setSortable(true);
        grid.addColumn(Person::getFirstName)
                .setHeader("FirstName").setKey("firstname")
                .setSortable(true);
        for (int i = 0; i < 297; i++) {
            grid.addColumn(Person::getLastName)
                .setKey("lastname"+i)
                .setHeader("LastName #" + i)
                .setSortable(true);
        }
        return grid;
    }
}
