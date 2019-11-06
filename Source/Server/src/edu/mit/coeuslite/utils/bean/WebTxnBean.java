/*
 * WebTxnBean.java
 *
 * Created on December 7, 2005, 12:27 PM
 */

package edu.mit.coeuslite.utils.bean;

import edu.mit.coeus.bean.PersonInfoBean;
import edu.mit.coeus.bean.UserInfoBean;
import edu.mit.coeus.exception.CoeusException;
import edu.mit.coeus.utils.ObjectCloner;
import edu.mit.coeus.utils.dbengine.DBEngineConstants;
import edu.mit.coeus.utils.dbengine.DBEngineImpl;
import edu.mit.coeus.utils.dbengine.DBException;
import edu.mit.coeus.utils.dbengine.Parameter;
import edu.mit.coeus.utils.dbengine.ProcReqParameter;
import edu.mit.coeuslite.utils.SessionConstants;
import edu.mit.coeuslite.utils.statement.Procedure;
import edu.mit.coeuslite.utils.statement.Result;
import edu.mit.coeuslite.utils.statement.SQL;
import edu.mit.coeuslite.utils.statement.Statement;
import edu.mit.coeuslite.utils.statement.StatementConstants;
import edu.mit.coeuslite.utils.statement.StatementContentHandler;
import edu.mit.coeuslite.utils.statement.StatementFactory;
import edu.mit.coeuslite.utils.statement.StatementMap;
import edu.mit.coeuslite.utils.statement.StatementType;
import edu.mit.coeuslite.utils.xmlReader.ReadJSPPlaceHolder;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.sql.Timestamp;
import java.text.ParseException;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Vector;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import org.apache.commons.beanutils.BeanUtilsBean;
import org.apache.commons.beanutils.Converter;
import org.apache.commons.beanutils.DynaProperty;
import org.apache.struts.action.DynaActionForm;
import org.apache.struts.action.DynaActionFormClass;
import org.apache.struts.config.FormBeanConfig;
import org.apache.struts.config.ModuleConfig;
import org.apache.struts.util.RequestUtils;
import org.w3c.dom.Document;
import java.lang.*;
import java.lang.reflect.InvocationTargetException;
import java.text.SimpleDateFormat;
import java.util.Iterator;
import java.util.Set;
import javax.servlet.http.HttpSession;
import org.apache.commons.beanutils.DynaBean;
import org.apache.struts.validator.DynaValidatorForm;

/**
 *
 * @author  nadhgj
 */
public class WebTxnBean {

    private edu.mit.coeuslite.utils.DateUtils dateUtils = new edu.mit.coeuslite.utils.DateUtils();
    private static final String TIME_STAMP = "yyyy-MM-dd";
    private static final String REQUIRED_DATE_FORMAT = TIME_STAMP +" hh:mms ";
    private SimpleDateFormat simpleDateFormat = new SimpleDateFormat(REQUIRED_DATE_FORMAT);
    private static final String TIME_STAMP_CONSTANT = "Timestamp";

