/** Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 */

package edu.mit.coeus.award.controller;

import edu.mit.coeus.gui.event.Controller;
import edu.mit.coeus.gui.CoeusMessageResources;
import edu.mit.coeus.award.bean.AwardBaseBean;
import edu.mit.coeus.utils.query.QueryEngine;
import edu.mit.coeus.utils.CoeusVector;
import edu.mit.coeus.award.bean.AwardReportReqBean;
import edu.mit.coeus.exception.CoeusException;
import edu.mit.coeus.brokers.*;
import edu.mit.coeus.utils.*;
import edu.mit.coeus.irb.bean.PersonInfoFormBean;

/*
 * RepRequirementController.java
 * Created on July 13, 2004, 10:31 AM
 * @author  ajaygm
 */


/** Super class of all Controllers for Reporting Reqirements.
 * Contains utility methods for Award Module (Reporting Reqirements).
 */
public abstract class RepRequirementController extends Controller{
    
    public String queryKey;
    public AwardBaseBean awardBaseBean;
    private QueryEngine queryEngine;
    private CoeusMessageResources coeusMessageResources;
    
    public final static String EMPTY = "";
    
    private static final String SERVLET = "/AwardReportReqMaintenanceServlet";
    public static final String COULD_NOT_CONTACT_SERVER = "Could not contact server";
    private static final String UNIQUE_PER_VIOLATION_MSG = "repRequirements_exceptionCode.1055";
    private static final String INVALID_PERSON_MSG="repRequirements_exceptionCode.1053";
    private final char GET_PERSON_DETAILS = 'G';
    private String personId = "";
    private String personName = "";
    
    /** Creates a new instance of RepRequirementController.
     * creates the Key for the query engine from the award base bean.
     * @param awardBaseBean award Base Bean.
     */    
    public RepRequirementController(AwardBaseBean awardBaseBean) {
        if(awardBaseBean != null && awardBaseBean.getMitAwardNumber() != null) {
            this.awardBaseBean = awardBaseBean;
            queryKey = awardBaseBean.getMitAwardNumber();
        }
        queryEngine = QueryEngine.getInstance();
        coeusMessageResources = CoeusMessageResources.getInstance();
    }
    /** Creates a new instance of RepRequirementController.
     *  It is used for reporting requirements called outside award
     */
    public RepRequirementController() {
        
    }
    /** Getter for property queryKey.
     * @return Value of property queryKey.
     *
     */
    public String getQueryKey() {
        return queryKey;
    }
        
    public void prepareQueryKey(AwardBaseBean awardBaseBean) {
        queryKey = awardBaseBean.getMitAwardNumber();
    } 
    
    public int getMaxReportNo(){
        int maxRepNo = 0;
        try{
            CoeusVector cvRepReqBean = (CoeusVector)queryEngine.getDetails(queryKey,AwardReportReqBean.class);
            for(int index = 0 ; index < cvRepReqBean.size() ; index++){
                AwardReportReqBean awardReportReqBean= (AwardReportReqBean) cvRepReqBean.get(index);
                if(awardReportReqBean.getReportNumber() > maxRepNo){
                    maxRepNo = awardReportReqBean.getReportNumber();
                }
            }
            return maxRepNo;
        }catch (CoeusException coeusException){
            coeusException.printStackTrace();
        }
        return maxRepNo;
    }
    /**
     * This method authenticates the person name by making a server call.
     * If there are more than one person with that name it displays a message to indicate thaT.
     * The focus flag is set when the user searchs for the person using the search window and the validation for TOO_MANY
     * full names is not done in the case of the person search using the search window..(Bug 1273)
     */
    public boolean authenticatePerson(String perName ,boolean focusFlag) {
        personId = "";
        RequesterBean requesterBean = new RequesterBean();
        requesterBean.setFunctionType(GET_PERSON_DETAILS);
        requesterBean.setDataObject(perName);
        AppletServletCommunicator appletServletCommunicator = new AppletServletCommunicator(CoeusGuiConstants.CONNECTION_URL + SERVLET, requesterBean);
        appletServletCommunicator.setRequest(requesterBean);
        appletServletCommunicator.send();
        ResponderBean responderBean = appletServletCommunicator.getResponse();
        if(responderBean == null) {
            //Could not contact server.
            CoeusOptionPane.showErrorDialog(COULD_NOT_CONTACT_SERVER);
            return false;
        }
        if(responderBean.isSuccessfulResponse()) {
            PersonInfoFormBean personInfoFormBean = (PersonInfoFormBean)responderBean.getDataObject();
            if(personInfoFormBean.getPersonID()==null){
                return false;
            }
            if(!focusFlag){
                if(personInfoFormBean.getPersonID().equals("TOO_MANY")) {
                    CoeusOptionPane.showInfoDialog(perName+" "+ coeusMessageResources.parseMessageKey(UNIQUE_PER_VIOLATION_MSG));
                    return false;
                }
            }
            personId = personInfoFormBean.getPersonID();
            personName = personInfoFormBean.getFullName();
            return true;
        } else {
            CoeusOptionPane.showInfoDialog(coeusMessageResources.parseMessageKey(INVALID_PERSON_MSG));
            return false;
        }
    }
    
    /**
     * Getter for property personId.
     * @return Value of property personId.
     */
    public java.lang.String getAuthenticatedPersonId() {
        return personId;
    }
    
        /**
     * Getter for property personId.
     * @return Value of property personName.
     */
    public java.lang.String getAuthenticatedPersonName() {
        return personName;
    }
    
}//End of class.
