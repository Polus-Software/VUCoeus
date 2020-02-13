
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%@ page contentType="text/html;charset=UTF-8" language="java"%>

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
    
            
    </script>
</head>
<body>

	<html:form action="/protocolList.do" method="post">

		<table width="800" height="100%" border="0" cellpadding="0"
			cellspacing="0" class="table">
			<tr>
				<td height="100%" align="left" valign="top"><table width="99%"
						border="0" align="center" cellpadding="0" cellspacing="0"
						class="tabtable">
						<tr>
							<td colspan="4" align="left" valign="top"><table
									width="100%" height="20" border="0" cellpadding="0"
									cellspacing="0" class="tableheader">
									<tr>
										<td><bean:message key="scheduleProtocols.title" /></td>
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
                                
                                String destPage = CoeusLiteConstants.REVIEW_COMMENTS;
                                
                                     String sequenceNumber = "";
                                     String submissionNumber = "";
                                     String scheduleId ="";
                                     if(protocolColumnNames != null && protocolColumnNames.size()>0){
                                      
                                         sequenceNumber = protocol.get("SEQUENCE_NUMBER").toString().trim();
                                         submissionNumber = protocol.get("SUBMISSION_NUMBER").toString().trim();
             
                                         for(int index=0;index<protocolColumnNames.size();index++){
                                            edu.mit.coeus.search.bean.DisplayBean displayBean = (edu.mit.coeus.search.bean.DisplayBean)protocolColumnNames.elementAt(index);
                                            if(!displayBean.isVisible())
                                                continue;
                                            String key = displayBean.getName();
                                            if(key != null){
                                                String value = protocol.get(key) == null ? "" : protocol.get(key).toString();
                                                   if(value !=null && key.equals("PROTOCOL_STATUS_DESCRIPTION") && value.equals("Submitted to IRB") ){
                                                    
                                                    %>
												<% } if(index == 0){
                                                    protocolNumber=value;
                                                    
                                              
                                %>


												<%
                                    } else if(index==1){
                                        if(value != null && value.length() > 50){ 
                                            value = value.substring(0,45);
                                            value = value+" ...";
                                        }
                                     } else if(index==2){
                                        if(value != null && value.length() > 28){ 
                                            value = value.substring(0,25);
                                            value = value+" ...";
                                        }
                                     } else if(index==3){
                                        if(value != null && value.length() > 30){ 
                                            value = value.substring(0,28);
                                            value = value+" ...";
                                        }
                                     }        
                                      
                                    %>

												<td align="left" nowrap class="copy"><a
													href="<%=request.getContextPath()%>/getProtocolData.do?SEARCH_ACTION=SEARCH_ACTION&protocolNumber=<%=protocolNumber.trim()%>&PAGE=<%=destPage.trim()%>&sequenceNumber=<%=sequenceNumber%>&submissionNumber=<%=submissionNumber%>&fromIsReviewer=true&isAgenda=true&openAgenda=false"><u>
															<%=value%>
													</u> </a></td>
												<%
                                            }
                                        }
                                     }
                                 %>
											</tr>
											<% count++;%>
										</logic:iterate>
									</logic:present>

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

	</html:form>
</body>
</html:html>
