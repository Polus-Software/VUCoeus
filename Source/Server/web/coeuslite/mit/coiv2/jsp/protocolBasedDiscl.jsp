<%--
    Document   : awardBasedDiscl
    Created on : Mar 17, 2010, 6:48:32 PM
    Author     : Sony
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"
	import="edu.mit.coeuslite.coiv2.beans.CoiDisclosureBean,java.util.HashMap,edu.mit.coeuslite.coiv2.utilities.CoiConstants,java.util.Vector,edu.mit.coeuslite.coiv2.beans.CoiProtocolInfoBean,edu.mit.coeuslite.coiv2.services.CoiCommonService;"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/coeus-utils.tld" prefix="coeusUtils"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1" />
<title>C O I</title>
<link
	href="<%=request.getContextPath()%>/coeuslite/mit/coiv2/css/layout.css"
	rel="stylesheet" type="text/css" />
<link
	href="<%=request.getContextPath()%>/coeuslite/mit/coiv2/css/styles.css"
	rel="stylesheet" type="text/css" />
<link
	href="<%=request.getContextPath()%>/coeuslite/mit/coiv2/css/collapsemenu.css"
	rel="stylesheet" type="text/css" />
<%String path = request.getContextPath();                   
                    String disable = "";                   
                    String operationType = (String) request.getAttribute("operation");
                    CoiDisclosureBean disclosureBean = (CoiDisclosureBean) request.getSession().getAttribute("disclosureBeanSession");
                    String target = "#";
                  // if (disclosureBean == null || disclosureBean.getCoiDisclosureNumber() == null) {
                     //   target = path + "/getCompleteDisclCoiv2.do?projectType=Protocol";
                   // } else {
                        target = path + "/coiCommonSave.do";
                   // }
                     int index = 0;
        %>
</head>
<script src="js/jquery.js" type="text/javascript"></script>
<script type="text/javascript">
        function addDisclSubmit(count){
              var canContinue=false;
        var cc=parseInt(count);
        if(cc>0){
          for(i=0;i<=cc;i++ ){
             if(document.forms[0].checkedProtocolBasedProjects[i] == undefined) {
                 if(document.forms[0].checkedProtocolBasedProjects.checked==true){
                      canContinue=true;
                }
             }
             else if(document.forms[0].checkedProtocolBasedProjects[i].checked==true)
             {
                canContinue=true;
                break;
             }
          }
        }
        else{
             if(document.forms[0].checkedProtocolBasedProjects.checked==true)
             {
                canContinue=true;
             }
        }
        if(canContinue==false)
          {
              alert("Please select a protocol to continue");
          }else{
            document.forms[0].action='<%=target%>';
            document.forms[0].submit();
            }
        }
        function showPjtDet(){
            document.getElementById("pjtDet").style.visibility="visible";
        }
    </script>

