/**
 * @(#)LookUpWindowConstants.java  1.0  
 *
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 */
package edu.mit.coeus.departmental.gui;

/**
 * This interface contains all the constant values which are used in the 
 * Departmental module.
 * @author  Raghunath P.V.
 * @version 1.0
 */
public interface LookUpWindowConstants {
    
    // Constant used for Person Search in Others tab
    public final static String PERSON_LOOKUP_WINDOW = "w_Person_Select";
    // Constant used for Cost Element Search in Others tab
    public final static String COST_ELEMENT_LOOKUP_WINDOW = "w_select_cost_element";
    // Constant used for Person Search in Unit tab
    public final static String UNIT_LOOKUP_WINDOW = "w_Unit_Select";
    // Constant used for Person Search in Rolodex tab
    public final static String ROLODEX_LOOKUP_WINDOW = "w_Rolodex_Select";
    // Constant used for Code, Description Search in Others tab
    public final static String CODE_LOOKUP_WINDOW = "w_arg_Code_tbl";
    // Constant used for Value, Description Search in Others tab
    public final static String VALUE_LOOKUP_WINDOW = "w_arg_Value_list";
    //Constant used for displaying column names for Code, Description Lookup Window
    public final static String[] CODE_COLUMN_NAMES = {"Code", "Description"};
    //Constant used for displaying column names for Unit Selection Lookup Window
    public final static String[] UNIT_COLUMN_NAMES = {"Unit Number", "Unit Name"};
    //Constant used for displaying column names for Value, Description Lookup Window
    public final static String[] VALUE_COLUMN_NAMES = {"Value", "Description"};
    //Parameter to the Coeus Search to display the search window.
    public final static String PERSON_SEARCH = "personSearch";
    //Parameter to the Coeus Search to display the search window.
    public final static String ROLODEX_SEARCH = "rolodexSearch";
    //Parameter to the Coeus Search to display the search window.
    public final static String UNIT_SEARCH = "LEADUNITSEARCH";
    
    // 4580: Add organization and sponsor search in custom elements - Start
    public final static String ORGANIZATION_SEARCH = "w_organization_select";
    public final static String SPONSOR_SEARCH = "w_sponsor_select";
    // 4580: Add organization and sponsor search in custom elements - End
    
    // COEUSQA-2685_IACUC - comm member areas of research should point to IACUC areas of research_start
    public static final String[] COMMITEE_TYPES = {"Code","Committee Type"};    
    // COEUSQA-2685_IACUC - comm member areas of research should point to IACUC areas of research_end
    
    // Added for COEUSQA-2520: Data Override functionality in Coeus Proposal Development - Start
    public static final String AWARD_LOOKUP_WINDOW = "w_award_select";
    public static final String PROPOSAL_DEV_LOOKUP_WINDOW = "w_proposal_dev_select";
    public static final String AWARD_SEARCH = "AWARDSEARCH";
    public static final String PROPOSAL_DEV_SEARCH = "PROPOSALDEVSEARCH";
            
    // Added for COEUSQA-2520: Data Override functionality in Coeus Proposal Development - End
}
