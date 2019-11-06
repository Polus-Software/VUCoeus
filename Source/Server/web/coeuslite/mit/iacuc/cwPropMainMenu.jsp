<? xml version="1.0" ?>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<jsp:useBean id="userUnits" scope="request" class="java.util.Vector" />
<bean:size id="userUnitsSize" name="userUnits" />


<jsp:useBean id="user" scope="session"
	class="edu.mit.coeus.bean.UserInfoBean" />
<jsp:useBean id="person" scope="session"
	class="edu.mit.coeus.bean.PersonInfoBean" />
<%@ include file="/coeuslite/mit/utils/CoeusContextPath.jsp"%>
<html:html locale="true">


<head>
<title>CoeusLite</title>
<html:base />
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
<link
	href="<bean:write name='ctxtPath'/>/coeuslite/mit/utils/css/coeus_styles.css"
	rel="stylesheet" type="text/css">
<script language="javascript" type="text/JavaScript" src="_js.js"></script>
<script language="JavaScript" type="text/JavaScript">
    function open_proposal_search(link)
     {
        validFlag = "proposal";
        var winleft = (screen.width - 650) / 2;
        var winUp = (screen.height - 450) / 2;  
        var win = "scrollbars=1,resizable=1,width=790,height=450,left="+winleft+",top="+winUp
        sList = window.open('<bean:write name='ctxtPath'/>'+"/"+link, "list", win);
     }
     function fetch_Data(result)
     {     
        document.budgetSummary.proposalNumber.value = result["PROPOSAL_NUMBER"] ;
     }
     
</script>

<!--<link href="css/ipf.css" rel="stylesheet" type="text/css">
        <style type="text/css">
        </style>-->
</head>

<body>
	<html:form action="/budgetSummary.do" method="post">
		<a name="top"></a>
		<!-- New Template for cwViewFinEntity - Start   -->


		<table width="100%" height="100%" border="0" cellpadding="0"
			cellspacing="0" class="table">

			<tr>
				<td height="20" align="left" valign="top" class="theader">
					&nbsp;&nbsp; <bean:message key="label.ProposalDevelopmentMainMenu" />
					<%--Proposal Development Main Menu--%>
				</td>
			</tr>

			<tr align="left">
				<td class='copybold'><font color="red"> <logic:messagesPresent
							message="true">
							<html:messages id="message" message="true"
								property="creationProposalRightRequired" bundle="proposal">
								<li><bean:write name="message" /></li>
							</html:messages>

							<html:messages id="message" message="true"
								property="ProposalNumberRequired" bundle="budget">
								<li><bean:write name="message" /></li>
							</html:messages>

							<html:messages id="message" message="true"
								property="InvalidProposal" bundle="budget">
								<li><bean:write name="message" /></li>
							</html:messages>

							<html:messages id="message" message="true"
								property="budgetDoesnotExist" bundle="budget">
								<li><bean:write name="message" /></li>
							</html:messages>


						</logic:messagesPresent>

				</font></td>
			</tr>


			<!-- EntityDetails - Start  -->
			<tr>
				<td align="left" height='625px' valign="top"><table width="99%"
						border="0" align="center" cellpadding="0" cellspacing="0"
						class="tabtable">
						<tr>
							<td colspan="4" align="left" valign="top"><table
									width="100%" height="20" border="0" cellpadding="0"
									cellspacing="0" class="tableheader">
									<tr>
										<td><bean:message key="label.Welcome" /> <%--Welcome--%>
											<b><bean:write name="person" property="firstName" />&nbsp;<bean:write
													name="person" property="lastName" /></b></td>
									</tr>
								</table></td>
						</tr>
						<tr>
							<td height='10'>&nbsp;</td>
						<tr>
							<td>
								<table>
									<TR>
										<TD class="copy"><bean:message key="label.ProposalNumber" />
											<%--Proposal Number--%></TD>
										<td width="8">
											<div align="left">
												<html:text property="proposalNumber" value="" />
											</div>
										</td>
										<td class="copy" nowrap colspan="2"><a
											href="javaScript:open_proposal_search('/coiSearch.do?type=<bean:message key="searchWindow.title.proposal"/>&search=true&searchName=ALL_PROPOSALDEVSEARCHNOROLES');"
											class="copysmall"><bean:message bundle="budget"
													key="headerLabel.openBudget" /></a></td>
										<td><html:submit property="Open Budget"
												styleClass="clbutton" value="Open Budget" /></td>
										<td valign="top"></td>
									</TR>

									<tr>
										<td height='10'>&nbsp;</td>
								</table>
							</td>
						</tr>


						<tr>
							<td>&nbsp;</td>
						</tr>

					</table></td>
			</tr>
			<tr>
				<td height='10'>&nbsp;</td>
			</tr>




		</table>
		<!-- New Template for cwViewFinEntity - End  -->





	</html:form>
</body>
</html:html>
