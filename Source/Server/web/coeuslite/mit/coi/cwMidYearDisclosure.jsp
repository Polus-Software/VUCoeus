<%@ page
	import="org.apache.struts.validator.DynaValidatorForm,
java.util.Vector,edu.mit.coeuslite.utils.ComboBoxBean,org.apache.struts.taglib.html.JavascriptValidatorTag"%>

<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ include file="/coeuslite/mit/utils/CoeusContextPath.jsp"%>

<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<jsp:useBean id="getRowData" scope="session" class="java.util.Vector" />
<jsp:useBean id="person" scope="session"
	class="edu.mit.coeus.bean.PersonInfoBean" />
<html>
<head>
<title>JSP Page</title>
<link
	href="<bean:write name='ctxtPath'/>/coeuslite/mit/utils/css/coeus_styles.css"
	rel="stylesheet" type="text/css">
<script language="JavaScript">

  var validFlag; 
    
     function insert_data(data)
     {
        document.midYearDisclosure.acType.value = data;
        document.midYearDisclosure.action = "<%=request.getContextPath()%>/midYearDisclosure.do";
     }

     function open_proposal_search(link)
     {
        validFlag = "proposal";    
        var winleft = (screen.width - 650) / 2;
        var winUp = (screen.height - 450) / 2;  
        var win = "scrollbars=1,resizable=1,width=820,height=450,left="+winleft+",top="+winUp
        sList = window.open('<bean:write name='ctxtPath'/>'+"/"+link, "list", win);
     }
     
     function open_award_search(link)
     {
        validFlag = "award";    
        var winleft = (screen.width - 650) / 2;
        var winUp = (screen.height - 450) / 2;  
        var win = "scrollbars=1,resizable=1,width=820,height=450,left="+winleft+",top="+winUp
        sList = window.open('<bean:write name='ctxtPath'/>'+"/"+link, "list", win);
        if (parseInt(navigator.appVersion) >= 4) {
            window.sList.focus(); 
            }
               
     }
          
         
      function fetch_Data(result)
      {       
         if(validFlag=="proposal")
         {   
             document.midYearDisclosure.proposalNumber.value = result["PROPOSAL_NUMBER"] ;
         }
         if(validFlag=="award")
         {
            document.midYearDisclosure.sponsorAwdNumber.value = result["MIT_AWARD_NUMBER"];
         }
      }
      
   /*   function validate_search()
      {
           var proposalVal;
           var awardVal;
           var propRadioVal;
           var awdRadioVal;
           
           
            proposalVal =  document.midYearDisclosure.proposalNumber.value ;
            awardVal = document.midYearDisclosure.sponsorAwdNumber.value;
            
            propRadioVal = document.midYearDisclosure.disclosureTypeCode[0].checked;
            awdRadioVal = document.midYearDisclosure.disclosureTypeCode[1].checked;
            
            
            if(propRadioVal==true && proposalVal=='' )
            {
                alert("Proposal number is required. Use the Search button to find a Proposal number.");
                return false;
            }
            
            if(awdRadioVal==true && awardVal=='')
            {
                alert("Award number is required. Use the Search button to find an Award number.");
                return false;
            }
            
            return true;
      }  */
      
</script>



</head>
<body>

	<script>
function validateForm(form) {
    return validatemidYearDisclosure(form);
}

function refresh(){
    document.midYearDisclosure.personName.value="";
}
</script>
	<%--   New DESIGN   TEMPLATE    --%>

	<html:form action="/createMidYearDisclosure.do" method="post"
		onsubmit="return validateForm(this)">

		<table width="980" height="100%" border="0" cellpadding="0"
			cellspacing="0" class="table">
			<tr>
				<td height="15" align="left" valign="top" class="tableheader">
					<bean:message bundle="coi" key="coiDisclosure.headerAddNewDisc" />
					<%=person.getFullName() %>
				</td>
			</tr>
			<!--  Select Proposal or Award - Start  -->
			<tr>
				<td align="center" valign="top"><br>
					<table width="99%" border="0" align="center" cellpadding="0"
						cellspacing="0" class="tabtable">
						<tr>
							<td colspan="4" align="left" valign="top"><table
									width="100%" height="20" border="0" cellpadding="0"
									cellspacing="0" class="tableheader">
									<tr>
										<td><bean:message bundle="coi"
												key="coiDisclosure.selectProposalAward" /></td>
									</tr>
								</table></td>
						</tr>
						<tr>
							<td width="70%" valign="top" class="copy" align="left">
								<table width="100%" border="0" cellpadding="3" cellspacing='0'>
									<tr>
										<td nowrap class="copy" width="10%" align="left"><b>
												<bean:message bundle="coi"
													key="coiDisclosure.midyearDisclData" />
										</b></td>
									</tr>
									<tr align="left">
										<td class='copy'><font color="red"> <html:messages
													id="message" message="true" bundle="coi">
                                           &nbsp; * <bean:write
														name="message" />
												</html:messages>
										</font></td>
									</tr>
									<td width="70%" valign="top" class="copy" align="left">
										<table width="100%" border="0" cellpadding="2" cellspacing='0'>
											<tr>
												<td nowrap class="copybold" width="20%" align="left"><html:radio
														property="disclosureTypeCode" value="2" /> <bean:message
														bundle="coi" key="label.submitProposal" /> &nbsp;&nbsp;</td>
												<td width="6">
													<div align="left">
														<html:text property="proposalNumber" styleClass="textbox"
															value="" />
													</div>
												</td>
												<td class="copy" nowrap colspan="2"><a
													href="javaScript:open_proposal_search('/coiSearch.do?type=<bean:message key="searchWindow.title.proposal" bundle="coi"/>&search=true&searchName=ALL_PROPOSAL_SEARCH');"
													class="copysmall"> <u><bean:message bundle="coi"
																key="label.search" /></u></a>&nbsp;&nbsp; <bean:message
														bundle="coi" key="label.searchProposal" /></td>
											</tr>
											<tr>
												<td nowrap class="copybold" width="20%" align="left"><html:radio
														property="disclosureTypeCode" value="1" /> <bean:message
														bundle="coi" key="label.award" /> &nbsp;&nbsp</td>
												<td width="6">
													<div align="left">
														<html:text property="sponsorAwdNumber"
															styleClass="textbox" value="" />
													</div>
												</td>
												<td class="copy" nowrap colspan="2"><a
													href="javaScript:open_award_search('/coiSearch.do?type=<bean:message key="searchWindow.title.award" bundle="coi"/>&search=true&searchName=ALL_AWARD_SEARCH');"
													class="copysmall"><u><bean:message bundle="coi"
																key="label.search" /></u></a>&nbsp;&nbsp; <bean:message
														bundle="coi" key="label.searchAward" /></td>
											</tr>
											<tr>
												<td colspan='4' class='savebutton'><br> <html:submit
														property="save" value="Save" /></td>
											</tr>
											<tr>
												<td></td>
											</tr>
										</table>
									</td>

								</table>
							</td>
						</tr>

					</table></td>
			</tr>
			<tr>
				<td height='10'>&nbsp;</td>
			</tr>
		</table>

	</html:form>


</body>
</html>
