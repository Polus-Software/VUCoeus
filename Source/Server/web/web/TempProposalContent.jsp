<%--
/*
 * @(#)TempProposalContent.jsp 1.0	2002/06/11	1:55:20AM
 *
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 *
 * @author  RaYaKu
 */
--%>
<%-- This JSP file is for ALLOWING THE USER TO CREATE TEMPORARY PROPOSALS--%>
<%@page errorPage = "ErrorPage.jsp" 
	import = "org.apache.struts.action.Action"
%>
 <%@ taglib uri="/WEB-INF/struts-html.tld"     prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld"     prefix="bean" %>
<%@ taglib uri="/WEB-INF/coeus-utils.tld"     prefix="coeusUtils" %>

<%@ include file="CoeusContextPath.jsp"  %>
<jsp:useBean id="collProposalTypes" scope="session" class="java.util.Vector" />

<jsp:useBean id ="srchRefresh" scope ='request' class = "java.lang.String" />

<table width="100%"  cellpadding="0" cellspacing="0" border="0">
<html:form action="/createTempProposal.do">
<tr>
<!--
<td background="<bean:write name="ctxtPath"/>/images/separator.gif" width="1">
    <image src="<bean:write name="ctxtPath"/>/images/separator.gif" width="1">
</td>
-->
<td  width="657" valign="top">
<table width="100%" border="0" cellspacing="0" cellpadding="0">
<tr>
<td height="5"></td>
</tr>
<tr bgcolor="#cccccc">
		<td height="23" class="header"> &nbsp;
		&nbsp;<bean:message key="tempProposal.header" />
		</td>
