/*
 * ProtocolSubmissionDynaBeanList.java
 *
 * Created on 31 August 2006, 10:47
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

/**
 *
 * @author  mohann
 */
public class ProtocolSubmissionDynaBeanList extends ValidatorForm{
    private List list;
    private List beanList;
    private List scheduleList;
    private List reviewerList;
    private List checkList;
    /** Creates a new instance of DynaBeanList */
    public ProtocolSubmissionDynaBeanList() {
        list = new ArrayList();
        beanList = new ArrayList();
        scheduleList = new ArrayList();
        reviewerList = new ArrayList();
        checkList = new ArrayList();
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
     * Getter for property scheduleList.
     * @return Value of property scheduleList.
     */
    public java.util.List getScheduleList() {
        return scheduleList;
    }    
    
     /**
     * Setter for property beanList.
     * @param beanList New value of property beanList.
     */
    public void setScheduleList(java.util.List scheduleList) {
        this.scheduleList = scheduleList;
    } 
    public void setScheduleListIndexed(int index, Object value) { 
        scheduleList.set(index, value); 
    }
    
    public DynaActionForm getScheduleListIndexed(int index) { 
        return (DynaActionForm)scheduleList.get(index); 
    }
    // start here
      public DynaActionForm getDynaFormScheduleIndexed(int index) {
        return (DynaActionForm)scheduleList.get(index);
    }

    public void setDynaFormSchedule(int index, DynaActionForm dynaBean) {
        scheduleList.set(index, dynaBean);

    }
    
    public DynaActionForm getDynaFormSchedule(int index) {
        return (DynaActionForm )scheduleList.get(index);
    }

    public void setDynaFormScheduleIndexed(int index, DynaActionForm dynaBean) {
        scheduleList.set(index, dynaBean);

    }
    /**
     * Getter for property reviewerList.
     * @return Value of property reviewerList.
     */
    public java.util.List getReviewerList() {
        return reviewerList;
    }    
    
     /**
     * Setter for property beanList.
     * @param beanList New value of property beanList.
     */
    public void setReviewerList(java.util.List reviewerList) {
        this.reviewerList = reviewerList;
    } 
    public void setReviewerListIndexed(int index, Object value) { 
        reviewerList.set(index, value); 
    }
    
    public DynaActionForm getReviewerListIndexed(int index) { 
        return (DynaActionForm)reviewerList.get(index); 
    }
    // start here
      public DynaActionForm getDynaFormReviewerIndexed(int index) {
        return (DynaActionForm)reviewerList.get(index);
    }

    public void setDynaFormReviewer(int index, DynaActionForm dynaBean) {
        reviewerList.set(index, dynaBean);

    }
    
    public DynaActionForm getDynaFormReviewer(int index) {
        return (DynaActionForm )reviewerList.get(index);
    }

    public void setDynaFormReviewerIndexed(int index, DynaActionForm dynaBean) {
        reviewerList.set(index, dynaBean);

    }
    
     /**
     * Getter for property checkList.
     * @return Value of property reviewerList.
     */
    public java.util.List getCheckList() {
        return checkList;
    }    
    
     /**
     * Setter for property beanList.
     * @param beanList New value of property beanList.
     */
    public void setCheckList(java.util.List checkList) {
        this.checkList = checkList;
    } 
    public void setCheckListIndexed(int index, Object value) { 
        checkList.set(index, value); 
    }
    
    public DynaActionForm getCheckListIndexed(int index) { 
        return (DynaActionForm)checkList.get(index); 
    }
    // start here
      public DynaActionForm getDynaFormCheckIndexed(int index) {
        return (DynaActionForm)checkList.get(index);
    }

    public void setDynaFormCheck(int index, DynaActionForm dynaBean) {
        checkList.set(index, dynaBean);

    }
    
    public DynaActionForm getDynaFormCheck(int index) {
        return (DynaActionForm )checkList.get(index);
    }

    public void setDynaFormCheckIndexed(int index, DynaActionForm dynaBean) {
        checkList.set(index, dynaBean);

    }

}
