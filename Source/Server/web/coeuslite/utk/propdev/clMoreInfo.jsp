

<%@ taglib uri="/WEB-INF/struts-html.tld"     prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld"     prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld"     prefix="logic" %>
<%@ taglib uri="/WEB-INF/coeus-utils.tld"     prefix="coeusUtils" %>
<%@ include file= "/coeuslite/mit/utils/CoeusContextPath.jsp"  %>

<html:html>
<head>
<title>Comments</title>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
<link href="<bean:write name='ctxtPath'/>/coeuslite/mit/utils/css/ipf.css" rel="stylesheet" type="text/css">
<link href="<bean:write name='ctxtPath'/>/coeuslite/mit/utils/css/coeus_styles.css" rel="stylesheet" type="text/css">
<script>
function moreInfo(){
 alert("Functionality is not implemented"); 
}
</script>
</head>
<body>
<table  WIDTH='98%' border="0" align="center" cellpadding="0" cellspacing="0" class="tabtable" >    
    <tr bgcolor='#E1E1E1'>
        <td height="20%" align="left" valign=bottom>
            
            <table height="90%"  border="0" cellpadding="0" cellspacing="0" class="tableheader">
                <tr>
                  <td align=left class='copybold'><b> <bean:message bundle="proposal" key="proposalInvKeyPersons.investigatorDetails"/></b> </td>
                </tr>
            </table>
        </td>
    </tr>
    <tr> <td>  <a href='javascript:moreInfo()'> <bean:message bundle="proposal" key="proposalInvKeyPersons.creditSplit"/>  </a> </td> </tr> 
    <tr> <td>  <a href='javascript:moreInfo()'> <bean:message bundle="proposal" key="proposalInvKeyPersons.personDetails"/>  </a> </td> </tr> 
    <tr>  <td>   <a href='javascript:moreInfo()'> <bean:message bundle="proposal" key="proposalInvKeyPersons.unitDetails"/>  </a> </td> </tr> 
    <tr>   <td>  <a href='javascript:moreInfo()'> <bean:message bundle="proposal" key="proposalInvKeyPersons.degreeInfo"/>  </a> </td> </tr> 
   
   <tr>
    <td align=right>
        <html:button property="Close" value="Close" onclick="window.close();" styleClass="clbutton"/>
    </td>
   </tr>
</table>

</body>
</html:html>

