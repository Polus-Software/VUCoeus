/*
 * CoeusWebList.java
 *
 * Created on August 18, 2006, 10:58 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package edu.mit.coeuslite.utils;

import edu.mit.coeus.utils.UtilFactory;
import java.util.*;
import org.apache.struts.action.*;

/**
 *
 * @author sharathk
 */
public class CoeusWebList extends ArrayList{
    
    private DynaActionFormClass dynaActionFormClass;
    
    /** Creates a new instance of CoeusWebList */
    public CoeusWebList() {
    }
    
    public Object get(int index) {
        Object retValue = null;
        try{
            if(size() > index) {
                retValue = super.get(index);
                if(retValue == null) {
                    retValue = dynaActionFormClass.newInstance();
                    set(index, retValue);
                }
            }else {
                retValue = dynaActionFormClass.newInstance();
                //add(retValue);
                while(index >= size()) {
                    add(null);
                }
                set(index, retValue);
            }
        }catch (Exception exception) {
            UtilFactory.log(exception.getMessage(), exception, "CoeusWebList", "get");
        }
        return retValue;
    }
    
    public DynaActionFormClass getDynaActionFormClass() {
        return dynaActionFormClass;
    }
    
    public void setDynaActionFormClass(DynaActionFormClass dynaActionFormClass) {
        this.dynaActionFormClass = dynaActionFormClass;
    }
    
    
}