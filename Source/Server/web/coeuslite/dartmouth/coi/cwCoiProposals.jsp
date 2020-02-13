<%@page contentType="text/html"
	import="edu.mit.coeus.utils.CoeusProperties,
edu.mit.coeus.utils.CoeusPropertyKeys,
java.util.ArrayList,
java.util.List,
org.apache.struts.validator.DynaValidatorForm,
java.util.Vector"%>
<%@page pageEncoding="UTF-8" import="edu.mit.coeus.bean.*"%>
<%@page errorPage="ErrorPage.jsp"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/coeus-utils.tld" prefix="coeusUtils"%>
<%@ include file="/coeuslite/mit/utils/CoeusContextPath.jsp"%>
<jsp:useBean id="person" scope="session"
	class="edu.mit.coeus.bean.PersonInfoBean" />
<jsp:useBean id="reviewProposals" scope="session"
	class="java.util.Vector" />
<bean:size id="totProposals" name="reviewProposals" />
<jsp:useBean id="dynaForm" scope="request"
	class="org.apache.struts.validator.DynaValidatorForm" />

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
   "http://www.w3.org/TR/html4/loose.dtd">

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>JSP Page</title>
<%String path = request.getContextPath();%>
<link href="<%=path%>/coeuslite/dartmouth/utils/css/coeus_styles.css"
	rel="stylesheet" type="text/css">
<script>
            function open_search_window(link){
                    var winleft = (screen.width - 650) / 2;
                    var winUp = (screen.height - 450) / 2;  
                    var win = "scrollbars=1,resizable=1,width=800,height=450,left="+winleft+",top="+winUp
                     sList = window.open('<bean:write name='ctxtPath'/>'+"/"+link, "list", win);
                      if (parseInt(navigator.appVersion) >= 4) {
                        window.sList.focus(); 
                        }
                }
        </script>

</head>

