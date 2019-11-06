/*
 * @(#)DisclosureValidationBean.java 1.0 4/04/02
 *
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 */
package edu.mit.coeus.coi.bean;

import java.sql.SQLException;
import java.util.Vector;
import java.util.Hashtable;
/* CASE #748 Begin */
import java.util.HashMap;
/* CASE #748 End */
import java.util.LinkedList;
import edu.mit.coeus.utils.dbengine.*;
import edu.mit.coeus.utils.UtilFactory;
/* CASE #734 Comment Begin */
//import edu.mit.coeus.coi.exception.CoeusException;
/* CASE #734 Comment End */
/* CASE #734 Begin */
import edu.mit.coeus.exception.CoeusException;
/* CASE #734 End */
/**
 *  This class is used for validating the disclosure before creating a new one.
 *
 * @version 1.0 April 1, 2002, 4:46 PM
 * @author  Geo Thomas
 */
public class DisclosureValidationBean {
    private DBEngineImpl db;
//    private UtilFactory UtilFactory;
    /** Creates new DisclosureValdation */
    public DisclosureValidationBean(){
        //db = DBEngineImpl.getInstance();
        db = new DBEngineImpl();
//        UtilFactory = new UtilFactory();
    }
    /**
     *  This is the method to validate the award number. This method will be called when the user
     *  selects the disclosure type as "award" for creating a new disclosure.
     *  <li>To check validity, it uses the function fn_is_valid_award_num.
     *  @param String Award Number
     *  @return boolean Validity
     *  @exception DBException
     */
    public boolean isAwardNumValid(String awardNum) throws DBException{
        Vector result = new Vector(3,2);
        boolean validFlag = false;
        Vector param= new java.util.Vector();
        param.addElement(new Parameter("MIT_AWARD_NUMBER","String",awardNum));
        //calling stored function
        if(db!=null){
            result = db.executeFunctions("Coeus",
              "{ <<OUT INTEGER rowCount>> = call fn_is_valid_award_num ( <<MIT_AWARD_NUMBER>>) }",
              param);
        }else{
            throw new DBException("exceptionCode.10001");
        }
        /* case #748 comment begin */
        /*if(result!=null){
            Hashtable awardCountRow = (Hashtable)result.elementAt(0);
            String awardCount = (String)awardCountRow.get("rowCount");
            if(awardCount!=null && Integer.parseInt(awardCount)>0){
                validFlag = true;
            }
        }*/
        /* case #748 comment end */
        /* case #748 begin */
        if(result!=null){
            HashMap awardCountRow = (HashMap)result.elementAt(0);
            String awardCount = awardCountRow.get("rowCount").toString();
            if(Integer.parseInt(awardCount)>0){
                validFlag = true;
            }
        }
        /* case #748 end */
        return validFlag;
    }
    /**
     *  The method to validate the proposal number. This method will be called when the user
     *  selects the disclosure type as "proposal" for creating a new disclosure.
     *  <li>To check validity, it uses the function fn_is_valid_inst_prop_num.
     *  @param String Proposal Number
     *  @return boolean Validity
     *  @exception DBException
     */
    public boolean isProposalNumValid(String proposalNum) throws DBException{
        Vector result = new Vector(3,2);
        boolean validFlag = false;
        Vector param= new java.util.Vector();
        param.addElement(new Parameter("PROPOSAL_NUMBER","String",proposalNum));
        //calling stored function
        if(db!=null){
            result = db.executeFunctions("Coeus",
              "{ <<OUT INTEGER rowCount>> = call fn_is_valid_inst_prop_num ( <<PROPOSAL_NUMBER>>) }",
              param);
        }else{
            throw new DBException("exceptionCode.10001");
        }
        /* case #748 comment begin */
        /*if(result!=null){
            Hashtable rowCount = (Hashtable)result.elementAt(0);
            String ProposalCount = (String)rowCount.get("rowCount");
            if(ProposalCount!=null && Integer.parseInt(ProposalCount)>0){
                validFlag = true;
            }
        }*/
        /* case #748 comment end */
        /* case #748 begin */
        if(result!=null){
            HashMap rowCount = (HashMap)result.elementAt(0);
            String ProposalCount = (String)rowCount.get("rowCount");
            if(ProposalCount!=null && Integer.parseInt(ProposalCount)>0){
                validFlag = true;
            }
        }
        /* case #748 end */
        return validFlag;
    }

