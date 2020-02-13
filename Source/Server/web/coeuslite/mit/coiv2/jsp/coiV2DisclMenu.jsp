<%-- 
    Document   : coiV2DisclMenu
    Created on : May 8, 2010, 11:05:25 AM
    Author     : Mr
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java"
import="java.util.Vector"
%>
<%@page import="edu.mit.coeuslite.utils.CoeusLiteConstants,
                edu.mit.coeuslite.utils.bean.MenuBean"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/coeus-utils.tld"	prefix="coeusUtils" %>
<%@ include file= "/coeuslite/mit/utils/CoeusContextPath.jsp"  %>
<%@ page import="edu.mit.coeuslite.coiv2.utilities.CoiConstants,java.util.Vector,edu.mit.coeuslite.coiv2.beans.CoiDisclosureBean,edu.mit.coeuslite.coiv2.beans.CoiInfoBean"%>


<%
    Vector approvedDisclosureView = new Vector();
          if(request.getAttribute("ApprovedDisclDetView") != null) {
           approvedDisclosureView= (Vector) request.getAttribute("ApprovedDisclDetView");
           CoiDisclosureBean coiBean=(CoiDisclosureBean)approvedDisclosureView.get(0);
           int reviewStatus = coiBean.getReviewStatusCode();
             if(reviewStatus == 4) {
                      request.setAttribute("assignedtoReviewer", true);
                  }

    }
   String proposalNumber = (String)session.getAttribute("proposalNumber"+session.getId());
   String group="4";
   String mode = (String)session.getAttribute("mode") ;   
    boolean readOnly = false;
    if(mode != null && mode.equals("display")){
        readOnly = true;
    }
    String link = "";
    boolean isEvent = false;
    String contextPath = request.getContextPath(); 
//check=approvedDisclosureview
    //String fromApprovedView=(String)request.getParameter("check");
    String fromApprovedView=null;
    if(session.getAttribute("checkPrint")!=null){
    fromApprovedView= (String)session.getAttribute("checkPrint");
    }

    if(session.getAttribute("isEvent") != null) {
        isEvent = (Boolean)session.getAttribute("isEvent");
    }
    
    String disclosureNumber=null;
    if(session.getAttribute("param1")!=null){
        disclosureNumber=(String)session.getAttribute("param1");
    }
    CoiInfoBean coiInfoBean=(CoiInfoBean)session.getAttribute("CoiInfoBean");
    Integer sequenceNumber=coiInfoBean.getSequenceNumber();
    
    String personId="" ;
    if(session.getAttribute("disclosurePersonId")!=null){
        personId=(String)session.getAttribute("disclosurePersonId");
    }else{
         personId=(String)session.getAttribute("param3");
    }
    String moduleName = "";

    if(session.getAttribute("param5") != null){
        moduleName = (String)session.getAttribute("param5");
    }

    String check = "";
            if(session.getAttribute("checkPrint") != null){
                check =session.getAttribute("checkPrint").toString();
            }
    String projectType = null;
    if(session.getAttribute("projectType")!=null){
        projectType = (String)session.getAttribute("projectType");
           }
    
    String projectLabel = "By Projects";
    
    if(projectType != null && !(projectType.equalsIgnoreCase("Annual") || projectType.equalsIgnoreCase("Revision"))) {
        projectLabel="By Project";
    }

     boolean noQstnrPresent = false;

    if(session.getAttribute("noQuestionnaireForModule") != null){
        noQstnrPresent = (Boolean)session.getAttribute("noQuestionnaireForModule");
    }  

   String value = "";  
   if(session.getAttribute("selectedValue")!=null){
       value=(String)session.getAttribute("selectedValue");
   }                   
