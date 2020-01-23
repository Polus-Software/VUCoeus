/*
 * SyncBudgetPersons.java
 *
 * Created on 10 July 2006, 14:05
 */

/* PMD check performed, and commented unused imports and variables on 04-MAY-2011
 * by Maharaja Palanichamy
 */

package edu.wmc.coeuslite.utils;

import edu.mit.coeus.bean.UserInfoBean;
import edu.mit.coeus.budget.bean.AppointmentsBean;
import edu.mit.coeus.budget.bean.BudgetDataTxnBean;
import edu.mit.coeus.budget.bean.BudgetInfoBean;
import edu.mit.coeus.budget.bean.BudgetPersonSyncBean;
import edu.mit.coeus.budget.bean.BudgetPersonsBean;
import edu.mit.coeus.exception.CoeusException;
import edu.mit.coeus.user.bean.UserMaintDataTxnBean;
import edu.mit.coeus.utils.CoeusFunctions;
import edu.mit.coeus.utils.CoeusVector;
import edu.mit.coeus.utils.ComboBoxBean;
import edu.mit.coeus.utils.TypeConstants;
import edu.mit.coeus.utils.dbengine.DBException;
import edu.mit.coeuslite.utils.DateUtils;
import edu.mit.coeuslite.utils.bean.WebTxnBean;
import edu.wmc.coeuslite.budget.bean.AppointmentTypeBean;
import java.sql.Date;
import java.sql.Timestamp;
//import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.Vector;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.apache.commons.beanutils.BeanUtilsBean;
import org.apache.commons.beanutils.DynaBean;
import org.apache.struts.action.DynaActionForm;
import org.apache.struts.validator.DynaValidatorForm;

/**
 *
 * @author  mohann
 */
public class SyncBudgetPersons {
    //    private HttpServletRequest request;
    //    private HttpSession session;
    public static final String EMPTY_STRING = "";
    public static String navigator = EMPTY_STRING;
    public static final String NINE_MON_DURATION = "9M DURATION";
    public static final String TEN_MON_DURATION = "10M DURATION";
    public static final String ELEVEN_MON_DURATION = "11M DURATION";
    public static final String TWELVE_MON_DURATION = "12M DURATION";
    public static final String REG_EMPLOYEE = "REG EMPLOYEE";
    public static final String SUM_EMPLOYEE = "SUM EMPLOYEE";
    public static final String TMP_EMPLOYEE = "TMP EMPLOYEE";
    SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
    //  CoeusVector cvAppointmentDetails = new CoeusVector();
    
    /** Creates a new instance of SyncBudgetPersons */
    public SyncBudgetPersons() {
    }
    
