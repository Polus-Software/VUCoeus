/**
 * $Header: /home/cvs/cvsroot/coeus/Source/edu/umdnj/coeus/reporting/gui/Attic/FiscalYearPanel.java,v 1.1.2.4 2007/09/07 13:48:10 cvs Exp $
 * $Log: FiscalYearPanel.java,v $
 * Revision 1.1.2.4  2007/09/07 13:48:10  cvs
 * Re-factor code to address misplaced FY
 *
 * Revision 1.1.2.3  2007/09/07 12:32:21  cvs
 * Fiscal Year Delineation: support GUI infrastructure
 *
 * Revision 1.1.2.2  2007/08/02 15:55:09  cvs
 * Address size differences between parent and child component widths
 *
 * Revision 1.1.2.1  2007/07/27 14:58:08  cvs
 * Add support for Annual Reports
 *
 *
 */
/*
 * @(#)FiscalYearPanel.java 
 * Copyright (c) University of Medicine and Dentistry of New Jersey
 * 1 World's Fair Drive, Somerset, New Jersey 08873
 * All rights reserved.
 *
 * 
 * Author: Romerl Elizes
 * Description: Fiscal Year Panel for UMDNJ COEUS.
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

public class FiscalYearPanel extends JPanel
{
    public JLabel lblFiscalYearsLabel;
    public JLabel lblFiscalYearTypeLabel;
    public JComboBox comboFiscalYearTypes;
    public JComboBox comboFiscalYears;

    private int iwidth = 300;
    private int iheight = 200;
    private boolean addnone = false;

    public FiscalYearPanel()
    {
        initComponents();
    }

    public FiscalYearPanel(int width, int height)
    {
        this.iwidth = width;
        this.iheight = height;
        initComponents();
    }

    public FiscalYearPanel(int width, int height, boolean addnone)
    {
        this.iwidth = width;
        this.iheight = height;
        this.addnone = addnone;
        initComponents();
    }


    private void initComponents() 
    {
        setLayout(new GridLayout(4,1));
    
        setBorder(new javax.swing.border.CompoundBorder());
        setMinimumSize(new java.awt.Dimension(iwidth, iheight));
        setMaximumSize(new java.awt.Dimension(iwidth, iheight));
        setPreferredSize(new java.awt.Dimension(iwidth, iheight));
    
        setSize(iwidth, iheight);
    
        lblFiscalYearTypeLabel = new JLabel("Fiscal Year Type:");
        lblFiscalYearTypeLabel.setFont(CoeusFontFactory.getLabelFont());
        add(lblFiscalYearTypeLabel);

        comboFiscalYearTypes = new JComboBox(FiscalYearTypesArray);
        add(comboFiscalYearTypes);
    
        lblFiscalYearsLabel = new JLabel("Fiscal Year");
        lblFiscalYearsLabel.setFont(CoeusFontFactory.getLabelFont());
        add(lblFiscalYearsLabel);
    
        if (addnone == true)    
           comboFiscalYears = new JComboBox(FiscalYearsArray2);
        else
           comboFiscalYears = new JComboBox(FiscalYearsArray1);
        add(comboFiscalYears);
    }

  public Insets insets() 
  {
    return new Insets(0,0,0,0);
  }

    public String[] fiscalYearTypesArray =
    {
       "STATE",
       "NIH",
       "CALENDAR"
    };

    public String[] FiscalYearTypesArray =
    {
       "State - (7/1/XXXX-1 to 6/30/XXXX)",
       "NIH - (10/1/XXXX-1 to 9/30/XXXX)",
       "Calendar - (1/1/XXXX to 12/31/XXXX)"
    };

    public String[] FiscalYearsArray1 = 
    { 
        "FY 1998", 
        "FY 1999", 
        "FY 2000", 
        "FY 2001", 
        "FY 2002", 
        "FY 2003", 
        "FY 2004", 
        "FY 2005", 
        "FY 2006", 
        "FY 2007", 
        "FY 2008",
        "FY 2009",
        "FY 2010",
        "FY 2011",
        "FY 2012"
    };

    public String[] FiscalYearsArray2 = 
    { 
        "None", 
        "FY 1998", 
        "FY 1999", 
        "FY 2000", 
        "FY 2001", 
        "FY 2002", 
        "FY 2003", 
        "FY 2004", 
        "FY 2005", 
        "FY 2006", 
        "FY 2007", 
        "FY 2008",
        "FY 2009",
        "FY 2010",
        "FY 2011",
        "FY 2012"
    };


    public String[] fiscalYearsArray1 = 
    { 
        "1998", 
        "1999", 
        "2000", 
        "2001", 
        "2002", 
        "2003", 
        "2004", 
        "2005", 
        "2006", 
        "2007", 
        "2008",
        "2009",
        "2010",
        "2011",
        "2012"
    };

    public String[] fiscalYearsArray2 = 
    { 
        "NONE", 
        "1998", 
        "1999", 
        "2000", 
        "2001", 
        "2002", 
        "2003", 
        "2004", 
        "2005", 
        "2006", 
        "2007", 
        "2008",
        "2009",
        "2010",
        "2011",
        "2012"
    };
}   
    

