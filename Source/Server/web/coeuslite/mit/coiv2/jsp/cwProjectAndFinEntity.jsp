<%@ page
	import="org.apache.struts.validator.DynaValidatorForm,edu.mit.coeuslite.coiv2.formbeans.CoiPersonProjectDetailsForm,java.util.HashMap,edu.mit.coeuslite.coiv2.utilities.CoiConstants,java.util.*,
         edu.mit.coeuslite.coiv2.beans.CoiPersonProjectDetails,java.util.Vector,edu.mit.coeus.coi.bean.ComboBoxBean,org.apache.struts.taglib.html.JavascriptValidatorTag,
         edu.mit.coeuslite.coiv2.beans.CoiProjectEntityDetailsBean,edu.mit.coeuslite.coiv2.services.CoiCommonService,edu.mit.coeuslite.coiv2.beans.CoiProtocolInfoBean,edu.mit.coeuslite.coiv2.beans.CoiFinancialEntityBean;"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/coeus-utils.tld" prefix="coeusUtils"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">

<html xmlns="http://www.w3.org/1999/xhtml">
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
<script
	src="<%=request.getContextPath()%>/coeuslite/mit/utils/scripts/modal/modaldbox.js"></script>
<script type="text/javascript">
    function showDialog(txtAreaID)
 {
     var width  = 650;//document.getElementById('divTXTDetails').style.pixelWidth;
     var height = 170;//document.getElementById('divTXTDetails').style.pixelHeight;

     sm('divTXTDetails',width,height);

     var txtValue=document.getElementById(txtAreaID).value;
    // alert(txtValue);
     document.getElementById('TxtAreaComments').innerHTML=txtValue;
     document.getElementById("mbox").style.left="385";//450
     document.getElementById("mbox").style.top="250";//250
 }
</script>
<%
            String commentsStr="";
        String[] val = (String[]) request.getAttribute("financialArrayEntityList");
                    String path = request.getContextPath();
                   /* HashMap thisUserRights = (HashMap) request.getAttribute("rights");
                    Integer permissionTYpe = (Integer) thisUserRights.get(CoiConstants.DISCL);
                    boolean disable = false;
                    String disabled="";
                    if (permissionTYpe == 2) {
                        disabled="";
                        disable = false;
                    }*/

        %>
<html:javascript formName="finEntity" dynamicJavascript="true"
	staticJavascript="true" />
</head>
<script src="js/jquery.js" type="text/javascript"></script>
<script type="text/javascript">
        var globinFinEnity = "null";
        var result=[];
        $(document).ready(function() {
            $("#menu > li > a[@class=collapsed] ").find("+ ul").slideToggle("medium");
            $("#menu  > li > a").click(function() {
                $(this).toggleClass("expanded").toggleClass("collapsed").find("+ ul").slideToggle("medium");
            });
        });
         
        function saveFinEntityConflict()
        {                   
           var flag=false;
           var relationFlag=false;
               <%
                   Vector financialList = (Vector) request.getAttribute("financialEntityList");
                   int size=financialList.size();

                      for(int i=0;i<size;i++)
                       {
                          CoiFinancialEntityBean finBean = (CoiFinancialEntityBean) financialList.get(i);
                          String typeName = finBean.getCode();
                   %>
                          var id="<%=typeName%>";
                          var relationId="orgRelDesc<%=typeName%>";
          
                    if((document.getElementById(id).value==null) ||(document.getElementById(id).value==" "))
                        {
          
                         flag=true;
                          
                        }
                        if((document.getElementById(relationId).value==null) ||(document.getElementById(relationId).value==" "))
                            {
                               relationFlag=true;
                            }
                   <%  } %>
          
                    if(flag)
                        {
                            alert("Please select the COI status...!!");
                            
                        }
                        else if(relationFlag)
                            {
                                 alert("Please enter the relationship description...!!");
                            }
                    else
                        {
                        var operationType='<%=request.getParameter("operationType")%>'
            document.forms[0].action= '<%=path%>' + "/saveProjectDetails.do?saveFinEntityConflict="+result+"&operationType="+operationType;
            document.forms[0].submit();
                        }
                        }
        function changeStatusCode()  
        {
            debugger;
         
            var str=[];
            var k=0;
        <% if(val != null){
        for (int i = 0; i < val.length; i++) {%> 
              str[k]='<%=val[i]%>';  
                k++; 
        <%}}%>
                for(var i=0; i<str.length; i++)
                {
                    var propertyName=str[i];
                    var value=document.forms[0].coiStatusCode.value;
                    document.getElementById(propertyName).value=value;
                     
                } 

            }

         function exitToCoi(){ 
            var answer = confirm("This operation will discard your current changes.. Do you want to continue..?");
                 if(answer) {
                    document.location = '<%=request.getContextPath()%>/exitDisclosure.do';
                    window.location;
                 }
         }
    </script>

