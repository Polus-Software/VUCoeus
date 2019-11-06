<%--
    Document   : awardBasedDiscl
    Created on : Mar 17, 2010, 6:48:32 PM
    Author     : Sony
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"
	import="edu.mit.coeuslite.coiv2.beans.CoiDisclosureBean,java.util.HashMap,edu.mit.coeuslite.coiv2.utilities.CoiConstants,java.util.Vector,edu.mit.coeuslite.coiv2.beans.CoiProtocolInfoBean;"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/coeus-utils.tld" prefix="coeusUtils"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1" />
<title>C O I</title>
<link
	href="<%=request.getContextPath()%>/coeuslite/mit/coiv2/css/layout.css"
	rel="stylesheet" type="text/css" />
<link
	href="<%=request.getContextPath()%>/coeuslite/mit/coiv2/css/styles.css"
	rel="stylesheet" type="text/css" />
<link
	href="<%=request.getContextPath()%>/coeuslite/mit/coiv2/css/collapsemenu.css"
	rel="stylesheet" type="text/css" />
<%String path = request.getContextPath();                   
                    String operationType = (String) request.getAttribute("operation");
                    CoiDisclosureBean disclosureBean = (CoiDisclosureBean) request.getSession().getAttribute("disclosureBeanSession");
                    String target = "#";
                    if (disclosureBean == null || disclosureBean.getCoiDisclosureNumber() == null) {
                        target = path + "/getCompleteDisclCoiv2.do?projectType=Protocol";
                    } else {
                        target = path + "/createDisclosureCoiv2.do?operation=UPDATE&projectType=Other&operationType=" + operationType;
                    }
                     int index = 0;
        %>
</head>
<script src="js/jquery.js" type="text/javascript"></script>
<script type="text/javascript">
        function addDisclSubmit(count){
              var canContinue=false;
        var cc=parseInt(count);
        if(cc>1){
          for(i=0;i<cc;i++ ){
             if(document.forms[0].checkedProtocolBasedProjects[i].checked==true)
             {
                canContinue=true;
                break;
             }
          }
        }else{
             if(document.forms[0].checkedProtocolBasedProjects.checked==true)
             {
                canContinue=true;
             }
        }
        if(canContinue==false)
          {
              alert("Please select a protocol to continue");
          }else{
            document.forms[0].action='<%=target%>';
            document.forms[0].submit();
            }
        }
        function showPjtDet(){
            document.getElementById("pjtDet").style.visibility="visible";
        }
    </script>


