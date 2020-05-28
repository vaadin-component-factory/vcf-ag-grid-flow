# Wrapper for [Ag-grid Js Component](https://www.ag-grid.com/)

This add-on wraps the JavaScript component Ag-Grid.

This component reproduces some JAVA API of the Vaadin Grid component to displays tabular data.

It can help if you encounter problems of performance with vaadin-grid if:
* Internet Explorer 11 is a requirement
* You have a lot of columns

This add-on does not bring all the features of ag-grid.

Here is a list of features included:
* Use lazy data source with the DataProvider
* Cell renderers: The cell renderer need to be done in Javascript.
    * It can be done with Polymer, Lit or pure html. It's highly discouraged to use webcomponents inside cell renderer if you want good performance in IE11. 
* Cell Click listener
* Sorting is possible
* Column Grouping
* Frozen column on the left and right
* Dynamic Cell Css class

Here is a list of **not implemented** features:
* Edit cell
* Column reordering
* Filtering
* Row classes
* Selection
* Pivot

## Licence

This component is part of Vaadin Prime. Still, open source you need to have a valid CVAL license in order to use it. Read more at: [vaadin.com/pricing](https://vaadin.com/pricing)

The community version of Ag grid is used in this add-on.
You can read the details for the licensing [here](https://www.ag-grid.com/license-pricing.php)


## Development instructions

Starting the demo server:

Go to ag-grid-flow-demo and run:
```
mvn jetty:run
```

This deploys demo at http://localhost:8080

Test in Internet Explorer 11 / EdgeHTML:

Go to ag-grid-flow-demo and run:
```
mvn jetty:run -Dvaadin.devmode.transpile=true 
```
## Examples

TODO