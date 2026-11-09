package com.eventos.eventos_inscripciones;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.confirmdialog.ConfirmDialog;
import com.vaadin.flow.component.datetimepicker.DateTimePicker;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.FlexLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;

@Route(value = "actividades", layout = MainLayout.class)
public class ActividadesView extends VerticalLayout {

    private List<Actividad> actividades = new ArrayList<>();
    private List<Evento> eventos = new ArrayList<>();

    private Grid<Actividad> tabla = new Grid<>(Actividad.class, false);

    private int siguienteId = 1;

    public ActividadesView() {

        addClassName("eventos-pagina");

        // Cargar datos

        try {

            actividades = ArchivoActividades.cargarActividades();
            eventos = ArchivoEventos.cargarEventos();

            for (Actividad actividad : actividades) {

                if (actividad.getId() >= siguienteId) {
                    siguienteId = actividad.getId() + 1;
                }
            }

        } catch (IOException e) {

            Notification.show("Error al cargar los datos.");
        }

        // Encabezado

        H2 titulo = new H2("Gestión de actividades");

        Paragraph subtitulo = new Paragraph(
            "Administra las actividades de los eventos registrados."
        );

        // Formulario

        TextField nombre = new TextField("Nombre de la actividad");
        nombre.setWidthFull();

        TextArea descripcion = new TextArea("Descripción");
        descripcion.setWidthFull();
        descripcion.setMinHeight("90px");

        ComboBox<Evento> evento = new ComboBox<>("Evento asociado");
        evento.setItems(eventos);
        evento.setItemLabelGenerator(Evento::getNombre);
        evento.setWidthFull();

        DateTimePicker fechaInicio = new DateTimePicker("Fecha y hora de inicio");
        fechaInicio.setWidthFull();

        DateTimePicker fechaFin = new DateTimePicker("Fecha y hora de finalización");
        fechaFin.setWidthFull();

        // Filas adaptables

        FlexLayout fila1 = new FlexLayout(nombre, evento);
        fila1.setWidthFull();
        fila1.setFlexWrap(FlexLayout.FlexWrap.WRAP);
        fila1.setAlignItems(FlexComponent.Alignment.CENTER);
        fila1.addClassName("fila-responsive");

        nombre.setMinWidth("220px");
        evento.setMinWidth("220px");

        fila1.setFlexGrow(1, nombre, evento);

        FlexLayout fila2 = new FlexLayout(fechaInicio, fechaFin);
        fila2.setWidthFull();
        fila2.setFlexWrap(FlexLayout.FlexWrap.WRAP);
        fila2.setAlignItems(FlexComponent.Alignment.CENTER);
        fila2.addClassName("fila-responsive");

        fechaInicio.setMinWidth("220px");
        fechaFin.setMinWidth("220px");

        fila2.setFlexGrow(1, fechaInicio, fechaFin);

        // Botones

        Button guardar = new Button("Registrar actividad");
        guardar.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

        Button editar = new Button("Editar seleccionado");
        Button actualizar = new Button("Guardar cambios");
        Button cancelar = new Button("Cancelar edición");
        Button eliminar = new Button("Eliminar seleccionado");

        actualizar.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        eliminar.addThemeVariants(ButtonVariant.LUMO_ERROR);

        editar.setEnabled(false);
        actualizar.setEnabled(false);
        cancelar.setEnabled(false);
        eliminar.setEnabled(false);

        FlexLayout botones = new FlexLayout(
            editar, actualizar, cancelar, eliminar
        );

        botones.setWidthFull();
        botones.setFlexWrap(FlexLayout.FlexWrap.WRAP);
        botones.addClassName("botones-edicion");

        // Tarjeta del formulario

        VerticalLayout formulario = new VerticalLayout(
            new H2("Registrar actividad"),
            nombre,
            descripcion,
            fila1,
            fila2,
            guardar,
            botones
        );

        formulario.addClassName("tarjeta-eventos");
        formulario.setWidthFull();
        formulario.setPadding(true);
        formulario.setSpacing(true);

        // Tabla

        configurarTabla();

        tabla.setItems(actividades);

        VerticalLayout tarjetaTabla = new VerticalLayout(
            new H2("Actividades registradas"),
            tabla
        );

        tarjetaTabla.addClassName("tarjeta-eventos");
        tarjetaTabla.setWidthFull();
        tarjetaTabla.setPadding(true);

        // Registrar actividad

        guardar.addClickListener(e -> {

            if (nombre.isEmpty()
                    || evento.isEmpty()
                    || fechaInicio.isEmpty()
                    || fechaFin.isEmpty()) {

                Notification.show("Completa todos los campos obligatorios.");
                return;
            }

            if (!fechaFin.getValue().isAfter(fechaInicio.getValue())) {

                Notification.show("La fecha final debe ser posterior al inicio.");
                return;
            }

            Actividad nueva = new Actividad(
                siguienteId,
                evento.getValue().getId(),
                nombre.getValue(),
                descripcion.getValue(),
                fechaInicio.getValue(),
                fechaFin.getValue()
            );

            if (hayCruce(nueva, null)) {

                Notification.show("El horario se cruza con otra actividad del evento.");
                return;
            }

            actividades.add(nueva);
            siguienteId++;

            try {

                ArchivoActividades.guardarActividades(actividades);

            } catch (IOException ex) {

                actividades.remove(nueva);
                siguienteId--;

                Notification.show("Error al guardar la actividad.");
                return;
            }

            actualizarTabla();

            limpiarFormulario(nombre, descripcion, evento, fechaInicio, fechaFin);

            Notification.show("Actividad registrada correctamente.");
        });

        // Seleccionar actividad

        tabla.asSingleSelect().addValueChangeListener(e -> {

            boolean seleccionado = e.getValue() != null;

            editar.setEnabled(seleccionado);
            eliminar.setEnabled(seleccionado);
        });

        // Editar actividad

        editar.addClickListener(e -> {

            Actividad seleccionada = tabla.asSingleSelect().getValue();

            if (seleccionada != null) {

                nombre.setValue(seleccionada.getNombre());
                descripcion.setValue(seleccionada.getDescripcion());

                for (Evento item : eventos) {

                    if (item.getId() == seleccionada.getIdEvento()) {
                        evento.setValue(item);
                        break;
                    }
                }

                fechaInicio.setValue(seleccionada.getFechaInicio());
                fechaFin.setValue(seleccionada.getFechaFin());

                guardar.setEnabled(false);
                editar.setEnabled(false);
                eliminar.setEnabled(false);
                actualizar.setEnabled(true);
                cancelar.setEnabled(true);
            }
        });

        // Actualizar actividad

        actualizar.addClickListener(e -> {

            Actividad seleccionada = tabla.asSingleSelect().getValue();

            if (seleccionada == null) {
                return;
            }

            if (nombre.isEmpty()
                    || evento.isEmpty()
                    || fechaInicio.isEmpty()
                    || fechaFin.isEmpty()) {

                Notification.show("Completa todos los campos obligatorios.");
                return;
            }

            if (!fechaFin.getValue().isAfter(fechaInicio.getValue())) {

                Notification.show("La fecha final debe ser posterior al inicio.");
                return;
            }

            Actividad modificada = new Actividad(
                seleccionada.getId(),
                evento.getValue().getId(),
                nombre.getValue(),
                descripcion.getValue(),
                fechaInicio.getValue(),
                fechaFin.getValue()
            );

            if (hayCruce(modificada, seleccionada.getId())) {

                Notification.show("El horario se cruza con otra actividad del evento.");
                return;
            }

            int posicion = actividades.indexOf(seleccionada);

            actividades.set(posicion, modificada);

            try {

                ArchivoActividades.guardarActividades(actividades);

            } catch (IOException ex) {

                actividades.set(posicion, seleccionada);

                Notification.show("Error al actualizar la actividad.");
                return;
            }

            actualizarTabla();

            limpiarFormulario(nombre, descripcion, evento, fechaInicio, fechaFin);

            tabla.asSingleSelect().clear();

            guardar.setEnabled(true);
            actualizar.setEnabled(false);
            cancelar.setEnabled(false);

            Notification.show("Actividad actualizada correctamente.");
        });

        // Cancelar edición

        cancelar.addClickListener(e -> {

            limpiarFormulario(nombre, descripcion, evento, fechaInicio, fechaFin);

            tabla.asSingleSelect().clear();

            guardar.setEnabled(true);
            actualizar.setEnabled(false);
            cancelar.setEnabled(false);
        });

        // Eliminar actividad

        eliminar.addClickListener(e -> {

            Actividad seleccionada = tabla.asSingleSelect().getValue();

            if (seleccionada == null) {
                return;
            }

            ConfirmDialog confirmacion = new ConfirmDialog();

            confirmacion.setHeader("Eliminar actividad");
            confirmacion.setText("¿Estás seguro de eliminar esta actividad?");
            confirmacion.setCancelable(true);
            confirmacion.setCancelText("Cancelar");
            confirmacion.setConfirmText("Eliminar");
            confirmacion.setConfirmButtonTheme("error primary");

            confirmacion.addConfirmListener(event -> {

                actividades.remove(seleccionada);

                try {

                    ArchivoActividades.guardarActividades(actividades);

                } catch (IOException ex) {

                    actividades.add(seleccionada);

                    Notification.show("Error al eliminar la actividad.");
                    actualizarTabla();
                    return;
                }

                actualizarTabla();
                tabla.asSingleSelect().clear();

                Notification.show("Actividad eliminada correctamente.");
            });

            confirmacion.open();
        });

        // Agregar componentes

        add(titulo, subtitulo, formulario, tarjetaTabla);

        setWidthFull();
        setMaxWidth("1500px");
        getStyle().set("margin", "0 auto");
        setPadding(true);
        setSpacing(true);
    }

