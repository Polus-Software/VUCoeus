/*
 * QueryEngine.java
 *
 * Created on October 1, 2003, 5:34 PM
 */

package edu.mit.coeus.utils.query;

/** Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 * @author sharathk
 */

import java.util.*;
import java.lang.reflect.*;

import edu.mit.coeus.exception.CoeusException;
import edu.mit.coeus.bean.BaseBean;
import edu.mit.coeus.bean.ComparableBean;
import edu.mit.coeus.bean.CoeusBean;
import edu.mit.coeus.utils.*;

/**
 * Contains different sets of Data Collection
 * in Hashtable. can execute query to get a
 * subSets of the Data Collection or
 * can add, update and delete data to a Data Collection.
 * This class is a Singleton since it has to be
 * accessed globally.
 * @version 1.1  - 12/5/2004
 */
public class QueryEngine {
    
    /** QueryEngine instance.
     */
    private static QueryEngine queryEngine = null;
    
    private Hashtable dataCollection;
    
    /** Creates a new instance of QueryEngine */
    private QueryEngine() {
        dataCollection = new Hashtable();
    }
    
    /** creates an instance if not already created.
     * else returns the instance created.
     * @return instance of itself.
     */
    public static QueryEngine getInstance() {
        if(queryEngine == null) {
            queryEngine = new QueryEngine();
        }
        return queryEngine;
    }
    
    /** returns a subset of Data Collection.
     * @return Coeus Vector
     * @param comparableBean ComparableBean
     * @param key dataCollection key
     * @throws CoeusException CoeusException
     */
    public CoeusVector executeQuery(Object key, ComparableBean comparableBean)throws CoeusException {
        Hashtable data = (Hashtable)dataCollection.get(key);
        if(data == null) return new CoeusVector();
        CoeusVector coeusVector = (CoeusVector)data.get(comparableBean.getClass());
        if(coeusVector == null) return new CoeusVector();
        return cloneVectorData(coeusVector.isLike(comparableBean));
    }
    
    /** returns a subset of Data Collection. This subset is got by applying the
     * operator parameter. Operator can be a combination of multiple logical and
     * relational conditions.
     * @return Coeus Vector
     * @param beanClass bean Class
     * @param key dataCollection key
     * @param operator Operator
     * @throws CoeusException CoeusException
     */
    public CoeusVector executeQuery(Object key, Class beanClass, Operator operator)throws CoeusException {
        Hashtable data = (Hashtable)dataCollection.get(key);
        if(data == null) return new CoeusVector();
        CoeusVector dataVector = (CoeusVector)data.get(beanClass);
        if(dataVector == null) return new CoeusVector();
        return cloneVectorData(dataVector.filter(operator));
    }
    
    /**
     * returns a subset of Data Collection. This subset is got by applying the
     * operator parameter. Operator can be a combination of multiple logical and
     * relational conditions.
     * @return returns results in this Coeus Vector
     * @param collectionKey collection key
     * @param dataKey data key
     * @param operator Operator
     * @throws CoeusException CoeusException
     * @since 1.1
     */
    public CoeusVector executeQuery(Object collectionKey, Object dataKey, Operator operator) throws CoeusException {
        Hashtable data = (Hashtable)dataCollection.get(collectionKey);
        if(data == null) return new CoeusVector();
        CoeusVector dataVector = (CoeusVector)data.get(dataKey);
        if(dataVector == null) return new CoeusVector();
        return cloneVectorData(dataVector.filter(operator));
    }
    
    /** returns a subset of Data Collection. This subset is got by applying the
     * operator parameter. Operator can be a combination of multiple logical and
     * relational conditions. This method returns only those records which are not deleted.
     * @return Coeus Vector
     * @param beanClass bean Class
     * @param key dataCollection key
     * @param operator Operator
     * @throws CoeusException CoeusException
     */
    public CoeusVector getActiveData(Object key, Class beanClass, Operator operator) throws CoeusException {
        And and = new And(operator, CoeusVector.FILTER_ACTIVE_BEANS);
        return executeQuery(key, beanClass, and);
    }
    
