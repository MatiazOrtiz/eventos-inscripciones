
package com.eventos.eventos_inscripciones;

import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;

@Route(value = "eventos", layout = MainLayout.class)
public class EventosView extends VerticalLayout {

    public EventosView() {
        add(
            new H2("Eventos"),
            new Paragraph("Aquí administraremos los eventos.")
        );
    }
}
