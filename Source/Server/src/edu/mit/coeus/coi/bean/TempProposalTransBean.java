/*
 * @(#)TempProposalTransBean.java 1.0 4/02/02
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
import java.util.LinkedList;
import edu.mit.coeus.utils.UtilFactory;
/* CASE #734 Comment Begin */
//import edu.mit.coeus.coi.exception.CoeusException;
/* CASE #734 Comment End */
/* CASE #734 Begin */
import edu.mit.coeus.exception.CoeusException;
/* CASE #734 End */
import edu.mit.coeus.utils.dbengine.*;
import edu.mit.coeus.utils.CoeusFunctions;
/**
 *
 * This class is used for adding a temporary proposal.
 *
 * @version 1.0 April 2, 2002, 12:08 PM
 * @author  Anil Nandakumar
 */
public class TempProposalTransBean extends java.lang.Object {
    /*
     *  Singleton instance of a dbEngine
     */
    private DBEngineImpl dbEngine;

//    private UtilFactory UtilFactory;
    /** Creates new TempProposalTransBean.
     * Contructor with no argument
     */
    public TempProposalTransBean(){
//        UtilFactory = new UtilFactory();
        //dbEngine = DBEngineImpl.getInstance();
        dbEngine = new DBEngineImpl();
    }
    /**
     *  This method is used to add a new temporary proposal.
     *  To generate a new proposal log number, it uses fn_get_next_temp_log_num function.
     *  To add data, it uses dw_add_proposallog procedure.
     *  @param TempProposalDetailsBean objTempPropBean
     *  @return String strTempProposalNumber
     *  @exception DBException
     *  @exception CoeusException
     */
    /* CASE #665 Comment Begin */
    /*  Split this method into two methods.  Move stored procedure call to get
        next temp proposal number moved to
        getNextTempLogNumDisc().  New method will return void. */
   /* public String add(TempProposalDetailsBean objTempPropBean)
            throws CoeusException,DBException{
        Vector result = new Vector(3,2);
        String tempProposalNumber = new String();
        Vector param= new java.util.Vector();
        //get the next temporary proposal log number
        if(dbEngine!=null){
            //CASE#164 Comment Begin
            //result = dbEngine.executeFunctions("Coeus", "{ <<OUT STRING tempProposalNo>> = call fn_get_next_temp_log_num() }", param);
            //CASE#164 Comment End

            //CASE#164 Begin
            result = dbEngine.executeFunctions("Coeus",
             "{ <<OUT STRING tempProposalNo>> = call fn_get_next_temp_log_num_disc() }", param);
            //CASE#164 End
        }else{
            throw new DBException("exceptionCode.10001");
        }
        if(result!=null){
            Hashtable proposalCountRow = (Hashtable)result.elementAt(0);
            tempProposalNumber = (String)proposalCountRow.get("tempProposalNo");
        }
        param.addElement(new Parameter("PROPOSAL_NUMBER","String",tempProposalNumber));
        param.addElement(new Parameter("PROPOSAL_TYPE_CODE","String",
              objTempPropBean.getProposalTypeCode().toString()));
        param.addElement(new Parameter("TITLE","String",
              objTempPropBean.getTitle().toString()));
        param.addElement(new Parameter("PI_ID","String",
              objTempPropBean.getPiId().toString()));
        param.addElement(new Parameter("PI_NAME","String",
              objTempPropBean.getPiName().toString()));
        param.addElement(new Parameter("LEAD_UNIT","String",
              objTempPropBean.getLeadUnit().toString()));
        param.addElement(new Parameter("SPONSOR_CODE","String",
              objTempPropBean.getSponsorCode().toString()));
        param.addElement(new Parameter("SPONSOR_NAME","String",
              objTempPropBean.getSponsorName().toString()));
        param.addElement(new Parameter("AV_NON_MIT_PERSON_FLAG","String","N"));
        param.addElement(new Parameter("LOG_STATUS","String",
              objTempPropBean.getLogStatus().toString()));
        param.addElement(new Parameter("COMMENTS","String",
              objTempPropBean.getComments().toString()));
        param.addElement(new Parameter("UPDATE_USER","String",
              objTempPropBean.getUpdateUser()));
        if(dbEngine!=null){
            StringBuffer sqlQry = new StringBuffer("call dw_add_proposallog (");
            sqlQry.append("<<PROPOSAL_NUMBER>> , ");
            sqlQry.append("<<PROPOSAL_TYPE_CODE>> , <<TITLE>> , ");
            sqlQry.append("<<PI_ID>> , <<PI_NAME>> , <<LEAD_UNIT>> , ");
            sqlQry.append("<<SPONSOR_CODE>> ,  <<SPONSOR_NAME>> , <<AV_NON_MIT_PERSON_FLAG>>, ");
            sqlQry.append("<<LOG_STATUS>> , <<COMMENTS>>, <<UPDATE_USER>> )");
            dbEngine.executeRequest("Coeus", sqlQry.toString(), "Coeus", param);
        }else{
            throw new DBException("exceptionCode.10001");
        }
        return tempProposalNumber;
    }*/

