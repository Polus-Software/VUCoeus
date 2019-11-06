/*
 * @(#)UserRoleNodeRenderer.java
 *
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 *
 * Created on April 10, 2003, 9:02 PM
 */

package edu.mit.coeus.utils.tree;

import java.util.*;
import java.awt.*;
import javax.swing.*;
import javax.swing.tree.*;

import edu.mit.coeus.utils.CoeusGuiConstants;
import edu.mit.coeus.bean.UserRolesInfoBean;
import edu.mit.coeus.bean.UserInfoBean;
import edu.mit.coeus.bean.RoleInfoBean;

/**
 * This class is used as renderer component in UserRolesTree. Icon of the tree nodes
 * changes depending on the type of the node. Different icons are available for
 * active /inactive role node, active/ inactive user node.
 */
public class UserRoleNodeRenderer extends DefaultTreeCellRenderer {
    
    ImageIcon activeRoleIcon, inactiveRoleIcon, activeUserIcon, inactiveUserIcon;
    ImageIcon activeAdminRole,inactiveAdminRole,activePropRole,inactivePropRole,
    activeSysRole, inactiveSysRole;
    ImageIcon activeDescend,inactiveDescend,iacucActiveIcon,iacucInActiveIcon;
    private static final char IACUC_RIGHT_TYPE = 'I';
    private boolean showDescend;
    
    public UserRoleNodeRenderer(boolean showDescend){
        this();
        this.showDescend = showDescend;
    }
    /**
     * Creates new UserRoleNodeRenderer
     */
    public UserRoleNodeRenderer(){
        super();
    /* try to read from the URL, if unable to read , take the default values.
     * If UserRoleTree is used as a component in Forte Form editor, it can't read
     * from URL, so then it will take the icons from the strings given and
     * bring up the component.
     */
        String actRole = "/images/rolepa.gif";
        String inactRole = "/images/rolepi.gif";
        String actUser = "/images/usera.gif";
        String inactUser = "/images/useri.gif";
        
        String actAdminRole = "/adminrolea.gif";
        String inactAdminRole = "/adminrolei.gif";
        String actPropRole = "/proprolepa.gif";
        String inactPropRole = "/proprolepi.gif";
        String actSysRole = "/systemrolea.gif";
        String inactSysRole = "/systemrolei.gif";
        
        String actDescend = "/descendy.gif";
        String inactDescend = "/descendn.gif";

        
        java.net.URL aRole = getClass().getClassLoader().getResource( CoeusGuiConstants.ACTIVE_ROLE_ICON );
        java.net.URL inaRole = getClass().getClassLoader().getResource( CoeusGuiConstants.INACTIVE_ROLE_ICON );
        
        java.net.URL aUser = getClass().getClassLoader().getResource( CoeusGuiConstants.ACTIVE_USER_ICON );
        java.net.URL inaUser = getClass().getClassLoader().getResource( CoeusGuiConstants.INACTIVE_USER_ICON );
        
        java.net.URL aAdminRole = getClass().getClassLoader().getResource( CoeusGuiConstants.ADMIN_ACTIVE_ROLE_ICON );
        java.net.URL inaAdminRole = getClass().getClassLoader().getResource( CoeusGuiConstants.ADMIN_INACTIVE_ROLE_ICON );
        
        java.net.URL aPropRole = getClass().getClassLoader().getResource( CoeusGuiConstants.PROP_ACTIVE_ROLE_ICON );
        java.net.URL inaPropRole = getClass().getClassLoader().getResource( CoeusGuiConstants.PROP_INACTIVE_ROLE_ICON );
        
        java.net.URL aSysRole = getClass().getClassLoader().getResource( CoeusGuiConstants.SYSTEM_ACTIVE_ROLE_ICON );
        java.net.URL inaSysRole = getClass().getClassLoader().getResource( CoeusGuiConstants.SYSTEM_INACTIVE_ROLE_ICON );
        
        java.net.URL aDescend = getClass().getClassLoader().getResource( CoeusGuiConstants.DESCEND_YES_ICON );
        java.net.URL inaDescend = getClass().getClassLoader().getResource( CoeusGuiConstants.DESCEND_NO_ICON );
        
        //Added for IACUC Changes - Start
        java.net.URL iacucActiveRole = getClass().getClassLoader().getResource( CoeusGuiConstants.IACUC_ACTIVE_ROLE_ICON);
        java.net.URL iacucInACtiveRole = getClass().getClassLoader().getResource( CoeusGuiConstants.IACUC_IN_ACTIVE_ROLE_ICON);
        //IACUC Changes - End
        
        
        if( aRole != null ) {
            activeRoleIcon = new ImageIcon( aRole );
        }else {
            activeRoleIcon = new ImageIcon( actRole );
        }
        if( inaRole != null ) {
            inactiveRoleIcon = new ImageIcon( inaRole );
        }else {
            inactiveRoleIcon = new ImageIcon( inactRole );
        }
        
        //Added for IACUC Changes - Start
        if(aDescend!=null){
            iacucActiveIcon = new ImageIcon(iacucActiveRole);
        }else{
            iacucActiveIcon = new ImageIcon(iacucActiveRole);
        }
        if(inaDescend!=null){
            iacucInActiveIcon = new ImageIcon(iacucInACtiveRole);
        }else{
            iacucInActiveIcon = new ImageIcon(iacucInACtiveRole);
        }
        //IACUC Changes - End
        
        if( aUser != null ) {
            activeUserIcon = new ImageIcon( aUser );
        }else {
            activeUserIcon = new ImageIcon( actUser );
        }
        if( inaUser != null ) {
            inactiveUserIcon = new ImageIcon( inaUser );
        }else {
            inactiveUserIcon = new ImageIcon( inactUser );
        }
        
        if(aAdminRole!=null){
            activeAdminRole = new ImageIcon(aAdminRole);
        }else{
            activeAdminRole = new ImageIcon(actAdminRole);
        }
        if(inaAdminRole!=null){
            inactiveAdminRole = new ImageIcon(inaAdminRole);
        }else{
            inactiveAdminRole = new ImageIcon(inactAdminRole);
        }
        if(aPropRole!=null){
            activePropRole = new ImageIcon(aPropRole);
        }else{
            activePropRole = new ImageIcon(actPropRole);
        }
        if(inaPropRole!=null){
            inactivePropRole = new ImageIcon(inaPropRole);
        }else{
            inactivePropRole = new ImageIcon(inactPropRole);
        }
        if(aSysRole!=null){
            activeSysRole = new ImageIcon(aSysRole);
        }else{
            activeSysRole = new ImageIcon(actSysRole);
        }
        if(inaSysRole!=null){
            inactiveSysRole = new ImageIcon(inaSysRole);
        }else{
            inactiveSysRole = new ImageIcon(inactSysRole);
        }
        if(aDescend!=null){
            activeDescend = new ImageIcon(aDescend);
        }else{
            activeDescend = new ImageIcon(actDescend);
        }
        if(inaDescend!=null){
            inactiveDescend = new ImageIcon(inaDescend);
        }else{
            inactiveDescend = new ImageIcon(inactDescend);
        }
        

        
    }
    
