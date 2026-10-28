
package com.eventos.eventos_inscripciones;

import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;

@Route(value = "actividades", layout = MainLayout.class)
public class ActividadesView extends VerticalLayout {

    public ActividadesView() {
        add(
            new H2("Actividades"),
            new Paragraph("Aquí administraremos las actividades.")
        );
    }
}
