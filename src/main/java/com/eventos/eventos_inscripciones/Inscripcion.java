
package com.eventos.eventos_inscripciones;

public class Inscripcion {

    private int id;
    private int idEvento;
    private String nombre;
    private String documento;
    private String correo;
    private String estado;

    public Inscripcion(int id, int idEvento, String nombre,
            String documento, String correo, String estado) {

        this.id = id;
        this.idEvento = idEvento;
        this.nombre = nombre;
        this.documento = documento;
        this.correo = correo;
        this.estado = estado;
    }

    public int getId() {
        return id;
    }

    public int getIdEvento() {
        return idEvento;
    }

    public String getNombre() {
        return nombre;
    }

    public String getDocumento() {
        return documento;
    }

    public String getCorreo() {
        return correo;
    }

    public String getEstado() {
        return estado;
    }

    public void setIdEvento(int idEvento) {
        this.idEvento = idEvento;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setDocumento(String documento) {
        this.documento = documento;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }
}
