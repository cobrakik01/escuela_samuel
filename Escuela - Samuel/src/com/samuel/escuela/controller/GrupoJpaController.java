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
import com.samuel.escuela.entity.CalificacionGrupo;
import com.samuel.escuela.entity.Grupo;
import java.util.ArrayList;
import java.util.List;
import com.samuel.escuela.entity.Materia;
import javax.persistence.EntityManager;

/**
 *
 * @author cobrakik01
 */
public class GrupoJpaController extends BaseController implements Serializable {

    public void create(Grupo grupo) {
        if (grupo.getCalificacionGrupoList() == null) {
            grupo.setCalificacionGrupoList(new ArrayList<CalificacionGrupo>());
        }
        if (grupo.getMateriaList() == null) {
            grupo.setMateriaList(new ArrayList<Materia>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            List<CalificacionGrupo> attachedCalificacionGrupoList = new ArrayList<CalificacionGrupo>();
            for (CalificacionGrupo calificacionGrupoListCalificacionGrupoToAttach : grupo.getCalificacionGrupoList()) {
                calificacionGrupoListCalificacionGrupoToAttach = em.getReference(calificacionGrupoListCalificacionGrupoToAttach.getClass(), calificacionGrupoListCalificacionGrupoToAttach.getCalificacionGrupoPK());
                attachedCalificacionGrupoList.add(calificacionGrupoListCalificacionGrupoToAttach);
            }
            grupo.setCalificacionGrupoList(attachedCalificacionGrupoList);
            List<Materia> attachedMateriaList = new ArrayList<Materia>();
            for (Materia materiaListMateriaToAttach : grupo.getMateriaList()) {
                materiaListMateriaToAttach = em.getReference(materiaListMateriaToAttach.getClass(), materiaListMateriaToAttach.getId());
                attachedMateriaList.add(materiaListMateriaToAttach);
            }
            grupo.setMateriaList(attachedMateriaList);
            em.persist(grupo);
            for (CalificacionGrupo calificacionGrupoListCalificacionGrupo : grupo.getCalificacionGrupoList()) {
                Grupo oldGrupoOfCalificacionGrupoListCalificacionGrupo = calificacionGrupoListCalificacionGrupo.getGrupo();
                calificacionGrupoListCalificacionGrupo.setGrupo(grupo);
                calificacionGrupoListCalificacionGrupo = em.merge(calificacionGrupoListCalificacionGrupo);
                if (oldGrupoOfCalificacionGrupoListCalificacionGrupo != null) {
                    oldGrupoOfCalificacionGrupoListCalificacionGrupo.getCalificacionGrupoList().remove(calificacionGrupoListCalificacionGrupo);
                    oldGrupoOfCalificacionGrupoListCalificacionGrupo = em.merge(oldGrupoOfCalificacionGrupoListCalificacionGrupo);
                }
            }
            for (Materia materiaListMateria : grupo.getMateriaList()) {
                Grupo oldGrupoIdOfMateriaListMateria = materiaListMateria.getGrupoId();
                materiaListMateria.setGrupoId(grupo);
                materiaListMateria = em.merge(materiaListMateria);
                if (oldGrupoIdOfMateriaListMateria != null) {
                    oldGrupoIdOfMateriaListMateria.getMateriaList().remove(materiaListMateria);
                    oldGrupoIdOfMateriaListMateria = em.merge(oldGrupoIdOfMateriaListMateria);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Grupo grupo) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Grupo persistentGrupo = em.find(Grupo.class, grupo.getId());
            List<CalificacionGrupo> calificacionGrupoListOld = persistentGrupo.getCalificacionGrupoList();
            List<CalificacionGrupo> calificacionGrupoListNew = grupo.getCalificacionGrupoList();
            List<Materia> materiaListOld = persistentGrupo.getMateriaList();
            List<Materia> materiaListNew = grupo.getMateriaList();
            List<String> illegalOrphanMessages = null;
            for (CalificacionGrupo calificacionGrupoListOldCalificacionGrupo : calificacionGrupoListOld) {
                if (!calificacionGrupoListNew.contains(calificacionGrupoListOldCalificacionGrupo)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain CalificacionGrupo " + calificacionGrupoListOldCalificacionGrupo + " since its grupo field is not nullable.");
                }
            }
            for (Materia materiaListOldMateria : materiaListOld) {
                if (!materiaListNew.contains(materiaListOldMateria)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Materia " + materiaListOldMateria + " since its grupoId field is not nullable.");
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
            grupo.setCalificacionGrupoList(calificacionGrupoListNew);
            List<Materia> attachedMateriaListNew = new ArrayList<Materia>();
            for (Materia materiaListNewMateriaToAttach : materiaListNew) {
                materiaListNewMateriaToAttach = em.getReference(materiaListNewMateriaToAttach.getClass(), materiaListNewMateriaToAttach.getId());
                attachedMateriaListNew.add(materiaListNewMateriaToAttach);
            }
            materiaListNew = attachedMateriaListNew;
            grupo.setMateriaList(materiaListNew);
            grupo = em.merge(grupo);
            for (CalificacionGrupo calificacionGrupoListNewCalificacionGrupo : calificacionGrupoListNew) {
                if (!calificacionGrupoListOld.contains(calificacionGrupoListNewCalificacionGrupo)) {
                    Grupo oldGrupoOfCalificacionGrupoListNewCalificacionGrupo = calificacionGrupoListNewCalificacionGrupo.getGrupo();
                    calificacionGrupoListNewCalificacionGrupo.setGrupo(grupo);
                    calificacionGrupoListNewCalificacionGrupo = em.merge(calificacionGrupoListNewCalificacionGrupo);
                    if (oldGrupoOfCalificacionGrupoListNewCalificacionGrupo != null && !oldGrupoOfCalificacionGrupoListNewCalificacionGrupo.equals(grupo)) {
                        oldGrupoOfCalificacionGrupoListNewCalificacionGrupo.getCalificacionGrupoList().remove(calificacionGrupoListNewCalificacionGrupo);
                        oldGrupoOfCalificacionGrupoListNewCalificacionGrupo = em.merge(oldGrupoOfCalificacionGrupoListNewCalificacionGrupo);
                    }
                }
            }
            for (Materia materiaListNewMateria : materiaListNew) {
                if (!materiaListOld.contains(materiaListNewMateria)) {
                    Grupo oldGrupoIdOfMateriaListNewMateria = materiaListNewMateria.getGrupoId();
                    materiaListNewMateria.setGrupoId(grupo);
                    materiaListNewMateria = em.merge(materiaListNewMateria);
                    if (oldGrupoIdOfMateriaListNewMateria != null && !oldGrupoIdOfMateriaListNewMateria.equals(grupo)) {
                        oldGrupoIdOfMateriaListNewMateria.getMateriaList().remove(materiaListNewMateria);
                        oldGrupoIdOfMateriaListNewMateria = em.merge(oldGrupoIdOfMateriaListNewMateria);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = grupo.getId();
                if (findGrupo(id) == null) {
                    throw new NonexistentEntityException("The grupo with id " + id + " no longer exists.");
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
            Grupo grupo;
            try {
                grupo = em.getReference(Grupo.class, id);
                grupo.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The grupo with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<CalificacionGrupo> calificacionGrupoListOrphanCheck = grupo.getCalificacionGrupoList();
            for (CalificacionGrupo calificacionGrupoListOrphanCheckCalificacionGrupo : calificacionGrupoListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Grupo (" + grupo + ") cannot be destroyed since the CalificacionGrupo " + calificacionGrupoListOrphanCheckCalificacionGrupo + " in its calificacionGrupoList field has a non-nullable grupo field.");
            }
            List<Materia> materiaListOrphanCheck = grupo.getMateriaList();
            for (Materia materiaListOrphanCheckMateria : materiaListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Grupo (" + grupo + ") cannot be destroyed since the Materia " + materiaListOrphanCheckMateria + " in its materiaList field has a non-nullable grupoId field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            em.remove(grupo);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Grupo> findGrupoEntities() {
        return findGrupoEntities(true, -1, -1);
    }

    public List<Grupo> findGrupoEntities(int maxResults, int firstResult) {
        return findGrupoEntities(false, maxResults, firstResult);
    }

    private List<Grupo> findGrupoEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Grupo.class));
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

    public Grupo findGrupo(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Grupo.class, id);
        } finally {
            em.close();
        }
    }

    public int getGrupoCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Grupo> rt = cq.from(Grupo.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }

}
