<%--
/*
 * @(#)CoeusSearchError.jsp 1.0 05/07/2002 11:22:03AM
 *
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 *
 * @author  RaYaKu
 */
--%>

<%-- Error page for CoeusSearch --%>

<%@page  import="edu.mit.coeus.search.exception.CoeusSearchException,
                                   edu.mit.coeus.utils.UtilFactory,
                                   edu.mit.coeus.utils.dbengine.DBException"
                  isErrorPage="true" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>

<%@include file="CoeusContextPath.jsp"  %>

<%
Exception objException = (Exception)request.getAttribute("EXCEPTION");
//UtilFactory objUtilFact = new UtilFactory();
String exceptionType = "Server encountered an unknown error.";
String execCode = "";
String errorMessage  = "";
if(objException!=null){
            String strExceCode = "";
            if(objException instanceof CoeusSearchException){
                exceptionType = " Coeus Search encountered an exception";
                CoeusSearchException coeusEx = (CoeusSearchException)objException;
                errorMessage = coeusEx.getMessage();
            }else  if(objException instanceof DBException){
                exceptionType = "Coeus Search encountered Database error.";
                DBException dbEx = (DBException)objException;
                errorMessage = dbEx.getUserMessage();
            } else {
                errorMessage = objException.getMessage();
            }

} else {

	errorMessage = exception.getMessage();
	UtilFactory.log(exception.getMessage(),((Exception)exception),"Error Page","");
    //errorMessage = exception.getMessage();
}
%>
<html:html locale="true">
<head>
<title>Coeus Error Page</title>

<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
<style type="text/css">
div {font-family:verdana;font-size:10px}
a {font-family:verdana;font-size:10px;color:blue;text-decoration:none;}
a:active {color:red;text-decoration:none;}
a:visited {color:blue;text-decoration:none;}
a:hover {color:red;text-decoration:none;}
</style>

</head>

<body bgcolor="#FFFFFF" leftmargin="0" topmargin="0" marginwidth="0" marginheight="0">
  <table width="780" border="0" cellspacing="0" cellpadding="1" align="center" bgcolor="#CCCCCC">
    <tr>
      <td>
        <table width="780" border="0" cellspacing="0" cellpadding="0" align="center">
          <tr>
            <td valign="top">
              <table width="100%" border="0" cellspacing="0" cellpadding="0" bgcolor="#FFFFFF">
                <tr bgcolor="#cccccc">

                <td height="23"> &nbsp;<font face="Verdana, Arial, Helvetica, sans-serif" size="2"><b>
                   Exception occured in the search </b></font></td>

                </tr>
                <tr>
                  <td height="23">

                  <table width="100%" border="0" cellspacing="0" cellpadding="3">
                    <tr>
                      <td height="40">
                        <div align="center"> <font size="3">

                          </font></div>
                      </td>
                    </tr>
                    <tr>
                      <td height="24">
                        <div align="center"> <font face="Verdana, Arial, Helvetica, sans-serif" size="2">
                                <%=errorMessage%>
                        </font></div>
                      </td>
                    </tr>
                  </table>
                  </td>
                </tr>
                <tr>
                  <td height="23">
                    <div align="center">
                        <A href='javascript:history.back()'>
                            <img src="<bean:write name='ctxtPath'/>/images/goback.gif" border='0' width="42" height="22"></A>
                    </div>
                  </td>
                </tr>
              </table>
            </td>
          </tr>
        </table>
      </td>
    </tr>
  </table>

</body>
</html:html>
