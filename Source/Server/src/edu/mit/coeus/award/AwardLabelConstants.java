/*
 * AwardLabelConstants.java
 *
 * Created on May 18, 2005, 2:22 PM
 */

package edu.mit.coeus.award;

/**
 *
 * @author  geot
 */
public interface AwardLabelConstants {
    /**
     * It defines the terms
     */
	
    public static final String EQUIPMENT_APPROVAL = "Equipment Approval";
    public static final String INVENTION = "Invention";
    public static final String OTHER_REQUIREMENT = "Other Approval/Notification Requirement";
    public static final String PROPERTY = "Property";
    public static final String PUBLICATION = "Publication";
    public static final String REFERENCED_DOCUMENTS = "Referenced Documents";
    public static final String RIGHTS_IN_DATA = "Rights In Data";
    public static final String SUBCONTRACT_APPROVAL = "Subcontract Approval";
    public static final String TRAVEL = "Travel";
    
    //Addded code due to bug fix 1023
    public static final String CONTACT_TYPE = "Contact Type";
    public static final String PERSON_NAME = "Person Name";
    public static final String BEAN_BEFORE_MODIFICATION = "Bean Before Modification";
    public static final String BEAN_AFTER_MODIFICATION = "Bean After Modification";
    
    //Added for 2796:Sync to parent
    public static final String AWARD_STATUS = "Award Status";
    public static final String SPONSOR_CODE = "Sponsor Code";
    public static final String ADD_SYNC = "Add and Sync to Child Awards";
    public static final String MODIFY_SYNC = "Modify and Sync to Child Awards";
    public static final String DELETE_SYNC = "Delete and Sync to Child Awards";
    public static final String SYNC = "Sync to Child Awards";
}
