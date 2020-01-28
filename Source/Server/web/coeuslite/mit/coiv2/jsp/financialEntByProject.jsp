<%-- 
    Document   : financialEntByProject
    Created on : Feb 14, 2012, 4:40:56 PM
    Author     : x + iy
--%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN"
    "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<%@ include file= "/coeuslite/mit/utils/CoeusContextPath.jsp"%>
<%@ page import="java.util.Vector,edu.mit.coeuslite.coiv2.beans.Coiv2AttachmentBean,edu.mit.coeuslite.coiv2.services.CoiCommonService,edu.mit.coeuslite.coiv2.beans.CoiPersonProjectDetails"%>

<%@ taglib uri="/WEB-INF/struts-bean.tld"  prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld"  prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/coeus-utils.tld"  prefix="coeusUtils"%>
<%  String path = request.getContextPath();%>
<html:html locale="true">
    <head>
        <title>Disclosure:Certification Confirmation</title>
        <script language="javaScript">
          
 function saveAndcontinue()
            {
               
//                    document.forms[0].action='<%=path%>'+"/getProjectDetailsAndFinEntityDetailsCoiv2.do";
//                    document.forms[0].submit();
                    document.location = '<%=request.getContextPath()%>/coiConflictStatus.do';
                    window.location;
                    
            }

              function exitToCoi(){
                var answer = confirm( "This operation will discard current changes.  Prior screen entries will be saved.  You may continue this disclosure event by selecting it from the Pending section of your COI home page.  Click 'OK' to continue.");
                     if(answer) {
                        document.location = '<%=request.getContextPath()%>/exitDisclosure.do';
                        window.location;
                     }
             }
        </script>

    </head>
    <html:form action="/saveCertificationCoiv2.do">
            <div id="InProgress">
                <table style="width: 100%;" height="100%"  border="0" cellpadding="2" cellspacing="0" class="table" align='center'>
           <%--     code added for displaying project details...starts--%> 
          <%
            String startDate = "";
            String endDate = "";
            String pjctTitle="";
            String moduleItemKey="";
            String awardTitle="";
            String pjctId="";
            
            int eventCode=Integer.parseInt(request.getSession().getAttribute("InPrgssCode").toString());
            String projectType = (String) request.getSession().getAttribute("projectType");
            if(projectType.equalsIgnoreCase("Proposal")||projectType.equalsIgnoreCase("iacucProtocol")||projectType.equalsIgnoreCase("Protocol")||projectType.equalsIgnoreCase("Award") || projectType.equalsIgnoreCase("Travel")){
            Vector pjctDetails = (Vector)request.getSession().getAttribute("eventProjectDetails");
            if(pjctDetails !=null){
            CoiPersonProjectDetails coiPersonProjectDetails =(CoiPersonProjectDetails) pjctDetails.get(0);
            pjctTitle=coiPersonProjectDetails.getCoiProjectTitle();
            moduleItemKey=coiPersonProjectDetails.getModuleItemKey();
            awardTitle=coiPersonProjectDetails.getAwardTitle();
            pjctId=coiPersonProjectDetails.getModuleItemKey();
            CoiCommonService coiCommonService1 = CoiCommonService.getInstance();
            if(coiPersonProjectDetails.getCoiProjectStartDate()!=null)
            {
                startDate = (String) coiCommonService1.getFormatedDate(coiPersonProjectDetails.getCoiProjectStartDate());
            }
            if(coiPersonProjectDetails.getCoiProjectEndDate()!=null)
            {
              endDate = (String) coiCommonService1.getFormatedDate(coiPersonProjectDetails.getCoiProjectEndDate());
            }}}
             %>

               <%
                if(projectType.equals("Protocol")){%>
                 <tr><td colspan="4"><b>For your project listed below, please answer the certification questionnaire:</b></td></tr>
                 <tr><td align="right" width="15%" style="float: none" nowrap><b>IRB protocol # :</b></td>
                            <td style="float: none" align="left" width="30%"><%=moduleItemKey%></td>
                            <td width="15%" style="float: none"></td>
                            <td style="float: none"></td>
                                </tr>
                        <tr><td align="right" width="15%" style="float: none"><b>Title:</b></td>
                            <td style="float: none" align="left" width="30%"><%=pjctTitle%></td>
                            <td width="15%" style="float: none"></td>
                            <td style="float: none"></td>
                        </tr>

                        <tr>
                            <td align="right" width="15%" style="float: none" nowrap><b>Application Date :</b></td><td align="left"  width="33%" style="float: none"><%=startDate%></td>
                            <td align="right" width="15%" style="float: none" nowrap><b>Expiration Date :</b></td><td align="left" width="18%" style="float: none"><%=endDate%></td>
                        </tr>
                   <%}else if(projectType.equalsIgnoreCase("iacucProtocol")){%>
                         <tr><td colspan="4"><b>For your project listed below, please answer the certification questionnaire:</b></td></tr>
                        <tr><td align="right" width="15%" style="float: none" nowrap>&emsp;<b>IACUC protocol # :</b></td>
                            <td align="left" width="30%" align="left"><%=moduleItemKey%></td>
                            <td width="15%" style="float: none"></td>
                            <td style="float: none"></td>
                                </tr>
                        <tr><td align="right" width="15%" style="float: none">&emsp;<b>Title:</b></td>
                            <td align="left" width="30%" align="left"><%=pjctTitle%></td>
                            <td width="15%" style="float: none"></td>
                            <td style="float: none"></td>
                        </tr>
                        <tr>
                            <td align="right" width="15%" style="float: none" nowrap>&emsp;<b>Application Date :</b></td><td align="left" width="33%" style="float: none"><%=startDate%></td>
                            <td align="right" width="15%" style="float: none" nowrap>&emsp;&emsp;<b>Expiration Date :</b></td><td align="left" width="18%" style="float: none"><%=endDate%></td>
                        </tr>
                <%}else if(projectType.equals("Proposal")){%>
                        <tr><td colspan="4"><b>For your project listed below, please answer the certification questionnaire:</b></td></tr>
                        <tr><td align="right" width="15%" style="float: none">&emsp;&emsp;&emsp;<b>Proposal # :</b></td>
                            <td align="left" width="30%" align="left"><%=moduleItemKey%></td>
                            <td width="15%" style="float: none"></td>
                            <td style="float: none"></td>
                                </tr>
                        <tr><td align="right" width="15%" style="float: none">&emsp;&emsp;&emsp;<b>Title:</b></td>
                            <td align="left" width="30%" align="left"><%=pjctTitle%></td>
                            <td width="15%" style="float: none"></td>
                            <td style="float: none"></td>
                        </tr>
                        <tr>
                            <td align="right" width="15%" style="float: none">&emsp;&emsp;&emsp;<b>Start Date :</b></td><td align="left" width="33%" style="float: none"><%=startDate%></td>
                            <td align="right" width="15%" style="float: none">&emsp;&emsp;<b>End Date :</b></td><td align="left" width="18%" style="float: none"><%=endDate%></td>
                        </tr>
               <%} else if(projectType.equals("Award")){ %>
                      <tr><td colspan="4"><b>For your project listed below, please answer the certification questionnaire:</b></td></tr>
                        <tr><td align="right" width="15%" style="float: none">&emsp;&emsp;&emsp;<b>Award # :</b></td>
                            <td align="left" width="30%" align="left"><%=moduleItemKey%></td>
                            <td width="15%" style="float: none"></td>
                            <td style="float: none"></td>
                                </tr>
                        <tr><td align="right" width="15%" style="float: none">&emsp;&emsp;&emsp;<b>Title:</b></td>
                            <td align="left" width="30%" align="left"><%=pjctTitle%></td>
                            <td width="15%" style="float: none"></td>
                            <td style="float: none"></td>
                        </tr>
                        <tr>
                            <td align="right" width="15%" style="float: none">&emsp;&emsp;&emsp;<b>Start Date :</b></td><td align="left" width="33%" style="float: none"><%=startDate%></td>
                            <td align="right" width="15%" style="float: none">&emsp;&emsp;<b>End Date :</b></td><td align="left" width="18%" style="float: none"><%=endDate%></td>
                        </tr>
               <% }else if(projectType.equals("Travel")){%>
                 <tr><td colspan="4"><b>For your project listed below, please answer the certification questionnaire:</b></td></tr>
                        <tr><td align="right" width="15%" style="float: none">&emsp;&emsp;&emsp;<b>Travel # :</b></td>
                            <td align="left" width="30%" align="left"><%=moduleItemKey%></td>
                            <td width="15%" style="float: none"></td>
                            <td style="float: none"></td>
                                </tr>
                        <tr><td align="right" width="15%" style="float: none">&emsp;&emsp;&emsp;<b>Title:</b></td>
                            <td align="left" width="30%" align="left"><%=pjctTitle%></td>
                            <td width="15%" style="float: none"></td>
                            <td style="float: none"></td>
                        </tr>
                        <tr>
                            <td align="right" width="15%" style="float: none">&emsp;&emsp;&emsp;<b>Start Date :</b></td><td align="left" width="33%" style="float: none"><%=startDate%></td>
                            <td align="right" width="15%" style="float: none">&emsp;&emsp;<b>End Date :</b></td><td align="left" width="18%" style="float: none"><%=endDate%></td>
                        </tr>
               <%}%>


