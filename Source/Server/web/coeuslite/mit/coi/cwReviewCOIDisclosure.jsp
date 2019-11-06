<%@ page
	import="org.apache.struts.validator.DynaValidatorForm,
java.util.Vector,edu.mit.coeuslite.utils.ComboBoxBean,org.apache.struts.taglib.html.JavascriptValidatorTag"%>
<%@ page import="java.util.*"%>

<%@ page contentType="text/html;charset=UTF-8" language="java"%>

<%@page errorPage="ErrorPage.jsp"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/coeus-utils.tld" prefix="coeusUtils"%>
<%@ include file="/coeuslite/mit/utils/CoeusContextPath.jsp"%>

<jsp:useBean id="reviewDisclosureList" scope="request"
	class="java.util.Vector" />
<jsp:useBean id="person" scope="session"
	class="edu.mit.coeus.bean.PersonInfoBean" />
<bean:size id="disclosureSize" name="reviewDisclosureList" />
<jsp:useBean id="disclosureStatus" scope="session"
	class="java.util.Vector" />



<html:html>
<head>
<title>Coeus Web</title>
<html:base />
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
<link
	href="<bean:write name='ctxtPath'/>/coeuslite/mit/utils/css/coeus_styles.css"
	rel="stylesheet" type="text/css">
<script language="javascript" type="text/JavaScript"
	src="<bean:write name='ctxtPath'/>/coeuslite/mit/utils/scripts/sorttable.js"></script>
<script language='JavaScript'>

             function open_person_search(link)
             {
                 validFlag = "proposal";    
                 var winleft = (screen.width - 650) / 2;
                 var winUp = (screen.height - 450) / 2;  
                 var win = "scrollbars=1,resizable=1,width=790,height=450,left="+winleft+",top="+winUp
                 sList = window.open('<bean:write name='ctxtPath'/>'+"/"+link, "list", win);
             }
     
             function fetch_Data(result)
             {       
                document.coiDisclosure.fullName.value = result["FULL_NAME"] ;         
             }

        </script>

