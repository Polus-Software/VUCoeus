<%@page contentType="text/html"
	import="edu.mit.coeus.utils.CoeusProperties,edu.mit.coeus.utils.CoeusPropertyKeys,java.util.ArrayList,java.util.List,
org.apache.struts.validator.DynaValidatorForm,
java.util.Vector,
java.lang.Integer,
java.util.HashMap,
edu.mit.coeus.utils.DateUtils,
java.util.Calendar,
edu.dartmouth.coeuslite.coi.beans.DisclosureBean,
org.apache.struts.action.DynaActionForm,
java.util.List,edu.mit.coeuslite.utils.CoeusDynaBeansList"%>
<%@page pageEncoding="UTF-8" import="edu.mit.coeus.bean.*"%>
<%@page errorPage="ErrorPage.jsp"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/coeus-utils.tld" prefix="coeusUtils"%>
<%@ include file="/coeuslite/mit/utils/CoeusContextPath.jsp"%>
<jsp:useBean id="person" scope="session"
	class="edu.mit.coeus.bean.PersonInfoBean" />
<jsp:useBean id="PIFinEnt" scope="session" class="java.util.Vector" />
<bean:size id="totentity" name="PIFinEnt" />
<html>
<head>
<%String path = request.getContextPath();
    String policyPath=CoeusProperties.getProperty(CoeusPropertyKeys.POLICY_FILE);
    String statusStr="";%>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>COI</title>
<link href="<%=path%>/coeuslite/dartmouth/utils/css/coeus_styles.css"
	rel="stylesheet" type="text/css">
<script src="<%=path%>/coeuslite/dartmouth/utils/scripts/divSlide.js"></script>
<script src="<%=path%>/coeuslite/dartmouth/utils/scripts/Balloon.js"></script>
<script>
    function openFinEntity(entityNumber,sequenceNum) {
      var url = "<%=path%>/reviewAnnFinEntityHist.do?entityNumber="+entityNumber+"&seqNum="+sequenceNum+"&header=no&review=review";
      window.open(url, "History", "scrollbars=1,resizable=yes, width=1000, height=700, left=100, top = 100");
            }
    function openConflictStatus(personId,fullName) {
                var url1 = "<%=path%>/updateConflictStatus.do?personId="+personId+"&&fullName="+fullName;
                window.open(url1, "History", "scrollbars=1,resizable=yes, width=750, height=400, left=200, top = 100");
            }
    </script>
<body>
	<table align="center" width='100%' border="0" cellpadding="0"
		cellspacing="0" class="table">
		<%
             String slctPerson="";
             String fullName="";
            if(request.getAttribute("SelectedPerson")!=null){
           slctPerson=(String)request.getAttribute("SelectedPerson");
            fullName=(String)request.getAttribute("FullName");
            }%>
		<logic:present name="PIFinEnt" scope="session">
			<% String strBgColor = "#DCE5F1";
                int disclIndex = 0;%>


			<logic:notEqual name="totentity" value='0'>
				<tr>
					<td colspan="2">List of Financial Entities for <%=fullName%>:
					</td>
					<td colspan="3" align="right"><a
						href="javascript:openConflictStatus('<%=slctPerson%>','<%=fullName%>');">Update
							Conflict Status</a></td>
				</tr>
				<tr class="theaderBlue">
					<td height="20px" width="50px">&nbsp;</td>
					<td align="left" class="theader" width="310">Entity Name</td>
					<td align="left" class="theader" width="200">Conflict Status</td>
					<td align="left" class="theader" width="100">Reviewed by</td>
					<td align="left" class="theader" width="87">Last Update</td>
				</tr>
				<logic:iterate id="finEntDet" name="PIFinEnt"
					type="org.apache.commons.beanutils.DynaBean">
					<bean:define id="entityNumber" name="finEntDet"
						property="entityNumber" />
					<bean:define id="entitySeqNumber" name="finEntDet"
						property="entitySequenceNumber" />
					<%                                  
                        if (disclIndex%2 == 0) {
                        strBgColor = "#D6DCE5";
                        } else {
                        strBgColor="#DCE5F1";
                        }
                        %>
					<tr bgcolor='<%=strBgColor%>' class="rowLine"
						onmouseover="className='rowHover rowLine'"
						onmouseout="className='rowLine'">
						<td width='50px'><a
							href="javascript:openFinEntity('<%=entityNumber%>','<%=entitySeqNumber%>');">View</a></td>
						<td class="copy"><coeusUtils:formatOutput name="finEntDet"
								property="entityName" /> </a></td>
						<td class="copy"><coeusUtils:formatOutput name="finEntDet"
								property="coiStatus" /> </a></td>
						<td class="copy"><coeusUtils:formatOutput name="finEntDet"
								property="reviewer" /> </a></td>
						<td class="copy"><coeusUtils:formatOutput name="finEntDet"
								property="updtimestamp" /> </a></td>
					</tr>
					<% disclIndex++;%>
				</logic:iterate>
			</logic:notEqual>

			<logic:equal name="totentity" value='0'>
				<tr>
					<td colspan="3"><bean:message bundle="coi"
							key="Annualdisclosure.Review.noFE" /></td>
				</tr>
			</logic:equal>
		</logic:present>
		<logic:notPresent name="PIFinEnt" scope="session">
			<tr>
				<td colspan="3"><bean:message bundle="coi"
						key="Annualdisclosure.Review.noFE" /></td>
			</tr>
		</logic:notPresent>
	</table>
</body>
</html>