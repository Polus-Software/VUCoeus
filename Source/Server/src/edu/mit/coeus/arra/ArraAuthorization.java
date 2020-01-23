/*
 * ArraAuthorization.java
 *
 * Created on September 23, 2009, 12:40 PM
 *
 */

package edu.mit.coeus.arra;

import edu.mit.coeus.arra.bean.ArraReportTxnBean;
// import edu.mit.coeus.award.bean.AwardInvestigatorsBean;
// import edu.mit.coeus.award.bean.AwardReportTxnBean;
// import edu.mit.coeus.award.bean.AwardTxnBean;
import edu.mit.coeus.exception.CoeusException;
// import edu.mit.coeus.instprop.bean.InvestigatorUnitAdminTypeBean;
import edu.mit.coeus.user.bean.UserMaintDataTxnBean;
// import edu.mit.coeus.utils.CoeusVector;
import edu.mit.coeus.utils.UtilFactory;
// import edu.mit.coeus.utils.dbengine.DBEngineConstants;
// import edu.mit.coeus.utils.dbengine.DBEngineImpl;
import edu.mit.coeus.utils.dbengine.DBException;
// import edu.mit.coeus.utils.dbengine.Parameter;
// import java.util.HashMap;
// import java.util.Vector;

/**
 *
 * @author sreenath
 */
public class ArraAuthorization {
    
    private static final String MAINTAIN_ARRA_REPORTS = "MAINTAIN_ARRA_REPORTS";
    private static final String VIEW_ARRA_REPORTS     = "VIEW_ARRA_REPORTS";
    // ARRA phase 2 - Start
    private static final String MODIFY_ARRA_REPORTS     = "MODIFY_ARRA_REPORTS";
    
    // private String userId;
    
    /** Creates a new instance of ArraAuthorization */
    public ArraAuthorization() {
        
    }
    
   /* public ArraAuthorization(String userId) {
        this.userId = userId;
    }
    */
    // ARRA phase 2 - End
    /* This function is used to check if the user has enough rights to modify an arra report award.
     * @param userId - logged in userId
     * @param personId - logged in personId
     * @param mitAwardNumber - the mit award number
     * @param versionNumber 
     * @param reportNumber 
     * @return boolean true if user has right, false if user does not have right
     */
    public boolean canModifyArraReportDetails(String userId, String personId, String mitAwardNumber, int versionNumber, int reportNumber) throws CoeusException, DBException, org.okip.service.shared.api.Exception, Exception{
        boolean canModify = false;
        // Arra Phase 2 Changes 
//        AwardTxnBean awardTxnBean = new AwardTxnBean();
        // int validAwardNumber = awardTxnBean.validateAwardNumber(mitAwardNumber);
        //   if(validAwardNumber == 1){
         ArraReportTxnBean arraReportTxnBean = new ArraReportTxnBean(userId);
         try{
             
             String awardLeadUnit = arraReportTxnBean.fetchLeadUnitForArraAward(mitAwardNumber, versionNumber, reportNumber);
            
            // Check if the user has Unit level right in the lead unit of the award.
//            canModify = checkUserHasMaintainArraRight(userId, mitAwardNumber);
             canModify = checkUserHasMaintainArraRight(userId, mitAwardNumber, versionNumber, reportNumber, awardLeadUnit);
            
            if(!canModify){
                // Check if the user is the user has "MODIFY_ARRA_REPORTS" right
//                canModify = checkUserHasModifyArraRight(userId,mitAwardNumber);
                canModify = checkUserHasModifyArraRight(userId,mitAwardNumber,versionNumber, reportNumber, awardLeadUnit);
            }
            
            if(!canModify){
                // Check if the user is the user is the ARRA admin assistant for the award
                canModify = arraReportTxnBean.isPersonArraAdminAssistantForAward(personId, mitAwardNumber);
            }

            if(!canModify){
                // Check if the user is the PI of the award
//                canModify = checkPersonIsPI(personId,mitAwardNumber);
                canModify = arraReportTxnBean.isUserPiForArraAward(userId, mitAwardNumber, versionNumber, reportNumber);
            }
        } catch(Exception ex){
            UtilFactory.log(ex.getMessage());
        }
        return canModify;
    }
    
