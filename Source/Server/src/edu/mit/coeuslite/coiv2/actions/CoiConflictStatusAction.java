/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.mit.coeuslite.coiv2.actions;

import edu.dartmouth.coeuslite.coi.action.COIBaseAction;
import edu.mit.coeus.bean.PersonInfoBean;
import edu.mit.coeus.bean.UserInfoBean;
import edu.mit.coeus.coi.bean.ComboBoxBean;
import edu.mit.coeus.utils.dbengine.DBException;
import edu.mit.coeuslite.coiv2.beans.CoiAnnualPersonProjectDetails;
import edu.mit.coeuslite.coiv2.beans.CoiFinancialEntityBean;
import edu.mit.coeuslite.coiv2.beans.CoiInfoBean;
import edu.mit.coeuslite.coiv2.utilities.CoiConstants;
import edu.mit.coeuslite.utils.SessionConstants;
import edu.mit.coeuslite.utils.bean.WebTxnBean;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
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
 * @author UnNamed
 */
public class CoiConflictStatusAction extends COIBaseAction{
    private static String GET_CONFLICT = "/coiConflictStatus";
    private static String SAVE_CONFLICT_STATUS = "/coiSaveConflictStatus";
    private static String SAVE_SELECTED_PROJECT = "/coiSaveSelectedProject"; 
    private static String SKIP_PROJECT = "/coiSkipProject";
    private static String USER_ID;
    @Override
    public ActionForward performExecuteCOI(ActionMapping actionMapping, ActionForm actionForm, HttpServletRequest request, HttpServletResponse response) throws Exception {
        String navigator = "success" ;
         HttpSession session = request.getSession(); 
         UserInfoBean userInfoBean = (UserInfoBean)session.getAttribute("user"+session.getId());
         USER_ID = userInfoBean.getUserId();
        request.setAttribute("DiscViewByPrjt", true);//to check Project menu selected
        CoiInfoBean coiInfoBean = (CoiInfoBean)request.getSession().getAttribute("CoiInfoBean");
           if(coiInfoBean==null){
              coiInfoBean =new CoiInfoBean();
           }
    /*  // DISCARD Action     
        getProjectsForDiscardAction(request,coiInfoBean);  
     // DISCARD Action  */
        String moduleItemKey = (String)request.getParameter("selectedProject");   
        request.setAttribute("projectType",coiInfoBean.getProjectType());
          
        Vector vecProjectDetails = (Vector)request.getSession().getAttribute("CoiAnnualProjects");
        if(actionMapping.getPath().equals(GET_CONFLICT)){
            vecProjectDetails = getNextProject(request,vecProjectDetails,coiInfoBean); 
        }
        else if(actionMapping.getPath().equals(SAVE_CONFLICT_STATUS)){
            saveProjectConflitStatus(request,vecProjectDetails,coiInfoBean); 
            vecProjectDetails = getNextProject(request,vecProjectDetails,coiInfoBean);
            boolean canContinue = checkProjectSavedFinished(vecProjectDetails);
            if(canContinue){
                navigator = "continue";
            }
        } 
        else if(actionMapping.getPath().equals(SAVE_SELECTED_PROJECT)){
            if(moduleItemKey!=null){
             vecProjectDetails = getSelectedProject(request,vecProjectDetails,moduleItemKey,coiInfoBean);
            }
        }     
        else if(actionMapping.getPath().equals(SKIP_PROJECT)){
            if(moduleItemKey!=null){
             getSkippedProject(request,vecProjectDetails,moduleItemKey,coiInfoBean);
             vecProjectDetails = getNextProject(request,vecProjectDetails,coiInfoBean);
             boolean canContinue = checkProjectSavedFinished(vecProjectDetails);
             if(canContinue){
                navigator = "continue";
             }
            }
        }        
        // sub header details S T A R T S      
        PersonInfoBean person = (PersonInfoBean) request.getSession().getAttribute(SessionConstants.LOGGED_IN_PERSON);
        String personId = person.getPersonID();
        getCoiPersonDetails(personId,request);
        String disclosureNumber=null;
        Integer sequenceNumber=null;
        disclosureNumber = coiInfoBean.getDisclosureNumber();
        sequenceNumber =coiInfoBean.getSequenceNumber();
        if(sequenceNumber == null){
            sequenceNumber =coiInfoBean.getApprovedSequence();
        }
        setApprovedDisclosureDetails(disclosureNumber,sequenceNumber,personId,request);     
       // sub header details E N D S
      //Menu Saved Start   
        request.setAttribute("byProjectMenu",true);
        if(coiInfoBean!=null){
        coiMenuDataSaved(coiInfoBean.getDisclosureNumber(),coiInfoBean.getSequenceNumber(),coiInfoBean.getPersonId(),request);
        }
     //Menu Saved ends
       return actionMapping.findForward(navigator);
    }
     private void getEntityStatus(HttpServletRequest request) throws DBException, Exception{
       //Getting entity satus code to populate in combobox
       PersonInfoBean person = (PersonInfoBean) request.getSession().getAttribute("person");
       HashMap hmData = new HashMap();
       hmData.put("personId", person.getPersonID());
       WebTxnBean webTxnBean = new WebTxnBean();
       Hashtable entityCodeList = (Hashtable) webTxnBean.getResults(request, "getEntityStatusCode", hmData);
       Vector entityTypeList = (Vector)entityCodeList.get("getEntityStatusCode");
       Vector entytyStatusList = filterEntityStatusCode(entityTypeList);
       request.setAttribute("typeList", entytyStatusList);
   } 
   
