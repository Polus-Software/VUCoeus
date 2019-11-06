<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ include file="/coeuslite/mit/utils/CoeusContextPath.jsp"%>
<%@ page
	import="java.util.List,edu.mit.coeuslite.utils.CoeusDynaBeansList,java.util.Vector,
                edu.mit.coeuslite.utils.CoeusLiteConstants"%>

<% CoeusDynaBeansList coeusDynaBeansList = (CoeusDynaBeansList)session.getAttribute("protocolCustomData"); %>

<html:html>

<%
    String selectedTab =(String) session.getAttribute("selectedTab");
    CoeusDynaBeansList list = (CoeusDynaBeansList) session.getAttribute("protocolCustomDataList");
    List lstGroupName = (Vector) list.getBeanList();    
    String mode=(String)session.getAttribute("mode"+session.getId());
    boolean modeValue=false;
    if(mode!=null && !mode.equals("")){   
        if(mode.equalsIgnoreCase("D")){
            modeValue=true;
        }
    }
    //Added for coeus4.3 Amendments and Renewal enhancement
    String strProtocolNum = (String)session.getAttribute(CoeusLiteConstants.PROTOCOL_NUMBER+session.getId());
    if(strProtocolNum == null)
        strProtocolNum = "";    
    String amendRenewPageMode = (String)session.getAttribute("amendRenewPageMode"+request.getSession().getId());
    if(strProtocolNum.length() > 10 && (amendRenewPageMode == null || 
            amendRenewPageMode.equalsIgnoreCase(CoeusLiteConstants.DISPLAY_MODE))){
        modeValue = true;
    }    
 %>

<head>
<title>Protocol Custom Data</title>
<style>
.cltextbox-color {
	width: 360px;
}
</style>
</head>
<script language="javascript" type="text/JavaScript"
	src="<bean:write name='ctxtPath'/>/coeuslite/mit/utils/scripts/coeusLiteCalendar.js">
</script>
<script>
    
    var index = "" ;
    var errValue = false;
    var errLock = false;
    function openLookupWindow(lookupWin, lookupVal, lookupArgument, count) {
        index = count;
        var linkValue = 'generalProposalSearch.do';
        var winleft = (screen.width - 830) / 2;
        var winUp = (screen.height - 450) / 2;  
        var win = "scrollbars=1,resizable=1,width=830,height=450,left="+winleft+",top="+winUp
        if((lookupWin == "w_arg_value_list") || (lookupWin == "w_arg_code_tbl") || (lookupWin == "w_select_cost_element")){
            linkValue = 'getArgumentData.do';
            var win = "scrollbars=1,resizable=1,width=580,height=300,left="+winleft+",top="+winUp
        }
        link = linkValue+'?type='+lookupVal+'&search=true&searchName='+lookupWin+'&argument='+lookupArgument;
        sList = window.open('<bean:write name='ctxtPath'/>'+"/"+link, "list", win);
        if (parseInt(navigator.appVersion) >= 4) {
            window.sList.focus(); 
        }
    }    
    
    function fetch_Data(result,searchType){
        dataChanged();
        if(searchType == "Unit Search"){         
            var currentValue = document.getElementsByName('dynaFormData['+index+'].columnValue');        
            currentValue[0].value = result["UNIT_NUMBER"];
            var currentDesc = document.getElementsByName('dynaFormData['+index+'].description');         
            currentDesc[0].value = result["UNIT_NAME"];
        }else if(searchType == "Person Search"){
            var currentValue = document.getElementsByName('dynaFormData['+index+'].columnValue');        
            currentValue[0].value = result["PERSON_ID"];
            var currentDesc = document.getElementsByName('dynaFormData['+index+'].description');         
            currentDesc[0].value = result["FULL_NAME"];
        }else if(searchType == "Rolodex"){          
            var currentValue = document.getElementsByName('dynaFormData['+index+'].columnValue');        
            currentValue[0].value = result["ROLODEX_ID"];
            var currentDesc = document.getElementsByName('dynaFormData['+index+'].description'); 
            currentDesc[0].value = result["LAST_NAME"];
            if(result["LAST_NAME"] == "null" || result["LAST_NAME"] == undefined ){
                currentDesc[0].value = "";  
            }
        // 4580: Add organization and sponsor search in custom elements - Start    
        } else if(searchType == "Organization Search"){
            var currentValue = document.getElementsByName('dynaFormData['+index+'].columnValue');        
            currentValue[0].value = result["ID"];
            var currentDesc = document.getElementsByName('dynaFormData['+index+'].description');         
            currentDesc[0].value = result["NAME"];
        } else if(searchType == "Sponsor Search"){
            var currentValue = document.getElementsByName('dynaFormData['+index+'].columnValue');        
            currentValue[0].value = result["ID"];
            var currentDesc = document.getElementsByName('dynaFormData['+index+'].description');         
            currentDesc[0].value = result["NAME"];
        }
        // 4580: Add organization and sponsor search in custom elements - End
    }    
    
    
    // Added for Case#3023 - Coeus Lite - Others Tab -Start
    function put_Data(listCode,listDesc) {
            dataChanged();
            var currentValue = document.getElementsByName('dynaFormData['+index+'].columnValue');
            currentValue[0].value = listCode;
            var currentDesc = document.getElementsByName('dynaFormData['+index+'].description'); 
            currentDesc[0].value = listDesc;
    }
    // Added for Case#3023 - Coeus Lite - Others Tab -End
    
    function tabSelected(linkSelected, status){
        if(status == 'G'){            
            CLICKED_LINK = "<%=request.getContextPath()%>/getGroups.do?selectedTab="+linkSelected+"&oldSelectedTab=<%=selectedTab%>";            
            if(validate()){
                document.protocolCustomDataList.action = "<%=request.getContextPath()%>/getGroups.do?selectedTab="+linkSelected+"&oldSelectedTab=<%=selectedTab%>";
                document.protocolCustomDataList.submit();            
            }
        }
    } 
    
    //function saveCustomData(){
    //    document.protocolCustomDataList.action = "<%=request.getContextPath()%>/saveCustomData.do";
    //    document.protocolCustomDataList.submit();    
    //}
    
    function valueChanged(clicked){    
        if(clicked == true || clicked == 'true'){            
            dataChanged();
        }
    }
    
