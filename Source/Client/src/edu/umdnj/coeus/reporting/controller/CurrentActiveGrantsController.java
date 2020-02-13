/**
 * $Header: /home/cvs/cvsroot/coeus/Source/edu/umdnj/coeus/reporting/controller/Attic/CurrentActiveGrantsController.java,v 1.1.2.4 2007/08/16 18:45:53 cvs Exp $
 * $Log: CurrentActiveGrantsController.java,v $
 * Revision 1.1.2.4  2007/08/16 18:45:53  cvs
 * Add support for sorting by PI and Department
 *
 * Revision 1.1.2.3  2007/07/25 18:39:25  cvs
 * Add Headers and Snippet integrity in PDF
 *
 * Revision 1.1.2.2  2007/06/18 17:15:48  cvs
 * Add check for Enforce Project End Date
 *
 * Revision 1.1.2.1  2007/03/29 16:19:40  cvs
 * Add support for Current Active Grants Report
 *
 *
 */
/*
 * @(#)CurrentActiveGrantsController.java 
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
import edu.umdnj.coeus.reporting.gui.CurrentActiveGrantsForm;
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

public class CurrentActiveGrantsController extends Controller 
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
    /** CurrentActiveGrantsForm Form instance whihc form is UI */
    private CurrentActiveGrantsForm currentActiveGrantsForm = null;

    private static final String REPORTING_MAINTENANCE_SERVLET = "/ReportingMaintenanceServlet";
    private String UnitNumber;
    private String UnitName;

    public CurrentActiveGrantsController() 
    {
        super();
        this.parent= CoeusGuiConstants.getMDIForm();
        this.modal =  true;
        initialiseController();
    }

    /** Creates a new instance of CurrentActiveGrantsController
     * with parameters
     * @param parent Component parent form
     * @param modal booelan if <CODE>true<CODE> modal window
     * @param budgetInfoBean BudgetInfoBean instance - unimplemented
     */    
    public CurrentActiveGrantsController(
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
        if(currentActiveGrantsForm == null) 
        {
            currentActiveGrantsForm =  new CurrentActiveGrantsForm();   //(Component) parent,modal);
        }
        queryEngine = QueryEngine.getInstance();
        coeusMessageResources = CoeusMessageResources.getInstance();
        currentActiveGrantsForm.descriptionLabel.setText(UnitName);
        currentActiveGrantsForm.descriptionLabel.setFont(new Font("SansSerif",Font.BOLD,14));
    	currentActiveGrantsForm.repeatHeaderLabel.setVisible(false);
    	currentActiveGrantsForm.checkRepeatHeader.setVisible(false);
    	currentActiveGrantsForm.reportSortLabel.setVisible(false);
    	currentActiveGrantsForm.radioBtnAwardNo.setVisible(false);
    	currentActiveGrantsForm.radioBtnPI.setVisible(false);
    	currentActiveGrantsForm.radioBtnDept.setVisible(false);
    	currentActiveGrantsForm.radioBtnAwardNo.setSelected(true);
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
        ((CurrentActiveGrantsForm) getControlledUI()).setVisible(true);
    }

    /** method to set Visible the form on close */    
    public void close() 
    {
        ((CurrentActiveGrantsForm) getControlledUI()).setVisible(false);
    }

    /** Method to return an instance of UI form
     * @return Component instaince of BudgetJustificationForm
     */
    public java.awt.Component getControlledUI() 
    {
        return currentActiveGrantsForm;
    }

    /** To Register  all the Actionlistner for the Components of UI */
    public void registerComponents() 
    {
        currentActiveGrantsForm.jbnRunButton.addActionListener(this);
        currentActiveGrantsForm.jbnCloseButton.addActionListener(this);
        currentActiveGrantsForm.comboReportFormatsList.addItemListener(this);
    }

  public void actionPerformed(ActionEvent ev)
  {
     Object obj = ev.getSource();
     if (obj == currentActiveGrantsForm.jbnRunButton)
     {
        RunButtonAction();
     }
     else
     if (obj == currentActiveGrantsForm.jbnCloseButton)
     {
       this.close();
     }
  }

  private void RunButtonAction()
  {
    int fnum = currentActiveGrantsForm.comboReportFormatsList.getSelectedIndex();
    if (fnum == -1)
    {
       log("Please select a format.");
    }
    
    String strFormat = currentActiveGrantsForm.comboFormatTypes[fnum];
    boolean bNIH = currentActiveGrantsForm.checkNIH.isSelected();
    boolean bEnforceDate = currentActiveGrantsForm.checkProjectEndDate.isSelected();
    boolean bRepeatHeader = currentActiveGrantsForm.checkRepeatHeader.isSelected();

    generateReport(strFormat,bNIH,bEnforceDate,bRepeatHeader);
  }

  public void itemStateChanged(ItemEvent ev)
  {
     Object obj = ev.getSource();
     if (obj == currentActiveGrantsForm.comboReportFormatsList)
     {
	itemStateChanged_comboReportFormatsList();
     }
  }

  private void itemStateChanged_comboReportFormatsList()
  {
     int fnum = currentActiveGrantsForm.comboReportFormatsList.getSelectedIndex();
     if (fnum > -1)
     {
        String strFormat = currentActiveGrantsForm.comboFormatTypes[fnum];
        if (strFormat.compareTo("PDF")==0)
	{
    		currentActiveGrantsForm.repeatHeaderLabel.setVisible(true);
    		currentActiveGrantsForm.checkRepeatHeader.setVisible(true);
    		currentActiveGrantsForm.reportSortLabel.setVisible(true);
    		currentActiveGrantsForm.radioBtnAwardNo.setVisible(true);
    		currentActiveGrantsForm.radioBtnPI.setVisible(true);
    		currentActiveGrantsForm.radioBtnDept.setVisible(true);
	}
	else
	{
    		currentActiveGrantsForm.repeatHeaderLabel.setVisible(false);
    		currentActiveGrantsForm.checkRepeatHeader.setVisible(false);
    		currentActiveGrantsForm.reportSortLabel.setVisible(false);
    		currentActiveGrantsForm.radioBtnAwardNo.setVisible(false);
    		currentActiveGrantsForm.radioBtnPI.setVisible(false);
    		currentActiveGrantsForm.radioBtnDept.setVisible(false);
	}
     }
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
      return currentActiveGrantsForm;
  }

    /**
     * display alert message
     *
     * @param mesg the message to be displayed
     */
    private void log(String mesg) {
        CoeusOptionPane.showInfoDialog(mesg);
    }

    private boolean generateReport(String strFormat, 
            boolean bNIH, boolean bEnforceDate, boolean bRepeatHeader) 
    {

        AppletContext coeusContext = CoeusGuiConstants.getMDIForm().getCoeusAppletContext();
        StringBuffer strbuf = new StringBuffer(CoeusGuiConstants.CONNECTION_URL);
        strbuf.append("/GetCurrentActiveGrantsServlet?UnitName=");
        strbuf.append(UnitNumber);
        strbuf.append("&Format=");
        strbuf.append(strFormat);
        if (bNIH == true)
            strbuf.append("&NIH=true");
        if (bEnforceDate == true)
            strbuf.append("&EnforceEndDate=true");
        if (bRepeatHeader == true)
            strbuf.append("&RepeatHeader=true");
        String command = currentActiveGrantsForm.group.getSelection().getActionCommand();
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


 
