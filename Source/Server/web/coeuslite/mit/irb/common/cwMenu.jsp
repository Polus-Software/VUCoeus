<%@ page contentType="text/html;charset=UTF-8" language="java"
	import="java.util.Vector"%>
<%@page
	import="edu.mit.coeuslite.utils.CoeusLiteConstants,
                edu.mit.coeuslite.utils.bean.MenuBean"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ include file="/coeuslite/mit/utils/CoeusContextPath.jsp"%>
<jsp:useBean id="menuItemsVector" scope="session"
	class="java.util.Vector" />

<%  String protocolNumber = (String)session.getAttribute("PROTOCOL_NUMBER"+session.getId());
    String sequenceNumber = (String)session.getAttribute("SEQUENCE_NUMBER"+session.getId());
    String EMPTY_STRING = "";
    String strMode = (String)session.getAttribute("mode"+session.getId());
    boolean allPagesSaved = true;
    boolean generalInfoSaved = false;
    Vector menuList = (Vector) session.getAttribute("menuItemsVector");
    for (int i=0;i<menuList.size();i++) { 
        MenuBean protocolMenuBean = (MenuBean) menuList.elementAt(i);
        if (!protocolMenuBean.isDataSaved() && !protocolMenuBean.getMenuId().equals("003")) {
            allPagesSaved = false;
        } 
    }
    int index = 2;
    //COEUSQA-1433 - Allow Recall from Routing - Start
    boolean hasApproverRecallRight = false;
    boolean hasRecallRight = false;
    if(session.getAttribute("HAS_RECALL_RIGHTS")!=null){
        hasRecallRight = ((Boolean)session.getAttribute("HAS_RECALL_RIGHTS")).booleanValue();
    } 
    if(session.getAttribute("HAS_APPROVER_RECALL_RIGHTS")!=null){
       hasApproverRecallRight = ((Boolean)session.getAttribute("HAS_APPROVER_RECALL_RIGHTS")).booleanValue();
    }
    //COEUSQA-1433 - Allow Recall from Routing - End
    //Added for case#3551 - Protocol going into Pending/in Progress status after SMR action
    String statusCode = (String)session.getAttribute("protocolStatusCode");
    String statusCodeForMap = (String)session.getAttribute("statusCodeForMap");
    String routing = (String) request.getAttribute("ROUTING");
    String pageAttr = request.getParameter("PAGE");
    int approvalSequence=0;
    if(session.getAttribute("approvalSequenceNumber")!=null){
         approvalSequence=(Integer)session.getAttribute("approvalSequenceNumber");
        }
%>
<html:html locale="true">
<html:base />

<head>
<title>Untitled</title>
<link
	href="<bean:write name='ctxtPath'/>/coeuslite/mit/utils/css/coeus_styles.css"
	rel="stylesheet" type="text/css">
