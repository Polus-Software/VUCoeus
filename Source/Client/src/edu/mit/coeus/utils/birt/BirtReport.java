/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.mit.coeus.utils.birt;

import edu.mit.coeus.brokers.RequesterBean;
import edu.mit.coeus.brokers.ResponderBean;
import edu.mit.coeus.gui.CoeusInternalFrame;
import edu.mit.coeus.gui.CoeusMessageResources;
import edu.mit.coeus.gui.event.BlockingGlassPane;
import edu.mit.coeus.utils.AppletServletCommunicator;
import edu.mit.coeus.utils.CoeusGuiConstants;
import edu.mit.coeus.utils.CoeusOptionPane;
import edu.mit.coeus.utils.birt.bean.BirtConstants;
import edu.mit.coeus.utils.birt.bean.BirtParameterBean;
import edu.mit.coeus.utils.birt.bean.BirtReportBean;
import java.awt.Cursor;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Helper class for thr client side
 * @author sharathk
 */
public class BirtReport {

    private String connectTo = CoeusGuiConstants.CONNECTION_URL + "/BirtServlet";

    /**
     * fetches the report param details, creates instance of BirtReportGUIBuilder and displays it.
     * @param reportId
     * contact server and display Report.
     * if the report takes parameters display ReportGui
     */
    public void displayReport(String reportId, Map paramValues, CoeusInternalFrame cif, BirtReportBean bean) {
        BlockingGlassPane blockingGlassPane = new BlockingGlassPane();
        try {
            CoeusGuiConstants.getMDIForm().setCursor(new Cursor(Cursor.WAIT_CURSOR));
            CoeusGuiConstants.getMDIForm().setGlassPane(blockingGlassPane);
            blockingGlassPane.block(true);
            String unit = null;
            CodeRunner codeRunner = new CodeRunner();
            if (bean != null && bean.getRight() != null) {
                if (paramValues != null) {
                    Iterator iterator = paramValues.keySet().iterator();
                    while (iterator.hasNext()) {
                        codeRunner.analyzeAndRun((String) paramValues.get(iterator.next()), cif);
                        unit = codeRunner.getValue();
                        if(unit == null || unit.trim().length() == 0) {
                            //Throw Error
                            CoeusOptionPane.showErrorDialog(CoeusMessageResources.getInstance().parseMessageKey("reportMaintenanceCode.1011"));
                            return;
                        }
                    }
                }
            }
            RequesterBean request = new RequesterBean();
            ResponderBean response = null;
            Map map = new HashMap();
            map.put(BirtConstants.REPORT_ID, reportId);
            map.put(BirtConstants.RIGHTS, unit);
            map.put(BirtConstants.BASE_WINDOW, cif == null ? null : cif.getClass().getName());
            request.setDataObject(map);
            request.setFunctionType(BirtConstants.GET_REPORT_PARAMS);
            AppletServletCommunicator comm = new AppletServletCommunicator(connectTo, request);
            comm.send();
            response = comm.getResponse();
            if (response != null && response.hasResponse()) {
                //Okay
                if (response.getDataObject() instanceof List) {
                    List birtParameterList = (List) response.getDataObject();
                    BirtParameterBean parameterBean;
                    for(int index=0;index<birtParameterList.size();index++){
                        parameterBean = (BirtParameterBean) birtParameterList.get(index);
                        if(parameterBean.getParameterValueCode() != null) {
                            //CodeRunner codeRunner = new CodeRunner();
                            codeRunner.analyzeAndRun(parameterBean.getParameterValueCode(), cif);
                            if(codeRunner.getValue() != null){
                                parameterBean.setDefaultValue(codeRunner.getValue());
                            }
                        }
                    }
                    BirtReportGUIBuilder birtReportGUIBuilder = new BirtReportGUIBuilder();
                    birtReportGUIBuilder.displayReportGui(reportId, birtParameterList);
                } else if (response.getDataObject() instanceof Boolean) {
                    Boolean boolHasRight = (Boolean) response.getDataObject();
                    if (!boolHasRight.booleanValue()) {
                        CoeusOptionPane.showErrorDialog(CoeusMessageResources.getInstance().parseMessageKey("reportMaintenanceCode.1006"));
                    } else {
                        //This code will not be executed under normal circumstances
                        CoeusOptionPane.showErrorDialog(CoeusMessageResources.getInstance().parseMessageKey("exceptionCode.unKnown"));
                    }
                }
            }
        } catch (Exception exception) {
            exception.printStackTrace();
            CoeusOptionPane.showErrorDialog(exception.getMessage() == null ? exception.getClass().toString() : exception.getMessage());
        }finally{
            blockingGlassPane.block(false);
        }
    }

    /**
     * retreives the menu for the Module(i.e. Award, Proposal etc) 
     * @param reportType module type
     * @return
     */
    public Map getMenu(Integer reportType, String baseWindow) {
        Map values = null;
        try {
            RequesterBean request = new RequesterBean();
            request.setFunctionType(BirtConstants.GET_REPORTS_FOR_MODULE);
            Map requestObject = new HashMap();
            requestObject.put(BirtConstants.REPORT_TYPE, reportType);
            requestObject.put(BirtConstants.BASE_WINDOW, baseWindow);
            request.setDataObject(requestObject);
            AppletServletCommunicator comm = new AppletServletCommunicator(connectTo, request);
            comm.send();
            ResponderBean response = comm.getResponse();
            if (response != null && response.hasResponse()) {
                values = (Map)response.getDataObject();
            }
        } catch (Exception exception) {
            CoeusOptionPane.showErrorDialog(exception.getMessage());
        }
        return values;
    }

    /**
     * retreives the Report types
     * @return
     */
    public List getReportType() {
        List lstReportTypes = null;
        try {
            RequesterBean request = new RequesterBean();
            request.setFunctionType(BirtConstants.GET_REPORT_TYPES);
            AppletServletCommunicator comm = new AppletServletCommunicator(connectTo, request);
            comm.send();
            ResponderBean response = comm.getResponse();
            if (response != null && response.hasResponse()) {
                lstReportTypes = (List) response.getDataObject();
            }
        } catch (Exception exception) {
            CoeusOptionPane.showErrorDialog(exception.getMessage());
        }
        return lstReportTypes;
    }

    public boolean hasMaintainRights() {
        boolean maintainRights = false;
        try {
            RequesterBean request = new RequesterBean();
            request.setFunctionType(BirtConstants.HAS_MAINTAIN_RIGHT);
            AppletServletCommunicator comm = new AppletServletCommunicator(connectTo, request);
            comm.send();
            ResponderBean response = comm.getResponse();
            if (response != null && response.hasResponse() && response.getDataObject() != null && response.getDataObject() instanceof Boolean) {
                maintainRights = ((Boolean) response.getDataObject()).booleanValue();
            }
        } catch (Exception exception) {
            CoeusOptionPane.showErrorDialog(exception.getMessage());
        }
        return maintainRights;
    }
}