</tr>
<tr>
<td height="23">
  <table width="100%" border="0" cellspacing="0" cellpadding="0">
	<tr>
	  <td background="<bean:write name='ctxtPath'/>/images/tabback.gif">
		<table border=0 cellpadding=0 cellspacing=0>
		  <tr>
			<td><img src="<bean:write name='ctxtPath'/>/images/details.gif" width="120" height="24"></td>
		  </tr>
		</table>
	  </td>
	</tr>
  </table>

  <%-- show errors if any --%>
    <!-- CASE #1393 Comment Begin -->
  <% //if(srchRefresh.equalsIgnoreCase("D")) { %>
  <%--
    <html:errors/>
  --%>
  <% //} %>
  <!-- CASE #1393 Comment End -->
  <%--
  <logic:equal id="srchRefresh" value="D">
    <html:errors/>
  </logic:equal>
  --%>



  <table width="100%" border="0" cellspacing="0" cellpadding="5">
	<!-- CASE #1393 Begin -->
  	<% if(srchRefresh.equalsIgnoreCase("D")) { %>
		<tr>
		<td colspan = "8">
		<logic:present name="<%=Action.ERROR_KEY%>">
			<b><bean:message key="validationErrorHeader"/></b>
			<bean:message key="validationErrorSubHeader1"/>
			<html:errors/>
			</font>
		</logic:present>   	
		</td>
		</tr>
	<% } %>
	<!-- CASE #1393 End -->
	<tr>
		<td colspan="8" class="fontBrown">
			<b><bean:message key="tempProposal.label.introduction" /></b>
		</td>
	</tr>
	<tr>
		<td colspan="8">
			<b><font class="mediumsizered">*</font></b>
			<font class="mediumsizebrown"> indicates required field</font>
		</td>
	</tr>	
	<tr bgcolor="#FBF7F7">
	  <td width="99" height="25">
		<div align="left"><bean:message key="tempProposal.label.proposalType"/> </div>
	  </td>
	  <td width="6" height="25">
		<div align="left"><font face="Verdana, Arial, Helvetica, sans-serif" size="2">:</font></div>
	  </td>
	  <td valign="top" width="2" align="right">
	  	<font class="mediumsizered"><b>&nbsp;*</b></font>
	  </td>
	  <td height="25" colspan="3">
		  <html:select property="typeCode">
			<html:options collection="collProposalTypes" property="code"    labelProperty="description"  />
		  </html:select>
	  </td>
	  <td height="20" width="123">&nbsp;</td>

	  <td  height="25" width="113">
		<div align="left"><font face="Verdana, Arial, Helvetica, sans-serif" size="2">&nbsp;</font></div>
	  </td>
	  <td  height="25" width="8">
		<div align="left"><font face="Verdana, Arial, Helvetica, sans-serif" size="2">&nbsp;</font></div>
	  </td>
	</tr>
	<%--
	<tr bgcolor="#F7EEEE">
	  <td height="25">
		<div align="left"><bean:message key="tempProposal.label.status"/></div>
	  </td>
	  <td height="25">
		<div align="left"><font face="Verdana, Arial, Helvetica, sans-serif" size="2">:</font></div>
	  </td>
	  <td>&nbsp;</td>
	  <td colspan="5" height="25">
		<div align="left"><b></b> <b><bean:message key="tempProposal.label.temporary"/></b></div>
	  </td>
	  <td height="20" width="10">&nbsp;</td>
	</tr>
	--%>
	<tr bgcolor="#F7EEEE">
	  <td height="25">
		<div align="left"><bean:message key="tempProposal.label.title"/></div>
	  </td>
	  <td  height="25">:</td>
	  <td valign="top" width="2" align="right">
	  	<font class="mediumsizered"><b>&nbsp;*</b></font>
	  </td>	  
	  <td colspan="5" height="25">
	  	<font face="Verdana, Arial, Helvetica, sans-serif" size="2">
		<%--<html:textarea property="title"  cols="40" rows="2" />--%>
                <textarea name="title"
                    wrap='virtual' cols="40" rows="2" ><bean:write name='frmTempProposal'
                    property='title'/></textarea>
		</font></td>
	  <td height="20" width="10">&nbsp;</td>
	</tr>
	<tr bgcolor="#FBF7F7">
	  <td height="25">
		<div align="left"><bean:message key="tempProposal.label.piName"/> </div>
	  </td>
	  <td height="25">
		<div align="left"><font face="Verdana, Arial, Helvetica, sans-serif" size="2">:</font></div>
	  </td>
	  <td>&nbsp;</td>
	  <td height="25" colspan="3">
		<div align="left"> <b><bean:write name="frmTempProposal" property="primInvestigaterName"/> </b></div>
	  </td>
	  <td  height="25">
		<div align="left">&nbsp;</div>
	  </td>
	  <td height="25">
		<div align="left">&nbsp;</div>
	  </td>
	  <td  height="20" >&nbsp;</td>
	</tr>
	<!-- CASE #1374 Comment Begin -->
	<%--
	<tr bgcolor="#FBF7F7">
	  <td width="99" height="25">
		<div align="left"><bean:message key="tempProposal.label.leadUnit"/></div>
	  </td>
	  <td width="6" height="25">
		<div align="left"><font face="Verdana, Arial, Helvetica, sans-serif" size="2">:</font></div>
	  </td>
	  <td width="28" height="25">
		 <font face="Verdana, Arial, Helvetica, sans-serif" size="2">
                    <html:text property="leadUnit" size="9" />
		 </font>
	  </td>

	  <td width="24" height="25"><font face="Verdana, Arial, Helvetica, sans-serif" size="2">
		<a href="javascript:openSearchWin('<bean:write name='ctxtPath'/>/coeusSearch.do?searchname=leadUnitSearch&fieldName=leadUnit','leadunit');"><img src="<bean:write name='ctxtPath'/>/images/searchpage.gif" width="22" height="20" border="0"></a></font>
        </td>

	  <td colspan="4" bgcolor="#FBF7F7">
	  	<div align = "left">
		  <coeusUtils:formatOutput name="frmTempProposal" property="leadUnitName"/>
		  </div>
	  </td>
	</tr>
	--%>
	<!-- CASE #1374 Comment End -->
	<tr bgcolor="#F7EEEE">
	  <!-- CASE #1374 Comment Begin -->
	  <%--
	  <td width="99" height="20">
		<div align="left"><bean:message key="tempProposal.label.sponsor"/></div>
	  </td>
	  <td width="6" height="20">
		<div align="left"><font face="Verdana, Arial, Helvetica, sans-serif" size="2">:</font></div>
	  </td>
	  <td width="28" height="20" >
		<font face="Verdana, Arial, Helvetica, sans-serif" size="2">
		 <html:text property="sponsorId" size="9"/>
		</font>
	  </td>

	  <td width="24" >
          <font face="Verdana, Arial, Helvetica, sans-serif" size="2">
            <a href="javascript:openSearchWin('<bean:write name='ctxtPath'/>/coeusSearch.do?searchname=sponsorSearch&fieldName=sponsorId','sponsor');">
               <img src="<bean:write name='ctxtPath'/>/images/searchpage.gif" width="22" height="20" border="0"></a>
          </font>
        </td>
        <td width="254">
		<font face="Verdana, Arial, Helvetica, sans-serif" size="1">
		<bean:message key="tempProposal.nsf.nih.codes"/>
        </font></td>
	--%>
	<!-- CASE #1374 Comment End -->
	
	  <td height="25">
		<div align="left">
		<bean:message key="tempProposal.label.sponsorName"/>
		</div>
	  </td>
	  <td height="25">
		<div align="left"><font face="Verdana, Arial, Helvetica, sans-serif" size="2">:</font></div>
	  </td>	 
	  <td valign="top" width="2" align="right">
	  	<font class="mediumsizered"><b>&nbsp;*</b></font>
	  </td>	  

	  <td colspan="6">
		<font face="Verdana, Arial, Helvetica, sans-serif" size="2">
		  <html:text property="sponsorName" size="25"/>
  	      </font>

	  </td>
	</tr>
	<tr bgcolor="#FBF7F7">
	  <td  height="25">
		<div align="left"><bean:message key="tempProposal.label.comments"/></div>
	  </td>
	  <td height="25">
		<div align="left"><font face="Verdana, Arial, Helvetica, sans-serif" size="2">:</font></div>
	  </td>
		<td>&nbsp;</td>
	  <td colspan="5" height="25">
		<div align="left"><font face="Verdana, Arial, Helvetica, sans-serif" size="2">
		<%--<html:textarea property="comments"  cols="40" rows="2" />--%>
                    <textarea name="comments"
                        wrap='virtual' cols="40" rows="2" ><bean:write name='frmTempProposal'
                                property='comments'/></textarea>
		  </font></div>
	  </td>
	  <td height="20" >&nbsp;</td>
	</tr>
	<tr>
	  <td  height="40">&nbsp;</td>
	  <%-- <td  height="40">&nbsp;</td> --%>
	  <td  colspan="3">&nbsp;</td>
	  <td colspan="3" height="40">
		<div align="right">
		<a href="JavaScript:history.back();"><img src="<bean:write name='ctxtPath'/>/images/goback.gif" width="55" height="22" border="0"></a>&nbsp;
		<!-- CASE #665 Comment Begin -->
	   	<%--<html:image src="images/submit.gif"  border="0" />--%>
	   	<!-- CASE #665 Comment End -->
	   	<!-- CASE #665 Begin -->
	   	<!-- Replace submit button with continue button -->
	   	<html:image page="/images/continue.gif"  border="0" />
	   	<!-- CASE #665 End -->
		  &nbsp; &nbsp; &nbsp;
		  </div>
	  </td>
	  <td >&nbsp;</td>
	</tr>
  </table>
</td>
</tr>
<tr>
<td height="23">&nbsp; </td>
</tr>
</table></td>
</tr>
<%-- keep little information in hidden controls --%>
<html:hidden property="logStatus" value="T"/>
<html:hidden property="primInvestigaterName" />
<html:hidden property="primInvestigaterId" />
<html:hidden property="leadUnitName" />
<html:hidden property="searchRefresh" />
</html:form>

</table>



