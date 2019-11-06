/*
 * IrbWindowConstants.java
 *
 * Created on June 1, 2007, 11:13 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package edu.mit.coeus.iacuc.bean;

import edu.mit.coeus.iacuc.bean.*;

/**
 *
 * @author noorula
 */
public interface IrbWindowConstants {
    
    public static final String GENERAL_INFO = "AC001";
    public static final String ORGANIZATION = "AC017";    
    public static final String INVESTIGATOR = "AC002";
    public static final String KEY_STUDY_PERSONS = "AC003";
    public static final String CORRESPONDENTS = "AC009";    
    public static final String AREA_OF_RESEARCH = "AC004";
    public static final String SPECIAL_REVIEW = "AC007";
    public static final String FUNDING_SOURCE = "AC005";
    public static final String SUBJECTS = "AC006";
    public static final String UPLOAD_DOCUMENTS = "AC008";
    public static final String NOTES = "AC015";
    public static final String IDENTIFIERS = "AC016";
    public static final String ROLES = "AC018"; 
    public static final String OTHERS = "AC023";
    public static final String UPLOAD_OTHER_DOCUMENTS = "AC024";
    //Modified with CoeusQA-2551-Rework how user enters species,study groups and procedures in IACUC protocols
//    public static final String SPECIES = "AC032";
//    public static final String STUDY_GROUP = "AC034";
    public static final String SPECIES_STUDY_GROUP = "AC032";
    //CoeusQA:2551 - End
    public static final String SCIENTIFIC_JUSTIFICATION = "AC035";
    public static final String ALTERNATIVE_SEARCH = "AC036";
    //Added for COEUSDEV-86 : Questionnaire for a Submission - Start
    public static final String GENERAL_INFO_LABEL = "General Info";
    public static final String ORGANIZATION_LABEL = "Protocol Organizations";
    public static final String INVESTIGATOR_LABEL = "Protocol Investigators";
    public static final String KEY_STUDY_PERSONS_LABEL = "Study Personnel";
    public static final String CORRESPONDENTS_LABEL = "Protocol Correspondents";
    public static final String AREA_OF_RESEARCH_LABEL = "Areas of Research";
    public static final String SPECIAL_REVIEW_LABEL = "Special Review";
    public static final String FUNDING_SOURCE_LABEL = "Funding Source";
    public static final String SUBJECTS_LABEL = "Subjects";
    public static final String UPLOAD_DOCUMENTS_LABEL = "Attachments";
    public static final String NOTES_LABEL = "Protocol Notes";
    public static final String IDENTIFIERS_LABEL = "Protocol Identifiers";
    public static final String ROLES_LABEL = "Protocol Roles";
    public static final String OTHERS_LABEL = "Others";
    public static final String UPLOAD_OTHER_DOCUMENTS_LABEL = "Other Attachments";
    //Modified with CoeusQA-2551-Rework how user enters species,study groups and procedures in IACUC protocols
//    public static final String SPECIES_LABEL = "Species";
//    public static final String STUDY_GROUP_LABEL = "Study Group";
    public static final String SPECIES_STUDY_GROUP_LABEL = "Species/Procedures";
    //CoeusQA-2551 End
    public static final String SCIENTIFIC_JUSTIFICATION_LABEL = "Scientific Justification";
    public static final String ALTERNATIVE_SEARCH_LABEL = "Alternative Search";
    //Added for COEUSDEV-86 : Questionnaire for a Submission - End
    
}
