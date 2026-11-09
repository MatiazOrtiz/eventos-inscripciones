
package com.eventos.eventos_inscripciones;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class ArchivoAsistencias {

    private static final String RUTA = "data/asistencias.txt";

    public static void guardarAsistencias(List<Asistencia> asistencias) throws IOException {

        File carpeta = new File("data");
        carpeta.mkdirs();

        PrintWriter escritor = new PrintWriter(new FileWriter(RUTA));

        for (Asistencia asistencia : asistencias) {

            escritor.println(
                asistencia.getId() + "|" +
                asistencia.getIdActividad() + "|" +
                asistencia.getIdInscripcion() + "|" +
                asistencia.isAsistio()
            );
        }

        escritor.close();
    }

    public static List<Asistencia> cargarAsistencias() throws IOException {

        List<Asistencia> asistencias = new ArrayList<>();

        File archivo = new File(RUTA);

        if (!archivo.exists()) {
            return asistencias;
        }

        BufferedReader lector = new BufferedReader(new FileReader(RUTA));

        String linea;

        while ((linea = lector.readLine()) != null) {

            String[] datos = linea.split("\\|");

            int id = Integer.parseInt(datos[0]);
            int idActividad = Integer.parseInt(datos[1]);
            int idInscripcion = Integer.parseInt(datos[2]);
            boolean asistio = Boolean.parseBoolean(datos[3]);

            Asistencia asistencia = new Asistencia(
                id, idActividad, idInscripcion, asistio
            );

            asistencias.add(asistencia);
        }

        lector.close();

        return asistencias;
    }
}
