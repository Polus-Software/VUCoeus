/*
 * @(#)StateComboBoxUI.java 08/20/2002 9:21 AM
 *
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 */

package edu.mit.coeus.utils;

import javax.swing.plaf.basic.*;
import javax.swing.*;
import java.awt.Dimension;
import java.awt.Rectangle;
/**
 * <code> StateComboBoxUI </code> is a UI class that extends BasicComboBoxUI 
 * which sets some default properties which are required for implemented classes.
 *
 * @author  yaman
 * @date Aug,20,2002
 * @since 1.0
 */
public class StateComboBoxUI extends BasicComboBoxUI{
    
       /**
        * Creates the ComboBox popup that is displayed when the implemented combobox is selected.
        * As in Basic LAF the combobox does not provide the horizontal scrollbar for popup this 
        * method is overridden with that property.
        * @return ComboPopup pop up combo.
        */
        protected ComboPopup createPopup(){
            BasicComboPopup popup = new BasicComboPopup(comboBox){
               
                  public void show() {
                    int selectedIndex = comboBox.getSelectedIndex();
                    if ( selectedIndex == -1 ) {
                      list.clearSelection();
                    } else {
                      list.setSelectedIndex( selectedIndex );
                    }            
                    list.ensureIndexIsVisible( list.getSelectedIndex() );
                    
                    java.awt.Insets insets = getInsets();
                    Dimension listDim = list.getPreferredSize();
                    boolean hasScrollBar = scroller.getViewport().getViewSize().height != listDim.height;
//                    if(hasScrollBar){              
//                        JScrollBar scrollBar = scroller.getVerticalScrollBar();
////                        listDim.width += scrollBar.getPreferredSize().getWidth();
//                        System.out.println("scrollBar width:"+scrollBar.getPreferredSize().getWidth());
//                        System.out.println("list width:"+listDim.width);
//                        listDim.width+= Math.round((scrollBar.getPreferredSize().getWidth() - listDim.width)* 0.7);
//                    }
                    int width = Math.max(listDim.width,comboBox.getWidth() - (insets.right + insets.left));
                    int height = getPopupHeightForRowCount( comboBox.getMaximumRowCount());
                    Rectangle popupBounds = computePopupBounds(0,comboBox.getHeight(),width,height);
                    Dimension scrollSize = popupBounds.getSize();
                    scroller.setMaximumSize( scrollSize );
                    scroller.setPreferredSize( scrollSize );
                    scroller.setMinimumSize( scrollSize );
                    scroller.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
                    list.revalidate();                    
                    setLightWeightPopupEnabled( comboBox.isLightWeightPopupEnabled() );
                    super.list.invalidate();
                    super.comboBox.validate();
                    show( comboBox, popupBounds.x, popupBounds.y );
                  }                
            };
            popup.getAccessibleContext().setAccessibleParent(comboBox);
            return popup;
        }//end of method createPopup        
    
}