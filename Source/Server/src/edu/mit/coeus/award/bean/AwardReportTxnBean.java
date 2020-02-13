/*
 * AwardReportTxnBean.java
 *
 * Created on July 2, 2004, 11:31 AM
 */
package edu.mit.coeus.award.bean;

import edu.mit.coeus.utils.dbengine.DBEngineImpl;
import edu.mit.coeus.utils.dbengine.DBException;
import edu.mit.coeus.utils.dbengine.Parameter;
import edu.mit.coeus.utils.dbengine.DBEngineConstants;
import edu.mit.coeus.utils.dbengine.TransactionMonitor;
import edu.mit.coeus.utils.UtilFactory;
import edu.mit.coeus.exception.CoeusException;
import edu.mit.coeus.utils.CoeusVector;
import edu.mit.coeus.utils.ComboBoxBean;
import edu.mit.coeus.bean.FrequencyBean;
import edu.mit.coeus.bean.FrequencyBaseBean;
import edu.mit.coeus.bean.UserInfoBean;
import edu.mit.coeus.bean.PersonInfoBean;
import edu.mit.coeus.bean.ReportBean;

import edu.mit.coeus.bean.CoiDisclForItemBean;

import edu.mit.coeus.subcontract.bean.SubContractTxnBean;
import edu.mit.coeus.bean.CommentTypeBean;
import edu.mit.coeus.utils.query.Equals;
import edu.mit.coeus.utils.query.And;
import edu.mit.coeus.departmental.bean.DepartmentPersonTxnBean;
import edu.mit.coeus.utils.dbengine.ProcReqParameter;
import java.util.Vector;
import java.util.HashMap;
import java.util.Hashtable;
import java.sql.Date;
import java.sql.Timestamp;
/**
 * This class contains implementation of get procedures 
 * used in Award Module.
 * @author  Shivakumar M J
 */


public class AwardReportTxnBean {
    
    // instance of dbEngine
    
    private DBEngineImpl dbEngine;
    
    private static final String rowLockStr = "osp$award";
    private static final String DSN = "Coeus";
    //Report Class Names
    private static final String FISCAL_CLASS_CODE = "FISCAL_CLASS_CODE";
    private static final String TECHNICAL_MANAGEMENT_CLASS_CODE = "TECHNICAL_MANAGEMENT_CLASS_CODE";
    private static final String INTELLECTUAL_PROPERTY_CLASS_CODE = "INTELLECTUAL_PROPERTY_CLASS_CODE";
    private static final String PROPERTY_CLASS_CODE = "PROPERTY_CLASS_CODE";
    
    private TransactionMonitor transactionMonitor;
    
    /** Creates a new instance of AwardTxnBean */
    public AwardReportTxnBean() {
        dbEngine = new DBEngineImpl();
        transactionMonitor = TransactionMonitor.getInstance();
    }
    
    /**Creates new AwardTermsTxnBean and initializes userId.
     * @param userId String which the Loggedin userid
     */    
    public AwardReportTxnBean(String userId) {
     //   this.userId = userId;
        dbEngine = new DBEngineImpl();        
    }
    
    /** The following method is used get Account for forward
     * @param mitAwardNumber is the input
     * @throws CoeusException is an Exception class, which is used to represnt any exception comes
     * in COEUS web module
     * @throws DBException is an Exception class, which is used to handle exceptions in dbEngine package 
     * during SQL Command execution
     * @return type is integer
     */    
    // case 1438 : change return type from integer to string. jenlu 2/28/05
    
    //case 1438 begin
//    public int getAccountForAward(String mitAwardNumber)
    public String getAccountForAward(String mitAwardNumber)
    //case 1438 end
    throws CoeusException, DBException {
        
        dbEngine=new DBEngineImpl();
        Vector result=null;
        Vector param=new Vector();
        HashMap invRow=null;
        //case 1438 begin
        // int account_number=0;
        String account_number ="";
        //case 1438 end
        
        param.addElement(new Parameter("MIT_AWARD_NUMBER",
        DBEngineConstants.TYPE_STRING, mitAwardNumber));
        
        if(dbEngine!=null){
            result = dbEngine.executeFunctions("Coeus",
            //case 1438 begin
//            "{ <<OUT INTEGER ACCOUNT_NUMBER>> = "
            "{ <<OUT STRING ACCOUNT_NUMBER>> = "
            //case 1438 end
            +" call FN_GET_ACCOUNT_FOR_AWARD(<< MIT_AWARD_NUMBER >> ) }", param);
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }
        
        if(!result.isEmpty()){
            HashMap rowParameter = (HashMap)result.elementAt(0);
            //case 1438 begin
//            account_number = Integer.parseInt(rowParameter.get("ACCOUNT_NUMBER").toString());
            account_number = rowParameter.get("ACCOUNT_NUMBER")== null ? "" : rowParameter.get("ACCOUNT_NUMBER").toString().trim();
           //case 1438 end
        }
        return account_number;        
    }    
    
    /**
	 * This method is used to get the parameter value for the given module class code
	 * @param String parameterName
	 * @return String moduleId
	 * @exception DBException if any error during database transaction.
     * @exception CoeusException if the instance of dbEngine is not available.
	 */
	public String getParameterValue(String parameterName) throws DBException, CoeusException {
	
		Vector result = new Vector(3,2);                
        HashMap modId = null;
		String moduleId = null;
		Vector param= new Vector();
		param.add(new Parameter("PARAM_NAME",
                        DBEngineConstants.TYPE_STRING, parameterName));
        result = dbEngine.executeFunctions("Coeus",
        "{<<OUT STRING MOD_NAME>> = call get_parameter_value (<<PARAM_NAME>>)}",param);
        if(!result.isEmpty()) {
            modId = (HashMap)result.elementAt(0);
            moduleId = modId.get("MOD_NAME").toString();
        }
		return moduleId;
	}
    
           /** The following method is used get Root Account for award
     * @param mitAwardNumber is the input
     * @throws CoeusException is an Exception class, which is used to represnt any exception comes
     * in COEUS web module
     * @throws DBException is an Exception class, which is used to handle exceptions in dbEngine package 
     * during SQL Command execution
     * @return type is integer
     */ 
        //case 1438 : change return type from int to string.  jenlu 2/28/05
        
    //case 1438 begin    
//    public int getRootAccountForAward(String mitAwardNumber)
        public String getRootAccountForAward(String mitAwardNumber)
    //case 1438 end
    throws CoeusException, DBException {
        
        dbEngine=new DBEngineImpl();
        Vector result=null;
        Vector param=new Vector();
        HashMap invRow=null;
        //case 1438 begin
//        int rootAccount_number=0;
        String rootAccount_number = "";
        //case 1438 end
        
        param.addElement(new Parameter("MIT_AWARD_NUMBER",
        DBEngineConstants.TYPE_STRING, mitAwardNumber));
        
        if(dbEngine!=null){
            result = dbEngine.executeFunctions("Coeus",
            "{ <<OUT STRING ROOT_MIT_AWARD_NUMBER>> = "
            +" call FN_GET_ROOT_AWARD(<< MIT_AWARD_NUMBER >> ) }", param);
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }
        
        if(!result.isEmpty()){
            HashMap rowParameter = (HashMap)result.elementAt(0);
            rootAccount_number = getAccountForAward(rowParameter.get("ROOT_MIT_AWARD_NUMBER").toString());
            
        }
        return rootAccount_number;        
    }    
    /** The following method has been written to execute the procedure GET_AO_INFO which 
     *returns full_name and office location
     * @param userInfoBean is the input
     * @throws CoeusException is an Exception class, which is used to represnt any exception comes
     * in COEUS web module
     * @throws DBException is an Exception class, which is used to handle exceptions in dbEngine package 
     * during SQL Command execution
     * @return type is PersonInfoBean
     */    
    public PersonInfoBean getAoInfo(String unitNumber)
       throws CoeusException,DBException {
        dbEngine=new DBEngineImpl();
        Vector result=null;
        Vector param=new Vector();
        HashMap invRow=null;
        
        param.addElement(new Parameter("UNIT_NUMBER",
        DBEngineConstants.TYPE_STRING, unitNumber)); 
        
        if(dbEngine!=null){
            result = dbEngine.executeRequest("Coeus",
            "call GET_AO_INFO( << UNIT_NUMBER >>,<<OUT STRING FULL_NAME>>, <<OUT STRING OFFICE_LOCATION>> )",
            "Coeus", param);
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }
        int listSize=result.size();
        CoeusVector invList=null;
        PersonInfoBean personInfoBean = null;
        if(listSize>0){
            invList=new CoeusVector();
            invRow=(HashMap)result.elementAt(0); 
            personInfoBean = new PersonInfoBean();
            personInfoBean.setFullName((String)
               invRow.get("FULL_NAME"));
            personInfoBean.setOffLocation((String)
               invRow.get("OFFICE_LOCATION"));                      
        }    
        return personInfoBean;
    }       
    
