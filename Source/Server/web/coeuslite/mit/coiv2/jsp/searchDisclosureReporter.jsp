<%--
    Document   : searchDisclosure
    Created on : May 17, 2010, 6:09:46 PM
    Author     : Sony
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
    "http://www.w3.org/TR/html4/loose.dtd">
<%@ page
	import="edu.mit.coeus.bean.PersonInfoBean,java.util.Vector,edu.mit.coeuslite.coiv2.utilities.CoiConstants,edu.mit.coeuslite.coiv2.beans.CoiDisclosureBean;"%>
<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>C O I</title>
<%String path = request.getContextPath();%>
<script src="<%=path%>/coeuslite/dartmouth/utils/scripts/divSlide.js"></script>
<script src="<%=path%>/coeuslite/dartmouth/utils/scripts/Balloon.js"></script>
<link href="<%=path%>/coeuslite/dartmouth/utils/css/coeus_styles.css"
	rel="stylesheet" type="text/css" />
<link href="<%=request.getContextPath()%>/coiv2/css/layout.css"
	rel="stylesheet" type="text/css" />
<link href="<%=request.getContextPath()%>/coiv2/css/styles.css"
	rel="stylesheet" type="text/css" />
<link href="<%=request.getContextPath()%>/coiv2/css/collapsemenu.css"
	rel="stylesheet" type="text/css" />
<script type="text/javascript">
     function search(){
               if(document.forms[0].subDate.value != ""){
                     if(document.forms[0].subEndDate.value != ""){
                          document.forms[0].action = "<%=path%>/searchDisclByElementsReporter.do";
                          document.forms[0].submit();
                     }else{
                       alert("Please enter End Date");
                     }
                }else{
                    if(document.forms[0].subEndDate.value == ""){
                        document.forms[0].action = "<%=path%>/searchDisclByElementsReporter.do";
                        document.forms[0].submit();
                    }else{
                       alert("Please enter Start Date");
                    }
                }
            }
        </script>
<script language="javascript" type="text/JavaScript"
	src="<%=request.getContextPath()%>/coeuslite/mit/utils/scripts/coeusLiteCalendar.js"></script>
