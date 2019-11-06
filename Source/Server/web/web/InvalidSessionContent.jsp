<%--
/*
 * @(#)InvalidSessionContent.jsp 1.0	05/19/2002   08:00:25 AM
 *
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 *
 * @author  RaYaKu
 */
--%>
<!--
A session expiration page that will be shown to the user when there is no such
user existing in application or user is inactive with application after specified session active time.
-->
<%@page errorPage = "ErrorPage.jsp" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ include file="CoeusContextPath.jsp"  %>

<!-- CASE #665  Removed 'align=center' from table tag  -->
  <table width="780" border="0" cellspacing="0" cellpadding="1"  bgcolor="#CCCCCC">
    <tr>
      <td>
      <!-- CASE #665  Removed 'align=center' from table tag  -->
        <table width="780" border="0" cellspacing="0" cellpadding="0" >
          <tr>
            <td valign="top">
              <table width="100%" border="0" cellspacing="0" cellpadding="0" bgcolor="#FFFFFF">
                <tr bgcolor="#cccccc">
                  <td height="23"> &nbsp;
                  <font face="Verdana, Arial, Helvetica, sans-serif" size="2">
                  		<b> <bean:message key="sessionexpired.header" /> </b>
                  </font></td>
                </tr>
                <tr>
                  <td height="23">
                    <table width="100%" border="0" cellspacing="0" cellpadding="3">
                      <tr>
                        <td height="100">
                          <div align="center">
                          	<!-- CASE #665 Comment Begin -->
                          	<%--<font size="3"><bean:message key="sessionexpired.message" /> </font>--%>
                          	<!-- CASE #665 Comment End -->
                          	<!-- CASE #665 Begin -->
                          	<!-- Put font properties in properties file, along with message.  -->
                          	<bean:message key="sessionexpired.message" />
                          	<!-- CASE #665 End -->
                          	</div>
                        </td>
                      </tr>
                    </table>
                  </td>
                </tr>
                <tr>
                  <td height="23">
                    <div align="center"><A href='<bean:write name="ctxtPath"/>/loginCOI.do'><img src="<bean:write name="ctxtPath"/>/images/ok.gif" border='0' width="42" height="22">
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
