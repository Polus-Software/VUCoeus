/*
 * CustomElementsDataTxnBean.java
 *
 * Created on December 13, 2004, 12:30 PM
 */

package edu.mit.coeus.customelements.bean;

import edu.mit.coeus.utils.dbengine.DBEngineImpl;
import edu.mit.coeus.utils.dbengine.DBException;
import edu.mit.coeus.utils.dbengine.Parameter;
import edu.mit.coeus.utils.dbengine.DBEngineConstants;
import edu.mit.coeus.utils.dbengine.ProcReqParameter;
import edu.mit.coeus.exception.CoeusException;
import edu.mit.coeus.utils.ComboBoxBean;
import edu.mit.coeus.utils.CoeusVector;
import edu.mit.coeus.utils.TypeConstants;
import edu.mit.coeus.utils.locking.LockingBean;
import edu.mit.coeus.utils.CoeusFunctions;
import edu.mit.coeus.departmental.bean.DepartmentArgListFormBean;

import java.lang.Character;
import java.util.Vector;
import java.util.Hashtable;
import java.util.HashMap;
import java.sql.Timestamp;
import java.sql.Date;
import java.sql.Connection;


/**
 *
 * @author  shivakumarmj
 */
public class CustomElementsDataTxnBean {
    // instance of a dbEngine
    private DBEngineImpl dBEngineImpl;
    // Holds the suerId for the loggedin user
    
    private String userId;
    
    private static final String DSN = "Coeus";
    
    /** Creates a new instance of CustomElementsDataTxnBean */
    public CustomElementsDataTxnBean() {
        dBEngineImpl = new DBEngineImpl();
    }
    
    /**
     * Creates new CustomElementsDataTxnBean and initializes userId.
     * @param userId String which the Loggedin userid
     */
    public CustomElementsDataTxnBean(String userId) {
        this.userId = userId;
        dBEngineImpl = new DBEngineImpl();
    }
    
    /** The following method has been written get Custom Data Elements
     * @exception DBException if any error during database transaction.
     * @exception CoeusException if the instance of dbEngine is not available.
     * @return type is CoeusVector
     */    
    public CoeusVector getCustomDataElements() throws CoeusException, DBException{
        Vector result = new Vector(3,2);
        HashMap hmCustomElements = null;                
        CoeusVector coeusVector = null;
        Vector param= new Vector();
        
        if(dBEngineImpl!=null){
            result = dBEngineImpl.executeRequest("Coeus",
            "call DW_GET_CUSTOM_DATA_ELEMENTS( <<OUT RESULTSET rset>> )",
            "Coeus", param);
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }
        int vecSize = result.size();
        CustomElementsInfoBean customElementsInfoBean = null;
        
        if(vecSize > 0){            
            coeusVector = new CoeusVector();
            for(int count=0;count<vecSize;count++){
                customElementsInfoBean = new CustomElementsInfoBean();
                hmCustomElements = (HashMap) result.elementAt(count);
                customElementsInfoBean.setColumnName((String)hmCustomElements.get("COLUMN_NAME"));
                customElementsInfoBean.setColumnLabel((String)hmCustomElements.get("COLUMN_LABEL"));
                customElementsInfoBean.setDataType((String)hmCustomElements.get("DATA_TYPE"));
                customElementsInfoBean.setDataLength(hmCustomElements.get("DATA_LENGTH") == null ? 0 :
                    Integer.parseInt(hmCustomElements.get("DATA_LENGTH").toString()));
                 // Enhancement to hold the group data
                 customElementsInfoBean.setGroupCode((String)hmCustomElements.get("GROUP_NAME"));
                customElementsInfoBean.setDefaultValue((String)hmCustomElements.get("DEFAULT_VALUE"));                                     
                customElementsInfoBean.setHasLookUp(hmCustomElements.get("HAS_LOOKUP") == null ? false:
                    hmCustomElements.get("HAS_LOOKUP").toString().equalsIgnoreCase("Y") ? true :false);
                customElementsInfoBean.setLookUpReuired((String)hmCustomElements.get("HAS_LOOKUP"));
                customElementsInfoBean.setLookUpWindow((String)hmCustomElements.get("LOOKUP_WINDOW"));                    
                customElementsInfoBean.setLookUpArgument((String)hmCustomElements.get("LOOKUP_ARGUMENT"));
                customElementsInfoBean.setUpdateTimestamp((Timestamp)hmCustomElements.get("UPDATE_TIMESTAMP"));
                customElementsInfoBean.setUpdateUser((String)hmCustomElements.get("UPDATE_USER"));  
                coeusVector.addElement(customElementsInfoBean);
            }
        }
        return coeusVector;                            
    } 
    
