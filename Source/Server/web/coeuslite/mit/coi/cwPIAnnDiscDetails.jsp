<%@ page
	import="org.apache.struts.validator.DynaValidatorForm,
java.util.Vector,edu.mit.coeuslite.utils.ComboBoxBean,org.apache.struts.taglib.html.JavascriptValidatorTag"%>
<%@ page import="java.util.*"%>

<%@page pageEncoding="UTF-8"%>

<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/coeus-utils.tld" prefix="coeusUtils"%>
<jsp:useBean id="reviewDisclosureList" scope="request"
	class="java.util.Vector" />
<jsp:useBean id="person" scope="session"
	class="edu.mit.coeus.bean.PersonInfoBean" />
<bean:size id="disclosureSize" name="reviewDisclosureList" />
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
"http://www.w3.org/TR/html4/loose.dtd">

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<%String path = request.getContextPath();%>
<link rel=stylesheet
	href="<%=path%>/coeuslite/mit/utils/css/coeus_styles.css"
	type="text/css">
<title>Annual Disclosure Details</title>
<script>
            function rowHover(rowId, styleName) {
                elemId = document.getElementById(rowId);
                elemId.style.cursor = "hand";
                //If row is selected retain selection style
                //Apply hover Style only if row is not selected
                
                    elemId.className = styleName;
                
            }
            
        </script>
</head>
<body class="table">
	<b>Related Disclosures:</b>
	<%
                    org.apache.commons.beanutils.DynaBean tempBean = (org.apache.commons.beanutils.DynaBean)reviewDisclosureList.get(0);
                    String tempKey = (String)tempBean.get("module");
                    //out.print(disclosureSize +" - "+tempKey);
                    
                    List awardDisclosure, proposalDisclosure;
                    awardDisclosure = new ArrayList();
                    proposalDisclosure = new ArrayList();
                    String strAward = "Award";
                    for(int index = 0; index < reviewDisclosureList.size(); index++) {
                        tempBean = (org.apache.commons.beanutils.DynaBean)reviewDisclosureList.get(index);
                        tempKey = (String)tempBean.get("module");
                        if(tempKey != null && tempKey.equalsIgnoreCase(strAward)) {
                            //Award Disclosure
                            awardDisclosure.add(tempBean);
                        }//End IF
                        else {
                            //Proposal Disclosure
                            proposalDisclosure.add(tempBean);
                        }//End ELSE
                    }%>


	<table width="100%" align="center" border="0" cellspacing="0"
		cellpadding="3" class="tabtable">
		<tr>
			<td width="5%" align="left" class="theader" nowrap>&nbsp;</td>
			<td width="10%" align="left" class="theader" nowrap><bean:message
					bundle="coi" key="label.disclosureNumber" /></td>
			<td width="10%" align="left" class="theader" nowrap><bean:message
					bundle="coi" key="label.status" /></td>
			<td width="10%" align="left" class="theader" nowrap><bean:message
					bundle="coi" key="label.appliesTo" /></td>
			<td width="10%" align="left" class="theader" nowrap><bean:message
					bundle="coi" key="label.moduleItemKey" /></td>
			<td width="10%" align="left" class="theader" nowrap><bean:message
					bundle="coi" key="label.sponsor" /></td>
			<td width="20%" align="left" class="theader" nowrap><bean:message
					bundle="coi" key="label.title" /></td>
			<td width="15%" align="left" class="theader" nowrap><bean:message
					bundle="coi" key="label.lastUpdated" /></td>
			<td width="10%" align="left" class="theader" nowrap><bean:message
					bundle="coi" key="label.userName" /></td>
		</tr>
		<%String discNum, reqDiscNum;%>
		<logic:present name="reviewDisclosureList" scope="request">
			<logic:iterate id="data" name="ReviewDisclosureHistory"
				type="org.apache.commons.beanutils.DynaBean" indexId="ctr">
				<%
                discNum = (String)data.get("disclNo");
                reqDiscNum = (String)request.getParameter("disclosureNum");
                if(reqDiscNum != null && !reqDiscNum.startsWith(discNum)) {
                %>

				<tr class="rowLine" onmouseover="className='rowHover rowLine'"
					onmouseout="className='rowLine'">
					<td class="copy"><a target="_parent"
						href="viewCOIDisclosureDetails.do?disclosureNo=<bean:write name="data" property="disclNo"/>">view</a>
					</td>
					<td class="copy"><bean:write name="data" property="disclNo" />
					</td>
					<td class="copy"><bean:write name="data" property="coiStatus" />
					</td>
					<td class="copy"><bean:write name="data" property="module" />
					</td>
					<td class="copy"><bean:write name="data"
							property="moduleItemKey" /></td>
					<td class="copy"><bean:write name="data"
							property="sponsorName" /></td>
					<td class="copy"><bean:write name="data" property="title" /></td>
					<td nowrap><bean:write name="data" property="updtimestamp" />
					</td>
					<td><bean:write name="data" property="upduser" /></td>
				</tr>

				<%}%>

			</logic:iterate>
		</logic:present>
	</table>

</body>

</html>
