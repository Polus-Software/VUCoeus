<%@page contentType="text/html"%>
<%@page pageEncoding="UTF-8"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@page import="java.util.List, edu.mit.coeus.s2s.bean.OpportunityInfoBean"%>
<%@page import="edu.mit.coeus.exception.CoeusException, edu.mit.coeus.bean.CoeusMessageResourcesBean"%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">

<html>
    <head>
        <title>Opportunity</title>
        <script src="<%=request.getContextPath()%>/coeuslite/mit/utils/scripts/divSlide.js"></script>
        <script>
            var lastRowId = -1;
            var lastDataRowId;
            var lastDivId;
            var lastLinkId;
            var expandedRow = -1;
            
            var defaultStyle = "rowLine";
            var selectStyle = "rowHover";
            
            var txtShow = "show";
            var txtHide = "hide";
            
            function selectRow(rowId, dataRowId, divId, linkId) {
                var row = document.getElementById(rowId); 
                if(rowId == lastRowId) {
                    //Same Row just Toggle
                    var style = document.getElementById(dataRowId).style.display;
                    if(style == "") {
                        style = "none";
                        styleClass = defaultStyle;
                        expandedRow = -1;
                        toggleText = txtShow;
                    }else{
                        style = "";
                        styleClass = selectStyle;
                        expandedRow = rowId;
                        toggleText = txtHide;
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
                    document.getElementById(linkId).innerHTML=txtHide;
                    
                    //reset last selected row
                    if(lastRowId != -1) {
                        row = document.getElementById(lastRowId);
                        //document.getElementById(lastDataRowId).style.display = "none";
                        
                        ds3 = new DivSlider();
                        ds3.hideDiv(lastDivId, "document.getElementById('"+lastDataRowId+"').style.display='none'");
                        
                        row.className = defaultStyle;
                        document.getElementById(lastLinkId).innerHTML=txtShow;
                    }
                }
                
                lastRowId = rowId;
                lastDivId = divId;
                lastDataRowId = dataRowId;
                lastLinkId = linkId;
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
            
            function displayWaitScreen() {
                document.getElementById("oppTable").style.display="none";
                document.getElementById("waitTable").style.display="";
            }
            
            function doNothing(){}
        </script>
    </head>
    <% String mode=(String)session.getAttribute("mode"+session.getId()); 
       boolean displayMode = false;
       if(mode!=null && !mode.equals("")) {   
         if(mode.equalsIgnoreCase("display")){
            displayMode=true;
         }
       }
      //COEUSQA-2309 - START
      String exceptionMsg = null;
      if(request.getAttribute("Exception") != null){
           CoeusException coeusEx = (CoeusException)request.getAttribute("Exception");
           CoeusMessageResourcesBean coeusMessageResourcesBean = new CoeusMessageResourcesBean();
           exceptionMsg= coeusMessageResourcesBean.parseMessageKey(coeusEx.getUserMessage());
           if(exceptionMsg==null){
               exceptionMsg = coeusEx.getUserMessage();
           }
       }
       //COEUSQA-2309 - END
    %>
    <body <%=(exceptionMsg!=null?"onload=\"alert('"+exceptionMsg.replace('\n', ' ')+"')\"":"")%>>
        <table align="center" border="0" cellspacing="0" cellpadding="0" width="100%" height="100%" class="table">
            <tbody>
            <tr class="theader">
                <td>
                <table width="100%" cellspacing="0" cellpadding="0">
                    <tr>
                        <th align="left"> Grants.Gov </th>
                        </td>
                        
                    </tr>
                </table>
                </td>
            </tr>
            
            <tr>
                <td>
                        <div id="helpText" class='helptext'>            
                            <bean:message bundle="proposal" key="helpTextProposal.S2S"/>  
                        </div>                     
                </td>
            </tr>
            
            <%Object object = request.getSession().getAttribute("opportunity");
            List list = null;
            if(object != null) {
                list = (List)object;
                if(list.size() > 0) {
            OpportunityInfoBean oppBean = (OpportunityInfoBean)list.get(0);%>
               
             <%--<tr><!-- header Details START -->
                <td>
                    <table align="left" width="70%%" border="0" cellspacing="0" cellpadding="2" class="copy">
                        <tr>
                           <td align="right">proposal Number:</td>
                            <td><%=request.getSession().getAttribute("proposalNumber")%></td> 
                            <td align="right" class="copybold">CFDA Number: </td>
                            <td><%=oppBean.getCfdaNumber()%></td>
                        </tr>
                    </table>
                </td>
            </tr><!-- header Details END -->--%>
            
            <tr> <!-- Forms -->
                <td>
                    <br>
                    <table align="center" width="98%" border="0" cellspacing="0" cellpadding="2" class="tabtable" id="oppTable" style="display:">
                        <tr class="theader">
                            <td><bean:message bundle="proposal" key="generalInfoProposal.opportunityId"/> </td>
                            <td>Opportunity Title </td>
                            <%if(!displayMode) { %>
                            <td>&nbsp; </td>
                            <% } %>
                            <td align="right">Details</td>
                        </tr>
                        <% int counter; String title = "", onClick=""; %>
                        <logic:iterate id="opportunityBean" name="opportunity" scope="session" type="edu.mit.coeus.s2s.bean.OpportunityInfoBean" indexId="ctr">
                            <% counter = ctr.intValue();
                            onClick = "selectRow('row"+counter+"', 'dataRow"+counter+"', 'div"+counter+"' , 'toggle"+counter+"')";
                            %>
                            <tr class="rowLine" id="row<%=ctr%>" onclick="<%=onClick%>"  onmouseover="rowHover('row<%=ctr%>','rowHover rowLine')" onmouseout="rowHover('row<%=ctr%>','rowLine')">
                            <td nowrap>
                            <%String context = request.getContextPath();%>
                            <%--<a href="<%=context%>/saveOpportunity.do?opportunityId=<bean:write name="opportunityBean" property="opportunityId"/>">--%>
                            <bean:write name="opportunityBean" property="opportunityId"/>
                            <%--</a>--%>
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
                            <%if(!displayMode) {%>
                            <td nowrap>
                            <%--<bean:write name="opportunityBean" property="competitionId"/>--%>
                            <a onclick="javascript:displayWaitScreen()" href="<%=context%>/saveOpportunity.do?opportunityId=<bean:write name="opportunityBean" property="opportunityId"/>&competitionId=<bean:write name="opportunityBean" property="competitionId"/>">Select</a>
                            </td>
                            <% } %>
                            <td class="copybold" align="right"><a href="javascript:doNothing()" id="toggle<%=ctr%>">show</a></td>
                            </tr>
                            
                            <tr id="dataRow<%=ctr%>" style="display:none" class="copy rowHover">
                                <td colspan="4">
                                    <div id="div<%=ctr%>" style="overflow:hidden">
                                    <table border="0" width="100%" cellspacing="0" cellpadding="2" class="copy rowHover" onclick="<%=onClick%>">
                                        <tr>
                                            <td align="right" class="copybold">Title:</td>
                                            <td colspan="3"><bean:write name="opportunityBean" property="opportunityTitle"/></td>
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
                                        <tr>
                                            <td align="right" class="copybold" nowrap>Competition Id:</td>
                                            <td colspan="3"><bean:write name="opportunityBean" property="competitionId"/></td>
                                        </tr>
                                    </table>
                                    </div>
                                </td>
                            </tr>
                        </logic:iterate>
                    </table>
                    
                    <table style="display:none" id="waitTable">
                        <tr>
                        <td class="copybold" align="center"> Updating proposal. please wait. </td>
                        </tr>
                    </table>
                    <br>
                </td>
            </tr>
            <%
            }//End if list Size > 0
            }//End if Object(i.e. List) != null

            if(list == null || list.size() == 0) {%>
                <tr class="copy"><td>
                <%
                CoeusException coeusException = (CoeusException)request.getAttribute("Exception");
                if(coeusException != null) {
                    CoeusMessageResourcesBean coeusMessageResourcesBean =new CoeusMessageResourcesBean();
                    String errMsg= coeusMessageResourcesBean.parseMessageKey(coeusException.getMessage());
                %>
                    <img src="<%=request.getContextPath()%>/coeusliteimages/error.gif"> <%=errMsg%>
                <%}else {
            %>
                <img src="<%=request.getContextPath()%>/coeusliteimages/info.gif"> <bean:message bundle="proposal" key="grantsgov.noOpportunity"/>
            <%}%>
                </td></tr>
            <%}%>
            
        </table>
    </body>
<script>
      var help = '<bean:message bundle="proposal" key="helpTextProposal.S2S"/>';
      help = trim(help);
      if(help.length == 0) {
        document.getElementById('helpText').style.display = 'none';
      }
      function trim(s) {
            return s.replace( /^\s*/, "" ).replace( /\s*$/, "" );
      }
</script>    
   
</html>
