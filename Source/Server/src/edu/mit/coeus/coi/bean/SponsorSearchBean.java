/*
 * @(#)SponsorSearchBean.java 1.0 4/06/02
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
 * This class is for doing sponsors searches in the COI module. All search method will return
 * a vector which has a collection bean instances for that particular search result.
 *
 * @version 1.0 April 6, 2002, 6:22 PM
 * @author  Anil Nandakumar
 */
public class SponsorSearchBean {
    /*
     *  instance of a dbEngine
     */
    private DBEngineImpl dbEngine;
    /** Creates new SponsorSearchBean.
     * Contructor with no arguments
     */
    public SponsorSearchBean(){
        //dbEngine = DBEngineImpl.getInstance();
        dbEngine = new DBEngineImpl();
    }

    /**
     *  This method is used to get all sponsor details for a particular search criteria.
     *  To fetch the data, it uses dw_get_sponsor_list_for_query procedure.
     *  @param String Sponsor Code
     *  @param String Sponsor Name
     *  @param String Sponsor Type Code
     *  @param String State
     *  @param String Postal Code
     *  @param String Country Code
     *  @param String DunBrad Number
     *  @param String Audit Report Sent
     *  @param String OwnedByUnit
     *  @param String Acronym
     *  @param String DunsP4
     *  @param String Dodac Number
     *  @param String Cage Number
     *  @return Vector vector of <code>SponsorDetailsBean</code> instance
     *  @exception DBException
     */