<style type="text/css">
<!--
body {
	margin-left: 3px;
}
-->
</style>
<script>
             var fromInbox = false;
            function setPath(reportPath,selected,generalInfoSaved) {
            if(selected != "true" && generalInfoSaved == "true") {
                CLICKED_LINK = reportPath;
                if(fromInbox || validate() != false){
                    //window.location= reportPath;
                    window.location= CLICKED_LINK;
                }
                
              } else {
                 if(selected != "true") {
                    // Case# 3783: No 'Do you want to save' When creating a new protocol and moving from General Info tab to Org -Start
                    // Case# 3067: CoeusLite IRB - PI Responding to change requests  
                    //Added for case#3551 - Protocol going into Pending/in Progress status after SMR action - start
                    // Case# 3791: Unable to resubmit a protocol that had been disapproved by the IRB - Start
                    // if('<%=statusCode%>' =='102' || '<%=statusCode%>' == '104'){
                    // if('<%=statusCode%>' =='102' || '<%=statusCode%>' == '104' || '<%=statusCode%>' =='304'){
                    // Case# 3791: Unable to resubmit a protocol that had been disapproved by the IRB - End
                        //Added/Modified for case#3024 - Saving on creating new protocol - start
                        // document.irbGeneralInfoForm.action = "<%=request.getContextPath()%>/generalInfo.do?Type=Add&statusCode=100";
                        // document.irbGeneralInfoForm.submit();
                        //Added for case#3024 - Saving on creating new protocol - end                    
                    // }else{
                        // Modified for COEUSQA-3697 : Coeus4.5: CoeusLite IACUC Questionnaire Disappearing - Start
                        alert("Please Save General Info.");
                        // CLICKED_LINK = reportPath;    
                        // while(CLICKED_LINK.indexOf('&')!=-1){
                            // CLICKED_LINK = CLICKED_LINK.replace("&","$");
                        // }
                        // if(LINK.indexOf('?')!=-1) {
                            // FORM_LINK.action = LINK+"&CLICKED_LINK="+CLICKED_LINK;
                        // } else {
                            // FORM_LINK.action = LINK+"?CLICKED_LINK="+CLICKED_LINK;
                        // }
                        // FORM_LINK.submit(); 
                      // }
                      // Modified for COEUSQA-3697 : Coeus4.5: CoeusLite IACUC Questionnaire Disappearing - End
                     // Case# 3783: No 'Do you want to save' When creating a new protocol and moving from General Info tab to Org -End   
                    //Added/Modified for case#3551 - Protocol going into Pending/in Progress status after SMR action - end
                  }
              }
            }
            function saveProtocol() {
                var allPagesSaved = <%=allPagesSaved%>;
                if(!allPagesSaved) {
                    alert('All sections marked * are mandatory.');
                    return;
                } else {
                    path =  "<%=request.getContextPath()%>/processProtocol.do";
                    window.location= path;
                }
            }
            
            //Added for Case# 3018 -create ability to delete pending studies  -  Start
            //Modified for Case# 3781_Rename Delete Protocol - Start
            function deleteProtocol(reportPath,selected,generalInfoSaved,value){
            if(selected != "true") {
                var confirmMsg ;
                if(value == 'P'){
                    confirmMsg = "Are you sure you want to delete the Protocol?";
                }else if(value == 'NA'){
                    confirmMsg = "Are you sure you want to delete the Amendment?";
                }else if(value == 'NR'){
                    confirmMsg = "Are you sure you want to delete the Renewal?";
                }
                    if (confirm(confirmMsg)==true){
                        path =  "<%=request.getContextPath()%>/deleteProtocol.do";
                        window.location= path;
                       }
                  }
            }
            //Modified for Case# 3781_Rename Delete Protocol - End
            //Added for Case# 3018 -create ability to delete pending studies - End
            
            //Added for Case#3940_In Premium the copied protocol with the original protocols approval date - start
            function amendRenewCopy(){
                alert("Cannot copy Amendment/Renewal");
            }
            //Case#3940_In Premium the copied protocol with the original protocols approval date - end
        </script>
