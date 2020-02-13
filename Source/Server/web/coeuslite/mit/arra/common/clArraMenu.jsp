<%@ page contentType="text/html;charset=UTF-8" language="java"
	import="java.util.Vector"%>
<%@page
	import="edu.mit.coeuslite.utils.CoeusLiteConstants,
                edu.mit.coeuslite.utils.bean.MenuBean,edu.mit.coeuslite.utils.CoeusliteMenuItems,
                edu.mit.coeuslite.arra.action.ArraBaseAction"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ include file="/coeuslite/mit/utils/CoeusContextPath.jsp"%>
<jsp:useBean id="arraMenuItems" scope="session" class="java.util.Vector" />

<% 
    String arraReportNo  = (String)session.getAttribute("arraReportNo");
    String arraReportAwardNo = (String)session.getAttribute("arraReportAwardNo");
    String awardType = (String)session.getAttribute("awardType");
    //Based on the Award type, report can be generated
    String reportId = "ArraReport/ContractReport";
    if(ArraBaseAction.GRANT_AWARD_TYPE.equalsIgnoreCase(awardType)){
        reportId = "ArraReport/GrantLoanReport";
    } 
    // Arra Phase 2 Changes
    Integer currentVersionNumber = (Integer)session.getAttribute("arraReportVersion");
    
    String strMode = (String)session.getAttribute("mode"+session.getId());
    boolean allPagesSaved = true;
    boolean generalInfoSaved = false;
    Vector menuList = (Vector) session.getAttribute("arraMenuItems");
 
    int index = 2;
    
%>
<html:html locale="true">
<html:base />

<head>
<title>Untitled</title>
<link
	href="<bean:write name='ctxtPath'/>/coeuslite/mit/utils/css/coeus_styles.css"
	rel="stylesheet" type="text/css">
<style type="text/css">
</style>
<script>
            function setPath(reportPath) {
                CLICKED_LINK = reportPath;
             if( validate() != false){
                    window.location= CLICKED_LINK;
                }
            }
           function submitArraReport(reportId){ 
             if(confirm("<bean:message bundle="arra" key="arraSubmitCompleted.submitwarning"/>")){
                var getId = document.getElementById('submitId');
                if(getId != null && getId != undefined){
                getId.style.display="none";
                }
                if(document.arraAwardDetailsForm !=null && document.arraAwardDetailsForm != undefined){
                document.arraAwardDetailsForm.action = "<%=request.getContextPath()%>/openXml.do?reportId="+reportId;
                document.arraAwardDetailsForm.submit();  
                }
                else if(document.subcontractDynaBeansList != null && document.subcontractDynaBeansList != undefined){
                document.subcontractDynaBeansList.action = "<%=request.getContextPath()%>/openXml.do?reportId="+reportId;
                document.subcontractDynaBeansList.submit();  
                }
                else if(document.arraVendorDynaBeansList != null && document.arraVendorDynaBeansList != undefined){
                document.arraVendorDynaBeansList.action = "<%=request.getContextPath()%>/openXml.do?reportId="+reportId;
                document.arraVendorDynaBeansList.submit();  
                }
                else if(document.arraAwardSubcontractForm != null && document.arraAwardSubcontractForm != undefined){
                document.arraAwardSubcontractForm.action = "<%=request.getContextPath()%>/openXml.do?reportId="+reportId;
                document.arraAwardSubcontractForm.submit();  
                }
             }
           }
        </script>
