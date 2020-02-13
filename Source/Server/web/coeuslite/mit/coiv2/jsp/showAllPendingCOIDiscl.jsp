<%-- 
    Document   : showAllPendingCOIDiscl
    Created on : Sep 2, 2011, 10:14:39 AM
    Author     : twinkle
--%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
 <%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/coeus-utils.tld" prefix="coeusUtils" %>
<%@ page import="java.util.Vector,edu.mit.coeuslite.coiv2.beans.CoiDisclosureBean,edu.mit.coeuslite.coiv2.services.CoiCommonService,java.util.Date"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
<title>C O I</title>
<%String path = request.getContextPath();%>
<script src="<%=path%>/coeuslite/dartmouth/utils/scripts/divSlide.js"></script>
<script src="<%=path%>/coeuslite/dartmouth/utils/scripts/Balloon.js"></script>
<link href="<%=path%>/coeuslite/dartmouth/utils/css/coeus_styles.css" rel="stylesheet" type="text/css"/>
<link href="<%=request.getContextPath()%>/coeuslite/mit/coiv2/css/layout.css" rel="stylesheet" type="text/css" />
<link href="<%=request.getContextPath()%>/coeuslite/mit/coiv2/css/styles.css" rel="stylesheet" type="text/css" />
<link href="<%=request.getContextPath()%>/coeuslite/mit/coiv2/css/collapsemenu.css" rel="stylesheet" type="text/css" />
</head>
<script src="js/jquery.js" type="text/javascript"></script>
<script type="text/javascript">
function selectProj(index){
    var a = "imgtoggle"+index;
//    alert(a);
//    alert("=====index===="+index+"  "+document.getElementById(index));
    if(document.getElementById(index).style.visibility == 'visible'){
      document.getElementById(index).style.visibility = 'hidden';
      document.getElementById(index).style.height = "1px";
      document.getElementById(a).src="<%=path%>/coeusliteimages/plus.gif";
    }else{
     last();
     ds = new DivSlider();
     ds.showDiv(index,1000);
     document.getElementById(index).style.visibility = 'visible';
     document.getElementById(index).style.height = "100px";
      document.getElementById(a).src="<%=path%>/coeusliteimages/minus.gif";
    }
}

function selectProj1(index1){
    var a = "imgtoggle1"+index1;
    var divId = "div"+index1
//    alert(a);
//    alert("=====index===="+index+"  "+document.getElementById(index));
    if(document.getElementById(divId).style.visibility == 'visible'){
      document.getElementById(divId).style.visibility = 'hidden';
      document.getElementById(divId).style.height = "1px";
      document.getElementById(a).src="<%=path%>/coeusliteimages/plus.gif";
    }else{
     last();
     ds = new DivSlider();
     ds.showDiv(divId,1000);
     document.getElementById(divId).style.visibility = 'visible';
     document.getElementById(divId).style.height = "100px";
      document.getElementById(a).src="<%=path%>/coeusliteimages/minus.gif";
    }
}
</script>
<body>
    <%
        String statusChanged = "";

        if(request.getAttribute("changedStatus") != null) {
             statusChanged=(String)request.getAttribute("changedStatus");
        }
    %>
<table id="bodyTable" class="table" style="width: 985px;" border="0" align="center">
<tr style="background-color:#6E97CF;height: 22px;width: 985px;">
    <td colspan="5" style="background-color:#6E97CF;color:#FFFFFF;font-size:14px;font-weight:bold;margin: 0;padding: 2px 0 2px 10px;position: relative;text-align: left;"><strong>List Of All Pending Actions</strong></td>
</tr>
    <tr>
        <td colspan="5" style="background-color:#6E97CF;color:#173B63;font-size:12px;font-weight:bold;">Pending Actions with No Financial Entities</td>
    </tr>
<tr>
    <td style="background-color:#6E97CF;color:#173B63;font-size:12px;font-weight:bold;" width="2%"></td>
    <td style="background-color:#6E97CF;color:#173B63;font-size:12px;font-weight:bold;" width="30%"><strong>Owned By</strong></td>
    <td style="background-color:#6E97CF;color:#173B63;font-size:12px;font-weight:bold;" width="24%"><strong>Department</strong></td>
    <td style="background-color:#6E97CF;color:#173B63;font-size:12px;font-weight:bold;" width="15%"><strong>Last Update</strong></td>
    <td style="background-color:#6E97CF;color:#173B63;font-size:12px;font-weight:bold;"><strong>Expiration Date</strong></td>

