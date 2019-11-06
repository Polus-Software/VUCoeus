/*
 * @(#)PersonMaintenanceServlet.java 1.0 3/13/03 11:49 AM
 *
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 */

/*
 * PMD check performed, and commented unused imports and variables on 19-APR-2011
 * by Maharaja Palanichamy
 */

package edu.mit.coeus.servlet;

import edu.mit.coeus.brokers.RequesterBean;
import edu.mit.coeus.brokers.ResponderBean;
import edu.mit.coeus.bean.*;
import edu.mit.coeus.budget.report.ReportGenerator;
//import edu.mit.coeus.irb.bean.PersonInfoTxnBean;
import edu.mit.coeus.sponsormaint.bean.SponsorMaintenanceDataTxnBean;
import edu.mit.coeus.departmental.bean.*;
//import edu.mit.coeus.utils.xml.bean.instProp.generator.InstPropStream;

import edu.mit.coeus.utils.UtilFactory;
import edu.mit.coeus.utils.CoeusConstants;
import edu.mit.coeus.utils.TypeConstants;
import edu.mit.coeus.utils.dbengine.DBException;
import edu.mit.coeus.exception.CoeusException;
import java.util.HashMap;

import javax.servlet.http.*;
import javax.servlet.*;
import java.io.*;
//import java.text.DecimalFormat;
import java.util.Vector;
import java.util.Hashtable;
//import java.util.Properties;
import java.text.SimpleDateFormat;
import java.util.Date;
import edu.mit.coeus.user.bean.UserMaintDataTxnBean;
import edu.mit.coeus.utils.CoeusProperties;
import edu.mit.coeus.utils.CoeusPropertyKeys;
import edu.mit.coeus.utils.CoeusVector;

public class PersonMaintenanceServlet extends CoeusBaseServlet implements TypeConstants{
    
//    private UtilFactory UtilFactory = new UtilFactory();
    private static final String SAVE_DEGREE_DETAILS = "saveDegreeDetails";
    private static final String SAVE_BIOGRAPHY_DETAILS = "saveBiographyDetails";
    private static final String GET_BIOGRAPHY_DETAILS_FOR_PERSON = "getBiographyDetails";
    private static final String GET_DEGREE_DETAILS_FOR_PERSON = "getDegreeDetails";
    private static final String GET_BIOGRAPHY_PDF_DETAILS_FOR_PERSON  = "getBiographyPDFDetails";
    
    private static final char GET_PERSON_BIO_PDF = 'B';
    private static final char GET_PERSON_BIO_SOURCE = 'C';
    private static final char GET_PERSON_TRAINING = 'E';
    private static final char UPDATE_PERSON_TRAINING = 'F';
    private static final char ADD_PERSON = 'G';
    private static final char VALIDATE_PERSON_ID = 'H';
    private static final char USER_HAS_ADD_PERSON_RIGHT = 'J';
    private static final char GET_CURRENT_AND_PENDING_REPORT_DATA = 'K';
//    private static final char PRINT_CURRENT = 'L';
//    private static final char PRINT_PENDING = 'O';
    
    //For Case #1602 Start 1
    private static final char STATE_COUNTRY_DATA = 'P';
    //For Case #1602 End 1
    
    //Added for Coeus 4.3 PT ID - 2388:Person Enhancements - start
    //used to get custom elements for a person
    private static final char GET_PERSON_CUSTOM_ELEMENTS = 'Q';
    //check user has right to modufy person details
    private static final char USER_HAS_MODIFY_PERSON_RIGHT = 'R';
    //Added for Coeus 4.3 PT ID - 2388:Person Enhancements - end
    // 3282: Reviewer view of Protocols
    private static final char GET_USER_INFO = 'U';
    // 4467: If you put a country other than US, the state drop down should display the 'states' for the country.
    private static final char GET_STATE_COUNRTY_DATA_FOR_ALL = 'I';
    //Rights
    private static final String MAINTAIN_TRAINING = "MAINTAIN_TRAINING";
    private static final String ADD_PERSON_RIGHT = "ADD_PERSON";
    //Added for Coeus 4.3 PT ID - 2388:Person Enhancements - start
    //holds the Modify person right
    private static final String MODIFY_PERSON_RIGHT = "MODIFY_PERSON";
    //Added for Coeus 4.3 PT ID - 2388:Person Enhancements - end
    //Added for COEUSQA-2291 : Hide Reviewer Name in Review Comments - Start
    private static final char GET_PERSON_ID_FOR_USER = 'p';
    //COEUSQA-2291 : End
    /**
     *  This method is used for applets.
     *  Post the information into server using object serialization.
     */
    
