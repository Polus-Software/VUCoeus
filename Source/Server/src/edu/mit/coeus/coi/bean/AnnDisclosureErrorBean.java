/*
 * @(#)AnnDisclosureErrorBean.java 1.0 6/10/02
 *
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 */

package edu.mit.coeus.coi.bean;

import edu.mit.coeus.utils.UtilFactory;

/**
 * <code>AnnDisclosureErrorBean</code> is a class to hold the information of errors
 * occured while updating reveiwed Disclosures.
 *
 * @version 1.0 June 10,2002
 * @author Phaneendra Kumar.
 */
public class AnnDisclosureErrorBean {

    private String disclosureNumber;    /* the returned disclosurenumber.                   */
    private String entityName;          /* the returned entity naeme                                                 */
    private String entityNumber;        /* the retunred entity Number.                      */
    private int noOfRowsEffected;       /* no of rows effected by the procedure executed.   */
    private String errorMessage;        /* error message returned by exception.             */
    private String errorCode;           /* error code returned by exception.                */

    /** Creates new AnnDisclosreErrorBean */
    public AnnDisclosureErrorBean() {
    }

    /**
     * Gets the Disclosre Number of the Error returned
     *
     * @return returns the disclosureNumber of the Error info returned
     *  after batch updation of the AnnualDisclosures
     */
    public String getDisclosureNumber(){
        return disclosureNumber;
    }

    /**
     * Sets the Disclosure Number of the Error returned
     *  after batch updation of the AnnualDisclosures
     *
     * @param disclosureNumber
     */
    public void setDisclosureNumber(String disclosureNumber){
        this.disclosureNumber = UtilFactory.checkNullStr(disclosureNumber);
    }

    /**
     * Gets the Entity Number of the error returned
     * after batch updation of the AnnualDisclosures
     * @return returns the entity number
     */
    public String getEntityNumber(){
       return entityNumber;
    }

    /**
     * Sets the Entity Number of the Error info returned
     * after batch updation of the AnnualDisclosures
     * @param EntityNumber
     */
    public void setEntityNumber(String entityNumber){
       this.entityNumber = UtilFactory.checkNullStr(entityNumber);
    }

    /**
     * Gets the Entity Name
     * @return entityName
     */
    public String getEntityName(){
       return entityName;
    }

    /**
     * Sets the Entity Name
     * @param entityName
     */
    public void setEntityName(String entityName){
       this.entityName = UtilFactory.checkNullStr(entityName);
    }

    /**
     * Gets the information about <code>number of rows effected</code>
     * @return noOfRowsEffected
     */
    public int getNoOfRowsEffected(){
        return noOfRowsEffected;
    }

    /**
     * This method sets <code>number of rows effected</code>
     * @param noOfRowsEffected
     */
    public void setNoOfRowsEffected(int noOfRowsEffected){
       this.noOfRowsEffected = noOfRowsEffected;
    }

    /**
     * Gets the Error Message
     * @return errorMessage
     */
    public String getErrorMessage(){
        return errorMessage;
    }

    /**
     * Sets the Error Message
     * @param errorMessage
     */
    public void setErrorMessage(String errorMessage){
       this.errorMessage = UtilFactory.checkNullStr(errorMessage);
    }

    /**
     * Gets the Error Code
     * @return errorCode
     */
     public String getErrorCode(){
         return errorCode;
     }

    /**
     * Sets the Error Code
     * @param errorCode
     */
    public void setErrorCode(String errorCode){
        this.errorCode = UtilFactory.checkNullStr(errorCode);
    }
}
