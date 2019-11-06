<%@page contentType="text/html"%>
<%@page pageEncoding="UTF-8"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ include file= "/coeuslite/mit/utils/CoeusContextPath.jsp"%>

<jsp:useBean id="tbaPersonsData" scope="session" class="java.util.Vector"/>

<%--
The taglib directive below imports the JSTL library. If you uncomment it,
you must also add the JSTL library to the project. The Add Library... action
on Libraries node in Projects view can be used to add the JSTL 1.1 library.
--%>
<%--
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%> 
--%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
   "http://www.w3.org/TR/html4/loose.dtd">

<html>
    <head>
    <title>TBA Search</title>  
        <html:base/>
        <meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">    
        <script language="javascript" type="text/JavaScript">
            function getTBAData(personName, jobCode) {
               // Modified for Case 4145 - Lite budget personnel save problem when adding more rows w/out saving in between 
               // parent.opener.location = "<%=request.getContextPath()%>/AddPersonLineItem.do?tbaFlag=true&personName="+personName+"&jobCode="+jobCode;
               // self.close();       
                window.opener.document.budgetPersonsDynaBean.action = "<%=request.getContextPath()%>/AddPersonLineItem.do?tbaFlag=true&personName="+personName+"&jobCode="+jobCode;
                window.opener.document.budgetPersonsDynaBean.submit();
                window.close();
            }            
        </script>
        <script language="javascript" type="text/JavaScript" src="<bean:write name='ctxtPath'/>/coeuslite/mit/utils/scripts/sorttable.js"></script>
        <link href="../css/ipf.css" rel="stylesheet" type="text/css">
        <style type="text/css">
        </style>
        <link href="<bean:write name='ctxtPath'/>/coeuslite/mit/utils/css/coeus_styles.css" rel="stylesheet" type="text/css">
        
    </head>
    <body>
    <html:form action="/AddPersonLineItem.do" method="post">

    <table width="750" border="0" cellpadding="0" cellspacing="0" class="table">
        <tr>
            <td height="50" align="left" valign="top" class='core'>
                <table width="99%" border="0" align="center" cellpadding="0" cellspacing="0" class="tabtable">        
                    <tr>
                        <td>
                            <table width="100%" height="20"  border="0" cellpadding="0" cellspacing="0" class="tableheader">
                                <tr>
                                    <td>
                                        <bean:message bundle="budget" key="tba.label.persons"/>
                                    </td>
                                </tr>
                            </table>
                        </td>                      
                    </tr>  
                    
                    <tr>
                        <td height='10'>
                            &nbsp;
                        </td>
                    </tr>    
       
                    <tr align="center">
                        <td>
                            <table width="98%" align='center' border="0" cellpadding="3" cellspacing='3' id="t1" class="sortable">                    
                    
                            <tr>
                                <td width="33%" align="left" class="theader">
                                    <bean:message bundle="budget" key="tba.label.id"/>
                                </td>
                                <td width="33%" align="left" class="theader">
                                    <bean:message bundle="budget" key="tba.label.name"/>
                                </td> 
                                <td width="33%" align="left" class="theader">
                                    <bean:message bundle="budget" key="tba.label.jobCode"/>
                                </td>
                            </tr>
                            <%
                                String strBgColor = "#D6DCE5";
                                int index=0;
                            %>
                            <logic:present name="tbaPersonsData">
                            <logic:iterate id="tbaPersons"  name="tbaPersonsData" type="org.apache.commons.beanutils.DynaBean">
                            <%            
                                if (index%2 == 0){
                                    strBgColor = "#D6DCE5"; 
                                }
                                else{ 
                                    strBgColor="#DCE5F1"; 
                                }                        
                                String searchLink = "javascript:getTBAData('" 
                                                    +tbaPersons.get("personName")
                                                    +"','"
                                                    +tbaPersons.get("jobCode")
                                                    +"')";                        
                            %>   
                            <tr bgcolor="<%=strBgColor%>" valign="top" onmouseover="className='TableItemOn'" onmouseout="className='TableItemOff'">      
                                <td class="copy" align='left'>
                                    <html:link href="<%=searchLink%>">
                                        <bean:write name="tbaPersons" property="tbaId"/>
                                    </html:link>    
                                </td> 

                                <td class="copy" align='left'>
                                    <html:link href="<%=searchLink%>">
                                        <bean:write name="tbaPersons" property="personName"/>
                                    </html:link>    
                                </td>
                                <td class="copy" align='left'>
                                    <html:link href="<%=searchLink%>">
                                        <bean:write name="tbaPersons" property="jobCode"/>
                                    </html:link>    
                                </td>                        
                            </tr>   
                            <% 
                                index++;
                            %> 
                            </logic:iterate>
                            </logic:present>
                            
                            <logic:empty name="tbaPersonsData">
                                <tr>
                                    <td class="copy" align='left'>
                                        <bean:message bundle="budget" key="tba.label.sorry"/>                                        
                                    </td>
                                </tr>    
                            </logic:empty>    
                            </table>
                        </td>
                    </tr>                    
                </table>
            </td>
        </tr>
        
        <tr>
            <td height='45' align="left"> &nbsp; &nbsp;            
                <html:link href="javascript:window.close();" styleClass="copybold">
                    <bean:message bundle="budget" key="tba.label.close"/>
                </html:link>
            </td>
        </tr>        
        
    </table>
    </html:form>
    </body>
</html>
