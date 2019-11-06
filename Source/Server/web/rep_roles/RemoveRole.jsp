<%-- 
    Document   : RemoveRole
    Created on : May 26, 2010, 11:16:34 AM
    Author     : mlotfi
--%>


<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="edu.mit.coeus.utils.UtilFactory, edu.mit.coeus.utils.dbengine.*, edu.mit.coeus.bean.PersonInfoBean, java.util.*"%>
<%@page import="edu.mit.coeuslite.utils.SessionConstants, edu.mit.coeus.utils.CoeusFunctions"%>

<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Roles Save</title>
    </head>
    <body>
<%
               DBEngineImpl dbEngine = new DBEngineImpl();
            try {

                 String DeleteRoleQuery = "";

                    String  grantedRole = request.getParameter("grantedRole");
                    String userId = request.getParameter("userId");


                    DeleteRoleQuery = "REVOKE " + grantedRole + " FROM " + userId.toUpperCase();
                    dbEngine.executeRequest("Coeus", DeleteRoleQuery, "Coeus",null);


             response.sendRedirect(request.getContextPath() + "/rep_roles/UserRoles.jsp?userId="+userId.trim() );
                } catch (Exception ex) {
                out.print(ex.getMessage());
                UtilFactory.log(ex.getMessage(), ex, "RemoveRole.jsp", "");
            }


%>
  </body>
</html>