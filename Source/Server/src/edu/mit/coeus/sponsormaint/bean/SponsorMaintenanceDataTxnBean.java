/*
 * @(#)SponsorMaintenanceDataTxnBean.java 1.0 8/17/02 9:37 AM
 *
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 */

/* PMD check performed, and commented unused imports and variables on 20-APR-2011
 * by Bharati
 */

package edu.mit.coeus.sponsormaint.bean;

import edu.mit.coeus.rolodexmaint.bean.RolodexDetailsBean;
import edu.mit.coeus.rolodexmaint.bean.RolodexMaintenanceDataTxnBean;
import edu.mit.coeus.user.bean.UserMaintDataTxnBean;
import edu.mit.coeus.utils.CoeusVector;
import edu.mit.coeus.utils.UtilFactory;
import edu.mit.coeus.utils.dbengine.DBEngineImpl;
import edu.mit.coeus.utils.dbengine.DBException;
import edu.mit.coeus.utils.dbengine.DBEngineConstants;
import edu.mit.coeus.utils.dbengine.Parameter;
import edu.mit.coeus.utils.dbengine.ProcReqParameter;
import edu.mit.coeus.utils.dbengine.TransactionMonitor;
//import edu.mit.coeus.utils.UtilFactory;
import edu.mit.coeus.utils.ComboBoxBean;
import edu.mit.coeus.sponsormaint.bean.SponsorFormsBean;
import edu.mit.coeus.sponsormaint.bean.SponsorTemplateBean;
import edu.mit.coeus.utils.locking.LockingBean;
//import edu.mit.coeus.utils.locking.LockingException;
import edu.mit.coeus.utils.KeyConstants;

import java.util.Vector;
//import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
//import java.io.InputStream;
import java.util.Hashtable;
import java.util.HashMap;
import java.sql.Timestamp;
//import java.sql.SQLException;
import java.sql.Connection;

import edu.mit.coeus.exception.CoeusException;
import edu.mit.coeus.utils.CoeusFunctions;
/** 
 * This class provides the methods for performing all procedure executions for
 * a <CODE>SponsorMaintenanceForm</CODE>. Various methods are used to fetch data
 * for <CODE>SponsorMaintenanceForm</CODE> from the Database.
 * All methods are used <code>DBEngineImpl</code> singleton instance
 * for the databse interaction.
 * This class used by <CODE>SponsorMaintenanceServlet</CODE> communicate between
 * <CODE>SponsorMaintenanceForm</CODE>,<CODE>SponsorBaseWindow</CODE> and
 * <CODE>SponsorMaintenanceDataTxnBean</CODE>.
 *
 * @version 1.0 August 17, 2002, 9:37 AM
 * @author Mukundan C
 * @modified by Sagin
 * @date 28-10-02
 * Description : Implemented Standard Error Handling.
 */

public class SponsorMaintenanceDataTxnBean {

    /*
     *  Singleton instance of a dbEngine
     */
    private DBEngineImpl dbEngine;
    private SponsorMaintenanceFormBean sponsorFormData ;
    private RolodexDetailsBean rldxBean;
    private RolodexMaintenanceDataTxnBean rldTxnBean =
    new RolodexMaintenanceDataTxnBean();
    
    private static final String rowLockStr = "osp$Sponsor_";
    private static final String DSN = "Coeus";
    private static final String PACKAGE_NUMBER = "PACKAGE_NUMBER";
    private static final String SPONSOR_CODE = "SPONSOR_CODE";
    /*
     *  To hold the result after the execution of the dbEngine.
     */
    private Vector vectResult;

    private TransactionMonitor  transMon;
    
    private String userId;
    
    // Connection instance added by Shivakumar for locking enhancement
    private Connection conn = null;
   
    /** Creates new <CODE>SponsorMaintenanceDataTxnBean</CODE>
     */
    public SponsorMaintenanceDataTxnBean() {
        dbEngine = new DBEngineImpl();
        transMon = TransactionMonitor.getInstance();
    }
    
    /** Creates new <CODE>SponsorMaintenanceDataTxnBean</CODE>
     */
    public SponsorMaintenanceDataTxnBean(String userId) {
        dbEngine = new DBEngineImpl();
        transMon = TransactionMonitor.getInstance();
        this.userId = userId;
    }    

    /**
     * This method returns the boolean on the count sponsor code if the
     * sponsor code already present it will return true else false 
     * for duplicate check of sponsor code.
     * <li>To fetch the data, it uses get_sponsor_count procedure.
     *
     *  @param sponsorID input for the procedure
     *  @return boolean returns true for the count avaiable 
     *  @exception DBException if any error during database transaction.
     *  @exception CoeusException if the instance of dbEngine is not available.
     */
    public boolean getSponsorCount(String sponsorID)
                        throws CoeusException, DBException{
        Vector result = new Vector(3,2);
        Vector param= new Vector();
        String count="";
        param.addElement(new Parameter("SPONSOR_CODE",
                        DBEngineConstants.TYPE_STRING,sponsorID.trim()));
        sponsorFormData = new SponsorMaintenanceFormBean();
        if(dbEngine!=null){
            result = dbEngine.executeRequest("Coeus",
            "call get_sponsor_count (<< SPONSOR_CODE >>,<< OUT INTEGER COUNT >>)",
            "Coeus", param);
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }
        if (!result.isEmpty()){
            HashMap sponsorCount = (HashMap)result.elementAt(0);
            count = sponsorCount.get("COUNT").toString();
        }
        if (Integer.parseInt(count) == 0)
            return false;
        else
            return true;

    }
    
    /**
     * This method returns a boolean based on sponsor code autogenerate parameter parameter value. if the
     * parameter setting indicates autogenerate sponsor code it will return true else false 
     * to accept sponsor code from user.
     * <li>To fetch the data, it uses Fn_Check_Generate_Sponsor_Code procedure.
     *
     *  @return boolean returns true if autogenerate is required 
     *  @exception DBException if any error during database transaction.
     *  @exception CoeusException if the instance of dbEngine is not available.
     */
    public boolean getGenerateSponsorParamValue()
                        throws CoeusException, DBException{
        Vector result = new Vector(3,2);
        Vector param= new Vector();
        String autogenFlag="";
        //System.out.println("***inside getGenerateSponsorParamValue - about to call sp***");
        if(dbEngine!=null){
            result = dbEngine.executeFunctions("Coeus",
            "{ <<OUT INTEGER AUTOGENFLAG>> = call Fn_Check_Generate_Sponsor_Code() }", param);
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }
        //System.out.println("***getGenerateSponsorParamValue - call sp - done***");
        if (!result.isEmpty()){
            HashMap sponsorFlag = (HashMap)result.elementAt(0);
            autogenFlag = sponsorFlag.get("AUTOGENFLAG").toString();
        }
        if (Integer.parseInt(autogenFlag) == 0)
            return false;
        else
            return true;

    }
    
    /**
     * This method returns a generated sponsor code. 
     * <li>To fetch the data, it uses Fn_Generate_Sponsor_Code procedure.
     *
     *  @return boolean returns true if autogenerate is required 
     *  @exception DBException if any error during database transaction.
     *  @exception CoeusException if the instance of dbEngine is not available.
     */
    public String generateSponsorCode()
                        throws CoeusException, DBException{
        Vector result = new Vector(3,2);
        Vector param= new Vector();
        String sponsorCode="";
        //System.out.println("***inside generateSponsorCode - about to call sp***");
        if(dbEngine!=null){
            result = dbEngine.executeFunctions("Coeus",
            "{ <<OUT STRING SPONSORCODE>> = call Fn_Generate_Sponsor_Code() }", param);
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }
        //System.out.println("***Fn_Generate_Sponsor_Code - call sp - done***");
        if (!result.isEmpty()){
            HashMap sponsorCodeResult = (HashMap)result.elementAt(0);
            sponsorCode = sponsorCodeResult.get("SPONSORCODE").toString();
            //System.out.println("sponsor code - in proc func ***" + sponsorCode);
        }

        return sponsorCode;

    }

    /**
     *  This method is used to auto generate the sequence number for rolodex id's .
     *  <li>To fetch the data, it uses fn_get_rolodex_id function.
     *
     *  @return String Next Sequence Number
     *  @exception DBException if any error during database transaction.
     *  @exception CoeusException if the instance of dbEngine is not available.
     */
    public String getNextSeqNum() throws  CoeusException, DBException{
        String seqNum = null;
        Vector param= new java.util.Vector();
        Vector result = new Vector();
        /* calling stored function */
        if(dbEngine!=null){
            result = dbEngine.executeFunctions("Coeus",
            "{ <<OUT INTEGER SEQNUMBER>> = call fn_get_rolodex_id() }", param);
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }
        if(!result.isEmpty()){
            HashMap nextNumRow = (HashMap)result.elementAt(0);
            seqNum = nextNumRow.get("SEQNUMBER").toString();
        }
        return seqNum;
    }
    /**
     *  This method populates the ComboBox meant for the Sponsor Type
     *  in the SponsorMaintenanceForm.
     *  <li>To fetch the data, it uses the procedure dw_get_sponsor_type.
     *
     *  @return Vector map of all sponsor type with sponsor code as key and
     *  sponsor type description as value
     *  @exception DBException if any error during database transaction.
     *  @exception CoeusException if the instance of dbEngine is not available.
     */
    public Vector getSponsorTypes() throws CoeusException, DBException{
        Vector result = new Vector(3,2);
        Vector param= new Vector();
        Vector types = new Vector();
        if(dbEngine!=null){
            result = dbEngine.executeRequest("Coeus",
            "call dw_get_sponsor_type  ( <<OUT RESULTSET rset>> )",
            "Coeus", param);
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }
        for(int i=0;i<result.size();i++){
            HashMap sponsorTypeRow = (HashMap)result.elementAt(i);
            types.addElement(new ComboBoxBean(sponsorTypeRow.get(
            "SPONSOR_TYPE_CODE").toString(),
            sponsorTypeRow.get("DESCRIPTION").toString()));
        }
        return types;
    }

    /**
     *  This method populates the ComboBox meant for the State
     *  in the SponsorMaintenanceForm.
     *  <li>To fetch the data, it uses the procedure dw_get_state_codes.
     *
     *  @return vector list of all countries with country code as key and
     *  country name as value.
     *  @exception DBException if any error during database transaction.
     *  @exception CoeusException if the instance of dbEngine is not available.
     */
    public Vector getStates() throws CoeusException, DBException{
        Vector result = new Vector(3,2);
        Vector param= new Vector();
        Vector states = new Vector();
        if(dbEngine!=null){
            result = dbEngine.executeRequest("Coeus",
            "call dw_get_state_codes  ( <<OUT RESULTSET rset>> )",
            "Coeus", param);
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }
        for(int i=0;i<result.size();i++){
            HashMap stateRow = (HashMap)result.elementAt(i);
            states.addElement(new ComboBoxBean(stateRow.get("STATE_CODE").toString(),
            stateRow.get("DESCRIPTION").toString()));
        }
        return states;
    }
    
    // 4467: If you put a country other than US, the state drop down should display the 'states' for the country - Start
    /**
     *  Method used to get the State info along with the corresponding Countries.
     *  <li>To fetch the data, it uses the procedure dw_get_state_codes.
     *
     *  @return HashMap country code as key of the hashmap and vecor of
     *  ComboBoxBean as the corresponding state info of the country.
     *
     *  @exception DBException if any error during database transaction.
     *  @exception CoeusException if the instance of dbEngine is not available.
     */
    public HashMap fetchStatesWithCountry() throws CoeusException, DBException{
        Vector result = new Vector(3,2);
        Vector param= new Vector();
        Vector vecStates = new Vector();
        HashMap hmStates = new HashMap();
        HashMap hmCountryStateInfo= new HashMap();
        Vector vecCountryList = getCountries();
        String country = "";
        ComboBoxBean stateDetails;
        if(dbEngine!=null){
            result = dbEngine.executeRequest("Coeus",
                    "call dw_get_state_codes  ( <<OUT RESULTSET rset>> )",
                    "Coeus", param);
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }
        for(int i=0;i<result.size();i++){
            HashMap stateRow = (HashMap)result.elementAt(i);
            hmStates = new HashMap();
            hmStates.put(stateRow.get("COUNTRY_CODE"),(new ComboBoxBean(
                    (String) stateRow.get("STATE_CODE"),
                    (String) stateRow.get("DESCRIPTION"))));
            vecStates.addElement(hmStates);
        }
        
        if(vecCountryList != null && vecCountryList.size() >0 && vecStates != null){
            int countryCount = vecCountryList.size();
            int stateCount = vecStates.size();
            for(int coutryIndex = 0; coutryIndex < countryCount; coutryIndex++){
                ComboBoxBean cmbCountryBean =
                        (ComboBoxBean)vecCountryList.get(coutryIndex);
                country = cmbCountryBean.getCode();
                Vector vecStatesToBeAdded = new Vector();
                
                if( stateCount > 0){
                    for(int stateIndex = 0 ; stateIndex < stateCount; stateIndex++){
                        HashMap hmTmpStateData =
                                (HashMap)vecStates.get(stateIndex);
                        if( hmTmpStateData != null && hmTmpStateData.containsKey(country)){
                            ComboBoxBean cmStateBean = (ComboBoxBean) hmTmpStateData.get(country);
                            vecStatesToBeAdded.addElement(cmStateBean);
                        }
                    }
                }
                hmCountryStateInfo.put(country,vecStatesToBeAdded);
            }
        }
        return hmCountryStateInfo;
    }
    // 4467: If you put a country other than US, the state drop down should display the 'states' for the country - End

    /**
     *  This method populates the ComboBox meant for the country
     *  in the SponsorMaintenanceForm.
     *  <li>To fetch the data, it uses the procedure dw_get_country_code.
     *
     *  @return vector list of all countries with country code as key
     *  and country name as value.
     *  @exception DBException if any error during database transaction.
     *  @exception CoeusException if the instance of dbEngine is not available.
     */
    public Vector getCountries() throws CoeusException, DBException{
        Vector result = new Vector(3,2);
        Vector param= new java.util.Vector();
        Vector countries = new Vector();
        if(dbEngine!=null){
            result = dbEngine.executeRequest("Coeus",
            "call dw_get_country_code  ( <<OUT RESULTSET rset>> )",
            "Coeus", param);
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }
        for(int i=0;i<result.size();i++){
            HashMap countryRow = (HashMap)result.elementAt(i);
            countries.addElement(new ComboBoxBean(
            countryRow.get("COUNTRY_CODE").toString(),
            countryRow.get("COUNTRY_NAME").toString()));
        }
        return countries;
    }

    /**
     *  Method used to fetch the Timestamp from the database.
     *  Returns a current timestamp from the database.
     *  <li>To fetch the data, it uses dw_get_cur_sysdate procedure.
     *
     *  @return Timestamp current timestamp from the database
     *  @exception DBException if any error during database transaction.
     *  @exception CoeusException if the instance of dbEngine is not available.
     */
    public Timestamp getTimestamp() throws CoeusException, DBException{
        Timestamp timeStamp = null;
        Vector parameters= new Vector();
        Vector resultTimeStamp = new Vector();
        /* calling stored function */
        if(dbEngine!=null){
            resultTimeStamp = dbEngine.executeFunctions("Coeus",
            " call dw_get_cur_sysdate( <<OUT RESULTSET rset>> )", parameters);
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }
        if(!resultTimeStamp.isEmpty()){
            HashMap htTimeStampRow = (HashMap)resultTimeStamp.elementAt(0);
            timeStamp = (Timestamp)htTimeStampRow.get("SYSDATE");
        }
        return timeStamp;
    }

