<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ include file= "/coeuslite/mit/utils/CoeusContextPath.jsp"  %>
<%@ page import="java.util.List,edu.mit.coeuslite.utils.CoeusDynaBeansList,java.util.Vector,edu.utk.coeuslite.propdev.bean.EPSProposalHeaderBean"%>
<jsp:useBean  id="questionDetails"  scope="session" class="java.util.Vector"/>
<jsp:useBean  id="questionExplanation"  scope="session" class="java.lang.String"/>
<jsp:useBean  id="questionPolicy"  scope="session" class="java.lang.String"/>
<jsp:useBean  id="questionRegulation"  scope="session" class="java.lang.String"/>

<html:html>
<%
  CoeusDynaBeansList list = (CoeusDynaBeansList) session.getAttribute("proposalInvCertifyList");
   List lstQuestionAnswer = (Vector) list.getList();
  EPSProposalHeaderBean  proposalHeaderBean   = (EPSProposalHeaderBean) session.getAttribute("epsProposalHeaderBean");
  String sponsorCode = (String) proposalHeaderBean.getSponsorCode();
  String sponsorName = (String) proposalHeaderBean.getSponsorName();
  String proposalNumber = (String) session.getAttribute("proposalNumber"+session.getId());
  String mode=(String)session.getAttribute("mode"+session.getId());
  String personName = (String) session.getAttribute("proposalInvName");
  String popUp = (String) session.getAttribute("popUp");
  String moreQuestionExist = (String) session.getAttribute("moreQuestionsExist");
  
  session.removeAttribute("Summary_Flag");
  boolean modeValue=false;
  if(mode!=null && !mode.equals("")) {
      if(mode.equalsIgnoreCase("display")){
              modeValue=true;
      }
      
     
  }%>
<head>
<title>JSP Page</title>
<html:base/>   
       
 <script language='javascript'>
 var errValue = false;
   function saveQuestions(){   
        document.proposalInvCertifyList.action = "<%=request.getContextPath()%>/saveInvKeyPersonsCertify.do";
        document.proposalInvCertifyList.submit();    
   }
   
     //return to summary proposal
    function close_action_summary(){
        document.proposalInvCertifyList.action = "<%=request.getContextPath()%>/displayProposal.do?proposalNo=<%=proposalNumber%>";
        document.proposalInvCertifyList.submit();
         // window.close();
       }
     
    function close_action(){
        document.proposalInvCertifyList.action = "<%=request.getContextPath()%>/getInvKeyPersons.do";
        document.proposalInvCertifyList.submit();
        //window.close();

    }
         

    
     function showQuestion(questionId)
         { 
            var link = "getMoreQuestions.do?questionId="+questionId;
            var winleft = (screen.width - 650) / 2;
            var winUp = (screen.height - 450) / 2;  
            var win = "scrollbars=1,resizable=1,width=790,height=450,left="+winleft+",top="+winUp
            sList = window.open('<bean:write name='ctxtPath'/>'+"/"+link, "list", win);
         }
         
     function call(){
        return validate();
     }
     
     function printCertify() {
        window.open('<bean:write name='ctxtPath'/>'+"/printCertification.do");
     }
    
 </script>
    
</head>
<body>
    <html:form action="/getInvKeyPersonsCertify.do">
       
     <% if(popUp !=null && popUp.equals("close")) { %>
             <script> 
               document.proposalInvCertifyList.action = "<%=request.getContextPath()%>/getInvKeyPersons.do";
               document.proposalInvCertifyList.submit();                                 
            </script>            
    <%} %>
<table width="100%" height="100%"  border="0" cellpadding="0" cellspacing="0" class='table'>
<tr>
    <td>
        <table width="100%"  align=center border="0" cellpadding="0" cellspacing="0" class="tableheader">
        <tr>
            <td>
                <bean:message bundle="proposal" key="proposalInvCertify.certifyFor"/>  <%=personName%>
            </td>
        </tr>
        </table>    
    </td>
</tr>
<tr align='right'>
    
     <td>
         <% if(request.getParameter("index")!=null && request.getParameter("index").equalsIgnoreCase("summary")){
             %>
                <html:link href="javascript:close_action_summary();">
                <u><bean:message bundle="proposal" key="proposalInvCertify.backToSummary"/> </u>
                </html:link>
             <% } else { %>
            <html:link href="javascript:close_action();" onclick="return call();">
                <u><bean:message bundle="proposal" key="proposalInvCertify.returnToInvestigator"/> </u>
            </html:link>
             <% } %>
        
    </td> 
</tr>
<tr>
    <td class='copybold'>
        <font color='red'>
            <logic:messagesPresent> 
            <script>errValue = true; </script>
            <html:errors header="" footer=""/>
            </logic:messagesPresent> 
          <logic:messagesPresent message = "true"> 
          <script>errValue = true; </script>
            <html:messages id="message" message="true" property="needToFill" bundle="proposal">                
                <li><bean:write name = "message"/></li>
             </html:messages>
            <html:messages id="message" message="true" property="errMsg" bundle="proposal">                
            <script>errValue = false;</script>
               <li><bean:write name = "message"/></li>
            </html:messages>                 
           </logic:messagesPresent>           
        </font>
    </td>
