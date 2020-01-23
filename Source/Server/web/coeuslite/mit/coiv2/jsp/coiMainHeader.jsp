<%-- 
    Document   : coiMainHeader
    Created on : Mar 22, 2010, 1:12:06 PM
    Author     : Sony
--%>

<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/privilege.tld" prefix="priv"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ include file="/coeuslite/mit/utils/CoeusContextPath.jsp"%>
<jsp:useBean id="user" scope="session"
	class="edu.mit.coeus.bean.UserInfoBean" />
<jsp:useBean id="person" scope="session"
	class="edu.mit.coeus.bean.PersonInfoBean" />
<jsp:useBean id="userprivilege" class="java.lang.String" scope="session" />
<jsp:useBean id="loggedinUser" scope="session" class="java.lang.String" />




<jsp:useBean id="headerItemsVector" scope="session"
	class="java.util.Vector" />
<%
        java.lang.StringBuffer imagePathsSB = new java.lang.StringBuffer();
        imagePathsSB.append("MM_preloaduncimages(");
    %>

<%
    for (int i = 0;i<headerItemsVector.size();i++) {
        edu.mit.coeuslite.utils.bean.CoeusHeaderBean coeusHeaderBean = (edu.mit.coeuslite.utils.bean.CoeusHeaderBean)headerItemsVector.get(i);
        String path = coeusHeaderBean.getOnLoadImage();

        imagePathsSB.append(path);
        imagePathsSB.append(",");
        }
    %>

<%
    String imagePaths = imagePathsSB.substring(0,imagePathsSB.length()-1);
    imagePaths = imagePaths +")";
    %>


<a name="top"></a>

<table width="90%" border="0" cellpadding="0" cellspacing="0">
	<tr>
		<td>
			<table width="100%" border="0" cellpadding="0" cellspacing="0"
				STYLE="background-image:url('<bean:write name='ctxtPath'/>/coeusliteimages/header_background.gif');border:0">
				<tr>
					<td><img
						src="<bean:write name='ctxtPath'/>/coeusliteimages/header_coeus2.gif"
						width="675" height="42"></td>
					<td align="right" valign="top"><a href="javascript:;"
						onClick="MM_openBrWindow('web/clFeedback.jsp?module=IPF','','toolbar=no,width=500,height=325')"></td>
					<td cellpadding="2" align="right" class="copysmall">&nbsp;&nbsp;&nbsp;<span
						class="copysmallwhite"> <bean:message key="Header.user" />
							<%--User--%>: &nbsp;<%=loggedinUser%></td>
				</tr>
				<tr>
					<td height='20' colspan='3' align='right'><span
						class="copysmallwhite"> <html:link
								href="javaScript:getLockIds();">
								<font size='2' color='#D1E5FF'><u><bean:message
											key="locking.currentLockslabel" /></u></font>
							</html:link> &nbsp;&nbsp;&nbsp;
					</span></td>
				</tr>
			</table>
		</td>
	</tr>
	<tr>
		<td>
			<table width="100%" border="0" cellpadding="0" cellspacing="0">
				<tr>
					<td width="100%">
						<table bgcolor="#BACFE4" width="100%" height="22" border="0"
							cellpadding="0" cellspacing="1"
							STYLE="background-image:url('<bean:write name='ctxtPath'/>/coeusliteimages/nav_background.gif');border:0">
							<tr>
								<%
                                for (int i =0; i<headerItemsVector.size();  i++) {
                                    edu.mit.coeuslite.utils.bean.CoeusHeaderBean coeusHeaderBean = (edu.mit.coeuslite.utils.bean.CoeusHeaderBean)headerItemsVector.get(i);
                                    String path = coeusHeaderBean.getImagePath();
                                    String menuId = "image"+i;//coeusHeaderBean.getHeaderId();
                                    String onLoadImage=coeusHeaderBean.getOnLoadImage();
                                    String link = request.getContextPath()+coeusHeaderBean.getLink();
                                    String uri = request.getRequestURI();
                                    if (coeusHeaderBean.getLink().indexOf("coi") > -1) {
                            %>
								<td><html:link page='<%=link%>'>
										<img src="<bean:write name='ctxtPath'/><%=onLoadImage%>"
											name="<%=menuId%>" width="139" height="24" border="0"
											onMouseOut="MM_swapImgRestore()"
											onMouseOver="MM_swapImage('<%=menuId%>','','<bean:write name='ctxtPath'/><%=path%>',1)">
									</html:link></td>
								<%
                                } else {
                            %>
								<td><html:link page='<%=link%>'>
										<img src="<bean:write name='ctxtPath'/><%=path%>"
											name="<%=menuId%>" width="139" height="24" border="0"
											onMouseOut="MM_swapImgRestore()"
											onMouseOver="MM_swapImage('<%=menuId%>','','<bean:write name='ctxtPath'/><%=onLoadImage%>',1)">
									</html:link></td>
								<%
                                }
                            }
                                    String applicationName = request.getContextPath();
                                    //String logonLink = applicationName+"/logout.do";
                                    String logonLink = applicationName;
                                 %>
							</tr>
						</table>
					</td>
				</tr>






			</table>
		</td>
	</tr>
</table>