    /** This method will sync the Propsoal Persons with the Budget Persons
     * @param budgetInfoBean
     * @param functionType
     * @param HttpServletRequest
     * @param DynaActionForm
     * @throws Exception
     * @param HashMap
     */
    public HashMap getAllPropPersons(BudgetInfoBean budgetInfoBean,String functionType,HttpServletRequest request, DynaActionForm dynaForm) throws Exception{
        //        this.request = request;
        //        this.session = request.getSession();
        HttpSession session = request.getSession();
        HashMap personData = null;
        HashMap hmAllPropInfo = new HashMap();
        HashMap hmMultipleApps = new HashMap();
        CoeusVector cvBudgetPersonData = new CoeusVector();
        CoeusVector cvValidPersons = null;
        CoeusVector cvInvalidPersons = null;
        CoeusVector cvNullData = null;
        CoeusVector cvAllAppointments = new CoeusVector();
        CoeusVector cvPersonData = new CoeusVector();
        AppointmentsBean appointmentsBean = null;
        CoeusVector cvMultipleAppointments = new CoeusVector();
        CoeusVector cvMultiplePersonId = new CoeusVector();
        //Include Rolodex in Budget Persons - Enhancement
        CoeusVector cvValidPersonBeans = new CoeusVector();
        CoeusVector cvInvalidPersonBeans = new CoeusVector();
        BudgetPersonSyncBean budgetPersonSyncBean;
        
        /** Get all the proposal persons data which contains valid budget persons
         *and invalid budget persons
         */
        personData = getAllProposalPerForBudgetPersons(budgetInfoBean, functionType, request);
        if(personData!=null){
            cvValidPersons = (CoeusVector)personData.get("VALID_PERSONS");
            cvInvalidPersons = (CoeusVector)personData.get("INVALID_PERSONS");
            //Include Rolodex in Budget Persons - Enhancement
            cvValidPersonBeans = (CoeusVector)personData.get("VALID_PERSON_BEANS");
            cvInvalidPersonBeans = (CoeusVector)personData.get("INVALID_PERSON_BEANS");
            
        }
        // Get all the valid persons and then prepare budgetPersons data
        if(cvValidPersons!=null && cvValidPersonBeans !=null && cvValidPersons.size() >0 && cvValidPersonBeans.size()>0 ){
            for(int index = 0; index < cvValidPersons.size() ; index++){
                String personId = (String)cvValidPersons.get(index);
                
                //Include Rolodex in Budget Valid Person
                budgetPersonSyncBean = (BudgetPersonSyncBean)cvValidPersonBeans.get(index);
                boolean isNonEmployee =  budgetPersonSyncBean.isNonEmployee();
                //Added for Case#2918 - Use of Salary Anniversary Date for calculating inflation in budget development module -Start
                java.sql.Date salAnnivDate = null;                
                if(budgetPersonSyncBean.getSalaryAnniversaryDate()!= null){                    
                   long salAnivDate = budgetPersonSyncBean.getSalaryAnniversaryDate().getTime();
                   salAnnivDate = new java.sql.Date(salAnivDate);
                }
                //Added for Case#2918 - Use of Salary Anniversary Date for calculating inflation in budget development module -End
                
                String fullName = getPersonFullName(personId, request);
                CoeusVector cvBudgetPerons =
                getBudgetPersonsData(personId,fullName,budgetInfoBean,isNonEmployee,request,salAnnivDate);
                cvBudgetPersonData.addAll(cvBudgetPerons);
            }
        }
        if(cvInvalidPersons!= null && cvInvalidPersonBeans !=null  && cvInvalidPersons.size() > 0 && cvInvalidPersonBeans.size() > 0){
            for(int index = 0; index < cvInvalidPersons.size(); index++){
                cvAllAppointments.add(getAppointmentsForPerson((String)cvInvalidPersons.get(index), request));
            }
            if(cvAllAppointments!= null && cvAllAppointments.size() > 0){
                for(int index = 0; index < cvAllAppointments.size() ; index++){
                    CoeusVector cvAppData =(CoeusVector) cvAllAppointments.get(index);
                    String personId = (String)cvInvalidPersons.get(index);
                    //Include Rolodex in Budget Valid Person
                    budgetPersonSyncBean = (BudgetPersonSyncBean)cvInvalidPersonBeans.get(index);
                    //  boolean isNonEmployee =  budgetPersonSyncBean.isNonEmployee();
                    
                    appointmentsBean = (AppointmentsBean)cvAppData.get(0);
                    if(appointmentsBean.getAppointmentType() == null){
                        cvNullData = updateSyncData(appointmentsBean , personId , dynaForm, budgetPersonSyncBean, request);
                        cvPersonData.addAll(cvNullData);
                    }else{
                        cvMultiplePersonId.addElement(personId);
                        cvMultipleAppointments.addElement(cvAppData);
                    }
                }
                
                hmMultipleApps = getMultipleAppointmentsPerson(cvMultipleAppointments, cvMultiplePersonId, dynaForm, request);
            }
        }
        hmAllPropInfo.put("validPersonsSyncDetail", cvBudgetPersonData);
        hmAllPropInfo.put("multipleAppointmentsInfo", cvMultipleAppointments);
        hmAllPropInfo.put("syncPersonsInfo",cvPersonData);
        hmAllPropInfo.put("hmMultipleAppointments",hmMultipleApps);
        hmAllPropInfo.put("multipleAppointmentsNames",cvMultiplePersonId);
        session.setAttribute("validPersonsSyncDetail",cvBudgetPersonData);
        session.setAttribute("multipleAppointmentsNames", cvMultiplePersonId);
        //Modified for Case#3869 -Save not working for budget person - Start
        //session.setAttribute("optionsAppointmentTypes", getAppointmentTypes());
        session.setAttribute("optionsAppointmentTypes", getAppointmentTypes(request));
        //Modified for Case#3869 - Save not working for budget person - End
        
        return hmAllPropInfo;
    }
    
    
    /** This is to get all the proposal persons to Update budget persons
     * @param proposalNumber, Version Number from budgetInfoBean
     * @functionType
     * @returns HashMap contains valid Persons to fetch the data and Invalid persons where necessary data has to be filled
     * @throws Exception
     */
    private HashMap getAllProposalPerForBudgetPersons(
    BudgetInfoBean budgetInfoBean,String functionType, HttpServletRequest request) throws Exception{
        WebTxnBean  webTxnBean = new WebTxnBean();
        HashMap hmProposalPersonData = new HashMap();
        DateUtils dateUtils = new DateUtils();
        hmProposalPersonData.put("proposalNumber",budgetInfoBean.getProposalNumber());
        hmProposalPersonData.put("versionNumber",new Integer(budgetInfoBean.getVersionNumber()));
        hmProposalPersonData.put("mode",functionType);
        Hashtable htPersonData = (Hashtable) webTxnBean.getResults(request, "getPersionDetailForBudgetSync", hmProposalPersonData);
        Vector vecPersonData = (Vector)htPersonData.get("getPersionDetailForBudgetSync");
        CoeusVector cvValidPersons = null;
        CoeusVector cvInvalidPersons = null;
        HashMap hmPersons = new HashMap();
        String personId = EMPTY_STRING;
        String PERSON_TYPE ="Y";
        DynaValidatorForm dyanForm = null;
        if(vecPersonData!= null && vecPersonData.size() > 0){
            cvValidPersons = new CoeusVector();
            cvInvalidPersons = new CoeusVector();
            //Include Rolodex in Budget Persons - Enhancement
            CoeusVector cvValidPersonBeans = new CoeusVector();
            CoeusVector cvInvalidPersonBeans = new CoeusVector();
            BudgetPersonSyncBean budgetPersonSyncBean;
            //Include Rolodex in Budget Persons - Enhancement
            for(int index = 0; index< vecPersonData.size(); index++){
                dyanForm = (DynaValidatorForm)vecPersonData.elementAt(index);
                if(dyanForm!=null){
                    personId = (String)dyanForm.get("personId");
                    //Include Rolodex in Budget Persons - Enhancement
                    String fullName, nonMitPerson;
                    fullName = (String)dyanForm.get("fullName");
                    nonMitPerson = (String)dyanForm.get("nonEmployeeFlag");
                    budgetPersonSyncBean = new BudgetPersonSyncBean();
                    budgetPersonSyncBean.setPersonId(personId);
                    budgetPersonSyncBean.setFullName(fullName);
                    budgetPersonSyncBean.setNonEmployee(nonMitPerson.equalsIgnoreCase("Y") ? true : false);
                    //Include Rolodex in Budget Persons - Enhancement
                    //Added for Case#2918 - Use of Salary Anniversary Date for calculating inflation in budget development module -Start
                    if(dyanForm.get("salaryAnniversaryDate") != null){
//                    dateUtils.formatDate(dyanForm.get("salaryAnniversaryDate").toString(),SIMPLE_DATE_FORMAT);
//                        java.sql.Date effectiveDate  = dateUtils.getSQLDate((String)dyanForm.get("salaryAnniversaryDate").toString());
                        String salAnnivDate = (String) dyanForm.get("salaryAnniversaryDate");
                        java.util.StringTokenizer tokenizer = new StringTokenizer( salAnnivDate," ");
                        salAnnivDate = tokenizer.nextToken();
//                        DateFormat df = new SimpleDateFormat("MM/dd/yyyy");   
                        SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd");
                        SimpleDateFormat format2 = new SimpleDateFormat("MM/dd/yyyy");
                        String date2 = format2.format(format1.parse(salAnnivDate));
                        java.util.Date d1 = (java.util.Date) format2.parse(date2);                        
                        java.sql.Date d2 = new java.sql.Date(d1.getTime()); 
//                        salAnnivDate  = dateUtils.formatDate(salAnnivDate,":/.,|-","MM/dd/yyyy");
//                        java.sql.Date newSalAnnivDate = dateUtils.getSQLDate(salAnnivDate);
//                    Date salaryAnnivDate = (java.sql.Date) dyanForm.get("salaryAnniversaryDate");
                        budgetPersonSyncBean.setSalaryAnniversaryDate(d2);
                    }
                    //Added for Case#2918 - Use of Salary Anniversary Date for calculating inflation in budget development module - End
                    /** Group the persons based on the flag and get the valid and
                     * invalid persons data in a vector
                     */
                    if(PERSON_TYPE.equals(dyanForm.get("personType").toString())){
                        cvValidPersons.addElement(personId);
                        //Include Rolodex in Budget Persons - Enhancement - START
                        cvValidPersonBeans.add(budgetPersonSyncBean);
                        //Include Rolodex in Budget Persons - Enhancement - END
                    }else{
                        cvInvalidPersons.addElement(personId);
                        //Include Rolodex in Budget Persons - Enhancement - START
                        cvInvalidPersonBeans.add(budgetPersonSyncBean);
                        //Include Rolodex in Budget Persons - Enhancement - END
                    }
                }
            }
            hmPersons.put("VALID_PERSONS", cvValidPersons);
            hmPersons.put("INVALID_PERSONS", cvInvalidPersons);
            //Include Rolodex in Budget Persons - Enhancement - START
            hmPersons.put("VALID_PERSON_BEANS", cvValidPersonBeans);
            hmPersons.put("INVALID_PERSON_BEANS", cvInvalidPersonBeans);
            //Include Rolodex in Budget Persons - Enhancement - END
        }
        return hmPersons;
    }
    
