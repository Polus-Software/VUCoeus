<? xml version="1.0" ?>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ page
	import="org.apache.struts.taglib.html.JavascriptValidatorTag,
edu.mit.coeuslite.utils.CoeusLiteConstants,java.util.Vector,edu.mit.coeus.utils.ComboBoxBean"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<jsp:useBean id="vecExceptions" scope="session" class="java.util.Vector" />
<jsp:useBean id="vecAddedExceptions" scope="session"
	class="java.util.Vector" />
<%@ include file="/coeuslite/mit/utils/CoeusContextPath.jsp"%>

<html:html>
<head>
<%--Added for COEUSQA-2711_Increase IACUC Scientific Justification 1000 character-Start--%>
<link
	href="<bean:write name='ctxtPath'/>/coeuslite/mit/utils/css/coeus_styles.css"
	rel="stylesheet" type="text/css">
<script
	src="<%=request.getContextPath()%>/coeuslite/mit/utils/scripts/modal/modaldbox.js"></script>
<%--Added for COEUSQA-2711_Increase IACUC Scientific Justification 1000 character-End--%>
<link rel="stylesheet"
	href="<%=request.getContextPath()%>/coeuslite/mit/utils/scripts/modal/modaldbox.css"
	type="text/css" />
<title>Coeus Web</title>
<script language="javaScript" type="text/JavaScript">
            var errValue = false;
            var errLock = false;
            //Added For ISSUEID#1485 -user data entry is not restricted - start
            function setVisibility(val){
                document.iacucProtocolExceptionsForm.isExceptionVisible.value = "Y";
                
                showHide(val);
                changeVisibleFlagVale();
            }
            
             function changeVisibleFlagVale(){
                //document.getElementById("exceptionVisible").value = "yes";
             }
             
             function valueChanged(){
                changeVisibleFlagVale();
                dataChanged();
             }
            //Added For ISSUEID#1485 -user data entry is not restricted - end
            function showHide(val){
                    
                    if(val == 1){
                        document.getElementById('panel1').style.display = "block";
                    }else if(val == 2){
                        document.getElementById('panel2').style.display = "block";
                    }else if(val == 3){
                        //Added For ISSUEID#1485 -user data entry is not restricted - start
                        if(document.iacucProtocolExceptionsForm.isExceptionVisible.value == "Y"  ) {
                        //Added For ISSUEID#1485 -user data entry is not restricted - end
                            document.getElementById('showSpPanel').style.display = "none";
                             document.getElementById('hideSpPanel').style.display = "block";
                             if(document.getElementById('speciesPanel'))
                                 {
                                document.getElementById('speciesPanel').style.display = "block";
                             }
                             document.getElementById('speciesPanelSave').style.display = "block";
                        }
                    }else if(val == 4){
                    if(document.getElementById('showSpPanel')){
                         document.getElementById('showSpPanel').style.display = "block";
                          document.getElementById('hideSpPanel').style.display = "none";
                          document.getElementById('speciesPanel').style.display = "none";
                          document.getElementById('speciesPanelSave').style.display = "none";
                          if(document.getElementById('showValPanel'))
                            {
                            document.getElementById('showValPanel').style.display = "none";
                            }
                            }
                    }
            }   
            
            function saveSpeciesData(){
                document.iacucProtocolExceptionsForm.Save.disabled=true;
                //document.iacucProtocolExceptionsForm.exceptionCategoryCode.disabled=false;  
                if( document.iacucProtocolExceptionsForm.acType.value != 'U'){
                    document.iacucProtocolExceptionsForm.acType.value = 'I' ;
                }
                document.iacucProtocolExceptionsForm.action = "<%=request.getContextPath()%>/saveIacucProtoException.do";
                document.iacucProtocolExceptionsForm.submit();
            }
            
            function modifyException(exceptionId){
                document.iacucProtocolExceptionsForm.action = "<%=request.getContextPath()%>/loadIacucProtoExceptionDetails.do?exceptionId="+exceptionId;
                document.iacucProtocolExceptionsForm.submit();
            }
            
            function removeException(exceptionId, updateTimestamp){
             if (confirm("Are you sure you want to remove this Exception?")==true){
                document.iacucProtocolExceptionsForm.action = "<%=request.getContextPath()%>/removeIacucProtoException.do?exceptionId="+exceptionId+"&updateTimestamp="+updateTimestamp;
                document.iacucProtocolExceptionsForm.submit();
                }
            }
            function clearFormData(){                 
                document.iacucProtocolExceptionsForm.acType.value = '' ;
                //document.iacucProtocolExceptionsForm.exceptionCategoryCode.value = '' ;
                //document.iacucProtocolExceptionsForm.exceptionDescription.value = '' ;
                //document.getElementById("exceptionVisible").value = "no";
                showHide(4);
                 
            }
            
            function savePrinciples(){
                document.iacucProtocolExceptionsForm.action = "<%=request.getContextPath()%>/saveIacucProtoPrinciples.do";
                document.iacucProtocolExceptionsForm.submit();
            }
            
            <!--Added for ISSUEID#1831 - Validation Message not Handled - Lite (Wrapping) - Start -->           
            function viewDescription(count) {
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
                var loc = '<bean:write name='ctxtPath'/>/coeuslite/mit/utils/dialogs/cwIACUCView.jsp?recordId='+count+'&selectedItem=Description'+'&type=E';
                var newWin = window.open(loc,"formName",'dependent=1,toolbar=0,location=0,directories=0,status=0, menubar=0,scrollbars=0,resizable=0,width=' + w + ',height=' + h+ ',left='+ leftPos + ',top=' + topPos);
                newWin.window.focus();
            }
            <!--Added for ISSUEID#1831 - Validation Message not Handled - Lite (Wrapping) - End -->
            
      <!--Added for COEUSQA-2711_Increase IACUC Scientific Justification 1000 character-Start -->
     function showDialog(divId){
       if(navigator.appName == "Microsoft Internet Explorer") {
            document.getElementById(divId).style.width = 400;
            document.getElementById('inside'+divId).style.width = 400;
            document.getElementById('inside'+divId).style.height = 180;
            document.getElementById('summaryTable').style.width = 300;
            document.getElementById('summaryTextArea').style.width = 393;
        }
        //Default w,h is for 1024 by 768 pixels screen resolution
        var w = 220;
        var h = 350;
        var screenWidth = window.screen.width;
        var screenHeight = window.screen.height;
        if(screenWidth == 800 && screenHeight == 600){
                w = 2;
                h = 175;
        } else if(screenWidth == 1152 && screenHeight == 864){
                w = 350;
                h = 450;
        } else if(screenWidth == 1280 && screenHeight == 720){
                w = 475;
                h = 300;
        }else if(screenWidth == 1280 && screenHeight == 768){
                w = 475;
                h = 350;
        }else if(screenWidth == 1280 && screenHeight == 1024){
                w = 475;
                h = 600;
        }
        
        //widt, height is for pop-up dialog 
        var width =  Math.floor(((screenWidth - w) / 2));
        var height = Math.floor(((screenHeight - h) / 2));
        sm(divId,width,height);
     }
     <!--Added for COEUSQA-2711_Increase IACUC Scientific Justification 1000 character-End -->
            
            
    </script>
