/**
 * @(#)AwardTermsBean.java 1.0 01/03/04
 *
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 */
package edu.mit.coeus.award.bean;
import java.util.Vector;

/**
 * This class is used to hold Award Terms data
 * This is a common beans for all Terms in Award
 *
 * @author Prasanna Kumar K
 * @version 1.0
 * Created on March 05, 2004 12:30 PM
 */

public class AwardTermsBean extends AwardBaseBean implements java.io.Serializable{
    
    private int termsCode;
    private String termsDescription;
    
    /**
     *	Default Constructor
     */
    
    public AwardTermsBean(){
    }    
    
    /**
     *	Copy Constructor
     */    
    public AwardTermsBean(TemplateTermsBean templateTermsBean){
        this.setTermsCode(templateTermsBean.getTermsCode());
        this.setTermsDescription(templateTermsBean.getTermsDescription());
    }      

    /** Getter for property termsCode.
     * @return Value of property termsCode.
     */
    public int getTermsCode() {
        return termsCode;
    }
    
    /** Setter for property termsCode.
     * @param termsCode New value of property termsCode.
     */
    public void setTermsCode(int termsCode) {
        this.termsCode = termsCode;
    }
    
    /** Getter for property termsDescription.
     * @return Value of property termsDescription.
     */
    public java.lang.String getTermsDescription() {
        return termsDescription;
    }
    
    /** Setter for property termsDescription.
     * @param termsDescription New value of property termsDescription.
     */
    public void setTermsDescription(java.lang.String termsDescription) {
        this.termsDescription = termsDescription;
    }    
    
    /** 
     * Equals method to check whether Primary Key values are same for the given Bean
     * @param obj Object
     * @return  boolean indicating whether the two beans are same or not
     */    
    public boolean equals(Object obj) {
        AwardTermsBean awardTermsBean = (AwardTermsBean)obj;
		
		if(equalsMitAwardNumber(awardTermsBean.getMitAwardNumber()) &&
		awardTermsBean.getSequenceNumber() == getSequenceNumber()){
			if(awardTermsBean.getTermsCode() == getTermsCode()){
				return true;
			}else{
				return false;
			}
		}else {
			return false;
		}
    }
	
	private boolean equalsMitAwardNumber(String mitAwardNumber) {
		if(mitAwardNumber == null && getMitAwardNumber() == null) {
			//System.out.println("true");
			return true;
		}else if(mitAwardNumber == null && getMitAwardNumber() != null) {
			//System.out.println("false");
			return false;
		}else if(mitAwardNumber != null && getMitAwardNumber() == null) {
			//System.out.println("false");
			return false;
		}else {
			//System.out.println(mitAwardNumber.equals(getMitAwardNumber()));
			return mitAwardNumber.equals(getMitAwardNumber());
		}
	}
	
}