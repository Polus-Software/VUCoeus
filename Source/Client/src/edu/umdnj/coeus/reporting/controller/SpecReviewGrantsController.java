/**
 * $Header: /home/cvs/cvsroot/coeus/Source/edu/umdnj/coeus/reporting/controller/Attic/SpecReviewGrantsController.java,v 1.1.2.1 2007/06/21 19:17:28 cvs Exp $
 * $Log: SpecReviewGrantsController.java,v $
 * Revision 1.1.2.1  2007/06/21 19:17:28  cvs
 * Add support for IRB and IACUC Grant reports
 *
 *
 */
/*
 * @(#)SpecReviewGrantsController.java 
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
import edu.umdnj.coeus.reporting.gui.SpecReviewGrantsForm;
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

public class SpecReviewGrantsController extends Controller 
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
    /** SpecReviewGrantsForm Form instance whihc form is UI */
    private SpecReviewGrantsForm specReviewGrantsForm = null;

    private static final String REPORTING_MAINTENANCE_SERVLET = "/ReportingMaintenanceServlet";
    private String UnitNumber;
    private String UnitName;

    public SpecReviewGrantsController() 
    {
        super();
        this.parent= CoeusGuiConstants.getMDIForm();
        this.modal =  true;
        initialiseController();
    }

    /** Creates a new instance of SpecReviewGrantsController
     * with parameters
     * @param parent Component parent form
     * @param modal booelan if <CODE>true<CODE> modal window
     * @param budgetInfoBean BudgetInfoBean instance - unimplemented
     */    
    public SpecReviewGrantsController(
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
        if(specReviewGrantsForm == null) 
        {
            specReviewGrantsForm =  new SpecReviewGrantsForm();   //(Component) parent,modal);
        }
        specReviewGrantsForm.descriptionLabel.setText(UnitName);
        specReviewGrantsForm.descriptionLabel.setFont(new Font("SansSerif",Font.BOLD,14));
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
        ((SpecReviewGrantsForm) getControlledUI()).setVisible(true);
    }

    /** method to set Visible the form on close */    
    public void close() 
    {
        ((SpecReviewGrantsForm) getControlledUI()).setVisible(false);
    }

    /** Method to return an instance of UI form
     * @return Component instaince of BudgetJustificationForm
     */
    public java.awt.Component getControlledUI() 
    {
        return specReviewGrantsForm;
    }

    /** To Register  all the Actionlistner for the Components of UI */
    public void registerComponents() 
    {
        specReviewGrantsForm.jbnRunButton.addActionListener(this);
        specReviewGrantsForm.jbnCloseButton.addActionListener(this);
    }

  public void actionPerformed(ActionEvent ev)
  {
     Object obj = ev.getSource();
     if (obj == specReviewGrantsForm.jbnRunButton)
     {
        RunButtonAction();
     }
     else
     if (obj == specReviewGrantsForm.jbnCloseButton)
     {
       close();
     }
  }


  private void RunButtonAction()
  {
    int fnum = specReviewGrantsForm.comboReportFormatsList.getSelectedIndex();
    int spnum = specReviewGrantsForm.comboSpecialReviewTypeList.getSelectedIndex();
    
    if (fnum == -1)
    {
       log("Please select a format.");
    }
    if (spnum == -1)
    {
       log("Please select a special review type.");
    }
    
    String strFormat = specReviewGrantsForm.comboFormatTypes[fnum];
    String strSpecialReviewType = specReviewGrantsForm.comboSpecialReviewTypes[spnum];

    generateReport(strFormat, strSpecialReviewType);
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
      return specReviewGrantsForm;
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
            String strSpecialReviewType) 
    {

        AppletContext coeusContext = CoeusGuiConstants.getMDIForm().getCoeusAppletContext();
        StringBuffer strbuf = new StringBuffer(CoeusGuiConstants.CONNECTION_URL);
        strbuf.append("/GetCurrentSpecReviewGrantsServlet?UnitName=");
        strbuf.append(URLEncoder.encode(UnitNumber));
        strbuf.append("&Format=");
        strbuf.append(URLEncoder.encode(strFormat));
        strbuf.append("&SpecReviewType=");
        strbuf.append(URLEncoder.encode(strSpecialReviewType));

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


 
