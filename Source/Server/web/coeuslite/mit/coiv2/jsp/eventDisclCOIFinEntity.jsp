<%--
    Document   : eventDisclCOIFinEntity
    Created on : Sep 6, 2011, 12:56:43 PM
    Author     : twinkle
--%>


<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
   "http://www.w3.org/TR/html4/loose.dtd">
<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@page errorPage="ErrorPage.jsp"%>
<%@ taglib uri="/WEB-INF/coeus-utils.tld" prefix="coeusUtils"%>
<%@ include file="/coeuslite/mit/utils/CoeusContextPath.jsp"%>
<%@ page
	import="java.util.Vector,edu.mit.coeuslite.coiv2.beans.CoiDisclosureBean,edu.mit.coeuslite.coiv2.services.CoiCommonService,java.util.Date,
        edu.mit.coeuslite.coiv2.beans.CoiQuestionAnswerBean,edu.dartmouth.coeuslite.coi.beans.FinEntMatrixBean,edu.mit.coeuslite.utils.ComboBoxBean,
        edu.dartmouth.coeuslite.coi.beans.FinEntDetailsBean,edu.mit.coeuslite.coiv2.beans.CoiQuestionAnswerBean,edu.mit.coeuslite.coiv2.beans.Coiv2AttachmentBean;"%>
<jsp:useBean id="person" scope="session"
	class="edu.mit.coeus.bean.PersonInfoBean" />
<jsp:useBean id="annDisclFinEntity" scope="session"
	class="org.apache.struts.validator.DynaValidatorForm" />
<jsp:useBean id="entityType" scope="session" class="java.util.Vector" />
<bean:size id="entityTypeSize" name="entityType" scope="session" />
<jsp:useBean id="rltnType" scope="session" class="java.util.Vector" />
<bean:size id="rltnSize" name="rltnType" scope="session" />

<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>C O I</title>
<%String path = request.getContextPath();%>
<link href="<%=path%>/coeuslite/dartmouth/utils/css/coeus_styles.css"
	rel="stylesheet" type="text/css">
<script src="<%=path%>/coeuslite/dartmouth/utils/scripts/divSlide.js"></script>
<script src="<%=path%>/coeuslite/dartmouth/utils/scripts/Balloon.js"></script>
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
<script src="js/jquery.js" type="text/javascript">
        function getDetails() {
          alert(hhhh);
          document.getElementById("questionnaireDiv").style.display = 'none';
          document.getElementById("finEntDetails").style.display = 'block';
          document.getElementById("attachmentsDiv").style.display = 'block';
          alert("hi");
        }
        function getQuestionDetails()
        {
            alert(hi1);
            debugger;
          document.getElementById("questionnaireDiv").style.display = 'block';
          document.getElementById("finEntDetails").style.display = 'none';
          document.getElementById("attachmentsDiv").style.display = 'block';
        }
                    function validateSaveOrDelete(){
                if(document.forms[0].docType.value==null || document.forms[0].docType.value=='')
                {
                    alert('Please select Document Type');
                    document.forms[0].docType.focus();
                    return false;
                }

                if(document.forms[0].description.value==null ||document.forms[0].description.value.replace(/^\s+/,'').replace(/\s+$/,'')=='')
                {
                    alert('Please enter Description');
                    document.forms[0].description.focus();
                    return false;
                }
                if(document.forms[0].fileName.value==null || document.forms[0].fileName.value=='')
                {
                    alert('Please select a File');
                    document.forms[0].fileName.focus();
                    return false;
                }
                 var extdoc=format();
                if(extdoc=="exe" ||  extdoc=="ini" || extdoc=="bat"){
                    return false
                }else{
                return true;
            }
            }
        </script>
<html:javascript formName="annDisclFinEntity" dynamicJavascript="true"
	staticJavascript="true" />