    /**
     * returns a subset of Data Collection. This subset is got by applying the
     * operator parameter. Operator can be a combination of multiple logical and
     * relational conditions. This method returns only those records which are not deleted.
     * @return Coeus Vector
     * @param collectionKey collection Key
     * @param dataKey data Key
     * @param operator Operator
     * @throws CoeusException CoeusException
     * @since 1.1
     */
    public CoeusVector getActiveData(Object collectionKey, Object dataKey, Operator operator) throws CoeusException {
        And and = new And(operator, CoeusVector.FILTER_ACTIVE_BEANS);
        return executeQuery(collectionKey, dataKey, and);
        
    }
    
    /** returns a clone(copy) of dataVector which is passed as the parameter.
     * @param dataVector CoeusVector
     * @throws CoeusException CoeusException
     * @return CoeusVector
     */
    public CoeusVector cloneVectorData(CoeusVector dataVector) throws CoeusException {
        
        CoeusVector resultClone = new CoeusVector();
        if(dataVector == null) return resultClone;
        
        //return the clone not the original instances
        for(int index = 0; index < dataVector.size(); index++) {
            BaseBean baseBeanClone;
            try{
                //coeusBeanClone = (CoeusBean)ObjectCloner.deepCopy(coeusVector.get(index));
                resultClone.add(ObjectCloner.deepCopy(dataVector.get(index)));
            }catch (Exception exception) {
                throw new CoeusException(exception.getMessage());
            }
        }
        return resultClone;
    }
    
    /** returna all beans of this Class type
     * @return CoeusVector
     * @param key dataCollection key
     * @param beanClass beanClass
     * @throws CoeusException CoeusException
     */
    public CoeusVector getDetails(Object key, Class beanClass) throws CoeusException {
        Hashtable data = (Hashtable)dataCollection.get(key);
        CoeusVector coeusVector = new CoeusVector();
        if(data != null) coeusVector = (CoeusVector)data.get(beanClass);
        if(coeusVector == null) return new CoeusVector();
        return cloneVectorData(coeusVector);
    }
    
    /** returna all beans of this Class type
     * @return CoeusVector
     * @param key dataCollection key
     * @param collectionKey collectionKey
     * @throws CoeusException CoeusException
     */
    public CoeusVector getDetails(Object key, Object collectionKey)throws CoeusException {
        Hashtable data = (Hashtable)dataCollection.get(key);
        CoeusVector coeusVector = new CoeusVector();
        if(data != null) coeusVector = (CoeusVector)data.get(collectionKey);
        if(coeusVector == null) return new CoeusVector();
        return cloneVectorData(coeusVector);
    }
    
    /** adds a DatCollection(i.e. Hashtable) to QueryEngine.
     * @param key DatCollection key
     * @param value DataCollection(i.e. Hashtable)
     */
    public void addDataCollection(Object key, Hashtable value) {
        dataCollection.put(key, value);
    }
    
    /**
     * returns the hashtable contained with this key
     * @param key collection key
     * @return hashtable
     */    
    public Hashtable getDataCollection(Object key) {
        return (Hashtable)dataCollection.get(key);
    }

    /** removes a DatCollection(i.e. Hashtable) with the specified key from QueryEngine.
     * @param key key
     */
    public void removeDataCollection(Object key) {
        dataCollection.remove(key);
    }
    
    /** adds a collection to data collection.
     * @param key data collection key.
     * @param beanClass bean class.
     * @param collection collection to be added to datacollection.
     */ 
    public void addCollection(Object key, Class beanClass, CoeusVector collection) {
         Hashtable data = (Hashtable)dataCollection.get(key);
        
        if(data == null){
            addDataCollection(key, new Hashtable());
            data = (Hashtable)dataCollection.get(key);
        }
        data.put(beanClass, collection);
    }
    
