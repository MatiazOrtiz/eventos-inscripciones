package com.eventos.eventos_inscripciones;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class EventosInscripcionesApplication {

    public static void main(String[] args) {
        SpringApplication.run(EventosInscripcionesApplication.class, args);

        try {
            Thread.sleep(5000);

            new ProcessBuilder(
                "cmd", "/c", "start", "",
                "http://localhost:8080/"
            ).start();

        } catch (Exception e) {
            System.out.println("Error al abrir el navegador: " + e.getMessage());
        }
    }
}