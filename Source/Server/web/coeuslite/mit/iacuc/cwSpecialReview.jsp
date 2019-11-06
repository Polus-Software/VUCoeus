<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%--start1--%>

<%@ page contentType="text/html;charset=UTF-8" language="java"
	import="edu.mit.coeuslite.utils.CoeusLiteConstants,
        edu.mit.coeuslite.utils.DateUtils"%>
<%--end2--%>
<%@page import="org.apache.struts.taglib.html.JavascriptValidatorTag"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<jsp:useBean id="iacucSplReview" scope="session"
	class="java.util.Vector" />
<jsp:useBean id="approval" scope="session" class="java.util.Vector" />
<jsp:useBean id="ReviewList" scope="session" class="java.util.Vector" />
<%@ include file="/coeuslite/mit/utils/CoeusContextPath.jsp"%>
<%  DateUtils dateUtils = new edu.mit.coeuslite.utils.DateUtils();
%>
<html:html>
<head>
<title>Coeus Web</title>
<html:base />
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
<script language="javascript" type="text/JavaScript"
	src="<bean:write name='ctxtPath'/>/coeuslite/mit/utils/scripts/coeusLiteCalendar.js"></script>
<script>
        var errValue = false;
        var errLock = false;
        var selectedValue=0;
        function add_specialReview(action)
        {
            document.iacucSpecialReview.action = "<%=request.getContextPath()%>/iacucspecialReviewAction.do?actionPerformed="+action;
            document.iacucSpecialReview.submit();
        }
      
      function delete_data(data,desc,timestamp){
       if (confirm("Are you sure you want to delete the special review?")==true){
            document.iacucSpecialReview.acType.value = data;
            document.iacucSpecialReview.specialReviewNumber.value = desc;            
            document.iacucSpecialReview.awUpdateTimestamp.value = timestamp;
            document.iacucSpecialReview.action = "<%=request.getContextPath()%>/iacucspecialReviewAction.do";
            document.iacucSpecialReview.submit();
        }
      }
    
      function edit_data(desc,timestamp){
            document.iacucSpecialReview.acType.value = 'E';
            document.iacucSpecialReview.specialReviewNumber.value = desc;            
            document.iacucSpecialReview.awUpdateTimestamp.value = timestamp;
            document.iacucSpecialReview.action = "<%=request.getContextPath()%>/iacucspecialReviewAction.do";
            document.iacucSpecialReview.submit();
      }    
    
        function insert_data(data){
        var val = document.iacucSpecialReview.acType.value;
        if(val!='U'){
            document.iacucSpecialReview.acType.value = data;
        }
        
        document.iacucSpecialReview.action = "<%=request.getContextPath()%>/iacucspecialReviewActions.do";
        
        }
        
        function add_specialReview(){
        
            CLICKED_LINK = "<%=request.getContextPath()%>/getProtocolData.do?PAGE=S";
            if(validate() == true){
                document.iacucSpecialReview.action = "<%=request.getContextPath()%>/getIacucData.do?PAGE=S";
                document.iacucSpecialReview.submit();
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
        
                var loc = '<bean:write name='ctxtPath'/>/coeuslite/mit/utils/dialogs/cwViewSpecialReview.jsp?value='+value +'&type=S';
                var newWin = window.open(loc,"formName",'dependent=1,toolbar=0,location=0,directories=0,status=0, menubar=0,scrollbars=0,resizable=0,width=' + w + ',height=' + h+ ',left='+ leftPos + ',top=' + topPos);
                newWin.window.focus();
         
         }
         //Added for case 4267-Double clicking save results in error
         function disableSaveButton(){
            document.iacucSpecialReview.save.disabled=true;
         }
          function showHide(toggleId) {
               // alert(toggleId); 
                //1-Show 0- Hide
             if(toggleId == 1){
                document.getElementById('showOrHide').style.display = "none";
                selectedValue=0;
            }else if(toggleId == 0){
                document.getElementById('showOrHide').style.display = "";
                document.getElementById('showSpecialReview').style.display = "none"; 
                selectedValue=1;
            }
          }
          function cancelSpecialReview(){
            //document.getElementById('showSpecialReview').style.display = ""; 
            //document.getElementById('showOrHide').style.display = "none";
            document.iacucSpecialReview.action = "<%=request.getContextPath()%>/getIacucData.do?PAGE=S";
            document.iacucSpecialReview.submit();
          }
        </script>

<script>
        function validateForm() {
        insert_data("I");       
}
</script>

</head>
<body>
	<% boolean hasValidationMessage = false;
        //start--2
        String mode=(String)session.getAttribute(CoeusLiteConstants.MODE_DETAILS+session.getId());
        Boolean splRvwModeInDisplay = (Boolean) session.getAttribute("MODIFY_IACUC_SPECIAL_RVW"+session.getId());
        boolean modeValue=false;
        //Added for case 4590:Changes in special review being wiped out after an amendment is approved - Protocol
        if(mode.equalsIgnoreCase(CoeusLiteConstants.DISPLAY_MODE)){
            if(splRvwModeInDisplay.booleanValue()){
                modeValue=false;
            }else{
                modeValue=true;
            }
        //4590 End
        }else if(mode.equalsIgnoreCase(CoeusLiteConstants.MODIFY_MODE)){
            modeValue=false;
        }else if(mode.equalsIgnoreCase(CoeusLiteConstants.ADD_MODE)){
            modeValue=false;
        }
        String protocolNo = (String)session.getAttribute(CoeusLiteConstants.IACUC_PROTOCOL_NUMBER+session.getId());
        //end--2
        if(protocolNo!= null){ 
             protocolNo = "["+"Protocol No. - "+ protocolNo+"]";
        }else{
            protocolNo = "";
        }
        //Added for coeus4.3 Amendments and Renewal enhancement
        String strProtocolNum = (String)session.getAttribute(CoeusLiteConstants.IACUC_PROTOCOL_NUMBER+session.getId());
        if(strProtocolNum == null)
            strProtocolNum = "";    
        String amendRenewPageMode = (String)session.getAttribute("amendRenewPageMode"+request.getSession().getId());
        if(strProtocolNum.length() > 10 && (amendRenewPageMode == null || 
                amendRenewPageMode.equalsIgnoreCase(CoeusLiteConstants.DISPLAY_MODE))){
            modeValue = true;
        }%>
	<html:form action="/iacucspecialReviewActions" method="post"
		onsubmit="disableSaveButton()">
		<a name="top"></a>
		<%--  ************  START OF BODY TABLE  ************************--%>
		<!-- New Template for cwViewFinEntity - Start   -->
		<script type="text/javascript">
    document.getElementById('txt').innerHTML = '<bean:message bundle="iacuc" key="helpPageTextProtocol.SpecialReview"/>';
</script>
		<table width="100%" height="100%" border="0" cellpadding="0"
			cellspacing="0" class="table">
			<tr>
				<td>
					<table width="100%" border="0" cellpadding="0" cellspacing="0"
						class="tableheader">
						<tr>
							<td height="20%" align="left" valign="top"><bean:message
									bundle="iacuc" key="specialReviewLabel.SpecialReview" /></td>
							<td align="right" width="50%"><a id="helpPageText"
								href="javascript:showBalloon('helpPageText', 'helpText')"> <bean:message
										bundle="iacuc" key="helpPageTextProtocol.help" />
							</a></td>
						</tr>
					</table>
				</td>
			</tr>
			<tr>
				<td>
					<div id="helpText" class='helptext'>
						&nbsp;&nbsp;
						<bean:message key="helpTextProtocol.SpecialReview" />
					</div>
				</td>
			</tr>
			<%-- Commented for case id#2627
        <tr>
            <td class="copybold">
                &nbsp;&nbsp;
                <font color="red">*</font> 
                <bean:message key="label.indicatesReqFields"/>
            </td>
        </tr>
        --%>
			<tr class='copy' align="left">
				<td><font color="red"> <logic:messagesPresent>
							<script> 
                                        errValue = true;
                                    </script>
							<%hasValidationMessage = true;%>
							<html:errors header="" footer="" bundle="iacuc" />
						</logic:messagesPresent> <logic:messagesPresent message="true">
							<%hasValidationMessage = true;%>
							<script>errValue = true;</script>
							<html:messages id="message" message="true"
								property="error.specialReview.protocolNum">
								<font color='red'><li><bean:write name="message" /></li></font>
							</html:messages>
							<html:messages id="message" message="true"
								property="error.specialReview.applicationDate">
								<font color='red'><li><bean:write name="message" /></li></font>
							</html:messages>
							<html:messages id="message" message="true"
								property="error.specialReview.approvalDate">
								<font color='red'><li><bean:write name="message" /></li></font>
							</html:messages>
							<html:messages id="message" message="true"
								property="error.specialReview.expirationDate" bundle="iacuc">
								<font color='red'><li><bean:write name="message" /></li></font>
							</html:messages>
							<html:messages id="message" message="true" property="errMsg">
								<script>errLock = true;</script>
								<li><bean:write name="message" /></li>
							</html:messages>
						</logic:messagesPresent>
				</font></td>
			</tr>
			<%if(!modeValue){%>
			<tr>
				<td><div id="showSpecialReview">
						<a href="javascript:showHide(selectedValue);"><u><bean:message
									key="specialReviewLabel.AddSpecialReview" /> </u></a>
					</div></td>
			</tr>
			<%}%>
			<!--  Special Review - Start  -->
			<%if(!modeValue){%>
			<tr>
				<td>
					<div id="showOrHide" style="display: none">
						<table width="100%" border="0" align="center" cellpadding="0"
							cellspacing="0" class="tabtable">

							<tr>
								<td align="left" valign="top">
									<table width="100%">
										<tr>
											<td>
												<%
                            String addLink = "javascript:add_specialReview()";
                        %>
												<div id='add_label' style='display: none;'>
													&nbsp;&nbsp;&nbsp;&nbsp;
													<html:link href="<%=addLink%>">
														<u><bean:message
																key="specialReviewLabel.AddSpecialReview" /></u>
													</html:link>
												</div>
											</td>
										</tr>
									</table>
								</td>
							</tr>
							<tr>
								<td>
									<%--special review and approval drop down--%>
									<table width='100%' border='0'>
										<tr>
											<td nowrap class="copybold" width="7%" align="right">
												<%-- Commented for case id#2627
                                    <font color="red">*</font>
                                    --%> <bean:message
													key="specialReviewLabel.SpecialReview" />:&nbsp;
											</td>
											<td width='40%' align=left class='copy'><html:select
													property="specialReviewCode" styleClass="cltextbox-medium"
													onchange="dataChanged()" disabled="<%=modeValue%>">
													<html:option value="">
														<bean:message key="specialReviewLabel.pleaseSelect" />
													</html:option>
													<html:options collection="iacucSplReview" property="code"
														labelProperty="description" />
												</html:select></td>
											<td width='3%'></td>
											<td></td>
											<td></td>
											<td nowrap class="copybold" width="10%" align="right">
												<%-- Commented for case id#2627
                                    &nbsp;&nbsp;<font color="red">*</font>
                                    --%> <bean:message
													key="specialReviewLabel.Approval" />:&nbsp;
											</td>
											<td align=left valign=bottom class='copy'><html:select
													property="approvalCode" style="width:230px;"
													styleClass="cltextbox-medium" onchange="dataChanged()"
													disabled="<%=modeValue%>">
													<html:option value="">
														<bean:message key="specialReviewLabel.pleaseSelect" />
													</html:option>
													<html:options collection="approval" property="code"
														labelProperty="description" />
												</html:select></td>
										</tr>
									</table>
								</td>
							</tr>
							<tr>
								<td>
									<%--application date,approval date and proto number--%>
									<table width='100%' border='0' align="left">
										<tr>
											<td nowrap class="copybold" width="5%" align="right">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
												<bean:message key="specialReviewLabel.ProtocolNo" />:&nbsp;
											</td>
											<td class="copy" align="left" width='10%' nowrap><html:text
													property="spRevProtocolNumber" styleClass="textbox"
													disabled="<%=modeValue%>" maxlength="20"
													onchange="dataChanged()" /></td>
											<td width='20%'></td>
											<td nowrap class="copybold" width="5%" align="right">&nbsp;
												<bean:message key="specialReviewLabel.ApplDate" />:&nbsp;
											</td>
											<td class="copy" align=left width='10%' nowrap><html:text
													property="applicationDate" styleClass="textbox"
													onchange="dataChanged()" /> <%if(!modeValue){%> <html:link
													href="javascript:void(0)"
													onclick="displayCalendarWithTopLeft('applicationDate',8,25)"
													tabindex="32">
													<img id="imgIRBDate" title="" height="16" alt=""
														src="<bean:write name='ctxtPath'/>/coeusliteimages/cal.gif"
														width="16" onclick='dataChanged()' border="0"
														runat="server">
												</html:link> <%--a id="hlIRBDate" onclick="displayCalendarWithTopLeft('applicationDate',8,25)" tabindex="32" href="javascript:void(0);"
                                        runat="server"><img id="imgIRBDate" title="" height="16" alt="" src="<bean:write name='ctxtPath'/>/coeusliteimages/cal.gif" width="16"
                                        border="0" runat="server">
                                    </a--%> <%}%></td>
											<td width='21%'></td>
										</tr>
										<tr>
											<td nowrap class="copybold" width="5%" align="right">&nbsp;
												<bean:message key="specialReviewLabel.ApprDate" />:&nbsp;
											</td>
											<td valign="top" nowrap class="copy" width='10%' align=left>
												<html:text property="approvalDate" styleClass="textbox"
													onchange="dataChanged()" /> <%if(!modeValue){%> <html:link
													href="javascript:void(0)"
													onclick="displayCalendarWithTopLeft('approvalDate',10,-150)"
													tabindex="32">
													<img id="imgIRBDate" title="" height="16" alt=""
														src="<bean:write name='ctxtPath'/>/coeusliteimages/cal.gif"
														width="16" onclick='dataChanged()' border="0"
														runat="server">
												</html:link> <%}%>
											</td>
											<td width='20%'></td>
											<%-- Added for COEUSQA-1724-Expiration Date Column-start--%>
											<td nowrap class="copybold" width="5%" align="right">&nbsp;
												<bean:message key="specialReviewLabel.ExpirationDate"
													bundle="iacuc" />:&nbsp;
											</td>
											<td valign="top" nowrap class="copy" width='10%' align=left>
												<html:text property="expirationDate" styleClass="textbox"
													onchange="dataChanged()" /> <%if(!modeValue){%> <html:link
													href="javascript:void(0)"
													onclick="displayCalendarWithTopLeft('expirationDate',10,-150)"
													tabindex="32">
													<img id="imgIRBDate" title="" height="16" alt=""
														src="<bean:write name='ctxtPath'/>/coeusliteimages/cal.gif"
														width="16" onclick='dataChanged()' border="0"
														runat="server">
												</html:link> <%}%>
											</td>
											<td width='21%'></td>
											<%-- Added for COEUSQA-1724-Expiration Date Column-End--%>
										</tr>
									</table>
								</td>
							</tr>
							<tr>
								<td>
									<table border='0'>
										<tr>
											<td nowrap class="copybold" width="7%" align="right"
												valign="top">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
												<bean:message key="specialReviewLabel.Comments" />:&nbsp;
											</td>
											<td class="copy">
												<%--div class='copyitalics'><bean:message key="specialReview.commentsLimit"/></div--%>
												<html:textarea property="comments" styleClass="copy"
													cols="118" rows="3" disabled="<%=modeValue%>"
													onchange="dataChanged()" />
											</td>
										</tr>
									</table>
								</td>
							</tr>
							<tr class='table'>
								<td colspan="3" nowrap class="savebutton" width="50%"><html:submit
										property="save" value="Save" styleClass="clsavebutton"
										onclick="validateForm();" /> <html:button property="save"
										styleClass="clsavebutton" onclick="cancelSpecialReview()">
										<bean:message bundle="iacuc" key="button.label.cancel" />
									</html:button></td>
							</tr>
						</table>
					</div>
				</td>
			</tr>

			<%}%>
			<!--  Special Review - End  -->

			<!--  List of Special Review - Start -->
			<tr>
				<td align="left" valign="top" class='core'><table width="100%"
						border="0" align="center" cellpadding="0" cellspacing="0"
						class="tabtable">
						<tr align="center">
							<td colspan="3">
								<div
									STYLE="overflow: auto; width: 768px; padding: 0px; margin: 0px">
									<table width="100%" border="0" cellpadding="2" class="tabtable">
										<tr align="left">
											<td width="20%" align="left" class="theader"><bean:message
													key="specialReviewLabel.SpecialReview" /></td>
											<td nowrap width="10%" class="theader"><bean:message
													key="specialReviewLabel.Approval" /></td>
											<td nowrap width="12%" class="theader"><bean:message
													key="specialReviewLabel.ProtocolNo" /></td>
											<td nowrap width="15%" class="theader"><bean:message
													key="specialReviewLabel.ApplDate" /></td>
											<td nowrap width="15%" class="theader"><bean:message
													key="specialReviewLabel.ApprDate" /></td>
											<td nowrap width="15%" class="theader"><bean:message
													key="specialReviewLabel.ExpirationDate" bundle="iacuc" /></td>
											<td nowrap width='8%' class='theader' align="center"><bean:message
													key="specialReviewLabel.Comments" /></td>
											<%if(!modeValue){%>
											<td width="8%" class="theader"></td>
											<%}%>
										</tr>
										<%                String strBgColor = "#DCE5F1"; //BGCOLOR=EFEFEF  FBF7F7
                                                                      int count = 0;
                                                                    %>
										<logic:present name="ReviewList">
											<logic:iterate id="review" name="ReviewList"
												type="org.apache.struts.validator.DynaValidatorForm">
												<% String protoNum = "";
                                            if(review.get("spRevProtocolNumber") == null){
                                                protoNum = "";
                                                }else{
                                                  protoNum = review.get("spRevProtocolNumber").toString();  
                                                    }
                                            %>
												<% 
                                               if (count%2 == 0) 
                                               strBgColor = "#D6DCE5"; 
                                               else 
                                               strBgColor="#DCE5F1";
                                               String editLink = "javascript:edit_data('"+review.get("specialReviewNumber") +"','" +review.get("updateTimestamp") +"')";
                                               boolean flag = true;
                                               if(review.get("showInLite") !=null &&
                                                       review.get("showInLite").equals("N")){
                                                   flag = false;
                                               }
                                           %>
												<tr align="left" class="copy" bgcolor="<%=strBgColor%>"
													onmouseover="className='TableItemOn'"
													onmouseout="className='TableItemOff'">
													<td class='copy'>
														<%if(!modeValue && flag){%> <html:link href="<%=editLink%>">
															<%=review.get("specialReviewDescription").toString()%>
														</html:link> <%}else{%> <%=review.get("specialReviewDescription").toString()%>
														<%}%>
													</td>
													<td class='copy'><%=review.get("approvalDescription").toString()%>
													</td>
													<td class='copy'><%=protoNum%></td>
													<td class='copy'>
														<%--
                                                System.out.println("applDate"+review.get("applicationDate").toString());
                                                //if(review.get("applicationDate").toString() != null){
                                                String applDateVal = dateUtils.formatDate((String)review.get("applicationDate"),edu.mitweb.coeus.iacuc.DateUtils.MM_DD_YYYY);
                                                review.set("applicationDate",applDateVal);
                                                System.out.println("edu.mitweb.coeus.iacuc.DateUtils.MM_DD_YYYY "+edu.mitweb.coeus.iacuc.DateUtils.MM_DD_YYYY);
                                                System.out.println("applDateVal"+dateUtils.formatDate((String)review.get("applicationDate"),edu.mitweb.coeus.iacuc.DateUtils.MM_DD_YYYY));
                                                //}else{
                                                //applDateVal = "";
                                                --%> <%String applDate = "";
                                              if(review.get("applicationDate") == null){
                                                  applDate = "";
                                                }else{
                                                  applDate = (String)review.get("applicationDate");  
                                                }
                                                %> <%=applDate%>
													</td>
													<td class='copy'>
														<%--String dateValue = dateUtils.formatDate(review.get("approvalDate").toString(),edu.mitweb.coeus.iacuc.DateUtils.MM_DD_YYYY);--%>
														<%String apprvDate = "";
                                                  if(review.get("approvalDate") == null){
                                                      apprvDate = "";
                                                    }else{
                                                      apprvDate = (String)review.get("approvalDate");  
                                                    }
                                                    %> <%=apprvDate%>
													</td>
													<%--Added to display expiration date-start--%>
													<td class='copy'>
														<%--String dateValue = dateUtils.formatDate(review.get("approvalDate").toString(),edu.mitweb.coeus.iacuc.DateUtils.MM_DD_YYYY);--%>
														<%String expirationDate = "";
                                                  if(review.get("expirationDate") == null){
                                                      expirationDate = "";
                                                    }else{
                                                      expirationDate = (String)review.get("expirationDate");  
                                                    }
                                                    %> <%=expirationDate%>
													</td>
													<%--Added to display expiration date-end--%>
													<td class='copy' align="center">
														<%
                                                    String viewLink = "javascript:view_comments('" +count +"')";
                                                %> <html:link
															href="<%=viewLink%>">
															<bean:message key="label.View" />
														</html:link> <%--a href="javascript:view_comments('<%=review.get("comments")%>')">
                                                    <bean:message key="label.View"/>
                                                </a--%>
													</td>
													<%if(!modeValue){%>
													<td class='copy'>
														<%if(flag){
                                                    String removeLink = "javascript:delete_data('D','" +review.get("specialReviewNumber") +"','" +review.get("updateTimestamp") +"')";
                                                %> <html:link
															href="<%=removeLink%>">
															<bean:message key="fundingSrc.Remove" />
														</html:link> <%--a href="javascript:delete_data('D','<%=review.get("specialReviewNumber")%>','<%=review.get("updateTimestamp")%>')">
                                                    <bean:message key="fundingSrc.Remove"/>
                                                </a--%> <%} else {%> <bean:message
															key="fundingSrc.Remove" /> <%}%>
													</td>
													<%}%>
												</tr>
												<% count++;%>
											</logic:iterate>
										</logic:present>
									</table>
								</div>
							</td>
						</tr>
						<tr>
							<td>&nbsp;</td>
						</tr>
					</table></td>
			</tr>
			<!--  List of Special Review -End -->
			<tr>
				<td height='10'>&nbsp;</td>
			</tr>
		</table>
		<!-- New Template for cwViewFinEntity - End  -->
		<html:hidden property="acType" />
		<html:hidden property="protocolNumber" />
		<html:hidden property="sequenceNumber" />
		<html:hidden property="specialReviewNumber" />
		<html:hidden property="awUpdateTimestamp" />


	</html:form>
	<script>
       /*     var applDate = new calendar2(document.forms['specialReview'].elements['applicationDate']);
            applDate.year_scroll = true;
            applDate.time_comp = false;
            
            var apprDate = new calendar2(document.forms['specialReview'].elements['approvalDate']);
            apprDate.year_scroll = true;
            apprDate.time_comp = false;*/
            
            
        </script>
	<iframe width='17%' height='20%' name="gToday:normal:agenda.js"
		id="gToday:normal:agenda.js" src="ipopeng.htm" scrolling="no"
		frameborder="0"
		style="visibility: visible; z-index: 999; position: absolute; left: -500px; top: 0px;">
	</iframe>

	<script>
    //setForm(document.specialReview);
  </script>

	<script>
          DATA_CHANGED = 'false';
          if(errValue && !errLock) {
            DATA_CHANGED = 'true';
          }
          if(document.iacucSpecialReview.acType.value != 'U'){
            document.iacucSpecialReview.acType.value = 'I';
          } else {
            document.getElementById('add_label').style.display = 'block';
          }
          if(document.iacucSpecialReview.acType.value == 'U'){
            document.getElementById('add_label').style.display = 'none';
            showHide(0);
          }
          LINK = "<%=request.getContextPath()%>/iacucspecialReviewActions.do"; 
          FORM_LINK = document.iacucSpecialReview;
          PAGE_NAME = "<bean:message key="specialReviewLabel.SpecialReview"/>";
          function dataChanged(){
            DATA_CHANGED = 'true';
          }
          linkForward(errValue);
         </script>
	<script>
      var help = ' <bean:message key="helpTextProtocol.SpecialReview"/>';
      help = trim(help);
      if(help.length == 0) {
        document.getElementById('helpText').style.display = 'none';
      }
      function trim(s) {
            return s.replace( /^\s*/, "" ).replace( /\s*$/, "" );
      }  
      <%if(hasValidationMessage){%>
        showHide(0);
      <%}%>
                
  </script>
</body>

</html:html>
