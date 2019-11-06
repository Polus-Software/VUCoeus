<%--@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="edu.mit.coeuslite.utils.CoeusDynaBeansList ,
 edu.mit.coeuslite.utils.CoeusLiteConstants"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ include file= "/coeuslite/mit/utils/CoeusContextPath.jsp"  %>
<html:html>
<%  int count = 0;
    String EMPTY_STRING = "";
    String link = EMPTY_STRING;
    String clearLink =EMPTY_STRING;
    String addDelLink = EMPTY_STRING;
    CoeusDynaBeansList proposalOrganizationLocationList 
    = (CoeusDynaBeansList)session.getAttribute("proposalOrganizationLocationList");
           String mode=(String)session.getAttribute("mode"+session.getId()); 
           boolean modeValue=false;
           if(mode!=null && !mode.equals("")) {   
            if(mode.equalsIgnoreCase("display")){
                modeValue=true;
             }
            }
%>
    <head>
        <title>Proposal Organization
        </title>
        <style>
        .cltextbox-color{ font-weight: normal;
                          overflow: auto;
                        }
        </style>
        
        <script>
            var searchSelection = "";
            var orgIndicator = "";
            var roloAddress = "";
            var roloId = "";
            var index = "";
            var errValue = false;
            var errLock = false;
            function searchWindow(value,orgType,locIndex) {
            searchSelection = value;
            orgIndicator = orgType;
            index = locIndex;
            var winleft = (screen.width - 650) / 2;
            var winUp = (screen.height - 450) / 2;  
            var win = "scrollbars=1,resizable=1,width=750,height=450,left="+winleft+",top="+winUp;
            if (value == 'rolodex'){
            sList = window.open('<%=request.getContextPath()%>/generalProposalSearch.do?type=<bean:message key="searchWindow.title.person" bundle="proposal"/>&search=true&searchName=WEBROLODEXSEARCH', "list", win);  
            } 
            else if(value == 'organization') {
            sList = window.open('<%=request.getContextPath()%>/generalProposalSearch.do?type=<bean:message key="searchWindow.title.organization" bundle="proposal"/>&search=true&searchName=WEBORGANIZATIONSEARCH', "list", win);  
            }
            if (parseInt(navigator.appVersion) >= 4) {
            window.sList.focus(); 
            }
            } 
      
            function fetch_Data(result){
            dataChanged();
            if(searchSelection == 'rolodex'){
            if(result["ROLODEX_ID"] != 'null' && result["ROLODEX_ID"] != undefined ){
            roloId = result["ROLODEX_ID"];
            }
            if(result["ORGANIZATION"] != 'null' && result["ORGANIZATION"] != undefined ){
            roloAddress = result["ORGANIZATION"]+"\n"; 
            }if(result["ADDRESS_LINE_1"] != 'null' && result["ADDRESS_LINE_1"] != undefined ){ 
            roloAddress += result["ADDRESS_LINE_1"]+"\n";
            }if(result["ADDRESS_LINE_2"] != 'null' && result["ADDRESS_LINE_2"] != undefined ){ 
            roloAddress += result["ADDRESS_LINE_2"]+"\n";
            }if(result["ADDRESS_LINE_3"] != 'null' && result["ADDRESS_LINE_3"] != undefined ){ 
            roloAddress += result["ADDRESS_LINE_3"]+"\n";
            }if(result["CITY"] != 'null' && result["CITY"] != undefined ){ 
            roloAddress += result["CITY"]+"\n";
            }if(result["COUNTY"] != 'null' && result["COUNTY"] != undefined ){ 
            roloAddress += result["COUNTY"]+"\n";
            }if(result["STATE"] != 'null' && result["STATE"] != undefined ){ 
            roloAddress += result["STATE"]+"\n";
            }if(result["POSTAL_CODE"] != 'null' && result["POSTAL_CODE"] != undefined ){ 
            roloAddress += result["POSTAL_CODE"]+"\n";
            }if(result["COUNTRY_NAME"] != 'null' && result["COUNTRY_NAME"] != undefined ){ 
            roloAddress += result["COUNTRY_NAME"];
            }
            var desc = "dynaFormBean["+index+"].rolodexAddress";
            var modifiedValue = document.getElementsByName(desc);
            modifiedValue[0].value = roloAddress;
            var roloIdVal = document.getElementsByName("dynaFormBean["+index+"].rolodexId");
            roloIdVal[0].value = roloId;
            }else if(searchSelection == 'organization'){
            if(orgIndicator == 'O'){
                 
            var organization= result["ORGANIZATION_NAME"];
            var orgId = result["ORGANIZATION_ID"];
            var modifiedValue = document.getElementsByName('dynaFormData[0].organizationName');
            modifiedValue[0].value = organization;
            document.proposalOrganizationLocationList.action = "<%=request.getContextPath()%>/modifyOrganizationLocation.do?&orgId="+orgId; 
            document.proposalOrganizationLocationList.submit();
            }else if(orgIndicator == 'P'){
            var organization= result["ORGANIZATION_NAME"];
            var orgId = result["ORGANIZATION_ID"];
            var modifiedValue = document.getElementsByName('dynaFormData[0].performingOrganizationName');
            modifiedValue[0].value = organization;
            var modifiedVal = document.getElementsByName('dynaFormData[0].performingOrgId');
            modifiedVal[0].value = orgId;
            }
            }
            }  
      
            function clearAddress(count){
            dataChanged();
            var addr = "dynaFormBean["+count+"].rolodexAddress";
            addr = document.getElementsByName(addr);
            addr[0].value = "";
            addr = "dynaFormBean["+count+"].rolodexId";
            addr = document.getElementsByName(addr);
            //document.proposalOrganizationLocationList.action = "<%=request.getContextPath()%>/clearLocation.do?&clearCount="+count;
            //document.proposalOrganizationLocationList.submit();
            addr[0].value = "";
           }
           
            function updateData(){
            document.proposalOrganizationLocationList.action = "<%=request.getContextPath()%>/saveOrganizationLocation.do?";
            document.proposalOrganizationLocationList.submit();
            }
            
            function addDelete(data,count) {
            if(data == "D") {
            if(confirm(<bean:message bundle="proposal" key="proposalLocation.delLocation"/>)== true) {
            //var currentValue = document.getElementsByName('dynaFormBean['+count+'].acType');
            //currentValue[0].value = "D";
            document.proposalOrganizationLocationList.action = "<%=request.getContextPath()%>/deleteLocation.do?&rowCount="+count;
            document.proposalOrganizationLocationList.submit();
            }
            } else {
            //var currentValue = document.getElementsByName('dynaFormBean['+count+'].acType');
            //currentValue[0].value = "I";
            document.proposalOrganizationLocationList.action = "<%=request.getContextPath()%>/addLocation.do?&addDeleteLoc="+data;
            document.proposalOrganizationLocationList.submit(); 
            }
            }
            
        </script>
    </head>
    <%try{%>
    <body>
        <html:form  action="/getOrganization.do">
            <table  width='100%' border='0' cellpadding='0' cellspacing='4' class='table'>
            <tr>
                <td height="10" class='copy' colspan='3'>
                <logic:messagesPresent>
                    <font color='red'>
                        <script>errValue = true;</script>
                        <html:errors header="" footer=""/>
                    </font>
                </logic:messagesPresent>
                   <logic:messagesPresent message = "true"> 
                        <script>errValue = true;</script>
                     <font color="red">
                    <html:messages id="message" message="true" property="errMsg" bundle="proposal">  
                        <script>errLock = true;</script>
                        <li><bean:write name = "message"/></li>
                    </html:messages>
                    <html:messages id="message" message="true" bundle="proposal" property="proposalLocation.error.minLoc">                
                        <li><bean:write name = "message"/></li>
                     </html:messages>
                     <html:messages id="message" message="true" bundle="proposal" property="proposalLocation.error.sameLoc">                
                        <li><bean:write name = "message"/></li>
                     </html:messages>
                    </font>
                   </logic:messagesPresent> 
                </td>
            </tr>
            <tr>
            <td class="core">
                <table width='100%' align=center border='0' cellpadding='0' cellspacing='0' class='tabtable'>
                <tr>
                    <td colspan='3' class='tableheader'>
                        <bean:message bundle="proposal" key="proposalOrganization.Organization"/>                       
                    </td>
                </tr>
                 
                <tr>
                    <td colspan='3' class='helptext'>                        
                        <div id="helpText" >            
                            <bean:message bundle="proposal" key="helpTextProposal.Organization"/>  
                        </div>                        
                    </td>
                </tr>              
  
                
                <logic:iterate id="dynaFormData" name="proposalOrganizationLocationList" property="list" type="org.apache.struts.action.DynaActionForm" indexId="orgIndex">
                    <tr>
                    <td width='20%' class='copybold'>&nbsp;&nbsp;<bean:message bundle="proposal" key="proposalOrganization.Organization"/></td>
                    <td width='51%' class='copy'><html:text name="dynaFormData" property="organizationName"  readonly="true" indexed="true"  size="60"/></td>
                    <td width='29%'>
                    <%link = "javaScript:searchWindow('organization','O')";
                    if(!modeValue){%>
                                
                        <html:link href="<%=link%>">
                            <u><bean:message  bundle="proposal" key="proposalOrganization.Search"/></u>
                        </html:link>
                   <%}%> 
                    </td>
                    </tr>
                    <tr>
                    <td valign=top class='copybold'>&nbsp;&nbsp;<bean:message  bundle="proposal" key="proposalOrganization.Contact"/></td>
                    <td colspan='2' class='copy'><html:textarea name="dynaFormData" property="contactAddress" readonly="true" rows="5" styleClass="cltextbox-color" indexed="true"/></td>
                    </tr>
                    <tr>
                    <td class='copybold'>&nbsp;&nbsp;<bean:message bundle="proposal" key="proposalOrganization.PerformingOrganization"/></td>
                    <td class='copy' ><html:text name="dynaFormData"   property="performingOrganizationName" readonly="true" indexed="true"  size="60"/></td>
                    <td><%link = "javaScript:searchWindow('organization','P');";%>
                     <%if(!modeValue){%>
                    <html:link href="<%=link%>">
                        <u><bean:message  bundle="proposal" key="proposalOrganization.Search"/></u>
                    </html:link></td>
                     <%}%>       
                    <td>
                        <html:hidden name="dynaFormData" property="performingOrgId" indexed="true"/>
                        <html:hidden name="dynaFormData" property="updateUserOrg" indexed="true"/>
                        <html:hidden name="dynaFormData" property="updateTimeStampOrg" indexed="true"/>
                    </td>
                    </tr>
                </logic:iterate>
                <tr>
                    <td height='10' colspan='3'>
                    </td>
                </tr>
                </table>
            </td>
            </tr>
            <tr>
            <td >
                <br>
                <table width='100%' align=center border='0' cellpadding='0' cellspacing='0' class='tabtable'>
                <tr>
                    <td colspan='4' class='tableheader'><bean:message  bundle="proposal" key="proposalLocation.Location"/></td>
                </tr>
                <tr>
                    <td colspan='4' height='10'>
                    </td>
                </tr>
                <tr>
                    
                    <td colspan='4'>&nbsp;&nbsp;
                     <%addDelLink="javaScript:addDelete('A','"+count+"');";
                        if(!modeValue){%>
                        <html:link href="<%=addDelLink%>">
                            <u><bean:message bundle="proposal" key="proposalLocation.AddLocation"/></u>
                        </html:link>
                    <%}%>    
                    </td>
                </tr>
                <tr>
                    <td colspan='4' height='10'>
                    </td>
                </tr>
                <logic:iterate id="dynaFormBean" name="proposalOrganizationLocationList" property="beanList" type="org.apache.struts.action.DynaActionForm" indexId="locIndex">
                    <tr>
                    <td>
                        &nbsp;&nbsp;<html:text name="dynaFormBean" property="location" maxlength="60" size="60" readonly="<%=modeValue%>" indexed="true" onchange="dataChanged()"/>
                    </td>
                    <td colspan='3' >
                         <%addDelLink="javaScript:addDelete('D','"+count+"');";
                            if(!modeValue){%> 
                        <html:link href="<%=addDelLink%>">
                            <u><bean:message  bundle="proposal" key="proposalLocation.DeleteLocation"/></u>
                        </html:link>
                        &nbsp;&nbsp;|&nbsp;&nbsp;<%}%>
                        <%link = "javaScript:searchWindow('rolodex','R','"+locIndex+"');";
                if(!modeValue){%>
                        <html:link href="<%=link%>">
                            <u><bean:message bundle="proposal" key="proposalLocation.FindAddress"/></u>
                        </html:link>
                      &nbsp;&nbsp;|&nbsp;&nbsp;<%}%>
                      <%clearLink = "javaScript:clearAddress('"+count+"');";
                        if(!modeValue){%>
                        <html:link href="<%=clearLink%>">
                            <u><bean:message bundle="proposal" key="proposalLocation.ClearAddress"/></u>
                        </html:link>
                     <%}%>   
                    </td>
                        
                    <%--td width='12%' align=center>
                     <%link = "javaScript:searchWindow('rolodex','R','"+locIndex+"');";
                if(!modeValue){%>|
                        <html:link href="<%=link%>">
                            <u><bean:message  bundle="proposal" key="proposalLocation.FindAddress"/></u>
                        </html:link>
                      <%}%>
                    </td--%>
                        
                    <%--td width='12%' >
                     <%clearLink = "javaScript:clearAddress('"+count+"');";
                        if(!modeValue){%>|
                        <html:link href="<%=clearLink%>">
                            <u><bean:message bundle="proposal" key="proposalLocation.ClearAddress"/></u>
                        </html:link>
                     <%}%>   
                    </td--%>
                    <%--/tr>
                    <tr>
                    <td colspan='4'>
                        &nbsp;&nbsp;<html:textarea name="dynaFormBean" property="rolodexAddress" styleClass="cltextbox-color" indexed="true" readonly="true" rows="5"/>
                    <html:hidden name="dynaFormBean" property="rolodexId" indexed="true"/>
                        <html:hidden name="dynaFormBean" property="acType" indexed="true"/>
                        <html:hidden name="dynaFormBean" property="updateUserLoc" indexed="true"/>
                        <html:hidden name="dynaFormBean" property="updateTimeStampLoc" indexed="true"/>
                    </td>
                    </tr>
            <%count++;%>
                </logic:iterate>
                <tr>
                    <td  height='20'>
                    </td>
                </tr>
                </table>
            </td>
            </tr>
            <tr>
                <td class='savebutton' >
                 <%if(!modeValue){%>
                    <html:button property="save" value="Save" styleClass="clsavebutton"  onclick="updateData()"/>
                <%}%>
                </td>
            </tr>
            
            </table>
        </html:form>
    <script>
          DATA_CHANGED = 'false';
          var dataModified = '<%=request.getAttribute("dataModified")%>';
          if(dataModified == 'modified' || (errValue && !errLock)){
                DATA_CHANGED = 'true';
          }
          LINK = "<%=request.getContextPath()%>/saveOrganizationLocation.do?";
          FORM_LINK = document.proposalOrganizationLocationList;
          PAGE_NAME = "<bean:message bundle="proposal" key="proposalOrganization.Organization"/>";
          function dataChanged(){
            DATA_CHANGED = 'true';
          }
          linkForward(errValue);
    </script>
    <script>
          var help = '<bean:message bundle="proposal" key="helpTextProposal.Organization"/>';
          help = trim(help);
          if(help.length == 0) {
            document.getElementById('helpText').style.display = 'none';
          }
          function trim(s) {
                return s.replace( /^\s*/, "" ).replace( /\s*$/, "" );
          }  
    </script>      
    </body>
    <%}catch(Exception e){e.printStackTrace();}%>
