/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.mit.coeuslite.coiv2.actions;

import edu.dartmouth.coeuslite.coi.action.COIBaseAction;
import edu.mit.coeus.utils.CoeusProperties;
import edu.mit.coeus.utils.CoeusPropertyKeys;
import edu.mit.coeus.utils.UtilFactory;
import edu.mit.coeus.utils.document.DocumentBean;
import edu.mit.coeus.utils.document.DocumentConstants;
import edu.mit.coeus.utils.document.DocumentIdGenerator;
import edu.mit.coeus.xml.generator.ReportReaderConstants;

import edu.mit.coeuslite.coiv2.services.GetDisclosureForPrint;
import java.io.File;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import java.util.GregorianCalendar;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Vector;
import javax.servlet.http.HttpSession;

/**
 *
 * @author Mr.Biju
 */
public class PrintProcessCoiv2 extends COIBaseAction {

    public static final String ENTITIES = "ENTITIES";
    public static final String NOTES = "NOTES";
    public static final String ATTACHMENTS = "ATTACHMENTS";
    public static final String QUESTIONNAIRE = "QUESTIONNAIRE";
    public static final String DISCL_NUMBER = "DISCLOSURE_NUMBER";
    public static final String SEQ_NUMBER = "SEQUENCE_NUMBER";
    public static final String PERSONID = "PERSON_ID";
    public static final String MODULECODE = "MODULECODE";
    public static final String READER_CLASS = "edu.mit.coeus.xml.generator.ReportReader";

    @Override
    public ActionForward performExecuteCOI(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {

        ActionForward actionForward = null;

        HttpSession session = request.getSession();
        String reportID = "Coiv2print/Coiv2disclosureprint";
        String reportDir = CoeusProperties.getProperty(CoeusPropertyKeys.REPORT_GENERATED_PATH);
        String reportPath = session.getServletContext().getRealPath("/") + File.separator + reportDir;



        try {

            String disclosureNumber = request.getParameter("param1");
            String sequenceNumber = request.getParameter("param2");
            String personID = request.getParameter("param3");
            String moduleString = request.getParameter("param5");
            int moduleCode = 0;
            Map disclosureMap = new HashMap();

            GetDisclosureForPrint getDisclosure = new GetDisclosureForPrint();

            if (moduleString.equalsIgnoreCase("Protocol")) {
                moduleCode = 12;
            } else if (moduleString.equalsIgnoreCase("Proposal")) {
                moduleCode = 11;
            } else if (moduleString.equalsIgnoreCase("Award")) {
                moduleCode = 1;
            } else if (moduleString.equalsIgnoreCase("Miscellaneous")) {
                moduleCode = 0;
            }else if(moduleString.equalsIgnoreCase("Annual")){
                moduleCode = 13;
            }


            disclosureMap = getDisclosure.getADisclosure(request, disclosureNumber, sequenceNumber, personID, moduleCode);




            List disclosurelist = new ArrayList();

            Hashtable disclosureTable = new Hashtable();
            disclosureTable.put(ReportReaderConstants.REPORT_ID, reportID);
            disclosureTable.put("DATA", disclosureMap);

            disclosureTable.put(ReportReaderConstants.DISCLOSURE_NUMBER, disclosureNumber);
            disclosureTable.put(ReportReaderConstants.SEQUENCE_NUMBER, sequenceNumber);
            disclosureTable.put(ReportReaderConstants.PERSONID, personID);
            disclosureTable.put(ReportReaderConstants.MODULE_CODE, String.valueOf(moduleCode));
            disclosureTable.put(ReportReaderConstants.REQUEST, request);
            request.setAttribute(DocumentConstants.READER_CLASS, READER_CLASS);
            Map map = new HashMap();

            map.put(ReportReaderConstants.REPORT_ID, reportID);
            map.put(ReportReaderConstants.REPORT_PARAMS, disclosureTable);
            map.put(ReportReaderConstants.REPOORT_PATH, reportPath);
            map.put(ReportReaderConstants.REPORT_NAME, String.valueOf(disclosureNumber) + " Ver :" + String.valueOf(sequenceNumber));
            map.put(DocumentConstants.READER_CLASS, READER_CLASS);





            DocumentBean documentBean = new DocumentBean();
            documentBean.setParameterMap(map);

            DocumentIdGenerator documentIdGenerator = new DocumentIdGenerator();
            String documentId = documentIdGenerator.generateDocumentId();

            request.getSession().setAttribute(documentId, documentBean);

            actionForward = new ActionForward("/StreamingServlet?" + DocumentConstants.DOC_ID + "=" + documentId, true);


        } catch (Exception e) {
            e.printStackTrace();
            UtilFactory.log(e.getMessage(), e, "PrintProcessCoiv2", "performExecuteCOI()");



        }



        return actionForward;
    }
//    private static XMLGregorianCalendar getDate() {
//	try {
//	    return DatatypeFactory.newInstance().newXMLGregorianCalendar(new GregorianCalendar());
//	} catch (DatatypeConfigurationException e) {
//	    throw new Error(e);
//	}
//    }
//    
}
