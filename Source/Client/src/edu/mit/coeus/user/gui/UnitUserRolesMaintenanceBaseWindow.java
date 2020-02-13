/*
 * UnitUserRolesMaintenanceBaseWindow.java
 *
 * Created on May 11, 2011, 2:50 PM
 *
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 *
 */

package edu.mit.coeus.user.gui;

import edu.mit.coeus.bean.UserInfoBean;
import edu.mit.coeus.brokers.RequesterBean;
import edu.mit.coeus.brokers.ResponderBean;
import edu.mit.coeus.gui.CoeusAppletMDIForm;
import edu.mit.coeus.gui.CoeusMessageResources;
import edu.mit.coeus.search.gui.CoeusSearch;
import edu.mit.coeus.user.bean.DataPositions;
import edu.mit.coeus.user.bean.UserRolesController;
import edu.mit.coeus.user.controller.UnitUserRolesMaintenanceController;
import edu.mit.coeus.utils.AppletServletCommunicator;
import edu.mit.coeus.utils.CoeusGuiConstants;
import edu.mit.coeus.utils.CoeusOptionPane;
import edu.mit.coeus.utils.TypeConstants;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyVetoException;
import java.beans.VetoableChangeListener;
import java.util.HashMap;
import java.util.Vector;

/**
 *
 * @author divyasusendran
 */
public class UnitUserRolesMaintenanceBaseWindow{
    
    private CoeusAppletMDIForm mdiForm;
    private CoeusSearch coeusSearch = null;
    private CoeusMessageResources coeusMessageResources;
    private static final char CHECK_USER_HAS_RIGHT_IN_HOME_UNIT = 'F';
    
    /** Creates a new instance of UnitUserRolesMaintenanceBaseWindow */
    public UnitUserRolesMaintenanceBaseWindow() {
        coeusMessageResources = CoeusMessageResources.getInstance();
        displayUserSearch();
        
    }   
    
    /**
     *Display User Search window after checking the Logged in User's rights
     */
    private void displayUserSearch() {
        try{
            if(coeusSearch == null){
                coeusSearch = new CoeusSearch(CoeusGuiConstants.getMDIForm(), "USERSEARCH",CoeusSearch.TWO_TABS);
            }
            coeusSearch.showSearchWindow();            
            HashMap hashMap = coeusSearch.getSelectedRow();
            if(hashMap == null){
                return ;
            }
            UserRolesController userRolesController = new UserRolesController();
            UserInfoBean userInfoBean = userRolesController.getUser(hashMap.get("USER_ID").toString());
            
            //check if the logged in user has MAINTAIN_USER_ROLES right in the HOME UNIT of the person searched
            String loggedInUser = CoeusGuiConstants.getMDIForm().getUserId();

            if(!checkForRights(userInfoBean,loggedInUser)){
                CoeusOptionPane.showInfoDialog(coeusMessageResources.parseMessageKey("propHierarchy_exceptionCode.1015"));// Logged in user has no right
                return;
            }else{
                // if checkForRights() is true , logged in user has MAINTAIN_USER_ROLES right in the HOME UNIT
                // display the newly created User Roles MaintenanceForm
                UnitUserRolesMaintenanceController unitUserRolesController = new UnitUserRolesMaintenanceController(userInfoBean,loggedInUser);
                unitUserRolesController.display();
            }
        }catch (Exception exception) {
            exception.printStackTrace();
        }
        
    }
    
    /**
     *Check if Logged in User has MAINTAIN_USER_ROLES right in HOME UNIT of searched user
     *@param userInfoBean
     *@param loggedInUser
     *@return hasRight is true if Logged in User has right
     *@exception  throws Exception
     */
    private boolean checkForRights(UserInfoBean userInfoBean,String loggedInUser) throws Exception{
        
        boolean hasRight = false;
        RequesterBean request = new RequesterBean();
        request.setFunctionType(CHECK_USER_HAS_RIGHT_IN_HOME_UNIT);
        Vector vecRequestObjects = new Vector();
        vecRequestObjects.add(loggedInUser);
        vecRequestObjects.add(userInfoBean);
        request.setDataObjects(vecRequestObjects);
        
        AppletServletCommunicator comm = new AppletServletCommunicator(CoeusGuiConstants.CONNECTION_URL
                + CoeusGuiConstants.USER_SERVLET, request);
        comm.send();
        ResponderBean response = comm.getResponse();
        if(!response.isSuccessfulResponse()){
            throw new Exception(response.getMessage());
        }else{
            if(response.getDataObject() != null){
                hasRight = new Boolean(response.getDataObject().toString()).booleanValue();
            }
        }
        return hasRight;
    }
    
}
