<%@page contentType="text/html"
	import="edu.mit.coeus.utils.CoeusProperties,edu.mit.coeus.utils.CoeusPropertyKeys,java.util.ArrayList,java.util.List,
org.apache.struts.validator.DynaValidatorForm,
java.util.Vector,
java.lang.Integer,
java.util.HashMap,
edu.mit.coeus.utils.DateUtils,
java.util.Calendar,
edu.dartmouth.coeuslite.coi.beans.DisclosureBean,
org.apache.struts.action.DynaActionForm,
java.util.List,edu.mit.coeuslite.utils.CoeusDynaBeansList"%>
<%@page pageEncoding="UTF-8" import="edu.mit.coeus.bean.*"%>
<%@page errorPage="ErrorPage.jsp"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/coeus-utils.tld" prefix="coeusUtils"%>
<%@ include file="/coeuslite/mit/utils/CoeusContextPath.jsp"%>
<jsp:useBean id="person" scope="session"
	class="edu.mit.coeus.bean.PersonInfoBean" />
<jsp:useBean id="questionsList" scope="session"
	class="edu.mit.coeuslite.utils.CoeusDynaBeansList" />
<jsp:useBean id="disclFinEntDet" scope="session"
	class="java.util.Vector" />
<bean:size id="totentity" name="disclFinEntDet" />
<jsp:useBean id="disclProposals" scope="session"
	class="java.util.Vector" />
<bean:size id="totProposals" name="disclProposals" />
<jsp:useBean id="dynaForm" scope="request"
	class="org.apache.struts.validator.DynaValidatorForm" />
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
"http://www.w3.org/TR/html4/loose.dtd">

<html>
<head>
<%String path = request.getContextPath();
        String policyPath=CoeusProperties.getProperty(CoeusPropertyKeys.POLICY_FILE);
        String statusStr="";%>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>COI</title>
<link href="<%=path%>/coeuslite/dartmouth/utils/css/coeus_styles.css"
	rel="stylesheet" type="text/css">
<script src="<%=path%>/coeuslite/dartmouth/utils/scripts/divSlide.js"></script>
<script src="<%=path%>/coeuslite/dartmouth/utils/scripts/Balloon.js"></script>
<script>            
        var plus = new Image();
        var minus = new Image();
        
        function preLoadImages() {
            plus.src = "<%=path%>/coeusliteimages/plus.gif";
            minus.src = "<%=path%>/coeusliteimages/minus.gif";
        }               
        preLoadImages();   
	function selectDiv(rowId,historyRowId,divId,toggleId){
		 var style=document.getElementById(rowId).style.display;
	  if(style==""){
		  document.getElementById(rowId).style.display='none';
		  document.getElementById(divId).style.display='none';		 
		  toggleText=plus.src;
		  }
		  else{
		  document.getElementById(rowId).style.display="";
		  document.getElementById(divId).style.display="";
		  toggleText=minus.src;		 
		}
		  toggleImage(toggleId,toggleText);		 
		  }
        function toggleImage(toggleId,teggleText){
		  var ImgId="img"+toggleId;
                  document.getElementById(ImgId).src=toggleText;			  
		  }		
        <%--Added Case#4447 : FS_Next phase of COI enhancements - Start--%>
        var defaultStyle = "rowLine";
        var selectStyle = "rowHover";
        
        function selectRow(rowId, dataRowId, divId, linkId, projectFrame, url) {
            var style = document.getElementById(dataRowId).style.display;
            var toggleText;
            if(style == ""){
                 style = "none";
                 styleClass = defaultStyle;
                 toggleText = plus.src;
                ds = new DivSlider();
                if(style == "") {
                    document.getElementById(dataRowId).style.display = "";
                    ds.showDiv(divId);
                }else {
                     ds.hideDiv(divId, "document.getElementById('"+dataRowId+"').style.display='none'");
                }
                var imgId = "img"+linkId;
                document.getElementById(imgId).src=toggleText;
            }else {
                document.getElementById(dataRowId).style.display = "";
                var divRef = document.getElementById(divId);
        	if(!divRef.hasChildNodes()) {
                    addFrame(divId, projectFrame, url);
                }
                
                projectFrame = document.getElementById(projectFrame);
                var frameSrc = projectFrame.getAttribute("src");
                if(frameSrc == null || frameSrc == "") {
                    projectFrame.setAttribute("src", url);
                }
                ds2 = new DivSlider();
                ds2.showDiv(divId);
                var imgId = "img"+linkId;
                toggleText = minus.src;
                document.getElementById(imgId).src=minus.src;
            }

        }
        
        
        var plus = new Image();
        var minus = new Image();
        
        function preLoadImages() {
            plus.src = "<%=path%>/coeusliteimages/plus.gif";
            minus.src = "<%=path%>/coeusliteimages/minus.gif";
        }
        
         function addFrame(divId, projectFrame, url) {
            iframeElem = document.createElement("IFRAME");
            document.createAttribute("src");
            iframeElem.setAttribute("src", url);
            iframeElem.setAttribute("id", projectFrame);
            iframeElem.setAttribute("width", "100%");
            iframeElem.setAttribute("scrolling", "auto");
            iframeElem.setAttribute("marginHeight", "0");
            iframeElem.setAttribute("marginWidth", "0");
            iframeElem.setAttribute("frameBorder", "0");
            var divRef = document.getElementById(divId);
            divRef.appendChild(iframeElem);
        }
        
        preLoadImages();   
       <%--Case 4447 - end--%>     
        </script>
