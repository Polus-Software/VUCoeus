<%-- 
    Document   : disclHeaderFooter
    Created on : May 8, 2010, 12:21:16 PM
    Author     : Mr
--%>

<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles"%>
<%@page
	import="java.util.HashMap,edu.mit.coeuslite.coiv2.utilities.CoiConstants"%>

<%--<%
// HashMap thisUserRights = (HashMap) request.getAttribute("rights");
         //   Integer val = (Integer) thisUserRights.get(CoiConstants.DISCL);
         //  boolean disable = false;
          //  if (val == 2) {
             //   disable =true ;
           // }

%>--%>

<html>
<head>
<link href="/mit/utils/css/ipf.css" rel="stylesheet" type="text/css">
</head>

<tiles:definition id="headerfooterbean"
	page="/coeuslite/mit/coiv2/admintiles/BaseLayout.jsp" scope="request">
	<tiles:put name="header"
		value="/coeuslite/mit/coiv2/jsp/coiMainHeaderCoiv2.jsp" />
	<tiles:put name="subheader"
		value="/coeuslite/mit/coiv2/jsp/showcoiv2submenu.jsp" />
	<tiles:put name="menu"
		value="/coeuslite/mit/coiv2/jsp/coiV2DisclMenu.jsp" />
	<tiles:put name="footer"
		value="/coeuslite/mit/irb/common/cwCopyright.jsp" />
	<tiles:put name="body1"
		value="/coeuslite/mit/coiv2/tile/bodyHeader.jsp" />
</tiles:definition>
</html>