    public Vector getSponsorDetails(String strSponsorCode,String strSponsorName,
                                    String strSponsorTypeCode,String strState,
                                    String strPostalCode,String strCountryCode,
                                    String strDunBradNumber,String strAuditReportSent,
                                    String strOwnedByUnit,String strAcronym,
                                    String strDunsP4,String strDodacNumber,
                                    String strCageNumber)
                throws DBException{
        Vector result = new Vector(3,2);
        Vector sponsors = new Vector(3,2);
        StringBuffer searchQry = new StringBuffer("");
        searchQry.append("SELECT OSP$SPONSOR.SPONSOR_CODE,  ");
        searchQry.append("OSP$SPONSOR.SPONSOR_NAME,  ");
        searchQry.append("OSP$SPONSOR.SPONSOR_TYPE_CODE,  ");
        searchQry.append("OSP$SPONSOR.ROLODEX_ID, ");
        searchQry.append("OSP$ROLODEX.STATE,  ");
        searchQry.append("OSP$ROLODEX.POSTAL_CODE,  ");
        searchQry.append("OSP$ROLODEX.COUNTRY_CODE,  ");
        searchQry.append("OSP$SPONSOR.DUN_AND_BRADSTREET_NUMBER,  ");
        searchQry.append("OSP$SPONSOR.AUDIT_REPORT_SENT_FOR_FY,  ");
        searchQry.append("OSP$SPONSOR.OWNED_BY_UNIT,  ");
        searchQry.append("OSP$SPONSOR.ACRONYM,  ");
        searchQry.append("OSP$SPONSOR.DUNS_PLUS_FOUR_NUMBER,  ");
        searchQry.append("OSP$SPONSOR.DODAC_NUMBER,  ");
        searchQry.append("OSP$SPONSOR.CAGE_NUMBER  ");
        searchQry.append("FROM OSP$SPONSOR, OSP$ROLODEX   ");
        searchQry.append("WHERE ( OSP$SPONSOR.ROLODEX_ID = OSP$ROLODEX.ROLODEX_ID )  ");
        if(strSponsorCode  !=null && !strSponsorCode  .equals("")){
            searchQry.append("AND (UPPER(OSP$SPONSOR.SPONSOR_CODE) = " + UtilFactory.checkNull(strSponsorCode) + ")");
        }
        if(strSponsorName !=null && !strSponsorName .equals("")){
            searchQry.append("AND (UPPER(OSP$SPONSOR.SPONSOR_NAME) = " + UtilFactory.checkNull(strSponsorName) + ")");
        }
        if(strSponsorTypeCode!=null && !strSponsorTypeCode.equals("")){
            searchQry.append("AND (UPPER(OSP$SPONSOR.SPONSOR_TYPE_CODE) = " + UtilFactory.checkNull(strSponsorTypeCode) + ")");
        }
        if(strState!=null && !strState.equals("")){
            searchQry.append("AND (UPPER(OSP$ROLODEX.STATE) = " + UtilFactory.checkNull(strState) + ")");
        }
        if(strPostalCode!=null && !strPostalCode.equals("")){
            searchQry.append("AND (UPPER(OSP$ROLODEX.POSTAL_CODE) = " + UtilFactory.checkNull(strPostalCode) + ")");
        }
        if(strCountryCode!=null && !strCountryCode.equals("")){
            searchQry.append("AND (UPPER(OSP$ROLODEX.COUNTRY_CODE) = " + UtilFactory.checkNull(strCountryCode) + ")");
        }
        if(strDunBradNumber!=null && !strDunBradNumber.equals("")){
            searchQry.append("AND (UPPER(OSP$SPONSOR.DUN_AND_BRADSTREET_NUMBER) = " + UtilFactory.checkNull(strDunBradNumber) + ")");
        }
        if(strAuditReportSent!=null && !strAuditReportSent.equals("")){
            searchQry.append("AND (UPPER(OSP$SPONSOR.AUDIT_REPORT_SENT_FOR_FY) = " + UtilFactory.checkNull(strAuditReportSent) + ")");
        }
        if(strOwnedByUnit!=null && !strOwnedByUnit.equals("")){
            searchQry.append("AND (UPPER(OSP$SPONSOR.OWNED_BY_UNIT) = " + UtilFactory.checkNull(strOwnedByUnit) + ")");
        }
        if(strAcronym!=null && !strAcronym.equals("")){
            searchQry.append("AND (UPPER(OSP$SPONSOR.ACRONYM) = " + UtilFactory.checkNull(strAcronym) + ")");
        }
        if(strDunsP4!=null && !strDunsP4.equals("")){
            searchQry.append("AND (UPPER(OSP$SPONSOR.DUNS_PLUS_FOUR_NUMBER) = " + UtilFactory.checkNull(strDunsP4) + ")");
        }
        if(strDodacNumber!=null && !strDodacNumber.equals("")){
            searchQry.append("AND (UPPER(OSP$SPONSOR.DODAC_NUMBER) = " + UtilFactory.checkNull(strDodacNumber) + ")");
        }
        if(strCageNumber!=null && !strCageNumber.equals("")){
            searchQry.append("AND (UPPER(OSP$SPONSOR.CAGE_NUMBER) = " + UtilFactory.checkNull(strCageNumber) + ")");
        }
        Vector param= new java.util.Vector();
        param.addElement(new Parameter("QUERY","String",searchQry.toString()));
        if(dbEngine!=null){
            result = dbEngine.executeRequest("Coeus", "call dw_get_sponsor_list_for_query  ( <<QUERY>> , <<OUT RESULTSET rset>> )", "Coeus", param);
        }else{
            throw new DBException("exceptionCode.10001");
        }
        for(int i=0;i<result.size();i++){
            /* case #748 comment begin */
            //Hashtable sponsorRow = (Hashtable)result.elementAt(i);
            /* case #748 comment end */
            /* case #748 begin */
            HashMap sponsorRow = (HashMap)result.elementAt(i);
            /* case #748 end */
            SponsorDetailsBean sponsorDetails = new SponsorDetailsBean();
            sponsorDetails.setSponsorCode((String)sponsorRow.get("SPONSOR_CODE"));
            sponsorDetails.setSponsorName((String)sponsorRow.get("SPONSOR_NAME"));
            sponsorDetails.setSponsorTypeCode((String)sponsorRow.get("SPONSOR_TYPE_CODE"));
            sponsorDetails.setState((String)sponsorRow.get("STATE"));
            sponsorDetails.setPostalCode((String)sponsorRow.get("POSTAL_CODE"));
            sponsorDetails.setCountryCode((String)sponsorRow.get("COUNTRY_CODE"));
            sponsorDetails.setDunBradNumber((String)sponsorRow.get("DUN_AND_BRADSTREET_NUMBER"));
            sponsorDetails.setAuditReportSent((String)sponsorRow.get("AUDIT_REPORT_SENT_FOR_FY"));
            sponsorDetails.setOwnedByUnit((String)sponsorRow.get("OWNED_BY_UNIT"));
            sponsorDetails.setAcronym((String)sponsorRow.get("ACRONYM"));
            sponsorDetails.setDunsP4((String)sponsorRow.get("DUNS_PLUS_FOUR_NUMBER"));
            sponsorDetails.setDodacNumber((String)sponsorRow.get("DODAC_NUMBER"));
            sponsorDetails.setCageNumber((String)sponsorRow.get("CAGE_NUMBER"));
            sponsors.add(sponsorDetails);
        }
        return sponsors;
    }
    /**
     *  This method populates the list box meant to retrieve the Sponsor Type in the sponsor search screen.
     *  To fetch the data, it uses the procedure dw_get_sponsor_type.
     *  @return vector map of all sponsor type with sponsor code as key and
                            sponsor type description as value
     *  @exception DBException
     */
    public Vector getSponsorType() throws DBException{
        Vector result = new Vector(3,2);
        Vector param= new java.util.Vector();
        Vector sponsorTypes = new Vector();
        if(dbEngine!=null){
            result = dbEngine.executeRequest("Coeus",
                "call dw_get_sponsor_type  ( <<OUT RESULTSET rset>> )",
                "Coeus", param);
        }else{
            throw new DBException("exceptionCode.10001");
        }
        for(int i=0;i<result.size();i++){
            /* case #748 comment begin */
            //Hashtable sponsorRow = (Hashtable)result.elementAt(i);
            /* case #748 comment end */
            /* case #748 begin */
            HashMap sponsorRow = (HashMap)result.elementAt(i);
            /* case #748 end */
            sponsorTypes.addElement
              (new ComboBoxBean(sponsorRow.get("SPONSOR_TYPE_CODE").toString(),
              (String)sponsorRow.get("DESCRIPTION")));
        }
        return sponsorTypes;
    }
    /**
     *  This method populates the list box meant to retrieve the states in the sponsor search screen.
     *  To fetch the data, it uses the procedure dw_get_state_codes.
     *  @return vector of all states with state code as key and state description as value
     *  @exception DBException
     */
    public Vector getState() throws DBException{
        Vector result = new Vector(3,2);
        Vector param= new java.util.Vector();
        Vector states = new Vector();
        if(dbEngine!=null){
            result = dbEngine.executeRequest("Coeus",
              "call dw_get_state_codes  ( <<OUT RESULTSET rset>> )",
              "Coeus", param);
        }else{
            throw new DBException("exceptionCode.10001");
        }
        for(int i=0;i<result.size();i++){
            /* case #748 comment begin */
            //Hashtable stateRow = (Hashtable)result.elementAt(i);
            /* case #748 comment end */
            /* case #748 begin */
            HashMap stateRow = (HashMap)result.elementAt(i);
            /* case #748 end */
            states.addElement(
              new ComboBoxBean(stateRow.get("STATE_CODE").toString(),
              (String)stateRow.get("DESCRIPTION")));
        }
        return states;
    }
    /**
     *  This method populates the list box meant to retrieve the countries in the sponsor search screen.
     *  To fetch the data, it uses the procedure dw_get_country_code.
     *  @return vector list of all countries with country code as key and country name as value.
     *  @exception DBException
     */
    public Vector getCountry() throws DBException{
        Vector result = new Vector(3,2);
        Vector param= new java.util.Vector();
        Vector countries = new Vector();
        if(dbEngine!=null){
            result = dbEngine.executeRequest("Coeus",
              "call dw_get_country_code  ( <<OUT RESULTSET rset>> )",
              "Coeus", param);
        }else{
            throw new DBException("exceptionCode.10001");
        }
        for(int i=0;i<result.size();i++){
            /* case #748 comment begin */
            //Hashtable countryRow = (Hashtable)result.elementAt(i);
            /* case #748 comment end */
            /* case #748 begin */
            HashMap countryRow = (HashMap)result.elementAt(i);
            /* case #748 end */
            countries.addElement(new ComboBoxBean
              (countryRow.get("COUNTRY_CODE").toString(),
              countryRow.get("COUNTRY_NAME").toString()));
            //countries.put(countryRow.get("COUNTRY_CODE").toString(),countryRow.get("COUNTRY_NAME").toString());
        }
        return countries;
    }

    //Main method for testing
    /*public static void main(String args[]) throws DBException{
        SponsorSearchBean bean = new SponsorSearchBean();
        bean.getSponsorDetails("","","","","","","","","","advanced","","","");
    }*/
}