/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.samuel.escuela.controller;

import java.io.Serializable;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

/**
 *
 * @author cobrakik01
 */
public class BaseController implements Serializable {

    public BaseController(EntityManagerFactory emf) {
        this.emf = emf;
    }

    public BaseController() {
        this(Persistence.createEntityManagerFactory("EscuelalPU"));
    }

    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }
}
