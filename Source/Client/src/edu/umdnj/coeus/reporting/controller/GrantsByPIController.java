/**
 * $Header: /home/cvs/cvsroot/coeus/Source/edu/umdnj/coeus/reporting/controller/Attic/GrantsByPIController.java,v 1.1.2.5 2007/09/06 18:31:21 cvs Exp $
 * $Log: GrantsByPIController.java,v $
 * Revision 1.1.2.5  2007/09/06 18:31:21  cvs
 * Fiscal Year Delineation: support GUI infrastructure
 *
 * Revision 1.1.2.4  2007/03/08 14:56:15  cvs
 * Change titles into Active for Grants, Expand some dialogs, Select DS in Grants by PI
 *
 * Revision 1.1.2.3  2007/01/30 20:24:49  cvs
 * Add servlet and GUI support for PI delimiter
 *
 * Revision 1.1.2.2  2007/01/26 18:43:19  cvs
 * Add list box for Primary Investigators
 *
 * Revision 1.1.2.1  2007/01/26 16:39:39  cvs
 * Add support for Awards by Investigators Report GUI
 *
 *
 */
/*
 * @(#)GrantsByPIController.java 
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
import javax.swing.*;
import javax.swing.event.*;
import java.util.Hashtable;

import edu.mit.coeus.utils.TypeConstants;
import edu.mit.coeus.utils.CoeusOptionPane;
import edu.mit.coeus.utils.CoeusGuiConstants;
import edu.umdnj.coeus.reporting.gui.GrantsByPIForm;
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

import edu.umdnj.coeus.reporting.bean.PIBean;

import edu.umdnj.coeus.reporting.bean.UnitHierarchyBean;

public class GrantsByPIController extends Controller 
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
    /** GrantsByPIForm Form instance whihc form is UI */
    private GrantsByPIForm grantsByPIForm = null;

    private static final String REPORTING_MAINTENANCE_SERVLET = "/ReportingMaintenanceServlet";
    private String UnitNumber;
    private String UnitName;

    // Vectors for PI's
    private CoeusVector cvUMDNJPI;
    private CoeusVector cvNonUMDNJPI;
    private CoeusVector mainvector;
    private boolean hasRight;

    public GrantsByPIController() 
    {
        super();
        this.parent= CoeusGuiConstants.getMDIForm();
        this.modal =  true;
        initialiseController();
    }

    /** Creates a new instance of GrantsByPIController
     * with parameters
     * @param parent Component parent form
     * @param modal booelan if <CODE>true<CODE> modal window
     * @param budgetInfoBean BudgetInfoBean instance - unimplemented
     */    
    public GrantsByPIController(
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
        defaultVectors();
        if(grantsByPIForm == null) 
        {
            grantsByPIForm =  new GrantsByPIForm();   //(Component) parent,modal);
        }
        grantsByPIForm.descriptionLabel.setText(UnitName);
        grantsByPIForm.descriptionLabel.setFont(new Font("SansSerif",Font.BOLD,14));
        queryEngine = QueryEngine.getInstance();
        coeusMessageResources = CoeusMessageResources.getInstance();
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
        ((GrantsByPIForm) getControlledUI()).setVisible(true);
    }

    /** method to set Visible the form on close */    
    public void close() 
    {
        ((GrantsByPIForm) getControlledUI()).setVisible(false);
    }

    /** Method to return an instance of UI form
     * @return Component instaince of BudgetJustificationForm
     */
    public java.awt.Component getControlledUI() 
    {
        return grantsByPIForm;
    }

    /** To Register  all the Actionlistner for the Components of UI */
    public void registerComponents() 
    {
        grantsByPIForm.jbnRunButton.addActionListener(this);
        grantsByPIForm.jbnCloseButton.addActionListener(this);
      
    }

  public void actionPerformed(ActionEvent ev)
  {
     Object obj = ev.getSource();
     if (obj == grantsByPIForm.jbnRunButton)
     {
        RunButtonAction();
     }
     else
     if (obj == grantsByPIForm.jbnCloseButton)
     {
       close();
     }
  }

  private void RunButtonAction()
  {
    int fnum = grantsByPIForm.comboReportFormatsList.getSelectedIndex();
    int fpinum = grantsByPIForm.lstInvestigators.getSelectedIndex();
    if (fnum == -1)
    {
       log("Please select a format.");
       return;
    }
    if (fpinum == -1)
    {
        log("Please select an investigator");
        return;
    }
    
    
    String strFormat = grantsByPIForm.comboFormatTypes[fnum];
    String strFiscalYearType = grantsByPIForm.fiscalYearController.getFiscalYearType();
    String strFiscalYear = grantsByPIForm.fiscalYearController.getFiscalYear();
    String strInvestigator = "";
    if (fpinum > 0)
    {
        PIBean obj = (PIBean)mainvector.elementAt(fpinum-1);
        strInvestigator = obj.getPersonID();
    }

    generateReport(strFormat, strFiscalYearType, strFiscalYear, strInvestigator);
  }

  public void setFormData(Object data)
              throws edu.mit.coeus.exception.CoeusException
  {
       retrieveListInfo(UnitNumber);
  }
  
  private void retrieveListInfo(String UnitName)
          throws CoeusException
  {
        Hashtable htData  = getPIData(UnitName);
        if (htData != null)
        {
            cvUMDNJPI = new CoeusVector();
            cvNonUMDNJPI = new CoeusVector();
            try
            {
                cvUMDNJPI = (CoeusVector)htData.get(new Integer(0));
                cvNonUMDNJPI = (CoeusVector)htData.get(new Integer(1));
                hasRight = ((Boolean)htData.get(new Integer(2))).booleanValue();
                setListData(cvUMDNJPI,cvNonUMDNJPI);
            }
            catch (Exception e)
            {
                e.printStackTrace(System.out);
            }


        }
      
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
      return grantsByPIForm;
  }

    /**
     * display alert message
     *
     * @param mesg the message to be displayed
     */
    private void log(String mesg) {
        CoeusOptionPane.showInfoDialog(mesg);
    }

    private boolean generateReport(String strFormat, String strFiscalYearType, String strFiscalYear, String strInvestigator) 
    {

        AppletContext coeusContext = CoeusGuiConstants.getMDIForm().getCoeusAppletContext();
        StringBuffer strbuf = new StringBuffer(CoeusGuiConstants.CONNECTION_URL);
        strbuf.append("/GetGrantsByPIServlet?UnitName=");
        strbuf.append(UnitNumber);
        strbuf.append("&Format=");
        strbuf.append(strFormat);
        strbuf.append("&FiscalYearType=");
        strbuf.append(strFiscalYearType);
        strbuf.append("&FiscalYear=");
        strbuf.append(strFiscalYear);
        if (strInvestigator!=null && strInvestigator.length()>0)
        {
            strbuf.append("&Investigator=");
            strbuf.append(strInvestigator);
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

    private void defaultVectors()
    {
        hasRight = false;
        cvUMDNJPI = null;
        cvNonUMDNJPI = null;
        mainvector = null;
    }
    
    private Hashtable getPIData(String UnitName)
            throws CoeusException
    {
        RequesterBean requesterBean = new RequesterBean();

        requesterBean.setFunctionType('A');//"GET_UMDNJ_PI"

        Hashtable data=null;

        AppletServletCommunicator appletServletCommunicator = new AppletServletCommunicator();

        String strurl = CoeusGuiConstants.CONNECTION_URL + REPORTING_MAINTENANCE_SERVLET + "?UnitName=" + UnitName;
                
        appletServletCommunicator.setConnectTo(strurl);

        appletServletCommunicator.setRequest(requesterBean);

        appletServletCommunicator.send();

        ResponderBean responderBean = appletServletCommunicator.getResponse();

        if(responderBean!= null){

            if(!responderBean.isSuccessfulResponse()){

                throw new CoeusException(responderBean.getMessage(), 1);

            }else{

                data = (Hashtable)responderBean.getDataObject();

            }

        }

        return data;        
    }
    
    private void setListData(CoeusVector data1,CoeusVector data2)
    {
        mainvector = new CoeusVector();
        if (data1 != null && data1.size() > 0)
        {
            for (int inum =0 ; inum < data1.size(); inum++)
                mainvector.addElement(data1.elementAt(inum));
        }
        if (data2 != null && data2.size() > 0)
        {
            for (int inum = 0; inum < data2.size(); inum++)
                mainvector.addElement(data2.elementAt(inum));
        }
        
        if (mainvector != null && mainvector.size() > 0)
        {
            grantsByPIForm.listModel.removeAllElements();
            int ilst = 0;
            grantsByPIForm.listModel.addElement("None");
            
            for (int inum = 0; inum < mainvector.size(); inum++)
            {
                PIBean obj = (PIBean)mainvector.elementAt(inum);
                grantsByPIForm.listModel.addElement(obj.getFullName());
            }
        }
    }
}


 