    private void setBiographyNumbers(String pId, Vector biographyData){
        
        int maxBioNumber = 0;
        DepartmentBioPersonFormBean bioBean = null;
        
        maxBioNumber = getMaxPersonBiographyNumber(pId);
        int size = biographyData.size();
        for(int index = 0; index < size; index++){
            bioBean = (DepartmentBioPersonFormBean)biographyData.elementAt(index);
            if(bioBean.getAcType() != null){
                if(bioBean.getAcType().equalsIgnoreCase(INSERT_RECORD)){
                    maxBioNumber = maxBioNumber + 1;
                    bioBean.setBioNumber(maxBioNumber);
                }
            }
        }
    }
    
    private int getMaxPersonBiographyNumber(String pId){
        int maxNo = 0;
        try{
            DepartmentPersonTxnBean departmentPersonTxnBean = new DepartmentPersonTxnBean();
            maxNo = departmentPersonTxnBean.getPersonBiographyNumber(pId);
        }catch(Exception e){
            e.printStackTrace();
        }
        return maxNo;
    }
    
    public void doPost(HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {
        
        // the request object from applet
        RequesterBean requester = null;
        // the response object to applet
        ResponderBean responder = new ResponderBean();
        
        // open object input/output streams
        ObjectInputStream inputFromApplet = null;
        ObjectOutputStream outputToApplet = null;
        
        String loggedinUser ="";
        String personId = "";
//        String personName = "";
        String homeUnit = "";
//        String userName = "";
        String userUnitNumber = "";
        String moduleCode = "";
//        String getDegreeDetails = "";
        String getDetails = "";
//        String getBiographyDetails = "";
        boolean userCanMaintainUnitPersonInfo = false;
        boolean userCanMaintainPersonInfo = false;
        //Added for case 3761 - Modify Person right issue - start
        boolean userCanModifyPersonInfo = false; 
        //Added for case 3761 - Modify Person right issue - end
        char functionType;
        Vector dataObjects = new Vector();
        
//        PersonInfoTxnBean personInfoTxnBean = new PersonInfoTxnBean();
        DepartmentPersonTxnBean departmentPersonTxnBean =
                new DepartmentPersonTxnBean();
        SponsorMaintenanceDataTxnBean sponsorMaintenanceDataTxnBean =
                new SponsorMaintenanceDataTxnBean();
        
        DepartmentPersonFormBean departmentPersonFormBean = null;
//        PersonCustomElementsInfoBean personCustomElementsInfoBean = null;
        Vector departmentOthersFormBeanVector = new Vector();
        Vector custColumnsForModuleVector = new Vector();
        //COEUSQA-1686 - Add additional fields to the Current  Pending Support Schema - Start
        boolean investigatorAndKeyPerson = false;
        //COEUSQA-1686 - Add additional fields to the Current  Pending Support Schema - End
        
        try {
            inputFromApplet = new ObjectInputStream(request.getInputStream());
            requester = (RequesterBean) inputFromApplet.readObject();
            isValidRequest(requester);
            UserInfoBean userBean = (UserInfoBean)new
                    UserDetailsBean().getUserInfo(requester.getUserName());
            loggedinUser = userBean.getUserId();
            DepartmentPersonUpdateTxnBean departmentPersonUpdateTxnBean =
                    new DepartmentPersonUpdateTxnBean(loggedinUser);
            functionType = requester.getFunctionType();
            if (functionType == SAVE_RECORD){
                boolean success = false;
                String requestId = (requester.getId() == null ? ""
                        : (String)requester.getId());
                if(requestId.equalsIgnoreCase(SAVE_DEGREE_DETAILS)){
                    Vector vecDegreeData = (Vector)requester.getDataObject();
                    if(vecDegreeData != null){
                        success = departmentPersonUpdateTxnBean.mainAddUpdDepartmentPersonDegree(vecDegreeData);
                    }
                    responder.setResponseStatus(success);
                }else if(requestId.equalsIgnoreCase(SAVE_BIOGRAPHY_DETAILS)){
                    
                    Vector vecData = (Vector)requester.getDataObject();
                    Vector vecBiographyData = (Vector)vecData.get(0);
                    String personNumber = (String)vecData.get(1);
                    
                    if(vecBiographyData != null){
                        setBiographyNumbers(personNumber, vecBiographyData);
                        success = departmentPersonUpdateTxnBean.mainAddUpdPersonBio(vecBiographyData);
                        vecBiographyData = departmentPersonTxnBean.getBioPerson(personNumber);
                        responder.setDataObjects(vecBiographyData);
                    }
                    responder.setResponseStatus(success);
                }else{
                    Vector otherTabData = (Vector)requester.getDataObject();
                    if(otherTabData != null){
                        success = departmentPersonUpdateTxnBean.mainAddUpdPersonOther(otherTabData);
                    }
                    responder.setResponseStatus(success);
                }
            }else if (functionType == DISPLAY_MODE || functionType == MODIFY_MODE){
                
//                personName = (requester.getId() == null ? ""
//                    : (String)requester.getId());
                personId = requester.getId();
                
//                personId = personInfoTxnBean.getPersonID(personName);
                
                moduleCode = departmentPersonTxnBean.getParameterValues("COEUS_MODULE_PERSON");
                getDetails = (String)requester.getDataObject();
                
                homeUnit = departmentPersonTxnBean.getHomeUnit(personId);
                
                if(homeUnit != null){
                    userCanMaintainUnitPersonInfo =
                            sponsorMaintenanceDataTxnBean.isUserHasRight(loggedinUser, homeUnit, "MAINTAIN_PERSON_INFO");
                    //Added for case 3761 - Modify Person right issue - start
                    UserMaintDataTxnBean userMaintDataTxnBean = new UserMaintDataTxnBean();
                    userCanModifyPersonInfo = userMaintDataTxnBean.getUserHasRight(loggedinUser, MODIFY_PERSON_RIGHT, homeUnit);
                }else{
                    userCanModifyPersonInfo = true;
                }
                //Added for case 3761 - Modify Person right issue - end
                
                Vector userInformation = (Vector)departmentPersonTxnBean.getUserInfoForPerson(personId);
                
//                String id = (String)userInformation.elementAt(0);
                userUnitNumber = (String)userInformation.elementAt(1);
                
                if(userUnitNumber != null){
                    userCanMaintainPersonInfo =
                            sponsorMaintenanceDataTxnBean.isUserHasRight(loggedinUser, userUnitNumber, "MAINTAIN_PERSON_INFO");
                }
                
                if(getDetails != null
                        && getDetails.trim().length() > 0
                        && getDetails.equalsIgnoreCase(GET_DEGREE_DETAILS_FOR_PERSON)){
                    
                    Vector vecDegreeTypeCodes = (Vector)departmentPersonTxnBean.getDegreeTypeCodeDescription();
                    Vector vecSchoolCodes = (Vector)departmentPersonTxnBean.getSchoolCodeDescription();
                    Vector vecDegreeData = (Vector)departmentPersonTxnBean.getDepartmentPersonDegree(personId);
                    
                    dataObjects.addElement(vecDegreeTypeCodes);
                    dataObjects.addElement(vecSchoolCodes);
                    dataObjects.addElement(vecDegreeData);
                    dataObjects.addElement(new Boolean(userCanMaintainUnitPersonInfo));
                    dataObjects.addElement(new Boolean(userCanMaintainPersonInfo));
                    
                    responder.setDataObject(dataObjects);
                    responder.setId(personId);
                    responder.setResponseStatus(true);
                    
                } else if(getDetails != null
                        && getDetails.trim().length() > 0
                        && getDetails.equalsIgnoreCase(GET_BIOGRAPHY_DETAILS_FOR_PERSON)){
                    
                    Vector vecDataObjs = new Vector();
                    Vector vecBiographyData = null;
                    if(personId != null){
                        vecBiographyData = (Vector)departmentPersonTxnBean.getBioPerson(personId);
                    }
                    vecDataObjs.addElement(vecBiographyData);
                    vecDataObjs.addElement(new Boolean(userCanMaintainUnitPersonInfo));
                    vecDataObjs.addElement(new Boolean(userCanMaintainPersonInfo));
                    
                    responder.setDataObject(vecDataObjs);
                    responder.setId(personId);
                    responder.setResponseStatus(true);
                    
                }else if(getDetails != null
                        && getDetails.trim().length() > 0
                        && getDetails.equalsIgnoreCase(GET_BIOGRAPHY_PDF_DETAILS_FOR_PERSON)){
                    
                    Vector vecDataObjs = new Vector();
                    Vector vecBiographyPDFData = null;
                    if(personId != null){
                        vecBiographyPDFData = (Vector)departmentPersonTxnBean.getBioPDFPerson(personId);
                    }
                    vecDataObjs.addElement(vecBiographyPDFData);
                    vecDataObjs.addElement(new Boolean(userCanMaintainUnitPersonInfo));
                    vecDataObjs.addElement(new Boolean(userCanMaintainPersonInfo));
                    
                    responder.setDataObject(vecDataObjs);
                    responder.setId(personId);
                    responder.setResponseStatus(true);
                    
                }else if(personId != null && personId.trim().length() > 0){
                    
                    departmentPersonFormBean =
                            departmentPersonTxnBean.getPersonDetails(personId);
                    
                    departmentOthersFormBeanVector =
                            (Vector)departmentPersonTxnBean.getPersonOthersDetails(personId);
                    
                    // Added by NAdh
                    //start 2 Aug 2004
                    CoeusVector training = departmentPersonTxnBean.getTraining();
                    CoeusVector personTraining = departmentPersonTxnBean.getDepartmentPersonTraining(personId);
                    //nadh end 2 Aug 2004
                    
                    //homeUnit = departmentPersonTxnBean.getHomeUnit(personId);
                    
                    if(moduleCode != null && moduleCode.trim().length() > 0){
                        custColumnsForModuleVector = (Vector)departmentPersonTxnBean.getPersonColumnModule(moduleCode);
                    }
                    
                    dataObjects.addElement(departmentPersonFormBean);
                    dataObjects.addElement(new Boolean(userCanMaintainUnitPersonInfo));
                    dataObjects.addElement(new Boolean(userCanMaintainPersonInfo));
                    dataObjects.addElement(custColumnsForModuleVector);
                    dataObjects.addElement(departmentOthersFormBeanVector);
                    // Added by NAdh
                    //start 2 Aug 2004
                    dataObjects.addElement(training);
                    dataObjects.addElement(personTraining);
                    //nadh end 2 Aug 2004
                    //Added for case 3761 - Modify Person right issue - end
                    dataObjects.addElement(new Boolean(userCanModifyPersonInfo));
                    //Added for case 3761 - Modify Person right issue - end
                    responder.setDataObjects(dataObjects);
                    responder.setId(personId);
                    responder.setResponseStatus(true);
                }
            }else if(functionType == GET_PERSON_BIO_PDF){
                DepartmentBioPDFPersonFormBean departmentBioPDFPersonFormBean = (DepartmentBioPDFPersonFormBean)requester.getDataObject();
                departmentBioPDFPersonFormBean = departmentPersonTxnBean.getPersonBioPDF(departmentBioPDFPersonFormBean);
                
                if(departmentBioPDFPersonFormBean!=null && departmentBioPDFPersonFormBean.getFileBytes() != null){
                    byte[] fileData = departmentBioPDFPersonFormBean.getFileBytes();
                    //Create the PDF file in Server
                    CoeusConstants.SERVER_HOME_PATH = this.getServletContext().getRealPath("/");
//                    InputStream is = getClass().getResourceAsStream("/coeus.properties");
//                    Properties coeusProps = new Properties();
//                    coeusProps.load(is);
                    String reportPath = CoeusProperties.getProperty(CoeusPropertyKeys.REPORT_GENERATED_PATH); //get path (to generate PDF) from config
                    
                    String filePath = CoeusConstants.SERVER_HOME_PATH+File.separator+reportPath;
                    File reportDir = new File(filePath);
                    if(!reportDir.exists()){
                        reportDir.mkdirs();
                    }
                    SimpleDateFormat dateFormat= new SimpleDateFormat("MMddyyyy-hhmmss");
                    File reportFile = new File(reportDir + File.separator + "PersonBioPDF"+dateFormat.format(new Date())+".pdf");
                    
                    reportFile.deleteOnExit();
                    
                    FileOutputStream fos = new FileOutputStream(reportFile);
                    fos.write( fileData,0,fileData.length );
                    fos.close();
                    dataObjects.addElement("/"+reportPath+"/"+reportFile.getName());
                }else{
                    dataObjects.addElement(null);
                }
                responder.setDataObjects(dataObjects);
                responder.setResponseStatus( true );
            }
            //Commented for case 3685 - Removed source tables from database - start
//            else if(functionType == GET_PERSON_BIO_SOURCE){
//                DepartmentBioSourceFormBean departmentBioSourceFormBean = (DepartmentBioSourceFormBean)requester.getDataObject();
//                departmentBioSourceFormBean = departmentPersonTxnBean.getPersonBioSource(departmentBioSourceFormBean);
//                
//                if(departmentBioSourceFormBean!=null && departmentBioSourceFormBean.getFileBytes() != null){
//                    byte[] fileData = departmentBioSourceFormBean.getFileBytes();
//                    //Create the PDF file in Server
//                    CoeusConstants.SERVER_HOME_PATH = this.getServletContext().getRealPath("/");
//                    InputStream is = getClass().getResourceAsStream("/coeus.properties");
//                    Properties coeusProps = new Properties();
//                    coeusProps.load(is);
//                    String reportPath = coeusProps.getProperty("REPORT_GENERATED_PATH"); //get path (to generate PDF) from config
//                    
//                    String filePath = CoeusConstants.SERVER_HOME_PATH+File.separator+reportPath;
//                    File reportDir = new File(filePath);
//                    if(!reportDir.exists()){
//                        reportDir.mkdirs();
//                    }
//                    SimpleDateFormat dateFormat= new SimpleDateFormat("MMddyyyy-hhmmss");
//                    File reportFile = new File(reportDir + File.separator + "PersonBioSource"+dateFormat.format(new Date())+".doc");
//                    
//                    reportFile.deleteOnExit();
//                    
//                    FileOutputStream fos = new FileOutputStream(reportFile);
//                    fos.write( fileData,0,fileData.length );
//                    fos.close();
//                    dataObjects.addElement("/"+reportPath+"/"+reportFile.getName());
//                }else{
//                    dataObjects.addElement(null);
//                }
//                responder.setDataObjects(dataObjects);
//                responder.setResponseStatus( true );
//            }
            //Commented for case 3685 - Removed source tables from database - end
            else if(functionType == GET_PERSON_TRAINING){
                personId = (String)requester.getDataObject();
                UserMaintDataTxnBean userMaintDataTxnBean = new UserMaintDataTxnBean();
                // 3587: Multi Campus Enahncements - Start
//                boolean hasRight = userMaintDataTxnBean.getUserHasOSPRight(loggedinUser, MAINTAIN_TRAINING);
                boolean hasRight = false;
                departmentPersonFormBean = departmentPersonTxnBean.getPersonDetails(personId);
                if(departmentPersonFormBean.getHomeUnit() != null &&
                        !"".equals(departmentPersonFormBean.getHomeUnit())){
                    hasRight = userMaintDataTxnBean.getUserHasRight(loggedinUser, MAINTAIN_TRAINING, departmentPersonFormBean.getHomeUnit());
                }
                // 3587: Multi Campus Enahncements - End
                CoeusVector training = departmentPersonTxnBean.getTraining();
                CoeusVector personTraining = departmentPersonTxnBean.getDepartmentPersonTraining(personId);

                dataObjects = new Vector();
                dataObjects.addElement(new Boolean(hasRight));
                dataObjects.addElement(training);
                dataObjects.addElement(personTraining);
                
                responder.setDataObjects(dataObjects);
                responder.setResponseStatus( true );
            }else if(functionType == UPDATE_PERSON_TRAINING){
                CoeusVector personTraining = (CoeusVector)requester.getDataObject();
                boolean success = departmentPersonUpdateTxnBean.addUpdDepartmentPersonTraining(personTraining);
                
                responder.setResponseStatus( true );
            }else if(functionType == ADD_PERSON){
                //Modified for Coeus 4.3 PT ID - 2388:Person Enhancements - start
                //Included the functionality to save person custom elements
                //departmentPersonFormBean = (DepartmentPersonFormBean)requester.getDataObject();
                Vector vecServerDataObject = requester.getDataObjects();
                if(vecServerDataObject!=null && vecServerDataObject.size()>=1){
                    departmentPersonFormBean = (DepartmentPersonFormBean)vecServerDataObject.get(0);
                    Vector vecPersonCustomElements = (Vector)vecServerDataObject.get(1);
                    boolean success = departmentPersonUpdateTxnBean.addUpdPerson(departmentPersonFormBean);
                    success = departmentPersonUpdateTxnBean.mainAddUpdPersonOther(vecPersonCustomElements);
                    responder.setResponseStatus( true );
                }else
                    responder.setResponseStatus(false);
                responder.setMessage(null);
                //Modified for Coeus 4.3 PT ID - 2388:Person Enhancements - end
            }
            //Added for Coeus 4.3 PT ID - 2388:Person Enhancements - start
            // To get the Person custom elements
            else if(functionType == GET_PERSON_CUSTOM_ELEMENTS){
                moduleCode = departmentPersonTxnBean.getParameterValues("COEUS_MODULE_PERSON");
                if(moduleCode != null && moduleCode.trim().length() > 0){
                    custColumnsForModuleVector = (Vector)departmentPersonTxnBean.getPersonColumnModule(moduleCode);
                }
                dataObjects = new Vector();
                dataObjects.addElement(custColumnsForModuleVector);
                responder.setDataObjects(dataObjects);
                responder.setResponseStatus(true);
            }//Added for Coeus 4.3 PT ID - 2388:Person Enhancements - end
            else if(functionType == VALIDATE_PERSON_ID){
                personId = (String)requester.getDataObject();
                boolean isExist = departmentPersonTxnBean.isPersonExists(personId);
                responder.setDataObject(new Boolean(isExist));
                responder.setResponseStatus( true );
                responder.setMessage(null);
            }else if(functionType == USER_HAS_ADD_PERSON_RIGHT){
                UserMaintDataTxnBean userMaintDataTxnBean = new UserMaintDataTxnBean();
                boolean hasRight = userMaintDataTxnBean.getUserHasOSPRight(loggedinUser, ADD_PERSON_RIGHT);
                responder.setDataObject(new Boolean(hasRight));
                responder.setResponseStatus( true );
                responder.setMessage(null);
            }
            //Added for Coeus 4.3 PT ID - 2388:Person Enhancements - start
            //To check whether the user has the right to modify person details
            else if(functionType == USER_HAS_MODIFY_PERSON_RIGHT){
                UserMaintDataTxnBean userMaintDataTxnBean = new UserMaintDataTxnBean();
                //Modified for case 3761 - Modify Person right issue - start
                //boolean hasRight = userMaintDataTxnBean.getUserHasOSPRight(loggedinUser, MODIFY_PERSON_RIGHT);
                boolean hasRight = userMaintDataTxnBean.getUserHasRightInAnyUnit(loggedinUser, MODIFY_PERSON_RIGHT);
                //Modified for case 3761 - Modify Person right issue - end
                responder.setDataObject(new Boolean(hasRight));
                responder.setResponseStatus( true );
                responder.setMessage(null);
            }//Added for Coeus 4.3 PT ID - 2388:Person Enhancements - end
            else if(functionType == GET_CURRENT_AND_PENDING_REPORT_DATA){
                //COEUSQA-1686 - Add additional fields to the Current  Pending Support Schema - Start
                //personId = (String)requester.getDataObject();
                CoeusVector personData = (CoeusVector)requester.getDataObjects();
                personId = (String)personData.get(0);
                investigatorAndKeyPerson = (Boolean)personData.get(1);
                //Hashtable htReportData = departmentPersonTxnBean.getCurrentAndPendingReport(personId);
                Hashtable htReportData = departmentPersonTxnBean.getCurrentAndPendingReport(personId, investigatorAndKeyPerson);
                //COEUSQA-1686 - Add additional fields to the Current  Pending Support Schema - End
                responder.setDataObject(htReportData);
                responder.setResponseStatus( true );
                responder.setMessage(null);
                // Added by chandra to implement current & Pending Support Reports
//            }else if(functionType == PRINT_CURRENT || functionType==PRINT_PENDING){
//                InstPropStream instPropStream = new InstPropStream();
//                personName = (String)requester.getId();
//                CoeusVector reportData = (CoeusVector)requester.getDataObjects();
//                char reportType = requester.getFunctionType();
//                String path = "";
//                String fileName = "";
//                ByteArrayOutputStream byteArrayOutputStream = null;
//                if(reportType==PRINT_CURRENT) {
//                    byteArrayOutputStream = instPropStream.getStreamData(reportData,personName,PRINT_CURRENT);
//                    path = "/edu/mit/coeus/utils/xml/data/CurrentSupport.xsl";
//                    fileName = "CurrentReport";
//                }else if(reportType==PRINT_PENDING) {
//                    byteArrayOutputStream = instPropStream.getStreamData(reportData,personName,PRINT_PENDING);
//                    path = "/edu/mit/coeus/utils/xml/data/PendingSupport.xsl";
//                    fileName = "PendingReport";
//                }
//                String pdfUrl = "";
//                pdfUrl = generateCurrentPendingReport(byteArrayOutputStream,path,fileName);
//                responder.setDataObject(pdfUrl);
//                responder.setResponseStatus(true);
//                responder.setMessage(null);
            }
            //For Case #1602 Start 2
            else if(functionType == STATE_COUNTRY_DATA){
                SponsorMaintenanceDataTxnBean sponsorTxnBean = new SponsorMaintenanceDataTxnBean();
                Vector vecStates = sponsorTxnBean.getStates();
                Vector vecCountries = sponsorTxnBean.getCountries();
                CoeusVector cvData = new CoeusVector();
                cvData.add(0,(vecStates == null? new Vector(): vecStates));
                cvData.add(1,(vecCountries == null? new Vector(): vecCountries));
                responder.setDataObject(cvData);
                responder.setResponseStatus(true);
            }
            //For Case #1602 End 2
            // 4467: If you put a country other than US, the state drop down should display the 'states' for the country - Start
            else if(functionType == GET_STATE_COUNRTY_DATA_FOR_ALL){
                SponsorMaintenanceDataTxnBean sponsorTxnBean = new SponsorMaintenanceDataTxnBean();
                HashMap hmStatesWithCountry = sponsorTxnBean.fetchStatesWithCountry();
                Vector vecCountries = sponsorTxnBean.getCountries();
                CoeusVector cvData = new CoeusVector();
                cvData.add(0,(hmStatesWithCountry == null? new HashMap(): hmStatesWithCountry));
                cvData.add(1,(vecCountries == null? new Vector(): vecCountries));
                responder.setDataObject(cvData);
                responder.setResponseStatus(true);
            }
            // 4467: If you put a country other than US, the state drop down should display the 'states' for the country - End
            // 3282: Reviewer view of Protocols - Start
            else if(functionType == GET_USER_INFO){
                responder.setDataObject(userBean);
                responder.setResponseStatus(true);
            }
           // 3282: Reviewer view of Protocols - End
            //Added for COEUSQA-2291 : Hide Reviewer Name in Review Comments - Start
            else if(functionType == GET_PERSON_ID_FOR_USER){
                UserDetailsBean userDetailsBean = new UserDetailsBean();
                personId =userDetailsBean.getPersonID(userBean.getUserId());
                responder.setDataObject(personId);
                responder.setResponseStatus(true);
            }
            //COEUSQA-2291 : End
        }catch( CoeusException coeusEx ) {
//            int index=0;
            String errMsg;
            if(coeusEx.getErrorId()==999999){
                errMsg = "dbEngine_intlErr_exceptionCode.1028";
                responder.setLocked(true);
            }else{
                errMsg = coeusEx.getMessage();
            }
            CoeusMessageResourcesBean coeusMessageResourcesBean
                    =new CoeusMessageResourcesBean();
            errMsg= coeusMessageResourcesBean.parseMessageKey(errMsg);
            responder.setResponseStatus(false);
            //print the error message at client side
            responder.setException(coeusEx);
            responder.setMessage(errMsg);
            UtilFactory.log( errMsg, coeusEx, "PersonMaintenanceServlet",
                    "perform");
            
        }catch( DBException dbEx ) {
            
//            int index=0;
            String errMsg = dbEx.getUserMessage();
            if (dbEx.getErrorId() == 20102 ) {
                errMsg = "dbEngine_intlErr_exceptionCode.1028";
            }
            if (errMsg.equals("db_exceptionCode.1111")) {
                responder.setCloseRequired(true);
            }
            CoeusMessageResourcesBean coeusMessageResourcesBean
                    = new CoeusMessageResourcesBean();
            errMsg= coeusMessageResourcesBean.parseMessageKey(errMsg);
            
            responder.setResponseStatus(false);
            
            //print the error message at client side
            responder.setException(dbEx);
            responder.setMessage(errMsg);
            UtilFactory.log( errMsg, dbEx,
                    "PersonMaintenanceServlet", "perform");
            
        }catch(Exception e) {
            //e.printStackTrace();
            //print the error message at server side
            responder.setResponseStatus(false);
            //print the error message at client side
            responder.setException(e);
            responder.setMessage(e.getMessage());
            UtilFactory.log( e.getMessage(), e,
                    "PersonMaintenanceServlet", "perform");
        //Case 3193 - START
        }catch(Throwable throwable){
            Exception ex = new Exception(throwable);
            responder.setException(ex);
            responder.setResponseStatus(false);
            responder.setMessage(ex.getMessage());
            UtilFactory.log( throwable.getMessage(), throwable, "PersonMaintenanceServlet", "doPost");
        //Case 3193 - END
        } finally {
            try{
                // send the object to applet
                outputToApplet
                        = new ObjectOutputStream(response.getOutputStream());
                outputToApplet.writeObject(responder);
                // close the streams
                if (inputFromApplet!=null){
                    inputFromApplet.close();
                }
                if (outputToApplet!=null){
                    outputToApplet.flush();
                    outputToApplet.close();
                }
            }catch (IOException ioe){
                UtilFactory.log( ioe.getMessage(), ioe,
                        "PersonMaintenanceServlet", "perform");
            }
        }
    }
    public String generateCurrentPendingReport(ByteArrayOutputStream byteArrayOutputStream,
            String path,String fileName) throws Exception {
        String pdfUrl = "";
        CoeusConstants.SERVER_HOME_PATH = this.getServletContext().getRealPath("/");
        ReportGenerator reportGenerator = new ReportGenerator();
        ByteArrayInputStream xmlStream;
        InputStream xslStream;
        String report = new String(byteArrayOutputStream.toByteArray());
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(report.getBytes());
        xmlStream = byteArrayInputStream;
        xmlStream.close();
        xslStream = getClass().getResourceAsStream(path);
        byteArrayOutputStream = reportGenerator.convertXML2PDF(xmlStream, xslStream);
        byteArrayOutputStream.close();
        //get path (to generate PDF) from config
        String reportPath = CoeusProperties.getProperty(CoeusPropertyKeys.REPORT_GENERATED_PATH);
        String filePath = CoeusConstants.SERVER_HOME_PATH+File.separator+reportPath;
        File reportDir = new File(filePath);
        if(!reportDir.exists()){
            reportDir.mkdirs();
        }
        SimpleDateFormat dateFormat= new SimpleDateFormat("MMddyyyy-hhmmss");
        File file =null;
        File xmlFile = null;
        file = new File(filePath, fileName+dateFormat.format(new java.util.Date())+".pdf");
        xmlFile = new File(filePath, fileName+dateFormat.format(new java.util.Date())+".xml");
        FileOutputStream fos = new FileOutputStream(file);
        fos.write(byteArrayOutputStream.toByteArray());
        fos.close();
        byteArrayOutputStream.close();
        String debugMode = CoeusProperties.getProperty(CoeusPropertyKeys.GENERATE_XML_FOR_DEBUGGING) ;
        if (debugMode != null) {
            if (debugMode.equalsIgnoreCase("Y")
            || debugMode.equalsIgnoreCase("Yes")) {
                FileOutputStream fosXml = null;
                try{
                    fosXml = new FileOutputStream(xmlFile);
                    fosXml.write(report.getBytes());
                }catch(Exception ex){
                    fosXml.flush();
                    fosXml.close();
                    UtilFactory.log("Not able to write xml file");
                }finally{
                    if(fosXml!=null){
                        fosXml.flush();
                        fosXml.close();
                    }
                }
            }
        }
        pdfUrl = "/"+reportPath+"/"+file.getName();
        return pdfUrl;
    }
}