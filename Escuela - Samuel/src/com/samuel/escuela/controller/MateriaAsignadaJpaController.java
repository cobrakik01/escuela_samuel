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
import com.samuel.escuela.entity.Materia;
import com.samuel.escuela.entity.MateriaAsignada;
import com.samuel.escuela.entity.MateriaAsignadaPK;
import com.samuel.escuela.entity.Profesor;
import java.util.List;
import javax.persistence.EntityManager;

/**
 *
 * @author cobrakik01
 */
public class MateriaAsignadaJpaController extends BaseController implements Serializable {

    public void create(MateriaAsignada materiaAsignada) throws PreexistingEntityException, Exception {
        if (materiaAsignada.getMateriaAsignadaPK() == null) {
            materiaAsignada.setMateriaAsignadaPK(new MateriaAsignadaPK());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Materia materia = materiaAsignada.getMateria();
            if (materia != null) {
                materia = em.getReference(materia.getClass(), materia.getId());
                materiaAsignada.setMateria(materia);
            }
            Profesor profesor = materiaAsignada.getProfesor();
            if (profesor != null) {
                profesor = em.getReference(profesor.getClass(), profesor.getId());
                materiaAsignada.setProfesor(profesor);
            }
            em.persist(materiaAsignada);
            if (materia != null) {
                materia.getMateriaAsignadaList().add(materiaAsignada);
                materia = em.merge(materia);
            }
            if (profesor != null) {
                profesor.getMateriaAsignadaList().add(materiaAsignada);
                profesor = em.merge(profesor);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findMateriaAsignada(materiaAsignada.getMateriaAsignadaPK()) != null) {
                throw new PreexistingEntityException("MateriaAsignada " + materiaAsignada + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(MateriaAsignada materiaAsignada) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            MateriaAsignada persistentMateriaAsignada = em.find(MateriaAsignada.class, materiaAsignada.getMateriaAsignadaPK());
            Materia materiaOld = persistentMateriaAsignada.getMateria();
            Materia materiaNew = materiaAsignada.getMateria();
            Profesor profesorOld = persistentMateriaAsignada.getProfesor();
            Profesor profesorNew = materiaAsignada.getProfesor();
            if (materiaNew != null) {
                materiaNew = em.getReference(materiaNew.getClass(), materiaNew.getId());
                materiaAsignada.setMateria(materiaNew);
            }
            if (profesorNew != null) {
                profesorNew = em.getReference(profesorNew.getClass(), profesorNew.getId());
                materiaAsignada.setProfesor(profesorNew);
            }
            materiaAsignada = em.merge(materiaAsignada);
            if (materiaOld != null && !materiaOld.equals(materiaNew)) {
                materiaOld.getMateriaAsignadaList().remove(materiaAsignada);
                materiaOld = em.merge(materiaOld);
            }
            if (materiaNew != null && !materiaNew.equals(materiaOld)) {
                materiaNew.getMateriaAsignadaList().add(materiaAsignada);
                materiaNew = em.merge(materiaNew);
            }
            if (profesorOld != null && !profesorOld.equals(profesorNew)) {
                profesorOld.getMateriaAsignadaList().remove(materiaAsignada);
                profesorOld = em.merge(profesorOld);
            }
            if (profesorNew != null && !profesorNew.equals(profesorOld)) {
                profesorNew.getMateriaAsignadaList().add(materiaAsignada);
                profesorNew = em.merge(profesorNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                MateriaAsignadaPK id = materiaAsignada.getMateriaAsignadaPK();
                if (findMateriaAsignada(id) == null) {
                    throw new NonexistentEntityException("The materiaAsignada with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(MateriaAsignadaPK id) throws NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            MateriaAsignada materiaAsignada;
            try {
                materiaAsignada = em.getReference(MateriaAsignada.class, id);
                materiaAsignada.getMateriaAsignadaPK();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The materiaAsignada with id " + id + " no longer exists.", enfe);
            }
            Materia materia = materiaAsignada.getMateria();
            if (materia != null) {
                materia.getMateriaAsignadaList().remove(materiaAsignada);
                materia = em.merge(materia);
            }
            Profesor profesor = materiaAsignada.getProfesor();
            if (profesor != null) {
                profesor.getMateriaAsignadaList().remove(materiaAsignada);
                profesor = em.merge(profesor);
            }
            em.remove(materiaAsignada);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<MateriaAsignada> findMateriaAsignadaEntities() {
        return findMateriaAsignadaEntities(true, -1, -1);
    }

    public List<MateriaAsignada> findMateriaAsignadaEntities(int maxResults, int firstResult) {
        return findMateriaAsignadaEntities(false, maxResults, firstResult);
    }

    private List<MateriaAsignada> findMateriaAsignadaEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(MateriaAsignada.class));
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

    public MateriaAsignada findMateriaAsignada(MateriaAsignadaPK id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(MateriaAsignada.class, id);
        } finally {
            em.close();
        }
    }

    public int getMateriaAsignadaCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<MateriaAsignada> rt = cq.from(MateriaAsignada.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }

}
