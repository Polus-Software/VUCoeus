<jsp:useBean id="negotiationSubHeaderVector" scope="application" class="java.util.Vector" />
<% 
for (int index = 0;index<negotiationSubHeaderVector.size();index++) {
	edu.mit.coeuslite.utils.bean.SubHeaderBean subHeaderBean =(edu.mit.coeuslite.utils.bean.SubHeaderBean)negotiationSubHeaderVector.elementAt(index);
	String headerName = subHeaderBean.getSubHeaderName();
	String headerLink = subHeaderBean.getSubHeaderLink();
	String personLink = subHeaderBean.getProtocolSearchLink();
	String subHeaderId = subHeaderBean.getSubHeaderId();
	boolean isSelected = subHeaderBean.isSelected();
                                                    
	if (headerLink == null || headerLink.equals("EMPTY")) {%>
		<font face='Arial, Helvetica, sans-serif' size='1px' color='blue'> <%=headerName%> </font>    
                                                    
	<%}else {%>
		<%if(subHeaderId != null && subHeaderId.equalsIgnoreCase("SH002")){%>
			<a href="javaScript:open_negotiation_search('<%=headerLink%>');" class="menu"><%=headerName%></a>&nbsp;&nbsp;|&nbsp;&nbsp;
		<%}else if(subHeaderId != null && subHeaderId.equalsIgnoreCase("SH003")){%>
			<html:link page ="<%=headerLink%>" styleClass="menu">
			<% if(isSelected) { %>
				<font color="#6D0202"><b><%=headerName%></b>
			<%} else {%>
				<%=headerName%> 
			<%}%>
			</html:link>&nbsp;&nbsp;|&nbsp;&nbsp;                                                   
		<%}else{%>
			<html:link page ="<%=headerLink%>" styleClass="menu">
				<% if(isSelected) { %>
					<font color="#6D0202"><b><%=headerName%></b>
				<%} else {%>
          			<%=headerName%> 
				<%}%>
			</html:link>&nbsp;&nbsp;|&nbsp;&nbsp;
		<%}
	}
}%> 