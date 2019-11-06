/*
 * @(#)CodeTableServlet.java
 *
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 *
 */

package edu.mit.coeus.servlet;

import edu.mit.coeus.customelements.bean.CustomElementsDataTxnBean;
import edu.mit.coeus.iacuc.bean.ProtocolDataTxnBean;
import edu.mit.coeus.utils.CoeusVector;
import javax.servlet.*;
import javax.servlet.http.*;

import java.util.*;
import java.io.*;
import java.net.*;

import javax.xml.parsers.SAXParserFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import org.xml.sax.*;
import org.xml.sax.helpers.DefaultHandler;

import edu.mit.coeus.bean.UserInfoBean;
import edu.mit.coeus.bean.UserDetailsBean;
import edu.mit.coeus.codetable.bean.AllCodeTablesBean;
import edu.mit.coeus.codetable.bean.CodeTableTxnBean;
import edu.mit.coeus.codetable.bean.ColumnBean;
import edu.mit.coeus.codetable.bean.DataBean;
import edu.mit.coeus.codetable.bean.ParameterBean;
import edu.mit.coeus.codetable.bean.RequestTxnBean;
import edu.mit.coeus.codetable.bean.StoredProcedureBean;
import edu.mit.coeus.codetable.bean.TableStructureBean;
import edu.mit.coeus.codetable.parser.CodeTableParser ;
import edu.mit.coeus.codetable.parser.XMLFileName ;
import edu.mit.coeus.brokers.* ;
import edu.mit.coeus.utils.UtilFactory;


/**
 * Servlet for accessing and updating code table data.
 * Takes RequestTxnBean from GUI, which contains action field.  Valid values
 * for action include "LIST_TABLES", "GET_DATA" and "MODIFY_DATA".
 * Instantiates CodeTableTxnBean to access the database.
 * @see edu.mit.coeus.codetable.bean.RequestTxnBean.
 * @see edu.mit.coeus.codetable.bean.CodeTableTxnBean.
 */
public class CodeTableServlet extends CoeusBaseServlet {

    //AllCodeTablesBean allCodeTablesBean = null;
    //private String action = "";
    // Added for COEUSQA-2570 : OSP Administrators should have the rights to override cost sharing submission field value before submitting the proposal to the sponsor - Start
    private static final String GET_PROP_DEV_EDITABLE_COLUMNS = "GET_PROP_DEV_EDITABLE_COLUMNS";
    // Added for COEUSQA-2570 : OSP Administrators should have the rights to override cost sharing submission field value before submitting the proposal to the sponsor - End
    
    /** Initializes the servlet.
     */
    public void init(ServletConfig config) throws ServletException
    {
            super.init(config);
    }

    /** Destroys the servlet.
     */
    public void destroy()
    {
    }