    /**
     * This method deletes the sponsor details in the sponsor and rolodex table.
     *  <li>To fetch the data, it uses delete_sponsor,delete_rolodex procedure.
     *
     *  @param sponsorCode String
     *  @param isModify to check for modify
     *  @return boolean on the successful delete.
     *  @exception DBException if any error during database transaction.
     *  @exception CoeusException if the instance of dbEngine is not available.
     */
    public boolean deleteSponsorMaintenance(String sponsorCode,boolean isModify)
    throws CoeusException, DBException{
        Vector paramSponsor= new Vector();
        Vector paramRolox= new Vector();
        Vector procedures = new Vector(5,3);
        SponsorMaintenanceFormBean spBean =
            getSponsorMaintenanceDetails(sponsorCode);
        String rolodexID = spBean.getRolodexID();
        paramRolox.add(new Parameter("ROLODEXID",
        DBEngineConstants.TYPE_INT,rolodexID));
        paramSponsor.add(new Parameter("SPONSORCODE",
        DBEngineConstants.TYPE_STRING,sponsorCode));

        StringBuffer strqryRx = new StringBuffer(
        "call DELETE_ROLODEX (<< ROLODEXID >> )");
        StringBuffer strqrySp = new StringBuffer(
        "call DELETE_SPONSOR (<< SPONSORCODE >> )");

        ProcReqParameter procReqParamSp  = new ProcReqParameter();
        procReqParamSp.setDSN("Coeus");
        procReqParamSp.setParameterInfo(paramSponsor);
        procReqParamSp.setSqlCommand(strqrySp.toString());

        ProcReqParameter procReqParamRx  = new ProcReqParameter();
        procReqParamRx.setDSN("Coeus");
        procReqParamRx.setParameterInfo(paramRolox);
        procReqParamRx.setSqlCommand(strqryRx.toString());

        /* Add the procedure Requirement parameter to the Vector */
        procedures.addElement(procReqParamSp);
        procedures.addElement(procReqParamRx);

        if(dbEngine!=null){
            dbEngine.executeStoreProcs(procedures);
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }

        return true;
    }

    /**
     * overridden method to implement rowlocking by using Transaction monitor
     *
     * @param sponsorMaintBean contains sponsor details for modify\insert
     * @param rolodexDetailsBean contains rolodex details
     * @param functionType check for modify
     * @return boolean on the successful Update or Add.
     * @exception DBException if any error during database transaction.
     * @exception CoeusException if the instance of dbEngine is not available.
     */
    public boolean addUpdateSponsorMaintenance(
            SponsorMaintenanceFormBean sponsorMaintBean,
            RolodexDetailsBean rolodexDetailsBean,char functionType)
                throws CoeusException, DBException{
        boolean success=
            addUpdateSponsorMaintenance(sponsorMaintBean, rolodexDetailsBean);
        if((success)&&(functionType=='U')){
              String rowId = rowLockStr + sponsorMaintBean.getSponsorCode();
              transMon.releaseEdit(rowId); 
              return true;
        }
        return success;
    }
    
    // Code added by Shivakumar for locking enhancement -BEGIN
//    public boolean addUpdateSponsorMaintenance(
//            SponsorMaintenanceFormBean sponsorMaintBean,
//            RolodexDetailsBean rolodexDetailsBean,String loggedinUser,char functionType)
//                throws CoeusException, DBException{
//        boolean success=
//            addUpdateSponsorMaintenance(sponsorMaintBean, rolodexDetailsBean);
//        if(dbEngine!=null){
//                  conn = dbEngine.beginTxn();
//              }else{
//                    throw new CoeusException("db_exceptionCode.1000");
//              } 
//              LockingBean lockingBean = new LockingBean();
//              String rowId1 = rowLockStr + sponsorMaintBean.getSponsorCode();
//              lockingBean = transMon.newLock(rowId1, loggedinUser, "000001", conn);              
//              boolean gotLock = lockingBean.isGotLock();
//              if(gotLock){
//                  transactionCommit();
//              }    
//        if((success)&&(functionType=='U')){
//              String rowId = rowLockStr + sponsorMaintBean.getSponsorCode();
//              // Commented by Shivakumar for locking enhancement
////              transMon.releaseEdit(rowId);
//              // Code added by Shivakumar - BEGIN 1
//              transMon.releaseEdit(rowId,loggedinUser);
//              // Code added by Shivakumar - END 1
//              return true;
//        }
//        return success;
//    }  
    
    // Creating new addUpdateSponsorMaintenance method for fixing bug in locking
    
    public LockingBean addUpdateSponsorMaintenance(
            SponsorMaintenanceFormBean sponsorMaintBean,
            RolodexDetailsBean rolodexDetailsBean,String loggedinUser,char functionType)
                throws CoeusException, DBException{
        boolean success=
            addUpdateSponsorMaintenance(sponsorMaintBean, rolodexDetailsBean);
        LockingBean releaseLockingBean = new LockingBean();
        
        
        if(dbEngine!=null){
                  conn = dbEngine.beginTxn();
              }else{
                    throw new CoeusException("db_exceptionCode.1000");
              } 
              LockingBean lockingBean = new LockingBean();
              String rowId1 = rowLockStr + sponsorMaintBean.getSponsorCode();
              lockingBean = transMon.newLock(rowId1, loggedinUser, "000001", conn);              
              boolean gotLock = lockingBean.isGotLock();
              if(gotLock){
                  transactionCommit();
              }    
              
        if((success)&&(functionType=='U')){
              String rowId = rowLockStr + sponsorMaintBean.getSponsorCode();              
              releaseLockingBean = transMon.releaseLock(rowId,loggedinUser);              
              //return true;
        }
        return releaseLockingBean;
    }  
    
    public boolean lockCheck(String sponsorCode, String loggedinUser)
        throws CoeusException, DBException{        
        String rowId = this.rowLockStr+sponsorCode;
        boolean lockCheck = transMon.lockAvailabilityCheck(rowId,loggedinUser);
        return lockCheck;
     }
    
    
    // Code added by Shivakumar for locking enhancement -END
    /**
     * Method used to add and update the details of SponsorMaintenance details,
     * for the rolodex.
     * <li>To fetch the data, it uses update_sponsor,update_rolodex  procedure.
     *
     * @param sponsorMaintBean contains sponsor details for modify\insert
     * @param rolodexDetailsBean contains rolodex details
     * @return boolean on the successful Update or Add.
     * @exception DBException if any error during database transaction.
     * @exception CoeusException if the instance of dbEngine is not available.
     */
    public boolean addUpdateSponsorMaintenance(
            SponsorMaintenanceFormBean sponsorMaintBean,
            RolodexDetailsBean rolodexDetailsBean) throws CoeusException, DBException{
        Timestamp dbTimestamp = (new CoeusFunctions()).getDBTimestamp();                     
        /* keep the parameters for the stored procedure in a vector */
        Vector paramSponsor= new Vector();
        Vector paramRolox= new Vector();
        /* add the organization id parameter into param vector */
        Vector procedures = new Vector(5,3);
        //Added on 14 May, 2004 - start        
        if(sponsorMaintBean.getAcType().equalsIgnoreCase("U")){
            if("0".equals(sponsorMaintBean.getRolodexID())){
                RolodexMaintenanceDataTxnBean rolodexMaintenanceDataTxnBean = new RolodexMaintenanceDataTxnBean();
                rolodexDetailsBean.setRolodexId(rolodexMaintenanceDataTxnBean.getNextSeqNum());
                sponsorMaintBean.setRolodexID(rolodexDetailsBean.getRolodexId());
                rolodexDetailsBean.setAcType("I");
            }
        }
        //Added on 14 May, 2004 - end
        paramRolox.addElement(new Parameter("ROLODEXID",
        DBEngineConstants.TYPE_INT,
        rolodexDetailsBean.getRolodexId()));
        paramRolox.addElement(new Parameter("LASTNAME",
        DBEngineConstants.TYPE_STRING,
        rolodexDetailsBean.getLastName()));
        paramRolox.addElement(new Parameter("FIRSTNAME",
        DBEngineConstants.TYPE_STRING,
        rolodexDetailsBean.getFirstName()));
        paramRolox.addElement(new Parameter("MIDDLENAME",
        DBEngineConstants.TYPE_STRING,
        rolodexDetailsBean.getMiddleName()));
        paramRolox.addElement(new Parameter("SUFFIX",
        DBEngineConstants.TYPE_STRING,
        rolodexDetailsBean.getSuffix()));
        paramRolox.addElement(new Parameter("PREFIX",
        DBEngineConstants.TYPE_STRING,
        rolodexDetailsBean.getPrefix()));
        paramRolox.addElement(new Parameter("TITLE",
        DBEngineConstants.TYPE_STRING,
        rolodexDetailsBean.getTitle()));
        paramRolox.addElement(new Parameter("ORGANIZATION",
        DBEngineConstants.TYPE_STRING,
        rolodexDetailsBean.getOrganization().trim()));
        //Added for COEUSQA-1434 : Add the functionality to set a status on a Sponsor record - start
        // Added this parameter for status which was missed for the COEUSQA-1528 Add the functionality to set a status on a Rolodex entry 
        paramRolox.addElement(new Parameter("ROLODEX_STATUS",
        DBEngineConstants.TYPE_STRING,
        rolodexDetailsBean.getStatus() == null ? "A":rolodexDetailsBean.getStatus().trim()));        
        //Added for COEUSQA-1434 : Add the functionality to set a status on a Sponsor record - end
        paramRolox.addElement(new Parameter("ADDRESS1",
        DBEngineConstants.TYPE_STRING,
        rolodexDetailsBean.getAddress1().trim()));
        paramRolox.addElement(new Parameter("ADDRESS2",
        DBEngineConstants.TYPE_STRING,
        rolodexDetailsBean.getAddress2().trim()));
        paramRolox.addElement(new Parameter("ADDRESS3",
        DBEngineConstants.TYPE_STRING,
        rolodexDetailsBean.getAddress3().trim()));
        paramRolox.addElement(new Parameter("FAX",
        DBEngineConstants.TYPE_STRING,
        rolodexDetailsBean.getFax().trim()));
        paramRolox.addElement(new Parameter("CITY",
        DBEngineConstants.TYPE_STRING,
        rolodexDetailsBean.getCity().trim()));
        paramRolox.addElement(new Parameter("COUNTY",
        DBEngineConstants.TYPE_STRING,
        rolodexDetailsBean.getCounty().trim()));
        paramRolox.addElement(new Parameter("STATE",
        DBEngineConstants.TYPE_STRING,
        rolodexDetailsBean.getState().trim()));
        paramRolox.addElement(new Parameter("POSTALCODE",
        DBEngineConstants.TYPE_STRING,
        rolodexDetailsBean.getPostalCode().trim()));
        paramRolox.addElement(new Parameter("PHONE",
        DBEngineConstants.TYPE_STRING,
        rolodexDetailsBean.getPhone().trim()));
        paramRolox.addElement(new Parameter("EMAIL",
        DBEngineConstants.TYPE_STRING,
        rolodexDetailsBean.getEMail().trim()));
        //Modified by shiji for bug fix id :1843 : step 1 - start
        paramRolox.addElement(new Parameter("COUNTRY",
        DBEngineConstants.TYPE_STRING,
        rolodexDetailsBean.getCountry().trim()));
        //bug id : 1843 : step 1 - end
        paramRolox.addElement(new Parameter("COMMENTS",
        DBEngineConstants.TYPE_STRING,
        rolodexDetailsBean.getComments()));
        paramRolox.addElement(new Parameter("SPONSORCODE",
        DBEngineConstants.TYPE_STRING,
        sponsorMaintBean.getSponsorCode().trim()));
        paramRolox.addElement(new Parameter("DELETE_FLAG",
        DBEngineConstants.TYPE_STRING,
        rolodexDetailsBean.getDeleteFlag()));
        paramRolox.addElement(new Parameter("SPONSOR_ADDRESS_FLAG",
        DBEngineConstants.TYPE_STRING,
        rolodexDetailsBean.getSponsorAddressFlag()));
        paramRolox.addElement(new Parameter("OWNED_BY_UNIT",
        DBEngineConstants.TYPE_STRING,
        rolodexDetailsBean.getOwnedByUnit()));
        paramRolox.addElement(new Parameter("UPDATE_USER",
        DBEngineConstants.TYPE_STRING,
        userId));        
        paramRolox.addElement(new Parameter("UPDATE_TIMESTAMP",
        DBEngineConstants.TYPE_TIMESTAMP,
        dbTimestamp));                
        paramRolox.addElement(new Parameter("ORGIN_ROLODEXID",
        DBEngineConstants.TYPE_INT,
        rolodexDetailsBean.getRolodexId()));
        paramRolox.addElement(new Parameter("LASTUPDATEUSER",
        DBEngineConstants.TYPE_STRING,
        rolodexDetailsBean.getLastUpdateUser()));
        paramRolox.addElement(new Parameter("LASTUPDATETIME",
        DBEngineConstants.TYPE_TIMESTAMP,
        rolodexDetailsBean.getLastUpdateTime()));
        paramRolox.addElement(new Parameter("AC_TYPE",
        DBEngineConstants.TYPE_STRING,
        rolodexDetailsBean.getAcType()));
        StringBuffer strqryRx = new StringBuffer("call UPD_ROLODEX ( ");
        strqryRx.append(" << ROLODEXID >> ,");
        strqryRx.append(" << LASTNAME >> ,");
        strqryRx.append(" << FIRSTNAME >> ,");
        strqryRx.append(" << MIDDLENAME >> ,");
        strqryRx.append(" << SUFFIX >> ,");
        strqryRx.append(" << PREFIX >> ,");
        strqryRx.append(" << TITLE >> ,");
        strqryRx.append(" << ORGANIZATION >> ,");
        //Added for COEUSQA-1434 : Add the functionality to set a status on a Sponsor record - start
        // Added this parameter for status which was missed for the COEUSQA-1528 Add the functionality to set a status on a Rolodex entry 
        strqryRx.append(" << ROLODEX_STATUS >> ,");
        //Added for COEUSQA-1434 : Add the functionality to set a status on a Sponsor record - end
        strqryRx.append(" << ADDRESS1 >> ,");
        strqryRx.append(" << ADDRESS2 >> ,");
        strqryRx.append(" << ADDRESS3 >> ,");
        strqryRx.append(" << FAX >> ,");
        strqryRx.append(" << CITY >> ,");
        strqryRx.append(" << COUNTY >> ,");
        strqryRx.append(" << STATE >> ,");
        strqryRx.append(" << POSTALCODE >> ,");
        strqryRx.append(" << PHONE >> ,");
        strqryRx.append(" << EMAIL >> ,");
        strqryRx.append(" << COUNTRY >> ,");
        strqryRx.append(" << COMMENTS >> ,");
        strqryRx.append(" << SPONSORCODE >> ,");
        strqryRx.append(" << DELETE_FLAG >> ,");
        strqryRx.append(" << SPONSOR_ADDRESS_FLAG >> ,");
        strqryRx.append(" << OWNED_BY_UNIT >> ,");
        strqryRx.append(" << UPDATE_USER >> ,");
        strqryRx.append(" << UPDATE_TIMESTAMP >> ,");        
        strqryRx.append(" << ORGIN_ROLODEXID >> ,");        
        strqryRx.append(" << LASTUPDATEUSER >> ,");
        strqryRx.append(" << LASTUPDATETIME >> ,");
        strqryRx.append(" << AC_TYPE >> )");

        ProcReqParameter procReqParamRx  = new ProcReqParameter();
        procReqParamRx.setDSN("Coeus");
        procReqParamRx.setParameterInfo(paramRolox);
        procReqParamRx.setSqlCommand(strqryRx.toString());

        /* This for Sponsor details to be added */
        String refId = (sponsorMaintBean.getRefId() == null ? null :
            (sponsorMaintBean.getRefId().equalsIgnoreCase("")? null :
                sponsorMaintBean.getRefId()));
                paramSponsor.addElement(new Parameter("SPONSOR_CODE",
                DBEngineConstants.TYPE_STRING,
                sponsorMaintBean.getSponsorCode().trim()));
                paramSponsor.addElement(new Parameter("SPONSOR_NAME",
                DBEngineConstants.TYPE_STRING,
                sponsorMaintBean.getName().trim()));
                paramSponsor.addElement(new Parameter("ACRONYM",
                DBEngineConstants.TYPE_STRING,
                sponsorMaintBean.getAcronym().trim()));
                paramSponsor.addElement(new Parameter("SPONSOR_TYPE_CODE",
                DBEngineConstants.TYPE_INT,
                sponsorMaintBean.getType().trim()));              
                //Added for COEUSQA-1434 : Add the functionality to set a status on a Sponsor record - start
                paramSponsor.addElement(new Parameter("STATUS",
                DBEngineConstants.TYPE_STRING,
                sponsorMaintBean.getStatus().trim()));
                //Added for COEUSQA-1434 : Add the functionality to set a status on a Sponsor record - end       
                //VISUAL COMPLIANCE
                paramSponsor.addElement(new Parameter("IS_VISUAL_COMPLAINCE",
                DBEngineConstants.TYPE_STRING,
                sponsorMaintBean.getVisualCompliance().trim()));
                
                paramSponsor.addElement(new Parameter("VISUAL_COMPLIANCE_EXPLANATION",
                DBEngineConstants.TYPE_STRING,
                sponsorMaintBean.getVisualComplianceExpl().trim()));
                //VISUAL COMPLIANCE
                paramSponsor.addElement(new Parameter("DUN_AND_BRADSTREET_NUMBER",
                DBEngineConstants.TYPE_STRING,
                sponsorMaintBean.getDuns().trim()));
                paramSponsor.addElement(new Parameter("DUNS_PLUS_FOUR_NUMBER",
                DBEngineConstants.TYPE_STRING,
                sponsorMaintBean.getDuns4().trim()));
                paramSponsor.addElement(new Parameter("DODAC_NUMBER",
                DBEngineConstants.TYPE_STRING,
                sponsorMaintBean.getDodc().trim()));
                paramSponsor.addElement(new Parameter("CAGE_NUMBER",
                DBEngineConstants.TYPE_STRING,
                sponsorMaintBean.getCage().trim()));
                paramSponsor.addElement(new Parameter("POSTALCODE",
                DBEngineConstants.TYPE_STRING,
                sponsorMaintBean.getPostalCode()));
                paramSponsor.addElement(new Parameter("STATE",
                DBEngineConstants.TYPE_STRING,
                sponsorMaintBean.getState().trim()));
                paramSponsor.addElement(new Parameter("COUNTRY_CODE",
                DBEngineConstants.TYPE_STRING,
                sponsorMaintBean.getCountry().trim()));
                paramSponsor.addElement(new Parameter("ROlODEX_ID",
                DBEngineConstants.TYPE_INT,
                rolodexDetailsBean.getRolodexId()));
                paramSponsor.addElement(new Parameter("AUDIT_REPORT_SENT_FOR_FY",
                DBEngineConstants.TYPE_STRING,
                sponsorMaintBean.getAuditReport().trim()));
                paramSponsor.addElement(new Parameter("OWNED_BY_UNIT",
                DBEngineConstants.TYPE_STRING,
                sponsorMaintBean.getOwnedBy().trim()));
                paramSponsor.addElement(new Parameter("UPDATE_TIMESTAMP",
                DBEngineConstants.TYPE_TIMESTAMP,
                dbTimestamp));
                paramSponsor.addElement(new Parameter("UPDATE_USER",
                DBEngineConstants.TYPE_STRING,
                userId));                
                paramSponsor.addElement(new Parameter("ORIG_SPONSOR_CODE",
                DBEngineConstants.TYPE_STRING,
                sponsorMaintBean.getSponsorCode()));
                paramSponsor.addElement(new Parameter("LASTUPDATETIME",
                DBEngineConstants.TYPE_TIMESTAMP,
                sponsorMaintBean.getLastUpdateTime()));
                paramSponsor.addElement(new Parameter("LASTUPDATEUSER",
                DBEngineConstants.TYPE_STRING,
                sponsorMaintBean.getLastUpdateUser()));
                paramSponsor.addElement(new Parameter("AC_TYPE",
                DBEngineConstants.TYPE_STRING,sponsorMaintBean.getAcType()));

                StringBuffer strqrySp = new StringBuffer("call UPD_SPONSOR ( ");
                strqrySp.append(" << SPONSOR_CODE >> ,");
                strqrySp.append(" << SPONSOR_NAME >> ,");
                strqrySp.append(" << ACRONYM >> ,");
                strqrySp.append(" << SPONSOR_TYPE_CODE >> ,");
                //Added for COEUSQA-1434 : Add the functionality to set a status on a Sponsor record - start
                strqrySp.append("<< STATUS >> ,");
                //Added for COEUSQA-1434 : Add the functionality to set a status on a Sponsor record - end
                strqrySp.append(" << DUN_AND_BRADSTREET_NUMBER >> ,");
                strqrySp.append(" << DUNS_PLUS_FOUR_NUMBER >> ,");
                strqrySp.append(" << DODAC_NUMBER >> ,");
                strqrySp.append(" << CAGE_NUMBER >> ,");
                strqrySp.append(" << POSTALCODE >> ,");
                strqrySp.append(" << STATE >> ,");
                strqrySp.append(" << COUNTRY_CODE >> ,");
                strqrySp.append(" << ROlODEX_ID >> ,");
                strqrySp.append(" << AUDIT_REPORT_SENT_FOR_FY >> ,");
                strqrySp.append(" << OWNED_BY_UNIT >> ,");                
                strqrySp.append(" << UPDATE_TIMESTAMP >> ,");
                strqrySp.append(" << UPDATE_USER >> ,");                
                strqrySp.append(" << ORIG_SPONSOR_CODE >> ,");
                strqrySp.append(" << LASTUPDATETIME >> ,");
                strqrySp.append(" << LASTUPDATEUSER >> ,");               
                strqrySp.append(" << AC_TYPE >> ,");
                  //VISUAL COMPLAINCE CHECKED
                strqrySp.append("<< IS_VISUAL_COMPLAINCE >> ,");
                strqrySp.append("<< VISUAL_COMPLIANCE_EXPLANATION >> )");
                //VISUAL COMPLAINCE CHECKED
                ProcReqParameter procReqParamSp  = new ProcReqParameter();
                procReqParamSp.setDSN("Coeus");
                procReqParamSp.setParameterInfo(paramSponsor);
                procReqParamSp.setSqlCommand(strqrySp.toString());

                /* Added both procedure to the vector */
                procedures.addElement(procReqParamRx);
                procedures.addElement(procReqParamSp);


        if (dbEngine != null){
            dbEngine.executeStoreProcs(procedures);
        }else {
            throw new CoeusException("db_exceptionCode.1000");
        }

                return true;
    }

