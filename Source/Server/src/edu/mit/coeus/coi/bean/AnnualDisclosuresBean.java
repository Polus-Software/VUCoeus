/*
 * @(#)AnnualDisclosuresBean.java 1.0 6/5/02
 *
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 */
package edu.mit.coeus.coi.bean;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Vector;
import java.util.Hashtable;
/* CASE #748 Begin */
import java.util.HashMap;
/* CASE #748 End */
import edu.mit.coeus.utils.dbengine.DBEngineImpl;
import edu.mit.coeus.utils.dbengine.DBException;
import edu.mit.coeus.utils.dbengine.Parameter;
import edu.mit.coeus.utils.UtilFactory;
import edu.mit.coeus.exception.CoeusException;

/**
 * <code>AnnualDisclosuresBean</code> is a class to hold all disclosures
 * information.
 *
 * @version 1.0 June 5,2002
 * @author Phaneendra Kumar.
 */
public class AnnualDisclosuresBean {

    /* CASE #401 Comment Begin */
    //private static final String AWARD_TYPE = "Award";
    //private static final String PROPOSAL_TYPE = "Proposal";
    //private static final String AWARDCODE = "1";
    //private static final String PROPOSALCODE = "2";
    /* CASE #401 Comment End */

    /*
     * Singleton instance of a dbEngine
     */
    private DBEngineImpl dbEngine;

    /*
     * Initialize bean with a particular person id. ie, All methods
     * in this class always belong to a person.
     */
    private String personId;

    /*
     * Initialize bean with Entity Number as one of the parameter
     */
    private String entityNumber;

    /**
     * Creates new AnnualDisclosuresBean with the logged in personId information
     *
     * @param person id
     */
    public AnnualDisclosuresBean(String personId, String entityNumber){
       this.personId = personId;
        this.entityNumber = entityNumber;
        this.dbEngine = new DBEngineImpl();
    }

    /* CASE #401 End */

    /**
     * The method used to get all pending disclosures that attached with
     * a particular FinancialEntity. The method will return a collection
     * of <code>AnnDisclosureDetailsBean</code> instances.
     * It will throw an exception if the person id or entity number
     * for this bean is null.
     * To fetch the data, it uses dw_get_discl_for_person_entity procedure.
     *
     * @return Vector vector of EntityDetails instance for a person
     *
     * @exception DBException
     * @exception CoeusException
     */
    public Vector getPenidngDisclosures() throws DBException,CoeusException{
        if (   personId==null   ) {
            throw new CoeusException("exceptionCode.20001");
        }
        if (  entityNumber == null ) {
            throw new CoeusException("exceptionCode.20002");
        }
        Vector result = new Vector(3,2);
        Vector parameters= new Vector();
        Vector disclosures = new Vector(3,2);
        AnnDisclosureDetailsBean annDisclDetails = null;
        parameters.addElement(new Parameter("PERSON_ID","String",personId.toString()));
        parameters.addElement(new Parameter("ENTITY_NUMBER","String",entityNumber.toString()));
        if(dbEngine!=null){
            result = dbEngine.executeRequest("Coeus",
                        "call dw_get_discl_for_person_entity ( <<PERSON_ID>> , "+
                        "<<ENTITY_NUMBER>> , <<OUT RESULTSET rset>> )", "Coeus",
                        parameters);
        }else{
            throw new DBException("exceptionCode.10001");
        }
        for(int cntDisclosures=0;cntDisclosures<result.size();cntDisclosures++){
            /* case #748 comment begin */
            //Hashtable disclosuresInfo= (Hashtable)result.elementAt(cntDisclosures);
            /* case #748 comment end */
            /* case #748 begin */
            HashMap disclosuresInfo= (HashMap)result.elementAt(cntDisclosures);
            /* case #748 end */
            /* CASE #401 Comment Begin */
            /*
            if (disclosuresInfo.get("MODULE_CODE").toString().equals(AWARDCODE))    {
                annDisclDetails = getAwardInfo(disclosuresInfo.get("MODULE_ITEM_KEY").toString());
                annDisclDetails.setDisclosureFor(AWARD_TYPE);
            }else if (disclosuresInfo.get("MODULE_CODE").toString().equals(PROPOSALCODE))  {
                annDisclDetails = getProposalInfo(disclosuresInfo.get("MODULE_ITEM_KEY").toString());
                annDisclDetails.setDisclosureFor(PROPOSAL_TYPE);
            }
            */
            /* CASE #401 Begin */
            // Stored procedure updated to get title and sponsor and to sort by
            // module code, title, sponsor.
            annDisclDetails = new AnnDisclosureDetailsBean();
            annDisclDetails.setDisclosureFor((String)disclosuresInfo.get("MODULE_DESCRIPTION"));
            annDisclDetails.setTitle((String)disclosuresInfo.get("TITLE"));
            annDisclDetails.setSponsor((String)disclosuresInfo.get("SPONSOR_NAME"));
            /* CASE #401 End */
            annDisclDetails.setDisclosureNumber
                    ((String)disclosuresInfo.get("COI_DISCLOSURE_NUMBER"));
            annDisclDetails.setEntityNumber
                    ((String)disclosuresInfo.get("ENTITY_NUMBER"));
            annDisclDetails.setNumber
                    (disclosuresInfo.get("MODULE_ITEM_KEY") == null ? null :
                    disclosuresInfo.get("MODULE_ITEM_KEY").toString());
            annDisclDetails.setConflictStatus
                    (disclosuresInfo.get("COI_STATUS_CODE") == null ? null :
                    disclosuresInfo.get("COI_STATUS_CODE").toString());
            annDisclDetails.setEntitySeqNumber
                    (disclosuresInfo.get("ENTITY_SEQUENCE_NUMBER") == null ? null :
                    disclosuresInfo.get("ENTITY_SEQUENCE_NUMBER").toString());
            annDisclDetails.setSequenceNumber
                    (disclosuresInfo.get("SEQUENCE_NUMBER") == null ? null :
                    disclosuresInfo.get("SEQUENCE_NUMBER").toString());
            annDisclDetails.setDescription
                    ((String)disclosuresInfo.get("DESCRIPTION"));
            annDisclDetails.setUpdateUser
                    ((String)disclosuresInfo.get("UPDATE_USER"));
            annDisclDetails.setUpdateTimeStamp
                    (disclosuresInfo.get("UPDATE_TIMESTAMP").toString());
            disclosures.add(annDisclDetails);
        }
        return disclosures;
    }

