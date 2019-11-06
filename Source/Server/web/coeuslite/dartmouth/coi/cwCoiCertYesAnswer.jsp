<%@page contentType="text/html"
	import="edu.mit.coeus.utils.CoeusProperties,edu.mit.coeus.utils.CoeusPropertyKeys,org.apache.struts.action.Action"%>
<%@page pageEncoding="UTF-8" import="edu.mit.coeus.bean.*"%>
<%@page errorPage="ErrorPage.jsp"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/coeus-utils.tld" prefix="coeusUtils"%>
<%@ include file="/coeuslite/mit/utils/CoeusContextPath.jsp"%>
<jsp:useBean id="person" scope="session"
	class="edu.mit.coeus.bean.PersonInfoBean" />
<jsp:useBean id="disclFinEnt" scope="session" class="java.util.Vector" />
<bean:size id="totentity" name="disclFinEnt" />
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
</head>
<body>
	<form name="finEntityData" action="/deactivateAnnFinEnt.do">
		<table width="980" align="center" border="0" cellpadding="0"
			cellspacing="0" class="table">
			<tr>
				<td height="23" class="theader" style="font-size: 14px" colspan="2"><bean:message
						bundle="coi" key="AnnualDisclosure.FinacialEntities.Header" /></td>
			</tr>
			<tr valign="middle">
				<td height="93" bgcolor="#D6DCE5" class="rowLine"
					onmouseover="className='rowHover rowLine'"
					onmouseout="className='rowLine'" colspan="2"><bean:message
						bundle="coi" key="AnnualDisclosure.FinacialEntities.Text" /></td>
			</tr>
			<tr class="theaderBlue">
				<td height="25" align="left" class="theader" style="font-size: 13px">Financial
					Entities for <%=person.getFullName()%>
				</td>
				<td align="right"><a
					href='<%=path%>/getAnnDisclFinEntity.do?actionFrom=coiDiscl'><u><bean:message
								bundle="coi" key="AnnualDisclosure.FinacialEntities.AddNewFE" /></u></a>&nbsp;&nbsp;
					<logic:notEqual name="totentity" value='0'>
						<img src='<%=path%>/coeusliteimages/seperator.gif'
							align="absbottom">
						<!-- Modified for Case#4447 -  Next phase of COI enhancements - Start 
                         <a href='<%=path%>/updDisclFE.do'><bean:message bundle="coi" key="AnnualDisclosure.FinacialEntities.ContinueDiscl"/>&nbsp;&nbsp;</a></td></tr> -->
						<a href='<%=path%>/annDisclosure.do'><u><bean:message
									bundle="coi"
									key="AnnualDisclosure.FinacialEntities.ContinueDiscl" /></u></a>&nbsp;&nbsp;</td>
			</tr>
			</logic:notEqual>
			</td>
			</tr>
			<tr>
				<td class="copybold" colspan="2"><bean:message bundle="coi"
						key="AnnualDisclosure.FinacialEntities.listHeader" /></td>
			</tr>
			<tr>
				<td class="copybold" colspan="2"><logic:present
						name="disclFinEnt" scope="session">
						<%  String strBgColor = "#DCE5F1";
                            int disclIndex = 0;%>
						<table width="100%" align="center" border="0" cellpadding="0"
							cellspacing="0" class="table">
							<tr class="theaderBlue">
								<td width="10px">&nbsp;</td>
								<td align="left" class="theader" width="507">Entity Name</td>
								<td align="left" class="theader" width="154">Status</td>
								<td align="left" class="theader" width="87">&nbsp;</td>
							</tr>
							<logic:notEqual name="totentity" value='0'>
								<logic:iterate id="finEntDet" name="disclFinEnt"
									type="org.apache.commons.beanutils.DynaBean">
									<bean:define id="entityNumber" name="finEntDet"
										property="entityNumber" />
									<bean:define id="entityName" name="finEntDet"
										property="entityName" />
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
										<td height='20px'>&nbsp;</td>
										<td class="copy"><a
											href='<%=path%>/reviewAnnFinEntity.do?entityNumber=<%=entityNumber%>&actionFrom=coiDiscl'><coeusUtils:formatOutput
													name="finEntDet" property="entityName" /> </a></td>
										<td class="copy"><a
											href='<%=path%>/reviewAnnFinEntity.do?entityNumber=<%=entityNumber%>&actionFrom=coiDiscl'><coeusUtils:formatOutput
													name="finEntDet" property="statusDesc" /> </a></td>
										<td class="copy">[ <logic:equal name="finEntDet"
												property="statusCode" value='1'>

												<a
													href='<%=path%>/deactivateAnnFinEnt.do?entityNumber=<%=entityNumber%>&actionFrom=coiDiscl'>
													<%-- 
                                                    Modified for Case#4049 - "Remove" Entity label should be changed  - Start
                                                    <bean:message bundle="coi" key="financialEntity.removeLink"/></a> --%>
													<bean:message bundle="coi"
														key="financialEntity.makeInactive" />
												</a>
												<%--Case#4049 - End--%>
											</logic:equal> <logic:notEqual name="finEntDet" property="statusCode"
												value='1'>
												<a
													href='<%=path%>/activateAnnFinEnt.do?entityNumber=<%=entityNumber%>&actionFrom=coiDiscl'>
													<bean:message bundle="coi"
														key="financialEntity.makeActiveLink" />
												</a>
											</logic:notEqual> ] &nbsp;&nbsp;
										</td>
									</tr>
								</logic:iterate>
							</logic:notEqual>

							<logic:equal name="totentity" value='0'>
								<tr>
									<td colspan="3">There is no finanacial entities</td>
								</tr>
							</logic:equal>
						</table>
					</logic:present></td>
			</tr>
			<tr class="theaderBlue">
				<td height="25" align="left" class="theaderBlue"
					style="font-size: 13px">&nbsp;&nbsp;<a
					href="<%=path%>/createDisclosure.do?operation=UPDATE"><u>Back</u></a>
				</td>
				<td align="right"><a
					href='<%=path%>/getAnnDisclFinEntity.do?actionFrom=coiDiscl'><u><bean:message
								bundle="coi" key="AnnualDisclosure.FinacialEntities.AddNewFE" /></u></a>&nbsp;&nbsp;
					<logic:notEqual name="totentity" value='0'>
						<img src='<%=path%>/coeusliteimages/seperator.gif'
							align="absbottom">
						<!--Case#4447 -  Next phase of COI enhancements - Start 
                            <a href='<%=path%>/updDisclFE.do'>-->
						<a href='<%=path%>/annDisclosure.do'> <u><bean:message
									bundle="coi"
									key="AnnualDisclosure.FinacialEntities.ContinueDiscl" /></u>
						</a>&nbsp;&nbsp;
                        </logic:notEqual></td>
			</tr>
		</table>
	</form>

	<%
