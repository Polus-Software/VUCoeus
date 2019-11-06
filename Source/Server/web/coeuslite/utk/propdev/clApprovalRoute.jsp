<%@ page import="org.apache.struts.validator.DynaValidatorForm,java.util.Vector,
java.util.Iterator,java.util.Hashtable,java.util.*"%>

<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<%@page errorPage = "ErrorPage.jsp" %>
<%@ page import="java.util.HashMap" %>
<%@ page import="java.util.Hashtable" %>
<%@ page import="java.util.Set" %>
<%@ page import="org.apache.batik.dom.util.HashTable" %>
<%@ page import="org.apache.struts.validator.DynaValidatorForm" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="edu.mit.coeus.utils.CoeusVector" %>
<%@ page import="java.util.Comparator" %>
<%@ page import="java.util.Vector" %>
<%@ page import="java.util.Iterator" %>
<%@ page import="org.apache.commons.beanutils.*" %>
<%@ page import="org.apache.commons.collections.comparators.*" %>
<%@ page import="edu.utk.coeuslite.propdev.bean.ApprovalRouteDisplayBean"%>
<%@ page import="edu.mit.coeus.utils.CoeusVector"%>
<%@ page import="edu.mit.coeus.routing.bean.RoutingTxnBean"%>
<%@ page import="edu.mit.coeus.routing.bean.RoutingDetailsBean"%>
<%@ page import="edu.mit.coeus.utils.ModuleConstants" %>
<%@ page import="java.util.logging.Level" %>
<%@ page import="java.util.StringTokenizer" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" 	prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" 	prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" 	prefix="logic" %>
<%@ taglib uri="/WEB-INF/coeus-utils.tld"	prefix="coeusUtils" %>
<%@ include file= "/coeuslite/mit/utils/CoeusContextPath.jsp"  %>

<%edu.mit.coeus.routing.bean.RoutingBean routingBean = 
        (edu.mit.coeus.routing.bean.RoutingBean) session.getAttribute("routingBean"+session.getId());
routingBean = (routingBean == null) ? new edu.mit.coeus.routing.bean.RoutingBean() : routingBean;%>

<html:html>

<% 
	String applicationPath = request.getContextPath();
	
        String EMPTY_STRING = "";
        Vector vecApprovalRouteMaps = (Vector)request.getAttribute("vecApproval");
        String moduleCode = (String)session.getAttribute("moduleCode"+session.getId());
        int iModuleCode = (moduleCode == null)? 0 : Integer.parseInt(moduleCode);

        String routingStartDate =(String) session.getAttribute("routingstartdate");
        String routingEndDate =(String) session.getAttribute("routingenddate");
        int submissionNumber=(Integer)session.getAttribute("submissionNumber");
         String statusCodeRejected=(String)request.getAttribute("statusCode");
         int approvalStatusFlag=0;
               
%>



<head>

