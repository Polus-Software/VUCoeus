<!--
/*
 * @(#)ViewPendingDiscContent.jsp 1.0 
 *
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 *
 * @author  coeus-dev-team
 */
-->

<!--
Display all disclosures pertaining to temporary proposals.
-->

<%@ page import="java.util.Vector,
		edu.mit.coeus.utils.UtilFactory,
		edu.mit.coeus.coi.bean.DisclosureHeaderBean"
	errorPage = "ErrorPage.jsp" %>
	
<%@ taglib uri="/WEB-INF/struts-html.tld"     prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld"     prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld"    prefix="logic" %>
<%@ taglib uri="/WEB-INF/request.tld"		prefix="req" %>
<%@ taglib uri="/WEB-INF/coeus-utils.tld"	prefix="coeusUtils" %>

<%@ include file="CoeusContextPath.jsp"  %>

<!-- Find out the size of collPendingDisc (see the parent page,ViewPendingDisc.jsp)-->
<bean:size id="collPendingDiscSize" name="collPendingDisc" />

<table width="100%" cellpadding="0" cellspacing="0" border="0">
     <tr>
        <td background="<bean:write name="ctxtPath"/>/images/separator.gif" width="1">
            <image src="<bean:write name="ctxtPath"/>/images/separator.gif" width="1">
        </td>
        <td width="645">
	  	<table width="100%" border="0" cellspacing="0" align="left" cellpadding="0">
	  		<tr>
	  		<td>
	  			<table width="100%" border="0" cellspacing="0" cellpadding="5">
		  		<tr bgcolor="#cccccc">

				  <td height="23" class="header" > &nbsp;
				    <b><bean:message key="viewPendingDisc.header" />
				    </b>
				  </td>
				  </tr>
				  <tr>
					<td><div><bean:message key="viewPendingDisc.introduction" /></div>
					</td>
				  </tr>
				  </table>
			</td>
		  </tr>
		  <tr>
		    <td height="23">
		      <table width="100%" border="0" cellspacing="0" cellpadding="0">
			<tr>
			  <td background="<bean:write name="ctxtPath"/>/images/tabback.gif">
			    <table border=0 cellpadding=0 cellspacing=0>
			      <tr>
				<td> <img src="<bean:write name="ctxtPath"/>/images/coidisclosure.gif" width="120" height="24"></td>
			      </tr>
			    </table>
			  </td>
			</tr>
		      </table>
	  	<table width="100%" border="0" cellspacing="0" align="left" cellpadding="0">

		  <tr>
		    <td height="23">

		      <!-- Listing All COI Disclosures for Temporary Proposals -->

		      <table width="100%" border="0" cellspacing="1" cellpadding="5" align="right" >

			<tr bgcolor="#CC9999" >
			  <td width="130" height="25">
			    <div align="center"><font color="#FFFFFF">
				<bean:message key="viewPendingDisc.label.piName" />
				</font>
				</div>
			</td>
			  <td width="80" height="25">
			    <div align="center"><font color="#FFFFFF">
						<bean:message key="coiDisclosure.label.disclosurenumber" /></font>
				</div>
			</td>
			  <td width="110" height="25">
			    <div align="center"><font color="#FFFFFF"><bean:message key="coiDisclosure.label.sponsor" /></font></div>
			  </td>

			  <td width="270" height="25" >
			    <div align="center"><font color="#FFFFFF"><bean:message key="coiDisclosure.label.title" /></font></div>
			  </td>
			  <td width="54" height="25" >
			    <div align="center"><font color="#FFFFFF">
			    <bean:message key="viewPendingDisc.label.updateTimestamp" />
			    </font></div>
			  </td>


			</tr>
					<%-- show the results in alternate colors --%>
			<% int disclosureCount=0; %>
			<logic:present name="collPendingDisc"  scope="session" >
			<!-- CASE #810 Begin -->
			<logic:equal name="collPendingDiscSize" value="0">
				<tr>
				<td colspan="5" align="center">
					<font size="2" face="Verdana, Arial, Helvetica, sans-serif">
					<bean:message key="viewPendingDisc.noPendingDisc"/>
					</font>
				</td>
				</tr>
			</logic:equal>
			<!-- CASE #810 End -->
				<logic:iterate id="discHeader" name="collPendingDisc" type="DisclosureHeaderBean">
			<tr valign="left" bgcolor='<%=( (disclosureCount++) % 2 == 0 ? "#FBF7F7" : "#F7EEEE") %>'>
			<td height="25">
				<div align="left">
				<coeusUtils:formatOutput name="discHeader" property="name"/>
				</div>
			</td>
			<td height="25">
			    <div align="left">
				    <coeusUtils:formatOutput name="discHeader" property="disclosureNo"/>
			    </div>
			  </td>

			  <td height="25">
			    <div align="left">
			    <coeusUtils:formatOutput name="discHeader" property="sponsor" defaultValue="&nbsp;" />
			    </div>
			  </td>
			  <td height="25" >
			    <div align="left">
			    <coeusUtils:formatOutput name="discHeader" property="title" defaultValue="&nbsp;" />
			    </div>
			  </td>
			  <td height="25" >
			    <div align="left">
			    <coeusUtils:formatDate name="discHeader" property="updatedDate" />
			    </div>
			  </td>


			</tr>
			</logic:iterate>
			</logic:present>
			<!--<tr>
			  <td width="91" height="25">&nbsp;</td>
			  <td width="63" height="25">&nbsp;</td>
			  <td width="69" height="25">&nbsp;</td>
			  <td width="79" height="25">&nbsp;</td>
			  <td width="103" height="25">&nbsp;</td>
			  <td width="79" height="25">&nbsp;</td>
			  <td width="135" height="25">
			    <div align="right"> </div>
			  </td>
			</tr>-->
		      </table>
				<!-- End of listing all COI Disclosures for temp proposals and their details -->
		    </td>
		  </tr>
      </table>
     </td>
    </tr>

</table>



            </td>
      </tr>
</table>
