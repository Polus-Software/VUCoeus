<? xml version="1.0" ?>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%--start 1--%>
<%@ page
	import="org.apache.struts.taglib.html.JavascriptValidatorTag,
                edu.mit.coeuslite.utils.CoeusLiteConstants"%>
<%--end 1--%>

<%@ page import="org.apache.struts.taglib.html.JavascriptValidatorTag"%>
<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<jsp:useBean id="areaData" scope="session" class="java.util.Vector" />
<%@ include file="/coeuslite/mit/utils/CoeusContextPath.jsp"%>

<html:html>
<head>
<title>Coeus Web</title>

<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
<link
	href="<bean:write name='ctxtPath'/>/coeuslite/mit/utils/css/coeus_styles.css"
	rel="stylesheet" type="text/css">
	<!--<link href="css/ipf.css" rel="stylesheet" type="text/css">
        <style type="text/css">
        </style>-->
<script language="javaScript" type="text/JavaScript">            
            function showList(frm) 
            {
            var winleft = (screen.width - 600) / 2;
            var winUp = (screen.height - 400) / 2;  
            <!-- COEUSQA:3209 - CoeusLite IACUC Areas of Research - Start -->
            <!-- var url_value = "<%=request.getContextPath()%>/areaSearch.do?type=area&form=" + frm +"&expand=false"; -->
            var url_value = "<%=request.getContextPath()%>/areaSearch.do?type=area&form=" + frm +"&expand=true";
            <!-- COEUSQA:3209 - End -->
            var win = "scrollbars=yes,resizable=1,width=750,height=450,left="+winleft+",top="+winUp;
            sList = window.open(url_value, "list", win);

            if (parseInt(navigator.appVersion) >= 4) {
            window.sList.focus(); 
            }

            }
            var errValue = false;
           function insert_data(data){
         
                document.areaForm.acType.value = data;
                 document.areaForm.action = "<%=request.getContextPath()%>/areaOfResearch.do";
               // document.areaForm.submit();
            
            }
            
            function delete_data(data,code,timestamp){
              if (confirm("Are you sure you want to delete the Area of Research?")==true){
                document.areaForm.acType.value = data;
                document.areaForm.areaTimeStamp.value = timestamp;
                document.areaForm.researchAreaCode.value = code;
                document.areaForm.action = "<%=request.getContextPath()%>/areaOfResearch.do";
                document.areaForm.submit();
              }
            }
            
            function save_Data(){
                document.areaForm.action = "<%=request.getContextPath()%>/areaOfResearch.do";
                document.areaForm.submit();            
            }
            function validateForm(form) {
                insert_data("I");
               // alert('validating'); 
                return validateAreaForm(form);
            }
        </script>

