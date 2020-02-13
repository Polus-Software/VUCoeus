<%-- 
    Document   : coiV2InProgressMenu
    Created on : Feb 21, 2012, 9:13:04 AM
    Author     : x + iy
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
<%@ page import="edu.mit.coeuslite.coiv2.utilities.CoiConstants,edu.mit.coeuslite.coiv2.beans.CoiInfoBean"%>


<% String proposalNumber = (String)session.getAttribute("proposalNumber"+session.getId());
   String group="4";
   String mode = (String)session.getAttribute("mode") ;   
    boolean readOnly = false;
    if(mode != null && mode.equals("display")){
        readOnly = true;
    }
    String link = "";    
    String contextPath = request.getContextPath();
//check=approvedDisclosureview
    //String fromApprovedView=(String)request.getParameter("check");
    String fromApprovedView=null;
    if(session.getAttribute("checkPrint")!=null){
    fromApprovedView= (String)session.getAttribute("checkPrint");
    }
    String projectType = null;
    CoiInfoBean coiInfoBean = (CoiInfoBean)request.getSession().getAttribute("CoiInfoBean");
    if(coiInfoBean!=null){
        projectType = coiInfoBean.getProjectType();
    }
    
    String projectLabel = "By Projects";
    
    if(projectType != null && !(projectType.equalsIgnoreCase("Annual") || projectType.equalsIgnoreCase("Revision"))) {
        projectLabel="By Project";
    }

    boolean noQstnrPresent = false;

    if(session.getAttribute("noQuestionnaireForModule") != null){
        noQstnrPresent = (Boolean)session.getAttribute("noQuestionnaireForModule");
    }
