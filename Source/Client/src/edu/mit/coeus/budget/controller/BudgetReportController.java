/*
 * BudgetReportController.java
 *
 * Created on December 21, 2005, 1:35 PM
 */

package edu.mit.coeus.budget.controller;

import edu.mit.coeus.bean.CoeusReportGroupBean;
import edu.mit.coeus.exception.CoeusException;
import edu.mit.coeus.utils.report.ReportController;
import java.util.Hashtable;

/**
 *
 * @author  geot
 */
public class BudgetReportController extends ReportController{
    
    /** Creates a new instance of BudgetReportController */
    public BudgetReportController(String repGrpName) throws CoeusException{
        super(repGrpName);
    }    
    
    //Modified for COEUSQA-1683 Print option to display Version Comments on Budget Reports
    public Hashtable getPrintData(boolean isCommentSelected) throws CoeusException {
        return null;
    }
}
