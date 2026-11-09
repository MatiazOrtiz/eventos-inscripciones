
package com.eventos.eventos_inscripciones;

import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.router.Route;

import java.io.IOException;
import java.util.List;

@Route(value = "estadisticas", layout = MainLayout.class)
public class EstadisticasView extends VerticalLayout {

    public EstadisticasView() {

        setSizeFull();
        setPadding(true);
        setSpacing(true);

        H2 titulo = new H2("Estadísticas generales");

        try {

            List<Evento> eventos = ArchivoEventos.cargarEventos();
            List<Actividad> actividades = ArchivoActividades.cargarActividades();
            List<Inscripcion> inscripciones = ArchivoInscripciones.cargarInscripciones();
            List<Asistencia> asistencias = ArchivoAsistencias.cargarAsistencias();

            int inscritos = 0;
            int enEspera = 0;
            int presentes = 0;
            int ausentes = 0;

            for (Inscripcion inscripcion : inscripciones) {

                if (inscripcion.getEstado().equals("Inscrito")) {
                    inscritos++;
                } else if (inscripcion.getEstado().equals("En espera")) {
                    enEspera++;
                }
            }

            for (Asistencia asistencia : asistencias) {

                if (asistencia.isAsistio()) {
                    presentes++;
                } else {
                    ausentes++;
                }
            }

            int totalAsistencias = presentes + ausentes;
            double porcentaje = 0;

            if (totalAsistencias > 0) {
                porcentaje = (presentes * 100.0) / totalAsistencias;
            }

            Span textoPorcentaje = new Span(
                    String.format("%.2f%%", porcentaje)
            );

            HorizontalLayout fila1 = new HorizontalLayout(
                    crearTarjeta("Total de eventos", eventos.size()),
                    crearTarjeta("Total de actividades", actividades.size()),
                    crearTarjeta("Participantes inscritos", inscritos)
            );

            HorizontalLayout fila2 = new HorizontalLayout(
                    crearTarjeta("Personas en espera", enEspera),
                    crearTarjeta("Asistencias", presentes),
                    crearTarjeta("Ausencias", ausentes)
            );

            VerticalLayout tarjetaPorcentaje = new VerticalLayout(
                    new H3("Porcentaje de asistencia"),
                    textoPorcentaje
            );

            tarjetaPorcentaje.addClassName("tarjeta-eventos");

            fila1.setWidthFull();
            fila2.setWidthFull();

            add(titulo, fila1, fila2, tarjetaPorcentaje);

        } catch (IOException e) {

            add(new Span("Error al cargar las estadísticas: " + e.getMessage()));
        }
    }

    private VerticalLayout crearTarjeta(String titulo, int cantidad) {

        H3 nombre = new H3(titulo);
        H2 numero = new H2(String.valueOf(cantidad));

        VerticalLayout tarjeta = new VerticalLayout(nombre, numero);

        tarjeta.addClassName("tarjeta-eventos");
        tarjeta.setWidth("250px");

        return tarjeta;
    }
}