%>



    <script>
             function open_budget_summary(link){
                //Commented/Added for case#2776  - Allow concurrent Prop dev access in Lite - start
                //var value = "<%=request.getAttribute("createBudget")%>" ;
               var value = "<%=session.getAttribute("createBudget"+session.getId())%>";
               //Commented/Added for case#2776  - Allow concurrent Prop dev access in Lite - end
               var rights = "<%=readOnly%>";
                if(value == "createBudget" && rights == "false"){
                   var msg = '<bean:message bundle="proposal" key="generalInfoProposal.createNewBudget"/>';
                   if (confirm(msg)==true){
                     window.location= link;
                    }
                  }else{
                   window.location= link;
                  }
               }

             function checkProposal(value)
             {
                var proposalNumber = '<%=proposalNumber%>';
                if(proposalNumber == 'null' || proposalNumber == ''){
                    alert("<bean:message bundle="proposal" key="generalInfoProposal.saveProposalGeneral"/>");
                    return false;
                }
                CLICKED_LINK = value;
                return validateSavedInfo();
             }

             function showAlert(){
             alert("<bean:message bundle="proposal" key="generalInfoProposal.notImplemented"/>");
             }

        </script>
   <!-- <body class="body" style="height: 100%;background-color: #9DBE9;">-->
  
   <table class="table" align="left" width="100%" bgcolor="#9DBFE9">
                   <tr><td>
                           <table class="table" align="center" width="98%" style="padding: 4px">
                               <logic:notPresent name="frmEventDiscl">
                                   <logic:notPresent name="historyView">
                                <tr class="menuHeaderName">
                                    <td colspan="3">
                                        Disclosure View
                                    </td>
                                </tr>
                               </logic:notPresent>
                                </logic:notPresent>
                               <logic:present name="frmEventDiscl">
                                 <tr class="menuHeaderName">
                                    <td colspan="3">
                                        Project Event View
                                    </td>
                                </tr>
                               </logic:present>
                                <logic:present name="historyView">
                                 <tr class="menuHeaderName">
                                    <td colspan="3">
                                        History View
                                    </td>
                                </tr>
                               </logic:present>
                           <%if(projectType!=null && projectType.equalsIgnoreCase("Travel")){
                               String url="/travelDetails.do?&sequenceNumber="+sequenceNumber+"&param1="+disclosureNumber+"&param7="+personId+"&edit=true&view=true";
                                %>
            <tr class="rowline">               
              <td width="16%"  height='16' align="left" valign="top" class = "coeusMenu">
                  <logic:present name="coiMenuDatasaved"><logic:equal name="coiMenuDatasaved" property="travelDataSaved" value="true">  <img src="<%=request.getContextPath()%>/coeusliteimages/img_saved.gif" width="19" height="19">
                  </logic:equal></logic:present>
                  </td>
                   <logic:present name="DiscViewTravel" scope="request">
                 <td width="80%"  height='16' align="left" valign="middle" class = "selectedMenuIndicator">
                      
                   <html:link action="<%=url%>"><font color="#6D0202">Travel Details</font></html:link>
                 </td>
               <td width='4%' align=right  class ="selectedMenuIndicator"> <bean:message key="menu.selected"/> </td>
               </logic:present>
               <logic:notPresent name="DiscViewTravel" scope="request">
                  <td width="80%"  height='16' align="left" valign="middle" class = "coeusMenu">
                   <html:link action="<%=url%>">Travel Details</html:link>
               </td>
                <td width='4%' align=right  class ="selectedMenuIndicator"></td>
               </logic:notPresent> <% } %>
            </tr>
            <%if(!noQstnrPresent) {%>
            <tr>
                <td width="16%"  height='16' align="left" valign="top" class = "coeusMenu">
                  <logic:present name="coiMenuDatasaved"><logic:equal name="coiMenuDatasaved" property="questDataSaved" value="true">  <img src="<%=request.getContextPath()%>/coeusliteimages/img_saved.gif" width="19" height="19">
                  </logic:equal></logic:present>
                  </td>              
              
               <logic:present name="DiscViewQtnr" scope="request">
                 <td width="80%"  height='16' align="left" valign="middle" class = "selectedMenuIndicator">  
                   <html:link action="/screeningquestions.do"><font color="#6D0202">Questionnaires</font></html:link>
                 </td>
               <td width='4%' align=right  class ="selectedMenuIndicator"> <bean:message key="menu.selected"/> </td>
               </logic:present>
               <logic:notPresent name="DiscViewQtnr" scope="request">
                  <td width="80%"  height='16' align="left" valign="middle" class = "coeusMenu">     
                   <html:link action="/screeningquestions.do">Questionnaires</html:link>
               </td>
                <td width='4%' align=right  class ="selectedMenuIndicator"></td>
               </logic:notPresent>
            </tr>
            <%}else {%>
                 <tr class="rowline">
                        <td width="16%"  height='16' align="left" valign="top" class = "coeusMenu"></td>
                        <td width="80%"  height='16' align="left" valign="middle" class = "coeusMenu" style="text-decoration: none;color: gray">
                             Questionnaires
                        </td>
                       <td width='4%' align=right  class ="selectedMenuIndicator"></td>
                    </tr>
            <%}%>
            <% /* if(moduleName.equalsIgnoreCase("Annual")||moduleName.equalsIgnoreCase("Revision")||check.equalsIgnoreCase("approvedDisclosureview"))
             {*/%>
