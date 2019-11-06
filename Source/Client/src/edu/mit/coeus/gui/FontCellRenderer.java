/*
 * @(#)FontCellRenderer.java 1.0 8/27/02
 *
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 */
package edu.mit.coeus.gui;

import edu.mit.coeus.gui.CoeusFontFactory;
import javax.swing.JTextArea;
import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;
import java.awt.*;

/**
 * <code> FontCellRenderer </code> is a Renderer to render the default font
 * in a column of a table and which does not allow any selection in that column
 * focus is disabled and wraps the line in the cell too.
 *
 * @version :1.0 August 27, 2002, 1:35 PM
 * @author Guptha K
 */
public class FontCellRenderer extends JTextArea implements TableCellRenderer {

    /**
     * Default constructor to instantiate and enables the wrapping feature in textarea.
     */
    public FontCellRenderer() {

        setLineWrap(true);
        setWrapStyleWord(true);
        setOpaque(true);

    }

    /**
     * An overridden method to render the text in multi line and sets the default
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
    public Component getTableCellRendererComponent(JTable table,
                                                   Object obj, boolean isSelected, boolean hasFocus, int row,
                                                   int column) {

        if (isSelected) {
            setForeground(table.getSelectionForeground());
            setBackground(table.getSelectionBackground());
        } else {
            setForeground(table.getForeground());
            setBackground(table.getBackground());
        }
        setFont(CoeusFontFactory.getNormalFont());
        setText((String) obj);
        return this;

    }

}
