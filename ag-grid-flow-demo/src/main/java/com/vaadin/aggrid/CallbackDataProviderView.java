package com.vaadin.aggrid;

import com.vaadin.aggrid.bean.Person;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.data.provider.DataProvider;
import com.vaadin.flow.router.Route;
import com.vaadin.aggrid.bean.PersonUtil;

import java.util.List;

/**
 * @author jcgueriaud
 */
@Route(value = "callback", layout = MainLayout.class)
public class CallbackDataProviderView extends Div {


    public CallbackDataProviderView() {
        setSizeFull();
        AgGrid<Person> grid = buildGrid();
        grid.refreshColumnDefs();
        DataProvider<Person, Void> dataProvider =
                DataProvider.fromCallbacks(
                        // First callback fetches items based on a query
                        query -> {
                            // The index of the first item to load
                            int offset = query.getOffset();

                            // The number of items to load
                            int limit = query.getLimit();
                            // build a page of random person
                            List<Person> persons = PersonUtil.buildPersons(offset, limit);

                            return persons.stream();
                        },
                        // Second callback that gives the size, 1 million hardcoded
                        query -> 10000000);
        grid.setDataProvider(dataProvider);
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
        grid.addColumn("lastname",Person::getLastName)
                .setHeader("LastName")
                .setSortable(true);
        return grid;
    }
}
