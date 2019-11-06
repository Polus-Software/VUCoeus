<%-- 
    Document   : screeningQuestions
    Created on : May 8, 2010, 11:07:38 AM
    Author     : Mr
--%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
    "http://www.w3.org/TR/html4/loose.dtd">
<%@ page
	import="edu.mit.coeuslite.utils.bean.SubHeaderBean,edu.mit.coeuslite.coiv2.beans.CoiQuestionAnswerBean,java.util.Vector;"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>JSP Page</title>
<%  String path = request.getContextPath();
            String projectType = null;
            if(session.getAttribute("projectType")!=null){
                projectType = (String)session.getAttribute("projectType");
           }
%>
<script language="javaScript">

            function saveAndcontinue()
            { 
             <%if( projectType!=null && projectType.equalsIgnoreCase("Travel")){%>
                document.forms[0].action= '<%=path%>'+"/attachments.do?&param6=throughShowAllDiscl";
                document.forms[0].submit();     
             <%}else{%>
                document.forms[0].action= '<%=path%>'+"/getProjectsByFinancialEntitiesView.do";
                document.forms[0].submit(); 
             <%}%>
            }
            function continueWithoutQnr(){        
             <%if( projectType!=null && projectType.equalsIgnoreCase("Travel")){%>
                document.forms[0].action= '<%=path%>'+"/attachments.do?&param6=throughShowAllDiscl";
                document.forms[0].submit();     
             <%}else{%>
                document.forms[0].action= '<%=path%>'+"/getProjectsByFinancialEntitiesView.do"; 
                document.forms[0].submit(); 
             <%}%> 
             }
             
        </script>
</head>
<body>
	<html:form action="/updateFinancialEntityByProjects.do">
		<%-- <logic:present name="bodySubHeaderVectorCoiv2">
             <logic:present name="apprvdDisclosureBean">
                      <jsp:useBean id="bodySubHeaderVectorCoiv2" scope="session" class="java.util.Vector" />
                  <table class="tablemain" width="100%" border="0" cellpadding="0" cellspacing="0">
                    <tr style="background-image: url(/coeus44server/coeusliteimages/tile_menu.gif);border: 0pt none;" >
                    <td  style="background-image: url(/coeus44server/coeusliteimages/body_background.gif);border: 0pt none;height: 22px;text-align: right;" >

               <%
                                String subHeaderNames[]=new String[bodySubHeaderVectorCoiv2.size()];
                                String actionPaths[]=new String[bodySubHeaderVectorCoiv2.size()];
                                for (int i = 0; i < bodySubHeaderVectorCoiv2.size(); i++) {
                                    SubHeaderBean bean = (SubHeaderBean) bodySubHeaderVectorCoiv2.get(i);
                                         actionPaths[i]=bean.getSubHeaderLink();
                                         subHeaderNames[i]  = bean.getSubHeaderName();
                                    }
                                for(int j=0;j<actionPaths.length;j++){
               %>
               <td>
                   <html:link action="<%=actionPaths[j]%>"><u><%=subHeaderNames[j]%></u></html:link>
                </td>
               <%
                     }

                     %>
              </td>
            </tr>
             </table>

</logic:present>--%>


		<logic:present name="questionDetView">
			<table id="noteBodyTable" class="table" width="100%" border="0">
				<tr style="background-color: #6E97CF; height: 22px;">
					<td style="color: #FFFFFF; font-size: 12px; font-weight: bold;">Question
						Id</td>
					<td style="color: #FFFFFF; font-size: 12px; font-weight: bold;">Questions</td>
					<td style="color: #FFFFFF; font-size: 12px; font-weight: bold;">Answers</td>
				</tr>
				<%
                            String qnBgColor = "#DCE5F1";
                            int indexqn = 0;
                            int qid = 1;
                %>
				<logic:iterate id="qnsAnsView" name="questionDetView">
					<%
                                if (indexqn % 2 == 0) {
                                    qnBgColor = "#D6DCE5";
                                } else {
                                    qnBgColor = "#DCE5F1";
                                }

               Vector entityPjtList = (Vector) request.getAttribute("questionDetView");
               if (entityPjtList != null && entityPjtList.size() > 0){
               //if answer is N/A,that is stored into the database as X.Code added for replace X with N/A
               for(int i=0;i<entityPjtList.size();i++)
                {
                    CoiQuestionAnswerBean questionDetai=
                   (CoiQuestionAnswerBean)entityPjtList.get(i);
                    String ans=(String)questionDetai.getAnswer();
                    if(ans!=null && ans.equals("X"))
                    {
                          questionDetai.setAnswer("N/A");
                    }
                }
               //Code added for replace X with N/A  ends

            }
                    %>
					<tr bgcolor="<%=qnBgColor%>" id="row<%=indexqn%>" class="rowLine"
						onmouseover="className='rowHover rowLine'"
						onmouseout="className='rowLine'" height="22px">
						<td><%= qid%></td>
						<td><bean:write name="qnsAnsView" property="question" /></td>
						<td><bean:write name="qnsAnsView" property="answerString" /></td>
					</tr>
					<%indexqn++;
                                qid++;%>
				</logic:iterate>
				<tr>
					<td class='savebutton' align="left" colspan="3"><html:button
							onclick="javaScript:saveAndcontinue();" property="Save"
							styleClass="clsavebutton" style="width:150px;">
                                           Continue
                                        </html:button></td>
				</tr>
			</table>
		</logic:present>
		<logic:notPresent name="questionDetView">
			<table id="noteBodyTable" class="table" width="100%" border="0">
				<tr>
					<td><font color="red"><bean:message bundle="coi"
								key="coiMessage.noQuestionnaire" /></font></td>
				</tr>
				<tr>
					<td class='savebutton' align='left'><html:submit
							value="Continue" styleClass="clsavebutton"
							onclick="javascript:continueWithoutQnr();" /></td>
				</tr>
			</table>
		</logic:notPresent>
	</html:form>
</body>
</html>
