/**
 * $Header: /home/cvs/cvsroot/coeus/Source/edu/umdnj/coeus/reporting/controller/Attic/CannedReportsController.java,v 1.1.2.11 2007/01/17 15:28:07 cvs Exp $
 * $Log: CannedReportsController.java,v $
 * Revision 1.1.2.11  2007/01/17 15:28:07  cvs
 * Add CSV Support
 *
 * Revision 1.1.2.10  2007/01/04 20:01:40  cvs
 * Add support for Sponsor Type Graph by School by Sponsor Type GUI
 *
 * Revision 1.1.2.9  2006/12/28 20:46:40  cvs
 *  Add support for grants by sponsor type graph GUI
 *
 * Revision 1.1.2.8  2006/12/08 19:56:56  cvs
 * Add support for JFreeChart charting engine
 *
 * Revision 1.1.2.7  2006/12/07 15:25:36  cvs
 * Remove third options for Sample Reports and Sample Graphs
 *
 * Revision 1.1.2.6  2006/12/01 14:14:32  cvs
 * Add XLS support for formatted output to Canned Reports dialog
 *
 * Revision 1.1.2.5  2006/11/30 15:01:58  cvs
 * Remove 3D type charts from list of charts
 *
 * Revision 1.1.2.4  2006/11/20 20:51:14  cvs
 * Add TDG engine support for demo purposes
 *
 * Revision 1.1.2.3  2006/11/16 18:53:47  cvs
 * Add basic XML and HTML data retrieval and browser output mechanism
 *
 * Revision 1.1.2.2  2006/11/15 20:29:18  cvs
 * Added itemListener logic to listen for events with comboReportsList and comboGraphsList.
 *
 * Revision 1.1.2.1  2006/11/10 16:11:54  cvs
 * Controller file for Canned Reports Form
 *
 */