<body>
	<table id="bodyTable" class="table" border="0" align="center">
		<tr style="background-color: #6E97CF; height: 22px;">

		</tr>

		<tr>

		</tr>
		<table width="100%" border="0" align="left" cellpadding="0"
			cellspacing="0" class="tabtable">

			<tr height="20px">
				<td height="20px" class='theader' style="font-size: 13px"
					colspan="4">Financial Entity:</td>
			</tr>
			<tr>
				<%-- <%  String link = "/removeNotes"; %>
         <td height="20px" class='theader' width="5%"  style="font-size:13px">
             <html:link href="<%=link%>" onclick="javascript:getDetails()"> Details </html:link></td>--%>
				<td height="20px" class='theader' width="7%" style="font-size: 13px"><a
					href="javaScript:getDetails();">Details</a></td>
				<td height="20px" class='theader' width="13%"
					style="font-size: 13px"><a
					href="javaScript:getQuestionDetails();">Questionnaires</a></td>
				<td height="20px" class='theader' width="11%"
					style="font-size: 13px"><a
					href="javaScript:setViewByManagedFinancialEnity();">Attachments
				</a></td>
				<td height="20px" class='theader' width="69%"
					style="font-size: 13px"><a
					href="javaScript:setViewByManagedFinancialEnity();">History </a></td>
				<%--            <td height="20px" class='theader'  width="10%"  style="font-size:13px">
              <html:link href="<%=link%>"> Attachments </html:link></td>--%>


			</tr>


			<%
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
     String listLink=path+"/listAnnFinEntity.do";
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
ComboBoxBean relationType = new ComboBoxBean();
Vector vecRelationship = (Vector)session.getAttribute("finRelType");
session.setAttribute("relationTypes",vecRelationship);
%>


			<%--
