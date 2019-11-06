/**
 * $Header: /home/cvs/cvsroot/coeus/Source/edu/umdnj/coeus/reporting/controller/Attic/SummaryPIGrantsController.java,v 1.1.2.3 2007/02/16 18:33:24 cvs Exp $
 * $Log: SummaryPIGrantsController.java,v $
 * Revision 1.1.2.3  2007/02/16 18:33:24  cvs
 * Add support for Summary PI Grants for all schools GUI
 *
 * Revision 1.1.2.2  2007/02/08 18:16:02  cvs
 * Add Support for Top 10 functionality GUI
 *
 * Revision 1.1.2.1  2007/02/08 16:42:21  cvs
 * Add Summary PI Grant functionality for GUI
 *
 *
 */
/*
 * @(#)SummaryPIGrantsController.java 
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
import edu.umdnj.coeus.reporting.gui.SummaryPIGrantsForm;
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

public class SummaryPIGrantsController extends Controller 
                 implements ActionListener
{
    /** Parent Component instance */
    private Component parent;
    /** Parameter For Model Form Window */
    boolean modal;

    /** CoeusMessageResources instance for Messages */
    private CoeusMessageResources coeusMessageResources;

    /** Query Engine instance */
    private QueryEngine queryEngine;
    /** SummaryPIGrantsForm Form instance whihc form is UI */
    private SummaryPIGrantsForm summaryPIGrantsForm = null;

    private static final String REPORTING_MAINTENANCE_SERVLET = "/ReportingMaintenanceServlet";
    private String UnitNumber;
    private String UnitName;

    public SummaryPIGrantsController() 
    {
        super();
        this.parent= CoeusGuiConstants.getMDIForm();
        this.modal =  true;
        initialiseController();
    }

    /** Creates a new instance of SummaryPIGrantsController
     * with parameters
     * @param parent Component parent form
     * @param modal booelan if <CODE>true<CODE> modal window
     * @param budgetInfoBean BudgetInfoBean instance - unimplemented
     */    
    public SummaryPIGrantsController(
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
        if(summaryPIGrantsForm == null) 
        {
            summaryPIGrantsForm =  new SummaryPIGrantsForm();   //(Component) parent,modal);
        }
        summaryPIGrantsForm.descriptionLabel.setText(UnitName);
        summaryPIGrantsForm.descriptionLabel.setFont(new Font("SansSerif",Font.BOLD,14));
        queryEngine = QueryEngine.getInstance();
        coeusMessageResources = CoeusMessageResources.getInstance();
        summaryPIGrantsForm.radioBtnName.setSelected(true);
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
        ((SummaryPIGrantsForm) getControlledUI()).setVisible(true);
    }

    /** method to set Visible the form on close */    
    public void close() 
    {
        ((SummaryPIGrantsForm) getControlledUI()).setVisible(false);
    }

    /** Method to return an instance of UI form
     * @return Component instaince of BudgetJustificationForm
     */
    public java.awt.Component getControlledUI() 
    {
        return summaryPIGrantsForm;
    }

    /** To Register  all the Actionlistner for the Components of UI */
    public void registerComponents() 
    {
        summaryPIGrantsForm.jbnRunButton.addActionListener(this);
        summaryPIGrantsForm.jbnCloseButton.addActionListener(this);
    }

  public void actionPerformed(ActionEvent ev)
  {
     Object obj = ev.getSource();
     if (obj == summaryPIGrantsForm.jbnRunButton)
     {
        RunButtonAction();
     }
     else
     if (obj == summaryPIGrantsForm.jbnCloseButton)
     {
       close();
     }
  }


  private void RunButtonAction()
  {
    int fynum = summaryPIGrantsForm.comboFiscalYearList.getSelectedIndex();
    String strFiscalYear = summaryPIGrantsForm.comboFiscalYearTypes[fynum];
 
    generateReport(strFiscalYear);
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
      return summaryPIGrantsForm;
  }

    /**
     * display alert message
     *
     * @param mesg the message to be displayed
     */
    private void log(String mesg) {
        CoeusOptionPane.showInfoDialog(mesg);
    }

    private boolean generateReport(String strFiscalYear) 
    {

        AppletContext coeusContext = CoeusGuiConstants.getMDIForm().getCoeusAppletContext();
        StringBuffer strbuf = new StringBuffer(CoeusGuiConstants.CONNECTION_URL);
        strbuf.append("/GetSummaryPIGrantsServlet?UnitName=");
        strbuf.append(UnitNumber);
	if (strFiscalYear.length()>0)
	{
        	strbuf.append("&FiscalYear=");
        	strbuf.append(strFiscalYear);
	}
        String command = summaryPIGrantsForm.group.getSelection().getActionCommand();
        strbuf.append("&Sort=");
        strbuf.append(command);

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


 