    /** Creates a new instance of ProceedureTest */
    public WebTxnBean() {
    }



    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here

    }
    
    /** Identifies the transaction/Statement id's and prepares procParamster for the
     *Transaction/statements and then retrives the data. It reads the XML data 
     *using StatementContentHandler.
     *@paramm req contains User Info.
     *@param statementId to build SQL query.
     *@param bean contains details for get\modify\insert
     *@returns Object
     */

   public Object getResults(HttpServletRequest req, String statementId,Object bean) 
            throws IOException,CoeusException,DBException,Exception{
        HttpSession session = req.getSession();
        ServletContext servletContext = session.getServletContext();
//        UserInfoBean userInfoBean = (UserInfoBean)session.getAttribute("user"+session.getId());
//        String userId = userInfoBean.getUserId();
        PersonInfoBean personInfo = (PersonInfoBean)session.getAttribute(SessionConstants.LOGGED_IN_PERSON);
        String userId = personInfo.getUserName();
        Vector vecProcParams = null;
        Vector vecSql = null;
        List lsStatements = null;
        HashMap hmDataContent =new HashMap();
        if(userId!= null){
            StatementContentHandler statementContentHandler = (StatementContentHandler)servletContext.getAttribute(StatementConstants.STATEMENT_HANDLER);
            lsStatements = statementContentHandler.findStatement(statementId);
            StatementFactory statementFactory = new StatementFactory();
            vecProcParams = new Vector(5,3);
            vecSql = new Vector();
            for(int index=0;index<lsStatements.size();index++) {
                Statement statement = (Statement)lsStatements.get(index);
                StatementType statementType = statementFactory.getStatementType(statement);
                ProcReqParameter procReqParameter = statementType.execute(bean, userId);
                //Vinay: Fix for Oracle 10g DB/Client Error Start
                //If the query type of SQL, then set all parameters to sql vector.
                //If the query type of Procedure, then set all parameters to procedure vector.
                //put the vector to hashmap and pass it to runProcedure method.
                if(statement.getType()!= null && statement.getType().equalsIgnoreCase("sql")){
                    vecSql.addElement(procReqParameter);
                    hmDataContent.put(SQL.class,vecSql);
                }else{
                    vecProcParams.addElement(procReqParameter);
                    hmDataContent.put(Procedure.class,vecProcParams);
                }
                //Vinay: Fix for Oracle 10g DB/Client Error End
            }
        }

        //Vinay: Fix for Oracle 10g DB/Client Error Start
        //runProcedures method is called with hashmap as one of the argument
        //and this hashmap is having the vector which is having parameters of
        //sql query or procedure.
        return runProcedures(hmDataContent, bean, lsStatements, req);
        //Vinay: Fix for Oracle 10g DB/Client Error End

    }
