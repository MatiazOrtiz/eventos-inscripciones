
package com.eventos.eventos_inscripciones;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.confirmdialog.ConfirmDialog;
import com.vaadin.flow.component.datetimepicker.DateTimePicker;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;

@Route(value = "eventos", layout = MainLayout.class)
public class EventosView extends VerticalLayout {

    private List<Evento> eventos = new ArrayList<>();

    private Grid<Evento> tablaRegistrados = new Grid<>(Evento.class, false);
    private Grid<Evento> tablaResultados = new Grid<>(Evento.class, false);

    private int siguienteId = 1;
    private boolean busquedaRealizada = false;

    public EventosView() {

        // Formulario de registro

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

        // Botones para actualizar y eliminar

        Button editar = new Button("Editar seleccionado");
        Button actualizar = new Button("Actualizar cambios");
        Button cancelar = new Button("Cancelar edición");
        Button eliminar = new Button("Eliminar seleccionado");

        editar.setEnabled(false);
        actualizar.setEnabled(false);
        cancelar.setEnabled(false);
        eliminar.setEnabled(false);

        HorizontalLayout botonesEdicion = new HorizontalLayout(
            editar, actualizar, cancelar, eliminar
        );

        // Formulario de búsqueda

        H2 tituloBusqueda = new H2("Buscar eventos");

        TextField buscarNombre = new TextField("Nombre del evento");
        buscarNombre.setPlaceholder("Escribe el nombre que deseas buscar...");
        buscarNombre.setClearButtonVisible(true);
        buscarNombre.setWidthFull();

        ComboBox<String> filtroCategoria = new ComboBox<>("Categoría");
        filtroCategoria.setItems(
            "Académico",
            "Cultural",
            "Deportivo",
            "Social",
            "Tecnológico"
        );
        filtroCategoria.setPlaceholder("Todas las categorías");
        filtroCategoria.setClearButtonVisible(true);
        filtroCategoria.setWidthFull();

        Button buscarEventos = new Button("Buscar eventos");
        Button mostrarTodos = new Button("Mostrar todos");

        // Configurar las tablas

        configurarTabla(tablaRegistrados);
        configurarTabla(tablaResultados);

        // Registrar evento

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

            tablaRegistrados.setItems(eventos);

            if (busquedaRealizada) {
                actualizarResultados(buscarNombre, filtroCategoria);
            }

            limpiarFormulario(
                nombre, categoria, fechaHora,
                lugar, capacidad, descripcion
            );

            Notification.show("Evento registrado correctamente.");

        });

        // Seleccionar un evento de la tabla

        tablaRegistrados.asSingleSelect().addValueChangeListener(event -> {

            boolean seleccionado = event.getValue() != null;

            editar.setEnabled(seleccionado);
            eliminar.setEnabled(seleccionado);

        });

        // Cargar los datos del evento en el formulario

        editar.addClickListener(event -> {

            Evento seleccionado = tablaRegistrados.asSingleSelect().getValue();

            if (seleccionado != null) {

                nombre.setValue(seleccionado.getNombre());
                categoria.setValue(seleccionado.getCategoria());
                fechaHora.setValue(seleccionado.getFechaHora());
                lugar.setValue(seleccionado.getLugar());
                capacidad.setValue(seleccionado.getCapacidad());
                descripcion.setValue(seleccionado.getDescripcion());

                guardar.setEnabled(false);
                editar.setEnabled(false);
                eliminar.setEnabled(false);

                actualizar.setEnabled(true);
                cancelar.setEnabled(true);

                Notification.show("Puedes modificar los datos del evento.");

            }

        });

        // Guardar los cambios del evento

        actualizar.addClickListener(event -> {

            Evento seleccionado = tablaRegistrados.asSingleSelect().getValue();

            if (seleccionado == null) {
                Notification.show("Selecciona un evento.");
                return;
            }

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

            seleccionado.setNombre(nombre.getValue());
            seleccionado.setCategoria(categoria.getValue());
            seleccionado.setFechaHora(fechaHora.getValue());
            seleccionado.setLugar(lugar.getValue());
            seleccionado.setCapacidad(capacidad.getValue());
            seleccionado.setDescripcion(descripcion.getValue());

            tablaRegistrados.setItems(new ArrayList<>(eventos));

            if (busquedaRealizada) {
                actualizarResultados(buscarNombre, filtroCategoria);
            }

            limpiarFormulario(
                nombre, categoria, fechaHora,
                lugar, capacidad, descripcion
            );

            tablaRegistrados.asSingleSelect().clear();

            guardar.setEnabled(true);
            actualizar.setEnabled(false);
            cancelar.setEnabled(false);

            Notification.show("Evento actualizado correctamente.");

        });

        // Cancelar la edición

        cancelar.addClickListener(event -> {

            limpiarFormulario(
                nombre, categoria, fechaHora,
                lugar, capacidad, descripcion
            );

            tablaRegistrados.asSingleSelect().clear();

            guardar.setEnabled(true);
            actualizar.setEnabled(false);
            cancelar.setEnabled(false);

            Notification.show("Edición cancelada.");

        });

        // Eliminar evento con confirmación

        eliminar.addClickListener(event -> {

            Evento seleccionado = tablaRegistrados.asSingleSelect().getValue();

            if (seleccionado == null) {
                Notification.show("Selecciona un evento.");
                return;
            }

            ConfirmDialog confirmacion = new ConfirmDialog();

            confirmacion.setHeader("Eliminar evento");
            confirmacion.setText(
                "¿Estás seguro de que deseas eliminar este evento?"
            );

            confirmacion.setCancelable(true);
            confirmacion.setCancelText("Cancelar");
            confirmacion.setConfirmText("Eliminar");
            confirmacion.setConfirmButtonTheme("error primary");

            confirmacion.addConfirmListener(e -> {

                eventos.remove(seleccionado);

                tablaRegistrados.setItems(new ArrayList<>(eventos));

                if (busquedaRealizada) {
                    actualizarResultados(buscarNombre, filtroCategoria);
                }

                tablaRegistrados.asSingleSelect().clear();

                Notification.show("Evento eliminado correctamente.");

            });

            confirmacion.open();

        });

        // Buscar eventos

        buscarEventos.addClickListener(event -> {

            busquedaRealizada = true;

            actualizarResultados(buscarNombre, filtroCategoria);

            if (tablaResultados.getListDataView().getItemCount() == 0) {
                Notification.show("No se encontraron eventos.");
            } else {
                Notification.show("Búsqueda realizada correctamente.");
            }

        });

        // Mostrar todos los eventos en la segunda tabla

        mostrarTodos.addClickListener(event -> {

            buscarNombre.clear();
            filtroCategoria.clear();

            busquedaRealizada = true;

            tablaResultados.setItems(new ArrayList<>(eventos));

            Notification.show("Mostrando todos los eventos.");

        });

        // Organización de la pantalla

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
            tablaRegistrados,
            botonesEdicion,

            tituloBusqueda,
            buscarNombre,
            filtroCategoria,
            buscarEventos,
            mostrarTodos,

            new H2("Resultados de búsqueda"),
            tablaResultados
        );

        setSpacing(true);
        setPadding(true);
        setWidthFull();
    }

    // Configurar las columnas de las tablas

    private void configurarTabla(Grid<Evento> tabla) {

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
    }

    // Actualizar los resultados de búsqueda

    private void actualizarResultados(
            TextField buscarNombre,
            ComboBox<String> filtroCategoria) {

        String texto = buscarNombre.getValue()
                .trim().toLowerCase(Locale.ROOT);

        String categoriaSeleccionada = filtroCategoria.getValue();

        List<Evento> resultados = new ArrayList<>();

        for (Evento evento : eventos) {

            boolean coincideNombre = evento.getNombre()
                    .toLowerCase(Locale.ROOT).contains(texto);

            boolean coincideCategoria = categoriaSeleccionada == null
                    || evento.getCategoria().equals(categoriaSeleccionada);

            if (coincideNombre && coincideCategoria) {
                resultados.add(evento);
            }
        }

        tablaResultados.setItems(resultados);
    }

    // Limpiar los campos del formulario

    private void limpiarFormulario(
            TextField nombre,
            ComboBox<String> categoria,
            DateTimePicker fechaHora,
            TextField lugar,
            IntegerField capacidad,
            TextArea descripcion) {

        nombre.clear();
        categoria.clear();
        fechaHora.clear();
        lugar.clear();
        capacidad.clear();
        descripcion.clear();
    }
}
