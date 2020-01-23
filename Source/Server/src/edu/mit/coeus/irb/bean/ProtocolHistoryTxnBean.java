/*
 * ProtocolHistoryTxnBean.java
 *
 * Created on July 10, 2007, 5:14 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package edu.mit.coeus.irb.bean;

import edu.mit.coeus.exception.CoeusException;
import edu.mit.coeus.utils.ChangeHistoryBean;
import edu.mit.coeus.utils.ChangeHistoryGroup;
import edu.mit.coeus.utils.CoeusVector;
import edu.mit.coeus.utils.dbengine.DBEngineConstants;
import edu.mit.coeus.utils.dbengine.DBEngineImpl;
import edu.mit.coeus.utils.dbengine.DBException;
import edu.mit.coeus.utils.dbengine.Parameter;
import edu.mit.coeus.utils.dbengine.TransactionMonitor;
import java.util.Date;
import java.util.HashMap;
import java.util.TreeMap;
import java.util.Vector;

/**
 * This class is used to get all the details for populating the history details
 * of protocol.
 *
 * @author leenababu
 */
public class ProtocolHistoryTxnBean {
    // instance of a dbEngine
    private DBEngineImpl dbEngine;
    // instance of a TransactionMonitor
    private TransactionMonitor transMon;
    //holds the String value "String"
    private final String STRING = "String";
    //holds the String value "Date"
    private final String DATE = "Date";
    //holds the empty string
    private final String EMPTY_STRING = "";
    /** Creates a new instance of ProtocolHistoryTxnBean */
    public ProtocolHistoryTxnBean() {
        dbEngine = new DBEngineImpl();
        transMon = TransactionMonitor.getInstance();
    }
    
    /**
     * Populates the ProtocolHistoryBean with the change details between the protocol
     * with sequence numbers <code>currSeqNumber</code> and <code>prevSeqNumber</code>
     *
     * @param currSeqNumber - sequenceNumber of the current ProtocolInfoBean
     * @param prevSeqNumber - sequenceNumber of the previous PreviousInfoBean
     * @return ProtocolHistoryBean populated with the history details
     */
//Modified for Case# 3087 - In Premium - Review History Add the following elements - Start    
//    public ProtocolHistoryBean getProtocolHistory(String protocolNumber, int currSeqNumber, int prevSeqNumber)
      public TreeMap getProtocolHistory(String protocolNumber, int currSeqNumber, int prevSeqNumber)
      throws CoeusException, DBException{
//Modified for Case# 3087 - In Premium - Review History Add the following elements - End          
        ProtocolHistoryBean protocolHistoryBean = new ProtocolHistoryBean();
        protocolHistoryBean.setCurrProtocolInfoBean(getProtocolInfo(protocolNumber,currSeqNumber));
        if(currSeqNumber == 0){
            prevSeqNumber = protocolHistoryBean.getCurrProtocolInfoBean().getSequenceNumber()-1;
            protocolHistoryBean.setPrevProtocolInfoBean(getProtocolInfo(protocolNumber,prevSeqNumber));
        }else{
            protocolHistoryBean.setPrevProtocolInfoBean(getProtocolInfo(protocolNumber,prevSeqNumber));
        }
        //Modified and Added for Case# 3087 - In Premium - Review History Add the following elements - Start
        TreeMap hmProtocolHistory = new TreeMap();
        //Populate the header information
        protocolHistoryBean.setProtocolNumber(protocolNumber);
        protocolHistoryBean.setSequenceNumber(Integer.toString(protocolHistoryBean.getCurrProtocolInfoBean().getSequenceNumber()));
        protocolHistoryBean.setTitle(protocolHistoryBean.getCurrProtocolInfoBean().getTitle());
        hmProtocolHistory.put("ProtocolHistoryBean",protocolHistoryBean);
        populateGeneralInfoGroup(protocolHistoryBean,hmProtocolHistory);
        populateInvestigatorGroup(protocolHistoryBean,hmProtocolHistory);
        populateKeyPersonGroup(protocolHistoryBean,hmProtocolHistory);
        populateAttachmentGroup(protocolHistoryBean,hmProtocolHistory);
        
        populateFundingSource(protocolHistoryBean,hmProtocolHistory);
        populateOrganization(protocolHistoryBean,hmProtocolHistory);
        populateCorrespondents(protocolHistoryBean,hmProtocolHistory);
        populateAreaOfResearch(protocolHistoryBean,hmProtocolHistory);
        populateSubjects(protocolHistoryBean, hmProtocolHistory);
        populateSpecialReview(protocolHistoryBean,hmProtocolHistory);
//        return protocolHistoryBean;
        return hmProtocolHistory;
        //Modified and Added for Case# 3087 - In Premium - Review History Add the following elements - End
    }
    
    /**
     * Returns the protocol information set in a ProtocolInfoBean for the given
     * <code>protocolNumber</code> and <code>sequenceNumber</code>. If
     * <code>sequenceNumber</code> is 0, then the maximum of the sequence number
     * for the protocol is used.
     *
     * @param protocolNumber  Protocol Number
     * @param sequenceNumber  Sequence Number
     * @return instance of ProtocolInfoBean populated with the protocol details
     */
    public ProtocolInfoBean getProtocolInfo(String protocolNumber, int sequenceNumber)
    throws CoeusException, DBException{
        ProtocolInfoBean protocolInfoBean = new ProtocolInfoBean();
        ProtocolDataTxnBean protocolDataTxnBean = new ProtocolDataTxnBean();
        
        //Fetch the general Info of the protocol
        if(sequenceNumber == 0){
            protocolInfoBean = protocolDataTxnBean.getProtocolMaintenanceDetails(protocolNumber);
            sequenceNumber = protocolInfoBean.getSequenceNumber();
        }else{
            protocolInfoBean = protocolDataTxnBean.
                    getProtocolMaintenanceDetails(protocolNumber, sequenceNumber);
        }
        
        //Fetch the Investigators
        protocolInfoBean.setInvestigators(protocolDataTxnBean.
                getProtocolInvestigators(protocolNumber, sequenceNumber));
        //Fetch the KeyPersons
        protocolInfoBean.setKeyStudyPersonnel(protocolDataTxnBean.
                getProtocolKeyPersonList(protocolNumber, sequenceNumber));
        //Fetch all the actions perfomed
        protocolInfoBean.setActions(protocolDataTxnBean.getProtocolActions(protocolNumber, sequenceNumber));
        
        //Added for Case# 3087 - In Premium - Review History Add the following elements - Start
        //Fetch Funding Source data 
        protocolInfoBean.setFundingSources(protocolDataTxnBean.
                getProtocolFundingSources(protocolNumber, sequenceNumber));
        //Fetch  Protocol Organization data 
        protocolInfoBean.setLocationLists(protocolDataTxnBean.
                getProtocolLocationList(protocolNumber,sequenceNumber));
        //Fetch Correspondents data 
        protocolInfoBean.setCorrespondants(protocolDataTxnBean.
                getProtocolCorrespondents(protocolNumber,sequenceNumber));
        //Fetch  Area of Research data 
        protocolInfoBean.setAreaOfResearch(protocolDataTxnBean.
                getProtocolResearchArea(protocolNumber,sequenceNumber));
        //Fetch  Subjects data 
        protocolInfoBean.setVulnerableSubjectLists(protocolDataTxnBean.
                getProtocolVulnerableSubList(protocolNumber,sequenceNumber));
        //Fetch Special Review data 
        protocolInfoBean.setSpecialReviews(protocolDataTxnBean.
                getProtocolSpecialReview(protocolNumber,sequenceNumber));
        //Added for Case# 3087 - In Premium - Review History Add the following elements - End
        return protocolInfoBean;
    }
    
