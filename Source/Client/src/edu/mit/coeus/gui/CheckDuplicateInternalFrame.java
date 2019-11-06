/*
 * CheckDuplicateInternalFrame.java
 *
 * Created on January 9, 2003, 2:52 PM
 */

package edu.mit.coeus.gui;

import java.util.*;

import edu.mit.coeus.utils.CoeusGuiConstants;
/**
 * This class is used to maintain all the opened internal frames in the
 * application. It maintains a HashMap named <CODE>moduleFrames</CODE> with
 * module name as key and a HashMap named <CODE>openedFrames</CODE> as value.
 * <CODE>openedFrames</CODE> holds the details of the frames opened for a 
 * particular module, with record id (i.e like Committee id, protocol id etc..)
 * as key and a vector named <CODE> frameDetails</CODE> as value. This vector 
 * consists of two elements where the first element represents the mode in which
 * the internal frame is opened and the second element is the reference to the
 * internal frame.
 * @author  ravikanth
 */
public class CheckDuplicateInternalFrame {
    
    private HashMap openedFrames;
    private HashMap moduleFrames;
    private Vector frameDetails;
    private static final char COPY_MODE = 'E';
    private static final char DISPLAY_MODE = 'D';
    CoeusMessageResources messageResources;
    private Map controllers = new HashMap();
    
    /** Creates a new instance of CheckDuplicateInternalFrame */
    public CheckDuplicateInternalFrame() {
        moduleFrames = new HashMap();
        messageResources = CoeusMessageResources.getInstance();
    }
    
    /**
     * This method is used to store the internal frame reference used to show
     * the details of the given refId  in the given module. 
     *
     * @param moduleId String representing module name like "Committee Details" etc..
     * @param refId String representing the primary key whose details will be shown.
     * eg: for committee module refId  is CommitteeID value.
     * @param mode String representing the form opening mode.
     * @param frame reference to the <CODE>CoeusInternalFrame</CODE> object.
     */    
    public void putFrame(String moduleId, String refId, char mode , CoeusInternalFrame frame){
        if( moduleFrames.containsKey(moduleId) ){
            openedFrames = (HashMap)moduleFrames.get(moduleId);
        }else {
            openedFrames = new HashMap();
        }
        frameDetails = new Vector();
        frameDetails.addElement(""+mode);
        frameDetails.addElement(frame);
        if(mode == COPY_MODE){
            refId = "";
        }
        openedFrames.put(refId,frameDetails);
        moduleFrames.put(moduleId,openedFrames);
    }
    
    /**
     * Method is used to store the internal frame reference object for the given
     * module.
     * @param moduleId String representing module name like "Committee Details" etc..
     * @param frame reference to the <CODE>CoeusInternalFrame</CODE> object.
     */    
    public void putFrame(String moduleId, CoeusInternalFrame frame){
        moduleFrames.put(moduleId,frame);
    }
    
