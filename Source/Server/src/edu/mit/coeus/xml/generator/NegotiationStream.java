/*
 * NegotiationStream.java
 *
 * Created on July 13, 2005, 1:28 PM
 */

package edu.mit.coeus.xml.generator;

import edu.mit.coeus.utils.*;
import edu.mit.coeus.utils.query.*;
import edu.mit.coeus.utils.UtilFactory;
import edu.mit.coeus.utils.dbengine.DBException;
import edu.mit.coeus.exception.CoeusException;
import edu.mit.coeus.negotiation.bean.*;
import edu.mit.coeus.user.bean.UserMaintDataTxnBean;
import edu.mit.coeus.utils.xml.bean.negotiation.*;
//import java.math.BigDecimal;
import java.util.*;
import javax.xml.bind.JAXBException;


/**
 *this file is created for case 1735 --Negotiation Activity Report
 * @author  jenlu
 */
public class NegotiationStream extends ReportBaseStream{
    private ObjectFactory objFactory;
    private CoeusXMLGenrator xmlGenerator;
    private CoeusVector cvNegotiationNums;
    private String negotiationNumber;
    private String printType;
    private String userID;
    private int activityNumber;
    private NegotiationTxnBean negotiationTxnBean;
    private NegotiationInfoBean negotiationInfoBean;
    private NegotiationHeaderBean negotiationHeaderBean;
    //case 3590 start
    private NegotiationLocationBean negotiationLocationBean;
    //case 3590 end 
    private static final String packageName = "edu.mit.coeus.utils.xml.bean.negotiation";
    private static final String RESTRICTED_VIEW = "restrictedView";
    private static final String ACTIVITY_NUM = "activityNumber";
    private static final String[] SORTING_FIELDS = {"activityDate", "activityNumber"};
    private static final String PRINT_ONE_ACTIVITY = "printOne";
    private static final String PRINT_ALL_ACTIVITY = "printAll"; 
    private static final String PRINT_NEGOTIATION = "printNegotiation";
    /** Creates a new instance of NegotiationStream */
    public NegotiationStream() {
        objFactory = new ObjectFactory();
        xmlGenerator = new CoeusXMLGenrator();
    }
    
    
    public org.w3c.dom.Document getStream(Hashtable params) throws DBException,CoeusException{
        printType = (String)params.get("PRINT_TYPE");
        userID = (String)params.get("USER_ID");
        if (printType.equals(PRINT_NEGOTIATION)) {
             cvNegotiationNums = (CoeusVector)params.get("NEGOTIATION_NUMS");
        }else{
             negotiationNumber = (String)params.get("NEGOTIATION_NUM");      
             if (printType.equals(PRINT_ONE_ACTIVITY)) {
                activityNumber = Integer.parseInt(params.get("ACTIVITY_NUM").toString());
             }              
        }
        negotiationTxnBean = new NegotiationTxnBean();
  
        NegotiationsType negotiationsType = getNegotiations();
       
        return xmlGenerator.marshelObject(negotiationsType,packageName);
        
     }
    
    public Object getObjectStream(Hashtable params) throws DBException,CoeusException{
        printType = (String)params.get("PRINT_TYPE");
        userID = (String)params.get("USER_ID");
        if (printType.equals(PRINT_NEGOTIATION)) {
             cvNegotiationNums = (CoeusVector)params.get("NEGOTIATION_NUMS");
        }else{
             negotiationNumber = (String)params.get("NEGOTIATION_NUM");      
             if (printType.equals(PRINT_ONE_ACTIVITY)) {
                activityNumber = Integer.parseInt(params.get("ACTIVITY_NUM").toString());
             }              
        }
        negotiationTxnBean = new NegotiationTxnBean();
        NegotiationsType negotiationsType = getNegotiations();
        return negotiationsType;
    }
    
