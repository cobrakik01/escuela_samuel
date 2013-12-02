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
@Table(name = "materia_asignada", catalog = "escuela_samuel", schema = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "MateriaAsignada.findAll", query = "SELECT m FROM MateriaAsignada m"),
    @NamedQuery(name = "MateriaAsignada.findById", query = "SELECT m FROM MateriaAsignada m WHERE m.materiaAsignadaPK.id = :id"),
    @NamedQuery(name = "MateriaAsignada.findByProfesorId", query = "SELECT m FROM MateriaAsignada m WHERE m.materiaAsignadaPK.profesorId = :profesorId"),
    @NamedQuery(name = "MateriaAsignada.findByMateriaId", query = "SELECT m FROM MateriaAsignada m WHERE m.materiaAsignadaPK.materiaId = :materiaId"),
    @NamedQuery(name = "MateriaAsignada.findByFechaAsignacion", query = "SELECT m FROM MateriaAsignada m WHERE m.fechaAsignacion = :fechaAsignacion")})
public class MateriaAsignada implements Serializable {
    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected MateriaAsignadaPK materiaAsignadaPK;
    @Basic(optional = false)
    @Column(name = "fecha_asignacion", nullable = false)
    @Temporal(TemporalType.DATE)
    private Date fechaAsignacion;
    @JoinColumn(name = "materia_id", referencedColumnName = "id", nullable = false, insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private Materia materia;
    @JoinColumn(name = "profesor_id", referencedColumnName = "id", nullable = false, insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private Profesor profesor;

    public MateriaAsignada() {
    }

    public MateriaAsignada(MateriaAsignadaPK materiaAsignadaPK) {
        this.materiaAsignadaPK = materiaAsignadaPK;
    }

    public MateriaAsignada(MateriaAsignadaPK materiaAsignadaPK, Date fechaAsignacion) {
        this.materiaAsignadaPK = materiaAsignadaPK;
        this.fechaAsignacion = fechaAsignacion;
    }

    public MateriaAsignada(int id, int profesorId, int materiaId) {
        this.materiaAsignadaPK = new MateriaAsignadaPK(id, profesorId, materiaId);
    }

    public MateriaAsignadaPK getMateriaAsignadaPK() {
        return materiaAsignadaPK;
    }

    public void setMateriaAsignadaPK(MateriaAsignadaPK materiaAsignadaPK) {
        this.materiaAsignadaPK = materiaAsignadaPK;
    }

    public Date getFechaAsignacion() {
        return fechaAsignacion;
    }

    public void setFechaAsignacion(Date fechaAsignacion) {
        this.fechaAsignacion = fechaAsignacion;
    }

    public Materia getMateria() {
        return materia;
    }

    public void setMateria(Materia materia) {
        this.materia = materia;
    }

    public Profesor getProfesor() {
        return profesor;
    }

    public void setProfesor(Profesor profesor) {
        this.profesor = profesor;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (materiaAsignadaPK != null ? materiaAsignadaPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof MateriaAsignada)) {
            return false;
        }
        MateriaAsignada other = (MateriaAsignada) object;
        if ((this.materiaAsignadaPK == null && other.materiaAsignadaPK != null) || (this.materiaAsignadaPK != null && !this.materiaAsignadaPK.equals(other.materiaAsignadaPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.samuel.escuela.entity.MateriaAsignada[ materiaAsignadaPK=" + materiaAsignadaPK + " ]";
    }
    
}
