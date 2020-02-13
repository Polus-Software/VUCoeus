<%@page contentType="text/html"%>
<%@page pageEncoding="UTF-8"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
   "http://www.w3.org/TR/html4/loose.dtd">

<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Opportunity Search</title>
        <script src="<%=request.getContextPath()%>/coeuslite/mit/utils/scripts/divSlide.js"></script>
        <script>
            //JIRA COEUSDEV 61 - START
            var messageId, linkId;

            function GetXmlHttpObject(handler)
            {
                var objXMLHttp=null
                if (window.XMLHttpRequest)
                {
                    objXMLHttp=new XMLHttpRequest()
                }
                else if (window.ActiveXObject)
                {
                    objXMLHttp=new ActiveXObject("Microsoft.XMLHTTP")
                }
                return objXMLHttp
            }

            function checkSchema(schema, messageId, linkId)
            {
                if(document.getElementById(messageId).innerHTML!="checking..."){
                    //Status already checked
                    return;
                }

                this.messageId = messageId;
                this.linkId = linkId;
                xmlHttp=GetXmlHttpObject()
                if (xmlHttp==null)
                {
                    alert ("Browser does not support HTTP Request")
                    return
                }
                var url="saveOpportunity.do?checkSchema="+schema;
                xmlHttp.onreadystatechange=stateChanged
                xmlHttp.open("GET",url,true)
                xmlHttp.send(null)
            }

            function stateChanged()
            {
            if (xmlHttp.readyState==4 || xmlHttp.readyState=="complete")
            {
                if(xmlHttp.responseText == "true"){
                    document.getElementById(linkId).style.display="";
                    document.getElementById(messageId).innerHTML=""
                }else {
                    document.getElementById(messageId).innerHTML="Not eligible for Coeus transmission"
                }
            }
            }
            //JIRA COEUSDEV 61 - END
        </script>
        <script>
            var lastRowId = -1;
            var lastDataRowId;
            var lastDivId;
            var expandedRow = -1;
            
            var defaultStyle = "rowLine";
            var selectStyle = "rowHover";
            
            function selectRow(rowId, dataRowId, divId, linkId) {
            var row = document.getElementById(rowId); 
            if(rowId == lastRowId) {
            //Same Row just Toggle
            var style = document.getElementById(dataRowId).style.display;
            if(style == "") {
            style = "none";
            styleClass = defaultStyle;
            expandedRow = -1;
            toggleText="&gt;&gt;";
            }else{
            style = "";
            styleClass = selectStyle;
            expandedRow = rowId;
            toggleText="&lt;&lt;";
            }
            //document.getElementById(dataRowId).style.display = style;
            //Sliding - START
            ds = new DivSlider();
            if(style == "") {
            document.getElementById(dataRowId).style.display = "";
            ds.showDiv(divId);
            }else {
            ds.hideDiv(divId, "document.getElementById('"+dataRowId+"').style.display='none'");
            }
            //Sliding - END
            row.className = defaultStyle;
            //row.className = styleClass;
            document.getElementById(linkId).innerHTML=toggleText;
            }else {
            document.getElementById(dataRowId).style.display = "";
            //Sliding - START
            ds2 = new DivSlider();
            ds2.showDiv(divId);
            //Sliding - END
                    
            //row.className = selectStyle;
            row.className = defaultStyle;
            expandedRow = rowId;
            document.getElementById(linkId).innerHTML="&lt;&lt;";
                    
            //reset last selected row
            if(lastRowId != -1) {
            row = document.getElementById(lastRowId);
            //document.getElementById(lastDataRowId).style.display = "none";
                        
            ds3 = new DivSlider();
            ds3.hideDiv(lastDivId, "document.getElementById('"+lastDataRowId+"').style.display='none'");
                        
            row.className = defaultStyle;
            document.getElementById(linkId).innerHTML="&gt;&gt;";
            }
            }
                
            lastRowId = rowId;
            lastDivId = divId;
            lastDataRowId = dataRowId;
            }
            
            
            function rowHover(rowId, styleName) {
            elemId = document.getElementById(rowId);
            elemId.style.cursor = "hand";
            //If row is selected retain selection style
            //Apply hover Style only if row is not selected
            if(rowId != expandedRow) {
            elemId.className = styleName;
            }
            }
            
            function validateOpportunityForm() {
                var cfda = document.opportunitySearchForm.cfdaNumber.value;
                var programId = document.opportunitySearchForm.programNumber.value;
                if(cfda == "" && programId == "") {
                    alert("<bean:message bundle="proposal" key="opportunity.enter.cfdaOrprogramId"/>");
                    return false;
                }
                return true;
            }
        </script>
    </head>
    <body>
        <table align="center" border="0" cellspacing="0" cellpadding="0" width="100%" class="table">
            <%if(request.getAttribute("Exception") != null) {%>
            <tr><td><br>
                    <table align="center" width="98%" border="0" cellspacing="0" cellpadding="2" class="tabtable">
                        <tr><td class="copyred">
                                <img src="<%=request.getContextPath()%>/coeusliteimages/error.gif"> <%=request.getAttribute("Exception")%>
                        </td></tr>
                    </table>
            </td></tr>
            <% } %>
            
            <tr> <!-- Opportunity Search Form - START -->
                <td>
                    <br>
                    <html:form action="opportunitySearchAction" onsubmit="return validateOpportunityForm()">
                        <table align="center" width="98%" border="0" cellspacing="0" cellpadding="2" class="tabtable">
                            <tr class="theader">
                                <td>Grants.gov Opportunity Search </td>
                            </tr>
                            <tr> <!-- Instructions -->
                                <td align="center" class="copybold">
                                    <br>Enter CFDA Number and/or Opportunity Id to search for Opportunity.
                                </td>
                            </tr>
                            
                            <tr class="copybold" align="center"> <!-- Details -->
                                <td>
                                    <table class="copybold">
                                    <tr>
                                    <td>CFDA Number :</td>
                                    <td><html:text property="cfdaNumber" /> &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>
                                    <td>Opportunity Id :</td>
                                    <td><html:text property="programNumber" />&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>
                                    <td><html:submit value="Search"/></td>
                                    </tr>
                                    <tr>
                                    <td>&nbsp;</td>
                                    <td valign="top">xx.xxx</td>
                                    <td>&nbsp;</td>
                                    <td>&nbsp;</td>
                                    <td>&nbsp;</td>
                                    </tr>
                                    </table>
                                </td>
                            </tr>
                        </table>
                    </html:form>
                </td>
            </tr><!-- Opportunity Search Form - END -->
            
            <tr> <!-- Search results - START -->
                <td>
                    <%if(request.getAttribute("opportunity") != null) {%>
                    <table align="center" width="98%" border="0" cellspacing="0" cellpadding="2" class="tabtable">
                        <tr class="theader">
                            <td>Opportunity Id </td>
                            <td>Opportunity Title </td>
                            <td>Competition Id </td>
                            <td>&nbsp;</td>
                        </tr>
                        <% int counter; String title = "", onClick="", checkSchema=""; %>
                        <logic:iterate id="opportunityBean" name="opportunity" scope="request" type="edu.mit.coeus.s2s.bean.OpportunityInfoBean" indexId="ctr">
                            <% counter = ctr.intValue();
                                onClick = "selectRow('row"+counter+"', 'dataRow"+counter+"', 'div"+counter+"' , 'toggle"+counter+"')";
                                //JIRA COEUSDEV 61 - START
                                checkSchema="checkSchema('"+opportunityBean.getSchemaUrl()+"','txtMessage"+counter+"','createPropLink"+counter+"')";
                                //JIRA COEUSDEV 61 - END
                            %>
                            <tr class="rowLine" id="row<%=ctr%>" onclick="<%=onClick%>,<%=checkSchema%>" onmouseover="rowHover('row<%=ctr%>','rowHover rowLine')" onmouseout="rowHover('row<%=ctr%>','rowLine')">
                                <td nowrap>
                                    <%String context = request.getContextPath();%>
                                    <bean:write name="opportunityBean" property="opportunityId"/>
                                </td>
                                <td nowrap>
                                    <%--<bean:write name="opportunityBean" property="opportunityTitle"/>--%>
                                    <%
                                        title = opportunityBean.getOpportunityTitle();
                                        if(title != null && title.length() > 50){
                                            title = title.substring(0, 50) + "...";
                                        }
                                        out.print(title);
                                    %>
                                </td>
                                <td nowrap>
                                    <bean:write name="opportunityBean" property="competitionId"/>
                                </td>
                                <td id="toggle<%=ctr%>" class="copybold" align="right">&gt;&gt;</td>
                            </tr>
                            
                            <tr id="dataRow<%=ctr%>" style="display:none" class="copy rowHover">
                                <td colspan="4">
                                    <div id="div<%=ctr%>" style="overflow:hidden">
                                        <table border="0" width="100%" cellspacing="0" cellpadding="2" class="copy rowHover"><!-- onclick="<%=onClick%>"-->
                                            <tr>
                                                <td align="right" class="copybold">Title:</td>
                                                <td><bean:write name="opportunityBean" property="opportunityTitle"/></td>
                                                <%
                                                //JIRA COEUSDEV 61 - START
                                                boolean hasCreatePropRight = ((Boolean)request.getAttribute("CREATE_PROPOSAL_RIGHT")).booleanValue();
                                                if(hasCreatePropRight) {
                                                %>
                                                <td colspan="2" align="center"><span id="txtMessage<%=counter%>">checking...</span>
                                                <a id="createPropLink<%=counter%>" style="display:none" href="<%=request.getContextPath()%>/getUnitDetails.do?SUBHEADER_ID=SH002&schemaUrl=<bean:write name='opportunityBean' property='schemaUrl'/>&oppNum=<bean:write name='opportunityBean' property='opportunityId'/>&compId=<bean:write name='opportunityBean' property='competitionId'/>">Create Proposal</a></td>
                                                <%
                                                //JIRA COEUSDEV 61 - END
                                                }
                                                %>
                                            </tr>
                                            <tr>
                                                <td align="right" class="copybold" nowrap>Starting Date:</td><td><bean:write name="opportunityBean" property="openingDate"/></td>
                                                <td align="right" class="copybold" nowrap>Closing Date:</td><td><bean:write name="opportunityBean" property="closingDate"/></td>
                                            </tr>
                                            <tr>
                                                <td align="right" class="copybold" nowrap>Schema url:</td>
                                                <td colspan="3"><a href="<bean:write name="opportunityBean" property="schemaUrl"/>" target="_blank"><bean:write name="opportunityBean" property="schemaUrl"/></a></td>
                                            </tr>
                                            <tr>
                                                <td align="right" class="copybold" nowrap>Instruction url:</td>
                                                <td colspan="3"><a href="<bean:write name="opportunityBean" property="instructionUrl"/>" target="_blank"><bean:write name="opportunityBean" property="instructionUrl"/></a></td>
                                            </tr>
                                        </table>
                                    </div>
                                </td>
                            </tr>
                        </logic:iterate>
                    </table>
                    <br>
                    <% }else if(request.getAttribute("searchResults") != null && request.getAttribute("searchResults").equals(new Boolean(true))){ %>
                    <table align="center" width="98%" border="0" cellspacing="0" cellpadding="2" class="tabtable">
                        <tr><td>
                        <img src="<%=request.getContextPath()%>/coeusliteimages/info.gif"> <bean:message bundle="proposal" key="grantsgov.noOpportunity"/>
                        </td></tr>
                    </table>
                    <br>
                    <% } %>
                </td>
            </tr> <!-- Search results - END -->
        </table>
    </body>
</html>
