<%@page contentType="text/html"%>
<%@page pageEncoding="UTF-8"%>
<%@ page import="java.util.List, edu.mit.coeus.s2s.bean.FormInfoBean, edu.mit.coeus.s2s.validator.S2SValidationException"%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
"http://www.w3.org/TR/html4/loose.dtd">

<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>S2S Validation Error Page</title>
        <link rel=stylesheet href="coeuslite/mit/utils/css/coeus_styles.css" type="text/css">  
    </head>
    <body>
        
        <!-- validation Messages - START -->
        <%
        Exception ex = (Exception)request.getAttribute("Exception");
        if(ex != null) {%>
                <br>
                <table align="center" width="98%" border="0" cellspacing="0" cellpadding="0">
                    <tr><td>
                            <table width="100%" border="0" cellspacing="0" cellpadding="2" class="theader">
                                <tr><td>
                                        Error:
                                    </td>
                                </tr>
                            </table>
                    </td></tr>
                    
                    <tr><td>
                            <div id="errorDetails" style="overflow:hidden">
                                <table align="center" width="100%" border="0" cellspacing="0" cellpadding="0" class="tabtable">
                                    
                                    <tr><td class="copy">
                                            <table width="100%" border="0"> 
                                                <tr><td>
                                                        <img src="coeusliteimages/error.gif" hspace="5" vspace="5">
                                                    </td>
                                                    <td class="copybold">
                                                        <font color="red">
                                                            <%
                                                            String exceptionMessage = ex.getMessage();
                                                            //if message has a link. display link in next line
                                                            int index = exceptionMessage.indexOf("<a");
                                                            if(index != -1){
                                                                exceptionMessage = exceptionMessage.substring(0, index) +"<br>"+exceptionMessage.substring(index, exceptionMessage.length());
                                                            }
                                                            out.print(exceptionMessage);
                                                        %></font>
                                                </td></tr>
                                            </table>
                                    </td></tr>
                                    <tr><td>
                                            <table align="center" width="98%" border="0" cellspacing="0" cellpadding="2" class="tabtable">
                                                <%
                                                if(ex instanceof S2SValidationException) {%>
                                                <tr><td><font color="red"><b>Please Correct the Following Errors:</b></font></td></tr>
                                                <%
                                                S2SValidationException s2SValidationException = (S2SValidationException)ex;
                                                List errList = s2SValidationException.getErrors();
                                                S2SValidationException.ErrorBean errorBean;
                                                FormInfoBean formInfoBean;
                                                for(index = 0; index < errList.size(); index++) {
                                                errorBean = (S2SValidationException.ErrorBean)errList.get(index);
                                                if(errorBean.getMsgObj() instanceof FormInfoBean) {
                                                formInfoBean = (FormInfoBean)errorBean.getMsgObj();
                                                %><tr class="rowLine table"><td><b><%=formInfoBean.getFormName()%></b></td></tr>
                                                <%
                                                }else {%>
                                                <tr class="rowLine"><td><%=errorBean.getMsgObj()%></td></tr>
                                                <%    }
                                                   }
                                                }
                                                %>
                                    </table> <br>  </td></tr>
                                </table>
                            </div>
                    </td></tr>
        </table> 
        <%}%>
        
    </body>
</html>