    /**The following method has been written to execute the procedure 
     * @param moduleItemKey is the input data FN_IS_ALL_DISC_STATUS_COMPLETE which returns the 
     * account number
     * @param moduleCode is the input data
     * @throws CoeusException is an Exception class, which is used to represnt any exception comes
     * in COEUS web module
     * @throws DBException is an Exception class, which is used to handle exceptions in dbEngine package 
     * during SQL Command execution
     * @return type is integer
     */    
    public int getIsAllDiscStatusComplete(String moduleItemKey, int moduleCode)
    throws CoeusException, DBException {
        
        dbEngine=new DBEngineImpl();
        Vector result=null;
        Vector param=new Vector();
        HashMap invRow=null;
        int retNumber = 0;
        
        param.addElement(new Parameter("MODULE_ITEM_KEY",
        DBEngineConstants.TYPE_STRING, moduleItemKey));
        param.addElement(new Parameter("MODULE_CODE",
        DBEngineConstants.TYPE_INT, ""+moduleCode));
       
        if(dbEngine!=null){
            result = dbEngine.executeFunctions("Coeus",
            "{ <<OUT INTEGER ACCOUNT_NUMBER>> = "
            +" call FN_IS_ALL_DISC_STATUS_COMPLETE(<< MODULE_ITEM_KEY >>, << MODULE_CODE >> ) }", param);
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }
        
        if(!result.isEmpty()){
            HashMap rowParameter = (HashMap)result.elementAt(0);
            retNumber = Integer.parseInt(rowParameter.get("ACCOUNT_NUMBER").toString());
        }
        return retNumber; 
        
    }    
    
    /** The following method has been written to retrieve the data related to 
     *  Disclosure number
     * @param coiDisclForItemBean is the input
     * @throws CoeusException is an Exception class, which is used to represnt any exception comes
     * in COEUS web module
     * @throws DBException is an Exception class, which is used to handle exceptions in dbEngine package 
     * during SQL Command execution
     * @return type is CoiDisclForItemBean.
     */    
    public CoeusVector getCoiDisclForItem(String moduleItemKey, int moduleCode)
    throws CoeusException, DBException {
        dbEngine=new DBEngineImpl();
        Vector result=null;
        Vector param=new Vector();
        HashMap row=null;
        CoiDisclForItemBean coiDisclForItemBean;    
        CoeusVector cvCoiDiscl;
        param.addElement(new Parameter("MODULE_CODE",
        DBEngineConstants.TYPE_INT, ""+moduleCode));
        
        param.addElement(new Parameter("MODULE_ITEM_KEY",
        DBEngineConstants.TYPE_STRING, moduleItemKey));        
        
        if(dbEngine!=null){
            result = dbEngine.executeRequest("Coeus",
            "call GET_COI_DISCL_FOR_ITEM( <<MODULE_CODE >>, << MODULE_ITEM_KEY >>,<<OUT RESULTSET rset>> )",
            "Coeus", param);            
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }  
        //
        int listSize = result.size();
        cvCoiDiscl = new CoeusVector();
        if(listSize > 0) {            
            
            for(int rowNum = 0; rowNum < listSize; rowNum++) {
                row = (HashMap)result.elementAt(rowNum);
                coiDisclForItemBean = new CoiDisclForItemBean();
                coiDisclForItemBean.setCoiDisclosureNumber((String)
                    row.get("COI_DISCLOSURE_NUMBER"));
                coiDisclForItemBean.setPersonId((String)
                    row.get("FULL_NAME"));
                coiDisclForItemBean.setDisclosureType((String)
                    row.get("DISCLOSURE_TYPE"));
                coiDisclForItemBean.setCoiDisclosureStatusCode((String)
                    row.get("COI_DISCLOSURE_STATUS_CODE"));
                
		cvCoiDiscl.addElement(coiDisclForItemBean);
            }
        }    
        return cvCoiDiscl;
       
    }   
    
   //case 2415 start
    public CoeusVector getCoiDisclForAward(String moduleItemKey, int moduleCode)
    throws CoeusException, DBException {
        dbEngine=new DBEngineImpl();
        Vector result=null;
        Vector param=new Vector();
        HashMap row=null;
        CoiDisclForItemBean coiDisclForItemBean;    
        CoeusVector cvCoiDiscl;
        param.addElement(new Parameter("MODULE_CODE",
        DBEngineConstants.TYPE_INT, ""+moduleCode));
        
        param.addElement(new Parameter("MODULE_ITEM_KEY",
        DBEngineConstants.TYPE_STRING, moduleItemKey));        
        
        if(dbEngine!=null){
            result = dbEngine.executeRequest("Coeus",
            "call GET_COI_DISCL_FOR_AWARD( <<MODULE_CODE >>, << MODULE_ITEM_KEY >>,<<OUT RESULTSET rset>> )",
            "Coeus", param);            
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }  
        //
        int listSize = result.size();
        cvCoiDiscl = new CoeusVector();
        if(listSize > 0) {            
            
            for(int rowNum = 0; rowNum < listSize; rowNum++) {
                row = (HashMap)result.elementAt(rowNum);
                coiDisclForItemBean = new CoiDisclForItemBean();
                coiDisclForItemBean.setCoiDisclosureNumber((String)
                    row.get("COI_DISCLOSURE_NUMBER"));
                coiDisclForItemBean.setPersonId((String)
                    row.get("FULL_NAME"));
                coiDisclForItemBean.setDisclosureType((String)
                    row.get("DISCLOSURE_TYPE"));
                coiDisclForItemBean.setCoiDisclosureStatusCode((String)
                    row.get("COI_DISCLOSURE_STATUS_CODE"));
                
		cvCoiDiscl.addElement(coiDisclForItemBean);
            }
        }    
        return cvCoiDiscl;       
    }   
   //case 2415 end
    
    /** The following method has been written to get data related to Child Award
     * @param awardHierarchyBean is the input 
     * @throws CoeusException is an Exception class, which is used to represnt any exception comes
     * in COEUS web module
     * @throws DBException is an Exception class, which is used to handle exceptions in dbEngine package 
     * during SQL Command execution
     * @return type is AwardAmountInfoBean
     */    
    public AwardAmountInfoBean getChildAward(String parentMitAwardNumber)
    throws CoeusException, DBException {
        dbEngine=new DBEngineImpl();
        Vector result=null;
        Vector param=new Vector();
        CoeusVector vctAwardAmountInfo = null;
        HashMap row=null;
        
        param.addElement(new Parameter("PARENT_MIT_AWARD_NUMBER",
        DBEngineConstants.TYPE_STRING, parentMitAwardNumber));
        
        if(dbEngine!=null){
            result = dbEngine.executeRequest("Coeus",
            "call DW_GET_CHILD_AWARDS( << PARENT_MIT_AWARD_NUMBER >>, <<OUT RESULTSET rset>> )",
            "Coeus", param);            
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }  
        
         int listSize = result.size();
        AwardAmountInfoBean awardAmountInfoBean = null;
        awardAmountInfoBean = new AwardAmountInfoBean();
        if(listSize>0){            
            vctAwardAmountInfo = new CoeusVector();
            for(int rowNum = 0; rowNum < listSize; rowNum++){
                row = (HashMap)result.elementAt(rowNum);
                //awardAmountInfoBean = new AwardAmountInfoBean();
                awardAmountInfoBean.setRootMitAwardNumber((String)
                    row.get("ROOT_MIT_AWARD_NUMBER"));
                awardAmountInfoBean.setRootMitAwardNumber((String)
                    row.get("MIT_AWARD_NUMBER"));
                awardAmountInfoBean.setRootMitAwardNumber((String)
                    row.get("PARENT_ROOT_MIT_AWARD_NUMBER"));
                awardAmountInfoBean.setAnticipatedTotalAmount(
                row.get("ANTICIPATED_TOTAL_AMOUNT") == null ? 0 : Double.parseDouble(row.get("ANTICIPATED_TOTAL_AMOUNT").toString()));
                awardAmountInfoBean.setAmountObligatedToDate(
                row.get("AMOUNT_OBLIGATED_TO_DATE") == null ? 0 : Double.parseDouble(row.get("AMOUNT_OBLIGATED_TO_DATE").toString()));
                awardAmountInfoBean.setFinalExpirationDate(
                row.get("FINAL_EXPIRATION_DATE") == null ?
                null : new Date(((Timestamp) row.get("FINAL_EXPIRATION_DATE")).getTime()));                
                awardAmountInfoBean.setCurrentFundEffectiveDate(
                row.get("CURRENT_FUND_EFFECTIVE_DATE") == null ?
                null : new Date(((Timestamp) row.get("CURRENT_FUND_EFFECTIVE_DATE")).getTime()));
                awardAmountInfoBean.setObligationExpirationDate(
                row.get("OBLIGATION_EXPIRATION_DATE") == null ?
                null : new Date(((Timestamp) row.get("OBLIGATION_EXPIRATION_DATE")).getTime()));
                awardAmountInfoBean.setAccountNumber((String)
                    row.get("ACCOUNT_NUMBER"));
            }
         }    
        return awardAmountInfoBean;
        
    }   
    
