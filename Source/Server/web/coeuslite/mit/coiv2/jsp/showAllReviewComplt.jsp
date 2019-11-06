<%-- 
    Document   : showAllReviewComplt
    Created on : Apr 20, 2012, 11:57:15 AM
    Author     : veena
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
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

	<table id="bodyTable" class="table" style="width: 985px;" border="0"
		align="center">
		<tr style="background-color: #6E97CF; height: 22px; width: 985px;">
			<td colspan="9"
				style="background-color: #6E97CF; color: #FFFFFF; font-size: 14px; font-weight: bold; margin: 0; padding: 2px 0 2px 10px; position: relative; text-align: left;"><strong>List
					of Assigned Reviews Completed </strong></td>
		</tr>
		<tr>
			<td
				style="background-color: #6E97CF; color: #173B63; font-size: 12px; font-weight: bold;">&nbsp;</td>
			<td
				style="background-color: #6E97CF; color: #173B63; font-size: 12px; font-weight: bold;"><strong>Owned
					By</strong></td>
			<td
				style="background-color: #6E97CF; color: #173B63; font-size: 12px; font-weight: bold;"><strong>Disc.Event</strong></td>
			<td
				style="background-color: #6E97CF; color: #173B63; font-size: 12px; font-weight: bold;">Event
				#</td>
			<td
				style="background-color: #6E97CF; color: #173B63; font-size: 12px; font-weight: bold;"><strong>Disclosure
					Status</strong></td>
			<td
				style="background-color: #6E97CF; color: #173B63; font-size: 12px; font-weight: bold;"><strong>Department</strong></td>
			<td
				style="background-color: #6E97CF; color: #173B63; font-size: 12px; font-weight: bold;"><strong>Submit
					Date</strong></td>
			<td
				style="background-color: #6E97CF; color: #173B63; font-size: 12px; font-weight: bold;"><strong>Expiration
					Date</strong></td>
			<td
				style="background-color: #6E97CF; color: #173B63; font-size: 12px; font-weight: bold;"><strong>&nbsp;&nbsp;&nbsp;</strong></td>
		</tr>


		<logic:present name="pjtEntDetView" scope="request">
			<%
            session.removeAttribute("isEvent");  
            String strBgColor = "#DCE5F1";
            Vector projectNameList = (Vector)request.getAttribute("pjtEntDetView");
            int index = 0;
            int sequenceNum = 0;
       %>
			<logic:iterate id="projectList" name="pjtEntDetView">
				<%
                                if (index%2 == 0) {
                                strBgColor = "#D6DCE5";
                                } else {
                                strBgColor="#DCE5F1"; 
                                }
     %>
				<%
             String dispositionStatus="";
             //Integer dispositionStatusCode==1
              boolean isFEPresent = false;
             CoiDisclosureBean beanHeader = (CoiDisclosureBean) projectNameList.get(index);
             CoiCommonService coiCommonService = CoiCommonService.getInstance();
             String updateHeader = coiCommonService.getFormatedDate(beanHeader.getUpdateTimestamp());
             String expireHeader = coiCommonService.getFormatedDate(beanHeader.getExpirationDate());
             request.setAttribute("dispositionStatus","approve");
             dispositionStatus=(String)request.getAttribute("dispositionStatus");
             //if(dispositionStatusCode==1){
               //  dispositionStatus="approve";
             //}
             //if(beanHeader.getIsFEPresent()!=null && beanHeader.getIsFEPresent() == 1) {
                 //isFEPresent = true;
            // }
             sequenceNum = (Integer)beanHeader.getSequenceNumber();
             int code = beanHeader.getModuleCode(); 
             String moduleName = "";
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
                    moduleName = "Annual"; 
                }
             else if(code == 8) {
                    moduleName = "Travel";
                      }
        %>

				<tr bgcolor="<%=strBgColor%>" id="row<%=index%>" class="rowLine"
					onmouseover="className='rowHover rowLine'"
					onmouseout="className='rowLine'" style="height: 20px;">
					<td><img
						src='<%=request.getContextPath()%>/coeusliteimages/plus.gif'
						border='none' id="imgtoggle<%=index%>" name="imgtoggle<%=index%>"
						border="none" onclick="javaScript:selectProj(<%=index%>);" /> <img
						src='<%=request.getContextPath()%>/coeusliteimages/minus.gif'
						style="display: none;" border='none' id="imgtoggleminus<%=index%>"
						name="imgtoggleminus<%=index%>" border="none"
						onclick="javaScript:selectProj(<%=index%>);" /></td>
					<% String link1 = request.getContextPath()+"/getannualdisclosure.do?&param1=" + beanHeader.getCoiDisclosureNumber() +"&param2=" + beanHeader.getSequenceNumber()+"&param7="+ beanHeader.getPersonId()+"&param5="+moduleName+"&param6="+"throughShowAllDiscl"+"&fromReview=showAllAnnualReview";%>
					<td class="copy" style="color: #173B63;"><a href='<%=link1%>'><b><bean:write
									name="projectList" property="userName" /> </b></a>

						<td><a href='<%=link1%>'><b> <%=moduleName%>
							</b></a></td>

						<td><a href='<%=link1%>'><b><bean:write
										name="projectList" property="moduleItemKey" /></b></a></td>
						<td><a href='<%=link1%>'> <b><bean:write
										name="projectList" property="disclosureStatus" /></b></a></td>
						<td><a href='<%=link1%>'> <b><bean:write
										name="projectList" property="department" /></b></a></td>
						<td><a href='<%=link1%>'> <b><bean:write
										name="projectList" property="updateTimestamp"
										format="MM/dd/yyyy H:MM" /></b></a></td>
						<td><a href='<%=link1%>'> <b><bean:write
										name="projectList" property="expirationDate"
										format="MM/dd/yyyy" /></b></a></td> <% String linkPrint = "/approveddisclosureprint.do?selected=current&fromReview=showAllAnnualReview&param1="+ beanHeader.getCoiDisclosureNumber() +"&param2=" + beanHeader.getSequenceNumber()+"&param7="+ beanHeader.getPersonId();%>
						<td width=""><u><html:link action='<%=linkPrint%>'
									target="_blank">Print</html:link></u></td></td>
				</tr>

				<tr>
					<td colspan="9">
						<div id="<%=index%>"
							style="height: 1px; width: 985px; visibility: hidden; background-color: #9DBFE9; overflow-x: hidden; overflow-y: scroll;">
							<table id="bodyTable" class="table"
								style="width: 985px; height: 100%;" border="0">
								<tr>
									<td
										style="background-color: #6E97CF; color: #FFFFFF; font-size: 12px; font-weight: bold;"
										width="18%">Assigned Reviewer</td>
									<td
										style="background-color: #6E97CF; color: #FFFFFF; font-size: 12px; font-weight: bold;">Recommended
										Action</td>
									<td
										style="background-color: #6E97CF; color: #FFFFFF; font-size: 12px; font-weight: bold;">Department</td>
									<td
										style="background-color: #6E97CF; color: #FFFFFF; font-size: 12px; font-weight: bold;"><strong>Last
											Update</strong></td>
									<td
										style="background-color: #6E97CF; color: #FFFFFF; font-size: 12px; font-weight: bold;"><strong>&nbsp;&nbsp;&nbsp;</strong></td>
									<td
										style="background-color: #6E97CF; color: #FFFFFF; font-size: 12px; font-weight: bold;"><strong>&nbsp;&nbsp;&nbsp;</strong></td>
									<td
										style="background-color: #6E97CF; color: #FFFFFF; font-size: 12px; font-weight: bold;"><strong>&nbsp;&nbsp;&nbsp;</strong></td>
									<td
										style="background-color: #6E97CF; color: #FFFFFF; font-size: 12px; font-weight: bold;"><strong>&nbsp;&nbsp;&nbsp;</strong></td>
								</tr>
								<logic:present name="message">
									<logic:equal value="false" name="message">
										<tr>
											<td colspan="9"><font color="red">No disclosures
													found</font></td>
										</tr>
									</logic:equal>
								</logic:present>

								<%         int i = 0;
           Vector disclVector = (Vector) request.getAttribute("entityNameList");