    /** Processes requests for both HTTP <code>GET</code> and <code>POST</code> methods.
     * @param request servlet request
     * @param response servlet response
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, java.io.IOException
    {
         //java.io.PrintWriter out = response.getWriter();
        //System.out.println("begin CodeTableServlet.processRequest()");
        ObjectInputStream iStream = null ; 
        ObjectOutputStream oStream = null ; 
        ResponderBean responder = null ;
        //Addded for Case#4585 - Protocol opened from list window is not the correct one - Start 
        String action = "";
        AllCodeTablesBean allCodeTablesBean = null;
        //Case#4585 - End
        
       try
       {
//       System.out.println("\n *** 1 ***\n") ;
       iStream = new ObjectInputStream(request.getInputStream()) ;
//       System.out.println("\n *** 2 ***\n") ;
          
//       System.out.println("\n *** 3 ***\n") ;    
        RequesterBean requester = null;
        // the response object to applet
        responder = new ResponderBean();
//        System.out.println("\n *** 4 ***\n") ;
        
            // read the serialized request object from applet
        requester = (RequesterBean) iStream.readObject();
        isValidRequest(requester);
//        System.out.println("\n *** 5 ***\n") ;
        RequestTxnBean requestTxnBean = (RequestTxnBean)requester.getDataObject() ; 
//        System.out.println("\n *** 6 ***\n") ;
        if(requestTxnBean != null)
        {
//          System.out.println("*** Servlet recvd a RequestTxnBean ***") ;
          action = requestTxnBean.getAction();
//          System.out.println(" RequestTxnBean action: "+action);
        }
        else
          System.out.println("requestTxnBean == null");
        

        if(action.equals("LIST_TABLES"))
        {
//          System.out.println("*** request for table list ***");
          allCodeTablesBean = parseXMLFile();
         
           // get the user
            UserInfoBean userBean = (UserInfoBean)new
                UserDetailsBean().getUserInfo(requester.getUserName());
            String userId = userBean.getUserId();         
                
          responder.setId(userId) ;
          
          responder.setDataObject(allCodeTablesBean);
          responder.setResponseStatus(true);
          responder.setMessage(null);
                
          
        }
        else if(action.equals("GET_DATA"))
        {
//          System.out.println("*** Accessing database  to retrieve data***") ;
          CodeTableTxnBean txnBean = null ;
          try
          {
//            System.out.println("*** b4 CodeTableTxnBean *** ") ;     
            txnBean = new CodeTableTxnBean();
//             System.out.println("*** after CodeTableTxnBean *** ") ;   
          }
          catch(Exception exp)
          {
            System.out.println("*** Error in CodeTableTxnBean *** ") ;   
            exp.printStackTrace() ;
          }
          
   
          StoredProcedureBean selectStoredProcedure =
           requestTxnBean.getStoredProcedureBean();
           
          Vector codeTableData = txnBean.getData(selectStoredProcedure, requestTxnBean.getSelectProcParameters());
          
//          System.out.println("codeTableData.size(): " + codeTableData.size());
//          System.out.println("***Sending back DataBean***");
          DataBean dataBean = new DataBean();
          dataBean.setVectData(codeTableData);
          responder.setDataObject(dataBean);
          responder.setResponseStatus(true);
          responder.setMessage(null);
          
        }
        else if(action.equals("MODIFY_DATA"))
        {
//          System.out.println("*** Updating Database ***");
          CodeTableTxnBean txnBean = new CodeTableTxnBean();
        
          StoredProcedureBean updateStoredProcedure =
            requestTxnBean.getStoredProcedureBean();
          
         
          Vector rowsToUpdate = requestTxnBean.getRowsToModify();
          
          boolean success = txnBean.updateData(updateStoredProcedure, rowsToUpdate);
//          System.out.println("success of update in CodeTableServlet: "+success);
          /* Send a Boolean to the GUI to let it know whether the update was successful.*/
          Boolean successObj = new Boolean(success);
          responder.setDataObject(successObj);
          responder.setResponseStatus(true);
          responder.setMessage(null);
          
