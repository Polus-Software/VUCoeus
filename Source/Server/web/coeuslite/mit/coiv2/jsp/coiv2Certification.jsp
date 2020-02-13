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
            function onCertify()
            {
             <%   if(request.getAttribute("coiCertValidFails")!=null){ %>
                   alert("Please update the financial interest status for all projects before certifying the disclosure");
                   document.location = '<%=request.getContextPath()%>/coiCentralAction.do';
                   window.location;
                 
                      <%    }
                else{ %>
                alert("Your Disclosure has been submitted")
                var cerText = "The information provided for this Financial Interest Disclosure, including responses to Yes/No questions, list of pertinent financial entities, and disclosure of possible conflict with my sponsored activity, is an accurate and current statement of my reportable outside interests and activities.";
                 document.forms[0].action= '<%=path%>' +"/saveCertificationCoiv2.do?&cerText="+cerText;
                 document.forms[0].submit();
             <%    } %>
            }
              function exitToCoi(){
                var answer = confirm("This operation will discard current changes.  Prior screen entries will be saved.  You may continue this disclosure event by selecting it from the Pending section of your COI home page.  Click 'OK' to continue.");
                     if(answer) {
                        document.location = '<%=request.getContextPath()%>/exitDisclosure.do';
                        window.location;
                     }
             }
        </script>                        
    </head>
    <html:form action="/saveCertificationCoiv2.do">
    <%--    <logic:present name="coiCertValidFails">
        <script language="javaScript">
            alert("Please save all projects before certifying the disclosure");
            document.forms[0].action= '<%=path%>' +"/createDisclosureFromInProgress.do?operation=UPDATE&operationType=MODIFY";
            document.forms[0].submit();                           
        </script>        
        </logic:present>
    --%>   
            <div id="notesProtocol">
                <table style="width: 100%;" height="100%"  border="0" cellpadding="2" cellspacing="0" class="table" align='center'>
           <%--     code added for displaying project details...starts--%>
          <%
            String startDate = "";
            String endDate = "";
            String pjctTitle="";
            String moduleItemKey="";
            String awardTitle="";
            String pjctId="";
            String eventName="";
            int code=0;
            String projectType = (String) request.getSession().getAttribute("projectType");
            if(projectType.equalsIgnoreCase("Proposal")||projectType.equalsIgnoreCase("iacucProtocol")||projectType.equalsIgnoreCase("Protocol")||projectType.equalsIgnoreCase("Award")||projectType.equalsIgnoreCase("Travel")){
            Vector pjctDetails = (Vector)request.getSession().getAttribute("projectDetailsListInSeesion");
            if(pjctDetails !=null){
            CoiPersonProjectDetails coiPersonProjectDetails =(CoiPersonProjectDetails) pjctDetails.get(0);
            pjctTitle=coiPersonProjectDetails.getCoiProjectTitle();
            moduleItemKey=coiPersonProjectDetails.getModuleItemKey();
            awardTitle=coiPersonProjectDetails.getAwardTitle();
            pjctId=coiPersonProjectDetails.getModuleItemKey();
            eventName=coiPersonProjectDetails.getEventName();
            code=coiPersonProjectDetails.getModuleCode();
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
               if(code==7){
                    projectType="IRB Protocol";
                }
             if(code==9){
                    projectType="IACUC Protocol";
                }
                if(projectType.equals("IRB Protocol") || projectType != null && projectType.equalsIgnoreCase("IACUC Protocol")){%>
                  <tr><td colspan="4"><b>For your project listed below, please answer the certification questionnaire:</b></td></tr>
                        <tr><td align="right" width="15%" style="float: none" nowrap><b><%=projectType%> # :</b></td>
                            <td style="float: none" align="left" width="30%"><%=moduleItemKey%></td>
                            <td width="15%" style="float: none"></td>
                            <td style="float: none"></td>
                                </tr>
                        <tr><td align="right" width="15%" style="float: none" nowrap><b>Title :</b></td>
                            <td colspan="3" style="float: none" align="left" width="30%"><%=pjctTitle%></td>
                        <%--<td width="15%" style="float: none"></td>
                            <td style="float: none"></td>--%>
                        </tr>
                        <tr>
                            <td align="right" width="15%" style="float: none" nowrap><b>Application Date :</b></td><td style="float: none" align="left" width="30%"><%=startDate%></td>
                            <td align="right" width="23%" style="float: none" nowrap><b>Expiration Date :</b></td><td align="left"><%=endDate%></td>
                        </tr>
                <%}else if(projectType.equals("Proposal")){%>
                       <tr><td colspan="4"><b>For your project listed below, please answer the certification questionnaire:</b></td></tr>
                        <tr><td align="right" width="15%" style="float: none" nowrap><b>Proposal # :</b></td>
                            <td style="float: none" align="left" width="30%"><%=moduleItemKey%></td>
                            <td width="15%" style="float: none"></td>
                            <td style="float: none"></td>
                                </tr>
                        <tr><td align="right" width="15%" style="float: none" nowrap><b>Title:</b></td>
                            <td colspan="3" style="float: none" align="left" width="30%"><%=pjctTitle%></td>
                        <%--<td width="15%" style="float: none"></td>
                            <td style="float: none"></td>--%>
                        </tr>
                        <tr>
                            <td align="right" width="15%" style="float: none" nowrap><b>Start Date :</b></td><td style="float: none" align="left" width="30%"><%=startDate%></td>
                            <td align="right" width="23%" style="float: none" nowrap><b>End Date :</b></td><td align="left"><%=endDate%></td>
                        </tr>
               <%} else if(projectType.equals("Award")){ %>
                       <tr><td colspan="4"><b>For your project listed below, please answer the certification questionnaire:</b></td></tr>
                        <tr><td align="right" width="15%" style="float: none" nowrap><b>Award # :</b></td>
                            <td colspan="3" style="float: none" align="left" width="30%"><%=moduleItemKey%></td>
                            <%--<td width="15%" style="float: none"></td>
                            <td style="float: none"></td>--%>
                                </tr>
                        <tr><td align="right" width="15%" style="float: none" nowrap><b>Title:</b></td>
                            <td style="float: none" align="left" width="30%"><%=pjctTitle%></td>
                        <td width="15%" style="float: none"></td>
                            <td style="float: none"></td>
                        </tr>
                        <tr>
                            <td align="right" width="15%" style="float: none" nowrap><b>Start Date :</b></td><td style="float: none" align="left" width="30%"><%=startDate%></td>
                            <td align="right" width="23%" style="float: none" nowrap><b>End Date :</b></td><td align="left"><%=endDate%></td>
                        </tr>
               <% } else if(projectType.equals("Travel")){ %>
                      <tr><td colspan="4"><b>For your project listed below, please answer the certification questionnaire:</b></td></tr>
                        <tr><td align="right" width="15%" style="float: none" nowrap><b>Travel # :</b></td>
                            <td style="float: none" align="left" width="30%"><%=moduleItemKey%></td>
                            <td width="15%" style="float: none"></td>
                            <td style="float: none"></td>
                                </tr>
                        <tr><td align="right" width="15%" style="float: none" nowrap><b>Title:</b></td>
                            <td colspan="3" style="float: none" align="left" width="30%"><%=pjctTitle%></td>
                        <%--<td width="15%" style="float: none"></td>
                            <td style="float: none"></td>--%>
                        </tr>
                        <tr>
                            <td align="right" width="15%" style="float: none" nowrap><b>Start Date :</b></td><td style="float: none" align="left" width="30%"><%=startDate%></td>
                            <td align="right" width="23%" style="float: none" nowrap><b>End Date :</b></td><td align="left"><%=endDate%></td>
                        </tr>
               <% } %>


<%--      code added for displaying project details...ends--%>
                    <tr style="background-color:#6E97CF;" >
                        <td colspan="4">
                    <h1 style="background-color:#6E97CF;color:#FFFFFF;float:left;font-size:14px;font-weight:bold;margin: 0;padding: 2px 0 2px 10px;position: relative;text-align: left;">Disclosure : Certification</h1>
                </td>
                    </tr>
                    <tr><td>&nbsp;</td></tr>
                    <tr>
                        <td colspan="4">
                        <b>Certification:</b><br/>
                     <%-- The information provided for this Financial Interest Disclosure, including responses to Yes/No questions, list of pertinent financial entities, and disclosure of possible conflict with my sponsored activity, is an accurate and current statement of my reportable outside interests and activities.--%>
   <bean:message bundle="coi" key="AnnualDisclosure.Certification.Text"/>
                    </td>
                    </tr>
                    <tr>
                        <td align="center" colspan="4"><br/>
                            <html:button property="Save" onclick="javaScript:onCertify();" styleClass="clsavebutton" style="width:150px;">
                            Certify
                            </html:button>
                            <html:button onclick="javaScript:exitToCoi();" property="Save" styleClass="clsavebutton" style="width:150px;">
                               Quit
                            </html:button>
                    </td>
                    </tr>
                    <tr>
                        <td align="center" colspan="4"><br/><br/>
                    Last Updated: <bean:write name="coiv2Certification" property="updateTimeStampNew" format="yyyy-MM-dd  hh:mm a"/>
                        </td></tr>
                      <tr>
                        <td align="center" colspan="4"><br/>
                            Thank you for your cooperation.</td></tr>
                      <tr>
                        <td align="center" colspan="4"><br/>
                            When you are done click on the 'Logout' button above right</td></tr>
                    </table>
                
               </div>

        </html:form>
<%--</body>--%>
</html:html>