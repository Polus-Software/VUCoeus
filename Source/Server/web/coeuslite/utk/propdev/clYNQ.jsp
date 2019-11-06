<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ include file= "/coeuslite/mit/utils/CoeusContextPath.jsp"  %>
<%@ page import="java.util.List,edu.mit.coeuslite.utils.CoeusDynaBeansList,java.util.Vector"%>
<html:html>
<%String selectedTab =(String) request.getAttribute("selectedTab");
  request.setAttribute("selectedTab", selectedTab);
  if(selectedTab==null) {
    selectedTab =(String) request.getParameter("selectedTab");
  }
  CoeusDynaBeansList list = (CoeusDynaBeansList) session.getAttribute("ynqFormList");
  List lstGroupName = new Vector();
  List lstQuestionAnswer = new Vector();
  if(list.getBeanList() != null && !list.getBeanList().isEmpty()){
    lstGroupName = (Vector) list.getBeanList();
    lstQuestionAnswer = (Vector) list.getList();
  }
  String mode=(String)session.getAttribute("mode"+session.getId());
  boolean modeValue=false;
  if(mode!=null && !mode.equals("")) {   
      if(mode.equalsIgnoreCase("display")){
              modeValue=true;
      }
  }%>
<head><title>JSP Page</title>
</head>
<script language="javascript" type="text/JavaScript" src="<bean:write name='ctxtPath'/>/coeuslite/mit/utils/scripts/coeusLiteCalendar.js">
</script>
<script>
    var change = 'false';
    var errValue = false;
    var errLock = false;
    function openWindow(id) {
        document.getElementById(id+'_Show').style.display = 'none';
        document.getElementById(id+'_Hide').style.display = 'block';
        document.getElementById(id).style.display = 'block';
    }

    function closeWindow(id) {
        document.getElementById(id+'_Show').style.display = 'block';
        document.getElementById(id+'_Hide').style.display = 'none';
        document.getElementById(id).style.display = 'none';
    }
    
    function tabSelected(linkSelected, status) {
        if(status == 'G') {
            CLICKED_LINK = "<%=request.getContextPath()%>/getYNQDetails.do?selectedTab="+linkSelected+"&oldSelectedTab=<%=selectedTab%>";
            if(validateSavedInfo()) {
                document.ynqFormList.action = "<%=request.getContextPath()%>/getYNQDetails.do?selectedTab="+linkSelected+"&oldSelectedTab=<%=selectedTab%>";
                document.ynqFormList.submit();
            }
        } else {
            document.ynqFormList.action = "<%=request.getContextPath()%>/updateYNQDetails.do?selectedTab="+linkSelected+"&oldSelectedTab=<%=selectedTab%>";
            document.ynqFormList.submit();
        }
    }
    
    function changeValue(value,explanation,requiredDate,id,clicked) {
        if(clicked == true || clicked == 'true') {
            change = 'true';
            dataChanged();
        }
        if(explanation.indexOf(value)!=-1) {
            document.getElementById('explanation_'+id).style.display = 'block';
        } else {
            document.getElementById('explanation_'+id).style.display = 'none';
        }
        if(requiredDate.indexOf(value)!=-1) {
            document.getElementById('reviewDate_'+id).style.display = 'block';
        } else {
            document.getElementById('reviewDate_'+id).style.display = 'none';
        }
    }
    
     function showQuestion(link)
     {
        var winleft = (screen.width - 650) / 2;
        var winUp = (screen.height - 450) / 2;  
        var win = "scrollbars=1,resizable=1,width=790,height=450,left="+winleft+",top="+winUp
        sList = window.open('<bean:write name='ctxtPath'/>'+"/"+link, "list", win);
        if (parseInt(navigator.appVersion) >= 4) {
        window.sList.focus(); 
        }
     }    
    
</script>
<body>
<html:form action="/updateYNQDetails.do">
<table width="100%" height="100%"  border="0" cellpadding="0" cellspacing="0" class='table'>
<tr>
    <td>
        <table width="100%" height="100%" align=center border="0" cellpadding="0" cellspacing="0" class="tableheader">
        <tr>
            <td>
                <bean:message bundle="proposal" key="ynq.ynq"/>:                 
            </td>            
        </tr>
        </table>    
    </td>
</tr>

<tr>
    <td>
        <div id="helpText" class='helptext'>            
            <bean:message bundle="proposal" key="helpTextProposal.YNQ"/>  
        </div>
    </td>
</tr>