    /** The following method has been written to get Custom Data Elements for usage
     * @exception DBException if any error during database transaction.
     * @exception CoeusException if the instance of dbEngine is not available.
     * @return type is CoeusVector
     */    
    public CoeusVector getCustomDataElementUsage() throws CoeusException, DBException{
        Vector result = new Vector(3,2);
        HashMap hmCustomElements = null;                
        CoeusVector coeusVector = null;
        Vector param= new Vector();
        
        if(dBEngineImpl!=null){
            result = dBEngineImpl.executeRequest("Coeus",
            "call DW_GET_CUST_DATA_ELEMENT_USAGE( <<OUT RESULTSET rset>> )",
            "Coeus", param);
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }
        int vecSize = result.size();
        CustomElementsUsageBean customElementsUsageBean = null;
        
        if(vecSize > 0){            
            coeusVector = new CoeusVector();
            for(int count=0;count<vecSize;count++){
                customElementsUsageBean = new CustomElementsUsageBean();
                hmCustomElements = (HashMap) result.elementAt(count);
                customElementsUsageBean.setColumnName((String)hmCustomElements.get("COLUMN_NAME"));                
                customElementsUsageBean.setModuleCode(hmCustomElements.get("MODULE_CODE") == null ? 0 :
                    Integer.parseInt(hmCustomElements.get("MODULE_CODE").toString()));
                customElementsUsageBean.setIsRequired(hmCustomElements.get("IS_REQUIRED") == null ? false:
                    hmCustomElements.get("IS_REQUIRED").toString().equalsIgnoreCase("Y") ? true :false);
                customElementsUsageBean.setUpdateTimestamp((Timestamp)hmCustomElements.get("UPDATE_TIMESTAMP"));
                customElementsUsageBean.setUpdateUser((String)hmCustomElements.get("UPDATE_USER"));  
                customElementsUsageBean.setDescription((String)hmCustomElements.get("DESCRIPTION"));  
                coeusVector.addElement(customElementsUsageBean);
            }
        }
        return coeusVector;                            
    }
    