    /**
     * Get the person full name for corresponding person id
     * @param personId
     * @throws Exception
     * @return fullPersonName
     */
    private String getPersonFullName(String personId, HttpServletRequest request) throws Exception {
        WebTxnBean webTxnBean = new WebTxnBean();
        HashMap hmPersonData = new HashMap();
        String personName = EMPTY_STRING;
        hmPersonData.put("personId",personId);
        Hashtable htPersonData = (Hashtable) webTxnBean.getResults(request, "getPersonName", hmPersonData);
        if(htPersonData !=null && htPersonData.size()>0){
            personName = (String)((HashMap) htPersonData.get("getPersonName")).get("ls_name");
        }
        return personName;
    }
    
    /** This method will set the Proposal Persons with the Budget Persons In New Mode
     * @ param personId, personName, budgetInfoBean
     * @ throws Exception
     * @ return CoeusVector
     */
    private CoeusVector getBudgetPersonsData(String personId,
    String personName,BudgetInfoBean budgetInfoBean, boolean isNonEmployee, HttpServletRequest request,Date salAnnivDate) throws Exception{
        CoeusVector cvBudgetPersons = new CoeusVector();
        CoeusVector cvData = getAppointmentsForPerson(personId, request);
        BudgetPersonsBean budgetPersonsBean = null;
        AppointmentTypeBean appointmentsBean = new AppointmentTypeBean();
        if(cvData!= null && cvData.size() > 0){
            appointmentsBean = (AppointmentTypeBean)cvData.get(0);
            budgetPersonsBean = new BudgetPersonsBean();
            budgetPersonsBean.setProposalNumber(budgetInfoBean.getProposalNumber());
            budgetPersonsBean.setAw_ProposalNumber(budgetInfoBean.getProposalNumber());
            budgetPersonsBean.setVersionNumber(budgetInfoBean.getVersionNumber());
            budgetPersonsBean.setAw_VersionNumber(budgetInfoBean.getVersionNumber());
            budgetPersonsBean.setPersonId(personId);
            budgetPersonsBean.setAw_PersonId(personId);
            budgetPersonsBean.setFullName(personName);
            budgetPersonsBean.setJobCode( appointmentsBean.getJobCode());
            budgetPersonsBean.setAw_JobCode(appointmentsBean.getJobCode());
            budgetPersonsBean.setAppointmentType(appointmentsBean.getAppointmentType());
            budgetPersonsBean.setAw_AppointmentType(appointmentsBean.getAppointmentType());
            budgetPersonsBean.setEffectiveDate(budgetInfoBean.getStartDate());
            budgetPersonsBean.setAw_EffectiveDate(budgetInfoBean.getStartDate());
            budgetPersonsBean.setCalculationBase(appointmentsBean.getSalary());
            budgetPersonsBean.setAw_CalculationBase(appointmentsBean.getSalary());
            budgetPersonsBean.setUpdateTimestamp(appointmentsBean.getUpdateTimestamp());
            budgetPersonsBean.setUpdateUser(appointmentsBean.getUpdateUser());
            budgetPersonsBean.setAcType(TypeConstants.INSERT_RECORD);
            //Added for Case#2918 - Use of Salary Anniversary Date for calculating inflation in budget development module -Start
            budgetPersonsBean.setSalaryAnniversaryDate(salAnnivDate);
            budgetPersonsBean.setAw_SalaryAnniversaryDate(salAnnivDate);
            //Added for Case#2918 - Use of Salary Anniversary Date for calculating inflation in budget development module -End
            //Include Rolodex in Budget Persons - Enhancement - START - 2
            budgetPersonsBean.setNonEmployee(isNonEmployee);
            budgetPersonsBean.setAw_nonEmployeeFlag(isNonEmployee);
            //Include Rolodex in Budget Persons - Enhancement - END - 2
            
            cvBudgetPersons.addElement(budgetPersonsBean);
        }
        return cvBudgetPersons;
    }
    
    
    
