/*
 * @(#)DepartmentPersonTxnBean.java 1.0 03/10/03 9:50 AM
 *
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 */

/*
 * PMD check performed, and commented unused imports and variables on 19-APR-2011
 * by Maharaja Palanichamy
 */

package edu.mit.coeus.departmental.bean;

import edu.mit.coeus.award.bean.AwardTxnBean;
import edu.mit.coeus.customelements.bean.CustomElementsInfoBean;
import edu.mit.coeus.instprop.bean.InstituteProposalTxnBean;
import edu.mit.coeus.utils.ModuleConstants;
import edu.mit.coeus.utils.dbengine.*;
import edu.mit.coeus.exception.CoeusException;
import edu.mit.coeus.departmental.bean.*;
import edu.mit.coeus.utils.ComboBoxBean;
import edu.mit.coeus.utils.KeyConstants;

//import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Vector;
import java.util.HashMap;
import java.util.Hashtable;
import java.sql.Date;
import java.sql.Timestamp;

import edu.mit.coeus.utils.CoeusVector;
/**
 * This class provides the methods for performing all procedure executions for
 * a Department Person detail functionality. Various methods are used to fetch
 * the Department Person details from the Database.
 * All methods are used <code>DBEngineImpl</code> singleton instance for the
 * database interaction.
 *
 * @version 1.0 on March 11, 2003, 3:44 PM
 * @author  Mukundan C/subramanya
 */

public class DepartmentPersonTxnBean {
    
    // Singleton instance of a dbEngine
    private DBEngineImpl dbEngine;
    
    /**
     * Creates a new instance of DepartmentPersonTxnBean
     * @param personID String represent the person ID.
     */
    public DepartmentPersonTxnBean() {
        dbEngine = new DBEngineImpl();
        
    }
    
    /**
     *  This method used get person bio graphy number for the person id
     *  <li>To fetch the data, it uses the function GET_MAX_FOR_PERSON_BIO.
     *
     *  @return int bio graphy number for the person id
     *  @param String person id to get biography number
     *  @exception DBException if any error during database transaction.
     *  @exception CoeusException if the instance of dbEngine is not available.
     */
    public int getPersonBiographyNumber(String personId)
    throws CoeusException, DBException {
        int number = 0;
        
        Vector param= new Vector();
        Vector result = new Vector();
        param.add(new Parameter("PERSON_ID",
                DBEngineConstants.TYPE_STRING,personId));
        /* calling stored function */
        if(dbEngine!=null){
            result = dbEngine.executeFunctions("Coeus",
                    "{ <<OUT INTEGER NUMBER>> = "
                    +" call GET_MAX_FOR_PERSON_BIO(<< PERSON_ID >> ) }", param);
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }
        if(!result.isEmpty()){
            HashMap rowParameter = (HashMap)result.elementAt(0);
            number = Integer.parseInt(rowParameter.get("NUMBER").toString());
        }
        return number;
    }
    
    /**
     *  This method used get the paramater values for the other tab of departmental
     *  person screen  from osp$parameter.
     *  <li>To fetch the data, it uses the function GET_PARAMETER_VALUE.
     *
     *  @return String parameter value
     *  @param String parammeter for which the values will returned
     *  @exception DBException if any error during database transaction.
     *  @exception CoeusException if the instance of dbEngine is not available.
     */
    public String getParameterValues(String parameter)
    throws CoeusException, DBException {
        String value = null;
        Vector param= new Vector();
        Vector result = new Vector();
        param.add(new Parameter("PARAMATER",DBEngineConstants.TYPE_STRING,parameter));
        /* calling stored function */
        if(dbEngine!=null){
            result = dbEngine.executeFunctions("Coeus",
                    "{ <<OUT STRING VALUE>> = "
                    +" call GET_PARAMETER_VALUE(<< PARAMATER >> ) }", param);
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }
        if(!result.isEmpty()){
            HashMap rowParameter = (HashMap)result.elementAt(0);
            //Added By Raghunath P.V.
            if(rowParameter.get("VALUE") != null){
                value = rowParameter.get("VALUE").toString();
            }
        }
        return value;
    }
    
    /**
     *  This method is used to get unit name for the given unit number
     *  <li>To fetch the data, it uses get_unit_name procedure.
     *
     *  @param unitNumber String
     *  @return unitname String
     *  @exception DBException if any error during database transaction.
     *  @exception CoeusException if the instance of dbEngine is not available.
     */
    public String getUnitName(String unitNumber)
    throws DBException,CoeusException{
        Vector param= new Vector();
        Vector result = new Vector();
        
        String unitName = null;
        param.add(new Parameter("UNITNUMBER","String",unitNumber.trim()));
        
        /* CASE #1798 Comment Begin */
        //calling stored function
        /*if(dbEngine!=null){
            result = dbEngine.executeRequest("Coeus",
            "call get_unit_name( << UNITNUMBER >> , << OUT STRING UNITNAME >> ) ",
            "Coeus", param);
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }*/
        /* CASE #1798 Begin */
        /* calling stored function */
        if(dbEngine!=null){
            result = dbEngine.executeFunctions("Coeus",
                    "{ <<OUT STRING UNITNAME>> = "
                    +" call FN_GET_UNIT_NAME (<< UNITNUMBER >> ) }", param);
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }
        /* CASE #1798 End */
        
        if(!result.isEmpty()){
            HashMap row = (HashMap)result.elementAt(0);
            //Added By Raghunath P.V.
            if ( row.get("UNITNAME") != null){
                unitName = row.get("UNITNAME").toString();
            }
        }
        return unitName;
    }
    
    /**
     *  This method is used to get home unit for the given person id
     *  <li>To fetch the data, it uses GET_HOME_UNIT procedure.
     *
     *  @param personId String for which the home unit is returned
     *  @return home unit String
     *  @exception DBException if any error during database transaction.
     *  @exception CoeusException if the instance of dbEngine is not available.
     */
    public String getHomeUnit(String personId)
    throws DBException,CoeusException{
        Vector param= new Vector();
        Vector result = new Vector();
        
        String homeUnit = null;
        param.add(new Parameter("PERSON_ID","String",personId.trim()));
        
        //calling stored function
        if(dbEngine!=null){
            result = dbEngine.executeRequest("Coeus",
                    "call GET_HOME_UNIT( << PERSON_ID >> , << OUT STRING HOME_UNIT >> ) ",
                    "Coeus", param);
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }
        if(!result.isEmpty()){
            HashMap rowHome = (HashMap) result.elementAt(0);
            //Object  hmUnit = rowHome.get("HOME_UNIT");
            //Added By Raghunath P.V.
            if ( rowHome.get("HOME_UNIT") != null){
                homeUnit = rowHome.get("HOME_UNIT").toString();
            }
        }
        return homeUnit;
    }
    
    /**
     *  This method is used to get user person infomation for the given person id
     *  <li>To fetch the data, it uses GET_USER_INFO_FOR_PERSON procedure.
     *
     *  @param person id String
     *  @return Vector of userid and unit name for person id
     *  @exception DBException if any error during database transaction.
     *  @exception CoeusException if the instance of dbEngine is not available.
     */
    public Vector getUserInfoForPerson(String personId)
    throws DBException,CoeusException{
        Vector param= new Vector();
        Vector result = new Vector();
        Vector dataResult = new Vector();
        HashMap rowPerson = null;
        String userId = null;
        String unitNumber = null;
        param.add(new Parameter("PERSON_ID","String",personId.trim()));
        
        //calling stored function
        if(dbEngine!=null){
            result = dbEngine.executeRequest("Coeus",
                    "call GET_USER_INFO_FOR_PERSON ( << PERSON_ID >> , "+
                    " << OUT STRING USERID >> ,<< OUT STRING UNITNUMBER >> ) ", "Coeus", param);
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }
        if(!result.isEmpty()){
            rowPerson = (HashMap)result.elementAt(0);
            //Object  user = rowPerson.get("USERID");
            //Added By Raghunath P.V.
            if ( rowPerson.get("USERID") != null && rowPerson.get("UNITNUMBER") != null){
                userId = rowPerson.get("USERID").toString();
                unitNumber = rowPerson.get("UNITNUMBER").toString();
            }
        }
        dataResult.addElement(userId);
        dataResult.addElement(unitNumber);
        return dataResult;
    }
    
    /**
     *  This method used populates combo box with type code and description in the
     *  for Departmental person screen on the argument string from different table.
     *  <li>To fetch the data, it uses the procedure DW_GET_ARG_CODE_TBL_NEW.
     *
     *  @param String argument on which the procedure will decide from which table
     *  it will get the code and description .
     *  @return Vector map of all type code and description
     *  @exception DBException if any error during database transaction.
     *  @exception CoeusException if the instance of dbEngine is not available.
     */
    public Vector getArgumentCodeDescription(String argument)
    throws CoeusException,DBException{
        Vector result = null;
        Vector vecArgumentTypes = null;
        Vector param= new Vector();
        HashMap hasArgumentTypes = null;
        param.add(new Parameter("ARGUMENT_STRING","String",argument.trim()));
        if(dbEngine != null){
            result = new Vector(3,2);
            result = dbEngine.executeRequest("Coeus",
                    "call GET_ARG_CODE_TBL( <<ARGUMENT_STRING>> , <<OUT RESULTSET rset>> )",
                    "Coeus", param);
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }
        int typeCount = result.size();
        if (typeCount > 0){
            vecArgumentTypes = new Vector();
            for(int rowIndex=0; rowIndex<typeCount; rowIndex++){
                hasArgumentTypes = (HashMap)result.elementAt(rowIndex);
                vecArgumentTypes.addElement(new ComboBoxBean(
                        hasArgumentTypes.get(
                        "TYPE_CODE").toString(),
                        hasArgumentTypes.get("DESCRIPTION").toString()));
            }
        }
        return vecArgumentTypes;
    }
    
    /**
     *  This method used populates combo box with type code and description in the
     *  for Departmental person screen on the argument string from different table.
     *  <li>To fetch the data, it uses the procedure DW_GET_COSTELEMENT_LIST.
     *
     *  @return Vector map of all type code and description
     *  @exception DBException if any error during database transaction.
     *  @exception CoeusException if the instance of dbEngine is not available.
     */
    public Vector getCostElements() throws CoeusException, DBException{
        Vector result = null;
        Vector param= new Vector();
        HashMap costElementRow = null;
        if(dbEngine !=null){
            result = new Vector(3,2);
            result = dbEngine.executeRequest("Coeus",
                    "call DW_GET_COSTELEMENT_LIST( <<OUT RESULTSET rset>> )", "Coeus", param);
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }
        int listSize = result.size();
        Vector budgetList = null;
        if (listSize > 0){
            budgetList = new Vector();
            for(int rowIndex=0; rowIndex<listSize; rowIndex++){
                costElementRow = (HashMap)result.elementAt(rowIndex);
                budgetList.addElement(new ComboBoxBean(
                        costElementRow.get("COST_ELEMENT").toString(),
                        costElementRow.get("DESCRIPTION").toString()));
            }
        }
        return budgetList;
    }
    
    
    /**
     *  This method used populates combo box with degree Type Code in the
     *  Department person screen from OSP$DEGREE_TYPE.
     *  <li>To fetch the data, it uses the procedure GET_DEGREE_TYPES.
     *
     *  @return Vector map of all DegreeType code with DegreeCode
     *  code as key and DegreeCode description as value.
     *  @exception DBException if any error during database transaction.
     *  @exception CoeusException if the instance of dbEngine is not available.
     */
    public Vector getDegreeTypeCodeDescription() throws CoeusException,DBException{
        Vector result = null;
        Vector vecDegreeCode = null;
        Vector param= new Vector();
        HashMap hasDegreeCode = null;
        if(dbEngine != null){
            result = new Vector(3,2);
            result = dbEngine.executeRequest("Coeus",
                    "call GET_DEGREE_TYPES( <<OUT RESULTSET rset>> )",
                    "Coeus", param);
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }
        int degreeCount = result.size();
        if (degreeCount > 0){
            vecDegreeCode = new Vector();
            for(int rowIndex=0; rowIndex<degreeCount; rowIndex++){
                hasDegreeCode = (HashMap)result.elementAt(rowIndex);
                //System.out.println( "Degree Code is >>"+hasDegreeCode.get( "DEGREE_CODE"));
                vecDegreeCode.addElement(new ComboBoxBean(
                        hasDegreeCode.get(
                        "DEGREE_CODE").toString(),
                        hasDegreeCode.get("DESCRIPTION").toString()));
            }
        }
        return vecDegreeCode;
    }
    
    
    /**
     *  This method used populates combo box with school Code in the
     *  Department person screen from OSP$SCHOOL_CODE.
     *  <li>To fetch the data, it uses the procedure GET_SCHOOL_CODES.
     *
     *  @return Vector map of all school code with schoolCode
     *  code as key and schoolCode description as value.
     *  @exception DBException if any error during database transaction.
     *  @exception CoeusException if the instance of dbEngine is not available.
     */
    public Vector getSchoolCodeDescription() throws CoeusException,DBException{
        Vector result = null;
        Vector vecSchoolCode = null;
        Vector param= new Vector();
        HashMap hasSchoolCode = null;
        if(dbEngine != null){
            result = new Vector(3,2);
            result = dbEngine.executeRequest("Coeus",
                    "call GET_SCHOOL_CODES( <<OUT RESULTSET rset>> )",
                    "Coeus", param);
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }
        int degreeCount = result.size();
        if (degreeCount > 0){
            vecSchoolCode = new Vector();
            for(int rowIndex=0; rowIndex<degreeCount; rowIndex++){
                hasSchoolCode = (HashMap)result.elementAt(rowIndex);
                vecSchoolCode.addElement(new ComboBoxBean(
                        hasSchoolCode.get(
                        "SCHOOL_CODE").toString(),
                        hasSchoolCode.get("DESCRIPTION").toString()));
            }
        }
        return vecSchoolCode;
    }
    
