package com.vaadin.aggrid;

import com.vaadin.aggrid.bean.Person;
import com.vaadin.aggrid.bean.PersonUtil;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.data.provider.DataProvider;
import com.vaadin.flow.router.Route;

/**
 * @author jcgueriaud
 */
@Route(value = "300", layout = MainLayout.class)
public class AgGrid300ColumnsView extends Div {


    public AgGrid300ColumnsView() {
        setSizeFull();
        AgGrid<Person> grid = buildGrid();
        grid.refreshColumnDefs();
        grid.setItems(DataProvider.ofCollection(PersonUtil.buildPersons()));
        add(grid);
    }

    private AgGrid<Person> buildGrid() {
        AgGrid<Person> grid = new AgGrid<>();
        grid.setSizeFull();
        grid.addColumn("id",Person::getId)
                .setFrozen(true)
                .setHeader("Id")
                .setSortable(true);
        grid.addColumn("firstname",Person::getFirstName)
                .setHeader("FirstName")
                .setSortable(true);
        for (int i = 0; i < 297; i++) {
            grid.addColumn("lastname" + i,Person::getLastName)
                .setHeader("LastName #" + i)
                .setSortable(true);
        }
        return grid;
    }
}
