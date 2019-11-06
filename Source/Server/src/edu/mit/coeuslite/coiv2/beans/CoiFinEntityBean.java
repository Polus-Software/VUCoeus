/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package edu.mit.coeuslite.coiv2.beans;

/**
 *
 * @author twinkle
 */
public class CoiFinEntityBean {
    private String code;
    private int sequenceNum;
    private int statusCode;
    private String statusDesc;
    private String description;
    private int entityTypeCode;

    /**
     * @return the code
     */
    public String getCode() {
        return code;
    }

    /**
     * @param code the code to set
     */
    public void setCode(String code) {
        this.code = code;
    }

    /**
     * @return the sequenceNum
     */
    public int getSequenceNum() {
        return sequenceNum;
    }

    /**
     * @param sequenceNum the sequenceNum to set
     */
    public void setSequenceNum(int sequenceNum) {
        this.sequenceNum = sequenceNum;
    }

    /**
     * @return the statusCode
     */
    public int getStatusCode() {
        return statusCode;
    }

    /**
     * @param statusCode the statusCode to set
     */
    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    /**
     * @return the statusDesc
     */
    public String getStatusDesc() {
        return statusDesc;
    }

    /**
     * @param statusDesc the statusDesc to set
     */
    public void setStatusDesc(String statusDesc) {
        this.statusDesc = statusDesc;
    }

    /**
     * @return the description
     */
    public String getDescription() {
        return description;
    }

    /**
     * @param description the description to set
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * @return the entityTypeCode
     */
    public int getEntityTypeCode() {
        return entityTypeCode;
    }

    /**
     * @param entityTypeCode the entityTypeCode to set
     */
    public void setEntityTypeCode(int entityTypeCode) {
        this.entityTypeCode = entityTypeCode;
    }

}
