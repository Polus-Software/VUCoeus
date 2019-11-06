/*
 * RatesMaintenanceServlet.java
 *
 * Created on August 17, 2004, 8:36 PM
 */

package edu.mit.coeus.servlet;

import edu.mit.coeus.brokers.RequesterBean;
import edu.mit.coeus.brokers.ResponderBean;
import edu.mit.coeus.bean.*;
import edu.mit.coeus.rates.bean.CERatesBean;

import edu.mit.coeus.utils.UtilFactory;
import edu.mit.coeus.utils.TypeConstants;
import edu.mit.coeus.utils.KeyConstants;
import edu.mit.coeus.utils.dbengine.DBException;
import edu.mit.coeus.exception.CoeusException;

import edu.mit.coeus.moduleparameters.bean.ParameterBean;
import edu.mit.coeus.moduleparameters.parser.ProcessParameterXML;
import edu.mit.coeus.utils.CoeusConstants;
import edu.mit.coeus.user.bean.UserMaintDataTxnBean;
import edu.mit.coeus.budget.bean.*;
import edu.mit.coeus.utils.CoeusVector;
import edu.mit.coeus.propdev.bean.ProposalDevelopmentFormBean;
import edu.mit.coeus.propdev.bean.ProposalDevelopmentTxnBean;
import edu.mit.coeus.departmental.bean.DepartmentPersonTxnBean;
import edu.mit.coeus.budget.calculator.bean.ValidCalcTypesBean;
import edu.mit.coeus.utils.query.QueryEngine;
import edu.mit.coeus.utils.xml.bean.budget.generator.BudgetStream;
import edu.mit.coeus.propdev.bean.ProposalActionTxnBean;
import edu.mit.coeus.utils.CoeusFunctions;
import edu.mit.coeus.budget.report.ReportGenerator;
import edu.mit.coeus.utils.locking.LockingBean;
import edu.mit.coeus.utils.locking.LockingException;
import edu.mit.coeus.rates.bean.RateClassBean;
import edu.mit.coeus.rates.bean.RateTypeBean;
import edu.mit.coeus.rates.bean.RatesTxnBean;
import edu.mit.coeus.rates.bean.InstituteRatesBean;
import edu.mit.coeus.utils.ComboBoxBean;

import javax.servlet.http.*;
import javax.servlet.*;
import java.io.*;
import java.util.Vector;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Properties;
import java.util.Date;
import java.text.SimpleDateFormat;

/**
 *
 * @author  shivakumarmj
 */
public class RatesMaintenanceServlet extends CoeusBaseServlet implements TypeConstants{
    
    // Functionalities
    private static final char GET_RATE_DATA = 'A';
    
    private static final char UPDATE_RATE_DATA = 'B';
    
    private static final char VALID_CE_RATES = 'C';
    
    private static final char COST_ELEMENT_LIST = 'E';
    
    private static final char UPDATE_CE_RATES = 'U';
    
    //COEUSQA:2393 - Revamp Coeus Budget Engine - Start
    private static final char GET_RATE_CLASS_TYPE_DATA = 'R';
    //COEUSQA:2393 - End
	