    /** The following method has been written to get data related to Science Code 
     * for Man
     * @param awardScienceCodeBean is the input
     * @throws CoeusException is an Exception class, which is used to represnt any exception comes
     * in COEUS web module
     * @throws DBException is an Exception class, which is used to handle exceptions in dbEngine package 
     * during SQL Command execution
     * @return type is AwardScienceCodeBean
     */    
    public CoeusVector getScienceCodeForMan(String mitAwardNumber)
    throws CoeusException, DBException {
        
        
        dbEngine=new DBEngineImpl();
        Vector result=null;
        Vector param=new Vector();
        CoeusVector cvScienceCode = null;
        HashMap row=null;
        
        param.addElement(new Parameter("MIT_AWARD_NUMBER",
        DBEngineConstants.TYPE_STRING, mitAwardNumber));
        
        if(dbEngine!=null){
            result = dbEngine.executeRequest("Coeus",
            "call DW_GET_A_SCIENCE_CODE_FOR_MAN( << MIT_AWARD_NUMBER >>, <<OUT RESULTSET rset>> )",
            "Coeus", param);            
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }        
        int listSize = result.size();
        AwardScienceCodeBean awardScienceCodeBean = null;
        if(listSize > 0) {            
            cvScienceCode = new CoeusVector();
            for(int rowNum = 0; rowNum < listSize; rowNum++) {
                row = (HashMap)result.elementAt(rowNum);
                awardScienceCodeBean = new AwardScienceCodeBean();
                awardScienceCodeBean.setMitAwardNumber((String)
                    row.get("MIT_AWARD_NUMBER"));
                awardScienceCodeBean.setSequenceNumber(
                row.get("SEQUENCE_NUMBER") == null ? 0 : Integer.parseInt(row.get("SEQUENCE_NUMBER").toString()));
                awardScienceCodeBean.setScienceCode((String)
                    row.get("SCIENCE_CODE"));
                awardScienceCodeBean.setUpdateTimestamp(
                    (Timestamp)row.get("UPDATE_TIMESTAMP"));
                awardScienceCodeBean.setUpdateUser((String)
                    row.get("UPDATE_USER"));
                awardScienceCodeBean.setDescription((String)
                    row.get("DESCRIPTION"));   
				cvScienceCode.addElement(awardScienceCodeBean);
              }
           }   
        return cvScienceCode;         
    
    }   
    
    /** The following method has been written to get data related to Report
     * @throws CoeusException is an Exception class, which is used to represnt any exception comes
     * in COEUS web module
     * @throws DBException is an Exception class, which is used to handle exceptions in dbEngine package 
     * during SQL Command execution
     * @return type is ReportBean 
     */    
    public CoeusVector getReport() throws CoeusException, DBException {
        
        dbEngine=new DBEngineImpl();
        Vector result=null;
        Vector param=new Vector();
        
        HashMap row=null;
        ReportBean reportBean=null;
        CoeusVector cvReport = null;
        if(dbEngine!=null){
            result = dbEngine.executeRequest("Coeus",
            "call DW_GET_REPORT(<<OUT RESULTSET rset>> )",
            "Coeus", param);            
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }
        int listSize = result.size();
        
        if(listSize>0) {            
            cvReport = new CoeusVector();
            for(int rowNum = 0; rowNum < listSize; rowNum++){
                row = (HashMap)result.elementAt(rowNum);
				reportBean = new ReportBean();
                reportBean.setCode((String)row.get("REPORT_CODE"));
                reportBean.setDescription((String)row.get("DESCRIPTION"));
                reportBean.setFinalReportFlag(row.get("FINAL_REPORT_FLAG") == null ? false:(row.get("FINAL_REPORT_FLAG").toString().equalsIgnoreCase("y") ? true : false));
                reportBean.setUpdateTimestamp((Timestamp)row.get("UPDATE_TIMESTAMP"));
                reportBean.setUpdateUser((String)row.get("UPDATE_USER"));
				cvReport.addElement(reportBean);
            }
        }
        return cvReport;
    }    
    
    
    /** The following method has been written to data related to Frequency
     * @throws CoeusException is an Exception class, which is used to represnt any exception comes
     * in COEUS web module
     * @throws DBException is an Exception class, which is used to handle exceptions in dbEngine package 
     * during SQL Command execution
     * @return type is FrequencyBean
     */    
    public CoeusVector getFrequency() throws CoeusException, DBException {
        
        dbEngine=new DBEngineImpl();
        Vector result=null;
        Vector param=new Vector();
        CoeusVector cvFrequencyData = null;
        HashMap row=null;
        FrequencyBean frequencyBean=null;
        
        if(dbEngine!=null){
            result = dbEngine.executeRequest("Coeus",
            "call DW_GET_FREQUENCY(<<OUT RESULTSET rset>> )",
            "Coeus", param);            
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }
        int listSize = result.size();
        
        if(listSize > 0) {            
            cvFrequencyData = new CoeusVector();
            for(int rowNum = 0; rowNum < listSize; rowNum++) {
				frequencyBean = new FrequencyBean();
                row = (HashMap)result.elementAt(rowNum);
                //frequencyBean.setCode((String)row.get("FREQUENCY_CODE"));   
                frequencyBean.setCode(row.get("FREQUENCY_CODE") == null ? "0" : 
                        row.get("FREQUENCY_CODE").toString());
                frequencyBean.setDescription((String)row.get("DESCRIPTION"));
                frequencyBean.setNumberOfDays(
                row.get("NUMBER_OF_DAYS") == null ? 0 : Integer.parseInt(row.get("NUMBER_OF_DAYS").toString()));
                frequencyBean.setNumberOfMonths(
                row.get("NUMBER_OF_MONTHS") == null ? 0 : Integer.parseInt(row.get("NUMBER_OF_MONTHS").toString()));
                frequencyBean.setRepeatFlag((String)row.get("REPEAT_FLAG"));
                frequencyBean.setProposalDueFlag((String)row.get("PROPOSAL_DUE_FLAG"));
                frequencyBean.setInvoiceFlag((String)row.get("INVOICE_FLAG"));
                frequencyBean.setUpdateTimestamp((Timestamp)row.get("UPDATE_TIMESTAMP"));
                frequencyBean.setUpdateUser((String)row.get("UPDATE_USER"));
                frequencyBean.setAdvanceNumberOfDays(
                row.get("ADVANCE_NUMBER_OF_DAYS") == null ? 0 : Integer.parseInt(row.get("ADVANCE_NUMBER_OF_DAYS").toString()));
                frequencyBean.setAdvanceNumberOfMonths(
                row.get("ADVANCE_NUMBER_OF_MONTHS") == null ? 0 : Integer.parseInt(row.get("ADVANCE_NUMBER_OF_MONTHS").toString()));            
		cvFrequencyData.addElement(frequencyBean);
            }
        }          
       return cvFrequencyData;
     }       
    
