<%--
/*
 * @(#)AnnDiscFEContent.jsp 1.0 2002/05/30
 *
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 *
 * @author  Phaneendra Kumar Bhyri.
 */
--%>

<%--
A component that displays all the Financial Entities not yet reviewed
--%>
<%@page errorPage = "ErrorPage.jsp" %>
<%@ taglib uri="/WEB-INF/struts-html.tld"     prefix="html" %>
<%@ taglib uri = '/WEB-INF/struts-template.tld'     prefix = 'template' %>
<%@ taglib uri="/WEB-INF/struts-bean.tld"     prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld"     prefix="logic" %>
<%@ taglib uri='/WEB-INF/response.tld'	 	  		prefix='res' %>
<%@ taglib uri='/WEB-INF/session.tld' 	 			prefix='sess' %>
<%@ include file="CoeusContextPath.jsp"  %>
<%@ taglib uri="/WEB-INF/coeus-utils.tld"	  prefix="coeusUtils" %>

<jsp:useBean id="loggedinpersonname" scope="session" class="java.lang.String" />

<!-- CASE #409 Begin -->
<jsp:useBean id="finEntities" scope="request" class="java.util.Vector" />
<bean:size id="totalFinEntities" name="finEntities" />
<!-- CASE #409 End -->
<!-- This finEntityIndex is used to select the pending financial entity for the disclosures info.
     This index should be cleared from session before entering into the AnnualDisclosures main page.
-->

