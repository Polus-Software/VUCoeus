<%--
    Document   : cwCoiFinEntityCoiv2
    Created on : Sep 16, 2010, 2:32:23 PM
    Author     : ROSHIN
--%>
<%@page contentType="text/html"
	import="edu.mit.coeus.utils.CoeusProperties,edu.mit.coeus.utils.DateUtils,
java.util.Calendar,
edu.mit.coeuslite.utils.ComboBoxBean,
edu.mit.coeus.utils.query.Equals,
java.util.Vector,
edu.mit.coeus.utils.CoeusPropertyKeys,
org.apache.struts.validator.DynaValidatorForm,
edu.dartmouth.coeuslite.coi.beans.DisclosureBean,
edu.dartmouth.coeuslite.coi.beans.FinEntMatrixBean,
edu.dartmouth.coeuslite.coi.beans.FinEntDetailsBean,
edu.mit.coeuslite.utils.ComboBoxBean"%>
<%@page pageEncoding="UTF-8" import="edu.mit.coeus.bean.*"%>
<%@page errorPage="ErrorPage.jsp"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/coeus-utils.tld" prefix="coeusUtils"%>
<%@ include file="/coeuslite/mit/utils/CoeusContextPath.jsp"%>
<jsp:useBean id="person" scope="session"
	class="edu.mit.coeus.bean.PersonInfoBean" />
<jsp:useBean id="annDisclFinEntity" scope="session"
	class="org.apache.struts.validator.DynaValidatorForm" />
<jsp:useBean id="entityType" scope="session" class="java.util.Vector" />
<bean:size id="entityTypeSize" name="entityType" scope="session" />
<jsp:useBean id="rltnType" scope="session" class="java.util.Vector" />
<bean:size id="rltnSize" name="rltnType" scope="session" />

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
"http://www.w3.org/TR/html4/loose.dtd">

<html:html>
<head>
<%String path = request.getContextPath();
        String policyPath=CoeusProperties.getProperty(CoeusPropertyKeys.POLICY_FILE);%>
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
<style>
.deleteRow {
	font-weight: bold;
	color: #CC0000;
	background-color: white;
}

.addRow {
	font-weight: bold;
	background-color: white;
}

.rowHeight {
	height: 25px;
}
</style>
<script
	src="<%=request.getContextPath()%>/coeuslite/mit/utils/scripts/modal/modaldbox.js"></script>
<style>
.cltextbox-medium {
	width: 160px;
}

.cltextbox-color {
	width: 160px;
	font-weight: normal;
}

.textbox {
	width: 160px;
	font-weight: normal;
}

.cltextbox-nonEditcolor {
	width: 160px;
	font-weight: normal;
}

html, body {
	height: 100%;
}
</style>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>COI</title>
<html:base />

<link href="<%=path%>/coeuslite/dartmouth/utils/css/coeus_styles.css"
	rel="stylesheet" type="text/css">
<script src="<%=path%>/coeuslite/dartmouth/utils/scripts/divSlide.js"></script>
<script src="<%=path%>/coeuslite/dartmouth/utils/scripts/Balloon.js"></script>
<script>
         var lastPopup;
           function showPopUp(linkId, frameId, width, height, src, bookmarkId) {
                showBalloon(linkId, frameId, width, height, src, bookmarkId);
            }
        function fnSubmit(){
        document.forms[0].action='<%=path%>/addAnnDisclFinEntityCoiv2.do?addMore=Y';
        if(validateAnnDisclFinEntity(document.forms[0])){
        document.forms[0].submit();
         }
        }
        function fnSubmitExit(){
        document.forms[0].action='<%=path%>/addAnnDisclFinEntityCoiv2.do?exit=Y';
        if(validateAnnDisclFinEntity(document.forms[0])){
         document.forms[0].submit();
         }
        }
        function ismaxlength(obj){
        var mlength=300;
        if (obj.getAttribute && obj.value.length>mlength)
        obj.value=obj.value.substring(0,mlength)
        }
    </script>
<html:javascript formName="annDisclFinEntity" dynamicJavascript="true"
	staticJavascript="true" />
<!--    COEUS 3424 STARTS-->
<script
	src="<%=request.getContextPath()%>/coeuslite/mit/utils/scripts/modal/modaldbox.js"></script>
