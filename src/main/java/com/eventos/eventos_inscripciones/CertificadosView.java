
package com.eventos.eventos_inscripciones;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.StreamResource;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Route(value = "certificados", layout = MainLayout.class)
public class CertificadosView extends VerticalLayout {

    private ComboBox<Evento> comboEvento = new ComboBox<>("Seleccionar evento");

    private Grid<Inscripcion> tabla = new Grid<>(Inscripcion.class, false);

    private List<Evento> eventos = new ArrayList<>();
    private List<Actividad> actividades = new ArrayList<>();
    private List<Inscripcion> inscripciones = new ArrayList<>();
    private List<Asistencia> asistencias = new ArrayList<>();

    private Inscripcion participanteSeleccionado;

    private Button botonCertificado = new Button("Generar certificado");

    private Anchor enlaceDescarga = new Anchor();

    private Span mensaje = new Span("Selecciona un evento para consultar sus participantes.");

    public CertificadosView() {

        setSizeFull();
        setPadding(true);
        setSpacing(true);

        H2 titulo = new H2("Generación de certificados");

        try {

            eventos = ArchivoEventos.cargarEventos();
            actividades = ArchivoActividades.cargarActividades();
            inscripciones = ArchivoInscripciones.cargarInscripciones();
            asistencias = ArchivoAsistencias.cargarAsistencias();

            comboEvento.setItems(eventos);
            comboEvento.setItemLabelGenerator(Evento::getNombre);
            comboEvento.setWidth("350px");

            configurarTabla();

            comboEvento.addValueChangeListener(evento -> {

                participanteSeleccionado = null;
                botonCertificado.setEnabled(false);
                enlaceDescarga.setVisible(false);

                if (evento.getValue() != null) {

                    cargarParticipantes(evento.getValue());

                } else {

                    tabla.setItems(new ArrayList<>());
                    mensaje.setText("Selecciona un evento para consultar sus participantes.");
                }
            });

            botonCertificado.setEnabled(false);

            botonCertificado.addClickListener(evento -> {

                if (participanteSeleccionado != null) {

                    generarCertificado(participanteSeleccionado);
                }
            });

            enlaceDescarga.setText("Descargar certificado HTML");
            enlaceDescarga.setVisible(false);
            enlaceDescarga.getElement().setAttribute("download", true);

            HorizontalLayout botones = new HorizontalLayout(
                    botonCertificado,
                    enlaceDescarga
            );

            botones.setAlignItems(FlexComponent.Alignment.CENTER);

            add(titulo, comboEvento, mensaje, tabla, botones);

        } catch (IOException e) {

            Notification.show("Error al cargar los datos: " + e.getMessage());
        }
    }

    private void configurarTabla() {

        tabla.addColumn(Inscripcion::getNombre)
                .setHeader("Participante")
                .setAutoWidth(true);

        tabla.addColumn(Inscripcion::getDocumento)
                .setHeader("Documento")
                .setAutoWidth(true);

        tabla.addColumn(Inscripcion::getCorreo)
                .setHeader("Correo")
                .setAutoWidth(true);

        tabla.addColumn(inscripcion -> calcularPorcentaje(inscripcion) + "%")
                .setHeader("Asistencia")
                .setAutoWidth(true);

        tabla.addColumn(inscripcion -> {

            if (calcularPorcentaje(inscripcion) >= 80) {
                return "Aprobado";
            } else {
                return "No cumple";
            }

        }).setHeader("Certificado")
                .setAutoWidth(true);

        tabla.setWidthFull();
        tabla.setAllRowsVisible(true);

        tabla.asSingleSelect().addValueChangeListener(evento -> {

            participanteSeleccionado = evento.getValue();

            botonCertificado.setEnabled(false);
            enlaceDescarga.setVisible(false);

            if (participanteSeleccionado != null) {

                if (calcularPorcentaje(participanteSeleccionado) >= 80) {

                    botonCertificado.setEnabled(true);

                } else {

                    Notification.show(
                            "El participante no cumple con el 80% de asistencia."
                    );
                }
            }
        });
    }

