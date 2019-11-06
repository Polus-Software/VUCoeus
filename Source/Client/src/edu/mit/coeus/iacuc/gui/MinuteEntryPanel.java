/**
 * @(#)MinuteEntryPanel.java 1.0 November 27, 2002
 *
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved. 
 */

package edu.mit.coeus.iacuc.gui;

import edu.mit.coeus.gui.CoeusFontFactory;
import edu.mit.coeus.iacuc.bean.MinuteEntryInfoBean;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

/** <CODE>MinuteEntryPanel</CODE> is a JComponent which display
 * the minute entry details of the selected minute entry.
 * This class will be instantiated from <CODE>ScheduleMinuteMaintenance</CODE> form.
 * @author Raghunath P.V.
 * @version: 1.0 November 27, 2002
 */
public class MinuteEntryPanel extends javax.swing.JComponent {

    /** Schedule Minutes Data Bean */
    private MinuteEntryInfoBean minuteEntryInfoBean;
    /** String variable used to hold Minute Entry Description */
    private String stAreaDescription;
    /** String variable used to hold Contingency Value */
    private String stContingencyValueDesc;
    /** String variable used to hold Contingency Code */
    private String stContingencyCode;
    /** String variable used to hold Entry Type description */
    private String stTypeDesc1;
    /** boolean variable used to hold private selected status of minute entry */
    private boolean boPrivateCmdFlag1;
    /** String variable used to hold Entry Label description*/
    private String stEntry1;
    /** String variable used to hold Entry type Label description*/
    private String stEntryType1;
    /** String variable used to hold Private Label description*/
    private String stPrivateCmdLabel1;
    /** String variable used to hold Contingency Label description*/
    private String stContingencyLabel;
    /** String variable used to hold Protocol description description*/
    private String stEntryDescriptionValue1;
    /** String variable used to hold Protocol label description*/
    private String stEntryDescriptionLabel1;
    /** This is used to notify whether the panel is selected or not. */
    private boolean isSelected;

    /** Creates a new MinuteEntryPanel.<p>
     * <I> Default Constructor.</I>
     */
    public MinuteEntryPanel() {
    }

     /** Constructor which instantiates MinuteEntryPanel. It populates the components
      *  with the data available in MinuteEntryInfoBean.
      * @param minuteEntryInfoBean is a MinuteEntryInfoBean which contains the
      *  minute entry details.
      * @param isSelected is a boolean variable used to change this component
      *  background color.
      *
      * if true sets the JComponent backGround Color to Gray, else sets the backGround Color to LightGray.
      */
    public MinuteEntryPanel(MinuteEntryInfoBean minuteEntryInfoBean, 
                boolean isSelected) {
        this.minuteEntryInfoBean = minuteEntryInfoBean;
        this.isSelected = isSelected;
        initComponents();
        setMinuteValidation();
    }

