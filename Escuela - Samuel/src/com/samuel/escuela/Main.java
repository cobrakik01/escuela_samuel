/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.samuel.escuela;

import com.samuel.escuela.controller.UsuarioJpaController;
import com.samuel.escuela.form.AlumnoMasterDetailForm;
import com.samuel.escuela.form.LoaderForm;
import com.samuel.escuela.form.PrincipalForm;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

/**
 *
 * @author cobrakik01
 */
public class Main {

    private static LoaderForm loader;

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {

        try {
            /*
             for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
             if ("Metal".equals(info.getName())) {//if ("Nimbus".equals(info.getName())) {
             javax.swing.UIManager.setLookAndFeel(info.getClassName());
             break;
             }
             }
             */
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (ClassNotFoundException | IllegalAccessException | InstantiationException | UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(AlumnoMasterDetailForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }

        loader = new LoaderForm();
        loader.setVisible(true);
        new Thread(new Runnable() {

            @Override
            public void run() {
                new UsuarioJpaController().getEntityManager().getTransaction();
                new PrincipalForm().setVisible(true);
                loader.setVisible(false);
                loader.dispose();
            }
        }).start();
    }

}