</tr>
<logic:present name="entityNameList">
       <%
            session.removeAttribute("isEvent");
            String strBgColor = "#DCE5F1";
            Vector projectNameList = (Vector)request.getAttribute("entityNameList");
            int index = 0;
        %>
<logic:iterate id="pjtTitle" name="entityNameList">
     <%
                                if (index%2 == 0) {
                                strBgColor = "#D6DCE5";
                                } else {
                                strBgColor="#DCE5F1";
                                }
     %>

<%

             CoiDisclosureBean beanHeader = (CoiDisclosureBean) projectNameList.get(index);
             CoiCommonService coiCommonService = CoiCommonService.getInstance();
             String updateHeader = beanHeader.getUpdateTimestampNew();//coiCommonService.getFormatedDate(beanHeader.getUpdateTimestamp());
             String expireHeader = coiCommonService.getFormatedDate(beanHeader.getExpirationDate());

          if(beanHeader.getIsFEFlag()!=null && ( beanHeader.getIsFEFlag()== 2 || beanHeader.getIsFEFlag()== 0)) {

           %>

   <tr bgcolor="<%=strBgColor%>" id="row<%=index%>" class="rowLine" onmouseover="className='rowHover rowLine'" onmouseout="className='rowLine'" style="height: 20px;">
       <td>
   <img src='<%=request.getContextPath()%>/coeusliteimages/plus.gif' border='none' id="imgtoggle<%=index%>" name="imgtoggle<%=index%>" border="none" onclick="javascript:selectProj(<%=index%>);"/>
   <img src='<%=request.getContextPath()%>/coeusliteimages/minus.gif' style="display: none;" border='none' id="imgtoggleminus<%=index%>" name="imgtoggleminus<%=index%>" border="none" onclick="javascript:selectProj(<%=index%>);"/>
   </td>

   <td>
   <b><bean:write name="pjtTitle" property="userName"/></b>
   </td>

<td>
   <b><bean:write name="pjtTitle" property="department"/></b>
   </td>

   <td>
 <b><coeusUtils:formatDate name="pjtTitle" property="updateTimestampNew" formatString="yyyy-MM-dd  hh:mm a"/></b>
   </td>
   <td>
 <b><coeusUtils:formatDate name="pjtTitle" property="expirationDate" formatString="MM/dd/yyyy"/></b>
   </td>

   </tr>
<tr>
<td colspan="5">
<div id="<%=index%>" style="height: 1px;width: 985px;visibility: hidden;background-color: #9DBFE9;overflow-x: hidden; overflow-y: scroll;">
<table id="bodyTable" class="table" style="width: 985px;height: 100%;" border="0" >
<tr>
<td style="background-color:#6E97CF;color:#FFFFFF;font-size:12px;font-weight:bold;" width="18%">Disc.Event</td>
<td style="background-color:#6E97CF;color:#FFFFFF;font-size:12px;font-weight:bold;">Event #</td>
<td style="background-color:#6E97CF;color:#FFFFFF;font-size:12px;font-weight:bold;">Review Status</td>
<td style="background-color:#6E97CF;color:#FFFFFF;font-size:12px;font-weight:bold;"><strong>Last Update</strong></td>
<td style="background-color:#6E97CF;color:#FFFFFF;font-size:12px;font-weight:bold;"><strong>Submit Date</strong></td>
<td style="background-color:#6E97CF;color:#FFFFFF;font-size:12px;font-weight:bold;"><strong>&nbsp;&nbsp;&nbsp;</strong></td>
<td style="background-color:#6E97CF;color:#FFFFFF;font-size:12px;font-weight:bold;"><strong>&nbsp;&nbsp;&nbsp;</strong></td>
</tr>
<logic:present name="message">
<logic:equal value="false" name="message">
<tr>
<td colspan="7">
<font color="red">No disclosures found</font>
</td>
</tr>
</logic:equal>
</logic:present>
<%         int i = 0;
           Vector disclVector = (Vector) request.getAttribute("pjtEntDetView");
           boolean event=false;
          if(!event){




%>
    <logic:iterate id="pjtEntView" name="pjtEntDetView"  type="edu.mit.coeuslite.coiv2.beans.CoiDisclosureBean">
            <%
             boolean isFEPresent = false;
            CoiDisclosureBean projectName = (CoiDisclosureBean)projectNameList.get(index);
            request.getSession().removeAttribute("selectedPjct");
            request.getSession().removeAttribute("disclPjctModuleName");
            String name = projectName.getCoiDisclosureNumber();
           // request.getSession().setAttribute("selectedPjct", name);
            String projectType=projectName.getModuleName();
            request.getSession().setAttribute("pjctType", projectType);
             CoiDisclosureBean bean = (CoiDisclosureBean) disclVector.get(i);
             String moduleName = "";
             String name1 = projectName.getCoiDisclosureNumber();
             int code = bean.getModuleCode();
             String moduleKey=bean.getModuleItemKey();
             Integer seqNo= (Integer)bean.getSequenceNumber();
request.getSession().setAttribute("selectedPjctSeqNo", seqNo);
request.getSession().setAttribute("selectedPjct", moduleKey);
request.getSession().setAttribute("selectedPjctsModuleItemKeySeqNo", moduleKey);
request.getSession().setAttribute("frmDiscl", true);
if(moduleKey==null){
    moduleKey="";
}

                if(code == 1) {
                    moduleName = "Award";
                }
                else if(code == 2) {
                    moduleName = "Proposal";
                }
               else if(code == 3) {
                    moduleName = "IRB Protocol";
                }
                else if(code == 4) {
                    moduleName = "IACUC Protocol";
                }
                else if(code == 5) {
                    moduleName = "Annual";
                }
                else if(code == 6) {
                    moduleName = "Revision";
                }
                else if(code == 11) {
                    moduleName = "Proposal";
                }
                 else if(code == 12) {
                    moduleName = "Protocol";
                }

                else if(code == 13) {
                    moduleName = "Annual";
                }
                else if(code == 8) {
                    moduleName = "Travel";
                      }

               request.getSession().setAttribute("disclPjctModuleName", moduleName);
              coiCommonService = CoiCommonService.getInstance();
              String update = bean.getUpdateTimestampNew();//coiCommonService.getFormatedDate(bean.getUpdateTimestamp());
              String expire = coiCommonService.getFormatedDate(bean.getExpirationDate());
            if(bean.getIsFEPresent()!=null && bean.getIsFEPresent()== 0) {

                 isFEPresent = false;


  %>
        <logic:equal value="<%=name%>" property="coiDisclosureNumber" name="pjtEntView">
            <%
             event=true;
            String link1 = request.getContextPath()+"/getannualdisclosure.do?selected=current&param1=" + bean.getCoiDisclosureNumber() +"&param2=" + bean.getSequenceNumber()+"&param7="+ bean.getPersonId()+"&param5="+moduleName+"&param6="+"throughShowAllDiscl"+"&selectedPjct="+moduleKey+"&fromReview=showAllReview";
            if(moduleName!= null && moduleName.equals("Travel")){
                link1= request.getContextPath()+"/travelDetails.do?selected=current&param1=" + bean.getCoiDisclosureNumber()+"&param7="+ bean.getPersonId()+"&edit=true&view=true&selectedPjct="+moduleKey+"&sequenceNumber="+bean.getSequenceNumber()+"&param6="+"throughShowAllDiscl";
            }
            %>

<tr class="rowLineLight" onmouseover="className='rowHover rowLine'" onmouseout="className='rowLineLight'" height="22px">
<td>
<!--   <a href="/getannualdisclosure.do?&param1=" + bean.getCoiDisclosureNumber() +"&param2=" + bean.getSequenceNumber()+"&param7="+ bean.getPersonId()+"&param5="+bean.getModuleName()+"&param6="+"throughShowAllDiscl";>  <b><bean:write name="pjtEntView" property="moduleName"/></b></a>-->
<a href='<%=link1%>'>  <b><%=moduleName%></b></a>
   </td>
<td>
 <a href='<%=link1%>'><b><bean:write name="pjtEntView" property="moduleItemKey"/></b></a>
</td>
   <td>
 <a href='<%=link1%>'><b><bean:write name="pjtEntView" property="reviewStatus"/></b></a>
</td>
<td>
 <a href='<%=link1%>'><b><coeusUtils:formatDate name="pjtEntView" property="updateTimestampNew" formatString="yyyy-MM-dd  hh:mm a"/></b></a>
</td>
<td>
 <a href='<%=link1%>'><b><coeusUtils:formatDate name="pjtEntView" property="updateTimestampNew" formatString="MM/dd/yyyy"/></b></a>
</td>
<%--    <td>
        <b><bean:write name="pjtEntView" property="updateUser"/></b>
    </td>--%>
<!-- <td width="12%">
       <a href="<%=link1%>"> <b><%=expire%></b></a>
    </td>-->

<% String linkPrint = "/approveddisclosureprint.do?selected=current&fromReview=showAllReview&param1=" + bean.getCoiDisclosureNumber() +"&param2=" + bean.getSequenceNumber()+"&param7="+ bean.getPersonId();%>
<td width=""><html:link action='<%=linkPrint%>' target="_blank">Print</html:link></td>
 <td width="">     <%if(!isFEPresent){%>
         <%
            String linkAppr = request.getContextPath()+"/updateStatus.do?&param1=" + bean.getCoiDisclosureNumber() +"&param2=" + bean.getSequenceNumber()+"&fromReviewList=true&isFRmListApprv=true&frmList=annualDiscl";
         %>
     <a href="<%=linkAppr%>">Approve</a>
      <%}%>
   </td>
</tr>
    </logic:equal>
         <% }i++;%>
    </logic:iterate><%}%>
    <%if(!event){%>
    <tr>
<td colspan="7">
<font color="red">No disclosures found</font>
</td>
</tr>
    <%}%>
</table>
</div></td>
    </tr>
 <%
 }
