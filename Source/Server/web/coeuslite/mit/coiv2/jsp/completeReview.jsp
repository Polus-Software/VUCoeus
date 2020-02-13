<%--
    Document   : completeReview
    Created on : 20 Apr, 2012, 12:22:01 PM
    Author     : indhulekha
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
   "http://www.w3.org/TR/html4/loose.dtd">
<%@ include file= "/coeuslite/mit/utils/CoeusContextPath.jsp"%>
<%@ page import="java.util.Vector,edu.mit.coeus.coi.bean.ComboBoxBean"%>

<%@ taglib uri="/WEB-INF/struts-bean.tld"  prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld"  prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/coeus-utils.tld"  prefix="coeusUtils"%>

<%
    String path = request.getContextPath();
    String reviewingDisclNum = null;
    Integer reviewingSeqNum = null;
    String reviewerPersonId = null;
    String personId1 = null;

    if(request.getAttribute("reviewingDisclNum") != null) {
        reviewingDisclNum = request.getAttribute("reviewingDisclNum").toString();
     }
     if(request.getAttribute("reviewingSeqNum") != null) {
         reviewingSeqNum = Integer.parseInt(request.getAttribute("reviewingSeqNum").toString());
     }
    if(request.getAttribute("reviewerPersonId") != null) {
        reviewerPersonId = request.getAttribute("reviewerPersonId").toString();
    }
    if(request.getAttribute("personId1") != null) {
        personId1 = request.getAttribute("personId1").toString();
    }
%>
<html:html locale="true">
    <head>
        <title>Reviewer Comment</title>
        <%
        String reviewC= (String)session.getAttribute("reviewC");
          Vector recommendedActionList = new Vector();
         if(request.getAttribute("recommendedActionList") != null) {
             recommendedActionList = (Vector)request.getAttribute("recommendedActionList");
             }
          Vector revDetails=(Vector)request.getAttribute("details");
        %>

        <script type="text/javascript">
            function saveAndcomplteReview(){
                var success=validateSave();
                if(success==true){
                    var r=confirm("Do you want to save the recommended action?");
                   if(r==true){
                 document.forms[0].action= "<%=path%>/completeReviewAction.do?reviewingDisclNum=<%=reviewingDisclNum%>&reviewingSeqNum=<%=reviewingSeqNum%>&reviewerPersonId=<%=reviewerPersonId%>&reviewComplete=true";
                 document.forms[0].submit();}
            }}
            function cancelReview(){
                 document.forms[0].action= "<%=path%>/completeReviewAction.do?calcelReview=true";
                 document.forms[0].submit();
             }
            function validateSave(){
              if(document.forms[0].recommendedAction.value==null || document.forms[0].recommendedAction.value=='')
            {
            alert("Please select recommended action");
            document.forms[0].recommendedAction.focus();
            return false;
            }
            return true;
            }
       </script>
    </head>
<body>
    <table id="setStatusBodyTable" class="table" style="width: 100%; height: auto; " >
    <tr><td>
    <html:form action="/saveNotesCoiv2.do">
        <logic:present name="update">
    <logic:equal value="true" name="update">
        <font color="red">You have successfully completed this review and set the Recommended Action :&nbsp;<%=reviewC%></font>
    </logic:equal>
    </logic:present>

    <logic:notPresent name="update">

            <div>
                <table style="width: 100%;" height="100%"  border="0" cellpadding="0" cellspacing="0" class="table" align='center'>
                    <tr>
                        <td height="20" width="50%" align="left" valign="top" class="theader" colspan="2">Complete Review</td>
                    </tr>
                    <tr>
                        <td valign="top" align="left" style="width:80%;padding-left: 2px;" colspan="2"><br/>
                           Please complete the review by selecting a recommended action from
                           the dropdown and then Save. If you wish to return to the disclosure,
                           select Cancel <br/><br/></td>
                    </tr>

                             <%
                    String beancodeConflict="";
                    String code="" ;

                    if(revDetails != null){
                    for(int row=0;row<revDetails.size();row++){
                    edu.mit.coeuslite.coiv2.beans.Coiv2AssignDisclUserBean userBean = (edu.mit.coeuslite.coiv2.beans.Coiv2AssignDisclUserBean)revDetails.get(row);
                    String prsnId = userBean.getPersonId();
                   if(reviewerPersonId.equals(prsnId) ) {
                       if(userBean.getCode() != null){
                         code=userBean.getCode().toString();
                       %>
                        <tr>
                        <td colspan="2">
                        <b> Recommended Action:</b>
                        <select id="recommendedAction" name="recommendedAction" style="width: 70%" >
                            <%
                             if (recommendedActionList != null) {
                                                ComboBoxBean bean;
                                                for (int index = 0; index < recommendedActionList.size(); index++) {
                                                bean = (ComboBoxBean) recommendedActionList.get(index);
                                                beancodeConflict= bean.getDescription();
                                                String selected = "";
                                                if (code != null && bean.getCode().equals(code)) {
                                                    selected = "selected";
                                                }
                            %>
                        <option  <%=selected%> value="<%=bean.getCode()%>" > <%=bean.getDescription() %></option>
                        <% }} %>
                       </select>
                        </td></tr>
                    <% }else{%>
                     <tr>
                        <td colspan="2">
                    <b> Recommended Action:</b>
                        <select id="recommendedAction" name="recommendedAction" style="width: 70%" >
                        <option value=""></option>
                        <logic:present name="recommendedActionList">
                        <logic:iterate id="recommendAction" name="recommendedActionList">
                        <option value="<bean:write name="recommendAction" property="code"/>"><bean:write name="recommendAction" property="description"/></option>

                        </logic:iterate>
                        </logic:present>
                       </select>
                       <% }} %>
                        </td>
                    </tr>
                      <% }} %>

                    <tr>
                    <tr><td colspan="2"> &nbsp;</td></tr>
                        <td class='savebutton' width="5%" style="padding-left: 10px;">
                            <html:button property="Save" styleClass="clsavebutton" style="width:100px;" onclick="javaScript:saveAndcomplteReview();">
                                Save
                            </html:button>
                        </td>
                        <td class='savebutton'>
                            <html:button property="Save" styleClass="clsavebutton" style="width:100px;" onclick="javaScript:cancelReview();">
                                 Cancel
                            </html:button>
                        </td>
                    </tr>
                </table>
            </div>

    </logic:notPresent>
     </td></tr></table>
    </html:form>
      </body>
</html:html>