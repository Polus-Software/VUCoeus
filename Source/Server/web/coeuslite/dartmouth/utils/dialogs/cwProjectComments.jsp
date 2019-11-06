<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/coeus-utils.tld" prefix="coeusUtils"%>
<%@ include file="/coeuslite/mit/utils/CoeusContextPath.jsp"%>
<%@ page contentType="text/html;charset=UTF-8" language="java"
	import="java.util.Vector,org.apache.struts.validator.DynaValidatorForm"%>


<%
String indexValue = (String)request.getParameter("value");
int index = indexValue == null?index = -1 : Integer.valueOf(indexValue).intValue();
String module = (String)request.getParameter("module");
String moduleItemKey = (String)request.getParameter("moduleItemKey");
String relationShip = (String)request.getParameter("relationShip");
boolean txtDisable = false;
boolean displayMode = false;
Object dis = request.getParameter("disabled");
String disable =  dis == null ? "" : dis.toString();
if(disable!=null && disable.equals("true")){
    txtDisable = true;
    displayMode = true;
}
%>

<Script>
  function chkComm(){
        var strValue=document.getElementById('comments').value;
        var value=trim(strValue);
        
        if(value==null || value.length==0)
        {
            alert("<bean:message bundle="coi" key="error.EntityRelDesc.relationShip"/>");
            document.getElementById('comments').focus();
            return false;
        }else if(value.length > 2000) {
            alert('<bean:message bundle="coi" key="error.EntityRelDesc.length"/>');
            document.coiDisclosure.entityOrgRln.focus();
            return false;
        }else{
            document.forms[0].submit();
            var index = <%=index%>;
            if(index == 0){
                window.opener.document.getPerFnProjects.relComments0.value = document.commentsForm.comments.value;
            }else{
                window.opener.document.getPerFnProjects.relComments[index].value = document.commentsForm.comments.value;
            }
            closeWindow();    
        }
    }
     function trim(stringToTrim) {
        return stringToTrim.replace(/^\s+|\s+$/g,"");
    }
    function relationShipData()
    {
        var index = <%=index%>;
        <%if(!txtDisable){%>
        var value;
        if(index == 0){
            value = window.opener.document.getPerFnProjects.relComments0.value;
        }else{
            value = window.opener.document.getPerFnProjects.relComments[index].value;
        }
        if(value!==null || value.length!=0)
        {
          document.commentsForm.comments.value =value;
        }
        <%}else{%>
         document.commentsForm.comments.value = '<%=relationShip%>';
        <%}%>
    }
     function closeWindow(){
       self.close();   
     }  
</script>

<html>
<head>
<title>Comments</title>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
<link
	href="<bean:write name='ctxtPath'/>/coeuslite/mit/utils/css/ipf.css"
	rel="stylesheet" type="text/css">
<link
	href="<bean:write name='ctxtPath'/>/coeuslite/mit/utils/css/coeus_styles.css"
	rel="stylesheet" type="text/css">
</head>
<body onload="javascript:relationShipData()">
	<form id='commentsForm' name='commentsForm'>
		<table WIDTH='90%' border="0" align="center" cellpadding="0"
			cellspacing="0" class="tabtable">
			<%if(!displayMode){%>
			<tr bgcolor='#E1E1E1'>
				<td height="20%" align="left" valign=bottom width>
					<table height="40%" width='100%' border="0" cellpadding="0"
						cellspacing="0" class="tableheader">
						<tr>
							<td align=left class="theader"><b><bean:message
										bundle="coi" key="label.EntityRelDesc.enterRelationShip" />&nbsp;</b><%=module%>&nbsp;:&nbsp;<%=moduleItemKey%></td>
						</tr>
					</table>
				</td>
			</tr>
			<%}%>
			<tr>
				<td>
					<%if(txtDisable){%> <textArea id="comments" name="entityOrgRln"
						cols="65" rows="5" styleClass="copy" disabled></textArea> <%}else{%>
					<textArea id="comments" name="entityOrgRln" cols="65" rows="5"
						styleClass="copy"></textArea> <%}%> <input type="hidden"
					name="relationShipDesc">
				</td>
				<!--Addded  Case#4447 : Next phase of COI enhancements - Start -->
			<tr class='table'>
				<td colspan='6' class='savebutton'>
					<%if(txtDisable){%> <html:button property="close" value="Close"
						styleClass="clbutton" onclick="window.close();" /> <%}else{%> <html:button
						property="save" value="Save" styleClass="clbutton"
						onclick="javascript:chkComm();" /> &nbsp; <html:button
						property="Cancel" value="Cancel"
						onclick="javascript:closeWindow();" styleClass="clbutton" /> <%}%> <input
					type='hidden' id='index' name='index' value='<%=indexValue%>'>
				</td>
			</tr>
			<!--Case#4447 - End -->
		</table>
	</form>
</body>
</html>

