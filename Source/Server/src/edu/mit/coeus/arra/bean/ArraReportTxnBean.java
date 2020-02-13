/*
 * ArraReportTxnBean.java
 *
 * Created on September 21, 2009, 2:02 PM
 *
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 */

package edu.mit.coeus.arra.bean;

import edu.mit.coeus.award.bean.AwardTxnBean;
import edu.mit.coeus.exception.CoeusException;
import edu.mit.coeus.instprop.bean.InvestigatorUnitAdminTypeBean;
import edu.mit.coeus.utils.CoeusVector;
import edu.mit.coeus.utils.dbengine.DBEngineConstants;
import edu.mit.coeus.utils.dbengine.DBEngineImpl;
import edu.mit.coeus.utils.dbengine.DBException;
import edu.mit.coeus.utils.dbengine.Parameter;
import edu.mit.coeus.arra.bean.ArraReportBean;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Vector;

/**
 *
 * @author keerthyjayaraj
 */
public class ArraReportTxnBean {
    
    private DBEngineImpl dbEngine;
    private String userId;
    /** Creates a new instance of ArraReportTxnBean */
    public ArraReportTxnBean() {
        dbEngine = new DBEngineImpl();
    }
    
    public ArraReportTxnBean(String userId) {
        super();
        this.userId = userId;
        dbEngine = new DBEngineImpl();
    }
    
    
    /**
     * This method is used to fetch all the award details of arra reporting.
     * <li>To fetch the data, it uses the procedure GET_ARRA_AWARD_DETAILS.
     * @param reportNumber - The Arra report Number.
     * @param mitAwardNumber - The mit award number.
     * @return detailBean - The ArraAwardDetailsBean
     * @exception DBException if the instance of a dbEngine is null.
     */
    public ArraAwardDetailsBean getArraAwardDetails(int reportNumber,String mitAwardNumber) throws DBException{
        
        Vector result = new Vector(3,2);
        ArraAwardDetailsBean detailBean = null;
        
        Vector param= new Vector();
        param.addElement(new Parameter("AV_ARRA_REPORT_NUMBER", DBEngineConstants.TYPE_INT, Integer.toString(reportNumber)));
        param.addElement(new Parameter("AV_MIT_AWARD_NUMBER", DBEngineConstants.TYPE_STRING, mitAwardNumber));
        
        if(dbEngine != null){
            result = dbEngine.executeRequest("Coeus",
                    "call GET_ARRA_AWARD_DETAILS(<<AV_ARRA_REPORT_NUMBER>>,<<AV_MIT_AWARD_NUMBER>>, <<OUT RESULTSET rset>> ) ",
                    "Coeus", param);
        }
        if(result != null && result.size() > 0){
            HashMap hmRow = null;
            for(int i = 0; i<result.size(); i++){
                hmRow = (HashMap)result.get(i);
                detailBean = new ArraAwardDetailsBean();
                detailBean.setArraReportNumber(getIntValue(hmRow.get("ARRA_REPORT_NUMBER")));
                detailBean.setVersionNumber(getIntValue(hmRow.get("VERSION_NUMBER")));
                detailBean.setMitAwardNumber((String)hmRow.get("MIT_AWARD_NUMBER"));
                detailBean.setSequenceNumber(getIntValue(hmRow.get("SEQUENCE_NUMBER")));
                detailBean.setProjectStatus((String)hmRow.get("PROJECT_STATUS"));
                detailBean.setProjectTitle((String)hmRow.get("PROJECT_TITLE"));
                detailBean.setActivityCode((String)hmRow.get("ACTIVITY_CODE"));
                detailBean.setAwardingAgencyCode((String)hmRow.get("AWARDING_AGENCY_CODE"));
                detailBean.setFundingAgencyCode((String)hmRow.get("FUNDING_AGENCY_CODE"));
                detailBean.setAwardDescription((String)hmRow.get("AWARD_DESCRIPTION"));
                detailBean.setActivityDescription((String)hmRow.get("ACTIVITY_DESCRIPTION"));
                detailBean.setNoOfJobs(getDoubleValue(hmRow.get("NUMBER_OF_JOBS")));
                detailBean.setJobsAtSubs(getDoubleValue(hmRow.get("JOBS_AT_SUBS")));
                detailBean.setEmploymentImpact((String)hmRow.get("JOBS_CREATED_DESCRIPTION"));
                if(hmRow.get("INFRASTRUCTURE_CONTACT_ID")!=null){
                    detailBean.setInfraContactId(hmRow.get("INFRASTRUCTURE_CONTACT_ID").toString());
                }
                detailBean.setInfrastructureRationale((String)hmRow.get("INFRASTRUCTURE_RATIONALE"));
                if(hmRow.get("PRIM_PLACE_OF_PERF")!=null){
                    detailBean.setPrimPlaceOfPerfId(hmRow.get("PRIM_PLACE_OF_PERF").toString());
                }
                detailBean.setPrimPlaceCongDistrict((String)hmRow.get("PRIM_PLACE_OF_PERF_CONG_DIST"));
                detailBean.setAwardNumber((String)hmRow.get("AWARD_NUMBER"));
                detailBean.setAccountNumber((String)hmRow.get("ACCOUNT_NUMBER"));
                detailBean.setAwardType((String)hmRow.get("AWARD_TYPE"));
                detailBean.setAwardDate(getDateValue(hmRow.get("AWARD_DATE")));
                detailBean.setAwardAmount(getDoubleValue(hmRow.get("AWARD_AMOUNT")));
                detailBean.setTotalFederalInvoiced(getDoubleValue(hmRow.get("TOTAL_FED_ARRA_FUNDS_INVOICED")));
                detailBean.setTotalExpenditure(getDoubleValue(hmRow.get("TOTAL_FED_ARRA_EXPENDITURE")));
                detailBean.setTotalInfraExpenditure(getDoubleValue(hmRow.get("TOTAL_FED_ARRA_INFRA_EXPEND")));
                detailBean.setRecipientDUNSNumber((String)hmRow.get("RECIPIENT_DUNS_NUMBER"));
                detailBean.setCfdaNumber((String)hmRow.get("CFDA_NUMBER"));
                detailBean.setGovContractingOfficeCode((String)hmRow.get("GOV_CONTRACTING_OFFICE_CODE"));
                detailBean.setIndSubAwards(getIntValue(hmRow.get("NUM_OF_SUBS_TO_INDIVIDUALS")));
                detailBean.setIndSubAwardAmount(getDoubleValue(hmRow.get("AMOUNT_OF_SUBS_TO_INDIVIDUALS")));
                detailBean.setVendorLess25K(getIntValue(hmRow.get("NUM_OF_PAY_VENDOR_LESS_25K")));
                detailBean.setVendorLess25KAmount(getDoubleValue(hmRow.get("AMOUNT_OF_PAY_VENDOR_LESS_25K")));
                detailBean.setSubAwdLess25K(getIntValue(hmRow.get("NUM_OF_SUBS_LESS_25K")));
                detailBean.setSubAwdLess25KAmount(getDoubleValue(hmRow.get("AMOUNT_OF_SUBS_LESS_25K")));
                detailBean.setOrderNumber((String)hmRow.get("ORDER_NUMBER"));
                detailBean.setRecipientCongDistrict((String)hmRow.get("RECIPIENT_CONG_DISTRICT"));
                detailBean.setIndicationOfReporting((String)hmRow.get("INDICATION_OF_REPORTING"));       
                detailBean.setFinalReportIndicator((String)hmRow.get("FINAL_REPORT_FLAG"));
                detailBean.setAgencyTAS((String)hmRow.get("AGENCY_TAS"));
                detailBean.setUpdateTimestamp((Timestamp)hmRow.get("UPDATE_TIMESTAMP"));
                detailBean.setUpdateUser((String)hmRow.get("UPDATE_USER"));
                detailBean.setTasSubCode((String)hmRow.get("TAS_SUB_CODE"));
            }
        }
        return detailBean;
    }
    
    
    /**
     * This method is used to fetch the list of vendors.
     * Vendors can be applicable at award level or subcontract level.
     * If the award level vendors are required, pass subContractCode as null.
     * <li>To fetch the data, it uses the procedure GET_ARRA_AWARD_VENDORS.
     * @param reportNumber - The Arra report Number.
     * @param mitAwardNumber - The mit award number.
     * @param subContractCode - The subcontract code.
     * @return vctVendors - The Vector of  ArraVendorBean
     * @exception DBException if the instance of a dbEngine is null.
     */
    public Vector getArraVendors(int reportNumber,String mitAwardNumber,String subContractCode) throws DBException{
        
        Vector result = new Vector(3,2);
        ArraVendorBean vendor = null;
        Vector vctVendors = null;
        Vector param= new Vector();
        param.addElement(new Parameter("AV_ARRA_REPORT_NUMBER", DBEngineConstants.TYPE_INT, Integer.toString(reportNumber)));
        param.addElement(new Parameter("AV_MIT_AWARD_NUMBER", DBEngineConstants.TYPE_STRING, mitAwardNumber));
        param.addElement(new Parameter("AV_SUBCONTRACT_CODE", DBEngineConstants.TYPE_STRING, subContractCode));
        
        if(dbEngine != null){
            result = dbEngine.executeRequest("Coeus",
                    "call GET_ARRA_AWARD_VENDORS(<<AV_ARRA_REPORT_NUMBER>>,<<AV_MIT_AWARD_NUMBER>>, <<AV_SUBCONTRACT_CODE>>, <<OUT RESULTSET rset>> ) ",
                    "Coeus", param);
        }
        if(result != null && result.size() > 0){
            HashMap hmRow = null;
            vctVendors = new Vector();
            for(int i = 0; i<result.size(); i++){
                hmRow = (HashMap)result.get(i);
                vendor = new ArraVendorBean();
                vendor.setArraReportNumber(getIntValue(hmRow.get("ARRA_REPORT_NUMBER")));
                vendor.setVersionNumber(getIntValue(hmRow.get("VERSION_NUMBER")));
                vendor.setMitAwardNumber((String)hmRow.get("MIT_AWARD_NUMBER"));
                vendor.setSubContractCode((String)hmRow.get("SUBCONTRACT_CODE"));
                vendor.setVendorNumber((String)hmRow.get("VENDOR_NUMBER"));
                vendor.setVendorDUNS((String)hmRow.get("VENDOR_DUNS"));
                vendor.setVendorHQZipCode((String)hmRow.get("VENDOR_HQ_ZIP_CODE"));
                vendor.setVendorName((String)hmRow.get("VENDOR_NAME"));
                vendor.setServiceDescription((String)hmRow.get("SERVICE_DESCRIPTION"));
                vendor.setPaymentAmount(getDoubleValue(hmRow.get("PAYMENT_AMOUNT")));
                vendor.setUpdateTimestamp((Timestamp)hmRow.get("UPDATE_TIMESTAMP"));
                vendor.setUpdateUser((String)hmRow.get("UPDATE_USER"));
                vctVendors.add(vendor);
            }
        }
        return vctVendors;
    }
    
    
    /**
     * This method is used to fetch list of highly compensated individuals.
     * <li>To fetch the data, it uses the procedure GET_ARRA_AWARD_HIGHLY_COMP.
     * @param organizationId - The organization Id
     * @return vctIndividuals - The Vector of  ArraVendorBean
     * @exception DBException if the instance of a dbEngine is null.
     */
    public Vector getArraHighlyCompensated(String organizationId) throws DBException{
        
        Vector result = new Vector(3,2);
        ArraHighlyCompensatedBean highlyComp = null;
        Vector vctIndividuals = null;
        Vector param= new Vector();
        param.addElement(new Parameter("AV_ORGANIZATION_ID", DBEngineConstants.TYPE_STRING, organizationId));
        
        if(dbEngine != null){
            result = dbEngine.executeRequest("Coeus",
                    "call GET_ARRA_AWARD_HIGHLY_COMP( <<AV_ORGANIZATION_ID>>, <<OUT RESULTSET rset>> ) ",
                    "Coeus", param);
        }
        if(result != null && result.size() > 0){
            HashMap hmRow = null;
            vctIndividuals = new Vector();
            for(int i = 0; i<result.size(); i++){
                hmRow = (HashMap)result.get(i);
                highlyComp = new ArraHighlyCompensatedBean();
                highlyComp.setOrganizationId((String)hmRow.get("ORGANIZATION_ID"));
                highlyComp.setPersonNumber(getIntValue(hmRow.get("PERSON_NUMBER")));
                highlyComp.setPersonName((String)hmRow.get("PERSON_NAME"));
                highlyComp.setCompensation(getDoubleValue(hmRow.get("COMPENSATION")));
                highlyComp.setUpdateTimestamp((Timestamp)hmRow.get("UPDATE_TIMESTAMP"));
                highlyComp.setUpdateUser((String)hmRow.get("UPDATE_USER"));
                vctIndividuals.add(highlyComp);
            }
        }
        return vctIndividuals;
    }
    
