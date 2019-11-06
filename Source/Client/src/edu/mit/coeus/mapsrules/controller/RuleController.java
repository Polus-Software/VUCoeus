/*
 * RuleController.java
 *
 * Created on October 11, 2005, 6:28 PM
 */

package edu.mit.coeus.mapsrules.controller;

import edu.mit.coeus.gui.event.Controller;
import edu.mit.coeus.mapsrules.bean.RuleBaseBean;

/**
 *
 * @author  chandrashekara
 */
public abstract class RuleController extends Controller{
    public static final String EMPTY_STRING = "";
    public static final String ROUTING = "R";
    public static final String VALIDATION= "V";
    public static final String NOTIFICATION = "N";
    
    public static final String VAL_ROUTING = "Routing";
    public static final String VAL_VALIDATION = "Validation";
    public static final String VAL_NOTIFICATION = "Notification";
     //Added for Coeus 4.3 Routing enhancement -PT ID:2785 - start
    public static final String QUESTION = "Q";
    public static final String VAL_QUESTION = "Question";
    //Added for Coeus 4.3 Routing enhancement -PT ID:2785 - end
    public String queryKey = EMPTY_STRING;
    private RuleBaseBean ruleBaseBean;
    
    /** Creates a new instance of RuleController */
    public RuleController() {
    }
    
    public RuleController(RuleBaseBean ruleBaseBean){
        
        if(ruleBaseBean != null && ruleBaseBean.getUnitNumber() != null) {
            this.ruleBaseBean  = ruleBaseBean;
            queryKey = ruleBaseBean.getUnitNumber();
        }
    }
    
   
    
}
