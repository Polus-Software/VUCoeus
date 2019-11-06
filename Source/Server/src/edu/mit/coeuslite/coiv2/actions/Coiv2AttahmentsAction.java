/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.mit.coeuslite.coiv2.actions;
import edu.dartmouth.coeuslite.coi.action.COIBaseAction;
import edu.mit.coeus.bean.PersonInfoBean;
import edu.mit.coeuslite.coiv2.beans.CoiAwardInfoBean;
import edu.mit.coeuslite.utils.ComboBoxBean;
import edu.mit.coeuslite.coiv2.beans.CoiDisclosureBean;
import edu.mit.coeuslite.coiv2.beans.CoiFinancialEntityBean;
import edu.mit.coeuslite.coiv2.beans.CoiInfoBean;
import edu.mit.coeuslite.coiv2.beans.CoiProposalBean;
import edu.mit.coeuslite.coiv2.beans.CoiProtocolInfoBean;
import edu.mit.coeuslite.coiv2.beans.Coiv2AttachmentBean;
import edu.mit.coeuslite.coiv2.formbeans.Coiv2Attachment;
import edu.mit.coeuslite.coiv2.services.CoiAttachmentService;
import edu.mit.coeuslite.coiv2.services.CoiCommonService;
import edu.mit.coeuslite.utils.SessionConstants;
import edu.mit.coeuslite.utils.bean.WebTxnBean;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Vector;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.fop.datatypes.Length;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.validator.DynaValidatorForm;

/**
 *
 * @author Mr Lijo Joy
 */