<table    border="0" cellpadding="0" cellspacing="0" class="table" align="center"  width="100%">
    <%if(!mode.equals("review")){%>
    <tr valign="top"><td height="10" align="left" valign="top" class="theader" style="font-size:14px"><bean:message bundle="coi" key="financialEntity.addFinancialEntity"/></td></tr>
    <tr><td class="copybold">&nbsp;&nbsp;
            <font color="red">*</font>
            Indicates mandatory field
        </td>
    </tr><%}%>
    <tr>
        <td  align="left" valign="top" colspan="4" >

        <table width="100%"  border="0" align="left" cellpadding="0" cellspacing="0" class="tabtable">
            <tr height="20px"><td colspan="4">
            <table width="100%"  border="0" align="left" cellpadding="0" cellspacing="0" class="tabtable"><tr>
                    <td height="20px" colspan="4" class="theader" style="font-size:13px">Financial Entity Details</td>
                <%if(mode.equals("review") || mode.equals("edit")){
                listActiveOnly=false;
                %>
                <bean:define id="entityNumber" name="annDisclFinEntity" property="entityNumber"/>
                <bean:define id="seqNumber" name="annDisclFinEntity" property="sequenceNum"/>
                <%if(mode.equals("review")){%>
                <%}
                if(mode.equals("edit")){
                readOnly=false;
                disabled="";%>
                <input type="hidden" id="entity" name="entityNumber" value="<%=entityNumber%>"/>
                <input type="hidden" name="seqNum" value="<%=seqNumber.toString()%>"/>
                <%   }}%> 
    </tr></table></td>
    </tr>
    <tr><td valign="top">
            <table  width="100%" height="20"  border="0" cellpadding="0" cellspacing="0" class="table">
                <!--Addded for Case#4447 : Next phase of COI enhancements - Start
                For display mode
                -->
                <%if(mode.equals("review")){%>
                <tr><td align="right" class="copybold">Entity Name : </td>
                <%String entName = (String)annDisclFinEntity.get("entityName");
                entName = entName == null ? "" : entName;
                %>
                <td align="left">&nbsp;<%=entName%></td>
                <td align="right" class="copybold">Public/Privately held : </td>
                <%}else{%><!-- Case#4447 - End -->
                <tr><td align="right" class="copybold"><font color="red">*</font>Entity Name : </td>
                    <%String disclStatusCode=annDisclFinEntity.get("statusCode").toString();%>
                    <td align="left"><html:text name="annDisclFinEntity" property="entityName"   tabindex="1" readonly="<%=readOnly%>"/><html:hidden name="annDisclFinEntity" property="statusCode" value="<%=disclStatusCode%>"/></td>

                    <td align="right" class="copybold"><font color="red">*</font>Public/Privately held : </td>
               <%}%>
                    <td align="left" styleclass="textbox-long">
                         <%if(!mode.equals("review")){%>
                        <%String shareOwnerShip = (String)annDisclFinEntity.get("shareOwnerShip");%>
                        <html:select  name="annDisclFinEntity"  styleClass="textbox-long" property="shareOwnerShip" disabled="<%=readOnly%>">
                            <html:options collection="optionsShareOwnership" property="code" labelProperty="description"/>
                        </html:select>
                        <%}else{
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
                        %>
                         &nbsp;<%=typeDescription%>
                        <%}%>
                      </td></tr>
                <%if(mode.equals("review")){%>
                <tr><td align="right" class="copybold"></font>Type : </td>
                <%}else{%>
                <tr><td align="right" class="copybold"><font color="red">*</font>Type : </td>
                <%}%>
                    <td>
                        <!--Modified for Case# 4447 - Next phase of COI enhancements - Start-->
                        <%if(!mode.equals("review")){%>
                        <html:select  name="annDisclFinEntity"  styleClass="textbox-long" property="entityTypeCode" disabled="<%=readOnly%>">
                            <html:options collection="entityType" property="code" labelProperty="description"/>
                        </html:select>
                        <%}else{
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
                        %>
                         &nbsp;<%=typeDescription%>
                        <%}%>
                       
                     <%if(mode.equals("review")){%>
                     </td><td align="right" class="copybold"><font color="red"></font><b><bean:message key="coiFinEntity.orgFlag" bundle="coi"/>:</b></td>
                     <%}else{%>
                    </td><td align="right" class="copybold"><font color="red">*</font><b><bean:message key="coiFinEntity.orgFlag" bundle="coi"/>:</b></td>
                    <%}%>
                <td> 
                    <%if(!mode.equals("review")){%>
                    <html:radio name="annDisclFinEntity" property="relatedToOrgFlag" value='Y'  disabled="<%=readOnly%>"/>Yes <html:radio name="annDisclFinEntity" property="relatedToOrgFlag" value='N' disabled="<%=readOnly%>"/>No
                    <%}else{
                        String orgFlag = (String)annDisclFinEntity.get("relatedToOrgFlag");
                        if(orgFlag != null && "Y".equalsIgnoreCase(orgFlag)){%>
                         &nbsp;Yes
                        <%}else{%>&nbsp;No<%}
                    }%><!--Case#4447 - End-->
                </td></tr>
            </table>

                
                <div id="finEntDetails">
                    <tr height="20px">
        <td height="20px" class="theader" style="font-size:13px"> Relationship Details</td>
    </tr>
    <tr valign="top"><td valign="top" bgcolor="#FFFFFF" >
        <div style="overflow:auto;">
    <table width="100%" cellpadding="0" cellspacing="0" class="table" border="1" valign="top" >

    <tr height='25px' valign="top">
        <td height="20px" class="copybold" style="font-size:13px" valign="top"> &nbsp;</td>
        <logic:iterate name="relationTypes" id="relation" type="edu.mit.coeuslite.utils.ComboBoxBean">
            <td class="copybold" align="left"><coeusUtils:formatOutput name="relation" property="description"/></td>
        </logic:iterate>
         <%if(mode.equals("review")){%>
        <td align="left"><font class="copybold">Comments </font></td>
        <%}else{%>
        <td align="left"><font class="copybold">Comments </font><font class="copy">(not more than 300 characters)</font></td>
        <%}%>
    </tr>

    <logic:iterate id="dtaGroup" name="finEntdataGroup" scope="session" type="edu.dartmouth.coeuslite.coi.beans.FInEntDataGroupBean">
    <tr height='25'>
        <td class="copybold"><coeusUtils:formatOutput name="dtaGroup" property="description"/></td>
         <bean:define id="groupId" name="dtaGroup" property="code"/>
         <bean:define id="grouplabel" name="dtaGroup" property="description"/>
         <logic:iterate id="matrix" name="finEntdataMatrix" type="edu.dartmouth.coeuslite.coi.beans.FinEntMatrixBean">
           <%if(matrix.getGroupSortId()>dtaGroup.getSortId()){
         break;
        }
        %>
          <bean:define id="columnId" name="matrix" property="columnName"/>
          <bean:define id="matrixGpId" name="matrix" property="dataGroupId"/>
  
    <%
  //  Vector dataEntries=null;
    Vector dataEntries=new Vector();
   // dataEntries=null;
    checked="";
     rowComment="";
    selected="";%>
    <logic:present name="entityDetails"  scope="session">
        <logic:iterate id="entity" name="entityDetails" type="edu.dartmouth.coeuslite.coi.beans.FinEntDetailsBean">
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

   
           <%}%>

        </logic:iterate>
    </logic:present>
    <logic:equal name="matrix" property="dataGroupId" value="<%=groupId.toString()%>">
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

          <input type="hidden" name="column" value="<%=columnId%>"/>
          <logic:notEqual name="matrix" property="columnLabel" value="<%=grouplabel.toString()%>" >
          <!--Addded for Case#4447 : Next phase of COI enhancements - Start -->
          <%int totalRelationShip =0;
              if(vecRelationship != null && vecRelationship.size() > 0){
               totalRelationShip = vecRelationship.size()+1;

              }
          %>
          <!--Case#4447 - End-->
          <td colspan='<%=totalRelationShip%>' ></td>
          <tr><td><coeusUtils:formatOutput name="matrix" property="columnLabel"/>
                  <logic:equal name="matrix" property="statusFlag" value="I">(Currently Inactive)</logic:equal>
            </td>
          </logic:notEqual>
    <%//int dataEntriesSize=dataEntries.size(); %>
    <logic:equal name="matrix" property="guiType" value="CHECKBOX">

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
               <td align='left'>&nbsp; <img src="<bean:write name='ctxtPath'/>/coeusliteimages/checked.gif">  </td>
            <%}else{%>
             <td><img src="<bean:write name='ctxtPath'/>/coeusliteimages/spacer.gif" width="11" height="11"></td>
            <%}%>
        <%}else{%>
        <td><input type="checkbox" name="<%=chkBoxId%>" value="1" <%=disabled%> <%=checked%> ></td>
        <%}%>
        <%} %>    <!-- Case#4447 - End -->
    </logic:equal>
    <logic:equal name="matrix" property="guiType" value="DROPDOWN">
    <bean:define id="lookupArg" name="matrix" property="lookupArgument"/>
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
        <%if(!mode.equals("review")){%>
        <select  name="<%=drpDownId%>" <%=disabled%> >
        <option name="<%=drpDownId%>" id="<%=drpDownId%>" value="" <%=selected%>>None</otpion>
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
        <option name="<%=drpDownId%>" id="<%=drpDownId%>" value="<%=combo.getCode()%>" <%=selected%>><%=combo.getDescription()%></otpion>
        <%}}
        }%>

    </select>
    <%}else{
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
                &nbsp;<%=typeDescription%>
            <%}
         }
    }

    %>
    <%}%>