</tr>
<tr>
   <td class='copybold'> 
         <bean:message bundle="proposal" key="generalInfoProposal.proposalNumber"/> : <%=proposalNumber%>
   </td>
</tr>   
<tr>
   <td class='copybold'>
        <bean:message bundle="proposal" key="proposalInvCertify.sponsor"/> : <%=(sponsorCode !=null ? sponsorCode : "") %>  <%= (sponsorName !=null ? " : "+sponsorName : "")%>
   </td>
</tr>   

<tr>
    <td height='10'> </td>
</tr>
<tr>
    <td align='left' >
        
        <table width="99%" height="90%" align=center  border="0" cellpadding="0" cellspacing="0" class='tabtable'>
      
        <tr>
            <td>
            <table width="100%"  align=center  border="0" cellpadding="0" cellspacing="0">
            <tr height='20' class='theader'>
                <td width='10%' class='copybold'>
                      <bean:message bundle="proposal" key="proposalInvCertify.code"/>
                </td>
                <td width='50%' class='copybold'>
                      <bean:message bundle="proposal" key="ynq.question"/>
                </td>
                <td width='21%' class='copybold'>
                      <bean:message bundle="proposal" key="proposalInvCertify.answer"/>
                </td>
                <td width='5%'> &nbsp; </td>
            </tr>
            </table>
            </td>
        </tr>
         <%  
                    String strBgColor = "#DCE5F1";                    
                    int count = 0;
             %>
        <logic:iterate id="dynaFormData" name="proposalInvCertifyList" property="list" type="org.apache.struts.action.DynaActionForm" indexId="index" scope="session">
        <%int numberOfAnswers = Integer.parseInt(dynaFormData.get("noOfAnswers").toString());          
          String questionId = dynaFormData.get("questionId").toString();
        %>
        <%
                if (count%2 == 0) 
                    strBgColor = "#D6DCE5"; 
                else 
                    strBgColor="#DCE5F1"; 
         %>
        <tr bgcolor="<%=strBgColor%>" valign="top">
            <td>
                <table width="100%" align=center  border="0" cellpadding="2" cellspacing="1">
                    <tr>
                            <td width='10%' class='copybold'>
                                    <bean:write name="dynaFormData" property="questionId"/>
                            </td>
                            <td  width='50%' class='copybold'>
                                    <bean:write name="dynaFormData" property="description"/>
                            </td>

                            <td width='7%' class='copybold'>
                                <html:radio name="dynaFormData" property="answer" value="Y" indexed="true"  onclick="dataChanged()" disabled="<%=modeValue%>"/> 
                                &nbsp;<bean:message bundle="proposal" key="ynq.yes"/>
                            </td>
                            <td width='7%' class='copybold'>
                                <html:radio name="dynaFormData" property="answer" value="N" indexed="true" onclick="dataChanged()"   disabled="<%=modeValue%>"/>
                                &nbsp;<bean:message bundle="proposal" key="ynq.no"/>
                            </td>                
                            <td width='7%' class='copybold'>
                            <%if(numberOfAnswers==3) {%>
                                <html:radio name="dynaFormData" property="answer" value="X" indexed="true" onclick="dataChanged()" disabled="<%=modeValue%>"/>
                                &nbsp;<bean:message bundle="proposal" key="ynq.n/a"/>
                            <%} else {%>
                                &nbsp;
                            <%}%>
                            </td>
                             <%
                                 String viewLink = "javascript:showQuestion('"+questionId+"')";                                 
                             %>
                            <td width='5%'> 
                                <html:link href="<%=viewLink%>"> <bean:message bundle="proposal" key="proposalInvKeyPersons.more"/> </html:link>
                            </td>                       
                        
                    </tr>            
                </table>
            </td>
        </tr>
         <% count++;%>
           <html:hidden name="dynaFormData" property="dateReqFor" />
        </logic:iterate>
            
        </table>
    </td>
</tr>
   
<tr>  
    <td class='savebutton'>  
    <%if(!modeValue) {%>             
        <html:button property="ok" value="Save" styleClass="clsavebutton" onclick="javaScript:saveQuestions()"/>
     <% }  %>   &nbsp;&nbsp;&nbsp;
<!-- JM 5-31-2011 removed per 4.4.2, 4-2-2015 re-enabling for testing -->
     <html:button property="printCertifyButton" value="Print Certification" styleClass="clsavebutton" onclick="javaScript:printCertify()"/>
<!-- JM END -->
    </td>   
   
</tr>      
 
<tr>
    <td height='10'> </td>
</tr>
</table>

</html:form>
  
<script>
      DATA_CHANGED = 'false';
      if(errValue) {
            DATA_CHANGED = 'true';
      }
      LINK = "<%=request.getContextPath()%>/saveInvKeyPersonsCertify.do";
      FORM_LINK = document.proposalInvCertifyList;
      PAGE_NAME = "Proposal Investigator Certify";
      function dataChanged(){
        DATA_CHANGED = 'true';
      }
</script>
</body>
</html:html>