<!--            <tr class="rowLine">
                
                <td width="16%"  height='16' align="left" valign="top" class = "coeusMenu">
                    <logic:present name="coiMenuDatasaved"><logic:equal name="coiMenuDatasaved" property="finEntDataSaved" value="true">  <img width="19" height="19" border="0" src="<%=request.getContextPath()%>/coeusliteimages/img_saved.gif"> </logic:equal></logic:present>
                </td>              
               <logic:present name="DiscViewFinEnt" scope="request">
               <td width='80%'  align="left" valign="middle" class = "coeusMenu" > 
                   <html:link action="/financialentities.do"><font color="#6D0202">By Financial Entity</font></html:link>
                    </td>
               <td width='4%' align=right  class ="selectedMenuIndicator" ><bean:message key="menu.selected"/> </td>
               </logic:present>
               <logic:notPresent name="DiscViewFinEnt" scope="request">
                <td width='80%'  align="left" valign="middle" class = "coeusMenu" >
                   <html:link action="/financialentities.do">By Financial Entity</html:link>
               </td>
                <td width='4%' align=right  class ="selectedMenuIndicator"></td>
               </logic:notPresent>
            </tr>-->
             <%/*}*/%>
<logic:notPresent name="frmEventDiscl">
    <%if((moduleName.equalsIgnoreCase("Annual")||moduleName.equalsIgnoreCase("Revision")||check.equalsIgnoreCase("approvedDisclosureview"))){%>
             <tr class="rowLine">
                <td width="16%"  height='16' align="left" valign="top" class = "coeusMenu">
                    <logic:present name="coiMenuDatasaved"><logic:equal name="coiMenuDatasaved" property="prjDataSaved" value="true">  <img width="19" height="19" border="0" src="<%=request.getContextPath()%>/coeusliteimages/img_saved.gif"> </logic:equal></logic:present>
                </td>
                <logic:present name="DiscViewByPrjt" scope="request">
                  <td width='80%'  align="left" valign="middle" class = "coeusMenu" >
                      <html:link action="/getProjectsByFinancialEntitiesView.do"><font color="#6D0202"><%=projectLabel%></font></html:link>
                  </td>
               <td width='4%' align=right  class ="selectedMenuIndicator" ><bean:message key="menu.selected"/> </td>
               </logic:present>
               <logic:notPresent name="DiscViewByPrjt" scope="request">
                <td width='80%'  align="left" valign="middle" class = "coeusMenu" >
               <html:link action="/getProjectsByFinancialEntitiesView.do"><%=projectLabel%></html:link>
               </td>
                <td width='4%' align=right  class ="selectedMenuIndicator"></td>
               </logic:notPresent>
            </tr>      
    <%}%>
    <%if((!moduleName.equalsIgnoreCase("Annual") && !moduleName.equalsIgnoreCase("Revision")&& !check.equalsIgnoreCase("approvedDisclosureview")&& (projectType!=null && !projectType.equalsIgnoreCase("Travel")))){%>
    <tr class="rowLine">
                <td width="16%"  height='16' align="left" valign="top" class = "coeusMenu">
                    <logic:present name="coiMenuDatasaved"><logic:equal name="coiMenuDatasaved" property="prjDataSaved" value="true">  <img width="19" height="19" border="0" src="<%=request.getContextPath()%>/coeusliteimages/img_saved.gif"> </logic:equal></logic:present>
                </td>
                <logic:present name="DiscViewByPrjt" scope="request">
                  <td width='80%'  align="left" valign="middle" class = "coeusMenu" >
                      <html:link action="/getProjectsByFinancialEntitiesView.do"><font color="#6D0202">By Project</font></html:link>
                  </td>
               <td width='4%' align=right  class ="selectedMenuIndicator" ><bean:message key="menu.selected"/> </td>
               </logic:present>
               <logic:notPresent name="DiscViewByPrjt" scope="request">
                <td width='80%'  align="left" valign="middle" class = "coeusMenu" >
               <html:link action="/getProjectsByFinancialEntitiesView.do">By Project</html:link>
               </td>
                <td width='4%' align=right  class ="selectedMenuIndicator"></td>
               </logic:notPresent>
            </tr>
     <%}%>
