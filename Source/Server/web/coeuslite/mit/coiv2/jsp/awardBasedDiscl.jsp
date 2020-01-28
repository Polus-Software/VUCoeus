<%--
    Document   : awardBasedDiscl
    Created on : Mar 17, 2010, 6:48:32 PM
    Author     : Sony
--%>

<%@page contentType="text/html" pageEncoding="UTF-8" import="edu.mit.coeuslite.coiv2.beans.CoiDisclosureBean,java.util.Vector,java.util.HashMap,edu.mit.coeuslite.coiv2.utilities.CoiConstants,edu.mit.coeuslite.coiv2.beans.CoiAwardInfoBean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" 	prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" 	prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" 	prefix="logic" %>
<%@ taglib uri="/WEB-INF/coeus-utils.tld"	prefix="coeusUtils" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1" />
        <title>C O I</title>
        <link href="<%=request.getContextPath()%>/coeuslite/mit/coiv2/css/layout.css" rel="stylesheet" type="text/css" />
        <link href="<%=request.getContextPath()%>/coeuslite/mit/coiv2/css/styles.css" rel="stylesheet" type="text/css" />
        <link href="<%=request.getContextPath()%>/coeuslite/mit/coiv2/css/collapsemenu.css" rel="stylesheet" type="text/css" />
        <%          String path = request.getContextPath();            
            String disable = "";           
                    String operationType = (String) request.getAttribute("operation");
                    CoiDisclosureBean disclosureBean = (CoiDisclosureBean) request.getSession().getAttribute("disclosureBeanSession");
                    String target = "#";
                   // if (disclosureBean == null || disclosureBean.getCoiDisclosureNumber() == null) {
                     //   target = path + "/getCompleteDisclCoiv2.do?projectType=Award";
                    //} else {
                        target = path + "/coiCommonSave.do";
                   // }
        %>
    </head>

    <script type="text/javascript">
        function showPjtDet(){
            document.getElementById("pjtDet").style.visibility="visible";
        }
        function addDisclSubmit(count){
            debugger;
                  var canContinue=false;
        var cc=parseInt(count);
        if(cc>0){
          for(i=0;i<=cc;i++ ){
            if(document.forms[0].checkedAwardBasedProjects[i] == undefined){
                 if(document.forms[0].checkedAwardBasedProjects.checked==true){
                    canContinue=true;
                 } 
            }
            else if(document.forms[0].checkedAwardBasedProjects[i].checked==true)
             {
                canContinue=true;
                break;
             }
          }
        }
        else{
             if(document.forms[0].checkedAwardBasedProjects.checked==true)
             {
                canContinue=true;
             }
        }
        if(canContinue==false)
          {
              alert("Please select a award to continue");
          }else{
            document.forms[0].action='<%=target%>';
            document.forms[0].submit();
            }
        }
    </script>
        <table id="bodyTable" class="table" style="width: 100%;" border="0">
            <html:form action="/createDisclosureCoiv2.do" method="POST">
                <logic:empty name="allAwardList">
                    <tr>
                        <td colspan="5"><p style="color: #ff0000;font-size: 12px;">There are no awards in the system that requires your COI disclosure.</p></td>
                    </tr>
                </logic:empty>
                <logic:notEmpty name="allAwardList">
                <tr>
                    <td colspan="5">Please select an award from the list to create a new COI disclosure</td>
                </tr>

                <tr style="background-color:#6E97CF;">
                    <td width="15%" style="padding-left: 23px"><strong>Award #</strong></td>
                    <td width="12%"><strong>Account #</strong></td>
                    <td width="35%"><strong>Title</strong></td>
                    <td width="25%"><strong>Sponsor</strong></td> 
                    <td><strong>Award Date</strong></td>
                </tr>

                <%
                            String[] rowColors = {"#D6DCE5", "#DCE5F1"};
                            int i = 0;
                            String rowColor = "";
                            Vector awardList = (Vector) request.getAttribute("allAwardList");
                            int index = 0;
                %>

                <logic:iterate id="allAwards" name="allAwardList">
                    <%
                                if (i == 0) {
                                    rowColor = rowColors[0];
                                    i = 1;
                                } else {
                                    rowColor = rowColors[1];
                                    i = 0;
                                }
                                String checked = "";
                                CoiAwardInfoBean awardInfoBean = new CoiAwardInfoBean();
                                if (awardList != null && !awardList.isEmpty()) {
                                    awardInfoBean = (CoiAwardInfoBean) awardList.get(index);
                                    if (awardInfoBean.isChecked() == true) {
                                        checked = "checked";
                                    }
                                }
                                index++;
                    %>
                   <tr bgcolor="<%=rowColor%>" class="rowLine" onmouseover="className='rowHover rowLine'" onmouseout="className='rowLine'" style="width: 765px;">
                       <td><input type="radio"  <%=disable%> <%=checked%> name="checkedAwardBasedProjects" value="<bean:write name="allAwards" property="mitAwardNumber"/>;<bean:write name="allAwards" property="awardTitle"/>;<bean:write name="allAwards" property="startDate"/>;<bean:write name="allAwards" property="awardExecutionDate"/>;<bean:write name="allAwards" property="projectSponsor"/>" /><bean:write name="allAwards" property="mitAwardNumber"/></td>
                       <td><bean:write name="allAwards" property="accountNumber"/></td>
                       <td><bean:write name="allAwards" property="awardTitle"/></td>
                       <td><bean:write name="allAwards" property="projectSponsor"/></td>
                       <td><bean:write name="allAwards" property="startDate" format="dd-MMM-yyyy"/></td>
                    </tr>
                </logic:iterate>

                <tr>
                    <td align="left" colspan="5">
                         <% String link="javaScript:addDisclSubmit('" + index + "')"; %>
                             <html:button styleClass="clsavebutton"  value="Continue" property="save"  onclick="<%=link%>"/>
                    </td>
                </tr>
                </logic:notEmpty>
                <tr bgcolor="#D1E5FF"><td colspan="5">
                   <div class="myForm" id="pjtDet" style="visibility: hidden; overflow:hidden; height: 1px;width:700px" align="center">
                            <div class="Formline0">
                                <br/>
                                <div class="FormLeft">
                                    Project Title
                                    <input type="text" name="textfield" id="textfield" />
                                </div>
                                <div class="FormRight">
                                    Project Dept.
                                    <input type="text" name="textfield" id="textfield" />
                                </div>
                            </div>

                            <div class="Formline2">
                                Description
                                <textarea name="textarea" id="textarea" cols="50" rows="5"></textarea>
                            </div>
                            <div class="FormLeft" align="left"><html:button styleClass="clsavebutton" property="button" value="Submit"></html:button></div>
                            <div class="FormRight"></div>
                        </div></td></tr>
                    </html:form>

        </table>
</html>