    /**
     * This method is used to fetch the list of subcontracts.
     * <li>To fetch the data, it uses the procedure GET_ARRA_AWARD_SUBCONTRACTS.
     * @param reportNumber - The Arra report Number.
     * @param mitAwardNumber - The mit award number.
     * @return vctSubcontracts - The Vector of  ArraAwardSubcontractBean
     * @exception DBException if the instance of a dbEngine is null.
     */
    public Vector getArraAwardSubcontracts(int reportNumber,String mitAwardNumber) throws DBException{
        
        Vector result = new Vector(3,2);
        ArraAwardSubcontractBean subcontract = null;
        Vector vctSubcontracts = null;
        Vector param= new Vector();
        param.addElement(new Parameter("AV_ARRA_REPORT_NUMBER", DBEngineConstants.TYPE_INT, Integer.toString(reportNumber)));
        param.addElement(new Parameter("AV_MIT_AWARD_NUMBER", DBEngineConstants.TYPE_STRING, mitAwardNumber));
        
        if(dbEngine != null){
            result = dbEngine.executeRequest("Coeus",
                    "call GET_ARRA_AWARD_SUBCONTRACTS(<<AV_ARRA_REPORT_NUMBER>>,<<AV_MIT_AWARD_NUMBER>>, <<OUT RESULTSET rset>> ) ",
                    "Coeus", param);
        }
        if(result != null && result.size() > 0){
            HashMap hmRow = null;
            vctSubcontracts = new Vector();
            for(int i = 0; i<result.size(); i++){
                hmRow = (HashMap)result.get(i);
                subcontract = new ArraAwardSubcontractBean();
                subcontract.setArraReportNumber(getIntValue(hmRow.get("ARRA_REPORT_NUMBER")));
                subcontract.setVersionNumber(getIntValue(hmRow.get("VERSION_NUMBER")));
                subcontract.setMitAwardNumber((String)hmRow.get("MIT_AWARD_NUMBER"));
                subcontract.setSubcontractCode((String)hmRow.get("SUBCONTRACT_CODE"));
                subcontract.setSubcontractNo((String)hmRow.get("SUBCONTRACT_NUMBER"));
                subcontract.setSubcontractorID((String)hmRow.get("SUBCONTRACTOR_ID"));
                subcontract.setSubcontractorName((String)hmRow.get("SUBCONTRACTOR_NAME"));
                subcontract.setSubAwardAmount(getDoubleValue(hmRow.get("SUBAWARD_AMOUNT")));
                subcontract.setSubAwardAmtDispursed(getDoubleValue(hmRow.get("SUBAWARD_AMOUNT_DISPURSED")));
                subcontract.setNoOfJobs(getDoubleValue(hmRow.get("NUMBER_OF_JOBS")));
                subcontract.setSubAwardDate(getDateValue(hmRow.get("SUBAWARD_DATE")));
                if(hmRow.get("PRIM_PLACE_OF_PERF")!=null){
                    subcontract.setPrimPlaceOfPerfId(hmRow.get("PRIM_PLACE_OF_PERF").toString());
                }
                subcontract.setPrimPlaceCongDist((String)hmRow.get("PRIM_PLACE_OF_PERF_CONG_DIST"));
                subcontract.setSubRecipientDUNS((String)hmRow.get("SUB_RECIPIENT_DUNS"));
                subcontract.setSubRecipientCongDist((String)hmRow.get("SUB_RECIPIENT_CONG_DIST"));
                subcontract.setIndicationOfReporting((String)hmRow.get("INDICATION_OF_REPORTING"));
                vctSubcontracts.add(subcontract);
            }
        }
        return vctSubcontracts;
    }
    
