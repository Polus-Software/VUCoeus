/*
 * @(#)DisclosureDetailViewBean.java 1.0 3/26/02
 *
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 */
package edu.mit.coeus.coi.bean;

//import java.sql.SQLException;
import java.util.Vector;
import java.util.Hashtable;
import java.sql.Timestamp;
/* CASE #748 Begin */
import java.util.HashMap;
/* CASE #748 End */
import java.util.LinkedList;
import edu.mit.coeus.utils.dbengine.*;
import edu.mit.coeus.utils.UtilFactory;
/**
 * This class holds the disclosure Details using DisclosureInfoDetailBean and
 * DisclosureInfoHistoryBean objects.
 *
 * @version 1.0 March 26, 2002, 10:08 AM
 * @author  Anil Nandakumar
 */
public class DisclosureDetailViewBean {
    /*
     *  Instance of a dbEngine
     */
    private DBEngineImpl dbEngine;
    /**
     * Creates new DisclosureDetailViewBean
     * Contructor with no arguments
     */
    public DisclosureDetailViewBean(){
        //dbEngine = DBEngineImpl.getInstance();
        dbEngine = new DBEngineImpl();
    }
    /**
     *  This method is used to get all details corresponding to a disclosure and entity number.
     *  <li>To fetch the data, it uses dw_get_inv_coi_discDetail procedure.
     *  @param String Disclosure Number
     *  @param String Entity Number
     *  @return DisclosureInfoDetailBean Entity details of a dislosure
     *  @exception DBException
     */
    public DisclosureInfoDetailBean getDisclosureInfoDetail(
            String disclosureNumber, String entityNumber) throws DBException{
        Vector result = new Vector();
        Vector param = new Vector();
        DisclosureInfoDetailBean disclInfoDetail = new DisclosureInfoDetailBean();
        param.addElement(new Parameter("COI_DISCLOSURE_NUMBER","String",disclosureNumber));
        param.addElement(new Parameter("ENTITY_NUMBER","String",entityNumber));
        if(dbEngine!=null){
            result = dbEngine.executeRequest("Coeus",
              "call dw_get_inv_coi_discDetail ( <<COI_DISCLOSURE_NUMBER>> , <<ENTITY_NUMBER>> , <<OUT RESULTSET rset>> ) ",
              "Coeus", param);
        }else{
            throw new DBException("exceptionCode.10001");
        }
        if(result!=null && !result.isEmpty()){
            /* case #748 comment begin */
            //Hashtable disclInfoRow = (Hashtable)result.elementAt(0);
            /* case #748 comment end */
            /* case #748 begin */
            HashMap disclInfoRow = (HashMap)result.elementAt(0);
            /* case #748 end */
            disclInfoDetail.setEntityName(disclInfoRow.get("ENTITY_NAME").toString());
            disclInfoDetail.setConflictStatus(disclInfoRow.get("COI_STATUS").toString());
            disclInfoDetail.setReviewer(disclInfoRow.get("COI_REVIEWER").toString());
            disclInfoDetail.setDesc((String)disclInfoRow.get("DESCRIPTION"));
        }
        return disclInfoDetail;
    }

    /**
     *  This method is used to get all historical information corresponding to a disclosure.
     *  <li>To fetch the data, it uses dw_get_Ent_history_Details procedure.
     *  @param String disclosureNumber
     *  @param String entityNumber
     *  @return Vector vector of DisclosureInfoHistoryBean instance
     *  @exception DBException
     */
    //CASE #748 Method updated to throw only DBException
    public Vector getDisclosureInfoHistory(String disclosureNumber, String entityNumber)
                                                            throws DBException{
        Vector result = new Vector(3,2);
        Vector disclInfoHistoryDetails = new Vector(3,2);
        Vector param= new java.util.Vector();
        param.addElement(new Parameter("COI_DISCLOSURE_NUMBER","String",disclosureNumber));
        param.addElement(new Parameter("ENTITY_NUMBER","String",entityNumber));
        if(dbEngine!=null){
            result = dbEngine.executeRequest("Coeus",
              "call dw_get_Ent_history_Details ( <<COI_DISCLOSURE_NUMBER>> , <<ENTITY_NUMBER>> ,  <<OUT RESULTSET rset>> )",
              "Coeus", param);
        }else{
            throw new DBException("exceptionCode.10001");
        }
        for(int j=0;j<result.size();j++){
            /* case #748 comment begin */
            //Hashtable disclHistoryInfoRow = (Hashtable)result.elementAt(j);
            /* case #748 comment end */
            /* case #748 begin */
            HashMap disclHistoryInfoRow = (HashMap)result.elementAt(j);
            /* case #748 end */
            DisclosureInfoHistoryBean disclosureInfoHistory =
                new DisclosureInfoHistoryBean();
            disclosureInfoHistory.setEntityNumber(disclHistoryInfoRow.get
                ("ENTITY_NUMBER").toString());
            disclosureInfoHistory.setSequenceNumber(disclHistoryInfoRow.get
                ("SEQUENCE_NUMBER").toString());
            disclosureInfoHistory.setEntitySequenceNumber(disclHistoryInfoRow.get
                ("ENTITY_SEQUENCE_NUMBER").toString());
            disclosureInfoHistory.setConflictStatus(disclHistoryInfoRow.get
                ("COI_STATUS").toString());
            disclosureInfoHistory.setReviewer(disclHistoryInfoRow.get
                ("COI_REVIEWER").toString());
            disclosureInfoHistory.setDesc((String)disclHistoryInfoRow.get
                ("DESCRIPTION"));
            disclosureInfoHistory.setUpdatedBy(disclHistoryInfoRow.get
                ("UPDATE_USER").toString());
            disclosureInfoHistory.setUpdatedDate
                ((Timestamp)disclHistoryInfoRow.get("UPDATE_TIMESTAMP"));
            disclInfoHistoryDetails.add(disclosureInfoHistory);
        }
        return disclInfoHistoryDetails;
    }
}