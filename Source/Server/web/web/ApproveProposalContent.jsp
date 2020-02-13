<%--
/*
 * @(#)ApproveProposalContent.jsp
 *
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 *
 * Created on
 *
 * @author  Coeus Dev Team
 * @version 1.0
 */
--%>
<%@ page language="java" %>
<%@ page  errorPage="ErrorPage.jsp"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix ="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix ="logic" %>

<%@ include file="CoeusContextPath.jsp"  %>

<%	System.out.println("begin ApproveProposalContent.jsp 2");	%>

<style type="text/css">
	body, td, th, p, a, h4, h3, h2, h1 {font-family:verdana, helvetica, sans-serif}
	body, td, th, p, a, h4 {font-size: 10px}
	h1 {font-size: 16px}
	h2 {font-size: 14px}
 	h3 {font-size: 12px}
	div {font-family:verdana;font-size:10px}
	a {color:blue;text-decoration:none;}
	a:active {color:red;text-decoration:none;}
	a:visited {color:blue;text-decoration:none;}
	a:hover {color:red;text-decoration:none;}
</style>
<html:html>
  <table width="100%" border="0" cellspacing="0" cellpadding="5">
    <html:form action="/ApproveProposalAction.do" focus="comments">
    <tr bgcolor="#cccccc">
    	<td height="23" >
    		<h2><b>
    		<logic:equal name="ApproveProposalForm" property="actionMode" value="ByPass">
		  <bean:message key="approveProposal.bypass" />
		</logic:equal>
		<logic:equal name="ApproveProposalForm" property="actionMode" value="Reject">
		  <bean:message key="approveProposal.reject" />
		</logic:equal>
		<logic:equal name="ApproveProposalForm" property="actionMode" value="Approve">
		  <bean:message key="approveProposal.approve" />
		</logic:equal>
		</b></h2>
    	</td>
    </tr>
      <tr>
	  <td><div><font color="#7F1B00"><bean:message key="comments.label"/></div></font></td>
      </tr>
      <tr>
  	  <td>
  	    <font color="red"><html:errors property="comments"/></font>
   	    <textarea name="comments" cols="50" rows="10">
   	    <logic:equal name="ApproveProposalForm" property="actionMode" value="ByPass">
   	    <bean:message key="approveProposal.bypass.defaultComment" />
   	    </logic:equal>
          </textarea>
	  </td>
      </tr>
      <tr>
  	  <td>
   	    <html:hidden property="proposalNumber">
          </html:hidden>
   	    <html:hidden property="mapId">
          </html:hidden>
   	    <html:hidden property="levelNumber">
          </html:hidden>
   	    <html:hidden property="stopNumber">
          </html:hidden>
   	    <html:hidden property="updateTimeStamp">
          </html:hidden>
   	    <html:hidden property="updateUser">
          </html:hidden>
   	    <html:hidden property="userId">
          </html:hidden>
   	    <html:hidden property="actionMode">
          </html:hidden>
	  </td>
      </tr>
      <tr>
	  <td>
        <html:image page="/images/submit.gif" border="0" />
        <a href="JavaScript:history.back();">
	<img src="<bean:write name="ctxtPath"/>/images/cancel.gif" width="55" height="22" border="0">
	</a>
        </td>
      </tr>
    </html:form>
  </table>
</html:html>
