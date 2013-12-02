/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.samuel.escuela.controller;

import com.samuel.escuela.controller.exceptions.IllegalOrphanException;
import com.samuel.escuela.controller.exceptions.NonexistentEntityException;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import com.samuel.escuela.entity.MateriaAsignada;
import com.samuel.escuela.entity.Profesor;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;

/**
 *
 * @author cobrakik01
 */
public class ProfesorJpaController extends BaseController implements Serializable {

    public void create(Profesor profesor) {
        if (profesor.getMateriaAsignadaList() == null) {
            profesor.setMateriaAsignadaList(new ArrayList<MateriaAsignada>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            List<MateriaAsignada> attachedMateriaAsignadaList = new ArrayList<MateriaAsignada>();
            for (MateriaAsignada materiaAsignadaListMateriaAsignadaToAttach : profesor.getMateriaAsignadaList()) {
                materiaAsignadaListMateriaAsignadaToAttach = em.getReference(materiaAsignadaListMateriaAsignadaToAttach.getClass(), materiaAsignadaListMateriaAsignadaToAttach.getMateriaAsignadaPK());
                attachedMateriaAsignadaList.add(materiaAsignadaListMateriaAsignadaToAttach);
            }
            profesor.setMateriaAsignadaList(attachedMateriaAsignadaList);
            em.persist(profesor);
            for (MateriaAsignada materiaAsignadaListMateriaAsignada : profesor.getMateriaAsignadaList()) {
                Profesor oldProfesorOfMateriaAsignadaListMateriaAsignada = materiaAsignadaListMateriaAsignada.getProfesor();
                materiaAsignadaListMateriaAsignada.setProfesor(profesor);
                materiaAsignadaListMateriaAsignada = em.merge(materiaAsignadaListMateriaAsignada);
                if (oldProfesorOfMateriaAsignadaListMateriaAsignada != null) {
                    oldProfesorOfMateriaAsignadaListMateriaAsignada.getMateriaAsignadaList().remove(materiaAsignadaListMateriaAsignada);
                    oldProfesorOfMateriaAsignadaListMateriaAsignada = em.merge(oldProfesorOfMateriaAsignadaListMateriaAsignada);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Profesor profesor) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Profesor persistentProfesor = em.find(Profesor.class, profesor.getId());
            List<MateriaAsignada> materiaAsignadaListOld = persistentProfesor.getMateriaAsignadaList();
            List<MateriaAsignada> materiaAsignadaListNew = profesor.getMateriaAsignadaList();
            List<String> illegalOrphanMessages = null;
            for (MateriaAsignada materiaAsignadaListOldMateriaAsignada : materiaAsignadaListOld) {
                if (!materiaAsignadaListNew.contains(materiaAsignadaListOldMateriaAsignada)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain MateriaAsignada " + materiaAsignadaListOldMateriaAsignada + " since its profesor field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            List<MateriaAsignada> attachedMateriaAsignadaListNew = new ArrayList<MateriaAsignada>();
            for (MateriaAsignada materiaAsignadaListNewMateriaAsignadaToAttach : materiaAsignadaListNew) {
                materiaAsignadaListNewMateriaAsignadaToAttach = em.getReference(materiaAsignadaListNewMateriaAsignadaToAttach.getClass(), materiaAsignadaListNewMateriaAsignadaToAttach.getMateriaAsignadaPK());
                attachedMateriaAsignadaListNew.add(materiaAsignadaListNewMateriaAsignadaToAttach);
            }
            materiaAsignadaListNew = attachedMateriaAsignadaListNew;
            profesor.setMateriaAsignadaList(materiaAsignadaListNew);
            profesor = em.merge(profesor);
            for (MateriaAsignada materiaAsignadaListNewMateriaAsignada : materiaAsignadaListNew) {
                if (!materiaAsignadaListOld.contains(materiaAsignadaListNewMateriaAsignada)) {
                    Profesor oldProfesorOfMateriaAsignadaListNewMateriaAsignada = materiaAsignadaListNewMateriaAsignada.getProfesor();
                    materiaAsignadaListNewMateriaAsignada.setProfesor(profesor);
                    materiaAsignadaListNewMateriaAsignada = em.merge(materiaAsignadaListNewMateriaAsignada);
                    if (oldProfesorOfMateriaAsignadaListNewMateriaAsignada != null && !oldProfesorOfMateriaAsignadaListNewMateriaAsignada.equals(profesor)) {
                        oldProfesorOfMateriaAsignadaListNewMateriaAsignada.getMateriaAsignadaList().remove(materiaAsignadaListNewMateriaAsignada);
                        oldProfesorOfMateriaAsignadaListNewMateriaAsignada = em.merge(oldProfesorOfMateriaAsignadaListNewMateriaAsignada);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = profesor.getId();
                if (findProfesor(id) == null) {
                    throw new NonexistentEntityException("The profesor with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(Integer id) throws IllegalOrphanException, NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Profesor profesor;
            try {
                profesor = em.getReference(Profesor.class, id);
                profesor.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The profesor with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<MateriaAsignada> materiaAsignadaListOrphanCheck = profesor.getMateriaAsignadaList();
            for (MateriaAsignada materiaAsignadaListOrphanCheckMateriaAsignada : materiaAsignadaListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Profesor (" + profesor + ") cannot be destroyed since the MateriaAsignada " + materiaAsignadaListOrphanCheckMateriaAsignada + " in its materiaAsignadaList field has a non-nullable profesor field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            em.remove(profesor);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Profesor> findProfesorEntities() {
        return findProfesorEntities(true, -1, -1);
    }

    public List<Profesor> findProfesorEntities(int maxResults, int firstResult) {
        return findProfesorEntities(false, maxResults, firstResult);
    }

    private List<Profesor> findProfesorEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Profesor.class));
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

    public Profesor findProfesor(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Profesor.class, id);
        } finally {
            em.close();
        }
    }

    public int getProfesorCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Profesor> rt = cq.from(Profesor.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }

}
