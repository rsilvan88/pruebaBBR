package com.evertec.springboot2.crud.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.sql.Timestamp;

@Entity
@Table(name = "tarea")
public class Tarea {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "identificador")
    private long id;

    @Column(name = "descripcion", nullable = false)
    private String descripcion;

    @Column(name = "fecha_creacion", nullable = false)
    private Timestamp fechaCreacion;

    @Column(name = "fecha_modificacion", nullable = false)
    private Timestamp fechaModificacion;

    @Column(name = "vigente", nullable = false)
    private boolean vigente;

    public Tarea() {
    }

    public Tarea(String descripcion, Timestamp fechaCreacion, Timestamp fechaModificacion, boolean vigente) {
        this.descripcion = descripcion;
        this.fechaCreacion = fechaCreacion;
        this.fechaModificacion = fechaModificacion;
        this.vigente = vigente;
    }

    // Getters y setters

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Timestamp getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(Timestamp fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    public Timestamp getFechaModificacion() {
        return fechaModificacion;
    }

    public void setFechaModificacion(Timestamp fechaModificacion) {
        this.fechaModificacion = fechaModificacion;
    }

    public boolean isVigente() {
        return vigente;
    }

    public void setVigente(boolean vigente) {
        this.vigente = vigente;
    }

    @Override
    public String toString() {
        return "Tarea [id=" + id + ", descripcion=" + descripcion + ", fechaCreacion=" + fechaCreacion +
                ", fechaModificacion=" + fechaModificacion + ", vigente=" + vigente + "]";
    }
}