%>

								<logic:iterate id="pjtEntView" name="entityNameList"
									type="edu.mit.coeuslite.coiv2.beans.CoiDisclosureBean">
									<%
        
           
            CoiDisclosureBean projectName = (CoiDisclosureBean)disclVector.get(i);
            request.getSession().removeAttribute("selectedPjct");
            request.getSession().removeAttribute("disclPjctModuleName");
            String name = beanHeader.getCoiDisclosureNumber();
           //request.getSession().setAttribute("selectedPjct", name);
            String projectType=projectName.getModuleName();
            request.getSession().setAttribute("pjctType", projectType);
             CoiDisclosureBean bean = (CoiDisclosureBean) disclVector.get(i);
             String moduleName1 = "";
             String name1 = projectName.getCoiDisclosureNumber();
             int code1 = bean.getModuleCode();
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
              String update = coiCommonService.getFormatedDate(bean.getUpdateTimestamp());
              String expire = coiCommonService.getFormatedDate(bean.getExpirationDate());
  %>
									<logic:equal value="<%=name%>" property="coiDisclosureNumber"
										name="pjtEntView">

										<%if(sequenceNum==seqNo) {%>
										<tr class="rowLineLight"
											onmouseover="className='rowHover rowLine'"
											onmouseout="className='rowLineLight'" height="22px">

											<td><b><bean:write name="pjtEntView"
														property="reviewerName" /></b></td>
											<td><b><bean:write name="pjtEntView"
														property="recmdDescptn" /></b></td>
											<td><b><bean:write name="pjtEntView"
														property="department" /></b></td>
											<td colspan="5"><b><bean:write name="pjtEntView"
														property="updateTimestamp" format="MM/dd/yyyy H:MM" /></b></td>
										</tr>
										<%}%>
									</logic:equal>
									<% i++;
       %>
									<% request.getSession().setAttribute("lastIndex",index); %>
								</logic:iterate>
							</table>
						</div>
					</td>
				</tr>
				<%
  index++;
 %>
			</logic:iterate>
			<% request.getSession().setAttribute("lastIndex",index); %>
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