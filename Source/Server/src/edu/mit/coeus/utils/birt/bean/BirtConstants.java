/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package edu.mit.coeus.utils.birt.bean;

/**
 *
 * @author sharathk
 */
public interface BirtConstants {
    //Report types
    public static final String PDF = "PDF";
    public static final String EXCEL = "Excel";
    public static final String HTML = "HTML";
    //Control types
    public static final int TYPE_TEXT = 1;
    public static final int TYPE_COMBO_BOX = 2;
    public static final int TYPE_RADIO = 3;

    //Data Types
    public static final int STRING = 5;
    public static final int FLOAT = 6;
    public static final int DECIMAL = 7;
    public static final int DATE = 8;
    public static final int DATE_TIME = 9;
    public static final int BOOLEAN = 10;

    public static final String READER_CLASS = "edu.mit.coeus.utils.birt.BirtDocumentReader";
    public static final String GLOBAL_MENU_CLASS = "edu.mit.coeus.gui.CoeusAppletMDIForm";

    public static final String RIGHTS = "RIGHTS";
    public static final String REPORT_TYPES = "REPORT_TYPES";


    public static final String REPORT_BEAN = "REPORT_BEAN";
    public static final String REPORT_ID = "REPORT_ID";
    public static final String REPORT_TYPE = "REPORT_TYPE";
    public static final String PARAMETERS = "PARAMETERS";
    public static final String BASE_WINDOW = "BASE_WINDOW";

    //Servlet Constants
    public static final char GET_REPORT_PARAMS = 'P';
    public static final char GET_ALL_REPORTS = 'R';
    public static final char NEW_REPORT = 'N';
    public static final char EDIT_REPORT = 'E';
    public static final char VIEW_REPORT = 'V' ;
    public static final char DELETE_REPORT = 'D';
    public static final char SAVE_REPORT_DETAILS = 'S';
    public static final char SAVE_REPORT_TEMPLATE = 'T';
    public static final char GET_REPORTS_FOR_MODULE = 'M';
    public static final char GET_REPORT_TYPES = 'G';
    public static final char HAS_MAINTAIN_RIGHT = 'A';
    public static final char DOWNLOAD_REPORT = 'W';
}
