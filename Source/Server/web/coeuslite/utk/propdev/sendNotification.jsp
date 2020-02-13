<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
   "http://www.w3.org/TR/html4/loose.dtd">
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <%@page import="java.util.Vector;import org.apache.batik.dom.util.HashTable;import java.util.Arrays"%>
        <%@ taglib uri="/WEB-INF/struts-html.tld" 	prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" 	prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" 	prefix="logic" %>
<%@ taglib uri="/WEB-INF/coeus-utils.tld"	prefix="coeusUtils" %>
<%@ include file= "/coeuslite/mit/utils/CoeusContextPath.jsp"%>
<%@ page import="java.util.*,edu.utk.coeuslite.propdev.bean.rightPersonBean"%>
<jsp:useBean id="rightPersonBean" scope="session" class="edu.utk.coeuslite.propdev.bean.rightPersonBean" />
<jsp:useBean id ="name" scope ='request' class = "java.lang.String" />
<jsp:useBean id ="lastnotificationdate" scope ='request' class = "java.util.Date" />
 <jsp:useBean id="ressList" scope="session" class="java.util.Vector"/>
<%
String proposalNumber=(String)request.getParameter("proposalNumber");

%>
        <title>Send Notification</title>
         <link rel=stylesheet href="<bean:write name='ctxtPath'/>/coeuslite/mit/utils/css/ipf.css" type="text/css">
           <link href="<bean:write name='ctxtPath'/>/coeuslite/mit/utils/css/coeus_styles.css" rel="stylesheet" type="text/css">
        <script language="javascript">
            function  validate()
            {
                var total="";
                var len=document.rightPersonBean.check.length;

              if(len== undefined)
                 {
                     if(document.rightPersonBean.check.checked)
                      {

                          total+=document.rightPersonBean.check.value
                      }
                 }
                else  if(len==0)
                    {
                        alert("This proposal has no proposal persons");
                    }
             else{
                   for(var i=0; i < document.rightPersonBean.check.length; i++)
                   {
                      if(document.rightPersonBean.check[i].checked)
                      {
                       total+=document.rightPersonBean.check[i].value
                      }
                   }
                 }
if(total==""){
alert("Please Select a Person")
}
else
    {
 document.rightPersonBean.action = "<%=request.getContextPath()%>/sendEmail.do";
 document.rightPersonBean.submit();
 if(document.getElementById('something').style.visibility == 'visible'){
			document.getElementById('something').style.visibility = 'hidden';
		}else{
		document.getElementById('something').style.visibility = 'visible';
		}
    }
}
    function sendNotification(){
        alert("Entering ");
        document.forms[0].action="<%=request.getContextPath()%>/sendNotificationMsg.do";
        alert("action is"+document.forms[0].action)
        document.forms[0].submit();
    }
function selectDeselectAll(value)
{
 var len=document.rightPersonBean.check.length;
 if(len==undefined){
     document.rightPersonBean.check.checked=value;
}
else{
    for(var i=0; i < len;i++)

   document.rightPersonBean.check[i].checked=value;
}
}
        </script>
    </head>
    <body>
         <html:form action="/sendEmail.do" >
             <logic:present name="notificationDetails">
                 <table width="600px" height="100%"  border="0" cellpadding="0" cellspacing="0" class="table" align="center">
    <tr>
          <td>
              &nbsp;
          </td>
    </tr>
    <!-- CertificationQuestion - Start  -->
    <tr>
      <td height="10" align="left" valign="top">
          <tr>
            <td colspan="2" align="left" valign="top">
                <table width="100%" height="20"  border="0" cellpadding="0" cellspacing="0" class="tableheader">
                <tr>
                    <td> <center>Send Notification</center>  </td>
                </tr>
            </table></td>
          </tr>
            <tr>
                    <td align="center" valign="top">
                         <table width="100%"  border="0" cellspacing="1" cellpadding="3" class="tabtable" align="center">
                        <tr  valign="top">
                            <td  width="20%"  class="theader" align="center">Select</td>
                            <td   class="theader" align="left">Name</td>
                            <td  class="theader" align="left">Last Notification</td>
                        </tr>
                   <logic:iterate id="sendNotificList" name="notificationDetails">
                       <tr>
                           <td class="copy" align="center">
                           <input type="checkbox"  name="check" value="<bean:write name='sendNotificList' property='id'/>" />
                        </td>
                        <td class="copy" align="left">
                      <bean:write name="sendNotificList" property="name"/>
                        </td>
                    <td class="copy" align="left">
                     <bean:write name="sendNotificList" property="notificationDate"/>
                        </td>
                           </tr>
                           </logic:iterate>
                            <tr>
                                <td align="left">Select : <a href="javascript:selectDeselectAll(true)">All</a> | <a href="javascript:selectDeselectAll(false)">None</a></td>
                                <td align="center" colspan="2">
                          <input type="button" name="sendButton" value="Send"  Class="clsavebutton" onclick="validate()"/>
                          <input type="button" value="Close" class="clsavebutton" onclick="javascript:window.close()"/>
                              </td>
                               </tr>
                         </table>
                         </td>
                         </tr>
               <tr>
                  <td>
                      &nbsp;
                  </td>
              </tr>
        </table>
                <table width="100%" border="0" cellpadding="0" cellspacing="0"   colspan="3" class="table1">
                    <tr></tr>
                          <tr></tr>
                                <tr></tr>
                    </table>
                               </logic:present>
                <logic:present name="mailSend">
                   <div id="send_mail" align="center" style="height:100px;width: 400px;margin-top: 10%;border: 1px solid black;background-color:#d1e5fe;margin-left:auto;margin-right: auto;">
                     <table width="50%">
                         <tr>
                     <h4><center>All Notifications Are Sent</center></h4>
                        </tr>
                        <tr>
                        <center><input type="button" value="Close" Class="clsavebutton" onclick="javascript:window.close()"/></center>
                        </tr>
                     </table>
                        </div>
                </logic:present>
             <logic:notPresent name="mailSend">
              <logic:notPresent name="notificationDetails">
                  <div id="send_mail" align="center" style="height:100px;margin-top: 10%;border: 1px solid black;background-color:#d1e5fe;margin-left:auto;margin-right: auto;">
                   <table width="80%">
                         <tr>
                         <h4><center><font color="red">Notifications not sent due to missing mail address.</font></center></h4>
                        </tr>
                        <tr align="center">
                        <center><input type="button" value="Close" Class="clsavebutton" onclick="javascript:window.close()"/></center>
                        </tr>
                     </table>
                   </div>
              </logic:notPresent>

             </logic:notPresent>
             <div id="something" style="visibility: hidden;overflow: hidden;width:300px;height:50px;border: 1px solid black;background-color:#d1e5fe;margin-left:auto;margin-right: auto;">
               Please wait...!!</div>
               </html:form>

