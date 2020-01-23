<%-- 
    Document   : showAllWipDisclView
    Created on : Apr 20, 2012, 10:34:23 AM
    Author     : ajay
--%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/coeus-utils.tld" prefix="coeusUtils"%>
<%@ page
	import="java.util.Vector,edu.mit.coeuslite.coiv2.beans.CoiDisclosureBean,edu.mit.coeuslite.coiv2.services.CoiCommonService,java.util.Date;"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>C O I</title>
<%String path = request.getContextPath();%>
<script src="<%=path%>/coeuslite/dartmouth/utils/scripts/divSlide.js"></script>
<script src="<%=path%>/coeuslite/dartmouth/utils/scripts/Balloon.js"></script>
<link href="<%=path%>/coeuslite/dartmouth/utils/css/coeus_styles.css"
	rel="stylesheet" type="text/css" />
<link
	href="<%=request.getContextPath()%>/coeuslite/mit/coiv2/css/layout.css"
	rel="stylesheet" type="text/css" />
<link
	href="<%=request.getContextPath()%>/coeuslite/mit/coiv2/css/styles.css"
	rel="stylesheet" type="text/css" />
<link
	href="<%=request.getContextPath()%>/coeuslite/mit/coiv2/css/collapsemenu.css"
	rel="stylesheet" type="text/css" />
