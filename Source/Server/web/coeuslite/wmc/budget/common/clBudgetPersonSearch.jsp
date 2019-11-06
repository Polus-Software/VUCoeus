<%@ page contentType="text/html;charset=UTF-8" language="java" 
    import="edu.mit.coeus.utils.ComboBoxBean,
   edu.mit.coeus.search.bean.DisplayBean,
                java.util.Vector,
                java.util.HashMap,
                edu.mit.coeus.utils.UtilFactory,
                edu.mit.coeuslite.utils.SessionConstants,
                edu.mit.coeuslite.utils.CoeusLiteConstants"
	%>
   


<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>


<jsp:useBean id="displayList" scope="session" class="java.util.Vector" />
<jsp:useBean id="searchinfoholder" scope='session'
                class="edu.mit.coeus.search.bean.SearchInfoHolderBean"/>
<bean:define id="criteriaList" name="searchinfoholder" property="criteriaList"/>
<jsp:useBean id ="fldName" scope ='request' class = "java.lang.String" />
<jsp:useBean id ="searchName" scope ='request' class = "java.lang.String" />
<jsp:useBean id="searchResult" scope='request' class="java.util.Hashtable"/>
<jsp:useBean id="ressList" scope="session" class="java.util.Vector"/>
<jsp:useBean id ="fieldName" scope ='request' class = "java.lang.String" />
<bean:define id='retrieveLimit' name='searchinfoholder' property='retrieveLimit'/>
<%@ include file= "/coeuslite/mit/utils/CoeusContextPath.jsp"  %>

<% 

Vector resList = null;
String strSearch  = "";
String strType    = "";
String search = (String)request.getAttribute("search");
HttpSession sessions = request.getSession();
 HashMap resData = (HashMap)request.getAttribute("getRowData");
if (request.getParameter("search") != null)
  strSearch = request.getParameter("search");
if (request.getParameter("type") != null)
strType = request.getParameter("type");
// COEUSQA-1891-Multicampus enhancement -Start  
String campusCode = (String)session.getAttribute(SessionConstants.USER_CAMPUS_CODE) ;
String multiCampusEnabled = (String)session.getAttribute(SessionConstants.MULTI_CAMPUS_ENABLED);
final String CAMPUS = "Campus";
// COEUSQA-1891-Multicampus enhancement -End
%>

