<!--
/*
 * @(#)CoeusWebStartContent.jsp
 *
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 *
 * Created on Nov. 2, 2004
 *
 * @author  Coeus Dev Team
 * @version 1.0
 */
-->

<%@page  errorPage="web/ErrorPage.jsp"
	import="java.io.IOException,
		java.io.InputStream,
		java.util.Properties,
		edu.mit.coeus.utils.UtilFactory" %>
<%@ taglib uri='/WEB-INF/struts-html.tld' prefix='html' %>
<%@ taglib uri='/WEB-INF/struts-bean.tld' prefix='bean' %>

<%@ include file="web/CoeusContextPath.jsp"  %>
<jsp:include page="web/CoeusCacheRemove.jsp" flush="true" />



<%
//JM 6-11-2012 updated to version 4.5

	//System.out.println("inside CoeusWebStart.jsp");
	//Read application URL from properties file.  
	String appHomeURL = "";
	String jnlpURL = "";
// JM 6-2-2011 updated per 4.4.2
	String java32URL = "";
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
        if(appHomeURL == null){
        	appHomeURL = "";
		String errorMsg = "Value for APP_HOME_URL not found in coeus.properties.";
		System.out.println(errorMsg);
		UtilFactory.log(errorMsg, null, "", "");
        }        
        jnlpURL = appHomeURL + "coeus.jnlp";
// JM 3-6-2013 updated to use Java 1.8
		java32URL = appHomeURL + "jre-8u45-windows-i586.exe";
// JM END
%>

<html>
<head>
<title>Coeus</title>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
</head>
<!-- JM 6-2-2011 updated per 4.4.2 -->
<body bgcolor="#FFFFFF" text="000000" link="666666" vlink="666666" alink="gold">
	
 <table width="100%" border="0" cellspacing="0" cellpadding="0" style="background-color:black;">
    <tr>
<!-- JM 12-14-2014 update styles -->
      <td width="100%" valign="middle" align="center" height="85" style="padding-left:200px;">
      	<img src="coeusliteimages/irbBanner.gif" border="0" alt="Coeus Banner">
      </td>
    </tr>
 </table>

<hr>

<table>
<tr>
   <td valign="top">
		   	<script language="javascript">
			document.write('<a href="<%=jnlpURL%>">');
			document.write("<img src='coeus_vu.png' width='200' border='0'>");
			document.write('</a>');
		   	</script>
	        </td>
			<td>
				<table style="padding:0px 0px 0px 30px;">
				<tr>
					<td valign="top" style="padding:16px;">
					   	<font face="Verdana, Arial, Helvetica, sans-serif" size="2">
					   	<script language="javascript">
						document.write('<a href="<%=jnlpURL%>" style=\"color:#3366FF;text-decoration:none;\"><b>Launch Coeus 6.0</b></a>');
					   	</script>
					   	</font>
					   	<BR>
					   	<BR>
	                    <font face="Verdana, Arial, Helvetica, sans-serif" size="2">
	                    <script language="javascript">
							document.write("Coeus Premium is a 32-bit application and requires 32-bit Java 1.8 on your system.");
	                    </script>
	                    </font>
	                    <BR>
	                    <font face="Verdana, Arial, Helvetica, sans-serif" size="2">
	                    <script language="javascript">
							document.write("This version of Java will work on both 32-bit and 64-bit platforms.");			
	                    </script>
	                    <script language="javascript">
	             		document.write('<a href="<%=java32URL%>" style=\"color:#3366FF;text-decoration:none;\">Download Java 1.8</a>');
			    		</script>
	                    </font>
                	</td>
               	</tr>
            	<tr>
                	<td valign="top" style="padding:16px;">
	                    <font face="Verdana, Arial, Helvetica, sans-serif" size="2">
	                        <a href='coeuslite/mit/utils/cwCoeusIndex.jsp' style="color:#3366FF;text-decoration:none;">
	                        <b>Launch CoeusLite</b></a>
	                    </font>
	                </td>
	    		</tr>
			</table>
   		</td>
	</tr>
</table>


<hr>

<br>			
</body>
</html>
