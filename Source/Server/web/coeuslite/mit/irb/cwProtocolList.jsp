
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%@ page contentType="text/html;charset=UTF-8" language="java"%>

<%--start 1--%>
<%@page
	import="java.util.HashMap, 
                java.util.ArrayList, 
                java.util.Hashtable, 
                java.util.Iterator, 
                edu.mit.coeuslite.utils.CoeusLiteConstants"%>
<%--end 1--%>

<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ include file="/coeuslite/mit/utils/CoeusContextPath.jsp"%>
<jsp:useBean id="protocolList" scope="session" class="java.util.Vector" />
<jsp:useBean id="protocolColumnNames" scope="session"
	class="java.util.Vector" />
<bean:size id="protocolListSize" name="protocolList" />
<%--start 2--%>
<%
    
    String protocolName = (String)session.getAttribute(CoeusLiteConstants.PROTOCOL_NAME);
    String protoNum = session.getAttribute(CoeusLiteConstants.PROTOCOL_NUMBER+session.getId()) == null ? "" : session.getAttribute(CoeusLiteConstants.PROTOCOL_NUMBER+session.getId()).toString().trim();
    
     //Added for Case# 3018 -create ability to delete pending studies  -  Start
    String invalidProtocol =  "";
    if ( request.getAttribute("invalidProtocol") != null ) { 
        invalidProtocol = (String) request.getAttribute("invalidProtocol");
        if( invalidProtocol.equals("YES") ){
            invalidProtocol = "Invalid Protocol";
        }
    }
//Added for Case# 3018 -create ability to delete pending studies  -  End
%>
<%--end 2--%>

<html:html>
<head>
<title>Coeus Web</title>
<html:base />
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
<link
	href="<bean:write name='ctxtPath'/>/coeuslite/mit/utils/css/coeus_styles.css"
	rel="stylesheet" type="text/css">
<link href="/coeuslite/mit/utils/css/ipf.css" rel="stylesheet"
	type="text/css">
<script language="javascript" type="text/JavaScript"
	src="<bean:write name='ctxtPath'/>/coeuslite/mit/utils/scripts/sorttable.js"></script>
<script>
            function getSubmissionDetail(protocolNumber, sequenceNumber) {               
                var winleft = (screen.width - 650) / 2;
                var winUp = (screen.height - 450) / 2;  
                var win = "scrollbars=1,resizable=1,width=768,height=400,left="+winleft+",top="+winUp                        
                sList = window.open("<%=request.getContextPath()%>/getSubmissionDetails.do?&protocolNumber="+protocolNumber+"&sequenceNumber="+sequenceNumber, "list", win);    
                //window.open("<%=request.getContextPath()%>/getProtocolAction.do?&protocolNumber="+protocolNumber+"&acType=V&sequenceNumber="+sequenceNumber+"&actionId="+actionId+"&protoCorrespTypeCode="+protoCorrespTypeCode); 
            }     
    </script>
</head>
<body>
	<html:messages id="message" message="true">
		<script>
var msg = '<bean:write name = "message" />';

<%--start 3--%>
displayMessage = "<%=session.getAttribute(CoeusLiteConstants.MODE_DETAILS+session.getId())%>";
displayMode = "<%=CoeusLiteConstants.DISPLAY_MODE%>"
<%--end 3--%>

if(displayMessage == displayMode ) {
canEdit = confirm(msg+". "+"Do you want to open it in Display mode?");
    if(canEdit) {
        window.location= "<%=request.getContextPath()%>/getProtocolData.do?protocolNumber=<%=protoNum%>&PAGE=G&isEditable="+canEdit+"&notFromIsReviewer=true";
    }
}else {
    alert(msg);
}
</script>
		<script>
        //Added for Case# 3018 -create ability to delete pending studies  -  Start
        var noRecord = '<%=invalidProtocol%>';
        if(noRecord.length > 0
            && noRecord != 'null'){
            alert(noRecord);
        }
        //Added for Case# 3018 -create ability to delete pending studies  -  End
