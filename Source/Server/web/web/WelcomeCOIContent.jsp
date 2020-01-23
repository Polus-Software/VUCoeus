<%--
/*
 * @(#)WelcomeCOI.jsp   1.0 2002/05/07  09:45:12AM
 *
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 *
 * @author  RaYaKu
 *
 */
--%>
<%-- Welcome page for COI module--%>

<%@ page  errorPage="ErrorPage.jsp"
        import="edu.mit.coeus.utils.CoeusConstants,
                edu.mit.coeus.action.common.CoeusActionBase,
                edu.mit.coeus.bean.PersonInfoBean"%>

<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/session.tld" prefix="sess" %>
<%@ taglib uri="/WEB-INF/privilege.tld" prefix="coeusPrivilege" %>

<%@ include file="CoeusContextPath.jsp"  %>

<%      System.out.println("inside WelcomeCOI.jsp");    %>

<coeusPrivilege:checkLogin forwardName="<%=CoeusConstants.LOGIN_COI_KEY%>" />

<!-- CASE #761 Begin -->
<jsp:useBean id="personInfo" scope ="session" class = "edu.mit.coeus.bean.PersonInfoBean" />
<jsp:useBean id="loggedinpersonid" scope="session" class="java.lang.String"/>
<!-- CASE #761 End -->

<table width="100%" border="0"   cellpadding="0" cellspacing="0" width="657" >
            <tr>
            <td height="5"></td>
          </tr>
</table>

<table border="0" cellspacing="0" cellpadding="0" width="657">
  <tr>
  
    <td >
      <table border="0" cellspacing="0" cellpadding="0" width="100%">
      
              <tr bgcolor="#CCCCCC">
                <td class="header" valign="center" height="25">
                &nbsp;&nbsp;Conflict of Interest Data for - 
                <bean:write name="personInfo" property="fullName"/>,
                  <bean:write name="personInfo" scope="session" property="dirTitle"/>
                  </td>
                  <!--<td class="header" valign="center">
                  <a href="Welcome.jsp">Coeus Home</a>&nbsp;&nbsp;
                  </td>-->
              </tr>      
      </table>
    </td>
	</tr>                
	 <tr>
	   <td>
            <table border="0" cellspacing="0" cellpadding="0">
              <tr>
                <td width="3%">&nbsp;</td>
                <td colspan="2">
                <br>
                <bean:message key="welcomeCOI.firstNote" />
                   <br>
                   <%--
                  <a class="smallsize" 
                  href="javascript:openwintext('http://web.mit.edu/osp/www/OSP_Booklet_2005/coi_guide.pdf','coi_guide');">
                  <bean:message key="welcomeCOI.instructionsLink"/> </a>
                  --%>
               </td>
              </tr>
                 <tr>
                 <td width="3%" height="25">&nbsp;</td>
                 <td width="75%" height="25">&nbsp;</td>
                 <td width="62%" height="25">&nbsp;</td>
                 </tr>
	    <tr>
	      <td >&nbsp;</td>
	      <td >
		<bean:message key="welcomeCOI.intro"/>
	      </td>
	      <td align="right">
			<%--<a class="smallsize" href="javascript:openwintext('<%=request.getContextPath()%>/web/COIInfo.html','welcomeCOI');">
			<bean:message key="welcomeCOI.moreDisclInfoLink"/>
			</a>--%>
	      </td>
	     </tr>
		<tr>
                 <td colspan="3" height="10">&nbsp;</td>
                 </tr> 
		<tr>
			<td >&nbsp;</td>
			<td>
			<ul>
			<li><b>
			<bean:message key="welcomeCOI.step1"/></b>
			<html:link page="/getFinEntities.do">
			<bean:message key="welcomeCOI.finEntLink"/>
			</html:link><br><br>
			&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<bean:message key="welcomeCOI.step1Text"/>
			</li>
			</ul>				
			</td>
			<td class="smallsize" align="right" valign="top">
			<a href="javascript:openmediumwin('<%=request.getContextPath()%>/web/whatsAFinEnt.html','welcomeCOI2');">
			<bean:message key="welcomeCOI.whatsAFinEnt"/>
			</a>
			</td>
		</tr>
		<tr>
		<td colspan="3" height="2">&nbsp;</td>
		</tr>
                    <tr>
                      <td >&nbsp;</td>                                                                                        
                      <td>
                        <ul>
                        <li><b>
                        <bean:message key="welcomeCOI.step2"/></b> 
                        <html:link page="/addCOIDisclosure.do">
                        <bean:message key="welcomeCOI.coiLink"/>
                        </html:link>
                        <bean:message key="welcomeCOI.coiLink2"/>
                        </li>
                        </ul>
                       </td>
                       <td>&nbsp;</td>
                    </tr>
                 <tr>
                 <td colspan="3" height="30">&nbsp;</td>
                 </tr>
<jsp:include page="WelcomeCOIInsert.jsp" flush="true"/>
		 <tr>
		 <td colspan="3" height="40">&nbsp;</td>
		 </tr> 
            </table>
            </td>
            </tr>
            <tr>
            <td>
                <img src="<bean:write name="ctxtPath"/>/images/welcome1-04.gif" width="657" height="8" border="0">
             </td>
        </tr>
</table>
