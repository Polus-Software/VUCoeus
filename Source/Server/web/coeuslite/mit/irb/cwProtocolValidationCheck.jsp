<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/coeus-utils.tld" prefix="coeusUtils"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ include file="/coeuslite/mit/utils/CoeusContextPath.jsp"%>
<%@ page
	import="edu.mit.coeuslite.utils.CoeusLiteConstants,java.util.Vector,java.util.List,java.lang.Integer, edu.mit.coeus.s2s.bean.FormInfoBean, edu.mit.coeus.s2s.validator.S2SValidationException, edu.mit.coeus.exception.CoeusException"%>
<jsp:useBean id="PROTOCOL_RULES_MAP" scope="session"
	class="java.util.Vector" />

<% 
String protocolNumber = (String) session.getAttribute("PROTOCOL_NUMBER"+session.getId());  
String sequenceNumber = (String)session.getAttribute("SEQUENCE_NUMBER"+session.getId());
String actionType = (String)request.getParameter("ACTION");
// Commented for COEUSQA-3085 :Inability to Submit Through Validation Check in Lite - Start
// Irrespactive to Modify/Display mode OK button to be displayed for the validation page
//String mode=(String)session.getAttribute(CoeusLiteConstants.MODE_DETAILS+session.getId());
//String lockMode = (String) session.getAttribute(CoeusLiteConstants.LOCK_MODE+session.getId());
int errorHeader = 1;
int warningHeader = 1;
//boolean modeValue=false;
        //Modified to set modeValue true if lockMode or mode value is'D'
//        if(lockMode != null && lockMode.equalsIgnoreCase(CoeusLiteConstants.DISPLAY_MODE)){
//            modeValue=true;
//        }else if(mode!=null){
//            if(mode.equalsIgnoreCase(CoeusLiteConstants.DISPLAY_MODE)){
//                modeValue=true;
//            }else if(mode.equalsIgnoreCase(CoeusLiteConstants.MODIFY_MODE)){
//                modeValue=false;
//            }else if(mode.equalsIgnoreCase(CoeusLiteConstants.ADD_MODE)){
//                modeValue=false;
//            }
//        }
// Commented for COEUSQA-3085 :Inability to Submit Through Validation Check in Lite - End
%>

<html:html>
<head>
<title>JSP Page</title>
<html:base />
<script>
        var errorPresent = false;
        function showProtocolDetails(f){
        var validationPresent = false;      
        var warningPresent = false;
        var type = '<%=actionType%>';        
            document.protocolValidationCheck.ok.disabled=true;
            var data = true ;            
            if(type =='null'){
                if(!errorPresent) {                    
                    if(!errorPresent){                        
                       data = confirm("<bean:message key="protocolValidationRules.submitForReview"/>")
                    }
                    if(data){
                        document.protocolValidationCheck.action = "<%=request.getContextPath()%>/getSubmitForReview.do?PAGE=SR&warnPresent=Y";
                        document.protocolValidationCheck.submit();
                    }else {
                        document.protocolValidationCheck.action = "<%=request.getContextPath()%>/getProtocolData.do?PAGE=G";
                        document.protocolValidationCheck.submit();
                    }                  

                } else {
                    document.protocolValidationCheck.action = "<%=request.getContextPath()%>/getProtocolData.do?PAGE=G";
                    document.protocolValidationCheck.submit();
                }
            <%-- Added for COEUSDEV-858 Add a validation button to proposal summary in lite (approval screens) for business rules Validations - start--%>
            }else if(type == 'VC'){
                    document.protocolValidationCheck.action = "<%=request.getContextPath()%>/displayProtocol.do?PAGE=G&protocolNumber=<%=protocolNumber%>&sequenceNumber=<%=sequenceNumber%>";
                    document.protocolValidationCheck.submit();
            }else{
                    document.protocolValidationCheck.action = "<%=request.getContextPath()%>/getProtocolData.do?PAGE=G";
                    document.protocolValidationCheck.submit();
            }
            <%-- Added for COEUSDEV-858 Add a validation button to proposal summary in lite (approval screens) for business rules Validations - end--%>       
        }
    </script>
