/**
 * $Header: /home/cvs/cvsroot/coeus/Source/edu/umdnj/coeus/reporting/gui/Attic/CannedReportsForm.java,v 1.1.2.10 2007/02/27 18:27:57 cvs Exp $
 * $Log: CannedReportsForm.java,v $
 * Revision 1.1.2.10  2007/02/27 18:27:57  cvs
 * Increase pixel height of dialogs per Therese instructions
 *
 * Revision 1.1.2.9  2007/01/17 15:28:53  cvs
 * Add CSV Support
 *
 * Revision 1.1.2.8  2007/01/04 20:02:07  cvs
 * Add support for Sponsor Type Graph by School by Sponsor Type GUI
 *
 * Revision 1.1.2.7  2006/12/28 20:46:16  cvs
 *  Add support for grants by sponsor type graph GUI
 *
 * Revision 1.1.2.6  2006/12/07 15:25:17  cvs
 * Remove third options for Sample Reports and Sample Graphs
 *
 * Revision 1.1.2.5  2006/12/01 14:14:14  cvs
 * Add XLS support for formatted output to Canned Reports dialog
 *
 * Revision 1.1.2.4  2006/11/30 15:02:16  cvs
 * Remove 3D type charts from list of charts
 *
 * Revision 1.1.2.3  2006/11/20 20:50:59  cvs
 * Add TDG engine support for demo purposes
 *
 * Revision 1.1.2.2  2006/11/16 18:54:23  cvs
 * Add basic XML and HTML data retrieval and browser output mechanism
 *
 * Revision 1.1.2.1  2006/11/10 16:12:22  cvs
 * View file for Canned Reports Form
 *
 */
