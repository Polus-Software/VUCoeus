<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/coeus-utils.tld" prefix="coeusUtils"%>
<%@ include file="/coeuslite/mit/utils/CoeusContextPath.jsp"%>
<%@page
	import="java.sql.Date, edu.mit.coeuslite.iacuc.bean.ProtocolHeaderDetailsBean, java.text.SimpleDateFormat"%>

<html>
<title>IRBSummary</title>
<script language="javascript" type="text/JavaScript"
	src="<%=request.getContextPath()%>/coeuslite/mit/utils/scripts/divSlide.js"></script>
<script language="javascript" type="text/JavaScript"
	src="..\scripts\_js.js"></script>
<script language="javascript" type="text/JavaScript"
	src="..\scripts\CheckForward.js"></script>



<%String plus = request.getContextPath()+"/coeusliteimages/plus.gif";
  String minus = request.getContextPath()+"/coeusliteimages/minus.gif";%>

<script>
        function summaryHeader1(value)
        {
             if(value==53) {
           document.getElementById('sumhideimage').style.display = 'block';
            document.getElementById('sumHide').style.display = 'block';
            document.getElementById('summaryheader').style.display = 'block';
             document.getElementById('sumShow').style.display = 'none';
            document.getElementById('sumshowimage').style.display = 'none';
        }
           else if(value==54) {

            document.getElementById('sumhideimage').style.display = 'none';
            document.getElementById('sumHide').style.display = 'none';
            document.getElementById('summaryheader').style.display = 'none';
             document.getElementById('sumShow').style.display = 'block';
            document.getElementById('sumshowimage').style.display = 'block';
    }
    }

    </script>



