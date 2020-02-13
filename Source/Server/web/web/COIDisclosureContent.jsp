<%--
/*
 * @(#)COIDisclosureContent.jsp 1.0 2002/05/10  08:00:09 AM
 *
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 *
 * @author  RaYaKu
 */
--%>
<%--
A view component to dispaly all COI disclosures of selected search criteria, and
this page output is included in COIDisclosure.jsp page in runtime.
--%>
<%@ page import="java.util.Vector,edu.mit.coeus.utils.UtilFactory,
		edu.mit.coeus.coi.bean.*,
		org.apache.struts.action.Action,
		java.net.URLEncoder"
		errorPage = "ErrorPage.jsp" %>
<%@ taglib uri="/WEB-INF/struts-html.tld"     prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld"     prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld"    prefix="logic" %>
<%@ taglib uri="/WEB-INF/request.tld"		  prefix="req" %>
<%@ taglib uri="/WEB-INF/coeus-utils.tld"	  prefix="coeusUtils" %>
<%@ taglib uri="/WEB-INF/privilege.tld" prefix="priv" %>
<jsp:useBean id = "userprivilege" class = "java.lang.String" scope="session"/>
<%@ include file="CoeusContextPath.jsp"  %>
<%-- Find out the size of collCOIDisclosures and collCOIDisclosuresStatus
(see the parent page,COIDisclosure.jsp)--%>
<bean:size id="allCOIDisclosuresSize" name="collCOIDisclosures" />
<bean:size id="allCOIDisclosuresStatusSize" name="collCOIDisclosuresStatus"  />
<%
//System.out.println("privilege=>"+userprivilege);
//Create a vector of LabelValueBean beans which holds options information
//of appliesTo (html select) component.
Vector optionsAppliesTo = new Vector();
optionsAppliesTo.add(new ComboBoxBean("", ""));
optionsAppliesTo.add(new ComboBoxBean("Award","1"));
optionsAppliesTo.add(new ComboBoxBean("Institute Proposal","2"));
optionsAppliesTo.add(new ComboBoxBean("All","ALL"));
pageContext.setAttribute("optionsAppliesTo",optionsAppliesTo);
//Create a vector of LabelValueBean beans which holds options  information
//of Type (html select) component.
Vector optionsType = new Vector();
optionsType.add(new ComboBoxBean("", ""));
optionsType.add(new ComboBoxBean("Initial","I"));
optionsType.add(new ComboBoxBean("Annual","A"));
optionsType.add(new ComboBoxBean("All","ALL"));
pageContext.setAttribute("optionsType",optionsType);
%>

<html:form action="/coidisclosure.do" >
<!--
<table width="100%" cellpadding="0" cellspacing="0" border="0">
<tr>
	<td height="2">&nbsp;</td>
