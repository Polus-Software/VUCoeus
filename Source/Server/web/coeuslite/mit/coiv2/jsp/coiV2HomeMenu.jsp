<%@ page contentType="text/html;charset=UTF-8" language="java"
	import="java.util.Vector"%>
<%@page
	import="edu.mit.coeuslite.utils.CoeusLiteConstants,
                edu.mit.coeuslite.utils.bean.MenuBean"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/coeus-utils.tld" prefix="coeusUtils"%>
<%@ include file="/coeuslite/mit/utils/CoeusContextPath.jsp"%>
<%@page
	import="java.util.Vector,edu.mit.coeuslite.coiv2.beans.CoiDisclosureBean,java.util.Iterator,edu.mit.coeus.bean.PersonInfoBean,edu.mit.coeuslite.utils.SessionConstants,edu.mit.coeuslite.coiv2.beans.CoiDisclosureDetailsListBean"%>

<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<%! String annualDisclosureNumberInSession=null; %>
<%String path = request.getContextPath();%>
<% Vector pendingDisclosure = (Vector) request.getAttribute("pendingDiscl"); %>
<% CoiDisclosureBean annual = (CoiDisclosureBean)request.getAttribute("annualDisclosureBean");%>
<% String proposalNumber = (String)session.getAttribute("proposalNumber"+session.getId());
   String apprvdAnnual = (String)request.getAttribute("ApprvedAnnual");
   String group="4";
   String mode = (String)session.getAttribute("mode") ;
    boolean readOnly = false;
    if(mode != null && mode.equals("display")){
        readOnly = true;
    }
    String link = "";

    annualDisclosureNumberInSession= (String)session.getAttribute("param1");

    boolean isDue = false;
    if(session.getAttribute("isReviewDue") != null) {
       isDue = (Boolean)session.getAttribute("isReviewDue");
    }
//menu selection enhancement starts
 String currentLink=request.getAttribute("javax.servlet.forward.servlet_path").toString();
 int REVICE_UPDATE=0,HISTORY=1,NEW_PROPOSAL=2,NEW_IRB=3,NEW_IACUC=4,NEW_AWARD=5,MY_COI_HOME=6;
 boolean []menuFlag=new boolean[7];
 for(int index=0;index<7;menuFlag[index++]=false);
 if(currentLink.equals("/coi.do")||currentLink.equals("/coiHome.do")){
     menuFlag[MY_COI_HOME]=true;}
 else if(currentLink.equals("/allAwards.do")){
     menuFlag[NEW_AWARD]=true;}
 else if(currentLink.equals("/allIacucProtocol.do")){
     menuFlag[NEW_IACUC]=true;}
 else if(currentLink.equals("/allProtocol.do")){
     menuFlag[NEW_IRB]=true;}
 else if(currentLink.equals("/allProposals.do")){
     menuFlag[NEW_PROPOSAL]=true;}
 else if(currentLink.equals("/showHistoryForHeader.do")){
     menuFlag[HISTORY]=true;}
 else if(currentLink.equals("/coiAnnualRevision.do")){
     menuFlag[REVICE_UPDATE]=true;}


//menu selection enhancement ends
%>
<script>


function doreviewnupdate(){
     debugger;
     alert("You do not have any Approved Annual Disclosure");
     }
function open_budget_summary(link){
         //Commented/Added for case#2776  - Allow concurrent Prop dev access in Lite - start
         //var value = "<%=request.getAttribute("createBudget")%>" ;
         var value = "<%=session.getAttribute("createBudget"+session.getId())%>";
         //Commented/Added for case#2776  - Allow concurrent Prop dev access in Lite - end
         var rights = "<%=readOnly%>";
         if(value == "createBudget" && rights == "false"){
            var msg = '<bean:message bundle="proposal" key="generalInfoProposal.createNewBudget"/>';
            if (confirm(msg)==true){
                window.location= link;
                }
            }else{
                   window.location= link;
                  }
     }

function checkProposal(value)
             {
                var proposalNumber = '<%=proposalNumber%>';
                if(proposalNumber == 'null' || proposalNumber == ''){
                    alert("<bean:message bundle="proposal" key="generalInfoProposal.saveProposalGeneral"/>");
                    return false;
                }
                CLICKED_LINK = value;
                return validateSavedInfo();
             }

             function showAlert(){
             alert("<bean:message bundle="proposal" key="generalInfoProposal.notImplemented"/>");
             }