<table id="bodyTable" class="table" style="width: 100%;" border="0">
	<html:form action="/createDisclosureCoiv2.do">
		<logic:notPresent name="protocolList">
			<tr>
				<td><p style="color: #ff0000; font-size: 12px;">There are
						no IRB protocols in the system that requires your COI disclosure.</p></td>
			</tr>
		</logic:notPresent>
		<logic:present name="protocolList">
			<logic:empty name="protocolList">
				<tr>
					<td><p style="color: #ff0000; font-size: 12px;">There are
							no IRB protocols in the system that requires your COI disclosure.</p></td>
				</tr>
			</logic:empty>
			<logic:notEmpty name="protocolList">
				<tr>
					<td colspan="6">Please select an IRB protocol from the list to
						create a new COI disclosure</td>
				</tr>
				<%--<div class="ProjectDetails">--%>
				<tr style="background-color: #6E97CF;">
					<td width="14%"><strong> Protocol #</strong></td>
					<td><strong>Title</strong></td>
					<td width="20%"><strong> Protocol Type</strong></td>
					<td width="15%"><strong> Application Date</strong></td>
					<td width="15%"><strong> Expiration Date</strong></td>
					<%--<td><strong> PI</strong></td>--%>

				</tr>
			</logic:notEmpty>
			<%
                                    String[] rowColors = {"#D6DCE5", "#DCE5F1"};
                                    int i = 0;
                                    String rowColor = "";
                                    Vector protoList = (Vector) request.getAttribute("protocolList");
                        %>
			<logic:iterate id="allProtocols" name="protocolList">
				<%
                                        if (i == 0) {
                                            rowColor = rowColors[0];
                                            i = 1;
                                        } else {
                                            rowColor = rowColors[1];
                                            i = 0;
                                        }
                                        String checked = "";
                                        String applicationDate = "";
                                        String expirationDate = "";
                                        CoiCommonService coiCommonService1 = CoiCommonService.getInstance();
                                        CoiProtocolInfoBean coiProtocolInfoBean = new CoiProtocolInfoBean();
                                        if (protoList != null && !protoList.isEmpty()) {
                                            coiProtocolInfoBean = (CoiProtocolInfoBean) protoList.get(index);
                                            if(coiProtocolInfoBean.getApplicationDate() != null) {
                                                 applicationDate = (String) coiCommonService1.getFormatedDate(coiProtocolInfoBean.getApplicationDate());
                                               }
                                            if(coiProtocolInfoBean.getExpirationDate() != null) {
                                                expirationDate = (String) coiCommonService1.getFormatedDate(coiProtocolInfoBean.getExpirationDate());
                                            }
                                            if (coiProtocolInfoBean.isChecked() == true) {
                                                checked = "checked";
                                            }
                                        }
                                        index++;
                            %>
				<tr bgcolor="<%=rowColor%>" class="rowLine"
					onmouseover="className='rowHover rowLine'"
					onmouseout="className='rowLine'" style="width: 765px;">
					<td><input type="radio" <%=disable%> <%=checked%>
						name="checkedProtocolBasedProjects"
						value="<bean:write name="allProtocols" property="protocolNumber"/>;<bean:write name="allProtocols" property="title"/>;<bean:write name="allProtocols" property="applicationDate"/>;<bean:write name="allProtocols" property="expirationDate"/>" />
					<bean:write name="allProtocols" property="protocolNumber" /></td>
					<td><bean:write name="allProtocols" property="title" /></td>
					<td><bean:write name="allProtocols" property="projectType" /></td>
					<%--<td><bean:write name="allProtocols" property="applicationDate"/></td>
                            <td><bean:write name="allProtocols" property="expirationDate"/></td>--%>
					<td><%=applicationDate%></td>
					<td><%=expirationDate%></td>
					<%--  <td width="50px"><bean:write name="<%=CoiConstants.FULL_NAME_LGGED_IN%>"/></td>--%>

				</tr>
			</logic:iterate>
			<logic:notEmpty name="protocolList">
				<tr>
					<td align="left" colspan="3">
						<% String link="javaScript:addDisclSubmit('" + index + "')"; %> <html:button
							styleClass="clsavebutton" value="Continue" property="save"
							onclick="<%=link%>" />
					</td>
				</tr>
			</logic:notEmpty>
		</logic:present>
		<tr bgcolor="#D1E5FF">
			<td colspan="6">
				<div class="myForm" id="pjtDet"
					style="visibility: hidden; overflow: hidden; height: 1px">
					<div class="Formline0">
						<br />
						<div class="FormLeft">
							Project Title <input type="text" name="textfield" id="textfield" />
						</div>
						<div class="FormRight">
							Project Dept. <input type="text" name="textfield" id="textfield" />
						</div>
					</div>

					<div class="Formline2">
						Description
						<textarea name="textarea" id="textarea" cols="50" rows="5"></textarea>
					</div>
					<div class="FormLeft" align="left">
						<html:button styleClass="clsavebutton" property="button"
							value="Submit"></html:button>
					</div>
					<div class="FormRight"></div>
				</div>
			</td>
		</tr>
	</html:form>

</table>
</html>