<body>
	<table width="984" colspan='2' border="0" cellpadding="0"
		cellspacing="0" class="table">
		<form action='<%=path%>/addDisclProposal.do' method="post">
			<tr>
				<td height="26" class="theaderBlue" style="font-size: 14px"><bean:message
						bundle="coi" key="AnnualDisclosure.Proposals.Header" /></td>
				<td align="right" class="theaderblue"><a
					href='<%=path%>/addDisclProposal.do'>Continue with
						AnnualDisclosure</td>
			</tr>


			<%
                           List awardDisclosure=null;
                           List proposalDisclosure=null;
                           List protocolDisclosure=null;
                           
                           if(reviewProposals != null && reviewProposals.size() > 0) {
                               awardDisclosure = new ArrayList();
                           proposalDisclosure = new ArrayList();
                           protocolDisclosure=new ArrayList();
                           //  org.apache.commons.beanutils.DynaBean tempBean = (org.apache.commons.beanutils.DynaBean)reviewProposals.get(0);
                           //  dynaForm=(org.apache.struts.validator.DynaValidatorForm)reviewProposals.get(0);
                           String tempKey="";                
                           String strAward = "1";
                           String strProposal="2";
                           for(int index = 0; index < reviewProposals.size(); index++) {
                           dynaForm = (org.apache.struts.validator.DynaValidatorForm)reviewProposals.get(index);
                           tempKey =((Integer)dynaForm.get("moduleCode")).toString();
                           if(tempKey != null && tempKey.equalsIgnoreCase(strAward)) {
                           //Award Disclosure
                           awardDisclosure.add(dynaForm);
                           }//End IF
                           else if(tempKey != null && tempKey.equalsIgnoreCase(strProposal)) {
                           //Proposal Disclosure
                           proposalDisclosure.add(dynaForm);
                           }//End ELSE
                           else{
                               protocolDisclosure.add(dynaForm);
                           }
                           }//End FOR
                           
                           request.setAttribute("proposalDisclosure", proposalDisclosure);
                           request.setAttribute("awardDisclosure", awardDisclosure);
                           request.setAttribute("protocolDisclosure", protocolDisclosure);
                           request.setAttribute("awardDiscSize", new Integer(awardDisclosure.size()));
                           request.setAttribute("proposalDiscSize", new Integer(proposalDisclosure.size()));
                           request.setAttribute("protocolDiscSize", new Integer(protocolDisclosure.size()));
                           }
                           else{
                             request.setAttribute("awardDiscSize", new Integer(0));
                           request.setAttribute("proposalDiscSize", new Integer(0));  
                           request.setAttribute("protocolDiscSize", new Integer(0)); 
                           }
                           %>
			<tr>
				<td height="24" colspan="2" align="left" class="copybold"><bean:message
						bundle="coi" key="AnnualDisclosure.Proposals.List" /></td>
			</tr>
			<logic:notEqual name='proposalDiscSize' value='0'>
				<tr>
					<td colspan="2">
						<table width="99%" border="0" align="center" cellpadding="2"
							cellspacing="0" class="tabtable">
							<tr class="theader">

								<td width="10">&nbsp;</td>
								<td align="left" class="theaderBlue" width="194"><bean:message
										bundle="coi" key="label.proposalNumber" /></td>
								<td align="left" class="theaderBlue" width="156"><bean:message
										bundle="coi" key="label.status" /></td>
								<td align="left" class="theaderBlue" width="212"><bean:message
										bundle="coi" key="label.sponsor" /></td>

								<td align="left" class="theaderBlue" width="412"><bean:message
										bundle="coi" key="label.title" /></td>
								<td>&nbsp;</td>
							</tr>
							<%  int disclIndex = 0;
                                String strBgColor = "#DCE5F1";
                                
                                %>
							<logic:iterate id='proposal' name="proposalDisclosure"
								type="org.apache.commons.beanutils.DynaBean">
								<bean:define id="moduleKey" name="proposal"
									property="moduleItemKey" />
								<bean:define id="title" name="proposal" property="title" />
								<bean:define id="modulecode" name="proposal"
									property="moduleCode" />
								<%                                  
                                    if (disclIndex%2 == 0) {
                                    strBgColor = "#D6DCE5";
                                    } else {
                                    strBgColor="#DCE5F1";
                                    }
                                    String proposalNumber=(String)proposal.get("moduleItemKey");
                                    %>
								<tr bgcolor='<%=strBgColor%>' id="row<%=disclIndex%>"
									class="rowLine" onmouseover="className='rowHover rowLine'"
									onmouseout="className='rowLine'">
									<td width="10" height="21">&nbsp;</td>
									<td align="left" width="194"><coeusUtils:formatOutput
											name="proposal" property="moduleItemKey" /></td>
									<td align="left" width="156"><coeusUtils:formatOutput
											name="proposal" property="description" /></td>
									<td align="left" width="212"><coeusUtils:formatOutput
											name="proposal" property="sponsorName" /></td>
									<td align="left"><coeusUtils:formatOutput name="proposal"
											property="title" /></td>
									<%--    <td>
                                        <logic:equal name="proposal" property="disclExistsFlg"  value = '-1'> 
                                      <a href="javascript:open_search_window('getFinEntRev.do?moduleItemKey=<%=moduleKey%>&title=<%=title%>&moduleCode=<%=modulecode%>');">Add</a>
                                       </logic:equal>
                                       <logic:equal name="proposal" property="disclExistsFlg"  value = '1'>  
                                           <img src='<%=path%>/coeusliteimages/images/check.gif'/>
                                       </logic:equal>
                                    </td>--%>
								</tr>
							</logic:iterate>
						</table>
					</td>
				</tr>
			</logic:notEqual>
			<logic:equal name="proposalDiscSize" value="0">
				<tr>
					<td colspan="2">
						<table width="99%" border="0" align="center" cellpadding="2"
							cellspacing="0" class="tabtable">
							<tr>
								<td class=copybold><bean:message bundle="coi"
										key="AnnualDisclosure.Proposals.NOList" /></td>
							</tr>
						</table>
					</td>
				</tr>
			</logic:equal>
			<tr>
				<td height="24" colspan="2" align="left" class="copybold"><bean:message
						bundle="coi" key="AnnualDisclosure.Awards.List" /></td>
			</tr>

			<logic:notEqual name='awardDiscSize' value='0'>
				<tr>
					<td colspan="2">
						<table width="99%" border="0" align="center" cellpadding="2"
							cellspacing="0" class="tabtable">
							<tr class="theader">
								<td width="10">&nbsp;</td>
								<td align="left" class="theaderBlue" width="194"><bean:message
										bundle="coi" key="label.awardNumber" /></td>
								<td align="left" class="theaderBlue" width="156"><bean:message
										bundle="coi" key="label.status" /></td>
								<td align="left" class="theaderBlue" width="212"><bean:message
										bundle="coi" key="label.sponsor" /></td>
								<td align="left" class="theaderBlue" width="412"><bean:message
										bundle="coi" key="label.title" /></td>
							</tr>

							<%  int disclIndex = 0;
                                String strBgColor = "#DCE5F1";
                                
                                %>
							<logic:iterate id='award' name="awardDisclosure"
								type="org.apache.commons.beanutils.DynaBean">
								<bean:define id='awardNumber' name='award'
									property="moduleItemKey" type="java.lang.String" />
								<%                                  
                                    if (disclIndex%2 == 0) {
                                    strBgColor = "#D6DCE5";
                                    } else {
                                    strBgColor="#DCE5F1";
                                    }
                                    
                                    %>

								<tr bgcolor='<%=strBgColor%>' id="row<%=disclIndex%>"
									class="rowLine" onmouseover="className='rowHover rowLine'"
									onmouseout="className='rowLine'">
									<td width="10" height="21">&nbsp;</td>
									<td align="left" width="194"><coeusUtils:formatOutput
											name="award" property="moduleItemKey" /></td>
									<td align="left" width="156"><coeusUtils:formatOutput
											name="award" property="description" /></td>
									<td align="left" width="212"><coeusUtils:formatOutput
											name="award" property="sponsorName" /></td>
									<td align="left" width="412"><coeusUtils:formatOutput
											name="award" property="title" />
									<td>
								</tr>
							</logic:iterate>
						</table>
					</td>
				</tr>
			</logic:notEqual>
			<logic:equal name="awardDiscSize" value="0">
				<tr>
					<td colspan="2">
						<table width="99%" border="0" align="center" cellpadding="2"
							cellspacing="0" class="tabtable">
							<tr>
								<td class="copybold"><bean:message bundle="coi"
										key="AnnualDisclosure.Awards.NOList" /></td>
							</tr>
						</table>
					</td>
				</tr>
			</logic:equal>
			<tr>
				<td height="24" colspan="2" align="left" class="copybold"><bean:message
						bundle="coi" key="AnnualDisclosure.Protocols.List" /></td>
			</tr>

			<logic:notEqual name='protocolDiscSize' value='0'>
				<tr>
					<td colspan="2">
						<table width="99%" border="0" align="center" cellpadding="2"
							cellspacing="0" class="tabtable">
							<tr class="theader">
								<td width="10">&nbsp;</td>
								<td align="left" class="theaderBlue" width="194"><bean:message
										bundle="coi" key="label.protocolNumber" /></td>
								<td align="left" class="theaderBlue" width="156"><bean:message
										bundle="coi" key="label.status" /></td>
								<td align="left" class="theaderBlue" width="212"><bean:message
										bundle="coi" key="label.sponsor" /></td>
								<td align="left" class="theaderBlue" width="412"><bean:message
										bundle="coi" key="label.title" /></td>
							</tr>

							<%  int disclIndex = 0;
                                String strBgColor = "#DCE5F1";
                                
                                %>
							<logic:iterate id='protocol' name="protocolDisclosure"
								type="org.apache.commons.beanutils.DynaBean">
								<bean:define id='protocolNumber' name='protocol'
									property="moduleItemKey" type="java.lang.String" />
								<%                                  
                                    if (disclIndex%2 == 0) {
                                    strBgColor = "#D6DCE5";
                                    } else {
                                    strBgColor="#DCE5F1";
                                    }
                                    
                                    %>

								<tr bgcolor='<%=strBgColor%>' id="row<%=disclIndex%>"
									class="rowLine" onmouseover="className='rowHover rowLine'"
									onmouseout="className='rowLine'">
									<td width="10" height="21">&nbsp;</td>
									<td align="left" width="194"><coeusUtils:formatOutput
											name="protocol" property="moduleItemKey" /></td>
									<td align="left" width="156"><coeusUtils:formatOutput
											name="protocol" property="description" /></td>
									<td align="left" width="212"><coeusUtils:formatOutput
											name="protocol" property="sponsorName" /></td>
									<td align="left" width="412"><coeusUtils:formatOutput
											name="protocol" property="title" />
									<td>
								</tr>
							</logic:iterate>
						</table>
					</td>
				</tr>
			</logic:notEqual>
			<logic:equal name="protocolDiscSize" value="0">
				<tr>
					<td colspan="2">
						<table width="99%" border="0" align="center" cellpadding="2"
							cellspacing="0" class="tabtable">
							<tr>
								<td class="copybold"><bean:message bundle="coi"
										key="AnnualDisclosure.Protocols.NOList" /></td>
							</tr>
						</table>
					</td>
				</tr>
			</logic:equal>

			<tr>
				<td colspan="2" align="center" height="20px"><input
					type="submit" value="Continue Annual Disclosure"
					style="width: 200px" /></td>
			</tr>
		</form>
	</table>
</body>
</html>
