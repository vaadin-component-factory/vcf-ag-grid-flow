package org.vaadin.aggrid;

import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.data.provider.DataProvider;
import com.vaadin.flow.router.Route;
import org.vaadin.aggrid.bean.Person;
import org.vaadin.aggrid.bean.PersonUtil;

/**
 * @author jcgueriaud
 */
@Route(value = "frozen", layout = MainLayout.class)
public class FrozenView extends Div {


    public FrozenView() {
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
                .setFrozen(true)
                .setHeader("Id");
        for (int i = 0; i < 10; i++) {
            grid.addColumn("firstname"+i,Person::getFirstName)
                    .setHeader("FirstName");
        }
        grid.addColumn("lastname (frozen)",Person::getLastName)
                .setHeader("LastName")
                .setFrozen(AbstractColumn.FrozenColumn.right);
        return grid;
    }
}
