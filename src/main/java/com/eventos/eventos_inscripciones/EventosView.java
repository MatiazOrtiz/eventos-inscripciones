
package com.eventos.eventos_inscripciones;

import java.util.ArrayList;
import java.util.List;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.datetimepicker.DateTimePicker;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;

@Route(value = "eventos", layout = MainLayout.class)
public class EventosView extends VerticalLayout {

    private List<Evento> eventos = new ArrayList<>();
    private Grid<Evento> tabla = new Grid<>(Evento.class, false);

    private int siguienteId = 1;

    public EventosView() {

        H2 titulo = new H2("Gestión de eventos");

        TextField nombre = new TextField("Nombre del evento");
        nombre.setWidthFull();

        ComboBox<String> categoria = new ComboBox<>("Categoría");
        categoria.setItems(
            "Académico",
            "Cultural",
            "Deportivo",
            "Social",
            "Tecnológico"
        );
        categoria.setWidthFull();

        DateTimePicker fechaHora = new DateTimePicker("Fecha y hora");
        fechaHora.setWidthFull();

        TextField lugar = new TextField("Lugar");
        lugar.setWidthFull();

        IntegerField capacidad = new IntegerField("Capacidad");
        capacidad.setMin(1);
        capacidad.setStepButtonsVisible(true);
        capacidad.setWidthFull();

        TextArea descripcion = new TextArea("Descripción");
        descripcion.setWidthFull();
        descripcion.setMinHeight("100px");

        Button guardar = new Button("Registrar evento");

        guardar.addClickListener(event -> {

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

            Evento nuevoEvento = new Evento(
                siguienteId,
                nombre.getValue(),
                categoria.getValue(),
                fechaHora.getValue(),
                lugar.getValue(),
                capacidad.getValue(),
                descripcion.getValue()
            );

            eventos.add(nuevoEvento);
            siguienteId++;

            tabla.setItems(eventos);

            nombre.clear();
            categoria.clear();
            fechaHora.clear();
            lugar.clear();
            capacidad.clear();
            descripcion.clear();

            Notification.show("Evento registrado correctamente.");

        });

        tabla.addColumn(Evento::getId)
                .setHeader("ID")
                .setAutoWidth(true);

        tabla.addColumn(Evento::getNombre)
                .setHeader("Nombre");

        tabla.addColumn(Evento::getCategoria)
                .setHeader("Categoría");

        tabla.addColumn(Evento::getFechaHora)
                .setHeader("Fecha y hora");

        tabla.addColumn(Evento::getLugar)
                .setHeader("Lugar");

        tabla.addColumn(Evento::getCapacidad)
                .setHeader("Capacidad")
                .setAutoWidth(true);

        tabla.addColumn(Evento::getDescripcion)
                .setHeader("Descripción");

        tabla.addThemeVariants(GridVariant.LUMO_WRAP_CELL_CONTENT);

        tabla.setAllRowsVisible(true);
        tabla.setWidthFull();

        add(
            titulo,
            nombre,
            categoria,
            fechaHora,
            lugar,
            capacidad,
            descripcion,
            guardar,
            new H2("Eventos registrados"),
            tabla
        );

        setSpacing(true);
        setPadding(true);
        setWidthFull();
    }
}
