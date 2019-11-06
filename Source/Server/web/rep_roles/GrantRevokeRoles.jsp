<%--
    Document   : GrantRevokeRoles
    Created on : May 12, 2010, 10:46:05 AM
    Author     : mlotfi
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="edu.mit.coeus.utils.UtilFactory, edu.mit.coeus.utils.dbengine.*, java.sql.*, java.util.*"%>

<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <link rel=stylesheet href="<%=request.getContextPath()%>/coeuslite/mit/utils/css/coeus_styles.css" type="text/css">
        <title>JSP Page</title>
        <script>
            function selectComment(arrCommentId){
                document.reviewComments.check1.checked = true;
            }
        </script>
    </head>
    <body>
        <form name="reviewComments" action="<%=request.getContextPath()%>/rep_roles/SaveRolesForUser.jsp">

                                    <table width="100%" border="0" cellpadding="3" cellspacing="0" class="tabtable">
                                        <tr class="theader">
                                            <td colspan="3" align="center">Select roles to add </td>
                                        </tr>
                                        <%
                                        DBEngineImpl dbEngine = new DBEngineImpl();
                                        String userId2 = (String)session.getAttribute("userId2");

                                                        try {
                                                             Vector param = new Vector();
                                                            Vector result = null;
                                                            Vector result2 = null;
                                                             String selectQuery = "";
                                                             String  selectQueryRolesForUser = "";
                                                            int listSize = 0;
                                                            int listSize2 = 0;
                               selectQuery = "SELECT ROLE_NAME from OSP$REPORT_ROLES_LIST";

                               param.addElement(new Parameter("AW_USER_ID", DBEngineConstants.TYPE_STRING, userId2.toUpperCase()));
                               selectQueryRolesForUser = "SELECT GRANTED_ROLE from dba_role_privs WHERE upper(grantee) = <<AW_USER_ID>> and GRANTED_ROLE in (select ROLE_NAME from OSP$REPORT_ROLES_LIST)";

                                                            if (dbEngine != null) {
                                                                result = new Vector();
                                                                result2 = new Vector();

                                                                result = dbEngine.executeRequest("Coeus", selectQuery, "Coeus",null);

                                                                result2 = dbEngine.executeRequest("Coeus", selectQueryRolesForUser, "Coeus",param);

                                                                if (result != null && result2 != null) {
                                                                    listSize = result.size();
                                                                    listSize2 = result2.size();
                                                                }
                                                            }
                                                            if (listSize > 0 ) {
                                                                for (int index = 0; index < listSize; index++) {
                                                                    boolean roleAlreadyAssigned = false;
                                                                    HashMap map = (HashMap) result.get(index);

                                                                   for (int index2 = 0; index2 < listSize2; index2++) {
                                                                       HashMap map2 = (HashMap) result2.get(index2);
                                                                       if((map.get("ROLE_NAME")).equals(map2.get("GRANTED_ROLE")))
                                                                         {
                                                                          roleAlreadyAssigned = true;
                                                                          break;
                                                                         }
                                                                   }

                                        %>
                                        <tr class="rowLine">
                                          <%if(!roleAlreadyAssigned) {%>

                                           <td width="10%">
                         <input value="<%=map.get("ROLE_NAME")%>" type="checkbox"  name="rolesToAdd"/>
                                          </td>
                                          <td width="90%"><%=map.get("ROLE_NAME")%></td>
                                            <% }     %>




                                        </tr>
                                        <%
                                                }//End For
                                                }//End If


                                            }catch(Exception ex){
                                                out.print(ex.getMessage());
                                                UtilFactory.log(ex.getMessage(), ex, "GrantRevokeRoles.jsp", "");
                                            }
                                        %>
                                        <tr>
                                         <td align="left" width="17%">
                  <!--        <input type="submit" style="width:100"  value="Add Roles">    -->
                                        </td>
                                          </tr>
                                    </table>

        </form>
    </body>
</html>