</head>
<body>
	<table width="100%" height="100%" border="0" cellpadding="0"
		cellspacing="0" class="table">

		<tr>
			<td align="left" valign="top">
				<table width="195" border="0" align="center" cellpadding="0"
					cellspacing="0">
					<logic:iterate name="arraMenuItems" id="arraMenuBean"
						type="edu.mit.coeuslite.utils.bean.MenuBean">
						<bean:define id="menuId" name="arraMenuBean" property="menuId" />
						<% String linkName = arraMenuBean.getMenuName();
                             String group = arraMenuBean.getGroup();
                             group = (group==null)?"":group.trim();
                            /* //Set Mark Complete Invisible if page is displayed in View Mode - START
                             if(arraMenuBean.getMenuId().equals("AR004") && strMode.equals("D")){ //Check for Mark Complete
                                 arraMenuBean.setVisible(false);
                             }else {
                                 arraMenuBean.setVisible(true);
                             }
                             //Set Mark Complete Invisible if page is displayed in View Mode - END */
                          %>
						<%if(index==Integer.parseInt(group)){%>
						<tr>
							<td>&nbsp;</td>
						</tr>
						<%index++;}%>
						<%if(arraMenuBean.isVisible()){%>
						<tr class="coeusMenuSeparator">
							<td colspan='3' height='2'></td>
						</tr>
						<tr>
							<td width="16%" height='19' align="left" valign="bottom"
								class="coeusMenu"><logic:equal name="arraMenuBean"
									property="dataSaved" value="true">
									<img
										src="<bean:write name='ctxtPath'/>/coeusliteimages/img_saved.gif"
										width="19" height="19">
								</logic:equal></td>
							<td width="80%" height='20' align="left" valign="top"
								class="coeusMenu"><bean:define id="name"
									name="arraMenuBean" property="menuName" /> <bean:define
									id="link_Value" name="arraMenuBean" property="menuLink" /> <bean:define
									id="selected" name="arraMenuBean" property="selected" /> <bean:define
									id="fieldName" name="arraMenuBean" property="fieldName" /> <bean:define
									id="dataSaved" name="arraMenuBean" property="dataSaved" /> <%-- AR006 - Print link and AR008 for Submit Report link
                                        if("AR006".equals(arraMenuBean.getMenuId()) || "AR008".equals(arraMenuBean.getMenuId())){--%>
								<%
                                        if("AR008".equals(arraMenuBean.getMenuId())){
                                            link_Value = "/printArraReport.do?repId="+reportId;
                                        }
                                        // // Arra Phase 2 Changes
                                        //String absPath = request.getContextPath()+link_Value+"&arraReportNo="+arraReportNo+"&arraReportAwardNo="+arraReportAwardNo;
                                        String absPath = request.getContextPath()+link_Value+"&arraReportNo="+arraReportNo+"&arraReportAwardNo="+arraReportAwardNo+"&arraVersionNumber="+currentVersionNumber;
                                        if(((Boolean)selected).booleanValue()){%>
								<font color="#6D0202"><b> <%
                                               String starIndicator = linkName.substring(0,1);
                                               String newLink = linkName.substring(1,linkName.length());
                                            %> <%if(starIndicator.equals("*")){%>
										<font color="red">*</font><%=newLink%> <%}else{%> <%=name%> <%}%>
								</b></font> <%} else {%> <%
                                                String starIndicator = linkName.substring(0,1);
                                                String newLink = linkName.substring(1,linkName.length());                                                
                                            %> <%if(starIndicator.equals("*")){%>
								<a href="javascript:setPath('<%=absPath%>');" class='copy'>
									<font color="red">*</font><%=newLink%>
							</a> <%}else if (arraMenuBean.getMenuId().equals(CoeusliteMenuItems.ARRA_GRANT_LOAN_REPORT_PRINT_MENU_CODE)
                                                || arraMenuBean.getMenuId().equals(CoeusliteMenuItems.ARRA_CONTRACT_REPORT_PRINT_MENU_CODE)){%>
								<a href="<%=absPath%>" class='copy' target='_blank'> <%=name%>
							</a> <%}else if(arraMenuBean.getMenuId().equals(CoeusliteMenuItems.ARRA_SUBMIT_COMPLETED_MENU_CODE)){%>
								<span id="submitId" style="display: block;"><a
									href="javascript:submitArraReport('<%=reportId%>');"
									class='copy'> <%=name%>
								</a></span> <%}else{%> <a href="javascript:setPath('<%=absPath%>');"
								class='copy'> <%=name%>
							</a> <%}%> <%}%></td>
							<td width='4%' align=right nowrap class="selectedMenuIndicator">
								<logic:equal name="arraMenuBean" property="selected"
									value="true">
									<bean:message key="menu.selected" />
								</logic:equal>
							</td>
						</tr>
						<tr class="coeusMenuSeparator">
							<td colspan='3' height='2'></td>
						</tr>
						<%}%>
					</logic:iterate>
				</table>
			</td>
		</tr>
	</table>
</body>
</html:html>