%>



    <script>
             
             function checkCOI(value)
             {  
                 var path ="ATTACH";                 
                CLICKED_LINK = value;
                if(CHECK_ATTACH == path){
                if(validateSaveOrDelete()){
                return validateSavedInfo();
                }else{
                    return false;
                }
                }
                else{
                    return validateSavedInfo(); 
                }
             }
               function validateSaveOrDelete(){
                if(document.coiv2Attachment.docType.value==null || document.coiv2Attachment.docType.value=='')
                {
                    alert('Please select Document Type');
                    document.coiv2Attachment.docType.focus();
                    return false;
                }

                if(document.coiv2Attachment.description.value==null ||document.coiv2Attachment.description.value.replace(/^\s+/,'').replace(/\s+$/,'')=='')
                {
                    alert('Please enter Description');
                    document.coiv2Attachment.description.focus();
                    return false;
                }
                if(document.coiv2Attachment.fileName.value==null || document.coiv2Attachment.fileName.value=='')
                {
                    alert('Please select a File');
                    document.coiv2Attachment.fileName.focus();
                    return false;
                }
                var extdoc=format();
                if(extdoc=="exe" ||  extdoc=="ini" || extdoc=="bat"){
                    return false
                }else{
                return true;
            }
            }

   function format(){
              var filename = document.coiv2Attachment.document.value;
              var filelength = parseInt(filename.length) - 3;
              var fileext = filename.substring(filelength,filelength + 3);
               if( fileext=="exe" ||  fileext=="ini" || fileext=="bat" ){
                   alert("change upload document format");
                   return fileext;
               }
          }

        </script>
   <!-- <body class="body" style="height: 100%;background-color: #9DBE9;">-->
  
   <table class="table" align="left" width="100%" bgcolor="#9DBFE9">
                   <tr><td>
                           <table class="table" align="center" width="200" style="padding: 4px">
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
         <logic:notPresent  name="disableCoiMenu">
                        <tr class="rowline">
                            <%if(projectType!=null && projectType.equalsIgnoreCase("Travel")){ %>
                          <% link = "return checkCOI('"+request.getContextPath()+"/travelDetails.do?&sequenceNumber="+session.getAttribute("sequenceNumber")+"')";
                              String link2 ="/travelDetails.do?&sequenceNumber="+coiInfoBean.getSequenceNumber()+"&param1="+coiInfoBean.getDisclosureNumber()+"&param7="+coiInfoBean.getPersonId(); %>

                            <td width="16%"  height='16' align="left" valign="top" class = "coeusMenu">
                              <logic:present name="coiMenuDatasaved"><logic:equal name="coiMenuDatasaved" property="travelDataSaved" value="true">  <img src="<%=request.getContextPath()%>/coeusliteimages/img_saved.gif" width="19" height="19">
                              </logic:equal></logic:present>
                              </td>
                              <logic:present name="DiscViewTravel" scope="request">
                                  <td width="80%"  height='16' align="left" valign="middle" class = "selectedMenuIndicator" nowrap>
                                 <html:link action="<%=link2%>" onclick="<%=link%>"><font color="#6D0202" >Travel Details</font></html:link>

                             </td>
                           <td width='4%' align=right  class ="selectedMenuIndicator"><bean:message key="menu.selected"/> </td>
                           </logic:present>
                           <logic:notPresent name="DiscViewTravel" scope="request">
                               <td width="80%"  height='16' align="left" valign="middle" class="coeusMenu" nowrap>

                                <html:link action="<%=link2%>" onclick="<%=link%>">Travel Details</html:link>

                           </td>
                            <td width='4%' align=right  class ="selectedMenuIndicator"></td> 
                           </logic:notPresent></tr>
                        <% } %>
                        <%if(!noQstnrPresent) {%>
                        <tr class="rowline">
                            <td width="16%"  height='16' align="left" valign="top" class = "coeusMenu">
                                <logic:present name="coiMenuDatasaved"><logic:equal name="coiMenuDatasaved" property="questDataSaved" value="true">  <img width="19" height="19" border="0" src="<%=request.getContextPath()%>/coeusliteimages/img_saved.gif"> </logic:equal></logic:present>
                            </td>   
                              <logic:present name="DiscViewQtnr" scope="request">
                             <td width="80%"  height='16' align="left" valign="middle" class = "selectedMenuIndicator">
                                <html:link action="/coiQuestionnaire.do" onclick="<%=link%>"><font color="#6D0202">Questionnaires</font></html:link>

                             </td>
                           <td width='4%' align=right  class ="selectedMenuIndicator"> <bean:message key="menu.selected"/> </td>
                           </logic:present>
                           <logic:notPresent name="DiscViewQtnr" scope="request">
                              <td width="80%"  height='16' align="left" valign="middle" class ="coeusMenu">

                                <html:link action="/coiQuestionnaire.do" onclick="<%=link%>">Questionnaires</html:link>

                           </td>
                            <td width='4%' align=right  class ="selectedMenuIndicator"></td>
                           </logic:notPresent>
                        </tr> 
                        <% }else{%>
                            <tr class="rowline">
                                <td width="16%"  height='16' align="left" valign="top" class = "coeusMenu"></td>
                                <td width="80%"  height='16' align="left" valign="middle" class = "coeusMenu" style="text-decoration: none;color: gray">              
                                     Questionnaires
                                </td>
                               <td width='4%' align=right  class ="selectedMenuIndicator"></td>
                            </tr>
                        <%}%>
                <% link = "return checkCOI('"+request.getContextPath()+"/coiCentralAction.do"+"')";%>
              <%if(projectType == null || (projectType!=null && !projectType.equalsIgnoreCase("Travel"))){ %>
                <tr class="rowLine">
                            <td width="16%"  height='16' align="left" valign="top" class = "coeusMenu">
                                <logic:present name="coiMenuDatasaved"><logic:equal name="coiMenuDatasaved" property="prjDataSaved" value="true">  <img width="19" height="19" border="0" src="<%=request.getContextPath()%>/coeusliteimages/img_saved.gif"> </logic:equal></logic:present>
                            </td>                
                            <logic:present name="DiscViewByPrjt" scope="request">
                              <td width='80%'  align="left" valign="middle" class = "coeusMenu" >
                                  <html:link action="/coiCentralAction.do" onclick="<%=link%>"><font color="#6D0202"><%=projectLabel%></font></html:link>
                              </td>
                           <td width='4%' align=right  class ="selectedMenuIndicator" ><bean:message key="menu.selected"/> </td>
                           </logic:present>
                           <logic:notPresent name="DiscViewByPrjt" scope="request">
                            <td width='80%'  align="left" valign="middle" class = "coeusMenu" >
                           <html:link action="/coiCentralAction.do" onclick="<%=link%>"><%=projectLabel%></html:link>
                           </td>
                            <td width='4%' align=right  class ="selectedMenuIndicator"></td>
                           </logic:notPresent>
                        </tr>
                        <%}%>

                            <% link = "return checkCOI('"+request.getContextPath()+"/getAttachmentsCoiv2.do?operationType=MODIFY"+"')";%>
                        <tr class="rowLine">
                             <td width="16%"  height='16' align="left" valign="top" class = "coeusMenu">
                                 <logic:present name="coiMenuDatasaved"><logic:equal name="coiMenuDatasaved" property="attachDataSaved" value="true">   <img width="19" height="19" border="0" src="<%=request.getContextPath()%>/coeusliteimages/img_saved.gif">
                                     </logic:equal></logic:present>
                             </td>                 
                           <logic:present name="DiscViewAttchmt" scope="request">
                               <td width="80%"  align="left" valign="middle" class = "coeusMenu" >

                 <html:link action="/getAttachmentsCoiv2.do?operationType=MODIFY" onclick="<%=link%>"><font color="#6D0202">Attachments</font></html:link>

                              </td>
                           <td width='4%' align=right  class ="selectedMenuIndicator" ><bean:message key="menu.selected"/> </td>
                           </logic:present>
                           <logic:notPresent name="DiscViewAttchmt" scope="request">
                               <td width="80%"  align="left" valign="middle" class = "coeusMenu" >

                  <html:link action="/getAttachmentsCoiv2.do?operationType=MODIFY" onclick="<%=link%>">Attachments</html:link>

                              </td>
                            <td width='4%' align=right  class ="selectedMenuIndicator"></td>
                           </logic:notPresent>               

                        </tr>
                      
                         <% link = "return checkCOI('"+request.getContextPath()+"/getNotesCoiv2.do"+"')";%>
                        <tr class="rowLine">
                            <td width="16%"  height='16' align="left" valign="top" class = "coeusMenu">
                                <logic:present name="coiMenuDatasaved"> <logic:equal name="coiMenuDatasaved" property="noteDataSaved" value="true"> <img width="19" height="19" border="0" src="<%=request.getContextPath()%>/coeusliteimages/img_saved.gif"> </logic:equal></logic:present>
                            </td>
                           <logic:present name="DiscViewNotes" scope="request">
                           <td align="left" valign="middle" class = "coeusMenu">   
                         <html:link action="/getNotesCoiv2.do" onclick="<%=link%>"><font color="#6D0202">Notes</font></html:link>
                           </td>
                           <td width='4%' align=right  class ="selectedMenuIndicator" ><bean:message key="menu.selected"/> </td>
                           </logic:present>
                           <logic:notPresent name="DiscViewNotes" scope="request">
                            <td align="left" valign="middle" class = "coeusMenu" >
                 <html:link action="/getNotesCoiv2.do" onclick="<%=link%>">Notes</html:link>
                           </td>
                            <td width='4%' align=right  class ="selectedMenuIndicator"></td>
                           </logic:notPresent> 
                        </tr>

                         <tr class="rowline">
                               <% link = "return checkCOI('"+request.getContextPath()+"/getCertificationCoiv2.do?operationType=MODIFY"+"')";%>
                             <td width="16%"  height='16' align="left" valign="top" class = "coeusMenu">
                                 <logic:present name="coiMenuDatasaved"><logic:equal name="coiMenuDatasaved" property="certDataSaved" value="true"><img width="19" height="19" border="0" src="<%=request.getContextPath()%>/coeusliteimages/img_saved.gif"> </logic:equal></logic:present> 
                             </td>                
                            <logic:present name="DiscViewCertify" scope="request">
                           <td width="80%"  height='16' align="left" valign="middle" class = "coeusMenu">   
                 <html:link action="/getCertificationCoiv2.do?operationType=MODIFY" onclick="<%=link%>"><font color="#6D0202">Certifications</font></html:link>


                           </td>
                           <td width='4%' align=right  class ="selectedMenuIndicator" ><bean:message key="menu.selected"/> </td>
                           </logic:present>
                           <logic:notPresent name="DiscViewCertify" scope="request">
                           <td width="80%"  height='16' align="left" valign="middle" class = "coeusMenu">

                  <html:link action="/getCertificationCoiv2.do?operationType=MODIFY" onclick="<%=link%>">Certifications</html:link>


                           </td>
                            <td width='4%' align=right  class ="selectedMenuIndicator"></td>
                           </logic:notPresent>

                        </tr>