<%--<body>--%>
<td valign="top" bgcolor="#D1E5FF">
	<table id="bodyTable1" class="table" style="width: 765px;" border="0">
		<tr style="background-color: #6E97CF;">
			<td colspan="6">
				<h1
					style="background-color: #6E97CF; color: #FFFFFF; float: left; font-size: 14px; font-weight: bold; margin: 0; padding: 2px 0 2px 10px; position: relative; text-align: left;">
					Financial Disclosure by
					<bean:write name="person" property="fullName" />
				</h1>
			</td>
		</tr>
		<tr>
			<td
				style="color: #333333; font-family: Arial, Helvetica, sans-serif; font-size: 12px; font-weight: bold; float: left; width: 100px;">
				Reporter Name :</td>
			<td
				style="color: #333333; font-family: Arial, Helvetica, sans-serif; font-size: 12px; align: left; width: 150px;">
				<bean:write name="person" property="fullName" />
			</td>
			<td
				style="color: #333333; font-family: Arial, Helvetica, sans-serif; font-size: 12px; font-weight: bold; float: left; width: 100px;">
				Department :</td>
			<td
				style="color: #333333; font-family: Arial, Helvetica, sans-serif; font-size: 12px; align: left; width: 150px;">
				<bean:write name="person" property="dirDept" />
			</td>
			<td
				style="color: #333333; font-family: Arial, Helvetica, sans-serif; font-size: 12px; font-weight: bold; float: left; width: 100px;">
				Reporter Email :</td>
			<td
				style="color: #333333; font-family: Arial, Helvetica, sans-serif; font-size: 12px; align: left; width: 150px;">
				<bean:write name="person" property="email" />
			</td>
		</tr>
		<%--         <tr>
                           <td style="color: #333333;font-family:Arial,Helvetica,sans-serif;font-size:12px;font-weight:bold;float: left">
                                    Office Location :
                           </td>
                           <td style="color: #333333;font-family:Arial,Helvetica,sans-serif;font-size:12px;align:left;">
                           <bean:write name="person" property="offLocation"/>
                           </td>
                           <td style="color: #333333;font-family:Arial,Helvetica,sans-serif;font-size:12px;font-weight:bold;float: left">
                           Office Phone :
                           </td>
                           <td style="color: #333333;font-family:Arial,Helvetica,sans-serif;font-size:12px;align:left;">
                           <bean:write name="person" property="offPhone"/>
                           </td>
                           <td style="color: #333333;font-family:Arial,Helvetica,sans-serif;font-size:12px;font-weight:bold;float: left">
                          Secondary Office:
                          </td>
                           <td style="color: #333333;font-family:Arial,Helvetica,sans-serif;font-size:12px;align:left;">
                           <bean:write name="person" property="secOffLoc"/>
                           </td>
                     </tr>--%>
		<tr>
			<td colspan="6"><img height="2" border="0" width="100%"
				src="/coeus44server/coeusliteimages/line4.gif" /></td>
		</tr>
	</table> <%-- <table id="bodyTable" class="table" style="width: 765px;" border="0">
            <html:form action="/createDisclosureCoiv2.do">
                 <div align="center">
                     <div id="wrapper">
                         <div id="main">
                <logic:notPresent name="protocolList">
                    <tr>
                        <td><p style="color: #ff0000;font-size: 12px;">No Protocol is created for the user</p></td>
                    </tr>
                </logic:notPresent>
                <logic:present name="protocolList">
                    <tr>
                        <td colspan="2">Please select necessary protocols from this list to add the disclosure</td>
                    </tr>
                    <div class="ProjectDetails">
                    <tr style="background-color:#6E97CF;">
                        <td><strong> Protocol Number</strong></td>
                        <td><strong> Protocol Name</strong></td>
                        <td><strong> PI</strong></td></tr>
                        <%
                                    String[] rowColors = {"#D6DCE5", "#DCE5F1"};
                                    int i = 0;
                                    String rowColor = "";
                                    Vector protoList = (Vector) request.getAttribute("protocolList");
                        %>
                        <logic:iterate id="allProtocols" name="protocolList">
                            <%
                                        if (i == 0) {
                                            rowColor = rowColors[0];
                                            i = 1;
                                        } else {
                                            rowColor = rowColors[1];
                                            i = 0;
                                        }
                                        String checked = "";
                                        CoiProtocolInfoBean coiProtocolInfoBean = new CoiProtocolInfoBean();
                                        if (protoList != null && !protoList.isEmpty()) {
                                            coiProtocolInfoBean = (CoiProtocolInfoBean) protoList.get(index);
                                            if (coiProtocolInfoBean.isChecked() == true) {
                                                checked = "checked";
                                            }
                                        }
                                        index++;
                            %>
                       <tr bgcolor="<%=rowColor%>" class="rowLine" onmouseover="className='rowHover rowLine'" onmouseout="className='rowLine'" style="width: 765px;">
                            <td><input type="checkbox" <%=disable%> <%=checked%> name="checkedProtocolBasedProjects" value="<bean:write name="allProtocols" property="protocolNumber"/>:<bean:write name="allProtocols" property="title"/>"/><bean:write name="allProtocols" property="protocolNumber"/></td>
                            <td><bean:write name="allProtocols" property="title"/></td>
                            <td width="50px"><bean:write name="allProtocols" property="createUser"/></td></tr>
                        </logic:iterate>

                                <div class="column1"> </div>
                                <div class="column1"> </div>

                    <tr>
                            <td></td>
                            <td></td>
                            <td><a href="javaScript:addDisclSubmit();">Add to Disclosure</a></td>
                        <td align="left" colspan="3">
                             <% String link="javaScript:addDisclSubmit('" + index + "')"; %>
                             <html:button styleClass="clsavebutton"  value="Continue" property="save"  onclick="<%=link%>"/>
                        </td>
                    </tr>

                </logic:present>

                     <div class="column2"><a href="javaScript:showPjtDet();"> Add New</a></div>

                            <div class="column1"> <a href="javaScript:addDisclSubmit();">Add to Disclosure</a></div>
                 <div class="column1">    </div>
                 <div class="column2"></div>
                
                <tr bgcolor="#D1E5FF"><td colspan="3">
                        <div class="myForm" id="pjtDet" style="visibility: hidden;height: 0px">
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
                        <tr bgcolor="#D1E5FF"><td style="height: 100%;" colspan="3">&nbsp;</td></tr>
                                       </div>
                                   </div>
                               </div>
                           </div>
                    </html:form>

        </table>--%>
</td>
</tr>
</table>
<%--</body>--%>
</html>