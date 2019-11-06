<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ include file= "/coeuslite/mit/utils/CoeusContextPath.jsp"  %>

<jsp:useBean id="proposalType" scope="session" class="java.util.Vector" />
<jsp:useBean id="activityType" scope="session" class="java.util.Vector" />
<jsp:useBean id="noticeOfOppr" scope="session" class="java.util.Vector" />
<jsp:useBean id="proposalNumber" scope="session" class="java.lang.String" />
<jsp:useBean id="user" scope="session" class="edu.mit.coeus.bean.UserInfoBean" />
<jsp:useBean id= "createUser" scope="session" class="java.lang.String"/>
<jsp:useBean id="updateTimestamp" scope="request" class="java.lang.String" />
<jsp:useBean id="createTimestamp" scope="session" class="java.lang.String" />
<jsp:useBean id="actionType" scope="request" class="java.lang.String" />
<jsp:useBean id="unitNumber" scope="session" class="java.lang.String" />
<jsp:useBean id="unitName" scope="session" class="java.lang.String" />
<jsp:useBean id="creationStatDesc" scope="session" class="java.lang.String" />
<jsp:useBean id= "updUser" scope="session" class="java.lang.String"/>
<jsp:useBean id="optionsNSFCodes" scope="session" class="java.util.Vector" />

