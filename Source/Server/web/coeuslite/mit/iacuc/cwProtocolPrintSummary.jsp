<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ include file="/coeuslite/mit/utils/CoeusContextPath.jsp"%>
<%@ page
	import="org.apache.struts.validator.DynaValidatorForm, java.util.Vector,java.util.HashMap,java.util.Set,
java.util.Map,java.util.Iterator"%>
<%@ page import="edu.mit.coeuslite.utils.CoeusLiteConstants"%>
<jsp:useBean id="printSummChkBox" scope="request"
	class="java.util.HashMap" />
<jsp:useBean id="printSummQuestio" scope="request"
	class="java.util.HashMap" />
<jsp:useBean id="printSummProtoAttach" scope="request"
	class="java.util.HashMap" />
<jsp:useBean id="printSummOtherAttach" scope="request"
	class="java.util.HashMap" />

<html:html>
<head>
<title>Print Summary</title>

</head>
<%Map hmPrintSummaryChkBox = (Map)request.getAttribute("printSummChkBox");
Map hmPrintSummaryQuest = (Map)request.getAttribute("printSummQuestio");
// Added for  COEUSQA-2630_Allow the ability to print attachments with protocol summary/Schema changes start
Map hmPrintSummaryProtoAttach = (Map)request.getAttribute("printSummProtoAttach");
Map hmPrintSummaryOtherAttach = (Map)request.getAttribute("printSummOtherAttach");
// Added for COEUSQA-2630_Allow the ability to print attachments with protocol summary/Schema changes end
HashMap map = new HashMap();
int chkIndex = 0;
%>

<script>
    
    function selectAll(numOfChk) {
        var numOfChk = numOfChk
        for (var index = 1; index <= numOfChk; index++) {
            box = eval("document.protocolPrintForm.chk"+index); 
            if (box.checked == false) box.checked = true;
       }
   }

   function deSelectAll(numOfChk) {
    var numOfChk = numOfChk
        for (var index = 1; index <= numOfChk; index++) {
            box = eval("document.protocolPrintForm.chk"+index); 
            if (box.checked == true) box.checked = false;
        }
   }
  
</script>

<html:form action="/iacucPrint" method="post" target="_blank">

	<script type="text/javascript">
             document.getElementById('txt').innerHTML = '<bean:message bundle="iacuc" key="helpPageTextProtocol.PrintSummary"/>';
