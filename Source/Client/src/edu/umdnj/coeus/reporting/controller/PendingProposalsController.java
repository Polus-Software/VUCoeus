/**
 * $Header: /home/cvs/cvsroot/coeus/Source/edu/umdnj/coeus/reporting/controller/Attic/PendingProposalsController.java,v 1.1.2.2 2007/02/09 16:52:52 cvs Exp $
 * $Log: PendingProposalsController.java,v $
 * Revision 1.1.2.2  2007/02/09 16:52:52  cvs
 * URL Encode parameter strings going out of Proposal servlets
 *
 * Revision 1.1.2.1  2007/01/18 14:56:26  cvs
 * Add Pending Proposal support
 *
 */
/*
 * @(#)PendingProposalsController.java 
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
 * Description: Grants by School controller for UMDNJ COEUS 411.
 * 
 */
package edu.umdnj.coeus.reporting.controller;

import edu.mit.coeus.gui.URLOpener;
import java.applet.AppletContext;
import java.awt.Component;
import java.awt.Font;
import java.net.URL;
import java.net.URLEncoder;
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
import edu.umdnj.coeus.reporting.gui.PendingProposalsForm;
import edu.mit.coeus.utils.query.QueryEngine;
import edu.mit.coeus.gui.CoeusDlgWindow;
import edu.mit.coeus.gui.CoeusMessageResources;
import edu.mit.coeus.gui.CoeusFontFactory;
import edu.mit.coeus.gui.event.Controller;
import edu.mit.coeus.utils.CoeusVector;
import edu.mit.coeus.utils.AppletServletCommunicator;
import edu.mit.coeus.brokers.RequesterBean;
import edu.mit.coeus.brokers.ResponderBean;

import edu.mit.coeus.exception.CoeusException;

import edu.umdnj.coeus.reporting.bean.UnitHierarchyBean;

