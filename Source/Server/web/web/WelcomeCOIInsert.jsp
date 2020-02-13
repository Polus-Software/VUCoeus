<%--
/*
 * @(#)WelcomeCOIInsert.jsp	1.0 2002/05/07	09:45:12AM
 *
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 *
 * @author  RaYaKu
 *
 */
--%>

<%@ page  errorPage="ErrorPage.jsp"
	import="edu.mit.coeus.utils.CoeusConstants,
		edu.mit.coeus.bean.PersonInfoBean"%>

<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/session.tld" prefix="sess" %>
<%@ taglib uri="/WEB-INF/privilege.tld" prefix="coeusPrivilege" %>

<%@ include file="CoeusContextPath.jsp"  %>

<jsp:useBean id="personInfo" scope ="session" class = "edu.mit.coeus.bean.PersonInfoBean" />
<jsp:useBean id="loggedinpersonid" scope="session" class="java.lang.String"/>

            <!-- CASE #761 Begin -->
            <!-- Show annual disclosure link based on boolean attribute of PersonInfoBean in session, BUT
            only if user has not done a select person.  If user has done select person, never show ann discl link -->
            <%
            System.out.println("personInfo.getPendingAnnDisclosure(): "+personInfo.getPendingAnnDisclosure());
            if(loggedinpersonid != null && personInfo.getPersonID() != null
            		&& personInfo.getPersonID().equals(loggedinpersonid) && personInfo.getPendingAnnDisclosure() ){
            %>
		    <tr>
		      <td>&nbsp;</td>
		      <td colspan="2">
			<b><font color="red"><bean:message key="welcomeCOI.annDiscDue"/></font></b>
			<html:link page="/getAnnDiscPendingFEs.do">
				<bean:message key="welcomeCOI.annDiscLink"/></html:link>						
		       </td>
		    </tr> 		    
	
	<%
		}
	%>