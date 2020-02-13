/*
 * @(#)NarrativeUserTableRenderer.java
 *
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 *
 * Created on May 31, 2003, 3:19 PM
 */

package edu.mit.coeus.propdev.gui;

import javax.swing.*;
import javax.swing.border.EtchedBorder;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;

import edu.mit.coeus.propdev.bean.*;
import edu.mit.coeus.gui.CoeusFontFactory;
/**
 * This class is used to show access rights radio buttons and to display user id
 * as well as user name in the users table of the ProposalNarrativeForm and 
 * ProposalNarrativeModuleDetails.
 *
 * @author  ravikanth
 */
public class NarrativeUserTableRenderer extends DefaultTableCellRenderer {
    private JPanel pnlRadioButtons;
    private JRadioButton rbtnRead, rbtnModify, rbtnNone;
    private ButtonGroup btnGroup;
    private JLabel lblUserName;
  
    /** Creates a new instance of NarrativeUserTableRenderer */
    public NarrativeUserTableRenderer() {
        pnlRadioButtons = new JPanel( new FlowLayout(FlowLayout.LEFT,0,0));
        rbtnRead = new JRadioButton("Read");
        rbtnModify = new JRadioButton("Modify");
        rbtnNone = new JRadioButton("None");
        btnGroup = new ButtonGroup();
        btnGroup.add( rbtnRead );
        btnGroup.add( rbtnModify );
        btnGroup.add( rbtnNone );
        rbtnRead.setFont( CoeusFontFactory.getNormalFont() );
        rbtnModify.setFont( CoeusFontFactory.getNormalFont() );
        rbtnNone.setFont( CoeusFontFactory.getNormalFont() );
        pnlRadioButtons.add( rbtnRead );
        pnlRadioButtons.add( rbtnModify );
        pnlRadioButtons.add( rbtnNone );
        lblUserName = new JLabel();
        lblUserName.setFont( CoeusFontFactory.getNormalFont() );
    }
    /**
     * Overloaded method of DefaultTableCellRenderer which is used to show custom
     * table renderer component.
     */
    public Component getTableCellRendererComponent(JTable table,
        Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        boolean showIcon=false;    
        if( value != null ) {
            
            ProposalNarrativeModuleUsersFormBean userFormBean = 
                ( ProposalNarrativeModuleUsersFormBean ) value;
            char accessType = userFormBean.getAccessType();
            
            lblUserName.setText( userFormBean.getUserId().toUpperCase() + " ("+
                    userFormBean.getUserName() + ")" );
            switch ( accessType ) {
                case 'R' :
                            rbtnRead.setSelected( true );
                            break;
                case 'M' :
                            rbtnModify.setSelected( true );
                            break;
                case 'N':
                            rbtnNone.setSelected( true );
                            break;
            }
        }
        if( column == 2 ) {
            /* if the column is rights return the panel which consists of radio buttons */
            if( !table.isEnabled() ) {
                //Code commented to avoid displaying text.
                //System.out.println("setting bgColor");
                rbtnRead.setEnabled(false);
                rbtnModify.setEnabled(false);
                rbtnNone.setEnabled(false);
            }
            return pnlRadioButtons;
        }else if( column == 1 ) {
            /* if the column is user name column return the label component */
            return lblUserName;
        }
        return null;
    }
}