          //COEUSQA:1691 - Front End Configurations to CoeusLite Pages - Start
          //This is to update the properties file if there is any updations for message properties file in code table interface.
          //The properties file will be update from DB.
          txnBean.replaceMessageProperties(updateStoredProcedure.getName());
          //COEUSQA:1691 - End
                   
        }
        else if(action.equals("DEPENDENCY_CHECK_INT"))
        {
//              System.out.println("*** checking for Dependency int col ***");
              CodeTableTxnBean txnBean = new CodeTableTxnBean();
              HashMap hashDependencyValues = requestTxnBean.getDependencyCheck() ;

              int count = txnBean.checkDependencyForIntCol(hashDependencyValues.get("a_table").toString(),
                                                                 hashDependencyValues.get("a_column").toString(),
                                                                 new Integer(hashDependencyValues.get("a_column_value").toString())) ;
              boolean success;
              if (count == 0 )
              {
                success = true ;
              }    
              else
              {
                success = false ;
              }    
              
              /* Send a Boolean to the GUI to let it know whether the update was successful.*/
              Boolean successObj = new Boolean(success);
              responder.setDataObject(successObj);
              responder.setResponseStatus(true);
              responder.setMessage(null);
          
        }
        else if(action.equals("DEPENDENCY_CHECK_VARCHAR2"))
        {
//              System.out.println("*** checking for Dependency varchar2 column ***");
            
              CodeTableTxnBean txnBean = new CodeTableTxnBean();
              HashMap hashDependencyValues = requestTxnBean.getDependencyCheck() ;
             
              int count = txnBean.checkDependencyForVarchar2Col(hashDependencyValues.get("a_table").toString(),
                                                                 hashDependencyValues.get("a_column").toString(),
                                                                 hashDependencyValues.get("a_column_value").toString()) ;
//              System.out.println("*** checking for Dependency varchar2 get count ***");
              boolean success;
              if (count == 0 )
              {
                success = true ;
              }    
              else
              {
                success = false ;
              }
              
              /* Send a Boolean to the GUI to let it know whether the update was successful.*/
              Boolean successObj = new Boolean(success);
              responder.setDataObject(successObj);
              responder.setResponseStatus(true);
              responder.setMessage(null);
        }
        else if(action.equals("DEPENDENCY_CHECK_STATE"))
        {
//              System.out.println("*** checking for Dependency int col ***");
              CodeTableTxnBean txnBean = new CodeTableTxnBean();
              HashMap hashDependencyValues = requestTxnBean.getDependencyCheck() ;

              int count = txnBean.checkDependencyForVarchar2ColState(hashDependencyValues.get("as_select").toString(),
                                                                 hashDependencyValues.get("as_from").toString(),
                                                                 hashDependencyValues.get("as_where").toString()) ;
              boolean success;
              if (count == 0 )
              {
                success = true ;
              }    
              else
              {
                success = false ;
              }    
            
              /* Send a Boolean to the GUI to let it know whether the update was successful.*/
              Boolean successObj = new Boolean(success);
              responder.setDataObject(successObj);
              responder.setResponseStatus(true);
              responder.setMessage(null);
          
        }
        else if(action.equals("GET_COLUMNS_FOR_TABLE"))
        {
//          System.out.println("*** Accessing database  to retrieve data***") ;
          CodeTableTxnBean txnBean = null ;
          try
          {
            txnBean = new CodeTableTxnBean();
          }
          catch(Exception exp)
          {
            System.out.println("*** Error in CodeTableTxnBean *** ") ;   
            exp.printStackTrace() ;
          }
           
          String tableName = requestTxnBean.getTableName();
          
          Vector codeTableData = txnBean.getColumnsForTable(tableName);
          
//          System.out.println("codeTableData.size(): " + codeTableData.size());
//          System.out.println("***Sending back DataBean***");
          DataBean dataBean = new DataBean();
          dataBean.setVectData(codeTableData);
          responder.setDataObject(dataBean);
          responder.setResponseStatus(true);
          responder.setMessage(null);
         
        }
       
        else if(action.equals("GET_FUNCTION_LIST"))
        {
//          System.out.println("*** Accessing database  to retrieve data***") ;
          CodeTableTxnBean txnBean = null ;
          try
          {
            txnBean = new CodeTableTxnBean();
          }
          catch(Exception exp)
          {
            System.out.println("*** Error in CodeTableTxnBean *** ") ;   
            exp.printStackTrace() ;
          }
           
          Vector codeTableData = txnBean.getDataForFunctionList();
          
//          System.out.println("codeTableData.size(): " + codeTableData.size());
//          System.out.println("***Sending back DataBean***");
          DataBean dataBean = new DataBean();
          dataBean.setVectData(codeTableData);
          responder.setDataObject(dataBean);
          responder.setResponseStatus(true);
          responder.setMessage(null);
         
        }
        else if(action.equals("GET_USER_MAINTAIN_CODE_TABLES_RIGHTS"))
        {
              CodeTableTxnBean txnBean = new CodeTableTxnBean();
              String userId = requestTxnBean.getTableName();
             
             
              int count = txnBean.checkUserHasOSPRight(userId, "MAINTAIN_CODE_TABLES");
//              System.out.println("*** checking for Dependency varchar2 get count ***");
              boolean success;
              if (count == 1 )
              {
                success = true ;
              }    
              else
              {
                success = false ;
              }
              
              System.out.println("return val obtd: "+ success);
              /* Send a Boolean to the GUI to let it know whether the update was successful.*/
              Boolean successObj = new Boolean(success);
              responder.setDataObject(successObj);
              responder.setResponseStatus(true);
              responder.setMessage(null);
          //code added for coeus4.3 enhancement - starts
          //To get the protocol modules data.
        } else if(action.equals("GET_PROTOCOL_MODULES_DATA")){
              CodeTableTxnBean txnBean = new CodeTableTxnBean();
              String procedureName = requestTxnBean.getProcedureName();
              Vector codeTableData = txnBean.getProtocolModuleNames(procedureName);
              DataBean dataBean = new DataBean();
              dataBean.setVectData(codeTableData);
              responder.setDataObject(dataBean);
              responder.setResponseStatus(true);
              responder.setMessage(null);            
        }//code added for coeus4.3 enhancement - ends
        //Code added for Coeus4.3 Enhancement - Start
        // Email Implementation
        else if(action.equals("GET_ACTIONS_FOR_EMAIL_NOTIF")) {
            CodeTableTxnBean txnBean = new CodeTableTxnBean();
            try
            {
                txnBean = new CodeTableTxnBean();
            }
            catch(Exception exp)
            {
                System.out.println("*** Error in CodeTableTxnBean *** ") ;   
                exp.printStackTrace() ;
            }
           
            Vector codeTableData = txnBean.getActionsForMail();
          
            DataBean dataBean = new DataBean();
            dataBean.setVectData(codeTableData);
            responder.setDataObject(dataBean);
            responder.setResponseStatus(true);
            responder.setMessage(null);
        } //Added for Case #3121 - start
         //Tuition Fee Calculation Enhancement
        else if(action.equals("GET_COST_ELEMENT")) {
              CodeTableTxnBean txnBean = new CodeTableTxnBean();
              String procedureName = requestTxnBean.getProcedureName();
              Vector codeTableData = txnBean.getCostElements(procedureName);
              DataBean dataBean = new DataBean();
              dataBean.setVectData(codeTableData);
              responder.setDataObject(dataBean);
              responder.setResponseStatus(true);
              responder.setMessage(null);            
        }
        //Added for Case #3121 - end
        //Added for COEUSDEV-86 : Questionnaire for a Submission -Start
        else if(action.equals("GET_SUB_MODULES_FOR_MODULE")){
            CodeTableTxnBean codeTableTxnBean = new CodeTableTxnBean();
            String moduleCode = (String)requester.getId();
            Vector vcSubModules = codeTableTxnBean.getSubModulesForModule(moduleCode);
            DataBean dataBean = new DataBean();
            dataBean.setVectData(vcSubModules);
            responder.setDataObject(dataBean);
            responder.setResponseStatus(true);
            responder.setMessage(null); 
        }
        //Added For IACUC get procedure category-Start
        else if(action.equals("PROCEDURE_CATEGORY_DATA")){
            ProtocolDataTxnBean protocolDataTxnBean = new ProtocolDataTxnBean();
            String moduleCode = (String)requester.getId();
            Vector vecProcCategoryType = protocolDataTxnBean.getProcedureCategoryTypes();
            DataBean dataBean = new DataBean();
            dataBean.setVectData(vecProcCategoryType);
            responder.setDataObject(dataBean);
            responder.setResponseStatus(true);
            responder.setMessage(null); 
        }
        //Added For IACUC get procedure category-End
        //Added with COEUSQA-2667:User interface for setting up question details for procedure categories - Start
        else if(action.equals("LOOKUP_ARGUMENT")){
            CustomElementsDataTxnBean customElementsDataTxnBean = new CustomElementsDataTxnBean();
            CoeusVector cvArgNames = customElementsDataTxnBean.getArgumentNamesForCodeTable();
            DataBean dataBean = new DataBean();
            dataBean.setVectData(cvArgNames);
            responder.setDataObject(dataBean);
            responder.setResponseStatus(true);
            responder.setMessage(null);
        }
        //COEUSQA-2667:End
        //COEUSDEV-86 : End
        // Added for COEUSQA-2570 : OSP Administrators should have the rights to override cost sharing submission field value before submitting the proposal to the sponsor - Start
        else if(action.equals(GET_PROP_DEV_EDITABLE_COLUMNS)){
            CodeTableTxnBean txnBean = null ;
            txnBean = new CodeTableTxnBean();
            String tableName = requestTxnBean.getTableName();
            Vector vecProposalTable = txnBean.getColumnsForTable("OSP$EPS_PROPOSAL");
            Vector vecbudgetTable = txnBean.getColumnsForTable("OSP$BUDGET");
            for(Object budgetTableColumn : vecbudgetTable){
                HashMap hmBudgetTableColumn = (HashMap)budgetTableColumn;
                String budgetColumnName = (String)hmBudgetTableColumn.get("COLUMN_NAME");
                if("SUBMIT_COST_SHARING_FLAG".equals(budgetColumnName)){
                    vecProposalTable.add(hmBudgetTableColumn);
                    break;
                }
            }
            DataBean dataBean = new DataBean();
            dataBean.setVectData(vecProposalTable);
            responder.setDataObject(dataBean);
            responder.setResponseStatus(true);
            responder.setMessage(null);
            
        }
        // Added for COEUSQA-2570 : OSP Administrators should have the rights to override cost sharing submission field value before submitting the proposal to the sponsor - End                
        //COEUSQA:3005 - Dependency between the Location Name and Location Type in IACUC - Start
        else if(action.equals("LOCATION_TYPE_DATA")){
            ProtocolDataTxnBean protocolDataTxnBean = new ProtocolDataTxnBean();
            String moduleCode = (String)requester.getId();
            Vector vecLocationType = protocolDataTxnBean.getLocationTypes();
            DataBean dataBean = new DataBean();
            dataBean.setVectData(vecLocationType);
            responder.setDataObject(dataBean);
            responder.setResponseStatus(true);
            responder.setMessage(null);
        }
        //COEUSQA:3005 - End
        // Added fot COEUSQA-1725 Extend the functionality of Lab Allocation in proposal development budgeting - Start
        else if(action.equals("GET_CODE_TABLE_TYPE_DETAILS")){
            CodeTableTxnBean codeTableTxnBean = new CodeTableTxnBean();
            CoeusVector cvTypes = codeTableTxnBean.getFormulatedTypes(requestTxnBean.getProcedureName());
            responder.setDataObject(cvTypes);
            responder.setResponseStatus(true);
            responder.setMessage(null);

        }
        // Added fot COEUSQA-1725 Extend the functionality of Lab Allocation in proposal development budgeting - End
        
       }catch(Exception ex)
       {
           //ex.printStackTrace() ;
           //System.out.println(" **************** Server Side Errors ************* ") ;
           UtilFactory.log( ex.getMessage(), ex, "CodeTableServlet", "doPost");
        //Case 3193 - START
        }catch(Throwable throwable){
            Exception ex = new Exception(throwable);
            responder.setException(ex);
            responder.setResponseStatus(false);
            responder.setMessage(ex.getMessage());
            UtilFactory.log( throwable.getMessage(), throwable, "CodeTableServlet", "doPost");
        //Case 3193 - END
       }finally {
            try{
        
                // send the object to applet
                oStream
                = new ObjectOutputStream(response.getOutputStream());
                oStream.writeObject(responder);
                // close the streams
                if (iStream!=null){
                    iStream.close();
                }
                if (oStream!=null){
                    oStream.flush();
                    oStream.close();
                }
            }catch (IOException ioe){
                //ioe.printStackTrace() ;
                UtilFactory.log( ioe.getMessage(), ioe, "CodeTableServlet", "doPost");
            }
        }
       
        
    }

    /** Handles the HTTP <code>GET</code> method.
     * @param request servlet request
     * @param response servlet response
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, java.io.IOException {
        processRequest(request, response);
    }

    /** Handles the HTTP <code>POST</code> method.
     * @param request servlet request
     * @param response servlet response
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, java.io.IOException {
        processRequest(request, response);
    }

    /** Returns a short description of the servlet.
     */
    public String getServletInfo() {
        return "Short description";
    }

    //Simulate parsing the XML file.
    private AllCodeTablesBean parseXMLFile()
    {
      AllCodeTablesBean resultsbean = null ;  
     
      try{
         XMLFileName xmlFileName = new XMLFileName();
         String xmlFile = xmlFileName.getName();
               
         SAXParserFactory factory = SAXParserFactory.newInstance();
         SAXParser parser = null;
     
         parser = factory.newSAXParser();
           
         CodeTableParser handler = new CodeTableParser();
        /*
         *Comented by Geo to read the xml file as input stream
         * On 11-22-2004
         */
//         parser.parse(xmlFile, handler);
         InputStream codeTableIS = getClass().getResourceAsStream(xmlFile);
         parser.parse(codeTableIS, handler);
         /*
          * End Block by Geo
          */
         resultsbean = handler.resultbean();
         
           } catch (IOException e1) {
                e1.printStackTrace();
                UtilFactory.log(e1.getMessage(),e1,"CodeTableServlet", "parseXMLFile");
           } catch (SAXException e2) {
                e2.printStackTrace();
                UtilFactory.log(e2.getMessage(),e2,"CodeTableServlet", "parseXMLFile");
           } catch (ParserConfigurationException ex) {
                ex.printStackTrace();    
                UtilFactory.log(ex.getMessage(),ex,"CodeTableServlet", "parseXMLFile");
            }
          
          return resultsbean ;
        
    }


}