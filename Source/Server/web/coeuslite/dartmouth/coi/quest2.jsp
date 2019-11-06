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
document.questionnair.action="/coiMain.do";
document.questionnair.submit();
}
function showQuestion(link)
    {
        var winleft = (screen.width - 650) / 2;
        var winUp = (screen.height - 450) / 2;  
        var win = "scrollbars=1,resizable=1,width=790,height=450,left="+winleft+",top="+winUp
        sList = window.open('<bean:write name='ctxtPath'/>'+"/"+link, "list", win);
    }      
      
</script>

<html:javascript formName="finEntity" />
<body>
	<html:form action="/addCertQuestions.do" method="POST">
		<table width="100%" colspan='2' border="0" cellpadding="0"
			cellspacing="0" class="tableheader">

			<tr>
				<td style="font-size: 14px" class="theaderBlue" align="left">
					Annual Disclosure :Certification Questions</td>
			</tr>
			<tr align="center">
				<td>
					<table width="99%" border="0" cellpadding="2" callspacing="0"
						class="tabtable">

						<tr>
							<td colspan="2" class="theader" align="left"><font
								class="copy">Please answer the following questions.</font></td>
						</tr>
						<tr>
							<td><logic:present name='ynqList' scope="session">
									<%
                                                    String strBgColor = "#DCE5F1";
                                                    int certificateIndex=0;
                                                    %>
									<table width="99%" border="0" cellpadding="2" callspacing="0"
										class="tabtable">

										<logic:iterate id="ynqDataBean" name="ynqList"
											type="org.apache.commons.beanutils.DynaBean" indexId="ctr">

											<%
                                                            
                                                            if (certificateIndex%2 == 0) {
                                                            strBgColor = "#D6DCE5"; 
                                                            }
                                                            else { 
                                                            strBgColor="#DCE5F1"; 
                                                            }
                                                            
                                                            String totalAnswers =(String)ynqDataBean.get("noOfAnswers");
                                                            String questionId =(String)ynqDataBean.get("questionId");
                                                            String answer = (String)ynqDataBean.get("answer");                                                       
                                                            String seqNo=(String)ynqDataBean.get("sequenceNum");
                                                            if(seqNo == null || !seqNo.equals("")){
                                                            seqNo ="1";
                                                            }
                                                            //preselected question
                                                            String  preSelectedQuestionCode =null;                                                          
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
												<td width="130" height="25" class="copy" align="left">
													<div>
														<font color="red">*</font> <input type="radio"
															name="<%= questionId %>"
															<%= (answer!=null && answer.equals("Y"))?"checked":""  %>
															value="Y">
														<bean:message bundle="coi" key="financialEntity.yes" />
														<%--Yes--%>
														<input type="radio" name="<%= questionId %>"
															<%= (answer!=null && answer.equals("N"))?"checked":""  %>
															value="N">
														<bean:message bundle="coi" key="financialEntity.no" />
														<%--No--%>
														<% if(totalAnswers!=null && Integer.parseInt(totalAnswers)>2 ) {%>
														<input type="radio" name="<%= questionId %>"
															<%= (answer!=null && answer.equals("X"))?"checked":""  %>
															value="X">
														<bean:message bundle="coi" key="financialEntity.na" />
														<%--N/A--%>

														<%} 
                                             certificateIndex++;
                                           %>

													</div>
												</td>
											</tr>
										</logic:iterate>
									</table>
								</logic:present></td>
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
	</html:form>
</body>
</html>


