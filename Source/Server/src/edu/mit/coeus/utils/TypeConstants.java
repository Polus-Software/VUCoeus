/*
 * FunctionTypeConstants.java
 *
 * Created on March 18, 2003, 6:11 PM
 */

package edu.mit.coeus.utils;

/**
 *
 * @author  Raghunath
 */
public interface TypeConstants {

    //function type specifies Modify mode
    public static final char MODIFY_MODE = 'M';
    //function type specifies Add mode
    public static final char ADD_MODE = 'A';
    //function type specifies Display mode
    public static final char DISPLAY_MODE = 'D';
    //function type specifies Amend mode
    public static final char AMEND_MODE = 'E';

    //function type specifies Copy mode
    public static final char COPY_MODE = 'C';

    //function type specifies New mode
    public static final char NEW_MODE = 'N';
    
    //Award Budget Enhancment 
    //function type specifies New mode
    public static final char REBUDGET_MODE = 'R';
    
    // Type Constant specifies Save record 
    public static final char SAVE_RECORD = 'S';

    
    
    // AcType Constant specifies Insert 
    public static final String INSERT_RECORD = "I";
    // AcType Constant specifies update 
    public static final String UPDATE_RECORD = "U";
    // AcType Constant specifies Delete 
    public static final String DELETE_RECORD = "D";
    // No record Constant specifies 0
    public static final int NO_RECORD = 0;
    
  
    
    
    //Stores the RIGHT ID which is required for checking user rights to create new proposal
    public static final String CREATE_RIGHT="CREATE_PROPOSAL";
    
    //Stores the RIGHT ID which is required for checking user rights to modify proposal
    public static final String MODIFY_RIGHT="MODIFY_PROPOSAL";
    
    //Stores the RIGHT ID which is required for checking user rights to view proposal
    public static final String VIEW_RIGHT="VIEW_PROPOSAL";  

    /**
    *Stores the RIGHT ID which is required for checking user rights to modify narrative
     */
    public static final String MODIFY_NARRATIVE_RIGHT="MODIFY_NARRATIVE";
    //Added for getting JOB_CODE Validation from parameter table  start
     public static final String JOB_CODE_VALIDATION_ENABLED = "JOBCODE_VALIDATION_ENABLED";
     //Added for getting JOB_CODE Validation from parameter table end
    
}
