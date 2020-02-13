/** Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 */

/**
 * FundingProposalSearch.java
 * Created on June 14, 2004, 05:26 PM
 * @author  Bijosh
 */

package edu.mit.coeus.search.gui;

import java.awt.Component;
import java.awt.event.ActionListener;
import java.util.HashMap;
import javax.swing.JButton;

import edu.mit.coeus.utils.CoeusOptionPane;
import edu.mit.coeus.gui.CoeusMessageResources;
import edu.mit.coeus.brokers.RequesterBean;
import edu.mit.coeus.brokers.ResponderBean;
import edu.mit.coeus.utils.AppletServletCommunicator;
import edu.mit.coeus.utils.CoeusGuiConstants;
import edu.mit.coeus.utils.CoeusVector;
import edu.mit.coeus.award.bean.AwardDetailsBean;

public class FundingProposalSearch extends InstituteProposalSearch 
implements ActionListener{
    
    /** Holds CoeusMessageResources instance for reading message Properties.
     */
    private CoeusMessageResources coeusMessageResources = 
            CoeusMessageResources.getInstance();
    
    private static final String STATUS_CODE = "STATUS_CODE";
    private static final int PENDING = 1;
    private static final int FUNDED = 2;
    private static final int REVISION_REQ = 6;
    // JM 4-6-2012 added award pending type
    private static final int AWARD_PENDING = 8;
    private static final String SELECT_PENDING_PROPOSALS = 
            "instPropLog_exceptionCode.1404";
    private static final String DISCLOSURE_MSG1 = "The proposal you selected '";
    private static final String DISCLOSURE_MSG2 = 
    "' has financial interest \ndisclosures associated with it which are not complete." +
    "\n If you select this proposal, the award's status will be \nset to Hold. " +
    "Do you want to select this proposal?";
    private static final String AWARD_SERVLET = "/AwardMaintenanceServlet";
    private static final String connectTo = CoeusGuiConstants.CONNECTION_URL +
    AWARD_SERVLET;
    private static final char GET_DATA_FN_TYPE = 'U';
    private boolean awardStatusToBeMadeHold=false;
    /** Creates a new instance of FundingProposalSearch
     *  @param parent frame
     *  @param request identifier
     *  @param request type
     */
    public FundingProposalSearch(Component parentFrame, String searchReq,
    int reqType) throws Exception{
        super(parentFrame, searchReq, reqType);
    }
    
    /** This method triggers all actions based on the event occured
     * @param actionEvent takes the actionEvent
     */
    public void actionPerformed(java.awt.event.ActionEvent actionEvent) {
        try{
            Object source = actionEvent.getSource();
            if(source instanceof javax.swing.JButton){
                if(((JButton)source).getName().equals("SearchWindowOK")){
                    SearchResultWindow searchResWindow = super.getResultWindow();
                    if(searchResWindow==null || searchResWindow.getSelectedRow()==null
                        || searchResWindow.getSelectedRow().isEmpty()){
                        throw new Exception(coeusMessageResources.parseMessageKey(
                                            "search_exceptionCode.1119"));
                    }
                    HashMap resultData = searchResWindow.getSelectedRow();
                    String proposalNumber =(String)resultData.get("PROPOSAL_NUMBER");
                    int status = resultData.get(STATUS_CODE) == null ? 0 : Integer.parseInt(resultData.get(STATUS_CODE).toString());
                    // JM 4-6-2012 removed awarded, unfunded, and revision required as acceptable types; added award pending; changed error message
                    if( !(status==PENDING || status==AWARD_PENDING )){
                        CoeusOptionPane.showInfoDialog("The proposal '"+proposalNumber+"' cannot be selected. \n It is not of status \"Pending\" or \"Award Pending\".");
                        return ;
                    }
                    // Query from the server for checking financial disclosures
                    boolean isDisclosure=getDisclosureDataFromServer(proposalNumber);
                    if(!isDisclosure) {
                        int selectionOption = CoeusOptionPane.showQuestionDialog(DISCLOSURE_MSG1+proposalNumber+DISCLOSURE_MSG2,CoeusOptionPane.OPTION_YES_NO, CoeusOptionPane.DEFAULT_NO);
                        if (selectionOption==CoeusOptionPane.SELECTION_NO) {
                            return;
                        } else if (selectionOption == CoeusOptionPane.SELECTION_YES) {
                            setAwardStatusToBeMadeHold(true);
                        }
                    }
                    super.actionPerformed(actionEvent);
                }else{
                    super.actionPerformed(actionEvent);
                }
            }
        }catch(Exception ex){
            CoeusOptionPane.showInfoDialog(ex.getMessage());
        }
    }
    
    
    private boolean getDisclosureDataFromServer (String proposalNumber) {
        String moduleKey = proposalNumber;
        CoeusVector cvModuleDetals = new CoeusVector(); 
        Integer moduleCode = new Integer(2);
        cvModuleDetals.add(moduleCode);
        cvModuleDetals.add(moduleKey);
        RequesterBean requesterBean = new RequesterBean();
        ResponderBean responderBean = new ResponderBean();
        requesterBean.setFunctionType(GET_DATA_FN_TYPE);
        requesterBean.setDataObject(cvModuleDetals);
        AppletServletCommunicator comm= new AppletServletCommunicator(connectTo,
        requesterBean);
        comm.setRequest(requesterBean);
        comm.send();
        responderBean = comm.getResponse();
        boolean retValue=false;
        if(responderBean.isSuccessfulResponse()) {
            retValue = ((Boolean)responderBean.getDataObject()).booleanValue();
        }      
        return retValue;
    }
    
    /**
     * Getter for property awardStatusToBeMadeHold.
     * @return Value of property awardStatusToBeMadeHold.
     */
    public boolean isAwardStatusToBeMadeHold() {
        return awardStatusToBeMadeHold;
    }
    
    /**
     * Setter for property awardStatusToBeMadeHold.
     * @param awardStatusToBeMadeHold New value of property awardStatusToBeMadeHold.
     */
    public void setAwardStatusToBeMadeHold(boolean awardStatusToBeMadeHold) {
        this.awardStatusToBeMadeHold = awardStatusToBeMadeHold;
    }
    
}



