/**
 * $Header: /home/cvs/cvsroot/coeus/Source/edu/umdnj/coeus/reporting/gui/Attic/CurrentActiveGrantsForm.java,v 1.1.2.4 2007/08/16 18:45:53 cvs Exp $
 * $Log: CurrentActiveGrantsForm.java,v $
 * Revision 1.1.2.4  2007/08/16 18:45:53  cvs
 * Add support for sorting by PI and Department
 *
 * Revision 1.1.2.3  2007/07/25 18:39:25  cvs
 * Add Headers and Snippet integrity in PDF
 *
 * Revision 1.1.2.2  2007/06/18 17:15:48  cvs
 * Add check for Enforce Project End Date
 *
 * Revision 1.1.2.1  2007/03/29 16:19:40  cvs
 * Add support for Current Active Grants Report
 *
 *
 */
/*
 * @(#)CurrentActiveGrantsForm.java 
 *
 * Copyright (c) University of Medicine and Dentistry of New Jersey
 *
 * 1 World's Fair Drive, Somerset, New Jersey 08873
 *
 * All rights reserved.
 *
 * 
 * Author: Romerl Elizes
 *
 * Description: Canned Reports form for UMDNJ COEUS 411.
 * 
 */
package edu.umdnj.coeus.reporting.gui;

import java.util.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.event.*;



import edu.mit.coeus.utils.CoeusGuiConstants;
import edu.mit.coeus.utils.CoeusOptionPane;
import edu.mit.coeus.gui.CoeusDlgWindow;
import edu.mit.coeus.gui.CoeusFontFactory;

public class CurrentActiveGrantsForm extends CoeusDlgWindow

{
 /** Creates a new instance of CurrentActiveGrantsForm */
 public CurrentActiveGrantsForm() 
 {
   super(CoeusGuiConstants.getMDIForm(),"",true);
   setFormUI();
 }

 public CurrentActiveGrantsForm(Component parent, boolean modal)
 {
   super((Component)parent,"",true);
   setFormUI();
 }