    /**
     * Populates the General Info group change details into the hmProtocolHistory
     *
     * @param protcolHistoryBean instance of ProtocolHistoryBean
     */
    //Modified and Commented for Case# 3087 - In Premium - Review History Add the following elements - Start
//    public void populateGeneralInfoGroup(ProtocolHistoryBean protocolHistoryBean)
    public void populateGeneralInfoGroup(ProtocolHistoryBean protocolHistoryBean,TreeMap hmProtocolHistory ) 
    throws CoeusException, DBException{
        
//        ProtocolInfoBean currentProtocol = protocolHistoryBean.getCurrProtocolInfoBean();
//        ProtocolInfoBean prevProtocol = protocolHistoryBean.getPrevProtocolInfoBean();
//        
//        ChangeHistoryGroup generalInfoGroup = new ChangeHistoryGroup();
//        ChangeHistoryBean changeHistoryBean = null;
//        generalInfoGroup.setName("General Info");
//        int status = 0;
//        
//        //Compare title
//        if(currentProtocol.getTitle().equals(prevProtocol.getTitle())){
//            status = 0;
//        }else{
//            status = 1;
//        }
//        changeHistoryBean = new ChangeHistoryBean(
//                "Title",currentProtocol.getTitle(),STRING, status );
//        generalInfoGroup.getHistoryBeanList().add(changeHistoryBean);
//        
//        //Compare Application Date
//        if(currentProtocol.getApplicationDate()==null && prevProtocol.getApplicationDate() ==null){
//            status = 0;
//        }else if(currentProtocol.getApplicationDate()==null && prevProtocol.getApplicationDate() !=null){
//            status = 1;
//        }else if(currentProtocol.getApplicationDate()!=null && prevProtocol.getApplicationDate() ==null){
//            status = 1;
//        }else if(currentProtocol.getApplicationDate().equals(prevProtocol.getApplicationDate())){
//            status = 0;
//        }else{
//            status = 1;
//        }
//        changeHistoryBean = new ChangeHistoryBean(
//                "Application Date",(currentProtocol.getApplicationDate()==null)? null:currentProtocol.getApplicationDate().toString(),DATE, status );
//        generalInfoGroup.getHistoryBeanList().add(changeHistoryBean);
//        
//        //Compare Approval Date
//        if(currentProtocol.getApprovalDate()==null && prevProtocol.getApprovalDate() ==null){
//            status = 0;
//        }else if(currentProtocol.getApprovalDate()==null && prevProtocol.getApprovalDate() !=null){
//            status = 1;
//        }else if(currentProtocol.getApprovalDate()!=null && prevProtocol.getApprovalDate() ==null){
//            status = 1;
//        }else if(currentProtocol.getApprovalDate().equals(prevProtocol.getApprovalDate())){
//            status = 0;
//        }else{
//            status = 1;
//        }
//        changeHistoryBean = new ChangeHistoryBean(
//                "Approval Date",(currentProtocol.getApprovalDate()==null)? null:currentProtocol.getApprovalDate().toString(),DATE, status );
//        generalInfoGroup.getHistoryBeanList().add(changeHistoryBean);
//        
//
//        //Compare Expiration Date
//        if(currentProtocol.getExpirationDate()==null && prevProtocol.getExpirationDate() ==null){
//            status = 0;
//        }else if(currentProtocol.getExpirationDate()==null && prevProtocol.getExpirationDate() !=null){
//            status = 1;
//        }else if(currentProtocol.getExpirationDate()!=null && prevProtocol.getExpirationDate() ==null){
//            status = 1;
//        }else if(currentProtocol.getExpirationDate().equals(prevProtocol.getExpirationDate())){
//            status = 0;
//        }else{
//            status = 1;
//        }
//        changeHistoryBean = new ChangeHistoryBean(
//                "Expiration Date",(currentProtocol.getExpirationDate()==null)? null:currentProtocol.getExpirationDate().toString(),
//                DATE, status );
//        changeHistoryBean.setName("Expiration Date");
//        generalInfoGroup.getHistoryBeanList().add(changeHistoryBean);
//        
//        //Compare Last Approval Date
//        if(currentProtocol.getLastApprovalDate()==null && prevProtocol.getLastApprovalDate() ==null){
//            status = 0;
//        }else if(currentProtocol.getLastApprovalDate()==null && prevProtocol.getLastApprovalDate() !=null){
//            status = 1;
//        }else if(currentProtocol.getLastApprovalDate()!=null && prevProtocol.getLastApprovalDate() ==null){
//            status = 1;
//        }else if(currentProtocol.getLastApprovalDate().equals(prevProtocol.getLastApprovalDate())){
//            status = 0;
//        }else{
//            status = 1;
//        }
//        changeHistoryBean = new ChangeHistoryBean(
//                "Last Approval Date",
//                (currentProtocol.getLastApprovalDate()==null)? null:currentProtocol.getLastApprovalDate().toString(),
//                DATE, status );
//        
//        generalInfoGroup.getHistoryBeanList().add(changeHistoryBean);
//        protocolHistoryBean.getChangeHistoryGroupList().add(generalInfoGroup);
//        //Find the last action performed
//        String lastAction = getProtocolLastAction(protocolHistoryBean);
//        protocolHistoryBean.setLastAction(lastAction);
//Modified and Commented for Case# 3087 - In Premium - Review History Add the following elements - End        
        
        //Added for Case# 3087 - In Premium - Review History Add the following elements - Start
        ProtocolInfoBean currentProtocol = protocolHistoryBean.getCurrProtocolInfoBean();
        ProtocolInfoBean prevProtocol =  protocolHistoryBean.getPrevProtocolInfoBean();
               
        Vector currTitle = new Vector();
        Vector prevTitle = new Vector();
        currTitle.add(currentProtocol.getTitle());
        prevTitle.add(prevProtocol.getTitle());
        Vector title = new Vector();
        title.add(currTitle);
        title.add(prevTitle);
        hmProtocolHistory.put("Title",title);
        
        Vector currApplicationDate = new Vector();
        Vector prevApplicationDate = new Vector();
        if(currentProtocol.getApplicationDate() != null){
            currApplicationDate.add(new Date(currentProtocol.getApplicationDate().getTime()));
        }
        if(prevProtocol.getApplicationDate() != null){
            prevApplicationDate.add(new Date(prevProtocol.getApplicationDate().getTime()));
        }
        Vector applicationDate = new Vector();
        applicationDate.add(currApplicationDate);
        applicationDate.add(prevApplicationDate);
        hmProtocolHistory.put("Application Date",applicationDate);
        
        Vector currApprovalDate = new Vector();
        Vector prevApprovalDate = new Vector();
        if(currentProtocol.getApprovalDate() != null){
            currApprovalDate.add(new Date(currentProtocol.getApprovalDate().getTime()));
        }
        if(prevProtocol.getApprovalDate() != null){
        prevApprovalDate.add(new Date(prevProtocol.getApprovalDate().getTime()));
        }
        Vector ApprovalDate = new Vector();
        ApprovalDate.add(currApprovalDate);
        ApprovalDate.add(prevApprovalDate);
        hmProtocolHistory.put("Approval Date",ApprovalDate);
        
        Vector currExpirationDate = new Vector();
        Vector prevExpirationDate = new Vector();
        if(currentProtocol.getExpirationDate() != null){
            currExpirationDate.add(new Date(currentProtocol.getExpirationDate().getTime()));
        }
        if(prevProtocol.getExpirationDate() != null){
            prevExpirationDate.add(new Date(prevProtocol.getExpirationDate().getTime()));
        }
        Vector expirationDate = new Vector();
        expirationDate.add(currExpirationDate);
        expirationDate.add(prevExpirationDate);
        hmProtocolHistory.put("Expiration Date",expirationDate);        
        
        Vector currLastApprDate = new Vector();
        Vector prevLastApprDate = new Vector();
        if(currentProtocol.getLastApprovalDate() != null){
            currLastApprDate.add(new Date(currentProtocol.getLastApprovalDate().getTime()));
        }
        if(prevProtocol.getLastApprovalDate() != null){
            prevLastApprDate.add(new Date(prevProtocol.getLastApprovalDate().getTime()));
        }
        Vector lastApprDate = new Vector();
        lastApprDate.add(currLastApprDate);
        lastApprDate.add(prevLastApprDate);
        hmProtocolHistory.put("Last Approval Date",lastApprDate);     
        String lastAction;
        try{
            lastAction = getProtocolLastAction(protocolHistoryBean);
        }catch(Exception ex){
            lastAction = "";
        }    
        protocolHistoryBean.setLastAction(lastAction);
        //Added for Case# 3087 - In Premium - Review History Add the following elements - End
    }
    