   private Vector getFinancialEnt(HttpServletRequest request,Vector finEntityCombo,String moduleItemKey,CoiInfoBean coiInfoBean) throws Exception{       
        PersonInfoBean person = (PersonInfoBean) request.getSession().getAttribute("person");
        HashMap hmDiscData = new HashMap();
        HashMap hmData = new HashMap();
        hmData.put("personId", person.getPersonID());
        finEntityCombo = getFinEntity(request, hmData);
        request.setAttribute("financialEntityList", finEntityCombo);
        String[] entityCode = new String[finEntityCombo.size()];
        int i = 0;
        for (Iterator it1 = finEntityCombo.iterator(); it1.hasNext();) {
            CoiFinancialEntityBean entity = (CoiFinancialEntityBean) it1.next();
            entityCode[i] = entity.getCode();
            i++;
            if (entity.getEntityNumber()!=null && entity.getEntityNumber().trim().length() > 0){
                    hmDiscData.put("disclosureNumber", coiInfoBean.getDisclosureNumber());
                    hmDiscData.put("sequenceNumber", coiInfoBean.getSequenceNumber());
                    hmDiscData.put("moduleItemKey", moduleItemKey);
                    hmDiscData.put("entityNumber", entity.getEntityNumber());                    
                    Vector vecDiscDetailsData = getSavedDisclDetailsData(request, hmDiscData);
                    if(vecDiscDetailsData!=null){
                      CoiFinancialEntityBean entityDetailMerge = (CoiFinancialEntityBean) vecDiscDetailsData.get(0); 
                      entity.setStatusCode(entityDetailMerge.getStatusCode());
                      entity.setRelationshipDescription(entityDetailMerge.getRelationshipDescription());
                      entity.setOrgRelationDescription(entityDetailMerge.getOrgRelationDescription());
                    }
                    else{
                           entity.setStatusCode(CoiConstants.COI_DISCLOSURE_STATUS_CODE);//  pending                           
                           entity.setOrgRelationDescription(null);
                    }
                  }
        }
        request.setAttribute("financialArrayEntityList", entityCode);//setting finacial entity code Array
        return finEntityCombo;
   }
    private Vector getFinEntity(HttpServletRequest request, HashMap hmData) throws IOException, Exception {
       WebTxnBean webTxnBean = new WebTxnBean();
       Hashtable projectDetailsList = (Hashtable) webTxnBean.getResults(request, "getFinEntityListCoiv2Bean", hmData);
        Vector finEntityList = (Vector) projectDetailsList.get("getFinEntityListCoiv2Bean");
        Vector finEntityListNew=new Vector();
        if(finEntityList!=null){
        for(int index=0;index<finEntityList.size();index++){
        if(((CoiFinancialEntityBean)finEntityList.get(index)).getStatusCode()==1){
            finEntityListNew.add(finEntityList.get(index));
        }
        }
        }
        request.getSession().setAttribute("finEntityComboList", finEntityListNew);
        return finEntityListNew;
    }
     private Vector filterEntityStatusCode(Vector entityTypeList) {
      Vector entytyStatusList = new Vector();
      if(entityTypeList != null && entityTypeList.size() > 0) {
        for(int k=0; k < entityTypeList.size(); k++) {
            ComboBoxBean bean = (ComboBoxBean)entityTypeList.get(k);          
             if(bean.getCode().equals("210") || bean.getCode().equals("320") || bean.getCode().equals("310")){
                entytyStatusList.add(bean);
             }
        }
    }
      return entytyStatusList;
  }     
          private void setApprovedDisclosureDetails(String coiDisclosureNumber,Integer sequenceNumber,String personId,HttpServletRequest request) throws Exception {       
        HashMap hmData = new HashMap();
        hmData.put("coiDisclosureNumber", coiDisclosureNumber);
            if(coiDisclosureNumber==null)
            { hmData.put("sequenceNumber",0);
            }
            else
            {hmData.put("sequenceNumber", sequenceNumber);
            }
        hmData.put("personId", personId);
        WebTxnBean webTxn = new WebTxnBean();            
            Hashtable DisclData = (Hashtable) webTxn.getResults(request, "getDisclBySequnce", hmData);
            Vector DisclDet = (Vector) DisclData.get("getDisclBySequnce");
            if (DisclDet != null && DisclDet.size() > 0) {
                request.setAttribute("ApprovedDisclDetView", DisclDet);
            }
      
    }
    private Vector getNextProject(HttpServletRequest request,Vector vecProjectDetails,CoiInfoBean coiInfoBean) throws Exception{
        int projectNum = 0;
        String moduleItemKey = null;
        if(vecProjectDetails!=null && !vecProjectDetails.isEmpty()){
             for (Iterator it = vecProjectDetails.iterator(); it.hasNext();) {
                 CoiAnnualPersonProjectDetails  coiAnnualPersonProjectDetails =(CoiAnnualPersonProjectDetails)it.next();                 
                 projectNum++;
                 if(coiAnnualPersonProjectDetails!=null && !coiAnnualPersonProjectDetails.isFlag()){  
                   //  coiAnnualPersonProjectDetails.setFlag(true);
                     moduleItemKey = coiAnnualPersonProjectDetails.getModuleItemKey();
                     //FE details S T A R T S        
                     Vector finEntityCombo = new Vector();
                     finEntityCombo = getFinancialEnt(request,finEntityCombo,moduleItemKey,coiInfoBean);
                     getEntityStatus(request);        
                     //FE details E N D S                     
                     request.setAttribute("coiProjectDetail", coiAnnualPersonProjectDetails);
                     request.setAttribute("projectNum",projectNum);
                     request.setAttribute("totalProjectSize", vecProjectDetails.size());
                     request.setAttribute("moduleItemKeyConflict",coiAnnualPersonProjectDetails.getModuleItemKey());
                     break;                     
                 }                 
             }             
     //     request.getSession().setAttribute("projectDetailsListInSeesion",vecProjectDetails);
          request.getSession().setAttribute("CoiAnnualProjects",vecProjectDetails);            
     }
        return vecProjectDetails;
    }
    private Vector saveProjectConflitStatus(HttpServletRequest request,Vector vecProjectDetails,CoiInfoBean coiInfoBean) throws IllegalAccessException, InvocationTargetException, IOException, Exception{
        WebTxnBean webTxnBean = new WebTxnBean();
        String moduleItemKey = request.getParameter("moduleItemKey");
        Vector finEntityComboList = (Vector) request.getSession().getAttribute("finEntityComboList");
            if (finEntityComboList == null || finEntityComboList.isEmpty()) {
                CoiFinancialEntityBean empty = new CoiFinancialEntityBean();
                finEntityComboList.add(empty);
            }
            Vector saveProjectDetailsList = new Vector();
            String relationshipDescription = " ";
            String orgRelationshipDesc = " ";
            if(vecProjectDetails!=null && !vecProjectDetails.isEmpty()){
                for (Iterator it = vecProjectDetails.iterator(); it.hasNext();) {
                    CoiAnnualPersonProjectDetails coiPersonProjectDetails = (CoiAnnualPersonProjectDetails)it.next();
           if(coiPersonProjectDetails!=null && coiPersonProjectDetails.getModuleItemKey().equalsIgnoreCase(moduleItemKey)){                
                for(int i=0;i<finEntityComboList.size();i++){
            CoiFinancialEntityBean finEntity =(CoiFinancialEntityBean)finEntityComboList.get(i);
            String statusCode = null;
            String[] finValueArr = null;
            String finEntityNumber = null;
            String finSquenceNumber = null;
            if (finEntity.getCode() != null && !finEntity.getCode().equals("")) {
                statusCode = request.getParameter(finEntity.getCode());
                finValueArr = finEntity.getCode().split(":");
                finEntityNumber = finValueArr[0];
                finSquenceNumber = finValueArr[1];
                 relationshipDescription = request.getParameter("relDesc"+finEntity.getCode());
                 orgRelationshipDesc = request.getParameter("orgRelDesc"+finEntity.getCode());
            } else {
                statusCode = "210";
                finEntityNumber = null;
                finSquenceNumber = null;
                relationshipDescription = " ";
                orgRelationshipDesc = " ";
            }
            coiPersonProjectDetails.setEntityNumber(finEntityNumber);
            coiPersonProjectDetails.setEntitySequenceNumber(finSquenceNumber);
            if(relationshipDescription == null){
                coiPersonProjectDetails.setRelationShipDescription("test");
            }else{
               coiPersonProjectDetails.setRelationShipDescription(relationshipDescription); 
            }
            
            coiPersonProjectDetails.setOrgRelationDescription(orgRelationshipDesc);

            if (statusCode != null) {
                coiPersonProjectDetails.setCoiStatusCode(Integer.parseInt(statusCode));
            }
            coiPersonProjectDetails.setModuleItemKey(String.valueOf(coiPersonProjectDetails.getModuleItemKey()));
            coiPersonProjectDetails.setCoiProjectId(coiPersonProjectDetails.getModuleItemKey());
//                    if(coiPersonProjectDetails.isDataSaved()){
//                       coiPersonProjectDetails.setAcType("U"); 
//                    }else{
//                    coiPersonProjectDetails.setAcType(coiPersonProjectDetails.getAcType());
//                    }
            PersonInfoBean personInfoBean = (PersonInfoBean) request.getSession().getAttribute("person");
            coiPersonProjectDetails.setUpdateUser(personInfoBean.getUserName());
            coiPersonProjectDetails.setCoiDisclosureNumber(coiInfoBean.getDisclosureNumber());
            coiPersonProjectDetails.setSequenceNumber(coiInfoBean.getSequenceNumber());
            coiPersonProjectDetails.setCoiDisclosureNumber(coiInfoBean.getDisclosureNumber());
            coiPersonProjectDetails.setSequenceNumber(coiInfoBean.getSequenceNumber());
                        if(coiPersonProjectDetails!=null){
                            Integer discDetNumber = null;       
                            HashMap hmData = new HashMap();
                            hmData.put("disclosureNumber" , coiPersonProjectDetails.getCoiDisclosureNumber());
                            hmData.put("sequenceNumber",coiPersonProjectDetails.getSequenceNumber());
                            hmData.put("moduleItemKey" ,coiPersonProjectDetails.getModuleItemKey());
                            hmData.put("entityNumber",coiPersonProjectDetails.getEntityNumber());
                            hmData.put("entitySeqNumber",coiPersonProjectDetails.getEntitySequenceNumber());
                            Hashtable htGetDisclDetNum =
                            (Hashtable)webTxnBean.getResults(request, "fnGetCoiDisclDetNum", hmData);
                            HashMap hmGetDisclDetNum = (HashMap)htGetDisclDetNum.get("fnGetCoiDisclDetNum");
                            if(hmGetDisclDetNum !=null && hmGetDisclDetNum.size()>0){
                                discDetNumber = Integer.parseInt(hmGetDisclDetNum.get("li_return").toString()); 
                                if(discDetNumber > 0){
                                    coiPersonProjectDetails.setCoiDiscDetails(discDetNumber);
                                    coiPersonProjectDetails.setDataSaved(true);
                                    coiPersonProjectDetails.setAcType("U");
                                }else{
                                    coiPersonProjectDetails.setDataSaved(false);
                                    coiPersonProjectDetails.setAcType("I");
                                }
                            }else{
                                coiPersonProjectDetails.setDataSaved(false);
                                coiPersonProjectDetails.setAcType("I");
                            }       
                             coiPersonProjectDetails.setUpdateUser(USER_ID); 
                             webTxnBean.getResults(request, "updCoiDiscDetails", coiPersonProjectDetails);
                             coiPersonProjectDetails.setDataSaved(true);
                             coiPersonProjectDetails.setFlag(true);
                        }   
                
              

             }
           }
         }                
        }
       return vecProjectDetails ;   
       }
    
