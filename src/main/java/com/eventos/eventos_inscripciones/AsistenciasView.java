
package com.eventos.eventos_inscripciones;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.confirmdialog.ConfirmDialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.router.Route;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Route(value = "asistencias", layout = MainLayout.class)
public class AsistenciasView extends VerticalLayout {

    private List<Evento> eventos = new ArrayList<>();
    private List<Actividad> actividades = new ArrayList<>();
    private List<Inscripcion> inscripciones = new ArrayList<>();
    private List<Asistencia> asistencias = new ArrayList<>();

    private ComboBox<Evento> eventoCombo = new ComboBox<>("Evento");
    private ComboBox<Actividad> actividadCombo = new ComboBox<>("Actividad");
    private ComboBox<Inscripcion> participanteCombo = new ComboBox<>("Participante");

    private Checkbox asistioCheck = new Checkbox("El participante asistió");

    private Button guardarButton = new Button("Registrar asistencia");
    private Button cancelarButton = new Button("Cancelar edición");

    private Grid<Asistencia> tabla = new Grid<>(Asistencia.class, false);

    private Asistencia asistenciaSeleccionada = null;

    public AsistenciasView() {

        setSizeFull();
        setPadding(true);
        setSpacing(true);

        cargarDatos();

        eventoCombo.setItemLabelGenerator(Evento::getNombre);
        actividadCombo.setItemLabelGenerator(Actividad::getNombre);

        participanteCombo.setItemLabelGenerator(
            inscripcion -> inscripcion.getNombre() + " - " + inscripcion.getDocumento()
        );

        eventoCombo.setItems(eventos);

        eventoCombo.addValueChangeListener(event -> {

            Evento seleccionado = event.getValue();

            actividadCombo.clear();
            participanteCombo.clear();

            if (seleccionado != null) {

                List<Actividad> actividadesEvento = new ArrayList<>();
                List<Inscripcion> participantesEvento = new ArrayList<>();

                for (Actividad actividad : actividades) {
                    if (actividad.getIdEvento() == seleccionado.getId()) {
                        actividadesEvento.add(actividad);
                    }
                }

                for (Inscripcion inscripcion : inscripciones) {
                    if (inscripcion.getIdEvento() == seleccionado.getId()
                            && inscripcion.getEstado().equals("Inscrito")) {
                        participantesEvento.add(inscripcion);
                    }
                }

                actividadCombo.setItems(actividadesEvento);
                participanteCombo.setItems(participantesEvento);
            }
        });

        guardarButton.addClickListener(event -> guardarAsistencia());

        cancelarButton.addClickListener(event -> limpiarFormulario());

        configurarTabla();

        HorizontalLayout botones = new HorizontalLayout(
            guardarButton, cancelarButton
        );

        add(
            eventoCombo,
            actividadCombo,
            participanteCombo,
            asistioCheck,
            botones,
            tabla
        );

        actualizarTabla();
    }

    private void cargarDatos() {

        try {
            eventos = ArchivoEventos.cargarEventos();
            actividades = ArchivoActividades.cargarActividades();
            inscripciones = ArchivoInscripciones.cargarInscripciones();
            asistencias = ArchivoAsistencias.cargarAsistencias();

        } catch (IOException e) {
            Notification.show("Error al cargar los datos");
        }
    }

    private void guardarAsistencia() {

        Evento evento = eventoCombo.getValue();
        Actividad actividad = actividadCombo.getValue();
        Inscripcion participante = participanteCombo.getValue();

        if (evento == null || actividad == null || participante == null) {
            Notification.show("Selecciona el evento, la actividad y el participante");
            return;
        }

        Asistencia asistenciaExistente = null;

        for (Asistencia asistencia : asistencias) {

            if (asistencia.getIdActividad() == actividad.getId()
                    && asistencia.getIdInscripcion() == participante.getId()) {

                asistenciaExistente = asistencia;
                break;
            }
        }

        if (asistenciaExistente != null) {

            asistenciaExistente.setAsistio(asistioCheck.getValue());

        } else {

            int nuevoId = 1;

            for (Asistencia asistencia : asistencias) {
                if (asistencia.getId() >= nuevoId) {
                    nuevoId = asistencia.getId() + 1;
                }
            }

            Asistencia nuevaAsistencia = new Asistencia(
                nuevoId,
                actividad.getId(),
                participante.getId(),
                asistioCheck.getValue()
            );

            asistencias.add(nuevaAsistencia);
        }

        try {

            ArchivoAsistencias.guardarAsistencias(asistencias);

            Notification.show("Asistencia guardada correctamente");

            limpiarFormulario();
            actualizarTabla();

        } catch (IOException e) {
            Notification.show("Error al guardar la asistencia");
        }
    }

