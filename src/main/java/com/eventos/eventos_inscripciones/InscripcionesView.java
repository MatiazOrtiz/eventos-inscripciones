
package com.eventos.eventos_inscripciones;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.confirmdialog.ConfirmDialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.EmailField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;

@Route(value = "inscripciones", layout = MainLayout.class)
public class InscripcionesView extends VerticalLayout {

    private List<Inscripcion> inscripciones = new ArrayList<>();
    private List<Evento> eventos = new ArrayList<>();

    private Grid<Inscripcion> tabla = new Grid<>(Inscripcion.class, false);

    private int siguienteId = 1;

    public InscripcionesView() {

        addClassName("eventos-pagina");

        try {

            eventos = ArchivoEventos.cargarEventos();
            inscripciones = ArchivoInscripciones.cargarInscripciones();

            for (Inscripcion inscripcion : inscripciones) {

                if (inscripcion.getId() >= siguienteId) {
                    siguienteId = inscripcion.getId() + 1;
                }
            }

        } catch (IOException e) {

            Notification.show("Error al cargar los datos.");
        }

        H2 titulo = new H2("Gestión de inscripciones");

        Paragraph subtitulo = new Paragraph(
            "Registra participantes y administra sus inscripciones."
        );

        // Formulario

        ComboBox<Evento> evento = new ComboBox<>("Seleccionar evento");
        evento.setItems(eventos);
        evento.setItemLabelGenerator(Evento::getNombre);
        evento.setWidthFull();

        TextField nombre = new TextField("Nombre del participante");
        nombre.setWidthFull();

        TextField documento = new TextField("Documento");
        documento.setWidthFull();

        EmailField correo = new EmailField("Correo electrónico");
        correo.setWidthFull();

        Button guardar = new Button("Registrar inscripción");
        guardar.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

        VerticalLayout formulario = new VerticalLayout(
            new H2("Registrar participante"),
            evento,
            nombre,
            documento,
            correo,
            guardar
        );

        formulario.addClassName("tarjeta-eventos");
        formulario.setWidthFull();

        // Tabla

        configurarTabla();
        tabla.setItems(inscripciones);

        Button cancelarInscripcion = new Button("Cancelar inscripción");
        cancelarInscripcion.addThemeVariants(ButtonVariant.LUMO_ERROR);
        cancelarInscripcion.setEnabled(false);

        tabla.asSingleSelect().addValueChangeListener(e -> {
            cancelarInscripcion.setEnabled(e.getValue() != null);
        });

        VerticalLayout tarjetaTabla = new VerticalLayout(
            new H2("Participantes inscritos"),
            tabla,
            cancelarInscripcion
        );

        tarjetaTabla.addClassName("tarjeta-eventos");
        tarjetaTabla.setWidthFull();

        // Registrar inscripción

        guardar.addClickListener(e -> {

            if (evento.isEmpty()
                    || nombre.isEmpty()
                    || documento.isEmpty()
                    || correo.isEmpty()) {

                Notification.show("Completa todos los campos.");
                return;
            }

            if (!correo.isInvalid() && !correo.getValue().isBlank()) {
                // El correo tiene un formato válido.
            } else {
                Notification.show("Escribe un correo electrónico válido.");
                return;
            }

            for (Inscripcion inscripcion : inscripciones) {

                if (inscripcion.getIdEvento() == evento.getValue().getId()
                        && inscripcion.getDocumento().equals(documento.getValue())) {

                    Notification.show("Este participante ya está inscrito en el evento.");
                    return;
                }
            }

            int cantidadInscritos = contarInscritos(evento.getValue().getId());

            String estado;

            if (cantidadInscritos < evento.getValue().getCapacidad()) {
                estado = "Inscrito";
            } else {
                estado = "En espera";
            }

            Inscripcion nueva = new Inscripcion(
                siguienteId,
                evento.getValue().getId(),
                nombre.getValue(),
                documento.getValue(),
                correo.getValue(),
                estado
            );

            inscripciones.add(nueva);
            siguienteId++;

            try {

                ArchivoInscripciones.guardarInscripciones(inscripciones);

            } catch (IOException ex) {

                inscripciones.remove(nueva);
                siguienteId--;

                Notification.show("Error al guardar la inscripción.");
                return;
            }

            actualizarTabla();

            nombre.clear();
            documento.clear();
            correo.clear();

            Notification.show(
                estado.equals("Inscrito")
                    ? "Participante inscrito correctamente."
                    : "Evento lleno. Participante agregado a la lista de espera."
            );
        });

        // Cancelar inscripción

        cancelarInscripcion.addClickListener(e -> {

            Inscripcion seleccionada = tabla.asSingleSelect().getValue();

            if (seleccionada == null) {
                return;
            }

            ConfirmDialog confirmacion = new ConfirmDialog();

            confirmacion.setHeader("Cancelar inscripción");
            confirmacion.setText(
                "¿Estás seguro de cancelar esta inscripción?"
            );

            confirmacion.setCancelable(true);
            confirmacion.setCancelText("Volver");
            confirmacion.setConfirmText("Cancelar inscripción");
            confirmacion.setConfirmButtonTheme("error primary");

            confirmacion.addConfirmListener(eventoConfirmacion -> {

                int posicion = inscripciones.indexOf(seleccionada);

                inscripciones.remove(seleccionada);

                promocionarParticipante(seleccionada.getIdEvento());

                try {

                    ArchivoInscripciones.guardarInscripciones(inscripciones);

                } catch (IOException ex) {

                    Notification.show("Error al guardar los cambios.");
                    return;
                }

                actualizarTabla();
                tabla.asSingleSelect().clear();

                Notification.show("Inscripción cancelada correctamente.");
            });

            confirmacion.open();
        });

        add(titulo, subtitulo, formulario, tarjetaTabla);

        setWidthFull();
        setMaxWidth("1500px");
        getStyle().set("margin", "0 auto");
        setPadding(true);
        setSpacing(true);
    }

