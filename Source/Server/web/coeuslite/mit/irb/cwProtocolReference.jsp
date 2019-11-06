<%@ page contentType="text/html;charset=UTF-8" language="java"
	import="edu.mit.coeuslite.utils.CoeusLiteConstants,
        edu.mit.coeuslite.utils.DateUtils"%>

<%@page import="org.apache.struts.taglib.html.JavascriptValidatorTag"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/coeus-utils.tld" prefix="coeusUtils"%>
<%@ include file="/coeuslite/mit/utils/CoeusContextPath.jsp"%>
<jsp:useBean id="protocolReferenceTypes" scope="session"
	class="java.util.Vector" />
<jsp:useBean id="protocolReferenceList" scope="session"
	class="java.util.Vector" />

<html>
<head>
<title>Reference Details</title>
<html:base />
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
<script language="javascript" type="text/JavaScript"
	src="<bean:write name='ctxtPath'/>/coeuslite/mit/utils/scripts/coeusLiteCalendar.js"></script>
<script>
         var errValue = false;
         var errLock = false;
       function validatepage() {   
            document.protocolReference.acType.value = "I";           
        }
        
        function delete_data(desc,timestamp){
       if (confirm(<bean:message key="protocolReference.deleteMessage"/>)==true){
            document.protocolReference.acType.value = "D";
            document.protocolReference.referenceNumber.value = desc;            
            document.protocolReference.awUpdateTimestamp.value = timestamp;
            document.protocolReference.action = "<%=request.getContextPath()%>/deleteReferenceAction.do";
            document.protocolReference.submit();
        }
    }
    
    function view_comments(val) {
       var value;
       if(val.length<1000)
                {value=val; }
       else
                 {value=val.substring(0,1000);}
        var w = 550;
        var h = 213;
        if(navigator.appName == "Microsoft Internet Explorer") {
            w = 522;
            h = 196;
        }
        if (window.screen) {
               leftPos = Math.floor(((window.screen.width - 500) / 2));
               topPos = Math.floor(((window.screen.height - 350) / 2));
         }
        
                var loc = '<bean:write name='ctxtPath'/>/coeuslite/mit/utils/dialogs/cwViewSpecialReview.jsp?value='+value +'&type=R';
                var newWin = window.open(loc,"formName",'dependent=1,toolbar=0,location=0,directories=0,status=0, menubar=0,scrollbars=0,resizable=0,width=' + w + ',height=' + h+ ',left='+ leftPos + ',top=' + topPos);
                newWin.window.focus();
         
         }
