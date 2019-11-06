/*
 * @(#)SearchQueryBean.java
 *
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 *
 * Created on July 15, 2002, 3:24 PM
 * @author  Geo Thomas
 * @version 1.0
 * @modified by Sagin
 * @date 28-10-02
 * Description : Implemented Standard Error Handling. 
 *
 */

/* PMD check performed, and commented unused imports and variables on 07-MAY-2007
 * by Noorul
 */
package edu.mit.coeus.search.bean;

import java.util.Hashtable;
import java.util.Vector;
import java.util.Enumeration;
import java.util.HashMap;

import edu.mit.coeus.utils.UtilFactory;
import edu.mit.coeus.search.exception.CoeusSearchException;

import edu.mit.coeus.utils.dbengine.DBEngineImpl;
import edu.mit.coeus.utils.dbengine.DBException;
import edu.mit.coeus.utils.dbengine.Parameter;
import edu.mit.coeus.exception.CoeusException;
import edu.mit.coeus.utils.CoeusFunctions;
import edu.mit.coeus.utils.CoeusProperties;
import edu.mit.coeus.utils.CoeusPropertyKeys;

import edu.mit.coeus.utils.DateUtils;
//import java.io.IOException;

/**
 *  The class used to build the query,execute it and parse the result set into 
 * a hashtable. To process the query it will be making use of the SearchInfoHolder 
 * class object.
 */
public class SearchExecutionBean {

    private Hashtable columnList;
    
    private String customQuery;//Added by Nadh for CustomQuery Search enhancement   start : 3-aug-2005
    
    private SearchInfoHolderBean searchInfoHolder;
    
//    private UtilFactory UtilFactory;
    
    private DBEngineImpl dbEngine;
    
    private DateUtils dtUtils = new DateUtils();
    
    private static final String BETWEEN = "BETWEEN";
    private static final String INVALID_SEARCH = "Invalid Search criteria in row number: ";
    private static final String LIKE = "LIKE";
    
    /** 
     * Creates new SearchQueryBean 
     *  @param SearchInfoHolder object
     */
    public SearchExecutionBean(SearchInfoHolderBean searchInfoHolder) {
        this.searchInfoHolder = searchInfoHolder;
        columnList = new Hashtable();
//        UtilFactory = new UtilFactory();
        dbEngine = new DBEngineImpl();
    }

    /**
     *  Method used to add columns to the column list
     *  @param ColumnBean instance
     */
    public void addColumn(ColumnBean column) {
        columnList.put(column.getName(),column);
    }
    /**
     *  Method used to add columns to the column list
     *  @param ColumnBean instance
     */
    public void setColumnList(Vector columns) {
        int colCnt = columns.size();
        for(int colIndex=0;colIndex<colCnt;colIndex++){
            ColumnBean column = (ColumnBean)columns.elementAt(colIndex);
            columnList.put(column.getName(),column);
        }
    }
    
    /**
     *  Method used to add attribute bean instance to a column object
     *  @param ColumnBean instance
     *  @param AttributeBean instance
     */
    public void addAttribute(ColumnBean column, AttributeBean attribute) {
        column.addAttribute(attribute);
    }

