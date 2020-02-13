/*
 * @(#)CodeTableTxnBean.java
 *
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 *
 */

package edu.mit.coeus.codetable.bean;

import edu.mit.coeus.bean.CoeusTypeBean;
import edu.mit.coeus.utils.CoeusVector;
import edu.mit.coeus.utils.UtilFactory;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Serializable;
import java.net.URISyntaxException;
import java.net.URL;
import java.sql.Timestamp;
import java.util.Date;
import java.util.HashMap;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.HashSet;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.Vector;


import edu.mit.coeus.utils.dbengine.DBEngineConstants;
import edu.mit.coeus.utils.dbengine.DBEngineImpl;
import edu.mit.coeus.utils.dbengine.DBException;
import edu.mit.coeus.utils.dbengine.Parameter;
import edu.mit.coeus.bean.CoeusMessageResourcesBean;

import edu.mit.coeus.exception.CoeusException;


/**
 * Component for accessing and updating code table data.
 * For communication between the Server and the database, using DBEngine.
 * Constructor instantiates DBEngineImpl.
 */
public class CodeTableTxnBean implements Serializable
{

  /**
   * DBEngineImpl instance, for accessing and updating database.
   * @see edu.mit.coeus.utils.dbengine.DBEngineImpl
   */
  private DBEngineImpl dbEngine;
  //COEUSQA:1691 - Front End Configurations to CoeusLite Pages - Start
  private final String UPDATE_ARRA_MESSAGE = "UPDATE_ARRA_MESSAGE";
  private final String UPDATE_BUDGET_MESSAGE = "UPDATE_BUDGET_MESSAGE";
  private final String UPDATE_COI_MESSAGE = "UPDATE_COI_MESSAGE";
  private final String UPDATE_NEGOTIATION_MESSAGE = "UPDATE_NEGOTIATION_MESSAGE";
  private final String UPDATE_PROPOSAL_MESSAGE = "UPDATE_PROPOSAL_MESSAGE";
  private final String UPDATE_IRB_PROTOCOL_MESSAGE =  "UPDATE_IRB_PROTOCOL_MESSAGE";
  private final String UPDATE_AC_PROTOCOL_MESSAGE = "UPDATE_AC_PROTOCOL_MESSAGE";
  private final String UPDATE_SUBCONTRACT_MESSAGE = "UPDATE_SUBCONTRACT_MESSAGE";
  
  private final String ARRA_MESSAGE_PROPERTY = "ArraMessages.properties";
  private final String BUDGET_MESSAGE_PROPERTY = "BudgetMessages.properties";
  private final String IRB_PROTO_MESSAGE_PROPERTY = "IRBMessages.properties";
  private final String AC_PROTO_MESSAGE_PROPERTY = "IACUCProtocolMessages.properties";
  private final String NEGOTIATION_MESSAGE_PROPERTY = "NegotiationMessages.properties";
  private final String PROPOSAL_MESSAGE_PROPERTY = "ProposalMessages.properties";
  private final String SUB_CONTRACT_MESSAGE_PROPERTY = "SubcontractMessages.properties";
  private final String COI_MESSAGE_PROPERTY = "COIMessages.properties";
  private final String PROPERTY_FILE_UPDATED = "PropertiesFileStatus.properties";
  
  private final String ARRA_MESSAGE_PROCEDURE = "GET_ARRA_MESSAGE_LIST";
  private final String BUDGET_MESSAGE_PROCEDURE = "GET_BUDGET_MESSAGE_LIST";
  private final String IRB_PROTO_MESSAGE_PROCEDURE = "GET_IRB_PROTOCOL_MESSAGE_LIST";
  private final String AC_PROTO_MESSAGE_PROCEDURE = "GET_AC_PROTOCOL_MESSAGE_LIST";
  private final String NEGOTIATION_MESSAGE_PROCEDURE = "GET_NEGOTIATION_MESSAGE_LIST";
  private final String PROPOSAL_MESSAGE_PROCEDURE = "GET_PROPOSAL_MESSAGE_LIST";
  private final String SUBCONTRACT_MESSAGE_PROCEDURE = "GET_SUBCONTRACT_MESSAGE_LIST";
  private final String COI_MESSAGE_PROCEDURE = "GET_COI_MESSAGE_LIST";
  //COEUSQA:1691 - End

  /**
   * No argument constructor.
   * Constructor instantiates DBEngineImpl.
   * @see edu.mit.coeus.utils.dbengine.DBEngineImpl
   */
  public CodeTableTxnBean()
  {

    dbEngine = new DBEngineImpl();

  }