<%String path = request.getContextPath();%>
<script src="<%=path%>/coeuslite/mit/utils/scripts/divSlide.js"></script>
<script src="<%=path%>/coeuslite/mit/utils/scripts/Balloon.js"></script>
<script>
        //var lastPopup;
        function showPopUp(linkId, frameId, width, height, src, bookmarkId, url) {
            showBalloon(linkId, frameId, width, height, src, bookmarkId);
            //showBalloonPopup(frameId);
        }
        
        var lastRowId = -1;
        var lastDataRowId;
        var lastDivId;
        var lastLinkId;
        var expandedRow = -1;

        //Variabled used to increase/decrease the height of the Iframe after expanded to set to proper height
        //depending on the content.
        var strIframeId;
        var strDivId;

        var defaultStyle = "rowLine";
        var selectStyle = "rowHover";
        
        function selectRow(rowId, dataRowId, divId, linkId, historyFrame, url) {
            var row = document.getElementById(rowId); 
            if(rowId == lastRowId) {
                //Same Row just Toggle
                var style = document.getElementById(dataRowId).style.display;
                if(style == "") {
                    style = "none";
                    styleClass = defaultStyle;
                    expandedRow = -1;
                    toggleText = plus.src;
                }else{
                    style = "";
                    styleClass = selectStyle;
                    expandedRow = rowId;
                    toggleText = minus.src;
                }
                //document.getElementById(dataRowId).style.display = style;
                //Sliding - START
                ds = new DivSlider();
                if(style == "") {
                    document.getElementById(dataRowId).style.display = "";
                    ds.showDiv(divId);
                }else {
                     ds.hideDiv(divId, "document.getElementById('"+dataRowId+"').style.display='none'");
                }
                //Sliding - END
                row.className = defaultStyle;
                var imgId = "img"+linkId;
                document.getElementById(imgId).src=toggleText;
            }else {
                document.getElementById(dataRowId).style.display = "";
                
                //Create IFrame 
                var divRef = document.getElementById(divId);
                //alert(divRef.hasChildNodes());
        	if(!divRef.hasChildNodes()) {
                    addFrame(divId, historyFrame, url);
                }
                strDivId = divId;
                strIframeId = historyFrame;
                historyFrame = document.getElementById(historyFrame);
                var frameSrc = historyFrame.getAttribute("src");
                if(frameSrc == null || frameSrc == "") {
                    historyFrame.setAttribute("src", url);
                }
                
                //Sliding - START
                ds2 = new DivSlider();
                ds2.showDiv(divId, "setHeight()");
                //Sliding - END

                row.className = defaultStyle;
                expandedRow = rowId;
                var imgId = "img"+linkId;
                document.getElementById(imgId).src=minus.src;
                
                //reset last selected row
                if(lastRowId != -1) {
                    row = document.getElementById(lastRowId);
                    //document.getElementById(lastDataRowId).style.display = "none";

                    ds3 = new DivSlider();
                    ds3.hideDiv(lastDivId, "document.getElementById('"+lastDataRowId+"').style.display='none'");

                    row.className = defaultStyle;
                    var imgId = "img"+lastLinkId;
                    document.getElementById(imgId).src=plus.src;
                }
            }

            lastRowId = rowId;
            lastDivId = divId;
            lastDataRowId = dataRowId;
            lastLinkId = linkId;
        }
        
        
        var plus = new Image();
        var minus = new Image();
        
        function preLoadImages() {
            plus.src = "<%=path%>/coeusliteimages/plus.gif";
            minus.src = "<%=path%>/coeusliteimages/minus.gif";
        }
        
        function addFrame(divId, historyFrame, url) {
            iframeElem = document.createElement("IFRAME");
            document.createAttribute("src");
            iframeElem.setAttribute("src", url);
            iframeElem.setAttribute("id", historyFrame);
            iframeElem.setAttribute("width", "100%");
            iframeElem.setAttribute("scrolling", "auto");
            iframeElem.setAttribute("marginHeight", "0");
            iframeElem.setAttribute("marginWidth", "0");
            iframeElem.setAttribute("frameBorder", "0");
            var divRef = document.getElementById(divId);
            divRef.appendChild(iframeElem);
        }
        
        /*
         * Called to set the height of the Iframe after expanded to set to proper height
         * depending on the content.
         */
        function setHeight(){
            var height = document.getElementById(strIframeId).contentWindow.document.body.scrollHeight + 10;
            if(height < 50) height = 50;
            document.getElementById(strIframeId).height = height;
            document.getElementById(strDivId).style.height = height;
        }

        preLoadImages();
        </script>

