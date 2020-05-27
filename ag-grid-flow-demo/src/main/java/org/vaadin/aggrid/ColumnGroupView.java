package org.vaadin.aggrid;

import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.data.provider.DataProvider;
import com.vaadin.flow.router.Route;
import org.vaadin.aggrid.bean.Person;
import org.vaadin.aggrid.bean.PersonUtil;

/**
 * Here is an example of different levels of header.
 * <p>
 *     The API is different than Vaadin Grid *
 * </p>
 *
 * @author jcgueriaud
 */
@Route(value = "columnGroup", layout = MainLayout.class)
public class ColumnGroupView extends Div {


    public ColumnGroupView() {
        setSizeFull();
        AgGrid<Person> grid = buildGrid();
        grid.refreshColumnDefs();
        grid.setDataProvider(DataProvider.ofCollection(PersonUtil.buildPersons()));
        add(grid);
    }

    private AgGrid<Person> buildGrid() {
        AgGrid<Person> grid = new AgGrid<>();
        grid.setSizeFull();
        grid.addColumn("id",Person::getId)
                .setHeader("Id")
                .setSortable(true);
        ColumnGroup<Person> nameColumnGroup = grid.addColumnGroup().setHeader("NAME");
        nameColumnGroup.addColumn("firstname", Person::getFirstName)
                .setHeader("FirstName")
                .setSortable(true);
        nameColumnGroup.addColumn("lastname", Person::getLastName)
                .setHeader("LastName")
                .setSortable(true);

        grid.addColumn("age", Person::getAge)
                .setHeader("Age")
                .setSortable(true);
        return grid;
    }
}