    /**
     * Populates the Investigator group change details into the hmProtocolHistory
     *
     * @param protcolHistoryBean - instance of ProtocolHistoryBean
     */
    //Modified and Added for Case# 3087 - In Premium - Review History Add the following elements - Start
//    public void populateInvestigatorGroup(ProtocolHistoryBean protocolHistoryBean){
    public void populateInvestigatorGroup(ProtocolHistoryBean protocolHistoryBean ,TreeMap hmProtocolHistory){        
//        ProtocolInfoBean currentProtocol = protocolHistoryBean.getCurrProtocolInfoBean();
//        ProtocolInfoBean prevProtocol = protocolHistoryBean.getPrevProtocolInfoBean();
//        ProtocolInvestigatorsBean currInvestigatorBean = null;
//        ProtocolInvestigatorsBean prevInvestigatorBean = null;
//        
//        //Find the PI and comapre PI changes
//        ProtocolInvestigatorsBean investigatorBean =null;
//        ChangeHistoryGroup PIGroup = new ChangeHistoryGroup();
//        ChangeHistoryBean changeHistoryBean = null;
//        PIGroup.setName("PI");
//        int status = 0;
//        if(protocolHistoryBean.getCurrProtocolInfoBean().getInvestigators()!=null){
//            for(int i=0; i < protocolHistoryBean.getCurrProtocolInfoBean().getInvestigators().size();i++){
//
//                investigatorBean = (ProtocolInvestigatorsBean)protocolHistoryBean.getCurrProtocolInfoBean().getInvestigators().get(i);
//                if(investigatorBean.isPrincipalInvestigatorFlag()){
//                    currInvestigatorBean = investigatorBean;
//                    break;
//                }
//            }
//        }
//        
//        if(protocolHistoryBean.getPrevProtocolInfoBean().getInvestigators()!=null){
//            for(int i=0; i < protocolHistoryBean.getPrevProtocolInfoBean().getInvestigators().size();i++){
//                investigatorBean = (ProtocolInvestigatorsBean)protocolHistoryBean.getPrevProtocolInfoBean().getInvestigators().get(i);
//                if(investigatorBean.isPrincipalInvestigatorFlag()){
//                    prevInvestigatorBean = investigatorBean;
//                    break;
//                }
//            }
//        }
//        if((currInvestigatorBean==null && prevInvestigatorBean!=null) ||
//                (currInvestigatorBean.getPersonId()==null && prevInvestigatorBean.getPersonId() != null )){
//            status = 3;
//            changeHistoryBean = new ChangeHistoryBean(EMPTY_STRING,prevInvestigatorBean.getPersonName(), STRING, status);
//            PIGroup.getHistoryBeanList().add(changeHistoryBean);
//        }else if(currInvestigatorBean!=null && prevInvestigatorBean==null ||
//                (currInvestigatorBean.getPersonId()!=null && prevInvestigatorBean.getPersonId() == null )) {
//            status = 2;
//            changeHistoryBean = new ChangeHistoryBean(EMPTY_STRING,prevInvestigatorBean.getPersonName(), STRING, status);
//            PIGroup.getHistoryBeanList().add(changeHistoryBean);
//        }else if((currInvestigatorBean == null && prevInvestigatorBean == null )||
//                (currInvestigatorBean.getPersonId()==null && prevInvestigatorBean.getPersonId() == null )){
//            status = 0;
//            changeHistoryBean = new ChangeHistoryBean(EMPTY_STRING,EMPTY_STRING,STRING, status);
//            PIGroup.getHistoryBeanList().add(changeHistoryBean);
//        }else if (currInvestigatorBean.getPersonId().equals(prevInvestigatorBean.getPersonId())){
//            status = 0;
//            changeHistoryBean = new ChangeHistoryBean(EMPTY_STRING,currInvestigatorBean.getPersonName(),STRING, status);
//            PIGroup.getHistoryBeanList().add(changeHistoryBean);
//        }else{
//            changeHistoryBean = new ChangeHistoryBean(EMPTY_STRING,prevInvestigatorBean.getPersonName(),STRING, 3);
//            PIGroup.getHistoryBeanList().add(changeHistoryBean);
//            changeHistoryBean = new ChangeHistoryBean(EMPTY_STRING,currInvestigatorBean.getPersonName(),STRING, 2);
//            PIGroup.getHistoryBeanList().add(changeHistoryBean);
//        }
//        protocolHistoryBean.setPrincpalInvestigator(currInvestigatorBean.getPersonName());
//        protocolHistoryBean.getChangeHistoryGroupList().add(PIGroup);
//
//        //Populate Co Investigators
//        ChangeHistoryGroup investigatorGroup = new ChangeHistoryGroup();
//        investigatorGroup.setName("Co-investigators");
//        
//        Vector currInvestigatorsList = currentProtocol.getInvestigators();
//        Vector prevInvestigatorsList = prevProtocol.getInvestigators();
//        
//        //Finds the investigators which are new and not modified
//        boolean found = false;
//        if(prevInvestigatorsList == null && currInvestigatorsList != null){
//            for(int i = 0; i < currInvestigatorsList.size(); i++){
//                currInvestigatorBean = (ProtocolInvestigatorsBean)currInvestigatorsList.get(i);
//                if(!currInvestigatorBean.isPrincipalInvestigatorFlag()){
//                    changeHistoryBean= new ChangeHistoryBean(EMPTY_STRING, currInvestigatorBean.getPersonName(), STRING, 2);
//                    investigatorGroup.getHistoryBeanList().add(changeHistoryBean);
//                }
//            }
//        }else if(prevInvestigatorsList != null && currInvestigatorsList == null){
//            for(int i = 0; i < prevInvestigatorsList.size(); i++){
//                prevInvestigatorBean = (ProtocolInvestigatorsBean)prevInvestigatorsList.get(i);
//                if(!currInvestigatorBean.isPrincipalInvestigatorFlag()){//Exclude PI
//                    changeHistoryBean= new ChangeHistoryBean(EMPTY_STRING, prevInvestigatorBean.getPersonName(), STRING, 3);
//                    investigatorGroup.getHistoryBeanList().add(changeHistoryBean);
//                }
//            }
//        }else if(prevInvestigatorsList!=null && currInvestigatorsList!=null){
//            for(int i = 0; i < currInvestigatorsList.size(); i++){
//                currInvestigatorBean = (ProtocolInvestigatorsBean)currInvestigatorsList.get(i);
//                if(currInvestigatorBean.isPrincipalInvestigatorFlag()){
//                    continue;
//                }
//                found = false;
//                for(int j = 0; j < prevInvestigatorsList.size(); j++){
//                    prevInvestigatorBean =(ProtocolInvestigatorsBean)prevInvestigatorsList.get(j);
//                    if(prevInvestigatorBean.isPrincipalInvestigatorFlag()){
//                        continue;
//                    }
//                    if(!currInvestigatorBean.isPrincipalInvestigatorFlag()){//Exclude PI
//                        if(currInvestigatorBean.getPersonId().equals(prevInvestigatorBean.getPersonId())){
//                            found = true;
//                            changeHistoryBean= new ChangeHistoryBean(EMPTY_STRING, currInvestigatorBean.getPersonName(), STRING, 0);
//                            break;
//                        }
//                    }
//                }
//                if(!found && !currInvestigatorBean.isPrincipalInvestigatorFlag()){//Exclude PI
//                    changeHistoryBean= new ChangeHistoryBean(EMPTY_STRING, currInvestigatorBean.getPersonName(), STRING, 2);
//                }
//                investigatorGroup.getHistoryBeanList().add(changeHistoryBean);
//
//            }
//            //Finds the investigators which are deleted
//            for(int i = 0; i < prevInvestigatorsList.size(); i++){
//                prevInvestigatorBean = (ProtocolInvestigatorsBean)prevInvestigatorsList.get(i);
//                if(prevInvestigatorBean.isPrincipalInvestigatorFlag()){
//                    continue;
//                }
//                found = false;
//                for(int j = 0; j < currInvestigatorsList.size(); j++){
//                    currInvestigatorBean =(ProtocolInvestigatorsBean)currInvestigatorsList.get(j);
//                    if(currInvestigatorBean.isPrincipalInvestigatorFlag()){
//                        continue;
//                    }
//                    if(!prevInvestigatorBean.isPrincipalInvestigatorFlag()){//Exclude PI
//                        if(currInvestigatorBean.getPersonId().equals(prevInvestigatorBean.getPersonId())){
//                            found = true;
//                            break;
//                        }
//                    }
//                }
//                if(!found && !prevInvestigatorBean.isPrincipalInvestigatorFlag()){
//                    changeHistoryBean= new ChangeHistoryBean(EMPTY_STRING, prevInvestigatorBean.getPersonName(), STRING, 3);
//                    investigatorGroup.getHistoryBeanList().add(changeHistoryBean);
//                }
//            }
//        }
//        
//        if(investigatorGroup.getHistoryBeanList().size() ==  0){
//            //Add a bean into the investigator group with dummy values if there is no co-investigaors
//            changeHistoryBean = new ChangeHistoryBean(EMPTY_STRING, EMPTY_STRING, STRING, 0);
//            investigatorGroup.getHistoryBeanList().add(changeHistoryBean);
//        }
//        protocolHistoryBean.getChangeHistoryGroupList().add(investigatorGroup);
//Modified and Added for Case# 3087 - In Premium - Review History Add the following elements - End 
        
        //Added for Case# 3087 - In Premium - Review History Add the following elements - Start
        ProtocolInfoBean currentProtocol = protocolHistoryBean.getCurrProtocolInfoBean();
        ProtocolInfoBean prevProtocol = protocolHistoryBean.getPrevProtocolInfoBean();        
        //Find the PI and comapre PI changes
        ProtocolInvestigatorsBean investigatorBean =null;        
        CoeusVector cvCurrentInvestigators = new CoeusVector();
        CoeusVector cvPrevInvestigators = new CoeusVector();
        
        if(protocolHistoryBean.getCurrProtocolInfoBean().getInvestigators()!=null){
            for(int i=0; i < protocolHistoryBean.getCurrProtocolInfoBean().getInvestigators().size();i++){
                ProtocolInvestigatorsBean currPIBean = new ProtocolInvestigatorsBean();
                investigatorBean = (ProtocolInvestigatorsBean)protocolHistoryBean.getCurrProtocolInfoBean().getInvestigators().get(i);
                if(investigatorBean.isPrincipalInvestigatorFlag()){
                    currPIBean = investigatorBean;
                    protocolHistoryBean.setPrincpalInvestigator(investigatorBean.getPersonName());
                    cvCurrentInvestigators.add(currPIBean);
                    break;
                }
            }
        }
        
        if(protocolHistoryBean.getPrevProtocolInfoBean().getInvestigators()!=null){
            for(int i=0; i < protocolHistoryBean.getPrevProtocolInfoBean().getInvestigators().size();i++){
                ProtocolInvestigatorsBean prevPIBean = new ProtocolInvestigatorsBean();
                investigatorBean = (ProtocolInvestigatorsBean)protocolHistoryBean.getPrevProtocolInfoBean().getInvestigators().get(i);
                if(investigatorBean.isPrincipalInvestigatorFlag()){
                    prevPIBean = investigatorBean;
                    cvPrevInvestigators.add(prevPIBean);
                    break;
                }
            }
        }
        CoeusVector cvProtoPI = new CoeusVector();
        cvProtoPI.add(cvCurrentInvestigators);
        cvProtoPI.add(cvPrevInvestigators);
        hmProtocolHistory.put("PI",cvProtoPI);
        
        //Populate Co Investigators       
        Vector currInvestigatorsList = currentProtocol.getInvestigators();
        Vector prevInvestigatorsList = prevProtocol.getInvestigators();
        CoeusVector cvCurrCoInvestigators = new CoeusVector();
        CoeusVector cvPrevCoInvestigators = new CoeusVector();
        
        if(protocolHistoryBean.getCurrProtocolInfoBean().getInvestigators()!=null){
            for(int i=0; i < protocolHistoryBean.getCurrProtocolInfoBean().getInvestigators().size();i++){
                ProtocolInvestigatorsBean currCoInvestBean = new ProtocolInvestigatorsBean();
                investigatorBean = (ProtocolInvestigatorsBean)protocolHistoryBean.getCurrProtocolInfoBean().getInvestigators().get(i);
                if(!investigatorBean.isPrincipalInvestigatorFlag()){
                    currCoInvestBean = investigatorBean;
                    cvCurrCoInvestigators.add(currCoInvestBean);
                }
            }
        }        
        if(protocolHistoryBean.getPrevProtocolInfoBean().getInvestigators()!=null){
            for(int i=0; i < protocolHistoryBean.getPrevProtocolInfoBean().getInvestigators().size();i++){
                ProtocolInvestigatorsBean prevCoInvestBean = new ProtocolInvestigatorsBean();
                investigatorBean = (ProtocolInvestigatorsBean)protocolHistoryBean.getPrevProtocolInfoBean().getInvestigators().get(i);
                if(!investigatorBean.isPrincipalInvestigatorFlag()){
                    prevCoInvestBean = investigatorBean;
                    cvPrevCoInvestigators.add(prevCoInvestBean);
                }
            }
        }
        CoeusVector cvCoInvestigators = new CoeusVector();
        cvCoInvestigators.add(cvCurrCoInvestigators);
        cvCoInvestigators.add(cvPrevCoInvestigators);
        hmProtocolHistory.put("Co Investigators",cvCoInvestigators);
        //Added for Case# 3087 - In Premium - Review History Add the following elements - End
    }
    
