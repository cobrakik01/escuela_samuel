/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.samuel.escuela.entity;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author cobrakik01
 */
@Entity
@Table(name = "grupo_alumno", catalog = "escuela_samuel", schema = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "GrupoAlumno.findAll", query = "SELECT g FROM GrupoAlumno g"),
    @NamedQuery(name = "GrupoAlumno.findById", query = "SELECT g FROM GrupoAlumno g WHERE g.grupoAlumnoPK.id = :id"),
    @NamedQuery(name = "GrupoAlumno.findByAlumnoId", query = "SELECT g FROM GrupoAlumno g WHERE g.grupoAlumnoPK.alumnoId = :alumnoId"),
    @NamedQuery(name = "GrupoAlumno.findByGrupoId", query = "SELECT g FROM GrupoAlumno g WHERE g.grupoAlumnoPK.grupoId = :grupoId"),
    @NamedQuery(name = "GrupoAlumno.findByCalificacion", query = "SELECT g FROM GrupoAlumno g WHERE g.calificacion = :calificacion")})
public class GrupoAlumno implements Serializable {
    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected GrupoAlumnoPK grupoAlumnoPK;
    @Basic(optional = false)
    @Column(nullable = false)
    private double calificacion;
    @JoinColumn(name = "grupo_id", referencedColumnName = "id", nullable = false, insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private Grupo grupo;
    @JoinColumn(name = "alumno_id", referencedColumnName = "id", nullable = false, insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private Alumno alumno;

    public GrupoAlumno() {
    }

    public GrupoAlumno(GrupoAlumnoPK grupoAlumnoPK) {
        this.grupoAlumnoPK = grupoAlumnoPK;
    }

    public GrupoAlumno(GrupoAlumnoPK grupoAlumnoPK, double calificacion) {
        this.grupoAlumnoPK = grupoAlumnoPK;
        this.calificacion = calificacion;
    }

    public GrupoAlumno(int id, int alumnoId, int grupoId) {
        this.grupoAlumnoPK = new GrupoAlumnoPK(id, alumnoId, grupoId);
    }

    public GrupoAlumnoPK getGrupoAlumnoPK() {
        return grupoAlumnoPK;
    }

    public void setGrupoAlumnoPK(GrupoAlumnoPK grupoAlumnoPK) {
        this.grupoAlumnoPK = grupoAlumnoPK;
    }

    public double getCalificacion() {
        return calificacion;
    }

    public void setCalificacion(double calificacion) {
        this.calificacion = calificacion;
    }

    public Grupo getGrupo() {
        return grupo;
    }

    public void setGrupo(Grupo grupo) {
        this.grupo = grupo;
    }

    public Alumno getAlumno() {
        return alumno;
    }

    public void setAlumno(Alumno alumno) {
        this.alumno = alumno;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (grupoAlumnoPK != null ? grupoAlumnoPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof GrupoAlumno)) {
            return false;
        }
        GrupoAlumno other = (GrupoAlumno) object;
        if ((this.grupoAlumnoPK == null && other.grupoAlumnoPK != null) || (this.grupoAlumnoPK != null && !this.grupoAlumnoPK.equals(other.grupoAlumnoPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.samuel.escuela.entity.GrupoAlumno[ grupoAlumnoPK=" + grupoAlumnoPK + " ]";
    }
    
}