    /**
     *  This method is used check the rights to delete or modify the the
     *  sponsor details .
     *  <li>To fetch the data, it uses fn_user_has_right procedure.
     *
     *  @param user String
     *  @param unitNumber String
     *  @param sponsorRight String
     *  @return boolean true if user has rights
     *  @exception DBException if any error during database transaction.
     *  @exception CoeusException if the instance of dbEngine is not available.
     */
    public boolean isUserHasRight(String user,String unitNumber,
    String sponsorRight) throws CoeusException, DBException {
        int userRight = 0;
        boolean hasRight =false;
        Vector param= new Vector();
        Vector result = new Vector();
        param.add(new Parameter("USER",DBEngineConstants.TYPE_STRING,user));
        param.add(new Parameter("UNITNUMBER",
        DBEngineConstants.TYPE_STRING,unitNumber));
        param.add(new Parameter("SPONSORRIGHT",
        DBEngineConstants.TYPE_STRING,sponsorRight));
        /* calling stored function */
        
        /* JM 9-18-2015 changed right checking procedure call */
        if(dbEngine!=null){
            //result = dbEngine.executeFunctions("Coeus",
            //"{ <<OUT INTEGER RIGHTEXISTS>> = call fn_user_has_right(<< USER >>,"+
            //"<< UNITNUMBER >> , << SPONSORRIGHT >> ) }", param);
            result = dbEngine.executeFunctions("Coeus",
            "{ <<OUT INTEGER RIGHTEXISTS>> = call fn_user_has_right_in_any_unit(<< USER >>,"+
            "<< SPONSORRIGHT >> ) }", param);
        /* JM END */
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }
        if(!result.isEmpty()){
            HashMap rowRolodexId = (HashMap)result.elementAt(0);
            userRight = Integer.parseInt(
            rowRolodexId.get("RIGHTEXISTS").toString());
        }
        if ( userRight == 1 )  {
            hasRight = true;
        }else if ( userRight == 0 )    {
            hasRight = false;
        }
        
        return hasRight;
    }


    /**
     *  This method is used to check Sponsor is used by other user before
     *  delete or modify if it used it will stop user to do so.
     *  <li>To fetch the data, it uses check_sponsor_is_used procedure.
     *
     *  @param sponsorId String
     *  @param userName String
     *  @param maintainFlag integer
     *  @param deleteFlag integer
     *  @return resultData Vector
     *  @exception DBException if any error during database transaction.
     *  @exception CoeusException if the instance of dbEngine is not available.
     */
    public Vector checkSponsorIsUsed(String sponsorId,String userName,
    int maintainFlag,int deleteFlag) throws CoeusException, DBException {
        Vector resultData = new Vector();
        Vector param= new Vector();
        Vector result = new Vector();
        String strMaintainFlag = (new Integer(maintainFlag)).toString();
        String strDeleteFlag = (new Integer(deleteFlag)).toString();
        String okFlag = "";
        String proposal = "";
        String rolodex = "";
        param.add(new Parameter("SPONSORCODE",
        DBEngineConstants.TYPE_STRING,sponsorId.trim()));
        param.add(new Parameter("USERNAME",
        DBEngineConstants.TYPE_STRING,userName.toUpperCase()));
        param.add(new Parameter("MAINTAINFLAG",
        DBEngineConstants.TYPE_INT,strMaintainFlag));
        param.add(new Parameter("DELETEFLAG",
        DBEngineConstants.TYPE_INT,strDeleteFlag));

        /* calling stored function */
        if(dbEngine!=null){
            result = dbEngine.executeRequest("Coeus",
            "call check_sponsor_is_used( << SPONSORCODE >> , << USERNAME >> , "+
            "<< MAINTAINFLAG >> , << DELETEFLAG >>,<< OUT INTEGER PROPOSAL >>,"+
            "<< OUT INTEGER ROLODEX >> , << OUT INTEGER OKFLAG >> )",
            "Coeus", param);
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }
        if(!result.isEmpty()){
            HashMap rowSponsor = (HashMap)result.elementAt(0);
            okFlag = rowSponsor.get("OKFLAG").toString();
            proposal = rowSponsor.get("PROPOSAL").toString();
            rolodex = rowSponsor.get("ROLODEX").toString();
        }
        resultData.add(proposal);
        resultData.add(rolodex);
        resultData.add(okFlag);

        return resultData;
    }

    /**
     *  Method to check whether the record is being opened by another user
     *
     *  @param sponsorCode String
     *  @return boolean status true can delete
     *  @exception DBException if any error during database transaction.
     *  @exception CoeusException if the instance of dbEngine is not available.
     */
    public boolean canDeleteSponsor(String sponsorCode)
            throws CoeusException, DBException{
        String rowId = rowLockStr + sponsorCode;
        if(!transMon.canDelete(rowId)){
            throw new CoeusException("Delete_exceptionCode.444444");
        }
        return true;
    }
    
    /**
     *  overridden method to implement rowlocking by using Transaction monitor
     *
     * @param sponsorID String
     * @param functionType to check for modify
     * @return vector which contains sponsor and rolodex beans.
     * @exception DBException if any error during database transaction.
     * @exception CoeusException if the instance of dbEngine is not available.
     */
    public Vector getSponsorRolodexDetails(String sponsorID,char functionType)
            throws CoeusException, DBException{
        if(functionType=='M'){
            String rowId = rowLockStr+sponsorID.trim();
            // Commented by Shivakumar for locking implementation
            //if(transMon.canEdit(rowId))
                return getSponsorRolodexDetails(sponsorID);
            //else
             //   throw new CoeusException("exceptionCode.999999");
        }else{
            return getSponsorRolodexDetails(sponsorID);
        }
    }
    
    // Code added by Shivakumar for locking enhancement -BEGIN
    
    public LockingBean getSponsorRolodexDetails(String sponsorID,char functionType,String loggedinUser,String unitNumber)
            throws CoeusException, DBException{
//            if(dbEngine!=null){
//                  conn = dbEngine.beginTxn();
//            }else{
//                throw new CoeusException("db_exceptionCode.1000");
//            } 
              String rowId = rowLockStr+sponsorID.trim();      
              LockingBean lockingBean = transMon.canEdit(rowId,loggedinUser,unitNumber);              
              return lockingBean;
    }    
    
    // Method to commit transaction
        public void transactionCommit() throws DBException{            
            dbEngine.commit(conn);
        }    
        
        // Method to rollback transaction
        public void transactionRollback() throws DBException{
            dbEngine.rollback(conn);
        }    
        
        // Method to close the connection
        public void endConnection() throws DBException{
            dbEngine.endTxn(conn);
        }    
    
    // Code added by Shivakumar for locking enhancement END
    
    /**
     * This methods returns the Vector contains both the bean,
     * sponsor bean and rolodex bean
     *
     * @param sponsorID String
     * @return vector which contains sponsor and rolodex beans.
     * @exception DBException if any error during database transaction.
     * @exception CoeusException if the instance of dbEngine is not available.
     */
    public Vector getSponsorRolodexDetails(String sponsorID)
        throws CoeusException, DBException{
        Vector dataBeans = new Vector();
        SponsorMaintenanceFormBean sponsorBean = getSponsorMaintenanceDetails(sponsorID);
        /* get the rolodex id for this sponsor code */
        String rolodexId = sponsorBean.getRolodexID();
/*        RolodexDetailsBean rolodexBean =
        rldTxnBean.getRolodexDetails(rolodexId,false);
 */
        RolodexDetailsBean rolodexBean =rldTxnBean.getRolodexMaintenanceDetails(rolodexId);
        dataBeans.add(sponsorBean);
        dataBeans.add(rolodexBean);
        return dataBeans;
    }

    /**
     * Method used to get entire details of Sponsor details for the sponsor id.
     * <li>To fetch the data, it uses select_sponsor procedure.
     *
     * @param sponsorID String
     * @return Sponsor bean.
     * @exception DBException if any error during database transaction.
     * @exception CoeusException if the instance of dbEngine is not available.
     */
    public SponsorMaintenanceFormBean getSponsorMaintenanceDetails(String sponsorID)
            throws CoeusException, DBException{
        Vector results;
        Vector vecSponsor;
        results = new Vector();
        Vector vecResult = new Vector();
        vecSponsor = new Vector();
        Vector  param = new Vector();
        Vector procedures = new Vector(5,3);

        sponsorFormData = new SponsorMaintenanceFormBean();
        param.addElement(new Parameter("SPONSORCODE", DBEngineConstants.TYPE_STRING,sponsorID));

        StringBuffer strqry = new StringBuffer("call GET_SPONSOR ( ");
        strqry.append(" << SPONSORCODE >> ,");
        strqry.append("<< OUT RESULTSET rset >> )");

        if (dbEngine != null) {
            vecResult = dbEngine.executeRequest("Coeus",strqry.toString(),"Coeus", param);
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }
        if (!vecResult.isEmpty()) {
            HashMap sponsorRow = (HashMap)vecResult.elementAt(0);
            sponsorFormData.setRolodexID( sponsorRow.get("ROLODEX_ID").toString());
            sponsorFormData.setSponsorCode((String)sponsorRow.get("SPONSOR_CODE"));
            sponsorFormData.setName( (String)sponsorRow.get("SPONSOR_NAME"));
            sponsorFormData.setTypes(getSponsorTypes());
            sponsorFormData.setType( sponsorRow.get("SPONSOR_TYPE_CODE").toString());
            //Added for COEUSQA-1434 : Add the functionality to set a status on a Sponsor record - start
            if(sponsorRow.get("STATUS")!=null)
                sponsorFormData.setStatus((String)sponsorRow.get("STATUS"));
            //Added for COEUSQA-1434 : Add the functionality to set a status on a Sponsor record - end
            //Added for COEUSQA-1434 : Add the functionality to set a status on a Sponsor record - start
            if(sponsorRow.get("IS_VISUAL_COMPLIANCE")!=null)
                sponsorFormData.setVisualCompliance((String)sponsorRow.get("IS_VISUAL_COMPLIANCE"));
            
            sponsorFormData.setVisualComplianceExpl((String)sponsorRow.get("VISUAL_COMPLIANCE_EXPLANATION"));
            //
            sponsorFormData.setTypeDescription((String)sponsorRow.get("SPONSOR_TYPE_DESC"));
            sponsorFormData.setAcronym((String)sponsorRow.get("ACRONYM"));
            sponsorFormData.setDuns( (String)
                            sponsorRow.get("DUN_AND_BRADSTREET_NUMBER"));
            sponsorFormData.setDuns4( (String)
                        sponsorRow.get("DUNS_PLUS_FOUR_NUMBER"));
            sponsorFormData.setAuditReport( (String)
                        sponsorRow.get("AUDIT_REPORT_SENT_FOR_FY"));
            sponsorFormData.setCage( (String)sponsorRow.get("CAGE_NUMBER"));
            sponsorFormData.setDodc( (String)sponsorRow.get("DODAC_NUMBER"));
            sponsorFormData.setOwnedBy( (String)sponsorRow.get("OWNED_BY_UNIT"));
            sponsorFormData.setState( (String)sponsorRow.get("STATE"));
            sponsorFormData.setStateDescription((String)sponsorRow.get("STATE_DESCRIPTION"));
            //Modified by shiji for bug fix id : 1843 : step 2 - start
            sponsorFormData.setCountry(sponsorRow.get("COUNTRY_CODE")==null ?" " :(String)sponsorRow.get("COUNTRY_CODE"));
            sponsorFormData.setCountryName(sponsorRow.get("COUNTRY_NAME")== null? " " :(String)sponsorRow.get("COUNTRY_NAME"));
            //bug id : 1843 : step 2 - end
            sponsorFormData.setStates(getStates());
            sponsorFormData.setCountries(getCountries());
            sponsorFormData.setPostalCode( (String)sponsorRow.get("POSTAL_CODE"));
            sponsorFormData.setCreateUser( (String)sponsorRow.get("CREATE_USER"));
            sponsorFormData.setLastUpdateTime(
            (Timestamp)sponsorRow.get("UPDATE_TIMESTAMP"));
            sponsorFormData.setLastUpdateUser((String)sponsorRow.get("UPDATE_USER"));
            sponsorFormData.setCreateUser( (String)sponsorRow.get("CREATE_USER"));
        }
        return sponsorFormData;
    }
    
