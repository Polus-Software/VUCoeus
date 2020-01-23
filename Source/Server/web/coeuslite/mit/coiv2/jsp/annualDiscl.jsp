<%--
    Document   : annualDiscl
    Created on : Mar 16, 2010, 7:39:23 PM
    Author     : Roshin
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"
	import="edu.mit.coeuslite.coiv2.beans.CoiAnnualPersonProjectDetails, edu.mit.coeuslite.coiv2.beans.CoiDisclosureBean, java.util.Vector,edu.mit.coeuslite.coiv2.beans.CoiProposalBean,org.apache.struts.taglib.html.JavascriptValidatorTag,java.util.HashMap,edu.mit.coeuslite.coiv2.utilities.CoiConstants,edu.mit.coeuslite.coiv2.beans.CoiProtocolInfoBean,edu.mit.coeuslite.coiv2.beans.CoiAwardInfoBean;"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/coeus-utils.tld" prefix="coeusUtils"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%--<html xmlns="http://www.w3.org/1999/xhtml">--%>
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
                String opertnType = null;
                String operationType = (String) request.getAttribute("operation");
                if(request.getAttribute("operationType") != null) {
                 opertnType = (String)request.getAttribute("operationType");
                }
                request.setAttribute("operationType", opertnType);               
               
                  String  noProjects = path + "/getAttachmentsCoiv2.do";
                  String  target = path + "/coiConflictStatus.do";
               
                int index = 0;
                int instindex = 0;
                int protoIndex = 0;
                int iacucprotoIndex=0;
                int awardIndex = 0;
                 boolean pjtPresent = false;
          
            %>
</head>
<script language="javascript" type="text/JavaScript"
	src="<%=request.getContextPath()%>/coeuslite/mit/utils/scripts/coeusLiteCalendar.js"></script>
<script type="text/javascript">
    var ctxpath='<%=request.getContextPath()%>';
    function addDisclSubmit(count,protoCount,awardCount){        
            document.forms[0].action='<%=target%>';
            document.forms[0].submit();
       // }
    }
     function noProjects(){       
            document.forms[0].action='<%=noProjects%>';
            document.forms[0].submit();
       // }
    }
    function showPjtDet(){
        document.getElementById("pjtDet").style.visibility="visible";
        document.forms[0].coiProjectTitle.focus();
    }
    function validateNewproject()
    {
        if(document.forms[0].coiProjectTitle.value==null || document.forms[0].coiProjectTitle.value=='')
        {
            alert("Please enter a project titile");
            document.forms[0].coiProjectTitle.focus();
            return false;
        }
        if(document.forms[0].coiProjectId.value==null || document.forms[0].coiProjectId.value=='')
        {
            alert("Please enter a project id");
            document.forms[0].coiProjectId.focus();
            return false;
        }
        if(document.forms[0].coiProjectType.value==null || document.forms[0].coiProjectType.value=='')
        {
            alert("Please enter a project type");
            document.forms[0].coiProjectType.focus();
            return false;
        }
        if(document.forms[0].coiProjectSponser.value==null || document.forms[0].coiProjectSponser.value=='')
        {
            alert("Please enter a project Sponser");
            document.forms[0].coiProjectSponser.focus();
            return false;
        }
        if(document.forms[0].coiProjectFundingAmount.value==null || document.forms[0].coiProjectFundingAmount.value=='')
        {
            alert("Please enter a funding amount");
            document.forms[0].coiProjectFundingAmount.focus();
            return false;
        }
        if(document.forms[0].coiProjectStartDate.value==null || document.forms[0].coiProjectStartDate.value=='')
        {
            alert("Please enter a Start date");
            document.forms[0].coiProjectStartDate.focus();
            return false;
        }
        if(document.forms[0].coiProjectEndDate.value==null || document.forms[0].coiProjectEndDate.value=='')
        {
            alert("Please enter a End date");
            document.forms[0].coiProjectEndDate.focus();
            return false;
        }
        return true;
    }
    function saveProject()
    {
        debugger;
        var success=validateNewproject();
        var operationType='<%= request.getAttribute("operation")%>';
        if(success==true){
            document.forms[0].action='<%=path%>'+"/saveNonIntegratedAnnuals.do?&operation="+operationType;
            document.forms[0].submit();
        }
    }

     function exitToCoi(){
        var answer = confirm( "This operation will discard current changes.  Prior screen entries will be saved.  You may continue this disclosure event by selecting it from the Pending section of your COI home page.  Click 'OK' to continue." );
             if(answer) {
                document.location = '<%=request.getContextPath()%>/exitDisclosure.do';
                window.location;
             }
     }     

