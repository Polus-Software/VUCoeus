/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.mit.coeus.servlet;

import edu.mit.coeus.award.bean.AwardTxnBean;
import edu.mit.coeus.bean.CoeusMessageResourcesBean;
import edu.mit.coeus.bean.UserDetailsBean;
import edu.mit.coeus.bean.UserInfoBean;
import edu.mit.coeus.brokers.RequesterBean;
import edu.mit.coeus.brokers.ResponderBean;
import edu.mit.coeus.exception.CoeusException;
import edu.mit.coeus.routing.bean.RoutingDetailsBean;
import edu.mit.coeus.routing.bean.RoutingMapBean;
import edu.mit.coeus.routing.bean.RoutingUpdateTxnBean;
import edu.mit.coeus.user.bean.UserMaintDataTxnBean;
import edu.mit.coeus.utils.ApplicationContext;
import edu.mit.coeus.utils.CoeusVector;
import edu.mit.coeus.utils.TypeConstants;
import edu.mit.coeus.utils.UtilFactory;
import edu.mit.coeus.utils.dbengine.DBEngineImpl;
import edu.mit.coeus.utils.dbengine.DBException;
import edu.mit.coeus.utils.document.CoeusDocument;
import edu.mit.coeus.utils.document.DocumentBean;
import edu.mit.coeus.utils.document.DocumentConstants;
import edu.mit.coeus.utils.document.DocumentIdGenerator;
import edu.mit.coeus.utils.locking.LockingBean;
import edu.mit.coeus.utils.locking.LockingException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashMap;
import java.util.Vector;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author midhunmk
 */
public class AwardSubmissionServlet extends CoeusBaseServlet implements TypeConstants{
    
    private static final char SUBMIT_FOR_APPROVE='A';
    private static final char GET_APPROVAL_MAPS = 'C';
    private static final char UPDATE_APPROVAL_MAP='B';
    private static final char UPDATE_AWARD_STATUS='D';
    
    private static final char GET_AWARD_DOCUMENT='E';
    private static final char CHECK_USER_HAS_DOC_ROUTE_RIGHT='F';
    private static final char REMOVE_AWARD_DOCUMENT='G';
    private static final char GET_AWD_DOC_DETAILS='H';
    private static final String ROUTE_AWARD_DOCUMENTS="ROUTE_AWARD_DOCUMENTS";
            
