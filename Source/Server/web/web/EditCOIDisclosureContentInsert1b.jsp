<%--
/*
 * @(#) EditCOIDisclosureContentInsert1b.jsp 1.0 2002/06/05
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
                java.net.URLEncoder" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" 	prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" 	prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" 	prefix="logic" %>
<%@ taglib uri="/WEB-INF/privilege.tld" prefix="priv" %>
<%@ taglib uri="/WEB-INF/coeus-utils.tld"	prefix="coeusUtils" %>
<%@ include file="CoeusContextPath.jsp"  %>

<jsp:useBean  id="action" 			scope="session" class="java.lang.String" />
<jsp:useBean  id="disclosureNo" 			scope="session" class="java.lang.String" />
<jsp:useBean  id="discHeaderAwPrInfo" 	scope="session" class="edu.mit.coeus.coi.bean.DisclosureHeaderBean" />
<jsp:useBean  id="moduleCode" 				scope="session" class="java.lang.String" />
<jsp:useBean  id="disclosureHeader" 		scope="session" class="edu.mit.coeus.coi.bean.DisclosureHeaderBean" />
<jsp:useBean  id="collCOIDisclStatus"		scope="session" class="java.util.LinkedList" />
<jsp:useBean  id="collCOIReviewer"		scope="session" class="java.util.LinkedList" />
<jsp:useBean id = "userprivilege" class = "java.lang.String" scope="session"/>

<priv:hasOSPRight name="hasOspRightToEdit" value="<%=Integer.parseInt(userprivilege)%>"> 
	<jsp:useBean id="hasMaintainCOI" class="java.lang.String" scope="session"/>
</priv:hasOSPRight>
<!-- CASE #1374 Show disclosure details only if user has maintain coi -->
<logic:present name="hasMaintainCOI" scope="session">
                <tr  bgcolor="#F7EEEE">
                	<td width="5" height="30" >&nbsp;</td>
                  <td width="100" height="30"  align="left" >
                    <div><bean:message key="editCOIDisclosure.label.disclosureType"/></div>
                  </td>
                  <td width="6" height="30" ><div>:</div></td>
                  <td width="176" height="30" >
                    <div><b>
                    <logic:equal name="action" value="edit" >
				<html:select  property='disclosureType'>
				   <html:option value="I">Initial</html:option>
				   <html:option value="A">Annual</html:option>
				</html:select>
			</logic:equal>
                    </b> &nbsp;</div>
                  </td>


                  <td width="123" height="30" align="left" >
                    <div>
			<logic:equal name="moduleCode" scope="session" value="1" >
				<bean:message key="editCOIDisclosure.label.award"/>
			</logic:equal>
			<logic:notEqual name="moduleCode" scope="session" value="1">
				<bean:message key="editCOIDisclosure.label.proposal"/>
			</logic:notEqual>
			  &nbsp;                    
                    <bean:message key="editCOIDisclosure.label.status"/>
                    </div>
                  </td>
                  <td width="3" height="30" ><div>:</div></td>
                  <td width="238" height="30" align="left" >
                    <div><b><bean:write name="discHeaderAwPrInfo" scope="session" property="status"/></b>&nbsp;</div>
                  </td>




                </tr>
                <tr bgcolor="#FBF7F7" >
                	<td width="5" height="30" >&nbsp;</td>
                  <td width="100" height="30">
                    <div><bean:message key="editCOIDisclosure.label.reviewedBy"/>&nbsp;</div>
                  </td>
                  <td height="30" width="6"><div>:</div></td>
                  <td height="30" width="176" >
                    <logic:equal name="action" value="edit" >
                    <!-- check for maintain coi disclosure role	If the user has this role, then make reviewed by field editable.--> 
			<logic:present name="hasMaintainCOI">
			<div><b>
				<select name = 'disclReviewerCode'>
				<%
				String discReviewerCode = disclosureHeader.getReviewerCode();
				int revListSize = collCOIReviewer.size();
				System.out.println("revListSize: "+revListSize);
				for(int i=0; i<revListSize; i++)
				{
					String selected = "";				
					ComboBoxBean comboBox = (ComboBoxBean)collCOIReviewer.get(i);
					String revCode = comboBox.getCode();
					String revDescription = comboBox.getDescription();
					if(revCode.equalsIgnoreCase(discReviewerCode))
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
			</b></div>
			</logic:present>
			<logic:notPresent name="hasMaintainCOI" >
                    		<div><b>
                    		<bean:write name="disclosureHeader" scope="session" property="reviewer"/>
                    		</div></b>&nbsp;
                    		<input type="hidden" name="disclReviewerCode" 
                    		value="<bean:write name='disclosureHeader' scope='session' property='reviewerCode'/>" />
                    	</logic:notPresent>
                    </logic:equal>
                    <logic:notEqual name="action" value="edit">
                    <b><bean:write name="disclosureHeader" scope="session" property="reviewer"/></b>&nbsp;
                    </logic:notEqual>
                  </td>
                  
                  
                  <td width="123" height="30">
                    <div><bean:message key="editCOIDisclosure.label.disclosureStatus"/></div>
                  </td>
                  <td width="3" height="30"><div>:</div></td>
                  <td width="238" height="30">
                    <logic:equal name="action" value="edit" >
                    <!-- Check for maintain coi disclosure role.  If user has this role, then make disclosure status editable.  	-->
			<logic:present name="hasMaintainCOI">
			<div><b>
			<select  name='disclStatCode'>
				<%
				String disclStatCode = disclosureHeader.getDisclStatCode();
				int statListSize = collCOIDisclStatus.size();
				for(int i=0; i<statListSize; i++)
				{
					String selected = "";
					ComboBoxBean comboBox = (ComboBoxBean)collCOIDisclStatus.get(i);
					String statCode = comboBox.getCode();
					String statDescription = comboBox.getDescription();
					if(statCode.equalsIgnoreCase(disclStatCode))
					{
						selected = "selected";
					}
				%>
				   <option <%= selected %> value="<%= statCode %>"><%= statDescription %>
				   </option>
				<%
				}
				%>

			</select>
			</b></div>
			</logic:present>
			<logic:notPresent name="hasMaintainCOI">   
				<div><b>
				<bean:write name="disclosureHeader" scope="session" property="disclStatus"/>
				</b></div>
                    		&nbsp;
                    		<input type="hidden" name="disclStatCode" 
                    		value='<bean:write name="disclosureHeader" scope="session" property="disclStatCode"/>' />
			</logic:notPresent>			
                    </logic:equal>
                    <logic:notEqual name="action" value="edit">
                    <b><bean:write name="disclosureHeader" scope="session" property="disclStatus"/></b>&nbsp;
                    </logic:notEqual>
                    </div>
                  </td>


  
                </tr>

              </table>
<!-- End disclosure details -->
<!-- CASE #1374 Begin -->
</logic:present>
<!-- CASE #1374 End -->