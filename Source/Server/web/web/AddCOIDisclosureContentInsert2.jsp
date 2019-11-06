<%--
/*
 * @(#)AddCOIDisclosureContent.jsp	1.0 2002/06/12 01:32:13 AM
 *
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 *
 * @author  RaYaKu
 */
--%>
<%-- This JSP file is for Adding a new COI Disclosure for a person--%>

<%@page import="edu.mit.coeus.coi.bean.CertificateDetailsBean,
		edu.mit.coeus.coi.bean.DisclosureInfoBean,
		edu.mit.coeus.coi.bean.ComboBoxBean,
		org.apache.struts.action.Action,
                java.net.URLEncoder" 
	errorPage="ErrorPage.jsp" %>
<%@ taglib uri="/WEB-INF/struts-html.tld"     prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld"     prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld"     prefix="logic" %>
<%@ taglib uri="/WEB-INF/coeus-utils.tld"     prefix="coeusUtils" %>
<%@ include file="CoeusContextPath.jsp"  %>

<!-- CASE #1374 All values are in actionform. DisclosureHeaderBean not needed -->
<%--<jsp:useBean id="disclosureHeaderBean" scope='session' class="edu.mit.coeus.coi.bean.DisclosureHeaderBean"/>--%>
<jsp:useBean id="collCOIDiscCertDetails" scope='session' class="java.util.Vector"/>
<jsp:useBean id="collCOIDisclosureInfo" scope='session' class="java.util.Vector"/>
<!-- CASE #1374: Get coi status collection from session -->
<jsp:useBean  id="collCOIStatus" scope="session" class="java.util.LinkedList" />
<jsp:useBean id="personId" scope ="request" class = "java.lang.String" />
<bean:size id="coiDiscCertificatesSize" name="collCOIDiscCertDetails" />
<bean:size id="collCOIDisclosureInfoSize" name="collCOIDisclosureInfo"/>

	<!-- Show Entity Conflict Status -->
	<logic:notEqual name="collCOIDisclosureInfoSize" value="0">
		  <!-- CASE #864 Remove align="right" from table attributes-->
		  <table width="100%" border="0" cellspacing="1" cellpadding="5">
			<tr>
			  <td colspan="6" class="fontBrown">
			  <b><bean:message key="addCOIDisclosure.label.conflictStatusInstructions1"/>
				<logic:equal name="frmAddCOIDisclosure" scope="session" property="disclosureTypeCode" value="1">
					<bean:message key="addCOIDisclosure.label.awardNoCap"/>
				</logic:equal>
				<logic:notEqual name="frmAddCOIDisclosure" scope="session" property="disclosureTypeCode" value="1">
					 <bean:message key="addCOIDisclosure.label.proposalNoCap"/>
				</logic:notEqual>
				<bean:message key="addCOIDisclosure.label.conflictStatusInstructions2"/>
			  </b>
			  </td>
			</tr>	
<!-- CASE #1374 Begin -->
	<!-- Drop-down to change conflict status for all entities. Doesn't work on Safari on Mac-->                  
<script language="javascript">
	var os = "";
	var isMac = 0;
	var isSafari = 0;
	
	try{
		os = navigator.platform;

		if(os.indexOf("Mac") != -1){
			isMac = 1;
		}

		if(navigator.userAgent.indexOf("Safari")!= -1){
			isSafari = 1;
		}
	}
	catch(e){
	}			

	if(!(isMac && isSafari)){
		//alert('in here');  
		document.write('<tr>');
		document.write('<td colspan="6">');
		document.write('Set conflict status for all entities to:&nbsp;');
		document.write('<select name="conflictStatus" onchange="changeAllStatus();">');
		document.write('<option selected>- Please Select -</option>');
	}
</script>
			<%
			String description1 = "";
			String code1 = "";
			for(int i=0;i<collCOIStatus.size();i++){
				ComboBoxBean objCombBean = (ComboBoxBean)collCOIStatus.get(i);
				code1 = objCombBean.getCode();
				description1 = "";
				if( code1.equals("301") || code1.equals("200") ){
					description1 = objCombBean.getDescription();
			%>

<script language="javascript">
	if(!(isMac && isSafari)){
		//alert("before option tag");
		document.write('<option value="<%=code1%>">');
		document.write('<%=description1%>');
		document.write('</option>');
	}
</script>
			<%
				}
			}
			%>
