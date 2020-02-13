/*
 * DynaBeanList.java
 *
 * Created on June 1, 2006, 11:10 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package edu.wmc.coeuslite.utils;

import edu.mit.coeuslite.utils.*;
import java.util.*;
import javax.servlet.*;
import javax.servlet.http.*;
import org.apache.struts.action.*;
import org.apache.struts.config.*;
import org.apache.struts.util.*;
import org.apache.struts.validator.*;

/**
 *
 * @author sharathk
 */
public class DynaBeanList extends ValidatorForm{
    private String listFormName;
    private String beanListFormName;
    
    private CoeusWebList list;
    private CoeusWebList beanList;
    
    /** Creates a new instance of DynaBeanList */
    public DynaBeanList() {
        list = new CoeusWebList();
        beanList = new CoeusWebList();
    }
    
    public CoeusWebList getList() {
        return list;
    }

    public void setList(CoeusWebList list) {
        this.list = (CoeusWebList)list;
    }
    
    public void setListIndexed(int index, Object value) { 
        list.set(index, value); 
    }
    
    public DynaActionForm getListIndexed(int index) { 
        return (DynaActionForm)list.get(index); 
    }
    
    //
    public DynaActionForm getListBean(int index) {
        return (DynaActionForm)list.get(index);
    }
    
    public DynaActionForm getListBeanIndexed(int index) {
        return (DynaActionForm)list.get(index);
    }
    
    public void setListBean(int index, DynaActionForm testBean) {
        list.set(index, testBean);
    }
    
    public void setListBeanIndexed(int index, DynaActionForm testBean) {
        list.set(index, testBean);
    }
    //------------------------------------------------------------------
    
     public CoeusWebList getBeanList() {
        return beanList;
    }

    public void setBeanList(CoeusWebList beanList) {
        this.beanList = (CoeusWebList)beanList;
    }
    
    public void setBeanListIndexed(int index, Object value) { 
        beanList.set(index, value); 
    }
    
    public DynaActionForm getBeanListIndexed(int index) { 
        return (DynaActionForm)beanList.get(index); 
    }
    
    //
    public DynaActionForm getSimpleBean(int index) {
        return (DynaActionForm)beanList.get(index);
    }
    
    public DynaActionForm getSimpleBeanIndexed(int index) {
        return (DynaActionForm)beanList.get(index);
    }
     
    public void setSimpleBean(int index, DynaActionForm testBean) {
        beanList.set(index, testBean);
    }
    
    public void setSimpleBeanIndexed(int index, DynaActionForm testBean) {
        beanList.set(index, testBean);
    }
    
    //------------------------------------------------------------------
    public void reset(ActionMapping mapping, HttpServletRequest request) {
        setFormClass(mapping, request, "listFormName", list);
        setFormClass(mapping, request, "beanListFormName", beanList);
    }

    public String getListFormName() {
        return listFormName;
    }

    public void setListFormName(String listFormName) {
        this.listFormName = listFormName;
    }

    public String getBeanListFormName() {
        return beanListFormName;
    }

    public void setBeanListFormName(String beanListFormName) {
        this.beanListFormName = beanListFormName;
    }
    
    private void setFormClass(ActionMapping mapping, HttpServletRequest request, String formName, CoeusWebList coeusWebList) {
        if(coeusWebList != null && coeusWebList.size() == 0 && coeusWebList.getDynaActionFormClass() == null) {
            String name = mapping.getName();
            FormBeanConfig formBeanConfig = mapping.getModuleConfig().findFormBeanConfig(name);
            FormPropertyConfig formPropertyConfig = formBeanConfig.findFormPropertyConfig(formName);
            if(formPropertyConfig != null && formPropertyConfig.getInitial() != null) {
                String listName = formPropertyConfig.getInitial();
                
                ServletContext servletContext = request.getSession().getServletContext();
                ModuleConfig moduleConfig = RequestUtils.getModuleConfig(request, servletContext);
                
                FormBeanConfig formConfig = moduleConfig.findFormBeanConfig(listName);
                DynaActionFormClass dynaClass = DynaActionFormClass.createDynaActionFormClass(formConfig);
                
                coeusWebList.setDynaActionFormClass(dynaClass);
            }
        }
    }
    
}