<html:html locale="true">
    <head>
        <title><bean:message bundle="proposal" key="search.title1"/> <%=session.getAttribute("type").toString()%></title>
        <link href="<bean:write name='ctxtPath'/>/coeuslite/mit/utils/css/ipf.css" rel="stylesheet" type="text/css">
        <style type="text/css">
            //div {font-family:verdana;font-size:10px}
            //a {font-family:verdana;font-size:10px;color:blue;text-decoration:none;}
            //a:active {color:red;text-decoration:none;}
            //a:visited {color:blue;text-decoration:none;}
            //a:hover {color:red;text-decoration:none;}
        </style>

        <html:base/>
        <meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
        <link href="<bean:write name='ctxtPath'/>/coeuslite/mit/utils/css/coeus_styles.css" rel="stylesheet" type="text/css">

        <script language="javascript" type="text/JavaScript" src="_js.js"></script>
        <script language="JavaScript" type="text/JavaScript">
      
   
   
    
     
            function getRowData(rowId) {    
            document.budgetPersonsDynaBean.action = "<%=request.getContextPath()%>/personSearch.do?actionPerformed="+rowId;
            document.budgetPersonsDynaBean.submit();
            }

            function populateResultData(){
            resArray = new Array();
       
          <%if(resData != null) {
          java.util.Set keys= resData.keySet();
            for ( java.util.Iterator iterator = keys.iterator(); iterator.hasNext(); ) {
                    String key = iterator.next().toString();
                    // Added for COEUSQA-3141_Rolodex Comments with more than one line will not populate to Lite proposals_start
                    if(key.equals("COMMENTS") && resData.get(key)!= null){
                                String data = ((String)resData.get(key)).replaceAll("\n","");
                                data = data.replaceAll("\r","");
                                data = data.replaceAll("\t","");
                                data = data.replaceAll("\b","");
                                data = data.replaceAll("\f","");
                                data = data.replaceAll("\"","");
                                data = data.replaceAll("\'","");
                                resData.put("COMMENTS",data);
                            }
                    // Added for COEUSQA-3141_Rolodex Comments with more than one line will not populate to Lite proposals_end
                %>  
            resArray["<%=key%>"] = "<%=resData.get(key)%>";
            <%           
             }%>
            var proposalNumber = "<%=request.getAttribute("proposalNumber")%>";
            var searchType ="<%=(String)session.getAttribute("type")%>";
            var fullName = '';
            var personId = '';
            var salaryAnnivDate = '';
            <%--JIRA COEUSDEV-341 START  fullname is enclosed by "(double quotes) instead of '(single quotes) since some names can contain ' ex: o'connor--%>
            if(searchType == 'budgetRolodexSearch') {
            if("<%=resData.get("LAST_NAME")%>"!="null"){
            fullName="<%=resData.get("LAST_NAME")%>"+", ";
            }        
            if("<%=resData.get("MIDDLE_NAME")%>"!='null'){
            fullName+= "<%=resData.get("MIDDLE_NAME")%>"+", ";
            }                
            if("<%=resData.get("FIRST_NAME")%>"!="null"){
            fullName+="<%=resData.get("FIRST_NAME")%>";
            }
            if('<%=resData.get("SALARY_ANNIVERSARY_DATE")%>'!="null"){
            salaryAnnivDate = '<%=resData.get("SALARY_ANNIVERSARY_DATE")%>';
            }
             <% 
              if(resData.get("SALARY_ANNIVERSARY_DATE") != null){
               session.setAttribute("srcSalaryAnnivDate", resData.get("SALARY_ANNIVERSARY_DATE"));
               }          
            %>
            if(fullName=='null' || fullName== undefined || fullName.length==0){
            fullName = "<%=resData.get("ORGANIZATION")%>";
            }
            <%--JIRA COEUSDEV-341 END--%>
            personId = "<%=resData.get("ROLODEX_ID")%>";                
            } else if(searchType == 'budgetPersonSearch') {
            fullName = "<%=resData.get("FULL_NAME")%>";
            personId = "<%=resData.get("PERSON_ID")%>";              
            }
            window.opener.document.budgetPersonsDynaBean.action = "<%=request.getContextPath()%>/AddPersonLineItem.do?proposalNumber="+proposalNumber+"&personId="+personId+"&fullName="+fullName+"&salaryAnnivDate="+salaryAnnivDate;
            window.opener.document.budgetPersonsDynaBean.submit();
            window.close();
            
           <% } %>
            }
        </script>  
   

    </head>

    <body onload="populateResultData()" class='tabtable'>
  <% String srch = "/personSearch.do?type=+strType"; %>