<title>Department Required for Proposal Approval</title>
<script language="javascript" type="text/JavaScript" src="_js.js"></script>
<script language="JavaScript" src="<%=applicationPath%>/coeuslite/mit/utils/scripts/tree.js"></script>
<link href="<bean:write name='ctxtPath'/>/coeuslite/mit/utils/css/coeus_styles.css" rel="stylesheet" type="text/css">
<link href="/coeuslite/mit/utils/css/ipf.css" rel="stylesheet" type="text/css">
<Script language="JavaScript">
   function processVisiblity (blockId)
   {
	var obj = document.getElementById(blockId);
	var textShow = document.getElementById(blockId+"_show");
	var textHide = document.getElementById(blockId+"_hide");
	if (obj.style.display=='none') 
	{ 
            obj.style.display = 'block';
            textShow.style.display = 'none';
            textHide.style.display = 'block';
	} 
	else 
	{
            obj.style.display = 'none';
            textShow.style.display = 'block';
            textHide.style.display = 'none';
	}
   }
   
   function show_div(div_id) {
        // hide all the divs
        var typeId = div_id+'_show';
        document.getElementById('Heirarchy').style.display = 'none';
        document.getElementById('Boxed').style.display = 'none';
        document.getElementById('Heirarchy_show').style.display = 'none';
        document.getElementById('Boxed_show').style.display = 'none';        
        <%-- COEUSQA-1433 - Allow Recall from Routing - Start --%>
        document.getElementById('Recall_data').style.display = 'block';
        <%-- COEUSQA-1433 - Allow Recall from Routing - End --%>
        document.getElementById('Legend').style.display = 'block';
        
        // show the requested div
        document.getElementById(div_id).style.display = 'block';
        document.getElementById(typeId).style.display = 'block';
    }
    <%-- Modified for 3915 - can't see comments in routing in Approval Routing 
        ApproverName parameter added
    --%>
    function showComments(routingNumber, mapNumber, levelNumber, stopNumber, approverNumber, approverName, approvalStatus){
        <%-- Modified for 3915 - can't see comments in routing in Approval Routing --%>
        var winleft = (screen.width - 550) / 2;
        var winUp = (screen.height - 450) / 2; 
        var win = "scrollbars=1,resizable=1,width=600,height=500,left="+winleft+",top="+winUp;       
         <%--   ApproverName parameter is passed to clApprRoutingComments.jsp page
        --%>
        sList = window.open("<%=request.getContextPath()%>/approvalRoutComments.do?routingNumber="
                +routingNumber+"&mapNumber="+mapNumber+"&levelNumber="+levelNumber+"&stopNumber="+stopNumber+"&approverNumber="+approverNumber+"&approverName="+approverName+"&approvalStatus="+approvalStatus, "list", win);        
    }
    <%--Modified for the case id 2574  begin--%>
    <%-- Added for case# 4262 - Routing attachments not visible in Lite  -starts --%>
    function showAttachments(routingNumber, mapNumber, levelNumber, stopNumber, approverNumber, approverName, approvalStatus){
        var winleft = (screen.width - 550) / 2;
        var winUp = (screen.height - 450) / 2; 
        var win = "scrollbars=1,resizable=1,width=600,height=500,left="+winleft+",top="+winUp;       
        sList = window.open("<%=request.getContextPath()%>/approvalRoutingAttachments.do?routingNumber="
                +routingNumber+"&mapNumber="+mapNumber+"&levelNumber="+levelNumber+"&stopNumber="+stopNumber+"&approverNumber="+approverNumber+"&approverName="+approverName+"&approvalStatus="+approvalStatus, "list", win);        
    }
    <%--Modified for the case id 2574  end--%>
    <%-- Added for case# 4262 - Routing attachments not visible in Lite -ends --%>
    
    <%-- Added for COEUSQA:1445 - User Not able to View Routing Comments through Show Routing - Start --%>
    function showCommentsAndAttachments(routingNumber)
          {
            var winleft = (screen.width - 800) / 2;
            var winUp = (screen.height - 450) / 2; 
            var win = "scrollbars=1,resizable=1,width=800,height=500,left="+winleft+",top="+winUp;    
            sList = window.open("<%=request.getContextPath()%>/routingCommentsAndAttachments.do?routingNumber="+routingNumber, "list", win);        
          }
     <%-- Added for COEUSQA:1445 - End --%>
     
    <!-- COEUSQA:1699 - Add Approver Role - Start -->         
    function addApprover(mapNumber){         
        var winleft = (screen.width - 650) / 2;
        var winUp = (screen.height - 450) / 2;
        var win = "scrollbars=1,resizable=1,width=750,height=450,left="+winleft+",top="+winUp;
            
        <%if(iModuleCode == ModuleConstants.PROPOSAL_DEV_MODULE_CODE){%>
            sList = window.open('<%=request.getContextPath()%>/generalProposalSearch.do?type=AddApprover&search=true&searchName=WEBPERSONSEARCH&mapNumber='+mapNumber, "list", win);
        <%}else if(iModuleCode == ModuleConstants.PROTOCOL_MODULE_CODE){%>
            sList = window.open('<%=request.getContextPath()%>/irbSearch.do?type=AddApprover&search=true&searchName=WEBPERSONSEARCH&mapNumber='+mapNumber, "list", win);
        <%} else if(iModuleCode == ModuleConstants.IACUC_MODULE_CODE){%>
            sList = window.open('<%=request.getContextPath()%>/iacucSearch.do?type=AddApprover&search=true&searchName=WEBPERSONSEARCH&mapNumber='+mapNumber, "list", win);
        <%}%>
          
    }
    
    function addAlternateApprover(routingNumber, mapNumber, levelNumber, stopNumber, approverNumber){
        var winleft = (screen.width - 650) / 2;
        var winUp = (screen.height - 450) / 2;
        var win = "scrollbars=1,resizable=1,width=750,height=450,left="+winleft+",top="+winUp;      
        
        <%if(iModuleCode == ModuleConstants.PROPOSAL_DEV_MODULE_CODE){%>
            sList = window.open("<%=request.getContextPath()%>/generalProposalSearch.do?type=AddAlternateApprover&search=true&searchName=WEBPERSONSEARCH&routingNumber="
                +routingNumber+"&mapNumber="+mapNumber+"&levelNumber="+levelNumber+"&stopNumber="+stopNumber+"&approverNumber="+approverNumber, "list", win);
        <%}else if(iModuleCode == ModuleConstants.PROTOCOL_MODULE_CODE){%>
            sList = window.open("<%=request.getContextPath()%>/irbSearch.do?type=AddAlternateApprover&search=true&searchName=WEBPERSONSEARCH&routingNumber="
                +routingNumber+"&mapNumber="+mapNumber+"&levelNumber="+levelNumber+"&stopNumber="+stopNumber+"&approverNumber="+approverNumber, "list", win);
        <%} else if(iModuleCode == ModuleConstants.IACUC_MODULE_CODE){%>
            sList = window.open("<%=request.getContextPath()%>/iacucSearch.do?type=AddAlternateApprover&search=true&searchName=WEBPERSONSEARCH&routingNumber="
                +routingNumber+"&mapNumber="+mapNumber+"&levelNumber="+levelNumber+"&stopNumber="+stopNumber+"&approverNumber="+approverNumber, "list", win);
        <%}%>
    }
     <!-- COEUSQA:1699 - End -->
</Script>


</head>
<body onload="show_div('Boxed')">

<%
		
		String screenDisplayType = (String)request.getAttribute("DisplayType");
		String mapIDsPresent = (String)request.getAttribute("mapIDsPresent");
    	pageContext.setAttribute("mapIDsPresent", mapIDsPresent, PageContext.PAGE_SCOPE);
        //COEUSQA:1445 - User Not able to View Routing Comments through Show Routing - Start
        String routingNumber = null;
        routingNumber = (String)request.getAttribute("RoutingNumber");
        String commAttachPresent = request.getContextPath()+"/coeusliteimages/complete.gif";
        String commAttachAbsent = request.getContextPath()+"/coeusliteimages/none.gif";
        //COEUSQA:1445 - End
    	
	
