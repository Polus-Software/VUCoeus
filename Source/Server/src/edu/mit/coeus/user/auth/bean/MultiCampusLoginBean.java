/*
 * MultiCampusLoginBean.java
 *
 * Created on January 24, 2007, 4:06 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package edu.mit.coeus.user.auth.bean;

import edu.mit.coeus.bean.*;

/**
 *
 * @author geot
 */
public class MultiCampusLoginBean extends LoginBean{
    private String campusCode;

    /** Creates a new instance of MultiCampusLoginBean */
    public MultiCampusLoginBean(String userId,String password,String campus) {
        setUserId(userId);
        setPassword(password);
        setCampusCode(campus);
    }

    public String getCampusCode() {
        return campusCode;
    }

    public void setCampusCode(String campusCode) {
        this.campusCode = campusCode;
    }
    
}