    public Component getTreeCellRendererComponent(JTree tree, Object value,
    boolean sel, boolean expanded, boolean leaf,
    int row, boolean hasFocus) {
        
        JPanel pnlRolesPanel = new JPanel(new FlowLayout(FlowLayout.LEFT,0,0));
        JLabel lblDescend = new JLabel();
        JLabel lblRole = new JLabel();
        pnlRolesPanel.add(lblRole);
        pnlRolesPanel.add(lblDescend);
        String nodeValue = "";
        String userId = "";
        String userName = "";
        String user = "";
        super.getTreeCellRendererComponent(tree, value,
        sel, expanded, leaf, row, hasFocus);
        UserRolesInfoBean bean = ((UserRoleNode)value).getDataObject();
        setBackgroundNonSelectionColor(
        (Color) UIManager.getDefaults().get("Panel.background"));
        if( bean != null ){
            if(bean.isRole()){
                
                /* node is of type role , check for role status */
                RoleInfoBean roleBean = bean.getRoleBean();
                nodeValue = roleBean.getRoleName();
                
                
                switch ( roleBean.getStatus()){
                    case 'A' :  if(!showDescend ){
                        // active role
                        setIcon(activeRoleIcon);
                        break;
                    }else{
                        switch ( roleBean.getRoleType() ) {
                            case 'S' :
                                lblRole.setIcon(activeSysRole);
                                break;
                                
                            case 'R' :
                                lblRole.setIcon(activeRoleIcon);
                                break;
                                
                            case 'P' :
                                lblRole.setIcon(activePropRole);
                                break;
                                
                            case 'O' :
                                lblRole.setIcon(activeAdminRole);
                                break;
                             //Added for IACUC Changes - Start
                            case IACUC_RIGHT_TYPE:
                                lblRole.setIcon(iacucActiveIcon);
                                break;
                            //IACUC Changes - End
                        }
                        if( roleBean.isDescend() ) {
                            lblDescend.setIcon(activeDescend);
                        }else{
                            lblDescend.setIcon(inactiveDescend);
                        }
                    }
                    break;
                    // }
                    case 'I' :  if(!showDescend ){
                        // inactive role
                        setIcon(inactiveRoleIcon);
                        break;
                    }else{
                        switch(roleBean.getRoleType()){
                            case 'S' :
                                lblRole.setIcon(inactiveSysRole);
                                break;
                                
                            case 'R' :
                                lblRole.setIcon(inactiveRoleIcon);
                                break;
                                
                            case 'P' :
                                lblRole.setIcon(inactivePropRole);
                                break;
                                
                            case 'O' :
                                lblRole.setIcon(inactiveAdminRole);
                                break;
                            //Added for IACUC Changes - Start
                            case IACUC_RIGHT_TYPE:
                                lblRole.setIcon(iacucInActiveIcon);
                                break;
                            //IACUC Changes - End
                        }
                        if( roleBean.isDescend()) {
                            lblDescend.setIcon(activeDescend);
                        }else{
                            lblDescend.setIcon(inactiveDescend);
                        }
                    }
                    break;
                }
                lblDescend.setText(nodeValue);
            }else{
                /* node is of type user */
                UserInfoBean userBean = bean.getUserBean();
                if( userBean != null ){
                    // to get userId
                    userId = userBean.getUserId();
                    // get User Name
                    userName = userBean.getUserName();
                    //concatenate user Id and User Name.
                    user = userId +"  " + userName;
                    
                    switch ( userBean.getStatus() ){
                        case 'A' :  if(!showDescend){
                            // active user
                            setIcon(activeUserIcon);
                            break;
                        }else{
                            lblRole.setIcon(activeUserIcon);
                            if(userBean.getDescendFlag()=='Y'){
                                lblDescend.setIcon(activeDescend);
                            }else{
                                lblDescend.setIcon(inactiveDescend);
                            }
                        }
                        break;
                        case 'I' :  if(!showDescend){
                            // inactive user
                            setIcon(inactiveUserIcon);
                            break;
                        }else{
                            lblRole.setIcon(inactiveUserIcon);
                            if(userBean.getDescendFlag()=='Y'){
                                lblDescend.setIcon(activeDescend);
                            }else{
                                lblDescend.setIcon(inactiveDescend);
                            }
                        }
                    }
                }
                lblDescend.setText(user);
            }
            if( sel ) {
                           pnlRolesPanel.setBackground((java.awt.Color) javax.swing.UIManager.getDefaults().get("Table.selectionBackground"));
                           lblDescend.setForeground(Color.white);
            }else   {
                            pnlRolesPanel.setBackground((java.awt.Color) javax.swing.UIManager.getDefaults().get("Panel.background"));
                
                    }
        }
        if(!showDescend){
            return this;
        }else{
            return pnlRolesPanel;
        }
    }
    
