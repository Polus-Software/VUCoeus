/*
 * AwardBudgetController.java
 *
 * Created on July 14, 2005, 1:20 PM
 */

package edu.mit.coeus.award.controller;

import edu.mit.coeus.brokers.RequesterBean;
import edu.mit.coeus.brokers.ResponderBean;
import edu.mit.coeus.exception.CoeusException;
import edu.mit.coeus.utils.AppletServletCommunicator;
import edu.mit.coeus.utils.CoeusGuiConstants;
import edu.mit.coeus.utils.CoeusOptionPane;
import edu.mit.coeus.utils.KeyConstants;
import java.util.Hashtable;

/**
 *
 * @author  chandrashekara
 */
public abstract class AwardBudgetController extends AwardController{
    // Boolean values for Unit Level rights - start
    public boolean isAwardBudgetViewer;
    public boolean isAwardBudgetModifier;
//    public boolean isAwardBudgetAggregator;
    // Boolean values for Unit Level rights - End
    
    // Boolean values for OSP Level rights - start
    public boolean isAwardBudgetCreator;
    public boolean isOspAwardBudgetSubmitter;
    public boolean isUnitAwardBudgetSubmitter;
    public boolean isAwardBudgetSubmitter;
    public boolean isAwardBudgetApprover;
    public boolean isAwardBudgetAdmin;
    public boolean isPostAwardBudget;
    // Boolean values for OSP Level rights - End
    
    /*Award Budget Status*/
    protected static final int IN_PROGRESS=1;
    protected static final int SUBMITTED=5;
    protected static final int REJECTED=8;
    protected static final int POSTED=9;
    protected static final int TO_BE_POSTED=10;
    protected static final int ERROR_IN_POSTING=11;

    /*Award Budget Type*/
    protected static final int NEW = 1;
    protected static final int REBUDGET = 2;
    
    private Hashtable unitLevelRight;
    private Hashtable ospLevelRight;
    
    /** Creates a new instance of AwardBudgetController */
    public AwardBudgetController() {
    }
    

    
    /**
     * Getter for property unitLevelRight.
     * @return Value of property unitLevelRight.
     */
    public java.util.Hashtable getUnitLevelRight() {
        unitLevelRight = new Hashtable();
        unitLevelRight.put(KeyConstants.VIEW_AWARD_BUDGET, new Boolean(isAwardBudgetViewer));
        unitLevelRight.put(KeyConstants.MODIFY_AWARD_BUDGET, new Boolean(isAwardBudgetModifier));
        unitLevelRight.put(KeyConstants.SUBMIT_AWARD_BUDGET, new Boolean(isUnitAwardBudgetSubmitter));
        
//        unitLevelRight.put(KeyConstants.MAINTAIN_AWARD_BUDGET, new Boolean(isAwardBudgetAggregator));
        //Added with case 3587: MultiCampus Enhancement
        unitLevelRight.put(KeyConstants.CREATE_AWARD_BUDGET, new Boolean(isAwardBudgetCreator));
        unitLevelRight.put(KeyConstants.SUBMIT_ANY_AWARD_BUDGET, new Boolean(isOspAwardBudgetSubmitter));
        unitLevelRight.put(KeyConstants.APPROVE_AWARD_BUDGET, new Boolean(isAwardBudgetApprover));
        unitLevelRight.put(KeyConstants.MAINTAIN_AWARD_BUDGET_ROUTING, new Boolean(isAwardBudgetAdmin));
        unitLevelRight.put(KeyConstants.POST_AWARD_BUDGET, new Boolean(isPostAwardBudget));
        //3587 End
        return unitLevelRight;
    }
    
