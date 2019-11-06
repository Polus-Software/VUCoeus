<%--
/*
 * @(#) AnnualDisclosuresMain.jsp 1.0 2002/06/05
 *
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 *
 * @author  Geo Thomas.
 */
--%>

<%-- This JSP file is for listing the COI Disclosures of the user--%>
<%@page import="edu.mit.coeus.coi.bean.ComboBoxBean,
		org.apache.struts.action.Action,
                java.net.URLEncoder" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" 	prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" 	prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" 	prefix="logic" %>
<%@ taglib uri="/WEB-INF/privilege.tld" prefix="priv" %>
<%@ taglib uri="/WEB-INF/coeus-utils.tld"	prefix="coeusUtils" %>
<%@ include file="CoeusContextPath.jsp"  %>

<jsp:useBean  id="action" 			scope="session" class="java.lang.String" />
<jsp:useBean  id="disclosureNo" 			scope="session" class="java.lang.String" />
<jsp:useBean  id="collCOIDisclosureInfo"	scope="session" class="java.util.Vector" />
<bean:size id="collCOIDisclosureInfoSize"   name="collCOIDisclosureInfo" />
<jsp:useBean  id="collCOIStatus" 		scope="session" class="java.util.LinkedList" />
<jsp:useBean  id="collCOIReviewer"		scope="session" class="java.util.LinkedList" />
<jsp:useBean id = "userprivilege" class = "java.lang.String" scope="session"/>


<priv:hasOSPRight name="hasOspRightToEdit" value="<%=Integer.parseInt(userprivilege)%>">
	<jsp:useBean id="hasMaintainCOI" class="java.lang.String" scope="session"/>
</priv:hasOSPRight>

	<%	System.out.println("inside editcoidisclsourecontentinsert2.jsp");	%>

              <table width="100%" border="0" cellspacing="0" cellpadding="0">
                <tr><td background="<bean:write name="ctxtPath"/>/images/tabback.gif">
                    <table border=0 cellpadding=0 cellspacing=0>
                      <tr><td><img src="<bean:write name="ctxtPath"/>/images/finEntDisc.gif" width="167" height="24"></td>
                      </tr>
                    </table>
                </td></tr>
              </table>

              <!-- Show Entity Conflict Status Information -->
              <logic:notEqual name="collCOIDisclosureInfoSize" value="0" >
              <!-- CASE #864 Remove table attribute align="right" -->
             <table width="100%" border="0" cellspacing="1" cellpadding="5">
                <tr><td colspan="6" height="2"></td></tr>
                <tr>
                	<td colspan="6" class="fontBrown">
                	<b><bean:message key="viewCOIDisclosureDetails.conflictStatusInstructions"/>
				<logic:equal name="moduleCode" scope="session" value="1">
					<bean:message key="viewCOIDisclosureDetails.award"/>
				</logic:equal>
				<logic:notEqual name="moduleCode" scope="session" value="1">
					 <bean:message key="viewCOIDisclosureDetails.proposal"/>
				</logic:notEqual>             	
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
                    <div align="center"><font color="#FFFFFF"><bean:message key="editCOIDisclosure.label.entityName" /></font></div>
                  </td>
                  <td width="217" height="25" colspan="2">
                    <div align="center"><font color="#FFFFFF"><bean:message key="editCOIDisclosure.label.conflictStatus" /></font></div>
                  </td>
                  <td width="111" height="25">
                    <div align="center"><font color="#FFFFFF"><bean:message key="editCOIDisclosure.label.reviewedBy" /></font></div>
                  </td>
                  <td width="195" height="25">
                    <div align="center"><font color="#FFFFFF"><bean:message key="editCOIDisclosure.label.description" /></font></div>
                  </td>
                  <td width="20" height="25">&nbsp;
                    <%-- <div align="center"><font color="#FFFFFF"><bean:message key="editCOIDisclosure.label.lastUpdated" /></font></div> --%>
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
                
<%
    int disclIndex = 0;
