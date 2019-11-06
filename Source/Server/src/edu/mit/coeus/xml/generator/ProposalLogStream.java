/*
 * @(#)ProposalLogStream.java September 20, 2004, 3:27 PM
 *
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 */

package edu.mit.coeus.xml.generator;

import edu.mit.coeus.exception.CoeusException;
import edu.mit.coeus.instprop.bean.InstituteProposalBean;
import edu.mit.coeus.instprop.bean.InstituteProposalLogBean;
import edu.mit.coeus.instprop.bean.InstituteProposalTxnBean;
import edu.mit.coeus.instprop.bean.TempProposalMergeLogBean;
import edu.mit.coeus.utils.CoeusVector;
import edu.mit.coeus.utils.dbengine.DBException;
import edu.mit.coeus.utils.xml.bean.proposalLog.*;
import java.math.BigInteger;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Hashtable;
import java.text.*;

/**
 *
 * @author  Geo Thomas
 * @Created on September 20, 2004, 3:27 PM
 */
public class ProposalLogStream extends ReportBaseStream{
    private InstituteProposalTxnBean instPropTxnBean;
    private ObjectFactory objFactory;
    private CoeusXMLGenrator xmlGenerator;
    private final HashMap status = new HashMap();
    private static final String packageName = "edu.mit.coeus.utils.xml.bean.proposalLog";
    // Added for COEUSQA-1471_show institute proposal for merged proposal logs_start
    private static final String TEMPORARY_PROPOSAL_LOG_STATUS = "T";
    private static final String DISCLOSURE_PROPOSAL_LOG_STATUS = "D";
    // Added for COEUSQA-1471_show institute proposal for merged proposal logs_end
    
    /** Creates a new instance of ProposalLogStream */
    public ProposalLogStream() {
        objFactory = new ObjectFactory();;
        status.put("P", "Pending");
        status.put("M", "Merged");
        status.put("S", "Submitted");
        status.put("T", "Temporary");
        status.put("V", "Void");
        xmlGenerator = new CoeusXMLGenrator();
        
    }
    
    public org.w3c.dom.Document getStream(java.util.Hashtable params) throws CoeusException, DBException {
        String propNumber = (String)params.get("PROPOSAL_NUMBER");
//        ProposalLog proposalLog = getProposalLog(propNumber);
        ProposalLogType proposalLogType = getProposalLogType(propNumber);
        return xmlGenerator.marshelObject(proposalLogType,packageName);
    }
    
    public Object getObjectStream(Hashtable params) throws DBException,CoeusException{
        String propNumber = (String)params.get("PROPOSAL_NUMBER");
        ProposalLogType proposalLogType = getProposalLogType(propNumber);
        return proposalLogType;
    }
    
