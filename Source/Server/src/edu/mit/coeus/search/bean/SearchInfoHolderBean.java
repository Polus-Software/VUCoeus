/*
 * @(#)SearchInfoHolderBean.java
 *
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 *
 * Created on July 1, 2002, 12:02 PM
 * @author  Geo Thomas
 * @version 1.0
 */

package edu.mit.coeus.search.bean;

import java.util.Vector;
import java.util.Hashtable;
/**
 * The class used to hold all the information for a particular search.
 */
public class SearchInfoHolderBean implements java.io.Serializable{

    //holds query string
    private String queryString;
    //holds default where clause
    private String whereClause;
    //holds display list
    private Vector displayList;
    //holds criteria list
    private Vector criteriaList;
    //holds orderBy clause
    private String orderBy;
    //holds remaining clause
    private String remClause;
    //holds parameters
    private Vector params;
    //holds procedure name
    private String procedureName;
    //holds display label
    private String displayLabel;
    //holds max num rows
    private String retrieveLimit;
    //holds primary key for a Search
    private String primaryKeyColumn;
    //holds custom query list
    private Vector customQueryList;//Added by Nadh for CoeusSearch enhancement(CustomQuery)   start : 3-aug-2005
    
    private DefaultSelectBean defaultSelectBean;
    public static int NUMBER_OF_ROWS = 25;
    
    /** Creates new SearchInfoHolderBean */
    public SearchInfoHolderBean() {
    }

    /**
     *  The method used to get the query string
     *  @return query string
     */
    public String getQueryString() {
        return queryString;
    }
    
    /**
     *  The method used to set the query string
     *  @param query string
     */
    public void setQueryString(java.lang.String queryString) {
        this.queryString = queryString;
    }
    
    /**
     *  The method used to get the display label
     *  @return display label
     */
    public String getDisplayLabel() {
        return displayLabel;
    }
    
    /**
     *  The method used to set the display label
     *  @param display label
     */
    public void setDisplayLabel(java.lang.String displayLabel) {
        this.displayLabel = displayLabel;
    }

    /**
     *  The method used to get the maximum number of rows to be displayed in the result page
     *  @return maximum number of rows
     */
    public String getRetrieveLimit() {
        return (retrieveLimit==null||retrieveLimit.trim().equals(""))?"40":retrieveLimit;
    }
    
    /**
     *  The method used to set the maximum number of rows be displayed on the result page
     *  @param maximum number of rows
     */
    public void setRetrieveLimit(java.lang.String retrieveLimit) {
        this.retrieveLimit = retrieveLimit;
    }

    /**
     *  The method used to get the where clause
     *  @return where clause
     */
    public String getWhereClause() {
        return whereClause==null?"":whereClause;
    }
    
    /**
     *  The method used to set the where clause
     *  @param where clause
     */
    public void setWhereClause(java.lang.String whereClause) {
        this.whereClause = whereClause;
    }

    /**
     *  The method used to get the criteria list
     *  @return criteria list
     */
    public Vector getCriteriaList() {
        return criteriaList;
    }
    
    /**
     *  The method used to set the criteria list
     *  @param criteria list
     */
    public void setCriteriaList(Vector criteriaList) {
        this.criteriaList = criteriaList;
    }

    /**
     *  The method used to get the display list
     *  @return display list
     */
    public Vector getDisplayList() {
        return displayList;
    }
    
    /**
     *  The method used to set the display list
     *  @param display list
     */
    public void setDisplayList(Vector displayList) {
        this.displayList = displayList;
    }
    
    /**
     *  The method used to get the oreder by clause
     *  @return oreder by clause
     */
    public String getOrderBy() {
        return orderBy;
    }
    
    /**
     *  The method used to get the oreder by clause
     *  @return oreder by clause
     */
    public void setOrderBy(String orderBy) {
        this.orderBy = orderBy;
    }
    
    /**
     *  The method used to get the remaining clause
     *  @return remaining clause
     */
    public String getRemClause() {
        return remClause==null?"":remClause;
    }
    
    /**
     *  The method used to set the remaining clause
     *  @param remaining clause
     */
    public void setRemClause(String remClause) {
        this.remClause = remClause;
    }

    /**
     *  The method used to get the parameters
     *  @return parameters
     */
    public Vector getParams() {
        return params;
    }
    
    /**
     *  The method used to set the parameters
     *  @param parameters
     */
    public void setParams(Vector params) {
        this.params = params;
    }
    
    /**
     *  The method used to get the procedure name
     *  @return procedure name
     */
    public String getProcedureName() {
        return procedureName;
    }
    
    /**
     *  The method used to set the procedure name
     *  @param procedure name
     */
    public void setProcedureName(String procedureName) {
        this.procedureName = procedureName;
    }
    
    /**
     * Overridden method of toString. It will form a string representation of
     * each element associated with this class.
     * @return Concatinated string representation of each element 
     */
    public String toString(){
        StringBuffer strBffr = new StringBuffer("");
        strBffr.append("Query String=>"+queryString);
        strBffr.append(";");
        strBffr.append("Display List=>"+displayList.toString());
        strBffr.append(";");
        strBffr.append("Oreder By=>"+orderBy);
        strBffr.append(";");
        strBffr.append("Params=>"+params.toString());
        strBffr.append(";");
        strBffr.append("Procedure Name=>"+procedureName);
        return strBffr.toString();
    }        

    /** Getter for property primaryKeyColumn.
     * @return Value of property primaryKeyColumn.
     */
    public java.lang.String getPrimaryKeyColumn() {
        return primaryKeyColumn;
    }    
    
    /** Setter for property primaryKeyColumn.
     * @param primaryKeyColumn New value of property primaryKeyColumn.
     */
    public void setPrimaryKeyColumn(java.lang.String primaryKeyColumn) {
        this.primaryKeyColumn = primaryKeyColumn;
    }
    
    /**
     * Getter for property defaultSelectBean.
     * @return Value of property defaultSelectBean.
     */
    public DefaultSelectBean getDefaultSelectBean() {
        return defaultSelectBean;
    }
    
    /**
     * Setter for property defaultSelectBean.
     * @param defaultSelectBean New value of property defaultSelectBean.
     */
    public void setDefaultSelectBean(DefaultSelectBean defaultSelectBean) {
        this.defaultSelectBean = defaultSelectBean;
    }
    
    //Added by Nadh for CoeusSearch enhancement(CustomQuery)   start : 3-aug-2005
    /**
     * Getter for property customQueryList.
     * @return Value of property customQueryList.
     */
    public java.util.Vector getCustomQueryList() {
        return customQueryList;
    }
    
    /**
     * Setter for property customQueryList.
     * @param customQueryList New value of property customQueryList.
     */
    public void setCustomQueryList(java.util.Vector customQueryList) {
        this.customQueryList = customQueryList;
    }
    //end 3-aug-2005
}