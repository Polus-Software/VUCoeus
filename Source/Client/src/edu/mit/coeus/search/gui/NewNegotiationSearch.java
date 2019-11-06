/** Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 */

package edu.mit.coeus.search.gui;

import java.awt.Component;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import java.util.HashMap;

import edu.mit.coeus.brokers.*;
import edu.mit.coeus.exception.CoeusClientException;
import edu.mit.coeus.gui.CoeusMessageResources;
import edu.mit.coeus.utils.AppletServletCommunicator;
import edu.mit.coeus.utils.CoeusGuiConstants;
import edu.mit.coeus.utils.CoeusOptionPane;

/**
 * NewNegotiationSearch.java
 * Created on July 20, 2004, 10:29 AM
 * @author  Vyjayanthi
 */
public class NewNegotiationSearch extends CoeusSearch 
implements ActionListener {
    
    /** Holds CoeusMessageResources instance for reading message Properties.
     */
    private CoeusMessageResources coeusMessageResources = 
            CoeusMessageResources.getInstance();
    
    private static final char NEGOTIATION_COUNT = 'D';
    private static final String CONNECTION_STRING = CoeusGuiConstants.CONNECTION_URL +
            "/NegotiationMaintenanceServlet";
    private static final String PROPOSAL_NUMBER = "PROPOSAL_NUMBER";
    private static final String LETTER_T = "T";
    private static final String LETTER_P = "P";
    private static final String NEGOTIATION_EXISTS = "negotiationDetail_exceptionCode.1103";
    private static final String NEGOTIATION_CANNOT_BE_TEMP = "negotiationDetail_exceptionCode.1104";
    
    /** Creates a new instance of NewNegotiationSearch */
    public NewNegotiationSearch() {
    }
    
    /** Creates a new instance of NewNegotiationSearch
     *  @param parent frame
     *  @param request identifier
     *  @param request type
     */
    public NewNegotiationSearch(Component parentFrame, String searchReq,
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
                    String proposalNumber = (String)resultData.get(PROPOSAL_NUMBER);
                    if( negotiationExists(proposalNumber) ){
                        CoeusOptionPane.showInfoDialog(
                        coeusMessageResources.parseMessageKey(NEGOTIATION_EXISTS));
                        return ;
                    }
                    try{
                        Integer.parseInt(proposalNumber.substring(0));
                    }catch (NumberFormatException numberFormatException){
                        if( !(proposalNumber.startsWith(LETTER_T) || proposalNumber.startsWith(LETTER_P)) ) {
                            CoeusOptionPane.showInfoDialog(
                            coeusMessageResources.parseMessageKey(NEGOTIATION_CANNOT_BE_TEMP));
                            return ;
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
    
    /** To check if the negotiation exists for the given proposal number
     * @param proposalNumber holds the proposal number
     */
    private boolean negotiationExists(String proposalNumber) 
    throws CoeusClientException {
        RequesterBean requester = new RequesterBean();
        requester.setFunctionType(NEGOTIATION_COUNT);
        requester.setDataObject(proposalNumber);
        AppletServletCommunicator comm = new AppletServletCommunicator(CONNECTION_STRING, requester);
        comm.send();
        ResponderBean response = comm.getResponse();
        if(response.isSuccessfulResponse()){
            return ((Boolean)response.getDataObject()).booleanValue();
        }else {
            throw new CoeusClientException(response.getMessage());
        }
    }
    
}
