/*
 * UserRoleNode.java
 *
 * Created on April 10, 2003, 8:26 PM
 */

package edu.mit.coeus.utils.tree;

import java.util.*;
import javax.swing.*;
import javax.swing.tree.*;

import edu.mit.coeus.bean.UserRolesInfoBean;
import edu.mit.coeus.bean.UserInfoBean;

/**
 *
 * @author  ravikanth
 */
public class UserRoleNode extends DefaultMutableTreeNode {
    
    UserRolesInfoBean data;
    /** Creates a new instance of UserRoleNode */
    public UserRoleNode() {
        super();
    }
    
    public UserRoleNode(UserRolesInfoBean userObject){
        super(userObject);
        this.data = userObject;
    }
    
    public void setDataObject(UserRolesInfoBean obj){
        this.data = obj;
    }
    
    public UserRolesInfoBean getDataObject(){
       return data;
    }
    
    public void add( UserRoleNode child) {
        if( getChildCount() > 0 ) {
            Enumeration enumChildren = children();
            int childIndex = 0;
            UserRoleNode oldChild;
            UserRolesInfoBean oldBean = null, childBean = null;
            UserInfoBean oldUserBean,childUserBean;
            childBean = (UserRolesInfoBean) child.getUserObject();
            if(childBean != null  && !childBean.isRole() ) {
                while ( enumChildren.hasMoreElements() ) {
                     oldChild = (UserRoleNode)enumChildren.nextElement();

                     oldBean = (UserRolesInfoBean)oldChild.getUserObject();
                     if(oldBean != null ){
                         oldUserBean = oldBean.getUserBean();
                         childUserBean = childBean.getUserBean();

                         if( oldUserBean.getUserId().equalsIgnoreCase(childUserBean.getUserId() ) ){
                             // duplicate node , so don't add.
                            return;
                         }else if( oldUserBean.getUserId().compareToIgnoreCase( 
                                childUserBean.getUserId() ) < 0 ) {
                             childIndex++;
                             continue;
                         }else{
                             break;
                         }
                     }
                }
                insert( child, childIndex );
            }else{
                super.add(child);
            }
        }else{
            super.add( child );
        }
    }
}