public class Coiv2AttahmentsAction extends COIBaseAction { 
     private static String projectType ="";
    public ActionForward performExecuteCOI(ActionMapping actionMapping, ActionForm actionForm, HttpServletRequest request, HttpServletResponse response) throws Exception {

        CoiInfoBean coiInfoBean = (CoiInfoBean)request.getSession().getAttribute("CoiInfoBean");
           if(coiInfoBean==null){
              coiInfoBean =new CoiInfoBean();
           }
           String disclosureNumber = coiInfoBean.getDisclosureNumber();
           Integer sequenceNumber = coiInfoBean.getSequenceNumber();        
        //for annual disclosure menu change
        CoiCommonService coiCommonService1 = CoiCommonService.getInstance();
        Integer disclosureAvailable = coiCommonService1.userHasDisclosure(request);
         request.removeAttribute("DiscViewByPrjt");//to check Project menu selected
         request.setAttribute("DiscViewAttchmt", true);//to check Attachment menu selected
        if (disclosureAvailable > 0) {
            request.setAttribute("disclosureAvailableMessage", true);
            CoiDisclosureBean annualBean = new CoiDisclosureBean();
            annualBean = coiCommonService1.getAnnualDisclosure(request);
            request.setAttribute("annualDisclosureBean", annualBean);
        } else {
            request.setAttribute("disclosureAvailableMessage", false);
        }
        HttpSession session = request.getSession();
         // sub header details S T A R T S      
        PersonInfoBean person = (PersonInfoBean) request.getSession().getAttribute(SessionConstants.LOGGED_IN_PERSON);
        String personId = person.getPersonID();
        getCoiPersonDetails(personId,request);

        disclosureNumber = coiInfoBean.getDisclosureNumber();
        Integer approvedSequenceNumber = coiInfoBean.getSequenceNumber();
        if(approvedSequenceNumber == null){
            approvedSequenceNumber = coiInfoBean.getApprovedSequence();
        }
        String selPersonId = coiInfoBean.getPersonId();

        setApprovedDisclosureDetails(disclosureNumber,approvedSequenceNumber,selPersonId,request);
       // sub header details E N D S
        //Menu Saved Start   
        request.setAttribute("byProjectMenu",true);
        if(coiInfoBean!=null){
        coiMenuDataSaved(coiInfoBean.getDisclosureNumber(),coiInfoBean.getSequenceNumber(),coiInfoBean.getPersonId(),request);
        }
     //Menu Saved ends
        //for annual disclosure menu change end
 Vector pjtlist=new Vector();
//added by 10-12-2010
PersonInfoBean person1 = (PersonInfoBean) session.getAttribute(SessionConstants.LOGGED_IN_PERSON);
String personId1=person1.getPersonID();
 String persnId = null;
            persnId = (String) session.getAttribute("param3");
            if (persnId == null) {
            persnId=personId1;
            }
WebTxnBean txnBean = new WebTxnBean();
 HashMap hmData1 = new HashMap();
        hmData1.put("personId", personId1);
        Hashtable htPersonData = (Hashtable) txnBean.getResults(request, "getPersonDetails", hmData1);
        Vector personDatas = (Vector) htPersonData.get("getPersonDetails");
        if (personDatas != null && personDatas.size() > 0) {
            PersonInfoBean personInfoBean = (PersonInfoBean) personDatas.get(0);

            //added by Vineetha
              request.setAttribute("PersonDetails", personDatas);
            session.setAttribute("person", personInfoBean);
        }
           request.getSession().removeAttribute("attachmentList");              
           setApprovedDisclosureDetails(disclosureNumber,sequenceNumber,personId,request);
           request.setAttribute("DiscViewAttchmt", true);//to check Attachment menu selected
        CoiAttachmentService coiAttachmentService = CoiAttachmentService.getInstance();       
        String operationType = (String) request.getAttribute("operationType");
        String isViewer = (String) request.getAttribute("isViewer");
        String returnType = "failure";
        Vector docType = coiAttachmentService.getDocumentType(request);
        request.setAttribute("DocTypes", docType);
        HashMap hmData = new HashMap();
        hmData = new HashMap();
        Vector finEntityCombo = new Vector();
        hmData = new HashMap();
        hmData.clear();
        hmData.put("personId", person.getPersonID());
        finEntityCombo = getFinEntity(request, hmData);
        request.setAttribute("FinEntForPerson", finEntityCombo);
        request.getSession().setAttribute("FinEntForPerson", finEntityCombo);
        Vector attachmentDet = coiAttachmentService.getUploadDocumentForPerson(disclosureNumber, sequenceNumber, personId1);
        if(attachmentDet != null) {
            request.setAttribute("attachmentList", attachmentDet);
            request.getSession().setAttribute("attachmentList", attachmentDet);
            request.getSession().setAttribute("attachmentListInsession", attachmentDet);
        }
        else {
            request.removeAttribute("attachmentList");
            request.getSession().removeAttribute("attachmentList");
            request.setAttribute("message", false);
        }


  //for getting financial entities of logged in person ends
           //for getting disclosure projects strts
           projectType = (String) request.getSession().getAttribute("projectType");
           if(projectType!= null)
           {
               if(projectType.equals("Annual") || projectType.equals("Revision"))
               {
                   session.setAttribute("annual", true);
                   HashMap hmDiscl=new HashMap();                  
                    hmDiscl.put("coiDisclosureNumber",disclosureNumber);
    hmDiscl.put("sequenceNumber",sequenceNumber);
    Vector vecProposal=(Vector)request.getSession().getAttribute("proposalListForAttachment");
    Vector vecInstProposal=  (Vector)request.getSession().getAttribute("getInstProposals");
    Vector vecAward=(Vector)request.getSession().getAttribute("allAwardList");
    Vector vecProtocol=(Vector)request.getSession().getAttribute("protocolProjectListList");
    Vector vecIacucProtocol=(Vector)request.getSession().getAttribute("getAllIACUCProtocolList");   

  if(vecProposal != null &&vecProposal.size()>0)
    {
        for(int i=0;i<vecProposal.size();i++)
        {

                     CoiProposalBean coipropbean=
                    (CoiProposalBean)vecProposal.get(i);
                     //CoiProposalBean propbean=new CoiProposalBean();
                     ComboBoxBean propbean = new ComboBoxBean();
                     String title=coipropbean.getProposalNumber()+" : "+(String)coipropbean.getTitle();
                     propbean.setDescription(title);
                     propbean.setCode(coipropbean.getProposalNumber());
                     pjtlist.add(propbean);
        }
    }
      if(vecInstProposal != null &&vecInstProposal.size()>0)
    {
        for(int i=0;i<vecInstProposal.size();i++)
        {

                     CoiProposalBean coipropbean=
                    (CoiProposalBean)vecInstProposal.get(i);
                     //CoiProposalBean propbean=new CoiProposalBean();
                     ComboBoxBean propbean = new ComboBoxBean();
                     String title=coipropbean.getProposalNumber()+" : "+(String)coipropbean.getTitle();
                     propbean.setDescription(title);
                     propbean.setCode(coipropbean.getProposalNumber());
                     pjtlist.add(propbean);
        }
    }
     if(vecAward != null &&vecAward.size()>0)
    {
        for(int i=0;i<vecAward.size();i++)
        {

                     CoiAwardInfoBean coiawardbean=
                    (CoiAwardInfoBean)vecAward.get(i);
                  //   CoiProposalBean propbean=new CoiProposalBean();
                     ComboBoxBean propbean = new ComboBoxBean();
                     String title=coiawardbean.getMitAwardNumber()+" : "+(String)coiawardbean.getAwardTitle();

                     propbean.setDescription(title);
                     propbean.setCode(coiawardbean.getMitAwardNumber());
                     pjtlist.add(propbean);
        }
    }

  if(vecProtocol != null &&vecProtocol.size()>0)
    {
        for(int i=0;i<vecProtocol.size();i++)
        {

                     CoiProtocolInfoBean coiprotobean=
                    (CoiProtocolInfoBean)vecProtocol.get(i);
                     //CoiProposalBean propbean=new CoiProposalBean();
                     ComboBoxBean propbean = new ComboBoxBean();
                     String title=coiprotobean.getProtocolNumber()+" : "+(String)coiprotobean.getTitle();
                     propbean.setDescription(title);
                     propbean.setCode(coiprotobean.getProtocolNumber());
                     pjtlist.add(propbean);
                    
        }
    }
   if(vecIacucProtocol != null &&vecIacucProtocol.size()>0)
    {
        for(int i=0;i<vecIacucProtocol.size();i++)
        {

                     CoiProtocolInfoBean coiprotobean=
                    (CoiProtocolInfoBean)vecIacucProtocol.get(i);
                   //  CoiProposalBean propbean=new CoiProposalBean();
                      ComboBoxBean propbean = new ComboBoxBean();
                     String title=coiprotobean.getProtocolNumber()+" : "+(String)coiprotobean.getTitle();
                     propbean.setDescription(title);
                     propbean.setCode(coiprotobean.getProtocolNumber());
                     pjtlist.add(propbean);
        }
    }

    if(pjtlist.size() == 0) {
                hmData.clear();
                hmData.put("coiDisclosureNumber",disclosureNumber);
                hmData.put("sequenceNumber",sequenceNumber);
                WebTxnBean webTxn = new WebTxnBean();
                Hashtable DisclPjcts = (Hashtable) webTxn.getResults(request, "getAllAnnualPjctForAttachment", hmData);
                Vector pjcts = (Vector) DisclPjcts.get("getAllAnnualPjctForAttachment");
                ComboBoxBean propbean;
                if(pjcts!=null){
                for (Iterator it = pjcts.iterator(); it.hasNext();) {
                    propbean = new ComboBoxBean();
                    CoiDisclosureBean disclBean=(CoiDisclosureBean)it.next();
                    String description = disclBean.getModuleItemKey() + " : " + disclBean.getPjctName();
                    propbean.setDescription(description);
                    propbean.setCode(disclBean.getModuleItemKey());
                    pjtlist.add(propbean);
                }}
              //  request.setAttribute("projectList", pjtlist);
    }

request.getSession().setAttribute("list", pjtlist);
 request.setAttribute("projectList", pjtlist);
}
           }
 //code for getting disclosure projects ends

        if (actionMapping.getPath().equals("/saveAttachmentsCoiv2")) {
            session = request.getSession();
            int discDetNo=-1;
            WebTxnBean webTxn=new WebTxnBean();
            HashMap hmp=new HashMap();
            HashMap finEntMap = new HashMap();
            PersonInfoBean loggedPer = (PersonInfoBean) session.getAttribute(SessionConstants.LOGGED_IN_PERSON);
            String pid=loggedPer.getPersonID();
           //code for feting  FE details starts
           Boolean frmAttchmnt=false;
           if(session.getAttribute("fromAttachment")!=null){
             frmAttchmnt=(Boolean)session.getAttribute("fromAttachment");
           }
           if(frmAttchmnt==true){
           if(session.getAttribute("checkPrint")!=null &&session.getAttribute("checkPrint").equals("approvedDisclosureview")){
             finEntMap.put("coidisclosureNumber",disclosureNumber);
             finEntMap.put("disclSeqnumber",null);
           }
           else
           {
             finEntMap.put("coidisclosureNumber",disclosureNumber);
             finEntMap.put("disclSeqnumber",sequenceNumber);
           }
           finEntityCombo = getFinEntityForAttachments(request, finEntMap);
           }
           else{
           if(request.getSession().getAttribute("disclosurePersonId")!=null)
            {
               finEntMap.put("personId",request.getSession().getAttribute("disclosurePersonId"));
           }
           finEntityCombo = getFinEntity(request, finEntMap);
           }
           //code for feting  FE details ends
            request.setAttribute("FinEntForPerson", finEntityCombo);
            request.getSession().setAttribute("FinEntForPerson", finEntityCombo);
            Coiv2Attachment attachmentBean = (Coiv2Attachment) actionForm;
            operationType = request.getParameter("operationType");
            isViewer = request.getParameter("isViewer");
            attachmentBean.setDisclosureNumber(disclosureNumber);
            attachmentBean.setSequenceNumber(sequenceNumber);
            Coiv2AttachmentBean coiAttachmentBean = new Coiv2AttachmentBean();
            String docDescription= (String) request.getSession().getAttribute("docAttachDescription");
            BeanUtils.copyProperties(coiAttachmentBean, attachmentBean);
            coiAttachmentBean.setDocument(attachmentBean.getDocument());
            coiAttachmentBean.setProjectName(attachmentBean.getPjtName());
            coiAttachmentBean.setFileBytes(attachmentBean.getDocument().getFileData());
            coiAttachmentBean.setFileName(attachmentBean.getDocument().getFileName());
            coiAttachmentBean.setFileNameHidden(attachmentBean.getDocument().getFileName());
            coiAttachmentBean.setDocCode(attachmentBean.getDocCode());
            coiAttachmentBean.setDescription(attachmentBean.getDescription().trim());
            coiAttachmentBean.setDocType(attachmentBean.getDocType());
            String entityNumber=attachmentBean.getEntity();

           if(entityNumber != null) {
               if(entityNumber.contains(":")) {
                   String splitList[] = entityNumber.split(":");

                   if(splitList != null && splitList.length > 0) {
                       entityNumber = splitList[0];
                   }
               }
           }

           String moduleItemKey=attachmentBean.getPjtName();

           if(moduleItemKey == null) {
               moduleItemKey = request.getParameter("pjtName");
           }
            if (attachmentBean.getEntityNumber() == 0) {
                coiAttachmentBean.setAcType("I");

                //getting disc detail number is needed only when a document is inserting
            if(((moduleItemKey!=null) && !moduleItemKey.equals("0"))&&((entityNumber!=null)&& !entityNumber.equals("0"))){
            hmp.put("coiDisclosureNumber",disclosureNumber);
            hmp.put("seqNumberOfAttachment",sequenceNumber);
            hmp.put("moduleitemkey",moduleItemKey);
            hmp.put("entityNo",entityNumber);

           Hashtable entityDetails = (Hashtable) webTxn.getResults(request,"getDiscDetailsNumber",hmp);
           hmp=(HashMap)entityDetails.get("getDiscDetailsNumber");
          discDetNo= Integer.parseInt((hmp.get("COI_DISC_DETAIL_NUMBER")).toString());
            }
            }
          coiAttachmentBean.setDiscDetailsNumber(discDetNo);
             docDescription=docDescription+coiAttachmentBean.getDescription();
            docDescription=docDescription+":";
            request.getSession().setAttribute("docAttachDescription",docDescription);

            coiAttachmentBean.setUpdateUser(loggedPer.getUserId());
            coiAttachmentBean.setUpdateTimeStamp(new Date());




          if (isTokenValid(request)) {
                returnType = coiAttachmentService.saveOrUpdateOrDelete(coiAttachmentBean, request);

       hmp.clear();


       String check="";
       Vector attDet=new Vector();
       if(session.getAttribute("checkPrint")!=null){
                 check =session.getAttribute("checkPrint").toString();
       }
       if(check.equals("approvedDisclosureview")){
                      HashMap hmap=new HashMap();
                       hmap.put("coiDisclosureNumber",disclosureNumber);
                       hmap.put("sequenceNumber",sequenceNumber);
                Hashtable DisclPjcts = (Hashtable) webTxn.getResults(request, "getAllAnnualPjctForAttachment", hmap);
                Vector pjcts = (Vector) DisclPjcts.get("getAllAnnualPjctForAttachment");
                                ComboBoxBean propbean;
                 CoiDisclosureBean disclBean;
                 if(pjcts!=null){
                for (Iterator it = pjcts.iterator(); it.hasNext();) {
                    propbean = new ComboBoxBean();
                    disclBean=(CoiDisclosureBean)it.next();
                    String description = disclBean.getModuleItemKey() + " : " + disclBean.getPjctName();
                    propbean.setDescription(description);
                    propbean.setCode(disclBean.getModuleItemKey());
                    pjtlist.add(propbean);
                }}
                  session.setAttribute("projectList", pjtlist);
                   Vector approvedAttDet=new Vector();
                // Vector attDet=new Vector();
                   hmp.put("disclosureNumber",disclosureNumber);
                   hmp.put("sequenceNumber",sequenceNumber);
                   Hashtable approvedDisclosureAttachments = (Hashtable) webTxn.getResults(request, "getDisclDocument", hmp);
                   approvedAttDet = (Vector) approvedDisclosureAttachments.get("getDisclDocument");
                    if (approvedAttDet != null && approvedAttDet.size() > 0) {
                     for (Iterator itr = approvedAttDet.iterator(); itr.hasNext();) {
                     Coiv2AttachmentBean coiv2AttachmentBean=(Coiv2AttachmentBean)itr.next();
                      attDet.add(coiv2AttachmentBean);
                     }

                    }
           }
       else{
                 
        hmp.put("disclosureNumber",disclosureNumber);
        hmp.put("sequenceNumber",sequenceNumber);
        CoiAttachmentService attachmentService = CoiAttachmentService.getInstance();
        attDet = attachmentService.getUploadDocumentForPerson(disclosureNumber,sequenceNumber,personId1);}
        if(attDet != null) {
         request.setAttribute("attachmentList", attDet);
        }
        else {
            request.removeAttribute("attachmentList");
            request.getSession().removeAttribute("attachmentList");
            request.setAttribute("message", false);
        }
      }
          else {
                returnType = "success";
            }
          String module=null;

            if(session.getAttribute("disclPjctModuleName")!=null)
                    {module=(String)session.getAttribute("disclPjctModuleName");}
             String loggedInPersonIdForCoi=(String)session.getAttribute("loggedInPersonIdForCoi");
            if (loggedInPersonIdForCoi!=null && !loggedInPersonIdForCoi.equalsIgnoreCase(persnId))
            {
                request.setAttribute("ToShowMY", "true");
            }
           LoadHeaderDetails(persnId,request);
           coiMenuDataSaved(disclosureNumber,sequenceNumber,persnId,request);//coi saved menu

        } else if (actionMapping.getPath().equals("/viewAttachment")) {
            operationType = request.getParameter("operationType");
            request.setAttribute("operationType", operationType);
            String enity = request.getParameter("entityNum");
            Vector list = (Vector) request.getSession().getAttribute("attachmentListInsession");
            //fix the issue view attachments throwing error statrs
            if(list==null)
            {
                disclosureNumber=(String)request.getSession().getAttribute("selectedAnnualDisclosureNo");
                list = coiAttachmentService.getUploadDocumentForPerson(disclosureNumber, sequenceNumber, personId1);
            }
            //fix the issue view attachments throwing error ends
            if(list!=null){
            for (Iterator it = list.iterator(); it.hasNext();) {
                Coiv2AttachmentBean attachment = (Coiv2AttachmentBean) it.next();
                if (attachment.getEntityNumber() == new Integer(enity).intValue()) {
                    request.setAttribute("downloadAttachmentBean", attachment);
                    break;
                }
            }}
            return actionMapping.findForward("success");
        } else {
            operationType = request.getParameter("operationType");
            request.setAttribute("operationType", operationType);
            returnType = "success";
        }
        CoiDisclosureBean currDisclosure = new CoiDisclosureBean();
        if (operationType != null && operationType.equals("MODIFY")) {
            String disclNumber = (String) request.getSession().getAttribute("DisclosureNumberInUpdateSession");
            Integer seqNumber = (Integer) request.getSession().getAttribute("currentSequence");
           if(disclNumber == null && session.getAttribute("currentSequence")!=null) {
              disclNumber=(String)session.getAttribute("COIDiscNumber");
              seqNumber=Integer.parseInt(session.getAttribute("currentSequence").toString()); 
           }
           if(seqNumber == null){
               seqNumber = (Integer)request.getSession().getAttribute("DisclSeqNumber");
           }
            currDisclosure.setCoiDisclosureNumber(disclNumber);
            if(seqNumber != null){
            currDisclosure.setSequenceNumber(seqNumber);
            }

        } else {
            currDisclosure = (CoiDisclosureBean) request.getSession().getAttribute("disclosureBeanSession");
        }
        request.setAttribute("operationType", operationType);
        request.setAttribute("isViewer", isViewer);
        if (isViewer != null && isViewer.equals("VIEWER")) {
            request.setAttribute("option", "attachments");
            request.setAttribute("userHasRight", true);
            returnType = "attachmentViewer";
        }
        BeanUtils.copyProperties(actionForm, new Coiv2Attachment());
        saveToken(request);
        return actionMapping.findForward(returnType);
    }
    private Vector getFinEntity(HttpServletRequest request, HashMap hmData) throws IOException, Exception {
        WebTxnBean webTxnBean = new WebTxnBean();
        Hashtable projectDetailsList = (Hashtable) webTxnBean.getResults(request, "getFinEntityListForPersonCoiv2", hmData);
        Vector finEntityList = (Vector) projectDetailsList.get("getFinEntityListCoiv2");
        Vector finEntityComboList = new Vector();
        if (finEntityList != null && !finEntityList.isEmpty()) {
            for (int i = 0; i < finEntityList.size(); i++) {
                DynaValidatorForm finEntity = (DynaValidatorForm) finEntityList.get(i);
                if (finEntity.get("statusCode").toString().equals("1")) {
                    String code = (String)finEntity.get("entityNumber");
                    String desc = (String) finEntity.get("entityName");
                    //String commentStr= (String) finEntity.get("commentStr");
                    CoiFinancialEntityBean boxBean = new CoiFinancialEntityBean();
                    boxBean.setCode(code);
                    boxBean.setDescription(desc);
                    //boxBean.setComment(commentStr);
                    finEntityComboList.add(boxBean);
                }
            }
        }
        request.getSession().setAttribute("FinEntForPerson", finEntityComboList);
        return finEntityComboList;
    }
     //function for feting all approved attachments FE starts
     private Vector getFinEntityForAttachments(HttpServletRequest request, HashMap hmData) throws IOException, Exception {
        WebTxnBean webTxnBean = new WebTxnBean();
        Hashtable projectDetailsList = (Hashtable) webTxnBean.getResults(request, "getFinEntityListForAttachment", hmData);
        Vector finEntityList = (Vector) projectDetailsList.get("getFinEntityListForAttachment");
        Vector finEntityComboListForAttachments = new Vector();
        if (finEntityList != null && !finEntityList.isEmpty()) {
            for (int i = 0; i < finEntityList.size(); i++) {
                DynaValidatorForm finEntity = (DynaValidatorForm) finEntityList.get(i);
               if (finEntity.get("statusCode").toString().equals("1")) {
                    String code = (String)finEntity.get("entityNumber");
                    String desc = (String) finEntity.get("entityName");
                    CoiFinancialEntityBean boxBean = new CoiFinancialEntityBean();
                    boxBean.setCode(code);
                    boxBean.setDescription(desc);
                    finEntityComboListForAttachments.add(boxBean);
               }
            }
        }
        request.getSession().setAttribute("FinEntForPerson", finEntityComboListForAttachments);
        return finEntityComboListForAttachments;
    }
 //function for feting all approved attachments FE ends
    public CoiDisclosureBean getApprovedDisclosureBean(String personId,HttpServletRequest request)throws Exception{
        HashMap hmData = new HashMap();
        hmData.put("personId", personId);
        WebTxnBean webTxn = new WebTxnBean();
        Vector apprvdDiscl = null;
        CoiDisclosureBean apprvdDisclosureBean = new CoiDisclosureBean();
        Hashtable apprvdDisclDet = (Hashtable) webTxn.getResults(request, "getApprovedDisclosure", hmData);
        apprvdDiscl = (Vector) apprvdDisclDet.get("getApprovedDisclosure");
        if (apprvdDiscl != null && apprvdDiscl.size() > 0) {
            apprvdDisclosureBean = (CoiDisclosureBean) apprvdDiscl.get(0);
            request.setAttribute("apprvdDisclosureBean", apprvdDisclosureBean);
            request.getSession().setAttribute("disclosureBeanSession", apprvdDisclosureBean);
        }
        return apprvdDisclosureBean;
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
}