    //Added for Case# 3087 - In Premium - Review History Add the following elements - Start
    /*
     * Populates the Special Review change details into the hmProtocolHistory
     * @param protocolHistoryBean - contains data of current and previous sequences of the protocol
     * @param hmProtocolHistory - contains the history of changes in the protocol
     */    
    public void populateSpecialReview(ProtocolHistoryBean protocolHistoryBean,TreeMap hmProtocolHistory){
        
        ProtocolInfoBean currentProtocol = protocolHistoryBean.getCurrProtocolInfoBean();
        ProtocolInfoBean prevProtocol = protocolHistoryBean.getPrevProtocolInfoBean();       
        ProtocolSpecialReviewFormBean protocolSpecialReviewFormBean = null;
        Vector currSpecialReviewList = currentProtocol.getSpecialReviews();
        Vector prevSpecialReviewList = prevProtocol.getSpecialReviews();
        CoeusVector cvCurrSpecialReview= new CoeusVector();
        CoeusVector cvPrevSpecialReview = new CoeusVector();
        if(currSpecialReviewList!=null){
            for(int i=0; i < currSpecialReviewList.size();i++){                
                protocolSpecialReviewFormBean = (ProtocolSpecialReviewFormBean)currSpecialReviewList.get(i);
                ProtocolSpecialReviewFormBean currSpecialReview =  new ProtocolSpecialReviewFormBean();
                currSpecialReview = protocolSpecialReviewFormBean;
                cvCurrSpecialReview.add(currSpecialReview);
            }
        }        
        if(prevSpecialReviewList!=null){
            for(int i=0; i < prevSpecialReviewList.size();i++){               
                protocolSpecialReviewFormBean = (ProtocolSpecialReviewFormBean)prevSpecialReviewList.get(i);
                ProtocolSpecialReviewFormBean prevSpecialReview = new ProtocolSpecialReviewFormBean();                
                prevSpecialReview = protocolSpecialReviewFormBean;
                cvPrevSpecialReview.add(prevSpecialReview);
            }
        }
        CoeusVector cvSpecialReview = new CoeusVector();
        cvSpecialReview.add(cvCurrSpecialReview);
        cvSpecialReview.add(cvPrevSpecialReview);
        hmProtocolHistory.put("Special Review",cvSpecialReview);
    }
    