</head>
<html:form action="/searchDisclosure.do">
	<body class="tabtable">
		<logic:notPresent name="searchDetView">
			<logic:notPresent name="message">
				<li><bean:message bundle="proposal" key="search.header1" /> <bean:message
						bundle="proposal" key="search.header2" /> <b><bean:message
							bundle="proposal" key="search.header3" /> </b> or <b><bean:message
							bundle="proposal" key="search.header5" /></b> or <b><bean:message
							bundle="proposal" key="search.header6" /></b> <bean:message
						bundle="proposal" key="search.header4" /></li>
				<table id="bodyTable" style="width: 960px; height: 100%"
					align="center">
					<tr>
						<td colspan="4">&nbsp;</td>
					</tr>
					<tr>
						<td width="15%" align="right">&nbsp;&nbsp;&nbsp;&nbsp;<b>Financial
								Entity:</b></td>
						<td width="25%"><html:select property="entityName"
								style="height: 20px;width: 200px;">
								<html:option value="select">Select</html:option>
								<logic:notEmpty name="entityNameList">
									<html:options collection="entityNameList"
										labelProperty="entityName" property="entityName" />
								</logic:notEmpty>
							</html:select></td>
						<td width="20%" align="right">&nbsp;&nbsp;&nbsp;&nbsp;<b>Disclosure
								Type:</b>
						</td>
						<td width="40%"><html:select property="disclosureEvent"
								style="height: 20px;width: 200px;">
								<html:option value="select">Select</html:option>
								<logic:notEmpty name="disclosureEvent">
									<html:options collection="disclosureEvent"
										labelProperty="description" property="code" />
								</logic:notEmpty>
							</html:select></td>
					</tr>
					<tr>
						<td align="right">&nbsp;&nbsp;&nbsp;&nbsp;<b>Disposition
								Status: </b>
						</td>
						<td><html:select property="dispositionStatus"
								style="height: 20px;width: 200px;">
								<html:option value="select">Select</html:option>
								<logic:notEmpty name="DisposStatusListView">
									<html:options collection="DisposStatusListView"
										labelProperty="dispositionStatus"
										property="disclosureDispositionCode" />
								</logic:notEmpty>
							</html:select></td>
						<td align="right">&nbsp;&nbsp;&nbsp;&nbsp;<b>Review
								Status:</b>
						</td>
						<td><html:select property="disclosureStatus"
								style="height: 20px;width: 200px;">
								<html:option value="select">Select</html:option>
								<logic:notEmpty name="DisclStatusListView">
									<html:options collection="DisclStatusListView"
										labelProperty="disclosureStatus"
										property="disclosureStatusCode" />
								</logic:notEmpty>
							</html:select></td>
					</tr>
					<tr>
						<td align="right">&nbsp;&nbsp;&nbsp;<b>Submission Start
								Date</b>
						</td>
						<td><html:text property="subDate" styleClass="copy"
								maxlength="10" style="width:175px;height: 14px;" /> <a
							id="hlIRBDate"
							onclick="displayCalendarWithTopLeft('subDate',8,25)"
							tabindex="32" href="javascript:void(0);"
							style="vertical-align: bottom" runat="server"><img
								id="imgIRBDate" title="" height="16" alt=""
								src="<%=request.getContextPath()%>/coeusliteimages/cal.gif"
								width="16" border="0" runat="server"></img> </a></td>
						<td align="right">&nbsp;&nbsp;&nbsp;<b>Submission End
								Date</b>
						</td>
						<td><html:text property="subEndDate" styleClass="copy"
								maxlength="10" style="width:175px;height: 14px;" /> <a
							id="hlIRBDate"
							onclick="displayCalendarWithTopLeft('subEndDate',8,25)"
							tabindex="32" href="javascript:void(0);"
							style="vertical-align: bottom" runat="server"><img
								id="imgIRBDate" title="" height="16" alt=""
								src="<%=request.getContextPath()%>/coeusliteimages/cal.gif"
								width="16" border="0" runat="server"></img> </a></td>
					</tr>
					<tr>
						<logic:equal value="false" name="dateMessage">
							<td colspan="2">
								&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<font
								color="red">Please enter a valid start date
									format(MM/DD/YYYY)</font>
							</td>
						</logic:equal>
						<logic:notPresent name="dateMessage">
							<td colspan="2">&nbsp;</td>
						</logic:notPresent>
						<logic:equal value="false" name="dateEndMessage">
							<td colspan="2">
								&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
								<font color="red">Please enter a valid end date
									format(MM/DD/YYYY)</font>
							</td>
						</logic:equal>
						<logic:notPresent name="dateEndMessage">
							<td colspan="2">&nbsp;</td>
						</logic:notPresent>
					</tr>
					<tr>
						<td colspan="4">&nbsp;</td>
					</tr>
					<tr>
						<td>&nbsp;</td>
						<td align="right"><html:button styleClass="clsavebutton"
								onclick="javaScript:search();" property="button" value="Search"></html:button>
						</td>
						<td>&nbsp;&nbsp;&nbsp;&nbsp;<html:button
								styleClass="clsavebutton" property="button" value="Cancel"
								onclick="window.close()"></html:button>
						</td>
						<td>&nbsp;</td>
					</tr>
					<logic:notPresent name="message">
						<logic:equal value="false" name="noselectionmessage">
							<tr>
								<td colspan="2">
									<li><font color="red">Please enter selection
											criteria</font></li>
								</td>
							</tr>
						</logic:equal>
					</logic:notPresent>
					<%--<tr><td style="height: 250px;" colspan="2">&nbsp;</td></tr>--%>
				</table>
			</logic:notPresent>
		</logic:notPresent>
		<logic:equal value="true" name="noselectionmessage">
			<table class="tabtable" border="0">
				<tr valign="center">
					<td class="tabheader"><font size="2" color="#ffffff"> <b>
								<font face="Verdana,Arial,Helvtica,sans-serif" color="black">
									&nbsp;&nbsp;&nbsp;&nbsp;Disclosure Search List </font>
						</b>
					</font></td>
				</tr>
			</table>
			<table width="100%" border="0" class='tabtable' cellspacing="0"
				cellpadding="0">
				<tr>
					<td align="right" class="theader" height='30'><html:link
							href="javascript:history.go(-1)">
							<bean:message key="search.searchAgain" />
						</html:link></td>
					<td class='theader' height='30'>&nbsp;&nbsp;</td>
					<td align="left" class="theader" height='30'><html:link
							href="javascript://" onclick="window.close()">
							<bean:message key="search.closeWindow" />
						</html:link></td>
				</tr>
			</table>
			<table id="bodyTable" class="sortable"
				style="width: 100%; height: 100%;" border="0" align="center">
				<tr class="theader">
					<td
						style="background-color: #6E97CF; color: #173B63; font-size: 12px; font-weight: bold;">&nbsp;</td>
					<td
						style="background-color: #6E97CF; color: #173B63; font-size: 12px; font-weight: bold;">Disclosure
						Number</td>
					<td
						style="background-color: #6E97CF; color: #173B63; font-size: 12px; font-weight: bold;">Owned
						By</td>
					<td
						style="background-color: #6E97CF; color: #173B63; font-size: 12px; font-weight: bold;">Financial
						Entity</td>
					<td
						style="background-color: #6E97CF; color: #173B63; font-size: 12px; font-weight: bold;">Disclosure
						Event</td>
					<td
						style="background-color: #6E97CF; color: #173B63; font-size: 12px; font-weight: bold; text-align: center">Disclosure
						Status</td>
					<td
						style="background-color: #6E97CF; color: #173B63; font-size: 12px; font-weight: bold;">Disposition
						Status</td>
					<td
						style="background-color: #6E97CF; color: #173B63; font-size: 12px; font-weight: bold;">Last
						Updated</td>
					<td
						style="background-color: #6E97CF; color: #173B63; font-size: 12px; font-weight: bold;">Update
						User</td>
					<td
						style="background-color: #6E97CF; color: #173B63; font-size: 12px; font-weight: bold;">Expiration
						Date</td>
				</tr>
				<logic:present name="message">
					<logic:equal value="false" name="message">
						<tr>
							<td colspan="4"><font color="red">No disclosures
									found</font></td>
						</tr>
					</logic:equal>
				</logic:present>
				<logic:present name="searchDetView">
					<%         int i = 0;
                            int index = 0;
                            String strBgColor = "#DCE5F1";
                            Vector disclVector = (Vector) request.getAttribute("searchDetView");
                %>
					<logic:iterate id="serEntView" name="searchDetView">
						<%
                                if (index % 2 == 0) {
                                    strBgColor = "#D6DCE5";
                                } else {
                                    strBgColor = "#DCE5F1";
                                }
                    %>
						<%CoiDisclosureBean bean = (CoiDisclosureBean) disclVector.get(i);%>
						<tr bgcolor="<%=strBgColor%>" class="TableItemOff"
							onmouseover="className='TableItemOn'"
							onmouseout="className='TableItemOff'" style="height: 22px;">
							<td class="copy">
								<%String link1 = "/getannualdisclosure.do?&param1=" + bean.getCoiDisclosureNumber() + "&param2=" + bean.getSequenceNumber() + "&param3=" + bean.getPersonId() + "&param5=" + bean.getModuleName();%>
								<script>