</script>
<body>
	<!-- Added for Page Help - Start-->
	<script type="text/javascript">
document.getElementById('txt').innerHTML = '<bean:message key="helpPageTextProtocol.Others"/>';
</script>
	<!-- Added for Page Help - End -->
	<html:form action="/saveCustomData.do">

		<table width="100%" height="100%" border="0" cellpadding="0"
			cellspacing="0" class='table'>
			<tr>
				<td>
					<!-- Added for Page Help - Start -->
					<table width="100%" border="0" cellpadding="0" cellspacing="0">
						<tr>
							<td height="20" align="left" valign="top" class="theader"><bean:message
									key="protocolCustomData.label.header" /></td>
							<td height="20" align="right" valign="top" class="tableheader">
								<a id="helpPageText"
								href="javascript:showBalloon('helpPageText', 'helpText')"> <u><bean:message
											key="helpPageTextProtocol.help" /></u>
							</a>
							</td>&nbsp;&nbsp;
						</tr>
					</table> <!-- Added for Page Help - End -->
				</td>
			</tr>

			<%if(lstGroupName!=null && lstGroupName.size()>0){%>
			<tr>
				<td class='copybold'><font color="red">*</font>
				<bean:message key="label.indicatesReqFields" />
				<td>
			</tr>
			<%}%>

			<tr class="copy">
				<td><logic:messagesPresent message="true">
						<script>errValue = true;</script>
						<font color='red'> <html:messages id="message"
								message="true" property="isRequired">
								<li><bean:write name="message" /></li>
							</html:messages> <html:messages id="message" message="true"
								property="numberFormatException">
								<li><bean:write name="message" /></li>
							</html:messages> <html:messages id="message" message="true"
								property="notValidDate">
								<li><bean:write name="message" /></li>
							</html:messages> <html:messages id="message" message="true"
								property="notValidLength">
								<li><bean:write name="message" /></li>
							</html:messages> <html:messages id="message" message="true"
								property="noCustElements">
								<script>errValue = false;</script>
								<li><bean:write name="message" /></li>
							</html:messages> <html:messages id="message" message="true" property="errMsg">
								<script>errLock = true;</script>
								<li><bean:write name="message" /></li>
							</html:messages>
						</font>
					</logic:messagesPresent></td>
			</tr>

			<%if(lstGroupName!=null && lstGroupName.size()>0){%>
			<tr>
				<td>
					<table width="100%" height="100%" align=center border="0"
						cellpadding="0" cellspacing="0" class='tabtable'>
						<tr>
							<td class='copybold'><logic:iterate id="dynaFormData"
									name="protocolCustomDataList" property="beanList"
									type="org.apache.struts.action.DynaActionForm" indexId="index"
									scope="session">
									<%if(selectedTab != null && dynaFormData.get("groupCode") != null && 
                     !dynaFormData.get("groupCode").equals("") && selectedTab.equals(dynaFormData.get("groupCode"))){%>
									<b><font color="#6D0202"><%=dynaFormData.get("groupCode")%></font></b>
									<%} else if(dynaFormData.get("groupCode") != null && !dynaFormData.get("groupCode").equals("")){
                    String link = "javaScript:tabSelected('"+dynaFormData.get("groupCode")+"','G')";%>
									<b><html:link href="<%=link%>">
											<u><%=dynaFormData.get("groupCode")%></u>
										</html:link></b>
									<%}%>&nbsp;&nbsp;&nbsp;
                </logic:iterate></td>
						</tr>
					</table>
				</td>
			</tr>



			<tr>
				<td>
					<table width="100%" border="0" cellspacing="0" cellpadding="1"
						class="tabtable">
						<%  int count = 0;
              java.util.Vector vecProtocolCustomData = (java.util.Vector)coeusDynaBeansList.getList(); 
              if(vecProtocolCustomData != null && vecProtocolCustomData.size() > 0){%>
						<logic:iterate id="dynaFormData" name="protocolCustomData"
							property="list" type="org.apache.struts.action.DynaActionForm"
							indexId="index">
							<%
                            String hasLookup = (String)dynaFormData.get("hasLookUp");
                            String lookUpWindow = (String)dynaFormData.get("lookUpWindow");
                            String dataLength = ((String)dynaFormData.get("dataLength")).trim();
                            String lookUpValue = (String)dynaFormData.get("lookUpValue");
                            String dataType = (String)dynaFormData.get("dataType");
                            String argumentName = (String)dynaFormData.get("lookUpArgument");
                            String isRequired =(String)dynaFormData.get("isRequired");
                            String clicked = "javascript:valueChanged('true')";
                            boolean isPresent = false ;
                            boolean isDisabled = false ;
                            if(hasLookup!=null && !hasLookup.equals("")){  
                              if(hasLookup.equalsIgnoreCase("Y")){
                                  if(lookUpWindow!=null && !lookUpWindow.equals("")){
                                      isPresent = true;
                                      isDisabled = true;
                                      }
                                  }
                              }
                        %>
							<tr>
								<td></td>
								<td class="copybold">
									<% if(isRequired != null && !isRequired.equals("")){
                            if(isRequired.equalsIgnoreCase("Y")){%> <font
									color='red'>*</font> <%}}%> <bean:write name="dynaFormData"
										property="columnLabel" />
								</td>
								<td>
									<% if(dataType != null && !dataType.equals("")){
                            if(dataType.equals("NUMBER")){%> <html:text
										property="columnValue" name="dynaFormData" maxlength="10"
										indexed="true" readonly="<%=isDisabled%>"
										disabled="<%=modeValue%>" onchange="<%=clicked%>"
										styleClass="textbox" /> <% }else{%> <html:text
										property="columnValue" name="dynaFormData" maxlength="2000"
										indexed="true" readonly="<%=isDisabled%>"
										disabled="<%=modeValue%>" onchange="<%=clicked%>"
										styleClass="textbox" /> <%}}%>
								</td>
								<td class="copybold">
									<% 
                        if(!modeValue){
                         if(isPresent){
                         String image= request.getContextPath()+"/coeusliteimages/search.gif";
                         String pageUrl="javaScript:openLookupWindow('"+lookUpWindow+"','"+lookUpValue+"','"+argumentName+"','"+count+"')";%>
									<html:link href="<%=pageUrl%>">
										<u><bean:message key="label.search" /></u>
									</html:link> <%}}%>
								</td>
								<td><html:text property="description" name="dynaFormData"
										maxlength="2000" styleClass="cltextbox-color" indexed="true"
										readonly="true" /></td>
							</tr>
							<% count++; %>
						</logic:iterate>
						<%}%>
					</table>
				</td>
			</tr>
			<%if(!modeValue){ %>
			<tr>
				<td class='savebutton'>
					<%  if(vecProtocolCustomData != null && vecProtocolCustomData.size() >0){ %>
					<html:submit property="Submit" styleClass="clsavebutton"
						value="Save" /> <%}%>
				</td>
			</tr>
			<%}%>
			<%}%>
		</table>
	</html:form>
	<script>
      DATA_CHANGED = 'false';
      if(errValue && !errLock) {
            DATA_CHANGED = 'true';
      }
      LINK = "<%=request.getContextPath()%>/saveCustomData.do";
      FORM_LINK = document.protocolCustomDataList;
      PAGE_NAME = "Custom Data";
      function dataChanged(){
            DATA_CHANGED = 'true';
      }
      linkForward(errValue);
</script>
	<script>
      var help = '<bean:message key="helpTextProtocol.Others"/>';
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