    /**
     * Method is used to check whether any record in the given module has been
     * opened in Edit mode. It is also used to check whether the given record is
     * already opened in Display mode.
     *
     * @param moduleId String representing module name.
     * @param refId String representing the primary key of the record.
     * @param mode String representing the requested form opening mode.
     * @throws Exception if any record is requested for editing while the 
     * application is already editing another record in the same module. And also
     * it throws Exception when the requested record is already opened in any mode.
     * @return  boolean false if the requested record is not yet opened in any mode
     * 
     */    
    //Code Commented/Modified for case id#2308 - Multiple Proposals/Protocols/... open in display mode - start
    public boolean isDuplicate(String moduleId, String refId, char mode) 
        throws Exception {
        /* for copy mode refId will be empty*/    
        //code modified for coeus4.3 Amendments/Renewal enhancements
//        if ( mode == COPY_MODE ) {        
        if ( mode == COPY_MODE || mode == 'P') {
            refId = "";
        }
        /* check any frames are opened for the given module */    
        if( moduleFrames.containsKey(moduleId) ){
            /* get the collection of opened frames for the given module */
            openedFrames = (HashMap) moduleFrames.get(moduleId);
            /* check given record is present in the opened frames */
            if( openedFrames.containsKey(refId) ) {
                /* if the given record already exists don't display any 
                   exception, directly show him the same window. */
                throw new Exception("");
                
            }else if ( ( ( mode != DISPLAY_MODE ) )
                    && ( getEditingFrame( moduleId ) != null ) ){
                String message = "";
                boolean maxDisplay = false;
                if(moduleId.equals(CoeusGuiConstants.COMMITTEE_FRAME_TITLE)){
                    message = messageResources.parseMessageKey(
                        "committee_edit_exceptionCode.2300");
                }else if (moduleId.equals(CoeusGuiConstants.PROTOCOL_FRAME_TITLE)){
                    message = messageResources.parseMessageKey(
                        "protocol_edit_exceptionCode.2301");
                }else if (moduleId.equals(CoeusGuiConstants.SCHEDULE_DETAILS_TITLE)){
                    message = messageResources.parseMessageKey(
                        "schedule_edit_exceptionCode.2302");
                }else if (moduleId.equals(CoeusGuiConstants.AMENDMENT_DETAILS_TITLE)){
                    message = messageResources.parseMessageKey(
                        "amend_edit_exceptionCode.2303");
                }else if (moduleId.equals(CoeusGuiConstants.RENEWAL_DETAILS_TITLE)){
                    message = messageResources.parseMessageKey(
                        "revision_edit_exceptionCode.2304");
                }else if ( moduleId.equals(CoeusGuiConstants.PROPOSAL_DETAILS_FRAME_TITLE) ) {
                    message = messageResources.parseMessageKey(
                        "proposal_DtlForm_exceptionCode.2300");                                            
                }else if ( moduleId.equals(CoeusGuiConstants.AWARD_BASE_WINDOW) ) {
                    message = messageResources.parseMessageKey(
                        "award_exceptionCode.1001");
                }else if ( moduleId.equals(CoeusGuiConstants.BUDGET_FRAME_TITLE ) ){
                    message = messageResources.parseMessageKey(
                        "budget_common_exceptionCode.1009");
                }else if ( moduleId.equals(CoeusGuiConstants.PROPOSAL_NARRATIVE_FRAME_TITLE ) ){
                    message = messageResources.parseMessageKey(
                        "narrative_edit_exceptionCode.1111");
                }else if(moduleId.equals(CoeusGuiConstants.CORRECT_INSTITUTE_PROPOSAL_BASE_WINDOW ) ){
                    message = messageResources.parseMessageKey(
                        "instPropo_exceptionCode.1001");
                }else if(moduleId.equals(CoeusGuiConstants.REPORTING_REQ_BASE_WINDOW)) {
                    message = messageResources.parseMessageKey(
                        "addRepRequirements_exceptionCode.1051");
                }else if(moduleId.equals(CoeusGuiConstants.NEGOTIATION_DETAILS)) {
                    message = messageResources.parseMessageKey(
                        "negotiationBaseWindow_exceptionCode.1052");
                }else if(moduleId.equals(CoeusGuiConstants.CORRECT_SUBCONTRACT_BASE_WINDOW)) {
                    message = messageResources.parseMessageKey(
                        "subcontractBasewindow_exceptionCode.1003");
                }else if(moduleId.equals(CoeusGuiConstants.SPONSORHIERARCHY_BASE_WINDOW)) {
                    message = "";
                }else if(moduleId.equals(CoeusGuiConstants.QUESTIONNAIRE_TITLE)) {
                    message = messageResources.parseMessageKey(
                        "questionnaire_exceptionCode.1009");
                }
                //Added for Questionnaire Answer start 1
                else if(moduleId.equals(CoeusGuiConstants.PROPOSAL_QUESTIONNAIRE)) {
                    message = messageResources.parseMessageKey(
                        "questions_exceptionCode.1008");
                }else if(moduleId.equals(CoeusGuiConstants.PROTOCOL_QUESTIONNAIRE)) {
                    message = messageResources.parseMessageKey(
                        "questions_exceptionCode.1008");
                }
                 //Added for Questionnaire Answer end 1
                return maxDisplay;
                //throw new Exception(message);
            }else if( mode == DISPLAY_MODE ){
                if(moduleId.equals(CoeusGuiConstants.PROPOSAL_DETAILS_FRAME_TITLE)){
                    int openedWindowsCount = 0;
                    int totalWindowsCount = openedFrames.size();
                    Collection values = openedFrames.values();
                    Iterator valuesIterator = values.iterator();
                    while( valuesIterator.hasNext() ) {
                        Vector details = (Vector)valuesIterator.next();
                        if( details.get(0).equals(""+CoeusGuiConstants.DISPLAY_MODE)) {
                            openedWindowsCount++;
                        }
                    }                    
//                    if( openedWindowsCount == CoeusGuiConstants.MAX_PROP_DISPLAY_SHEETS ) {
//                        throw new Exception("The number of proposal sheets opened for display has reached the maximum.");
//                    }                    
                }else if(moduleId.equals(CoeusGuiConstants.AWARD_BASE_WINDOW)){
                    int openedWindowsCount = 0;
                    int totalWindowsCount = openedFrames.size();
                    Collection values = openedFrames.values();
                    Iterator valuesIterator = values.iterator();
                    while( valuesIterator.hasNext() ) {
                        Vector details = (Vector)valuesIterator.next();
                        if( details.get(0).equals(""+CoeusGuiConstants.DISPLAY_MODE)) {
                            openedWindowsCount++;
                        }
                    }
//                    if( openedWindowsCount == CoeusGuiConstants.MAX_AWARD_DISPLAY_SHEETS ) {
//                        throw new Exception(messageResources.parseMessageKey("award_exceptionCode.1002"));
//                    }
                }else if(moduleId.equals(CoeusGuiConstants.CORRECT_INSTITUTE_PROPOSAL_BASE_WINDOW)){
                    int openedWindowsCount = 0;
                    int totalWindowsCount = openedFrames.size();
                    Collection values = openedFrames.values();
                    Iterator valuesIterator = values.iterator();
                    while( valuesIterator.hasNext() ) {
                        Vector details = (Vector)valuesIterator.next();
                        if( details.get(0).equals(""+CoeusGuiConstants.DISPLAY_MODE)) {
                            openedWindowsCount++;
                        }
                    }
//                    if( openedWindowsCount == CoeusGuiConstants.MAX_INST_PROPOSAL_DISPLAY_SHEETS ) {
//                        throw new Exception(messageResources.parseMessageKey("instPropo_exceptionCode.1002"));
//                    }
                }else if( moduleId.equals(CoeusGuiConstants.BUDGET_FRAME_TITLE) ){
                    int openedWindowsCount = 0;
                    int totalWindowsCount = openedFrames.size();
                    Collection values = openedFrames.values();
                    Iterator valuesIterator = values.iterator();
                    while( valuesIterator.hasNext() ) {
                        Vector details = (Vector)valuesIterator.next();
                        if( details.get(0).equals(""+CoeusGuiConstants.DISPLAY_MODE)) {
                            openedWindowsCount++;
                        }
                    }
//                    if( openedWindowsCount == CoeusGuiConstants.MAX_PROP_DISPLAY_SHEETS ) {
//                        throw new Exception("budget_common_exceptionCode.1010");
//                    }
                }else if(moduleId.equals(CoeusGuiConstants.REPORTING_REQ_BASE_WINDOW) ) {
                    int openedWindowsCount = 0;
                    int totalWindowsCount = openedFrames.size();
                    Collection values = openedFrames.values();
                    Iterator valuesIterator = values.iterator();
                    while( valuesIterator.hasNext() ) {
                        Vector details = (Vector)valuesIterator.next();
                        if( details.get(0).equals(""+CoeusGuiConstants.DISPLAY_MODE)) {
                            openedWindowsCount++;
                        }
                    }
//                    if( openedWindowsCount == CoeusGuiConstants.MAX_REP_REQ_DISPLAY_SHEETS ) {
//                        throw new Exception("addRepRequirements_exceptionCode.1052");
//                    }
                }else if(moduleId.equals(CoeusGuiConstants.NEGOTIATION_DETAILS) ) {
                    int openedWindowsCount = 0;
                    int totalWindowsCount = openedFrames.size();
                    Collection values = openedFrames.values();
                    Iterator valuesIterator = values.iterator();
                    while( valuesIterator.hasNext() ) {
                        Vector details = (Vector)valuesIterator.next();
                        if( details.get(0).equals(""+CoeusGuiConstants.DISPLAY_MODE)) {
                            openedWindowsCount++;
                        }
                    }
//                    if( openedWindowsCount == CoeusGuiConstants.MAX_NEGOTIATION_DISPLAY_SHEETS) {
//                        throw new Exception("The number of negotiation sheets opened for display has reached the maximum.");
//                    }
                }else if(moduleId.equals(CoeusGuiConstants.QUESTIONNAIRE_TITLE)){
                    int openedWindowsCount = 0;
                    int totalWindowsCount = openedFrames.size();
                    Collection values = openedFrames.values();
                    Iterator valuesIterator = values.iterator();
                    while( valuesIterator.hasNext() ) {
                        Vector details = (Vector)valuesIterator.next();
                        if( details.get(0).equals(""+CoeusGuiConstants.DISPLAY_MODE)) {
                            openedWindowsCount++;
                        }
                    }
//                    if( openedWindowsCount == CoeusGuiConstants.MAX_NEGOTIATION_DISPLAY_SHEETS) {
//                        throw new Exception("The number of questionanire  window opened for display has reached the maximum.");
//                    }
                }
                //Added for Questionnaire Answer start 2
                else if(moduleId.equals(CoeusGuiConstants.PROPOSAL_QUESTIONNAIRE)){
                    int openedWindowsCount = 0;
                    int totalWindowsCount = openedFrames.size();
                    Collection values = openedFrames.values();
                    Iterator valuesIterator = values.iterator();
                    while( valuesIterator.hasNext() ) {
                        Vector details = (Vector)valuesIterator.next();
                        if( details.get(0).equals(""+CoeusGuiConstants.DISPLAY_MODE)) {
                            openedWindowsCount++;
                        }
                    }
//                    if( openedWindowsCount == CoeusGuiConstants.MAX_PROP_DISPLAY_SHEETS ) {
//                        throw new Exception("The number of proposal sheets opened for display has reached the maximum.");
//                    }
                }
                else if(moduleId.equals(CoeusGuiConstants.PROTOCOL_QUESTIONNAIRE)){
                    int openedWindowsCount = 0;
                    int totalWindowsCount = openedFrames.size();
                    Collection values = openedFrames.values();
                    Iterator valuesIterator = values.iterator();
                    while( valuesIterator.hasNext() ) {
                        Vector details = (Vector)valuesIterator.next();
                        if( details.get(0).equals(""+CoeusGuiConstants.DISPLAY_MODE)) {
                            openedWindowsCount++;
                        }
                    }
//                    if( openedWindowsCount == CoeusGuiConstants.MAX_AWARD_DISPLAY_SHEETS ) {
//                        throw new Exception("The number of questionanire  window opened for display has reached the maximum.");
//                    }
                }
                //Added for Questionnaire Answer end 2
                else if(moduleId.equals(CoeusGuiConstants.CORRECT_SUBCONTRACT_BASE_WINDOW) ) {
                    int openedWindowsCount = 0;
                    int totalWindowsCount = openedFrames.size();
                    Collection values = openedFrames.values();
                    Iterator valuesIterator = values.iterator();
                    while(valuesIterator.hasNext()) {
                        Vector details = (Vector)valuesIterator.next();
                        if(details.get(0).equals(""+CoeusGuiConstants.DISPLAY_MODE)) 
                            openedWindowsCount++;                        
                    }
//                    if(openedWindowsCount == CoeusGuiConstants.MAX_SUBCONTRACT_DISPLAY_SHEETS) {
//                        throw new Exception(messageResources.parseMessageKey("subcontractBasewindow_exceptionCode.1002"));
//                    }
                }else if(moduleId.equals(CoeusGuiConstants.SPONSORHIERARCHY_BASE_WINDOW) ) {
//                    if(openedFrames.size() > 1) {
//                        throw new Exception("");
//                    }
                }


            }//End if mode == DISPLAY_MODE 
        }
        return false;
    }
    //Code Commented/Modified for case id#2308 - Multiple Proposals/Protocols/... open in display mode - end
    
