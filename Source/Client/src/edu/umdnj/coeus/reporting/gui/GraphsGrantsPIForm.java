/**
 * $Header: /home/cvs/cvsroot/coeus/Source/edu/umdnj/coeus/reporting/gui/Attic/GraphsGrantsPIForm.java,v 1.1.2.5 2007/10/10 15:45:32 cvs Exp $
 * $Log: GraphsGrantsPIForm.java,v $
 * Revision 1.1.2.5  2007/10/10 15:45:32  cvs
 * Add fiscal year choices up to 2011
 *
 * Revision 1.1.2.4  2007/03/08 14:56:16  cvs
 * Change titles into Active for Grants, Expand some dialogs, Select DS in Grants by PI
 *
 * Revision 1.1.2.3  2007/02/27 18:27:57  cvs
 * Increase pixel height of dialogs per Therese instructions
 *
 * Revision 1.1.2.2  2007/02/08 18:16:02  cvs
 * Add Support for Top 10 functionality GUI
 *
 * Revision 1.1.2.1  2007/02/06 17:09:48  cvs
 * Add servlet support for PI Graphs GUI
 *
 *
 */
/*
 * @(#)GraphsGrantsPIForm.java 
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

public class GraphsGrantsPIForm extends CoeusDlgWindow

{
 /** Creates a new instance of GraphsGrantsPIForm */
 public GraphsGrantsPIForm() 
 {
   super(CoeusGuiConstants.getMDIForm(),"",true);
   setFormUI();
 }

 public GraphsGrantsPIForm(Component parent, boolean modal)
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
    setTitle("Graph of Active Grants by Primary Investigator");
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

    schoolLabel = new JLabel("Select format type, sort type, and run report.");
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

    reportFiscalYearLabel = new JLabel("Fiscal Year:");
    reportFiscalYearLabel.setFont(CoeusFontFactory.getLabelFont());
    gBC = new GridBagConstraints();
    gBC.gridx = 0;
    gBC.gridy = 5;
    gBC.gridwidth = 2;
    gBC.gridheight = 1;
    gBC.fill = GridBagConstraints.HORIZONTAL;
    gBC.anchor = GridBagConstraints.WEST;
    gBC.insets = insets();
    pane.add(reportFiscalYearLabel, gBC);

    comboFiscalYearList = new JComboBox(comboFiscalYearDescriptionTypes);
    gBC = new GridBagConstraints();
    gBC.gridx = 0;
    gBC.gridy = 6;
    gBC.gridwidth = 2;
    gBC.gridheight = 1;
    gBC.fill = GridBagConstraints.HORIZONTAL;
    gBC.anchor = GridBagConstraints.WEST;
    gBC.insets = insets();
    pane.add(comboFiscalYearList, gBC);

    reportSortLabel = new JLabel("Sort:");
    reportSortLabel.setFont(CoeusFontFactory.getLabelFont());
    gBC = new GridBagConstraints();
    gBC.gridx = 0;
    gBC.gridy = 7;
    gBC.gridwidth = 2;
    gBC.gridheight = 1;
    gBC.fill = GridBagConstraints.HORIZONTAL;
    gBC.anchor = GridBagConstraints.WEST;
    gBC.insets = insets();
    pane.add(reportSortLabel, gBC);

    radioBtnName = new JRadioButton("Primary Investigator");
    radioBtnName.setActionCommand("piname");
    gBC = new GridBagConstraints();
    gBC.gridx = 0;
    gBC.gridy = 8;
    gBC.gridwidth = 2;
    gBC.gridheight = 1;
    gBC.fill = GridBagConstraints.HORIZONTAL;
    gBC.anchor = GridBagConstraints.WEST;
    gBC.insets = insets();
    pane.add(radioBtnName, gBC);
    
    radioBtnTotal = new JRadioButton("Award Amount");
    radioBtnTotal.setActionCommand("amount");
    gBC = new GridBagConstraints();
    gBC.gridx = 0;
    gBC.gridy = 9;
    gBC.gridwidth = 2;
    gBC.gridheight = 1;
    gBC.fill = GridBagConstraints.HORIZONTAL;
    gBC.anchor = GridBagConstraints.WEST;
    gBC.insets = insets();
    pane.add(radioBtnTotal, gBC);

    radioBtnTop10 = new JRadioButton("Top 10");
    radioBtnTop10.setActionCommand("top10");
    gBC = new GridBagConstraints();
    gBC.gridx = 0;
    gBC.gridy = 10;
    gBC.gridwidth = 2;
    gBC.gridheight = 1;
    gBC.fill = GridBagConstraints.HORIZONTAL;
    gBC.anchor = GridBagConstraints.WEST;
    gBC.insets = insets();
    pane.add(radioBtnTop10, gBC);

    group = new ButtonGroup();
    group.add(radioBtnName);
    group.add(radioBtnTotal);
    group.add(radioBtnTop10);

    jbnRunButton = new JButton("Run");
    gBC = new GridBagConstraints();
    gBC.gridx = 0;
    gBC.gridy = 11;
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
    gBC.gridy = 11;
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

  private final int iwidth  = 370;
  private final int iheight = 450;
  private final int buttonwidth = 80;
  private final int buttonheight = 25;

  public JLabel descriptionLabel;
  public JLabel schoolLabel;
  public JLabel reportFormatsLabel;
  public JComboBox comboReportFormatsList;
  public JLabel reportFiscalYearLabel;
  public JComboBox comboFiscalYearList;
  public JLabel reportSortLabel;
  public JRadioButton radioBtnName;
  public JRadioButton radioBtnTotal;
  public JRadioButton radioBtnTop10;
  public ButtonGroup group;
  
  public JButton jbnRunButton;
  public JButton jbnCloseButton;

  public String[] comboFormatDescriptionTypes = { "PNG", "JPEG" };

  public String[] comboFormatTypes = { "PNG", "JPEG" };
    
  public String[] comboFiscalYearDescriptionTypes = 
  { 
      "None", 
      "FY 98 (7/1/1997-6/30/1998)", 
      "FY 99 (7/1/1998-6/30/1999)", 
      "FY 00 (7/1/1999-6/30/2000)", 
      "FY 01 (7/1/2000-6/30/2001)", 
      "FY 02 (7/1/2001-6/30/2002)", 
      "FY 03 (7/1/2002-6/30/2003)", 
      "FY 04 (7/1/2003-6/30/2004)", 
      "FY 05 (7/1/2004-6/30/2005)", 
      "FY 06 (7/1/2005-6/30/2006)", 
      "FY 07 (7/1/2006-6/30/2007)", 
      "FY 08 (7/1/2007-6/30/2008)",
      "FY 09 (7/1/2008-6/30/2009)",
      "FY 10 (7/1/2009-6/30/2010)",
      "FY 11 (7/1/2010-6/30/2011)"
  };
  public String[] comboFiscalYearTypes = { "", "1998", "1999", "2000", "2001", "2002", "2003", "2004", "2005", "2006", "2007", "2008", "2009", "2010", "2011"};
}