    /*
     * Populates the Protocol Subjects change details into the hmProtocolHistory
     * @param protocolHistoryBean - contains data of current and previous sequences of the protocol
     * @param hmProtocolHistory - contains the history of changes in the protocol
     *
     */
    public void populateSubjects(ProtocolHistoryBean protocolHistoryBean,TreeMap hmProtocolHistory){
        
        ProtocolInfoBean currentProtocol = protocolHistoryBean.getCurrProtocolInfoBean();
        ProtocolInfoBean prevProtocol = protocolHistoryBean.getPrevProtocolInfoBean();
        ProtocolVulnerableSubListsBean protoVulnerableSubBean = null;
        Vector currVulnerableSubList = currentProtocol.getVulnerableSubjectLists();
        Vector prevVulnerableSubList = prevProtocol.getVulnerableSubjectLists();
        CoeusVector cvCurrSubjects = new CoeusVector();
        CoeusVector cvPrevSubjects = new CoeusVector();
        if(currVulnerableSubList!=null){
            for(int i=0; i < currVulnerableSubList.size();i++){                
                protoVulnerableSubBean = (ProtocolVulnerableSubListsBean)currVulnerableSubList.get(i);
                ProtocolVulnerableSubListsBean currVulnerableSub =  new ProtocolVulnerableSubListsBean();
                currVulnerableSub = protoVulnerableSubBean;
                cvCurrSubjects.add(currVulnerableSub);
            }
        }        
        if(prevVulnerableSubList!=null){
            for(int i=0; i < prevVulnerableSubList.size();i++){               
                protoVulnerableSubBean = (ProtocolVulnerableSubListsBean)prevVulnerableSubList.get(i);
                ProtocolVulnerableSubListsBean prevVulnerableSub = new ProtocolVulnerableSubListsBean();                
                prevVulnerableSub = protoVulnerableSubBean;
                cvPrevSubjects.add(prevVulnerableSub);
            }
        }
        CoeusVector cvVulnerableSubjects = new CoeusVector();
        cvVulnerableSubjects.add(cvCurrSubjects);
        cvVulnerableSubjects.add(cvPrevSubjects);
        hmProtocolHistory.put("Subjects",cvVulnerableSubjects);
    }
    
    /*
     * Populates the Protocol Area of Research change details into the hmProtocolHistory
     * @param protocolHistoryBean - contains data of current and previous sequences of the protocol
     * @param hmProtocolHistory - contains the history of changes in the protocol
     */
    public void populateAreaOfResearch(ProtocolHistoryBean protocolHistoryBean,TreeMap hmProtocolHistory){
        
        ProtocolInfoBean currentProtocol = protocolHistoryBean.getCurrProtocolInfoBean();
        ProtocolInfoBean prevProtocol = protocolHistoryBean.getPrevProtocolInfoBean();
        ProtocolReasearchAreasBean protocolResearchAreasBean = null;
        Vector currResearchAreasList = currentProtocol.getAreaOfResearch();
        Vector prevResearchAreasList = prevProtocol.getAreaOfResearch();
        CoeusVector cvCurrResearchArea = new CoeusVector();
        CoeusVector cvPrevResearchArea = new CoeusVector();
        if(currResearchAreasList!=null){
            for(int i=0; i < currResearchAreasList.size();i++){                
                protocolResearchAreasBean = (ProtocolReasearchAreasBean)currResearchAreasList.get(i);
                ProtocolReasearchAreasBean currResearchAreasBean =  new ProtocolReasearchAreasBean();
                currResearchAreasBean = protocolResearchAreasBean;
                cvCurrResearchArea.add(currResearchAreasBean);
            }
        }        
        if(prevResearchAreasList!=null){
            for(int i=0; i < prevResearchAreasList.size();i++){               
                protocolResearchAreasBean = (ProtocolReasearchAreasBean)prevResearchAreasList.get(i);
                ProtocolReasearchAreasBean prevResearchAreasBean = new ProtocolReasearchAreasBean();                
                prevResearchAreasBean = protocolResearchAreasBean;
                cvPrevResearchArea.add(prevResearchAreasBean);
            }
        }
        CoeusVector cvResearchArea = new CoeusVector();
        cvResearchArea.add(cvCurrResearchArea);
        cvResearchArea.add(cvPrevResearchArea);
        hmProtocolHistory.put("Area Of Research",cvResearchArea);        
    }
    