    /* This function is used to check if the user has enough rights to view an arra report award.
     * @param userId - logged in userId
     * @param mitAwardNumber - the mit award number
     * @return boolean true if user has right, false if user does not have right
     */
    // Arra Phase 2 Changes
//    public boolean canViewArraReportDetails(String userId, String mitAwardNumber) throws CoeusException, DBException, org.okip.service.shared.api.Exception {
    public boolean canViewArraReportDetails(String userId, String mitAwardNumber, int versionNumber, int reportNumber) throws CoeusException, DBException, org.okip.service.shared.api.Exception {
        boolean canView = false;
//        AwardTxnBean awardTxnBean = new AwardTxnBean();
       // int validAwardNumber = awardTxnBean.validateAwardNumber(mitAwardNumber);
       // if (validAwardNumber == 1) {
        
//            canView = checkUserHasViewArraRight(userId, mitAwardNumber);
        ArraReportTxnBean arraReportTxnBean = new ArraReportTxnBean();
        String awardLeadUnit = "";
        try {
            awardLeadUnit = arraReportTxnBean.fetchLeadUnitForArraAward(mitAwardNumber, versionNumber, reportNumber);
        } catch (Exception ex) {
            UtilFactory.log(ex.getMessage());
        }
        canView = checkUserHasViewArraRight(userId, mitAwardNumber, versionNumber, reportNumber, awardLeadUnit);
        
        return canView;
    }
    
   /* This function is used to check if the user has enough rights to mark an ARRA award as Complete
    * @param userId - logged in userId
    * @param personId - logged in personId
    * @param mitAwardNumber - the mit award number
    * @param arraAwardVersionNumber
    * @param arraAwardReportNumber
    * @return boolean true if user has right, false if user does not have right
    */
    //Arra Phase 2 Changes
//    public boolean canMarkReportComplete(String userId,String personId,String mitAwardNumber) throws CoeusException, DBException, org.okip.service.shared.api.Exception{
    public boolean canMarkReportComplete(String userId, String mitAwardNumber, int arraAwardVersionNumber, int arraAwardReportNumber) throws Exception, org.okip.service.shared.api.Exception{
        boolean hasRight = false;
//        hasRight = checkPersonIsPI(personId,mitAwardNumber);
        ArraReportTxnBean arraReportTxnBean = new ArraReportTxnBean(userId); 
        hasRight = arraReportTxnBean.isUserPiForArraAward(userId, mitAwardNumber, arraAwardVersionNumber, arraAwardReportNumber);
        if(!hasRight){
            // Commented for ARRA Phase 2 changes.
            // User doesnot require any OSP right along with MAINTAIN_ARRA right
//            boolean hasMaintainRight = checkUserHasMaintainArraRight(userId,mitAwardNumber);
//            if(hasMaintainRight){
//                UserMaintDataTxnBean userMaintDataTxnBean = new UserMaintDataTxnBean();
//                hasRight = userMaintDataTxnBean.getUserHasAnyOSPRight(userId);
//            }
//            hasRight = checkUserHasMaintainArraRight(userId,mitAwardNumber);
            String awardLeadUnit = arraReportTxnBean.fetchLeadUnitForArraAward(mitAwardNumber, arraAwardVersionNumber, arraAwardReportNumber);
            hasRight = checkUserHasMaintainArraRight(userId,mitAwardNumber, arraAwardVersionNumber, arraAwardReportNumber, awardLeadUnit);
        }
        return hasRight;
    }
    
