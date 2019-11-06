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
                java.net.URLEncoder" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" 	prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" 	prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" 	prefix="logic" %>
<%@ taglib uri="/WEB-INF/privilege.tld" prefix="priv" %>
<%@ taglib uri="/WEB-INF/coeus-utils.tld"	prefix="coeusUtils" %>
<%@ include file="CoeusContextPath.jsp"  %>
<jsp:useBean  id="action" 			scope="request" class="java.lang.String" />
<jsp:useBean  id="disclosureNo" 			scope="request" class="java.lang.String" />
<jsp:useBean  id="discHeaderAwPrInfo" 	scope="request" class="edu.mit.coeus.coi.bean.DisclosureHeaderBean" />
<jsp:useBean  id="collCOIDisclosureInfo"	scope="request" class="java.util.Vector" />
<jsp:useBean  id="collCOICertDetails" 			scope="request" class="java.util.Vector" />
<bean:size id="collCOICertDetailsSize"   name="collCOICertDetails" />
<bean:size id="collCOIDisclosureInfoSize"   name="collCOIDisclosureInfo" />
<jsp:useBean  id="moduleCode" 				scope="request" class="java.lang.String" />
<jsp:useBean  id="disclosureHeader" 		scope="request" class="edu.mit.coeus.coi.bean.DisclosureHeaderBean" />
<jsp:useBean  id="collCOIStatus" 		scope="request" class="java.util.LinkedList" />
<jsp:useBean id = "userprivilege" class = "java.lang.String" scope="session"/>


