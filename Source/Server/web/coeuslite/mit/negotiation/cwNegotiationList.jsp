
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%@ page contentType="text/html;charset=UTF-8" language="java"%>


<%@page
	import="java.util.HashMap, 
                java.util.ArrayList, 
                java.util.Hashtable, 
                java.util.Iterator, 
                edu.mit.coeuslite.utils.CoeusLiteConstants"%>


<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ include file="/coeuslite/mit/utils/CoeusContextPath.jsp"%>
<jsp:useBean id="negotiationList" scope="request"
	class="java.util.Vector" />
<jsp:useBean id="negotiationColumnNames" scope="request"
	class="java.util.Vector" />

<html:html>
<head>
<title>Coeus Web</title>
<html:base />
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
<link
	href="<bean:write name='ctxtPath'/>/coeuslite/mit/utils/css/coeus_styles.css"
	rel="stylesheet" type="text/css">
<link href="/coeuslite/mit/utils/css/ipf.css" rel="stylesheet"
	type="text/css">
<script language="javascript" type="text/JavaScript"
	src="<bean:write name='ctxtPath'/>/coeuslite/mit/utils/scripts/sorttable.js"></script>
<script>
          var display = false;
    var divIdDisplay = '';

    function showHelp(divId, elementId){
        document.getElementById(divId).style.display="";
        tabId = divId+"Tab";
        height = document.getElementById(tabId).clientHeight;
        width = document.getElementById(tabId).clientWidth;
        divHeight = document.getElementById(divId).clientHeight;//style.height;
        document.getElementById(divId).style.display="none";
        document.getElementById(divId).style.height = height+55;
        document.getElementById(divId+"2").style.height = height+10;

        elem = document.getElementById(elementId);
        var pos = getPosition(elementId);
        //Change Positions if the Help Window Boundaries goes out of Browser Window Boundaries
        browserWidth = getWindowWidth();
        browserHeight = getWindowHeight();
        var helpTop = pos[1], helpLeft = pos[0];
        if((pos[0]+width) > (browserWidth-20)){
            helpLeft = pos[0] - ((pos[0]+width) - browserWidth)-20;
        }
        if((pos[1]+height) > browserHeight){
            helpTop = pos[1] - ((pos[1]+height) - browserHeight);
            //if help is way top. center it
            if((helpTop+height) < pos[1]) helpTop = pos[1] - (height/2);
        }
        document.getElementById(divId).style.top = helpTop;
        document.getElementById(divId).style.left = helpLeft;
        showMe(divId);
    }

    function showMe(divId){
        if(divIdDisplay != '') {
            document.getElementById(divIdDisplay).style.display="none";
        }
        display = true;
        document.getElementById(divId).style.display="";
        divIdDisplay = divId;
    }

    function hideHelp(divId){
        display = false;
        setTimeout("hideMe('"+divId+"')", 250);
        //hideMe(divId);
    }
    
    function hideMe(divId){
        if(!display) {
            document.getElementById(divId).style.display="none";
        }
    }

    function getWindowWidth()
    {
        var x = 0;
        if (self.innerHeight)
        {
            x = self.innerWidth;
        }
        else if (document.documentElement && document.documentElement.clientHeight)
        {
            x = document.documentElement.clientWidth;
        }
        else if (document.body)
        {
            x = document.body.clientWidth;
        }
        return x;
    }

    function getWindowHeight()
    {
        var y = 0;
        if (self.innerHeight)
        {
            y = self.innerHeight;
        }
        else if (document.documentElement && document.documentElement.clientHeight)
        {
            y = document.documentElement.clientHeight;
        }
        else if (document.body)
        {
            y = document.body.clientHeight;
        }
        return y;
    }
    </script>
</head>
<%String help_image = request.getContextPath()+"/coeusliteimages/icon_help.gif";
%>
<body>
	<html:messages id="message" message="true">


	</html:messages>
<html>