    /** The following method has been written to update the custom data elements
     * @param customElementsInfoBean is the input
     * @exception DBException if any error during database transaction.
     * @exception CoeusException if the instance of dbEngine is not available.
     * @return type is boolean 
     */    
    public ProcReqParameter updateCustomDataElements(CustomElementsInfoBean customElementsInfoBean) 
        throws CoeusException, DBException{
            Vector paramCustomDataElements = new Vector();        
            CoeusFunctions coeusFunctions = new CoeusFunctions();
//            Vector procedures = new Vector(5,3);
            Timestamp dbTimestamp = coeusFunctions.getDBTimestamp();
            boolean success = false;
            paramCustomDataElements.addElement(new Parameter("COLUMN_NAME",
                DBEngineConstants.TYPE_STRING,customElementsInfoBean.getColumnName()));
            paramCustomDataElements.addElement(new Parameter("COLUMN_LABEL",
                DBEngineConstants.TYPE_STRING,customElementsInfoBean.getColumnLabel()));
            paramCustomDataElements.addElement(new Parameter("DATA_TYPE",
                DBEngineConstants.TYPE_STRING,customElementsInfoBean.getDataType()));
            paramCustomDataElements.addElement(new Parameter("DATA_LENGTH",
                DBEngineConstants.TYPE_INT,""+customElementsInfoBean.getDataLength()));
            paramCustomDataElements.addElement(new Parameter("DEFAULT_VALUE",
                DBEngineConstants.TYPE_STRING,customElementsInfoBean.getDefaultValue()));            
            boolean hasLookUp = customElementsInfoBean.isHasLookUp();
            String strHasLookUp = "";
            if(hasLookUp){
                strHasLookUp = "Y";
            }else{
                strHasLookUp = "N";
            }    
            paramCustomDataElements.addElement(new Parameter("HAS_LOOKUP",
                DBEngineConstants.TYPE_STRING,strHasLookUp));
            paramCustomDataElements.addElement(new Parameter("LOOKUP_WINDOW",
                DBEngineConstants.TYPE_STRING,customElementsInfoBean.getLookUpWindow()));
            paramCustomDataElements.addElement(new Parameter("LOOKUP_ARGUMENT",
                DBEngineConstants.TYPE_STRING,customElementsInfoBean.getLookUpArgument()));
            paramCustomDataElements.addElement(new Parameter("UPDATE_TIMESTAMP",
                DBEngineConstants.TYPE_TIMESTAMP,dbTimestamp));
            paramCustomDataElements.addElement(new Parameter("UPDATE_USER",
                DBEngineConstants.TYPE_STRING,userId));
              // Enhancement to hold the Group code which is used in coeuslite
            paramCustomDataElements.addElement(new Parameter("GROUP_NAME",
                DBEngineConstants.TYPE_STRING,customElementsInfoBean.getGroupCode()));
            paramCustomDataElements.addElement(new Parameter("AW_COLUMN_NAME",
                DBEngineConstants.TYPE_STRING,customElementsInfoBean.getColumnName()));
            paramCustomDataElements.addElement(new Parameter("AW_UPDATE_TIMESTAMP",
                DBEngineConstants.TYPE_TIMESTAMP,customElementsInfoBean.getUpdateTimestamp()));
            paramCustomDataElements.addElement(new Parameter("AC_TYPE",
                DBEngineConstants.TYPE_STRING,customElementsInfoBean.getAcType()));
            
            StringBuffer sqlCustomDataElements = new StringBuffer(
                "call dw_upd_custom_data_elements(");
            sqlCustomDataElements.append("<<COLUMN_NAME>> ,");
            sqlCustomDataElements.append("<<COLUMN_LABEL>> ,");
            sqlCustomDataElements.append("<<DATA_TYPE>> ,");
            sqlCustomDataElements.append("<<DATA_LENGTH>> ,");
            sqlCustomDataElements.append("<<DEFAULT_VALUE>> ,");
            sqlCustomDataElements.append("<<HAS_LOOKUP>> ,");
            sqlCustomDataElements.append("<<LOOKUP_WINDOW>> ,");
            sqlCustomDataElements.append("<<LOOKUP_ARGUMENT>> ,");
            sqlCustomDataElements.append("<<UPDATE_TIMESTAMP>> ,");
            sqlCustomDataElements.append("<<UPDATE_USER>> ,");
            sqlCustomDataElements.append("<<GROUP_NAME>> ,");
            sqlCustomDataElements.append("<<AW_COLUMN_NAME>> ,");
            sqlCustomDataElements.append("<<AW_UPDATE_TIMESTAMP>> ,");
            sqlCustomDataElements.append("<<AC_TYPE>> )");
            
            ProcReqParameter procCustomDataElements = new ProcReqParameter();
            procCustomDataElements.setDSN(DSN);
            procCustomDataElements.setParameterInfo(paramCustomDataElements);
            procCustomDataElements.setSqlCommand(sqlCustomDataElements.toString());
            
            return procCustomDataElements;
            
//            if(dBEngineImpl!=null){
//                if(procedures != null && procedures.size() > 0){
//                    dBEngineImpl.executeStoreProcs(procedures);
//                }
//            }else{
//                throw new CoeusException("db_exceptionCode.1000");
//            }
//            success = true;
//            return success;
    }   
    
