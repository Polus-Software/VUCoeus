/**
 * $Header: /home/cvs/cvsroot/coeus/Source/edu/umdnj/coeus/reporting/controller/Attic/GrantsBySponsorController.java,v 1.1.2.5 2007/09/06 18:31:21 cvs Exp $
 * $Log: GrantsBySponsorController.java,v $
 * Revision 1.1.2.5  2007/09/06 18:31:21  cvs
 * Fiscal Year Delineation: support GUI infrastructure
 *
 * Revision 1.1.2.4  2007/06/20 18:47:23  cvs
 * Add database retrieval for Sponsor Type Description
 *
 * Revision 1.1.2.3  2007/06/19 19:51:39  cvs
 * Fix GUI bug by url encoding Format Type string
 *
 * Revision 1.1.2.2  2007/01/17 17:21:24  cvs
 * Add NIH support
 *
 * Revision 1.1.2.1  2006/12/27 19:15:31  cvs
 *  Add support for grants by sponsor type GUI
 *
 *
 */
/*
 * @(#)GrantsBySponsorController.java 
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
import edu.umdnj.coeus.reporting.gui.GrantsBySponsorForm;
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

public class GrantsBySponsorController extends Controller 
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
    /** GrantsBySponsorForm Form instance whihc form is UI */
    private GrantsBySponsorForm grantsBySponsorForm = null;
    private boolean hasRight;


    private static final String REPORTING_MAINTENANCE_SERVLET = "/ReportingMaintenanceServlet";
    private String UnitNumber;
    private String UnitName;

    public GrantsBySponsorController() 
    {
        super();
        this.parent= CoeusGuiConstants.getMDIForm();
        this.modal =  true;
        initialiseController();
    }

    /** Creates a new instance of GrantsBySponsorController
     * with parameters
     * @param parent Component parent form
     * @param modal booelan if <CODE>true<CODE> modal window
     * @param budgetInfoBean BudgetInfoBean instance - unimplemented
     */    
    public GrantsBySponsorController(
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
        if(grantsBySponsorForm == null) 
        {
            grantsBySponsorForm =  new GrantsBySponsorForm();   //(Component) parent,modal);
        }
        grantsBySponsorForm.descriptionLabel.setText(UnitName);
        grantsBySponsorForm.descriptionLabel.setFont(new Font("SansSerif",Font.BOLD,14));
        queryEngine = QueryEngine.getInstance();
        coeusMessageResources = CoeusMessageResources.getInstance();
        grantsBySponsorForm.checkNIH.setVisible(false);
        grantsBySponsorForm.checkNIH.setSelected(false);
        grantsBySponsorForm.reportNIHLabel.setVisible(false);
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
        ((GrantsBySponsorForm) getControlledUI()).setVisible(true);
    }

    /** method to set Visible the form on close */    
    public void close() 
    {
        ((GrantsBySponsorForm) getControlledUI()).setVisible(false);
    }

    /** Method to return an instance of UI form
     * @return Component instaince of BudgetJustificationForm
     */
    public java.awt.Component getControlledUI() 
    {
        return grantsBySponsorForm;
    }

    /** To Register  all the Actionlistner for the Components of UI */
    public void registerComponents() 
    {
        grantsBySponsorForm.jbnRunButton.addActionListener(this);
        grantsBySponsorForm.jbnCloseButton.addActionListener(this);
    }

  public void actionPerformed(ActionEvent ev)
  {
     Object obj = ev.getSource();
     if (obj == grantsBySponsorForm.jbnRunButton)
     {
        RunButtonAction();
     }
     else
     if (obj == grantsBySponsorForm.jbnCloseButton)
     {
       close();
     }
  }

  public void itemStateChanged(ItemEvent ev)
  {
     Object obj = ev.getSource();
     if (obj == grantsBySponsorForm.comboSponsorTypeList)
     {
         itemStateChanged_comboSponsorTypeList();
     }
  }

  private void itemStateChanged_comboSponsorTypeList()
  {
     int fnum = grantsBySponsorForm.comboSponsorTypeList.getSelectedIndex();
     if (fnum > -1)
     {
        String strFormat = ((String)grantsBySponsorForm.comboSponsorTypeList.getItemAt(fnum));
        if (strFormat.compareTo("Federal")==0)
	{
          grantsBySponsorForm.checkNIH.setVisible(true);
          grantsBySponsorForm.checkNIH.setSelected(false);
          grantsBySponsorForm.reportNIHLabel.setVisible(true);
	}
	else
	{
          grantsBySponsorForm.checkNIH.setVisible(false);
          grantsBySponsorForm.checkNIH.setSelected(false);
          grantsBySponsorForm.reportNIHLabel.setVisible(false);
	}
     }      
  }


  private void RunButtonAction()
  {
    int hnum = grantsBySponsorForm.comboSponsorTypeList.getSelectedIndex();
    int fnum = grantsBySponsorForm.comboReportFormatsList.getSelectedIndex();
    if (fnum == -1)
    {
       log("Please select a format.");
    }
    
    String strSponsorType = ((String)grantsBySponsorForm.comboSponsorTypeList.getItemAt(hnum));
    String strFormat = grantsBySponsorForm.comboFormatTypes[fnum];
    String strFiscalYearType = grantsBySponsorForm.fiscalYearController.getFiscalYearType();
    String strFiscalYear = grantsBySponsorForm.fiscalYearController.getFiscalYear();

    if (strSponsorType.compareTo("None")==0)
       strSponsorType = "";

    generateReport(strSponsorType,strFormat, strFiscalYearType, strFiscalYear);
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
      return grantsBySponsorForm;
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
                   String strSponsorType, 
                   String strFormat, 
                   String strFiscalYearType, 
                   String strFiscalYear) 
    {
        AppletContext coeusContext = CoeusGuiConstants.getMDIForm().getCoeusAppletContext();
        StringBuffer strbuf = new StringBuffer(CoeusGuiConstants.CONNECTION_URL);
        strbuf.append("/GetGrantsBySponsorServlet?Format=");
        strbuf.append(strFormat);
        strbuf.append("&UnitName=");
        strbuf.append(UnitNumber);
        if (strSponsorType.length()>0)
        {
           strbuf.append("&SponsorType=");
           strbuf.append(URLEncoder.encode(strSponsorType));
           
           if (strSponsorType.compareTo("Federal")==0)
           {
               boolean bNIH = grantsBySponsorForm.checkNIH.isSelected();
               if (bNIH==true)
               {
                   strbuf.append("&NIH=true");
               }
           }
        }
        strbuf.append("&FiscalYearType=");
        strbuf.append(strFiscalYearType);
        strbuf.append("&FiscalYear=");
        strbuf.append(strFiscalYear);

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
    
  public void setFormData(Object data)
              throws edu.mit.coeus.exception.CoeusException
  {
       retrieveListInfo();
       grantsBySponsorForm.comboSponsorTypeList.setSelectedIndex(0);
  }

  private void retrieveListInfo()
          throws CoeusException
  {
        Hashtable htData  = getSponsorTypeData();
        if (htData != null)
        {
            CoeusVector cvSponsorType = new CoeusVector();
            try
            {
                cvSponsorType = (CoeusVector)htData.get(new Integer(0));
                hasRight = ((Boolean)htData.get(new Integer(1))).booleanValue();
                setListData(cvSponsorType);
            }
            catch (Exception e)
            {
                e.printStackTrace(System.out);
            }


        }
      
  }

      private Hashtable getSponsorTypeData()
            throws CoeusException
    {
        RequesterBean requesterBean = new RequesterBean();

        requesterBean.setFunctionType('D');//"GET_SPONSOR_TYPE_DESCRIPTIONS

        Hashtable data=null;

        AppletServletCommunicator appletServletCommunicator = new AppletServletCommunicator();

        String strurl = CoeusGuiConstants.CONNECTION_URL + REPORTING_MAINTENANCE_SERVLET ;
                
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

    private void setListData(CoeusVector data1)
    {
        if (data1 != null && data1.size() > 0)
        {
            for (int inum =0 ; inum < data1.size(); inum++)
                grantsBySponsorForm.comboSponsorTypeList.addItem((String)data1.elementAt(inum));
        }        
    }
}


 