    /** adds a collection to data collection.
     * @param key data collection key.
     * @param collectionKey collection key.
     * @param collection collection to be added to datacollection.
     */    
    public void addCollection(Object key, Object collectionKey, CoeusVector collection) {
        Hashtable data = (Hashtable)dataCollection.get(key);
        
        if(data == null){
            addDataCollection(key, new Hashtable());
            data = (Hashtable)dataCollection.get(key);
        }
        data.put(collectionKey, collection);
    }
    
    /** adds a CoeusBean to the DataCollection with the specified key
     * @param baseBean BaseBean
     * @param key key of the DataCollection
     */
    public void addData(Object key, BaseBean baseBean) {
        Hashtable data = (Hashtable)dataCollection.get(key);
        
        if(data == null){
            addDataCollection(key, new Hashtable());
            data = (Hashtable)dataCollection.get(key);
        }
        
        CoeusVector vector = (CoeusVector)data.get(baseBean.getClass());
        if(vector == null) data.put(baseBean.getClass(), new CoeusVector());
        
        ((CoeusVector)data.get(baseBean.getClass())).add(baseBean);
    }
    
    /** adds a CoeusBean to the DataCollection with the specified key
     * @param baseBean BaseBean
     * @param key key of the DataCollection
     */
    public void addData(Object key, Object dataKey, BaseBean baseBean) {
        Hashtable data = (Hashtable)dataCollection.get(key);
        
        if(data == null){
            addDataCollection(key, new Hashtable());
            data = (Hashtable)dataCollection.get(key);
        }
        
        CoeusVector vector = (CoeusVector)data.get(dataKey);
        if(vector == null) data.put(dataKey, new CoeusVector());
        
        ((CoeusVector)data.get(dataKey)).add(baseBean);
    }
    
    /** removes CoeusBean from the DataCollection with the specified key
     * @param baseBean BaseBean
     * @param key DataCollection key
     */
    public void removeData(Object key, BaseBean baseBean) {
        Hashtable data = (Hashtable)dataCollection.get(key);
        
        CoeusVector coeusVector = (CoeusVector)data.get(baseBean.getClass());
        
        coeusVector.remove(baseBean);
    }
    
    /** removes a CoeusBean from the DataCollection with the specified key
     * @param key DataCollection key
     * @param beanClass Class type of the bean to locate CoeusVector from
     * where the Object has to be removed
     * @param index index at which the Object has to be removed.
     */
    public void removeData(Object key, Class beanClass,  int index) {
        Hashtable data = (Hashtable)dataCollection.get(key);
        
        CoeusVector coeusVector = (CoeusVector)data.get(beanClass);
        
        coeusVector.remove(index);
    }
    
    /** removes a CoeusBean from the DataCollection with the specified key
     * @param key DataCollection key
     * @param beanClass Class type of the bean to locate CoeusVector from
     * where the Object has to be removed
     * @param index index at which the Object has to be removed.
     */
    public void removeData(Object key, Object dataKey,  Operator operator) {
        if(key == null || operator == null) return ;
        Hashtable data = (Hashtable)dataCollection.get(key);
        
        CoeusVector coeusVector = (CoeusVector)data.get(dataKey);
        if(coeusVector == null || coeusVector.size() == 0) return ;
        
        BaseBean  baseBean = null;
        for(int index = 0; index < coeusVector.size() ; index++){
            baseBean = (BaseBean)coeusVector.elementAt(index);
            if(baseBean == null) continue;
            if(operator.getResult(baseBean) == true){
                coeusVector.remove(index);
                index--;
            }
        }
    }
    
