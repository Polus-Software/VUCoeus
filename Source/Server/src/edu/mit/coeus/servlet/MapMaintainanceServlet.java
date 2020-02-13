/*
 * MapMaintainanceServlet.java
 * Created on October 14, 2005, 12:04 PM
 *
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 *
 */

package edu.mit.coeus.servlet;

import edu.mit.coeus.bean.CoeusMessageResourcesBean;
import edu.mit.coeus.bean.UserDetailsBean;
import edu.mit.coeus.bean.UserInfoBean;
import edu.mit.coeus.brokers.RequesterBean;
import edu.mit.coeus.brokers.ResponderBean;
import edu.mit.coeus.exception.CoeusException;
import edu.mit.coeus.mapsrules.bean.MapDetailsBean;
import edu.mit.coeus.mapsrules.bean.MapHeaderBean;
import edu.mit.coeus.mapsrules.bean.MapUpdateTxnBean;
import edu.mit.coeus.mapsrules.bean.MapsTxnBean;
import edu.mit.coeus.personroles.bean.PersonRoleDataTxnBean;
import edu.mit.coeus.user.bean.UserMaintDataTxnBean;
import edu.mit.coeus.utils.CoeusVector;
import edu.mit.coeus.utils.TypeConstants;
import edu.mit.coeus.utils.UtilFactory;
import edu.mit.coeus.utils.dbengine.DBException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Hashtable;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author  vinayks
 */
public class MapMaintainanceServlet extends CoeusBaseServlet implements TypeConstants  {
    
    private static final char GET_MAP_DETAILS_DATA = 'A';
    private static final char GET_MAP_DATA='B';
    private static final char GET_MAP_ID='C';
    private static final char GET_USERS_FOR_UNIT='D';
    private static final char GET_UNIT_MAP_DETAIL_DATA = 'E';
    private static final char DELETE_UNIT_MAP='F';
    private static final char UPDATE_UNIT_MAP='G';
    
    /**
     * This method handles all the POST requests from the Client
     *
     * @param request HttpServletRequest
     * @param response HttpServletResponse
     * @throws ServletException if any ServletException
     * @throws IOException if any IOException
     */
    
