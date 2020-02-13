
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%@ page contentType="text/html;charset=UTF-8" language="java"%>

<%--start 1--%>
<%@page
	import="org.apache.struts.taglib.html.JavascriptValidatorTag,
edu.mit.coeuslite.utils.CoeusLiteConstants, java.util.Vector"%>
<%--end 1--%>

<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ include file="/coeuslite/mit/utils/CoeusContextPath.jsp"%>

<html:html>
<head>

<title>Coeus Web</title>
<style>
.cltextbox-color {
	font-weight: normal;
	width: 500px
}
</style>
<html:base />
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
<%--<link href="<bean:write name='ctxtPath'/>/coeuslite/mit/utils/css/coeus_styles.css" rel="stylesheet" type="text/css">--%>

<script>
    var validFlag;
    var idx;
    var errValue = false;
    var selectedValue=0;
    function func_not(){
        return false;
    }
    
    function insert_data(data){
        document.iacucFundingSourceForm.acType.value = 'I';
        document.iacucFundingSourceForm.action = "<%=request.getContextPath()%>/iacucfundingSource.do";
    }
    
    function findType(){
   
        if(document.iacucFundingSourceForm.type.selectedIndex==1){
            
        }
        else if(document.iacucFundingSourceForm.type.selectedIndex==2){
            
        }
        else if(document.iacucFundingSourceForm.type.selectedIndex==3){
            
        }
    }
    
    function open_search(valid,index)
          {
          
            validFlag = valid;
            idx = index;
            var winleft = (screen.width - 650) / 2;
            var winUp = (screen.height - 450) / 2;  
            var win = "scrollbars=1,resizable=1,width=800,height=450,left="+winleft+",top="+winUp
            switch(index){
                case 1:
                    sList = window.open('<%=request.getContextPath()%>/iacucSearch.do?type=<bean:message key="searchWindow.title.sponsor"/>&search=true&searchName=WEBSPONSORSEARCH', "list", win);
                    break;
                case 2:
                    sList = window.open('<%=request.getContextPath()%>/iacucSearch.do?type=<bean:message key="searchWindow.title.unit"/>&search=true&searchName=WEBLEADUNITSEARCH', "list", win);
                    break;
                case 3:
                    break;
                case 4:
                    sList = window.open('<%=request.getContextPath()%>/iacucSearch.do?type=<bean:message key="searchWindow.title.devproposal"/>&search=true&searchName=ALL_PROPOSALDEVSEARCHNOROLES', "list", win);
                    break;
                case 5:
                     //Commented and Added for COEUSQA-2428_CLONE -LIte - Prop Dev - Application does not return any rows while searching for Original proposal_Start
                    //sList = window.open('<%=request.getContextPath()%>/iacucSearch.do?type=<bean:message key="searchWindow.title.proposal"/>&search=true&searchName=PROPOSALSEARCH', "list", win);
                    sList = window.open('<%=request.getContextPath()%>/iacucSearch.do?type=<bean:message key="searchWindow.title.proposal"/>&search=true&searchName=INSTITUTEPROPOSALSEARCH', "list", win);
                    //Commented and Added for COEUSQA-2428_CLONE -LIte - Prop Dev - Application does not return any rows while searching for Original proposal_End
                    break;
                case 6:
                    sList = window.open('<%=request.getContextPath()%>/iacucSearch.do?type=<bean:message key="searchWindow.title.award"/>&search=true&searchName=ALL_AWARD_SEARCH', "list", win);
                    break;
            }
            if (parseInt(navigator.appVersion) >= 4) {
            window.sList.focus(); 
            }
          }
          
     function fetch_Data(result){         
         switch(idx){
            case 1:
                document.iacucFundingSourceForm.fundingSource.value = result["SPONSOR_CODE"];
                document.iacucFundingSourceForm.fundingSourceName.value = result["SPONSOR_NAME"];
                break;
            case 2:
                document.iacucFundingSourceForm.fundingSource.value = result["UNIT_NUMBER"];
                document.iacucFundingSourceForm.fundingSourceName.value = result["UNIT_NAME"]; 
                break;
            case 4:
                document.iacucFundingSourceForm.fundingSource.value = result["PROPOSAL_NUMBER"];
                if(result["TITLE"] != 'null' && result["TITLE"] != undefined ){ 
                    document.iacucFundingSourceForm.fundingSourceName.value = result["TITLE"];
                }else{
                    document.iacucFundingSourceForm.fundingSourceName.value = "";
                }
                
                break;
            case 5:;
                document.iacucFundingSourceForm.fundingSource.value = result["PROPOSAL_NUMBER"];
                document.iacucFundingSourceForm.fundingSourceName.value = result["TITLE"];             
                break;
            case 6:
                document.iacucFundingSourceForm.fundingSource.value = result["MIT_AWARD_NUMBER"];
                document.iacucFundingSourceForm.fundingSourceName.value = result["TITLE"];             
                break;
         }
         dataChanged();
         
     }
     
    function search(){         
            if(document.iacucFundingSourceForm.code.value==0){
                alert("Please Select a Type");
            }            
            if(document.iacucFundingSourceForm.code.value==1){
                open_search(true,1);
            }
            if(document.iacucFundingSourceForm.code.value==2){
                open_search(false,2);
            }
            if(document.iacucFundingSourceForm.code.value==3){
                open_search(false,3);
            }
            if(document.iacucFundingSourceForm.code.value==4){
                open_search(false,4);
            }
            if(document.iacucFundingSourceForm.code.value==5){
                open_search(false,5);
            }
            if(document.iacucFundingSourceForm.code.value==6){
                open_search(false,6);
            }
    }
         
     function delete_data(data,typeCode,fundingSourceVal,timestamp){
            if (confirm("Are you sure you want to delete the funding source?")==true){
                document.iacucFundingSourceForm.acType.value = data;
            //   document.iacucFundingSourceForm.code.value = typeCode;
            //    document.iacucFundingSourceForm.fundingSource.value = fundingSourceVal;                
                document.iacucFundingSourceForm.fundingSourceTimeStamp.value=timestamp;
                document.iacucFundingSourceForm.action = "<%=request.getContextPath()%>/iacucdeleteFundingSource.do?fundingSourceNumber=" +fundingSourceVal;
                document.iacucFundingSourceForm.submit();
            }
        }
            
                  
     function clearFields(sel){        
        document.iacucFundingSourceForm.fundingSource.value = "";
        document.iacucFundingSourceForm.fundingSourceName.value = "";
        var typeCode = sel.options[sel.selectedIndex].value; 
        
        //Added/Commented for COEUSQA-3064 Unable to save funding source type in Lite if it is other than base types supplied-start
        //if(typeCode == '3'){            
        //    document.getElementById('hide_Search').style.display = 'none';            
        //}
        /*
        TypeCode 1 - Sponsor
        TypeCode 2 - Unit
        TypeCode 4 - Development Proposal
        TypeCode 5 - Institute Proposal
        TypeCode 6 - Award
        */
        if(typeCode == '1' || typeCode == '2' || typeCode == '4'
          || typeCode == '5'|| typeCode == '6'){            
            document.getElementById('hide_Search').style.display = 'block';            
        }else{            
            document.getElementById('hide_Search').style.display = 'none';            
        }      
        //Added/Commented for COEUSQA-3064 Unable to save funding source type in Lite if it is other than base types supplied-end       
        dataChanged();
     }      
     
        function showHide(toggleId) {
               // alert(toggleId); 
                //1-Show 0- Hide
             if(toggleId == 1){
                document.getElementById('showOrHide').style.display = "none";
                selectedValue=0;
            }else if(toggleId == 0){
                document.getElementById('showOrHide').style.display = "";
                document.getElementById('showFundingSource').style.display = "none"; 
                selectedValue=1;
            }
          }
          function cancelFundingSource(){
            //document.getElementById('showFundingSource').style.display = ""; 
            //document.getElementById('showOrHide').style.display = "none";
            document.iacucFundingSourceForm.action = "<%=request.getContextPath()%>/getIacucData.do?PAGE=F";
            document.iacucFundingSourceForm.submit();
          }
        </script>