    /**
     *  The method used to release the lock of a particular sponsor
     *  @param rowId the id for lock to be released
     */
    public void releaseEdit(String rowId){
        transMon.releaseEdit(this.rowLockStr+rowId);
    }
    public void releaseEdit(String rowId,String loggedinUser)
         throws CoeusException,DBException{
        transMon.releaseEdit(this.rowLockStr+rowId,loggedinUser);
    }
    // Calling releaseLock method for bug fixing in locking
    public LockingBean releaseLock(String rowId,String loggedinUser)
         throws CoeusException,DBException{
        LockingBean lockingBean = transMon.releaseLock(this.rowLockStr+rowId,loggedinUser);
        return lockingBean;
    }
    // Code added by Shivakumar for Sponsor Forms Maintenance in Coeus 4.0 - BEGIN    
    public CoeusVector getSponsorForms(String sponsorCode)
        throws CoeusException, DBException{
        Vector param= new Vector();
        Vector result = new Vector();
        HashMap invRow=null;
        param.addElement(new Parameter("SPONSOR_CODE",
            DBEngineConstants.TYPE_STRING, sponsorCode)); 
        if(dbEngine !=null){
            result=new Vector(3,2);
            result=dbEngine.executeRequest("Coeus",
              "call GET_SPONSOR_FORMS(<< SPONSOR_CODE >>,<< OUT RESULTSET rset >>)", "Coeus", param);
        }else{
            throw new CoeusException("db_exceptionCode.1000");   
        }
        int listSize=result.size();           
        CoeusVector invList = new CoeusVector();        
        SponsorFormsBean sponsorFormsBean = null; 
        if(listSize>0){
            for(int index = 0; index < listSize; index++){    
                sponsorFormsBean = new SponsorFormsBean(); 
                invRow=(HashMap)result.elementAt(index);
                int rowId = index+1;
                sponsorFormsBean.setRowId(rowId);
                sponsorFormsBean.setSponsorCode((String)invRow.get("SPONSOR_CODE"));
                sponsorFormsBean.setPackageNumber(Integer.parseInt(invRow.get(PACKAGE_NUMBER) == null ? "1" :
                    invRow.get(PACKAGE_NUMBER).toString()));                
                sponsorFormsBean.setPackageName((String)invRow.get("PACKAGE_NAME"));
                sponsorFormsBean.setLastUpdateTime((Timestamp)invRow.get("UPDATE_TIMESTAMP"));
                sponsorFormsBean.setUpdateUser((String)invRow.get("UPDATE_USER"));
                invList.addElement(sponsorFormsBean);
            }
        }    
        return invList;
    }        
    
    public CoeusVector getSponsorFormTemplates(String sponsorCode)
        throws CoeusException, DBException{
        Vector param= new Vector();
        Vector result = new Vector();
        HashMap invRow=null;
        param.addElement(new Parameter("SPONSOR_CODE",
            DBEngineConstants.TYPE_STRING, sponsorCode)); 
        if(dbEngine !=null){
            result=new Vector(3,2);
            result=dbEngine.executeRequest("Coeus",
              "call GET_SPONSOR_FORM_TEMPLATES(<< SPONSOR_CODE >>,<< OUT RESULTSET rset >>)", "Coeus", param);
        }else{
            throw new CoeusException("db_exceptionCode.1000");   
        }
        int listSize=result.size();            
        CoeusVector invList = new CoeusVector();        
        SponsorTemplateBean sponsorTemplateBean = null; 
        if(listSize>0){
            for(int index = 0; index < listSize; index++){    
                sponsorTemplateBean = new SponsorTemplateBean(); 
                invRow=(HashMap)result.elementAt(index); 
                // Added by chandra - to get the sponsor code when the form is loaded-25th Aug
                sponsorTemplateBean.setSponsorCode((String)invRow.get("SPONSOR_CODE"));
                // end chandra - 25th Aug 2004
//                sponsorTemplateBean.setPackageNumber(Integer.parseInt(invRow.get(PACKAGE_NUMBER) == null ? "0" :
//                    invRow.get(PACKAGE_NUMBER).toString()));     
                int rowId = index+1;
                sponsorTemplateBean.setRowId(rowId);
                
                sponsorTemplateBean.setPackageNumber(Integer.parseInt(invRow.get(PACKAGE_NUMBER) == null ? "1" :
                  invRow.get(PACKAGE_NUMBER).toString()));  
                sponsorTemplateBean.setPageNumber(Integer.parseInt(invRow.get("PAGE_NUMBER") == null ? "1" :
                    invRow.get("PAGE_NUMBER").toString()));                    
                sponsorTemplateBean.setPageDescription((String)invRow.get("PAGE_DESCRIPTION"));    
//                sponsorTemplateBean.setFormTemplate((String)invRow.get("FORM_TEMPLATE"));
                sponsorTemplateBean.setLastUpdateTime((Timestamp)invRow.get("UPDATE_TIMESTAMP"));
                sponsorTemplateBean.setUpdateUser((String)invRow.get("UPDATE_USER"));
                /*
                 * Userid to Username enhancement - Start
                 * Added new property to get username
                 */
                if(invRow.get("UPDATE_USER_NAME") != null) {
                    sponsorTemplateBean.setUpdateUserName((String)invRow.get("UPDATE_USER_NAME"));
                } else {
                    sponsorTemplateBean.setUpdateUserName((String)invRow.get("UPDATE_USER"));
                }
                //Userid to Username enhancement - End
                invList.addElement(sponsorTemplateBean);
            }
        }    
        return invList;
    }
    
    public SponsorTemplateBean getPageCLOBData(String sponsorCode, int packageNumber, int pageNumber)
        throws CoeusException, DBException{
        Vector param= new Vector();
        Vector result = new Vector();
        HashMap invRow=null;
        param.addElement(new Parameter("SPONSOR_CODE",
            DBEngineConstants.TYPE_STRING, sponsorCode)); 
        param.addElement(new Parameter(PACKAGE_NUMBER,
            DBEngineConstants.TYPE_STRING,""+packageNumber)); 
        param.addElement(new Parameter("PAGE_NUMBER",
            DBEngineConstants.TYPE_STRING,""+pageNumber));         
        if(dbEngine !=null){
            result=new Vector(3,2);
            result=dbEngine.executeRequest("Coeus",
              "call GET_CLOB_PAGE_DATA(<< SPONSOR_CODE >>,<< PACKAGE_NUMBER >>, <<PAGE_NUMBER >>, << OUT RESULTSET rset >>)", "Coeus", param);
        }else{
            throw new CoeusException("db_exceptionCode.1000");   
        }           
        int listSize=result.size();            
        CoeusVector invList = new CoeusVector();        
        SponsorTemplateBean sponsorTemplateBean = null; 
        if(listSize>0){
            for(int index = 0; index < listSize; index++){    
                sponsorTemplateBean = new SponsorTemplateBean(); 
                invRow=(HashMap)result.elementAt(index);          
                sponsorTemplateBean.setSponsorCode((String)invRow.get("SPONSOR_CODE"));                
                sponsorTemplateBean.setPackageNumber(Integer.parseInt(invRow.get(PACKAGE_NUMBER) == null ? "1" :
                  invRow.get(PACKAGE_NUMBER).toString()));  
                sponsorTemplateBean.setPageNumber(Integer.parseInt(invRow.get("PAGE_NUMBER") == null ? "1" :
                    invRow.get("PAGE_NUMBER").toString()));                    
                sponsorTemplateBean.setPageDescription((String)invRow.get("PAGE_DESCRIPTION"));                 
                try{
                 
                    ByteArrayOutputStream byteArrayOutputStream;
                    // Commented by Shivakumar for BLOB implementation - BEGIN
//                    java.sql.Clob clobPage = (java.sql.Clob)invRow.get("FORM_TEMPLATE");
                    // Commented by Shivakumar for BLOB implementation - END
                    // Code added by Shivakumar for BLOB implementation - BEGIN
                    byteArrayOutputStream =(ByteArrayOutputStream)invRow.get("FORM_TEMPLATE");
                    // Code added by Shivakumar for BLOB implementation - END
                    // Commented by Shivakumar for BLOB implementation - BEGIN
//                    InputStream inputStream  = clobPage.getAsciiStream();
                    // Commented by Shivakumar for BLOB implementation - END
//                    InputStream inputStream  = byteStream.getBinaryStream();
                    // Commented by Shivakumar for BLOB implementation - BEGIN
//                    byte dataBytes[] = new byte[(int)clobPage.length()];
                    // Commented by Shivakumar for BLOB implementation - END
                    byte dataBytes[] = byteArrayOutputStream.toByteArray();
                    byteArrayOutputStream.close();                
                    
                    sponsorTemplateBean.setFormTemplate(dataBytes);
                    
                }catch(Exception ex){
                    ex.printStackTrace();
                    throw new CoeusException(ex.getMessage());
                }    
                sponsorTemplateBean.setLastUpdateTime((Timestamp)invRow.get("UPDATE_TIMESTAMP"));
                sponsorTemplateBean.setUpdateUser((String)invRow.get("UPDATE_USER"));
            }
        } 
        return sponsorTemplateBean;
    }
    
    
    
    
    public boolean updateSponsorForm(SponsorFormsBean sponsorFormsBean)
        throws CoeusException, DBException{
            
        boolean success = false;
        Vector param=null;
        Timestamp dbTimestamp = (new CoeusFunctions()).getDBTimestamp();
        param = new Vector();
        param.addElement(new Parameter("SPONSOR_CODE",
        DBEngineConstants.TYPE_STRING, sponsorFormsBean.getSponsorCode())); 
        param.addElement(new Parameter(PACKAGE_NUMBER,
        DBEngineConstants.TYPE_INT, ""+sponsorFormsBean.getPackageNumber())); 
        param.addElement(new Parameter("PACKAGE_NAME",
        DBEngineConstants.TYPE_STRING, sponsorFormsBean.getPackageName())); 
        param.addElement(new Parameter("UPDATE_USER",
        DBEngineConstants.TYPE_STRING, userId)); 
        param.addElement(new Parameter("UPDATE_TIMESTAMP",
        DBEngineConstants.TYPE_TIMESTAMP, dbTimestamp)); 
        param.addElement(new Parameter("AW_UPDATE_USER",
        DBEngineConstants.TYPE_STRING, sponsorFormsBean.getUpdateUser())); 
        param.addElement(new Parameter("AW_UPDATE_TIMESTAMP",
        DBEngineConstants.TYPE_TIMESTAMP, sponsorFormsBean.getLastUpdateTime()));
        param.addElement(new Parameter("AC_TYPE",
        DBEngineConstants.TYPE_STRING, sponsorFormsBean.getAcType())); 
        StringBuffer sql = new StringBuffer("call DW_UPD_SPONSOR_FORMS(");
        
        sql.append(" <<SPONSOR_CODE>> , ");
        sql.append(" <<PACKAGE_NUMBER>> , ");
        sql.append(" <<PACKAGE_NAME>> , ");
        sql.append(" <<UPDATE_USER>> , ");
        sql.append(" <<UPDATE_TIMESTAMP>> , ");
        sql.append(" <<AW_UPDATE_USER>> , ");
        sql.append(" <<AW_UPDATE_TIMESTAMP>> , ");
        sql.append(" <<AC_TYPE>> )");
        
        ProcReqParameter procReqParameter  = new ProcReqParameter();
        procReqParameter.setDSN(DSN);
        procReqParameter.setParameterInfo(param);
        procReqParameter.setSqlCommand(sql.toString());      
        Vector vctUpdateSponsorFormData = new Vector();
        
        vctUpdateSponsorFormData.addElement(procReqParameter);
        if(dbEngine!=null) {
           dbEngine.executeStoreProcs(vctUpdateSponsorFormData);            
        } else {
            throw new CoeusException("db_exceptionCode.1000");
        }      
        success = true;
        return success;
    }        
    
    
    
    public ProcReqParameter getSponsorForm(SponsorFormsBean sponsorFormsBean)
        throws CoeusException, DBException{
            
        boolean success = false;
        Vector param=null;
        Timestamp dbTimestamp = (new CoeusFunctions()).getDBTimestamp();
         
        param = new Vector();
        //sponsorFormsBean.setLastUpdateTime(dbTimestamp);
        param.addElement(new Parameter("SPONSOR_CODE",
        DBEngineConstants.TYPE_STRING, sponsorFormsBean.getSponsorCode())); 
        param.addElement(new Parameter(PACKAGE_NUMBER,
        DBEngineConstants.TYPE_INT, ""+sponsorFormsBean.getPackageNumber())); 
        param.addElement(new Parameter("PACKAGE_NAME",
        DBEngineConstants.TYPE_STRING, sponsorFormsBean.getPackageName())); 
        param.addElement(new Parameter("UPDATE_USER",
        DBEngineConstants.TYPE_STRING, userId)); 
        //Case#2445 - proposal development print forms linked to indiv sponsor, should link to sponsor hierarchy -Start
        param.addElement(new Parameter("GROUP_NAME",
        DBEngineConstants.TYPE_STRING, sponsorFormsBean.getGroupName())); 
        //Case#2445 - End
        param.addElement(new Parameter("UPDATE_TIMESTAMP",
        DBEngineConstants.TYPE_TIMESTAMP, dbTimestamp)); 
        param.addElement(new Parameter("AW_UPDATE_USER",
        DBEngineConstants.TYPE_STRING, sponsorFormsBean.getUpdateUser())); 
        param.addElement(new Parameter("AW_UPDATE_TIMESTAMP",
        DBEngineConstants.TYPE_TIMESTAMP, sponsorFormsBean.getLastUpdateTime()));
        param.addElement(new Parameter("AC_TYPE",
        DBEngineConstants.TYPE_STRING, sponsorFormsBean.getAcType()));         
        
        //Case#2445 - proposal development print forms linked to indiv sponsor, should link to sponsor hierarchy -Start
        //StringBuffer sql = new StringBuffer("call DW_UPD_SPONSOR_FORMS(");
        StringBuffer sql = new StringBuffer("call UPD_SPONSOR_FORMS(");
        //Case#2445 - End
        sql.append(" <<SPONSOR_CODE>> , ");
        sql.append(" <<PACKAGE_NUMBER>> , ");
        sql.append(" <<PACKAGE_NAME>> , ");
        sql.append(" <<UPDATE_USER>> , ");
        sql.append(" <<GROUP_NAME>> , ");
        sql.append(" <<UPDATE_TIMESTAMP>> , ");
        sql.append(" <<AW_UPDATE_USER>> , ");
        sql.append(" <<AW_UPDATE_TIMESTAMP>> , ");
        sql.append(" <<AC_TYPE>> )");
        
        ProcReqParameter procReqParameter  = new ProcReqParameter();
        procReqParameter.setDSN(DSN);
        procReqParameter.setParameterInfo(param);
        procReqParameter.setSqlCommand(sql.toString());      
        Vector vctUpdateSponsorFormData = new Vector();      

        return procReqParameter;
    } 
    
    
//    public boolean updateSponsorFormTemplates(SponsorFormsBean sponsorFormsBean)
//        throws CoeusException, DBException{
//            
//        boolean success = false;
//        Vector param=null;
//        Timestamp dbTimestamp = (new CoeusFunctions()).getDBTimestamp();
//        param = new Vector();
//        param.addElement(new Parameter("SPONSOR_CODE",
//        DBEngineConstants.TYPE_STRING, sponsorFormsBean.getSponsorCode())); 
//        param.addElement(new Parameter(PACKAGE_NUMBER,
//        DBEngineConstants.TYPE_INT, ""+sponsorFormsBean.getPackageNumber())); 
//        param.addElement(new Parameter("PAGE_NUMBER",
//        DBEngineConstants.TYPE_INT,""+sponsorFormsBean.getPageNumber())); 
//        param.addElement(new Parameter("PAGE_DESCRIPTION",
//        DBEngineConstants.TYPE_STRING,sponsorFormsBean.getPageDescription())); 
//        param.addElement(new Parameter("FORM_TEMPLATE",
//        DBEngineConstants.TYPE_STRING,sponsorFormsBean.getFormTemplate())); 
//        param.addElement(new Parameter("UPDATE_USER",
//        DBEngineConstants.TYPE_STRING, userId)); 
//        param.addElement(new Parameter("UPDATE_TIMESTAMP",
//        DBEngineConstants.TYPE_TIMESTAMP, dbTimestamp)); 
//        param.addElement(new Parameter("AW_UPDATE_USER",
//        DBEngineConstants.TYPE_STRING, sponsorFormsBean.getLastUpdateUser())); 
//        param.addElement(new Parameter("AW_UPDATE_TIMESTAMP",
//        DBEngineConstants.TYPE_TIMESTAMP, sponsorFormsBean.getLastUpdateTime()));
//        param.addElement(new Parameter("AC_TYPE",
//        DBEngineConstants.TYPE_STRING, sponsorFormsBean.getAcType())); 
//        StringBuffer sql = new StringBuffer("call UPD_SPONSOR_FORM_TEMPLATES(");
//        
//        sql.append(" <<SPONSOR_CODE>> , ");
//        sql.append(" <<PACKAGE_NUMBER>> , ");
//        sql.append(" <<PAGE_NUMBER>> , ");
//        sql.append(" <<PAGE_DESCRIPTION>> , ");
//        sql.append(" <<FORM_TEMPLATE>> , ");
//        sql.append(" <<UPDATE_USER>> , ");
//        sql.append(" <<UPDATE_TIMESTAMP>> , ");
//        sql.append(" <<AW_UPDATE_USER>> , ");
//        sql.append(" <<AW_UPDATE_TIMESTAMP>> , ");
//        sql.append(" <<AC_TYPE>> )");
//        
//        ProcReqParameter procReqParameter  = new ProcReqParameter();
//        procReqParameter.setDSN(DSN);
//        procReqParameter.setParameterInfo(param);
//        procReqParameter.setSqlCommand(sql.toString());      
//        Vector vctUpdateAwardRepReq = new Vector();
//        
//        vctUpdateAwardRepReq.addElement(procReqParameter);
//        if(dbEngine!=null) {
//           dbEngine.executeStoreProcs(vctUpdateAwardRepReq);            
//        } else {
//            throw new CoeusException("db_exceptionCode.1000");
//        }      
//        success = true;
//        return success;
//    }
    