<%if(resData == null) {%>
        <html:form action="<%=srch%>" method="post">
   
<%if(search!=null){%>

<%System.out.println("***yes***");%>   
        <div id='searchFormDiv'>
  
            <input type="hidden" name="fieldName" value='<%=fldName%>'>
            <input type="hidden" name="searchName" value='<%=searchName%>'>
    
            <table width="100%"  border="0" cellpadding="0" cellspacing="0">
    
                <tr class="theader">  
                <li>
                    <bean:message bundle="proposal" key="search.header1"/> <bean:message bundle="proposal" key="search.header2"/>
                    <b><bean:message bundle="proposal" key="search.header3"/> </b>
                    or <b><bean:message bundle="proposal" key="search.header5"/></b> or <b><bean:message bundle="proposal" key="search.header6"/></b> 
                    <bean:message bundle="proposal" key="search.header4"/>
                </li>
         
                </tr>
                <tr><td>&nbsp;</td></tr>
                <logic:iterate  id='criteria' name='criteriaList' indexId="index">

<%Integer count = (Integer)pageContext.getAttribute("index");
 
  boolean isNewRow = false;
 if(count.intValue() % 2 == 0){%>
                <tr>
 <%}%>
                <td>
                    <bean:define id='field' name='criteria' property='fieldBean' type="edu.mit.coeus.search.bean.FieldBean"/>
                    <bean:define id='fieldNames' name='field' property='name' type='java.lang.String'/>
    
                    <logic:equal name='field' property='type' value="list">
                        <bean:define id='fields' name='field' property='comboList' type='java.util.Vector'/>
                        <td  nowrap class="copy" align = "right" maxlength="20">
                            <b><%=field.getLabel()%></b>:&nbsp; 
                        </td>
                        <td maxlength="20">
                            <select name='<%=fieldNames%>'>
                                <!-- COEUSQA-1891-Multicampus enhancement - Start-->
                                <%if(CAMPUS.equalsIgnoreCase(field.getLabel())){ %>
                                <option value="0"> <bean:message key="search.allCampuses"/> </option>
                                <logic:iterate id="cmbBean" name="field" property="comboList" type="edu.mit.coeus.utils.ComboBoxBean"> 
                                    <%if(cmbBean.getCode().equals(campusCode) ){%>
                                    <option selected value="<%= cmbBean.getCode() %>"><%= cmbBean.getDescription() %></option>
                                    <%} else if(!"".equals(cmbBean.getCode())) {%>
                                    <option value="<%= cmbBean.getCode() %>"><%= cmbBean.getDescription() %></option>
                                    <%}%>
                                </logic:iterate>   
                                <%} else {%>
                                <!-- COEUSQA-1891-Multicampus enhancement - End-->
                                <logic:iterate id="cmbBean" name="field" property="comboList" type="edu.mit.coeus.utils.ComboBoxBean"> 
                                    <option value="<%= cmbBean.getCode() %>"><%= cmbBean.getDescription() %></option>
                                </logic:iterate>                
                                <%}%>
                            </select>
                        </td>
                    </logic:equal>
                    <logic:notEqual name='field' property='type' value='list'>
                        <td  nowrap class="copy" align="right" maxlength="20">
                            <b><%=field.getLabel()%></b>:&nbsp; 
                        </td>
                        <td maxlength="20"> 
                            <input class="textbox-long"
                            type='text' name='<%=fieldNames%>'/>&nbsp;&nbsp;
                        </td>
  	
                    </logic:notEqual>
    
                </td>
   
                </logic:iterate>
                <tr align="center">
                    <td colspan="9" class="copy"><br>
                        <span class="copy">
                            <html:submit property="Search" value="Search" styleClass="clbutton"/><a href="javascript:search();"></a>
                        </span>
                        &nbsp;&nbsp;&nbsp;
                        <span class="copy">
                            <html:button property="Cancel" value="Cancel" onclick="window.close();" styleClass="clbutton"/>
                            <br><br>
                        </span> 
                    </td>
                </tr>
            </table>
            <%--/html:form--%>
    <%}%>
        </div>
        <div id='searchingDiv' style='display: none;'>
            <table width="100%" height="100%" align="center" border="0" cellpadding="0" cellspacing="0">
                <tr>
                    <td align='center' class='copybold'>Searching . . . <br> please wait . . .</td>
                </tr>
            </table>
        </div>
    
    
  <%  if (!strSearch.equalsIgnoreCase("true")) {
    displayList = (Vector)searchResult.get("displaylabels");
    resList = (Vector)searchResult.get("reslist");
    String rowCount = (String)searchResult.get("rowcount");
    Vector colNames = new Vector();%>
    <%pageContext.setAttribute("rowCount",new Integer((rowCount!=null?rowCount:"0")));
    pageContext.setAttribute("displayList",displayList);
    int index = 0;
    if(resList!=null && !resList.isEmpty()){
%>
        <table>
        <table width='100%' class="tabtable" >
        <tr valign="center"><td class='tableheader'>
            &nbsp;<font color="#FFFFFF" size="2"><b>
            <font face="Verdana, Arial, Helvetica, sans-serif" color='black'>
                <bean:write name='searchinfoholder' property='displayLabel'/> Result
            </font></b></font></td>
        </tr>
        </table>
   
        <!-- Top section for displaying search again and close window-->
        <table width="100%" border="0" class='tabtable' cellspacing="0" cellpadding="0">
        <tr>
            <td align="right" class="theader" height='30'>
                <html:link href="javascript:history.go(-1)">
                    <bean:message  key="search.searchAgain"/>
                </html:link>
            </td>
            <td class='theader' height='30'>&nbsp;&nbsp;</td>
            <td align="left" class="theader"  height='30'>
                <html:link href="javascript://" onclick="window.close()">
                    <bean:message key="search.closeWindow"/>
                </html:link>
            </td>
        </tr>
        </table>   
        <table>
            <tr  class='theader'>  
    
                <logic:iterate id="dispBean" name="displayList" type="edu.mit.coeus.search.bean.DisplayBean">
        <%if(dispBean.isVisible()){
         colNames.add(dispBean.getName());%>        
        <%   if(dispBean.getName().equals("FULL_NAME")){
               %><td nowrap width="<bean:write name='dispBean' property='name'/>"><font face="Verdana, Arial, Helvetica, sans-serif" size='1'><b>
                    <bean:write name='dispBean' property='name'/> &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; &nbsp;&nbsp;
                    &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; </b></font></td>
              <%  }else { %><td width="<bean:write name='dispBean' property='name'/>"><font
                    face="Verdana, Arial, Helvetica, sans-serif" size='1'><b>
                    <bean:write name='dispBean' property='name'/></b></font></td>
              <%  } %>
              
        <%}%>
                </logic:iterate>
    
            </tr>
    

<%
	for(int resRowCnt=0; resRowCnt<resList.size(); resRowCnt++){
		HashMap resultRow = (HashMap)resList.get(resRowCnt);
%>
            <tr bgColor="<%= ((++index)%2==0?"#D6DCE5":"#DCE5F1") %>" onmouseover="className='TableItemOn'" onmouseout="className='TableItemOff'">
    
<%
		for(int colIndex=0; colIndex<colNames.size(); colIndex++){
			Object objValue = resultRow.get(colNames.get(colIndex));
			String value = objValue == null ? "&nbsp" : objValue.toString();
                        String searchLink = "javascript:getRowData('" +resRowCnt +"')";
			if (colIndex ==0) {                         

                            
%>                        
                                
            <td class='copy'>
                <html:link href="<%=searchLink%>">
                    <u><%=UtilFactory.dispEmptyStr(value)%></u>
                </html:link>
            </td>
<%
    			}else{
        			if(colIndex==1){
%>
            				

<%
        			}
%>
            <td class='copy'>
                <html:link href="<%=searchLink%>">
                    <u><%=UtilFactory.dispEmptyStr(value)%></u>
                </html:link>
            </td>
<%
    			}
    		}
%>

            </tr>
<%
	}
%>
        </table>
<%
    }else{
%>
        <logic:greaterThan name='rowCount' value='0'>
            <div align='center'><bean:message bundle="proposal" key="search.selection1"/><%=rowCount%><bean:message bundle="proposal" key="search.selection2"/>
            <br><bean:message bundle="proposal" key="search.selection3"/><%=retrieveLimit%><bean:message bundle="proposal" key="search.selection4"/>
            <br><bean:message bundle="proposal" key="search.selection5"/><div>
        </logic:greaterThan>
        <logic:equal name='rowCount' value='0'>
            <div align='center'><bean:message bundle="proposal" key="search.noResult"/></div>
        </logic:equal>
<%
    }
%>

        <!-- Bottom section for displaying search again and close window-->
        <table width="100%" border="0" class='tabtable' cellspacing="0" cellpadding="0">
        <tr>
            <td align="right" class="theader" height='30'>
                <html:link href="javascript:history.go(-1)">
                    <bean:message  key="search.searchAgain"/>
                </html:link>
            </td>
            <td class='theader' height='30'>&nbsp;&nbsp;</td>
            <td align="left" class="theader"  height='30'>
                <html:link href="javascript://" onclick="window.close()">
                    <bean:message key="search.closeWindow"/>
                </html:link>
            </td>
        </tr>
        </table>
        </table>


  <% } %>  
        </html:form>
<%}%>
    </body>
</html:html>
