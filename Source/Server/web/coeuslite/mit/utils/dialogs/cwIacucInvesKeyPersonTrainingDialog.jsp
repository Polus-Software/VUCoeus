<%@ page contentType="text/html;charset=UTF-8" language="java"
	import="java.util.Hashtable,java.util.Vector,edu.mit.coeuslite.iacuc.action.TreeView;"%>
<%--<%@ page import="edu.mitweb.coeus.irb.action.TreeView;" %>  --%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ include file="/coeuslite/mit/utils/CoeusContextPath.jsp"%>
<jsp:useBean id="vecProtocolInvestigatorData" scope="session"
	class="java.util.Vector" />

<html:html locale="true">
<head>
<title>Investigators / Study Personnel</title>
<html:base />
<link
	href="<bean:write name='ctxtPath'/>/coeuslite/mit/utils/css/ipf.css"
	rel="stylesheet" type="text/css">
<link
	href="<bean:write name='ctxtPath'/>/coeuslite/mit/utils/css/coeus_styles.css"
	rel="stylesheet" type="text/css">
<%
String studyGroupId = "";
String studyGroupIndex = "";
if(request.getAttribute("studyGroupId") != null){
    studyGroupId = (String)request.getAttribute("studyGroupId");
}
if(request.getAttribute("studyGroupIndex") != null){
    studyGroupIndex = (String)request.getAttribute("studyGroupIndex");
}

Vector vecProtoInvestigatorData = (Vector)session.getAttribute("vecProtocolInvestigatorData");
%>
<script>
    function processRequest(listCode,listDesc){
        var studyGrpId = <%=studyGroupId%>;
        var studyGrpIndex = <%=studyGroupIndex%>;
        
        listDesc = listDesc == 'null' || listDesc == 'undefined' ?"":listDesc;         
        opener.putInvesKeyData(listCode,listDesc,studyGrpId,studyGrpIndex);        
        window.close();
    }
    
    <!-- COEUSQA:3463 - IACUC CoeusLite Select Multiple Protocol Personnel - Start -->
    function formSubmit() {     
        var studyGrpId = <%=studyGroupId%>;
        var studyGrpIndex = <%=studyGroupIndex%>;     
        var index = 0;
        var personalIds="";
        var nonEmployeeFlag="";
        var n = <%=vecProtoInvestigatorData.size()%>;
        for(index=0;index<n;index++){              
            if(document.getElementById('code'+index).checked) {           
                if(index==0){           
                    personalIds = document.getElementById('codeValue'+index).value + "!"+document.getElementById('nonEmployeeFlag'+index).value;
                }
                else{
                    if(personalIds != null && personalIds != ''){
                        personalIds = personalIds + ":" + document.getElementById('codeValue'+index).value+ "!"+document.getElementById('nonEmployeeFlag'+index).value
                    }
                    else{
                        personalIds = document.getElementById('codeValue'+index).value+ "!"+document.getElementById('nonEmployeeFlag'+index).value
                    }
                }
                
                nonEmployeeFlag = document.getElementById('nonEmployeeFlag'+index).value;
            }       
       }       
       if(personalIds == null || personalIds == ''){       
          alert("<bean:message  bundle="iacuc" key="validation.procedure.personnel"/>");
          return;
       }
       opener.putInvesKeyData(personalIds,'',studyGrpId,studyGrpIndex);   
       window.close();       
    }
    <!-- COEUSQA:3463 - End -->
    
</script>
</head>
<body bgcolor="#376DAA">

	<html:form action="/iacucStudyPersonlList.do" method="post">
		<table width="100%" border="0" cellpadding="0" cellspacing="0"
			class="tabtable">
			<tr valign=top>
				<td height='10' colspan="2">
					<table width="100%" border="0" cellpadding="0" cellspacing="0"
						class="tableheader">
						<tr valign="top">
							<td>Investigators / Study Personnel</td>
						</tr>
					</table>
				</td>

			</tr>
			<% int indexColor = 1;
           int checkBoxIndex = 0;%>
			<logic:present name="vecProtocolInvestigatorData">
				<logic:iterate id="listData" name="vecProtocolInvestigatorData"
					type="org.apache.struts.action.DynaActionForm">

					<%  String strBgColor = "#D6DCE5";
                if (indexColor%2 == 0) { 
                    strBgColor = "#DCE5F1"; }
                    String checkboxCode = "code"+checkBoxIndex;
                    String hiddenTextCode = "codeValue"+checkBoxIndex;
                    String hiddenNonEmployeeFlag = "nonEmployeeFlag"+checkBoxIndex;
            %>
					<tr bgcolor="<%=strBgColor%>" colspan="2"
						onmouseover="className='TableItemOn'"
						onmouseout="className='TableItemOff'">

						<td height='20'>
							<%
            String personId = (String)listData.get("personId");
            String personName = (String)listData.get("personName");
            String isNonEmployeeFlag = (String)listData.get("isNonEmployeeFlag");
            String pageLink = "javaScript:processRequest('"+personId+"','"+personName+"')";
            %> <html:checkbox name="listData" styleClass="copy"
								styleId="<%=checkboxCode%>" property="personId" /> <html:hidden
								property="code" styleId="<%=hiddenTextCode%>"
								value="<%=personId%>" /> <html:hidden property="code"
								styleId="<%=hiddenNonEmployeeFlag%>"
								value="<%=isNonEmployeeFlag%>" />
						</td>
						<td class="copy"><%=personName%></td>
					</tr>
					<% indexColor++; checkBoxIndex++;%>
				</logic:iterate>
			</logic:present>

			<tr align="right" class="theader" height='30'>
				<td align='left' colspan="5">&nbsp; <html:button
						property="Save" value="OK" styleClass="clsavebutton"
						onclick="formSubmit()" /> <html:button property="Cancel"
						value="Cancel" styleClass="clsavebutton" onclick="window.close();" />
				</td>
			</tr>
		</table>
	</html:form>
</body>
</html:html>