    /**
     * This method is used to fetch the list of jobs.
     * <li>To fetch the data, it uses the procedure GET_ARRA_REPORT_JOBS.
     * @param reportNumber - The Arra report Number.
     * @param mitAwardNumber - The mit award number.
     * @return vctJobs - The Vector of  ArraReportJobBean
     * @exception DBException if the instance of a dbEngine is null.
     */
    public Vector getArraReportJobs(int reportNumber,String mitAwardNumber) throws DBException{
        
        Vector result = new Vector(3,2);
        ArraReportJobBean job = null;
        Vector vctJobs = null;
        Vector param= new Vector();
        param.addElement(new Parameter("AV_ARRA_REPORT_NUMBER", DBEngineConstants.TYPE_INT, Integer.toString(reportNumber)));
        param.addElement(new Parameter("AV_MIT_AWARD_NUMBER", DBEngineConstants.TYPE_STRING, mitAwardNumber));
        
        if(dbEngine != null){
            result = dbEngine.executeRequest("Coeus",
                    "call GET_ARRA_REPORT_JOBS(<<AV_ARRA_REPORT_NUMBER>>,<<AV_MIT_AWARD_NUMBER>>, <<OUT RESULTSET rset>> ) ",
                    "Coeus", param);
        }
        if(result != null && result.size() > 0){
            HashMap hmRow = null;
            vctJobs = new Vector();
            for(int i = 0; i<result.size(); i++){
                hmRow = (HashMap)result.get(i);
                job = new ArraReportJobBean();
                job.setArraReportNumber(getIntValue(hmRow.get("ARRA_REPORT_NUMBER")));
                job.setVersionNumber(getIntValue(hmRow.get("VERSION_NUMBER")));
                job.setMitAwardNumber((String)hmRow.get("MIT_AWARD_NUMBER"));
                job.setPersonId((String)hmRow.get("PERSON_ID"));
                job.setPersonName((String)hmRow.get("PERSON_NAME"));
                job.setJobTitle((String)hmRow.get("JOB_TITLE"));
                job.setFte(getDoubleValue(hmRow.get("FTE")));
                vctJobs.add(job);
            }
        }
        return vctJobs;
    }
    