</tr>
</table>
-->
<table width="100%" cellpadding="0" cellspacing="0" border="0">
     <tr>
        <td background="<bean:write name="ctxtPath"/>/images/separator.gif" width="1">
            <image src="<bean:write name="ctxtPath"/>/images/separator.gif" width="1">
        </td>
        <td width="645">
	  	<table width="100%" border="0" cellspacing="0" align="left" cellpadding="0">
	  		<tr>
	  		<td>
	  			<table width="100%" border="0" cellspacing="0" cellpadding="5">
		  		<tr bgcolor="#cccccc">

				  <td height="23" class="header"> 
				    <bean:message key="coiDisclosure.header" />
				    <bean:write name="frmCOIDisclosure" property="personName" /> 
				  </td>
				  </tr>
				  <!-- CASE #1046 Comment Begin -->
				  <%--
				  <tr>
					<td><div><bean:message key="coiDisclosure.introduction" /></div>
					</td>
				  </tr>
				  --%>
				  <!-- CASE #1046 Comment End -->
				  <!-- CASE #1046 Begin -->
				  <logic:present name="customizedListIntroduction" scope="request">
				  <tr>
					<td class="fontBrown">
					<b><bean:message key="coiDisclosure.introductionCustomizedList" /></b>
					</td>
				  </tr>
				  </logic:present>
				  <logic:notPresent name="customizedListIntroduction" scope="request">
				  <tr>
					<td class="fontBrown">
					<b><bean:message key="coiDisclosure.introduction" /></b>
					</td>
				  </tr>
				  </logic:notPresent>
				  <!-- CASE #1046 End -->
				  <tr>
					<td align="right">
						<a href="<bean:write name="ctxtPath"/>/midyeardisclosure.do"><img src="<bean:write name="ctxtPath"/>/images/midyear_disclosure.gif" border="0"></a>&nbsp;&nbsp;
					</td>
				  </tr>				  
				  </table>
			</td>
		  </tr>
		  <tr>
		    <td height="23">
		      <table width="100%" border="0" cellspacing="0" cellpadding="0">
			<tr>
			  <td background="<bean:write name="ctxtPath"/>/images/tabback.gif">
			    <table border=0 cellpadding=0 cellspacing=0>
			      <tr>
				<td> <img src="<bean:write name="ctxtPath"/>/images/coidisclosure.gif" width="120" height="24"></td>
			      </tr>
			    </table>
			  </td>
			</tr>
		      </table>
		      <!-- Listing All COI Disclosures -->

		      <table width="100%" border="0" cellspacing="1" cellpadding="5" align="right" >
			<tr>
				<td>
				    <!-- CASE #1393 Put validation heading before errors -->
					<!-- Display errors -->
					<logic:present name="<%=Action.ERROR_KEY%>">
						<b><bean:message key="validationErrorHeader"/></b>
						<bean:message key="validationErrorSubHeader1"/>
						<html:errors/>
						</font>
					</logic:present>				    
				</td>
			  </tr>

		      <logic:notPresent name="org.apache.struts.action.ERROR">
		      <!-- CASE #1400.  Adjust column widths and add column for View disclosure -->
			<tr bgcolor="#CC9999" >
			  <td width="50" height="25">
			    <div align="center"><font color="#FFFFFF">
				&nbsp;</font></div>
			  </td>			
			  <td width="85" height="25">
			    <div align="center"><font color="#FFFFFF">
						<bean:message key="coiDisclosure.label.disclosurenumber" /></font></div>
			  </td>
			  <td width="80" height="25"><div align="center">
			  <font color="#FFFFFF"><bean:message key="coiDisclosure.label.status" /></font></div>
			  </td>
			  <td width="80" height="25"><div align="center">
			    <font color="#FFFFFF"><bean:message key="coiDisclosure.label.appliesto" /></font></div>
			  </td>

			  <td width="86" height="25">
			    <div align="center"><font color="#FFFFFF"><bean:message key="coiDisclosure.label.sponsor" /></font></div>
			  </td>
			  <td width="430" height="25" colspan="5">
			    <div align="center"><font color="#FFFFFF"><bean:message key="coiDisclosure.label.title" /></font></div>
			  </td>

				<%--
			  <td width="79" height="25">
			    <div align="center"><font color="#FFFFFF"><bean:message key="coiDisclosure.label.itemkey" /></font></div>
			  </td>
			  <td width="103" height="25">
			    <div align="center"><font color="#FFFFFF"><bean:message key="coiDisclosure.label.discType" /></font></div>
			  </td>
			  <td width="79" height="25">
			    <div align="center"><font color="#FFFFFF"><bean:message key="coiDisclosure.label.review" /></font></div>
			  </td>
			  <td width="135" height="25">
			    <div align="center"><font color="#FFFFFF"><bean:message key="coiDisclosure.label.update" /></font></div>
			  </td>
				--%>

			</tr>
					<%-- show the results in alternate colors --%>
			<% int disclosureCount=0; %>
			<logic:present name="collCOIDisclosures"  scope="session" >
				<logic:iterate id="discHeader" name="collCOIDisclosures" type="edu.mit.coeus.coi.bean.DisclosureHeaderBean">
			<tr valign="left" bgcolor='<%=( (disclosureCount++) % 2 == 0 ? "#FBF7F7" : "#F7EEEE") %>'>
				<%	String disclTitleStr = "";
					String disclTitle = "";
					disclTitleStr = discHeader.getTitle();
					if ( (disclTitleStr != null ) && (disclTitleStr != "") ) {
					disclTitle = URLEncoder.encode(disclTitleStr);
					}
				%>
			<!-- CASE #1400 Begin -->
			<td>
			<div>
			<a href="<bean:write name='ctxtPath'/>/viewCOIDisclosureDetails.do?action=view&disclNo=<bean:write name='discHeader' property='disclosureNo' />" >
			<bean:message key="coiDisclosure.label.viewDisclosure"/>
			</a>
			</div>
			</td>
			<!-- CASE #1400 End -->
			<td height="25">

			    <div align="left">
			    <!-- CASE #1400 Comment Begin -->
			    <%--
				<a href="<bean:write name='ctxtPath'/>/viewCOIDisclosureDetails.do?action=view&disclNo=<bean:write name='discHeader' property='disclosureNo' />" >
				    <coeusUtils:formatOutput name="discHeader" property="disclNo"/>
				</a>
			 --%>
			 <!-- CASE #1400 Comment End -->
			 <!-- CASE #1400 Begin -->
			 <!-- Spell out Annual or Initial instead of A or I.  Don't make the number a link. -->
				    <coeusUtils:formatOutput name="discHeader" property="disclosureNo"/>
				<br>
				   (<coeusUtils:formatOutput name="discHeader" property="disclType"/>)
			<!-- CASE #1400 End -->
				</div>
			  </td>
			<td height="25">
			    <div align="left"> <coeusUtils:formatOutput name="discHeader" property="status" defaultValue="&nbsp;" /> </div>
			  </td>
			  <td height="25">
			    <div align="left"> <coeusUtils:formatOutput name="discHeader" property="appliesTo" defaultValue="&nbsp;" /> </div>
			  </td>

			  <td height="25">
			    <div align="left">
			    	<!-- CASE #1374 change discHeader property from sponsor, which has sponsor code and name, to sponsor -->
			    	<coeusUtils:formatOutput name="discHeader" property="sponsorName" defaultValue="&nbsp;" /> 
			    </div>
			  </td>
			  <td height="25" colspan="5">
			  <!-- CASE #1400.  Make the title a link to the disclosure -->
			    <div align="left"> 
				<a href="<bean:write name='ctxtPath'/>/viewCOIDisclosureDetails.do?action=view&disclNo=<bean:write name='discHeader' property='disclosureNo' />" >			    <coeusUtils:formatOutput name="discHeader" property="title" defaultValue="&nbsp;" /> 
				</div>
				</a>
			  </td>

				<%--
			  <td  height="25">
			    <div align="center"> <coeusUtils:formatOutput name="discHeader" property="keyNumber" defaultValue="&nbsp;" /> </div>
			  </td>
			  <td height="25">
			    <div align="center"> <coeusUtils:formatOutput name="discHeader" property="disclType" defaultValue="&nbsp;" /> </div>
			  </td>
			  <td height="25">
			    <div align="center"> <coeusUtils:formatOutput name="discHeader" property="reviewer" defaultValue="&nbsp;" /> </div>
			  </td>
			  <td width="135" height="25">
						<div align="center"><coeusUtils:formatDate name="discHeader" property="updatedDate" /></div>
			  </td>
				--%>


			</tr>
					</logic:iterate>
					</logic:present>
			</logic:notPresent>
			<!--<tr>
			  <td width="91" height="25">&nbsp;</td>
			  <td width="63" height="25">&nbsp;</td>
			  <td width="69" height="25">&nbsp;</td>
			  <td width="79" height="25">&nbsp;</td>
			  <td width="103" height="25">&nbsp;</td>
			  <td width="79" height="25">&nbsp;</td>
			  <td width="135" height="25">
			    <div align="right"> </div>
			  </td>
			</tr>-->
		      </table>
				<!-- End of listing all COI Disclosures and their details -->
		    </td>
		  </tr>
      </table>
     </td>
    </tr>