    private void configurarTabla() {

        tabla.addColumn(Asistencia::getId).setHeader("ID");

        tabla.addColumn(asistencia -> buscarNombreActividad(
            asistencia.getIdActividad()
        )).setHeader("Actividad");

        tabla.addColumn(asistencia -> buscarNombreParticipante(
            asistencia.getIdInscripcion()
        )).setHeader("Participante");

        tabla.addColumn(asistencia ->
            asistencia.isAsistio() ? "Asistió" : "No asistió"
        ).setHeader("Estado");

        tabla.addComponentColumn(asistencia -> {

            Button editar = new Button("Editar");

            editar.addClickListener(event -> editarAsistencia(asistencia));

            return editar;

        }).setHeader("Acciones");

        tabla.addComponentColumn(asistencia -> {

            Button eliminar = new Button("Eliminar");

            eliminar.addClickListener(event -> confirmarEliminacion(asistencia));

            return eliminar;

        }).setHeader("Eliminar");
    }

    private String buscarNombreActividad(int id) {

        for (Actividad actividad : actividades) {
            if (actividad.getId() == id) {
                return actividad.getNombre();
            }
        }

        return "Actividad no encontrada";
    }

    private String buscarNombreParticipante(int id) {

        for (Inscripcion inscripcion : inscripciones) {
            if (inscripcion.getId() == id) {
                return inscripcion.getNombre();
            }
        }

        return "Participante no encontrado";
    }

    private void editarAsistencia(Asistencia asistencia) {

        Actividad actividadEncontrada = null;
        Inscripcion participanteEncontrado = null;

        for (Actividad actividad : actividades) {
            if (actividad.getId() == asistencia.getIdActividad()) {
                actividadEncontrada = actividad;
                break;
            }
        }

        for (Inscripcion inscripcion : inscripciones) {
            if (inscripcion.getId() == asistencia.getIdInscripcion()) {
                participanteEncontrado = inscripcion;
                break;
            }
        }

        if (actividadEncontrada != null && participanteEncontrado != null) {

            for (Evento evento : eventos) {
                if (evento.getId() == actividadEncontrada.getIdEvento()) {
                    eventoCombo.setValue(evento);
                    break;
                }
            }

            actividadCombo.setValue(actividadEncontrada);
            participanteCombo.setValue(participanteEncontrado);

            asistioCheck.setValue(asistencia.isAsistio());

            asistenciaSeleccionada = asistencia;

            guardarButton.setText("Actualizar asistencia");
        }
    }

    private void confirmarEliminacion(Asistencia asistencia) {

        ConfirmDialog dialogo = new ConfirmDialog();

        dialogo.setHeader("Eliminar asistencia");
        dialogo.setText("¿Estás seguro de eliminar este registro?");
        dialogo.setCancelable(true);
        dialogo.setCancelText("Cancelar");
        dialogo.setConfirmText("Eliminar");

        dialogo.addConfirmListener(event -> {

            asistencias.remove(asistencia);

            try {

                ArchivoAsistencias.guardarAsistencias(asistencias);

                Notification.show("Asistencia eliminada correctamente");

                actualizarTabla();

            } catch (IOException e) {
                Notification.show("Error al eliminar la asistencia");
            }
        });

        dialogo.open();
    }

    private void limpiarFormulario() {

        eventoCombo.clear();
        actividadCombo.clear();
        participanteCombo.clear();
        asistioCheck.clear();

        asistenciaSeleccionada = null;

        guardarButton.setText("Registrar asistencia");
    }

    private void actualizarTabla() {
        tabla.setItems(asistencias);
    }
}