</td>
    <%}%>
    </logic:equal>
    <%String comment="cmnt"+columnId;%>
  <%boolean isExt=false;
      String rowCmnt="";
      if(rowComment.trim()!=null && !rowComment.trim().equals("")){
          if(rowComment.trim().length()>30){
      rowCmnt=rowComment.trim();
      rowComment=rowCmnt.substring(0,30)+"..";
      isExt=true;
      }
      }
 if(mode.equals("review")){%>
 <td><%=rowComment%>
 <%}else{%>
 <td><TEXTAREA  NAME='<%=comment%>' ROWS="2"  COLS="30" style="height:20px" <%=disabled%>  ONKEYUP="return ismaxlength(this)" ><%=rowComment%></TEXTAREA>
 <%}%>
 <%if(isExt){%>
     <a  id="linkComment" href="javascript:showBalloon('linkComment', 'Comment')" style="copy">read Comment</a>
     <div  id="Comment" style="display:none;overflow:auto;position:absolute;height:250" onClick="hideBalloon('Comment')">
  <div style="overflow:auto;position:absolute;height:250">
                <table  width="100%"  height="100%" border="0" cellpadding="2" cellspacing="0" class="lineBorderWhiteBackgrnd">
                    <tr valign="top"><td></td><td align="right"><a href="javascript:hideBalloon('Comment')"><img border="0" src='<%=path%>/coeusliteimages/none.gif'></a></td></tr>
                    <tr><td colspan="2" valign="top">
                       <%=rowCmnt%>
                        </td>
                    </tr>
                </table>
            </div>
 </div>
    <%}%>
</td>
    <logic:equal name="matrix" property="columnLabel" value="<%=grouplabel.toString()%>" >
    </tr>
</logic:equal>
<%}%>
    </logic:equal>
    </logic:iterate>
    </logic:iterate>
    </table></div>
    </td></tr>