    /** This method to get Appointments for particular person
     * @return CoeusVector
     * @param personId String
     * @ throws Exception
     * @exception Exception if the instance of dbEngine is not available.
     */
    public CoeusVector getAppointmentsForPerson(String personId, HttpServletRequest request) throws Exception{
        WebTxnBean webTxnBean = new WebTxnBean();
        DynaValidatorForm dynaForm = null;
        AppointmentTypeBean appointmentsBean = null;
        Vector vecAppPerson = new Vector();
        HashMap hmAppForPerson = new HashMap();
        HttpSession session = request.getSession();
        hmAppForPerson.put("personId",personId);
        Hashtable htAppForPerson = (Hashtable) webTxnBean.getResults(request, "getAppointmentsForPerson", hmAppForPerson);
        vecAppPerson = (Vector)  htAppForPerson.get("getAppointmentsForPerson");
        CoeusVector cvPersonData = new CoeusVector();
        edu.mit.coeus.bean.UserInfoBean userInfoBean
        = (edu.mit.coeus.bean.UserInfoBean)session.getAttribute("user"+session.getId());
        String userId = userInfoBean.getUserId();
        int recCount =0;
        if(vecAppPerson !=null && !vecAppPerson.equals(EMPTY_STRING)){
            recCount =vecAppPerson.size();
        }
        if (recCount >0){
            for(int rowIndex=0;rowIndex<recCount;rowIndex++){
                appointmentsBean = new AppointmentTypeBean();
                dynaForm =(DynaValidatorForm) vecAppPerson.get(rowIndex);
                appointmentsBean.setAppointmentType((String)dynaForm.get("appointmentType"));
                appointmentsBean.setJobCode((String)dynaForm.get("jobCode"));
                appointmentsBean.setSalary(Double.parseDouble(dynaForm.get("salary") == null ? "0" : dynaForm.get("salary").toString()));
                appointmentsBean.setPrimarySecondaryIndicator((String)dynaForm.get("primarySecondaryIndicator"));
                appointmentsBean.setUnitNumber((String)dynaForm.get("unitNumber"));
                appointmentsBean.setAppointmentStartDate((Date)dynaForm.get("appointmentStartDate"));
                appointmentsBean.setAppointmentEndDate((Date)dynaForm.get("appointmentEndDate"));
                appointmentsBean.setUnitName((String)dynaForm.get("unitName"));
                appointmentsBean.setJobTitle((String)dynaForm.get("jobTitle"));
                appointmentsBean.setUpdateTimestamp(prepareTimeStamp());
                appointmentsBean.setUpdateUser(userId);
                cvPersonData.addElement(appointmentsBean);
            }
        }  else{
            appointmentsBean = new AppointmentTypeBean();
            appointmentsBean.setPrimarySecondaryIndicator(null);
            appointmentsBean.setAppointmentType(null);
            cvPersonData.addElement(appointmentsBean);
            
        }
        return cvPersonData;
    }
    
    
    /** Update if the person doesn't have appoinement type and Job code
     * @param contains the bean of persons which doen't have either AppoinentType or Job code
     * @param personId
     * @param DynaActionForm
     * @returns the CoeusVector of persons which has all the details required for the Budget persons
     * @throws Exception
     */
    private CoeusVector updateSyncData(AppointmentsBean appointmentsBean , String personId ,  DynaActionForm dynaForm, 
            BudgetPersonSyncBean budgetPersonSyncBean, HttpServletRequest request)throws Exception {
        CoeusVector cvNullData = new CoeusVector();
        DynaBean appTypeDynaForm = null;
        String fullName = budgetPersonSyncBean.getFullName();
        WebTxnBean webTxnBean = new WebTxnBean();
        
        appTypeDynaForm = ((DynaBean)dynaForm).getDynaClass().newInstance();
        appTypeDynaForm.set("personId", personId);
        //Modified for Rolodex in Budget Persons Enhancement
        // appTypeDynaForm.set("fullName", getPersonFullName(personId));
        appTypeDynaForm.set("fullName", budgetPersonSyncBean.getFullName());
        if(appointmentsBean.getJobCode() == null || appointmentsBean.getJobCode().equals(EMPTY_STRING)){            
            //Default Job Code - Start
            //Get Default Job Code from PARAMETERS TABLE
            Map hmDefaultJobCode  = new HashMap();
            hmDefaultJobCode.put("jobCode", "DEFAULT_JOB_CODE");
            hmDefaultJobCode =(Hashtable)webTxnBean.getResults(request, "getDefaultJobCode", hmDefaultJobCode);
            hmDefaultJobCode = (HashMap)hmDefaultJobCode.get("getDefaultJobCode");             
            appTypeDynaForm.set("jobCode",(String)hmDefaultJobCode.get("ls_value"));
            //Default Job Code - End
        }else{
            appTypeDynaForm.set("jobCode",appointmentsBean.getJobCode());
        }
        appTypeDynaForm.set("salary", new Double(appointmentsBean.getSalary()) );
        //Modified for case#3869 -Save not working for budget person - Start
//        appTypeDynaForm.set("appointmentType","REG EMPLOYEE");        
        if(appTypeDynaForm.get("appointmentType") == null || appTypeDynaForm.get("appointmentType").equals(EMPTY_STRING) ){
            
            WebTxnBean  appTypeWebTxnBean = new WebTxnBean();
            /* Set argument name to get data from lookup table */
            String argumentName = "AppointmentTypes";
            /* Get appointment types from argument lookup table */
            HashMap hmlookupArgument = new HashMap();
            hmlookupArgument.put("argumentName",argumentName);
            Hashtable htAppointmentTypesData =
                    (Hashtable)appTypeWebTxnBean.getResults(request, "getArgumentValuesData", hmlookupArgument);
            Vector vecAppointmentTypes = (Vector)htAppointmentTypesData.get("getArgValueList");
            if(vecAppointmentTypes != null && vecAppointmentTypes.size() > 0){
                for(int appCnt = 0 ;appCnt <vecAppointmentTypes.size();appCnt++){
                    edu.mit.coeuslite.utils.ComboBoxBean comboBoxBean = (edu.mit.coeuslite.utils.ComboBoxBean)vecAppointmentTypes.get(appCnt);
                    if("REG EMPLOYEE".equals(comboBoxBean.getDescription())){
                        appTypeDynaForm.set("appointmentType",comboBoxBean.getDescription());
                        break;
                    }
                }
            }
        }else{
            appTypeDynaForm.set("appointmentType",appointmentsBean.getAppointmentType());
        }
        //Modified for Case#3869- Save not working for budget person - End
        //Include Rolodex in Budget Persons - Enhancement - START
        
        appTypeDynaForm.set("nonEmployeeFlag", Boolean.toString(budgetPersonSyncBean.isNonEmployee()));
        // syncBean.setNonEmployee(budgetPersonSyncBean.isNonEmployee());
        //Include Rolodex in Budget Persons - Enhancement - END
        //Added for Case#2918 - Use of Salary Anniversary Date for calculating inflation in budget development module -Start
        if(budgetPersonSyncBean.getSalaryAnniversaryDate() != null){      
            java.sql.Date salAnnivDate = budgetPersonSyncBean.getSalaryAnniversaryDate();
            String strSalAnnivDate = dateFormat.format(salAnnivDate);
            appTypeDynaForm.set("salaryAnniversaryDate", strSalAnnivDate);
        }        
        //Added for Case#2918 - Use of Salary Anniversary Date for calculating inflation in budget development module -End
        
        cvNullData.addElement(appTypeDynaForm);
        return cvNullData;
    }
    