    public void doPost(HttpServletRequest request,
    HttpServletResponse response)
    throws ServletException,IOException{
        // the request object from applet
        RequesterBean requester = null;
        // the response object to applet
        ResponderBean responder = new ResponderBean();
        
         CoeusMessageResourcesBean coeusMessageResourcesBean
            = new CoeusMessageResourcesBean();
        
        // open object input/output streams
        ObjectInputStream inputFromApplet = null;
        ObjectOutputStream outputToApplet = null;
        
        String loggedinUser ="";
        String loggedUnitNumber = "";
        String userId = "";
        MapsTxnBean mapsTxnBean = null;
        MapUpdateTxnBean mapUpdateTxnBean = null;
        boolean canUserAddMap =false;
        boolean canUserModifyMap=false;
        
        try{
            // get an input stream
            inputFromApplet = new ObjectInputStream(request.getInputStream());
            
            // read the serialized request object from applet
            requester = (RequesterBean) inputFromApplet.readObject();
            isValidRequest(requester);
            loggedinUser = requester.getUserName();
            
            // get the user
            UserInfoBean userBean = (UserInfoBean)new
            UserDetailsBean().getUserInfo(requester.getUserName());
            
            loggedUnitNumber = userBean.getUnitNumber();
            userId = userBean.getUserId();
            char functionType = requester.getFunctionType();
            //To get the Map and Map Details Data
            if(functionType == GET_MAP_DETAILS_DATA){
                mapsTxnBean = new MapsTxnBean();
                CoeusVector cvData = new CoeusVector();
                int mapId = ((Integer)requester.getDataObject()).intValue();
                CoeusVector cvMapHeaderData =mapsTxnBean.getUnitMapHeaderData(mapId);
                cvData.add(cvMapHeaderData);
                CoeusVector cvMapDetailData = mapsTxnBean.getUnitMapDetailData(mapId);
                cvData.add(cvMapDetailData);
                
                responder.setDataObject(cvData);
                responder.setMessage(null);
                responder.setResponseStatus(true);
            }else if(functionType ==GET_MAP_DATA){//To get the Map Header Data
                mapsTxnBean = new MapsTxnBean();
                CoeusVector cvData = new CoeusVector();
                CoeusVector params = (CoeusVector)requester.getDataObject();
                String unitNumber=(String)params.get(0);
                
                /**This checks whether the user has ADD_MAP
                 *Right if not Disable the add menu item.
                 */
                // Case 2447 - start - step1
                // Use UserMaintDataTxnBean which uses authorization package
                UserMaintDataTxnBean userMaintDataTxnBean = new UserMaintDataTxnBean();
                canUserAddMap = userMaintDataTxnBean.getUserHasRight(loggedinUser, "ADD_MAP",unitNumber);
                cvData.addElement(new Boolean(canUserAddMap));
                 // Case 2447 - End - step1
                  
                /**This checks whether the user has MODIFY_MAP
                 *Right if not Disable the modify and delete menu item.
                 */
                 // Case 2447 - start - step2
                canUserModifyMap=userMaintDataTxnBean.getUserHasRight(loggedinUser, "MODIFY_MAP",unitNumber);
                cvData.addElement(new Boolean(canUserModifyMap));                
                // Case 2447 - end - step2
                CoeusVector cvMapData =mapsTxnBean.getMapHeaderData(unitNumber);
                cvData.addElement(cvMapData);
                
                responder.setDataObject(cvData);
                responder.setMessage(null);
                responder.setResponseStatus(true);
            }else if(functionType == GET_UNIT_MAP_DETAIL_DATA){//Get the Detail Data for the Map 
                mapsTxnBean = new MapsTxnBean();
                int mapId = ((Integer)requester.getDataObject()).intValue();
                CoeusVector cvMapDetailData = mapsTxnBean.getUnitMapDetailData(mapId);
                responder.setDataObject(cvMapDetailData);
                responder.setMessage(null);
                responder.setResponseStatus(true);                
            }else if(functionType==GET_MAP_ID){//This gets the Next Map Id
                mapsTxnBean = new MapsTxnBean();
                Integer mapID =mapsTxnBean.getNextMapID();
                responder.setDataObject(mapID);
                responder.setMessage(null);
                responder.setResponseStatus(true);
            }else if(functionType == GET_USERS_FOR_UNIT){ //To get the users for the unit number
                mapsTxnBean = new MapsTxnBean();
                String unitNumber = (String)requester.getDataObject();
                CoeusVector cvUserData = mapsTxnBean.getUserForUnit(unitNumber);
                //Modified for Coeus 4.3 Routing enhancement -PT ID:2785 - start
                CoeusVector cvMapData = new CoeusVector();
                //To fetch the roles
                PersonRoleDataTxnBean personRoleTxnBean = new PersonRoleDataTxnBean();
                CoeusVector cvRoles = personRoleTxnBean.getRoleList();
                //To fetch the qualifiers
                CoeusVector cvQualifiers = personRoleTxnBean.getQualifierList(null);
                cvMapData.add(0,cvUserData);
                cvMapData.add(1,cvRoles);
                cvMapData.add(2,cvQualifiers);
                responder.setDataObject(cvMapData);
                //responder.setDataObject(cvUserData);
                //Modified for Coeus 4.3 Routing enhancement -PT ID:2785 - start
                responder.setMessage(null);
                responder.setResponseStatus(true);
            }else if(functionType == DELETE_UNIT_MAP ){//To delete a Map by passsing mapid
                mapUpdateTxnBean = new MapUpdateTxnBean(loggedinUser);
                int mapId = ((Integer)requester.getDataObject()).intValue();
                Integer number = mapUpdateTxnBean.deleteMaps(mapId);
                responder.setDataObject(number);
                responder.setMessage(null);
                responder.setResponseStatus(true);                
            }else if(functionType == UPDATE_UNIT_MAP){//To Update the Maps
                mapUpdateTxnBean = new MapUpdateTxnBean(loggedinUser);
                Hashtable htMapData = (Hashtable)requester.getDataObject();
                boolean success = false;
                MapHeaderBean mapHeaderBean = (MapHeaderBean)htMapData.get(MapHeaderBean.class);
                CoeusVector cvDetailData = (CoeusVector)htMapData.get(MapDetailsBean.class);
                char mode=((Character)htMapData.get("MODE")).charValue();
                //To update the map header data
                success = mapUpdateTxnBean.updateMaps( mapHeaderBean,mode);
                
                if(success){
                     if(cvDetailData != null && cvDetailData.size() > 0){
                        for(int count=0;count < cvDetailData.size();count++){
                            MapDetailsBean mapDetailsBean = (MapDetailsBean)cvDetailData.elementAt(count);
                            if(mapDetailsBean.getAcType() == null){
                                continue;
                            }
                            //To update the Map Details data
                            success = mapUpdateTxnBean.updateMapDetails(mapDetailsBean,mode);
                        }//End of For loop
                    }//End of inner if
                }//End of outer if
                responder.setDataObject(new Boolean(success));
                responder.setMessage(null);
                responder.setResponseStatus(true);
            }
            
            
        }catch( CoeusException coeusEx ) {
            //coeusEx.printStackTrace();
            String errMsg;
            if(coeusEx.getErrorId()==999999){
                errMsg = "dbEngine_intlErr_exceptionCode.1028";
                responder.setLocked(true);
            }else{
                errMsg = coeusEx.toString();
            }
           
            errMsg= coeusMessageResourcesBean.parseMessageKey(errMsg);
            responder.setException(coeusEx);
            responder.setResponseStatus(false);
            
            responder.setMessage(errMsg);
            UtilFactory.log( errMsg, coeusEx,
            "MapMaintainanceServlet", "doPost");
            
        }catch( DBException dbEx ) {
            //dbEx.printStackTrace();
            String errMsg = dbEx.getUserMessage();
            if (dbEx.getErrorId() == 20102 ) {
                errMsg = "dbEngine_intlErr_exceptionCode.1028";
            }
            if (errMsg.equals("db_exceptionCode.1111")) {
                responder.setCloseRequired(true);
            }
           
            errMsg= coeusMessageResourcesBean.parseMessageKey(errMsg);
            
            responder.setResponseStatus(false);
            
            //print the error message at client side
            responder.setException(dbEx);
            responder.setMessage(errMsg);
            UtilFactory.log( errMsg, dbEx,
            "MapMaintainanceServlet", "perform");
            
        }catch(Exception e) {
            //e.printStackTrace();
            //print the error message at server side
            responder.setResponseStatus(false);
            //print the error message at client side
            responder.setException(e);
            responder.setMessage(e.getMessage());
            UtilFactory.log( e.getMessage(), e,
            "MapMaintainanceServlet", "perform");
        //Case 3193 - START
        }catch(Throwable throwable){
            Exception ex = new Exception(throwable);
            responder.setException(ex);
            responder.setResponseStatus(false);
            responder.setMessage(ex.getMessage());
            UtilFactory.log( throwable.getMessage(), throwable, "MapMaintainanceServlet", "doPost");
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
                "MapMaintainanceServlet", "doPost");
            }
        }
        
    }
    
}