    /**
     * Gets all the person Details for other tab for specific person ID. The
     * return Vector(Collection) contains column name ,column value,column label
     * and data type  from OSP$PERSON_CUSTOM_DATA table.
     * <li>To fetch the data, it uses the procedure DW_GET_PERSON_CUSTOM_DATA.
     *
     * @param proposalNumber Specific proposal Number.
     * @return PersonCustomElementsInfoBean attributes of the person.
     * @exception DBException if any error during database transaction.
     * @exception CoeusException if the instance of dbEngine is not available.
     */
    public Vector getPersonOthersDetails(String personId)
    throws CoeusException, DBException{
        Vector result = null;
        Vector param= new Vector();
        PersonCustomElementsInfoBean departmentOthersFormBean = null;
        HashMap othersRow = null;
        param.addElement(new Parameter("PERSON_ID",
                DBEngineConstants.TYPE_STRING,personId));
        if(dbEngine !=null){
            result = new Vector(3,2);
            result = dbEngine.executeRequest("Coeus",
                    "call GET_PERSON_CUSTOM_DATA( <<PERSON_ID>> , "+
                    "<<OUT RESULTSET rset>> )", "Coeus", param);
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }
        int listSize = result.size();
        Vector othersList = null;
        if (listSize > 0){
            othersList = new Vector(3,2);
            for(int rowIndex=0; rowIndex<listSize; rowIndex++){
                departmentOthersFormBean = new PersonCustomElementsInfoBean();
                othersRow = (HashMap)result.elementAt(rowIndex);
                departmentOthersFormBean.setPersonId( (String)
                othersRow.get("PERSON_ID"));
                departmentOthersFormBean.setColumnName( (String)
                othersRow.get("COLUMN_NAME"));
                departmentOthersFormBean.setColumnValue( (String)
                othersRow.get("COLUMN_VALUE"));
                departmentOthersFormBean.setColumnLabel( (String)
                othersRow.get("COLUMN_LABEL"));
                departmentOthersFormBean.setDataType( (String)
                othersRow.get("DATA_TYPE"));
                departmentOthersFormBean.setDataLength(
                        Integer.parseInt(othersRow.get("DATA_LENGTH") == null ? "0" :
                            othersRow.get("DATA_LENGTH").toString()));
                departmentOthersFormBean.setDefaultValue( (String)
                othersRow.get("DEFAULT_VALUE"));
                departmentOthersFormBean.setHasLookUp(
                        othersRow.get("HAS_LOOKUP") == null ? false :
                            (othersRow.get("HAS_LOOKUP").toString()
                            .equalsIgnoreCase("y") ? true :false));
                departmentOthersFormBean.setLookUpWindow( (String)
                othersRow.get("LOOKUP_WINDOW"));
                departmentOthersFormBean.setLookUpArgument( (String)
                othersRow.get("LOOKUP_ARGUMENT"));
                departmentOthersFormBean.setDescription( (String)
                othersRow.get("DESCRIPTION"));
                departmentOthersFormBean.setUpdateTimestamp(
                        (Timestamp)othersRow.get("UPDATE_TIMESTAMP"));
                departmentOthersFormBean.setUpdateUser( (String)
                othersRow.get("UPDATE_USER"));
                othersList.add(departmentOthersFormBean);
            }
        }
        return othersList;
    }
    
    /**
     * Gets all the person Details for budget menu tab . The
     * return Vector(Collection) contains cost element,description and etc
     * from OSP$BUDGET_CATEGORY table.
     * <li>To fetch the data, it uses the procedure GET_ACTIVE_COSTELEMENT_LIST.
     *
     * @return DepartmentBudgetFormBean attributes of the person.
     * @exception DBException if any error during database transaction.
     * @exception CoeusException if the instance of dbEngine is not available.
     */
    public Vector getPersonBudgetDetails()
    throws CoeusException, DBException{
        Vector result = null;
        Vector param= new Vector();
        DepartmentBudgetFormBean departmentBudgetFormBean = null;
        HashMap budgetRow = null;
        
        if(dbEngine !=null){
            result = new Vector(3,2);
            // COEUSQA-1414 Allow schools to indicate if cost element is still active - Start
            result = dbEngine.executeRequest("Coeus",
                    "call GET_ACTIVE_COSTELEMENT_LIST( <<OUT RESULTSET rset>> )", "Coeus", param);
            // COEUSQA-1414 Allow schools to indicate if cost element is still active - End
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }
        int listSize = result.size();
        Vector budgetList = null;
        if (listSize > 0){
            budgetList = new Vector(3,2);
            for(int rowIndex=0; rowIndex<listSize; rowIndex++){
                departmentBudgetFormBean = new DepartmentBudgetFormBean();
                budgetRow = (HashMap)result.elementAt(rowIndex);
                departmentBudgetFormBean.setCostElement( (String)
                budgetRow.get("COST_ELEMENT"));
                departmentBudgetFormBean.setDescription( (String)
                budgetRow.get("DESCRIPTION"));
                departmentBudgetFormBean.setBudgetCategoryCode(
                        Integer.parseInt(budgetRow.get("BUDGET_CATEGORY_CODE") == null ? "0" :
                            budgetRow.get("BUDGET_CATEGORY_CODE").toString()));
                departmentBudgetFormBean.setOnOffCampusFlag(
                        budgetRow.get("ON_OFF_CAMPUS_FLAG") == null ? false :
                            (budgetRow.get("ON_OFF_CAMPUS_FLAG").toString()
                            .equalsIgnoreCase("N") ? true :false));
                departmentBudgetFormBean.setUpdateTimestamp( (Timestamp)
                budgetRow.get("UPDATE_TIMESTAMP"));
                departmentBudgetFormBean.setUpdateUser( (String)
                budgetRow.get("UPDATE_USER"));
                departmentBudgetFormBean.setCategoryType(
                        budgetRow.get("CATEGORY_TYPE") == null ? ' ' :
                            ((String)budgetRow.get("CATEGORY_TYPE")).charAt(0));
                budgetList.add(departmentBudgetFormBean);
            }
        }
        return budgetList;
    }
    
    
    /**
     * Gets all the person Details for argumentvalus list . The
     * return Vector(Collection) contains cost argument name,value and description
     * from OSP$ARG_VALUE_LOOKUP table.
     * <li>To fetch the data, it uses the procedure DW_GET_ARG_VALUE_LIST.
     *
     * @param String argumentName to get the argument details for departmental screen
     * @return DepartmentArgListFormBean attributes of the person.
     * @exception DBException if any error during database transaction.
     * @exception CoeusException if the instance of dbEngine is not available.
     */
    public Vector getArgumentValueList(String argumentName)
    throws CoeusException, DBException{
        Vector result = null;
        Vector param= new Vector();
//        DepartmentArgListFormBean departmentArgListFormBean = null;
        HashMap argValueRow = null;
        param.addElement(new Parameter("ARGUMENT_NAME",
                DBEngineConstants.TYPE_STRING,argumentName));
        if(dbEngine !=null){
            result = new Vector(3,2);
            result = dbEngine.executeRequest("Coeus",
                    "call DW_GET_ARG_VALUE_LIST( <<ARGUMENT_NAME>>, <<OUT RESULTSET rset>> )",
                    "Coeus", param);
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }
        int listSize = result.size();
        Vector argValueList = null;
        /*if (listSize > 0){
            argValueList = new Vector(3,2);
            for(int rowIndex=0; rowIndex<listSize; rowIndex++){
            departmentArgListFormBean = new DepartmentArgListFormBean();
            argValueRow = (HashMap)result.elementAt(rowIndex);
            departmentArgListFormBean.setArgumentName( (String)
                            argValueRow.get("ARGUMENT_NAME"));
            departmentArgListFormBean.setValue( (String)
                            argValueRow.get("VALUE"));
            departmentArgListFormBean.setDescription( (String)
                            argValueRow.get("DESCRIPTION"));
            argValueList.add(departmentArgListFormBean);
                }
            }*/
        if (listSize > 0){
            argValueList = new Vector();
            for(int rowIndex=0; rowIndex<listSize; rowIndex++){
                argValueRow = (HashMap)result.elementAt(rowIndex);
                /*argValueList.addElement(new ComboBoxBean(
                argValueRow.get("VALUE").toString(),
                argValueRow.get("DESCRIPTION").toString()));
                 */
                argValueList.addElement(new ComboBoxBean(
                        (String)argValueRow.get("VALUE"),
                        (String)argValueRow.get("DESCRIPTION")));
                
            }
        }
        return argValueList;
    }
    
    public String getDescForLookupCode(String code, String lookupWindow, String lookupArgument)throws CoeusException, DBException{
        String value = null;

        Vector param= new Vector();
        Vector result = new Vector();
        param.add(new Parameter("CODE", DBEngineConstants.TYPE_STRING, code));
        param.add(new Parameter("LOOKUP_WINDOW", DBEngineConstants.TYPE_STRING, lookupWindow));
        param.add(new Parameter("LOOKUP_ARGUMENT", DBEngineConstants.TYPE_STRING, lookupArgument));
        
        /* calling stored function */
        if(dbEngine!=null){
            result = dbEngine.executeFunctions("Coeus",
                    "{ <<OUT STRING DESCRIPTION>> = "
                    +" call get_desc_for_lookup_code(<< CODE >>, <<LOOKUP_WINDOW>>, <<LOOKUP_ARGUMENT>> ) }", param);
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }
        if(!result.isEmpty()){
            HashMap rowParameter = (HashMap)result.elementAt(0);
            value = (String)rowParameter.get("DESCRIPTION");
        }
        return value;
    }

    /**
     * Gets all the person Details for person column module list . The
     * return Vector(Collection) contains column name,column value ,column label and etc
     * from OSP$CUSTOM_DATA_ELEMENT_USAGE ,OSP$CUSTOM_DATA_ELEMENTS  table.
     * <li>To fetch the data, it uses the procedure DW_GET_CUST_COLUMNS_FOR_MODULE.
     *
     * @param String moduleCode to get the person column details for departmental screen
     * @return PersonCustomElementsInfoBean attributes of the person.
     * @exception DBException if any error during database transaction.
     * @exception CoeusException if the instance of dbEngine is not available.
     */
    public Vector getPersonColumnModule(String moduleCode)
    throws CoeusException, DBException{
        Vector result = null;
        Vector param= new Vector();
        CustomElementsInfoBean departmentOthersFormBean = null;
        HashMap othersRow = null;
        param.addElement(new Parameter("MODULE_CODE",
                DBEngineConstants.TYPE_STRING,moduleCode));
        if(dbEngine !=null){
            result = new Vector(3,2);
            result = dbEngine.executeRequest("Coeus",
                    "call DW_GET_CUST_COLUMNS_FOR_MODULE( <<MODULE_CODE>> , "+
                    "<<OUT RESULTSET rset>> )", "Coeus", param);
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }
        int listSize = result.size();
        Vector othersList = null;
        if (listSize > 0){
            othersList = new Vector(3,2);
            for(int rowIndex=0; rowIndex<listSize; rowIndex++){
                departmentOthersFormBean = new CustomElementsInfoBean();
                othersRow = (HashMap)result.elementAt(rowIndex);
                departmentOthersFormBean.setColumnName( (String)
                othersRow.get("COLUMN_NAME"));
                departmentOthersFormBean.setColumnValue( (String)
                othersRow.get("COLUMN_VALUE"));
                departmentOthersFormBean.setColumnLabel( (String)
                othersRow.get("COLUMN_LABEL"));
                departmentOthersFormBean.setDataType( (String)
                othersRow.get("DATA_TYPE"));
                departmentOthersFormBean.setDataLength(
                        Integer.parseInt(othersRow.get("DATA_LENGTH") == null ? "0" :
                            othersRow.get("DATA_LENGTH").toString()));
                departmentOthersFormBean.setDefaultValue( (String)
                othersRow.get("DEFAULT_VALUE"));
                departmentOthersFormBean.setHasLookUp(
                        othersRow.get("HAS_LOOKUP") == null ? false :
                            (othersRow.get("HAS_LOOKUP").toString()
                            .equalsIgnoreCase("y") ? true :false));
                departmentOthersFormBean.setLookUpWindow( (String)
                othersRow.get("LOOKUP_WINDOW"));
                departmentOthersFormBean.setLookUpArgument( (String)
                othersRow.get("LOOKUP_ARGUMENT"));
                departmentOthersFormBean.setRequired(
                        othersRow.get("IS_REQUIRED") == null ? false :
                            (othersRow.get("IS_REQUIRED").toString()
                            .equalsIgnoreCase("y") ? true :false));
                
                othersList.add(departmentOthersFormBean);
            }
        }
        return othersList;
    }
    
