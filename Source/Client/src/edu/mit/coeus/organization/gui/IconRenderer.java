/*
 * @(#)IconRenderer.java 08/26/02 1.0
 *
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 */
package edu.mit.coeus.organization.gui;

import java.awt.Component;
import javax.swing.ImageIcon;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

/**
 * <code> IconRenderer </code> a Renderer to render an icon in the cell of selected row.
 * and in all other non-selected rows the first cell will have nothing(empty).
 *
 * @version :1.0 August 26, 2002, 1:35 PM
 * @author Guptha K
 */
public class IconRenderer  extends DefaultTableCellRenderer {
    
    private  final  ImageIcon handIcon = new ImageIcon(getClass().getClassLoader().getResource("images/handIcon.gif"));
    private  final  ImageIcon emptyIcon = null;
    
    /** Default Constructor*/
    public IconRenderer() {
    }
    
    /**
     * An overridden method to render the component(icon) in cell.
     * foreground/background for this cell and Font too.
     *
     * @param table  the JTable that is asking the renderer to draw; can be null
     * @param value  the value of the cell to be rendered. It is up to the specific renderer to interpret and draw the value. For example, if value is the string "true", it could be rendered as a string or it could be rendered as a check box that is checked. null is a valid value
     * @param isSelected  true if the cell is to be rendered with the selection highlighted; otherwise false
     * @param hasFocus if true, render cell appropriately. For example, put a special border on the cell, if the cell can be edited, render in the color used to indicate editing
     * @param row the row index of the cell being drawn. When drawing the header, the value of row is -1
     * @param column  the column index of the cell being drawn
     *
     * @see TableCellRenderer#getTableCellRendererComponent(JTable, Object, boolean, boolean, int, int)
     */
    public Component getTableCellRendererComponent(JTable table, Object value,
            boolean isSelected, boolean hasFocus, int row, int column) {

        setText((String)value);
        // if row is selected the place the icon in this cell wherever this renderer is used.
        if( isSelected ){
            setIcon(handIcon);
        }else{
            setIcon(emptyIcon);
        }
        return this;

    }

}