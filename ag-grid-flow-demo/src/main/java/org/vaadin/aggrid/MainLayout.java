package org.vaadin.aggrid;

import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.applayout.DrawerToggle;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.RouterLink;

/**
 * @author jcgueriaud
 */
@CssImport("./demo.css")
public class MainLayout extends AppLayout {

    public MainLayout() {
        final DrawerToggle drawerToggle = new DrawerToggle();
        final RouterLink simple = new RouterLink("Simple", SimpleView.class);
        final RouterLink renderer = new RouterLink("Renderer", RendererView.class);
        final RouterLink columnGroup = new RouterLink("Column Group", ColumnGroupView.class);
        final VerticalLayout menuLayout = new VerticalLayout(simple, renderer, columnGroup);
        menuLayout.add(new RouterLink("Width", WidthView.class));
        menuLayout.add(new RouterLink("Css class", CssClassView.class));
        menuLayout.add(new RouterLink("Frozen columns", FrozenView.class));
        menuLayout.add(new RouterLink("Lit Component Renderer", LitComponentRendererView.class));
        menuLayout.add(new RouterLink("Polymer Component Renderer", PolymerComponentRendererView.class));
        menuLayout.add(new RouterLink("Vaadin Template Renderer", VaadinGridTemplateRendererView.class));
        menuLayout.add(new RouterLink("Vaadin Component Renderer", VaadinGridComponentRendererView.class));
        addToDrawer(menuLayout);
        addToNavbar(drawerToggle);
    }

}