</head>
<body>
	<%--start 2--%>
	<% String mode=(String)session.getAttribute(CoeusLiteConstants.MODE_DETAILS+session.getId());       
            boolean modeValue=false;
            if(mode.equalsIgnoreCase(CoeusLiteConstants.DISPLAY_MODE)){
                modeValue=true;
            }else if(mode.equalsIgnoreCase(CoeusLiteConstants.MODIFY_MODE)){
                modeValue=false;
            }else if(mode.equalsIgnoreCase(CoeusLiteConstants.ADD_MODE)){
                modeValue=false;
              }
        %>

	<%String protocolNo = (String)session.getAttribute(CoeusLiteConstants.PROTOCOL_NUMBER+session.getId());

            if(protocolNo!= null){ 
                 protocolNo = "["+"Protocol No. - "+ protocolNo+"]";
            }else{
                protocolNo = "";
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
	<%--end 2--%>

	<html:form action="/areaOfResearch.do" method="post"
		onsubmit="return validateForm(this)">

		<!-- New Template for cwAreaOfResearch - Start   -->

		<script type="text/javascript">
    document.getElementById('txt').innerHTML = '<bean:message key="helpPageTextProtocol.AreasOfResearch"/>';
</script>
		<table width="100%" height="100%" border="0" cellpadding="0"
			cellspacing="0" class="table">
			<tr>
				<td>
					<table width='99%' cellpadding='0' cellspacing='0' class='tabtable'
						align=center>
						<tr class='tableheader'>
							<td height="20" align="left" valign="top" class="theader"><bean:message
									key="areasOfResch.AreasOfResearch" /></td>
							<td align="right"><a id="helpPageText"
								href="javascript:showBalloon('helpPageText', 'helpText')"> <bean:message
										key="helpPageTextProtocol.help" />
							</a></td>
						</tr>
					</table>
				</td>
			</tr>
			<tr>
				<td>
					<div id="helpText" class='helptext'>
						&nbsp;&nbsp;
						<bean:message key="helpTextProtocol.AreasOfResearch" />
					</div>
				</td>
				<!--
      </tr>
        <tr>
            <td class="copybold">
                &nbsp;&nbsp;
                <font color="red">*</font> 
                  <bean:message key="label.indicatesReqFields"/>   
            </td>
        </tr>-->
			<tr class='copy' align="left">
				<td><font color="red"> <logic:messagesPresent>
							<script>errValue = true;</script>
							<html:errors header="" footer="" />
						</logic:messagesPresent> <logic:messagesPresent message="true">
							<html:messages id="message" message="true">
								<li><bean:write name="message" /></li>
							</html:messages>
						</logic:messagesPresent>
				</font></td>
			</tr>
			<!--  Add Areas of Research - Start  -->
			<!--
    <tr>
      <td  align="left" valign="top" class='core'><table width="100%"  border="0" align="center" cellpadding="0" cellspacing="0" class="tabtable">
          <tr>
            <td colspan="4" align="left" valign="top"><table width="100%" height="20"  border="0" cellpadding="0" cellspacing="0" class="tableheader">
                <tr>
                  <td>  <bean:message key="areasOfResch.AddAreasOfResearch"/></td>
                </tr>
            </table></td>
          </tr>

           <tr>
                                  
                     <td width="1%">
                         <img src="<bean:write name='ctxtPath'/>/coeusliteimages/spacer.gif" width="0" height="0">
                    </td>
                    <td>
                        <table width="100%" border="0" cellpadding="0" cellspacing="5" >
                            <tr>
                            <td width="100%" height="15" colspan="6"></td>
                            </tr>
                            <tr>
                                <td width="14%" nowrap class="copybold"><font color="red">*</font><bean:message key="areasOfResch.Code"/>: </td>
                                <td class="copy" colspan="2"> 
                                    <html:text property="researchAreaCode" maxlength="8" styleClass="textbox-long" disabled="<%=modeValue%>" readonly="true"/>
                                    <%if(!modeValue){%>
                                    
                                        <html:link href="javascript:showList('areaForm')">
                                            <u><bean:message key="label.search"/></u>
                                        </html:link>
                                    <%}%>
                                </td>
                            </tr>
                            <tr>
                                <td width="14%" nowrap class="copybold"><bean:message key="areasOfResch.Description"/>:</td>
                                <td width="20%" class="copysmall" colspan="2">
                                    <html:text property="researchAreaDescription" styleClass="textbox-long" size="58" disabled="<%=modeValue%>" readonly="true" />
                                    
                                </td>
                                <td width="18%">
                                </td>
                            </tr>
                            <tr>
                            <td width="100%" height="10" colspan="6"></td>
                            </tr>
                                            

                        </table>
                    </td>
              </tr>
        <%if(!modeValue){%>
         <tr class='table'>
            <td class='savebutton'nowrap colspan="6" >
                 <html:submit property="Save" value="Save" styleClass="clsavebutton"/>

             </td>
         </tr>
         <%}%>     
         -->
			<html:hidden property="researchAreaCode" />
			<html:hidden property="researchAreaDescription" />

			<tr>
				<td class='tabtable' height='20' valign=middle>
					<%if(!modeValue){%> <html:link href="javascript:showList('areaForm')">
						<u><bean:message key="areasOfResch.addAreasOfResearch" /></u>
					</html:link> <%}%>
				</td>
			</tr>

			<!--  Add Areas of Research - End  -->


			<!-- List of Areas of Research - Start -->
			<tr>
				<td align="left" valign="top" class='core'><table width="100%"
						border="0" align="center" cellpadding="0" cellspacing="0"
						class="tabtable">
						<tr>
							<td colspan="4" align="left" valign="top"><table
									width="100%" height="20" border="0" cellpadding="0"
									cellspacing="0" class="tableheader">
									<tr>
										<td><bean:message key="areasOfResch.LstAreasofResch" /></td>
									</tr>
								</table></td>
						</tr>
						<tr>
							<td height='10'>&nbsp;</td>
						</tr>
						<tr align="center">
							<td colspan="3">
								<DIV
									STYLE="overflow: auto; width: 750px; height: 405px; padding: 0px; margin: 0px">
									<%
                                //if (areaData.size() > 0) 
                               // { %>
									<table width="100%" border="0" cellpadding="0" class="tabtable">

										<tr>
											<td width="20%" align="left" class="theader"><bean:message
													key="areasOfResch.Code" /></td>
											<td width="45%" align="left" class="theader"><bean:message
													key="areasOfResch.Description" /></td>
											<%if(!modeValue){%>
											<td width="15%" align="left" class="theader">&nbsp;</td>
											<%}%>
										</tr>
										<% String strBgColor = "#DCE5F1"; //BGCOLOR=EFEFEF  FBF7F7
                                                int count = 0;
                                 %>
										<logic:present name="areaData">
											<logic:iterate id="researchAreaList" name="areaData"
												type="org.apache.struts.validator.DynaValidatorForm">
												<% 
                                       if (count%2 == 0) 
                                          strBgColor = "#D6DCE5"; 
                                       else 
                                          strBgColor="#DCE5F1"; 
                                    %>
												<tr bgcolor="<%=strBgColor%>" width="20%" valign="top"
													onmouseover="className='TableItemOn'"
													onmouseout="className='TableItemOff'">
													<td align="left" nowrap class="copy"><%=researchAreaList.get("researchAreaCode")%>
													</td>
													<td width="45%" align="left" nowrap class="copy"><%=researchAreaList.get("researchAreaDescription")%></td>

													<%if(!modeValue){
                                             String removeLink = "javascript:delete_data('D','" +researchAreaList.get("researchAreaCode") +"','" +researchAreaList.get("areaTimeStamp") +"')";
                                         %>
													<td align="center" width="15%" nowrap class="copy"><html:link
															href="<%=removeLink%>">
															<bean:message key="fundingSrc.Remove" />
														</html:link> <%--a href="javascript:delete_data('D','<%=researchAreaList.get("researchAreaCode")%>','<%=researchAreaList.get("areaTimeStamp")%>')"> 
                                                    <bean:message key="fundingSrc.Remove"/>
                                                </a--%></td>
													<%}%>
												</tr>
												<% count++;%>
											</logic:iterate>
										</logic:present>

									</table>
									<%//}%>
								</DIV>
							</td>
						</tr>

						<tr>
							<td>&nbsp;</td>
						</tr>

					</table></td>
			</tr>
			<!-- List of Areas of Research -End -->

			<tr>
				<td height='10'>&nbsp;</td>
			</tr>
		</table>
		<!-- New Template for cwAreaOfResearch - End  -->


		<html:hidden property="acType" />
		<html:hidden property="protocolNumber" />
		<html:hidden property="sequenceNumber" />
		<html:hidden property="areaTimeStamp" />
	</html:form>
	<script>
          DATA_CHANGED = 'false';
          if(errValue) {
                DATA_CHANGED = 'true';
          }
          document.areaForm.acType.value = 'I';
          LINK = "<%=request.getContextPath()%>/areaOfResearch.do";
          FORM_LINK = document.areaForm;
          PAGE_NAME = "<bean:message key="areasOfResch.AreasOfResearch"/>";
          function dataChanged(){
            DATA_CHANGED = 'true';
          }
          linkForward(errValue);
         </script>
	<script>
      var help = ' <bean:message key="helpTextProtocol.AreasOfResearch"/>';
      help = trim(help);
      if(help.length == 0) {
        document.getElementById('helpText').style.display = 'none';
      }
      function trim(s) {
            return s.replace( /^\s*/, "" ).replace( /\s*$/, "" );
      }  
  </script>
</body>
</html:html>

