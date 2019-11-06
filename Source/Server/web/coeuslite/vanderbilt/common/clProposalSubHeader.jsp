<jsp:useBean id="proposalSubHeader" scope="session" class="java.util.Vector" />
<% 
for (int index=0;index<proposalSubHeader.size();index++) {
	edu.mit.coeuslite.utils.bean.SubHeaderBean subHeaderBean = (edu.mit.coeuslite.utils.bean.SubHeaderBean) proposalSubHeader.get(index);
    String subHeaderId = subHeaderBean.getSubHeaderId();
    String headerName = subHeaderBean.getSubHeaderName();
    String headerLink = subHeaderBean.getSubHeaderLink();
    String proposalLink = subHeaderBean.getProtocolSearchLink();
    boolean isSelected = subHeaderBean.isSelected();
                                   
  	if (headerLink == null || headerLink.equals("EMPTY")) {
    %>
    	<font face='Arial, Helvetica, sans-serif' size='1px' color='blue'> <%=headerName%> </font>
    <%
    } 
    else if (proposalLink.equals("false")) {
    	String link = "javascript:return callMenu('"+request.getContextPath()+headerLink+"')";
		%>
		<html:link page ="<%=headerLink%>" styleClass="menu" onclick="<%=link%>">
		<% if(isSelected) { %>
			<font color="#6D0202" size='2px'>
			<b><%=headerName%></b>
		<% } else { %>
       		<%=headerName%> 
		<% } %>
		</html:link>&nbsp;&nbsp;|&nbsp;&nbsp;
		<%
	}
	else if (proposalLink.equals("true")) { %>
		<% if(isSelected) { %>
			<font color="#6D0202" size='2px'>
			<b><%=headerName%></b>
		<% } else { %>
			<a href="javaScript:open_proposal_search('<%=headerLink%>');" class="menu" onclick="return callMenu('')"><%=headerName%></a>&nbsp;&nbsp;|&nbsp;&nbsp;
		<%
		}
	}
}
%>       
