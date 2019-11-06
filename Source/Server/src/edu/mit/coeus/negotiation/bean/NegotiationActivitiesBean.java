/*
 * NegotiationActvitiesBean.java
 * This bean is for the negotiation activities bean
 * Created on July 13, 2004, 4:33 PM
 */

package edu.mit.coeus.negotiation.bean;

import java.sql.Date;
import java.beans.*;

/**
 * opyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 * @author  jobinelias
 */
public class NegotiationActivitiesBean extends NegotiationBaseBean {
	
	private int activityNumber;
	private String description;
	private int negotiationActTypeCode;
	private String documentFileAddress;
	private Date followUpDate;
	private Date createDate;
	private Date activityDate;
	private boolean restrictedView;
	private String lastUpdatedBy;
	private PropertyChangeSupport propertySupport;
	private String activityTypeDescription;
	private static final String RESTRICTED_VIEW = "restrictedView";
//        New fields added with case 2806 - attachments to negotiations - Start
        private boolean attachmentPresent;
        private NegotiationAttachmentBean attachmentBean;
//        New fields added with case 2806 - attachments to negotiations - End

	/** Creates a new instance of NegotiationActvitiesBean */
	public NegotiationActivitiesBean() {
		propertySupport = new PropertyChangeSupport( this );
	}
	/**
     * Method used to add propertychange listener to the fields
     * @param listener PropertyChangeListener
     */
    public void addPropertyChangeListener(PropertyChangeListener listener) {
        propertySupport.addPropertyChangeListener(listener);
    }

    /**
     * Method used to remove propertychange listener
     * @param listener PropertyChangeListener
     */
    public void removePropertyChangeListener(PropertyChangeListener listener) {
        propertySupport.removePropertyChangeListener(listener);
    }
	
	/**
	 * Getter for property lastUpdatedBy.
	 * @return Value of property lastUpdatedBy.
	 */
	public java.lang.String getLastUpdatedBy() {
		return lastUpdatedBy;
	}
	
	/**
	 * Setter for property lastUpdatedBy.
	 * @param lastUpdatedBy New value of property lastUpdatedBy.
	 */
	public void setLastUpdatedBy(java.lang.String lastUpdatedBy) {
		this.lastUpdatedBy = lastUpdatedBy;
	}
	
	/**
	 * Getter for property activityNumber.
	 * @return Value of property activityNumber.
	 */
	public int getActivityNumber() {
		return activityNumber;
	}
	
	/**
	 * Setter for property activityNumber.
	 * @param activityNumber New value of property activityNumber.
	 */
	public void setActivityNumber(int activityNumber) {
		this.activityNumber = activityNumber;
	}
	
	/**
	 * Getter for property description.
	 * @return Value of property description.
	 */
	public java.lang.String getDescription() {
		return description;
	}
	
	/**
	 * Setter for property description.
	 * @param description New value of property description.
	 */
	public void setDescription(java.lang.String description) {
		this.description = description;
	}
	
	/**
	 * Getter for property negotiationActTypeCode.
	 * @return Value of property negotiationActTypeCode.
	 */
	public int getNegotiationActTypeCode() {
		return negotiationActTypeCode;
	}
	
	/**
	 * Setter for property negotiationActTypeCode.
	 * @param negotiationActTypeCode New value of property negotiationActTypeCode.
	 */
	public void setNegotiationActTypeCode(int negotiationActTypeCode) {
		this.negotiationActTypeCode = negotiationActTypeCode;
	}
	
	/**
	 * Getter for property documentFileAddress.
	 * @return Value of property documentFileAddress.
	 */
	public java.lang.String getDocumentFileAddress() {
		return documentFileAddress;
	}
	
	/**
	 * Setter for property documentFileAddress.
	 * @param documentFileAddress New value of property documentFileAddress.
	 */
	public void setDocumentFileAddress(java.lang.String documentFileAddress) {
		this.documentFileAddress = documentFileAddress;
	}
	
	/**
	 * Getter for property followUpDate.
	 * @return Value of property followUpDate.
	 */
	public java.sql.Date getFollowUpDate() {
		return followUpDate;
	}
	
	/**
	 * Setter for property followUpDate.
	 * @param followUpDate New value of property followUpDate.
	 */
	public void setFollowUpDate(java.sql.Date followUpDate) {
		this.followUpDate = followUpDate;
	}
	
	/**
	 * Getter for property createDate.
	 * @return Value of property createDate.
	 */
	public java.sql.Date getCreateDate() {
		return createDate;
	}
	
	/**
	 * Setter for property createDate.
	 * @param createDate New value of property createDate.
	 */
	public void setCreateDate(java.sql.Date createDate) {
		this.createDate = createDate;
	}
	
	/**
	 * Getter for property activityDate.
	 * @return Value of property activityDate.
	 */
	public java.sql.Date getActivityDate() {
		return activityDate;
	}
	
	/**
	 * Setter for property activityDate.
	 * @param activityDate New value of property activityDate.
	 */
	public void setActivityDate(java.sql.Date activityDate) {
		this.activityDate = activityDate;
	}
	
	/**
	 * Getter for property isRestrictedView.
	 * @return Value of property isRestrictedView.
	 */
	public boolean isRestrictedView() {
		return restrictedView;
	}
	
	/**
	 * Setter for property isRestrictedView.
	 * @param newRestrictedView New value of property isRestrictedView.
	 */
	public void setRestrictedView(boolean newRestrictedView) {
		boolean oldRestrictedView = restrictedView;
        this.restrictedView = newRestrictedView;
        propertySupport.firePropertyChange(RESTRICTED_VIEW, 
                oldRestrictedView, restrictedView);
	}
	
	/** 
     * Equals method to check whether Primary Key values are same for the given Bean
     * @param obj Object
     * @return  boolean indicating whether the two beans are same or not
     */    
    public boolean equals(Object obj) {
        NegotiationActivitiesBean negotiationActivitiesBean = (NegotiationActivitiesBean)obj;
        if (negotiationActivitiesBean.getNegotiationNumber().equals(getNegotiationNumber()) &&
			(negotiationActivitiesBean.getActivityNumber() == getActivityNumber())) {
            return true;
        } else {
            return false;
        }
    }
	
	/**
	 * Getter for property activityTypeDescription.
	 * @return Value of property activityTypeDescription.
	 */
	public java.lang.String getActivityTypeDescription() {
		return activityTypeDescription;
	}
	
	/**
	 * Setter for property activityTypeDescription.
	 * @param activityTypeDescription New value of property activityTypeDescription.
	 */
	public void setActivityTypeDescription(java.lang.String activityTypeDescription) {
		this.activityTypeDescription = activityTypeDescription;
	}
        //New getters and setters added with case 2806 - attachments to negotiations - start
        public NegotiationAttachmentBean getAttachmentBean() {
            return attachmentBean;
        }
        
        public void setAttachmentBean(NegotiationAttachmentBean attachmentBean) {
            this.attachmentBean = attachmentBean;
        }
        
         public boolean isAttachmentPresent() {
            return attachmentPresent;
        }

        public void setAttachmentPresent(boolean attachmentPresent) {
            this.attachmentPresent = attachmentPresent;
        }
        //New getters and setters added with case 2806 - attachments to negotiations - end

       

}
