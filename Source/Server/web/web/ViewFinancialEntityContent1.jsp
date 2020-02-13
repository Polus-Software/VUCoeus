<%--
/*
 * @(#)ViewFinancialEntityContent.jsp  1.0.1 2002/06/08 11:45:12
 *
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.

 * @author  RaYaKu
 */
--%>
<%--
A View component to show the Financial Entity Details
--%>
<%@page errorPage = "ErrorPage.jsp" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" 	prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" 	prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" 	prefix="logic" %>
<%@ taglib uri="/WEB-INF/privilege.tld" prefix="priv" %>
<%@ taglib uri="/WEB-INF/coeus-utils.tld"	prefix="coeusUtils" %>
<%@ include file="CoeusContextPath.jsp"  %>
<jsp:useBean id="actionFrom" scope="request" class="java.lang.String"/>

<jsp:useBean id="personName" scope="request" class="java.lang.String" />
<jsp:useBean id="entityNo" scope="request" class="java.lang.String" />
<!-- CASE #1400 Comment next line -->
<%--<jsp:useBean id="seqNo" scope="request" class="java.lang.String" />--%>
<jsp:useBean id="entityDetails" scope="request" class="edu.mit.coeus.coi.bean.EntityDetailsBean" />
<jsp:useBean id="colFinCertificationDetails" 	scope="request" class="java.util.Vector" />
<jsp:useBean id = "userprivilege" class = "java.lang.String" scope="session"/>
<%-- CASE#212 Begin --%>
<jsp:useBean id = "loggedinpersonid" class = "java.lang.String" scope="session"/>
<jsp:useBean id = "personInfo" class = "edu.mit.coeus.bean.PersonInfoBean" scope="session"/>
<%-- CASE#212 End --%>
<%
 //create a hash map which holds the parameter names and values
java.util.HashMap htmlLinkValues = new java.util.HashMap();
htmlLinkValues.put("actionFrom", actionFrom); 
htmlLinkValues.put("entityNo", entityNo); 
//case #1400 comment: htmlLinkValues.put("seqNo", seqNo);
pageContext.setAttribute("htmlLinkValues", htmlLinkValues);
%>
<table width="100%" cellpadding="0" cellspacing="0" border="0">
      <tr>
        <td background="<bean:write name="ctxtPath"/>/images/separator.gif" width="1">
            <img src="<bean:write name="ctxtPath"/>/images/separator.gif" width="1">
        </td>
        <td width="645">
          <table width="100%" border="0" cellspacing="0" align="left" cellpadding="0">
            <tr>
             <td height="5"></td>
           </tr>
           <tr bgcolor="#cccccc">
            <td height="23" > &nbsp;<font face="Verdana, Arial, Helvetica, sans-serif" size="2"> <b>
				<bean:message key="viewFinEntity.header" />
				<coeusUtils:formatOutput name="entityDetails" property="name" defaultValue="&nbsp;" scope="request"/>
				</b></font>
			</td>
		</tr>
		<tr valign="top">
			<td height="23">
              <table width="100%" border="0" cellspacing="0" cellpadding="0">
				<tr>
				<td height="30">
					 <div align="left">
						<table width="100%" border="0" cellspacing="0" cellpadding="0">
							<tr>
							 <td>
								<div align="left">&nbsp;<font color="#663300"><b>
									<font color="#7F1B00">
									<bean:message key="viewFinEntity.personName" /> :
									<bean:write name="personName" scope="request" />
									</font></b></font>
								</div>
							</td>
							<td height="40" align="right">
									<a href="JavaScript:history.back();">
									<img src="<bean:write name="ctxtPath"/>/images/goback.gif" height="22" border="0"></a>&nbsp;
									
<%-- Create an image button which submits the page with assigned querystring information(htmlLinkvalues) --%>

<%--
    *    CASE#212 Begin
    *
    *    IF userprivilege is 2, then user has Maintain COI role.  Then the user should be able to edit any financial entity.
    *    Make Edit button visible.
    *
    *    loggedinpersonid holds currently logged in user's person ID
    *    personInfo Holds person details of the currenlty selected person (whose disclosures the user is viewing)
    *    if loggedinpersonid = personInfo.getPersonID() then user is viewing his own financial entity.
    *    else he is viewwing someone else's financial entity
    *    If a user is viewing his own financial entity then
    *    the user can edit the disclosure, make Edit button visible
--%>

<!-- CASE #399 Change width attribute to 42 for img tag for edit button -->

 <%
                if (Integer.parseInt(userprivilege) == 2)
                {
                  System.out.println("User Priv = 2");
                  %>
                  <html:link page="/editFinEntity.do" name="htmlLinkValues" >
				<img src="<bean:write name="ctxtPath"/>/images/edit.gif" width="42" height="22" border="0">
			</html:link>
                  <%
                } else
                {
                %>
                <%-- CASE #352 Begin --%>
                <%--
                  if (loggedinpersonid.equals(personInfo.getPersonID()))
                --%>
                 <%
                 if(entityDetails.getPersonId().equals(loggedinpersonid))
                 {
                  %>
                    <html:link
			page="/editFinEntity.do" name="htmlLinkValues" >
				<img src="<bean:write name="ctxtPath"/>/images/edit.gif" width="42" height="22" border="0">
			</html:link>
		<%
                  }
                }
                %>
                <%-- CASE #352 End --%>
<%-- CASE#212 End   --%>
							</td>
							</tr>
			 			</table>
			 		</div>
			 	</td>
			 	</tr>
		    </table>
			 <table width="100%" border="0" cellspacing="0" cellpadding="0">
				<tr>
					<td background="<bean:write name="ctxtPath"/>/images/tabback.gif">
						<table border=0 cellpadding=0 cellspacing=0>
						<tr>
						<td><img src="<bean:write name="ctxtPath"/>/images/entitydetails.gif" width="120" height="24"></td>
						</tr>
						</table>
					</td>
				</tr>
		      </table>
