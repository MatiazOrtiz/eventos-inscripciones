package com.eventos.eventos_inscripciones;

import java.time.LocalDateTime;

public class Evento {

    private int id;
    private String nombre;
    private String categoria;
    private LocalDateTime fechaHora;
    private String lugar;
    private int capacidad;
    private String descripcion;

    public Evento(int id, String nombre, String categoria,
            LocalDateTime fechaHora, String lugar,
            int capacidad, String descripcion) {

        this.id = id;
        this.nombre = nombre;
        this.categoria = categoria;
        this.fechaHora = fechaHora;
        this.lugar = lugar;
        this.capacidad = capacidad;
        this.descripcion = descripcion;
    }

    public int getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public String getCategoria() {
        return categoria;
    }

    public LocalDateTime getFechaHora() {
        return fechaHora;
    }

    public String getLugar() {
        return lugar;
    }

    public int getCapacidad() {
        return capacidad;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public void setFechaHora(LocalDateTime fechaHora) {
        this.fechaHora = fechaHora;
    }

    public void setLugar(String lugar) {
        this.lugar = lugar;
    }

    public void setCapacidad(int capacidad) {
        this.capacidad = capacidad;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }
}