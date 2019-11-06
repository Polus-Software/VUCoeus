
package edu.mit.coeus.action.propdev;

//import java.io.IOException;
//import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
//import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

//import java.sql.SQLException;
//import java.sql.Timestamp;
import java.util.*;
import java.io.*;
//import java.text.SimpleDateFormat;
import javax.servlet.*;
//import oracle.sql.BLOB;
//import java.sql.Blob;
//import java.sql.ResultSet;
//import oracle.sql.RAW;
import oracle.jdbc.driver.*;

//import org.apache.struts.action.ActionError;
//import org.apache.struts.action.ActionErrors;

import edu.mit.coeus.exception.CoeusException;
import edu.mit.coeus.utils.UtilFactory;
import edu.mit.coeus.utils.dbengine.DBException;
import edu.mit.coeus.action.common.CoeusActionBase;
import edu.mit.coeus.propdev.bean.web.GetNarrativeDocumentBean;
import edu.mit.coeus.propdev.bean.ProposalNarrativePDFSourceBean;
//import edu.mit.coeus.propdev.bean.ProposalNarrativeTxnBean;
//import edu.mit.coeus.user.bean.UserMaintDataTxnBean;
import edu.mit.coeus.utils.CoeusConstants;

/* CASE #748 Extend CoeusActionBase */
public class GetNarrativeDocumentAction extends CoeusActionBase {

    public ActionForward perform(ActionMapping mapping,
                                 ActionForm form,
                                 HttpServletRequest request,
                                 HttpServletResponse response) throws IOException, ServletException {
//        UtilFactory UtilFactory = new UtilFactory();
        boolean errorFlag = true;
        ActionForward actionForward = mapping.findForward(SUCCESS);
        try {
            System.out.println("Inside getNarrativePDFAction ******");
            String proposalNumber = request.getParameter("proposalNumber");
            String moduleNumber = request.getParameter("moduleNumber");
            String docType = request.getParameter("documentType");
            System.out.println("Proposal :::: " + proposalNumber);
            System.out.println("Module :::: " + moduleNumber );
            System.out.println("docType :::: " + docType );
             /* Check for userId in session object.  If it does not exist, forward
            to login page, setting requestedURL as this action class. */
            HttpSession session = request.getSession();
            String userId = (String)session.getAttribute(USER_ID);
            System.out.println("UserId: " + userId);
            if(userId == null)
            {
                request.setAttribute("requestedURL",
                    "displayProposal.do?proposalNumber="+proposalNumber);
                request.setAttribute("validationType", CoeusConstants.LOGIN_KEY);
                return mapping.findForward(CoeusConstants.LOGIN_KEY);
            }
            //userId = "lmrobin";
            //Check if user has right to view this narrative
            /*ProposalNarrativeTxnBean proposalNarrativeTxnBean = new ProposalNarrativeTxnBean();
            boolean hasRightToView =
                proposalNarrativeTxnBean.canViewNarrativeModule
                (userId, proposalNumber, moduleNumber);
            UserMaintDataTxnBean userMaintDataTxnBean = new UserMaintDataTxnBean();
                        //If no rights check for OSP right
            if(!hasRightToView){
                hasRightToView =
                  userMaintDataTxnBean.getUserHasOSPRight(userId, VIEW_ANY_PROPOSAL);
            }*/
            GetNarrativeDocumentBean narrativeDocBean = new GetNarrativeDocumentBean();
            boolean hasRightToView = narrativeDocBean.hasRightToViewNarrative(
                proposalNumber, moduleNumber, userId);
            if(!hasRightToView){
                throw new CoeusException(
                    "exceptionCode.Proposal.hasNoRightToViewNarrative");
            }
            ProposalNarrativePDFSourceBean proposalNarrativePDFSourceBean =
                new ProposalNarrativePDFSourceBean();
            proposalNarrativePDFSourceBean.setProposalNumber(proposalNumber);
            proposalNarrativePDFSourceBean.
                setModuleNumber(Integer.parseInt(moduleNumber));
            if(docType.equalsIgnoreCase("PDF")){
                proposalNarrativePDFSourceBean = narrativeDocBean.
                                getNarrativePDF(proposalNarrativePDFSourceBean);
                //response.setContentType ("application/pdf");
            }
            else{
                System.out.println("not pdf");
                proposalNarrativePDFSourceBean = narrativeDocBean.
                            getNarrativeSource(proposalNarrativePDFSourceBean);
                //response.setContentType ("application/msword");
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
              if (docType.equals("PDF"))
              {
                response.setContentType ("application/pdf");
              }
              else
              {
                response.setContentType ("application/msword");
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
            System.out.println("End of getNarrativeDocumentAction ******");
            errorFlag = false;
            System.out.println("End of getNarrativeDocumentAction ******");
        }catch(DBException DBEx){
           request.setAttribute("EXCEPTION", DBEx);
           //So that propdev nav bar will be displayed on Error Page.
           request.setAttribute("usePropDevErrorPage", "true");
        }catch(CoeusException CEx){
            UtilFactory.log(CEx.getMessage(), CEx, "GetNarrativeDocumentAction",
                  "perform");
           request.setAttribute("EXCEPTION", CEx);
           //So that propdev nav bar will be displayed on Error Page.
           request.setAttribute("usePropDevErrorPage", "true");
        }
        catch (Exception e) {
           e.printStackTrace();
           UtilFactory.log(e.getMessage(), e, "GetNarrativeDocumentAction", "perform");
           request.setAttribute("EXCEPTION", e);
           //So that propdev nav bar will be displayed on Error Page.
           request.setAttribute("usePropDevErrorPage", "true");
        }
        if(errorFlag){
            actionForward = mapping.findForward(FAILURE);
        }
        return actionForward;
    }
}