    private String mitAwardNumber;
    private Integer sequenceNumber;
    private Integer docuNumber;
    private AwardTxnBean awardTxnBean;
    private DBEngineImpl dbEngine;
    
    
    @Override
    public void doPost(HttpServletRequest request,
    HttpServletResponse response)
    throws ServletException, IOException {
        
        RequesterBean requester = null;

        ResponderBean responder = new ResponderBean();
        
        ObjectInputStream inputFromApplet = null;
        ObjectOutputStream outputToApplet = null;
        
        String loggedinUser ="";
                
        try {
            // get an input stream
            inputFromApplet = new ObjectInputStream(request.getInputStream());
            // read the serialized request object from applet
            requester = (RequesterBean) inputFromApplet.readObject();
            isValidRequest(requester);
            loggedinUser = requester.getUserName();
            
            // get the user
            UserInfoBean userBean = (UserInfoBean)new
            UserDetailsBean().getUserInfo(requester.getUserName());
         
            // keep all the beans into vector
            Vector dataObjects = new Vector();
            
            char functionType = requester.getFunctionType();
            awardTxnBean=new AwardTxnBean();
            
            if(functionType==SUBMIT_FOR_APPROVE){
                dataObjects = (Vector)requester.getDataObjects();
                mitAwardNumber = (String)dataObjects.elementAt(0);
                sequenceNumber = (Integer)dataObjects.elementAt(1);
                String awardLeadUnit = (String)dataObjects.elementAt(2);
                String option = (String)dataObjects.elementAt(3);
                
                dataObjects = awardTxnBean.submitToApprove(
                        mitAwardNumber, sequenceNumber.intValue(), awardLeadUnit, option,loggedinUser);
                responder.setDataObjects(dataObjects);
                responder.setResponseStatus(true);
                responder.setMessage(null);
                
            }
            else if(functionType==UPDATE_AWARD_STATUS)
            {
                dataObjects = (Vector)requester.getDataObject();
                mitAwardNumber = (String)dataObjects.elementAt(0);
                docuNumber = (Integer)dataObjects.elementAt(1);
                Integer statusCode=(Integer)dataObjects.elementAt(2);
                responder.setDataObject(awardTxnBean.ChangeAwardStatus(mitAwardNumber, docuNumber.intValue(), statusCode.intValue()));
                responder.setResponseStatus(true);
                responder.setMessage(null);
            }
            else if(functionType == GET_APPROVAL_MAPS){
                
                dataObjects = (Vector) requester.getDataObjects();
                String awardUnitNumber = (String) dataObjects.elementAt(0);
                mitAwardNumber = (String) dataObjects.elementAt(1);
                //String awardUnitNumber = awardTxnBean.getLeadUnitForAward(mitAwardNumber);
                //int mapId = ((Integer) dataObjects.elementAt(2)).intValue();
                int roleId = ((Integer)dataObjects.elementAt(2)).intValue();

                dataObjects = awardTxnBean.getUnitMap(awardUnitNumber);
                Vector vctDataObjects = new Vector(3,2);
                //Add Unit Map details
                vctDataObjects.addElement(dataObjects);
                vctDataObjects.addElement(null);
                vctDataObjects.addElement(null);
                responder.setDataObjects(vctDataObjects);
                responder.setResponseStatus(true);
                responder.setMessage(null);
            }//Modified for bug id 1856 step 2: start
            else if (functionType==UPDATE_APPROVAL_MAP){
                dataObjects = (Vector) requester.getDataObjects();
                CoeusVector cvApprovalMaps=(CoeusVector)dataObjects.get(0);
                RoutingMapBean routingMapBean = null;
                Vector procedures = new Vector(5,3);
                dbEngine = new DBEngineImpl();
        RoutingUpdateTxnBean routingUpdateTxnBean = new RoutingUpdateTxnBean(userBean.getUserId());
        if(cvApprovalMaps!=null){
            int length = cvApprovalMaps.size();
            for(int index=0;index<length;index++){
                routingMapBean =(RoutingMapBean)cvApprovalMaps.elementAt(index);
                if (routingMapBean!=null && routingMapBean.getAcType() != null ) {
                    if(!routingMapBean.getAcType().equals("D")){
                        procedures.add( routingUpdateTxnBean.addUpdDelRoutingMaps(routingMapBean));
                    }

                    //Update Approvals for this Map
                    CoeusVector vctProposalApprovals = routingMapBean.getRoutingMapDetails();
                    RoutingDetailsBean routingDetailsBean = null;

                    if(vctProposalApprovals!=null){
                        for(int approvalRow = 0; approvalRow < vctProposalApprovals.size(); approvalRow++){
                            routingDetailsBean = (RoutingDetailsBean)vctProposalApprovals.elementAt(approvalRow);
                            if(routingDetailsBean!=null && routingDetailsBean.getAcType()!=null){
                                procedures.add(routingUpdateTxnBean.addUpdDelRoutingDetails(routingDetailsBean));
                            }
                        }
                    }
                    if(routingMapBean.getAcType().equals("D")){
                        procedures.add(routingUpdateTxnBean.addUpdDelRoutingMaps(routingMapBean));
                    }
                }
            }
        }
        if(dbEngine!=null){

            try{
                dbEngine.executeStoreProcs(procedures);
                responder.setDataObject(new Integer(1));
                responder.setResponseStatus(true);
                responder.setMessage(null);
            }catch (DBException dbEx){
                throw new CoeusException(dbEx.getMessage());
            }
            

        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }
        
        
            }
            else if(functionType==GET_AWARD_DOCUMENT){
            //fetch the award document in the table to server location
              
                
                dataObjects = (Vector) requester.getDataObject();
                mitAwardNumber=(String)dataObjects.get(0);
                String documentNumber =(String)dataObjects.get(1);
                CoeusDocument coeusDocument=new CoeusDocument();
                coeusDocument.setDocumentData(awardTxnBean.getAwardDocument(mitAwardNumber,Integer.valueOf(documentNumber)));
                coeusDocument.setMimeType(DocumentConstants.MIME_PDF);
                coeusDocument.setDocumentName("Routed Document");
                StringBuffer strBuff = request.getRequestURL();
                StringBuffer stringBuffer = new StringBuffer();
                DocumentBean documentBean = new DocumentBean();
                HashMap retMap = new HashMap();
                DocumentIdGenerator documentIdGenerator = new DocumentIdGenerator();
                String documentId = documentIdGenerator.generateDocumentId();
                    String strPath = new String(strBuff);
                    strPath = strPath.substring(0,strPath.lastIndexOf('/'));
                    stringBuffer.append(strPath);
                    stringBuffer.append("/StreamingServlet");
                    stringBuffer.append("?");
                    stringBuffer.append(DocumentConstants.DOC_ID);
                    stringBuffer.append("=");
                    stringBuffer.append(documentId);
                    retMap.put(DocumentConstants.DOCUMENT_URL, stringBuffer.toString());
                    retMap.put(DocumentConstants.COEUS_DOCUMENT, coeusDocument);
                    documentBean.setParameterMap(retMap);
                    ApplicationContext.setAttribute(documentId, documentBean, loggedinUser);
                responder.setDataObject(stringBuffer.toString());
                responder.setResponseStatus(true);
            }
            else if(functionType==CHECK_USER_HAS_DOC_ROUTE_RIGHT){
                mitAwardNumber=(String)requester.getDataObject();
                String leadUnitNumber=awardTxnBean.getLeadUnitForAward(mitAwardNumber);
                UserMaintDataTxnBean userMaintDataTxnBean=new UserMaintDataTxnBean();
                boolean hasRight=userMaintDataTxnBean.getUserHasRight(loggedinUser, ROUTE_AWARD_DOCUMENTS, leadUnitNumber);
                responder.setDataObject(hasRight);
                responder.setResponseStatus(true);
            }
            else if(functionType==REMOVE_AWARD_DOCUMENT){
                if(requester.getDataObject()!=null){
                   edu.mit.coeus.award.bean.AwardDocumentRouteBean awardDocumentRouteBean=
                    (edu.mit.coeus.award.bean.AwardDocumentRouteBean)requester.getDataObject();
                responder.setResponseStatus(awardTxnBean.removeAwardDocumentRouteBean(awardDocumentRouteBean));
                responder.setResponseStatus(true);
                }
                    
            }
            else if(functionType==GET_AWD_DOC_DETAILS){
                if(requester.getDataObject()!=null){
                    edu.mit.coeus.award.bean.AwardDocumentRouteBean awardDocumentRouteBean=
                    (edu.mit.coeus.award.bean.AwardDocumentRouteBean)requester.getDataObject();
                  int approvalSeq=awardDocumentRouteBean.getRoutingApprovalSeq();
                  responder.setDataObject(awardTxnBean.getRoutedDocumentOfAprvlSeq(awardDocumentRouteBean.getMitAwardNumber(), approvalSeq));
                  responder.setResponseStatus(true);
                }
            }
            
            
        }
        catch( LockingException lockEx ) {
            //lockEx.printStackTrace();
            LockingBean lockingBean = lockEx.getLockingBean();
            String errMsg = lockEx.getErrorMessage();
            CoeusMessageResourcesBean coeusMessageResourcesBean
            =new CoeusMessageResourcesBean();
            errMsg= coeusMessageResourcesBean.parseMessageKey(errMsg);
            responder.setException(lockEx);
            responder.setResponseStatus(false);
            responder.setMessage(errMsg);
            UtilFactory.log( errMsg, lockEx,
            "AwardMaintenanceServlet", "doPost");
        }
        catch( CoeusException coeusEx ) {
            //coeusEx.printStackTrace();
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
            
            responder.setResponseStatus(false);
            responder.setException(coeusEx);
            responder.setMessage(errMsg);
            UtilFactory.log( errMsg, coeusEx,
            "AwardMaintenanceServlet", "doPost");
            
        }catch( DBException dbEx ) {
            //dbEx.printStackTrace();
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
            //   responder.setException(dbEx);
            responder.setException(new CoeusException(errMsg));
            responder.setMessage(errMsg);
            UtilFactory.log( errMsg, dbEx,
            "AwardMaintenanceServlet", "doPost");
            
        }catch(Exception e) {
            //e.printStackTrace();
            responder.setResponseStatus(false);
            responder.setException(e);
            responder.setMessage(e.getMessage());
            UtilFactory.log( e.getMessage(), e,
            "AwardMaintenanceServlet", "doPost");
        //Case 3193 - START
        }catch(Throwable throwable){
            Exception ex = new Exception(throwable);
            responder.setException(ex);
            responder.setResponseStatus(false);
            responder.setMessage(ex.getMessage());
            UtilFactory.log( throwable.getMessage(), throwable, "AwardMaintenanceServlet", "doPost");
        //Case 3193 - END
        } finally {
            try{
                
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
                "AwardMaintenanceServlet", "doPost");
            }
        }

        
        
}
}
