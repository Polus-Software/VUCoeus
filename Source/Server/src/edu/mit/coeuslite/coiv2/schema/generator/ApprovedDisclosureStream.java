/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package edu.mit.coeuslite.coiv2.schema.generator;

import edu.mit.coeus.bean.PersonInfoBean;
import edu.mit.coeus.exception.CoeusException;
import edu.mit.coeus.utils.dbengine.DBException;
import edu.mit.coeus.xml.generator.ReportBaseStream;
import edu.mit.coeus.xml.generator.ReportReaderConstants;
import edu.mit.coeuslite.coiv2.beans.CoiAnnualProjectEntityDetailsBean;
import edu.mit.coeuslite.coiv2.beans.CoiDisclosureBean;
import edu.mit.coeuslite.coiv2.beans.CoiProjectEntityDetailsBean;
import edu.mit.coeuslite.coiv2.beans.CoiQuestionAnswerBean;
import edu.mit.coeuslite.coiv2.beans.Coiv2AttachmentBean;
import edu.mit.coeuslite.coiv2.beans.Coiv2NotesBean;
import edu.mit.coeuslite.coiv2.print.approved.CertificationQuestion;
import edu.mit.coeuslite.coiv2.print.approved.CoiDisclosure;
import edu.mit.coeuslite.coiv2.print.approved.Person;
import edu.mit.coeuslite.coiv2.print.approved.CoiDisclosureDetails;
import edu.mit.coeuslite.coiv2.print.approved.DisclosureDocuments;
import edu.mit.coeuslite.coiv2.print.approved.DisclosureNotes;
import edu.mit.coeuslite.coiv2.print.approved.impl.CertificationQuestionImpl;
import edu.mit.coeuslite.coiv2.print.approved.impl.CoiDisclosureDetailsImpl;
import edu.mit.coeuslite.coiv2.print.approved.impl.CoiDisclosureImpl;
import edu.mit.coeuslite.coiv2.print.approved.impl.DisclosureProjectsImpl;
import edu.mit.coeuslite.coiv2.print.approved.impl.PersonImpl;
import edu.mit.coeuslite.coiv2.services.CoiCommonService;
import edu.mit.coeuslite.coiv2.print.approved.DisclosureProjects;
import edu.mit.coeuslite.coiv2.print.approved.impl.DisclosureDocumentsImpl;
import edu.mit.coeuslite.coiv2.print.approved.impl.DisclosureNotesImpl;
import edu.mit.coeuslite.utils.bean.WebTxnBean;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Vector;
import org.w3c.dom.Document;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 *
 * @author Mr
 */
public class ApprovedDisclosureStream extends ReportBaseStream{

    public static final String ENTITIES = "ENTITIES";
    public static final String NOTES = "NOTES";
    public static final String ATTACHMENTS = "ATTACHMENTS";
    public static final String QUESTIONNAIRE = "QUESTIONNAIRE";
    //public static final String PROJECTS="PROJECTS";
    public static final String PERSON="PERSON";

    @Override
    public Object getObjectStream(Hashtable params) throws DBException, CoeusException {
       return getObjectStreamDocuument(params);
    }

    @Override
    public Document getStream(Hashtable params) throws DBException, CoeusException {
        return super.getStream(params);
    }


