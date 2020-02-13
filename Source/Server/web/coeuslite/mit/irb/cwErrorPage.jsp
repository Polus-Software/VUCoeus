<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%@ page contentType="text/html;charset=UTF-8" language="java"
	isErrorPage="true"%>

<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ include file="/coeuslite/mit/utils/CoeusContextPath.jsp"%>
<%@page import="edu.mit.coeus.utils.UtilFactory"%>

<%
  System.out.println("****************************************");
  System.out.println("***   In cwErrorPage.jsp             ***");
  System.out.println("****************************************");
%>
<html:html>

<head>
<html:base />
<link
	href="<bean:write name='ctxtPath'/>/coeuslite/mit/utils/css/coeus_styles.css"
	rel="stylesheet" type="text/css">
<script language="javascript">
	
</script>

<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">

<title>An Error has Occurred!!</title>

<!--<link href="css/ipf.css" rel="stylesheet" type="text/css">
        <style type="text/css">
        </style>-->


<SCRIPT language="JavaScript">
<!--
//
// Browser Detection
//
NS4 = (document.layers) ? true : false;

if (NS4) {
	location.href = 'index_noncomp_browser.cfm';
	}

//-->

var errorDetail;

function showHideDetails() {
    detailsBody = document.getElementById("details");
    link = document.getElementById("detailsLink");
    
    children = detailsBody.childNodes;
    linkChildren = link.childNodes;
    link.removeChild(linkChildren[0]);
    
    if(children.length == 2) {
        //Already Expanded. Hide this time.
        detailsBody.removeChild(children[1]);
        
        linkText = document.createTextNode("Show Details >>");
        link.appendChild(linkText);
        return;
    }
    
    row = document.createElement("TR");
    data = document.createElement("TD");
    data.setAttribute("class", "tablemainsub");
    text = document.createTextNode(errorDetail)
    data.appendChild(text);
    row.appendChild(data);
    
    detailsBody.appendChild(row);
    
    linkText = document.createTextNode("Hide Details <<");
    link.appendChild(linkText);
}
</SCRIPT>

</head>

<body>


	<table width="70%" height="100%" align="center" class='table'
		border="0" cellpadding="0">
		<tr>
			<td>
				<!-- JM 5-31-2011 updated per 4.4.2 -->
				<table width="100%" cellpadding="0" cellspacing="0"
					STYLE="background-color: black; border: 0">
					<tr>
						<td><img
							src="<bean:write name='ctxtPath'/>/coeusliteimages/header_coeus1.gif"
							width="675" height="50"></td>
					</tr>
				</table>
			</td>
		</tr>

		<tr>
			<td class="tabtable" align="left"><b><bean:message
						key="errorPage.ErrorMessage" /> <%--An Error has Occured:--%> </b></td>
		</tr>



		<tr>
			<td class="tabtable" height='100%'>
				<%
            String message = "No Message.";
            Exception customException = (Exception)request.getAttribute("Exception");

            if(customException != null) {
                if (customException.getMessage()!=null)
                  message = customException.getMessage();
                //setting Error Detail(Print Stack trace)
                String errorDetails = customException.toString()+" ";
                StackTraceElement stackTraceElement[] = customException.getStackTrace();
                for(int index = 0; index < stackTraceElement.length; index++) {
                    errorDetails = errorDetails + stackTraceElement[index].toString();
                }

                UtilFactory.log("FROM COEUS LITE : "+errorDetails, "irb/cwErrorPage.jsp");
                %> <script>errorDetail = '<%=errorDetails%>'</script>
				<%
            }else if(exception != null) {
                message = exception.getMessage();
                //setting Error Detail(Print Stack trace)
                String errorDetails = customException.toString()+" ";
                StackTraceElement stackTraceElement[] = customException.getStackTrace();
                for(int index = 0; index < stackTraceElement.length; index++) {
                    errorDetails = errorDetails + stackTraceElement[index].toString();
                }
                UtilFactory.log("FROM COEUS LITE : "+errorDetails, "irb/cwErrorPage.jsp");
                %> <script>errorDetail = '<%=errorDetails%>'</script>
				<%
            }
                out.write(message);
            %> <br> <br>
			</td>
		</tr>


		<tbody id="details">
			<tr>
				<td class="tablemain" align=right><a id="detailsLink"
					href='javascript:showHideDetails()'><bean:message
							key="errorPage.Link" /> <%--Details >>--%> </a></td>
			</tr>
		</tbody>

		</tr>
	</table>
	<!-- JM 5-31-2011 updated per 4.4.2 -->
	<table bgcolor='#000000' align="center" width="70%" height="100%"
		border="0" cellpadding="0">
		<tr>
			<td align="left"><font color="white" size="-1"> <bean:message
						key="label.copywriteMIT" />
			</font></td>
			<td align="right"><font color="white" size="-1"> <bean:message
						key="label.copywirteCoeus" />
			</font></td>
		</tr>
	</table>

</body>
</html:html>