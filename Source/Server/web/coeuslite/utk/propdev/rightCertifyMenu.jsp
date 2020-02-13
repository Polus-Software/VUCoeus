<%--
    Document   : rightCertifyMenu
    Created on : Dec 16, 2010, 3:25:49 PM
    Author     : anishk
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
   "http://www.w3.org/TR/html4/loose.dtd">
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ include file= "/coeuslite/mit/utils/CoeusContextPath.jsp"  %>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JSP Page</title>
        <%
        String proposalNumber = (String)session.getAttribute("proposalNumber");
        
        %>

         <script language="javascript">

          function certifyInvestigator(personId, proposalNum, personName) {

           document.rightPersonCertifyBean.action = "<%=request.getContextPath()%>/toGetRoledQuestionnaire.do?propPersonId="+personId+"&proposalNumber="+proposalNum+"&personName="+personName;

           document.rightPersonCertifyBean.submit();

           }
        function fnProposalSummary(proposalNum) {            
           document.rightPersonCertifyBean.action = "<%=request.getContextPath()%>/displayProposal.do?proposalNo="+proposalNum;           
           document.rightPersonCertifyBean.submit(); 
        }
         function fnProposalDetails(proposalNum) {
           document.rightPersonCertifyBean.action = "<%=request.getContextPath()%>/getGeneralInfo.do?proposalNumber="+proposalNum;
           document.rightPersonCertifyBean.submit(); 
        }
        function fnProposalInvestigator() {
           document.rightPersonCertifyBean.action = "<%=request.getContextPath()%>/getInvKeyPersons.do";
           document.rightPersonCertifyBean.submit(); 
        }
        function fnCoiModule() {
           document.rightPersonCertifyBean.action = "<%=request.getContextPath()%>/coi.do?Menu_Id=004";
           document.rightPersonCertifyBean.submit(); 
        }
        
        </script>
    </head>
    <body>        
    <logic:notPresent name="InvalidPersonFromPremium">
    <logic:notEqual name="InvalidPersonFromPremium" value="true">
<table width="200px" align="left" height="575px"  border="0" cellpadding="0" cellspacing="5" class="table">

      <tr class="rowLine"><%int index=1;%>

      <td align="left" valign="top">
                          <table width="100%"   border="0" align="left" cellpadding="2" cellspacing="0" class="table">
         <% String strBgColor_key = "#DCE5F1";  //BGCOLOR=EFEFEF  FBF7F7
                          int count_key = 0;%>
                      <html:form action="/toGetRoledQuestionnaire">

                      <logic:present name="ppcProposalInvKeyData">
                        <logic:notPresent name="frmSummary">
                           <logic:notPresent name="hidePersons">
                               <logic:notEmpty name="ppcProposalInvKeyData">
                           <tr class = "menuHeaderName">                            
                            <td colspan="4">Proposal Persons</td>                            
                          </tr>

                      <logic:iterate id="propInvBean" name="ppcProposalInvKeyData" type="org.apache.struts.validator.DynaValidatorForm">
                       <% if (count_key%2 == 0)
                            strBgColor_key = "#D6DCE5";
                         else
                            strBgColor_key="#DCE5F1";%>

                <%   //System.out.println("Hello=>personId=>"+propInvBean.get("personId"));%>

                      <%
                        String piFlag = (String)propInvBean.get("principalInvestigatorFlag");
                        String multiPIFlag = (String)propInvBean.get("multiPIFlag");
                        String isSelected = (String)propInvBean.get("isSelected");
                      %>

                          <% String invKeyRole = "2";   // default to key person role
                             if (piFlag!=null && (piFlag.equals("Y") || piFlag.equals("N"))) {
                                invKeyRole = "1";       // default to co-PI
                                if (piFlag.equals("Y")) {
                                   invKeyRole = "0";    // set to be PI but when pass it to delete_data (javascription function), the 'value'
                                                        // will not be set to "0"; must use special check in InvKeyPersonAction (Server side)
                                }
                             }%>
                 <%-- <tr bgcolor="<%=strBgColor_key%>" onmouseover="className='TableItemOn'" onmouseout="className='TableItemOff'">
                 <td class="copy">--%>
                 <tr>
                     <td width='10%'  align="left" valign="top" class = "coeusMenu"></td>
                     <td height="80%" align="left" valign="top" class = "coeusMenu"  colspan="2">

                             <%  String certifyPersonName = (String) propInvBean.get("personName");
                             //If certifyPersonName contains ' use ` instead so that javascript doesn't think its string concatenation character
                                  certifyPersonName = certifyPersonName.replace('\'','`');
                                  String certifyRight = (String) session.getAttribute("CERTIFY_RIGHTS_EXIST");

                              %>

                     <%--<a href="#" > <%=propInvBean.get("personName")%></a>


