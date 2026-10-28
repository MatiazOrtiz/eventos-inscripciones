
package com.eventos.eventos_inscripciones;

import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.applayout.DrawerToggle;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.RouterLink;

public class MainLayout extends AppLayout {

    public MainLayout() {
        H1 titulo = new H1("Eventos e Inscripciones");
        titulo.getStyle().set("font-size", "20px");
        titulo.getStyle().set("margin", "0");

        HorizontalLayout encabezado = new HorizontalLayout(
            new DrawerToggle(), titulo
        );

        encabezado.setAlignItems(
            HorizontalLayout.Alignment.CENTER
        );

        addToNavbar(encabezado);

        RouterLink inicio = new RouterLink("Inicio", InicioView.class);
        RouterLink eventos = new RouterLink("Eventos", EventosView.class);
        RouterLink actividades = new RouterLink("Actividades", ActividadesView.class);
        RouterLink inscripciones = new RouterLink("Inscripciones", InscripcionesView.class);

        VerticalLayout menu = new VerticalLayout(
            new Span("MENÚ PRINCIPAL"),
            inicio,
            eventos,
            actividades,
            inscripciones
        );

        menu.setPadding(true);
        menu.setSpacing(true);

        addToDrawer(menu);
    }
}
