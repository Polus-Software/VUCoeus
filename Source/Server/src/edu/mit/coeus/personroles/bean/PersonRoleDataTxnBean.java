/*
 * PersonRoleDataTxnBean.java
 *
 * Created on May 18, 2007, 12:16 PM
 *
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 *
 */

package edu.mit.coeus.personroles.bean;

import edu.mit.coeus.exception.CoeusException;
import edu.mit.coeus.utils.CoeusVector;
import edu.mit.coeus.utils.dbengine.DBEngineConstants;
import edu.mit.coeus.utils.dbengine.DBEngineImpl;
import edu.mit.coeus.utils.dbengine.DBException;
import edu.mit.coeus.utils.dbengine.Parameter;
import java.util.HashMap;
import java.util.Vector;

/**
 *
 * @author talarianand
 */
public class PersonRoleDataTxnBean {

    // instance of a dbEngine
    private DBEngineImpl dbEngineImpl;
    // Holds the userId for the loggedin user
    
    private String userId;
    
    private static final String DSN = "Coeus";
    
    private static final String ROLE_CODE = "ROLE_TYPE_CODE";
    
    private static final String DESCRIPTION = "DESCRIPTION";
    
    /** Creates a new instance of PersonRoleDataTxnBean */
    public PersonRoleDataTxnBean() {
        dbEngineImpl = new DBEngineImpl();
    }
    
    /**
     * Creates new PersonRoleDataTxnBean and initializes userId.
     * @param userId String which the Loggedin userid
     */
    public PersonRoleDataTxnBean(String userId) {
        this.userId = userId;
        dbEngineImpl = new DBEngineImpl();
    }
    
    /**
     * This gets the list of person role types
     * @return CoesuVector which will have the list of role type.
     */
    public CoeusVector getRoleList() throws CoeusException, DBException {
        Vector result = new Vector(3,2);
        HashMap hmRoleList = null;                
        CoeusVector coeusVector = null;
        Vector param= new Vector();
        
        if(dbEngineImpl!=null){
            result = dbEngineImpl.executeRequest(DSN,
            "call GET_PERSON_ROLE_TYPE( <<OUT RESULTSET rset>> )",
            DSN, param);
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }
        int vecSize = result.size();
        PersonRoleInfoBean mailActionInfoBean = null;
        
        if(vecSize > 0){            
            coeusVector = new CoeusVector();
            for(int count=0;count<vecSize;count++){
                mailActionInfoBean = new PersonRoleInfoBean();
                hmRoleList = (HashMap) result.elementAt(count);
                //mailActionInfoBean.setRoleCode(Integer.parseInt(hmRoleList.get(ROLE_CODE).toString()));
                mailActionInfoBean.setRoleCode(hmRoleList.get(ROLE_CODE).toString());
                mailActionInfoBean.setRoleName((String) hmRoleList.get(DESCRIPTION));
                coeusVector.addElement(mailActionInfoBean);
            }
        }
        return coeusVector;                            
    }
    
    /**
     * This method gets the list of qualifiers for a specific Role Type.
     * If the rolecode is null, then this method returns a CoeusVector with 
     * all the rolecodes and qualifiers
     * @param roleCode
     * @return CoeusVector with a list of qualifiers.
     */
    public CoeusVector getQualifierList(String roleCode) throws CoeusException, DBException {
        Vector result = new Vector(3,2);
        HashMap hmQualifierList = null;                
        CoeusVector coeusVector = null;
        Vector param= new Vector();
        String tempRoleCode = "";
        
        if(dbEngineImpl!=null){
            result = dbEngineImpl.executeRequest(DSN,
            "call GET_QUALIFIER_FOR_PERSON_ROLE( <<OUT RESULTSET rset>> )",
            DSN, param);
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }
        PersonRoleInfoBean mailActionInfoBean = null;
        
        if(result != null && result.size() > 0){            
            coeusVector = new CoeusVector();
            for(int count = 0; count < result.size(); count++){
                mailActionInfoBean = new PersonRoleInfoBean();
                hmQualifierList = (HashMap) result.elementAt(count);
                tempRoleCode = (String) hmQualifierList.get(ROLE_CODE);
                if(roleCode != null && roleCode != "") {
                    if(roleCode.equals(tempRoleCode)) {
                        mailActionInfoBean.setQualifierCode(hmQualifierList.get("QUALIFIER_CODE").toString());
                        mailActionInfoBean.setRoleQualifier((String) hmQualifierList.get(DESCRIPTION));
                        //mailActionInfoBean.setRoleCode(Integer.parseInt((String) hmQualifierList.get(ROLE_CODE)));
                        mailActionInfoBean.setRoleCode((String) hmQualifierList.get(ROLE_CODE));
                        coeusVector.addElement(mailActionInfoBean);
                    }
                } else {
                    mailActionInfoBean.setQualifierCode(hmQualifierList.get("QUALIFIER_CODE").toString());
                    mailActionInfoBean.setRoleQualifier((String) hmQualifierList.get(DESCRIPTION));
                    //mailActionInfoBean.setRoleCode(Integer.parseInt((String) hmQualifierList.get(ROLE_CODE)));
                    mailActionInfoBean.setRoleCode((String) hmQualifierList.get(ROLE_CODE));
                    coeusVector.addElement(mailActionInfoBean);
                }
            }
        }
        return coeusVector;                            
    }
    
