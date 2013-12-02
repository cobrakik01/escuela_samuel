/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.samuel.escuela.component;

import java.awt.Component;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Insets;
import javax.swing.ImageIcon;
import javax.swing.border.Border;

/**
 *
 * @author cobrakik01
 */
public class DesckTopPaneBackGround implements Border {

    private Image img = null;

    public DesckTopPaneBackGround() {
        img = new ImageIcon(this.getClass().getResource("/com/samuel/escuela/icon/background_DescktopPane.jpg")).getImage();
    }

    public DesckTopPaneBackGround(ImageIcon img) {
        this.img = img.getImage();
    }

    @Override
    public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
        g.drawImage(img, 0, 0, width, height, null);
    }

    @Override
    public Insets getBorderInsets(Component c) {
        return new Insets(0, 0, 0, 0);
    }

    @Override
    public boolean isBorderOpaque() {
        return false;
    }

}
