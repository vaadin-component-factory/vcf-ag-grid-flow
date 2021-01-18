package com.vaadin.aggrid;

import com.vaadin.aggrid.bean.Person;
import com.vaadin.aggrid.bean.PersonUtil;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.data.provider.DataProvider;
import com.vaadin.flow.data.renderer.TemplateRenderer;
import com.vaadin.flow.router.Route;

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
@Route(value = "perf-template-renderer", layout = MainLayout.class)
public class VaadinGridPerfTemplateRendererView extends Div {

    public VaadinGridPerfTemplateRendererView() {
        setSizeFull();
        Grid<Person> grid = buildAdvancedGrid();
        grid.setDataProvider(DataProvider.ofCollection(PersonUtil.buildPersons()));
        add(grid);
    }

    private Grid<Person> buildAdvancedGrid() {
        Grid<Person> grid = new Grid<>();
        grid.setSizeFull();
        for (int i = 0; i < 30; i++) {
            grid.addColumn(TemplateRenderer.<Person>of("<vaadin-text-field value=\"[[item.firstname]]\"></vaadin-text-field>")
                .withProperty("firstname", Person::getFirstName))
                .setHeader("Id"+i)
                .setKey("id"+i);
        }
        return grid;
    }
}