</html:html--%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN"
	"http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<%@ include file= "/coeuslite/mit/utils/CoeusContextPath.jsp"%>

<%@ page import="edu.mit.coeuslite.utils.CoeusLiteConstants,
                edu.mit.coeuslite.utils.CoeusDynaBeansList,java.util.Vector,
                edu.mit.coeuslite.utils.ComboBoxBean"%>

<%@ taglib uri="/WEB-INF/struts-bean.tld"  prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld"  prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/coeus-utils.tld"  prefix="coeusUtils"%>
<jsp:useBean id="propLocationTypes" scope="session" class="java.util.Vector" />
<html:html locale="true">
    <head>
        <%
        String mode=(String)session.getAttribute("mode"+session.getId()); 
           boolean modeValue=false;
           if(mode!=null && !mode.equals("")) {   
            if(mode.equalsIgnoreCase("display")){
                modeValue=true;
             }
            }
        %>
        <style>
            .cltextbox-color{ font-weight: normal; width: 285px ;}
            .cltextbox_medium{width: 300px;}
            .cltextbox{
/* JM 5-31-2011 updated background-color per 4.4.2 */            
                    background-color: #e9e9e9;
                    border: 0px;
                    font-family: Arial, Helvetica, sans-serif;
                    font-size: 12px;
                    font-weight: normal;
                    width: 300px;
                    color: #333333;
            }
            
        </style>        
        <title>Organization</title>
        <script language="JavaScript">
            var errValue = false;
            var errLock = false;   
            var searchSelection = "";
            var roloAddress = "";
            var index = "";
            
            function updateData(){
                document.proposalOrganizationLocationList.action = "<%=request.getContextPath()%>/saveOrganizationLocation.do?";
                document.proposalOrganizationLocationList.submit();
            }
            
            function addOrgLoc(){
                document.proposalOrganizationLocationList.action = "<%=request.getContextPath()%>/addOrgLocation.do"; 
                document.proposalOrganizationLocationList.submit();
            }
            
            function addCongDist(count){
                document.proposalOrganizationLocationList.action = "<%=request.getContextPath()%>/addOrgLocCongDistrict.do?propCongDist="+count; 
                document.proposalOrganizationLocationList.submit();
            }
            
            function deleteOrgLoc(count) {
                if(confirm("<bean:message bundle="proposal" key="proposalLocation.delOrganizationLoc"/>")== true) {                                
                    document.proposalOrganizationLocationList.action = "<%=request.getContextPath()%>/deleteOrgLocation.do?&rowCount="+count;
                    document.proposalOrganizationLocationList.submit();
                }
            }
            
            function delCongDistrict(count,congDist){
                if(confirm("<bean:message bundle="proposal" key="proposalLocation.delCongDist"/>")== true) {  
                    document.proposalOrganizationLocationList.action = "<%=request.getContextPath()%>/deleteCongDist.do?&orgCount="+count+"&orgCongDist="+congDist; 
                    document.proposalOrganizationLocationList.submit();
                }
            }
            
            function clearAddress(count){
                dataChanged();
                var addr = "dynaFormData["+count+"].contactAddress";
                addr = document.getElementsByName(addr);
                addr[0].value = "";
                addr = "dynaFormData["+count+"].rolodexId";
                addr = document.getElementsByName(addr);
                addr[0].value = "";
           }
           
           function searchWindow(value,orgType,locIndex) {
                searchSelection = value;
                orgIndicator = orgType;
                index = locIndex;
                var winleft = (screen.width - 650) / 2;
                var winUp = (screen.height - 450) / 2;  
                var win = "scrollbars=1,resizable=1,width=750,height=450,left="+winleft+",top="+winUp;
                if (value == 'rolodex'){
                    sList = window.open('<%=request.getContextPath()%>/generalProposalSearch.do?type=<bean:message key="searchWindow.title.person" bundle="proposal"/>&search=true&searchName=WEBROLODEXSEARCH', "list", win);  
                } 
                else if(value == 'organization') {
                    sList = window.open('<%=request.getContextPath()%>/generalProposalSearch.do?type=<bean:message key="searchWindow.title.organization" bundle="proposal"/>&search=true&searchName=WEBORGANIZATIONSEARCH', "list", win);  
                }
                if (parseInt(navigator.appVersion) >= 4) {
                    window.sList.focus(); 
                }
            } 
            
            function fetch_Data(result){
            dataChanged();
            if(searchSelection == 'rolodex'){
            if(result["ROLODEX_ID"] != 'null' && result["ROLODEX_ID"] != undefined ){
            roloId = result["ROLODEX_ID"];
            }
            if(result["ORGANIZATION"] != 'null' && result["ORGANIZATION"] != undefined ){
            roloAddress = result["ORGANIZATION"]+"\n"; 
            }if(result["ADDRESS_LINE_1"] != 'null' && result["ADDRESS_LINE_1"] != undefined ){ 
            roloAddress += result["ADDRESS_LINE_1"]+"\n";
            }if(result["ADDRESS_LINE_2"] != 'null' && result["ADDRESS_LINE_2"] != undefined ){ 
            roloAddress += result["ADDRESS_LINE_2"]+"\n";
            }if(result["ADDRESS_LINE_3"] != 'null' && result["ADDRESS_LINE_3"] != undefined ){ 
            roloAddress += result["ADDRESS_LINE_3"]+"\n";
            }if(result["CITY"] != 'null' && result["CITY"] != undefined ){ 
            roloAddress += result["CITY"]+"\n";
            }if(result["COUNTY"] != 'null' && result["COUNTY"] != undefined ){ 
            roloAddress += result["COUNTY"]+"\n";
            }if(result["STATE"] != 'null' && result["STATE"] != undefined ){ 
            roloAddress += result["STATE"]+"\n";
            }if(result["POSTAL_CODE"] != 'null' && result["POSTAL_CODE"] != undefined ){ 
            roloAddress += result["POSTAL_CODE"]+"\n";
            }if(result["COUNTRY_NAME"] != 'null' && result["COUNTRY_NAME"] != undefined ){ 
            roloAddress += result["COUNTRY_NAME"]+"\n";
            }
            var desc = "dynaFormData["+index+"].contactAddress";
            var modifiedValue = document.getElementsByName(desc); 
            modifiedValue[0].value = roloAddress;
            var roloIdVal = document.getElementsByName("dynaFormData["+index+"].rolodexId");
            roloIdVal[0].value = roloId;
            }else if(searchSelection == 'organization'){
            
            if(orgIndicator == 'O'){
                var organization= result["ORGANIZATION_NAME"];                
                var orgId = result["ORGANIZATION_ID"];
                var modValue = document.getElementsByName('dynaFormData['+index+'].locationName');
                modValue[0].value = organization;                
                var roloValue = document.getElementsByName('dynaFormData['+index+'].organizationId');
                roloValue[0].value = orgId;
                
                document.proposalOrganizationLocationList.action = "<%=request.getContextPath()%>/modifyOrganization.do?otherOrgCount="+index+"&orgId="+orgId; 
                document.proposalOrganizationLocationList.submit();
                }
            }
            }  
            
            function changeType(count){
                document.proposalOrganizationLocationList.action = "<%=request.getContextPath()%>/changeOrgLocation.do?orgLocChange="+count; 
                document.proposalOrganizationLocationList.submit();
            }
        </script>
        <%int count = 0;%>
        
        <html:base/> 
    </head>
    <body>
        <html:form action="/getOrganization.do">   
            <table width="100%" border="0" cellpadding="0" cellspacing="0" class="table">
                <tr class='tableheader'>
                    <td>
                        <bean:message bundle="proposal" key="proposalOrganization.Organization"/>:
                    </td>
                </tr>
                <tr>
                    <td>&nbsp;
                    </td>
                </tr>
                <tr>
                    <td>
                        <logic:messagesPresent>
                            <font color='red'>
                                <script>errValue = true;</script>
                                <html:errors header="" footer=""/>
                            </font>
                        </logic:messagesPresent>
                       <logic:messagesPresent message = "true"> 
                            <script>errValue = true;</script>
                         <font color="red">
                        <html:messages id="message" message="true" property="errMsg" bundle="proposal">  
                            <script>errLock = true;</script>
                            <li><bean:write name = "message"/></li>
                        </html:messages>
                        <html:messages id="message" message="true" bundle="proposal" property="proposalLocation.error.CongDist">                
                            <li><bean:write name = "message"/></li>
                         </html:messages>
                         <html:messages id="message" message="true" bundle="proposal" property="proposalLocation.error.OrgLoc">                
                            <li><bean:write name = "message"/></li>
                         </html:messages>
                         <html:messages id="message" message="true" bundle="proposal" property="proposalLocation.error.emptyOrgLocType">                
                            <li><bean:write name = "message"/></li>
                         </html:messages>
                         <html:messages id="message" message="true" bundle="proposal" property="proposalLocation.error.sameCongDist">                
                            <li><bean:write name = "message"/></li>
                         </html:messages>
                         <html:messages id="message" message="true" bundle="proposal" property="proposalLocation.error.sameOrgLoc">                
                            <li><bean:write name = "message"/></li>
                         </html:messages>
                        </font>
                       </logic:messagesPresent> 
                    </td>
                </tr>
                
                <tr>
                    <td align="left" height="100%">
