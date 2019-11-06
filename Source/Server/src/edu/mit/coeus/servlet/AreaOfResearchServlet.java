/*
 * @(#)AreaOfResearchServlet.java
 *
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 *
 */

/* PMD check performed, and commented unused imports and variables on 06-AUGUST-2010
 * by Johncy M John
 */
package edu.mit.coeus.servlet;           

import edu.mit.coeus.bean.CoeusMessageResourcesBean;
import edu.mit.coeus.bean.UserDetailsBean;
import javax.servlet.*;
import javax.servlet.http.*;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.IOException;

import java.util.Vector;
import java.util.Hashtable;

import edu.mit.coeus.bean.UserInfoBean;
import edu.mit.coeus.utils.UtilFactory;
import edu.mit.coeus.irb.bean.AreaOfResearchTxnBean;
import edu.mit.coeus.irb.bean.ProtocolDataTxnBean;
import edu.mit.coeus.brokers.RequesterBean;
import edu.mit.coeus.brokers.ResponderBean; 
import edu.mit.coeus.exception.CoeusException;
//import edu.mit.coeus.bean.*;
import edu.mit.coeus.utils.ModuleConstants;
import edu.mit.coeus.utils.dbengine.DBException;

/**
 * This Servlet used to communicate from the Applet to DataBase 
 * by handling Applet Requests and sending across DataBase Response with 
 * interleaved business logic process at the server end. This communication
 * is confined to Area Of Research Tree Building and Tree Node Manipulation
 * request-response routine.
 *
 * @author  Subramanya
 * @version 1.0 September 23, 2002, 10:50 PM
 * @modified by Sagin
 * @date 29-10-02
 * Description : Implemented Standard Error Handling. 
 *
 */

public class AreaOfResearchServlet extends CoeusBaseServlet {
   