    /**
     * Gets all the person Details for specific proposal number. The return Vector(Collection)
     * contains complete person information like person id, ssn, last name, first name,
     * age, gender, race, degree, vertern type etc.
     * @param proposalNumber Specific proposal Number.
     * @return DepartmentPersonFormBean attributes of the person.
     * @exception CoeusException raised if dbEngine is Null.
     * @exception DBException raised during db transactional error.
     */
    public DepartmentPersonFormBean getAllPersonDetails(
            String proposalNumber )throws CoeusException, DBException {
        
        Vector resultPerson = new Vector(3, 2);
        Vector personParameter = new Vector();
        personParameter.addElement(new Parameter("PROPOSAL_NUMBER",
                DBEngineConstants.TYPE_STRING,
                proposalNumber ));
        if (dbEngine != null) {
            
            //Execute the  DB Procedure and Stores the result in Vector.
            resultPerson = dbEngine.executeRequest("Coeus",
                    "call DW_GET_PROP_PERSON ( <<PROPOSAL_NUMBER>>, " +
                    " <<OUT RESULTSET rset>>) ",
                    "Coeus", personParameter );
        } else {
            throw new CoeusException("db_exceptionCode.1000");
        }
        
        int personSize = resultPerson.size();
        
        if ( personSize < 1) {
            return null;
        }
        
//        Vector personResultSet = new Vector(3, 2);
        HashMap personRow = null;
        DepartmentPersonFormBean personBean = new DepartmentPersonFormBean();
        personRow = (HashMap) resultPerson.elementAt(0);
        
        personBean.setPersonId( (String)
        personRow.get("PROPOSAL_NUMBER"));
        personBean.setPersonId( (String)
        personRow.get("PERSON_ID"));
        personBean.setSsn( (String)
        personRow.get("SSN"));
        personBean.setLastName( (String)
        personRow.get("LAST_NAME"));
        personBean.setFirstName( (String)
        personRow.get("FIRST_NAME"));
        personBean.setMiddleName( (String)
        personRow.get("MIDDLE_NAME"));
        personBean.setFullName( (String)
        personRow.get("FULL_NAME"));
        personBean.setPriorName( (String)
        personRow.get("PRIOR_NAME"));
        personBean.setUserName( (String)
        personRow.get("USER_NAME"));
        personBean.setEmailAddress( (String)
        personRow.get("EMAIL_ADDRESS"));
        personBean.setDateOfBirth(
                personRow.get("DATE_OF_BIRTH") == null ?
                    null : new Date(((Timestamp) personRow.get(
                "DATE_OF_BIRTH")).getTime()));
        personBean.setAge(Integer.parseInt(personRow.get("AGE") == null ? "0" :
            personRow.get("AGE").toString()));
        personBean.setAgeByFiscalYear(
                Integer.parseInt(personRow.get("AGE_BY_FISCAL_YEAR") == null ? "0" :
                    personRow.get("AGE_BY_FISCAL_YEAR").toString()));
        personBean.setGender( (String)
        personRow.get("GENDER"));
        personBean.setRace( (String)
        personRow.get("RACE"));
        personBean.setEduLevel( (String)
        personRow.get("EDUCATION_LEVEL"));
        personBean.setDegree( (String)
        personRow.get("DEGREE"));
        personBean.setMajor( (String)
        personRow.get("MAJOR"));
        personBean.setHandicap(
                personRow.get("IS_HANDICAPPED") == null ? false :
                    (personRow.get("IS_HANDICAPPED").toString()
                    .equalsIgnoreCase("y") ? true :false));
        personBean.setHandiCapType( (String)
        personRow.get("HANDICAP_TYPE"));
        personBean.setVeteran(
                personRow.get("IS_VETERAN") == null ? false :
                    (personRow.get("IS_VETERAN").toString()
                    .equalsIgnoreCase("y") ? true :false));
        personBean.setVeteranType( (String)
        personRow.get("VETERAN_TYPE"));
        personBean.setVisaCode( (String)
        personRow.get("VISA_CODE"));
        personBean.setVisaType( (String)
        personRow.get("VISA_TYPE"));
        personBean.setVisaRenDate(
                personRow.get("VISA_RENEWAL_DATE") == null ?
                    null : new Date(((Timestamp) personRow.get(
                "VISA_RENEWAL_DATE")).getTime()));
        personBean.setHasVisa(
                personRow.get("HAS_VISA") == null ? false :
                    (personRow.get("HAS_VISA").toString()
                    .equalsIgnoreCase("y") ? true :false));
        personBean.setOfficeLocation( (String)
        personRow.get("OFFICE_LOCATION"));
        personBean.setOfficePhone( (String)
        personRow.get("OFFICE_PHONE"));
        personBean.setSecOfficeLocation( (String)
        personRow.get("SECONDRY_OFFICE_LOCATION"));
        personBean.setSecOfficePhone( (String)
        personRow.get("SECONDRY_OFFICE_PHONE"));
        personBean.setSchool( (String)
        personRow.get("SCHOOL"));
        personBean.setYearGraduated( (String)
        personRow.get("YEAR_GRADUATED"));
        personBean.setDirDept( (String)
        personRow.get("DIRECTORY_DEPARTMENT"));
        personBean.setSaltuation( (String)
        personRow.get("SALUTATION"));
        personBean.setCountryCitizenship( (String)
        personRow.get("COUNTRY_OF_CITIZENSHIP"));
        personBean.setPrimaryTitle( (String)
        personRow.get("PRIMARY_TITLE"));
        personBean.setDirTitle( (String)
        personRow.get("DIRECTORY_TITLE"));
        personBean.setHomeUnit( (String)
        personRow.get("HOME_UNIT"));
        personBean.setFaculty(
                personRow.get("IS_FACULTY") == null ? false :
                    (personRow.get("IS_FACULTY").toString()
                    .equalsIgnoreCase("y") ? true :false));
        personBean.setGraduateStudentStaff(
                personRow.get("IS_GRADUATE_STUDENT_STAFF") == null ? false :
                    (personRow.get("IS_GRADUATE_STUDENT_STAFF").toString()
                    .equalsIgnoreCase("y") ? true :false));
        personBean.setResearchStaff(
                personRow.get("IS_RESEARCH_STAFF") == null ? false :
                    (personRow.get("IS_RESEARCH_STAFF").toString()
                    .equalsIgnoreCase("y") ? true :false));
        personBean.setServiceStaff(
                personRow.get("IS_SERVICE_STAFF") == null ? false :
                    (personRow.get("IS_SERVICE_STAFF").toString()
                    .equalsIgnoreCase("y") ? true :false));
        personBean.setSupportStaff(
                personRow.get("IS_SUPPORT_STAFF") == null ? false :
                    (personRow.get("IS_SUPPORT_STAFF").toString()
                    .equalsIgnoreCase("y") ? true :false));
        personBean.setOtherAcademicGroup(
                personRow.get("IS_OTHER_ACCADEMIC_GROUP") == null ? false :
                    (personRow.get("IS_OTHER_ACCADEMIC_GROUP").toString()
                    .equalsIgnoreCase("y") ? true :false));
        personBean.setMedicalStaff(
                personRow.get("IS_MEDICAL_STAFF") == null ? false :
                    (personRow.get("IS_MEDICAL_STAFF").toString()
                    .equalsIgnoreCase("y") ? true :false));
        personBean.setVacationAccural(
                personRow.get("VACATION_ACCURAL") == null ? false :
                    (personRow.get("VACATION_ACCURAL").toString()
                    .equalsIgnoreCase("y") ? true :false));
        personBean.setOnSabbatical(
                personRow.get("IS_ON_SABBATICAL") == null ? false :
                    (personRow.get("IS_ON_SABBATICAL").toString()
                    .equalsIgnoreCase("y") ? true :false));
        personBean.setProvided( (String)
        personRow.get("ID_PROVIDED"));
        personBean.setVerified( (String)
        personRow.get("ID_VERIFIED"));
        personBean.setUpdateTimestamp( (Timestamp)
        personRow.get("UPDATE_TIMESTAMP"));
        personBean.setUpdateUser( (String)
        personRow.get("UPDATE_USER"));
        //COEUSQA-1674 - Allow Division Lead Unit to be modified in the person details - Start
        personBean.setDivision( (String)
        personRow.get("DIVISION"));
        //COEUSQA-1674 - Allow Division Lead Unit to be modified in the person details - End
        
        return personBean;
    }
    
