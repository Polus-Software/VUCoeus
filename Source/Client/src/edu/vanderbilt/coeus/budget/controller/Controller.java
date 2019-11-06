/*
 * Controller.java
 */

package edu.vanderbilt.coeus.budget.controller;


import edu.mit.coeus.exception.CoeusClientException;
import edu.mit.coeus.brokers.*;
import edu.mit.coeus.utils.*;

public class Controller {
    
    private final char GET_ACTIVE_PERIOD_TYPE ='o';
    private final String connectURL = "/BudgetMaintenanceServlet";
    private static final String COULD_NOT_CONTACT_SERVER = "Could not contact server";
    
    /** 
     * creates a new instance of Controller
     */
    public Controller() {
    }
    
    /**
     * Fetches all the active period types from the database
     * @return String [][] arrPeriodType
     * @throws Exception CoeusClientException
     */
    public String [][] fetchActivePeriodTypes() throws CoeusClientException {
        String [][] arrPeriodType = null;
        CoeusVector cvData = new CoeusVector();
        RequesterBean requesterBean  = new RequesterBean();
        requesterBean.setFunctionType(GET_ACTIVE_PERIOD_TYPE);

        String connectTo = CoeusGuiConstants.CONNECTION_URL + connectURL;
        AppletServletCommunicator comm = new AppletServletCommunicator(connectTo, requesterBean );
        comm.send();
        ResponderBean responderBean = comm.getResponse();
         if ( responderBean != null ){
            if(responderBean.isSuccessfulResponse()) {
            	cvData = (CoeusVector) responderBean.getDataObject();
                if (cvData  != null && cvData.size() > 0) {
                	String[] period = new String[2];
                	arrPeriodType = new String[cvData.size()][2];
                	for (int c=0; c < cvData.size(); c++) {
                		period = (String[]) cvData.get(c);
                		arrPeriodType[c] = period;
                	}
                }
            }else {
                throw new CoeusClientException(responderBean.getMessage(), CoeusClientException.ERROR_MESSAGE);
            }
         }else{
             throw new CoeusClientException(COULD_NOT_CONTACT_SERVER);
         }
        return arrPeriodType;
    }
}