</head>
<body>

	<html:form action="/protocolValidationType.do">
		<script type="text/javascript">
            document.getElementById('txt').innerHTML = '<bean:message key="helpTextProtocol.ValidationCheck"/>';
    </script>
		<table width='100%' cellpadding='0' cellspacing='0' class='table'>
			<%Exception ex = (Exception)request.getAttribute("Exception");%>
			<tr>
				<td>
					<table width='100%' cellpadding='0' cellspacing='0'
						class='tabtable'>

						<tr>
							<td height='20' colspan='4' class='copybold'><font
								color='red'> <html:errors header="" footer="" />
							</font> <%if((PROTOCOL_RULES_MAP!=null)&&(PROTOCOL_RULES_MAP.size()>0)){%>
								<script>
            validationPresent = true;
          </script>
								<table width="100%" border="0" cellpadding="0" cellspacing="0"
									class="tabtable">
									<tr>
										<td width="100%" colspan='3' class='copybold'>
											<table width="100%" border='0' class='tableheader'>
												<tr>
													<td><bean:message
															key="protocolValidationRules.FailedValidations" /> <font
														color='Red'> <%=protocolNumber%>
													</font>
													<td>
													<td><a id="helpPageText"
														href="javascript:showBalloon('helpPageText', 'helpText')">
															<bean:message key="helpPageTextProtocol.help" />
													</a></td>
												</tr>
											</table>
										</td>
									</tr>
									<tr>
										<td colspan='3' height='100%'></td>
									</tr>

									<logic:present name="PROTOCOL_RULES_MAP" scope="session">
										<logic:iterate id="validationRules" name="PROTOCOL_RULES_MAP"
											type="org.apache.commons.beanutils.DynaBean">
											<%if(validationRules.get("ruleCategory") != null && validationRules.get("ruleCategory").equals("E") && (errorHeader == 1)){errorHeader++;%>
											<script>
                                errorPresent = true;
                            </script>
											<tr>
												<td class="copybold" colspan="2"><font color=red><bean:message
															key="protocolValidationRules.Protocol" /> <%=protocolNumber%>
														<bean:message key="protocolValidationRules.error" /></font></td>
											</tr>
											<tr>
												<td height="10" class="copybold" colspan="2">&nbsp;</td>
											</tr>
											<%} else if(validationRules.get("ruleCategory") != null && validationRules.get("ruleCategory").equals("W") && (warningHeader ==1) ){ warningHeader++;%>
											<script>
                            warningPresent = true;
                            </script>
											<tr>
												<td class="copybold" colspan="2"><font color=blue><bean:message
															key="protocolValidationRules.Protocol" /> <%=protocolNumber%>
														<bean:message key="protocolValidationRules.warning" /></font></td>
											</tr>
											<tr>
												<td height="10" class="copybold" colspan="2">&nbsp;</td>
											</tr>
											<%}%>
											<tr>
												<td width="10%" class="copybold"><bean:message
														key="protocolValidationRules.Department" /></td>
												<td width="80%" class="copybold"><coeusUtils:formatOutput
														name="validationRules" property="unitName" /></td>
											</tr>
											<tr>
												<td colspan='3' class="copy"><coeusUtils:formatOutput
														name="validationRules" property="description" /></td>
											</tr>
											<tr>
												<td colspan='3' width="100%">&nbsp;</td>
											</tr>
										</logic:iterate>
									</logic:present>
								</table> <%}else{
                if(ex == null){%>
								<table width="100%" height="100%" border="0" cellpadding="0"
									cellspacing="0" class="table">
									<tr>
										<td height='100%'>&nbsp;</td>
									</tr>
									<tr>
										<td height='100%' align=center>&nbsp;<bean:message
												key="protocolValidationRules.Success" />

										</td>
									</tr>
									<tr>
										<td height='100%'>&nbsp;</td>
									</tr>
								</table> <%}%> <%}%></td>
						</tr>
						<tr>
							<td>
								<%-- // Modified for COEUSQA-3085	Inability to Submit Through Validation Check in Lite - Start
                    // Irrespactive to Modify/Display mode OK button to be displayed for the validation page
                    <html:button value="OK"  property="ok" styleClass="clsavebutton" disabled="<%=modeValue%>" onclick="showProtocolDetails()"  /> --%>
								<html:button value="OK" property="ok" styleClass="clsavebutton"
									onclick="showProtocolDetails()" /> <%-- // Modified for COEUSQA-3085	Inability to Submit Through Validation Check in Lite - Start --%>
							</td>
						</tr>
					</table>
				</td>
			</tr>
		</table>
	</html:form>

	<script>
          var help = ' <bean:message key="helpTextProtocol.ValidationCheck"/>';
          help = trim(help);
          if(help.length == 0) {
                document.getElementById('helpText').style.display = 'none';
          }
          function trim(s) {
                return s.replace( /^\s*/, "" ).replace( /\s*$/, "" );
          }  
      </script>
</body>
</html:html>
