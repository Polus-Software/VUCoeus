<%--
Content for Welcome.jsp, which uses Coeus Template.
--%>


<%@page  errorPage="ErrorPage.jsp"
	import="java.io.IOException,
		java.io.InputStream,
		java.util.Properties,
		edu.mit.coeus.utils.UtilFactory" %>
<%@ taglib uri='/WEB-INF/struts-html.tld' prefix='html' %>
<%@ taglib uri='/WEB-INF/struts-bean.tld' prefix='bean' %>

<%@ include file="CoeusContextPath.jsp"  %>

<style type="text/css">
<!--
.header {font-family:verdana; font-size:13px; font-weight:bold}
-->
</style>

<%	System.out.println("inside WelcomeContent.jsp");
	
	//Read application URL from properties file.  
	//UtilFactory utilFactory = new UtilFactory();
	String appHomeURL = "";
	String launchURL = "";
       	InputStream is = null;  
       	try{
        	is = getClass().getResourceAsStream("/coeus.properties");
        }
        catch(Exception ex){
            String errorMsg = "Can't read the properties file. ";
            errorMsg += "Make sure coeus.properties is in the CLASSPATH or in the classes folder.";
            System.out.println(errorMsg);
        }
        Properties coeusProps = new Properties();
        try {
             	coeusProps.load(is);
        	appHomeURL = coeusProps.getProperty("APP_HOME_URL");
        }
        catch (Exception e) {
            String errorMsg = "Value for APP_HOME_URL not found in coeus.properties.";
            System.out.println(errorMsg);
            UtilFactory.log(errorMsg, e, "", "");
        }
        launchURL = appHomeURL + "CoeusWebStart.jsp";
        
%>

<table width="100%" border="0"   cellpadding="0" cellspacing="0" width="657" >
            <tr>
            <td height="5"></td>
          </tr>
	<tr bgcolor="#cccccc">
		<td align="left" height="23" colspan="4" class="header">
			&nbsp;<bean:message key="welcome.headerBar"/>
		</td>
	</tr>
</table>

<table border="0" cellpadding="5" cellspacing="0" width="100%">

<tr>
	<td>
              <bean:message key="welcome.firstNote" />
	</td>
</tr>

<tr>
	<td>
		You will need an 
		<a href="http://web.mit.edu/ist/topics/certificates/">MIT Certificate</a> to access these pages.
	</td>
</tr>
<tr>
	<td height="1">
              &nbsp;
	</td>
</tr>
</table>
<hr />
<table>

<tr>

	<td>
		<%--<bean:message key="welcome.secondNote"/>--%>
	    <b><bean:message key="welcome.text3" /></b>
	    <%--<bean:message key="welcome.fourthNote" />--%>
	    <ul>
	    <li><bean:message key="welcome.text4"/><html:link forward="loginCOI" > 
		<bean:message key="welcome.text5"/>
	    	</html:link>.
	    </li>
	    <li><bean:message key="welcome.text6"/>
	    	<a  href="<bean:write name='ctxtPath'/>/getInboxMessages.do?messageType=unresolved" > 
	    	<bean:message key="welcome.text7"/>
	    	</a>.
	    </li>
	    <li><html:link forward="changePassword">
		<bean:message key="welcome.text8"/>
	    	</html:link>
	    </ul>
	</td>
</tr>
<tr>
	<td height="1">
              &nbsp;
	</td>
</tr>
</table>
<hr/>
<table>

<tr>
	<td >
		<bean:message key="welcome.applicationLink1"/>
		<%=launchURL%>
		<bean:message key="welcome.applicationLink2"/>
	</td>
</tr>
<tr>
	<td height="40">
              &nbsp;
	</td>
</tr>
</tr>
<tr>
	<td>
		<img src="<bean:write name="ctxtPath"/>/images/welcome1-04.gif" width="657" height="8" border="0">
	</td>
</tr>
</table>