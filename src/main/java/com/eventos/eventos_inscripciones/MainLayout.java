
package com.eventos.eventos_inscripciones;

import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.applayout.DrawerToggle;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.sidenav.SideNav;
import com.vaadin.flow.component.sidenav.SideNavItem;

public class MainLayout extends AppLayout {

    public MainLayout() {

        DrawerToggle botonMenu = new DrawerToggle();

        H1 titulo = new H1("Gestión de Eventos");
        titulo.getStyle()
                .set("font-size", "clamp(18px, 3vw, 26px)")
                .set("margin", "0")
                .set("white-space", "nowrap");

        SideNav menu = new SideNav();

        menu.setWidthFull();

        menu.addItem(new SideNavItem("Eventos", EventosView.class));
        menu.addItem(new SideNavItem("Actividades", ActividadesView.class));
        menu.addItem(new SideNavItem("Inscripciones", InscripcionesView.class));
        menu.addItem(new SideNavItem("Asistencias", AsistenciasView.class));
        menu.addItem(new SideNavItem("Estadísticas", EstadisticasView.class));
        menu.addItem(new SideNavItem("Certificados", CertificadosView.class));

        VerticalLayout contenido = new VerticalLayout(menu);

        contenido.setWidthFull();
        contenido.setPadding(true);
        contenido.setSpacing(true);

        addToNavbar(botonMenu, titulo);
        addToDrawer(contenido);
    }
}
