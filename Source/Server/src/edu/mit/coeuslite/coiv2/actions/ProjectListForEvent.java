/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.mit.coeuslite.coiv2.actions;
import edu.dartmouth.coeuslite.coi.action.COIBaseAction;
import edu.mit.coeus.bean.PersonInfoBean;
import edu.mit.coeuslite.coiv2.beans.CoiInfoBean;
import edu.mit.coeuslite.coiv2.beans.CoiPersonProjectDetails;
import edu.mit.coeuslite.coiv2.beans.CoiProjectEntityDetailsBean;
import edu.mit.coeuslite.utils.SessionConstants;
import edu.mit.coeuslite.utils.bean.WebTxnBean;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Vector;
import javax.servlet.http.*;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
/**
 *
 * @author x + iy
 */
public class ProjectListForEvent extends COIBaseAction {
     private static final String SUCCESS = "success";
     
     public ActionForward performExecuteCOI(ActionMapping actionMapping, ActionForm actionForm,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
      //   projectDetailsListInSeesion
        CoiInfoBean coiInfoBean = (CoiInfoBean)request.getSession().getAttribute("CoiInfoBean");
        if(coiInfoBean==null){
            coiInfoBean =new CoiInfoBean();
        }
        String  disclosureNumber = null,projectId = null;
        int moduleCode = -1 ,eventcode = -1,sequenceNumber = -1;
        request.setAttribute("DiscViewByPrjt", true);//to check Project menu selected
        HashMap hmData = new HashMap();
        Vector projectDetails = new Vector();
        CoiPersonProjectDetails projectDet = new CoiPersonProjectDetails();   
        PersonInfoBean person = (PersonInfoBean) request.getSession().getAttribute(SessionConstants.LOGGED_IN_PERSON);
        String personId=person.getPersonID();
        if(request.getSession().getAttribute("InPrgssModulecode")!=null){
            moduleCode = Integer.parseInt(request.getSession().getAttribute("InPrgssModulecode").toString());  
        }
        if(request.getSession().getAttribute("InPrgssCode")!=null){
            eventcode = Integer.parseInt(request.getSession().getAttribute("InPrgssCode").toString());  
        }
        if(request.getSession().getAttribute("InPrgssSeqNo")!=null){
            sequenceNumber = Integer.parseInt(request.getSession().getAttribute("InPrgssSeqNo").toString()); 
        }
        if(request.getSession().getAttribute("InPrgssDisclNo")!=null){
            disclosureNumber=(String)request.getSession().getAttribute("InPrgssDisclNo");            
        }
        if(request.getSession().getAttribute("InPrgssProjectID")!=null){
           projectId= (String)request.getSession().getAttribute("InPrgssProjectID");
        }
        
   if(sequenceNumber == -1 || (request.getSession().getAttribute("hasEnteredCoiNonQnr")!=null)){
    sequenceNumber= (Integer) request.getSession().getAttribute("currentSequence"); 
   }

        hmData.put("coiDisclosureNumber", disclosureNumber);
        hmData.put("sequenceNumber",sequenceNumber);
        hmData.put("moduleCode", moduleCode);
        hmData.put("moduleItemKey",projectId);
        WebTxnBean webTxn = new WebTxnBean();
        Vector apprvdDiscl = null;
        CoiProjectEntityDetailsBean coiProjectEntityDetailsBean = new CoiProjectEntityDetailsBean();
        Hashtable apprvdDisclDet = (Hashtable) webTxn.getResults(request, "getCoiProjectDetails", hmData);
        apprvdDiscl = (Vector) apprvdDisclDet.get("getCoiProjectDetails");
        if (apprvdDiscl != null && apprvdDiscl.size() > 0) {
            coiProjectEntityDetailsBean = (CoiProjectEntityDetailsBean) apprvdDiscl.get(0);
            projectDet.setModuleCode(moduleCode);
            projectDet.setCoiProjectTitle(coiProjectEntityDetailsBean.getCoiProjectTitle());
            projectDet.setAwardTitle(coiProjectEntityDetailsBean.getCoiProjectTitle());

            if(moduleCode == 0) {
               projectDet.setCoiProjectSponser(coiProjectEntityDetailsBean.getEventName()) ;
               projectDet.setEventName(coiProjectEntityDetailsBean.getCoiProjectSponsor());
               projectDet.setPurpose(coiProjectEntityDetailsBean.getPurpose());
            }else {
                projectDet.setCoiProjectSponser(coiProjectEntityDetailsBean.getCoiProjectSponsor());
            }
            projectDet.setCoiProjectStartDate(coiProjectEntityDetailsBean.getCoiProjectStartDate());
            projectDet.setCoiProjectEndDate(coiProjectEntityDetailsBean.getCoiProjectEndDate());  
            projectDet.setModuleItemKey(projectId);
          //  projectDet.setCoiProjectSponser(coiProjectEntityDetailsBean.getCoiProjectSponsor());
            projectDetails.add(projectDet);     
            request.setAttribute("proposalList", projectDetails);
            request.setAttribute("InProgProjectList", projectDetails);
            request.getSession().setAttribute("projectDetailsListInSeesion", projectDetails);
        }  
        
        if(request.getSession().getAttribute("currentSequence")!=null){
           sequenceNumber=(Integer) request.getSession().getAttribute("currentSequence");          
           }
       setApprovedDisclosureDetails(disclosureNumber,sequenceNumber,personId,request);  
       //Menu Saved Start   
        request.setAttribute("byProjectMenu",true);
        if(coiInfoBean!=null){
        coiMenuDataSaved(coiInfoBean.getDisclosureNumber(),coiInfoBean.getSequenceNumber(),coiInfoBean.getPersonId(),request);
        }
     //Menu Saved ends
       return actionMapping.findForward(SUCCESS);
       
     }
       private void setApprovedDisclosureDetails(String coiDisclosureNumber,Integer sequenceNumber,String personId,HttpServletRequest request) throws Exception {
        HttpSession session = request.getSession();
        HashMap hmData = new HashMap();
        hmData.put("coiDisclosureNumber", coiDisclosureNumber);
         if(coiDisclosureNumber==null)
        { hmData.put("sequenceNumber",0);}
        else
        {hmData.put("sequenceNumber", sequenceNumber);}
        hmData.put("personId", personId);
        WebTxnBean webTxn = new WebTxnBean(); 
        Hashtable DisclData = (Hashtable) webTxn.getResults(request, "getDisclBySequnce", hmData);
        Vector DisclDet = (Vector) DisclData.get("getDisclBySequnce");
        if (DisclDet != null && DisclDet.size() > 0) {
            request.setAttribute("ApprovedDisclDetView", DisclDet);
        }

        hmData = new HashMap();
        hmData.put("personId", personId);
        Hashtable htPersonData = (Hashtable) webTxn.getResults(request, "getPersonDetails", hmData);
        Vector personDatas = (Vector) htPersonData.get("getPersonDetails");
        if (personDatas != null && personDatas.size() > 0) {
            PersonInfoBean personInfoBean = (PersonInfoBean) personDatas.get(0);
              request.setAttribute("PersonDetails", personDatas);
            session.setAttribute("person", personInfoBean);
    }

    }
    
}