    /**
     * replaces this BaseBean in the collection.
     * @param collectionKey collection key
     * @param dataKey data key
     * @param baseBean base bean to replace
     * @since 1.1
     */    
    public void setData(Object collectionKey, Object dataKey, BaseBean baseBean) {
        Hashtable data = (Hashtable)dataCollection.get(collectionKey);
        if(data == null) return ;
        
        CoeusVector coeusVector = (CoeusVector)data.get(dataKey);
        if(coeusVector == null) return ;
        
        BaseBean bean;
        for(int index = 0; index < coeusVector.size(); index++) {
            bean = (CoeusBean)coeusVector.elementAt(index);
            if(baseBean.equals(bean)) {
                coeusVector.set(index, baseBean);
            }
        }
    }
    
    /** Removes bean instances from query engine.
     * @param key data collection key.
     * @param beanClass bean class.
     * @param operator operator.
     */
    public void removeData(Object key, Class beanClass, Operator operator) {
        if(key == null || operator == null) return ;
        Hashtable data = (Hashtable)dataCollection.get(key);
        
        CoeusVector coeusVector = (CoeusVector)data.get(beanClass);
        if(coeusVector == null || coeusVector.size() == 0) return ;
        
        BaseBean  baseBean = null;
        for(int index = 0; index < coeusVector.size() ; index++){
            baseBean = (BaseBean)coeusVector.elementAt(index);
            if(baseBean == null) continue;
            if(operator.getResult(baseBean) == true){
                coeusVector.remove(index);
                index--;
            }
        }
    }
    
    /** inserts a CoeusBean to the CoeusVector in a DataCollection.
     * @param key DataCollection key.
     * @param coeusBean coeusBean to be inserted
     */
    public void insert(Object key, CoeusBean coeusBean) {
        Hashtable data = (Hashtable)dataCollection.get(key);
        if(data == null){
            addDataCollection(key, new Hashtable());
            data = (Hashtable)dataCollection.get(key);
        }
        
        CoeusVector vector = (CoeusVector)data.get(coeusBean.getClass());
        if(vector == null) {
            vector =new CoeusVector();
            data.put(coeusBean.getClass(), vector);
        }
        
        coeusBean.setAcType(TypeConstants.INSERT_RECORD);
        CoeusBean coeusBeanToInsert;
        try{
            coeusBean = (CoeusBean)ObjectCloner.deepCopy(coeusBean);
            //Check if this bean already exists. if exists replace new bean with old one.
            for(int index = 0; index < vector.size(); index++) {
                coeusBeanToInsert = (CoeusBean)vector.elementAt(index);
                if(coeusBeanToInsert == null) continue;
                //if(vector.get(index) == null) continue;
                if(coeusBean.equals(coeusBeanToInsert)) {
                    //Managing Ac Types
                    
                    if(coeusBeanToInsert.getAcType() == null){
                        coeusBean.setUpdateTimestamp(coeusBeanToInsert.getUpdateTimestamp());
                        coeusBean.setAcType(TypeConstants.UPDATE_RECORD);
                    }
                    else if(coeusBeanToInsert.getAcType().equals(TypeConstants.UPDATE_RECORD)) {
                        coeusBean.setUpdateTimestamp(coeusBeanToInsert.getUpdateTimestamp());
                        coeusBean.setAcType(TypeConstants.UPDATE_RECORD);
                    }else if(coeusBeanToInsert.getAcType().equals(TypeConstants.DELETE_RECORD)) {
                        coeusBean.setUpdateTimestamp(coeusBeanToInsert.getUpdateTimestamp());
                        coeusBean.setAcType(TypeConstants.UPDATE_RECORD);
                    }
                    else {
                        coeusBean.setAcType(TypeConstants.INSERT_RECORD);
                    }
                   
                    vector.set(index, coeusBean);
                    return ;
                }
            }
            //((CoeusVector)data.get(coeusBean.getClass())).add(coeusBean);
            vector.add(coeusBean);
        }catch (Exception exception) {
            exception.printStackTrace();
        }
        
    }
    
