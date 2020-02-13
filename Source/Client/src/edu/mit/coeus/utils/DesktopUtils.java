/*
 * @(#)DesktopUtils.java 1.0 11/11/02
 *
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 */
package edu.mit.coeus.utils;

import javax.swing.*;
import javax.swing.event.*;
import java.awt.*;
import java.awt.event.*;

/**
 * This will construct the Desk top Utils used for providing the 
 * Window desktop utilities features like cascade, horizontal/vertical 
 * appearence etc.
 * @name <code>DesktopUtils</code>
 * @version 1.0
 * @description <code>DesktopUtils</code> is used for Cascading, 
 * Horizontal Tiling, Vertical Tiling, Minimizing & Maximizing of 
 * Internal frames
 */
public class DesktopUtils{
    
    private static final int UNUSED_HEIGHT = 48;
    private static int nextX; // Next X position
    private static int nextY; // Next Y position
    private static final int DEFAULT_OFFSETX = 24;
    private static final int DEFAULT_OFFSETY = 24;
    private static int offsetX = DEFAULT_OFFSETX;
    private static int offsetY = DEFAULT_OFFSETY;

    /**
     * This will construct the Desk top Utils used for providing the 
     * Window desktop utilities features like cascade, horizontal/vertical 
     * appearence etc.
     */
    public DesktopUtils(){        
    }

    /**
     * Layout all the children of this container so that they are tiled vertically
     * @param desktop <code>JDesktopPane</code>
     */

    public static void tileVertical(JDesktopPane desktop) {
        DesktopManager manager = desktop.getDesktopManager();
        if (manager == null) {
            // No desktop manager - do nothing
            return;
        }

        Component[] comps = desktop.getComponents();
        Component comp;
        int count = 0;

        // Count and handle only the internal frames
        for (int i = 0; i < comps.length; i++){
            comp = comps[i];
            if (comp instanceof JInternalFrame && comp.isVisible()) {
                count++;
            }
        }

        if (count != 0){
            double root = Math.sqrt((double) count);
            int rows = (int) root;
            int columns = count / rows;
            int spares = count - (columns * rows);

            Dimension paneSize = desktop.getSize();
            int columnWidth = paneSize.width / columns;

            // We leave some space at the bottom that doesn't get covered
            int availableHeight = paneSize.height - UNUSED_HEIGHT;
            int mainHeight = availableHeight / rows;
            int smallerHeight = availableHeight / (rows + 1);
            int rowHeight = mainHeight;
            int x = 0;
            int y = 0;
            int thisRow = rows;
            int normalColumns = columns - spares;

            for (int i = comps.length - 1; i >= 0; i--) {
                comp = comps[i];
                if (comp instanceof JInternalFrame && comp.isVisible()){
                    manager.setBoundsForFrame((JComponent) comp, x, y,
                            columnWidth, rowHeight);
                    y += rowHeight;
                    if (--thisRow == 0) {
                        // Filled the row
                        y = 0;
                        x += columnWidth;

                        // Switch to smaller rows if necessary
                        if (--normalColumns <= 0) {
                            thisRow = rows + 1;
                            rowHeight = smallerHeight;
                        }
                        else{
                            thisRow = rows;
                        }
                    }
                }
            }
        }
    } // end of TileAll

    /**
     * Layout all the children so that they are horizonatlly tiled
     * @param desktop <code>JDesktopPane</code>
     */

    public static final void tileHorizontal(JDesktopPane desktop){
      int _resizableCnt = 0;
      JInternalFrame  _allFrames[] = desktop.getAllFrames();
      for (int _x = 0; _x < _allFrames.length; _x++){
          JInternalFrame _frame = _allFrames[_x];
          if ((_frame.isVisible()) && (!_frame.isIcon())) {
              if (!_frame.isResizable())
               try{
                   _frame.setMaximum(false);
               } catch (Exception _e){
                  // OK, to take no action here
               }
               if (_frame.isResizable())
                  _resizableCnt++;
          }
      } // End for
      int _width = desktop.getBounds().width;
      int _height = arrangeIcons(desktop);
      if (_resizableCnt != 0) {
          int   _fHeight = _height / _resizableCnt;
          int   _yPos = 0;
          for (int _x = 0; _x < _allFrames.length; _x++){
               JInternalFrame _frame = _allFrames[_x];
               if ((_frame.isVisible())   &&
                    (_frame.isResizable()) &&
                         (!_frame.isIcon())){
                    _frame.setSize(_width,_fHeight);
                    _frame.setLocation(0,_yPos);
                    _yPos += _fHeight;
               }
           } // End for
      }
    }