    /* This function is used to check if the user has MAINTAIN_ARRA_REPORTS right.
     * @param userId - logged in userId
     * @param mitAwardNumber - the mit award number
     * @return boolean true if user has right, false if user does not have right
     */
    // Arra Phase 2 Changes
//    public boolean checkUserHasMaintainArraRight(String userId, String mitAwardNumber) throws CoeusException, DBException, org.okip.service.shared.api.Exception{
    private boolean checkUserHasMaintainArraRight(String userId, String mitAwardNumber, int versionNumber, int reportNumber, String awardLeadUnit) {
        boolean hasRight = false;
//        AwardTxnBean awardTxnBean = new AwardTxnBean();
        UserMaintDataTxnBean userMaintDataTxnBean = new UserMaintDataTxnBean();
        ArraReportTxnBean arraReportTxnBean = new ArraReportTxnBean(userId);
        //String awardLeadUnit;
        try {
            //awardLeadUnit = arraReportTxnBean.fetchLeadUnitForArraAward(mitAwardNumber, versionNumber, reportNumber);
            hasRight = userMaintDataTxnBean.getUserHasRight(userId, MAINTAIN_ARRA_REPORTS, awardLeadUnit);
        } catch (Exception ex) {
            UtilFactory.log(ex.getMessage());
        }
        
        return hasRight;
    }
    
    /* This function is used to check if the user has VIEW_ARRA_REPORTS right.
     * @param userId - logged in userId
     * @param mitAwardNumber - the mit award number
     * @return boolean true if user has right, false if user does not have right
     */
    //Arra Phase 2 Changes
//    private boolean checkUserHasViewArraRight(String userId, String mitAwardNumber) throws CoeusException, DBException, org.okip.service.shared.api.Exception{
    private boolean checkUserHasViewArraRight(String userId, String mitAwardNumber, int versionNumber, int reportNumber, String awardLeadUnit) {
        boolean hasRight = false;
//        AwardTxnBean awardTxnBean = new AwardTxnBean();
        UserMaintDataTxnBean userMaintDataTxnBean = new UserMaintDataTxnBean();
//        String awardLeadUnit = awardTxnBean.getLeadUnitForAward(mitAwardNumber);
        ArraReportTxnBean arraReportTxnBean = new ArraReportTxnBean(userId);
        //String awardLeadUnit;
        try {
            //awardLeadUnit = arraReportTxnBean.fetchLeadUnitForArraAward(mitAwardNumber, versionNumber, reportNumber);
            hasRight = userMaintDataTxnBean.getUserHasRight(userId, VIEW_ARRA_REPORTS, awardLeadUnit);
        } catch (Exception ex) {
            UtilFactory.log(ex.getMessage());
        }
        
        return hasRight;
    }
    // Arra Phase 2 Changes
//    /* This function is used to check if the logged in user is the PI of an award.
//     * @param personId - logged in personId
//     * @param mitAwardNumber - the mit award number
//     * @return boolean true if user is the PI, false if user is not the PI.
//     */
//    private boolean checkPersonIsPI(String personId,String mitAwardNumber) throws CoeusException, DBException{
//        boolean personIsPI = false;
//        // Get all the investigators
//        AwardTxnBean awardTxnBean = new AwardTxnBean();
//        CoeusVector cvAwardInvestigators = awardTxnBean.getAwardInvestigators(mitAwardNumber);
//        AwardInvestigatorsBean awardInvestigatorsBean = null;
//        boolean piPresent = false;
//        if(cvAwardInvestigators != null){
//            int totalInvestigators = cvAwardInvestigators.size();
//            for(int index = 0; index < totalInvestigators; index++){
//                awardInvestigatorsBean = (AwardInvestigatorsBean) cvAwardInvestigators.get(index);
//                // Check if the investigator is PI
//                if(awardInvestigatorsBean.isPrincipalInvestigatorFlag()){
//                    piPresent = true;
//                    break;
//                }
//            }
//        }
//        if(piPresent && awardInvestigatorsBean != null){
//            if(personId != null && awardInvestigatorsBean.isPrincipalInvestigatorFlag()){
//                if(personId.equalsIgnoreCase(awardInvestigatorsBean.getPersonId())){
//                    personIsPI = true;
//                }
//            }
//        }
//        return personIsPI;
//    }
    /* This function is used to check if the user has enough rights to mark an ARRA award as InComplete
    * @param userId - logged in userId
    * @param mitAwardNumber - the mit award number
    * @return boolean true if user has right, false if user does not have right
    */
    // Arra Phase 2 Changes
//    public boolean canMarkReportInComplete(String userId,String mitAwardNumber) throws CoeusException, DBException, org.okip.service.shared.api.Exception{
      public boolean canMarkReportInComplete(String userId, String mitAwardNumber, int versionNumber, int reportNumber) throws CoeusException, DBException, org.okip.service.shared.api.Exception, Exception{
        boolean hasMaintainRight = false;        
//        hasMaintainRight = checkUserHasMaintainArraRight(userId,mitAwardNumber);   
        
        ArraReportTxnBean arraReportTxnBean = new ArraReportTxnBean();
        String awardLeadUnit = arraReportTxnBean.fetchLeadUnitForArraAward(mitAwardNumber, versionNumber, reportNumber);
        
        hasMaintainRight = checkUserHasMaintainArraRight(userId, mitAwardNumber, versionNumber, reportNumber, awardLeadUnit);   
        return hasMaintainRight;
    }
    
