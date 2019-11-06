/*
 * @(#)CoeusUtils.java
 *
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 *
 */
package edu.mit.coeus.utils;

import edu.mit.coeus.brokers.*;
import edu.mit.coeus.unit.bean.*;
import edu.mit.coeus.irb.bean.*;
import edu.mit.coeus.gui.CoeusMessageResources;

/**
 * This class is used to define the set of common functionalities of coeus 
 * Application like fetch Person Details with personID, UnitDetails with Unit 
 * Id. 
 *
 * @author  subramanya Created on March 14, 2003, 10:20 AM
 */
public class CoeusUtils {

    private static final String COEUS_FUNCTIONS_SERVLET = "/coeusFunctionsServlet";
    
    //This holds the instance created falg.
    private static boolean hasInstance = false;
    
    //Holds the Requester Bean instance
    private RequesterBean requester = null;
    
    //Holds the Responder Bean instance
    private ResponderBean response = null;
    
    //Holds the AppletServletCommunicator instance
    private AppletServletCommunicator appServletComm = null;
    
    //This hold the self reference (instance )of this class.
    private static CoeusUtils coeusUitlsInstance = null;
    
    //holds the db access flag
    private boolean isTxnSuccess = false; 
    
    //holds CoeusMessageResources instance used for reading message Properties.
    private CoeusMessageResources coeusMessageResources;
    
    //private constructor which restrict the user from instantiating this object.
    private CoeusUtils() {
        coeusMessageResources = CoeusMessageResources.getInstance();
    }
    
    
    /**
     * This is a singleton method used to get the Instance of this class
     *
     * @return CoeusUtils 
     */
    public static CoeusUtils getInstance(){
        if( !hasInstance ) {
            coeusUitlsInstance = new CoeusUtils();
        }
        return coeusUitlsInstance;
    }
 
    /**
     * This is a supporting method to get PersonInfoBean for specific person which will
     * validate the person name against the db value
     * @param name string value represent the person Name
     * @return PersonInfoFormBean which contains person details
     */
    public PersonInfoFormBean getPersonInfoID( String name ){
   
        requester = new RequesterBean();
        requester.setDataObject("GET_PERSONINFO");
        requester.setId(name);
        
        appServletComm = new AppletServletCommunicator( 
                                        CoeusGuiConstants.CONNECTION_URL + 
                                        CoeusGuiConstants.FUNCTION_SERVLET,
                                        requester );
        appServletComm.send();
        response = appServletComm.getResponse();
        PersonInfoFormBean personInfoFormBean = null;
        if ( response!=null ){        
            personInfoFormBean = (PersonInfoFormBean) response.getDataObject();
        }        
        return personInfoFormBean;
    }
    
    /**
     * This is a supporting method to get UnitDetailFormBean for 
     * specific unit number which will be used to validate 
     * the unit id/name against the data base.
     * @param unitNumber string value represent the unit number.
     * @return UnitDetailFormBean contains the unit details.
     */
    public UnitDetailFormBean getUnitInfoBean( String unitNumber ){
        
        requester = new RequesterBean();
        requester.setFunctionType('G');
        requester.setId( unitNumber );
        
        appServletComm = new AppletServletCommunicator(
                                        CoeusGuiConstants.CONNECTION_URL + 
                                        CoeusGuiConstants.UNIT_SERVLET,
                                        requester );
        appServletComm.send();
        ResponderBean response = appServletComm.getResponse();
        UnitDetailFormBean unitInfoBean = null;
        if ( response != null ){            
            unitInfoBean = (UnitDetailFormBean) response.getDataObject();
        }
        if( unitInfoBean == null || unitInfoBean.getUnitNumber() == null){
            unitInfoBean = null;
            CoeusOptionPane.showWarningDialog(
                                coeusMessageResources.parseMessageKey(
                                        "protoInvFrm_exceptionCode.1138"));
        }
        return unitInfoBean;
    }
        /**
	 * Get Database Timestamp on client side
	 * @return  Database Timestamp
	 */
	public static java.sql.Timestamp getDBTimeStamp() {
            java.sql.Timestamp dbTimeStamp;
            String connectTo = CoeusGuiConstants.CONNECTION_URL+COEUS_FUNCTIONS_SERVLET;
            // connect to the database and get the current DBTimestamp
            RequesterBean request = new RequesterBean();
            request.setDataObject("GET_DBTIMESTAMP");
            AppletServletCommunicator comm
            = new AppletServletCommunicator(connectTo, request);
            comm.send();
            ResponderBean response = comm.getResponse();
            dbTimeStamp = (java.sql.Timestamp) response.getDataObject();
            return dbTimeStamp;
	}        
    
    
}
