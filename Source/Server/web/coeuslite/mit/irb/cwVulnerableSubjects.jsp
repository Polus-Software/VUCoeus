<? xml version="1.0" ?>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ page
	import="org.apache.struts.taglib.html.JavascriptValidatorTag,
                edu.mit.coeuslite.utils.CoeusLiteConstants"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<jsp:useBean id="VulnerableData" scope="session"
	class="java.util.Vector" />
<%@ include file="/coeuslite/mit/utils/CoeusContextPath.jsp"%>

<html:html>
<head>
<link
	href="<bean:write name='ctxtPath'/>/coeuslite/mit/utils/css/coeus_styles.css"
	rel="stylesheet" type="text/css">
<title>Coeus Web</title>
<script language="javaScript" type="text/JavaScript">
            var errValue = false;
            var errLock = false;
            function insert_data(data){
                document.vulerable.acType.value = data;
                document.vulerable.action = "<%=request.getContextPath()%>/vulerableSubjects.do";                
            }
            
            function delete_data(data,vulTypeCode,timestamp,subjectCnt){
                if (confirm('<bean:message key="delete.VulnerableSubjects"/>')==true){
                    document.vulerable.acType.value = data;
                    document.vulerable.vulnerableSubjectTypeCode.value = vulTypeCode;
                    document.vulerable.subjectCount.value = subjectCnt;
                    document.vulerable.updateTimeStamp.value = timestamp;
                    document.vulerable.action = "<%=request.getContextPath()%>/vulerableSubjects.do";
                    document.vulerable.submit();
                }
            } 
               
            //case id = 2478 - start     
            function clearFields(){                
                document.vulerable.subjectCount.value = "";
                dataChanged();
            }               
            //case id = 2478 - end
            
        </script>
</head>
<body>
	<%--html:errors/--%>

	<% String mode=(String)session.getAttribute(CoeusLiteConstants.MODE_DETAILS+session.getId());
            boolean modeValue=false;
            String EMPTY_STRING = "";
            if(mode.equalsIgnoreCase(CoeusLiteConstants.DISPLAY_MODE)){
                modeValue=true;
            }else if(mode.equalsIgnoreCase(CoeusLiteConstants.MODIFY_MODE)){
                modeValue=false;
            }else if(mode.equalsIgnoreCase(CoeusLiteConstants.ADD_MODE)){
                modeValue=false;
              }
            
             String subjectCountExists = (String)session.getAttribute("subjectCountExists");
            //System.out.println("Subject Count Flag >>>>>>>"+subjectCountExists);
            //Added for Coeus4.3 subject count enhancement - start
            String subjectCount = (String)session.getAttribute("subjectCount");
            //Added for Coeus4.3 subject count enhancement - end
 %>

	<%String protocolNo = (String)session.getAttribute(CoeusLiteConstants.PROTOCOL_NUMBER+session.getId());
      
            if(protocolNo!= null){ 
                 protocolNo = "["+"Protocol No. - "+ protocolNo+"]";
            }else{
                protocolNo = "";
                }
        //Added for coeus4.3 Amendments and Renewal enhancement
        String strProtocolNum = (String)session.getAttribute(CoeusLiteConstants.PROTOCOL_NUMBER+session.getId());
        if(strProtocolNum == null)
            strProtocolNum = "";    
        String amendRenewPageMode = (String)session.getAttribute("amendRenewPageMode"+request.getSession().getId());
        if(strProtocolNum.length() > 10 && (amendRenewPageMode == null || 
                amendRenewPageMode.equalsIgnoreCase(CoeusLiteConstants.DISPLAY_MODE))){
            modeValue = true;
        }%>
	<script>
        function validateForm(form) {
            //document.vulerable.count.value = document.vulerable.subjectCount.value;
            insert_data("I");
            //return validateVulerable(form);
        }
    </script>

	<html:form action="/vulerableSubjects.do" method="post"
		onsubmit="validateForm(this)">
		<a name="top"></a>

		<!-- New Template for cwVulnerableSubjects - Start   -->
		<script type="text/javascript">
    document.getElementById('txt').innerHTML = '<bean:message key="helpPageTextProtocol.Subjects"/>';
