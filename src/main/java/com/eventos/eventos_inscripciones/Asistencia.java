
package com.eventos.eventos_inscripciones;

public class Asistencia {

    private int id;
    private int idActividad;
    private int idInscripcion;
    private boolean asistio;

    public Asistencia(int id, int idActividad, int idInscripcion, boolean asistio) {
        this.id = id;
        this.idActividad = idActividad;
        this.idInscripcion = idInscripcion;
        this.asistio = asistio;
    }

    public int getId() {
        return id;
    }

    public int getIdActividad() {
        return idActividad;
    }

    public int getIdInscripcion() {
        return idInscripcion;
    }

    public boolean isAsistio() {
        return asistio;
    }

    public void setIdActividad(int idActividad) {
        this.idActividad = idActividad;
    }

    public void setIdInscripcion(int idInscripcion) {
        this.idInscripcion = idInscripcion;
    }

    public void setAsistio(boolean asistio) {
        this.asistio = asistio;
    }
}