  /**
   * Returns a Vector containing a list of HashMap objects.  Each HashMap
   * object represents one row of code table data, with key-value pairs for
   * column name and column value for the given row.  Takes as a parameter a
   * StoredProcedureBean containing name of stored procedure for selecting all
   * data from the given code table.
   * @param StoredProcedureBean storedProcedureBean StoredProcedureBean containing
   * name of stored procedure for selecting all data from the given code table.
   * @return Vector codeTableResultSet contains a list of HashMap objects.  Each
   * HashMap object represents one row of code table data, with key-value pairs
   * for column name and column value for the given row.
   * @see edu.mit.coeus.codetable.bean.StoredProcedureBean;
   * @throws DBException
   */
  public Vector getData(StoredProcedureBean selectStoredProcedure, Vector vectParam)
    throws DBException
  {

    Vector codeTableResultSet= new Vector();
    Vector vectParameters = new Vector() ;
    try
    {        
      String storedProcedureName = selectStoredProcedure.getName();

      /* Begin to put together the parameters for DBEngineImpl.executeRequest(). */
      String serviceName = "Coeus";
      String dataSource = "Coeus";
      StringBuffer sqlCommand = null ;
      

      /*Construct an empty Vector to pass to DBEngine.executeRequest(), since
      select stored procedure for code table will not require any parameters.*/
     if (vectParam.size() == 0)
     {
        sqlCommand = new StringBuffer("call "+storedProcedureName);
        sqlCommand.append("( <<OUT RESULTSET rset>> )");
     }    
     else
     {    
         if (vectParam.size()>0)
         {
             if (vectParam.get(0).toString().equalsIgnoreCase("osp$state_code")) // first element will be the table name then follwed by parameters
             {    
                sqlCommand = new StringBuffer("call "+storedProcedureName);
                sqlCommand.append("( <<AW_COUNTRY_CODE>> , <<OUT RESULTSET rset>> )");

                vectParameters.add( new Parameter(vectParam.get(1).toString(), //    "AW_COUNTRY_CODE",
                            DBEngineConstants.TYPE_STRING,
                            vectParam.get(2).toString())) ; // countryCode          
             } 
             /* CASE #IRBEN00078 and CASE #IRBEN00079 Begin */
             //Modified for Case#3053_Submission Details Type Qualifier Filter - Start
             //Added for Submission Type- Review Type Codetable 
//             else if (vectParam.get(0).toString().equalsIgnoreCase("osp$valid_proto_action_action")
//                || vectParam.get(0).toString().equalsIgnoreCase("osp$valid_proto_action_coresp")) 
             else if (vectParam.get(0).toString().equalsIgnoreCase("osp$valid_proto_action_action")
                || vectParam.get(0).toString().equalsIgnoreCase("OSP$AC_PROTO_FOLLOWUP_ACTIONS")
                || vectParam.get(0).toString().equalsIgnoreCase("osp$valid_proto_action_coresp")
                ||vectParam.get(0).toString().equalsIgnoreCase("OSP$VALID_PROTO_SUB_TYPE_QUAL")
                ||vectParam.get(0).toString().equalsIgnoreCase("OSP$VALID_PROTO_SUB_REV_TYPE")
                ||vectParam.get(0).toString().equalsIgnoreCase("OSP$AC_PROTOCOL_ACTION_CORRESP")
                ||vectParam.get(0).toString().equalsIgnoreCase("OSP$AC_PROTO_SUB_TYPE_QUAL")
                ||vectParam.get(0).toString().equalsIgnoreCase("OSP$AC_PROTOCOL_SUB_REV_TYPE")
                ||vectParam.get(0).toString().equalsIgnoreCase("OSP$AC_RECOMMEND_ACTION")
               ) 
             //Modified for Case#3053_Submission Details Type Qualifier Filter - End
             {    
                sqlCommand = new StringBuffer("call "+storedProcedureName);
                sqlCommand.append("( <<"+vectParam.get(1).toString()+">> , <<OUT RESULTSET rset>> )");
                vectParameters.add( new Parameter(vectParam.get(1).toString(), //    "AW_PROTOCOL_ACTION_TYPE_CODE",
                            DBEngineConstants.TYPE_INT, vectParam.get(2))) ; // protocolActionCode  
             } else if(vectParam.get(0).toString().equalsIgnoreCase("osp$person_role_module")) {
                 sqlCommand = new StringBuffer("call "+storedProcedureName);
                 sqlCommand.append("( <<"+vectParam.get(1).toString()+">>, <<OUT RESULTSET rset>> )");

                 vectParameters.add( new Parameter(vectParam.get(1).toString(), //    "AW_RECIPIENT_MODULE_CODE",
                            DBEngineConstants.TYPE_STRING,
                            vectParam.get(2).toString())) ; // moduleCode          
             } else if(vectParam.get(0).toString().equalsIgnoreCase("osp$coeus_sub_module") 
              //Added for COEUSQA-2320 : Show in Lite for Special Review in Code table - start
             ||vectParam.get(0).toString().equalsIgnoreCase("OSP$SPECIAL_REVIEW_USAGE") ||//COEUSQA-2320 : end
                     vectParam.get(0).toString().equalsIgnoreCase("OSP$CONTACT_USAGE")) {
                 sqlCommand = new StringBuffer("call "+storedProcedureName);
                 sqlCommand.append("( <<"+vectParam.get(1).toString()+">>, <<OUT RESULTSET rset>> )");

                 vectParameters.add( new Parameter(vectParam.get(1).toString(), //    "AW_MODULE_CODE",
                            DBEngineConstants.TYPE_STRING,
                            vectParam.get(2).toString())) ; // moduleCode          
             }
             /* CASE #IRBEN00078 and CASE #IRBEN00079 End */    
         }   
     } 
      if(dbEngine != null)
      {
            codeTableResultSet = dbEngine.executeRequest("Coeus",
            sqlCommand.toString(),
            "Coeus", vectParameters );
      }
      }
      catch(Exception e)
      {    
           e.printStackTrace();
      }

    return codeTableResultSet;
  }

