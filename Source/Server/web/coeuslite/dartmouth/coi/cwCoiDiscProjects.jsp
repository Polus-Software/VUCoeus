<%@page contentType="text/html"
	import="edu.mit.coeus.utils.CoeusProperties,
edu.mit.coeus.utils.CoeusPropertyKeys,
java.util.ArrayList,
java.util.List,
org.apache.struts.validator.DynaValidatorForm,
java.util.Vector,
edu.dartmouth.coeuslite.coi.beans.ProposalBean,
edu.dartmouth.coeuslite.coi.beans.AwardBean,
edu.dartmouth.coeuslite.coi.beans.ProtocolBean,
edu.dartmouth.coeuslite.coi.beans.DisclosureBean,
edu.mit.coeuslite.utils.ComboBoxBean,
edu.mit.coeus.utils.DateUtils,
java.util.Calendar"%>
<%@page pageEncoding="UTF-8" import="edu.mit.coeus.bean.*"%>

<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/coeus-utils.tld" prefix="coeusUtils"%>
<%@ include file="/coeuslite/mit/utils/CoeusContextPath.jsp"%>
<jsp:useBean id="person" scope="session"
	class="edu.mit.coeus.bean.PersonInfoBean" />
<jsp:useBean id="proposalList" scope="session" class="java.util.Vector" />
<bean:size id="proposalDiscSize" name="proposalList" />
<jsp:useBean id="protocolList" scope="session" class="java.util.Vector" />
<bean:size id="protocolDiscSize" name="protocolList" />
<jsp:useBean id="awardList" scope="session" class="java.util.Vector" />
<bean:size id="awardDiscSize" name="awardList" />
<jsp:useBean id="dynaForm" scope="request"
	class="org.apache.struts.validator.DynaValidatorForm" />

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
   "http://www.w3.org/TR/html4/loose.dtd">

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>COI Projects</title>
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
		<tr class="theaderBlue">
			<td></td>
			<td align="right"><a href="javascript:self.close()">Close
					the window</a></td>
		</tr>

		<logic:present name="currDiscl" scope="session">
			<%DisclosureBean discl=(DisclosureBean)session.getAttribute("currDiscl");
                String statusStr="";
                %>
			<tr>
				<td width="100%" colspan="2"><table border="0" width="100%"
						cellpadding="0" cellspacing="0" class="tabtable">
						<logic:present name="DisclStatus" scope="session">
							<logic:iterate id="status" name="DisclStatus"
								type="edu.mit.coeuslite.utils.ComboBoxBean">
								<bean:define id="statuscode" name="status" property="code" />
								<logic:equal name='statuscode'
									value='<%=discl.getDisclosureStatusCode().toString()%>'>
									<bean:define id="statusDesc" name="status"
										property="description" />
									<%statusStr=(String)statusDesc;%>
								</logic:equal>
							</logic:iterate>
						</logic:present>
						<%--  <logic:notEqual name="disclSize" value='0'>--%>


						<%--  <logic:iterate id="discl" name="FinDiscl" type="edu.dartmouth.coeuslite.coi.beans.DisclosureBean" >--%>
						<bean:define id="dtUpdate" name="currDiscl"
							property="updateTimestamp" type="java.sql.Date" />
						<%long time=dtUpdate.getTime();
                    Calendar cal=Calendar.getInstance();
                    cal.setTimeInMillis(time);
                    DateUtils dtUtils=new DateUtils();
                    String dtStr=dtUtils.formatCalendar(cal,"MM/dd/yyyy hh:mm:ss a");
                        
                        String fullName="";
                        if(request.getAttribute("fullName")!=null){
                           fullName=(String)request.getAttribute("fullName");
                        }%>
						<tr bgcolor="#D6DCE5" bordercolor="#79A5F4" height="25px"
							id="row0">
							<td width="100" align="left" class="copybold">Person Name :</td>
							<td width='181px' align="left"><%=fullName%></td>
							<td width="100" align="left" class="copybold">DisclosureNumber
								:</td>
							<td width='181px' align="left"><%=discl.getCoiDisclosureNumber()%></td>
						</tr>
						<tr bgcolor="#D6DCE5" bordercolor="#79A5F4" height="25px"
							id="row0">
							<td width="100" height="20" align="left" class="copybold">Status
								:</td>
							<td width="181" height="21" align="left"><%=statusStr%></td>

							<td align="left" width="100" class="copybold">Last Updated :</td>
							<td align="left" width="180"><%=dtStr%></td>
						</tr>
						<tr bgcolor="#D6DCE5" bordercolor="#79A5F4" height="25px"
							id="row0">
							<td align="left" width="100" class="copybold">Expiration
								Date :</td>
							<td align="left" width="100"><coeusUtils:formatDate
									name="currDiscl" property="expirationDate" /></td>
							<td></td>
							<td></td>
						</tr>
					</table></td>
			</tr>
		</logic:present>


		<tr>
			<td height="26" class="theaderBlue" style="font-size: 14px"><bean:message
					bundle="coi" key="AnnualDisclosure.Proposals.Header" /></td>
			<td align="right" class="theaderBlue"></td>
		</tr>


		<%--
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
                           --%>
		<tr>
			<td height="24" colspan="2" align="left" class="copybold"
				style="font-size: 14px">Current Proposals :</td>
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
							<td align="left" class="theaderBlue" width="412"><bean:message
									bundle="coi" key="label.title" /></td>
							<td align="left" class="theaderBlue" width="212"><bean:message
									bundle="coi" key="label.sponsor" /></td>
							<td align="left" class="theaderBlue" width="156"><bean:message
									bundle="coi" key="label.proposalStartDate" /></td>
							<td align="left" class="theaderBlue" width="156"><bean:message
									bundle="coi" key="label.proposalEndDate" /></td>
							<td align="left" class="theaderBlue" width="156"><bean:message
									bundle="coi" key="label.totalCost" /></td>
							<td align="left" class="theaderBlue" width="156"><bean:message
									bundle="coi" key="label.Role" /></td>

							<td>&nbsp;</td>
						</tr>
						<%  int disclIndex = 0;
                                String strBgColor = "#DCE5F1";
                                
                                %>
						<logic:iterate id='proposal' name="proposalList"
							type="edu.dartmouth.coeuslite.coi.beans.ProposalBean">
							<bean:define id="proposalNumber" name="proposal"
								property="proposalNumber" />

							<%                                  
                                    if (disclIndex%2 == 0) {
                                    strBgColor = "#D6DCE5";
                                    } else {
                                    strBgColor="#DCE5F1";
                                    }
                                   // String proposalNumber=(String)proposal.get("proposalNumber");
                                    %>
							<tr bgcolor='<%=strBgColor%>' id="row<%=disclIndex%>"
								class="rowLine" onmouseover="className='rowHover rowLine'"
								onmouseout="className='rowLine'">
								<td width="10">&nbsp;</td>

								<td align="left" width="194"><coeusUtils:formatOutput
										name="proposal" property="proposalNumber" /></td>
								<td align="left" width="412"><coeusUtils:formatOutput
										name="proposal" property="title" /></td>
								<td align="left" width="212"><coeusUtils:formatOutput
										name="proposal" property="sponsor" /></td>
								<td align="left" width="156"><coeusUtils:formatOutput
										name="proposal" property="startDate" /></td>
								<td align="left" width="156"><coeusUtils:formatOutput
										name="proposal" property="endDate" /></td>
								<td align="left" width="156"><coeusUtils:formatOutput
										name="proposal" property="totalCost" /></td>
								<td align="left" width="156"><coeusUtils:formatOutput
										name="proposal" property="role" /></td>

								<td>&nbsp;</td>
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
			<td height="24" colspan="2" align="left" class="copybold"
				style="font-size: 14px">Current Awards :</td>
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
							<td align="left" class="theaderBlue" width="412"><bean:message
									bundle="coi" key="label.title" /></td>
							<td align="left" class="theaderBlue" width="212"><bean:message
									bundle="coi" key="label.sponsor" /></td>
							<td align="left" class="theaderBlue" width="156"><bean:message
									bundle="coi" key="label.effectiveDate" /></td>
							<td align="left" class="theaderBlue" width="156"><bean:message
									bundle="coi" key="label.finalExpDate" /></td>
							<td align="left" class="theaderBlue" width="156"><bean:message
									bundle="coi" key="label.oblCost" /></td>
							<td align="left" class="theaderBlue" width="156"><bean:message
									bundle="coi" key="label.antCost" /></td>
							<td align="left" class="theaderBlue" width="156"><bean:message
									bundle="coi" key="label.Role" /></td>

							<td>&nbsp;</td>
						</tr>

						<%  int disclIndex = 0;
                                String strBgColor = "#DCE5F1";
                                
                                %>
						<logic:iterate id='award' name="awardList"
							type="edu.dartmouth.coeuslite.coi.beans.AwardBean">
							<bean:define id='awardNumber' name='award' property="awardNumber"
								type="java.lang.String" />
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
								<td width="10">&nbsp;</td>
								<td align="left" width="194"><coeusUtils:formatOutput
										name="award" property="awardNumber" /></td>
								<td align="left" width="412"><coeusUtils:formatOutput
										name="award" property="title" /></td>
								<td align="left" width="212"><coeusUtils:formatOutput
										name="award" property="sponsor" /></td>
								<td align="left" width="156"><coeusUtils:formatOutput
										name="award" property="startDate" /></td>
								<td align="left" width="156"><coeusUtils:formatOutput
										name="award" property="expDate" /></td>
								<td align="left" width="156"><coeusUtils:formatOutput
										name="award" property="oblTotal" /></td>
								<td align="left" width="156"><coeusUtils:formatOutput
										name="award" property="antTotal" /></td>
								<td align="left" width="156"><coeusUtils:formatOutput
										name="award" property="role" /></td>

								<td>&nbsp;</td>
							</tr>
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
			<td height="24" colspan="2" align="left" class="copybold"
				style="font-size: 14px">Current Protocols :</td>
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
							<td align="left" class="theaderBlue" width="412"><bean:message
									bundle="coi" key="label.title" /></td>
							<td align="left" class="theaderBlue" width="156"><bean:message
									bundle="coi" key="label.status" /></td>
							<td align="left" class="theaderBlue" width="156"><bean:message
									bundle="coi" key="label.fundingSrc" /></td>
							<td align="left" class="theaderBlue" width="212"><bean:message
									bundle="coi" key="label.lastApprovalDate" /></td>
							<td align="left" class="theaderBlue" width="412"><bean:message
									bundle="coi" key="label.expDate" /></td>
							<td align="left" class="theaderBlue" width="212"><bean:message
									bundle="coi" key="label.role" /></td>
						</tr>
						<%  int disclIndex = 0;
                                String strBgColor = "#DCE5F1";                                
                                %>
						<logic:iterate id='protocol' name="protocolList"
							type="edu.dartmouth.coeuslite.coi.beans.ProtocolBean">
							<bean:define id='protocolNumber' name='protocol'
								property="protocolNumber" type="java.lang.String" />
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
										name="protocol" property="protocolNumber" /></td>
								<td align="left" width="156"><coeusUtils:formatOutput
										name="protocol" property="title" /></td>
								<td align="left" width="156"><coeusUtils:formatOutput
										name="protocol" property="status" /></td>
								<td align="left" width="156"><coeusUtils:formatOutput
										name="protocol" property="fundingSource" /></td>
								<td align="left" width="212"><coeusUtils:formatOutput
										name="protocol" property="lastApprDate" /></td>
								<td align="left" width="212"><coeusUtils:formatOutput
										name="protocol" property="expDate" /></td>
								<td align="left" width="412"><coeusUtils:formatOutput
										name="protocol" property="role" /></td>
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


		<tr class="theaderBlue">
			<td></td>
			<td align="right"><a href="javascript:self.close()">Close
					the window</a></td>
		</tr>

		</form>
	</table>
</body>
</html>