    /** Get the persons name if multiple appoinements exists
     *  @param CoeusVector containing the mulptiple appoinements for the selected budget
     *  @param CoeusVector containing the persons names
     *  @throws Exception
     *  @ return CoeusVector
     */
    private HashMap getMultipleAppointmentsPerson(CoeusVector cvMultipleAppointments , CoeusVector cvPersons ,
    DynaActionForm dynaForm, HttpServletRequest request) throws Exception{
        HashMap hmMultipleAppDetail = new HashMap();
        if(cvMultipleAppointments!= null && cvMultipleAppointments.size() > 0){
            for(int index = 0; index < cvMultipleAppointments.size() ; index++){
                CoeusVector cvMultipleApp = (CoeusVector)cvMultipleAppointments.get(index);
                //Convert AppointmentTypeBean to DynaForm
                CoeusVector cvDynaForm = getDynaValidatorForm(cvMultipleApp,dynaForm);
                String personId = (String) cvPersons.get(index);
                String fullName = getPersonFullName(personId, request);
                hmMultipleAppDetail.put(fullName,cvDynaForm);
            }
        }
        return hmMultipleAppDetail;
    }
    
    /**
     * Convert the AppointmentTypeBean to DynaForm
     * @param AppointmentTypeBean
     * @throws Exception if any error occurs
     * @return CoeusVector of DyanForm
     */
    private CoeusVector getDynaValidatorForm(CoeusVector  cvAppointmentTypeBean, DynaActionForm dynaForm) throws Exception{
        CoeusVector cvAppointmentInfo = new CoeusVector();
        DynaBean appTypeDynaForm = null;
        BeanUtilsBean beanUtilsBean = null;
        AppointmentTypeBean appointmentTypeBean = null;
        for(int index=0;  index < cvAppointmentTypeBean.size() ; index++ ){
            beanUtilsBean = new BeanUtilsBean();
            appointmentTypeBean = (AppointmentTypeBean) cvAppointmentTypeBean.get(index);
            appTypeDynaForm = ((DynaBean)dynaForm).getDynaClass().newInstance();
            appTypeDynaForm.set("appointmentType", appointmentTypeBean.getAppointmentType());
            appTypeDynaForm.set("jobCode",  appointmentTypeBean.getJobCode() );
            appTypeDynaForm.set("salary", new Double(appointmentTypeBean.getSalary()) );
            appTypeDynaForm.set("primarySecondaryIndicator",  appointmentTypeBean.getPrimarySecondaryIndicator() );
            appTypeDynaForm.set("unitNumber", appointmentTypeBean.getUnitNumber());
            appTypeDynaForm.set("unitName", appointmentTypeBean.getUnitName() );
            appTypeDynaForm.set("jobTitle", appointmentTypeBean.getJobTitle());
            appTypeDynaForm.set("appointmentStartDate", appointmentTypeBean.getAppointmentEndDate() );
            appTypeDynaForm.set("appointmentEndDate", appointmentTypeBean.getAppointmentEndDate() );
            appTypeDynaForm.set("updateTimestamp",appointmentTypeBean.getUpdateTimestamp().toString());
            appTypeDynaForm.set("updateUser",appointmentTypeBean.getUpdateUser());
            cvAppointmentInfo.addElement(appTypeDynaForm);
        }
        return cvAppointmentInfo;
    }
    /**
     * Method to get Selected  Appointments
     * @param HttpServletRequest
     * @param BudgetInfoBean
     * @return CoeusVector of Selected data
     * @throws Exception if exception occur
     */
    public CoeusVector getSelectedAppointments(HttpServletRequest request , BudgetInfoBean budgetInfoBean) throws Exception{
        HttpSession session = request.getSession();
        HashMap hmMultipleAppointmentsDetail = (HashMap) session.getAttribute("multipleAppointmentsPersons");
        CoeusVector cvMultiplePersonId = (CoeusVector) session.getAttribute("multipleAppointmentsNames");
        CoeusVector cvMultipleAppInfo = new CoeusVector();
        String appointmentTypeCode = null;
        DynaValidatorForm  dynaForm = null;
        BudgetPersonsBean budgetPersonsBean = null;
        if( (hmMultipleAppointmentsDetail != null && hmMultipleAppointmentsDetail.size() > 0 )
        && cvMultiplePersonId !=null && cvMultiplePersonId.size() >0 ){
            for(int index = 0; index < hmMultipleAppointmentsDetail.size(); index++){
                String personId= (String) cvMultiplePersonId.elementAt(index);
                String personName = getPersonFullName(personId, request);
                appointmentTypeCode = request.getParameter(personName);
                CoeusVector cvAppDet = (CoeusVector) hmMultipleAppointmentsDetail.get(personName);
                if((cvAppDet !=null && cvAppDet.size() >0)&& (appointmentTypeCode !=null && !appointmentTypeCode.equals(EMPTY_STRING)) ) {
                    dynaForm= (DynaValidatorForm) cvAppDet.elementAt(Integer.parseInt(appointmentTypeCode));
                    budgetPersonsBean = new BudgetPersonsBean();
                    budgetPersonsBean.setProposalNumber(budgetInfoBean.getProposalNumber());
                    budgetPersonsBean.setAw_ProposalNumber(budgetInfoBean.getProposalNumber());
                    budgetPersonsBean.setVersionNumber(budgetInfoBean.getVersionNumber());
                    budgetPersonsBean.setAw_VersionNumber(budgetInfoBean.getVersionNumber());
                    budgetPersonsBean.setPersonId(personId);
                    budgetPersonsBean.setAw_PersonId(personId);
                    budgetPersonsBean.setFullName(personName);
                    budgetPersonsBean.setJobCode((String) dynaForm.get("jobCode"));
                    budgetPersonsBean.setAw_JobCode((String) dynaForm.get("jobCode"));
                    budgetPersonsBean.setAppointmentType((String) dynaForm.get("appointmentType"));
                    budgetPersonsBean.setAw_AppointmentType((String) dynaForm.get("appointmentType"));
                    budgetPersonsBean.setEffectiveDate(budgetInfoBean.getStartDate());
                    budgetPersonsBean.setAw_EffectiveDate(budgetInfoBean.getStartDate());
                    budgetPersonsBean.setCalculationBase( Double.parseDouble(dynaForm.get("salary").toString()));
                    budgetPersonsBean.setAw_CalculationBase( Double.parseDouble(dynaForm.get("salary").toString()));
                    String strTime = (String)dynaForm.get("updateTimestamp");
                    Timestamp stamp = Timestamp.valueOf(strTime);
                    budgetPersonsBean.setUpdateTimestamp(stamp);
                    budgetPersonsBean.setUpdateUser((String) dynaForm.get("updateUser"));
                    budgetPersonsBean.setAcType(TypeConstants.INSERT_RECORD);
                    cvMultipleAppInfo.addElement(budgetPersonsBean);
                }
            }
        }
        return cvMultipleAppInfo;
    }
    
