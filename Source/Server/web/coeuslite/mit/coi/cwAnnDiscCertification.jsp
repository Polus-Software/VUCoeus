<%@ page
	import="org.apache.struts.validator.DynaValidatorForm,
                  edu.mit.coeuslite.utils.ComboBoxBean,
                  org.apache.struts.action.Action,
                  java.net.URLEncoder,
                  java.util.HashMap,
                  java.util.Set,
                  java.util.Iterator;"%>
<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/coeus-utils.tld" prefix="coeusUtils"%>
<%@ include file="/coeuslite/mit/utils/CoeusContextPath.jsp"%>
<jsp:useBean id="ynqList" scope="session" class="java.util.Vector" />
<bean:size id="ynqListSize" name="ynqList" />
<jsp:useBean id="person" scope="session"
	class="edu.mit.coeus.bean.PersonInfoBean" />

<html:html>
<head>
<title>Coeus Web</title>
<html:base />
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">

</head>
<title>JSP Page</title>
<script language='javascript'>

 function showQuestion(link)
 {
    var winleft = (screen.width - 650) / 2;
    var winUp = (screen.height - 450) / 2;  
    var win = "scrollbars=1,resizable=1,width=790,height=450,left="+winleft+",top="+winUp
    sList = window.open('<bean:write name='ctxtPath'/>'+"/"+link, "list", win);
 }

 function saveAndProceed() {
    document.coiDisclosure.submit();
 }
</script>

