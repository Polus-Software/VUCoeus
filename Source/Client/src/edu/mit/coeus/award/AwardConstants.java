/*
 * @(#) AwardConstants.java	1.0 05/05/2004 18:24:19
 *
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 *
 */

package edu.mit.coeus.award;

import edu.mit.coeus.utils.KeyConstants;
import java.util.HashMap;


/**
 * AwardConstants is defined certain constants only for the Award module.
 * Defines all the Award Terms and a hashmap containing its values.
 * @author jobinelias
 * @version 1.0 May 05, 2004
 */

public final class AwardConstants implements AwardLabelConstants{
    
    
    
    public static HashMap awardTerms = new HashMap();
    public static HashMap awardFields = new HashMap();
    static{
        awardTerms.put(EQUIPMENT_APPROVAL, KeyConstants.EQUIPMENT_APPROVAL_TERMS);
        awardTerms.put(INVENTION, KeyConstants.INVENTION_TERMS);
        awardTerms.put(OTHER_REQUIREMENT, KeyConstants.PRIOR_APPROVAL_TERMS);
        awardTerms.put(PROPERTY, KeyConstants.PROPERTY_TERMS);
        awardTerms.put(PUBLICATION, KeyConstants.PUBLICATION_TERMS);
        awardTerms.put(REFERENCED_DOCUMENTS, KeyConstants.REFERENCED_DOCUMENT_TERMS);
        awardTerms.put(RIGHTS_IN_DATA, KeyConstants.RIGHTS_IN_DATA_TERMS);
        awardTerms.put(SUBCONTRACT_APPROVAL, KeyConstants.SUBCONTRACT_APPROVAL_TERMS);
        awardTerms.put(TRAVEL, KeyConstants.TRAVEL_RESTRICTION_TERMS);
        
        awardFields.put(KeyConstants.AWARD_STATUS,AWARD_STATUS);
        awardFields.put(KeyConstants.SPONSOR_CODE,SPONSOR_CODE);
    }
    /* Available Sync Targets */
    public static final String SYNC_ALL_CHILDREN    = "C";
    public static final String SYNC_ACTIVE_CHILDREN = "A";
    /*Available Sync Modes */
    public static final char ADD_SYNC    = 'A';
    public static final char MODIFY_SYNC = 'M';
    public static final char DELETE_SYNC = 'D';
    public static final char SYNC = 'S';
    
    //Added for COEUSDEV-416 : Award Sync to Children - Display proper error message when not syncing because the award is not saved
    public static final String INVESTIGATOR_SYNC = "INVESTIGATOR_SYNC";
    public static final String CONTACTS_SYNC = "CONTACTS_SYNC";
    public static final String REPORTS_SYNC = "REPORTS_SYNC";
    public static final String TERMS_SYNC = "TERMS_SYNC";
    public static final String COMMENTS_SYNC = "COMMENTS_SYNC";
    public static final String DETAIL_SYNC = "DETAIL_SYNC";
    //COEUSDEV-416 : End
}