    /*
     * Preparing the Appointements Type Combobox bean
     * @ return Vector
     */
    //Modified for Case#3869-Save not working for budget person - Start
//    private Vector getAppointmentTypes(){
    private Vector getAppointmentTypes(HttpServletRequest request) throws Exception{      
        Vector  vecAppTypes = new Vector();
//        ComboBoxBean cmbAppTypes = new ComboBoxBean(NINE_MON_DURATION,NINE_MON_DURATION);
//        vecAppTypes.addElement(cmbAppTypes);
//        cmbAppTypes = new ComboBoxBean(TEN_MON_DURATION,TEN_MON_DURATION);
//        vecAppTypes.addElement(cmbAppTypes);
//        cmbAppTypes = new ComboBoxBean(ELEVEN_MON_DURATION,ELEVEN_MON_DURATION);
//        vecAppTypes.addElement(cmbAppTypes);
//        cmbAppTypes = new ComboBoxBean(TWELVE_MON_DURATION,TWELVE_MON_DURATION);
//        vecAppTypes.addElement(cmbAppTypes);
//        cmbAppTypes = new ComboBoxBean(REG_EMPLOYEE,REG_EMPLOYEE);
//        vecAppTypes.addElement(cmbAppTypes);
//        cmbAppTypes = new ComboBoxBean(SUM_EMPLOYEE,SUM_EMPLOYEE);
//        vecAppTypes.addElement(cmbAppTypes);
//        cmbAppTypes = new ComboBoxBean(TMP_EMPLOYEE,TMP_EMPLOYEE);
//        vecAppTypes.addElement(cmbAppTypes);
        //COEUSQA-2036 Code Table Prop Dev Appt Type and Period Types - Start
        //WebTxnBean  webTxnBean = new WebTxnBean();
        /* Set argument name to get data from lookup table */
        String argumentName = "AppointmentTypes"; 
        /* Get appointment types from argument lookup table */
        //HashMap hmlookupArgument = new HashMap();
        //hmlookupArgument.put("argumentName",argumentName);
        //Hashtable htAppointmentTypesData = 
        //    (Hashtable)webTxnBean.getResults(request, "getArgumentValuesData", hmlookupArgument);
        //Vector vecAppointmentTypes = (Vector)htAppointmentTypesData.get("getArgValueList"); 
        Vector vecAppointmentTypes = fetchAppointmentTypes();
        //COEUSQA-2036 Code Table Prop Dev Appt Type and Period Types - End
          if(vecAppointmentTypes != null && vecAppointmentTypes.size() >0){
              for(int appCnt = 0 ;appCnt <vecAppointmentTypes.size();appCnt++){
                  edu.mit.coeuslite.utils.ComboBoxBean comboBoxBean = (edu.mit.coeuslite.utils.ComboBoxBean)vecAppointmentTypes.get(appCnt);
                  ComboBoxBean appTypeCmbBean =  new ComboBoxBean();
                  appTypeCmbBean.setCode(comboBoxBean.getDescription());
                  appTypeCmbBean.setDescription(comboBoxBean.getDescription());
                  vecAppTypes.add(appTypeCmbBean);
              }
          }
//         vecAppTypes = vecAppTypes != null ? vecAppTypes : new Vector();
        return vecAppTypes;
    }
    //Modified for Case#3869 - Save not working for budget person - End
    
