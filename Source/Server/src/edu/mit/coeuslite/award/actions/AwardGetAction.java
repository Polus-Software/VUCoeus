/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.mit.coeuslite.award.actions;

import edu.mit.coeus.award.bean.AwardInvestigatorsBean;
import edu.mit.coeus.award.bean.AwardListBean;
import edu.mit.coeus.bean.PersonInfoBean;
import edu.mit.coeus.bean.UserDetailsBean;
import edu.mit.coeus.bean.UserInfoBean;
import edu.mit.coeus.search.bean.DisplayBean;
import edu.mit.coeus.search.bean.ProcessSearchXMLBean;
import edu.mit.coeus.search.bean.SearchExecutionBean;
import edu.mit.coeus.search.bean.SearchInfoHolderBean;
import edu.mit.coeus.utils.CoeusFunctions;
import edu.mit.coeus.utils.Utils;
import edu.mit.coeus.utils.dbengine.DBEngineConstants;
import edu.mit.coeus.utils.dbengine.DBEngineImpl;
import edu.mit.coeus.utils.dbengine.DBException;
import edu.mit.coeus.utils.dbengine.Parameter;
import edu.mit.coeuslite.award.beans.AwardDisplayBean;
//import edu.mit.coeuslite.award.beans.AwardHirarchyBean;
import edu.mit.coeuslite.award.beans.AwardLeadUnits;
import edu.mit.coeuslite.award.formbeans.AllAwardRecordFormBean;
import edu.mit.coeuslite.utils.SessionConstants;
import edu.mit.coeuslite.utils.bean.WebTxnBean;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Vector;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

/**
 *
 * @author vineetha
 */
public class AwardGetAction extends AwardBaseAction {

    /* forward name="success" path="" */
    private static final String SUCCESS = "success";
    private static final String PARAMETERFLAG="ENABLE_AWD_ANT_OBL_DIRECT_INDIRECT_COST";

    /**
     * This is the action called from the Struts framework.
     * @param mapping The ActionMapping used to select this instance.
     * @param form The optional ActionForm bean for this request.
     * @param request The HTTP Request we are processing.
     * @param response The HTTP Response we are processing.
     * @throws java.lang.Exception
     * @return
     */
    // @Override
    public AwardGetAction() {
    }

    private void createParentChildTree(Vector data, HttpServletRequest request, String awdparent) {
        HttpSession session = request.getSession();
        Vector parentHierarchy = new Vector();
        Vector childHierarchy = new Vector();
        if (data != null && data.size() > 0) {
            Iterator itr = data.iterator();

            String awardNumber = "";
            while (itr.hasNext()) {
                HashMap dataMap = (HashMap) itr.next();
                awardNumber = dataMap.get("AWARD_NUMBER").toString();
               
                if (awardNumber.equals(awdparent) ) {
                    parentHierarchy.add(dataMap);
                }          

                else{               
                      childHierarchy.add(dataMap);                  
                     }
                }
        }
        request.setAttribute("awardList", parentHierarchy);
        request.setAttribute("childHierarchy", childHierarchy);


    }

    public ActionForward performExecute(ActionMapping actionMapping, ActionForm actionForm,
            HttpServletRequest request, HttpServletResponse response) throws Exception {


        getAwardData(request);
 //       getAwardDisclosreDetails(request);
        return actionMapping.findForward(SUCCESS);

    }
/*Function to get award disclosure details--starts*/
 private void getAwardDisclosreDetails(HttpServletRequest request) throws Exception {
    HttpSession session = request.getSession();
    WebTxnBean webTxn = new WebTxnBean();
    String awardNumber = (String)session.getAttribute("mitawardnumber");
    HashMap hmData = new HashMap();
    hmData.put("mitawardnumber", awardNumber);
    Hashtable awardDisclDetails = (Hashtable) webTxn.getResults(request, "getAwardDisclosureStausDetail", hmData);
    Vector awardDisclInfo = (Vector) awardDisclDetails.get("getAwardDisclosureStausDetail");
    if (awardDisclInfo != null && awardDisclInfo.size() > 0) {
    session.setAttribute("awardDisclDetails", awardDisclInfo);
 }
 }
/*Function to get award disclosure details--ends*/

