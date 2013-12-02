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
import com.samuel.escuela.entity.CalificacionGrupo;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;

/**
 *
 * @author cobrakik01
 */
public class AlumnoJpaController extends BaseController implements Serializable {

    public void create(Alumno alumno) {
        if (alumno.getCalificacionGrupoList() == null) {
            alumno.setCalificacionGrupoList(new ArrayList<CalificacionGrupo>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            List<CalificacionGrupo> attachedCalificacionGrupoList = new ArrayList<CalificacionGrupo>();
            for (CalificacionGrupo calificacionGrupoListCalificacionGrupoToAttach : alumno.getCalificacionGrupoList()) {
                calificacionGrupoListCalificacionGrupoToAttach = em.getReference(calificacionGrupoListCalificacionGrupoToAttach.getClass(), calificacionGrupoListCalificacionGrupoToAttach.getCalificacionGrupoPK());
                attachedCalificacionGrupoList.add(calificacionGrupoListCalificacionGrupoToAttach);
            }
            alumno.setCalificacionGrupoList(attachedCalificacionGrupoList);
            em.persist(alumno);
            for (CalificacionGrupo calificacionGrupoListCalificacionGrupo : alumno.getCalificacionGrupoList()) {
                Alumno oldAlumnoOfCalificacionGrupoListCalificacionGrupo = calificacionGrupoListCalificacionGrupo.getAlumno();
                calificacionGrupoListCalificacionGrupo.setAlumno(alumno);
                calificacionGrupoListCalificacionGrupo = em.merge(calificacionGrupoListCalificacionGrupo);
                if (oldAlumnoOfCalificacionGrupoListCalificacionGrupo != null) {
                    oldAlumnoOfCalificacionGrupoListCalificacionGrupo.getCalificacionGrupoList().remove(calificacionGrupoListCalificacionGrupo);
                    oldAlumnoOfCalificacionGrupoListCalificacionGrupo = em.merge(oldAlumnoOfCalificacionGrupoListCalificacionGrupo);
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
            List<CalificacionGrupo> calificacionGrupoListOld = persistentAlumno.getCalificacionGrupoList();
            List<CalificacionGrupo> calificacionGrupoListNew = alumno.getCalificacionGrupoList();
            List<String> illegalOrphanMessages = null;
            for (CalificacionGrupo calificacionGrupoListOldCalificacionGrupo : calificacionGrupoListOld) {
                if (!calificacionGrupoListNew.contains(calificacionGrupoListOldCalificacionGrupo)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain CalificacionGrupo " + calificacionGrupoListOldCalificacionGrupo + " since its alumno field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            List<CalificacionGrupo> attachedCalificacionGrupoListNew = new ArrayList<CalificacionGrupo>();
            for (CalificacionGrupo calificacionGrupoListNewCalificacionGrupoToAttach : calificacionGrupoListNew) {
                calificacionGrupoListNewCalificacionGrupoToAttach = em.getReference(calificacionGrupoListNewCalificacionGrupoToAttach.getClass(), calificacionGrupoListNewCalificacionGrupoToAttach.getCalificacionGrupoPK());
                attachedCalificacionGrupoListNew.add(calificacionGrupoListNewCalificacionGrupoToAttach);
            }
            calificacionGrupoListNew = attachedCalificacionGrupoListNew;
            alumno.setCalificacionGrupoList(calificacionGrupoListNew);
            alumno = em.merge(alumno);
            for (CalificacionGrupo calificacionGrupoListNewCalificacionGrupo : calificacionGrupoListNew) {
                if (!calificacionGrupoListOld.contains(calificacionGrupoListNewCalificacionGrupo)) {
                    Alumno oldAlumnoOfCalificacionGrupoListNewCalificacionGrupo = calificacionGrupoListNewCalificacionGrupo.getAlumno();
                    calificacionGrupoListNewCalificacionGrupo.setAlumno(alumno);
                    calificacionGrupoListNewCalificacionGrupo = em.merge(calificacionGrupoListNewCalificacionGrupo);
                    if (oldAlumnoOfCalificacionGrupoListNewCalificacionGrupo != null && !oldAlumnoOfCalificacionGrupoListNewCalificacionGrupo.equals(alumno)) {
                        oldAlumnoOfCalificacionGrupoListNewCalificacionGrupo.getCalificacionGrupoList().remove(calificacionGrupoListNewCalificacionGrupo);
                        oldAlumnoOfCalificacionGrupoListNewCalificacionGrupo = em.merge(oldAlumnoOfCalificacionGrupoListNewCalificacionGrupo);
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
            List<CalificacionGrupo> calificacionGrupoListOrphanCheck = alumno.getCalificacionGrupoList();
            for (CalificacionGrupo calificacionGrupoListOrphanCheckCalificacionGrupo : calificacionGrupoListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Alumno (" + alumno + ") cannot be destroyed since the CalificacionGrupo " + calificacionGrupoListOrphanCheckCalificacionGrupo + " in its calificacionGrupoList field has a non-nullable alumno field.");
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
