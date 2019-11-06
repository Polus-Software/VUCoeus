/*
 * BudgetSubAwardConstants.java
 *
 * Created on May 19, 2006, 2:07 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package edu.mit.coeus.budget;

/**
 *
 * @author sharathk
 */
public interface BudgetSubAwardConstants
{

    public static final char GET_BUDGET_SUB_AWARD = 'A';
    public static final char SAVE_BUDGET_SUB_AWARD = 'B';
    public static final char GET_PDF = 'C';
    public static final char GET_XML = 'D';
    public static final char TRANSLATE = 'T';
    //COEUSQA-2735 Cost sharing distribution for Sub awards - Start
    public static final char GET_SUB_AWARD_COST_SHARING = 'E';
    //COEUSQA-2735 Cost sharing distribution for Sub awards - End
    // Added for COEUSQA-2115 Subaward budgeting for Proposal Development - Start
    public static final char SYNC_XML_FOR_SUB_AWARD_DETAILS = 'X';
    // Added for COEUSQA-2115 Subaward budgeting for Proposal Development - End
    public static final String PDF = "pdf";
    public static final String XML = "xml";
    public static final String ATTACHMENT = "ATTACHMENT";
    public static final String XML_GENERATED_SUCCESSFULLY = "XML Generated successfully";
    public static final String COULD_NOT_DETERMINE_DOC_TYPE = "Could Not Determine Document Type";
    public static final String COULD_NOT_EXTRACT_XML_FROM_PDF = "Could Not Extract XML From PDF";
    //public static final String TRANSLATION_COMMENTS = "TC";
    public static final String TIMESTAMP = "TS";
    public static final String CONTENT_ID = "CONTENT_ID";
    
    public static final String PROPOSAL_NUMBER = "propNum";
    public static final String VERSION_NUMBER = "version";
    public static final String SUB_AWARD_NUM = "subAwd";
    public static final String FILE = "file";
    // Added for COEUSQA-2115 Subaward budgeting for Proposal Development - Start
    public static final String SUBCONTRACTOR_F_AND_A_LT_25K = "SUBCONTRACTOR_F_AND_A_LT_25K";
    public static final String SUBCONTRACTOR_F_AND_A_GT_25K = "SUBCONTRACTOR_F_AND_A_GT_25K";
    public static final String SUBCONTRACTOR_DIRECT_F_AND_A_LT_25K = "SUBCONTRACTOR_DIRECT_F_AND_A_LT_25K";
    public static final String SUBCONTRACTOR_DIRECT_F_AND_A_GT_25K = "SUBCONTRACTOR_DIRECT_F_AND_A_GT_25K";
    // Added for COEUSQA-2115 Subaward budgeting for Proposal Development - End
    
    // JM 6-25-2013 added for organizations in subaward
    public static final char GET_PROP_ORGS_FOR_SUB = 'O';
    // JM END
}