<%--
/*
 * @(#)GetFinancialEntitiesContent.jsp	1.0 2002/06/08	15:07:03
 *
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 *
 * @author  RaYaKu
 */
--%>
<%--
A View component that dispalys all Financial Entities for  user

This page is using HNCTemplate.jsp as a  template that supports and layouts the
components in a standard fashion.
--%>
<%@page errorPage = "ErrorPage.jsp" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld"     prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld"     prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld"     prefix="logic" %>
<%@ taglib uri="/WEB-INF/coeus-utils.tld"     prefix="coeusUtils" %>
<%@ include file="CoeusContextPath.jsp"  %>
<jsp:useBean id="personName" scope="request" class="java.lang.String" />
<jsp:useBean id="allFinancialEntities" scope="request" class="java.util.Vector" />
<bean:size id="totalFinancialEntities" name="allFinancialEntities" />

<table width="100%" cellpadding="0" cellspacing="0" border="0">
<tr>
    <td >
    <%--background="<bean:write name="ctxtPath"/>/images/separator.gif" width="1">
        <image src="<bean:write name="ctxtPath"/>/images/separator.gif" width="1">
     --%>
    </td>
<td width="645">
  <table width="100%" border="0" cellspacing="0" align="left" cellpadding="0">
	<tr>
	<td height="5"></td>
  </tr>
  <tr bgcolor="#cccccc">
	<td height="23" > &nbsp;<font face="Verdana, Arial, Helvetica, sans-serif" size="2"> <b>
	   <bean:message key="financialEntity.header"/>
	   <bean:write name="personName" scope='request' />

	 </b></font>
	</td>
  </tr>
    			<tr>
    				<td>&nbsp;</td>
  			</tr>
  	<tr>
  		<td>
  		<table width="100%" cellspacing="0" cellpadding="5">
			<tr>
				<td width="40%" align="left">
					&nbsp;&nbsp;<html:link page="/addFinEntity.do?actionFrom=coiFinEntity">
					<img border="0" 
					src='<bean:write name="ctxtPath"/>/images/add_financial_entity.gif'>
					</html:link>
				</td>
				<td width="40% align="right">
					<html:link forward="loginCOI">
					<img border="0" 
					src='<bean:write name="ctxtPath"/>/images/finished_financial_entities.gif'>
					</html:link>
				</td>
				<td width="20%" align="right">
					<a href="javascript:openmediumwin('<%=request.getContextPath()%>/web/whatsAFinEnt.html','getFinEnt1');">
					<img border="0" 
					src='<bean:write name="ctxtPath"/>/images/help.gif'></a>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;		
				</td>
			</tr>
			<tr>
			<td colspan="3" height="30" valign="bottom">
			<font class="fontBrown"><b><bean:message key="financialEntity.introduction" /></b></font>
			<%--
			<bean:message key="financialEntity.introduction1" />
			<a  href="COIInfo.html" onClick="javascript:openwintext('<%=request.getContextPath()%>/web/COIInfo.html','');return false;">
			<bean:message key="financialEntity.additionalInfo" /></a> 
			<bean:message key="financialEntity.additionalInfo2"/>
			<bean:message key="financialEntity.introduction2"/>
			--%>
			</td>
			</tr>			
  		</table>
		</td>
  	</tr>
  	<logic:present name="backToDiscl">
  	<tr>
  		<td>
  		<b>&nbsp;
		<logic:equal name="backToDiscl" value="addDiscl">
			<a href="<bean:write name="ctxtPath"/>/addCOIDisclosure.do?actionFrom=editFinEnt"><bean:message key="financialEntity.backToAddDiscl"/></a>
		</logic:equal>
		<logic:equal name="backToDiscl" value="editDiscl">
			<a href="<bean:write name="ctxtPath"/>/viewCOIDisclosureDetails.do?action=edit&actionFrom=editFinEnt"><bean:message key="financialEntity.backToEditDiscl"/></a>
		</logic:equal>
		<logic:equal name="backToDiscl" value="annDiscCert">
			<a href="<bean:write name="ctxtPath"/>/annDiscCertification.do?actionFrom=editFinEnt"><bean:message key="financialEntity.backToAnnDisc"/></a>
		</logic:equal>
		</b>
		</td>
	</tr>
	<tr>
		<td height="2">&nbsp;</td>
	</tr>
  	</logic:present>  	
  <tr>

	<td height="23">
	<table width="100%" border="0" cellspacing="0" cellpadding="0">
	<tr>
	  <td background="<bean:write name="ctxtPath"/>/images/tabback.gif">
		<table border=0 cellpadding=0 cellspacing=0>
		  <tr>
			<td><img src="<bean:write name='ctxtPath'/>/images/financialentity.gif" width="120" height="24"></td>
		  </tr>
		</table>
	  </td>
	</tr>
  </table>
  <!-- List All Financial Entities and their details -->
  <!-- CASE #912 Change table width = 100% -->
  <table width="100%" border="0" cellspacing="1" cellpadding="5" align="right">
	<tr>
	  <td colspan="5" height="5"></td>
	</tr>
	<tr bgcolor="#CC9999">
	  <td width="190" height="25">
		<div align="center"><font color="#FFFFFF"><bean:message key="financialEntity.entityName"/> </font></div>
	  </td>
	  <td width="74" height="25">
		<div align="center"><font color="#FFFFFF"><bean:message key="financialEntity.statusCode"/> </font></div>
	  </td>
	  <td width="147" height="25">
		<div align="center"><font color="#FFFFFF"><bean:message key="financialEntity.relationshipToEntity"/> </font></div>
	  </td>
	  <td width="156" colspan="2">&nbsp;</td>
	</tr>

	<logic:present name="allFinancialEntities"  scope="request" >
	<%	int count=0;	%>
	<logic:iterate id="entity" name="allFinancialEntities" type="edu.mit.coeus.coi.bean.EntityDetailsBean">
	<tr bgcolor='<%=( (count) % 2 == 0 ? "#FBF7F7" : "#F7EEEE") %>'>
	  <td height="25">
		<div align="left">
		<!-- CASE #1221 Comment Begin -->
		<%--<a href="<bean:write name="ctxtPath"/>/viewFinEntity.do?actionFrom=coiFinEntity&entityNo=<bean:write name='entity' property='number'/>" method="POST">
			   <bean:write name='entity' property='name' />
		</a>
		--%>
		<!-- CASE #1221 Comment End -->
		<!-- CASE #1221 Begin -->
		<!-- seq no no longer needed for view fin ent url -->
		&nbsp;
		<a href="<bean:write name="ctxtPath"/>/viewFinEntity.do?actionFrom=coiFinEntity&entityNo=<bean:write name='entity' property='number'/>" method="POST">
		<bean:write name='entity' property='name' />
		</a>
		<!-- CASE #1221 End -->
		</div>
	  </td>
	  <td height="25">
		<div align="left">
        <logic:present name="entity" property="status" >
            <logic:equal name="entity" property="status"  value = '1'>
                <bean:message key="financialEntity.active"/>
            </logic:equal>
            <logic:notEqual name="entity" property="status"  value = '1'>
                <bean:message key="financialEntity.inactive"/>
           </logic:notEqual>
       </logic:present>
	  </div>
	 </td>
	  <td height="25">
		<div align="left">
		&nbsp;
		<bean:write name="entity" property="personReType" />
		</div>
	  </td>
	<td align="center">
		<font color="blue">[</font>
		<logic:equal name="entity" property="status"  value = '1'>
			<a href="<bean:write name="ctxtPath"/>/deactivateFinEnt.do?entityNo=<bean:write name='entity' property='number'/>" method="POST">
				<bean:message key="financialEntity.removeLink"/>
			</a>
		</logic:equal>
		<logic:notEqual name="entity" property="status"  value = '1'>
			<a href="<bean:write name="ctxtPath"/>/activateFinEnt.do?entityNo=<bean:write name='entity' property='number'/>&actionFrom=coiFinEntity" method="POST">
				<bean:message key="financialEntity.makeActiveLink"/>
			</a>
		</logic:notEqual>		
		<font color="blue">]</font>
	</td>
	<td>
		<font color="blue">[</font>
		<a href="<bean:write name="ctxtPath"/>/editFinEntity.do?actionFrom=coiFinEntity&entityNo=<bean:write name='entity' property='number'/>&actionFrom=coiFinEntity" method="POST">
					<bean:message key="financialEntity.editLink"/>
		</a>
		<font color="blue">]&nbsp;</font>
	</td>
	  
	</tr>
	<%	count++;	%>
	</logic:iterate>
	</logic:present>

	<logic:equal name="totalFinancialEntities"  value="0" >
		<tr>
		<td  colspan="5" height="2">
		&nbsp;
		</td>
		</tr>	
		<tr>
		<td align="center" width="100%"  colspan="3">
		<bean:message key="financialEntity.result"/> 
		</td>
		</tr>
		<!-- CASE #737 End -->
	</logic:equal>
  </table>
  <!-- End of Listing All Financial Entities and their details -->
 </td>
 </tr>
 <tr>
 <td height="23">&nbsp; </td>
 </tr>
 <tr>
 <td>
   	<logic:greaterThan name="totalFinancialEntities" value="5">
   		<table width="100%" cellspacing="0" cellpadding="5">
			<tr>
				<td width="40%" align="left">
					&nbsp;&nbsp;<html:link page="/addFinEntity.do?actionFrom=coiFinEntity">
					<img border="0" 
					src='<bean:write name="ctxtPath"/>/images/add_financial_entity.gif'>
					</html:link>
				</td>
				<td width="40% align="right">
					<html:link forward="loginCOI">
					<img border="0" 
					src='<bean:write name="ctxtPath"/>/images/finished_financial_entities.gif'>
					</html:link>
				</td>
				<td width="20%" align="right">
					<a href="javascript:openwintext('<%=request.getContextPath()%>/web/whatsAFinEnt.html','getFinEnt1');">
					<img border="0" 
					src='<bean:write name="ctxtPath"/>/images/help.gif'></a>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;		
				</td>
			</tr>
  		</table>
  	</logic:greaterThan>
 </td>
 </tr>
