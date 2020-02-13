 /*
  * ValidationUtil.java
  *
  * Created on June 6, 2006, 4:25 PM
  */

package edu.wmc.coeuslite.utils;


import edu.mit.coeuslite.utils.DateUtils;
import java.io.Serializable;
import java.util.Date;
import java.util.Locale;
import java.util.StringTokenizer;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.validator.Field;
import org.apache.commons.validator.GenericTypeValidator;
import org.apache.commons.validator.GenericValidator;
import org.apache.commons.validator.UrlValidator;
import org.apache.commons.validator.Validator;
import org.apache.commons.validator.ValidatorAction;
import org.apache.commons.validator.ValidatorUtil;
import org.apache.commons.validator.util.ValidatorUtils;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.util.MessageResources;
import org.apache.struts.util.RequestUtils;
import org.apache.struts.validator.DynaValidatorForm;
import org.apache.struts.validator.FieldChecks;
import org.apache.struts.validator.Resources;

/**
 * <p>
 * This class contains the default validations that are used in the
 * validator-rules.xml file.
 * </p>
 * <p>
 * In general passing in a null or blank will return a null Object or a false
 * boolean. However, nulls and blanks do not result in an error being added to the
 * errors.
 * </p>
 *
 * @since Struts 1.1
 */
public class ValidationUtil extends FieldChecks implements Serializable {
    
    /**
     *  Commons Logging instance.
     */
    private static final Log log = LogFactory.getLog(ValidationUtil.class);
    
    public static final String FIELD_TEST_NULL = "NULL";
    public static final String FIELD_TEST_NOTNULL = "NOTNULL";
    public static final String FIELD_TEST_EQUAL = "EQUAL";
    private static final String DATE_SEPARATERS = ":/.,|-";
    private static final String SIMPLE_DATE_FORMAT = "MM/dd/yyyy";
    
   
   