    /**
     * Gets all the person Details for specific person ID. The return Vector(Collection)
     * contains complete person information like person id, ssn, last name, first name,
     * age, gender, race, degree, vertern type etc.
     * @param proposalNumber Specific proposal Number.
     * @return ProposalPersonFormBean attributes of the person.
     * @exception CoeusException raised if dbEngine is Null.
     * @exception DBException raised during db transactional error.
     */
    public DepartmentPersonFormBean getPersonDetails(
            String personId )throws CoeusException, DBException {
        
        Vector resultPerson = new Vector(3, 2);
        Vector param = new Vector();
        param.addElement(new Parameter("PERSON_ID",
                DBEngineConstants.TYPE_STRING,personId ));
        if (dbEngine != null) {
            
            //Execute the  DB Procedure and Stores the result in Vector.
            resultPerson = dbEngine.executeRequest("Coeus",
                    "call dw_get_person (  <<PERSON_ID>> , <<OUT RESULTSET rset>> )   ",
                    "Coeus", param);
        } else {
            throw new CoeusException("db_exceptionCode.1000");
        }
        
        int personSize = resultPerson.size();
        
        if ( personSize < 1) {
            return null;
        }
        
//        Vector personResultSet = new Vector(3, 2);
        HashMap personRow = null;
        DepartmentPersonFormBean personBean = new DepartmentPersonFormBean();
        personRow = (HashMap) resultPerson.elementAt(0);
        personBean.setPersonId( (String)
        personRow.get("PERSON_ID"));
        personBean.setSsn( (String)
        personRow.get("SSN"));
        personBean.setLastName( (String)
        personRow.get("LAST_NAME"));
        personBean.setFirstName( (String)
        personRow.get("FIRST_NAME"));
        personBean.setMiddleName( (String)
        personRow.get("MIDDLE_NAME"));
        personBean.setFullName( (String)
        personRow.get("FULL_NAME"));
        personBean.setPriorName( (String)
        personRow.get("PRIOR_NAME"));
        personBean.setUserName( (String)
        personRow.get("USER_NAME"));
        personBean.setEmailAddress( (String)
        personRow.get("EMAIL_ADDRESS"));
        personBean.setDateOfBirth(
                personRow.get("DATE_OF_BIRTH") == null ?
                    null : new Date(((Timestamp) personRow.get(
                "DATE_OF_BIRTH")).getTime()));
        personBean.setAge(Integer.parseInt(personRow.get("AGE") == null ? "0" :
            personRow.get("AGE").toString()));
        personBean.setAgeByFiscalYear(
                Integer.parseInt(personRow.get("AGE_BY_FISCAL_YEAR") == null ? "0" :
                    personRow.get("AGE_BY_FISCAL_YEAR").toString()));
        personBean.setGender( (String)
        personRow.get("GENDER"));
        personBean.setRace( (String)
        personRow.get("RACE"));
        personBean.setEduLevel( (String)
        personRow.get("EDUCATION_LEVEL"));
        personBean.setDegree( (String)
        personRow.get("DEGREE"));
        personBean.setMajor( (String)
        personRow.get("MAJOR"));
        personBean.setHandicap(
                personRow.get("IS_HANDICAPPED") == null ? false :
                    (personRow.get("IS_HANDICAPPED").toString()
                    .equalsIgnoreCase("y") ? true :false));
        personBean.setHandiCapType( (String)
        personRow.get("HANDICAP_TYPE"));
        personBean.setVeteran(
                personRow.get("IS_VETERAN") == null ? false :
                    (personRow.get("IS_VETERAN").toString()
                    .equalsIgnoreCase("y") ? true :false));
        personBean.setVeteranType( (String)
        personRow.get("VETERAN_TYPE"));
        personBean.setVisaCode( (String)
        personRow.get("VISA_CODE"));
        personBean.setVisaType( (String)
        personRow.get("VISA_TYPE"));
        personBean.setVisaRenDate(
                personRow.get("VISA_RENEWAL_DATE") == null ?
                    null : new Date(((Timestamp) personRow.get(
                "VISA_RENEWAL_DATE")).getTime()));
        personBean.setHasVisa(
                personRow.get("HAS_VISA") == null ? false :
                    (personRow.get("HAS_VISA").toString()
                    .equalsIgnoreCase("y") ? true :false));
        personBean.setOfficeLocation( (String)
        personRow.get("OFFICE_LOCATION"));
        personBean.setOfficePhone( (String)
        personRow.get("OFFICE_PHONE"));
        personBean.setSecOfficeLocation( (String)
        personRow.get("SECONDRY_OFFICE_LOCATION"));
        personBean.setSecOfficePhone( (String)
        personRow.get("SECONDRY_OFFICE_PHONE"));
        personBean.setSchool( (String)
        personRow.get("SCHOOL"));
        personBean.setYearGraduated( (String)
        personRow.get("YEAR_GRADUATED"));
        personBean.setDirDept( (String)
        personRow.get("DIRECTORY_DEPARTMENT"));
        personBean.setSaltuation( (String)
        personRow.get("SALUTATION"));
        personBean.setCountryCitizenship( (String)
        personRow.get("COUNTRY_OF_CITIZENSHIP"));
        personBean.setPrimaryTitle( (String)
        personRow.get("PRIMARY_TITLE"));
        personBean.setDirTitle( (String)
        personRow.get("DIRECTORY_TITLE"));
        personBean.setHomeUnit( (String)
        personRow.get("HOME_UNIT"));
        String unitNumber = (String)personRow.get("HOME_UNIT");
        if(unitNumber != null){
            personBean.setUnitName(getUnitName(unitNumber));
        }
        personBean.setFaculty(
                personRow.get("IS_FACULTY") == null ? false :
                    (personRow.get("IS_FACULTY").toString()
                    .equalsIgnoreCase("y") ? true :false));
        personBean.setGraduateStudentStaff(
                personRow.get("IS_GRADUATE_STUDENT_STAFF") == null ? false :
                    (personRow.get("IS_GRADUATE_STUDENT_STAFF").toString()
                    .equalsIgnoreCase("y") ? true :false));
        personBean.setResearchStaff(
                personRow.get("IS_RESEARCH_STAFF") == null ? false :
                    (personRow.get("IS_RESEARCH_STAFF").toString()
                    .equalsIgnoreCase("y") ? true :false));
        personBean.setServiceStaff(
                personRow.get("IS_SERVICE_STAFF") == null ? false :
                    (personRow.get("IS_SERVICE_STAFF").toString()
                    .equalsIgnoreCase("y") ? true :false));
        personBean.setSupportStaff(
                personRow.get("IS_SUPPORT_STAFF") == null ? false :
                    (personRow.get("IS_SUPPORT_STAFF").toString()
                    .equalsIgnoreCase("y") ? true :false));
        personBean.setOtherAcademicGroup(
                personRow.get("IS_OTHER_ACCADEMIC_GROUP") == null ? false :
                    (personRow.get("IS_OTHER_ACCADEMIC_GROUP").toString()
                    .equalsIgnoreCase("y") ? true :false));
        personBean.setMedicalStaff(
                personRow.get("IS_MEDICAL_STAFF") == null ? false :
                    (personRow.get("IS_MEDICAL_STAFF").toString()
                    .equalsIgnoreCase("y") ? true :false));
        personBean.setVacationAccural(
                personRow.get("VACATION_ACCURAL") == null ? false :
                    (personRow.get("VACATION_ACCURAL").toString()
                    .equalsIgnoreCase("y") ? true :false));
        personBean.setOnSabbatical(
                personRow.get("IS_ON_SABBATICAL") == null ? false :
                    (personRow.get("IS_ON_SABBATICAL").toString()
                    .equalsIgnoreCase("y") ? true :false));
        personBean.setProvided( (String)
        personRow.get("ID_PROVIDED"));
        personBean.setVerified( (String)
        personRow.get("ID_VERIFIED"));
        personBean.setUpdateTimestamp( (Timestamp)
        personRow.get("UPDATE_TIMESTAMP"));
        personBean.setUpdateUser( (String)
        personRow.get("UPDATE_USER"));
        
        //Case #1602 Start
        personBean.setAddress1((String)
        personRow.get("ADDRESS_LINE_1"));
        personBean.setAddress2((String)
        personRow.get("ADDRESS_LINE_2"));
        personBean.setAddress3((String)
        personRow.get("ADDRESS_LINE_3"));
        personBean.setCity((String)
        personRow.get("CITY"));
        personBean.setCounty((String)
        personRow.get("COUNTY"));
        personBean.setState((String)
        personRow.get("STATE"));
        personBean.setPostalCode((String)
        personRow.get("POSTAL_CODE"));
        personBean.setCountryCode((String)
        personRow.get("COUNTRY_CODE"));
        personBean.setFaxNumber((String)
        personRow.get("FAX_NUMBER"));
        personBean.setPagerNumber((String)
        personRow.get("PAGER_NUMBER"));
        personBean.setMobilePhNumber((String)
        personRow.get("MOBILE_PHONE_NUMBER"));
        personBean.setEraCommonsUsrName((String)
        personRow.get("ERA_COMMONS_USER_NAME"));
        //Case #1602 End
        
        //Added for Coeus 4.3 PT ID - 2388:Person Enhancements - start
        //Added status column to denote the active status of a person
        personBean.setStatus((String)personRow.get("STATUS"));
        personBean.setAWPersonId((String)
        personRow.get("PERSON_ID"));
        //Added for Coeus 4.3 PT ID - 2388:Person Enhancements - end
        //Added for Case#2918 - Use of Salary Anniversary Date for calculating inflation in budget development module - starts
        personBean.setSalaryAnniversaryDate(
                                personRow.get("SALARY_ANNIVERSARY_DATE") == null ? null
                                :new Date( ((Timestamp) personRow.get(
                                "SALARY_ANNIVERSARY_DATE")).getTime()));        
        //Added for Case#2918 - Use of Salary Anniversary Date for calculating inflation in budget development module - ends
        //COEUSQA-1674 - Allow Division Lead Unit to be modified in the person details - Start
        personBean.setDivision( (String)
        personRow.get("DIVISION"));
        //COEUSQA-1674 - Allow Division Lead Unit to be modified in the person details - End
        return personBean;
    }
    
    /**
     * Gets all the person Details for person id and proposal number . The
     * return Vector(Collection) from OSP$EPS_PROP_PERSON  table.
     * <li>To fetch the data, it uses the procedure DW_GET_PROP_PERSON_FOR_ONE.
     *
     * @param String proposalNumber
     * @param String personId to get the person details for departmental screen
     * @return DepartmentPersonFormBean attributes of the person.
     * @exception DBException if any error during database transaction.
     * @exception CoeusException if the instance of dbEngine is not available.
     */
    public DepartmentPersonFormBean getPersonDetailsForPersonID(String proposalNumber,
            String personId )throws CoeusException, DBException {
        
        Vector resultPerson = new Vector(3, 2);
        Vector param = new Vector();
        param.addElement(new Parameter("PROPOSAL_NUMBER",
                DBEngineConstants.TYPE_STRING, proposalNumber ));
        param.addElement(new Parameter("PERSON_ID",
                DBEngineConstants.TYPE_STRING, personId ));
        
        if (dbEngine != null) {
            
            //Execute the  DB Procedure and Stores the result in Vector.
            resultPerson = dbEngine.executeRequest("Coeus",
                    "call DW_GET_PROP_PERSON_FOR_ONE ( <<PROPOSAL_NUMBER>>, "+
                    " <<PERSON_ID>>, <<OUT RESULTSET rset>>) " , "Coeus", param );
        } else {
            throw new CoeusException("db_exceptionCode.1000");
        }
        
        int personSize = resultPerson.size();
        
        if ( personSize < 1) {
            return null;
        }
        
//        Vector personResultSet = new Vector(3, 2);
        HashMap personRow = null;
        DepartmentPersonFormBean personBean = new DepartmentPersonFormBean();
        personRow = (HashMap) resultPerson.elementAt(0);
        
        personBean.setPersonId( (String)
        personRow.get("PROPOSAL_NUMBER"));
        personBean.setPersonId( (String)
        personRow.get("PERSON_ID"));
        personBean.setSsn( (String)
        personRow.get("SSN"));
        personBean.setLastName( (String)
        personRow.get("LAST_NAME"));
        personBean.setFirstName( (String)
        personRow.get("FIRST_NAME"));
        personBean.setMiddleName( (String)
        personRow.get("MIDDLE_NAME"));
        personBean.setFullName( (String)
        personRow.get("FULL_NAME"));
        personBean.setPriorName( (String)
        personRow.get("PRIOR_NAME"));
        personBean.setUserName( (String)
        personRow.get("USER_NAME"));
        personBean.setEmailAddress( (String)
        personRow.get("EMAIL_ADDRESS"));
        personBean.setDateOfBirth(
                personRow.get("DATE_OF_BIRTH") == null ?
                    null : new Date(((Timestamp) personRow.get(
                "DATE_OF_BIRTH")).getTime()));
        personBean.setAge(Integer.parseInt(personRow.get("AGE") == null ? "0" :
            personRow.get("AGE").toString()));
        personBean.setAgeByFiscalYear(
                Integer.parseInt(personRow.get("AGE_BY_FISCAL_YEAR") == null ? "0" :
                    personRow.get("AGE_BY_FISCAL_YEAR").toString()));
        personBean.setGender( (String)
        personRow.get("GENDER"));
        personBean.setRace( (String)
        personRow.get("RACE"));
        personBean.setEduLevel( (String)
        personRow.get("EDUCATION_LEVEL"));
        personBean.setDegree( (String)
        personRow.get("DEGREE"));
        personBean.setMajor( (String)
        personRow.get("MAJOR"));
        personBean.setHandicap(
                personRow.get("IS_HANDICAPPED") == null ? false :
                    (personRow.get("IS_HANDICAPPED").toString()
                    .equalsIgnoreCase("y") ? true :false));
        personBean.setHandiCapType( (String)
        personRow.get("HANDICAP_TYPE"));
        personBean.setVeteran(
                personRow.get("IS_VETERAN") == null ? false :
                    (personRow.get("IS_VETERAN").toString()
                    .equalsIgnoreCase("y") ? true :false));
        personBean.setVeteranType( (String)
        personRow.get("VETERAN_TYPE"));
        personBean.setVisaCode( (String)
        personRow.get("VISA_CODE"));
        personBean.setVisaType( (String)
        personRow.get("VISA_TYPE"));
        personBean.setVisaRenDate(
                personRow.get("VISA_RENEWAL_DATE") == null ?
                    null : new Date(((Timestamp) personRow.get(
                "VISA_RENEWAL_DATE")).getTime()));
        personBean.setHasVisa(
                personRow.get("HAS_VISA") == null ? false :
                    (personRow.get("HAS_VISA").toString()
                    .equalsIgnoreCase("y") ? true :false));
        personBean.setOfficeLocation( (String)
        personRow.get("OFFICE_LOCATION"));
        personBean.setOfficePhone( (String)
        personRow.get("OFFICE_PHONE"));
        personBean.setSecOfficeLocation( (String)
        personRow.get("SECONDRY_OFFICE_LOCATION"));
        personBean.setSecOfficePhone( (String)
        personRow.get("SECONDRY_OFFICE_PHONE"));
        personBean.setSchool( (String)
        personRow.get("SCHOOL"));
        personBean.setYearGraduated( (String)
        personRow.get("YEAR_GRADUATED"));
        personBean.setDirDept( (String)
        personRow.get("DIRECTORY_DEPARTMENT"));
        personBean.setSaltuation( (String)
        personRow.get("SALUTATION"));
        personBean.setCountryCitizenship( (String)
        personRow.get("COUNTRY_OF_CITIZENSHIP"));
        personBean.setPrimaryTitle( (String)
        personRow.get("PRIMARY_TITLE"));
        personBean.setDirTitle( (String)
        personRow.get("DIRECTORY_TITLE"));
        personBean.setHomeUnit( (String)
        personRow.get("HOME_UNIT"));
        personBean.setFaculty(
                personRow.get("IS_FACULTY") == null ? false :
                    (personRow.get("IS_FACULTY").toString()
                    .equalsIgnoreCase("y") ? true :false));
        personBean.setGraduateStudentStaff(
                personRow.get("IS_GRADUATE_STUDENT_STAFF") == null ? false :
                    (personRow.get("IS_GRADUATE_STUDENT_STAFF").toString()
                    .equalsIgnoreCase("y") ? true :false));
        personBean.setResearchStaff(
                personRow.get("IS_RESEARCH_STAFF") == null ? false :
                    (personRow.get("IS_RESEARCH_STAFF").toString()
                    .equalsIgnoreCase("y") ? true :false));
        personBean.setServiceStaff(
                personRow.get("IS_SERVICE_STAFF") == null ? false :
                    (personRow.get("IS_SERVICE_STAFF").toString()
                    .equalsIgnoreCase("y") ? true :false));
        personBean.setSupportStaff(
                personRow.get("IS_SUPPORT_STAFF") == null ? false :
                    (personRow.get("IS_SUPPORT_STAFF").toString()
                    .equalsIgnoreCase("y") ? true :false));
        personBean.setOtherAcademicGroup(
                personRow.get("IS_OTHER_ACCADEMIC_GROUP") == null ? false :
                    (personRow.get("IS_OTHER_ACCADEMIC_GROUP").toString()
                    .equalsIgnoreCase("y") ? true :false));
        personBean.setMedicalStaff(
                personRow.get("IS_MEDICAL_STAFF") == null ? false :
                    (personRow.get("IS_MEDICAL_STAFF").toString()
                    .equalsIgnoreCase("y") ? true :false));
        personBean.setVacationAccural(
                personRow.get("VACATION_ACCURAL") == null ? false :
                    (personRow.get("VACATION_ACCURAL").toString()
                    .equalsIgnoreCase("y") ? true :false));
        personBean.setOnSabbatical(
                personRow.get("IS_ON_SABBATICAL") == null ? false :
                    (personRow.get("IS_ON_SABBATICAL").toString()
                    .equalsIgnoreCase("y") ? true :false));
        personBean.setProvided( (String)
        personRow.get("ID_PROVIDED"));
        personBean.setVerified( (String)
        personRow.get("ID_VERIFIED"));
        personBean.setUpdateTimestamp( (Timestamp)
        personRow.get("UPDATE_TIMESTAMP"));
        personBean.setUpdateUser( (String)
        personRow.get("UPDATE_USER"));
        //COEUSQA-1674 - Allow Division Lead Unit to be modified in the person details - Start
        personBean.setDivision( (String)
        personRow.get("DIVISION"));
        //COEUSQA-1674 - Allow Division Lead Unit to be modified in the person details - End
        
        return personBean;
    }
    
