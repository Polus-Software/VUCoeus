
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%@ page contentType="text/html;charset=UTF-8" language="java"%>

<%--start 1--%>
<%@page
	import="org.apache.struts.taglib.html.JavascriptValidatorTag,
                edu.mit.coeuslite.utils.CoeusLiteConstants, java.util.Vector,org.apache.struts.validator.DynaValidatorForm"%>
<%--end 1--%>

<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ include file="/coeuslite/mit/utils/CoeusContextPath.jsp"%>
<%
    String protocolNumber = "";
     String enableProtocolToDevPropLink = "";
     if(session.getAttribute("enableProtocolToDevPropLink") != null) {
         enableProtocolToDevPropLink = (String)session.getAttribute("enableProtocolToDevPropLink");
     }

     boolean hasCreateProposalRight = false;

     if(request.getAttribute("hasCreateProposalRight") != null) {
         hasCreateProposalRight = (Boolean)request.getAttribute("hasCreateProposalRight");
     }
%>

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
    function func_not(){
        return false;
    }
    
    function insert_data(data){
        document.fundingSourceFrm.acType.value = data;
        document.fundingSourceFrm.action = "<%=request.getContextPath()%>/fundingSource.do";
    }
    
    function findType(){
   
        if(document.fundingSourceFrm.type.selectedIndex==1){
            
        }
        else if(document.fundingSourceFrm.type.selectedIndex==2){
            
        }
        else if(document.fundingSourceFrm.type.selectedIndex==3){
            
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
                    sList = window.open('<%=request.getContextPath()%>/irbSearch.do?type=<bean:message key="searchWindow.title.sponsor"/>&search=true&searchName=WEBSPONSORSEARCH', "list", win);
                    break;
                case 2:
                    sList = window.open('<%=request.getContextPath()%>/irbSearch.do?type=<bean:message key="searchWindow.title.unit"/>&search=true&searchName=WEBLEADUNITSEARCH', "list", win);
                    break;
                case 3:
                    break;
                case 4:
                    sList = window.open('<%=request.getContextPath()%>/irbSearch.do?type=<bean:message key="searchWindow.title.devproposal"/>&search=true&searchName=ALL_PROPOSALDEVSEARCHNOROLES', "list", win);
                    break;
                case 5:
                    //Commented and Added for COEUSQA-2428_CLONE -LIte - Prop Dev - Application does not return any rows while searching for Original proposal_Start
                    //sList = window.open('<%=request.getContextPath()%>/irbSearch.do?type=<bean:message key="searchWindow.title.proposal"/>&search=true&searchName=PROPOSALSEARCH', "list", win);
                    sList = window.open('<%=request.getContextPath()%>/irbSearch.do?type=<bean:message key="searchWindow.title.proposal"/>&search=true&searchName=INSTITUTEPROPOSALSEARCH', "list", win);
                    ////Commented and Added for COEUSQA-2428_CLONE -LIte - Prop Dev - Application does not return any rows while searching for Original proposal_End
                    //Commented and Added for COEUSQA-2428_CLONE -LIte - Prop Dev - Application does not return any rows while searching for Original proposal_End
                    break;
                case 6:
                    sList = window.open('<%=request.getContextPath()%>/irbSearch.do?type=<bean:message key="searchWindow.title.award"/>&search=true&searchName=ALL_AWARD_SEARCH', "list", win);
                    break;
            }
            if (parseInt(navigator.appVersion) >= 4) {
            window.sList.focus(); 
            }
          }
          
     function fetch_Data(result){         
         switch(idx){
            case 1:
                document.fundingSourceFrm.fundingSource.value = result["SPONSOR_CODE"];
                document.fundingSourceFrm.fundingSourceName.value = result["SPONSOR_NAME"];
                break;
            case 2:
                document.fundingSourceFrm.fundingSource.value = result["UNIT_NUMBER"];
                document.fundingSourceFrm.fundingSourceName.value = result["UNIT_NAME"]; 
                break;
            case 4:
                document.fundingSourceFrm.fundingSource.value = result["PROPOSAL_NUMBER"];
                if(result["TITLE"] != 'null' && result["TITLE"] != undefined ){ 
                    document.fundingSourceFrm.fundingSourceName.value = result["TITLE"];
                }else{
                    document.fundingSourceFrm.fundingSourceName.value = "";
                }
                
                break;
            case 5:;
                document.fundingSourceFrm.fundingSource.value = result["PROPOSAL_NUMBER"];
                document.fundingSourceFrm.fundingSourceName.value = result["TITLE"];             
                break;
            case 6:
                document.fundingSourceFrm.fundingSource.value = result["MIT_AWARD_NUMBER"];
                document.fundingSourceFrm.fundingSourceName.value = result["TITLE"];             
                break;
         }
         dataChanged();
         
     }
     
    function search(){         
            if(document.fundingSourceFrm.code.value==0){
                alert("Please Select a Type");
            }            
            if(document.fundingSourceFrm.code.value==1){
                open_search(true,1);
            }
            if(document.fundingSourceFrm.code.value==2){
                open_search(false,2);
            }
            if(document.fundingSourceFrm.code.value==3){
                open_search(false,3);
            }
            if(document.fundingSourceFrm.code.value==4){
                open_search(false,4);
            }
            if(document.fundingSourceFrm.code.value==5){
                open_search(false,5);
            }
            if(document.fundingSourceFrm.code.value==6){
                open_search(false,6);
            }
    }
         
     function delete_data(data,typeCode,fundingSourceVal,timestamp){
            if (confirm("Are you sure you want to delete the funding source?")==true){
                document.fundingSourceFrm.acType.value = data;
            //   document.fundingSourceFrm.code.value = typeCode;
            //    document.fundingSourceFrm.fundingSource.value = fundingSourceVal;                
                document.fundingSourceFrm.fundingSourceTimeStamp.value=timestamp;
                document.fundingSourceFrm.action = "<%=request.getContextPath()%>/deleteFundingSource.do?fundingSourceNumber=" +fundingSourceVal;
                document.fundingSourceFrm.submit();
            }
        }
            
     var typeCode = '';
     function clearFields(sel){        
        document.fundingSourceFrm.fundingSource.value = "";
        document.fundingSourceFrm.fundingSourceName.value = "";
        typeCode = sel.options[sel.selectedIndex].value;

        if(typeCode == '4') {
            <%if(enableProtocolToDevPropLink.equals("1") && hasCreateProposalRight){%>

                document.getElementById("mybtn").style.visibility = "visible";
            <%}%>
        }
        else if(typeCode == '3'){
            document.getElementById('hide_Search').style.display = 'none';
            document.getElementById("mybtn").style.visibility = "hidden";
        typeCode = sel.options[sel.selectedIndex].value;
        } else{
            document.getElementById('hide_Search').style.display = 'block';
            document.getElementById("mybtn").style.visibility = "hidden";
        }
        
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

</script>

<script>
 function validateForm(form) {
     insert_data("I");
     //return validateFundingSourceFrm(form);
   }
</script>


</head>
<body>


	<%--start 2--%>

	<% String protocolNo = (String)session.getAttribute(CoeusLiteConstants.PROTOCOL_NUMBER+session.getId());
        
            if(protocolNo!= null){
                protocolNumber = protocolNo.toString();
                 protocolNo = "["+"Protocol No. - "+ protocolNo+"]";
            }else{
                protocolNo = "";
                }
        
 String mode=(String)session.getAttribute(CoeusLiteConstants.MODE_DETAILS+session.getId());
 Boolean fundingSourceMode = (Boolean) session.getAttribute("MODIFY_PROTOCOL_FUNDING_SOURCE"+session.getId());  
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
   Vector fundingSources = (Vector) session.getAttribute("fundingSources");
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


	<html:form action="/fundingSource" method="post"
		onsubmit="validateForm(this);">
		<a name="top"></a>
		<%--  ************  START OF BODY TABLE  ************************--%>

		<!-- New Template for cwFundingSourceForm - Start   -->
		<script type="text/javascript">
    document.getElementById('txt').innerHTML = '<bean:message key="helpPageTextProtocol.FundingSource"/>';
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
										key="helpPageTextProtocol.help" />
							</a></td>
						</tr>
					</table>
				</td>
			</tr>
			<tr>
				<td>
					<div id="helpText" class='helptext'>
						<bean:message key="helpTextProtocol.FundingSource" />
					</div>
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
							<html:errors header="" footer="" />
						</logic:messagesPresent> <logic:messagesPresent message="true">
							<script>errValue = true;</script>
							<html:messages id="message" message="true">
								<li><bean:write name="message" /></li>
							</html:messages>
						</logic:messagesPresent> <%
                            if(request.getAttribute("isValid") != null) {
                                String isValid = request.getAttribute("isValid").toString();
                                if(!isValid.equals("true")) {
                        %> Please fill the following details for the
						protocol...
						<li><%=isValid%>.</li> <%
                         }
                        }

                        if(request.getAttribute("streamProposal") != null) {
                            if(request.getAttribute("streamProposal").toString().equals("true")) {
                                %> New Proposal is created successfully
						with the current Protocol details... <%
                            }else {
                                 %> Proposal Creation failed because of
						some errors... <%
                                }
                        }
                   %>
				</font></td>
			</tr>
			<!-- Add Funding Source - Start  -->
			<tr>
				<td align="left" valign="top" class='core'><table width="100%"
						border="0" align="center" cellpadding="0" cellspacing="0"
						class="tabtable">
						<tr>
							<td colspan="4" align="left" valign="top"><table
									width="100%" height="20" border="0" cellpadding="0"
									cellspacing="0" class="tableheader">
									<tr>
										<td><bean:message key="fundingSrc.AddFundingSource" /></td>
									</tr>
								</table></td>
						</tr>

						<tr>
							<td width="70%" valign="top" class="copy" align="left">
								<table width="100%" border="0" cellpadding="0" cellspacing='5'>
									<tr>
										<td nowrap class="copybold" width="10%" align="left">
											<%-- Commented for case id#2627
                                                    <font color="red">*</font>
                                                    --%> <bean:message
												key="fundingSrc.Type" />:
										</td>
										<td class="copysmall" width="20%" align="left"><html:select
												property="code" styleClass="textbox-long"
												disabled="<%=modeValue%>" onchange="clearFields(this)"
												onkeyup="clearFields(this)">
												<html:option value="">
													<bean:message bundle="proposal"
														key="generalInfoProposal.pleaseSelect" />
												</html:option>
												<html:options collection="fundingTypes" property="code"
													labelProperty="description" />>
                                                    </html:select></td>
										<td><input type="button" id="mybtn"
											value="Start Proposal" class="clsavebutton"
											style="visibility: hidden;" onclick="createNewProposal();" />
										</td>
										<td width="10%" align="left">&nbsp;</td>
										<td width="60%" align="left">&nbsp;</td>
									</tr>

									<tr>
										<td nowrap class="copybold" width="10%" align="left">
											<%-- Commented for case id#2627
                                                    <font color="red">*</font>
                                                    --%> <bean:message
												key="fundingSrc.SpnsUnitNo" />:
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
										<td nowrap class="copybold" width="10%" align="left"><bean:message
												key="fundingSrc.Name" />:</td>
										<td colspan="3"><html:text property="fundingSourceName"
												styleClass="cltextbox-color" readonly="true" /></td>
									</tr>
								</table>
							</td>
						</tr>

					</table></td>
			</tr>
			<%if(!modeValue){%>
			<tr class='table'>
				<td colspan="3" nowrap class="savebutton" width="50%"><html:submit
						property="Save" value="Save" styleClass="clsavebutton" /></td>
			</tr>
			<%}%>
			<!-- Add Funding Source - End  -->


			<!-- List of Funding Source - Start -->
			<tr>
				<td align="left" valign="top" class='core'><table width="100%"
						border="0" align="center" cellpadding="0" cellspacing="0"
						class="tabtable">
						<tr>
							<td colspan="4" align="left" valign="top"><table
									width="100%" height="20" border="0" cellpadding="0"
									cellspacing="0" class="tableheader">
									<tr>
										<td><bean:message key="fundingSrc.ListofFunSrc" /></td>
									</tr>
								</table></td>
						</tr>
						<tr align="center">
							<td colspan="3"><br>

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
									<logic:present name="fundingSources">
										<logic:iterate id="fundingSourceBean" name="fundingSources"
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
												<%
                                                        DynaValidatorForm dynaForm = (DynaValidatorForm)fundingSources.get(count);
                                                        String proposalNum = dynaForm.getString("fundingSource");
                                                        if(proposalNum != null && proposalNum.length() >= 7 &&(fundingSourceBean.get("description").equals("Development Proposal"))){
                                                        
                                                    %>
												<td align="left" class="copy"><a
													href="<%=request.getContextPath()%>/getGeneralInfo.do?proposalNumber=<%=fundingSourceBean.get("fundingSource")%>&Menu_Id=003">
														<%=fundingSourceBean.get("fundingSource")%></a></td>
												<%} else {%>
												<td align="left" class="copy"><%=fundingSourceBean.get("fundingSource")%></td>
												<%}%>
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
								</table></td>
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
          document.fundingSourceFrm.acType.value = 'I';
          LINK = "<%=request.getContextPath()%>/fundingSource.do";
          FORM_LINK = document.fundingSourceFrm;
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
        typeCode = document.fundingSourceFrm.code.value;        
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

      function createNewProposal() {
       document.fundingSourceFrm.action = "<%=request.getContextPath()%>/newStubProposal.do?protocolNumber=<%=protocolNumber%>"
        document.fundingSourceFrm.submit();
    }
        //Added/Commented for COEUSQA-3064 Unable to save funding source type in Lite if it is other than base types supplied-end    
       if(typeCode == '4') {
            <%if(enableProtocolToDevPropLink.equals("1")){%>
                document.getElementById("mybtn").style.visibility = "visible";
            <%}%>
        }
        else if(typeCode == '3'){
            //document.getElementById('hide_Search').style.display = 'none';
            document.getElementById("mybtn").style.visibility = "hidden";
            } else{
            //document.getElementById('hide_Search').style.display = 'block';
            document.getElementById("mybtn").style.visibility = "hidden";
        }
  </script>
</body>
</html:html>