    /**
     * The method used to get the Award Info for a particular person Disclosure
     *
     * This method will throw an exception if the Award No is null.
     * To fetch the data, it uses get_award_info procedure.
     *
     * @return AnnDisclosureDetailsBean AwardInfoDetails
     *
     * @exception DBException
     * @exception CoeusException
     */
    private AnnDisclosureDetailsBean getAwardInfo(String awardNo)
            throws DBException,CoeusException{
        if (  awardNo == null ) {
            throw new CoeusException("exceptionCode.70010");
        }
        Vector result = new Vector(3,2);
        Vector parameters= new java.util.Vector();
        AnnDisclosureDetailsBean annDisclDetails  = new AnnDisclosureDetailsBean();
        parameters.addElement(new Parameter("AWARD_NO","String",awardNo.toString()));
        if(dbEngine!=null){
            result = dbEngine.executeRequest("Coeus", "call get_awardinfo ( <<AWARD_NO>> ," +
                        "<<OUT RESULTSET rset>> )", "Coeus", parameters);
        }else{
            throw new DBException("exceptionCode.10001");
        }
        for(int cntAwards=0;cntAwards<result.size();cntAwards++){
            /* case #748 comment begin */
            //Hashtable  awardsInfo = (Hashtable)result.elementAt(cntAwards);
            /* case #748 comment end */
            /* case #748 begin */
            HashMap  awardsInfo = (HashMap)result.elementAt(cntAwards);
            /* case #748 end */
            annDisclDetails.setSponsor((String)awardsInfo.get("SPONSOR_NAME"));
            annDisclDetails.setTitle((String)awardsInfo.get("AS_TITLE"));
        }
        return annDisclDetails;
    }


    /**
     * This method is used to get the Proposal Infomation for a penidng Annual disclosure
     * This method will throw an exception if the proposal no is null.
     * To fetch the data, it uses procedure get_proposal_info.
     *
     * @return AnnDisclosureDetailsBean AnnualDisclosureDetails for a particular disclosure
     *
     * @exception DBException
     * @exception CoeusException
     */
     private AnnDisclosureDetailsBean getProposalInfo(String proposalNo)
            throws DBException,CoeusException{
        if (  proposalNo == null ) {
            throw new CoeusException("exceptionCode.70011");
        }
        Vector result = new Vector(3,2);
        Vector parameters= new java.util.Vector();
        AnnDisclosureDetailsBean annDisclDetails  = new AnnDisclosureDetailsBean();

        parameters.addElement(new Parameter("PROPOSAL_NO","String",proposalNo.toString()));
        if(dbEngine!=null){
            result = dbEngine.executeRequest("Coeus", "call get_inst_propinfo ("+
                        "<<PROPOSAL_NO>> , <<OUT RESULTSET rset>> )", "Coeus", parameters);
        }else{
            throw new DBException("exceptionCode.10001");
        }
        for(int cntProposals=0;cntProposals<result.size();cntProposals++){
            /* case #748 comment begin */
            //Hashtable proposalsInfo = (Hashtable)result.elementAt(cntProposals);
            /* case #748 comment end */
            /* case #748 begin */
            HashMap proposalsInfo = (HashMap)result.elementAt(cntProposals);
            /* case #748 end */
            annDisclDetails.setSponsor((String)proposalsInfo.get("SPONSOR_NAME"));
            annDisclDetails.setTitle((String)proposalsInfo.get("AS_TITLE"));
        }
        return annDisclDetails;
    }
}