</script>
	<body>
		<table width="100%" height="100%" border="0" cellpadding="0"
			cellspacing="0" class="table">
			<tr>
				<td class='tableheader'>
					<table width="100%" border="0" cellpadding="0" cellspacing="0">
						<tr>
							<td width="50%" height="10%" align="left" valign="top"
								class="theader"><bean:message
									key="printSummary.PrintSummary" /></td>
							<td width="50%" class="theader" align="right"><a
								id="helpPageText"
								href="javascript:showBalloon('helpPageText', 'helpText')"> <bean:message
										bundle="iacuc" key="helpPageTextProtocol.help" />
							</a></td>
						</tr>
					</table>
				</td>
			</tr>
			<tr>
				<td height="50%" align="left" valign="top">
					<table width="99%" border="0" align="center" cellpadding="0"
						cellspacing="0" class="tabtable">
						<logic:present name="printSummChkBox">

							<%int count = 0;
            Set chkBoxName = hmPrintSummaryChkBox.keySet();
            Iterator chkIterator = chkBoxName.iterator();
            while(chkIterator.hasNext()){
                if(count == 0){%>
							<tr>
								<%}%>
								<td width="1%" valign="left">
									<%String chkName = (String)chkIterator.next();
                String labelName = (String)hmPrintSummaryChkBox.get(chkName);%>
									<input type="checkbox" name="<%=chkName%>" checked>
								</td>
								<td width="20%" valign="left" class="copybold"><%=labelName%>
								</td>
								<% count++;
                if(count == 3){
                count = 0;
                }
               if(count == 0){%>
							</tr>
							<tr>
								<td>&nbsp;</td>
							</tr>
							<%}
            }%>

							</td>
							</tr>

							</tr>
						</logic:present>


					</table>
				</td>
			</tr>
			<%if(hmPrintSummaryQuest != null && hmPrintSummaryQuest.size() > 0){%>
			<tr>
				<td height="5"></td>
			</tr>
			<!-- Questionnaire Forms Start -->
			<tr>
				<td height="52%" align="left" valign="top">
					<table width="99%" border="0" align="center" cellpadding="0"
						cellspacing="0" class="tabtable">
						<tr>
							<td colspan="6" align="left" valign="top">
								<table width="100%" height="10%" border="0" cellpadding="0"
									cellspacing="0" class="tableheader">
									<tr>
										<td><bean:message key="printSummary.QuestionnaireForms" />
										</td>
									</tr>
								</table>
							</td>
						</tr>
						<logic:present name="printSummChkBox">

							<%int count = 0;
            Set chkBoxName = hmPrintSummaryQuest.keySet();
            Iterator chkIterator = chkBoxName.iterator();
            while(chkIterator.hasNext()){
                if(count == 0){%>
							<tr>
								<%}%>
								<td width="1%" valign="left">
									<%String chkName = (String)chkIterator.next();
                String labelName = (String)hmPrintSummaryQuest.get(chkName);%>
									<input type="checkbox" name="<%=chkName%>" checked>
								</td>
								<td width="20%" valign="left" class="copybold"><%=labelName%>
								</td>
								<% count++;
                if(count == 3){
                count = 0;
                }
               if(count == 0){%>
							</tr>
							<tr>
								<td>&nbsp;</td>
							</tr>
							<%}
            }%>

							</td>
							</tr>

							</tr>
						</logic:present>

						<tr>

							<td>&nbsp;</td>
						</tr>

					</table>
				</td>
			</tr>
			<%}%>
			<!-- COEUSQA-2630_Allow the ability to print attachments with protocol summary/Schema changes start-->
			<%if(hmPrintSummaryProtoAttach != null && hmPrintSummaryProtoAttach.size() > 0){%>
			<tr>
				<td height="5"></td>
			</tr>
			<!-- Questionnaire Forms Start -->
			<tr>
				<td height="52%" align="left" valign="top">
					<table width="99%" border="0" align="center" cellpadding="0"
						cellspacing="0" class="tabtable">
						<tr>
							<td colspan="6" align="left" valign="top">
								<table width="100%" height="10%" border="0" cellpadding="0"
									cellspacing="0" class="tableheader">
									<tr>
										<td>Protocol Attachments</td>
									</tr>
								</table>
							</td>
						</tr>
						<logic:present name="printSummChkBox">

							<%int count = 0;
            Set chkBoxName = hmPrintSummaryProtoAttach.keySet();
            Iterator chkIterator = chkBoxName.iterator();
            while(chkIterator.hasNext()){
                if(count == 0){%>
							<tr>
								<%}%>
								<td width="1%" valign="left">
									<%String chkName = (String)chkIterator.next();
                String labelName = (String)hmPrintSummaryProtoAttach.get(chkName);%>
									<input type="checkbox" name="<%=chkName%>" checked
									align="middle">
								</td>
								<td width="20%" valign="left" class="copybold"><%=labelName%>
								</td>
								<% count++;
                if(count == 3){
                count = 0;
                }
               if(count == 0){%>
							</tr>
							<tr>
								<td>&nbsp;</td>
							</tr>
							<%}
            }%>

							</td>
							</tr>

							</tr>
						</logic:present>

						<tr>

							<td>&nbsp;</td>
						</tr>

					</table>
				</td>
			</tr>
			<%}%>
			<%if(hmPrintSummaryOtherAttach != null && hmPrintSummaryOtherAttach.size() > 0){%>
			<tr>
				<td height="5"></td>
			</tr>
			<!-- Questionnaire Forms Start -->
			<tr>
				<td height="52%" align="left" valign="top">
					<table width="99%" border="0" align="center" cellpadding="0"
						cellspacing="0" class="tabtable">
						<tr>
							<td colspan="6" align="left" valign="top">
								<table width="100%" height="10%" border="0" cellpadding="0"
									cellspacing="0" class="tableheader">
									<tr>
										<td>Other Attachments</td>
									</tr>
								</table>
							</td>
						</tr>
						<logic:present name="printSummChkBox">

							<%int count = 0;
            Set chkBoxName = hmPrintSummaryOtherAttach.keySet();
            Iterator chkIterator = chkBoxName.iterator();
            while(chkIterator.hasNext()){
                if(count == 0){%>
							<tr>
								<%}%>
								<td width="1%" valign="left">
									<%String chkName = (String)chkIterator.next();
                String labelName = (String)hmPrintSummaryOtherAttach.get(chkName);%>
									<input type="checkbox" name="<%=chkName%>" checked>
								</td>
								<td width="20%" valign="left" class="copybold"><%=labelName%>
								</td>
								<% count++;
                if(count == 3){
                count = 0;
                }
               if(count == 0){%>
							</tr>
							<tr>
								<td>&nbsp;</td>
							</tr>
							<%}
            }%>

							</td>
							</tr>

							</tr>
						</logic:present>

						<tr>

							<td>&nbsp;</td>
						</tr>

					</table>
				</td>
			</tr>
			<%}%>
			<!-- COEUSQA-2630_Allow the ability to print attachments with protocol summary/Schema changes end-->
			<tr>
				<%
    String vcSize = hmPrintSummaryChkBox.size()+hmPrintSummaryQuest.size()+hmPrintSummaryProtoAttach.size()+hmPrintSummaryOtherAttach.size()+"";
    String noOfModulesForSelect = "javascript:selectAll('"+vcSize+"')";
    String noOfModulesForDeSelect = "javascript:deSelectAll('"+vcSize+"')";
    %>
			
			<tr class='table'>
				<td colspan="3" nowrap class="savebutton" width="10%"><html:button
						property="SelectAll" value="Select All" styleClass="clsavebutton"
						onclick="<%=noOfModulesForSelect%>" />&nbsp;&nbsp;&nbsp;&nbsp; <html:button
						property="DeSelectAll" value="Deselect All"
						styleClass="clsavebutton" onclick="<%=noOfModulesForDeSelect%>" />&nbsp;&nbsp;&nbsp;&nbsp;
					<html:submit property="Print" value="Print"
						styleClass="clsavebutton" /></td>
			</tr>

			</tr>

		</table>


	</body>

</html:form>


</html:html>
