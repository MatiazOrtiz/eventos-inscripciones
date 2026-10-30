
package com.eventos.eventos_inscripciones;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.io.IOException;

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

        addClassName("eventos-pagina");

        // Cargar eventos guardados

        try {

            eventos = ArchivoEventos.cargarEventos();

            for (Evento evento : eventos) {

                if (evento.getId() >= siguienteId) {
                    siguienteId = evento.getId() + 1;
                }
            }

        } catch (IOException e) {

            Notification.show("Error al cargar los eventos.");
        }

        // Encabezado

        H2 titulo = new H2("Gestión de eventos");
        titulo.addClassName("titulo-pagina");

        Paragraph subtitulo = new Paragraph(
            "Administra, organiza y consulta los eventos de la plataforma."
        );
        subtitulo.addClassName("subtitulo-pagina");

        VerticalLayout encabezado = new VerticalLayout(titulo, subtitulo);
        encabezado.setPadding(false);
        encabezado.setSpacing(false);
        encabezado.addClassName("encabezado-eventos");

        // Formulario

        TextField nombre = new TextField("Nombre del evento");
        nombre.setPlaceholder("Ej. Feria de tecnología");
        nombre.setWidthFull();

        ComboBox<String> categoria = new ComboBox<>("Categoría");
        categoria.setItems(
            "Académico",
            "Cultural",
            "Deportivo",
            "Social",
            "Tecnológico"
        );
        categoria.setPlaceholder("Selecciona una categoría");
        categoria.setWidthFull();

        DateTimePicker fechaHora = new DateTimePicker("Fecha y hora");
        fechaHora.setWidthFull();

        TextField lugar = new TextField("Lugar");
        lugar.setPlaceholder("Ej. Auditorio principal");
        lugar.setWidthFull();

        IntegerField capacidad = new IntegerField("Capacidad");
        capacidad.setMin(1);
        capacidad.setStepButtonsVisible(true);
        capacidad.setPlaceholder("Número de asistentes");
        capacidad.setWidthFull();

        TextArea descripcion = new TextArea("Descripción");
        descripcion.setPlaceholder("Describe brevemente el evento...");
        descripcion.setWidthFull();
        descripcion.setMinHeight("90px");

        Button guardar = new Button("Registrar evento");
        guardar.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        guardar.addClassName("boton-principal");

        // Botones de edición

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

        HorizontalLayout botonesEdicion = new HorizontalLayout(
            editar, actualizar, cancelar, eliminar
        );

        botonesEdicion.setWidthFull();
        botonesEdicion.addClassName("botones-edicion");

        // Tarjeta del formulario

        H2 tituloFormulario = new H2("Registrar un evento");
        tituloFormulario.addClassName("titulo-seccion");

        Paragraph ayudaFormulario = new Paragraph(
            "Completa los datos para crear un nuevo evento."
        );
        ayudaFormulario.addClassName("texto-ayuda");

        HorizontalLayout fila1 = new HorizontalLayout(nombre, categoria);
        fila1.setWidthFull();
        fila1.setFlexGrow(1, nombre);
        fila1.setFlexGrow(1, categoria);

        HorizontalLayout fila2 = new HorizontalLayout(fechaHora, lugar);
        fila2.setWidthFull();
        fila2.setFlexGrow(1, fechaHora);
        fila2.setFlexGrow(1, lugar);

        HorizontalLayout fila3 = new HorizontalLayout(capacidad, descripcion);
        fila3.setWidthFull();
        fila3.setFlexGrow(1, capacidad);
        fila3.setFlexGrow(2, descripcion);

        VerticalLayout tarjetaFormulario = new VerticalLayout(
            tituloFormulario,
            ayudaFormulario,
            fila1,
            fila2,
            fila3,
            guardar,
            botonesEdicion
        );

        tarjetaFormulario.addClassName("tarjeta-eventos");
        tarjetaFormulario.setWidthFull();
        tarjetaFormulario.setPadding(true);
        tarjetaFormulario.setSpacing(true);

        // Tabla de eventos registrados

        configurarTabla(tablaRegistrados);
        tablaRegistrados.setItems(eventos);

        H2 tituloRegistrados = new H2("Eventos registrados");
        tituloRegistrados.addClassName("titulo-seccion");

        Paragraph ayudaRegistrados = new Paragraph(
            "Selecciona un evento para editarlo o eliminarlo."
        );
        ayudaRegistrados.addClassName("texto-ayuda");

        VerticalLayout tarjetaRegistrados = new VerticalLayout(
            tituloRegistrados,
            ayudaRegistrados,
            tablaRegistrados
        );

        tarjetaRegistrados.addClassName("tarjeta-eventos");
        tarjetaRegistrados.setWidthFull();
        tarjetaRegistrados.setPadding(true);

        // Búsqueda

        TextField buscarNombre = new TextField("Nombre del evento");
        buscarNombre.setPlaceholder("Escribe el nombre...");
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

        Button buscarEventos = new Button("Buscar");
        buscarEventos.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

        Button mostrarTodos = new Button("Mostrar todos");

        HorizontalLayout filaBusqueda = new HorizontalLayout(
            buscarNombre,
            filtroCategoria,
            buscarEventos,
            mostrarTodos
        );

        filaBusqueda.setWidthFull();
        filaBusqueda.setAlignItems(FlexComponent.Alignment.END);
        filaBusqueda.setFlexGrow(1, buscarNombre);
        filaBusqueda.setFlexGrow(1, filtroCategoria);

        // Tabla de resultados

        configurarTabla(tablaResultados);

        H2 tituloResultados = new H2("Resultados de búsqueda");
        tituloResultados.addClassName("titulo-seccion");

        VerticalLayout tarjetaBusqueda = new VerticalLayout(
            new H2("Buscar eventos"),
            new Paragraph(
                "Filtra los eventos por su nombre o categoría."
            ),
            filaBusqueda,
            tituloResultados,
            tablaResultados
        );

        tarjetaBusqueda.addClassName("tarjeta-eventos");
        tarjetaBusqueda.setWidthFull();
        tarjetaBusqueda.setPadding(true);

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

            try {

                ArchivoEventos.guardarEventos(eventos);

            } catch (IOException e) {

                eventos.remove(nuevoEvento);
                siguienteId--;

                Notification.show("Error al guardar el evento.");
                return;
            }

            tablaRegistrados.setItems(new ArrayList<>(eventos));

            if (busquedaRealizada) {
                actualizarResultados(buscarNombre, filtroCategoria);
            }

            limpiarFormulario(
                nombre, categoria, fechaHora,
                lugar, capacidad, descripcion
            );

            Notification.show("Evento registrado correctamente.");
        });

        // Seleccionar evento

        tablaRegistrados.asSingleSelect().addValueChangeListener(event -> {

            boolean seleccionado = event.getValue() != null;

            editar.setEnabled(seleccionado);
            eliminar.setEnabled(seleccionado);

        });

        // Cargar datos para editar

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

        // Actualizar evento

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

            String nombreAnterior = seleccionado.getNombre();
            String categoriaAnterior = seleccionado.getCategoria();
            java.time.LocalDateTime fechaAnterior = seleccionado.getFechaHora();
            String lugarAnterior = seleccionado.getLugar();
            int capacidadAnterior = seleccionado.getCapacidad();
            String descripcionAnterior = seleccionado.getDescripcion();

            seleccionado.setNombre(nombre.getValue());
            seleccionado.setCategoria(categoria.getValue());
            seleccionado.setFechaHora(fechaHora.getValue());
            seleccionado.setLugar(lugar.getValue());
            seleccionado.setCapacidad(capacidad.getValue());
            seleccionado.setDescripcion(descripcion.getValue());

            try {

                ArchivoEventos.guardarEventos(eventos);

            } catch (IOException e) {

                seleccionado.setNombre(nombreAnterior);
                seleccionado.setCategoria(categoriaAnterior);
                seleccionado.setFechaHora(fechaAnterior);
                seleccionado.setLugar(lugarAnterior);
                seleccionado.setCapacidad(capacidadAnterior);
                seleccionado.setDescripcion(descripcionAnterior);

                Notification.show("Error al actualizar el evento.");
                return;
            }

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

        // Cancelar edición

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

        // Eliminar con confirmación

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

                try {

                    ArchivoEventos.guardarEventos(eventos);

                } catch (IOException ex) {

                    eventos.add(seleccionado);

                    tablaRegistrados.setItems(new ArrayList<>(eventos));

                    Notification.show("Error al eliminar el evento.");
                    return;
                }

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

        // Mostrar todos los eventos

        mostrarTodos.addClickListener(event -> {

            buscarNombre.clear();
            filtroCategoria.clear();

            busquedaRealizada = true;

            tablaResultados.setItems(new ArrayList<>(eventos));

            Notification.show("Mostrando todos los eventos.");
        });

        // Agregar componentes a la página

        add(
            encabezado,
            tarjetaFormulario,
            tarjetaRegistrados,
            tarjetaBusqueda
        );

        setWidthFull();
        setMaxWidth("1500px");
        getStyle().set("margin", "0 auto");
        setPadding(true);
        setSpacing(true);
    }

    // Configurar columnas de las tablas

    private void configurarTabla(Grid<Evento> tabla) {

        tabla.addColumn(Evento::getId)
                .setHeader("ID")
                .setAutoWidth(true);

        tabla.addColumn(Evento::getNombre)
                .setHeader("Nombre")
                .setAutoWidth(true);

        tabla.addColumn(Evento::getCategoria)
                .setHeader("Categoría")
                .setAutoWidth(true);

        tabla.addColumn(Evento::getFechaHora)
                .setHeader("Fecha y hora")
                .setAutoWidth(true);

        tabla.addColumn(Evento::getLugar)
                .setHeader("Lugar")
                .setAutoWidth(true);

        tabla.addColumn(Evento::getCapacidad)
                .setHeader("Capacidad")
                .setAutoWidth(true);

        tabla.addColumn(Evento::getDescripcion)
                .setHeader("Descripción");

        tabla.addThemeVariants(GridVariant.LUMO_WRAP_CELL_CONTENT);

        tabla.setAllRowsVisible(true);
        tabla.setWidthFull();
    }

    // Actualizar resultados de búsqueda

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

    // Limpiar formulario

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
