package org.vaadin.aggrid;

import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.applayout.DrawerToggle;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.RouterLink;

/**
 * @author jcgueriaud
 */
public class MainLayout extends AppLayout {

    private final Div leftMenu = new Div();
    private final Div mainContent = new Div();

    public MainLayout() {
        final DrawerToggle drawerToggle = new DrawerToggle();
        final RouterLink home = new RouterLink("Simple", SimpleView.class);
        final RouterLink about = new RouterLink("Renderer", RendererView.class);
        final VerticalLayout menuLayout = new VerticalLayout(home, about);
        addToDrawer(menuLayout);
        addToNavbar(drawerToggle);
    }

}