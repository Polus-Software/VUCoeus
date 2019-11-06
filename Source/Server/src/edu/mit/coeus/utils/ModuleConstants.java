/*
* @(#) ModuleConstants.java 1.0 07/28/2003 17:00:00
*
* Copyright (c) Massachusetts Institute of Technology
* 77 Massachusetts Avenue, Cambridge, MA 02139-4307
* All rights reserved.
*
*/

package edu.mit.coeus.utils;

/**
 * The interface used to hold constants of <code>Module codes in COEUS</code>
 * @author  Prasanna Kumar K
 * @version 1.0 July 28,2003
 */

public interface ModuleConstants {

    //Holds module code of Awards
    public static final int AWARD_MODULE_CODE = 1;
    //Holds module code of Institute Proposal
    public static final int PROPOSAL_INSTITUTE_MODULE_CODE = 2;
    //Holds module code of Development Proposal
    public static final int PROPOSAL_DEV_MODULE_CODE = 3;
    //Holds module code of Subcontracts
    public static final int SUBCONTRACTS_MODULE_CODE = 4;
    //Holds module code of Negotiations
    public static final int NEGOTIATIONS_MODULE_CODE = 5;
    //Holds module code of Person
    public static final int PERSON_MODULE_CODE = 6;
    //Holds module code of IRB - Protocol
    public static final int PROTOCOL_MODULE_CODE = 7;
    // Holds module code of IACUC Protocol
    public static final int IACUC_MODULE_CODE = 9;
     // Holds module code of Travel
    public static final int TRAVEL_MODULE_CODE = 0;
    /*
     *Development Proposal Sub Modules
     *
     */
    //Holds sub module code of Proposal Budget - Added with case 2158
    public static final int PROPOSAL_DEV_BUDGET_SUB_MODULE = 1;
    public static final String IACUC_MODULE_DESCRIPTION = "IACUC Protocol";
    public static final int  ANNUAL_COI_DISCLOSURE= 8;
    public static final int  ANNUAL_CERTIFY_DISCLOSURE= 3;
    //public static final int  ANNUAL_COI_DISCLOSURE_MODULE_CODE= 8;
    //Coi event type code
    public static final int COI_EVENT_AWARD = 1;
    public static final int COI_EVENT_PROPOSAL = 2;
    public static final int COI_EVENT_PROTOCOL = 3;
    public static final int COI_EVENT_IACUC = 4;
    public static final int COI_EVENT_ANNUAL = 5;
    public static final int COI_EVENT_REVISION = 6;
    public static final int COI_EVENT_OTHER = 7;
    public static final int COI_EVENT_TRAVEL = 8;
    //Coi event type code
 }