function showConfirm(link)
     {
              var answer=confirm("You already have a revision in progress. Please submit it for review");
              if(answer) {
                document.location  = "<%=request.getContextPath()%>"+link;
                window.location;

             }
           else {
              return;
            }
       }

  function showConfirm1(link)
      {
           var answer=confirm("You already have a revision submitted for review . Do you want to create another revision?");
              if(answer) {
                document.location  = "<%=request.getContextPath()%>"+link;
                window.location;

             }
           else {
              return;
            }
       }
       function showConfirmRevise(link)
      {
           <%if(apprvdAnnual == null){ %> 
                alert('You do not have any approved Annual Disclosure.');   
           <%}else{%>
               document.location  = "<%=request.getContextPath()%>"+link;
                window.location;
           <%}%>    
           
       }

        function showTvlDet(link){
           document.location ="<%=request.getContextPath()%>"+link;
           window.location;
        }
       

        </script>
</head>
<body>
	<table class="table" align="left" width="100%" bgcolor="#9DBFE9">
		<tr>
			<td>
				<table class="table" align="center" width="200" style="padding: 4px">
					<tr class="menuHeaderName">
						<td colspan="3">My Disclosure</td>
					</tr>



					<logic:present name="disclosureAvailableMessage">
						<logic:notPresent name="annualDisclosureNumber">
							<tr class="rowLine">
								<td width="16%" height='16' align="left" valign="top"
									class="coeusMenu"></td>
								<td width="80%" align="left" valign="middle" class="coeusMenu">
									<html:link action="/coiAnnualRevision.do?projectType=Annual">Create New</html:link>
								</td>
								<td width='4%' align=right class="selectedMenuIndicator"></td>
							</tr>
						</logic:notPresent>
						<logic:present name="showReviewUpdate">
							<tr class="rowLine">
								<td width="16%" height='16' align="left" valign="top"
									class="coeusMenu"></td>
								<td width="80%" align="left" valign="middle" class="coeusMenu">
									<%if(!isDue) {%> <html:link
										action="/showDisclosure.do?check=approvedDisclosureview">View Current</html:link>
									<%}else {%> <a href="javascript:return false;"
									style="text-decoration: none; color: gray">View Current</a> <%}%>
								</td>

								<td width='4%' align=right class="selectedMenuIndicator"></td>
							</tr>
						</logic:present>
						<logic:notPresent name="showReviewUpdate">
							<logic:equal value="true" name="disclosureAvailableMessage">
								<tr class="rowLine">
									<td width="16%" height='16' align="left" valign="top"
										class="coeusMenu"></td>
									<td width="80%" align="left" valign="middle" class="coeusMenu">
										<a href="javascript:return false;"
										style="text-decoration: none; color: gray">View Current</a>
									</td>
									<td width='4%' align=right class="selectedMenuIndicator"></td>
								</tr>
							</logic:equal>
						</logic:notPresent>
						<%
        Vector pendingRevisionDetails = (Vector)request.getSession().getAttribute("pendingRevisedPjctList");
        String link1="";
        String disclNumber="";
        Integer seqNumber=0;
        String personId="";
        if(pendingRevisionDetails !=null && pendingRevisionDetails.size()>0){
        CoiDisclosureBean coiDisclosureBean=(CoiDisclosureBean) pendingRevisionDetails.get(0);
        disclNumber=coiDisclosureBean.getCoiDisclosureNumber();
        seqNumber=coiDisclosureBean.getSequenceNumber();
        PersonInfoBean person = (PersonInfoBean) session.getAttribute(SessionConstants.LOGGED_IN_PERSON);
        personId = person.getPersonID();
       // link1="/findDisclosureProjectType.do?param1="+disclNumber+"&param2="+seqNumber+"&param3="+personId+"&param5=6&param6=pendingDiscl";
        link1="/coiInProgress.do?discl="+disclNumber+"&seq="+seqNumber+"&personId="+personId+"&projectID=null&eventType=Revision&moduleCode=6";
        }
        %>
						<logic:equal value="true" name="disclosureAvailableMessage">
							<tr class="rowLine">
								<td width="16%" height='16' align="left" valign="top"
									class="coeusMenu"></td>
								<logic:present name="annualDisclosureBean">
									<logic:equal value="true" name="disclosureAvailableMessage">
										<%
        annualDisclosureNumberInSession= (String)session.getAttribute("showReviewUpdate");
        if(annualDisclosureNumberInSession!=null){
          if(!isDue){
            %>
										<logic:present name="projectType">
											<logic:equal value="Revision" name="projectType">
												<logic:equal name="certified" value="true">
													<td width="80%" align="left" valign="middle"
														class="coeusMenu"><a
														href="javascript:showConfirm1('/coiAnnualRevision.do?projectType=Revision');">
															Revise/Update</a></td>
													<td width='4%' align=right class="selectedMenuIndicator"></td>
												</logic:equal>
												<logic:equal name="certified" value="false">
													<td width="80%" align="left" valign="middle"
														class="coeusMenu">
														<%--<html:link action="#" onclick="javascript:showDialog('<%=link1%>')"> Revise/Update</html:link>--%>
														<a href="javascript:showConfirm('<%=link1%>');">
															Revise/Update</a>
													</td>
													<td width='4%' align=right class="selectedMenuIndicator"></td>
												</logic:equal>
												<%--<td width='4%' align=right  class ="selectedMenuIndicator" ><bean:message key="menu.selected"/></td>--%>
											</logic:equal>
											<logic:notEqual value="Revision" name="projectType">
												<logic:equal name="certified" value="true">
													<td width="80%" align="left" valign="middle"
														class="coeusMenu"><a
														href="javascript:showConfirm1('/coiAnnualRevision.do?projectType=Revision');">
															Revise/Update</a></td>
													<td width='4%' align=right class="selectedMenuIndicator"></td>
												</logic:equal>
												<logic:equal name="certified" value="false">
													<td width="80%" align="left" valign="middle"
														class="coeusMenu"><a
														href="javascript:showConfirm('<%=link1%>');">
															Revise/Update</a></td>
													<td width='4%' align=right class="selectedMenuIndicator"></td>
												</logic:equal>
											</logic:notEqual>
										</logic:present>
										<logic:notPresent name="projectType">
											<logic:equal name="certified" value="true">
												<td width="80%" align="left" valign="middle"
													class="coeusMenu"><a
													href="javascript:showConfirmRevise('/coiAnnualRevision.do?projectType=Revision');">
														Revise/Update</a></td>
												<td width='4%' align=right class="selectedMenuIndicator"></td>
											</logic:equal>
											<logic:equal name="certified" value="false">
												<td width="80%" align="left" valign="middle"
													class="coeusMenu"><a
													href="javascript:showConfirm('<%=link1%>');">
														Revise/Update</a></td>
												<td width='4%' align=right class="selectedMenuIndicator"></td>
											</logic:equal>

										</logic:notPresent>
										<%} else { %>
										<td width="80%" align="left" valign="middle" class="coeusMenu">
											<a href="javascript:return false;"
											style="text-decoration: none; color: gray">Revise/Update</a>
										</td>
										<td width='4%' align=right class="selectedMenuIndicator"></td>
										<%}}else{
        %>
										<td width="80%" align="left" valign="middle" class="coeusMenu">
											<a href="javascript:return false;"
											style="text-decoration: none; color: gray">Revise/Update</a>
										</td>
										<td width='4%' align=right class="selectedMenuIndicator"></td>
										<% } %>
									</logic:equal>
								</logic:present>
							</tr>
						</logic:equal>
						<logic:present name="showReviewUpdate">
							<tr class="rowLine">
								<td width="16%" height='16' align="left" valign="top"
									class="coeusMenu"></td>
								<% if(!isDue){
                    %>
								<logic:present name="projectType">
									<logic:equal value="History" name="projectType">
										<td width="80%" align="left" valign="middle" class="coeusMenu">
											<html:link action="/showHistoryForHeader.do">
												<span style="color: #6D0202;">History</span>
											</html:link>
										</td>
										<td width='4%' align=right class="selectedMenuIndicator">
											<bean:message key="menu.selected" />
										</td>
									</logic:equal>
									<logic:notEqual value="History" name="projectType">
										<td width="80%" align="left" valign="middle" class="coeusMenu">
											<html:link action="/showHistoryForHeader.do">History</html:link>
										</td>
										<td width='4%' align=right class="selectedMenuIndicator"></td>
									</logic:notEqual>
								</logic:present>
								<logic:notPresent name="projectType">

									<td width="80%" align="left" valign="middle" class="coeusMenu">
										<html:link action="/showHistoryForHeader.do">History</html:link>
									</td>
									<td width='4%' align=right class="selectedMenuIndicator"></td>
								</logic:notPresent>
								<%}else {%>
								<td width="80%" align="left" valign="middle" class="coeusMenu">
									<a href="javascript:return false;"
									style="text-decoration: none; color: gray">History</a>
								</td>
								<td width='4%' align=right class="selectedMenuIndicator"></td>
								<% } %>
							</tr>

							<tr class="rowLine">
								<td width="16%" height='16' align="left" valign="top"
									class="coeusMenu"></td>
								<td width="80%" align="left" valign="middle" class="coeusMenu">
									<% if(!isDue){%> <html:link
										action="/approveddisclosureprint.do?selected=Approved"
										target="_blank">Print</html:link> <%}else {%> <a
									href="javascript:return false;"
									style="text-decoration: none; color: gray">Print</a> <% } %>
								</td>
								<td width='4%' align=right class="selectedMenuIndicator"></td>
							</tr>
						</logic:present>
						<logic:notPresent name="showReviewUpdate">
							<logic:equal value="true" name="disclosureAvailableMessage">
								<tr class="rowLine">
									<td width="16%" height='16' align="left" valign="top"
										class="coeusMenu"></td>
									<td width="80%" align="left" valign="middle" class="coeusMenu">
										<a href="javascript:return false;"
										style="text-decoration: none; color: gray">History</a>
									</td>
									<td width='4%' align=right class="selectedMenuIndicator"></td>
								</tr>

								<tr class="rowLine">
									<td width="16%" height='16' align="left" valign="top"
										class="coeusMenu"></td>
									<td width="80%" align="left" valign="middle" class="coeusMenu">
										<a href="javascript:return false;"
										style="text-decoration: none; color: gray">Print</a>
									</td>
									<td width='4%' align=right class="selectedMenuIndicator"></td>
								</tr>
							</logic:equal>
						</logic:notPresent>
					</logic:present>

					<tr>
						<td width="16%" height='16' align="left" valign="top"></td>
						<td width="80%" align="left" valign="middle">&nbsp</td>
						<td width='4%' align=right></td>
					</tr>

					<tr class="menuHeaderName">
						<td colspan="3">Disclosure Event</td>
					</tr>

					<tr class="rowline">
						<td width="16%" height='16' align="left" valign="top"
							class="coeusMenu"></td>
						<%if(!isDue){
                      %>
						<logic:present name="projectType">
							<logic:equal value="Proposal" name="projectType">
								<td width="80%" height='16' align="left" valign="middle"
									class="coeusMenu"><html:link action="/allProposals.do">
										<span style="color: #6D0202;">New Proposal</span>
									</html:link></td>
								<td width='4%' align=right class="selectedMenuIndicator">
									<bean:message key="menu.selected" />
								</td>
							</logic:equal>
							<logic:notEqual value="Proposal" name="projectType">
								<td width="80%" height='16' align="left" valign="middle"
									class="coeusMenu"><html:link action="/allProposals.do">New Proposal</html:link>
								</td>
								<td width='4%' align=right class="selectedMenuIndicator"></td>
							</logic:notEqual>
						</logic:present>

						<logic:notPresent name="projectType">
							<td width="80%" height='16' align="left" valign="middle"
								class="coeusMenu"><html:link action="/allProposals.do">New Proposal</html:link>
							</td>
							<td width='4%' align=right class="selectedMenuIndicator"></td>
						</logic:notPresent>
						<%
                      }else {%>
						<td width="80%" height='16' align="left" valign="middle"
							class="coeusMenu"><a href="javascript:return false;"
							style="text-decoration: none; color: gray">New Proposal</a></td>
						<td width='4%' align=right class="selectedMenuIndicator"></td>
						<%}%>
					</tr>

					<tr class="rowLine">
						<td width="16%" height='16' align="left" valign="top"
							class="coeusMenu"></td>
						<%if(!isDue) {
                   %>
						<logic:present name="projectType">
							<logic:equal value="Protocol" name="projectType">
								<td width='80%' align="left" valign="middle" class="coeusMenu">
									<html:link action="/allProtocol.do?proType=irb">
										<span style="color: #6D0202;">New IRB Protocol</span>
									</html:link>
								</td>
								<td width='4%' align=right class="selectedMenuIndicator">
									<bean:message key="menu.selected" />
								</td>
							</logic:equal>
							<logic:notEqual value="Protocol" name="projectType">
								<td width='80%' align="left" valign="middle" class="coeusMenu">
									<html:link action="/allProtocol.do?proType=irb">New IRB Protocol</html:link>
								</td>
								<td width='4%' align=right class="selectedMenuIndicator"></td>
							</logic:notEqual>
						</logic:present>

						<logic:notPresent name="projectType">
							<td width='80%' align="left" valign="middle" class="coeusMenu">
								<html:link action="/allProtocol.do?proType=irb">New IRB Protocol</html:link>
							</td>
							<td width='4%' align=right class="selectedMenuIndicator"></td>
						</logic:notPresent>
						<%} else {%>
						<td width='80%' align="left" valign="middle" class="coeusMenu">
							<a href="javascript:#" style="text-decoration: none; color: gray">New
								IRB Protocol</a>
						</td>
						<td width='4%' align=right class="selectedMenuIndicator"></td>
						<%}%>
					</tr>

					<tr class="rowLine">
						<td width="16%" height='16' align="left" valign="top"
							class="coeusMenu"></td>
						<%if(!isDue) {
                      %>
						<logic:present name="projectType">
							<logic:equal value="IacucProtocol" name="projectType">
								<td width='80%' align="left" valign="middle" class="coeusMenu">
									<html:link action="/allIacucProtocol.do?proType=iacuc">
										<span style="color: #6D0202;">New IACUC Protocol</span>
									</html:link>
								</td>
								<td width='4%' align=right class="selectedMenuIndicator">
									<bean:message key="menu.selected" />
								</td>
							</logic:equal>
							<logic:notEqual value="IacucProtocol" name="projectType">
								<td width='80%' align="left" valign="middle" class="coeusMenu">
									<html:link action="/allIacucProtocol.do?proType=iacuc">New IACUC Protocol</html:link>
								</td>
								<td width='4%' align=right class="selectedMenuIndicator"></td>
							</logic:notEqual>
						</logic:present>

						<logic:notPresent name="projectType">
							<td width='80%' align="left" valign="middle" class="coeusMenu">
								<html:link action="/allIacucProtocol.do?proType=iacuc">New IACUC Protocol</html:link>
							</td>
							<td width='4%' align=right class="selectedMenuIndicator"></td>
						</logic:notPresent>
						<%}else {%>
						<td width='80%' align="left" valign="middle" class="coeusMenu">
							<a href="javascript:#" style="text-decoration: none; color: gray">New
								IACUC Protocol</a>
						</td>
						<td width='4%' align=right class="selectedMenuIndicator"></td>
						<%}%>
					</tr>
					<tr class="rowLine">
						<td width="16%" height='16' align="left" valign="top"
							class="coeusMenu"></td>
						<%if(!isDue) {
                      %>
						<logic:present name="projectType">
							<logic:equal value="Award" name="projectType">
								<td align="left" valign="middle" class="coeusMenu"><html:link
										action="/allAwards.do">
										<span style="color: #6D0202;"> New Award</span>
									</html:link></td>
								<td width='4%' align=right class="selectedMenuIndicator">
									<bean:message key="menu.selected" />
								</td>
							</logic:equal>
							<logic:notEqual value="Award" name="projectType">
								<td align="left" valign="middle" class="coeusMenu"><html:link
										action="/allAwards.do">New Award</html:link></td>
								<td width='4%' align=right class="selectedMenuIndicator"></td>
							</logic:notEqual>
						</logic:present>
						<logic:notPresent name="projectType">
							<td align="left" valign="middle" class="coeusMenu"><html:link
									action="/allAwards.do">New Award</html:link></td>
							<td width='4%' align=right class="selectedMenuIndicator"></td>
						</logic:notPresent>
						<%
                        } else {%>
						<td align="left" valign="middle" class="coeusMenu"><a
							href="javascript: #" style="text-decoration: none; color: gray">New
								Award</a></td>
						<td width='4%' align=right class="selectedMenuIndicator"></td>

						<%}%>

					</tr>

					<tr class="rowLine">
						<td width="16%" height='16' align="left" valign="top"
							class="coeusMenu"></td>

						<%if(!isDue) {
                      %>
						<logic:present name="projectType">
							<logic:equal value="Travel" name="projectType">
								<td align="left" valign="middle" class="coeusMenu"><html:link
										action="/newTravel.do">
										<span style="color: #6D0202;"> New Travel</span>
									</html:link></td>
								<td width='4%' align=right class="selectedMenuIndicator">
									<bean:message key="menu.selected" />
								</td>
							</logic:equal>
							<logic:notEqual value="Travel" name="projectType">
								<td align="left" valign="middle" class="coeusMenu"><html:link
										action="/newTravel.do">New Travel</html:link></td>
								<td width='4%' align=right class="selectedMenuIndicator"></td>
							</logic:notEqual>
						</logic:present>
						<logic:notPresent name="projectType">
							<td align="left" valign="middle" class="coeusMenu"><html:link
									action="/newTravel.do">New Travel</html:link></td>
							<td width='4%' align=right class="selectedMenuIndicator"></td>
						</logic:notPresent>
						<%
                        } else {%>
						<td align="left" valign="middle" class="coeusMenu"><a
							href="javascript: #" style="text-decoration: none; color: gray">New
								Travel</a></td>
						<td width='4%' align=right class="selectedMenuIndicator"></td>

						<%}%>

					</tr>

					<tr>
						<td width="16%" height='16' align="left" valign="top"></td>
						<td width="80%" align="left" valign="middle">&nbsp</td>
						<td width='4%' align=right></td>
					</tr>
					<tr class="menuHeaderName">
						<td colspan="3">Financial Entities</td>
					</tr>

					<tr class="rowline">
						<td width="16%" height='16' align="left" valign="top"
							class="coeusMenu"></td>
						<td width="80%" height='16' align="left" valign="middle"
							class="coeusMenu"><html:link
								action="/getAnnDisclFinEntityCoiv2.do?actionFrom=main&createNew=newEntity">Create New</html:link>
						</td>
						<td width='4%' align=right class="selectedMenuIndicator"></td>
					</tr>

					<tr class="rowline">
						<td width="16%" height='16' align="left" valign="top"
							class="coeusMenu"></td>
						<td width="80%" height='16' align="left" valign="middle"
							class="coeusMenu"><html:link
								action="/listAnnFinEntityCoiv2.do">Review/Update</html:link></td>
						<td width='4%' align=right class="selectedMenuIndicator"></td>
					</tr>
					<tr>
						<td width="16%" height='16' align="left" valign="top"></td>
						<td width="80%" align="left" valign="middle">&nbsp</td>
						<td width='4%' align=right></td>
					</tr>
					<tr class="rowline">
						<td width="16%" height='16' align="left" valign="top"
							class="coeusMenu"></td>
						<%if(menuFlag[MY_COI_HOME]){%>
						<td width="80%" height='16' align="left" valign="middle"
							class="coeusMenu"><html:link
								action="/coiHome.do?Menu_Id=004">
								<span style="color: #6D0202;">My COI Home</span>
							</html:link></td>
						<td width='4%' align=right class="selectedMenuIndicator"><bean:message
								key="menu.selected" /></td>
						<%}else{%>
						<td width="80%" height='16' align="left" valign="middle"
							class="coeusMenu"><html:link
								action="/coiHome.do?Menu_Id=004">My COI Home</html:link></td>
						<td width='4%' align=right class="selectedMenuIndicator"></td>
						<%}%>



					</tr>
				</table>
			</td>
		</tr>
		<tr bgcolor="#9DBFE9">
			<td style="height: 500;" colspan="3">&nbsp;</td>
		</tr>
	</table>
</body>
</html>