    /** Helper method which is used to set the components visuble based on the
        value of EntryType in MinuteEntryInfoBean  
     */
    private void setMinuteValidation(){
        if(minuteEntryInfoBean != null){

            stEntry1 = "Entry";
            stAreaDescription = minuteEntryInfoBean.getMinuteEntry();

            stEntryType1 = "Entry Type:";
            stTypeDesc1 = minuteEntryInfoBean.getMinuteEntryTypeDesc();

            stPrivateCmdLabel1 = "Private";
            boPrivateCmdFlag1 = minuteEntryInfoBean.isPrivateCommentFlag();

            stContingencyLabel = "Contingency:";
            stContingencyCode = minuteEntryInfoBean.
                                        getProtocolContingencyCode();
            stContingencyValueDesc = minuteEntryInfoBean.
                                        getProtocolContingencyDesc();

            if (stTypeDesc1.equals("Protocol")) {
                stEntryDescriptionValue1 = minuteEntryInfoBean.
                                                getProtocolNumber();
            } else if (stTypeDesc1.equals("Other Business")) {
                stEntryDescriptionValue1 = minuteEntryInfoBean.
                                                getOtherItemDesc();
            }

            if(stTypeDesc1.equalsIgnoreCase("General Comments") ||
                stTypeDesc1.equalsIgnoreCase("Attendance")) {

                    lblEntryDescriptionLabel1.setVisible(false);
                 lblEntryDescriptionValue1.setVisible(false);
                 lblContingencyLabel.setVisible(false);
                 lblContingencyValueDesc.setVisible(false);

                 chkPrivateCmdFlag1.setVisible(true);
                 chkPrivateCmdFlag1.setSelected(boPrivateCmdFlag1);

                 lblPrivateCmdLabel1.setVisible(true);
                 lblPrivateCmdLabel1.setText(stPrivateCmdLabel1);

                 lblEntryType1.setVisible(true);
                 lblEntryType1.setText(stEntryType1);

                 lblTypeDesc1.setVisible(true);
                 lblTypeDesc1.setText(stTypeDesc1);

                 lblEntry1.setVisible(true);
                 lblEntry1.setText(stEntry1);

                 txtAreaDescription.setText(stAreaDescription);
                 //scrPnDescription.setVisible(true);
                 txtAreaDescription.setVisible(true);

             }else if(stTypeDesc1.equalsIgnoreCase("Other Business")){

                 lblContingencyLabel.setVisible(false);
                 lblContingencyValueDesc.setVisible(false);

                 chkPrivateCmdFlag1.setVisible(true);
                 chkPrivateCmdFlag1.setSelected(boPrivateCmdFlag1);

                 lblPrivateCmdLabel1.setVisible(true);
                 lblPrivateCmdLabel1.setText(stPrivateCmdLabel1);

                 lblEntryType1.setVisible(true);
                 lblEntryType1.setText(stEntryType1);

                 lblTypeDesc1.setVisible(true);
                 lblTypeDesc1.setText(stTypeDesc1);

                 lblEntry1.setVisible(true);
                 lblEntry1.setText(stEntry1);

                 txtAreaDescription.setText(stAreaDescription);
                 txtAreaDescription.setVisible(true);

                 lblEntryDescriptionLabel1.setVisible(true);
                 String stEntryDescriptionLabel = "Action Item:";
                 lblEntryDescriptionLabel1.setText(stEntryDescriptionLabel);

                 lblEntryDescriptionValue1.setVisible(true);
                 lblEntryDescriptionValue1.setText(stEntryDescriptionValue1);

             }else if(stTypeDesc1.equalsIgnoreCase("Protocol")){

                 lblContingencyLabel.setVisible(true);
                 lblContingencyLabel.setText(stContingencyLabel);

                 lblContingencyValueDesc.setVisible(true);
                 String stContingencySeperator=null;
                 if(stContingencyCode !=null && stContingencyCode.
                                                        trim().length() > 0){
                    stContingencySeperator = " - ";
                 }else {
                     stContingencySeperator = "";
                 }
                 stContingencyValueDesc = 
                    stContingencyValueDesc == null? "":stContingencyValueDesc;
                 stContingencyCode = 
                    stContingencyCode == null? "":stContingencyCode;

                 lblContingencyValueDesc.setText(stContingencyCode + 
                        stContingencySeperator + stContingencyValueDesc);

                 chkPrivateCmdFlag1.setVisible(true);
                 chkPrivateCmdFlag1.setSelected(boPrivateCmdFlag1);

                 lblPrivateCmdLabel1.setVisible(true);
                 lblPrivateCmdLabel1.setText(stPrivateCmdLabel1);

                 lblEntryType1.setVisible(true);
                 lblEntryType1.setText(stEntryType1);

                 lblTypeDesc1.setVisible(true);
                 lblTypeDesc1.setText(stTypeDesc1);

                 lblEntry1.setVisible(true);
                 lblEntry1.setText(stEntry1);

                 txtAreaDescription.setText(stAreaDescription);
                 txtAreaDescription.setVisible(true);

                 lblEntryDescriptionLabel1.setVisible(true);
                 stEntryDescriptionLabel1 = "Protocol No.";
                 lblEntryDescriptionLabel1.setText(stEntryDescriptionLabel1);

                 lblEntryDescriptionValue1.setVisible(true);
                 lblEntryDescriptionValue1.setText(stEntryDescriptionValue1);

              }else{
              }
            }
        if(isSelected){
            chkPrivateCmdFlag1.setBackground(Color.gray);
            txtAreaDescription.setBackground(Color.gray);
        }else{
            chkPrivateCmdFlag1.setBackground(new java.awt.Color(204, 204, 204));
            txtAreaDescription.setBackground(new java.awt.Color(204, 204, 204));
         }
    }
    
