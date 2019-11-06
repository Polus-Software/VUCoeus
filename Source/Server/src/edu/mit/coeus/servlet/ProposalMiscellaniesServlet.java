
/*
 * ProposalMaintenanceServlet.java
 *
 * Created on March 13, 2003, 12:27 PM
 */

package edu.mit.coeus.servlet;

import javax.servlet.*;
import javax.servlet.http.*;

import edu.mit.coeus.brokers.RequesterBean;
import edu.mit.coeus.brokers.ResponderBean;
import edu.mit.coeus.bean.*;

import edu.mit.coeus.propdev.bean.*;
import edu.mit.coeus.irb.bean.PersonInfoTxnBean;
import edu.mit.coeus.utils.locking.LockingException;
import edu.mit.coeus.utils.locking.LockingBean;

import edu.mit.coeus.utils.UtilFactory;
import edu.mit.coeus.utils.dbengine.DBException;
import edu.mit.coeus.exception.CoeusException;
import edu.mit.coeus.organization.bean.*;
import edu.mit.coeus.rolodexmaint.bean.*;
import edu.mit.coeus.utils.*;
import edu.mit.coeus.utils.CoeusConstants;
import edu.mit.coeus.departmental.bean.DepartmentPersonTxnBean;
import edu.mit.coeus.sponsormaint.bean.SponsorMaintenanceDataTxnBean;
import edu.mit.coeus.customelements.bean.CustomElementsInfoBean;
import edu.mit.coeus.irb.bean.ProtocolDataTxnBean;
import edu.mit.coeus.user.bean.UserMaintDataTxnBean;
import java.io.*;
import java.text.DecimalFormat;
import java.util.*;
import java.text.SimpleDateFormat;
/**
 * This servlet is used to handle the proposal requests.
 * @author  subramanya
 * @version
 */
public class ProposalMiscellaniesServlet extends CoeusBaseServlet implements TypeConstants {

    //holds the until Facotry instance
//    private UtilFactory UtilFactory = new UtilFactory();
//    String loggedinUser;
    
    //Function Types
    private static final char GET_PROPOSAL_PERSON_BIO_PDF = 'A';
    private static final char GET_PROPOSAL_PERSON_BIO_SOURCE = 'B';
    private static final char UPDATE_PROPOSAL_PERSON_BIO_SOURCE = 'C';
    private static final char UPDATE_PROPOSAL_PERSON_BIO_PDF = 'D';
    private static final char COPY_PERSON_TO_PROPOSAL_PERSON = 'E';
    private static final char UPDATE_NARRATIVE_STATUS = 'F';
    //Rights
    
    //Role
    
    /** Initializes the servlet.
     */
    public void init(ServletConfig config) throws ServletException {
        super.init(config);

    }

    /** Destroys the servlet.
     */
    public void destroy() {

    }

