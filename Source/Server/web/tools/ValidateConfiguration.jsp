<%@page contentType="text/html" import="java.io.*,java.util.*,
                    edu.mit.coeus.utils.*,java.sql.*,java.lang.reflect.*,
                    javax.sql.*,javax.naming.*,
                    java.security.*,java.security.cert.*,
                    javax.net.ssl.*,gov.grants.apply.soap.util.*,edu.mit.coeus.utils.mail.*,
                    edu.mit.coeus.user.auth.*,edu.mit.coeus.user.auth.bean.*,
                    edu.mit.coeus.bean.*,edu.mit.coeus.brokers.RequesterBean"%>
<%@page pageEncoding="UTF-8"%>
<html>
    <head><title>JSP Page</title></head>
    <body>
<%
    //ENTER USERID and PASSWORD HERE
        String authId = "userid";
        String pwd = "xxx";

        
    Properties props = new Properties();
    InputStream stream = null;
        try {
                
                stream = new CoeusFunctions().getClass().getResourceAsStream("/coeus.properties");
                props.load( stream );
        } finally {
            try {
                stream.close();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
%>
        <%-- <jsp:useBean id="beanInstanceName" scope="session" class="beanPackage.BeanClassName" /> --%>
        <%-- <jsp:getProperty name="beanInstanceName"  property="propertyName" /> --%>
        <table width='100%'>
        <th>System Properties</th>
        <tr><td>Java Version : </td><td><%=System.getProperty("java.version")%></td></tr>
        <tr><td>OS : </td><td><%=System.getProperty("os.name")%></td></tr>
        <th>JVM Memory Info</th>
        <% long memUsed = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory(); %>
        <tr><td>Free memory : </td><td><%=Runtime.getRuntime().freeMemory()%></td></tr>
        <tr><td>Total memory : </td><td><%=Runtime.getRuntime().totalMemory()%></td></tr>
        <tr><td>Maximum memory : </td><td><%=Runtime.getRuntime().maxMemory()%></td></tr>
        <tr><td>Memory used : </td><td><%=memUsed%></td></tr>
        
        <th>Coeus Properties</th>
<%
        
         Enumeration en  = props.keys();
         while(en.hasMoreElements()){
             String k = (String)en.nextElement();
             String v = props.getProperty(k);
%>
        <tr><td><%=k%></td><td><%=v%></td></tr>
<%
        }
%>

        <th>Datasource Properties</th>
<%
    try{
            String jndiName = CoeusProperties.getProperty(CoeusPropertyKeys.DS_JNDI_NAME, "jdbc/coeus");            
            String initialContextFactory = CoeusProperties.getProperty(CoeusPropertyKeys.CONTEXT_FACTORY,"");
            String contextURL = CoeusProperties.getProperty(CoeusPropertyKeys.CONTEXT_URL,"");
            Properties p = new Properties();
            Context envContext;
            //For other App Servers use the respective Context Factory for JNDI look up
            if(!initialContextFactory.equals("") && !contextURL.equals("")){
                p.put("java.naming.factory.initial", initialContextFactory);
                p.put("java.naming.provider.url", contextURL);
                envContext = new InitialContext(p);
            }else{
                //For tomcat no need to use Context Factory for JNDI look up
                Context initContext = new InitialContext();
                envContext  = (Context)initContext.lookup("java:cocdmp/env");
            }
            DataSource ds = (DataSource)envContext.lookup(jndiName); 
            if(ds!=null){
                    Method[] meths = ds.getClass().getMethods();
                    for(int i=0;i<meths.length;i++){
                        //out.println(meths[i].getName());
                        String methodName = meths[i].getName();
                        if(!methodName.startsWith("get") ||
                        		methodName.indexOf("getConnection") != -1 ||
                        		methodName.indexOf("Password") != -1) continue;
                        Object obj=null;
                        try{
                            obj = meths[i].invoke(ds,null);
                        }catch(Exception ex){
                            continue;
                        }
%>
        <tr><td><%=meths[i].getName()%></td><td><%=obj==null?"":obj.toString()%></td></tr>
<%
                    }
        }

        Connection con =null;
        try{
            con = ds.getConnection();
            DatabaseMetaData dbMetaData = con.getMetaData();
            String dbURL = dbMetaData.getURL();
            if(con!=null){
%>
            <tr><td>Connect to database <%=dbURL%></td><td>OK</td></tr>
<%
            }
        }finally{
            try{
                if(con!=null) con.close();
            }catch(SQLException ex){
                ex.printStackTrace();
            }
        }
        }catch(Exception ex){
            ex.printStackTrace();
        }
%>
        <th>S2S Properties</th>

<%
            String path= application.getRealPath("/").replace( '\\', '/');
            String soapServerPropertyFile =path+"/WEB-INF/soap_server.properties";
            SoapUtils.setSoapServerPropFile( soapServerPropertyFile );
%>
        <tr><td>Keystore file</td><td><%=gov.grants.apply.soap.util.SoapUtils.getProperty("javax.net.ssl.keyStore")%></td></tr>
        <tr><td>Truststore file</td><td><%=gov.grants.apply.soap.util.SoapUtils.getProperty("javax.net.ssl.trustStore")%></td></tr>
        <tr><td colspan='2'>Trying to load Keystore and Truststore</td></tr>
<%
            try{
            KeyStore ks = KeyStore.getInstance("JKS");
            FileInputStream fis = new FileInputStream(gov.grants.apply.soap.util.SoapUtils.getProperty("javax.net.ssl.keyStore"));
            if(fis!=null && fis.available()!=-1) out.println("<tr><td colspan='2'>Keystore File is readable</td></tr>");
            String keyPass = gov.grants.apply.soap.util.SoapUtils.getProperty("javax.net.ssl.keyStorePassword");
            ks.load(fis, keyPass.toCharArray());
            KeyStore ts = KeyStore.getInstance("JKS");
            FileInputStream tfis = new FileInputStream(gov.grants.apply.soap.util.SoapUtils.getProperty("javax.net.ssl.trustStore"));
            if(tfis!=null && tfis.available()!=-1) out.println("<tr><td colspan='2'>Truststore File is readable</td></tr>");
            String tsPass = gov.grants.apply.soap.util.SoapUtils.getProperty("javax.net.ssl.trustStorePassword");
            ts.load(tfis, tsPass.toCharArray());
            KeyManagerFactory kmf = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
            kmf.init(ks, keyPass.toCharArray());
            TrustManagerFactory tmf = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
            tmf.init(ts);
            
%>
        <tr><td colspan='2'>Keystore and Truststore successfully loaded</td></tr>
        <tr><td>Number of private keys in Keystore file</td><td><%=ks.size()%></td></tr>
        <tr><td>Number of public certs in Truststore file</td><td><%=ts.size()%></td></tr>
        <tr><td colspan='2'>Trying to create SSLContext</td></tr>
<%
            SSLContext sslContext = SSLContext.getInstance("SSL");
            sslContext.init(kmf.getKeyManagers(), tmf.getTrustManagers(), null);
            out.print("<tr><td colspan='2'>SSLContext initialized</td></tr>");
            }catch(Exception ex){
                out.print("<tr><td colspan='2'>"+ex.getMessage()+"</td></tr>");
            }
%>
        <th>Mail Properties</th>

<%
		Properties mailprops = new Properties();
		mailprops.put(MailProperties.getProperty(MailPropertyKeys.CMS_MAIL_PROTOCOL_KEY), 
			            MailProperties.getProperty(MailPropertyKeys.CMS_MAIL_PROTOCOL,"smtps"));
		mailprops.put(MailProperties.getProperty(MailPropertyKeys.CMS_MAIL_HOST_KEY),
			            MailProperties.getProperty(MailPropertyKeys.CMS_MAIL_HOST));
		mailprops.put(MailProperties.getProperty(MailPropertyKeys.CMS_MAIL_PORT_KEY),
			            MailProperties.getProperty(MailPropertyKeys.CMS_MAIL_PORT));
		mailprops.put(MailProperties.getProperty(MailPropertyKeys.CMS_MAIL_AUTH_KEY),
			            MailProperties.getProperty(MailPropertyKeys.CMS_MAIL_AUTH,"true"));
		final String userId = MailProperties.getProperty(MailPropertyKeys.CMS_MAIL_USER_ID);
		final String password = MailProperties.getProperty(MailPropertyKeys.CMS_MAIL_PASSWORD);
		mailprops.put(MailPropertyKeys.CMS_MAIL_USER_ID,userId);
        
		mailprops.put(MailPropertyKeys.CMS_ENABLED,MailProperties.getProperty(MailPropertyKeys.CMS_ENABLED,"1"));
		mailprops.put(MailPropertyKeys.CMS_MODE,MailProperties.getProperty(MailPropertyKeys.CMS_MODE,"1"));
		mailprops.put(MailPropertyKeys.CMS_TEST_MAIL_RECEIVE_ID,MailProperties.getProperty(MailPropertyKeys.CMS_TEST_MAIL_RECEIVE_ID,"1"));
		mailprops.put(MailPropertyKeys.CMS_SENDER_ID,MailProperties.getProperty(MailPropertyKeys.CMS_SENDER_ID,"1"));
		
		
		Enumeration mailen  = mailprops.keys();
        while(mailen.hasMoreElements()){
             String k = (String)mailen.nextElement();
             String v = mailprops.getProperty(k);
%>
        <tr><td><%=k%></td><td><%=v%></td></tr>
<%
        }
        String toAddress = request.getParameter("txtMailTo");
        boolean dontsend = false;
        if(toAddress!=null && toAddress.trim().length()>0){
        	CoeusMailService cms = new CoeusMailService();
        	SetMailAttributes mailProp = new SetMailAttributes();
        	mailProp.setTo(toAddress);
        	mailProp.setMessage("Test Message from Coeus Configuration tool");
        	cms.sendMessage(mailProp);

%>
            <tr><td colspan="2">Message sent</td></tr>
<%
	        }

%>            
        <form method="post" action="ValidateConfiguration.jsp">
	        <tr><td>To address:</td><td><input name="txtMailTo" type="text" size="50"></td></tr>
	        <tr><td colspan="2"><input type="submit" text="Send mail" name="Send mail"/></td></tr>
        </form>
        
        <th>Authentication Properties</th>
        <tr><td>Total memory</td><td><%=Runtime.getRuntime().totalMemory()%></td></tr>
        <tr><td>Free memory</td><td><%=Runtime.getRuntime().freeMemory()%></td></tr>
        <tr><td>Max memory</td><td><%=Runtime.getRuntime().maxMemory()%></td></tr>
        <th>Library versions</th>
        <tr><td>Xalan version</td><td><%=org.apache.xpath.compiler.FunctionTable.class.getProtectionDomain()%></td></tr>
        </table>
    </body>
</html>