</head>
<body>
	<table align="center" width='984' border="0" cellpadding="0"
		cellspacing="0" class="table">
		<%-- Commneted for Case#4447 -Next phase of COI enhancements  
            <tr>
                <td class='theaderBlue'colspan="2" align='left' style="font-size=16px"><bean:message bundle="coi" key="AnnualDisclosure.Review.Details"/> <%=person.getFullName()%></td>
            </tr>
            Case#4447 - End--%>
		<tr>
			<td colspan="2" height="2px"></td>
		</tr>
		<tr>
			<td class="theader" width="10px" height="20px"><a
				href="javascript:selectDiv('row0','historyRow0','historyData0','toggle0');"
				id="toggle0"> <img src='<%=path%>/coeusliteimages/minus.gif'
					id="imgtoggle0" name="imgtoggle0" border="none"></a></td>
			<td height="20" width="100%" class="theader" style="font-size: 13px"><bean:message
					bundle="coi" key="AnnualDisclosure.Review.Header" /></td>
		</tr>

		<tr id="row0" class="copy rowHover">
			<td colspan="2" class="table" width="100%">
				<!--NOTE: DO NOT PUT THE DIV END TAG IN NEXT LINE NOT PUT EVEN A SINGLE SPACE
     MOZILLA RETURNS TRUE FOR HASCHILDNODES EVEN IF A SPACE IS PRESENT B/N START
     AND END TAG :(-->
				<div id="historyData0" style="overflow: hidden">
					<logic:present name="DisclPer" scope="session">
						<%DisclosureBean discl=(DisclosureBean)session.getAttribute("DisclPer");
                
                %>
						<logic:present name="DisclStatus" scope="session">
							<logic:iterate id="status" name="DisclStatus"
								type="edu.mit.coeuslite.utils.ComboBoxBean">
								<bean:define id="statuscode" name="status" property="code" />
								<logic:equal name='statuscode'
									value='<%=discl.getDisclosureStatusCode().toString()%>'>
									<bean:define id="statusDesc" name="status"
										property="description" />
									<%statusStr=(String)statusDesc;%>
								</logic:equal>
							</logic:iterate>
						</logic:present>
						<%--  <logic:notEqual name="disclSize" value='0'>--%>
						<table align="left" width='100%' border="0" cellpadding="0"
							cellspacing="0" class="table">
							<tr class="theader" height="20px">
								<td width="15px">&nbsp;</td>
								<td width="181" align="left" class="theader">DisclosureNumber</td>
								<td width="181" height="20" align="left" class="theader">Status</td>
								<td align="left" class="theader" width="183">Last Updated</td>

								<td align="left" class="theader" width="614">Expiration
									Date</td>
							</tr>
							<%--  <logic:iterate id="discl" name="FinDiscl" type="edu.dartmouth.coeuslite.coi.beans.DisclosureBean" >--%>
							<bean:define id="dtUpdate" name="DisclPer"
								property="updateTimestamp" type="java.sql.Date" />
							<%long time=dtUpdate.getTime();
                    Calendar cal=Calendar.getInstance();
                    cal.setTimeInMillis(time);
                    DateUtils dtUtils=new DateUtils();
                    String dtStr=dtUtils.formatCalendar(cal,"MM/dd/yyyy hh:mm:ss a");%>
							<tr bgcolor="#D6DCE5" bordercolor="#79A5F4" height="20px"
								id="row0">
								<td>&nbsp;</td>
								<td width='181px' align="left"><%=discl.getCoiDisclosureNumber()%></td>
								<td width="181" height="21" align="left"><%=statusStr%></td>
								<td align="left" width="183"><%=dtStr%></td>
								<td align="left" width="614"><coeusUtils:formatDate
										name="DisclPer" property="expirationDate" /></td>
							</tr>
							<tr id="historyRow0" style="display: none" class="copy rowHover">
								<td colspan="5">
									<!--NOTE: DO NOT PUT THE DIV END TAG IN NEXT LINE NOT PUT EVEN A SINGLE SPACE
                                                                    MOZILLA RETURNS TRUE FOR HASCHILDNODES EVEN IF A SPACE IS PRESENT B/N START
                                                                    AND END TAG :(
                                   -->
									<div id="historyData0" style="overflow: hidden;"></div>
								</td>
							</tr>
							<%--      </logic:iterate>                     --%>
						</table>


					</logic:present>
					<logic:notPresent name="DisclPer" scope="session">
						<table>
							<tr>
								<td height="28" class="copybold" colspan="3">You have not
									created an Annual disclosure. <a
									href='<%=path%>/getCompleteDiscl.do'>Complete Annual
										Disclosure </a>
								</td>
							</tr>
						</table>
					</logic:notPresent>

				</div>
			</td>
		</tr>
		<tr>
			<td class="theader" width="10px" height="20px"><a
				href="javascript:selectDiv('row1','historyRow1','historyData1','toggle1')"
				id="toggle1"> <img src='<%=path%>/coeusliteimages/minus.gif'
					id="imgtoggle1" name="imgtoggle1" border="none"></a></td>
			<td height="20" width="986px" class="theader" style="font-size: 13px"><bean:message
					bundle="coi" key="AnnualDisclosure.Certification.Header" /></td>

		</tr>
		<%
      int questSize=((Vector)questionsList.getList()).size();
      
     // int maxht=800;
   int ht=20+questSize*13;
   /*   if(ht>maxht){
     ht=maxht;
     }*/
      %>
		<tr id="row1" style="" height='<%=ht%>px'>
			<td colspan="2" valign="top" width="100%">
				<!--NOTE: DO NOT PUT THE DIV END TAG IN NEXT LINE NOT PUT EVEN A SINGLE SPACE
     MOZILLA RETURNS TRUE FOR HASCHILDNODES EVEN IF A SPACE IS PRESENT B/N START
     AND END TAG :(-->

				<div id="historyData1"
					style="overflow:auto; position: absolute;height:<%=ht%>px">
					<table align="left" width='986' border="0" cellpadding="4"
						cellspacing="0" class="tabtable">
						<%  
              String strBgColor = "#DCE5F1";
             String calImage = request.getContextPath()+"/coeusliteimages/cal.gif";
             HashMap hmQuestionNumber = new HashMap();
             int questionNum = 1; 
             int count=0;
       //      String mode=(String)session.getAttribute("mode"+session.getId()); 
    boolean modeValue=false;
    boolean originalMode = false;
 /*   if(mode!=null && !mode.equals("")){   
         if(mode.equalsIgnoreCase("display") || mode.equalsIgnoreCase("D")){*/
                modeValue=true;
                originalMode = true;
      /*   }
    }*/
             %><logic:iterate id="dynaFormData" name="questionsList"
							property="list" type="org.apache.struts.action.DynaActionForm"
							indexId="index" scope="session">
							<%
                        if (count%4 == 0)
                            strBgColor = "#D6DCE5";
                        else 
                            strBgColor="#DCE5F1";
                        String questionId 		= ((Integer)dynaFormData.get("questionId")).toString();
                        String questionNumber 		= ((Integer)dynaFormData.get("questionNumber")).toString();
                        String question 		= (String)dynaFormData.get("description");
                        String maxAnswers   		= ((Integer)dynaFormData.get("maxAnswers")).toString();
                        String answer   		= (String)dynaFormData.get("answer");
                        String validAnswers   		= (String)dynaFormData.get("validAnswer");
                        String lookUpName   		= (String)dynaFormData.get("lookUpName");
                        String lookupGUI                = (String)dynaFormData.get("lookUpGui");  
                        String answerDataType           = (String)dynaFormData.get("answerDataType");
                        String answerMaxLength          = ((Integer)dynaFormData.get("answerMaxLength")).toString();
                        String answerNumber             = ((Integer)dynaFormData.get("answerNumber")).toString();
                        String searchName               = (String)dynaFormData.get("searchName");
                        boolean isLookupPresent 	= false;
                        boolean isLookupDisabled	= false;
                        if(lookupGUI!=null && !lookupGUI.equals("")){
                                isLookupPresent 	= true;
                        }
                        answer = (answer == null)? "": answer;
                 %>
							<%if(Integer.parseInt(answerNumber) == 0){%>
							<!--<tr height="5px"><td >&nbsp;</td><td>&nbsp;</td><td>&nbsp;</td><td>&nbsp;</td></tr>-->
							<tr bgcolor="<%=strBgColor%>" class="rowLine"
								onmouseover="className='rowHover rowLine'"
								onmouseout="className='rowLine'">
								<td height="20" valign="top" width="8px">
									<%String key = ""+questionId+questionNumber;
                        if(hmQuestionNumber.get(key) == null){
                            hmQuestionNumber.put(key, ""+questionNum);
                            questionNum++;
                        }%> <%=hmQuestionNumber.get(key)%>.&nbsp;
								</td>
								<td width="950px"><bean:write name="dynaFormData"
										property="description" /></td>
								<%} else {%>
								<td width="25%" class="copy" nowrap valign="top">
									<% if(validAnswers!=null && !validAnswers.equals("")){                                 
                                   if(validAnswers.equalsIgnoreCase("Text")){%>

									<%--if(Integer.parseInt(answerMaxLength) <= 20){ %>
                                            <html:text property="answer" name="dynaFormData" maxlength="<%=answerMaxLength%>" indexed="true" disabled="<%=modeValue%>" onchange="dataChanged()"/>
                                        <%}else if(Integer.parseInt(answerMaxLength) > 20 && Integer.parseInt(answerMaxLength) <= 80){%>
                                            <html:text property="answer" name="dynaFormData" styleClass="textbox-long" maxlength="<%=answerMaxLength%>" indexed="true" disabled="<%=modeValue%>" onchange="dataChanged()"/>
                                        <%}else{%>
                                            <html:textarea property="answer" name="dynaFormData" styleClass="textbox-longer" cols="100" rows="5" indexed="true" disabled="<%=modeValue%>" onchange="dataChanged()"/>
                                        <%}--%> <%if(answer.length() <= 2000){%>
									<%if(answer.length() <= 60){%> <html:text property="answer"
										name="dynaFormData" indexed="true" disabled="<%=modeValue%>"
										onchange="dataChanged()" /> <%}else if(answer.length() >60 && answer.length() <= 120){%>
									<html:text property="answer" name="dynaFormData"
										styleClass="textbox-long" indexed="true"
										disabled="<%=modeValue%>" onchange="dataChanged()" /> <%}else{%>
									<html:textarea property="answer" name="dynaFormData"
										styleClass="textbox-longer" cols="150" rows="5" indexed="true"
										disabled="<%=modeValue%>" onchange="dataChanged()" /> <%}%> <%}%>

									<% if(!modeValue){
                                        if(answerDataType.equals("Date")){ 
                                            String calender ="javascript:displayCalendarWithTopLeft('dynaFormData["+count+"].answer',8,25)";%>

									<html:link href="<%=calender%>" onclick="dataChanged()">
										<html:img src="<%=calImage%>" border="0" height="16"
											width="16" />
									</html:link> <%}}%> <%}else if(validAnswers.equalsIgnoreCase("YN")){
                                    %> <html:radio property="answer"
										name="dynaFormData" value="Y" indexed="true"
										disabled="<%=modeValue%>" onchange="dataChanged()" />Yes
									&nbsp; <html:radio property="answer" name="dynaFormData"
										value="N" indexed="true" disabled="<%=modeValue%>"
										onchange="dataChanged()" />No <%}else if(validAnswers.equalsIgnoreCase("YNX")){%>
									<html:radio property="answer" name="dynaFormData" value="Y"
										indexed="true" disabled="<%=modeValue%>"
										onchange="dataChanged()" />Yes &nbsp; <html:radio
										property="answer" name="dynaFormData" value="N" indexed="true"
										disabled="<%=modeValue%>" onchange="dataChanged()" />No <html:radio
										property="answer" name="dynaFormData" value="X" indexed="true"
										disabled="<%=modeValue%>" onchange="dataChanged()" />N/A <%}else if(isLookupPresent){%>
									<html:text property="answer" name="dynaFormData"
										maxlength="2000" indexed="true" readonly="true"
										disabled="<%=modeValue%>" onchange="dataChanged()" /> <%  String pageUrl="javaScript:openLookupWindow('"+lookupGUI+"','"+lookUpName+"','','"+count+"')";
                                        if(!modeValue){
                                        String image= request.getContextPath()+"/coeusliteimages/search.gif";%>
									<html:link href="<%=pageUrl%>">
										<u><bean:message bundle="proposal"
												key="proposalOrganization.Search" /></u>
									</html:link> <%}}%> <%--<html:text property="answer" name="dynaFormData" maxlength="10" indexed="true" readonly ="true" disabled="<%=modeValue%>" />
                                        <a id="hlIRBDate" onclick="displayCalendarWithTopLeft('dynaFormData[<%=count%>].answer',8,25)" tabindex="32" href="javascript:void(0);"
                                         runat="server"><img id="imgIRBDate" title="" height="16" alt="" src="<bean:write name='ctxtPath'/>/coeusliteimages/cal.gif" width="16"
                                         border="0" runat="server">
                                        </a>--%> <%}
                                  if(!modeValue){
                                  if(isLookupPresent){
                                   String image= request.getContextPath()+"/coeusliteimages/search.gif";
                                   //String pageUrl="javaScript:openLookupWindow('"+lookupGUI+"','"+lookUpName+"','"+validAnswers+"','"+count+"')";
                                   %> <%-- <html:link  href=""><html:img src="<%=image%>"/> </html:link>--%>
									<%}}%>
								</td>
								<td width="25%" class="copybold" nowrap valign="center">&nbsp;
									<html:hidden property="searchName" name="dynaFormData"
										indexed="true" /> <span style="font-weight: bold;"
									id="searchName<%=count%>"> <%if(searchName != null && searchName.length()>0){%>
										<%=searchName%> <%}%>
								</span>
								</td>
								<%--  <td align="left" width="20px" valign="top">
                                <html:text property="searchName"  name="dynaFormData" maxlength="2000" styleClass="cltextbox-color" indexed="true" readonly="true" onchange="dataChanged()"/>
                      </td>--%>
							</tr>
							<%}%>
							<% count++ ; %>
						</logic:iterate>
					</table>
				</div>
			</td>
		</tr>

		<tr>
			<td class="theader" width="10px" height="20px"><a
				href="javascript:selectDiv('row2','historyRow2','historyData2','toggle2')"
				id="toggle2"> <img src='<%=path%>/coeusliteimages/minus.gif'
					id="imgtoggle2" name="imgtoggle2" border="none"></a></td>
			<td height="20" width="986" class="theader" style="font-size: 13px"><bean:message
					bundle="coi" key="AnnualDisclosure.FinacialEntities.Header" /></td>

		</tr>
		<%
      
      ht=23+totentity.intValue()*20;
      // ht=(ht==0)?40:ht;
      %>

		<tr id="row2" height='<%=ht%>px'>
			<td colspan="2" valign="top">
				<!--NOTE: DO NOT PUT THE DIV END TAG IN NEXT LINE NOT PUT EVEN A SINGLE SPACE
     MOZILLA RETURNS TRUE FOR HASCHILDNODES EVEN IF A SPACE IS PRESENT B/N START
     AND END TAG :(-->
				<div id="historyData2"
					style="overflow:none; position:absolute; height:<%=ht%>px;">
					<table align="left" width='986' border="0" cellpadding="0"
						cellspacing="0" class="table">

						<logic:present name="disclFinEntDet" scope="session">
							<% strBgColor = "#DCE5F1";
                     int disclIndex = 0;%>


							<logic:notEqual name="totentity" value='0'>
								<tr class="theaderBlue">
									<td height="20px" width="10px">&nbsp;</td>
									<td align="left" class="theader" width="507">Entity Name</td>
									<td align="left" class="theaderBlue" width="154">&nbsp;</td>
									<td align="left" class="theaderBlue" width="87">&nbsp;</td>
								</tr>
								<logic:iterate id="finEntDet" name="disclFinEntDet"
									type="org.apache.commons.beanutils.DynaBean">
									<bean:define id="entityNumber" name="finEntDet"
										property="entityNumber" />
									<bean:define id="entitySequenceNumber" name="finEntDet"
										property="entitySequenceNumber" />
									<%                                  
                                if (disclIndex%2 == 0) {
                                strBgColor = "#D6DCE5";
                                } else {
                                strBgColor="#DCE5F1";
                                }
                                String entitySeqNumber = (String)entitySequenceNumber;
                                %>
									<tr height='20px' bgcolor='<%=strBgColor%>' class="rowLine"
										onmouseover="className='rowHover rowLine'"
										onmouseout="className='rowLine'">
										<%-- Added for Case#4447 : FS_Next phase of COI enhancements - Start
                                  <td  height='20px'>&nbsp;</td> --%>
										<td><a
											href="javascript:selectRow('row<%=disclIndex+10%>','projectRow<%=disclIndex+10%>','projectData<%=disclIndex+10%>','toggle<%=disclIndex+10%>','projectFrame<%=disclIndex+10%>','<%=path%>/getFinEntPrj.do?entityNumber=<%=entityNumber%>&entitySequenceNumber=<%=entitySeqNumber%>')"
											id="toggle<%=disclIndex+10%>"> <img
												src='<%=request.getContextPath()%>/coeusliteimages/plus.gif'
												border='none' id="imgtoggle<%=disclIndex+10%>"
												name="imgtoggle<%=disclIndex+10%>" border="none">
										</a></td>
										<!--Modified Case#4447 : Next phase of COI enhancements - Start -->
										<%String coiDisclosureType = (String)request.getParameter("COI_DISCL_TYPE");%>
										<%if(coiDisclosureType != null && coiDisclosureType.equals("disclosureSearch")){%>
										<td class="copy"><a
											href='<%=path%>/reviewAnnFinEntity.do?entityNumber=<%=entityNumber%>&actionFrom=revDiscl&COI_DISCL_TYPE=<%=coiDisclosureType%>'><coeusUtils:formatOutput
													name="finEntDet" property="entityName" /> </a></td>
										<td class="copy"><a
											href='<%=path%>/reviewAnnFinEntity.do?entityNumber=<%=entityNumber%>&actionFrom=revDiscl&COI_DISCL_TYPE=<%=coiDisclosureType%>'><coeusUtils:formatOutput
													name="finEntDet" property="statusDesc" /> </a></td>
										<td class="copy"></td>
										<%}else{%>
										<td class="copy"><a
											href='<%=path%>/reviewAnnFinEntity.do?entityNumber=<%=entityNumber%>&actionFrom=revDiscl'><coeusUtils:formatOutput
													name="finEntDet" property="entityName" /> </a></td>
										<td class="copy"><a
											href='<%=path%>/reviewAnnFinEntity.do?entityNumber=<%=entityNumber%>&actionFrom=revDiscl'><coeusUtils:formatOutput
													name="finEntDet" property="statusDesc" /> </a></td>
										<td class="copy"></td>
										<%}%>
										<!--Case#4447 - End -->
									</tr>
									<tr id="projectRow<%=disclIndex+10%>" style="display: none"
										class="copy rowHover">
										<td colspan="5">
											<div id="projectData<%=disclIndex+10%>"
												style="overflow: hidden;"></div>
										</td>
									</tr>
									<%-- Case 4447 - End--%>
									<% disclIndex++;%>
								</logic:iterate>
							</logic:notEqual>

							<logic:equal name="totentity" value='0'>
								<tr>
									<td colspan="3"><bean:message bundle="coi"
											key="Annualdisclosure.Review.noFE" /></td>
								</tr>
							</logic:equal>
						</logic:present>
						<logic:notPresent name="disclFinEntDet" scope="session">
							<tr>
								<td colspan="3"><bean:message bundle="coi"
										key="Annualdisclosure.Review.noFE" /></td>
							</tr>
						</logic:notPresent>
					</table>
				</div>
			</td>
		</tr>
		<%-- <tr >
      <td class="theaderBlue" width="10px" height="20px"><a href="javascript:selectDiv('row3','historyRow3','historyData3','toggle3')" id="toggle3">
         <img src='<%=path%>/coeusliteimages/minus.gif' id="imgtoggle3" name="imgtoggle3" border="none"></a></td>
      <td height="22"  width="986" class="theaderBlue" style="font-size:13px"><bean:message bundle="coi" key="AnnualDisclosure.Proposals.Header"/></td>
      
    </tr>   
    <%
          ht=135+totProposals.intValue()*23;
     /* if(ht>maxht){
     ht=maxht; 
      }*/
      
      ht=(ht==0)?135:ht;
      %>
            <tr id="row3" style="" class="copy rowHover" height='<%=ht%>px' >
                <td colspan="2" valign="top" >
                    <!--NOTE: DO NOT PUT THE DIV END TAG IN NEXT LINE NOT PUT EVEN A SINGLE SPACE
     MOZILLA RETURNS TRUE FOR HASCHILDNODES EVEN IF A SPACE IS PRESENT B/N START
     AND END TAG :(-->
                    <%
                    List awardDisclosure, proposalDisclosure,protocolDisclosure;
                    awardDisclosure = new ArrayList();
                    proposalDisclosure = new ArrayList();
                    protocolDisclosure = new ArrayList();
                    if(disclProposals != null && disclProposals.size() > 0) {
                    
                    //  org.apache.commons.beanutils.DynaBean tempBean = (org.apache.commons.beanutils.DynaBean)reviewProposals.get(0);
                    //  dynaForm=(org.apache.struts.validator.DynaValidatorForm)reviewProposals.get(0);
                    String tempKey="";                
                    String strAward = "1";
                    String strProposal = "2";
                    for(int index = 0; index < disclProposals.size(); index++) {
                    dynaForm = (org.apache.struts.validator.DynaValidatorForm)disclProposals.get(index);
                    tempKey = ((Integer)dynaForm.get("moduleCode")).toString();
                    if(tempKey != null && tempKey.equalsIgnoreCase(strAward)) {
                    //Award Disclosure
                    awardDisclosure.add(dynaForm);
                    }//End IF
                    else if(tempKey != null && tempKey.equalsIgnoreCase(strProposal)) {
                    //Proposal Disclosure
                    proposalDisclosure.add(dynaForm);
                    }
                    else{
                        protocolDisclosure.add(dynaForm);
                        //End ELSE
                    }
                    }//End FOR
                    
                    }
                    request.setAttribute("proposalDisclosure", proposalDisclosure);
                    request.setAttribute("awardDisclosure", awardDisclosure);
                    request.setAttribute("protocolDisclosure", awardDisclosure);
                    request.setAttribute("awardDiscSize", new Integer(awardDisclosure.size()));
                    request.setAttribute("proposalDiscSize", new Integer(proposalDisclosure.size()));
                    request.setAttribute("protocolDiscSize", new Integer(proposalDisclosure.size()));
                    %>     
                    <div id="historyData3" style="overflow:auto; position: absolute;height:<%=ht%>px" >   
                    
                    <table  width="984" colspan='2' border="0" cellpadding="0" cellspacing="0" class="table"> 
                        <tr><td colspan="2">
                        <tr>
                            <td height="20" colspan="2" align="left" class="copybold"><bean:message bundle="coi" key="AnnualDisclosure.Proposals.List"/></td>
                        </tr>
                        <tr><td height='100%' colspan="2">
                                <logic:notEqual name='proposalDiscSize' value='0'>
                                    <table  width="984" height="100%"  align="left" colspan='2' border="0" cellpadding="0" cellspacing="0" class="table"> 
                                        <tr class="theader" >
                                            <td width="10" height='22px'>&nbsp;</td>
                                            <td  align="left" class="theaderBlue" width="194"><bean:message bundle="coi" key="label.proposalNumber"/></td>
                                            <td  align="left" class="theaderBlue" width="156"><bean:message bundle="coi" key="label.status"/></td>
                                            <td  align="left" class="theaderBlue" width="212"><bean:message bundle="coi" key="label.sponsor"/></td>
                                            <td align="left" class="theaderBlue" width="412"><bean:message bundle="coi" key="label.title"/></td>
                                        </tr>
                                        <%  int propIndex = 0;
                                        strBgColor = "#DCE5F1";
                                        
                                        %>
                                        <logic:iterate id='proposal' name="proposalDisclosure" type="org.apache.commons.beanutils.DynaBean">
                                            
                                            <%                                  
                                            if (propIndex%2 == 0) {
                                            strBgColor = "#D6DCE5";
                                            } else {
                                            strBgColor="#DCE5F1";
                                            }
                                            String proposalNumber=(String)proposal.get("moduleItemKey");
                                            %>                                        
                                            <tr bgcolor='<%=strBgColor%>' id="row<%=propIndex%>" class="rowLine" onmouseover="className='rowHover rowLine'" onmouseout="className='rowLine'">
                                                <td width="10" height="22">&nbsp;</td>
                                                <td align="left"  width="194"><coeusUtils:formatOutput name="proposal" property="moduleItemKey"/></td>
                                                <td align="left"  width="156"><coeusUtils:formatOutput name="proposal" property="description" /> </td>
                                                <td align="left"  width="212"><coeusUtils:formatOutput name="proposal" property="sponsorName"/></td>
                                            <td align="left"><coeusUtils:formatOutput name="proposal" property="title"/></td></tr>   
                                            <%propIndex++;%>
                                        </logic:iterate>
                                    </table>
                                </logic:notEqual>
                                <logic:equal name="proposalDiscSize" value="0">
                                    <table width="984"  border="0" align="center" cellpadding="2" cellspacing="0" class="tabtable">  
                                        <tr><td class="copy" height="22px"> <bean:message bundle="coi" key="AnnualDisclosure.Proposals.NOList"/></td></tr>
                                    </table>
                                </logic:equal>  
                        </td></tr>
                        <tr>
                            <td height="20px" colspan="2" align="left" class="copybold" height="22px"><bean:message bundle="coi" key="AnnualDisclosure.Awards.List"/></td>
                        </tr>
                        
                        <tr><td height="100%" colspan="2">
                                <logic:notEqual name='awardDiscSize' value='0'>
                                    <table  width="984"  align="left" colspan='2' border="0" cellpadding="0" cellspacing="0" class="table"> 
                                        
                                        <tr class="theader">
                                            <td width="10" height='22px'>&nbsp;</td>
                                            <td align="left" class="theaderBlue" width="194"><bean:message bundle="coi" key="label.awardNumber"/></td>
                                            <td align="left" class="theaderBlue" width="156"><bean:message bundle="coi" key="label.status"/></td>
                                            <td align="left" class="theaderBlue" width="212"><bean:message bundle="coi" key="label.sponsor"/></td>
                                            <td align="left" class="theaderBlue" width="412"><bean:message bundle="coi" key="label.title"/></td>
                                        </tr>
                                        
                                        <%  int awardIndex = 0;
                                        strBgColor = "#DCE5F1";
                                        
                                        %>
                                        <logic:iterate id='award' name="awardDisclosure" type="org.apache.commons.beanutils.DynaBean">
                                            <bean:define id='awardNumber' name='award' property="moduleItemKey" type="java.lang.String"/>
                                            <%                                  
                                            if (awardIndex %2 == 0) {
                                            strBgColor = "#D6DCE5";
                                            } else {
                                            strBgColor="#DCE5F1";
                                            }
                                            
                                            %>
                                            
                                            <tr bgcolor='<%=strBgColor%>' id="row<%=awardIndex%>" class="rowLine" onmouseover="className='rowHover rowLine'" onmouseout="className='rowLine'">
                                                <td width="10" height='22px'>&nbsp;</td>
                                                <td align="left"  width="194"><coeusUtils:formatOutput name="award" property="moduleItemKey"/></td>
                                                <td align="left"  width="156"><coeusUtils:formatOutput name="award" property="description" /></td>
                                                <td align="left"  width="212"><coeusUtils:formatOutput name="award" property="sponsorName"/></td>
                                                <td align="left"  width="412"> <coeusUtils:formatOutput name="award" property="title"/>
                                                <td>
                                            </tr>  
                                            <%awardIndex++;%>
                                        </logic:iterate>
                                    </table>
                                </logic:notEqual>
                                <logic:equal name="awardDiscSize" value="0">
                                    <table width="984"  border="0" align="center" cellpadding="2" cellspacing="0" class="tabtable">  
                                        <tr><td class="copy" height="22px"> <bean:message bundle="coi" key="AnnualDisclosure.Awards.NOList"/></td></tr>
                                    </table>
                                </logic:equal>    
                        </td></tr>
                        
                        <tr>
                            <td height="20px" colspan="2" align="left" class="copybold" height="22px"> <bean:message bundle="coi" key="AnnualDisclosure.Protocols.List"/></td>
                        </tr>
                        
                        <tr><td height="100%" colspan="2">
                                <logic:notEqual name='protocolDiscSize' value='0'>
                                    <table  width="984"  align="left" colspan='2' border="0" cellpadding="0" cellspacing="0" class="table"> 
                                        
                                        <tr class="theader">
                                            <td width="10" height='22px'>&nbsp;</td>
                                            <td align="left" class="theaderBlue" width="194"><bean:message bundle="coi" key="label.protocolNumber"/></td>
                                            <td align="left" class="theaderBlue" width="156"><bean:message bundle="coi" key="label.status"/></td>
                                            <td align="left" class="theaderBlue" width="212"><bean:message bundle="coi" key="label.sponsor"/></td>
                                            <td align="left" class="theaderBlue" width="412"><bean:message bundle="coi" key="label.title"/></td>
                                        </tr>
                                        
                                        <%  int protocolIndex = 0;
                                        strBgColor = "#DCE5F1";
                                        
                                        %>
                                        <logic:iterate id='protocol' name="protocolDisclosure" type="org.apache.commons.beanutils.DynaBean">
                                            <bean:define id='protocolNumber' name='award' property="moduleItemKey" type="java.lang.String"/>
                                            <%                                  
                                            if (protocolIndex%2 == 0) {
                                            strBgColor = "#D6DCE5";
                                            } else {
                                            strBgColor="#DCE5F1";
                                            }
                                            
                                            %>
                                            
                                            <tr bgcolor='<%=strBgColor%>' id="row<%=protocolIndex%>" class="rowLine" onmouseover="className='rowHover rowLine'" onmouseout="className='rowLine'">
                                                <td width="10" height='22px'>&nbsp;</td>
                                                <td align="left"  width="194"><coeusUtils:formatOutput name="award" property="moduleItemKey"/></td>
                                                <td align="left"  width="156"><coeusUtils:formatOutput name="award" property="description" /></td>
                                                <td align="left"  width="212"><coeusUtils:formatOutput name="award" property="sponsorName"/></td>
                                                <td align="left"  width="412"> <coeusUtils:formatOutput name="award" property="title"/>
                                                <td>
                                            </tr>  
                                            <%protocolIndex++;%>
                                        </logic:iterate>
                                    </table>
                                </logic:notEqual>
                                <logic:equal name="protocolDiscSize" value="0">
                                    <table width="984"  border="0" align="center" cellpadding="2" cellspacing="0" class="tabtable" >  
                                        <tr><td class="copy" height="22px"><bean:message bundle="coi" key="AnnualDisclosure.Protocols.NOList"/></td></tr>
                                    </table>
                                </logic:equal>    
                        </td></tr>
                    </table>
            </td></tr>
        </table></div>
        </td></tr> --%>
	</table>
</body>
</html>