    // 
    
    
    
    public SponsorTemplateBean updateSponsorFormTemplates(SponsorTemplateBean sponsorTemplateBean)
        throws CoeusException, DBException{       
            
        boolean success = false;
        Vector param=null;
        Timestamp dbTimestamp = (new CoeusFunctions()).getDBTimestamp();
        param = new Vector();
        param.addElement(new Parameter("SPONSOR_CODE",
            DBEngineConstants.TYPE_STRING, sponsorTemplateBean.getSponsorCode())); 
        param.addElement(new Parameter(PACKAGE_NUMBER,
            DBEngineConstants.TYPE_INT, ""+sponsorTemplateBean.getPackageNumber())); 
        param.addElement(new Parameter("PAGE_NUMBER",
            DBEngineConstants.TYPE_INT,""+sponsorTemplateBean.getPageNumber())); 
        param.addElement(new Parameter("PAGE_DESCRIPTION",
            DBEngineConstants.TYPE_STRING,sponsorTemplateBean.getPageDescription())); 
        // Getting the Form template data in byteArrayInputStream 
        if(sponsorTemplateBean.getFormTemplate() != null){
           byte byteData[] =  sponsorTemplateBean.getFormTemplate();
           // Commented by Shivakumar for BLOB implementation - BEGIN
//            param.addElement(new Parameter("FORM_TEMPLATE",
//                DBEngineConstants.TYPE_CLOB, new String(byteData))); 
            // Commented by Shivakumar for BLOB implementation - BEGIN
           //Added by Geo
           //Should throw exception if the template byte length is 0
           if(byteData.length==0)
            throw new CoeusException("The Template Data is null");
        
            param.addElement(new Parameter("FORM_TEMPLATE",
                DBEngineConstants.TYPE_BLOB, byteData)); 
            
        }
        param.addElement(new Parameter("UPDATE_TIMESTAMP",
            DBEngineConstants.TYPE_TIMESTAMP, dbTimestamp));
        param.addElement(new Parameter("UPDATE_USER",
            DBEngineConstants.TYPE_STRING, userId));   
            



        param.addElement(new Parameter("AC_TYPE",
            DBEngineConstants.TYPE_STRING, sponsorTemplateBean.getAcType()));            
//        StringBuffer sql = new StringBuffer("call UPD_SPONSOR_FORM_TEMPLATES(");        
//        System.out.println("Action type="+sponsorTemplateBean.getAcType());
        StringBuffer sqlBudget = new StringBuffer("");
        if(sponsorTemplateBean.getAcType().trim().equals("I")){
            sqlBudget.append("insert into osp$sponsor_form_templates(");
            sqlBudget.append(" SPONSOR_CODE , ");
            sqlBudget.append(" PACKAGE_NUMBER , ");
            sqlBudget.append(" PAGE_NUMBER , ");
            sqlBudget.append(" PAGE_DESCRIPTION , ");
            sqlBudget.append(" FORM_TEMPLATE , ");
            sqlBudget.append(" UPDATE_TIMESTAMP , ");
            sqlBudget.append(" UPDATE_USER ) ");            



            sqlBudget.append(" VALUES (");
            sqlBudget.append(" <<SPONSOR_CODE>> , ");
            sqlBudget.append(" <<PACKAGE_NUMBER>> , ");
            sqlBudget.append(" <<PAGE_NUMBER>> , ");
            sqlBudget.append(" <<PAGE_DESCRIPTION>> , ");
            sqlBudget.append(" <<FORM_TEMPLATE>> , ");
            sqlBudget.append(" <<UPDATE_TIMESTAMP>> , ");
            sqlBudget.append(" <<UPDATE_USER>> ) ");



//            System.out.println("insert statement=>"+sqlBudget.toString());
        }else {           
            param.addElement(new Parameter("AW_UPDATE_TIMESTAMP",
                DBEngineConstants.TYPE_TIMESTAMP,
                sponsorTemplateBean.getLastUpdateTime()));
            param.addElement(new Parameter("AW_UPDATE_USER",
                DBEngineConstants.TYPE_STRING,
                sponsorTemplateBean.getUpdateUser()));              
            if(sponsorTemplateBean.getAcType().trim().equals("U")){
                
                sqlBudget.append("update osp$sponsor_form_templates set");
                sqlBudget.append(" PAGE_DESCRIPTION =  ");
                sqlBudget.append(" <<PAGE_DESCRIPTION>> , ");
                sqlBudget.append(" FORM_TEMPLATE =  ");
                sqlBudget.append(" <<FORM_TEMPLATE>> , ");
                sqlBudget.append(" UPDATE_TIMESTAMP = ");
                sqlBudget.append(" <<UPDATE_TIMESTAMP>>  ");                
                sqlBudget.append(" where ");
                sqlBudget.append(" SPONSOR_CODE = ");
                sqlBudget.append(" <<SPONSOR_CODE>> ");
                sqlBudget.append(" and PACKAGE_NUMBER = ");
                sqlBudget.append(" <<PACKAGE_NUMBER>> ");
                sqlBudget.append(" and PAGE_NUMBER = ");
                sqlBudget.append(" <<PAGE_NUMBER>> ");
                sqlBudget.append(" and UPDATE_TIMESTAMP = ");
                sqlBudget.append(" <<AW_UPDATE_TIMESTAMP>> ");
                sqlBudget.append(" and UPDATE_USER = ");
                sqlBudget.append(" <<AW_UPDATE_USER>> ");


//                System.out.println("update statement=>"+sqlBudget.toString());
            }else if(sponsorTemplateBean.getAcType().trim().equals("D")){
                sqlBudget.append(" delete from osp$sponsor_form_templates where ");
                sqlBudget.append(" SPONSOR_CODE = ");
                sqlBudget.append(" <<SPONSOR_CODE>> ");
                sqlBudget.append(" and PACKAGE_NUMBER = ");
                sqlBudget.append(" <<PACKAGE_NUMBER>> ");
                sqlBudget.append(" and PAGE_NUMBER = ");
                sqlBudget.append(" <<PAGE_NUMBER>> ");
                sqlBudget.append(" and UPDATE_TIMESTAMP = ");
                sqlBudget.append(" <<AW_UPDATE_TIMESTAMP>> ");
                sqlBudget.append(" and UPDATE_USER =");
                sqlBudget.append(" <<AW_UPDATE_USER>> ");


//                System.out.println("delete statement=>"+sqlBudget.toString());
            }
        }
        ProcReqParameter procSponsor  = new ProcReqParameter();
        procSponsor.setDSN(DSN);
        procSponsor.setParameterInfo(param);
        procSponsor.setSqlCommand(sqlBudget.toString());
        
        Vector vecProcParameters = new Vector();
        
        if(sponsorTemplateBean!=null && sponsorTemplateBean.getAcType() != null){             
             vecProcParameters.addElement(procSponsor);
         if(dbEngine!=null){
                java.sql.Connection conn = null;
                try{
                    conn = dbEngine.beginTxn();
                    if(vecProcParameters != null  && vecProcParameters.size() > 0) {
                        dbEngine.batchSQLUpdate(vecProcParameters, conn);
                    }
                    dbEngine.commit(conn);
                }catch(Exception sqlEx){
                    dbEngine.rollback(conn);
                    throw new CoeusException(sqlEx.getMessage());
                }finally{
                    dbEngine.endTxn(conn);
                }
         }else{
                throw new CoeusException("db_exceptionCode.1000");
         }
         success = true;     
        }
        
        sponsorTemplateBean.setLastUpdateTime(dbTimestamp);
        sponsorTemplateBean.setUpdateUser(userId);
        //Added for Case#2445 - proposal development print forms linked to indiv sponsor, should link to sponsor hierarchy  - Start
        UserMaintDataTxnBean userMaintDataTxnBean = new UserMaintDataTxnBean();
        String userName = userMaintDataTxnBean.getUserName(userId);
        sponsorTemplateBean.setUpdateUserName(userName);
        //Case#2445 - End
        sponsorTemplateBean.setAcType(null);
        return sponsorTemplateBean;
    } 
    