function childClose(){
var link = '<%=link1%>';
window.opener.location.href = "<%=path%>"+link;
window.close();
}
</script> <html:link
									style="color: #003399;font-family:Arial,Helvetica,sans-serif;font-size:12px;font-weight:bold;float: left;font-size:12px;"
									href="javascript://" onclick="childClose()">View</html:link>
								&nbsp;&nbsp;
							</td>
							<td
								style="color: #173B63; font-size: 12px; font-weight: bold; text-align: center">
								<b><bean:write name="serEntView"
										property="coiDisclosureNumber" /></b>
							</td>
							<td style="color: #173B63; font-size: 12px; font-weight: bold;">
								<b><bean:write name="serEntView" property="userName" /></b>
							</td>
							<td style="color: #173B63; font-size: 12px; font-weight: bold;">
								<b><bean:write name="serEntView" property="entityName" /></b>
							</td>
							<td style="color: #173B63; font-size: 12px; font-weight: bold;">
								<b><bean:write name="serEntView" property="moduleName" /></b>
							</td>
							<td style="color: #173B63; font-size: 12px; font-weight: bold;">
								<b><bean:write name="serEntView" property="disclosureStatus" /></b>
							</td>
							<td
								style="color: #173B63; font-size: 12px; font-weight: bold; text-align: center">
								<b><bean:write name="serEntView"
										property="dispositionStatus" /></b>
							</td>
							<td style="color: #173B63; font-size: 12px; font-weight: bold;">
								<b><bean:write name="serEntView" property="updateTimestamp" /></b>
							</td>
							<td
								style="color: #173B63; font-size: 12px; font-weight: bold; text-align: center">
								<b><bean:write name="serEntView" property="updateUser" /></b>
							</td>
							<td style="color: #173B63; font-size: 12px; font-weight: bold;">
								<b><bean:write name="serEntView" property="expirationDate" /></b>
							</td>
						</tr>
						<%index++;%>
					</logic:iterate>
				</logic:present>
			</table>
			<table width="100%" border="0" class='tabtable' cellspacing="0"
				cellpadding="0">
				<tr>
					<td align="right" class="theader" height='30'><html:link
							href="javascript:history.go(-1)">
							<bean:message key="search.searchAgain" />
						</html:link></td>
					<td class='theader' height='30'>&nbsp;&nbsp;</td>
					<td align="left" class="theader" height='30'><html:link
							href="javascript://" onclick="window.close()">
							<bean:message key="search.closeWindow" />
						</html:link></td>
				</tr>
			</table>
		</logic:equal>
		<br />
		<br />
	</body>
</html:form>
</html>
