/*
 * finEntityDataMatrixBean.java
 *
 * Created on April 1, 2008, 10:33 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package edu.dartmouth.coeuslite.coi.beans;

/**
 *
 * @author blessy
 */
public class FinEntMatrixBean {
    
    /** Creates a new instance of finEntityDataMatrixBean */
    public FinEntMatrixBean() {
    }
    private String columnName;
    private String columnLabel;
    private String guiType;
    private String lookupArgument;
    private Integer dataGroupId;
    private int columnSortId;
    private int groupSortId;
    private char statusFlag;

    public String getColumnName() {
        return columnName;
    }

    public void setColumnName(String columnName) {
        this.columnName = columnName;
    }

    public String getColumnLabel() {
        return columnLabel;
    }

    public void setColumnLabel(String columnLabel) {
        this.columnLabel = columnLabel;
    }

    public String getGuiType() {
        return guiType;
    }

    public void setGuiType(String guiType) {
        this.guiType = guiType;
    }

    public String getLookupArgument() {
        return lookupArgument;
    }

    public void setLookupArgument(String lookupArgument) {
        this.lookupArgument = lookupArgument;
    }

    public Integer  getDataGroupId() {
        return dataGroupId;
    }

    public void setDataGroupId(Integer dataGroupId) {
        this.dataGroupId = dataGroupId;
    }

    public char getStatusFlag() {
        return statusFlag;
    }

    public void setStatusFlag(char statusFlag) {
        this.statusFlag = statusFlag;
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