    /*
     *Commented by Geo on 07-Mar-2005.
     * No need to execute a separate query to get the count
     */
//    /**
//     *  Method used to get the number of rows to retrieved from a select clause.
//     *  To get the row count it uses <code>fn_get_row_count_for_query</code>
//     *  stored function.This method will be invoked before executing the query.
//     *  It will handle the DBException and set row count to '0' if any DBException occured.
//     *  @param query string to be executed
//     */
//    public int getRowCount(String buildQuery) throws CoeusException, DBException{
//        String rowCount = "0";
//        Vector param= new java.util.Vector();
//        param.addElement(new Parameter("QUERY","String",buildQuery));
//        Vector result = new Vector();
//        //calling stored function
//        if(dbEngine!=null){
//            result = dbEngine.executeFunctions("Coeus",
//            "{ <<OUT INTEGER ROWCOUNT>> = call fn_get_row_count_for_query( <<QUERY>> ) }",
//            param);
//        }else{
//            throw new CoeusException("db_exceptionCode.1000");
//        }
//        if(!result.isEmpty()){
//            HashMap rowCntRow = (HashMap)result.elementAt(0);
//            rowCount = rowCntRow.get("ROWCOUNT").toString();
//        }
//        return Integer.parseInt(rowCount);
//    }
    /**
     *  Method used to execute the query. The result will be bundled in a 
     * two dimensional <code>vector</code>, which will be packed in a hashtable with
     * the reference <code>'reslist'</code> and display lables for each column will
     * be an another vector in the same hashtable with the refernece <code>displaylabels</code>
     *  @return Hashtable which has three elements<br>
     *  1) Row count
     *  2) Display label list
     *  3) Result set
     * @exception CoeusSearchException
     */
    public Hashtable executeSearchQuery() 
                throws CoeusException, CoeusSearchException, DBException, Exception{
        Hashtable fullDisplayList = new Hashtable();
        Vector displayList = searchInfoHolder.getDisplayList();
//        Vector criteriaList = searchInfoHolder.getCriteriaList();
//        Vector resRowList = new Vector();
        Vector result = null;
        Vector param= new java.util.Vector();
        String buildQuery = buildQuery();
        /*
         *Commented by Geo
         *  No need to execute a separate query to get the row count.
         *  Since the where clause is the same for the row count query
         *  No gain in performance rather, it dowubles the time since there are two 
         *  DB executions.
         */
//        /* To get the rowcount*/
//        CriteriaBean firstCriteriaBean = (CriteriaBean)criteriaList.firstElement();
//        String firstCriteriaName = firstCriteriaBean.getName();
//        int fromIndex = buildQuery.toUpperCase().indexOf("FROM",0);
//        String selectSubQuery = buildQuery.substring(0,fromIndex);
//        //Added to get count of Primary key for each search - start
//        //String newSelectClause = "Select count("+firstCriteriaName+") "+buildQuery.substring(fromIndex);
//        String searchPrimaryKey = searchInfoHolder.getPrimaryKeyColumn();
//        if(searchPrimaryKey==null || searchPrimaryKey.equals("")){
//            //System.out.println("Primary Key Not Specified!!!");
//            searchPrimaryKey = firstCriteriaName;
//        }
//        String newSelectClause = "Select count("+searchPrimaryKey+") "+buildQuery.substring(fromIndex);
//        //Added to get count of Primary key for each search - end
//        int rowCount = getRowCount(newSelectClause);
        //System.out.println("select rowcount=>"+rowCount);
//        /*end block*/
        int retrieveLimit = 0;
        //Added on April 17, 2004 - start
        /*
        try{
            retrieveLimit = Integer.parseInt(searchInfoHolder.getRetrieveLimit());
        }catch(NumberFormatException numEx){
            UtilFactory.log(numEx.getMessage(),numEx,"executeSearchQuery","SearchExecutionBean");
            retrieveLimit = 250;
            //throw new CoeusSearchException("Please specify the retrieve limit for the search");
        }*/
        CoeusFunctions coeusFunctions = new CoeusFunctions();
        String retrieveLimitFromDB = null;
        if(searchInfoHolder.getRetrieveLimit()!=null && !searchInfoHolder.getRetrieveLimit().equals("")){
            retrieveLimitFromDB = coeusFunctions.getParameterValue(searchInfoHolder.getRetrieveLimit());
            retrieveLimit = retrieveLimitFromDB == null ? 250 : Integer.parseInt(retrieveLimitFromDB);
        }else{
            retrieveLimit = 250;
        }
//        //Added on April 17, 2004 - end
//        }else{
        if(dbEngine!=null){
            try{
                param.addElement(new Parameter("QUERY","String",buildQuery));
                StringBuffer sqlCommand = new StringBuffer("call ");
                sqlCommand.append(searchInfoHolder.getProcedureName());
                sqlCommand.append("( <<QUERY>> , <<OUT RESULTSET rset>> )   ");
               // System.out.println("sql command=>"+sqlCommand.toString());
               // System.out.println("sql PARAM =>"+param);
                result = dbEngine.executeRequest("Coeus", sqlCommand.toString(), 
                                "Coeus", param);
                int rowCount = result.size();
                if(rowCount==0){
                    throw new CoeusSearchException("No rows found with the current selection criteria");
                }else if(rowCount>retrieveLimit){
                    fullDisplayList.put("rowcount",""+rowCount);
                    throw new CoeusSearchException("The current selection criteria will "+
                        "retrieve "+rowCount+" rows.\n"+
                        "This is more than the limit of "+retrieveLimit+
                        //" set in Resource File.\nPlease modify the selection criteria to "+
                        " rows set in Parameter Maintenance.\nPlease modify the selection criteria to "+
                        "retrieve fewer rows.");
                }
            }catch(DBException dbEx){
//                String messages = dbEx.getMessage();
//                int id = dbEx.getErrorId();
//                if( id == 00911){
//                    messages = "No rows found with the current selection criteria";
//                }
                UtilFactory.log(dbEx.getMessage(),dbEx,"executeSearchQuery",
                "SearchExecutionBean");
                throw new CoeusSearchException(dbEx.getMessage());
            }
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }
        //resDisplayList.addElement(displayList);
            /*int dispCount = displayList.size();
            for(int k=0;k<result.size();k++){
                Hashtable searchResultRow = (Hashtable)result.elementAt(k);
                Vector resultColumn = new Vector(3,2);
                for(int dispIndex=0;dispIndex<dispCount;dispIndex++){
                    DisplayBean display = (DisplayBean)displayList.elementAt(dispIndex);
                    String fieldName = display.getName();
                    String resValue = null;
                    try{
                        resValue = searchResultRow.get(fieldName).toString();
                    }catch(NullPointerException nEx){
                        UtilFactory.log(nEx.getMessage(),nEx,"executeSearchRequest",
                            "SearchExecutionBean");
                        throw new CoeusSearchException("Please check the entries in "+
                            "the DISPLAY element : "+fieldName);
                    }
                    resultColumn.addElement(resValue.trim().equals("null")?"":resValue);
                }
                resRowList.addElement(resultColumn);
            }
             */
        //        }
        fullDisplayList.put("displaylabels",displayList);
        //fullDisplayList.put("reslist",resRowList);
        DefaultSelectBean defaultSelectionBean = searchInfoHolder.getDefaultSelectBean();
        if( result != null && defaultSelectionBean != null ){
            int resRows = result.size();
            for( int indx = 0; indx < resRows ; indx++ ){
                HashMap hmResultRow = (HashMap)result.get(indx);
                hmResultRow.put("ID", hmResultRow.get(defaultSelectionBean.getId()));
                hmResultRow.put("NAME", hmResultRow.get(defaultSelectionBean.getName()));
            }
        }
        fullDisplayList.put("reslist",result);
        return fullDisplayList;
    }
    /**
     *  Method used to set the SearchInfoHolderBean instance
     *  @param SearchInfoHolderBean
     */
    public void setSearchInfoHolder(SearchInfoHolderBean searchInfoHolder) {
        this.searchInfoHolder = searchInfoHolder;
    }
    /**
     *  Method used to form the query by parsing the values from the column objects
     * and attribute objects.
     * @return query for the search
     * @exception CoeusSearchException
     */
    private String buildQuery() throws CoeusSearchException{
        if(searchInfoHolder==null){
            throw new CoeusSearchException("Necessary information for building "+
                "the query is not available");
        }
        StringBuffer query = new StringBuffer(searchInfoHolder.getQueryString());
        StringBuffer whereClause = new StringBuffer(searchInfoHolder.getWhereClause());
        if(columnList!=null){
            //set to true if row has been changed
            boolean rowChanged = false;
            //set to true if atleast search criteria has been entered
//            boolean attExistsFlag = false;
            //set to true if any ORs happen between rows
            boolean orFlag = false;
            //set to true if any ANDs happen between columns
            boolean andFlag = false;
            //set to truw if where clause has been formed
            boolean whereFlag = false;
            boolean firstElementFlag = true;
            //to hold the joins
            Hashtable joinQueryList = new Hashtable();
            //to hold the tables
            Hashtable tableQueryList = new Hashtable();
            int criteriaCount = 0;
            int numberOfRows = SearchInfoHolderBean.NUMBER_OF_ROWS;
            for(int rowIndex=0;rowIndex<numberOfRows;rowIndex++){
                rowChanged = true;
                Vector criteriaList = searchInfoHolder.getCriteriaList();
                criteriaCount = criteriaList.size();
                   
                for(int criteriaIndex=0;criteriaIndex<criteriaCount;criteriaIndex++){
                    CriteriaBean criteria = (CriteriaBean)criteriaList.elementAt(criteriaIndex);
                    String criteriaName = criteria.getName().trim();
                    String alias = criteria.getAlias();
                    ColumnBean column = (ColumnBean)columnList.get(criteriaName);
                    if(column!=null){
                        Hashtable attList = column.getAttList();
                        AttributeBean attribute = (AttributeBean)attList.get(""+rowIndex);
                        if(attribute!=null){
                            String attValue = attribute.getAttValue();
                            String valueType = attribute.getValueType();
                            // code commented for coeus4.3 search between two dates enhancements, 
                            // And this code is added in SearchUtil.java class 
//                            //case 2042 start
//                            int index = valueType.indexOf("=");
//                            if(index != -1 && index != 0){
//                                throw new CoeusSearchException(
//                                    "Invalid Search criteria in row number: "+(rowIndex+1));
//                            }//case 2042 End
                            if(valueType.toUpperCase().indexOf("AND")!=-1){
                                throw new CoeusSearchException(
                                    INVALID_SEARCH+(rowIndex+1));
                            }
                            if(attValue!=null){
//                                attExistsFlag = true;
                                if(whereClause.toString().toUpperCase().indexOf("WHERE")==-1){
                                    whereClause.append(" WHERE ((");
                                    whereFlag = true;
                                }else if(!firstElementFlag && rowChanged){
                                    whereClause.append(") OR (");
                                    orFlag = true;
                                }else{
                                    whereClause.append((!whereFlag && !andFlag)?" AND ((":" AND ");
                                    andFlag=true;
                                }
                                firstElementFlag = false;
                                String join = criteria.getJoin();
                                if(join!=null && !join.trim().equals("") && 
                                        joinQueryList.get(criteriaName)==null){
                                    joinQueryList.put(criteriaName,join);
                                }
                                String table = criteria.getTable();
                                if(table!=null && !table.trim().equals("") && 
                                        tableQueryList.get(criteriaName)==null){
                                    tableQueryList.put(criteriaName,table);
                                }
                                // Code modified for coeus4.3 search between two dates enhancements - starts
                                // if the search creteria is having between and it is a date search
                                // then UPPER should not be added
                                if(valueType.toUpperCase().indexOf(BETWEEN)!=-1 && criteria.isDate()){
                                    whereClause.append((((alias!=null)&&(!alias.trim().equals("")))?alias:criteriaName)+" ");
                                } else {
                                    whereClause.append("UPPER("+
                                        (((alias!=null)&&(!alias.trim().equals("")))?
                                                alias:criteriaName)+")");
                                }
                                // Code modified for coeus4.3 search between two dates enhancements - ends
                                whereClause.append(valueType);
                                /* begin fixID: 190_2 */
                               /* modified on 03-MAR-2003 to 
                                 allow to check using LIKE though the
                                 criteria type is either number or date */
                                if(criteria.isNumber() && valueType.toUpperCase().indexOf(LIKE) == -1){
                                    whereClause.append(""+attValue);
                                // Code modified for coeus4.3 search between two dates enhancements
                                }else if(criteria.isDate() && 
                                    ( !valueType.equalsIgnoreCase("IS NULL") && valueType.toUpperCase().indexOf(LIKE) == -1 )
                                    && valueType.toUpperCase().indexOf(BETWEEN)==-1){
                                /* end fixID: 190_2 */    
                                    /* if user wants to search  null values in data column 
                                     * then dont parse the date
                                     */
                                    /* modified by ravi on 19-02-03  to fix the bug with id: #153*/
                                    /* as we are not parsing the date to dd-mon-yyyy from
                                       search GUI screen we have to parse now */
                                    whereClause.append("to_date("+
                                        UtilFactory.checkNull(dtUtils.formatDate(attValue.trim(),"/-:,","dd-MMM-yyyy"))+",\'dd-MON-yyyy\')");
                                // Code modified for coeus4.3 search between two dates enhancements - starts
                                } else if(valueType.toUpperCase().indexOf(BETWEEN)!=-1){
                                    whereClause.append(" "+formateAttributeValue(attValue, criteria.isDate(), rowIndex));
                                // Code modified for coeus4.3 search between two dates enhancements - ends
                                } else if( !valueType.equalsIgnoreCase("IS NULL")){
                                    whereClause.append(UtilFactory.checkNull(attValue));
                                }
/*                                whereClause.append(
                                    criteria.isNumber()?(""+attValue):(UtilFactory.checkNull(attValue)));
                                whereClause.append(
                                    criteria.isDate()?("to_date("+attValue+",\"dd-Mon-yyyy\""):"");
 */
                                for(int inLoopIndex=(rowIndex+1);inLoopIndex<numberOfRows;inLoopIndex++){
                                    AttributeBean tempAttr = 
                                        (AttributeBean)attList.get(""+inLoopIndex);
                                        if(tempAttr!=null){
                                            attValue = tempAttr.getAttValue();
                                            valueType = tempAttr.getValueType();
                                            int andIndex = -1;
                                            if(attValue!=null && 
                                                (andIndex = valueType.trim().toUpperCase().indexOf("AND"))!=-1){
                                                    whereClause.append(" AND ");
                                                    // Code modified for coeus4.3 search between two dates enhancements - starts
                                                    if(valueType.toUpperCase().indexOf(BETWEEN)==-1){
                                                        whereClause.append("UPPER("+
                                                            (((alias!=null)&&(!alias.trim().equals("")))?
                                                                    alias:criteriaName)+")");
                                                    } else {
                                                        whereClause.append((((alias!=null)&&(!alias.trim().equals("")))?alias:criteriaName));                                    
                                                    }
                                                    // Code modified for coeus4.3 search between two dates enhancements - ends
                                                    whereClause.append(" "+valueType.substring(andIndex+3)+" ");
/*                                                    whereClause.append(
                                                        criteria.isNumber()?
                                                        (""+attValue):(UtilFactory.checkNull(attValue)));
                                                    whereClause.append(
                                                        criteria.isDate()?
                                                        ("to_date("+attValue+",\"dd-Mon-yyyy\""):"");
 */                                                 
                                                    /* begin fixID: 190_2 */
                                                   /* modified on 03-MAR-2003 to 
                                                     allow to check using LIKE though the
                                                     criteria type is either number or date */
                                                    if(criteria.isNumber()  && valueType.toUpperCase().indexOf(LIKE) == -1){
                                                        whereClause.append(""+attValue);
                                                    // Code modified for coeus4.3 search between two dates enhancements
                                                    }else if(criteria.isDate()  
                                                        && ( !valueType.equalsIgnoreCase("IS NULL") && valueType.toUpperCase().indexOf(LIKE) == -1 )
                                                        && valueType.toUpperCase().indexOf(BETWEEN)==-1){
                                                     /* end fixID: 190_2 */   
                                                        /* if user wants to search  null values in data column 
                                                         * then dont parse the date
                                                         */
                                                        /* modified by ravi on 19-02-03  to fix the bug with id: #153*/
                                                        /* as we are not parsing the date to dd-mon-yyyy from
                                                           search GUI screen we have to parse now */
                                                        whereClause.append("to_date("+
                                                            UtilFactory.checkNull(dtUtils.formatDate(attValue.trim(),"/-:,","dd-MMM-yyyy"))+",\'dd-MON-yyyy\')");
                                                    // Code modified for coeus4.3 search between two dates enhancements - starts
                                                    } else if(valueType.toUpperCase().indexOf(BETWEEN)!=-1){
                                                        whereClause.append(" "+formateAttributeValue(attValue, criteria.isDate(), rowIndex));
                                                    // Code modified for coeus4.3 search between two dates enhancements - ends
                                                    } else{
                                                        whereClause.append(UtilFactory.checkNull(attValue));
                                                    }
                                                    attList.remove(""+inLoopIndex);
                                            }
                                        }
                                }
                                    
                                rowChanged = false;
                                //System.out.println("query on the fly=>"+whereClause.toString());
                            }
                        }
                    }
                }
            }
            if(criteriaCount !=0){
            //modified by Nadh for CustomQuery Search enhancement   start : 3-aug-2005
            if((customQuery ==null || customQuery.length() < 0) ) {
                
                if(!whereFlag){
                    whereClause.append(")");
                }else if(whereFlag && orFlag){
                    whereClause.append(")");
                }else if(!orFlag){
                    whereClause.append(")");
                }
                whereClause.append(")");
            }else {
                whereClause.append(" AND ");
                whereClause.append(customQuery);
            }//end 3-aug-2005
            }
            Enumeration tableEnum = tableQueryList.keys();
            while(tableEnum!=null && tableEnum.hasMoreElements()){
                String tableCriteriaName = (String)tableEnum.nextElement();
                query.append(" , ");
                query.append(tableQueryList.get(tableCriteriaName));
            }
            query.append(whereClause.toString());
            
            Enumeration joinEnum = joinQueryList.keys();
            while(joinEnum!=null && joinEnum.hasMoreElements()){
                String joinCriteriaName = (String)joinEnum.nextElement();
                query.append(" AND (");
                query.append(joinQueryList.get(joinCriteriaName));
                query.append(")");
            }
            String orderBy = searchInfoHolder.getOrderBy();
            if(orderBy!=null && !orderBy.trim().equals("")){
                query.append("ORDER BY "+orderBy);
            }
            //JIRA COEUSQA-3014 - START
            query.append(" ").append(searchInfoHolder.getRemClause()); //20101201 David Harrison, Brown University. We need to add a space before appending the remclause to the rest of the query
            //JIRA COEUSQA-3014 - END
        }
        
        //System.out.println("query=>"+query.toString());
        //log the query
        try{
        if(CoeusProperties.getProperty(CoeusPropertyKeys.SEARCH_DEBUG,"No").equalsIgnoreCase("Yes"))
            UtilFactory.log("Query=>"+query.toString(),null,null,null);
        }catch(Exception ex){
            //do nothing
        }
        return query.toString();
    }
    /**
     * Overridden method of toString. It will form a string representation of
     * each element associated with this class.
     * @return Concatinated string representation of each element 
     */
    public String toString(){
        StringBuffer strBffr = new StringBuffer("");
        strBffr.append("Columns=>"+columnList.toString());
        strBffr.append(";");
        strBffr.append("SearchInfoHolder=>"+searchInfoHolder.toString());
        return strBffr.toString();
    }
    
