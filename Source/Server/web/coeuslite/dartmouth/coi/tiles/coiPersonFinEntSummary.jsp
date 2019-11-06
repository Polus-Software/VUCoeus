<%@page contentType="text/html"
	import="edu.mit.coeus.utils.CoeusProperties,edu.mit.coeus.utils.DateUtils,
java.util.Calendar,
edu.mit.coeus.utils.CoeusPropertyKeys,
org.apache.struts.validator.DynaValidatorForm,
edu.dartmouth.coeuslite.coi.beans.DisclosureBean,
edu.mit.coeuslite.utils.ComboBoxBean,
java.util.Vector"%>
<%@page pageEncoding="UTF-8" import="edu.mit.coeus.bean.*"%>

<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/coeus-utils.tld" prefix="coeusUtils"%>
<%@ include file="/coeuslite/mit/utils/CoeusContextPath.jsp"%>
<jsp:useBean id="person" scope="session"
	class="edu.mit.coeus.bean.PersonInfoBean" />
<%--<jsp:useBean id="CurrDisclPer"  scope="session" class="edu.dartmouth.coeuslite.coi.beans.DisclosureBean" />--%>
<<jsp:useBean id="perFinEntDet" scope="session" class="java.util.Vector" />
<bean:size id="totSize" name="perFinEntDet" scope="session" />
<jsp:useBean id="annDisclFinEntity" scope="session"
	class="org.apache.struts.validator.DynaValidatorForm" />
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
"http://www.w3.org/TR/html4/loose.dtd">

<html>
<head>
<%String path = request.getContextPath();
        String policyPath=CoeusProperties.getProperty(CoeusPropertyKeys.POLICY_FILE);
        String tempEntNumber="";
        Integer relnType=new Integer(0);
        int index=0;
        Vector vecRelationship = new Vector();  
        String fullName="";
        if(request.getAttribute("FullName")!=null)
        fullName=(String)request.getAttribute("FullName");
ComboBoxBean relationType = new ComboBoxBean();
relationType = new ComboBoxBean();
relationType.setCode("1");
relationType.setDescription("Self");
vecRelationship.addElement(relationType);
relationType = new ComboBoxBean();
relationType.setCode("2");
relationType.setDescription("Spouse");
vecRelationship.addElement(relationType);
relationType = new ComboBoxBean();
relationType.setCode("3");
relationType.setDescription("Other Family");
vecRelationship.addElement(relationType);
relationType = new ComboBoxBean();
relationType.setCode("4");
relationType.setDescription("Student/Staff");
vecRelationship.addElement(relationType);
session.setAttribute("relationTypes",vecRelationship);%>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>COI Financial Entity Summary</title>
<link href="<%=path%>/coeuslite/dartmouth/utils/css/coeus_styles.css"
	rel="stylesheet" type="text/css">
<script src="<%=path%>/coeuslite/dartmouth/utils/scripts/divSlide.js"></script>
<script src="<%=path%>/coeuslite/dartmouth/utils/scripts/Balloon.js"></script>

<table width="982" align="left" border="0" cellpadding="0"
	cellspacing="0" class="table">
	<tr class="theaderBlue">
		<td align="right"><a href="javascript:self.close()">Close the
				window</a></td>
	</tr>
	<tr class="theaderBlue">
		<td>Financial Entity Detatils of <%=fullName%></td>
	</tr>
	<logic:present name="perFinEntDet" scope="session">
		<logic:notEqual name='totSize' value='0'>
			<logic:iterate id="vecFinEntity" name="perFinEntDet"
				type="java.util.Vector">
				<logic:iterate id="entity" name="vecFinEntity"
					type="org.apache.commons.beanutils.DynaBean">
					<bean:define id="entitynumber" name="entity"
						property="entityNumber" />
					<logic:notEqual name="entitynumber"
						value="<%=tempEntNumber.toString()%>">
						<%if(index>0){%>
					
</table>
</td>
</tr>
<%}%>
<tr class="theader">
	<td><coeusUtils:formatOutput name="entity" property="entityName" /></td>
</tr>
<tr>
	<td><table>
			</logic:notEqual>
			<bean:define id="rlnCode" name="entity" property="rlnType" />
			<logic:notEqual name="rlnCode" value="<%=relnType.toString()%>">

				<tr class="copybold">
					<td align="left" colspan="4"><logic:iterate id="rlnBean"
							name="relationTypes" scope="session"
							type="edu.mit.coeuslite.utils.ComboBoxBean">
							<logic:equal name="rlnBean" property="code"
								value="<%=rlnCode.toString()%>">
								<coeusUtils:formatOutput name="rlnBean" property="description" />
							</logic:equal>
						</logic:iterate></td>
				</tr>
				<% relnType=(Integer)rlnCode;%>
			</logic:notEqual>
			<%-- <tr><td></td><td><coeusUtils:formatOutput name="entity" property="groupName"/></td></tr>--%>
			<tr class="copybold">
				<td width="50px">&nbsp;&nbsp;&nbsp;</td>
				<td><coeusUtils:formatOutput name="entity"
						property="columnName" /></td>
			</tr>
			<logic:equal name="entity" property="guiType" value="DROPDOWN">
				<tr class="copy">
					<td>&nbsp;&nbsp;&nbsp;</td>
					<td>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<coeusUtils:formatOutput
							name="entity" property="columnValue" /></td>
				</tr>
			</logic:equal>
			<logic:equal name="entity" property="guiType" value="CHECKBOX">
				<tr class="copy">
					<td>&nbsp;&nbsp;&nbsp;</td>
					<td>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<coeusUtils:formatOutput
							name="entity" property="comments" /></td>
				</tr>
			</logic:equal>
			<%tempEntNumber=entitynumber.toString();%>
			<%index++;%>
			</logic:iterate>

			</logic:iterate>
			</logic:notEqual>
			<logic:equal name='totSize' value='0'>
				<tr>
					<td class="copybold"><bean:message bundle="coi"
							key="Annualdisclosure.Review.noFE" /></td>
				</tr>
			</logic:equal>
			</logic:present>
		</table></td>
</tr>
<tr class="theaderBlue">
	<td align="right"><a href="javascript:self.close()">Close the
			window</a></td>
</tr>
</table>
</body>
</html>
