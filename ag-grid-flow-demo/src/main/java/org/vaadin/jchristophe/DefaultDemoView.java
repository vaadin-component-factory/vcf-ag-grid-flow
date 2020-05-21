package org.vaadin.jchristophe;

import com.vaadin.flow.component.dependency.JsModule;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.data.provider.DataProvider;
import com.vaadin.flow.demo.DemoView;
import com.vaadin.flow.router.Route;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@JsModule("./src/ag-renderer.js")
@Route("")
public class DefaultDemoView extends DemoView {

    @Override
    protected void initView() {
        getElement().setAttribute("style","max-width:90%;");
        createBasicExample();
        createAdvancedExample();
    }


    private void createBasicExample() {
        Div message = createMessageDiv("basic-message");

        // begin-source-example
        // source-example-heading: Basic 1
        AgGrid<Person> grid = buildGrid();
        grid.refreshColumnDefs();
        grid.setDataProvider(DataProvider.ofCollection(buildPersons()));
        // end-source-example

        addCard("Basic Example", "Basic 1", grid, message);
    }

    private void createAdvancedExample() {
        Div message = createMessageDiv("advanced-message");

        // begin-source-example
        // source-example-heading: Advanced 1
        AgGrid<Person> grid = buildAdvancedGrid();
        grid.refreshColumnDefs();
        grid.setDataProvider(DataProvider.ofCollection(buildPersons()));
        // end-source-example

        addCard("Advanced Example", "Advanced 1", grid, message);
    }

    private AgGrid<Person> buildGrid() {
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
        return grid;
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
                .setCellRendererFramework("simple-greeting")
                .setHeader("LastName")
                .setSortable(true);
        grid.addColumn("age", Person::getAge)
                 .setCellRenderer("AdultRenderer")
                //.setCellRendererFramework("currency-cell-renderer")
                .setHeader("Age")
                .setSortable(true);

        grid.addColumn("price", Person::getPrice)
                //.setCellRenderer("AdultRenderer")
                .setCellRendererFramework("currency-cell-renderer")
                .setHeader("Price")
                .setSortable(true);




        return grid;
    }

    private List<Person> buildPersons() {
        List<Person> persons = new ArrayList<>();
        for (int i = 0; i < 1000; i++) {
            persons.add(generateRandom(i));
        }
        return persons;
    }

    private Div createMessageDiv(String id) {
        Div message = new Div();
        message.setId(id);
        message.getStyle().set("whiteSpace", "pre");
        return message;
    }

    public static Person generateRandom(int id) {
        Person person = new Person();
        person.setId(id);
        person.setFirstName(generateRandomString());
        person.setLastName(generateRandomString());
        person.setAge(generateRandomInt(99));
        person.setPrice(new Price(generateRandomInt(1000)));
        return person;
    }

    private static String generateRandomString(){
        int leftLimit = 97; // letter 'a'
        int rightLimit = 122; // letter 'z'
        int targetStringLength = 10;
        Random random = new Random();

        return random.ints(leftLimit, rightLimit + 1)
                .limit(targetStringLength)
                .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                .toString();
    }


    private static int generateRandomInt(int bound){
        Random random = new Random();
        return random.nextInt(bound);
    }
}
