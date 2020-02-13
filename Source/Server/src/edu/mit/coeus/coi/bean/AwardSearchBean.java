/*
 * @(#)AwardSearchBean.java 1.0 3/30/02
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
 * This class is for doing Award searches in the COI module. All search method will return
 * a vector which has a collection of bean instance for that particular search result.
 *
 * @version 1.0 March 30, 2002, 11:03 AM
 * @author  Anil Nandakumar
 */
public class AwardSearchBean {
    /*
     *  Instance of a dbEngine
     */
    private DBEngineImpl dbEngine;
    /** Creates new AwardSearchBean.
     * Contructor with one argument
     * @param String person id
     */
    public AwardSearchBean(){
        //dbEngine = DBEngineImpl.getInstance();
        dbEngine = new DBEngineImpl();
    }
    /**
     *  This method is used to get all award details for a particular search criterea.
     *  This method will also throw an exception if the person id for this bean is null.
     *  To fetch the data, it uses dw_get_award_list_forquery procedure.
     *  @param String personName
     *  @param String sponAwadNo
     *  @param String awardNo
     *  @param String accountNo
     *  @param String sponsorCode
     *  @param String sponName
     *  @param String title
     *  @param String investigator
     *  @param String unitNo
     *  @param String unitName
     *  @param String OSPAdmin
     *  @param String status
     *  @return vector
     *  @exception DBException
     *  @exception CoeusException
     */
    public Vector getAwardDetails(String personName, String sponAwadNo,String awardNo,
                                    String accountNo,String sponsorCode,String sponName,
                                    String title,String investigator,String unitNo,
                                    String unitName,String OSPAdmin,String status)
                                    throws DBException,CoeusException{
        /*if(personID==null){
            throw new CoeusException("exceptionCode.20001");
        }*/
        Vector result = new Vector(3,2);
        Vector awards = new Vector(3,2);
        StringBuffer searchQry = new StringBuffer("");
        //QUERY TO SEARCH FOR AWARDS..DYNAMICALLY BUILT BASED ON SEARCH CRITERIA
        searchQry.append("SELECT OSP$AWARD.SPONSOR_AWARD_NUMBER, ");
        searchQry.append("OSP$AWARD.MIT_AWARD_NUMBER,  ");
        searchQry.append("OSP$AWARD_UNITS.UNIT_NUMBER, ");
        searchQry.append("OSP$UNIT.UNIT_NAME, ");
        searchQry.append("OSP$AWARD.ACCOUNT_NUMBER, ");
        searchQry.append("OSP$AWARD.STATUS_CODE,  ");
        searchQry.append("OSP$AWARD_STATUS.DESCRIPTION,  ");
        searchQry.append("OSP$AWARD_HEADER.TITLE,  ");
        searchQry.append("OSP$AWARD.SPONSOR_CODE,  ");
        searchQry.append("OSP$SPONSOR.SPONSOR_NAME,  ");
        searchQry.append("OSP$AWARD_INVESTIGATORS.PERSON_NAME,  ");
        searchQry.append("OSP$PERSON.FULL_NAME ");
        searchQry.append("FROM OSP$AWARD ,OSP$AWARD_HEADER, OSP$SPONSOR, OSP$AWARD_UNITS, OSP$UNIT,  ");
        searchQry.append("OSP$AWARD_INVESTIGATORS, OSP$PERSON, OSP$AWARD_STATUS   ");
        searchQry.append("WHERE ( OSP$AWARD.SPONSOR_CODE = OSP$SPONSOR.SPONSOR_CODE (+))  ");
        searchQry.append("AND ( OSP$AWARD.SEQUENCE_NUMBER IN  (   ");
        searchQry.append(" SELECT MAX(A.SEQUENCE_NUMBER)   ");
        searchQry.append(" FROM OSP$AWARD A   ");
        searchQry.append(" WHERE A.MIT_AWARD_NUMBER =OSP$AWARD. MIT_AWARD_NUMBER ))  ");
        searchQry.append("AND ( OSP$AWARD.MIT_AWARD_NUMBER = OSP$AWARD_HEADER.MIT_AWARD_NUMBER)    ");
        searchQry.append("AND ( OSP$AWARD.SEQUENCE_NUMBER = OSP$AWARD_HEADER.SEQUENCE_NUMBER)    ");
        searchQry.append("AND ( OSP$AWARD.MIT_AWARD_NUMBER = OSP$AWARD_UNITS.MIT_AWARD_NUMBER)     ");
        searchQry.append("AND ( OSP$AWARD_UNITS.LEAD_UNIT_FLAG = 'Y')    ");
        searchQry.append("AND ( OSP$AWARD_UNITS.SEQUENCE_NUMBER IN  (   ");
        searchQry.append(" SELECT MAX(B.SEQUENCE_NUMBER)   ");
        searchQry.append(" FROM OSP$AWARD_UNITS B   ");
        searchQry.append(" WHERE B.MIT_AWARD_NUMBER =OSP$AWARD_UNITS.MIT_AWARD_NUMBER ))  ");
        searchQry.append("AND ( OSP$AWARD_UNITS.UNIT_NUMBER = OSP$UNIT.UNIT_NUMBER )   ");
        searchQry.append("AND ( OSP$UNIT.OSP_ADMINISTRATOR = OSP$PERSON.PERSON_ID (+) )     ");
        searchQry.append("AND ( OSP$AWARD.MIT_AWARD_NUMBER = OSP$AWARD_INVESTIGATORS.MIT_AWARD_NUMBER)   ");
        searchQry.append("AND ( OSP$AWARD_INVESTIGATORS.PRINCIPAL_INVESTIGATOR_FLAG = 'Y')    ");
        searchQry.append("AND ( OSP$AWARD_INVESTIGATORS.SEQUENCE_NUMBER IN  (   ");
        searchQry.append(" SELECT MAX(C.SEQUENCE_NUMBER)   ");
        searchQry.append(" FROM OSP$AWARD_INVESTIGATORS C   ");
        searchQry.append(" WHERE C.MIT_AWARD_NUMBER =OSP$AWARD_INVESTIGATORS. MIT_AWARD_NUMBER))  ");
        searchQry.append("AND OSP$AWARD.STATUS_CODE = OSP$AWARD_STATUS.STATUS_CODE   ");
        searchQry.append("AND (OSP$AWARD.MIT_AWARD_NUMBER  IN (   ");
        searchQry.append(" SELECT DISTINCT AWARD.MIT_AWARD_NUMBER   ");
        //FROM CLAUSE BEGINS HERE
        searchQry.append(" FROM OSP$AWARD AWARD ");
        if(title!=null && !title.equals("")){
            searchQry.append(" , OSP$AWARD_HEADER HEADER ");
        }
        if(unitNo!=null && !unitNo.equals("")){
            searchQry.append(" ,OSP$AWARD_UNITS AWU  ");
        }
        if(unitName!=null && !unitName.equals("")){
            searchQry.append(" , OSP$UNIT UNIT  ");
        }
        if(OSPAdmin!=null && !OSPAdmin.equals("")){
            searchQry.append(" , OSP$PERSON PERSON  ");
        }

        if(sponsorCode!=null && !sponsorCode.equals("")){
            searchQry.append(" , OSP$SPONSOR SPONSOR ");
        }else if(sponName!=null && !sponName.equals("")){
            searchQry.append(" , OSP$SPONSOR SPONSOR ");
        }

        if(investigator!=null && !investigator.equals("")){
            searchQry.append(" , OSP$AWARD_INVESTIGATORS INV  ");
        }
        //WHERE CLAUSE BEGINS HERE
        searchQry.append(" WHERE (AWARD.SEQUENCE_NUMBER = ( ");
        searchQry.append(" SELECT MAX(A1.SEQUENCE_NUMBER) ");
        searchQry.append(" FROM OSP$AWARD A1 ");
        searchQry.append(" WHERE AWARD.MIT_AWARD_NUMBER = A1.MIT_AWARD_NUMBER ))     ");
        if(title!=null && !title.equals("")){
            searchQry.append(" AND ( AWARD.MIT_AWARD_NUMBER = HEADER.MIT_AWARD_NUMBER)   ");
            searchQry.append(" AND ( HEADER.SEQUENCE_NUMBER IN   ");
            searchQry.append("  ( SELECT MAX(HEADER1.SEQUENCE_NUMBER)  ");
            searchQry.append(" FROM OSP$AWARD_HEADER HEADER1   ");
            searchQry.append(" WHERE HEADER1.MIT_AWARD_NUMBER = HEADER.MIT_AWARD_NUMBER))   ");
            searchQry.append(" AND (UPPER(HEADER.TITLE) LIKE " + UtilFactory.checkNull(title) + ")");
        }
        if(unitNo!=null && !unitNo.equals("")){
            searchQry.append(" AND ( AWARD.MIT_AWARD_NUMBER = AWU.MIT_AWARD_NUMBER)    ");
            searchQry.append(" AND ( AWU.SEQUENCE_NUMBER IN    ");
            searchQry.append(" ( SELECT MAX(AWU1.SEQUENCE_NUMBER)    ");
            searchQry.append(" FROM OSP$AWARD_UNITS AWU1    ");
            searchQry.append(" WHERE AWU1.MIT_AWARD_NUMBER =AWU.MIT_AWARD_NUMBER ))   ");
            searchQry.append(" AND (AWU.UNIT_NUMBER = " + UtilFactory.checkNull(unitNo) + ")");
        }
        if(unitName!=null && !unitName.equals("")){
            searchQry.append(" AND ( OSP$AWARD_UNITS.UNIT_NUMBER = UNIT.UNIT_NUMBER ) ");
            searchQry.append(" AND (UPPER(UNIT.UNIT_NAME) LIKE " + UtilFactory.checkNull(unitName) + ")");
        }
        if(OSPAdmin!=null && !OSPAdmin.equals("")){
            searchQry.append(" AND ( OSP$UNIT.OSP_ADMINISTRATOR = PERSON.PERSON_ID) ");
            searchQry.append(" AND (UPPER(OSP$PERSON.FULL_NAME) LIKE " + UtilFactory.checkNull(OSPAdmin) + ")");
        }
        if(sponsorCode!=null && !sponsorCode.equals("")){
            searchQry.append(" AND ( AWARD.SPONSOR_CODE = SPONSOR.SPONSOR_CODE) ");
            searchQry.append(" AND (AWARD.SPONSOR_CODE = " + UtilFactory.checkNull(sponsorCode) + ")");
        }
        if(sponName!=null && !sponName.equals("")){
            searchQry.append(" AND ( AWARD.SPONSOR_CODE = SPONSOR.SPONSOR_CODE) ");
            searchQry.append(" AND (UPPER(SPONSOR.SPONSOR_NAME) LIKE "  + UtilFactory.checkNull(sponName) + ")");
        }
        if(investigator!=null && !investigator.equals("")){
            searchQry.append(" AND  ( AWARD.MIT_AWARD_NUMBER = INV.MIT_AWARD_NUMBER)    ");
            searchQry.append(" AND ( INV.SEQUENCE_NUMBER IN    ");
            searchQry.append(" ( SELECT MAX(INV1.SEQUENCE_NUMBER)   ");
            searchQry.append(" FROM OSP$AWARD_INVESTIGATORS INV1    ");
            searchQry.append(" WHERE INV1.MIT_AWARD_NUMBER =INV.MIT_AWARD_NUMBER)) ");
            searchQry.append(" AND (UPPER(INV.PERSON_NAME) LIKE " + UtilFactory.checkNull(investigator) + ")");
        }
        if(status!=null && !status.equals("")){
            searchQry.append(" AND (AWARD.STATUS_CODE = " + status  + ")");
        }
        if(personName!=null && !personName.equals("")){
            searchQry.append(" AND (UPPER(OSP$PERSON.FULL_NAME) LIKE " + UtilFactory.checkNull(personName) + ")");
        }
        if(sponAwadNo!=null && !sponAwadNo.equals("")){
            searchQry.append(" AND  (UPPER(AWARD.SPONSOR_AWARD_NUMBER) = " + UtilFactory.checkNull(sponAwadNo) + ")");
        }
        if(awardNo!=null && !awardNo.equals("")){
            searchQry.append(" AND (AWARD.MIT_AWARD_NUMBER = " + UtilFactory.checkNull(awardNo) + ")");
        }
        if(accountNo!=null && !accountNo.equals("")){
            searchQry.append(" AND (AWARD.ACCOUNT_NUMBER = " + UtilFactory.checkNull(accountNo) + ")");
        }
        if(title!=null && !title.equals("")){
            searchQry.append(" AND (UPPER(HEADER.TITLE) LIKE  " + UtilFactory.checkNull(title) + ")");
        }
        searchQry.append(" )) " );
        Vector param= new java.util.Vector();
        param.addElement(new Parameter("QUERY","String",searchQry.toString()));
        if(dbEngine!=null){
            result = dbEngine.executeRequest("Coeus", "call dw_get_award_list_forquery ( <<QUERY>> , <<OUT RESULTSET rset>> )", "Coeus", param);
        }else{
            throw new DBException("exceptionCode.10001");
        }
        for(int i=0;i<result.size();i++){
            /* case #748 comment begin */
            //Hashtable awardRow = (Hashtable)result.elementAt(i);
            /* case #748 comment end */
            /* case #748 begin */
            HashMap awardRow = (HashMap)result.elementAt(i);
            /* case #748 end */
            AwardDetailsBean awardDetails = new AwardDetailsBean();
            awardDetails.setSponsorAwardNumber(
              awardRow.get("SPONSOR_AWARD_NUMBER") == null ? null :
              awardRow.get("SPONSOR_AWARD_NUMBER").toString());
            awardDetails.setMitAwardNumber(awardRow.get("MIT_AWARD_NUMBER").toString());
            awardDetails.setUnitNumber(awardRow.get("UNIT_NUMBER").toString());
            awardDetails.setUnitName(awardRow.get("UNIT_NAME") == null ? null:
                                        awardRow.get("UNIT_NAME").toString());
            awardDetails.setAccountNumber(
                                awardRow.get("ACCOUNT_NUMBER") == null ? null :
                                awardRow.get("ACCOUNT_NUMBER").toString());
            awardDetails.setStatusCode(
                                awardRow.get("STATUS_CODE") == null ? null :
                                awardRow.get("STATUS_CODE").toString());
            awardDetails.setStatusDesc((String)awardRow.get("DESCRIPTION"));
            awardDetails.setTitle((String)awardRow.get("TITLE"));
            awardDetails.setSponsorCode((String)awardRow.get("SPONSOR_CODE"));
            awardDetails.setSponsorName((String)awardRow.get("SPONSOR_NAME"));
            awardDetails.setInvestigatorName((String)awardRow.get("PERSON_NAME"));
            awardDetails.setFullName((String)awardRow.get("FULL_NAME"));
            awards.add(awardDetails);
        }
        return awards;
    }