    /**
     * Gives the list of person role types for the specific module
     * @param ModuleCode
     * @return CoesuVector which will have the list of role type.
     */
    public CoeusVector fetchRoleList(String moduleCode) throws CoeusException, DBException {
        Vector result = new Vector(3,2);
        HashMap hmRoleList = null;                
        CoeusVector coeusVector = null;
        Vector param= new Vector();
        
        param.add(new Parameter("AV_ROLE_MODULE_CODE", DBEngineConstants.TYPE_INT, moduleCode));
        
        if(dbEngineImpl!=null){
            result = dbEngineImpl.executeRequest(DSN,
            "call GET_PERSON_ROLE_MODULE( <<AV_ROLE_MODULE_CODE>>, <<OUT RESULTSET rset>> )",
            DSN, param);
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }
        int vecSize = result.size();
        PersonRoleInfoBean mailActionInfoBean = null;
        
        if(vecSize > 0){            
            coeusVector = new CoeusVector();
            for(int count=0;count<vecSize;count++){
                mailActionInfoBean = new PersonRoleInfoBean();
                hmRoleList = (HashMap) result.elementAt(count);
                //mailActionInfoBean.setRoleCode(Integer.parseInt(hmRoleList.get(ROLE_CODE).toString()));
                mailActionInfoBean.setRoleCode(hmRoleList.get(ROLE_CODE).toString());
                mailActionInfoBean.setRoleName((String) hmRoleList.get(DESCRIPTION));
                mailActionInfoBean.setModuleCode(hmRoleList.get("MODULE_CODE").toString());
                coeusVector.addElement(mailActionInfoBean);
            }
        }
        return coeusVector;                            
    }
    
    /**
     * Is used to fetch the module and submodule information for a specific role
     * @param roleId String Role Id
     * @return 
     * @throws CoeusException, DBException
     */
    public CoeusVector fetchRoleModule(String roleId) throws CoeusException, DBException {
        Vector result = new Vector();
        HashMap hmRoleList = null;                
        CoeusVector coeusVector = null;
        Vector param= new Vector();
        
        param.add(new Parameter("AV_ROLE_TYPE_CODE", DBEngineConstants.TYPE_INT, roleId));
        
        if(dbEngineImpl!=null){
            result = dbEngineImpl.executeRequest(DSN,
            "call GET_ROLE_MODULES( <<AV_ROLE_TYPE_CODE>>, <<OUT RESULTSET rset>> )",
            DSN, param);
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }
        int vecSize = result.size();
        PersonRoleInfoBean mailActionInfoBean = null;
        
        if(vecSize > 0){            
            coeusVector = new CoeusVector();
            for(int count=0;count<vecSize;count++){
                mailActionInfoBean = new PersonRoleInfoBean();
                hmRoleList = (HashMap) result.elementAt(count);
                //mailActionInfoBean.setRoleCode(Integer.parseInt(hmRoleList.get(ROLE_CODE).toString()));
                mailActionInfoBean.setRoleCode(hmRoleList.get(ROLE_CODE).toString());
                mailActionInfoBean.setRoleName((String) hmRoleList.get(DESCRIPTION));
                mailActionInfoBean.setModuleCode(hmRoleList.get("MODULE_CODE").toString());
                mailActionInfoBean.setModuleDescription((String) hmRoleList.get("MODULE_DESCRIPTION"));
                mailActionInfoBean.setSubModuleCode(hmRoleList.get("SUB_MODULE_CODE").toString());
                mailActionInfoBean.setSubModuleDescription((String) hmRoleList.get("SUB_MODULE_DESCRIPTION"));
                coeusVector.addElement(mailActionInfoBean);
            }
        }
        return coeusVector;                            
    }
    
}