     /**
      * This methos checks if the user has 'MODIFY_ARRA_REPORTS' right in the lead unit of the passed
      * mitAwardNumber.
      */
     // Arra Phase 2 Changes
//     public boolean checkUserHasModifyArraRight(String userId, String mitAwardNumber) throws CoeusException, DBException, org.okip.service.shared.api.Exception{
     private boolean checkUserHasModifyArraRight(String userId, String mitAwardNumber, int versionNumber, int reportNumber, String awardLeadUnit) {
         boolean hasRight = false;
//         AwardTxnBean awardTxnBean = new AwardTxnBean();
         UserMaintDataTxnBean userMaintDataTxnBean = new UserMaintDataTxnBean();
//         String awardLeadUnit = awardTxnBean.getLeadUnitForAward(mitAwardNumber);
         ArraReportTxnBean arraReportTxnBean = new ArraReportTxnBean(userId);
        //String awardLeadUnit;
        try {
            // awardLeadUnit = arraReportTxnBean.fetchLeadUnitForArraAward(mitAwardNumber, versionNumber, reportNumber);
            hasRight = userMaintDataTxnBean.getUserHasRight(userId, MODIFY_ARRA_REPORTS, awardLeadUnit);
        } catch (Exception ex) {
            UtilFactory.log(ex.getMessage());
        }
         
         return hasRight;
     }
     
     public boolean canModifyAllArraReportFields(String userId, String mitAwardNumber, int versionNumber, int reportNumber){
         
         ArraReportTxnBean arraReportTxnBean = new ArraReportTxnBean();
        String awardLeadUnit = "";
        try {
            awardLeadUnit = arraReportTxnBean.fetchLeadUnitForArraAward(mitAwardNumber, versionNumber, reportNumber);
        } catch (Exception ex) {
            UtilFactory.log(ex.getMessage());
        }
         
         boolean canModify = checkUserHasMaintainArraRight(userId, mitAwardNumber, versionNumber, reportNumber, awardLeadUnit);
         return canModify;
     }
      /* This function is used to check if the user has enough rights to mark an ARRA award as Submit
       * @param userId - logged in userId
       * @param mitAwardNumber - the mit award number
       * @return boolean true if user has right, false if user does not have right
       */
     // Arra Phase 2 Changes
      public boolean canMarkReportSubmit(String userId, String mitAwardNumber, int versionNumber, int reportNumber) throws CoeusException, DBException, org.okip.service.shared.api.Exception, Exception{
        boolean hasMaintainRight = false;      
        ArraReportTxnBean arraReportTxnBean = new ArraReportTxnBean();
        String awardLeadUnit = arraReportTxnBean.fetchLeadUnitForArraAward(mitAwardNumber, versionNumber, reportNumber);
        hasMaintainRight = checkUserHasMaintainArraRight(userId, mitAwardNumber, versionNumber, reportNumber, awardLeadUnit);   
        return hasMaintainRight;
    }
     // Arra- Phase 2 Changes - End
}