    /**
     *  The method to validate the sponsor number. This method will be called when the user
     *  enters a sponsor number in a field and submits the page.
     *  <li>To check validity, it uses the function get_sponsorname.
     *  @param String Sponsor Number
     *  @return boolean Validity
     *  @exception DBException
     */
    public boolean isSponsorNumValid(String sponsorNum, String sponsorName)
            throws  CoeusException,DBException{
        Vector result = new Vector(3,2);
        boolean validFlag = false;
        Vector vectParam= new java.util.Vector();
        vectParam.addElement(new Parameter("SPONSOR_CODE","String",sponsorNum));
        vectParam.addElement(new Parameter("SPONSOR_NAME","String",sponsorName));
        //calling stored function
        if(db!=null){
            result = db.executeFunctions("Coeus",
              "{ <<OUT STRING rowCount>> = call get_sponsorname ( <<SPONSOR_CODE>> , <<SPONSOR_NAME>> ) }",
              vectParam);
        }else{
            throw new DBException("exceptionCode.10001");
        }
        /* case #748 comment begin */
        /*if(result!=null){
            Hashtable rowCount = (Hashtable)result.elementAt(0);
            String sponsorCount = (String)rowCount.get("rowCount");
            if(sponsorCount!=null && !sponsorCount.trim().equals("")){
                validFlag = true;
            }
        }*/
        /* case #748 comment end */
        /* case #748 begin */
        if(result!=null){
            HashMap rowCount = (HashMap)result.elementAt(0);
            String sponsorCount = (String)rowCount.get("rowCount");
            if(sponsorCount!=null && !sponsorCount.trim().equals("")){
                validFlag = true;
            }
        }
        /* case #748 end */
        return validFlag;
    }


    /**
     *  The method to get sponsor name. This method will be called when the user
     *  enters a sponsor number in a field and submits the page.
     *  <li>To check validity, it uses the function get_sponsorname.
     *  @param String Sponsor Number
     *  @return boolean Validity
     *  @exception DBException
     */
    // CASE #748 Method updated to throw only DBException
    public String getSponsorName(String sponsorNum, String sponsorName)
            throws DBException{
        Vector result = new Vector(3,2);
        String spName = null;
        Vector vectParam= new java.util.Vector();
        vectParam.addElement(new Parameter("SPONSOR_CODE","String",sponsorNum));
        vectParam.addElement(new Parameter("SPONSOR_NAME","String",sponsorName));
        //calling stored function
        if(db!=null){
            result = db.executeFunctions("Coeus",
              "{ <<OUT STRING sponsorName>> = call get_sponsorname ( <<SPONSOR_CODE>> , <<SPONSOR_NAME>> ) }",
              vectParam);
        }else{
            throw new DBException("exceptionCode.10001");
        }
        if(result!=null){
            /* case #748 comment begin */
            //Hashtable rowCount = (Hashtable)result.elementAt(0);
            /* case #748 comment end */
            /* case #748 begin */
            HashMap rowCount = (HashMap)result.elementAt(0);
            /* case #748 end */
            String sponsorNm = (String)rowCount.get("sponsorName");
            if(sponsorNm!=null && !sponsorNm.trim().equals("")){
                spName = sponsorNm;
            }
        }
        return spName;
    }

    /**
     *  The method to validate the lead unit number. This method will be called when the user
     *  enters a lead unit number in a field and submits the page.
     *  To check validity, it uses the function FN_GET_UNITNAME.
     *  @param String Lead Unit Number
     *  @return boolean Validity
     *  @exception DBException
     */
    //CASE #748 Method updated to throw only DBException
    public boolean isLeadUnitNumValid(String LeadUnitNum) throws DBException{
        Vector result = new Vector(3,2);
        boolean validFlag = false;
        Vector param= new java.util.Vector();
        param.addElement(new Parameter("UNIT_NUMBER","String",LeadUnitNum));
        if(db!=null){
            result = db.executeFunctions("Coeus",
                "{ <<OUT STRING rowCount>> = call FN_GET_UNITNAME ( <<UNIT_NUMBER>>) }",
                param);
        }else{
            throw new DBException("exceptionCode.10001");
        }
        if(result!=null){
            /* case #748 comment begin */
            //Hashtable rowCount = (Hashtable)result.elementAt(0);
            /* case #748 comment end */
            /* case #748 begin */
            HashMap rowCount = (HashMap)result.elementAt(0);
            /* case #748 end */
            String leadUnitCount = (String)rowCount.get("rowCount");
            if(leadUnitCount!=null && !leadUnitCount.trim().equals("")){
                validFlag = true;
            }
        }
        return validFlag;
    }

    /**
     *  The method to get lead unit name. This method will be called when the user
     *  enters a lead unit number in a field and submits the page.
     *  To check validity, it uses the function FN_GET_UNITNAME.
     *  @param String Lead Unit Number
     *  @return boolean Validity
     *  @exception DBException
     */
    // CASE #748 Method updated to throw only DBException
    public String getLeadUnitName(String LeadUnitNum)
            throws DBException{
        Vector result = new Vector(3,2);
        String leadName = null;
        Vector param= new java.util.Vector();
        param.addElement(new Parameter("UNIT_NUMBER","String",LeadUnitNum));
        if(db!=null){
            result = db.executeFunctions("Coeus",
                "{ <<OUT STRING LEADUNITNAME>> = call FN_GET_UNITNAME ( <<UNIT_NUMBER>>) }",
                param);
        }else{
            throw new DBException("exceptionCode.10001");
        }
        if(result!=null){
            /* case #748 comment begin */
            //Hashtable rowCount = (Hashtable)result.elementAt(0);
            /* case #748 comment end */
            /* case #748 begin */
            HashMap rowCount = (HashMap)result.elementAt(0);
            /* case #748 end */
            String leadUnitName = (String)rowCount.get("LEADUNITNAME");
            if(leadUnitName!=null && !leadUnitName.trim().equals("")){
                leadName = leadUnitName;
            }
        }
        return leadName;
    }
}