     public ProcReqParameter getSponsorFormTemplates(SponsorTemplateBean sponsorTemplateBean)
        throws CoeusException, DBException{       
            
        boolean success = false;
        Vector param=null;        
       // CoeusVector cvSponsorTemplateData = (CoeusVector)htSponsorData.get(KeyConstants.PAGE_DATA);
        Timestamp dbTimestamp = (new CoeusFunctions()).getDBTimestamp();
        param = new Vector();
        
        param.addElement(new Parameter("SPONSOR_CODE",
            DBEngineConstants.TYPE_STRING, sponsorTemplateBean.getSponsorCode())); 
        param.addElement(new Parameter(PACKAGE_NUMBER,
            DBEngineConstants.TYPE_INT, ""+sponsorTemplateBean.getPackageNumber())); 
        param.addElement(new Parameter("PAGE_NUMBER",
            DBEngineConstants.TYPE_INT,""+sponsorTemplateBean.getPageNumber())); 
        param.addElement(new Parameter("PAGE_DESCRIPTION",
            DBEngineConstants.TYPE_STRING,sponsorTemplateBean.getPageDescription())); 
        // Getting the Form template data in byteArrayInputStream         
//        if((sponsorTemplateBean.getAcType().equals("I")) || (sponsorTemplateBean.getAcType().equals("U"))){
        if(sponsorTemplateBean.getFormTemplate() != null){
            byte byteData[] =  sponsorTemplateBean.getFormTemplate();
            // Commented by Shivakumar for BLOB implementation - BEGIN
//            param.addElement(new Parameter("FORM_TEMPLATE",
//                DBEngineConstants.TYPE_CLOB, new String(byteData)));  
            // Commented by Shivakumar for BLOB implementation - BEGIN
            // Commented by Shivakumar for BLOB implementation - BEGIN
//                DBEngineConstants.TYPE_BLOB, new String(byteData)));  
            // Commented by Shivakumar for BLOB implementation - END
                param.addElement(new Parameter("FORM_TEMPLATE",
                DBEngineConstants.TYPE_BLOB, byteData));  
        }
        
        param.addElement(new Parameter("UPDATE_TIMESTAMP",
            DBEngineConstants.TYPE_TIMESTAMP, dbTimestamp));
        param.addElement(new Parameter("UPDATE_USER",
            DBEngineConstants.TYPE_STRING, userId));                  
        param.addElement(new Parameter("AC_TYPE",
            DBEngineConstants.TYPE_STRING, sponsorTemplateBean.getAcType())); 
        
        StringBuffer sqlBudget = new StringBuffer("");
        if(sponsorTemplateBean.getAcType().trim().equals("I")){
            sqlBudget.append("insert into osp$sponsor_form_templates(");
            sqlBudget.append(" SPONSOR_CODE , ");
            sqlBudget.append(" PACKAGE_NUMBER , ");
            sqlBudget.append(" PAGE_NUMBER , ");
            sqlBudget.append(" PAGE_DESCRIPTION , ");
            sqlBudget.append(" FORM_TEMPLATE , ");
            sqlBudget.append(" UPDATE_TIMESTAMP , ");
            sqlBudget.append(" UPDATE_USER ) ");            
        
            sqlBudget.append(" VALUES (");
            sqlBudget.append(" <<SPONSOR_CODE>> , ");
            sqlBudget.append(" <<PACKAGE_NUMBER>> , ");
            sqlBudget.append(" <<PAGE_NUMBER>> , ");
            sqlBudget.append(" <<PAGE_DESCRIPTION>> , ");
            sqlBudget.append(" <<FORM_TEMPLATE>> , ");
            sqlBudget.append(" <<UPDATE_TIMESTAMP>> , ");
            sqlBudget.append(" <<UPDATE_USER>> ) ");

        }else {           
            param.addElement(new Parameter("AW_UPDATE_TIMESTAMP",
                DBEngineConstants.TYPE_TIMESTAMP,
                sponsorTemplateBean.getLastUpdateTime()));
            param.addElement(new Parameter("AW_UPDATE_USER",
                DBEngineConstants.TYPE_STRING,
                sponsorTemplateBean.getUpdateUser()));            
            if(sponsorTemplateBean.getAcType().trim().equals("U")){                
                sqlBudget.append("update osp$sponsor_form_templates set");
                sqlBudget.append(" PAGE_DESCRIPTION =  ");
                sqlBudget.append(" <<PAGE_DESCRIPTION>> , ");                
                sqlBudget.append(" UPDATE_TIMESTAMP = ");
                sqlBudget.append(" <<UPDATE_TIMESTAMP>> ");                
                sqlBudget.append(" where ");
                sqlBudget.append(" SPONSOR_CODE = ");
                sqlBudget.append(" <<SPONSOR_CODE>> ");
                sqlBudget.append(" and PACKAGE_NUMBER = ");
                sqlBudget.append(" <<PACKAGE_NUMBER>> ");
                sqlBudget.append(" and PAGE_NUMBER = ");
                sqlBudget.append(" <<PAGE_NUMBER>> ");
                sqlBudget.append(" and UPDATE_TIMESTAMP = ");
                sqlBudget.append(" <<AW_UPDATE_TIMESTAMP>> ");
                sqlBudget.append(" and UPDATE_USER = ");
                sqlBudget.append(" <<AW_UPDATE_USER>> ");

            }else if(sponsorTemplateBean.getAcType().trim().equals("D")){
                sqlBudget.append(" delete from osp$sponsor_form_templates where ");
                sqlBudget.append(" SPONSOR_CODE = ");
                sqlBudget.append(" <<SPONSOR_CODE>> ");
                sqlBudget.append(" and PACKAGE_NUMBER = ");
                sqlBudget.append(" <<PACKAGE_NUMBER>> ");
                sqlBudget.append(" and PAGE_NUMBER = ");
                sqlBudget.append(" <<PAGE_NUMBER>> ");                
                sqlBudget.append(" and UPDATE_TIMESTAMP = ");
                sqlBudget.append(" <<AW_UPDATE_TIMESTAMP>> ");
                sqlBudget.append(" and UPDATE_USER =");
                sqlBudget.append(" <<AW_UPDATE_USER>> ");


            }
        }
        ProcReqParameter procSponsor  = new ProcReqParameter();
        procSponsor.setDSN(DSN);
        procSponsor.setParameterInfo(param);
        procSponsor.setSqlCommand(sqlBudget.toString());
        
        return procSponsor;
    }    
    
    
    public Hashtable updateSponsorDataBulk(Hashtable htSponsorData)
        throws CoeusException, DBException{       
        SponsorTemplateBean sponsorTemplateBean = null;    
        SponsorFormsBean sponsorFormsBean = null;
        boolean success = false;        
        Vector vecProcParameters = new Vector();
        Vector procedures = new Vector();
        CoeusVector cvSponsorTemplateData = (CoeusVector)htSponsorData.get(KeyConstants.PAGE_DATA);
        if (cvSponsorTemplateData!=null && cvSponsorTemplateData.size()>0) {
                  for(int index=0; index < cvSponsorTemplateData.size(); index++){
                      sponsorTemplateBean = (SponsorTemplateBean)cvSponsorTemplateData.elementAt(index);
                      if(sponsorTemplateBean.getAcType()==null) {
                          continue;
                      }
                      ProcReqParameter procSponsorTemplate = getSponsorFormTemplates(sponsorTemplateBean);
                      vecProcParameters.add(procSponsorTemplate);
                  }
        }           
        CoeusVector cvSponsorFormData = (CoeusVector)htSponsorData.get(KeyConstants.PACKAGE_DATA);
        if (cvSponsorFormData!=null && cvSponsorFormData.size()>0) {
                  for(int index=0; index < cvSponsorFormData.size(); index++){
                      sponsorFormsBean = (SponsorFormsBean)cvSponsorFormData.elementAt(index);
                      if(sponsorFormsBean.getAcType()==null) {
                          continue;
                      }
                      ProcReqParameter procSponsorForms = getSponsorForm(sponsorFormsBean);
                      procedures.add(procSponsorForms);
                  }
        }
        
        if((procedures != null  && procedures.size() > 0) ||  
           (vecProcParameters != null  && vecProcParameters.size() > 0)) {
       // if(sponsorTemplateBean!=null && sponsorTemplateBean.getAcType() != null){             
         if(dbEngine!=null){
                java.sql.Connection conn = null;
                try{
                    conn = dbEngine.beginTxn();
                    if(procedures != null  && procedures.size() > 0){
                        dbEngine.executeStoreProcs(procedures,conn);
                    }
                    if(vecProcParameters != null  && vecProcParameters.size() > 0){
                        dbEngine.batchSQLUpdate(vecProcParameters, conn); 
                    }
                    dbEngine.commit(conn);
                }catch(Exception sqlEx){
                    dbEngine.rollback(conn);
                    throw new CoeusException(sqlEx.getMessage());
                }finally{
                    dbEngine.endTxn(conn);
                }
         }else{
                throw new CoeusException("db_exceptionCode.1000");
         }
         success = true;     
        }     
        Hashtable htUpdateSponsorData = new Hashtable();
        //CoeusVector cvUpdSponsorFormData  = new CoeusVector();
       // CoeusVector cvUpdSponsorTemplateData  = new CoeusVector();
        
        //Modified for Case#2445 - proposal development print forms linked to indiv sponsor, should link to sponsor hierarchy - Start
        //When sponsor forms are loaded for hierarchy, sponsor forms are fetched based on group name(level1) else based on sponsor code
        Boolean isSponsorHierarchy = (Boolean)htSponsorData.get("SPONSOR_HIERARCHY");
        if(isSponsorHierarchy != null && isSponsorHierarchy.booleanValue()){
            String sponsorCode = (String)htSponsorData.get("SPONSOR_CODE");
            String groupName = (String)htSponsorData.get("GROUP_NAME");
            //gets all sponsor form data's based on sponsorcode and groupname (level1)
            CoeusVector cvUpdSponsorForms = getHierarchySponsorForms(sponsorCode,groupName);
            //gets all sponsor templates based on the sponsor forms
            CoeusVector cvUpdSponsorFormTemplates = getSponsorFormTemplates(cvUpdSponsorForms);
            htUpdateSponsorData.put(KeyConstants.PACKAGE_DATA, cvUpdSponsorForms);
            htUpdateSponsorData.put(KeyConstants.PAGE_DATA, cvUpdSponsorFormTemplates);
        }else{
            CoeusVector cvSponsorTemplateUpdData = (CoeusVector)htSponsorData.get(KeyConstants.PAGE_DATA);
            if (cvSponsorTemplateUpdData!=null && cvSponsorTemplateUpdData.size()>0) {
                for(int index=0; index < cvSponsorTemplateData.size(); index++){
                    sponsorTemplateBean = (SponsorTemplateBean)cvSponsorTemplateUpdData.elementAt(index);
                    String UpdateSponsorCode = sponsorTemplateBean.getSponsorCode();
                    // Setting acType to null while sending the bean to client
                    sponsorTemplateBean.setAcType("");
                    CoeusVector cvUpdSponsorTemplateData = getSponsorFormTemplates(UpdateSponsorCode);
                    if(cvUpdSponsorTemplateData != null){
                        htUpdateSponsorData.put(KeyConstants.PAGE_DATA, cvUpdSponsorTemplateData);
                    }
                }
            }
            
            
            CoeusVector cvSponsorFormUpdData = (CoeusVector)htSponsorData.get(KeyConstants.PACKAGE_DATA);
            if (cvSponsorFormUpdData!=null && cvSponsorFormUpdData.size()>0) {
                for(int index=0; index < cvSponsorFormData.size(); index++){
                    sponsorFormsBean = (SponsorFormsBean)cvSponsorFormUpdData.elementAt(index);
                    String UpdateSponFormsCode = sponsorFormsBean.getSponsorCode();
                    // Setting acType to null while sending the bean to client
                    sponsorFormsBean.setAcType("");
                    CoeusVector cvUpdSponsorFormsData = getSponsorForms(UpdateSponFormsCode);
                    if(cvUpdSponsorFormsData != null){
                        htUpdateSponsorData.put(KeyConstants.PACKAGE_DATA, cvUpdSponsorFormsData);
                    }
                }
            }
        }
        return htUpdateSponsorData;
    } 
    
    
    public int checkSponsorFormPageExists(String sponsorCode,int packageNumber,int pageNumber)
        throws CoeusException, DBException{
            int pageState = 0;
        Vector param= new Vector();
        Vector result = new Vector();
        HashMap invRow=null;
        param.addElement(new Parameter("SPONSOR_CODE",
            DBEngineConstants.TYPE_STRING, sponsorCode));
        param.addElement(new Parameter(PACKAGE_NUMBER,
            DBEngineConstants.TYPE_INT, ""+packageNumber));
        param.addElement(new Parameter("PAGE_NUMBER",
            DBEngineConstants.TYPE_INT, ""+pageNumber));
        if(dbEngine!=null){
            result = dbEngine.executeFunctions("Coeus",
            "{ <<OUT INTEGER PAGE_EXISTS>> = call FN_CHK_SPON_TEMP_PAGE_EXISTS(<< SPONSOR_CODE >>,"+
            "<< PACKAGE_NUMBER >> , << PAGE_NUMBER >> ) }", param);
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }
        if(!result.isEmpty()){
            HashMap rowParameter = (HashMap)result.elementAt(0);
            pageState = Integer.parseInt(rowParameter.get("PAGE_EXISTS").toString());
        }
        return pageState;
    } 
    
    
    
    public CoeusVector getSponsorHierarchyList() throws CoeusException, DBException{
        Vector param= new Vector();
        Vector result = new Vector();
        CoeusVector cvSponHierarchyList = null;
        HashMap hmSponHierarchy = null;
        result = dbEngine.executeRequest("Coeus",
            "call DW_GET_SPONSOR_HIERARCHY_LIST( <<OUT RESULTSET rset>> )",
            "Coeus", param);
        int listSize=result.size();
        if(listSize > 0){
            cvSponHierarchyList = new CoeusVector();
            for(int count=0;count<listSize;count++){
                hmSponHierarchy = (HashMap)result.elementAt(count);                
                cvSponHierarchyList.addElement(hmSponHierarchy.get("HIERARCHY_NAME"));
            }    
        }
        return cvSponHierarchyList;
        
    }    
    
    public boolean deleteSponsorHierarchy(String hierarchyName) throws 
        CoeusException, DBException{
            
        Vector param= new Vector();
        Vector result = new Vector();   
        boolean success = false;
        int status = 0;
        param.addElement(new Parameter("HIERARCHY_NAME",
            DBEngineConstants.TYPE_STRING, hierarchyName));
        if(dbEngine!=null){
            result = dbEngine.executeFunctions("Coeus",
            "{ <<OUT INTEGER DELETE_STATUS>> = call FN_DELETE_SPONSOR_HIERARCHY(<< HIERARCHY_NAME >> ) }", param);
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }
        if(!result.isEmpty()){
            HashMap rowParameter = (HashMap)result.elementAt(0);
            status = Integer.parseInt(rowParameter.get("DELETE_STATUS").toString());
        }
        //case 2427 start
//        if(status == 1){
         if(status == 0){
        //case 2427 end
            success = true;
        }else{
            success = false;
        }    
        
        return success;
        
    }    
    
    
    public boolean copySponsorHierarchy(String sourceHierarchy, String targetHierarchy, String loggedinUser) 
        throws CoeusException, DBException{
            
        Vector param= new Vector();
        Vector result = new Vector();   
        boolean success = false;
        int status = 0;
        param.addElement(new Parameter("AS_SOURCE_HIERARCHY",
            DBEngineConstants.TYPE_STRING, sourceHierarchy));
        param.addElement(new Parameter("AS_TARGET_HIERARCHY",
            DBEngineConstants.TYPE_STRING, targetHierarchy));
        param.addElement(new Parameter("AS_USER_ID",
            DBEngineConstants.TYPE_STRING, loggedinUser));
        
        if(dbEngine!=null){
            result = dbEngine.executeFunctions("Coeus",
            "{ <<OUT INTEGER COPY_STATUS>> = call FN_COPY_SPONSOR_HIERARCHY(<< AS_SOURCE_HIERARCHY >>,<< AS_TARGET_HIERARCHY >>, << AS_USER_ID >> ) }", param);
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }
        
        if(!result.isEmpty()){
            HashMap rowParameter = (HashMap)result.elementAt(0);
            status = Integer.parseInt(rowParameter.get("COPY_STATUS").toString());
        }
        if(status == 0){
            success = true;
        }else{
            success = false;
        }    
        
        return success;
        
    }
    
    
    
    public CoeusVector getSponsorNotInHierarchy(String hierarchyName) throws CoeusException, DBException{
        Vector param= new Vector();
        Vector result = new Vector();
        SponsorHierarchyBean sponsorHierarchyBean = null;
        CoeusVector cvSponHierarchyList = null;
        HashMap hmSponHierarchy = null;
        param.addElement(new Parameter("HIERARCHY_NAME",
            DBEngineConstants.TYPE_STRING, hierarchyName));
        if(dbEngine!=null){
            result = dbEngine.executeRequest("Coeus",
                "call DW_GET_SPON_NOT_IN_HIERARCHY(<< HIERARCHY_NAME >>, <<OUT RESULTSET rset>> )",
                "Coeus", param);
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }    
        int listSize=result.size();
        if(listSize > 0){
            cvSponHierarchyList = new CoeusVector();
            for(int count=0;count<listSize;count++){
                sponsorHierarchyBean = new SponsorHierarchyBean();
                hmSponHierarchy = (HashMap)result.elementAt(count);                
                sponsorHierarchyBean.setSponsorCode((String)hmSponHierarchy.get("SPONSOR_CODE"));
                sponsorHierarchyBean.setSponsorName((String)hmSponHierarchy.get("SPONSOR_NAME"));
                cvSponHierarchyList.addElement(sponsorHierarchyBean);
            }    
        }
        return cvSponHierarchyList;
        
    }
    
    public CoeusVector getSponsorHierarchy(String hierarchyName) throws CoeusException, DBException{
        Vector param= new Vector();
        Vector result = new Vector();
        CoeusVector cvSponHierarchyList = null;
        SponsorHierarchyBean sponsorHierarchyBean = null;
        HashMap hmSponHierarchy = null;
        param.addElement(new Parameter("HIERARCHY_NAME",
            DBEngineConstants.TYPE_STRING,hierarchyName));
        result = dbEngine.executeRequest("Coeus",
            "call DW_GET_SPONSOR_HIERARCHY(<<HIERARCHY_NAME>>, <<OUT RESULTSET rset>> )",
            "Coeus", param);
        int listSize=result.size();
        int rowId = 1;
        if(listSize > 0){
            cvSponHierarchyList = new CoeusVector();
            for(int count=0;count<listSize;count++){
                sponsorHierarchyBean = new SponsorHierarchyBean();
                hmSponHierarchy = (HashMap)result.elementAt(count);                
                sponsorHierarchyBean.setHierarchyName((String)hmSponHierarchy.get("HIERARCHY_NAME"));
                sponsorHierarchyBean.setSponsorCode((String)hmSponHierarchy.get("SPONSOR_CODE"));
                sponsorHierarchyBean.setSponsorName((String)hmSponHierarchy.get("SPONSOR_NAME"));
                sponsorHierarchyBean.setLevelOne((String)hmSponHierarchy.get("LEVEL1"));
                sponsorHierarchyBean.setLevelTwo((String)hmSponHierarchy.get("LEVEL2"));
                sponsorHierarchyBean.setLevelThree((String)hmSponHierarchy.get("LEVEL3"));
                sponsorHierarchyBean.setLevelFour((String)hmSponHierarchy.get("LEVEL4"));
                sponsorHierarchyBean.setLevelFive((String)hmSponHierarchy.get("LEVEL5"));                
                sponsorHierarchyBean.setLevelSix((String)hmSponHierarchy.get("LEVEL6"));
                sponsorHierarchyBean.setLevelSeven((String)hmSponHierarchy.get("LEVEL7"));
                sponsorHierarchyBean.setLevelEight((String)hmSponHierarchy.get("LEVEL8"));
                sponsorHierarchyBean.setLevelNine((String)hmSponHierarchy.get("LEVEL9"));
                sponsorHierarchyBean.setLevelTen((String)hmSponHierarchy.get("LEVEL10"));
                sponsorHierarchyBean.setUpdateTimestamp((Timestamp)hmSponHierarchy.get("UPDATE_TIMESTAMP"));
                sponsorHierarchyBean.setUpdateUser((String)hmSponHierarchy.get("UPDATE_USER"));                
                sponsorHierarchyBean.setLevelOneSortId(hmSponHierarchy.get("LEVEL1_SORTID") == null ? 0 :
                    Integer.parseInt(hmSponHierarchy.get("LEVEL1_SORTID").toString()));
                sponsorHierarchyBean.setLevelTwoSortId(hmSponHierarchy.get("LEVEL2_SORTID") == null ? 0 :
                    Integer.parseInt(hmSponHierarchy.get("LEVEL2_SORTID").toString()));
                sponsorHierarchyBean.setLevelThreeSortId(hmSponHierarchy.get("LEVEL3_SORTID") == null ? 0 :
                    Integer.parseInt(hmSponHierarchy.get("LEVEL3_SORTID").toString()));                        
                sponsorHierarchyBean.setLevelFourSortId(hmSponHierarchy.get("LEVEL4_SORTID") == null ? 0 :
                    Integer.parseInt(hmSponHierarchy.get("LEVEL4_SORTID").toString()));
                sponsorHierarchyBean.setLevelFiveSortId(hmSponHierarchy.get("LEVEL5_SORTID") == null ? 0 :
                    Integer.parseInt(hmSponHierarchy.get("LEVEL5_SORTID").toString()));
                sponsorHierarchyBean.setLevelSixSortId(hmSponHierarchy.get("LEVEL6_SORTID") == null ? 0 :
                    Integer.parseInt(hmSponHierarchy.get("LEVEL6_SORTID").toString()));                
                sponsorHierarchyBean.setLevelSevenSortId(hmSponHierarchy.get("LEVEL7_SORTID") == null ? 0 :
                    Integer.parseInt(hmSponHierarchy.get("LEVEL7_SORTID").toString()));
                sponsorHierarchyBean.setLevelEightSortId(hmSponHierarchy.get("LEVEL8_SORTID") == null ? 0 :
                    Integer.parseInt(hmSponHierarchy.get("LEVEL8_SORTID").toString()));
                sponsorHierarchyBean.setLevelNineSortId(hmSponHierarchy.get("LEVEL9_SORTID") == null ? 0 :
                    Integer.parseInt(hmSponHierarchy.get("LEVEL9_SORTID").toString()));    
                sponsorHierarchyBean.setLevelTenSortId(hmSponHierarchy.get("LEVEL10_SORTID") == null ? 0 :
                    Integer.parseInt(hmSponHierarchy.get("LEVEL10_SORTID").toString()));        
                    sponsorHierarchyBean.setRowId(rowId++);
                cvSponHierarchyList.addElement(sponsorHierarchyBean);
            }    
        }
        return cvSponHierarchyList;
    }
    
