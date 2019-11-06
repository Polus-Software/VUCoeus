
<%--
    Document   : bodyHeader
    Created on : Aug 5, 2010, 2:32:25 PM
    Author     : Invision
--%>


<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/privilege.tld" prefix="priv"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/coeus-utils.tld" prefix="coeusUtils"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
   "http://www.w3.org/TR/html4/loose.dtd">
<%@page
	import="java.util.Vector,edu.mit.coeuslite.coiv2.beans.CoiDisclosureBean,
        edu.mit.coeuslite.coiv2.services.CoiCommonService,java.util.Date,edu.mit.coeuslite.utils.bean.SubHeaderBean,
        edu.mit.coeus.bean.PersonInfoBean,edu.mit.coeus.bean.UserDetailsBean,edu.mit.coeuslite.coiv2.beans.CoiAnnualProjectEntityDetailsBean;"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
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
<link
	href="<%=request.getContextPath()%>/coeuslite/dartmouth/utils/css/coeus_styles.css"
	rel="stylesheet" type="text/css" />
<style>
#mbox {
	background-color: #6e97cf;
	padding: 0px 8px 8px 8px;
	border: 3px solid #095796;
}

#mbm {
	font-family: sans-serif;
	font-weight: bold;
	float: right;
	padding-bottom: 5px;
}

#ol {
	background-image:
		url('../coeuslite/mit/utils/scripts/modal/overlay.png');
}

.dialog {
	display: none
}

* html #ol {
	background-image: none;
	filter: progid:DXImageTransform.Microsoft.AlphaImageLoader(src="../coeuslite/mit/utils/scripts/modal/overlay.png",
		sizingMethod="scale");
}
</style>

<%
       Vector approvedDisclosureView = new Vector();
          if(request.getAttribute("ApprovedDisclDetView") != null) {
           approvedDisclosureView= (Vector) request.getAttribute("ApprovedDisclDetView");
    }
          String bodyHeader = "Current Disclosure";
          if(session.getAttribute("param6")!= null){
          //bodyHeader = (String)session.getAttribute("param6");
          bodyHeader = "Disclosure Details";
          }
          String faculty = "";
          if(session.getAttribute("person") != null) {
              PersonInfoBean prsnInfoBean = (PersonInfoBean)session.getAttribute("person");

              String facultyFlag = prsnInfoBean.getFacFlag();

              if(facultyFlag != null && facultyFlag.equalsIgnoreCase("N")) {
                  faculty = "No";
              }
              else if(facultyFlag != null && facultyFlag.equalsIgnoreCase("Y")) {
                  faculty = "Yes";
              }
            }
          boolean isDue = false;
            if(session.getAttribute("isReviewDue") != null) {
               isDue = (Boolean)session.getAttribute("isReviewDue");
            }
          String path = request.getContextPath();
          boolean isEvent = false;
           if(session.getAttribute("isEvent") != null) {
                isEvent = (Boolean)session.getAttribute("isEvent");
            }

          String peronFullName = "";
          if(request.getAttribute("PersonDetails") != null) {
              Vector personData = (Vector) request.getAttribute("PersonDetails");

              if(personData != null && personData.size() > 0) {
                  PersonInfoBean perondet = (PersonInfoBean)personData.get(0);
                  peronFullName = perondet.getFullName();
              }
          }
          else if(session.getAttribute("PersonDetails") != null) {
              Vector personData = (Vector) session.getAttribute("PersonDetails");

              if(personData != null && personData.size() > 0) {
                  PersonInfoBean perondet = (PersonInfoBean)personData.get(0);
                  peronFullName = perondet.getFullName();
              }
          }
                String Desc="";
                if(session.getAttribute("Desc")!=null)
                    Desc=session.getAttribute("Desc").toString();

                String Campus="";
                if(session.getAttribute("Campus")!=null)
                    Campus=session.getAttribute("Campus").toString();
       %>
