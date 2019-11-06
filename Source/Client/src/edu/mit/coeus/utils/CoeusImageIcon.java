/*
 * @(#)CoeusImageIcon.java 1.0 07/25/02
 *
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 */
package edu.mit.coeus.utils;

import javax.swing.*;
import java.net.URL;
import java.io.File;


public class CoeusImageIcon extends ImageIcon{

    /*
     * applet code base
     */
    URL codeBase;
    /*
     * images directory
     */
    private final String IMAGE_DIRECTORY = "images";
    /*
     * file seperator. for windows it is '/'
     */
    private String separator = File.separator;

    /**
     * creates CoeusImageIcon
     *
     * @param codeBase URL applet code base
     */
    public CoeusImageIcon(URL codeBase){
         this.codeBase = codeBase;
    }

    /**
     * method to load icon from an applet codebase
     *
     * @param iconFile String file name of the icon
     * @return ImageIcon imageIcon object
     */
    public ImageIcon getCoeusImageIcon(String iconFile) {
        String icon_loc = IMAGE_DIRECTORY+separator+iconFile;
        URL url = null; //local variable
        try {
            url = new URL(codeBase, icon_loc);
        } catch (java.net.MalformedURLException e) {
            return null;
        }
        return (new ImageIcon(url));
    }
}