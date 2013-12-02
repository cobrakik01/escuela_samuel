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
import com.samuel.escuela.entity.Grupo;
import com.samuel.escuela.entity.Materia;
import com.samuel.escuela.entity.MateriaAsignada;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import javax.persistence.EntityManager;

/**
 *
 * @author cobrakik01
 */
public class MateriaJpaController extends BaseController implements Serializable {

    public void create(Materia materia) {
        if (materia.getMateriaAsignadaList() == null) {
            materia.setMateriaAsignadaList(new ArrayList<MateriaAsignada>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Grupo grupoId = materia.getGrupoId();
            if (grupoId != null) {
                grupoId = em.getReference(grupoId.getClass(), grupoId.getId());
                materia.setGrupoId(grupoId);
            }
            List<MateriaAsignada> attachedMateriaAsignadaList = new ArrayList<MateriaAsignada>();
            for (MateriaAsignada materiaAsignadaListMateriaAsignadaToAttach : materia.getMateriaAsignadaList()) {
                materiaAsignadaListMateriaAsignadaToAttach = em.getReference(materiaAsignadaListMateriaAsignadaToAttach.getClass(), materiaAsignadaListMateriaAsignadaToAttach.getMateriaAsignadaPK());
                attachedMateriaAsignadaList.add(materiaAsignadaListMateriaAsignadaToAttach);
            }
            materia.setMateriaAsignadaList(attachedMateriaAsignadaList);
            em.persist(materia);
            if (grupoId != null) {
                grupoId.getMateriaList().add(materia);
                grupoId = em.merge(grupoId);
            }
            for (MateriaAsignada materiaAsignadaListMateriaAsignada : materia.getMateriaAsignadaList()) {
                Materia oldMateriaOfMateriaAsignadaListMateriaAsignada = materiaAsignadaListMateriaAsignada.getMateria();
                materiaAsignadaListMateriaAsignada.setMateria(materia);
                materiaAsignadaListMateriaAsignada = em.merge(materiaAsignadaListMateriaAsignada);
                if (oldMateriaOfMateriaAsignadaListMateriaAsignada != null) {
                    oldMateriaOfMateriaAsignadaListMateriaAsignada.getMateriaAsignadaList().remove(materiaAsignadaListMateriaAsignada);
                    oldMateriaOfMateriaAsignadaListMateriaAsignada = em.merge(oldMateriaOfMateriaAsignadaListMateriaAsignada);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Materia materia) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Materia persistentMateria = em.find(Materia.class, materia.getId());
            Grupo grupoIdOld = persistentMateria.getGrupoId();
            Grupo grupoIdNew = materia.getGrupoId();
            List<MateriaAsignada> materiaAsignadaListOld = persistentMateria.getMateriaAsignadaList();
            List<MateriaAsignada> materiaAsignadaListNew = materia.getMateriaAsignadaList();
            List<String> illegalOrphanMessages = null;
            for (MateriaAsignada materiaAsignadaListOldMateriaAsignada : materiaAsignadaListOld) {
                if (!materiaAsignadaListNew.contains(materiaAsignadaListOldMateriaAsignada)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain MateriaAsignada " + materiaAsignadaListOldMateriaAsignada + " since its materia field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (grupoIdNew != null) {
                grupoIdNew = em.getReference(grupoIdNew.getClass(), grupoIdNew.getId());
                materia.setGrupoId(grupoIdNew);
            }
            List<MateriaAsignada> attachedMateriaAsignadaListNew = new ArrayList<MateriaAsignada>();
            for (MateriaAsignada materiaAsignadaListNewMateriaAsignadaToAttach : materiaAsignadaListNew) {
                materiaAsignadaListNewMateriaAsignadaToAttach = em.getReference(materiaAsignadaListNewMateriaAsignadaToAttach.getClass(), materiaAsignadaListNewMateriaAsignadaToAttach.getMateriaAsignadaPK());
                attachedMateriaAsignadaListNew.add(materiaAsignadaListNewMateriaAsignadaToAttach);
            }
            materiaAsignadaListNew = attachedMateriaAsignadaListNew;
            materia.setMateriaAsignadaList(materiaAsignadaListNew);
            materia = em.merge(materia);
            if (grupoIdOld != null && !grupoIdOld.equals(grupoIdNew)) {
                grupoIdOld.getMateriaList().remove(materia);
                grupoIdOld = em.merge(grupoIdOld);
            }
            if (grupoIdNew != null && !grupoIdNew.equals(grupoIdOld)) {
                grupoIdNew.getMateriaList().add(materia);
                grupoIdNew = em.merge(grupoIdNew);
            }
            for (MateriaAsignada materiaAsignadaListNewMateriaAsignada : materiaAsignadaListNew) {
                if (!materiaAsignadaListOld.contains(materiaAsignadaListNewMateriaAsignada)) {
                    Materia oldMateriaOfMateriaAsignadaListNewMateriaAsignada = materiaAsignadaListNewMateriaAsignada.getMateria();
                    materiaAsignadaListNewMateriaAsignada.setMateria(materia);
                    materiaAsignadaListNewMateriaAsignada = em.merge(materiaAsignadaListNewMateriaAsignada);
                    if (oldMateriaOfMateriaAsignadaListNewMateriaAsignada != null && !oldMateriaOfMateriaAsignadaListNewMateriaAsignada.equals(materia)) {
                        oldMateriaOfMateriaAsignadaListNewMateriaAsignada.getMateriaAsignadaList().remove(materiaAsignadaListNewMateriaAsignada);
                        oldMateriaOfMateriaAsignadaListNewMateriaAsignada = em.merge(oldMateriaOfMateriaAsignadaListNewMateriaAsignada);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = materia.getId();
                if (findMateria(id) == null) {
                    throw new NonexistentEntityException("The materia with id " + id + " no longer exists.");
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
            Materia materia;
            try {
                materia = em.getReference(Materia.class, id);
                materia.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The materia with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<MateriaAsignada> materiaAsignadaListOrphanCheck = materia.getMateriaAsignadaList();
            for (MateriaAsignada materiaAsignadaListOrphanCheckMateriaAsignada : materiaAsignadaListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Materia (" + materia + ") cannot be destroyed since the MateriaAsignada " + materiaAsignadaListOrphanCheckMateriaAsignada + " in its materiaAsignadaList field has a non-nullable materia field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Grupo grupoId = materia.getGrupoId();
            if (grupoId != null) {
                grupoId.getMateriaList().remove(materia);
                grupoId = em.merge(grupoId);
            }
            em.remove(materia);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Materia> findMateriaEntities() {
        return findMateriaEntities(true, -1, -1);
    }

    public List<Materia> findMateriaEntities(int maxResults, int firstResult) {
        return findMateriaEntities(false, maxResults, firstResult);
    }

    private List<Materia> findMateriaEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Materia.class));
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

    public Materia findMateria(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Materia.class, id);
        } finally {
            em.close();
        }
    }

    public int getMateriaCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Materia> rt = cq.from(Materia.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }

    public List<Materia> findMateriaByDisable() {
        List<Materia> list = null;
        list = this.findMateriaEntities();
        if(list == null){
            list = new LinkedList<>();
        }
        return list;
    }

}
