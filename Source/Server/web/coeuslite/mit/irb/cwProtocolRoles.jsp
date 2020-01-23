<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ include file="/coeuslite/mit/utils/CoeusContextPath.jsp"%>
<%@ page
	import="org.apache.struts.validator.DynaValidatorForm ,
        edu.mit.coeuslite.utils.CoeusLiteConstants, edu.mit.coeuslite.irb.bean.ProtocolHeaderDetailsBean,java.util.HashMap"%>
<jsp:useBean id="vecRolesType" scope="session" class="java.util.Vector" />
<jsp:useBean id="protocolRolesDetails" scope="session"
	class="java.util.TreeMap" />
<html:html>
<%  String EMPTY_STRING = "";
    String link = EMPTY_STRING;
    ProtocolHeaderDetailsBean headerBean=(ProtocolHeaderDetailsBean)session.getAttribute("protocolHeaderBean");
    String createUser = headerBean.getCreateUser();
    boolean isError = new Boolean(request.getAttribute("protoRoleError").toString()).booleanValue();
    String mode=(String)session.getAttribute(CoeusLiteConstants.MODE_DETAILS+session.getId());
    boolean modeValue=false;
    if(mode.equalsIgnoreCase(CoeusLiteConstants.DISPLAY_MODE)){
        mode=(String)session.getAttribute("rolesMode"+session.getId());
        modeValue=true;
        if(mode!=null && mode.equalsIgnoreCase(CoeusLiteConstants.MODIFY_MODE)){
            modeValue=false;
        }
    }else if(mode.equalsIgnoreCase(CoeusLiteConstants.MODIFY_MODE)){
        modeValue=false;
    }else if(mode.equalsIgnoreCase(CoeusLiteConstants.ADD_MODE)){
        modeValue=false;
    }
%>
<head>
<title>JSP Page</title>
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

function removeLink(value,data){
    if (confirm(<bean:message key="protocolRoles.removeConfirm"/>)==true){
        document.protocolRolesForm.action = "<%=request.getContextPath()%>/deleteProtoUser.do?removeProtoRole="+value+"&removeProtoUser="+data;
        document.protocolRolesForm.submit();
    }
}       
var roleName ="";
function searchWindow(value) {       
    roleName = value;
    var winleft = (screen.width - 650) / 2;
    var winUp = (screen.height - 450) / 2;  
    var win = "scrollbars=1,resizable=1,width=750,height=450,left="+winleft+",top="+winUp;        
       sList = window.open('<%=request.getContextPath()%>/irbSearch.do?type=<bean:message key="searchWindow.title.person"/>&search=true&searchName=USERSEARCH', "list", win);  
   if (parseInt(navigator.appVersion) >= 4) {
        window.sList.focus(); 
        }
    } 

 function fetch_Data(result){        
          var homeUnit = "";
          if(result["USER_ID"] != 'null' && result["USER_ID"] != undefined){
          userId = result["USER_ID"];
          }
          if(result["USER_NAME"] != 'null' && result["USER_NAME"] != undefined){
          userName = result["USER_NAME"];
          }
          if(result["UNIT_NUMBER"] != 'null' && result["UNIT_NUMBER"] != undefined){
          unitNum = result["UNIT_NUMBER"];
          }
          if(result["UNIT_NAME"] != 'null' && result["UNIT_NAME"] != undefined){
          unitName = result["UNIT_NAME"];
          }
          homeUnit = unitNum+" : "+unitName;
          document.protocolRolesForm.action = "<%=request.getContextPath()%>/addProtoUser.do?protoRole="+roleName+"&protoUserId="+userId+"&protoUserName="+userName+"&protoUnitNum="+unitNum+"&protoUnitName="+unitName+"&protoUserHomeUnit="+homeUnit;
          document.protocolRolesForm.submit();
    }        
</script>
</head>
<body>

	<html:form action="/getProtocolRoles">
		<script type="text/javascript">
    document.getElementById('txt').innerHTML = '<bean:message key="helpPageTextProtocol.Roles"/>';
