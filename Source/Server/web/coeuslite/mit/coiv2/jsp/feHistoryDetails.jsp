<%-- 
    Document   : feHistoryDetails
    Created on : Apr 19, 2012, 6:33:09 PM
    Author     : ajay
--%>
<%@ page import="org.apache.struts.validator.DynaValidatorForm"%>

<%@ page contentType="text/html;charset=UTF-8" language="java"
	import="edu.dartmouth.coeuslite.coi.beans.FinEntMatrixBean,
         edu.mit.coeuslite.utils.ComboBoxBean,
         edu.dartmouth.coeuslite.coi.beans.FinEntDetailsBean,
         edu.mit.coeus.utils.CoeusProperties,
         edu.mit.coeus.utils.CoeusPropertyKeys,
         java.util.Vector"%>

<%@page errorPage="ErrorPage.jsp"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/coeus-utils.tld" prefix="coeusUtils"%>
<%@ include file="/coeuslite/mit/utils/CoeusContextPath.jsp"%>
<jsp:useBean id="viewFinEntityData" scope="request"
	class="java.util.Vector" />
<jsp:useBean id="viewFinEntityCertData" scope="request"
	class="java.util.Vector" />
<jsp:useBean id="person" scope="session"
	class="edu.mit.coeus.bean.PersonInfoBean" />
<jsp:useBean id="entityNumber" scope="request" class="java.lang.String" />
<jsp:useBean id="loggedinpersonid" class="java.lang.String"
	scope="session" />
<jsp:useBean id="userprivilege" class="java.lang.String" scope="session" />
<jsp:useBean id="annDisclFinEntity" scope="session"
	class="org.apache.struts.validator.DynaValidatorForm" />
<jsp:useBean id="entityType" scope="session" class="java.util.Vector" />
<%
//create a hash map which holds the parameter names and values
java.util.HashMap htmlLinkValues = new java.util.HashMap();
htmlLinkValues.put("entityNo", entityNumber); ;
pageContext.setAttribute("htmlLinkValues", htmlLinkValues);
%>

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
</style>
<link rel=stylesheet
	href="<bean:write name='ctxtPath'/>/coeuslite/mit/utils/css/ipf.css"
	type="text/css">
<title>Coeus Web</title>
<html:base />
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
<link
	href="<bean:write name='ctxtPath'/>/coeuslite/mit/utils/css/coeus_styles.css"
	rel="stylesheet" type="text/css">
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
<link href="<%=path%>/coeuslite/dartmouth/utils/css/coeus_styles.css"
	rel="stylesheet" type="text/css">
<script src="<%=path%>/coeuslite/dartmouth/utils/scripts/divSlide.js"></script>
<script src="<%=path%>/coeuslite/dartmouth/utils/scripts/Balloon.js"></script>
<script language='javascript'>
        var lastPopup;
           function showPopUp(linkId, frameId, width, height, src, bookmarkId) {
                showBalloon(linkId, frameId, width, height, src, bookmarkId);
            }
         function showQuestion(link)
         {
            var winleft = (screen.width - 650) / 2;
            var winUp = (screen.height - 450) / 2;  
            var win = "scrollbars=1,resizable=1,width=790,height=450,left="+winleft+",top="+winUp
            sList = window.open('<bean:write name='ctxtPath'/>'+"/"+link, "list", win);
            if (parseInt(navigator.appVersion) >= 4) {
            window.sList.focus(); 
            }
         }
         function showDialog(txtHeader,txtName)
        {
     
         var width  =650;// document.getElementById("divTXTDetails").style.pixelWidth;
         var height = 130;//document.getElementById("divTXTDetails").style.pixelHeight;
         sm("divTXTDetails",width,height);
         document.getElementById('lblHeader').innerHTML =txtHeader;
         document.getElementById('TxtAreaDetailName').value=txtName;
         var txtValue=document.getElementById(txtName).value;
         document.getElementById('TxtAreaComments').value=txtValue;
         document.getElementById("mbox").style.left="385";//450
         document.getElementById("mbox").style.top="250";//250
      }
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

    </script>