    public void setEnabled(boolean enabled) {
        chkPrivateCmdFlag1.setEnabled(enabled);
        txtAreaDescription.setEditable(enabled);
    }
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    private void initComponents() {//GEN-BEGIN:initComponents
        java.awt.GridBagConstraints gridBagConstraints;

        lblEntry1 = new javax.swing.JLabel();
        chkPrivateCmdFlag1 = new javax.swing.JCheckBox();
        lblPrivateCmdLabel1 = new javax.swing.JLabel();
        lblEntryType1 = new javax.swing.JLabel();
        lblTypeDesc1 = new javax.swing.JLabel();
        lblContingencyLabel = new javax.swing.JLabel();
        lblEntryDescriptionLabel1 = new javax.swing.JLabel();
        lblEntryDescriptionValue1 = new javax.swing.JLabel();
        lblContingencyValueDesc = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        txtAreaDescription = new javax.swing.JTextArea();

        setLayout(new java.awt.GridBagLayout());

        setPreferredSize(new java.awt.Dimension(500, 150));
        setMinimumSize(new java.awt.Dimension(500, 150));
        setMaximumSize(new java.awt.Dimension(500, 150));
        lblEntry1.setText("Entry");
        lblEntry1.setForeground(java.awt.Color.black);
        lblEntry1.setFont(CoeusFontFactory.getLabelFont());
        lblEntry1.setPreferredSize(new java.awt.Dimension(40, 17));
        lblEntry1.setMinimumSize(new java.awt.Dimension(40, 17));
        lblEntry1.setMaximumSize(new java.awt.Dimension(40, 17));
        lblEntry1.setHorizontalTextPosition(javax.swing.SwingConstants.LEFT);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 3, 3);
        add(lblEntry1, gridBagConstraints);

