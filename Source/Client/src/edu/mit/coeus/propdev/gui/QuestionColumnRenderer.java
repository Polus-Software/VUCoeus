/*
 * @(#)QuestionColumnRenderer.java 08/26/02
 *
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 */

package edu.mit.coeus.propdev.gui;

import java.awt.Component;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.table.TableCellRenderer;

/**
 * <code> QuestionColumnRenderer </code> a Renderer for "Question" column, This
 * renders the data in TextArea and that is placed in cells of Question column.
 * This is required as the wrapping of data is needed according the size of the column
 * which is fixed in length.
 *
 * @version :1.0 August 26, 2002, 1:35 PM
 * @author Guptha K
 */
public class QuestionColumnRenderer extends JTextArea    implements TableCellRenderer {
    
    /**
     * The Default Constructor which enables the wrapping feature of textarea
     */
    public QuestionColumnRenderer() {
        setLineWrap(true);
        setWrapStyleWord(true);
    }
    
    /**
     * An overridden method to render the data in textarea that is being placed in
     * Table cell under Question column
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
    public Component getTableCellRendererComponent(JTable jTable,Object obj,
            boolean isSelected, boolean hasFocus, int row, int column) {

        setText((String)obj);
        return this;

    }
    
}
