/*
 * @(#)AreaOfResearchTreeNodeBean.java
 *
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 *
 */

package edu.mit.coeus.irb.bean;

import java.util.Vector;
import java.sql.Timestamp;
import edu.mit.coeus.utils.tree.ITreeNodeInfo; 

/**
 * The class used to represent each node in the
 * Area of Research tree structure. It holds all the attributes like
 * Research Code, Parent Research Code and Description. It contains accessor 
 * method to get&set values to the resepctive Attributes. This data bean
 * represent a Each Tree Node data and it will be used by the <Code>DnDJTree</Code> & 
 * <Code>TreeBuilder</Code> Class.
 * 
 * @author  Subramanya
 * @version 1.0 September 20, 2002, 4:50 PM
 */

public class AreaOfResearchTreeNodeBean implements java.io.Serializable,
                                                    ITreeNodeInfo {

    //holds research area code( unique/primary key)
    private String researchAreaCode;

    //holds parent research area code
    private String parentResearchAC;

    //holds childer flag status
    private boolean hasChildren;

    //holds update tmiestamp
    private Timestamp updateTimestamp;

    //holds update user id
    private String updateUser;

    //holds unit name
    private String description;

    //Collection of Node/Element's of Type Area of Research
    private Vector researchAreaChildren = null;


    /** 
     * Creates new AreaOfResearchTreeNodeBean.
     * @param arCode        Area Of Research Code( String)
     * @param prCode        Parent Research Area Code( String)
     * @param hsChild       has Children ( boolean - falg )
     * @param tmStamp       Time Stamp for UPdation.( java.sql.Timestamp )
     * @param updater       Name of the Updater ( String )
     * @param desc          Description of the Area of Research 
     */
    public AreaOfResearchTreeNodeBean( String arCode, String prCode,
                                               boolean hsChild, String tmStamp,
                                               String updater, String desc ) {

                    researchAreaCode = arCode;
                    parentResearchAC = prCode;
                    hasChildren = hsChild;
                    updateTimestamp = Timestamp.valueOf( tmStamp );
                    updateUser = updater;
                    description = desc ;

    }

        
    /** 
     * Creates new AreaOfResearchTreeNodeBean.
     * @param arCode        Area Of Research Code( String)
     * @param prCode        Parent Research Area Code( String)
     * @param hsChild       has Children ( boolean - falg )
     * @param updater       Name of the Updater ( String )
     * @param desc          Description of the Area of Research 
     */
    public AreaOfResearchTreeNodeBean( String arCode, String prCode,
                                           boolean hsChild, String desc, 
                                           String updater ) {

                researchAreaCode = arCode;
                parentResearchAC = prCode;
                hasChildren = hsChild;
                updateUser = updater;
                description = desc ;

    }
        
        
    /** 
     * Creates new AreaOfResearchTreeNodeBean.
     * @param arCode        Area Of Research Code( String)
     * @param prCode        Parent Research Area Code( String)
     * @param hsChild       has Children ( boolean - falg )
     * @param desc          Description of the Area of Research 
     */
    public AreaOfResearchTreeNodeBean( String arCode, String prCode,
                                        boolean hsChild, String desc ) {

                researchAreaCode = arCode;
                parentResearchAC = prCode;
                hasChildren = hsChild;
                updateTimestamp = null;
                updateUser = null;
                description = desc ;

    }


    /** 
     * Creates new AreaOfResearchTreeNodeBean.
     * @param arCode        Area Of Research Code( String)
     * @param prCode        Parent Research Area Code( String)
     * @param hsChild       has Children ( boolean - falg )
     */
    public AreaOfResearchTreeNodeBean( String arCode, String prCode,
                                                boolean hsChild) {

                researchAreaCode = arCode;
                parentResearchAC = prCode;
                hasChildren = hsChild;
                updateTimestamp = null;
                updateUser = null;
                description = null ;
    }


    /**
     * Add or Append new child to the ChildrenList of existing Area Of 
     * Research Tree Data Structure.
     * @param child         new Area Of Research ( 
     *                                 edu.mit.coeus.utils.tree.ITreeNodeInfo)
     *
     * @return boolean status flag of the Add Operation. (True - Successful: 
     *                                                    False - Failure )
     */
    public boolean addNewChild( ITreeNodeInfo child ){

            boolean isAdded = false;
            if( researchAreaChildren == null ){
                    researchAreaChildren = new Vector();
                    hasChildren = true;
            }
            if( child != null ){
                    researchAreaChildren.addElement( child );
                    isAdded = true;
            }
            return isAdded;
    }


    /**
     * Gets all the children of this AreaOfResearchTree Node/Tree Structure.
     *
     * @return Vector contains the collection of all children each of
     * type AreaOfResHierarchicalFormBean.
     *
     */
    public Vector getAllChildrens(){

            return researchAreaChildren;
    }


    /**
     * Method used to get Research Area Code.
     * @return String represent the Research Area Code.
     *
     */
    public String getResearchAreaCode() {

        return researchAreaCode;
    }

    
    /**
     * Method used to get Research Area Code.
     * @return String represent the Research Area Code.
     *
     */
    public String getNodeID() {

        return researchAreaCode;
    }


    /**
     * Method used to set the parent node id.
     * @param parentID represent the Parent Research Area Code.
     */
    public void setParentNodeID( String parentID ) {

        this.parentResearchAC = parentID;
    }
        
        
    /**
     * Method used to set the unit number
     * @param researchCode represent the Research Area Code.
     */
    public void setResearchAreaCode( String researchCode ) {

        this.researchAreaCode = researchCode;
    }

    /**
     * Method used to get the parent Research Area Code.
     * @return String represent Research Area Code.
     */
    public String getParentResearchAreaCode() {

        return parentResearchAC;
    }


    /**
     * Method used to get the parent Research Area Code.
     * @return String represent Research Area Code.
     */
    public String getParentNodeID() {

        return parentResearchAC;
    }


    /**
     * Method used to set the parent Research Area Code.
     * @param parentCode represent the Research Area Code.
     */
    public void setParentResearchAreaCode( String parentCode ) {

        this.parentResearchAC = parentCode;
    }

    
    /**
     *  Method used to get the status of the children flag
     * @return boolean true if the Node(Research Code) has children,
     * false if not.
     */
    public boolean getChildrenFlag() {

        return hasChildren;
    }


    /**
     *  Method used to get the status of the children flag
     * @return boolean true if the Node(Research Code) has children,
     * false if not.
     */
    public boolean hasChildren() {

        return hasChildren;
    }


    /**
     * Method used to set the status of the children flag
     * @param childrenFlag represents that it has Children or not. String
     * 'Y' refers to setting true value otherwise false.
     */
    public void setChildrenFlag( String childrenFlag ) {

        this.hasChildren = childrenFlag.equalsIgnoreCase("y")?true:false;
    }


    /**
     * Method used to get the updated timestamp
     * @return Timestamp Updated Timestamp.
     */
    public Timestamp getUpdateTimestamp() {

        return updateTimestamp;
    }


    /**
     * Method used to set the updated timestamp
     * @param updateTimestamp represent Updated Timestamp
     */
    public void setUpdateTimestamp( String updateTimestamp ) {

        this.updateTimestamp = Timestamp.valueOf( updateTimestamp);
    }


    /**
     * Method used to get the update user id
     * @return String Updated User Id
     */
    public String getUpdateUser() {

        return updateUser;
    }


    /**
     * Method used to set the update user id
     * @param updateUser Updated User Id
     */
    public void setUpdateUser( String updateUser ) {

        this.updateUser = updateUser;
    }

    
    /**
     * Method used to get the Research Area Description.
     * @return String represent Research Area Description.
     */
    public String getRADescription() {

        return description;
    }


    /**
     * Method used to get the Research Area Description.
     * @return String represent Research Area Description.
     */
    public String getNodeDescription() {

        return description;
    }

    
    /**
     * Method used to set the Research Area Description.
     * @param desc represent the Research Area Description.
     */
    public void setRADescription( String desc ) {

        this.description = desc;
    }


    /**
     * Method used to get the Relative Name of Research Area.
     * This will be concatination of Research Area Code and Description.
     * @return String relative Name containing Research Code and Description.
     */
    public String getRelativeName(){

            return researchAreaCode + " : " + description;
    }

    
    /**
     * Method used to get the Relative Name of Research Area.
     * This will be concatination of Research Area Code and Description.
     * @return String relative Name containing Research Code and Description.
     */
    public String toString(){

            return researchAreaCode + " : " + description ;
    }
    
}