</script>

	</html:messages>
	<html:form action="/protocolList.do" method="post">

		<!-- New Template for cwProtocolList - Start   -->


		<table width="100%" height="100%" border="0" cellpadding="0"
			cellspacing="0" class="table">


			<!-- EntityDetails - Start  -->
			<tr>
				<td height="653" align="left" valign="top"><table width="99%"
						border="0" align="center" cellpadding="0" cellspacing="0"
						class="tabtable">
						<tr>
							<td colspan="4" align="left" valign="top"><table
									width="100%" height="20" border="0" cellpadding="0"
									cellspacing="0" class="tableheader">
									<tr>
										<td>List of <%=session.getAttribute("protocolName")%>
										</td>
									</tr>
								</table></td>
						</tr>
						<tr>
							<td>&nbsp;</td>
						</tr>
						<tr align="center">
							<td>
								<table width="98%" height="100%" border="0" cellpadding="2"
									cellspacing="0" id="t1" class="sortable">
									<tr>

										<%
                                     if(protocolColumnNames != null && protocolColumnNames.size()>0){
                                         for(int index=0;index<protocolColumnNames.size();index++){
                                             edu.mit.coeus.search.bean.DisplayBean displayBean = (edu.mit.coeus.search.bean.DisplayBean)protocolColumnNames.elementAt(index);
                                             if(displayBean.isVisible()){
                                                 String strColumnName = displayBean.getValue();
                                                 %>
										<td class="theader"><%=strColumnName%></td>
										<!--<td class="tableheadersub3">&nbsp;&nbsp;</td>-->
										<%
                                             }
                                        }
                                     }
                                 %>

									</tr>
									<%  
                                        String strBgColor = "#DCE5F1";
                                        String protocolNumber="";
                                        int count = 0;
                                 %>
									<logic:present name="protocolList">
										<logic:iterate id="protocol" name="protocolList"
											type="java.util.HashMap">
											<%
                                        if (count%2 == 0) 
                                            strBgColor = "#D6DCE5"; 
                                        else 
                                            strBgColor="#DCE5F1"; 
                                 %>
											<tr bgcolor="<%=strBgColor%>" valign="top"
												onmouseover="className='TableItemOn'"
												onmouseout="className='TableItemOff'">
												<%
                                //start--5
                                String statusFlag  = "NO";   
                                String destPage = CoeusLiteConstants.GENERAL_INFO_PAGE;
                                //end--5
                                     if(protocolColumnNames != null && protocolColumnNames.size()>0){
                                         String sequenceNumber = protocol.get("SEQUENCE_NUMBER").toString().trim();
                                         for(int index=0;index<protocolColumnNames.size();index++){
                                            edu.mit.coeus.search.bean.DisplayBean displayBean = (edu.mit.coeus.search.bean.DisplayBean)protocolColumnNames.elementAt(index);
                                            if(!displayBean.isVisible())
                                                continue;
                                            String key = displayBean.getName();
                                            if(key != null){
                                                String value = protocol.get(key) == null ? "" : protocol.get(key).toString();
                                                   if(value !=null && key.equals("PROTOCOL_STATUS_DESCRIPTION") && value.equals("Submitted to IRB") ){
                                                    statusFlag = "YES";
                                                    %>
												<% } if(index == 0){
                                                    protocolNumber=value;
                                                //session.setAttribute("protocolNumber",value);
                                %>
												<%--td align="left" nowrap class="copy"><a href="<%=request.getContextPath()%>/generalInfo.do?SEARCH_ACTION=SEARCH_ACTION&protocolNumber=<%=value%>"><%=value%></a></td--%>

												<%
                                    } else if(index==1){
                                        if(value != null && value.length() > 60){ 
                                            value = value.substring(0,55);
                                            value = value+" ...";
                                        }
                                     }
                                                
                                        String submissionDet = "javaScript:getSubmissionDetail('"+protocolNumber+"','"+sequenceNumber+"');";         
                                    %>

												<%--JIRA COEUSQA-2437 -START--%>
												<td align="left" nowrap class="copy">
													<%if(statusFlag.equalsIgnoreCase("YES") && value.equals("Submitted to IRB") ){ %>
													<a
													href="<%=request.getContextPath()%>/getProtocolData.do?SEARCH_ACTION=SEARCH_ACTION&protocolNumber=<%=protocolNumber.trim()%>&PAGE=<%=destPage.trim()%>&sequenceNumber=<%=sequenceNumber%>&notFromIsReviewer=true"><u>
															<%=value%>
													</u> </a> &nbsp;&nbsp; <html:link href="<%=submissionDet%>">
														<img
															src="<bean:write name='ctxtPath'/>/coeusliteimages/protsubdetails.gif"
															width="16" border="0" vspace="0" height="15">
													</html:link> <% }else{ %> <a
													href="<%=request.getContextPath()%>/getProtocolData.do?SEARCH_ACTION=SEARCH_ACTION&protocolNumber=<%=protocolNumber.trim()%>&PAGE=<%=destPage.trim()%>&sequenceNumber=<%=sequenceNumber%>&notFromIsReviewer=true"><u>
															<%=value%>
													</u> </a> <%}%> <%--JIRA COEUSQA-2437 -END--%>
												</td>
												<%
                                            }
                                        }
                                     }
                                 %>
											</tr>
											<% count++;%>
										</logic:iterate>
									</logic:present>
									<logic:equal name="protocolListSize" value="0">
										<tr>
											<td height="23" align=center>
												<div>
													<bean:message bundle="proposal"
														key="generalInfoProposal.noResults" />
												</div>
											</td>
										</tr>
									</logic:equal>
								</table>
							</td>
						</tr>

						<tr>
							<td>&nbsp;</td>
						</tr>

					</table></td>
			</tr>
			<tr>
				<td height='10'>&nbsp;</td>
			</tr>

		</table>
		<!-- New Template for cwProtocolList - End  -->




	</html:form>
</body>
</html:html>
