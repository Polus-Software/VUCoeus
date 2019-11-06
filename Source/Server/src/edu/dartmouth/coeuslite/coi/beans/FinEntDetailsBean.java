/*
 * FinEntDetailsBean.java
 *
 * Created on April 2, 2008, 12:43 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package edu.dartmouth.coeuslite.coi.beans;

import java.sql.Date;
import java.sql.Timestamp;

/**
 *
 * @author blessy
 */
public class FinEntDetailsBean {
    
    /** Creates a new instance of FinEntDetailsBean */
    public FinEntDetailsBean() {
    }
    private String entityNumber;
    private Integer seqNumber;
    private String columnName;
    private String columnValue;
    private int columnSortId;
    private int groupSortId;
    private Integer rlnType;
    private String comments;
    private java.sql.Date updateTimestamp;
    private String updateUser;
    private String acType;

    public String getEntityNumber() {
        return entityNumber;
    }

    public void setEntityNumber(String entityNumber) {
        this.entityNumber = entityNumber;
    }

    public Integer getSeqNumber() {
        return seqNumber;
    }

    public void setSeqNumber(Integer seqNumber) {
        this.seqNumber = seqNumber;
    }

    public String getColumnName() {
        return columnName;
    }

    public void setColumnName(String columnName) {
        this.columnName = columnName;
    }

    public String getColumnValue() {
        return columnValue;
    }

    public void setColumnValue(String columnValue) {
        this.columnValue = columnValue;
    }

    public Integer getRlnType() {
        return rlnType;
    }

    public void setRlnType(Integer rlnType) {
        this.rlnType = rlnType;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public Date getUpdateTimestamp() {
        return updateTimestamp;
    }

    public void setUpdateTimestamp(Date updateTimestamp) {
        this.updateTimestamp = updateTimestamp;
    }

    public String getUpdateUser() {
        return updateUser;
    }

    public void setUpdateUser(String updateUser) {
        this.updateUser = updateUser;
    }

    public String getAcType() {
        return acType;
    }

    public void setAcType(String acType) {
        this.acType = acType;
    }

    public int getColumnSortId() {
        return columnSortId;
    }

    public void setColumnSortId(int columnSortId) {
        this.columnSortId = columnSortId;
    }

    public int getGroupSortId() {
        return groupSortId;
    }

    public void setGroupSortId(int groupSortId) {
        this.groupSortId = groupSortId;
    }
    
}