%>
                <logic:iterate id="disclosureInfo" name="collCOIDisclosureInfo" scope="session"
                type="edu.mit.coeus.coi.bean.DisclosureInfoBean" >
                <INPUT type='hidden' name='hdnEntityNum' value='<%= disclosureInfo.getEntityNumber() %>'>
                <INPUT type='hidden' name='hdnEntSeqNum' value='<%= disclosureInfo.getEntSeqNumber() %>'>
                <INPUT type='hidden' name='hdnSeqNum' value='<%= disclosureInfo.getSeqNumber() %>'>
               
                <tr bgcolor="#FBF7F7">
		<td height="25">
			<div>
                  	<a href="<bean:write name='ctxtPath' />/viewDisclosureDetails.do?disclNo=<%=disclosureNo %>&amp;entNo=<bean:write name='disclosureInfo' property='entityNumber'  />" >
                  		<bean:write name='disclosureInfo' property='entityName'  />
                    	</a>
                    	</div>
                  </td>
			 <!-- CASE #1374 Add * for required field -->
			 <td width="1" valign="top" align="right" >
			 <font color="red"><b>*</b></font>
			  </td>
                  
                  <td height="25">
                  
                  	<logic:present name="hasMaintainCOI">
                  	<div>
				<SELECT name='sltConflictStat<%=disclIndex%>'>
					<%
					String strConfStatCode = disclosureInfo.getConflictStatusCode();
					int lstSize = collCOIStatus.size();
					for(int i=0;i<lstSize;i++){
						ComboBoxBean objCombBean = (ComboBoxBean)collCOIStatus.get(i);
						String code = objCombBean.getCode();
						String description = objCombBean.getDescription();
						String strSelected = "";
						if(code.equalsIgnoreCase(strConfStatCode)) {
						    strSelected = "selected";
						}
						%>
						<option <%= strSelected %> value="<%= code %>"><%= description %></option>
					<%
					}
					%>
				</SELECT>
			</div>
			</logic:present>

			<logic:notPresent name="hasMaintainCOI" >


			<!--  Dynamically generate drop down list.  Show the selected value as well as description for codes 301 and 200.  -->
				<div>
				<SELECT name='sltConflictStat<%=disclIndex%>'>
					<%
					String strConfStatCode = disclosureInfo.getConflictStatusCode();
					int lstSize = collCOIStatus.size();
					for(int i=0;i<lstSize;i++){
						ComboBoxBean objCombBean = (ComboBoxBean)collCOIStatus.get(i);
						String code = objCombBean.getCode();
						String description = objCombBean.getDescription();
						if(code.equalsIgnoreCase(strConfStatCode)) {
						%>
							<option selected value="<%= code %>"><%= description %></option>
						<%
						}
					}
					for(int i=0;i<lstSize;i++){
						ComboBoxBean objCombBean = (ComboBoxBean)collCOIStatus.get(i);
						String code = objCombBean.getCode();
						String description = objCombBean.getDescription();
						if(!code.equalsIgnoreCase(strConfStatCode) && (code.equalsIgnoreCase("301") || code.equalsIgnoreCase("200"))) {
						%>
							<option value="<%= code %>"><%= description %></option>
						<%
					    }
					}

					%>
				</SELECT>
				</div>
			</logic:notPresent>

                  </td>
                  <td height="25">
                  <div align="center">
                  <logic:equal name="action" scope="session" value="edit">
                  	<!-- check for maintain COI role  -->
                  	<logic:present name="hasMaintainCOI">
				<select name='sltReviewerCode<%=disclIndex%>'>
				<%
					String reviewerCode = disclosureInfo.getReviewerCode();
					int revListSize = collCOIReviewer.size();
					for(int i=0; i<revListSize; i++)
					{
						String selected = "";
						ComboBoxBean comboBox = (ComboBoxBean)collCOIReviewer.get(i);
						String revCode = comboBox.getCode();
						String revDescription = comboBox.getDescription();
						if(revCode.equalsIgnoreCase(reviewerCode))
						{
							selected = "selected";
						}
					%>
					   <option <%= selected %> value="<%= revCode %>"><%= revDescription %>
					   </option>
					<%
					}
					%>
				</select>
			</logic:present>
                  	<logic:notPresent name="hasMaintainCOI">
                  		<bean:write name="disclosureInfo" property="reviewer"/>
                  	</logic:notPresent>
		</logic:equal>
		<logic:notEqual name="action" scope="session" value="edit">
			<bean:write name="disclosureInfo" property="reviewer"/>
		</logic:notEqual>
                  </div>
                  </td>


                  <td height="25">
                  <%
                    String tmpDescr = "";
                    String disclDescription = (tmpDescr=disclosureInfo.getDesc())==null?"":tmpDescr;
                  %>
		<div>
                    <INPUT type='text' size='25' name='description<%=disclIndex%>' value="<%= disclDescription %>" maxlength="1000">
		</div>

                  </td>
                  <td height="25">
                  <%
                   String encDescription= "";
                   encDescription = URLEncoder.encode(disclDescription);
                  %>

                    <a href='JavaScript:openwinDesc ("EditDisclosureDescription.jsp?desc=<%=encDescription%>&index=<%=disclIndex++%>","Description");'>
                    <img src="<bean:write name='ctxtPath'/>/images/more.gif" width="22" height="20" border="0">
                    </a>
                  </td>


                </tr>
			</logic:iterate>
              </table>

              </logic:notEqual>
              <!-- CASE #1374 Begin -->
              <logic:equal  name="collCOIDisclosureInfoSize" value="0" >
              <table width="100%" border="0" cellpadding="5" cellspacing="0">
              <tr>
              	<td height="5">&nbsp;</td>
              </tr>
              <tr>
              	<td>
              &nbsp;&nbsp;<bean:message key="editCOIDisclosure.noFinEnt"/>
              </td>
              </tr>
              </table>
              </logic:equal>
              <!-- CASE #1374 End -->
              <!-- End of Disclosure Information -->
