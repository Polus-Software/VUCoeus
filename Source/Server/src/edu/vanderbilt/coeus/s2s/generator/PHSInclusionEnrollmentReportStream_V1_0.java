/**
 * PHSInclusionEnrollmentReportStream_V1_0
 * @author Jill McAfee
 */

package edu.vanderbilt.coeus.s2s.generator;
 
import java.sql.Date;
import java.util.Calendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.math.BigInteger;

import javax.xml.bind.JAXBException;

import edu.mit.coeus.exception.CoeusException;
import edu.mit.coeus.propdev.bean.ProposalDevelopmentFormBean;
import edu.mit.coeus.propdev.bean.ProposalDevelopmentTxnBean;
import edu.mit.coeus.propdev.bean.ProposalYNQBean;
import edu.mit.coeus.questionnaire.bean.QuestionAnswerBean;
import edu.mit.coeus.utils.CoeusVector;
import edu.mit.coeus.utils.DateUtils;
import edu.mit.coeus.utils.UtilFactory;
import edu.mit.coeus.utils.dbengine.DBException;
import edu.mit.coeus.xml.generator.CoeusXMLException;
import edu.mit.coeus.xml.generator.CoeusXMLGenrator;

import edu.vanderbilt.coeus.s2s.bean.PHSInclusionEnrollmentReportTxnBean_V1_0;
import gov.grants.apply.forms.phs398_coverpagesupplement_1_4_v1_4.PHS398CoverPageSupplement14Type;
import gov.grants.apply.forms.phs_inclusion_enrollment_report_v1_0.*;

 public class PHSInclusionEnrollmentReportStream_V1_0 extends edu.mit.coeus.s2s.generator.S2SBaseStream {
    private gov.grants.apply.forms.phs_inclusion_enrollment_report_v1_0.ObjectFactory objFactory;
    private gov.grants.apply.system.global_v1.ObjectFactory globalObjFactory;
    private gov.grants.apply.system.globallibrary_v2.ObjectFactory globalLibraryObjFactory;
    private gov.grants.apply.system.attachments_v1.ObjectFactory attachmentsObjFactory;
    private CoeusXMLGenrator xmlGenerator;
 
    private ProposalDevelopmentTxnBean propDevTxnBean;
    private ProposalDevelopmentFormBean propDevFormBean;
    
    private PHSInclusionEnrollmentReportType inclusionEnrollmentReport = null;
    private String propNumber;

    // Questionnaire ID for Inclusion Enrollment Report questionnaire
    public static final int QUESTIONNAIRE = 9;
    
    private final static String YES = "Y: Yes";
    private final static String NO = "N: No";
    private final static String CUMULATIVE = "Cumulative (Actual)";
    private String enrollmentType;
    
    // Question IDs for Inclusion Enrollment Report questionnaire
    public static final int STUDY_TITLE = 578;
    public static final int DELAYED_ONSET = 580;
    public static final int ENROLLMENT_TYPE = 581;
    public static final int EXISTING_DATASET = 582;
    public static final int ENROLLMENT_LOCATION = 583;
    public static final int CLINICAL_TRIAL = 2;
    public static final int CLINICAL_TRIAL_PHASE_3 = 3;
    public static final int NOT_HISP_LAT = 502;
    public static final int NOT_HISP_LAT_F = 503;
    public static final int NOT_HISP_LAT_F_AI = 504;
    public static final int NOT_HISP_LAT_F_AS = 505;
    public static final int NOT_HISP_LAT_F_HW = 506;
    public static final int NOT_HISP_LAT_F_BL = 507;
    public static final int NOT_HISP_LAT_F_WH = 508;
    public static final int NOT_HISP_LAT_F_MU = 509;
    public static final int NOT_HISP_LAT_F_UNK = 510;
    public static final int NOT_HISP_LAT_M = 511;
    public static final int NOT_HISP_LAT_M_AI = 512;
    public static final int NOT_HISP_LAT_M_AS = 513;
    public static final int NOT_HISP_LAT_M_HW = 514;
    public static final int NOT_HISP_LAT_M_BL = 515;
    public static final int NOT_HISP_LAT_M_WH = 516;
    public static final int NOT_HISP_LAT_M_MU = 517;
    public static final int NOT_HISP_LAT_M_UNK = 518;
    public static final int NOT_HISP_LAT_U = 519;
    public static final int NOT_HISP_LAT_U_AI = 520;
    public static final int NOT_HISP_LAT_U_AS = 521;
    public static final int NOT_HISP_LAT_U_HW = 522;
    public static final int NOT_HISP_LAT_U_BL = 523;
    public static final int NOT_HISP_LAT_U_WH = 524;
    public static final int NOT_HISP_LAT_U_MU = 525;
    public static final int NOT_HISP_LAT_U_UNK = 526;
    public static final int HISP_LAT = 527;
    public static final int HISP_LAT_F = 528;
    public static final int HISP_LAT_F_AI = 529;
    public static final int HISP_LAT_F_AS = 530;
    public static final int HISP_LAT_F_HW = 531;
    public static final int HISP_LAT_F_BL = 532;
    public static final int HISP_LAT_F_WH = 533;
    public static final int HISP_LAT_F_MU = 534;
    public static final int HISP_LAT_F_UNK = 535;
    public static final int HISP_LAT_M = 536;
    public static final int HISP_LAT_M_AI = 537;
    public static final int HISP_LAT_M_AS = 538;
    public static final int HISP_LAT_M_HW = 539;
    public static final int HISP_LAT_M_BL = 540;
    public static final int HISP_LAT_M_WH = 541;
    public static final int HISP_LAT_M_MU = 542;
    public static final int HISP_LAT_M_UNK = 543;
    public static final int HISP_LAT_U = 544;
    public static final int HISP_LAT_U_AI = 545;
    public static final int HISP_LAT_U_AS = 546;
    public static final int HISP_LAT_U_HW = 547;
    public static final int HISP_LAT_U_BL = 548;
    public static final int HISP_LAT_U_WH = 549;
    public static final int HISP_LAT_U_MU = 550;
    public static final int HISP_LAT_U_UNK = 551;
    public static final int UNK = 552;
    public static final int UNK_F = 553;
    public static final int UNK_F_AI = 554;
    public static final int UNK_F_AS = 555;
    public static final int UNK_F_HW = 556;
    public static final int UNK_F_BL = 557;
    public static final int UNK_F_WH = 558;
    public static final int UNK_F_MU = 559;
    public static final int UNK_F_UNK = 560;
    public static final int UNK_M = 561;
    public static final int UNK_M_AI = 562;
    public static final int UNK_M_AS = 563;
    public static final int UNK_M_HW = 564;
    public static final int UNK_M_BL = 565;
    public static final int UNK_M_WH = 566;
    public static final int UNK_M_MU = 567;
    public static final int UNK_M_UNK = 568;
    public static final int UNK_U = 569;
    public static final int UNK_U_AI = 570;
    public static final int UNK_U_AS = 571;
    public static final int UNK_U_HW = 572;
    public static final int UNK_U_BL = 573;
    public static final int UNK_U_WH = 574;
    public static final int UNK_U_MU = 575;
    public static final int UNK_U_UNK = 576;
    public static final int COMMENTS = 577;
    
    // Number of questions per full form instance
    public static final int QUESTIONS_PER_INSTANCE = 84;
    
    // Number of instances
    public static final int INSTANCES = 10; 
    
    /* Values for gender method args */
    private static final int GENDER_UNKNOWN = 0;
    private static final int MALE = 1;
    private static final int FEMALE = 2;
    
    /* Values for ethnicity method args */
    private static final int ETHNICITY_UNKNOWN = 0;
    private static final int HISPANIC = 1;
    private static final int NOT_HISPANIC = 2;
    
	private CoeusVector questionnaireData;
	private HashMap<Integer, String> instanceData;
   
    /** Creates a new instance of PHSInclusionEnrollmentReportStream_V1_0 */
    public PHSInclusionEnrollmentReportStream_V1_0(){
        objFactory = new gov.grants.apply.forms.phs_inclusion_enrollment_report_v1_0.ObjectFactory();
        globalObjFactory = new gov.grants.apply.system.global_v1.ObjectFactory();
        attachmentsObjFactory = new gov.grants.apply.system.attachments_v1.ObjectFactory();
        globalLibraryObjFactory = new gov.grants.apply.system.globallibrary_v2.ObjectFactory();
        xmlGenerator = new CoeusXMLGenrator();
        
        propDevTxnBean = new ProposalDevelopmentTxnBean();
    } 
   
    /* Initializes the report */
    private PHSInclusionEnrollmentReportType getInclusionEnrollmentReport() throws CoeusXMLException,CoeusException,DBException{
      
        try{
        	inclusionEnrollmentReport = objFactory.createPHSInclusionEnrollmentReport();
        	inclusionEnrollmentReport.setFormVersion("1.0");

        	questionnaireData = processQuestionnaireAnswers();
            for (int q = 0; q < questionnaireData.size(); q++) {
            	instanceData = (HashMap) questionnaireData.get(q);
            	inclusionEnrollmentReport.getStudy().add(createStudy());
            }
         }
        catch (JAXBException jaxbEx){
            UtilFactory.log(jaxbEx.getMessage(),jaxbEx,"PHSInclusionEnrollmentReportStream_V1_0","getInclusionEnrollmentReport()");
            throw new CoeusXMLException(jaxbEx.getMessage());
        }
        return inclusionEnrollmentReport;
    }
    
    private PHSInclusionEnrollmentReportType.StudyType createStudy() 
    	throws JAXBException,CoeusException,DBException {
    	   
    	PHSInclusionEnrollmentReportType.StudyType study = objFactory.createPHSInclusionEnrollmentReportTypeStudyType();
        
        String title = (String) instanceData.get(STUDY_TITLE);          
        if (title != null && title.length() > 250){
        	study.setStudyTitle(title.substring(0,250));
        }
        else{
        	study.setStudyTitle(title);
        }

        // Set form data based on questionnaire answers
        study.setDelayedOnsetStudy(NO);
		if (instanceData.get(DELAYED_ONSET) != null) {
			/* If delayed onset, the rest of the form should be blank except title */
    		if (instanceData.get(DELAYED_ONSET).equals("Y")) {
    	        study.setDelayedOnsetStudy(YES);
    		}
    		
    		/* If NOT delayed onset, populate form */
    		if (instanceData.get(DELAYED_ONSET).equals("N")) {
	            study.setDelayedOnsetStudy(NO);
	            
	            study.setComments((String) instanceData.get(COMMENTS));
		        
				if (instanceData.get(EXISTING_DATASET) != null) {
		    		if (instanceData.get(EXISTING_DATASET).equals("Y")) {
				        study.setExistingDatasetOrResource(YES);
		    		}
		    		
		    		if (instanceData.get(EXISTING_DATASET).equals("N")) {
				        study.setExistingDatasetOrResource(NO);
		    		}
				}
		        
		        study.setEnrollmentLocation((String) instanceData.get(ENROLLMENT_LOCATION));
		
				if (instanceData.get(CLINICAL_TRIAL) != null) {
		    		if (instanceData.get(CLINICAL_TRIAL).equals("Y")) {
				        study.setClinicalTrial(YES);
		    		}
		    		
		    		if (instanceData.get(CLINICAL_TRIAL).equals("N")) {
				        study.setClinicalTrial(NO);
		    		}
				}
		
		        if (study.getClinicalTrial().equals(NO)) {
			        study.setPhaseIIIClinicalTrial(NO);
		        }
		        else {
					if (instanceData.get(CLINICAL_TRIAL_PHASE_3) != null) {
			    		if (instanceData.get(CLINICAL_TRIAL_PHASE_3).equals("Y")) {
					        study.setPhaseIIIClinicalTrial(YES);
			    		}
			    		
			    		if (instanceData.get(CLINICAL_TRIAL_PHASE_3).equals("N")) {
					        study.setPhaseIIIClinicalTrial(NO);
			    		}
					}
		        }

		        enrollmentType = (String) instanceData.get(ENROLLMENT_TYPE);
		        study.setEnrollmentType(enrollmentType);
		        
		    	study.setHispanic(getHispanic());
		    	study.setNotHispanic(getNotHispanic());
		    	
		        /* If enrollment type is Cumulative (Actual), exclude this */
		        if (enrollmentType.equals(CUMULATIVE)) {
		        	study.setUnknownEthnicity(getUnknownEthnicity());
    			}
		    	
		    	study.setTotal(getRaceTotals());
			} // end delayed onset  = 'N'
		} 

        return study;
        
    }
    
    private PHSInclusionEnrollmentReportEthnicCategoryDataType getHispanic() throws JAXBException {
    	PHSInclusionEnrollmentReportEthnicCategoryDataType hispanic = 
    		objFactory.createPHSInclusionEnrollmentReportEthnicCategoryDataType();
    	hispanic.setFemale(getFemale(HISPANIC));
    	hispanic.setMale(getMale(HISPANIC));
    	
    	if (enrollmentType.equals(CUMULATIVE)) {
    		hispanic.setUnknownGender(getUnknownGender(HISPANIC));
    	}
    	return hispanic;
    }

    private PHSInclusionEnrollmentReportEthnicCategoryDataType getNotHispanic() throws JAXBException {
    	PHSInclusionEnrollmentReportEthnicCategoryDataType notHispanic = 
    		objFactory.createPHSInclusionEnrollmentReportEthnicCategoryDataType();
    	notHispanic.setFemale(getFemale(NOT_HISPANIC));
    	notHispanic.setMale(getMale(NOT_HISPANIC));
    	
    	if (enrollmentType.equals(CUMULATIVE)) {
    		notHispanic.setUnknownGender(getUnknownGender(NOT_HISPANIC));
    	}
    	return notHispanic;
    }
    
    private PHSInclusionEnrollmentReportEthnicCategoryDataType getUnknownEthnicity() throws JAXBException {
    	PHSInclusionEnrollmentReportEthnicCategoryDataType unknownEthnicity = 
    		objFactory.createPHSInclusionEnrollmentReportEthnicCategoryDataType();
    	unknownEthnicity.setFemale(getFemale(ETHNICITY_UNKNOWN));
    	unknownEthnicity.setMale(getMale(ETHNICITY_UNKNOWN));
    	unknownEthnicity.setUnknownGender(getUnknownGender(ETHNICITY_UNKNOWN));
    	return unknownEthnicity;
    }
    
    private PHSInclusionEnrollmentReportRacialCategoryDataType getFemale(int hispanic) throws JAXBException {
    	PHSInclusionEnrollmentReportRacialCategoryDataType female = 
    		objFactory.createPHSInclusionEnrollmentReportRacialCategoryDataType();
    	
    	switch (hispanic) {
			case HISPANIC:
		    	female.setAmericanIndian(getAnswer(HISP_LAT_F_AI));
		    	female.setAsian(getAnswer(HISP_LAT_F_AS));
		    	female.setBlack(getAnswer(HISP_LAT_F_BL));
		    	female.setHawaiian(getAnswer(HISP_LAT_F_HW));
		    	female.setMultipleRace(getAnswer(HISP_LAT_F_MU));
		    	female.setWhite(getAnswer(HISP_LAT_F_WH));
		    	
		    	if (enrollmentType.equals(CUMULATIVE)) {
		    		female.setUnknownRace(getAnswer(HISP_LAT_F_UNK));
		    	}
		    	
		    	female.setTotal(getTotals(FEMALE, HISPANIC)); 
		    	break;
			case NOT_HISPANIC:
		    	female.setAmericanIndian(getAnswer(NOT_HISP_LAT_F_AI));
		    	female.setAsian(getAnswer(NOT_HISP_LAT_F_AS));
		    	female.setBlack(getAnswer(NOT_HISP_LAT_F_BL));
		    	female.setHawaiian(getAnswer(NOT_HISP_LAT_F_HW));
		    	female.setMultipleRace(getAnswer(NOT_HISP_LAT_F_MU));
		    	female.setWhite(getAnswer(NOT_HISP_LAT_F_WH));
		    	
		    	if (enrollmentType.equals(CUMULATIVE)) {
		    		female.setUnknownRace(getAnswer(NOT_HISP_LAT_F_UNK));
		    	}
		    	
		    	female.setTotal(getTotals(FEMALE, NOT_HISPANIC)); 
		    	break;
			case ETHNICITY_UNKNOWN:
		    	female.setAmericanIndian(getAnswer(UNK_F_AI));
		    	female.setAsian(getAnswer(UNK_F_AS));
		    	female.setBlack(getAnswer(UNK_F_BL));
		    	female.setHawaiian(getAnswer(UNK_F_HW));
		    	female.setMultipleRace(getAnswer(UNK_F_MU));
		    	female.setWhite(getAnswer(UNK_F_WH));
		    	female.setUnknownRace(getAnswer(UNK_F_UNK));
		    	
		    	female.setTotal(getTotals(FEMALE, ETHNICITY_UNKNOWN)); 
		    	break;
    	}
    	return female;
    }

    private PHSInclusionEnrollmentReportRacialCategoryDataType getMale(int hispanic) throws JAXBException {
    	PHSInclusionEnrollmentReportRacialCategoryDataType male = 
    		objFactory.createPHSInclusionEnrollmentReportRacialCategoryDataType();
    	
    	switch (hispanic) {
			case HISPANIC:
		    	male.setAmericanIndian(getAnswer(HISP_LAT_M_AI));
		    	male.setAsian(getAnswer(HISP_LAT_M_AS));
		    	male.setBlack(getAnswer(HISP_LAT_M_BL));
		    	male.setHawaiian(getAnswer(HISP_LAT_M_HW));
		    	male.setMultipleRace(getAnswer(HISP_LAT_M_MU));
		    	male.setWhite(getAnswer(HISP_LAT_M_WH));
		    	
		    	if (enrollmentType.equals(CUMULATIVE)) {
		    		male.setUnknownRace(getAnswer(HISP_LAT_M_UNK));
		    	}
		    	
		    	male.setTotal(getTotals(MALE, HISPANIC)); 
		    	break;
			case NOT_HISPANIC:
		    	male.setAmericanIndian(getAnswer(NOT_HISP_LAT_M_AI));
		    	male.setAsian(getAnswer(NOT_HISP_LAT_M_AS));
		    	male.setBlack(getAnswer(NOT_HISP_LAT_M_BL));
		    	male.setHawaiian(getAnswer(NOT_HISP_LAT_M_HW));
		    	male.setMultipleRace(getAnswer(NOT_HISP_LAT_M_MU));
		    	male.setWhite(getAnswer(NOT_HISP_LAT_M_WH));
		    	
		    	if (enrollmentType.equals(CUMULATIVE)) {
		    		male.setUnknownRace(getAnswer(NOT_HISP_LAT_M_UNK));
		    	}
		    	
		    	male.setTotal(getTotals(MALE, NOT_HISPANIC)); 
		    	break;
			case ETHNICITY_UNKNOWN:
		    	male.setAmericanIndian(getAnswer(UNK_M_AI));
		    	male.setAsian(getAnswer(UNK_M_AS));
		    	male.setBlack(getAnswer(UNK_M_BL));
		    	male.setHawaiian(getAnswer(UNK_M_HW));
		    	male.setMultipleRace(getAnswer(UNK_M_MU));
		    	male.setWhite(getAnswer(UNK_M_WH));
		    	male.setUnknownRace(getAnswer(UNK_M_UNK));
		    	
		    	male.setTotal(getTotals(MALE, ETHNICITY_UNKNOWN)); 
		    	break;
    	}
    	return male;
    }

    private PHSInclusionEnrollmentReportRacialCategoryDataType getUnknownGender(int hispanic) throws JAXBException {
    	PHSInclusionEnrollmentReportRacialCategoryDataType unknown = 
    		objFactory.createPHSInclusionEnrollmentReportRacialCategoryDataType();
    	
    	switch (hispanic) {
			case HISPANIC:
				unknown.setAmericanIndian(getAnswer(HISP_LAT_U_AI));
				unknown.setAsian(getAnswer(HISP_LAT_U_AS));
				unknown.setBlack(getAnswer(HISP_LAT_U_BL));
				unknown.setHawaiian(getAnswer(HISP_LAT_U_HW));
		    	unknown.setMultipleRace(getAnswer(HISP_LAT_U_MU));
		    	unknown.setWhite(getAnswer(HISP_LAT_U_WH));
		    	unknown.setUnknownRace(getAnswer(HISP_LAT_U_UNK));
		    	
		    	unknown.setTotal(getTotals(GENDER_UNKNOWN, HISPANIC)); 
		    	break;
			case NOT_HISPANIC:
				unknown.setAmericanIndian(getAnswer(NOT_HISP_LAT_U_AI));
				unknown.setAsian(getAnswer(NOT_HISP_LAT_U_AS));
				unknown.setBlack(getAnswer(NOT_HISP_LAT_U_BL));
				unknown.setHawaiian(getAnswer(NOT_HISP_LAT_U_HW));
				unknown.setMultipleRace(getAnswer(NOT_HISP_LAT_U_MU));
				unknown.setWhite(getAnswer(NOT_HISP_LAT_U_WH));
				unknown.setUnknownRace(getAnswer(NOT_HISP_LAT_U_UNK));
		    	
				unknown.setTotal(getTotals(GENDER_UNKNOWN, NOT_HISPANIC)); 
				break;
			case ETHNICITY_UNKNOWN:
				unknown.setAmericanIndian(getAnswer(UNK_U_AI));
		    	unknown.setAsian(getAnswer(UNK_U_AS));
		    	unknown.setBlack(getAnswer(UNK_U_BL));
		    	unknown.setHawaiian(getAnswer(UNK_U_HW));
		    	unknown.setMultipleRace(getAnswer(UNK_U_MU));
		    	unknown.setWhite(getAnswer(UNK_U_WH));
		    	unknown.setUnknownRace(getAnswer(UNK_U_UNK));
		    	
		    	unknown.setTotal(getTotals(GENDER_UNKNOWN, ETHNICITY_UNKNOWN));
		    	break;
    	}
    	return unknown;
    }
    
    /* Gets totals given gender and ethnicity */
    private BigInteger getTotals(int gender, int hispanic) throws JAXBException {
    	int addItUp = 0;
    	
    	switch (gender) {
    		case FEMALE:
    			addItUp = getTotalFemales(hispanic);
    			break;
    		case MALE:
    			addItUp = getTotalMales(hispanic);
    			break;
    		case GENDER_UNKNOWN:
    			addItUp = getTotalUnknowns(hispanic);
    			break;
    	}
    	
    	BigInteger total =  new BigInteger(new Integer(addItUp).toString());
    	
    	return total;
    }
    
    /* Gets total females by race for a given ethnicity */
    private Integer getTotalFemales(int hispanic) throws JAXBException {
    	int totalFemales = 0;
    	
    	switch (hispanic) {
			case ETHNICITY_UNKNOWN:
				totalFemales = 
					getAnswer(UNK_F_AI) +
					getAnswer(UNK_F_AS) +
					getAnswer(UNK_F_BL) +
					getAnswer(UNK_F_HW) +
					getAnswer(UNK_F_MU) +
					getAnswer(UNK_F_WH) +
					getAnswer(UNK_F_UNK); 
				break;
    		case HISPANIC:
    			totalFemales = 
    				getAnswer(HISP_LAT_F_AI) +
    				getAnswer(HISP_LAT_F_AS) +
    				getAnswer(HISP_LAT_F_BL) +
    				getAnswer(HISP_LAT_F_HW) +
    				getAnswer(HISP_LAT_F_MU) +
    				getAnswer(HISP_LAT_F_WH);
    			
		    		if (enrollmentType.equals(CUMULATIVE)) {
		    			totalFemales = totalFemales + getAnswer(HISP_LAT_F_UNK);
		    		}
    			break;
    		case NOT_HISPANIC:
    			totalFemales = 
    				getAnswer(NOT_HISP_LAT_F_AI) +
    				getAnswer(NOT_HISP_LAT_F_AS) +
    				getAnswer(NOT_HISP_LAT_F_BL) +
    				getAnswer(NOT_HISP_LAT_F_HW) +
    				getAnswer(NOT_HISP_LAT_F_MU) +
    				getAnswer(NOT_HISP_LAT_F_WH);
    			
	    			if (enrollmentType.equals(CUMULATIVE)) {
	    				totalFemales = totalFemales + getAnswer(NOT_HISP_LAT_F_UNK);
	    			}
    			break;
    	}
    	
    	return totalFemales;
    }
    
    /* Gets males by race for a given ethnicity */
    private Integer getTotalMales(int hispanic) throws JAXBException {
    	int totalMales = 0;
    	
    	switch (hispanic) {
			case ETHNICITY_UNKNOWN:
				totalMales = 
					getAnswer(UNK_M_AI) +
					getAnswer(UNK_M_AS) +
					getAnswer(UNK_M_BL) +
					getAnswer(UNK_M_HW) +
					getAnswer(UNK_M_MU) +
					getAnswer(UNK_M_WH) +
					getAnswer(UNK_M_UNK); 
				break;
	    	case HISPANIC:
    			totalMales = 
    				getAnswer(HISP_LAT_M_AI) +
					getAnswer(HISP_LAT_M_AS) +
					getAnswer(HISP_LAT_M_BL) +
					getAnswer(HISP_LAT_M_HW) +
					getAnswer(HISP_LAT_M_MU) +
					getAnswer(HISP_LAT_M_WH);
    			
	    			if (enrollmentType.equals(CUMULATIVE)) {
	    				totalMales = totalMales + getAnswer(HISP_LAT_M_UNK); 
	    			}
    			break;
    		case NOT_HISPANIC:
    			totalMales = 
    				getAnswer(NOT_HISP_LAT_M_AI) +
					getAnswer(NOT_HISP_LAT_M_AS) +
					getAnswer(NOT_HISP_LAT_M_BL) +
					getAnswer(NOT_HISP_LAT_M_HW) +
					getAnswer(NOT_HISP_LAT_M_MU) +
					getAnswer(NOT_HISP_LAT_M_WH);
    			
	    			if (enrollmentType.equals(CUMULATIVE)) {
	    				totalMales = totalMales + getAnswer(NOT_HISP_LAT_M_UNK); 
	    			}
    			break;
    	}
    	
    	return totalMales;
    }

    /* Gets unknown gender by race for a given ethnicity */
    private Integer getTotalUnknowns(int hispanic) throws JAXBException {
    	int totalUnknowns = 0;
    	
    	switch (hispanic) {
			case ETHNICITY_UNKNOWN:
				totalUnknowns = 
					getAnswer(UNK_U_AI) +
					getAnswer(UNK_U_AS) +
					getAnswer(UNK_U_BL) +
					getAnswer(UNK_U_HW) +
					getAnswer(UNK_U_MU) +
					getAnswer(UNK_U_WH) +
					getAnswer(UNK_U_UNK); 
				break;
	    	case HISPANIC:
	    		totalUnknowns = 
    				getAnswer(HISP_LAT_U_AI) +
					getAnswer(HISP_LAT_U_AS) +
					getAnswer(HISP_LAT_U_BL) +
					getAnswer(HISP_LAT_U_HW) +
					getAnswer(HISP_LAT_U_MU) +
					getAnswer(HISP_LAT_U_WH) +
					getAnswer(HISP_LAT_U_UNK); 
	    		break;
    		case NOT_HISPANIC:
    			totalUnknowns = 
    				getAnswer(NOT_HISP_LAT_U_AI) +
					getAnswer(NOT_HISP_LAT_U_AS) +
					getAnswer(NOT_HISP_LAT_U_BL) +
					getAnswer(NOT_HISP_LAT_U_HW) +
					getAnswer(NOT_HISP_LAT_U_MU) +
					getAnswer(NOT_HISP_LAT_U_WH) +
					getAnswer(NOT_HISP_LAT_U_UNK);
    			break;
    	}
    	
    	return totalUnknowns;
    }

    /* Get totals for each race */
    private PHSInclusionEnrollmentReportTotalsDataType getRaceTotals() throws JAXBException {
    	PHSInclusionEnrollmentReportTotalsDataType totals = 
    		objFactory.createPHSInclusionEnrollmentReportTotalsDataType();
    	
    	/* American Indian */
    	int AI = 
    		getAnswer(NOT_HISP_LAT_F_AI) +
			getAnswer(NOT_HISP_LAT_M_AI) +
			getAnswer(HISP_LAT_F_AI) +
			getAnswer(HISP_LAT_M_AI);
    	
			if (enrollmentType.equals(CUMULATIVE)) {    	
				AI = AI + 
					getAnswer(NOT_HISP_LAT_U_AI) +
					getAnswer(HISP_LAT_U_AI) +
					getAnswer(UNK_F_AI) +
					getAnswer(UNK_M_AI) +
					getAnswer(UNK_U_AI);
			}

    	totals.setAmericanIndian(BigInteger.valueOf(AI));
    	
    	/* Asian */
    	int AS = 
    		getAnswer(NOT_HISP_LAT_F_AS) +
			getAnswer(NOT_HISP_LAT_M_AS) +
			getAnswer(HISP_LAT_F_AS) +
			getAnswer(HISP_LAT_M_AS);
			
			if (enrollmentType.equals(CUMULATIVE)) {   
				AS = AS +
					getAnswer(NOT_HISP_LAT_U_AS) +
					getAnswer(HISP_LAT_U_AS) +
					getAnswer(UNK_F_AS) +
					getAnswer(UNK_M_AS) +
					getAnswer(UNK_U_AS);
			}
    	
    	totals.setAsian(BigInteger.valueOf(AS));
    	
    	/* Black */
    	int BL = 
    		getAnswer(NOT_HISP_LAT_F_BL) +
			getAnswer(NOT_HISP_LAT_M_BL) +
			getAnswer(HISP_LAT_F_BL) +
			getAnswer(HISP_LAT_M_BL);
			
			if (enrollmentType.equals(CUMULATIVE)) {  
				BL = BL +
					getAnswer(NOT_HISP_LAT_U_BL) +
					getAnswer(HISP_LAT_U_BL) +
					getAnswer(UNK_F_BL) +
					getAnswer(UNK_M_BL) +
					getAnswer(UNK_U_BL);
			}
    	
    	totals.setBlack(BigInteger.valueOf(BL));
    	
    	/* Hawaiian */
    	int HW = 
    		getAnswer(NOT_HISP_LAT_F_HW) +
			getAnswer(NOT_HISP_LAT_M_HW) +
			getAnswer(HISP_LAT_F_HW) +
			getAnswer(HISP_LAT_M_HW);
			
			if (enrollmentType.equals(CUMULATIVE)) {  
				HW = HW + 
				getAnswer(NOT_HISP_LAT_U_HW) +
				getAnswer(HISP_LAT_U_HW) +
				getAnswer(UNK_F_HW) +
				getAnswer(UNK_M_HW) +
				getAnswer(UNK_U_HW);
			}
    	
	    totals.setHawaiian(BigInteger.valueOf(HW));
    	
    	/* Multiple Race */
    	int MU = 
    		getAnswer(NOT_HISP_LAT_F_MU) +
			getAnswer(NOT_HISP_LAT_M_MU) +
			getAnswer(HISP_LAT_F_MU) +
			getAnswer(HISP_LAT_M_MU);
			
			if (enrollmentType.equals(CUMULATIVE)) {  
				MU = MU +
					getAnswer(NOT_HISP_LAT_U_MU) +
					getAnswer(HISP_LAT_U_MU) +
					getAnswer(UNK_F_MU) +
					getAnswer(UNK_M_MU) +
					getAnswer(UNK_U_MU);
			}
    	
	   	totals.setMultipleRace(BigInteger.valueOf(MU));
    	
    	/* White */
    	int WH = 
    		getAnswer(NOT_HISP_LAT_F_WH) +
			getAnswer(NOT_HISP_LAT_M_WH) +
			getAnswer(HISP_LAT_F_WH) +
			getAnswer(HISP_LAT_M_WH);
			
			if (enrollmentType.equals(CUMULATIVE)) {  
				WH = WH +
					getAnswer(NOT_HISP_LAT_U_WH) +
					getAnswer(HISP_LAT_U_WH) +
					getAnswer(UNK_F_WH) +
					getAnswer(UNK_M_WH) +
					getAnswer(UNK_U_WH);
			}
    	
    	totals.setWhite(BigInteger.valueOf(WH));
    	
    	/* Unknown race */
    	int UNK = 0;
		if (enrollmentType.equals(CUMULATIVE)) { 
	    	UNK = 
	    		getAnswer(NOT_HISP_LAT_F_UNK) +
				getAnswer(NOT_HISP_LAT_M_UNK) +
				getAnswer(NOT_HISP_LAT_U_UNK) +
				getAnswer(HISP_LAT_F_UNK) +
				getAnswer(HISP_LAT_M_UNK) +
				getAnswer(HISP_LAT_U_UNK) +
				getAnswer(UNK_F_UNK) +
				getAnswer(UNK_M_UNK) +
				getAnswer(UNK_U_UNK);
	    	
	    	totals.setUnknownRace(BigInteger.valueOf(UNK));
		}

    	/* Total */
    	int TO = AI + AS + BL + HW + MU + WH + UNK;
    	
    	totals.setTotal(BigInteger.valueOf(TO));
    	
    	return totals;
    }
    
    /* Gets questionnaire answers from the database */
    private CoeusVector getQuestionnaireAnswers() throws CoeusException, DBException {
    	CoeusVector cvAnswers = new CoeusVector();
         
    	PHSInclusionEnrollmentReportTxnBean_V1_0 reportTxnBean = new PHSInclusionEnrollmentReportTxnBean_V1_0();
    	cvAnswers = reportTxnBean.getQuestionnaireAnswers(propNumber);

    	return cvAnswers;
    }
   
    /* Take questionnaire answers and put in CoeusVector for easy access by question ID */
    private CoeusVector processQuestionnaireAnswers() {
    	CoeusVector cvForm = new CoeusVector();
    	CoeusVector cvInstances = new CoeusVector();
	   
    	try {
    		cvForm = getQuestionnaireAnswers();
    	} catch (CoeusException e) {
    		UtilFactory.log("CoeusException:  Unable to retrieve questionnaire answers for Inclustion Enrollment Report.");
    		e.printStackTrace();
    	} catch (DBException e) {
    		UtilFactory.log("DBException:  Unable to retrieve questionnaire answers for Inclusion Enrollment Report.");
    		e.printStackTrace();
    	}
    	
    	// Creates a set with all the question numbers that start a new instance;
    	// Study Title is the first question in each instance of the questionnaire so 
    	// this is a list of question numbers for the Study Title question
    	Set<Integer> firstInstanceQuestions = new HashSet<Integer>();
    	firstInstanceQuestions.add(1);
    	for (int i=1; i < INSTANCES; i++) {
    		firstInstanceQuestions.add((i*QUESTIONS_PER_INSTANCE) + 1);
    	}
	   
    	QuestionAnswerBean bean = new QuestionAnswerBean();
    	HashMap<Integer, String> map = new HashMap<Integer, String>();
	   
    	if (cvForm.size() > 0) {
    		int f = -1;
    		for (int v=0; v < cvForm.size(); v++) {
    			bean = (QuestionAnswerBean) cvForm.get(v);
    			
    			if (firstInstanceQuestions.contains(bean.getQuestionNumber())) {
    				if (f >= 0) {
    					cvInstances.add(f,map);
    				}
    				map = new HashMap<Integer, String>();
    				f++;
    			}
    			
    			map.put(bean.getQuestionId(),bean.getAnswer());
    		}
    		cvInstances.add(f,map);
    	}
    	return cvInstances;
    }

    /* Return questionnaire answer or 0 given a question ID */
    private Integer getAnswer(int question_id) {
    	Integer answer = 0;
    	
    	if (instanceData.containsKey(question_id)) {
    		answer = Integer.parseInt(instanceData.get(question_id));
    	}
    	
    	return answer;
    }
    
    public Object getStream(HashMap ht) throws JAXBException, CoeusException, DBException{
        this.propNumber = (String)ht.get("PROPOSAL_NUMBER");
        return getInclusionEnrollmentReport();
    }    
    
   
}