</logic:notPresent> 
<logic:present  name="disableCoiMenu">
    <tr class="rowline">
         <%if(projectType!=null && projectType.equalsIgnoreCase("Travel")){ %>
                <td width="16%"  height='16' align="left" valign="top" class = "coeusMenu"></td>
                <td width="80%"  height='16' align="left" valign="middle" class = "coeusMenu" style="text-decoration: none;color: gray">
                     Travel Details
                </td>
               <td width='4%' align=right  class ="selectedMenuIndicator"></td> <% } %>
            </tr>
            <tr class="rowline">
                <td width="16%"  height='16' align="left" valign="top" class = "coeusMenu"></td>
                <td width="80%"  height='16' align="left" valign="middle" class = "coeusMenu" style="text-decoration: none;color: gray">              
                     Questionnaires
                </td>
               <td width='4%' align=right  class ="selectedMenuIndicator"></td>
            </tr>          
        
             <tr class="rowLine"> 
                <td width="16%"  height='16' align="left" valign="top" class = "coeusMenu"></td>
                <td width="80%"  height='16' align="left" valign="middle" class = "coeusMenu" style="text-decoration: none;color: gray">              
                     <%=projectLabel%>
                </td>
               <td width='4%' align=right  class ="selectedMenuIndicator"></td>                   
            </tr> 
            <tr class="rowLine"> 
                <td width="16%"  height='16' align="left" valign="top" class = "coeusMenu"></td>
                <td width="80%"  height='16' align="left" valign="middle" class = "coeusMenu" style="text-decoration: none;color: gray">              
                     Attachments
                </td>
               <td width='4%' align=right  class ="selectedMenuIndicator"></td>                   
            </tr> 
            <tr class="rowLine"> 
                <td width="16%"  height='16' align="left" valign="top" class = "coeusMenu"></td>
                <td width="80%"  height='16' align="left" valign="middle" class = "coeusMenu" style="text-decoration: none;color: gray">              
                     By Notes
                </td>
               <td width='4%' align=right  class ="selectedMenuIndicator"></td>                   
            </tr> 
            
             <tr class="rowLine"> 
                <td width="16%"  height='16' align="left" valign="top" class = "coeusMenu"></td>
                <td width="80%"  height='16' align="left" valign="middle" class = "coeusMenu" style="text-decoration: none;color: gray">              
                     Certifications
                </td>
               <td width='4%' align=right  class ="selectedMenuIndicator"></td>                   
            </tr>           
</logic:present> 
             <tr> <td height="17"></td></tr>
             <tr class="rowline">
                 <% link = "return checkCOI('"+request.getContextPath()+"/coiHome.do?Menu_Id=004"+"')";%>
               <td width="16%"  height='16' align="left" valign="top" class = "coeusMenu"></td>
               <td width="80%"  height='16' align="left" valign="middle" class = "coeusMenu">
               <html:link action="/coiHome.do?Menu_Id=004" onclick="<%=link%>">MY COI Home</html:link>
               </td>
              <td width='4%' align=right  class ="selectedMenuIndicator" ></td>
             </tr>
         </table></td></tr>
               <tr bgcolor="#9DBFE9"><td style="height: 500;" colspan="3">&nbsp;</td></tr>
               </table>
          