    /**
   * Update, Insert or Delete a row of code table data.
   * Takes as parameters a StoredProcedureBean associated with updates for the given
   * code table and a Vector of HashMap objects.
   * StoredProcedureBean contains ParameterBean objects.
   * HashMaps contain key-value pairs for parameter name and
   * parameter value.
   * Returns true if the update was successful.
   * @param updateStoredProcedure StoredProcedureBean associated with updates for
   * the given code table.
   * @param paramValues HashMap of key-value pairs for parameter name and
   * parameter value.
   * @return boolean success.  Was the update successful?
   * @throws DBException
   */
  public boolean updateData(StoredProcedureBean updateStoredProcedure,
    Vector vectParamValues) throws DBException
  {
    boolean success = false;
    String storedProcedureName = updateStoredProcedure.getName();
    /* Begin to put together the parameters for DBEngineImpl.executeRequest(). */
    String serviceName = "Coeus";
    String dataSource = "Coeus";
    /* Call update stored procedure for each row to be updated.  */
    for(int rowsToModifyCnt = 0; rowsToModifyCnt < vectParamValues.size();
      rowsToModifyCnt++)
    {
      /* Get the HashMap of parameter name/parameter value pairs for the
      row to be modified.  */
      HashMap hashParamValues = (HashMap)vectParamValues.get(rowsToModifyCnt);
      StringBuffer sqlCommand = new StringBuffer("call "+storedProcedureName+"(");

      /*Construct an edu.mit.coeus.utils.dbengine.Parameter object for each
      edu.mit.coeus.codetable.ParameterBean object.*/
      Vector vectParameters = new Vector();
      Vector vectParameterBeans = (Vector)updateStoredProcedure.getVectParameters();
      String paramName = "";
      ParameterBean parameterBean = null;
      Parameter parameter = null;
      Object paramValue = null;
      String paramDataType = "";
      for(int i = 0; i< vectParameterBeans.size() - 1; i++)
      {
        parameterBean = (ParameterBean)vectParameterBeans.get(i);
        paramName = parameterBean.getName();
        sqlCommand.append("<<"+paramName+">>,");
        paramValue = hashParamValues.get(paramName);
//        if(paramValue != null)
//            System.out.println("ParamValue length------->"+paramName+"----->"+paramValue.toString().length());
        paramDataType = parameterBean.getDataType();
        if (( paramDataType.equals("int") || paramDataType.equals("float")) && paramValue == null)
        {
            paramDataType = "String";
        }
        parameter = new Parameter(paramName, paramDataType, paramValue);
        vectParameters.addElement(parameter);
       
      }
      //Add the final parameter, and end the sqlCommand with a right parenthesis.
      parameterBean =
        (ParameterBean)vectParameterBeans.get(vectParameterBeans.size()-1);
      paramName = parameterBean.getName();
      sqlCommand.append("<<"+paramName+">>)");
      paramValue = hashParamValues.get(paramName);
      paramDataType = parameterBean.getDataType();
      parameter = new Parameter(paramName, paramDataType, paramValue);
      vectParameters.addElement(parameter);
     
      if(dbEngine != null)
      {
         
        Vector resultSet = dbEngine.executeRequest
          (serviceName, sqlCommand.toString(), dataSource, vectParameters);
      }
    }
    /*If we got this far without an exception being thrown, then the update was
    successful.*/
    success = true;
    return success;
  }

  
  
