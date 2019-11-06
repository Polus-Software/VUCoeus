<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ include file="/coeuslite/mit/utils/CoeusContextPath.jsp"%>
<%@ page import="edu.mit.coeuslite.utils.CoeusLiteConstants"%>
<jsp:useBean id="correspondentsTypeData" scope="session"
	class="java.util.Vector" />
<html:html>
<%int count=0;
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
<head>
<style>
.clcombobox-bigger {
	width: 160px;
}

.textbox-long {
	width: 200px;
}

.textbox-longer {
	width: 300px;
}
</style>
<title>Correspondents</title>
<script language="JavaScript">
    var row="";
    var index="";
    var searchSelection = "";
    var errValue = false;
    var errLock = false;
    function addDelete(data,value) {
        if(data == "D") {
            if(confirm("<bean:message key="correspondents.deleteConfirmation"/>")) {
                var currentValue = document.getElementsByName('dynaFormData['+value+'].awAcType');
                currentValue[0].value = "D";
                document.correspondentsList.action = "<%=request.getContextPath()%>/addDeleteCorrespondents.do?operation="+data;
                document.correspondentsList.submit();
            }
        } else {
            document.correspondentsList.action = "<%=request.getContextPath()%>/saveCorrespondents.do?operation="+data;
            document.correspondentsList.submit(); 
        }
    }
    
    function save() {
        document.correspondentsList.action = "<%=request.getContextPath()%>/saveCorrespondents.do?operation=S";
        document.correspondentsList.submit();       
    }
    
    
    function open_search(value) {
        searchSelection = value;
        var winleft = (screen.width - 650) / 2;
        var winUp = (screen.height - 450) / 2;  
        var win = "scrollbars=1,resizable=1,width=750,height=450,left="+winleft+",top="+winUp;
        if(value == 'person'){
            sList = window.open('<%=request.getContextPath()%>/irbSearch.do?type=<bean:message key="searchWindow.title.person"/>&search=true&searchName=WEBPERSONSEARCH', "list", win);  
        }else if (value == 'rolodex'){
            sList = window.open('<%=request.getContextPath()%>/irbSearch.do?type=<bean:message key="searchWindow.title.rolodex"/>&search=true&searchName=WEBROLODEXSEARCH', "list", win);  
        }
        if (parseInt(navigator.appVersion) >= 4) {
            window.sList.focus(); 
        }
      }
      
      function fetch_Data(result){
        var name="";
        var flag="";
        var personId="";
        if(searchSelection == 'person'){
            flag = "N";
            if(result["FULL_NAME"]!="null" && result["FULL_NAME"]!= undefined){
                name=result["NAME"];
            }
            if(result["PERSON_ID"]!=null && result["PERSON_ID"]!= undefined){
                personId = result["PERSON_ID"];
            }            
        } else if (searchSelection == 'rolodex') {
            flag = "Y";        
            if(result["LAST_NAME"]!="null" && result["LAST_NAME"]!= undefined){
                name=result["LAST_NAME"]+", ";
            }        
            if(result["FIRST_NAME"]!="null" && result["FIRST_NAME"]!= undefined){
                name+=result["FIRST_NAME"];
            }
            if(result["ROLODEX_ID"]!=null && result["ROLODEX_ID"]!= undefined){
                personId = result["ROLODEX_ID"];
            }
            if(name=='null' || name== undefined || name.length==0){
                name = result["ORGANIZATION"];
            }
            dataChanged();
        }
        var modifiedValue = "dynaFormData["+row+"].personName";
        modifiedValue = document.getElementsByName(modifiedValue);
        modifiedValue[0].value = name;
        modifiedValue = document.getElementsByName("dynaFormData["+row+"].nonEmployeeFlag");
        modifiedValue[0].value = flag;
        modifiedValue = "dynaFormData["+row+"].personId";
        modifiedValue = document.getElementsByName(modifiedValue);
        modifiedValue[0].value = personId;
      }
      
    function openDesc(value) {
        index = value;
        var currentValue = document.getElementsByName('dynaFormData['+index+'].comments');        
        var txtValue = currentValue[0].value;
        openWindow(index);        
    }
    
    function openWindow(txtValue){
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

        var loc = '<bean:write name='ctxtPath'/>/coeuslite/mit/utils/dialogs/cwViewText.jsp?value='+txtValue;
        var newWin = window.open(loc,"formName",'dependent=1,toolbar=0,location=0,directories=0,status=0, menubar=0,scrollbars=0,resizable=0,width=' + w + ',height=' + h+ ',left='+ leftPos + ',top=' + topPos);
        newWin.window.focus();
         
         }    
    
    function insertData(value) {
        var currentValue = document.getElementsByName('dynaFormData['+index+'].comments');
        if(currentValue[0].value != value){
            dataChanged();
            currentValue[0].value = value;
        }
    }
   