    /** The following method has been written to get data related to Approved 
     *  equipments
     * @param awardApprovedEquipmentBean is the input
     * @throws CoeusException is an Exception class, which is used to represnt any exception comes
     * in COEUS web module
     * @throws DBException is an Exception class, which is used to handle exceptions in dbEngine package 
     * during SQL Command execution
     * @return type is AwardApprovedEquipmentBean
     */    
    public AwardApprovedEquipmentBean getApprovedEquipments(String mitAwardNumber) throws 
        CoeusException, DBException {
        
        dbEngine=new DBEngineImpl();
        Vector result=null;
        Vector param=new Vector();
        
        HashMap row=null;
              
        param.addElement(new Parameter("MIT_AWARD_NUMBER",
        DBEngineConstants.TYPE_STRING, mitAwardNumber));
        
        if(dbEngine!=null){
            result = dbEngine.executeRequest("Coeus",
            "call DW_GET_A_APPRVD_EQUIPMENTS( << MIT_AWARD_NUMBER >>, <<OUT RESULTSET rset>> )",
            "Coeus", param);            
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }
        int listSize = result.size();
        System.out.println(listSize);
        AwardApprovedEquipmentBean awardApprovedEquipmentBean = new AwardApprovedEquipmentBean();
        
        if(listSize>0){            
            
            for(int rowNum = 0; rowNum < listSize; rowNum++) {
                row = (HashMap)result.elementAt(rowNum);
                awardApprovedEquipmentBean.setMitAwardNumber((String)row.get("MIT_AWARD_NUMBER"));
                awardApprovedEquipmentBean.setSequenceNumber(
                row.get("SEQUENCE_NUMBER") == null ? 0 : Integer.parseInt(row.get("SEQUENCE_NUMBER").toString()));
                awardApprovedEquipmentBean.setAw_Item((String)row.get("ITEM"));    
                awardApprovedEquipmentBean.setAw_Vendor((String)row.get("VENDOR"));    
                awardApprovedEquipmentBean.setAw_Model((String)row.get("MODEL"));    
                awardApprovedEquipmentBean.setAw_Item((String)row.get("ITEM"));    
                awardApprovedEquipmentBean.setAmount(
                row.get("AMOUNT") == null ? 0 : Double.parseDouble(row.get("AMOUNT").toString()));
                awardApprovedEquipmentBean.setUpdateTimestamp((Timestamp)row.get("UPDATE_TIMESTAMP"));
                awardApprovedEquipmentBean.setUpdateUser((String)row.get("UPDATE_USER"));
            }
        }    
       return  awardApprovedEquipmentBean;
    }    
    
    /** The following method has been written get data related to Approval of 
     * Foreign Trip for man
     * @param awardApprovedForeignTripBean is the input
     * @throws CoeusException is an Exception class, which is used to represnt any exception comes
     * in COEUS web module
     * @throws DBException is an Exception class, which is used to handle exceptions in dbEngine package 
     * during SQL Command execution
     * @return type is AwardApprovedForeignTrip 
     */    
    public AwardApprovedForeignTripBean getApprovedForeignTripForMan(String mitAwardNumber) throws 
        CoeusException, DBException {
        
        dbEngine=new DBEngineImpl();
        Vector result=null;
        Vector param=new Vector();
        HashMap row=null;
        AwardApprovedForeignTripBean awardApprovedForeignTripBean = null;
        param.addElement(new Parameter("MIT_AWARD_NUMBER",
        DBEngineConstants.TYPE_STRING, mitAwardNumber));
        
        if(dbEngine!=null){
            result = dbEngine.executeRequest("Coeus",
            "call DW_GET_A_APPRVD_FTRIP_FOR_MAN( << MIT_AWARD_NUMBER >>, <<OUT RESULTSET rset>> )",
            "Coeus", param);            
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }
        int listSize = result.size();
        if(listSize>0){            
            
            for(int rowNum = 0; rowNum < listSize; rowNum++) {
                row = (HashMap)result.elementAt(rowNum);
				awardApprovedForeignTripBean = new AwardApprovedForeignTripBean();
                awardApprovedForeignTripBean.setMitAwardNumber((String)row.get("MIT_AWARD_NUMBER"));
                awardApprovedForeignTripBean.setSequenceNumber(
                row.get("SEQUENCE_NUMBER") == null ? 0 : Integer.parseInt(row.get("SEQUENCE_NUMBER").toString()));
                awardApprovedForeignTripBean.setPersonId((String)row.get("PERSON_ID"));
                awardApprovedForeignTripBean.setPersonName((String)row.get("PERSON_NAME"));
                awardApprovedForeignTripBean.setAw_Destination(("DESTINATION"));
                awardApprovedForeignTripBean.setAw_DateFrom(
                row.get("DATE_FROM") == null ?
                null : new Date(((Timestamp) row.get("DATE_FROM")).getTime()));
                awardApprovedForeignTripBean.setDateTo(
                row.get("DATE_TO") == null ?
                null : new Date(((Timestamp) row.get("DATE_TO")).getTime()));
                awardApprovedForeignTripBean.setAmount(
                row.get("AMOUNT") == null ? 0 : Double.parseDouble(row.get("AMOUNT").toString()));
                awardApprovedForeignTripBean.setUpdateTimestamp((Timestamp)row.get("UPDATE_TIMESTAMP"));
                awardApprovedForeignTripBean.setUpdateUser((String)row.get("UPDATE_USER"));                
            }            
        }
       return awardApprovedForeignTripBean; 
    }    
    
    
    /** The following method has been written to get Approved Sub Contracts
     * @param awardApprovedSubcontractBean is the input
     * @throws CoeusException is an Exception class, which is used to represnt any exception comes
     * in COEUS web module
     * @throws DBException is an Exception class, which is used to handle exceptions in dbEngine package 
     * during SQL Command execution
     * @return type is AwardApprovedSubContractBean 
     */    
     public AwardApprovedSubcontractBean getApprovedSubContracts(String mitAwardNumber) throws 
        CoeusException, DBException {
        
        dbEngine=new DBEngineImpl();
        Vector result=null;
        Vector param=new Vector();
        HashMap row=null;
        AwardApprovedSubcontractBean awardApprovedSubcontractBean = null;
        param.addElement(new Parameter("MIT_AWARD_NUMBER",
        DBEngineConstants.TYPE_STRING, mitAwardNumber));
        
        if(dbEngine!=null){
            result = dbEngine.executeRequest("Coeus",
            "call DW_GET_A_APPRVD_SUBCONTRACTS( << MIT_AWARD_NUMBER >>, <<OUT RESULTSET rset>> )",
            "Coeus", param);            
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }
        int listSize = result.size();
        
        if(listSize>0){            
           
            for(int rowNum = 0; rowNum < listSize; rowNum++) {
				awardApprovedSubcontractBean = new AwardApprovedSubcontractBean();
                row = (HashMap)result.elementAt(rowNum);
                awardApprovedSubcontractBean.setMitAwardNumber((String)row.get("MIT_AWARD_NUMBER"));
                awardApprovedSubcontractBean.setSequenceNumber(
                row.get("SEQUENCE_NUMBER") == null ? 0 : Integer.parseInt(row.get("SEQUENCE_NUMBER").toString()));
                awardApprovedSubcontractBean.setSubcontractName((String)row.get("SUBCONTRACTOR_NAME"));
                awardApprovedSubcontractBean.setAmount(
                row.get("AMOUNT") == null ? 0 : Double.parseDouble(row.get("AMOUNT").toString()));
                awardApprovedSubcontractBean.setUpdateTimestamp((Timestamp)row.get("UPDATE_TIMESTAMP"));
                awardApprovedSubcontractBean.setUpdateUser((String)row.get("UPDATE_USER"));                
            }
        }
       return awardApprovedSubcontractBean; 
     }     
    