      /* CASE #665 Begin */
      /* Create temp proposal log.  Takes TempProposalDetailsBean with all
      necessary info.  This case puts stored proc call to get the temp proposal
      number into its own method. */
      /**
       * Create a temp proposal log, so that user can make COI Disclosure.
       * @param objTempPropBean Contains all info necessary to create the temp proposal log.
       * @throws CoeusException
       * @throws DBException
       */
      public void add(TempProposalDetailsBean objTempPropBean)
                                      throws CoeusException,DBException{
        Vector result = new Vector(3,2);
        Vector param= new java.util.Vector();

        param.addElement(new Parameter("PROPOSAL_NUMBER","String",
              objTempPropBean.getProposalNumber()));
        param.addElement(new Parameter("PROPOSAL_TYPE_CODE","String",
              objTempPropBean.getProposalTypeCode().toString()));
        param.addElement(new Parameter("TITLE","String",
              objTempPropBean.getTitle().toString()));
        param.addElement(new Parameter("PI_ID","String",
              objTempPropBean.getPiId().toString()));
        param.addElement(new Parameter("PI_NAME","String",
              objTempPropBean.getPiName().toString()));
        param.addElement(new Parameter("LEAD_UNIT","String",
              objTempPropBean.getLeadUnit().toString()));
        param.addElement(new Parameter("SPONSOR_CODE","String",
              (String)objTempPropBean.getSponsorCode()));
        param.addElement(new Parameter("SPONSOR_NAME","String",
              (String)objTempPropBean.getSponsorName()));
        param.addElement(new Parameter("AV_NON_MIT_PERSON_FLAG","String","N"));
        param.addElement(new Parameter("LOG_STATUS","String",
              objTempPropBean.getLogStatus().toString()));
        /* CASE #1374 Comments field removed from form */
        param.addElement(new Parameter("COMMENTS","String", null));
        param.addElement(new Parameter("UPDATE_USER","String",
              objTempPropBean.getUpdateUser()));
        if(dbEngine!=null){
            StringBuffer sqlQry = new StringBuffer("call dw_add_proposallog (");
            sqlQry.append("<<PROPOSAL_NUMBER>> , ");
            sqlQry.append("<<PROPOSAL_TYPE_CODE>> , <<TITLE>> , ");
            sqlQry.append("<<PI_ID>> , <<PI_NAME>> , <<LEAD_UNIT>> , ");
            sqlQry.append("<<SPONSOR_CODE>> ,  <<SPONSOR_NAME>> , <<AV_NON_MIT_PERSON_FLAG>>, ");
            sqlQry.append("<<LOG_STATUS>> , <<COMMENTS>>, <<UPDATE_USER>> )");
            dbEngine.executeRequest("Coeus", sqlQry.toString(), "Coeus", param);
        }else{
            throw new DBException("exceptionCode.10001");
        }
    }
    /* CASE #665 End */

