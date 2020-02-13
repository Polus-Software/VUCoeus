/**
 * @(#)RolodexMaintController.java  1.0
 *
 * Created on August 24, 2002, 9:17 PM
 *
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 */

package edu.mit.coeus.rolodexmaint.bean;

import edu.mit.coeus.brokers.RequesterBean;
import edu.mit.coeus.brokers.ResponderBean;
import edu.mit.coeus.utils.CoeusGuiConstants;
import edu.mit.coeus.utils.AppletServletCommunicator;

import java.net.URL;
import java.io.InputStream;
import java.io.ObjectOutputStream;
import java.net.URLConnection;
import java.io.ObjectInputStream;
import java.io.IOException;
import java.util.Vector;

/** This class connects to the server and gets the RolodexDetailsBean based on the rolodexId.
 *
 * @author  phani
 * @version 1.0
 */
public class RolodexMaintController {
    
    private String CONNECTION_URL = CoeusGuiConstants.CONNECTION_URL;
    private final String CONTENT_KEY = "Content-Type";
    private final String CONTENT_VALUE =    "application/octet-stream";
    private final String ROLODEX_SERVLET = "/rolMntServlet";
    private  RequesterBean requester;
    //Added for COEUSQA-1434 : Add the functionality to set a status on a Sponsor record - start
    private String sponsorStatus = "";
   
    /** Creates new RolodexMaintController Default Constructor */
    public RolodexMaintController() {
    }
    
    /**
     *  Method used to send the requester Bean to the servlet for database communication.
     *
     * @param srvComponentName the Servlet to be used for communication to the database
     * @param requester a RequesterBean which consist of rolodexId and servlet details.
     *
     * @return ResponderBean
     *
     */
    public ResponderBean sendToServer(String srvComponentName,RequesterBean requester) {
        String connectTo =CoeusGuiConstants.CONNECTION_URL+srvComponentName;
        AppletServletCommunicator comm = new AppletServletCommunicator(
        connectTo, requester);
        comm.send();
        ResponderBean response = comm.getResponse();
        return response;
        
    }

    /**
     *  Method used to get the Rolodex Details for a given rolodexId from 
     *  the databse
     *
     *  @param rolodexId id for the rolodex.
     *
     *  @return RolodexDetailsBean
     */
     public RolodexDetailsBean displayRolodexInfo(String rolodexId) {
        String DISPLAY_TITLE = "DISPLAY ROLODEX";
        Vector responseData =new Vector();
        RolodexDetailsBean rldxBean = null;
        RequesterBean requester = new RequesterBean();
        ResponderBean response=null;
        
        /*set the function type as 'V' to notify the servlet to get the information from the database */
        requester.setFunctionType('V');
        requester.setRequestedForm("Rolodex Details");

        /*set the data for requester bean with 'rolodexid' a key value to get the information */
        requester.setDataObject(rolodexId);

        /* send the requester bean with the information to the server */
        response = sendToServer(ROLODEX_SERVLET,requester);
        if (response != null) {
            /* responseBean will be having RolodexDetails bean if it successful in retrieving the information 
             * from the database
             */
            rldxBean  = (RolodexDetailsBean)response.getDataObject();
        }
        /* return the rolodexDetails bean*/
        return rldxBean;
     }
     
     
     /**
     *  Method used to get the RolodexId for a given SponsorCode from 
     *  the databse
     *
     *  @param sponsorCode String
     *
     *  @return rolodexId
     */
      public String getRolodexIdForSponsor(String sponsorCode) {
        String rolodexId ="";
        requester = new RequesterBean();
        //Function Type 'R' is to specify in servlet to get RolodexId for Sponsor
        requester.setFunctionType('R');
        requester.setRequestedForm("Rolodex Details");
        /* set the dataObject with sponsorCode for retrieving coresponding rolodexId 
         * from the SponsorTable for the given SponsorCode
         */
        requester.setDataObject(sponsorCode);
        /*send the serialized requesterBean with the assigned information to the servlet */
        ResponderBean response = sendToServer(ROLODEX_SERVLET,requester);
        if (response != null) {
            /* response Bean will be returned with RolodexId as dataObject */
            rolodexId = response.getDataObject().toString();
        }
        /* return the rolodexId*/
        return rolodexId;
    }
    
     /**
     *  Method used to get the SponsorName for a given SponsorCode from 
     *  the databse
     *
     *  @param sponsorCode String
     *
     *  @return SponsorName
     */
      public String getSponsorName(String sponsorCode) {
          String sponsorName = "";
          requester = new RequesterBean();
        /* specify the function type in requester Bean as 'S' to notify the servlet to call a particular
         * procedure for the information retrieval from the database.
         */
          requester.setFunctionType('S');
          requester.setRequestedForm("Rolodex Details");
          /* set dataObject as 'SponsorCode' the key value for the sponsorName retrieval from the database*/
          requester.setDataObject(sponsorCode);
          /* send the  reuester Bean to the servlet with assigned information */
          ResponderBean response = sendToServer(ROLODEX_SERVLET,requester);
          if (response != null) {
              /* response Bean will be returned with sponsorName as dataObject*/
              //Commented/Added for case#3341 - Sponsor Code Validation
              //sponsorName= response.getDataObject().toString();
              //Added for COEUSQA-1434 : Add the functionality to set a status on a Sponsor record - start
              //sponsorName= (String)response.getDataObject();
              if(response.getDataObjects() != null){
                  sponsorName= (String)response.getDataObjects().get(0);
                  if(response.getDataObjects().get(1)!=null){
                      setSponsorStatus(response.getDataObjects().get(1).toString());
                  }
              }
              //Added for COEUSQA-1434 : Add the functionality to set a status on a Sponsor record - end
          }
          
          // prepare url for organization maintenance servlet
          return sponsorName;
      }
      
    //Added for COEUSQA-1434 : Add the functionality to set a status on a Sponsor record - start
    /**
     * gets the status of the Sponsor
     * @return status of the Sponsor
     */
    public String getSponsorStatus() {
        return sponsorStatus;
    }

    /**
     * sets the status to the Sponsor 
     * @param status
     */
    public void setSponsorStatus(String sponsorStatus) {
        this.sponsorStatus = sponsorStatus;
    }
    //Added for COEUSQA-1434 : Add the functionality to set a status on a Sponsor record - end
}