  public int checkDependencyForIntCol(String a_table, String a_column, Integer a_column_value) throws DBException
  {
        int count  = 0 ;
        Vector param= new Vector();
        param.add(new Parameter("a_table",
                    DBEngineConstants.TYPE_STRING, a_table)) ;
        param.add(new Parameter("a_column",
                 DBEngineConstants.TYPE_STRING, a_column ));
        param.add(new Parameter("a_column_value",
                    DBEngineConstants.TYPE_INT, a_column_value ));
        
//        HashMap nextNumRow = null;
        HashMap nextNumRow = null;
        Vector result = new Vector();
        if(dbEngine!=null){
            result = dbEngine.executeFunctions("Coeus",
            "{<<OUT INTEGER v_count >> = call fn_get_row_count_intcol ( "
              + " << a_table >> , << a_column >> , << a_column_value >> )}", param);
        }else
        {
            CoeusMessageResourcesBean coeusMessageResourcesBean 
                    =new CoeusMessageResourcesBean();
            String msg = coeusMessageResourcesBean.parseMessageKey("db_exceptionCode.1000");
                                                        
            throw new DBException(msg);
        }
        if(!result.isEmpty()){

            nextNumRow = (HashMap)result.elementAt(0);
 
            count = Integer.parseInt(nextNumRow.get("v_count").toString());
        }
      return count ;
  }
  
  
public int checkDependencyForVarchar2Col(String a_table, String a_column, String a_column_value) throws DBException
{
  int count  = 0 ;
  Vector param= new Vector();
        param.add(new Parameter("a_table",
                    DBEngineConstants.TYPE_STRING, a_table)) ;
        param.add(new Parameter("a_column",
                 DBEngineConstants.TYPE_STRING, a_column ));
        param.add(new Parameter("a_column_value",
                    DBEngineConstants.TYPE_STRING, a_column_value ));
         
//        HashMap nextNumRow = null;
        HashMap nextNumRow = null;
        Vector result = new Vector();
        if(dbEngine!=null){
            result = dbEngine.executeFunctions("Coeus",
            "{<<OUT INTEGER v_count>> = call fn_get_row_count_varchar2col ( "
              + " << a_table >> , << a_column >> , << a_column_value >> )}", param);
//            System.out.println("***In code Transaction Bean*  checkDependencyForVarchar2Col @@@ after exec sp!!!**");
        }else
        {
            CoeusMessageResourcesBean coeusMessageResourcesBean 
                    =new CoeusMessageResourcesBean();
            String msg = coeusMessageResourcesBean.parseMessageKey("db_exceptionCode.1000");
                                                        
            throw new DBException(msg);
//            throw new DBException("db_exceptionCode.1000");
        }
        if(!result.isEmpty()){
            nextNumRow = (HashMap)result.elementAt(0);
            count = Integer.parseInt(nextNumRow.get("v_count").toString());
        }
      return count ;
  }
  
  
 public int checkDependencyForVarchar2ColState(String as_select, String as_from, String as_where) throws DBException
 {
  int count  = 0 ;
  Vector param= new Vector();
        param.add(new Parameter("as_select",
                    DBEngineConstants.TYPE_STRING, as_select)) ;
        param.add(new Parameter("as_from",
                 DBEngineConstants.TYPE_STRING, as_from ));
        param.add(new Parameter("as_where",
                    DBEngineConstants.TYPE_STRING, as_where ));

        HashMap nextNumRow = null;
        Vector result = new Vector();
        if(dbEngine!=null){
            result = dbEngine.executeFunctions("Coeus",
            "{<<OUT INTEGER v_count>> = call fn_get_row_count ( "
              + " << as_select >> , << as_from >> , << as_where >> )}", param);
//            System.out.println("***In code Transaction Bean*  checkDependencyForVarchar2Col @@@ after exec sp!!!**");
        }else
        {
            CoeusMessageResourcesBean coeusMessageResourcesBean 
                    =new CoeusMessageResourcesBean();
            String msg = coeusMessageResourcesBean.parseMessageKey("db_exceptionCode.1000");
                                                        
            throw new DBException(msg);
//            throw new DBException("db_exceptionCode.1000");
        }
        if(!result.isEmpty()){
            nextNumRow = (HashMap)result.elementAt(0);
            count = Integer.parseInt(nextNumRow.get("v_count").toString());

        }
      return count ;
  }
 

 
 public Vector getColumnsForTable(String as_tableName)
    throws DBException
  {

    Vector codeTableResultSet= new Vector();
    Vector vectParameters = new Vector() ;
    try
    {


      /* Begin to put together the parameters for DBEngineImpl.executeRequest(). */
      String serviceName = "Coeus";
      String dataSource = "Coeus";
      StringBuffer sqlCommand = null ;
      
       sqlCommand = new StringBuffer("call " + "dw_get_columns_for_table" ) ;
       sqlCommand.append("( <<as_table_name>> , <<OUT RESULTSET rset>> )");

       vectParameters.add( new Parameter( "as_table_name", DBEngineConstants.TYPE_STRING, as_tableName )) ;      
                
      if(dbEngine != null)
      {
            codeTableResultSet = dbEngine.executeRequest("Coeus",
            sqlCommand.toString(),
            "Coeus", vectParameters );
      }

     }
      catch(Exception e)
      {    
          e.printStackTrace();  
      }

    return codeTableResultSet;
  }

