package com.vaadin.aggrid;

import com.vaadin.aggrid.bean.Person;
import com.vaadin.aggrid.bean.PersonUtil;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.provider.DataProvider;
import com.vaadin.flow.router.Route;

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
 *  <p>On chrome: (tests are done in dev mode + inspector opened
 *  so it's really slower than usual).
 *  The size of the view is also really important.
 *  30 columns of Vaadin textfield
 *  <ol>
 *  <li>ag-grid + lit-renderer: 3100ms</li>
 *  <li>vaadin-grid + template-renderer: 24000ms</li>
 *  <li>vaadin-grid + component-renderer: 19000ms</li>
 *  </ol>
 *
 *  30 columns of Span
 *  <ol>
 *  <li>ag-grid + lit-renderer: 1000ms</li>
 *  <li>vaadin-grid + template-renderer: 4500ms</li>
 *  <li>vaadin-grid + component-renderer: 7600ms</li>
 *  </ol>
 *  </p>
 * @author jcgueriaud
 */
@Route(value = "perf-component-renderer", layout = MainLayout.class)
public class VaadinGridPerfComponentRendererView extends Div {

    public VaadinGridPerfComponentRendererView() {
        setSizeFull();
        Grid<Person> grid = buildAdvancedGrid();
        grid.setDataProvider(DataProvider.ofCollection(PersonUtil.buildPersons()));
        add(grid);
    }

    private Grid<Person> buildAdvancedGrid() {
        Grid<Person> grid = new Grid<>();
        grid.setSizeFull();
        for (int i = 0; i < 30; i++) {
            grid.addComponentColumn(p -> {
                TextField textField = new TextField();
                textField.setValue(p.getFirstName());
                return textField;
            }).setHeader("Id"+i)
                .setKey("id"+i);
        }
        return grid;
    }
}