<!-- insert ViewFinancialEntityContentInsert.jsp here -->
<jsp:include page="ViewFinancialEntityContentInsert.jsp" />
<!-- end of insert  -->

		  </td>
	  </tr>
	  <tr>
		<td height="2">&nbsp;</td>
	  </tr>
	  <tr>
		<td height="23">
		  <table width="100%" border="0" cellspacing="0" cellpadding="0">
			<tr>
			  <td background="<bean:write name="ctxtPath"/>/images/tabback.gif">
				<table border=0 cellpadding=0 cellspacing=0>
				  <tr>
					<td><img src="<bean:write name="ctxtPath"/>/images/certification.gif" width="120" height="24"></td>
				  </tr>
				</table>
			  </td>
			</tr>
		  </table>
		  <!-- Certification-->
		  <table width="100%" border="0" cellspacing="1" cellpadding="5">
			<tr>
			  <td colspan="2" height="5"></td>
			</tr>
			<tr>

			  <td width="450" height="25" bgcolor="#CC9999">
				<div align="center"><font color="#FFFFFF">
					<bean:message key="viewFinEntity.label.question"/>
				</font></div>
			  </td>
			  <td width="128" height="25" bgcolor="#CC9999">
				<div align="center"><font color="#FFFFFF">
					<bean:message key="viewFinEntity.label.answer"/>
				</font></div>
			  </td>
			</tr>
			<!-- Certication Details of Financial Entity -->
			<logic:present  name="colFinCertificationDetails"  >
			<%	int count = 0;	%>
               	<logic:iterate id="certficateDetails" name="colFinCertificationDetails" type="edu.mit.coeus.coi.bean.CertificateDetailsBean">

            <tr bgcolor='<%=( (count) % 2 == 0 ? "#FBF7F7" : "#F7EEEE") %>'>
			 <%-- <td width="85" height="25" valign="top">
				<div align="center"><font face="Verdana, Arial, Helvetica, sans-serif">
			    	<a href='javascript:showQuestion("<bean:write name='ctxtPath' />/question.do?questionNo=<%=certficateDetails.getCode()%>",  "ae");'  method="POST">
				<coeusUtils:formatOutput name="certficateDetails" property="code" defaultValue="&nbsp;" />
				   </a>
				</font></div>
			  </td> --%>
			  <td valign="top">
				<div align="justify">
				<!-- CASE #1393 Begin -->
				<b><bean:message key="viewFinEntity.label.question"/>				
				<coeusUtils:formatOutput name="certficateDetails" property="label"/></b>:
				<!-- CASE #1393 End -->
				<coeusUtils:formatOutput name="certficateDetails" property="question" defaultValue="&nbsp;"/>
				<a href='javascript:showQuestion("<bean:write name='ctxtPath' />/question.do?questionNo=<%=certficateDetails.getCode()%>",  "ae");'  method="POST">
				<bean:message key="viewFinEntity.question.explanationLink" />
				</a>
				</div>
			  </td>
			  <td height="25" valign="top">
				<div align="center"> <font face="Verdana, Arial, Helvetica, sans-serif">
					<coeusUtils:formatOutput name="certficateDetails" property="answer" defaultValue="&nbsp;" />
				 </font></div>
			  </td>
			</tr>
			<%	count++;	%>
				</logic:iterate>
			</logic:present>
			</table>
			<table width="100%" border="0" cellspacing="0" cellpadding="0">
			<tr>
			  <td height="40" >
				<div align="right"><a href="JavaScript:history.back();"><img src="<bean:write name="ctxtPath"/>/images/goback.gif" width="55" height="22" border="0"></a>&nbsp;
				<%-- CASE #352 Comment Begin --%>
				<%--
                                <priv:hasOSPRight name="hasOspRightToEdit" value="<%=Integer.parseInt(userprivilege)%>">
					<html:link page="/editFinEntity.do" name="htmlLinkValues" >
							<img src="<bean:write name="ctxtPath"/>/images/edit.gif" width="42" height="22" border="0">
					</html:link>
                                  </priv:hasOSPRight>
                                  <priv:hasOSPRight name="noRights" value="<%=Integer.parseInt(userprivilege)%>">
				<%-- Create an image button which submits the page with assigned querystring information(htmlLinkvalues) --%>
				<%--
					<html:link page="/editFinEntity.do" name="htmlLinkValues" >
							<img src="<bean:write name="ctxtPath"/>/images/edit.gif" width="42" height="22" border="0">
					</html:link>
                                  </priv:hasOSPRight>
                                  --%>
                                <%-- CASE #352 Comment End --%>

				<%-- Create an image button which submits the page with assigned querystring information(htmlLinkvalues) --%>
				<%-- CASE #352 Begin --%>
				<%

				if (Integer.parseInt(userprivilege) == 2)
				{
				  %>
				  <html:link page="/editFinEntity.do" name="htmlLinkValues" >
						<img src="<bean:write name="ctxtPath"/>/images/edit.gif" width="42" height="22" border="0">
					</html:link>
				  <%
				} else
				{
				 if(entityDetails.getPersonId().equals(loggedinpersonid))
				 {
				  %>
				  <!-- CASE #399 Change width attribute to 42 for img tag for edit.gif -->
				    <html:link
					page="/editFinEntity.do" name="htmlLinkValues" >
						<img src="<bean:write name="ctxtPath"/>/images/edit.gif" width="42" height="22" border="0">
					</html:link>
				<%
				  }
				}
				%>
				<%-- CASE #352 End --%>

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