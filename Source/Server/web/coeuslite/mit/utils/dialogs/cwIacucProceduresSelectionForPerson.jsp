<%@ page contentType="text/html;charset=UTF-8" language="java"
	import="org.apache.struts.action.DynaActionForm,edu.mit.coeuslite.utils.CoeusLiteConstants;"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ include file="/coeuslite/mit/utils/CoeusContextPath.jsp"%>
<jsp:useBean id="iacucProcedurePersons" scope="session"
	class="edu.mit.coeuslite.utils.CoeusDynaFormList" />
<jsp:useBean id="vecAddedSpeciesData" scope="session"
	class="java.util.Vector" />

<html:html locale="true">
<head>
<title>Procedures</title>
<html:base />
<link
	href="<bean:write name='ctxtPath'/>/coeuslite/mit/utils/css/ipf.css"
	rel="stylesheet" type="text/css">
<link
	href="<bean:write name='ctxtPath'/>/coeuslite/mit/utils/css/coeus_styles.css"
	rel="stylesheet" type="text/css">
<script>
    function formSubmit(personId){
        document.iacucProtoStudyGroupsDynaBeansList.action = "<%=request.getContextPath()%>/addRemoveProceduresForPerson.do?personId="+personId;
        document.iacucProtoStudyGroupsDynaBeansList.submit();
    }
    
    function selectAllProcedures(form,procedure){
        var searchShowHeaderElem = document.getElementsByTagName("input");
        for(var i = 0; i < searchShowHeaderElem.length; i++) {
            var name = searchShowHeaderElem[i].name;
            var showHeaderReg = new RegExp(procedure);
            if(showHeaderReg.exec(name) != null){
             if(form.checked){
                searchShowHeaderElem[i].checked = 'true';
             }else{
                searchShowHeaderElem[i].checked = '';
             }
            }
        }
    }
    
     function hideProcedures(procedureCat){
        var searchShowHeaderElem = document.getElementsByTagName("div");
        for(var i = 0; i < searchShowHeaderElem.length; i++) {
            var id = searchShowHeaderElem[i].id;
            var showHeaderReg = new RegExp("showHeaderFor"+procedureCat);
            var hideHeaderReg = new RegExp("hideHeaderFor"+procedureCat);
            var procedureListReg = new RegExp(procedureCat+"ProcedureList");
            if(showHeaderReg.exec(id) != null){
             searchShowHeaderElem[i].style.display = "block";
            }
            if(hideHeaderReg.exec(id) != null){
             searchShowHeaderElem[i].style.display = "none";
            }

            if(procedureListReg.exec(id) != null){
                searchShowHeaderElem[i].style.display = "none";
            }

        }
    }
    
 
    
