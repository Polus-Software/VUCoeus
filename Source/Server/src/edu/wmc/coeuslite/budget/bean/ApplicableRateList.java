/*
 * ProposalRateBean.java
 *
 * Created on May 31, 2006, 4:19 PM
 */

package edu.wmc.coeuslite.budget.bean;

import java.util.* ;
import javax.servlet.http.* ;
import org.apache.struts.action.* ;
import org.apache.struts.validator.* ;

/**
 *
 * @author  prr2004
 */


public class ApplicableRateList extends ValidatorForm{
    
 private List list ;   
   
    public ApplicableRateList() 
    {
        list = new ArrayList() ;
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
    
    
    public void setListIndexed(int index, Object value)
    {
        list.set(index, value) ;
    }
    
    public DynaActionForm getListIndexed(int index)
    {
        return (DynaActionForm)list.get(index) ;
    }
    
        
    
    // is method called by JSP
    public DynaActionForm getBudgetProposalRate_wmc(int index) {
        return (DynaActionForm)list.get(index);
    }
    
    public DynaActionForm getBudgetProposalRate_wmcIndexed(int index) {
        return (DynaActionForm)list.get(index);
    }
     
    public void setApplicableRateList(int index, DynaActionForm actForm) {
    	    list.set(index, actForm);
    }
 
    public void setApplicableRateListIndexed(int index, DynaActionForm actForm) {
        list.set(index, actForm); 
    }
    
    public java.util.List getApplicableRateList() {
    	     return list;
    }
 
    public java.util.List getApplicableRateListIndexed() {
         return list;
    }
    
    // method called by JSP
    public void setBudgetProposalRate_wmc(int index, DynaActionForm budgetProposalRate_wmc) {
    	    list.set(index, budgetProposalRate_wmc);
    }
      
    public void setBudgetProposalRate_wmcIndexed(int index, DynaActionForm budgetProposalRate_wmc) {
    	    list.set(index, budgetProposalRate_wmc);
    }

    public void reset(ActionMapping mapping, HttpServletRequest request) {

    }
     
//        public ActionErrors validate(ActionMapping mapping, HttpServletRequest request  )
//    {
//        ActionErrors errors = new ActionErrors() ;
//        
//        if (request.getParameter("operation") != null)
//        {    
//            errors.add("test", new ActionMessage("validation from bean")) ;
//            System.out.println("Validation " ) ;
//            if (list != null)
//            {
//                System.out.println("List Size " + list.size()) ;
//                for (int i=0; i< list.size() ; i++)
//                {
//                   DynaActionForm dynatest = (DynaActionForm)list.get(i) ;
//                   System.out.println(" Rate  " + dynatest.get("applicableRate_wmc")) ;
//                
//                }    
//                    
//            }    
//                
//        }   
//        
//        
//        return errors ;
//         
//    }
    
     
}