<script type="text/javascript">
    function showDialog(txtHeader,txtName)
 {
     var width  =650;// document.getElementById("divTXTDetails").style.pixelWidth;
      var height = 130;//document.getElementById("divTXTDetails").style.pixelHeight;
     sm("divTXTDetails",width,height);
     document.getElementById('lblHeader').innerHTML =txtHeader;
     document.getElementById('TxtAreaDetailName').value=txtName;
     var txtValue=document.getElementById(txtName).value;
  /* var detailsComments=document.getElementById('TxtAreaComments').value;
     var txtAreaIdName=document.getElementById('TxtAreaDetailName').value;
     document.getElementById(txtAreaIdName).value=detailsComments; */
     document.getElementById('TxtAreaComments').value=txtValue;
     document.getElementById("mbox").style.left="385";//450
     document.getElementById("mbox").style.top="250";//250
//     document.getElementById("mbox").style.position="fixed !important";
//    document.getElementById("mbox").style.position="fixed";     
      }
 //TxtAreaComments
 function clearTxtAreaComments()
 {
     document.getElementById('divTXTDetails').value="";
 }
 function reloadTxtArea()
 {
     var detailsComments=document.getElementById('TxtAreaComments').value;
     var txtAreaIdName=document.getElementById('TxtAreaDetailName').value;
     document.getElementById(txtAreaIdName).value=detailsComments;
     hm('divTXTDetails');
 }
 function fnGotoFinEntListPage()
 {
          document.forms[0].action="<%=request.getContextPath()%>/listAnnFinEntityCoiv2.do";
          document.forms[0].submit();
 }

</script>
<!--    COEUS 3424 ENDS-->
</head>
<body>
	<html:form action='/addAnnDisclFinEntityCoiv2.do' method="POST"
		onsubmit="return validateAnnDisclFinEntity(this)">

		<%
//create a vector of comboboxbean instances to show Share owner ship options
            boolean readOnly=false;
            boolean isSlcted=false;
            boolean listActiveOnly=true;
            String disabled="";
            String mode="add";
            String checked="";
            String selected="";
            String columnValue="";
            Integer rln=new Integer(0);
            String rowComment="";
            boolean isAnswered=false;
            if(request.getAttribute("mode")!=null){
            mode=(String)request.getAttribute("mode");
                        if(mode.equals("review")){
                readOnly=true;
                disabled="disabled";
            }

  }
    String actionFrom="";

    if(request.getAttribute("addFinEntFrom")!=null){
    actionFrom=(String)request.getAttribute("addFinEntFrom");
    }
     String listLink=path+"/listAnnFinEntityCoiv2.do";
     if(actionFrom.equals("coiDiscl")){
       listLink=path+"/disclFinEntity.do";
     }
     if(actionFrom.equals("revDiscl")){
       listLink=path+"/getCompleteDiscl.do?acType=V";
     }
Vector vecShareOwnership = new Vector();
ComboBoxBean orgType = new ComboBoxBean();
orgType = new ComboBoxBean();
orgType.setCode("P");
orgType.setDescription("Public");
vecShareOwnership.addElement(orgType);
orgType = new ComboBoxBean();
orgType.setCode("V");
orgType.setDescription("Private");
vecShareOwnership.addElement(orgType);
pageContext.setAttribute("optionsShareOwnership",vecShareOwnership);

//Vector vecRelationship = new Vector();
ComboBoxBean relationType = new ComboBoxBean();
//Case# 4447 - Next phase of COI enhancements - Start
Vector vecRelationship = (Vector)session.getAttribute("finRelType");

//relationType = new ComboBoxBean();
//relationType.setCode("1");
//relationType.setDescription("Self");
//vecRelationship.addElement(relationType);
//relationType = new ComboBoxBean();
//relationType.setCode("2");
//relationType.setDescription("Spouse");
//vecRelationship.addElement(relationType);
//relationType = new ComboBoxBean();
//relationType.setCode("3");
//relationType.setDescription("Other Family");
//vecRelationship.addElement(relationType);
//relationType = new ComboBoxBean();
//relationType.setCode("4");
//relationType.setDescription("Student/Staff");
//vecRelationship.addElement(relationType);