<table width="100%" border="0" cellpadding="0" cellspacing="0">
<tr>
	<td background="<bean:write name="ctxtPath"/>/images/separator.gif" height="5" width="1">
        <image src="<bean:write name="ctxtPath"/>/images/separator.gif" height="5" width="1">
    </td>            
    <td height="5"></td>
 </tr>
  <tr>
    <td background="<bean:write name="ctxtPath"/>/images/separator.gif" width="1">
        <image src="<bean:write name="ctxtPath"/>/images/separator.gif" width="1">
    </td>
     <td width="645">
          <table width="100%" border="0" cellspacing="0" align="left" cellpadding="0">
          <tr bgcolor="#cccccc">
            <td height="23" class="header"> &nbsp;
            <bean:message key='annDisFinancialEntity.disclosuretitle' /> <bean:write name ="loggedinpersonname" />
           </td>
          </tr>
          <tr>
          	<td>
          		<table width="100%" cellspacing="0" cellpadding="5" border="0">
          		<tr>
          			<td colspan="3" height="5">&nbsp;</td>
          		</tr>
          		<tr>
				<td width="40%" align="left">
					&nbsp;&nbsp;<html:link page="/addFinEntity.do?actionFrom=AnnualFE">
					<img border="0" 
					src='<bean:write name="ctxtPath"/>/images/add_financial_entity.gif'>
					</html:link>
				</td>
				<td width="40% align="right">
					<a href="<bean:write name='ctxtPath'/>/annDiscCertification.do">
					<img border="0" 
					src='<bean:write name="ctxtPath"/>/images/proceed_to_annual.gif'>
					</a>
				</td>
				<td width="20%" align="right">
					<a href="javascript:openmediumwin('<%=request.getContextPath()%>/web/whatsAFinEnt.html','getFinEnt1');">
					<img border="0" 
					src='<bean:write name="ctxtPath"/>/images/help.gif'></a>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;		
				</td>          		
          		</tr>
          		<tr>
				<td colspan="3">
				<!-- CASE #1393 Comment Begin -->
				<%--
				<html:errors />
				--%>
				<!-- CASE #1393 Comment End -->
				
				
				<!-- CASE #912 Begin -->
				<!-- Different function used to check for active fin ent, for better performance -->
				<logic:equal name="hasActiveEntities" value="true">
					<font class="fontBrown">
					<b><bean:message key="annDisFinancialEntity.intro" /></b>
					</font>
					<%-- CASE #1374 Comment Begin 
					<a  href="whatsAFinEnt.html" 
					onClick="javascript:openwintext('<%=request.getContextPath()%>/web/whatsAFinEnt.html','');return false;">
					Additional information </a> regarding requirements for disclosing financial information.
					<br/>
					<bean:message key="annDisFinancialEntity.intro2"/>
					CASE #1374 Comment End --%>
				</logic:equal>
				<logic:notEqual name="hasActiveEntities" value="true">
					<font class="fontBrown">
					<b><bean:message key="annDisFinancialEntity.noFinEnt" /></b>
					</font>
					<%-- CASE #1374 Comment Begin 
					<br><br>
					<a href="COIInfo.html" 
					onClick="javascript:openwintext('<%=request.getContextPath()%>/web/COIInfo.html','');return false;">
					<bean:message key="annDisFinancialEntity.additionalInfo"/>
					</a> 
					<bean:message key="annDisFinancialEntity.additionalInfo2"/>
					<br><br>
					<bean:message key="annDisFinancialEntity.noFinEnt2" />
					CASE #1374 Comment End --%>

				</logic:notEqual>
				<!-- CASE #912 End -->
				
				</td>
			</tr>
			</table>
		</td>
          </tr>
          <tr>
            <td height="142">
             <table width="100%" border="0" cellspacing="0" cellpadding="0">
                <tr>
                  <td background="<bean:write name="ctxtPath"/>/images/tabback.gif">
                    <table border=0 cellpadding=0 cellspacing=0>
                      <tr>
                        <td><img src="<bean:write name="ctxtPath"/>/images/financialentity.gif" width="120" height="24"></td>
                      </tr>
                    </table>
                  </td>
                </tr>
              </table>
              <!-- CASE #939 Change to table width from 99% to 100% -->
              <table width="100%" border="0" cellspacing="1" cellpadding="0" align="right">

                <tr>
                  <td  height="5"></td>
                </tr>
                <tr bgcolor="#CC9999">
                <!--
                  <td width="23" height="25">
                    <div align="center">&nbsp;</div>
                  </td>
		-->
                  <td width="190" height="25">
                    <div align="center"><font color="#FFFFFF"><bean:message key="annDisFinancialEntity.entityName"/> </font></div>
                  </td>
                  <td width="74" height="25">
                    <div align="center"><font color="#FFFFFF"><bean:message key = "annDisFinancialEntity.statusCode" /> </font></div>
                  </td>
                  <td width="147" height="25">
                    <div align="center"><font color="#FFFFFF"><bean:message key = "annDisFinancialEntity.personRelationshipType" /> </font></div>
                  </td>
                  <td width="156" colspan="2">&nbsp;</td>
                </tr>

                <!--
                Get all the pending financial entities and iterate thru the collection and display the
                each entity information
                -->
                <!-- CASE #409 Comment Begin -->
                <%-- <logic:present name="allAnnualDiscEntities"  scope="request" >
                	<logic:iterate id="entity" name="allAnnualDiscEntities" >
                --%>
                <!-- CASE #409 Comment End -->
                <!-- CASE #409 Begin -->
                <logic:present name="finEntities"  scope="request" >
                    <logic:iterate id="entity" name="finEntities" >
                	<!-- CASE #409 End -->
                     <tr bgcolor="#FBF7F7">
                     <!-- CASE #1374 Comment Begin -->
                     <!-- Move edit link to far right column, and add make active link -->
                     <%--
                      <td width="23" height="25" bgcolor="#FBF7F7">

                        <!-- CASE #1374 Remove seq no from url-->
                        <a href="<bean:write name='ctxtPath'/>/editFinEntity.do?actionFrom=AnnualFE&entityNo=<bean:write name='entity' property='number'/>" >
                            <bean:message key ='annDisFinancialEntity.EditLink' />
                        </a>
                      </td>
                      --%>
                      <!-- CASE #1374 Comment End -->

                      <td height="25" bgcolor="#FBF7F7">&nbsp;&nbsp;

                        <!-- CASE #1374 Remove seq no from url-->
                        <a href="<bean:write name='ctxtPath'/>/viewFinEntity.do?actionFrom=AnnualFE&entityNo=<bean:write name='entity' property='number'/>" method="POST">
                           <bean:write name='entity' property='name' />
                       </a>
                      </td>

                      <td  height="25">
                        <div align="left">
                        <!--
                        if the financial entity status value is '1' display as Active
                        else display as InActive.
                        -->
                        <logic:present name="entity" property="status">
                            <logic:equal name="entity" property="status"  value = '1'>
                                <bean:message key="annDisFinancialEntity.active"/>
                            </logic:equal>


                            <logic:notEqual name="entity" property="status"  value = '1'>
                                <bean:message key="annDisFinancialEntity.inactive"/>
                           </logic:notEqual>
                        </logic:present>
                      </div>
                     </td>
                      <td height="25">
                        <div align="left"><bean:write name='entity' property='personReType' /></div>
                      </td>
			<td align="center">
				<font color="blue">[</font>
				<logic:equal name="entity" property="status"  value = '1'>
					<a href="<bean:write name="ctxtPath"/>/deactivateFinEnt.do?entityNo=<bean:write name='entity' property='number'/>&actionFrom=AnnualFE" method="POST">
						<bean:message key="financialEntity.removeLink"/>
					</a>
				</logic:equal>
				<logic:notEqual name="entity" property="status"  value = '1'>
					<a href="<bean:write name="ctxtPath"/>/activateFinEnt.do?entityNo=<bean:write name='entity' property='number'/>&actionFrom=AnnualFE" method="POST">
						<bean:message key="financialEntity.makeActiveLink"/>
					</a>
				</logic:notEqual>		
				<font color="blue">]</font>
			</td>
			<td>
				<font color="blue">[</font>
				<a href="<bean:write name="ctxtPath"/>/editFinEntity.do?entityNo=<bean:write name='entity' property='number'/>&actionFrom=AnnualFE" method="POST">
					<bean:message key="financialEntity.editLink"/>
				</a>
				<font color="blue">]&nbsp;</font>
			</td>                      
                    </tr>

                  </logic:iterate>
                </logic:present>
                
                <%-- CASE #1374 Comment Begin 
                <logic:equal name="totalFinEntities"  value="0" >
                    <tr>
                    <td colspan='5' height="2">	&nbsp;</td>
                   </tr>                
                    <tr>
                    <td colspan='5' align='center' >
                        <bean:message key="annDisFinancialEntity.noFinEnt3"/>  
                   </td>
                   </tr>
                </logic:equal>
                <tr>
                  <td colspan="5" height="34">
			<logic:equal name="hasActiveEntities" value="true">
			&nbsp;
			<bean:message key = "annDisFinancialEntity.AnnualDisclosureslink" />
			</logic:equal>
                  </td>
                </tr>
		CASE #1374 Comment End --%>
		</table>
		</td>
		</tr>
		<!-- CASE #1374 Begin -->
		<logic:greaterThan name="totalFinEntities" value="5">
			<tr>
			<td>
			<table width="100%" cellspacing="0" cellpadding="5">
				<tr>
					<td width="40%" align="left">
						&nbsp;&nbsp;<html:link page="/addFinEntity.do?actionFrom=coiFinEntity">
						<img border="0" 
						src='<bean:write name="ctxtPath"/>/images/add_financial_entity.gif'>
						</html:link>
					</td>
					<td width="40% align="right">
						<a href="<bean:write name='ctxtPath'/>/annDiscCertification.do">
						<img border="0" 
						src='<bean:write name="ctxtPath"/>/images/proceed_to_annual.gif'>
						</a>
					</td>
					<td width="20%" align="right">
						<a href="javascript:openwintext('<%=request.getContextPath()%>/web/whatsAFinEnt.html','getFinEnt1');">
						<img border="0" 
						src='<bean:write name="ctxtPath"/>/images/help.gif'></a>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;		
					</td>
				</tr>
			</table>
			</td>
			</tr>
		</logic:greaterThan>		
		<!-- CASE #1374 End -->	
          <tr>
            <td height="23">
              <div align="right"></div>
            </td>
          </tr>
        </table>
     </td>
   </tr>
 </table>