<head>
<title>Protocol Details</title>
</head>
<%try{%>
<body>
	<%
    ProtocolHeaderDetailsBean headerBean=(ProtocolHeaderDetailsBean)session.getAttribute("iacucHeaderBean");
     String lineImage = request.getContextPath()+"/coeusliteimages/line4.gif";
    if(headerBean!=null){
     String EMPTYSTRING ="";
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
    
    //Added for CoeusLite4.3 header changes enhacement - Start
    Date LastApprovalDate = headerBean.getLastApprovalDate();
    String strLastApprovalDate =EMPTYSTRING;
    if(LastApprovalDate!=null){
        SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
        strLastApprovalDate = dateFormat.format(LastApprovalDate);
    }
    //Added for CoeusLite4.3 header changes enhacement - end
    
    String status=headerBean.getProtocolStatusDescription();
    if(status!=null && (!status.equals(EMPTYSTRING))){
         status=(status.length()>60)?status.substring(0,61)+"...":status;
         }else{status=EMPTYSTRING;}
        //added for user Name id
        String updateUserName = headerBean.getUpdateUserName();
        String createUserName = headerBean.getCreateUserName();
        
    //Added for case#2490 - Coeus Lite - Protocol Submission - Meeting Date - start
    Date scheduledDate = headerBean.getScheduledDate();
    String strScheduledDate = EMPTYSTRING;
    if(scheduledDate != null){
        SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
        strScheduledDate = dateFormat.format(scheduledDate);
    }
    //Added for case#2490 - Coeus Lite - Protocol Submission - Meeting Date - end         
        
%>

	<tr>
		<td height="2"></td>
	</tr>
	<tr>
		<td align="center">
			<table width="98%" border="0" cellpadding="0" cellspacing="0"
				class="tableheader">
				<%--   <td align="center"><img src="images/11.png" width="870" height="73" /></td>--%>

				<%-- commented for COEUSQA-3483 : Changes to Protocol Summary screen in Routing in Lite - start--%>
				<%--<tr> 
 <td width='50%' align=left nowrap>
                     <div id='sumshowimage' style='display: none;'>
         <html:link href="javaScript:summaryHeader1('53');">
 <html:img src="<%=plus%>" border="0"/></html:link>&nbsp;
            <bean:message bundle="protocol" key="irbInvKeyPersons.LstOfSummaryDetails"/>
                   </div>
                   <div id='sumhideimage'>
                            <html:link href="javaScript:summaryHeader1('54');">
                            <html:img src="<%=minus%>" border="0"/></html:link>&nbsp; <bean:message bundle="protocol" key="irbInvKeyPersons.LstOfSummaryDetails"/>
                       </div>
 </td>   
                      <td width='50%' align=right>
                        <html:link href="javaScript:summaryHeader1('53');">
                        <div id='sumShow' style='display: none;'>
                            <bean:message bundle="proposal" key="summary.show"/>
                        </div>
                        </html:link>
                        <html:link href="javaScript:summaryHeader1('54');">
                        <div id='sumHide'>
                            <bean:message bundle="proposal" key="summary.hide"/>
                        </div>
                        </html:link></td>
 </tr>--%>
				<%-- commented for COEUSQA-3483 : Changes to Protocol Summary screen in Routing in Lite - end--%>

			</table> <%--</td>
     </tr>

               <tr>
     <td >--%>
			<table width='98%' cellpadding='0' cellspacing='0' class='tabtable'
				align=center style="border: 0">
				<tr>
					<td height="2"></td>
				</tr>

			</table>
			<div id="summaryheader">


				<table width='98%' cellpadding='0' cellspacing='0' class='tabtable'
					align=center border="0">

					<tr>
						<td nowrap class='copybold'>&nbsp;&nbsp;&nbsp;<bean:message
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
						<td class='copybold' nowrap>&nbsp;&nbsp;&nbsp;<bean:message
								key="protocolHeader.Investigator" />:
						</td>
						<td class='copy' nowrap><%=headerBean.getPrincipalInvestigator()%>
						</td>
						<%}else{%>
						<td class='copybold' nowrap>&nbsp;&nbsp;&nbsp;<bean:message
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
						<td class='copy' nowrap><%=title%></td>
						<%-- Added for case#2490 - Coeus Lite - Protocol Submission - Meeting Date - start --%>
						<td align="left" class='copybold' nowrap>&nbsp;&nbsp;&nbsp;<bean:message
								key="protocolHeader.MeetingDate" />:
						</td>
						<%if(!strScheduledDate.equals(EMPTYSTRING)){%>
						<td class='copy' nowrap>&nbsp;&nbsp;&nbsp;<%=strScheduledDate%>
						</td>
						<%}else{%>
						<td class='copy' nowrap></td>
						<%}%>
						<%-- Added for case#2490 - Coeus Lite - Protocol Submission - Meeting Date - end --%>
					</tr>
					<%--  <tr><td colspan='4'><html:img src="<%=lineImage%>" width="100%" height="2" border="0"/></td></tr>--%>
					<%--
               <%if( headerBean.getUnitNumber()!=null && ! headerBean.getUnitNumber().equals(EMPTYSTRING)){%>
               <tr><td  class='copybold' nowrap>&nbsp;&nbsp;&nbsp;<bean:message key="protocolHeader.LeadUnit"/>:</td><td  colspan='7' class='copy'><%= headerBean.getUnitNumber()%>: <%=leadUnit%></td></tr>
               <%}else{%>
               <tr><td  class='copybold' nowrap>&nbsp;&nbsp;&nbsp;<bean:message key="protocolHeader.LeadUnit"/>:</td><td  colspan='7' class='copy'> </td></tr>
               <%}%>
               <tr>
               --%>
					<%--   <%if((headerBean.getUpdateTimeStamp()!=null && !headerBean.getUpdateTimeStamp().equals(EMPTYSTRING))){%>
                               <% if(updateUserName == null || updateUserName.equals("")){
                                        updateUserName = updateUser;}else{updateUserName = updateUserName;}%>
                    <td class='copybold' nowrap>&nbsp;&nbsp;&nbsp;<bean:message key="protocolHeader.LastUpdated"/>:</td><td class='copy' colspan='3' nowrap><%=updateTimestamp%> <b>by</b> <%=updateUserName%>  </td>
               <%}else{%>
                    <td class='copybold' nowrap>&nbsp;&nbsp;&nbsp;<bean:message key="protocolHeader.LastUpdated"/>:</td><td class='copy' colspan='3' nowrap> <%=updateUserName%> </td>
               <%}%>--%>
				</table>

			</div>
		</td>
	</tr>

	<%}%>
</body>

<%}catch(Exception e){e.printStackTrace();}%>
</html>