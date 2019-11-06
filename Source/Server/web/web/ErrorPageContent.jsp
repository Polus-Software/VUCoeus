<%--
/*
 * @(#)ErrorPageContent.jsp
 *
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 *
 * Created on March 5, 2004, 11:22 AM
 *
 * @author  RaYaKu
 * @version 1.0
 */
--%>

<!-- Coeus Error Page -->
<%@page isErrorPage="true" import="edu.mit.coeus.exception.CoeusException,
                                   edu.mit.coeus.utils.UtilFactory,
                                   edu.mit.coeus.utils.dbengine.DBException"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<!-- CASE #665 Begin -->
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<!-- CASE #665 End -->

<%@include file="CoeusContextPath.jsp"  %>

<%
Exception objException = (Exception)request.getAttribute("EXCEPTION");
String exceptionType = "Server encountered an unknown error.";
String execCode = "";
String errorMessage  = "";
if(objException!=null){
           String strExceCode = "";
           if(objException instanceof CoeusException){
           		/* CASE #665 Changed message displayed to user here.  */	
			   exceptionType = " Coeus encountered an error. ";
			   CoeusException coeusEx = (CoeusException)objException;
			   execCode = coeusEx.getUserMessage();
			   errorMessage=coeusEx.getUserMessage();
			   //errorMessage+="<br />"+coeusEx.getMessage();
		   }else  if(objException instanceof DBException){
			    exceptionType = "Coeus encountered a Database error.";
			    DBException dbEx = (DBException)objException;
			    execCode = dbEx.getUserMessage();
			    execCode += dbEx.getMessage();
			    errorMessage=dbEx.getUserMessage();
			    errorMessage+="<br />"+dbEx.getMessage();
		   } else {
				errorMessage=objException.getClass().getName();
				errorMessage+="<br />"+objException.getMessage();
		   }

} 
else if(objException == null){
	System.out.println("objException == null.");
	errorMessage = exception.getClass().getName();
	errorMessage += "<br />"+exception.getMessage();
}
%>
<!-- CASE #665 Removed align="center" attribute".  Change width of table, to accomodate left nav panel. -->
  <table width="657" border="0" cellspacing="0" cellpadding="1" bgcolor="#CCCCCC">
    <tr>
      <td>
      <!-- CASE #665 Remove align="center" attribute". Change width of table, to accomodate left nav panel. -->
        <table width="657" border="0" cellspacing="0" cellpadding="0" >
          <tr>
            <td valign="top">
              <table width="100%" border="0" cellspacing="0" cellpadding="0" bgcolor="#FFFFFF">
                <tr bgcolor="#cccccc">
                <div>
                  <td height="23"> &nbsp;<font face="Verdana, Arial, Helvetica, sans-serif" size="2"><b>
                  <bean:message key="errorPage.header" />
                    </b></font></td>
                </div>
                </tr>
                <tr>
                  <td height="23">
                    <table width="100%" border="0" cellspacing="0" cellpadding="3">
                      <tr>
                        <td height="100">
                        <!-- CASE #665 Change font size and bold it to match the rest of the page.  -->
                          <div align="center"> <font face="Verdana, Arial, Helvetica, sans-serif" size="2"><b><%=exceptionType%> </b> </font></div>
                        </td>
                      </tr>
                      <tr>
			  <td height="100">
			  <!-- CASE #665 Change font size to match the rest of the page.  -->
			  <div align="center"> <font face="Verdana, Arial, Helvetica, sans-serif" size="2">
			  <%if(execCode.indexOf("exceptionCode") != -1){ %>
				<bean:message key="<%= errorMessage%>" />
				<%}else{%>
				<%=errorMessage%>
				<%}%>
				</font></b></div>
			  </td>
			</tr>			
                    </table>
                  </td>
                </tr>
                <tr>
                  <!-- CASE #665 Comment Begin -->
                  <%--
                  <td height="23">
                    <div align="center"><A href='javascript:history.back()'><img src="<bean:write name="ctxtPath" />/images/goback.gif" border='0' width="42" height="22"></A>
                    </div>
                  </td>
                  --%>
                  <!-- CASE #665 Comment End -->
                  <!-- CASE #665 Begin -->
                  <!-- If error is from duplicate submission, don't show Back Button.  Show OK button to take user to submitted financial entity or disclosure. -->
                  <td height="23" align="center" >
                  <logic:notPresent name="dupSubmission" scope="request">
			<a href='javascript:history.back()'><img src="<bean:write name='ctxtPath' />/images/goback.gif" border='0' width="42" height="22"></a>
		</logic:notPresent>
		<logic:present name="dupSubmission" scope="request">
			<logic:present name="dupSubmissionTempProp">
				<bean:message key="errorPage.dupTempPropSubmissionMsg" />
				<p />
				<a href="<bean:write name='ctxtPath'/>/getcoidisclosure.do"<img src="<bean:write name='ctxtPath'/>/images/ok.gif" border="0"></a>
			</logic:present>		
			<logic:present name="disclosureNumEP">
				<bean:message key="errorPage.dupDiscSubmissionMsg" />
				<p />
				<a href="<bean:write name='ctxtPath'/>/viewCOIDisclosureDetails.do?action=view&disclNo=<bean:write name='disclosureNumEP'/>"><img src="<bean:write name='ctxtPath'/>/images/ok.gif" border="0"></a>
			</logic:present>
			<logic:present name="entityNumber">
				<logic:equal name="actionType" value="I">
					<bean:message key="errorPage.dupInsertFEMsg" />
				</logic:equal>
				<logic:equal name="actionType" value="U">
					<bean:message key="errorPage.dupUpdateFEMsg" />
				</logic:equal>
				<p />
				<a href='<bean:write name="ctxtPath"/>/viewFinEntity.do?actionFrom=<bean:write name="actionFrom"/>&entityNo=<bean:write name="entityNumber"/>'><img src="<bean:write name='ctxtPath'/>/images/ok.gif" border="0"></a>				
			</logic:present>
		</logic:present>
		</td>
                  <!-- CASE #665 End -->
                  
                </tr>
              </table>
            </td>
          </tr>
        </table>
      </td>
    </tr>
  </table>