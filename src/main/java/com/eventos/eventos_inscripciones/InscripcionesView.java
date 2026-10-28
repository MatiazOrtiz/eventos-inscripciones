
package com.eventos.eventos_inscripciones;

import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;

@Route(value = "inscripciones", layout = MainLayout.class)
public class InscripcionesView extends VerticalLayout {

    public InscripcionesView() {
        add(
            new H2("Inscripciones"),
            new Paragraph("Aquí administraremos las inscripciones.")
        );
    }
}