</script>

		<table width="100%" height="100%" border="0" cellpadding="0"
			cellspacing="0" class="table">

			<tr>
				<td height="20" align="left" valign="top" class="theader"><bean:message
						key="vulnSubLabel.VulnerableSubjects" /></td>
			</tr>
			<tr>
				<td>
					<div id="helpText" class='helptext'>
						&nbsp;&nbsp;
						<bean:message key="helpTextProtocol.Subjects" />
					</div>
				</td>
			</tr>
			<%-- Commented for case id#2627
        <tr>
            <td class="copybold">
                &nbsp;&nbsp;
                <font color="red">*</font> 
                <bean:message key="label.indicatesReqFields"/>
            </td>
        </tr>
        --%>
			<tr class='copy' align="left">
				<td><font color="red"> <logic:messagesPresent>
							<script>errValue = true;</script>
							<html:errors header="" footer="" />
						</logic:messagesPresent> <logic:messagesPresent message="true">
							<script>errValue = true;</script>
							<html:messages id="message" message="true" property="errMsg">
								<script>errLock = true;</script>
								<li><bean:write name="message" /></li>
							</html:messages>
							<html:messages id="message" message="true"
								property="vulnerable_subject_id">
								<li><bean:write name="message" /></li>
							</html:messages>
							<html:messages id="message" message="true"
								property="invalidNumber">
								<li><bean:write name="message" /></li>
							</html:messages>
						</logic:messagesPresent>
				</font></td>
			</tr>
			<!--  Vulnerable Subjects - Start  -->
			<tr>
				<td align="left" valign="top" class='core'><table width="100%"
						border="0" align="center" cellpadding="0" cellspacing="0"
						class="tabtable">
						<tr>
							<td colspan="4" align="left" valign="top"><table
									width="100%" height="20" border="0" cellpadding="0"
									cellspacing="0" class="tableheader">
									<tr>
										<td><bean:message
												key="vulnSubLabel.AddVulnerableSubjects" /></td>
										<td align="right"><a id="helpPageText"
											href="javascript:showBalloon('helpPageText', 'helpText')">
												<bean:message key="helpPageTextProtocol.help" />
										</a></td>
									</tr>
								</table></td>
						</tr>
						<tr>
							<td>
								<table width="99%" border="0" cellpadding="0" cellspacing="5">
									<tr>
										<td width="15%" nowrap class="copybold" align="left">
											<%-- Commented for case id#2627
                                    &nbsp;<font color="red">*</font>
                                    --%> <bean:message
												key="vulnSubLabel.Description" />:
										</td>

										<td nowrap class="copy"><html:select
												property="vulnerableSubjectTypeCode"
												styleClass="textbox-long" disabled="<%=modeValue%>"
												onchange="clearFields();">
												<html:option value="">
													<bean:message key="generalInfoLabel.pleaseSelect" />
												</html:option>
												<html:options collection="Subject" property="code"
													labelProperty="description" />
											</html:select></td>
										<%if (subjectCountExists.equals("NO")){%>
										<script>
                                        document.vulerable.vulnerableSubjectTypeCode.value = "";
                                    </script>
										<%}%>
									</tr>

									<!-- Modified for Coeus4.3 subject count enhancement - start -->
									<%if (!subjectCount.equals("N") && !subjectCount.equals(EMPTY_STRING)){ %>
									<tr>
										<td nowrap class="copybold" align="left"><bean:message
												key="vulnSubLabel.Count" />:</td>

										<td class="copy"><html:text property="subjectCount"
												styleClass="textbox-long" disabled="<%=modeValue%>"
												maxlength="6" onchange="dataChanged()" /></td>
										<%if (subjectCountExists.equals("NO")){%>
										<script>
                                            document.vulerable.subjectCount.value = "";
                                        </script>
										<%session.setAttribute("subjectCountExists","YES");
                                }%>
									</tr>
									<%} else {%>
									<tr>
										<td nowrap class="copybold" align="left">&nbsp;&nbsp;</td>

										<td class="copy"><html:hidden property="subjectCount" />
										</td>
									</tr>
									<%}%>
									<!-- Modified for Coeus4.3 subject count enhancement - end -->



								</table>
							</td>
						</tr>

					</table></td>
			</tr>
			<%if(!modeValue){%>
			<tr class='table'>
				<td class='savebutton' nowrap colspan="6"><html:submit
						property="Save" value="Save" styleClass="clsavebutton" /></td>
			</tr>
			<%}%>
			<!--  Vulnerable Subjects - End  -->


			<!-- Add Vulnerable Subjects: - Start -->
			<tr>
				<td align="left" valign="top" class='core'><table width="100%"
						border="0" align="center" cellpadding="0" cellspacing="0"
						class="tabtable">
						<tr>
							<td colspan="4" align="left" valign="top"><table
									width="100%" height="20" border="0" cellpadding="0"
									cellspacing="0" class="tableheader">
									<tr>
										<td><bean:message key="vulnSubLabel.VulnerableSubjects" />
										</td>
									</tr>
								</table></td>
						</tr>
						<tr>
							<td height='10'>&nbsp;</td>
						</tr>
						<tr align="center">
							<td colspan="3">
								<DIV
									STYLE="overflow: auto; width: 770px; height: 408px; padding: 0px; margin: 0px">
									<table width="100%" border="0" cellpadding="0" class="tabtable">
										<tr>
											<td width="15%" align="left" class="theader"><bean:message
													key="vulnSubLabel.VulnerableSubject" /></td>
											<td width="30%" align="left" class="theader"><bean:message
													key="vulnSubLabel.Count" /></td>
											<td width="10%" align="left" class="theader">&nbsp;</td>
										</tr>
										<%--
                                            if (VulnerableData.size() > 0) 
                                        { --%>
										<% String strBgColor = "#DCE5F1"; //BGCOLOR=EFEFEF  FBF7F7
                                            int count = 0;
                                         %>
										<logic:present name="VulnerableData">
											<logic:iterate id="VulnerableBean" name="VulnerableData"
												type="org.apache.struts.validator.DynaValidatorForm">
												<% 
                                           if (count%2 == 0) 
                                           strBgColor = "#D6DCE5"; 
                                           else 
                                           strBgColor="#DCE5F1"; 
                                       %>
												<tr bgcolor="<%=strBgColor%>" valign="top"
													onmouseover="className='TableItemOn'"
													onmouseout="className='TableItemOff'">
													<td align="left" nowrap class="copy"><%=VulnerableBean.get("vulnerableSubjectTypeDesc")%>
													</td>
													<td width="30%" align="left" nowrap class="copy"><bean:write
															name="VulnerableBean" property="subjectCount" /></td>
													<td align="center" nowrap class="copy">
														<%--<html:link page="/addVulerableSubjects.do?action=&vulCount=<%=VulnerableBean.getVulnerableSubjectTypeCode()%>">Remove</html:link>--%>
														<%-- String strLink = "/vulerableSubjects.do?vulCount=" + VulnerableBean.getVulnerableSubjectTypeCode();--%>
														<%if(!modeValue){
                                                String removeLink = "javascript:delete_data('D','" +VulnerableBean.get("vulnerableSubjectTypeCode") +"','" +VulnerableBean.get("updateTimeStamp") +"','" +VulnerableBean.get("subjectCount") +"')"; 
                                            %> <html:link
															href="<%=removeLink%>">
															<bean:message key="fundingSrc.Remove" />
														</html:link> <%--a href="javascript:delete_data('D','<%=VulnerableBean.get("vulnerableSubjectTypeCode")%>','<%=VulnerableBean.get("updateTimeStamp")%>','<%=VulnerableBean.get("subjectCount")%>')">
                                                    <bean:message key="fundingSrc.Remove"/>
                                                </a--%> <%}%>
													</td>
												</tr>
												<% count++;%>
											</logic:iterate>
										</logic:present>
									</table>
									<%--}--%>
								</DIV>
							</td>
						</tr>
						<tr>
							<td>&nbsp;</td>
						</tr>

					</table></td>
			</tr>
			<!-- Add Vulnerable Subjects: -End -->
			<tr>
				<td height='10'>&nbsp;</td>
			</tr>
		</table>
		<!-- New Template for cwVulnerableSubjects - End  -->
		<html:hidden property="acType" />
		<html:hidden property="protocolNumber" />
		<html:hidden property="sequenceNumber" />
		<html:hidden property="updateTimeStamp" />
		<%--<html:hidden property="count"/>--%>
	</html:form>
	<script>
          DATA_CHANGED = 'false';
          if(errValue && !errLock) {
                DATA_CHANGED = 'true';
          }
          document.vulerable.acType.value = 'I';
          LINK = "<%=request.getContextPath()%>/vulerableSubjects.do"; 
          FORM_LINK = document.vulerable;
          PAGE_NAME = "<bean:message key="vulnSubLabel.VulnerableSubjects"/>";
          function dataChanged(){
                DATA_CHANGED = 'true';
          }
          linkForward(errValue);
         </script>
	<script>
      var help = '<bean:message key="helpTextProtocol.Subjects"/>';
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