</head>
<body>


	<html:form action="/annualDisclosure" method="post">

		<table width="980" height="100%" border="0" cellpadding="0"
			cellspacing="0" class="table">

			<tr>
				<td height="20" align="left" valign="top" class="theader"><bean:message
						bundle="coi" key="label.certificationQuesAnnDiscl" /> <%=person.getFullName()%>
				</td>
			</tr>

			<%--<tr>
        <td  height="20" class="copy">&nbsp;&nbsp;
            <bean:message bundle="coi" key="label.certificationQuesAnnDisclInfo"/>
            Click <a href="javascript:saveAndProceed()">'Save And Proceed'</a>
        </td>
    </tr>--%>
			<tr>
				<td>&nbsp;&nbsp;<bean:message bundle="coi"
						key="coiAnnualDisclosure.questionHeader" /></td>
			</tr>
			<!--Certification -Start -->

			<tr>
				<td align="center" valign="top"><br> <%--<table width="98%"  border="0" align="center" cellpadding="0" cellspacing="0" class="tabtable">
                        <tr>
                          <td align="left" valign="top"><table width="100%" height="20"  border="0" cellpadding="0" cellspacing="0" class="tableheader">
                              <tr>
                                <td> <bean:message bundle="coi" key="financialEntity.subheaderCertification"/>  </td>
                              </tr>
                          </table></td>
                        </tr>
                     
                        <tr align="center" class='copy'>
                                <td colspan="2">--%> <!--<DIV STYLE="overflow: auto; width: 900px; height: 165; padding:0px; margin: 0px">-->

					<table width="98%" class="tabtable" border="0" cellpadding="2"
						cellspacing="0">
						<logic:present name="ynqList" scope="session">
							<tr class='copy'>
								<td width="70%" wrap align="center" class="theader"><bean:message
										bundle="coi" key="label.question" /></td>
								<td width="15%" align="center" class="theader"><bean:message
										bundle="coi" key="label.answer" /></td>
							</tr>
							<logic:notEqual name="ynqListSize" value="0">
								<% 
                                                //String preSelectedQuestions[] =(String[])session.getAttribute("selectedQuestions");
                                                //String preSelectedAnswers[] =(String[])session.getAttribute("selectedAnswers");
                                                String strBgColor = "#DCE5F1";
                                                int certificateIndex=0;                                                
                                                for(int certificateCount = 0 ; certificateCount < ynqList.size() ; certificateCount++){
                                                        DynaValidatorForm dynaValidatorForm =
                                                                (DynaValidatorForm)ynqList.elementAt(certificateCount) ;
                                                        String questionId =(String)dynaValidatorForm.get("questionId");
                                                        String noOfAns =(String)dynaValidatorForm.get("noOfAnswers");
                                                        String answer = (String)dynaValidatorForm.get("answer");
                                                        
                                                        String certCode = (String)dynaValidatorForm.get("questionId");                                                        
                                                        String correspEntQuestLabel =(String)dynaValidatorForm.get("correspEntQuestLabel");
                                                        String questionLabel = (String)dynaValidatorForm.get("label");
                                                        String checkedYes="";
                                                        String checkedNo="";
                                                        String checkedNA="";
                                                        //String preSelectedQuestionCode =null;
                                                        //String preSelectedAnswerCode = null;
                                                        
                                                       if(answer != null){
                                                            if(answer.equals("Y") ){
                                                                    checkedYes="checked";
                                                             }

                                                             else if(answer.equals("N") ){
                                                                    checkedNo="checked";
                                                             }

                                                             else if(answer.equals("X") ){
                                                                    checkedNA="checked";
                                                             }
                                                    }
                                                        
                                                     if (certificateIndex%2 == 0) 
                                                        {
                                                           strBgColor = "#D6DCE5"; 
                                                         }
                                                           else { 
                                                                strBgColor="#DCE5F1"; 
                                                             }    
                                            %>

								<INPUT type='hidden' name='hdnQuestionId'
									value='<%=dynaValidatorForm.get("questionId")%>'>
								<INPUT type='hidden' name='hdnQuestionDesc'
									value='<%=dynaValidatorForm.get("description")%>'>
								<INPUT type='hidden' name='hdnSeqNo' value='1'>
								<tr bgcolor="<%=strBgColor%>" class="rowLine"
									onmouseover="className='rowHover rowLine'"
									onmouseout="className='rowLine'">
									<td width="70%" class='copy'><logic:present
											name="certQuestionErrors">
											<%
                                                                            
                                                                            String errorCertCode = null;
                                                                            Object errorObject = null;
                                                                            HashMap entitiesWithYes = null;
                                                                            String errorCode = null;
                                                                            String entityNumber = null;
                                                                            String entityName = null;

                                                                            HashMap certQuestionErrors = (HashMap)session.getAttribute("certQuestionErrors");                                                       
                                                                            Iterator keysIterator = certQuestionErrors.keySet().iterator();

                                                                            while(keysIterator.hasNext()){
                                                                                entitiesWithYes = null;
                                                                                errorCode = null;	         	
                                                                                errorCertCode = (String)keysIterator.next();
                                                                                if(errorCertCode != null &&  errorCertCode.equals(certCode)){
                                                                                        errorObject = certQuestionErrors.get(errorCertCode);
                                                                                        if(errorObject.getClass().getName().equals("java.util.HashMap")){
                                                                                                entitiesWithYes = (HashMap)certQuestionErrors.get(errorCertCode);

                                                                                        }
                                                                                        else{
                                                                                                errorCode = certQuestionErrors.get(errorCertCode).toString();

                                                                                        }
                                                                                }                                                            
                                                                                //If error is because user answered No, and has one or more entities
                                                                                //with answer Yes, get the list of entities with Yes answer.
                                                                                if(entitiesWithYes != null){
                                                                                        if(entitiesWithYes.size() == 1){

                                                                                        %>
											<font color="red"> <bean:message
													key="error.addCOIDisclosure.entYesAnsNoForOne" bundle="coi" />
												<%=correspEntQuestLabel%> <bean:message
													key="error.addCOIDisclosure.entYesAnsNo2ForOne"
													bundle="coi" />
											</font>
											<%
                                                                                 }
                                                                            else{
                                                                                    
                                                                             %>
											<font color="red"> <bean:message
													key="error.addCOIDisclosure.entYesAnsNoForTwo" bundle="coi" />
												<%=correspEntQuestLabel%> <bean:message
													key="error.addCOIDisclosure.entYesAnsNo2ForTwo"
													bundle="coi" />
											</font>
											<%
                                                                            }
                                                                        Iterator entities = entitiesWithYes.keySet().iterator();
                                                                        for(int cnt = 0; cnt<entitiesWithYes.size(); cnt++){
                                                                                entityNumber = (String)entities.next();
                                                                                entityName = (String)entitiesWithYes.get(entityNumber);
                                                                        %>
											<a
												href="<bean:write name='ctxtPath'/>/editFinEnt.do?actionFrom=annDiscCert&entityNumber=<%=entityNumber%>">
												<%=entityName%></a>
											<%
                                                                                if(cnt<(entitiesWithYes.size()-1)){
                                                                                        out.print(",");
                                                                                }
                                                                         }//End For

                                                                        if(entitiesWithYes.size() == 1){
                                                                            %>
											<!--<font face="Verdana, Arial, Helvetica, sans-serif" size="1" color="red">-->
											<font color="red"> <bean:message
													key="error.addCOIDisclosure.entYesAnsNo3ForOne"
													bundle="coi" /> <%=correspEntQuestLabel%> <bean:message
													key="error.addCOIDisclosure.entYesAnsNo4ForOne"
													bundle="coi" />
											</font>
											<%
                                                                        }
                                                                        else{
                                                                             %>

											<font color="red"> <bean:message
													key="error.addCOIDisclosure.entYesAnsNo3ForTwo"
													bundle="coi" /> <%=correspEntQuestLabel%> <bean:message
													key="error.addCOIDisclosure.entYesAnsNo4ForTwo"
													bundle="coi" />
											</font>
											<%
                                                                         }//End else		

                                                                        }//End   if(entitiesWithYes != null)

                                                                        //User has answered Yes but has no financial entities
                                                                        else if(errorCode != null && errorCode.equals("-1")){

                                                                        %>
											<font color="red"> <bean:message
													key="error.annDiscCertification.noFinEntAnsReq"
													bundle="coi" /> <a
												href="<bean:write name='ctxtPath'/>/getAddFinEnt.do?actionFrom=annDiscCert">Add
													a new Financial Entity</a> <bean:message
													key="error.annDiscCertification.noFinEntAnsReq2"
													bundle="coi" />
											</font>
											<%
                                                                                }
                                                                                //Error code -2: User answered Yes, but no fin ent with answer Yes to this question.
                                                                        else if(errorCode != null && errorCode.equals("-2")){
                                                                                        
                                                                        %>

											<font color="red"> <bean:message
													key="error.addCOIDisclosure.entNoAnsYes" bundle="coi" /> <%=correspEntQuestLabel%>
												<bean:message key="error.addCOIDisclosure.entNoAnsYes2"
													bundle="coi" /> <a
												href="<bean:write name="ctxtPath"/>/getAnnDiscPendingFEs.do?actionFrom=annDiscCert">
													<bean:message key="error.addCOIDisclosure.entNoAnsYes3"
														bundle="coi" />
											</a>
											</font>
											<bean:message key="error.addCOIDisclsoure.entNoAnsYes4"
												bundle="coi" />
											<%
                                                                                }
                                                                        }
                                                                        %>
										</logic:present>

										<div class='copy' align="justify">
											<b><bean:message bundle="coi" key="label.question" />
												<%--Question--%> <%=dynaValidatorForm.get("label")%></b>
											<%=dynaValidatorForm.get("description")%>
											<a
												href='javascript:showQuestion("/question.do?questionNo=<%=dynaValidatorForm.get("questionId")%>",  "ae");'
												method="POST"> <u> <bean:message bundle="coi"
														key="financialEntity.moreAbtQuestion" />
													<%--More about this question--%>
											</u>
											</a>
										</div></td>

									<td class="copy" valign="top" width="15%">
										<table border="0" width="100%" cellpadding="0" cellspacing="0"
											border="0" class="copy">
											<tr align="center">
												<td style="border-style: solid none; border-width: 0px 0;">
													<bean:message bundle="coi" key="financialEntity.yes" /><br>
													<input type="radio" name="<%= questionId %>" value="Y"
													<%=checkedYes%> class='copy'>
												</td>
												<td style="border-style: solid none; border-width: 0px 0;">
													<bean:message bundle="coi" key="financialEntity.no" /><br>
													<input type="radio" name="<%= certCode %>" value="N"
													<%=checkedNo%> class='copy'>
												</td>
												<% if(noOfAns!=null && Integer.parseInt(noOfAns)> 2 ){%>
												<td style="border-style: solid none; border-width: 0px 0;">
													<bean:message bundle="coi" key="financialEntity.na" /><br>
													<input type="radio" name="<%= certCode %>" value="X"
													<%=checkedNA%> class='copy'>
												</td>
												<%}%>
											</tr>
										</table>
									</td>
								</tr>
								<% }certificateIndex++;//End For%>
							</logic:notEqual>

						</logic:present>
						<logic:equal name="ynqListSize" value="0">
							<tr>
								<td colspan='3' height="23" align=center class='copy'>
									<div>
										<bean:message bundle="coi"
											key="financialEntity.noCertQuestionFound" />
										<%--No Certification Questions Found--%>
									</div>
								</td>
							</tr>
						</logic:equal>

					</table>
					<p align="left">
						&nbsp;&nbsp;
						<html:submit property="save" value=" Save And Proceed " />
					</p> <!--</div>--> <%--</td>                            
                          </tr>
                           <tr class='table'>
                              <td colspan="3" nowrap class="savebutton" >
                                  <html:submit property="save" value="Save"  styleClass="clsavebutton" />
                              </td>
                           </tr>


                        
                          
    </table>--%></td>
			</tr>
			<!--Certification -End -->





		</table>

	</html:form>

</body>


</html:html>