</logic:notPresent>

             <tr class="rowLine">
                 <td width="16%"  height='16' align="left" valign="top" class = "coeusMenu">
                     <logic:present name="coiMenuDatasaved"><logic:equal name="coiMenuDatasaved" property="attachDataSaved" value="true">   <img width="19" height="19" border="0" src="<%=request.getContextPath()%>/coeusliteimages/img_saved.gif"> </logic:equal></logic:present>
                 </td>
               <logic:present name="DiscViewAttchmt" scope="request">
                   <td width="80%"  align="left" valign="middle" class = "coeusMenu" >

    <html:link action="/attachments.do"><font color="#6D0202">Attachments</font></html:link>

                  </td>
               <td width='4%' align=right  class ="selectedMenuIndicator" ><bean:message key="menu.selected"/> </td>
               </logic:present>
               <logic:notPresent name="DiscViewAttchmt" scope="request">
                   <td width="80%"  align="left" valign="middle" class = "coeusMenu" >
    <html:link action="/attachments.do">Attachments</html:link>

                  </td>
                <td width='4%' align=right  class ="selectedMenuIndicator"></td>
               </logic:notPresent>
            </tr>
            
            <tr class="rowLine">
                <td width="16%"  height='16' align="left" valign="top" class = "coeusMenu">
                    <logic:present name="coiMenuDatasaved"> <logic:equal name="coiMenuDatasaved" property="noteDataSaved" value="true"> <img width="19" height="19" border="0" src="<%=request.getContextPath()%>/coeusliteimages/img_saved.gif"> </logic:equal></logic:present>
                </td>
               <logic:present name="DiscViewNotes" scope="request">
               <td align="left" valign="middle" class = "coeusMenu">  
            <html:link action="/notes.do"><font color="#6D0202">Notes</font></html:link>  
               </td>
               <td width='4%' align=right  class ="selectedMenuIndicator" ><bean:message key="menu.selected"/> </td>
               </logic:present>
               <logic:notPresent name="DiscViewNotes" scope="request">
                <td align="left" valign="middle" class = "coeusMenu" >
                    <html:link action="/notes.do">Notes</html:link>
               </td>
                <td width='4%' align=right  class ="selectedMenuIndicator"></td>
               </logic:notPresent>
            </tr>
           
             <tr class="rowline">
                 <td width="16%"  height='16' align="left" valign="top" class = "coeusMenu">
                     <logic:present name="coiMenuDatasaved"><logic:equal name="coiMenuDatasaved" property="certDataSaved" value="true"><img width="19" height="19" border="0" src="<%=request.getContextPath()%>/coeusliteimages/img_saved.gif"> </logic:equal></logic:present> 
                 </td>
                <logic:present name="DiscViewCertify" scope="request">
               <td width="80%"  height='16' align="left" valign="middle" class = "coeusMenu">

    <html:link action="/certify.do"><font color="#6D0202">Certifications</font></html:link>

               </td>
               <td width='4%' align=right  class ="selectedMenuIndicator" ><bean:message key="menu.selected"/> </td>
               </logic:present>
               <logic:notPresent name="DiscViewCertify" scope="request">
               <td width="80%"  height='16' align="left" valign="middle" class = "coeusMenu">
 
    <html:link action="/certify.do">Certifications</html:link>
         
               
               </td>
                <td width='4%' align=right  class ="selectedMenuIndicator"></td>
               </logic:notPresent>
            </tr>
           <%-- <% if(fromApprovedView!=null){ %>
            <logic:notPresent name="frmEventDiscl">--%>
           <%
              String linkPrint="";
              if((value.equalsIgnoreCase("current"))){
                   linkPrint="/approveddisclosureprint.do?selected=current&param1="+disclosureNumber +"&param2="+sequenceNumber+"&param7="+personId;
               }else{
                  linkPrint="/approveddisclosureprint.do?selected=Approved&param1="+disclosureNumber +"&param2="+sequenceNumber+"&param7="+personId;
              }%>
            <tr class="rowLine">
                 <td width="16%"  height='16' align="left" valign="top" class = "coeusMenu"></td>                
               <logic:present name="DiscViewPrint" scope="request">
                   <td width="80%"  align="left" valign="middle" class = "coeusMenu" >
                       <html:link action="/approveddisclosureprint.do?" target="_blank"><font color="#6D0202">Print</font></html:link>
                  </td>
               <td width='4%' align=right  class ="selectedMenuIndicator" ><bean:message key="menu.selected"/> </td>
               </logic:present>
               <logic:notPresent name="DiscViewPrint" scope="request">
                   <td width="80%"  align="left" valign="middle" class = "coeusMenu" >                     
                  <html:link action='<%=linkPrint%>' target="_blank">Print</html:link>
                  </td>
                <td width='4%' align=right  class ="selectedMenuIndicator"></td>
               </logic:notPresent>
            </tr>
           <%-- </logic:notPresent>
           <%}%>--%>

           <logic:present name="<%=CoiConstants.IS_VIEWER%>">