    /*
     * Populates the Protocol Correspondents change details into the hmProtocolHistory
     * @param protocolHistoryBean - contains data of current and previous sequences of the protocol
     * @param hmProtocolHistory - contains the history of changes in the protocol
     */
    public void populateCorrespondents(ProtocolHistoryBean protocolHistoryBean,TreeMap hmProtocolHistory){
        
        ProtocolInfoBean currentProtocol = protocolHistoryBean.getCurrProtocolInfoBean();
        ProtocolInfoBean prevProtocol = protocolHistoryBean.getPrevProtocolInfoBean();
        ProtocolCorrespondentsBean protocolCorrespondentsBean = null;
        Vector currCorrespondentsList = currentProtocol.getCorrespondetns();
        Vector prevCorrespondentsList = prevProtocol.getCorrespondetns();
        CoeusVector cvCurrCorrespondants = new CoeusVector();
        CoeusVector cvPrevCorrespondants = new CoeusVector();
        if(currCorrespondentsList!=null){
            for(int i=0; i < currCorrespondentsList.size();i++){                
                protocolCorrespondentsBean = (ProtocolCorrespondentsBean)currCorrespondentsList.get(i);
                ProtocolCorrespondentsBean currCorrespondentsBean = new ProtocolCorrespondentsBean();
                currCorrespondentsBean = protocolCorrespondentsBean;
                cvCurrCorrespondants.add(currCorrespondentsBean);
            }
        }        
        if(prevCorrespondentsList!=null){
            for(int i=0; i < prevCorrespondentsList.size();i++){               
                protocolCorrespondentsBean = (ProtocolCorrespondentsBean)prevCorrespondentsList.get(i);
                ProtocolCorrespondentsBean prevCorrespondentsBean = new ProtocolCorrespondentsBean();
                prevCorrespondentsBean = protocolCorrespondentsBean;
                cvPrevCorrespondants.add(prevCorrespondentsBean);
            }
        }
        CoeusVector cvCorrespondents = new CoeusVector();
        cvCorrespondents.add(cvCurrCorrespondants);
        cvCorrespondents.add(cvPrevCorrespondants);
        hmProtocolHistory.put("Correspondents",cvCorrespondents);
    }
    
    /*
     * Populates the Protocol Organization change details into the hmProtocolHistory
     * @param protocolHistoryBean - contains data of current and previous sequences of the protocol
     * @param hmProtocolHistory - contains the history of changes in the protocol
     */
    public void populateOrganization(ProtocolHistoryBean protocolHistoryBean,TreeMap hmProtocolHistory){
        
        ProtocolInfoBean currentProtocol = protocolHistoryBean.getCurrProtocolInfoBean();
        ProtocolInfoBean prevProtocol = protocolHistoryBean.getPrevProtocolInfoBean();
        ProtocolLocationListBean  protocolLocationListBean = null;
        Vector currLocationsList = currentProtocol.getLocationLists();
        Vector prevLocationsList = prevProtocol.getLocationLists();        
        ProtocolLocationListBean prevLocationBean = null;
        CoeusVector cvCurrentLocation = new CoeusVector();
        CoeusVector cvPrevLocation = new CoeusVector();
         if(currLocationsList!=null){
            for(int i=0; i < currLocationsList.size();i++){                
                protocolLocationListBean = (ProtocolLocationListBean)currLocationsList.get(i);
                ProtocolLocationListBean currKeyPersonBean = new ProtocolLocationListBean();
                currKeyPersonBean = protocolLocationListBean;
                cvCurrentLocation.add(currKeyPersonBean);
            }
        }
        if(prevLocationsList!=null){
            for(int i=0; i < prevLocationsList.size();i++){                
                protocolLocationListBean = (ProtocolLocationListBean)prevLocationsList.get(i);
                ProtocolLocationListBean prevKeyPersonBean = new ProtocolLocationListBean();
                prevKeyPersonBean = protocolLocationListBean;
                cvPrevLocation.add(prevKeyPersonBean);
            }
        }
        CoeusVector cvOrganization  = new CoeusVector();
        cvOrganization.add(cvCurrentLocation);
        cvOrganization.add(cvPrevLocation);
        hmProtocolHistory.put("Organization",cvOrganization);                
 
    }
    
