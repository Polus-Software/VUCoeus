<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ include file= "/coeuslite/mit/utils/CoeusContextPath.jsp"  %>
<%@ page import="java.util.Vector,org.apache.struts.validator.DynaValidatorForm"%>
<jsp:useBean id="vecAbstractTypes" scope="session" class="java.util.Vector" />
<jsp:useBean id="vecProposalAbstracts" scope="session" class="java.util.Vector" />
<html:html>
<%String display =(String) request.getAttribute("display");
  String mode=(String)session.getAttribute("mode"+session.getId());
  boolean modeValue=false;
  if(mode!=null && !mode.equals("")) {   
      if(mode.equalsIgnoreCase("display")){
              modeValue=true;
      }
  }
String menuCode = request.getParameter("MENU_CODE");
//Added for case#2349 - Update timestamp for the Abstracts Module
String updateUser = "";
String updateTimestamp = "";
%>
<head><title>JSP Page</title>
<style type="text/css">
</style>
</head>
<script>
    var errValue = false;
    var errLock = false;
    function save(value,stamp) {
        DATA_CHANGED = 'false'
        document.abstractForm.showTab.value = value;
        document.abstractForm.hideTab.value = '<%=display%>';
        document.abstractForm.action = "<%=request.getContextPath()%>/saveAbstract.do?MENU_CODE=<%=menuCode%>";
        document.abstractForm.submit();
    }
    function pageSave() {
        document.abstractForm.action = "<%=request.getContextPath()%>/saveAbstract.do?MENU_CODE=<%=menuCode%>";
        document.abstractForm.submit();
    }
</script>
<body>
<html:form action="/getAbstract.do" focus="abstract">
<table width="100%" height="100%"  border="0" cellpadding="4" cellspacing="0" class='table'>
<tr class='tableheader'>
    <!--td>
        <table width="100%" height="100%" align=center border="0" cellpadding="0" cellspacing="0" class="tableheader">
        <tr-->
            <td>
                <bean:message bundle="proposal" key="proposalAbstract.abstract"/>
               
            </td>
        <!--/tr>
        </table>
    </td-->
</tr>
        <tr>
            <td>
                <div id="helpText" class='helptext'>            
                    <bean:message bundle="proposal" key="helpTextProposal.Abstract"/>  
                </div>  
            </td>
        </tr> 
<tr>
    <td>
    
    <table width="100%" height="100%" align=center  border="0" cellpadding="2" cellspacing="0" class='tabtable'>
    <tr>
        <td>
            <!-- Showing the Locking messages - start -->
            <logic:messagesPresent message = "true">
                <script>errValue = true;</script>
                <!-- If lock is deleted then show this message --> 
                <font color='red'>
                  <html:messages id="message" message="true" property="errMsg" bundle="proposal">
                        <script>errLock = true;</script>
                        <li><bean:write name = "message"/></li>
                  </html:messages>
                </font>
            </logic:messagesPresent>
            <!-- Showing the Locking messages - End -->
        </td>
    </tr>

    <tr>
        <td class='copybold'>
        
            <logic:present name = "vecAbstractTypes">            
                <logic:iterate id="documentType" name="vecAbstractTypes" type="org.apache.struts.validator.DynaValidatorForm"> 
                    <%String code = (String) documentType.get("abstractTypeCode");
                      String updateTimeStamp = (String) documentType.get("updateTimestamp");  
                      String image= request.getContextPath()+"/coeusliteimages/check_ti.gif";
                      String scriptCall = "javaScript:save('"+code+"')";
                      if(vecProposalAbstracts!=null && vecProposalAbstracts.size()>0) {
                        for(int index=0;index<vecProposalAbstracts.size();index++){
                            DynaValidatorForm dynaValidatorForm = (DynaValidatorForm) vecProposalAbstracts.get(index);
                            if(dynaValidatorForm!=null && dynaValidatorForm.get("abstractTypeCode").equals(code)) {%>
                                <html:img src="<%=image%>"/>
                                <%break;
                            }
                        }
                      }%>
            <%if(Integer.parseInt(display)==Integer.parseInt(code)){%>
                <b><font color="#6D0202"><%=documentType.get("description")%></font></b>
            <%} else {%>
                <b><html:link href="<%=scriptCall%>"><u><%=documentType.get("description")%></u></html:link></b>
             <%}%>&nbsp;&nbsp;&nbsp;
             
            </logic:iterate>                 
           </logic:present>
        </td>
     </tr>
     
     </table>
     </td>
</tr>
<tr>
    <td>
        <!--<table width="100%" height="100%" align=center  border="0" cellpadding="0" cellspacing="0">
        <tr>
            <td>-->
                <html:textarea name="abstractForm" property="abstract" disabled="<%=modeValue%>" styleClass="copy" cols="150" rows="25" onchange="dataChanged()"/>
                <script>
                        if(navigator.appName == "Microsoft Internet Explorer")
                        {
                            document.abstractForm.abstract.cols=158;
                            document.abstractForm.abstract.rows=23;
                        }                    
                </script>
            <!--</td>
        </tr>
        </table>-->
    </td>
</tr>

<!-- Added for case#2349 - Update timestamp for the Abstracts Module - start -->
<tr>
    <td>
        <b>Last Updated by :&nbsp;</b>
        <%
            if(vecProposalAbstracts!= null && vecProposalAbstracts.size() > 0){
                for(int index=0;index<vecProposalAbstracts.size();index++){
                    DynaValidatorForm dynaValidatorForm = (DynaValidatorForm) vecProposalAbstracts.get(index);
                    if(dynaValidatorForm!=null && dynaValidatorForm.get("abstractTypeCode").equals(display)) {
                        updateUser = (String)dynaValidatorForm.get("updateUser");
                        break;
                    }
                }                
            }
        %>
        <%=updateUser%>        
    </td>
</tr>
<!-- Added for case#2349 - Update timestamp for the Abstracts Module - end -->

<tr>
    <td class="savebutton">
            <html:button property="ok" value="Save" styleClass="clsavebutton" disabled="<%=modeValue%>" onclick="pageSave();"/>
    </td>
</tr>     
</table>
<html:hidden property="showTab" name="abstractForm"/>
<html:hidden property="hideTab" name="abstractForm"/>
<html:hidden property="awUpdateTimestamp" name="abstractForm"/>
</html:form>
<script>
      DATA_CHANGED = 'false';
      if(errValue && !errLock) {
            DATA_CHANGED = 'true';
      }
      LINK = "<%=request.getContextPath()%>/saveAbstract.do";
      FORM_LINK = document.abstractForm;
      document.abstractForm.showTab.value = '<%=display%>';
      document.abstractForm.hideTab.value = '<%=display%>';
      PAGE_NAME = "<bean:message bundle="proposal" key="proposalAbstract.abstract"/>";
      function dataChanged(){
        DATA_CHANGED = 'true';
      }
      linkForward(errValue);
</script>
<script>
      var help = '<bean:message bundle="proposal" key="helpTextProposal.Abstract"/>';
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
