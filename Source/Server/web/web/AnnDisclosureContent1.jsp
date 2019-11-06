<%--
/*
 * @(#)AnnDisclosureContent.jsp 1.0 2002/06/05
 *
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 *
 * @author  Phaneendra Kumar Bhyri.
 */
--%>

<%@ page import="java.util.Vector,
                 edu.mit.coeus.coi.bean.ComboBoxBean,
                 org.apache.struts.action.Action" 
	errorPage = "ErrorPage.jsp" %>

<%@ taglib  uri = '/WEB-INF/struts-template.tld'  prefix = 'template' %>
<%@ taglib  uri = "/WEB-INF/struts-bean.tld"      prefix="bean"       %>
<%@ taglib  uri = "/WEB-INF/struts-logic.tld"     prefix="logic"      %>
<%@ taglib  uri = "/WEB-INF/coeus-utils.tld"      prefix="coeusUtils" %>
<%@ taglib  uri = "/WEB-INF/struts-html.tld"      prefix="html"       %>
<%@ include file= "CoeusContextPath.jsp"  %>

<jsp:useBean id="loggedinpersonname" scope="session" class="java.lang.String" />
<jsp:useBean id="financialEntity" scope="request" class="edu.mit.coeus.coi.bean.EntityDetailsBean" />
<jsp:useBean id="allPendingDisclosures" scope="session" class="java.util.Vector" />
<bean:size id="totalPendingDisclosures" name="allPendingDisclosures" />
<jsp:useBean id="entityNumber" scope = "request" class = "java.lang.String" />

<%	System.out.println("Inside AnndisclosureContent.jsp");	%>