    /**
     * inserts a CoeusBean to the CoeusVector in a DataCollection.
     * @param collectionKey collection Key
     * @param dataKey data Key
     * @param coeusBean coeusBean to be inserted
     * @since 1.1
     */
    public void insert(Object collectionKey, Object dataKey, CoeusBean coeusBean) {
        Hashtable data = (Hashtable)dataCollection.get(collectionKey);
        if(data == null){
            addDataCollection(collectionKey, new Hashtable());
            data = (Hashtable)dataCollection.get(collectionKey);
        }
        
        CoeusVector vector = (CoeusVector)data.get(dataKey);
        if(vector == null) {
            vector =new CoeusVector();
            data.put(dataKey, vector);
        }
        
        coeusBean.setAcType(TypeConstants.INSERT_RECORD);
        CoeusBean coeusBeanToInsert;
        try{
            coeusBean = (CoeusBean)ObjectCloner.deepCopy(coeusBean);
            //Check if this bean already exists. if exists replace new bean with old one.
            for(int index = 0; index < vector.size(); index++) {
                coeusBeanToInsert = (CoeusBean)vector.elementAt(index);
                if(coeusBeanToInsert == null) continue;
                //if(vector.get(index) == null) continue;
                if(coeusBean.equals(coeusBeanToInsert)) {
                    //Managing Ac Types
                    
                    if(coeusBeanToInsert.getAcType() == null){
                        coeusBean.setUpdateTimestamp(coeusBeanToInsert.getUpdateTimestamp());
                        coeusBean.setAcType(TypeConstants.UPDATE_RECORD);
                    }
                    else if(coeusBeanToInsert.getAcType().equals(TypeConstants.UPDATE_RECORD)) {
                        coeusBean.setUpdateTimestamp(coeusBeanToInsert.getUpdateTimestamp());
                        coeusBean.setAcType(TypeConstants.UPDATE_RECORD);
                    }else if(coeusBeanToInsert.getAcType().equals(TypeConstants.DELETE_RECORD)) {
                        coeusBean.setUpdateTimestamp(coeusBeanToInsert.getUpdateTimestamp());
                        coeusBean.setAcType(TypeConstants.UPDATE_RECORD);
                    }
                    else {
                        coeusBean.setAcType(TypeConstants.INSERT_RECORD);
                    }
                   
                    vector.set(index, coeusBean);
                    return ;
                }
            }
            //((CoeusVector)data.get(coeusBean.getClass())).add(coeusBean);
            vector.add(coeusBean);
        }catch (Exception exception) {
            exception.printStackTrace();
        }
        
    }//end insert
    
    
    /** updates a CoeusBean to the CoeusVector in a DataCollection.
     * @param key DataCollection key
     * @param coeusBean coeusBean to be updated
     * @throws CoeusException if operation cannot be performed
     */
    public void update(Object key, CoeusBean coeusBean) throws CoeusException{
        try{
            Hashtable data = (Hashtable)dataCollection.get(key);
            CoeusVector coeusVector = (CoeusVector)data.get(coeusBean.getClass());
            
            CoeusBean coeusBeanToUpdate = null;
            for(int index = 0; index < coeusVector.size(); index++) {
                
                coeusBeanToUpdate = (CoeusBean)coeusVector.elementAt(index);
                if(coeusBeanToUpdate == null) continue;
                if(coeusBeanToUpdate.equals(coeusBean)){
                    
                    //Managing AC Types
                    if(coeusBeanToUpdate.getAcType() == null){
                        coeusBean.setUpdateTimestamp(coeusBeanToUpdate.getUpdateTimestamp());
                        coeusBean.setAcType(TypeConstants.UPDATE_RECORD);
                    }
                    else if(coeusBeanToUpdate.getAcType().equals(TypeConstants.INSERT_RECORD)) {
                        coeusBean.setAcType(TypeConstants.INSERT_RECORD);
                    }
                    else if(coeusBeanToUpdate.getAcType().equals(TypeConstants.UPDATE_RECORD)) {
                        coeusBean.setUpdateTimestamp(coeusBeanToUpdate.getUpdateTimestamp());
                        coeusBean.setAcType(TypeConstants.UPDATE_RECORD);
                    }
                    else {
                        coeusBean.setAcType(TypeConstants.UPDATE_RECORD);
                    }
                    coeusVector.set(index, ObjectCloner.deepCopy(coeusBean));
                }//End if isLIke
                
            }//End For
        }catch (Exception exception) {
            throw new CoeusException(exception.getMessage());
        }
        
    }
    