    //IACUC Changes - Start
    private static final char IACUC_AREA_OF_RESEARCH = 'Z';
    //IACUC Changes - End
    //COEUSQA-2802 - Allow user to remove values for areas of research in IRB and IACUC 
    private static final char CHECK_DEPENDENCY = 'C';
    private static final char RESEARCH_AREA_EXISTS = 'E';
    /** 
     * Initializes the Area Of Research servlet.
     * @param config contins the configuration info about the Servlet for the 
     *                  Web Container/Web Server. ( SevletConfig )
     * @exception ServletException 
     */  
    public void init( ServletConfig config ) throws ServletException {
        
        super.init( config );
        
    }

    
    /** Processes requests for both HTTP <code>GET</code> 
     * and <code>POST</code> methods.
     * @param request servlet request holds the client-side requests data Object
     * @param response servlet response hols the results for the client request 
     * data Object.
     * @exception ServletException 
     * @exception IOException 
     */
    protected void processRequest( HttpServletRequest request, 
                                                HttpServletResponse response)
                                throws ServletException, java.io.IOException {
        
        
        RequesterBean requester = null;
        // the response object to applet
        
        ResponderBean responder = new ResponderBean();
//        UtilFactory UtilFactory = new UtilFactory();
        
        // open object input/output streams
        ObjectInputStream inputFromApplet = null;
        ObjectOutputStream outputToApplet = null;
        
        try {
            // get an input stream
            inputFromApplet = new ObjectInputStream(request.getInputStream());
            // read the serialized request object from applet
            requester = (RequesterBean) inputFromApplet.readObject();
            isValidRequest(requester);
            // get the user
            UserInfoBean userInfo = (UserInfoBean)new 
                UserDetailsBean().getUserInfo(requester.getUserName());
            
            if( userInfo == null ){
                
                    throw new CoeusException("sess_exceptionCode.1003");
                    
            }    
            
            String userId = userInfo.getUserId();
            
            
            //obtains the User Request Type. Like 'I'/'U' denoting Insert / 
            // Update Operation Type.
            char functionType = requester.getFunctionType();
                       
            Vector researchAreaNodes = new Vector(3,2);
            
            //Area Of Research Transaction bean responsible for DB interaction.
            AreaOfResearchTxnBean researchTxn = new AreaOfResearchTxnBean ( 
                                                                    userId );
            edu.mit.coeus.iacuc.bean.AreaOfResearchTxnBean iacucResearchTxnBean = 
                    new edu.mit.coeus.iacuc.bean.AreaOfResearchTxnBean(userId );
            boolean isOprSuccessful = true;
            
            switch( functionType ){
                
                case( 'G' ) :
            
                    //fetchs all Area Of Research Tree Node and store it in 
                    //Vector. This is sent client side through response Object
                    researchAreaNodes = researchTxn.
                                                getResearchHierarchyDetails();
                    
                    break;
                    
                case( 'D' ) :    
                case( 'U' ) :
                case( 'I' ) : 
                    //COEUSQA-2684  Areas of research maintenance screen for IACUC areas of research - start
                    Vector param = requester.getDataObjects();
                    Hashtable modifiedNodes = ( Hashtable )requester.getDataObject();                    
                    if(param.get(0) != null) {
                        int moduleCode = Integer.parseInt(param.get(0).toString());
                        if(moduleCode == ModuleConstants.PROTOCOL_MODULE_CODE) {
                            //collection object from client-end for Insert/Update
                            //Operation
                            isOprSuccessful = researchTxn.addUpdDelAreaOfResearchData(modifiedNodes, functionType );
                            researchAreaNodes = researchTxn.getResearchHierarchyDetails();
                        } else if (moduleCode == ModuleConstants.IACUC_MODULE_CODE) {
                            edu.mit.coeus.iacuc.bean.AreaOfResearchTxnBean iacucResearchTxn = new edu.mit.coeus.iacuc.bean.AreaOfResearchTxnBean(userId );
                            isOprSuccessful = iacucResearchTxn.addUpdDelAreaOfResearchData(modifiedNodes, functionType );
                            researchAreaNodes = iacucResearchTxn.getResearchHierarchyDetails();
                        }
                    }
                    //COEUSQA-2684  Areas of research maintenance screen for IACUC areas of research - end
                    break;
                case('P' ):
                    
                    ProtocolDataTxnBean data = new ProtocolDataTxnBean();
                    researchAreaNodes = data.getCorrespondentTypes();
                    isOprSuccessful = true;
                    break;
                    //IACUC Changes - Start
                case (IACUC_AREA_OF_RESEARCH):
                    edu.mit.coeus.iacuc.bean.AreaOfResearchTxnBean iacucResearchTxn = 
                            new edu.mit.coeus.iacuc.bean.AreaOfResearchTxnBean(userId );
                    researchAreaNodes = iacucResearchTxn.getResearchHierarchyDetails();
                    break;
                    //IACUC Changes - End
                    
                    //COEUSQA-2802 - Allow user to remove values for areas of research in IRB and IACUC 
                case (CHECK_DEPENDENCY):
                    Vector dataObjects = requester.getDataObjects(); 
                    int canDelete = -1;
                    if(dataObjects.get(0) != null) {                        
                        int moduleCode = Integer.parseInt(dataObjects.get(0).toString());
                        if(moduleCode == ModuleConstants.PROTOCOL_MODULE_CODE) {
                            edu.mit.coeus.irb.bean.AreaOfResearchTreeNodeBean areaOfResearchBean = (edu.mit.coeus.irb.bean.AreaOfResearchTreeNodeBean)requester.getDataObject();
                            canDelete = researchTxn.checkCanDeleteResearchArea(areaOfResearchBean);
                        } else if(moduleCode == ModuleConstants.IACUC_MODULE_CODE) {
                            edu.mit.coeus.iacuc.bean.AreaOfResearchTreeNodeBean iacucAreaOfResearchBean = (edu.mit.coeus.iacuc.bean.AreaOfResearchTreeNodeBean)requester.getDataObject();                            
                            canDelete = iacucResearchTxnBean.checkCanDeleteResearchArea(iacucAreaOfResearchBean);
                        }
                    }
                    responder.setId(canDelete + "");  
                    break;
                    //check research_code exists in table
                case (RESEARCH_AREA_EXISTS):
                    Vector dataObject = requester.getDataObjects(); 
                    int researchCodeExits = -1;
                    if(dataObject.get(0) != null && dataObject.get(1) != null) {                        
                        int moduleCode = Integer.parseInt(dataObject.get(0).toString());
                        Vector vecResearchArea = (Vector)dataObject.get(1);
                        Vector vecSelectedRow = null;
                        String researchAreaCode = null;
                        for(int i = 0; i < vecResearchArea.size(); i++) {                                
                            vecSelectedRow = (Vector)vecResearchArea.get(i);
                            if(vecSelectedRow != null) {
                                researchAreaCode = vecSelectedRow.get(0).toString();
                                if(researchAreaCode != null){
                                    if(moduleCode == ModuleConstants.PROTOCOL_MODULE_CODE) {
                                        researchCodeExits = researchTxn.checkResearchAreaExists(researchAreaCode);
                                    } else if(moduleCode == ModuleConstants.IACUC_MODULE_CODE) {
                                        researchCodeExits = iacucResearchTxnBean.checkResearchAreaExists(researchAreaCode);
                                    }
                                    //Research area not existing
                                    if(researchCodeExits == 0) {
                                        responder.setMessage("Research Area Code : '" + researchAreaCode + "'");
                                        break;
                                    }
                                }

                            }                                
                        }
                    }
                    responder.setId(researchCodeExits + "");  
                    break;
                    
            }
            
            // set the responder object
            responder.setDataObject( researchAreaNodes );
            responder.setResponseStatus( isOprSuccessful );
            
            
        }catch( CoeusException coeusEx ) {
            
            String errMsg = coeusEx.getMessage();
            CoeusMessageResourcesBean coeusMessageResourcesBean=
                                                new CoeusMessageResourcesBean();
            errMsg= coeusMessageResourcesBean.parseMessageKey(errMsg);
            
            responder.setResponseStatus(false);

            //print the error message at client side
            responder.setException(coeusEx);
            responder.setMessage(errMsg);
            UtilFactory.log( errMsg, coeusEx, "AreaOfResearchServlet",
                                                                "perform");
            
        } catch( DBException dbEx ) {
            
            String errMsg = dbEx.getUserMessage();
            CoeusMessageResourcesBean coeusMessageResourcesBean=
                                                new CoeusMessageResourcesBean();
            errMsg= coeusMessageResourcesBean.parseMessageKey(errMsg);
            
            responder.setResponseStatus(false);

            //print the error message at client side
            responder.setException(dbEx);
            responder.setMessage(errMsg);
            UtilFactory.log( errMsg, dbEx, "AreaOfResearchServlet", "perform");
            
        } catch( Exception e ) {
            
            responder.setResponseStatus(false); 
            //print the error message at client side
            responder.setException(e);
            responder.setMessage( e.getMessage() );
            UtilFactory.log( e.getMessage(), e,"AreaOfResearchServlet",
                                                                "perform");
        //Case 3193 - START
        }catch(Throwable throwable){
            Exception ex = new Exception(throwable);
            responder.setException(ex);
            responder.setResponseStatus(false);
            responder.setMessage(ex.getMessage());
            UtilFactory.log( throwable.getMessage(), throwable, "AreaOfResearchServlet", "doPost");
        //Case 3193 - END    
        } finally {
            
            //explicitly release the resources like I/O Stream, communication
            //channel
            try{
            
                // send the object to applet
                outputToApplet = new ObjectOutputStream( 
                                                   response.getOutputStream());
                outputToApplet.writeObject( responder );
            
                // close the streams
                if (inputFromApplet!=null){
                    
                    inputFromApplet.close();
                    
                }
            
                
                if (outputToApplet!=null){
                    
                    outputToApplet.flush();
                    outputToApplet.close();
                    
                }
            
            }catch (IOException ioe){
                
                UtilFactory.log(ioe.getMessage(),ioe,"AreaOfResearchServlet",
                                                                    "perform");
            
            }
        }

    } 

    
   /** 
    * Handles the HTTP <code>GET</code> method.
    * @param request servlet request
    * @param response servlet response
    * @exception ServletException
    */
    protected void doGet( HttpServletRequest request, 
            HttpServletResponse response) throws ServletException, 
            java.io.IOException {
                
                processRequest(request, response);
                
    } 

    
   /** 
    * Handles the HTTP <code>POST</code> method.
    * @param request servlet request
    * @param response servlet response
    * @exception ServletException
    */
    protected void doPost( HttpServletRequest request, 
                           HttpServletResponse response)throws ServletException,
                           java.io.IOException {
                               
                processRequest(request, response);
                
    }

    
   /** 
    * Method used to get the Servlet Info. 
    * @return String Returns a short description of the servlet.
    */
    public String getServletInfo() {
        
        return "Short description-Area Of Research Servlet";
        
    }

}
