/**
 * $Header: /home/cvs/cvsroot/coeus/Source/edu/umdnj/coeus/reporting/gui/Attic/AnnualReportsForm.java,v 1.1.2.2 2007/08/06 16:15:12 cvs Exp $
 * $Log: AnnualReportsForm.java,v $
 * Revision 1.1.2.2  2007/08/06 16:15:12  cvs
 * Add GUI for Quarterly Reports
 *
 * Revision 1.1.2.1  2007/07/27 14:58:08  cvs
 * Add support for Annual Reports
 *
 *
 */
/*
 * @(#)AnnualReportForm.java 
 * Copyright (c) University of Medicine and Dentistry of New Jersey
 * 1 World's Fair Drive, Somerset, New Jersey 08873
 * All rights reserved.
 * 
 * Author: Romerl Elizes
 * Description: Annual Reports form for UMDNJ COEUS.
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

import edu.umdnj.coeus.reporting.controller.FiscalYearController;

public class AnnualReportsForm extends CoeusDlgWindow

{
 /** Creates a new instance of AnnualReportForm */
 public AnnualReportsForm() 
 {
   super(CoeusGuiConstants.getMDIForm(),"",true);
   setFormUI();
 }

 public AnnualReportsForm(Component parent, boolean modal)
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
    setTitle("Annual Report by Unit");
    setFont(CoeusFontFactory.getLabelFont());

    JPanel pane = new JPanel();
    pane.setLayout(new GridBagLayout());
    pane.setBorder(new javax.swing.border.CompoundBorder());
    pane.setMinimumSize(new java.awt.Dimension(iwidth, iheight));
    pane.setPreferredSize(new java.awt.Dimension(iwidth, iheight));

    setSize(iwidth, iheight);

    GridBagConstraints gBC = new GridBagConstraints();
    gBC.fill = GridBagConstraints.HORIZONTAL;

    descriptionLabel = new JLabel("Select unit, fiscal year type, and run report.");
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

    schoolLabel = new JLabel("Select fiscal year type, and run report.");
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

    fiscalYearController = new FiscalYearController(iwidth,100);
    gBC = new GridBagConstraints();
    gBC.gridx = 0;
    gBC.gridy = 3;
    gBC.gridwidth = 2;
    gBC.gridheight = 1;
    gBC.fill = GridBagConstraints.HORIZONTAL;
    gBC.anchor = GridBagConstraints.WEST;
    gBC.insets = insets();
    pane.add(fiscalYearController.getFiscalYearPanel(), gBC);

    jbnRunButton = new JButton("Run");
    gBC = new GridBagConstraints();
    gBC.gridx = 0;
    gBC.gridy = 4;
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
    gBC.gridy = 4;
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
    return new Insets(15, 15, 15, 15);
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
  public FiscalYearController fiscalYearController;

  public JButton jbnRunButton;
  public JButton jbnCloseButton;

}
