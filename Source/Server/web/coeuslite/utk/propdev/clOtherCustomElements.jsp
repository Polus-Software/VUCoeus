<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">


<%@ include file= "/coeuslite/mit/utils/CoeusContextPath.jsp"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld"  prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld"  prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/coeus-utils.tld"  prefix="coeusUtils"%>
<%@page import="edu.mit.coeuslite.utils.CoeusDynaBeansList"%>
<% CoeusDynaBeansList coeusDynaBeansList = (CoeusDynaBeansList)session.getAttribute("otherCsutomData"); %>

<html:html>
<head>
<title>Coeus Lite</title>

<html:base/> 
</head>

<script>
var index = "" ;
var errValue = false;
var errLock = false;
function openLookupWindow(lookupWin,lookupVal, lookupArgument, count) {
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

function put_Data(listCode,listDesc) {
    dataChanged();
    var currentValue = document.getElementsByName('dynaFormData['+index+'].columnValue');        
    currentValue[0].value = listCode;
    var currentDesc = document.getElementsByName('dynaFormData['+index+'].description');         
    currentDesc[0].value = listDesc;

}
</script>
<body>
<html:form action="/saveOthersData" method="POST">
 <% String mode=(String)session.getAttribute("mode"+session.getId()); 
           boolean modeValue=false;
           if(mode!=null && !mode.equals("")) {   
            if(mode.equalsIgnoreCase("display")){
                modeValue=true;
             }
            }
    %>
<table width="100%" height="100%"  border="0" cellpadding="0" cellspacing="0" class="table">
    <tr>
        <td>
            &nbsp;
        </td>
    </tr>
    <tr>
        <td height="10%" align="left" valign="top" class='core'>
            <table width="100%"  border="0" align="center" cellpadding="1" cellspacing="0" class="tabtable">                                                             
              <tr class='tableheader'>
                <td colspan="5" height='20' align="left" valign="top">
                    <bean:message bundle="proposal" key="customElements.header"/>                         
                </td>
             </tr>
             <tr class='theader'>
                <td colspan='4' class='copybold'>
                <font color="red">*</font> <bean:message bundle="proposal" key="generalInfoProposal.mandatory"/>
                <td>
            </tr>
            <tr> 
              <td>
                <div id="helpText" class='helptext'>            
                    <bean:message bundle="proposal" key="helpTextProposal.Others"/>  
                </div>                          
              </td>
            </tr>  
           
             <tr class="copy">
                <td>
                   <logic:messagesPresent message="true"> 
                        <script>errValue = true;</script>
                        <font color='red'>
                        <html:messages id="message" message="true" property="isRequired" bundle="proposal">                
                            <li><bean:write name = "message"/></li>
                        </html:messages>
                        <html:messages id="message" message="true" property="numberFormatException" bundle="proposal">                
                            <li><bean:write name = "message"/></li>
                        </html:messages>
                        <html:messages id="message" message="true" property="notValidDate" bundle="proposal">                
                            <li><bean:write name = "message"/></li>
                        </html:messages>
                        <html:messages id="message" message="true" property="customElements.notValidLength" bundle="proposal">                
                            <li><bean:write name = "message"/></li>
                        </html:messages>
                        <!-- Showing the Locking messages - start -->
                        <!-- If lock is deleted then show this message -->                         
                        <html:messages id="message" message="true" property="errMsg" bundle="proposal">
                            <script>errLock = true;</script>
                            <li><bean:write name = "message"/></li>
                        </html:messages>
                        </font>
                        <!-- Showing the Locking messages - End -->                      
                  </logic:messagesPresent>   
                </td>
             </tr>
             <tr>
                <td height='10'>
                </td>
             </tr>
             <tr>
                <td>
                    <table width="100%"  border="0" cellspacing="0" cellpadding="0">   
                      <%  int count = 0;
                          java.util.Vector vecOtherData = (java.util.Vector)coeusDynaBeansList.getList(); 
                          if(vecOtherData!=null && vecOtherData.size() >0){%>
                         <logic:iterate id="dynaFormData" name="otherCsutomData" property="list" type="org.apache.struts.action.DynaActionForm" indexId="index">
                                    <%
                                        String hasLookup = (String)dynaFormData.get("hasLookUp");
                                        String lookUpWindow = (String)dynaFormData.get("lookUpWindow");
                                        String dataLength = ((String)dynaFormData.get("dataLength")).trim();
                                        String lookUpValue = (String)dynaFormData.get("lookUpValue");
                                        String dataType = (String)dynaFormData.get("dataType");
                                        String argumentName = (String)dynaFormData.get("lookUpArgument");
                                        String isRequired =(String)dynaFormData.get("isRequired");
                                        
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
                              <tr class="copybold">
                                <td width='1%'></td>
                                <td width = "15%" nowrap>
                                    <% if(isRequired != null && !isRequired.equals("")){
                                        if(isRequired.equalsIgnoreCase("Y")){%>
                                                    <font color='red'>*</font>
                                            <% }}%>
                                    <bean:write name="dynaFormData" property="columnLabel"/>
                                </td>
                                <td width="10%">
                                    <% if(dataType!=null && !dataType.equals("")){
                                        if(dataType.equals("NUMBER")){%>
                                          <html:text property="columnValue" name="dynaFormData" maxlength="10" indexed="true" readonly ="<%=isDisabled%>" disabled="<%=modeValue%>" onchange="dataChanged()"/>   
                                    <% }else{%>
                                          <html:text property="columnValue" name="dynaFormData" maxlength="2000" indexed="true" readonly ="<%=isDisabled%>" disabled="<%=modeValue%>" onchange="dataChanged()"/>   
                                     <%   }}
                                    %>
                                    
                                </td>
                                <td nowrap>&nbsp;                                   
                                    <% 
                                    if(!modeValue){
                                     if(isPresent){
                                     String image= request.getContextPath()+"/coeusliteimages/search.gif";
                                     String pageUrl="javaScript:openLookupWindow('"+lookUpWindow+"','"+lookUpValue+"','"+argumentName+"','"+count+"')";%>
                                    <html:link  href="<%=pageUrl%>"><u><bean:message bundle="proposal" key="proposalOrganization.Search"/></u></html:link>                                    
                                    <%} }%>
                                </td>
                                <td><html:text property="description"  name="dynaFormData" maxlength="2000" styleClass="cltextbox-color" indexed="true" readonly="true"/></td>
                                
                                <tr><td></td></tr>
                              </tr>
                            <% count++ ; %>
                       </logic:iterate>
                        <%}else{%>  
                        <tr class='copy' align="center">
                              <td><font color='red'><bean:message bundle="proposal" key="customElements.noData"/></font>
                              </td>
                            </tr>
                        <%}%>
                    </table>
                </td>
             </tr>
             <tr>
                <td height='15'>
                </td>
             </tr>
            </table>
        </td>
    </tr>
     <%if(!modeValue){ %>
    <tr> 
        <td class='savebutton'>
            <% if(vecOtherData!=null && vecOtherData.size() >0){%>
            <html:submit property="Submit" styleClass="clsavebutton" value="Save" />
            <%}%>
        </td>
    </tr>
    <%}%>
</table>
</html:form>
<script>
      DATA_CHANGED = 'false';
      if(errValue && !errLock) {
            DATA_CHANGED = 'true';
      }
      LINK = "<%=request.getContextPath()%>/saveOthersData.do";
      FORM_LINK = document.dynaBeanList;
      PAGE_NAME = "Custom Data";
      function dataChanged(){
        DATA_CHANGED = 'true';
      }
      linkForward(errValue);
</script>
<script>
      var help = '<bean:message bundle="proposal" key="helpTextProposal.Others"/>';
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
