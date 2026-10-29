package com.eventos.eventos_inscripciones;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.datetimepicker.DateTimePicker;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;

@Route(value = "eventos", layout = MainLayout.class)
public class EventosView extends VerticalLayout {

    public EventosView() {

        H2 titulo = new H2("Gestión de eventos");

        TextField nombre = new TextField("Nombre del evento");

        ComboBox<String> categoria = new ComboBox<>("Categoría");
        categoria.setItems(
            "Académico",
            "Cultural",
            "Deportivo",
            "Social",
            "Tecnológico"
        );

        DateTimePicker fechaHora = new DateTimePicker("Fecha y hora");

        TextField lugar = new TextField("Lugar");

        IntegerField capacidad = new IntegerField("Capacidad");
        capacidad.setMin(1);
        capacidad.setStepButtonsVisible(true);

        TextArea descripcion = new TextArea("Descripción");

        Button guardar = new Button("Registrar evento", evento -> {

            if (nombre.isEmpty()
                    || categoria.isEmpty()
                    || fechaHora.isEmpty()
                    || lugar.isEmpty()
                    || capacidad.isEmpty()) {

                Notification.show("Completa todos los campos obligatorios.");
                return;
            }

            if (capacidad.getValue() <= 0) {
                Notification.show("La capacidad debe ser mayor que cero.");
                return;
            }

            Notification.show("Formulario validado correctamente.");

        });

        add(
            titulo,
            nombre,
            categoria,
            fechaHora,
            lugar,
            capacidad,
            descripcion,
            guardar
        );

        setSpacing(true);
        setPadding(true);
        setMaxWidth("600px");
    }
}