    public boolean updateSponsorHierarchy(SponsorHierarchyBean sponsorHierarchyBean) 
        throws CoeusException, DBException{
            
        Vector paramSponsorHierarchy = new Vector();        
        CoeusFunctions coeusFunctions = new CoeusFunctions();
        Vector procedures = new Vector(5,3);
        Timestamp dbTimestamp = coeusFunctions.getDBTimestamp();
        boolean success = false;  
        paramSponsorHierarchy.addElement(new Parameter("HIERARCHY_NAME",
            DBEngineConstants.TYPE_STRING,sponsorHierarchyBean.getHierarchyName()));
        paramSponsorHierarchy.addElement(new Parameter("SPONSOR_CODE",
            DBEngineConstants.TYPE_STRING,sponsorHierarchyBean.getSponsorCode()));
        paramSponsorHierarchy.addElement(new Parameter("LEVEL1",
            DBEngineConstants.TYPE_STRING,sponsorHierarchyBean.getLevelOne()));
        paramSponsorHierarchy.addElement(new Parameter("LEVEL2",
            DBEngineConstants.TYPE_STRING,sponsorHierarchyBean.getLevelTwo()));
        paramSponsorHierarchy.addElement(new Parameter("LEVEL3",
            DBEngineConstants.TYPE_STRING,sponsorHierarchyBean.getLevelThree()));
        paramSponsorHierarchy.addElement(new Parameter("LEVEL4",
            DBEngineConstants.TYPE_STRING,sponsorHierarchyBean.getLevelFour()));
        paramSponsorHierarchy.addElement(new Parameter("LEVEL5",
            DBEngineConstants.TYPE_STRING,sponsorHierarchyBean.getLevelFive()));        
        paramSponsorHierarchy.addElement(new Parameter("LEVEL6",
            DBEngineConstants.TYPE_STRING,sponsorHierarchyBean.getLevelSix()));
        paramSponsorHierarchy.addElement(new Parameter("LEVEL7",
            DBEngineConstants.TYPE_STRING,sponsorHierarchyBean.getLevelSeven()));
        paramSponsorHierarchy.addElement(new Parameter("LEVEL8",
            DBEngineConstants.TYPE_STRING,sponsorHierarchyBean.getLevelEight()));
        paramSponsorHierarchy.addElement(new Parameter("LEVEL9",
            DBEngineConstants.TYPE_STRING,sponsorHierarchyBean.getLevelNine()));
        paramSponsorHierarchy.addElement(new Parameter("LEVEL10",
            DBEngineConstants.TYPE_STRING,sponsorHierarchyBean.getLevelTen()));
        paramSponsorHierarchy.addElement(new Parameter("UPDATE_TIMESTAMP",
            DBEngineConstants.TYPE_TIMESTAMP,dbTimestamp));
        paramSponsorHierarchy.addElement(new Parameter("UPDATE_USER",
            DBEngineConstants.TYPE_STRING,userId));
        paramSponsorHierarchy.addElement(new Parameter("LEVEL1_SORTID",
            DBEngineConstants.TYPE_INTEGER,sponsorHierarchyBean.getLevelOneSortId()== 0 ?null:new Integer(sponsorHierarchyBean.getLevelOneSortId())));
            
        paramSponsorHierarchy.addElement(new Parameter("LEVEL2_SORTID",
            DBEngineConstants.TYPE_INTEGER,sponsorHierarchyBean.getLevelTwoSortId() == 0 ? null :new Integer(sponsorHierarchyBean.getLevelTwoSortId())));
        paramSponsorHierarchy.addElement(new Parameter("LEVEL3_SORTID",
            DBEngineConstants.TYPE_INTEGER,sponsorHierarchyBean.getLevelThreeSortId() == 0 ? null :new Integer(sponsorHierarchyBean.getLevelThreeSortId())));        
        paramSponsorHierarchy.addElement(new Parameter("LEVEL4_SORTID",
            DBEngineConstants.TYPE_INTEGER,sponsorHierarchyBean.getLevelFourSortId() == 0 ? null :new Integer(sponsorHierarchyBean.getLevelFourSortId())));
        paramSponsorHierarchy.addElement(new Parameter("LEVEL5_SORTID",
            DBEngineConstants.TYPE_INTEGER,sponsorHierarchyBean.getLevelFiveSortId() == 0 ? null :new Integer(sponsorHierarchyBean.getLevelFiveSortId())));
        paramSponsorHierarchy.addElement(new Parameter("LEVEL6_SORTID",
            DBEngineConstants.TYPE_INTEGER,sponsorHierarchyBean.getLevelSixSortId() == 0 ? null :new Integer(sponsorHierarchyBean.getLevelSixSortId())));
        paramSponsorHierarchy.addElement(new Parameter("LEVEL7_SORTID",
            DBEngineConstants.TYPE_INTEGER,sponsorHierarchyBean.getLevelSevenSortId() == 0 ? null :new Integer(sponsorHierarchyBean.getLevelSevenSortId())));
        paramSponsorHierarchy.addElement(new Parameter("LEVEL8_SORTID",
            DBEngineConstants.TYPE_INTEGER,sponsorHierarchyBean.getLevelEightSortId() == 0 ? null :new Integer(sponsorHierarchyBean.getLevelEightSortId())));
        paramSponsorHierarchy.addElement(new Parameter("LEVEL9_SORTID",
            DBEngineConstants.TYPE_INTEGER,sponsorHierarchyBean.getLevelNineSortId() == 0 ? null :new Integer(sponsorHierarchyBean.getLevelNineSortId())));
        paramSponsorHierarchy.addElement(new Parameter("LEVEL10_SORTID",
            DBEngineConstants.TYPE_INTEGER,sponsorHierarchyBean.getLevelTenSortId() == 0 ? null :new Integer(sponsorHierarchyBean.getLevelTenSortId())));
        paramSponsorHierarchy.addElement(new Parameter("AW_HIERARCHY_NAME",
            DBEngineConstants.TYPE_STRING,sponsorHierarchyBean.getHierarchyName()));
        paramSponsorHierarchy.addElement(new Parameter("AW_SPONSOR_CODE",
            DBEngineConstants.TYPE_STRING,sponsorHierarchyBean.getSponsorCode()));        
        paramSponsorHierarchy.addElement(new Parameter("AW_UPDATE_TIMESTAMP",
            DBEngineConstants.TYPE_TIMESTAMP,sponsorHierarchyBean.getUpdateTimestamp()));
        paramSponsorHierarchy.addElement(new Parameter("AW_UPDATE_USER",
            DBEngineConstants.TYPE_STRING,sponsorHierarchyBean.getUpdateUser()));
        paramSponsorHierarchy.addElement(new Parameter("AC_TYPE",
            DBEngineConstants.TYPE_STRING,sponsorHierarchyBean.getAcType()));
        
        StringBuffer sqlSponsorHierarchy = new StringBuffer(
        "call DW_UPDATE_SPONSOR_HIERARCHY(");
        sqlSponsorHierarchy.append(" <<HIERARCHY_NAME>> , ");
        sqlSponsorHierarchy.append(" <<SPONSOR_CODE>> , ");    
        sqlSponsorHierarchy.append(" <<LEVEL1>> , ");
        sqlSponsorHierarchy.append(" <<LEVEL2>> , ");
        sqlSponsorHierarchy.append(" <<LEVEL3>> , ");        
        sqlSponsorHierarchy.append(" <<LEVEL4>> , ");
        sqlSponsorHierarchy.append(" <<LEVEL5>> , ");
        sqlSponsorHierarchy.append(" <<LEVEL6>> , ");        
        sqlSponsorHierarchy.append(" <<LEVEL7>> , ");
        sqlSponsorHierarchy.append(" <<LEVEL8>> , ");
        sqlSponsorHierarchy.append(" <<LEVEL9>> , ");
        sqlSponsorHierarchy.append(" <<LEVEL10>> , ");
        sqlSponsorHierarchy.append(" <<UPDATE_TIMESTAMP>> , ");
        sqlSponsorHierarchy.append(" <<UPDATE_USER>> , ");     
        sqlSponsorHierarchy.append(" <<LEVEL1_SORTID>> , ");
        sqlSponsorHierarchy.append(" <<LEVEL2_SORTID>> , ");
        sqlSponsorHierarchy.append(" <<LEVEL3_SORTID>> , ");
        sqlSponsorHierarchy.append(" <<LEVEL4_SORTID>> , ");
        sqlSponsorHierarchy.append(" <<LEVEL5_SORTID>> , ");
        sqlSponsorHierarchy.append(" <<LEVEL6_SORTID>> , ");
        sqlSponsorHierarchy.append(" <<LEVEL7_SORTID>> , ");
        sqlSponsorHierarchy.append(" <<LEVEL8_SORTID>> , ");
        sqlSponsorHierarchy.append(" <<LEVEL9_SORTID>> , ");
        sqlSponsorHierarchy.append(" <<LEVEL10_SORTID>> , ");
        sqlSponsorHierarchy.append(" <<AW_HIERARCHY_NAME>> , ");
        sqlSponsorHierarchy.append(" <<AW_SPONSOR_CODE>> , ");    
        sqlSponsorHierarchy.append(" <<AW_UPDATE_TIMESTAMP>> , ");        
        sqlSponsorHierarchy.append(" <<AW_UPDATE_USER>> , ");        
        sqlSponsorHierarchy.append(" <<AC_TYPE>> )");
        
        ProcReqParameter procSponsorHierarchy = new ProcReqParameter();
        procSponsorHierarchy.setDSN(DSN);
        procSponsorHierarchy.setParameterInfo(paramSponsorHierarchy);
        procSponsorHierarchy.setSqlCommand(sqlSponsorHierarchy.toString());
        
        procedures.add(procSponsorHierarchy);
        
        if(dbEngine!=null){
            if(procedures != null && procedures.size() > 0){
                dbEngine.executeStoreProcs(procedures);
            }
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }
        success = true;
        return success;
     }    
    
    
	/**
	 * This method is used to get the SponsorContacts.
	 * The stored procedure used is GET_SPONSOR_CONTACT
	 * @return CoeusVector
	 * @exception DBException if any error during database transaction.
         * @exception CoeusException if the instance of dbEngine is not available.
	 */
    //Added by Jinu 28-02-2005
    public Vector getSponsorContactTypes() throws CoeusException, DBException{
        Vector result = new Vector(3,2);
        Vector param= new Vector();
        Vector types = new Vector();
        if(dbEngine!=null){
            result = dbEngine.executeRequest("Coeus",
            "call GET_SPONSOR_CONTACT_TYPE  ( <<OUT RESULTSET rset>> )",
            "Coeus", param);
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }
        for(int i=0;i<result.size();i++){
            HashMap sponsorTypeRow = (HashMap)result.elementAt(i);
            types.addElement(new ComboBoxBean(sponsorTypeRow.get(
            "SPONSOR_CONTACT_TYPE_CODE").toString(),
            sponsorTypeRow.get("DESCRIPTION").toString()));
        }
        return types;
    }
    
    //End Jinu.
    
    
    
    
	/**
	 * This method is used to get the SponsorContacts.
	 * The stored procedure used is GET_SPONSOR_CONTACT
	 * @return CoeusVector
	 * @exception DBException if any error during database transaction.
         * @exception CoeusException if the instance of dbEngine is not available.
	 */
    //Added by Jinu 31-01-2005
    public CoeusVector getSponsorContacts(String sponsorCode) throws CoeusException, DBException{
        Vector result = new Vector();
        Vector param= new Vector();
        HashMap hmSponsorContacts = null;
        SponsorContactBean sponsorContactBean = null;
        CoeusVector cvSponsorContact = null;
        param.addElement(new Parameter("SPONSOR_CODE",
        DBEngineConstants.TYPE_STRING,sponsorCode.trim()));

        if(dbEngine!=null){
            result = dbEngine.executeRequest("Coeus",
            "call GET_SPONSOR_CONTACT ( << SPONSOR_CODE >>,<<OUT RESULTSET rset>> )",
            "Coeus", param);
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }
        int listSize = result.size();
        if(listSize > 0){
            cvSponsorContact = new CoeusVector();
            for(int index = 0; index < listSize; index++){
                sponsorContactBean = new SponsorContactBean();
                hmSponsorContacts = (HashMap)result.elementAt(index);
                sponsorContactBean.setSponsorCode((String)hmSponsorContacts.get("SPONSOR_CODE"));
                int sponsorContactTypeCode = Integer.parseInt(hmSponsorContacts.get("SPONSOR_CONTACT_TYPE_CODE").toString());
                sponsorContactBean.setSponsorContactTypeCode(sponsorContactTypeCode);
                sponsorContactBean.setPersonId((String)hmSponsorContacts.get("PERSON_ID"));
                sponsorContactBean.setPersonName((String)hmSponsorContacts.get("FULL_NAME"));
                sponsorContactBean.setUpdateTimestamp((Timestamp)hmSponsorContacts.get("UPDATE_TIMESTAMP"));
                sponsorContactBean.setUpdateUser((String)hmSponsorContacts.get("UPDATE_USER"));
                
                sponsorContactBean.setAwSponsorCode(sponsorContactBean.getSponsorCode());
                sponsorContactBean.setAwSponsorContactTypeCode(sponsorContactBean.getSponsorContactTypeCode());
                sponsorContactBean.setAwPersonId(sponsorContactBean.getPersonId());
                cvSponsorContact.addElement(sponsorContactBean);
            }
        }
        return cvSponsorContact;
    }    
    
    //End Jinu.

