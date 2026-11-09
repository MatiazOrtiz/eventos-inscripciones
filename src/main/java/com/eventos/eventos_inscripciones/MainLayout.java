
package com.eventos.eventos_inscripciones;

import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.sidenav.SideNav;
import com.vaadin.flow.component.sidenav.SideNavItem;

public class MainLayout extends AppLayout {

    public MainLayout() {

        H1 titulo = new H1("Gestión de Eventos");

        SideNav menu = new SideNav();

        menu.addItem(new SideNavItem("Eventos", EventosView.class));
        menu.addItem(new SideNavItem("Actividades", ActividadesView.class));
        menu.addItem(new SideNavItem("Inscripciones", InscripcionesView.class));
        menu.addItem(new SideNavItem("Asistencias", AsistenciasView.class));

        VerticalLayout contenido = new VerticalLayout(titulo, menu);

        contenido.setPadding(true);
        contenido.setSpacing(true);

        addToNavbar(titulo);
        addToDrawer(menu);
    }
}
