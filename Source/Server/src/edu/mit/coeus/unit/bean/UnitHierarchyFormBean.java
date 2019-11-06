/*
 * @(#)UnitHierarchicalFormBean.java
 *
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 *
 */

package edu.mit.coeus.unit.bean;

import edu.mit.coeus.bean.BaseBean;
import edu.mit.coeus.utils.CoeusVector;

import java.util.Vector;

import edu.mit.coeus.utils.tree.ITreeNodeInfo; 

/**
 * The class used to represent each node in the
 * Unit Hierarchy tree structure.
 *
 * @author  Geo
 * @version 1.0 September 20, 2002, 4:50 PM
 */

public class UnitHierarchyFormBean implements BaseBean, java.io.Serializable, ITreeNodeInfo {
    //holds unit number( unique/primary key)
    private String unitNumber;
    //holds unit number( unique/primary key)
    private String unitName;
    //holds parent parent unit number
    private String parentUnitNumber;
    //holds parent parent unit number
    private String parentUnitName;
    //holds childer flag status
    private boolean hasChildren;
    //holds update timestamp
    private java.sql.Timestamp updateTimestamp;
    //holds update user id
    private String updateUser;
    //holds unit detail
    private UnitDetailFormBean unitDetail;
    //Collection of Node/Element's of Type UnitHierarchyFormBean
    private Vector unitChildren = null;
    //holds the function type
    private char functionType;
    //Added for Unit Hierarchy Enhancement start by Tarique
    //Holds the admin type and type code
    private CoeusVector cvAdminType;
    private CoeusVector cvAdminTypeCode;
    //Added for Unit Hierarchy Enhancement end by Tarique
    
    /* JM 7-14-2015 added status */
    private String status;    
    
    /** 
     * Creates new UnitHierarchyFormBean
     */
     public UnitHierarchyFormBean(){
     }
     
     /**
      * Creates new UnitHierarchyFormBean
      * @param unitNumber unitNumber
      * @param unitName Unit Name
      * @param parentUnitNumber parentUnitNumber
      * @param hsChild Children status      
      * @param tmStamp Time Stamp for UPdation.
      * @param updater Name of the Updater 
      * @param unitDetail unitDetail Form bean
      */
     public UnitHierarchyFormBean(String unitNumber, 
            String unitName, String parentUnitNumber, boolean hsChild, 
            String tmStamp, String updater, UnitDetailFormBean unitDetail) {
        this.unitNumber = unitNumber;
        this.unitName = unitName;
        this.parentUnitNumber = parentUnitNumber;
        this.hasChildren = hsChild;
        this.updateTimestamp = java.sql.Timestamp.valueOf( tmStamp );
        this.updateUser = updater;
        this.unitDetail = unitDetail ;
    }

     /** 
     * Creates new UnitHierarchyFormBean 
     * @param unitNumber            Unit Number
     * @param unitName              Unit Name
     * @param parentUnitNumber      Parent Unit Number
     * @param updater               Name of the Updater
     */
     public UnitHierarchyFormBean(String unitNumber, String unitName, 
            String parentUnitNumber, String updater) {
        this(unitNumber,unitName,parentUnitNumber,false,null,updater,null);
    }
        
    /** 
     * Creates new UnitHierarchyFormBean  .
     * @param unitNumber            Unit Number
     * @param parentUnitNumber      Parent Unit Number
     * @param hsChild               has Children
     */
     public UnitHierarchyFormBean(String unitNumber, String parentUnitNumber, 
            boolean hsChild) {
        this(unitNumber,null,parentUnitNumber,hsChild,null,null,null);
    }

    /**
     * Addes or Append New child to the ChildrenList of Existing Unit Hierarchy
     * @param child  ITreeNodeInfo
     * @return boolean status flag of the Add Operation. (True - Successful: 
     *                                                    False - Failure )
     */
    public boolean addNewChild( ITreeNodeInfo child ){
            boolean isAdded = false;
            if( unitChildren == null ){
                    unitChildren = new Vector();
                    hasChildren = true;
            }
            if( child != null ){
                    unitChildren.add( child );
                    isAdded = true;
            }
            return isAdded;
    }

    /**
     * gets all the children of this UnitHierarchy Node.
     *
     * @return Vector contains the collection of all childrens each of
     * type UnitHierarchyFormBean.
     *
     */
    public Vector getAllChildrens(){
        return unitChildren;
    }
    
    /* JM 7-14-2015 added status */
    /**
     *  Method used to get status
     * @return status
     */
    public String getStatus() {
        return status;
    }
    
    /**  Method used to set status
     * @param status
     */
    public void setStatus(String status) {
        this.status = status;
    }
    /* JM END */

