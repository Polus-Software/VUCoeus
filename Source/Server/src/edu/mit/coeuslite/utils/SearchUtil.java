/*
 * @(#) OrganizationSearchForm.java
 *
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 *
 */
package edu.mit.coeuslite.utils;

import edu.mit.coeus.search.bean.AttributeBean;
import edu.mit.coeus.search.bean.ColumnBean;
import edu.mit.coeus.search.bean.CriteriaBean;
import edu.mit.coeus.search.bean.SearchExecutionBean;
import edu.mit.coeus.search.bean.SearchInfoHolderBean;
import edu.mit.coeus.search.exception.CoeusSearchException;
import java.util.Vector;
import javax.servlet.http.HttpServletRequest;

/**
 * SearchUtil.java
 * This is the class to seperate srerch creterias that user enters
 * Created on April 20, 2007, 11:49 AM
 * @author  noorula
 */
public class SearchUtil {
    // COEUSQA-1891: Multicampus enhancement - Start
    private static final String UNIT_SEARCH_CAMPUS_COLUMN =  "SUBSTR(UNIT_NUMBER,1,3)";
    private static final String PERSON_SEARCH_CAMPUS_COLUMN = "OSP$MULTI_CAMPUS_PERSON_MAP.CAMPUS_CODE";
    private static final String ALL_CAMPUSES_CODE = "0";
    // COEUSQA-1891: Multicampus enhancement - End
    /** Creates a new instance of SearchUtil */
    private SearchUtil() {
    }
    
    /**
     * This method is used to get the Criterias that user enters, 
     * this will be used for building query  
     * @param searchExecution
     * @param searchInfoHolder
     * @param request
     * @throws Exception
     * @return boolean
     */    
    public static boolean fillCriteria(SearchExecutionBean searchExecution,
    SearchInfoHolderBean searchInfoHolder, HttpServletRequest request)throws Exception{
        String[] fieldValues = null;
        Vector criteriaList = searchInfoHolder.getCriteriaList();
        int criteriaCount = criteriaList.size();
        boolean isSearchCriteriaEntered = false;
        for(int criteriaIndex=0;criteriaIndex<criteriaCount;criteriaIndex++){
            CriteriaBean criteria = (CriteriaBean)criteriaList.get(criteriaIndex);
            String criteriaName = criteria.getName().trim();
            fieldValues = request.getParameterValues(criteriaName);
            ColumnBean column = new ColumnBean(criteriaName);
            if(fieldValues!=null){
                for(int fieldIndex = 0 ; fieldIndex < fieldValues.length ; fieldIndex++){
                    String fieldValue = fieldValues[fieldIndex];
                    
                    // COEUSQA-1891: Multicampus enhancement - Start
                    // If the user has selected 'All Campuses' do not add the filed.
                    if(UNIT_SEARCH_CAMPUS_COLUMN.equalsIgnoreCase(criteriaName) ||
                        PERSON_SEARCH_CAMPUS_COLUMN.equalsIgnoreCase(criteriaName)){
                           if(ALL_CAMPUSES_CODE.equalsIgnoreCase(fieldValue)){
                               continue;
                           }
                        }
                    // COEUSQA-1891: Multicampus enhancement - End
                    
                    if(fieldValue!=null && !fieldValue.trim().equals("")){
                        if(fieldValue.indexOf("*")!=-1){
                            fieldValue = fieldValue.replace('*','%');
                            fieldValue = "LIKE " + fieldValue;
                        }
                        isSearchCriteriaEntered = true;
                        AttributeBean attribute = new AttributeBean(
                        ""+fieldIndex,fieldValue);
                        String valueType = attribute.getValueType();
                        valueType = valueType.replaceAll(" ", "");
                        //Code commented for coeus4.3 search between two dates enhancement
                        //As per code review remark.
//                        //case 2042 start
//                        int index = valueType.indexOf("=");
//                        if(index != -1 && index != 0 && index != 1){
//                            throw new CoeusSearchException(
//                                "Invalid Search criteria in row number: "+(criteriaIndex+1));
//                        }//case 2042 End
                        searchExecution.addAttribute(column,attribute);
                    }
                }
            }
            searchExecution.addColumn(column);
        }
        return isSearchCriteriaEntered;
    }
    
}