    /** The following  method has been written to update the custom data element usage
     * @param customElementsUsageBean is the input
     * @exception DBException if any error during database transaction.
     * @exception CoeusException if the instance of dbEngine is not available.
     * @return type is boolean 
     */    
    public ProcReqParameter updateCustomDataElementUsage(CustomElementsUsageBean customElementsUsageBean) 
        throws CoeusException, DBException{
            Vector paramCustomDataElements = new Vector();        
            CoeusFunctions coeusFunctions = new CoeusFunctions();
//            Vector procedures = new Vector(5,3);
            Timestamp dbTimestamp = coeusFunctions.getDBTimestamp();
            boolean success = false;
            paramCustomDataElements.addElement(new Parameter("COLUMN_NAME",
                DBEngineConstants.TYPE_STRING,customElementsUsageBean.getColumnName()));
            paramCustomDataElements.addElement(new Parameter("MODULE_CODE",
                DBEngineConstants.TYPE_INT,""+customElementsUsageBean.getModuleCode()));
            
            boolean isRequired = customElementsUsageBean.isIsRequired();
            String strIsReq = "N";
            if(isRequired){
                strIsReq = "Y";                
            }
            paramCustomDataElements.addElement(new Parameter("IS_REQUIRED",
                DBEngineConstants.TYPE_STRING,strIsReq));
            paramCustomDataElements.addElement(new Parameter("UPDATE_TIMESTAMP",
                DBEngineConstants.TYPE_TIMESTAMP,dbTimestamp));
            paramCustomDataElements.addElement(new Parameter("UPDATE_USER",
                DBEngineConstants.TYPE_STRING,userId));            
            paramCustomDataElements.addElement(new Parameter("AW_COLUMN_NAME",
                DBEngineConstants.TYPE_STRING,customElementsUsageBean.getColumnName()));
            paramCustomDataElements.addElement(new Parameter("AW_MODULE_CODE",
                DBEngineConstants.TYPE_INT,""+customElementsUsageBean.getModuleCode()));
            paramCustomDataElements.addElement(new Parameter("AW_UPDATE_TIMESTAMP",
                DBEngineConstants.TYPE_TIMESTAMP,customElementsUsageBean.getUpdateTimestamp()));
            paramCustomDataElements.addElement(new Parameter("AC_TYPE",
                DBEngineConstants.TYPE_STRING,customElementsUsageBean.getAcType()));
            
            StringBuffer sqlCustomDataElements = new StringBuffer(
                "call dw_upd_cust_data_element_usage(");
            sqlCustomDataElements.append("<<COLUMN_NAME>> ,");
            sqlCustomDataElements.append("<<MODULE_CODE>> ,");
            sqlCustomDataElements.append("<<IS_REQUIRED>> ,");
            sqlCustomDataElements.append("<<UPDATE_TIMESTAMP>> ,");
            sqlCustomDataElements.append("<<UPDATE_USER>> ,");
            sqlCustomDataElements.append("<<AW_COLUMN_NAME>> ,");
            sqlCustomDataElements.append("<<AW_MODULE_CODE>> ,");
            sqlCustomDataElements.append("<<AW_UPDATE_TIMESTAMP>> ,");
            sqlCustomDataElements.append("<<AC_TYPE>> )");
            
            ProcReqParameter procCustomDataElements = new ProcReqParameter();
            procCustomDataElements.setDSN(DSN);
            procCustomDataElements.setParameterInfo(paramCustomDataElements);
            procCustomDataElements.setSqlCommand(sqlCustomDataElements.toString());
            
            return procCustomDataElements;
            
//            if(dBEngineImpl!=null){
//                if(procedures != null && procedures.size() > 0){
//                    dBEngineImpl.executeStoreProcs(procedures);
//                }
//            }else{
//                throw new CoeusException("db_exceptionCode.1000");
//            }
//            success = true;
//            return success;
    }   
    
