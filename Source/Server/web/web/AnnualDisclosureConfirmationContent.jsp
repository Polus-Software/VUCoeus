<!--
/*
 * @(#)AnnualDisclosureConfirmationContent.jsp	1.0 2002/06/12 01:32:13 AM
 *
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 *
 * @author  RaYaKu
 */
-->


<%@ page import="edu.mit.coeus.utils.CoeusConstants"
	errorPage="ErrorPage.jsp"%>
	
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri='/WEB-INF/privilege.tld' prefix='coeusPrivilege' %>

<jsp:useBean id = "personInfo" class = "edu.mit.coeus.bean.PersonInfoBean" scope="session"/>

<%@ include file= "CoeusContextPath.jsp"  %>

<%	System.out.println("Inside AnnualDisclosureConfirmation.jsp");	%>

<table width="779" border="0" cellspacing="0" cellpadding="0" align="center">
  <tr> 
    <td width="779" valign="top"> 
      <table width="100%" border="0" cellspacing="0" cellpadding="0">
        <form name="frmForm">
          <tr> 
            <td height="5"></td>
          </tr>
          <tr bgcolor="#cccccc"> 
            <td height="23" class="header"> &nbsp;
            <bean:message key="annDisclConf.header"/>
                    <bean:write name="personInfo" property="fullName"/></td>
          </tr>
          <tr> 
            <td height="23">
              <div align="center"> 
                <table width="100%" border="0" cellpadding="0">
                  <tr> 
                    <td>&nbsp;</td>
                  </tr>
                  <tr> 
                    <td height="49"> 
                    <!-- CASE #399 added font face Verdana, changed font size to 2 -->
                      <div align="center"><b><font face="Verdana, Arial, Helvetica, sans-serif" size="2">
                        <bean:message key="annDisclConf.headerContent"/>
                     </font></b></div>
                    </td>
                  </tr>
                  <tr> 
                    <td>
                    <!-- CASE #399 added font face Verdana, added bold -->
                      <div align="center"><font face="Verdana, Arial, Helvetica, sans-serif" size="2"><b>
                        <bean:message key="annDisclConf.confirmation"/>
                      </b></font></div>
                    </td>
                  </tr>
                  <tr> 
                    <td> 
                      <div align="center">
                        <html:link page="/annualDisclsFinalUpdate.do">
                            <img src="<bean:write name="ctxtPath"/>/images/submitannualdisclosure.gif" 
                            width="163" height="24" border="0">
                            </html:link>&nbsp;
                            	<html:link forward="loginCOI">
                            		<img src="<bean:write name="ctxtPath"/>/images/cancel.gif" width="56" height="23" 
                            		border="0">
                        	</html:link>
                     </div>
                    </td>
                  </tr>
                </table>
              </div>
            </td>
          </tr>
        </form>
      </table>
    </td>
  </tr>
</table>