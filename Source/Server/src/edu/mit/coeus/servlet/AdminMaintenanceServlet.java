/*
 * AdminMaintenanceServlet.java
 *
 * Created on November 22, 2004, 12:01 PM
 */

package edu.mit.coeus.servlet;



import edu.mit.coeus.brokers.RequesterBean;
import edu.mit.coeus.brokers.ResponderBean;
import edu.mit.coeus.bean.*;
import edu.mit.coeus.utils.UtilFactory;
import edu.mit.coeus.utils.TypeConstants;
import edu.mit.coeus.utils.dbengine.DBException;
import edu.mit.coeus.exception.CoeusException;
import edu.mit.coeus.moduleparameters.bean.ParameterBean;
import edu.mit.coeus.utils.CoeusConstants;
import edu.mit.coeus.utils.KeyConstants;
import edu.mit.coeus.utils.CoeusVector;
import edu.mit.coeus.award.bean.*;
import edu.mit.coeus.utils.CoeusFunctions;
import edu.mit.coeus.utils.query.Equals;
import edu.mit.coeus.utils.query.And;
import edu.mit.coeus.utils.DateUtils;
import edu.mit.coeus.utils.ObjectCloner;
import edu.mit.coeus.utils.query.NotEquals;
import edu.mit.coeus.user.bean.UserMaintDataTxnBean;
import edu.mit.coeus.utils.locking.LockingBean;
import edu.mit.coeus.utils.locking.LockingException;
import edu.mit.coeus.admin.bean.*;
import edu.mit.coeus.award.bean.ValidBasisMethodPaymentBean;
import edu.mit.coeus.utils.CoeusVector;

import java.io.*;
import java.net.*;
import java.util.Hashtable;
import java.util.HashMap;
import java.util.Vector;
import javax.servlet.*;
import javax.servlet.http.*;

/**
 *
 * @author  shivakumarmj
 * @version
 */
public class AdminMaintenanceServlet extends CoeusBaseServlet implements TypeConstants{
	
	private static final char GET_VALID_AWARD_BASIS = 'A';
	
	private static final char UPDATE_VALID_AWARD_BASIS = 'B';
	private static final char GET_VALID_FREQUENCY_AND_FREQUENCY_BASE = 'C';
	private static final char GET_CLASS_REPORT_FREQUENCY = 'D';
	private static final char GET_METHOD_OF_PAYMENT = 'E';
	private static final char GET_YNQ_DETAILS = 'F';
	private static final char UPDATE_METHOD_OF_PAYMENT = 'G';
	private static final char UPDATE_CLASS_REPORT_FREQUENCY = 'H'; 
        private static final char GET_ALL_QUESTION_DEATAILS = 'I';
        private static final char UPDATE_VALID_FREQUENCY_BASE = 'J';
	private static final char UPDATE_YNQ = 'K';
        private static final char GET_ONLY_EXPLANATION = 'M';
        
	
	/** Handles the HTTP <code>POST</code> method.
	 * @param request servlet request
	 * @param response servlet response
	 */
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
		
		String loggedinUser;
		String unitNumber;
		
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
			
			unitNumber = userBean.getUnitNumber();
			// keep all the beans into vector
			Vector dataObjects = new Vector();
			
			char functionType = requester.getFunctionType();
			
