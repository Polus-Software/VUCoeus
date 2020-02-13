/*
 * PHS398CoverPageSupplementStream_V3_0.java
 *
 */

package edu.vanderbilt.coeus.s2s.generator;

import edu.mit.coeus.departmental.bean.DepartmentPersonFormBean;
import edu.mit.coeus.exception.CoeusException;
import edu.mit.coeus.organization.bean.OrganizationMaintenanceFormBean;
import edu.mit.coeus.propdev.bean.ProposalDevelopmentTxnBean;
import edu.mit.coeus.questionnaire.bean.QuestionAnswerBean;
import edu.mit.coeus.rolodexmaint.bean.RolodexDetailsBean;
import edu.mit.coeus.s2s.util.S2SHashValue;
import edu.mit.coeus.utils.CoeusConstants;
import edu.mit.coeus.utils.CoeusFunctions;
import edu.mit.coeus.utils.CoeusVector;
import edu.mit.coeus.utils.UtilFactory;
import edu.mit.coeus.utils.dbengine.DBException;
import edu.mit.coeus.xml.generator.CoeusXMLException;
import edu.mit.coeus.xml.generator.CoeusXMLGenrator;
import edu.vanderbilt.coeus.s2s.bean.PHS398CoverPageSupplementTxnBean_V3_0;
import gov.grants.apply.forms.phs398_coverpagesupplement_v3_0.*;
import gov.grants.apply.forms.phs398_coverpagesupplement_v3_0.PHS398CoverPageSupplement30Type.ClinicalTrialType;
import gov.grants.apply.system.globallibrary_v2.*;

import java.math.BigDecimal;
import java.util.ArrayList;

import java.util.HashMap;
import java.util.Hashtable;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Vector;
import javax.xml.bind.JAXBException;

import edu.mit.coeus.s2s.generator.*;

/**
 *
 * @author  Jill McAfee
 */
public class PHS398CoverPageSupplementStream_V3_0 extends S2SBaseStream {
    private gov.grants.apply.forms.phs398_coverpagesupplement_v3_0.ObjectFactory objFactory;
    private gov.grants.apply.system.globallibrary_v2.ObjectFactory globallibObjFactory;
    private CoeusXMLGenrator xmlGenerator;
    
    // Txn bean
    private PHS398CoverPageSupplementTxnBean_V3_0 phs398CoverPageSupplementTxnBean;
    QuestionAnswerBean bean;
     
    private CoeusVector cvAnswers;
    private String propNumber;
    private final static String YES = "Y: Yes";
    private final static String NO = "N: No";
    
    private final static int CLINICAL_TRIAL = 2;
    private final static int CLINICAL_TRIAL_PHASE_3 = 3;
    private final static int VERTEBRATE_ANIMALS_EUTHANIZED = 590;
    private final static int VERTEBRATE_ANIMALS_METHOD = 591;
    private final static int VERTEBRATE_ANIMALS_JUSTIFICATION = 592;
    private final static int STEM_CELLS_USED = 5;
    private final static int STEM_CELL_LINES_KNOWN = 6;
    private final static int STEM_CELL_LINES = 142;
    private final static int INVENTIONS_AND_PATENTS = 119;
    private final static int INVENTIONS_AND_PATENTS_PREVIOUSLY_REPORTED = 120;
    private final static int CHANGE_OF_PI = 114;
    private final static int PREVIOUS_PI = 115;
    private final static int CHANGE_OF_INSTITUTION = 116;
    private final static int PREVIOUS_INSTITUTION = 117;
    
    /** Creates a new instance of PHS398CoverPageSupplementStream_V3_0 */
    public PHS398CoverPageSupplementStream_V3_0() {
        objFactory = new gov.grants.apply.forms.phs398_coverpagesupplement_v3_0.ObjectFactory();
        globallibObjFactory = new gov.grants.apply.system.globallibrary_v2.ObjectFactory();
        xmlGenerator = new CoeusXMLGenrator();   
    }
    