<html:form action="/saveProjectDetails.do">
	<table class="table" style="width: 100%;" border="0">
		<%int count = 0;
            String startDate = "";
            String endDate = "";
            String pjctTitle="";
            String moduleItemKey="";
            String awardTitle="";
            String pjctId="";
            String eventName = "";
            String purpose = "";

             String projectType = (String) request.getSession().getAttribute("projectType");
               if(projectType.equalsIgnoreCase("Proposal")||projectType.equalsIgnoreCase("iacucProtocol")||projectType.equalsIgnoreCase("Protocol")||projectType.equalsIgnoreCase("Award") ||projectType.equalsIgnoreCase("Travel")){
             Vector pjctDetails = (Vector)request.getSession().getAttribute("projectDetailsListInSeesion");
             if(pjctDetails !=null){
            CoiPersonProjectDetails coiPersonProjectDetails =(CoiPersonProjectDetails) pjctDetails.get(0);
            pjctTitle=coiPersonProjectDetails.getCoiProjectTitle();
            moduleItemKey=coiPersonProjectDetails.getModuleItemKey();
            awardTitle=coiPersonProjectDetails.getAwardTitle();
            pjctId=coiPersonProjectDetails.getModuleItemKey();
            eventName = coiPersonProjectDetails.getEventName();
            purpose = coiPersonProjectDetails.getPurpose();
            if(purpose == null) {
                purpose = "";
             }
            CoiCommonService coiCommonService1 = CoiCommonService.getInstance();
            if(coiPersonProjectDetails.getCoiProjectStartDate()!=null)
            {
                startDate = (String) coiCommonService1.getFormatedDate(coiPersonProjectDetails.getCoiProjectStartDate());
            }
            if(coiPersonProjectDetails.getCoiProjectEndDate()!=null)
            {
              endDate = (String) coiCommonService1.getFormatedDate(coiPersonProjectDetails.getCoiProjectEndDate());
            }}}
                %>
		<%
                if(projectType.equals("Protocol") || projectType != null && projectType.equalsIgnoreCase("iacucProtocol")){%>
		<tr>
			<td colspan="2"><b>For your project listed below, think
					about your relationship with all your Financial Entities and update
					the Financial Interest status as appropriate:</b></td>
		</tr>
		<tr>
			<td colspan="2"><b>Project #:</b><%=pjctId%></td>
		</tr>
		<tr>
			<td colspan="2"><b>Project Title :</b><%=pjctTitle%></td>
		</tr>
		<tr>
			<td style="width: 415px;"><b>Application Date :</b><%=startDate%></td>
			<td style="width: 405px;"><b>Expiration Date :</b><%=endDate%></td>
		</tr>
		<%}else if(projectType.equals("Proposal")){%>
		<tr>
			<td colspan="2"><b>For your project listed below, think
					about your relationship with all your Financial Entities and update
					the Financial Interest status as appropriate:</b></td>
		</tr>
		<tr>
			<td colspan="2"><b>Project # :</b><%=moduleItemKey%></td>
		</tr>
		<tr>
			<td colspan="2"><b>Project Title :</b><%=pjctTitle%></td>
		</tr>
		<tr>
			<td style="width: 415px;"><b>Start Date :</b><%=startDate%></td>
			<td style="width: 405px;"><b>End Date :</b><%=endDate%></td>
		</tr>
		<%} else if(projectType.equals("Award")){ %>
		<tr>
			<td colspan="2"><b>For your project listed below, think
					about your relationship with all your Financial Entities and update
					the Financial Interest status as appropriate:</b></td>
		</tr>
		<tr>
			<td colspan="2"><b>Project #:</b><%=moduleItemKey%></td>
		</tr>
		<tr>
			<td colspan="2"><b>Project Title :</b><%=awardTitle%></td>
		</tr>
		<tr>
			<td style="width: 410px;"><b>Start Date :</b><%=startDate%></td>
			<td style="width: 410px;"><b>End Date :</b><%=endDate%></td>
		</tr>
		<% }else if(projectType.equals("Travel")){%>
		<tr>
			<td colspan="2"><b>For your project listed below, think
					about your relationship with all your Financial Entities and update
					the Financial Interest status as appropriate:</b></td>
		</tr>
		<tr>
			<td colspan="2"><b>Project # :</b><%=moduleItemKey%></td>
		</tr>
		<tr>
			<td colspan="2"><b>Project Title :</b><%=eventName%></td>
		</tr>
		<tr>
			<td style="width: 410px;"><b>Start Date :</b><%=startDate%></td>
			<td style="width: 410px;"><b>End Date :</b><%=endDate%></td>
		</tr>
		<%}%>
		<tr>
			<td class="copybold" colspan="2"><img width="100%" height="2"
				border="0"
				src="<%=request.getContextPath()%>/coeusliteimages/line4.gif"></img></td>
		</tr>

		<tr>
			<td colspan="2"><b> Set Financial Interest Status for all
					Financial Entities to: </b> <html:select property="coiStatusCode"
					onchange="changeStatusCode();" style="width:190px">
					<html:option value="--------Please Select---------" />
					<html:options collection="typeList" property="code"
						labelProperty="description" />
				</html:select></td>
		</tr>
		<tr>
			<td class="copybold" colspan="2"><img width="100%" height="2"
				border="0"
				src="<%=request.getContextPath()%>/coeusliteimages/line4.gif"></img></td>
		</tr>

		<%          Vector finList = (Vector) request.getAttribute("financialEntityList");
                            Vector typeList = (Vector) request.getAttribute("typeList");
                           int inx=0;
                %>



		<html:hidden property="moduleItemKey" />
		<%
                            String[] rowColors = {"#D6DCE5", "#DCE5F1"};
                            int i = 0;
                            String rowColor = "";
                            Vector projectDetails = (Vector) request.getAttribute("projectDetails");

                %>

		<logic:iterate id="financialEntity" name="financialEntityList">
			<%

                                if (i == 0) {
                                    rowColor = rowColors[0];
                                    i = 1;
                                } else {
                                    rowColor = rowColors[1];
                                    i = 0;
                                }

                    %>
			<tr>
				<td bgcolor="#376DAA" colspan="2"><br />
					<div style="background-color: #376DAA" /></td>
			</tr>
			<tr class="copybold" bgcolor="#9DBFE9" class="rowLine">
				<td colspan="2"><br />
					<div style="font-size: 13; float: left;">
						<b>Financial Entity: &emsp; &nbsp; <bean:write
								name="financialEntity" property="description" /></b>
					</div>


					<div style="font-size: 13; float: right;">
						<b>COI Status:&nbsp;</b>
						<%
                                            commentsStr="";
                                            CoiFinancialEntityBean typeBean = (CoiFinancialEntityBean) finList.get(count);
                                            String nameType = typeBean.getCode();
                                            String beancodeConflict=null;
                                            count++;
                                            String statusCode = typeBean.getStatusCode().toString();
                                            String comment=typeBean.getRelationshipDescription();
                                            if(comment==null){comment="";}

                                %>
						<select id="<%=nameType%>" name="<%=nameType%>"
							style="width: 190px">
							<option value=" ">--------Please Select---------</option>
							<%
                                                if (typeList != null) {
                                                    ComboBoxBean bean;
                                                    for (int index1 = 0; index1 < typeList.size(); index1++) {
                                                        bean = (ComboBoxBean) typeList.get(index1);
                                                        beancodeConflict= bean.getDescription();
                                                        String selected = "";
                                                        if (statusCode != null && bean.getCode().equals(statusCode)) {
                                                            selected = "selected"; 
                                                        } 

                                    %>
							<option <%=selected%> value="<%=bean.getCode()%>"<%--onclick="javascript:checkConflict('<%=beancodeConflict%>','<%= inx%>');"--%>  >
								<%=bean.getDescription()%>
							</option>
							<% }
                                                } 
                                    %>
						</select>
					</div></td>
			</tr>
			<%String commentObj=typeBean.getRelationshipDescription();

        String comments="";
        if(commentObj==null){comments="";}
        else{comments=(String)commentObj;
            if(comments.trim().length()>210)
                {comments=(String)comments.substring(0,210);
                 comments+="<a href=\"javaScript:showDialog('txtAreaCommentId"+nameType+"')\">[...]</a>";

                 }}%>
			<tr>
				<td width="17%"></td>
				<td colspan="2"><b>Description of entity's area of business
						focus and specifically your work with it.</b></td>
			</tr>

			<tr>
				<td width="17%"></td>
				<td colspan="2"><font color="black"> <i>" <%=comments%>
							"
					</i></font> <textarea id="relDesc<%=nameType%>" style="display: none"
						name="relDesc<%=nameType%>" cols="77" rows="3"><%=typeBean.getRelationshipDescription()%></textarea>
				</td>
			</tr>
			<tr>
				<td colspan="3">
					<%--<input type="hidden" name="actionFrom" value="coiAnnFin"/>--%>
					<!--            coeus -3424 starts--> <input
					id="txtAreaCommentId<%=nameType%>" name="finEntDet" type="hidden"
					value="<%=typeBean.getRelationshipDescription()%>" /> <!--            coeus -3424 ends-->
					&nbsp;&nbsp;
				</td>
			</tr>
			<tr>
				<td></td>
			</tr>
			<tr>
				<td></td>
			</tr>
			<tr>
				<td></td>
			</tr>
			<tr>
				<td></td>
			</tr>
			<tr>
				<td></td>
			</tr>
			<tr>
				<td></td>
			</tr>
			<tr>
				<td></td>
			</tr>
			<tr>
				<td></td>
			</tr>
			<tr>
				<td></td>
			</tr>
			<tr>
				<td></td>
			</tr>
			<tr>
				<td></td>
			</tr>

			<%comment=typeBean.getOrgRelationDescription();if(comment==null){comment="";}%>

			<tr>
				<td width="17%"></td>
				<td colspan="2"><b>Describe the relationship between this
						project and this entity, include any involvement of students,
						staff,and</b></td>
			</tr>
			<tr>
				<td width="17%"></td>
				<td colspan="2"><b> MIT resources. If there is no
						relationship, explain why.</b></td>
			</tr>
			<tr>
				<td colspan="3" align="right"><textarea
						id="orgRelDesc<%=nameType%>" name="orgRelDesc<%=nameType%>"
						cols="77" rows="8"><%=typeBean.getOrgRelationDescription()%></textarea></td>
			</tr>
			<tr>
				<td class="copybold" colspan="3"><img width="100%" height="2"
					border="0"
					src="<%=request.getContextPath()%>/coeusliteimages/line4.gif" /></td>
			</tr>
			<%inx++;%>

		</logic:iterate>

		<tr>
			<td class='savebutton' align="left" colspan="2">
				<%/*if(disable==false){*/%> <html:button styleClass="clsavebutton"
					style="width:150px;" value="Save & Continue" property="save"
					onclick="javaScript:saveFinEntityConflict();" /> <%/*}else{*/%> <%--<html:button styleClass="clsavebutton" style="width:150px;" value="Continue" property="save"  onclick="javaScript:saveFinEntityConflict();"/>--%>
				<%/*}*/%> <html:button styleClass="clsavebutton" style="width:150px;"
					value="Discard Changes" property="save"
					onclick="javaScript:exitToCoi();" />
			</td>
		</tr>

	</table>
	<div id="divTXTDetails" class="dialog"
		style="width: auto; overflow: hidden; position: absolute;">
		<table width="100%" border="0" cellpadding="1" cellspacing="1"
			class="table">
			<tr style="background-color: #6E97CF; font-size: 12px; margin: 0;">
				<td style="padding: 2px 0px 5px 0px"><font color="#FFFFFF"
					size="2px"><b>Entity Business Focus</b></font></td>
			</tr>
			<tr style="height: 100px; width: 200px;">
				<%-- <td align="center"> <textarea id="TxtAreaComments" cols="3" style=" height: 70px;resize:none; width: 430px;" disabled ></textarea></td>--%>
				<td align="left" style="height: 100px;">
					<div
						style="height: 100px; width: 650px; overflow-x: scroll; overflow-y: scroll; overflow: auto;">
						<label id="TxtAreaComments"></label>
					</div>
				</td>
			</tr>
			<tr style="background-color: #6E97CF;">
				<td align="center"><input type="button" value="Close"
					class="clsavebutton" onclick="hm('divTXTDetails');" /></td>

			</tr>
		</table>
	</div>
</html:form>

</html>