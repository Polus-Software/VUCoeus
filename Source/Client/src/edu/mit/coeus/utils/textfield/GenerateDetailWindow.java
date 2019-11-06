/*
 * @(#)GenerateDetailWindow.java
 *
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 *
 * Created on March 7, 2003, 12:02 PM
 */

package edu.mit.coeus.utils.textfield;

import edu.mit.coeus.rolodexmaint.gui.RolodexMaintenanceDetailForm;
import edu.mit.coeus.sponsormaint.gui.SponsorMaintenanceForm;
import edu.mit.coeus.organization.gui.DetailForm;
import edu.mit.coeus.unit.gui.UnitDetailForm;

import edu.mit.coeus.utils.CoeusGuiConstants;
import edu.mit.coeus.utils.AppletServletCommunicator;
import edu.mit.coeus.brokers.*;



/**
 * GenerateDetailWindow is a class which contains methods to show the details of
 * Rolodex, Sponsor, Organization etc modules for the specified id.
 * @author  ravikanth
 */
public class GenerateDetailWindow {
    
    /** Creates a new instance of GenerateDetailWindow */
    public GenerateDetailWindow() {
    }
    
    /**
     * Method used to show the Rolodex details for the given rolodexId.
     * @param rolodexId String representing the id, whose details will be shown.
     */
    public void showRolodexDetails ( String rolodexId ) {
        if ( rolodexId != null && rolodexId.trim().length() > 0 ) {
            RolodexMaintenanceDetailForm frmRolodex 
                = new RolodexMaintenanceDetailForm('V',rolodexId);
            frmRolodex.showForm(CoeusGuiConstants.getMDIForm(),
                CoeusGuiConstants.TITLE_ROLODEX,true);
        }    
    }
    
    /**
     * Method used to show the Sponsor details for the given sponsorId.
     * @param sponsorId String representing the id, whose details will be shown.
     */
    public void showSponsorDetails ( String sponsorId ) {
        if ( sponsorId != null && sponsorId.trim().length() > 0 ) {
            SponsorMaintenanceForm frmSponsor 
                    = new SponsorMaintenanceForm('D',sponsorId);
            frmSponsor.showForm(CoeusGuiConstants.getMDIForm(),
                CoeusGuiConstants.SPONSOR_FRAME_TITLE,true);
        }    
    }

    /**
     * Method used to show the Unit details for the given unitId.
     * @param unitId String representing the id, whose details will be shown.
     * @throws Exception when unable to load the Unit detail window for the
     * specified id.
     */
    public void showUnitDetails ( String unitId ) throws Exception {
        if ( unitId != null && unitId.trim().length() > 0 ) {
            UnitDetailForm frmUnit = new UnitDetailForm(unitId,'G');
            frmUnit.showUnitForm(CoeusGuiConstants.getMDIForm());
        }    
    }
    
    /**
     * Method used to show the Organization details for the given organizationId.
     * @param organizationId String representing the id, whose details will be shown.
     * @throws Exception when unable to load the Organization detail window for the
     * specified id.
     */
    public void showOrganizationDetails ( String organizationId ) throws Exception {
        if ( organizationId != null && organizationId.trim().length() > 0 ) {
            DetailForm frmOrgDetailForm = new DetailForm('D', organizationId);
            frmOrgDetailForm.showDialogForm(CoeusGuiConstants.getMDIForm());
        }    
    }
    /**
     * Method used to show the Protocol details for the given protocolId.
     * @param protocolId String representing the id, whose details will be shown.
     * @throws Exception when unable to load the Protocol detail window for the
     * specified id.
     */
    public void showProtocolDetails ( String protocolId ) throws Exception {
        if ( protocolId != null && protocolId.trim().length() > 0 ) {
        }    
    }

    /**
     * Method used to show Person details for the given personId.
     * @param personId String representing the id, whose details will be shown.
     * @throws Exception when unable to load the Person detail window for the
     * specified id.
     */
    public void showPersonDetails ( String personId ) throws Exception {
        if ( personId != null && personId.trim().length() > 0 ) {
        }    
    } 
    
    /**
     * Method used to show Award details for the given awardId.
     * @param awardId String representing the id, whose details will be shown.
     * @throws Exception when unable to load the Award detail window for the
     * specified id.
     */
    public void showAwardDetails ( String awardId ) throws Exception {
        if ( awardId != null && awardId.trim().length() > 0 ) {
        }    
    } 
    
    /**
     * Method used to show User details for the given userId.
     * @param userId String representing the id, whose details will be shown.
     * @throws Exception when unable to load the User detail window for the
     * specified id.
     */
    public void showUserDetails ( String userId ) throws Exception {
        if ( userId != null && userId.trim().length() > 0 ) {
        }    
    }     
    
    /**
     * Method used to validate the given id in the database by connecting to the
     * server using the servlet specified.
     * @param detailId String representing the id, whose validity should be checked
     * @param servletName String rerpesenting the name of the servlet which will be 
     * used to connect to the server.
     * @param requestType character which specifies the action to be performed in the 
     * servlet to validate the id.
     * @return boolean true if the given id is valid else false.
     */
    public boolean validate(String detailId, String servletName, char requestType ){
       return validate(detailId,servletName, null, requestType);
    }
    
    /**
     * Method used to validate the given id in the database by connecting to the
     * server using the servlet specified.
     * @param detailId String representing the id, whose validity should be checked
     * @param servletName String rerpesenting the name of the servlet which will be 
     * used to connect to the server.
     * @param functionName specifies the name of the function to be executed in the servlet.
     * @param requestType character which specifies the action to be performed in the 
     * servlet to validate the id.
     * @return boolean true if the given id is valid else false.
     */
    public boolean validate(String detailId, String servletName, String functionName, char requestType){
        RequesterBean requester = new RequesterBean();
        requester.setFunctionType(requestType);
        requester.setId( detailId );
        if( functionName != null ){
            requester.setDataObject(functionName);
        }
        String connectTo = CoeusGuiConstants.CONNECTION_URL + "/" + servletName;
        AppletServletCommunicator comm
        = new AppletServletCommunicator(connectTo, requester);
        comm.send();
        ResponderBean response = comm.getResponse();
        if ( response!=null ){
            return response.isSuccessfulResponse();
        }
        
        return false;    
    
    }
}