    private void getAwardData(HttpServletRequest request) throws Exception {
        HttpSession session = request.getSession();
        //HashMap hmData=new HashMap();
        WebTxnBean webTxn = new WebTxnBean();
        Vector awardDisplay = new Vector();
        Vector awardInvestigator = new Vector();
        Vector awardKeyPersons = new Vector();
        Vector awardUnit = new Vector();
        Vector awardChild = new Vector();
        String awardNumber = request.getParameter("awardNumber");
        session.setAttribute("mitawardnumber", awardNumber);
        AwardDisplayBean allAward = new AwardDisplayBean();
        DBEngineImpl dbEngine = null;
        Vector displayLabels = new Vector();

        displayLabels.add(new DisplayBean("AWARD_NUMBER","Award Number","","true","String",""));
        displayLabels.add(new DisplayBean("OBLIGATED_TOTAL_DIRECT","Obl. Total Direct","","true","number",""));
        displayLabels.add(new DisplayBean("OBLIGATED_TOTAL_INDIRECT","Obl. Total Indirect","","true","number",""));
        displayLabels.add(new DisplayBean("OBLI_DISTRIBUTABLE_AMOUNT","Obligated Distributable","","true","number",""));
        displayLabels.add(new DisplayBean("ANTICIPATED_TOTAL_DIRECT","Ant. Total Direct","","true","number",""));
        displayLabels.add(new DisplayBean("ANTICIPATED_TOTAL_INDIRECT","Ant. Total Indirect","","true","number",""));
        displayLabels.add(new DisplayBean("ANT_DISTRIBUTABLE_AMOUNT","Anticipated Distributable","","true","number",""));
        displayLabels.add(new DisplayBean("OBLIGATION_EFFECTIVE_DATE","Obligation Effective Date","","true","Date",""));
        displayLabels.add(new DisplayBean("OBLIGATION_EXPIRATION_DATE","Obligation Expiration Date","","true","Date",""));
        displayLabels.add(new DisplayBean("FINAL_EXPIRATION_DATE","Final Expiration Date","","true","Date",""));

        HashMap hmData = new HashMap();
        hmData.put("awardNumber", awardNumber);

        Hashtable apprvdDisclDet = (Hashtable) webTxn.getResults(request, "getAwardDetails", hmData);
        awardDisplay = (Vector) apprvdDisclDet.get("getAwardDetails");
        if (awardDisplay != null && awardDisplay.size() > 0) {
            allAward = (AwardDisplayBean) awardDisplay.get(0);
            request.setAttribute("awardListtmp", allAward);

            Hashtable investi = (Hashtable) webTxn.getResults(request, "getAwardInvestigators", hmData);
            awardInvestigator = (Vector) investi.get("getAwardInvestigators");
            request.setAttribute("awardInvestList", awardInvestigator);


            ////////////////////FOR THE AWARD KEY PERSON LIST PREPARATION.
            Hashtable keyPersons = (Hashtable) webTxn.getResults(request, "getAwardKeypersons", hmData);
            awardKeyPersons = (Vector) keyPersons.get("getAwardKeypersons");
            request.setAttribute("awardKeyPersonsList", awardKeyPersons);
            ////////////////////AWARD KEY PERSON LIST PREPARATION ENDS.

            
            Hashtable units = (Hashtable) webTxn.getResults(request, "getAwardUnits", hmData);
            awardUnit = (Vector) units.get("getAwardUnits");
            for (Iterator it = awardUnit.iterator(); it.hasNext();) {
                AwardLeadUnits bean = (AwardLeadUnits) it.next();
                 request.setAttribute("leadUnit", bean);
            }
           

            Hashtable splRevtb = (Hashtable) webTxn.getResults(request, "getAwardSpecialReviewList", hmData);
            Vector splRevList = (Vector)splRevtb.get("getAwardSpecialReviewList");
             request.setAttribute("specialRevList", splRevList);
            
            //getting the parameter value
            CoeusFunctions coeusFunctions=new CoeusFunctions();
            String parameter =coeusFunctions.getParameterValue(PARAMETERFLAG);
            if((parameter!=null)&&(parameter.equals("1")))
                {session.setAttribute("ENABLE_DIRECT_INDIRECT",true);}
            else{session.setAttribute("ENABLE_DIRECT_INDIRECT",false);}
            
            
            PersonInfoBean personInfoBean = (PersonInfoBean) session.getAttribute(SessionConstants.LOGGED_IN_PERSON);
            String personId = personInfoBean.getPersonID();
            
// for the hierarchy

            dbEngine = new DBEngineImpl();
            Vector result = new Vector(3,2);
            Vector param= new Vector();

             param = new Vector();
            param.addElement(new Parameter("AS_MIT_AWARD_NUMBER",
                    DBEngineConstants.TYPE_STRING,hmData.get("awardNumber").toString()));

             if(dbEngine!=null){
                result = dbEngine.executeRequest("Coeus",
                        "call GET_PARENT_CHILD_AWD_DET( "
                        +"<<AS_MIT_AWARD_NUMBER>>, <<OUT RESULTSET rset>> )", "Coeus", param);
            }
            else{
                throw new DBException("DB instance is not available");
            }

            Vector awardListDisplay = new Vector();
            Iterator itr1 = displayLabels.iterator();

            while(itr1.hasNext()) {
                DisplayBean dispBean = (DisplayBean)itr1.next();
                    awardListDisplay.add(dispBean);
            }

             request.setAttribute("awardColumnNames", awardListDisplay);

             createParentChildTree(result, request, awardNumber);
        }
    }
static Map sortByValue(Map map) {
     List list = new LinkedList(map.entrySet());
     Collections.sort(list, new Comparator() {
          public int compare(Object o1, Object o2) {
               return ((Comparable) ((Map.Entry) (o1)).getValue())
              .compareTo(((Map.Entry) (o2)).getValue());
          }
     });

    Map result = new LinkedHashMap();
    for (Iterator it = list.iterator(); it.hasNext();) {
        Map.Entry entry = (Map.Entry)it.next();
        result.put(entry.getKey(), entry.getValue());
    }
    return result;
}
}


      