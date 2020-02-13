/**
 * $Header: /home/cvs/cvsroot/coeus/Source/edu/umdnj/coeus/reporting/controller/Attic/FiscalYearController.java,v 1.1.2.3 2007/09/06 18:31:21 cvs Exp $
 * $Log: FiscalYearController.java,v $
 * Revision 1.1.2.3  2007/09/06 18:31:21  cvs
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
 * @(#)FiscalYearController.java 
 * Copyright (c) University of Medicine and Dentistry of New Jersey
 * 1 World's Fair Drive, Somerset, New Jersey 08873
 * All rights reserved.
 * 
 * Author: Romerl Elizes
 *
 * Description: Fiscal Year Panel controller for UMDNJ COEUS.
 * 
 */
package edu.umdnj.coeus.reporting.controller;

import java.applet.AppletContext;
import java.awt.Component;
import java.net.URL;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.ItemListener;
import java.awt.event.ItemEvent;
import javax.swing.JOptionPane;
import javax.swing.AbstractAction;
import java.util.Hashtable;

import edu.mit.coeus.utils.TypeConstants;
import edu.mit.coeus.utils.CoeusOptionPane;
import edu.mit.coeus.utils.CoeusGuiConstants;
import edu.umdnj.coeus.reporting.gui.FiscalYearPanel;
import edu.mit.coeus.utils.query.QueryEngine;
import edu.mit.coeus.gui.CoeusDlgWindow;
import edu.mit.coeus.gui.CoeusMessageResources;
import edu.mit.coeus.gui.CoeusFontFactory;
import edu.mit.coeus.gui.event.Controller;

import edu.mit.coeus.exception.CoeusException;

public class FiscalYearController extends Controller 
                 implements ItemListener
{
    /** Parent Component instance */
    private Component parent;

    /** CoeusMessageResources instance for Messages */
    private CoeusMessageResources coeusMessageResources;

    /** Query Engine instance */
    private QueryEngine queryEngine;
    /** FiscalYearPanel Form instance whihc form is UI */
    private FiscalYearPanel fiscalYearPanel = null;

    private int iwidth = 300;
    private int iheight = 200;

    private String FiscalYearType = "STATE";
    private String FiscalYear = "1998";

    private boolean addNone = false;

    public FiscalYearController() 
    {
        super();
        this.parent= CoeusGuiConstants.getMDIForm();
        initialiseController();
    }

    /** Creates a new instance of FiscalYearController
     * with parameters
     * @param parent Component parent form
     */    
    public FiscalYearController(
                   int iwidth,
                   int iheight)
    {
        super();
        this.parent= CoeusGuiConstants.getMDIForm();
        this.iwidth = iwidth;
        this.iheight = iheight;
        initialiseController();
    }

    public FiscalYearController(
                   int iwidth,
                   int iheight, 
                   boolean addNone)
    {
        super();
        this.parent= CoeusGuiConstants.getMDIForm();
        this.iwidth = iwidth;
        this.iheight = iheight;
        this.addNone = addNone;
        if (addNone == true) FiscalYear = "NONE";
        initialiseController(addNone);
    }


    /** Method to initialise the Controller */
    private void initialiseController() 
    {
        if(fiscalYearPanel == null) 
        {
            fiscalYearPanel =  new FiscalYearPanel(iwidth,iheight);   //(Component) parent,modal);
        }
        coeusMessageResources = CoeusMessageResources.getInstance();
        registerComponents();
    }

    /** Method to initialise the Controller */
    private void initialiseController(boolean addnone) 
    {
        if(fiscalYearPanel == null) 
        {
            fiscalYearPanel =  new FiscalYearPanel(iwidth,iheight,addnone);   //(Component) parent,modal);
        }
        coeusMessageResources = CoeusMessageResources.getInstance();
        registerComponents();
    }

    /** displey Method to make form visible */
    public void display() 
    {
        ((FiscalYearPanel) getControlledUI()).setVisible(true);
    }

    /** method to set Visible the form on close */    
    public void close() 
    {
        ((FiscalYearPanel) getControlledUI()).setVisible(false);
    }

    /** Method to return an instance of UI form
     * @return Component instaince of BudgetJustificationForm
     */
    public java.awt.Component getControlledUI() 
    {
        return fiscalYearPanel;
    }

    /** To Register  all the Actionlistner for the Components of UI */
    public void registerComponents() 
    {
    	fiscalYearPanel.comboFiscalYearTypes.addItemListener(this);
    	fiscalYearPanel.comboFiscalYears.addItemListener(this);
    }

    public void itemStateChanged(ItemEvent ev)
    {
       Object obj = ev.getSource();
       if (obj == fiscalYearPanel.comboFiscalYearTypes)
       {
    	itemStateChanged_comboFiscalYearTypes();
       }
       else
       if (obj == fiscalYearPanel.comboFiscalYears)
       {
    	itemStateChanged_comboFiscalYears();
       }
       
    }

    private void itemStateChanged_comboFiscalYearTypes()
    {
       int fnum = fiscalYearPanel.comboFiscalYearTypes.getSelectedIndex();
       if (fnum > -1)
       {
          FiscalYearType =fiscalYearPanel.fiscalYearTypesArray[fnum];
       }
    }
    
    private void itemStateChanged_comboFiscalYears()
    {
       int fnum = fiscalYearPanel.comboFiscalYears.getSelectedIndex();
       if (fnum > -1)
       {
          if (addNone == true)
             FiscalYear = fiscalYearPanel.fiscalYearsArray2[fnum];     
          else
             FiscalYear = fiscalYearPanel.fiscalYearsArray1[fnum];     
       }
    }

    public void setFormData(Object data)
    {
    }
    
    public void saveFormData()
    {
    }

    /** validate all UI actions
     * @return boolean if <true> validation true
     * @throws CoeusUIException CoeusUIException instance
     */
    public boolean validate() throws edu.mit.coeus.exception.CoeusUIException 
    {
        return false;
    }

    public void formatFields()
    {
    }

    public Object getFormData() 
    {
        return fiscalYearPanel;
    }

    /**
     * display alert message
     *
     * @param mesg the message to be displayed
     */
    private void log(String mesg) {
        CoeusOptionPane.showInfoDialog(mesg);
    }

    public FiscalYearPanel getFiscalYearPanel() 
    {
        return fiscalYearPanel;
    }

    public String getFiscalYearType() 
    { 
        return FiscalYearType; 
    }

    public String getFiscalYear() 
    { 
        return FiscalYear; 
    }
}


 