    /**
     * Gets all the department biography Details for person id . The
     * return Vector(Collection) from OSP$PERSON_BIO table.
     * <li>To fetch the data, it uses the procedure DW_GET_PER_BIO_FOR_PERSON.
     *
     * @param String personId to get the biography details for departmental screen
     * @return DepartmentBioPersonFormBean attributes of the person.
     * @exception DBException if any error during database transaction.
     * @exception CoeusException if the instance of dbEngine is not available.
     */
    public Vector getBioPerson(String personId)
    throws CoeusException, DBException{
        Vector result = null;
        Vector param= new Vector();
        DepartmentBioPersonFormBean departmentBioPersonFormBean = null;
        HashMap bioPersonRow = null;
        param.addElement(new Parameter("PERSON_ID",
                DBEngineConstants.TYPE_STRING,personId));
        if(dbEngine !=null){
            result = new Vector(3,2);
            result = dbEngine.executeRequest("Coeus",
                    "call GET_PER_BIO_FOR_PERSON( <<PERSON_ID>>, <<OUT RESULTSET rset>> )",
                    "Coeus", param);
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }
        int listSize = result.size();
        Vector bioPersonList = null;
        if (listSize > 0){
            bioPersonList = new Vector(3,2);
            for(int rowIndex=0; rowIndex<listSize; rowIndex++){
                departmentBioPersonFormBean = new DepartmentBioPersonFormBean();
                bioPersonRow = (HashMap)result.elementAt(rowIndex);
                departmentBioPersonFormBean.setPersonId( (String)
                bioPersonRow.get("PERSON_ID"));
                departmentBioPersonFormBean.setBioNumber(Integer.parseInt
                        (bioPersonRow.get("BIO_NUMBER").toString()));
                departmentBioPersonFormBean.setDescription( (String)
                bioPersonRow.get("DESCRIPTION"));
                departmentBioPersonFormBean.setUpdateTimestamp( (Timestamp)
                bioPersonRow.get("UPDATE_TIMESTAMP"));
                departmentBioPersonFormBean.setUpdateUser( (String)
                bioPersonRow.get("UPDATE_USER"));
                //Case 2793:NOW Person Maintainer - Uploading documents
                departmentBioPersonFormBean.setDocumentTypeCode(
                                Integer.parseInt(bioPersonRow.get(
                                    "DOCUMENT_TYPE_CODE") == null ? "0" :
                                bioPersonRow.get("DOCUMENT_TYPE_CODE").toString()));
                departmentBioPersonFormBean.setDocumentTypeDescription((String)
                                bioPersonRow.get("DOCUMENT_TYPE_DESC"));
                //2793 End
                departmentBioPersonFormBean.setHasBioPDF( bioPersonRow.get("PDF_BIO_NUMBER") == null ? false : true);
                departmentBioPersonFormBean.setHasBioSource( bioPersonRow.get("SOURCE_BIO_NUMBER") == null ? false : true);
                //Added for case 3685 - Show timestamp in Person biography details - start
                DepartmentBioPDFPersonFormBean deptBioPDFPersonFormBean = 
                        new DepartmentBioPDFPersonFormBean();
                deptBioPDFPersonFormBean.setFileName((String)
                    bioPersonRow.get("PDF_FILE_NAME"));
                deptBioPDFPersonFormBean.setMimeType((String)
                    bioPersonRow.get("PDF_MIME_TYPE"));//case 4007
                deptBioPDFPersonFormBean.setUpdateUser((String)
                    bioPersonRow.get("PDF_UPDATE_USER"));
                deptBioPDFPersonFormBean.setUpdateTimestamp(
                    (Timestamp)bioPersonRow.get("PDF_UPDATE_TIMESTAMP"));
                departmentBioPersonFormBean.setDepartmentBioPDFPersonFormBean(deptBioPDFPersonFormBean);
                //Added for case 3685 - Show timestamp in Person biography details - start
                bioPersonList.add(departmentBioPersonFormBean);
            }
        }
        return bioPersonList;
    }
    
    /**
     * Gets all the department biography PDF Details for person id . The
     * return Vector(Collection) from OSP$PERSON_BIO_PDF table.
     * <li>To fetch the data, it uses the procedure DW_GET_PER_BIO_PDF_FOR_PER.
     *
     * @param String personId to get biography PDF details for departmental screen
     * @return DepartmentBioPDFPersonFormBean attributes of the person.
     * @exception DBException if any error during database transaction.
     * @exception CoeusException if the instance of dbEngine is not available.
     */
    public Vector getBioPDFPerson(String personId)
    throws CoeusException, DBException{
        Vector result = null;
        Vector param= new Vector();
        DepartmentBioPDFPersonFormBean departmentBioPDFPersonFormBean = null;
        HashMap bioPDFPersonRow = null;
        param.addElement(new Parameter("PERSON_ID",
                DBEngineConstants.TYPE_STRING,personId));
        if(dbEngine !=null){
            result = new Vector(3,2);
            result = dbEngine.executeRequest("Coeus",
                    "call DW_GET_PER_BIO_PDF_FOR_PER( <<PERSON_ID>>, <<OUT RESULTSET rset>> )",
                    "Coeus", param);
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }
        int listSize = result.size();
        Vector bioPDFPersonList = null;
        if (listSize > 0){
            bioPDFPersonList = new Vector(3,2);
            for(int rowIndex=0; rowIndex<listSize; rowIndex++){
                departmentBioPDFPersonFormBean = new DepartmentBioPDFPersonFormBean();
                bioPDFPersonRow = (HashMap)result.elementAt(rowIndex);
                departmentBioPDFPersonFormBean.setPersonId( (String)
                bioPDFPersonRow.get("PERSON_ID"));
                departmentBioPDFPersonFormBean.setBioNumber(Integer.parseInt
                        (bioPDFPersonRow.get("BIO_NUMBER").toString()));
                departmentBioPDFPersonFormBean.setFileName( (String)
                bioPDFPersonRow.get("FILE_NAME"));
                departmentBioPDFPersonFormBean.setUpdateTimestamp( (Timestamp)
                bioPDFPersonRow.get("UPDATE_TIMESTAMP"));
                departmentBioPDFPersonFormBean.setUpdateUser( (String)
                bioPDFPersonRow.get("UPDATE_USER"));
                bioPDFPersonList.add(departmentBioPDFPersonFormBean);
            }
        }
        return bioPDFPersonList;
    }
    
    /**
     * Gets all the department biography Source Details for person id . The
     * return Vector(Collection) from OSP$PERSON_BIO_SOURCE table.
     * <li>To fetch the data, it uses the procedure DW_GET_PER_BIO_SRCE_FOR_PER.
     *
     * @param String personId to get biography Source details for departmental screen
     * @return DepartmentBioSourceFormBean attributes of the person.
     * @exception DBException if any error during database transaction.
     * @exception CoeusException if the instance of dbEngine is not available.
     */
    public Vector getBioSourcePerson(String personId)
    throws CoeusException, DBException{
        Vector result = null;
        Vector param= new Vector();
        DepartmentBioSourceFormBean departmentBioSourceFormBean = null;
        HashMap bioSourcePersonRow = null;
        param.addElement(new Parameter("PERSON_ID",
                DBEngineConstants.TYPE_STRING,personId));
        if(dbEngine !=null){
            result = new Vector(3,2);
            result = dbEngine.executeRequest("Coeus",
                    "call DW_GET_PER_BIO_SRCE_FOR_PER( <<PERSON_ID>>, <<OUT RESULTSET rset>> )",
                    "Coeus", param);
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }
        int listSize = result.size();
        Vector bioSourcePersonList = null;
        if (listSize > 0){
            bioSourcePersonList = new Vector(3,2);
            for(int rowIndex=0; rowIndex<listSize; rowIndex++){
                departmentBioSourceFormBean = new DepartmentBioSourceFormBean();
                bioSourcePersonRow = (HashMap)result.elementAt(rowIndex);
                departmentBioSourceFormBean.setPersonId( (String)
                bioSourcePersonRow.get("PERSON_ID"));
                departmentBioSourceFormBean.setBioNumber(Integer.parseInt
                        (bioSourcePersonRow.get("BIO_NUMBER").toString()));
                departmentBioSourceFormBean.setFileName( (String)
                bioSourcePersonRow.get("FILE_NAME"));
                departmentBioSourceFormBean.setInputType(
                        bioSourcePersonRow.get("INPUT_TYPE").toString().charAt(0));
                departmentBioSourceFormBean.setPlatformType(
                        bioSourcePersonRow.get("PLATFORM_TYPE").toString().charAt(0));
                departmentBioSourceFormBean.setUpdateTimestamp( (Timestamp)
                bioSourcePersonRow.get("UPDATE_TIMESTAMP"));
                departmentBioSourceFormBean.setUpdateUser( (String)
                bioSourcePersonRow.get("UPDATE_USER"));
                bioSourcePersonList.add(departmentBioSourceFormBean);
            }
        }
        return bioSourcePersonList;
    }
    