index++;
 %>
</logic:iterate>
<%request.getSession().setAttribute("lastIndex",index);%>
</logic:present>

 <tr>
        <td colspan="5" style="background-color:#6E97CF;color:#173B63;font-size:12px;font-weight:bold;">Pending Actions with Financial Entities</td>
    </tr>
<tr>
    <td style="background-color:#6E97CF;color:#173B63;font-size:12px;font-weight:bold;" width="2%"></td>
    <td style="background-color:#6E97CF;color:#173B63;font-size:12px;font-weight:bold;" width="30%"><strong>Owned By</strong></td>
    <td style="background-color:#6E97CF;color:#173B63;font-size:12px;font-weight:bold;" width="24%"><strong>Department</strong></td>
    <td style="background-color:#6E97CF;color:#173B63;font-size:12px;font-weight:bold;" width="15%"><strong>Last Update</strong></td>
    <td style="background-color:#6E97CF;color:#173B63;font-size:12px;font-weight:bold;"><strong>Expiration Date</strong></td>
</tr>

    <logic:present name="entityNameList">
        <%
            session.removeAttribute("isEvent");
            String strBgColor1 = "#DCE5F1";
            Vector projectNameList1 = (Vector)request.getAttribute("entityNameList");
            int index1 = 0;
        %>
        <logic:iterate id="pjtTitle1" name="entityNameList">
            <%
                if (index1%2 == 0) {
                strBgColor1 = "#D6DCE5";
                } else {
                strBgColor1="#DCE5F1";
                }
            %>
            <%

            CoiDisclosureBean beanHeader1 = (CoiDisclosureBean) projectNameList1.get(index1);

            if(beanHeader1.getIsFEFlag() !=null && (beanHeader1.getIsFEFlag() == 2 ||  beanHeader1.getIsFEFlag() == 1) ){
             %>
   <tr bgcolor="<%=strBgColor1%>" id="row1<%=index1%>" class="rowLine" onmouseover="className='rowHover rowLine'" onmouseout="className='rowLine'" style="height: 20px;">
       <td>
   <img src='<%=request.getContextPath()%>/coeusliteimages/plus.gif' border='none' id="imgtoggle1<%=index1%>" name="imgtoggle1<%=index1%>" border="none" onclick="javascript:selectProj1(<%=index1%>);"/>
   <img src='<%=request.getContextPath()%>/coeusliteimages/minus.gif' style="display: none;" border='none' id="imgtoggleminus1<%=index1%>" name="imgtoggleminus1<%=index1%>" border="none" onclick="javascript:selectProj1(<%=index1%>);"/>
   </td>

   <td>
   <b><bean:write name="pjtTitle1" property="userName"/></b>
   </td>

    <td>
       <b><bean:write name="pjtTitle1" property="department"/></b>
       </td>

       <td>
     <b><coeusUtils:formatDate name="pjtTitle1" property="updateTimestampNew" formatString="yyyy-MM-dd  hh:mm a"/></b>
       </td>
       <td>
     <b><coeusUtils:formatDate name="pjtTitle1" property="expirationDate" formatString="MM/dd/yyyy"/></b>
       </td>

   </tr>
    <tr>
        <td colspan="5">
        <div id="div<%=index1%>" style="height: 1px;width: 985px;visibility: hidden;background-color: #9DBFE9;overflow-x: hidden; overflow-y: scroll;">
        <table id="bodyTable" class="table" style="width: 985px;height: 100%;" border="0" >
        <tr>
       <td style="background-color:#6E97CF;color:#FFFFFF;font-size:12px;font-weight:bold;" width="18%">Disc.Event</td>
       <td style="background-color:#6E97CF;color:#FFFFFF;font-size:12px;font-weight:bold;">Event #</td>
       <td style="background-color:#6E97CF;color:#FFFFFF;font-size:12px;font-weight:bold;">Review Status</td>
       <td style="background-color:#6E97CF;color:#FFFFFF;font-size:12px;font-weight:bold;"><strong>Last Update</strong></td>
       <td style="background-color:#6E97CF;color:#FFFFFF;font-size:12px;font-weight:bold;"><strong>Submit Date</strong></td>
       <td style="background-color:#6E97CF;color:#FFFFFF;font-size:12px;font-weight:bold;"><strong>&nbsp;&nbsp;&nbsp;</strong></td>
       <td style="background-color:#6E97CF;color:#FFFFFF;font-size:12px;font-weight:bold;"><strong>&nbsp;&nbsp;&nbsp;</strong></td>
        </tr>
        <logic:present name="message">
        <logic:equal value="false" name="message">
        <tr>
        <td colspan="7">
        <font color="red">No disclosures found</font>
        </td>
        </tr>
        </logic:equal>
        </logic:present>
        <%         int i = 0;
                   Vector disclVector = (Vector) request.getAttribute("pjtEntDetView");
        %>
            <logic:iterate id="pjtEntView" name="pjtEntDetView"  type="edu.mit.coeuslite.coiv2.beans.CoiDisclosureBean">
                 <%
                  boolean isFEPresent = false;
                    CoiDisclosureBean projectName = (CoiDisclosureBean)projectNameList1.get(index1);
                    request.getSession().removeAttribute("selectedPjct");
                    request.getSession().removeAttribute("disclPjctModuleName");
                    String name = projectName.getCoiDisclosureNumber();
                    String projectType=projectName.getModuleName();
                    request.getSession().setAttribute("pjctType", projectType);
                     CoiDisclosureBean bean = (CoiDisclosureBean) disclVector.get(i);
                     String moduleName = "";
                     int code = bean.getModuleCode();
                     String moduleKey=bean.getModuleItemKey();
                     Integer seqNo= (Integer)bean.getSequenceNumber();
                    request.getSession().setAttribute("selectedPjctSeqNo", seqNo);
                    request.getSession().setAttribute("selectedPjct", moduleKey);
                    request.getSession().setAttribute("selectedPjctsModuleItemKeySeqNo", moduleKey);
                    request.getSession().setAttribute("frmDiscl", true);
                    if(moduleKey==null){
                        moduleKey="";
                    }
                     if(code == 1) {
                            moduleName = "Award";
                        }
                        else if(code == 2) {
                            moduleName = "Proposal";
                        }
                       else if(code == 3) {
                            moduleName = "IRB Protocol";
                        }
                        else if(code == 4) {
                            moduleName = "IACUC Protocol";
                        }
                        else if(code == 5) {
                            moduleName = "Annual";
                        }
                        else if(code == 6) {
                            moduleName = "Revision";
                        }
                        else if(code == 11) {
                            moduleName = "Proposal";
                        }
                         else if(code == 12) {
                            moduleName = "Protocol";
                        }

                        else if(code == 13) {
                            moduleName = "Annual";
                        }
                        else if(code == 8) {
                         moduleName = "Travel";
                      }
                     request.getSession().setAttribute("disclPjctModuleName", moduleName);
                     CoiCommonService coiCommonService1 = CoiCommonService.getInstance();
                      String update = bean.getUpdateTimestampNew();//coiCommonService1.getFormatedDate(bean.getUpdateTimestamp());
                      String expire = coiCommonService1.getFormatedDate(bean.getExpirationDate());
                      if(bean.getIsFEPresent()!=null && bean.getIsFEPresent()== 1) {
                       isFEPresent = true;

                 %>
                 <logic:equal value="<%=name%>" property="coiDisclosureNumber" name="pjtEntView">
                    <% String link1 = request.getContextPath()+"/getannualdisclosure.do?selected=current&param1=" + bean.getCoiDisclosureNumber() +"&param2=" + bean.getSequenceNumber()+"&param7="+ bean.getPersonId()+"&param5="+moduleName+"&param6="+"throughShowAllDiscl"+"&selectedPjct="+moduleKey+"&fromReview=showAllReview";%>

                    <tr class="rowLineLight" onmouseover="className='rowHover rowLine'" onmouseout="className='rowLineLight'" height="22px">
                    <td>
                    <!--   <a href="/getannualdisclosure.do?&param1=" + bean.getCoiDisclosureNumber() +"&param2=" + bean.getSequenceNumber()+"&param7="+ bean.getPersonId()+"&param5="+bean.getModuleName()+"&param6="+"throughShowAllDiscl";>  <b><bean:write name="pjtEntView" property="moduleName"/></b></a>-->
                    <a href='<%=link1%>'>  <b><%=moduleName%></b></a>
                       </td>
                    <td>
                     <a href='<%=link1%>'><b><bean:write name="pjtEntView" property="moduleItemKey"/></b></a>
                    </td>
                       <td>
                     <a href='<%=link1%>'><b><bean:write name="pjtEntView" property="reviewStatus"/></b></a>
                    </td>
                    <td>
                     <a href='<%=link1%>'><b><coeusUtils:formatDate name="pjtEntView" property="updateTimestampNew" formatString="yyyy-MM-dd  hh:mm a"/></b></a>
                    </td>
                    <td>
                     <a href='<%=link1%>'><b><coeusUtils:formatDate name="pjtEntView" property="updateTimestampNew" formatString="MM/dd/yyyy"/></b></a>
                    </td>
                    <%--    <td>
                            <b><bean:write name="pjtEntView" property="updateUser"/></b>
                        </td>--%>
                    <!-- <td width="12%">
                           <a href="<%=link1%>"> <b><%=expire%></b></a>
                        </td>-->

                    <% String linkPrint = "/approveddisclosureprint.do?selected=current&fromReview=showAllReview&param1=" + bean.getCoiDisclosureNumber() +"&param2=" + bean.getSequenceNumber()+"&param7="+ bean.getPersonId();%>
                    <td width=""><html:link action='<%=linkPrint%>' target="_blank">Print</html:link></td>

                     <td width=""> &nbsp;
                       </td>
                    </tr>
            </logic:equal>
                 <% } i++;%>
            </logic:iterate>
        </table>
        </div>
        </td>
   </tr>
    <%
   }
   index1++;
 %>
        </logic:iterate>
        <%request.getSession().setAttribute("lastIndex1",index1);%>
    </logic:present>
    <logic:present name="noRightAtPerHomeUnit">
        <script language="javaScript">
            alert("You do not have the right to view this disclosure.");
            <%request.removeAttribute("noRightAtPerHomeUnit");%>
        </script>
    </logic:present>


    <%if(statusChanged.length() > 1){%>
            <logic:present name="message">
                <logic:equal value="true" name="message">
                    <%--<font color="red">You have successfully changed the disclosure status to <%= statusChanged%></font>--%>
                    <script>
                        alert("You have successfully changed the disclosure status to '<%= statusChanged%>' and completed the review.");
                    </script>
                </logic:equal>
                <logic:equal value="false" name="message">
                    <font color="red">Disclosure Not Updated</font>
                </logic:equal>
            </logic:present>
    <%}%>
</table>
</body>
<script>
    function last(){
      var lastinx = "<%=request.getSession().getAttribute("lastIndex")%>";
   //   alert(lastinx);
      for(var i=0;i<lastinx;i=i+1){
  //    alert(i);
      var b = "imgtoggle"+i;
      var c = "imgtoggle1"+i;
       var divId = "div"+i
    if(document.getElementById(i) != null) {
    if(document.getElementById(i).style.visibility == 'visible'){
      document.getElementById(i).style.visibility = 'hidden';
      document.getElementById(i).style.height = "1px";
      if(document.getElementById(b) != null) {
        document.getElementById(b).src="<%=path%>/coeusliteimages/plus.gif";
        }
    }
    }
    if(document.getElementById(divId) != null) {
    if(document.getElementById(divId).style.visibility == 'visible'){
      document.getElementById(divId).style.visibility = 'hidden';
      document.getElementById(divId).style.height = "1px";
      if(document.getElementById(c) != null) {
        document.getElementById(c).src="<%=path%>/coeusliteimages/plus.gif";
        }
    }
    }
}
}

</script>
</html>