</script>
		<table width='100%' border='0' cellpadding='0' cellspacing='0'
			class='table'>
			<%String plus = request.getContextPath()+"/coeusliteimages/plus.gif";
  String minus = request.getContextPath()+"/coeusliteimages/minus.gif";%>

			<tr class='core'>
				<td>
					<table width='100%' border='0' cellpadding='2' cellspacing='0'
						class='tabtable'>
						<tr>
							<td colspan='4'>
								<table width="100%" border="0" cellpadding="0" cellspacing="0">
									<tr class="theader">
										<td width="50%"><bean:message
												key="protocolRoles.ProtocolRoles" /></td>
										<td align="right" width="50%"><a id="helpPageText"
											href="javascript:showBalloon('helpPageText', 'helpText')">
												<bean:message key="helpPageTextProtocol.help" />
										</a></td>
									</tr>
								</table>
							</td>
						</tr>
						<%if(!isError){%>
						<tr>
							<td class='copy' colspan='4'><font color="red"> <html:errors
										header="" footer="" /> <logic:messagesPresent message="true">
										<html:messages id="message" message="true" property="errMsg">
											<li><bean:write name="message" /></li>
										</html:messages>
										<html:messages id="message" message="true" property="acqLock">
											<li><bean:write name="message" /></li>
										</html:messages>
										<html:messages id="message" message="true"
											property="protocolRoles.userId">
											<li><bean:write name="message" /></li>
										</html:messages>
									</logic:messagesPresent>
							</font></td>
						</tr>
						<% int count = 1;%>
						<logic:iterate id="hmapId" name="protocolRolesDetails"
							indexId="index">
							<bean:define id="protoRole" name="hmapId" property="key" />
							<% HashMap hmRoleName = (HashMap)session.getAttribute("protocolRoleNameId");
                                       String roleNam = (String)hmRoleName.get(protoRole);
                                    %>
							<tr>
								<td class="copybold">
									<%String divName="Panel"+count;%>
									<div id='<%=divName%>' style='display: none;'>

										<%String divlink = "javascript:showHide(1,'"+count+"')";%>
										<html:link href="<%=divlink%>">
											<!-- Modified for Case#3291 - Start
                                                            To avoid the double quotes when setting the attributes to img tag-->
											<%String imagePlus=request.getContextPath()+"/coeusliteimages/plus.gif";%>
											<html:img src="<%=imagePlus%>" border="0" />
											<%--<html:img src="<%=request.getContextPath()+"/coeusliteimages/plus.gif"%>" border="0"/> --%>
											<!-- Modified for Case#3291 - End -->
										</html:link>
										&nbsp;
										<%=roleNam%>

									</div> <%divName="hidePanel"+count;%>
									<div id='<%=divName%>'>

										<% divlink = "javascript:showHide(2,'"+count+"')";%>
										<html:link href="<%=divlink%>">
											<!-- Modified for Case#3291 - Start
                                                            To avoid the double quotes when setting the attributes to img tag-->
											<%String imageMinus=request.getContextPath()+"/coeusliteimages/minus.gif";%>
											<html:img src="<%=imageMinus%>" border="0" />
											<%--<html:img src="<%=request.getContextPath()+"/coeusliteimages/minus.gif"%>" border="0"/> --%>
											<!-- Modified for Case#3291 - End -->
										</html:link>
										&nbsp;
										<%=roleNam%>

									</div>
								</td>
								<td align=right>
									<!-- Modified for COEUSQA-2824_IRB and IACUC protocol access permissions should not include approver_start-->
									<%if(((Integer)protoRole != CoeusLiteConstants.IRB_PROTOCOL_APPROVER_ID) && !modeValue){%>
									<% 
                                    link = "javaScript:searchWindow('"+protoRole+"');";%>
									<html:link href="<%=link%>">
										<u><bean:message key="protocolRoles.Users" /></u>
									</html:link> <%}%>
								</td>
								<td>
									<!-- Modified for COEUSQA-2824_IRB and IACUC protocol access permissions should not include approver_end-->
								</td>
							</tr>

							<tr>
								<td colspan='3'>
									<%divName="pan"+count;%>
									<div id='<%=divName%>'>
										<table width='100%' border='0' cellpadding='3' cellspacing='1'
											class='tabtable'>
											<tr>
												<td class='theader' width='5%' nowrap><bean:message
														key="protocolRoles.UserID" /></td>
												<td class='theader' width='30%'><bean:message
														key="protocolRoles.UserName" /></td>
												<td class='theader' colspan='2' width='50%'><bean:message
														key="protocolRoles.HomeUnit" /></td>
												<%if(!modeValue){%>
												<td class='theader' width='15%'></td>
												<%}%>
											</tr>
											<%        String appKey = null;
                                                        String strBgColor = "#DCE5F1";
                                                         int row=0;                                                        
                                                        %>
											<bean:define id="beanData" name="hmapId" property="value"
												type="java.util.Vector" />
											<logic:iterate id="user" name="beanData"
												type="org.apache.struts.validator.DynaValidatorForm">
												<%
                                                if(!(user.get("roleId").equals(EMPTY_STRING))&&(user.get("roleId") != null)){
                                                int protoRoleId = ((Integer)user.get("roleId")).intValue();
                                                 }%>

												<% if(!(user.get("userId").equals(EMPTY_STRING))&&(user.get("userId") != null)){
                                                String protoUserId = (String)user.get("userId");
                                                %>

												<%if (row%2 == 0) {
                                                    strBgColor = "#D6DCE5"; 
                                                }
                                               else { 
                                                strBgColor="#DCE5F1"; 
                                             }   %>
												<tr bgcolor="<%=strBgColor%>"
													onmouseover="className='TableItemOn'"
													onmouseout="className='TableItemOff'">
													<td class='copy' width='5%' nowrap><bean:write
															name="user" property="userId" />
														<bean:define id="roleUserId" name="user" property="userId" /></td>
													<td class='copy' width='30%'><bean:write name="user"
															property="userName" /></td>
													<td class='copy' colspan='2' width='50%'><bean:write
															name="user" property="unitNumber" />: <bean:write
															name="user" property="unitName" /></td>
													<%if(!modeValue){%>
													<%-- If condition modified for internal issue fix 1834--%>
													<%-- <%if(!createUser.equals(roleUserId) || !roleNam.equals("Protocol Aggregator")){%> --%>
													<!-- Modified for COEUSQA-2824_IRB and IACUC protocol access permissions should not include approver_start-->
													<%if(((Integer)protoRole != CoeusLiteConstants.IRB_PROTOCOL_APPROVER_ID)){%>
													<%if((beanData != null && beanData.size() > 1) 
                                                    &&!createUser.equals(roleUserId) 
                                                    || !roleNam.equals("Protocol Aggregator")){%>

													<%String removeLink = "javaScript:removeLink('"+protoRole+"','"+roleUserId+"')";%>
													<td class='copy' width='15%' align=center><html:link
															href="<%=removeLink%>">
															<bean:message key="protocolRoles.Remove" />
														</html:link></td>
													<%}else{%>
													<td class='copy' width='15%'></td>
													<%}
                                                }else{%>
													<td class='copy' width='15%'></td>
													<%}            
                                               }%>
													<!-- Modified for COEUSQA-2824_IRB and IACUC protocol access permissions should not include approver_end-->
												</tr>
												<%}row++;%>
											</logic:iterate>

										</table>
									</div>
								</td>
							</tr>
							<%count++;%>
						</logic:iterate>
						<tr>
							<td colspan='4'>&nbsp;</td>
						</tr>
						<%}else{%>
						<tr>
							<td colspan='4' class='copybold'><font><bean:message
										key="protocolRoles.error" /></font></td>
						</tr>
						<%}%>
					</table>
				</td>
			</tr>

		</table>
	</html:form>
</body>
</html:html>