    /**
     * Gets all the department degree Details for person id . The
     * return Vector(Collection) from OSP$PERSON_DEGREE table.
     * <li>To fetch the data, it uses the procedure DW_GET_PERSON_DEGREE.
     *
     * @param String personId to get degree details for departmental screen
     * @return DepartmentPerDegreeFormBean attributes of the person.
     * @exception DBException if any error during database transaction.
     * @exception CoeusException if the instance of dbEngine is not available.
     */
    public Vector getDepartmentPersonDegree(String personId)
    throws CoeusException, DBException{
        Vector result = null;
        Vector param= new Vector();
        DepartmentPerDegreeFormBean departmentPerDegreeFormBean = null;
        HashMap degreePersonRow = null;
        param.addElement(new Parameter("PERSON_ID",
                DBEngineConstants.TYPE_STRING,personId));
        if(dbEngine !=null){
            result = new Vector(3,2);
            result = dbEngine.executeRequest("Coeus",
                    "call GET_PERSON_DEGREE( <<PERSON_ID>>, <<OUT RESULTSET rset>> )",
                    "Coeus", param);
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }
        int listSize = result.size();
        Vector degreePersonList = null;
        if (listSize > 0){
            degreePersonList = new Vector(3,2);
            for(int rowIndex=0; rowIndex<listSize; rowIndex++){
                departmentPerDegreeFormBean = new DepartmentPerDegreeFormBean();
                degreePersonRow = (HashMap)result.elementAt(rowIndex);
                departmentPerDegreeFormBean.setPersonId( (String)
                degreePersonRow.get("PERSON_ID"));
                departmentPerDegreeFormBean.setDegreeCode((String)
                degreePersonRow.get("DEGREE_CODE"));
                departmentPerDegreeFormBean.setAwDegreeCode((String)
                degreePersonRow.get("DEGREE_CODE"));
                departmentPerDegreeFormBean.setDegreeDescription((String)
                degreePersonRow.get("DEGREEDESC"));
                departmentPerDegreeFormBean.setGraduationDate(
                        new Date(((Timestamp) degreePersonRow.get(
                        "GRADUATION_DATE")).getTime()));
                departmentPerDegreeFormBean.setDegree( (String)
                degreePersonRow.get("DEGREE"));
                departmentPerDegreeFormBean.setAwDegree( (String)
                degreePersonRow.get("DEGREE"));
                departmentPerDegreeFormBean.setFieldOfStudy( (String)
                degreePersonRow.get("FIELD_OF_STUDY"));
                departmentPerDegreeFormBean.setSpecialization( (String)
                degreePersonRow.get("SPECIALIZATION"));
                departmentPerDegreeFormBean.setSchool( (String)
                degreePersonRow.get("SCHOOL"));
                departmentPerDegreeFormBean.setSchoolIdCode( (String)
                degreePersonRow.get("SCHOOL_ID_CODE"));
                departmentPerDegreeFormBean.setSchoolDescription( (String)
                degreePersonRow.get("SCHOOLDESC"));
                departmentPerDegreeFormBean.setSchoolId( (String)
                degreePersonRow.get("SCHOOL_ID"));
                departmentPerDegreeFormBean.setUpdateTimestamp( (Timestamp)
                degreePersonRow.get("UPDATE_TIMESTAMP"));
                departmentPerDegreeFormBean.setUpdateUser( (String)
                degreePersonRow.get("UPDATE_USER"));
                degreePersonList.add(departmentPerDegreeFormBean);
            }
        }
        return degreePersonList;
    }
    
    /**
     *  The method used to fetch the Person Bio PDF's from DB.
     *  Template will be stored in the database and written to the Server hard drive.
     *
     *  @param departmentBioPDFPersonFormBean DepartmentBioPDFPersonFormBean
     *  @return DepartmentBioPDFPersonFormBean
     *
     *  @exception DBException if any error during database transaction.
     *  @exception CoeusException if the instance of dbEngine is not available.
     */
    public DepartmentBioPDFPersonFormBean getPersonBioPDF(DepartmentBioPDFPersonFormBean departmentBioPDFPersonFormBean)
    throws CoeusException, DBException{
        Vector result = new Vector(3, 2);
        Vector parameter = new Vector();
        String selectQuery = "";
        byte[] fileData = null;
        
        parameter.addElement(new Parameter("PERSON_ID",
                DBEngineConstants.TYPE_STRING, departmentBioPDFPersonFormBean.getPersonId()));
        parameter.addElement(new Parameter("BIO_NUMBER",
                DBEngineConstants.TYPE_INT, ""+departmentBioPDFPersonFormBean.getBioNumber()));
        
        selectQuery = "SELECT BIO_PDF, FILE_NAME, UPDATE_USER, UPDATE_TIMESTAMP FROM OSP$PERSON_BIO_PDF "+
                "WHERE PERSON_ID =  <<PERSON_ID>> AND  BIO_NUMBER =  <<BIO_NUMBER>>";
        
        HashMap resultRow = null;
        if(dbEngine!=null){
            result = dbEngine.executeRequest("Coeus", selectQuery, "Coeus",parameter);
            if( !result.isEmpty() ){
                resultRow = (HashMap)result.get(0);
                //Modified for case 3685 - Remove Word icons - start
                //departmentBioPDFPersonFormBean.setFileBytes((byte[])resultRow.get("BIO_PDF"));
                departmentBioPDFPersonFormBean.setFileBytes(convert((ByteArrayOutputStream)resultRow.get("BIO_PDF")));
                //Modified for case 3685 - Remove Word icons - end
                departmentBioPDFPersonFormBean.setFileName((String)resultRow.get("FILE_NAME"));
                departmentBioPDFPersonFormBean.setUpdateUser((String)resultRow.get("UPDATE_USER"));
                departmentBioPDFPersonFormBean.setUpdateTimestamp((Timestamp)resultRow.get("UPDATE_TIMESTAMP"));
                return departmentBioPDFPersonFormBean;
            }
        }else {
            throw new CoeusException("db_exceptionCode.1000");
        }
        return null;
    }
    
    /**
     *  The method used to fetch the Person Bio PDF's from DB.
     *  Template will be stored in the database and written to the Server hard drive.
     *
     *  @param departmentBioPDFPersonFormBean DepartmentBioPDFPersonFormBean
     *  @return DepartmentBioPDFPersonFormBean
     *
     *  @exception DBException if any error during database transaction.
     *  @exception CoeusException if the instance of dbEngine is not available.
     */
    public DepartmentBioSourceFormBean getPersonBioSource(DepartmentBioSourceFormBean departmentBioSourceFormBean)
    throws CoeusException, DBException{
        Vector result = new Vector(3, 2);
        Vector parameter = new Vector();
        String selectQuery = "";
        byte[] fileData = null;
        
        parameter.addElement(new Parameter("PERSON_ID",
                DBEngineConstants.TYPE_STRING, departmentBioSourceFormBean.getPersonId()));
        parameter.addElement(new Parameter("BIO_NUMBER",
                DBEngineConstants.TYPE_INT, ""+departmentBioSourceFormBean.getBioNumber()));
        
        selectQuery = "SELECT BIO_SOURCE, FILE_NAME, SOURCE_EDITOR, INPUT_TYPE, PLATFORM_TYPE, UPDATE_USER, UPDATE_TIMESTAMP FROM OSP$PERSON_BIO_SOURCE "+
                "WHERE PERSON_ID =  <<PERSON_ID>> AND  BIO_NUMBER =  <<BIO_NUMBER>>";
        
        HashMap resultRow = null;
        if(dbEngine!=null){
            result = dbEngine.executeRequest("Coeus", selectQuery, "Coeus",parameter);
            if( !result.isEmpty() ){
                resultRow = (HashMap)result.get(0);
                //Modified for case 3685 - Remove Word icons - start
                //departmentBioSourceFormBean.setFileBytes((byte[])resultRow.get("BIO_SOURCE"));
                departmentBioSourceFormBean.setFileBytes(convert((ByteArrayOutputStream)resultRow.get("BIO_SOURCE")));
                //Modified for case 3685 - Remove Word icons - end
                departmentBioSourceFormBean.setFileName((String)resultRow.get("FILE_NAME"));
                departmentBioSourceFormBean.setSourceEditor((String)resultRow.get("SOURCE_EDITOR"));
                departmentBioSourceFormBean.setInputType(resultRow.get("INPUT_TYPE").toString().charAt(0));
                departmentBioSourceFormBean.setPlatformType(resultRow.get("PLATFORM_TYPE").toString().charAt(0));
                departmentBioSourceFormBean.setUpdateUser((String)resultRow.get("UPDATE_USER"));
                departmentBioSourceFormBean.setUpdateTimestamp((Timestamp)resultRow.get("UPDATE_TIMESTAMP"));
                return departmentBioSourceFormBean;
            }
        }else {
            throw new CoeusException("db_exceptionCode.1000");
        }
        return null;
    }
    
    /**
     *  This method used to Training information
     *  <li>To fetch the data, it uses the procedure DW_GET_TRAINING.
     *
     *  @return CoeusVector of all ComboBox beans
     *  @exception DBException if any error during database transaction.
     *  @exception CoeusException if the instance of dbEngine is not available.
     */
    public CoeusVector getTraining() throws CoeusException, DBException{
        Vector result = null;
        Vector param= new Vector();
        HashMap costElementRow = null;
        if(dbEngine !=null){
            result = new Vector(3,2);
            result = dbEngine.executeRequest("Coeus",
                    "call DW_GET_TRAINING( <<OUT RESULTSET rset>> )", "Coeus", param);
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }
        int listSize = result.size();
        CoeusVector trainingList = null;
        if (listSize > 0){
            trainingList = new CoeusVector();
            for(int rowIndex=0; rowIndex<listSize; rowIndex++){
                costElementRow = (HashMap)result.elementAt(rowIndex);
                trainingList.addElement(new ComboBoxBean(
                        costElementRow.get("TRAINING_CODE").toString(),
                        costElementRow.get("DESCRIPTION").toString()));
            }
        }
        return trainingList;
    }
    
    /**
     * Gets all the department person training Details for the given person id . The
     * return CoeusVector(Collection) from OSP$PERSON_TRAINING table.
     * <li>To fetch the data, it uses the procedure DW_GET_PERSON_TRAINING.
     *
     * @param String personId to get training details for departmental screen
     * @return DepartmentPersonTrainingBean attributes of the person.
     * @exception DBException if any error during database transaction.
     * @exception CoeusException if the instance of dbEngine is not available.
     */
    public CoeusVector getDepartmentPersonTraining(String personId)
    throws CoeusException, DBException{
        Vector result = null;
        Vector param= new Vector();
        DepartmentPersonTrainingBean departmentPersonTrainingBean = null;
        HashMap degreePersonRow = null;
        param.addElement(new Parameter("PERSON_ID",
                DBEngineConstants.TYPE_STRING,personId));
        if(dbEngine !=null){
            result = new Vector(3,2);
            result = dbEngine.executeRequest("Coeus",
                    "call GET_PERSON_TRAINING( <<PERSON_ID>>, <<OUT RESULTSET rset>> )",
                    "Coeus", param);
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }
        int listSize = result.size();
        CoeusVector degreePersonList = null;
        if (listSize > 0){
            degreePersonList = new CoeusVector();
            for(int rowIndex=0; rowIndex<listSize; rowIndex++){
                departmentPersonTrainingBean = new DepartmentPersonTrainingBean();
                degreePersonRow = (HashMap)result.elementAt(rowIndex);
                departmentPersonTrainingBean.setPersonId( (String)
                degreePersonRow.get("PERSON_ID"));
                departmentPersonTrainingBean.setTrainingNumber(
                        degreePersonRow.get("TRAINING_NUMBER") == null ? 0 : Integer.parseInt(degreePersonRow.get("TRAINING_NUMBER").toString()));
                departmentPersonTrainingBean.setTrainingCode(
                        degreePersonRow.get("TRAINING_CODE") == null ? 0 : Integer.parseInt(degreePersonRow.get("TRAINING_CODE").toString()));
                departmentPersonTrainingBean.setDateRequested(
                        degreePersonRow.get("DATE_REQUESTED") == null ?
                            null : new Date(((Timestamp) degreePersonRow.get(
                        "DATE_REQUESTED")).getTime()));
                departmentPersonTrainingBean.setDateSubmitted(
                        degreePersonRow.get("DATE_SUBMITTED") == null ?
                            null : new Date(((Timestamp) degreePersonRow.get(
                        "DATE_SUBMITTED")).getTime()));
                departmentPersonTrainingBean.setDateAcknowledged(
                        degreePersonRow.get("DATE_ACKNOWLEDGED") == null ?
                            null : new Date(((Timestamp) degreePersonRow.get(
                        "DATE_ACKNOWLEDGED")).getTime()));
                departmentPersonTrainingBean.setFollowUpDate(
                        degreePersonRow.get("FOLLOWUP_DATE") == null ?
                            null : new Date(((Timestamp) degreePersonRow.get(
                        "FOLLOWUP_DATE")).getTime()));
                departmentPersonTrainingBean.setScore((String)
                degreePersonRow.get("SCORE"));
                departmentPersonTrainingBean.setComments((String)
                degreePersonRow.get("COMMENTS"));
                departmentPersonTrainingBean.setUpdateTimestamp( (Timestamp)
                degreePersonRow.get("UPDATE_TIMESTAMP"));
                departmentPersonTrainingBean.setUpdateUser( (String)
                degreePersonRow.get("UPDATE_USER"));
                departmentPersonTrainingBean.setAw_TrainingNumber(departmentPersonTrainingBean.getTrainingNumber());
                //COEUSQA:3537 - Coeus IACUC Training Revisions - Start
                departmentPersonTrainingBean.setSpeciesType((String)degreePersonRow.get("SPECIES"));
                departmentPersonTrainingBean.setProcedureType((String)degreePersonRow.get("PROCEDURE"));
                //COEUSQA:3537 - End
                degreePersonList.add(departmentPersonTrainingBean);
            }
        }
        return degreePersonList;
    }
    