     /** The following methos has been written to get Cost Sharing for man
      * @param awardCostSharingBean is the input
      * @throws CoeusException is an Exception class, which is used to represnt any exception comes
      * in COEUS web module
      * @throws DBException is an Exception class, which is used to handle exceptions in dbEngine package 
      * during SQL Command execution
      * @return type is AwardCostSharingBean
      */     
     public CoeusVector getCostSharingForMan(String mitAwardNumber) throws 
        CoeusException, DBException {
        
        dbEngine=new DBEngineImpl();
        Vector result = null;
        Vector param = new Vector();
        CoeusVector cvCostSharing = null;
        HashMap row = null;
        AwardCostSharingBean awardCostSharingBean = null;
        param.addElement(new Parameter("MIT_AWARD_NUMBER",
        DBEngineConstants.TYPE_STRING, mitAwardNumber));
        
        if(dbEngine!=null){
            result = dbEngine.executeRequest("Coeus",
            "call DW_GET_A_COST_SHARING_FOR_MAN( << MIT_AWARD_NUMBER >>, <<OUT RESULTSET rset>> )",
            "Coeus", param);            
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }
        
        int listSize = result.size();
        cvCostSharing = new CoeusVector();
        if(listSize > 0) {            
            
            for(int rowNum = 0; rowNum < listSize; rowNum++) {
                row = (HashMap)result.elementAt(rowNum);
				awardCostSharingBean = new AwardCostSharingBean();
                awardCostSharingBean.setMitAwardNumber((String)row.get("MIT_AWARD_NUMBER"));
                awardCostSharingBean.setSequenceNumber(
                row.get("SEQUENCE_NUMBER") == null ? 0 : Integer.parseInt(row.get("SEQUENCE_NUMBER").toString()));
                awardCostSharingBean.setFiscalYear((String)row.get("FISCAL_YEAR"));
                awardCostSharingBean.setCostSharingPercentage(
                row.get("COST_SHARING_PERCENTAGE") == null ? 0 : Double.parseDouble(row.get("COST_SHARING_PERCENTAGE").toString()));
                awardCostSharingBean.setCostSharingType(
                row.get("COST_SHARING_TYPE") == null ? 0 : Integer.parseInt(row.get("COST_SHARING_TYPE").toString()));
                awardCostSharingBean.setSourceAccount((String)row.get("SOURCE_ACCOUNT"));
                awardCostSharingBean.setAw_DestinationAccount((String)row.get("DESTINATION_ACCOUNT"));
                awardCostSharingBean.setAmount(
                row.get("AMOUNT") == null ? 0 : Double.parseDouble(row.get("AMOUNT").toString()));
                awardCostSharingBean.setUpdateTimestamp((Timestamp)row.get("UPDATE_TIMESTAMP"));
                awardCostSharingBean.setUpdateUser((String)row.get("UPDATE_USER"));    
				cvCostSharing.addElement(awardCostSharingBean);
            }
        }    
        return cvCostSharing;
     }
     
     /** The following method has been written to get Award Sponsor Funding data
      * @param awardTransferingSponsorBean is the input
      * @throws CoeusException is an Exception class, which is used to represnt any exception comes
      * in COEUS web module
      * @throws DBException is an Exception class, which is used to handle exceptions in dbEngine package 
      * during SQL Command execution
      * @return type is CoeusVector
      */     
    public CoeusVector getAwardSponsorFunding(String mitAwardNumber) throws 
        CoeusException, DBException {
        
        dbEngine=new DBEngineImpl();
        Vector result=null;
        Vector param=new Vector();
        HashMap row=null;
		CoeusVector cvTransferingSponsor = null;
        AwardTransferingSponsorBean awardTransferingSponsorBean = null;
        param.addElement(new Parameter("MIT_AWARD_NUMBER",
        DBEngineConstants.TYPE_STRING, mitAwardNumber));
        
        if(dbEngine!=null){
            result = dbEngine.executeRequest("Coeus",
            "call DW_GET_AWARD_SPONSOR_FUNDING( << MIT_AWARD_NUMBER >>, <<OUT RESULTSET rset>> )",
            "Coeus", param);            
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }
        int listSize = result.size();
        cvTransferingSponsor = new CoeusVector();
                
        if(listSize>0){            
            
            for(int rowNum = 0; rowNum < listSize; rowNum++) {
                row = (HashMap)result.elementAt(rowNum);
				awardTransferingSponsorBean = new AwardTransferingSponsorBean();
                awardTransferingSponsorBean.setMitAwardNumber((String)row.get("MIT_AWARD_NUMBER"));
                awardTransferingSponsorBean.setSequenceNumber(
                row.get("SEQUENCE_NUMBER") == null ? 0 : Integer.parseInt(row.get("SEQUENCE_NUMBER").toString()));
                awardTransferingSponsorBean.setSponsorCode((String)row.get("SPONSOR_CODE"));
                awardTransferingSponsorBean.setUpdateTimestamp((Timestamp)row.get("UPDATE_TIMESTAMP"));
                awardTransferingSponsorBean.setUpdateUser((String)row.get("UPDATE_USER"));   
				cvTransferingSponsor.addElement(awardTransferingSponsorBean);
            }
        }
      return cvTransferingSponsor;  
    }    
    
    
    /** The following method has been written to get Valid Basis of Payment
     * @throws CoeusException is an Exception class, which is used to represnt any exception comes
     * in COEUS web module
     * @throws DBException is an Exception class, which is used to handle exceptions in dbEngine package 
     * during SQL Command execution
     * @return type is CoeusVector
     */    
    public CoeusVector getValidBasisOfPayment() throws CoeusException, DBException {
        
        dbEngine=new DBEngineImpl();
        Vector result=null;
        Vector param=new Vector();
        CoeusVector cvValidbasis = null;
        HashMap row=null;
        ValidBasisMethodPaymentBean validBasisMethodPaymentBean=null;
        
        if(dbEngine!=null){
            result = dbEngine.executeRequest("Coeus",
            "call DW_GET_VALID_BASIS_OF_PAYMENT(<<OUT RESULTSET rset>> )",
            "Coeus", param);            
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }
        int listSize = result.size();
        cvValidbasis = new CoeusVector();
        if(listSize>0){            
            
            for(int rowNum = 0; rowNum < listSize; rowNum++) {
                row = (HashMap)result.elementAt(rowNum);
				validBasisMethodPaymentBean=new ValidBasisMethodPaymentBean();
                validBasisMethodPaymentBean.setBasisOfPaymentCode(
                row.get("BASIS_OF_PAYMENT_CODE") == null ? 0 : Integer.parseInt(row.get("BASIS_OF_PAYMENT_CODE").toString()));
                validBasisMethodPaymentBean.setDescription((String)row.get("DESCRIPTION"));
				cvValidbasis.addElement(validBasisMethodPaymentBean);
            }    
        }    
      return cvValidbasis;  
    }    
    
    /**The following method has been written to get Valid Basis Method of Payment
     * @throws CoeusException is an Exception class, which is used to represnt any exception comes
     * in COEUS web module
     * @throws DBException is an Exception class, which is used to handle exceptions in dbEngine package 
     * during SQL Command execution
     * @return type is CoeusVector
     */    
    public CoeusVector getAllMethodOfPayment() throws CoeusException, DBException {
        
        dbEngine=new DBEngineImpl();
        Vector result=null;
        Vector param=new Vector();
        CoeusVector vctAwardAmountInfo = null;
        HashMap row=null;
		
        ValidBasisMethodPaymentBean validBasisMethodPaymentBean=null;
        
        if(dbEngine!=null){
            result = dbEngine.executeRequest("Coeus",
            "call DW_GET_ALL_METHOD_OF_PMT(<<OUT RESULTSET rset>> )",
            "Coeus", param);            
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }
        int listSize = result.size();
        
		vctAwardAmountInfo = new CoeusVector();
        
        if(listSize>0){            
            for(int rowNum = 0; rowNum < listSize; rowNum++) {
                row = (HashMap)result.elementAt(rowNum);
				validBasisMethodPaymentBean=new ValidBasisMethodPaymentBean();
                validBasisMethodPaymentBean.setCode(row.get("METHOD_OF_PAYMENT_CODE") == null ? "0" : 
                        row.get("METHOD_OF_PAYMENT_CODE").toString());
                validBasisMethodPaymentBean.setDescription((String) row.get("DESCRIPTION"));                                
                validBasisMethodPaymentBean.setUpdateTimestamp(
                        (Timestamp)row.get("UPDATE_TIMESTAMP"));                
                validBasisMethodPaymentBean.setUpdateUser((String)
                         row.get("UPDATE_USER"));    
				vctAwardAmountInfo.addElement(validBasisMethodPaymentBean);
            }
        }    
      return   vctAwardAmountInfo;
    }    
    
