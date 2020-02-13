<%--
    Document  :&nbsp; awardGetInfo
    Created on:&nbsp; Dec 29, 2010, 2:16:19 PM
    Author    :&nbsp; vineetha
--%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/coeus-utils.tld" prefix="coeusUtils"%>
<%@ include file="/coeuslite/mit/utils/CoeusContextPath.jsp"%>
<%@page	import="java.util.Vector,edu.mit.coeuslite.coiv2.services.CoiCommonService,edu.mit.coeuslite.award.beans.AwardDisplayBean,edu.mit.coeuslite.award.beans.AwardInvestigatorsBean"%>

<html:html>
  <head>
      <%
String path = request.getContextPath();%>
    <title>CoeusLite</title>
    <style>
            #mbox{background-color:#6e97cf; padding:0px 8px 8px 8px; border:3px solid #095796;}
            #mbm{font-family:sans-serif;font-weight:bold;float:right;padding-bottom:5px;}
            #ol{background-image: url('../coeuslite/mit/utils/scripts/modal/overlay.png');}
            .dialog {display:none}

            * html #ol{background-image:none; filter:progid:DXImageTransform.Microsoft.AlphaImageLoader(src="../coeuslite/mit/utils/scripts/modal/overlay.png", sizingMethod="scale");}

        </style>
        <style>
            .deleteRow{font-weight:bold;color:#CC0000;background-color:white;}
            .addRow{font-weight:bold;background-color:white;}
            .rowHeight{height:25px;}
        </style>
      <script src="<%=request.getContextPath()%>/coeuslite/mit/utils/scripts/modal/modaldbox.js"></script>
    <style>
        .cltextbox-medium{ width: 160px; }
        .cltextbox-color { width: 160px; font-weight: normal; }
        .textbox { width: 160px; font-weight: normal; }
        .cltextbox-nonEditcolor { width: 160px; font-weight: normal; }
    </style>
    <title>CoeusLite</title>
    <script language="javascript" type="text/JavaScript" src="..\scripts\_js.js"></script>
    <script language="javascript" type="text/JavaScript" src="..\scripts\CheckForward.js"></script>

    <style type="text/css">
        .clsavebutton {
         border-right: gray 1px solid;
         border-top: #ffffff 1px solid;
         border-left: #ffffff 1px solid;
         border-bottom: dimgray 1px solid;
         background-color: #D6DFF7;
         width: 120px;
         font-size: 11px;
         font-weight: bold;
         font-family: Verdana, Arial, Helvetica, sans-serif;
         color:black;
}
        .cltextbox-color{ font-weight: normal; width: 220px}
    </style>
    <%--<link href="<%=request.getContextPath()%>/coeuslite/mit/utils/css/coeus_styles.css" rel="stylesheet" type="text/css" />--%>
     <script language="javascript" type="text/JavaScript" src="<bean:write name='ctxtPath'/>/coeuslite/mit/utils/scripts/coeusLiteCalendar.js"></script>
     <script type="text/javascript"> 
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

  function showDisclDialog(divId)
 {
           <logic:empty name="awardDisclDetails">
                   alert("This award has no investigators & keypersons");
          </logic:empty>
          <logic:notEmpty name="awardDisclDetails">
                width = document.getElementById(divId).style.pixelWidth;
                height = document.getElementById(divId).style.pixelHeight;
                sm(divId,width,height);
           </logic:notEmpty>
 document.getElementById("mbox").style.left="450";
 document.getElementById("mbox").style.top="250";
            }


           function save(i)
            {
                
                var success= true;
             
                if(success==true){
                    document.forms[0].action= '<%=path%>' +"/awardPrintnew.do?awdType="+i;
                    document.forms[0].target="_blank"; 
                    document.forms[0].submit();
                }

            }


    </script>
    <html:javascript formName="generalInfoProposal"
                dynamicJavascript="true"
                staticJavascript="true"/>
      <html:base/>

  </head>
  <body>
 
        <%--AwardDisplayBean award = (AwardDisplayBean) request.getAttribute("awardList");
         // Vector awardList= (Vector) request.getAttribute("awardList");--%>
        <%--<logic:notPresent name="awardListtmp">--%>

        

         <logic:present name="awardListtmp">
              <%
        String awardEffectiveDate="";
        String finalExpirationDate="";
          String obligeffDate ="";
         String obligexpDate ="";
         String preawardEffDate = "";
                // AwardDisplayBean apprvdDisclosuresBean = (AwardDisplayBean) (awardListtmp.get(0));
          AwardDisplayBean award1 = (AwardDisplayBean) request.getAttribute("awardListtmp");
                       CoiCommonService coiCommonService = CoiCommonService.getInstance();
                       if(award1.getawardEffDate() != null) {
                         awardEffectiveDate = award1.getawardEffDate();
                     }
                       if(award1.getfinalExpDate() != null) {
                       finalExpirationDate =award1.getfinalExpDate();
                        }
                          if(award1.getStartDate() != null) {
                         obligeffDate = award1.getStartDate();
                         }
                        if(award1.getEndDate() != null) {
                         obligexpDate = award1.getEndDate();
                         }
                       if(award1.getPreAwardEffDate() != null) {
                           preawardEffDate = award1.getPreAwardEffDate();
                           }
              %>
    <table width="100%" height="100%" border="0" cellpadding="2" cellspacing="0" class="table">
       <tr class="theader">
      <td colspan="3"><jsp:include page="/coeuslite/mit/award/jsp/awardInfoHeader.jsp" flush="true"/> </td>
       </tr>
    
  <table width="100%" height="100%" class="tableheader">
       <tr>
       <td>Effective Date and Personnel</td>
     <%--  <logic:present name="awardDisclDetails">--%>
       <td>
      Schedules and Special Reviews 

       </td>
     <%--  </logic:present>--%>
      </tr>
      <tr class="theader">
          <td valign="top" width="50%">
             
              <table  width="95%"  height="100%" border="0" align="left" cellpadding="0" cellspacing="0" class="tabtable">
                   <tr><td align="left" class='copybold' nowrap>&nbsp;&nbsp;Pre Award Effective Date  :</td>
                      <td >&nbsp;<%= preawardEffDate%> </td>
                    </tr>
                    <tr><td align="left" class='copybold' nowrap>&nbsp;&nbsp;Award Effective Date  :</td>
                      <td >&nbsp;<%= awardEffectiveDate%> </td>
                    </tr>
 <logic:present name="awardListtmp">
               <tr></tr>
              <tr>
                  <td align="left" class='copybold' width="30%" nowrap>&nbsp;&nbsp;Investigators :</td>
                  <td align="left" width="70%">
                      <%int count=0;
                      Vector invstList = new Vector();
                      String unitName = "";
                      if(request.getAttribute("awardInvestList") != null) {
                          invstList = (Vector)request.getAttribute("awardInvestList");
                          }
                      %>
                      <logic:present name="awardInvestList">
                          <logic:iterate id="investigators" name="awardInvestList">
                          <%

                            AwardInvestigatorsBean invstBean = (AwardInvestigatorsBean)invstList.get(count);
                            if(invstBean != null) {
                                unitName = invstBean.getUnitName();
                                }
                          %>
                              <%if(count>0)
    {%> </td>
              
              </tr><tr>
                  <td ></td>
                  <td>
                  <%;}
    count++;%>
  &nbsp;<bean:write name="investigators" property="fullname" /><%if(unitName != null && unitName.length() > 1) {%>(<%=unitName%>)<%}%>
                          </logic:iterate>
                      </logic:present> 
               </td>
              
              </tr>

              <%--for the key person listting--%>
                           <tr>
                  <td align="left" class='copybold' width="30%" nowrap>&nbsp;&nbsp;Key Persons :</td>
                  <td align="left" width="70%">
                      <% count=0;
                        Vector kpList = new Vector();
                        String kpUnitName = "";
                        if(request.getAttribute("awardKeyPersonsList") != null) {
                          kpList = (Vector)request.getAttribute("awardKeyPersonsList");
                        }
                      %>
                      <logic:present name="awardKeyPersonsList">
                          <logic:iterate id="keypersons" name="awardKeyPersonsList">
                            <%

                            AwardInvestigatorsBean kpBean = (AwardInvestigatorsBean)kpList.get(count);
                            if(kpBean != null) {
                                kpUnitName = kpBean.getUnitName();
                                }
                          %>
                              <%if(count>0)
    {%> </td>
              
              </tr><tr>
                <td ></td>
                  <td>
                  <%;}
    count++;%>
  &nbsp;<bean:write name="keypersons" property="fullname" /><%if(kpUnitName != null && kpUnitName.length() > 1) {%>(<%=kpUnitName%>)<%}%>
                          </logic:iterate>
                      </logic:present>
               </td>
               
              </tr>
              <%--<tr>
                  <td colspan="2" align="left">
                      &nbsp;  <a href="javascript:showDisclDialog('divDisclosureStaus')" >COI Disclosure Status</a>
                  </td>
              </tr>--%>
            
 </logic:present>
               </table>
        

       </td>
       
       <td valign="top" width="50%">
  <table  width="100%" height="100%"  border="0" align="left" cellpadding="3" cellspacing="2" class="tabtable">
      <logic:present name="awardListtmp">
        <tr></tr>
       
         
   <%
  AwardDisplayBean projectDetails = (AwardDisplayBean) request.getAttribute("awardListtmp");
 // projectDetails=(Vector) request.getSession().getAttribute("awardListtmp");
    AwardDisplayBean award=new AwardDisplayBean();

 //    award = (AwardDisplayBean)projectDetails.get(0);
  String apprvdEq= projectDetails.getApprvdEq();
  String apprvdsub= projectDetails.getApprvdsub();
  String apprvdfor= projectDetails.getApprvdfor();
    String paymnt= projectDetails.getPaymnt();
      String transfr= projectDetails.getTransfer();
        String cost= projectDetails.getCostsharing();
          String indir= projectDetails.getIndirectcost();
%>



        <tr></tr>

        <tr>  <td align="left" class='copybold' width="40%" nowrap>&nbsp;&nbsp;Approved Equipment
            </td>
            <td width="6%">
                <%
                if((apprvdEq!=null) && (apprvdEq.equalsIgnoreCase("N0"))){ %>


  <input type="checkbox"  name="apprvdEq"  disabled="true"/>
      <%} else {%>
  <input type="checkbox"  name="apprvdEq" checked="true" disabled="true"/>

      <%}%>


            </td>
            <td align="right" class='copybold' width="38%">&nbsp;&nbsp;Cost Sharing
                 </td>
            <td>
            <%
                if((cost!=null) && (cost.equalsIgnoreCase("N0"))) {

    %>

 <input type="checkbox"  name="cost"  disabled="true"/>
      <%} else {%>

  <input type="checkbox"  name="cost" checked="true" disabled="true"/>
      <%}%>
    </td>
        </tr>
        <tr>
       <td align="left" class='copybold'>&nbsp;&nbsp;Approved Subcontract
       </td>
       <td>
            <%
                if((apprvdsub!=null) && (apprvdsub.equalsIgnoreCase("N0"))) {

    %>
    <input type="checkbox"  name="apprvdsub"  disabled="true"/>
      <%} else {%>

   <input type="checkbox"  name="apprvdsub" checked="true" disabled="true"/>
      <%}%>
       </td>
       <td align="right" class='copybold'>&nbsp;&nbsp;Indirect Cost
       </td>
       <td>

            <%
                if((indir!=null) && (indir.equalsIgnoreCase("N0"))) {

    %>
<input type="checkbox"  name="indir"  disabled="true"/>
      <%} else {%>

 <input type="checkbox"  name="indir" checked="true" disabled="true"/>
      <%}%>
       </td>
        </tr>
          <tr>
       <td align="left" class='copybold'>&nbsp;&nbsp;Approved Foreign Trip
            </td>
            <td align="left">
            <%
                if((apprvdfor!=null) && (apprvdfor.equalsIgnoreCase("N0"))) {

    %>
   <input type="checkbox"  name="apprvdfor"  disabled="true"/>
      <%} else {%>

   <input type="checkbox"  name="apprvdfor" checked="true" disabled="true"/>
   <%}%></td>
              <td align="right" class='copybold'>&nbsp;&nbsp;Transfer Sponsor
       </td>
       <td>

           <%
                if((transfr!=null) && (transfr.equalsIgnoreCase("N0"))) {

    %>
  <input type="checkbox"  name="transfr"  disabled="true"/>
      <%} else {%>

    <input type="checkbox"  name="transfr" checked="true" disabled="true"/>
      <%}%>
       </td>
        </tr>

           <tr>
       <td class='copybold' align="left" >&nbsp;&nbsp;Payment Schedule
            </td>
            <td colspan="3" align="left">
            <%
                if((paymnt!=null) && (paymnt.equalsIgnoreCase("N0"))) {

    %>
 <input type="checkbox"  name="paymnt"  disabled="true"/>
      <%} else {%>
 <input type="checkbox"  name="paymnt" checked="true" disabled="true"/>
      <%}%>
       </td>
        </tr>

        <tr>
            <td colspan="4">
                &nbsp;
            </td>
        </tr>
        <tr>
            <td  class='copybold' align="left">&nbsp;&nbsp;Special Reviews:</td>
            <td class='copybold' align="left">Status</td>
            <td class='copybold' align="center">Protocol #</td>
            <td class='copybold' align="center" nowrap>Appr Date</td>
        </tr>
        
        <logic:present name="specialRevList">
            <logic:notEmpty name="specialRevList">
            <logic:iterate id="splRevList" name="specialRevList">
                <tr>
                    <td align="left">&nbsp;&nbsp;<bean:write name="splRevList" property="specialReview" /> </td>
                    <td align="left"><bean:write name="splRevList" property="approvalTypeCode" /></td>
                    <td align="center"><bean:write name="splRevList" property="protocolNumber" /></td>
                    <td align="center" nowrap><bean:write name="splRevList" property="approvalDate" /></td>
                </tr>
             
            </logic:iterate>
            </logic:notEmpty>
               <logic:empty name="specialRevList">
                   <tr><td colspan="4">
                  &nbsp; &nbsp;  No Special Reviews present
                       </td></tr>
               </logic:empty>
        </logic:present>
       
         <logic:notPresent name="specialRevList">
                   <tr><td colspan="4">
                  &nbsp; &nbsp;No Special Reviews present
                       </td></tr>
               </logic:notPresent>

                 </logic:present>
           </table></td>
      </tr>
  </table>

     <tr class="theader">
    <td colspan="2"><jsp:include page="/coeuslite/mit/award/jsp/ParentChild.jsp" flush="true"/> </td>
                </tr>
   
  </table>
         </logic:present>
          <logic:notPresent name="awardListtmp">
              <table>  <tr class="theader"><td  width="5%" colspan="10" height="23" align=center>
             Award Details not present

                      </td></tr></table>
          </logic:notPresent>
<%--code to display award disclosure details starts--%>
  <% String strBgColor = "#DCE5F1"; //BGCOLOR=EFEFEF  FBF7F7
   int count1 = 0;%>

   <%
         String title="";
         String awardNumber = (String)session.getAttribute("mitawardnumber");
         Vector disclHeaderDet = (Vector)session.getAttribute("awardDisclDetails");
         if(disclHeaderDet != null && disclHeaderDet.size()>0){
           for(int i=0;i<disclHeaderDet.size();i++){
               AwardDisplayBean awardDisplayBean =  (AwardDisplayBean)disclHeaderDet.get(i);
               title = (String)awardDisplayBean.getTitle();
           }
            if(title.length() > 45) {
                       title = title.substring(0,45)+"...";
                    }
                       if(title==null) {
                        title = "";
                     }
      }
    %>
   <div id="divDisclosureStaus" class="dialog" style="width:450px;overflow: hidden;position:absolute;">

         <logic:present name="awardDisclDetails">
                 <table width="450px"   border="0" cellpadding="0" cellspacing="0" class="table" align="center">
                  <tr>
                    <td colspan="4" align="left" valign="top">
                        <div class="tableheader">COI Disclosure Status</div>
                        <table width="100%" height="7"  border="0" cellpadding="1" cellspacing="1" class="table">
                             <tr><td height="10%" width="20%" align="left">Project # :</td><td width="80%" align="left"><%=awardNumber%></td></tr>
                             <tr><td width="20%" align="left">Title : </td><td width="80%" align="left"><%=title%></td></tr>
                        </table>
                    </td>
                   </tr>
                        <tr height="22">
                            <td valign="top"  width="33%"   class="theader" align="left">Person Name</td>
                           <%-- <td valign="top"  class="theader" align="left">Department</td>--%>
                            <td valign="top" width="33%"  class="theader" align="left">Role</td>
                            <td valign="top" class="theader" align="left">Disclosure Status</td>
                        </tr>
                        <% int index = 0;%>
                   <logic:iterate id="awardDisclDetailsList" name="awardDisclDetails">
                       <tr  bgcolor="<%=strBgColor%>" valign="top" onmouseover="className='TableItemOn'" onmouseout="className='TableItemOff'">
                           <td class="copy" align="left">
                               <logic:notEmpty name="awardDisclDetailsList" property="personName">
                               <bean:write name="awardDisclDetailsList" property="personName"/>
                               </logic:notEmpty>
                           </td>
         <%--               <td class="copy" align="left">  
                              <logic:notEmpty name="awardDisclDetailsList" property="department">
                              <bean:write name="awardDisclDetailsList" property="department"/> 
                              </logic:notEmpty>
                        </td>--%>
                        <td class="copy" align="left">
                             <logic:notEmpty name="awardDisclDetailsList" property="role">
                             <bean:write name="awardDisclDetailsList" property="role"/>
                             </logic:notEmpty>
                        </td>
                         <td class="copy" align="left">
                            <logic:notEmpty name="awardDisclDetailsList" property="disclosureStatus">
                            <bean:write name="awardDisclDetailsList" property="disclosureStatus"/>
                            </logic:notEmpty>
                            <logic:empty name="awardDisclDetailsList" property="disclosureStatus">
                                Not Disclosed
                            </logic:empty>
                        </td>
                           </tr>
                            <%count1++;%>
                           <%index++;%>
                           </logic:iterate>
                           <tr><td>&nbsp;</td></tr>
                            <tr class="copy">
                                <td align="center" colspan="3" >
                                    <input type="button"  value="Close" class="clsavebutton" onclick="hm('divDisclosureStaus')"/>
                                </td>
                               </tr>
                         </table>
                       </logic:present>
                     </div>
   <%--code to display award disclosure details ends--%>
      </body>
</html:html>   
      
