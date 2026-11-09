
package com.eventos.eventos_inscripciones;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class DatosIniciales {

    public static void inicializar() {

        try {

            List<Evento> eventosExistentes =
                ArchivoEventos.cargarEventos();

            if (!eventosExistentes.isEmpty()) {
                System.out.println("Los datos iniciales ya existen.");
                return;
            }

            List<Evento> eventos = new ArrayList<>();
            List<Actividad> actividades = new ArrayList<>();
            List<Inscripcion> inscripciones = new ArrayList<>();
            List<Asistencia> asistencias = new ArrayList<>();

            // Evento
            eventos.add(new Evento(
                1,
                "Copa Universitaria de Futbol 2026",
                "Deportivo",
                LocalDateTime.of(2026, 11, 22, 8, 0),
                "Cancha deportiva UPB",
                2,
                "Torneo universitario de futbol."
            ));

            // Actividades
            actividades.add(new Actividad(
                1, 1,
                "Calentamiento deportivo",
                "Preparacion de los participantes.",
                LocalDateTime.of(2026, 11, 22, 8, 0),
                LocalDateTime.of(2026, 11, 22, 9, 0)
            ));

            actividades.add(new Actividad(
                2, 1,
                "Partido final",
                "Partido final del torneo.",
                LocalDateTime.of(2026, 11, 22, 9, 30),
                LocalDateTime.of(2026, 11, 22, 11, 0)
            ));

            // Inscripciones
            inscripciones.add(new Inscripcion(
                1, 1, "Juan David Martinez",
                "3001001", "juan@gmail.com", "Inscrito"
            ));

            inscripciones.add(new Inscripcion(
                2, 1, "Samuel Restrepo",
                "3001002", "samuel@gmail.com", "Inscrito"
            ));

            inscripciones.add(new Inscripcion(
                3, 1, "Nicolas Gomez",
                "3001003", "nicolas@gmail.com", "En espera"
            ));

            // Asistencias
            asistencias.add(new Asistencia(1, 1, 1, true));
            asistencias.add(new Asistencia(2, 1, 2, true));
            asistencias.add(new Asistencia(3, 2, 1, true));
            asistencias.add(new Asistencia(4, 2, 2, false));

            // Guardar datos
            ArchivoEventos.guardarEventos(eventos);
            ArchivoActividades.guardarActividades(actividades);
            ArchivoInscripciones.guardarInscripciones(inscripciones);
            ArchivoAsistencias.guardarAsistencias(asistencias);

            System.out.println("Datos iniciales creados correctamente.");

        } catch (IOException e) {

            System.out.println("Error al crear los datos iniciales: "
                + e.getMessage());
        }
    }
}
