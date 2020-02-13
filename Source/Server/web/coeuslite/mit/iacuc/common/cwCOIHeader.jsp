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

<html:html locale="true">
<head>
<title>CoeusWeb</title>
<html:base />

<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
<script language="javascript" type="text/JavaScript" src="_js.js"></script>
<script
	src="<%=request.getContextPath()%>/coeuslite/mit/utils/scripts/modal/modaldbox.js"></script>
<link rel="stylesheet"
	href="<%=request.getContextPath()%>/coeuslite/mit/utils/scripts/modal/modaldbox.css"
	type="text/css" />
<script language="JavaScript" type="text/JavaScript">
<!--
function MM_swapImgRestore() { //v3.0
  var i,x,a=document.MM_sr; for(i=0;a&&i<a.length&&(x=a[i])&&x.oSrc;i++) x.src=x.oSrc;
}

function MM_preloadImages() { //v3.0
  var d=document; if(d.images){ if(!d.MM_p) d.MM_p=new Array();
    var i,j=d.MM_p.length,a=MM_preloadImages.arguments; for(i=0; i<a.length; i++)
    if (a[i].indexOf("#")!=0){ d.MM_p[j]=new Image; d.MM_p[j++].src=a[i];}}
}

function MM_findObj(n, d) { //v4.01
  var p,i,x;  if(!d) d=document; if((p=n.indexOf("?"))>0&&parent.frames.length) {
    d=parent.frames[n.substring(p+1)].document; n=n.substring(0,p);}
  if(!(x=d[n])&&d.all) x=d.all[n]; for (i=0;!x&&i<d.forms.length;i++) x=d.forms[i][n];
  for(i=0;!x&&d.layers&&i<d.layers.length;i++) x=MM_findObj(n,d.layers[i].document);
  if(!x && d.getElementById) x=d.getElementById(n); return x;
}

function MM_swapImage() { //v3.0
  var i,j=0,x,a=MM_swapImage.arguments; document.MM_sr=new Array; for(i=0;i<(a.length-2);i+=3)
   if ((x=MM_findObj(a[i]))!=null){document.MM_sr[j++]=x; if(!x.oSrc) x.oSrc=x.src; x.src=a[i+2];}
}
//-->


function open_protocol_search(link)
{
   
    var winleft = (screen.width - 650) / 2;
    var winUp = (screen.height - 450) / 2;  
    var win = "scrollbars=1,resizable=1,width=900,height=450,left="+winleft+",top="+winUp
    //var win = "toolbar=0,scrollbars=1,statusbar=0,menubar=0,resizable=1,width=650,height=400,left="+winleft+",top="+winUp
    sList = window.open('<%=request.getContextPath()%>'+"/"+link, "list", win);
               
}

function getLockIds() {            
            var winleft = (screen.width - 650) / 2;
            var winUp = (screen.height - 450) / 2;  
            var win = "scrollbars=1,resizable=1,width=770,height=450,left="+winleft+",top="+winUp                        
            sList = window.open("<%=request.getContextPath()%>//getLockIdsList.do", "list", win);           
    }