    /**
     * This method is used to arrage the icons on the desk top
     * @param desktop <code>JDesktopPane</code>
     * @return int y - axis position
     */
    public static final int arrangeIcons(JDesktopPane desktop) {
      int _iconCnt = 0;
      JInternalFrame  _allFrames[] = desktop.getAllFrames();
      for (int _x = 0; _x < _allFrames.length; _x++)
        if ((_allFrames[_x].isVisible()) && (_allFrames[_x].isIcon()))
            _iconCnt++;
      int _height = desktop.getBounds().height;
      int _yPos = _height;
      if (_iconCnt != 0) {
        int _width = desktop.getBounds().width;
        int        _xPos = 0;
        for (int _x = 0; _x < _allFrames.length; _x++) {
           JInternalFrame _frame = _allFrames[_x];
           if ((_frame.isVisible()) && (_frame.isIcon())){
               Dimension _dim = _frame.getDesktopIcon().getSize();
               int   _iWidth = _dim.width;
               int   _iHeight = _dim.height;
               if (_yPos == _height)
                  _yPos = _height - _iHeight;
               if ((_xPos + _iWidth > _width) && (_xPos != 0)) {
                   _xPos = 0;
                   _yPos -= _iHeight;
               }
               _frame.getDesktopIcon().setLocation(_xPos,_yPos);
               _xPos += _iWidth;
           } // End if
        } // End for
      } // End if
      return(_yPos);
   } // End method


    /**
    * Layer/Maximize the Current Active Window
    * @param desktop <code>JDesktopPane</code>
    */

    public static void layer(JDesktopPane desktop){
        
         JInternalFrame activeWindow = desktop.getSelectedFrame();  
         try {
             if(activeWindow != null) {
                activeWindow.setMaximum( true ); 
             }
         }catch ( java.beans.PropertyVetoException pve ) { 
              pve.printStackTrace(); 
         }         
    }
    
    
   /**
    * Cascade all the children
    * @param desktop <code>JDesktopPane</code>
    */

    public static void cascadeAll(JDesktopPane desktop){
        Component[] comps = desktop.getComponents();
        int count = comps.length;
        nextX = 0;
        nextY = 0;

        for (int i = count - 1; i >= 0; i--){
            Component comp = comps[i];
            if (comp instanceof JInternalFrame && comp.isVisible()) {
                cascade(comp, desktop);
            }
        }
    }

    /**
     * Minimize all the children
     * @param desktop <code>JDesktopPane</code>
     */

    public static void minimizeAll(JDesktopPane desktop){
        Component[] comps = desktop.getComponents();
        int count = comps.length;

        for (int i = count - 1; i >= 0; i--){
            Component comp = comps[i];
            if (comp instanceof JInternalFrame && comp.isVisible()){
            	JInternalFrame jif = (JInternalFrame)comp;
                if (jif.isIconifiable()){
                  try{
                        jif.setIcon(true);
                  }catch (java.beans.PropertyVetoException e) {
                    e.printStackTrace();
                  }
               }
            }
        }
    }

    /**
     * Maximize all the children
     * @param desktopPane <code>JDesktopPane</code>
     */

    public static void maximizeAll(JDesktopPane desktopPane){
      JInternalFrame[] openWindows = desktopPane.getAllFrames();
       for(int i=0;i<openWindows.length;i++) {
        if(openWindows[i].isIcon())
          try {openWindows[i].setIcon(false);}
          catch (java.beans.PropertyVetoException pve) { pve.printStackTrace(); }
        }
    }

    /**
     * Place a component so that it is relative to the previous one
     * @param comp <code>Component</code>
     * @param desktop <code>JDesktopPane</code>
     *
     */

    protected static void cascade(Component comp, JDesktopPane desktop){
        Dimension paneSize = desktop.getSize();

        int targetWidth = 3 * paneSize.width / 4;
        int targetHeight = 3 * paneSize.height / 4;

        DesktopManager manager = desktop.getDesktopManager();
        if (manager == null){
            comp.setBounds(0, 0, targetWidth, targetHeight);
            return;
        }

        if (nextX + targetWidth > paneSize.width || nextY + targetHeight > paneSize.height){
            nextX = 0;
            nextY = 0;
        }

        manager.setBoundsForFrame((JComponent) comp, nextX, nextY,
                targetWidth, targetHeight);

        nextX += offsetX;
        nextY += offsetY;
    }
}
