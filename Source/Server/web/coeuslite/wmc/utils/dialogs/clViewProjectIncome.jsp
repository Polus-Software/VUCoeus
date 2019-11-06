<%@ page import="edu.mit.coeuslite.utils.CoeusDynaBeansList,
                java.util.ArrayList,java.util.List,
                org.apache.struts.action.DynaActionForm"%>
<%@ taglib uri="/WEB-INF/struts-html.tld"     prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld"     prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld"     prefix="logic" %>
<%@ taglib uri="/WEB-INF/coeus-utils.tld"     prefix="coeusUtils" %>
<%@ include file= "/coeuslite/mit/utils/CoeusContextPath.jsp"  %>
<%
    String indexValue = request.getParameter("value").toString();
    //System.out.println("indexValue >>>>> "+indexValue);
    String value = "";
    CoeusDynaBeansList coeusDynaBeansList = (CoeusDynaBeansList)session.getAttribute("budgetProjectIncomeList");    
    List list = (List)coeusDynaBeansList.getList();
    if(list != null && list.size() > 0){
        DynaActionForm form = (DynaActionForm)list.get(Integer.parseInt(indexValue));
        //System.out.println("form >>>>>> "+form);
        value = (String)form.get("description");
        if(value == null){
            value = "";
        }        
    }
    //System.out.println("value >>>>>> "+value);
%>
<%
   String mode = (String)session.getAttribute("mode"+session.getId());
   String budgetStatusMode = (String)session.getAttribute("budgetStatusMode");
   boolean readOnly = false;
   if(mode!= null && mode.equals("display")){
       readOnly = true;
   }else if(budgetStatusMode != null && budgetStatusMode.equals("complete")){
        readOnly = true;
   }
%>
<%--script>
alert(value);
</script--%>
<html>
<head>
<title>Comments</title>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
<link href="<bean:write name='ctxtPath'/>/coeuslite/mit/utils/css/ipf.css" rel="stylesheet" type="text/css">
<link href="<bean:write name='ctxtPath'/>/coeuslite/mit/utils/css/coeus_styles.css" rel="stylesheet" type="text/css">
<script>
    function fetchData(){
        opener.fetch_Data(document.test.txtDesc.value);
        window.close();
    }
</script>
</head>
<body>
<form name="test">
<table WIDTH='100%' border="0" align="center" cellpadding="0" cellspacing="0" class="tabtable" >    
    <tr bgcolor='#E1E1E1'>
        <td height="15%" align="left" valign=bottom>
            <table height="40%" border="0" cellpadding="0" cellspacing="0" class="tableheader">
                <tr>
                  <td align=left class='copybold'><b> <bean:message key="specialReviewLabel.Comments"/></b> </td>
                </tr>
            </table>
        </td>
    </tr>
    <tr >
     <td>
        <html:textarea property="txtDesc" disabled="<%=readOnly%>" styleClass="copy" cols="100" rows="10" value="<%=value%>"  />
     </td>
   </tr>
   <tr>
    <td align='center'>
        <html:button property="Close" value="Close" styleClass="clbutton" onclick="fetchData();"/>
    </td>
   </tr>
</table>
</form>
</body>
</html>