/*
 * CustomizeMaintRepReq.java
 *
 * Created on August 6, 2004, 2:10 PM
 */

package edu.mit.coeus.award.gui;

/**
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 * @author  sharathk
 */
public class CustomizeMaintRepReqForm extends CustomizeRepReqForm{
    
    public static final String AWARD_NO = "Award No";
    public static final String PRINCIPAL_INVESTIGATOR = "Principal Investigator";
    public static final String UNIT_NUMBER = "Unit No";
    public static final String UNIT_NAME = "Unit Name";
    public static final String REPORT_CLASS = "Report Class";
    public static final String COPY_OSP = "Copy OSP";
    public static final String CONTACT = "Contact";
    public static final String ADDRESS = "Address";
    public static final String SPONSOR_CODE = "Sponsor Code";
    public static final String SPONSOR_NAME = "Sponsor Name";
    public static final String SPONSOR_AWD_NUM = "Sponsor Award Number";
    public static final String TITLE = "Title";
        
    /** Creates a new instance of CustomizeMaintRepReq */
    public CustomizeMaintRepReqForm() {
    }
    
    protected void initData() {
        //Commented for case#2268 - Report Tracking Functionality - start
        strAll = new String[]{AWARD_NO, PRINCIPAL_INVESTIGATOR, UNIT_NUMBER, UNIT_NAME,
        REPORT_CLASS, REPORT_TYPE, FREQUENCY, FREQUENCY_BASE, BASE_DATE, STATUS,
        DUE_DATE, COPY_OSP, /*CONTACT, ADDRESS, COPIES,*/ OVERDUE_COUNTER, ACTIVITY_DATE,
        COMMENTS, PERSON, SPONSOR_CODE, SPONSOR_NAME, SPONSOR_AWD_NUM, TITLE};
        
        strGroups = new String[]{PRINCIPAL_INVESTIGATOR, REPORT_CLASS, REPORT_TYPE, 
        FREQUENCY, FREQUENCY_BASE, BASE_DATE, COPY_OSP};
        
        strDetail = new String[]{AWARD_NO, UNIT_NUMBER, UNIT_NAME, STATUS, DUE_DATE, 
        /*CONTACT, ADDRESS, COPIES,*/ OVERDUE_COUNTER, ACTIVITY_DATE, COMMENTS, PERSON};
        //Commented for case#2268 - Report Tracking Functionality - end
    }
    
    protected void postInitComponents() {
        super.postInitComponents();
        btnDefault.setVisible(false);
    }
    
     public static void main(String s[]) {
        CustomizeMaintRepReqForm cutomizeRepReq = new CustomizeMaintRepReqForm();
        int sel = cutomizeRepReq.display();
        if(sel == OK_CLICKED) {
            //System.out.println("OK");
        }else if(sel == CANCEL_CLICKED){
            //System.out.println("Cancel");
        }
        
    }
     
}
