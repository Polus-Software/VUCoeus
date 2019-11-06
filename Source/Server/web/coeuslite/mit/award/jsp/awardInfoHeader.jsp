<%-- 
    Document   : awardHeader
    Created on : Jul 28, 2011, 11:10:58 AM
    Author     : indhulekha
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
   "http://www.w3.org/TR/html4/loose.dtd">
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ include file="/coeuslite/mit/utils/CoeusContextPath.jsp"%>
<%@page
	import="java.util.Vector,edu.mit.coeuslite.coiv2.services.CoiCommonService,edu.mit.coeuslite.award.beans.AwardDisplayBean;"%>

<html:html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Award Header</title>
</head>
<body>
	<%
               String title = "";
               String accountNum = "";
               String acountType = "";

               if(request.getAttribute("awardListtmp") != null) {
                   AwardDisplayBean displayBean = (AwardDisplayBean)request.getAttribute("awardListtmp");
                     title = displayBean.getTitle().toString();

                  if(title.length() > 45) {
                       title = title.substring(0,45)+"...";
                    }
                       if(title==null) {
                        title = "";
                     }
                    accountNum = displayBean.getAccntNumber();
                    acountType = displayBean.getAccountType();

                   if(accountNum==null) {
                        accountNum = "";
                     }

                    if(acountType== null) {
                        acountType = "";
                     }
                }
               %>
	<table width="100%" border="0" cellpadding="2" cellspacing="2"
		class="table">
		<logic:present name="awardListtmp">


			<tr>
				<td align="right" class='copybold' nowrap width="10%">Account
					No. :</td>
				<td class='copy' nowrap width="30%">&nbsp; <logic:notEmpty
						name="awardListtmp" property="accntNumber">
						<bean:write name="awardListtmp" property="accntNumber" />
					</logic:notEmpty> (<logic:notEmpty name="awardListtmp" property="status">
						<bean:write name="awardListtmp" property="status" />
					</logic:notEmpty>)
				</td>
				<td class='copybold' nowrap width="10%" align="right">Award No.
					:</td>
				<td class='copy' width="30%" nowrap>&nbsp;<logic:notEmpty
						name="awardListtmp" property="awardNumber">
						<bean:write name="awardListtmp" property="awardNumber" />
					</logic:notEmpty>
				</td>

			</tr>
			<tr>
				<td class='copybold' nowrap width="10%" align="right">Sponsor:</td>
				<td class='copy' nowrap width="30%">&nbsp; <logic:notEmpty
						name="awardListtmp" property="sponsor">
						<bean:write name="awardListtmp" property="sponsor" />
					</logic:notEmpty> &nbsp;<logic:notEmpty name="awardListtmp" property="sponsorNum">(<bean:write
							name="awardListtmp" property="sponsorNum" />)</logic:notEmpty>
				</td>
				<td align="right" class='copybold' nowrap width="10%">Activity
					Type :</td>
				<td class='copy' nowrap width="30%">&nbsp; <logic:notEmpty
						name="awardListtmp" property="activityType">
						<bean:write name="awardListtmp" property="activityType" />
					</logic:notEmpty>
				</td>
			</tr>
			<tr>
				<td class='copybold' nowrap width="10%" align="right">Sponsor
					Award No.:</td>
				<td class='copy' nowrap width="30%">&nbsp;<logic:notEmpty
						name="awardListtmp" property="sponsorNo">
						<bean:write name="awardListtmp" property="sponsorNo" />
					</logic:notEmpty></td>
				<td align="right" class='copybold' nowrap width="10%">Award
					Type :</td>
				<td class='copy' nowrap width="30%">&nbsp;<logic:notEmpty
						name="awardListtmp" property="awardtype">
						<bean:write name="awardListtmp" property="awardtype" />
					</logic:notEmpty></td>
			</tr>

			<tr>
				<td class='copybold' nowrap width="10%" align="right">Prime
					Sponsor:</td>
				<td class='copy' nowrap width="30%">&nbsp;<logic:notEmpty
						name="awardListtmp" property="primeSponsor">
						<bean:write name="awardListtmp" property="primeSponsor" />
					</logic:notEmpty> <logic:notEmpty name="awardListtmp" property="primeSponsorNo">(<bean:write
							name="awardListtmp" property="primeSponsorNo" />)</logic:notEmpty>
				</td>
				<td align="right" class='copybold' nowrap width="10%">Account
					Type :</td>
				<td class='copy' nowrap width="30%">&nbsp;<logic:notEmpty
						name="awardListtmp" property="accountType">
						<bean:write name="awardListtmp" property="accountType" />
					</logic:notEmpty></td>
			</tr>
			<tr>
				<td class='copybold' nowrap width="10%" align="right">Title :</td>
				<td class='copy' nowrap>&nbsp;<%=title%></td>
				<td align="right" class='copybold' nowrap width="10%">Lead
					Unit:</td>
				<td class='copy' nowrap width="30%"><logic:present
						name="leadUnit">
                       
                       &nbsp;<bean:write name="leadUnit"
							property="unitNumber" /> - <bean:write name="leadUnit"
							property="unitName" />

					</logic:present></td>
			</tr>
		</logic:present>
	</table>
</body>
</html:html>
