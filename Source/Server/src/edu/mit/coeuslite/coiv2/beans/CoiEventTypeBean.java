/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package edu.mit.coeuslite.coiv2.beans;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;

/**
 *
 * @author indhulekha
 */
public class CoiEventTypeBean extends org.apache.struts.action.ActionForm {
    
    private String description;

    private int eventType;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getEventType() {
        return eventType;
    }

    public void setEventType(int eventType) {
        this.eventType = eventType;
    }

   

    /**
     *
     */
    public CoiEventTypeBean() {
        super();
        // TODO Auto-generated constructor stub
    }

}