    /**
     * updates a CoeusBean to the CoeusVector in a DataCollection.
     * @param collectionKey collection Key
     * @param dataKey data Key
     * @param coeusBean coeusBean to be updated
     * @throws CoeusException if operation cannot be performed
     * @since 1.1
     */
    public void update(Object collectionKey, Object dataKey, CoeusBean coeusBean) throws CoeusException{
        try{
            Hashtable data = (Hashtable)dataCollection.get(collectionKey);
            CoeusVector coeusVector = (CoeusVector)data.get(dataKey);
            
            CoeusBean coeusBeanToUpdate = null;
            for(int index = 0; index < coeusVector.size(); index++) {
                
                coeusBeanToUpdate = (CoeusBean)coeusVector.elementAt(index);
                if(coeusBeanToUpdate == null) continue;
                if(coeusBeanToUpdate.equals(coeusBean)){
                    
                    //Managing AC Types
                    if(coeusBeanToUpdate.getAcType() == null){
                        coeusBean.setUpdateTimestamp(coeusBeanToUpdate.getUpdateTimestamp());
                        coeusBean.setAcType(TypeConstants.UPDATE_RECORD);
                    }
                    else if(coeusBeanToUpdate.getAcType().equals(TypeConstants.INSERT_RECORD)) {
                        coeusBean.setAcType(TypeConstants.INSERT_RECORD);
                    }
                    else if(coeusBeanToUpdate.getAcType().equals(TypeConstants.UPDATE_RECORD)) {
                        coeusBean.setUpdateTimestamp(coeusBeanToUpdate.getUpdateTimestamp());
                        coeusBean.setAcType(TypeConstants.UPDATE_RECORD);
                    }
                    else {
                        coeusBean.setAcType(TypeConstants.UPDATE_RECORD);
                    }
                    coeusVector.set(index, ObjectCloner.deepCopy(coeusBean));
                }//End if isLIke
                
            }//End For
        }catch (Exception exception) {
            throw new CoeusException(exception.getMessage());
        }
        
    }//end update
    
    /** sets  a value to all the records in the vector.
     */
    public boolean setUpdate(Object key, Class beanClass, String fieldName, Class parameterType, Object value, Operator whereCondition) throws CoeusException {
        Hashtable data = (Hashtable)dataCollection.get(key);
        CoeusVector coeusVector = (CoeusVector)data.get(beanClass);
        //coeusVector = coeusVector.filter(whereCondition);
        
        if(coeusVector == null || coeusVector.size() == 0) return false;//throw new CoeusException("Nothing to Update");
        
        CoeusBean current;
        Field field = null;
        Method method = null;
        Class dataClass = beanClass;
        
        try{
            field = dataClass.getField(fieldName);
            
            //Field Not Available So cannot Set Values. just return.
            if(field == null) throw new CoeusException("Field Not Available"); ;
            
            if(! field.isAccessible()) {
                throw new NoSuchFieldException();
            }
        }catch (NoSuchFieldException noSuchFieldException) {
            try{
                String methodName = "set" + (fieldName.charAt(0)+"").toUpperCase()+ fieldName.substring(1);
                Class args[] = {parameterType};//{field.getType()};
                method = dataClass.getMethod(methodName, args);
            }catch (NoSuchMethodException noSuchMethodException) {
                throw new CoeusException(noSuchMethodException.getMessage());
            }
        }
        
        Object parameters[] = {value};
        try{
            for(int index = 0; index < coeusVector.size(); index++) {
                current  = (CoeusBean)coeusVector.get(index);
                
                if(whereCondition.getResult(current) == true){
                    
                    if(field != null && field.isAccessible()) {
                        field.set(current, value);
                    }
                    else{
                        method.invoke(current, parameters);
                    }
                    
                    /*if(! fieldName.equals("acType")) {
                        current.setAcType(TypeConstants.UPDATE_RECORD);
                        queryEngine.update(key, current);
                    }else {
                        queryEngine.set(key, current);
                    }*/
                    
                    coeusVector.set(index, current);
                    
                }
            }
            return true;
        }catch (IllegalAccessException illegalAccessException) {
            throw new CoeusException(illegalAccessException.getMessage());
        }catch (InvocationTargetException invocationTargetException) {
            throw new CoeusException(invocationTargetException.getMessage());
        }
        
    }
    
