/*
 * as.java
 *
 * Created on November 30, 2004, 3:39 PM
 */

package edu.mit.coeus.sponsorhierarchy.controller;


/**Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 *
 * @author  nadhgj
 */
import edu.mit.coeus.utils.CoeusTextField;
import edu.mit.coeus.utils.LimitedPlainDocument;

import javax.swing.*;
import javax.swing.event.*;
import java.awt.event.*;
import java.awt.*;
import java.util.*;

public class HierarchyTreeEditor extends CoeusTextField implements CellEditor {
  String value = "";
  Vector listeners = new Vector();

  // Mimic all the constructors people expect with text fields.
  public HierarchyTreeEditor() { 
   // Listen to our own action events so that we know when to stop editing.
      super(5);
      setDocument(new LimitedPlainDocument(50));
     addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent ae) {
        if (stopCellEditing()) { fireEditingStopped(); }
      }
    });
  }

  // Implement the CellEditor methods.
  public void cancelCellEditing() { setText(""); }

  // Stop editing only if the user entered a valid value.
  public boolean stopCellEditing() {
    return true;
  }

  public Object getCellEditorValue() { return value; }

  
  public boolean isCellEditable(EventObject eo) {
    if ((eo == null) || 
        ((eo instanceof MouseEvent) && 
         (((MouseEvent)eo).isMetaDown()))) {
      return true;
    }
    return false;
  }

  public boolean shouldSelectCell(EventObject eo) { return true; }

  // Add support for listeners.
  public void addCellEditorListener(CellEditorListener cel) {
    listeners.addElement(cel);
  }

  public void removeCellEditorListener(CellEditorListener cel) {
    listeners.removeElement(cel);
  }

  protected void fireEditingStopped() {
    if (listeners.size() > 0) {
      ChangeEvent ce = new ChangeEvent(this);
      for (int i = listeners.size() - 1; i >= 0; i--) {
        ((CellEditorListener)listeners.elementAt(i)).editingStopped(ce);
      }
    }
  }
}
