/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.mit.coeuslite.coiv2.actions;


import edu.dartmouth.coeuslite.coi.action.COIBaseAction;
import edu.mit.coeuslite.coiv2.beans.CoiAnnualProjectEntityDetailsBean;
import edu.mit.coeuslite.coiv2.beans.CoiProjectDiscListBean;
import edu.mit.coeuslite.utils.bean.WebTxnBean;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Vector;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

/**
 *
 * @author midhunmk
 */
public class ShowDisclosureDetailsAction extends COIBaseAction {

    /* forward name="success" path="" */
    private static final String SUCCESS = "success";

    /**
     * This is the action called from the Struts framework.
     * @param mapping The ActionMapping used to select this instance.
     * @param form The optional ActionForm bean for this request.
     * @param request The HTTP Request we are processing.
     * @param response The HTTP Response we are processing.
     * @throws java.lang.Exception
     * @return
     */

  public ActionForward performExecuteCOI(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request, HttpServletResponse response) throws Exception {
         HttpSession session = request.getSession();
         String coiProjectId=request.getParameter("coiProjectId").toString();
         WebTxnBean webTxn = new WebTxnBean();
         HashMap hmData = new HashMap();
         Vector resultlist = new Vector();
         session.removeAttribute("COIProjectDetail");
         session.removeAttribute("COIProjectDetailList");
         hmData.put("coiProjectId",coiProjectId);
         Hashtable resultData=(Hashtable)webTxn.getResults(request, "getCoiDisclosureProjectInfo", hmData);
          Vector resultlist1=(Vector)resultData.get("getCoiDisclosureProjectInfo");
         if((resultlist1!=null)&&(!resultlist1.isEmpty()))
         {
             CoiAnnualProjectEntityDetailsBean coiAnnualProjectEntityDetailsBean=
                     (CoiAnnualProjectEntityDetailsBean)resultlist1.get(0);
             session.setAttribute("COIProjectDetail",coiAnnualProjectEntityDetailsBean );
             hmData.clear();
             hmData.put("coiDisclosureNumber",coiAnnualProjectEntityDetailsBean.getCoiDisclosureNumber());
             hmData.put("moduleItemKey",coiAnnualProjectEntityDetailsBean.getModuleItemKey());
             hmData.put("modulecode",coiAnnualProjectEntityDetailsBean.getModuleCode());
             resultData=(Hashtable)webTxn.getResults(request, "getCoiHistoryDetail", hmData);
             resultlist=(Vector)resultData.get("getCoiHistoryDetail");
         }
        
        StringBuffer str= new StringBuffer();
        String str1="";
        java.util.Date tmpDate;
        SimpleDateFormat dateformatMMDDYYYY = new SimpleDateFormat("MM/dd/yyyy");

        String headerDetails = " <table class=\"tabtable\" style=\"width: 100%;padding: 10px; \"><tr style=\"background-color:#6E97CF;\"><td colspan=\"2\">"+
           "<font style=\"color:#333333;float:left;font-size:14px;font-weight:bold;\">Project History</font>"+
           "</td></tr>";        
        headerDetails += "#!#!#!#";
        str=str.append(headerDetails);

        String historyDetails="<div style=\"height: 220px;width: 650px;overflow-x: scroll; overflow-y: scroll;overflow: auto;\"><table class=\"tabtable\" border=\"1\" cellspacing=\"1\" cellpadding=\"1\" style=\"width: 100%;height: 50%;\"><tr><td class=\"theader\" align=\"left\" font style=\"color:#333333;\">Project #</td><td class=\"theader\" align=\"left\" font style=\"color:#333333;\">Event Name</td><td class=\"theader\" align=\"left\" font style=\"color:#333333;\">Title</td><td class=\"theader\" align=\"left\" font style=\"color:#333333;\">Status</td><td class=\"theader\" align=\"left\" font style=\"color:#333333;\">Update Timestamp</td></tr>";

        String disclDetails = "";
        if(resultlist!=null && !resultlist.isEmpty()){
        for (int i=0; i< resultlist.size(); i++) {
            CoiProjectDiscListBean object = (CoiProjectDiscListBean) resultlist.get(i);
            if(object!= null){
                disclDetails +="<tr valign=\"top\" style=\"height: 10px;\" bgcolor=\"#DCE5F1\";\" onmouseover=\"className='TableItemOn'\" onmouseout=\"className='TableItemOff'\" class=\"TableItemOff\">";
                disclDetails +="<td  align=\"left\" style=\"font-family:Arial,Helvetica,sans-serif;font-weight:normal;font-size:12px;align:left;\">"+ object.getCoiModuleItemKey();
                disclDetails +="</td> <td align=\"left\" style=\"font-family:Arial,Helvetica,sans-serif;font-weight:normal;font-size:12px;align:left;\">";
                disclDetails += object.getCoiEventName()+"</td>";
                disclDetails +="<td align=\"left\" style=\"font-family:Arial,Helvetica,sans-serif;font-weight:normal;font-size:12px;align:left;\">";
                disclDetails += object.getCoititle()+"</td>";
                disclDetails +="<td align=\"left\" style=\"font-family:Arial,Helvetica,sans-serif;font-weight:normal;font-size:12px;align:left;\">";
                disclDetails += object.getCoiProjectStatusDesc()+"</td>";
                disclDetails +="<td align=\"left\" style=\"font-family:Arial,Helvetica,sans-serif;font-weight:normal;font-size:12px;align:left;\">";
                disclDetails += object.getCoiUpdateTimeStamp()+"</td></tr>";
                            
            }
        }
        historyDetails += disclDetails+"<tr><td colspan=\"5\" align=\"center\"><input type=\"button\"  value=\"Close\" class=\"clsavebutton\" onclick=\"javascript:closebtn();\"/></td></tr></table></div>";
         str=str.append(historyDetails);
       } else {
            historyDetails +="<tr valign=\"top\" bgcolor=\"#DCE5F1\";\" onmouseover=\"className='TableItemOff'\" onmouseout=\"className='TableItemOff'\" class=\"TableItemOff\"><td colspan=\"5\" align=\"center\">No Details Found </td></tr>"+
                    "</table><div style=\"background-color:#6E97CF;height:60px\" align=\"center\"><input type=\"button\"  value=\"Close\" class=\"clsavebutton\" onclick=\"javascript:closebtn();\"/></div></div>";
             str=str.append(historyDetails);
       }
//        if(resultlist!=null){
        response.getWriter().print(str);
        response.flushBuffer();
//        }
        return mapping.findForward(SUCCESS);
    }
}
