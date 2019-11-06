<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%@ page
	import="org.apache.struts.validator.DynaValidatorForm,
         java.util.Vector,edu.mit.coeuslite.utils.ComboBoxBean,org.apache.struts.taglib.html.JavascriptValidatorTag"%>
<%@ page import="java.text.SimpleDateFormat, java.util.Date"%>

<%@ page contentType="text/html;charset=UTF-8" language="java"%>

<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/coeus-utils.tld" prefix="coeusUtils"%>
<%@ include file="/coeuslite/mit/utils/CoeusContextPath.jsp"%>
<jsp:useBean id="ynqList" scope="session" class="java.util.Vector" />
<jsp:useBean id="questionData" scope="session" class="java.util.Vector" />
<jsp:useBean id="orgtypelist" scope="session" class="java.util.Vector" />
<jsp:useBean id="finentityreltype" scope="session"
	class="java.util.Vector" />
<jsp:useBean id="person" scope="session"
	class="edu.mit.coeus.bean.PersonInfoBean" />
<jsp:useBean id="personFullName" scope="request"
	class="java.lang.String" />
<bean:size id="ynqListSize" name="ynqList" />


<%
//create a vector of comboboxbean instances to show Share owner ship options
            Vector vecShareOwnership = new Vector();
            ComboBoxBean orgType = new ComboBoxBean();
            orgType.setCode("");
            orgType.setDescription("");
            vecShareOwnership.addElement(orgType);
            orgType = new ComboBoxBean();
            orgType.setCode("P");
            orgType.setDescription("Public");
            vecShareOwnership.addElement(orgType);
            orgType = new ComboBoxBean();
            orgType.setCode("V");
            orgType.setDescription("Private");
            vecShareOwnership.addElement(orgType);
            pageContext.setAttribute("optionsShareOwnership", vecShareOwnership);
%>

<html:html>
<head>
<title>Coeus Web</title>
<html:base />
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">

<link
	href="<bean:write name='ctxtPath'/>/coeuslite/mit/utils/css/coeus_styles.css"
	rel="stylesheet" type="text/css">
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
<style type="text/css">
<!--
body {
	margin-left: 3px;
}
-->
</style>
</head>
<script>
        function validateForm(form) {
            if(document.finEntity.entityName.value.length > 60) {
                alert('<bean:message bundle="coi" key="error.relEntityLength"/>');  
                document.finEntity.entityName.focus();
                return false;
            }
            var value=document.finEntity.entityRelTypeCode.value;
            if(value==4 || value==5){
                var strDesc=document.finEntity.relationShipDesc.value;        
                var desc=trim(strDesc);
                if(desc.length==0 || desc==null){
                    alert('<bean:message bundle="coi" key="error.relationShipDescMandatory"/>');  
                    document.finEntity.relationShipDesc.focus();
                    return false;
                }  
  
            }
            strDesc =  document.finEntity.orgRelnDesc.value;
            strDesc = trim(strDesc);
            if(strDesc == null || strDesc.length == 0) {
                alert('<bean:message bundle="coi" key="error.orgRelnDescMandatory"/>');  
                document.finEntity.orgRelnDesc.focus();
                return false;
            }
            
            if(document.finEntity.relationShipDesc.value.length > 2000) {
                alert('<bean:message bundle="coi" key="error.DescRelation.length"/>');
                document.finEntity.relationShipDesc.focus();
                return false;
            }
            if(document.finEntity.orgRelnDesc.value.length > 2000) {
                alert('<bean:message bundle="coi" key="error.EntityRelDesc.length"/>');
                document.finEntity.orgRelnDesc.focus();
                return false;
            }
            
            if(validateFinEntity(form) == true )
            {
                // Hide the code in first div tag
                document.getElementById('finEntityFormDiv').style.display = 'none';
                // Display code in second div tag
                document.getElementById('messageDiv').style.display = 'block';   
            
           
            }
            else
            {
                return validateFinEntity(form);
            }
    
    
            //  return validateFinEntity(form);
        }
    
        function showQuestion(link)
        {
            var winleft = (screen.width - 650) / 2;
            var winUp = (screen.height - 450) / 2;  
            var win = "scrollbars=1,resizable=1,width=790,height=450,left="+winleft+",top="+winUp
            sList = window.open('<bean:write name='ctxtPath'/>'+"/"+link, "list", win);
        }
        /*    function chkRelnDescription(){
    var value=document.finEntity.entityRelTypeCode.value;
    if(value==4 || value==5){
        var desc=document.finEntity.relationShipDesc.value;
        if(desc.length==0 || desc==null){
            alert("enter");  
            document.finEntity.relationShipDesc.focus();
            return false;
        }            
      }
      return true;
    }    */ 
        function trim(stringToTrim) {
            return stringToTrim.replace(/^\s+|\s+$/g,"");
        }

    </script>

