/*
 * AppointmentTypeBean.java
 *
 * Created on 13 July 2006, 16:20
 */

package edu.wmc.coeuslite.budget.bean;

import edu.mit.coeus.budget.bean.AppointmentsBean;

/**
 *
 * @author  mohann
 */
public class AppointmentTypeBean extends AppointmentsBean {
    
     //holds selected value
    private String selected;
    /** Creates a new instance of AppointmentTypeBean */
    public AppointmentTypeBean() {
    }
    
    
    /**
     *  Get Selected Id
     *  @return String Person Id
     */
    public String getSelected() {
        return selected;
    }
    /**
     *  Set Person Id
     *  @param personId String Person Id
     */
    public void setSelected(String selected) {
        this.selected = selected;
    }
    
    
}
