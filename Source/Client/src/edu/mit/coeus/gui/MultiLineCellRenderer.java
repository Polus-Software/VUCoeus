/*
 * @(#)MultiLineRenderer.java 08/26/02
 *
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 */
package edu.mit.coeus.gui;

import javax.swing.JTextArea;
import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;
import java.awt.Component;
import edu.mit.coeus.gui.CoeusFontFactory;

/**
 * <code> MultiLineCellRenderer </code> a Renderer for "Question" column, This
 * renders the data in TextArea and that is placed in cells of Question column.
 * This is required as the wrapping of data is needed according to the size of the column.
 * where column width is fixed.
 *
 * @version :1.0 August 26, 2002, 1:35 PM
 * @author Guptha K
 */

public class MultiLineCellRenderer extends JTextArea implements TableCellRenderer {

    /**
     *  The Default Constructor which enables the wrapping feature of textarea
     */
    public MultiLineCellRenderer() {

        setLineWrap(true);
        setWrapStyleWord(true);
        setOpaque(true);

    }
    
    /**
     * An overridden method to render the data in textarea that is being placed in
     * Table cell under Question column
     * Returns the component used for drawing the cell.
     * @param table  
     * @param value
     * @param isSelected
     * @param hasFocus 
     * @param row 
     * @param column
     *
     */    
    public Component getTableCellRendererComponent(JTable table, Object obj,
            boolean isSelected, boolean hasFocus, int row, int column) {

        if (isSelected) {
            setForeground(table.getSelectionForeground());
            setBackground(table.getSelectionBackground());
        } else {
            setForeground(table.getForeground());
            setBackground(table.getBackground());
        }
        setFont(CoeusFontFactory.getNormalFont());
        setText((String)obj);
        return this;

    }
    
}// end of class