    /** To get number of frames open for the given moduleId and mode
     * @return openedWindowsCount the count of open internal frames in the given mode
     */
    public int getFrameCount(String moduleId, char mode) {
        int openedWindowsCount = 0;
        if ( moduleFrames.containsKey(moduleId) ) {
            openedFrames = ( HashMap ) moduleFrames.get( moduleId );
            int totalWindowsCount = openedFrames.size();
            Collection values = openedFrames.values();
            Iterator valuesIterator = values.iterator();
            while( valuesIterator.hasNext() ) {
                Vector details = (Vector)valuesIterator.next();
                if( details.get(0).equals("" + mode)) {
                    openedWindowsCount++;
                }
            }
        }
        return openedWindowsCount;
    }
    
    /**
     * This method is used to get the internal frame which is opened in edit mode
     * for a given module.
     *
     * @param moduleId String representing the name of the module.
     * @return reference to the internal frame if any record is opened in edit
     * mode for the given module, else null.
     */
    public CoeusInternalFrame getEditingFrame(String moduleId){
        if ( moduleFrames.containsKey(moduleId) ) {
            openedFrames = ( HashMap ) moduleFrames.get( moduleId );
            Set set = openedFrames.keySet();
            if(!set.isEmpty()){
                Iterator iterator = set.iterator();
                boolean found;
                while(iterator.hasNext()){
                    frameDetails = (Vector) openedFrames.get(iterator.next().toString());
                    char openedMode = frameDetails.elementAt(0).toString().charAt(0);
                    if(openedMode != DISPLAY_MODE){
                        return (CoeusInternalFrame)frameDetails.elementAt(1);
                    }
                }
            }
        }
        return null;
    }
    