    /** sets  a value to all the records in the vector.
     */
    public boolean setUpdate(Object key, Object dataKey, Class beanClass, String fieldName, Class parameterType, Object value, Operator whereCondition) throws CoeusException {
        Hashtable data = (Hashtable)dataCollection.get(key);
        CoeusVector coeusVector = (CoeusVector)data.get(dataKey);
        //coeusVector = coeusVector.filter(whereCondition);
        
        if(coeusVector == null || coeusVector.size() == 0) return false;//throw new CoeusException("Nothing to Update");
        
        CoeusBean current;
        Field field = null;
        Method method = null;
        Class dataClass = beanClass;
        
        try{
            field = dataClass.getField(fieldName);
            
            //Field Not Available So cannot Set Values. just return.
            if(field == null) throw new CoeusException("Field Not Available"); ;
            
            if(! field.isAccessible()) {
                throw new NoSuchFieldException();
            }
        }catch (NoSuchFieldException noSuchFieldException) {
            try{
                String methodName = "set" + (fieldName.charAt(0)+"").toUpperCase()+ fieldName.substring(1);
                Class args[] = {parameterType};//{field.getType()};
                method = dataClass.getMethod(methodName, args);
            }catch (NoSuchMethodException noSuchMethodException) {
                throw new CoeusException(noSuchMethodException.getMessage());
            }
        }
        
        Object parameters[] = {value};
        try{
            for(int index = 0; index < coeusVector.size(); index++) {
                current  = (CoeusBean)coeusVector.get(index);
                
                if(whereCondition.getResult(current) == true){
                    
                    if(field != null && field.isAccessible()) {
                        field.set(current, value);
                    }
                    else{
                        method.invoke(current, parameters);
                    }
                    
                    /*if(! fieldName.equals("acType")) {
                        current.setAcType(TypeConstants.UPDATE_RECORD);
                        queryEngine.update(key, current);
                    }else {
                        queryEngine.set(key, current);
                    }*/
                    
                    coeusVector.set(index, current);
                    
                }
            }
            return true;
        }catch (IllegalAccessException illegalAccessException) {
            throw new CoeusException(illegalAccessException.getMessage());
        }catch (InvocationTargetException invocationTargetException) {
            throw new CoeusException(invocationTargetException.getMessage());
        }
        
    }
    
    
    /** removes/ sets the Ac Type of a CoeusBean in the CoeusVector in a DataCollection.
     * @param key DataCollection key
     * @param coeusBean coeusBean to be removed/changed Ac type.
     * @throws CoeusException if the operation cannot be performed.
     */
    public void delete(Object key, CoeusBean coeusBean) throws CoeusException {
        try{
            Hashtable data = (Hashtable)dataCollection.get(key);
            CoeusVector coeusVector = (CoeusVector)data.get(coeusBean.getClass());
            
            CoeusBean coeusBeanToDelete = null;
            
            for(int index = 0; index < coeusVector.size(); index++) {
                coeusBeanToDelete = (CoeusBean)coeusVector.elementAt(index);
                if(coeusBeanToDelete == null) continue;
                if(coeusBeanToDelete.equals(coeusBean)){
                    
                    //Managing AC Types
                    if(coeusBeanToDelete.getAcType() == null){
                        coeusBean.setAcType(TypeConstants.DELETE_RECORD);
                    }
                    else if(coeusBeanToDelete.getAcType().equals(TypeConstants.INSERT_RECORD)) {
                        //removeData(key, coeusBean.getClass(), index);
                        coeusVector.remove(index);
                        index--;
                        continue;
                    }
                    else if(coeusBeanToDelete.getAcType().equals(TypeConstants.UPDATE_RECORD)) {
                        coeusBean.setAcType(TypeConstants.DELETE_RECORD);
                    }
                    else {
                        coeusBean.setAcType(TypeConstants.DELETE_RECORD);
                    }
                    
                    coeusVector.set(index, ObjectCloner.deepCopy(coeusBean));
                    
                }//End if isLike
                
            }
        }catch (Exception exception) {
            throw new CoeusException(exception.getMessage());
        }
        
    }
    