    /**
     *
     * @param  bean     The bean validation is being performed on.
     * @param  va       The <code>ValidatorAction</code> that is currently being performed.
     * @param  field    The <code>Field</code> object associated with the current
     *      field being validated.
     * @param  errors   The <code>ActionMessages</code> object to add errors to if any
     *      validation errors occur.
     * @param validator The <code>Validator</code> instance, used to access
     * other field values.
     * @param  request  Current request object.
     * @return true if valid, false otherwise.
     */
    public static Object validateDate(Object bean,
    ValidatorAction va, Field field,
    ActionMessages errors,
    Validator validator,
    HttpServletRequest request) {
        
        Object result = null;
        String value = null;
        if (isString(bean)) {
            value = (String) bean;
        } else {
            value = ValidatorUtils.getValueAsString(bean, field.getProperty());
        }
        String datePattern = field.getVarValue("datePattern");
        String datePatternStrict = field.getVarValue("datePatternStrict");
        Locale locale = RequestUtils.getUserLocale(request, null);
        
        if (GenericValidator.isBlankOrNull(value)) {
            return Boolean.TRUE;
        }
        
        try {
            if (datePattern != null && datePattern.length() > 0) {
                result = GenericTypeValidator.formatDate(value, datePattern, false);
            } else if (datePatternStrict != null && datePatternStrict.length() > 0) {
                result = GenericTypeValidator.formatDate(value, datePatternStrict, true);
            } else {
                result = GenericTypeValidator.formatDate(value, locale);
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            
        }
        
        if (result == null) {
            errors.add(field.getKey(), Resources.getActionMessage(validator, request, va, field));
        }
        
        return result == null ? Boolean.FALSE : result;
    }
    
    
    /**This method is used to comapre two dates value1 and value2
     *where value2 cannot be later than value1
     */
    public static boolean validateDateValidate(Object bean,
    ValidatorAction va,
    Field field,
    ActionMessages errors,
    Validator validator,
    HttpServletRequest request) {
        String sProperty1 = field.getVarValue("firstProperty");
        String value1 = ValidatorUtil.getValueAsString(bean, sProperty1);
        String value2 = ValidatorUtil.getValueAsString(bean, field.getProperty());
        DateUtils dtUtils = new DateUtils();
        
        if (!GenericValidator.isBlankOrNull(value1) && !GenericValidator.isBlankOrNull(value2) ) {
            try {
                value1 = dtUtils.formatDate(value1,DATE_SEPARATERS,SIMPLE_DATE_FORMAT);
                value2 = dtUtils.formatDate(value2,DATE_SEPARATERS,SIMPLE_DATE_FORMAT);
                java.sql.Date startDate = dtUtils.getSQLDate(value1);
                java.sql.Date endDate = dtUtils.getSQLDate(value2);
                
                if (startDate.after(endDate)) {
                    ActionMessage dataMessage = Resources.getActionMessage(validator, request, va, field);
                    errors.add(field.getKey(), dataMessage);
                    return false;
                }
            } catch (Exception e) {
               log.error(e.getMessage(), e);
                return false;
            }
        }
        
        return true;
    }
   
    /**Method to compare two date field. define 'second property' as var name parameter 
      and var value form property name. Using this method user can validate two dates 
     *in indexed property. 
    **/
    public static boolean validateTwoDateFieldValidate(Object bean,
    ValidatorAction va,
    Field field,
    ActionMessages errors,
    Validator validator,
    HttpServletRequest request) {
        MessageResources messages = MessageResources.getMessageResources("BudgetMessages");
        String msg = null;
        boolean isIndexed = false;
        int index = 0;
        if (field.isIndexed()) {
            isIndexed = true;
            String key = field.getKey();
            
            final int leftBracket = key.indexOf("[");
            final int rightBracket = key.indexOf("]");
            
            if ((leftBracket > -1) && (rightBracket > -1)) {
                index = Integer.parseInt(key.substring(leftBracket + 1, rightBracket));
            }
        }
        String value1 = ValidatorUtil.getValueAsString(bean, field.getProperty());
        String sProperty2 = field.getVarValue("secondProperty");
        String value2 = ValidatorUtil.getValueAsString(bean, sProperty2);
        DateUtils dtUtils = new DateUtils();
        
        if (GenericValidator.isBlankOrNull(value1)) {
            msg = messages.getMessage("adjustPeriod_exceptionCode.1459");
            if(isIndexed){
                msg = messages.getMessage("adjustPeriod_exceptionCode.1452", new Integer(index + 1));
            }
            msg = "<font color='red'>"+msg+"</font>";
            errors.add(field.getKey(), new ActionMessage(msg, false));
            return false;
        }
        if (GenericValidator.isBlankOrNull(value2)) {
            msg = messages.getMessage("adjustPeriod_exceptionCode.1460");
            if(isIndexed){
                msg = messages.getMessage("adjustPeriod_exceptionCode.1453", new Integer(index + 1));
            }
            msg = "<font color='red'>"+msg+"</font>";
            errors.add(field.getKey(), new ActionMessage(msg, false));
            return false;
        }
        if (!GenericValidator.isBlankOrNull(value1) && !GenericValidator.isBlankOrNull(value2) ) {
            try {
                
                
                value1 = dtUtils.formatDate(value1,DATE_SEPARATERS,SIMPLE_DATE_FORMAT);
                
                if(value1 == null){
                    msg = messages.getMessage("adjustPeriod_exceptionCode.1463", new Integer(index + 1));
                    if(isIndexed){
                        msg = messages.getMessage("adjustPeriod_exceptionCode.1461", new Integer(index + 1));
                    }
                    msg = "<font color='red'>"+msg+"</font>";
                    errors.add(field.getKey(), new ActionMessage(msg, false));
                    return false;
                }
                java.sql.Date startDate = dtUtils.getSQLDate(value1);
                value2 = dtUtils.formatDate(value2,DATE_SEPARATERS,SIMPLE_DATE_FORMAT);
                
                if(value2 == null){
                    msg = messages.getMessage("adjustPeriod_exceptionCode.1464", new Integer(index + 1));
                    if(isIndexed){
                        msg = messages.getMessage("adjustPeriod_exceptionCode.1462", new Integer(index + 1));
                    }
                    msg = "<font color='red'>"+msg+"</font>";
                    errors.add(field.getKey(), new ActionMessage(msg, false));
                    return false;
                }
                java.sql.Date endDate = dtUtils.getSQLDate(value2);
                if (startDate.after(endDate)) {
                    msg = messages.getMessage("adjustPeriod_exceptionCode.1454");
                    msg = "<font color='red'>"+msg+"</font>";
                    errors.add(field.getKey(), new ActionMessage(msg, false));
                    return false;
                }
                
            } catch (Exception e) {
               log.error(e.getMessage(), e);
                return false;
            }
        }
        
        return true;
    }

    public static boolean validDatePattern(Object bean,
    ValidatorAction va,
    Field field,
    ActionMessages errors,
    Validator validator,
    HttpServletRequest request) {
        String value1 = ValidatorUtil.getValueAsString(bean, field.getProperty());
        DateUtils dtUtils = new DateUtils();
        if (!GenericValidator.isBlankOrNull(value1)) {
            try {
                String dateResult = dtUtils.formatDate(value1,DATE_SEPARATERS,SIMPLE_DATE_FORMAT);
                if (dateResult==null) {
                    ActionMessage dataMessage = Resources.getActionMessage(validator, request, va, field);
                    errors.add(field.getKey(), dataMessage);
                    return false;
                }
            } catch (Exception e) {
                errors.add(field.getKey(), Resources.getActionMessage(validator, request, va, field));
                return false;
            }
        }
        
        return true;
    }
    /**
     *Method to validate Data for Line Item using indexed property.
     **/
    public static boolean validateBudgetLineItems(Object bean,
    ValidatorAction va,
    Field field,
    ActionMessages errors,
    Validator validator,
    HttpServletRequest request) {
        MessageResources messages = MessageResources.getMessageResources("BudgetMessages");
        String msg = null;
        boolean isIndexed = false;
        int index = 0;
        if (field.isIndexed()) {
            isIndexed = true;
            String key = field.getKey();
            
            final int leftBracket = key.indexOf("[");
            final int rightBracket = key.indexOf("]");
            
            if ((leftBracket > -1) && (rightBracket > -1)) {
                index = Integer.parseInt(key.substring(leftBracket + 1, rightBracket));
            }
        }
        String value1 = ValidatorUtil.getValueAsString(bean, field.getProperty());
        String sProperty2 = field.getVarValue("secondProperty");
        String value2 = ValidatorUtil.getValueAsString(bean, sProperty2);
        
        if (GenericValidator.isBlankOrNull(value1)) {
            if(isIndexed){
                msg = messages.getMessage("budget_period_exceptionCode.2000", new Integer(index + 1));
            }
            msg = "<font color='red'>"+msg+"</font>";
            errors.add(field.getKey(), new ActionMessage(msg, false));
            return false;
        }
        if (!GenericValidator.isBlankOrNull(value2)) {
            value2 = value2.replaceAll("[$,/,]","");
            if(isIndexed){
                msg = messages.getMessage("budget_period_exceptionCode.2002", new Integer(index + 1));
            }
            msg = "<font color='red'>"+msg+"</font>";
            Object result = GenericTypeValidator.formatDouble(value2);

            if (result == null) {
                errors.add(field.getKey(), new ActionMessage(msg, false));
                return false;
            }
           
        }
       
        
        int pos = value2.indexOf(".");
        if(pos != -1){
            value2 =  value2.substring(0,pos);
        }
        //Added for Case #3132 - start
        //Changing quantity field from integer to float
        int length = 10;
        String errorMessage = "error.lineItemDetails.lineItemCost.currencyMaxlength";
        if(field.getProperty().equals("quantity")) {
            length = 4;
            errorMessage = "error.lineItemDetails.quantity.quantityMaxlength";
        }
        //Added for Case #3132 - end
//        if(value2.length() > 10){ //Commented for Case #3132 Changing quantity field from integer to float
        if(value2.length() > length){ 
            //Commented for Case #3132 Changing quantity field from integer to float
//            msg = messages.getMessage("error.lineItemDetails.lineItemCost.currencyMaxlength");
            msg = messages.getMessage(errorMessage);
            msg = "<font color='red'>"+msg+"</font>";
            errors.add(field.getKey(), new ActionMessage(msg, false));
            return false ;
        }
         return true;
    }
    // this method checks the maximum length for the currency fields. for a max value of 10
    public static boolean validateCurrencyMaxLength(Object bean,
    ValidatorAction va, Field field,
    ActionMessages errors,
    Validator validator,
    HttpServletRequest request) {       
        String value = null;
        
        if (isString(bean)) {
            value = (String) bean;
        } else {
            value = ValidatorUtils.getValueAsString(bean, field.getProperty());
            String temp = value.replaceAll("[$,/,]","");
            int pos = temp.indexOf(".");
            if(pos!=-1){
                temp =  temp.substring(0,pos);               
            }
            if(temp.length()>10){
                    errors.add(field.getKey(), Resources.getActionMessage(validator, request, va, field));
                    return false ;
            }
        }
        return true ;
    }
    //method to validate Budget Modular Direct Costs
    public static boolean validateBudgetDCModular(Object bean,
    ValidatorAction va, Field field,
    ActionMessages errors,
    Validator validator,
    HttpServletRequest request) {
        MessageResources messages = MessageResources.getMessageResources("BudgetMessages");
        String msg = null;
        String strTempCost = "";
        String value1 = ValidatorUtil.getValueAsString(bean, field.getProperty());
        String sProperty2 = field.getVarValue("secondProperty");
        String value2 = ValidatorUtil.getValueAsString(bean, sProperty2);
       
        if (!GenericValidator.isBlankOrNull(value1) && !GenericValidator.isBlankOrNull(value2) ) {
            value1 = value1.replaceAll("[$,/,]","");
            
            Object result = GenericTypeValidator.formatDouble(value1);
            if (result == null) {
                msg = messages.getMessage("budgetModCumulative.invalidDCLess");
                msg = "<font color='red'>"+msg+"</font>";
                errors.add(field.getKey(), new ActionMessage(msg, false));
                return false;
            }
            int pos = value1.indexOf(".");
            if(pos!=-1){
                strTempCost =  value1.substring(0,pos);               
            }else{
                strTempCost = value1;
            }
            if(strTempCost.length() > 10 ){
                msg = messages.getMessage("budgetModCumulative.maxDCLess");
                msg = "<font color='red'>"+msg+"</font>";
                errors.add(field.getKey(), new ActionMessage(msg, false));
                return false;
            }
            value2 = value2.replaceAll("[$,/,]","");
            result = GenericTypeValidator.formatDouble(value2);
            if (result == null) {
                msg = messages.getMessage("budgetModCumulative.invalidConsFNA");
                msg = "<font color='red'>"+msg+"</font>";
                errors.add(field.getKey(), new ActionMessage(msg, false));
                return false;
            }
            pos = value2.indexOf(".");
            if(pos!=-1){
                strTempCost =  value2.substring(0,pos);               
            }else{
                strTempCost = value2;
            }
            if(strTempCost.length() > 10 ){
                msg = messages.getMessage("budgetModCumulative.maxConsFNA");
                msg = "<font color='red'>"+msg+"</font>";
                errors.add(field.getKey(), new ActionMessage(msg, false));
                return false;
            }
            double totalDC = 0 + Double.parseDouble(value1) + Double.parseDouble(value2);
            //Resolve the big decimal problem(E.g 1.111E7)
            strTempCost = java.text.NumberFormat.getCurrencyInstance(java.util.Locale.US).format(new Double(totalDC));
            strTempCost = strTempCost.replaceAll("[$,/,]","");
            pos = strTempCost.indexOf(".");
            if(pos!=-1){
                strTempCost =  strTempCost.substring(0,pos);               
            }
            if(strTempCost.length() > 10){
                msg = messages.getMessage("budgetModCumulative.maxTotalDC");
                msg = "<font color='red'>"+msg+"</font>";
                errors.add(field.getKey(), new ActionMessage(msg, false));
                return false;
            }
           
        }
        return true;
    }
    //Method to validate Budget Modular Indirect Costs
    public static boolean validateBudgetModularIDC(Object bean,
    ValidatorAction va, Field field,
    ActionMessages errors,
    Validator validator,
    HttpServletRequest request) {
        MessageResources messages = MessageResources.getMessageResources("BudgetMessages");
        String msg = null;
        String value1 = ValidatorUtil.getValueAsString(bean, field.getProperty());
        String sProperty2 = field.getVarValue("secondProperty");
        String value2 = ValidatorUtil.getValueAsString(bean, sProperty2);
        sProperty2 = field.getVarValue("thirdProperty");
        String value3 = ValidatorUtil.getValueAsString(bean, sProperty2);
        int index = 0;
        if (field.isIndexed()) {
            String key = field.getKey();
            
            final int leftBracket = key.indexOf("[");
            final int rightBracket = key.indexOf("]");
            
            if ((leftBracket > -1) && (rightBracket > -1)) {
                index = Integer.parseInt(key.substring(leftBracket + 1, rightBracket));
            }
        }
       
        if (!GenericValidator.isBlankOrNull(value1) && !GenericValidator.isBlankOrNull(value2) 
            && !GenericValidator.isBlankOrNull(value3)) {
            
            Object result = GenericTypeValidator.formatDouble(value1);
            if (result == null) {
                msg = messages.getMessage("budgetModCumulative.invalidIDCRate",new Integer(index + 1));
                msg = "<font color='red'>"+msg+"</font>";
                errors.add(field.getKey(), new ActionMessage(msg, false));
                return false;
            }
            int pos = value1.indexOf(".");
            String strTempCost = "";
            pos = value1.indexOf(".");
            if(pos!=-1){
                strTempCost =  value1.substring(0,pos);               
            }else{
                strTempCost = value1;
            }
            if(strTempCost.length() > 3){
                msg = messages.getMessage("budgetModCumulative.maxIDCRate",new Integer(index + 1));
                msg = "<font color='red'>"+msg+"</font>";
                errors.add(field.getKey(), new ActionMessage(msg, false));
                return false;
            }
            
            value2 = value2.replaceAll("[$,/,]","");
            
            result = GenericTypeValidator.formatDouble(value2);
            if (result == null) {
                msg = messages.getMessage("budgetModCumulative.invalidIDCBase",new Integer(index + 1));
                msg = "<font color='red'>"+msg+"</font>";
                errors.add(field.getKey(), new ActionMessage(msg, false));
                return false;
            }
            pos = value2.indexOf(".");
            if(pos!=-1){
                strTempCost =  value2.substring(0,pos);               
            }else{
                strTempCost = value2;
            }
            if(strTempCost.length() > 10 ){
                msg = messages.getMessage("budgetModCumulative.maxIDCBase", new Integer(index + 1));
                msg = "<font color='red'>"+msg+"</font>";
                errors.add(field.getKey(), new ActionMessage(msg, false));
                return false;
            }
            value3 = value3.replaceAll("[$,/,]","");
            result = GenericTypeValidator.formatDouble(value3);
            if (result == null) {
                msg = messages.getMessage("budgetModCumulative.invalidFundRequested", new Integer(index + 1));
                msg = "<font color='red'>"+msg+"</font>";
                errors.add(field.getKey(), new ActionMessage(msg, false));
                return false;
            }
            pos = value3.indexOf(".");
            if(pos!=-1){
                strTempCost =  value3.substring(0,pos);               
            }else{
                strTempCost = value3;
            }
            if(strTempCost.length() > 10 ){
                msg = messages.getMessage("budgetModCumulative.maxFundRequest",new Integer(index + 1));
                msg = "<font color='red'>"+msg+"</font>";
                errors.add(field.getKey(), new ActionMessage(msg, false));
                return false;
            }
        }
        return true;
    }
    
    public static boolean validateProjectIncomeData(Object bean,
    ValidatorAction va, Field field,
    ActionMessages errors,
    Validator validator,
    HttpServletRequest request) {
        MessageResources messages = MessageResources.getMessageResources("BudgetMessages");
        String msg = null;
        String value1 = ValidatorUtil.getValueAsString(bean, field.getProperty());
        String sProperty2 = field.getVarValue("secondProperty");
        String value2 = ValidatorUtil.getValueAsString(bean, sProperty2);
        int index = 0;
        if (field.isIndexed()) {
            String key = field.getKey();
            
            final int leftBracket = key.indexOf("[");
            final int rightBracket = key.indexOf("]");
            
            if ((leftBracket > -1) && (rightBracket > -1)) {
                index = Integer.parseInt(key.substring(leftBracket + 1, rightBracket));
            }
        }
        if(GenericValidator.isBlankOrNull(value1)){
            msg = messages.getMessage("projectIncome.income.required",new Integer(index + 1));
            msg = "<font color='red'>"+msg+"</font>";
            errors.add(field.getKey(), new ActionMessage(msg, false));
            return false;
        }
        if(GenericValidator.isBlankOrNull(value2)){
            msg = messages.getMessage("projectIncome.description.required",new Integer(index + 1));
            msg = "<font color='red'>"+msg+"</font>";
            errors.add(field.getKey(), new ActionMessage(msg, false));
            return false;
        }
        
        value1 = value1.replaceAll("[$,/,]","");
        Object result = GenericTypeValidator.formatDouble(value1);
        if (result == null) {
            msg = messages.getMessage("projectIncome.invalidIncome",new Integer(index + 1));
            msg = "<font color='red'>"+msg+"</font>";
            errors.add(field.getKey(), new ActionMessage(msg, false));
            return false;
        }
        double amount = Double.parseDouble(value1);
        if(amount <= 0){
            msg = messages.getMessage("projectIncome.invalidIncome",new Integer(index + 1));
            msg = "<font color='red'>"+msg+"</font>";
            errors.add(field.getKey(), new ActionMessage(msg, false));
            return false;
        }
        int pos = value1.indexOf(".");
        String strTempAmt = "";
        pos = value1.indexOf(".");
        if(pos!=-1){
            strTempAmt =  value1.substring(0,pos);               
        }else{
            strTempAmt = value1;
        }
        if(strTempAmt.length() > 10 ){
            msg = messages.getMessage("projectIncome.maxAmount",new Integer(index + 1));
            msg = "<font color='red'>"+msg+"</font>";
            errors.add(field.getKey(), new ActionMessage(msg, false));
            return false;
        }
        return true;
     }
    
    /**This method is used to comapre percent effort and percent charged
     * where percent effort should be greater than or equal to percent charged.
     */
    public static boolean validatePercentEffort(Object bean,
    ValidatorAction va,
    Field field,
    ActionMessages errors,
    Validator validator,
    HttpServletRequest request) {
        MessageResources messages = MessageResources.getMessageResources("BudgetMessages");
        String sProperty1 = field.getVarValue("validateField");
        String percentCharged = ValidatorUtil.getValueAsString(bean, sProperty1);
        String percentEffort = ValidatorUtil.getValueAsString(bean, field.getProperty());
        String msg = null;
        boolean isIndexed = false;
        int index = 0;
        if (!GenericValidator.isBlankOrNull(percentCharged) && !GenericValidator.isBlankOrNull(percentEffort) ) {
            try {
                double perEffort = Double.parseDouble(percentEffort);
                double perCharged = Double.parseDouble(percentCharged); 
                if (perCharged > perEffort) {
                    msg = messages.getMessage("budgetPersonnel_exceptionCode.1000");
                    ActionMessage dataMessage = Resources.getActionMessage(validator, request, va, field);
                    errors.add(field.getKey(), new ActionMessage(msg, false));
                    return false;
                }
            } catch (Exception e) {
               log.error(e.getMessage(), e);
                return false;
            }
        }
        
        return true;
    }
    
    public static boolean validateAnswers(Object bean,
    ValidatorAction va,
    Field field,
    ActionMessages errors,
    Validator validator,
    HttpServletRequest request) {
        MessageResources messages = MessageResources.getMessageResources("ProposalMessages");
        String answer = ValidatorUtil.getValueAsString(bean, field.getProperty());
        String property = field.getVarValue("firstProperty");
        String question = ValidatorUtil.getValueAsString(bean, property);
        property = field.getVarValue("secondProperty");
        String questionId = ValidatorUtil.getValueAsString(bean, property);
        property = field.getVarValue("thirdProperty");
        String requiredDate = ValidatorUtil.getValueAsString(bean, property);        
        String msg = null;
        DateUtils dtUtils = new DateUtils();
        // Commmented for YNQ Coeus4.3 Enhancement,( all questions to be answered before submitting 
        // for approval but when in YNQ page, allow only individual Questions to be answered)
        // Uncommented for validation proposal investigator certify questions
        if (GenericValidator.isBlankOrNull(answer)) {
            try {
                    msg = messages.getMessage("error.ynq.answerRequired");
                    msg += " "+question;
                    ActionMessage dataMessage = Resources.getActionMessage(validator, request, va, field);
                    errors.add(field.getKey(), new ActionMessage(msg, false));
                    return false;                
            } catch (Exception e) {
                log.error(e.getMessage(), e);
                return false;
            }
        } else if(!GenericValidator.isBlankOrNull(requiredDate)) {
            try {
                String dateResult = dtUtils.formatDate(requiredDate, DATE_SEPARATERS, SIMPLE_DATE_FORMAT);
                if (dateResult==null) {
                    msg = messages.getMessage("error.ynq.requiredDateField");
                    msg += " "+questionId;
                    ActionMessage dataMessage = Resources.getActionMessage(validator, request, va, field);
                    errors.add(field.getKey(), new ActionMessage(msg, false));
                    return false;
                }
            } catch (Exception e) {
                errors.add(field.getKey(), Resources.getActionMessage(validator, request, va, field));
                return false;
            }            
        }
        return true;
    }
    
     /**This method is used to compare two dates startDate and deadLineDate
     *where deadLineDate cannot be later than startDate
     */
    public static boolean validateDeadlineDate(Object bean,
    ValidatorAction va,
    Field field,
    ActionMessages errors,
    Validator validator,
    HttpServletRequest request) {
        String sProperty1 = field.getVarValue("firstProperty");
        String value1 = ValidatorUtil.getValueAsString(bean, sProperty1);
        String value2 = ValidatorUtil.getValueAsString(bean, field.getProperty());
        DateUtils dtUtils = new DateUtils();
        
        if (!GenericValidator.isBlankOrNull(value1) && !GenericValidator.isBlankOrNull(value2) ) {
            try {
                value1 = dtUtils.formatDate(value1,DATE_SEPARATERS,SIMPLE_DATE_FORMAT);
                value2 = dtUtils.formatDate(value2,DATE_SEPARATERS,SIMPLE_DATE_FORMAT);
                java.sql.Date startDate = dtUtils.getSQLDate(value1);
                java.sql.Date deadlineDate = dtUtils.getSQLDate(value2);
                int result = startDate.compareTo(deadlineDate);
                if (result < 0) {
                    ActionMessage dataMessage = Resources.getActionMessage(validator, request, va, field);
                    errors.add(field.getKey(), dataMessage);
                    return false;
                }
            } catch (Exception e) {
               log.error(e.getMessage(), e);
                return false;
            }
        }
        
        return true;
    }
    
    //Added for Case #3132 Changing the quantity field from int to float
    /**
     * Is used to check the maximum length for the quantity field for a max value of 4
     * @param bean Object
     * @param validatorAction ValidatorAction
     * @param field Field
     * @param errors ActionMessages
     * @param validator Validator
     * @param request HttpServletRequest
     * @return boolean
     */
    public static boolean validateQuantityMaxLength(Object bean,
    ValidatorAction validatorAction, Field field,
    ActionMessages errors,
    Validator validator,
    HttpServletRequest request) {       
        String value = null;
        
        if (isString(bean)) {
            value = (String) bean;
        } else {
            value = ValidatorUtils.getValueAsString(bean, field.getProperty());
            String temp = value.replaceAll("[$,/,]","");
            int pos = temp.indexOf(".");
            if(pos!=-1){
                temp =  temp.substring(0,pos);               
            }
            if(temp.length()>4){
                errors.add(field.getKey(), Resources.getActionMessage(validator, request, validatorAction, field));
                return false ;
            }
        }
        return true ;
    }

    /**
     * Validation rule for amount fields in Arra module.
     *
     */
    
    public static boolean validateArraAmount(Object bean,
    ValidatorAction va, Field field,
    ActionMessages errors,
    Validator validator,
    HttpServletRequest request) {
        MessageResources arra = MessageResources.getMessageResources("ArraMessages");
        String msg = null;
        String strTmpAmt = "";
        String fieldName = field.getProperty();
        String fieldvalue = ValidatorUtil.getValueAsString(bean, field.getProperty());
        String invalidArraAmount = "arra.error.invalid.";
        String amountLimitExceeded = "arra.error.maxLimit.";
        
        if (!GenericValidator.isBlankOrNull(fieldvalue)  ) {
       
            fieldvalue = fieldvalue.replaceAll("[$,/,]","");
            
            Object result = GenericTypeValidator.formatDouble(fieldvalue);
            if (result == null) {
                msg = arra.getMessage(invalidArraAmount + fieldName);
                msg = "<font color='red'>"+msg+"</font>";
                errors.add(field.getKey(), new ActionMessage(msg, false));
                return false;
            }
            int pos = fieldvalue.indexOf(".");
            if(pos!=-1){
                strTmpAmt =  fieldvalue.substring(0,pos);               
            }else{
                strTmpAmt = fieldvalue;
            }
            if(strTmpAmt.length() > 10 ){
                msg = arra.getMessage(amountLimitExceeded + fieldName);
                msg = "<font color='red'>"+msg+"</font>";
                errors.add(field.getKey(), new ActionMessage(msg, false));
                return false;
            }

           
        }
        return true;
    }
    
    /**
     * Validation rule for positive double value for no of Jobs field in Arra module.
     *
     */
    
    public static boolean validateNoOfJobs(Object bean,
    ValidatorAction va, Field field,
    ActionMessages errors,
    Validator validator,
    HttpServletRequest request) {
        MessageResources arra = MessageResources.getMessageResources("ArraMessages");
        String msg = null;
        String strTmpAmt = "";
        String fieldName = field.getProperty();
        String fieldvalue = ValidatorUtil.getValueAsString(bean, field.getProperty());
        String invalidArraAmount = "arra.error.invalid.";
        String amountLimitExceeded = "arra.error.maxLimit.";
        
        if (!GenericValidator.isBlankOrNull(fieldvalue)  ) {
       
            fieldvalue = fieldvalue.replaceAll("[,]","");
            fieldvalue = fieldvalue.replaceAll("[$]","");         
            Object result = GenericTypeValidator.formatDouble(fieldvalue);
            if (result == null || fieldvalue.indexOf("-")!= -1 ) {
                msg = arra.getMessage(invalidArraAmount + fieldName);
                msg = "<font color='red'>"+msg+"</font>";
                errors.add(field.getKey(), new ActionMessage(msg, false));
                return false;
            }
            int pos = fieldvalue.indexOf(".");
            if(pos!=-1){
                strTmpAmt =  fieldvalue.substring(0,pos);               
            }else{
                strTmpAmt = fieldvalue;
            }
            if(strTmpAmt.length() > 10 ){
                msg = arra.getMessage(amountLimitExceeded + fieldName);
                msg = "<font color='red'>"+msg+"</font>";
                errors.add(field.getKey(), new ActionMessage(msg, false));
                return false;
            }

           
        }
        return true;
    }
}