    /**
     * This method is used to remove the internal frame reference when the 
     * frame is closed.
     * @param moduleId String representing the module name.
     * @param refId  String representing the primary key of the record whose 
     * details are shown in the internal frame.
     */    
    public void removeFrame(String moduleId, String refId){
        if( moduleFrames.containsKey(moduleId) ){
            openedFrames = (HashMap) moduleFrames.get(moduleId);
            if( openedFrames.containsKey(refId) ) {
                removeController(((Vector)openedFrames.get(refId)).get(1));
                openedFrames.remove(refId);
                if(openedFrames.size() > 0 ) {
                    moduleFrames.put(moduleId,openedFrames);
                }else{
                    removeController(moduleFrames.get(moduleId));
                    moduleFrames.remove(moduleId);
                }
            }
        }
    }
    
    /**
     * Overloaded method which is used to remove the internal frame reference
     * when the frame is closed. Mainly used for base windows as there won't be
     * any reference id and mode for base windows.
     * @param moduleId  String representing module name like "Committee"
     */    
    public void removeFrame(String moduleId){
        removeController(moduleFrames.get(moduleId));
        moduleFrames.remove(moduleId);
    }

    /**
     * This method is used to get the internal frame reference object for the 
     * given module id and reference id.
     * @param moduleId String representing the module name.
     * @param refId String representing the primary key of the record whose details
     * are used to show in the internal frame.
     * @return  reference to <CODE>CoeusInternalFrame</CODE> if there is any 
     * object for the given module id and reference id else null.
     */    
    public CoeusInternalFrame getFrame(String moduleId, String refId){
        if( moduleFrames.containsKey(moduleId) ){
            openedFrames = (HashMap) moduleFrames.get(moduleId);
            if( openedFrames.containsKey(refId) ) {
                return (CoeusInternalFrame) ((Vector)openedFrames.get(refId)).elementAt(1);
            }
        }
        return null;
    }
    