</head>
<script src="js/jquery.js" type="text/javascript"></script>
<script type="text/javascript">
function selectProj(index){
    var a = "imgtoggle"+index;
//    alert(a);
//    alert("=====index===="+index+"  "+lastinx);
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
</script>
<body>
	<table id="bodyTable" class="table" style="width: 960px;" border="0"
		align="center">
		<tr style="background-color: #6E97CF; height: 22px; width: 960px;">
			<td colspan="6"
				style="background-color: #6E97CF; color: #FFFFFF; font-size: 14px; font-weight: bold; margin: 0; padding: 2px 0 2px 10px; position: relative; text-align: left;"><strong>List
					Of WIP</strong></td>
		</tr>
		<tr>
			<td
				style="background-color: #6E97CF; color: #173B63; font-size: 12px; font-weight: bold;">&nbsp;</td>
			<!--<td style="background-color:#6E97CF;color:#173B63;font-size:12px;font-weight:bold;"><strong>Disclosure Number</strong></td>-->
			<td
				style="background-color: #6E97CF; color: #173B63; font-size: 12px; font-weight: bold;"><strong>Owned
					By</strong></td>
			<td
				style="background-color: #6E97CF; color: #173B63; font-size: 12px; font-weight: bold;">Department</td>
			<td
				style="background-color: #6E97CF; color: #173B63; font-size: 12px; font-weight: bold;"><strong>Last
					Update</strong></td>
			<td
				style="background-color: #6E97CF; color: #173B63; font-size: 12px; font-weight: bold;"><strong>Expiration
					Date</strong></td>
		</tr>
		<%--<logic:present name="message">
<logic:equal value="false" name="message">
<tr>
<td colspan="6">
<font color="red">No disclosures assigned by admin</font>
</td>
</tr>  
</logic:equal>
</logic:present>--%>

		<logic:empty name="entityNameList">
			<tr>
				<td colspan="6"><font color="red">No Disclosure found</font></td>
			</tr>
		</logic:empty>

		<logic:notPresent name="entityNameList">
			<tr>
				<td colspan="6"><font color="red">No Disclosure Found</font></td>
			</tr>
		</logic:notPresent>

		<logic:present name="entityNameList">

			<%
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
%>
				<tr bgcolor="<%=strBgColor%>" id="row<%=index%>" class="rowLine"
					onmouseover="className='rowHover rowLine'"
					onmouseout="className='rowLine'" height="22px">
					<td width="5%"><img
						src='<%=request.getContextPath()%>/coeusliteimages/plus.gif'
						border='none' id="imgtoggle<%=index%>" name="imgtoggle<%=index%>"
						border="none" onclick="javascript:selectProj(<%=index%>);" /> <img
						src='<%=request.getContextPath()%>/coeusliteimages/minus.gif'
						style="display: none;" border='none' id="imgtoggleminus<%=index%>"
						name="imgtoggleminus<%=index%>" border="none"
						onclick="javascript:selectProj(<%=index%>);" /></td>

					<td><b><bean:write name="pjtTitle" property="userName" /></b>
					</td>
					<td><b><bean:write name="pjtTitle" property="department" /></b>
					</td>
					<td><b><coeusUtils:formatDate name="pjtTitle"
								property="updateTimestampNew" formatString="yyyy-MM-dd  hh:mm a" /></b>
					</td>
					<td><b><coeusUtils:formatDate name="pjtTitle"
								property="expirationDate" formatString="MM/dd/yyyy" /></b></td>
				</tr>
				<tr>
					<td colspan="6">
						<div id="<%=index%>"
							style="height: 1px; width: 955px; visibility: hidden; background-color: #9DBFE9; overflow-x: hidden; overflow-y: scroll;">
							<table id="bodyTable" class="table"
								style="width: 960px; height: 100%;" border="0">
								<tr>

									<td
										style="background-color: #6E97CF; color: #FFFFFF; font-size: 12px; font-weight: bold;"><strong>Disc.Event</strong></td>
									<td
										style="background-color: #6E97CF; color: #FFFFFF; font-size: 12px; font-weight: bold;"><strong>Event
											#</strong></td>
									<!--<td style="background-color:#6E97CF;color:#FFFFFF;font-size:12px;font-weight:bold;"><strong>Disclosure Status</strong></td>-->
									<td
										style="background-color: #6E97CF; color: #FFFFFF; font-size: 12px; font-weight: bold;">Review
										Status</td>
									<td
										style="background-color: #6E97CF; color: #FFFFFF; font-size: 12px; font-weight: bold;"><strong>Last
											Update</strong></td>
									<td
										style="background-color: #6E97CF; color: #FFFFFF; font-size: 12px; font-weight: bold;"><strong>Last
											Update By</strong></td>
									<td
										style="background-color: #6E97CF; color: #FFFFFF; font-size: 12px; font-weight: bold;"><strong>Submit
											Date</strong></td>
									<td
										style="background-color: #6E97CF; color: #FFFFFF; font-size: 12px; font-weight: bold;">&nbsp;</td>
								</tr>
								<%         
            session.removeAttribute("isEvent");
           int i = 0;
           Vector disclVector = (Vector) request.getAttribute("pjtEntDetView");
           int code = 0;
           String ModuleName = "";
%>
								<logic:iterate id="pjtEntView" name="pjtEntDetView">
									<% CoiDisclosureBean projectName = (CoiDisclosureBean)projectNameList.get(index);
            String name = projectName.getCoiDisclosureNumber();
             String pjctName = projectName.getPjctName();
            
             CoiDisclosureBean bean = (CoiDisclosureBean) disclVector.get(i);
              String moduleKey=bean.getModuleItemKey();
             request.getSession().setAttribute("selectedPjct",moduleKey);
             coiCommonService = CoiCommonService.getInstance();
             String update = bean.getUpdateTimestampNew();//coiCommonService.getFormatedDate(bean.getUpdateTimestamp());
             String expire = coiCommonService.getFormatedDate(bean.getExpirationDate());
             code = bean.getModuleCode();

             if(code == 1) {
                 ModuleName = "Award";
             }
             else if(code == 2) {
                 ModuleName = "Proposal";
             }
             else if(code == 3) {
                 ModuleName = "IRB Protocol";
             }
             else if(code == 4) {
                 ModuleName = "IACUC Protocol";
             }
             else if(code == 5) {
                 ModuleName = "Annual";
             }
             else if(code == 6) {
                 ModuleName = "Revision";
             }
              else if(code == 8) {
                 ModuleName = "Travel";
             }
%>
									<logic:equal value="<%=name%>" property="coiDisclosureNumber"
										name="pjtEntView">
										<tr class="rowLineLight"
											onmouseover="className='rowHover rowLine'"
											onmouseout="className='rowLineLight'" height="22px">
											<!--    <td>
<%String link1 = request.getContextPath()+"/getannualdisclosure.do?selected=current&param1=" + bean.getCoiDisclosureNumber() +"&param2=" + bean.getSequenceNumber()+"&param7="+ bean.getPersonId()+"&param5="+ModuleName+"&param6="+"othersDiscl"+"&selectedPjct="+moduleKey+"&fromReview=showAllReview";
    if(ModuleName!= null && ModuleName.equals("Travel")){
        link1= request.getContextPath()+"/travelDetails.do?selected=current&param1=" + bean.getCoiDisclosureNumber()+"&param7="+ bean.getPersonId()+"&edit=true&view=true&selectedPjct="+moduleKey+"&sequenceNumber="+bean.getSequenceNumber()+"&param6="+"throughShowAllDiscl";
    }
%>
<html:link style="color: #003399;font-family:Arial,Helvetica,sans-serif;font-size:12px;font-weight:bold;float: left;font-size:12px;" action="<%=link1%>">View</html:link>
&nbsp;&nbsp;
    </td>-->
											<td><a href='<%=link1%>'> <b>
														<%--<bean:write name="pjtEntView" property="moduleName"/>--%><%=ModuleName%></b></a>
											</td>
											<td><a href='<%=link1%>'><b><bean:write
															name="pjtEntView" property="moduleItemKey" /></b></a></td>
											<!--<td>
<a href='<%=link1%>' ><b><bean:write name="pjtEntView" property="disclosureStatus"/></b></a>
</td>-->
											<td><a href='<%=link1%>'><b><bean:write
															name="pjtEntView" property="reviewStatus" /></b></a></td>
											<td><a href='<%=link1%>'><b><coeusUtils:formatDate
															name="pjtEntView" property="updateTimestampNew"
															formatString="yyyy-MM-dd  hh:mm a" /></b></a></td>
											<td><a href='<%=link1%>'><b><bean:write
															name="pjtEntView" property="updateUser" /></b></a></td>
											<td><a href='<%=link1%>'><b><coeusUtils:formatDate
															name="pjtEntView" property="updateTimestampNew"
															formatString="MM/dd/yyyy" /></b></a></td>
											<td>
												<% String linkPrint = "/approveddisclosureprint.do?selected=current&fromReview=showAllAnnualReview&param1="+ projectName.getCoiDisclosureNumber() +"&param2=" +bean.getSequenceNumber()+"&param7="+ projectName.getPersonId();%>
												<b><html:link action="<%=linkPrint%>" target="_blank">Print&nbsp;&nbsp;&nbsp;</html:link></b>
											</td>
										</tr>
									</logic:equal>
									<%i++;%>
								</logic:iterate>
							</table>
						</div>
					</td>
				</tr>
				<%index++;%>
			</logic:iterate>
			<%request.getSession().setAttribute("lastIndex",index);%>
		</logic:present>
	</table>
</body>
<script>
    function last(){
      var lastinx = "<%=request.getSession().getAttribute("lastIndex")%>";
   //   alert(lastinx);
      for(var i=0;i<lastinx;i=i+1){
  //    alert(i);
      var b = "imgtoggle"+i;
    if(document.getElementById(i).style.visibility == 'visible'){
      document.getElementById(i).style.visibility = 'hidden';
      document.getElementById(i).style.height = "1px";
      document.getElementById(b).src="<%=path%>/coeusliteimages/plus.gif";
}}}
</script>
</html>