    /** The following  method has been written to update the custom data element usage
     * @param CoeusVector is the input
     * @exception DBException if any error during database transaction.
     * @exception CoeusException if the instance of dbEngine is not available.
     * @return type is boolean 
     */    
    public boolean updateCustomDataElementsAndUsageData(Hashtable cvData) 
        throws CoeusException, DBException {
            
        Vector procedures = new Vector(5,3);
        boolean success = false;
        CustomElementsInfoBean customElementsInfoBean = null;
        CustomElementsUsageBean customElementsUsageBean = null;
        CoeusVector cvCustomElements = (CoeusVector)cvData.get(CustomElementsInfoBean.class);
        CoeusVector cvCustomUsage = (CoeusVector)cvData.get(CustomElementsUsageBean.class);
                    
        if(cvCustomElements != null && cvCustomElements.size() > 0) {
        for(int index=0; index < cvCustomElements.size(); index++) {
                customElementsInfoBean = (CustomElementsInfoBean)cvCustomElements.elementAt(index);
                if(customElementsInfoBean.getAcType()==null){
                    continue;
                }else{
                    procedures.addElement(updateCustomDataElements(customElementsInfoBean));
                }
            }
        }
        if(cvCustomUsage != null && cvCustomUsage.size() > 0) {
            for(int index=0; index < cvCustomUsage.size(); index++) {
                customElementsUsageBean = (CustomElementsUsageBean)cvCustomUsage.elementAt(index);
                if(customElementsUsageBean.getAcType()==null){
                    continue;
                }else{
                    procedures.addElement(updateCustomDataElementUsage(customElementsUsageBean));
                }
            }
        }
        
        if(dBEngineImpl!=null){
                if(procedures != null && procedures.size() > 0){
                    dBEngineImpl.executeStoreProcs(procedures);
                }
            }else{
                throw new CoeusException("db_exceptionCode.1000");
            }
            success = true;
            return success;
    }
    
    /** The following method has been written to check whether a dependency 
     *  exists in the table. 
     * @param table is the table name 
     * @param column is the column name
     * @param columnValue
     * @exception DBException if any error during database transaction.
     * @exception CoeusException if the instance of dbEngine is not available.
     * @return type is integer
     */    
    public CoeusVector getRowCountVarcharToColumn(CustomElementsInfoBean customElementsInfoBean) 
        throws CoeusException, DBException{
            int columnCount = 0;
            boolean status = true;
            boolean deleteStatus = true;
            CoeusVector cvDepTable = new CoeusVector();
            Vector param=null;
            Vector result = new Vector();
            Vector vecTables = new Vector();
            vecTables.addElement("OSP$AWARD_CUSTOM_DATA");
            vecTables.addElement("OSP$EPS_PROP_CUSTOM_DATA");
            vecTables.addElement("OSP$EPS_PROP_PER_CUSTOM_DATA");
            vecTables.addElement("OSP$NEGOTIATION_CUSTOM_DATA");
            vecTables.addElement("OSP$PERSON_CUSTOM_DATA");
            vecTables.addElement("OSP$PROPOSAL_CUSTOM_DATA");            
            vecTables.addElement("OSP$PROTOCOL_CUSTOM_DATA");
            vecTables.addElement("osp$subcontract_custom_data");
            System.out.println("Column name "+customElementsInfoBean.getColumnName());
            for(int count = 0;count < vecTables.size() ; count++){
                param= new Vector();
                param.add(new Parameter("TABLE",DBEngineConstants.TYPE_STRING,vecTables.elementAt(count)));
                param.add(new Parameter("COLUMN",
                    DBEngineConstants.TYPE_STRING,"COLUMN_NAME"));
                param.add(new Parameter("COLUMN_VALUE",
                    DBEngineConstants.TYPE_STRING,customElementsInfoBean.getColumnName().trim()));
                if(dBEngineImpl!=null){
                result = dBEngineImpl.executeFunctions("Coeus",
                    "{ <<OUT INTEGER COUNT>> = call fn_get_row_count_varchar2col(<< TABLE >>,"+
                        "<< COLUMN >> , << COLUMN_VALUE >> ) }", param);
                }else{
                    throw new CoeusException("db_exceptionCode.1000");
                }
                if(!result.isEmpty()){
                    HashMap hmRowCount = (HashMap)result.elementAt(0);
                    columnCount = Integer.parseInt(hmRowCount.get("COUNT").toString());
                }
                if(columnCount > 0){
                    status = false;
                    cvDepTable.addElement(vecTables.elementAt(count));
                    break;
                }   
            }
            if(columnCount == 0){
                if(customElementsInfoBean.getAcType() != null) {
                    Vector procedures = new Vector(5,3);
                    procedures.addElement(updateCustomDataElements(customElementsInfoBean));
                    if(dBEngineImpl!=null){
                        if(procedures != null && procedures.size() > 0){
                            dBEngineImpl.executeStoreProcs(procedures);
                        }
                    }else{
                        throw new CoeusException("db_exceptionCode.1000");
                    }
                    deleteStatus = true;
                }
            }
            cvDepTable.addElement(new Boolean(status));
            cvDepTable.addElement(new Boolean(deleteStatus));
            return cvDepTable;
    }        
    