    /** Handles the HTTP <code>POST</code> method.
     * @param request servlet request
     * @param response servlet response
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, java.io.IOException {
        // the request object from applet
        RequesterBean requester = null;
        // the response object to applet
        ResponderBean responder = new ResponderBean();

        // open object input/output streams
        ObjectInputStream inputFromApplet = null;
        ObjectOutputStream outputToApplet = null;
        UserInfoBean userBean;
        ProposalDevelopmentTxnBean proposalDataTxnBean;
        ProposalDevelopmentUpdateTxnBean proposalUpdTxnBean;
        ProposalDevelopmentFormBean proposalDevelopmentFormBean=null;

        String proposalNumber;
        char functionType ;

        try {
            // get an input stream
            inputFromApplet = new ObjectInputStream( request.getInputStream() );
            
            // read the serialized request object from applet
            requester = (RequesterBean) inputFromApplet.readObject();
            isValidRequest(requester);
            // get the user
            String loggedinUser = requester.getUserName();
            
            proposalDataTxnBean = new ProposalDevelopmentTxnBean();
            
            // keep all the beans into vector
            Vector dataObjects = new Vector();
            functionType = requester.getFunctionType();
            proposalNumber = ( requester.getId() == null ? "" : (String)requester.getId());
            
            if(functionType == GET_PROPOSAL_PERSON_BIO_PDF){
                ProposalPersonBioPDFBean proposalPersonBioPDFBean = (ProposalPersonBioPDFBean)requester.getDataObject();
                
                ProposalPersonTxnBean proposalPersonTxnBean = new ProposalPersonTxnBean();                
                proposalPersonBioPDFBean = proposalPersonTxnBean.getProposalPersonBioPDF(proposalPersonBioPDFBean);
                String filePath = "";
                if(proposalPersonBioPDFBean!=null && proposalPersonBioPDFBean.getFileBytes() != null){
                    byte[] fileData = proposalPersonBioPDFBean.getFileBytes();
                    //Create the file in Server
                    CoeusConstants.SERVER_HOME_PATH = this.getServletContext().getRealPath("/");
//                    InputStream is = getClass().getResourceAsStream("/coeus.properties");
//                    Properties coeusProps = new Properties();
//                    coeusProps.load(is);
                    String reportPath = CoeusProperties.getProperty(CoeusPropertyKeys.REPORT_GENERATED_PATH); //get path (to generate PDF) from config

                    filePath = CoeusConstants.SERVER_HOME_PATH+File.separator+reportPath;
                    File reportDir = new File(filePath);
                    if(!reportDir.exists()){
                        reportDir.mkdirs();
                    }
                    SimpleDateFormat dateFormat= new SimpleDateFormat("MMddyyyy-hhmmss");
                    File reportFile = new File(reportDir + File.separator + "ProposalPersonPDF"+dateFormat.format(new Date())+".pdf");
                    
                    reportFile.deleteOnExit();
                    
                    FileOutputStream fos = new FileOutputStream(reportFile);
                    fos.write( fileData,0,fileData.length );
                    fos.close();
                    filePath = "/"+reportPath+"/"+reportFile.getName();
                }else{
                    filePath = null;
                }                
                
                responder.setDataObject(filePath);
                responder.setResponseStatus(true);
                responder.setMessage(null);
            }else if(functionType == GET_PROPOSAL_PERSON_BIO_SOURCE){
                ProposalPersonBioSourceBean proposalPersonBioSourceBean = (ProposalPersonBioSourceBean)requester.getDataObject();
                
                ProposalPersonTxnBean proposalPersonTxnBean = new ProposalPersonTxnBean();                
                proposalPersonBioSourceBean = proposalPersonTxnBean.getProposalPersonBioSource(proposalPersonBioSourceBean);
                String filePath = "";
                if(proposalPersonBioSourceBean!=null && proposalPersonBioSourceBean.getFileBytes() != null){
                    byte[] fileData = proposalPersonBioSourceBean.getFileBytes();
                    //Create the file in Server
                    CoeusConstants.SERVER_HOME_PATH = this.getServletContext().getRealPath("/");
                    InputStream is = getClass().getResourceAsStream("/coeus.properties");
                    Properties coeusProps = new Properties();
                    coeusProps.load(is);
                    String reportPath = coeusProps.getProperty("REPORT_GENERATED_PATH"); //get path (to generate PDF) from config

                    filePath = CoeusConstants.SERVER_HOME_PATH+File.separator+reportPath;
                    File reportDir = new File(filePath);
                    if(!reportDir.exists()){
                        reportDir.mkdirs();
                    }
                    SimpleDateFormat dateFormat= new SimpleDateFormat("MMddyyyy-hhmmss");
                    File reportFile = new File(reportDir + File.separator + "ProposalPersonPDF"+dateFormat.format(new Date())+".doc");
                    
                    reportFile.deleteOnExit();
                    
                    FileOutputStream fos = new FileOutputStream(reportFile);
                    fos.write( fileData,0,fileData.length );
                    fos.close();
                    filePath = "/"+reportPath+"/"+reportFile.getName();
                }else{
                    filePath = null;
                }
                                
                responder.setDataObject(filePath);
                responder.setResponseStatus(true);
                responder.setMessage(null);
            }else if(functionType == UPDATE_PROPOSAL_PERSON_BIO_SOURCE){
                ProposalPersonBioSourceBean proposalPersonBioSourceBean = (ProposalPersonBioSourceBean)requester.getDataObject();
                ProposalPersonTxnBean proposalPersonTxnBean = new ProposalPersonTxnBean(loggedinUser);
                boolean isUpdate = proposalPersonTxnBean.addUpdatePersonBioSource(proposalPersonBioSourceBean);
                responder.setDataObject(new Boolean(isUpdate));
                responder.setResponseStatus(true);
                responder.setMessage(null);
            }else if(functionType == UPDATE_PROPOSAL_PERSON_BIO_PDF){
                ProposalPersonBioPDFBean proposalPersonBioPDFBean = (ProposalPersonBioPDFBean)requester.getDataObject();
                ProposalPersonTxnBean proposalPersonTxnBean = new ProposalPersonTxnBean(loggedinUser);
                boolean isUpdate = proposalPersonTxnBean.addUpdatePersonBioPDF(proposalPersonBioPDFBean);
                responder.setDataObject(new Boolean(isUpdate));
                responder.setResponseStatus(true);
                responder.setMessage(null);                
            }else if(functionType == COPY_PERSON_TO_PROPOSAL_PERSON){
                dataObjects = requester.getDataObjects();
                proposalNumber = (String)dataObjects.elementAt(0);
                String personId = (String)dataObjects.elementAt(1);
                ProposalPersonTxnBean proposalPersonTxnBean = new ProposalPersonTxnBean(loggedinUser);
                //Modified with coeusdev-139 : Allow multiple person attachments of same document type in Person Bio Module
//                boolean isUpdate = proposalPersonTxnBean.copyPersonBioToProposalPersonBio(proposalNumber, personId);
//                dataObjects = new Vector();
//                dataObjects = proposalPersonTxnBean.getProposalBioGraphy(proposalNumber, personId);
                dataObjects = proposalPersonTxnBean.getPersonBiographyToSync(personId);
                //COEUSDEV - 139 End
                responder.setDataObjects(dataObjects);
                responder.setResponseStatus(true);
                responder.setMessage(null);
            }else if(functionType == UPDATE_NARRATIVE_STATUS){
                dataObjects = requester.getDataObjects();
                //Proposal Number 
                proposalNumber = (String)dataObjects.elementAt(0);
                //Proposal Narrative Status
                Character narrativeStatus = (Character)dataObjects.elementAt(1);
                //Retain or Release Lock
                Boolean isReleaseLock = (Boolean)dataObjects.elementAt(2);
                ProposalNarrativeTxnBean proposalNarrativeTxnBean = new ProposalNarrativeTxnBean(loggedinUser);
                boolean succes = proposalNarrativeTxnBean.updateProposalNarrativeStatus(proposalNumber, narrativeStatus.charValue());
                if(isReleaseLock != null && isReleaseLock.booleanValue() == true){
                    // Commented by Shivakumar for locking enhancement - BEGIN
//                    proposalNarrativeTxnBean.releaseEdit(proposalNumber);
                    // Commented by Shivakumar for locking enhancement - END
                    // Code added by Shivakumar for locking enhancement - BEGIN
//                    proposalNarrativeTxnBean.releaseEdit(proposalNumber,loggedinUser);
                    // Calling releaseLock method for fixing bug in locking
                    LockingBean lockingBean = proposalNarrativeTxnBean.releaseLock(proposalNumber,loggedinUser);
                    responder.setLockingBean(lockingBean);
                    // Code added by Shivakumar for locking enhancement - END
                }
                responder.setResponseStatus(true);
                responder.setMessage(null);                
            }
        }catch( LockingException lockEx ) {
               //lockEx.printStackTrace();
               LockingBean lockingBean = lockEx.getLockingBean();
               String errMsg = lockEx.getErrorMessage();        
               CoeusMessageResourcesBean coeusMessageResourcesBean
                    =new CoeusMessageResourcesBean();
                errMsg= coeusMessageResourcesBean.parseMessageKey(errMsg);            
                responder.setException(lockEx);
                responder.setResponseStatus(false);            
                responder.setMessage(errMsg);
                UtilFactory.log( errMsg, lockEx, "ProposalMiscellaniesServlet", "perform");
        }catch( CoeusException coeusEx ) {
            int index=0;
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
            responder.setException(coeusEx);
            responder.setResponseStatus(false);
            responder.setMessage(errMsg);
            UtilFactory.log( errMsg, coeusEx, "ProposalMiscellaniesServlet", "perform");

        }catch( DBException dbEx ) {

            int index=0;
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
                "ProposalMiscellaniesServlet", "perform");

        }catch(Exception e) {
            //e.printStackTrace();
            responder.setResponseStatus(false);
            responder.setException(e);
            responder.setMessage(e.getMessage());
            UtilFactory.log( e.getMessage(), e,
                "ProposalMiscellaniesServlet", "perform");
        //Case 3193 - START
        }catch(Throwable throwable){
            Exception ex = new Exception(throwable);
            responder.setException(ex);
            responder.setResponseStatus(false);
            responder.setMessage(ex.getMessage());
            UtilFactory.log( throwable.getMessage(), throwable, "ProposalMiscellaniesServlet", "doPost");
        //Case 3193 - END
        }finally {
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
                "ProposalMiscellaniesServlet", "perform");
            }
        }
    }

    /** Handles the HTTP <code>GET</code> method.
     * @param request servlet request
     * @param response servlet response
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, java.io.IOException {

    } 
     
}