/*
 * CoeusDynaFormList.java
 *
 * Created on 11 December 2006, 21:29
 */

package edu.mit.coeuslite.utils;


import java.util.ArrayList;
import java.util.List;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;
import org.apache.struts.action.DynaActionFormClass;
import org.apache.struts.config.FormBeanConfig;
import org.apache.struts.config.ModuleConfig;
import org.apache.struts.util.RequestUtils;
import org.apache.struts.validator.DynaValidatorForm;
import org.apache.struts.validator.ValidatorForm;


public class CoeusDynaFormList extends ValidatorForm{
    private List list;
    private List beanList;
    private List infoList;
    private List dataList;
    private List valueList;
    // Added for COEUSQA-3459 IACUC CoeusLite Procedure Screen Usability - Start
    private List additionalInfoList;
    // Added for COEUSQA-3459 IACUC CoeusLite Procedure Screen Usability - End
    
    /** Creates a new instance of DynaBeanList */
    public CoeusDynaFormList() {
        list = new ArrayList();
        beanList = new ArrayList();
        infoList = new ArrayList();
        dataList = new ArrayList();
        valueList = new ArrayList();
    }
    public void setListIndexed(int index, Object value) {
        list.set(index, value);
    }
    
    public DynaActionForm  getListIndexed(int index) {
        return (DynaActionForm )list.get(index);
    }
    
    /**
     * Getter for property list.
     * @return Value of property list.
     */
    public java.util.List getList() {
        return list;
    }
    
    /**
     * Setter for property list.
     * @param list New value of property list.
     */
    public void setList(java.util.List list) {
        this.list = list;
    }
    public void reset(ActionMapping mapping, HttpServletRequest request) {
        
    }
    
    public DynaActionForm getDynaForm(HttpServletRequest request, String formInstance) throws Exception{
        ServletContext servletContext = request.getSession().getServletContext();
        ModuleConfig moduleConfig = RequestUtils.getModuleConfig(request, servletContext);
        FormBeanConfig formConfig = moduleConfig.findFormBeanConfig(formInstance);
        DynaActionFormClass dynaClass = DynaActionFormClass.createDynaActionFormClass(formConfig);
        DynaActionForm dynaActionForm = (DynaActionForm)dynaClass.newInstance();
        return dynaActionForm;
    }
    
     public DynaActionForm getDynaFormData(int index) {
        return (DynaActionForm)list.get(index);
    }
    
    public DynaActionForm getDynaBeanIndexed(int index) {
        return (DynaActionForm)list.get(index);
    }
     
    public void setDynaFormData(int index, DynaActionForm formdata) {
        list.set(index, formdata);
    }
    
    public void setDynaBeanIndexed(int index, DynaActionForm dynaBean) {
        list.set(index, dynaBean);
    }
    
    /**
     * Getter for property beanList.
     * @return Value of property beanList.
     */
    public java.util.List getBeanList() {
        return beanList;
    }    
   
    /**
     * Setter for property beanList.
     * @param beanList New value of property beanList.
     */
    public void setBeanList(java.util.List beanList) {
        this.beanList = beanList;
    } 
    public void setBeanListIndexed(int index, Object value) { 
        beanList.set(index, value); 
    }
    
    public DynaActionForm getBeanListIndexed(int index) { 
        return (DynaActionForm)beanList.get(index); 
    }
    // start here
      public DynaActionForm getDynaFormBeanIndexed(int index) {
        return (DynaActionForm)beanList.get(index);
    }

    public void setDynaFormBean(int index, DynaActionForm dynaBean) {
        beanList.set(index, dynaBean);

    }
    
    public DynaActionForm getDynaFormBean(int index) {
        return (DynaActionForm )beanList.get(index);
    }

    public void setDynaFormBeanIndexed(int index, DynaActionForm dynaBean) {
        beanList.set(index, dynaBean);

    }

    
    
    /**
     * Getter for property infoList.
     * @return Value of property infoList.
     */
    public java.util.List getInfoList() {
        return infoList;
    }    
   