    /*
     * Populates the Funding Sourcechange details into the hmProtocolHistory
     * @param protocolHistoryBean - contains data of current and previous sequences of the protocol
     * @param hmProtocolHistory - contains the history of changes in the protocol
     */
    public void populateFundingSource(ProtocolHistoryBean protocolHistoryBean, TreeMap hmProtocolHistory){
        
        ProtocolInfoBean currentProtocol = protocolHistoryBean.getCurrProtocolInfoBean();
        ProtocolInfoBean prevProtocol = protocolHistoryBean.getPrevProtocolInfoBean();
        ProtocolFundingSourceBean protocolFundingSourceBean = null;
        Vector currFundingSourceList = currentProtocol.getFundingSources();
        Vector prevFundingSourceList = prevProtocol.getFundingSources();    
        CoeusVector cvCurrentFundingSrc = new CoeusVector();
        CoeusVector cvPrevFundingSrc = new CoeusVector();
        if(currFundingSourceList!=null){
            for(int i=0; i < currFundingSourceList.size();i++){
                ProtocolFundingSourceBean currFundingSrcBean = new ProtocolFundingSourceBean();
                protocolFundingSourceBean = (ProtocolFundingSourceBean)currFundingSourceList.get(i);
                    currFundingSrcBean = protocolFundingSourceBean;
                    cvCurrentFundingSrc.add(currFundingSrcBean);
            }
        }
        if(prevFundingSourceList!=null){
            for(int i=0; i < prevFundingSourceList.size();i++){
                ProtocolFundingSourceBean prevFundingSrcBean = new ProtocolFundingSourceBean();
                protocolFundingSourceBean = (ProtocolFundingSourceBean)prevFundingSourceList.get(i);
                    prevFundingSrcBean = protocolFundingSourceBean;
                    cvPrevFundingSrc.add(prevFundingSrcBean);

            }
        }
        CoeusVector cvFundingSrc = new CoeusVector();
        cvFundingSrc.add(cvCurrentFundingSrc);
        cvFundingSrc.add(cvPrevFundingSrc);
        hmProtocolHistory.put("Funding Source",cvFundingSrc);
    }
    //Added for Case# 3087 - In Premium - Review History Add the following elements - End
    
    
    /**
     * Populates the Key Persons group change details into the hmProtocolHistory
     *
     * @param protcolHistoryBean - instance of ProtocolHistoryBean
     */  
    //Modified and Added for Case# 3087 - In Premium - Review History Add the following elements - Start
//    public void populateKeyPersonGroup(ProtocolHistoryBean protocolHistoryBean ){
    public void populateKeyPersonGroup(ProtocolHistoryBean protocolHistoryBean,TreeMap hmProtocolHistory ){
//        ProtocolInfoBean currentProtocol = protocolHistoryBean.getCurrProtocolInfoBean();
//        ProtocolInfoBean prevProtocol = protocolHistoryBean.getPrevProtocolInfoBean();
//        
//        ChangeHistoryGroup keyPersonGroup = new ChangeHistoryGroup();
//        ChangeHistoryBean changeHistoryBean = null;
//        keyPersonGroup.setName("Key Study Persons");
//        
//
//        Vector currKeyPersonsList = currentProtocol.getKeyStudyPersonnel();
//        Vector prevKeyPersonsList = prevProtocol.getKeyStudyPersonnel();
//        ProtocolKeyPersonnelBean currKeyPersonBean = null;
//        ProtocolKeyPersonnelBean prevKeyPersonBean = null;
//        
//        //Finds the investigators which are new and not modified
//        boolean found = false;
//        if(prevKeyPersonsList == null && currKeyPersonsList != null){
//            for(int i = 0; i < currKeyPersonsList.size(); i++){
//                currKeyPersonBean = (ProtocolKeyPersonnelBean)currKeyPersonsList.get(i);
//                changeHistoryBean= new ChangeHistoryBean(EMPTY_STRING, currKeyPersonBean.getPersonName(), STRING, 2);
//                keyPersonGroup.getHistoryBeanList().add(changeHistoryBean);
//            }
//        }else if(prevKeyPersonsList != null && currKeyPersonsList == null){
//            for(int i = 0; i < prevKeyPersonsList.size(); i++){
//                prevKeyPersonBean = (ProtocolKeyPersonnelBean)prevKeyPersonsList.get(i);
//                changeHistoryBean= new ChangeHistoryBean(EMPTY_STRING, prevKeyPersonBean.getPersonName(), STRING, 3);
//                keyPersonGroup.getHistoryBeanList().add(changeHistoryBean);
//
//            }
//        }else if(prevKeyPersonsList!=null && currKeyPersonsList!=null){
//            for(int i = 0; i < currKeyPersonsList.size(); i++){
//                currKeyPersonBean = (ProtocolKeyPersonnelBean)currKeyPersonsList.get(i);
//                found = false;
//                for(int j = 0; j < prevKeyPersonsList.size(); j++){
//                    prevKeyPersonBean =(ProtocolKeyPersonnelBean)prevKeyPersonsList.get(j);
//                    if(currKeyPersonBean.getPersonId().equals(prevKeyPersonBean.getPersonId())){
//                        found = true;
//                        changeHistoryBean= new ChangeHistoryBean(EMPTY_STRING, currKeyPersonBean.getPersonName(), STRING, 0);
//                        break;
//                    }
//                }
//                if(!found){
//                    changeHistoryBean= new ChangeHistoryBean(EMPTY_STRING, currKeyPersonBean.getPersonName(), STRING, 2);
//                }
//                keyPersonGroup.getHistoryBeanList().add(changeHistoryBean);
//            }
//            //Finds the keyPersons which are deleted
//            for(int i = 0; i < prevKeyPersonsList.size(); i++){
//                prevKeyPersonBean = (ProtocolKeyPersonnelBean)prevKeyPersonsList.get(i);
//                found = false;
//                for(int j = 0; j < currKeyPersonsList.size(); j++){
//                    currKeyPersonBean =(ProtocolKeyPersonnelBean)currKeyPersonsList.get(j);
//                    if(currKeyPersonBean.getPersonId().equals(prevKeyPersonBean.getPersonId())){
//                        found = true;
//                        break;
//                    }
//                }
//                if(!found){
//                    changeHistoryBean= new ChangeHistoryBean(EMPTY_STRING, prevKeyPersonBean.getPersonName(), STRING, 3);
//                    keyPersonGroup.getHistoryBeanList().add(changeHistoryBean);
//                }
//                
//            }
//        }
//        if(keyPersonGroup.getHistoryBeanList().size() ==  0){
//            //Add a bean into the keyPerson group with dummy values if there is no keypersons
//            changeHistoryBean = new ChangeHistoryBean(EMPTY_STRING, EMPTY_STRING, STRING, 0);
//        }
//        protocolHistoryBean.getChangeHistoryGroupList().add(keyPersonGroup);
        
        //Modified and Added for Case# 3087 - In Premium - Review History Add the following elements - End
        //Added for Case# 3087 - In Premium - Review History Add the following elements - Start
        ProtocolInfoBean currentProtocol = protocolHistoryBean.getCurrProtocolInfoBean();
        ProtocolInfoBean prevProtocol = protocolHistoryBean.getPrevProtocolInfoBean();
        ProtocolKeyPersonnelBean protoKeyPersonnelBean = null;
        Vector currKeyPersonsList = currentProtocol.getKeyStudyPersonnel();
        Vector prevKeyPersonsList = prevProtocol.getKeyStudyPersonnel();
        CoeusVector cvCurrentKeyPerson = new CoeusVector();
        CoeusVector cvPrevKeyPerson = new CoeusVector();        
         if(currKeyPersonsList!=null){
            for(int i=0; i < currKeyPersonsList.size();i++){
                ProtocolKeyPersonnelBean currKeyPersonBean = new ProtocolKeyPersonnelBean();
                protoKeyPersonnelBean = (ProtocolKeyPersonnelBean)currKeyPersonsList.get(i);
                    currKeyPersonBean = protoKeyPersonnelBean;
                    cvCurrentKeyPerson.add(currKeyPersonBean);
            }
        }        
        if(prevKeyPersonsList!=null){
            for(int i=0; i < prevKeyPersonsList.size();i++){
                ProtocolKeyPersonnelBean prevKeyPersonBean = new ProtocolKeyPersonnelBean();
                protoKeyPersonnelBean = (ProtocolKeyPersonnelBean)prevKeyPersonsList.get(i);
                    prevKeyPersonBean = protoKeyPersonnelBean;
                    cvPrevKeyPerson.add(prevKeyPersonBean);
            }
        }
        CoeusVector cvKeyPerson  = new CoeusVector();
        cvKeyPerson.add(cvCurrentKeyPerson);
        cvKeyPerson.add(cvPrevKeyPerson);
        hmProtocolHistory.put("Key Study Persons",cvKeyPerson);        
        // Added for Case# 3087 - In Premium - Review History Add the following elements - End
    }
    
