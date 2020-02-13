/**
 * PlannedReportStream_V1_0
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
import edu.mit.coeus.questionnaire.bean.QuestionAnswerBean;
import edu.mit.coeus.utils.CoeusVector;
import edu.mit.coeus.utils.DateUtils;
import edu.mit.coeus.utils.UtilFactory;
import edu.mit.coeus.utils.dbengine.DBException;
import edu.mit.coeus.xml.generator.CoeusXMLException;
import edu.mit.coeus.xml.generator.CoeusXMLGenrator;

import edu.vanderbilt.coeus.s2s.bean.PlannedEnrollmentReportTxnBean;
import gov.grants.apply.forms.planned_report_v1_0.*;

 public class PlannedReportStream_V1_0 extends edu.mit.coeus.s2s.generator.S2SBaseStream {
    private gov.grants.apply.forms.planned_report_v1_0.ObjectFactory objFactory;
    private gov.grants.apply.system.global_v1.ObjectFactory globalObjFactory;
    private gov.grants.apply.system.globallibrary_v2.ObjectFactory globalLibraryObjFactory;
    private gov.grants.apply.system.attachments_v1.ObjectFactory attachmentsObjFactory;
    private CoeusXMLGenrator xmlGenerator;
 
    private ProposalDevelopmentTxnBean propDevTxnBean;
    private ProposalDevelopmentFormBean propDevFormBean;
    
    private PlannedReportType plannedReport = null;
    private String propNumber;
    
    // Questionnaire ID for Planned Enrollment Report questionnaire
    public static final int QUESTIONNAIRE = 6;
    
    // Question IDs for Planned Enrollment Report questionnaire
    public static final int STUDY_TITLE = 578;
    public static final int DOM_FOR = 501;
    public static final int NOT_HISP_LAT = 502;
    public static final int NOT_HISP_LAT_F = 503;
    public static final int NOT_HISP_LAT_F_AI = 504;
    public static final int NOT_HISP_LAT_F_AS = 505;
    public static final int NOT_HISP_LAT_F_HW = 506;
    public static final int NOT_HISP_LAT_F_BL = 507;
    public static final int NOT_HISP_LAT_F_WH = 508;
    public static final int NOT_HISP_LAT_F_MU = 509;
    public static final int NOT_HISP_LAT_M = 511;
    public static final int NOT_HISP_LAT_M_AI = 512;
    public static final int NOT_HISP_LAT_M_AS = 513;
    public static final int NOT_HISP_LAT_M_HW = 514;
    public static final int NOT_HISP_LAT_M_BL = 515;
    public static final int NOT_HISP_LAT_M_WH = 516;
    public static final int NOT_HISP_LAT_M_MU = 517;
    public static final int HISP_LAT = 527;
    public static final int HISP_LAT_F = 528;
    public static final int HISP_LAT_F_AI = 529;
    public static final int HISP_LAT_F_AS = 530;
    public static final int HISP_LAT_F_HW = 531;
    public static final int HISP_LAT_F_BL = 532;
    public static final int HISP_LAT_F_WH = 533;
    public static final int HISP_LAT_F_MU = 534;
    public static final int HISP_LAT_M = 536;
    public static final int HISP_LAT_M_AI = 537;
    public static final int HISP_LAT_M_AS = 538;
    public static final int HISP_LAT_M_HW = 539;
    public static final int HISP_LAT_M_BL = 540;
    public static final int HISP_LAT_M_WH = 541;
    public static final int HISP_LAT_M_MU = 542;
    public static final int COMMENTS = 577;
    
    // Number of questions per full form instance
    public static final int QUESTIONS_PER_INSTANCE = 34;
    
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
	
    /** Creates a new instance of PlannedReportStream_V1_0 */
    public PlannedReportStream_V1_0(){
        objFactory = new gov.grants.apply.forms.planned_report_v1_0.ObjectFactory();
        globalObjFactory = new gov.grants.apply.system.global_v1.ObjectFactory();
        attachmentsObjFactory = new gov.grants.apply.system.attachments_v1.ObjectFactory();
        globalLibraryObjFactory = new gov.grants.apply.system.globallibrary_v2.ObjectFactory();
        xmlGenerator = new CoeusXMLGenrator();
        
        propDevTxnBean = new ProposalDevelopmentTxnBean();
    } 
   
    /* Initializes the report */
    private PlannedReportType getPlannedReport() throws CoeusXMLException,CoeusException,DBException{
      
        try{
        	plannedReport = objFactory.createPlannedReport();
        	plannedReport.setFormVersion("1.0");

            questionnaireData = processQuestionnaireAnswers();

            for (int q = 0; q < questionnaireData.size(); q++) {
            	instanceData = (HashMap) questionnaireData.get(q);
	        	plannedReport.getStudy().add(createStudy());
            }
           
         }
        catch (JAXBException jaxbEx){
            UtilFactory.log(jaxbEx.getMessage(),jaxbEx,"PlannedReportStream_V1_0","getPlannedReport()");
            throw new CoeusXMLException(jaxbEx.getMessage());
        }
        return plannedReport;
    }
    
    /* Gets and sets the form data */
    private PlannedReportType.StudyType createStudy() 
    	throws JAXBException,CoeusException,DBException {
    	   
    	PlannedReportType.StudyType study = objFactory.createPlannedReportTypeStudyType();
    	
        String title = (String) instanceData.get(STUDY_TITLE);          
        if (title != null && title.length() > 250){
        	study.setStudyTitle(title.substring(0,250));
        }
        else{
        	study.setStudyTitle(title);
        }
    	
        // Set form data based on questionnaire answers
        study.setDomesticForeignIndicator((String) instanceData.get(DOM_FOR));
        study.setComments((String) instanceData.get(COMMENTS));
    	
        study.setHispanic(getHispanic());
        study.setNotHispanic(getNotHispanic());
    	
        study.setTotal(getRaceTotals());

        return study;
    }
    
    /* Gets Hispanic ethnicity numbers by gender */
    private PlannedReportEthnicCategoryDataType getHispanic() throws JAXBException {
    	PlannedReportEthnicCategoryDataType hispanic = 
    		objFactory.createPlannedReportEthnicCategoryDataType();
    	hispanic.setFemale(getFemale(HISPANIC));
    	hispanic.setMale(getMale(HISPANIC));
    	return hispanic;
    }

    /* Get Not Hispanic ethnicity numbers by gender */
    private PlannedReportEthnicCategoryDataType getNotHispanic() throws JAXBException {
    	PlannedReportEthnicCategoryDataType notHispanic = 
    		objFactory.createPlannedReportEthnicCategoryDataType();
    	notHispanic.setFemale(getFemale(NOT_HISPANIC));
    	notHispanic.setMale(getMale(NOT_HISPANIC));
    	return notHispanic;
    }
    
    /* Gets females by race for a given ethnicity */
    private PlannedReportRacialCategoryDataType getFemale(int hispanic) throws JAXBException {
    	PlannedReportRacialCategoryDataType female = 
    		objFactory.createPlannedReportRacialCategoryDataType();
    	
    	switch (hispanic) {
    		case HISPANIC:
		    	female.setAmericanIndian(getAnswer(HISP_LAT_F_AI));
		    	female.setAsian(getAnswer(HISP_LAT_F_AS));
		    	female.setBlack(getAnswer(HISP_LAT_F_BL));
		    	female.setHawaiian(getAnswer(HISP_LAT_F_HW));
		    	female.setMultipleRace(getAnswer(HISP_LAT_F_MU));
		    	female.setWhite(getAnswer(HISP_LAT_F_WH));
		    	
		    	female.setTotal(getTotals(FEMALE, HISPANIC)); 
		    	break;
    		case NOT_HISPANIC:
		    	female.setAmericanIndian(getAnswer(NOT_HISP_LAT_F_AI));
		    	female.setAsian(getAnswer(NOT_HISP_LAT_F_AS));
		    	female.setBlack(getAnswer(NOT_HISP_LAT_F_BL));
		    	female.setHawaiian(getAnswer(NOT_HISP_LAT_F_HW));
		    	female.setMultipleRace(getAnswer(NOT_HISP_LAT_F_MU));
		    	female.setWhite(getAnswer(NOT_HISP_LAT_F_WH));

		    	female.setTotal(getTotals(FEMALE, NOT_HISPANIC)); 
		    	break;
    	}
    	return female;
    }

    /* Gets males by race for a given ethnicity */
    private PlannedReportRacialCategoryDataType getMale(int hispanic) throws JAXBException {
    	PlannedReportRacialCategoryDataType male = 
    		objFactory.createPlannedReportRacialCategoryDataType();
    	
    	switch (hispanic) {
	    	case HISPANIC:
		    	male.setAmericanIndian(getAnswer(HISP_LAT_M_AI));
		    	male.setAsian(getAnswer(HISP_LAT_M_AS));
		    	male.setBlack(getAnswer(HISP_LAT_M_BL));
		    	male.setHawaiian(getAnswer(HISP_LAT_M_HW));
		    	male.setMultipleRace(getAnswer(HISP_LAT_M_MU));
		    	male.setWhite(getAnswer(HISP_LAT_M_WH));
		    	
		    	male.setTotal(getTotals(MALE, HISPANIC)); 
		    	break;
	    	case NOT_HISPANIC:
		    	male.setAmericanIndian(getAnswer(NOT_HISP_LAT_M_AI));
		    	male.setAsian(getAnswer(NOT_HISP_LAT_M_AS));
		    	male.setBlack(getAnswer(NOT_HISP_LAT_M_BL));
		    	male.setHawaiian(getAnswer(NOT_HISP_LAT_M_HW));
		    	male.setMultipleRace(getAnswer(NOT_HISP_LAT_M_MU));
		    	male.setWhite(getAnswer(NOT_HISP_LAT_M_WH));
		    	
		    	male.setTotal(getTotals(MALE, NOT_HISPANIC));
		    	break;
		    	
    	}
    	
    	return male;
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
    	}
    	
    	BigInteger total =  new BigInteger(new Integer(addItUp).toString());
    	
    	return total;
    }
    
    /* Gets total females by race for a given ethnicity */
    private Integer getTotalFemales(int hispanic) throws JAXBException {
    	int totalFemales = 0;
    	
    	switch (hispanic) {
    		case HISPANIC:
    			totalFemales = 
    				getAnswer(HISP_LAT_F_AI) +
    				getAnswer(HISP_LAT_F_AS) +
    				getAnswer(HISP_LAT_F_BL) +
    				getAnswer(HISP_LAT_F_HW) +
    				getAnswer(HISP_LAT_F_MU) +
    				getAnswer(HISP_LAT_F_WH);
    			break;
    		case NOT_HISPANIC:
    			totalFemales = 
    				getAnswer(NOT_HISP_LAT_F_AI) +
    				getAnswer(NOT_HISP_LAT_F_AS) +
    				getAnswer(NOT_HISP_LAT_F_BL) +
    				getAnswer(NOT_HISP_LAT_F_HW) +
    				getAnswer(NOT_HISP_LAT_F_MU) +
    				getAnswer(NOT_HISP_LAT_F_WH);
    			break;
    	}
    	
    	return totalFemales;
    }
    
    /* Gets males by race for a given ethnicity */
    private Integer getTotalMales(int hispanic) throws JAXBException {
    	int totalMales = 0;
    	
    	switch (hispanic) {
    		case HISPANIC:
    			totalMales = 
    				getAnswer(HISP_LAT_M_AI) +
					getAnswer(HISP_LAT_M_AS) +
					getAnswer(HISP_LAT_M_BL) +
					getAnswer(HISP_LAT_M_HW) +
					getAnswer(HISP_LAT_M_MU) +
					getAnswer(HISP_LAT_M_WH);
    			break;
    		
    		case NOT_HISPANIC:
    			totalMales = 
    				getAnswer(NOT_HISP_LAT_M_AI) +
					getAnswer(NOT_HISP_LAT_M_AS) +
					getAnswer(NOT_HISP_LAT_M_BL) +
					getAnswer(NOT_HISP_LAT_M_HW) +
					getAnswer(NOT_HISP_LAT_M_MU) +
					getAnswer(NOT_HISP_LAT_M_WH);
    			break;
    	}
    	
    	return totalMales;
    }
    
    /* Get totals for each race */
    private PlannedReportTotalsDataType getRaceTotals() throws JAXBException {
    	PlannedReportTotalsDataType totals = 
    		objFactory.createPlannedReportTotalsDataType();
    	
    	/* American Indian */
    	int AI = getAnswer(NOT_HISP_LAT_F_AI) +
			getAnswer(NOT_HISP_LAT_M_AI) +
			getAnswer(HISP_LAT_F_AI) +
			getAnswer(HISP_LAT_M_AI);

    	totals.setAmericanIndian(BigInteger.valueOf(AI));
    	
    	/* Asian */
    	int AS = getAnswer(NOT_HISP_LAT_F_AS) +
			getAnswer(NOT_HISP_LAT_M_AS) +
			getAnswer(HISP_LAT_F_AS) +
			getAnswer(HISP_LAT_M_AS);
    	
    	totals.setAsian(BigInteger.valueOf(AS));
    	
    	/* Black */
    	int BL = getAnswer(NOT_HISP_LAT_F_BL) +
			getAnswer(NOT_HISP_LAT_M_BL) +
			getAnswer(HISP_LAT_F_BL) +
			getAnswer(HISP_LAT_M_BL);
    	
    	totals.setBlack(BigInteger.valueOf(BL));
    	
    	/* Hawaiian */
    	int HW = getAnswer(NOT_HISP_LAT_F_HW) +
			getAnswer(NOT_HISP_LAT_M_HW) +
			getAnswer(HISP_LAT_F_HW) +
			getAnswer(HISP_LAT_M_HW);
    	
	    totals.setHawaiian(BigInteger.valueOf(HW));
    	
    	/* Multiple Race */
    	int MU = getAnswer(NOT_HISP_LAT_F_MU) +
			getAnswer(NOT_HISP_LAT_M_MU) +
			getAnswer(HISP_LAT_F_MU) +
			getAnswer(HISP_LAT_M_MU);
    	
	   	totals.setMultipleRace(BigInteger.valueOf(MU));
    	
    	/* White */
    	int WH = getAnswer(NOT_HISP_LAT_F_WH) +
			getAnswer(NOT_HISP_LAT_M_WH) +
			getAnswer(HISP_LAT_F_WH) +
			getAnswer(HISP_LAT_M_WH);
    	
    	totals.setWhite(BigInteger.valueOf(WH));

    	/* Total */
    	int TO = AI + AS + BL + HW + MU + WH;
    	
    	totals.setTotal(BigInteger.valueOf(TO));
    	
    	return totals;
    }
    
    /* Get proposal data */
    private ProposalDevelopmentFormBean getPropDevData() throws DBException,CoeusException {
        if (propNumber == null) 
            throw new CoeusXMLException("Proposal Number is Null");
        return propDevTxnBean.getProposalDevelopmentDetails(propNumber);
    }
  
    /* Gets questionnaire answers from the database */
    private CoeusVector getQuestionnaireAnswers() throws CoeusException, DBException {
    	CoeusVector cvAnswers = new CoeusVector();
         
    	PlannedEnrollmentReportTxnBean reportTxnBean = new PlannedEnrollmentReportTxnBean();
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
    		UtilFactory.log("CoeusException:  Unable to retrieve questionnaire answers for Planned Enrollment Report.");
    		e.printStackTrace();
    	} catch (DBException e) {
    		UtilFactory.log("DBException:  Unable to retrieve questionnaire answers for Planned Enrollment Report.");
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
    
    /* Some utilities */
   	private String checkNull (String s) {
    	if (s == null){
    		return "Unknown";
    	}
    	else {
    		return s;
    	}
    }
     
    private Calendar getCal(Date date){
        return DateUtils.getCal(date);
    }
    
    private Calendar getTodayDate() {
        return DateUtils.getTodayDate();
    }
    
    public Object getStream(HashMap ht) throws JAXBException, CoeusException, DBException{
        this.propNumber = (String)ht.get("PROPOSAL_NUMBER");
        return getPlannedReport();
    }    
    
   
}
