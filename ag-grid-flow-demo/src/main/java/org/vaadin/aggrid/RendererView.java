package org.vaadin.aggrid;

import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.data.provider.DataProvider;
import com.vaadin.flow.router.Route;
import org.vaadin.aggrid.bean.Person;
import org.vaadin.aggrid.bean.PersonUtil;

/**
 * @author jcgueriaud
 */
@Route(value = "renderer", layout = MainLayout.class)
public class RendererView extends Div {


    public RendererView() {
        AgGrid<Person> grid = buildAdvancedGrid();
        grid.refreshColumnDefs();
        grid.setDataProvider(DataProvider.ofCollection(PersonUtil.buildPersons()));
        add(grid);
    }

    private AgGrid<Person> buildAdvancedGrid() {
        AgGrid<Person> grid = new AgGrid<>();
        grid.setHeight("1000px");
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
 /*       grid.addColumn("priceHtm", Person::getPrice)
                .setCellRenderer("currency-html-renderer")
                .withActionHandler("currencyClicked", this::currencyClicked)
                //.setCellRendererFramework("currency-cell-renderer")
                .setHeader("Price (H)")
                .setSortable(true);
        grid.addColumn("priceLit", Person::getPrice)
                //.setCellRenderer("AdultRenderer")
                .setCellRendererFramework("currency-lit-renderer")
                .withActionHandler("currencyClicked", this::currencyClicked)
                .setHeader("Price (L)")
                .setSortable(true);
        grid.addColumn("pricePol", Person::getPrice)
                //.setCellRenderer("AdultRenderer")
                .setCellRendererFramework("currency-polymer-renderer")
                .withActionHandler("currencyClicked", this::currencyClicked)
                .setHeader("Price (P)")
                .setSortable(true);
*/
        for (int i = 0; i < 10; i++) {

            grid.addColumn("price"+i, Person::getPrice)
                    //.setCellRenderer("currency-html-renderer")
                    .setCellRendererFramework("currency-lit-renderer")
                    .setHeader("Price"+i)
                    .setSortable(true);
        }

        return grid;
    }


    private void currencyClicked(Person person) {
        Notification.show("Person clicked "+ person.toString());
    }

}
