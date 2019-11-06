/*
 * ApplicationContext.java
 *
 * Created on November 16, 2006, 11:23 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package edu.mit.coeus.utils;

import edu.mit.coeus.utils.dbengine.DBEngineImpl;
import edu.mit.coeus.utils.dbengine.DBException;
import edu.mit.coeus.utils.dbengine.Parameter;
import edu.mit.coeus.utils.dbengine.ProcReqParameter;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Vector;

/**
 * Use this class to set and get Application level(Global) variables.
 * The implementation writes and reads to the database.
 * @author sharathk
 */
public class ApplicationContext {
    
    /** Creates a new instance of ApplicationContext */
    private ApplicationContext() {
    }
    
    /**
     * set Application level attribute
     * Binds an object to a given attribute name. 
     * If the name specified is already used for an attribute, this method will remove the old 
     * attribute and bind the name to the new attribute. 
     * @param attributeKey key
     * @param attributeValue value
     */
    public final static synchronized void setAttribute(String attributeKey, Object attributeValue, String userId) {
        
        if(containsKey(attributeKey)) {
            removeAttribute(attributeKey);
        }
        
        DBEngineImpl dBEngineImpl = new DBEngineImpl();
        StringBuffer sqlBuffer = new StringBuffer();
        sqlBuffer.append("insert into OSP$APPLICATION_CONTEXT(");
        sqlBuffer.append(" ATTRIBUTE_KEY, ");
        sqlBuffer.append(" ATTRIBUTE_VALUE, ");
        sqlBuffer.append(" UPDATE_TIMESTAMP, ");
        sqlBuffer.append(" UPDATE_USER ) ");
        sqlBuffer.append(" VALUES (");
        sqlBuffer.append(" <<ATTRIBUTE_KEY>> , ");
        sqlBuffer.append(" <<ATTRIBUTE_VALUE>> , ");
        sqlBuffer.append(" <<UPDATE_TIMESTAMP>> , ");
        sqlBuffer.append(" <<UPDATE_USER>> ) ");
        
        Timestamp timestamp;
        try{
            timestamp = (new CoeusFunctions()).getDBTimestamp();
            
            Vector param = new Vector();
            
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
            objectOutputStream.writeObject(attributeValue);
            byte data[] = byteArrayOutputStream.toByteArray();
            
            param.addElement(new Parameter("ATTRIBUTE_KEY", "String", attributeKey));
            param.addElement(new Parameter("ATTRIBUTE_VALUE", "Blob", data));
            param.addElement(new Parameter("UPDATE_TIMESTAMP", "Timestamp", timestamp));
            param.addElement(new Parameter("UPDATE_USER", "String", userId));
            
            ProcReqParameter procReqParameter = new ProcReqParameter();
            procReqParameter.setDSN("Coeus");
            procReqParameter.setParameterInfo(param);
            procReqParameter.setSqlCommand(sqlBuffer.toString());
            
            dBEngineImpl.sqlUpdate(procReqParameter);
            
        }catch   (Exception exception) {
            UtilFactory.log(exception.getMessage(),exception,"Application","setAttribute()");
        }
    }
    
    /**
     * Returns the attribute with the given name, or null if there is no attribute by that name. 
     * A list of supported attributes can be retrieved using getAttributeNames. 
     * The attribute is returned as a java.lang.Object or some subclass. 
     * @param attributeKey key
     * @return returns the value for this key, else returns null
     */
    public final static synchronized Object getAttribute(String attributeKey) {
        Object retObject = null;
        try{
            DBEngineImpl dBEngineImpl = new DBEngineImpl();
            StringBuffer sqlBuffer = new StringBuffer();
            sqlBuffer.append("SELECT ");
            sqlBuffer.append(" ATTRIBUTE_VALUE ");
            sqlBuffer.append(" FROM  OSP$APPLICATION_CONTEXT WHERE");
            sqlBuffer.append(" ATTRIBUTE_KEY = <<ATTRIBUTE_KEY>> ");
            
            Vector param = new Vector();
            param.addElement(new Parameter("ATTRIBUTE_KEY", "String", attributeKey));
            
            Vector result = new Vector();
            if(dBEngineImpl != null) {
                result = dBEngineImpl.executeRequest("Coeus", sqlBuffer.toString(), "Coeus", param);
                if(result != null && result.size() > 0) {
                    HashMap mapRow = (HashMap)result.elementAt(0);
                    ByteArrayOutputStream byteArrayOutputStream;
                    byteArrayOutputStream = (ByteArrayOutputStream)mapRow.get("ATTRIBUTE_VALUE");
                    byte data[] = byteArrayOutputStream.toByteArray();
                    
                    ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(data);
                    ObjectInputStream objectInputStream = new ObjectInputStream(byteArrayInputStream);
                    retObject = objectInputStream.readObject();
                    
                }
            }
        }catch (Exception exception) {
            UtilFactory.log(exception.getMessage(),exception,"Application","getAttribute()");
        }
        return retObject;
    }
    
    
    /**
     * Removes the attribute with the given name. 
     * After removal, subsequent calls to getAttribute(java.lang.String) 
     * to retrieve the attribute's value will return null.
     * @param attributeKey 
     */
    public final static synchronized void removeAttribute(String attributeKey) {
        try{
            DBEngineImpl dBEngineImpl = new DBEngineImpl();
            StringBuffer sqlBuffer = new StringBuffer();
            sqlBuffer.append("DELETE ");
            sqlBuffer.append(" FROM  OSP$APPLICATION_CONTEXT WHERE");
            sqlBuffer.append(" ATTRIBUTE_KEY = <<ATTRIBUTE_KEY>> ");
            
            Vector param = new Vector();
            param.addElement(new Parameter("ATTRIBUTE_KEY", "String", attributeKey));
            
            ProcReqParameter procReqParameter = new ProcReqParameter();
            procReqParameter.setDSN("Coeus");
            procReqParameter.setParameterInfo(param);
            procReqParameter.setSqlCommand(sqlBuffer.toString());
            
            dBEngineImpl.sqlUpdate(procReqParameter);
            
        }catch (Exception exception) {
            UtilFactory.log(exception.getMessage(),exception,"Application","removeAttribute()");
        }
    }
    
    /**
     * returns true id this key exists, else returns false.
     * @param key 
     * @return 
     */
    public final static synchronized boolean containsKey(String key){
        boolean containsKey = false;
        try{
            DBEngineImpl dBEngineImpl = new DBEngineImpl();
            StringBuffer sqlBuffer = new StringBuffer();
            sqlBuffer.append("SELECT ");
            sqlBuffer.append("  count(ATTRIBUTE_KEY) AS COUNT ");
            sqlBuffer.append(" FROM  OSP$APPLICATION_CONTEXT WHERE");
            sqlBuffer.append(" ATTRIBUTE_KEY = <<ATTRIBUTE_KEY>> ");
            
            Vector param = new Vector();
            param.addElement(new Parameter("ATTRIBUTE_KEY", "String", key));
            
            Vector result = new Vector();
            if(dBEngineImpl != null) {
                result = dBEngineImpl.executeRequest("Coeus", sqlBuffer.toString(), "Coeus", param);
                if(result != null && result.size() > 0) {
                    HashMap hashMap = (HashMap) result.elementAt(0);
                    BigDecimal bigDecimal = (BigDecimal)hashMap.get("COUNT");
                    if(bigDecimal != null) {
                        containsKey = (bigDecimal.intValue() == 1);
                    }
                }
            }
        }catch (Exception exception) {
            UtilFactory.log(exception.getMessage(),exception,"Application","containsKey()");
        }
        return containsKey;
    }
    
}