<!-- CASE #1374 Begin -->
<!-- Show response window to confirm ent added or modified -->
<% if(request.getAttribute("FESubmitSuccess") != null){
	request.removeAttribute("FESubmitSuccess");
	String message = (String)request.getAttribute("entityName");
	String actionType = (String)request.getAttribute("actionType");
	String reloadLocation = request.getContextPath()+"/getAnnDiscPendingFEs.do";
	if(actionType != null){
		if(actionType.equals("I")){
			message += " successfully added.";
		}
		else if(actionType.equals("U") ){
			message += " successfully modified.";
		}
		else if(actionType.equals("activate") ){
			message += " made active.";
		}
		else if(actionType.equals("deactivate") ){
			message += " made inactive.";
		}		
	}
	int indexOfApost = message.indexOf("'");
	if(indexOfApost != -1){
		int indexOfApostIncr = indexOfApost+1;
		int endOfMessage = message.length();
		String message1 = message.substring(0, indexOfApost);
		String message2 = message.substring(indexOfApostIncr, message.length());
		message = message1+message2;
	}	
	out.print("<script language='javascript'>");
	out.print("confirm('"+message+"');");
	out.print("window.location='"+reloadLocation+"';");
	out.print("</script>");
}

%>
<!-- CASE #1374 End -->