    /**
     * Populates the Attachment change details into the hmProtocolHistory
     *
     * @param protcolHistoryBean - instance of ProtocolHistoryBean
     */
//Modified and Added for Case# 3087 - In Premium - Review History Add the following elements - Start    
//    public void populateAttachmentGroup(ProtocolHistoryBean protocolHistoryBean)
    public void populateAttachmentGroup(ProtocolHistoryBean protocolHistoryBean,TreeMap hmProtocolHistory)
    throws DBException{
//        ProtocolDataTxnBean protoDataTxnBean = new ProtocolDataTxnBean();
//        ChangeHistoryGroup attachmentGroup = new ChangeHistoryGroup();
//        attachmentGroup.setName("Attachments");
//        ChangeHistoryBean changeHistoryBean = null;
//        int currSequence = protocolHistoryBean.getCurrProtocolInfoBean().getSequenceNumber();
//        int prevSequence = protocolHistoryBean.getPrevProtocolInfoBean().getSequenceNumber();
//        
//        Vector attachmentList = protoDataTxnBean.getUploadDocumentForProtocol(protocolHistoryBean.getProtocolNumber());
//        if(attachmentList!=null){
//            UploadDocumentBean documentBean = null;
//            UploadDocumentBean historyDocumentBean = null;
//            Vector historyList = null;
//            boolean foundInCurrSequence = false;
//            boolean foundInPrevSequence = false;
//            int currStatusCode = 0, prevStatusCode = 0;
//            for(int i=0;i<attachmentList.size();i++){
//                documentBean = (UploadDocumentBean)attachmentList.get(i);
//                if(documentBean.getSequenceNumber()<=currSequence){
//                    historyList = protoDataTxnBean.getProtocolHistoryData(
//                            protocolHistoryBean.getProtocolNumber(),Integer.toString(documentBean.getDocumentId()));
//                    if(historyList!=null){
//                        foundInCurrSequence = false;
//                        foundInPrevSequence = false;
//                        changeHistoryBean = null;
//                        for(int j=0;j<historyList.size();j++){
//                            historyDocumentBean = (UploadDocumentBean)historyList.get(j);
//                            if(historyDocumentBean.getSequenceNumber()== currSequence){
//                                foundInCurrSequence = true;
//                                currStatusCode = historyDocumentBean.getStatusCode();
//                            }else if(historyDocumentBean.getSequenceNumber()== prevSequence){
//                                foundInPrevSequence = true;
//                                prevStatusCode = historyDocumentBean.getStatusCode();
//                            }
//                            if(foundInCurrSequence && foundInPrevSequence){
//                                break;
//                            }
//                        }//End of for loop of historyList
//                        if(foundInCurrSequence && foundInPrevSequence){
//                            if(currStatusCode == 3){
//                                changeHistoryBean = new ChangeHistoryBean(EMPTY_STRING, documentBean.getDocType()+":"+documentBean.getDescription(),STRING, 3);
//                            }else {
//                                changeHistoryBean = new ChangeHistoryBean(EMPTY_STRING, documentBean.getDocType()+":"+documentBean.getDescription(),STRING, 1);
//                            }
//                        }else if(foundInCurrSequence && !foundInPrevSequence ){
//                            changeHistoryBean = new ChangeHistoryBean(EMPTY_STRING, documentBean.getDocType()+":"+documentBean.getDescription(),STRING, 2);
//                        }else if((!foundInCurrSequence && !foundInPrevSequence && documentBean.getStatusCode()!=3) ||
//                                (!foundInCurrSequence && foundInPrevSequence && prevStatusCode!=3)){
//                            changeHistoryBean = new ChangeHistoryBean(EMPTY_STRING, documentBean.getDocType()+":"+documentBean.getDescription(),STRING, 0);
//                        }
//                        
//                        if(changeHistoryBean!=null){
//                            attachmentGroup.getHistoryBeanList().add(changeHistoryBean);
//                        }
//                    }
//                }
//            }//End of for loop of attachmentList
//        }//End of if(attachmentList!=null)
//        if(attachmentGroup.getHistoryBeanList().size()==0){
//            changeHistoryBean = new ChangeHistoryBean(EMPTY_STRING,EMPTY_STRING,STRING,0);
//            attachmentGroup.getHistoryBeanList().add(changeHistoryBean);
//        }
//        protocolHistoryBean.getChangeHistoryGroupList().add(attachmentGroup);
        //Modified and Added for Case# 3087 - In Premium - Review History Add the following elements - End
        
        //Added for Case# 3087 - In Premium - Review History Add the following elements - Start
        ProtocolDataTxnBean protoDataTxnBean = new ProtocolDataTxnBean();  
        ProtocolInfoBean currentProtocol = protocolHistoryBean.getCurrProtocolInfoBean();
        ProtocolInfoBean prevProtocol = protocolHistoryBean.getPrevProtocolInfoBean();       
        UploadDocumentBean uploadDocumentBean = null;
        Vector currAttachmentList = protoDataTxnBean.getProtocolDocuments(currentProtocol.getProtocolNumber(),currentProtocol.getSequenceNumber());
        Vector prevAttachmentList = protoDataTxnBean.getProtocolDocuments(prevProtocol.getProtocolNumber(),prevProtocol.getSequenceNumber());
        CoeusVector cvCurrAttachments = new CoeusVector();
        CoeusVector cvPrevAttachments = new CoeusVector();
        if(currAttachmentList!=null){
            for(int i=0; i < currAttachmentList.size();i++){                
                uploadDocumentBean = (UploadDocumentBean)currAttachmentList.get(i);
                UploadDocumentBean currUploadDocumentBean =  new UploadDocumentBean();
                currUploadDocumentBean = uploadDocumentBean;
                cvCurrAttachments.add(currUploadDocumentBean);
            }
        }        
        if(prevAttachmentList!=null){
            for(int i=0; i < prevAttachmentList.size();i++){               
                uploadDocumentBean = (UploadDocumentBean)prevAttachmentList.get(i);
                UploadDocumentBean prevUploadDocumentBean = new UploadDocumentBean();                
                prevUploadDocumentBean = uploadDocumentBean;
                cvPrevAttachments.add(prevUploadDocumentBean);
            }
        }
        CoeusVector cvAttachments = new CoeusVector();
        cvAttachments.add(cvCurrAttachments);
        cvAttachments.add(cvPrevAttachments);
        hmProtocolHistory.put("Attachments",cvAttachments);
        //Added for Case# 3087 - In Premium - Review History Add the following elements - End
    }
    /**
     *  Get the last action performed which caused the creation of the protocol sequence.
     *
     *  @param protocolHistoryBean instance of ProtocolHistoryBean
     */
    public String getProtocolLastAction(ProtocolHistoryBean protocolHistoryBean)
    throws CoeusException, DBException {
        String lastAction = null;
        Vector param= new Vector();
        Vector result = new Vector();
        param.add(new Parameter("PROTOCOL_NUMBER",
                DBEngineConstants.TYPE_STRING,protocolHistoryBean.getProtocolNumber()));
        param.addElement(new Parameter("SEQUENCE_NUMBER",
                DBEngineConstants.TYPE_INT,protocolHistoryBean.getSequenceNumber()));
        /* calling stored function */
        if(dbEngine!=null){
            result = dbEngine.executeFunctions("Coeus",
                    "{ <<OUT STRING LAST_ACTION>> = "
                    +" call FN_GET_LAST_ACTION_ON_PROTOCOL(<< PROTOCOL_NUMBER >> ,<< SEQUENCE_NUMBER >> ) }", param);
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }
        if(!result.isEmpty()){
            HashMap rowParameter = (HashMap)result.elementAt(0);
            lastAction = rowParameter.get("LAST_ACTION").toString();
        }
        return lastAction;
    }
}
