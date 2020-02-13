/*
 * EmptyHeaderRenderer.java
 *
 * Created on November 13, 2002, 11:21 AM
 */

package edu.mit.coeus.utils;

import java.awt.Component;
import javax.swing.JList;
import javax.swing.JTable;
import javax.swing.JLabel;
import javax.swing.ListCellRenderer;
import javax.swing.UIManager;
import javax.swing.table.TableCellRenderer;

/**
 * This class is used to remove the header for a Column in JTable. This will be
 * useful when any icon is used to render the column.
 *
 * @author  ravikanth
 */
public class EmptyHeaderRenderer extends JList implements TableCellRenderer {
    
    /** 
     * Creates a new instance of EmptyHeaderRenderer which will set the default
     * foreground/background and border properties of this renderer for a cell.
     * 
     */
    public EmptyHeaderRenderer() {
        setOpaque(true);
        setForeground(UIManager.getColor("TableHeader.foreground"));
        setBackground(UIManager.getColor("TableHeader.background"));
        setBorder(new javax.swing.border.EmptyBorder(0, 0, 0, 0));
        ListCellRenderer renderer = getCellRenderer();
        ((JLabel) renderer).setHorizontalAlignment(JLabel.CENTER);
        setCellRenderer(renderer);
        
    }
    
    /**
     * The Overridden method of TableCellRenderer which is called for every 
     * cell when a component
     * is going to be rendered in its cell.
     * Returns the component used for drawing the cell.
     * This method is used to configure the renderer appropriately before 
     * drawing.
     *
     * @param table  the JTable that is asking the renderer to draw; 
     * can be null
     * @param value  the value of the cell to be rendered. 
     * It is up to the specific renderer to interpret and draw the value. 
     * For example, if value is the string "true", it could be rendered as 
     * a string or it could be rendered as a check box that is checked. 
     * null is a valid value
     * @param isSelected  true if the cell is to be rendered with the 
     * selection highlighted; otherwise false
     * @param hasFocus if true, render cell appropriately. For example, 
     * put a special border on the cell, if the cell can be edited, render 
     * in the color used to indicate editing
     * @param row the row index of the cell being drawn. 
     * When drawing the header, the value of row is -1
     * @param column  the column index of the cell being drawn
     * @return Component table editing cell component
     */
    public Component getTableCellRendererComponent(JTable table,
                                                    Object value,
    boolean isSelected, boolean hasFocus, int row, int column) {
        return this;            
    }

}
