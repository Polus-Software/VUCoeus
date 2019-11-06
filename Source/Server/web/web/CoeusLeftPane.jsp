<%--
/*
 * @(#)CoeusLeftPane.jsp
 *
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 *
 * Created on May8, 2002, 11:45 AM
 *
 * @author  coeus-dev-team
 * @version 1.0
 */
--%>
<%--
Coeus application Navigation page which helps the user in working coeus application
and situated in Left hand side of a page, This page output is included in other view
component in runtime.

Note*. The Navigation options(links) will vary on the user role (previlege) basis.
--%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri= "/WEB-INF/struts-bean.tld"     prefix="bean" %>
<%@ taglib uri= "/WEB-INF/struts-logic.tld"     prefix="logic" %>
<%@ taglib uri="/WEB-INF/session.tld" prefix="sess" %>
<%@ include file="CoeusContextPath.jsp"  %>


<table width="100%" border="0" cellspacing="0" cellpadding="0" 
	background="<bean:write name="ctxtPath"/>/images/coeusmenu-03.gif"> 
<tr>
	<td height="2" >&nbsp;</td>
</tr>
<tr>
<td>
<table width="100%" border="0" cellspacing="2" cellpadding="2" 
	background="<bean:write name="ctxtPath"/>/images/coeusmenu-03.gif"> 
	<logic:present name="welcome" scope="request">
		<tr>
		    	<td>
				<html:link page="/getInboxMessages.do?messageType=unresolved">
					View Inbox
				</html:link>
			</td>
		</tr>
	</logic:present>
	<logic:notPresent name="welcome" scope="request">
		<tr>
			<td>
				<html:link page="/getInboxMessages.do?messageType=unresolved">
				    Unresolved Messages
				</html:link>
            		</td>
          	</tr>
          	<tr>
	  		<td>&nbsp;</td>
		  </tr>
		  <tr>
		    <td>
			<html:link page="/getInboxMessages.do?messageType=resolved">
				Resolved Messages
			</html:link>
		    </td>
		  </tr>
	</logic:notPresent>
          <tr>
          	<td>&nbsp;</td>
          </tr>
	<tr>
		<td>
			<a href="javascript:openSearchWin('<bean:write name='ctxtPath'/>/coeusSearch.do?searchname=proposalDevSearchNoRoles&fieldName=proposalNum','proposal');">
						
			                    Proposal Search
                  </a>
		</td>
	</tr>
	<tr>
		<td>&nbsp;</td>
          </tr>
          <tr>
            <td>
            	<html:link forward="changePassword">
            		Change Your Coeus Password
            	</html:link>
            </td>
          </tr>
          <tr>
	  	<td>&nbsp</td>
          </tr>
          <logic:present name="approvalRights"  scope="request" >
            <logic:iterate id="Rights"  name="approvalRights" type="edu.mit.coeus.propdev.bean.web.ApprovalRightsBean">
              <logic:equal name="Rights" property="viewRoutingRight" value="1">
              <tr>
                <td>
                 <a href="<bean:write name="ctxtPath"/>/MessageApprovalAction.do?action=maps&proposalNumber=<bean:write name="Rights" property="proposalNumber" />">
          		 <bean:message key="viewRouting.title"/> 
                </td>
              </tr>
              <tr>
	  	    <td>&nbsp</td>
              </tr>
              </logic:equal>
              <logic:equal name="Rights" property="approveRejectRight" value="1">
              <tr>
                <td>
                 <a href="<bean:write name="ctxtPath"/>/RefreshProposalAction.do?actionMode=Approve&proposalNumber=<bean:write name="Rights" property="proposalNumber" />">
          		 <bean:message key="approveProposal.label"/> 
                </td>
              </tr>
              <tr>
	  	    <td>&nbsp</td>
              </tr>
              <tr>
                <td>
                 <a href="<bean:write name="ctxtPath"/>/RefreshProposalAction.do?actionMode=Reject&proposalNumber=<bean:write name="Rights" property="proposalNumber" />">
          		 <bean:message key="rejectProposal.label"/> 
                </td>
              </tr>
              <tr>
	  	    <td>&nbsp</td>
              </tr>
              </logic:equal>
            </logic:iterate>
          </logic:present>
	<logic:present name="welcome" scope="request">
	<tr>
		<td>
			<html:link forward="loginCOI">
				Conflict of Interest Module
			</html:link>
		</td>
	</tr>
	<tr>
		<td>&nbsp;</td>
	</tr>
	<tr>
		<td>
			<html:link href="http://web.mit.edu/osp/www/index.html">
				More Coeus Information
			</html:link>
		</td>
	</tr>
      <tr>
	    <td>&nbsp</td>
      </tr>
	</logic:present>

          <tr>
            <td>
            	<html:link forward="welcome">
            		Coeus Home
            	</html:link>
            </td>
          </tr>
          <tr>
          	<td>&nbsp</td>
          </tr>

	<tr>
		<td height="30">&nbsp;</td>
	</tr>

</table>
</td>
</tr>
<tr valign="bottom">
<td>
<p><img src="<bean:write name="ctxtPath"/>/images/bottom.gif" width="129" height="5"></p>
</td>
</tr>
</table>   