    public CoeusVector getModuleNameNotInUsage(String columnName) 
        throws CoeusException, DBException{
            
        Vector result = new Vector(3,2);
        HashMap hmModuleName = null;                
        CoeusVector coeusVector = null;
        CustomElementsUsageBean customElementsUsageBean = null;
        Vector param= new Vector();   
        
        param.add(new Parameter("COLUMN_NAME", 
                DBEngineConstants.TYPE_STRING,columnName));
        
        if(dBEngineImpl!=null){
            result = dBEngineImpl.executeRequest("Coeus",
            "call get_module_name_not_in_usage(<<COLUMN_NAME>>, <<OUT RESULTSET rset>> )",
            "Coeus", param);
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }
        int resultSize = result.size();
        if(resultSize > 0){
            coeusVector = new CoeusVector();
            for(int count = 0;count < resultSize ; count++){
                hmModuleName = (HashMap)result.elementAt(count);
                customElementsUsageBean = new CustomElementsUsageBean();                
                customElementsUsageBean.setModuleCode(hmModuleName.get("MODULE_CODE") == null ? 0 :
                    Integer.parseInt(hmModuleName.get("MODULE_CODE").toString()));
                customElementsUsageBean.setDescription((String)hmModuleName.get("DESCRIPTION"));
                coeusVector.addElement(customElementsUsageBean);
            }    
        }    
        return coeusVector;            
    }        
    
    public CoeusVector getArgumentNamesForCodeTable() 
        throws CoeusException, DBException{
        Vector result = new Vector(3,2);
        HashMap hmArgName = null;                
        ComboBoxBean comboBoxBean = null;
        Vector param= new Vector();   
        CoeusVector coeusVector = null;
        if(dBEngineImpl!=null){
            result = dBEngineImpl.executeRequest("Coeus",
            "call get_arg_names_for_code_tbl(<<OUT RESULTSET rset>> )",
            "Coeus", param);
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }
        int resultSize = result.size();
        if(resultSize > 0){            
            coeusVector = new CoeusVector();
            for(int count = 0;count < resultSize ; count++){
                hmArgName = (HashMap)result.elementAt(count);
                comboBoxBean = new ComboBoxBean();
                comboBoxBean.setCode((String)hmArgName.get("CODE"));
                comboBoxBean.setDescription((String)hmArgName.get("DESCRIPTION"));
                coeusVector.addElement(comboBoxBean);
            }
        }    
        return coeusVector;
    }        
    
    
    
    
     public static void main(String args[]){
        try{
            CustomElementsDataTxnBean customElementsDataTxnBean = new CustomElementsDataTxnBean();                                    
            CoeusVector cvDelete = customElementsDataTxnBean.getArgumentNamesForCodeTable();
            System.out.println("Vector size "+cvDelete.size());
        }catch(Exception ex){
            ex.printStackTrace();
        }
    }
    
}
