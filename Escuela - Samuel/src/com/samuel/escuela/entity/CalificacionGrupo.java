/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.samuel.escuela.entity;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author cobrakik01
 */
@Entity
@Table(name = "calificacion_grupo", catalog = "escuela_samuel", schema = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "CalificacionGrupo.findAll", query = "SELECT c FROM CalificacionGrupo c"),
    @NamedQuery(name = "CalificacionGrupo.findById", query = "SELECT c FROM CalificacionGrupo c WHERE c.calificacionGrupoPK.id = :id"),
    @NamedQuery(name = "CalificacionGrupo.findByAlumnoId", query = "SELECT c FROM CalificacionGrupo c WHERE c.calificacionGrupoPK.alumnoId = :alumnoId"),
    @NamedQuery(name = "CalificacionGrupo.findByGrupoId", query = "SELECT c FROM CalificacionGrupo c WHERE c.calificacionGrupoPK.grupoId = :grupoId"),
    @NamedQuery(name = "CalificacionGrupo.findByCalificacion", query = "SELECT c FROM CalificacionGrupo c WHERE c.calificacion = :calificacion"),
    @NamedQuery(name = "CalificacionGrupo.findByFechaCreada", query = "SELECT c FROM CalificacionGrupo c WHERE c.fechaCreada = :fechaCreada"),
    @NamedQuery(name = "CalificacionGrupo.findByFechaEditada", query = "SELECT c FROM CalificacionGrupo c WHERE c.fechaEditada = :fechaEditada")})
public class CalificacionGrupo implements Serializable {
    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected CalificacionGrupoPK calificacionGrupoPK;
    @Basic(optional = false)
    @Column(nullable = false)
    private double calificacion;
    @Basic(optional = false)
    @Column(name = "fecha_creada", nullable = false)
    @Temporal(TemporalType.DATE)
    private Date fechaCreada;
    @Basic(optional = false)
    @Column(name = "fecha_editada", nullable = false)
    @Temporal(TemporalType.DATE)
    private Date fechaEditada;
    @JoinColumn(name = "grupo_id", referencedColumnName = "id", nullable = false, insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private Grupo grupo;
    @JoinColumn(name = "alumno_id", referencedColumnName = "id", nullable = false, insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private Alumno alumno;

    public CalificacionGrupo() {
    }

    public CalificacionGrupo(CalificacionGrupoPK calificacionGrupoPK) {
        this.calificacionGrupoPK = calificacionGrupoPK;
    }

    public CalificacionGrupo(CalificacionGrupoPK calificacionGrupoPK, double calificacion, Date fechaCreada, Date fechaEditada) {
        this.calificacionGrupoPK = calificacionGrupoPK;
        this.calificacion = calificacion;
        this.fechaCreada = fechaCreada;
        this.fechaEditada = fechaEditada;
    }

    public CalificacionGrupo(int id, int alumnoId, int grupoId) {
        this.calificacionGrupoPK = new CalificacionGrupoPK(id, alumnoId, grupoId);
    }

    public CalificacionGrupoPK getCalificacionGrupoPK() {
        return calificacionGrupoPK;
    }

    public void setCalificacionGrupoPK(CalificacionGrupoPK calificacionGrupoPK) {
        this.calificacionGrupoPK = calificacionGrupoPK;
    }

    public double getCalificacion() {
        return calificacion;
    }

    public void setCalificacion(double calificacion) {
        this.calificacion = calificacion;
    }

    public Date getFechaCreada() {
        return fechaCreada;
    }

    public void setFechaCreada(Date fechaCreada) {
        this.fechaCreada = fechaCreada;
    }

    public Date getFechaEditada() {
        return fechaEditada;
    }

    public void setFechaEditada(Date fechaEditada) {
        this.fechaEditada = fechaEditada;
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
        hash += (calificacionGrupoPK != null ? calificacionGrupoPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof CalificacionGrupo)) {
            return false;
        }
        CalificacionGrupo other = (CalificacionGrupo) object;
        if ((this.calificacionGrupoPK == null && other.calificacionGrupoPK != null) || (this.calificacionGrupoPK != null && !this.calificacionGrupoPK.equals(other.calificacionGrupoPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.samuel.escuela.entity.CalificacionGrupo[ calificacionGrupoPK=" + calificacionGrupoPK + " ]";
    }
    
}
