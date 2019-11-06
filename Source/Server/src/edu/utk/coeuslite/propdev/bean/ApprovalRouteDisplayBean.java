package edu.utk.coeuslite.propdev.bean;
import edu.mit.coeus.bean.BaseBean;
import java.util.Comparator;
import java.sql.Date;
/**
 * @author manjunathm
 * This is the benClass for holding the approval Route display information
 */
public class ApprovalRouteDisplayBean implements BaseBean {

	private String proposalNumber;
	private String approvalStatus ;
	private String approvalStatusText ;
	private String userId;
	private String userName;
	private String primaryApproverFlag;
	private Integer levelNumber;
	private Integer stopNumber;
	private String updateTimeStamp;
	private Integer mapId;
	private String approverType ;
	private String approvalStatusImage ;
    private String bypassFlag;
    private String description;
    // Added for displaying user name for user Id
        private String approvalUserName;
    //End
        private String routingNumber;
        private Integer approverNumber;
	private int mapNumber;
        //Added for Case :# 3915 - can't see comments in routing in Approval Routing -Start
        private Date approvalDate;
        //Case :#3915 - End
        
        //COEUSQA:1445 - User Not able to View Routing Comments through Show Routing - Start
        private boolean isCommentsPresent;
        private boolean isAttachmentsPresent;
        //COEUSQA:1445 - End
        
        /**
         * @return returns isCommentsPresent
         */
        public boolean getIsCommentsPresent() {
            return isCommentsPresent;
        }
        /**
         * @param isCommentsPresent
         */
        public void setIsCommentsPresent(boolean isCommentsPresent) {
            this.isCommentsPresent = isCommentsPresent;
        }
        
        /**
         * @return returns isAttachmentsPresent
         */
        public boolean getIsAttachmentsPresent() {
            return isAttachmentsPresent;
        }
        /**
         * @param isAttachmentsPresent
         */
        public void setIsAttachmentsPresent(boolean isAttachmentsPresent) {
            this.isAttachmentsPresent = isAttachmentsPresent;
        }
        
	/**
	 * @return returns approvalStatus
	 */
	public String getApprovalStatus() {
		return approvalStatus;
	}
	/**
	 * @param approvalStatus 
	 */
	public void setApprovalStatus(String approvalStatus) {
		this.approvalStatus = approvalStatus;
	}
	/**
	 * @return returns level 
	 */
	public Integer getLevelNumber() {
		return levelNumber;
	}
	/**
	 * @param level
	 */
	public void setLevelNumber(Integer level) {
		this.levelNumber = level;
	}
	/**
	 * @return returns primaryApproverFlag 
	 */
	public String getPrimaryApproverFlag() {
		return primaryApproverFlag;
	}
	/**
	 * @param primaryApproverFlag
	 */
	public void setPrimaryApproverFlag(String primaryApproverFlag) {
		this.primaryApproverFlag = primaryApproverFlag;
	}
	/**
	 * @return returns stopNumber
	 */
	public Integer getStopNumber() {
		return stopNumber;
	}
	/**
	 * @param stopNumber
	 */
	public void setStopNumber(Integer stopNumber) {
		this.stopNumber = stopNumber;
	}
	/**
	 * @return returns userId
	 */
	public String getUserId() {
		return userId;
	}
	/**
	 * @param userId
	 */
	public void setUserId(String userId) {
		this.userId = userId;
	}
	/**
	 * @return returns approvalStatusImage
	 */
	public String getApprovalStatusImage() {
		return approvalStatusImage;
	}
	/**
	 * @param approvalStatusImage
	 */
	public void setApprovalStatusImage(String approvalStatusImage) {
		this.approvalStatusImage = approvalStatusImage;
	}	
	/**
	 * @return returns approverType
	 */
	public String getApproverType() {
		return approverType;
	}
	/**
	 * @param approverType
	 */
	public void setApproverType(String approverType) {
		this.approverType = approverType;
	}
		
        /**
         * Getter for property bypassFlag.
         * @return Value of property bypassFlag.
         */
        public java.lang.String getBypassFlag() {
            return bypassFlag;
        }
        
        /**
         * Setter for property bypassFlag.
         * @param bypassFlag New value of property bypassFlag.
         */
        public void setBypassFlag(java.lang.String bypassFlag) {
            this.bypassFlag = bypassFlag;
        }
		/**
		 * @return
		 */
		public Integer getMapId() {
			return mapId;
		}
		/**
		 * @param mapId
		 */
		public void setMapId(Integer mapId) {
			this.mapId = mapId;
		}
		/**
		 * @return
		 */
		public String getProposalNumber() {
			return proposalNumber;
		}
		/**
		 * @param proposalNumber
		 */
		/**
		 * @param proposalNumber
		 */
		public void setProposalNumber(String proposalNumber) {
			this.proposalNumber = proposalNumber;
		}
		/**
		 * @return
		 */
		public String getUpdateTimeStamp() {
			return updateTimeStamp;
		}
		/**
		 * @param updateTimeStamp
		 */
		public void setUpdateTimeStamp(String updateTimeStamp) {
			this.updateTimeStamp = updateTimeStamp;
		}
		/**
		 * @return
		 */
		public String getApprovalStatusText() {
			return approvalStatusText;
		}
		/**
		 * @param approvalStatusText
		 */
		public void setApprovalStatusText(String approvalStatusText) {
			this.approvalStatusText = approvalStatusText;
		}
		/**
		 * @return
		 */
		public String getUserName() {
			return userName;
		}
		/**
		 * @param userName
		 */
		public void setUserName(String userName) {
			this.userName = userName;
		}
		/**
		 * @return
		 */
		public String getDescription() {
			return description;
		}
		/**
		 * @param description
		 */
		public void setDescription(String description) {
			this.description = description;
		}
        // Added for displaying user name for user Id
                /**
                 * Getter for property approvalUserName.
                 * @return Value of property approvalUserName.
                 */
                public java.lang.String getApprovalUserName() {
                    return approvalUserName;
                }
                
                /**
                 * Setter for property approvalUserName.
                 * @param approvalUserName New value of property approvalUserName.
                 */
                public void setApprovalUserName(java.lang.String approvalUserName) {
                    this.approvalUserName = approvalUserName;
                }
        //End

    public int getMapNumber() {
        return mapNumber;
    }

    public void setMapNumber(int mapNumber) {
        this.mapNumber = mapNumber;
    }

    public String getRoutingNumber() {
        return routingNumber;
    }

    public void setRoutingNumber(String routingNumber) {
        this.routingNumber = routingNumber;
    }

    public Integer getApproverNumber() {
        return approverNumber;
    }

    public void setApproverNumber(Integer approverNumber) {
        this.approverNumber = approverNumber;
    }
   //Added for Case :# 3915 - can't see comments in routing in Approval Routing -Start
   /** Setter for property approvalDate.
     * @param approvalDate New value of property approvalDate.
     */
   public void setApprovalDate(Date approvalDate){
       this.approvalDate = approvalDate;
    }
   /** Getter for property approvalDate.
     * @return Value of property approvalDate.
     */
    public Date getApprovalDate(){
        return this.approvalDate;
    }  
    //Case :#3915 - End
}