    /** Getter for property activeRoleIcon.
     * @return Value of property activeRoleIcon.
     */
    public ImageIcon getActiveRoleIcon() {
        return activeRoleIcon;
    }
    
    /** Setter for property activeRoleIcon.
     * @param activeRoleIcon New value of property activeRoleIcon.
     */
    public void setActiveRoleIcon(ImageIcon activeRoleIcon) {
        this.activeRoleIcon = activeRoleIcon;
    }
    
    /** Getter for property inactiveRoleIcon.
     * @return Value of property inactiveRoleIcon.
     */
    public ImageIcon getInactiveRoleIcon() {
        return inactiveRoleIcon;
    }
    
    /** Setter for property inactiveRoleIcon.
     * @param inactiveRoleIcon New value of property inactiveRoleIcon.
     */
    public void setInactiveRoleIcon(ImageIcon inactiveRoleIcon) {
        this.inactiveRoleIcon = inactiveRoleIcon;
    }
    
    /** Getter for property activeUserIcon.
     * @return Value of property activeUserIcon.
     */
    public ImageIcon getActiveUserIcon() {
        return activeUserIcon;
    }
    
    /** Setter for property activeUserIcon.
     * @param activeUserIcon New value of property activeUserIcon.
     */
    public void setActiveUserIcon(ImageIcon activeUserIcon) {
        this.activeUserIcon = activeUserIcon;
    }
    