 public void setFormUI() 
 {        
    initComponents();
    //setResizable(false);
    pack();
    Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
    Dimension dlgSize = getSize();
    setLocation(screenSize.width/2 - (dlgSize.width/2),
    screenSize.height/2 - (dlgSize.height/2));
  }

    
  private void initComponents() 
  {
    setTitle("Current Active Grants by Unit");
    setFont(CoeusFontFactory.getLabelFont());

    JPanel pane = new JPanel();
    pane.setLayout(new GridBagLayout());
    pane.setBorder(new javax.swing.border.CompoundBorder());
    pane.setMinimumSize(new java.awt.Dimension(iwidth, iheight));
    pane.setPreferredSize(new java.awt.Dimension(iwidth, iheight));

    setSize(iwidth, iheight);

    GridBagConstraints gBC = new GridBagConstraints();
    gBC.fill = GridBagConstraints.HORIZONTAL;

    descriptionLabel = new JLabel("Unit:");
    descriptionLabel.setFont(CoeusFontFactory.getLabelFont());
    gBC = new GridBagConstraints();
    gBC.gridx = 0;
    gBC.gridy = 0;
    gBC.gridwidth = 2;
    gBC.gridheight = 1;
    gBC.fill = GridBagConstraints.HORIZONTAL;
    gBC.anchor = GridBagConstraints.WEST;
    gBC.insets = insets();
    pane.add(descriptionLabel, gBC);

    schoolLabel = new JLabel("Select format type, and run report.");
    schoolLabel.setFont(CoeusFontFactory.getLabelFont());
    gBC = new GridBagConstraints();
    gBC.gridx = 0;
    gBC.gridy = 1;
    gBC.gridwidth = 2;
    gBC.gridheight = 1;
    gBC.fill = GridBagConstraints.HORIZONTAL;
    gBC.anchor = GridBagConstraints.WEST;
    gBC.insets = insets();
    pane.add(schoolLabel, gBC);

    reportFormatsLabel = new JLabel("Format:");
    reportFormatsLabel.setFont(CoeusFontFactory.getLabelFont());
    gBC = new GridBagConstraints();
    gBC.gridx = 0;
    gBC.gridy = 3;
    gBC.gridwidth = 2;
    gBC.gridheight = 1;
    gBC.fill = GridBagConstraints.HORIZONTAL;
    gBC.anchor = GridBagConstraints.WEST;
    gBC.insets = insets();
    pane.add(reportFormatsLabel, gBC);

    comboReportFormatsList = new JComboBox(comboFormatDescriptionTypes);
    gBC = new GridBagConstraints();
    gBC.gridx = 0;
    gBC.gridy = 4;
    gBC.gridwidth = 2;
    gBC.gridheight = 1;
    gBC.fill = GridBagConstraints.HORIZONTAL;
    gBC.anchor = GridBagConstraints.WEST;
    gBC.insets = insets();
    pane.add(comboReportFormatsList, gBC);

    reportNIHLabel = new JLabel("NIH:");
    reportNIHLabel.setFont(CoeusFontFactory.getLabelFont());
    gBC = new GridBagConstraints();
    gBC.gridx = 0;
    gBC.gridy = 5;
    gBC.gridwidth = 1;
    gBC.gridheight = 1;
    gBC.fill = GridBagConstraints.HORIZONTAL;
    gBC.anchor = GridBagConstraints.WEST;
    gBC.insets = insets();
    pane.add(reportNIHLabel, gBC);

    checkNIH = new JCheckBox();
    gBC = new GridBagConstraints();
    gBC.gridx = 1;
    gBC.gridy = 5;
    gBC.gridwidth = 1;
    gBC.gridheight = 1;
    gBC.fill = GridBagConstraints.HORIZONTAL;
    gBC.anchor = GridBagConstraints.EAST;
    gBC.insets = insets();
    pane.add(checkNIH, gBC);

    enforceProjectEndDateLabel = new JLabel("Enforce Project End Date:");
    enforceProjectEndDateLabel.setFont(CoeusFontFactory.getLabelFont());
    gBC = new GridBagConstraints();
    gBC.gridx = 0;
    gBC.gridy = 6;
    gBC.gridwidth = 1;
    gBC.gridheight = 1;
    gBC.fill = GridBagConstraints.HORIZONTAL;
    gBC.anchor = GridBagConstraints.WEST;
    gBC.insets = insets();
    pane.add(enforceProjectEndDateLabel, gBC);

    checkProjectEndDate = new JCheckBox();
    gBC = new GridBagConstraints();
    gBC.gridx = 1;
    gBC.gridy = 6;
    gBC.gridwidth = 1;
    gBC.gridheight = 1;
    gBC.fill = GridBagConstraints.HORIZONTAL;
    gBC.anchor = GridBagConstraints.EAST;
    gBC.insets = insets();
    pane.add(checkProjectEndDate, gBC);

    repeatHeaderLabel = new JLabel("Repeat Heading throughout report:");
    repeatHeaderLabel.setFont(CoeusFontFactory.getLabelFont());
    gBC = new GridBagConstraints();
    gBC.gridx = 0;
    gBC.gridy = 7;
    gBC.gridwidth = 1;
    gBC.gridheight = 1;
    gBC.fill = GridBagConstraints.HORIZONTAL;
    gBC.anchor = GridBagConstraints.WEST;
    gBC.insets = insets();
    pane.add(repeatHeaderLabel, gBC);

    checkRepeatHeader = new JCheckBox();
    gBC = new GridBagConstraints();
    gBC.gridx = 1;
    gBC.gridy = 7;
    gBC.gridwidth = 1;
    gBC.gridheight = 1;
    gBC.fill = GridBagConstraints.HORIZONTAL;
    gBC.anchor = GridBagConstraints.EAST;
    gBC.insets = insets();
    pane.add(checkRepeatHeader, gBC);

    reportSortLabel = new JLabel("Sort:");
    reportSortLabel.setFont(CoeusFontFactory.getLabelFont());
    gBC = new GridBagConstraints();
    gBC.gridx = 0;
    gBC.gridy = 8;
    gBC.gridwidth = 2;
    gBC.gridheight = 1;
    gBC.fill = GridBagConstraints.HORIZONTAL;
    gBC.anchor = GridBagConstraints.WEST;
    gBC.insets = insets();
    pane.add(reportSortLabel, gBC);

    radioBtnAwardNo = new JRadioButton("Award Number");
    radioBtnAwardNo.setActionCommand("awardno");
    gBC = new GridBagConstraints();
    gBC.gridx = 0;
    gBC.gridy = 9;
    gBC.gridwidth = 2;
    gBC.gridheight = 1;
    gBC.fill = GridBagConstraints.HORIZONTAL;
    gBC.anchor = GridBagConstraints.WEST;
    gBC.insets = insets();
    pane.add(radioBtnAwardNo, gBC);
    
    radioBtnPI = new JRadioButton("Primary Investigator");
    radioBtnPI.setActionCommand("pi");
    gBC = new GridBagConstraints();
    gBC.gridx = 0;
    gBC.gridy = 10;
    gBC.gridwidth = 2;
    gBC.gridheight = 1;
    gBC.fill = GridBagConstraints.HORIZONTAL;
    gBC.anchor = GridBagConstraints.WEST;
    gBC.insets = insets();
    pane.add(radioBtnPI, gBC);

    radioBtnDept = new JRadioButton("Department");
    radioBtnDept.setActionCommand("department");
    gBC = new GridBagConstraints();
    gBC.gridx = 0;
    gBC.gridy = 11;
    gBC.gridwidth = 2;
    gBC.gridheight = 1;
    gBC.fill = GridBagConstraints.HORIZONTAL;
    gBC.anchor = GridBagConstraints.WEST;
    gBC.insets = insets();
    pane.add(radioBtnDept, gBC);

    group = new ButtonGroup();
    group.add(radioBtnAwardNo);
    group.add(radioBtnPI);
    group.add(radioBtnDept);

    jbnRunButton = new JButton("Run");
    gBC = new GridBagConstraints();
    gBC.gridx = 0;
    gBC.gridy = 12;
    gBC.gridwidth = 1;
    gBC.gridheight = 1;
    gBC.fill = GridBagConstraints.HORIZONTAL;
    gBC.anchor = GridBagConstraints.WEST;
    gBC.insets = insets();
    jbnRunButton.setMaximumSize(new java.awt.Dimension(buttonwidth, buttonheight));
    jbnRunButton.setMinimumSize(new java.awt.Dimension(buttonwidth, buttonheight));
    jbnRunButton.setPreferredSize(new java.awt.Dimension(buttonwidth, buttonheight));
    pane.add(jbnRunButton, gBC);

    jbnCloseButton = new JButton("Close");
    gBC = new GridBagConstraints();
    gBC.gridx = 1;
    gBC.gridy = 12;
    gBC.gridwidth = 1;
    gBC.gridheight = 1;
    gBC.fill = GridBagConstraints.HORIZONTAL;
    gBC.anchor = GridBagConstraints.WEST;
    gBC.insets = insets();
    jbnCloseButton.setMaximumSize(new java.awt.Dimension(buttonwidth, buttonheight));
    jbnCloseButton.setMinimumSize(new java.awt.Dimension(buttonwidth, buttonheight));
    jbnCloseButton.setPreferredSize(new java.awt.Dimension(buttonwidth, buttonheight));
    pane.add(jbnCloseButton, gBC);

    getContentPane().add(pane); 

  }

