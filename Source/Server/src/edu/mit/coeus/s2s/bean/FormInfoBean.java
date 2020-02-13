package edu.mit.coeus.s2s.bean;

import edu.mit.coeus.bean.BaseBean;
import java.io.Serializable;
import java.sql.Timestamp;

public class FormInfoBean implements BaseBean,Serializable{
    private String ns;
    private String schUrl;
    private String formName;
    private boolean mandatory = true;
    private boolean available;
    private boolean include;
    private Timestamp updateTimestamp;
    private String updateUser;
    private String proposalNumber;
    private Timestamp awUpdateTimestamp;
    private String awProposalNumber;
    private char acType;
    private String sortIndex="zzzzzzz";
    private boolean attachedForm;
    public FormInfoBean(String ns,String minOccurs,String schUrl){
        this(ns,minOccurs,schUrl,null);
    }
    public FormInfoBean(String ns,String minOccurs,String schUrl,String formName){
        this.ns = ns;
        this.schUrl = schUrl;
        this.mandatory = (minOccurs==null || minOccurs.trim().equals("") || Integer.parseInt(minOccurs)>0);
        this.formName = formName;
    }
    
    public FormInfoBean(){
    }
    /**
     * Getter for property ns.
     * @return Value of property ns.
     */
    public java.lang.String getNs() {
        return ns;
    }
    
    /**
     * Setter for property ns.
     * @param ns New value of property ns.
     */
    public void setNs(java.lang.String ns) {
        this.ns = ns;
    }
    
    /**
     * Getter for property schUrl.
     * @return Value of property schUrl.
     */
    public java.lang.String getSchUrl() {
        return schUrl;
    }
    
    /**
     * Setter for property schUrl.
     * @param schUrl New value of property schUrl.
     */
    public void setSchUrl(java.lang.String schUrl) {
        this.schUrl = schUrl;
    }
    
    /**
     * Getter for property isMandatory.
     * @return Value of property isMandatory.
     */
    public boolean isMandatory() {
        return mandatory;
    }
    
    /**
     * Setter for property isMandatory.
     * @param isMandatory New value of property isMandatory.
     */
    public void setMandatory(boolean mandatory) {
        this.mandatory = mandatory;
    }
    
    public String toString(){
        String msg = isMandatory()?"Mandatory":"Optional";
        StringBuffer msgBffr = new StringBuffer(msg);
        msgBffr.append("NameSpace"+this.ns);
        return msgBffr.toString();
        
    }
    
    /**
     * Getter for property formName.
     * @return Value of property formName.
     */
    public java.lang.String getFormName() {
        return formName;
    }
    
    /**
     * Setter for property formName.
     * @param formName New value of property formName.
     */
    public void setFormName(java.lang.String formName) {
        this.formName = formName;
    }
    
    /**
     * Getter for property available.
     * @return Value of property available.
     */
    public boolean isAvailable() {
        return available;
    }
    
    /**
     * Setter for property available.
     * @param available New value of property available.
     */
    public void setAvailable(boolean available) {
        this.available = available;
    }
    
    /**
     * Getter for property include.
     * @return Value of property include.
     */
    public boolean isInclude() {
        //should include if its a mandatory form and its available
        if(mandatory && available) return true;
        return include;
    }
    
    /**
     * Setter for property include.
     * @param include New value of property include.
     */
    public void setInclude(boolean include) {
        this.include = include;
    }
    
    /**
     * Getter for property updateTimestamp.
     * @return Value of property updateTimestamp.
     */
    public java.sql.Timestamp getUpdateTimestamp() {
        return updateTimestamp;
    }
    
    /**
     * Setter for property updateTimestamp.
     * @param updateTimestamp New value of property updateTimestamp.
     */
    public void setUpdateTimestamp(java.sql.Timestamp updateTimestamp) {
        this.updateTimestamp = updateTimestamp;
    }
    
    /**
     * Getter for property updateUser.
     * @return Value of property updateUser.
     */
    public java.lang.String getUpdateUser() {
        return updateUser;
    }
    
    /**
     * Setter for property updateUser.
     * @param updateUser New value of property updateUser.
     */
    public void setUpdateUser(java.lang.String updateUser) {
        this.updateUser = updateUser;
    }
    
    /**
     * Getter for property proposalNumber.
     * @return Value of property proposalNumber.
     */
    public java.lang.String getProposalNumber() {
        return proposalNumber;
    }
    
    /**
     * Setter for property proposalNumber.
     * @param proposalNumber New value of property proposalNumber.
     */
    public void setProposalNumber(java.lang.String proposalNumber) {
        this.proposalNumber = proposalNumber;
    }
    
    /**
     * Getter for property awUpdateTimestamp.
     * @return Value of property awUpdateTimestamp.
     */
    public java.sql.Timestamp getAwUpdateTimestamp() {
        return awUpdateTimestamp;
    }
    
    /**
     * Setter for property awUpdateTimestamp.
     * @param awUpdateTimestamp New value of property awUpdateTimestamp.
     */
    public void setAwUpdateTimestamp(java.sql.Timestamp awUpdateTimestamp) {
        this.awUpdateTimestamp = awUpdateTimestamp;
    }
    
    /**
     * Getter for property awProposalNumber.
     * @return Value of property awProposalNumber.
     */
    public java.lang.String getAwProposalNumber() {
        return awProposalNumber;
    }
    
    /**
     * Setter for property awProposalNumber.
     * @param awProposalNumber New value of property awProposalNumber.
     */
    public void setAwProposalNumber(java.lang.String awProposalNumber) {
        this.awProposalNumber = awProposalNumber;
    }
    
    /**
     * Getter for property acType.
     * @return Value of property acType.
     */
    public char getAcType() {
        return acType;
    }
    
    /**
     * Setter for property acType.
     * @param acType New value of property acType.
     */
    public void setAcType(char acType) {
        this.acType = acType;
    }
    
    /**
     * Getter for property sortIndex.
     * @return Value of property sortIndex.
     */
    public String getSortIndex() {
        return sortIndex;
    }
    
    /**
     * Setter for property sortIndex.
     * @param sortIndex New value of property sortIndex.
     */
    public void setSortIndex(String sortIndex) {
        this.sortIndex = sortIndex;
    }
    /**
     * Getter for property available.
     * @return Value of property sortIndex.
     */
    public int getAvailable() {
        return available?0:1;
    }
    /**
     * Getter for property available.
     * @return Value of property sortIndex.
     */
    public int getInclude() {
        return include?0:1;
    }
	/**
	 * @return the attachedForm
	 */
	public boolean isAttachedForm() {
		return attachedForm;
	}
	/**
	 * @param attachedForm the attachedForm to set
	 */
	public void setAttachedForm(boolean attachedForm) {
		this.attachedForm = attachedForm;
	}
    
}