/*
 * @(#)CannedReportsController.java 
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
 * Description: Canned Reports controller for UMDNJ COEUS 411.
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
import javax.swing.JOptionPane;
import javax.swing.AbstractAction;
import java.util.Hashtable;

import edu.mit.coeus.utils.TypeConstants;
import edu.mit.coeus.utils.CoeusOptionPane;
import edu.mit.coeus.utils.CoeusGuiConstants;
import edu.umdnj.coeus.reporting.gui.CannedReportsForm;
import edu.mit.coeus.utils.query.QueryEngine;
import edu.mit.coeus.gui.CoeusDlgWindow;
import edu.mit.coeus.gui.CoeusMessageResources;
import edu.mit.coeus.gui.CoeusFontFactory;
import edu.mit.coeus.gui.event.Controller;

import edu.mit.coeus.exception.CoeusException;

public class CannedReportsController extends Controller 
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
    /** CannedReportsForm Form instance whihc form is UI */
    private CannedReportsForm cannedReportsForm = null;
    private String UnitNumber;
    private String UnitName;

    public CannedReportsController() 
    {
        super();
        this.parent= CoeusGuiConstants.getMDIForm();
        this.modal =  true;
        initialiseController();
    }
    /** Creates a new instance of CannedReportsController
     * with parameters
     * @param parent Component parent form
     * @param modal booelan if <CODE>true<CODE> modal window
     * @param budgetInfoBean BudgetInfoBean instance - unimplemented
     */    
    public CannedReportsController(
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
        if(cannedReportsForm == null) 
        {
            cannedReportsForm =  new CannedReportsForm();   //(Component) parent,modal);
        }
        queryEngine = QueryEngine.getInstance();
        coeusMessageResources = CoeusMessageResources.getInstance();
        registerComponents();
    }

    /** displey Method to make form visible */
    public void display() 
    {
        ((CannedReportsForm) getControlledUI()).setVisible(true);
    }
    /** method to set Visible the form on close */    
    public void close() 
    {
        ((CannedReportsForm) getControlledUI()).setVisible(false);
    }
    /** Method to return an instance of UI form
     * @return Component instaince of BudgetJustificationForm
     */
    public java.awt.Component getControlledUI() 
    {
        return cannedReportsForm;
    }
    /** To Register  all the Actionlistner for the Components of UI */
    public void registerComponents() 
    {
        cannedReportsForm.jbnRunButton.addActionListener(this);
        cannedReportsForm.jbnCloseButton.addActionListener(this);
        cannedReportsForm.radioReport.addActionListener(this);
        cannedReportsForm.radioGraph.addActionListener(this);
        cannedReportsForm.radioReport.setSelected(true);
        EnablePanelItems(true);
    }
  private void EnablePanelItems(boolean bReport)
  {
     cannedReportsForm.comboReportsList.setEnabled(bReport);
     cannedReportsForm.comboReportsList.setSelectedIndex(0);
     cannedReportsForm.reportFormatsLabel.setEnabled(bReport);
     cannedReportsForm.comboReportFormatsList.setEnabled(bReport);
     cannedReportsForm.comboReportFormatsList.setSelectedIndex(0);
     cannedReportsForm.comboGraphsList.setEnabled(!bReport);
     cannedReportsForm.comboGraphsList.setSelectedIndex(0);
     cannedReportsForm.graphFormatsLabel.setEnabled(!bReport);
     cannedReportsForm.comboGraphFormatsList.setEnabled(!bReport);
     cannedReportsForm.comboGraphFormatsList.setSelectedIndex(0);
     cannedReportsForm.comboGraphTypesList.setEnabled(!bReport);
     cannedReportsForm.comboGraphTypesList.setSelectedIndex(0);

  }
  public void actionPerformed(ActionEvent ev)
  {
     Object obj = ev.getSource();
     if (obj == cannedReportsForm.jbnRunButton)
     {
        RunButtonAction();
     }
     else
     if (obj == cannedReportsForm.jbnCloseButton)
     {
       close();
     }
     else
     if (obj == cannedReportsForm.radioReport)
     {
        EnablePanelItems(true);
     }
     else
     if (obj == cannedReportsForm.radioGraph)
     {
        EnablePanelItems(false);
     }
  }

  private void RunButtonAction()
  {
     if (cannedReportsForm.radioReport.isSelected() == true)
     {
        int inum = cannedReportsForm.comboReportsList.getSelectedIndex();
        int fnum = cannedReportsForm.comboReportFormatsList.getSelectedIndex();
        String strFormat = "";
        String strType = "";
        switch(inum)
        {
           case 0: // Report One
                   strType = "ReportOne";
                   break;
           case 1: // Report Two
                   strType = "ReportTwo";
                   break;
           default:
                   strType = "ReportOne";
                   break;
        }
        switch(fnum)
        {
           case 0: // HTML
                   strFormat = "HTML";
                   break;
           case 1: // PDF
                   strFormat = "PDF";
                   break;
           case 2: // XML
                   strFormat = "XML";
                   break;
           case 3: // XLS
                   strFormat = "XLS";
                   break;
           case 4: // CSV
                   strFormat = "CSV";
                   break;
           default:
                   strFormat = "XML";
                   break;
        }
	generateReport(strType,strFormat);
     }
     else
     {
        int inum = cannedReportsForm.comboGraphsList.getSelectedIndex();
        int fnum = cannedReportsForm.comboGraphFormatsList.getSelectedIndex();
        int cnum = cannedReportsForm.comboGraphTypesList.getSelectedIndex();
        String strFormat = "";
        String strType = "";
	String graphtype = "";
        switch(inum)
        {
           case 0: // Graph One
                   strType = "GraphOne";
                   break;
           case 1: // Graph Two
                   strType = "GraphTwo";
                   break;
           case 2: // Graph Three
                   strType = "GraphThree";
                   break;
           case 3: // Graph Three
                   strType = "GraphFour";
                   break;
            
           default:
                   strType = "GraphOne";
                   break;
        }
        switch(fnum)
        {
           case 0: // PNG
                   strFormat = "PNG";
                   break;
           case 1: // PDF
                   strFormat = "JPEG";
                   break;
           default:
                   strFormat = "PNG";
                   break;
        }
        switch(cnum)
        {
           case 0: // BAR
                   graphtype = "BAR";
                   break;
           case 1: // LINE
                   graphtype = "LINE";
                   break;
           case 2: // AREA
                   graphtype = "AREA";
                   break;
           case 3: // PIE
                   graphtype = "PIE";
                   break;
        }
	generateGraph(strType,strFormat,graphtype);
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
      return cannedReportsForm;
  }

    /**
     * display alert message
     *
     * @param mesg the message to be displayed
     */
    private void log(String mesg) {
        CoeusOptionPane.showInfoDialog(mesg);
    }

    private boolean generateReport(String strType, String strFormat) 
    {

        AppletContext coeusContext = CoeusGuiConstants.getMDIForm().getCoeusAppletContext();
        StringBuffer strbuf = new StringBuffer(CoeusGuiConstants.CONNECTION_URL);
        strbuf.append("/ReportingServlet?SQLRequest=");
        strbuf.append(strType);
        strbuf.append("&Format=");
        strbuf.append(strFormat);

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

    private boolean generateGraph(String strType, String strFormat, String graphtype) 
    {

        AppletContext coeusContext = CoeusGuiConstants.getMDIForm().getCoeusAppletContext();
        StringBuffer strbuf = new StringBuffer(CoeusGuiConstants.CONNECTION_URL);
        strbuf.append("/GraphingServlet?SQLRequest=");
        strbuf.append(strType);
        strbuf.append("&Format=");
        strbuf.append(strFormat);
        strbuf.append("&GraphType=");
        strbuf.append(graphtype);

        try{
            URL templateURL = new URL(strbuf.toString());

            if(coeusContext != null){
                coeusContext.showDocument( templateURL, "_blank" );
            }else{
                javax.jnlp.BasicService bs = (javax.jnlp.BasicService)javax.jnlp.ServiceManager.lookup("javax.jnlp.BasicService");
                bs.showDocument(templateURL);
            }
            return true;
        } catch (Exception ex) {
            ex.printStackTrace();
            CoeusOptionPane.showErrorDialog(""+ex.getMessage());
            return false;
        }
    }

}


 
