/*
 * PrintCertificationAction.java
 *
 * Created on December 21, 2006, 4:19 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package edu.utk.coeuslite.propdev.action;

import edu.mit.coeus.bean.CoeusReportGroupBean;
import edu.mit.coeus.propdev.bean.ProposalDevelopmentFormBean;
import edu.mit.coeus.propdev.bean.ProposalDevelopmentTxnBean;
import edu.mit.coeus.utils.CoeusProperties;
import edu.mit.coeus.utils.CoeusPropertyKeys;
import edu.mit.coeus.utils.CoeusVector;
import edu.mit.coeus.utils.TypeConstants;
import edu.mit.coeus.utils.document.DocumentBean;
import edu.mit.coeus.utils.document.DocumentConstants;
import edu.mit.coeus.utils.document.DocumentIdGenerator;
import edu.mit.coeus.xml.conf.ReportConfigEngine;
import edu.mit.coeus.xml.generator.ReportReaderConstants;
import edu.utk.coeuslite.propdev.bean.EPSProposalHeaderBean;
import java.io.File;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;
import java.util.Vector;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

/**
 *
 * @author sharathk
 */
public class PrintCertificationAction  extends ProposalBaseAction{
    
    public static final String READER_CLASS = "edu.mit.coeus.xml.generator.ReportReader";
    
    /** Creates a new instance of PrintCertificationAction */
    public PrintCertificationAction() {
    }
    
    public ActionForward performExecute(ActionMapping actionMapping, ActionForm actionForm,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
        ActionForward actionForward = null;
        
        HttpSession session = request.getSession();
        
        String reportId = "Proposal/PrintCertification";
        CoeusReportGroupBean.Report report = ReportConfigEngine.getReport(reportId);
        String reportDir = CoeusProperties.getProperty(CoeusPropertyKeys.REPORT_GENERATED_PATH);
        String reportPath = session.getServletContext().getRealPath("/")+File.separator+reportDir;
        String repName = report.getDispValue().replace(' ','_');
        
        EPSProposalHeaderBean ePSProposalHeaderBean = (EPSProposalHeaderBean)request.getSession().getAttribute("epsProposalHeaderBean");
        String proposalNumber  = ePSProposalHeaderBean.getProposalNumber();
        
        ProposalDevelopmentTxnBean proposalDevelopmentTxnBean = new ProposalDevelopmentTxnBean();
        ProposalDevelopmentFormBean proposalDevelopmentFormBean = proposalDevelopmentTxnBean.getProposalDevelopmentDetails(proposalNumber, TypeConstants.DISPLAY_MODE);
        
        String personId, personName, investigatorFlag = null;
        personId = (String) session.getAttribute("proposalInvPersonId");
        personName = (String)session.getAttribute("proposalInvName");
        investigatorFlag = ePSProposalHeaderBean.getPersonId().equals(personId) ? "Y" : "N";
        
        CoeusVector coeusVector = new CoeusVector();
        
        Vector vecAll = new Vector();
        Vector vec = new Vector();
        vec.add(personId);
        vec.add(personName);
        vec.add(investigatorFlag);
        vecAll.add(vec);
        
        coeusVector.add(proposalDevelopmentFormBean);
        coeusVector.add(vecAll);
        
        Hashtable hashtable = new Hashtable();
        hashtable.put(ReportReaderConstants.REPORT_ID, reportId);
        hashtable.put("DATA", coeusVector);
        
        Map map = new HashMap();
        map.put(ReportReaderConstants.REPORT_ID, reportId);
        map.put(ReportReaderConstants.REPOORT_PATH, reportPath);
        map.put(ReportReaderConstants.REPORT_NAME, repName);
        map.put(ReportReaderConstants.REPORT_PARAMS, hashtable);
        map.put(DocumentConstants.READER_CLASS, READER_CLASS);
        
        DocumentBean documentBean = new DocumentBean();
        documentBean.setParameterMap(map);
        
        DocumentIdGenerator documentIdGenerator = new DocumentIdGenerator();
        String documentId = documentIdGenerator.generateDocumentId();
        request.getSession().setAttribute(documentId, documentBean);
        
        actionForward = new ActionForward("/StreamingServlet?"+DocumentConstants.DOC_ID+"="+documentId,true);
        
        return actionForward;
    }
}