<%--      code added for displaying project details...ends--%>
           <logic:present name="eventProjectDetails">
                 
                  <% if(eventCode==2){%>
                    <tr style="background-color:#6E97CF;height: 20px;">                        
                    <td><strong> Proposal #</strong></td>
                    <td><strong> Title</strong></td>
                    <td><strong> Sponsor</strong></td>
                    <td><strong> Start Date</strong></td>
                    <td><strong> End Date</strong></td>                    
                   </tr> 
                   <%}else if( eventCode==1){%>
                     <tr style="background-color:#6E97CF;height: 20px;">
                     <td><strong> Award #</strong></td>
                     <td><strong> Title</strong></td>
                     <td><strong> Sponsor</strong></td>
                     <td><strong> Start Date</strong></td>
                     <td><strong> End Date</strong></td> 
                    </tr>
                   <%}else if(((eventCode==3)|| (eventCode==4))){%>  
                        <tr style="background-color:#6E97CF;height: 20px;">
                        <td><strong> Protocol #</strong></td>
                       <td><strong>Title</strong></td>
                       <td><strong> Sponsor</strong></td>
                        <td style="width:120px; " ><strong> Application Date</strong></td>
                        <td style="width:120px; " ><strong> Expiration Date</strong></td>
                    </tr>
                   <%}else if(eventCode == 8) {%>
                    <tr style="background-color:#6E97CF;height: 20px;">
                     <td><strong>Event Name</strong></td>
                     <td><strong> Purpose</strong></td>
                     <td><strong> Sponsor</strong></td>
                     <td><strong> Start Date</strong></td>
                     <td><strong> End Date</strong></td>
                    </tr>
                   <%}%>
                    <%                                                       
                             String TraveleventName = "";
                             String purpose = "";
                             String travelSponsor = "";
                            Vector propsal = (Vector) request.getAttribute("eventProjectDetails");
                                 CoiPersonProjectDetails propBean1 = (CoiPersonProjectDetails) propsal.get(0);
                                 CoiCommonService coiCommonService1 = CoiCommonService.getInstance();
                                 String eDate="",sDate="";
                                 if(propBean1.getCoiProjectStartDate()!=null){
                                  sDate = coiCommonService1.getFormatedDate(propBean1.getCoiProjectStartDate());
                                 }
                                 if(propBean1.getCoiProjectEndDate()!=null){
                                  eDate =coiCommonService1.getFormatedDate(propBean1.getCoiProjectEndDate());                                  
                                 }
                                 TraveleventName = propBean1.getCoiProjectTitle();
                                  purpose = propBean1.getPurpose();
                                  travelSponsor = propBean1.getCoiProjectSponser();
                                 String mItemKey =propBean1.getModuleItemKey();
                                 String moduleTitle =propBean1.getCoiProjectTitle();
                                 String projectSponsor = propBean1.getCoiProjectSponser();  
                                if(projectSponsor==null){
                                   projectSponsor=""; 
                                }

                    %>
                    <tr bgcolor="#D6DCE5" class="rowLine" onmouseover="className='rowHover rowLine'" onmouseout="className='rowLine'" >

                        <%if(eventCode == 8){%>
                            <td width="15%"><%=TraveleventName%></td>
                            <td width="30%"><%=purpose%></td>
                            <td width="20%"><%=travelSponsor%></td>
                            <td width="15%"><%=sDate%></td>
                            <td><%=eDate%></td>
                        <%} else {%>
                        <td width="15%"><%=mItemKey%></td>
                        <td width="30%"><%=moduleTitle%></td>
                        <td width="20%"><%=projectSponsor%></td>
                        <td width="15%"><%=sDate%></td>
                        <td><%=eDate%></td>
                        <%}%>
                    </tr>
                       
                  
                
                       
               <tr>
                   <td class='savebutton' align="left" colspan="4">
                    <html:button onclick="javaScript:saveAndcontinue();" property="Save" styleClass="clsavebutton" style="width:150px;">
                        Continue
                    </html:button>

                    <html:button onclick="javaScript:exitToCoi();" property="Save" styleClass="clsavebutton" style="width:150px;">
                        Quit
                    </html:button>
                  </td>
               </tr>
          </logic:present>          
                    </table>
                         
        </html:form>
<%--</body>--%>
</html:html>