  public Insets insets() 
  {
    return new Insets(5, 5, 5, 5);
  }

  /**
   * display alert message
   *
   * @param mesg the message to be displayed
   */
  private void log(String mesg) {
      CoeusOptionPane.showInfoDialog(mesg);
  }

  private final int iwidth  = 310;
  private final int iheight = 450;
  private final int buttonwidth = 80;
  private final int buttonheight = 25;

  public JLabel descriptionLabel;
  public JLabel schoolLabel;
  public JLabel reportFormatsLabel;
  public JComboBox comboReportFormatsList;
  public JLabel reportNIHLabel;
  public JCheckBox checkNIH;
  public JLabel enforceProjectEndDateLabel;
  public JCheckBox checkProjectEndDate;
  public JLabel repeatHeaderLabel;
  public JCheckBox checkRepeatHeader;
  public JLabel reportSortLabel;
  public JRadioButton radioBtnAwardNo;
  public JRadioButton radioBtnPI;
  public JRadioButton radioBtnDept;
  public ButtonGroup group;

  public JButton jbnRunButton;
  public JButton jbnCloseButton;

  public String[] comboFormatDescriptionTypes = { "HTML", "PDF", "Excel", "CSV"};

  public String[] comboFormatTypes = { "HTML", "PDF", "XLS", "CSV"};
}