if(request.getAttribute("FESubmitSuccess") != null){
        request.removeAttribute("FESubmitSuccess");
        
        String entityName = (String)request.getAttribute("entityName");
        String actionType = (String)request.getAttribute("actionType");
        String reloadLocation = request.getContextPath()+"/disclFinEntity.do";
     //   MessageResources messageResources = getResources(request);
       String key="";
        //messageResources.getMessage("AnnualDisclosure.FinancialEntity.Add");
      if(actionType != null){
            if(actionType.equals("I")){
               key="AnnualDisclosure.FinancialEntity.Add";
            } else if(actionType.equals("U") ){
                key="AnnualDisclosure.FinancialEntity.Edit";
            } else if(actionType.equals("activate") ){
                 key="AnnualDisclosure.FinancialEntity.Active";
            } else if(actionType.equals("deactivate") ){
                 key="AnnualDisclosure.FinancialEntity.Inactive";
            }
        }
   /*     int indexOfApost = message.indexOf("'");
        if(indexOfApost != -1){
            int indexOfApostIncr = indexOfApost+1;
            int endOfMessage = message.length();
            String message1 = message.substring(0, indexOfApost);
            String message2 = message.substring(indexOfApostIncr, message.length());
            message = message1+message2;
        }*/
     /*   out.print("<script language='javascript'>");
        out.print("alert('<bean:message bundle="coi" key="financialEntity.makeActiveLink"/>');");
//out.print("openwintext('FESubmitSuccess.jsp', 'getFinEnt3');");//can't use because of popup blockers
        out.print("window.location='"+reloadLocation+"';");
        out.print("</script>");*/%>
	<script language='javascript'>
            alert('The Entity "<bean:message bundle="coi" key="AnnualDisclosure.FinancialEntity.Entity" arg0='<%=entityName%>'/>" <bean:message bundle="coi" key='<%=key%>'/>');
            window.location='<%=reloadLocation%>';
        </script>
	<%}

%>
</body>
</html>