</head>
<body style="padding-left: 180px">
	<%--  ************  START OF BODY TABLE  ************************--%>

	<table width="980" border="0" cellpadding="0" cellspacing="0"
		class="table">
		<logic:present name="viewFinEntityData" scope="request">
			<logic:iterate id="viewdata" name="viewFinEntityData"
				type="org.apache.commons.beanutils.DynaBean">
				<tr>
					<td height="20" align="left" valign="top" class="theader"><bean:message
							bundle="coi" key="financialEntity.headerFinEntitiesDet" /> - <coeusUtils:formatOutput
							name="viewdata" property="entityName" /></td>
				</tr>

				<tr>
					<td height='25'>
						<table width="100%" cellspacing="0" border="0">
							<tr>
								<td class="copybold" width="50%">&nbsp;&nbsp; <bean:message
										bundle="coi" key="label.personName" /> : <%=person.getFullName()%>
								</td>
								<%
                                String header = request.getParameter("header");
                                if(!(header != null && (header.equalsIgnoreCase("n") || header.equalsIgnoreCase("no")))){
                                %>
								<td class='copybold' align="left">
									<%--<a href="<bean:write name='ctxtPath'/>/getReviewFinEnt.do"><u>Back</u></a>--%>
									<a href="javascript:history.back()"><u>Back</u></a>
								</td>
								<%}%>
								<td class='copybold' align="right">
									<%if(!(header != null && (header.equalsIgnoreCase("n") || header.equalsIgnoreCase("no")))){
                                    %> <logic:notPresent
										name="viewFinEntityDisplay" scope="request">
										<%
                                        if (Integer.parseInt(userprivilege) == 2){                                     
                                        %>
										<html:link action="/editFinEnt.do" paramName="viewdata"
											paramProperty="entityNumber" paramId="entityNumber">
											<u><bean:message bundle="coi"
													key="financialEntity.editLink" /></u>
										</html:link>
										<%
                                        } else{
                                            if(viewdata.get("personId").equals(loggedinpersonid)){
                                        %>
										<html:link action="/editFinEnt.do" paramName="viewdata"
											paramProperty="entityNumber" paramId="entityNumber">
											<u><bean:message bundle="coi"
													key="financialEntity.editLink" /></u>
										</html:link>
										<%
                                            }
                                        }
                                        %>
									</logic:notPresent> <%}else{%> <a href="javascript:window.close()"><big>Close
											&nbsp;</big></a> <%}%>
								</td>
							</tr>
						</table>
					</td>
				</tr>


				<tr class='copy' align="left">
					<font color="red"> <html:messages id="message"
							message="true">
							<bean:write name="message" />
						</html:messages>
					</font>
				</tr>
				<!-- EntityDetails - Start  -->
				<tr>
					<td height="119" align="left" valign="top">
						<table width="99%" border="0" align="center" cellpadding="0"
							cellspacing="0" class="tabtable">
							<tr>
								<td align="left" valign="top">
									<table width="100%" height="20" border="0" cellpadding="0"
										cellspacing="0" class="tableheader">
										<tr>
											<td><bean:message bundle="coi"
													key="financialEntity.subheaderEntityDet" /></td>
										</tr>
									</table>
								</td>
							</tr>
							<tr>

								<td valign="top" class="copy" align="left">
									<table width="100%" border="0" cellpadding="2">
										<tr>
											<td nowrap class="copybold" width="10%" align="left"><bean:message
													bundle="coi" key="label.entityName" /></td>
											<td width="5" class="copybold">:</td>
											<td width="40%" class="copy"><coeusUtils:formatOutput
													name="viewdata" property="entityName" /></td>

											<td nowrap class="copybold" align="left"><bean:message
													bundle="coi" key="label.shareOwnerShip" /></td>
											<td width="5" class="copybold">:</td>
											<td width="40%" class="copy"><logic:equal
													name="viewdata" property="shareOwnerShip" value='P'>
													<bean:message bundle="coi" key="financialEntity.public" />
												</logic:equal> <logic:notEqual name="viewdata" property="shareOwnerShip"
													value='P'>
													<bean:message bundle="coi" key="financialEntity.private" />
												</logic:notEqual> <logic:notPresent name="viewdata" property="shareOwnerShip">
                                                    &nbsp;
                                                </logic:notPresent></td>
										</tr>

										<tr>
											<td nowrap class="copybold" align="left"><bean:message
													bundle="coi" key="label.currentStatus" /></td>
											<td class="copybold">:</td>
											<td class="copy"><coeusUtils:formatOutput
													name="viewdata" property="status" /></td>

											<td nowrap class="copybold" align="left"><bean:message
													bundle="coi" key="label.orgTypeCode" /></td>
											<td class="copybold">:</td>
											<td class="copy"><coeusUtils:formatOutput
													name="viewdata" property="entityType" /></td>
										</tr>

										<tr>
											<td nowrap class="copybold" align="left"><bean:message
													bundle="coi" key="label.statusExplanation" /></td>
											<td class="copybold">:</td>
											<td class="copy"><coeusUtils:formatOutput
													name="viewdata" property="statusDesc" /></td>
										</tr>

										<tr>
											<td nowrap class="copybold" align="left"><bean:message
													bundle="coi" key="label.lastUpdated" /></td>
											<td class="copybold">:</td>
											<td class="copy"><coeusUtils:formatDate name="viewdata"
													formatString="MM-dd-yyyy  hh:mm a" property="updtimestamp" />
												&nbsp; <bean:write name="viewdata" property="upduser" /></td>

										</tr>

									</table>
								</td>
							</tr>

						</table>
					</td>
				</tr>
				<tr>
					<td height='10'>&nbsp;</td>
				</tr>
				<!-- RelationShip - Start -->
				<tr>
					<td height="52" align="left" valign="top"><table width="99%"
							border="0" align="center" cellpadding="0" cellspacing="0"
							class="tabtable">
							<tr>
								<td colspan="4" align="left" valign="top"><table
										width="100%" height="20" border="0" cellpadding="0"
										cellspacing="0" class="tableheader">
										<tr>
											<td><bean:message bundle="coi"
													key="financialEntity.subheaderRelEntity" /></td>
										</tr>
									</table></td>
							</tr>

							<tr>
								<td>
									<table width="100%" border="0" cellpadding="2">
										<tr>
											<td nowrap class="copybold" width="10%" align="left">
												Entity Business Focus</td>
											<td width="6" class="copybold">:</td>
											<td class="copy" colspan="2" style="text-align: justify">
												<coeusUtils:formatOutput name="viewdata"
													property="relationShipDesc" />
											</td>
										</tr>
										<tr>
											<td class="copybold" width="10%" align="left">
												Involvement of Students</td>
											<td width="6" class="copybold">:</td>
											<td class="copy" colspan="2" style="text-align: justify">
												<coeusUtils:formatOutput name="viewdata"
													property="invlmntStudnt" />
											</td>
										</tr>
										<tr>
											<td class="copybold" width="10%" align="left">
												Involvement of Staff or Subordinates</td>
											<td width="6" class="copybold">:</td>
											<td class="copy" colspan="2" style="text-align: justify">
												<%-- JIRA COEUSDEV-544 --%> <coeusUtils:formatOutput
													name="viewdata" property="invlmntStaff" />
											</td>
										</tr>
										<tr>
											<td class="copybold" width="10%" align="left">Use of MIT
												Resources</td>
											<td width="6" class="copybold">:</td>
											<td class="copy" colspan="2" style="text-align: justify">
												<coeusUtils:formatOutput name="viewdata"
													property="resoucreMit" />
											</td>
										</tr>
									</table>

								</td>
							</tr>
							</logic:iterate>
							</logic:present>

						</table></td>
				</tr>
				<!-- RelationShip -End -->
				<%
    boolean readOnly=false;
    String disabled="";
    String mode="";   
    if(request.getAttribute("mode")!=null){
            mode=(String)request.getAttribute("mode");
            if(mode.equals("review")){
                readOnly=true;
                disabled="disabled";
            }

  }    
        %>
				<tr height="20px">
					<td height="20px" class="theader" style="font-size: 13px">
						Relationship Details</td>
				</tr>
				<tr valign="top">
					<td valign="top" bgcolor="#9DBFE9">
						<div style="width: 970px; overflow: auto;">
							<table width="100%" cellpadding="0" cellspacing="0" class="table"
								border="1" valign="top">

								<tr height='25px' valign="top">
									<td height="20px" class="copybold" style="font-size: 13px"
										valign="top">&nbsp;</td>
									<logic:present name="relationTypes">
										<logic:iterate name="relationTypes" id="relation"
											type="edu.mit.coeuslite.utils.ComboBoxBean">
											<td class="copybold" align="left"><coeusUtils:formatOutput
													name="relation" property="description" /></td>
										</logic:iterate>
									</logic:present>
									<%if(mode.equals("review")){%>
									<td align="left"><font class="copybold">Comments </font></td>
									<%}else{%>
									<td align="left"><font class="copybold">Comments </font><font
										class="copy">(not more than 300 characters)</font></td>
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
    String rowComment="";
    String checked="";
    String selected="";
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
            Integer rln=new Integer(0);  
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
        boolean listActiveOnly=true;        
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

												<input type="hidden" name="column" value="<%=columnId%>" />
												<logic:notEqual name="matrix" property="columnLabel"
													value="<%=grouplabel.toString()%>">
													<!--Addded for Case#4447 : Next phase of COI enhancements - Start -->
													<%
              Vector vecRelationship = (Vector)session.getAttribute("finRelType");
              int totalRelationShip =0;
              if(vecRelationship != null && vecRelationship.size() > 0){
               totalRelationShip = vecRelationship.size()+1;
              }
              session.setAttribute("relationTypes",vecRelationship);      
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
												<logic:equal name="matrix" property="guiType"
													value="CHECKBOX">

													<%
        Vector vecRelatnship = (Vector)session.getAttribute("finRelType");
        for(int i=0;i<vecRelatnship.size();i++){
        ComboBoxBean chkBox=(ComboBoxBean)vecRelatnship.get(i);
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
													<td align='left'>&nbsp; <img
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
    boolean isAnswered=false;
    Vector vecRelationship = (Vector)session.getAttribute("finRelType");
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
														<!--Modified for Case#4447 - Coi enchanment - Start --> <%if(!mode.equals("review")){%>
														<select style="width: 100px" name="<%=drpDownId%>"
														<%=disabled%>>
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
												<td><TEXTAREA ID='<%=comment%>' NAME='<%=comment%>'
														ROWS="2" COLS="30" style="height: 20px; resize: none;"
														<%=disabled%> ONKEYUP="return ismaxlength(this)"><%=rowCmnt%></TEXTAREA>
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
						</div>
					</td>
				</tr>
				<tr>
					<td height='10' align="right"><br> <%String header = request.getParameter("header");
            if(header != null && (header.equalsIgnoreCase("n") || header.equalsIgnoreCase("no"))){%>
						<a href="javascript:window.close()"><big>Close &nbsp;</big></a> <%}%><br>
					</td>
				</tr>
	</table>
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

</body>
</html:html>