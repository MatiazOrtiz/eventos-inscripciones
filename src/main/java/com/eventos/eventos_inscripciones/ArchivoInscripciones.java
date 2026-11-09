
package com.eventos.eventos_inscripciones;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class ArchivoInscripciones {

    private static final String RUTA = "data/inscripciones.txt";

    public static void guardarInscripciones(List<Inscripcion> inscripciones)
            throws IOException {

        Path ruta = Path.of(RUTA);

        Files.createDirectories(ruta.getParent());

        try (BufferedWriter escritor = Files.newBufferedWriter(ruta)) {

            for (Inscripcion inscripcion : inscripciones) {

                escritor.write(
                    inscripcion.getId() + "|" +
                    inscripcion.getIdEvento() + "|" +
                    limpiarTexto(inscripcion.getNombre()) + "|" +
                    limpiarTexto(inscripcion.getDocumento()) + "|" +
                    limpiarTexto(inscripcion.getCorreo()) + "|" +
                    limpiarTexto(inscripcion.getEstado())
                );

                escritor.newLine();
            }
        }
    }

    public static List<Inscripcion> cargarInscripciones()
            throws IOException {

        List<Inscripcion> inscripciones = new ArrayList<>();

        Path ruta = Path.of(RUTA);

        if (!Files.exists(ruta)) {
            return inscripciones;
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
                        String documento = datos[3];
                        String correo = datos[4];
                        String estado = datos[5];

                        Inscripcion inscripcion = new Inscripcion(
                            id,
                            idEvento,
                            nombre,
                            documento,
                            correo,
                            estado
                        );

                        inscripciones.add(inscripcion);

                    } catch (NumberFormatException e) {

                        System.out.println("Error en los números de una inscripción.");

                    }

                } else {

                    System.out.println("Una línea de inscripciones tiene datos incorrectos.");
                }
            }
        }

        return inscripciones;
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
    