    /**
     *  This method populates the list box meant to retrieve the award status for the award search screen.
     *  This method will also throw an exception if the person id for this bean is null.
     *  To fetch the data, it uses a simple database fetch query.
     *  @return HashMap
     *  @exception DBException
     *  @exception CoeusException
     */

    /* CASE #748 */
    // Return HashMap instead of Hashtable
    public HashMap getAwardStatus() throws DBException,CoeusException{
        Vector result = new Vector(3,2);
        Vector param= new java.util.Vector();
        /* case #748 comment begin */
        //Hashtable awdStatus = new Hashtable();
        /* case #748 comment end */
        /* case #748 begin */
        HashMap awdStatus = new HashMap();
        /* case #748 end */
        if(dbEngine!=null){
            result = dbEngine.executeRequest("Coeus", "SELECT * FROM OSP$AWARD_STATUS", "Coeus", param);
        }else{
            throw new DBException("exceptionCode.10001");
        }
        for(int statusCount=0;statusCount<result.size();statusCount++){
            /* case #748 comment begin */
            //Hashtable awdRow = (Hashtable)result.elementAt(statusCount);
            /* case #748 comment end */
            /* case #748 begin */
            HashMap awdRow = (HashMap)result.elementAt(statusCount);
            /* case #748 end */
            awdStatus.put(awdRow.get("STATUS_CODE").toString(),
                                (String)awdRow.get("DESCRIPTION"));
        }
        return awdStatus;
    }
    /*public static void main(String args[]) throws Exception{
        AwardSearchBean bean = new AwardSearchBean();
        bean.getAwardDetails("","","000006-001","","","","","","","","sdfdgd","");
    }*/
}