    // Configurar tabla

    private void configurarTabla() {

        tabla.addColumn(Actividad::getId)
                .setHeader("ID")
                .setAutoWidth(true);

        tabla.addColumn(Actividad::getIdEvento)
                .setHeader("ID Evento")
                .setAutoWidth(true);

        tabla.addColumn(Actividad::getNombre)
                .setHeader("Nombre")
                .setAutoWidth(true);

        tabla.addColumn(Actividad::getFechaInicio)
                .setHeader("Inicio")
                .setAutoWidth(true);

        tabla.addColumn(Actividad::getFechaFin)
                .setHeader("Finalización")
                .setAutoWidth(true);

        tabla.addColumn(Actividad::getDescripcion)
                .setHeader("Descripción");

        tabla.addThemeVariants(GridVariant.LUMO_WRAP_CELL_CONTENT);

        tabla.setAllRowsVisible(true);
        tabla.setWidthFull();
        tabla.setMinWidth("700px");
    }

    // Comprobar horarios superpuestos

    private boolean hayCruce(Actividad nueva, Integer idIgnorado) {

        for (Actividad actividad : actividades) {

            if (idIgnorado != null && actividad.getId() == idIgnorado) {
                continue;
            }

            if (actividad.getIdEvento() == nueva.getIdEvento()) {

                boolean seCruzan =
                    nueva.getFechaInicio().isBefore(actividad.getFechaFin())
                    && nueva.getFechaFin().isAfter(actividad.getFechaInicio());

                if (seCruzan) {
                    return true;
                }
            }
        }

        return false;
    }

    // Actualizar tabla

    private void actualizarTabla() {

        tabla.setItems(new ArrayList<>(actividades));
    }

    // Limpiar formulario

    private void limpiarFormulario(
            TextField nombre,
            TextArea descripcion,
            ComboBox<Evento> evento,
            DateTimePicker fechaInicio,
            DateTimePicker fechaFin) {

        nombre.clear();
        descripcion.clear();
        evento.clear();
        fechaInicio.clear();
        fechaFin.clear();
    }
}