<!-- JM 5-31-2011 updated per 4.4.2 -->  
                        <table border="0" cellpadding="0" cellspacing="0" bgcolor="#e9e9e9" >
                            <logic:iterate id="dynaFormData" name="proposalOrganizationLocationList" property="list" scope="session" type="org.apache.struts.action.DynaActionForm" indexId="index">
                                <%
                                String locType = "";
                                if(dynaFormData.get("locationTypeCode") != null){
                                    locType = dynaFormData.get("locationTypeCode").toString();
                                }
                                %>
                                <tr class='tableheader'>
                                    <td height="10" colspan="2">
                                        <%if(!locType.equals("") && (locType.equals("1"))){%>
                                        <bean:message bundle="proposal" key="proposalOrganization.ProposalOrganization"/>:
                                        <%}else if(!locType.equals("") && (locType.equals("2"))){%>
                                        <bean:message bundle="proposal" key="proposalOrganization.PerformingOrganization"/>:
                                        <%}else if(!locType.equals("") && ((!locType.equals("1") || !locType.equals("2")) && count==2)) {%>
                                        <bean:message bundle="proposal" key="proposalLocation.OrganizationLocation"/>:
                                        <%}%>
                                    </td>
                                </tr>
                                <tr>
                                <td height="100%" width="40%" valign="top">
                                    <!-- Left section -->
