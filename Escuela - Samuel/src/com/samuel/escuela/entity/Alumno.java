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
    @NamedQuery(name = "Alumno.findAll", query = "SELECT a FROM Alumno a"),
    @NamedQuery(name = "Alumno.findById", query = "SELECT a FROM Alumno a WHERE a.id = :id"),
    @NamedQuery(name = "Alumno.findByNombre", query = "SELECT a FROM Alumno a WHERE a.nombre = :nombre"),
    @NamedQuery(name = "Alumno.findByApellidoPaterno", query = "SELECT a FROM Alumno a WHERE a.apellidoPaterno = :apellidoPaterno"),
    @NamedQuery(name = "Alumno.findByApellidoMaterno", query = "SELECT a FROM Alumno a WHERE a.apellidoMaterno = :apellidoMaterno")})
public class Alumno implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(nullable = false)
    private Integer id;
    public static final String PROP_ID = "id";
    @Basic(optional = false)
    @Column(nullable = false, length = 45)
    private String nombre;
    public static final String PROP_NOMBRE = "nombre";
    @Basic(optional = false)
    @Column(name = "apellido_paterno", nullable = false, length = 45)
    private String apellidoPaterno;
    public static final String PROP_APELLIDOPATERNO = "apellidoPaterno";
    @Column(name = "apellido_materno", length = 45)
    private String apellidoMaterno;
    public static final String PROP_APELLIDOMATERNO = "apellidoMaterno";
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "alumno")
    private List<GrupoAlumno> grupoAlumnoList;
    public static final String PROP_GRUPOALUMNOLIST = "grupoAlumnoList";

    public Alumno() {
    }

    public Alumno(Integer id) {
        this.id = id;
    }

    public Alumno(Integer id, String nombre, String apellidoPaterno) {
        this.id = id;
        this.nombre = nombre;
        this.apellidoPaterno = apellidoPaterno;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        Integer oldId = this.id;
        this.id = id;
        propertyChangeSupport.firePropertyChange(PROP_ID, oldId, id);
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

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        String oldNombre = this.nombre;
        this.nombre = nombre;
        propertyChangeSupport.firePropertyChange(PROP_NOMBRE, oldNombre, nombre);
    }

    public String getApellidoPaterno() {
        return apellidoPaterno;
    }

    public void setApellidoPaterno(String apellidoPaterno) {
        String oldApellidoPaterno = this.apellidoPaterno;
        this.apellidoPaterno = apellidoPaterno;
        propertyChangeSupport.firePropertyChange(PROP_APELLIDOPATERNO, oldApellidoPaterno, apellidoPaterno);
    }

    public String getApellidoMaterno() {
        return apellidoMaterno;
    }

    public void setApellidoMaterno(String apellidoMaterno) {
        String oldApellidoMaterno = this.apellidoMaterno;
        this.apellidoMaterno = apellidoMaterno;
        propertyChangeSupport.firePropertyChange(PROP_APELLIDOMATERNO, oldApellidoMaterno, apellidoMaterno);
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

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Alumno)) {
            return false;
        }
        Alumno other = (Alumno) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.samuel.escuela.entity.Alumno[ id=" + id + " ]";
    }
}