    /** The following method has been written to get PaymentSchedule
     * @param awardPaymentScheduleBean is the input
     * @throws CoeusException is an Exception class, which is used to represnt any exception comes
     * in COEUS web module
     * @throws DBException is an Exception class, which is used to handle exceptions in dbEngine package 
     * during SQL Command execution
     * @return type is AwardPaymentScheduleBean
     */    
    public CoeusVector getPaymentSchedule(String mitAwardNumber) throws 
            CoeusException, DBException {
        
        dbEngine=new DBEngineImpl();
        Vector result=null;
        Vector param=new Vector();
        CoeusVector vctAwardAmountInfo = null;
        HashMap row=null;
                
       param.addElement(new Parameter("MIT_AWARD_NUMBER",
        DBEngineConstants.TYPE_STRING, mitAwardNumber));
        
        if(dbEngine!=null){
            result = dbEngine.executeRequest("Coeus",
            "call DW_GET_A_PAYMENT_SCHEDULE( << MIT_AWARD_NUMBER >>, <<OUT RESULTSET rset>> )",
            "Coeus", param);        
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }
       
       int listSize = result.size();
        
        AwardPaymentScheduleBean awardPaymentScheduleBean = null;
        if(listSize>0){            
            vctAwardAmountInfo = new CoeusVector();
            for(int rowNum = 0; rowNum < listSize; rowNum++) {
                row = (HashMap)result.elementAt(rowNum);
				awardPaymentScheduleBean = new AwardPaymentScheduleBean();
                awardPaymentScheduleBean.setMitAwardNumber((String)row.get("MIT_AWARD_NUMBER"));
                awardPaymentScheduleBean.setSequenceNumber(
                row.get("SEQUENCE_NUMBER") == null ? 0 : Integer.parseInt(row.get("SEQUENCE_NUMBER").toString()));
                awardPaymentScheduleBean.setDueDate(
                row.get("DUE_DATE") == null ?
                null : new Date(((Timestamp) row.get("DUE_DATE")).getTime()));
                awardPaymentScheduleBean.setAmount(
                row.get("AMOUNT") == null ? 0 : Double.parseDouble(row.get("AMOUNT").toString()));
                awardPaymentScheduleBean.setUpdateTimestamp((Timestamp)row.get("UPDATE_TIMESTAMP"));                
                awardPaymentScheduleBean.setUpdateUser((String)row.get("UPDATE_USER"));                               
                awardPaymentScheduleBean.setSubmitDate(
                row.get("SUBMIT_DATE") == null ?
                null : new Date(((Timestamp) row.get("SUBMIT_DATE")).getTime()));
                awardPaymentScheduleBean.setSubmitBy((String)row.get("SUBMITTED_BY"));
                awardPaymentScheduleBean.setInvoiceNumber((String)row.get("INVOICE_NUMBER"));
                awardPaymentScheduleBean.setStatusDescription((String)row.get("STATUS_DESCRIPTION"));
				vctAwardAmountInfo.addElement(awardPaymentScheduleBean);
            }
        }    
      return vctAwardAmountInfo;  
    }    
    
    /** The following method has been written to retrieve the data related to 
     * CloseOut process.
     * @param awardCloseOutBean is the input
     * @throws CoeusException is an Exception class, which is used to represnt any exception comes
     * in COEUS web module
     * @throws DBException is an Exception class, which is used to handle exceptions in dbEngine package 
     * during SQL Command execution
     * @return type is AwardCloseOutBean
     */    
    public CoeusVector getCloseOut(String mitAwardNumber) throws 
            CoeusException, DBException {
        
        dbEngine=new DBEngineImpl();
        Vector result=null;
        Vector param=new Vector();
        CoeusVector vctAwardAmountInfo = null;
        HashMap row=null;
        AwardCloseOutBean awardCloseOutBean = null;
       param.addElement(new Parameter("MIT_AWARD_NUMBER",
        DBEngineConstants.TYPE_STRING, mitAwardNumber));
        
        if(dbEngine!=null){
            result = dbEngine.executeRequest("Coeus",
            "call DW_GET_A_CLOSEOUT( << MIT_AWARD_NUMBER >>, <<OUT RESULTSET rset>> )",
            "Coeus", param);        
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }
       
       int listSize = result.size();
       if(listSize>0){            
            vctAwardAmountInfo = new CoeusVector();
            for(int rowNum = 0; rowNum < listSize; rowNum++) {
                row = (HashMap)result.elementAt(rowNum);
				awardCloseOutBean=new AwardCloseOutBean();
                awardCloseOutBean.setMitAwardNumber((String)row.get("MIT_AWARD_NUMBER"));
                awardCloseOutBean.setSequenceNumber(
                row.get("SEQUENCE_NUMBER") == null ? 0 : Integer.parseInt(row.get("SEQUENCE_NUMBER").toString()));
                awardCloseOutBean.setFinalInvSubmissionDate(
                row.get("FINAL_SUBMISSION_DATE") == null ?
                null : new Date(((Timestamp) row.get("FINAL_SUBMISSION_DATE")).getTime()));
                awardCloseOutBean.setFinalTechSubmissionDate(
                row.get("FINAL_TECH_SUBMISSION_DATE") == null ?
                null : new Date(((Timestamp) row.get("FINAL_TECH_SUBMISSION_DATE")).getTime()));
                awardCloseOutBean.setFinalPatentSubmissionDate(
                row.get("FINAL_PATENT_SUBMISSION_DATE") == null ?
                null : new Date(((Timestamp) row.get("FINAL_PATENT_SUBMISSION_DATE")).getTime()));
                awardCloseOutBean.setFinalPropSubmissionDate(
                row.get("FINAL_PROP_SUBMISSION_DATE") == null ?
                null : new Date(((Timestamp) row.get("FINAL_PROP_SUBMISSION_DATE")).getTime()));
                awardCloseOutBean.setArchiveLocation((String)row.get("ARCHIVE_LOCATION"));
                awardCloseOutBean.setCloseOutDate(
                row.get("CLOSEOUT_DATE") == null ?
                null : new Date(((Timestamp) row.get("CLOSEOUT_DATE")).getTime()));
                awardCloseOutBean.setUpdateTimestamp(
                        (Timestamp)row.get("UPDATE_TIMESTAMP"));                
                awardCloseOutBean.setUpdateUser((String)
                         row.get("UPDATE_USER"));   
				vctAwardAmountInfo.addElement(awardCloseOutBean);
            }
       }
       return vctAwardAmountInfo;
    }   
   // Added b Jobin - start 
   /**This method used check whether signature checkbox to be selected or not.
      * <li>To check the data, it uses the function fn_signature_req_on_notice.
      *
      * @return int 0 if needed the signature check box and 1 if not needed.
      * @param mitAwardNumber String
      * @param seqNumber String
      * @exception DBException if any error during database transaction.
      * @exception CoeusException if the instance of dbEngine is not available. 
      **/
     public int getSignatureCheck(String mitAwardNumber, int seqNumber) throws CoeusException, DBException{
        int isRequired = 0;
        Vector param= new Vector();
        Vector result = new Vector();
        param.add(new Parameter("as_award_number",
            DBEngineConstants.TYPE_STRING, mitAwardNumber));
        param.add(new Parameter("ai_sequence_number",
            DBEngineConstants.TYPE_INT, ""+seqNumber));        
        
        /* calling stored function */
        if(dbEngine!=null){
            result = dbEngine.executeFunctions("Coeus",
            "{ <<OUT INTEGER IS_NEEDED>> = "
            +" call fn_signature_req_on_notice(<<as_award_number>>, <<ai_sequence_number>> ) }", param);
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }
        if(!result.isEmpty()){
            HashMap rowParameter = (HashMap)result.elementAt(0);
            isRequired = Integer.parseInt(rowParameter.get("IS_NEEDED").toString());
        }
        return isRequired;
    }     
	 
