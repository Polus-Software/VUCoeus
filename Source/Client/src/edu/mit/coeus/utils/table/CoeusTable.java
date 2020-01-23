/*
 * CoeusTable.java
 *
 * Created on July 14, 2004, 12:13 PM
 */

package edu.mit.coeus.utils.table;

/**
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 * @author  sharathk
 */

import java.awt.event.MouseEvent;
import javax.swing.JTable;
import javax.swing.table.TableModel;


public class CoeusTable extends JTable{
    
    /** Creates a new instance of CoeusTable */
    public CoeusTable() {
    }
    
    public CoeusTable(int numRows, int numColumns) {
       super(numRows, numColumns);
    }
    
    public CoeusTable(TableModel dm) {
        super(dm);
    }
    
    protected void processMouseMotionEvent(MouseEvent e) {
        switch (e.getID()) {
            case MouseEvent.MOUSE_MOVED:
            case MouseEvent.MOUSE_ENTERED:     // Ignore these events
            case MouseEvent.MOUSE_EXITED:
                break;
            default:
                super.processMouseMotionEvent(e);
                break;
        }
    }
    
    public String getToolTipText(MouseEvent event) {
        // Do absolutely nothing. The default JTable invokes
        // prepareRenderer, which may cause cell renderers to do
        // work... which in a lot of cases, we don't want them to.
        return "";
    }
}
