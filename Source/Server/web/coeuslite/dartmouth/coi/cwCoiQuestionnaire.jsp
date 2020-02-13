<%@page contentType="text/html"
	import="edu.mit.coeus.utils.CoeusProperties,edu.mit.coeus.utils.CoeusPropertyKeys"%>
<%@page pageEncoding="UTF-8" import="edu.mit.coeus.bean.*"%>
<%@page errorPage="ErrorPage.jsp"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/coeus-utils.tld" prefix="coeusUtils"%>
<%@ include file="/coeuslite/mit/utils/CoeusContextPath.jsp"%>
<jsp:useBean id="person" scope="session"
	class="edu.mit.coeus.bean.PersonInfoBean" />
<jsp:useBean id="ynqList" scope="session" class="java.util.Vector" />
<bean:size id="questSize" name="ynqList" />
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
"http://www.w3.org/TR/html4/loose.dtd">

<html>
<head>
<%String path = request.getContextPath();%>

<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>COI</title>

<link href="<%=path%>/coeuslite/dartmouth/utils/css/coeus_styles.css"
	rel="stylesheet" type="text/css">
</head>
<script language="javascript">
function showMain(){
document.discCertificationForm.action="<%=path%>/addCertQuestions.do?mode=exit";
document.discCertificationForm.submit();
}
function showQuestion(link)
    {
        var winleft = (screen.width - 650) / 2;
        var winUp = (screen.height - 450) / 2;  
        var win = "scrollbars=1,resizable=1,width=790,height=450,left="+winleft+",top="+winUp
        sList = window.open('<bean:write name='ctxtPath'/>'+"/"+link, "list", win);
    }      
      
</script>
<body>
	<form name="discCertificationForm"
		action='<%=path%>/addCertQuestions.do?mode=continue' method="POST">
		<table width="100%" colspan='2' border="0" cellpadding="0"
			cellspacing="0" class="tableheader">

			<tr>
				<td style="font-size: 14px" class="theader" align="left">
					Disclosure :Certification Questions</td>
			</tr>
			<tr align="center">
				<td>
					<table width="99%" border="0" cellpadding="2" callspacing="0"
						class="tabtable">
						<tr>
							<td colspan="2" align="left">
								<table width="100%" cellspacing="0" border="0">
									<tr align="left">
										<td class='copy'><font color="red"> <logic:messagesPresent
													message="true">
													<html:messages id="msg" bundle="coi" message="true"
														property="answer">
														<bean:write name="msg" />
													</html:messages>
													<html:messages id="msg2" bundle="coi" message="true"
														property="duplicateSubmission">
														<bean:write name="msg2" />
													</html:messages>
												</logic:messagesPresent>
										</font></td>
									</tr>
								</table>
							</td>
						</tr>

						<tr>
							<td colspan="2" class="theader" align="left" height="25px"><font
								class="copy">Please answer the following questions.</font></td>
						</tr>
						<tr>
							<td>
								<%
            String strBgColor = "#DCE5F1";
            int certificateIndex=0;
            //int certificateCount=0;
           String preSelectedQuestions[] =(String[])request.getAttribute("selectedQuestions");
           String preSelectedAnswers[] =(String[])request.getAttribute("selectedAnswers");
            %> <logic:present name='ynqList' scope="session">
									<table>
										<logic:iterate id="ynqDataBean" name="ynqList"
											type="org.apache.commons.beanutils.DynaBean" indexId="ctr">
											<%
                
                if (certificateIndex%2 == 0) {
                strBgColor = "#D6DCE5"; 
                }
                else { 
                strBgColor="#DCE5F1"; 
                }
                
                String noOfAns =(String)ynqDataBean.get("noOfAnswers");
                String questionId =(String)ynqDataBean.get("questionId");
               
                String answer = (String)ynqDataBean.get("answer");                                                       
                String seqNo=(String)ynqDataBean.get("sequenceNum");
                if(seqNo == null || !seqNo.equals("")){
                seqNo ="1";
                }
                //preselected question
                String  preSelectedQuestionCode =null;  
                String preSelectedAnswerCode = null; 
                String checkedYes="";
                String checkedNo="";
                String checkedNA="";
                if((preSelectedAnswers!=null) && (preSelectedAnswers.length>0 )){
                 preSelectedAnswerCode=preSelectedAnswers[certificateIndex];
                  }
                   if(preSelectedAnswerCode!=null){
                                                    if(preSelectedAnswerCode.equals("Y") ){
                                                        checkedYes="checked";
                                                    }
                                                    
                                                    if(preSelectedAnswerCode.equals("N") ){
                                                        checkedNo="checked";
                                                    }
                                                    
                                                    if(preSelectedAnswerCode.equals("X") ){
                                                        checkedNA="checked";
                                                    }
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
												<%String objValue = ynqDataBean.get("label")==null ? "" : (String)ynqDataBean.get("label");%>
												<td width="10%" class="copybold" valign="top"><bean:message
														bundle="coi" key="label.question" />
													<%--Question--%> <%=objValue%>:</td>
												<td width="700" class="copy" valign="top"><coeusUtils:formatOutput
														name="ynqDataBean" property="questionDesc" /></b> <a
													href='javascript:showQuestion("/question.do?questionNo=<%=ynqDataBean.get("questionId")%>",  "ae");'
													method="POST"> <u> <bean:message bundle="coi"
																key="financialEntity.moreAbtQuestion" />
													</u>
												</a></td>
												<td class="copy" valign="top" width="20%"><input
													type="radio" name="<%= questionId %>" value="Y"
													<%=checkedYes%> class='copy'> <bean:message
														bundle="coi" key="financialEntity.yes" />
													<%--Yes--%> <input type="radio" name="<%= questionId %>"
													value="N" <%=checkedNo%> class='copy'> <bean:message
														bundle="coi" key="financialEntity.no" />
													<%--No--%> <% if(noOfAns!=null && Integer.parseInt(noOfAns)> 2 ){%>
													<input type="radio" name="<%= questionId %>" value="X"
													<%=checkedNA%> class='copy'> <bean:message
														bundle="coi" key="financialEntity.na" />
													<%--N/A--%> <%}%></td>
											</tr>
											<% certificateIndex++;
                        %>
										</logic:iterate>
									</table>

								</logic:present> <html:hidden property="acType" value="I" /> <html:hidden
									property="coiDisclosureNumber" value="" /> <html:hidden
									property="personId" value="" />

							</td>
						</tr>
						<tr>
							<td><table width="100%" height="20" colspan='2' border="0"
									cellpadding="0" cellspacing="0" class="table">
									<tr>
										<td align="right"><input type="submit"
											value="Save & Continue" style="width: 150px">&nbsp;&nbsp;&nbsp;</td>
										<td>&nbsp;&nbsp;&nbsp;<input type="button"
											value="Save & Exit" style="width: 150px"
											onClick="javascript:showMain();"></td>
									</tr>
								</table></td>
						</tr>

					</table>
				</td>
			</tr>
		</table>
	</form>
</body>
</html>


