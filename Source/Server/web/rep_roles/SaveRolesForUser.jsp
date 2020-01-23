<%--
    Document   : SaveRolesForUser
    Created on : May 12, 2010, 9:56:37:03 AM
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



        <%! String[] rolesToAdd; %>
        
<%



               
                String userId2 = (String)session.getAttribute("userId2");
                rolesToAdd = request.getParameterValues("rolesToAdd");

          
               DBEngineImpl dbEngine = new DBEngineImpl();
            try {
                Integer id = null;
                String comment = "";
                int count = 0, index = -1;
                 String DeleteAllRolesQuery = "";

                 Vector param = new Vector();
                 Vector result = null;
                 String selectQuery ="";
                 String GrantRolesQuery = "";
                 String AW_USER_ID = "";
                 int listSize = 0;
                 

                 if (rolesToAdd != null)
            {
               for (int i = 0; i < rolesToAdd.length; i++)
               {
                      GrantRolesQuery = "GRANT " + rolesToAdd[i] + " TO " + userId2.toUpperCase();
                      dbEngine.executeRequest("Coeus", GrantRolesQuery, "Coeus",null);
               }
           }   else out.println ("<b>none<b>");

                 
             response.sendRedirect(request.getContextPath() + "/rep_roles/UserRoles.jsp?userId="+ userId2 );
                } catch (Exception ex) {
                out.print(ex.getMessage());
                UtilFactory.log(ex.getMessage(), ex, "SaveRolesForUser.jsp", "");
            }

                 
              
%>

  </body>
</html>