	 /**This method used check whether signature checkbox to be selected or not.
      * <li>To check the data, it uses the function dw_get_award.
      *
      * @return int 0 if needed the signature check box and 1 if not needed.
      * @param mitAwardNumber String
	  * @return AwardBean
      * @exception DBException if any error during database transaction.
      * @exception CoeusException if the instance of dbEngine is not available. 
      **/
    /* public AwardBean getAwardDetails(String mitAwardNumber) throws CoeusException, DBException {
        
        Vector param= new Vector();
        Vector result = new Vector();
		HashMap row = null;
		AwardBean awardBean = new AwardBean();
        param.add(new Parameter("MIT_AWARD_NUMBER",
            DBEngineConstants.TYPE_STRING, ""+mitAwardNumber));
        
		/* calling stored function */
      /*  if(dbEngine!=null){
            result = dbEngine.executeRequest("Coeus",
            "call DW_GET_AWARD( << AW_MIT_AWARD_NUMBER >>, <<OUT RESULTSET rset>> )",
            "Coeus", param);        
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }
		int listSize = result.size();
        if (listSize > 0) {            
            CoeusVector cvAwardDetails = new CoeusVector();
            for(int index = 0; index < listSize; index++) {
                row = (HashMap)result.elementAt(index);
                
				awardBean.setMitAwardNumber((String)row.get("MIT_AWARD_NUMBER"));
                
				awardBean.setSequenceNumber(
                row.get("SEQUENCE_NUMBER") == null ? 0 : Integer.parseInt(row.get("SEQUENCE_NUMBER").toString()));
				
				awardBean.setModificationNumber((String)row.get("MODIFICATION_NUMBER"));
				
				awardBean.setSponsorAwardNumber((String)row.get("SPONSOR_AWARD_NUMBER"));
				
				awardBean.setStatusCode(
                row.get("STATUS_CODE") == null ? 0 : Integer.parseInt(row.get("STATUS_CODE").toString()));
				
				awardBean.setTemplateCode(
                row.get("TEMPLATE_CODE") == null ? 0 : Integer.parseInt(row.get("TEMPLATE_CODE").toString()));
                
				awardBean.setAwardExecutionDate(
                row.get("AWARD_EXECUTION_DATE") == null ?
                null : new Date(((Timestamp) row.get("AWARD_EXECUTION_DATE")).getTime()));
				
                awardBean.setAwardEffectiveDate(
                row.get("AWARD_EFFECTIVE_DATE") == null ?
                null : new Date(((Timestamp) row.get("AWARD_EFFECTIVE_DATE")).getTime()));
				
				awardBean.setBeginDate(
                row.get("BEGIN_DATE") == null ?
                null : new Date(((Timestamp) row.get("BEGIN_DATE")).getTime()));
				
				awardBean.setSponsorCode((String)row.get("SPONSOR_CODE"));
				awardBean.setAccountNumber((String)row.get("ACCOUNT_NUMBER"));
				awardBean.setApprvdEquipmentIndicator((String)row.get("APPRVD_EQUIPMENT_INDICATOR"));
				awardBean.setApprvdForeignTripIndicator((String)row.get("APPRVD_FOREIGN_TRIP_INDICATOR"));
				awardBean.setApprvdSubcontractIndicator((String)row.get("APPRVD_SUBCONTRACT_INDICATOR"));
				awardBean.setPaymentScheduleIndicator((String)row.get("PAYMENT_SCHEDULE_INDICATOR"));
				awardBean.setIdcIndicator((String)row.get("IDC_INDICATOR"));
				awardBean.setTransferSponsorIndicator((String)row.get("TRANSFER_SPONSOR_INDICATOR"));
				awardBean.setCostSharingIndicator((String)row.get("COST_SHARING_INDICATOR"));
				
				awardBean.setUpdateTimestamp(
                        (Timestamp)row.get("UPDATE_TIMESTAMP")); 
				awardBean.setSpecialReviewIndicator((String)row.get("SPECIAL_REVIEW_INDICATOR"));
				awardBean.setScienceCodeIndicator((String)row.get("SCIENCE_CODE_INDICATOR"));
				awardBean.setUpdateUser((String)
                         row.get("UPDATE_USER")); 
				awardBean.setNsfCode((String)row.get("NSF_CODE"));
                
            }
       }
        return awardBean;
    }*/
    
	 /**This method used check whether signature checkbox to be selected or not.
      * <li>To check the data, it uses the function dw_get_award.
      *
      * @return int 0 if needed the signature check box and 1 if not needed.
      * @param mitAwardNumber String
	  * @param commentCode int
	  * @return CoeusVector
      * @exception DBException if any error during database transaction.
      * @exception CoeusException if the instance of dbEngine is not available. 
      **/
     public CoeusVector getAwardComments(String mitAwardNumber) throws CoeusException, DBException {
        CoeusVector cvAwardComments = new CoeusVector();
        Vector param= new Vector();
        Vector result = new Vector();
		HashMap row = null;
		AwardCommentsBean awardCommentsBean = new AwardCommentsBean();
        param.add(new Parameter("MIT_AWARD_NUMBER",
            DBEngineConstants.TYPE_STRING, ""+mitAwardNumber));
		
		/* calling stored function */
        if(dbEngine!=null){
            result = dbEngine.executeRequest("Coeus",
            "call DW_GET_AWARD_COMMENTS( << AS_MIT_AWARD_NUM >>, <<OUT RESULTSET rset>> )",
            "Coeus", param);        
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }
		int listSize = result.size();
        if (listSize > 0) {            
            
            for(int index = 0; index < listSize; index++) {
                row = (HashMap)result.elementAt(index);
                
				awardCommentsBean.setMitAwardNumber((String)row.get("MIT_AWARD_NUMBER"));
                
				awardCommentsBean.setSequenceNumber(
                row.get("SEQUENCE_NUMBER") == null ? 0 : Integer.parseInt(row.get("SEQUENCE_NUMBER").toString()));
				
				awardCommentsBean.setCommentCode(
                row.get("COMMENT_CODE") == null ? 0 : Integer.parseInt(row.get("COMMENT_CODE").toString()));
                
				awardCommentsBean.setCheckListPrintFlag(((Boolean)(row.get("CHECKLIST_PRINT_FLAG"))).booleanValue());
				
				awardCommentsBean.setComments((String)row.get("COMMENTS"));
				
				awardCommentsBean.setUpdateTimestamp(
                        (Timestamp)row.get("UPDATE_TIMESTAMP")); 
				
				awardCommentsBean.setUpdateUser((String)
                         row.get("UPDATE_USER")); 
				cvAwardComments.addElement(awardCommentsBean);
			}
       }
        return cvAwardComments;
    }
	 