    /** Getter for property inactiveUserIcon.
     * @return Value of property inactiveUserIcon.
     */
    public ImageIcon getInactiveUserIcon() {
        return inactiveUserIcon;
    }
    
    /** Setter for property inactiveUserIcon.
     * @param inactiveUserIcon New value of property inactiveUserIcon.
     */
    public void setInactiveUserIcon(ImageIcon inactiveUserIcon) {
        this.inactiveUserIcon = inactiveUserIcon;
    }
    
    /** Setter for Property ActiveAdminRole Icon
     *@param ActiveAdminRole icon New value of property activeAdminRole
     */
    public void setActiveAdminIcon(ImageIcon activeAdminRole){
        this.activeAdminRole = activeAdminRole;
    }
    /** Getter for property activeAdminRole icon.
     *@return value of property activeAdmin icon
     */
    public ImageIcon getActiveAdminIcon(){
        return activeAdminRole;
    }
    /** Setter for Property InactiveAdminRole Icon
     *@param ActiveAdminRole icon New value of property inactiveAdminRole
     */
    public void setInactiveAdminIcon(ImageIcon inactiveAdminRole){
        this.inactiveAdminRole = inactiveAdminRole;
    }
    /** Getter for property inactiveAdminRole icon.
     *@return value of property inactiveAdmin icon
     */
    public ImageIcon getInactiveAdminIcon(){
        return inactiveAdminRole;
    }
    
    /**
     *Setter for property activePropRole Icon
     *@param activePropRole icon, new value of property activePropRole
     */
    public void setActivePropIcon(ImageIcon activePropRole){
        this.activePropRole = activePropRole;
    }
    /** getter for property activePropRole icon
     *@return value of property activePropRole icon.
     */
    public ImageIcon getActivePropIcon(){
        return activePropRole;
    }
    
    /** setter for property inactivePropRole icon
     *@param inactivePropRole icon New value of property inactivePropRole icon.
     */
    public void setInactivePropIcon(ImageIcon inactivePropRole){
        this.inactivePropRole = inactivePropRole;
    }
    /** getter for property inactivePropRole Icon.
     *@return value of property inactivePropRole
     */
    public ImageIcon getInactivePropIcon(){
        return inactivePropRole;
    }
    
    /**setter for property activeSysRole icon
     *@param activeSysRole icon New value of property activeSysRole icon.
     */
    public void setActiveSysIcon(ImageIcon activeSysRole){
        this.activeSysRole = activeSysRole;
    }
    /** getter for property activeSysRole Icon.
     *@return value of property activeSysRole
     */
    public ImageIcon getActiveSysIcon(){
        return activeSysRole;
    }
    /**setter for property inactiveSysRole icon
     *@param inactiveSysRole icon New value of property inactiveSysRole icon.
     */
    public void setInactiveSysIcon(ImageIcon inactiveSysRole){
        this.inactiveSysRole = inactiveSysRole;
    }
    /** getter for property inactiveSysRole Icon.
     *@return value of property inactiveSysRole
     */
    public ImageIcon getInactiveSysIcon(){
        return inactiveSysRole;
    }
    
    /** setter for property activeDescend icon
     *@param activeDescend New value of property activeDescend
     */
    public void setActiveDescendIcon(ImageIcon activeDescend){
        this.activeDescend = activeDescend;
    }
    /** getter for property activeDescend Icon.
     *@return value of property activeDescend
     */
    public ImageIcon getActiveDescendIcon(){
        return activeDescend;
    }
    /** setter for property inactiveDescend icon
     *@param inactiveDescend New value of property inactiveDescend
     */
    public void setInactiveDescendIcon(ImageIcon inactiveDescend){
        this.inactiveDescend = inactiveDescend;
    }
    
    /** getter for property inactiveDescend Icon.
     *@return value of property inactiveDescend
     */
    public ImageIcon getInactiveDescendIcon(){
        return inactiveDescend;
    }
    
}