%>


 <logic:equal  name="mapIDsPresent"  value="Yes" scope="page">
 
  <table border="0" width="100%" cellspacing="1" cellpadding="0" height="100%" align=left class='theader'>
    <tr valign=top>
      <td width="70%" align=left height='20'>
          <%if(iModuleCode == ModuleConstants.PROPOSAL_DEV_MODULE_CODE){%>
            <b><bean:message bundle="proposal" key="routing.deptPropApproval"/></b>
          <%} else if(iModuleCode == ModuleConstants.PROTOCOL_MODULE_CODE || iModuleCode == ModuleConstants.IACUC_MODULE_CODE){%>
            <b><bean:message bundle="proposal" key="routing.deptProtocolApproval"/></b>
          <%}%>
        </td>
        <%-- Added for COEUSQA:1445 - User Not able to View Routing Comments through Show Routing - Start --%>
        <td width="30%" align="left" nowrap>           
            <a href="javaScript:showCommentsAndAttachments('<%=routingNumber%>');" class="menu">
                <bean:message bundle="proposal" key="routing.commentsAttachmentsLabel"/>
            </a>         
        </td>
        <%-- Added for COEUSQA:1445 - End --%>
    </tr>
    <tr valign=top>
      <td width="100%" align=left colspan="2">
        <table border="0" width="98%" cellspacing="0" cellpadding="0" class='tabtable' align=center>
          <tr>
            <td height='10'>
            </td>
          </tr>        
          <tr>
            <td width="100%" align=left height='20'>
                <table border="0" width="98%" cellspacing="0" cellpadding="0">

                    <tr style="font-family: Arial,Helvetica,sans-serif;font-size: 12px;color: #333333;font-weight: bold;">
                        <td width='2%'>
                    </td>
                    <td colspan="2"><div style="width: 125px;float: left;">Routing Start Date:</div><%=routingStartDate %>
                            </td>
                    </tr>
                    <tr style="font-family: Arial,Helvetica,sans-serif;font-size: 12px;color: #333333;font-weight: bold;">
                        <td width='2%'>
                    </td>
                    <td colspan="2"><div style="width: 125px;float: left;">Routing End Date:</div><%=routingEndDate %>
                        </td>                    </tr>
                    <tr style="font-family: Arial,Helvetica,sans-serif;font-size: 12px;color: #333333;font-weight: bold;">
                        <td width='2%'>
                    </td>
                    <td colspan="2" height="20" valign="top"><div style="width: 125px;float: left;">Submission Number:</div><%=submissionNumber %></td>
                    </tr>
                <tr>


                <tr>
                    <td width='2%'>
                    </td>
                    <td class='copy'>
                          <%if(iModuleCode == ModuleConstants.PROPOSAL_DEV_MODULE_CODE){%>
                            <bean:message bundle="proposal" key="routing.listOfDept"/>
                          <%} else if(iModuleCode == ModuleConstants.PROTOCOL_MODULE_CODE || iModuleCode == ModuleConstants.IACUC_MODULE_CODE){%>
                            <bean:message bundle="proposal" key="routing.listOfDept.protocol"/>
                          <%}%>                        
                    </td>
                </tr>
                </table>
            </td>
          </tr>
          <tr>
            <td height='10'>
            </td>
          </tr>
          <tr valign=top>
            <td width="100%" align=left>

            <div id="Boxed">
           
        	<table border="0" width="96%" cellspacing="0" cellpadding="0" align=center>
               

         		<%
         		Integer oldLevel = new Integer(0);
    			final Integer ZERO 	 = new Integer(0);
    			boolean isFirstTime = true;
          		%>
                    <% if(vecApprovalRouteMaps!=null && vecApprovalRouteMaps.size() > 0) {
                        for(int indexCount=0 ; indexCount<vecApprovalRouteMaps.size() ; indexCount++) {
                        DynaValidatorForm data = (DynaValidatorForm) vecApprovalRouteMaps.get(indexCount);
                        String originalMapId =((Integer) data.get("mapNumber")).toString();%>
	            <logic:iterate id="hmapId"  name="hmDisplayCollection"  >	
		             <bean:define id="mapId">
		                  <bean:write name="hmapId" property="key" />
		             </bean:define>
          	<%if(originalMapId!=null && originalMapId.equals(mapId)) {%>
		          	<bean:define id="beanData"  name="hmapId" property="value" type="java.util.ArrayList"  /> 
                 <!-- COEUSQA:1699 - Add Approver Role - Start -->
                 <% String approverStatus = null;
                    int mapNumber = 0;
                 %>
                 <logic:iterate id="display1" name="beanData"  offset="5"  type="java.util.ArrayList" >
                     <logic:iterate id = "values1" name = "display1" type = "edu.utk.coeuslite.propdev.bean.ApprovalRouteDisplayBean">	
                         <%if("W".equals(values1.getApprovalStatus())){
                            approverStatus = "W"; 
                            mapNumber = values1.getMapNumber();
                         }
                         %>
                     </logic:iterate>                     
                 </logic:iterate>                
                 <!-- COEUSQA:1699 - End -->
                                
                <tr class='theader'>
                  
                  <td width="18%" height="19" align="left" nowrap><b><%=beanData.get(1)%></b></td>
                  <td width="20%" height="19" align="center"></td>
                  <td width="20%" height="19">&nbsp;</td>                 
                  <td align="center">   
                      <!-- COEUSQA:1699 - Add Approver Role - Start -->
                        <% if((Boolean)session.getAttribute("hasAddApproverRight") == true) {
                           String addApproversURL = "javaScript:addApprover("+mapNumber+")";
                           if("W".equals(approverStatus)) { %> 
                              <html:link href="<%=addApproversURL%>"> <bean:message bundle="proposal" key="routing.addApprover"/> </html:link>
                        <%}}%> 
                      <!-- COEUSQA:1699 - End -->
                  </td>
                  <td width="20%" height="19" align=center>
                    <%String link="javaScript:processVisiblity('"+mapId+"')";%>
                        <html:link href="<%=link%>">
                        <div id="<%=mapId%>_show" style="display: none"><bean:message bundle="proposal" key="routing.show"/></div>
                        <div id="<%=mapId%>_hide" style="display: block"><bean:message bundle="proposal" key="routing.hide"/></div>
                        </html:link>
                  </td>

                </tr>
	             <tr>
                 <td colspan="6">	             
                    <div id="<%=mapId%>" style="display: block">
	             <table width="100%" border="0" cellspacing="0" cellpadding="0">
	             <%int indexColor=0;%>
	            <logic:iterate id="display" name="beanData"  offset="5"  type="java.util.ArrayList" > 
		          		 <logic:iterate id = "values" name = "display" type = "edu.utk.coeuslite.propdev.bean.ApprovalRouteDisplayBean">	
		    <%  String strBgColor="#D6DCE5";
                        if (indexColor%2 == 0) { 
                            strBgColor = "#DCE5F1"; }%>
	           	  <%    
                                        if((values.getApprovalStatus()).equals("C")||(values.getApprovalStatus()).equals("E")){
                                            approvalStatusFlag++;
                                        }
	           	  		pageContext.setAttribute("waitForApproval", values.getApprovalStatus(), PageContext.PAGE_SCOPE);
	           	  		pageContext.setAttribute("primaryApproverFlag", values.getPrimaryApproverFlag(), PageContext.PAGE_SCOPE);
	           	  		
   	             		if( !isFirstTime &&  oldLevel != ZERO && !oldLevel.equals(values.getLevelNumber()))
   	             		{
   	             	%> 
	   	             	<tr class='tabtable'>
	   	               		<td width="15%" height="25" align="left"><img src="<%=applicationPath%>/coeusliteimages/approvalStop.gif"></td>
	   	               		<td width="25%" height="25" align="left">&nbsp;</td>
	   	               		<td width="20%" height="25" align="left">&nbsp;</td>
	   	               		<td width="15%" height="25" align="left">&nbsp;</td>
                                        <td width="15%" height="25" align="left">&nbsp;</td>                
	   	               		<td width="25%" height="25" align="right"><img src="<%=applicationPath%>/coeusliteimages/approvalStop.gif"></td>
	   	             	</tr><%indexColor++;%>
   	             	<%
   	             		}
   	             		isFirstTime = false;
   	             		oldLevel = values.getLevelNumber();
   	             	%> 	             	   	
	             
   	               	<%
   	               	String spacer = null;
   	               	String stopDescription = null;
   	               	String userName = null;
   	               	if(values.getPrimaryApproverFlag().equalsIgnoreCase("N"))
   	               	{
   	               		 //userName = "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"+values.getUserId();
                                 // Added for displaying user name for user Id
                                 userName = values.getApprovalUserName();
                                 //End
   	               		 spacer="&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;";
   	               		 stopDescription = (String)beanData.get(1)+" (Alternate)";
   	               	}
   	               	else
   	               	{
   	               		 //userName = values.getUserId();
                                 // Added for displaying user name for user Id
                                userName = values.getApprovalUserName();
                                //End
   	               		 spacer=EMPTY_STRING;
   	               		 stopDescription = (String)beanData.get(1);
   	               	}
   	               	%>
   	               <%strBgColor="#D6DCE5";
                        if (indexColor%2 == 0) { 
                            strBgColor = "#DCE5F1"; }%>
   	               	
   	               <tr bgcolor='<%=strBgColor%>' class='copy'>
   	              
	   	               	<td width="10%" height="19" align="left"><img src="<%=applicationPath%>/coeusliteimages/<%=values.getApproverType()%>">&nbsp;&nbsp;<img src="<%=applicationPath%>/coeusliteimages/<%=values.getApprovalStatusImage()%>"></td>
   	               	
   	               	<logic:equal name="primaryApproverFlag" value="Y" scope="page">
   	               		<td width="25%" height="19"  class="copyBold"><%=userName%></td>
   	               	</logic:equal>
   	  	            <logic:equal name="primaryApproverFlag" value="N" scope="page">
   	               		<td width="25%" height="19" class="copyBold"><%=userName%></td>
   	               	</logic:equal>               	
   	               	
   	               	<td width="20%" height="19" align="left"><%=values.getApprovalStatusText()%></td>
                        
                        <%if(values.getApprovalStatus() != null
                                && (values.getApprovalStatus().equals("A")
                                    || values.getApprovalStatus().equals("R")
                                    || values.getApprovalStatus().equals("B")
                                    || values.getApprovalStatus().equals("P"))){
        
                            //Added for 3915 - can't see comments in routing in Approval Routing 
                            String approverName = "'"+userName+"'";
                            String approvalStatus = "'"+values.getApprovalStatus()+"'";
                            String attributes = "javaScript:showComments("+values.getRoutingNumber()+","+values.getMapNumber()+","+values.getLevelNumber()+","+values.getStopNumber()+","+values.getApproverNumber()+","+approverName+","+approvalStatus+")";
                            //Added for case#4262 -  Routing attachments not visible in Lite 
                            String attachmentsURL = "javaScript:showAttachments("+values.getRoutingNumber()+","+values.getMapNumber()+","+values.getLevelNumber()+","+values.getStopNumber()+","+values.getApproverNumber()+","+approverName+","+approvalStatus+")";%>
                            <td width="13%" height="19">
                               
                                <!-- COEUSQA:1445 - User Not able to View Routing Comments through Show Routing - Start -->
                                <% if(values.getIsCommentsPresent() == true) { %>
                                <html:img src="<%=commAttachPresent%>" border="0"/> 
                                <%} else { %>
                                <html:img src="<%=commAttachAbsent%>" border="0"/> 
                                <% } %>
                                <!--COEUSQA:1445 - End -->
                                <html:link href="<%=attributes%>">
                                    <bean:message bundle="proposal" key="routing.commentsLabel" />
                                </html:link>
                            </td>
                            <%-- Modified for 4262 - Routing attachments not visible in Lite -start --%>
                            <td width="13%" height="19">
                                <!--COEUSQA:1445 - User Not able to View Routing Comments through Show Routing - Start -->
                                <% if(values.getIsAttachmentsPresent() == true) { %>
                                <html:img src="<%=commAttachPresent%>" border="0"/> 
                                <%} else { %>
                                <html:img src="<%=commAttachAbsent%>" border="0"/> 
                                <% } %>
                                <!-- COEUSQA:1445 - End -->
                                <html:link href="<%=attachmentsURL%>">
                                    <bean:message bundle="proposal" key="summary.attachments" />
                                </html:link>
                            </td>
                            <%-- Modified for 4262 - Routing attachments not visible in Lite -ends --%>
                        <%} else {%>
                            <td width="13%" height="19">&nbsp; </td>
                        <%}%>
                        <td width="25%">
                            <!-- COEUSQA:1699 - Add Approver Role - Start -->
                            <% if((Boolean)session.getAttribute("hasAddApproverRight") == true) {
                               if("W".equals(values.getApprovalStatus()) && "Y".equalsIgnoreCase(values.getPrimaryApproverFlag()) ) { %>                               
                                   <% String addAlternateApproversURL = "javaScript:addAlternateApprover("+values.getRoutingNumber()+","+values.getMapNumber()+","+values.getLevelNumber()+","+values.getStopNumber()+","+values.getApproverNumber()+")"; %>
                                       <html:link href="<%=addAlternateApproversURL%>"> <bean:message bundle="proposal" key="routing.addAlternateApprover"/>  </html:link>                          
                            <%} }%> </td>
                           <!-- COEUSQA:1699 - End -->
   	               	<td colspan="3" width="25%" height="19"><!--<%=values.getDescription()%>--></td>
   	              	</tr><%indexColor++;%>
   	             
	       		 </logic:iterate>
                         
	        	</logic:iterate>
		        <%String strBgColor="#D6DCE5";
                        if (indexColor%2 == 0) { 
                            strBgColor = "#DCE5F1"; }%>
		 	    <tr bgcolor='<%=strBgColor%>'>
   	               	<td width="15%" height="19">&nbsp;</td>
   	               	<td width="25%" height="19">&nbsp;</td>
   	               	<td width="20%" height="19">&nbsp;</td>
   	               	<td width="15%" height="19">&nbsp;</td>
   	               	<td width="25%" height="19">&nbsp;</td>
   	             </tr>          	
	             </table>	
		          
                	</div> 
                	</td>
					</tr>
              	
              	<%
              		isFirstTime = true; 
              	%>
              	<%}%>
	          	</logic:iterate>
	          	
                <%}}%>
              </table> 
         </div> 
         
         
        <div id="Heirarchy" style='display: none;'>
        	<table border="0" width="96%" align=center cellspacing="0" cellpadding="0" class='table'>
        	<tr class='theader'>
                    <td height='20'>
                        Approval Status
                    </td>
        	</tr>
            <tr valign=top>
            <td>   
            <%
            
                        
         		oldLevel = new Integer(0);
    			String oldMap =	"-1";
    			String newMainApprover = EMPTY_STRING;
    			String oldMainApprover = EMPTY_STRING;
    			String mainApprover = EMPTY_STRING;
     			String oldAlternateApprover = EMPTY_STRING;
    			String newAlternateApprover = EMPTY_STRING;
   				
    			String approverImage = EMPTY_STRING;
    			String approverStatus = EMPTY_STRING;
    			String approverBypassString = EMPTY_STRING;
 
    			String approverDisplayString = EMPTY_STRING;
    			String oldNodeString = EMPTY_STRING;
    			String altApproverDisplayString = EMPTY_STRING;
    			StringBuffer displayBuffer = new StringBuffer();
    			
    			boolean alternateApproverFlag =	false;
    			boolean followingAlternateApproverFlag  = false;
    			boolean mainApproverFlag = false;
    			boolean followingMain = false;
				isFirstTime = true;
                String fontStart = "<font size=2px>";
                String fontEnd = "</font>";
                
                int maxLevel = 0;
                boolean levelCheck = true;
                boolean altCheck = false;
                Vector maps = new Vector();
    			
          	%>
           	<script><!--

           var TREE_ITEMS = [
                  	
         	['<%=fontStart%>Approval Route<%=fontEnd%>',0, 

                    <% int mapsCount=0;
                        if(vecApprovalRouteMaps!=null && vecApprovalRouteMaps.size() > 0) {
                        for(int indexCount=0 ; indexCount<vecApprovalRouteMaps.size() ; indexCount++) {
                        DynaValidatorForm data = (DynaValidatorForm) vecApprovalRouteMaps.get(indexCount);
                        String originalMapId =((Integer) data.get("mapNumber")).toString();
                        String parentMap = ((Integer) data.get("parentMapNumber")).toString();
%>         
           
  
	            <logic:iterate id="hmapId"  name="hmDisplayCollection"  >	
		             <bean:define id="mapId">
		                  <bean:write name="hmapId" property="key" />
		             </bean:define>
		             <%if(originalMapId!=null && originalMapId.equals(mapId)) {%>
	        		<bean:define id="beanData"  name="hmapId" property="value" type="java.util.ArrayList"  />  
   	            	<% 
                        for(int index=0 ; index<maps.size() ; index++){
                            if(maps.get(index).equals(parentMap)){
                                    for(int cou=mapsCount; cou>=index+1 ; cou--){
                                        displayBuffer.append("],");
                                            mapsCount--;
                                    }
                            }
                        }
                        mapsCount++;
                        maps.add(parentMap);%>
   	            	<%
   	            		/**
   	            		*	This is the Map Description
   	            		*/
   	            		
   	            		displayBuffer.append("['"+fontStart+beanData.get(1)+fontEnd+"',0,");
                               
   	            	%>
                                            <%String oldPrimaryAppr = EMPTY_STRING;
                                              int count =0;
                                              oldLevel = new Integer(0);%>   	            	
   	                      <logic:iterate id="display" name="beanData"  offset="5"  type="java.util.ArrayList" > 

		          		 <logic:iterate id = "values" name = "display" type = "edu.utk.coeuslite.propdev.bean.ApprovalRouteDisplayBean">
		        		<%
		        				if(levelCheck)
		        				{
		        					maxLevel = values.getLevelNumber().intValue();
		        					
		        					levelCheck = false;
		        					
		        				}
		        				
		        			// This gets the stage display

		        		if(  oldLevel != ZERO && !oldLevel.equals(values.getLevelNumber()))
   	            			{   
   	            				String stage = EMPTY_STRING;
   	            				if(!isFirstTime)
   	            				{       
                                                            
                                                            if(displayBuffer.substring(displayBuffer.length()-2,displayBuffer.length()).equals("0,") &&
                                                                                                                        !oldPrimaryAppr.equals(EMPTY_STRING)) {
                                                              displayBuffer.delete(displayBuffer.length()-3,displayBuffer.length());
                                                              displayBuffer.append("],");
                                                              count--;
                                                            }

                                                        for(int index=1 ; index <= count ; index++) {
                                                            displayBuffer.append("],");
                                                        }
                                                        count = 0;
   	            				}
                                                stage="['"+fontStart+"Stage "+values.getLevelNumber()+fontEnd+"',0,";
                                                oldPrimaryAppr = EMPTY_STRING;
                                                count++;
                                                
   	            				displayBuffer.append(stage);
                                                
   	            				isFirstTime = false;		         	
   	            				oldLevel =values.getLevelNumber();
   	            			}
	             			if(values.getBypassFlag().equals("1"))
	             			{
	             				//approverBypassString ="&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<a href="+applicationPath+"/getApprovalActions.do?actionMode=bypass&beanName=byPassBean"+mapId+">Bypass</a>";
	             			}
	             			else
	             			{
	             				approverBypassString = EMPTY_STRING;
	             			}
		        			approverImage = "<img src="+applicationPath+"/coeusliteimages/"+values.getApproverType()+">";
		        			approverStatus = "<img src="+applicationPath+"/coeusliteimages/"+values.getApprovalStatusImage()+">";
                                        String newPrimaryAppr = values.getPrimaryApproverFlag();
                                                
                                        if(oldPrimaryAppr.equals("Y") && newPrimaryAppr.equals("Y")) {
                                            if(displayBuffer.substring(displayBuffer.length()-2,displayBuffer.length()).equals("0,")) { 
                                                displayBuffer.delete(displayBuffer.length()-3,displayBuffer.length());
                                                displayBuffer.append("],");
                                                count--;
                                            }
                                        }
                                        
                                        //newAlternateApprover=approverImage+" "+approverStatus+" "+fontStart+values.getUserName()+""+approverBypassString+fontEnd;
                                        newAlternateApprover=approverImage+" "+approverStatus+" "+fontStart+values.getApprovalUserName()+""+approverBypassString+fontEnd;
                                        if(newPrimaryAppr.equals("Y")){
                                            displayBuffer.append("['"+newAlternateApprover+"',0,");
                                            count++;
                                        } else {
                                            displayBuffer.append("['"+newAlternateApprover+"'],");
                                        }
                                        oldPrimaryAppr = newPrimaryAppr;%>	
	           		</logic:iterate>
		      </logic:iterate>
                      <%
                        if(oldPrimaryAppr.equals("Y")){
                          if(displayBuffer.substring(displayBuffer.length()-2,displayBuffer.length()).equals("0,")) {                          
                              displayBuffer.delete(displayBuffer.length()-3,displayBuffer.length());
                              displayBuffer.append("],");
                              count--;
                          }
                        }
                        for(int index=1 ; index < count+1 ; index++) {
                            displayBuffer.append("],");
                        }%>

              	<%}%>
	 			</logic:iterate>

                <%}}

                        for(int index=1 ; index <= mapsCount ; index++) {
                            displayBuffer.append("],");
                        }%>    


            <%=displayBuffer.toString()%>
            ]
		    ];
			new tree (TREE_ITEMS,getImageTemplate('BASIC','<%=applicationPath%>/'));
			--></script>
   	      </td>
          </tr>
        </table>
  
       
        </div>
        
  
        
      </td>
    </tr>
    <%-- COEUSQA-1433 - Allow Recall from Routing - Start --%>
    <tr>
      <td width="100%" height="5"></td>
    </tr>
    <tr>	
        <td>
            <div id="Recall_data" style='display: block;'>	
                <table border="0" width="100%" cellspacing="5" cellpadding="0">
                    <%String startUserName = routingBean.getRoutingStartUser();
                    String endUserName = routingBean.getRoutingEndUser();                       
                    String startDate = routingBean.getRoutingStartDate();
                    String endDate = routingBean.getRoutingEndDate();                    
                    String displayRoutingValue = startUserName+" "+startDate;
                    String displayRecallValue = endUserName+" "+endDate;%>
                    <tr class='copy'>
                        <td width="135"><bean:message bundle="proposal" key="routing.routedBy"/></td>
                        <td align="left"><%=displayRoutingValue%></td>
                    </tr>                    
                    <tr class='copy'>                        
                        <td width="135"><bean:message bundle="proposal" key="routing.recalledBy"/></td>
                        <% 
                            String statusCode = "";
                            if((session.getAttribute("protocolStatusCode") != null) && (iModuleCode == ModuleConstants.PROTOCOL_MODULE_CODE)){
                                statusCode = (String)session.getAttribute("protocolStatusCode");
                            }
                            if((session.getAttribute("proposalStatus") != null) && (iModuleCode == ModuleConstants.PROPOSAL_DEV_MODULE_CODE)){
                                statusCode = ""+(Integer)session.getAttribute("proposalStatus");
                            }
                            if((session.getAttribute("statusCode") != null) && (iModuleCode == ModuleConstants.IACUC_MODULE_CODE)){
                                statusCode = (String)session.getAttribute("statusCode");
                            }
                          //  if("8".equals(statusCode) || "100".equals(statusCode)){
                          if(approvalStatusFlag>=1){%>
                            <td align="left"><%=displayRecallValue%></td>
                        <%}%>

                    </tr>                    
                </table>
            </div>
        </td>
    </tr>
    <%-- COEUSQA-1433 - Allow Recall from Routing - End --%>
    <tr>
      <td width="100%" height="21"></td>
    </tr>
    
    <tr>
      <td width="100%" height="50">
        <div align="center">
          <center>
          <table border="0" width="25%" cellpadding="0" cellspacing="0">
            <tr>
               <td colspan='2' width="50%" align=center class='copybold'>
                <%String linkSelection="javaScript:show_div('Boxed')";%>
                <html:link href="<%=linkSelection%>">
                <div id="Heirarchy_show" style="display: none"><u><bean:message bundle="proposal" key="routing.showBoxed"/></u></div>
                </html:link>
                <%linkSelection="javaScript:show_div('Heirarchy')";%>
                <html:link href="<%=linkSelection%>">
                <div id="Boxed_show" style="display: block"><u><bean:message bundle="proposal" key="routing.showHierarchy"/></u></div>
                </html:link>                
               </td>
            </tr>
          </table>
          </center>
        </div>
      </td>
 
    </tr>
    <tr>
    	<td width="100%" height="19" id="LegendText" align="right" class='copybold'>
            <%linkSelection="javaScript:processVisiblity('Legend')";%>
            <html:link href="<%=linkSelection%>">
            <div id="Legend_show" style="display: none"><u><bean:message bundle="proposal" key="routing.showLegend"/></u></div>
            <div id="Legend_hide" style="display: block"><u><bean:message bundle="proposal" key="routing.hideLegend"/></u></div>
            </html:link>
    	</td>
  	</tr>
  	<tr>	
  		<td>
  		<div id="Legend" style='display: block;'>	
	  		<table border="0" width="100%" cellspacing="0" cellpadding="0" height="21">
	      		<tr class='copy'>
                            <td align=center><img src="<%=applicationPath%>/coeusliteimages/primary.gif"></td>
                            <td><bean:message bundle="proposal" key="routing.primaryApprover"/></td>
                            <td align=center><img src="<%=applicationPath%>/coeusliteimages/alternate.gif"></td>
                            <td><bean:message bundle="proposal" key="routing.alternateApprover"/></td>
                            <td align=center><img src="<%=applicationPath%>/coeusliteimages/approved.gif"></td>
                            <td><bean:message bundle="proposal" key="routing.approved"/></td>
	      		    <td align=center><img src="<%=applicationPath%>/coeusliteimages/bypass.gif"></td>
	      		    <td><bean:message bundle="proposal" key="routing.bypassed"/></td>
                            <!-- COEUSQA:3441 - Recalled Proposal notifications, record status changes, and inbox messages - Start -->
                            <td align=center><img src="<%=applicationPath%>/coeusliteimages/recalled.gif"></td>
	      		    <td><bean:message bundle="proposal" key="routing.recalled"/></td>
                            <!-- COEUSQA:3441 - End -->
	      		</tr>
	      		<tr class='copy'>
                            <td align=center><img src="<%=applicationPath%>/coeusliteimages/altappr.gif"></td>
                            <td><bean:message bundle="proposal" key="routing.approvedByOther"/></td>
                            <td align=center><img src="<%=applicationPath%>/coeusliteimages/inprogress.gif"></td>
                            <td><bean:message bundle="proposal" key="routing.inProgress"/></td>
                            <td align=center><img src="<%=applicationPath%>/coeusliteimages/pass.gif"></td>
                            <td><bean:message bundle="proposal" key="routing.passed"/></td>
                            <td align=center><img src="<%=applicationPath%>/coeusliteimages/reject.gif"></td>
                            <td><bean:message bundle="proposal" key="routing.rejected"/></td>
	      		</tr>
	      		<tr class='copy'>
                            <td align=center><img src="<%=applicationPath%>/coeusliteimages/tobesub.gif"></td>
                            <td><bean:message bundle="proposal" key="routing.toBeSubmitted"/></td>
                            <td align=center><img src="<%=applicationPath%>/coeusliteimages/waiting.gif"></td>
                            <td><bean:message bundle="proposal" key="routing.waitingForApproval"/></td>
                            <td align=center><img src="<%=applicationPath%>/coeusliteimages/delegate.gif"></td>
                            <td><bean:message bundle="proposal" key="routing.delegated"/></td>
                            <td align=center><img src="<%=applicationPath%>/coeusliteimages/altpassed.gif"></td>
                            <td><bean:message bundle="proposal" key="routing.passedByOther"/></td>
                            <td></td>
                       </tr>
	      	</table>
      	</div>
      </td>
    </tr>
    <tr>
        <td height='10'>
        </td>
    </tr>
  </table>
  </td>
  </tr>
  
       <%
          String nextNav = applicationPath+"/approvalRoute.do?pageNav=n";
          String previousNav = applicationPath+"/approvalRoute.do?pageNav=p";
          String previousRouting=(String) session.getAttribute("showPreviousRouting");
         
          int approvalSequence=(Integer)session.getAttribute("approvalSequenceNumber");
          String currentSubmissionNumber="";
          String originalSubmissionNumber="";

          if(session.getAttribute("currentSubmissionNumber")!=null && session.getAttribute("originalSubmissionNumber")!=null)
              {
          currentSubmissionNumber=session.getAttribute("currentSubmissionNumber").toString();
          originalSubmissionNumber=session.getAttribute("originalSubmissionNumber").toString();
          }
          
          if(previousRouting==null && ((approvalSequence>2) || statusCodeRejected.equals("3") || statusCodeRejected.equals("211") || statusCodeRejected.equals("214")))
              {
       %>
       <tr>
           <td colspan="2">
               <table cellpadding="0" cellspacing="0" border="0" width="80%" align="center" style="line-height: 30px;font-size: 12px;font-weight: bold;">
                   <tr>
                       <%
                       if(Integer.parseInt(currentSubmissionNumber)==1)
                           {

                       %>
                       <td width="50%" align="center" style="color: #3767b3;">
                           <label> << Previous </label>

                      </td>
                      <td align="center">
                         <html:link href="<%=nextNav%>"> Next >> </html:link>
                      </td>
                      <%
                      }
                       else if(currentSubmissionNumber.equals(originalSubmissionNumber))
                           {
                       %>
                       <td width="50%" align="center">
                          <html:link href="<%=previousNav%>"> << Previous </html:link>

                      </td>
                      <td align="center" style="color: #3767b3;">
                          <label> Next >> </label>
                      </td>
                      <%
                      }
                       else
                           {
                       %>
                        <td width="50%" align="center">
                          <html:link href="<%=previousNav%>"> << Previous </html:link>

                      </td>
                      <td align="center">
                         <html:link href="<%=nextNav%>"> Next >> </html:link>
                      </td>
                       <%
                       }
                       %>
                  </tr>
               </table>
           </td>
       </tr>
  <%
  }
  %>
  </table>
 </logic:equal>
 <logic:equal name="mapIDsPresent" value="No" scope="page">
 <table border="0" width="25%" cellpadding="0" cellspacing="0">
 <tr>
     <td>
         <%if(iModuleCode == ModuleConstants.PROPOSAL_DEV_MODULE_CODE){%>
                <bean:message bundle="proposal" key="routing.noValues"/>
         <%} else if(iModuleCode == ModuleConstants.PROTOCOL_MODULE_CODE || iModuleCode == ModuleConstants.IACUC_MODULE_CODE){%>
                <bean:message bundle="proposal" key="routing.noValues.protocol"/>
         <%}%>
         
     </td>
 </tr>
 </table>                         
</logic:equal>    
</body>
</html:html>

