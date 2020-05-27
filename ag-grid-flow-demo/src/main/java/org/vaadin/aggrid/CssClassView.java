package org.vaadin.aggrid;

import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.data.provider.DataProvider;
import com.vaadin.flow.router.Route;
import org.vaadin.aggrid.bean.Person;
import org.vaadin.aggrid.bean.PersonUtil;

/**
 * Here is an example of different ways to style your cells.
 * <p>You can:
 *     <ul>
 *         <li>Add class name for a cell or for the header</li>
 *         <li>Use an expression to display a dynamic css class name</li>
 *     </ul>
 * </p>
 *
 * @author jcgueriaud
 */
@Route(value = "cssClass", layout = MainLayout.class)
public class CssClassView extends Div {


    public CssClassView() {
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
                .setWidth(228)
                .setHeader("Id");
        grid.addColumn("firstname",Person::getFirstName)
                .setHeader("FirstName")
                .setCellClass("green");
        grid.addColumn("lastname",Person::getLastName)
                .setHeaderClass("green")
                .setHeader("LastName");
        grid.addColumn("age",Person::getAge)
                .setCellClassRules("green", "x < 20 ")
                .setCellClassRules("red", "x > 50 ")
                .setHeader("Age");
        return grid;
    }
}