</table>
</td>
</tr>
</table>
<!-- CASE #1374 Begin -->
<!-- Show response window to confirm ent added or modified -->
<% if(request.getAttribute("FESubmitSuccess") != null){
	request.removeAttribute("FESubmitSuccess");
	System.out.println("FESubmitSuccess is not null");
	String message = (String)request.getAttribute("entityName");
	String actionType = (String)request.getAttribute("actionType");
	String reloadLocation = request.getContextPath()+"/getFinEntities.do";
	if(actionType != null){
		if(actionType.equals("I")){
			message += " successfully added.";
		}
		else if(actionType.equals("U") ){
			message += " successfully modified.";
		}
		else if(actionType.equals("activate") ){
			message += " made active.";
		}
		else if(actionType.equals("deactivate") ){
			message += " made inactive.";
		}
	}
	int indexOfApost = message.indexOf("'");
	if(indexOfApost != -1){
		int indexOfApostIncr = indexOfApost+1;
		int endOfMessage = message.length();
		String message1 = message.substring(0, indexOfApost);
		String message2 = message.substring(indexOfApostIncr, message.length());
		message = message1+message2;
	}	
	out.print("<script language='javascript'>");
	out.print("confirm('"+message+"');");
	//out.print("openwintext('FESubmitSuccess.jsp', 'getFinEnt3');");//can't use because of popup blockers
	out.print("window.location='"+reloadLocation+"';");
	out.print("</script>");
}

%>
<!-- CASE #1374 End -->