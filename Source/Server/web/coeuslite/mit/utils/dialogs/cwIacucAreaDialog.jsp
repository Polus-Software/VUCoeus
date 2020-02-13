<%@ page contentType="text/html;charset=UTF-8" language="java"
	import="java.util.Hashtable,java.util.Vector,edu.mit.coeuslite.iacuc.action.TreeView;"%>
<%--<%@ page import="edu.mitweb.coeus.irb.action.TreeView;" %>  --%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ include file="/coeuslite/mit/utils/CoeusContextPath.jsp"%>
<%--<%@include file="treeview.jsp" %>--%>


<%--<jsp:useBean id="areas" scope="session" class="java.util.Vector" />--%>
<%-- <jsp:getProperty name="beanInstanceName"  property="propertyName" /> --%>
<%
String strType="";
String strForm="";
TreeView tv=new TreeView();
//TreeView tv= new TreeView();
//Hashtable htNodes= new Hashtable();
//Hashtable htCodes= new Hashtable(); 

HttpSession sessions = request.getSession();
if (sessions.getAttribute("areas") != null)
   tv = (TreeView)sessions.getAttribute("areas"); 
if (request.getParameter("type") != null)
  strType = request.getParameter("type");
if(request.getParameter("form") != null)
    strForm = request.getParameter("form");

System.out.println("************************************** ");
System.out.println("**** In cwAreaDialog.jsp           ** ");
System.out.println("strType="+strType);
System.out.println("strForm="+strForm);
System.out.println("************************************** ");
String exp = request.getParameter("expand");
boolean expand = false;
if(exp != null && exp.equals("true")){
    expand = true;
}
%>


<html:html locale="true">
<head>
<title>COEUS WEB</title>
<html:base />
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
<script language="javascript" type="text/JavaScript" src="_js.js"></script>
<link href="../css/ipf.css" rel="stylesheet" type="text/css">
<style type="text/css">
</style>
<link
	href="<bean:write name='ctxtPath'/>/coeuslite/mit/utils/css/coeus_styles.css"
	rel="stylesheet" type="text/css">
</head>
<body bgcolor="#376DAA">
	<%
   String srchStr="/iacucareaSearch.do?type=area&form= "+ strForm;
  // TreeView tv = (TreeView)session.getAttribute("areas"); 
  // String folder=request.getContextPath()+"/web images/coeusirb/folder";
   //tv.setImagesUrl(folder);
   //tv.setContextUrl(request.getContextPath());
%>

	<html:form action="<%=srchStr%>" method="post">
		<table width="100%" border="0" cellpadding="0" cellspacing="0"
			class="tabtable">
			<tr>
				<td colspan="2" class="theader">
					<table width="100%" border="0" cellspacing="0" cellpadding="10">
						<td class="copybold"><bean:message bundle="iacuc"
								key="areaDialog.researchListing" />
							<%--COEUS - Area Of Research Listing--%> <span class="copysmall">(<bean:message
									bundle="iacuc" key="areaDialog.areaByClickingName" />
								<%--choose the area of research by clicking the name--%>)
						</span></td>
					</table>
				</td>
			</tr>
			<tr>
			<tr>
				<td colspan="2">
					<table width="100%" border="0" cellspacing="0" cellpadding="10">
						<td class="copybold">
							<%if(expand){%> <html:link
								action="/iacucareaSearch.do?type=area&form=iacucAreaForm&expand=false">
								<u><bean:message key="areaDialog.collapseAll" /></u>
							</html:link> <%} else {%> <html:link
								action="/iacucareaSearch.do?type=area&form=iacucAreaForm&expand=true">
								<u><bean:message key="areaDialog.expandAll" /></u>
							</html:link> <%}%>

						</td>
					</table>
				</td>
			</tr>
			<tr>
				<td width="1%"><img
					src="<bean:write name='ctxtPath'/>/coeusliteimages/spacer.gif"
					width="1"></td>
				<td width="100%">
					<table width="99%" border="0" cellspacing="0" cellpadding="0">
						<tr>
							<td width="1%"><img
								src="<bean:write name='ctxtPath'/>/coeusliteimages/spacer.gif"
								height="22" width="10"></td>
							<td width="90%" class="copy">
								<%out.print(tv.getTree(expand));%>
							</td>
						</tr>
					</table>
				</td>
			</tr>
		</table>
	</html:form>
</body>
</html:html>
