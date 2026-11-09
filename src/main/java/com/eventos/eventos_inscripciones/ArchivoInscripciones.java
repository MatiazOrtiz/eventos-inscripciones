
package com.eventos.eventos_inscripciones;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

public class ArchivoInscripciones {

    private static final String RUTA = "data/inscripciones.txt";

    public static void guardarInscripciones(List<Inscripcion> inscripciones)
            throws IOException {

        File archivo = new File(RUTA);

        archivo.getParentFile().mkdirs();

        try (PrintWriter escritor = new PrintWriter(new FileWriter(archivo))) {

            for (Inscripcion inscripcion : inscripciones) {

                escritor.println(
                    inscripcion.getId() + "|" +
                    inscripcion.getIdEvento() + "|" +
                    inscripcion.getNombre().replace("|", "/") + "|" +
                    inscripcion.getDocumento().replace("|", "/") + "|" +
                    inscripcion.getCorreo().replace("|", "/") + "|" +
                    inscripcion.getEstado()
                );
            }
        }
    }

    public static List<Inscripcion> cargarInscripciones()
            throws IOException {

        List<Inscripcion> inscripciones = new ArrayList<>();

        File archivo = new File(RUTA);

        if (!archivo.exists()) {
            return inscripciones;
        }

        try (BufferedReader lector = new BufferedReader(new FileReader(archivo))) {

            String linea;

            while ((linea = lector.readLine()) != null) {

                String[] datos = linea.split("\\|", -1);

                if (datos.length == 6) {

                    Inscripcion inscripcion = new Inscripcion(
                        Integer.parseInt(datos[0]),
                        Integer.parseInt(datos[1]),
                        datos[2],
                        datos[3],
                        datos[4],
                        datos[5]
                    );

                    inscripciones.add(inscripcion);
                }
            }
        }

        return inscripciones;
    }
}