	 /** Method used to get Award Amount Info for the given Award Number.
     * <li>To fetch the data, it uses DW_GET_MONEY_AND_END_DATES.
     * 
     * @return AwardAmountInfoBean awardAmountInfoBean
     * @param mitAwardNumber is used to get AwardAmountInfoBean
     * @exception DBException if any error during database transaction.
     * @exception CoeusException if the instance of dbEngine is not available.
     */
    public AwardAmountInfoBean getMoneyAndEndDates(String mitAwardNumber)
    throws CoeusException, DBException{
        
        Vector result = new Vector(3,2);
        Vector param= new Vector();
        HashMap row = null;        
        param = new Vector(3,2);
		AwardAmountInfoBean awardAmountInfoBean = null;
        param.addElement(new Parameter("MIT_AWARD_NUMBER",
            DBEngineConstants.TYPE_STRING, mitAwardNumber));
        if(dbEngine!=null){
            result = dbEngine.executeRequest("Coeus",
            "call DW_GET_MONEY_AND_END_DATES ( <<MIT_AWARD_NUMBER>>, <<OUT RESULTSET rset>> )",
            "Coeus", param);
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }
        int listSize = result.size();
        Vector messageList = null;
        if(listSize>0){
            row = (HashMap)result.elementAt(0);
            awardAmountInfoBean.setMitAwardNumber((String)
                row.get("MIT_AWARD_NUMBER"));
            awardAmountInfoBean.setSequenceNumber(
                row.get("SEQUENCE_NUMBER") == null ? 0 : Integer.parseInt(row.get("SEQUENCE_NUMBER").toString()));
            awardAmountInfoBean.setAmountSequenceNumber(
                row.get("AMOUNT_SEQUENCE_NUMBER") == null ? 0 : Integer.parseInt(row.get("AMOUNT_SEQUENCE_NUMBER").toString()));
            awardAmountInfoBean.setAnticipatedTotalAmount(
                row.get("ANTICIPATED_TOTAL_AMOUNT") == null ? 0 : Double.parseDouble(row.get("ANTICIPATED_TOTAL_AMOUNT").toString()));
            awardAmountInfoBean.setAnticipatedDistributableAmount(
                row.get("ANT_DISTRIBUTABLE_AMOUNT") == null ? 0 : Double.parseDouble(row.get("ANT_DISTRIBUTABLE_AMOUNT").toString()));
            awardAmountInfoBean.setFinalExpirationDate(
                row.get("FINAL_EXPIRATION_DATE") == null ?
                null : new Date(((Timestamp) row.get("FINAL_EXPIRATION_DATE")).getTime()));
            awardAmountInfoBean.setCurrentFundEffectiveDate(
                row.get("CURRENT_FUND_EFFECTIVE_DATE") == null ?
                null : new Date(((Timestamp) row.get("CURRENT_FUND_EFFECTIVE_DATE")).getTime()));
            awardAmountInfoBean.setEffectiveDate(
                row.get("EFFECTIVE_DATE") == null ?
                null : new Date(((Timestamp) row.get("EFFECTIVE_DATE")).getTime()));
            awardAmountInfoBean.setObligationExpirationDate(
                row.get("OBLIGATION_EXPIRATION_DATE") == null ?
                null : new Date(((Timestamp) row.get("OBLIGATION_EXPIRATION_DATE")).getTime()));
            awardAmountInfoBean.setAmountObligatedToDate(
                row.get("AMOUNT_OBLIGATED_TO_DATE") == null ? 0 : Double.parseDouble(row.get("AMOUNT_OBLIGATED_TO_DATE").toString()));
            awardAmountInfoBean.setObliDistributableAmount(
                row.get("OBLI_DISTRIBUTABLE_AMOUNT") == null ? 0 : Double.parseDouble(row.get("OBLI_DISTRIBUTABLE_AMOUNT").toString()));
            awardAmountInfoBean.setTransactionId((String)
                row.get("TRANSACTION_ID"));
            awardAmountInfoBean.setEntryType((String)
                row.get("ENTRY_TYPE"));
            awardAmountInfoBean.setEomProcessFlag(
                row.get("EOM_PROCESS_FLAG") == null ? false : row.get("EOM_PROCESS_FLAG").toString().equalsIgnoreCase("Y") ? true : false);
            awardAmountInfoBean.setAnticipatedChange(
                row.get("ANTICIPATED_AMOUNT_CHANGE") == null ? 0 : Double.parseDouble(row.get("ANTICIPATED_AMOUNT_CHANGE").toString()));
            awardAmountInfoBean.setObligatedChange(
                row.get("OBLIGATED_AMOUNT_CHANGE") == null ? 0 : Double.parseDouble(row.get("OBLIGATED_AMOUNT_CHANGE").toString()));
            awardAmountInfoBean.setUpdateTimestamp((Timestamp)
                row.get("UPDATE_TIMESTAMP"));
            awardAmountInfoBean.setUpdateUser( (String)
                row.get("UPDATE_USER"));
        }
        
        return awardAmountInfoBean;
    }
	
    /**Main method to create Constructors & to call methods.
     * @param args
     */    
    public static void main(String args[]) {
        try{
            AwardReportTxnBean awardReportTxnBean=new AwardReportTxnBean("COEUS");
            // Call to method getAccountForAward            
//            int account_number = awardReportTxnBean.getAccountForAward("000007-001");
            
            //Call to method getAoInfo
//            PersonInfoBean personInfoBean=new PersonInfoBean();
//            UserInfoBean userInfoBean=new UserInfoBean();            
//            userInfoBean.setUnitNumber("061061");            
//            awardReportTxnBean.getAoInfo(userInfoBean);
//            
            
//            Call to method getIsAllDiscStatusComplete
//            String moduleItemKey="04070021";
//            String moduleCode="2";
//            int number=awardReportTxnBean.getIsAllDiscStatusComplete(moduleItemKey, moduleCode);
            
            
//             Call to method getChildAward
//            AwardHierarchyBean awardHierarchyBean=new AwardHierarchyBean();
//            awardHierarchyBean.setParentMitAwardNumber("000049-001");
//            awardReportTxnBean.getChildAward(awardHierarchyBean);
            
//              Call to getScienceCodeForMan
//              AwardScienceCodeBean awardScienceCodeBean=new AwardScienceCodeBean();
//              awardScienceCodeBean.setMitAwardNumber("000007-001");
//              awardReportTxnBean.getScienceCodeForMan(awardScienceCodeBean);
            
//              Call to getReportBean
//              ReportBean reportBean=new ReportBean();
//              awardReportTxnBean.getReport();
            
//              Call to getFrequency
//              FrequencyBean frequencyBean=new FrequencyBean();
//              awardReportTxnBean.getFrequency();
            
//             Call to getApprovedEquipments           
//              AwardApprovedEquipmentBean awardApprovedEquipmentBean=new AwardApprovedEquipmentBean();
//              awardApprovedEquipmentBean.setMitAwardNumber("000007-001");
//              awardReportTxnBean.getApprovedEquipments(awardApprovedEquipmentBean);
            
//            Call to getApprovedForeignTripForMan  
//            AwardApprovedForeignTripBean awardApprovedForeignTripBean=new AwardApprovedForeignTripBean();
//            awardApprovedForeignTripBean.setMitAwardNumber("000007-001");                               
//            awardReportTxnBean.getApprovedForeignTripForMan(awardApprovedForeignTripBean);
            
//            Call to getApprovedSubContracts 
//            AwardApprovedSubcontractBean awardApprovedSubcontractBean=new AwardApprovedSubcontractBean();
//            awardApprovedSubcontractBean.setMitAwardNumber("000006-001");
//            awardReportTxnBean.getApprovedSubContracts(awardApprovedSubcontractBean);
//            System.out.println("The return data is  " +);
            
//            Call to getCostSharingForMan
//            AwardCostSharingBean awardCostSharingBean=new AwardCostSharingBean();
//            awardCostSharingBean.setMitAwardNumber("000008-001");
//            awardReportTxnBean.getCostSharingForMan(awardCostSharingBean);
            
//            Call to getAwardSponsorFunding
//            AwardTransferingSponsorBean awardTransferingSponsorBean=new AwardTransferingSponsorBean();
//            awardTransferingSponsorBean.setMitAwardNumber("000007-001");
//            awardReportTxnBean.getAwardSponsorFunding(awardTransferingSponsorBean);
            
//            Call to getValidBasisOfPayment
//            ValidBasisMethodPaymentBean validBasisMethodPaymentBean=new ValidBasisMethodPaymentBean();
//            awardReportTxnBean.getValidBasisOfPayment();
            
//            Call to getAllMethodOfOfPayment
//            ValidBasisMethodPaymentBean validBasisMethodPaymentBean=new ValidBasisMethodPaymentBean();
//            awardReportTxnBean.getAllMethodOfOfPayment();
             
//             Call to getPaymentSchedule
//             AwardPaymentScheduleBean awardPaymentScheduleBean=new AwardPaymentScheduleBean();
//             awardPaymentScheduleBean.setMitAwardNumber("000007-001");
//             awardReportTxnBean.getPaymentSchedule(awardPaymentScheduleBean);
             
//             Call to getCloseout
//             AwardCloseOutBean awardCloseOutBean=new AwardCloseOutBean();
//             awardCloseOutBean.setMitAwardNumber("000007-001");
//             awardReportTxnBean.getCloseOut(awardCloseOutBean);
            
//            Call to getCoiDisclForItem
//             CoiDisclForItemBean coiDisclForItemBean=new CoiDisclForItemBean();
//             coiDisclForItemBean.setModuleCode("2");
//             coiDisclForItemBean.setModuleItemKey("04070021");
//             awardReportTxnBean.getCoiDisclForItem(coiDisclForItemBean);
             
        }catch(Exception ex){
            ex.printStackTrace();
        }                
    }    
}    