<!-- JM 5-31-2011 updated per 4.4.2 -->  
                                    <table width="40%"  align="left" border="0" cellpadding="0" cellspacing="0" bgcolor="#e9e9e9" > 
                                            <%if(!locType.equals("") && (locType.equals("1") || locType.equals("2"))){%>
                                            <tr>
                                                <td class="copyBold" colspan="3" nowrap> 
                                                    &nbsp;<html:text property="locationTypeDesc" name="dynaFormData" readonly="true" styleClass="cltextbox" style="font-weight:bold;" indexed="true" maxlength="35"/>
                                                </td> 
                                           </tr> 
                                          <%}else{%>
                                         <tr> 
                                            <td class="copyBold" height="15" width="10%" align="left"  nowrap>
                                                    &nbsp;<bean:message bundle="proposal" key="proposalLocation.Type"/>: &nbsp;&nbsp;
                                            </td>
                                            <td width="15%" height="15" align="left">
                                              <%
                                                if(count > 1){
                                                    Vector vecLoctypes = (Vector)session.getAttribute("propLocationTypes");
                                                    if(vecLoctypes != null && vecLoctypes.size() >0){
                                                        for(int cnt = 0; cnt < vecLoctypes.size() ; cnt++){
                                                                ComboBoxBean comboBean = (ComboBoxBean)vecLoctypes.get(cnt);
                                                                if(comboBean.getCode().equals("1") || comboBean.getCode().equals("2")){
                                                                    vecLoctypes.remove(cnt--);
                                                                }
                                                        }
                                                    }
                                                }
                                              %>                                              
                                              <%String changeType = "javascript:changeType('"+count+"')";%>
                                              <html:select property="locationTypeCode" styleClass="cltextbox_small" name="dynaFormData" disabled="<%=modeValue%>"  onchange="<%=changeType%>" indexed="true">
                                                  <html:option value=""><bean:message bundle="proposal" key="generalInfoProposal.pleaseSelect"/></html:option>
                                                  <html:options collection="propLocationTypes" property="code" labelProperty="description"/>
                                              </html:select>
                                            </td>
                                            <td width="15%" height="15" align="left">
                                              <%if(!modeValue){%>
                                              <%String delLink="javaScript:deleteOrgLoc('"+count+"');";%>
                                              <html:link href="<%=delLink%>"><bean:message bundle="proposal" key="proposalLocation.DeleteOrgLoc"/></html:link>
                                              <%}%>
                                            </td>
                                           </tr> 
                                          <%}%>
                                         
                                        <tr>
                                            <td class="copybold" width="10%" valign="top" nowrap>&nbsp;<bean:message bundle="proposal" key="proposalLocation.Address"/>: </td>
                                            <td class="copy" colspan="2">
                                                <html:textarea name="dynaFormData" property="contactAddress" readonly="true" rows="4" cols="20"  styleClass="cltextbox-color" indexed="true"/>
                                            </td>                         
                                            <script>
                                                if(navigator.appName == "Microsoft Internet Explorer")
                                                {
                                                    var addr = "dynaFormData[<%=count%>].contactAddress";
                                                    addr = document.getElementsByName(addr);
                                                    addr[0].cols=20;
                                                    addr[0].rows=5;
                                                }
                                           </script>  
                                        </tr>                                        
                                        <%--tr>
                                            <td width="10%"></td>
                                            <td width="30%" valign="bottom">                                                
                                                <%if(!modeValue){%>
                                                <% if((!locType.equals("1") && !locType.equals("2") && !locType.equals("3"))){%>
                                                <%String link = "javaScript:searchWindow('rolodex','R','"+count+"');";%>
                                                <%String clearLink = "javaScript:clearAddress('"+count+"');";%>
                                                <html:link href="<%=link%>">
                                                    <bean:message bundle="proposal" key="proposalLocation.FindAddress"/>
                                                </html:link> 
                                                &nbsp;&nbsp; | &nbsp;&nbsp;
                                                <html:link href="<%=clearLink%>">
                                                    <bean:message bundle="proposal" key="proposalLocation.ClearAddress"/>
                                                </html:link>
                                                <%}%>
                                                <%}%>                                                
                                            </td>
                                            <td width="10%"></td>
                                        </tr--%>                                        
                                    </table>
                                    <!--Left Section end -->
                                </td>
                                <td align="left" valign="top" width="40%" height="100%">
                                    <!--Right Section Start -->