<script language="javascript">
	if(!(isMac && isSafari)){
		//alert("before ending the select");
		document.write('</select>');
		document.write('</td>');
		document.write('</tr>');
	}
</script>
			

<!-- CASE #1374 End -->	
			<tr bgcolor="#CC9999">
			  <td height="25" width="113">
				<div align="center"><font color="#FFFFFF"><bean:message key="addCOIDisclosure.label.entityName"/></font></div>
			  </td>
			  <td width="217" height="25" colspan="2">
				<div align="center"><font color="#FFFFFF"><bean:message key="addCOIDisclosure.label.conflictStatus"/></font></div>
			  </td>
			  <td width="111" height="25">
				<div align="center"><font color="#FFFFFF"><bean:message key="addCOIDisclosure.label.reviewedBy"/></font></div>
			  </td>
			  <td width="195" height="25">
				<div align="center"><font color="#FFFFFF"><bean:message key="addCOIDisclosure.label.description"/></font></div>
			  </td>
			  <td width="20" height="25">
              			&nbsp;
			  </td>
			</tr>

	<!-- CASE #1393 Begin -->
	<logic:present name="<%=Action.ERROR_KEY%>">
                <tr>
                <td colspan="6" align = "left">
			<font color="red">
			&nbsp;&nbsp;<html:errors property="invalidConflictStatus"/>
			&nbsp;&nbsp;<html:errors property="invalidConflictStatusHasMaintain"/> 
			</font>
		</td>
                </tr>	
         </logic:present>
	<!-- CASE #1393 End -->
	
            <%--<bean:message key="addCOIDisclosure.label.editDescription"/> --%>
			<%-- Struts1.0.2 disadvantage: Action Form do not support creation of dynamic form
			properties but it is possible in struts1.1( still beta and not stable release).

			So creation of dynamic properties followed  regular JSP way.

			--%>


	<%
               // get the previously selected values of questions and answers if available then display them
               //CASE #1374 Get preselected conflict status and description from session instead of request.
               String preSelectedConfStatus[] =(String[])session.getAttribute("selectedConflictStatus");
               String preSelectedDesc[] =(String[])session.getAttribute("selectedDescription");
                int disclIndex = 0;
                System.out.println("collCOIDisclosureInfo.size() on jsp: "+collCOIDisclosureInfo.size());
        %>
			<logic:iterate id="disclosureInfoBean" name="collCOIDisclosureInfo" 
			type="edu.mit.coeus.coi.bean.DisclosureInfoBean">
			<INPUT type='hidden' name='hdnEntityNum' value='<bean:write name="disclosureInfoBean" property="entityNumber"/>'>
			<INPUT type='hidden' name='hdnEntSeqNum' value='<bean:write name="disclosureInfoBean" property="entSeqNumber"/>'>
			<INPUT type='hidden' name='hdnSeqNum' value='<bean:write name="disclosureInfoBean" property="seqNumber"/>'>
			<tr bgcolor="#FBF7F7">
			<!-- CASE #864 Remove td width="113" attribute-->
			  <td height="25">
				<div align="left"><bean:write name="disclosureInfoBean" property="entityName" /></div>
			  </td>
<!-- CASE #1046 Comment Begin -->
<%--			  
			  <td  height="25">
			<div>
                      <select name='sltConflictStat<%=disclIndex%>' >

                        <%
                        String  preSelectedConflictStatus = "";
                        if( (preSelectedConfStatus != null) && (preSelectedConfStatus.length > 0 ) ){
                            preSelectedConflictStatus = preSelectedConfStatus[disclIndex];
                        }
                        String strSelectedNPR = "";
                        String strSelectedNC = "";
                        String strSelectedPIC = "";

                        strSelectedNPR = "100".equals(preSelectedConflictStatus.trim())?"selected":"";
                        strSelectedNC = "200".equals(preSelectedConflictStatus.trim())?"selected":"";
                        strSelectedPIC = "301".equals(preSelectedConflictStatus.trim())?"selected":"";
                        %>
                            <option <%= strSelectedNPR %> value="100">Not Previously Reported</option>
                            <option <%= strSelectedNC %> value="200">No conflict</option>
                            <option <%= strSelectedPIC %> value="301">PI Identified Conflict</option>
                          </select>
                          </div>
			  </td>
