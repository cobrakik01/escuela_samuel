/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.samuel.escuela.entity;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.beans.VetoableChangeListener;
import java.beans.VetoableChangeSupport;
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
    @NamedQuery(name = "Grupo.findAll", query = "SELECT g FROM Grupo g"),
    @NamedQuery(name = "Grupo.findById", query = "SELECT g FROM Grupo g WHERE g.id = :id"),
    @NamedQuery(name = "Grupo.findByNombre", query = "SELECT g FROM Grupo g WHERE g.nombre = :nombre"),
    @NamedQuery(name = "Grupo.findByFechaCreacion", query = "SELECT g FROM Grupo g WHERE g.fechaCreacion = :fechaCreacion"),
    @NamedQuery(name = "Grupo.findByCalificacionGrupal", query = "SELECT g FROM Grupo g WHERE g.calificacionGrupal = :calificacionGrupal")})
public class Grupo implements Serializable {

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
    @Column(name = "fecha_creacion", nullable = false)
    @Temporal(TemporalType.DATE)
    private Date fechaCreacion;
    public static final String PROP_FECHACREACION = "fechaCreacion";
    @Basic(optional = false)
    @Column(name = "calificacion_grupal", nullable = false)
    private double calificacionGrupal;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "grupo")
    private List<GrupoAlumno> grupoAlumnoList;
    public static final String PROP_GRUPOALUMNOLIST = "grupoAlumnoList";
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "grupoId")
    private List<Materia> materiaList;
    public static final String PROP_MATERIALIST = "materiaList";

    public Grupo() {
    }

    public Grupo(Integer id) {
        this.id = id;
    }

    public Grupo(Integer id, String nombre, Date fechaCreacion, double calificacionGrupal) {
        this.id = id;
        this.nombre = nombre;
        this.fechaCreacion = fechaCreacion;
        this.calificacionGrupal = calificacionGrupal;
    }

    @XmlTransient
    public List<GrupoAlumno> getGrupoAlumnoList() {
        return grupoAlumnoList;
    }

    public void setGrupoAlumnoList(List<GrupoAlumno> grupoAlumnoList) {
        List<GrupoAlumno> oldGrupoAlumnoList = this.grupoAlumnoList;
        this.grupoAlumnoList = grupoAlumnoList;
        propertyChangeSupport.firePropertyChange(PROP_GRUPOALUMNOLIST, oldGrupoAlumnoList, grupoAlumnoList);
    }

    @XmlTransient
    public List<Materia> getMateriaList() {
        return materiaList;
    }

    public void setMateriaList(List<Materia> materiaList) {
        List<Materia> oldMateriaList = this.materiaList;
        this.materiaList = materiaList;
        propertyChangeSupport.firePropertyChange(PROP_MATERIALIST, oldMateriaList, materiaList);
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
        if (!(object instanceof Grupo)) {
            return false;
        }
        Grupo other = (Grupo) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return this.getNombre();
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

    public Date getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(Date fechaCreacion) {
        Date oldFechaCreacion = this.fechaCreacion;
        this.fechaCreacion = fechaCreacion;
        propertyChangeSupport.firePropertyChange(PROP_FECHACREACION, oldFechaCreacion, fechaCreacion);
    }

    public static final String PROP_CALIFICACIONGRUPAL = "calificacionGrupal";

    public double getCalificacionGrupal() {
        return calificacionGrupal;
    }

    public void setCalificacionGrupal(double calificacionGrupal) {
        double oldCalificacionGrupal = this.calificacionGrupal;
        this.calificacionGrupal = calificacionGrupal;
        propertyChangeSupport.firePropertyChange(PROP_CALIFICACIONGRUPAL, oldCalificacionGrupal, calificacionGrupal);
    }

    private transient final PropertyChangeSupport propertyChangeSupport = new PropertyChangeSupport(this);

    public void addPropertyChangeListener(PropertyChangeListener listener) {
        propertyChangeSupport.addPropertyChangeListener(listener);
    }

    public void removePropertyChangeListener(PropertyChangeListener listener) {
        propertyChangeSupport.removePropertyChangeListener(listener);
    }

    private transient final VetoableChangeSupport vetoableChangeSupport = new VetoableChangeSupport(this);

    public void addVetoableChangeListener(VetoableChangeListener listener) {
        vetoableChangeSupport.addVetoableChangeListener(listener);
    }

    public void removeVetoableChangeListener(VetoableChangeListener listener) {
        vetoableChangeSupport.removeVetoableChangeListener(listener);
    }
}