<table width="100%" border="0" cellpadding="0" cellspacing="0">
  <tr>

  <html:form name="frmAnnualDisclosures" type="edu.mit.coeus.action.coi.AnnualDisclosuresActionForm" 
  	action="/annualDisclosuresUpdate.do"  >
  <!-- CASE #404 End -->
    <td background="<bean:write name="ctxtPath"/>/images/separator.gif" width="1">
    <image src="<bean:write name="ctxtPath"/>/images/separator.gif" width="1">
    </td>
     <td width="645">
      <table width="100%" border="0" cellspacing="0" cellpadding="0">
            <tr>
            <td height="5"></td>
          </tr>      
          <tr bgcolor="#cccccc">
            <td class="header" height="23"> &nbsp;
              <!--<font face="Verdana, Arial, Helvetica, sans-serif" size="2">-->
              <b><bean:message key='annualDisclosure.header' />&nbsp; 
              <bean:write name ="loggedinpersonname" />
              &nbsp;for&nbsp;
              <bean:write name='financialEntity' property='name' /> 
              </b>
              <!--</font>-->

              </td>
          </tr>
          <tr>
            <td height="23">
	  <!-- Display any error messages existing in request scope -->
	  <!-- CASE #1393 Comment Begin -->
	  <%--
              <html:errors/>
           --%>
           <!-- CASE #1393 Comment End -->
                <table width="100%" border="0" cellspacing="0" cellpadding="5">
           <!-- CASE #1393 Begin -->
		<logic:present name="<%=Action.ERROR_KEY%>">
			<tr>
			<td>
			<b><bean:message key="validationErrorHeader"/></b>
			<bean:message key="validationErrorSubHeader1"/>
			<html:errors/>
			</font>
			</td>
			</tr>
		</logic:present>            
           <!-- CASE #1393 End -->                
                <logic:present name="errors"  scope="request" >
                <!-- CASE #406 Begin -->
			<tr>
			<td>
			<font face="Verdana, Arial, Helvetica, sans-serif" size="1" color="red"><b>
			<bean:message key="error.annDisclosure.final.update" />
			</b></font>
			</td>
			</tr>
                <!-- CASE #406 End -->
                   <tr>
                   <td>
                  <logic:iterate id="error" name="errors"  type ="edu.mit.coeus.coi.bean.AnnDisclosureErrorBean">
                  <!-- CASE #406 Begin -->
                    <logic:notEqual name ="error" property ="entityNumber" value ="1">
                  <!-- CASE #406 End -->
                        <!-- CASE #406 Comment Begin -->
                        <%--
                        	<logic:notEqual name ="error" property ="entityNumber" value ="0">
                            <font face="Verdana, Arial, Helvetica, sans-serif" size="2" color="red"><b>
                                AnnualDisclosure not complete &nbsp;&nbsp;<bean:write  name ="error" property ="entityNumber" />&nbsp;&nbsp;<bean:write  name ="error" property ="entityName" />
                            </b></font>
                        --%>
                        <!-- CASE #406 Comment End -->
                        <!-- CASE #406 Begin -->
                        	<ul>
                        	<font face="Verdana, Arial, Helvetica, sans-serif" size="1" color="red"><b>
                        	<li>
                        	<bean:write name="error" property="errorMessage" />
                        	</li>
                        	</b></font>
                        	</ul>
                        <!-- CASE #406 End -->
                    </logic:notEqual>
                  </logic:iterate>
                  <!-- CASE #406 Begin -->
                   </td>
                  </tr>
                  <!-- CASE #406 End -->
                </logic:present>
            </table>

              <!-- CASE #939 Comment Begin -->
              <%--
              <table width="100%" border="0" cellspacing="0" cellpadding="5">
                <tr>
                	<td>
                	<div>
                	<bean:message key="annualDisclosure.intro1" />
                	<bean:message key="annualDisclosure.intro2" />
                	<!-- CASE #399 Comment Begin -->
                	<%--
                	<bean:message key="annualDisclosure.intro3" />
                	--%>
                	<%--
                	<!-- CASE #399 Comment End -->
                	</div>
                	</td>
                </tr>
              </table>
              --%>
              <!-- CASE #939 Comment End -->
              <!-- CASE #939 Begin -->
              <table width="100%" border="0" cellspacing="0" cellpadding="5">
                	<logic:notPresent name="allEntitiesUpdated">
				<tr>
					<td>
					<font class="fontBrown"><b>
					<bean:message key="annualDisclosure.intro1" />&nbsp;
					<bean:write name='financialEntity' property='name' />.
					<bean:message key="annualDisclosure.intro2" />
					</b></font>
					</td>
				</tr>
			</logic:notPresent>
			<logic:present name="allEntitiesUpdated">
				<tr>
					<td height="3">&nbsp;</td>
				</tr>
				<tr>
					<td align = "center">
					<bean:message key="annualDisclosure.allEntitiesUpdated"/>
					<br>
				</tr>
				<tr>
					<td align = "center">
					<html:link href="AnnualDisclosureConfirmation.jsp" >
						<img src="<bean:write name="ctxtPath" />/images/ok.gif" border="0">
					</html:link>
					</td>
				</tr>
				<tr>
					<td height="1">&nbsp;</td>
				</tr>
			</logic:present>
                </tr>
		  <tr>
			<td height="5">&nbsp;</td>
		  </tr>              
                
              </table>
              <!-- CASE #939 End -->

              <!-- CASE #1374 Comment Begin -->
              <%-- 	
 	<table width="100%" border="0" cellspacing="0" cellpadding="0">
			<tr>
			  <td background="<bean:write name="ctxtPath" />/images/tabback.gif">
				<table border=0 cellpadding=0 cellspacing=0>
				  <tr>
					<td > <img src="<bean:write name="ctxtPath" />/images/finEntDetails.gif" width="144" height="24"></td>
				  </tr>
				</table>
			  </td>
			</tr>
		  </table>

              <!-- Display of financial entity information here...-->
              <table width="100%" border="0" cellspacing="0" cellpadding="5">
                <tr bgcolor="#FBF7F7">
                  <td width="50" height="25">
                    <div align="left"><bean:message key = 'annualDisclosure.name' /> </div>
                  </td>
                  <td width="5" height="25">
                    <div align="left">:</div>
                  </td>

                  <td width="180" height="25">
                    <div align="left"> <b><bean:write name='financialEntity' property='name' /> </b></div>
                    <html:hidden property ="name" />
                  </td>
                  <td width="162" height="25">
                    <div align="left"><bean:message key = 'annualDisclosure.Type' />&nbsp;</div>
                  </td>
                  <td width="5" height="25">
                    <div align="left">:</div>
                  </td>

                  <td width="252" height="25">
                    <div align="left"><b><bean:write name='financialEntity' property='type'/></b></div>
                    <html:hidden property ="type" />
                  </td>
                </tr>

                <tr bgcolor="#F7EEEE">
                  <td width="140" height="25">
                    <div align="left"><bean:message key = 'annualDisclosure.status' /></div>
                  </td>
                  <td width="5" height="25">
                    <div align="left">:</div>
                  </td>
                  <td width="84" height="25">
                  <div align="left"><b>

                  <bean:write name="financialEntity" property ="status" />
                   </b></div>
                  </td>

                  <td width="162" height="25">
                    <div align="left"><bean:message key = 'annualDisclosure.shareOwnership' /></div>
                  </td>
                  <td width="5" height="25">
                    <div align="left">:</div>
                  </td>
                  <td width="252" height="25">

                    <div align="left"><b>
                   <logic:present name="financialEntity" property="shareOwnship" >
                   <logic:notEqual name="financialEntity" property="shareOwnship"  value = ''>
                        <logic:equal name="financialEntity" property="shareOwnship"  value = 'V'>
                            <bean:message key="annualDisclosure.private"/>
                        </logic:equal>

                        <logic:equal name="financialEntity" property="shareOwnship"  value = 'P'>
                            <bean:message key="annualDisclosure.public"/>
                       </logic:equal>
                   </logic:notEqual>
                   <logic:equal name="financialEntity" property="shareOwnship"  value = ''>
                   &nbsp;
                   </logic:equal>
                   </logic:present>
                   <logic:notPresent name="financialEntity" property="shareOwnship">
                   &nbsp;
                   </logic:notPresent>
                   <html:hidden property="shareOwnship" />


                    </b></div>
                  </td>
                </tr>

              </table>
              <!-- Financial entity display ends here.............-->

              <table width = "100%">
                <tr bgcolor="#CCCCCC">
                  <td height="23" class="header">
                  <!--<font face="Verdana, Arial, Helvetica, sans-serif" size="2">-->
                  <b>
                    <bean:message key='annualDisclosure.FinEntityTitle' />
                    <bean:write name='financialEntity' property='name' />
                    </b>
                    <!--</font>-->
                    </td>
                </tr>
              </table>
              --%>
              <!-- CASE #1374 Comment End -->
	<!-- CASE #399 Comment Begin -->
	<%--
              <table width="100%" border="0" cellspacing="0" cellpadding="0">
                <tr bgcolor="#7F1B00">
                  <td height="20"><font face="Verdana, Arial, Helvetica, sans-serif" size="2" color="#FFFFFF"><b>&nbsp;&nbsp;<bean:message key = 'annualDisclosure.coiDisclosure' />&nbsp;<bean:write name='financialEntity' property='name' /></b></font></td>
                </tr>
              </table>
	--%>
	<!-- CASE #399 Comment End -->

            <!--  Display of the Pending Disclosures information for the selected Financial entity starts here.....-->

<!-- insert AnnDisclosureContentInsert.jsp -->
<jsp:include page="AnnDisclosureContentInsert.jsp" flush="true"/>
<!-- end insert -->


            </td>
          </tr>

      </table>
      </td>



   </html:form>
   </tr>
 </table>



