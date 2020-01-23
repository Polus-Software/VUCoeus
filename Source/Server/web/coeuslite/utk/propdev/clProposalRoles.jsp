<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ include file= "/coeuslite/mit/utils/CoeusContextPath.jsp"  %>
<%@ page import="org.apache.struts.validator.DynaValidatorForm ,
        edu.mit.coeuslite.utils.CoeusLiteConstants,java.util.HashMap"%>
<jsp:useBean id="proposalRolesDetails" scope="session" class="java.util.TreeMap" />
<jsp:useBean id="proposalRoleNameId" scope="session" class="java.util.HashMap" />
<html:html>
<%  String EMPTY_STRING = "";
    String link = EMPTY_STRING;
    String mode=(String)session.getAttribute("mode"+session.getId());
  boolean modeValue=false;
  if(mode!=null && !mode.equals("")) {   
      if(mode.equalsIgnoreCase("display")){
              modeValue=true;
      }
  }
  if(!modeValue) {//Not in Display Mode. Case 4000
      Boolean hasMaintainRight = (Boolean)request.getAttribute("hasMaintainRight");
      if(hasMaintainRight==null || !hasMaintainRight.booleanValue()) {
          modeValue=true; //Do not show 'Add User' link
      }
  }
%>
<head><title>JSP Page</title>
<script>
var errValue = false;
var errLock = false;

function showHide(val,value){
var panel = 'Panel'+value;
var pan = 'pan'+value;
var hidePanel  = 'hidePanel'+value;
if(val == 1){
            document.getElementById(panel).style.display = "none";
            document.getElementById(hidePanel).style.display = "block";
            document.getElementById(pan).style.display = "block";
        }
else if(val == 2){
            document.getElementById(panel).style.display = "block";
            document.getElementById(hidePanel).style.display = "none";
            document.getElementById(pan).style.display = "none";
        }        

}

function updateData(){
    document.proposalRolesForm.action = "<%=request.getContextPath()%>/saveProposalRoles.do?";
    document.proposalRolesForm.submit();
}
 
function removeLink(value, removeData){
if(confirm('<bean:message bundle="proposal" key="proposalRoles.confirm"/>')){
    document.proposalRolesForm.action = "<%=request.getContextPath()%>/deleteUser.do?removeRole="+value+"&removeUser="+removeData;
    document.proposalRolesForm.submit();
   } 
}       
var roleName ="";
function searchWindow(value) {       
    roleName = value;
    var winleft = (screen.width - 650) / 2;
    var winUp = (screen.height - 450) / 2;  
    var win = "scrollbars=1,resizable=1,width=750,height=450,left="+winleft+",top="+winUp;        
       sList = window.open('<%=request.getContextPath()%>/irbSearch.do?type=<bean:message key="searchWindow.title.person"/>&search=true&searchName=USERSEARCH', "list", win);  
        if (parseInt(navigator.appVersion) >= 4) {
        window.sList.focus(); 
        }
    } 

 function fetch_Data(result){        
          var userId ="";
          var userName = "";
          var unitNum = "";
          var unitName = "";
          var homeUnit = "";
          if(result["USER_ID"] != 'null' && result["USER_ID"] != undefined){
          userId = result["USER_ID"];
          }
          if(result["USER_NAME"] != 'null' && result["USER_NAME"] != undefined){
          userName = result["USER_NAME"];
          }
          if(result["UNIT_NUMBER"] != 'null' && result["UNIT_NUMBER"] != undefined){
          unitNum = result["UNIT_NUMBER"];
          }
          if(result["UNIT_NAME"] != 'null' && result["UNIT_NAME"] != undefined){
          unitName = result["UNIT_NAME"];
          }
          homeUnit = unitNum+" : "+unitName;
          document.proposalRolesForm.action = "<%=request.getContextPath()%>/addUser.do?propRole="+roleName+"&propUserId="+userId+"&propUserName="+userName+"&propUnitNum="+unitNum+"&propUnitName="+unitName+"&userHomeUnit="+homeUnit;
          document.proposalRolesForm.submit();
    }        
</script>
</head>
<body>

<html:form action="/getProposalRoles">
<table width='100%' border='0' cellpadding='0' cellspacing='0' class='table'>
<%String plus = request.getContextPath()+"/coeusliteimages/plus.gif";
  String minus = request.getContextPath()+"/coeusliteimages/minus.gif";%>
