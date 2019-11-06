/*
 * RecordListener.java
 *
 * Created on July 15, 2003, 9:59 AM
 */

package edu.mit.coeus.user.gui;

import edu.mit.coeus.bean.UserInfoBean;

/**
 *
 * @author  senthilar
 */
public interface RecordListener {
    
    void recordCreated(UserInfoBean userinfoBean);
    
    void recordDeleted(UserInfoBean userinfoBean);
    
    void recordUpdated(UserInfoBean userinfoBean);
    
}