    private void cargarParticipantes(Evento evento) {

        List<Inscripcion> participantesEvento = new ArrayList<>();

        for (Inscripcion inscripcion : inscripciones) {

            if (inscripcion.getIdEvento() == evento.getId()
                    && inscripcion.getEstado().equals("Inscrito")) {

                participantesEvento.add(inscripcion);
            }
        }

        tabla.setItems(participantesEvento);

        mensaje.setText(
                "Participantes inscritos: " + participantesEvento.size()
        );

        if (participantesEvento.isEmpty()) {

            mensaje.setText("Este evento no tiene participantes inscritos.");
        }
    }

    private int calcularPorcentaje(Inscripcion inscripcion) {

        int totalActividades = 0;
        int totalAsistencias = 0;

        for (Actividad actividad : actividades) {

            if (actividad.getIdEvento() == inscripcion.getIdEvento()) {

                totalActividades++;

                for (Asistencia asistencia : asistencias) {

                    if (asistencia.getIdActividad() == actividad.getId()
                            && asistencia.getIdInscripcion() == inscripcion.getId()
                            && asistencia.isAsistio()) {

                        totalAsistencias++;
                        break;
                    }
                }
            }
        }

        if (totalActividades == 0) {
            return 0;
        }

        return (int) Math.round(
                (totalAsistencias * 100.0) / totalActividades
        );
    }

    private void generarCertificado(Inscripcion participante) {

        Evento evento = comboEvento.getValue();

        if (evento == null) {
            return;
        }

        int porcentaje = calcularPorcentaje(participante);

        if (porcentaje < 80) {

            Notification.show("El participante no cumple el requisito.");
            return;
        }

        DateTimeFormatter formato =
                DateTimeFormatter.ofPattern("dd/MM/yyyy");

        String fecha = evento.getFechaHora().format(formato);

        String html = """
                <!DOCTYPE html>
                <html lang="es">
                <head>
                    <meta charset="UTF-8">
                    <title>Certificado</title>

                    <style>
                        body {
                            font-family: Arial, sans-serif;
                            background: #f2f2f2;
                            padding: 40px;
                        }

                        .certificado {
                            background: white;
                            border: 8px double #263b63;
                            padding: 50px;
                            text-align: center;
                            max-width: 850px;
                            margin: auto;
                        }

                        h1 {
                            color: #263b63;
                            font-size: 36px;
                        }

                        h2 {
                            color: #333;
                        }

                        .nombre {
                            font-size: 30px;
                            font-weight: bold;
                            margin: 30px;
                        }

                        .detalle {
                            font-size: 18px;
                            line-height: 1.8;
                        }
                    </style>
                </head>

                <body>

                    <div class="certificado">

                        <h1>CERTIFICADO DE PARTICIPACIÓN</h1>

                        <h2>Se certifica que:</h2>

                        <div class="nombre">
                            %s
                        </div>

                        <p class="detalle">
                            Ha participado satisfactoriamente en el evento
                            <strong>%s</strong>.
                        </p>

                        <p class="detalle">
                            Realizado el día %s en %s.
                        </p>

                        <p class="detalle">
                            Porcentaje de asistencia: <strong>%d%%</strong>
                        </p>

                        <br><br>

                        <p>Gestión de Eventos e Inscripciones</p>

                    </div>

                </body>
                </html>
                """.formatted(
                    escapar(participante.getNombre()),
                    escapar(evento.getNombre()),
                    fecha,
                    escapar(evento.getLugar()),
                    porcentaje
                );

        String nombreArchivo = "Certificado_"
                + participante.getDocumento() + ".html";

        StreamResource recurso = new StreamResource(
                nombreArchivo,
                () -> new ByteArrayInputStream(
                        html.getBytes(StandardCharsets.UTF_8)
                )
        );

        enlaceDescarga.setHref(recurso);
        enlaceDescarga.setVisible(true);

        Notification.show("Certificado generado correctamente.");
    }

    private String escapar(String texto) {

        return texto.replace("&", "&amp;")
                .replace("<", "&lt;")
                .replace(">", "&gt;")
                .replace("\"", "&quot;")
                .replace("'", "&#39;");
    }
}