    /**
     *  This method used get Max Person Training number for the person id
     *  <li>To fetch the data, it uses the function FN_GET_MAX_PER_TRAINING_NUMBER.
     *
     *  @return int bio graphy number for the person id
     *  @param String person id to get biography number
     *  @exception DBException if any error during database transaction.
     *  @exception CoeusException if the instance of dbEngine is not available.
     */
    public int getMaxPersonTrainingNumber(String personId)
    throws CoeusException, DBException {
        int number = 0;
        
        Vector param= new Vector();
        Vector result = new Vector();
        param.add(new Parameter("PERSON_ID",
                DBEngineConstants.TYPE_STRING,personId));
        /* calling stored function */
        if(dbEngine!=null){
            result = dbEngine.executeFunctions("Coeus",
                    "{ <<OUT INTEGER NUMBER>> = "
                    +" call FN_GET_MAX_PER_TRAINING_NUMBER(<< PERSON_ID >> ) }", param);
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }
        if(!result.isEmpty()){
            HashMap rowParameter = (HashMap)result.elementAt(0);
            number = Integer.parseInt(rowParameter.get("NUMBER").toString());
        }
        return number;
    }
    
    /**
     *  This method used check whether the person id already exist in DB
     *  <li>To fetch the data, it uses the function FN_PERSON_EXISTS.
     *
     *  @return int bio graphy number for the person id
     *  @param String person id to get biography number
     *  @exception DBException if any error during database transaction.
     *  @exception CoeusException if the instance of dbEngine is not available.
     */
    public boolean isPersonExists(String personId)
    throws CoeusException, DBException {
        int number = 0;
        
        Vector param= new Vector();
        Vector result = new Vector();
        param.add(new Parameter("PERSON_ID",
                DBEngineConstants.TYPE_STRING,personId));
        /* calling stored function */
        if(dbEngine!=null){
            result = dbEngine.executeFunctions("Coeus",
                    "{ <<OUT INTEGER IS_EXIST>> = "
                    +" call FN_PERSON_EXISTS(<< PERSON_ID >> ) }", param);
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }
        if(!result.isEmpty()){
            HashMap rowParameter = (HashMap)result.elementAt(0);
            number = Integer.parseInt(rowParameter.get("IS_EXIST").toString());
        }
        if(number > 0){
            return true;
        }else{
            return false;
        }
    }
    
    /**
     *  This method used get Person Full name for the given Person Id
     *  <li>To fetch the data, it uses the function FN_GET_PERSON_NAME.
     *
     *  @return String Person Full Name
     *  @param String person id to get biography number
     *  @exception DBException if any error during database transaction.
     *  @exception CoeusException if the instance of dbEngine is not available.
     */
    public String getPersonName(String personId)
    throws CoeusException, DBException {
        String fullName = "";
        
        Vector param= new Vector();
        Vector result = new Vector();
        param.add(new Parameter("PERSON_ID",
                DBEngineConstants.TYPE_STRING,personId));
        /* calling stored function */
        if(dbEngine!=null){
            result = dbEngine.executeFunctions("Coeus",
                    "{ <<OUT STRING FULL_NAME>> = "
                    +" call FN_GET_PERSON_NAME(<< PERSON_ID >> ) }", param);
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }
        if(!result.isEmpty()){
            HashMap rowParameter = (HashMap)result.elementAt(0);
            fullName = (String)rowParameter.get("FULL_NAME");
        }
        return fullName;
    }
    
