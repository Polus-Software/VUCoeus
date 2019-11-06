/*
 * DocumentBean.java
 *
 * Created on June 27, 2006, 11:56 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package edu.mit.coeus.utils.document;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 *
 * @author sharathk
 */
public class DocumentBean implements Serializable{
    
    private List lstAuthorizationBean;
    
    private Map parameterMap;
    
    /** Creates a new instance of DocumentBean */
    public DocumentBean() {
    }

    public List getLstAuthorizationBean() {
        return lstAuthorizationBean;
    }

    public void setLstAuthorizationBean(List lstAuthorizationBean) {
        this.lstAuthorizationBean = lstAuthorizationBean;
    }
    
    public Map getParameterMap() {
        return parameterMap;
    }

    public void setParameterMap(Map parameterMap) {
        this.parameterMap = parameterMap;
    }
    
}