    private Object getObjectStreamDocuument(Hashtable params){


        Object object=null;
         String personID = (String) params.get(ReportReaderConstants.PERSONID);
         
        CoiDisclosure disclosure=new CoiDisclosureImpl();
        try{

            CoiDisclosureBean approvedDisclosure=(CoiDisclosureBean)params.get("approvedDisclosure");            
            String disclosureNumber = (String) params.get(ReportReaderConstants.DISCLOSURE_NUMBER);
            String sequenceNumber = (String)params.get(ReportReaderConstants.SEQUENCE_NUMBER);
            String expiryDate=(String) params.get(ReportReaderConstants.EXPIRYDATE);
            String fullName=(String) params.get(ReportReaderConstants.FULLNAME);
            String moduleCode=(String) params.get(ReportReaderConstants.MODULE_CODE);
            Map disclosureMap = new HashMap();
            disclosureMap=(HashMap)params.get("disclosureINFO");
            Vector PersonDetails=(Vector)disclosureMap.get(PERSON);
            if(PersonDetails!=null){
                Person reporter=new PersonImpl();
                PersonInfoBean person= (PersonInfoBean) PersonDetails.get(0);
                if(person!=null){
                reporter.setFullName(person.getFullName());
                reporter.setOffPhone(person.getOffPhone());
                reporter.setAddress1(person.getAddress1());
                reporter.setDirDept(person.getDirDept());
                reporter.setEmail(person.getEmail());
                reporter.setSchool(person.getSchool());
                }
                disclosure.getPerson().add(reporter);
            }


            disclosure.setDisclosureNumber(disclosureNumber);
            disclosure.setSequenceNumber(sequenceNumber);
            disclosure.setPersonID(personID);
            disclosure.setExpirationDate(expiryDate);
            disclosure.setModuleCode(moduleCode);
            //disclosure.setDisclosureDipositionStatus(approvedDisclosure.getDispositionStatus());
            //disclosure.setExpirationDate(approvedDisclosure.getExpirDate());
            //disclosure.setDisclosureStatus(approvedDisclosure.getDisclosureStatus());
            if(approvedDisclosure.getCertificationText()!=null && !approvedDisclosure.getCertificationText().equals("")){
            disclosure.setCertificationText(approvedDisclosure.getCertificationText());
            }
            if(approvedDisclosure.getCertificationTimestamp()!=null && !approvedDisclosure.getCertificationTimestamp().equals("")){
            disclosure.setCertificationTimestamp(approvedDisclosure.getCertificationTimestamp().toString());
            }

//            disclosure.setCertifiedBy(approvedDisclosure.getCertifiedBy());
//            //disclosure.setReviewStatus(approvedDisclosure.getReviewStatus());
//            //disclosure.setUpdateTimestamp(approvedDisclosure.getUpdateTime());
//            //disclosure.setUpdateUser(approvedDisclosure.getUpdateUser());
//
//
//
//
//            Vector FinancialEntities=(Vector)disclosureMap.get(ENTITIES);
//
//
//
//       if(FinancialEntities!=null){
//
//
//            for (Iterator it = FinancialEntities.iterator(); it.hasNext();) {
//                CoiDisclosureDetails disclDetails=new CoiDisclosureDetailsImpl();
//                CoiAnnualProjectEntityDetailsBean projectDetails = (CoiAnnualProjectEntityDetailsBean)it.next();
//                disclDetails.setEntityName(projectDetails.getEntityName());
//                //disclDetails.setProjectKeyOrModuleItemKey(projectDetails.getModuleItemKey());
//                disclDetails.setConflictStatus(projectDetails.getEntityStatus());
//                //disclDetails.setUpdateTimestamp(projectDetails.getUpdateTimeStamp().toString());
//               // disclDetails.setUpdateUser(projectDetails.getUpdateUser());
//               if(disclDetails.getEntityName()!=null && !disclDetails.getEntityName().equals("")){
//                disclosure.getCoiDisclosureDetails().add(disclDetails);
//               }
//          }
//       }
//
//            List ProjectNamelist=new ArrayList();
//
//            /*
//             *
//             * changes added by jai
//             */
//
//            CoiCommonService lCoiCommonService=CoiCommonService.getInstance();
// if(FinancialEntities!=null){
//
//
//            for (Iterator it = FinancialEntities.iterator(); it.hasNext();) {
//                DisclosureProjects projects=new DisclosureProjectsImpl();
//                CoiAnnualProjectEntityDetailsBean projectDetails = (CoiAnnualProjectEntityDetailsBean)it.next();
//                if(projectDetails.getCoiProposalProjectTitle()!=null && !projectDetails.getCoiProposalProjectTitle().equals("")){
//                projects.setProjectTitle(projectDetails.getCoiProposalProjectTitle());
//                }
//
//                if(projectDetails.getCoiProjectTitle()!=null && !projectDetails.getCoiProjectTitle().equals("")){
//                    if(ProjectNamelist.contains(projectDetails.getCoiProjectTitle())){
//                        continue;
//                    }else{
//                        ProjectNamelist.add(projectDetails.getCoiProjectTitle());
//                    }
//                  projects.setProjectTitle(projectDetails.getCoiProjectTitle());
//                }
//
//                if(projectDetails.getCoiAwardProjectTitle()!=null && !projectDetails.getCoiAwardProjectTitle().equals("")){
//                projects.setProjectTitle(projectDetails.getCoiAwardProjectTitle());
//                }
//                if(projectDetails.getCoiProtocolProjectTitle()!=null && !projectDetails.getCoiProtocolProjectTitle().equals("")){
//                projects.setProjectTitle(projectDetails.getCoiProtocolProjectTitle());
//                }
//                projects.setProjectSponsor(projectDetails.getCoiProjectSponsor());
//                if(projectDetails.getCoiProjectStartDate()!=null && !projectDetails.getCoiProjectStartDate().equals("")){
//                projects.setProjectStartDate(lCoiCommonService.getFormatedDate(projectDetails.getCoiProjectStartDate()));
//                }
//                if(projectDetails.getCoiProjectEndDate()!=null && !projectDetails.getCoiProjectEndDate().equals("")){
//                projects.setProjectEndDate(lCoiCommonService.getFormatedDate(projectDetails.getCoiProjectEndDate()));
//                }
//
//                if(projects.getProjectTitle()!=null && !projects.getProjectTitle().equals("")){
//                disclosure.getDisclosureProjects().add(projects);
//                }
//
//                //addding financial Entity Adding part to
//
//            }
//            }
//
////


         //disclosure.setCertifiedBy(approvedDisclosure.getCertifiedBy());
            disclosure.setCertifiedBy(fullName);

            //disclosure.setReviewStatus(approvedDisclosure.getReviewStatus());
            //disclosure.setUpdateTimestamp(approvedDisclosure.getUpdateTime());
            //disclosure.setUpdateUser(approvedDisclosure.getUpdateUser());




            Vector FinancialEntities=(Vector)disclosureMap.get(ENTITIES);



//       if(FinancialEntities!=null){
//
//
//            for (Iterator it = FinancialEntities.iterator(); it.hasNext();) {
//                CoiDisclosureDetails disclDetails=new CoiDisclosureDetailsImpl();
//                CoiAnnualProjectEntityDetailsBean projectDetails = (CoiAnnualProjectEntityDetailsBean)it.next();
//                disclDetails.setEntityName(projectDetails.getEntityName());
//                //disclDetails.setProjectKeyOrModuleItemKey(projectDetails.getModuleItemKey());
//                disclDetails.setConflictStatus(projectDetails.getEntityStatus());
//                //disclDetails.setUpdateTimestamp(projectDetails.getUpdateTimeStamp().toString());
//               // disclDetails.setUpdateUser(projectDetails.getUpdateUser());
//               if(disclDetails.getEntityName()!=null && !disclDetails.getEntityName().equals("")){
//                disclosure.getCoiDisclosureDetails().add(disclDetails);
//               }
//          }
//       }

            List ProjectNamelist=new ArrayList();

            /*
             *
             * changes added by jai
             */
//             CoiDisclosureDetails disclDetails=new CoiDisclosureDetailsImpl();
//
//            CoiCommonService lCoiCommonService=CoiCommonService.getInstance();
// if(FinancialEntities!=null){
//
//
//            for (Iterator it = FinancialEntities.iterator(); it.hasNext();) {
//                DisclosureProjects projects=new DisclosureProjectsImpl();
//                CoiAnnualProjectEntityDetailsBean projectDetails = (CoiAnnualProjectEntityDetailsBean)it.next();
//                if(projectDetails.getCoiProposalProjectTitle()!=null && !projectDetails.getCoiProposalProjectTitle().equals("")){
//                projects.setProjectTitle(projectDetails.getCoiProposalProjectTitle());
//                }
//                projects.setProjectSponsor(projectDetails.getCoiProjectSponsor());
//                if(projectDetails.getCoiProjectStartDate()!=null && !projectDetails.getCoiProjectStartDate().equals("")){
//                projects.setProjectStartDate(lCoiCommonService.getFormatedDate(projectDetails.getCoiProjectStartDate()));
//                }
//                if(projectDetails.getCoiProjectEndDate()!=null && !projectDetails.getCoiProjectEndDate().equals("")){
//                projects.setProjectEndDate(lCoiCommonService.getFormatedDate(projectDetails.getCoiProjectEndDate()));
//                }
//
//                if(projectDetails.getCoiProjectTitle()!=null && !projectDetails.getCoiProjectTitle().equals("")){
//                    if(ProjectNamelist.contains(projectDetails.getCoiProjectTitle())){
//                        if(projectDetails.getEntityName()!=null){
//                        //disclDetails
//                        }
//                        continue;
//                    }else{
//                        ProjectNamelist.add(projectDetails.getCoiProjectTitle());
//                    }
//                  projects.setProjectTitle(projectDetails.getCoiProjectTitle());
//                }
//
//                if(projectDetails.getCoiAwardProjectTitle()!=null && !projectDetails.getCoiAwardProjectTitle().equals("")){
//                projects.setProjectTitle(projectDetails.getCoiAwardProjectTitle());
//                }
//                if(projectDetails.getCoiProtocolProjectTitle()!=null && !projectDetails.getCoiProtocolProjectTitle().equals("")){
//                projects.setProjectTitle(projectDetails.getCoiProtocolProjectTitle());
//                }
//
//
//                if(projects.getProjectTitle()!=null && !projects.getProjectTitle().equals("")){
//                disclosure.getDisclosureProjects().add(projects);
//                }
//
//                //addding financial Entity Adding part to
//
//            }
//            }

//
            CoiDisclosureDetails disclDetails=new CoiDisclosureDetailsImpl();             
            CoiCommonService lCoiCommonService=CoiCommonService.getInstance();
            DisclosureProjects projects=new DisclosureProjectsImpl();
            int financialEntitySize=0;
            if(FinancialEntities!=null && !FinancialEntities.isEmpty()){
              financialEntitySize=FinancialEntities.size();
            }
           // for (Iterator it = FinancialEntities.iterator(); it.hasNext();) {
            int module=Integer.parseInt(moduleCode);
            if(module!=8){    
            for(int i=0;i<financialEntitySize;i++){
            CoiAnnualProjectEntityDetailsBean projectDetails = (CoiAnnualProjectEntityDetailsBean)FinancialEntities.get(i);
//            if(lNewProj){
//                projects=new DisclosureProjectsImpl();
//            }

            if(projectDetails.getCoiProjectTitle()!=null && !projectDetails.getCoiProjectTitle().equals("")){
                    if(ProjectNamelist.contains(projectDetails.getCoiProjectTitle())){
                         if(projectDetails.getEntityName()!=null){
                                disclDetails=new CoiDisclosureDetailsImpl();
                                disclDetails.setEntityName(projectDetails.getEntityName());
                                disclDetails.setConflictStatus(projectDetails.getEntityStatus());
                        //disclDetails
                        }
                projects.getCoiDisclosureDetails().add(disclDetails);
                 if(i==financialEntitySize-1){
                   disclosure.getDisclosureProjects().add(projects);
                 }
                 //lNewProj=false;
                       continue;
                   }else{
                      //since the project title is not repeating
                        if(i!=0){
                         disclosure.getDisclosureProjects().add(projects);
                         projects=new DisclosureProjectsImpl();
                         projects.setProjectTitle(projectDetails.getCoiProjectTitle());
                        }else{
                         projects.setProjectTitle(projectDetails.getCoiProjectTitle());
                        }
                         //lNewProj=true;
//                        if(i==1){
//                        disclosure.getDisclosureProjects().add(projects);
//                        }
                       ProjectNamelist.add(projectDetails.getCoiProjectTitle());
                    }

                  projects.setProjectSponsor(projectDetails.getCoiProjectSponsor());
                  if(projectDetails.getCoiProjectStartDate()!=null && !projectDetails.getCoiProjectStartDate().toString().equals("")){
                projects.setProjectStartDate(lCoiCommonService.getFormatedDate(projectDetails.getCoiProjectStartDate()));
                }
                  if(projectDetails.getApplicationDate()!=null && !projectDetails.getApplicationDate().toString().equals("")){
                projects.setProjectStartDate(lCoiCommonService.getFormatedDate(projectDetails.getApplicationDate()));
                  }

                if(projectDetails.getCoiProjectEndDate()!=null && !projectDetails.getCoiProjectEndDate().toString().equals("")){
                projects.setProjectEndDate(lCoiCommonService.getFormatedDate(projectDetails.getCoiProjectEndDate()));
                }

               if(projectDetails.getEntityName()!=null){
                                disclDetails=new CoiDisclosureDetailsImpl();
                                disclDetails.setEntityName(projectDetails.getEntityName());
                                disclDetails.setConflictStatus(projectDetails.getEntityStatus());
                        //disclDetails
                  }

                projects.getCoiDisclosureDetails().add(disclDetails);
                if(i==financialEntitySize-1){
                   disclosure.getDisclosureProjects().add(projects);
                 }
//                if(i!=0){
//                disclosure.getDisclosureProjects().add(projects);
//                }
                }

            }

            } 
            else if(module==8){
                for(int i=0;i<financialEntitySize;i++){
                CoiProjectEntityDetailsBean tvlDetails = (CoiProjectEntityDetailsBean)FinancialEntities.get(i);
                 
                if(tvlDetails.getEventName()!=null){
                    projects.setEventName(tvlDetails.getEventName());
                }
                if(tvlDetails.getDestination()!=null){
                    projects.setDestination(tvlDetails.getDestination());
                }
                if(tvlDetails.getPurpose()!=null){
                    projects.setPurpose(tvlDetails.getPurpose());
                }
                if(tvlDetails.getCoiProjectSponsor()!=null){
                    projects.setCoiProjectSponsor(tvlDetails.getCoiProjectSponsor());
                }
                if(tvlDetails.getsDate()!=null && !tvlDetails.getsDate().toString().equals("")){
                    projects.setSDate(tvlDetails.getsDate());
                }
                if(tvlDetails.geteDate()!=null && !tvlDetails.geteDate().toString().equals("")){
                    projects.setEDate(tvlDetails.geteDate());
                }
                if(tvlDetails.getCoiProjectFundingAmount()!=null){
                    projects.setProjectFundingAmount(tvlDetails.getCoiProjectFundingAmount().toString());
                }
               
               disclosure.getDisclosureProjects().add(projects);
            }
                
            }

                
         Vector questionnaire=(Vector)disclosureMap.get(QUESTIONNAIRE);

         if(questionnaire!=null){

            for (Iterator it = questionnaire.iterator(); it.hasNext();) {
                CertificationQuestion certQuest=new CertificationQuestionImpl();
                CoiQuestionAnswerBean qyuestionAnswer = (CoiQuestionAnswerBean)it.next();
                certQuest.setQuestion(qyuestionAnswer.getQuestion());
                certQuest.setAnswer(qyuestionAnswer.getAnswer());
                certQuest.setQuestionID(qyuestionAnswer.getQuestionId());

                disclosure.getCertificationQuestion().add(certQuest);




            }
         }





          Vector disclosureNotes=(Vector)disclosureMap.get(NOTES);
         if(disclosureNotes!=null){

            for (Iterator it = disclosureNotes.iterator(); it.hasNext();) {
                DisclosureNotes notes=new DisclosureNotesImpl();
                Coiv2NotesBean note = (Coiv2NotesBean)it.next();
                notes.setComments(note.getComments());
                notes.setTitle(note.getTitle());
                notes.setNoteEntry(note.getCoiNotepadEntryTypeDesc());
                notes.setUpdateUser(note.getUpdateUser());
                if(note.getUpdateTimestamp()!=null){
                notes.setUpdateTimestamp(note.getUpdateTimestamp().toString());
                }
               // notes.setUpdateTimestamp(note.getUpdateTimestamp().toString());
                
                disclosure.getDisclosureNotes().add(notes);



            }}



         Vector attachemnt=(Vector)disclosureMap.get(ATTACHMENTS);
            if(attachemnt!=null){

                for (Iterator it = attachemnt.iterator(); it.hasNext();) {
                DisclosureDocuments disclDoc=new DisclosureDocumentsImpl();
                Coiv2AttachmentBean  doc = (Coiv2AttachmentBean)it.next();
                disclDoc.setDocumentType(doc.getDocType());
                disclDoc.setDescription(doc.getDescription());
                disclDoc.setUPDATEUSER(doc.getUpdateUser());
                if(doc.getUpdateTimeStamp()!=null){
                disclDoc.setUPDATETIMESTAMP(doc.getUpdateTimeStamp().toString());
                }
               // disclDoc.setUPDATETIMESTAMP(doc.getUpdateTimeStamp().toString());

                disclosure.getDisclosureDocuments().add(disclDoc);



            }

            }













        }catch(Exception e){
            e.printStackTrace();
        }

        return disclosure;



    }





}

