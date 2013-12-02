/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.samuel.escuela.entity;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 *
 * @author cobrakik01
 */
@Embeddable
public class MateriaAsignadaPK implements Serializable {
    @Basic(optional = false)
    @Column(nullable = false)
    private int id;
    @Basic(optional = false)
    @Column(name = "profesor_id", nullable = false)
    private int profesorId;
    @Basic(optional = false)
    @Column(name = "materia_id", nullable = false)
    private int materiaId;

    public MateriaAsignadaPK() {
    }

    public MateriaAsignadaPK(int id, int profesorId, int materiaId) {
        this.id = id;
        this.profesorId = profesorId;
        this.materiaId = materiaId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getProfesorId() {
        return profesorId;
    }

    public void setProfesorId(int profesorId) {
        this.profesorId = profesorId;
    }

    public int getMateriaId() {
        return materiaId;
    }

    public void setMateriaId(int materiaId) {
        this.materiaId = materiaId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (int) id;
        hash += (int) profesorId;
        hash += (int) materiaId;
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof MateriaAsignadaPK)) {
            return false;
        }
        MateriaAsignadaPK other = (MateriaAsignadaPK) object;
        if (this.id != other.id) {
            return false;
        }
        if (this.profesorId != other.profesorId) {
            return false;
        }
        if (this.materiaId != other.materiaId) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.samuel.escuela.entity.MateriaAsignadaPK[ id=" + id + ", profesorId=" + profesorId + ", materiaId=" + materiaId + " ]";
    }
    
}