</script>
<html:base />
</head>
<body>
	<html:form action="/saveCorrespondents.do">
		<!-- Added for Page Help - Start-->
		<script type="text/javascript">
document.getElementById('txt').innerHTML = '<bean:message key="helpPageTextProtocol.Correspondents"/>';
</script>
		<!-- Added for Page Help - End -->
		<table width='100%' cellpadding='0' cellspacing='0' class='table'>
			<tr>
				<td>
					<!-- Added for Page Help - Start-->
					<table width="100%" border="0" cellpadding="0" cellspacing="0">
						<tr>
							<td height="20" align="left" valign="top" class='theader'><bean:message
									key="correspondents.correspondents" /></td>
							<td height="20" align="right" valign="top" class='tableheader'>
								<a id="helpPageText"
								href="javascript:showBalloon('helpPageText', 'helpText')"> <u><bean:message
											key="helpPageTextProtocol.help" /></u>
							</a>
							</td>&nbsp;&nbsp;
						</tr>
					</table> <!-- Added for Page Help - End -->
				</td>
			</tr>
			<tr>
				<td>
					<div id="helpText" class='helptext'>
						<bean:message key="helpTextProtocol.Correspondents" />
					</div>
				</td>
			</tr>
			<tr class='theader' style='font-weight: normal;'>
				<td class='copy'><font color="red"> <logic:messagesPresent>
							<script>errValue = true; </script>
							<html:errors header="" footer="" />
						</logic:messagesPresent> <logic:messagesPresent message="true">
							<script>errValue = true; </script>
							<html:messages id="message" message="true"
								property="duplicateRecordsNotAllowed">
								<li><bean:write name="message" /></li>
							</html:messages>
							<html:messages id="message" message="true" property="errMsg"
								bundle="proposal">
								<script>errLock = true;</script>
								<li><bean:write name="message" /></li>
							</html:messages>

						</logic:messagesPresent>
				</font></td>
			</tr>


			<tr>
				<td class='core'>
					<table width='100%' cellpadding='0' cellspacing='0'
						class='tabtable'>
						<tr>
							<td class='tableheader'><bean:message
									key="correspondents.addCorrespondents" /></td>
						</tr>
						<%if(!modeValue){%>
						<tr>
							<td>
								<div id="enable" style='display: none;'>
									<table width='100%' cellpadding='0' cellspacing='0'>
										<tr class='copybold'>
											<td width='33%' align=center>
												<%String link="javaScript:addDelete('A','"+count+"');";%> <html:link
													href="<%=link%>">
													<u><bean:message key="correspondents.add" /></u>
												</html:link>
											</td>
											<td width='33%' align=center><html:link
													href="javascript:open_search('person')">
													<u><bean:message key="correspondents.findPerson" /></u>
												</html:link></td>
											<td width='34%' align=center><html:link
													href="javascript:open_search('rolodex')">
													<u><bean:message key="correspondents.findRolodex" /></u>
												</html:link></td>
										</tr>
									</table>
								</div>
								<div id="disable" style='display: none;'>
									<table width='100%' cellpadding='0' cellspacing='0'>
										<tr class='copybold'>
											<td width='33%' align=center>
												<%link="javaScript:addDelete('A','"+count+"');";%> <html:link
													href="<%=link%>">
													<u><bean:message key="correspondents.add" /></u>
												</html:link>
											</td>
											<td width='33%' align=center>&nbsp;</td>
											<td width='34%' align=center>&nbsp;</td>
										</tr>
									</table>
								</div>
							</td>
						</tr>
						<%}%>
						<tr>
							<td height='20'></td>
						</tr>
						<tr>
							<td>
								<table width='99%' cellpadding='0' cellspacing='1' align=center
									class='tabtable'>
									<tr class='theader'>
										<td width='20%'><bean:message key="correspondents.type" />
										</td>
										<td width='27%'><bean:message key="correspondents.name" />
										</td>
										<td width='46%'><bean:message
												key="correspondents.comments" /></td>
										<td width='7%'></td>
									</tr>
									<logic:iterate id="dynaFormData" name="correspondentsList"
										property="list" type="org.apache.struts.action.DynaActionForm"
										indexId="index" scope="session">
										<tr class='copy'>
											<td width='20%' align=center><html:select
													name="dynaFormData" property="correspondentType"
													styleClass="clcombobox-bigger" onchange="dataChanged()"
													indexed="true" disabled="<%=modeValue%>">
													<html:option value="">
														<bean:message key="correspondents.pleaseSelect" />
													</html:option>
													<html:options collection="correspondentsTypeData"
														property="code" labelProperty="description" />
												</html:select></td>
											<td width='27%'><html:text name="dynaFormData"
													property="personName" styleClass="textbox-long"
													indexed="true" readonly="true" /></td>
											<td width='46%'><html:hidden name="dynaFormData"
													property="acType" indexed="true" /> <html:hidden
													name="dynaFormData" property="awUpdateTimestamp"
													indexed="true" /> <html:hidden name="dynaFormData"
													property="nonEmployeeFlag" indexed="true" /> <html:text
													name="dynaFormData" property="comments"
													styleClass="textbox-longer" onchange="dataChanged()"
													indexed="true" maxlength="100" readonly="<%=modeValue%>" />
												<%String link="javaScript:openDesc('"+count+"');";%> &nbsp; <html:link
													href="<%=link%>">

													<%if(!modeValue){%>
													<bean:message key="correspondents.edit" />
													<%} else {%>
													<bean:message key="correspondents.view" />
													<%}%>

												</html:link> <html:hidden name="dynaFormData"
													property="awCorrespondentType" indexed="true" /></td>
											<td width='7%' align=center><html:hidden
													name="dynaFormData" property="awUpdateUser" indexed="true" />
												<html:hidden name="dynaFormData" property="awAcType"
													indexed="true" /> <%if(!modeValue){%> <%link="javaScript:addDelete('D','"+count+"');";%>
												<html:link href="<%=link%>">
													<bean:message key="correspondents.remove" />
												</html:link> <%}%> <html:hidden name="dynaFormData" property="personId"
													indexed="true" /> <html:hidden name="dynaFormData"
													property="awPersonId" indexed="true" /></td>
										</tr>
										<%count++;%>
									</logic:iterate>
								</table>
							</td>
						</tr>
						<tr>
							<td height='20'></td>
						</tr>
						<%if(!modeValue && count>0){%>
						<tr class='table'>
							<td class='savebutton'><html:button property="ok"
									value="Save" styleClass="clsavebutton" onclick="save();" /></td>
						</tr>
						<%}%>
					</table>
				</td>
			</tr>

		</table>
	</html:form>
	<script>
        row = '<%=count-1%>';
        if(row>=0) {
            document.getElementById('enable').style.display = 'block';
        } else {
            document.getElementById('disable').style.display = 'block';
        }
</script>
	<script>
          DATA_CHANGED = 'false';
          var dataModified = '<%=request.getAttribute("dataModified")%>';
          if(errValue && !errLock){
                DATA_CHANGED = 'true';
          }
          LINK = "<%=request.getContextPath()%>/saveCorrespondents.do?operation=S";
          FORM_LINK = document.correspondentsList;
          PAGE_NAME = "<bean:message key="correspondents.correspondents"/>";
          function dataChanged(){
            DATA_CHANGED = 'true';
          }
          linkForward(errValue);
    </script>
	<script>
      var help = '<bean:message key="helpTextProtocol.Correspondents"/>';
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