    //COEUSQA-2036 Code Table Prop Dev Appt Type and Period Types - Start
    /**
     * Fetch all the appointment types from the database
     * @return Vector vecAppointmentTypes
     */
    public Vector fetchAppointmentTypes(){
        Vector vecAppointmentTypes = new Vector();
        BudgetDataTxnBean budgetTxnBean = new BudgetDataTxnBean();
        try {
        	// JM 12-11-2012 only want active appointment types
            //HashMap appointmentTypes = budgetTxnBean.getAppointmentTypeValues();
            HashMap appointmentTypes = budgetTxnBean.getActiveAppointmentTypeValues();
        	// JM END
            vecAppointmentTypes = constructAppointmentTypeVector(appointmentTypes);
        } catch (DBException ex) {
            ex.printStackTrace();
        } catch (CoeusException ex) {
            ex.printStackTrace();
        }
        return vecAppointmentTypes;
    }
    
    /**
     * To create the appointment types in to ComboBox values
     * @return Vector vecAppointmentTypes
     * @param HashMap hmData
     */
    public Vector constructAppointmentTypeVector(HashMap hmData){
        Vector vecApptData = new Vector();
        Set<Map.Entry<String, String>> setData = hmData.entrySet();
        for(Map.Entry<String,String> mapData : setData){
            edu.mit.coeuslite.utils.ComboBoxBean cmbBean = new edu.mit.coeuslite.utils.ComboBoxBean();
            cmbBean.setCode(mapData.getKey());
            cmbBean.setDescription(mapData.getKey());
            vecApptData.add(cmbBean);
        }
        return vecApptData;
    }
    //COEUSQA-2036 Code Table Prop Dev Appt Type and Period Types - End
    
    /*
     * Preparing all the valid persons,multiple appointments and sync persons coeusVector into single vector
     * @param cvValidPersons
     * @param cvSelectedMultipleAppointments
     * @param cvSyncPersons
     * @throws Exception
     * @return CoeusVector
     */
    public CoeusVector getAllBudgetSyncPersons(CoeusVector cvValidPersons, CoeusVector cvSelectedMultipleAppointments, CoeusVector cvSyncPersons) throws Exception{
        CoeusVector propSyncPersons = new CoeusVector();
        if(cvValidPersons !=null && cvValidPersons.size()>0 ){
            propSyncPersons.addAll(cvValidPersons);
        }
        if(cvSelectedMultipleAppointments !=null && cvSelectedMultipleAppointments.size()>0){
            propSyncPersons.addAll(cvSelectedMultipleAppointments);
        }
        if(cvSyncPersons !=null && cvSyncPersons.size()>0){
            propSyncPersons.addAll(cvSyncPersons);
        }
        return propSyncPersons;
    }
    /**
     * Method to get current timestamp
     * @throws Exception if exception occur
     * @return current timestamp
     */
    public Timestamp prepareTimeStamp() throws Exception{
        Timestamp dbTimestamp = null;
        CoeusFunctions coeusFunctions = new CoeusFunctions();
        dbTimestamp = coeusFunctions.getDBTimestamp();
        return dbTimestamp;
    }
    
