/*
 * @(#)NSFApplicationChecklistV1_2Stream.java
 *
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 */

package edu.mit.coeus.s2s.generator;

//import edu.mit.coeus.s2s.generator.stream.*;

import edu.mit.coeus.exception.CoeusException;
import edu.mit.coeus.s2s.bean.S2STxnBean;

import edu.mit.coeus.utils.UtilFactory;
import edu.mit.coeus.utils.dbengine.DBException;
import edu.mit.coeus.xml.generator.CoeusXMLException;
import edu.mit.coeus.xml.generator.CoeusXMLGenrator;

import gov.grants.apply.forms.nsf_applicationchecklist_1_2_v1_2.*;
import java.util.HashMap;
import javax.xml.bind.JAXBException;
 
/**
 * @author  jenlu
 * 
 */
 
 public class NSFApplicationChecklistV1_2Stream extends S2SBaseStream{ 
    private gov.grants.apply.forms.nsf_applicationchecklist_1_2_v1_2.ObjectFactory objFactory;
    private CoeusXMLGenrator xmlGenerator;

    private String propNumber;
    private UtilFactory utilFactory;
    
    private S2STxnBean s2sTxnBean;
    
    /** Creates a new instance of NSFApplicationChecklistV1_1Stream */
    public NSFApplicationChecklistV1_2Stream(){
        objFactory = new gov.grants.apply.forms.nsf_applicationchecklist_1_2_v1_2.ObjectFactory();
     } 
   
  
    private NSFApplicationChecklist12Type getNSFApplicationChecklist() 
        throws CoeusXMLException,CoeusException,DBException, JAXBException{
            
        s2sTxnBean = new S2STxnBean();
        
        try{
            NSFApplicationChecklist12Type NSFApplicationChecklist = 
                                    objFactory.createNSFApplicationChecklist12();
            
            NSFApplicationChecklist12Type.CoverSheetType coverSheet = 
                objFactory.createNSFApplicationChecklist12TypeCoverSheetType();
            coverSheet.setCheckCoverSheet(getChecklistAnswer(1));
            coverSheet.setCheckRenewal(getChecklistAnswer(2));
            /* Is this a full application related a previously submitted 
             *preliminary application? */
            coverSheet.setCheckFullApp(getChecklistAnswer(3));
            /* NSF proposals should not be "continuations" */
            coverSheet.setCheckTypeApp(getChecklistAnswer(4));
            /* Is the application certified? */
            coverSheet.setCheckAppCert(getChecklistAnswer(5));
            NSFApplicationChecklist.setCoverSheet(coverSheet);
            
            NSFApplicationChecklist.setCheckRRSite(getChecklistAnswer(6));
            NSFApplicationChecklist.setCheckRROtherInfo(getChecklistAnswer(7));
            
            NSFApplicationChecklist.setCheckProjectSummary(getChecklistAnswer(8));;
            
            NSFApplicationChecklist12Type.ProjectNarrativeType projectNarrative = 
                objFactory.createNSFApplicationChecklist12TypeProjectNarrativeType();
            projectNarrative.setCheckProjectNarrative(getChecklistAnswer(9));
            /* Does narrative include merit review criteria? */
            projectNarrative.setCheckMeritReview(getChecklistAnswer(10));
            /* URL's should not be included in the narrative. */
            projectNarrative.setCheckURL(getChecklistAnswer(11));            
            /** Does narrative include info regarding prior support? */
            projectNarrative.setCheckPriorSupport(getChecklistAnswer(12));
            /* HR Info that is mandatory for renwals from academic institutions. */
            projectNarrative.setCheckHRInfo(getChecklistAnswer(13));
            NSFApplicationChecklist.setProjectNarrative(projectNarrative);
            /* Bibliography attached. */
            NSFApplicationChecklist.setCheckBiblio(getChecklistAnswer(14));
            /* Facilities attached. */
            NSFApplicationChecklist.setCheckFacilities(getChecklistAnswer(15));
            
            NSFApplicationChecklist12Type.EquipmentType equipment = 
                objFactory.createNSFApplicationChecklist12TypeEquipmentType();
            /* Equipment attached. */
            equipment.setCheckEquipment(getChecklistAnswer(16));
            /* Supplementary information pdf attached. */
            equipment.setCheckSuppDoc(getChecklistAnswer(17));   
            /* Additional items relevant to NSF Program complete. */
            equipment.setCheckAdditionalItems(getChecklistAnswer(18));
            NSFApplicationChecklist.setEquipment(equipment);
            
            NSFApplicationChecklist12Type.RRSrProfileType RRSrProfile = 
                objFactory.createNSFApplicationChecklist12TypeRRSrProfileType();
            RRSrProfile.setCheckRRSrProfile(getChecklistAnswer(19));            
            RRSrProfile.setCheckBioSketch(getChecklistAnswer(20));
            RRSrProfile.setCheckCurrentPendingSupport(getChecklistAnswer(21));
            NSFApplicationChecklist.setRRSrProfile(RRSrProfile);
            
            NSFApplicationChecklist.setCheckRRPersonalData(getChecklistAnswer(22));

            NSFApplicationChecklist12Type.RRBudgetType RRBudget = 
                objFactory.createNSFApplicationChecklist12TypeRRBudgetType();
            /* Budget complete */
            RRBudget.setCheckRRBudget(getChecklistAnswer(23)); 
            /* Budget justification attached. */
            RRBudget.setCheckRRBudgetJustification(getChecklistAnswer(24));
//            /* Cost sharing correctly implemented. */
//            RRBudget.setCheckCostSharing(getChecklistAnswer(25));
            NSFApplicationChecklist.setRRBudget(RRBudget);
            
            NSFApplicationChecklist12Type.NSFCoverType NSFCover = 
                objFactory.createNSFApplicationChecklist12TypeNSFCoverType();
            //NSF Cover Page stream included
            NSFCover.setCheckNSFCover(getChecklistAnswer(26));
            //NSF Unit Consideration complete on NSF Cover Page
            NSFCover.setCheckNSFUnit(getChecklistAnswer(27));
            //Other NSF Info
            NSFCover.setCheckNSFOtherInfo(getChecklistAnswer(28));
            //Lobbying stream included
            NSFCover.setCheckNSFSFLLL(getChecklistAnswer(29));
            /* Deviation athorization included */
            NSFCover.setCheckNSFDevAuth(getChecklistAnswer(30));
            /* NSF Fast Lane registration included */
            NSFCover.setCheckNSFReg(getChecklistAnswer(31));
            /* Suggested Reviewers included. */
            NSFCover.setCheckDoNotInclude(getChecklistAnswer(32));
            NSFApplicationChecklist.setNSFCover(NSFCover);
            
           /**
            *FormVersion
           */
            
           NSFApplicationChecklist.setFormVersion("1.2");
             
           return NSFApplicationChecklist;
           
        }catch (JAXBException jaxbEx){
            utilFactory.log(jaxbEx.getMessage(),jaxbEx,"NSFApplicationChecklistV1_2Stream",
                "getNSFApplicationChecklist()");
            throw new CoeusXMLException(jaxbEx.getMessage());
        }
    }
    
    private String getChecklistAnswer(int questionId)
    throws DBException, CoeusException{
        String answer = s2sTxnBean.getNSFChecklistAnswer(propNumber, questionId);
        //v1.1 changed enumeration. (from v1.0 "Yes/No/NotApplicable" to  V1.1 "Y: Yes/N: No/NA: Not Applicable")
        if (answer != null)
            if (answer.equalsIgnoreCase("Yes"))
                answer = "Y: Yes";
            if (answer.equalsIgnoreCase("No"))
                answer = "N: No";
            if (answer.equalsIgnoreCase("NotApplicable"))
                answer = "NA: Not Applicable";
        return answer;
    } 
     
    
    public Object getStream(HashMap ht) throws JAXBException, CoeusException, DBException{
        this.propNumber = (String)ht.get("PROPOSAL_NUMBER");
        return getNSFApplicationChecklist();
    }    
 
 }   
    
