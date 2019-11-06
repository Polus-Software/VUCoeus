/*
 * ProposalPrintAction.java
 *
 * Created on August 11, 2006, 11:52 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package edu.utk.coeuslite.propdev.action;

import edu.mit.coeus.bean.PersonInfoBean;
import edu.mit.coeus.propdev.bean.ProposalDevelopmentTxnBean;
import edu.mit.coeus.s2s.bean.FormInfoBean;
import edu.mit.coeus.s2s.bean.OpportunityInfoBean;
import edu.mit.coeus.s2s.bean.S2SHeader;
import edu.mit.coeus.s2s.bean.S2SSubmissionDataTxnBean;
import edu.mit.coeus.sponsormaint.bean.SponsorTemplateBean;
import edu.mit.coeus.utils.CoeusVector;
import edu.mit.coeus.utils.KeyConstants;
import edu.mit.coeus.utils.document.*;
import edu.mit.coeuslite.utils.*;
import edu.mit.coeuslite.utils.bean.WebTxnBean;
import edu.wmc.coeuslite.utils.DynaBeanList;
import java.util.*;
import java.util.Hashtable;
import java.util.List;
import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.struts.action.*;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;
import org.apache.struts.config.*;
import org.apache.struts.config.ModuleConfig;
import org.apache.struts.util.*;

/**
 *
 * @author sharathk
 */
public class ProposalPrintAction extends ProposalBaseAction{
    
    /** Creates a new instance of ProposalPrintAction */
    public ProposalPrintAction() {
    }
    
     public ActionForward performExecute(ActionMapping actionMapping, ActionForm actionForm,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
        ActionForward actionForward = null;
        
        DynaBeanList dynaBeanList = (DynaBeanList)actionForm;
        
        if(dynaBeanList != null && dynaBeanList.getList() != null && dynaBeanList.getList().size() == 0){
            String proposalNumber = request.getParameter("proposalNumber");
            WebTxnBean webTxnBean = new WebTxnBean();
            Map map = new HashMap();
            map.put("proposalNumber", proposalNumber);
            Hashtable result = (Hashtable)webTxnBean.getResults(request, "getProposalSummaryDetails", map);
            List lstPropSummary = (List)result.get("getProposalSummaryDetails");
            DynaActionForm propSummaryForm  = (DynaActionForm)lstPropSummary.get(0);
            
            String sponsorCode, primeSponsorCode;
            sponsorCode = (String)propSummaryForm.get("sponsorCode");
            primeSponsorCode = (String)propSummaryForm.get("primeSponsorCode");
            
            //Requesting Print Forms.
            HttpSession session = request.getSession();
            PersonInfoBean personInfo = (PersonInfoBean)session.getAttribute(SessionConstants.LOGGED_IN_PERSON);
            ProposalDevelopmentTxnBean proposalDevelopmentTxnBean = new ProposalDevelopmentTxnBean();
            Hashtable hashtable = proposalDevelopmentTxnBean.getProposalPrintForms(
                        personInfo.getUserName(),proposalNumber,sponsorCode, primeSponsorCode);
            
            if(hashtable != null) {
                List packageData, pageData;
                packageData = (List)hashtable.get(KeyConstants.PACKAGE_DATA);
                pageData = (List)hashtable.get(KeyConstants.PAGE_DATA);
                
                //The Page Data is already Sorted by Package number.
                //Facilitates grouping and displaying at the client side.
                
                CoeusWebList pageList = new CoeusWebList();
                SponsorTemplateBean sponsorTemplateBean;
                DynaActionForm dynaActionForm;
                for(int index=0; index < pageData.size(); index++) {
                    sponsorTemplateBean = (SponsorTemplateBean)pageData.get(index);
                    dynaActionForm = getSponsorTemplateForm(request, sponsorTemplateBean);
                    pageList.add(dynaActionForm);
                }
                
                CoeusWebList packageList = new CoeusWebList();
                packageList.addAll(packageData);
                dynaBeanList.setBeanList(packageList);
                dynaBeanList.setList(pageList);
            }
            
            //Include S2S Available Forms For Printing - START
            S2SSubmissionDataTxnBean txnBean = new S2SSubmissionDataTxnBean();
            S2SHeader headerParam = new S2SHeader();
            String cfda = (String)propSummaryForm.get("cfdaCode");
            headerParam.setCfdaNumber(cfda);
            headerParam.setSubmissionTitle(proposalNumber);
            Object s2sDetails[] = txnBean.getS2SDetails(headerParam);
            
            CoeusWebList formList = new CoeusWebList();
            if(s2sDetails[1] instanceof CoeusWebList) {
                formList = (CoeusWebList)s2sDetails[1];
            }else {
                List lst = (List)s2sDetails[1];
                if(lst != null) {
                    formList.addAll(lst);
                }
            }
            
            DynaActionForm dynaFormInfobean;
            FormInfoBean formInfoBean;
            CoeusWebList dynaFormList = new CoeusWebList();
            GrantsGovAction grantsAction = new GrantsGovAction();
            if(formList != null) {
                for(int index = 0; index < formList.size(); index++) {
                    formInfoBean = (FormInfoBean)formList.get(index);
                    if(formInfoBean.isAvailable() && formInfoBean.isInclude()) {
                        // only Available and included Forms Can be printed
                        //So add only available Forms
                        dynaFormInfobean = grantsAction.getFormInfoBean(request, formInfoBean);
                        dynaFormList.add(dynaFormInfobean);
                    }
                }
            }
            
            DynaBeanList grantsDynaBeanList = new DynaBeanList();
            
            OpportunityInfoBean oppInfo = (OpportunityInfoBean)s2sDetails[0];
            GrantsGovAction grantsGovAction = new GrantsGovAction();
            DynaActionForm opportunityForm = grantsGovAction.getOpportunity(request, oppInfo);
            CoeusWebList list = new CoeusWebList();
            if(opportunityForm != null) {
                list.add(opportunityForm);
            }
            
            grantsDynaBeanList.setBeanList(list == null || list.size() == 0 ? null : list);
            grantsDynaBeanList.setList(dynaFormList == null || dynaFormList.size() == 0 ? null : dynaFormList);
            request.setAttribute("grantsGov", grantsDynaBeanList);
            //Include S2S Available Forms For Printing - END
            
            actionForward = actionMapping.findForward("success");
            
            Map mapMenuList = new HashMap();
            mapMenuList.put("menuItems",CoeusliteMenuItems.PROPOSAL_MENU_ITEMS);
            mapMenuList.put("menuCode",CoeusliteMenuItems.PROP_PRINT_MENU_CODE); 
            setSelectedMenuList(request, mapMenuList);
            
        }else {
            //Form submitted. Generate Report and Stream
            List lstData = new ArrayList();
            
            CoeusWebList coeusWebList = dynaBeanList.getList();
            DynaActionForm dynaActionForm;
            SponsorTemplateBean sponsorTemplateBean;
            Boolean boolPrint;
            Hashtable hashtable;
            String proposalNumber = (String)request.getSession().getAttribute("proposalNumber"+request.getSession().getId());
            for(int index = 0; index < coeusWebList.size(); index++) {
                dynaActionForm = (DynaActionForm)coeusWebList.get(index);
                boolPrint = (Boolean)dynaActionForm.get("print");
                if(boolPrint != null && boolPrint.booleanValue()){
                    //Selected for Print.
                    sponsorTemplateBean = getSponsorTemplateBean(dynaActionForm);
                    hashtable = new Hashtable();
                    hashtable.put("SPONSOR_CODE", dynaActionForm.get("sponsorCode"));
                    hashtable.put("PROPOSAL_NUMBER", proposalNumber);
                    hashtable.put("PACKAGE_NUMBER", dynaActionForm.get("packageNumber"));
                    hashtable.put("PAGE_NUMBER", dynaActionForm.get("pageNumber"));
                    hashtable.put("PAGE_DATA", sponsorTemplateBean);
                    lstData.add(hashtable);
                }//End If
            }//End For
            
            //Forward to Streaming Servlet.
//            request.setAttribute("PRINT_PROPOSAL", lstData);
//            request.setAttribute(DocumentConstants.READER_CLASS, "edu.mit.coeus.propdev.ProposalPrintReader");
//            actionForward = new ActionForward("/StreamingServlet");
            
            //Forward to Streaming Servlet.
            request.setAttribute("PRINT_PROPOSAL", lstData);
            request.setAttribute(DocumentConstants.READER_CLASS, "edu.mit.coeus.propdev.ProposalPrintReader");
            
            DocumentBean documentBean = new DocumentBean();
            Map map = new HashMap();
            map.put(DocumentConstants.READER_CLASS, "edu.mit.coeus.propdev.ProposalPrintReader");
            map.put("PRINT_PROPOSAL", lstData);
            documentBean.setParameterMap(map);
//          String docId = request.getSession().getId();
            String docId = DocumentIdGenerator.generateDocumentId();
            request.getSession().setAttribute(docId, documentBean);
            
            actionForward = new ActionForward("/StreamingServlet?"+DocumentConstants.DOC_ID+"="+docId,true);
        }
        
        return actionForward;
     }
     
