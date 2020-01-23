<%@ page import="edu.mit.coeuslite.utils.CoeusDynaBeansList,
                java.util.List, java.lang.Double,org.apache.struts.action.DynaActionForm"%>
<%@ taglib uri="/WEB-INF/struts-html.tld"     prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld"     prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld"     prefix="logic" %>
<%@ taglib uri="/WEB-INF/coeus-utils.tld"     prefix="coeusUtils" %>
<jsp:useBean id="subAwardDetailList" scope="request" class="edu.mit.coeuslite.utils.CoeusDynaBeansList" />
<%@ include file= "/coeuslite/mit/utils/CoeusContextPath.jsp"  %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
   "http://www.w3.org/TR/html4/loose.dtd">
   
<html>
    <head>
        <title>Sub Award Details</title>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <link href="<bean:write name='ctxtPath'/>/coeuslite/mit/utils/css/ipf.css" rel="stylesheet" type="text/css">
        <link href="<bean:write name='ctxtPath'/>/coeuslite/mit/utils/css/coeus_styles.css" rel="stylesheet" type="text/css">
    </head>
    <body>
        <script>
            function syncCost(buttonClick){
                
               if(buttonClick == 'yes'){
               
                document.formulatedLineItemDetails.action = "<%=request.getContextPath()%>/syncCalculatedLineItemCosts.do?&canSync=YES";
                document.formulatedLineItemDetails.submit();
                
               }else{
               
                document.formulatedLineItemDetails.action = "<%=request.getContextPath()%>/syncCalculatedLineItemCosts.do?&canSync=NO";
                document.formulatedLineItemDetails.submit();
               
               }
            }
            
        </script>
        <html:form action="/syncCalculatedLineItemCosts.do" method="post"> 
            <table width="100%" height="100%"  border="0" cellpadding="0" cellspacing="0" class="table">
                <tr class="table">
                    <td>            
                        <table width="100%" border="0" cellpadding="0" cellspacing="0" class='tableheader'>
                            <tr>
                                <td height="2%" align="left" valign="top" class="theader">Sync Calculated Line Item costs</td>
                            </tr>
                        </table>            
                    </td>
                </tr> 
                <tr> 
                    <td align=center class='savebutton'>
                    
                        Do you want to sync formulated line item costs?
                  
                    </td>
                </tr>
                <tr>
                    <td>
                        <html:button value="Yes" property="yes" styleClass="clsavebutton" onclick="syncCost('yes')"/>
                        &nbsp;&nbsp;<html:button value="No"  property="no" styleClass="clsavebutton" onclick="syncCost('no')"/>
                        </td>
                    </tr>
            </table>
        
      </html:form>
    </body>
</html>
