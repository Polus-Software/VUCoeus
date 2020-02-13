<%@page contentType="text/html"
	import="edu.mit.coeus.utils.CoeusProperties,edu.mit.coeus.utils.DateUtils,
java.util.Calendar,
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
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>COI Financail Entity Details</title>
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
        document.forms[0].action='<%=path%>/addAnnDisclFinEntity.do?addMore=Y';
        if(validateAnnDisclFinEntity(document.forms[0])){        
         document.forms[0].submit();
         }
        }  
        function fnSubmitExit(){
        document.forms[0].action='<%=path%>/addAnnDisclFinEntity.do?exit=Y';
        if(validateAnnDisclFinEntity(document.forms[0])){        
         document.forms[0].submit();
         }
        }  
</script>
<html:javascript formName="annDisclFinEntity" dynamicJavascript="true"
	staticJavascript="true" />
<body>


	<%
//create a vector of comboboxbean instances to show Share owner ship options
boolean readOnly=false;
boolean isSlcted=false;
boolean listActiveOnly=true;
String disabled="";
String mode="review";
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
	<table width="980" border="0" cellpadding="0" cellspacing="0"
		class="table" align="center">
		<tr class="theaderBlue">
			<td align="right" colspan="2">
				<%if(request.getAttribute("header")!=null){
    if(((String)request.getAttribute("header")).equals("disclosure")){%>
				<a href="javascript:history.go(-1)"><u>Back to Review</u></a>&nbsp;<img
				src='<%=path%>/coeusliteimages/seperator.gif' height="15px" /> <% }
    else{
        
    }}
    %> <a href="javascript:self.close()"><u>Close</u></a>
			</td>
		</tr>

		<tr>
			<td>

				<table width="100%" border="0" align="left" cellpadding="0"
					cellspacing="0" class="tabtable">

					<tr height="20px">
						<td height="20px" class='theader' style="font-size: 13px">Financial
							Entity Details</td>
						<%if(mode.equals("review") || mode.equals("edit")){
        listActiveOnly=false;
        %>
						<bean:define id="entityNumber" name="annDisclFinEntity"
							property="entityNumber" />
						<bean:define id="seqNumber" name="annDisclFinEntity"
							property="sequenceNum" />
						<%
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

				<table width="100%" height="20" border="0" cellpadding="0"
					cellspacing="0" class="table">
					<tr>
						<td align="right" class="copybold"></font>Entity Name :</td>
						<%String disclosureStatus= annDisclFinEntity.get("statusCode").toString();%>
						<td align="left">&nbsp; <%if(mode.equals("review")){
                    String entityName = (String)annDisclFinEntity.get("entityName");
                    entityName = entityName == null ? "" : entityName;
                    %> <%=entityName%> <%}else{%> <html:text
								style="width:250" name="annDisclFinEntity" property="entityName"
								tabindex="1" readonly="<%=readOnly%>" />
							<html:hidden name="annDisclFinEntity" property="statusCode"
								value="<%=disclosureStatus%>" /> <%}%>
						</td>
						<td align="right" class="copybold"></font>Public/Privately held :</td>
						<td align="left" styleclass="textbox-long">
							<%if(!mode.equals("review")){%> <%String shareOwnerShip = (String)annDisclFinEntity.get("shareOwnerShip");%>
							<html:select name="annDisclFinEntity" styleClass="textbox-long"
								property="shareOwnerShip" disabled="<%=readOnly%>">
								<html:options collection="optionsShareOwnership" property="code"
									labelProperty="description" />
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
                        %> &nbsp;<%=typeDescription%> <%}%>

						</td>
					</tr>
					<tr>
						<td align="right" class="copybold"></font>Type :</td>
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
						</td>
						<td align="right" class="copybold"><bean:message
								key="coiFinEntity.orgFlag" bundle="coi" />:</td>
						<td>
							<!--Modified for Case# 4447 - Next phase of COI enhancements - Start
                        For Dislpay Mode
                      --> <%if(!mode.equals("review")){%> <html:radio
								name="annDisclFinEntity" property="relatedToOrgFlag" value='Y'
								disabled="<%=readOnly%>" />Yes <html:radio
								name="annDisclFinEntity" property="relatedToOrgFlag" value='N'
								disabled="<%=readOnly%>" />No <%}else{
                        String orgFlag = (String)annDisclFinEntity.get("relatedToOrgFlag");
                        if(orgFlag != null && "Y".equalsIgnoreCase(orgFlag)){%>
							&nbsp;Yes <%}else{%>&nbsp;No<%}
                    }%><!--Case#4447 - End-->
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
				<table width="100%" cellpadding="0" cellspacing="0" class="table"
					border="1" valign="top">
					<tr height='25px' valign="top">
						<td height="20px" class="copybold" style="font-size: 13px"
							valign="top">&nbsp;</td>
						<logic:iterate name="relationTypes" id="relation"
							type="edu.mit.coeuslite.utils.ComboBoxBean">
							<td class="copybold" align="left"><coeusUtils:formatOutput
									name="relation" property="description" /></td>
						</logic:iterate>
						<td class="copybold" align="left">Comments</td>
					</tr>
					<tr>
						<logic:iterate id="dtaGroup" name="finEntdataGroup"
							scope="session"
							type="edu.dartmouth.coeuslite.coi.beans.FInEntDataGroupBean">

							<td height="25px" class="copybold"><coeusUtils:formatOutput
									name="dtaGroup" property="description" /></td>
							<bean:define id="groupId" name="dtaGroup" property="code" />
							<bean:define id="grouplabel" name="dtaGroup"
								property="description" />
							<logic:iterate id="matrix" name="finEntdataMatrix"
								type="edu.dartmouth.coeuslite.coi.beans.FinEntMatrixBean">
								<%--if(dtaGroup.getSortId()>6)
                break;
            --%>
								<%if(matrix.getGroupSortId()>dtaGroup.getSortId()){
        break;   
        }
        %>
								<bean:define id="columnId" name="matrix" property="columnName" />
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
            } else if(entity.getComments()==null || entity.getComments().equals(""))
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

									<input type="hidden" name="column" value="<%=columnId%>" />
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
                        } else{
                            checked="";
                        }
                        
                    }else
                        checked="";
                    //  break;
                }
                
                //}*/%>


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
										<td><input type="checkbox" name="<%=chkBoxId%>" value="1"
											<%=disabled%> <%=checked%>></td>
										<%}%>
										<%} %>
										<!-- Case#4447 - End -->
									</logic:equal>
									<logic:equal name="matrix" property="guiType" value="DROPDOWN">
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
                    } else{
                        isAnswered=false;
                        
                    }
                    
                }
            %>

										<td>
											<!--Modified for Case#4447 - Coi enchanment - Start --> <%if(!mode.equals("review")){%>
											<select style="" name="<%=drpDownId%>" <%=disabled%>>
												<option name="<%=drpDownId%>" id="<%=drpDownId%>" value=""
													<%=selected%>>None
													</otpion>
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
                            } else{
                                selected="";
                            }} else{
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
											<%}%>
										
									</logic:equal>
									<%String comment="cmnt"+columnId;%>
									<%--   <td><TEXTAREA  NAME='<%=comment%>'><%=rowComment%> </TEXTAREA></td>--%>
									<%boolean isExt=false;
        String rowCmnt="";
        if(rowComment.trim()!=null && !rowComment.trim().equals("")){
        if(rowComment.trim().length()>30){
        rowCmnt=rowComment.trim();
        rowComment=rowCmnt.substring(0,30)+"..";
        isExt=true;
        }
        }%>

									<%if(mode.equals("review")){%>
									<td><%=rowComment%> <%}else{%>
									<td><TEXTAREA NAME='<%=comment%>' ROWS="2" COLS="30"
											style="height: 20px" <%=disabled%>
											ONKEYUP="return ismaxlength(this)"><%=rowComment%></TEXTAREA>
										<%}%> <%if(isExt){%> <a id="linkComment"
										href="javascript:showBalloon('linkComment', 'Comment')"
										style="">read Comment</a>
										<div id="Comment"
											style="display: none; overflow: auto; position: absolute; width: 200; height: 250"
											onClick="hideBalloon('Comment')">
											<div
												style="overflow: auto; position: absolute; width: 200; height: 250">
												<table width="100%" height="100%" border="0" cellpadding="2"
													cellspacing="0" class="lineBorderWhiteBackgrnd">
													<tr valign="top">
														<td></td>
														<td align="right"><a
															href="javascript:hideBalloon('Comment')"><img
																border="0" src='<%=path%>/coeusliteimages/none.gif'></a></td>
													</tr>
													<tr>
														<td colspan="2" valign="top"><%=rowCmnt%></td>
													</tr>
												</table>
											</div>
										</div> <%}%></td>

									<%--    </tr>
                <logic:notEqual name="matrix" property="columnLabel" value="<%=grouplabel.toString()%>" >
      </tr>
            </logic:notEqual>--%>
									<%}%>
								</logic:equal>
							</logic:iterate>
					</tr>
					</logic:iterate>
					</tr>
				</table>
			</td>
		</tr>
		<tr class="theaderBlue">
			<td align="right" colspan="2">
				<%if(request.getAttribute("header")!=null){
    if(((String)request.getAttribute("header")).equals("disclosure")){%>
				<a href="javascript:history.go(-1)"><u>Back to Review</u></a>&nbsp;<img
				src='<%=path%>/coeusliteimages/seperator.gif' height="15px" /> <% }
    else{
        
    } }
    %><a href="javascript:self.close()"><u>Close</u></a>
			</td>
		</tr>
	</table>
	<!--     </td></tr>
    </table>-->
</body>
</html:html>