session.setAttribute("relationTypes",vecRelationship);
//Case#4447 - End
%>
		<%--<form name="" action='<%=path%>/addAnnDisclFinEntity.do' method="post">--%>
		<table width="970px" border="0" cellpadding="0" cellspacing="0"
			class="table" align="center">

			<%if(!mode.equals("review")){%>
			<tr valign="top">
				<td height="10" align="left" valign="top" class="theader"
					style="font-size: 14px"><bean:message bundle="coi"
						key="financialEntity.addFinancialEntity" /></td>
				<td height="10" align="right" valign="top" class="theader"
					style="font-size: 14px"><a
					href='javascript: fnGotoFinEntListPage();'><u>Back</u></a></td>

			</tr>
			<tr>
				<td class="copybold" colspan="2">&nbsp;&nbsp; <font color="red">*</font>
					Indicates mandatory field
				</td>
			</tr>
			<%}%>
			<tr>
				<td align="left" valign="top" colspan="2">

					<table width="99%" border="0" align="left" cellpadding="0"
						cellspacing="0" class="tabtable">
						<tr height="20px">
							<td>
								<table width="100%" border="0" align="left" cellpadding="0"
									cellspacing="0" class="tabtable">
									<tr>
										<td height="20px" class="theader" style="font-size: 13px">Financial
											Entity Details</font>
										</td>
										<%if(mode.equals("review")){%>
										<td height="20px" width="50px" class="theader"
											style="font-size: 30px" align="right"><a
											href="javascript:window.close();"
											style="text-decoration: underline; font-size: medium">Close</a></td>
										<%}%>

										<%if(mode.equals("review") || mode.equals("edit")){
                listActiveOnly=false;
                %>
										<bean:define id="entityNumber" name="annDisclFinEntity"
											property="entityNumber" />
										<bean:define id="seqNumber" name="annDisclFinEntity"
											property="sequenceNum" />
										<%if(mode.equals("review")){%>
										<td align="right" class="theaderBlue">
											<!--Added for Case#4447 : Next phase of COI enhancements - Start-->
											<%
                    String disclosureType = request.getParameter("COI_DISCL_TYPE");
                    String removelink = (String) request.getAttribute("removelink");
                    if(disclosureType == null && removelink ==null){%> <a
											href='<%=path%>/reviewAnnFinEntityCoiv2.do?entityNumber=<%=entityNumber%>&mode=edit&actionFrom=<%=actionFrom%>'>
												Edit/ Modify Financial Entity </a> &nbsp;&nbsp;|&nbsp;&nbsp; <%}
                    %> <% if(removelink ==null){%> <a href='<%=listLink%>'>Back
												to Financial Entity List </a> <%}%>
										</td>
										<%--    <td align="right" class="theaderBlue"><a href="javascript:submitEdit('<%=entityNumber%>','<%=actionFrom%>','edit');">Edit&nbsp;&nbsp;</a></td> --%>
										<%}
                if(mode.equals("edit")){
                readOnly=false;
                disabled="";%>
										<input type="hidden" id="entity" name="entityNumber"
											value="<%=entityNumber%>" />
										<input type="hidden" name="seqNum"
											value="<%=seqNumber.toString()%>" />
										<%   }}%>
									</tr>
								</table>
							</td>
						</tr>

						<tr>
							<td valign="top">
								<table width="100%" height="20" border="0" cellpadding="2"
									cellspacing="0" class="table">
									<%if(mode.equals("review")){%>
									<tr>
										<td align="right" class="copybold">Entity Name :</td>
										<%String entName = (String)annDisclFinEntity.get("entityName");
                entName = entName == null ? "" : entName;
                %>
										<td align="left">&nbsp;<%=entName%></td>
										<td align="right" class="copybold">Public/Privately held
											:</td>
										<%}else{%><!-- Case#4447 - End -->
									<tr>
										<td align="right" class="copybold"><font color="red">*</font>Entity
											Name :</td>
										<%String disclStatusCode=annDisclFinEntity.get("statusCode").toString();%>
										<td align="left"><html:text style="width:250"
												name="annDisclFinEntity" property="entityName"
												readonly="<%=readOnly%>" />
											<html:hidden name="annDisclFinEntity" property="statusCode"
												value="<%=disclStatusCode%>" /></td>

										<td align="right" class="copybold"><font color="red">*</font>Public/Privately
											held :</td>
										<%}%>
										<td align="left" styleclass="textbox-long">
											<!--Modified for Case# 4447 - Next phase of COI enhancements - Start
                        For Dislpay Mode
                        --> <%if(!mode.equals("review")){%> <%String shareOwnerShip = (String)annDisclFinEntity.get("shareOwnerShip");%>
											<html:select name="annDisclFinEntity"
												styleClass="textbox-long" property="shareOwnerShip"
												disabled="<%=readOnly%>">
												<html:options collection="optionsShareOwnership"
													property="code" labelProperty="description" />
											</html:select> <%}else{
                        String typeCode = (String)annDisclFinEntity.get("shareOwnerShip");
                        typeCode = typeCode == null ? "" : typeCode;

                        String typeDescription = "";
                        if(entityType != null && vecShareOwnership.size() > 0 && !typeCode.equals("")){
                            for(int index = 0;index<vecShareOwnership.size();index++){
                                ComboBoxBean comboBoxBean = (ComboBoxBean)vecShareOwnership.get(index);
                                if(comboBoxBean.getCode().equals(typeCode)){
                                    typeDescription = comboBoxBean.getDescription();
                                    break;
                                }
                            }
                        }
                        typeDescription = typeDescription == null ? "" : typeDescription;
                        %> &nbsp;<%=typeDescription%> <%}%> <!--Modified for Case# 4447 - Next phase of COI enhancements - End-->
										</td>
									</tr>
									<!--Modified for Case# 4447 - Next phase of COI enhancements - Start
                 For Dislpay Mode
                 -->
									<%if(mode.equals("review")){%>
									<tr>
										<td align="right" class="copybold">Type :</td>
										<%}else{%>
									
									<tr>
										<td align="right" class="copybold"><font color="red">*</font>Type
											:</td>
										<%}%>
										<td>
											<!--Modified for Case# 4447 - Next phase of COI enhancements - Start-->
											<%if(!mode.equals("review")){%> <html:select
												name="annDisclFinEntity" styleClass="textbox-long"
												property="entityTypeCode" disabled="<%=readOnly%>">
												<html:options collection="entityType" property="code"
													labelProperty="description" />
											</html:select> <%}else{
                        String typeCode = (String)annDisclFinEntity.get("entityTypeCode");
                        typeCode = typeCode == null ? "" : typeCode;
                        String typeDescription = "";
                        if(entityType != null && entityType.size() > 0 && !typeCode.equals("")){
                            for(int index = 0;index<entityType.size();index++){
                                ComboBoxBean comboBoxBean = (ComboBoxBean)entityType.get(index);
                                if(comboBoxBean.getCode().equals(typeCode)){
                                    typeDescription = comboBoxBean.getDescription();
                                    break;
                                }
                            }
                        }
                        typeDescription = typeDescription == null ? "" : typeDescription;
                        %> &nbsp;<%=typeDescription%> <%}%> <!--Case#4447 - End -->
											<%if(mode.equals("review")){%>
										</td>
										<td align="right" class="copybold"><font color="red"></font><b><bean:message
													key="coiFinEntity.orgFlag" bundle="coi" />:</b></td>
										<%}else{%>
										</td>
										<td align="right" class="copybold"><font color="red">*</font><b><bean:message
													key="coiFinEntity.orgFlag" bundle="coi" />:</b></td>
										<%}%>
										<td>
											<!--Modified for Case# 4447 - Next phase of COI enhancements - Start
                        For Dislpay Mode
                      --> <%if(!mode.equals("review")){%> <html:radio
												name="annDisclFinEntity" property="relatedToOrgFlag"
												value='Y' disabled="<%=readOnly%>" />Yes <html:radio
												name="annDisclFinEntity" property="relatedToOrgFlag"
												value='N' disabled="<%=readOnly%>" />No <%}else{
                        String orgFlag = (String)annDisclFinEntity.get("relatedToOrgFlag");
                        if(orgFlag != null && "Y".equalsIgnoreCase(orgFlag)){%>
											&nbsp;Yes <%}else{%>&nbsp;No<%}
                    }%><!--Case#4447 - End-->
										</td>
									</tr>
									<tr>
										<td colspan="4"><div style="height: 15px; width: 460px;"></div></td>
									</tr>
									<tr>
										<td colspan="4">
											<div class="copybold" style="float: left; text-align: left;">
												<font color="red">*</font>
												<bean:message bundle="coi" key="financialEntity.Qstn1" />
												:
											</div>
										</td>
									</tr>
									<tr>
										<td colspan="4">
											<div
												style="height: 80px; width: auto; float: left; padding-left: 87px;">
												<html:textarea property="relationShipDesc" cols="103"
													rows="3" style="resize:none;" readonly="<%=readOnly%>"></html:textarea>
											</div>
										</td>
									</tr>
									<%--<tr>
                        <td colspan="4" >
                            <div style="height: 160px; width:100%;" >
                          <div  class="copybold" style="height: 15px; width: 960px; float:left; text-align: left;"><font color="red">*</font>Describe how your on-going research at MIT is or is not related to your work activities for this entity.&nbsp;&nbsp;If there is no relationship, please state "No relationship" :</div>

                            <div  style="height: 80px; width: auto; float:left;"><html:textarea  property="orgRelnDesc" cols="85" rows="3" style="resize:none;" disabled="<%=readOnly%>" ></html:textarea> </div>
                             </div>
                        </td>
                        </tr>
                        <tr><td  colspan="4" >
                         <div style="height: 80px; width: auto; float:left; padding-left:87px;"><html:textarea  property="orgRelnDesc"  cols="103" rows="3" style="resize:none; " disabled="<%=readOnly%>" ></html:textarea></div>
                        </td></tr>--%>
									<tr>
										<td colspan="4">
											<div class="copybold" style="float: left; text-align: left;">
												<bean:message bundle="coi" key="financialEntity.Qstn2" />
												:
											</div>
										</td>
									</tr>
									<tr>
										<td colspan="4">
											<div
												style="height: 80px; width: auto; float: left; padding-left: 87px;">
												<html:textarea property="invlmntStudnt" cols="103" rows="3"
													style="resize:none;" readonly="<%=readOnly%>"></html:textarea>
											</div>
										</td>
									</tr>
									<tr>
										<td colspan="4">
											<div class="copybold" style="float: left; text-align: left;">
												<bean:message bundle="coi" key="financialEntity.Qstn3" />
												:
											</div>
										</td>
									</tr>
									<tr>
										<td colspan="4">
											<div
												style="height: 80px; width: auto; float: left; padding-left: 87px;">
												<html:textarea property="invlmntStaff" cols="103" rows="3"
													style="resize:none;" readonly="<%=readOnly%>"></html:textarea>
											</div>
										</td>
									</tr>
									<tr>
										<td colspan="4">
											<div class="copybold" style="float: left; text-align: left;">
												<bean:message bundle="coi" key="financialEntity.Qstn4" />
												:
											</div>
										</td>
									</tr>
									<tr>
										<td colspan="4">
											<div
												style="height: 80px; width: auto; float: left; padding-left: 87px;">
												<html:textarea property="resoucreMit" cols="103" rows="3"
													style="resize:none;" readonly="<%=readOnly%>"></html:textarea>
											</div>
										</td>
									</tr>
								</table>
							</td>
						</tr>
						<tr height="20px">
							<td height="20px" class="theader" style="font-size: 13px">
								Relationship Details</td>
						</tr>
						<tr valign="top">
							<td valign="top" bgcolor="#FFFFFF">
								<div style="width: 970px; overflow: auto;">
									<table width="100%" cellpadding="0" cellspacing="0"
										class="table" border="1" valign="top">

										<tr height='25px' valign="top">
											<td height="20px" class="copybold" style="font-size: 13px"
												valign="top"></td>
											<logic:iterate name="relationTypes" id="relation"
												type="edu.mit.coeuslite.utils.ComboBoxBean">
												<td class="copybold" align="left"><coeusUtils:formatOutput
														name="relation" property="description" /></td>
											</logic:iterate>
											<%if(mode.equals("review")){%>
											<td align="left"><font class="copybold">Comments
											</font></td>
											<%}else{%>
											<td align="left"><font class="copybold">Comments
											</font><font class="copy">(not more than 300 characters)</font></td>
											<%}%>

										</tr>

										<logic:iterate id="dtaGroup" name="finEntdataGroup"
											scope="session"
											type="edu.dartmouth.coeuslite.coi.beans.FInEntDataGroupBean">
											<tr height='25'>
												<td class="copybold"><coeusUtils:formatOutput
														name="dtaGroup" property="description" /></td>
												<bean:define id="groupId" name="dtaGroup" property="code" />
												<bean:define id="grouplabel" name="dtaGroup"
													property="description" />
												<logic:iterate id="matrix" name="finEntdataMatrix"
													type="edu.dartmouth.coeuslite.coi.beans.FinEntMatrixBean">
													<%if(matrix.getGroupSortId()>dtaGroup.getSortId()){
         break;
        }
        %>
													<bean:define id="columnId" name="matrix"
														property="columnName" />
													<bean:define id="matrixGpId" name="matrix"
														property="dataGroupId" />
													<%--      <logic:equal name="matrix" property="dataGroupId" value="<%=groupId.toString()%>">
          <input type="hidden" name="column" value="<%=columnId%>"/>
          <logic:notEqual name="matrix" property="columnLabel" value="<%=grouplabel.toString()%>" >
          <td colspan='5'>&nbsp;</td></tr>   <tr><td><coeusUtils:formatOutput name="matrix" property="columnLabel"/></td>
          </logic:notEqual>--%>
													<%
  //  Vector dataEntries=null;
    Vector dataEntries=new Vector();
   // dataEntries=null;
    checked="";
     rowComment="";
    selected="";%>
													<logic:present name="entityDetails" scope="session">
														<logic:iterate id="entity" name="entityDetails"
															type="edu.dartmouth.coeuslite.coi.beans.FinEntDetailsBean">
															<%
           if(entity.getGroupSortId()>dtaGroup.getSortId()){
           // break;
            }
            String entColName=entity.getColumnName();
            int entGpId=0;
            boolean exitIter=false;
            if(columnId.equals(entColName)){
                //rowComment="";
             //    isAnswered=true;
            //    totEntry++;
            rln=(Integer)entity.getRlnType();
            dataEntries.add(entity);
            //  columnValue=entity.getColumnValue();
            if(entity.getComments()!=null && !(entity.getComments().equals("")) ){
            rowComment=entity.getComments();
         //   break;
            }
            else if(entity.getComments()==null || entity.getComments().equals(""))
            rowComment="";
          //  break;
            }else{
              //rowComment="";%>

															<%--       <logic:iterate id="Finmatrix" name="finEntdataMatrix" type="edu.dartmouth.coeuslite.coi.beans.FinEntMatrixBean">
                <logic:equal name="Finmatrix" property="columnName"  value="<%=entColName%>">
                    <bean:define id="finGpId" name="Finmatrix" property="dataGroupId"/>
                    <%entGpId=((Integer)finGpId).intValue();
                    exitIter=true;
                    %>
                </logic:equal>
                <%if(exitIter){
                    break;
                    }
                   %>
            </logic:iterate>
           <% if(entGpId>((Integer)matrixGpId).intValue()){
                        break;
           }--%>
															<%}%>

														</logic:iterate>
													</logic:present>
													<logic:equal name="matrix" property="dataGroupId"
														value="<%=groupId.toString()%>">
														<%boolean listRow=false;
                if(listActiveOnly){
                if(matrix.getStatusFlag()=='A'){
                listRow=true;
                }
                else{
                  listRow=false;
                }
              }else{
                if(matrix.getStatusFlag()=='A'){
                listRow=true;
                }
                else if(matrix.getStatusFlag()=='I'){
                        if(dataEntries.size()>0)
                            listRow=true;
                   }
                 else{
                     listRow=false;
                     }
              }
                if(listRow){%>

														<td style="display: none;"><input type="hidden"
															name="column" value="<%=columnId%>" /></td>
														<logic:notEqual name="matrix" property="columnLabel"
															value="<%=grouplabel.toString()%>">
															<!--Addded for Case#4447 : Next phase of COI enhancements - Start -->
															<%int totalRelationShip =0;
              if(vecRelationship != null && vecRelationship.size() > 0){
               totalRelationShip = vecRelationship.size()+1;

              }
          %>
															<!--Case#4447 - End-->
															<td colspan='<%=totalRelationShip%>'></td>
															<tr>
																<td><coeusUtils:formatOutput name="matrix"
																		property="columnLabel" /> <logic:equal name="matrix"
																		property="statusFlag" value="I">(Currently Inactive)</logic:equal>
																</td>
														</logic:notEqual>
														<%//int dataEntriesSize=dataEntries.size(); %>
														<%--
     <logic:equal name="matrix" property="guiType" value="TEXT">
           <% int index = 0;
           for(int i=0;i<vecRelationship.size();i++){
           ComboBoxBean drpDown=(ComboBoxBean)vecRelationship.get(i);
          String comment=columnId+drpDown.getCode();
              String rowCmnt="";
               index = i;
              FinEntDetailsBean finEnt=new FinEntDetailsBean();
              if(dataEntries!=null && dataEntries.size()>0){
                  if(dataEntries.size() > index){
                   finEnt=(FinEntDetailsBean)dataEntries.get(index);
                    rowCmnt=finEnt.getColumnValue();
                  if(rowCmnt!=null && rowCmnt.length()>30){
                    rowCmnt=rowCmnt.substring(0,30);
                    }
                    }
                  if(rowCmnt == null){
                      rowCmnt = "";
                  }
              }


         if(mode.equals("review")){%>
               <td><%=rowCmnt%>
             <input type="hidden" ID='<%=comment%>' value="<%=rowCmnt%>"/>
             <%}else{%>
         <td><TEXTAREA ID='<%=comment%>' NAME='<%=comment%>' ROWS="2"  COLS="10" style="height:20px; resize:none;" <%=disabled%>  ONKEYUP="return ismaxlength(this)" ><%=rowCmnt%></TEXTAREA>
         <%}}%>
        </td>
    </logic:equal>--%>

														<logic:equal name="matrix" property="guiType"
															value="CHECKBOX">

															<%
        for(int i=0;i<vecRelationship.size();i++){
        ComboBoxBean chkBox=(ComboBoxBean)vecRelationship.get(i);
        String chkBoxId=columnId+chkBox.getCode();
        //  if(isAnswered){

        for(int cnt=0;cnt<dataEntries.size();cnt++){
        FinEntDetailsBean finEnt=(FinEntDetailsBean)dataEntries.get(cnt);

       if(finEnt.getRlnType().intValue()==Integer.parseInt(chkBox.getCode())){
           if(finEnt.getColumnValue()!=null && finEnt.getColumnValue().equals("1")){
                checked="checked";
                break;
                }
                else{
                 checked="";
                }

        }else
        checked="";
        //  break;
        }

        //}*/ %>


															<!--Modified for Case#4447 : Next phase of COI enhancements - Start -->
															<%if(mode.equals("review")){
            if(checked != null && checked.equals("checked")){%>
															<td align='left'><img
																src="<bean:write name='ctxtPath'/>/coeusliteimages/checked.gif">
															</td>
															<%}else{%>
															<td><img
																src="<bean:write name='ctxtPath'/>/coeusliteimages/spacer.gif"
																width="11" height="11"></td>
															<%}%>
															<%}else{%>
															<td><input type="checkbox" name="<%=chkBoxId%>"
																value="1" <%=disabled%> <%=checked%>></td>
															<%}%>
															<%} %>
															<!-- Case#4447 - End -->
														</logic:equal>
														<logic:equal name="matrix" property="guiType"
															value="DROPDOWN">
															<bean:define id="lookupArg" name="matrix"
																property="lookupArgument" />
															<%
    for(int i=0;i<vecRelationship.size();i++){
    ComboBoxBean drpDown=(ComboBoxBean)vecRelationship.get(i);
    String drpDownId=columnId+drpDown.getCode();
    Vector argList=new Vector();
    if(session.getAttribute(lookupArg.toString())!=null){
    argList=(Vector)session.getAttribute(lookupArg.toString());
    }
    ComboBoxBean combo=new ComboBoxBean();
    FinEntDetailsBean finEnt=new FinEntDetailsBean();

    for(int cnt=0;cnt<dataEntries.size();cnt++){
    finEnt=(FinEntDetailsBean)dataEntries.get(cnt);

    if(finEnt.getRlnType().intValue()==Integer.parseInt(drpDown.getCode())){
    isAnswered=true;
    break;
    }
    else{
    isAnswered=false;

    }

    }
    %>

															<td>
																<!--Modified for Case#4447 - Coi enchanment - Start -->
																<%if(!mode.equals("review")){%> <select
																style="width: 100px" name="<%=drpDownId%>" <%=disabled%>>
																	<option name="<%=drpDownId%>" id="<%=drpDownId%>"
																		value="" <%=selected%>>None</option>
																	<%
        if(argList!=null){
        for(int j=0;j<argList.size();j++){
        combo=(ComboBoxBean)argList.get(j);
        if(isAnswered){
        if(finEnt.getColumnValue()!=null){
            if(finEnt.getColumnValue().equals(combo.getCode())){
        selected="selected";
        }else{
        selected="";
        }
        }
        else{
        selected="";
        }}
        else{
          selected="";
        }
        if(argList.size()>0){%>
																	<option name="<%=drpDownId%>" id="<%=drpDownId%>"
																		value="<%=combo.getCode()%>" <%=selected%>><%=combo.getDescription()%></otpion>
																		<%}}
        }%>
																	
															</select> <%}else{
    //Display Mode
    String typeCode = (String)annDisclFinEntity.get("entityTypeCode");
    typeCode = typeCode == null ? "" : typeCode;
    String typeDescription = "";
    if(entityType != null && argList.size() > 0 ){
        for(int index = 0;index<argList.size();index++){
            ComboBoxBean comboBoxBean = (ComboBoxBean)argList.get(index);
            String colValue = finEnt.getColumnValue();
            colValue = colValue == null ? "" : colValue;
            if(isAnswered && colValue.equals(comboBoxBean.getCode())){
                typeDescription = comboBoxBean.getDescription();
                typeDescription = typeDescription == null ? "" : typeDescription;  %>
																&nbsp;<%=typeDescription%> <%}
         }
    }

    %> <%}%> <!--Modified for Case#4447 - Coi enchanment - End -->
															</td>
															<%}%>
														</logic:equal>
														<%String comment="cmnt"+columnId;
      String rowCmnt="";
      if(rowComment.trim()!=null && !rowComment.trim().equals("")){
          rowCmnt=rowComment.trim();
          if(rowCmnt.length()>30){
            rowComment=rowCmnt.substring(0,30);
            }
      }

 if(mode.equals("review")){%>
														<td><%=rowComment%> <input type="hidden"
															ID='<%=comment%>' value="<%=rowCmnt%>" /> <%if(rowCmnt.length() > 30) {%>
															<a
															href="javascript:showDialog('<coeusUtils:formatOutput name="matrix" property="columnLabel"/>','<%=comment%>')">[...]</a>
															<%}%> <%}else{%>
														<td nowrap><TEXTAREA ID='<%=comment%>'
																NAME='<%=comment%>' ROWS="2" COLS="20"
																style="height: 20px; resize: none;" <%=disabled%>
																ONKEYUP="return ismaxlength(this)"><%=rowCmnt%></TEXTAREA>
															<a
															href="javascript:showDialog('<coeusUtils:formatOutput name="matrix" property="columnLabel"/>','<%=comment%>')">[...]</a>
															<%}%></td>

														<logic:equal name="matrix" property="columnLabel"
															value="<%=grouplabel.toString()%>">
											</tr>
											</logic:equal>
											<%}%>
											</logic:equal>

										</logic:iterate>

										</logic:iterate>

									</table>
								</div> <input id="mode" type='hidden' name="mode" value="<%=mode%>" />
								<input id="actionFrom" type="hidden" name="actionFrom"
								value="<%=actionFrom%>" />
							</td>
						</tr>
						<%if(!mode.equals("review")){%>
						<tr>
							<td align="center"><table width="100%" class="theaderBlue">
									<tr>
										<td align="left" width="25%"><a
											href='javascript: fnGotoFinEntListPage();'><u>Back</u></a></td>
										<td align="left" width="50%">
											<%if(actionFrom.equals("coiDiscl")){%> <html:submit
												value="Save & Continue" styleClass="clsavebutton" />
											<html:button property="saveandexit" styleClass="clsavebutton"
												value="Save & Exit" onclick="javascript:fnSubmitExit();" />
											<%}else{%> <html:submit value="Save & Continue"
												styleClass="clsavebutton" /> <%}%> <html:button
												property="CreateAnother" onclick="javaScript:fnSubmit();"
												value="Create Another Entity" styleClass="clsavebutton"
												style="width:160px;" />
										</td>
									</tr>
								</table></td>
						</tr>
						<%}%>



					</table>
				</td>
			</tr>
			<%if(mode.equals("review")){%>
			<tr>
				<td height="20px" class="theader" style="font-size: 30px"
					align="center" colspan="2"><a
					href="javascript:window.close();"
					style="text-decoration: underline; font-size: medium">Close</a></td>
			</tr>
			<%}%>
		</table>
		<!--    COEUS 3424 STARTS-->
		<input type="hidden" id="TxtAreaDetailName" value="" />
		<div id="divTXTDetails" class="dialog"
			style="width: 450px; overflow: hidden;">
			<table width="100%" border="0" cellpadding="0" cellspacing="0"
				align="center" style="background-color: #6E97CF;">
				<tr>
					<td style="padding: 2px 5px 2px 0px"><font color="#FFFFFF"
						size="2px"><b>Relationship Comments - <label
								id="lblHeader" /></b></font></td>
				</tr>
				<tr style="height: 85px;">
					<td align="center"><textarea id="TxtAreaComments" cols="3"
							style="height: 85px; resize: none; width: 620px;" <%=disabled%>></textarea>

					</td>
				</tr>
				<tr>
					<td align="center"><input type="button" value="Ok"
						class="clsavebutton" onclick="reloadTxtArea()" /> <input
						type="button" value="Cancel" Class="clsavebutton"
						onclick="hm('divDisclosureStaus')" /></td>

				</tr>
			</table>
		</div>
		<!--    COEUS 3424 ENDS-->

		<%
