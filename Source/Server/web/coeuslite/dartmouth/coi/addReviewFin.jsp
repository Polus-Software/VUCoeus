<%@page contentType="text/html"
	import="edu.mit.coeus.utils.CoeusProperties,edu.mit.coeus.utils.CoeusPropertyKeys,
edu.mit.coeuslite.utils.ComboBoxBean"%>
<%@page pageEncoding="UTF-8" import="edu.mit.coeus.bean.*"%>
<%@page errorPage="ErrorPage.jsp"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/coeus-utils.tld" prefix="coeusUtils"%>
<%@ include file="/coeuslite/mit/utils/CoeusContextPath.jsp"%>
<jsp:useBean id="person" scope="session"
	class="edu.mit.coeus.bean.PersonInfoBean" />
<jsp:useBean id="disclFinEntDet" scope="session"
	class="java.util.Vector" />
<bean:size id="totentity" name="disclFinEntDet" />
<jsp:useBean id="finEntityReview" scope="request"
	class="org.apache.struts.validator.DynaValidatorForm" />
<jsp:useBean id="coiStatus" scope="session" class="java.util.Vector" />


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
   function changeAllStatus(){

	var conflictStatus = document.forms[0].conflictStatus.value;
	for(var elementIndex=0; elementIndex < document.forms[0].elements.length; elementIndex++){
			var elementName = document.forms[0].elements[elementIndex].name;
			if( (elementName.indexOf("sltConflictStatus") != -1)
				|| (elementName.indexOf("coiStatus") != -1) ){
				document.forms[0].elements[elementIndex].value=conflictStatus;
			}
	}	
}
        </script>
</head>
<body>
	<form name="finEntityReview" action='<%=path%>/updFinReview.do'>
		<%
            String moduleItemKey="";
 if(request.getAttribute("moduleItemKey")!=null){
     moduleItemKey=(String)request.getAttribute("moduleItemKey");
 }
 String title="";
 if(request.getAttribute("title")!=null){
     title=(String)request.getAttribute("title");
 }
 String moduleCode="";
 if(request.getAttribute("moduleCode")!=null){
     moduleCode=(String)request.getAttribute("moduleCode");
 }
            %>
		<table width="100%" align="center" border="0" cellpadding="0"
			cellspacing="0" class="table">
			<tr>
				<td class="copybold" colspan="2">Add Conflict status for <%=title%></td>
			</tr>
			<tr>
				<td class="copybold" colspan="2"><logic:present
						name="disclFinEntDet" scope="session">
						<%  String strBgColor = "#DCE5F1";
                            int disclIndex = 0;%>
						<table width="100%" align="center" border="0" cellpadding="0"
							cellspacing="0" class="table">
							<tr>
								<td class='copy' style='' colspan="3">&nbsp;&nbsp;<bean:message
										bundle="coi" key="label.setConflictStatus" />
									<%--Set conflict status for all entities to:--%> <select
									name="conflictStatus" class='textbox-long'
									onchange="changeAllStatus();">
										<option selected>-
											<bean:message bundle="coi" key="label.pleaseSelect" /><%-- Please Select ---%></option>
										<%
                                String description1 = "";
                                String code1 = "";
                                for(int i=0;i<coiStatus.size();i++){
                                    ComboBoxBean objCombBean = (ComboBoxBean)coiStatus.get(i);
                                    code1 = objCombBean.getCode();
                                    description1 = "";
                                    if( code1.equals("301") || code1.equals("200") ){
                                        description1 = objCombBean.getDescription();
                                %>
										<option value="<%=code1%>"><%=description1%></option>
										<%
                                    }
                                }
                                %>
								</select>
								</td>
							</tr>

							<tr class="theaderBlue">
								<td width="10px">&nbsp;</td>
								<td align="left" class="theaderBlue" width="507">Entity
									Name</td>
								<td align="left" class="theaderBlue" width="154">Status</td>
								<td align="left" class="theaderBlue" width="154">Description</td>
							</tr>
							<logic:notEqual name="totentity" value='0'>
								<logic:iterate id="finEntDet" name="disclFinEntDet"
									type="org.apache.commons.beanutils.DynaBean">
									<bean:define id="entityNumber" name="finEntDet"
										property="entityNumber" />
									<bean:define id="entityName" name="finEntDet"
										property="entityName" />
									<bean:define id="sequenceNum" name="finEntDet"
										property="sequenceNum" />
									<input type="hidden" name="entityNumber" value="entityNumber" />
									<%String seqNum="seqNum"+entityNumber;%>
									<input type="hidden" name="<%=seqNum%>" value="seqNum" />
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
										<td align="left">
											<%String coiStatusStr="coiStatus"+entityNumber;%> <select
											name="<%=coiStatusStr%>" class='textbox-long'
											onchange="changeAllStatus();">
												<option name="<%=coiStatusStr%>" selected>-
													<bean:message bundle="coi" key="label.pleaseSelect" /><%-- Please Select ---%></option>
												<%
                                
                                for(int i=0;i<coiStatus.size();i++){
                                    ComboBoxBean objCombBean = (ComboBoxBean)coiStatus.get(i);
                                    code1 = objCombBean.getCode();
                                    description1 = "";
                                    if( code1.equals("301") || code1.equals("200") ){
                                        description1 = objCombBean.getDescription();
                                %>
												<option name="<%=coiStatusStr%>" value="<%=code1%>"><%=description1%></option>
												<%
                                    }
                                }
                                %>
										</select>
										</td>
										<%String coiCmnt="coiCmnt"+entityNumber;%>
										<td><textarea name="<%=coiCmnt%>" rows="1" cols="25"
												style="height: 20px"></textarea></td>
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
			<tr>
				<td colspan="3" align="center"><input type="submit"
					value="Submit" /></td>
			</tr>
		</table>
		<html:hidden name="finEntityReview" property="moduleItemKey"
			value="<%=moduleItemKey%>" />
		<html:hidden name="finEntityReview" property="moduleCode"
			value="<%=moduleCode%>" />


	</form>
</body>
</html>
