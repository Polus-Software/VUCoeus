/*
 * IconRenderer.java
 *
 * Created on November 13, 2002, 10:52 AM
 */

package edu.mit.coeus.utils;

import java.awt.Component;
import javax.swing.ImageIcon;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

/**
 * This class is used to construct the hand icon renderer which can be used as a
 * renderer component for a column in Table. By default, it shows the hand 
 * icon in the rendered column for the selected rows and for all other rows 
 * there won't be any icon. Depending on requirements user can specify any other
 * icons for selected rows and non-selected rows.
 *
 * @author  ravikanth
 */


public class IconRenderer extends DefaultTableCellRenderer {
    
    /* Hand icon which is used to diplay in the rendered column of selected 
      row */
    private ImageIcon handIcon ;
    
    /* Empty icon i.e null for all rows which are not selected */
    private ImageIcon emptyIcon;
    
    
    /** Creates a new instance of IconRenderer */
    public IconRenderer(){
        handIcon = new ImageIcon(getClass().getClassLoader().getResource(CoeusGuiConstants.HAND_ICON));
    }
    
    /**
     * An overridden method to render the component(icon) in cell.
     * foreground/background for this cell and Font too.
     *
     * @param table  the JTable that is asking the renderer to draw;
     * can be null
     * @param value  the value of the cell to be rendered. It is up to the
     * specific renderer to interpret and draw the value. For example,
     * if value is the string "true", it could be rendered as a string or
     * it could be rendered as a check box that is checked. null is a
     * valid value
     * @param isSelected  true if the cell is to be rendered with the
     * selection highlighted; otherwise false
     * @param hasFocus if true, render cell appropriately. For example,
     * put a special border on the cell, if the cell can be edited, render
     * in the color used to indicate editing
     * @param row the row index of the cell being drawn. When drawing the
     * header, the value of row is -1
     * @param column  the column index of the cell being drawn
     *
     * @see TableCellRenderer#getTableCellRendererComponent(JTable, Object,
     * boolean, boolean, int, int)
     * @return Component table cell editing component.
     */
    public Component getTableCellRendererComponent(JTable table,
    Object value, boolean isSelected, boolean hasFocus, int row,
    int column) {

        setOpaque(false);
        /* if row is selected the place the icon in this cell wherever this
           renderer is used. */
        if( isSelected ){
            setIcon(handIcon);
        }else{
            setIcon(emptyIcon);
        }
        return this;

    }
    
    /**
     * This method is used to specify the icon to be used to display in 
     * rendered column for selected row.
     *
     * @param icon instance of ImageIcon which will be used for rendering the 
     * column.
     */
    public void setSelectedRowIcon(ImageIcon icon){
        this.handIcon = icon;
    }

    /**
     * This method is used to specify the icon to be used to display in 
     * rendered column for all non selected rows.
     *
     * @param icon instance of ImageIcon which will be used for rendering the 
     * column.
     */

    public void setNonSelectedRowIcon(ImageIcon icon){
        this.emptyIcon = icon;
    }

}
