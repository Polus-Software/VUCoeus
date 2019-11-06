<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/coeus-utils.tld" prefix="coeusUtils"%>
<%@ include file="/coeuslite/mit/utils/CoeusContextPath.jsp"%>
<%@page import="edu.mit.coeus.negotiation.bean.NegotiationHeaderBean"%>

<html>
<head>
<title>ARRA Award Details</title>
</head>
<%try{
    NegotiationHeaderBean negotiationHeaderBean=(NegotiationHeaderBean)session.getAttribute("negotiationHeaderBean");
    String proposalNumber = negotiationHeaderBean.getProposalNumber() == null?"":negotiationHeaderBean.getProposalNumber();
    String title = negotiationHeaderBean.getTitle() == null? "":negotiationHeaderBean.getTitle();
    String leadUnit = negotiationHeaderBean.getLeadUnit();
    String unitName = negotiationHeaderBean.getUnitName()==null?"":negotiationHeaderBean.getUnitName();
    String piName = negotiationHeaderBean.getPiName()== null ?"" :negotiationHeaderBean.getPiName();
    String sponsorName = negotiationHeaderBean.getSponsorName()== null?"":negotiationHeaderBean.getSponsorName();
    String sponsorCode = negotiationHeaderBean.getSponsorCode();   
    //COEUSQA-119 : View negotiations in lite - Get UpdateTimeStamp - Start 
    String lineImage = request.getContextPath()+"/coeusliteimages/line4.gif";
    String lastUpdateTimestamp = negotiationHeaderBean.getLastUpdateTimestamp().toString()==null?"":negotiationHeaderBean.getLastUpdateTimestamp().toString();
    String lastUpdateUserName = negotiationHeaderBean.getLastUpdateUser()==null?"":negotiationHeaderBean.getLastUpdateUser();
    //COEUSQA-119 - End
    String EMPTYSTRING ="";
    if(title!=null && (!title.equals(EMPTYSTRING))){
        title=title.length()>120?title.substring(0,121)+"...":title;
    }  
    
%>
<body topmargin="0" leftmargin="0">

	<table width='100%' border="0" cellpadding="2" cellspacing="0"
		class="table">
		<tr>
			<td width="10%" nowrap class='copybold'>&nbsp;&nbsp;&nbsp;<bean:message
					bundle="negotiation" key="negotiationHeader.investigator" />&nbsp;
			</td>
			<td width="40%" class='copy' nowrap><%=piName%></td>
			<td nowrap class='copybold' align="right"><bean:message
					bundle="negotiation" key="negotiationHeader.proposalNumber" />&nbsp;</td>
			<td class='copy' nowrap><%=proposalNumber%></td>
		</tr>

		<tr>
			<td width="10%" nowrap class='copybold'>&nbsp;&nbsp;&nbsp;<bean:message
					bundle="negotiation" key="negotiationHeader.sponsor" />&nbsp;
			</td>
			<td width="40%" class='copy' nowrap>
				<%if(sponsorCode != null && !"  ".equalsIgnoreCase(sponsorCode)){%> <%=sponsorCode%>
				: <%=sponsorName%> <%}%>
			</td>
			<td width="10%" nowrap class='copybold' align=left>&nbsp;</td>
			<td width="40%" class='copy' nowrap></td>
		</tr>

		<tr>
			<td width="10%" nowrap class='copybold'>&nbsp;&nbsp;&nbsp;<bean:message
					bundle="negotiation" key="negotiationView.title" />&nbsp;
			</td>
			<td width="40%" class='copy' nowrap colspan="3">
				<%if(title != null && !"  ".equalsIgnoreCase(title)){%> <%=title%> <%}%>
			</td>
		</tr>

		<tr>
			<td class='copybold' nowrap colspan="4"><html:img
					src="<%=lineImage%>" width="100%" height="2" border="0" /></td>
		</tr>


		<tr>
			<td width="10%" nowrap class='copybold'>&nbsp;&nbsp;&nbsp;<bean:message
					bundle="negotiation" key="negotiationHeader.leadUnit" />&nbsp;
			</td>
			<td width="40%" class='copy' nowrap>
				<%if(leadUnit != null && !"  ".equalsIgnoreCase(leadUnit)){%> <%=leadUnit%>
				: <%=unitName%> <%}%>
			</td>
			<td width="10%" nowrap class='copybold' align=left></td>
			<td width="40%" class='copy' nowrap></td>
		</tr>

		<tr>
			<td width="10%" nowrap class='copybold'>&nbsp;&nbsp;&nbsp;<bean:message
					bundle="negotiation" key="negotiationHeader.lastUpdate" />&nbsp;
			</td>
			<td width="40%" class='copy' nowrap><%=lastUpdateTimestamp%> <b>by
			</b> <%=lastUpdateUserName%></td>
			<td width="10%" nowrap class='copybold' align=left></td>
			<td width="40%" class='copy' nowrap></td>
		</tr>


	</table>

</body>
<%}catch(Exception e){ e.printStackTrace(); }%>
</html>