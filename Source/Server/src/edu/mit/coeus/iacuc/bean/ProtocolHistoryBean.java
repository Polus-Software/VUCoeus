/*
 * ProtocolHistoryBean.java
 *
 * Created on July 10, 2007, 4:25 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package edu.mit.coeus.iacuc.bean;

import edu.mit.coeus.iacuc.bean.*;
import edu.mit.coeus.utils.CoeusVector;

/**
 * This class is used to hold the change details from one sequence number to
 * another sequence number of a particular protocol
 *
 * @author leenababu
 */
public class ProtocolHistoryBean implements java.io.Serializable {
    
    //holds the protocol number
    private String protocolNumber;
    
    //holds the sequence number
    private String sequenceNumber;
    
    //holds the title
    private String title;
    
    //holds the name of the principal Investigator
    private String princpalInvestigator;
    
    //holds the last action performed in the protocol
    private String lastAction;
    
    //holds the current ProtocolInfoBean object to which the details of ProtocolInfoBean
    //with a different sequence number is compared
    private ProtocolInfoBean currProtocolInfoBean;
    
    //holds the ProtocolInfoBean object to which the details of the
    //currentProtocolInfoBean is compared.
    private ProtocolInfoBean prevProtocolInfoBean;
    
    //holds the list of ChangeHistoryGroup objects
    private CoeusVector changeHistoryGroupList = new CoeusVector();
    
    /** Creates a new instance of ProtocolHistoryBean */
    public ProtocolHistoryBean() {
    }
    
    /**
     * Getter for property protocolNumber.
     * @return Value of property protocolNumber.
     */
    public String getProtocolNumber() {
        return protocolNumber;
    }
    
    /**
     * Setter for property protocolNumber.
     * @param name New value of property protocolNumber.
     */
    public void setProtocolNumber(String protocolNumber) {
        this.protocolNumber = protocolNumber;
    }
    
    /**
     * Getter for property sequenceNumber.
     * @return Value of property sequenceNumber.
     */
    public String getSequenceNumber() {
        return sequenceNumber;
    }
    
    /**
     * Setter for property sequenceNumber.
     * @param sequenceNumber New value of property sequenceNumber.
     */
    public void setSequenceNumber(String sequenceNumber) {
        this.sequenceNumber = sequenceNumber;
    }
    
    /**
     * Getter for property title.
     * @return Value of property title.
     */
    public String getTitle() {
        return title;
    }
    
    /**
     * Setter for property title.
     * @param title New value of property title.
     */
    public void setTitle(String title) {
        this.title = title;
    }
    
    /**
     * Getter for property princpalInvestigator.
     * @return Value of property princpalInvestigator.
     */
    public String getPrincpalInvestigator() {
        return princpalInvestigator;
    }
    
    /**
     * Setter for property princpalInvestigator.
     * @param princpalInvestigator New value of property princpalInvestigator.
     */
    public void setPrincpalInvestigator(String princpalInvestigator) {
        this.princpalInvestigator = princpalInvestigator;
    }
    
    /**
     * Getter for property lastAction.
     * @return Value of property lastAction.
     */
    public String getLastAction() {
        return lastAction;
    }
    
    /**
     * Setter for property lastAction.
     * @param lastAction New value of property lastAction.
     */
    public void setLastAction(String lastAction) {
        this.lastAction = lastAction;
    }
    
    /**
     * Getter for property currProtocolInfoBean.
     * @return Value of property currProtocolInfoBean.
     */
    public ProtocolInfoBean getCurrProtocolInfoBean() {
        return currProtocolInfoBean;
    }
    
    /**
     * Setter for property currProtocolInfoBean.
     * @param currProtocolInfoBean New value of property currProtocolInfoBean.
     */
    public void setCurrProtocolInfoBean(ProtocolInfoBean currProtocolInfoBean) {
        this.currProtocolInfoBean = currProtocolInfoBean;
    }
    
    /**
     * Getter for property prevProtocolInfoBean.
     * @return Value of property prevProtocolInfoBean.
     */
    public ProtocolInfoBean getPrevProtocolInfoBean() {
        return prevProtocolInfoBean;
    }
    
    /**
     * Setter for property prevProtocolInfoBean.
     * @param prevProtocolInfoBean New value of property prevProtocolInfoBean.
     */
    public void setPrevProtocolInfoBean(ProtocolInfoBean prevProtocolInfoBean) {
        this.prevProtocolInfoBean = prevProtocolInfoBean;
    }
    
    /**
     * Getter for property changeHistoryGroupList.
     * @return Value of property changeHistoryGroupList.
     */
    public CoeusVector getChangeHistoryGroupList() {
        return changeHistoryGroupList;
    }
    
    /**
     * Setter for property changeHistoryGroupList.
     * @param changeHistoryGroupList New value of property changeHistoryGroupList.
     */
    public void setChangeHistoryGroupList(CoeusVector changeHistoryGroupList) {
        this.changeHistoryGroupList = changeHistoryGroupList;
    }
}