    private ProposalLogType getProposalLogType(String propNumber) throws DBException, CoeusException{
        instPropTxnBean = new InstituteProposalTxnBean();
        InstituteProposalLogBean instPropLogBean = instPropTxnBean.getInstituteProposalLog(propNumber);
        ProposalLogType proposalLog = null;
        try{
            proposalLog = objFactory.createProposalLog();
            proposalLog.setComments(checkNull(instPropLogBean.getComments()));
            proposalLog.setProposalNumber(checkNull(instPropLogBean.getProposalNumber()));
            proposalLog.setProposalTitle(checkNull(instPropLogBean.getTitle()));
            proposalLog.setUpdateUser(checkNull(instPropLogBean.getUserName()));
            // Added for COEUSQA-1471_show institute proposal for merged proposal logs
            String proposalNumber = instPropLogBean.getProposalNumber().trim();
            String mergedWith = "";
            StringBuffer mergedWithProposal = new StringBuffer();
            if(proposalNumber.startsWith(TEMPORARY_PROPOSAL_LOG_STATUS) ||
                    proposalNumber.startsWith(DISCLOSURE_PROPOSAL_LOG_STATUS)){
                CoeusVector cvMergedData = instPropTxnBean.getMergedDataForProposalLog(proposalNumber);
                if(cvMergedData != null && !cvMergedData.isEmpty()){
                    for(int index=0;index<cvMergedData.size();index++){
                        mergedWith = (String)cvMergedData.get(index);
                    }
                }
            }else{
                CoeusVector cvMergedData = instPropTxnBean.getMergedDataForProposal(proposalNumber);
                if(cvMergedData != null && !cvMergedData.isEmpty()){
                    for(int index=0;index<cvMergedData.size();index++){
                        TempProposalMergeLogBean proposalMergedlogBean = (TempProposalMergeLogBean)cvMergedData.get(index);
                        mergedWithProposal.append(proposalMergedlogBean.getTempProposalNumber());
                        if(index < cvMergedData.size()-1){ // For the last proposal ',' wont be appended
                            mergedWithProposal.append(",");
                        }
                        
                    }
                    mergedWith = mergedWithProposal.toString();
                }
                
            }
            proposalLog.setMergedWith(mergedWith);
            // Added for COEUSQA-1471_show institute proposal for merged proposal logs
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM/dd/yyyy hh:mm a");
            proposalLog.setUpdateTimeStamp(simpleDateFormat.format(instPropLogBean.getUpdateTimestamp()));
            //case 3263 start
            proposalLog.setCreateUser(checkNull(instPropLogBean.getCreateUserName()));
//            simpleDateFormat = new SimpleDateFormat("MM/dd/yyyy"); //case 3436
            proposalLog.setCreateTimeStamp(simpleDateFormat.format(instPropLogBean.getCreateTimestamp()));
            //case 3436 start
            simpleDateFormat = new SimpleDateFormat("MM/dd/yyyy");
            if (instPropLogBean.getDeadlineDate()!= null)
            //case 3436 end 
            proposalLog.setDeadlinedate(simpleDateFormat.format(instPropLogBean.getDeadlineDate()));
            //case 3263 end
            LeadUnit leadUnit = objFactory.createLeadUnit();
            leadUnit.setUnitNumber(checkNull(instPropLogBean.getLeadUnit()));
            leadUnit.setUnitName(checkNull(instPropLogBean.getUnitName()));
            proposalLog.setLeadUnit(leadUnit);
            
            PrincipalInvestigator principalInvstigator =  objFactory.createPrincipalInvestigator();
            principalInvstigator.setPersonID(checkNull(instPropLogBean.getPrincipleInvestigatorId()));
            principalInvstigator.setFullName(instPropLogBean.getPrincipleInvestigatorName());
            proposalLog.setPI(principalInvstigator);
            
            ProposalType propType =  objFactory.createProposalType();
            BigInteger typeCode = instPropLogBean.getProposalTypeCode()==0?null:
                BigInteger.valueOf(instPropLogBean.getProposalTypeCode());
            propType.setProposalTypeCode(typeCode);
            propType.setProposalTypeDesc(checkNull(instPropLogBean.getProposalTypeDescription()));
            proposalLog.setProposalType(propType);
            
            proposalLog.setStatus(status.get(""+instPropLogBean.getLogStatus()).toString());
            
            Sponsor sponsor = objFactory.createSponsor();
            sponsor.setSponsorCode(checkNull(instPropLogBean.getSponsorCode()));
            sponsor.setSponsorName(checkNull(instPropLogBean.getSponsorName()));
            proposalLog.setSponsor(sponsor);
        }catch (Exception exception) {
            CoeusException coeusException = new CoeusException(exception.getMessage());
            throw coeusException;
        }
        return proposalLog;
    }
    private String checkNull(String obj){
        return obj==null?"":obj;
    }
    public static void main(String args[]) throws Exception{
        ProposalLogStream propLogStrm = new ProposalLogStream();
        Hashtable ht = new Hashtable();
        ht.put("PROPOSAL_NUMBER", "02091471");
        propLogStrm.getStream(ht);
    }
}
