<%@ page contentType="text/html;charset=UTF-8" language="java"
	import="org.apache.struts.action.DynaActionForm;"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ include file="/coeuslite/mit/utils/CoeusContextPath.jsp"%>
<jsp:useBean id="procedureList" scope="session"
	class="edu.mit.coeuslite.utils.CoeusDynaFormList" />

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

    function formSubmit(speciesId){
        document.iacucProceduresDynaBeanList.action = "<%=request.getContextPath()%>/saveSelectedProceduresToGroup.do?speciesId="+speciesId+"&reloadFromSelect="+true;
        document.iacucProceduresDynaBeanList.submit();
 
    }
    
   function showProcedures(procedureCat){
        var searchShowHeaderElem = document.getElementsByTagName("div");
        for(var i = 0; i < searchShowHeaderElem.length; i++) {
            var id = searchShowHeaderElem[i].id;
            var showHeaderReg = new RegExp("showHeaderFor"+procedureCat);
            var hideHeaderReg = new RegExp("hideHeaderFor"+procedureCat);
            var procedureListReg = new RegExp(procedureCat+"ProcedureList");
            if(showHeaderReg.exec(id) != null){
             searchShowHeaderElem[i].style.display = "none";
            }
            if(hideHeaderReg.exec(id) != null){
             searchShowHeaderElem[i].style.display = "block";
            }

            if(procedureListReg.exec(id) != null){
                searchShowHeaderElem[i].style.display = "block";
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
    
    function expandCollapseAll(expandCollapse){
        var expandCollapseAll = expandCollapse;
        var showHeader, hideHeader;
        if(expandCollapseAll == 'COLLAPSEALL'){
            showHeader = "block";
            hideHeader = "none";
        }else if(expandCollapseAll == 'EXPANDALL'){
            showHeader = "none";
            hideHeader = "block";
        }
        var searchShowHeaderElem = document.getElementsByTagName("div");
        for(var i = 0; i < searchShowHeaderElem.length; i++) {
            var id = searchShowHeaderElem[i].id;
            var showHeaderReg = new RegExp("showHeaderFor");
            var hideHeaderReg = new RegExp("hideHeaderFor");
            var procedureListReg = new RegExp("ProcedureList");
            if(showHeaderReg.exec(id) != null){
                searchShowHeaderElem[i].style.display = showHeader;
            }

            if(hideHeaderReg.exec(id) != null){
                searchShowHeaderElem[i].style.display = hideHeader;
            }

            if(procedureListReg.exec(id) != null){
                searchShowHeaderElem[i].style.display = hideHeader;
            }

        }
    }
    
    function expandAll(){
        expandCollapseAll('EXPANDALL');
        document.getElementById('ExpandLink').style.display = 'none';
        document.getElementById('CollapseLink').style.display = 'block';
    }
    
    function collapseAll(){
        expandCollapseAll('COLLAPSEALL');
        document.getElementById('ExpandLink').style.display = 'block';
        document.getElementById('CollapseLink').style.display = 'none';
    }

</script>
</head>
<body bgcolor="#376DAA">
	<%String plus = request.getContextPath()+"/coeusliteimages/plus.gif";
    String minus = request.getContextPath()+"/coeusliteimages/minus.gif";
    String selectionDfltExpanded = (String)request.getAttribute("IACUC_PROCEDURE_SELECTION_DEFAULT_EXPAND");
    String speciesId = (String)request.getAttribute("speciesId");
    %>
	<html:form action="/iacucProcedureList.do" method="post">

		<script>
            <%
            String windowCLose = (String)request.getAttribute("WINDOW_CLOSE");
            if("Y".equals(windowCLose)){
                String isDataModified = (String)request.getAttribute("isDataModified");
                if(isDataModified == null){
                    isDataModified = "N";
                }
    %>
    
    opener.closeSelection('<%=speciesId%>','<%=isDataModified%>')
    <%}%>

    </script>
		<table width="100%" border="0" cellpadding="0" cellspacing="0"
			class="tabtable">
			<tr>
				<td height='10' colspan="2">
					<table width="100%" border="0" cellpadding="0" cellspacing="0"
						class="tableheader">
						<tr valign="top">
							<td>Procedures - Please select all applicable procedures for
								this Study/Teaching Group</td>
						</tr>
					</table>
				</td>

			</tr>
			<tr>
				<td align="right">
					<div id="ExpandLink">
						<a href="javascript:expandAll()">Expand All</a> &nbsp;
					</div>
					<div id="CollapseLink">
						<a href="javascript:collapseAll()">Collapse All</a>&nbsp;
					</div>
				</td>
			</tr>
			<tr>
				<td>
					<table width="99%" border="0" cellpadding="0" cellspacing="0"
						class="tabtable" align="center">

						<% 
                    
                    Integer procedureCatCodeMain = new Integer(0);
                    %>
						<logic:present name="procedureList" scope="session"
							property="additionalInfoList">
							<logic:iterate id="procedureCategory" name="procedureList"
								property="additionalInfoList"
								type="org.apache.struts.action.DynaActionForm" scope="session">

								<%  int indexColor = 1;
                            boolean procCatHeader = false;
                            
                            Integer procedureCatCode = (Integer)procedureCategory.get("procedureCategoryCode");
                            String procedureCatDesc = "";
                            if(procedureCatCode.intValue() != procedureCatCodeMain.intValue()){
                                procedureCatCodeMain = procedureCatCode;
                                procedureCatDesc = (String)procedureCategory.get("procedureCategoryDesc");
                                procCatHeader = true;
                            %>

								<tr class="copybold">

									<td>
										<div id="<%=("showHeaderFor"+procedureCatDesc)%>">
											<%String divlink = "javascript:showProcedures('"+procedureCatDesc+"')";%>
											&nbsp;
											<html:link href="<%=divlink%>">
												<html:img src="<%=plus%>" border="0" />
											</html:link>
											&nbsp;<%=procedureCatDesc%>
										</div>
									</td>

								</tr>


								<tr class="copybold">

									<td>
										<div id="<%=("hideHeaderFor"+procedureCatDesc)%>">
											<% divlink = "javascript:hideProcedures('"+procedureCatDesc+"')";%>
											&nbsp;
											<html:link href="<%=divlink%>">
												<html:img src="<%=minus%>" border="0" />
											</html:link>
											&nbsp;<%=procedureCatDesc%>
										</div>
									</td>

								</tr>


								<%}%>
								<%if(procCatHeader){%>
								<tr>
									<td height="5">


										<div id="<%=procedureCatDesc+"ProcedureList"%>">
											<table width="100%" border="0" cellpadding="0"
												cellspacing="0">
												<logic:iterate id="procedureDetails" name="procedureList"
													property="additionalInfoList"
													type="org.apache.struts.action.DynaActionForm"
													scope="session">
													<% 
                                                String strBgColor = "#D6DCE5";
                                                Integer procedureCatCode2 = (Integer)procedureDetails.get("procedureCategoryCode");
                                                if(procedureCatCode2.intValue() == procedureCatCodeMain.intValue()){
                                                    if (indexColor%2 == 0) {
                                                        strBgColor = "#DCE5F1";
                                                    }
                                                %>

													<tr bgcolor="<%=strBgColor%>" colspan="5"
														onmouseover="className='TableItemOn'"
														onmouseout="className='TableItemOff'">
														<td class="copy">&nbsp;&nbsp;&nbsp;&nbsp; <html:checkbox
																name="procedureDetails" styleClass="copy" indexed="true"
																property="isProcedureSelected" /> <%=procedureDetails.get("procedureDesc")%>


														</td>
													</tr>
													<%indexColor++;%>
													<%}%>

												</logic:iterate>
											</table>
										</div>
									</td>
								</tr>
								<%}%>
							</logic:iterate>
						</logic:present>

					</table>
				</td>
			</tr>
			<tr>
				<td>&nbsp;</td>
			</tr>
			<tr align="right" class="theader" height='30'>
				<td align='left' colspan="5">&nbsp; <%String submitLink = "javaScript:formSubmit('"+speciesId+"')";%>
					<html:button property="Save" value="OK" styleClass="clsavebutton"
						onclick="<%=submitLink%>" /> <html:button property="Cancel"
						value="Cancel" styleClass="clsavebutton" onclick="window.close();" />
				</td>
			</tr>
		</table>
	</html:form>
	<script>
    
    
<%if("1".equals(selectionDfltExpanded)){%>
    expandAll();
<%}else{%>
    collapseAll();
<%}%>

</script>
</body>


</html:html>