--%>
                        <%if (certifyRight !=null && (certifyRight.equals("YES") )) {
                            if( piFlag.equals("Y") ) {%> <!--  || piFlag.equals("N") -->

                                <a href="javascript:certifyInvestigator('<%=propInvBean.get("personId")%>','<%=propInvBean.get("proposalNumber")%>','<%=certifyPersonName%>')"> <%=propInvBean.get("personName")%>(PI) </a>
                            <%}

                         else{%>
                          <a href="javascript:certifyInvestigator('<%=propInvBean.get("personId")%>','<%=propInvBean.get("proposalNumber")%>','<%=certifyPersonName%>')"> <%=propInvBean.get("personName")%> </a>
                         <%}%>
                       <% }else{%>
                          <a href="javascript:certifyInvestigator('<%=propInvBean.get("personId")%>','<%=propInvBean.get("proposalNumber")%>','<%=certifyPersonName%>')"> <%=propInvBean.get("personName")%> </a>
                         <%}%>

                    </td>
                    <td width="10%" style="color: #6D0202;font-size: 12px;font-weight: bold;" class = "coeusMenu">                    
                        <%if(isSelected!=null && isSelected.equalsIgnoreCase("Y")){%>
                        <bean:message key="menu.selected"/> 
                     <%}%>
                    </td>
                 </tr>

                      </logic:iterate>
                                    </logic:notEmpty>
                                </logic:notPresent>
                            </logic:notPresent>
                      </logic:present>
                 </html:form>
     <!-- Questionnairesss -->
     <logic:present name="ProposalRoles">


         <logic:present name="notInProposal">
             <logic:notEqual value="false" name="notInProposal">
     <logic:present name="QuestMenu">
 <%--     <html:link page ="<%=linkValue%>" styleClass="copy">Proposal Summary</html:link>      <logic:notPresent name="Is_From_ppc_main">
             <logic:empty name="QuestMenu">
                 <div style="color:black;margin-left: 10%;">&nbsp;&nbsp;&nbsp;No Questionnaire for your role </div>
             </logic:empty>
         </logic:notPresent>--%>
 <logic:notEmpty name="QuestMenu">
                 <%String link="";%>
                 <tr class = "menuHeaderName">                     
                        <td colspan='4' >Certification</td>                   
                   </tr>
                   <jsp:useBean id="QuestMenu" scope="session" class="java.util.Vector" />
                  <logic:iterate name="QuestMenu" id="proposalMenuBean" type="edu.mit.coeuslite.utils.bean.MenuBean">
                              <%
                              String linkName =  proposalMenuBean.getMenuName();
                              String linkValue = proposalMenuBean.getMenuLink();
                              String groupName = proposalMenuBean.getGroup();
                              String menuId =    proposalMenuBean.getMenuId();
                              boolean isMenuSaved = proposalMenuBean.isDataSaved();%>

                                      <tr>
                                          <td width='10%'  align="left" valign="top" class = "coeusMenu">
                                            <logic:equal name="proposalMenuBean" property="dataSaved" value="true">
                                      <img src="<bean:write name='ctxtPath'/>/coeusliteimages/img_saved.gif" width="19" height="19">
                                        </logic:equal>
                                          </td>
                                        <td width='60%'  align="left" valign="middle" class = "coeusMenu"  colspan="3" >
                             <html:link page ="<%=linkValue%>" styleClass="copy">
                               <%=linkName%></html:link>
                                         </td>                                        
                                     </tr>

                  </logic:iterate>
 </logic:notEmpty>
              </logic:present>
             </logic:notEqual>
         </logic:present>
     </logic:present>                                      
        <tr class = "menuHeaderName">            
          <td colspan='4' > Go To</td>
        </tr>
        <tr>
            <td width='10%'  align="left" valign="top" class = "coeusMenu"></td>
            <td colspan='3'  align="left" valign="middle" class = "coeusMenu">  
                <a href="javascript:fnProposalSummary('<%=proposalNumber%>');">Proposal Summary</a> 
            </td> 
        </tr>  
        <tr>
            <td width='10%'  align="left" valign="top" class = "coeusMenu"></td>
            <td colspan='3'  align="left" valign="middle" class = "coeusMenu">
                 <a href="javascript:fnProposalDetails('<%=proposalNumber%>');">Full Proposal Details</a> 
            </td>
        </tr> 
        <tr>
            <td width='10%'  align="left" valign="top" class = "coeusMenu"></td>
            <td colspan='3'  align="left" valign="middle" class = "coeusMenu">
                 <a href="javascript:fnProposalInvestigator();">Investigator Details</a> 
            </td> 
        </tr> 
        <logic:present name="showCoiInMenu">
        <tr>
            <td width='10%'  align="left" valign="top" class = "coeusMenu"></td>
            <td colspan='3'  align="left" valign="middle" class = "coeusMenu">
                 <a href="javascript:fnCoiModule();">COI Module</a> 
            </td>
        </tr>
        </logic:present>        
       </table>
       </td></tr></table>

  </logic:notEqual>
  </logic:notPresent>
    </body>
</html>