</div>--%>
			<%--   Financial entity details ends--%>

			<%--  Questionnaire details div starts--%>
			<%-- <div id="questionnaireDiv">
        <logic:present name="questionDetView">
            <table id="noteBodyTable" class="table" width="100%" border="0" >
                <tr style="background-color:#6E97CF;height: 22px;">
                    <td style="color:#FFFFFF;font-size:12px;font-weight:bold;">Question Id</td>
                    <td style="color:#FFFFFF;font-size:12px;font-weight:bold;">Questions</td>
                    <td style="color:#FFFFFF;font-size:12px;font-weight:bold;">Answers</td>
                </tr>
                <%
                            String qnBgColor = "#DCE5F1";
                            int indexqn = 0;
                            int qid = 1;
                %>
                <logic:iterate id="qnsAnsView" name="questionDetView">
                    <%
                                if (indexqn % 2 == 0) {
                                    qnBgColor = "#D6DCE5";
                                } else {
                                    qnBgColor = "#DCE5F1";
                                }

               Vector entityPjtList = (Vector) request.getAttribute("questionDetView");
               if (entityPjtList != null && entityPjtList.size() > 0){
               //if answer is N/A,that is stored into the database as X.Code added for replace X with N/A
               for(int i=0;i<entityPjtList.size();i++)
                {
                    CoiQuestionAnswerBean questionDetai=
                   (CoiQuestionAnswerBean)entityPjtList.get(i);
                    String ans=(String)questionDetai.getAnswer();
                    if(ans!=null && ans.equals("X"))
                    {
                          questionDetai.setAnswer("N/A");
                    }
                }
               //Code added for replace X with N/A  ends

            }
                    %>
                    <tr bgcolor="<%=qnBgColor%>" id="row<%=indexqn%>" class="rowLine" onmouseover="className='rowHover rowLine'" onmouseout="className='rowLine'" height="22px">
                        <td><%= qid%></td>
                        <td><bean:write name="qnsAnsView" property="question"/></td>
                        <td><bean:write name="qnsAnsView" property="answerString"/></td>
                    </tr>
                    <%indexqn++;
                                qid++;%>
                </logic:iterate>

            </table>
        </logic:present>
    </div>--%>
			<%--  Questionnaire details div ends--%>


			<%--    Attachments details div starts