    private NegotiationsType getNegotiations()throws CoeusXMLException,DBException,CoeusException{
        NegotiationsType negotiationsType = null;
        try{
            negotiationsType = objFactory.createNegotiations();
            negotiationsType.getNegotiationData().addAll(getNegotiationDatas());
        }catch(JAXBException jaxbEx){
            UtilFactory.log(jaxbEx.getMessage(),jaxbEx,"NegotiationStream","getStream()");
            throw new CoeusXMLException(jaxbEx.getMessage());
        }
        return negotiationsType;
    }
    
     private Vector getNegotiationDatas() throws JAXBException,CoeusException,DBException{
        Vector vcNegotiationData = new Vector();
        if (printType.equals(PRINT_NEGOTIATION) && ( cvNegotiationNums != null && cvNegotiationNums.size() > 0)) {
            for (int index = 0 ; index < cvNegotiationNums.size(); index++) {
                negotiationNumber = (String)cvNegotiationNums.get(index);
                vcNegotiationData.add(getNegotiationData(negotiationNumber));
            }
             
        }else{
            vcNegotiationData.add(getNegotiationData(negotiationNumber));
        }
        return vcNegotiationData;
     }
     
     private NegotiationDataType getNegotiationData(String negotiationNum)throws JAXBException,CoeusException,DBException{
        negotiationInfoBean = negotiationTxnBean.getNegotiationInfo(negotiationNumber);
        negotiationHeaderBean = negotiationTxnBean.getNegotiationHeader(negotiationNumber);
        NegotiationDataType negotiationDataType = objFactory.createNegotiationDataType();
        negotiationDataType.setProposalNumber(negotiationHeaderBean.getProposalNumber());
        negotiationDataType.setInvestigator(UtilFactory.convertNull(negotiationHeaderBean.getPiName()));
        negotiationDataType.setTitle(UtilFactory.convertNull(negotiationHeaderBean.getTitle()));
        //set sponsor
        if (negotiationHeaderBean.getSponsorCode() != null){
            SponsorType sponsorType = objFactory.createSponsorType();
            sponsorType.setSponsorCode(negotiationHeaderBean.getSponsorCode());
            sponsorType.setSponsorName(negotiationHeaderBean.getSponsorName());
            negotiationDataType.setSponsor(sponsorType);
        }
        //set unit
        if (negotiationHeaderBean.getLeadUnit() != null){
            LeadUnitType leadUnitType = objFactory.createLeadUnitType();
            leadUnitType.setUnitNumber(negotiationHeaderBean.getLeadUnit());
            leadUnitType.setUnitName(negotiationHeaderBean.getUnitName());
            negotiationDataType.setLeadUnit(leadUnitType);
        }        
       //set proposal type
        if (negotiationHeaderBean.getProposalTypeCode() != 0){
            ProposalType proposalType = objFactory.createProposalType();
            proposalType.setProposalTypeDesc(negotiationHeaderBean.getProposalTypeDescription());
            negotiationDataType.setProposalType(proposalType);
        }
        //set start data
        if (negotiationInfoBean.getStartDate() != null){
            Calendar tempDate = Calendar.getInstance();
            tempDate.setTime(negotiationInfoBean.getStartDate());
            negotiationDataType.setStartDate(tempDate);
        }             
        
        negotiationDataType.setDocFileAddress(UtilFactory.convertNull(negotiationInfoBean.getDocFileAddress()));
        negotiationDataType.setContractAdmin(UtilFactory.convertNull(negotiationHeaderBean.getInitialContractAdmin()));
        //set status
        if (negotiationInfoBean.getStatusCode() != 0){
            StatusType statusType = objFactory.createStatusType();
            statusType.setStatusDesc(negotiationInfoBean.getStatusDescription());
            negotiationDataType.setStatus(statusType);
        }
        
        negotiationDataType.setNegotiator(UtilFactory.convertNull(negotiationInfoBean.getNegotiatorName()));
        //case 3590 start
        if (negotiationInfoBean.getPrimeSponsorCode() != null){
            SponsorType primeSponsor = objFactory.createSponsorType();
            primeSponsor.setSponsorCode(negotiationInfoBean.getPrimeSponsorCode());
            primeSponsor.setSponsorName(negotiationInfoBean.getPrimeSponsorName());
            negotiationDataType.setPrimeSponsor(primeSponsor);
        }
        if (negotiationInfoBean.getProposedStartDate() != null){
            Calendar tempDate = Calendar.getInstance();
            tempDate.setTime(negotiationInfoBean.getProposedStartDate());
            negotiationDataType.setProposedStartDate(tempDate);
        } 
        if (negotiationInfoBean.getNegotiationAgreeTypeCode()!= 0){
            StatusType agreementType = objFactory.createStatusType();
            agreementType.setStatusDesc(negotiationInfoBean.getNegotiationAgreeTypeDescription());
            negotiationDataType.setAgreementType(agreementType);
        }
        
        CoeusVector cvNegotiationLocaton = negotiationTxnBean.
					getNegotiationLocation(negotiationNumber);
        if( cvNegotiationLocaton != null && cvNegotiationLocaton.size() > 0 ){
            negotiationLocationBean = (NegotiationLocationBean)cvNegotiationLocaton.get(0);
        }
        if (negotiationLocationBean != null){
           if (negotiationLocationBean.getNegotiationLocationTypeCode()!= 0){
                StatusType locationType = objFactory.createStatusType();
                locationType.setStatusDesc(negotiationLocationBean.getNegotiationLocationTypeDes());
                negotiationDataType.setLocationType(locationType);
            } 
            if (negotiationLocationBean.getEffectiveDate()!= null){
                Date effectiveDate,effectiveToday = new Date();
                DateUtils dateUtils = new DateUtils();
                effectiveDate = negotiationLocationBean.getEffectiveDate();
                Calendar tempDate = Calendar.getInstance();
                tempDate.setTime(effectiveDate);
                negotiationDataType.setEffectiveDate(tempDate);
                
//                if(effectiveToday.after(effectiveDate)) {
//                    int dateDiff = dateUtils.dateDifference(effectiveToday, effectiveDate);
//                    negotiationDataType.setNumOfDays(dateDiff);
//                }
                if(!effectiveDate.after(effectiveToday)) {
                    int dateDiff = dateUtils.dateDifference(effectiveToday, effectiveDate);
                    negotiationDataType.setNumOfDays(dateDiff);
                }
               
            }
        }
        //case 3590 end 
        Calendar currentDate = Calendar.getInstance();
        currentDate.setTime(new Date());
        negotiationDataType.setCurrentDate(currentDate);
        negotiationDataType.getActivities().addAll(getActivities());
        
        return negotiationDataType;         
     }
     