</script>
<style>
.theader {
	font-weight: normal;
}
</style>
</head>
<body>
	<%
        String mode=(String)session.getAttribute(CoeusLiteConstants.MODE_DETAILS+session.getId());
        boolean modeValue=false;

        if(mode.equalsIgnoreCase(CoeusLiteConstants.DISPLAY_MODE)){
            modeValue=true;

        }else if(mode.equalsIgnoreCase(CoeusLiteConstants.MODIFY_MODE)){
            modeValue=false;
        }else if(mode.equalsIgnoreCase(CoeusLiteConstants.ADD_MODE)){
            modeValue=false;
        }
        //Added for coeus4.3 Amendments and Renewal enhancement
        String strProtocolNum = (String)session.getAttribute(CoeusLiteConstants.PROTOCOL_NUMBER+session.getId());
        if(strProtocolNum == null)
            strProtocolNum = "";    
        String amendRenewPageMode = (String)session.getAttribute("amendRenewPageMode"+request.getSession().getId());
        if(strProtocolNum.length() > 10 && (amendRenewPageMode == null || 
                amendRenewPageMode.equalsIgnoreCase(CoeusLiteConstants.DISPLAY_MODE))){
            modeValue = true;
        }%>
	<html:form action="/saveReferenceAction">
		<script type="text/javascript">
            document.getElementById('txt').innerHTML = '<bean:message key="helpPageTextProtocol.References"/>';
        </script>
		<%if(!modeValue){%>
		<table width='100%' border='0' cellpadding='0' cellspacing='0'
			class='table'>
			<tr>
				<td class='core'>
					<table width='100%' align=center border='0' cellpadding='0'
						cellspacing='0' class='tabtable'>
						<tr class='theader'>
							<td width="80%" align="left" valign="top" colspan='3'><bean:message
									key="protocolReference.ProtocolReferences" /></td>
							<td width="20%" align="right"><a id="helpPageText"
								href="javascript:showBalloon('helpPageText', 'helpText')"> <bean:message
										key="helpPageTextProtocol.help" />
							</a></td>
						</tr>
						<tr>
							<td colspan='4'>
								<div id="helpText" class='helptext'>
									&nbsp;&nbsp;
									<bean:message key="helpTextProtocol.References" />
								</div>
							</td>
						</tr>
						<%-- Commented for case id#2627
    <tr class='theader'>
        <td class="copybold" colspan='4'>
            <font color="red">*</font> 
                <bean:message key="label.indicatesReqFields"/>
        </td>
    </tr>
    --%>
						<tr class='theader'>
							<td class='copy' nowrap colspan='4'><font color='red'>
									<logic:messagesPresent>
										<script>errValue = true;</script>
										<html:errors header="" footer="" />
									</logic:messagesPresent> <logic:messagesPresent message="true">
										<script>errValue = true;</script>
										<html:messages id="message" message="true" property="errMsg"
											bundle="proposal">
											<script>errLock = true;</script>
											<li><bean:write name="message" /></li>
										</html:messages>
									</logic:messagesPresent>
							</font></td>
						</tr>
						<tr class='tableheader'>
							<td align="left" valign="top" colspan='4'><bean:message
									key="protocolReference.addProtocolReferences" /></td>
						</tr>
						<tr>
							<td colspan='4' height='10'></td>
						</tr>
						<tr>

							<td class="copybold" align="left" width='10%' nowrap>
								<%-- Commented for case id#2627
            <font color="red">*</font>
            --%> <bean:message key="protocolReference.ReferenceType" />
								:
							</td>
							<td align=left width='25%' class='copy'><html:select
									property="referenceTypeCode" styleClass="cltextbox-medium"
									onchange="dataChanged()" disabled="<%=modeValue%>">
									<html:option value="">
										<bean:message key="correspondents.pleaseSelect" />
									</html:option>
									<html:options collection="protocolReferenceTypes"
										property="code" labelProperty="description" />
								</html:select></td>
							<td nowrap class="copybold" width='7%' align="left">
								<%-- Commented for case id#2627
            <font color="red">*</font>
            --%> <bean:message key="protocolReference.ReferenceNumber" />:
							</td>
							<td align=left width='25%' class='copy'><html:text
									property="referenceKey" styleClass="cltextbox-medium"
									disabled="false" maxlength="20" onchange="dataChanged()" /></td>

						</tr>
						<tr>

							<td class='copybold' width='10%'><bean:message
									key="specialReviewLabel.ApplDate" />:</td>
							<td align=left width='25%'><html:text
									property="applicationDate" maxlength="10"
									styleClass="cltextbox-medium" onchange="dataChanged()" /> <%if(!modeValue){%>
								<html:link href="javascript:void(0)"
									onclick="displayCalendarWithTopLeft('applicationDate',8,-30)"
									tabindex="32">
									<img id="imgIRBDate" title="" height="16" alt=""
										src="<bean:write name='ctxtPath'/>/coeusliteimages/cal.gif"
										onclick='dataChanged()' width="16" border="0" runat="server">
								</html:link> <%}%></td>
							<td align=left width='7%' class='copybold'><bean:message
									key="specialReviewLabel.ApprDate" />:</td>
							<td align=left width='25%'><html:text
									property="approvalDate" maxlength="10"
									styleClass="cltextbox-medium" onchange="dataChanged()" /> <%if(!modeValue){%>
								<html:link href="javascript:void(0)"
									onclick="displayCalendarWithTopLeft('approvalDate',10,-45)"
									tabindex="32">
									<img id="imgIRBDate" title="" height="16" alt=""
										src="<bean:write name='ctxtPath'/>/coeusliteimages/cal.gif"
										onclick='dataChanged()' width="16" border="0" runat="server">
								</html:link> <%}%></td>

						</tr>
						<tr>

							<td class='copybold' width='10%'><bean:message
									key="specialReviewLabel.Comments" /> :</td>
							<td width='57%' colspan='3'>
								<%--div class='copyitalics'><bean:message key="protocolReference.comments"/></div--%>
								<html:textarea property="comments" styleClass="copy" cols="114"
									disabled="<%=modeValue%>" onchange="dataChanged()" />
							</td>
							<script>
            if(navigator.appName == "Microsoft Internet Explorer")
            {
                document.protocolReference.comments.cols=117;
                document.protocolReference.comments.rows=3;
            }
       </script>
						</tr>
						<tr>
							<td height='10'></td>
						</tr>
						<%if(!modeValue){%>
						<tr class='table'>
							<td class='savebutton' colspan='4'><html:submit
									property="save" value="Save" styleClass="clsavebutton"
									onclick="validatepage();" /></td>
						</tr>
						<%}%>
					</table>
				</td>
			</tr>

			<%}%>
			<tr>
				<td class='core'>

					<table width='100%' align=center border='0' cellpadding='0'
						cellspacing='0' class='tabtable'>
						<tr class='tableheader'>
							<td align="left" valign="top" width='15%' colspan='7'><bean:message
									key="protocolReference.Referencelist" /></td>
						</tr>
						<tr>
							<td width='20' colspan='7'>&nbsp;</td>
						</tr>
						<tr class='theader'>
							<td width="15%" align="left" class="copybold" nowrap><bean:message
									key="protocolReference.ReferenceType" /></td>
							<td width="18%" class="copybold" nowrap><bean:message
									key="protocolReference.ReferenceNumber" /></td>
							<td width="15%" class="copybold" nowrap><bean:message
									key="specialReviewLabel.ApplDate" /></td>
							<td width="15%" class="copybold" nowrap><bean:message
									key="specialReviewLabel.ApprDate" /></td>
							<td width='10%' class='copybold' nowrap><bean:message
									key="specialReviewLabel.Comments" /></td>
							<td width="6%" class="theader" nowrap></td>
						</tr>
						<% String strBgColor = "#DCE5F1";
                       int count = 0;
                       %>
						<logic:iterate id="reference" name="protocolReferenceList"
							type="org.apache.struts.validator.DynaValidatorForm">
							<% 
                                       if (count%2 == 0) 
                                       strBgColor = "#D6DCE5"; 
                                       else 
                                       strBgColor="#DCE5F1"; 
                                   %>
							<tr bgcolor='<%=strBgColor%>'
								onmouseover="className='TableItemOn'"
								onmouseout="className='TableItemOff'">
								<td width="15%" align="left" class="copy"><bean:write
										name="reference" property="referenceTypeDescription" /></td>
								<td width="18%" class="copy"><bean:write name="reference"
										property="referenceKey" /></td>
								<td width="15%" class="copy"><coeusUtils:formatDate
										name="reference" property="applicationDate" /></td>
								<td width="15%" class="copy"><coeusUtils:formatDate
										name="reference" property="approvalDate" /></td>
								<td width='6%' class='copy'>
									<%String viewLink = "javascript:view_comments('" +count +"')";%>
									<html:link href="<%=viewLink%>">
										<bean:message key="label.View" />
									</html:link>
								</td>
								<%if(!modeValue){%>
								<td width="6%" class="copy">
									<%}%> <%if(!modeValue){
                               String removeLink = "javascript:delete_data('" +reference.get("referenceNumber") +"','" +reference.get("updateTimestamp") +"')";
                         %> <html:link href="<%=removeLink%>">
										<bean:message key="fundingSrc.Remove" />
									</html:link> <%}%>
								</td>
							</tr>
							<% count++;%>
						</logic:iterate>




					</table> <html:hidden property="acType" value="" /> <html:hidden
						property="protocolNumber" value="" /> <html:hidden
						property="sequenceNumber" value="" /> <html:hidden
						property="referenceNumber" value="" /> <html:hidden
						property="awUpdateTimestamp" value="" /> </html:form> <script>
          DATA_CHANGED = 'false';
          if(errValue && !errLock) {
                DATA_CHANGED = 'true';
          }
          document.protocolReference.acType.value = 'I';
          LINK = "<%=request.getContextPath()%>/saveReferenceAction.do";
          FORM_LINK = document.protocolReference;
          PAGE_NAME = "<bean:message key="protocolReference.ProtocolReferences"/>";
          function dataChanged(){
                DATA_CHANGED = 'true';
          }
          linkForward(errValue);
         </script> <script>
          var help = ' <bean:message key="helpTextProtocol.References"/>';
          help = trim(help);
          if(help.length == 0) {
                document.getElementById('helpText').style.display = 'none';
          }
          function trim(s) {
                return s.replace( /^\s*/, "" ).replace( /\s*$/, "" );
          }  
      </script>
</body>
</html>