--%>
			<div id="attachmentsDiv"
				style="width: 100%; overflow-x: hidden; overflow-y: scroll; overflow: auto;">


				<table id="attBodyTable" class="table" style="width: 100%;"
					border="0" cellpadding="0" cellspacing="0">
					<tr>
						<td height="20" width="50%" align="left" valign="top"
							class="theader" style="padding: 2px 0 2px 10px;">Disclosure
							Attachment</td>
					</tr>
				</table>

				<table width="100%" border="0" class="tabtable" cellpadding="1"
					cellspacing="5">
					<tr>
						<td width="12%" align="left" class="copybold" nowrap><bean:message
								key="uploadDocLabel.DocumentType" />:</td>
						<td width="30%" align="left" nowrap><logic:notEmpty
								name="DocTypes">
                            &nbsp;<html:select property="docType"
									name="coiv2Attachment" styleClass="textbox-long">
									<html:option value="">
										<bean:message key="specialReviewLabel.pleaseSelect" />
									</html:option>
									<html:options collection="DocTypes" property="code"
										labelProperty="description" />
								</html:select>
							</logic:notEmpty> <logic:empty name="DocTypes">
                            &nbsp;<html:select property="docType"
									name="coiv2Attachment" styleClass="textbox-long">
									<html:option value="">
										<bean:message key="specialReviewLabel.pleaseSelect" />
									</html:option>
								</html:select>
							</logic:empty></td>
						<td width="15%" align=right nowrap class="copybold"><bean:message
								key="uploadDocLabel.Description" />:</td>
						<td width='43%' align=left>&nbsp;<html:text
								property="description" name="coiv2Attachment"
								styleClass="textbox-long"></html:text></td>
					</tr>
					<tr>

						<td align="left" nowrap class="copybold"><bean:message
								key="uploadDocLabel.FileName" />:</td>

						<td align="left" class='copy' colspan='3'>&nbsp;<html:file
								property="document" name="coiv2Attachment"
								onchange="selectFile()" maxlength="300" size="50" />
						</td>
					</tr>
					<tr>
						<td>&nbsp;</td>
						<td colspan='3'><html:text property="fileName"
								style="width: 450px;" name="coiv2Attachment"
								styleClass="cltextbox-color" disabled="true" readonly="true" />
						</td>
					</tr>
					<tr class='table'>
						<td class='savebutton' colspan="4"><html:button
								property="Save" onclick="javaScript:saveOrDelete();"
								value="Save" styleClass="clsavebutton" /> &nbsp;&nbsp;&nbsp;</td>
					</tr>
				</table>


				<html:hidden name="coiv2Attachment" property="acType" />
				<html:hidden name="coiv2Attachment" property="disclosureNumber" />
				<html:hidden name="coiv2Attachment" property="sequenceNumber" />
				<html:hidden name="coiv2Attachment" property="entityNumber" />
				<html:hidden name="coiv2Attachment" property="fileBytes" />
				<html:hidden name="coiv2Attachment" property="fileNameHidden" />


				<table id="attBodyTable" class="table" style="width: 100%;"
					border="0">
					<tr style="background-color: #6E97CF; height: 22px; width: 100%;">
						<td
							style="color: #FFFFFF; font-size: 12px; font-weight: bold; padding: 2px 0 2px 10px;"
							colspan="7">List of Attachments</td>
					</tr>
					<tr style="background-color: #6E97CF; height: 22px; width: 100%;">
						<td
							style="font-size: 12px; font-weight: bold; width: 20%; padding-left: 5px;">Attachment
							Type</td>
						<td
							style="font-size: 12px; font-weight: bold;; width: 40%; padding-left: 5px;">Description</td>
						<td
							style="font-size: 12px; font-weight: bold;; width: 8%; padding-left: 5px;"
							align="center">Uploaded By</td>
						<td
							style="font-size: 12px; font-weight: bold;; width: 12%; padding-left: 5px;">File
							Name</td>
						<td
							style="font-size: 12px; font-weight: bold;; width: 8%; padding-left: 5px;">Posted
							Timestamp</td>

						<td
							style="font-size: 12px; font-weight: bold;; width: 5%; padding-left: 5px;"></td>
						<logic:present name="userHasRight">
							<logic:equal name="userHasRight" value="true">
								<td
									style="font-size: 12px; font-weight: bold;; width: 7%; padding-left: 5px;"></td>
							</logic:equal>
						</logic:present>
					</tr>
					<logic:present name="message">
						<logic:equal value="false" name="message">
							<tr>
								<td colspan="7"><font color="red">No attachments
										found</font></td>
							</tr>
						</logic:equal>
					</logic:present>
					<logic:present name="attachmentList">
						<%
                                String strBgColoratt = "#D6DCE5";
                                int indexatt = 0;
                                Vector attachmentList = (Vector) request.getAttribute("attachmentList");
                    %>
						<logic:iterate id="coiv2Attachment" name="attachmentList">
							<%
                                    if (indexatt % 2 == 0) {
                                        strBgColoratt = "#D6DCE5";
                                    } else {
                                        strBgColoratt = "#DCE5F1";
                                    }
                                    Coiv2AttachmentBean attachmentBean = (Coiv2AttachmentBean) attachmentList.get(indexatt);
                        %>
							<tr bgcolor="<%=strBgColoratt%>" class="rowLine"
								onmouseover="className='rowHover rowLine'"
								onmouseout="className='rowLine'" style="width: 765px;">
								<td style="padding-left: 5px;"><bean:write
										name="coiv2Attachment" property="docType" /></td>
								<td style="padding-left: 5px;" align="justify"><bean:write
										name="coiv2Attachment" property="description" /></td>
								<td style="padding-left: 5px;" align="center"><bean:write
										name="coiv2Attachment" property="updateUser" /></td>
								<td style="padding-left: 5px;"><bean:write
										name="coiv2Attachment" property="fileName" /></td>
								<td style="padding-left: 5px;"><bean:write
										name="coiv2Attachment" property="updateTimeStamp" /></td>

								<td class="copy" align='left' style="padding-left: 5px;">
									<%String link2 = "javaScript:viewAttachment('" + attachmentBean.getEntityNumber() + "','" + attachmentBean.getDisclosureNumber() + "','" + attachmentBean.getSequenceNumber() + "','" + attachmentBean.getUpdateUser() + "')";%>
									<html:link href="<%=link2%>"> View </html:link>
								</td>
								<logic:present name="userHasRight">
									<logic:equal name="userHasRight" value="true">
										<td class="copy" align='left' style="padding-left: 5px;">
											<%String link3 = "javaScript:removeAttachment('" + attachmentBean.getEntityNumber() + "','" + attachmentBean.getDisclosureNumber() + "','" + attachmentBean.getSequenceNumber() + "','" + attachmentBean.getUpdateUser() + "')";%>
											<html:link href="<%=link3%>"> Remove </html:link>
										</td>
									</logic:equal>
								</logic:present>
							</tr>
							<%indexatt++;%>
						</logic:iterate>
					</logic:present>
				</table>

			</div>

			<%--    Attachments details div ends--%>


		</table>

	</table>
</body>