    //Added by Nadh for CustomQuery Search enhancement   start : 3-aug-2005
    /**
     * Setter for property customQuery.
     * @param customQuery New value of property customQuery.
     */
    public void setCustomQuery(java.lang.String customQuery) {
        this.customQuery = customQuery;
    }//end 3-aug-2005

    /**
     * This method will format the fieldValue to proper date,
     * if the prooerty isDate is true.
     * @param fieldValue 
     * @param isDate fieldValue is date or not
     * @throws CoeusSearchException
     * @return String formatted value
     */    
     public String formateAttributeValue(String fieldValue, boolean isDate, 
        int rowIndex)throws CoeusSearchException{
         int startIndex = 0;
         String value = "";
         DateUtils dtUtils = new DateUtils();
         StringBuffer properValue = new StringBuffer();
         fieldValue = (fieldValue==null)?"":fieldValue;
         int indx = fieldValue.toUpperCase().indexOf("AND",startIndex);
         if( indx != -1){
             value = fieldValue.substring(startIndex, indx);
             // check for the field value is date 
             if(isDate){
                 value = UtilFactory.checkNull(dtUtils.formatDate(value.trim(),"/-:,","dd-MMM-yy"));
             } else {
                 value = UtilFactory.checkNull(value.trim());
             }
             // Adding AND condition between two values
             if(value!=null){
                 properValue.append(value+" AND ");
             } else {
                 throw new CoeusSearchException(
                            INVALID_SEARCH+(rowIndex+1));
             }
             startIndex = indx + "AND".length();
         } else {
                 throw new CoeusSearchException(
                            INVALID_SEARCH+(rowIndex+1));
         }
         value = fieldValue.substring(startIndex, fieldValue.length());
         // check for the field value is date 
         if(isDate){
             value = UtilFactory.checkNull(dtUtils.formatDate(value.trim(),"/-:,","dd-MMM-yy"));
         } else {
             value = UtilFactory.checkNull(value.trim());
         }
         // Adding the second value to the query
         if(value!=null){
             properValue.append(value);
         } else {
                 throw new CoeusSearchException(
                            INVALID_SEARCH+(rowIndex+1));
         }
         return properValue.toString();
     }    
}