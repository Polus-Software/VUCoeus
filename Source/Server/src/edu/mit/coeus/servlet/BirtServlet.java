/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.mit.coeus.servlet;

import edu.mit.coeus.brokers.RequesterBean;
import edu.mit.coeus.brokers.ResponderBean;
import edu.mit.coeus.user.bean.UserMaintDataTxnBean;
import edu.mit.coeus.utils.TypeConstants;
import edu.mit.coeus.utils.UtilFactory;
import edu.mit.coeus.utils.birt.BirtHelper;
import edu.mit.coeus.utils.birt.bean.BirtConstants;
import edu.mit.coeus.utils.birt.bean.BirtReportBean;
import edu.mit.coeus.utils.birt.bean.BirtReportTxnBean;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author sharathk
 */
public class BirtServlet extends HttpServlet {

    /** 
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code> methods.
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        //response.setContentType("text/html;charset=UTF-8");
        //PrintWriter out = response.getWriter();
        // the request object from applet
        RequesterBean requester = null;
        // the response object to applet
        ResponderBean responder = new ResponderBean();
        // open object input/output streams
        ObjectInputStream inputFromApplet = null;
        ObjectOutputStream outputToApplet = null;
        try {
            inputFromApplet = new ObjectInputStream(request.getInputStream());
            requester = (RequesterBean) inputFromApplet.readObject();
            String user = requester.getUserName();
            char functionType = requester.getFunctionType();
            BirtReportTxnBean birtReportTxnBean = new BirtReportTxnBean(user);
            if (functionType == BirtConstants.GET_REPORT_PARAMS) {
                Map requestObject = (Map)requester.getDataObject();
                String strReportId = (String)requestObject.get(BirtConstants.REPORT_ID);
                int reportId = Integer.parseInt(strReportId);
                String baseWindow = (String) requestObject.get(BirtConstants.BASE_WINDOW);
                BirtReportBean bean = birtReportTxnBean.getReport(reportId);
                //Check for rights first
                String unit = (String) requestObject.get(BirtConstants.RIGHTS);
                boolean hasRight = false;
                if(bean.getRight() == null) {
                    //No Right Check Required == Can Run Reports ~ has Right
                    hasRight = true;
                } else {
                    UserMaintDataTxnBean userMaintTxnBean = new UserMaintDataTxnBean();
                    if (unit == null) {
                        hasRight = userMaintTxnBean.getUserHasRightInAnyUnit(user, bean.getRight());
                    } else {
                        hasRight = userMaintTxnBean.getUserHasRight(user, bean.getRight(), unit);
                    }
                }
                if (hasRight) {
                    BirtHelper birtHelper = new BirtHelper();
                    ByteArrayOutputStream baos = birtReportTxnBean.getReportTemplate(reportId);
                    Map valueCodes = birtReportTxnBean.getParameterValueCodes(baseWindow);
                    ByteArrayInputStream inputStream = new ByteArrayInputStream(baos.toByteArray());
                    List list = birtHelper.getParameters(inputStream);
                    list = birtReportTxnBean.mapValueCodes(list, valueCodes);
                    responder.setDataObject(list);
                }else {
                    responder.setDataObject(new Boolean(hasRight));
                }
            } else if (functionType == BirtConstants.HAS_MAINTAIN_RIGHT) {
                UserMaintDataTxnBean userMaintTxnBean = new UserMaintDataTxnBean();
                boolean hasRight = userMaintTxnBean.getUserHasOSPRight(user, "MAINTAIN_CUSTOM_REPORTS");
                responder.setDataObject(new Boolean(hasRight));
            } else if (functionType == BirtConstants.GET_ALL_REPORTS) {
                List list = birtReportTxnBean.getAllReports();
                responder.setDataObject(list);
            } else if (functionType == BirtConstants.GET_REPORTS_FOR_MODULE) {
                Map requestObjects = (Map)requester.getDataObject();
                Integer reportType = (Integer)requestObjects.get(BirtConstants.REPORT_TYPE);
                String baseWindow = (String)requestObjects.get(BirtConstants.BASE_WINDOW);
                List lstReportTypes = null;
                Map valueCodes = null;
                if(reportType != null) {
                    lstReportTypes = birtReportTxnBean.getReportForType(reportType.intValue());
                    valueCodes = birtReportTxnBean.getMenuParameterValueCodes(baseWindow);
                }
                Map responseObjects = new HashMap();
                responseObjects.put(BirtConstants.REPORT_TYPES, lstReportTypes);
                responseObjects.put(BirtConstants.PARAMETERS, valueCodes);
                responder.setDataObject(responseObjects);
            } else if (functionType == BirtConstants.GET_REPORT_TYPES) {
                List lstReportTypes = birtReportTxnBean.getReportTypesForBaseWindow();
                responder.setDataObject(lstReportTypes);
            } else if (functionType == BirtConstants.NEW_REPORT) {
                //UserMaintDataTxnBean bean = new UserMaintDataTxnBean();
                //Vector vec = bean.getAllRights();
                List lstReportRights = birtReportTxnBean.getReportRights();
                List lstReportTypes = birtReportTxnBean.getReportTypes();
                Map map = new HashMap();
                map.put(BirtConstants.RIGHTS, lstReportRights);
                map.put(BirtConstants.REPORT_TYPES, lstReportTypes);
                responder.setDataObject(map);
            } else if (functionType == BirtConstants.SAVE_REPORT_DETAILS) {
                BirtReportBean birtReportBean = (BirtReportBean) requester.getDataObject();
                if (birtReportBean.getAcType().equalsIgnoreCase(TypeConstants.INSERT_RECORD)) {
                    birtReportBean = birtReportTxnBean.insertReport(birtReportBean);
                } else if (birtReportBean.getAcType().equalsIgnoreCase(TypeConstants.UPDATE_RECORD)) {
                    birtReportBean = birtReportTxnBean.updateReport(birtReportBean);
                }
                responder.setDataObject(birtReportBean);
            } else if (functionType == BirtConstants.SAVE_REPORT_TEMPLATE) {
                BirtReportBean birtReportBean = (BirtReportBean) requester.getDataObject();
                birtReportBean = birtReportTxnBean.updateReportTemplate(birtReportBean);
                responder.setDataObject(birtReportBean);
            } else if (functionType == BirtConstants.EDIT_REPORT) {
                Integer reportId = (Integer) requester.getDataObject();

                //UserMaintDataTxnBean userTxnBean = new UserMaintDataTxnBean();
                //Vector vec = userTxnBean.getAllRights();
                List lstReportRights = birtReportTxnBean.getReportRights();
                List lstReportTypes = birtReportTxnBean.getReportTypes();

                BirtReportBean bean = birtReportTxnBean.getReport(reportId.intValue());

                Map map = new HashMap();
                map.put(BirtConstants.RIGHTS, lstReportRights);
                map.put(BirtConstants.REPORT_TYPES, lstReportTypes);
                map.put(BirtConstants.REPORT_BEAN, bean);
                responder.setDataObject(map);
            } else if (functionType == BirtConstants.DELETE_REPORT) {
                BirtReportBean birtReportBean = (BirtReportBean) requester.getDataObject();
                birtReportTxnBean.deleteReport(birtReportBean);
            }else if (functionType == BirtConstants.DOWNLOAD_REPORT){
                Integer reportId = (Integer) requester.getDataObject();
                ByteArrayOutputStream baos = birtReportTxnBean.getReportTemplate(reportId.intValue());
                responder.setDataObject(baos.toByteArray());
            }
            responder.setResponseStatus(true);
        } catch (Exception exception) {
            UtilFactory.log(exception.getMessage(), exception, "BirtServlet", "processRequest");
            responder.setResponseStatus(false);
            responder.setMessage(exception.getMessage());
            responder.setException(exception);
        } catch (Throwable throwable) {
            Exception ex = new Exception(throwable);
            responder.setException(ex);
            responder.setResponseStatus(false);
            responder.setMessage(ex.getMessage());
            UtilFactory.log(throwable.getMessage(), throwable, "BirtServlet", "doPost");
        //Case 3193 - END
        } finally {
            try {
                // send the object to applet
                outputToApplet = new ObjectOutputStream(response.getOutputStream());
                outputToApplet.writeObject(responder);
                // close the streams
                if (inputFromApplet != null) {
                    inputFromApplet.close();
                }
                if (outputToApplet != null) {
                    outputToApplet.flush();
                    outputToApplet.close();
                }
            } catch (IOException ioe) {
                UtilFactory.log(ioe.getMessage(), ioe, "BirtServlet", "processRequest");
            }
        }
    }//End process request

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /** 
     * Handles the HTTP <code>GET</code> method.
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /** 
     * Handles the HTTP <code>POST</code> method.
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /** 
     * Returns a short description of the servlet.
     * @return a String containing servlet description
     */
    public String getServletInfo() {
        return "Birt Servlet";
    }// </editor-fold>
}