			String awardNumber="";
			int sequenceNumber;
			if(functionType == GET_VALID_AWARD_BASIS){
				Hashtable htValidAwardBasis = new Hashtable();
				UserMaintDataTxnBean userMaintTxnBean = new UserMaintDataTxnBean();
				boolean hasRight = userMaintTxnBean.getUserHasOSPRight(loggedinUser, "MAINTAIN_CODE_TABLES");
				AdminTxnBean adminTxnBean = new AdminTxnBean();
				CoeusVector cvValidAwardBasis = adminTxnBean.getValidAwardBasis();
				CoeusVector cvAwardType = adminTxnBean.getAwardType();
				CoeusVector cvBasisOfPayment = adminTxnBean.getBasisOfPayment();
				htValidAwardBasis.put(new Integer(0), cvValidAwardBasis);
				htValidAwardBasis.put(new Integer(1), cvAwardType);
				htValidAwardBasis.put(new Integer(2), new Boolean(hasRight));
				htValidAwardBasis.put(new Integer(3), cvBasisOfPayment);
				responder.setDataObject(htValidAwardBasis);
				responder.setResponseStatus(true);
				responder.setMessage(null);
			}else if(functionType == UPDATE_VALID_AWARD_BASIS){
				CoeusVector cvAwardBasisPayment = (CoeusVector)requester.getDataObject();
				AdminTxnBean adminTxnBean = new AdminTxnBean(loggedinUser);
				ValidBasisPaymentBean validBasisPaymentBean = null;
				if(cvAwardBasisPayment != null && cvAwardBasisPayment.size() > 0){
					for(int index=0; index < cvAwardBasisPayment.size(); index++){
						validBasisPaymentBean = (ValidBasisPaymentBean)cvAwardBasisPayment.elementAt(index);
						if(validBasisPaymentBean.getAcType()==null){
							continue;
						}
						boolean success = adminTxnBean.updateValidAwardBasisPayment(validBasisPaymentBean);
						
					}
					CoeusVector cvValidBasisPayment = adminTxnBean.getValidAwardBasis();
					responder.setDataObject(cvValidBasisPayment);
					responder.setResponseStatus(true);
					responder.setMessage(null);
				}
				
			}else if(functionType==GET_VALID_FREQUENCY_AND_FREQUENCY_BASE){
				Hashtable data = new Hashtable();
				AdminTxnBean adminTxnBean = new AdminTxnBean();
				AwardLookUpDataTxnBean awardLookUpDataTxnBean = new AwardLookUpDataTxnBean();
				UserMaintDataTxnBean userMaintTxnBean = new UserMaintDataTxnBean();
				boolean hasRight = userMaintTxnBean.getUserHasOSPRight(loggedinUser, "MAINTAIN_CODE_TABLES");
				CoeusVector cvFrequency = awardLookUpDataTxnBean.getFrequency();
				CoeusVector cvFreqBean = adminTxnBean.getFrequencyBase();
				CoeusVector cvValidFrequencyBase  = awardLookUpDataTxnBean.getAllValidFrequencyBase();
				CoeusVector cvAllFrequencyBase = awardLookUpDataTxnBean.getFrequencyBase();
				data.put(new Integer(0), new Boolean(hasRight));
				data.put(new Integer(1), cvFrequency);
				data.put(new Integer(2), cvValidFrequencyBase);
				data.put(new Integer(3), cvAllFrequencyBase);
				data.put(new Integer(4), cvFreqBean);
				responder.setDataObject(data);
				responder.setResponseStatus(true);
				responder.setMessage(null);
			}else if (functionType == GET_CLASS_REPORT_FREQUENCY) {
				HashMap data = new HashMap();
				AwardLookUpDataTxnBean awardLookUpDataTxnBean = new AwardLookUpDataTxnBean();
				AdminTxnBean adminTxnBean = new AdminTxnBean();
				UserMaintDataTxnBean userMaintTxnBean = new UserMaintDataTxnBean();
				
				boolean hasRight = userMaintTxnBean.getUserHasOSPRight(loggedinUser, "MAINTAIN_CODE_TABLES");
				data.put(KeyConstants.VIEW_RIGHTS, new Boolean(hasRight));
				
				CoeusVector cvFrequency = adminTxnBean.getFrequency();
				data.put(FrequencyBean.class, cvFrequency);
				
				CoeusVector cvReportClass = awardLookUpDataTxnBean.getReportClass();
				data.put(KeyConstants.REPORT_CLASS,cvReportClass);
				
				CoeusVector cvValidAwardCRF = adminTxnBean.getValidAwardCRF();
				data.put(ValidReportClassReportFrequencyBean.class,cvValidAwardCRF);
				
				CoeusVector cvReports = awardLookUpDataTxnBean.getReport();
				data.put(ReportBean.class,cvReports);
				
				responder.setDataObject(data);
				responder.setResponseStatus(true);
				responder.setMessage(null);
			} else if(functionType == UPDATE_CLASS_REPORT_FREQUENCY) {
				CoeusVector cvClassReportFrequency = (CoeusVector)requester.getDataObject();
				AdminTxnBean adminTxnBean = new AdminTxnBean(loggedinUser);
				ValidReportClassReportFrequencyBean validReportClassReportFrequencyBean = null;
				if(cvClassReportFrequency != null && cvClassReportFrequency.size() > 0) {
					for(int index=0; index < cvClassReportFrequency.size(); index++) {
						validReportClassReportFrequencyBean = (ValidReportClassReportFrequencyBean)cvClassReportFrequency.elementAt(index);
						if(validReportClassReportFrequencyBean.getAcType()==null){
							continue;
						}
						boolean success = adminTxnBean.updateClassReportFrequency(validReportClassReportFrequencyBean);
					}
				}
				responder.setResponseStatus(true);
				responder.setMessage(null);
			} else if (functionType == GET_METHOD_OF_PAYMENT) {
				HashMap data = new HashMap();
				AwardLookUpDataTxnBean awardLookUpDataTxnBean = new AwardLookUpDataTxnBean();
				AdminTxnBean adminTxnBean = new AdminTxnBean();
				AwardReportTxnBean awardReportTxnBean = new AwardReportTxnBean();
				UserMaintDataTxnBean userMaintTxnBean = new UserMaintDataTxnBean();
				boolean hasRight = userMaintTxnBean.getUserHasOSPRight(loggedinUser, "MAINTAIN_CODE_TABLES");
				data.put(new Integer(0), new Boolean(hasRight));
				CoeusVector cvValidBasisOfPayment = adminTxnBean.getValidBasisOfPayment();
				if(cvValidBasisOfPayment != null && cvValidBasisOfPayment.size() > 0){
					data.put(new Integer(1), cvValidBasisOfPayment);
				}
				CoeusVector cvValidMethodPayment = awardLookUpDataTxnBean.getValidMethodOfPayment();
				if(cvValidMethodPayment != null && cvValidMethodPayment.size() > 0){
					data.put(new Integer(2),cvValidMethodPayment);
				}
				
				CoeusVector cvAllMethodOfPayment = awardReportTxnBean.getAllMethodOfPayment();
				if(cvAllMethodOfPayment != null && cvAllMethodOfPayment.size() > 0){
					data.put(new Integer(3),cvAllMethodOfPayment);
				}
				responder.setDataObject(data);
				responder.setResponseStatus(true);
				responder.setMessage(null);
			}else if(functionType == GET_YNQ_DETAILS) {
				Hashtable data = new Hashtable();
				YNQTxnBean ynqTxnBean = new YNQTxnBean();
				UserMaintDataTxnBean userMaintTxnBean = new UserMaintDataTxnBean();
				
				boolean hasRight = userMaintTxnBean.getUserHasOSPRight(loggedinUser, "MAINTAIN_CODE_TABLES");
				data.put(KeyConstants.VIEW_RIGHTS, new Boolean(hasRight));
				
				CoeusVector cvQuestionExpl = ynqTxnBean.getQuestionExplanationAll();
				data.put(YNQExplanationBean.class, cvQuestionExpl);
				
				CoeusVector cvYNQ = ynqTxnBean.getAllYNQ();
				data.put(YNQBean.class, cvYNQ);
				
				responder.setDataObject(data);
				responder.setMessage(null);
				responder.setResponseStatus(true);
			}else if(functionType==GET_ALL_QUESTION_DEATAILS){
				String questionId = (String)requester.getDataObject();
				Hashtable data = new Hashtable();
				YNQTxnBean ynqTxnBean = new YNQTxnBean();
				CoeusVector cvGetData =ynqTxnBean.getYNQDetail(questionId);
				responder.setDataObjects(cvGetData);
				responder.setMessage(null);
				responder.setResponseStatus(true);
			} else if(functionType == UPDATE_YNQ) {
//                            Added by Jinu
                            boolean success = true;
                            Hashtable data = (Hashtable)requester.getDataObject();
                            if(data != null){
				CoeusVector cvYNQ = (CoeusVector)data.get(YNQBean.class);
				CoeusVector cvYNQExp = (CoeusVector)data.get(YNQExplanationBean.class);
				AdminTxnBean adminTxnBean = new AdminTxnBean(loggedinUser);
				YNQBean ynqBean = null;
                                YNQExplanationBean ynqExplanationBean = null;
				if(cvYNQExp != null && cvYNQExp.size() > 0) {
					for(int index=0; index < cvYNQExp.size(); index++) {
						ynqExplanationBean = (YNQExplanationBean)cvYNQExp.elementAt(index);
						if(ynqExplanationBean.getAcType()==null){
							continue;
						}else if(ynqExplanationBean.getAcType().equals(TypeConstants.DELETE_RECORD)){
                                                    success = adminTxnBean.updateYNQExplanation(ynqExplanationBean);
                                                }
					}
				}
				if(cvYNQ != null && cvYNQ.size() > 0) {
					for(int index=0; index < cvYNQ.size(); index++) {
						ynqBean = (YNQBean)cvYNQ.elementAt(index);
						if(ynqBean.getAcType()==null){
							continue;
						}else if(ynqBean.getAcType().equals(TypeConstants.DELETE_RECORD)){
                                                    success = adminTxnBean.updateYNQ(ynqBean);
                                                }
					}
				}
				if(cvYNQ != null && cvYNQ.size() > 0) {
					for(int index=0; index < cvYNQ.size(); index++) {
						ynqBean = (YNQBean)cvYNQ.elementAt(index);
						if(ynqBean.getAcType()==null){
							continue;
						}else if(ynqBean.getAcType().equals(TypeConstants.INSERT_RECORD) 
                                                    || ynqBean.getAcType().equals(TypeConstants.UPDATE_RECORD) ){
                                                    success = adminTxnBean.updateYNQ(ynqBean);
                                                }
					}
				}
				if(cvYNQExp != null && cvYNQExp.size() > 0) {
					for(int index=0; index < cvYNQExp.size(); index++) {
						ynqExplanationBean = (YNQExplanationBean)cvYNQExp.elementAt(index);
						if(ynqExplanationBean.getAcType()==null){
							continue;
						}else if(ynqExplanationBean.getAcType().equals(TypeConstants.INSERT_RECORD)
                                                    || ynqExplanationBean.getAcType().equals(TypeConstants.UPDATE_RECORD)){
                                                    success = adminTxnBean.updateYNQExplanation(ynqExplanationBean);
                                                }
					}
				}
                            }
                            responder.setResponseStatus(true);
                            responder.setMessage(null);
			} else if(functionType == UPDATE_METHOD_OF_PAYMENT){
				CoeusVector cvValidBasisPayment = (CoeusVector)requester.getDataObject();
                AwardLookUpDataTxnBean awardLookUpDataTxnBean = new AwardLookUpDataTxnBean();
				AdminTxnBean adminTxnBean = new AdminTxnBean(loggedinUser);
                boolean success = false;
				ValidBasisMethodPaymentBean validBasisMethodPaymentBean = null;
				if(cvValidBasisPayment != null && cvValidBasisPayment.size() > 0){
					for(int index=0; index < cvValidBasisPayment.size(); index++){
						validBasisMethodPaymentBean = (ValidBasisMethodPaymentBean)cvValidBasisPayment.elementAt(index);
						if(validBasisMethodPaymentBean.getAcType()==null){
							continue;
						}
						success = adminTxnBean.updateValidBasisPayment(validBasisMethodPaymentBean);
					}
					//CoeusVector cvValidBasisMethodPayment = awardLookUpDataTxnBean.getValidMethodOfPayment();
					//responder.setDataObject(cvValidBasisMethodPayment);
					responder.setResponseStatus(success);
					responder.setMessage(null);
				}
			}else if(functionType == UPDATE_VALID_FREQUENCY_BASE){
				CoeusVector cvValidFrequencyBase = (CoeusVector)requester.getDataObject();
				AdminTxnBean adminTxnBean = new AdminTxnBean(loggedinUser);
                                AwardLookUpDataTxnBean awardLookUpDataTxnBean = new AwardLookUpDataTxnBean();
				FrequencyBaseBean frequencyBaseBean = null;
				if(cvValidFrequencyBase != null && cvValidFrequencyBase.size() > 0){
					for(int index=0; index < cvValidFrequencyBase.size(); index++){
						frequencyBaseBean = (FrequencyBaseBean)cvValidFrequencyBase.elementAt(index);
						if(frequencyBaseBean.getAcType()==null){
							continue;
						}
						boolean success = adminTxnBean.updateValidFrequencyBase(frequencyBaseBean);
						
					}
                                CoeusVector cvValidFreqBase = awardLookUpDataTxnBean.getAllValidFrequencyBase();
                                responder.setDataObject(cvValidFreqBase);
                                responder.setResponseStatus(true);
                                responder.setMessage(null);
				}
				
			// added by chandra to get only explnations .
                        }else if(functionType==GET_ONLY_EXPLANATION){
                            String questionId = (String)requester.getDataObject();
                            YNQTxnBean ynqTxnBean = new YNQTxnBean();
                            UserMaintDataTxnBean userMaintTxnBean = new UserMaintDataTxnBean();
                            CoeusVector cvQuestionExpl = ynqTxnBean.getQuestionExplanation(questionId);
                            responder.setDataObjects(cvQuestionExpl);
                            responder.setMessage(null);
                            responder.setResponseStatus(true);
                        }
                        
		} catch(LockingException lockEx) {
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
			"AdminMaintenanceServlet", "doPost");
		} catch( CoeusException coeusEx ) {
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
			"AdminMaintenanceServlet", "doPost");
			
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
			responder.setException(dbEx);
			responder.setMessage(errMsg);
			UtilFactory.log( errMsg, dbEx,
			"AdminMaintenanceServlet", "doPost");
			
		}catch(Exception e) {
			//e.printStackTrace();
			responder.setResponseStatus(false);
			responder.setException(e);
			responder.setMessage(e.getMessage());
			UtilFactory.log( e.getMessage(), e,
			"AdminMaintenanceServlet", "doPost");
                //Case 3193 - START
                }catch(Throwable throwable){
                    Exception ex = new Exception(throwable);
                    responder.setException(ex);
                    responder.setResponseStatus(false);
                    responder.setMessage(ex.getMessage());
                    UtilFactory.log( throwable.getMessage(), throwable, "AdminMaintenanceServlet", "doPost");
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
				"AdminMaintenanceServlet", "doPost");
			}
		}
	}
	
}