    private PHS398CoverPageSupplement30Type getPHS398CoverPageSupplement()
                throws /*CoeusXMLException,*/CoeusException,DBException,JAXBException {
        PHS398CoverPageSupplement30Type phs398CoverPageSupplement30Type = objFactory.createPHS398CoverPageSupplement30();
        phs398CoverPageSupplementTxnBean = new PHS398CoverPageSupplementTxnBean_V3_0();
              
        try {
        	phs398CoverPageSupplement30Type.setFormVersion("3.0");
        	
        	/* Clinical Trials */
            phs398CoverPageSupplement30Type.setClinicalTrial(getClinicalTrialType());
            
            /* Vertebrate Animals */ 
            phs398CoverPageSupplement30Type.setVertebrateAnimals(getVertebrateAnimalsInfo());
            
            /* Program Income */
            phs398CoverPageSupplement30Type.setProgramIncome(getProgramIncome());
            
            /* Get program income for budget periods if program income exists */
            if (getProgramIncome().equals(YES)) {
                
                /* Program Income Budget Periods */
            	CoeusVector programIncomeBudgetPeriods = phs398CoverPageSupplementTxnBean.getProgramIncome(propNumber);

            	if (programIncomeBudgetPeriods.size() > 0) {
            		BigDecimal budgetPeriodValue;
            		Integer budgetPeriod;
            		
            		for (int i=0; i < programIncomeBudgetPeriods.size(); i++) {

            			/* Convert the insanely returned BigDecimal format to the more useful Integer */
            			budgetPeriodValue = (BigDecimal) programIncomeBudgetPeriods.get(i);
            			budgetPeriod = budgetPeriodValue.intValueExact();
            			
            			/* Get program income budget period info */
            			phs398CoverPageSupplement30Type.getIncomeBudgetPeriod().add(
            					getProgramIncomeBudgetPeriod(budgetPeriod));
            		}
            	}
            }
            
            /* Stem Cells */
            phs398CoverPageSupplement30Type.setStemCells(getStemCellsType());
            
            /* Inventions and Patents */
            HashMap inventionsAndPatents = getInventionsAndPatents();
            
            if (inventionsAndPatents != null) {
            	if (inventionsAndPatents.get(INVENTIONS_AND_PATENTS) != null) {
		            if (inventionsAndPatents.get(INVENTIONS_AND_PATENTS).equals("Y")) {
		 	            phs398CoverPageSupplement30Type.setIsInventionsAndPatents(YES);
		 	            
		 	            if (inventionsAndPatents.get(INVENTIONS_AND_PATENTS_PREVIOUSLY_REPORTED).equals("Y")) {
		 	            	phs398CoverPageSupplement30Type.setIsPreviouslyReported(YES);
		 	            }
		 	            else {
		 	            	phs398CoverPageSupplement30Type.setIsPreviouslyReported(NO);
		 	            }
		            }
		            else {
		            	phs398CoverPageSupplement30Type.setIsInventionsAndPatents(NO);
		            }
            	}
            }
            
            /* Change of Investigator / Institution */
            HashMap changeOfPiOrInstitution = getChangeOfInvestigatorOrInstitution();
            
            if (changeOfPiOrInstitution != null) {
            	if (changeOfPiOrInstitution.get(CHANGE_OF_PI) != null) {
		            if (changeOfPiOrInstitution.get(CHANGE_OF_PI).equals("Y")) {
		            	phs398CoverPageSupplement30Type.setIsChangeOfPDPI(YES);
		            	phs398CoverPageSupplement30Type.setFormerPDName(getPreviousPiName((String) changeOfPiOrInstitution.get(PREVIOUS_PI)));
		            }
		            else {
		            	phs398CoverPageSupplement30Type.setIsChangeOfPDPI(NO);
		            }
            	}
	            else {
	            	phs398CoverPageSupplement30Type.setIsChangeOfPDPI(NO);
	            }
	            
            	if (changeOfPiOrInstitution.get(CHANGE_OF_INSTITUTION) != null) {
		            if (changeOfPiOrInstitution.get(CHANGE_OF_INSTITUTION).equals("Y")) {
		            	phs398CoverPageSupplement30Type.setIsChangeOfInstitution(YES);
		            	phs398CoverPageSupplement30Type.setFormerInstitutionName(getOrganizationName((String) changeOfPiOrInstitution.get(PREVIOUS_INSTITUTION)));
		            }
		            else {
		            	phs398CoverPageSupplement30Type.setIsChangeOfInstitution(NO);
		            }
            	}
	            else {
	            	phs398CoverPageSupplement30Type.setIsChangeOfInstitution(NO);
	            }
            }
            else {
            	phs398CoverPageSupplement30Type.setIsChangeOfPDPI(NO);
            	phs398CoverPageSupplement30Type.setIsChangeOfInstitution(NO);
            }

            return phs398CoverPageSupplement30Type;
            
        }
        catch (JAXBException jaxbEx){
            UtilFactory.log(jaxbEx.getMessage(),jaxbEx,"PHS398CoverPageSupplementStream_V3_0","getPHS398CoverPageSupplement()");
            throw new CoeusXMLException(jaxbEx.getMessage());
        }          
    }   