    /**
     *  This method is to get the coeus country code and arra country codes mapping.
     *  To fetch the data, it uses the procedure get_arra_country_codes.
     *  @return HashMap of all countries with country code as key and
     *  arra country code as value.
     *  @exception DBException
     */
    public HashMap getCountryCodes() throws CoeusException, DBException{
        Vector result = new Vector(3,2);
        Vector param= new Vector();
        HashMap countries = new HashMap();
        if(dbEngine!=null){
            result = dbEngine.executeRequest("Coeus",
                    "call get_country_code_mapping ( <<OUT RESULTSET rset>> )",
                    "Coeus", param);
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }
        if(result != null && result.size() > 0){
            HashMap hmRow = null;
            for(int i=0;i<result.size();i++){
                hmRow = (HashMap)result.elementAt(i);
                countries.put(
                        (String) hmRow.get("COUNTRY_CODE"),
                        (String) hmRow.get("TARGET_COUNTRY_CODE"));
            }
        }
        return countries;
    }
    /**
     *  This method is to get the editable columns for table.
     *  To fetch the data, it uses the procedure GET_ARRA_COLUMN_PROPERTIES.
     *  @return HashMap of all columns with column name and 
     *  the editable right with 'yes' or 'no' value
     *  @exception DBException
     */
      public HashMap getEditableColumnNames(String tableName) throws CoeusException, DBException{
        Vector result = new Vector(3,2);
        Vector param= new Vector();
        param.addElement(new Parameter("AV_TABLE_NAME",DBEngineConstants.TYPE_STRING,tableName));
        HashMap columnNames = new HashMap();
        if(dbEngine!=null){
            result = dbEngine.executeRequest("Coeus",
                    "call GET_ARRA_COLUMN_PROPERTIES(<<AV_TABLE_NAME>>,<<OUT RESULTSET rset>>)",
                    "Coeus", param);
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }
        if(result != null && result.size() > 0){
            HashMap hmRow = null;
            for(int i=0;i<result.size();i++){
                hmRow = (HashMap)result.elementAt(i);
                columnNames.put(
                        (String) hmRow.get("COLUMN_NAME"),
                        (String) hmRow.get("EDITABLE"));
            }
        }
        return columnNames;
    }

       /**
     *  This method is to get the start and end date for the arra .
     *  To fetch the data, it uses the procedure GET_START_END_PERIOD_FOR_ARRA.
     *  @return HashMap of all start and end date
     *  @exception DBException
     */
      public ArraReportBean getStartAndEndDatePeriodOfArra() throws CoeusException, DBException{
        Vector result = new Vector(3,2);
        Vector param= new Vector();
        Vector dateVector = new Vector();
        ArraReportBean arraReportBean = new ArraReportBean();
        if(dbEngine!=null){
            result = dbEngine.executeRequest("Coeus",
                    "call GET_START_END_PERIOD_FOR_ARRA(<<OUT RESULTSET rset>>)",
                    "Coeus", param);
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }
        if(result != null && result.size() > 0){
            HashMap hmRow = null;
            for(int i=0;i<result.size();i++){
                hmRow = (HashMap)result.elementAt(i);
                arraReportBean.setArraReportNumber(getIntValue(hmRow.get("ARRA_REPORT_NUMBER")));
                arraReportBean.setReportPeriodStartDate(getDateValue(hmRow.get("REPORT_PERIOD_START")));
                arraReportBean.setReportPeriodEndDate(getDateValue(hmRow.get("REPORT_PERIOD_END")));                
                arraReportBean.setArraReportStatusCode(getIntValue(hmRow.get("ARRA_REPORT_STATUS_CODE")));
                arraReportBean.setLoadDate(getDateValue(hmRow.get("DATA_LOAD_DATE")));
                arraReportBean.setSubmissionDate(getDateValue(hmRow.get("SUBMISSION_DATE")));
                arraReportBean.setUpdateDate(getDateValue(hmRow.get("UPDATE_TIMESTAMP")));
                arraReportBean.setUpdateUser((String)hmRow.get("UPDATE_USER"));
               // dateVector.add(arraReportBean);
            }
        }
        return arraReportBean;
    }
      //Check Arra Column properties
     /* This function is used to check  Arra Column properties
      * @param tableName - table Name
      * @return HashMap of column Names and editable right
      */
      public HashMap checkArraColumnProperties(String tableName) throws CoeusException,DBException{
          HashMap columnNames = new HashMap();
          ArraReportTxnBean arraReportTxnBean = new ArraReportTxnBean();
          columnNames = arraReportTxnBean.getEditableColumnNames(tableName);
          return columnNames;
      }
    private int getIntValue(Object value){
        return (value==null)? 0 : Integer.parseInt(value.toString());
    }
    
    private double getDoubleValue(Object value){
        return (value==null)? 0 : Double.parseDouble(value.toString());
    }
    
    private Date getDateValue(Object value){
        return (value==null)? null : new Date(((Timestamp) value).getTime());
    }
    // Arra Phase 2 Changes - Start
    /**
     * This method is used to check if the user is PI for arra award.
     */
    public boolean isUserPiForArraAward(String userId, String mitAwardNumber, int versionNumber, int reportNumber) throws Exception{
        boolean isPi = false;
        
        
        Vector param = new Vector();
        Vector result = null;
        int count = -1;
        param.add(new Parameter("AV_ARRA_AWARD_NUMBER",DBEngineConstants.TYPE_STRING,mitAwardNumber));
        param.add(new Parameter("AV_ARRA_AWARD_VERSION_NUMBER",DBEngineConstants.TYPE_INT,new Integer(versionNumber)));
        param.add(new Parameter("AV_ARRA_AWARD_REPORT_NUMBER",DBEngineConstants.TYPE_INT,new Integer(reportNumber)));
        param.add(new Parameter("AV_USER_ID",DBEngineConstants.TYPE_STRING,userId));
        if(dbEngine != null) {
            result = dbEngine.executeFunctions("Coeus",
                    "{ <<OUT INTEGER COUNT>> = "
                    +" call FN_USER_IS_ARRA_AWARD_PI(<<AV_ARRA_AWARD_NUMBER>>, <<AV_ARRA_AWARD_VERSION_NUMBER>>, <<AV_ARRA_AWARD_REPORT_NUMBER>>, <<AV_USER_ID>>) }", param);
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }
        if(!result.isEmpty()){
            HashMap rowParameter = (HashMap)result.elementAt(0);
            if(rowParameter.get("COUNT") != null){
                rowParameter = (HashMap)result.elementAt(0);
                count = Integer.parseInt(rowParameter.get("COUNT").toString());
            }
        }
        
        if(count > 0){
            isPi = true;
        }
        
        return isPi;
    }
    
