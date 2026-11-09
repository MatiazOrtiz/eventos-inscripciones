package com.eventos.eventos_inscripciones;

import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.orderedlayout.FlexLayout;
import com.vaadin.flow.router.Route;

import java.io.IOException;
import java.util.List;

@Route(value = "estadisticas", layout = MainLayout.class)
public class EstadisticasView extends VerticalLayout {

    public EstadisticasView() {

        addClassName("eventos-pagina");

        setWidthFull();
        setMaxWidth("1500px");
        getStyle().set("margin", "0 auto");
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

            textoPorcentaje.getStyle()
                    .set("font-size", "32px")
                    .set("font-weight", "bold");

            // Primera fila de tarjetas

            FlexLayout fila1 = new FlexLayout(
                    crearTarjeta("Total de eventos", eventos.size()),
                    crearTarjeta("Total de actividades", actividades.size()),
                    crearTarjeta("Participantes inscritos", inscritos)
            );

            fila1.setWidthFull();
            fila1.setFlexWrap(FlexLayout.FlexWrap.WRAP);
            fila1.addClassName("fila-responsive");

            // Segunda fila de tarjetas

            FlexLayout fila2 = new FlexLayout(
                    crearTarjeta("Personas en espera", enEspera),
                    crearTarjeta("Asistencias", presentes),
                    crearTarjeta("Ausencias", ausentes)
            );

            fila2.setWidthFull();
            fila2.setFlexWrap(FlexLayout.FlexWrap.WRAP);
            fila2.addClassName("fila-responsive");

            // Tarjeta del porcentaje

            VerticalLayout tarjetaPorcentaje = new VerticalLayout(
                    new H3("Porcentaje de asistencia"),
                    textoPorcentaje
            );

            tarjetaPorcentaje.addClassName("tarjeta-eventos");
            tarjetaPorcentaje.setWidthFull();
            tarjetaPorcentaje.setMaxWidth("400px");
            tarjetaPorcentaje.setPadding(true);

            // Agregar componentes

            add(titulo, fila1, fila2, tarjetaPorcentaje);

        } catch (IOException e) {

            add(new Span("Error al cargar las estadísticas: " + e.getMessage()));
        }
    }

    // Crear tarjeta de estadística

    private VerticalLayout crearTarjeta(String titulo, int cantidad) {

        H3 nombre = new H3(titulo);
        H2 numero = new H2(String.valueOf(cantidad));

        VerticalLayout tarjeta = new VerticalLayout(nombre, numero);

        tarjeta.addClassName("tarjeta-eventos");
        tarjeta.setWidth("250px");
        tarjeta.setMaxWidth("100%");
        tarjeta.setFlexGrow(1);

        return tarjeta;
    }
}