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

<%@page errorPage="web/ErrorPage.jsp"
	import="java.io.IOException,
		java.io.InputStream,
		java.util.Properties,
		edu.mit.coeus.utils.UtilFactory"%>
<%@ taglib uri='/WEB-INF/struts-html.tld' prefix='html'%>
<%@ taglib uri='/WEB-INF/struts-bean.tld' prefix='bean'%>

<%@ include file="web/CoeusContextPath.jsp"%>
<jsp:include page="web/CoeusCacheRemove.jsp" flush="true" />



<%		
	//System.out.println("inside CoeusWebStart.jsp");
	//Read application URL from properties file.  
	String appHomeURL = "";
	String jnlpURL = "";
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
%>

<html>
<head>
<title>Coeus</title>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
</head>

<body bgcolor="#FFFFFF" text="000000" link="7F1B00" vlink="666666"
	alink="ffffff">

	<table width="586" border="0" cellspacing="0" cellpadding="0">
		<tr>
			<td colspan="2" valign="top" align="left" height="85"><img
				src="images/coeusheader.gif" width="780" height="111" border="0"
				alt="OSP Banner"></td>
		</tr>
	</table>

	<hr>

	<table>
		<!-- This table has two columns, with a table in each column.  Left col has the logo. Right col has the text. -->
		<tr>
			<td valign="top">
				<table>
					<tr>
						<td valign="top"><script language="javascript">
			document.write('<a href="<%=jnlpURL%>">');
			document.write("<img src='coeus_splash.jpg' width='200' border='0'>");
			document.write('</a>');
		   </script></td>
					</tr>
					<tr>
						<td valign="center"><font
							face="Verdana, Arial, Helvetica, sans-serif" size="2"> <script
									language="javascript">
			document.write('<a href="<%=jnlpURL%>"><b>Launch Coeus 4.3.1.1</b></a>');
			</script>
						</font></td>
						<td><font face="Verdana, Arial, Helvetica, sans-serif"
							size="2"> <script language="javascript">
                        document.write("Note: Coeus premium requires Java 1.5 on your desktop");
                        document.write('  <a href="http://coeus.mit.edu/jre_installer/jre-1_5_0_16-windows-i586-p.exe"> Download Java 1.5</a>');
                    </script>
						</font></td>
					</tr>
				</table>
			</td>
		<tr>
			<table>
				<tr>
					<td><font face="Verdana, Arial, Helvetica, sans-serif"
						size="2"> <a href='coeuslite/mit/utils/cwCoeusIndex.jsp'><b>
									Launch Coeus Lite</b></a>
					</font></td>
				</tr>
			</table>
		</tr>
	</table>


	<hr>

	<br>
</body>
</html>