        chkPrivateCmdFlag1.setSelected(boPrivateCmdFlag1);
        chkPrivateCmdFlag1.setFont(CoeusFontFactory.getLabelFont());
        chkPrivateCmdFlag1.setBackground(new java.awt.Color(204, 204, 204));
        chkPrivateCmdFlag1.setBorder(new javax.swing.border.EtchedBorder());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 7;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHEAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 0, 3, 3);
        add(chkPrivateCmdFlag1, gridBagConstraints);

        lblPrivateCmdLabel1.setText("Private");
        lblPrivateCmdLabel1.setForeground(java.awt.Color.black);
        lblPrivateCmdLabel1.setFont(CoeusFontFactory.getLabelFont());
        lblPrivateCmdLabel1.setMaximumSize(new java.awt.Dimension(29, 17));
        lblPrivateCmdLabel1.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 6;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 5, 5, 3);
        add(lblPrivateCmdLabel1, gridBagConstraints);

        lblEntryType1.setText("Entry Type:");
        lblEntryType1.setForeground(java.awt.Color.black);
        lblEntryType1.setFont(CoeusFontFactory.getLabelFont());
        lblEntryType1.setHorizontalTextPosition(javax.swing.SwingConstants.LEFT);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHEAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 3, 3);
        add(lblEntryType1, gridBagConstraints);

        lblTypeDesc1.setText(stTypeDesc1);
        lblTypeDesc1.setForeground(java.awt.Color.black);
        lblTypeDesc1.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        lblTypeDesc1.setFont(CoeusFontFactory.getNormalFont());
        lblTypeDesc1.setPreferredSize(new java.awt.Dimension(350, 16));
        lblTypeDesc1.setMinimumSize(new java.awt.Dimension(350, 17));
        lblTypeDesc1.setHorizontalTextPosition(javax.swing.SwingConstants.LEFT);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 5;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 3, 3);
        add(lblTypeDesc1, gridBagConstraints);

        lblContingencyLabel.setText("Cont. Code.");
        lblContingencyLabel.setForeground(java.awt.Color.black);
        lblContingencyLabel.setFont(CoeusFontFactory.getLabelFont());
        lblContingencyLabel.setHorizontalTextPosition(javax.swing.SwingConstants.LEFT);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 3, 3);
        add(lblContingencyLabel, gridBagConstraints);

        lblEntryDescriptionLabel1.setText("Protocol No.");
        lblEntryDescriptionLabel1.setForeground(java.awt.Color.black);
        lblEntryDescriptionLabel1.setFont(CoeusFontFactory.getLabelFont());
        lblEntryDescriptionLabel1.setHorizontalTextPosition(javax.swing.SwingConstants.LEFT);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 4, 3);
        add(lblEntryDescriptionLabel1, gridBagConstraints);

        lblEntryDescriptionValue1.setText(stEntryDescriptionValue1);
        lblEntryDescriptionValue1.setForeground(java.awt.Color.black);
        lblEntryDescriptionValue1.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        lblEntryDescriptionValue1.setFont(CoeusFontFactory.getNormalFont());
        lblEntryDescriptionValue1.setPreferredSize(new java.awt.Dimension(200, 17));
        lblEntryDescriptionValue1.setMinimumSize(new java.awt.Dimension(200, 17));
        lblEntryDescriptionValue1.setMaximumSize(new java.awt.Dimension(200, 17));
        lblEntryDescriptionValue1.setHorizontalTextPosition(javax.swing.SwingConstants.LEFT);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 4, 5);
        add(lblEntryDescriptionValue1, gridBagConstraints);

        lblContingencyValueDesc.setText(stContingencyValueDesc == null? "":stContingencyValueDesc);
        lblContingencyValueDesc.setForeground(java.awt.Color.black);
        lblContingencyValueDesc.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        lblContingencyValueDesc.setFont(CoeusFontFactory.getNormalFont());
        lblContingencyValueDesc.setPreferredSize(new java.awt.Dimension(200, 17));
        lblContingencyValueDesc.setMinimumSize(new java.awt.Dimension(200, 17));
        lblContingencyValueDesc.setMaximumSize(new java.awt.Dimension(200, 17));
        lblContingencyValueDesc.setVerifyInputWhenFocusTarget(false);
        lblContingencyValueDesc.setHorizontalTextPosition(javax.swing.SwingConstants.LEFT);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 3, 6);
        add(lblContingencyValueDesc, gridBagConstraints);

        jScrollPane1.setHorizontalScrollBarPolicy(javax.swing.JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        jScrollPane1.setPreferredSize(new java.awt.Dimension(420, 80));
        jScrollPane1.setMinimumSize(new java.awt.Dimension(420, 80));
        jScrollPane1.setMaximumSize(new java.awt.Dimension(420, 80));
        txtAreaDescription.setLineWrap(true);
        txtAreaDescription.setForeground(java.awt.Color.black);
        txtAreaDescription.setFont(CoeusFontFactory.getNormalFont());
        txtAreaDescription.setText(stAreaDescription);
        txtAreaDescription.setBackground(new java.awt.Color(204, 204, 204));
        jScrollPane1.setViewportView(txtAreaDescription);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.gridwidth = 7;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 2);
        add(jScrollPane1, gridBagConstraints);

    }//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel lblEntryType1;
    private javax.swing.JLabel lblContingencyValueDesc;
    private javax.swing.JLabel lblEntry1;
    private javax.swing.JTextArea txtAreaDescription;
    private javax.swing.JLabel lblTypeDesc1;
    private javax.swing.JLabel lblEntryDescriptionLabel1;
    private javax.swing.JCheckBox chkPrivateCmdFlag1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel lblEntryDescriptionValue1;
    private javax.swing.JLabel lblPrivateCmdLabel1;
    private javax.swing.JLabel lblContingencyLabel;
    // End of variables declaration//GEN-END:variables

}