</head>
<body>
	<html:form action="/coidisclosure.do" method="POST">
		<a name="top"></a>

		<table width="980" border="0" cellpadding="0" cellspacing="0"
			class="table">
			<%--<logic:present name = "viewFinEntityData" scope="request">
        <logic:iterate id="viewdata" name="viewFinEntityData" type="org.apache.commons.beanutils.DynaBean">   --%>
			<tr class="tableheader">
				<td align="left" valign="top" class="theader"><bean:message
						bundle="coi" key="coiDisclosure.headerDiscl" /> <%=person.getFullName()%>
				</td>
			</tr>

			<tr>
				<td><logic:present name="customizedListIntroduction"
						scope="request">
						<tr>
							<td class="copy"><b><bean:message bundle="coi"
										key="coiDisclosure.introductionCustomizedList" />
									<html:link action="/getReviewDiscl.do">
										<u>
											<%--<bean:message bundle="coi" key="lable.reviewExistDisc"/><%--Review Existing Disclosures.--%>Review
											Existing Disclosures.
										</u>
									</html:link></b></td>
						</tr>
					</logic:present> <logic:notPresent name="customizedListIntroduction"
						scope="request">
						<tr>
							<td class="copy"><b><bean:message bundle="coi"
										key="coiDisclosure.headerData" /> </b></td>
						</tr>
					</logic:notPresent>
				<td class="copy" align="right"><html:link
						action="/midYearDisclosure.do">
						<u> <bean:message bundle="coi"
								key="coiDisclosure.midYearDiscl" />
						</u>
					</html:link></td>
				</td>
			</tr>
			<%
                    List awardDisclosure, proposalDisclosure;
                    awardDisclosure = new ArrayList();
                    proposalDisclosure = new ArrayList();
                    
                    if(reviewDisclosureList != null && reviewDisclosureList.size() > 0) {
                        org.apache.commons.beanutils.DynaBean tempBean = (org.apache.commons.beanutils.DynaBean)reviewDisclosureList.get(0);
                        String tempKey = (String)tempBean.get("module");
                        
                        String strAward = "Award";
                        for(int index = 0; index < reviewDisclosureList.size(); index++) {
                            tempBean = (org.apache.commons.beanutils.DynaBean)reviewDisclosureList.get(index);
                            tempKey = (String)tempBean.get("module");
                            if(tempKey != null && tempKey.equalsIgnoreCase(strAward)) {
                                //Award Disclosure
                                awardDisclosure.add(tempBean);
                            }//End IF
                            else {
                                //Proposal Disclosure
                                proposalDisclosure.add(tempBean);
                            }//End ELSE
                        }//End FOR
                        request.setAttribute("proposalDisclosure", proposalDisclosure);
                        request.setAttribute("awardDisclosure", awardDisclosure);
                        request.setAttribute("awardDiscSize", new Integer(awardDisclosure.size()));
                        request.setAttribute("proposalDiscSize", new Integer(proposalDisclosure.size()));
                    }
                %>

			<!-- Award Disclosure - START -->
			<tr>
				<td valign="top"><b>&nbsp;COI Disclosures for Award</b>
					<table width="99%" border="0" align="center" cellpadding="2"
						cellspacing="0" class="tabtable">

						<tr class="theader">
							<td width="2%" nowrap>&nbsp;</td>
							<td width="3%" nowrap>&nbsp;</td>
							<td width="10%" nowrap><bean:message bundle="coi"
									key="label.disclosureNumber" /></td>
							<td width="15%" nowrap><bean:message bundle="coi"
									key="label.status" /></td>
							<%--<td width="10%" nowrap><bean:message bundle="coi" key="label.appliesTo"/></td>--%>
							<td width="10%" nowrap><bean:message bundle="coi"
									key="label.awardKey" /></td>
							<td width="10%" nowrap><bean:message bundle="coi"
									key="label.sponsor" /></td>
							<td width="30%" nowrap><bean:message bundle="coi"
									key="label.title" /></td>
							<td width="20%" nowrap><bean:message bundle="coi"
									key="label.lastUpdated" /></td>
						</tr>

						<logic:notEqual name="awardDiscSize" value="0">


							<logic:present name="awardDisclosure">

								<%  int disclIndex = 0;
                                    String strBgColor = "#DCE5F1";
                                    
                                    %>
								<logic:iterate id="data" name="awardDisclosure"
									type="org.apache.commons.beanutils.DynaBean">
									<%                                  
                                        if (disclIndex%2 == 0) {
                                        strBgColor = "#D6DCE5";
                                        } else {
                                        strBgColor="#DCE5F1";
                                        }
                                        %>

									<tr bgcolor='<%=strBgColor%>' id="row<%=disclIndex%>"
										class="rowLine" onmouseover="className='rowHover rowLine'"
										onmouseout="className='rowLine'">

										<td class="copy">
											<%
                                                //String personId = person.getPersonID();
                                                String moduleCode = (String)data.get("moduleCode");
                                                //if(moduleCode == null || moduleCode.equals("")) {
                                                //    moduleCode = (String)data.get("MODULE_CODE");
                                                //}
                                                String moduleItemKey = (String)data.get("moduleItemKey");
                                                if(moduleItemKey == null || moduleItemKey.equals("")) {
                                                    moduleItemKey = (String)data.get("keyNumber");
                                                }
                                                String coiDisclosureNumber = (String)data.get("coiDisclosureNumber");
                                                String url = ""+path+"/getDisclosureHistory.do?moduleCode="+moduleCode+"&moduleItemKey="+moduleItemKey+"&disclosureNum="+coiDisclosureNumber;
                                                
                                                String onClick = "selectRow('row"+disclIndex+"', 'historyRow"+disclIndex+"', 'historyData"+disclIndex+"' , 'toggle"+disclIndex+"', 'historyFrame"+disclIndex+"', '"+url+"')";
                                                %> <script>
                                            var link = "javascript:<%=onClick%>";
                                            document.write("<a href=\""+link+"\" id=\"toggle<%=disclIndex%>\"> <img src='<%=request.getContextPath()%>/coeusliteimages/plus.gif' border='0' id='imgtoggle<%=disclIndex%>' name='imgtoggle<%=disclIndex%>'> </a>");
                                                </script>
											<noscript>
												<a target="_blank" href="<%=url%>">&gt;&gt;</a>
											</noscript>
										</td>

										<td class="copy"><html:link
												action="/viewCOIDisclosureDetails.do" paramName="data"
												paramProperty="coiDisclosureNumber" paramId="disclosureNo">
												<bean:message bundle="coi" key="label.view" />
											</html:link></td>
										<td class="copy"><coeusUtils:formatOutput name="data"
												property="coiDisclosureNumber" /></td>
										<td class="copy"><coeusUtils:formatOutput name="data"
												property="coiStatus" /></td>
										<%--<td class="copy">
                                                <coeusUtils:formatOutput name="data" property="module"/>
                                            </td>--%>
										<td class="copy"><coeusUtils:formatOutput name="data"
												property="moduleItemKey" /></td>

										<td class="copy"><coeusUtils:formatOutput name="data"
												property="sponsorName" /></td>
										<td class="copy"><html:link
												action="/viewCOIDisclosureDetails.do" paramName="data"
												paramProperty="coiDisclosureNumber" paramId="disclosureNo">
												<coeusUtils:formatOutput name="data" property="title" />
											</html:link></td>
										<td class="copy"><coeusUtils:formatDate name="data"
												formatString="MM-dd-yyyy  hh:mm a" property="updtimestamp" />
											<coeusUtils:formatOutput name="data" property="upduser" /></td>
									</tr>

									<!-- History Details - START-->
									<tr id="historyRow<%=disclIndex%>" style="display: none"
										class="copy rowHover">
										<td colspan="8">
											<!--NOTE: DO NOT PUT THE DIV END TAG IN NEXT LINE NOT PUT EVEN A SINGLE SPACE
                                                MOZILLA RETURNS TRUE FOR HASCHILDNODES EVEN IF A SPACE IS PRESENT B/N START
                                                AND END TAG :(
                                                -->
											<div id="historyData<%=disclIndex%>"
												style="overflow: hidden;"></div>
										</td>
									</tr>
									<!-- History Details - END -->

									<% disclIndex++ ;%>
								</logic:iterate>
							</logic:present>

							<logic:notPresent name="awardDisclosure">
								<tr>
									<td colspan="8">
										<table width="100%" align="center" border="0">
											<tr>
												<td class="copybold" align='left'><bean:message
														bundle="coi" key="coiDisclosure.NoAwardCOIDiscl" /></td>
											</tr>
										</table>
									</td>
								</tr>
							</logic:notPresent>

						</logic:notEqual>


						<logic:equal name="awardDiscSize" value="0">
							<tr>
								<td colspan="8">
									<table width="100%" align="center" border="0">
										<tr>
											<td class="copybold" align='left'><bean:message
													bundle="coi" key="coiDisclosure.NoAwardCOIDiscl" /></td>
										</tr>
									</table>
								</td>
							</tr>
						</logic:equal>


					</table></td>
			</tr>
			<!-- Award Disclosure - END -->

			<tr>
				<td>&nbsp;</td>
			</tr>

			<!-- Institute Proposal Disclosure - START -->
			<tr>
				<td valign="top"><b>&nbsp;COI Disclosures for Proposal</b>
					<table width="99%" border="0" align="center" cellpadding="2"
						cellspacing="0" class="tabtable">

						<tr class="theader">
							<td width="2%" align="left" nowrap>&nbsp;</td>
							<td width="3%" align="left" nowrap>&nbsp;</td>
							<td width="10%" align="left" nowrap><bean:message
									bundle="coi" key="label.disclosureNumber" /></td>
							<td width="15%" align="left" nowrap><bean:message
									bundle="coi" key="label.status" /></td>
							<%--<td width="13%" align="left" nowrap><bean:message bundle="coi" key="label.appliesTo"/></td>--%>
							<td width="10%" align="left" nowrap><bean:message
									bundle="coi" key="label.proposalKey" /></td>
							<td width="10%" align="left" nowrap><bean:message
									bundle="coi" key="label.sponsor" /></td>
							<td width="30%" align="left" nowrap><bean:message
									bundle="coi" key="label.title" /></td>
							<td width="20%" align="left" nowrap><bean:message
									bundle="coi" key="label.lastUpdated" /></td>
						</tr>

						<logic:notEqual name="proposalDiscSize" value="0">


							<logic:present name="proposalDisclosure">

								<%  int disclIndex = 0;
                                    String strBgColor = "#DCE5F1";
                                    String mod = "prop";
                                    %>
								<logic:iterate id="data" name="proposalDisclosure"
									type="org.apache.commons.beanutils.DynaBean">
									<%                                  
                                        if (disclIndex%2 == 0) {
                                        strBgColor = "#D6DCE5";
                                        } else {
                                        strBgColor="#DCE5F1";
                                        }
                                        %>

									<tr bgcolor='<%=strBgColor%>' id="row<%=mod+disclIndex%>"
										class="rowLine" onmouseover="className='rowHover rowLine'"
										onmouseout="className='rowLine'">
										<td class="copy">
											<%
                                                String personId = person.getPersonID();
                                                String moduleCode = (String)data.get("moduleCode");
                                                String moduleItemKey = (String)data.get("moduleItemKey");
                                                String coiDisclosureNumber = (String)data.get("coiDisclosureNumber");
                                                String url = ""+path+"/getDisclosureHistory.do?personId="+personId+"&moduleCode="+moduleCode+"&moduleItemKey="+moduleItemKey+"&disclosureNum="+coiDisclosureNumber;
                                                
                                                String onClick = "selectRow('row"+mod+disclIndex+"', 'historyRow"+mod+disclIndex+"', 'historyData"+mod+disclIndex+"' , 'toggle"+mod+disclIndex+"', 'historyFrame"+mod+disclIndex+"', '"+url+"')";
                                                %> <script>
                                            var link = "javascript:<%=onClick%>";
                                            document.write("<a href=\""+link+"\" id=\"toggle<%=mod+disclIndex%>\"> <img src='<%=request.getContextPath()%>/coeusliteimages/plus.gif' border='0' id='imgtoggle<%=mod+disclIndex%>' name='imgtoggle<%=mod+disclIndex%>'> </a>");
                                                </script>
											<noscript>
												<a target="_blank" href="<%=url%>">&gt;&gt;</a>
											</noscript>
										</td>

										<td class="copy"><html:link
												action="/viewCOIDisclosureDetails.do" paramName="data"
												paramProperty="coiDisclosureNumber" paramId="disclosureNo">
												<bean:message bundle="coi" key="label.view" />
											</html:link></td>
										<td class="copy"><coeusUtils:formatOutput name="data"
												property="coiDisclosureNumber" /></td>
										<td class="copy"><coeusUtils:formatOutput name="data"
												property="coiStatus" /></td>
										<%--<td class="copy">
                                                <coeusUtils:formatOutput name="data" property="module"/>
                                            </td>--%>
										<td class="copy"><coeusUtils:formatOutput name="data"
												property="moduleItemKey" /></td>

										<td class="copy"><coeusUtils:formatOutput name="data"
												property="sponsorName" /></td>
										<td class="copy"><html:link
												action="/viewCOIDisclosureDetails.do" paramName="data"
												paramProperty="coiDisclosureNumber" paramId="disclosureNo">
												<coeusUtils:formatOutput name="data" property="title" />
											</html:link></td>
										<td class="copy"><coeusUtils:formatDate name="data"
												formatString="MM-dd-yyyy  hh:mm a" property="updtimestamp" />
											<%--String updateTimestamp = (String)data.get("updtimestamp");
                                                edu.mit.coeus.utils.DateUtils dateUtils = new edu.mit.coeus.utils.DateUtils();
                                                out.println(dateUtils.formatDate(java.sql.T<coeusUtils:formatDate name="data" formatString="MM-dd-yyyy  hh:mm a" property="updtimestamp"/>imestamp.valueOf(updateTimestamp.toString()),"MM-dd-yyyy  hh:mm a"));
                                                --%> <coeusUtils:formatOutput
												name="data" property="upduser" /></td>
									</tr>

									<!-- History Details - START-->
									<tr id="historyRow<%=mod+disclIndex%>" style="display: none"
										class="copy rowHover">
										<td colspan="8">
											<!--NOTE: DO NOT PUT THE DIV END TAG IN NEXT LINE NOT PUT EVEN A SINGLE SPACE
                                                MOZILLA RETURNS TRUE FOR HASCHILDNODES EVEN IF A SPACE IS PRESENT B/N START
                                                AND END TAG :(
                                                -->
											<div id="historyData<%=mod+disclIndex%>"
												style="overflow: hidden;"></div>
										</td>
									</tr>
									<!-- History Details - END -->

									<% disclIndex++ ;%>
								</logic:iterate>
							</logic:present>

							<logic:notPresent name="proposalDisclosure">
								<tr>
									<td colspan="8">
										<table width="100%" align="center" border="0">
											<tr>
												<td class="copybold" align='left'><bean:message
														bundle="coi" key="coiDisclosure.NoProposalCOIDiscl" /></td>
											</tr>
										</table>
									</td>
								</tr>
							</logic:notPresent>

						</logic:notEqual>


						<logic:equal name="proposalDiscSize" value="0">
							<tr>
								<td colspan="8">
									<table width="100%" align="center" border="0">
										<tr>
											<td class="copybold" align='left'><bean:message
													bundle="coi" key="coiDisclosure.NoProposalCOIDiscl" /></td>
										</tr>
									</table>
								</td>
							</tr>
						</logic:equal>


					</table></td>
			</tr>
			<!-- Institute Proposal Disclosure - END -->



			<tr>
				<td height='10'>&nbsp;</td>
			</tr>
			<!-- Filter list - Start -->
			<tr>
				<td align="left" valign="top"><table width="99%" border="0"
						align="center" cellpadding="0" cellspacing="0" class="tabtable">
						<tr>
							<td colspan="4" align="left" valign="top"><table
									width="100%" height="20" border="0" cellpadding="0"
									cellspacing="0" class="tableheader">
									<tr>
										<td><bean:message bundle="coi"
												key="coiDisclosure.subheaderLabel" /></td>
									</tr>
								</table></td>
						</tr>

						<tr>
							<td class="copy">&nbsp;&nbsp;<b> <bean:message
										bundle="coi" key="coiDisclosure.subheaderData" />
							</b>
							</td>
						</tr>


						<tr>
							<td>&nbsp;</td>
						</tr>


						<%
                            //create a vector of comboboxbean instances to show Share owner ship options
                            Vector optionsAppliesTo = new Vector();
                            ComboBoxBean orgType = new ComboBoxBean();
                            orgType.setCode("");
                            orgType.setDescription("");
                            optionsAppliesTo.addElement(orgType);
                            orgType = new ComboBoxBean();
                            orgType.setCode("1");
                            orgType.setDescription("Award");
                            optionsAppliesTo.addElement(orgType);
                            orgType = new ComboBoxBean();
                            orgType.setCode("2");
                            orgType.setDescription("Institute Proposal");
                            optionsAppliesTo.addElement(orgType);
                            orgType = new ComboBoxBean();
                            orgType.setCode("ALL");
                            orgType.setDescription("ALL");
                            optionsAppliesTo.addElement(orgType);
                            pageContext.setAttribute("optionsAppliesTo",optionsAppliesTo);
                            %>

						<% 
                            Vector optionsType = new Vector();
                            ComboBoxBean cborgType = new ComboBoxBean();
                            cborgType.setCode("");
                            cborgType.setDescription("");
                            optionsType.addElement(cborgType);
                            cborgType = new ComboBoxBean();
                            cborgType.setCode("I");
                            cborgType.setDescription("Initial");
                            optionsType.addElement(cborgType);
                            cborgType = new ComboBoxBean();
                            cborgType.setCode("A");
                            cborgType.setDescription("Annual");
                            optionsType.addElement(cborgType);
                            cborgType = new ComboBoxBean();
                            cborgType.setCode("ALL");
                            cborgType.setDescription("ALL");
                            optionsType.addElement(cborgType);
                            pageContext.setAttribute("optionsType",optionsType);
                            
                            %>

						<tr>
							<td>

								<table width="90%" border="0" cellpadding="0">
									<tr>

										<td nowrap class="copybold" width="20%" align="left">&nbsp;&nbsp;
											<bean:message bundle="coi" key="label.status" />:
										</td>
										<td width="6"></td>

										<td class='copy' align="left">&nbsp;&nbsp; <html:select
												name="coiDisclosure" styleClass="textbox-long"
												property="status">
												<option value=""></option>
												<html:options collection="disclosureStatus" property="code"
													labelProperty="description" />
											</html:select>
										</td>

										<td>&nbsp; &nbsp;</td>

										<td nowrap class="copybold" width="15%" align="left">&nbsp;&nbsp;
											<bean:message bundle="coi" key="label.appliesTo" /> :
										</td>
										<td width="6" class="copy"></td>
										<td class="copy" colspan="3">&nbsp;&nbsp;<b> <html:select
													styleClass="textbox-long" property="moduleCode">
													<html:options collection="optionsAppliesTo" property="code"
														labelProperty="description" />
												</html:select>
										</b>
										</td>
									</tr>

									<tr>
										<td nowrap class="copybold" width="20%" align="left">&nbsp;&nbsp;
											<bean:message bundle="coi" key="label.awardProposalNumber" />:
										</td>
										<td width="6"></td>
										<td class="copy" colspan="2">&nbsp;&nbsp; <b> <!--input type="text" name="proposalNumber" size="15" value=""-->
												<html:text property="proposalNumber"
													styleClass="textbox-long" value="" />
										</b>
										</td>
									</tr>
									<tr>
										<td nowrap class="copybold" width="20%" align="left">&nbsp;&nbsp;
											<bean:message bundle="coi" key="label.type" />:
										</td>
										<td width="6"></td>
										<td class="copy" colspan="2">&nbsp;&nbsp; <b> <html:select
													styleClass="textbox-long" property="disclosureType">
													<html:options collection="optionsType" property="code"
														labelProperty="description" />
												</html:select>

										</b>
										</td>
										<%--
                                            <td nowrap class="copybold" width="15%"align="left"> &nbsp;&nbsp; Person :</td>
                                            <td width="6" class="copy"> </td>
                                            <td width='5%' class="copy" nowrap colspan="0"> &nbsp;&nbsp; <html:text  styleClass="textbox-long" property="fullName" value="" />  
                                                <a href="javaScript:open_person_search('/coiSearch.do?type=<bean:message key="searchWindow.title.person" bundle="coi"/>&search=true&searchName=ALL_PERSON_SEARCH');" 
                                                   class="copysmall"><u><bean:message bundle="coi" key="label.search"/></u></a>
                                            </td>
                                            --%>
									</tr>
								</table>
							</td>
						</tr>
						<tr>
							<td>&nbsp;</td>
						</tr>
						<tr>
							<td colspan="3" nowrap class="copy" align="left" width="50%">
								&nbsp;&nbsp; <html:submit property="save" value="Find"
									style="width:100px" />&nbsp; &nbsp; <html:reset
									onclick="JavaScript:document.coiDisclosure.reset();"
									style="width:100px" />
							</td>
						</tr>
						<tr>
							<td>&nbsp;</td>
						</tr>
					</table></td>
			</tr>
			<!-- Customize list -End -->
			<tr>
				<td colspan="3" nowrap class="copy" align="left" width="50%"><br>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
					<input type="button" value="My COI Home"
					onclick="javascript:window.location = '<%=path%>/coi.do'"
					style="width: 200px"> <br>
				<br></td>
			</tr>

		</table>

	</html:form>
</body>
</html:html>