    // Configurar tabla

    private void configurarTabla() {

        tabla.addColumn(Inscripcion::getId)
                .setHeader("ID");

        tabla.addColumn(Inscripcion::getIdEvento)
                .setHeader("ID Evento");

        tabla.addColumn(Inscripcion::getNombre)
                .setHeader("Participante");

        tabla.addColumn(Inscripcion::getDocumento)
                .setHeader("Documento");

        tabla.addColumn(Inscripcion::getCorreo)
                .setHeader("Correo");

        tabla.addComponentColumn(inscripcion -> {

            Span etiqueta = new Span(inscripcion.getEstado());

            etiqueta.getStyle()
                    .set("padding", "5px 12px")
                    .set("border-radius", "20px")
                    .set("font-weight", "bold");

            if (inscripcion.getEstado().equals("Inscrito")) {

                etiqueta.getStyle()
                        .set("background-color", "#dcfce7")
                        .set("color", "#15803d");

            } else {

                etiqueta.getStyle()
                        .set("background-color", "#ffedd5")
                        .set("color", "#c2410c");
            }

            return etiqueta;

        }).setHeader("Estado");

        tabla.setWidthFull();
        tabla.setAllRowsVisible(true);
    }

    // Contar participantes inscritos

    private int contarInscritos(int idEvento) {

        int cantidad = 0;

        for (Inscripcion inscripcion : inscripciones) {

            if (inscripcion.getIdEvento() == idEvento
                    && inscripcion.getEstado().equals("Inscrito")) {

                cantidad++;
            }
        }

        return cantidad;
    }

    // Promocionar participante de la lista de espera

    private void promocionarParticipante(int idEvento) {

        Evento eventoSeleccionado = null;

        for (Evento evento : eventos) {

            if (evento.getId() == idEvento) {
                eventoSeleccionado = evento;
                break;
            }
        }

        if (eventoSeleccionado == null) {
            return;
        }

        if (contarInscritos(idEvento) >= eventoSeleccionado.getCapacidad()) {
            return;
        }

        for (Inscripcion inscripcion : inscripciones) {

            if (inscripcion.getIdEvento() == idEvento
                    && inscripcion.getEstado().equals("En espera")) {

                inscripcion.setEstado("Inscrito");

                Notification.show(
                    inscripcion.getNombre() + " pasó de la lista de espera a inscrito."
                );

                break;
            }
        }
    }

    // Actualizar tabla

    private void actualizarTabla() {

        tabla.setItems(new ArrayList<>(inscripciones));
    }
}
