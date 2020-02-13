/*
 * DynaBeanList.java
 *
 * Created on June 6, 2006, 11:43 AM
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
 * @author  Chandrashekhar
 */
public class CoeusDynaBeansList extends ValidatorForm{
    private List list;
    private List beanList;
    /** Creates a new instance of DynaBeanList */
    public CoeusDynaBeansList() {
        list = new ArrayList();
        beanList = new ArrayList();
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
        //COEUSDEV-848 - START
        if(list != null && !list.isEmpty()){
            DynaActionForm actionForm;
            for(int index=0; index <list.size(); index++){
                if(list.get(index) instanceof DynaActionForm){
                    actionForm = (DynaActionForm)list.get(index);
                    if(actionForm.getDynaClass().getName().equalsIgnoreCase("budgetSummary")){
                        //actionForm.reset(mapping, request); default implementation of reset does nothing
                        //reset checkbox values to N
                        actionForm.set("finalVersionFlag", "N");
                        actionForm.set("modularBudgetFlag", "N");
                        actionForm.set("submitCostSharingFlag", "N");
                    }
                }
            }
        }
        //COEUSDEV-848 - END
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


    
     

}
