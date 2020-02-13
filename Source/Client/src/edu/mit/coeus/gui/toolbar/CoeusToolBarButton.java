/*
 * @(#)CoeusToolBarButton.java 1.0 7/27/02
 *
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 */

package edu.mit.coeus.gui.toolbar;

import java.awt.*;
import javax.swing.*;
import javax.swing.border.BevelBorder;
import javax.swing.border.EtchedBorder;

/*** begin: modified by ravi on 17-02-2003 for displaying the tooltip message on status
 * bar. bug id: #147  */
/*import java.awt.event.*;
import edu.mit.coeus.utils.CoeusGuiConstants;
import edu.mit.coeus.gui.CoeusMessageResources;*/
/** end: fixed bug with id #147  */

/**
 * This class creates the CoeusToolBarButton which extends Jbutton with custom properties.
 *
 * @version :1.0 July 27,2002
 * @author Mukund C
 */

public class CoeusToolBarButton extends JButton {
  private static final Insets margins =
    new Insets(0, 0, 0, 0);
  
/** begin: fixed bug with id #147  */  
/*  private String toolTip;
  //holds CoeusMessageResources instance used for reading message Properties.
  private CoeusMessageResources coeusMessageResources;*/
/** end: fixed bug with id #147  */

  /** This constructor creates the CoeusToolBarButton by placing the text,tooltip and icon
   * on the toolbar button. This toolbar button is added to JToolBar.
   *
   * @param icon Icon
   * @param text String
   * @param tooltip String */
  public CoeusToolBarButton(Icon icon,String text,String tooltip) {
      super(icon);
      /** begin: fixed bug with id #147  */  
      /*toolTip = tooltip;*/
      /** end: fixed bug with id #147  */
      setMargin(margins);
      setVerticalTextPosition(BOTTOM);
      setHorizontalTextPosition(CENTER);
      setText(text);
      setToolTipText(tooltip);
      Dimension iconSize = new Dimension(25,25);
      setSize(iconSize);
      setMinimumSize(iconSize);
      setMaximumSize(iconSize);
      setPreferredSize(iconSize);
//      setBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED));
      setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED));
//      System.out.println(text+" icon size=>"+this.getSize().toString());
      /** begin: fixed bug with id #147  */
      /*coeusMessageResources = CoeusMessageResources.getInstance();
      addMouseListener(new CoeusMouseAdapter());*/
      /** end: fixed bug with id #147  */
  }

  /**
   * This constructor creates the CoeusToolBarButton with icon 
   *
   * @param icon Icon
   */
  public CoeusToolBarButton(Icon icon) {
    this(icon,null,null);
  }

  /**
   * This constructor creates the toolbar with placing the image on the toolbarbutton. 
   *
   * @param imageFile String
   */
  public CoeusToolBarButton(String imageFile) {
    this(new ImageIcon(imageFile));
  }

  /**
   * This constructor creates the toolbar with placing the text for the image on the toolbarbutton. 
   *
   * @param imageFile String
   * @param text String
   */
  public CoeusToolBarButton(String imageFile, String text) {
    this(new ImageIcon(imageFile));
    setText(text);
  }
  
  /** begin: fixed bug with id #147  */
  
  /**
   * This class is used to set the tooltip message in the status bar when mouse
   * is placed on a toolbar button. On mouseOut "Ready" will be set to the status
   * bar.
   */
  /*
  class CoeusMouseAdapter extends MouseAdapter {
      boolean clicked;
      public void mouseEntered(MouseEvent me) {
        CoeusGuiConstants.getMDIForm().setStatusMessage(toolTip);
      }
      public void mouseClicked(MouseEvent me) {
        clicked = true;
      }
      
      public void mouseExited(MouseEvent me) {
        if(!clicked){  
            CoeusGuiConstants.getMDIForm().setStatusMessage(
            coeusMessageResources.parseMessageKey("general_readyCode.2276"));
        }
        clicked = false;

      }
  }*/
  /** end: fixed bug with id #147  */
}

