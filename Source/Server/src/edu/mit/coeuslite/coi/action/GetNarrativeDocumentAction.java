/*
 * GetNarrativeDocumentAction.java
 *
 * Created on 28 February 2006, 18:54
 */

package edu.mit.coeuslite.coi.action;

/**
 *
 * @author  mohann
 */

import edu.mit.coeus.propdev.bean.ProposalNarrativePDFSourceBean;
import edu.mit.coeus.propdev.bean.web.GetNarrativeDocumentBean;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import javax.servlet.ServletOutputStream;
import org.apache.struts.action.ActionForm;


public class GetNarrativeDocumentAction extends COIBaseAction {
    //private WebTxnBean webTxnBean ;
    //private HttpServletRequest request;
    
    /** Creates a new instance of GetNarrativeDocumentAction */
    public GetNarrativeDocumentAction() {
        
    }
    /*
    *Fix # 2866
    *change method name from performExecute to performExecuteCOI
    */
    
    public ActionForward performExecuteCOI(ActionMapping actionMapping,ActionForm actionForm,
    HttpServletRequest request, HttpServletResponse response) throws Exception {
        //this.request = request;
        
        String proposalNumber = request.getParameter("proposalNumber");
        String moduleNumber = request.getParameter("moduleNumber");
        String docType = request.getParameter("documentType");
        
//        HttpSession session = request.getSession();
//        UserInfoBean userInfoBean = (UserInfoBean)session.getAttribute("user"+session.getId());
//        String userId = userInfoBean.getUserId();
//        if(userId == null || userId.equals("")){
            //session expired
//        }
        //Check if user has right to view this narrative
        //Commented with COEUSDEV-308 since hasRightToView doesnot have any usages.
//        boolean hasRightToView = hasRightToViewNarrative(
//        proposalNumber, moduleNumber, userId, request);
        GetNarrativeDocumentBean narrativeDocBean = new GetNarrativeDocumentBean();
        ProposalNarrativePDFSourceBean proposalNarrativePDFSourceBean =
        new ProposalNarrativePDFSourceBean();
        proposalNarrativePDFSourceBean.setProposalNumber(proposalNumber);
        proposalNarrativePDFSourceBean.setModuleNumber(Integer.parseInt(moduleNumber));
        if(docType.equalsIgnoreCase("PDF")){
            proposalNarrativePDFSourceBean = narrativeDocBean.
            getNarrativePDF(proposalNarrativePDFSourceBean);
        }
        else{            
            proposalNarrativePDFSourceBean = narrativeDocBean.
            getNarrativeSource(proposalNarrativePDFSourceBean);
        }
        if(proposalNarrativePDFSourceBean!=null &&
        proposalNarrativePDFSourceBean.getFileBytes() != null){
            byte[] narrativeData = proposalNarrativePDFSourceBean.getFileBytes();
            ByteArrayOutputStream BytesOut = new ByteArrayOutputStream();
            ObjectOutputStream objectOut = new ObjectOutputStream(BytesOut);
            objectOut.write(narrativeData);
            objectOut.flush();
            objectOut.close();
            BytesOut.flush();
            BytesOut.close();
            byte[] buffer = null;
            buffer = BytesOut.toByteArray();
            ByteArrayInputStream bais = new ByteArrayInputStream(buffer);
            ObjectInputStream ois = new ObjectInputStream(bais);
            if (docType.equals("PDF")) {
                response.setContentType("application/pdf");
            }
            else {
                response.setContentType("application/msword");
            }
            ServletOutputStream outs = response.getOutputStream();
            int l_chunk;
            while((l_chunk = ois.read()) != -1) {
                outs.write(l_chunk);
            }
            outs.flush();
            outs.close();
            buffer = null;
        }
        
        return actionMapping.findForward("success");
    }
    
    /*
     * Check if user has right to view this narrative
     */
    //Commented this function with COEUSDEV 308 - since the function fn_user_can_view_narr_module is discarded. 
//    private boolean hasRightToViewNarrative(String proposalNumber,
//    String moduleNumber,String userId, HttpServletRequest request) throws Exception{
//        HashMap  hmData =  new HashMap();
//        hmData.put("userId", userId);
//        hmData.put("proposalNumber", proposalNumber);
//        hmData.put("moduleNumber", moduleNumber);
//        WebTxnBean webTxnBean =  new WebTxnBean();
//        boolean rightExists = false;
//        Hashtable htRightExists = (Hashtable)webTxnBean.getResults(request,"canViewNarrative",hmData);
//        HashMap hmRightExists = (HashMap)htRightExists.get("canViewNarrative");
//        int canView = Integer.parseInt(hmRightExists.get("canView").toString());
//        if(canView >0 ){
//            rightExists = true;
//        }
//        return rightExists;
//    }
}