    /**
     * Setter for property unitLevelRight.
     * @param unitLevelRight New value of property unitLevelRight.
     */
    public void setUnitLevelRight(java.util.Hashtable unitLevelRight) {
        this.unitLevelRight = unitLevelRight;
        isAwardBudgetViewer = ((Boolean)unitLevelRight.get(KeyConstants.VIEW_AWARD_BUDGET)).booleanValue();
        isAwardBudgetModifier = ((Boolean)unitLevelRight.get(KeyConstants.MODIFY_AWARD_BUDGET)).booleanValue();
        isUnitAwardBudgetSubmitter = ((Boolean)unitLevelRight.get(KeyConstants.SUBMIT_AWARD_BUDGET)).booleanValue();
        isAwardBudgetSubmitter = isUnitAwardBudgetSubmitter || isOspAwardBudgetSubmitter;
//        isAwardBudgetAggregator = ((Boolean)unitLevelRight.get(KeyConstants.MAINTAIN_AWARD_BUDGET)).booleanValue();
        //Added with case 3587: MultiCampus Enhancement
        isAwardBudgetCreator = ((Boolean)unitLevelRight.get(KeyConstants.CREATE_AWARD_BUDGET)).booleanValue();
        isOspAwardBudgetSubmitter = ((Boolean)unitLevelRight.get(KeyConstants.SUBMIT_ANY_AWARD_BUDGET)).booleanValue();
        isAwardBudgetApprover = ((Boolean)unitLevelRight.get(KeyConstants.APPROVE_AWARD_BUDGET)).booleanValue();
        isAwardBudgetAdmin = ((Boolean)unitLevelRight.get(KeyConstants.MAINTAIN_AWARD_BUDGET_ROUTING)).booleanValue();
        isPostAwardBudget = ((Boolean)unitLevelRight.get(KeyConstants.POST_AWARD_BUDGET)).booleanValue();
        isAwardBudgetSubmitter = isUnitAwardBudgetSubmitter || isOspAwardBudgetSubmitter;
        //3587:End
    }
    
    /**
     * Getter for property ospLevelRight.
     * @return Value of property ospLevelRight.
     */
    public java.util.Hashtable getOspLevelRight() {
        ospLevelRight = new Hashtable();
        //Commented with case 3587: MultiCampus Enhancement
//        ospLevelRight.put(KeyConstants.CREATE_AWARD_BUDGET, new Boolean(isAwardBudgetCreator));
//        ospLevelRight.put(KeyConstants.SUBMIT_ANY_AWARD_BUDGET, new Boolean(isOspAwardBudgetSubmitter));
//        ospLevelRight.put(KeyConstants.APPROVE_AWARD_BUDGET, new Boolean(isAwardBudgetApprover));
//        ospLevelRight.put(KeyConstants.MAINTAIN_AWARD_BUDGET_ROUTING, new Boolean(isAwardBudgetAdmin));
//        ospLevelRight.put(KeyConstants.POST_AWARD_BUDGET, new Boolean(isPostAwardBudget));
        //3587 End
        return ospLevelRight;
    }
    
    /**
     * Setter for property ospLevelRight.
     * @param ospLevelRight New value of property ospLevelRight.
     */
    public void setOspLevelRight(java.util.Hashtable ospLevelRight) {
        this.ospLevelRight = ospLevelRight;
        //Commented with case 3587: MultiCampus Enhancement
//        isAwardBudgetCreator = ((Boolean)ospLevelRight.get(KeyConstants.CREATE_AWARD_BUDGET)).booleanValue();
//        isOspAwardBudgetSubmitter = ((Boolean)ospLevelRight.get(KeyConstants.SUBMIT_ANY_AWARD_BUDGET)).booleanValue();
//        isAwardBudgetApprover = ((Boolean)ospLevelRight.get(KeyConstants.APPROVE_AWARD_BUDGET)).booleanValue();
//        isAwardBudgetAdmin = ((Boolean)ospLevelRight.get(KeyConstants.MAINTAIN_AWARD_BUDGET_ROUTING)).booleanValue();
//        isPostAwardBudget = ((Boolean)ospLevelRight.get(KeyConstants.POST_AWARD_BUDGET)).booleanValue();
//        isAwardBudgetSubmitter = isUnitAwardBudgetSubmitter || isOspAwardBudgetSubmitter;
        //3587 End
    }
    
}
