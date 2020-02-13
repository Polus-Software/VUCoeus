<%-- 
    Document   : displayCount
    Created on : Dec 24, 2010, 2:46:03 PM
    Author     : vineetha
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/privilege.tld" prefix="priv" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<jsp:useBean  id="awardList" scope="request" class="java.util.Vector"/>
<jsp:useBean  id="awardColumnNames" scope="session" class="java.util.Vector"/>
<bean:size id="awardListSize" name="awardList"/>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
   "http://www.w3.org/TR/html4/loose.dtd">
<%@page import="java.util.Vector,edu.mit.coeuslite.coiv2.beans.CoiDisclosureBean,
        edu.mit.coeuslite.coiv2.services.CoiCommonService,java.util.Date,edu.mit.coeuslite.utils.bean.SubHeaderBean,
        edu.mit.coeus.bean.PersonInfoBean,edu.mit.coeus.bean.UserDetailsBean"%>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
        <title>Award</title>
        <link href="<%=request.getContextPath()%>/coeuslite/mit/coiv2/css/layout.css" rel="stylesheet" type="text/css" />
        <link href="<%=request.getContextPath()%>/coeuslite/mit/coiv2/css/styles.css" rel="stylesheet" type="text/css" />
        <link href="<%=request.getContextPath()%>/coeuslite/mit/coiv2/css/collapsemenu.css" rel="stylesheet" type="text/css" />
        <link href="<%=request.getContextPath()%>/coeuslite/dartmouth/utils/css/coeus_styles.css" rel="stylesheet" type="text/css" />


        <%
           Vector data = (Vector)request.getAttribute("awardList");
           int size = data.size();
           Vector data1=  (Vector)request.getAttribute("childHierarchy");
            if(data1!=null){size =size+ data1.size();}
            String ServerName=request.getContextPath();
            ServerName=ServerName+"/coeuslite/mit/award";
        %>
    </head>
    <body>
        <table id="bodyTable1" border="0" width="100%" height="20px" class="tabtable">
           <logic:present name="awardList">
            <tr height:10px" >
               <td style="width:75%; text-align: left;">
                    <h1 style="float:left;font-size:14px;font-weight:bold;margin: 0;padding: 2px 0 2px 10px;position: relative;text-align: left;">&nbsp;No. of Records :  <%=size%></h1>
                </td>
                   <%
                    if(size > 0) {
                   %> 
                 <td style="width: 25%; text-align: right;"><a href="<%=ServerName%>/jsp/AwardListPrint.jsp">Export to Excel</a>&nbsp;&nbsp;&nbsp;&nbsp;
                 </td>
                 <%}%>

              </tr>
  
           
        </logic:present>
        </table>
    </body>
</html>  