<logic:equal value="true" name="<%=CoiConstants.IS_VIEWER%>">
    <logic:present name="otherDiscl">
    <tr class="rowLine">
                 <td width="16%"  height='16' align="left" valign="top" class = "coeusMenu"></td>               
                  <logic:present name="DiscViewSetViewers" scope="request">
                       <td width="80%"  align="left" valign="middle" class = "coeusMenu" >
                           <html:link action="/setViewer.do"><font color="#6D0202">Viewer Actions</font></html:link>
                      </td>
                       <td width='4%' align=right  class ="selectedMenuIndicator" ><bean:message key="menu.selected"/> </td>
                  </logic:present>
                   <logic:notPresent name="DiscViewSetViewers" scope="request">
                       <td width="80%"  align="left" valign="middle" class = "coeusMenu" >
                         <html:link action="/setViewer.do">Viewer Actions</html:link>
                      </td>
                    <td width='4%' align=right  class ="selectedMenuIndicator"></td>
                   </logic:notPresent>
            </tr>
    </logic:present>
 </logic:equal>
</logic:present>
<%--            <logic:notPresent name="frmEventDiscl">
                        <tr class="rowLine">
                 <td width="16%"  height='16' align="left" valign="top" class = "coeusMenu"></td>
                <td width="80%"  align="left" valign="middle" class = "coeusMenu" >
                     <html:link action="/approveddisclosureprint.do">Print</html:link>
                  </td>
                  <td width='4%' align=right  class ="selectedMenuIndicator" ></td>
            </tr>
            </logic:notPresent>--%>
 <%--
 <logic:present name="<%=CoiConstants.IS_VIEWER%>">
               <logic:equal value="false" name="<%=CoiConstants.IS_VIEWER%>">
            <tr class="rowLine">
                 <td width="16%"  height='16' align="left" valign="top" class = "coeusMenu"></td>
                <td width="80%"  align="left" valign="middle" class = "coeusMenu" >
                     <html:link action="/approveddisclosureprint.do">Print</html:link>
                  </td>
                  <td width='4%' align=right  class ="selectedMenuIndicator" ></td>
            </tr>
            </logic:equal>
            </logic:present>