  /**
     *  This method is used to check whether the user has OSP rights.
     *  If the user has rights, it returns 1, else, returns 0.
     *  It uses the stored function FN_USER_HAS_OSP_RIGHT to do the validation
     *  @param userId is given as the input parameter to the function.
     *  @param rightId is given as the input parameter to the function.
     *  @return int hasRight
     *  @exception CoeusException raised if dbEngine is null.
     *  @exception DBException raised from the server side.
     */
 public int checkUserHasOSPRight(String userId, String rightId)
            throws CoeusException,DBException{
//                throws CoeusException,DBException{
        int hasRight = 0;
        Vector param= new java.util.Vector();
        Vector result = new Vector();
        //calling stored function
        param.addElement(new Parameter("USERID", DBEngineConstants.TYPE_STRING, userId));
        param.addElement(new Parameter("RIGHTID", DBEngineConstants.TYPE_STRING, rightId));

        if(dbEngine!=null){
            result = dbEngine.executeFunctions("Coeus",
                "{ << OUT INTEGER HAS_RIGHT >> = call FN_USER_HAS_OSP_RIGHT( << USERID >> , << RIGHTID >>) }",
                param);
        }else{
            CoeusMessageResourcesBean coeusMessageResourcesBean 
                    =new CoeusMessageResourcesBean();
            String msg = coeusMessageResourcesBean.parseMessageKey("db_exceptionCode.1000");
                                                        
            throw new DBException(msg);
//            throw new CoeusException("db_exceptionCode.1000");
        }
        if(result!=null && !result.isEmpty()){
            HashMap userRoleRow = (HashMap)result.elementAt(0);
            hasRight = Integer.parseInt(userRoleRow.get("HAS_RIGHT").toString());
        }
        return hasRight;
    }
 

 public Vector getDataForFunctionList()
    throws DBException
  {

    Vector codeTableResultSet= new Vector();
    Vector vectParameters = new Vector() ;
    try
    {


      /* Begin to put together the parameters for DBEngineImpl.executeRequest(). */
      String serviceName = "Coeus";
      String dataSource = "Coeus";
      StringBuffer sqlCommand = null ;
      
       sqlCommand = new StringBuffer("call " + "dw_get_function_list" ) ;
       sqlCommand.append("(<<OUT RESULTSET rset>> )");

//       vectParameters.add( new Parameter( "as_table_name", DBEngineConstants.TYPE_STRING, "OSP$RULE_FUNCTIONS" )) ;      
                
      if(dbEngine != null)
      {
            codeTableResultSet = dbEngine.executeRequest("Coeus",
            sqlCommand.toString(),
            "Coeus", vectParameters );
      }

     }
      catch(Exception e)
      {   
          e.printStackTrace();  
      }

    return codeTableResultSet;
  } 

 /**
  * Added for coeus4.3 enhancements
  * To get the protocol module details
  * @param procedureName
  * @throws edu.mit.coeus.utils.dbengine.DBException
  * @return Vector
  */
 public Vector getProtocolModuleNames(String procedureName) throws DBException {
     
     Vector result = new Vector();
     if(dbEngine!=null){
         result = dbEngine.executeRequest("Coeus",
                 "call "+procedureName+"  (<<OUT RESULTSET DETAILS>>)",
                 "Coeus", null);
     }else{
         throw new DBException("DB instance is not available");
     }
     return result;
 } 
 
 /**
  * This method is used to get the list of modules and corresponding actions
  * while creating a new action for email notification
  * 
  */
 public Vector getActionsForMail() throws DBException {
     Vector result = new Vector();
     if(dbEngine != null) {
         result = dbEngine.executeRequest("Coeus", "call " +
                 "GET_ACTIONS_FOR_EMAIL_NOTIF (<<OUT RESULTSET DETAILS>>)", "Coeus", null);
     } else {
         throw new DBException("DB Instance is not available");
     }
     return result;
 }
 
  //Added for Case #3121 - start
/**
  * 
  * To get the Cost Element details
  * @param procedureName
  * @throws edu.mit.coeus.utils.dbengine.DBException
  * @return Vector
  */
 public Vector getCostElements(String procedureName) throws DBException {
     
     Vector result = new Vector();
     if(dbEngine!=null){
         result = dbEngine.executeRequest("Coeus",
                 "call "+procedureName+"  (<<OUT RESULTSET DETAILS>>)",
                 "Coeus", null);
     }else{
         throw new DBException("DB instance is not available");
     }
     return result;
 } 
 //Added for Case #3121 - end
  
//Added for COEUSDEV-86 : Questionnaire for a Submission -Start
 /*
  * Method to get submodules for the module
  * @param moduleCode
  * @return vcSubModules
  */
 public Vector getSubModulesForModule(String moduleCode) throws DBException{
     Vector param= new Vector();
     Vector vcSubModules = new Vector();
     param.add(new Parameter("MODULE_CODE",
             DBEngineConstants.TYPE_STRING,moduleCode));
     if(dbEngine!=null){
         vcSubModules = dbEngine.executeRequest("Coeus",
                 "call get_sub_modules_for_module ( <<MODULE_CODE>> , <<OUT RESULTSET rset>> )",
                 "Coeus", param);
     }else{
         throw new DBException("DB instance is not available");
     }
     return vcSubModules;
     
 }
 //COEUSDEV-86 : End
 
// Added for COEUSQA-1725 Extend the functionality of Lab Allocation in proposal development budgeting - Start
 /**
  * Method to get the code table types based on the procedure
  * @param typeProcedureName
  * @throws edu.mit.coeus.utils.dbengine.DBException
  * @return cvTypes
  */
 public CoeusVector getFormulatedTypes(String typeProcedureName) throws DBException{
     Vector param= new Vector();
     CoeusVector cvTypes = null;
     
     if(dbEngine!=null){
         Vector vecTypes = dbEngine.executeRequest("Coeus","call "+typeProcedureName+" (<<OUT RESULTSET rset>> )","Coeus",null);
         cvTypes = new CoeusVector();
         if(vecTypes != null && !vecTypes.isEmpty()){
             for(Object formulatedType : vecTypes){
                 CoeusTypeBean coeusTypeBean = new CoeusTypeBean();
                 HashMap hmFormType = (HashMap)formulatedType;
                 coeusTypeBean.setTypeCode(Integer.parseInt(hmFormType.get("FORMULATED_CODE").toString()));
                 coeusTypeBean.setTypeDescription(hmFormType.get("DESCRIPTION").toString());
                 coeusTypeBean.setUpdateTimestamp((Timestamp)hmFormType.get("UPDATE_TIMESTAMP"));
                 coeusTypeBean.setUpdateUser(hmFormType.get("UPDATE_USER").toString());
                 cvTypes.add(coeusTypeBean);
             }
         }
     }else{
         throw new DBException("DB instance is not available");
     }
     return cvTypes;
 }
 
// Added for COEUSQA-1725 Extend the functionality of Lab Allocation in proposal development budgeting - End

 
 //COEUSQA:1691 - Front End Configurations to CoeusLite Pages - Start
 