<html:javascript formName="finEntity" dynamicJavascript="true"
	staticJavascript="true" />
<body>
	<html:form action="/addFinEntity.do" method="POST"
		onsubmit=" return validateForm(this)">

		<div id="finEntityFormDiv">
			<table width="980" border="0" cellpadding="0" cellspacing="0"
				class="table">
				<tr>
					<td height="10" align="left" valign="top" class="theader"><bean:message
							bundle="coi" key="financialEntity.headerAddEditFinEntDetails" />
						<%=person.getFullName()%></td>
				</tr>
				<tr>
					<td class="copybold">&nbsp;&nbsp; <font color="red">*</font> <bean:message
							bundle="coi" key="label.indicatesReqFlds" />
						<%--Indicates Required Fields--%>
					</td>
				</tr>

				<tr class='copy' align="left">
					<font color="red"> <logic:messagesPresent message="true">
							<html:messages id="message" message="true"
								property="orgRelnDescLength" bundle="coi">
								<li><bean:write name="message" /></li>
							</html:messages>
							<html:messages id="message" message="true"
								property="relationShipDesc" bundle="coi">
								<li><bean:write name="message" /></li>
							</html:messages>
							<html:messages id="message" message="true"
								property="relationShipDescMandatory" bundle="coi">
								<li><bean:write name="message" /></li>
							</html:messages>
							<html:messages id="message" message="true"
								property="entityNameLength" bundle="coi">
								<li><bean:write name="message" /></li>
							</html:messages>
							<html:messages id="message" message="true" property="orgRelnDesc"
								bundle="coi">
								<li><bean:write name="message" /></li>
							</html:messages>
							<html:messages id="message" message="true"
								property="org.apache.struts.action.GLOBAL_MESSAGE">
								<li><bean:write name="message" /></li>
							</html:messages>
						</logic:messagesPresent>
					</font>
				</tr>
				<!-- EntityDetails - Start  -->
				<tr>
					<td align="left" valign="top"><table width="99%" border="0"
							align="center" cellpadding="0" cellspacing="0" class="tabtable">
							<tr>
								<td colspan="4" align="left" valign="top"><table
										width="100%" height="20" border="0" cellpadding="0"
										cellspacing="0" class="tableheader">
										<tr>
											<td><bean:message bundle="coi"
													key="financialEntity.subheaderEntityDet" /></td>
										</tr>
									</table></td>
							</tr>

							<tr>
								<td width="20%" align="right" valign="top" class="copy">
									<table width="100%" border="0">
										<tr>

											<td width="40%" align="left" valign="top" class="copy">
												<table width="100%" border="0">
													<tr>
														<td align="left" nowrap class="copybold" width="135">
															<font color="red">*</font> <bean:message bundle="coi"
																key="label.entityName" /> :
														</td>
														<td align="left" class="copy"><html:text
																property="entityName" styleClass="textbox-long" /></td>
													</tr>
													<tr>
														<td nowrap align="left" class="copybold"><font
															color="red"></font> &nbsp;&nbsp; <bean:message
																bundle="coi" key="label.orgTypeCode" /> :</td>
														<td align="left" class="copy"><html:select
																name="finEntity" styleClass="textbox-long"
																property="entityTypeCode" style="width:400px">
																<%--html:options collection="orgtypelist" property="code" labelProperty="description"/--%>
																<%
                                                        Vector vec = (Vector)orgtypelist;
                                                            if(vec != null) {
                                                                ComboBoxBean bean;
                                                                DynaValidatorForm finEntForm = (DynaValidatorForm)request.getAttribute("finEntity");
                                                                String code = (String)finEntForm.get("entityTypeCode");
                                                            for(int index = 0; index <vec.size(); index++) {
                                                                bean = (ComboBoxBean)orgtypelist.get(index);
                                                                if(code != null && code.equals(bean.getCode())) {
                                                                    out.print("<option value='"+bean.getCode()+"' title='"+bean.getDescription()+"' selected>"+bean.getDescription()+"</option>");
                                                                }else {
                                                                    out.print("<option value='"+bean.getCode()+"' title='"+bean.getDescription()+"'>"+bean.getDescription()+"</option>");
                                                                }
                                                            }
                                                            }
                                                        %>
															</html:select></td>
													</tr>
												</table>
											</td>

											<td align="center" valign="top" nowrap class="copy"
												colspan='3'>
												<table cellpadding="0" border="0">
													<%-- <tr>                                                      
                                              <td align="left"  width='175' nowrap class="copybold" >
                                                 <bean:message bundle="coi" key="label.shareOwnerShip"/>   :
                                                  <td align="left" class="copy">
                                                        <html:select  name="finEntity"  styleClass="textbox-long" property="shareOwnerShip">
                                                            <html:options collection="optionsShareOwnership" property="code" labelProperty="description"/>
                                                       </html:select> 
                                                   </td>
                                              </td>
                                            </tr>   --%>
													<tr>
														<td class="copybold"><bean:message bundle="coi"
																key="label.createDate" /> :</td>
														<td class="copy" nowrap>
															<%--bean:write name="finEntity" property="updtimestamp"/--%>
															<%
            try {
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                Date date = simpleDateFormat.parse((String) request.getAttribute("createTimestamp"));
                SimpleDateFormat requiredDateFormat = new SimpleDateFormat("MM-dd-yyyy  hh:mm a");
                String output = requiredDateFormat.format(date);
                out.print(output);
            } catch (Exception exception) {
                //Do Nothing Here.
            }
                                                    %> <%if(request.getAttribute("createUser") != null){%>
															<bean:write name="createUser" /> <%}%>
														</td>
													</tr>
													<tr>
														<%--<td colspan="2" nowrap class="copy"><img src="<bean:write name='ctxtPath'/>/coeusliteimages/spacer.gif" width="5" height="25"></td>--%>
														<td class="copybold"><bean:message bundle="coi"
																key="label.lastUpdated" /> :</td>
														<td class="copy" nowrap>
															<%--bean:write name="finEntity" property="updtimestamp"/--%>
															<coeusUtils:formatDate name="finEntity"
																formatString="MM-dd-yyyy  hh:mm a"
																property="updtimestamp" /> <bean:write name="finEntity"
																property="upduser" />
														</td>
													</tr>

												</table>
											</td>
										</tr>
									</table>
								</td>
							</tr>

							<tr>
								<td>&nbsp;</td>
							</tr>

						</table></td>
				</tr>
				<!--Certification -Start -->
				<tr>
					<td height="51" align="left" valign="top"><br>
						<table width="99%" border="0" align="center" cellpadding="3"
							cellspacing="0" class="tabtable">
							<tr>
								<!--<td align="left" valign="top"><table width="100%" height="20"  border="0" cellpadding="0" cellspacing="0" class="tableheader">
                <tr>-->
								<td colspan="3" class="tableheader"><bean:message
										bundle="coi" key="financialEntity.subheaderCertification" /></td>
								<!--</tr>
                </table></td>-->
							</tr>

							<% //Look for preselected information, if available please show them
            String[] preSelectedQuestions = (String[]) request.getAttribute("selectedQuestions");
            String[] preSelectedAnswers = (String[]) request.getAttribute("selectedAnswers");
            String strBgColor = "#DCE5F1";
            int certificateIndex = 0;
            %>

							<logic:present name="ynqList" scope="session">
								<logic:iterate id="ynqDataBean" name="ynqList"
									type="org.apache.commons.beanutils.DynaBean" indexId="ctr">

									<%
            if (certificateIndex % 2 == 0) {
                strBgColor = "#D6DCE5";
            } else {
                strBgColor = "#DCE5F1";
            }

            String totalAnswers = (String) ynqDataBean.get("noOfAnswers");
            String questionId = (String) ynqDataBean.get("questionId");
            String answer = (String) ynqDataBean.get("answer");
            String seqNo = (String) ynqDataBean.get("sequenceNum");
            if (seqNo == null || !seqNo.equals("")) {
                seqNo = "1";
            }
            //preselected question
            String preSelectedQuestionCode = null;

            if ((preSelectedQuestions != null) && (preSelectedQuestions.length > 0)) {
                preSelectedQuestionCode = preSelectedQuestions[certificateIndex];
            }
            //preselected answer
            String preSelectedAnswerCode = null;
            if ((preSelectedAnswers != null) && (preSelectedAnswers.length > 0)) {
                preSelectedAnswerCode = preSelectedAnswers[certificateIndex];
            }

            if (preSelectedQuestionCode != null) {
                questionId = preSelectedQuestionCode;
            }
            if (preSelectedAnswerCode != null) {
                answer = preSelectedAnswerCode;
            }

                    %>
									<tr bgcolor="<%=strBgColor%>" class="rowLine"
										onmouseover="className='rowHover rowLine'"
										onmouseout="className='rowLine'">


										<INPUT type='hidden' name='hdnQuestionId'
											value='<%=ynqDataBean.get("questionId")%>'>
										<INPUT type='hidden' name='hdnQuestionDesc'
											value='<%=ynqDataBean.get("questionDesc")%>'>
										<INPUT type='hidden' name='hdnSeqNo' value='<%=seqNo%>'>
										<%String objValue = ynqDataBean.get("label") == null ? "" : (String) ynqDataBean.get("label");%>
										<td width="10%" class="copybold" valign="top"><bean:message
												bundle="coi" key="label.question" />
											<%--Question--%> <%=objValue%>:</td>
										<td width="80%" class="copy" valign="top"><coeusUtils:formatOutput
												name="ynqDataBean" property="questionDesc" /></b> <a
											href='javascript:showQuestion("/question.do?questionNo=<%=ynqDataBean.get("questionId")%>",  "ae");'
											method="POST"> <u> <bean:message bundle="coi"
														key="financialEntity.moreAbtQuestion" />
											</u>
										</a></td>
										<td width="10%" height="25" class="copy" align="left" nowrap>
											<table border="0" width="100%" cellpadding="0"
												cellspacing="0" border="0" class="copy">
												<tr align="center">
													<td style="border-style: solid none; border-width: 0px 0;">
														<font color="red">*</font>
													</td>
													<td style="border-style: solid none; border-width: 0px 0;">
														<bean:message bundle="coi" key="financialEntity.yes" /><br>
														<input type="radio" name="<%= questionId%>"
														<%= (answer != null && answer.equals("Y")) ? "checked" : ""%>
														value="Y">
													</td>
													<td style="border-style: solid none; border-width: 0px 0;">
														<bean:message bundle="coi" key="financialEntity.no" /><br>
														<input type="radio" name="<%= questionId%>"
														<%= (answer != null && answer.equals("N")) ? "checked" : ""%>
														value="N">
													</td>
													<% if (totalAnswers != null && Integer.parseInt(totalAnswers) > 2) {%>
													<td style="border-style: solid none; border-width: 0px 0;">
														<bean:message bundle="coi" key="financialEntity.na" /><br>
														<input type="radio" name="<%= questionId%>"
														<%= (answer != null && answer.equals("X")) ? "checked" : ""%>
														value="X">
													</td>
													<%}
                                certificateIndex++;
                                %>
												</tr>
											</table>
										</td>
									</tr>
								</logic:iterate>
							</logic:present>
							<logic:equal name="ynqListSize" value="0">
								<tr>
									<td colspan='3' height="23" align=center class='copy'>
										<div>
											<bean:message bundle="coi"
												key="financialEntity.noCertQuestionFound" />
										</div>
									</td>
								</tr>
							</logic:equal>


						</table></td>
				</tr>
				<!-- RelationShip - Start -->

				<tr>
					<td align="left" valign="top"><br>
						<table width="99%" border="0" align="center" cellpadding="0"
							cellspacing="0" class="tabtable">
							<tr>
								<td colspan="4" align="left" valign="top">
									<table width="100%" height="20" border="0" cellpadding="0"
										cellspacing="0" class="tableheader">
										<tr>
											<td><bean:message bundle="coi"
													key="financialEntity.subheaderRelEntity" /></td>
										</tr>
									</table>
								</td>
							</tr>
							<tr>
								<td>
									<table width="100%" border="0">
										<tr>
											<td align="left" nowrap class="copybold" width='7%'><font
												color="red"> * </font> <bean:message bundle="coi"
													key="label.entityRelType" /> :</td>
										</tr>
										<tr>
											<td width="100%" align="left" class="copy"><html:select
													name="finEntity" style="width:80%"
													property="entityRelTypeCode">
													<html:options collection="finentityreltype" property="code"
														labelProperty="description" />
												</html:select></td>
										</tr>
										<tr>
											<td align="left" nowrap class="copybold" valign="top"
												width='5%'><bean:message bundle="coi"
													key="label.descRelationship" /> :</td>
										</tr>
										<tr>
											<td align="left" class="copy" width='100%'><html:textarea
													property="relationShipDesc" cols="150" rows="5"
													styleClass="copy" /></td>
										</tr>
									</table>
								</td>
							</tr>
						</table></td>
				</tr>

				<!-- RelationShip -End -->
				<!--Financial Entity Relation starts-->
				<tr>
					<td><br>
						<table width="99%" border="0" align="center" cellpadding="0"
							cellspacing="0" class="tabtable">
							<tr>
								<td colspan="4" align="left" valign="top">
									<table width="100%" height="20" border="0" cellpadding="0"
										cellspacing="0" class="tableheader">
										<tr>
											<td><bean:message bundle="coi"
													key="financialEntity.subheaderRelResearch" /></td>
										</tr>
									</table>
								</td>
							</tr>
							<tr>
								<td>
									<table width="100%" border="0">
										<tr>
											<td align="left" nowrap class="copybold" width='7%'><font
												color="red">*</font> <bean:message bundle="coi"
													key="label.entityRelResearch" /> :
										<tr>
											<td align="left" class="copy" width='100%'><html:textarea
													name="finEntity" property="orgRelnDesc" cols="150" rows="5"
													styleClass="copy" /></td>
										</tr>
									</table>
								</td>
							</tr>
							<tr>
								<td>&nbsp;</td>
							</tr>
						</table></td>
				</tr>
				<!--Financial Entity Relation Ends -->

				<tr>
					<td colspan="3" nowrap class="savebutton" align="left"
						style='padding-left: 6px;' width="100%">
						<%--   <html:button  property="save" value="Save"  styleClass="clsavebutton" onclick="javascript:chkRelnDescription();" />--%>
						<html:submit property="save" value="Save and Submit" />
					</td>
				</tr>
				<!--Certification -End -->

				<html:hidden property="acType" />
				<html:hidden property="entityNumber" />
				<html:hidden property="sequenceNum" />
				<html:hidden property="actionFrom" />
				<input type="hidden" name="return"
					value="<%=request.getParameter("return")%>">

			</table>
		</div>
		<div id='messageDiv' style='display: none;'>
			<table width="100%" height="100%" align="center" border="0"
				cellpadding="3" cellspacing="0" class="tabtable">
				<tr>
					<td align='center' class='copyred'><br>
						&nbsp;&nbsp;&nbsp; <bean:message bundle="coi"
							key="coiMessage.pleaseWait" /></td>
				</tr>
			</table>
		</div>

	</html:form>
</body>
</html:html>