    /**
     * Setter for property infoList.
     * @param infoList New value of property infoList.
     */
    public void setInfoList(java.util.List infoList) {
        this.infoList = infoList;
    } 
    public void setInfoListIndexed(int index, Object value) { 
        infoList.set(index, value); 
    }
    
    public DynaActionForm getInfoListIndexed(int index) { 
        return (DynaActionForm)infoList.get(index); 
    }
    // start here
      public DynaActionForm getDynaFormInfoIndexed(int index) {
        return (DynaActionForm)infoList.get(index);
    }

    public void setDynaFormInfo(int index, DynaActionForm dynaBean) {
        infoList.set(index, dynaBean);

    }
    
    public DynaActionForm getDynaFormInfo(int index) {
        return (DynaActionForm )infoList.get(index);
    }

    public void setDynaFormInfoIndexed(int index, DynaActionForm dynaBean) {
        infoList.set(index, dynaBean);

    }

    
    /**
     * Getter for property dataList.
     * @return Value of property dataList.
     */
    public java.util.List getDataList() {
        return dataList;
    }    
   
    /**
     * Setter for property dataList.
     * @param dataList New value of property dataList.
     */
    
    public void setDataList(java.util.List dataList) {
        this.dataList = dataList;
    } 
    public void setDataListIndexed(int index, Object value) { 
        dataList.set(index, value); 
    }
    
    public DynaActionForm getDataListIndexed(int index) { 
        return (DynaActionForm)dataList.get(index); 
    }
    // start here
      public DynaActionForm getDynaFormDataIndexed(int index) {
        return (DynaActionForm)dataList.get(index);
    }

    public void setDynaFormDataList(int index, DynaActionForm dynaBean) {
        dataList.set(index, dynaBean);
    }
    
    public DynaActionForm getDynaFormDataList(int index) {
        return (DynaActionForm )dataList.get(index);
    }

    public void setDynaFormDataIndexed(int index, DynaActionForm dynaBean) {
        dataList.set(index, dynaBean);
    }  

    /**
     * Getter for property valueList.
     * @return Value of property valueList.
     */
    public List getValueList() {
        return valueList;
    }

    /**
     * Setter for property valueList.
     * @param valueList New value of property valueList.
     */
    public void setValueList(List valueList) {
        this.valueList = valueList;
    }
    
    /* 
     * Setter for valueList index
     * @param index 
     * @param value 
     */
       public void setValueListIndexed(int index, Object value) { 
        valueList.set(index, value); 
    }
    
    /* 
     * Getter for valueList index
     * @param index 
     * @param value 
     */
    public DynaActionForm getValueListIndexed(int index) { 
        return (DynaActionForm)valueList.get(index); 
    }
    
    /* 
     * Getter for DynaFormValue index
     * @param index 
     */
      public DynaActionForm getDynaFormValueIndexed(int index) {
        return (DynaActionForm)valueList.get(index);
    }
    
    /* 
     * Setter for DynaFormValue index
     * @param index 
     * @param dynaBean 
     */  
    public void setDynaFormValueList(int index, DynaActionForm dynaBean) {
        valueList.set(index, dynaBean);
    }
    
    /* 
     * Getter for DynaFormValueList index
     * @param index      
     */
    public DynaActionForm getDynaFormValueList(int index) {
        return (DynaActionForm )valueList.get(index);
    }
    
    /* 
     * Setter for DynaFormValueList index
     * @param index 
     * @param dynaBean 
     */
    public void setDynaFormValueListIndexed(int index, DynaActionForm dynaBean) {
        valueList.set(index, dynaBean);
    }      
    
    // Added for COEUSQA-3459 IACUC CoeusLite Procedure Screen Usability - Start
    /**
     * Method to get additional info details
     * @return 
     */
    public List getAdditionalInfoList() {
        return additionalInfoList;
    }

    /**
     * Method to set additional info details
     * @param additionInfoList 
     */
    public void setAdditionalInfoList(List additionInfoList) {
        this.additionalInfoList = additionInfoList;
    }
    // Added for COEUSQA-3459 IACUC CoeusLite Procedure Screen Usability - End
}