<!-- JM 5-31-2011 updated per 4.4.2 -->  
                                    <table width="40%" align="left"  border="0" cellpadding="0" cellspacing="0" bgcolor="#e9e9e9">                                        
                                        <tr>
                                            <td width="10%" class="copyBold" nowrap><bean:message bundle="proposal" key="proposalLocation.Location"/>: &nbsp;&nbsp;</td>
                                             <td width="30%" class="copy" nowrap>
                                                <%if(!locType.equals("") && (locType.equals("1") || locType.equals("2") || locType.equals("3"))){%> 
                                                    <html:text property="locationName" name="dynaFormData" styleClass="cltextbox" size="40" readonly="true"  indexed="true" maxlength="60"/>                                             
                                                <%}else{%>                                                    
                                                    <html:text property="locationName" name="dynaFormData" size="40" styleClass="cltextbox_medium" disabled="<%=modeValue%>"  onchange="dataChanged()" indexed="true" maxlength="60"/>                                                                                                      
                                                <%}%>
                                              </td>
                                                <%if(!modeValue){%>    
                                                <%if(!locType.equals("") && (locType.equals("2") || locType.equals("3"))){%>
                                                <%String searchlink = "javaScript:searchWindow('organization','O','"+count+"')";%>
                                                <td width="10%" align="left">
                                                <html:link href="<%=searchlink%>"><bean:message bundle="proposal" key="proposalOrganization.Search"/></html:link>
                                                </td>
                                                <%}else{%>
                                                    <td width="10%" align="left">&nbsp;</td>
                                                <%}%>
                                                 <%}%>
                                        </tr>                                        
                                        <tr>
                                            <td colspan="3" height="3" align="left">
                                            </td>
                                        </tr>
                                        <tr>
                                                <td width="10%" align="left" valign="top" class="copybold"><bean:message bundle="proposal" key="proposalLocation.CongDist"/>:</td>
                                                <td colspan="2" align="left" valign="top">
