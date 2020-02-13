/*
 * ProtocolMenuBean.java
 *
 * Created on March 30, 2005, 3:47 PM
 */

package edu.mit.coeuslite.utils.bean;

import edu.mit.coeus.bean.CoeusBean;

/**
 *
 * @author  bijosht
 */
public class MenuBean implements java.io.Serializable {
    
private String menuId;
private String menuName;
private String menuLink;
private boolean selected;
private boolean dataSaved;
private String fieldName;
private boolean visible;
// Groups the menu's
private String group;
// Added with CoeusQA2313: Completion of Questionnaire for Submission
private Object userObject;
// CoeusQA2313: Completion of Questionnaire for Submission - end

/**Holds the specific id's if required. This can be used when menus are 
 *generated dynamically
 */
private String dynamicId;

    /** Creates a new instance of ProtocolMenuBean */
    public MenuBean() {
    }
    
    /**
     * Getter for property menuId.
     * @return Value of property menuId.
     */
    public java.lang.String getMenuId() {
        return menuId;
    }
    
    /**
     * Setter for property menuId.
     * @param menuId New value of property menuId.
     */
    public void setMenuId(java.lang.String menuId) {
        this.menuId = menuId;
    }
    
    /**
     * Getter for property menuName.
     * @return Value of property menuName.
     */
    public java.lang.String getMenuName() {
        return menuName;
    }
    
    /**
     * Setter for property menuName.
     * @param menuName New value of property menuName.
     */
    public void setMenuName(java.lang.String menuName) {
        this.menuName = menuName;
    }
    
    /**
     * Getter for property menuLink.
     * @return Value of property menuLink.
     */
    public java.lang.String getMenuLink() {
        return menuLink;
    }
    
    /**
     * Setter for property menuLink.
     * @param menuLink New value of property menuLink.
     */
    public void setMenuLink(java.lang.String menuLink) {
        this.menuLink = menuLink;
    }
    
    /**
     * Getter for property selected.
     * @return Value of property selected.
     */
    public boolean isSelected() {
        return selected;
    }
    
    /**
     * Setter for property selected.
     * @param selected New value of property selected.
     */
    public void setSelected(boolean selected) {
        this.selected = selected;
    }
    
    /**
     * Getter for property dataSaved.
     * @return Value of property dataSaved.
     */
    public boolean isDataSaved() {
        return dataSaved;
    }
    
    /**
     * Setter for property dataSaved.
     * @param dataSaved New value of property dataSaved.
     */
    public void setDataSaved(boolean dataSaved) {
        this.dataSaved = dataSaved;
    }
    
    /**
     * Getter for property fieldName.
     * @return Value of property fieldName.
     */
    public java.lang.String getFieldName() {
        return fieldName;
    }
    
    /**
     * Setter for property fieldName.
     * @param fieldName New value of property fieldName.
     */
    public void setFieldName(java.lang.String fieldName) {
        this.fieldName = fieldName;
    }
    
    /**
     * Getter for property group.
     * @return Value of property group.
     */
    public java.lang.String getGroup() {
        return group;
    }
    
    /**
     * Setter for property group.
     * @param group New value of property group.
     */
    public void setGroup(java.lang.String group) {
        this.group = group;
    }
    
    /**
     * Getter for property visible.
     * @return Value of property visible.
     */
    public boolean isVisible() {
        return visible;
    }
    
    /**
     * Setter for property visible.
     * @param visible New value of property visible.
     */
    public void setVisible(boolean visible) {
        this.visible = visible;
    }
    
    /**
     * Getter for property dynamicId.
     * @return Value of property dynamicId.
     */
    public java.lang.String getDynamicId() {
        return dynamicId;
    }
    
    /**
     * Setter for property dynamicId.
     * @param dynamicId New value of property dynamicId.
     */
    public void setDynamicId(java.lang.String dynamicId) {
        this.dynamicId = dynamicId;
    }
    
    // Added with CoeusQA2313: Completion of Questionnaire for Submission
    /**
     * Getter for property userObject
     */
    public Object getUserObject() {
        return userObject;
    }

    /**
     * Setter for property userObject
     */
    public void setUserObject(Object userObject) {
        this.userObject = userObject;
    }
    
    // CoeusQA2313: Completion of Questionnaire for Submission- END
    
}
