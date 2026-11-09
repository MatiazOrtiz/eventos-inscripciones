
package com.eventos.eventos_inscripciones;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.checkbox.CheckboxGroup;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.confirmdialog.ConfirmDialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.router.Route;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Route(value = "asistencias", layout = MainLayout.class)
public class AsistenciasView extends VerticalLayout {

    private List<Evento> eventos = new ArrayList<>();
    private List<Actividad> actividades = new ArrayList<>();
    private List<Inscripcion> inscripciones = new ArrayList<>();
    private List<Asistencia> asistencias = new ArrayList<>();

    private ComboBox<Evento> eventoCombo = new ComboBox<>("Evento");
    private ComboBox<Actividad> actividadCombo = new ComboBox<>("Actividad");

    private CheckboxGroup<Inscripcion> participantesGrupo =
            new CheckboxGroup<>();

    private Button guardarButton = new Button("Guardar asistencias");
    private Button cancelarButton = new Button("Limpiar selección");

    private Grid<Asistencia> tabla = new Grid<>(Asistencia.class, false);

    public AsistenciasView() {

        setSizeFull();
        setPadding(true);
        setSpacing(true);

        cargarDatos();

        eventoCombo.setItemLabelGenerator(Evento::getNombre);
        actividadCombo.setItemLabelGenerator(Actividad::getNombre);

        participantesGrupo.setLabel("Selecciona los participantes que asistieron");

        participantesGrupo.setItemLabelGenerator(
                inscripcion -> inscripcion.getNombre()
                        + " - " + inscripcion.getDocumento()
        );

        eventoCombo.setItems(eventos);

        eventoCombo.addValueChangeListener(event -> {

            Evento seleccionado = event.getValue();

            actividadCombo.clear();
            participantesGrupo.clear();
            participantesGrupo.setItems(new ArrayList<>());

            if (seleccionado != null) {

                List<Actividad> actividadesEvento = new ArrayList<>();

                for (Actividad actividad : actividades) {

                    if (actividad.getIdEvento() == seleccionado.getId()) {
                        actividadesEvento.add(actividad);
                    }
                }

                actividadCombo.setItems(actividadesEvento);
            }
        });

        actividadCombo.addValueChangeListener(event -> {

            cargarParticipantesActividad(event.getValue());

        });

        guardarButton.addClickListener(event -> guardarAsistencias());

        cancelarButton.addClickListener(event -> limpiarFormulario());

        configurarTabla();

        HorizontalLayout botones = new HorizontalLayout(
                guardarButton,
                cancelarButton
        );

        add(
                eventoCombo,
                actividadCombo,
                participantesGrupo,
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

    private void cargarParticipantesActividad(Actividad actividad) {

        participantesGrupo.clear();

        if (actividad == null) {
            participantesGrupo.setItems(new ArrayList<>());
            return;
        }

        List<Inscripcion> participantesEvento = new ArrayList<>();

        for (Inscripcion inscripcion : inscripciones) {

            if (inscripcion.getIdEvento() == actividad.getIdEvento()
                    && inscripcion.getEstado().equals("Inscrito")) {

                participantesEvento.add(inscripcion);
            }
        }

        participantesGrupo.setItems(participantesEvento);

        Set<Inscripcion> presentes = new HashSet<>();

        for (Asistencia asistencia : asistencias) {

            if (asistencia.getIdActividad() == actividad.getId()
                    && asistencia.isAsistio()) {

                for (Inscripcion inscripcion : participantesEvento) {

                    if (inscripcion.getId() == asistencia.getIdInscripcion()) {
                        presentes.add(inscripcion);
                    }
                }
            }
        }

        participantesGrupo.setValue(presentes);
    }

    private void guardarAsistencias() {

        Actividad actividad = actividadCombo.getValue();

        if (eventoCombo.getValue() == null || actividad == null) {

            Notification.show("Selecciona el evento y la actividad");
            return;
        }

        Set<Inscripcion> presentes = participantesGrupo.getValue();

        if (presentes == null) {
            presentes = new HashSet<>();
        }

        List<Asistencia> copiaAnterior = new ArrayList<>();

        for (Asistencia asistencia : asistencias) {

            copiaAnterior.add(
                    new Asistencia(
                            asistencia.getId(),
                            asistencia.getIdActividad(),
                            asistencia.getIdInscripcion(),
                            asistencia.isAsistio()
                    )
            );
        }

        for (Inscripcion participante : inscripciones) {

            if (participante.getIdEvento() == actividad.getIdEvento()
                    && participante.getEstado().equals("Inscrito")) {

                boolean asistio = presentes.contains(participante);

                Asistencia asistenciaEncontrada = null;

                for (Asistencia asistencia : asistencias) {

                    if (asistencia.getIdActividad() == actividad.getId()
                            && asistencia.getIdInscripcion() == participante.getId()) {

                        asistenciaEncontrada = asistencia;
                        break;
                    }
                }

                if (asistenciaEncontrada != null) {

                    asistenciaEncontrada.setAsistio(asistio);

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
                            asistio
                    );

                    asistencias.add(nuevaAsistencia);
                }
            }
        }

        try {

            ArchivoAsistencias.guardarAsistencias(asistencias);

            Notification.show("Asistencias guardadas correctamente");

            actualizarTabla();
            cargarParticipantesActividad(actividad);

        } catch (IOException e) {

            asistencias = copiaAnterior;

            Notification.show("Error al guardar las asistencias");
        }
    }

    private void configurarTabla() {

        tabla.addColumn(Asistencia::getId)
                .setHeader("ID");

        tabla.addColumn(asistencia ->
                buscarNombreActividad(asistencia.getIdActividad())
        ).setHeader("Actividad");

        tabla.addColumn(asistencia ->
                buscarNombreParticipante(asistencia.getIdInscripcion())
        ).setHeader("Participante");

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

        tabla.setWidthFull();
        tabla.setAllRowsVisible(true);
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

        for (Actividad actividad : actividades) {

            if (actividad.getId() == asistencia.getIdActividad()) {
                actividadEncontrada = actividad;
                break;
            }
        }

        if (actividadEncontrada != null) {

            for (Evento evento : eventos) {

                if (evento.getId() == actividadEncontrada.getIdEvento()) {

                    eventoCombo.setValue(evento);
                    break;
                }
            }

            actividadCombo.setValue(actividadEncontrada);

            cargarParticipantesActividad(actividadEncontrada);

            guardarButton.setText("Actualizar asistencias");

            Notification.show(
                    "Modifica los participantes que asistieron a esta actividad."
            );
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

                if (actividadCombo.getValue() != null) {
                    cargarParticipantesActividad(actividadCombo.getValue());
                }

            } catch (IOException e) {

                asistencias.add(asistencia);

                Notification.show("Error al eliminar la asistencia");
            }
        });

        dialogo.open();
    }

    private void limpiarFormulario() {

        eventoCombo.clear();
        actividadCombo.clear();
        participantesGrupo.clear();

        guardarButton.setText("Guardar asistencias");
    }

    private void actualizarTabla() {

        tabla.setItems(asistencias);
    }
}
