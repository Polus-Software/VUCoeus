package edu.mit.is.service.authorization;


import edu.mit.coeus.utils.UtilFactory;
import edu.mit.coeus.utils.dbengine.DBEngineImpl;
import edu.mit.coeus.utils.dbengine.DBException;
import edu.mit.coeus.utils.dbengine.DBEngineConstants;
import edu.mit.coeus.utils.dbengine.Parameter;
import edu.mit.coeus.utils.dbengine.ProcReqParameter;

import java.util.HashMap;
import java.util.Vector;
import java.sql.Timestamp;

/**
 *
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: Massachusetts Institute of Technology</p>
 * @author Coeus Development Team
 * @version 1.0
 *
 * Methods removed
 *    <p>private static java.util.Properties getPropertiesFromTable</p>
 *    <p>private static Object getFieldFromTable(String fieldName,
 *    String[] columnNames, Object[] row)</p>
 *    <p>private static String getAuthorizationDetailSQL(Number id)</p>
 */

public class Authorization
implements org.okip.service.authorization.api.Authorization {
    
    private final static String NO        = "N";
    private final static String YES       = "Y";
    
    
    
    // Authorization field names
    
    private final static String AUTHORIZATION_ID  = "AUTHORIZATION_ID";
    private final static String FUNCTION_ID       = "FUNCTION_ID";
    private final static String QUALIFIER_ID      = "QUALIFIER_ID";
    private final static String KERBEROS_NAME     = "KERBEROS_NAME";
    private final static String QUALIFIER_CODE    = "QUALIFIER_CODE";
    private final static String FUNCTION_NAME     = "FUNCTION_NAME";
    private final static String FUNCTION_CATEGORY = "FUNCTION_CATEGORY";
    private final static String QUALIFIER_NAME    = "QUALIFIER_DESCRIPTION";
    private final static String MODIFIED_BY       = "MODIFIED_BY";
    private final static String MODIFIED_DATE     = "MODIFIED_DATE";
    private final static String DESCEND           = "DESCEND";
    private final static String EFFECTIVE_DATE    = "EFFECTIVE_DATE";
    private final static String EXPIRATION_DATE   = "EXPIRATION_DATE";
    
    private final static String PERSON            = "PERSON";
    private final static String FUNCTION          = "FUNCTION";
    private final static String QUALIFIER         = "QUALIFIER";
    private final static String ACTIVENOW         = "IS_ACTIVE_NOW";
    
    private  static DBEngineImpl dbEngine = new DBEngineImpl();
    
    public boolean isImplied() {
        // make this look in the database
        return false;
    }
    
    private Factory factory =  null;
    
    /**
     * Method getFactory
     *
     *
     * @return
     *
     */
    public Factory getFactory() {
        return this.factory;
    }
    
    protected void setFactory(
    Factory factory) {
        this.factory = factory;
    }
    
    private java.util.Properties properties = null;
    
    /**
     * Method getProperties
     *
     *
     * @return
     *
     */
    public java.util.Properties getProperties() {
        return this.properties;
    }
    
    protected void setProperties(java.util.Properties properties) {
        this.properties = properties;
    }
    
    protected Object getProperty(String propertyName) {
        if ((null != propertyName) && (null != this.properties)) {
            Object obj = this.properties.get(propertyName);
            
            try {
                if (-1 != propertyName.toUpperCase().indexOf(PERSON)) {
                    
                    // make this call to get all the person properties
                    
                    ((Person) obj).isFoundInAuthorization();
                }
            }
            catch (Exception ex) {}
            
            return obj;
        }
        
        return null;
    }
    
    protected void setProperty(String propertyName, Object property) {
        if (null == this.properties) {
            this.properties = new java.util.Properties();
        }
        
        if ((null != this.properties) && (null != propertyName)
        && (null != property)) {
            this.properties.put(propertyName, property);
        }
    }
    
    /**
     * Method getId
     *
     *
     * @return
     *
     */
    public Object getId() {
        return getProperty(AUTHORIZATION_ID);
    }
    
    protected void setId(Object id) {
        setProperty(AUTHORIZATION_ID, id);
    }
    
    /**
     * Method getAgent
     *
     *
     * @return
     *
     */
    public org.okip.service.authorization.api.Agent getAgent() {
        return (org.okip.service.authorization.api.Agent) getProperty(PERSON);
    }
    
    protected void setPerson(org.okip.service.authorization.api.Agent person) {
        setProperty(PERSON, person);
    }
    
    /**
     * Method getFunction
     *
     *
     * @return
     *
     */
    public org.okip.service.authorization.api.Function getFunction() {
        return (org.okip.service.authorization.api.Function) getProperty(FUNCTION);
    }
    
    protected void setFunction(org.okip.service.authorization.api.Function function) {
        setProperty(FUNCTION, function);
    }
    
    /**
     * Method getQualifier
     *
     *
     * @return
     *
     */
    public org.okip.service.authorization.api.Qualifier getQualifier() {
        return (org.okip.service.authorization.api.Qualifier) getProperty(QUALIFIER);
    }
    
    protected void setQualifier(org.okip.service.authorization.api.Qualifier qualifier) {
        setProperty(QUALIFIER, qualifier);
    }
    
    /**
     * Method getEffectiveDate
     *
     *
     * @return
     *
     */
    
    public java.sql.Timestamp getEffectiveDate() {
        return (java.sql.Timestamp) getProperty(EFFECTIVE_DATE);
    }
    
    protected void setEffectiveDate(java.sql.Timestamp effectiveDate) {
        setProperty(EFFECTIVE_DATE, effectiveDate);
    }
    
    /**
     * Method getExpirationDate
     *
     *
     * @return
     *
     */
    public java.sql.Timestamp getExpirationDate() {
        return (java.sql.Timestamp) getProperty(EXPIRATION_DATE);
    }
    
    protected void setExpirationDate(java.sql.Timestamp expirationDate) {
        setProperty(EXPIRATION_DATE, expirationDate);
    }
    
    /**
     * Method isActiveNow
     *
     *
     * @return
     *
     */
    public boolean isActiveNow() {
        return ((Boolean) getProperty(ACTIVENOW)).booleanValue();
    }
    
    protected void setActiveNow(boolean activeNow) {
        setProperty(ACTIVENOW, new Boolean(activeNow));
    }
    
    /**
     * Method getModifiedBy
     *
     *
     * @return
     *
     */
    public String getModifiedBy() {
        return (String) getProperty(MODIFIED_BY);
    }
    
    protected void setModifiedBy(String modifiedBy) {
        setProperty(MODIFIED_BY, modifiedBy);
    }
    
    /**
     * Method getModifiedDate
     *
     *
     * @return
     *
     */
    public java.sql.Timestamp getModifiedDate() {
        return (java.sql.Timestamp) getProperty(MODIFIED_DATE);
    }
    
    protected void setModifiedDate(java.sql.Timestamp modifiedDate) {
        setProperty(MODIFIED_DATE, modifiedDate);
    }
    
    protected Authorization(
    Factory factory,
    java.util.Properties properties) {
        setFactory(factory);
        setProperties(properties);
    }
    
    public Authorization(
    Factory factory,
    Object id, org.okip.service.authorization.api.Agent person,
    org.okip.service.authorization.api.Function function,
    org.okip.service.authorization.api.Qualifier qualifier,
    java.sql.Timestamp effectiveDate,
    java.sql.Timestamp expirationDate, boolean activeNow,
    String modifiedBy, java.sql.Timestamp modifiedDate
    ) {
        setFactory(factory);
        setId(id);
        setPerson(person);
        setFunction(function);
        setQualifier(qualifier);
        setEffectiveDate(effectiveDate);
        setExpirationDate(expirationDate);
        setActiveNow(activeNow);
        setModifiedBy(modifiedBy);
        setModifiedDate(modifiedDate);
    }
    
    /**
     * Coeus  - Needs Coeus implementation
     *
     *
     * Method delete
     *
     *
     * @throws org.okip.service.authorization.api.AuthorizationException
     *
     *
     */
    
    public void delete()
    throws org.okip.service.authorization.api.AuthorizationException {
        
    }
    
    /**
     * Method update
     *
     *
     * @param person
     * @param function
     * @param qualifier
     * @param effectiveDate
     * @param expirationDate
     *
     * @throws org.okip.service.authorization.api.AuthorizationException
     *
     *
     * Sabari - Needs Coeus Implementation
     */
    public void update(org.okip.service.authorization.api.Agent person,
    org.okip.service.authorization.api.Function function,
    org.okip.service.authorization.api.Qualifier qualifier,
    java.sql.Timestamp effectiveDate,
    java.sql.Timestamp expirationDate)
    throws org.okip.service.authorization.api.AuthorizationException {
        
    }
    
    /**
     * Method create
     *
     *
     * @param factory
     * @param person
     * @param function
     * @param qualifier
     *
     * @return
     *
     * @throws org.okip.service.authorization.api.AuthorizationException
     *
     */
    public static org.okip.service.authorization.api.Authorization create(
    Factory factory,
    org.okip.service.authorization.api.Agent person,
    org.okip.service.authorization.api.Function function,
    org.okip.service.authorization.api.Qualifier qualifier)
    throws org.okip.service.authorization.api.AuthorizationException {
        return create(factory, person, function, qualifier,
        new java.sql.Timestamp(new java.util.Date().getTime()),
        null);
    }
    
    /**
     * Method create
     *
     *
     * @param factory
     * @param person
     * @param function
     * @param qualifier
     * @param effectiveDate
     * @param expirationDate
     *
     * @return
     *
     * @throws org.okip.service.authorization.api.AuthorizationException
     *
     * Sabari -
     * Need Coeus implementation
     *
     */
    public static org.okip.service.authorization.api.Authorization create(
    Factory factory,
    org.okip.service.authorization.api.Agent person,
    org.okip.service.authorization.api.Function function,
    org.okip.service.authorization.api.Qualifier qualifier,
    java.sql.Timestamp effectiveDate,
    java.sql.Timestamp expirationDate)
    throws org.okip.service.authorization.api.AuthorizationException {
        
        return null;
    }
    
    /**
     * Method isAuthorized
     *
     *
     * @param factory
     * @param person
     * @param function
     * @param qualifier
     *
     * @return
     *
     * @throws org.okip.service.authorization.api.AuthorizationException
     *
     * COEUS IMPLEMENTATION
     */
    
    public static boolean isAuthorized(
    Factory factory,
    org.okip.service.authorization.api.Agent person,
    org.okip.service.authorization.api.Function function,
    org.okip.service.authorization.api.Qualifier qualifier)
    throws org.okip.service.authorization.api.AuthorizationException {
        
        Vector result = new Vector();
        Vector param = new Vector();
        
        FunctionType functionType;
        QualifierType qualifierType;
        boolean checkingForRole = false;
        String    functionTypeKey = "";
        String    qualifierTypeKey = "";
        
        
        functionType = (FunctionType) function.getFunctionType();
        if (null != functionType) {
            functionTypeKey = functionType.getKeyword();
            if (functionTypeKey.equalsIgnoreCase("ROLE")) {
                checkingForRole = true;
            }
        }
        
        if (null!= qualifier) {
            qualifierType = (QualifierType) qualifier.getQualifierType();
            if (qualifierType == null) {
                //Assume that the qualifier is a unit
                qualifierTypeKey = "UNIT";
            } else {
                qualifierTypeKey = qualifierType.getKeyword();
            }
        }
        
        try {
            
            if (qualifier != null) {
                //Checking with a qualifier
                if (qualifierTypeKey.equals("PROPOSAL")) {
                    if (checkingForRole == true) {
                        //Checking for a specific Role in a proposal
                        param.addElement(new Parameter("AW_USER_ID", "String", (String) person.getKey()));
                        param.addElement(new Parameter("AW_PROP_NUM", "String", (String) qualifier.getKey()));
                        param.addElement(new Parameter("AW_ROLE_ID", "String", (String) function.getKey()));
                        
                        if(dbEngine!=null) {
                            result = dbEngine.executeFunctions("Coeus",
                            "{ << OUT INTEGER RET >> = call FN_USER_HAS_PROPOSAL_ROLE ( << AW_USER_ID >> , << AW_PROP_NUM >> , << AW_ROLE_ID >> ) }",
                            param);
                            
                            if (result.size() > 0) {
                                
                                HashMap resultSet;
                                String  returnValue;
                                resultSet = (HashMap) result.get(0);
                                
                                returnValue = resultSet.get("RET").toString();
                                
                                if (returnValue.equals("1")) {
                                    return true;
                                }
                                else {
                                    return false;
                                }
                            } //end if result.size
                        }
                        
                    } else {
                        //Checking for a right in a specific Proposal
                        param.addElement(new Parameter("AW_USER_ID", "String", (String) person.getKey()));
                        param.addElement(new Parameter("AW_PROPOSAL_NUMBER", "String", (String) qualifier.getKey()));
                        param.addElement(new Parameter("AW_RIGHT_ID", "String", (String) function.getKey()));
                        
                        if(dbEngine!=null) {
                            result = dbEngine.executeFunctions("Coeus",
                            "{ << OUT INTEGER RET >> = call FN_USER_HAS_PROP_RIGHT ( << AW_USER_ID >> , << AW_PROPOSAL_NUMBER >> , << AW_RIGHT_ID >> ) }",
                            param);
                            
                            if (result.size() > 0) {
                                
                                HashMap resultSet;
                                String  returnValue;
                                resultSet = (HashMap) result.get(0);
                                
                                returnValue = resultSet.get("RET").toString();
                                
                                if (returnValue.equals("1")) {
                                    return true;
                                }
                                else {
                                    return false;
                                }
                            } //end if result.size
                        }
                    }
                    
                }else if(qualifierTypeKey.equals("PROTOCOL")){
                    if (checkingForRole == false) {
                        //Checking for a right in a specific Protocol
                        param.addElement(new Parameter("AW_USER_ID", "String", (String) person.getKey()));
                        param.addElement(new Parameter("AW_PROTOCOL_NUMBER", "String", (String) qualifier.getKey()));
                        param.addElement(new Parameter("AW_RIGHT_ID", "String", (String) function.getKey()));
                        
                        if(dbEngine!=null) {
                            result = dbEngine.executeFunctions("Coeus",
                            "{ << OUT INTEGER RET >> = call FN_USER_HAS_PROTOCOL_RIGHT ( << AW_USER_ID >> , << AW_PROTOCOL_NUMBER >> , << AW_RIGHT_ID >> ) }",
                            param);
                            
                            if (result.size() > 0) {
                                
                                HashMap resultSet;
                                String  returnValue;
                                resultSet = (HashMap) result.get(0);
                                
                                returnValue = resultSet.get("RET").toString();
                                
                                if (returnValue.equals("1")) {
                                    return true;
                                }
                                else {
                                    return false;
                                }
                            } //end if result.size
                        }
                    }                    
                
                //Added for COEUSQA-1724 : IACUC module - parent case - Start
                }else if(qualifierTypeKey.equals("IACUC_PROTOCOL")){
                    if (checkingForRole == false) {
                        //Checking for a right in a specific IACUC Protocol
                        param.addElement(new Parameter("AW_USER_ID", "String", (String) person.getKey()));
                        param.addElement(new Parameter("AW_PROTOCOL_NUMBER", "String", (String) qualifier.getKey()));
                        param.addElement(new Parameter("AW_RIGHT_ID", "String", (String) function.getKey()));
                        
                        if(dbEngine!=null) {
                            result = dbEngine.executeFunctions("Coeus",
                                    "{ << OUT INTEGER RET >> = call FN_USER_HAS_AC_PROTOCOL_RIGHT ( << AW_USER_ID >> , << AW_PROTOCOL_NUMBER >> , << AW_RIGHT_ID >> ) }",
                                    param);
                            
                            if (result.size() > 0) {
                                
                                HashMap resultSet;
                                String  returnValue;
                                resultSet = (HashMap) result.get(0);
                                
                                returnValue = resultSet.get("RET").toString();
                                
                                if (returnValue.equals("1")) {
                                    return true;
                                } else {
                                    return false;
                                }
                            } //end if result.size
                        }
                    }
                }
                //COEUSQA-1724 : End
                else {
                    //Qualifier is Unit
                    if (checkingForRole == true) {
                        //Checking for a specific Role
                        throw new org.okip.service.authorization.api.AuthorizationException("Not Implemented");
                    } else {
                        //Checking for a right in a unit
                        param.addElement(new Parameter("AW_USER_ID", "String", (String) person.getKey()));
                        param.addElement(new Parameter("AW_UNIT_NUMBER", "String", (String) qualifier.getKey()));
                        param.addElement(new Parameter("AW_RIGHT_ID", "String", (String) function.getKey()));
                        
                        if(dbEngine!=null) {
                            result = dbEngine.executeFunctions("Coeus",
                            "{ << OUT INTEGER RET >> = call FN_USER_HAS_RIGHT ( << AW_USER_ID >> , << AW_UNIT_NUMBER >> , << AW_RIGHT_ID >> ) }",
                            param);
                            
                            if (result.size() > 0) {
                                
                                HashMap resultSet;
                                String  returnValue;
                                resultSet = (HashMap) result.get(0);
                                
                                returnValue = resultSet.get("RET").toString();
                                
                                if (returnValue.equals("1")) {
                                    return true;
                                }
                                else {
                                    return false;
                                }
                            } //end if result.size
                        }
                    }
                }
                
            } else {
                //Checking for an authorization anywhere - Not specific to a qualifier
                if (checkingForRole == true) {
                    //Checking for a specific Role
                    param.addElement(new Parameter("AW_USER_ID", "String", (String) person.getKey()));
                    param.addElement(new Parameter("AW_ROLE_ID", "String", (String) function.getKey()));
                    
                    if (dbEngine != null) {
                        result = dbEngine.executeFunctions("Coeus",
                        "{ << OUT INTEGER RET >> = call FN_USER_HAS_ROLE ( << AW_USER_ID >> , << AW_ROLE_ID >> ) }",
                        param);
                        
                        if (result.size() > 0 ) {
                            HashMap resultSet;
                            String returnValue;
                            resultSet = (HashMap) result.get(0);
                            returnValue = resultSet.get("RET").toString();
                            
                            if (returnValue.equals("1")) {
                                return true;
                            } else {
                                return false;
                            }
                        }
                    }
                    
                } else {
                    //Checking for a right
                    param.addElement(new Parameter("AW_USER_ID", "String", (String) person.getKey()));
                    param.addElement(new Parameter("AW_RIGHT_ID", "String", (String) function.getKey()));
                    
                    if(dbEngine!=null) {
                        result = dbEngine.executeFunctions("Coeus",
                        "{ << OUT INTEGER RET >> = call fn_user_has_right_in_any_unit ( << AW_USER_ID >> , << AW_RIGHT_ID >> ) }",
                        param);
                        
                        if (result.size() > 0) {
                            HashMap resultSet;
                            String  returnValue;
                            resultSet = (HashMap) result.get(0);
                            returnValue = resultSet.get("RET").toString();
                            
                            if (returnValue.equals("1")) {
                                return true;
                            }
                            else {
                                return false;
                            }
                        }
                    }
                }
            }
            
        } //End try
        
        catch (Exception e) {
            throw new org.okip.service.authorization.api.AuthorizationException(
            e.getMessage());
        }
        
        return false;
    }
    
    /**
     * Method isAuthorized
     *
     *
     * @param factory
     * @param person
     * @param function
     * @param qualifier
     *
     * @return
     *
     * @throws org.okip.service.authorization.api.AuthorizationException
     *
     *
     * COEUS - NEEDS IMPLEMENTATION
     */
    public static boolean isAuthorized(
    Factory factory,
    org.okip.service.authorization.api.Agent person,
    org.okip.service.authorization.api.FunctionType functionType,
    org.okip.service.authorization.api.Qualifier qualifier)
    throws org.okip.service.authorization.api.AuthorizationException {
        
        Vector result = new Vector();
        Vector param = new Vector();
        String functionTypeDesc;
        
        functionTypeDesc = functionType.getKeyword();
        if (!functionTypeDesc.equals("OSP")) {
            throw new org.okip.service.authorization.api.AuthorizationException("Unsupported function Type");
        }
        
        try {
            
            if (null == qualifier) {
                param.addElement(new Parameter("AW_USER_ID", "String", (String) person.getKey()));
                
                if(dbEngine!=null) {
                    result = dbEngine.executeFunctions("Coeus",
                    "{ << OUT INTEGER RET >> = call FN_USER_HAS_ANY_OSP_RIGHT ( << AW_USER_ID >> ) }",
                    param);
                    
                    if (result.size() > 0) {
                        HashMap resultSet;
                        String  returnValue;
                        resultSet = (HashMap) result.get(0);
                        returnValue = resultSet.get("RET").toString();
                        
                        if (returnValue.equals("1")) {
                            return true;
                        }
                        else {
                            return false;
                        }
                    } else {
                        return false;
                    }
                }
                
            } else {
                //Qualifier is not null
                //This should raise a not implemented exception.
                throw new org.okip.service.authorization.api.AuthorizationException("Not Implemented");
            }
        } catch (Exception e) {
            throw new org.okip.service.authorization.api.AuthorizationException(
            e.getMessage());
        }
        
        return false;
    }
    
    /**
     * Method isAuthorized
     *
     *
     * @param factory
     * @param person
     * @param function
     * @param functionType
     *
     * @return
     *
     * @throws org.okip.service.authorization.api.AuthorizationException
     *
     *
     * This method is used to Check whether the User has OSP rights for the given Right Id.
     */
    public static boolean isAuthorized(
    Factory factory,
    org.okip.service.authorization.api.Agent person,
    org.okip.service.authorization.api.Function function,
    org.okip.service.authorization.api.FunctionType functionType)
    throws org.okip.service.authorization.api.AuthorizationException {
        Vector result = new Vector();
        Vector param = new Vector();
        String functionTypeDesc;
        
        functionTypeDesc = functionType.getKeyword();
        if (!functionTypeDesc.equals("OSP")) {
            throw new org.okip.service.authorization.api.AuthorizationException("Unsupported function Type");
        }
        
        try {
                param.addElement(new Parameter("AW_USER_ID", "String", (String) person.getKey()));
                param.addElement(new Parameter("AW_RIGHT_ID", "String", (String) function.getKey()));
                
                if(dbEngine!=null) {
                    result = dbEngine.executeFunctions("Coeus",
                    "{ << OUT INTEGER RET >> = call FN_USER_HAS_OSP_RIGHT ( << AW_USER_ID >> , << AW_RIGHT_ID >>) }",
                    param);
                    
                    if (result.size() > 0) {
                        HashMap resultSet;
                        String  returnValue;
                        resultSet = (HashMap) result.get(0);
                        returnValue = resultSet.get("RET").toString();
                        
                        if (returnValue.equals("1")) {
                            return true;
                        }
                        else {
                            return false;
                        }
                    } else {
                        return false;
                    }
                }
        } catch (Exception e) {
            throw new org.okip.service.authorization.api.AuthorizationException(
            e.getMessage());
        }
        
        return false;
    }    
    
    /**
     * Method addUpdDeleteUsers
     *
     *
     * @param factory
     * @param person
     *
     * @return boolean
     *
     * @throws org.okip.service.authorization.api.AuthorizationException
     *
     *
     * This method is used to Add/Update/Delete Users.
     */
    public static boolean addUpdDeleteUsers(
    Factory factory,
    Person person)
    throws org.okip.service.authorization.api.AuthorizationException {
        
        Vector result = new Vector();
        Vector param = new Vector();
        Vector procedures = new Vector(5,3);
        java.util.Properties personProperties = person.getProperties();
        boolean success = true;
        try {
                param.addElement(new Parameter("AV_USER_ID", "String", (String) person.getKey()));
                param.addElement(new Parameter("AV_USER_NAME", "String", (String) person.getName()));
                param.addElement(new Parameter("AV_NON_MIT_PERSON_FLAG", "String", (String) person.getProperty("NON_MIT_PERSON_FLAG")));
                param.addElement(new Parameter("AV_PERSON_ID", "String", (String) person.getProperty("PERSONID")));
                param.addElement(new Parameter("AV_USER_TYPE", "String", (String) person.getProperty("USER_TYPE")));
                param.addElement(new Parameter("AV_UNIT_NUMBER", "String", (String) person.getProperty("UNIT_NUMBER")));
                param.addElement(new Parameter("AV_STATUS", "String", (String) person.getProperty("STATUS")));
                param.addElement(new Parameter("AV_UPDATE_TIMESTAMP", DBEngineConstants.TYPE_TIMESTAMP, person.getProperty("UPDATE_TIMESTAMP")));
                param.addElement(new Parameter("AV_UPDATE_USER", "String", (String) person.getProperty("UPDATE_USER")));
                param.addElement(new Parameter("AW_USER_ID", "String", (String) person.getKey()));
                param.addElement(new Parameter("AW_UPDATE_TIMESTAMP", DBEngineConstants.TYPE_TIMESTAMP, person.getProperty("AW_UPDATE_TIMESTAMP")));
                param.addElement(new Parameter("AC_TYPE", "String", (String) person.getProperty("AC_TYPE")));
                
                StringBuffer sqlNarrative = new StringBuffer(
                                                "call DW_UPDATE_USER(");
                sqlNarrative.append(" <<AV_USER_ID>> , ");
                sqlNarrative.append(" <<AV_USER_NAME>> , ");
                sqlNarrative.append(" <<AV_NON_MIT_PERSON_FLAG>> , ");
                sqlNarrative.append(" <<AV_PERSON_ID>> , ");
                sqlNarrative.append(" <<AV_USER_TYPE>> , ");
                sqlNarrative.append(" <<AV_UNIT_NUMBER>> , ");
                sqlNarrative.append(" <<AV_STATUS>> , ");
                sqlNarrative.append(" <<AV_UPDATE_TIMESTAMP>> , ");
                sqlNarrative.append(" <<AV_UPDATE_USER>> , ");
                sqlNarrative.append(" <<AW_USER_ID>> , ");
                sqlNarrative.append(" <<AW_UPDATE_TIMESTAMP>> , ");
                sqlNarrative.append(" <<AC_TYPE>> )");
                
                ProcReqParameter procNarrative  = new ProcReqParameter();
                procNarrative.setDSN("Coeus");
                procNarrative.setParameterInfo(param);
                procNarrative.setSqlCommand(sqlNarrative.toString());                
                
                procedures.add(procNarrative);
                
                if(dbEngine!=null) {
                    dbEngine.executeStoreProcs(procedures);
                }
        } catch (Exception e) {
            throw new org.okip.service.authorization.api.AuthorizationException(
            e.getMessage());
        }
        success = true;
        return success;
    }      
    
    /**
     * Method getUserInfo
     *
     *
     * @param factory
     * @param person
     *
     * @return boolean
     *
     * @throws org.okip.service.authorization.api.AuthorizationException
     *
     *
     * This method is used to get User Info for the given User Id.
     */
    public static Person getUserInfo(
    Factory factory,
    Person person)
    throws org.okip.service.authorization.api.AuthorizationException {
        
        Vector result = new Vector();
        Vector param = new Vector();
        Vector procedures = new Vector(5,3);
        Vector authList = new Vector() ;
        try {
                
              param.addElement(new Parameter("AW_USER_ID", "String", (String) person.getKey()));
                
              result = dbEngine.executeRequest("Coeus",
              "call dw_get_user( << AW_USER_ID >> , <<OUT RESULTSET rset>> )",
              "Coeus", param);
              int rowCount = result.size();

              if (rowCount > 0) {
                  AuthorizationsIterator authorizations = new AuthorizationsIterator();

                  HashMap resultSet;
                  resultSet = (HashMap) result.get(0);
                  Qualifier authQualifier;
                  String nonMITPerson;
                  java.sql.Timestamp timeStamp;
                  person = (Person) factory.newPerson(resultSet.get("USER_ID").toString());
                  person.setName(resultSet.get("USER_NAME").toString());
                  person.setStatusCode(resultSet.get("STATUS").toString());
                  nonMITPerson = (String)resultSet.get("NON_MIT_PERSON_FLAG");
                  timeStamp = (java.sql.Timestamp)resultSet.get("UPDATE_TIMESTAMP");
                  String strUser = (String)resultSet.get("UPDATE_USER");
                  person.setProperty("PERSON_ID",resultSet.get("PERSON_ID")==null ? " " :resultSet.get("PERSON_ID").toString());
                  person.setProperty("NON_MIT_PERSON_FLAG",nonMITPerson);
                  person.setProperty("UPDATE_TIMESTAMP",timeStamp);
                  person.setProperty("UPDATE_USER",strUser);
                  person.setProperty("UNIT_NAME",resultSet.get("UNIT_NAME").toString());
                  person.setProperty("UNIT_NUMBER",resultSet.get("UNIT_NUMBER").toString());
                  return person;
              }
        } catch (Exception e) {
            throw new org.okip.service.authorization.api.AuthorizationException(
            e.getMessage());
        }
        return null;
    }
    
    /**
     * Method addUpdDeleteRoleRights
     *
     *
     * @param factory
     * @param person
     *
     * @return boolean
     *
     * @throws org.okip.service.authorization.api.AuthorizationException
     *
     *
     * This method is used to Add/Update/Delete Users.
     */
    public static boolean addUpdDeleteRoleRights(
    Factory factory,
    Function function)
    throws org.okip.service.authorization.api.AuthorizationException {
        
        Vector result = new Vector();
        Vector param = new Vector();
        Vector procedures = new Vector(5,3);
        java.util.Map roleRightsMap = function.getProperties();
        FunctionType functionType = null;
        functionType = (FunctionType)function.getFunctionType();
        try {
                
                param.addElement(new Parameter("AV_RIGHT_ID", "String", (String) function.getKey()));
                param.addElement(new Parameter("AV_ROLE_ID", "String", (String) roleRightsMap.get("ROLE_ID")));
                param.addElement(new Parameter("AV_DESCEND_FLAG", "String", (String) functionType.getKeyword()));
                param.addElement(new Parameter("AV_UPDATE_TIMESTAMP", "String", (String) roleRightsMap.get("UPDATE_TIMESTAMP")));
                param.addElement(new Parameter("AV_UPDATE_USER", "String", (String) roleRightsMap.get("UPDATE_USER")));
                param.addElement(new Parameter("AW_RIGHT_ID", "String", (String) function.getKey()));
                param.addElement(new Parameter("AW_ROLE_ID", "String", (String) roleRightsMap.get("ROLE_ID")));
                param.addElement(new Parameter("AW_UPDATE_TIMESTAMP", "String", (String) roleRightsMap.get("AW_UPDATE_TIMESTAMP")));
                param.addElement(new Parameter("AC_TYPE", "String", (String) roleRightsMap.get("AC_TYPE")));
                
                StringBuffer sqlNarrative = new StringBuffer(
                                                "call DW_UPDATE_ROLE_RIGHTS(");
                sqlNarrative.append(" <<AV_RIGHT_ID>> , ");
                sqlNarrative.append(" <<AV_ROLE_ID>> , ");
                sqlNarrative.append(" <<AV_DESCEND_FLAG>> , ");
                sqlNarrative.append(" <<AV_UPDATE_TIMESTAMP>> , ");
                sqlNarrative.append(" <<AV_UPDATE_USER>> , ");
                sqlNarrative.append(" <<AW_RIGHT_ID>> , ");
                sqlNarrative.append(" <<AW_ROLE_ID>> , ");
                sqlNarrative.append(" <<AW_UPDATE_TIMESTAMP>> , ");
                sqlNarrative.append(" <<AC_TYPE>> )");
                
                ProcReqParameter procNarrative  = new ProcReqParameter();
                procNarrative.setDSN("Coeus");
                procNarrative.setParameterInfo(param);
                procNarrative.setSqlCommand(sqlNarrative.toString());                
                
                procedures.add(procNarrative);
                
                if(dbEngine!=null) {
                    dbEngine.executeStoreProcs(procedures);
                }
        } catch (Exception e) {
            throw new org.okip.service.authorization.api.AuthorizationException(
            e.getMessage());
        }
        return false;
    }
}