<jsp:useBean id= "propInHierarchy" scope="session" class="java.lang.String"/>
<jsp:useBean id="proposalAwardTypes" scope="session" class="java.util.Vector" />
<html:html>
  <head>
    <title>CoeusLite</title>
    <style>
        .cltextbox-color{ font-weight: normal; width: 220px}
    </style>
     <script language="javascript" type="text/JavaScript" src="<bean:write name='ctxtPath'/>/coeuslite/mit/utils/scripts/coeusLiteCalendar.js"></script>
    <script>
                var errValue = false;
                var errLock = false;
                function open_search_window(link,windowName){
                    var winleft = (screen.width - 650) / 2;
                    var winUp = (screen.height - 450) / 2;  
                    var win = "scrollbars=1,resizable=1,width=800,height=450,left="+winleft+",top="+winUp
                    if(windowName == "Award"){
                        sList = window.open('<bean:write name='ctxtPath'/>'+"/"+link, "list", win);
                    }else if(windowName == "Sponsor"){
                        win = "scrollbars=1,resizable=1,width=800,height=450,left="+winleft+",top="+winUp
                        sList = window.open('<bean:write name='ctxtPath'/>'+"/"+link, "list", win);
                    }else if(windowName == "primeSponsor"){
                        win = "scrollbars=1,resizable=1,width=800,height=450,left="+winleft+",top="+winUp
                        sList = window.open('<bean:write name='ctxtPath'/>'+"/"+link, "list", win);
                    }else if(windowName == "Institute"){
                        win = "scrollbars=1,resizable=1,width=800,height=450,left="+winleft+",top="+winUp
                        sList = window.open('<bean:write name='ctxtPath'/>'+"/"+link, "list", win);                    
                    }
                    if (parseInt(navigator.appVersion) >= 4) {
                        window.sList.focus(); 
                        }
                }
                
                function fetch_Data(result,searchType){
                    dataChanged();
                    if(searchType == "Person Search"){
                        document.generalInfoProposal.contactFirstName.value = result["FIRST_NAME"] ;
                        document.generalInfoProposal.contactLastName.value = result["LAST_NAME"]; 
                        var email = result["EMAIL_ADDRESS"];
                        var phone = result["OFFICE_PHONE"];
                        if(email == "null"){                       
                        document.generalInfoProposal.contactEmail.value ="";
                        }else{
                        document.generalInfoProposal.contactEmail.value = result["EMAIL_ADDRESS"];
                        }
                        if(phone == "null"){
                         document.generalInfoProposal.contactPhone.value ="";
                        }else{
                        document.generalInfoProposal.contactPhone.value = result["OFFICE_PHONE"];
                        }
                    }else if(searchType == "Award Search"){                        
                        document.generalInfoProposal.awardNumber.value = result["MIT_AWARD_NUMBER"] ;
                    }else if(searchType == "Sponsor Search"){                        
                        document.generalInfoProposal.sponsorCode.value = result["SPONSOR_CODE"] ;
                        document.generalInfoProposal.sponsorName.value = result["SPONSOR_NAME"] ;
                    }else if(searchType == "Prime Sponsor Search"){                         
                        document.generalInfoProposal.primeSponsorCode.value = result["SPONSOR_CODE"] ;
                        document.generalInfoProposal.primeSponsorName.value = result["SPONSOR_NAME"] ;
                    }else if(searchType == "Proposal Search"){                        
                        document.generalInfoProposal.continuedFrom.value = result["PROPOSAL_NUMBER"];
                    }
                 }
                 
             function validateForm(form) { 
                var var_init_begin_date = new Date(form.startDate.value.substring(6,10),
                                                   form.startDate.value.substring(0,2)-1,
                                                   form.startDate.value.substring(3,5));           

                var var_init_end_date = new Date(form.endDate.value.substring(6,10),
                                                 form.endDate.value.substring(0,2)-1,
                                                 form.endDate.value.substring(3,5));

                var init_difference = var_init_end_date.getTime() - var_init_begin_date.getTime(); 
                var validate = true;         

                 //return validateGeneralInfoProposal(form); 

                if(validateGeneralInfoProposal(form)){
                   if (Math.round(init_difference) < Math.round(1) && Math.round(init_difference)!= Math.round(0)){                        
                        alert("<bean:message bundle="proposal" key="generalInfoProposal.invalidDate"/>");
                        form.startDate.focus();
                        return false;
                    }
                    if( document.generalInfoProposal.sponsorCode.value == "<bean:message bundle="proposal" key="generalInfoProposal.NIHSponsorCode"/>"){
                        if(document.generalInfoProposal.title.value.length >81){
                            alert("<bean:message bundle="proposal" key="generalInfoProposal.title.lengthIFNIH"/>");
                            form.title.focus();
                            return false;
                        }                   
                    }
                }else{
                    return false;
                }            
            }
            
   //Added for COEUSDEV-178:Clicking on ok button causes multiple submissions
   function disableSaveButton(){
        document.generalInfoProposal.Save.disabled=true;
   }       
            
    </script>
    <html:javascript formName="generalInfoProposal"
                dynamicJavascript="true" 
                staticJavascript="true"/>
      <html:base/> 

  </head>
  <body>  
  <% String mode=(String)session.getAttribute("mode"+session.getId()); 
           boolean modeValue=false;
           if(mode!=null && !mode.equals("")) {   
            if(mode.equalsIgnoreCase("display")){
                modeValue=true;
             }
            }
           String inCompleteImage= request.getContextPath()+"/coeusliteimages/incomplete.gif";
           String completeImage= request.getContextPath()+"/coeusliteimages/complete.gif";  
           String noneImage= request.getContextPath()+"/coeusliteimages/none.gif";    
           
            String propbudgetStatus = (String)session.getAttribute("proposalBudgetStatus");
            
            String narrativeStatus = (String)session.getAttribute("proposalNarrativeStatus");
            //Added for displaying heirarchy image - start
            String parentImage = request.getContextPath()+"/coeusliteimages/parent.gif";
            String childImage = request.getContextPath()+"/coeusliteimages/child.gif";
            String InHierarchy = (String)session.getAttribute("propInHierarchy");
            System.out.println("propInHeirarchy : "+InHierarchy);
            InHierarchy = (InHierarchy==null) ? "" : InHierarchy;
            String isParent = (String)session.getAttribute("proposalIsParent");
            System.out.println("isParent : "+isParent);
            isParent = (isParent==null) ? "" : isParent;
            //End
    %>
    <html:form  action="/generalInfoProposal.do" method="post" onsubmit="disableSaveButton()">   
    <table width="100%" height="100%"  border="0" cellpadding="0" cellspacing="0" class="table">
      <tr class='theader'>
          <td  width='70%'>
            <bean:message bundle="proposal" key="generalInfoProposal,information"/>&nbsp&nbsp;
          </td>
          <td width='30%' align=center class='copybold' nowrap>
          <%if(InHierarchy.equals("Y")){%>
                <%if(isParent.equals("Y")){%>
                Proposal Hierarchy <html:img height='15' align="center" src="<%=parentImage%>" />
                <%}else{%>
                Proposal Hierarchy <html:img height='15' align="center" src="<%=childImage%>" />
          <%}}%>
          &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
         <%if((propbudgetStatus == null) || propbudgetStatus.equalsIgnoreCase("N")){%>
                <bean:message bundle="proposal" key="generalInfoProposal.BudgetStatus"/><html:img height='15' align="center" src="<%=noneImage%>" />
            <%}else if(propbudgetStatus.equalsIgnoreCase("I")){%>
                 <bean:message bundle="proposal" key="generalInfoProposal.BudgetStatus"/><html:img height='15' align="center" src="<%=inCompleteImage%>" />
            <%}else if(propbudgetStatus.equalsIgnoreCase("C")){%>
                 <bean:message bundle="proposal" key="generalInfoProposal.BudgetStatus"/><html:img height='15' align="center" src="<%=completeImage%>" />
            <%}%>
            &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
            <%if((narrativeStatus == null) || narrativeStatus.equalsIgnoreCase("N")){%>
                <bean:message bundle="proposal" key="generalInfoProposal.NarrativeStatus"/><html:img height='15' align="center" src="<%=noneImage%>" />
            <%}else if(narrativeStatus.equalsIgnoreCase("I")){%>
                <bean:message bundle="proposal" key="generalInfoProposal.NarrativeStatus"/><html:img height='15' align="center" src="<%=inCompleteImage%>" />
            <%}else if(narrativeStatus.equalsIgnoreCase("C")){%>
                <bean:message bundle="proposal" key="generalInfoProposal.NarrativeStatus"/><html:img height='15' align="center" src="<%=completeImage%>" />
            <%}%>
        </td>
      </tr>
      <tr>
        <td align="left" colspan='2'  valign="top">
            <div id="helpText" class='helptext'>            
                    <bean:message bundle="proposal" key="helpTextProposal.GeneralInfo"/>  
            </div>
            <font color="red">*</font> <bean:message bundle="proposal" key="generalInfoProposal.mandatory"/>
        </td>
        
      </tr>
      
      <tr>
      <td class='copy' align="left" colspan='2'>
      <font color='red'>
      <logic:messagesPresent>
        <script>errValue = true;</script>
         <html:errors header="" footer=""/>
         </logic:messagesPresent>
         <logic:messagesPresent message="true">
        <script>errValue = true;</script>
          <html:messages id="message" message="true" property="budgetDoesnotExist" bundle="budget">
                <script>errValue = false;</script>
               <li><bean:write name = "message"/></li>
          </html:messages>
           <html:messages id="message" message="true" property="isNotValidSponsor" bundle="proposal">                
                <li><bean:write name = "message"/></li>
          </html:messages>
          <html:messages id="message" message="true" property="isNotValidAward" bundle="proposal">                
               <li><bean:write name = "message"/></li>
          </html:messages>
          <html:messages id="message" message="true" property="generalInfoProposal.error.invalidInstNo" bundle="proposal">                
               <li><bean:write name = "message"/></li>
          </html:messages>       
         <!-- If lock is deleted then show this message --> 
          <html:messages id="message" message="true" property="errMsg" bundle="proposal"> 
               <script>errLock = true;</script>
               <li><bean:write name = "message"/></li>
          </html:messages>
          <!-- If lock is acquired by another user, then show this message -->
           <html:messages id="message" message="true" property="acqLock" bundle="proposal">
                <script>errLock = true;</script>
               <li><bean:write name = "message"/></li>
          </html:messages>
          
           <html:messages id="message" message="true" property="InvalidProposal" bundle="budget">                
               <li><bean:write name = "message"/></li>
          </html:messages>
           <html:messages id="message" message="true" property="isNotValidPrimeSponsor" bundle="proposal">                
               <li><bean:write name = "message"/></li>
          </html:messages>
          <html:messages id="message" message="true" property="noModifyAndViewRights" bundle="budget">                
               <li><bean:write name = "message"/></li>
          </html:messages>
          <!-- Added for case#2776 - Allow concurrent Prop dev access in Lite - start -->
          <html:messages id="message" message="true" property="cannotCreateBudget" bundle="budget">   
              <script>errValue = false;</script>
              <li><bean:write name = "message"/></li>
          </html:messages>                           
          <!-- Added for case#2776 - Allow concurrent Prop dev access in Lite - end -->
      </logic:messagesPresent>   
      </font>
      </td>
      </tr>
      
      <%-- COEUSQA-1433 - Allow Recall from Routing - Start --%>
      <tr>
          <td class='copy' align="left" colspan='2'>
              <font color='red'>
                  <%if(session.getAttribute("routingLockMessage") != null){%>
                  <%=(String)session.getAttribute("routingLockMessage")%>
                  <%session.removeAttribute("routingLockMessage");%>
                  
                  <%}%>
              </font>
          </td>
      </tr>
      <%-- COEUSQA-1433 - Allow Recall from Routing - End --%>
 
  <tr valign="top">
    <td height="45%" align="left" valign="top" colspan='2'>
      <!--<table width="100%"  border="0" align="center" cellpadding="0" cellspacing="0" class="tabtable">
        <%--tr>
          <td colspan='4' align="left" valign="top">
            <table width="100%" height="20%"  border="0" cellpadding="0" cellspacing="0" class="tableheader">
              <tr STYLE="background-image:url('<bean:write name='ctxtPath'/>/coeusliteimages/body_background.gif');border:0">
              <td>&nbsp;<bean:message bundle="proposal" key="generalInfoProposal.proposalDetails"/></td>
            </tr>
          </table>
          </td>
        </tr--%>
         <tr> 
          <td colspan='4'> -->                         
           <table  width="100%"  border="0" align="left" cellpadding="3" cellspacing="0" class="tabtable">
           
           <%--<tr>
            <td colspan='4' align="left" valign="top" class="theader">            
            <bean:message bundle="proposal" key="generalInfoProposal.proposalDetails"/>            
          </td>
           </tr>--%>
           
              <tr>
                <td align="right" class='copybold' width="10%"><font color="red" size='2'>*</font><bean:message bundle="proposal" key="generalInfoProposal.proposalType"/></td>
                <td align="left" width="20%">
                  <html:select  name="generalInfoProposal"  styleClass="cltextbox-medium" property="proposalType" disabled="<%=modeValue%>" onchange="dataChanged()"> 
                     <html:option value=""> <bean:message bundle="proposal" key="generalInfoProposal.pleaseSelect"/> </html:option>
                    <html:options collection="proposalType" property="code" labelProperty="description"/>
                  </html:select>          
                </td>  
                
                 <td align="right" class='copybold' width='10%'><font color="red" size='2'>*</font><bean:message bundle="proposal" key="generalInfoProposal.activityType"/></td>
                 <td width="22%">
                  <html:select  name="generalInfoProposal" styleClass="cltextbox-medium" property="activityType"  disabled="<%=modeValue%>" onchange="dataChanged()"> 
                   <html:option value=""> <bean:message bundle="proposal" key="generalInfoProposal.pleaseSelect"/> </html:option>
                    <html:options collection="activityType" property="code" labelProperty="description"/>
                  </html:select>
                 </td>  
              </tr>
                <tr>
                <td align="right" nowrap class='copybold'><font color='red' size="2">*</font><bean:message bundle="proposal" key="generalInfoProposal.startDate"/></td>
                <td>
                   <html:text property="startDate" styleClass="textbox" onchange="dataChanged()" disabled="<%=modeValue%>" maxlength="10"/> 
                    <%if(!modeValue){ %>
                      <a id="hlIRBDate" onclick="displayCalendarWithTopLeft('startDate',8,25)" tabindex="32" href="javascript:void(0);"
                                 runat="server"><img id="imgIRBDate" title="" height="16" alt="" onclick='dataChanged()' src="<bean:write name='ctxtPath'/>/coeusliteimages/cal.gif" width="16"
                                  border="0" runat="server">
                      </a>
                   <%}%>
                </td> 
                
                 <td align="right"  nowrap class='copybold'><font color='red' size="2">*</font><bean:message bundle="proposal" key="generalInfoProposal.endDate"/></td>
                  <td>
                   <html:text property="endDate" styleClass="textbox" onchange="dataChanged()" disabled="<%=modeValue%>" maxlength="10"/>
                     <%if(!modeValue){ %>
                     <a id="hlIRBDate" onclick="displayCalendarWithTopLeft('endDate',10,-150)" tabindex="32" href="javascript:void(0);"
                                 runat="server"><img id="imgIRBDate" title="" height="16" alt="" onclick='dataChanged()' src="<bean:write name='ctxtPath'/>/coeusliteimages/cal.gif" width="16"
                                  border="0" runat="server">
                     </a>
                    <%}%>
                </td>  
               </tr>    
                <tr>
                    <td nowrap align="right" class='copybold'><bean:message bundle="proposal" key="generalInfoProposal.Original"/>:</td>
                     <td class='copy'>
                        <html:text property="continuedFrom" styleClass="textbox" onchange="dataChanged()" disabled="<%=modeValue%>" />
                         <%if(!modeValue){ %>
                         <%--Commented and Added for COEUSQA-2428_CLONE -LIte - Prop Dev - Application does not return any rows while searching for Original proposal_Start --%>
                        <%--a class="search" href="javascript:open_search_window('/generalProposalSearch.do?type=<bean:message key="searchWindow.title.proposal" bundle="proposal"/>&search=true&searchName=PROPOSALSEARCH','Institute')"--%>
                        <a class="search" href="javascript:open_search_window('/generalProposalSearch.do?type=<bean:message key="searchWindow.title.proposal" bundle="proposal"/>&search=true&searchName=INSTITUTEPROPOSALSEARCH','Institute')">
                        <%--Commented and Added for COEUSQA-2428_CLONE -LIte - Prop Dev - Application does not return any rows while searching for Original proposal_End --%>    
                                <u><bean:message bundle="proposal" key="generalInfoProposal.search"/></u>
                            </a>
                        <%}%>                        
                     </td>                    
                    <td align="right" class='copybold'><bean:message bundle="proposal" key="generalInfoProposal.award"/> </td>
                     <td>                  
                        <html:text property="awardNumber" styleClass="textbox" disabled="<%=modeValue%>" onchange="dataChanged()" maxlength="10" />
                     <%if(!modeValue){ %>
                    <a class="search" href="javascript:open_search_window('/generalProposalSearch.do?type=<bean:message key="searchWindow.title.award" bundle="proposal"/>&search=true&searchName=ALL_AWARD_SEARCH','Award')">
                        <u><bean:message bundle="proposal" key="generalInfoProposal.search"/></u>
                    </a>
                    <%}%>
                     </td>        
                </tr>
                
               <tr>                                
                <td align="right" class='copybold'><font color='red' size="2">*</font><bean:message bundle="proposal" key="generalInfoProposal.agencySponsor"/></td>
                 <td colspan='2' class='copy' nowrap>
                   <html:text property="sponsorCode" styleClass="textbox" onchange="dataChanged()" disabled="<%=modeValue%>" maxlength="6"/> 
                   <%if(!modeValue){ %>
                  <a href="javascript:open_search_window('/generalProposalSearch.do?type=<bean:message key="searchWindow.title.sponsor" bundle="proposal"/>&search=true&searchName=SPONSORSEARCH','Sponsor')" class='search'>
                          <u><bean:message bundle="proposal" key="generalInfoProposal.search"/></u></a>
                    <%}%> &nbsp;&nbsp;<html:text property="sponsorName" styleClass="cltextbox-color" readonly="true"/>
                 </td>
               </tr>
               <tr>
                <td align="right" class='copybold'><bean:message bundle="proposal" key="generalInfoProposal.primeSponsor"/></td>
                 <td colspan='2'>
                   <html:text property="primeSponsorCode" styleClass="textbox" onchange="dataChanged()" disabled="<%=modeValue%>" maxlength="6"/>
                     <%if(!modeValue){ %>
                   <a href="javascript:open_search_window('/generalProposalSearch.do?type=<bean:message key="searchWindow.title.primesponsor" bundle="proposal"/>&search=true&searchName=SPONSORSEARCH','primeSponsor')" class='search'>
                        <u><bean:message bundle="proposal" key="generalInfoProposal.search"/></u></a>
                    <%}%>&nbsp;&nbsp;<html:text property="primeSponsorName" styleClass="cltextbox-color" readonly="true"/>  
                 </td>        
               </tr>
               <tr>
                <td align="right" nowrap  class='copybold'>
                    <bean:message bundle="proposal" key="generalInfoProposal.proposalDeadLineDate"/>
                </td>
                <td align="left"> 
                    <html:text property="deadLineDate" styleClass="textbox" onchange="dataChanged()" disabled="<%=modeValue%>" maxlength="10"/>
                      <%if(!modeValue){ %>
                     <a id="hlIRBDate" onclick="displayCalendarWithTopLeft('deadLineDate',8,25)" tabindex="32" href="javascript:void(0);"
                            runat="server"><img id="imgIRBDate" title="" height="16" alt="" onclick='dataChanged()' src="<bean:write name='ctxtPath'/>/coeusliteimages/cal.gif" width="16" 
                            border="0" runat="server">
                     </a>
                    <%}%>
                </td>
                 <td align="right" class='copybold' nowrap>
                    <html:radio property='deadlineType' value="R"  disabled="<%=modeValue%>" onclick="dataChanged()"/><bean:message bundle="proposal" key="generalInfoProposal.reciept"/> &nbsp;
                 </td>
                <td class='copybold' nowrap>
                    <html:radio property='deadlineType' value="P" disabled="<%=modeValue%>" onclick="dataChanged()"/><bean:message bundle="proposal" key="generalInfoProposal.postMarked"/>                    
                </td>  
               </tr>
               <tr>
               
               <td align=right class='copybold'><bean:message bundle="proposal" key="generalInfoProposal.nsfscienceCode"/></td>
                <td colspan='2'>
                 <html:select  name="generalInfoProposal"  styleClass="cltextbox-medium" property="nsfCode" disabled="<%=modeValue%>" onchange="dataChanged()"> 
                         <html:option value=""><bean:message bundle="proposal" key="generalInfoProposal.pleaseSelect"/></html:option>
                        <html:options collection="optionsNSFCodes" property="code" labelProperty="description"/>
                      </html:select>
                </td>
               </tr>
               
               <!--Added for Case # 2162 - adding Award Type - Start-->
               <tr>
               
               <td align=right class='copybold'>Anticipated Award Type<%--bean:message bundle="proposal" key="generalInfoProposal.nsfscienceCode"/--%></td>
                <td colspan='2'>
                 <html:select  name="generalInfoProposal"  styleClass="cltextbox-medium" property="anticipatedAwardType" disabled="<%=modeValue%>" onchange="dataChanged()"> 
                         <html:option value=""><bean:message bundle="proposal" key="generalInfoProposal.pleaseSelect"/></html:option>
                        <html:options collection="proposalAwardTypes" property="code" labelProperty="description"/>
                      </html:select>
                </td>
               </tr>
               <!--Added for Case # 2162 -adding Award Type - End-->
               <tr>
                <td align='right' class='copybold' nowrap>
                    <bean:message bundle="proposal" key="generalInfoProposal.sponsorProposalNumber"/>  
                </td> 
                <td class='copy' colspan='3'>
                    <html:text property="sponsorProposalNuber" styleClass="textbox-longer" onchange="dataChanged()" size="80" disabled="<%=modeValue%>" maxlength="70" />   
                </td>
               </tr>
               

                
                  <tr>
                    <td align="right"  class='copybold'><font color='red' size='2'>*</font><bean:message bundle="proposal" key="generalInfoProposal.title"/></td>                    
                    <td align="left" colspan='3'>
                        <%--div class='copyitalics'><bean:message bundle="proposal" key="generalInfoProposal.title.length"/></div--%>
                       <html:textarea property="title"  styleClass="textbox-longer" onchange="dataChanged()"  disabled="<%=modeValue%>" />                       
                       
                    </td>  
                   </tr>
                   <tr>
                        <td align="right"  class='copybold'><bean:message bundle="proposal" key="generalInfoProposal.programTitle"/></td>   
                        <td align="left" colspan='3' height='1%'>
                           <%--div class='copyitalics'><bean:message bundle="proposal" key="generalInfoProposal.programTitle.length"/></div--%>
                           <html:textarea property="programAnnouncementTitle"  onchange="dataChanged()" styleClass="textbox-longer"   disabled="<%=modeValue%>" />

                           <script>
                            if(navigator.appName == "Microsoft Internet Explorer")
                            {
                                document.generalInfoProposal.title.cols=104;
                                document.generalInfoProposal.title.rows=2;
                                document.generalInfoProposal.programAnnouncementTitle.cols=104;
                                document.generalInfoProposal.programAnnouncementTitle.rows=2;
                            }
                           </script>  
                        </td>  
                   </tr>

                    <tr>
                        <td align="right" class='copybold' width='15%'><bean:message bundle="proposal" key="generalInfoProposal.proposalInResponse"/></td>
                        <td>
                           <html:select  name="generalInfoProposal"  styleClass="cltextbox-medium" property="noticeOfOppr" disabled="<%=modeValue%>" onchange="dataChanged()"> 
                           <html:option value=""> <bean:message bundle="proposal" key="generalInfoProposal.pleaseSelect"/> </html:option>
                            <html:options collection="noticeOfOppr" property="code" labelProperty="description"/>
                          </html:select>                
                        </td>
                         <td align="right" class='copybold'><bean:message bundle="proposal" key="generalInfoProposal.subContract"/></td>
                        <td align="left">                        
                           <html:checkbox property='subcontractFlag' value="Y" disabled="<%=modeValue%>" onclick="dataChanged()"/> 
                        </td> 
                   </tr>
                   
                    <tr>
                        <td align="right" class='copybold'><bean:message bundle="proposal" key="generalInfoProposal.opportunityId"/></td>
                        <td align="left">
                           <html:text property="programAnnouncementNumber" styleClass="textbox" onchange="dataChanged()" disabled="<%=modeValue%>" maxlength="50" />               
                        </td>
                        <td align="right" class='copybold'><bean:message bundle="proposal" key="generalInfoProposal.cfdaCode"/></td>
                        <td>
                           <html:text property="cfdaCode" styleClass="textbox" disabled="<%=modeValue%>" onchange="dataChanged()" maxlength="6" onblur="formatCfda()"/>               
                        </td>
                   </tr>
                  
                    <tr>
                        <td align="right" width='10%' class='copybold'><bean:message bundle="proposal" key="generalInfoProposal.agencyProgramCode"/></td>
                        <td>
                           <html:text property="agencyProgramCode" styleClass="textbox" onchange="dataChanged()" disabled="<%=modeValue%>" maxlength="50" />               
                        </td>  
                        <td align="right" width='15%' class='copybold'><bean:message bundle="proposal" key="generalInfoProposal.agencyDivCode"/></td>
                        <td align="left">
                           <html:text property="agencyDivCode" onchange="dataChanged()" styleClass="textbox" disabled="<%=modeValue%>" maxlength="50" />               
                        </td>
                        
                   </tr>
            <!--#Case COEUSQA-3951-->
                    <tr>
                        <td align="right" width='10%' class='copybold'><bean:message bundle="proposal" key="generalInfoProposal.agencyRoutingIdentifier"/></td>
                        <td>
                           <html:text property="agencyRoutingIdentifier" styleClass="textbox" onchange="dataChanged()" disabled="<%=modeValue%>" maxlength="50" />
                        </td>  
                     <%--  <logic:present name="isS2SSubTypIs_3"> COEUSQA-3951 commented because, currently there is no need for S2SSubmissionType checking--%>
                        <td align="right" width='18%' class='copybold'><bean:message bundle="proposal" key="generalInfoProposal.previousGGTrackingID"/></td>
                        <td align="left">
                           <html:text property="previousGGTrackingID" onchange="dataChanged()" styleClass="textbox" disabled="<%=modeValue%>" maxlength="15" />               
                        </td>
                     <%--  </logic:present> --%>
                   </tr>
        <!--#Case COEUSQA-3951-->
               </table>
          <!--</td>
         </tr>
         <%--if(!modeValue){ %>
          <tr>
            <td align="center">
                <html:submit property="Save" value="Save" styleClass="clbutton"/>
            </td>
           </tr>
         <%}--%>
        </table>-->
       </td>
      </tr>
      <%if(!modeValue){ %>
          <tr class='table'>
            <td colspan='4' class='savebutton'>
                <html:submit property="Save" value="Save" styleClass="clsavebutton"/>
            </td>
           </tr>
     <%}%>    
      <html:hidden property="organizationId"/>
      <html:hidden property="performingOrganizationId"/>
      <html:hidden property="acType"/>
      <html:hidden property="updateTimestamp"/>
      <html:hidden property="proposalNumber"/>
      <html:hidden property="statusCode"/>
      <html:hidden property="creationStatusCode"/>
      <html:hidden property="templateFlag"/>
      <html:hidden property="unitNumber"/>
      <html:hidden property="unitName"/>
      <html:hidden property="mailingAddresId"/>
      <html:hidden property="createUser"/>
      <html:hidden property="budgetStatus"/>
     </table>        
  </html:form>
  <script>
      function formatCfda() {
        //If it is already 6 chars and already has . at 3rd index. do nothing.
        var cfda = document.generalInfoProposal.cfdaCode.value;
        if(cfda.length == 5 && cfda.indexOf(".") == -1) {
            var pre = cfda.substring(0, 2);
            var post = cfda.substring(2, 5);
            document.generalInfoProposal.cfdaCode.value = pre+"."+post;
        }
      }
      formatCfda();
  </script>
  <script>
      DATA_CHANGED = 'false';
      if(errValue && !errLock) {
            DATA_CHANGED = 'true';
      }
      LINK = "<%=request.getContextPath()%>/generalInfoProposal.do";
      FORM_LINK = document.generalInfoProposal;
      PAGE_NAME = "<bean:message bundle="proposal" key="generalInfoProposal,information"/>";
      function dataChanged(){
        <%if(!modeValue){ %>
            DATA_CHANGED = 'true';
        <%}%>
      }
      linkForward(errValue);
  </script>
  <script>
      var help = '<bean:message bundle="proposal" key="helpTextProposal.GeneralInfo"/>';
      help = trim(help);
      if(help.length == 0) {
            document.getElementById('helpText').style.display = 'none';
      }
      function trim(s) {
            return s.replace( /^\s*/, "" ).replace( /\s*$/, "" );
      }  
  </script>  
    </body>  
</html:html>