</head>
<body>
	<table width="100%" height="100%" border="0" cellpadding="0"
		cellspacing="0" class="table">

		<tr>
			<td align="left" valign="top">
				<table width="195" border="0" align="center" cellpadding="0"
					cellspacing="0">
					<logic:iterate name="menuItemsVector" id="protocolmenubean"
						type="edu.mit.coeuslite.utils.bean.MenuBean">
						<bean:define id="menuId" name="protocolmenubean" property="menuId" />
						<%
                          // COEUSQA-1433 - Allow Recall from Routing - Start
                          String previousRouting=(String) session.getAttribute("showPreviousRouting");
                          if(menuId.equals("P010") && (hasRecallRight && hasApproverRecallRight)){
                                protocolmenubean.setVisible(true);
                          }else if(menuId.equals("P010") && (!hasRecallRight || !hasApproverRecallRight)){
                                protocolmenubean.setVisible(false);
                          }
                          else if(menuId.equals("P015") && ((approvalSequence<=2) && !("100".equals(statusCode)) || previousRouting==null)){
                              protocolmenubean.setVisible(false);
                          }
                          else if(menuId.equals("026") && ((statusCodeForMap!=null) && ("214".equals(statusCodeForMap)))){
                              protocolmenubean.setVisible(true);
                          }
                          //COEUSQA-1433 - Allow Recall from Routing - End
                          %>
						<%if(!("107".equals(statusCode) && "020".equals(menuId))){ // Submitted to IRB menu, Routing In Progress%>
						<% String linkName = protocolmenubean.getMenuName();
                             String group = protocolmenubean.getGroup();
                             group = (group==null)?"":group.trim();
                          %>
						<%if(index==Integer.parseInt(group)){%>
						<tr>
							<td>&nbsp;</td>
						</tr>
						<%index++;}%>
						<%if(menuId.equals("P021")) {%>
						<tr class="menuHeaderName">
							<td colspan='3'><%=linkName%></td>
						</tr>
						<%} else {%>
						<%if(protocolmenubean.isVisible()){%>
						<tr class="coeusMenuSeparator">
							<td colspan='3' height='2'></td>
						</tr>
						<tr>
							<td width="16%" height='19' align="left" valign="bottom"
								class="coeusMenu"><logic:equal name="protocolmenubean"
									property="dataSaved" value="true">
									<img
										src="<bean:write name='ctxtPath'/>/coeusliteimages/img_saved.gif"
										width="19" height="19">
								</logic:equal></td>
							<td width="80%" height='20' align="left" valign="top"
								class="coeusMenu"><bean:define id="name"
									name="protocolmenubean" property="menuName" /> <bean:define
									id="link_Value" name="protocolmenubean" property="menuLink" />
								<bean:define id="selected" name="protocolmenubean"
									property="selected" /> <bean:define id="fieldName"
									name="protocolmenubean" property="fieldName" /> <bean:define
									id="dataSaved" name="protocolmenubean" property="dataSaved" />
								<%
                                        String absPath = request.getContextPath()+link_Value;
                                        if(((String)fieldName).equals("GENERAL_INFO")){
                                            generalInfoSaved = ((Boolean)dataSaved).booleanValue();                                            
                                        } else if(((String)fieldName).equals("BACKTOINBOX")){
                                            generalInfoSaved = true;
                                            %> <script>
                                                fromInbox = true;
                                            </script> <%} else if("ROUTING".equals(routing)){
                                            generalInfoSaved = true;
                                        }
                                      %> <%if(((Boolean)selected).booleanValue()){%>
								<%-- Make the Request an Action menu always active --%> <%if(menuId.equals("020")){%>
								<b> <%-- <a style="color: #6D0202;" href="javascript:setPath('<%=absPath%>','<%=selected%>', '<%=generalInfoSaved%>');"> --%>
									<a style="color: #6D0202;"
									href="javascript:setPath('<%=absPath%>','false', '<%=generalInfoSaved%>');">
										<%=name%>
								</a>
							</b> <%}else{%> <font color="#6D0202"><b> <!-- Added/Modified for case#3057 – asterisk on left side menu label for Others - start -->
										<%String othersMandatory = (String)session.getAttribute("othersMandatory");%>
										<%if(menuId.equals("023") && othersMandatory != null && othersMandatory.equals("Y")){%>
										<font color="red">*</font><%=name%> <%}else{
                                               String starIndicator = linkName.substring(0,1);
                                               String newLink = linkName.substring(1,linkName.length());
                                            %> <%if(starIndicator.equals("*")){%>
										<font color="red">*</font><%=newLink%> <%}else{%> <%=name%> <%}%>
										<%}%>
								</b></font> <%}%> <%} else {
                                            String newSeq = (String) session.getAttribute("newSeq"+session.getId());
                                            if(newSeq!=null && newSeq.equals("incremented")){
                                                generalInfoSaved = false;
                                            }
                                            %> <%String othersMandatory = (String)session.getAttribute("othersMandatory");%>
								<%if(menuId.equals("023") && othersMandatory != null && othersMandatory.equals("Y")){%>
								<a
								href="javascript:setPath('<%=absPath%>','<%=selected%>', '<%=generalInfoSaved%>');"
								class='copy'> <font color="red">*</font><%=name%>
							</a> <%}else{
                                                String starIndicator = linkName.substring(0,1);
                                                String newLink = linkName.substring(1,linkName.length());                                                
                                            %> <%if(starIndicator.equals("*")){%>
								<a
								href="javascript:setPath('<%=absPath%>','<%=selected%>', '<%=generalInfoSaved%>');"
								class='copy'> <font color="red">*</font><%=newLink%>
							</a> <%}else{
                                                // Added for COEUSQA-3535 : CoeusLite IACUC "Double Amendment" Issue - Start
                                                if("021".equals(menuId) || "022".equals(menuId) || "029".equals(menuId)){
                                                    absPath = absPath +"&protocolNumber="+protocolNumber+"&sequenceNumber="+sequenceNumber;
                                                }
                                                // Added for COEUSQA-3535 : CoeusLite IACUC "Double Amendment" Issue - End
                                                    //Added for Case# 3018 -create ability to delete pending studies -Start
                                                    //Modified for Case# 3781_Rename Delete Protocol - Start
                                                    if(menuId.equals("025") && (fieldName.equals("DELETE PROTOCOL"))){%>
								<a
								href="javascript:deleteProtocol('<%=absPath%>','<%=selected%>', '<%=generalInfoSaved%>','P');">
									<%=name%>
							</a> <%}else if(menuId.equals("027") && (fieldName.equals("DELETE AMENDMENT"))){%>
								<a
								href="javascript:deleteProtocol('<%=absPath%>','<%=selected%>', '<%=generalInfoSaved%>','NA');">
									<%=name%>
							</a> <%}else if(menuId.equals("028") && (fieldName.equals("DELETE RENEWAL"))){%>
								<a
								href="javascript:deleteProtocol('<%=absPath%>','<%=selected%>', '<%=generalInfoSaved%>','NR');">
									<%=name%>
							</a> <%//Modified for Case# 3781_Rename Delete Protocol - End
                                                }else if(menuId.equals("010")){
                                                        //Case#3940_In Premium the copied protocol with the original protocols approval date - start
                                                        if((protocolNumber == null || protocolNumber.length() == 10) 
                                                                && !"NA".equals(pageAttr) && !"NR".equals(pageAttr)){%>
								<a
								href="javascript:setPath('<%=absPath%>','<%=selected%>', '<%=generalInfoSaved%>');"
								class='copy'> <%=name%>
							</a> <% }else{%> <a href="javascript:amendRenewCopy();"> <%=name%>
							</a> <%}
                                                  //Case#3940_In Premium the copied protocol with the original protocols approval date - end
                                                }else{%> <a
								href="javascript:setPath('<%=absPath%>','<%=selected%>', '<%=generalInfoSaved%>');"
								class='copy'> <%=name%>
							</a> <%}//Added for Case# 3018 -create ability to delete pending studies - End%>
								<%}%> <%}%> <%}%> <!-- Added/Modified for case#3057 – asterisk on left side menu label for Others - end -->
							</td>
							<td width='4%' align=right nowrap class="selectedMenuIndicator">
								<logic:equal name="protocolmenubean" property="selected"
									value="true">
									<bean:message key="menu.selected" />
								</logic:equal>
							</td>
						</tr>
						<tr class="coeusMenuSeparator">
							<td colspan='3' height='2'></td>
						</tr>
						<%}} 
                          }%>
					</logic:iterate>
				</table>
			</td>
		</tr>
	</table>
</body>
</html:html>
