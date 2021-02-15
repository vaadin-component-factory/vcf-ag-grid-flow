package com.vaadin.aggrid;

import com.vaadin.aggrid.bean.Person;
import com.vaadin.aggrid.bean.PersonUtil;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.data.provider.DataProvider;
import com.vaadin.flow.data.provider.ListDataProvider;
import com.vaadin.flow.router.Route;

import java.util.List;

import static com.vaadin.aggrid.bean.PersonUtil.generateRandom;

/**
 * @author jcgueriaud
 */
@Route(value = "refresh", layout = MainLayout.class)
public class RefreshView extends VerticalLayout {

    private int id = 6666;

    public RefreshView() {
        setSizeFull();
        AgGrid<Person> grid = buildGrid();
        grid.refreshColumnDefs();
        List<Person> items = PersonUtil.buildPersons();
        ListDataProvider<Person> dataProvider = DataProvider.ofCollection(items);
        grid.setDataProvider(dataProvider);
        add(new Button("Add a new person and refresh all", event -> {
            id++;
            items.add(generateRandom(id));
            dataProvider.refreshAll();
        }));
        addAndExpand(grid);
    }

    private AgGrid<Person> buildGrid() {
        AgGrid<Person> grid = new AgGrid<>();
        grid.setWidthFull();
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
