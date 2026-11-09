
package com.eventos.eventos_inscripciones;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class ArchivoAsistencias {

    private static final String RUTA = "data/asistencias.txt";

    public static void guardarAsistencias(List<Asistencia> asistencias)
            throws IOException {

        Path ruta = Path.of(RUTA);

        Files.createDirectories(ruta.getParent());

        try (BufferedWriter escritor = Files.newBufferedWriter(ruta)) {

            for (Asistencia asistencia : asistencias) {

                escritor.write(
                    asistencia.getId() + "|" +
                    asistencia.getIdActividad() + "|" +
                    asistencia.getIdInscripcion() + "|" +
                    asistencia.isAsistio()
                );

                escritor.newLine();
            }
        }
    }

    public static List<Asistencia> cargarAsistencias()
            throws IOException {

        List<Asistencia> asistencias = new ArrayList<>();

        Path ruta = Path.of(RUTA);

        if (!Files.exists(ruta)) {
            return asistencias;
        }

        try (BufferedReader lector = Files.newBufferedReader(ruta)) {

            String linea;

            while ((linea = lector.readLine()) != null) {

                String[] datos = linea.split("\\|", -1);

                if (datos.length == 4) {

                    try {

                        int id = Integer.parseInt(datos[0]);
                        int idActividad = Integer.parseInt(datos[1]);
                        int idInscripcion = Integer.parseInt(datos[2]);

                        if (!datos[3].equals("true") &&
                            !datos[3].equals("false")) {

                            System.out.println("Estado de asistencia incorrecto.");
                            continue;
                        }

                        boolean asistio = Boolean.parseBoolean(datos[3]);

                        Asistencia asistencia = new Asistencia(
                            id,
                            idActividad,
                            idInscripcion,
                            asistio
                        );

                        asistencias.add(asistencia);

                    } catch (NumberFormatException e) {

                        System.out.println("Error en los números de una asistencia.");
                    }

                } else {

                    System.out.println("Una línea de asistencias tiene datos incorrectos.");
                }
            }
        }

        return asistencias;
    }
}