     private Vector getActivities()throws JAXBException,CoeusException,DBException{
         Vector vcNegotiationActivities = new Vector();
         //Modified for COEUSDEV-294 : Error adding activity to a negotiation - Start
//         CoeusVector cvActivities = negotiationTxnBean.getNegotiationActivities(negotiationInfoBean.getNegotiationNumber(),userID);
         CoeusVector cvActivities = negotiationTxnBean.getNegotiationActivities(negotiationInfoBean.getNegotiationNumber(),userID,negotiationHeaderBean.getLeadUnit());
         //COEUSDEV-294 : End
         UserMaintDataTxnBean userMaintDataTxnBean = new UserMaintDataTxnBean();
//         //Check for MODIFY_ACTIVITIES OSP right -- don't need to check this in the activity 
//	 boolean hasOSPRight = userMaintDataTxnBean.getUserHasOSPRight(userID, MODIFY_ACTIVITIES);
         //Check for any OSP right for the user
         boolean hasAnyOSPRight = false;
         try{
            hasAnyOSPRight = userMaintDataTxnBean.getUserHasAnyOSPRight(userID);  
         }catch(org.okip.service.shared.api.Exception ex){
             UtilFactory.log(ex.getMessage(),ex, "NegotiationStream", "getActivities");
             throw new CoeusException(ex.getMessage());
         }
         if( cvActivities != null && cvActivities.size() > 0 && !hasAnyOSPRight){          
            Equals eqRestrictedView = new Equals(RESTRICTED_VIEW, false);
            cvActivities = cvActivities.filter(eqRestrictedView);            
         }
         if( cvActivities != null && cvActivities.size() > 0){
                cvActivities.sort(SORTING_FIELDS, false);
                
            if (printType.equalsIgnoreCase(PRINT_ALL_ACTIVITY)){
                for( int index = 0; index < cvActivities.size(); index++ ){
                    NegotiationActivitiesBean negotiationActivitiesBean = (NegotiationActivitiesBean)cvActivities.get(index);
                    ActivitiesType activitiesType = setActivityData(negotiationActivitiesBean);
                    vcNegotiationActivities.add(activitiesType);
                }
            }else if (printType.equalsIgnoreCase(PRINT_ONE_ACTIVITY)){
                Equals eqActivityNumber = new Equals(ACTIVITY_NUM, new Integer(activityNumber));
                cvActivities = cvActivities.filter(eqActivityNumber); 
                if( cvActivities != null && cvActivities.size() > 0){
                    NegotiationActivitiesBean negotiationActivitiesBean = (NegotiationActivitiesBean)cvActivities.get(0);
                    ActivitiesType activitiesType = setActivityData(negotiationActivitiesBean);
                    vcNegotiationActivities.add(activitiesType);
                }
            }else if (printType.equalsIgnoreCase(PRINT_NEGOTIATION)) {
                NegotiationActivitiesBean negotiationActivitiesBean = (NegotiationActivitiesBean)cvActivities.get(0);
                ActivitiesType activitiesType = setActivityData(negotiationActivitiesBean);
                vcNegotiationActivities.add(activitiesType);
            }
         }
         
         return vcNegotiationActivities;
         
     }
     private ActivitiesType setActivityData(NegotiationActivitiesBean negotiationActivitiesBean)throws JAXBException,CoeusException,DBException{
         ActivitiesType activitiesType = objFactory.createActivitiesType();
                
         if (negotiationActivitiesBean.getActivityDate() != null){
            Calendar tempDate = Calendar.getInstance();
            tempDate.setTime(negotiationActivitiesBean.getActivityDate());
            activitiesType.setActivityDate(tempDate);
         }      
         if (negotiationActivitiesBean.getCreateDate()  != null){
            Calendar tempDate = Calendar.getInstance();
            tempDate.setTime(negotiationActivitiesBean.getCreateDate());
            activitiesType.setCreateDate(tempDate);
         } 
         if (negotiationActivitiesBean.getFollowUpDate() != null){
            Calendar tempDate = Calendar.getInstance();
            tempDate.setTime(negotiationActivitiesBean.getFollowUpDate());
            activitiesType.setFollowupDate(tempDate);
         } 
         if (negotiationActivitiesBean.getUpdateTimestamp()!= null){
            Calendar tempDate = Calendar.getInstance();
            tempDate.setTime((Date)negotiationActivitiesBean.getUpdateTimestamp());
            activitiesType.setLastDate(tempDate);
         }
                
         if (negotiationActivitiesBean.getActivityNumber() != 0){
            ActivityType activityType = objFactory.createActivityType();
            activityType.setActivityDesc(negotiationActivitiesBean.getActivityTypeDescription());
            activitiesType.setActivity(activityType);
         }
                
//         activitiesType.setDocFileAddress(UtilFactory.convertNull(negotiationActivitiesBean.getDocumentFileAddress()));
         activitiesType.setUpdatedBy(UtilFactory.convertNull(negotiationActivitiesBean.getLastUpdatedBy()));
         activitiesType.setDescription(UtilFactory.convertNull(negotiationActivitiesBean.getDescription()));
         return activitiesType;
     }
}
