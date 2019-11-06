/**
 * $Header: /home/cvs/cvsroot/coeus/Source/edu/umdnj/coeus/reporting/gui/Attic/GrantsByPIForm.java,v 1.1.2.6 2007/09/06 18:31:21 cvs Exp $
 * $Log: GrantsByPIForm.java,v $
 * Revision 1.1.2.6  2007/09/06 18:31:21  cvs
 * Fiscal Year Delineation: support GUI infrastructure
 *
 * Revision 1.1.2.5  2007/03/08 14:56:16  cvs
 * Change titles into Active for Grants, Expand some dialogs, Select DS in Grants by PI
 *
 * Revision 1.1.2.4  2007/02/27 18:27:57  cvs
 * Increase pixel height of dialogs per Therese instructions
 *
 * Revision 1.1.2.3  2007/01/30 20:24:12  cvs
 * Add servlet and GUI support for PI delimiter
 *
 * Revision 1.1.2.2  2007/01/26 18:43:19  cvs
 * Add list box for Primary Investigators
 *
 * Revision 1.1.2.1  2007/01/26 16:38:51  cvs
 * Add support for Awards by Investigators Report GUI
 *
 *
 */
/*
 * @(#)GrantsByPIForm.java 
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
import edu.umdnj.coeus.reporting.controller.FiscalYearController;

public class GrantsByPIForm extends CoeusDlgWindow

{
 /** Creates a new instance of GrantsByPIForm */
 public GrantsByPIForm() 
 {
   super(CoeusGuiConstants.getMDIForm(),"",true);
   setFormUI();
 }

 public GrantsByPIForm(Component parent, boolean modal)
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
    setTitle("Active Grants by Primary Investigator");
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

    schoolLabel = new JLabel("Select primary investigator, format type, fiscal year, and run report.");
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

    investigatorLabel = new JLabel("Investigator:");
    investigatorLabel.setFont(CoeusFontFactory.getLabelFont());
    gBC = new GridBagConstraints();
    gBC.gridx = 0;
    gBC.gridy = 2;
    gBC.gridwidth = 2;
    gBC.gridheight = 1;
    gBC.fill = GridBagConstraints.HORIZONTAL;
    gBC.anchor = GridBagConstraints.WEST;
    gBC.insets = insets();
    pane.add(investigatorLabel, gBC);

    listModel = new DefaultListModel();
    listModel.addElement("None");
    lstInvestigators = new JList(listModel);
    gBC = new GridBagConstraints();
    gBC.gridx = 0;
    gBC.gridy = 3;
    gBC.gridwidth = 2;
    gBC.gridheight = 2;
    gBC.fill = GridBagConstraints.HORIZONTAL;
    gBC.anchor = GridBagConstraints.NORTH;
    gBC.insets = insets();
    lstInvestigators.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    lstInvestigators.setSelectedIndex(0);
    lstInvestigators.setVisibleRowCount(10);
    int listwidth = 100;
    int listheight = 120;
    JScrollPane listScrollPane = new JScrollPane();          
    listScrollPane.setVerticalScrollBarPolicy(javax.swing.JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
    listScrollPane.setMaximumSize(new java.awt.Dimension(listwidth, listheight));
    listScrollPane.setMinimumSize(new java.awt.Dimension(listwidth, listheight));
    listScrollPane.setPreferredSize(new java.awt.Dimension(listwidth, listheight));
    listScrollPane.setViewportView(lstInvestigators);
    pane.add(listScrollPane, gBC);

    reportFormatsLabel = new JLabel("Format:");
    reportFormatsLabel.setFont(CoeusFontFactory.getLabelFont());
    gBC = new GridBagConstraints();
    gBC.gridx = 0;
    gBC.gridy = 5;
    gBC.gridwidth = 2;
    gBC.gridheight = 1;
    gBC.fill = GridBagConstraints.HORIZONTAL;
    gBC.anchor = GridBagConstraints.WEST;
    gBC.insets = insets();
    pane.add(reportFormatsLabel, gBC);

    comboReportFormatsList = new JComboBox(comboFormatDescriptionTypes);
    gBC = new GridBagConstraints();
    gBC.gridx = 0;
    gBC.gridy = 6;
    gBC.gridwidth = 2;
    gBC.gridheight = 1;
    gBC.fill = GridBagConstraints.HORIZONTAL;
    gBC.anchor = GridBagConstraints.WEST;
    gBC.insets = insets();
    pane.add(comboReportFormatsList, gBC);

    fiscalYearController = new FiscalYearController(iwidth,100,true);
    gBC = new GridBagConstraints();
    gBC.gridx = 0;
    gBC.gridy = 7;
    gBC.gridwidth = 2;
    gBC.gridheight = 1;
    gBC.fill = GridBagConstraints.HORIZONTAL;
    gBC.anchor = GridBagConstraints.WEST;
    gBC.insets = insets();
    pane.add(fiscalYearController.getFiscalYearPanel(), gBC);

    jbnRunButton = new JButton("Run");
    gBC = new GridBagConstraints();
    gBC.gridx = 0;
    gBC.gridy = 8;
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
    gBC.gridy = 8;
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
    return new Insets(5, 15, 5, 15);
  }

  /**
   * display alert message
   *
   * @param mesg the message to be displayed
   */
  private void log(String mesg) {
      CoeusOptionPane.showInfoDialog(mesg);
  }

  private final int iwidth  = 410;
  private final int iheight = 500;
  private final int buttonwidth = 80;
  private final int buttonheight = 25;

  public JLabel descriptionLabel;
  public JLabel schoolLabel;
  public JLabel investigatorLabel;
  public JList lstInvestigators;
  public DefaultListModel listModel;
  public JLabel reportFormatsLabel;
  public JComboBox comboReportFormatsList;
  public FiscalYearController fiscalYearController;

  public JButton jbnRunButton;
  public JButton jbnCloseButton;

  public String[] comboFormatDescriptionTypes = { "HTML", "PDF", "Excel", "CSV"};

  public String[] comboFormatTypes = { "HTML", "PDF", "XLS", "CSV"};
    
  public String[] comboInvestigatorTypes = { "None" };
  
}