    private PHS398CoverPageSupplement30Type.ClinicalTrialType getClinicalTrialType() 
    		throws JAXBException, DBException, CoeusException {
        PHS398CoverPageSupplement30Type.ClinicalTrialType clinicalTrialType = objFactory.createPHS398CoverPageSupplement30TypeClinicalTrialType();

        /* Clinical Trial */
        cvAnswers = phs398CoverPageSupplementTxnBean.getClinicalTrial(propNumber);
        HashMap clinicalTrialAnswers = processQuestionnaireAnswers(cvAnswers);
     
        if (clinicalTrialAnswers != null) {
        	String clinicalTrial = (String) clinicalTrialAnswers.get(CLINICAL_TRIAL);
            if (clinicalTrial.equals("Y")) {
                 clinicalTrialType.setIsClinicalTrial(YES);
                 
                 /* Clinical Trial Phase 3 */
                 String clinicalTrialPhase3 = (String) clinicalTrialAnswers.get(CLINICAL_TRIAL_PHASE_3);
                 if (clinicalTrialPhase3.equals("Y")) {
                	 clinicalTrialType.setIsPhaseIIIClinicalTrial(YES);
                 } else {
                     clinicalTrialType.setIsPhaseIIIClinicalTrial(NO);
                 }
            }
            else {
            	clinicalTrialType.setIsClinicalTrial(NO);
            }
        }
        else {
        	clinicalTrialType.setIsClinicalTrial(NO);
        }
        return clinicalTrialType;
     }
    
