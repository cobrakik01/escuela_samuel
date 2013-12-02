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
import com.samuel.escuela.entity.CalificacionGrupo;
import com.samuel.escuela.entity.CalificacionGrupoPK;
import java.util.List;
import javax.persistence.EntityManager;

/**
 *
 * @author cobrakik01
 */
public class CalificacionGrupoJpaController extends BaseController implements Serializable {

    public void create(CalificacionGrupo calificacionGrupo) throws PreexistingEntityException, Exception {
        if (calificacionGrupo.getCalificacionGrupoPK() == null) {
            calificacionGrupo.setCalificacionGrupoPK(new CalificacionGrupoPK());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Grupo grupo = calificacionGrupo.getGrupo();
            if (grupo != null) {
                grupo = em.getReference(grupo.getClass(), grupo.getId());
                calificacionGrupo.setGrupo(grupo);
            }
            Alumno alumno = calificacionGrupo.getAlumno();
            if (alumno != null) {
                alumno = em.getReference(alumno.getClass(), alumno.getId());
                calificacionGrupo.setAlumno(alumno);
            }
            em.persist(calificacionGrupo);
            if (grupo != null) {
                grupo.getCalificacionGrupoList().add(calificacionGrupo);
                grupo = em.merge(grupo);
            }
            if (alumno != null) {
                alumno.getCalificacionGrupoList().add(calificacionGrupo);
                alumno = em.merge(alumno);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findCalificacionGrupo(calificacionGrupo.getCalificacionGrupoPK()) != null) {
                throw new PreexistingEntityException("CalificacionGrupo " + calificacionGrupo + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(CalificacionGrupo calificacionGrupo) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            CalificacionGrupo persistentCalificacionGrupo = em.find(CalificacionGrupo.class, calificacionGrupo.getCalificacionGrupoPK());
            Grupo grupoOld = persistentCalificacionGrupo.getGrupo();
            Grupo grupoNew = calificacionGrupo.getGrupo();
            Alumno alumnoOld = persistentCalificacionGrupo.getAlumno();
            Alumno alumnoNew = calificacionGrupo.getAlumno();
            if (grupoNew != null) {
                grupoNew = em.getReference(grupoNew.getClass(), grupoNew.getId());
                calificacionGrupo.setGrupo(grupoNew);
            }
            if (alumnoNew != null) {
                alumnoNew = em.getReference(alumnoNew.getClass(), alumnoNew.getId());
                calificacionGrupo.setAlumno(alumnoNew);
            }
            calificacionGrupo = em.merge(calificacionGrupo);
            if (grupoOld != null && !grupoOld.equals(grupoNew)) {
                grupoOld.getCalificacionGrupoList().remove(calificacionGrupo);
                grupoOld = em.merge(grupoOld);
            }
            if (grupoNew != null && !grupoNew.equals(grupoOld)) {
                grupoNew.getCalificacionGrupoList().add(calificacionGrupo);
                grupoNew = em.merge(grupoNew);
            }
            if (alumnoOld != null && !alumnoOld.equals(alumnoNew)) {
                alumnoOld.getCalificacionGrupoList().remove(calificacionGrupo);
                alumnoOld = em.merge(alumnoOld);
            }
            if (alumnoNew != null && !alumnoNew.equals(alumnoOld)) {
                alumnoNew.getCalificacionGrupoList().add(calificacionGrupo);
                alumnoNew = em.merge(alumnoNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                CalificacionGrupoPK id = calificacionGrupo.getCalificacionGrupoPK();
                if (findCalificacionGrupo(id) == null) {
                    throw new NonexistentEntityException("The calificacionGrupo with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(CalificacionGrupoPK id) throws NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            CalificacionGrupo calificacionGrupo;
            try {
                calificacionGrupo = em.getReference(CalificacionGrupo.class, id);
                calificacionGrupo.getCalificacionGrupoPK();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The calificacionGrupo with id " + id + " no longer exists.", enfe);
            }
            Grupo grupo = calificacionGrupo.getGrupo();
            if (grupo != null) {
                grupo.getCalificacionGrupoList().remove(calificacionGrupo);
                grupo = em.merge(grupo);
            }
            Alumno alumno = calificacionGrupo.getAlumno();
            if (alumno != null) {
                alumno.getCalificacionGrupoList().remove(calificacionGrupo);
                alumno = em.merge(alumno);
            }
            em.remove(calificacionGrupo);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<CalificacionGrupo> findCalificacionGrupoEntities() {
        return findCalificacionGrupoEntities(true, -1, -1);
    }

    public List<CalificacionGrupo> findCalificacionGrupoEntities(int maxResults, int firstResult) {
        return findCalificacionGrupoEntities(false, maxResults, firstResult);
    }

    private List<CalificacionGrupo> findCalificacionGrupoEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(CalificacionGrupo.class));
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

    public CalificacionGrupo findCalificacionGrupo(CalificacionGrupoPK id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(CalificacionGrupo.class, id);
        } finally {
            em.close();
        }
    }

    public int getCalificacionGrupoCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<CalificacionGrupo> rt = cq.from(CalificacionGrupo.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }

}