 /**
  * Method to replace the message properties files from Code Table Interface
  *
  * @param messageType
  * @throws java.net.URISyntaxException
  */
 public void replaceMessageProperties(String messageType) throws URISyntaxException{
     String fileName = null;
     HashMap hmKeyValues = null;
     
     if(UPDATE_ARRA_MESSAGE.equals(messageType)){
         fileName = ARRA_MESSAGE_PROPERTY;
         hmKeyValues = getMessageProperties(ARRA_MESSAGE_PROCEDURE, "ARRA_MSG_CODE", "ARRA_MSG_VALUE");
     } else if(UPDATE_BUDGET_MESSAGE.equals(messageType)){
         fileName = BUDGET_MESSAGE_PROPERTY;
         hmKeyValues = getMessageProperties(BUDGET_MESSAGE_PROCEDURE, "BUDGET_MSG_CODE", "BUDGET_MSG_VALUE");
     } else if(UPDATE_COI_MESSAGE.equals(messageType)){
         fileName = COI_MESSAGE_PROPERTY;
         hmKeyValues = getMessageProperties(COI_MESSAGE_PROCEDURE, "COI_MSG_CODE", "COI_MSG_VALUE");
     } else if(UPDATE_NEGOTIATION_MESSAGE.equals(messageType)){
         fileName = NEGOTIATION_MESSAGE_PROPERTY;
         hmKeyValues = getMessageProperties(NEGOTIATION_MESSAGE_PROCEDURE, "NEGOTIATION_MSG_CODE", "NEGOTIATION_MSG_VALUE");
     } else if(UPDATE_PROPOSAL_MESSAGE.equals(messageType)){
         fileName = PROPOSAL_MESSAGE_PROPERTY;
         hmKeyValues = getMessageProperties(PROPOSAL_MESSAGE_PROCEDURE, "PROPOSAL_MSG_CODE", "PROPOSAL_MSG_VALUE");
     } else if(UPDATE_IRB_PROTOCOL_MESSAGE.equals(messageType)){
         fileName = IRB_PROTO_MESSAGE_PROPERTY;
         hmKeyValues = getMessageProperties(IRB_PROTO_MESSAGE_PROCEDURE, "IRB_PROTOCOL_MSG_CODE", "IRB_PROTOCOL_MSG_VALUE");
     } else if(UPDATE_AC_PROTOCOL_MESSAGE.equals(messageType)){
         fileName = AC_PROTO_MESSAGE_PROPERTY;
         hmKeyValues = getMessageProperties(AC_PROTO_MESSAGE_PROCEDURE, "IACUC_PROTOCOL_MSG_CODE", "IACUC_PROTOCOL_MSG_VALUE");
     } else if(UPDATE_SUBCONTRACT_MESSAGE.equals(messageType)){
         fileName = SUB_CONTRACT_MESSAGE_PROPERTY;
         hmKeyValues = getMessageProperties(SUBCONTRACT_MESSAGE_PROCEDURE, "SUBCONTRACT_MSG_CODE", "SUBCONTRACT_MSG_VALUE");
     }
     if(hmKeyValues != null && fileName != null) {
         updatePropertiesFile(hmKeyValues, fileName);
     }
 }
 
 
 /**
  * Method to replace all the message properties files from StartUpServlet when the server startup
  *
  * @throws java.net.URISyntaxException
  */
 public void replaceAllMessageProperties() throws URISyntaxException {
     
     boolean isPropertiesFileStatusUpdated = isPropertiesFileStatusUpdated();
     
     if(isPropertiesFileStatusUpdated == false){
         HashMap hmRes = null;
         
         hmRes = getMessageProperties(ARRA_MESSAGE_PROCEDURE, "ARRA_MSG_CODE", "ARRA_MSG_VALUE");
         updatePropertiesFile(hmRes, ARRA_MESSAGE_PROPERTY);
         
         hmRes = getMessageProperties(BUDGET_MESSAGE_PROCEDURE, "BUDGET_MSG_CODE", "BUDGET_MSG_VALUE");
         updatePropertiesFile(hmRes, BUDGET_MESSAGE_PROPERTY);
         
         hmRes = getMessageProperties(COI_MESSAGE_PROCEDURE, "COI_MSG_CODE", "COI_MSG_VALUE");
         updatePropertiesFile(hmRes, COI_MESSAGE_PROPERTY);
         
         hmRes = getMessageProperties(NEGOTIATION_MESSAGE_PROCEDURE, "NEGOTIATION_MSG_CODE", "NEGOTIATION_MSG_VALUE");
         updatePropertiesFile(hmRes, NEGOTIATION_MESSAGE_PROPERTY);
         
         hmRes = getMessageProperties(PROPOSAL_MESSAGE_PROCEDURE, "PROPOSAL_MSG_CODE", "PROPOSAL_MSG_VALUE");
         updatePropertiesFile(hmRes, PROPOSAL_MESSAGE_PROPERTY);
         
         hmRes = getMessageProperties(IRB_PROTO_MESSAGE_PROCEDURE, "IRB_PROTOCOL_MSG_CODE", "IRB_PROTOCOL_MSG_VALUE");
         updatePropertiesFile(hmRes, IRB_PROTO_MESSAGE_PROPERTY);
         
         hmRes = getMessageProperties(AC_PROTO_MESSAGE_PROCEDURE, "IACUC_PROTOCOL_MSG_CODE", "IACUC_PROTOCOL_MSG_VALUE");
         updatePropertiesFile(hmRes, AC_PROTO_MESSAGE_PROPERTY);
         
         hmRes = getMessageProperties(SUBCONTRACT_MESSAGE_PROCEDURE, "SUBCONTRACT_MSG_CODE", "SUBCONTRACT_MSG_VALUE");
         updatePropertiesFile(hmRes, SUB_CONTRACT_MESSAGE_PROPERTY);
         
         //Update to true
         updatePropertiesFileStatus();
     }
 }
 
 
 /**
  * Method to get the message property details from DB
  *
  * @param storedProcedureName
  * @param key
  * @param value
  * @return
  */
 public HashMap getMessageProperties(String storedProcedureName, String messageKey, String messageValue) {
     dbEngine = new DBEngineImpl();
     Vector codeTableResultSet= new Vector();
     Vector vectParameters = new Vector() ;
     HashMap hmRes = new HashMap();
     try {
         StringBuffer sqlCommand = null ;
         
         sqlCommand = new StringBuffer("call " + storedProcedureName ) ;
         sqlCommand.append("(<<OUT RESULTSET rset>> )");
         
         if(dbEngine != null) {
             codeTableResultSet = dbEngine.executeRequest("Coeus",
                     sqlCommand.toString(),
                     "Coeus", vectParameters );
         }
         if(codeTableResultSet != null && !codeTableResultSet.isEmpty()) {
             for(int index=0;index<codeTableResultSet.size();index++){
                 HashMap rowParameter = (HashMap)codeTableResultSet.elementAt(index);
                 hmRes.put((String)rowParameter.get(messageKey),(String)rowParameter.get(messageValue));
             }
         }
     } catch(Exception ex) {         
         UtilFactory.log(ex.getMessage(),ex, "CodeTableTxnBean", "getMessageProperties");
     }
     return hmRes;
 }
 
 
 /**
  * Method to update the Message Properties file
  *
  * @param hmKeyValues
  * @param fileName
  * @throws java.net.URISyntaxException
  */
 private void updatePropertiesFile(HashMap hmKeyValues, String fileName) throws URISyntaxException {
     
     File messPropertyFile = null;
     ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
     if (classLoader == null) {
         classLoader = getClass().getClassLoader();
     }
     URL fileUrl = classLoader.getResource(fileName);
     if (fileUrl == null) {
         fileUrl = classLoader.getResource(fileName);
     }
     try {
         messPropertyFile = new File(fileUrl.toURI());
         BufferedReader reader = new BufferedReader(new FileReader(messPropertyFile));
         String messPropertyLine = "";
         String updatedLine = "";
         while((messPropertyLine = reader.readLine()) != null) {
             if(messPropertyLine != " " && messPropertyLine.length() != 0 && messPropertyLine.contains("=")) {
                 StringTokenizer stKeyValue = new StringTokenizer(messPropertyLine,"=");
                 
                 String propertyKey = "";
                 String propertyValue = "";
                 while(stKeyValue.hasMoreElements()){
                     propertyKey = stKeyValue.nextToken().trim();
                     break;
                 }
                 propertyValue = messPropertyLine.substring(propertyKey.length(), messPropertyLine.length());
                 String hmResValue = null;
                 if(hmKeyValues != null){
                     if(hmKeyValues.get(propertyKey) != null) {
                         hmResValue = " = "+hmKeyValues.get(propertyKey).toString();
                     } else{
                         hmResValue = propertyValue;
                     }
                 }
                 String messPropertyNewLine = null;
                 messPropertyNewLine =  messPropertyLine.replace(propertyValue, hmResValue);
                 updatedLine += messPropertyNewLine + "\r\n";
             } else{
                 //Coeusqa-3922 start
                  if (!(messPropertyLine != " " && messPropertyLine.length() != 0 && messPropertyLine.contains("<") && messPropertyLine.contains(">") ))
                 //Coeusqa-3922 end
                    updatedLine += messPropertyLine + "\r\n";
             }
         }
         reader.close();
         
         FileWriter writer = new FileWriter(messPropertyFile);
         writer.write(updatedLine);
         writer.close();
         
     } catch (IOException ioe) {        
         UtilFactory.log(ioe.getMessage(),ioe, "CodeTableTxnBean", "updatePropertiesFile");
     }
 }
 