<!-- JM 5-31-2011 updated per 4.4.2 -->  
                                                    <table align="left"  border="0" cellpadding="0" cellspacing="0" bgcolor="#e9e9e9">
                                                        <logic:iterate id="dynaCongDistrict" name="dynaFormData" property="cvCongDist" type="org.apache.struts.action.DynaActionForm" indexId="cdIndex">
                                                            <tr>
                                                                <td  align="left" valign="top" nowrap>                                                     
                                                                     <%String name = "congDist"+index+cdIndex;%>
                                                                     <bean:define id="cDistrict" name="dynaCongDistrict" property="congDistrict"/>
                                                                     <%if(!modeValue){%>
                                                                     <input type="text" name="<%=name%>"  class="cltextbox_small" onchange="dataChanged()" maxlength="50" size="35" value="<%=cDistrict%>">                                                     
                                                                     <%}else{%>
                                                                     <input type="text" name="<%=name%>"  class="cltextbox_small" disabled="true" onchange="dataChanged()" maxlength="50" size="35" value="<%=cDistrict%>">                                                     
                                                                     <%}%>
                                                                </td>
                                                                <td class="copy" align="left" valign="top">
                                                                    <%if(!modeValue){%>
                                                                    <%String delCongDistr= "javascript:delCongDistrict('"+count+"','"+cdIndex+"')";%>
                                                                    <html:link href="<%=delCongDistr%>"><bean:message bundle="proposal" key="proposalLocation.DeleteCongDist"/></html:link>
                                                                    <%}%>
                                                                </td>
                                                            </tr>
                                                        </logic:iterate>    
                                                        <%if(dynaFormData.get("cvCongDist") == null || ((java.util.Vector)dynaFormData.get("cvCongDist")).size()<4){
                                                                 int lsCount = (dynaFormData.get("cvCongDist") == null) ? 0 : ((java.util.Vector)dynaFormData.get("cvCongDist")).size();
                                                                 lsCount = 4 - lsCount;
                                                                 for(int row = 0; row < lsCount; row++){
                                                                     String invi = "invi"+row;%>
                                                                 <tr>
                                                                     <td  align="left" valign="top" colspan="2">                                                     
<!-- JM 5-31-2011 updated per 4.4.2 -->  
                                                                       <input type="text" name="<%=invi%>" readonly="true"  class="cltextbox_small" style="background-color: #e9e9e9; border: 0px;" maxlength="50" size="35">                                                     
                                                                     </td>   
                                                                 </tr>
                                                        <%       }
                                                           }%>
                                                    </table>
                                                </td>
                                            </tr>  
                                            
                                            <%--tr> 
                                               <%String addCongrDistrict = "javaScript:addCongDist('"+count+"')";%> 
                                               <td class="copy" align="left" colspan="3" valign="bottom" height="100%">
                                                   <%if(!modeValue){%>
                                                    &nbsp;&nbsp;<html:link href="<%=addCongrDistrict%>"><bean:message bundle="proposal" key="proposalLocation.AddCongDist"/></html:link>
                                                    <%}%>
                                               </td>
                                           </tr--%>
                                    </table>
                                </td>
                            </tr>
                            <tr> 
                               <td colspan="2">