    private PHS398CoverPageSupplement30Type.VertebrateAnimalsType getVertebrateAnimalsInfo() 
    		throws JAXBException, DBException, CoeusException {
        PHS398CoverPageSupplement30Type.VertebrateAnimalsType vertebrateAnimalsType = objFactory.createPHS398CoverPageSupplement30TypeVertebrateAnimalsType();

        /* Vertebrate Animals */
        cvAnswers = phs398CoverPageSupplementTxnBean.getVertebrateAnimals(propNumber);
        HashMap vertebrateAnimalsAnswers = processQuestionnaireAnswers(cvAnswers);

    	vertebrateAnimalsType.setAnimalEuthanasiaIndicator(NO);
        if (vertebrateAnimalsAnswers != null) {
        	String vertebrateAnimalsEuthanized = (String) vertebrateAnimalsAnswers.get(VERTEBRATE_ANIMALS_EUTHANIZED);
            if (vertebrateAnimalsEuthanized != null) {
	        	if (vertebrateAnimalsEuthanized.equals("Y")) {
	            	vertebrateAnimalsType.setAnimalEuthanasiaIndicator(YES);
	                 
	                 /* Animals euthanized method */
	                 String vertebrateAnimalsMethod = (String) vertebrateAnimalsAnswers.get(VERTEBRATE_ANIMALS_METHOD);
	                 if (vertebrateAnimalsMethod != null) {
		                 if (vertebrateAnimalsMethod.equals("Y")) {
		                	 vertebrateAnimalsType.setAVMAConsistentIndicator(YES);
		                 } 
		                 else {
		                	 vertebrateAnimalsType.setAVMAConsistentIndicator(NO);
		                	 
		                	 /* Euthanized justification */
		                	 vertebrateAnimalsType.setEuthanasiaMethodDescription((String) vertebrateAnimalsAnswers.get(VERTEBRATE_ANIMALS_JUSTIFICATION));
		                 }
	                 }
	            }
	            else {
	            	vertebrateAnimalsType.setAnimalEuthanasiaIndicator(NO);
	            }
            }
        }
        return vertebrateAnimalsType;
    }
    
    
    private String getProgramIncome() throws DBException, CoeusException {

    	CoeusVector programIncomeExists =  phs398CoverPageSupplementTxnBean.getProgramIncome(propNumber);
    	
        if (programIncomeExists.size() > 0) {
           	return YES;
        }
        else {
        	return NO;
        }
    }
    
	/* Get program income budget period info */    
    private PHS398CoverPageSupplement30Type.IncomeBudgetPeriodType getProgramIncomeBudgetPeriod(Integer budgetPeriod) 
    		throws JAXBException, DBException, CoeusException {
        
    	PHS398CoverPageSupplement30Type.IncomeBudgetPeriodType incomeBudgetPeriodType = 
        	objFactory.createPHS398CoverPageSupplement30TypeIncomeBudgetPeriodType();
    			
    	HashMap programIncomeBudgetPeriodInfo =  
    			phs398CoverPageSupplementTxnBean.getProgramIncomeBudgetPeriod(propNumber, budgetPeriod);
    	incomeBudgetPeriodType.setBudgetPeriod(budgetPeriod);
    	
    	BigDecimal money = new BigDecimal(programIncomeBudgetPeriodInfo.get("AMOUNT").toString());
    	incomeBudgetPeriodType.setAnticipatedAmount(money);
    	incomeBudgetPeriodType.setSource((String) programIncomeBudgetPeriodInfo.get("DESCRIPTION"));
    	
    	return incomeBudgetPeriodType;
     }
    
    /* Get stem cell information */
    private PHS398CoverPageSupplement30Type.StemCellsType getStemCellsType() throws JAXBException, DBException, CoeusException {
       PHS398CoverPageSupplement30Type.StemCellsType stemCellsType = objFactory.createPHS398CoverPageSupplement30TypeStemCellsType();

       cvAnswers = phs398CoverPageSupplementTxnBean.getStemCells(propNumber);
       HashMap stemCellAnswers = processQuestionnaireAnswers(cvAnswers);
    
       if (stemCellAnswers != null) {
    	   String stemCellsUsed = (String) stemCellAnswers.get(STEM_CELLS_USED);
           if (stemCellsUsed.equals("Y")) {
        	   stemCellsType.setIsHumanStemCellsInvolved(YES);

        	   /* The question asks if stem cell lines are known, but the box should be checked if they are NOT known */
        	   String stemCellLinesKnown = (String) stemCellAnswers.get(STEM_CELL_LINES_KNOWN);
               if (stemCellLinesKnown.equals("Y")) {
            	   stemCellsType.setStemCellsIndicator(NO);
            	   ArrayList<String> cellLines = getStemCellLines(cvAnswers);
            	   for (int c=0; c < cellLines.size(); c++) {
            		   stemCellsType.getCellLines().add(cellLines.get(c));
            	   }
               } 
               else {
            	   stemCellsType.setStemCellsIndicator(YES);
               }
           }
           else {
        	   stemCellsType.setIsHumanStemCellsInvolved(NO);
           }
       	}
       else {
    	   stemCellsType.setIsHumanStemCellsInvolved(NO);
       }
       return stemCellsType;
    }
       