    private boolean checkProjectSavedFinished(Vector vecProjects){
        boolean ret = true;
        if(vecProjects!=null && !vecProjects.isEmpty()){
            for (Iterator it = vecProjects.iterator(); it.hasNext();) {
                CoiAnnualPersonProjectDetails coiPersonProjectDetails = (CoiAnnualPersonProjectDetails)it.next();
                if(!coiPersonProjectDetails.isFlag()){
                    ret = false;
                    return ret;
                }                
            }           
            
        }
            return ret;
    }
     private Vector getSelectedProject(HttpServletRequest request,Vector vecProjectDetails,String moduleItemKey,CoiInfoBean coiInfoBean) throws Exception{
        int projectNum = 0;
        if(vecProjectDetails!=null && !vecProjectDetails.isEmpty()){
             for (Iterator it = vecProjectDetails.iterator(); it.hasNext();) {
                 CoiAnnualPersonProjectDetails  coiAnnualPersonProjectDetails =(CoiAnnualPersonProjectDetails)it.next();                 
                 projectNum++;
                 if(coiAnnualPersonProjectDetails!=null && coiAnnualPersonProjectDetails.getModuleItemKey().equalsIgnoreCase(moduleItemKey)){  
                   //  coiAnnualPersonProjectDetails.setFlag(true);
                     //FE details S T A R T S        
                     Vector finEntityCombo = new Vector();
                     finEntityCombo = getFinancialEnt(request,finEntityCombo,moduleItemKey,coiInfoBean);
                     getEntityStatus(request);        
                     //FE details E N D S
                     request.setAttribute("coiProjectDetail", coiAnnualPersonProjectDetails);
                     request.setAttribute("projectNum",projectNum);
                     request.setAttribute("totalProjectSize", vecProjectDetails.size());
                     request.setAttribute("moduleItemKeyConflict",coiAnnualPersonProjectDetails.getModuleItemKey());
                     break;                     
                 }
                 else{
                    coiAnnualPersonProjectDetails.setFlag(true); 
                 }
             }             
        //  request.getSession().setAttribute("projectDetailsListInSeesion",vecProjectDetails);         
          request.getSession().setAttribute("CoiAnnualProjects",vecProjectDetails);            
     }
        return vecProjectDetails;
    }
     private Vector getSavedDisclDetailsData(HttpServletRequest request, HashMap hmData) throws IOException, Exception {
        WebTxnBean webTxnBean = new WebTxnBean();
        Hashtable projectDetailsList = (Hashtable) webTxnBean.getResults(request, "getCoiDiscDetails", hmData);
        Vector finEntityList = (Vector) projectDetailsList.get("getCoiDiscDetails");
        return finEntityList;
    }
     private Vector getSkippedProject(HttpServletRequest request,Vector vecProjectDetails,String moduleItemKey,CoiInfoBean coiInfoBean) throws Exception{
        int projectNum = 0;
        if(vecProjectDetails!=null && !vecProjectDetails.isEmpty()){
             for (Iterator it = vecProjectDetails.iterator(); it.hasNext();) {
                 CoiAnnualPersonProjectDetails  coiAnnualPersonProjectDetails =(CoiAnnualPersonProjectDetails)it.next();                 
                 projectNum++;
                 if(coiAnnualPersonProjectDetails!=null && coiAnnualPersonProjectDetails.getModuleItemKey().equalsIgnoreCase(moduleItemKey)){  
                    if(!coiAnnualPersonProjectDetails.isFlag()){
                     coiAnnualPersonProjectDetails.setFlag(true); 
                     break; 
                    }
                 }
                 else{
                    coiAnnualPersonProjectDetails.setFlag(true); 
                 }
             } 
          request.getSession().setAttribute("CoiAnnualProjects",vecProjectDetails);            
     }
        return vecProjectDetails;
    }
   /*   // DISCARD Action 
      private void getProjectsForDiscardAction(HttpServletRequest request,CoiInfoBean coiInfoBean) throws Exception{
        Vector vecSavedProject = new Vector(); 
        HashMap hmData = new HashMap();
        hmData.put("disclosureNumber", coiInfoBean.getDisclosureNumber());
        hmData.put("sequenceNumber",coiInfoBean.getSequenceNumber());
        WebTxnBean webTxnBean = new WebTxnBean();
        Hashtable projectDetailsList = (Hashtable) webTxnBean.getResults(request, "getCoiDisclosureDetails", hmData);
        vecSavedProject = (Vector) projectDetailsList.get("getCoiDisclosureDetails");
        if(vecSavedProject!=null && !vecSavedProject.isEmpty()){
        request.getSession().setAttribute("coiProjectDiscardAction", vecSavedProject);  
        }
     }
      // DISCARD Action  */
  }
    
