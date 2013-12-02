/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.samuel.escuela.entity;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author cobrakik01
 */
@Entity
@Table(catalog = "escuela_samuel", schema = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Materia.findAll", query = "SELECT m FROM Materia m"),
    @NamedQuery(name = "Materia.findById", query = "SELECT m FROM Materia m WHERE m.id = :id"),
    @NamedQuery(name = "Materia.findByNombre", query = "SELECT m FROM Materia m WHERE m.nombre = :nombre"),
    @NamedQuery(name = "Materia.findByPlan", query = "SELECT m FROM Materia m WHERE m.plan = :plan")})
public class Materia implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(nullable = false)
    private Integer id;
    public static final String PROP_ID = "id";
    @Basic(optional = false)
    @Column(nullable = false, length = 100)
    private String nombre;
    public static final String PROP_NOMBRE = "nombre";
    @Basic(optional = false)
    @Column(nullable = false)
    @Temporal(TemporalType.DATE)
    private Date plan;
    public static final String PROP_PLAN = "plan";
    @JoinColumn(name = "grupo_id", referencedColumnName = "id", nullable = false)
    @ManyToOne(optional = false)
    private Grupo grupoId;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "materia")
    private List<MateriaAsignada> materiaAsignadaList;
    
    private transient final PropertyChangeSupport propertyChangeSupport = new PropertyChangeSupport(this);

    public Materia() {
    }

    public Materia(Integer id) {
        this.id = id;
    }

    public Materia(Integer id, String nombre, Date plan) {
        this.id = id;
        this.nombre = nombre;
        this.plan = plan;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        Integer oldId = this.id;
        this.id = id;
        propertyChangeSupport.firePropertyChange(PROP_ID, oldId, id);
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        String oldNombre = this.nombre;
        this.nombre = nombre;
        propertyChangeSupport.firePropertyChange(PROP_NOMBRE, oldNombre, nombre);
    }

    public Date getPlan() {
        return plan;
    }

    public void setPlan(Date plan) {
        this.plan = plan;
    }

    public Grupo getGrupoId() {
        return grupoId;
    }

    public void setGrupoId(Grupo grupoId) {
        this.grupoId = grupoId;
    }

    @XmlTransient
    public List<MateriaAsignada> getMateriaAsignadaList() {
        return materiaAsignadaList;
    }

    public void setMateriaAsignadaList(List<MateriaAsignada> materiaAsignadaList) {
        this.materiaAsignadaList = materiaAsignadaList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Materia)) {
            return false;
        }
        Materia other = (Materia) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.samuel.escuela.entity.Materia[ id=" + id + " ]";
    }

    public void addPropertyChangeListener(PropertyChangeListener listener) {
        propertyChangeSupport.addPropertyChangeListener(listener);
    }

    public void removePropertyChangeListener(PropertyChangeListener listener) {
        propertyChangeSupport.removePropertyChangeListener(listener);
    }
    
    
}