<tr class='core' >
    <td>
        <table width='100%' border='0' cellpadding='2' cellspacing='0' class='tabtable'>
        <tr class='tableheader'>
            <td colspan='4'>
               <bean:message bundle="proposal" key="proposalRoles.ProposalRoles"/>
            </td>
        </tr>
          <tr>
            <td align="left" valign="top">
                <div id="helpText" class='helptext'>            
                        <bean:message bundle="proposal" key="helpTextProposal.ProposalRoles"/>  
                </div>
            </td>
          </tr>        
        <tr>
            <td class='copy' colspan='4'>
                <font color="red">
               <logic:messagesPresent message = "true">
                <script>errValue = true;</script>
                <html:messages id="message" message="true" property="errMsg">
                    <script>errLock = true;</script>
                   <li><bean:write name = "message"/></li>
                </html:messages>
                <html:messages id="message" message="true" bundle="proposal" property="proposal.userId">                
                   <li><bean:write name = "message"/></li>
                </html:messages>                
               </logic:messagesPresent>  
               </font>
            </td>
        </tr>
       
                <%int count=1;%>
                <logic:iterate id="tmapId"  name="proposalRolesDetails" indexId="index">                                                                                    
                                 <tr>
                                  <bean:define id="proposalRole" name="tmapId" property="key" />
                                  <% HashMap hmRoleName = (HashMap)session.getAttribute("proposalRoleNameId");
                                       String roleNam = (String)hmRoleName.get(proposalRole);
                                    %>
                                 <td class='copybold'>  
                                 <%String divName="Panel"+count;%>
                                 <div id='<%=divName%>' style='display:none;'>
                                    
                                    <%String divlink = "javascript:showHide(1,'"+count+"')";%>
                                                        <html:link href="<%=divlink%>">
                                                        <!-- Modified for Case#3291 - Start
                                                            To avoid the double quotes when setting the attributes to img tag-->
                                                        <%String imagePlus=request.getContextPath()+"/coeusliteimages/plus.gif";%>
                                                        <html:img src="<%=imagePlus%>" border="0"/>
                                                        <%--<html:img src="<%=request.getContextPath()+"/coeusliteimages/plus.gif"%>" border="0"/> --%>
                                                        <!-- Modified for Case#3291 - End -->
                                                        </html:link>&nbsp;<%=roleNam%>
                                      &nbsp;  <%--html:text  styleClass="cltextbox-color" name="tmapId" style="cltextbox-long" property="key" readonly="true" indexed="true"/--%>
                                    
                                    </div>
                                    <%divName="hidePanel"+count;%>
                                    <div id='<%=divName%>' >
                                    
                                    <% divlink = "javascript:showHide(2,'"+count+"')";%>
                                                        <html:link href="<%=divlink%>">
                                                        <!-- Modified for Case#3291 - Start
                                                            To avoid the double quotes when setting the attributes to img tag-->
                                                        <%String imageMinus=request.getContextPath()+"/coeusliteimages/minus.gif";%>
                                                        <html:img src="<%=imageMinus%>" border="0"/>
                                                        <%--<html:img src="<%=request.getContextPath()+"/coeusliteimages/minus.gif"%>" border="0"/> --%>
                                                        <!-- Modified for Case#3291 - End -->
                                                        </html:link>&nbsp;<%=roleNam%>
                                      &nbsp;  <%--html:text  styleClass="cltextbox-color" name="tmapId" style="cltextbox-long" property="key" readonly="true" indexed="true"/--%>
                                    
                                    </div>
                                    </td>
                                    <td>
                                    
                                    </td>
                                    <td align=right>
                                     
                                    <% 
                                    if(!modeValue){
                                    if(!roleNam.equals("Approver")){
                                    link = "javaScript:searchWindow('"+proposalRole+"');";%>
                                    <html:link href="<%=link%>">
                                        <u><bean:message bundle="proposal" key="proposalRoles.UsersLink"/></u>
                                    </html:link>  
                                    <%}}%>
                                    </td>
                                 </tr> 
                                 
                               <tr>
                                    <td colspan='3'> 
                                   <%divName="pan"+count;%>
                                <div id='<%=divName%>'>
                                        <table width='100%'  border='0' cellpadding='3' cellspacing='1' class='tabtable'>
                                            <tr>
                                                <td  class='theader' width='5%' nowrap><bean:message bundle="proposal" key="proposalRoles.UserID"/>  </td>
                                                <td  class='theader' width='30%'><bean:message bundle="proposal" key="proposalRoles.UserName"/>  </td>
                                                <td  class='theader' colspan='2' width='50%'><bean:message bundle="proposal" key="proposalRoles.HomeUnit"/></td>
                                               <%if(!modeValue){ %><td  class='theader' width='15%'> </td><%}%> 
                                            </tr>
                                             <% String strBgColor = "#DCE5F1";
                                                         int row=0;                                                        
                                                        %>                                            
                                             <bean:define id="beanData"  name="tmapId" property="value" type="java.util.Vector" />   
                                             <logic:iterate id="user" name="beanData" type="org.apache.struts.validator.DynaValidatorForm">
                                             <%if (row%2 == 0) {

                                                    strBgColor = "#D6DCE5"; 
                                                }
                                               else { 
                                                strBgColor="#DCE5F1"; 
                                             } %>
                                             <bean:define id="propUserName" name="user" property="userName" />
                                                <%String userName =(propUserName.toString().length() > 60 ?(propUserName.toString()).substring(0,61)+"....":propUserName.toString() ); %>
                                                
                                            <%--if(!(user.get("acType").equals("D"))){--%>
                                             <tr bgcolor="<%=strBgColor%>" onmouseover="className='TableItemOn'" onmouseout="className='TableItemOff'">
                                                <td class='copy'><bean:write name="user" property="userId"/><bean:define id="roleUserId" name="user" property="userId" /></td>
                                                <td class='copy'><%=userName%></td>
                                                <td class='copy' colspan='2'><bean:write name="user" property="homeUnit"/></td>
                                                <% String remove = "javaScript:removeLink('"+proposalRole+"','"+roleUserId+"');"; %>
                                                <%if(!modeValue){ %>
                                              
                                                <td class='copy' align=center>
                                                    <%-- Modified for CASEID -COEUSQA-1367 Lite allows removal of all users from a dev proposal_UTC - START
                                                        if(!roleNam.equals("Approver")){%>
                                                            <html:link href="<%=remove%>">
                                                                <bean:message bundle="proposal" key="proposalRoles.Remove"/>
                                                            </html:link>  
                                                         }  
                                                    --%>
                                                    <%if(!roleNam.equals("Approver") && !roleNam.equals("Aggregator")){%>
                                                    <html:link href="<%=remove%>">
                                                        <bean:message bundle="proposal" key="proposalRoles.Remove"/>
                                                    </html:link>  
                                                    <%}if(roleNam.equals("Aggregator") && beanData.size() > 1) { %> 
                                                      <html:link href="<%=remove%>">
                                                        <bean:message bundle="proposal" key="proposalRoles.Remove"/>
                                                    </html:link>  
                                                    <%}%>
                                                    
                                                    <%--Modified for CASEID - COEUSQA-1367 Lite allows removal of all users from a dev proposal_UTC  END --%>
                                                </td>
                                                 <%}%>
                                             </tr>
                                             <%//}
                                             row++;               %>
                                             </logic:iterate> 
                                             
                                        </table>
                                       </div>
                                    </td>    
                                </tr>                              
                <%count++;%>      
             </logic:iterate>        
        <tr>
            <td height='10'>
            </td>
        </tr>
        </table>
    </td>
</tr>
<%--if(!modeValue){ %>
 <tr>
    <td colspan='3' class='savebutton'>
       <html:button property="save" value="Save" styleClass="clsavebutton"  onclick="updateData()" />
    </td>
 </tr>
 <%}--%> 
</table>
</html:form>
<%--script>
      DATA_CHANGED = 'false';
      var dataModified = '<%=request.getAttribute("dataModified")%>';
      if(dataModified == 'modified' || (errValue && !errLock)){
            DATA_CHANGED = 'true';
      }
      LINK = "<%=request.getContextPath()%>/saveProposalRoles.do?";
      PAGE_NAME = "<bean:message bundle="proposal" key="proposalRoles.ProposalRoles"/>";
      FORM_LINK = document.proposalRolesForm;
      function dataChanged(){
        DATA_CHANGED = 'true';
      }
      linkForward(errValue);
</script>
<script>
      var help = '<bean:message bundle="proposal" key="helpTextProposal.ProposalRoles"/>';
      help = trim(help);
      if(help.length == 0) {
        document.getElementById('helpText').style.display = 'none';
      }
      function trim(s) {
            return s.replace( /^\s*/, "" ).replace( /\s*$/, "" );
      }  
</script--%>
</body>
</html:html>