</table>


<table width="100%" cellpadding="0" cellspacing="0" border="0">
      <tr>
        <td background="<bean:write name="ctxtPath"/>/images/separator.gif" width="1">
            <image src="<bean:write name="ctxtPath"/>/images/separator.gif" width="1">
        </td>
            <td height="23">
              <table width="100%" border="0" cellspacing="0" cellpadding="0">
                <tr>
                  <td background="<bean:write name="ctxtPath"/>/images/tabback.gif">
                    <table border=0 cellpadding=0 cellspacing=0>
                      <tr>
                        <td> <img src="<bean:write name="ctxtPath"/>/images/customize.gif" width="121" height="24"></td>
                      </tr>
                    </table>
                  </td>
                </tr>
              </table>
              <table width="100%" border="0" cellspacing="0" cellpadding="5">
              	<tr>
              		<td colspan="5">
              			<div><b><font color="#7F1B00"><bean:message key="coiDisclosure.search.introduction" /></font></b></div>
              		</td>
              	</tr>
              	<tr>
              		<td colspan="5">&nbsp;
              	</tr>

              </table>
		<%--<logic:notPresent name="org.apache.struts.action.ERROR">
		<table width="100%" cellpadding="0" cellspacing="0" border="0">
			<tr>
				<td colspan="5">&nbsp;</td>
			</tr>
		  <tr>
		    <!-- <td width="1%">&nbsp;</td> -->
		    <td colspan="5"><font face="Verdana, Arial, Helvetica, sans-serif" size="1" color="red">
			&nbsp;<b> <bean:message key="coiDisclosure.label.searchresult1" />
			<bean:write name="allCOIDisclosuresSize"/> <bean:message key="coiDisclosure.label.searchresult2" />
			</b> </font>
		    </td>
		  </tr>
		</table>
		</logic:notPresent>
		--%>


            <!-- Coi disclosure search block begins -->

            <table width="100%" border="0" cellspacing="0" cellpadding="0">
              <tr height="30">
                <td width="1%" bgcolor="#FBF7F7">&nbsp;</td>
                <td bgcolor="#FBF7F7"><bean:message key="coiDisclosure.form.label.status" /></td>
                <td bgcolor="#FBF7F7"> : <html:select property="status" > <html:options collection="collCOIDisclosuresStatus" property="description"    labelProperty="code"  />
                  </html:select> </td>
                <td bgcolor="#FBF7F7"><bean:message key="coiDisclosure.form.label.applies" /></td>
                <td bgcolor="#FBF7F7"> :&nbsp;<html:select property="appliesTo" >
                            <html:options collection="optionsAppliesTo" property="description"
                                    labelProperty="code"/>
                        </html:select>
                </td>
                <td bgcolor="#FBF7F7">&nbsp;</td>
              </tr>
              <tr height="30">
                <td width="1%" bgcolor="#F7EEEE">&nbsp;</td>
                <td bgcolor="#F7EEEE"><bean:message key="coiDisclosure.form.label.award" /></td>
                <td bgcolor="#F7EEEE">: <html:text property="awardProposalNum" size="15" />
                </td>
                <%-- Checking whether the user has the privilege to do the person search--%>
                <priv:hasOSPRight name="hasOspRightToView" value="<%=Integer.parseInt(userprivilege)%>">
                <td bgcolor="#F7EEEE"><bean:message key="coiDisclosure.form.label.person" /></td>
                <td bgcolor="#F7EEEE"> :
                    <%--<logic:equal name="personInfo" property="ownInfo" value="true">--%>
                        <html:text property="personName" size='18'/>
                        <html:hidden property="personId" />
                    <%--</logic:equal>
                    <logic:notEqual name="personInfo" property="ownInfo" value="true">
                        <html:text name="personInfo" property="personID" size='18'/>
                        <html:hidden name="personInfo" property="fullName"/>
                    </logic:notEqual>--%>
