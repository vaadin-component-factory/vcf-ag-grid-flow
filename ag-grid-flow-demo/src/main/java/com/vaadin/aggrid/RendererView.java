package com.vaadin.aggrid;

import com.vaadin.aggrid.bean.Person;
import com.vaadin.flow.component.dependency.JsModule;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.data.provider.DataProvider;
import com.vaadin.flow.router.Route;
import com.vaadin.aggrid.bean.PersonUtil;

/**
 * Here is an example of different renderers for cells
 * <p><ul>
 * <li>HTML: The best renderer for IE11, but requires more js code/knowledge</li>
 * <li>Lit-Element: Seems quite performant even in IE11</li>
 * <li>Polymer Element: Slower than the others, especially in IE11</li>
 * </ul></p>
 * <p>
 * In this case, they rendered the same thing an HTML tag.
 *
 * </p>
 * @author jcgueriaud
 */
@JsModule("./src/currency-html-renderer.js")
@JsModule("./src/currency-lit-renderer.js")
@JsModule("./src/currency-polymer-renderer.js")
@Route(value = "renderer", layout = MainLayout.class)
public class RendererView extends Div {

    public RendererView() {
        setSizeFull();
        AgGrid<Person> grid = buildAdvancedGrid();
        grid.refreshColumnDefs();
        grid.setDataProvider(DataProvider.ofCollection(PersonUtil.buildPersons()));
        add(grid);
    }

    private AgGrid<Person> buildAdvancedGrid() {
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
       /* grid.addColumn("priceHtm", Person::getPrice)
                .setCellRenderer("currency-html-renderer")
                .withActionHandler("currencyClicked", this::currencyClicked)
                //.setCellRendererFramework("currency-cell-renderer")
                .setHeader("Price (H)")
                .setSortable(true);
        grid.addColumn("priceLit", Person::getPrice)
                .setCellRendererFramework("currency-lit-renderer")
                .withActionHandler("currencyClicked", this::currencyClicked)
                .setHeader("Price (L)")
                .setSortable(true);
        grid.addColumn("pricePol", Person::getPrice)
                .setCellRendererFramework("currency-polymer-renderer")
                .withActionHandler("currencyClicked", this::currencyClicked)
                .setHeader("Price (P)")
                .setSortable(true);*/
        for (int i = 0; i < 10; i++) {

            grid.addColumn("price"+i, Person::getPrice)
                    .setCellRenderer("currency-html-renderer")
                    //.setCellRendererFramework("currency-polymer-renderer")
                    .setHeader("Price"+i)
                    .setSortable(true);
        }
        return grid;
    }


    private void currencyClicked(Person person) {
        Notification.show("Person clicked "+ person.toString());
    }

}
