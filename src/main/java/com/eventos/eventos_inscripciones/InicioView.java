
package com.eventos.eventos_inscripciones;

import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;

@Route(value = "", layout = MainLayout.class)
public class InicioView extends VerticalLayout {

    public InicioView() {
        H1 titulo = new H1("Eventos e Inscripciones");
        H2 subtitulo = new H2("Sistema de gestión de eventos");

        Paragraph descripcion = new Paragraph(
            "Bienvenido al sistema de gestión de eventos e inscripciones."
        );

        Paragraph mensaje = new Paragraph(
            "Aquí podrás administrar eventos, actividades e inscripciones."
        );

        add(titulo, subtitulo, descripcion, mensaje);

        setAlignItems(Alignment.CENTER);
        setSpacing(true);
        setPadding(true);
    }
}
    