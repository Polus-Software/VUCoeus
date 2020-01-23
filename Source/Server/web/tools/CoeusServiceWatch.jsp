<%@page contentType="text/html" import="java.util.*,edu.mit.coeus.utils.scheduler.SchedulerEngine"%>
<%@page pageEncoding="UTF-8"%>
<html>
<head><title>Coeus Services Page</title></head>
<body>

<%-- <jsp:useBean id="beanInstanceName" scope="session" class="beanPackage.BeanClassName" /> --%>
<%-- <jsp:getProperty name="beanInstanceName"  property="propertyName" /> --%>
<table>
<%
String context = request.getContextPath();
Hashtable maps = SchedulerEngine.getInstance().getTaskTimers();
if(maps!=null && maps.size()>0){
%>
<form name='frmServiceWatch' action='<%=context%>/SchedulerServiceMaintServlet'>
<th align='left' bgcolor='ggf000' colspan='2'>Select Task Id to Stop Individual Task</th>
<tr>
<td>
<input type='hidden' name='acType' value='stoptask'>
<select name='jobName'>
<%
Enumeration keys = maps.keys();
while(keys.hasMoreElements()){
    String str = keys.nextElement().toString();
%>
<option value='<%=str%>'><%=str%></option>
<%
}
%>
</select>
<!--<input type="text" name='jobName'>-->
</td>
<td>
<input type='submit' value='Stop Task'>
</td>
</tr>
</form>
<form name='frmServiceWatch' method='post' action='<%=context%>/SchedulerServiceMaintServlet?acType=stopalltasks'>
<tr><td><input type='submit' value='Stop All Tasks'></td>
</form>
<%
}
%>
<form name='frmServiceWatch' method='post' action='<%=context%>/SchedulerServiceMaintServlet?acType=restartalltasks'>
<td><input type='submit' value='Re-Start All Tasks'></td></tr>
</form>
</table>
</body>
</html>