    /* Get stem cell lines from questionnaire answers and breaks out lists into individual entries */
    private ArrayList<String> getStemCellLines (CoeusVector dbAnswers) {
	   	QuestionAnswerBean bean = new QuestionAnswerBean();
	   	ArrayList<String> cellLines = new ArrayList<String>();
	   	String[] answerList = new String[10];
	
	   	if (dbAnswers.size() > 0) {
	   		for (int v=0; v < dbAnswers.size(); v++) {
	   			bean = (QuestionAnswerBean) dbAnswers.get(v);       			
	   			if (bean.getQuestionId() == STEM_CELL_LINES) {
	   				answerList = bean.getAnswer().split(",");
	   				for (int a=0; a < answerList.length; a++) {
	   					cellLines.add(answerList[a].trim());
	   				}
	   			}
	   		}
	   	}
	   	return cellLines;
    }
    
    /* Get inventions and patents */
    private HashMap getInventionsAndPatents() throws CoeusException, DBException {

        cvAnswers = phs398CoverPageSupplementTxnBean.getInventionsAndPatents(propNumber);
        HashMap inventionsAndPatentsAnswers = processQuestionnaireAnswers(cvAnswers);
     
        return inventionsAndPatentsAnswers;
    }
    
    /* Get change of PI / institution */
    private HashMap getChangeOfInvestigatorOrInstitution() throws CoeusException, DBException {

        cvAnswers = phs398CoverPageSupplementTxnBean.getChangeOfInvestigatorOrInstitution(propNumber);
        HashMap changeOfPiOrInstitution = processQuestionnaireAnswers(cvAnswers);
     
        return changeOfPiOrInstitution;
    }
    
    /* Get previous PI name */
    private HumanNameDataType getPreviousPiName(String rolodexId) throws CoeusException, DBException, JAXBException {
    	HumanNameDataType previousPiName = objFactory.createHumanNameDataType();
    	RolodexDetailsBean bean = phs398CoverPageSupplementTxnBean.getPreviousPiName(rolodexId);
    	
    	if (cvAnswers != null) {
       		previousPiName.setPrefixName((String) bean.getPrefix());
    		previousPiName.setFirstName((String) bean.getFirstName());
    		previousPiName.setMiddleName((String) bean.getMiddleName());
    		previousPiName.setLastName((String) bean.getLastName());
       		previousPiName.setSuffixName((String) bean.getSuffix());
    	}
    	return previousPiName;
    }
    
    /* Get previous institution name */
    private String getOrganizationName(String orgId) throws CoeusException, DBException, JAXBException {
    	OrganizationMaintenanceFormBean bean = phs398CoverPageSupplementTxnBean.getOrganizationName(orgId);
    	String organizationName = (String) bean.getOrganizationName();
    	return organizationName;
    }
    
    /* Take questionnaire answers and put in CoeusVector for easy access by question ID */
    private HashMap processQuestionnaireAnswers(CoeusVector dbAnswers) {
    	// Extract the questionnaire answers from the returned data
    	QuestionAnswerBean bean = new QuestionAnswerBean();
    	HashMap<Integer, String> map = new HashMap<Integer, String>();
	   
    	if (dbAnswers.size() > 0) {
    		for (int v=0; v < dbAnswers.size(); v++) {
    			bean = (QuestionAnswerBean) dbAnswers.get(v);
    			map.put(bean.getQuestionId(),bean.getAnswer());
    		}
    	}
    	return map;
    }
    
    public Object getStream(java.util.HashMap ht) throws JAXBException, CoeusException, DBException {
    	this.propNumber = (String)ht.get("PROPOSAL_NUMBER");
        return getPHS398CoverPageSupplement();
    }  
}