    /**
     * Method used to get Unit Number
     * @return String represent the Unit Number
     *
     */
    public String getNodeID() {
        return unitNumber;
    }

    /**
     * Method used to set the unit number
     * @param unitNumber represent the Unit Number
     */
    public void setNodeID( String unitNumber ) {
        this.unitNumber = unitNumber;
    }

    /**
     * Method used to set the parent unit number.
     * @param parentID represent the Parent parent unit number
     */
    public void setParentNodeID( String parentID ) {
        this.parentUnitNumber = parentID;
    }
        
    /**
     * Method used to get the parent unit number
     * @return String represent parent unit number
     */
    public String getParentNodeID() {
        return parentUnitNumber;
    }

    /**
     * Method used to get unit name.
     * @return String represent the unit name.
     *
     */
    public String getNodeName() {
        return unitName;
    }

    /** Method used to set the unit name
     * @param unitName  unit name
     */
    public void setNodeName( String unitName ) {
        this.unitName = unitName;
    }

    /**
     * Method used to set the parent node name.
     * @param parentName represent the Parent Unit Name.
     */
    public void setParentNodeName( String parentName ) {
        this.parentUnitName = parentName;
    }
        
    /**
     * Method used to get the parent Unit Name.
     * @return String represent parent unit name.
     */
    public String getParentNodeName() {
        return parentUnitName;
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
     * @param childrenFlag represent the it has Children or not. String
     * 'Y' refers to setting true value otherwise false.
     */
    public void setChildrenFlag( String childrenFlag ) {
        this.hasChildren = childrenFlag.equalsIgnoreCase("y")?true:false;
    }

    /**
     * Method used to get the updated timestamp
     * @return Timestamp Updated Timestamp.
     */
    public java.sql.Timestamp getUpdateTimestamp() {
        return updateTimestamp;
    }

    /**
     * Method used to set the updated timestamp
     * @param updateTimestamp represent Updated Timestamp
     */
    public void setUpdateTimestamp( String updateTimestamp ) {
        this.updateTimestamp = java.sql.Timestamp.valueOf( updateTimestamp);
    }

    /**
     * Method used to get the update user id
     * @return String Update User Id
     */
    public String getUpdateUser() {
        return updateUser;
    }

    /** Method used to set the update user id
     * @param updateUser updateUser  */
    public void setUpdateUser( String updateUser ) {
        this.updateUser = updateUser;
    }

    /**
     * Method used to get the function type
     * @return String function type
     */
    public char getFunctionType() {
        return functionType;
    }

    /** Method used to set the function type
     * @param functionType  function type
     */
    public void setFunctionType( char functionType ) {
        this.functionType = functionType;
    }

    /**
     * Method used to get the unit name
     * @return String represent unit name
     */
    public String getNodeDescription() {
        return "";
    }

    /**
     * Method used to set the unit name
     * @param desc represent the unit name
     */
    public void setRADescription( String desc ) {
    }

    /** not required for this class
     * @return  String empty string
     */
    public String getRelativeName(){
        return "";
    }

    /** Method used to set the unit detail.
     * @param unitDetail UnitDetailFormBean
     */
    public void setUnitDetail( UnitDetailFormBean unitDetail ) {
        this.unitDetail = unitDetail;
    }

    /**
     * Method used to get the unit details.
     * @return UnitDetailFormBean Unit detail form bean instance
     */
    public UnitDetailFormBean getUnitDetail() {
        return unitDetail;
    }
    
    /** Overridden method to display the unit number and unit name
     * This will be concatination of Unit number and unit name.
     * @return  String unit Number+Name
     */
    public String toString(){
        return unitNumber + " : " + unitName ;
    }
    
    /**
     * Getter for property cvAdminType.
     * @return Value of property cvAdminType.
     */
    public CoeusVector getCvAdminType() {
        return cvAdminType;
    }
    
    /**
     * Setter for property cvAdminType.
     * @param cvAdminType New value of property cvAdminType.
     */
    public void setCvAdminType(CoeusVector cvAdminType) {
        this.cvAdminType = cvAdminType;
    }
    
    /**
     * Getter for property cvAdminTypeCode.
     * @return Value of property cvAdminTypeCode.
     */
    public edu.mit.coeus.utils.CoeusVector getCvAdminTypeCode() {
        return cvAdminTypeCode;
    }
    
    /**
     * Setter for property cvAdminTypeCode.
     * @param cvAdminTypeCode New value of property cvAdminTypeCode.
     */
    public void setCvAdminTypeCode(edu.mit.coeus.utils.CoeusVector cvAdminTypeCode) {
        this.cvAdminTypeCode = cvAdminTypeCode;
    }
    
}