</script>


<table id="bodyTable" class="table" style="width: 100%; height: 100%;"
	border="0">
	<%--<body>--%>

	<html:form action="/getProjectDetailsAndFinEntityDetailsAnnualCoiv2.do"
		method="post">
		<logic:present name="coiDevProposal">
			<%-- <tr>
                <td colspan="3"><p style="color: #ff0000;font-size: 12px;">No Proposal is created for the userFollowing projects will be added to your annual disclosure</p></td>
                <% if (disable == null || disable.equals("")) {%>
                <td colspan="3"><a href="javaScript:showPjtDet();">Add New</a></td>
                <%}%>
            </tr>--%>
		</logic:present>
		<logic:notEmpty name="coiDevProposal">
			<%pjtPresent = true; %>
		</logic:notEmpty>
		<logic:notEmpty name="coiAward">
			<%pjtPresent = true; %>
		</logic:notEmpty>
		<logic:notEmpty name="coiInstProposals">
			<%pjtPresent = true; %>
		</logic:notEmpty>
		<logic:notEmpty name="coiIRBprotocol">
			<%pjtPresent = true; %>
		</logic:notEmpty>
		<logic:notEmpty name="coiIACUCProtocol">
			<%pjtPresent = true; %>
		</logic:notEmpty>


		<%if(pjtPresent){%>
		<tr>
			<td colspan="8">The following projects will be added to your
				disclosure. A check mark indicates the project financial entity
				relationship has been reviewed. All projects must be reviewed prior
				to submitting the disclosure. Click Continue to cycle through the
				listed projects or navigate to individual projects by clicking on
				the project number</td>

		</tr>
		<%}%>

		<logic:notEmpty name="coiDevProposal">
			<%--<tr>
                <td colspan="3">Select the proposals to include in the disclosureFollowing proposals will be added to your annual disclosure</td>
                <% if (disable == null || disable.equals("")) {%>
                <td colspan="3"><a href="javaScript:showPjtDet();">Add New</a></td>
                <%}%>
            </tr>--%>

			<tr style="background-color: #6E97CF;">
				<td><strong></strong></td>
				<td colspan="2"><strong> Proposal Number</strong></td>
				<td><strong> Proposal Name</strong></td>
				<td colspan="2"><strong> Sponsor</strong></td>

			</tr>

			<%
                        String[] rowColors = {"#D6DCE5", "#DCE5F1"};
                        int i = 0;
                        String rowColor = "";
            %>

			<logic:messagesPresent>
				<font color="red"> <bean:write name="invaliddate" />
				</font>
			</logic:messagesPresent>
			<%

                        Vector propsal = (Vector) request.getAttribute("coiDevProposal");
            %>
			<logic:iterate id="allProposals" name="coiDevProposal">
				<%
                            boolean dataSaved = false;  
                            if (i == 0) {
                                rowColor = rowColors[0];
                                i = 1;
                            } else {
                                rowColor = rowColors[1];
                                i = 0;
                            }
                            CoiAnnualPersonProjectDetails propBean = new CoiAnnualPersonProjectDetails();
                             if (propsal != null && !propsal.isEmpty()) {
                                propBean = (CoiAnnualPersonProjectDetails) propsal.get(index);
                                if(propBean.isDataSaved()){
                                       dataSaved = true;
                                   } 
                                String link = "";                                  
                                }
                             String link = "/coiSaveSelectedProject.do?selectedProject="+propBean.getModuleItemKey();
                            index++;
                                           %>
				<tr bgcolor="<%=rowColor%>" class="rowLine"
					onmouseover="className='rowHover rowLine'"
					onmouseout="className='rowLine'" style="width: 765px;">
					<td>
						<%if(dataSaved){ %> <img
						src="<%=request.getContextPath()%>/coeusliteimages/complete.gif"
						width="19" height="19" /> <%}%>
					</td>
					<td colspan="2"><input type="checkbox" checked
						style="visibility: hidden" name="checkedPropsalProjects"
						value="<bean:write name="allProposals" property="coiProjectId"/>;<bean:write name="allProposals" property="coiProjectTitle"/>;<bean:write name="allProposals" property="coiProjectStartDate"/>;<bean:write name="allProposals" property="coiProjectEndDate"/>" />
						<html:link action="<%=link%>">
							<bean:write name="allProposals" property="coiProjectId" />
						</html:link></td>
					<td><bean:write name="allProposals" property="coiProjectTitle" /></td>
					<td colspan="2"><bean:write name="allProposals"
							property="coiProjectSponser" /></td>

				</tr>
			</logic:iterate>
		</logic:notEmpty>
		<logic:notEmpty name="coiInstProposals">
			<%-- <tr>
            <td colspan="3">Following submitted proposals will be added to your annual disclosure</td>
            </tr>--%>
			<tr style="background-color: #6E97CF;">
				<td><strong></strong></td>
				<td colspan="2"><strong> Proposal Number</strong></td>
				<td><strong> Proposal Name</strong></td>
				<td colspan="2"><strong> Sponsor</strong></td>
			</tr>
			<%
                        String[] rowColors1 = {"#D6DCE5", "#DCE5F1"};
                        int i1 = 0;
                        String rowColor1 = "";
            %>

			<logic:messagesPresent>
				<font color="red"> <bean:write name="invaliddate" />
				</font>
			</logic:messagesPresent>
			<%

                        Vector propsal1 = (Vector) request.getAttribute("coiInstProposals");
            %>
			<logic:iterate id="allProposals1" name="coiInstProposals">
				<%
                         boolean dataSaved = false;
                         if (i1 == 0) {
                              rowColor1 = rowColors1[0];
                              i1 = 1;
                         } else {
                               rowColor1 = rowColors1[1];
                               i1 = 0;
                          }
                            CoiAnnualPersonProjectDetails propBean1 = new CoiAnnualPersonProjectDetails();                            
                            if (propsal1 != null && !propsal1.isEmpty()) {
                                propBean1 = (CoiAnnualPersonProjectDetails) propsal1.get(instindex);                                
                                if(propBean1.isDataSaved()){
                                       dataSaved = true;
                                   }  
                            }
                            String link = "/coiSaveSelectedProject.do?selectedProject="+propBean1.getModuleItemKey();
                            instindex++;
                %>
				<tr bgcolor="<%=rowColor1%>" class="rowLine"
					onmouseover="className='rowHover rowLine'"
					onmouseout="className='rowLine'" style="width: 765px;">
					<td>
						<%if(dataSaved){ %> <img
						src="<%=request.getContextPath()%>/coeusliteimages/complete.gif"
						width="19" height="19" /> <%}%>
					</td>
					<td colspan="2"><input type="checkbox" checked
						style="visibility: hidden" name="checkedInstPropsalProjects"
						value="<bean:write name="allProposals1" property="coiProjectId"/>;<bean:write name="allProposals1" property="coiProjectTitle"/>;<bean:write name="allProposals1" property="coiProjectStartDate"/>;<bean:write name="allProposals1" property="coiProjectEndDate"/>;<bean:write name="allProposals1" property="personName"/>;<bean:write name="allProposals1" property="coiProjectId"/>" />
						<html:link action="<%=link%>">
							<bean:write name="allProposals1" property="coiProjectId" />
						</html:link></td>
					</td>
					<td><bean:write name="allProposals1"
							property="coiProjectTitle" /></td>
					<td colspan="2"><bean:write name="allProposals1"
							property="coiProjectSponser" /></td>
				</tr>
			</logic:iterate>
		</logic:notEmpty>

		<logic:messagesPresent>
			<script type="text/javascript">
                showPjtDet();
                count= <%=index%>;
            </script>
		</logic:messagesPresent>
		<%--protocol start--%>
		<logic:present name="coiIRBprotocol">
			<logic:notEmpty name="coiIRBprotocol">
				<%--<tr>
            <td colspan="5">Following IRB protocols will be added to your annual disclosure</td>
            </tr>--%>


				<tr style="background-color: #6E97CF;">
					<td><strong></strong></td>
					<td colspan="2"><strong> Protocol Number</strong></td>
					<td><strong> Protocol Name</strong></td>
					<td colspan="2"><strong> Application Date</strong></td>
				</tr>

				<%
                            String[] rowColorsProto = {"#D6DCE5", "#DCE5F1"};
                            int iProto = 0;
                            String rowColorProto = "";
                            Vector protoList = (Vector) request.getAttribute("coiIRBprotocol");
                %>
				<logic:iterate id="allProtocols" name="coiIRBprotocol">
					<%
                              
                                boolean dataSaved = false;
                                if (iProto == 0) {
                                    rowColorProto = rowColorsProto[0];
                                    iProto = 1;
                                } else {
                                    rowColorProto = rowColorsProto[1];
                                    iProto = 0;
                                }                                
                                CoiAnnualPersonProjectDetails coiProtocolInfoBeanproto = new CoiAnnualPersonProjectDetails();
                                if (protoList != null && !protoList.isEmpty()) {
                                    coiProtocolInfoBeanproto = (CoiAnnualPersonProjectDetails) protoList.get(protoIndex);
                                     
                                   if(coiProtocolInfoBeanproto.isDataSaved()){
                                       dataSaved = true;
                                   }                                                                       
                                }
                               String link = "/coiSaveSelectedProject.do?selectedProject="+coiProtocolInfoBeanproto.getModuleItemKey();
                                protoIndex++;
                    %>
					<tr bgcolor="<%=rowColorProto%>" class="rowLine"
						onmouseover="className='rowHover rowLine'"
						onmouseout="className='rowLine'" style="width: 765px;">
						<td>
							<%if(dataSaved){ %> <img
							src="<%=request.getContextPath()%>/coeusliteimages/complete.gif"
							width="19" height="19" /> <%}%>
						</td>
						<td colspan="2"><input type="checkbox" checked
							style="visibility: hidden" name="checkedProtocolBasedProjects"
							value="<bean:write name="allProtocols" property="coiProjectId"/>;<bean:write name="allProtocols" property="coiProjectTitle"/>;<bean:write name="allProtocols" property="coiProjectStartDate"/>;<bean:write name="allProtocols" property="coiProjectEndDate"/>" />
							<html:link action="<%=link%>">
								<bean:write name="allProtocols" property="coiProjectId" />
							</html:link></td>
						<td><bean:write name="allProtocols"
								property="coiProjectTitle" /></td>
						<td colspan="2"><bean:write name="allProtocols"
								property="coiProjectStartDate" format="yyyy-MM-dd" /></td>
					</tr>
				</logic:iterate>
			</logic:notEmpty>
		</logic:present>
		<logic:present name="coiIACUCProtocol">
			<logic:notEmpty name="coiIACUCProtocol">
				<%--<tr>
            <td colspan="5">Following IACUC protocols will be added to your annual disclosure</td>
            </tr>--%>
				<tr style="background-color: #6E97CF;">
					<td></td>
					<td colspan="2"><strong> Protocol Number</strong></td>
					<td><strong> Protocol Name</strong></td>
					<td colspan="2"><strong> Application Date</strong></td>
				</tr>
				<%
                         String[] rowColorsProto1 = {"#D6DCE5", "#DCE5F1"};
                           int  iProto1 = 0;
                        String rowColorProto1 = "";
                       Vector iacucprotoList = (Vector) request.getAttribute("coiIACUCProtocol");
                %>
				<logic:iterate id="alliacucProtocols" name="coiIACUCProtocol">
					<% 
                             boolean dataSaved = false;   
                             if (iProto1 == 0) {
                                  rowColorProto1 = rowColorsProto1[0];
                               iProto1 = 1;
                               }
                       {
                                  rowColorProto1 = rowColorsProto1[1];
                                   iProto1 = 0;
                             }
                               String checkedProto1= "";
                              CoiAnnualPersonProjectDetails coiProtocolInfoBeaniacucproto = new CoiAnnualPersonProjectDetails();
                             if (iacucprotoList != null && !iacucprotoList.isEmpty()) {
                                  coiProtocolInfoBeaniacucproto = (CoiAnnualPersonProjectDetails) iacucprotoList.get(iacucprotoIndex);
                                  if(coiProtocolInfoBeaniacucproto.isDataSaved()){
                                       dataSaved = true;
                                   }  
                                }
                               String link = "/coiSaveSelectedProject.do?selectedProject="+coiProtocolInfoBeaniacucproto.getModuleItemKey();
  
                                iacucprotoIndex++;
                    %>

					<tr bgcolor="<%=rowColorProto1%>" class="rowLine"
						onmouseover="className='rowHover rowLine'"
						onmouseout="className='rowLine'" style="width: 765px;">
						<td>
							<%if(dataSaved){ %> <img
							src="<%=request.getContextPath()%>/coeusliteimages/complete.gif"
							width="19" height="19" /> <%}%>
						</td>
						<td colspan="2"><input type="checkbox" checked
							style="visibility: hidden"
							name="checkedIacucProtocolBasedProject"
							value="<bean:write name="alliacucProtocols" property="coiProjectId"/>;<bean:write name="alliacucProtocols" property="coiProjectTitle"/>;<bean:write name="alliacucProtocols" property="coiProjectStartDate"/>;<bean:write name="alliacucProtocols" property="coiProjectEndDate"/>" />
							<html:link action="<%=link%>">
								<bean:write name="alliacucProtocols" property="coiProjectId" />
							</html:link></td>
						<td><bean:write name="alliacucProtocols"
								property="coiProjectTitle" /></td>
						<td colspan="2"><bean:write name="alliacucProtocols"
								property="coiProjectStartDate" format="yyyy-MM-dd" /></td>
					</tr>
				</logic:iterate>
			</logic:notEmpty>
		</logic:present>
		<%--protocol end--%>

		<%--award start--%>
		<logic:present name="coiAward">
			<logic:notEmpty name="coiAward">
				<%--<tr>
                <td colspan="5">Please select necessary awards from this list to add the disclosureFollowing awards will be added to your annual disclosure</td>
            </tr>--%>

				<tr style="background-color: #6E97CF;">
					<td><strong></strong></td>
					<td width="15%" style="padding-left: 20px"><strong>Award
							#</strong></td>
					<td width="10%"><strong>Account #</strong></td>
					<td width="35%"><strong>Title</strong></td>
					<td width="22%"><strong>Sponsor</strong></td>
					<td><strong>Award Date</strong></td>

				</tr>

				<%
                        String[] rowColorsAward = {"#D6DCE5", "#DCE5F1"};
                        int iAward = 0;
                        String rowColorAward = "";
                        Vector awardList = (Vector) request.getAttribute("coiAward");
            %>

				<logic:iterate id="allAwards" name="coiAward">
					<%
                             boolean dataSaved = false;    
                            if (iAward == 0) {
                                rowColorAward = rowColorsAward[0];
                                iAward = 1;
                            } else {
                                rowColorAward = rowColorsAward[1];
                                iAward = 0;
                            }
                            
                            CoiAnnualPersonProjectDetails awardInfoBean = new CoiAnnualPersonProjectDetails();
                            if (awardList != null && !awardList.isEmpty()) {
                                awardInfoBean = (CoiAnnualPersonProjectDetails) awardList.get(awardIndex);
                                if(awardInfoBean.isDataSaved()){
                                       dataSaved = true;
                                   }  
                            }
                            String link = "/coiSaveSelectedProject.do?selectedProject="+awardInfoBean.getModuleItemKey();
                            awardIndex++;
                %>
					<tr bgcolor="<%=rowColorAward%>" class="rowLine"
						onmouseover="className='rowHover rowLine'"
						onmouseout="className='rowLine'" style="width: 765px;">
						<td>
							<%if(dataSaved){ %> <img
							src="<%=request.getContextPath()%>/coeusliteimages/complete.gif"
							width="19" height="19" /> <%}%>
						</td>
						<td><input type="checkbox" checked style="visibility: hidden"
							name="checkedAwardBasedProjects"
							value="<bean:write name="allAwards" property="coiProjectId"/>;<bean:write name="allAwards" property="awardTitle"/>;<bean:write name="allAwards" property="coiProjectStartDate"/>;<bean:write name="allAwards" property="coiProjectEndDate"/>" />
							<html:link action="<%=link%>">
								<bean:write name="allAwards" property="coiProjectId" />
							</html:link></td>
						<td align="center"><bean:write name="allAwards"
								property="accountNumber" /></td>
						<td><bean:write name="allAwards" property="coiProjectTitle" /></td>
						<td><bean:write name="allAwards" property="coiProjectSponser" /></td>
						<td><bean:write name="allAwards"
								property="coiProjectStartDate" format="yyyy-MM-dd" /></td>

					</tr>
				</logic:iterate>
			</logic:notEmpty>
		</logic:present>
		<%--award end--%>
		<%boolean hasEntered = false;%>
		<logic:empty name="coiDevProposal">
			<logic:empty name="coiIRBprotocol">
				<logic:empty name="coiIACUCProtocol">
					<logic:empty name="coiAward">
						<logic:empty name="coiInstProposals">
							<tr>
								<td><font color="red">You do not have any Projects</font></td>
							</tr>
							<tr>
								<td align="left" colspan="4">
									<% String link2 = "javaScript:noProjects()";
                                         hasEntered =true;%> <html:button
										styleClass="clsavebutton" value="Continue" property="save"
										onclick="<%=link2%>" /> &nbsp;<html:button
										property="saveAndProceed" styleClass="clsavebutton"
										style="width:150px;" value="Discard Changes"
										onclick="javascript:exitToCoi()" />
								</td>
							</tr>
						</logic:empty>
					</logic:empty>
				</logic:empty>
			</logic:empty>
		</logic:empty>
		<%if(!hasEntered){%>
		<tr>
			<td align="left" colspan="4">
				<%
                                String link = "javaScript:addDisclSubmit('" + index + "','" + protoIndex + "','" + awardIndex + "')";%>
				<html:button styleClass="clsavebutton" value="Continue"
					property="save" onclick="<%=link%>" /> &nbsp;<html:button
					property="saveAndProceed" styleClass="clsavebutton"
					style="width:150px;" value="Quit" onclick="javascript:exitToCoi()" />
			</td>
		</tr>
		<%}%>

	</html:form>
</table>