<%--<html:text property="personId" size="15"  />
                  <html:hidden property="personName"  /> --%></td>
                <td bgcolor="#F7EEEE"><a href="javascript:openwintext('<bean:write name="ctxtPath"/>/coeusSearch.do?searchname=personSearch&fieldName=personId&reqPage=newdisc&reqType=1','person')">
                  <img src="<bean:write name="ctxtPath"/>/images/searchpage.gif"
                  width="22" height="20" border="0"></a>&nbsp; </td>
                </priv:hasOSPRight> </tr>
              <tr height="30">
                <td width="1%" bgcolor="#FBF7F7">&nbsp;</td>
                <td bgcolor="#FBF7F7"><bean:message key="coiDisclosure.form.label.type" /></td>
                <td bgcolor="#FBF7F7">: <html:select property="type" > <html:options collection="optionsType" property="description" labelProperty="code"/>
                  </html:select> </td>
                <td bgcolor="#FBF7F7">&nbsp;</td>
                <td colspan="2" align="right" bgcolor="#FBF7F7">&nbsp;</td>
              </tr>
              <tr height="30">
                <td width="1%">&nbsp;</td>
                <td colspan="3">&nbsp;</td>
                <td colspan="2" align="right"> <html:image page="/images/find.gif"   border="0" />
                  &nbsp;<a href="JavaScript:document.frmCOIDisclosure.reset();"><img src="<bean:write name="ctxtPath"/>/images/reset.gif" border="0"
                  height="22" ></a></td>
              </tr>
            </table>
            </html:form> <!-- Finished COI disclosure search block --> </td>
      </tr>
</table>
