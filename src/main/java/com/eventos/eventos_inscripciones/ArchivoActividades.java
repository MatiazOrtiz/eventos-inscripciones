
package com.eventos.eventos_inscripciones;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class ArchivoActividades {

    private static final String RUTA = "data/actividades.txt";

    public static void guardarActividades(List<Actividad> actividades)
            throws IOException {

        Path ruta = Path.of(RUTA);

        Files.createDirectories(ruta.getParent());

        try (BufferedWriter escritor = Files.newBufferedWriter(ruta)) {

            for (Actividad actividad : actividades) {

                escritor.write(
                    actividad.getId() + "|" +
                    actividad.getIdEvento() + "|" +
                    limpiarTexto(actividad.getNombre()) + "|" +
                    limpiarTexto(actividad.getDescripcion()) + "|" +
                    actividad.getFechaInicio() + "|" +
                    actividad.getFechaFin()
                );

                escritor.newLine();
            }
        }
    }

    public static List<Actividad> cargarActividades()
            throws IOException {

        List<Actividad> actividades = new ArrayList<>();

        Path ruta = Path.of(RUTA);

        if (!Files.exists(ruta)) {
            return actividades;
        }

        try (BufferedReader lector = Files.newBufferedReader(ruta)) {

            String linea;

            while ((linea = lector.readLine()) != null) {

                String[] datos = linea.split("\\|", -1);

                if (datos.length == 6) {

                    try {

                        int id = Integer.parseInt(datos[0]);
                        int idEvento = Integer.parseInt(datos[1]);

                        String nombre = datos[2];
                        String descripcion = datos[3];

                        LocalDateTime fechaInicio =
                                LocalDateTime.parse(datos[4]);

                        LocalDateTime fechaFin =
                                LocalDateTime.parse(datos[5]);

                        Actividad actividad = new Actividad(
                            id,
                            idEvento,
                            nombre,
                            descripcion,
                            fechaInicio,
                            fechaFin
                        );

                        actividades.add(actividad);

                    } catch (NumberFormatException e) {

                        System.out.println("Error en los números de una actividad.");

                    } catch (java.time.format.DateTimeParseException e) {

                        System.out.println("Error en las fechas de una actividad.");
                    }

                } else {

                    System.out.println("Una línea de actividades tiene datos incorrectos.");
                }
            }
        }

        return actividades;
    }

    private static String limpiarTexto(String texto) {

        if (texto == null) {
            return "";
        }

        return texto.replace("|", "/")
                    .replace("\n", " ")
                    .replace("\r", " ");
    }
}
