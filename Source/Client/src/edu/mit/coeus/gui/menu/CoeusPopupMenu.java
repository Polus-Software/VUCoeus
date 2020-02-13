/*
 * @(#)CoeusPopupMenu.java 1.0 07/31/02
 *
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 */
package edu.mit.coeus.gui.menu;

import javax.swing.JPopupMenu;
import java.awt.event.MouseEvent;

/** <CODE>CoeusPopupMenu</CODE> is a popup menu to be displayed on right click of mouse button.
 *
 * @version :1.0 July 31, 2002, 11:00 AM
 * @author Phani
 */
public class CoeusPopupMenu extends JPopupMenu{
    
    /**
     * This will construct new CoeusPopupMenu.
     */
    public CoeusPopupMenu() {
        super();
        initialize();
    }

    /**
     * Creates the popup menu
     */
    public void initialize(){

        CoeusMenuItem FrameBar = new CoeusMenuItem("FrameBar",null,true,true);
        CoeusMenuItem Left = new CoeusMenuItem("Left",null,true,true);
        CoeusMenuItem Right = new CoeusMenuItem("Right",null,true,true);
        CoeusMenuItem Top = new CoeusMenuItem("Top",null,true,true);
        CoeusMenuItem Bottom  = new CoeusMenuItem("Bottom",null,true,true);
        CoeusMenuItem Floating = new CoeusMenuItem("Floating",null,true,true);
        CoeusMenuItem ShowText = new CoeusMenuItem("Show Text",null,true,true);
        CoeusMenuItem ShowPowerTips = new CoeusMenuItem("Show Power Tips",null,true,true);

        this.add(FrameBar);
        this.addSeparator();
        this.add(Left);
        this.add(Right);
        this.add(Top);
        this.add(Bottom);
        this.add(Floating);
        this.addSeparator();
        this.add(ShowText);
        this.add(ShowPowerTips);
    }
 }
