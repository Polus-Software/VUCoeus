/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.mit.coeuslite.coiv2.schema.generator;

import edu.mit.coeus.exception.CoeusException;
import edu.mit.coeus.utils.dbengine.DBException;
import edu.mit.coeus.xml.generator.CoeusXMLGenrator;
import edu.mit.coeus.xml.generator.ReportBaseStream;
import edu.mit.coeus.xml.generator.ReportReaderConstants;
import edu.mit.coeuslite.coiv2.beans.CoiAnnualProjectEntityDetailsBean;
import edu.mit.coeuslite.coiv2.beans.CoiDisclosureBean;
import edu.mit.coeuslite.coiv2.beans.CoiProjectEntityDetailsBean;
import edu.mit.coeuslite.coiv2.beans.CoiQuestionAnswerBean;
import edu.mit.coeuslite.coiv2.beans.Coiv2AttachmentBean;
import edu.mit.coeuslite.coiv2.beans.Coiv2NotesBean;
import edu.mit.coeuslite.coiv2.formbeans.Coiv2Notes;
import edu.mit.coeuslite.coiv2.jaxbclasses.COIDISCLOSUREType;
import edu.mit.coeuslite.coiv2.jaxbclasses.COIDISCLPROJECTSType;
import edu.mit.coeuslite.coiv2.jaxbclasses.COIDOCUMENTSType;
import edu.mit.coeuslite.coiv2.jaxbclasses.COINOTEPADENTRYTYPEType;
import edu.mit.coeuslite.coiv2.jaxbclasses.ObjectFactory;
import edu.mit.coeuslite.coiv2.jaxbclasses.QUESTIONType;
import edu.mit.coeuslite.coiv2.services.GetDisclosureForPrint;
import edu.mit.coeuslite.utils.bean.WebTxnBean;
import java.math.BigDecimal;

import java.util.HashMap;
import java.util.Hashtable;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.http.HttpServletRequest;
import org.w3c.dom.Document;

import java.util.Calendar;
import java.util.Iterator;
import java.util.Map;
import java.sql.Date;

/**
 *
 * @author Mr
 */
public class CoiV2Stream extends ReportBaseStream {

    public static final String ENTITIES = "ENTITIES";
    public static final String NOTES = "NOTES";
    public static final String ATTACHMENTS = "ATTACHMENTS";
    public static final String QUESTIONNAIRE = "QUESTIONNAIRE";
    private ObjectFactory objFactory;
    private CoeusXMLGenrator xmlGenerator;
    private final HashMap status = new HashMap();
    private static final String packageName = "edu.mit.coeuslite.coiv2.jaxbclasses";

    public CoiV2Stream() {
        objFactory = new ObjectFactory();
        status.put("P", "Pending");
        status.put("M", "Merged");
        status.put("S", "Submitted");
        status.put("T", "Temporary");
        status.put("V", "Void");
        xmlGenerator = new CoeusXMLGenrator();
    }

    @Override
    public Document getStream(Hashtable params) throws DBException, CoeusException {

        COIDISCLOSUREType cOIDISCLOSUREType = null;

        try {
            String disclosureNumber = (String) params.get(ReportReaderConstants.DISCLOSURE_NUMBER);
            String sequenceNumber = (String) params.get(ReportReaderConstants.SEQUENCE_NUMBER);
            String personID = (String) params.get(ReportReaderConstants.PERSONID);
            String moduleString = (String) params.get(ReportReaderConstants.MODULE_CODE);
            HttpServletRequest request = (HttpServletRequest) params.get(ReportReaderConstants.REQUEST);
            cOIDISCLOSUREType = getDisclosureType(request, disclosureNumber, sequenceNumber, personID, moduleString);
        } catch (Exception ex) {
            ex.printStackTrace();
            Logger.getLogger(CoiV2Stream.class.getName()).log(Level.SEVERE, null, ex);
        }


        return xmlGenerator.marshelObject(cOIDISCLOSUREType, packageName);



    }

    @Override
    public Object getObjectStream(Hashtable params) throws DBException, CoeusException {
        COIDISCLOSUREType cOIDISCLOSUREType = null;
        try {
            String disclosureNumber = (String) params.get(ReportReaderConstants.DISCLOSURE_NUMBER);
            String sequenceNumber = (String) params.get(ReportReaderConstants.SEQUENCE_NUMBER);
            String personID = (String) params.get(ReportReaderConstants.PERSONID);
            String moduleString = (String) params.get(ReportReaderConstants.MODULE_CODE);
            HttpServletRequest request = (HttpServletRequest) params.get(ReportReaderConstants.REQUEST);
            cOIDISCLOSUREType = getDisclosureType(request, disclosureNumber, sequenceNumber, personID, moduleString);
        } catch (Exception ex) {
            ex.printStackTrace();
            Logger.getLogger(CoiV2Stream.class.getName()).log(Level.SEVERE, null, ex);
        }
        return cOIDISCLOSUREType;
    }