<tr>
    <td class='copy'>
        <font color='red'>
            <logic:messagesPresent> 
            <script>errValue = true;</script>
            <html:errors header="" footer=""/>
            </logic:messagesPresent> 
            <logic:messagesPresent message="true"> 
            <script>errValue = true;</script>
            <html:messages id="message" message="true" property="needToFill" bundle="proposal">                
                <li><bean:write name = "message"/></li>
             </html:messages>
            <html:messages id="message" message="true" property="errMsg" bundle="proposal">
                <script>
                    errLock = true;
                </script>
               <li><bean:write name = "message"/></li>
            </html:messages>                 
           </logic:messagesPresent>            
        </font>
    </td>
</tr>
<%if(lstGroupName!=null && lstGroupName.size()>0){%>
<tr>
    <td>
    
    <table width="100%" height="100%" align=center  border="1" cellpadding="1" cellspacing="1" class='tabtable'>
    <tr>
        <td class='copybold'>
            <logic:iterate id="dynaFormData" name="ynqFormList" property="beanList" type="org.apache.struts.action.DynaActionForm" indexId="index" scope="session">
            <%if(selectedTab!=null && dynaFormData.get("groupName")!=null && 
                 !dynaFormData.get("groupName").equals("") && selectedTab.equals(dynaFormData.get("groupName"))){%>
                <b><font color="#6D0202"><%=dynaFormData.get("groupName")%></font></b>
            <%} else if(dynaFormData.get("groupName")!=null && 
                            !dynaFormData.get("groupName").equals("")){
                String link = "javaScript:tabSelected('"+dynaFormData.get("groupName")+"','G')";%>
                <b><html:link href="<%=link%>"><u><%=dynaFormData.get("groupName")%></u></html:link></b>
            <%}%>&nbsp;&nbsp;&nbsp;
            </logic:iterate>
        </td>
     </tr>
     </table>
     </td>
</tr>

<tr>
    <td class='core'>
        <%String calImage = request.getContextPath()+"/coeusliteimages/cal.gif";%>
        <table width="99%" align=center  border="0" cellpadding="3" cellspacing="0" class='rowSelect'>
        <%if(lstQuestionAnswer!=null && lstQuestionAnswer.size()>0) {%>
        <tr>
            <td>
            <table width="100%" align=center  border="0" cellpadding="3" cellspacing="0">
            <tr class='theader'>
                <td width='10%' align=center valign=top>
                        <bean:message bundle="proposal" key="ynq.questionId"/>
                </td>
                <td>
                        <bean:message bundle="proposal" key="ynq.question"/>
                </td>               
            </tr>
            </table>
            </td>
        </tr>
        <logic:iterate id="dynaFormData" name="ynqFormList" property="list" type="org.apache.struts.action.DynaActionForm" indexId="index" scope="session">
        <%int numberOfAnswers = Integer.parseInt(dynaFormData.get("numberOfAnswers").toString());
          String explanationField = (dynaFormData.get("explanationReqFor")==null)?"":dynaFormData.get("explanationReqFor").toString();
          String reqDateField = (dynaFormData.get("dateReqFor")==null)?"": dynaFormData.get("dateReqFor").toString();
          String questionId = dynaFormData.get("questionId").toString();
          String clicked = "javascript:changeValue('Y','"+explanationField+"','"+reqDateField+"','"+questionId+"','true')";
          String answer = (dynaFormData.get("answer")==null)?"": dynaFormData.get("answer").toString();
        %>
        <tr>
            <td>
            <table width="100%" align=center  border="0" cellpadding="0" cellspacing="0" class="tabtable">
            <tr>
                <td width='10%' class='copy' align=center valign=top>
                        <bean:write name="dynaFormData" property="questionId"/>
                </td>
                <td class='copy' colspan='4'>
                        <bean:write name="dynaFormData" property="description"/>
                </td>
            </tr>
            <tr>
                <td width='10%' class='copybold'>
                    
                </td>            
                <td width='20%' class='copy'>
                    <html:radio name="dynaFormData" property="answer" value="Y" indexed="true" onclick="<%=clicked%>" disabled="<%=modeValue%>"/> 
                    &nbsp;<bean:message bundle="proposal" key="ynq.yes"/>
                </td>
                <td width='20%' class='copy'>
                    <%clicked = "javascript:changeValue('N','"+explanationField+"','"+reqDateField+"','"+questionId+"','true')";%>
                    <html:radio name="dynaFormData" property="answer" value="N" indexed="true" onclick="<%=clicked%>" disabled="<%=modeValue%>"/>
                    &nbsp;<bean:message bundle="proposal" key="ynq.no"/>
                </td>
                
                <td width='20%' class='copy'>
                <%if(numberOfAnswers==3) {
                    clicked = "javascript:changeValue('X','"+explanationField+"','"+reqDateField+"','"+questionId+"','true')";
                %>
                    <html:radio name="dynaFormData" property="answer" value="X" indexed="true" onclick="<%=clicked%>" disabled="<%=modeValue%>"/>
                    &nbsp;<bean:message bundle="proposal" key="ynq.n/a"/>
                <%} else {%>
                    &nbsp;
                <%}%>
                </td>
                <td width='30%' class='copybold' align=left>
                    <%String queLink = "javascript:showQuestion('/question.do?questionNo="+dynaFormData.get("questionId")+"')";%>
                    <html:link href="<%=queLink%>">
                        <bean:message bundle="proposal" key="ynq.more"/>
                   </html:link>
                </td>                
            </tr>
            <tr>
                <td>
                    &nbsp;
                </td>
                <td colspan='4'>
                    <%//if(!explanationField.equals("")) {%>
                    <div id='explanation_<%=questionId%>' style='display: none;'>
                    <table width="100%" align=center  border="0" cellpadding="1" cellspacing="1">
                    <tr>
                        <td width='12%' class='copybold' valign=top>
                            <bean:message bundle="proposal" key="ynq.explanation"/> :
                        </td>
                        <td colspan='4'>
                            <html:textarea name="dynaFormData" property="explanation" styleClass="textbox-longer" style="width: 565px;" indexed="true" disabled="<%=modeValue%>" onchange="dataChanged()"/>
                        </td>
                    </tr>
                    </table>
                    </div>
                    <%//} if(!reqDateField.equals("")) {
                      String calender ="javascript:displayCalendarWithTopLeft('dynaFormData["+index+"].reviewDate',8,25)";
                    %>
                    <div id='reviewDate_<%=questionId%>' style='display: none;'>
                    <table width="100%" align=center  border="0" cellpadding="1" cellspacing="1">
                    <tr>
                        <td width='12%' class='copybold' valign=top>
                            <bean:message bundle="proposal" key="ynq.reviewDate"/> :
                        </td>
                        <td colspan='4'>
                            <html:text name="dynaFormData" property="reviewDate" styleClass="textbox" indexed="true" disabled="<%=modeValue%>" onchange="dataChanged()"/>
                            <%if(!modeValue) {%>
                            <html:link href="<%=calender%>" >
                                    <html:img src="<%=calImage%>" border="0" height="16" width="16" onclick="dataChanged()"/>
                            </html:link>
                            <%}%>
                        </td>
                    </tr>
                    </table>
                    </div>
                    <%//}%>
                </td>
            </tr>
            </table>
            </td>
        </tr>
        <!--<tr>
            <td height='15'>
            </td>
        </tr>-->
        <script>
            <%if(answer.length()>0) {
                clicked = "'"+answer+"','"+explanationField+"','"+reqDateField+"','"+questionId+"','false'";%>
                changeValue(<%=clicked%>);
            <%}%>
        </script>
        </logic:iterate>
        <%if(!modeValue) {%>
        <tr class='table'>
            <td class='savebutton'>
                <%String linkValue = "javaScript:tabSelected('"+selectedTab+"','U')";;%>
                <html:button property="ok" value="Save" styleClass="clsavebutton" onclick="<%=linkValue%>"/>
            </td>
        </tr>          
        <%}} else {%>
        <tr>
            <td align=center height='35' valign=middle class='copybold'>
                <bean:message bundle="proposal" key="ynq.noQuestions"/>
            </td>
        </tr>
        <%}%>        
        </table>
    </td>
</tr>
<%} else {%>
<tr>
    <td align=center height='35' valign=middle class='copybold'>
        <bean:message bundle="proposal" key="ynq.noGroups"/>
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
      LINK = "<%=request.getContextPath()%>/updateYNQDetails.do?selectedTab=<%=selectedTab%>&oldSelectedTab=<%=selectedTab%>";
      FORM_LINK = document.ynqFormList;
      PAGE_NAME = "<bean:message bundle="proposal" key="ynq.ynq"/>";
      function dataChanged(){
            DATA_CHANGED = 'true';
      }
      linkForward(errValue);
</script>
<script>
      var help = '<bean:message bundle="proposal" key="helpTextProposal.YNQ"/>';
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
