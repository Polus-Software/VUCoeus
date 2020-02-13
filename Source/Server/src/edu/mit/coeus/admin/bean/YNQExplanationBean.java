/**
 * @(#)YNQExplanationBean.java 1.0 8/22/02
 *
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 */
package edu.mit.coeus.admin.bean;

/**
 * @version :1.0 August 22,2002
 * @author jobinelias
 */

public class YNQExplanationBean extends YNQBaseBean implements java.io.Serializable{

    //private String questionId;
    private String explanationType;
    private String explanation;

	/**
	 *	Default Constructor
	 */
	 
    public YNQExplanationBean(){
    }

	/**
	 *	This method is used to fetch the Explanation
	 *	@return String explanation
	 */

    public String getExplanation() {
        return explanation;
    }

	/**
	 *	This method is used to set the Explanation
	 *	@param explanation String 
	 */

    public void setExplanation(String explanation) {
        this.explanation = explanation;
    }

	/**
	 *	This method is used to fetch the Question Id
	 *	@return String questionId
	 */
/*
    public String getQuestionId() {
        return questionId;
    }*/

	/**
	 *	This method is used to set the Question Id
	 *	@param questionId String 
	 */
/*
    public void setQuestionId(String questionId) {
        this.questionId = questionId;
    } */


	/**
	 *	This method is used to fetch the Explanation Type
	 *	@return String explanationType
	 */

    public String getExplanationType() {
        return explanationType;
    }

	/**
	 *	This method is used to set the Explanation Type
	 *	@param explanationType String 
	 */

    public void setExplanationType(String explanationType) {
        this.explanationType = explanationType;
    }
	
	public boolean equals(Object obj) {
        YNQExplanationBean bean = (YNQExplanationBean)obj;
        if(bean.getQuestionId().equals(getQuestionId()) &&
            bean.getExplanationType().equals(getExplanationType())) {
            return true;
        }else{
            return false;
        }
    }
}
