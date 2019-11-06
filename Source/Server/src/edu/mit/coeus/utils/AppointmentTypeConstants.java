/*
* @(#) AppointmentTypesConstants.java	1.0 10/17/2003 18:24:19
*
* Copyright (c) Massachusetts Institute of Technology
* 77 Massachusetts Avenue, Cambridge, MA 02139-4307
* All rights reserved.
*
*/

package edu.mit.coeus.utils;

import edu.mit.coeus.budget.bean.BudgetDataTxnBean;
import edu.mit.coeus.exception.CoeusException;
import edu.mit.coeus.utils.dbengine.DBException;
import java.util.HashMap;


/**
 * <code>AppointmentTypesConstants</code>  is a constants defined class for the Coeus application.
 * Defines all the AppointmentTypes and a hashmap containing its values.
 * @author Sagin
 * @version 1.0 October17,2003
 */ 

public final class AppointmentTypeConstants {

    /**
     * The session scope attribute under which the currently logged in user is stored.
     */
    public static String TMP_EMPLOYEE = "TMP EMPLOYEE";
    public static String SUM_EMPLOYEE = "SUM EMPLOYEE";
    public static String DURATION_9M = "9M DURATION";
    public static String DURATION_10M = "10M DURATION";
    public static String DURATION_11M = "11M DURATION";
    public static String EMPLOYEE_12M = "12M EMPLOYEE";
    public static String REG_EMPLOYEE = "REG EMPLOYEE";
    
    public static HashMap appointmentTypes = new HashMap();
    static{
        //COEUSQA-2036 Code Table Prop Dev Appt Type and Period Types - Start
//        appointmentTypes.put(AppointmentTypeConstants.TMP_EMPLOYEE, new Integer(1));
//        appointmentTypes.put(AppointmentTypeConstants.SUM_EMPLOYEE, new Integer(3));
//        appointmentTypes.put(AppointmentTypeConstants.DURATION_9M, new Integer(9));
//        appointmentTypes.put(AppointmentTypeConstants.DURATION_10M, new Integer(10));
//        appointmentTypes.put(AppointmentTypeConstants.DURATION_11M, new Integer(11));
//        appointmentTypes.put(AppointmentTypeConstants.EMPLOYEE_12M, new Integer(12));
//        appointmentTypes.put(AppointmentTypeConstants.REG_EMPLOYEE, new Integer(12));
        fetchAppointmentTypes();
        //COEUSQA-2036 Code Table Prop Dev Appt Type and Period Types - End
    }
    
    //COEUSQA-2036 Code Table Prop Dev Appt Type and Period Types - Start
    /**
     * Fetch all the appointment types from the database
     */
    public static void fetchAppointmentTypes(){
        BudgetDataTxnBean budgetTxnBean = new BudgetDataTxnBean();
        try {
            appointmentTypes = budgetTxnBean.getAppointmentTypeValues();
        } catch (DBException ex) {
            ex.printStackTrace();
        } catch (CoeusException ex) {
            ex.printStackTrace();
        }
    }
    //COEUSQA-2036 Code Table Prop Dev Appt Type and Period Types - End
}