<style>
.textbox-longer {
	font-weight: normal;
	width: 500px
}
</style>
</head>
<body>

	<% 
String mode=(String)session.getAttribute(CoeusLiteConstants.MODE_DETAILS+session.getId());
boolean modeValue=false;
String EMPTY_STRING = "";
if(mode.equalsIgnoreCase(CoeusLiteConstants.DISPLAY_MODE)){
    modeValue=true;
}else if(mode.equalsIgnoreCase(CoeusLiteConstants.MODIFY_MODE)){
    modeValue=false;
}else if(mode.equalsIgnoreCase(CoeusLiteConstants.ADD_MODE)){
    modeValue=false;
}
%>

	<%String protocolNo = (String)session.getAttribute(CoeusLiteConstants.IACUC_PROTOCOL_NUMBER+session.getId());

if(protocolNo!= null){ 
protocolNo = "["+"Protocol No. - "+ protocolNo+"]";
}else{
protocolNo = "";
}

String strProtocolNum = (String)session.getAttribute(CoeusLiteConstants.IACUC_PROTOCOL_NUMBER+session.getId());
if(strProtocolNum == null)
strProtocolNum = "";    
String amendRenewPageMode = (String)session.getAttribute("amendRenewPageMode"+request.getSession().getId());
if(strProtocolNum.length() > 10 && (amendRenewPageMode == null || 
amendRenewPageMode.equalsIgnoreCase(CoeusLiteConstants.DISPLAY_MODE))){
modeValue = true;

}%>
	<%try{%>

	<html:form action="/getIacucProtoExceptions.do" method="post"
		onsubmit="validateForm(this)">
		<a name="top"></a>

		<script type="text/javascript">
    document.getElementById('txt').innerHTML = '<bean:message bundle="iacuc" key="helpPageTextProtocol.ScientificJustification"/>';
    </script>

		<table width="100%" height="100%" border="0" cellpadding="0"
			cellspacing="0" class="table">

			<tr>
				<td>
					<table width="100%" border="0" cellpadding="0" cellspacing="0">
						<tr>
							<!-- JM 5-31-2011 updated classes per 4.4.2 -->
							<td height="20" align="left" valign="center" class="tableheader">
								&nbsp;Principles</td>
							<td height="20" align="right" valign="top" class="tableheader">
								<a id="helpPageText"
								href="javascript:showBalloon('helpPageText', 'helpText')"> <u><bean:message
											bundle="iacuc" key="helpPageTextProtocol.help" /></u>
							</a>
							</td>&nbsp;&nbsp;
						</tr>
					</table>
				</td>
			</tr>

			<logic:messagesPresent message="true">

				<tr class='copy' align="left">
					<td><div id='showValPanel'>
							<font color="red"> <script>errValue = true;</script> <html:errors
									header="" footer="" /> <html:messages id="message"
									message="true">

									<html:messages id="message" message="true"
										property="invalidExceptionCode" bundle="iacuc">
										<script>errLock = true;</script>
										<li><bean:write name="message" /></li>
									</html:messages>
									<html:messages id="message" message="true"
										property="invalidExceptionDesc" bundle="iacuc">
										<script>errLock = true;</script>
										<li><bean:write name="message" /></li>
									</html:messages>



								</html:messages> <%--Added For ISSUEID#1485 -user data entry is not restricted - start--%>
								<html:messages id="message" message="true"
									property="invalidPrincipleReduction" bundle="iacuc">
									<script>errLock = true;</script>
									<li><bean:write name="message" /></li>
								</html:messages> <html:messages id="message" message="true"
									property="invalidPrincipleRefinement" bundle="iacuc">
									<script>errLock = true;</script>
									<li><bean:write name="message" /></li>
								</html:messages> <html:messages id="message" message="true"
									property="invalidPrincipleReplacement" bundle="iacuc">
									<script>errLock = true;</script>
									<li><bean:write name="message" /></li>
								</html:messages> <%--Added For ISSUEID#1485 -user data entry is not restricted - end--%>

							</font>
						</div></td>
				</tr>
			</logic:messagesPresent>



			<tr nowrap class="copy" align="left">
				<td>
					<table width="100%" border="0" cellpadding="2" cellspacing="2"
						class="tabtable">

						<tr>
							<td nowrap class="copybold" align="right" valign="top">
								Principles of Reduction:</td>
							<td class="copy" align="left">&nbsp; <html:textarea
									name="iacucProtocolExceptionsForm" onblur=""
									property="reductionPrinciple" readonly="<%=modeValue%>"
									onchange="dataChanged()" rows="5" cols="70" />
							</td>
						</tr>
						<tr>
							<td nowrap class="copybold" align="right" valign="top">
								Principles of Refinement:</td>
							<td class="copy" align="left">&nbsp; <html:textarea
									name="iacucProtocolExceptionsForm" onblur=""
									property="refinementPrinciple" readonly="<%=modeValue%>"
									onchange="dataChanged()" rows="5" cols="70" />
							</td>
						</tr>
						<tr>
							<td nowrap class="copybold" align="right" valign="top">
								Principles of Replacement:</td>
							<td class="copy" align="left">&nbsp; <html:textarea
									name="iacucProtocolExceptionsForm" onblur=""
									property="replacementPrinciple" readonly="<%=modeValue%>"
									onchange="dataChanged()" rows="5" cols="70" />
							</td>
						</tr>
						<!--tr>  
                        <td class="copybold" nowrap  align="right" valign="top">
                            Exception:
                        </td>     
                        <td class="copy" align="left"><html:checkbox property="exceptionPresent" styleClass="copy" disabled="<%=modeValue%>"  onchange="dataChanged()"/>
                              
                            </td>
                    </tr-->
						<%--Added For ISSUEID#1485 -user data entry is not restricted - start--%>
						<html:hidden name="iacucProtocolExceptionsForm"
							property="isExceptionVisible" />
						<%--Added For ISSUEID#1485 -user data entry is not restricted - end--%>
						<table width="99%" border="0" cellpadding="0" cellspacing="5">
							<%if(!modeValue){%>
							<tr class='table'>
								<td width="15%" nowrap class="copybold" align="left"><html:button
										property="Save" value="Save" styleClass="clsavebutton"
										onclick="savePrinciples();" /></td>
							</tr>
							<%}%>
						</table>
					</table>
				</td>
			</tr>






			<%--tr>
            <td  align="left" valign="top" class='core'><table width="100%"  border="0" align="left" cellpadding="0" cellspacing="0" class="tabtable">
                
                  <tr>
                        <td class="theader">
                            Exceptions for this Protocol
                        </td>    
                    </tr>
                 
                     <%if(!modeValue){%>
                    <tr>
                        <td colspan="4" align="left" valign="top" class='table'>
                                        <div id='showSpPanel' class='tabtable'> &nbsp;                                            
                                            <%String divlink = "javascript:setVisibility(3)";%>                                            
                                            <html:link href="<%=divlink%>">                                              
                                                <u>Add Exceptions</u>
                                            </html:link>
                                        </div>
                                        </td>
                    </tr>                 
                    <tr>
                        <td>
                            <div id='speciesPanel' style='display:none;'> 
                                <table width="100%" border="0" cellpadding="0" cellspacing="5" >                                    
                                    <tr>
                                        <td  nowrap class="copybold" align = "right">
                                            <font color="red">*</font>Exception Category:                                   
                                        </td>
                                        
                                        <td nowrap class="copy">
                                            <html:select name="iacucProtocolExceptionsForm" property="exceptionCategoryCode" styleClass="textbox-long" disabled="<%=modeValue%>" onchange="valueChanged()">
                                                <html:option value=""> <bean:message bundle="proposal" key="generalInfoProposal.pleaseSelect"/> </html:option>
                                                <html:options collection="vecExceptions" property="code" labelProperty="description"/>
                                                
                                            </html:select>
                                        </td>
                                        <td nowrap class="copybold" align="right">
                                           
                                        </td>
                                        
                                        <td class="copy">                                    
                                            
                                        </td>  
                                    </tr>
                                    
                                    <tr>
                                        <td nowrap class="copybold" align = "right" valign="top">
                                            <font color="red">*</font>Description:                               
                                        </td>
                                        <td colspan="3"> 
                                           <html:textarea property="exceptionDescription" name="iacucProtocolExceptionsForm" cols="99" rows="3" styleClass="copy" onchange="valueChanged()"/>
                                        </td>
                                        
                                    </tr>
                                    
                                </table>
                            </div>
                        </td>
                    </tr>            
                    <%}%>
                </table>
            </td>
        </tr--%>

			<tr>
				<td class='copy' nowrap colspan="6">
					<div id='speciesPanelSave' style='display: none;' class="copybold">
						<table width="99%" border="0" cellpadding="0" cellspacing="5">
							<tr class='copy'>
								<td width="15%" nowrap class="copybold" align="left"><html:button
										property="Save" value="Save" styleClass="clsavebutton"
										onclick="saveSpeciesData();" /></td>
								<td nowrap class="copybold" align="left">
									<div id='hideSpPanel' style='display: none;' class="copybold">
										<html:button property="Cancel" value="Cancel"
											styleClass="clsavebutton" onclick="clearFormData();" />

									</div>
								</td>



							</tr>
						</table>
					</div>
				</td>
			</tr>



			<tr>
				<td align="left" valign="top" class='theader'><table
						width="100%" border="0" align="center" cellpadding="0"
						cellspacing="0" class="tabtable">


						<tr align="center">

							<td colspan="5">
								<%--<table width="100%" border="0" cellpadding="0" class="tabtable" >
                                <tr>
                                   <td width="25%"  align="left" class="theader">Exception Category</td>
                                   <td width="55%%" align="left" class="theader"> Description</td>
                                   <td width="10%" align="center" class="theader"></td>
                                   <td width="10%" align="center" class="theader"></td>
                                </tr> 
                                                       
                              <%  String strBgColor = "#DCE5F1";
                                   int count = 0;
                                %>  
                                                       <logic:notEmpty name="vecAddedExceptions">
                                                       
                                                       <logic:iterate id="exceptionsData"  name="vecAddedExceptions"  type="org.apache.struts.validator.DynaValidatorForm" indexId="index" >  
                                                       <% 
                                                       if (count%2 == 0)
                                                       strBgColor = "#D6DCE5";
                                                       else
                                                       strBgColor="#DCE5F1";
                                                       %>   
                                                       
                                                       <tr bgcolor="<%=strBgColor%>" valign="top" onmouseover="className='TableItemOn'" onmouseout="className='TableItemOff'">
                                                       <td width="25%" align="left" class="copy"><bean:write name="exceptionsData" property="exceptionCategoryDesc"/></td>
                                                       
                                                       <td width="66%" align="left" class="copy">
                                                       <%
                                                        String description  = (String)exceptionsData.get("exceptionDescription");
                                                        if(description != null && description.length()>80){%>
                                                       <div  id="<%=count%>" class="dialog" style="width:450px;height:200px;display:none;">
                                                           <div id="inside<%=count%>" style="overflow:auto;width:450px;height:186px">
                                                               <table cellpadding='0' cellspacing='1' align=left class='tabtable' width='400px'>
                                                                   <tr class='theader'>
                                                                       <td>
                                                                            <bean:message key="scientifiJustification.exceptionCategoryDesc" bundle="iacuc"/>
                                                                       </td>
                                                                   </tr>
                                                               </table>
                                                               <table border="0" id="summaryTable" cellpadding="2" cellspacing="0" class="lineBorderWhiteBackgrnd" width="400" height="150">
                                                                   <tr>
                                                                       <td>
                                                                           <html:textarea styleId="summaryTextArea" property="txtDesc" styleClass="copy" readonly="true" value="<%=description%>"  
                                                                                          disabled="false"  rows="9"  style="border-top-width:0px; border-right-width:0px; 
                                                                                          border-bottom-width:0px; border-left-width:0px;width:395px" />
                                                                       </td>
                                                                   </tr>
                                                               </table>
                                                               
                                                           </div>
                                                           <input type="button" onclick="javascript:hm('description')" value="Close">
                                                       </div>
                                                       <%}%>
                                                       
                                                       <%                                                       
                                                       String showSummary = "javascript:showDialog('"+count+"')";
                                                       if(description != null && description.length()>80){
                                                       description = description.substring(0,79);
                                                       %>
                                                       <%=description%><html:link href="<%=showSummary%>">&nbsp;[...]</html:link>
                                                       <%}else{%>
                                                       <%=description%>
                                                       <%}%>
                                                   </td>                                                   
                                        
                                        <%if(!modeValue){%>
                                        <td width="9%" align="center" class="copy"><a href="javaScript:modifyException('<bean:write name="exceptionsData" property="exceptionId"/>');" >Modify</a></td>
                                        <td width="9%" align="center" class="copy"><a href="javaScript:removeException('<bean:write name="exceptionsData" property="exceptionId"/>','<bean:write name="exceptionsData" property="awUpdateTimeStamp"/>');" >Remove</a></td>
                                        <%}else
                                        {%>
                                        <td width="7%" align="center" class="copy"></td>
                                        <td width="7%" align="center" class="copy"></td>
                                        <%}%>
                                    </tr>                                    
                                    <% count++;%>    
                                    
                                </logic:iterate>
                            </logic:notEmpty>
                            
                            
                            
                        <input type = "hidden" id="exceptionVisible" name ="exceptionVisible" value="no"/--%>
						<tr>
							<td colspan="5" align="left" valign="top" class='copy'>
								<div id='panel1' style='display: none;'>
									<table width="100%" border="0" align="center" cellpadding="0"
										cellspacing="0" class="copy">

									</table>
								</div>
							</td>
						</tr>

						</td>
						</tr>

					</table></td>
			</tr>


		</table>
		</td>
		</tr>

		<html:hidden property="acType" />
		<html:hidden property="protocolNumber" />
		<html:hidden property="sequenceNumber" />
		<html:hidden property="updateTimeStamp" />

		<html:hidden property="updateUser" />
		<html:hidden property="awUpdateTimeStamp" />

		<html:hidden property="awUpdateUser" />
		<html:hidden property="exceptionId" />
		<html:hidden property="awExceptionId" />
		<html:hidden property="awPriciplesUpdateTimeStamp" />
		<html:hidden property="principlesAcType" />


		<tr>

		</tr>
		</table>


	</html:form>
	<script>
          DATA_CHANGED = 'false';
          if(errValue && !errLock) {
                DATA_CHANGED = 'true';
          }
          //document.vulerable.acType.value = 'I';
         // LINK = "<%=request.getContextPath()%>/saveIacucProtoPrinciples.do"; 
         
          LINK = "<%=request.getContextPath()%>/saveIacucProtoPrinciplesAndException.do"; 
         
          FORM_LINK = document.iacucProtocolExceptionsForm;
          PAGE_NAME = "<bean:message key="scientificJustification.scieJustifcation" bundle="iacuc"/>";
          function dataChanged(){
                DATA_CHANGED = 'true';
          }
          linkForward(errValue);
</script>
	<script>
      var help = '<bean:message key="helpTextProtocol.Subjects"/>';
      help = trim(help);
      if(help.length == 0) {
            document.getElementById('helpText').style.display = 'none';
      }
      function trim(s) {
            return s.replace( /^\s*/, "" ).replace( /\s*$/, "" );
      }  
      if( document.iacucProtocolExceptionsForm.acType.value == 'U' || errValue == true){
        showHide(3);
      } else if(!<%=modeValue%>){
        clearFormData();
      }
</script>
</body>
<%}catch(Exception e){
       e.printStackTrace();
    }
       %>
</html:html>
