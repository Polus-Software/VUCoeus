<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/coeus-utils.tld" prefix="coeusUtils"%>
<%@ include file="/coeuslite/mit/utils/CoeusContextPath.jsp"%>
<%@page
	import="java.sql.Date, edu.mit.coeuslite.iacuc.bean.ProtocolHeaderDetailsBean, java.text.SimpleDateFormat"%>

<html>
<head>
<title>Protocol Details</title>
</head>
<%try{%>
<body>
	<%
    ProtocolHeaderDetailsBean headerBean=(ProtocolHeaderDetailsBean)session.getAttribute("iacucHeaderBean");
    //String strPage = request.getParameter("PAGE");
    String strPage =(String) session.getAttribute("iacucNewAmendRenew");
    boolean isNotAR = true;
    isNotAR = strPage!= null && (strPage.equals("NA")|| strPage.equals("NR") 
    || strPage.equals("RA") || strPage.equals("CR") || strPage.equals("CA"))? false : true;
    // Case # 3799: Provide the protocol # in the heading of the New Renewal - Start
    String newAmendment = "New Amendment";
    String newRenewal = "New Renewal";
    String newRenewalAmendment = "New Renewal/Amendment";
    String newContinuationReview = "New Continuation/Continuing Review";
    String newContinuationReviewAmend = "New Continuation/Continuing Review with Amendment";
    // Display the Header Information for New Amendment and New Renewal Page also.
    // if(headerBean!=null && (isNotAR)){
    if(headerBean!=null){
    // Case # 3799: Provide the protocol # in the heading of the New Renewal - End
     String EMPTYSTRING ="";
     String UNDEFINED="undefined";
    String updateTimestamp = headerBean.getUpdateTimeStamp();
    String updateUser =headerBean.getUpdateUser();
    String createTimestamp =headerBean.getCreateTimestamp();
    String createUser = headerBean.getCreateUser();
    String title = headerBean.getTitle().trim();
    if(title!=null && (!title.equals(EMPTYSTRING))){
        title=title.length()>60?title.substring(0,61)+"...":title;
    }else{title=EMPTYSTRING;}
    String leadUnit=headerBean.getLeadUnit();
    if(leadUnit!=null && (!leadUnit.equals(EMPTYSTRING))){
        leadUnit=leadUnit.length()>60?leadUnit.substring(0,61)+"...":leadUnit;
    }else{leadUnit=EMPTYSTRING;}
    Date ApprovalDate=headerBean.getApprovalDate();
    Date ExpiryDate = headerBean.getExpirationDate();
    String strApprovalDate=EMPTYSTRING;
    String strExpiryDate =EMPTYSTRING;
    
    if(ApprovalDate!=null){
            SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
            strApprovalDate = dateFormat.format(ApprovalDate);
    }
    
    if(ExpiryDate!=null){
            SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
             strExpiryDate = dateFormat.format(ExpiryDate);
    }
    
    String lineImage = request.getContextPath()+"/coeusliteimages/line4.gif";
    String status=headerBean.getProtocolStatusDescription();
    // Case # 3799: Provide the protocol # in the heading of the New Renewal - Start
    // if(status!=null && (!status.equals(EMPTYSTRING))){
    //    status=status.length()>60?status.substring(0,61)+"...":status;
    // }else{status=EMPTYSTRING;}
    
    if("NA".equals(strPage)){
        // If a New Amendment Window is opened for the Protocol
        status = newAmendment;
    } else if("NR".equals(strPage)){
        // If a New Renewal Window is opened for the Protocol
        status = newRenewal;
    }else if("RA".equals(strPage)){
        // If a New Renewal Window is opened for the Protocol
        status = newRenewalAmendment;
    }
    /*New Condition Added For New Iacuc Amendment/Renewal Type - Start*/
     else if("CR".equals(strPage)){
        // If a New Continuation Review Window is opened for the Protocol
        status = newContinuationReview;
    } else if("CA".equals(strPage)){
        // If a New Continuation Review with Amendment Window is opened for the Protocol
        status = newContinuationReviewAmend;
    }
    /*New Condition Added For New Iacuc Amendment/Renewal Type - End*/
     else if(status!=null && (!status.equals(EMPTYSTRING))){
        status=status.length()>60?status.substring(0,61)+"...":status;
    }else{
        status=EMPTYSTRING;
    }
    // Case # 3799: Provide the protocol # in the heading of the New Renewal - End
    //Added for CoeusLite4.3 header changes enhacement - Start
    Date LastApprovalDate = headerBean.getLastApprovalDate();
    String strLastApprovalDate =EMPTYSTRING;
    if(LastApprovalDate!=null){
        SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
        strLastApprovalDate = dateFormat.format(LastApprovalDate);
    }
    //Added for CoeusLite4.3 header changes enhacement - end

%>
	<table width='100%' height='100%' border='0' cellpadding='0'
		cellspacing='0' class='table'>
		<tr>
			<td align="left" nowrap class='copybold'>&nbsp;&nbsp;&nbsp;<bean:message
					key="protocolHeader.ProtocolNum" />:
			</td>
			<td class='copy' nowrap><%=headerBean.getProtocolNumber()%> <b>(<%=status%>)
			</b></td>
			<td nowrap class='copybold'>&nbsp;&nbsp;&nbsp;<bean:message
					key="protocolHeader.ExpirationDate" />:
			</td>
			<%if(!strExpiryDate.equals(EMPTYSTRING)){%>
			<td class='copy' nowrap>&nbsp;&nbsp;&nbsp;<%=strExpiryDate%>
			</td>
			<%}else{%>
			<td class='copy' nowrap></td>
			<%}%>
		</tr>
		<tr>
			<%if(headerBean.getPrincipalInvestigator()!=null && !headerBean.getPrincipalInvestigator().equals(EMPTYSTRING)){%>
			<td align="left" class='copybold' nowrap>&nbsp;&nbsp;&nbsp;<bean:message
					key="protocolHeader.Investigator" />:
			</td>
			<td class='copy' nowrap><%=headerBean.getPrincipalInvestigator()%></td>
			<%}else{%>
			<td align="left" class='copybold' nowrap>&nbsp;&nbsp;&nbsp;<bean:message
					key="protocolHeader.Investigator" />:
			</td>
			<td class='copy' nowrap></td>
			<%}%>
			<td align="left" class='copybold' nowrap>&nbsp;&nbsp;&nbsp;<bean:message
					key="protocolHeader.LastAppDate" />:
			</td>
			<%if(!strLastApprovalDate.equals(EMPTYSTRING)){%>
			<td class='copy' nowrap>&nbsp;&nbsp;&nbsp;<%=strLastApprovalDate%>
			</td>
			<%}else{%>
			<td class='copy' nowrap></td>
			<%}%>
		</tr>
		<tr>
			<td class='copybold' nowrap>&nbsp;&nbsp;&nbsp;<bean:message
					key="irbGeneralInfoForm.title" />:
			</td>
			<td colspan="3" class='copy' nowrap><%=title%></td>
		</tr>
	</table>
	<%}%>
</body>
<%}catch(Exception e){e.printStackTrace();}%>
</html>