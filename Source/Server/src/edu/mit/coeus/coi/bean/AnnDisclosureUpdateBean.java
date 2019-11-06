/*
 * @(#)AnnDisclosureUpdateBean.java 1.0 6/9/02
 *
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 */

package edu.mit.coeus.coi.bean;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Vector;
/* CASE #748 Begin */
import java.util.HashMap;
/* CASE #748 End */
import edu.mit.coeus.utils.UtilFactory;
import edu.mit.coeus.utils.dbengine.DBException;
import edu.mit.coeus.utils.dbengine.DBEngineImpl;
import edu.mit.coeus.utils.dbengine.Parameter;
import edu.mit.coeus.utils.dbengine.ProcReqParameter;
/* CASE #734 Comment Begin */
//import edu.mit.coeus.coi.exception.CoeusException;
/* CASE #734 Comment End */
/* CASE #734 Begin */
import edu.mit.coeus.exception.CoeusException;
/* CASE #734 End */

/**
 * <code>AnnDisclosureUpdateBean</code> is a class to update the reviewed disclosures
 * for a particular financial entity into the <code>coeus</code> database.
 *
 * @version 1.0 June 9,2002
 * @author Phaneendra Kumar.
 */
public class AnnDisclosureUpdateBean {

    protected static final String REVIEWER_CODE = "1";
    protected static final String AC_TYPE = "I";

    /*
     *  Singleton instance of a dbEngine
     */
    private DBEngineImpl dbEngine;

    /*
     *  Initialize bean with the logged in User Name.
     */
    private String userName;

    /**
     * Creates new AnnualDisclosureUpdateBean.
     *
     * @param userName -  The logged in user name
     */
    public AnnDisclosureUpdateBean(String userName ){
        this.userName = userName;
        dbEngine = new DBEngineImpl();
    }

    /**
     *  Method used to fetch the Timestamp from the database.
     *  Returns a current timestamp from the database.
     *  <li>To fetch the data, it uses dw_get_cur_sysdate procedure.
     *
     *  @return Timestamp current timestamp from the database
     *
     *  @exception DBException
     *  @exception CoeusException
     */
    public Timestamp getTimestamp() throws  CoeusException,DBException{
        Timestamp timeStamp = null;
        Vector parameters= new Vector();
        Vector resultTimeStamp = new Vector();
        /* calling stored function */
        if(dbEngine!=null){
            resultTimeStamp = dbEngine.executeFunctions("Coeus",
                    " call dw_get_cur_sysdate( <<OUT RESULTSET rset>> )", parameters);
        }else{
            throw new DBException("exceptionCode.10001");
        }
        if(!resultTimeStamp.isEmpty()){
            /* case #748 comment begin */
            //Hashtable htTimeStampRow = (Hashtable)resultTimeStamp.elementAt(0);
            /* case #748 comment end */
            /* case #748 begin */
            HashMap htTimeStampRow = (HashMap)resultTimeStamp.elementAt(0);
            /* case #748 end */
            timeStamp = (Timestamp)htTimeStampRow.get("SYSDATE");
        }
        return timeStamp;
    }

    /**
     *  The method used to update the conflict status of pendidng annual disclosure
     *  for a particular Financial Entity. This will throw an exception
     *  if the annulDisclosure information is null.
     *  <li>To update the data, it uses dw_upd_inv_coi_disc_details procedure.
     *
     *  @param Vector AnnualDisclosures, collection of all the disclosures for
     *  a particular financial entity.
     *  @return boolean, the transaction status. If any one of the updation gets failed,
     *  it will roll back all the transactions and returns false, else, returns true.
     *  @exception DBException
     *  @exception CoeusException
     */
    public boolean updateAnnualDisclosureInfo(Vector annualDisclosures)
            throws DBException,CoeusException{
        if(annualDisclosures == null){
            throw new CoeusException("exceptionCode.70009");
        }
        Timestamp currentDateTime = this.getTimestamp();
        final String dsn = "Coeus";
        //Holds all the procedures in an array to send to dbengine as one transaction.
        Vector procedures = new Vector(5,3);
        AnnDisclosureDetailsBean annualDisclosureInfo = null;
        for (int cnt =0;cnt<annualDisclosures.size();cnt++){
            Vector parameters= new java.util.Vector();
            int newSequenceNumber =0;
            String sequenceNumber ="";

            if (annualDisclosures.get(cnt) instanceof AnnDisclosureDetailsBean ){
                annualDisclosureInfo = (AnnDisclosureDetailsBean )annualDisclosures.get(cnt);
            }
            newSequenceNumber = Integer.parseInt(
                    annualDisclosureInfo.getSequenceNumber().toString());
            newSequenceNumber = newSequenceNumber + 1;
            sequenceNumber = new Integer(newSequenceNumber).toString();
            parameters.addElement(new Parameter("COI_DISCLOSURE_NUMBER",
                    "String",annualDisclosureInfo.getDisclosureNumber().trim()));
            parameters.addElement(new Parameter("ENTITY_NUMBER",
                    "String",annualDisclosureInfo.getEntityNumber().trim()));
            parameters.addElement(new Parameter("ENTITY_SEQUENCE_NUMBER",
                    "String",annualDisclosureInfo.getEntitySeqNumber().trim()));
            parameters.addElement(new Parameter("SEQUENCE_NUMBER",
                    "String", sequenceNumber.trim()));
            parameters.addElement(new Parameter("COI_STATUS_CODE",
                    "String",annualDisclosureInfo.getConflictStatus().trim()));
            parameters.addElement(new Parameter("COI_REVIEWER_CODE",
                    "String",REVIEWER_CODE));
            parameters.addElement(new Parameter("DESCRIPTION",
                    "String",annualDisclosureInfo.getDescription()));
            parameters.addElement(new Parameter("UPDATE_TIMESTAMP",
                    "Date",currentDateTime));
            /* CASE #855 Comment Begin */
            //parameters.addElement(new Parameter("UPDATE_USER","String","COEUS"));
            /* CASE #855 Comment End */
            /* cASE #855 Begin */
            parameters.addElement(new Parameter("UPDATE_USER","String",this.userName));
            /* CASE #855 End */
            parameters.addElement(new Parameter("AC_TYPE","String",AC_TYPE));

            StringBuffer buffSqlInfo =  new StringBuffer("call dw_upd_inv_coi_disc_details ( ");
                    buffSqlInfo.append(" << COI_DISCLOSURE_NUMBER >> ,");
            buffSqlInfo.append(" << ENTITY_NUMBER >> ,");
            buffSqlInfo.append(" << ENTITY_SEQUENCE_NUMBER >> ,");
            buffSqlInfo.append(" << SEQUENCE_NUMBER >> ,");
            buffSqlInfo.append(" << COI_STATUS_CODE >> ,");
            buffSqlInfo.append(" << COI_REVIEWER_CODE >> ,");
            buffSqlInfo.append(" << DESCRIPTION >> ,");
            buffSqlInfo.append(" << UPDATE_TIMESTAMP >> ,");
            buffSqlInfo.append(" << UPDATE_USER >> ,");
            buffSqlInfo.append(" << AC_TYPE >> )");
            ProcReqParameter procReqParamInfo = new ProcReqParameter();
            procReqParamInfo.setDSN(dsn);
            procReqParamInfo.setParameterInfo(parameters);
            procReqParamInfo.setSqlCommand(buffSqlInfo.toString());
            procedures.addElement(procReqParamInfo);
        }
        if(dbEngine!=null){
            dbEngine.executeStoreProcs(procedures);
        }else{
            throw new DBException("exceptionCode.10001");
        }
        return true;
    }
}