<script>
 function validateForm(form) {
 
     insert_data('I');
     //return validateFundingSourceFrm(form);
   }
        </script>


</head>
<body>


	<%--start 2--%>

	<% boolean hasValidationMessage = false;
        String protocolNo = (String)session.getAttribute(CoeusLiteConstants.IACUC_PROTOCOL_NUMBER+session.getId());
        
        if(protocolNo!= null){
            protocolNo = "["+"Protocol No. - "+ protocolNo+"]";
        }else{
            protocolNo = "";
        }
        
        String mode=(String)session.getAttribute(CoeusLiteConstants.MODE_DETAILS+session.getId());
        Boolean fundingSourceMode = (Boolean) session.getAttribute("MODIFY_IACUC_PROTOCOL_FUNDING_SOURCE"+session.getId());
        boolean modeValue=false;
        if(mode.equalsIgnoreCase(CoeusLiteConstants.DISPLAY_MODE)){
            if(fundingSourceMode.booleanValue()){
                modeValue=false;
            }else{
                modeValue=true;
            }
        }else if(mode.equalsIgnoreCase(CoeusLiteConstants.MODIFY_MODE)){
            modeValue=false;
        }else if(mode.equalsIgnoreCase(CoeusLiteConstants.ADD_MODE)){
            modeValue=false;
        }
        Vector fundingSources = (Vector) session.getAttribute("iacucFundingSources");
        //Added for coeus4.3 Amendments and Renewal enhancement
        String strProtocolNum = (String)session.getAttribute(CoeusLiteConstants.IACUC_PROTOCOL_NUMBER+session.getId());
        if(strProtocolNum == null)
            strProtocolNum = "";
        String amendRenewPageMode = (String)session.getAttribute("amendRenewPageMode"+request.getSession().getId());
        if(strProtocolNum.length() > 10 && (amendRenewPageMode == null ||
                amendRenewPageMode.equalsIgnoreCase(CoeusLiteConstants.DISPLAY_MODE))){
            modeValue = true;
        }%>

	<%--end 2--%>


	<html:form action="/iacucfundingSource" method="post"
		onsubmit="validateForm(this);">
		<a name="top"></a>
		<%--  ************  START OF BODY TABLE  ************************--%>

		<!-- New Template for cwFundingSourceForm - Start   -->
		<script type="text/javascript">
    document.getElementById('txt').innerHTML = '<bean:message bundle="iacuc" key="helpPageTextProtocol.FundingSource"/>';
            </script>
		<table width="100%" height="100%" border="0" cellpadding="0"
			cellspacing="0" class="table">

			<tr>
				<td>
					<table width="100%" border="0" cellpadding="0" cellspacing="0">
						<tr>
							<td width="50%" height="20" align="left" valign="top"
								class="theader"><bean:message
									key="fundingSrc.FundingSources" /></td>
							<td width="50%" class="theader" align="right"><a
								id="helpPageText"
								href="javascript:showBalloon('helpPageText', 'helpText')"> <bean:message
										bundle="iacuc" key="helpPageTextProtocol.help" />
							</a></td>
						</tr>
					</table>
				</td>
			</tr>

			<%-- Commented for case id#2627
        <tr>
            <td class="copybold">
                &nbsp;&nbsp;
                <font color="red">*</font> 
                <bean:message key="label.indicatesReqFields"/>
                <!--Indicates Required Fields-->
            </td>
        </tr>    
        --%>
			<tr class='copy' align="left">
				<td><font color="red"> <logic:messagesPresent>
							<script>errValue = true;</script>
							<%hasValidationMessage = true;%>
							<html:errors header="" footer="" />
						</logic:messagesPresent> <logic:messagesPresent message="true">
							<script>errValue = true;</script>
							<%hasValidationMessage = true;%>
							<html:messages id="message" message="true">
								<li><bean:write name="message" /></li>
							</html:messages>
						</logic:messagesPresent>
				</font></td>
			</tr>
			<!-- Add Funding Source - Start  -->
			<%if(!modeValue){%>
			<tr class='table'>
				<td><div id="showFundingSource">
						<a href="javascript:showHide(selectedValue);"><u>Add
								Funding Source</u></a>
					</div></td>
			</tr>
			<%}%>
			<tr>
				<td align="left" valign="top" class='core'>
					<%-- <table width="100%"  border="0" align="center" cellpadding="2" cellspacing="0" class="tabtable">
                                               
                            
                            <tr>
                                <td width="70%" valign="top" class="copy" align="left">--%>
					<div id="showOrHide" style="display: none">
						<table width="100%" border="0" cellpadding="0" class="tabtable"
							cellspacing='3'>
							<tr>
								<td class="copysmall" colspan="4"><bean:message
										key="helpText.fundingSource.Modify" bundle="iacuc" /></td>
							</tr>
							<tr>
								<td nowrap class="copybold" width="10%" align="right">
									<%-- Commented for case id#2627
                                                    <font color="red">*</font>
                                                    --%> <bean:message
										key="fundingSrc.Type" />:&nbsp;
								</td>
								<td class="copysmall" width="20%" align="left"><html:select
										property="code" styleClass="textbox-long"
										disabled="<%=modeValue%>" onchange="clearFields(this)">
										<html:option value="">
											<bean:message bundle="proposal"
												key="generalInfoProposal.pleaseSelect" />
										</html:option>
										<html:options collection="fundingTypes" property="code"
											labelProperty="description" />>
                                                </html:select></td>
								<td width="10%" align="left">&nbsp;</td>
								<td width="60%" align="left">&nbsp;</td>
							</tr>

							<tr>
								<td nowrap class="copybold" width="10%" align="right">
									<%-- Commented for case id#2627
                                                    <font color="red">*</font>
                                                    --%> <bean:message
										key="fundingSrc.SpnsUnitNo" />:&nbsp;
								</td>
								<td class="copysmall" width="20%" align="left"><html:text
										property="fundingSource" styleClass="textbox-long"
										onchange="dataChanged()" disabled="<%=modeValue%>"
										maxlength="200" /></td>
								<td width="10%" align="left">
									<div id='hide_Search' style='display: block;'>
										<%if(!modeValue){%>
										<html:link href="javascript:search()">
											<u><bean:message key="label.search" /></u>
										</html:link>
										<%}%>
									</div>
								</td>
								<td width="60%" align="left">&nbsp;</td>
							</tr>

							<tr>
								<td nowrap class="copybold" width="10%" align="right"><bean:message
										key="fundingSrc.Name" />:&nbsp;</td>
								<td colspan="3"><html:text property="fundingSourceName"
										styleClass="cltextbox-color" readonly="true" /></td>
							</tr>
							<%if(!modeValue){%>
							<tr class="table">
								<td colspan="4" width="100%" class="table"><html:submit
										property="Save" value="Save" styleClass="clsavebutton" /> <html:button
										property="Save" styleClass="clsavebutton"
										onclick="cancelFundingSource()">
										<bean:message bundle="iacuc" key="button.label.cancel" />
									</html:button></td>
							</tr>
							<%}%>
						</table>
					</div> <%--   </td>     
                            </tr> 
                            
                      </table>--%>
				</td>
			</tr>

			<!-- Add Funding Source - End  -->


			<!-- List of Funding Source - Start -->
			<tr>
				<td align="left" valign="top" class='core'><table width="100%"
						border="0" align="center" cellpadding="0" cellspacing="0"
						class="tabtable">
						<tr align="center">
							<td colspan="3">
								<table width="100%" border="0" cellpadding="0" class="tabtable">
									<tr>
										<td width="20%" align="left" class="theader"><bean:message
												key="fundingSrc.Type" /></td>
										<td width="25%" align="left" class="theader"><bean:message
												key="fundingSrc.SpnsUnitNo" /></td>
										<td width="45%" align="left" class="theader"><bean:message
												key="fundingSrc.Name" /></td>
										<%if(!modeValue){%>
										<td width="10%" align="left" class="theader"></td>
										<%}%>
									</tr>
									<%
                                        //if (fundingSources.size() > 0)
                                        //{
                                        String strBgColor = "#DCE5F1"; //BGCOLOR=EFEFEF  FBF7F7
                                        int count = 0;
                                        %>
									<logic:present name="iacucFundingSources">
										<logic:iterate id="fundingSourceBean"
											name="iacucFundingSources"
											type="org.apache.struts.validator.DynaValidatorForm">

											<% 
                                                if (count%2 == 0)
                                                    strBgColor = "#D6DCE5";
                                                else
                                                    strBgColor="#DCE5F1"; 
                                                %>
											<tr bgcolor="<%=strBgColor%>"
												onmouseover="className='TableItemOn'"
												onmouseout="className='TableItemOff'">
												<td align="left" class="copy"><%=fundingSourceBean.get("description")%></td>
												<td align="left" class="copy"><%=fundingSourceBean.get("fundingSource")%></td>
												<td align="left" class="copy">
													<%if(fundingSourceBean.get("fundingSourceName") != null){%> <%=fundingSourceBean.get("fundingSourceName")%>
													<%}%>
												</td>
												<%   boolean fundingSrcflag = true;
                                                    if("N".equals(fundingSourceBean.get("fundingSourceTypeFlag"))){
                                                        fundingSrcflag = false;
                                                    }
                                                    if(!modeValue && fundingSrcflag){
                                                        String removeLink = "javascript:delete_data('D','" +fundingSourceBean.get("code") +"','" +fundingSourceBean.get("fundingSource") +"')";
                                                    
                                                    
                                                    %>


												<td nowrap class="copy">
													<%--a href="javascript:delete_data('D','<%=fundingSourceBean.get("code")%>','<%=fundingSourceBean.get("fundingSource")%>','<%=fundingSourceBean.get("fundingSourceTimeStamp")%>');">
                                                                <bean:message key="fundingSrc.Remove"/>
                                                            </a--%> <html:link
														href="<%=removeLink%>">
														<bean:message key="fundingSrc.Remove" />
													</html:link>
												</td>
												<%}// else {%>
												<!--   <td nowrap class="copy"> 
                                                    <bean:message key="fundingSrc.Remove"/>   
                                                        </td>-->
												<%// }  %>
											</tr>
											<% count++;%>
										</logic:iterate>
									</logic:present>
									<%//}%>
								</table>

							</td>
						</tr>
						<tr>
							<td>&nbsp;</td>
						</tr>
					</table></td>
			</tr>
			<!-- List of Funding Source -End -->
			<tr>
				<td height='10'>&nbsp;</td>
			</tr>
		</table>
		<!-- New Template for cwFundingSourceForm - End  -->

		<html:hidden property="acType" />
		<html:hidden property="protocolNumber" />
		<html:hidden property="sequenceNumber" />
		<html:hidden property="fundingSourceTimeStamp" />

	</html:form>
	<script>
          DATA_CHANGED = 'false';
          if(errValue) {
                DATA_CHANGED = 'true';
          }
          document.iacucFundingSourceForm.acType.value = 'I';
          LINK = "<%=request.getContextPath()%>/iacucfundingSource.do";
          FORM_LINK = document.iacucFundingSourceForm;
          PAGE_NAME = " <bean:message key="fundingSrc.FundingSources"/>";
          function dataChanged(){
            DATA_CHANGED = 'true';
          }
          linkForward(errValue);
        </script>
	<script>
      var help = '<bean:message key="helpTextProtocol.FundingSource"/>';
      help = trim(help);
      if(help.length == 0) {
        document.getElementById('helpText').style.display = 'none';
      }
      function trim(s) {
            return s.replace( /^\s*/, "" ).replace( /\s*$/, "" );
      }                  
        var typeCode = document.iacucFundingSourceForm.code.value;                   
        //Added/Commented for COEUSQA-3064 Unable to save funding source type in Lite if it is other than base types supplied-start
        //if(typeCode == '3'){            
        //    document.getElementById('hide_Search').style.display = 'none';            
        //}
        /*
        TypeCode 1 - Sponsor
        TypeCode 2 - Unit
        TypeCode 4 - Development Proposal
        TypeCode 5 - Institute Proposal
        TypeCode 6 - Award
        */
        if(typeCode == '1' || typeCode == '2' || typeCode == '4'
          || typeCode == '5'|| typeCode == '6'){            
            document.getElementById('hide_Search').style.display = 'block';            
        }else{            
            document.getElementById('hide_Search').style.display = 'none';            
        }      
        //Added/Commented for COEUSQA-3064 Unable to save funding source type in Lite if it is other than base types supplied-end 
      <%if(hasValidationMessage){%>
        showHide(0);
      <%}%>

        </script>
</body>
</html:html>
