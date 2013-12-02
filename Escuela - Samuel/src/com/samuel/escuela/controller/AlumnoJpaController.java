/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.samuel.escuela.controller;

import com.samuel.escuela.controller.exceptions.IllegalOrphanException;
import com.samuel.escuela.controller.exceptions.NonexistentEntityException;
import com.samuel.escuela.entity.Alumno;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import com.samuel.escuela.entity.GrupoAlumno;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;

/**
 *
 * @author cobrakik01
 */
public class AlumnoJpaController extends BaseController implements Serializable {

    public void create(Alumno alumno) {
        if (alumno.getGrupoAlumnoList() == null) {
            alumno.setGrupoAlumnoList(new ArrayList<GrupoAlumno>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            List<GrupoAlumno> attachedGrupoAlumnoList = new ArrayList<GrupoAlumno>();
            for (GrupoAlumno grupoAlumnoListGrupoAlumnoToAttach : alumno.getGrupoAlumnoList()) {
                grupoAlumnoListGrupoAlumnoToAttach = em.getReference(grupoAlumnoListGrupoAlumnoToAttach.getClass(), grupoAlumnoListGrupoAlumnoToAttach.getGrupoAlumnoPK());
                attachedGrupoAlumnoList.add(grupoAlumnoListGrupoAlumnoToAttach);
            }
            alumno.setGrupoAlumnoList(attachedGrupoAlumnoList);
            em.persist(alumno);
            for (GrupoAlumno grupoAlumnoListGrupoAlumno : alumno.getGrupoAlumnoList()) {
                Alumno oldAlumnoOfGrupoAlumnoListGrupoAlumno = grupoAlumnoListGrupoAlumno.getAlumno();
                grupoAlumnoListGrupoAlumno.setAlumno(alumno);
                grupoAlumnoListGrupoAlumno = em.merge(grupoAlumnoListGrupoAlumno);
                if (oldAlumnoOfGrupoAlumnoListGrupoAlumno != null) {
                    oldAlumnoOfGrupoAlumnoListGrupoAlumno.getGrupoAlumnoList().remove(grupoAlumnoListGrupoAlumno);
                    oldAlumnoOfGrupoAlumnoListGrupoAlumno = em.merge(oldAlumnoOfGrupoAlumnoListGrupoAlumno);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Alumno alumno) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Alumno persistentAlumno = em.find(Alumno.class, alumno.getId());
            List<GrupoAlumno> grupoAlumnoListOld = persistentAlumno.getGrupoAlumnoList();
            List<GrupoAlumno> grupoAlumnoListNew = alumno.getGrupoAlumnoList();
            List<String> illegalOrphanMessages = null;
            for (GrupoAlumno grupoAlumnoListOldGrupoAlumno : grupoAlumnoListOld) {
                if (!grupoAlumnoListNew.contains(grupoAlumnoListOldGrupoAlumno)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain GrupoAlumno " + grupoAlumnoListOldGrupoAlumno + " since its alumno field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            List<GrupoAlumno> attachedGrupoAlumnoListNew = new ArrayList<GrupoAlumno>();
            for (GrupoAlumno grupoAlumnoListNewGrupoAlumnoToAttach : grupoAlumnoListNew) {
                grupoAlumnoListNewGrupoAlumnoToAttach = em.getReference(grupoAlumnoListNewGrupoAlumnoToAttach.getClass(), grupoAlumnoListNewGrupoAlumnoToAttach.getGrupoAlumnoPK());
                attachedGrupoAlumnoListNew.add(grupoAlumnoListNewGrupoAlumnoToAttach);
            }
            grupoAlumnoListNew = attachedGrupoAlumnoListNew;
            alumno.setGrupoAlumnoList(grupoAlumnoListNew);
            alumno = em.merge(alumno);
            for (GrupoAlumno grupoAlumnoListNewGrupoAlumno : grupoAlumnoListNew) {
                if (!grupoAlumnoListOld.contains(grupoAlumnoListNewGrupoAlumno)) {
                    Alumno oldAlumnoOfGrupoAlumnoListNewGrupoAlumno = grupoAlumnoListNewGrupoAlumno.getAlumno();
                    grupoAlumnoListNewGrupoAlumno.setAlumno(alumno);
                    grupoAlumnoListNewGrupoAlumno = em.merge(grupoAlumnoListNewGrupoAlumno);
                    if (oldAlumnoOfGrupoAlumnoListNewGrupoAlumno != null && !oldAlumnoOfGrupoAlumnoListNewGrupoAlumno.equals(alumno)) {
                        oldAlumnoOfGrupoAlumnoListNewGrupoAlumno.getGrupoAlumnoList().remove(grupoAlumnoListNewGrupoAlumno);
                        oldAlumnoOfGrupoAlumnoListNewGrupoAlumno = em.merge(oldAlumnoOfGrupoAlumnoListNewGrupoAlumno);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = alumno.getId();
                if (findAlumno(id) == null) {
                    throw new NonexistentEntityException("The alumno with id " + id + " no longer exists.");
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
            Alumno alumno;
            try {
                alumno = em.getReference(Alumno.class, id);
                alumno.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The alumno with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<GrupoAlumno> grupoAlumnoListOrphanCheck = alumno.getGrupoAlumnoList();
            for (GrupoAlumno grupoAlumnoListOrphanCheckGrupoAlumno : grupoAlumnoListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Alumno (" + alumno + ") cannot be destroyed since the GrupoAlumno " + grupoAlumnoListOrphanCheckGrupoAlumno + " in its grupoAlumnoList field has a non-nullable alumno field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            em.remove(alumno);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Alumno> findAlumnoEntities() {
        return findAlumnoEntities(true, -1, -1);
    }

    public List<Alumno> findAlumnoEntities(int maxResults, int firstResult) {
        return findAlumnoEntities(false, maxResults, firstResult);
    }

    private List<Alumno> findAlumnoEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Alumno.class));
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

    public Alumno findAlumno(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Alumno.class, id);
        } finally {
            em.close();
        }
    }

    public int getAlumnoCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Alumno> rt = cq.from(Alumno.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