    private COIDISCLOSUREType getDisclosureType(HttpServletRequest request, String disclNumber, String sequNumber, String perID, String module) throws DBException, CoeusException, Exception {


         COIDISCLOSUREType cOIDISCLOSUREType = objFactory.createCOIDISCLOSURE();
        try{

        GetDisclosureForPrint getdisclosureForPrint = new GetDisclosureForPrint();

        String disclosureNumber = disclNumber;
        String sequenceNumber = sequNumber;
        String personID = perID;
        String moduleString = module;
        int moduleInt = Integer.parseInt(moduleString);


//        WebTxnBean webTxn = new WebTxnBean();
//        HashMap hmData = new HashMap();
//        hmData.put("personId", personID);
//        Hashtable discl = (Hashtable) webTxn.getResults(request, "getAnnDisclPersonCoiv2", hmData);
//        Vector finDiscl = (Vector) discl.get("getDisclosureDetails");
//
//
//            for (Iterator it = finDiscl.iterator(); it.hasNext();) {
//                CoiDisclosureBean bean = (CoiDisclosureBean) it.next();
//
//                //CReating root element
//
//
//
//                cOIDISCLOSUREType.setCERTIFICATIONTEXT(checkNull(bean.getCertificationText()));
//                Calendar calcertification = Calendar.getInstance();
//                calcertification.setTime(bean.getCertificationTimestamp());
//                cOIDISCLOSUREType.setCERTIFICATIONTIMESTAMP(calcertification);
//                cOIDISCLOSUREType.setCERTIFIEDBY(checkNull(bean.getCertifiedBy()));
//                cOIDISCLOSUREType.setCOIDISCLOSURENUMBER(checkNull(bean.getCoiDisclosureNumber()));
//
//
//                BigDecimal dISCLOSUREDISPOSITIONCODE = new BigDecimal(bean.getDisclosureDispositionCode());
//
//                //creating disposition status
//                OSPCOIDISPOSITIONSTATUSType dispType=new ObjectFactory().createOSPCOIDISPOSITIONSTATUSType();
//                dispType.setCOIDISPOSITIONCODE(new BigDecimal(bean.getDisclosureDispositionCode()));
//                dispType.setDESCRIPTION(bean.getDispositionStatus());
//
//                //setting disposition status
//                cOIDISCLOSUREType.setOSPCOIDISPOSITIONSTATUS(dispType);
//                Calendar calExpiration = Calendar.getInstance();
//                calExpiration.setTime(bean.getExpirationDate());
//                cOIDISCLOSUREType.setEXPIRATIONDATE(calExpiration);
//
//                BigDecimal moduleCode = new BigDecimal(bean.getModuleCode());
//                cOIDISCLOSUREType.setMODULECODE(moduleCode);
//                cOIDISCLOSUREType.setPERSONID(checkNull(bean.getPersonId()));
//
//                BigDecimal sEQUENCENUMBER = new BigDecimal(bean.getSequenceNumber());
//                cOIDISCLOSUREType.setSEQUENCENUMBER(sEQUENCENUMBER);
//                Calendar calupdate = Calendar.getInstance();
//                calupdate.setTime(bean.getUpdateTimestamp());
//                cOIDISCLOSUREType.setUPDATETIMESTAMP(calupdate);
//                cOIDISCLOSUREType.setUPDATEUSER(checkNull(bean.getUserName()));
//            }


        Map disclosureMap = new HashMap();
        disclosureMap = getdisclosureForPrint.getADisclosure(request, disclosureNumber, sequenceNumber, personID, moduleInt);

        Vector financialEntities = (Vector) disclosureMap.get(ENTITIES);
        Vector notes = (Vector) disclosureMap.get(NOTES);
        Vector attachments = (Vector) disclosureMap.get(ATTACHMENTS);
        Vector questionnaire = (Vector) disclosureMap.get(QUESTIONNAIRE);


        //process o0f setting financial entity

        if(financialEntities!=null){

            if(financialEntities.size()>=1){

        for (Iterator finacial = financialEntities.iterator(); finacial.hasNext();) {


            CoiAnnualProjectEntityDetailsBean element = (CoiAnnualProjectEntityDetailsBean) finacial.next();


            COIDISCLPROJECTSType cOIDISCLPROJECTSType = objFactory.createCOIDISCLPROJECTSType();
            cOIDISCLPROJECTSType.setCOIDISCLOSURENUMBER(checkNull(element.getCoiDisclosureNumber()));

            if (element.getCoiProjectEndDate() != null) {
                Calendar calEnd = Calendar.getInstance();
                calEnd.setTime(element.getCoiProjectEndDate());
                cOIDISCLPROJECTSType.setCOIPROJECTENDDATE(calEnd);

            }


            if (element.getCoiProjectFundingAmount() != null) {
                BigDecimal fundingAmount = new BigDecimal(element.getCoiProjectFundingAmount());
                cOIDISCLPROJECTSType.setCOIPROJECTFUNDINGAMOUNT(fundingAmount);
            }

            cOIDISCLPROJECTSType.setCOIPROJECTID(checkNull(element.getCoiProjectId()));
            cOIDISCLPROJECTSType.setCOIPROJECTROLE(checkNull(element.getCoiProjectRole()));
            cOIDISCLPROJECTSType.setCOIPROJECTSPONSOR(checkNull(element.getCoiProjectSponsor()));

            if (element.getCoiProjectStartDate() != null) {
                Calendar calStart = Calendar.getInstance();
                calStart.setTime(element.getCoiProjectStartDate());
                cOIDISCLPROJECTSType.setCOIPROJECTSTARTDATE(calStart);

            }

            cOIDISCLPROJECTSType.setCOIPROJECTTITLE(checkNull(element.getCoiProjectTitle()));
            cOIDISCLPROJECTSType.setCOIPROJECTTYPE(checkNull(element.getCoiProjectType()));

            if (element.getEntitySequenceNumber() != null) {
                BigDecimal sequnceNumber = new BigDecimal(element.getEntitySequenceNumber());
                cOIDISCLPROJECTSType.setSEQUENCENUMBER(sequnceNumber);
            }

            if (element.getUpdateTimeStamp() != null) {

                Calendar calupdate = Calendar.getInstance();
                calupdate.setTime(element.getUpdateTimeStamp());
                cOIDISCLPROJECTSType.setUPDATETIMESTAMP(calupdate);

            }


            cOIDISCLPROJECTSType.setUPDATEUSER(checkNull(element.getUpdateUser()));


            cOIDISCLOSUREType.getCOIDISCLPROJECTS().add(cOIDISCLPROJECTSType);




        }

        }//null checking ends
        }//size checing ends


        //settng notes to disclosure

        if(notes!=null){
        if(notes.size()>0){
        for (Iterator notesIT = notes.iterator(); notesIT.hasNext();) {
            Coiv2NotesBean element = (Coiv2NotesBean) notesIT.next();


            COINOTEPADENTRYTYPEType cOINOTEPADENTRYTYPEType = objFactory.createCOINOTEPADENTRYTYPEType();
            cOINOTEPADENTRYTYPEType.setCOINOTEPADENTRYCODE(checkNull(element.getCoiNotepadEntryTypeCode()));
            cOINOTEPADENTRYTYPEType.setDESCRIPTION(checkNull(element.getComments()));

            cOINOTEPADENTRYTYPEType.setUPDATEUSER(checkNull(element.getUpdateUser()));


            cOIDISCLOSUREType.getCOINOTEPAD().add(cOINOTEPADENTRYTYPEType);


        }

        }// null checking ends for notes
        }//size checking for notes ends


if(attachments!=null){
    if(attachments.size()>0){
        for (Iterator attachmentsIT = attachments.iterator(); attachmentsIT.hasNext();) {
            Coiv2AttachmentBean element = (Coiv2AttachmentBean) attachmentsIT.next();

            COIDOCUMENTSType cOIDOCUMENTSType = objFactory.createCOIDOCUMENTSType();
            cOIDOCUMENTSType.setCOIDISCLOSURENUMBER(checkNull(element.getDisclosureNumber()));
            cOIDOCUMENTSType.setDESCRIPTION(checkNull(element.getDescription()));

            if (element.getEntityNumber() > 0) {

                BigDecimal entityNumber = new BigDecimal(element.getEntityNumber());
                cOIDOCUMENTSType.setENTRYNUMBER(entityNumber);
            }

            cOIDOCUMENTSType.setFILENAME(checkNull(element.getFileName()));

            if (element.getSequenceNumber() > 0) {
                BigDecimal sequnceNumber = new BigDecimal(element.getSequenceNumber());
                cOIDOCUMENTSType.setSEQUENCENUMBER(sequnceNumber);
            }

            if (element.getUpdateTimeStamp() != null) {
                Calendar cal = Calendar.getInstance();
                cal.setTime(element.getUpdateTimeStamp());
                cOIDOCUMENTSType.setUPDATETIMESTAMP(cal);

            }

            cOIDOCUMENTSType.setUPDATEUSER(checkNull(element.getUpdateUser()));

            cOIDISCLOSUREType.getCOIDOCUMENTS().add(cOIDOCUMENTSType);

        }
}//null checking ends for notes
}//size checking ends for notes

if(questionnaire!=null){
    if(questionnaire.size()>0){
        for (Iterator questionnaireIT = questionnaire.iterator(); questionnaireIT.hasNext();) {
            CoiQuestionAnswerBean element = (CoiQuestionAnswerBean) questionnaireIT.next();

            QUESTIONType question = objFactory.createQUESTIONType();
            question.setQUESTION(checkNull(element.getQuestion()));
            question.setANSWER(checkNull(element.getAnswer()));

            cOIDISCLOSUREType.setQUESTION(question);


        }

}//null checking ends for questionnaire


}//size checking ends for notes

        }catch(Exception e){
            e.printStackTrace();
            CoeusException coeusException =new CoeusException();
            throw coeusException;
        }
        

        return cOIDISCLOSUREType;
    }

    private String checkNull(String obj) {
        return obj == null ? "" : obj;
    }
}
