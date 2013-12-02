/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.samuel.escuela.controller;

import com.samuel.escuela.controller.exceptions.NonexistentEntityException;
import com.samuel.escuela.controller.exceptions.PreexistingEntityException;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import com.samuel.escuela.entity.Grupo;
import com.samuel.escuela.entity.Alumno;
import com.samuel.escuela.entity.GrupoAlumno;
import com.samuel.escuela.entity.GrupoAlumnoPK;
import java.util.List;
import javax.persistence.EntityManager;

/**
 *
 * @author cobrakik01
 */
public class GrupoAlumnoJpaController extends BaseController implements Serializable {

    public void create(GrupoAlumno grupoAlumno) throws PreexistingEntityException, Exception {
        if (grupoAlumno.getGrupoAlumnoPK() == null) {
            grupoAlumno.setGrupoAlumnoPK(new GrupoAlumnoPK());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Grupo grupo = grupoAlumno.getGrupo();
            if (grupo != null) {
                grupo = em.getReference(grupo.getClass(), grupo.getId());
                grupoAlumno.setGrupo(grupo);
            }
            Alumno alumno = grupoAlumno.getAlumno();
            if (alumno != null) {
                alumno = em.getReference(alumno.getClass(), alumno.getId());
                grupoAlumno.setAlumno(alumno);
            }
            em.persist(grupoAlumno);
            if (grupo != null) {
                grupo.getGrupoAlumnoList().add(grupoAlumno);
                grupo = em.merge(grupo);
            }
            if (alumno != null) {
                alumno.getGrupoAlumnoList().add(grupoAlumno);
                alumno = em.merge(alumno);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findGrupoAlumno(grupoAlumno.getGrupoAlumnoPK()) != null) {
                throw new PreexistingEntityException("GrupoAlumno " + grupoAlumno + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(GrupoAlumno grupoAlumno) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            GrupoAlumno persistentGrupoAlumno = em.find(GrupoAlumno.class, grupoAlumno.getGrupoAlumnoPK());
            Grupo grupoOld = persistentGrupoAlumno.getGrupo();
            Grupo grupoNew = grupoAlumno.getGrupo();
            Alumno alumnoOld = persistentGrupoAlumno.getAlumno();
            Alumno alumnoNew = grupoAlumno.getAlumno();
            if (grupoNew != null) {
                grupoNew = em.getReference(grupoNew.getClass(), grupoNew.getId());
                grupoAlumno.setGrupo(grupoNew);
            }
            if (alumnoNew != null) {
                alumnoNew = em.getReference(alumnoNew.getClass(), alumnoNew.getId());
                grupoAlumno.setAlumno(alumnoNew);
            }
            grupoAlumno = em.merge(grupoAlumno);
            if (grupoOld != null && !grupoOld.equals(grupoNew)) {
                grupoOld.getGrupoAlumnoList().remove(grupoAlumno);
                grupoOld = em.merge(grupoOld);
            }
            if (grupoNew != null && !grupoNew.equals(grupoOld)) {
                grupoNew.getGrupoAlumnoList().add(grupoAlumno);
                grupoNew = em.merge(grupoNew);
            }
            if (alumnoOld != null && !alumnoOld.equals(alumnoNew)) {
                alumnoOld.getGrupoAlumnoList().remove(grupoAlumno);
                alumnoOld = em.merge(alumnoOld);
            }
            if (alumnoNew != null && !alumnoNew.equals(alumnoOld)) {
                alumnoNew.getGrupoAlumnoList().add(grupoAlumno);
                alumnoNew = em.merge(alumnoNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                GrupoAlumnoPK id = grupoAlumno.getGrupoAlumnoPK();
                if (findGrupoAlumno(id) == null) {
                    throw new NonexistentEntityException("The grupoAlumno with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(GrupoAlumnoPK id) throws NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            GrupoAlumno grupoAlumno;
            try {
                grupoAlumno = em.getReference(GrupoAlumno.class, id);
                grupoAlumno.getGrupoAlumnoPK();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The grupoAlumno with id " + id + " no longer exists.", enfe);
            }
            Grupo grupo = grupoAlumno.getGrupo();
            if (grupo != null) {
                grupo.getGrupoAlumnoList().remove(grupoAlumno);
                grupo = em.merge(grupo);
            }
            Alumno alumno = grupoAlumno.getAlumno();
            if (alumno != null) {
                alumno.getGrupoAlumnoList().remove(grupoAlumno);
                alumno = em.merge(alumno);
            }
            em.remove(grupoAlumno);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<GrupoAlumno> findGrupoAlumnoEntities() {
        return findGrupoAlumnoEntities(true, -1, -1);
    }

    public List<GrupoAlumno> findGrupoAlumnoEntities(int maxResults, int firstResult) {
        return findGrupoAlumnoEntities(false, maxResults, firstResult);
    }

    private List<GrupoAlumno> findGrupoAlumnoEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(GrupoAlumno.class));
            Query q = em.createQuery(cq);
            if (!all) {
                q.setMaxResults(maxResults);
                q.setFirstResult(firstResult);
            }
            return q.getResultList();
        } finally {
            em.close();
        }
    }

    public GrupoAlumno findGrupoAlumno(GrupoAlumnoPK id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(GrupoAlumno.class, id);
        } finally {
            em.close();
        }
    }

    public int getGrupoAlumnoCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<GrupoAlumno> rt = cq.from(GrupoAlumno.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }

}