    /**
     * Method used to add and update the SponsorContact details
     * This uses update_sponsor_contact  procedure.
     *
     * @param sponsorMaintBean contains sponsor details for modify\insert
     * @param rolodexDetailsBean contains rolodex details
     * @return boolean on the successful Update or Add.
     * @exception DBException if any error during database transaction.
     * @exception CoeusException if the instance of dbEngine is not available.
     */
    //Added by Jinu. 28-01-2005
    public boolean addUpdDeleteSponsorContact(SponsorContactBean sponsorContactBean) throws CoeusException, DBException{
        Timestamp dbTimestamp = (new CoeusFunctions()).getDBTimestamp();                     
        Vector param = new Vector();
        Vector procedures = new Vector(5,3);

        param.addElement(new Parameter("SPONSOR_CODE",
        DBEngineConstants.TYPE_STRING,
        sponsorContactBean.getSponsorCode()));
        param.addElement(new Parameter("SPONSOR_CONTACT_TYPE_CODE",
        DBEngineConstants.TYPE_INT,
        ""+sponsorContactBean.getSponsorContactTypeCode()));
        param.addElement(new Parameter("PERSON_ID",
        DBEngineConstants.TYPE_STRING,
        sponsorContactBean.getPersonId()));
        param.addElement(new Parameter("UPDATE_USER",
        DBEngineConstants.TYPE_STRING,
        userId));        
        param.addElement(new Parameter("UPDATE_TIMESTAMP",
        DBEngineConstants.TYPE_TIMESTAMP,
        dbTimestamp));                

        param.addElement(new Parameter("AW_SPONSOR_CODE",
        DBEngineConstants.TYPE_STRING,
        sponsorContactBean.getAwSponsorCode()));
        param.addElement(new Parameter("AW_SPONSOR_CONTACT_TYPE_CODE",
        DBEngineConstants.TYPE_INT,
        ""+sponsorContactBean.getAwSponsorContactTypeCode()));
        param.addElement(new Parameter("AW_PERSON_ID",
        DBEngineConstants.TYPE_STRING,
        sponsorContactBean.getAwPersonId()));
        param.addElement(new Parameter("AW_UPDATE_USER",
        DBEngineConstants.TYPE_STRING,
        sponsorContactBean.getUpdateUser()));        
        param.addElement(new Parameter("AW_UPDATE_TIMESTAMP",
        DBEngineConstants.TYPE_TIMESTAMP,
        sponsorContactBean.getUpdateTimestamp()));                

        param.addElement(new Parameter("AC_TYPE",
        DBEngineConstants.TYPE_STRING,
        sponsorContactBean.getAcType()));
        StringBuffer strqry = new StringBuffer("call UPDATE_SPONSOR_CONTACT ( ");
        strqry.append(" << SPONSOR_CODE >> ,");
        strqry.append(" << SPONSOR_CONTACT_TYPE_CODE >> ,");
        strqry.append(" << PERSON_ID >> ,");
        strqry.append(" << UPDATE_USER >> ,");
        strqry.append(" << UPDATE_TIMESTAMP >> ,");        
        strqry.append(" << AW_SPONSOR_CODE >> , ");
        strqry.append(" << AW_SPONSOR_CONTACT_TYPE_CODE >> , ");    
        strqry.append(" << AW_PERSON_ID >> , ");    
        strqry.append(" << AW_UPDATE_USER >> , ");        
        strqry.append(" << AW_UPDATE_TIMESTAMP >> , ");        
        strqry.append(" << AC_TYPE >> )");

        ProcReqParameter procReqParam  = new ProcReqParameter();
        procReqParam.setDSN("Coeus");
        procReqParam.setParameterInfo(param);
        procReqParam.setSqlCommand(strqry.toString());


        procedures.addElement(procReqParam);


        if (dbEngine != null){
            dbEngine.executeStoreProcs(procedures);
        }else {
            throw new CoeusException("db_exceptionCode.1000");
        }
        return true;
    }
    //End Jinu.
    /**
     * This method deletes the sponsor details in the sponsor and rolodex table.
     *  <li>To fetch the data, it uses delete_sponsor,delete_rolodex procedure.
     *
     *  @param sponsorCode String
     *  @param isModify to check for modify
     *  @return boolean on the successful delete.
     *  @exception DBException if any error during database transaction.
     *  @exception CoeusException if the instance of dbEngine is not available.
     */
    //    Added by Jinu
    public boolean deleteSponsorContacts(String sponsorCode)
    throws CoeusException, DBException{
        Vector paramSponsorContact= new Vector();
        Vector procedures = new Vector(5,3);
        paramSponsorContact.add(new Parameter("SPONSORCODE",
        DBEngineConstants.TYPE_STRING,sponsorCode));

        StringBuffer strqrySp = new StringBuffer(
        "call DELETE_SPONSOR_CONTACTS (<< SPONSORCODE >> )");

        ProcReqParameter procReqParamSp  = new ProcReqParameter();
        procReqParamSp.setDSN("Coeus");
        procReqParamSp.setParameterInfo(paramSponsorContact);
        procReqParamSp.setSqlCommand(strqrySp.toString());


        /* Add the procedure Requirement parameter to the Vector */
        procedures.addElement(procReqParamSp);

        if(dbEngine!=null){
            dbEngine.executeStoreProcs(procedures);
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }

        return true;
    }
    //End Jinu
    
    // Code added by Shivakumar for Sponsor Forms Maintenance in Coeus 4.0 - END
    public static void main(String args[]){
        try{
            SponsorMaintenanceDataTxnBean sponsorMaintenanceDataTxnBean=new SponsorMaintenanceDataTxnBean();              
            SponsorHierarchyBean sponsorHierarchyBean = new SponsorHierarchyBean();
            Timestamp dbTimestamp = (new CoeusFunctions()).getDBTimestamp();            
            sponsorHierarchyBean.setHierarchyName("Test");
            sponsorHierarchyBean.setSponsorCode("002952");
            sponsorHierarchyBean.setUpdateTimestamp(dbTimestamp);
            sponsorHierarchyBean.setUpdateUser("COEUS");
            sponsorHierarchyBean.setLevelOne("For testing");
            sponsorHierarchyBean.setAcType("U");
            boolean success = sponsorMaintenanceDataTxnBean.updateSponsorHierarchy(sponsorHierarchyBean);
            System.out.println("Updation status "+success);
//            CoeusVector cvSponList = sponsorMaintenanceDataTxnBean.getSponsorHierarchy("Activity");
//            System.out.println("Vector size "+cvSponList.size());
            
            
        }catch(Exception ex){
            ex.printStackTrace();
        }    
    }
    
    //Case#2445 - proposal development print forms linked to indiv sponsor, should link to sponsor hierarchy -Start
    /*
     * Method to get the hierarchy sponsor form details through GET_HIERARCHY_SPONSOR_FORMS
     * from OSP$SPONSOR_FORMS table
     * @param sponsorCode
     * @param groupName - level1 name in OSP$SPONSOR_HIERARCHY
     * @return formList - CoeusVector hold's SponsorFormsBean 
     * @throws CoeusException,DBException
     */
    public CoeusVector getHierarchySponsorForms(String sponsorCode,String groupName)
    throws CoeusException, DBException{
        Vector param= new Vector();
        Vector result = new Vector();
        HashMap formRow=null;
        param.addElement(new Parameter(SPONSOR_CODE,
                DBEngineConstants.TYPE_STRING, sponsorCode));
        param.addElement(new Parameter("GROUP_NAME",
                DBEngineConstants.TYPE_STRING, groupName));
        if(dbEngine !=null){
            result=new Vector(3,2);
            result=dbEngine.executeRequest("Coeus",
                    "call GET_HIERARCHY_SPONSOR_FORMS(<< SPONSOR_CODE >>,<< GROUP_NAME >>,<< OUT RESULTSET rset >>)", "Coeus", param);
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }
        int listSize=result.size();
        CoeusVector formList = new CoeusVector();
        SponsorFormsBean sponsorFormsBean = null;
        if(listSize>0){
            for(int index = 0; index < listSize; index++){
                sponsorFormsBean = new SponsorFormsBean();
                formRow=(HashMap)result.elementAt(index);
                int rowId = index+1;
                sponsorFormsBean.setRowId(rowId);
                sponsorFormsBean.setSponsorCode((String)formRow.get(SPONSOR_CODE));
                sponsorFormsBean.setPackageNumber(Integer.parseInt(formRow.get(PACKAGE_NUMBER) == null ? "1" :
                    formRow.get(PACKAGE_NUMBER).toString()));
                sponsorFormsBean.setPackageName((String)formRow.get("PACKAGE_NAME"));
                sponsorFormsBean.setLastUpdateTime((Timestamp)formRow.get("UPDATE_TIMESTAMP"));
                sponsorFormsBean.setUpdateUser((String)formRow.get("UPDATE_USER"));
                sponsorFormsBean.setGroupName((String)formRow.get("GROUP_NAME"));
                formList.addElement(sponsorFormsBean);
            }
        }
        return formList;
    }
    
    /*
     * Method to get sponsor form templates for sponsor hierarchy group(level1) through GET_HIERAR_SPON_FORM_TEMPLATES
     * from OSP$SPONSOR_FORM_TEMPLATES table
     * @param cvHierarPackData
     * @return templateList - CoeusVector hold's sponsorTemplateBean
     * @throws CoeusException,DBException
     */
    public CoeusVector getSponsorFormTemplates(CoeusVector cvHierarPackData)
    throws CoeusException, DBException{
        CoeusVector templateList = new CoeusVector();
        if(cvHierarPackData != null){
            for(int index=0 ;index<cvHierarPackData.size();index++){
                SponsorFormsBean sponsorFormsBean = (SponsorFormsBean)cvHierarPackData.get(index);
                if(sponsorFormsBean != null){
                    Vector param= new Vector();
                    Vector result = new Vector();
                    HashMap templateRow=null;
                    param.addElement(new Parameter(SPONSOR_CODE,
                            DBEngineConstants.TYPE_STRING, sponsorFormsBean.getSponsorCode()));
                    param.addElement(new Parameter(PACKAGE_NUMBER,
                            DBEngineConstants.TYPE_INT,new Integer(sponsorFormsBean.getPackageNumber())));
                    if(dbEngine !=null){
                        result=new Vector(3,2);
                        result=dbEngine.executeRequest("Coeus",
                                "call GET_HIERAR_SPON_FORM_TEMPLATES(<< SPONSOR_CODE >>, << PACKAGE_NUMBER >>,<< OUT RESULTSET rset >>)", "Coeus", param);
                    }else{
                        throw new CoeusException("db_exceptionCode.1000");
                    }
                    int listSize=result.size();
                    
                    SponsorTemplateBean sponsorTemplateBean = null;
                    if(listSize>0){
                        for(int pageIndex = 0; pageIndex < listSize; pageIndex++){
                            sponsorTemplateBean = new SponsorTemplateBean();
                            templateRow=(HashMap)result.elementAt(pageIndex);
                            sponsorTemplateBean.setSponsorCode((String)templateRow.get(SPONSOR_CODE));
                            
                            int rowId = index+1;
                            sponsorTemplateBean.setRowId(rowId);
                            
                            sponsorTemplateBean.setPackageNumber(Integer.parseInt(templateRow.get(PACKAGE_NUMBER) == null ? "1" :
                                templateRow.get(PACKAGE_NUMBER).toString()));
                            sponsorTemplateBean.setPageNumber(Integer.parseInt(templateRow.get("PAGE_NUMBER") == null ? "1" :
                                templateRow.get("PAGE_NUMBER").toString()));
                            sponsorTemplateBean.setPageDescription((String)templateRow.get("PAGE_DESCRIPTION"));
                            sponsorTemplateBean.setLastUpdateTime((Timestamp)templateRow.get("UPDATE_TIMESTAMP"));
                            sponsorTemplateBean.setUpdateUser((String)templateRow.get("UPDATE_USER"));
                            if(templateRow.get("UPDATE_USER_NAME") != null) {
                                sponsorTemplateBean.setUpdateUserName((String)templateRow.get("UPDATE_USER_NAME"));
                            } else {
                                sponsorTemplateBean.setUpdateUserName((String)templateRow.get("UPDATE_USER"));
                            }
                            templateList.addElement(sponsorTemplateBean);
                        }
                        
                    }
                }
                
            }
            
        }
        return templateList;
    }
    
    /*
     * Method to get max package number for hierarchy sponsor forms through FN_GET_HIERAR_MAX_PACK_NUMBER 
     * @param sponsorCode
     * @return maxPackageNumber
     * throws CoeusException, DBException
     */
    public int getSponsorFormMaxPackNumber(String sponsorCode) throws CoeusException, DBException{
        int maxPackageNumber = 0;
        Vector param= new Vector();
        Vector result = new Vector();
        param.add(new Parameter(SPONSOR_CODE,DBEngineConstants.TYPE_STRING,sponsorCode));
        
        if(dbEngine!=null){
            result = dbEngine.executeFunctions("Coeus",
                    "{ <<OUT INTEGER MAXPACKAGENUMBER>> = call FN_GET_SPONSOR_MAX_PACK_NUMBER(<< SPONSOR_CODE >>) }", param);
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }
        if(!result.isEmpty()){
            HashMap hmMaxpackageNumber = (HashMap)result.elementAt(0);
            maxPackageNumber = Integer.parseInt(
                    hmMaxpackageNumber.get("MAXPACKAGENUMBER").toString());
        }
        return maxPackageNumber;
    }
    
    /*
     * Method to get max page number for package through FN_GET_SPON_PACK_MAX_PAGE_NUM 
     * @param sponsorCode,packageNumber
     * @return maxPageNumber
     * throws CoeusException, DBException
     */
    public int getPackageMaxPageNumber(String sponsorCode, int packageNumber) throws CoeusException, DBException{
        int maxPageNumber = 0;
        Vector param= new Vector();
        Vector result = new Vector();
        param.add(new Parameter(SPONSOR_CODE,DBEngineConstants.TYPE_STRING,sponsorCode));
        param.add(new Parameter(PACKAGE_NUMBER,DBEngineConstants.TYPE_INT,new Integer(packageNumber)));
        
        if(dbEngine!=null){
            result = dbEngine.executeFunctions("Coeus",
                    "{ <<OUT INTEGER MAXPAGENUMBER>> = call FN_GET_SPON_PACK_MAX_PAGE_NUM(<< SPONSOR_CODE >>,<< PACKAGE_NUMBER >>) }", param);
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }
        if(!result.isEmpty()){
            HashMap hmMaxPageNumber = (HashMap)result.elementAt(0);
            maxPageNumber = Integer.parseInt(
                    hmMaxPageNumber.get("MAXPAGENUMBER").toString());
        }
        return maxPageNumber;
    }
    
    /*
     * Method to delete all the forms related to a group in hierarchy in OSPSPONSOR_FORMS table
     * @param groupName
     * @return success
     * @throws CoeusException, DBException
     */
    public boolean deleteSponsorFormInHierarchy(String groupName)
    throws CoeusException, DBException{
        
        boolean success= false;
        Vector param= new Vector();
        Vector result = new Vector();
        param.add(new Parameter("GROUP_NAME",DBEngineConstants.TYPE_STRING,groupName));
        
        if(dbEngine!=null){
            result = dbEngine.executeFunctions("Coeus",
                    "{ <<OUT INTEGER SUCCESS>> = call fn_del_forms_in_hier_for_group(<< GROUP_NAME >> ) }", param);
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }
        if(!result.isEmpty()){
            HashMap hmDeleteSuccess = (HashMap)result.elementAt(0);
            int updated = Integer.parseInt(
                    hmDeleteSuccess.get("SUCCESS").toString());
            if(updated == 1){
                success = true;
            }
            
        }
        
        return success;
    }
     /*
     * Method to delete all the forms and templates for hierarchy in OSPSPONSOR_FORMS and OSP$SPONSOR_FORM_TEMPLATE table
     * @return success
     * @throws CoeusException, DBException
     */
    public boolean deleteFormsAndTempInHierarchy() throws CoeusException, DBException{
        boolean success= false;
        Vector param= new Vector();
        Vector result = new Vector();
        param.add(new Parameter(SPONSOR_CODE,DBEngineConstants.TYPE_STRING,"HIERAR"));
        
        if(dbEngine!=null){
            result = dbEngine.executeFunctions("Coeus",
                    "{ <<OUT INTEGER SUCCESS>> = call fn_delete_forms_in_hierarchy (<< SPONSOR_CODE >> ) }", param);
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }
        if(!result.isEmpty()){
            HashMap hmDeleteSuccess = (HashMap)result.elementAt(0);
            int updated = Integer.parseInt(
                    hmDeleteSuccess.get("SUCCESS").toString());
            if(updated == 0){
                success = true;
            }
            
        }
        
        return success;
    }
    
    /**
     * Method to delete template for a group in hierarchy in OSP$SPONSOR_FORM_TEMPLATE table
     * @param groupName
     * @throws CoeusException, DBException
     */
    public void deleteSponsorFormTempInHierarchy(String groupName)throws CoeusException, DBException{
        CoeusVector cvSponsorForms = getHierarchySponsorForms("HIERAR",groupName);
        Vector param= new Vector();
        Vector result = new Vector();
        if(cvSponsorForms != null && cvSponsorForms.size() > 0){
            for(int index=0;index<cvSponsorForms.size();index++){
                SponsorFormsBean sponsorFormsBean = (SponsorFormsBean)cvSponsorForms.get(index);
                int packageNumber = sponsorFormsBean.getPackageNumber();
                param.add(new Parameter(SPONSOR_CODE,DBEngineConstants.TYPE_STRING,sponsorFormsBean.getSponsorCode()));
                param.add(new Parameter(PACKAGE_NUMBER,DBEngineConstants.TYPE_INT,new Integer(packageNumber)));    
                if(dbEngine!=null){
                    result = dbEngine.executeFunctions("Coeus",
                            "{ <<OUT INTEGER SUCCESS>> = call fn_del_templ_in_hier_for_group (<< SPONSOR_CODE >>,<<PACKAGE_NUMBER>> ) }", param);
                }else{
                    throw new CoeusException("db_exceptionCode.1000");
                }
            }
            
        }
    }
    
     /**
     * Method to check package is exists for the level1 group in SponsorHierarchy
     * @param groupName
     * @throws CoeusException, DBException
     */
    public boolean isFormExistsForGroup(String groupName)throws DBException, CoeusException{
        int formExists = -1;
        Vector param= new Vector();
        Vector result = new Vector();
        param.add(new Parameter("GROUP_NAME",DBEngineConstants.TYPE_STRING,groupName));
        
        if(dbEngine!=null){
            result = dbEngine.executeFunctions("Coeus",
                    "{ <<OUT INTEGER FORMEXISTS>> = call FN_PACK_EXIST_FOR_HIERAR_GROUP(<< GROUP_NAME >>) }", param);
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }
        if(!result.isEmpty()){
            HashMap hmMaxPageNumber = (HashMap)result.elementAt(0);
            formExists = Integer.parseInt(
                    hmMaxPageNumber.get("FORMEXISTS").toString());
        }
        if(formExists == 1){
            return true;
        }
        return false;
    }
    //Case#2445 - End
  
}