<table width="100%" cellpadding="0" cellspacing="0" border="0">

 <tr>
  <td background="<bean:write name="ctxtPath"/>/images/separator.gif" width="1">
    <image src="<bean:write name="ctxtPath"/>/images/separator.gif" width="1">
  </td>

  <td width="645">
  	<html:form action="/editCOIDisclosure.do" >
      <%-- show the information in hidden that are required to edit COI disclsoure --%>
      <html:hidden name="frmAddCOIDisclosure" property="disclosureNo" />
      <html:hidden name="frmAddCOIDisclosure" property="userName" />
       <html:hidden name="frmAddCOIDisclosure"  property="accountType" />
       <input type='hidden' name='hdnEditDiscl' value='yes'>
  	 <table width="100%" border="0" cellspacing="0" cellpadding="0">
          <tr><td height="5"></td></tr>
          <tr><td height="23" bgcolor="#cccccc"> &nbsp;
            	<b>
            	<font face="Verdana, Arial, Helvetica, sans-serif" size="2">
				<bean:message key="editCOIDisclosure.header"/>
				<bean:write name="disclosureNo" scope="request"/>
            	</font>
            	</b>
          </td></tr>

                  <%-- Show Errors --%>
                  <html:errors/>


          <tr><td height="23">
              <table width="100%" border="0" cellspacing="0" cellpadding="0">
                <tr><td height="40">
                    <div align="right">
				<a href="JavaScript:history.back();">
					<img src="<bean:write name="ctxtPath"/>/images/goback.gif" width="55" height="22" border="0">
				</a>&nbsp;
                        <html:image page="/images/submit.gif" border="0" />
                      &nbsp; &nbsp; &nbsp;
			  </div>
                </td></tr>
              </table>
              <table width="100%" border="0" cellspacing="0" cellpadding="0">
                <tr><td background="<bean:write name="ctxtPath"/>/images/tabback.gif">
                    <table border=0 cellpadding=0 cellspacing=0>
                      <tr><td>
				<img src="<bean:write name="ctxtPath"/>/images/disclosure.gif" width="120" height="24">
			    </td></tr>
                    </table>
                 </td></tr>
              </table>





              <table width="100%" border="0" cellspacing="0" cellpadding="5">

                <tr><td colspan="6" height="5"></td></tr>
                <tr>
			<td width="100" height="20" bgcolor="#FBF7F7" align="left" valign="center">
                    <div><bean:message key="editCOIDisclosure.label.personName"/></div>
                  </td>
                  <td width="6" height="20" bgcolor="#FBF7F7" valign="center">:</td>
                  <td width="176" height="20" bgcolor="#FBF7F7" align="left" valign="center">
                    <div><b><bean:write name="disclosureHeader" property="name"/></b></div>
                  </td>
                  <td width="123" height="20" bgcolor="#FBF7F7" align="left" valign="center">
                    <div><bean:message key="editCOIDisclosure.label.disclosureNum"/></div>
                  </td>
                  <td width="3" height="20" bgcolor="#FBF7F7">:</td>
                  <td width="238" height="20" bgcolor="#FBF7F7" align="left" valign="center">
                    <div><b><bean:write name="disclosureHeader" property="disclosureNo"/></b> </div>
                  </td>
                </tr>
                <tr bgcolor="#F7EEEE">
                  <td width="100" height="20" align="left" valign="center">
                    <div><bean:message key="editCOIDisclosure.label.appliesTo"/></div>
                  </td>
                  <td width="6" height="20">:</td>
                  <td width="176" height="20" align="left" valign="center">
                    <div><b><bean:write name="disclosureHeader" property="appliesTo"/>&nbsp;</b></div>
                  </td>
                  <td width="123" height="20" align="left" valign="center">
                    <div>
			<logic:equal name="moduleCode" value="2" >
				<bean:message key="editCOIDisclosure.label.proposalNo"/>
			</logic:equal>
			<logic:notEqual name="moduleCode" value="2">
				<bean:message key="editCOIDisclosure.label.awardNum"/>
			</logic:notEqual>
			  &nbsp;
			</div>
                  </td>
                  <td width="3" height="20">:</td>
                  <td width="238" height="20" align="left" valign="center">
                    <div><b><bean:write name="disclosureHeader" property="keyNumber"/></b> </div>
                  </td>
                </tr>
                <tr>
                  <td width="100" height="20" bgcolor="#FBF7F7" align="left" valign="center">
                   <div> <bean:message key="editCOIDisclosure.label.title"/>
                    &nbsp;</div>
                  </td>
                  <td height="20" bgcolor="#FBF7F7" width="6" valign="center">:</td>
                  <td height="20" colspan='4' bgcolor="#FBF7F7" width="176" align="left" valign="center">
                    <div><b><bean:write name="discHeaderAwPrInfo" property="title"/></b>
                    &nbsp;</div>
                  </td>
                </tr>
                <tr bgcolor="#F7EEEE">
                  <td width="100" height="20" align="left" valign="center">
                    <div><bean:message key="editCOIDisclosure.label.account"/></div>
                  </td>
                  <td width="6" height="20">:</td>
                  <td width="176" height="20" align="left" valign="center">
                    <div><b><bean:write name="discHeaderAwPrInfo" property="account"/></b>&nbsp;</div>
                  </td>
                  <td width="123" height="20" align="left">
                    <div><bean:message key="editCOIDisclosure.label.status"/></div>
                  </td>
                  <td width="3" height="20">:</td>
                  <td width="238" height="20" align="left">
                    <div><b><bean:write name="discHeaderAwPrInfo" property="status"/></b>&nbsp;</div>
                  </td>
                </tr>
                <tr>
                  <td width="100" height="20" bgcolor="#FBF7F7" align="left" valign="center">
                    <div><bean:message key="editCOIDisclosure.label.disclosureType"/></div>
                  </td>
                  <td width="6" height="20" bgcolor="#FBF7F7">:</td>
                  <td width="176" height="20" bgcolor="#FBF7F7">
                    <div><b>
                    <logic:equal name="action" value="edit" >
						<html:select  property='disclosureType'>
						   <html:option value='I'>Initial   </html:option>
						   <html:option value='A'>Annual</html:option>
						</html:select>
					</logic:equal>
                    </b> &nbsp;</div>
                  </td>
                  <td width="123" height="20" bgcolor="#FBF7F7">
                    <div><bean:message key="editCOIDisclosure.label.disclosureStatus"/></div>
                  </td>
                  <td width="3" height="20" bgcolor="#FBF7F7">:</td>
                  <td width="238" height="20" bgcolor="#FBF7F7">
                    <div><b><bean:write name="disclosureHeader" property="disclStatus"/></b>&nbsp;</div>
                  </td>
                </tr>
                <tr bgcolor="#F7EEEE">
                  <td width="100" height="20">
                    <div><bean:message key="editCOIDisclosure.label.reviewedBy"/>&nbsp;</div>
                  </td>
                  <td height="20" width="6">
                    :
                  </td>
                  <td height="20" width="176" bgcolor="#F7EEEE">
                    <div><b><bean:write name="disclosureHeader" property="reviewer"/></b>&nbsp;</div>
                  </td>
                  <td height="20" width="123">&nbsp;</td>
                  <td height="20" width="3">&nbsp;</td>
                  <td height="20" width="238" bgcolor="#F7EEEE">&nbsp;</td>
                </tr>

              </table>
              </div>
           </td>
           </tr>

          <tr><td height="23">&nbsp;</td></tr>
          <tr><td height="23">



              <table width="100%" border="0" cellspacing="0" cellpadding="0">
                <tr><td background="<bean:write name="ctxtPath"/>/images/tabback.gif">
                    <table border=0 cellpadding=0 cellspacing=0>
                      <tr><td>
				<img src="<bean:write name="ctxtPath"/>/images/certification.gif" width="120" height="24">
			    </td></tr>
                    </table>
                </td></tr>
              </table>



             <!-- show COI Dislcolsure Certification Details -->
		  <logic:notEqual name="collCOICertDetailsSize" value="0">

		    <table width="100%" border="0" cellspacing="1" cellpadding="5">
              <tr><td colspan="2" height="5"></td></tr>
                  <td width="450" height="25" bgcolor="#CC9999">
                    <div align="center">
				<font color="#FFFFFF">
					<bean:message key="editCOIDisclosure.label.question"/>
				</font>
			  </div>
                  </td>
                  <td width="128" height="25">
                    <div align="center">
				<font color="#FFFFFF">
					<bean:message key="editCOIDisclosure.label.answer"/>
				</font>
			  </div>
                  </td>
                </tr>

                <logic:iterate id="certificateDetails" name="collCOICertDetails" type="edu.mit.coeus.coi.bean.CertificateDetailsBean" >
				<%
			String certCode = certificateDetails.getCode();
            		String certDesc = certificateDetails.getQuestion();
	            	String numOfAns = certificateDetails.getNumOfAns();
	            	String answer = certificateDetails.getAnswer();
	            	%>
                <tr bgcolor="#FBF7F7">
                  <%--<td width="85" height="25">
                    <div align="center">
				<font face="Verdana, Arial, Helvetica, sans-serif">
					<a href="JavaScript:openwin('Question.jsp?QuestionNo=<%= certCode %>','question');"><%= certCode %></a>
				</font>
			  </div>
                  </td>--%>
                  <td width="450" height="25">
                    <INPUT type='hidden'  name='hdnQuestionID' value='<%= certCode %>'>
                    <INPUT type='hidden'  name='hdnQuestionDesc' value='<%= certDesc %>'>
                    <INPUT type='hidden' name='hdnLastUpdate' value='<%= certificateDetails.getLastUpdate() %>'>
                    <div align="justify">
                    	<bean:write name="certificateDetails" property="question" />
                    	<a href="JavaScript:openwin('Question.jsp?QuestionNo=<%= certCode %>','question');">
                    	<bean:message key="editCOIDisclosure.question.explanationLink" />
                    	</a>
                    </div>
                  </td>
                  <td width="135" height="25" valign="top">
                    <div align="justify">
				<font face="Verdana, Arial, Helvetica, sans-serif">
                      		<input type="radio" name="<%= certCode %>" <%= answer.equals("Y")?"checked":""  %> value="Y" >Yes
                      		<input type="radio" name="<%= certCode %>" <%= answer.equals("N")?"checked":""  %> value="N">No
					<% if(numOfAns!=null && Integer.parseInt(numOfAns)>2){ %>
                      			<input type="radio" name="<%= certCode %>" <%= answer.equals("X")?"checked":""  %> value="X">N/A <%}%>
                  	</font>
			  </div>
                 </td>
                </tr>
      		</logic:iterate>
	  		</logic:notEqual>
			<INPUT type='hidden' name='hdnNumOfQs' value='<%= collCOICertDetailsSize %>' >
			<logic:equal name="collCOICertDetailsSize" value="0">
		            <tr><td colspan='3' height="23"><bean:message key="editCOIDisclosure.label.noQuestions" /></td></tr>
			</logic:equal>
            </table>
              <!-- End of certificate detials -->

          </td></tr>

          <tr><td height="23">&nbsp;</td></tr>
          <tr><td height="23">
              <table width="100%" border="0" cellspacing="0" cellpadding="0">
                <tr><td background="<bean:write name="ctxtPath"/>/images/tabback.gif">
                    <table border=0 cellpadding=0 cellspacing=0>
                      <tr><td><img src="<bean:write name="ctxtPath"/>/images/finEntDisc.gif" width="167" height="24"></td>
                      </tr>
                    </table>
                </td></tr>
              </table>

              <!-- Show Disclosure Information -->
              <logic:notEqual name="collCOIDisclosureInfoSize" value="0" >
             <table width="100%" border="0" cellspacing="1" cellpadding="0" align="right">
                <tr><td colspan="5" height="5"></td></tr>
                <tr bgcolor="#CC9999">
                  <td width="113" height="25">
                    <div align="center"><font color="#FFFFFF"><bean:message key="editCOIDisclosure.label.entityName" /></font></div>
                  </td>
                  <td width="102" height="25">
                    <div align="center"><font color="#FFFFFF"><bean:message key="editCOIDisclosure.label.conflictStatus" /></font></div>
                  </td>
                  <td width="111" height="25">
                    <div align="center"><font color="#FFFFFF"><bean:message key="editCOIDisclosure.label.reviewedBy" /></font></div>
                  </td>
                  <td width="210" height="25">
                    <div align="center"><font color="#FFFFFF"><bean:message key="editCOIDisclosure.label.description" /></font></div>
                  </td>
                  <td width="25" height="25">&nbsp;
                    <%-- <div align="center"><font color="#FFFFFF"><bean:message key="editCOIDisclosure.label.lastUpdated" /></font></div> --%>
                  </td>
                </tr>