    /**
     * Overloaded method used to get the internal frame reference object for the
     * given module id. Mainly used for getting base window references.
     * @param moduleId String representing the module name.
     * @return reference to <CODE>CoeusInternalFrame</CODE>. 
     */    
    public CoeusInternalFrame getFrame(String moduleId){
        return (CoeusInternalFrame)moduleFrames.get(moduleId);
    }
    
    /**
     * Method used to check whether any child windows of proposal is open before 
     * closing the ProposalDetailForm.
     * @return boolean false if none of the proposal child windows are open.
     * @throws Exception with custom message if any of the proposal child windows are open.
     */
    
    public boolean isProposalChildWindowsOpen() throws Exception{
        HashMap childWindows;
        if( moduleFrames != null && moduleFrames.size() > 0 ) {
            childWindows = ( HashMap ) moduleFrames.get(CoeusGuiConstants.PROPOSAL_DETAILS_FRAME_TITLE);
            if( childWindows != null && childWindows.size() > 0 ) {
                childWindows = ( HashMap ) moduleFrames.get(CoeusGuiConstants.PROPOSAL_NARRATIVE_FRAME_TITLE);
                if( childWindows != null && childWindows.size() > 0 ) {
                    throw new Exception("Narrative Window for this Proposal is open. Please close the Narrative Window before closing the Proposal Window.");
                }
                childWindows = ( HashMap ) moduleFrames.get(CoeusGuiConstants.PROPOSAL_ABSTRACTS_FRAME_TITLE);
                if( childWindows != null && childWindows.size() > 0 ) {
                    throw new Exception("The Abstract Window for this Proposal is open. Please close the Abstract Window before closing the Proposal Window.");
                }
                childWindows = ( HashMap ) moduleFrames.get(CoeusGuiConstants.PROP_PERSON_BIOGRAPHY_FRAME_TITLE);
                if( childWindows != null && childWindows.size() > 0 ) {
                    throw new Exception("The Personnel Window for this Proposal is open. Please close the Personnel Window before closing the Proposal Window.");
                }
            }
        }
        return false;
    }