<!-- JM 5-31-2011 updated per 4.4.2 -->  
                                   <table align="left" width="100%" border="0" bgcolor="#e9e9e9" cellpadding="0" cellspacing="0">
                                       <tr>
                                            <td width="50%" align="left" valign="bottom">                                                
                                                <%if(!modeValue){%>
                                                <% if((!locType.equals("1") && !locType.equals("2") && !locType.equals("3"))){%>
                                                <%String link = "javaScript:searchWindow('rolodex','R','"+count+"');";%>
                                                <%String clearLink = "javaScript:clearAddress('"+count+"');";%>
                                                &nbsp;&nbsp;<html:link href="<%=link%>">
                                                    <bean:message bundle="proposal" key="proposalLocation.FindAddress"/>
                                                </html:link> 
                                                &nbsp;&nbsp; | &nbsp;&nbsp;
                                                <html:link href="<%=clearLink%>">
                                                    <bean:message bundle="proposal" key="proposalLocation.ClearAddress"/>
                                                </html:link>
                                                <%}%>
                                                <%}%>                                                
                                            </td>
                                            <%String addCongrDistrict = "javaScript:addCongDist('"+count+"')";%> 
                                           <td width="50%" class="copy" align="left" valign="bottom" height="100%">
                                               <%if(!modeValue){%>
                                                &nbsp;&nbsp;<html:link href="<%=addCongrDistrict%>"><bean:message bundle="proposal" key="proposalLocation.AddCongDist"/></html:link>
                                                <%}%>
                                           </td>
                                       </tr>
                                   </table>
                               </td>                                               
                           </tr>
                            <% if (count == 1){%>
                            <tr>
                                <td colspan="3">  
                                    <table align="left" class="table" width="100%" border="0" cellpadding="0" cellspacing="0" >
                                        <tr>
                                            <td>
                                                <%if(!modeValue){%>
                                                 &nbsp;&nbsp;<html:link href="javascript:addOrgLoc();"><u><bean:message bundle="proposal" key="proposalLocation.AddOrgLoc"/></u></html:link>&nbsp;&nbsp;
                                                 <%}%>
                                            </td>
                                        </tr>
                                    </table>
                                </td>
                            </tr> 
                            <%}else{%>
                            <tr><td colspan="3"></td></tr>   
                            <%}%>
                            <%count++;%>
                            <html:hidden name="dynaFormData" property="acType" indexed="true"/>
                            <html:hidden name="dynaFormData" property="organizationId" indexed="true"/>
                            <html:hidden name="dynaFormData" property="rolodexId" indexed="true"/>
                          </logic:iterate>
                        </table>
                    </td>
                </tr>
                 <tr>
                    <td>
                        <%if(!modeValue){%>
                            <html:button property="save" value="Save" styleClass="clsavebutton" onclick="updateData();" />                       
                         <%}%>
                    </td>
                </tr>
            </table>
        </html:form>
        <script>
          DATA_CHANGED = 'false';
          var dataModified = '<%=request.getAttribute("dataModified")%>';
          if(dataModified == 'modified' || (errValue && !errLock)){
                DATA_CHANGED = 'true';
          }
          LINK = "<%=request.getContextPath()%>/saveOrganizationLocation.do?";
          FORM_LINK = document.proposalOrganizationLocationList;
          PAGE_NAME = "<bean:message bundle="proposal" key="proposalOrganization.Organization"/>";
          function dataChanged(){
            DATA_CHANGED = 'true';
          }
          linkForward(errValue);
    </script>
    </body>
</html:html>