/*
 * @(#)CannedReportsForm.java 
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

public class CannedReportsForm extends CoeusDlgWindow

{
 /** Creates a new instance of CannedReportsForm */
 public CannedReportsForm() 
 {
   super(CoeusGuiConstants.getMDIForm(),"",true);
   setFormUI();
 }

 public CannedReportsForm(Component parent, boolean modal)
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
    setTitle("Sample Reports");
    setFont(CoeusFontFactory.getLabelFont());

    JPanel pane = new JPanel();
    pane.setLayout(new GridBagLayout());
    pane.setBorder(new javax.swing.border.CompoundBorder());
    pane.setMinimumSize(new java.awt.Dimension(iwidth, iheight));
    pane.setPreferredSize(new java.awt.Dimension(iwidth, iheight));

    setSize(iwidth, iheight);

    GridBagConstraints gBC = new GridBagConstraints();
    gBC.fill = GridBagConstraints.HORIZONTAL;

    grpOutputType = new ButtonGroup();

    radioReport = new JRadioButton("Reports");
    gBC = new GridBagConstraints();
    gBC.gridx = 0;
    gBC.gridy = 0;
    gBC.gridwidth = 4;
    gBC.gridheight = 1;
    gBC.fill = GridBagConstraints.HORIZONTAL;
    gBC.anchor = GridBagConstraints.WEST;
    gBC.insets = new Insets(5,10,5,5);
    pane.add(radioReport, gBC);

    comboReportsList = new JComboBox(comboReportTypes);
    gBC = new GridBagConstraints();
    gBC.gridx = 0;
    gBC.gridy = 1;
    gBC.gridwidth = 4;
    gBC.gridheight = 1;
    gBC.fill = GridBagConstraints.HORIZONTAL;
    gBC.anchor = GridBagConstraints.WEST;
    gBC.insets = insets();
    pane.add(comboReportsList, gBC);

    reportFormatsLabel = new JLabel("Format:");
    reportFormatsLabel.setFont(CoeusFontFactory.getLabelFont());
    gBC = new GridBagConstraints();
    gBC.gridx = 0;
    gBC.gridy = 2;
    gBC.gridwidth = 2;
    gBC.gridheight = 1;
    gBC.fill = GridBagConstraints.HORIZONTAL;
    gBC.anchor = GridBagConstraints.WEST;
    gBC.insets = insets();
    pane.add(reportFormatsLabel, gBC);

    comboReportFormatsList = new JComboBox(comboReportFormatTypes);
    gBC = new GridBagConstraints();
    gBC.gridx = 2;
    gBC.gridy = 2;
    gBC.gridwidth = 2;
    gBC.gridheight = 1;
    gBC.fill = GridBagConstraints.HORIZONTAL;
    gBC.anchor = GridBagConstraints.WEST;
    gBC.insets = insets();
    pane.add(comboReportFormatsList, gBC);

    radioGraph = new JRadioButton("Graph");
    gBC = new GridBagConstraints();
    gBC.gridx = 0;
    gBC.gridy = 4;
    gBC.gridwidth = 4;
    gBC.gridheight = 1;
    gBC.fill = GridBagConstraints.HORIZONTAL;
    gBC.anchor = GridBagConstraints.WEST;
    gBC.insets = insets();
    pane.add(radioGraph, gBC);

    comboGraphsList = new JComboBox(comboGraphTypes);
    gBC = new GridBagConstraints();
    gBC.gridx = 0;
    gBC.gridy = 5;
    gBC.gridwidth = 4;
    gBC.gridheight = 1;
    gBC.fill = GridBagConstraints.HORIZONTAL;
    gBC.anchor = GridBagConstraints.WEST;
    gBC.insets = insets();
    pane.add(comboGraphsList, gBC);

    graphFormatsLabel = new JLabel("Format:");
    graphFormatsLabel.setFont(CoeusFontFactory.getLabelFont());
    gBC = new GridBagConstraints();
    gBC.gridx = 0;
    gBC.gridy = 6;
    gBC.gridwidth = 2;
    gBC.gridheight = 1;
    gBC.fill = GridBagConstraints.HORIZONTAL;
    gBC.anchor = GridBagConstraints.WEST;
    gBC.insets = insets();
    pane.add(graphFormatsLabel, gBC);

    comboGraphFormatsList = new JComboBox(comboGraphFormatTypes);
    gBC = new GridBagConstraints();
    gBC.gridx = 2;
    gBC.gridy = 6;
    gBC.gridwidth = 2;
    gBC.gridheight = 1;
    gBC.fill = GridBagConstraints.HORIZONTAL;
    gBC.anchor = GridBagConstraints.WEST;
    gBC.insets = insets();
    pane.add(comboGraphFormatsList, gBC);

    comboGraphTypesList = new JComboBox(comboGraphChartTypes);
    gBC = new GridBagConstraints();
    gBC.gridx = 2;
    gBC.gridy = 7;
    gBC.gridwidth = 2;
    gBC.gridheight = 1;
    gBC.fill = GridBagConstraints.HORIZONTAL;
    gBC.anchor = GridBagConstraints.WEST;
    gBC.insets = insets();
    pane.add(comboGraphTypesList, gBC);
    
    jbnRunButton = new JButton("Run");
    gBC = new GridBagConstraints();
    gBC.gridx = 2;
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
    gBC.gridx = 3;
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

    grpOutputType.add(radioReport);
    grpOutputType.add(radioGraph);

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
  private final int iheight = 350;
  private final int buttonwidth = 80;
  private final int buttonheight = 25;

  public JRadioButton radioReport;
  public JRadioButton radioGraph;
  public ButtonGroup grpOutputType;

  public JComboBox comboReportsList;
  public JLabel reportFormatsLabel;
  public JComboBox comboReportFormatsList;

  public JComboBox comboGraphsList;
  public JLabel graphFormatsLabel;
  public JComboBox comboGraphFormatsList;
  public JComboBox comboGraphTypesList;


  public JButton jbnRunButton;
  public JButton jbnCloseButton;

  public String[] comboReportTypes = { "Report 1", "Report 2"};
  public String[] comboGraphTypes = { "Graph 1", "Graph 2", "Graph 3", "Graph 4"};
  public String[] comboReportFormatTypes = { "HTML", "PDF", "XML", "XLS","CSV"};
  public String[] comboGraphFormatTypes = { "PNG", "JPEG"};
  public String[] comboGraphChartTypes = { "BAR", "LINE", "AREA", "PIE" };
    
}