<script>
           function cal(){
            document.forms[0].action= '<%=path%>' + "/coiAnnualRevision.do?projectType=Annual";
            document.forms[0].submit();
           }
            function showTitle(txtAreaID,id)
             {
                  var width  = 650;//document.getElementById('divTXTDetails').style.pixelWidth;
                  var height = 170;//document.getElementById('divTXTDetails').style.pixelHeight;

                 sm(id,width,height);

                 var txtValue=document.getElementById(txtAreaID).value;
                 document.getElementById('TxtArea').innerHTML=txtValue;
                 document.getElementById("mbox").style.left="385";//450
                 document.getElementById("mbox").style.top="250";//250
             }
       </script>
</head>
<body>
	<table id="bodyTable1" class="table" border="0" width="100%">
		<!-- JM 3-26-2012 updated colors -->
		<tr style="background-color: #999999;">
			<td colspan="4">
				<!-- JM 3-26-2012 updated colors -->
				<h1
					style="background-color: #999999; color: #FFFFFF; float: left; font-size: 14px; font-weight: bold; margin: 0; padding: 2px 0 2px 1px; position: relative; text-align: left;">
					Financial Disclosure by
					<%=peronFullName%><%--<bean:write name="person" property="fullName"/>--%>
				</h1>
			</td>
		</tr>
		<logic:present name="PersonDetails">
			<logic:iterate id="person" name="PersonDetails">
				<tr>
					<td
						style="color: #333333; font-family: Arial, Helvetica, sans-serif; font-size: 12px; font-weight: bold; float: none;"
						width="15%" align="right">&emsp;&emsp;&emsp; Name :</td>
					<td
						style="color: #333333; font-family: Arial, Helvetica, sans-serif; font-size: 12px; font-weight: bold; float: none;"
						width="35%" align="left">&nbsp;<span
						style="color: #333333; font-family: Arial, Helvetica, sans-serif; font-weight: normal; font-size: 12px;"><bean:write
								name="person" property="fullName" /></span>
					</td>
					<td
						style="color: #333333; font-family: Arial, Helvetica, sans-serif; font-size: 12px; font-weight: bold; float: none"
						width="18%" align="right">User Name :</td>
					<td
						style="color: #333333; font-family: Arial, Helvetica, sans-serif; font-size: 12px; font-weight: bold; float: none"
						align="left">&nbsp;<span
						style="color: #333333; font-family: Arial, Helvetica, sans-serif; font-weight: normal; font-size: 12px;"><bean:write
								name="person" property="userName" /></span>
					</td>
				</tr>
				<tr>
					<td
						style="color: #333333; font-family: Arial, Helvetica, sans-serif; font-size: 12px; font-weight: bold; float: none"
						align="right">&nbsp; Department :</td>
					<td
						style="color: #333333; font-family: Arial, Helvetica, sans-serif; font-size: 12px; font-weight: bold; float: none"
						align="left">&nbsp;<span
						style="color: #333333; font-family: Arial, Helvetica, sans-serif; font-weight: normal; font-size: 12px;"><%=Desc%><%--<bean:write name="person" property="homeUnit"/>--%></span>
					</td>

					<td
						style="color: #333333; font-family: Arial, Helvetica, sans-serif; font-size: 12px; font-weight: bold; float: none"
						align="right">&emsp;&emsp; Phone :</td>
					<td
						style="color: #333333; font-family: Arial, Helvetica, sans-serif; font-size: 12px; font-weight: bold; float: none"
						align="left">&nbsp;<span
						style="color: #333333; font-family: Arial, Helvetica, sans-serif; font-weight: normal; font-size: 12px;"><bean:write
								name="person" property="offPhone" /></span>
					</td>

				</tr>
				<tr>
					<td
						style="color: #333333; font-family: Arial, Helvetica, sans-serif; font-size: 12px; font-weight: bold; float: none"
						align="right">&nbsp;&nbsp;&emsp;&emsp; Faculty :</td>

					<td
						style="color: #333333; font-family: Arial, Helvetica, sans-serif; font-size: 12px; font-weight: bold; float: none"
						align="left">&nbsp;<span
						style="color: #333333; font-family: Arial, Helvetica, sans-serif; font-weight: normal; font-size: 12px;"><%=faculty%></span>
					</td>

					<td
						style="color: #333333; font-family: Arial, Helvetica, sans-serif; font-size: 12px; font-weight: bold; float: none"
						align="right">&nbsp; &emsp;&emsp; Email :</td>
					<td
						style="color: #333333; font-family: Arial, Helvetica, sans-serif; font-size: 12px; font-weight: bold; float: none"
						align="left">&nbsp;<span
						style="color: #333333; font-family: Arial, Helvetica, sans-serif; font-weight: normal; font-size: 12px;"><html:link
								href="mailto:">
								<bean:write name="person" property="email" />
							</html:link></span>
					</td>
				</tr>
				<tr>
					<td
						style="color: #333333; font-family: Arial, Helvetica, sans-serif; font-size: 12px; font-weight: bold; float: none"
						align="right">&emsp;&emsp; Campus :</td>
					<td
						style="color: #333333; font-family: Arial, Helvetica, sans-serif; font-size: 12px; font-weight: bold; float: none"
						colspan="3" align="left">&nbsp;<span
						style="color: #333333; font-family: Arial, Helvetica, sans-serif; font-weight: normal; font-size: 12px;"><%=Campus%><bean:write
								name="person" property="school" /></span>
					</td>
				</tr>

				<%--<tr>
                <td colspan="4">
                    <img height="2" border="0" width="100%" src="/coeus44server/coeusliteimages/line4.gif"/>
                </td>
            </tr>--%>

			</logic:iterate>
		</logic:present>
		<%--        </table>


        <table class="table" style="width: 100%;" border="0">--%>

		<logic:present name="ApprovedDisclDetView">
			<tr>
				<td class="copybold" nowrap="" colspan="4"><img width="100%"
					height="2" border="0"
					src="<%=request.getContextPath()%>/coeusliteimages/line4.gif"></td>
			</tr>
			<logic:iterate id="disclDetails" name="ApprovedDisclDetView">
				<tr>
					<td
						style="color: #333333; font-family: Arial, Helvetica, sans-serif; font-size: 12px; font-weight: bold; float: none;"
						align="right"><logic:notPresent name="ToShowMY">My</logic:notPresent>
						Discl. No :</td>
					<td
						style="color: #333333; font-family: Arial, Helvetica, sans-serif; font-size: 12px; font-weight: bold; float: none;"
						align="left"><span
						style="color: #333333; font-family: Arial, Helvetica, sans-serif; font-weight: normal; font-size: 12px;"><bean:write
								name="disclDetails" property="coiDisclosureNumber" />(<bean:write
								name="disclDetails" property="sequenceNumber" />)</span></td>
					<td
						style="color: #333333; font-family: Arial, Helvetica, sans-serif; font-size: 12px; font-weight: bold; float: none;"
						align="right">Expiration Date :</td>
					<td
						style="color: #333333; font-family: Arial, Helvetica, sans-serif; font-size: 12px; font-weight: bold; float: none;"
						align="left"><span
						style="color: #333333; font-family: Arial, Helvetica, sans-serif; font-weight: normal; font-size: 12px;">
							<logic:notEqual name="disclDetails" property="dispositionStatus"
								value="Pending">
								<coeusUtils:formatDate name="disclDetails"
									property="expirationDate" />
							</logic:notEqual>
					</span></td>
				</tr>
				<tr>
					<td nowrap
						style="color: #333333; font-family: Arial, Helvetica, sans-serif; font-size: 12px; font-weight: bold; float: none;"
						align="right">Disposition Status :</td>
					<td
						style="color: #333333; font-family: Arial, Helvetica, sans-serif; font-size: 12px; font-weight: bold; float: none;"
						align="left"><span
						style="color: #333333; font-family: Arial, Helvetica, sans-serif; font-weight: normal; font-size: 12px;"><bean:write
								name="disclDetails" property="dispositionStatus" /></span></td>
					<td nowrap
						style="color: #333333; font-family: Arial, Helvetica, sans-serif; font-size: 12px; font-weight: bold; float: none;"
						align="right"><logic:notPresent name="ToShowMY">My</logic:notPresent>
						Disclosure Status :</td>
					<td
						style="color: #333333; font-family: Arial, Helvetica, sans-serif; font-size: 12px; font-weight: bold; float: none;"
						align="left"><span
						style="color: #333333; font-family: Arial, Helvetica, sans-serif; font-weight: normal; font-size: 12px;"><bean:write
								name="disclDetails" property="disclosureStatus" /></span></td>
				</tr>
				<tr>
					<td
						style="color: #333333; font-family: Arial, Helvetica, sans-serif; font-size: 12px; font-weight: bold; float: none;"
						align="right">Last Updated :</td>
					<td
						style="color: #333333; font-family: Arial, Helvetica, sans-serif; font-size: 12px; font-weight: bold; float: none;"
						align="left"><span
						style="color: #333333; font-family: Arial, Helvetica, sans-serif; font-weight: normal; font-size: 12px;"><bean:write
								name="disclDetails" property="updateTimestamp" /><b> by </b>
						<bean:write name="disclDetails" property="updateUser" /></span></td>
					<td
						style="color: #333333; font-family: Arial, Helvetica, sans-serif; font-size: 12px; font-weight: bold; float: none;"
						align="right">Review Status :</td>
					<td
						style="color: #333333; font-family: Arial, Helvetica, sans-serif; font-size: 12px; font-weight: bold; float: none;"
						align="left"><span
						style="color: #333333; font-family: Arial, Helvetica, sans-serif; font-weight: normal; font-size: 12px;"><bean:write
								name="disclDetails" property="reviewStatus" /></span></td>
				</tr>
			</logic:iterate>
		</logic:present>

		<logic:notPresent name="ApprovedDisclDetView">
			<logic:present name="ApprovedDisclDet">
				<tr>
					<td class="copybold" nowrap="" colspan="4"><img width="100%"
						height="2" border="0"
						src="<%=request.getContextPath()%>/coeusliteimages/line4.gif"></td>
				</tr>
				<logic:iterate id="disclDetails" name="ApprovedDisclDet">
					<tr>
						<td
							style="color: #333333; font-family: Arial, Helvetica, sans-serif; font-size: 12px; font-weight: bold; float: none;"
							align="right"><logic:notPresent name="ToShowMY">My</logic:notPresent>
							Discl. No :</td>
						<td
							style="color: #333333; font-family: Arial, Helvetica, sans-serif; font-size: 12px; font-weight: bold; float: none;"
							align="left"><span
							style="color: #333333; font-family: Arial, Helvetica, sans-serif; font-weight: normal; font-size: 12px;"><bean:write
									name="disclDetails" property="coiDisclosureNumber" />(<bean:write
									name="disclDetails" property="sequenceNumber" />)</span></td>

						<td
							style="color: #333333; font-family: Arial, Helvetica, sans-serif; font-size: 12px; font-weight: bold; float: none;"
							align="right">Expiration Date :</td>
						<td
							style="color: #333333; font-family: Arial, Helvetica, sans-serif; font-size: 12px; font-weight: bold; float: none;"
							align="left"><span
							style="color: #333333; font-family: Arial, Helvetica, sans-serif; font-weight: normal; font-size: 12px;">
								<logic:notEqual name="disclDetails" property="dispositionStatus"
									value="Pending">
									<coeusUtils:formatDate name="disclDetails"
										property="expirationDate" />
								</logic:notEqual>
						</span></td>
					</tr>
					<tr>
						<td
							style="color: #333333; font-family: Arial, Helvetica, sans-serif; font-size: 12px; font-weight: bold; float: none;"
							align="right">Disposition Status :</td>
						<td
							style="color: #333333; font-family: Arial, Helvetica, sans-serif; font-size: 12px; font-weight: bold; float: none;"
							align="left"><span
							style="color: #333333; font-family: Arial, Helvetica, sans-serif; font-weight: normal; font-size: 12px;"><bean:write
									name="disclDetails" property="dispositionStatus" /></span></td>
						<td
							style="color: #333333; font-family: Arial, Helvetica, sans-serif; font-size: 12px; font-weight: bold; float: none;"
							align="right"><logic:notPresent name="ToShowMY">My</logic:notPresent>
							Disclosure Status :</td>
						<td
							style="color: #333333; font-family: Arial, Helvetica, sans-serif; font-size: 12px; font-weight: bold; float: none;"
							align="left"><span
							style="color: #333333; font-family: Arial, Helvetica, sans-serif; font-weight: normal; font-size: 12px;"><bean:write
									name="disclDetails" property="disclosureStatus" /></span></td>
					</tr>
					<tr>
						<td
							style="color: #333333; font-family: Arial, Helvetica, sans-serif; font-size: 12px; font-weight: bold; float: none;"
							align="right">Last Updated :</td>
						<td
							style="color: #333333; font-family: Arial, Helvetica, sans-serif; font-size: 12px; font-weight: bold; float: none;"
							align="left"><span
							style="color: #333333; font-family: Arial, Helvetica, sans-serif; font-weight: normal; font-size: 12px;"><bean:write
									name="disclDetails" property="updateTimestamp" /><b> by </b>
							<bean:write name="disclDetails" property="updateUser" /></span></td>
						<td
							style="color: #333333; font-family: Arial, Helvetica, sans-serif; font-size: 12px; font-weight: bold; float: none;"
							align="right">Review Status :</td>
						<td
							style="color: #333333; font-family: Arial, Helvetica, sans-serif; font-size: 12px; font-weight: bold; float: none;"
							align="left"><span
							style="color: #333333; font-family: Arial, Helvetica, sans-serif; font-weight: normal; font-size: 12px;"><bean:write
									name="disclDetails" property="reviewStatus" /></span></td>
					</tr>
				</logic:iterate>
			</logic:present>

		</logic:notPresent>
	</table>
	<logic:notPresent name="hide">
		<%


                 if(isEvent) {

           %>

		<table id="bodyTable" class="table" style="width: 100%;" border="0">
			<tr style="background-color: #6E97CF; height: 22px;">
				<td
					style="width: 10%; background-color: #6E97CF; color: #FFFFFF; float: none; font-size: 14px; font-weight: bold; margin: 0; padding: 2px 0 2px 10px; text-align: left;"><strong>Discl.Event</strong></td>
				<td
					style="width: 10%; background-color: #6E97CF; color: #FFFFFF; float: none; font-size: 14px; font-weight: bold; margin: 0; padding: 2px 0 2px 10px; text-align: left;"><strong>Project
						#</strong></td>
				<td
					style="width: 47%; background-color: #6E97CF; color: #FFFFFF; float: none; font-size: 14px; font-weight: bold; margin: 0; padding: 2px 0 2px 10px; text-align: left;"><strong>Project
						Name</strong></td>
				<td
					style="width: 13%; background-color: #6E97CF; color: #FFFFFF; float: none; font-size: 14px; font-weight: bold; margin: 0; padding: 2px 0 2px 10px; text-align: left;"><strong>Discl.Status</strong></td>

			</tr>
			<logic:empty name="eventPjtNameList">
				<tr>
					<td colspan="4"><font color="red">No Projects found</font></td>
				</tr>
			</logic:empty>
		</table>
		<table id="bodyTable" class="table" style="width: 100%; height: 100%;"
			border="0">
			<logic:present name="eventPjtNameList">
				<%
            String strBgColor = "#DCE5F1";
            Vector projectNameList = (Vector)session.getAttribute("eventPjtNameList");
              int index = 0;
              String linkFeed;
              String entityNum = "";
       %>
				<logic:iterate id="pjtTitle" name="eventPjtNameList">
					<%
                    String startDate = "";
                    String endDate = "";

                    if (index%2 == 0) {
                    strBgColor = "#D6DCE5";
                    } else {
                    strBgColor="#DCE5F1";
                    }

                 CoiAnnualProjectEntityDetailsBean pjtBean = (CoiAnnualProjectEntityDetailsBean) projectNameList.get(index);
                CoiCommonService coiCommonService1 = CoiCommonService.getInstance();
                 entityNum = pjtBean.getEntityNumber();
                 int moduleCode = pjtBean.getModuleCode();
                 String projectType ="";
               if(moduleCode == 1) {
                   projectType = "Award";
               }
               else if(moduleCode == 2){
                   projectType = "Proposal";
                   }
              else if(moduleCode == 3){
                   projectType = "Proposal";
                   }
              else if(moduleCode == 7){
                   projectType = "IRB Protocol";
              }
               else if(moduleCode == 9){
                   projectType = "IACUC Protocol";
                }
                  else if(moduleCode == 0){
                   projectType = "Travel";
                }
               %>
					<tr bgcolor="<%=strBgColor%>" id="row<%=index%>" class="rowLine"
						onmouseover="className='rowHover rowLine'"
						onmouseout="className='rowLine'" height="22px">

						<td width="7%" style="padding: 2px 0 2px 10px; text-align: left;"><%=projectType%></td>
						<td width="6%" style="padding: 2px 0 2px 10px; text-align: left;"><bean:write
								name="pjtTitle" property="coiProjectId" /></td>
						<td width="32%" style="padding: 2px 0 2px 10px; text-align: left;">
							<%  String title= pjtBean.getCoiProjectTitle();
                       String comment="";
                       if(title==null){comment="";
                       }else{
                       comment=title;
                       if(comment.trim().length()>120)
                           {comment= comment.substring(0, 120);
                       comment+="<a href=\"javaScript:showTitle('txtAreaComment"+index+"','divTXT')\">[...]</a>";
                       }}%><%=comment%> <input
							id="txtAreaComment<%=index%>" name="finEntDet" type="hidden"
							value="<%=pjtBean.getCoiProjectTitle()%>" />
						</td>
						<% if(!projectType.equalsIgnoreCase("Travel")) {%>
						<td width="9%" style="padding: 2px 0 2px 10px; text-align: left;">
							<bean:write name="pjtTitle" property="entityStatus" />
						</td>
						<%}else{%>
						<td width="4%" style="padding: 2px 0 2px 10px; text-align: left;"></td>
						<%}%>
					</tr>

					<%index++;%>
				</logic:iterate>
			</logic:present>
		</table>
		<%}%>

	</logic:notPresent>
	<%if(isDue){%>
	<html:form action="/showQuestionnaire.do">
		<table class="table" style="width: 100%; height: 40px;" border="0">
			<tr>
				<td height="15%" align="center"><font color="#800517"
					size="2px" face="verdana"><b><bean:message bundle="coi"
								key="Annualreview.due.Message" /></b></font>&nbsp;&nbsp;<html:button
						property="discSubmit" styleClass="clsavebutton"
						value="Prepare and Submit Annual Disclosure" style="width:270px;"
						onclick="javascript:cal();" /></td>
			</tr>
		</table>
	</html:form>
	<%}%>
	<div id="divTXT" class="dialog"
		style="width: auto; overflow: hidden; position: absolute;">
		<table width="100%" border="0" cellpadding="1" cellspacing="1"
			class="table">
			<tr style="background-color: #6E97CF; font-size: 12px; margin: 0;">
				<td style="padding: 2px 0px 5px 0px"><font color="#FFFFFF"
					size="2px"><b>Project Title</b></font></td>
			</tr>
			<tr style="height: 100px; width: 200px;">

				<td align="left" style="height: 100px;">
					<div
						style="height: 100px; width: 650px; overflow-x: scroll; overflow-y: scroll; overflow: auto;">
						<label id="TxtArea"></label>
					</div>
				</td>
			</tr>
			<tr style="background-color: #6E97CF;">
				<td align="center"><input type="button" value="Close"
					class="clsavebutton" onclick="hm('divTXT');" /></td>

			</tr>
		</table>
	</div>
</body>
</html>