public class PendingProposalsController extends Controller 
                 implements ActionListener, ItemListener
{
    /** Parent Component instance */
    private Component parent;
    /** Parameter For Model Form Window */
    boolean modal;

    /** CoeusMessageResources instance for Messages */
    private CoeusMessageResources coeusMessageResources;

    /** Query Engine instance */
    private QueryEngine queryEngine;
    /** PendingProposalsForm Form instance whihc form is UI */
    private PendingProposalsForm pendingProposalsForm = null;

    private static final String REPORTING_MAINTENANCE_SERVLET = "/ReportingMaintenanceServlet";
    private String UnitNumber;
    private String UnitName;

    public PendingProposalsController() 
    {
        super();
        this.parent= CoeusGuiConstants.getMDIForm();
        this.modal =  true;
        initialiseController();
    }

    /** Creates a new instance of PendingProposalsController
     * with parameters
     * @param parent Component parent form
     * @param modal booelan if <CODE>true<CODE> modal window
     * @param budgetInfoBean BudgetInfoBean instance - unimplemented
     */    
    public PendingProposalsController(
                   Component parent,
                   boolean modal,
                   String UnitNumber,
                   String UnitName)
                   //BudgetInfoBean budgetInfoBean) 
    {
        super();
        this.parent= parent;
        this.modal =  modal;
        this.UnitNumber =  UnitNumber;
        this.UnitName =  UnitName;
        initialiseController();	     
    }

    /** Method to initialise the Controller */
    private void initialiseController() 
    {
        if(pendingProposalsForm == null) 
        {
            pendingProposalsForm =  new PendingProposalsForm();   //(Component) parent,modal);
        }
        queryEngine = QueryEngine.getInstance();
        coeusMessageResources = CoeusMessageResources.getInstance();
        pendingProposalsForm.descriptionLabel.setText(UnitName);
        pendingProposalsForm.descriptionLabel.setFont(new Font("SansSerif",Font.BOLD,14));
    	pendingProposalsForm.reportFiscalYearLabel.setVisible(false);
    	pendingProposalsForm.comboFiscalYearList.setVisible(false);
    	pendingProposalsForm.reportFiscalMonthLabel.setVisible(false);
    	pendingProposalsForm.comboFiscalMonthList.setVisible(false);
        registerComponents();
        try {
            setFormData(null);
        }
        catch(Exception e) {
            e.printStackTrace(System.out);
        }
    }

    /** displey Method to make form visible */
    public void display() 
    {
        ((PendingProposalsForm) getControlledUI()).setVisible(true);
    }

    /** method to set Visible the form on close */    
    public void close() 
    {
        ((PendingProposalsForm) getControlledUI()).setVisible(false);
    }

    /** Method to return an instance of UI form
     * @return Component instaince of BudgetJustificationForm
     */
    public java.awt.Component getControlledUI() 
    {
        return pendingProposalsForm;
    }

    /** To Register  all the Actionlistner for the Components of UI */
    public void registerComponents() 
    {
    	pendingProposalsForm.comboReportFormatsList.addItemListener(this);
        pendingProposalsForm.jbnRunButton.addActionListener(this);
        pendingProposalsForm.jbnCloseButton.addActionListener(this);
    }

  public void actionPerformed(ActionEvent ev)
  {
     Object obj = ev.getSource();
     if (obj == pendingProposalsForm.jbnRunButton)
     {
        RunButtonAction();
     }
     else
     if (obj == pendingProposalsForm.jbnCloseButton)
     {
       close();
     }
  }

  public void itemStateChanged(ItemEvent ev)
  {
     Object obj = ev.getSource();
     if (obj == pendingProposalsForm.comboReportFormatsList)
     {
	itemStateChanged_comboReportFormatsList();
     }
  }

  private void itemStateChanged_comboReportFormatsList()
  {
     int fnum = pendingProposalsForm.comboReportFormatsList.getSelectedIndex();
     if (fnum > -1)
     {
        String strFormat = pendingProposalsForm.comboFormatTypes[fnum];
        if (strFormat.compareTo("PDF")==0)
	{
    		pendingProposalsForm.reportFiscalYearLabel.setVisible(true);
    		pendingProposalsForm.comboFiscalYearList.setVisible(true);
                pendingProposalsForm.reportFiscalMonthLabel.setVisible(true);
                pendingProposalsForm.comboFiscalMonthList.setVisible(true);
	}
	else
	{
    		pendingProposalsForm.reportFiscalYearLabel.setVisible(false);
    		pendingProposalsForm.comboFiscalYearList.setVisible(false);
                pendingProposalsForm.reportFiscalMonthLabel.setVisible(false);
                pendingProposalsForm.comboFiscalMonthList.setVisible(false);
	}
    	pendingProposalsForm.comboFiscalYearList.setSelectedIndex(0);
    	pendingProposalsForm.comboFiscalMonthList.setSelectedIndex(0);
     }
  }

  private void RunButtonAction()
  {
    int fnum = pendingProposalsForm.comboReportFormatsList.getSelectedIndex();
    int fynum = pendingProposalsForm.comboFiscalYearList.getSelectedIndex();
    int fmnum = pendingProposalsForm.comboFiscalMonthList.getSelectedIndex();
    if (fnum == -1)
    {
       log("Please select a format.");
    }
    
    String strFormat = pendingProposalsForm.comboFormatTypes[fnum];
    String strFiscalYear = pendingProposalsForm.comboFiscalYearTypes[fynum];
    String strFiscalMonth = pendingProposalsForm.comboMonthTypes[fmnum];

    generateReport(strFormat, strFiscalYear, strFiscalMonth);
  }

  public void setFormData(Object data)
              throws edu.mit.coeus.exception.CoeusException
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
      return pendingProposalsForm;
  }

    /**
     * display alert message
     *
     * @param mesg the message to be displayed
     */
    private void log(String mesg) {
        CoeusOptionPane.showInfoDialog(mesg);
    }

    private boolean generateReport(
            String strFormat, 
            String strFiscalYear, 
            String strFiscalMonth) 
    {

        AppletContext coeusContext = CoeusGuiConstants.getMDIForm().getCoeusAppletContext();
        StringBuffer strbuf = new StringBuffer(CoeusGuiConstants.CONNECTION_URL);
        strbuf.append("/GetPendingProposalsServlet?SchoolName=");
        strbuf.append(URLEncoder.encode(UnitName));
        strbuf.append("&Format=");
        strbuf.append(URLEncoder.encode(strFormat));
	if (strFormat.compareTo("PDF")==0 && strFiscalYear.length()>0)
	{
                String strDate = strFiscalYear;
                if (strFiscalMonth.length() > 0)
                {
                   strDate =   strFiscalMonth + "-" + strFiscalYear;
                }
        	strbuf.append("&DateFilter=");
        	strbuf.append(URLEncoder.encode(strDate));
	}

        try{
            URL templateURL = new URL(strbuf.toString());

//            if(coeusContext != null){
//                coeusContext.showDocument( templateURL, "_blank" );
//            }else{
//                javax.jnlp.BasicService bs = (javax.jnlp.BasicService)javax.jnlp.ServiceManager.lookup("javax.jnlp.BasicService");
//                bs.showDocument(templateURL);
//            }
            URLOpener.openUrl(templateURL);
            return true;
        } catch (Exception ex) {
            ex.printStackTrace();
            CoeusOptionPane.showErrorDialog(""+ex.getMessage());
            return false;
        }
    }
}


 