    public boolean updTempProposalInfo(TempProposalDetailsBean tempProposalDetails)
                throws CoeusException,DBException{
        boolean updateSuccess = false;
        Vector result = new Vector(3,2);
        /* Construct DisclosureDetailsBean in order to call its getTimestamp()
        method, to get current Timestamp from database. */
        DisclosureDetailsBean disclosureDetails =
            new DisclosureDetailsBean(tempProposalDetails.getPiId());
        Timestamp currentDate = disclosureDetails.getTimestamp();
        Vector param= new Vector();
        param.addElement(new Parameter("AV_PROPOSAL_NUMBER","String",
                tempProposalDetails.getProposalNumber()));
        param.addElement(new Parameter("AV_PROPOSAL_TYPE_CODE","String",
                tempProposalDetails.getProposalTypeCode()));
        param.addElement(new Parameter("AV_TITLE","String",
                tempProposalDetails.getTitle()));
        param.addElement(new Parameter("AV_PI_ID","String",
                tempProposalDetails.getPiId()));
        param.addElement(new Parameter("AV_PI_NAME","String",
                tempProposalDetails.getPiName()));
        param.addElement(new Parameter("AV_NON_MIT_PERSON_FLAG","String","N"));
        param.addElement(new Parameter("AV_LEAD_UNIT","String",
                tempProposalDetails.getLeadUnit()));
        param.addElement(new Parameter("AV_SPONSOR_CODE","String",
                tempProposalDetails.getSponsorCode()));
        param.addElement(new Parameter("AV_SPONSOR_NAME","String",
                tempProposalDetails.getSponsorName()));
        param.addElement(new Parameter("AV_LOG_STATUS","String",
                tempProposalDetails.getLogStatus()));
        param.addElement(new Parameter("AV_COMMENTS","String",
                tempProposalDetails.getComments()));
        param.addElement(new Parameter("AW_UPDATE_USER","String",
                tempProposalDetails.getLastUpdateUser()));
        param.addElement(new Parameter("AW_UPDATE_TIMESTAMP", "Date",
                tempProposalDetails.getLastUpdated()));
        param.addElement(new Parameter("AV_UPDATE_USER","String",
                tempProposalDetails.getUpdateUser()));
        param.addElement(new Parameter("AV_UPDATE_TIMESTAMP","Date",currentDate));
        param.addElement(new Parameter("AC_TYPE", "String", "U"));

        if(dbEngine!=null){
            StringBuffer sqlQry = new StringBuffer("call dw_upd_proposal_log (");
            sqlQry.append("<<AV_PROPOSAL_NUMBER>> , ");
            sqlQry.append("<<AV_PROPOSAL_TYPE_CODE>>, ");
            sqlQry.append("<<AV_TITLE>> , ");
            sqlQry.append("<<AV_PI_ID>> , <<AV_PI_NAME>>," );
            sqlQry.append("<<AV_NON_MIT_PERSON_FLAG>>, ");
            sqlQry.append("<<AV_LEAD_UNIT>> , ");
            sqlQry.append("<<AV_SPONSOR_CODE>> ,  <<AV_SPONSOR_NAME>> , ");
            sqlQry.append("<<AV_LOG_STATUS>> , ");
            sqlQry.append("<<AV_COMMENTS>>, ");
            sqlQry.append("<<AW_UPDATE_USER>>, ");
            sqlQry.append("<<AW_UPDATE_TIMESTAMP>>, ");
            sqlQry.append("<<AV_UPDATE_USER>>, ");
            sqlQry.append("<<AV_UPDATE_TIMESTAMP>>, ");
            sqlQry.append("<<AC_TYPE>> )");
            dbEngine.executeRequest("Coeus", sqlQry.toString(), "Coeus", param);
            updateSuccess = true;
        }else{
            throw new DBException("exceptionCode.10001");
        }
        return updateSuccess;
    }

