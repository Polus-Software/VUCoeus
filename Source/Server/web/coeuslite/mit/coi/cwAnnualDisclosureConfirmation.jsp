<%@ page import="org.apache.struts.validator.DynaValidatorForm"%>
<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/coeus-utils.tld" prefix="coeusUtils"%>
<%@ include file="/coeuslite/mit/utils/CoeusContextPath.jsp"%>
<%@ taglib uri="/WEB-INF/coeus-utils.tld" prefix="coeusUtils"%>

<html:html>

<head>
<title>JSP Page</title>
<script>
        function annualDiscReview() {
            window.location="<%=request.getContextPath()%>/getAnnDiscPendingFEs.do";
        }
    </script>
</head>
<body>
	<html:form action="/annualDisclsFinalUpdate.do">
		<table width="980" height="100%" border="0" cellpadding="0"
			cellspacing="0" class="table">


			<!--  - Start  -->
			<tr>
				<td height="119" align="left" valign="top">
					<table width="99%" border="0" align="center" cellpadding="0"
						cellspacing="0" class="tabtable">
						<%--<tr>
            <td colspan="4" align="left" valign="top">
                <table width="100%" height="20"  border="0" cellpadding="0" cellspacing="0" class="theader">
                <tr>
                  <td class="theader"> 
                       <bean:message bundle="coi" key="label.annDisclConfUpd" /> 
                  </td>
                </tr>
            </table></td>
          </tr>--%>
						<tr class='copy' align="center">
							<td><br> <%--<b> <bean:message bundle="coi" key="label.annDisclConfSubmit" /></b>--%>
							</td>
						</tr>
						<tr>
							<td>&nbsp;&nbsp;Select &#147;Review Annual Disclosure&#148;
								to Review and start-over all of your entities and disclosures
								before submitting.<br> &nbsp;&nbsp;Select &#147;Submit
								Annual Disclosure&#148; to Submit to disclosure process.<br>
							<br>
							</td>
						</tr>
						<tr align="left">
							<td>&nbsp;&nbsp;<html:button property="review"
									value="Review Annual Disclosure" onclick="annualDiscReview()" />
								<html:submit property="save" value="Submit Annual Disclosure" />
							</td>
						</tr>
						<tr>
							<td>&nbsp;</td>
						</tr>

					</table>
				</td>
			</tr>
			<!--  - End  -->
			<tr>
				<td height='10'>&nbsp;</td>
			</tr>
		</table>



	</html:form>
</body>

</html:html>
