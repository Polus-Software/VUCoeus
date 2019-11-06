/*
 * SyncInfoBean.java
 *
 * Created on April 20, 2009, 9:32 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package edu.mit.coeus.bean;

import java.util.HashMap;
import java.util.Map;

/**
 * This bean holds all the related info for syncing
 * @author keerthyjayaraj
 * Created with case 2796 : Award Syncing
 */
public class SyncInfoBean implements BaseBean,java.io.Serializable{
    
    
    private boolean syncRequired = false;
    private String syncTarget    = null;
    private Map parameter    = new HashMap();
    /**
     * Creates a new instance of SyncInfoBean
     */
    public SyncInfoBean() {
    }
    
    public String getSyncTarget() {
        return syncTarget;
    }
    
    public void setSyncTarget(String syncTarget) {
        this.syncTarget = syncTarget;
    }
    
    public boolean isSyncRequired() {
        return syncRequired;
    }
    
    public void setSyncRequired(boolean syncRequired) {
        this.syncRequired = syncRequired;
    }
    
    public Map getParameter() {
        return parameter;
    }

    public void setParameter(Map parameter) {
        this.parameter = parameter;
    }
    
    public void addParameter(Object key, Object value) {
        this.parameter.put(key,value);
    }
}
