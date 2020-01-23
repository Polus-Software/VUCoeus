/*
 * @(#)organization3Form.java 1.0 9/1/02
 *
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 */

package edu.mit.coeus.organization.gui;

import java.awt.*;

/**
 * This class constructs the Custom layout for the Organization window.
 *
 * @version :1.0 September 1, 2002, 1:35 PM
 * @author Geo
 */

class OrganizationTypeLayout implements LayoutManager {

    /**
     * Default Constructor
     */
    public OrganizationTypeLayout() {
    }
    /**
     * Adds the specified component with the specified name to the layout.
     * @param  name - the component name
     * @param  comp - the component to be added
     */
    
    public void addLayoutComponent(String name, Component comp) {
    }
    
    /**
     * Removes the specified component from the layout
     * @param  comp - the component to be added
     */
    public void removeLayoutComponent(Component comp) {
    }
    
    /**
     * Calculates the preferred size dimensions for the specified panel given 
     * the components in the specified parent container.
     * @param  parent - the component to be laid out
     * @return Dimension
     */
    public Dimension preferredLayoutSize(Container parent) {
        Dimension dim = new Dimension(0, 0);

        Insets insets = parent.getInsets();
        dim.width = 544 + insets.left + insets.right;
        dim.height = 500 + insets.top + insets.bottom;

        return dim;
    }
    
    /**
     * Calculates the minimum size dimensions for the specified panel given the 
     * components in the specified parent container.
     * @param  parent - the component to be laid out
     * @return Dimension
     */

    public Dimension minimumLayoutSize(Container parent) {
        Dimension dim = new Dimension(0, 0);
        return dim;
    }
    
    /**
     * Lays out the container in the specified panel.
     * @param  parent - the component to be laid out
     */

    public void layoutContainer(Container parent) {
        Insets insets = parent.getInsets();

        Component c;
        c = parent.getComponent(0);
        if (c.isVisible()) {c.setBounds(insets.left+16,insets.top+16,206,24);}
        c = parent.getComponent(1);
        if (c.isVisible()) {c.setBounds(insets.left+16,insets.top+48,206,230);}
        c = parent.getComponent(2);
        if (c.isVisible()) {c.setBounds(insets.left+238,insets.top+48,71,24);}
        c = parent.getComponent(3);
        if (c.isVisible()) {c.setBounds(insets.left+238,insets.top+87,71,24);}
        c = parent.getComponent(4);
        if (c.isVisible()) {c.setBounds(insets.left+325,insets.top+48,198,230);}
        c = parent.getComponent(5);
        if (c.isVisible()) {c.setBounds(insets.left+325,insets.top+16,198,24);}
    }
}