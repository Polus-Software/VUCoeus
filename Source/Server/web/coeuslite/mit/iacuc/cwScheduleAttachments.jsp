
<%@ page contentType="text/html;charset=UTF-8" language="java"%>

<%@page
	import="java.util.HashMap, 
java.util.ArrayList, 
java.util.Hashtable, 
java.util.Iterator, 
java.util.Vector, 
java.util.HashMap, 
edu.mit.coeuslite.utils.CoeusLiteConstants"%>
<%@ page import="edu.mit.coeus.utils.DateUtils"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ include file="/coeuslite/mit/utils/CoeusContextPath.jsp"%>
<jsp:useBean id="iacucList" scope="session" class="java.util.Vector" />
<jsp:useBean id="iacucColumnNames" scope="session"
	class="java.util.Vector" />
<jsp:useBean id="loggedinUser" scope="session" class="java.lang.String" />

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
"http://www.w3.org/TR/html4/loose.dtd">
<%
    HashMap  hmSubmittedProtocols = (HashMap)  request.getAttribute("schSubmittedProtocols");    
    Vector protoList = (Vector) session.getAttribute("iacucList");
    boolean hasErrorMessage = false;
%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link
	href="<%=request.getContextPath()%>/coeuslite/mit/utils/css/coeus_styles.css"
	rel="stylesheet" type="text/css">
<title><bean:message bundle="iacuc"
		key="scheduleAttachmentList.title" /></title>
<script>
            function showHide(val,value){
            var panel = 'Panel'+value;
            var pan = 'pan'+value;
            var hidePanel  = 'hidePanel'+value;
            if(val == 1){
            document.getElementById(panel).style.display = "none";
            document.getElementById(hidePanel).style.display = "block";
            document.getElementById(pan).style.display = "block";
            }
            else if(val == 2){
            document.getElementById(panel).style.display = "block";
            document.getElementById(hidePanel).style.display = "none";
            document.getElementById(pan).style.display = "none";
            }        
           }
                     
           function view_attachments(routingNumber, mapNumber, levelNumber, stopNumber, approverNumber,index){
              window.open("<%=request.getContextPath()%>/routingAttachments.do?&routingNumber="
                +routingNumber+"&mapNumber="+mapNumber+"&levelNumber="+levelNumber+"&stopNumber="+stopNumber+"&approverNumber="+approverNumber+"&index="+index);
           }  
       
        </script>
</head>
<body>

	<table width="100%" class='tabtable'>
		<tr class="theader">
			<td align="left" height="26"><bean:message bundle="iacuc"
					key="scheduleAttachmentList.title" /></td>
			<td align="right" height="26" valign="middle"><a
				href="javaScript:window.close();"><u>Close</u></a></td>
		</tr>
	</table>

	<table>
		<tr>
			<td></td>
		</tr>
	</table>


	<table border="0" width="99%" cellspacing="0" cellpadding="0"
		align=center class="copy">
		<tr>
			<td width="99%" height="100%">

				<table border="0" width="99%" cellspacing="0" cellpadding="0"
					align=center class="tabHeader">
					<%if(protoList == null || protoList.isEmpty()){%>
					<tr>
						<td class="copy" align="left">&nbsp;&nbsp;&nbsp;<bean:message
								bundle="iacuc"
								key="scheduleAttachList.noScheduleAttachmentsAvailable" />
						</td>
					</tr>
					<%} else {%>
					<tr align="left">
						<td>
							<table width="100%" height="100%" border="0" cellpadding="2"
								cellspacing="0" id="t1">
								<tr>
									<%
                                            if(iacucColumnNames != null && iacucColumnNames.size()>0){
                                                for(int index=0;index<iacucColumnNames.size();index++){
                                                    edu.mit.coeus.search.bean.DisplayBean displayBean = (edu.mit.coeus.search.bean.DisplayBean)iacucColumnNames.elementAt(index);
                                                    if(displayBean.isVisible()){
                                                        String strColumnName = displayBean.getValue();
                                            %>
									<td class="theader"><%=strColumnName%></td>

									<%
                                            }
                                            }%>
									<%}
                                            %>

								</tr>
								<%  
                                        String strBgColor = "#DCE5F1"; 
                                        int count = 0;
                                        int width = 0;
                                        %>
								<logic:present name="iacucList">
									<logic:iterate id="protocol" name="iacucList"
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
                                                    String destPage = CoeusLiteConstants.REVIEW_COMMENTS;                                   
                                                    String sequenceNumber = "";
                                                    String submissionNumber = "";
                                                    String scheduleId ="";
                                                    String attachmentId ="";
                                                    if(iacucColumnNames != null && iacucColumnNames.size()>0){

                                                        for(int index=0;index<iacucColumnNames.size();index++){
                                                            width = 0;
                                                            edu.mit.coeus.search.bean.DisplayBean displayBean = (edu.mit.coeus.search.bean.DisplayBean)iacucColumnNames.elementAt(index);
                                                           String key = displayBean.getName();
                                                            if(!displayBean.isVisible()){
                                                                if(index == 0){
                                                                    scheduleId = protocol.get(key) == null ? "" : protocol.get(key).toString();
                                                                }
                                                                if(index == 1){
                                                                    attachmentId = protocol.get(key) == null ? "" : protocol.get(key).toString();
                                                                }
                                                                continue;
                                                            }
                                                            if(index == 2){
                                                               width = 20;
                                                            }
                                                            if(index == 4){
                                                               width = 40;
                                                            }
                                                            if(key != null){
                                                                String value = protocol.get(key) == null ? "" : protocol.get(key).toString();                                                               
                                                    %>
											<%if(index==2){
                                                                    if(value != null && value.length() > 50){
                                                                        value = value.substring(0,47);
                                                                        value = value+" ...";
                                                                    }
                                                    }
                                                    %>
											<td align="left" width="<%=width%>%" nowrap class="copy">
												<%if(index != 4) {%> <%=value%> <%} else {%> <a
												href="<%=request.getContextPath()%>/protocolList.do?PROTOCOL_TYPE=VIEW_SCHEDULE_ATTACH&scheduleId=<%=scheduleId%>&attachmentId=<%=attachmentId%>&isAttach=true&openAttach=true"
												, target="_blank"><u> <%=value%>
												</u> </a> <%}%>
											</td>
											<%
                                                    }
                                                    }}
                                                    %>
										</tr>


										<% count++;%>
									</logic:iterate>
								</logic:present>

							</table>
						</td>
					</tr>
					<%}%>
					<tr>
						<td class="copybold">&nbsp;</td>
					</tr>
				</table>
			</td>
		</tr>
	</table>

</body>
</html>