</script>
</head>
<body bgcolor="#376DAA">
	<%String plus = request.getContextPath()+"/coeusliteimages/plus.gif";
    String minus = request.getContextPath()+"/coeusliteimages/minus.gif";
    String selectedPersonId = (String)request.getAttribute("personId");
    String mode = request.getParameter("mode");
    boolean modeValue=false;
    if(CoeusLiteConstants.DISPLAY_MODE.equalsIgnoreCase(mode)){
        modeValue = true;
    }else if(CoeusLiteConstants.MODIFY_MODE.equalsIgnoreCase(mode)){
        modeValue=false;
    }else if(CoeusLiteConstants.ADD_MODE.equalsIgnoreCase(mode)){
        modeValue=false;
    }
 
    %>
	<html:form action="/addRemoveProceduresForPerson.do" method="post">
		<script>
        <%
        String windowCLose = (String)request.getAttribute("WINDOW_CLOSE");
        if("Y".equals(windowCLose)){
        String isDataModified = (String)request.getAttribute("isDataModified");
        if(isDataModified == null){
            isDataModified = "N";
        }
        %>
        opener.closeSelection('<%=isDataModified%>')
        <%}%>
    </script>
		<table width="100%" border="0" cellpadding="0" cellspacing="0"
			class="tabtable">
			<tr>
				<td height='10' colspan="2">
					<table width="100%" border="0" cellpadding="0" cellspacing="0"
						class="tableheader">
						<tr valign="top">
							<td>
								<%if(!modeValue){%> <bean:message bundle="iacuc"
									key="studyGroup.msg.procedureSelectForPerson" /><%=request.getAttribute("personName")%>
								<%}else{%> <bean:message bundle="iacuc"
									key="studyGroup.msg.procedureForPerson" /><%=request.getAttribute("personName")%>
								<%}%>
							</td>
						</tr>
					</table>
				</td>

			</tr>

			<tr>
				<td>
					<table width="100%" border="0" cellpadding="2" cellspacing="0"
						class="tabtable" align="center">
						<logic:present name="vecAddedSpeciesData" scope="session">
							<%int procedureIndex = 0;%>
							<logic:iterate id="speciesGroupForm" name="vecAddedSpeciesData"
								type="org.apache.struts.action.DynaActionForm" indexId="index"
								scope="session">
								<%
                            int speciesId = ((Integer)speciesGroupForm.get("speciesId")).intValue();
                            String strSpeciesId = speciesId+"";
                            boolean isAllProcedureSelected = true;
                            %>
								<tr class="tabtable">
									<td class="copybold" width="5%"><bean:message
											bundle="iacuc" key="label.groupSpecies.group" /></td>
									<td class="copy"><html:text name="speciesGroupForm"
											size="40" disabled="true" property="groupName" /></td>
									<td class="copybold" width="13%"><bean:message
											bundle="iacuc" key="label.groupSpecies.species" /></td>
									<td class="copybold"><html:text name="speciesGroupForm"
											size="40" disabled="true" property="speciesName" /></td>
								</tr>


								<logic:present name="iacucProcedurePersons" scope="session"
									property="infoList">
									<logic:iterate id="personDetails" name="iacucProcedurePersons"
										property="infoList"
										type="org.apache.struts.action.DynaActionForm" scope="session">
										<%String personId = (String)personDetails.get("personId");
                                        int indexColor = 1;
                                    %>
										<logic:present name="personDetails"
											property="personResponsibleList">
											<logic:iterate id="personResponsible" name="personDetails"
												property="personResponsibleList"
												type="org.apache.struts.action.DynaActionForm">
												<%
                                            String strBgColor = "#D6DCE5";
                                            if (indexColor%2 == 0) {
                                                strBgColor = "#DCE5F1";
                                            }
                                            int procSpeciesId = ((Integer)personResponsible.get("speciesId")).intValue();
                                            if(selectedPersonId != null && selectedPersonId.equals(personId)
                                            && speciesId == procSpeciesId){

                                            %>
												<%if(indexColor == 1){%>
												<tr bgcolor="<%=strBgColor%>" colspan="5"
													onmouseover="className='TableItemOn'"
													onmouseout="className='TableItemOff'">
													<td class="copybold" colspan="4">
														<%indexColor++;
                                                    String selectAllLink = "javaScript:selectAllProcedures(this,'"+("personResponsible"+speciesId)+"')";
                                                    %> <html:checkbox
															name="personResponsible" styleId="<%=strSpeciesId%>"
															styleClass="copy" indexed="true"
															disabled="<%=modeValue%>"
															property="isAllProcedureSelected"
															onclick="<%=selectAllLink%>" /> <bean:message
															bundle="iacuc" key="studyGroup.label.allProcedures" />
													</td>
												</tr>

												<%}%>
												<%
                                            if (indexColor%2 == 0) {
                                                strBgColor = "#DCE5F1";
                                            }
                                            %>
												<tr bgcolor="<%=strBgColor%>" colspan="5"
													onmouseover="className='TableItemOn'"
													onmouseout="className='TableItemOff'">
													<td class="copybold" colspan="4">
														&nbsp;&nbsp;&nbsp;&nbsp; <%
                                                    String checkBoxName = "personResponsible"+speciesId+procedureIndex;
                                                    String isProcedureSelected = (String)personResponsible.get("isProcedureSelected");
                                                    if("on".equals(isProcedureSelected)){
                                                        if(!modeValue){
                                                    %> <input
														type="checkbox" name="<%=checkBoxName%>"
														value="<%=isProcedureSelected%>" checked="checked"
														class="copy"> <%}else{%> <input type="checkbox"
														name="<%=checkBoxName%>" value="<%=isProcedureSelected%>"
														checked="checked" class="copy" disabled> <%}%> <%}else{
                                                        isAllProcedureSelected = false;
                                                            if(!modeValue){
                                                        %> <input
														type="checkbox" name="<%=checkBoxName%>"
														value="<%=isProcedureSelected%>" class="copy"> <%}else{%>
														<input type="checkbox" name="<%=checkBoxName%>"
														value="<%=isProcedureSelected%>" disabled class="copy">
														<%}%> <%}%> <%=personResponsible.get("procedureDesc")%>
													</td>
												</tr>
												<%indexColor++;%>
												<%procedureIndex++;%>
												<%}%>
											</logic:iterate>
										</logic:present>
									</logic:iterate>
								</logic:present>
								<%if(isAllProcedureSelected){%>
								<script>
                                 var allProcSelElement = document.getElementById("<%=(speciesId+"")%>");
                                 if(allProcSelElement != null){
                                    allProcSelElement.checked = 'true';
                                 }
                                </script>
								<%}%>
							</logic:iterate>
						</logic:present>
					</table>
				</td>
			</tr>
			<tr align="right" class="theader" height='30'>
				<td align='left' colspan="5">&nbsp; <%if(!modeValue){
                    String submitLInk = "javaScript:formSubmit('"+selectedPersonId+"')";%>
					<html:button property="Save" value="OK" styleClass="clsavebutton"
						onclick="<%=submitLInk%>" /> <html:button property="Cancel"
						value="Cancel" styleClass="clsavebutton" onclick="window.close();" />
					<%}else{%> <html:button property="Close" value="Close"
						styleClass="clsavebutton" onclick="window.close();" /> <%}%>
				</td>
			</tr>
		</table>
	</html:form>

</body>


</html:html>