    /**
     * Method to Update Budget_Persons in osp$budget_persons
     * @param request request for to update budget persons
     * @param CoeusVector contains all the persons information.
     * @throws Exception if any error occurs
     */
    public void updateBudgetPersons( HttpServletRequest request ,
    CoeusVector cvSyncPersons) throws Exception{
        WebTxnBean webTxnBean = new WebTxnBean();
        BudgetPersonsBean budgetPersonsBean = null;
        if(cvSyncPersons!=null && cvSyncPersons.size()>0){
            for(int index=0; index<cvSyncPersons.size(); index++ ){
                budgetPersonsBean = new BudgetPersonsBean();
                budgetPersonsBean = (BudgetPersonsBean) cvSyncPersons.get(index);
                HashMap hmUpdBudgetPersons = new HashMap();
                hmUpdBudgetPersons.put("proposalNumber", budgetPersonsBean.getProposalNumber());
                hmUpdBudgetPersons.put("versionNumber", new Integer(budgetPersonsBean.getVersionNumber()));
                hmUpdBudgetPersons.put("personId",budgetPersonsBean.getPersonId());
                hmUpdBudgetPersons.put("jobCode",budgetPersonsBean.getJobCode());
                hmUpdBudgetPersons.put("effectiveDate",budgetPersonsBean.getEffectiveDate() );
                //COEUSQA-1535-Access to institutionally maintained salaries in proposal budget - Start
                //hmUpdBudgetPersons.put("calculationBase", new Double(budgetPersonsBean.getCalculationBase()));
                if(checkViewInstitutionalSalariesRight(request,budgetPersonsBean.getProposalNumber(),budgetPersonsBean.getPersonId())){
                    hmUpdBudgetPersons.put("calculationBase", new Double(budgetPersonsBean.getCalculationBase()));
                }else{
                    hmUpdBudgetPersons.put("calculationBase", new Double(0.00));
                }
                //COEUSQA-1535-Access to institutionally maintained salaries in proposal budget - End
                hmUpdBudgetPersons.put("appointmentType",budgetPersonsBean.getAppointmentType());
                hmUpdBudgetPersons.put("personName",budgetPersonsBean.getFullName());
                hmUpdBudgetPersons.put("nonEmployeeFlag",budgetPersonsBean.isNonEmployee() == true ? "Y" : "N");
                //Added for Case#2918 - Use of Salary Anniversary Date for calculating inflation in budget development module -Start
                   if(budgetPersonsBean.getSalaryAnniversaryDate() != null){
                         hmUpdBudgetPersons.put("salaryAnniversaryDate", budgetPersonsBean.getSalaryAnniversaryDate());
                   }
                //Added for Case#2918 - Use of Salary Anniversary Date for calculating inflation in budget development module - End
                hmUpdBudgetPersons.put("updateTimestamp", budgetPersonsBean.getUpdateTimestamp());
                hmUpdBudgetPersons.put("updateUser",budgetPersonsBean.getUpdateUser());
                hmUpdBudgetPersons.put("awNonEmployeeFlag",budgetPersonsBean.isAw_nonEmployeeFlag() == true ? "Y" : "N");
                hmUpdBudgetPersons.put("acType",budgetPersonsBean.getAcType());
                Hashtable htUpdBudgetPersons =
                (Hashtable)webTxnBean.getResults(request, "updBudgetPersons", hmUpdBudgetPersons);
            }
        }
    }
    
    //COEUSQA-1535-Access to institutionally maintained salaries in proposal budget - Start
    /**
     *
     * For the given ProposalNumber     
     * check user has proposal rates
     * @return boolean value for right
     * @param request for txnBean
     * @param proposalNumber for bean
     * @throws Exception if exception
     */
    protected boolean checkViewInstitutionalSalariesRight(HttpServletRequest request ,
    String proposalNumber, String appointmentPersonId)throws Exception{
        HttpSession session = request.getSession();
        boolean hasRight= false;
        String unitNumber = null;
        HashMap hmProposalHeader = new HashMap(); 
        WebTxnBean webTxnBean = new WebTxnBean();        
        UserMaintDataTxnBean userMaintDataTxnBean = new UserMaintDataTxnBean();
        UserInfoBean userInfoBean = (UserInfoBean)session.getAttribute("user"+session.getId());
        String loggedinUser = userInfoBean.getUserId();        
        Vector vecPersonInfo = new Vector();
        //If the userInfoBean is not null then fetch the person details
        if(appointmentPersonId!=null && appointmentPersonId.trim().length()>0){
            //If the person id is not empty then fetch the person details
            vecPersonInfo = userMaintDataTxnBean.getPersonInfo(appointmentPersonId);
        }
        //To fetch the last value which is home unit
        int decrementor = 1;
        int lastIndex = vecPersonInfo.size()-decrementor;
        //To fetch the home unit
        if(vecPersonInfo!=null && vecPersonInfo.size()>0){
            unitNumber = (String)vecPersonInfo.get(lastIndex);
        }else{
            unitNumber = EMPTY_STRING;
        }
        //Check user has VIEW_PROP_PERSON_INST_SALARIES right at proposal
        hasRight = userMaintDataTxnBean.getUserHasProposalRight(loggedinUser,proposalNumber,"VIEW_PROP_PERSON_INST_SALARIES");
        if(!hasRight){
            //Check user has VIEW_INSTITUTIONAL_SALARIES right at unit level
            hasRight = userMaintDataTxnBean.getUserHasRight(loggedinUser,"VIEW_INSTITUTIONAL_SALARIES",unitNumber);
        }

        return hasRight ;
    }
    //COEUSQA-1535-Access to institutionally maintained salaries in proposal budget - End
}
