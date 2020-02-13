/*
 * SelectPersonAction.java
 *
 * Created on 03 February 2006, 11:38
 */

package edu.mit.coeuslite.coi.action;

import edu.mit.coeus.bean.PersonInfoBean;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Vector;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.validator.DynaValidatorForm;

import edu.mit.coeuslite.utils.bean.WebTxnBean;
import edu.mit.coeus.bean.UserDetailsBean;

/**
 *
 * @author  mohann
 *  To select a person if the logged in user has <code>OSP right</code>.
 */
public class SelectPersonAction extends COIBaseAction {
    
    //private ActionForward actionForward = null;
    //private WebTxnBean webTxnBean ;
    //private HttpServletRequest req;
    //private HttpServletResponse res;
    //private ActionMapping mapping;
    private static final String EMPTY_STRING = "";
    
    /** Creates a new instance of SelectPersonAction */
    public SelectPersonAction() {
    }
    /*
    *Fix # 2866
    *change method name from performExecute to performExecuteCOI
    */
    public ActionForward performExecuteCOI(
                        ActionMapping actionMapping, 
                        org.apache.struts.action.ActionForm actionForm, 
                        HttpServletRequest request, 
                        HttpServletResponse response) throws Exception {
                            
        //this.req = request;
        //this.res = response;
        //this.mapping = actionMapping;
        
        HttpSession session= request.getSession();              
        WebTxnBean webTxnBean = new WebTxnBean();
        
           
         String sltdPersonID = request.getParameter( "personId" );            
            PersonInfoBean personInfoBean = null;
            if( sltdPersonID != null ) { 
                //get the details of the selected person after the search              
                 personInfoBean = getPersonInfo(sltdPersonID, request);                             
            } else {                
               // coeusExce.setMessage( "exceptionCode.40004" );
               // throw coeusExce;
            }
            session.setAttribute("person", personInfoBean);
        return actionMapping.findForward("success");
    }
    
    /* 
     * This method is used to get all the information of a particular person.
     */
    private  PersonInfoBean getPersonInfo(String sltdPersonID, HttpServletRequest request) throws Exception{
        HashMap hmpPersonData = new HashMap();
        hmpPersonData.put("personId",sltdPersonID);
        WebTxnBean webTxnBean = new WebTxnBean();
        Hashtable htPersonInfo = (Hashtable)webTxnBean.getResults(request,"getPersonInfoDetails",hmpPersonData);
        Vector vecPersonInfo = (Vector)htPersonInfo.get("getPersonDetails");
        HashMap hmPendingDisc = (HashMap)htPersonInfo.get("hasPendingDisclosure");
        String hasPendingDisclosure = hmPendingDisc.get("RetVal").toString();
        PersonInfoBean personInfoBean = null;
        if(vecPersonInfo!=null && vecPersonInfo.size() >0){
            personInfoBean = (PersonInfoBean) vecPersonInfo.get(0);
            personInfoBean.setPendingAnnDisclosure(hasPendingDisclosure(hasPendingDisclosure));
        }
        return personInfoBean;
    }
    
    /*
     *  This Method used to check whether the person has any pending disclosures.
     */
    private boolean hasPendingDisclosure(String hasPendingDisclosure)throws Exception{
        int hasDisclosure = 0;
        if(hasPendingDisclosure!=null || !hasPendingDisclosure.equals("")){
          hasDisclosure = Integer.parseInt(hasPendingDisclosure);
        }
        return (hasDisclosure > 0);
    }
    
}