/**
 * This method is to Insert/Update multiple statements in single transaction
 * @paramm req contains User Info.
 * @param statementId to build SQL query.
 * @param bean contains details for get\modify\insert
 *@returns Object
 */
    public Object getResultsData(HttpServletRequest req, String statementId, HashMap dataObject)
    throws IOException,CoeusException,DBException,Exception{
        Vector vecData  =  new Vector();
        ServletContext servletContext = req.getSession().getServletContext();
        HttpSession session = req.getSession();
        UserInfoBean userInfoBean = (UserInfoBean)session.getAttribute("user"+session.getId());
        String userId = userInfoBean.getUserId();
        DynaValidatorForm dynaForm = null;
        Vector vecProcParams = null;
        List lsStatements = null;
        if(userId!= null){
            StatementContentHandler statementContentHandler = (StatementContentHandler)servletContext.getAttribute(StatementConstants.STATEMENT_HANDLER);
            lsStatements = statementContentHandler.findStatement(statementId);
            int statementSize = lsStatements.size();
            StatementFactory statementFactory = new StatementFactory();
            vecProcParams = new Vector(5,3);
            for(int index=0 ; index < statementSize ; index++) {
                Statement statement = (Statement)lsStatements.get(index);
                StatementType statementType = statementFactory.getStatementType(statement);
                Set keySet = dataObject.keySet();
                Iterator iterator = keySet.iterator();
                
                while(iterator.hasNext()){
                    Object key = iterator.next();
                    Vector value = (Vector)dataObject.get(key);
                    if(key.equals(statement.getId())){
                        vecData.addElement(statement);
                        for(int porcIndex = 0; porcIndex < value.size(); porcIndex++){
                            dynaForm = (DynaValidatorForm)value.get(porcIndex);
                            ProcReqParameter procReqParameter = statementType.execute(dynaForm, userId);
                            vecProcParams.addElement(procReqParameter);
                        }
                    }
                }
            }
        }
        // remove those data which doesn't have in the statment
        if(vecData!= null &&vecData.size() > 0){
            lsStatements.clear();
            lsStatements.addAll(vecData);            
        }        
        
        return runProcedures(vecProcParams, dynaForm, lsStatements, req);        
    }
    
   
   
   
    /* This method gets data for a given procedureId from the DB
     * @param procedureId this holds ProcedureID for a specific Procedure.
     * @param bean this bean contains data for SqlQuery 
     * @return Vector of DynaActionForms.
     * @exception DBException if any error during database transaction. 
     * @exception CoeusException if the instance of dbEngine is not available.
     */
    private Object runProcedures(Vector procedures,Object bean, List lsStatements, HttpServletRequest request) throws CoeusException, DBException,Exception{
        Vector result = null;
        if(procedures != null) {
            DBEngineImpl dbEngine = new DBEngineImpl();
            if(dbEngine !=null){
                result = dbEngine.executeStoreProcs(procedures);                
            }else{
                throw new CoeusException("db_exceptionCode.1000");
            }
        }else {
            throw new CoeusException();
        }
        if(result != null && result.size()>0 ) {
            if(result.get(0) instanceof HashMap) {
                Vector outVector = new Vector();
                outVector.addElement(result);
                return returnResultSet(outVector, lsStatements, request);
            }else if(result.get(0) instanceof Vector) {
                return returnResultSet(result, lsStatements, request);
            } 
        }
        Hashtable htResult = new Hashtable();
        htResult.put(result.getClass(),result);
        return htResult;
    }
    
    
    /* This method gets data for a given procedureId from the DB
     * @param procedureId this holds ProcedureID for a specific Procedure.
     * @param bean this bean contains data for SqlQuery 
     * @return Vector of DynaActionForms.
     * @exception DBException if any error during database transaction. 
     * @exception CoeusException if the instance of dbEngine is not available.
     */
    private Object runProcedures(HashMap procedures,Object bean, List lsStatements, HttpServletRequest request) throws CoeusException, DBException,Exception{
        Vector result = null;
        Vector vecProcContent = null;
        Vector vecSqlContent = null;
        
        if(procedures!= null ){
            vecProcContent = (Vector)procedures.get(Procedure.class);
            vecSqlContent = (Vector)procedures.get(SQL.class);
        }
        
        if(vecProcContent != null) {
            DBEngineImpl dbEngine = new DBEngineImpl();
            if(dbEngine !=null){
                result = dbEngine.executeStoreProcs(vecProcContent);
            }else{
                throw new CoeusException("db_exceptionCode.1000");
            }
        }else if(vecSqlContent!= null){
            DBEngineImpl dbEngine = new DBEngineImpl();
            if(dbEngine !=null){
                for(int index = 0 ; index < vecSqlContent.size();index++ ){
                    ProcReqParameter procReqParameter = (ProcReqParameter)vecSqlContent.get(index);
                    result = dbEngine.executePreparedQuery("coeus",procReqParameter.getSqlCommand(),procReqParameter.getParameterInfo());
                }
            }else{
                throw new CoeusException("db_exceptionCode.1000");
            }
        }else{
            throw new CoeusException();
        }
        
        
        if(result != null && result.size()>0 ) {
            if(result.get(0) instanceof HashMap) {
                Vector outVector = new Vector();
                outVector.addElement(result);
                return returnResultSet(outVector, lsStatements, request);
            }else if(result.get(0) instanceof Vector) {
                return returnResultSet(result, lsStatements, request);
            } 
        }
        Hashtable htResult = new Hashtable();
        htResult.put(result.getClass(),result);
        return htResult;
    }
    
    /** It gets the vector data contains hashmap and then process the 
     *output data based on the output attribute specified in Transacion.XML. 
     *It looks for the DynaValidatorForm, if it doesn't found then it will
     *search for the CustomBean else returns the hashmap to the client
     *@param vecData contains data from the DB
     *@returns Hashtable keys as StatementIds
     */
    private Hashtable returnResultSet(Vector vecData, List lsStatements, HttpServletRequest request) throws Exception{
        Hashtable result = new Hashtable();
        for(int index=0;index<lsStatements.size();index++) {
            Vector vecInner = (Vector)vecData.get(index);
            Statement statement = (Statement)lsStatements.get(index);
            Result statementResult = statement.getResult();
            if( statementResult != null) {
                DynaBean dynaBean = instantiateDynaForm(statementResult.getType(), request);
                if(dynaBean != null) {
                    result.put(statement.getId(), getDynaSet(vecInner, dynaBean, statementResult, dynaBean));
                }else {
                    result.put(statement.getId(), getBeanSet(vecInner, statementResult));
                }
            } else if(statement.getStatementOutputList() != null){
                //result.put(statement.getId(),vecInner.get(0));
                 for(int innerIndex=0; innerIndex < vecInner.size(); innerIndex++){
                    result.put(statement.getId(),vecInner.get(innerIndex));
                }
            }

        }
        
        return result;
    }
    
    /** It gets the properticies of the bean from Transaction.XML and the result
     *obtained from database.
     *It prepares a custom bean specified in the output attribute in Trasaction.XML
     *and fetches the data in to it and returns to the client
     *@param vecResult contains HashMaps 
     *@param statementResult contains properties of bean
     *@returns Vector of Custom beans.
     */

    private Vector getBeanSet(Vector vecResult, Result statementResult)  throws Exception {
        Vector vecBeanData = new Vector();
        if(vecResult != null && vecResult.size() > 0) {
            Constructor constructor = getClassObject(statementResult.getType());
            for(int index=0; index<vecResult.size();index++) {
                Object bean = constructor.newInstance(new Object[] {});
                vecBeanData.addElement(prepareCustomBean((HashMap)vecResult.get(index), bean, statementResult.getStatementMapList()));
            }
        }
        return vecBeanData;
    }
    
    /** It gets the properticies of the bean from Transaction.XML and the result
     *obtained from database.
     *@param record contains bean data 
     *@param bean to which the record data will be fetched
     *@param statementMaps contains properties of bean.
     *@returns Object(Custom bean).
     *@exception NoSuchMethodException if specified method is not available,
     */

    private Object prepareCustomBean(HashMap record, Object bean, List statementMaps) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException,Exception{
        Method method;
        Field field ;
        Class dataClass = bean.getClass();
        String methodName, fieldName, type = null;
        Object value;
        StatementMap procMap = null;
        BeanUtilsBean beanUtilsBean = new BeanUtilsBean();
        for(int index=0; index<statementMaps.size(); index++) {
            procMap = (StatementMap)statementMaps.get(index);
            String strField = procMap.getField();

            methodName = (strField.charAt(0)+"").toUpperCase()+ strField.substring(1);
            method = findMethod(methodName, bean);
            if(method != null) {
                Class[] paramType  = method.getParameterTypes();

                Object[] args = new Object[1];
                args[0] = convertion(paramType[0],record.get(procMap.getParameter()));

                method.invoke(bean, args);
            }

        }
        return bean;
    }

    /** @param name the name the is contained in the method.
       * getXXX/setXXX where XXX is the name (case insensitive..)
       * @return the method found
       */
      private Method findMethod(String name, Object bean) throws NoSuchMethodException{
          Class dataClass = bean.getClass();
          if (bean == null) {
              throw new NoSuchMethodException();
//              return null;
          }
          Method[] methods = dataClass.getMethods();
          String pre = null;
          for (int i = 0; i < methods.length; i++) {
              Method method = methods[i];
              if (method.getName().equalsIgnoreCase("set" + name)) {
                  return method;
              } 
//              else if (method.getName().equalsIgnoreCase("is" + name)) {
//                  return method;
//              }
          }
//          throw new NoSuchMethodException();
          return null;
      }

    /** It gets a class object for a given string
     * @param strClass contains filly qualified path of CustomBean
     *@returns Constructor for strClass.
     */
    public synchronized Constructor getClassObject(String strClass) throws Exception {
        Constructor con = null;
        try {
            Class customBean = Class.forName(strClass);
            con = customBean.getConstructor(new Class[] {});

        }catch(Exception ex) {
            throw new Exception(ex.getMessage());
        }
        return con;
    }


    /* This method generates DynaActionForms. 
     * @param vecResult this holds records .
     * @return Vector of DynaActionForms
     */
    private Object getDynaSet(Vector vecResult, Object bean, Result statementResult, DynaBean dynaActionForm)  throws Exception {
        Vector vecDynaSet = new Vector();
        if(vecResult != null && vecResult.size() > 0) {
            for(int index=0; index<vecResult.size();index++) {
                vecDynaSet.addElement(generateDynaForm((HashMap)vecResult.get(index),bean, statementResult.getStatementMapList(), dynaActionForm));
            }
        }
        return vecDynaSet;
    }

    /* This method Generates DynaActionForm for a record 
     * @param record holds table row Data.
     * @return DynaActionForm
     */
    private DynaBean generateDynaForm(HashMap record,Object bean, List statementMaps, DynaBean dynaActionForm) throws Exception {
        StatementMap procMap = null;
        DynaBean newDynaActionForm = null;
        BeanUtilsBean beanUtilsBean = new BeanUtilsBean();
        newDynaActionForm = ((DynaBean)bean).getDynaClass().newInstance();
        //dynaActionForm = newDynaActionForm;
        HashMap dynaMap = (HashMap)((DynaActionForm)newDynaActionForm).getMap();
        for(int index=0; index<statementMaps.size(); index++) {
            procMap = (StatementMap)statementMaps.get(index);
            String field = procMap.getField();
            if(!dynaMap.containsKey(field))
                continue;
            Object value = record.get(procMap.getParameter());
            DynaProperty descriptor = dynaActionForm.getDynaClass().getDynaProperty(field);
            if (descriptor.getType() == null) {
                throw new NullPointerException
                ("The type for property '" + field + "' is invalid");
            }
            // check if the paramterType is Timestamp. If it is then
            // call the overloaded method to get the Timestamp.
            if(procMap.getParameterType().equals(TIME_STAMP_CONSTANT)){
                value = convertion(procMap.getParameterType(),descriptor.getType(),value);
            }else if(field.equals("onOffCampusFlag")){
               value = convertion(value, field, descriptor.getType());
            } else{
                value = convertion(descriptor.getType(),value);
            }
            
            newDynaActionForm.set(field,value);
        }

        return newDynaActionForm;
    }

    /** Instantiate the DynaValidatorForm from ModuleConfig. It needs form Name
     *to identify the respective formBean
     *@param formName specifies the name of the for in Struts-config.xml
     *@returns true if it founds else false
     */
    private DynaBean instantiateDynaForm(String formName, HttpServletRequest req) throws Exception {
        ServletContext servletContext = req.getSession().getServletContext();
        ModuleConfig moduleConfig = RequestUtils.getModuleConfig(req, servletContext);
        FormBeanConfig formConfig = moduleConfig.findFormBeanConfig(formName);
        if(formConfig == null)
            return null;
        DynaActionFormClass dynaClass = DynaActionFormClass.createDynaActionFormClass(formConfig);
        DynaBean dynaActionForm = (DynaBean)dynaClass.newInstance();
        return dynaActionForm;
    }

    /* This method convrtes one object type to Target Type.
     * @param name holds the DynaActionForm's property.
     * @param value holds value of an object ,
     * which has to be converted to DynaActionForm's propertyType
     * @return Object of destination type.
     */
    private Object convertion(Class type, Object value) throws Exception {
        if (value == null) {
            if (type.isPrimitive()) {
                throw new NullPointerException
                ("Primitive value for '" + type + "'");
            }
        } else {
            if(value instanceof java.sql.Timestamp) {
                value = dateConvertion(type, value);
            }else {
                BeanUtilsBean beanUtilsBean = new BeanUtilsBean();
                Converter converter = beanUtilsBean.getConvertUtils().lookup(type);
                value = converter.convert(type, value);
            }
        }

        return value;
    }
    
    /* This is a overloaded method 
     *This method convrtes one object type to Target Type which is of type Timestamp.
     * @param name holds the DynaActionForm's property and also property from Transaction.XML.
     * @param value holds value of an object ,
     * which has to be converted to DynaActionForm's propertyType
     * @return Object of destination type.
     */
    private Object convertion(String paramType, Class type, Object value) throws Exception {
        if (value == null) {
            if (type.isPrimitive()) {
                throw new NullPointerException
                ("Primitive value for '" + type + "'");
            }
        } else {
            if(value instanceof java.sql.Timestamp) {
                value = dateConvertion(paramType, type, value);
            }else {
                BeanUtilsBean beanUtilsBean = new BeanUtilsBean();
                Converter converter = beanUtilsBean.getConvertUtils().lookup(type);
                value = converter.convert(type, value);
            }
        }

        return value;
    }
    /** This overloaded method is used for the boolean conversion
     *for on-off campus flag. If the boolean value is true the flag should be N
     *If the boolean value is false - F. This scenario will occur for on-off campus falg
     * @param name holds the DynaActionForm's property and also property from Transaction.XML.
     * @param value holds value of an object ,
     * which has to be converted to DynaActionForm's propertyType
     * @return Object of destination type.
     */
    private Object convertion(Object value, String fieldName, Class type) throws Exception {
        if (value == null) {
            if (type.isPrimitive()) {
                throw new NullPointerException
                ("Primitive value for '" + type + "'");
            }
            
        } else {
                BeanUtilsBean beanUtilsBean = new BeanUtilsBean();
                value = onOffConvertion(value);
                Converter converter = beanUtilsBean.getConvertUtils().lookup(type);
                value = converter.convert(type, value);
            
        }

        return value;
    }
    /* This Overlaoded method converts a boolean object to a String Object
     * @param value holds boolean value
     * @return string object.This is written for on-offCampusFlag
     *If the value is true then set it N
     *If the value is false F
     */
    private String onOffConvertion(Object value) {
        String defaultValue = "F";
        if (value == null) {
            return defaultValue;
        }
        String stringValue = value.toString();
        defaultValue = (stringValue.equalsIgnoreCase("N")?"Y":"N");
        return defaultValue;
    }
    /** Date Type conversion and returns object of the converted Type.
     *@param value holds value of an object
     *@param type holds type to which value should be converted.
     *@returns object of converted type.
     */
    
    
    private Object dateConvertion(Class type, Object value) throws ParseException {
        if(value != null ) {
            if(value instanceof String) {
                value = dateUtils.formatDate(value.toString(),edu.mit.coeuslite.utils.DateUtils.MM_DD_YYYY);
            }else {
                if (type==java.sql.Date.class) {
                    return new java.sql.Date(((java.util.Date)value).getTime());
                }else if(type==java.util.Date.class){
                    return value;
                }else if(type==java.sql.Timestamp.class) {
                   value = simpleDateFormat.format(value);
                   return value;
                }else if(type==java.lang.String.class) {
                    value = value.toString();//to get the date as respectively stored in the database
                }
            }
        }
        return value;
    }
    
    /** This is an overloaded method which will identify Timestamp type from
     *Transaction.XML and gets the Timestamp object with proper format
     */
     private Object dateConvertion(String paramType, Class type, Object value) throws ParseException {
        if(value != null ) {
            if(value instanceof String) {
                value = dateUtils.formatDate(value.toString(),edu.mit.coeuslite.utils.DateUtils.MM_DD_YYYY);
            }else if(paramType.equals(TIME_STAMP_CONSTANT) && value instanceof Timestamp){
                Timestamp stamp  = Timestamp.valueOf(value.toString());
                value = stamp.toString();
            }else {
                if (type==java.sql.Date.class) {
                    return new java.sql.Date(((java.util.Date)value).getTime());
                }else if(type==java.util.Date.class){
                    return value;
                }else if(type==java.sql.Timestamp.class) {
                   value = simpleDateFormat.format(value);
                   return value;
                }else if(type==java.lang.String.class) {
                    value = value.toString();//to get the date as respectively stored in the database
                }
            }
        }
        return value;
    }

}