	// Rights
    private static final String MAINTAIN_CODE_TABLES = "MAINTAIN_CODE_TABLES";
    private static final String MAINTAIN_UNIT_CE_RATES = "MAINTAIN_UNIT_CE_RATES";
    public void doPost(HttpServletRequest request,
    HttpServletResponse response)
    throws ServletException, IOException {
        
//        UtilFactory UtilFactory = new UtilFactory();
        
        // the request object from applet
        RequesterBean requester = null;
        // the response object to applet
        ResponderBean responder = new ResponderBean();
        
        // open object input/output streams
        ObjectInputStream inputFromApplet = null;
        ObjectOutputStream outputToApplet = null;
        
        String loggedinUser ="";
        String unitNumber = "";
        
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
            if(functionType == GET_RATE_DATA){                
                Hashtable hshReport = new Hashtable();
                InstituteRatesBean instituteRatesBean = (InstituteRatesBean)requester.getDataObject();
                String ratesUnitNumber = instituteRatesBean.getUnitNumber();
                UserMaintDataTxnBean userMaintDataTxnBean = new UserMaintDataTxnBean();
                RatesTxnBean ratesTxnBean = new RatesTxnBean();
                CoeusVector coeusVector = ratesTxnBean.getRateClassList();
                if(coeusVector==null){
                        coeusVector = new CoeusVector();
                }                
                hshReport.put(KeyConstants.RATE_CLASS_DATA, coeusVector);                
                coeusVector = ratesTxnBean.getRateTypeList();
                if(coeusVector==null){
                        coeusVector = new CoeusVector();
                }                
                hshReport.put(KeyConstants.RATE_TYPE_DATA, coeusVector); 
                // Getting parent unit number
                // Commented by Shivakumar as per the request of Shivakumar
//                String parentUnitNumber = ratesTxnBean.getTopLevelUnit(ratesUnitNumber);
//                hshReport.put("PARENT_UNIT_NUMBER", parentUnitNumber);
                coeusVector = ratesTxnBean.getInstituteRates(ratesUnitNumber);                                
                hshReport.put(InstituteRatesBean.class, coeusVector);
                boolean hasRight = userMaintDataTxnBean.getUserHasOSPRight(loggedinUser, MAINTAIN_CODE_TABLES);
                hshReport.put("HAS_OSP_RIGHT", new Boolean(hasRight));				
        	CoeusVector vecActivityTypes = ratesTxnBean.getActivityTypes();
		hshReport.put(KeyConstants.ACTIVITY_TYPES, vecActivityTypes);
                responder.setDataObject(hshReport);                
                responder.setMessage(null);
                responder.setResponseStatus(true);
            }
             if(functionType == UPDATE_RATE_DATA){                
                Hashtable hshRateData = (Hashtable)requester.getDataObject();
                RatesTxnBean ratesTxnBean = new RatesTxnBean(loggedinUser);
                boolean  success = ratesTxnBean.updateInstituteRateData(hshRateData);
                responder.setDataObject(new Boolean(success));                
                responder.setMessage(null);
                responder.setResponseStatus(true);
             }
              //Added for Case #3121 - start
              //Tuition Fee calculation Enhancement
            if(functionType == VALID_CE_RATES) {
                RatesTxnBean ratesTxnBean = new RatesTxnBean();
                CERatesBean ceRatesBean = (CERatesBean) requester.getDataObject();
                String ceUnitNumber = ceRatesBean.getUnitNumber();
                CoeusVector cvRates = ratesTxnBean.getValidCERates(ceUnitNumber);
                responder.setDataObject(cvRates);
                responder.setMessage(null);
                responder.setResponseStatus(true);
            } 
            else if(functionType == COST_ELEMENT_LIST) {
                RatesTxnBean ratesTxnBean = new RatesTxnBean();
                UserMaintDataTxnBean userMaintDataTxnBean = new UserMaintDataTxnBean();
                String selunitNumber = (String) requester.getDataObject();
                boolean hasRight = userMaintDataTxnBean.getUserHasRight(loggedinUser, MAINTAIN_UNIT_CE_RATES, selunitNumber);
                Vector vecData = new Vector();
                CoeusVector cvCostList = ratesTxnBean.getCostElementList();
                vecData.addElement(cvCostList);
                vecData.addElement(new Boolean(hasRight));
                responder.setDataObject(vecData);
                responder.setMessage(null);
                responder.setResponseStatus(true);
            } else if(functionType == UPDATE_CE_RATES) {
                RatesTxnBean ratesTxnBean = new RatesTxnBean(loggedinUser);
                Hashtable htUpdateData = (Hashtable) requester.getDataObject();
                boolean success = ratesTxnBean.updateCERates(htUpdateData);
                responder.setDataObject(new Boolean(success));
                responder.setMessage(null);
                responder.setResponseStatus(true);
            }
            //Added for Case #3121 - end
            //COEUSQA:2393 - Revamp Coeus Budget Engine - Start
            else if(functionType == GET_RATE_CLASS_TYPE_DATA){
                Hashtable hshReport = new Hashtable();
                RatesTxnBean ratesTxnBean = new RatesTxnBean();
                CoeusVector coeusVector = ratesTxnBean.getRateClassList();
                if(coeusVector==null){
                    coeusVector = new CoeusVector();
                }
                hshReport.put(KeyConstants.RATE_CLASS_DATA, coeusVector);
                coeusVector = ratesTxnBean.getRateTypeList();
                if(coeusVector==null){
                    coeusVector = new CoeusVector();
                }
                BudgetDataTxnBean budgetDataTxnBean = new BudgetDataTxnBean();
                CoeusVector cvRateClassInclusions =  budgetDataTxnBean.getRateClassBaseInculsions();
                
                CoeusVector cvRateClassExclusions =  budgetDataTxnBean.getRateClassBaseExclusions();
                
                hshReport.put(KeyConstants.RATE_TYPE_DATA, coeusVector);
                if(cvRateClassInclusions == null) {
                    cvRateClassInclusions = new CoeusVector();
                }
                if(cvRateClassExclusions == null) {
                    cvRateClassExclusions = new CoeusVector();
                }
                hshReport.put("RATE_CLASS_INCLUSIONS", cvRateClassInclusions);
                hshReport.put("RATE_CLASS_EXCLUSIONS", cvRateClassExclusions);
                
                responder.setDataObject(hshReport);
                responder.setMessage(null);
                responder.setResponseStatus(true);
            }
            //COEUSQA:2393 - End
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
                "RatesMaintenanceServlet", "doPost");
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
            responder.setException(coeusEx);
            responder.setResponseStatus(false);
            
            responder.setMessage(errMsg);
            UtilFactory.log( errMsg, coeusEx,
            "RatesMaintenanceServlet", "doPost");
            
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
            responder.setException(dbEx);
            responder.setResponseStatus(false);
            
            responder.setMessage(errMsg);
            UtilFactory.log( errMsg, dbEx,
            "RatesMaintenanceServlet", "doPost");
            
        }catch(Exception e) {
            //e.printStackTrace();
            responder.setResponseStatus(false);
            responder.setException(e);
            responder.setMessage(e.getMessage());
            UtilFactory.log( e.getMessage(), e,
            "RatesMaintenanceServlet", "doPost");
            //Case 3193 - START
        }catch(Throwable throwable){
            Exception ex = new Exception(throwable);
            responder.setException(ex);
            responder.setResponseStatus(false);
            responder.setMessage(ex.getMessage());
            UtilFactory.log( throwable.getMessage(), throwable, "RatesMaintenanceServlet", "doPost");
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
                "RatesMaintenanceServlet", "doPost");
            }
        }
    }  
    
}