 /**
  * Method to check whether the properties file updated or not from the 
  * property file PropertiesFileStatus.properties
  *
  * @throws java.net.URISyntaxException
  * @return
  */
 private boolean isPropertiesFileStatusUpdated() throws URISyntaxException {
     
     File messPropertyFile = null;
     ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
     if (classLoader == null) {
         classLoader = getClass().getClassLoader();
     }
     URL fileUrl = classLoader.getResource(PROPERTY_FILE_UPDATED);
     if (fileUrl == null) {
         fileUrl = classLoader.getResource(PROPERTY_FILE_UPDATED);
     }
     try {
         messPropertyFile = new File(fileUrl.toURI());
         
         BufferedReader reader = new BufferedReader(new FileReader(messPropertyFile));
         String messPropertyLine = "";
         String propertyKey = "";
         String propertyValue = "";
         
         while((messPropertyLine = reader.readLine()) != null) {
             if(messPropertyLine != " " &&  messPropertyLine.length() != 0 && messPropertyLine.contains("=")) {
                 StringTokenizer stKeyValue = new StringTokenizer(messPropertyLine,"=");
                 while(stKeyValue.hasMoreElements()){
                     propertyKey = stKeyValue.nextToken().trim();
                     break;
                 }
                 propertyValue = messPropertyLine.substring(propertyKey.length(), messPropertyLine.length());
             }
         }
         reader.close();
         if("true".equals(propertyValue)){
             return true;
         }
     } catch (IOException ioe) {         
         UtilFactory.log(ioe.getMessage(),ioe, "CodeTableTxnBean", "isPropertiesFileStatusUpdated");
     }
     return false;
     
 }
 
 
 /**
  * Method to update the PropertiesFileStatus.properties file which indicates all the 
  * properties files have been updated
  *
  * @throws java.net.URISyntaxException
  */ 
 private void updatePropertiesFileStatus() throws URISyntaxException {
     
     File messPropertyFile = null;
     ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
     if (classLoader == null) {
         classLoader = getClass().getClassLoader();
     }
     URL fileUrl = classLoader.getResource(PROPERTY_FILE_UPDATED);
     if (fileUrl == null) {
         fileUrl = classLoader.getResource(PROPERTY_FILE_UPDATED);
     }
     try {
         messPropertyFile = new File(fileUrl.toURI());
         BufferedReader reader = new BufferedReader(new FileReader(messPropertyFile));
         String messPropertyLine = "";
         String updatedLine = "";
         while((messPropertyLine = reader.readLine()) != null) {
             if(messPropertyLine != " " &&  messPropertyLine.length() != 0 && messPropertyLine.contains("=")) {
                 StringTokenizer stKeyValue = new StringTokenizer(messPropertyLine,"=");
                 
                 String propertyKey = "";
                 String propertyValue = "";
                 while(stKeyValue.hasMoreElements()){
                     propertyKey = stKeyValue.nextToken().trim();
                     break;
                 }
                 propertyValue = messPropertyLine.substring(propertyKey.length(), messPropertyLine.length());
                 String resKeyValue = " = true";
                 
                 String messPropertyNewLine = null;
                 messPropertyNewLine =  messPropertyLine.replace(propertyValue, resKeyValue);
                 updatedLine += messPropertyNewLine + "\r\n";
             } else{
                 updatedLine += messPropertyLine + "\r\n";
             }
         }
         reader.close();
         
         FileWriter writer = new FileWriter(messPropertyFile);
         writer.write(updatedLine);
         writer.close();
         
     } catch (IOException ioe) {         
         UtilFactory.log(ioe.getMessage(),ioe, "CodeTableTxnBean", "updatePropertiesFileStatus");
     }
 }
 //COEUSQA:1691 - End
 
}//end class 