--%>
<!-- CASE #1046 Comment End -->
<!-- CASE #1046 Begin-->
			 <!-- CASE #1374 Add * for required field -->
			 <td width="1" valign="top" align="right">
			<font color="red"><b>*</b></font>
			  </td>
			  <td height="25">
			  <div>
		
                      <select name='sltConflictStat<%=disclIndex%>'>
                        <%
                        
                        String  preSelectedConflictStatus = "";
                        //If page is being reloaded after validation errors, get user-entered values.
                        if( (preSelectedConfStatus != null) && (preSelectedConfStatus.length > 0 ) ){
                            preSelectedConflictStatus = preSelectedConfStatus[disclIndex];
                        }
                        //System.out.println("preSelectedConflictStatus: "+preSelectedConflictStatus);
                        String code = "";
                        String description = "";
                        String selected = "";
			/*CASE #1374 Don't include Not Previously Reported in the drop down.  Default to Select Conflict Status*/
			%>
			<option <%=selected%>  value=''>- Please select -</option> 
			<%			
                        for(int i=0; i<collCOIStatus.size(); i++){
                        	ComboBoxBean comboBox = (ComboBoxBean)collCOIStatus.get(i);
                        	code = comboBox.getCode();
				description = comboBox.getDescription(); 
				selected = "";
				//Not Previously Reported, No Conflict and PI Potential Conflict are the only
				//possible options here.
				if(code.equals("200") || code.equals("301") ){
					//System.out.println("code: "+code+", description: "+description);
					if ( preSelectedConflictStatus.equals(code) ) {
						selected = "selected";
					}
					//If page is being loaded for the first time, status will be Not Previously Reported
					//for all entities.
					else if( code.equals("100") && preSelectedConflictStatus.equals("") ){
						selected = "selected";
					}
					%>
					<option <%=selected%>  value='<%=code%>'><%=description%></option> 
					<%
					//System.out.println("<option "+selected+" value="+code+">"+description+"<option>");
				}
			}
			%>
                          </select>
                          </div>
			  </td>
<!-- CASE #1046 End -->

			  <td height="25" align="center">
				<div align="center"><bean:message key="addCOIDisclosure.label.pi"/></div>
			  </td>
			  <td height="25">
<%
              String disclDescription = "";
              String  preSelectedDescription ="";
              if( (preSelectedDesc != null) && (preSelectedDesc.length > 0 ) ){
                preSelectedDescription = preSelectedDesc[disclIndex];
                disclDescription = preSelectedDescription;
              }else{
                   disclDescription = (disclosureInfoBean.getDesc() == null?"":disclosureInfoBean.getDesc());
              }
              String encDescription = URLEncoder.encode(disclDescription);
%>
		<div>

              <INPUT type='text' size='28' name='description<%=disclIndex%>' value="<%= disclDescription %>" maxlength="1000">
		</div>
		  <%--
              <INPUT type='text' size='25' name='description<%=disclIndex%>' disabled
                        onfocus='javascript:changeFocus(sltDiscStat<%=disclIndex%>);' value='<%= disclDescription %>'>
		  --%>


				<%-- <div align="center"><coeusUtils:formatOutput name="disclosureInfoBean" property="desc"  defaultValue="&nbsp;" /></div> --%>
			  </td>
			  </td>
			  <td  height="25">
               <a href="JavaScript:openwinDesc('EditDisclosureDescription.jsp?desc=<%=encDescription%>&index=<%=disclIndex++%>','Description');">
                    <img src="<bean:write name='ctxtPath'/>/images/more.gif" width="22" height="20" border="0">
               </a>
				<%-- <div align="center"><coeusUtils:formatDate name="disclosureInfoBean" property="lastUpdated" /></div> --%>
			  </td>

			</tr>
			</logic:iterate>
			<!-- Entity Information ends -->
		  </table>
	</logic:notEqual>
	<logic:equal name="collCOIDisclosureInfoSize" value="0">
              <table width="100%" border="0" cellpadding="5" cellspacing="0">
              <tr>
              	<td height="5">&nbsp;</td>
              </tr>
              <tr>
              	<td>
              &nbsp;&nbsp;<bean:message key="addCOIDisclosure.noFinEnt"/>
              </td>
              </tr>
              </table>
	
	</logic:equal>
		  