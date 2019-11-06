<jsp:useBean id="inboxSubHeader" scope="application" class="java.util.Vector" />
<% 
for (int index = 0;index<inboxSubHeader.size();index++) {
	edu.mit.coeuslite.utils.bean.SubHeaderBean subHeaderBean =(edu.mit.coeuslite.utils.bean.SubHeaderBean)inboxSubHeader.elementAt(index);
    String headerName = subHeaderBean.getSubHeaderName();
    String headerLink = subHeaderBean.getSubHeaderLink();
    String proposalLink = subHeaderBean.getProtocolSearchLink();
    if (headerLink == null || headerLink.equals("EMPTY")) {%>
    	<font face='Arial, Helvetica, sans-serif' size='1px' color='blue'> <%=headerName%> </font>
	<% 
	} else if (proposalLink.equals("false")) {
	%>
		<html:link page ="<%=headerLink%>" styleClass="menu"><%=headerName%> </html:link>&nbsp;&nbsp;|&nbsp;&nbsp;
	<%
	} else if (proposalLink.equals("true")) { %>
		<a href="javaScript:open_protocol_search('<%=headerLink%>');" class="menu"><%=headerName%></a>&nbsp;&nbsp;|&nbsp;&nbsp;
	<%
	}
}
%>