    public edu.mit.coeus.gui.event.Controller getController(Object uiClass) {
        edu.mit.coeus.gui.event.Controller controller = null, tempController = null;
        try{
        //check for internal frames
        Iterator iterator = controllers.keySet().iterator();
        List lstRemove = new ArrayList();
        while (iterator.hasNext()) {
            tempController = (edu.mit.coeus.gui.event.Controller)iterator.next();
            try {
                if (tempController != null && tempController.getControlledUI() != null && tempController.getControlledUI().equals(uiClass)) {
                    controller = tempController;
                }
                //remove controllers which doesn't control internal frames
                else if (tempController == null || tempController.getControlledUI() == null ||
                        !(tempController.getControlledUI() instanceof edu.mit.coeus.gui.CoeusInternalFrame)) {
                    lstRemove.add(tempController);
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }

        for(int index = 0; index < lstRemove.size(); index++) {
            controllers.remove(lstRemove.get(index));
        }
        lstRemove.clear();
        /*if(controllers.containsKey(uiClass)) {
            return (edu.mit.coeus.gui.event.Controller)controllers.get(uiClass);
        }else {
            return null;
        }*/
        }catch(Exception ex){
            ex.printStackTrace();
        }
        return controller;
    }
    
    public void putController(edu.mit.coeus.gui.event.Controller controller){
        if(controller ==null) return;
        controllers.put(controller, null);
    }
    private void removeController(Object uiClass){
        controllers.remove(getController(uiClass));
    }
}