     private DynaActionForm getSponsorTemplateForm(HttpServletRequest request, SponsorTemplateBean sponsorTemplateBean)throws IllegalAccessException, InstantiationException {
         ServletContext servletContext = request.getSession().getServletContext();
         ModuleConfig moduleConfig = RequestUtils.getModuleConfig(request, servletContext);
         FormBeanConfig formConfig = moduleConfig.findFormBeanConfig("sponsorTemplateForm");
         DynaActionFormClass dynaClass = DynaActionFormClass.createDynaActionFormClass(formConfig);
         DynaActionForm dynaActionForm = (DynaActionForm)dynaClass.newInstance();
         
         dynaActionForm.set("rowId", new Integer(sponsorTemplateBean.getRowId()));
         dynaActionForm.set("sponsorCode", sponsorTemplateBean.getSponsorCode());
         dynaActionForm.set("pageDescription", sponsorTemplateBean.getPageDescription());
         dynaActionForm.set("packageNumber", new Integer(sponsorTemplateBean.getPackageNumber()));
         dynaActionForm.set("pageNumber", new Integer(sponsorTemplateBean.getPageNumber()));
         
         return dynaActionForm;
     }
     
     private SponsorTemplateBean getSponsorTemplateBean(DynaActionForm dynaActionForm){
         SponsorTemplateBean sponsorTemplateBean = new SponsorTemplateBean();
         
         String sponsorCode, pageDescription;
         Integer rowId, packageNumber, pageNumber;
         Boolean print;
         
         rowId = (Integer)dynaActionForm.get("rowId");
         sponsorCode = (String)dynaActionForm.get("sponsorCode");
         pageDescription = (String)dynaActionForm.get("pageDescription");
         packageNumber = (Integer)dynaActionForm.get("packageNumber");
         pageNumber = (Integer)dynaActionForm.get("pageNumber");
         print = (Boolean)dynaActionForm.get("print");
         
         sponsorTemplateBean.setRowId(rowId.intValue());
         sponsorTemplateBean.setSponsorCode(sponsorCode);
         sponsorTemplateBean.setPageDescription(pageDescription);
         sponsorTemplateBean.setPackageNumber(packageNumber.intValue());
         sponsorTemplateBean.setPageNumber(pageNumber.intValue());
         
         return sponsorTemplateBean;
     }
     
}
