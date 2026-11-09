
package com.eventos.eventos_inscripciones;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.time.LocalDateTime;

public class ArchivoEventos {

    private static final String RUTA = "data/eventos.txt";

    public static void guardarEventos(List<Evento> eventos) throws IOException {

        Path ruta = Path.of(RUTA);

        Files.createDirectories(ruta.getParent());

        try (BufferedWriter escritor = Files.newBufferedWriter(ruta)) {

            for (Evento evento : eventos) {

                escritor.write(
                    evento.getId() + "|" +
                    limpiarTexto(evento.getNombre()) + "|" +
                    limpiarTexto(evento.getCategoria()) + "|" +
                    evento.getFechaHora() + "|" +
                    limpiarTexto(evento.getLugar()) + "|" +
                    evento.getCapacidad() + "|" +
                    limpiarTexto(evento.getDescripcion())
                );

                escritor.newLine();
            }
        }
    }

    public static List<Evento> cargarEventos() throws IOException {

        List<Evento> eventos = new ArrayList<>();

        Path ruta = Path.of(RUTA);

        if (!Files.exists(ruta)) {
            return eventos;
        }

        try (BufferedReader lector = Files.newBufferedReader(ruta)) {

            String linea;

            while ((linea = lector.readLine()) != null) {

                String[] datos = linea.split("\\|", -1);

                if (datos.length == 7) {

                    try {

                        int id = Integer.parseInt(datos[0]);
                        String nombre = datos[1];
                        String categoria = datos[2];
                        LocalDateTime fechaHora =
                                LocalDateTime.parse(datos[3]);
                        String lugar = datos[4];
                        int capacidad = Integer.parseInt(datos[5]);
                        String descripcion = datos[6];

                        Evento evento = new Evento(
                            id, nombre, categoria, fechaHora,
                            lugar, capacidad, descripcion
                        );

                        eventos.add(evento);

                    } catch (NumberFormatException e) {

                        System.out.println("Error en los números de un evento.");

                    } catch (java.time.format.DateTimeParseException e) {

                        System.out.println("Error en la fecha de un evento.");
                    }

                } else {

                    System.out.println("Una línea de eventos tiene datos incorrectos.");
                }
            }
        }

        return eventos;
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
