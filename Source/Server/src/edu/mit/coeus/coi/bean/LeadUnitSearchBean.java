/*
 * @(#)LeadUnitSearchBean.java 1.0 4/06/02
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
/**
 *
 * This class is for doing Lead Unit search in the COI module. All search methods will return
 * a vector which has a collection bean instance for that particular search result.
 *
 * @version 1.0 April 6, 2002, 12:03 PM
 * @author  Anil Nandakumar
 */
public class LeadUnitSearchBean {
    /*
     *  Singleton instance of a dbEngine
     */
    private DBEngineImpl dbEngine;
    /*
     *  To hold the result after the execution of the dbEngine engine.
     */
    private Vector result;
    /** Creates new LeadUnitSearchBean.
     * Contructor with no argument
     */
    public LeadUnitSearchBean() throws DBException,Exception{
        //dbEngine = DBEngineImpl.getInstance();
        dbEngine = new DBEngineImpl();
    }
    /**
     *  This method is used to get all lead unit details for a particular search criterea.
     *  <li>To fetch the data, it uses dw_get_unit_list_for_query procedure.
     *  @param String Lead Unit Number
     *  @param String Lead Unit Name
     *  @param String Admin Officer
     *  @param String Unit Head
     *  @param String Dean VP
     *  @param String Notify
     *  @return Vector vector of <code>LeadUnitDetailsBean</code> instance
     *  @exception DBException
     */
    public Vector getLeadUnitSearchDetails(String leadUnitNumber,
                                           String leadUnitName, String adminOfficer, String unitHead,
                                           String deanVP, String notify)
            throws DBException{
        Vector leadUnits = new Vector(3,2);
        StringBuffer searchQry = new StringBuffer("");
        searchQry.append("SELECT OSP$UNIT.UNIT_NUMBER,OSP$UNIT.UNIT_NAME,OSP$UNIT.ADMINISTRATIVE_OFFICER,  ");
        searchQry.append("OSP$PERSON_A.FULL_NAME  ADMINOFFICER ,  ");
        searchQry.append("OSP$UNIT.UNIT_HEAD, OSP$PERSON_B.FULL_NAME  UNITHEAD, OSP$UNIT.DEAN_VP,  ");
        searchQry.append("OSP$PERSON_C.FULL_NAME  DEANVP, OSP$UNIT.OTHER_INDIVIDUAL_TO_NOTIFY, OSP$PERSON_D.FULL_NAME  NOTIFY,  ");
        searchQry.append("OSP$UNIT.OSP_ADMINISTRATOR, OSP$PERSON_E.FULL_NAME  ADMIN ");
        searchQry.append("FROM 	OSP$UNIT, ");
        searchQry.append("OSP$PERSON OSP$PERSON_A, ");
        searchQry.append("OSP$PERSON OSP$PERSON_B, ");
        searchQry.append("OSP$PERSON OSP$PERSON_C, ");
        searchQry.append("OSP$PERSON OSP$PERSON_D, ");
        searchQry.append("OSP$PERSON OSP$PERSON_E ");
        searchQry.append("WHERE OSP$UNIT.ADMINISTRATIVE_OFFICER = OSP$PERSON_A.PERSON_ID (+)  ");
        searchQry.append("AND OSP$UNIT.UNIT_HEAD = OSP$PERSON_B.PERSON_ID (+)  ");
        searchQry.append("AND OSP$UNIT.DEAN_VP = OSP$PERSON_C.PERSON_ID (+)  ");
        searchQry.append("AND OSP$UNIT.OTHER_INDIVIDUAL_TO_NOTIFY= OSP$PERSON_D.PERSON_ID (+)  ");
        searchQry.append("AND OSP$UNIT.OSP_ADMINISTRATOR= OSP$PERSON_E.PERSON_ID (+)  ");
        if(adminOfficer!=null && !adminOfficer.equals("")){
            searchQry.append(" AND (UPPER(ADMINISTRATIVE_OFFICER) = " + UtilFactory.checkNull(adminOfficer) + ")");
        }
        if(unitHead!=null && !unitHead.equals("")){
            searchQry.append(" AND (UPPER(UNIT_HEAD) = " + UtilFactory.checkNull(unitHead) + ")");
        }
        if(deanVP!=null && !deanVP.equals("")){
            searchQry.append(" AND (UPPER(DEAN_VP) = " + UtilFactory.checkNull(deanVP) + ")");
        }
        if(notify!=null && !notify.equals("")){
            searchQry.append(" AND (UPPER(OTHER_INDIVIDUAL_TO_NOTIFY) =  " + UtilFactory.checkNull(notify) + ")");
        }
        if(leadUnitNumber!=null && !leadUnitNumber.equals("")){
            searchQry.append(" AND (OSP$UNIT.UNIT_NUMBER = " + UtilFactory.checkNull(leadUnitNumber) + ")");
        }
        if(leadUnitName!=null && !leadUnitName.equals("")){
            searchQry.append(" AND (UPPER(OSP$UNIT.UNIT_NAME) = " + UtilFactory.checkNull(leadUnitName) + ")");
        }
        Vector param= new java.util.Vector();
        param.addElement(new Parameter("QUERY","String",searchQry.toString()));
        if(dbEngine!=null){
            result = dbEngine.executeRequest("Coeus", "call dw_get_unit_list_for_query  ( <<QUERY>> , <<OUT RESULTSET rset>> )", "Coeus", param);
        }else{
            throw new DBException("exceptionCode.10001");
        }
        for(int leadUnitCount=0;leadUnitCount<result.size();leadUnitCount++){
            /* case #748 comment begin */
            //Hashtable leadUnitRow = (Hashtable)result.elementAt(leadUnitCount);
            /* case #748 comment end */
            /* case #748 begin */
            HashMap leadUnitRow = (HashMap)result.elementAt(leadUnitCount);
            /* case #748 end */
            LeadUnitDetailsBean leadUnitDetails = new LeadUnitDetailsBean();
            leadUnitDetails.setLeadUnitNumber(
                            leadUnitRow.get("UNIT_NUMBER") == null ? null :
                            leadUnitRow.get("UNIT_NUMBER").toString());
            leadUnitDetails.setLeadUnitName((String)leadUnitRow.get("UNIT_NAME"));
            leadUnitDetails.setAdminOfficer((String)leadUnitRow.get("ADMINOFFICER"));
            leadUnitDetails.setUnitHead((String)leadUnitRow.get("UNITHEAD"));
            leadUnitDetails.setDeanVP((String)leadUnitRow.get("DEANVP"));
            leadUnitDetails.setNotify((String)leadUnitRow.get("OTHER_INDIVIDUAL_TO_NOTIFY"));
            leadUnitDetails.setOSPAdmin((String)leadUnitRow.get("ADMIN"));
            leadUnits.add(leadUnitDetails);
        }
        return leadUnits;
    }
    /*
     *  main method for performing the unit test.
     */
    /*public static void main(String args[]) throws Exception{
        LeadUnitSearchBean bean = new LeadUnitSearchBean();
        bean.getLeadUnitSearchDetails("","dept admin & lab directors","","","","");
    }*/
}