    /* Code added by Shivakumar for getting Current & Pending report 02/11/2004
     */
    //COEUSQA-1686 - Add additional fields to the Current  Pending Support Schema - Start
    //public Hashtable getCurrentAndPendingReport(String personId)
    public Hashtable getCurrentAndPendingReport(String personId , boolean investigatorAndKeyPerson)
    //COEUSQA-1686 - Add additional fields to the Current  Pending Support Schema - End
    throws CoeusException, DBException {
        Vector param= new Vector();
        Vector result = new Vector();
        //COEUSQA-1686 - Add additional fields to the Current  Pending Support Schema - Start
        Vector currentKeyPersonResult = new Vector();
        Vector pendingKeyPersonResult = new Vector();
        //COEUSQA-1686 - Add additional fields to the Current  Pending Support Schema - End
        CoeusVector vecCurrentReportData = new CoeusVector();
        CoeusVector vecPendingReportData = new CoeusVector();
        Hashtable htReportData = new Hashtable();
        HashMap hmReportData = null;
                
        param.add(new Parameter("PERSON_ID",
                DBEngineConstants.TYPE_STRING,personId));
        
        DepartmentPendingReportBean departmentPendingReportBean = null;
        
        DepartmentCurrentReportBean departmentCurrentReportBean = null;
        
        //COEUSQA-1686 - Add additional fields to the Current  Pending Support Schema - Start
        AwardTxnBean awardTxnBean = new AwardTxnBean();
        
        InstituteProposalTxnBean instituteProposalTxnBean = new InstituteProposalTxnBean();
        //COEUSQA-1686 - Add additional fields to the Current  Pending Support Schema - End
        
        // Getting the person's Full name
        
        //String personFullName = getPersonName(personId);
        
        if(dbEngine !=null){
            result = new Vector(3,2);
            //Procedure name renamed with case 3505:Add % effort to current and Pending Report
//            result = dbEngine.executeRequest("Coeus",
//                    "call DW_GET_CURRENT_SUPPORT( <<PERSON_ID>>, <<OUT RESULTSET rset>> )",
//                    "Coeus", param);
            result = dbEngine.executeRequest("Coeus",
                    "call GET_CURRENT_SUPPORT( <<PERSON_ID>>, <<OUT RESULTSET rset>> )",
                    "Coeus", param);
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }
        int vecSize = result.size();
        if (vecSize > 0){
            for(int rowIndex=0; rowIndex<vecSize; rowIndex++){
                departmentCurrentReportBean = new DepartmentCurrentReportBean();
                hmReportData = (HashMap)result.elementAt(rowIndex);
                departmentCurrentReportBean.setSponsorName( (String)
                hmReportData.get("SPONSOR_NAME"));
                departmentCurrentReportBean.setTitle((String)
                hmReportData.get("TITLE"));
                departmentCurrentReportBean.setAwardEffectiveDate(
                        hmReportData.get("AWARD_EFFECTIVE_DATE") == null ?
                            null : new Date(((Timestamp)hmReportData.get("AWARD_EFFECTIVE_DATE")).getTime()));
                departmentCurrentReportBean.setFinalExpirationDate(
                        hmReportData.get("FINAL_EXPIRATION_DATE") == null ?
                            null : new Date(((Timestamp)hmReportData.get("FINAL_EXPIRATION_DATE")).getTime()));
                departmentCurrentReportBean.setPrincipleInvestigatorFlag(
                        hmReportData.get("PRINCIPAL_INVESTIGATOR_FLAG") == null ? false :
                            hmReportData.get("PRINCIPAL_INVESTIGATOR_FLAG").toString()
                            .equalsIgnoreCase("y") ? true : false);
                departmentCurrentReportBean.setSponsorAwardNumber((String)
                hmReportData.get("SPONSOR_AWARD_NUMBER"));
                
                //COEUSQA-1686 - Add additional fields to the Current  Pending Support Schema - Start
                departmentCurrentReportBean.setObliDirectAmount(
                        hmReportData.get("OBLIGATED_TOTAL_DIRECT") == null ? 0 : Double.parseDouble(hmReportData.get("OBLIGATED_TOTAL_DIRECT").toString()));
                departmentCurrentReportBean.setObliInDirectAmount(
                        hmReportData.get("OBLIGATED_TOTAL_INDIRECT") == null ? 0 : Double.parseDouble(hmReportData.get("OBLIGATED_TOTAL_INDIRECT").toString()));
                
                departmentCurrentReportBean.setMITAwardNumber((String)
                hmReportData.get("MIT_AWARD_NUMBER"));
                
                departmentCurrentReportBean.setGroupCode(getParameterValues("CURRENT_PENDING_REPORT_GROUP_NAME"));
                //COEUSQA-1686 - Add additional fields to the Current  Pending Support Schema - End
                departmentCurrentReportBean.setObliDistrubutableAmount(
                        hmReportData.get("OBLI_DISTRIBUTABLE_AMOUNT") == null ? 0 : Double.parseDouble(hmReportData.get("OBLI_DISTRIBUTABLE_AMOUNT").toString()));
                //  departmentCurrentReportBean.setPersonFullName(personFullName);
                
                departmentCurrentReportBean.setPrinicipleInvestigator(
                        hmReportData.get("PRINCIPAL_INVESTIGATOR_FLAG") == null ? "Co-I" :
                            hmReportData.get("PRINCIPAL_INVESTIGATOR_FLAG").toString()
                            .equalsIgnoreCase("y") ? "PI" : "Co-I");
                //Added for case 3505:Add % effort to current and Pending Report - Start
                departmentCurrentReportBean.setPercentageEffort(
                        Float.parseFloat(hmReportData.get( "PERCENTAGE_EFFORT") == null ? "0" : hmReportData.get( "PERCENTAGE_EFFORT").toString()));
                departmentCurrentReportBean.setAcademicYearEffort(
                        Float.parseFloat(hmReportData.get( "ACADEMIC_YEAR_EFFORT") == null ? "0" : hmReportData.get( "ACADEMIC_YEAR_EFFORT").toString()));
                departmentCurrentReportBean.setSummerYearEffort(
                        Float.parseFloat(hmReportData.get( "SUMMER_YEAR_EFFORT") == null ? "0" : hmReportData.get( "SUMMER_YEAR_EFFORT").toString()));
                departmentCurrentReportBean.setCalendarYearEffort(
                        Float.parseFloat(hmReportData.get( "CALENDAR_YEAR_EFFORT") == null ? "0" : hmReportData.get( "CALENDAR_YEAR_EFFORT").toString()));
                //Added for case 3505:Add % effort to current and Pending Report - End
                //COEUSQA-1686 - Add additional fields to the Current  Pending Support Schema - Start
                departmentCurrentReportBean.setCustomElements(awardTxnBean.getAwardCustomData(departmentCurrentReportBean.getMITAwardNumber()));
                //COEUSQA-1686 - Add additional fields to the Current  Pending Support Schema - End
                vecCurrentReportData.addElement(departmentCurrentReportBean);
            }
            
        }
        
        //COEUSQA-1686 - Add additional fields to the Current  Pending Support Schema - Start
        if(dbEngine !=null){
            if(investigatorAndKeyPerson){
                currentKeyPersonResult = new Vector(3,2);
                currentKeyPersonResult = dbEngine.executeRequest("Coeus",
                        "call GET_KEY_PERSON_CURRENT_SUPPORT( <<PERSON_ID>>, <<OUT RESULTSET rset>> )",
                        "Coeus", param);
            }
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }
        int keyPersonSize = currentKeyPersonResult.size();
        if (keyPersonSize > 0){
            for(int rowIndex=0; rowIndex<keyPersonSize; rowIndex++){
                departmentCurrentReportBean = new DepartmentCurrentReportBean();
                hmReportData = (HashMap)currentKeyPersonResult.elementAt(rowIndex);
                departmentCurrentReportBean.setSponsorName( (String)
                hmReportData.get("SPONSOR_NAME"));
                departmentCurrentReportBean.setTitle((String)
                hmReportData.get("TITLE"));
                departmentCurrentReportBean.setAwardEffectiveDate(
                        hmReportData.get("AWARD_EFFECTIVE_DATE") == null ?
                            null : new Date(((Timestamp)hmReportData.get("AWARD_EFFECTIVE_DATE")).getTime()));
                departmentCurrentReportBean.setFinalExpirationDate(
                        hmReportData.get("FINAL_EXPIRATION_DATE") == null ?
                            null : new Date(((Timestamp)hmReportData.get("FINAL_EXPIRATION_DATE")).getTime()));                
                departmentCurrentReportBean.setSponsorAwardNumber((String)
                hmReportData.get("SPONSOR_AWARD_NUMBER"));
                departmentCurrentReportBean.setMITAwardNumber((String)
                hmReportData.get("MIT_AWARD_NUMBER"));
                departmentCurrentReportBean.setPrinicipleInvestigator(
                        hmReportData.get("PRINCIPAL_INVESTIGATOR_FLAG") == null ? "Key-Per" :
                            hmReportData.get("PRINCIPAL_INVESTIGATOR_FLAG").toString()
                            .equalsIgnoreCase("y") ? "PI" : "Key-Per");
                departmentCurrentReportBean.setGroupCode(getParameterValues("CURRENT_PENDING_REPORT_GROUP_NAME"));
                departmentCurrentReportBean.setObliDirectAmount(
                        hmReportData.get("OBLIGATED_TOTAL_DIRECT") == null ? 0 : Double.parseDouble(hmReportData.get("OBLIGATED_TOTAL_DIRECT").toString()));
                departmentCurrentReportBean.setObliInDirectAmount(
                        hmReportData.get("OBLIGATED_TOTAL_INDIRECT") == null ? 0 : Double.parseDouble(hmReportData.get("OBLIGATED_TOTAL_INDIRECT").toString()));
                departmentCurrentReportBean.setObliDistrubutableAmount(
                        hmReportData.get("OBLI_DISTRIBUTABLE_AMOUNT") == null ? 0 : Double.parseDouble(hmReportData.get("OBLI_DISTRIBUTABLE_AMOUNT").toString()));                
                departmentCurrentReportBean.setPercentageEffort(
                        Float.parseFloat(hmReportData.get( "PERCENTAGE_EFFORT") == null ? "0" : hmReportData.get( "PERCENTAGE_EFFORT").toString()));
                departmentCurrentReportBean.setAcademicYearEffort(
                        Float.parseFloat(hmReportData.get( "ACADEMIC_YEAR_EFFORT") == null ? "0" : hmReportData.get( "ACADEMIC_YEAR_EFFORT").toString()));
                departmentCurrentReportBean.setSummerYearEffort(
                        Float.parseFloat(hmReportData.get( "SUMMER_YEAR_EFFORT") == null ? "0" : hmReportData.get( "SUMMER_YEAR_EFFORT").toString()));
                departmentCurrentReportBean.setCalendarYearEffort(
                        Float.parseFloat(hmReportData.get( "CALENDAR_YEAR_EFFORT") == null ? "0" : hmReportData.get( "CALENDAR_YEAR_EFFORT").toString()));
                departmentCurrentReportBean.setCustomElements(awardTxnBean.getAwardCustomData(departmentCurrentReportBean.getMITAwardNumber()));
                vecCurrentReportData.addElement(departmentCurrentReportBean);
            }
        }
        //COEUSQA-1686 - Add additional fields to the Current  Pending Support Schema - End
        
        if(dbEngine !=null){
            result = new Vector(3,2);
            //Procedure name renamed with case 3505:Add % effort to current and Pending Report
//            result = dbEngine.executeRequest("Coeus",
//                    "call DW_GET_PENDING_SUPPORT( <<PERSON_ID>>, <<OUT RESULTSET rset>> )",
//                    "Coeus", param);
            result = dbEngine.executeRequest("Coeus",
                    "call GET_PENDING_SUPPORT( <<PERSON_ID>>, <<OUT RESULTSET rset>> )",
                    "Coeus", param);
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }
        
        int listSize = result.size();
        if (listSize > 0){
            for(int rowIndex=0; rowIndex<listSize; rowIndex++){
                departmentPendingReportBean = new DepartmentPendingReportBean();
                hmReportData = (HashMap)result.elementAt(rowIndex);
                departmentPendingReportBean.setProposalNumber((String)
                hmReportData.get("PROPOSAL_NUMBER"));
                departmentPendingReportBean.setSponsorName((String)
                hmReportData.get("SPONSOR_NAME"));
                departmentPendingReportBean.setPrincipleInvestigatorFlag(
                        hmReportData.get("PRINCIPAL_INVESTIGATOR_FLAG") == null ? false :
                            hmReportData.get("PRINCIPAL_INVESTIGATOR_FLAG").toString()
                            .equalsIgnoreCase("y") ? true : false);
                departmentPendingReportBean.setTitle((String)
                hmReportData.get("TITLE"));
                departmentPendingReportBean.setRequestEndDateTotal(
                        hmReportData.get("REQUESTED_END_DATE_TOTAL") == null ?
                            null : new Date(((Timestamp)hmReportData.get("REQUESTED_END_DATE_TOTAL")).getTime()));
                //COEUSQA-1686 - Add additional fields to the Current  Pending Support Schema - Start                
                departmentPendingReportBean.setGroupCode(getParameterValues("CURRENT_PENDING_REPORT_GROUP_NAME"));
                //COEUSQA-1686 - Add additional fields to the Current  Pending Support Schema - End
                departmentPendingReportBean.setTotalDirectCostTotal(
                        hmReportData.get("TOTAL_DIRECT_COST_TOTAL") == null ? 0 : Double.parseDouble(hmReportData.get("TOTAL_DIRECT_COST_TOTAL").toString()));
                departmentPendingReportBean.setTotalIndirectCostTotal(
                        hmReportData.get("TOTAL_INDIRECT_COST_TOTAL") == null ? 0 : Double.parseDouble(hmReportData.get("TOTAL_INDIRECT_COST_TOTAL").toString()));
                departmentPendingReportBean.setRequestedStartDateTotal(
                        hmReportData.get("REQUESTED_START_DATE_INITIAL") == null ?
                            null : new Date(((Timestamp)hmReportData.get("REQUESTED_START_DATE_INITIAL")).getTime()));
                departmentPendingReportBean.setRequestEndDateTotal(
                        hmReportData.get("REQUESTED_END_DATE_TOTAL") == null ?
                            null : new Date(((Timestamp)hmReportData.get("REQUESTED_END_DATE_TOTAL")).getTime()));
                // departmentPendingReportBean.setPersonFullName(personFullName);
                departmentPendingReportBean.setTotalCost(departmentPendingReportBean.getTotalDirectCostTotal()+departmentPendingReportBean.getTotalIndirectCostTotal());
                
                departmentPendingReportBean.setPrincipleInvestigator(
                        hmReportData.get("PRINCIPAL_INVESTIGATOR_FLAG") == null ? "Co-I" :
                            hmReportData.get("PRINCIPAL_INVESTIGATOR_FLAG").toString()
                            .equalsIgnoreCase("y") ? "PI" : "Co-I");
                //Added for case 3505:Add % effort to current and Pending Report - Start
                departmentPendingReportBean.setPercentageEffort(
                        Float.parseFloat(hmReportData.get( "PERCENTAGE_EFFORT") == null ? "0" : hmReportData.get( "PERCENTAGE_EFFORT").toString()));
                departmentPendingReportBean.setAcademicYearEffort(
                        Float.parseFloat(hmReportData.get( "ACADEMIC_YEAR_EFFORT") == null ? "0" : hmReportData.get( "ACADEMIC_YEAR_EFFORT").toString()));
                departmentPendingReportBean.setSummerYearEffort(
                        Float.parseFloat(hmReportData.get( "SUMMER_YEAR_EFFORT") == null ? "0" : hmReportData.get( "SUMMER_YEAR_EFFORT").toString()));
                departmentPendingReportBean.setCalendarYearEffort(
                        Float.parseFloat(hmReportData.get( "CALENDAR_YEAR_EFFORT") == null ? "0" : hmReportData.get( "CALENDAR_YEAR_EFFORT").toString()));
                //Added for case 3505:Add % effort to current and Pending Report - End
                //COEUSQA-1686 - Add additional fields to the Current  Pending Support Schema - Start
                departmentPendingReportBean.setCustomElements(instituteProposalTxnBean.getInstituteProposalCustomData(departmentPendingReportBean.getProposalNumber()
                , ModuleConstants.PROPOSAL_INSTITUTE_MODULE_CODE));
                //COEUSQA-1686 - Add additional fields to the Current  Pending Support Schema - End
                vecPendingReportData.addElement(departmentPendingReportBean);
            }
        }
        
        //COEUSQA-1686 - Add additional fields to the Current  Pending Support Schema - Start
        if(dbEngine !=null){
            if(investigatorAndKeyPerson){
                pendingKeyPersonResult = new Vector(3,2);
                pendingKeyPersonResult = dbEngine.executeRequest("Coeus",
                        "call GET_KEY_PERSON_PENDING_SUPPORT( <<PERSON_ID>>, <<OUT RESULTSET rset>> )",
                        "Coeus", param);
            }
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }
        
        keyPersonSize = pendingKeyPersonResult.size();
        if (keyPersonSize > 0){
            for(int rowIndex=0; rowIndex<keyPersonSize; rowIndex++){
                departmentPendingReportBean = new DepartmentPendingReportBean();
                hmReportData = (HashMap)pendingKeyPersonResult.elementAt(rowIndex);
                departmentPendingReportBean.setProposalNumber((String)
                hmReportData.get("PROPOSAL_NUMBER"));
                departmentPendingReportBean.setSponsorName((String)
                hmReportData.get("SPONSOR_NAME"));                
                departmentPendingReportBean.setTitle((String)
                hmReportData.get("TITLE"));
                departmentPendingReportBean.setRequestEndDateTotal(
                        hmReportData.get("REQUESTED_END_DATE_TOTAL") == null ?
                            null : new Date(((Timestamp)hmReportData.get("REQUESTED_END_DATE_TOTAL")).getTime()));
                departmentPendingReportBean.setTotalDirectCostTotal(
                        hmReportData.get("TOTAL_DIRECT_COST_TOTAL") == null ? 0 : Double.parseDouble(hmReportData.get("TOTAL_DIRECT_COST_TOTAL").toString()));
                departmentPendingReportBean.setTotalIndirectCostTotal(
                        hmReportData.get("TOTAL_INDIRECT_COST_TOTAL") == null ? 0 : Double.parseDouble(hmReportData.get("TOTAL_INDIRECT_COST_TOTAL").toString()));
                departmentPendingReportBean.setRequestedStartDateTotal(
                        hmReportData.get("REQUESTED_START_DATE_INITIAL") == null ?
                            null : new Date(((Timestamp)hmReportData.get("REQUESTED_START_DATE_INITIAL")).getTime()));
                departmentPendingReportBean.setRequestEndDateTotal(
                        hmReportData.get("REQUESTED_END_DATE_TOTAL") == null ?
                            null : new Date(((Timestamp)hmReportData.get("REQUESTED_END_DATE_TOTAL")).getTime()));
                departmentPendingReportBean.setPrincipleInvestigator(
                        hmReportData.get("PRINCIPAL_INVESTIGATOR_FLAG") == null ? "Key-Per" :
                            hmReportData.get("PRINCIPAL_INVESTIGATOR_FLAG").toString()
                            .equalsIgnoreCase("y") ? "PI" : "Key-Per");
                
                departmentPendingReportBean.setGroupCode(getParameterValues("CURRENT_PENDING_REPORT_GROUP_NAME"));
                
                departmentPendingReportBean.setTotalCost(departmentPendingReportBean.getTotalDirectCostTotal()+departmentPendingReportBean.getTotalIndirectCostTotal());                
                departmentPendingReportBean.setPercentageEffort(
                        Float.parseFloat(hmReportData.get( "PERCENTAGE_EFFORT") == null ? "0" : hmReportData.get( "PERCENTAGE_EFFORT").toString()));
                departmentPendingReportBean.setAcademicYearEffort(
                        Float.parseFloat(hmReportData.get( "ACADEMIC_YEAR_EFFORT") == null ? "0" : hmReportData.get( "ACADEMIC_YEAR_EFFORT").toString()));
                departmentPendingReportBean.setSummerYearEffort(
                        Float.parseFloat(hmReportData.get( "SUMMER_YEAR_EFFORT") == null ? "0" : hmReportData.get( "SUMMER_YEAR_EFFORT").toString()));
                departmentPendingReportBean.setCalendarYearEffort(
                        Float.parseFloat(hmReportData.get( "CALENDAR_YEAR_EFFORT") == null ? "0" : hmReportData.get( "CALENDAR_YEAR_EFFORT").toString()));
                departmentPendingReportBean.setCustomElements(instituteProposalTxnBean.getInstituteProposalCustomData(departmentPendingReportBean.getProposalNumber()
                , ModuleConstants.PROPOSAL_INSTITUTE_MODULE_CODE));
                vecPendingReportData.addElement(departmentPendingReportBean);
            }
        }
        //COEUSQA-1686 - Add additional fields to the Current  Pending Support Schema - End
        
        htReportData.put(KeyConstants.DEPARTMENT_CURRENT_REPORT_DATA, vecCurrentReportData);
        htReportData.put(KeyConstants.DEPARTMENT_PENDING_REPORT_DATA, vecPendingReportData);
        return htReportData;
    }
    
    //Added for case 3685 - Remove Word icons - start
    /**
     * Converts ByteArrayOutputStream to array of bytes
     *
     * @param ByteArrayOutputStream
     * @return byte[]
     */
    private byte[] convert(ByteArrayOutputStream baos){
        byte[] byteArray = null;
        try {
          byteArray = baos.toByteArray();                  
        }finally{   
           try {
              baos.close();
           }catch (IOException ioex){}   
        }
        return byteArray;
    }
    //Added for case 3685 - Remove Word icons - end
  
   
    
    public static void main(String[] args){
        try{
            DepartmentPersonTxnBean departmentPersonTxnBean = new DepartmentPersonTxnBean();
            /*
            String fullName = departmentPersonTxnBean.getPersonName("900001182");
            System.out.println("Full Name : "+fullName);
             */
            //COEUSQA-1686 - Add additional fields to the Current  Pending Support Schema - Start
            //departmentPersonTxnBean.getCurrentAndPendingReport("900005309");
            departmentPersonTxnBean.getCurrentAndPendingReport("900005309",false);
            //COEUSQA-1686 - Add additional fields to the Current  Pending Support Schema - End
            /*if(coeusVector!=null){
                System.out.println("Size : "+coeusVector.size());
            }else{
                System.out.println("Data is null");
            }*/
            
        }catch(Exception ex){
            ex.printStackTrace();
        }
    }
}