<tr class="rowLine">
                 <td width="16%"  height='16' align="left" valign="top" class = "coeusMenu"></td>
                <td width="80%"  align="left" valign="middle" class = "coeusMenu" >
                     <html:link action="/cert.do">Certiication</html:link>
                  </td>
                  <td width='4%' align=right  class ="selectedMenuIndicator" ></td>
            </tr>--%>
 <% if(fromApprovedView==null){ %>
<logic:present name="<%=CoiConstants.IS_ADMIN%>">
<logic:equal value="true" name="<%=CoiConstants.IS_ADMIN%>">
    <logic:present name="isCertified">
    <logic:equal value="true" name="isCertified">
          <logic:notPresent name="fromReviewlist">
            <%-- <tr class="rowLine">
                 <td width="16%"  height='16' align="left" valign="top" class = "coeusMenu"></td>
                <td width="80%"  align="left" valign="middle" class = "coeusMenu" >
                     <html:link action="/setstatus.do">Actions</html:link>
                  </td>
                  <td width='4%' align=right  class ="selectedMenuIndicator" ></td>
            </tr>--%>
                <%--       <tr class="rowLine">
                 <td width="16%"  height='16' align="left" valign="top" class = "coeusMenu"></td>
                <td width="80%"  align="left" valign="middle" class = "coeusMenu" >
                     <html:link action="/assignDisclToUser.do">Assign Viewers</html:link>
                  </td>
                  <td width='4%' align=right  class ="selectedMenuIndicator" ></td>
            </tr>--%>
         </logic:notPresent>
    </logic:equal>
    </logic:present>
</logic:equal>
</logic:present>
<%}%>
<%--<tr> <td height="17" style="background-color:#538dd5;" colspan="3"></td></tr>--%>
                    <%--<logic:present name="fromReviewlist">--%>
         <logic:notPresent name="historyView">
             <%boolean isReviewer = false;
                if(session.getAttribute("isReviewer") != null) {
                    isReviewer = (Boolean)session.getAttribute("isReviewer");
                    }
                %>
               <%
                  if(fromApprovedView==null || isReviewer){ %>
                     <tr class="menuHeaderName">
                        <td colspan="3">
                            Review Actions
                        </td>
                    </tr>
                    <logic:present name="<%=CoiConstants.IS_ADMIN%>">
                    <logic:equal value="true" name="<%=CoiConstants.IS_ADMIN%>">                        
               
                   
<% boolean isAssigned = false;
   if(request.getAttribute("assignedtoReviewer") != null) {
     isAssigned = (Boolean)request.getAttribute("assignedtoReviewer");
    }
if(request.getSession().getAttribute("noRightForAssignedDiscl")==null){
 if(isAssigned) {
     %>
           <tr class="rowLine">
                 <td width="16%"  height='16' align="left" valign="top" class = "coeusMenu"></td>
                 <td width="80%"  align="left" valign="middle" class = "coeusMenu" colspan="2">
                       <font color="#BAB8B8">Set Review Status</font>
                  </td>
        </tr>

      <%}else {%>
     <tr class="rowLine">
                 <td width="16%"  height='16' align="left" valign="top" class = "coeusMenu"></td>
                  
               <logic:present name="ReviewActPending" scope="request">
                   <td width="80%"  align="left" valign="middle" class = "coeusMenu" >
                       <html:link action="/setstatus.do?&param=pending" ><font color="#6D0202">Set Review Status</font></html:link>
                  </td>
               <td width='4%' align=right  class ="selectedMenuIndicator" ><bean:message key="menu.selected"/> </td>
               </logic:present>
               <logic:notPresent name="ReviewActPending" scope="request">
                   <td width="80%"  align="left" valign="middle" class = "coeusMenu" >
                     <html:link action="/setstatus.do?&param=pending">Set Review Status</html:link>
                  </td>
                <td width='4%' align=right  class ="selectedMenuIndicator"></td>
               </logic:notPresent>
               
            </tr>
         <%}%>
                      

                            <tr class="rowLine">
                 <td width="16%"  height='16' align="left" valign="top" class = "coeusMenu"></td>

                
               <logic:present name="ReviewActApprove" scope="request">
                   <td width="80%"  align="left" valign="middle" class = "coeusMenu" >
                       <html:link action="/setstatus.do?&param=approve"><font color="#6D0202">Approve</font></html:link>
                  </td>
               <td width='4%' align=right  class ="selectedMenuIndicator" ><bean:message key="menu.selected"/> </td>
               </logic:present>
               <logic:notPresent name="ReviewActApprove" scope="request">
                   <td width="80%"  align="left" valign="middle" class = "coeusMenu" >
                     <html:link action="/setstatus.do?&param=approve">Approve</html:link>
                  </td>
                <td width='4%' align=right  class ="selectedMenuIndicator"></td>
               </logic:notPresent>
            </tr>
                                        <tr class="rowLine">
                 <td width="16%"  height='16' align="left" valign="top" class = "coeusMenu"></td>
                
                <logic:present name="ReviewActDisapprove" scope="request">
                    <td width="80%"  align="left" valign="middle" class = "coeusMenu" >
                        <html:link action="/setstatus.do?&param=disapprove"><font color="#6D0202">Disapprove</font></html:link>
                  </td>
               <td width='4%' align=right  class ="selectedMenuIndicator" ><bean:message key="menu.selected"/> </td>
               </logic:present>
               <logic:notPresent name="ReviewActDisapprove" scope="request">
                   <td width="80%"  align="left" valign="middle" class = "coeusMenu" >
                     <html:link action="/setstatus.do?&param=disapprove">Disapprove</html:link>
                  </td>
                <td width='4%' align=right  class ="selectedMenuIndicator"></td>
               </logic:notPresent>
            </tr>
               <tr class="rowLine">
                 <td width="16%"  height='16' align="left" valign="top" class = "coeusMenu">
                  <logic:present name="coiMenuDatasaved"><logic:equal name="coiMenuDatasaved" property="assignViewerDataSaved" value="true">
                          <img width="19" height="19" border="0" src="<%=request.getContextPath()%>/coeusliteimages/img_saved.gif">
                     </logic:equal></logic:present>
                 </td>
                
               <logic:present name="ReviewActAssignViewr" scope="request">
                   <td width="80%"  align="left" valign="middle" class = "coeusMenu" >
                       <html:link action="/assignDisclToUser.do"><font color="#6D0202">Assign Reviewers</font></html:link>
                  </td>
               <td width='4%' align=right  class ="selectedMenuIndicator" ><bean:message key="menu.selected"/> </td>
               </logic:present>
               <logic:notPresent name="ReviewActAssignViewr" scope="request">
                   <td width="80%"  align="left" valign="middle" class = "coeusMenu" >
                     <html:link action="/assignDisclToUser.do">Assign Reviewers</html:link>
                  </td>
                <td width='4%' align=right  class ="selectedMenuIndicator"></td>
               </logic:notPresent>   
            </tr>
             <%}%>
           </logic:equal>
        </logic:present>
        <%}%>
         </logic:notPresent>
           <%--</logic:present>--%>
            <logic:present name="isReviewer">
                    <logic:equal value="true" name="isReviewer">
                   <tr class="rowline">
                       <td width="16%"  height='16' align="left" valign="top" class = "coeusMenu"></td>
                       <td width="80%"  height='16' align="left" valign="middle" class = "coeusMenu">
                       <html:link action="/reviewerAction.do">Add Review Comments</html:link>
                       </td>
                      <td width='4%' align=right  class ="selectedMenuIndicator" ></td>
                   </tr>
                   <tr class="rowline">
                       <td width="16%"  height='16' align="left" valign="top" class = "coeusMenu">
                       <logic:present name="coiMenuDatasaved"><logic:equal name="coiMenuDatasaved" property="reviewCompleteSaved" value="true">
                          <img width="19" height="19" border="0" src="<%=request.getContextPath()%>/coeusliteimages/img_saved.gif">
                     </logic:equal></logic:present>    
                       </td>
                       <td width="80%"  height='16' align="left" valign="middle" class = "coeusMenu">
                       <html:link action="/completeReviewAction.do">Complete Review</html:link>
                       </td>
                      <td width='4%' align=right  class ="selectedMenuIndicator" ></td>
                   </tr>
                    </logic:equal>
            </logic:present>
             <tr> <td height="17"></td></tr>
             <tr class="rowline">
               <td width="16%"  height='16' align="left" valign="top" class = "coeusMenu"></td>
               <td width="80%"  height='16' align="left" valign="middle" class = "coeusMenu">
               <html:link action="/coiHome.do?Menu_Id=004">MY COI Home</html:link>
               </td>
              <td width='4%' align=right  class ="selectedMenuIndicator" ></td>
             </tr>

             <logic:present name="historyView">
                 <tr> <td height="10"></td></tr>
                 <tr class="rowline">
                   <td width="16%"  height='16' align="left" valign="top" class = "coeusMenu"></td>
                   <td width="80%"  height='16' align="left" valign="middle" class = "coeusMenu">
                   <html:link action="/showHistoryForHeader.do">Back to History</html:link>
                   </td>
                  <td width='4%' align=right  class ="selectedMenuIndicator" ></td>
                 </tr>
             </logic:present>
         <%--