    public TempProposalDetailsBean getTempProposalInfo(String proposalNumber)
                throws DBException{
        Vector result = new Vector(3,2);
        Vector param= new java.util.Vector();
        TempProposalDetailsBean tempProposalDetails = new TempProposalDetailsBean();
        param.addElement(new Parameter("AW_PROP_NO","String",proposalNumber));
        if(dbEngine!=null){
            result = dbEngine.executeRequest("Coeus",
                "call dw_get_proposal_log  (  <<AW_PROP_NO>> , <<OUT RESULTSET rset>>)   ",
                "Coeus", param);
        }else{
            throw new DBException("exceptionCode.10001");
        }
        /* case #748 comment begin */
        //Hashtable tempProposalRow = new Hashtable();
        /* case #748 comment end */
        /* case #748 begin */
        HashMap tempProposalRow = new HashMap();
        /* case #748 end */
        if(result!=null && !result.isEmpty()){
            /* case #748 comment begin */
            //tempProposalRow = (Hashtable)result.elementAt(0);
            /* case #748 comment end */
            /* case #748 begin */
            tempProposalRow = (HashMap)result.elementAt(0);
            /* case #748 end */
            tempProposalDetails.setProposalNumber(proposalNumber);
            tempProposalDetails.setTitle((String)tempProposalRow.get("TITLE"));
            tempProposalDetails.setProposalTypeCode(
                tempProposalRow.get("PROPOSAL_TYPE_CODE") == null ? null :
                tempProposalRow.get("PROPOSAL_TYPE_CODE").toString());
            tempProposalDetails.setLogStatus(
                tempProposalRow.get("LOG_STATUS") == null ? null :
                    tempProposalRow.get("LOG_STATUS").toString());
            tempProposalDetails.setPiId((String)tempProposalRow.get("PI_ID"));
            tempProposalDetails.setPiName((String)tempProposalRow.get("PI_NAME"));
            tempProposalDetails.setLeadUnit((String)tempProposalRow.get("LEAD_UNIT"));
            tempProposalDetails.setSponsorCode
                (tempProposalRow.get("SPONSOR_CODE") == null ? null :
                tempProposalRow.get("SPONSOR_CODE").toString());
            tempProposalDetails.setSponsorName
                ((String)tempProposalRow.get("SPONSOR_NAME"));
            tempProposalDetails.setComments((String)tempProposalRow.get("COMMENTS"));
            tempProposalDetails.setLastUpdated
                ((Timestamp)tempProposalRow.get("UPDATE_TIMESTAMP"));
            tempProposalDetails.setLastUpdateUser
                ((String)tempProposalRow.get("UPDATE_USER"));
            /* CASE #1374 Begin */
            tempProposalDetails.setProposalType
                ((String) tempProposalRow.get("PROPOSAL_TYPE") );
            /* CASE #1374 End */
        }
        return tempProposalDetails;

    }

    /* CASE #665 Begin */
    public String getNextTempLogNumDisc() throws DBException{
        Vector result = new Vector(3,2);
        String tempProposalNumber = new String();
        Vector param= new java.util.Vector();
        //get the next temporary proposal log number
        if(dbEngine!=null){
            result = dbEngine.executeFunctions("Coeus",
             "{ <<OUT STRING tempProposalNo>> = call fn_get_next_temp_log_num_disc() }",
             param);
        }else{
            throw new DBException("exceptionCode.10001");
        }
        if(result!=null){
            /* case #748 comment begin */
            //Hashtable proposalCountRow = (Hashtable)result.elementAt(0);
            /* case #748 comment end */
            /* case #748 begin */
            HashMap proposalCountRow = (HashMap)result.elementAt(0);
            /* case #748 end */
            tempProposalNumber = proposalCountRow.get("tempProposalNo").toString();
        }
        return tempProposalNumber;
    }
    /* CASE #665 End */
    
    /* CASE #1374 Begin */
    public String getDefaultLeadUnit() throws DBException, CoeusException{
        String parameterValue = null ;
        CoeusFunctions coeusFunctions = new CoeusFunctions();
        parameterValue = coeusFunctions.getParameterValue("DEFAULT_TEMP_PROP_LEAD_UNIT");
        return parameterValue ;        
    }
    /* CASE #1374 End */
}