    /**
     * This returns the lead unit of the arra award.
     */
    
    public String fetchLeadUnitForArraAward(String arraAwardNumber, int versionNumber, int reportNumber) throws Exception{
        String leadUnit = "";
        
        
        Vector param = new Vector();
        Vector result = null;
        int count = -1;
        param.add(new Parameter("AV_ARRA_AWARD_NUMBER",DBEngineConstants.TYPE_STRING,arraAwardNumber));
        param.add(new Parameter("AV_VERSION_NUMBER",DBEngineConstants.TYPE_INT,new Integer(versionNumber)));
        param.add(new Parameter("AV_REPORT_NUMBER",DBEngineConstants.TYPE_INT,new Integer(reportNumber)));
        
        if(dbEngine != null) {
            result = dbEngine.executeFunctions("Coeus",
                    "{ <<OUT STRING LEAD_UNIT>> = "
                    +" call FN_GET_LEAD_UNIT_FOR_ARRA_AWD(<<AV_ARRA_AWARD_NUMBER>>, <<AV_VERSION_NUMBER>>, <<AV_REPORT_NUMBER>>) }", param);
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }
        if(!result.isEmpty()){
            HashMap rowParameter = (HashMap)result.elementAt(0);
            if(rowParameter.get("LEAD_UNIT") != null){
                rowParameter = (HashMap)result.elementAt(0);
                leadUnit = rowParameter.get("LEAD_UNIT").toString();
            }
        }
        
        return leadUnit;
    }
    /**
     * This method is used to fetch all the award details of arra reporting.
     * <li>To fetch the data, it uses the procedure GET_ARRA_AWARD_DETAILS.
     * @param reportNumber - The Arra report Number.
     * @param mitAwardNumber - The mit award number.
     * @return detailBean - The ArraAwardDetailsBean
     * @exception DBException if the instance of a dbEngine is null.
     */
    public ArraAwardDetailsBean getPastArraAwardDetails(String mitAwardNumber) throws DBException{
        
        Vector result = new Vector(3,2);
        ArraAwardDetailsBean detailBean = null;
        
        Vector param= new Vector();        
        param.addElement(new Parameter("AV_MIT_AWARD_NUMBER", DBEngineConstants.TYPE_STRING, mitAwardNumber));
        
        if(dbEngine != null){
            result = dbEngine.executeRequest("Coeus",
                    "call GET_PAST_REPORTS_FOR_ARRA(<<AV_MIT_AWARD_NUMBER>>, <<OUT RESULTSET rset>> ) ",
                    "Coeus", param);
        }
        if(result != null && result.size() > 0){
            HashMap hmRow = null;
            for(int i = 0; i<result.size(); i++){
                hmRow = (HashMap)result.get(i);
                detailBean = new ArraAwardDetailsBean();
                detailBean.setArraReportNumber(getIntValue(hmRow.get("ARRA_REPORT_NUMBER")));
                detailBean.setVersionNumber(getIntValue(hmRow.get("VERSION_NUMBER")));
                detailBean.setMitAwardNumber((String)hmRow.get("MIT_AWARD_NUMBER"));
                detailBean.setSequenceNumber(getIntValue(hmRow.get("SEQUENCE_NUMBER")));
                detailBean.setProjectStatus((String)hmRow.get("PROJECT_STATUS"));
                detailBean.setProjectTitle((String)hmRow.get("PROJECT_TITLE"));
                detailBean.setActivityCode((String)hmRow.get("ACTIVITY_CODE"));
                detailBean.setAwardingAgencyCode((String)hmRow.get("AWARDING_AGENCY_CODE"));
                detailBean.setFundingAgencyCode((String)hmRow.get("FUNDING_AGENCY_CODE"));
                detailBean.setAwardDescription((String)hmRow.get("AWARD_DESCRIPTION"));
                detailBean.setActivityDescription((String)hmRow.get("ACTIVITY_DESCRIPTION"));
                detailBean.setNoOfJobs(getDoubleValue(hmRow.get("NUMBER_OF_JOBS")));
                detailBean.setJobsAtSubs(getDoubleValue(hmRow.get("JOBS_AT_SUBS")));
                detailBean.setEmploymentImpact((String)hmRow.get("JOBS_CREATED_DESCRIPTION"));
                if(hmRow.get("INFRASTRUCTURE_CONTACT_ID")!=null){
                    detailBean.setInfraContactId(hmRow.get("INFRASTRUCTURE_CONTACT_ID").toString());
                }
                detailBean.setInfrastructureRationale((String)hmRow.get("INFRASTRUCTURE_RATIONALE"));
                if(hmRow.get("PRIM_PLACE_OF_PERF")!=null){
                    detailBean.setPrimPlaceOfPerfId(hmRow.get("PRIM_PLACE_OF_PERF").toString());
                }
                detailBean.setPrimPlaceCongDistrict((String)hmRow.get("PRIM_PLACE_OF_PERF_CONG_DIST"));
                detailBean.setAwardNumber((String)hmRow.get("AWARD_NUMBER"));
                detailBean.setAccountNumber((String)hmRow.get("ACCOUNT_NUMBER"));
                detailBean.setAwardType((String)hmRow.get("AWARD_TYPE"));
                detailBean.setAwardDate(getDateValue(hmRow.get("AWARD_DATE")));
                detailBean.setAwardAmount(getDoubleValue(hmRow.get("AWARD_AMOUNT")));
                detailBean.setTotalFederalInvoiced(getDoubleValue(hmRow.get("TOTAL_FED_ARRA_FUNDS_INVOICED")));
                detailBean.setTotalExpenditure(getDoubleValue(hmRow.get("TOTAL_FED_ARRA_EXPENDITURE")));
                detailBean.setTotalInfraExpenditure(getDoubleValue(hmRow.get("TOTAL_FED_ARRA_INFRA_EXPEND")));
                detailBean.setRecipientDUNSNumber((String)hmRow.get("RECIPIENT_DUNS_NUMBER"));
                detailBean.setCfdaNumber((String)hmRow.get("CFDA_NUMBER"));
                detailBean.setGovContractingOfficeCode((String)hmRow.get("GOV_CONTRACTING_OFFICE_CODE"));
                detailBean.setIndSubAwards(getIntValue(hmRow.get("NUM_OF_SUBS_TO_INDIVIDUALS")));
                detailBean.setIndSubAwardAmount(getDoubleValue(hmRow.get("AMOUNT_OF_SUBS_TO_INDIVIDUALS")));
                detailBean.setVendorLess25K(getIntValue(hmRow.get("NUM_OF_PAY_VENDOR_LESS_25K")));
                detailBean.setVendorLess25KAmount(getDoubleValue(hmRow.get("AMOUNT_OF_PAY_VENDOR_LESS_25K")));
                detailBean.setSubAwdLess25K(getIntValue(hmRow.get("NUM_OF_SUBS_LESS_25K")));
                detailBean.setSubAwdLess25KAmount(getDoubleValue(hmRow.get("AMOUNT_OF_SUBS_LESS_25K")));
                detailBean.setOrderNumber((String)hmRow.get("ORDER_NUMBER"));
                detailBean.setRecipientCongDistrict((String)hmRow.get("RECIPIENT_CONG_DISTRICT"));
                detailBean.setIndicationOfReporting((String)hmRow.get("INDICATION_OF_REPORTING"));       
                detailBean.setFinalReportIndicator((String)hmRow.get("FINAL_REPORT_FLAG"));
                detailBean.setAgencyTAS((String)hmRow.get("AGENCY_TAS"));
                detailBean.setUpdateTimestamp((Timestamp)hmRow.get("UPDATE_TIMESTAMP"));
                detailBean.setUpdateUser((String)hmRow.get("UPDATE_USER"));
                detailBean.setTasSubCode((String)hmRow.get("TAS_SUB_CODE"));
                detailBean.setReportPeriodStart(getDateValue(hmRow.get("REPORT_PERIOD_START")));
                detailBean.setReportPeriodEnd(getDateValue(hmRow.get("REPORT_PERIOD_END")));
                detailBean.setSubmissionDate(getDateValue(hmRow.get("SUBMISSION_DATE")));
            }
        }
        return detailBean;
    }
    
