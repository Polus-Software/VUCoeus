<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%@ page contentType="text/html;charset=UTF-8" language="java" isErrorPage="true"%>

<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ include file= "/coeuslite/mit/utils/CoeusContextPath.jsp"  %>
<html:html>
    <head>
    <html:base/>
    <link href="<bean:write name='ctxtPath'/>/coeuslite/mit/utils/css/coeus_styles.css" rel="stylesheet" type="text/css">
    <script language="javascript">
	
    </script>
    <meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
    <title>
        An Error has Occurred!!
    </title>
</head>
<body>
    <table width="70%" height="100%" align="center"  class='table' border="0" cellpadding="0">
        <tr>
            <td>
                <table width="100%"  cellpadding="0" cellspacing="0" 
                       STYLE="background-image:url('<bean:write name='ctxtPath'/>/coeusliteimages/header_background.gif');border:0">
                    <tr>
                        <td>
                            <img src="<bean:write name='ctxtPath'/>/coeusliteimages/header_coeus1.gif" width="675" height="50">
                        </td>
                    </tr>
                </table>
            </td>
        </tr>
        <tr>
            <td class="tabtable" height='100%'>
                <br>
                <br>       
        <logic:present name="unAutherisedUser">
            <div style="text-align: center;color:navy;font-size: 15px">   You do not have right to modify/view proposal.
        </logic:present>
        <br>
        <br>
        <br>
        <br>

        </td>
        </tr>


        <tbody id="details">

        </tbody>   

        </tr>
    </table>
    <table bgcolor='#376DAA' align="center" width="70%" height="100%" border="0" cellpadding="0">
        <tr>
            <td align="left">
                <font color="white" size="-1">
                    <bean:message  key="label.copywriteMIT"/>
                </font>
            </td>
            <td align="right">
                <font color="white" size="-1">
                    <bean:message  key="label.copywirteCoeus"/>
                </font>
            </td>
        </tr>
    </table>

</body>
</html:html>