<table width="970" height="100%" border="0" cellpadding="0"
	cellspacing="0" class="table">

	<tr>
		<td height="653" align="left" valign="top"><table width="99%"
				border="0" align="center" cellpadding="0" cellspacing="0"
				class="tabtable">
				<tr>
					<td colspan="4" align="left" valign="top"><table width="100%"
							height="20" border="0" cellpadding="0" cellspacing="0"
							class="tableheader">
							<tr>
								<td>List of Negotiations</td>
							</tr>
						</table></td>
				</tr>
				<tr>
					<td>&nbsp;</td>
				</tr>
				<tr align="center">
					<td>
						<table width="98%" height="100%" border="0" cellpadding="2"
							cellspacing="0" id="t1" class="sortable">
							<tr>

								<%
                                     if(negotiationColumnNames != null && negotiationColumnNames.size()>0){
                                         for(int index=0;index<negotiationColumnNames.size();index++){
                                             edu.mit.coeus.search.bean.DisplayBean displayBean = (edu.mit.coeus.search.bean.DisplayBean)negotiationColumnNames.elementAt(index);
                                             if(displayBean.isVisible()){
                                                 String strColumnName = displayBean.getValue();
                                                 %>
								<td class="theader"><%=strColumnName%></td>
								<%
                                             }
                                        }
                                     }
                                 %>

							</tr>
							<%  
                                        String strBgColor = "#DCE5F1";
                                        String negotiationNumber="";
                                        String leadUnitNumber = "";
                                        int count = 0;
                                       
                                 %>
							<logic:present name="negotiationList">
								<logic:iterate id="negotiation" name="negotiationList"
									type="java.util.HashMap">
									<%     
                                        if (count%2 == 0) 
                                            strBgColor = "#D6DCE5"; 
                                        else 
                                            strBgColor="#DCE5F1"; 
                                 %>
									<tr bgcolor="<%=strBgColor%>" valign="top"
										onmouseover="className='TableItemOn'"
										onmouseout="className='TableItemOff'">
										<%
                                    String destPage = CoeusLiteConstants.GENERAL_INFO_PAGE;
                                    if(negotiationColumnNames != null && negotiationColumnNames.size()>0){                                       
                                        for(int index=0;index<negotiationColumnNames.size();index++){
                                            String statusFlag  = "NO";   
                                            int lenCompare = 30;
                                            edu.mit.coeus.search.bean.DisplayBean displayBean = (edu.mit.coeus.search.bean.DisplayBean)negotiationColumnNames.elementAt(index);
                                            if(!displayBean.isVisible())
                                                continue;
                                            String key = displayBean.getName();
                                            if(key != null){
                                                String value = negotiation.get(key) == null ? "" : negotiation.get(key).toString();
                                                if("TITLE".equals(key)){
                                                    lenCompare = 20;
                                                     statusFlag = "YES";
                                                }
                                                if(index == 0){
                                                    negotiationNumber = value;
                                                }                                               
                                                if("SPONSOR".equals(key)){
                                                    lenCompare=30;
                                                }   
                                                if("FULL_NAME".equals(key) || "PI_NAME".equals(key)){
                                                    lenCompare = 20;
                                                    statusFlag = "YES";
                                                }
                                                if(value != null && value.length() > lenCompare){
                                                                    value = value.substring(0,lenCompare-5);
                                                                    value = value+" ...";
                                                }
                                                leadUnitNumber = negotiation.get("LEAD_UNIT_NUMBER") == null ? "" : negotiation.get("LEAD_UNIT_NUMBER").toString();
                                    
                                    %>
										<td align="left" nowrap class="copy"><a
											href="<%=request.getContextPath()%>/viewNegotiation.do?NEGOTIATION_NUMBER=<%=negotiationNumber.trim()%>&LEAD_UNIT_NUMBER=<%=leadUnitNumber.trim()%>"><u>
													<%=value%>
											</u> </a> <%-- <% if(statusFlag.equalsIgnoreCase("YES")){ %>   
                                         <html:link href="javaScript:showHelp('title','titleId');" styleId="titleId"><html:img src="<%=help_image%>" border="0"/></html:link>
                                         <% } %>--%></td>
										<%
                                            }
                                        }
                                     }
                                 %>
									</tr>
									<% count++;%>
								</logic:iterate>
							</logic:present>

						</table>
					</td>
				</tr>

				<tr>
					<td>&nbsp;</td>
				</tr>

			</table></td>
	</tr>
	<tr>
		<td height='10'>&nbsp;</td>
	</tr>

</table>

</html>
<%
//Negotiation changes - Popup style is changed and a close button is added to close the popup
String helpArr[] = {"title"};
for(int index=0; index < helpArr.length; index++){%>
<div id="<%=helpArr[index]%>" class="mbox"
	style="position: absolute; width: 450; height: 225; display: none;">
	<table cellpadding='0' cellspacing='0' border="2" align=left
		class='tabtable' width="450">
		<tr class='theader'>
			<td width='100%'>Details</td>
		</tr>
	</table>
	<br />
	<div id="<%=helpArr[index]%>2"
		style="overflow: auto; width: 450; height: 186">
		<table border="2" id="<%=helpArr[index]%>Tab" cellpadding="2"
			cellspacing="2" class="lineBorderWhiteBackgrnd" width="450"
			height="50">
			<tr>
				<td>Value</td>
			</tr>
		</table>
	</div>
	<input type="button"
		onclick="javascript:hideHelp('<%=helpArr[index]%>')" value="Close">
</div>
<%}
%>
</body>
</html:html>