    /**
     * This method checks if the person is an Arra Admin Assistant for the passed mitAwardNumber 
     */
    public boolean isPersonArraAdminAssistantForAward(String personId, String mitAwardNumber) throws Exception{
        boolean isArraAdminAssistant = false;
        AwardTxnBean awardTxnBean = new AwardTxnBean();
        CoeusVector cvAdmins = awardTxnBean.getAwardUnitAdmin(mitAwardNumber);
        if(cvAdmins != null && !cvAdmins.isEmpty() && personId != null && !"".equals(personId)){
            
            String unitAdminTypeCode = null;
            Vector param= new Vector();
            Vector vecResult = new Vector();
            HashMap hmResult = new HashMap();
            
            param.add(new Parameter("PARAM_NAME",
                    DBEngineConstants.TYPE_STRING,"ARRA_UNIT_ADMIN_TYPE_CODE"));
            vecResult = dbEngine.executeFunctions("Coeus",
                    "{<<OUT STRING PARAM_VAL>>=call get_parameter_value ( "
                    + " << PARAM_NAME >>)}", param);
            if(!vecResult.isEmpty()){
                hmResult = (HashMap)vecResult.elementAt(0);
                unitAdminTypeCode = hmResult.get("PARAM_VAL").toString();
            }
            
            if(unitAdminTypeCode != null && !"".equals(unitAdminTypeCode)){
                InvestigatorUnitAdminTypeBean adminTypeBean = null;
                int totalAdmins = cvAdmins.size();
                for(int index = 0; index < totalAdmins; index++){
                    adminTypeBean = (InvestigatorUnitAdminTypeBean) cvAdmins.get(index);
                    if(unitAdminTypeCode.equalsIgnoreCase(String.valueOf(adminTypeBean.getUnitAdminType()))
                    && personId.equals(adminTypeBean.getAdministrator())){
                        isArraAdminAssistant = true;
                        break;
                    }
                }
            }
        }
        return isArraAdminAssistant;
    }
/**
     * This method is used to fetch all the award details of arra reporting.
     * <li>To fetch the data, it uses the procedure GET_ARRA_AWARD_DETAILS.
     * @param reportNumber - The Arra report Number.
     * @param mitAwardNumber - The mit award number.
     * @return detailBean - The ArraAwardDetailsBean
     * @exception DBException if the instance of a dbEngine is null.
     */
    public ArraAwardDetailsBean getArraAwardDetailsForVersion(int reportNumber,String mitAwardNumber,int versionNumber) throws DBException{
        
        Vector result = new Vector(3,2);
        ArraAwardDetailsBean detailBean = null;
        
        Vector param= new Vector();
        param.addElement(new Parameter("AV_ARRA_REPORT_NUMBER", DBEngineConstants.TYPE_INT, Integer.toString(reportNumber)));
        param.addElement(new Parameter("AV_MIT_AWARD_NUMBER", DBEngineConstants.TYPE_STRING, mitAwardNumber));
         param.addElement(new Parameter("AV_VERSION_NUMBER", DBEngineConstants.TYPE_INT, new Integer(versionNumber)));
        
        if(dbEngine != null){
            result = dbEngine.executeRequest("Coeus",
                    "call GET_ARRA_AWARD_DETAILS_VERSION(<<AV_ARRA_REPORT_NUMBER>>,<<AV_MIT_AWARD_NUMBER>>,<<AV_VERSION_NUMBER>>, <<OUT RESULTSET rset>> ) ",
                    "Coeus", param);
        }
        if(result != null && result.size() > 0){
            HashMap hmRow = null;
            for(int i = 0; i<result.size(); i++){
                hmRow = (HashMap)result.get(i);
                detailBean = new ArraAwardDetailsBean();
                detailBean.setArraReportNumber(getIntValue(hmRow.get("ARRA_REPORT_NUMBER")));
                detailBean.setVersionNumber(getIntValue(hmRow.get("VERSION_NUMBER")));
                detailBean.setMitAwardNumber((String)hmRow.get("MIT_AWARD_NUMBER"));
                detailBean.setSequenceNumber(getIntValue(hmRow.get("SEQUENCE_NUMBER")));
                detailBean.setProjectStatus((String)hmRow.get("PROJECT_STATUS"));
                detailBean.setProjectTitle((String)hmRow.get("PROJECT_TITLE"));
                detailBean.setActivityCode((String)hmRow.get("ACTIVITY_CODE"));
                detailBean.setAwardingAgencyCode((String)hmRow.get("AWARDING_AGENCY_CODE"));
                detailBean.setFundingAgencyCode((String)hmRow.get("FUNDING_AGENCY_CODE"));
                detailBean.setAwardDescription((String)hmRow.get("AWARD_DESCRIPTION"));
                detailBean.setActivityDescription((String)hmRow.get("ACTIVITY_DESCRIPTION"));
                detailBean.setNoOfJobs(getDoubleValue(hmRow.get("NUMBER_OF_JOBS")));
                detailBean.setJobsAtSubs(getDoubleValue(hmRow.get("JOBS_AT_SUBS")));
                detailBean.setEmploymentImpact((String)hmRow.get("JOBS_CREATED_DESCRIPTION"));
                if(hmRow.get("INFRASTRUCTURE_CONTACT_ID")!=null){
                    detailBean.setInfraContactId(hmRow.get("INFRASTRUCTURE_CONTACT_ID").toString());
                }
                detailBean.setInfrastructureRationale((String)hmRow.get("INFRASTRUCTURE_RATIONALE"));
                if(hmRow.get("PRIM_PLACE_OF_PERF")!=null){
                    detailBean.setPrimPlaceOfPerfId(hmRow.get("PRIM_PLACE_OF_PERF").toString());
                }
                detailBean.setPrimPlaceCongDistrict((String)hmRow.get("PRIM_PLACE_OF_PERF_CONG_DIST"));
                detailBean.setAwardNumber((String)hmRow.get("AWARD_NUMBER"));
                detailBean.setAccountNumber((String)hmRow.get("ACCOUNT_NUMBER"));
                detailBean.setAwardType((String)hmRow.get("AWARD_TYPE"));
                detailBean.setAwardDate(getDateValue(hmRow.get("AWARD_DATE")));
                detailBean.setAwardAmount(getDoubleValue(hmRow.get("AWARD_AMOUNT")));
                detailBean.setTotalFederalInvoiced(getDoubleValue(hmRow.get("TOTAL_FED_ARRA_FUNDS_INVOICED")));
                detailBean.setTotalExpenditure(getDoubleValue(hmRow.get("TOTAL_FED_ARRA_EXPENDITURE")));
                detailBean.setTotalInfraExpenditure(getDoubleValue(hmRow.get("TOTAL_FED_ARRA_INFRA_EXPEND")));
                detailBean.setRecipientDUNSNumber((String)hmRow.get("RECIPIENT_DUNS_NUMBER"));
                detailBean.setCfdaNumber((String)hmRow.get("CFDA_NUMBER"));
                detailBean.setGovContractingOfficeCode((String)hmRow.get("GOV_CONTRACTING_OFFICE_CODE"));
                detailBean.setIndSubAwards(getIntValue(hmRow.get("NUM_OF_SUBS_TO_INDIVIDUALS")));
                detailBean.setIndSubAwardAmount(getDoubleValue(hmRow.get("AMOUNT_OF_SUBS_TO_INDIVIDUALS")));
                detailBean.setVendorLess25K(getIntValue(hmRow.get("NUM_OF_PAY_VENDOR_LESS_25K")));
                detailBean.setVendorLess25KAmount(getDoubleValue(hmRow.get("AMOUNT_OF_PAY_VENDOR_LESS_25K")));
                detailBean.setSubAwdLess25K(getIntValue(hmRow.get("NUM_OF_SUBS_LESS_25K")));
                detailBean.setSubAwdLess25KAmount(getDoubleValue(hmRow.get("AMOUNT_OF_SUBS_LESS_25K")));
                detailBean.setOrderNumber((String)hmRow.get("ORDER_NUMBER"));
                detailBean.setRecipientCongDistrict((String)hmRow.get("RECIPIENT_CONG_DISTRICT"));
                detailBean.setIndicationOfReporting((String)hmRow.get("INDICATION_OF_REPORTING"));       
                detailBean.setFinalReportIndicator((String)hmRow.get("FINAL_REPORT_FLAG"));
                detailBean.setAgencyTAS((String)hmRow.get("AGENCY_TAS"));
                detailBean.setUpdateTimestamp((Timestamp)hmRow.get("UPDATE_TIMESTAMP"));
                detailBean.setUpdateUser((String)hmRow.get("UPDATE_USER"));
                detailBean.setTasSubCode((String)hmRow.get("TAS_SUB_CODE"));
            }
        }
        return detailBean;
    }
    /**
     * This method is used to fetch the list of subcontracts.
     * <li>To fetch the data, it uses the procedure GET_ARRA_AWARD_SUBCONTRACTS.
     * @param reportNumber - The Arra report Number.
     * @param mitAwardNumber - The mit award number.
     * @return vctSubcontracts - The Vector of  ArraAwardSubcontractBean
     * @exception DBException if the instance of a dbEngine is null.
     */
    public Vector getArraAwardSubcontractsForVersion(int reportNumber,String mitAwardNumber,int versionNumber) throws DBException{
        
        Vector result = new Vector(3,2);
        ArraAwardSubcontractBean subcontract = null;
        Vector vctSubcontracts = null;
        Vector param= new Vector();
        param.addElement(new Parameter("AV_ARRA_REPORT_NUMBER", DBEngineConstants.TYPE_INT, Integer.toString(reportNumber)));
        param.addElement(new Parameter("AV_MIT_AWARD_NUMBER", DBEngineConstants.TYPE_STRING, mitAwardNumber));
        param.addElement(new Parameter("AV_VERSION_NUMBER", DBEngineConstants.TYPE_INT, new Integer(versionNumber)));
        
        if(dbEngine != null){
            result = dbEngine.executeRequest("Coeus",
                    "call GET_ARRA_SUBCONTRACTS_VERSION(<<AV_ARRA_REPORT_NUMBER>>,<<AV_MIT_AWARD_NUMBER>>,<<AV_VERSION_NUMBER>>, <<OUT RESULTSET rset>> ) ",
                    "Coeus", param);
        }
        if(result != null && result.size() > 0){
            HashMap hmRow = null;
            vctSubcontracts = new Vector();
            for(int i = 0; i<result.size(); i++){
                hmRow = (HashMap)result.get(i);
                subcontract = new ArraAwardSubcontractBean();
                subcontract.setArraReportNumber(getIntValue(hmRow.get("ARRA_REPORT_NUMBER")));
                subcontract.setVersionNumber(getIntValue(hmRow.get("VERSION_NUMBER")));
                subcontract.setMitAwardNumber((String)hmRow.get("MIT_AWARD_NUMBER"));
                subcontract.setSubcontractCode((String)hmRow.get("SUBCONTRACT_CODE"));
                subcontract.setSubcontractNo((String)hmRow.get("SUBCONTRACT_NUMBER"));
                subcontract.setSubcontractorID((String)hmRow.get("SUBCONTRACTOR_ID"));
                subcontract.setSubcontractorName((String)hmRow.get("SUBCONTRACTOR_NAME"));
                subcontract.setSubAwardAmount(getDoubleValue(hmRow.get("SUBAWARD_AMOUNT")));
                subcontract.setSubAwardAmtDispursed(getDoubleValue(hmRow.get("SUBAWARD_AMOUNT_DISPURSED")));
                subcontract.setNoOfJobs(getDoubleValue(hmRow.get("NUMBER_OF_JOBS")));
                subcontract.setSubAwardDate(getDateValue(hmRow.get("SUBAWARD_DATE")));
                if(hmRow.get("PRIM_PLACE_OF_PERF")!=null){
                    subcontract.setPrimPlaceOfPerfId(hmRow.get("PRIM_PLACE_OF_PERF").toString());
                }
                subcontract.setPrimPlaceCongDist((String)hmRow.get("PRIM_PLACE_OF_PERF_CONG_DIST"));
                subcontract.setSubRecipientDUNS((String)hmRow.get("SUB_RECIPIENT_DUNS"));
                subcontract.setSubRecipientCongDist((String)hmRow.get("SUB_RECIPIENT_CONG_DIST"));
                subcontract.setIndicationOfReporting((String)hmRow.get("INDICATION_OF_REPORTING"));
                vctSubcontracts.add(subcontract);
            }
        }
        return vctSubcontracts;
    }
    /**
     * This method is used to fetch the list of vendors.
     * Vendors can be applicable at award level or subcontract level.
     * If the award level vendors are required, pass subContractCode as null.
     * <li>To fetch the data, it uses the procedure GET_ARRA_AWARD_VENDORS.
     * @param reportNumber - The Arra report Number.
     * @param mitAwardNumber - The mit award number.
     * @param subContractCode - The subcontract code.
     * @return vctVendors - The Vector of  ArraVendorBean
     * @exception DBException if the instance of a dbEngine is null.
     */
    public Vector getArraVendorsForVersion(int reportNumber,String mitAwardNumber,int versionNumber,String subContractCode) throws DBException{
        
        Vector result = new Vector(3,2);
        ArraVendorBean vendor = null;
        Vector vctVendors = null;
        Vector param= new Vector();
        param.addElement(new Parameter("AV_ARRA_REPORT_NUMBER", DBEngineConstants.TYPE_INT, Integer.toString(reportNumber)));
        param.addElement(new Parameter("AV_MIT_AWARD_NUMBER", DBEngineConstants.TYPE_STRING, mitAwardNumber));
        param.addElement(new Parameter("AV_VERSION_NUMBER", DBEngineConstants.TYPE_INT, new Integer(versionNumber)));
        param.addElement(new Parameter("AV_SUBCONTRACT_CODE", DBEngineConstants.TYPE_STRING, subContractCode));
        
        if(dbEngine != null){
            result = dbEngine.executeRequest("Coeus",
                    "call GET_ARRA_AWARD_VENDORS_VERSION(<<AV_ARRA_REPORT_NUMBER>>,<<AV_MIT_AWARD_NUMBER>>,<<AV_VERSION_NUMBER>>,<<AV_SUBCONTRACT_CODE>>, <<OUT RESULTSET rset>> ) ",
                    "Coeus", param);
        }
        if(result != null && result.size() > 0){
            HashMap hmRow = null;
            vctVendors = new Vector();
            for(int i = 0; i<result.size(); i++){
                hmRow = (HashMap)result.get(i);
                vendor = new ArraVendorBean();
                vendor.setArraReportNumber(getIntValue(hmRow.get("ARRA_REPORT_NUMBER")));
                vendor.setVersionNumber(getIntValue(hmRow.get("VERSION_NUMBER")));
                vendor.setMitAwardNumber((String)hmRow.get("MIT_AWARD_NUMBER"));
                vendor.setSubContractCode((String)hmRow.get("SUBCONTRACT_CODE"));
                vendor.setVendorNumber((String)hmRow.get("VENDOR_NUMBER"));
                vendor.setVendorDUNS((String)hmRow.get("VENDOR_DUNS"));
                vendor.setVendorHQZipCode((String)hmRow.get("VENDOR_HQ_ZIP_CODE"));
                vendor.setVendorName((String)hmRow.get("VENDOR_NAME"));
                vendor.setServiceDescription((String)hmRow.get("SERVICE_DESCRIPTION"));
                vendor.setPaymentAmount(getDoubleValue(hmRow.get("PAYMENT_AMOUNT")));
                vendor.setUpdateTimestamp((Timestamp)hmRow.get("UPDATE_TIMESTAMP"));
                vendor.setUpdateUser((String)hmRow.get("UPDATE_USER"));
                vctVendors.add(vendor);
            }
        }
        return vctVendors;
    }
    // Arra Phase 2 Changes - End
}
