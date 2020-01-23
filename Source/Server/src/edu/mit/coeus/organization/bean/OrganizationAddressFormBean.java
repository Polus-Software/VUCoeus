/*
 * @(#)OrganizationAddressFormBeanBean.java 1.0 8/27/02
 *
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 */
package edu.mit.coeus.organization.bean;

/**
 * This class represent Organization Address Form Data Bean which is 
 * used to set and get the Organization address details like Organization name,
 * contactAddessID, Congressional District.
 *
 * @version :1.0 August 22,2002
 * @author Guptha K
 */

public class OrganizationAddressFormBean implements java.io.Serializable{

    private String organizationName;
    private int contactAddressId;
    private String congressionalDistrict;
    private String animalWelfareAssurance;

	/**
	 * Default Constructor
	 */

	public OrganizationAddressFormBean(){
    }

    /** 
	 *	This method is used to fetch the Organization Name.
	 *  @return String organizationName
	 */
	
	public String getOrganizationName() {
        return organizationName;
    }
	
	/**
	 *  This method is used to set the Organization Name with the given string.
	 *  @param organizationName String organizationName
	 */

	public void setOrganizationName(String organizationName) {
        this.organizationName = organizationName;
    }

    /** 
	 *	This method is used to fetch the Contact Address Id.
	 *  @return int contantAddressId
	 */

	public int getContactAddressId() {
        return contactAddressId;
    }

    /**
	 *	This method is used to set the contact address id
	 *  @param contactAddressId int value contact Address Id
	 */

	public void setContactAddressId(int contactAddressId) {
        this.contactAddressId = contactAddressId;
    }

    /**
	 *	This method is used to fetch the Congressional District.
	 *  @return String congressionalDistrict.
	 */

	public String getCongressionalDistrict() {
        return congressionalDistrict;
    }

    /**
	 * This method is used to set the congressional district.
	 * @param congressionalDistrict string value congressional District
	 */

	public void setCongressionalDistrict(String congressionalDistrict) {
        this.congressionalDistrict = congressionalDistrict;
    }

    /*	This method is used to fetch the animalWelfareAssurance.
     *  @return String animalWelfareAssurance
     */
    public String getAnimalWelfareAssurance() {
        return animalWelfareAssurance;
    }

    /*  This method is used to set the animalWelfareAssurance.
     *  @param organizationName String animalWelfareAssurance
     */
    public void setAnimalWelfareAssurance(String animalWelfareAssurance) {
        this.animalWelfareAssurance = animalWelfareAssurance;
    }
}