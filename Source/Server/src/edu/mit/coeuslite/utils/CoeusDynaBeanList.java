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
 * @author  chandrashekara
 */
public class CoeusDynaBeanList extends ValidatorForm{
    private List list;
    private DynaActionForm dynaForm;
    /** Creates a new instance of DynaBeanList */
    public CoeusDynaBeanList() {
        list = new ArrayList();
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
    
    public DynaActionForm getDynaForm(HttpServletRequest request) throws Exception{
        ServletContext servletContext = request.getSession().getServletContext();
        ModuleConfig moduleConfig = RequestUtils.getModuleConfig(request, servletContext);
        FormBeanConfig formConfig = moduleConfig.findFormBeanConfig("adjustPeriodBoundary");
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
    
   

}
