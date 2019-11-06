/*
 * CoeusSearchResultAction.java
 *
 * Created on July 15, 2002, 2:17 PM
 */

package edu.mit.coeus.action.search;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.ServletException;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ForwardingActionForward;

import java.util.Vector;
import java.util.Hashtable;
import java.util.Enumeration;
import java.io.IOException;
import java.sql.SQLException;

import edu.mit.coeus.search.bean.ProcessSearchXMLBean;
import edu.mit.coeus.search.bean.SearchInfoHolderBean;
import edu.mit.coeus.search.bean.SearchExecutionBean;
import edu.mit.coeus.search.bean.ColumnBean;
import edu.mit.coeus.search.bean.AttributeBean;
import edu.mit.coeus.search.bean.CriteriaBean;

import edu.mit.coeus.action.common.CoeusActionBase;
import edu.mit.coeus.search.exception.CoeusSearchException;
import edu.mit.coeus.utils.dbengine.DBException;
import edu.mit.coeus.utils.UtilFactory;
/* CASE #748 Begin */
import edu.mit.coeus.exception.CoeusException;
/* CASE #748 End */


/**
 *
 * @author  geo
 * @version
 */
public class CoeusSearchResultAction  extends CoeusActionBase{

    /**
     * Process the specified HTTP request, and create the corresponding HTTP
     * response (or forward to another web component that will create it).
     * Return an ActionForward instance describing where and how control
     * should be forwarded, or null if the response has already been completed.
     *
     * @param actionMapping  The ActionMapping used to select this instance
     * @param actionForm  The optional ActionForm bean for this request (if any)
     * @param request  The HTTP request we are processing
     * @param response  The HTTP response we are creating
     *
     * @throws java.io.IOException  if an input/output error occurs
     * @throws javax.servlet.ServletException  if a servlet exception occurs.
     */
    public ActionForward perform( ActionMapping actionMapping ,
        ActionForm actionForm , HttpServletRequest request ,
            HttpServletResponse response ) throws IOException , ServletException{

        final String SEARCH_NAME = "searchname";
        HttpSession session = null;
        ActionForward actionforward = actionMapping.findForward( SUCCESS );
//        UtilFactory UtilFactory = new UtilFactory();
        boolean errorFlag = false;
        boolean validResult = false;	// the result of valid award/proposal
        ActionErrors actionErrors = new ActionErrors(); // to hold any validation errors
        String searchName = request.getParameter("searchName");
        try {
            session = request.getSession( true );
            SearchInfoHolderBean searchInfoHolder =
                (SearchInfoHolderBean)session.getAttribute("searchinfoholder");

            //System.out.println(searchInfoHolder.toString());

            SearchExecutionBean searchExecution = new SearchExecutionBean(searchInfoHolder);
            Vector columns = new Vector(3,2);
            String[] fieldValues = null;
/*            Hashtable criteriaList = searchInfoHolder.getCriteriaList();
            Enumeration criteriaEnum = criteriaList.keys();
            while(criteriaEnum.hasMoreElements()){
                String criteriaName = (String)criteriaEnum.nextElement();
 */
            Vector criteriaList = searchInfoHolder.getCriteriaList();
            int criteriaCount = criteriaList.size();
            for(int criteriaIndex=0;criteriaIndex<criteriaCount;criteriaIndex++){
                CriteriaBean criteria = (CriteriaBean)criteriaList.get(criteriaIndex);
                String criteriaName = criteria.getName().trim();
                fieldValues = request.getParameterValues(criteriaName);
                //System.out.println("field values from the parameter=>"+ fieldValues.toString());
                //System.out.println("field length from the parameter=>"+ fieldValues.length);
                ColumnBean column = new ColumnBean(criteriaName);
                for(int fieldIndex=0;fieldIndex<fieldValues.length;fieldIndex++){
                    String fieldValue = fieldValues[fieldIndex];
                    //System.out.println("field value=>"+fieldValue);
                    if(fieldValue!=null && !fieldValue.trim().equals("")){
                        /* CASE #1217 Begin */
                        /* to provide wild card searching using "*". Replace all
                         * occurances of * with "%" symbol by prefixing "LIKE" to the fieldValue
                         */
                        if( fieldValue.trim().indexOf('*') != -1 ){
                            String tempVal = "LIKE ";
                            fieldValue = fieldValue.replace('*','%');
                            if( fieldValue.toUpperCase().indexOf("LIKE") == -1 ){
                                fieldValue = tempVal + fieldValue;
                            }
                        }                        
                        /* CASE 1217 End */
                        AttributeBean attribute = new AttributeBean(
                                                        ""+fieldIndex,fieldValue);
                        //column.addAttribute(attribute);
                        searchExecution.addAttribute(column,attribute);
                    }
                }
                searchExecution.addColumn(column);
            }
            if(searchName.equals("personSearch")){
                request.setAttribute("reqType",request.getParameter("reqType"));
                request.setAttribute("reqPage",request.getParameter("reqPage"));
            }
            request.setAttribute("fieldName",request.getParameter("fieldName"));
            //System.out.println("field name in coeusSearchResultAction=>"+request.getParameter("fieldName"));
            //System.out.println("columns in the search execution=>"+searchExecution.toString());
            Hashtable searchResult = searchExecution.executeSearchQuery();
            if(searchResult!=null){
                //System.out.println("search result in action class=>"+searchResult.toString());
                request.setAttribute("searchResult",searchResult);
            }
            /* CASE #748 Begin */
            else{
                throw new CoeusException("exceptionCode.20005");
            }
        } catch (CoeusException cEx){
            errorFlag = true;
            cEx.printStackTrace();
            UtilFactory.log( cEx.getMessage() , cEx, "CoeusSearchResultAction",
                                                          "perform()" );
            request.setAttribute( "EXCEPTION", cEx );
        }
        /* CASE #748 End */
        catch( Exception ex ) {
            errorFlag = true;
            ex.printStackTrace();
            UtilFactory.log( ex.getMessage() , ex , "CoeusSearchResultAction" ,
                    "perform()" );
            request.setAttribute( "EXCEPTION" ,
                    new CoeusSearchException( ex.getMessage() ) );
        }

        /*
         * if any exceptions were thrown then forward to FAILURE page
         * other wise forward to SUCCESS
         */
        if( errorFlag ) {
            actionforward = actionMapping.findForward( FAILURE );
        } else {
            actionforward = actionMapping.findForward( searchName );
        }
        return actionforward;
    }//end of perform

}
