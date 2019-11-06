/*
 * @(#)FinancialEntityDetailsBean.java 1.0 08/19/02
 *
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 */

package edu.mit.coeus.rolodexmaint.bean;

/**
 *
 * This class provides the methods for performing all procedure executions for
 * a RoldexDetails. Various methods are used to fetch
 * the Rolodex Details from the Database.
 * All methods are used <code>DBEngineImpl</code> singleton instance for the
 * databse interaction.
 *
 * @version 1.0 August 16, 2002, 12:16 AM
 * @author  Phaneendra Kumar
 * @modified by Sagin
 * @date 28-10-02
 * Description : Implemented Standard Error Handling.
 *
 */

import java.sql.SQLException;
import java.sql.Timestamp;
import java.sql.Connection;

import edu.mit.coeus.utils.UtilFactory;
import edu.mit.coeus.utils.ComboBoxBean;
import java.util.Vector;
import java.util.HashMap;

import edu.mit.coeus.exception.CoeusException;
//import edu.mit.coeus.rolodexmaint.gui.RolodexMaintenanceDetailForm;
import edu.mit.coeus.utils.dbengine.DBException;
import edu.mit.coeus.utils.dbengine.DBEngineImpl;
import edu.mit.coeus.utils.dbengine.DBEngineConstants;
import edu.mit.coeus.utils.dbengine.Parameter;
import edu.mit.coeus.utils.dbengine.ProcReqParameter;
import edu.mit.coeus.utils.dbengine.TransactionMonitor;
import edu.mit.coeus.utils.CoeusVector;
import edu.mit.coeus.utils.locking.LockingBean;
import edu.mit.coeus.utils.locking.LockingException;

public class RolodexMaintenanceDataTxnBean {

     /*
      *  Singleton instance of a dbEngine
      */
    private DBEngineImpl dbEngine;
    // Connection instance added by Shivakumar foer locking enhancement
    private Connection conn = null;
    /*
     *  To hold the result after the execution of the dbEngine.
     */
    private Vector vectResult;
    private TransactionMonitor  transMon;

    private RolodexDetailsBean rldxDetails;
    
    //Added on 14 May, 2004 - start
    private String userId;
    //Added on 14 May, 2004 - start

    /** Creates new RolodexMaintenanceDataTxnBean */
    public RolodexMaintenanceDataTxnBean() {
        dbEngine = new DBEngineImpl();
        transMon = TransactionMonitor.getInstance();
    }

    /** Creates new RolodexMaintenanceDataTxnBean */
    public RolodexMaintenanceDataTxnBean(String userId) {
        dbEngine = new DBEngineImpl();
        transMon = TransactionMonitor.getInstance();
        this.userId = userId;
    }    