if(request.getAttribute("FESubmitSuccess") != null){
        request.removeAttribute("FESubmitSuccess");

        String entityName = (String)request.getAttribute("entityName");
        String actionType = (String)request.getAttribute("actionType");
        String reloadLocation = request.getContextPath()+"/getAnnDisclFinEntityCoiv2.do";
       String key="";
        //messageResources.getMessage("AnnualDisclosure.FinancialEntity.Add");
      if(actionType != null){
            if(actionType.equals("I")){
               key="AnnualDisclosure.FinancialEntity.Add";
            } else if(actionType.equals("U") ){
                key="AnnualDisclosure.FinancialEntity.Edit";
            } else if(actionType.equals("activate") ){
                 key="AnnualDisclosure.FinancialEntity.Active";
            } else if(actionType.equals("deactivate") ){
                 key="AnnualDisclosure.FinancialEntity.Inactive";
            }
        }
     /*   int indexOfApost = message.indexOf("'");
        if(indexOfApost != -1){
            int indexOfApostIncr = indexOfApost+1;
            int endOfMessage = message.length();
            String message1 = message.substring(0, indexOfApost);
            String message2 = message.substring(indexOfApostIncr, message.length());
            message = message1+message2;
        }
        out.print("<script language='javascript'>");
        out.print("alert('"+message+"');");
//out.print("openwintext('FESubmitSuccess.jsp', 'getFinEnt3');");//can't use because of popup blockers
      //  out.print("window.location='"+reloadLocation+"';");
        out.print("</script>");
}*/%>
		<script language='javascript'>
            alert('The Entity "<bean:message bundle="coi" key="AnnualDisclosure.FinancialEntity.Entity" arg0='<%=entityName%>'/>" <bean:message bundle="coi" key='<%=key%>'/>');
            window.location='<%=reloadLocation%>';
        </script>
		<%}

%>

	</html:form>
</body>
</html:html>