function showDialog(divId){
    width = document.getElementById(divId).style.pixelWidth;
    height = document.getElementById(divId).style.pixelHeight;
    sm(divId,width,height);
}
</script>
</head>
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
<body>
	<a name="top"></a>

	<table width="90%" border="0" cellpadding="0" cellspacing="0">
		<tr>
			<td>
				<!-- JM 5-31-2011 updated per 4.4.2 -->
				<table width="100%" border="0" cellpadding="0" cellspacing="0"
					STYLE="background-color: black; border: 0">
					<tr>
						<!-- JM 5-31-2011 updated per 4.4.2 -->
						<td><img
							src="<bean:write name='ctxtPath'/>/coeusliteimages/header_coeus2.gif"
							width="675" height="68"></td>
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
									<!-- JM 5-31-2011 updated per 4.4.2 -->
									<font size='2' color='#FFFFFF'><u><bean:message
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
                                    String headerId = coeusHeaderBean.getHeaderId();
                                    String menuId = "image"+i;//coeusHeaderBean.getHeaderId();
                                    String onLoadImage=coeusHeaderBean.getOnLoadImage();
                                    String link = request.getContextPath()+coeusHeaderBean.getLink();
                                    String uri = request.getRequestURI();
                                    if (coeusHeaderBean.getLink().indexOf("coi") > -1) {
                            %>
									<td><html:link page='<%=link%>'>
											<img src="<bean:write name='ctxtPath'/><%=onLoadImage%>"
												name="<%=menuId%>" width="98" height="24" border="0"
												onMouseOut="MM_swapImgRestore()"
												onMouseOver="MM_swapImage('<%=menuId%>','','<bean:write name='ctxtPath'/><%=path%>',1)">
										</html:link></td>
									<%
                                } else if("008".equals(headerId)) {
                            %>
									<td><html:link page='<%=link%>'>
											<img src="<bean:write name='ctxtPath'/><%=path%>"
												name="<%=menuId%>" width="112" height="24" border="0"
												onMouseOut="MM_swapImgRestore()"
												onMouseOver="MM_swapImage('<%=menuId%>','','<bean:write name='ctxtPath'/><%=onLoadImage%>',1)">
										</html:link></td>
									<%
                                } else {
                            %>
									<td><html:link page='<%=link%>'>
											<img src="<bean:write name='ctxtPath'/><%=path%>"
												name="<%=menuId%>" width="98" height="24" border="0"
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
					<tr height="100%">
						<td>
							<table width="100%" border="0" class="tablemain">
								<tr>
									<td align="center" styleClass="copysmall" nowrap><a
										class="menu"
										href="http://coeus.mit.edu/training/COI_Screens_QC.pdf"
										target="_blank"
										title="How to complete a Proposal-Specific Disclosure (PDF)">How
											to complete a Proposal Disclosure</a></td>
									<td align="center" styleClass="copysmall" nowrap
										style="border-left-style: solid; border-left-color: gray; border-left-width: 1px">
										<a class="menu"
										href="http://coeus.mit.edu/training/COI_Annual_Userguide.pdf"
										target="_blank"
										title="How to Complete an Annual Disclosure (Microsoft Word)">How
											to Complete an Annual Disclosure</a>
									</td>
									<td align="center" styleClass="copysmall" nowrap
										style="border-left-style: solid; border-left-color: gray; border-left-width: 1px">
										<a class="menu" id="linkMitCOI"
										href="javascript:showDialog('mitCOI')"
										title="MIT Conflict of Interest Policies">MIT COI Policies</a>
									</td>

									<%-- 
                                     IF Any of these
                                         1 VIEW_CONFLICT_OF_INTEREST,
                                         2.MAINTAIN_CONFLICT_OF_INTEREST
                                         rights are present then the ***SELECT PERSON*** Link is visible
                                     IF Any of these
                                        1.APPROVE_PROPOSAL
                                        2.VIEW_ALL_PENDING_DISCLOSURES
                                        rights are present then the ****View Pending Disclosures**** is Visible                                        
                                 --%>
									<jsp:useBean id="headerVector" scope="application"
										class="java.util.Vector" />
									<% 
                                     for (int index = 0;index<headerVector.size();index++) {
                                         edu.mit.coeuslite.utils.bean.SubHeaderBean subHeaderBean =(edu.mit.coeuslite.utils.bean.SubHeaderBean)headerVector.elementAt(index);
                                         String headerName = subHeaderBean.getSubHeaderName();
                                         String headerLink = subHeaderBean.getSubHeaderLink();
                                         String personLink = subHeaderBean.getProtocolSearchLink();
                                         String subHeaderId = subHeaderBean.getSubHeaderId();
                                         boolean currentPage = false;
                                         if(request.getRequestURL().indexOf(headerLink) != -1){
                                             currentPage = true;
                                             
                                         }%>
									<td align="center" styleClass="copysmall" nowrap
										style="border-left-style: solid; border-left-color: gray; border-left-width: 1px">
										<%if (headerLink == null || headerLink.equals("EMPTY")) {%> <font
										face='Arial, Helvetica, sans-serif' size='1px' color='blue'>
											<%=headerName%>
									</font> <%}else if(personLink.equals("false")){%> <% if (headerLink.equalsIgnoreCase("/viewPendingDisc.do")){ %>
										<logic:present name="viewPendingDisc" scope="session">
											<html:link page="<%=headerLink%>" styleClass="menu"><%=headerName%>
											</html:link>
										</logic:present> <%}
                                                 //#3202 - Begin. Added by Geo
                                                else if (headerLink.equalsIgnoreCase("/getReviewAnnualDiscl.do")|| headerLink.equalsIgnoreCase("/getApproveDiscl.do") || headerLink.equalsIgnoreCase("/getWip.do")){ %>
										<logic:notEqual name="userprivilege" value="">
											<priv:hasOSPRight name="hasosprighttoedit"
												value="<%=Integer.parseInt(userprivilege)%>">
												<html:link page="<%=headerLink%>" styleClass="menu"><%=headerName%>
												</html:link>
											</priv:hasOSPRight>
										</logic:notEqual> <%}//#3202 - end
                                                else{%> <html:link
											page="<%=headerLink%>" styleClass="menu"><%=headerName%>
										</html:link> <%}
                                                }else if(personLink.equals("true")){%>
										<logic:notEqual name="userprivilege" value="">
											<priv:hasOSPRight name="hasOspRightToView"
												value="<%=Integer.parseInt(userprivilege)%>">
												<a
													href="javaScript:open_protocol_search('<%=headerLink%>');"
													class="menu"><%=headerName%></a>
											</priv:hasOSPRight>
										</logic:notEqual> <%}%>
									</td>
									<%}%>

								</tr>
							</table>
						</td>
					</tr>
				</table>
			</td>
		</tr>
	</table>
	<div id="mitCOI" class="dialog" style="width: 500px; height: 350px">
		<div style="overflow: auto; width: 500px; height: 350px">
			<table border="0" cellpadding="2" cellspacing="0"
				class="lineBorderWhiteBackgrnd">
				<tr>
					<td colspan="2">
						<p>
							Investigators should familiarize themselves with the following
							sections of the MIT Policies and Procedures <a
								href="http://web.mit.edu/policies/" target="_blank">http://web.mit.edu/policies/</a>
							that pertain to potential conflict of interest in research.
						</p>
						<p>MIT Policies and Procedures</p>
						<dl>
							<dt>4.0 Faculty Rights and Responsibilities</dt>
							<dd>
								4.4 Conflict of Interests <a
									href="http://web.mit.edu/policies/4.4.html" target="_blank">http://web.mit.edu/policies/4.4.html</a><br>
								4.5 Outside Professional Activities <a
									href="http://web.mit.edu/policies/4.5.html" target="_blank">http://web.mit.edu/policies/4.5.html</a>
							</dd>
							<dt>12.0 Relations with the Public, Use of MIT Name, and
								Facilities Use</dt>
							<dd>
								12.3 Use of Institute Name <a
									href="http://web.mit.edu/policies/12.3.html" target="_blank">http://web.mit.edu/policies/12.3.html</a><br>
								12.4 Use of Institute Letterhead <a
									href="http://web.mit.edu/policies/12.4.html" target="_blank">http://web.mit.edu/policies/12.4.html</a>
							</dd>
							<dt>13.0 Information Policies</dt>
							<dd>
								13.1 Intellectual Property <a
									href="http://web.mit.edu/policies/13.1.html" target="_blank">http://web.mit.edu/policies/13.1.html</a><br>
								13.1.1 Ownership of Intellectual Property <br> 13.1.2
								Significant Use of MIT - Administered Resources<br> 13.1.3
								Ownership of Copyrights in Theses<br> 13.1.4 Invention and
								Proprietary Information Agreements<br> 13.1.5 Consulting
								Agreements<br> 13.1.7 Disclosures and Technology Transfer
							</dd>
						</dl>

					</td>
				</tr>
			</table>
		</div>
		<p>
			<input type="button" onclick="javascript:hm('mitCOI')" value="Close">
		</p>
	</div>
</body>
</html:html>