<%
    int disclIndex = 0;
%>
                <logic:iterate id="disclosureInfo" name="collCOIDisclosureInfo" type="edu.mit.coeus.coi.bean.DisclosureInfoBean" >
                <INPUT type='hidden' name='hdnEntityNum' value='<%= disclosureInfo.getEntityNumber() %>'>
                <INPUT type='hidden' name='hdnEntSeqNum' value='<%= disclosureInfo.getEntSeqNumber() %>'>
                <INPUT type='hidden' name='hdnSeqNum' value='<%= disclosureInfo.getSeqNumber() %>'>
                <tr bgcolor="#FBF7F7">
			<td width="113" height="25">
                  	<a href="<bean:write name='ctxtPath' />/viewDisclosureDetails.do?disclNo=<%=disclosureNo %>&amp;entNo=<bean:write name='disclosureInfo' property='entityNumber'  />" >
                  		<bean:write name='disclosureInfo' property='entityName'  />
                    	</a>
                  </td>
                  <td width="102" height="25">
                        <SELECT name='sltDiscStat<%=disclIndex%>'>
					<%
					    //LinkedList lstCOIStatus = objDisclosureDetailsBean.getAllCOIStatus();
					    String strConfStat = disclosureInfo.getConflictStatus();
					    int lstSize = collCOIStatus.size();
					    String strSelected = "";
					    for(int i=0;i<lstSize;i++){
				          ComboBoxBean objCombBean = (ComboBoxBean)collCOIStatus.get(i);
				          String code = objCombBean.getCode();
				          String description = objCombBean.getDescription();
                                  if(code.equalsIgnoreCase(strConfStat)) {
  				            strSelected = code.trim().equals(strConfStat.trim())?"selected":"";
                                  }else {
  				            strSelected = description.trim().equals(strConfStat.trim())?"selected":"";
                                  }
					%>
                        	<option <%= strSelected %> value="<%= code %>"><%= description %></option>
					<%
					    }
					%>
                        </SELECT>
                  </td>
                  <td width="111" height="25">
                    <div align="center"><bean:write name="disclosureInfo" property="reviewer"/></div>
                  </td>


                  <td width="210" height="25">
                  <%
                    String tmpDescr = "";
                    String disclDescription = (tmpDescr=disclosureInfo.getDesc())==null?"":tmpDescr;
                  %>

                    <INPUT type='text' size='25' name='description<%=disclIndex%>' value='<%= disclDescription %>'>

			<%--
                    <INPUT type='disabled' size='25' name='description<%=disclIndex%>' background="transparent"
                        onfocus='javascript:changeFocus(sltDiscStat<%=disclIndex%>);' value='<%= disclDescription %>'>
 			--%>

                  </td>
                  <td width="25" height="25">
                  <%
                   String encDescription= "";
                   encDescription = URLEncoder.encode(disclDescription);
                  %>

                    <a href='JavaScript:openwinDesc ("EditDisclosureDescription.jsp?desc=<%=encDescription%>&index=<%=disclIndex++%>","Description");'>
                    <img src="<bean:write name='ctxtPath'/>/images/more.gif" width="22" height="20" border="0">
                    </a>

			  <%--
                    document.Form1.description<%=disclIndex%>.value)
                    <a href="JavaScript:openwinDesc('EditDisclosureDescription.jsp?desc=<%=encDescription%>&index=<%=disclIndex++%>','Description');">
                    <img src="<bean:write name='ctxtPath'/>/images/more.gif" width="22" height="20" border="0">
                    </a>
			  --%>


                  </td>


                </tr>
			</logic:iterate>
              </table>

              </logic:notEqual>
              <!-- End of Disclosure Information -->
          </td></tr>

          <tr><td height="40">
              <div align="right">
                <a href="JavaScript:history.back();">
			<img src="<bean:write name="ctxtPath"/>/images/goback.gif" width="55" height="22" border="0">
		    </a>&nbsp;
                <html:image page="/images/submit.gif" border="0" />
                &nbsp; &nbsp; &nbsp; </div>
          </td></tr>
  	 </table>

        </html:form>

  </td>
 </tr>
</table>
