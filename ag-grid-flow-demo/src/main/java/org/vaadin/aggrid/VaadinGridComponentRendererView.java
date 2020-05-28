package org.vaadin.aggrid;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.provider.DataProvider;
import com.vaadin.flow.router.Route;
import org.vaadin.aggrid.bean.Person;
import org.vaadin.aggrid.bean.PersonUtil;

/**
 * Here is another example of different renderers for cells.
 * <p>This example is using template renderer.
 * </p>
 * <p>
 *  The idea is to compare a vaadin-grid and ag-grid with the same renderers lit or polymer.
 *  <ul>
 *      <li>Performance</li>
 *      <li>Develop time / maintenance</li>
 *  </ul>
 * </p>
 *
 *  <p>On chrome: (tests are done in dev mode + inspector opened). The size of the view is also really important
 *  <ol>
 *  <li>ag-grid + only text-renderer: 1100ms</li>
 *  <li>ag-grid + lit-renderer: 1600ms</li>
 *  <li>ag-grid + polymer-renderer: 2100ms</li>
 *  <li>vaadin-grid + template-renderer: 3100ms</li>
 *  <li>vaadin-grid + component-renderer: 3500ms</li>
 *  </ol>
 *  </p>
 * @author jcgueriaud
 */
@Route(value = "component-renderer", layout = MainLayout.class)
public class VaadinGridComponentRendererView extends Div {

    public VaadinGridComponentRendererView() {
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
        grid.addComponentColumn(p -> new Button(p.getFirstName()))
                .setHeader("FirstName").setKey("firstname")
                .setSortable(true);
        grid.addComponentColumn(p -> {
            TextField textField = new TextField();
            textField.setValue(p.getLastName());
            return textField;
        })
                .setKey("lastname")
                .setHeader("LastName")
                .setSortable(true);
        grid.addComponentColumn(person -> ((person.getId() % 2 == 0)?
                VaadinIcon.COMPRESS_SQUARE:VaadinIcon.COMPRESS)
                .create())
                .setHeader("Age")
                .setKey("age")
                .setSortable(true);
        return grid;
    }
}