<logic:present name="<%=CoiConstants.IS_VIEWER%>">
<logic:equal value="true" name="<%=CoiConstants.IS_VIEWER%>">
    <logic:present name="otherDiscl">
    <tr class="rowLine">
                 <td width="16%"  height='16' align="left" valign="top" class = "coeusMenu"></td>
                <td width="80%"  align="left" valign="middle" class = "coeusMenu" >
                     <html:link action="/setViewer.do">Viewer Actions</html:link>
                  </td>
                  <td width='4%' align=right  class ="selectedMenuIndicator" ></td>
            </tr>
    //</logic:present>
 </logic:equal>
</logic:present>


<tr>
                 <td width="16%"  height='16' align="left" valign="top"></td>
                <td width="80%"  align="left" valign="middle">
                &nbsp;
                </td>
                  <td width='4%' align=right></td>
            </tr>
            <tr class="rowLine">
                 <td width="16%"  height='16' align="left" valign="top" class = "coeusMenu"></td>
                <td width="80%"  align="left" valign="middle" class = "coeusMenu" >
                     <html:link action="/approveddisclosureprint.do">Print</html:link>
                  </td>
                  <td width='4%' align=right  class ="selectedMenuIndicator" ></td>
            </tr>--%>

         </table></td></tr>
               <tr bgcolor="#9DBFE9"><td style="height: 500;" colspan="3">&nbsp;</td></tr>
               </table>
          