    /*public RolodexDetailsBean getRolodexDetails(String rolodexId,boolean isModify)
    throws CoeusException, DBException {

        Vector param = new Vector();
        Vector vecResult = new Vector();
        Vector results = new Vector();
        Vector procedures = new Vector(5,3);
        rldxDetails = new RolodexDetailsBean();

        param.addElement(new Parameter("ROLODEXID",
        DBEngineConstants.TYPE_INT,rolodexId));
        if (isModify){
            StringBuffer strfnqry
            = new StringBuffer("{ <<OUT INTEGER LOCKEXISTS>> = call FN_GET_ROLODEX_LOCK(");
            strfnqry.append(" << ROLODEXID >> ) }");
            ProcReqParameter fnReqParamInfo  = new ProcReqParameter();
            fnReqParamInfo.setDSN("Coeus");
            fnReqParamInfo.setParameterInfo(param);
            fnReqParamInfo.setSqlCommand(strfnqry);
            procedures.addElement(fnReqParamInfo);
        }

        StringBuffer strqry = new StringBuffer("call select_rolodex ( ");
        strqry.append(" << ROLODEXID >> ,");
        strqry.append("<< OUT RESULTSET rset >> )");
        ProcReqParameter procReqParamInfo  = new ProcReqParameter();
        procReqParamInfo.setDSN("Coeus");
        procReqParamInfo.setParameterInfo(param);
        procReqParamInfo.setSqlCommand(strqry);
        procedures.addElement(procReqParamInfo);

        if(dbEngine!=null){
            //System.out.println("going to exedute store procs");
            results = dbEngine.executeStoreProcs(procedures,isModify,null);
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }
        //System.out.println("The results size " + results.size());

        if ( isModify ) {
            vecResult = (Vector)results.get(1);
        }else {
            vecResult = results;
        }
        //        if (results.get(pos) instanceof Vector) {
        //            vecResult = (Vector)results.get(pos);
        if (!vecResult.isEmpty()) {
            if (vecResult.elementAt(0) instanceof Hashtable) {
                Hashtable rolodexRow = (Hashtable)vecResult.elementAt(0);
                //System.out.println("the hashtable in the Vector is " +rolodexRow);
                rldxDetails.setRolodexId( (String)
                rolodexRow.get("ROLODEX_ID")));
                rldxDetails.setLastName( (String)
                rolodexRow.get("LAST_NAME")));
                rldxDetails.setMiddleName( (String)
                rolodexRow.get("MIDDLE_NAME")));
                rldxDetails.setFirstName( (String)
                rolodexRow.get("FIRST_NAME")));
                rldxDetails.setSuffix( (String)
                rolodexRow.get("SUFFIX")));
                rldxDetails.setPrefix( (String)
                rolodexRow.get("PREFIX")));
                rldxDetails.setTitle( (String)
                rolodexRow.get("TITLE")));
                rldxDetails.setOrganization( (String)
                rolodexRow.get("ORGANIZATION")));
                rldxDetails.setAddress1( (String)
                rolodexRow.get("ADDRESS_LINE_1")));
                rldxDetails.setAddress2( (String)
                rolodexRow.get("ADDRESS_LINE_2")));
                rldxDetails.setAddress3( (String)
                rolodexRow.get("ADDRESS_LINE_3")));
                rldxDetails.setFax( (String)
                rolodexRow.get("FAX_NUMBER")));
                rldxDetails.setEMail( (String)
                rolodexRow.get("EMAIL_ADDRESS")));
                rldxDetails.setCity( (String)
                rolodexRow.get("CITY")));
                rldxDetails.setCounty( (String)
                rolodexRow.get("COUNTY")));
                rldxDetails.setState( (String)
                rolodexRow.get("STATE")));
                rldxDetails.setPostalCode( (String)
                rolodexRow.get("POSTAL_CODE")));
                rldxDetails.setComments( (String)
                rolodexRow.get("COMMENTS")));
                rldxDetails.setPhone( (String)
                rolodexRow.get("PHONE_NUMBER")));
                rldxDetails.setCountry( (String)
                rolodexRow.get("COUNTRY_CODE")));
                rldxDetails.setSponsorCode( (String)
                rolodexRow.get("SPONSOR_CODE")));
                rldxDetails.setSponsorAddressFlag( (String)
                rolodexRow.get("SPONSOR_ADDRESS_FLAG")));
                rldxDetails.setOwnedByUnit( (String)
                rolodexRow.get("OWNED_BY_UNIT")));
                rldxDetails.setLastUpdateUser( (String)
                rolodexRow.get("UPDATE_USER")));
                rldxDetails.setLastUpdateTime(
                (Timestamp)rolodexRow.get("UPDATE_TIMESTAMP"));
                rldxDetails.setCreateUser( (String)
                rolodexRow.get("CREATE_USER")));
                if ( (rldxDetails.getSponsorCode() != null ) &&
                (!rldxDetails.getSponsorCode().equals("")) ) {
                    rldxDetails.setSponsorName(getSponsorName(
                    rldxDetails.getSponsorCode()));
                }
                rldxDetails.setStates(getStates());
                rldxDetails.setCountries(getCountries());
                if (isModify) {
                    rldxDetails.setRefId( (String) rolodexRow.get(DBEngineConstants.LOCK_REFERENCE)));
                    //System.out.println("The reference id for this connection " + rolodexRow.get(DBEngineConstants.LOCK_REFERENCE));
                }

            }
            //            }

        }
        return rldxDetails;
    }
     */
    /**
     *  overridden method to implement rowlocking by using Transaction monitor
     *  @author Geo
     */
    public RolodexDetailsBean getRolodexMaintenanceDetails(String rolodexid,
            char functionType) throws Exception{
        RolodexDetailsBean rowBean=getRolodexMaintenanceDetails(rolodexid);
        String rowId = rowLockStr+rowBean.getRolodexId();
        if(transMon.canEdit(rowId))
            return rowBean;
        else
            throw new CoeusException("exceptionCode.999999");
    }
    // Code added by Shivakumar for locking enhancement - BEGIN
    public LockingBean getRolodexLock(String rolodexid,
          String loggedinUser,String unitNumber) throws CoeusException,DBException{
          RolodexDetailsBean rowBean=getRolodexMaintenanceDetails(rolodexid);
          String rowId = rowLockStr+rowBean.getRolodexId();
          dbEngine = new DBEngineImpl();
//          if(dbEngine != null){
//              conn = dbEngine.beginTxn();              
//          }else{
//               throw new CoeusException("db_exceptionCode.1000");
//          }     
          LockingBean lockingBean = transMon.canEdit(rowId,loggedinUser,unitNumber);
          return lockingBean;
    }            
    public RolodexDetailsBean getNewRolodexMaintenanceDetails(String rolodexid,
            char functionType) throws Exception{
        RolodexDetailsBean rowBean=getRolodexMaintenanceDetails(rolodexid);
        String rowId = rowLockStr+rowBean.getRolodexId();
        return rowBean;       
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
        
      public boolean lockCheck(String rolodexId, String loggedinUser)
        throws CoeusException, DBException{      
        RolodexDetailsBean rowBean=getRolodexMaintenanceDetails(rolodexId);    
        String rowId = this.rowLockStr+rowBean.getRolodexId();
        boolean lockCheck = transMon.lockAvailabilityCheck(rowId,loggedinUser);
        return lockCheck;
     }
      // New lock method added for locking whenever new Rolodex as been created.
     public LockingBean getLock(String rolodexid,
          String loggedinUser,String unitNumber) throws CoeusException,DBException{
          RolodexDetailsBean rowBean=getRolodexMaintenanceDetails(rolodexid);
          String rowId = rowLockStr+rowBean.getRolodexId();
          dbEngine = new DBEngineImpl();
          if(dbEngine != null){
              conn = dbEngine.beginTxn();              
          }else{
               throw new CoeusException("db_exceptionCode.1000");
          }               
          LockingBean lockingBean = transMon.newLock(rowId, loggedinUser, unitNumber, conn);
          transactionCommit();
          return lockingBean;
    }
    // Code added by Shivakumar for locking enhancement - END

    /**
     *  Method used to get all the details of Roldex details.
     *  <li>To fetch the data, it uses select_Rolodex procedure.
     *  @param String rolodexId
     *  @return RolodexDetails
     *  @exception DBException
     *
     */

    public RolodexDetailsBean getRolodexMaintenanceDetails(String rolodexid)
    throws  CoeusException, DBException{

        Vector vecResult;

        if (rolodexid == null){
            //throw an exception
        }

        vecResult = new Vector();
        Vector  param = new Vector();
        rldxDetails = new RolodexDetailsBean();


        param.addElement(new Parameter("ROLODEXID",
        DBEngineConstants.TYPE_STRING,rolodexid));
        if (dbEngine != null) {
            vecResult =
            dbEngine.executeRequest("Coeus",
            "call GET_ROLODEX( << ROLODEXID >> , << OUT RESULTSET rset >> )",
            "Coeus", param);
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }

        if (!vecResult.isEmpty()) {
            HashMap rolodexRow = (HashMap)vecResult.elementAt(0);
            //System.out.println("rolodexid in server:"+rolodexRow.get("ROLODEX_ID").toString());
            rldxDetails.setRolodexId( rolodexRow.get("ROLODEX_ID").toString());
            //System.out.println("rolodexid in server from bean:"+rldxDetails.getRolodexId());
            rldxDetails.setLastName( (String) rolodexRow.get("LAST_NAME"));
            rldxDetails.setMiddleName( (String) rolodexRow.get("MIDDLE_NAME"));
            rldxDetails.setFirstName( (String) rolodexRow.get("FIRST_NAME"));
            rldxDetails.setSuffix( (String) rolodexRow.get("SUFFIX"));
            rldxDetails.setPrefix( (String) rolodexRow.get("PREFIX"));
            rldxDetails.setTitle( (String) rolodexRow.get("TITLE"));
            rldxDetails.setOrganization( (String) rolodexRow.get("ORGANIZATION"));
            //COEUSQA-1528 Add the functionality to set a status on a Rolodex entry START 
            if(rolodexRow.get("ROLODEX_STATUS")!=null)
                rldxDetails.setStatus((String) rolodexRow.get("ROLODEX_STATUS"));
            //COEUSQA-1528 Add the functionality to set a status on a Rolodex entry END 
            rldxDetails.setAddress1( (String) rolodexRow.get("ADDRESS_LINE_1"));
            rldxDetails.setAddress2( (String) rolodexRow.get("ADDRESS_LINE_2"));
            rldxDetails.setAddress3( (String) rolodexRow.get("ADDRESS_LINE_3"));
            rldxDetails.setFax( (String) rolodexRow.get("FAX_NUMBER"));
            rldxDetails.setEMail( (String) rolodexRow.get("EMAIL_ADDRESS"));
            rldxDetails.setCity( (String) rolodexRow.get("CITY"));
            rldxDetails.setCounty( (String) rolodexRow.get("COUNTY"));
            rldxDetails.setState( (String) rolodexRow.get("STATE"));
            rldxDetails.setPostalCode( (String) rolodexRow.get("POSTAL_CODE"));
            rldxDetails.setComments( (String) rolodexRow.get("COMMENTS"));
            rldxDetails.setPhone( (String) rolodexRow.get("PHONE_NUMBER"));
            //Modified by shiji for fixing bug id : 1843 - start
            rldxDetails.setCountry(rolodexRow.get("COUNTRY_CODE")== null ? "" :(String) rolodexRow.get("COUNTRY_CODE"));
            rldxDetails.setCountryName( rolodexRow.get("COUNTRY_NAME")==null ? "" : (String)rolodexRow.get("COUNTRY_NAME"));
            //bug id : 1843 - end
            rldxDetails.setSponsorCode( (String) rolodexRow.get("SPONSOR_CODE"));
            rldxDetails.setSponsorAddressFlag( (String) rolodexRow.get("SPONSOR_ADDRESS_FLAG"));
            rldxDetails.setOwnedByUnit( (String) rolodexRow.get("OWNED_BY_UNIT"));
            rldxDetails.setLastUpdateUser( (String) rolodexRow.get("UPDATE_USER"));
            rldxDetails.setLastUpdateTime(
                (Timestamp)rolodexRow.get("UPDATE_TIMESTAMP"));
            rldxDetails.setCreateUser( (String) rolodexRow.get("CREATE_USER"));
            if ( (rldxDetails.getSponsorCode() != null ) &&
            (!rldxDetails.getSponsorCode().equals("")) ) {
                rldxDetails.setSponsorName(getSponsorName(
                rldxDetails.getSponsorCode()));
            }
            rldxDetails.setStates(getStates());
            rldxDetails.setCountries(getCountries());
           
        }
        return rldxDetails;
    }

    private final String rowLockStr = "osp$Rolodex_";

    /**
     * Overridden method for implementing rowlocking by using transaction monitor.
     * @author Geo
     */
    public boolean addUpdateRolodexMaintenanceDetails(RolodexDetailsBean rldxBean,
            char functionType) throws CoeusException,DBException{
        String rowId = rowLockStr + rldxBean.getRolodexId();
        boolean success=addUpdateRolodexMaintenanceDetails(rldxBean);
        if((success==true)&&(functionType=='U'))
        {
              transMon.releaseEdit(rowId);
              return true;
          }
        return success;

    }
    
    // Code added by Shivakumar for locking enhancement - BEGIN
//    public boolean addUpdateRolodexMaintenanceDetails(RolodexDetailsBean rldxBean,
//            char functionType,String loggedinUser) throws CoeusException,DBException{
//        String rowId = rowLockStr + rldxBean.getRolodexId();
//        boolean success=addUpdateRolodexMaintenanceDetails(rldxBean);
//        if((success==true)&&(functionType=='U'))
//        {
//              transMon.releaseEdit(rowId,loggedinUser);
//              return true;
//          }
//        return success;
//
//    }
    // Created new addUpdateRolodexMaintenanceDetails for bug fixing in locking
    public LockingBean addUpdateRolodexMaintenanceDetails(RolodexDetailsBean rldxBean,
            char functionType,String loggedinUser) throws CoeusException,DBException{
        String rowId = rowLockStr + rldxBean.getRolodexId();
        LockingBean lockingBean = new LockingBean();
        boolean success=addUpdateRolodexMaintenanceDetails(rldxBean);
        boolean lockCheck = transMon.lockAvailabilityCheck(rowId, loggedinUser);
        //System.out.println("Function type "+functionType);
        if((!lockCheck) && (rldxBean.getAcType().equals("U"))){
              lockingBean = transMon.releaseLock(rowId,loggedinUser);
              //return true;
          }
        if(rldxBean.getAcType().equals("I")){
              boolean newLockCheck = true;
              lockingBean.setGotLock(newLockCheck);
          }    
        return lockingBean;

    }
    // Code added by Shivakumar for locking enhancement - END

    /**
     *  Method used to save all the details of Roldex Information.
     *  <li>To save the data, it uses select_Rolodex procedure.
     *  @param String rolodexId
     *  @return RolodexDetails
     *  @exception DBException
     *
     */

    public boolean addUpdateRolodexMaintenanceDetails(RolodexDetailsBean rldxBean)
    throws  CoeusException, DBException{

        String rldxId="";
        String deleteFlag ="";
        String sponsorAddressFlag = "";
        String ownedByUnit = "";
        Vector param = new Vector();
        Vector procedures = new Vector(5,3);
        //String refId = (rldxBean.getRefId() == null ? null : (rldxBean.getRefId().equalsIgnoreCase("")? null : rldxBean.getRefId()));
        ////System.out.println("RolodexMaintenanceDataTxnBean >>>>>the ref id is " + refId);
        Timestamp currentDateTime = this.getTimestamp();
        param.addElement(new Parameter("ROLODEXID",
        DBEngineConstants.TYPE_INT,rldxBean.getRolodexId()));
        param.addElement(new Parameter("LASTNAME",
        DBEngineConstants.TYPE_STRING,rldxBean.getLastName()));
        param.addElement(new Parameter("FIRSTNAME",
        DBEngineConstants.TYPE_STRING,rldxBean.getFirstName()));
        param.addElement(new Parameter("MIDDLENAME",
        DBEngineConstants.TYPE_STRING,rldxBean.getMiddleName()));
        param.addElement(new Parameter("SUFFIX",
        DBEngineConstants.TYPE_STRING,rldxBean.getSuffix()));
        param.addElement(new Parameter("PREFIX",
        DBEngineConstants.TYPE_STRING,rldxBean.getPrefix()));
        param.addElement(new Parameter("TITLE",
        DBEngineConstants.TYPE_STRING,rldxBean.getTitle()));
        param.addElement(new Parameter("ORGANIZATION",
        DBEngineConstants.TYPE_STRING,rldxBean.getOrganization()));
        //COEUSQA-1528 Add the functionality to set a status on a Rolodex entry START 
        param.addElement(new Parameter("ROLODEX_STATUS",DBEngineConstants.TYPE_STRING,rldxBean.getStatus()));
        //COEUSQA-1528 Add the functionality to set a status on a Rolodex entry END 
        param.addElement(new Parameter("ADDRESS1",
        DBEngineConstants.TYPE_STRING,rldxBean.getAddress1()));
        param.addElement(new Parameter("ADDRESS2",
        DBEngineConstants.TYPE_STRING,rldxBean.getAddress2()));
        param.addElement(new Parameter("ADDRESS3",
        DBEngineConstants.TYPE_STRING,rldxBean.getAddress3()));
        param.addElement(new Parameter("FAX",
        DBEngineConstants.TYPE_STRING,rldxBean.getFax()));
        param.addElement(new Parameter("CITY",
        DBEngineConstants.TYPE_STRING,rldxBean.getCity()));
        param.addElement(new Parameter("COUNTY",
        DBEngineConstants.TYPE_STRING,rldxBean.getCounty()));
        param.addElement(new Parameter("STATE",
        DBEngineConstants.TYPE_STRING,rldxBean.getState()));
        param.addElement(new Parameter("POSTALCODE",
        DBEngineConstants.TYPE_STRING,rldxBean.getPostalCode()));
        param.addElement(new Parameter("PHONE",
        DBEngineConstants.TYPE_STRING,rldxBean.getPhone()));
        param.addElement(new Parameter("EMAIL",
        DBEngineConstants.TYPE_STRING,rldxBean.getEMail()));
        param.addElement(new Parameter("COUNTRY",
        DBEngineConstants.TYPE_STRING,rldxBean.getCountry()));
        param.addElement(new Parameter("COMMENTS",
        DBEngineConstants.TYPE_STRING,rldxBean.getComments()));
        param.addElement(new Parameter("SPONSORCODE",
        DBEngineConstants.TYPE_STRING,rldxBean.getSponsorCode()));
        param.addElement(new Parameter("DELETE_FLAG",
        DBEngineConstants.TYPE_STRING,rldxBean.getDeleteFlag()));
        param.addElement(new Parameter("SPONSOR_ADDRESS_FLAG",
        DBEngineConstants.TYPE_STRING,rldxBean.getSponsorAddressFlag()));
        param.addElement(new Parameter("OWNED_BY_UNIT",
        DBEngineConstants.TYPE_STRING,rldxBean.getOwnedByUnit()));
        param.addElement(new Parameter("UPDATE_USER",
        DBEngineConstants.TYPE_STRING,userId));
        param.addElement(new Parameter("UPDATE_TIMESTAMP",
        DBEngineConstants.TYPE_DATE,currentDateTime));
        param.addElement(new Parameter("ROLODEXID",
        DBEngineConstants.TYPE_STRING,rldxBean.getRolodexId()));
        param.addElement(new Parameter("LASTUPDATEUSER",
        DBEngineConstants.TYPE_STRING,rldxBean.getLastUpdateUser()));
        param.addElement(new Parameter("LASTUPDATETIME",
        DBEngineConstants.TYPE_DATE,rldxBean.getLastUpdateTime()));        
        param.addElement(new Parameter("AC_TYPE",
        DBEngineConstants.TYPE_STRING,rldxBean.getAcType()));
      
        
        StringBuffer strqry = new StringBuffer("call upd_rolodex ( ");
        strqry.append(" << ROLODEXID >> ,");
        strqry.append(" << LASTNAME >> ,");
        strqry.append(" << FIRSTNAME >> ,");
        strqry.append(" << MIDDLENAME >> ,");
        strqry.append(" << SUFFIX >> ,");
        strqry.append(" << PREFIX >> ,");
        strqry.append(" << TITLE >> ,");
        strqry.append(" << ORGANIZATION >> ,");
        //COEUSQA-1528 Add the functionality to set a status on a Rolodex entry START 
        strqry.append(" << ROLODEX_STATUS >> ,");
        //COEUSQA-1528 Add the functionality to set a status on a Rolodex entry END  
        strqry.append(" << ADDRESS1 >> ,");
        strqry.append(" << ADDRESS2 >> ,");
        strqry.append(" << ADDRESS3 >> ,");
        strqry.append(" << FAX >> ,");
        strqry.append(" << CITY >> ,");
        strqry.append(" << COUNTY >> ,");
        strqry.append(" << STATE >> ,");
        strqry.append(" << POSTALCODE >> ,");
        strqry.append(" << PHONE >> ,");
        strqry.append(" << EMAIL >> ,");
        strqry.append(" << COUNTRY >> ,");
        strqry.append(" << COMMENTS >> ,");
        strqry.append(" << SPONSORCODE >> ,");
        strqry.append(" << DELETE_FLAG >> ,");
        strqry.append(" << SPONSOR_ADDRESS_FLAG >> ,");
        strqry.append(" << OWNED_BY_UNIT >> ,");
        strqry.append(" << UPDATE_USER >> ,");
        strqry.append(" << UPDATE_TIMESTAMP >> ,");        
        strqry.append(" << ROLODEXID >> ,");
        strqry.append(" << LASTUPDATEUSER >> ,");
        strqry.append(" << LASTUPDATETIME >> ,");
        strqry.append(" << AC_TYPE >> )");
        

        if (dbEngine != null ) {
            /*if( refId != null) {
                if (dbEngine.isUpdConnectionAvailable("Coeus",refId)) {
                    //dbEngine.executeStoreProcs(procedures);
                    dbEngine.executeRequest("Coeus",strqry,
                    "Coeus", param,false,refId);


                }else {
                    throw new DBException("db_exceptionCode.1111");
                }
            }else {
                dbEngine.executeRequest("Coeus",strqry,
                "Coeus", param,false,refId);
            }
             */
            dbEngine.executeRequest("Coeus",strqry.toString(),"Coeus", param);
        }else {
            throw new CoeusException
            ("db_exceptionCode.1000");
        }

        return true;
    }

    /**
     *  Method used to fetch the next Rolodex ID for Rolodex Details.
     *  This method is used when adding a new Rolodex Info .
     *  <li>To fetch the data, it uses fn_get_rolodex_id procedure.
     *  @return String Next Sequence Number
     *  @exception DBException
     */
    public String getNextSeqNum() throws  CoeusException, DBException{
        String seqNum = null;
        Vector param= new java.util.Vector();
        Vector result = new Vector();
        //calling stored function
        if(dbEngine!=null){
            result = dbEngine.executeFunctions("Coeus",
            "{ <<OUT INTEGER SEQNUMBER>> = call fn_get_rolodex_id() }", param);
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }
        if(!result.isEmpty()){
            HashMap nextNumRow = (HashMap)result.elementAt(0);
            seqNum = (String)nextNumRow.get("SEQNUMBER");
        }
        return seqNum;
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
    public Timestamp getTimestamp() throws  CoeusException, DBException{
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
     *  This method populates the list box meant to retrieve the countries in
     *  the rolodex details screen.
     *  To fetch the data, it uses the procedure dw_get_state_codes.
     *  @return vector list of all states with state code as key and
     *  state name as value.
     *  @exception DBException
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
            states.addElement(new ComboBoxBean(
            (String) stateRow.get("STATE_CODE"),
            (String) stateRow.get("DESCRIPTION")));

        }
        return states;
    }

    /**
     *  This method populates the list box meant to retrieve the countries in
     *  the rolodex details screen.
     *  To fetch the data, it uses the procedure dw_get_country_code.
     *  @return vector list of all countries with country code as key and
     *      country name as value.
     *  @exception DBException
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
            (String) countryRow.get("COUNTRY_CODE"),
            (String) countryRow.get("COUNTRY_NAME")));
        }
        return countries;
    }

    /**
     *  Method used to fetch the Sponsor Name for Sponsor Code.
     *  This method is used to check Sponsor Name to Rolodex Info Organization
     *  info.
     *  <li>To fetch the data, it uses get_Sponsor_Name function.
     *  @return String Sponsor Name
     *  @exception DBException
     */


    public String getSponsorName(String sponsorCode) throws CoeusException,
    DBException{
        String sponsorName = "";
        Vector param= new java.util.Vector();
        Vector result = new Vector();
        param.add(new Parameter("SPONSORCODE",DBEngineConstants.TYPE_STRING,
        sponsorCode));
        //calling stored function
        if(dbEngine!=null){
            result = dbEngine.executeFunctions("Coeus",
            "{ <<OUT STRING SPONSORNAME>> = call get_sponsor_name_new( << SPONSORCODE >> ) }",
            param);
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }
        if(!result.isEmpty()){
            HashMap rowSponsorName = (HashMap)result.elementAt(0);
            sponsorName = (String) rowSponsorName.get("SPONSORNAME");
        }
        return sponsorName;

    }
    
    /**
     *  Method used to fetch the valid Sponsor code for Sponsor Code.
     *  This method is used to get the valid Sponsor code from the osp$sponser table.
     *  <li>To fetch the data, it uses get_valid_sponsor_code function.
     *  @return String Sponsor code
     *  @exception DBException
     */


    public String getValidSponsorCode(String sponsorCode) throws CoeusException,
    DBException{
        String validSponsorCode = "";
        Vector param= new java.util.Vector();
        Vector result = new Vector();
        param.add(new Parameter("SPONSORCODE",DBEngineConstants.TYPE_STRING,
        sponsorCode));
        //calling stored function
        if(dbEngine!=null){
            result = dbEngine.executeFunctions("Coeus",
            "{ <<OUT STRING SPONSORCODE>> = call get_valid_sponsor_code( << SPONSORCODE >> ) }",
            param);
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }
        if(!result.isEmpty()){
            HashMap rowSponsorName = (HashMap)result.elementAt(0);
            validSponsorCode = (String) rowSponsorName.get("SPONSORCODE");
        }
        return validSponsorCode;

    }

    /**
     *  Method used to fetch the Rolodex Id for Sponsor Code.
     *  This method is used to fetch Rolodex Id  Info Organization info.
     *  <li>To fetch the data, it uses get_rolodexid_in_sponsor function.
     *  @return String Rolodex Id
     *  @exception DBException
     */

    public String getRolodexIdForSponsor(String sponsorCode) throws CoeusException,
    DBException {
        String rolodexId = null;
        Vector param= new java.util.Vector();
        Vector result = new Vector();
        param.add(new Parameter("SPONSORCODE",DBEngineConstants.TYPE_STRING,
        sponsorCode));
        if(dbEngine!=null){
            result = dbEngine.executeRequest("Coeus",
            "call get_rolodexid_in_sponsor(  << SPONSORCODE >> , << OUT INTEGER ROLODEXID >> ) ",
            "Coeus",param);
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }
        if(!result.isEmpty()){
            HashMap rowRolodexId = (HashMap)result.elementAt(0);
            rolodexId = (String) rowRolodexId.get("ROLODEXID");
        }
        return rolodexId;

    }


    /**
     *  Method used to check whether the user has right to modify or delete.
     *  This method is used to check before allowing to modify or delete.
     *  <li>To fetch the data, it uses fn_user_has_right procedure.
     *  @return boolean isUserHasRight
     *  @exception DBException
     */

    public boolean isUserHasRight(String user,
    String unitNumber,String rolodexRight)
    throws CoeusException, DBException {
        int userRight = 0;
        boolean hasRight =false;
        Vector param= new java.util.Vector();
        Vector result = new Vector();
        param.add(new Parameter("USER",DBEngineConstants.TYPE_STRING,user));
        param.add(new Parameter("UNITNUMBER",DBEngineConstants.TYPE_STRING,
        unitNumber));
        param.add(new Parameter("ROLODEXRIGHT",DBEngineConstants.TYPE_STRING,
        rolodexRight));
        //calling stored function
        if(dbEngine!=null){
            result = dbEngine.executeFunctions("Coeus",
            "{ <<OUT INTEGER RIGHTEXISTS>> = call fn_user_has_right( << USER >> , << UNITNUMBER >> , << ROLODEXRIGHT >> ) }",
            param);
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }
        if(!result.isEmpty()){
            HashMap rowRolodexId = (HashMap)result.elementAt(0);
            userRight = Integer.parseInt(
            rowRolodexId.get("RIGHTEXISTS").toString().trim());
        }
        if ( userRight == 1 )  {
            hasRight = true;
        }else if ( userRight == 0)    {
            hasRight =false;
        }
        return hasRight;

    }


    /**
     *  Method used to check whether rolodex id is used in any other module.
     *  <li>To check the info, it uses check_rolodexid_is_used function.
     *  @return Vector which includes the information regarding the right and
     *  where it is used in other tables.
     *  @exception DBException
     */

    public Vector checkRolodexIdIsUsed(String rolodexId,String userName,
    int maintainFlag) throws
    CoeusException, DBException {
        Vector resultData = new java.util.Vector();
        Vector param= new java.util.Vector();
        Vector result = new Vector();
        String strMaintainFlag = (new Integer(maintainFlag).toString());
        String okFlag = "";
        String usedInfo = "";
        param.add(new Parameter("ROLODEXID",
        DBEngineConstants.TYPE_INT,rolodexId));
        param.add(new Parameter("USERNAME",
        DBEngineConstants.TYPE_STRING,userName));
        param.add(new Parameter("MAINTAINFLAG",
        DBEngineConstants.TYPE_INT,strMaintainFlag));

        //calling stored function
        if(dbEngine!=null){
            result = dbEngine.executeRequest("Coeus",
            "call check_rolodexid_is_used( << ROLODEXID >> , << USERNAME >> , << MAINTAINFLAG >>  , << OUT STRING USEDINFO >> , << OUT INTEGER OKFLAG >> ) ",
            "Coeus", param);
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }
        if(!result.isEmpty()){
            HashMap rowRolodexId = (HashMap)result.elementAt(0);
            okFlag = (String) rowRolodexId.get("OKFLAG");
            usedInfo = (String) rowRolodexId.get("USEDINFO");
        }
        resultData.add(okFlag);
        resultData.add(usedInfo);

        return resultData;
    }

    public boolean canDeleteRolodex(String rolodexId)
            throws CoeusException, DBException{
        String rowId = rowLockStr + rolodexId;
        if(!transMon.canDelete(rowId)){
            throw new CoeusException("Delete_exceptionCode.444444");
        }
        return true;
    }
    
    // Code added by Shivakumar for locking enhancement - BEGIN
    public boolean canDeleteRolodex(String rolodexId,String loggedinUser)
            throws CoeusException, DBException{
        String rowId = rowLockStr + rolodexId;        
        if(!transMon.lockAvailabilityCheck(rowId,loggedinUser)){
            throw new CoeusException("Delete_exceptionCode.444444");
        }
        return true;
    }
    
    // Code added by Shivakumar for locking enhancement - END
    
    /**
     *  This method is used to delete the Rolodex Information from the database.
     *  To fetch the data, it uses the procedure delete_rolodex.
     *  @return boolean whether deletion is successful or not.
     *  @exception DBException
     *  @exception SQLException
     *  @exception Exception
     */
    public boolean deleteRolodex(String rolodexId)
        throws CoeusException, DBException{
        Vector result = new Vector(3,2);
        Vector param= new Vector();
        param.add(new Parameter("ROLODEXID",
        DBEngineConstants.TYPE_INT,rolodexId));
        if(dbEngine!=null){
            result = dbEngine.executeRequest("Coeus",
            "call delete_rolodex( << ROLODEXID >> )", "Coeus", param);
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }
        return true;
    }

    /**
     * get the rolodex name
     * This method executes the procedure to get the rolodex name
     * for a particular rolodex id
     *
     * @return String contains  rolodex name
     */
    public String getRolodexName(String rolodexId)
    throws  CoeusException, DBException{
        String rolodexName=null;
        if (rolodexId!=null){
            // keep the stored procedure result in a vector
            Vector result = null;
            // keep the parameters for the stored procedure in a vector
            Vector param = new Vector();
            // add the organization id parameter into param vector
            param.addElement(new Parameter("ROLODEX_ID",
            DBEngineConstants.TYPE_STRING, rolodexId));
            // execute the stored procedure
            if (dbEngine != null) {
                result = dbEngine.executeRequest("Coeus",
                "call get_name_from_rolodex  ( <<ROLODEX_ID>> , <<OUT STRING TEMP>> )",
                "Coeus", param);
            } else {
                throw new CoeusException("db_exceptionCode.1000");
            }
            HashMap orgDetailsRow = (HashMap) result.elementAt(0);
            rolodexName = (String) orgDetailsRow.get("TEMP");
        }
        return rolodexName;
    }

    public void releaseEdit(String rowId){
        transMon.releaseEdit(this.rowLockStr+rowId);
    }
    
    // Code added by Shivakumar  for locking enhancement - BEGIN 
//        public void releaseEdit(String rowId,String loggedinUser) 
//            throws CoeusException,DBException{
//            transMon.releaseEdit(this.rowLockStr+rowId,loggedinUser);
//        }
        // Calling releaseLock method for fixing bug in locking
        public LockingBean releaseLock(String rowId,String loggedinUser) 
            throws CoeusException,DBException{
            LockingBean lockingBean = new LockingBean();
            boolean lockCheck = transMon.lockAvailabilityCheck(this.rowLockStr+rowId, loggedinUser);
            if(!lockCheck){
                lockingBean = transMon.releaseLock(this.rowLockStr+rowId,loggedinUser);
            }
            return lockingBean;
        }
    // Code added by Shivakumar  for locking enhancement - END

/*    public void releaseUpdateLock(String refId)
        throws CoeusException,DBException   {
        //            String refId = null;
        //           refId = rdBean.getRefId();
        if (dbEngine != null){
            ////System.out.println("Txn Bean >>>>calling DBEngine to release the locked connection");
            dbEngine.releaseLockedConnection("Coeus",refId);
        }else {
            throw new CoeusException("dbExceptionCode.1111");
        }
    }
 */
    /**
     * Gets all Rolodex References for the given Rolodex Id
     * <li>To fetch the data, it uses the procedure GET_ROLODEX_REFERENCES.
     *
     * @param rolodexId Rolodex Id
     * @return CoeusVector of RolodexReferencesBeans
     * @exception DBException if any error during database transaction.
     * @exception CoeusException if the instance of dbEngine is not available.
     */    
    public CoeusVector getRolodexReferences(int rolodexId)
    throws  CoeusException, DBException{
        
        Vector vecResult;
        vecResult = new Vector();
        Vector  param = new Vector();
        RolodexReferencesBean rolodexReferencesBean = null;
        CoeusVector referencesList = null;
        
        param.addElement(new Parameter("ROLODEX_ID",
            DBEngineConstants.TYPE_STRING, ""+rolodexId));
        
        if (dbEngine != null) {
            vecResult = dbEngine.executeRequest("Coeus",
                "call GET_ROLODEX_REFERENCES( << ROLODEX_ID >> , << OUT RESULTSET rset >> )",
                "Coeus", param);
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }

        if (vecResult!=null && vecResult.size() > 0) {
            HashMap referencesRow ;
            referencesList = new CoeusVector();
            for(int row = 0; row < vecResult.size(); row++){
                referencesRow = (HashMap)vecResult.elementAt(row);
                rolodexReferencesBean = new RolodexReferencesBean();
                rolodexReferencesBean.setCurrentValue(""+rolodexId);
                rolodexReferencesBean.setTableName((String)referencesRow.get("TABLE_NAME"));
                rolodexReferencesBean.setColumnName((String)referencesRow.get("COLUMN_NAME"));
                rolodexReferencesBean.setCount(referencesRow.get("COUNT")==null ? 0 : Integer.parseInt(referencesRow.get("COUNT").toString()));
                rolodexReferencesBean.setColumnType((String)referencesRow.get("COLUMN_TYPE"));
                referencesList.addElement(rolodexReferencesBean);
            }
        }
        return referencesList;
    }
    
    /**
     *  Method used to replace the Column Value with a different one.
     *  This is used in the Replace functionality of Rolodex References
     *  
     *  <li>To fetch the data, it uses FN_REPLACE_COLUMN_VALUE procedure.
     *  @return int indicating whether update was successfull
     *  @exception DBException if any error during database transaction.
     *  @exception CoeusException if the instance of dbEngine is not available.
     */
    public int replaceColumnValue(RolodexReferencesBean rolodexReferencesBean)
            throws CoeusException, DBException{
        int updated = 0;
        Vector param= new java.util.Vector();
        Vector result = new Vector();
        //calling stored function
        
        param.addElement(new Parameter("TABLE_NAME",
            DBEngineConstants.TYPE_STRING, rolodexReferencesBean.getTableName()));
        param.addElement(new Parameter("COLUMN_NAME",
            DBEngineConstants.TYPE_STRING, rolodexReferencesBean.getColumnName()));
        param.addElement(new Parameter("CURRENT_VALUE",
            DBEngineConstants.TYPE_STRING, rolodexReferencesBean.getCurrentValue()));
        param.addElement(new Parameter("NEW_VALUE",
            DBEngineConstants.TYPE_STRING, rolodexReferencesBean.getNewValue()));
        param.addElement(new Parameter("COLUMN_TYPE",
            DBEngineConstants.TYPE_STRING, rolodexReferencesBean.getColumnType()));        
        if(dbEngine!=null){
            result = dbEngine.executeFunctions("Coeus",
            "{ <<OUT INTEGER IS_UPDATED>> = call FN_REPLACE_COLUMN_VALUE (<< TABLE_NAME >>, << COLUMN_NAME >>, << CURRENT_VALUE >>, << NEW_VALUE >>, << COLUMN_TYPE >> ) }", param);
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }
        if(!result.isEmpty()){
            HashMap replaceColumn = (HashMap)result.elementAt(0);
            updated = Integer.parseInt(replaceColumn.get("IS_UPDATED").toString());
        }
        return updated;
    }
     //coeusqa-1563  start 
     public String getRolodexFullName(String rolodexFullName) throws CoeusException,
    DBException{
        String finalRolodexFullName="";
        Vector param= new java.util.Vector();
        Vector result = new Vector();
        param.add(new Parameter("AS_ROLODEXNAME",DBEngineConstants.TYPE_STRING,rolodexFullName));
        //calling stored function
        if(dbEngine!=null){
            result = dbEngine.executeRequest("Coeus",
            "call GET_ROLODEX_INFO_NAME (  <<AS_ROLODEXNAME>> ,<< OUT STRING TITLE >> )   ",
            "Coeus", param);
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }
        if(!result.isEmpty()){
            HashMap rowrolodexName = (HashMap)result.elementAt(0);
            finalRolodexFullName = (String) rowrolodexName.get("TITLE");
        }
        return finalRolodexFullName;

    }
      //coeusqa-1563  end 
    public static void main(String args[]){
        try{
            RolodexMaintenanceDataTxnBean rolodexMaintenanceDataTxnBean = new RolodexMaintenanceDataTxnBean();
            CoeusVector coeusVector = rolodexMaintenanceDataTxnBean.getRolodexReferences(288);
            if(coeusVector!=null){
                System.out.println("Size : "+coeusVector.size());
            }
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    /**
     *  Method used to fetch the valid Sponsor code for Sponsor Code.
     *  This method is used to get the valid Sponsor code from the osp$sponser table.
     *  <li>To fetch the data, it uses FN_CHECK_VALID_SPONSOR_CODE function.
     *  @return String Sponsor code
     *  @exception DBException
     */
    public String getCodeForValidSponsor(String sponsorCode) throws CoeusException, DBException{
        String validSponsorCode = "";
        Vector param= new java.util.Vector();
        Vector result = new Vector();
        param.add(new Parameter("SPONSORCODE",DBEngineConstants.TYPE_STRING,
                sponsorCode));
        //calling stored function
        if(dbEngine!=null){
            result = dbEngine.executeFunctions("Coeus",
                    "{ <<OUT STRING SPONSORCODE>> = call FN_CHECK_VALID_SPONSOR_CODE( << SPONSORCODE >> ) }",
                    param);
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }
        if(!result.isEmpty()){
            HashMap rowSponsorName = (HashMap)result.elementAt(0);
            validSponsorCode = (String) rowSponsorName.get("SPONSORCODE");
        }
        return validSponsorCode;
    }
   
}