    /**
     * removes/ sets the Ac Type of a CoeusBean in the CoeusVector in a DataCollection.
     * @param collectionKey collection Key
     * @param dataKey data Key
     * @param coeusBean coeusBean to be removed/changed Ac type.
     * @throws CoeusException if the operation cannot be performed.
     * @since 1.1
     */
    public void delete(Object collectionKey, Object dataKey, CoeusBean coeusBean) throws CoeusException {
        try{
            Hashtable data = (Hashtable)dataCollection.get(collectionKey);
            CoeusVector coeusVector = (CoeusVector)data.get(dataKey);
            
            CoeusBean coeusBeanToDelete = null;
            
            for(int index = 0; index < coeusVector.size(); index++) {
                coeusBeanToDelete = (CoeusBean)coeusVector.elementAt(index);
                if(coeusBeanToDelete == null) continue;
                if(coeusBeanToDelete.equals(coeusBean)){
                    
                    //Managing AC Types
                    if(coeusBeanToDelete.getAcType() == null){
                        coeusBean.setAcType(TypeConstants.DELETE_RECORD);
                    }
                    else if(coeusBeanToDelete.getAcType().equals(TypeConstants.INSERT_RECORD)) {
                        //removeData(key, coeusBean.getClass(), index);
                        coeusVector.remove(index);
                        index--;
                        continue;
                    }
                    else if(coeusBeanToDelete.getAcType().equals(TypeConstants.UPDATE_RECORD)) {
                        coeusBean.setAcType(TypeConstants.DELETE_RECORD);
                    }
                    else {
                        coeusBean.setAcType(TypeConstants.DELETE_RECORD);
                    }
                    
                    coeusVector.set(index, ObjectCloner.deepCopy(coeusBean));
                    
                }//End if isLike
                
            }
        }catch (Exception exception) {
            throw new CoeusException(exception.getMessage());
        }
        
    }//end delete
    
    public void set(Object key, CoeusBean coeusBean)throws CoeusException {
        if(coeusBean.getAcType() == null) return ;
        
        if(coeusBean.getAcType().equals(TypeConstants.INSERT_RECORD)) {
            insert(key, coeusBean);
        }else if(coeusBean.getAcType().equals(TypeConstants.UPDATE_RECORD)) {
            update(key, coeusBean);
        }else if(coeusBean.getAcType().equals(TypeConstants.DELETE_RECORD)) {
            delete(key, coeusBean);
        }
    }
    
    public Enumeration getKeyEnumeration(Object key) {
        return ((Hashtable)dataCollection.get(key)).keys();
    }
    
    public Enumeration getKeys() {
        return dataCollection.keys();
    }

    
    
}

