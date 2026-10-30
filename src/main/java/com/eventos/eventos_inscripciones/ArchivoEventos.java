
package com.eventos.eventos_inscripciones;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.time.LocalDateTime;

public class ArchivoEventos {

    private static final String RUTA = "data/eventos.txt";

    // Guardar eventos en el archivo

    public static void guardarEventos(List<Evento> eventos) throws IOException {

        Path ruta = Path.of(RUTA);

        Files.createDirectories(ruta.getParent());

        try (PrintWriter escritor = new PrintWriter(
                Files.newBufferedWriter(ruta))) {

            for (Evento evento : eventos) {

                escritor.println(
                    evento.getId() + "|" +
                    limpiarTexto(evento.getNombre()) + "|" +
                    limpiarTexto(evento.getCategoria()) + "|" +
                    evento.getFechaHora() + "|" +
                    limpiarTexto(evento.getLugar()) + "|" +
                    evento.getCapacidad() + "|" +
                    limpiarTexto(evento.getDescripcion())
                );
            }
        }
    }

    // Leer eventos del archivo

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
                        LocalDateTime fechaHora = LocalDateTime.parse(datos[3]);
                        String lugar = datos[4];
                        int capacidad = Integer.parseInt(datos[5]);
                        String descripcion = datos[6];

                        Evento evento = new Evento(
                            id,
                            nombre,
                            categoria,
                            fechaHora,
                            lugar,
                            capacidad,
                            descripcion
                        );

                        eventos.add(evento);

                    } catch (NumberFormatException e) {

                        System.out.println("Error en los números de una línea.");

                    } catch (java.time.format.DateTimeParseException e) {

                        System.out.println("Error en la fecha de un evento.");

                    }
                }
            }
        }

        return eventos;
    }

    // Evitar que los datos dañen la separación del archivo

    private static String limpiarTexto(String texto) {

        if (texto == null) {
            return "";
        }

        return texto.replace("|", "/")
                    .replace("\n", " ")
                    .replace("\r", " ");
    }
}
