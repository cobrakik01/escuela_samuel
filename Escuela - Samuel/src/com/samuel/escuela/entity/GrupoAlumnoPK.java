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
public class GrupoAlumnoPK implements Serializable {
    @Basic(optional = false)
    @Column(nullable = false)
    private int id;
    @Basic(optional = false)
    @Column(name = "alumno_id", nullable = false)
    private int alumnoId;
    @Basic(optional = false)
    @Column(name = "grupo_id", nullable = false)
    private int grupoId;

    public GrupoAlumnoPK() {
    }

    public GrupoAlumnoPK(int id, int alumnoId, int grupoId) {
        this.id = id;
        this.alumnoId = alumnoId;
        this.grupoId = grupoId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getAlumnoId() {
        return alumnoId;
    }

    public void setAlumnoId(int alumnoId) {
        this.alumnoId = alumnoId;
    }

    public int getGrupoId() {
        return grupoId;
    }

    public void setGrupoId(int grupoId) {
        this.grupoId = grupoId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (int) id;
        hash += (int) alumnoId;
        hash += (int) grupoId;
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof GrupoAlumnoPK)) {
            return false;
        }
        GrupoAlumnoPK other = (GrupoAlumnoPK) object;
        if (this.id != other.id) {
            return false;
        }
        if (this.alumnoId != other.alumnoId) {
            return false;
        }
        if (this.grupoId != other.grupoId) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.samuel.escuela.entity.GrupoAlumnoPK[ id=" + id + ", alumnoId=" + alumnoId + ", grupoId=" + grupoId + " ]";
    }
    
}
