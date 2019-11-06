<? xml version="1.0" ?>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ page
	import="org.apache.struts.taglib.html.JavascriptValidatorTag,
edu.mit.coeuslite.utils.CoeusLiteConstants,java.util.Vector,edu.mit.coeus.utils.ComboBoxBean"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<jsp:useBean id="VulnerableData" scope="session"
	class="java.util.Vector" />
<%@ include file="/coeuslite/mit/utils/CoeusContextPath.jsp"%>

<html:html>
<head>
<link
	href="<bean:write name='ctxtPath'/>/coeuslite/mit/utils/css/coeus_styles.css"
	rel="stylesheet" type="text/css">
<title>Coeus Web</title>
<script language="javaScript" type="text/JavaScript">
            var errValue = false;
            var errLock = false;
            
            function showDetails(){
                document.vulerable.action = "<%=request.getContextPath()%>/getSpecies.do?PAGE=P";
                 document.vulerable.submit();
            }
            
            function showHide(val){
                    if(val == 1){
                        document.getElementById('panel1').style.display = "block";
                    }else if(val == 2){
                        document.getElementById('panel2').style.display = "block";
                    }else if(val == 3){
                         document.getElementById('showSpPanel').style.display = "none";
                          document.getElementById('hideSpPanel').style.display = "block";
                        document.getElementById('speciesPanel').style.display = "block";
                        document.getElementById('speciesPanelSave').style.display = "block";
                    }else if(val == 4){
                         document.getElementById('showSpPanel').style.display = "block";
                          document.getElementById('hideSpPanel').style.display = "none";
                          document.getElementById('speciesPanel').style.display = "none";
                          document.getElementById('speciesPanelSave').style.display = "none";
                    }
            }   
            
        </script>
<style>
.textbox-longer {
	font-weight: normal;
	width: 500px
}
</style>
</head>
<body>
	<%--html:errors/--%>

	<% 
        Vector vctSpecies = new Vector();
        vctSpecies.add(new ComboBoxBean("1","Rat"));
        vctSpecies.add(new ComboBoxBean("2","Mouse"));
        vctSpecies.add(new ComboBoxBean("3","Rabbit"));
        vctSpecies.add(new ComboBoxBean("4","Dog"));
        vctSpecies.add(new ComboBoxBean("5","Sheep"));
        vctSpecies.add(new ComboBoxBean("6","Swine"));
        session.setAttribute("Species",vctSpecies);
        Vector vctProc = new Vector();
        vctProc.add(new ComboBoxBean("1","Analgesics"));
        vctProc.add(new ComboBoxBean("2","Anesthesia"));
        vctProc.add(new ComboBoxBean("3","Antibody production"));
        vctProc.add(new ComboBoxBean("4","Aversive stimuli"));
        vctProc.add(new ComboBoxBean("5","Breeding"));
        vctProc.add(new ComboBoxBean("6","Blood sampling"));
        vctProc.add(new ComboBoxBean("7","Dosing"));
        vctProc.add(new ComboBoxBean("8","Drug testing"));
        vctProc.add(new ComboBoxBean("9","Euthanasia"));
        vctProc.add(new ComboBoxBean("10","Footpad injections"));
        vctProc.add(new ComboBoxBean("11","Gavage"));
        session.setAttribute("Procedure",vctProc);
        Vector vctUSDA = new Vector();
        vctUSDA.add(new ComboBoxBean("A","A"));
        vctUSDA.add(new ComboBoxBean("B","B"));
        vctUSDA.add(new ComboBoxBean("C","C"));
        vctUSDA.add(new ComboBoxBean("D","D"));
        vctUSDA.add(new ComboBoxBean("E","E"));
        session.setAttribute("USDACategory",vctUSDA);
        String mode=(String)session.getAttribute(CoeusLiteConstants.MODE_DETAILS+session.getId());
        boolean modeValue=false;
        String EMPTY_STRING = "";
        if(mode.equalsIgnoreCase(CoeusLiteConstants.DISPLAY_MODE)){
            modeValue=true;
        }else if(mode.equalsIgnoreCase(CoeusLiteConstants.MODIFY_MODE)){
            modeValue=false;
        }else if(mode.equalsIgnoreCase(CoeusLiteConstants.ADD_MODE)){
            modeValue=false;
        }
        
        String subjectCountExists = (String)session.getAttribute("subjectCountExists");
        //System.out.println("Subject Count Flag >>>>>>>"+subjectCountExists);
        //Added for Coeus4.3 subject count enhancement - start
        String subjectCount = (String)session.getAttribute("subjectCount");
        //Added for Coeus4.3 subject count enhancement - end
        %>

	<%String protocolNo = (String)session.getAttribute(CoeusLiteConstants.IACUC_PROTOCOL_NUMBER+session.getId());
        
        if(protocolNo!= null){ 
        protocolNo = "["+"Protocol No. - "+ protocolNo+"]";
        }else{
        protocolNo = "";
        }
        //Added for coeus4.3 Amendments and Renewal enhancement
        String strProtocolNum = (String)session.getAttribute(CoeusLiteConstants.IACUC_PROTOCOL_NUMBER+session.getId());
        if(strProtocolNum == null)
        strProtocolNum = "";    
        String amendRenewPageMode = (String)session.getAttribute("amendRenewPageMode"+request.getSession().getId());
        if(strProtocolNum.length() > 10 && (amendRenewPageMode == null || 
        amendRenewPageMode.equalsIgnoreCase(CoeusLiteConstants.DISPLAY_MODE))){
        modeValue = true;
        }%>


	<html:form action="/vulerableSubjects.do" method="post"
		onsubmit="validateForm(this)">
		<a name="top"></a>

		<!-- New Template for cwVulnerableSubjects - Start   -->
		<script type="text/javascript">
    document.getElementById('txt').innerHTML = '<bean:message key="helpPageTextProtocol.Subjects"/>';
            </script>

		<table width="100%" height="100%" border="0" cellpadding="0"
			cellspacing="0" class="table">

			<tr>
				<td height="20" align="left" valign="top" class="theader">
					Exceptions</td>
			</tr>

			<tr class='copy' align="left">
				<td>&nbsp;</td>
			</tr>
			<tr nowrap class="copy" align="left">
				<td>
					<table width="100%" border="0" cellpadding="0" cellspacing="5"
						class="tabtable">

						<tr>
							<td nowrap class="copybold" align="left">Principles of
								Reduction:</td>
							<td class="copy" align="left"><html:textarea onblur=""
									property="protocolNumber" styleClass="textbox-longer"
									readonly="<%=modeValue%>" /></td>
						</tr>
						<tr>
							<td nowrap class="copybold" align="left">Principles of
								Refinement:</td>
							<td class="copy" align="left"><html:textarea onblur=""
									property="protocolNumber" styleClass="textbox-longer"
									readonly="<%=modeValue%>" /></td>
						</tr>
						<tr>
							<td nowrap class="copybold" align="left">Principles of
								Replacement:</td>
							<td class="copy" align="left"><html:textarea onblur=""
									property="protocolNumber" styleClass="textbox-longer"
									readonly="<%=modeValue%>" /></td>
						</tr>
						<tr>
							<td nowrap class="copybold" align="left">Exception:</td>
							<td class="copy"><input type="checkbox" checked></td>
						</tr>
					</table>
				</td>
			</tr>
			<!--  Vulnerable Subjects - Start  -->
			<tr>
				<td align="left" valign="top" class='core'>
					<table width="100%" border="0" align="center" cellpadding="0"
						cellspacing="0" class="tabtable">
						<tr>
							<td colspan="4" align="left" valign="top">
								<table width="100%" height="15" border="0" cellpadding="0"
									cellspacing="0" class="table">
									<tr>
										<td height="20" align="left" valign="top" class="theader">
											<div id='showSpPanel' class='theader'>
												<%String divlink = "javascript:showHide(3)";%>
												<html:link href="<%=divlink%>">
													<%String imagePlus=request.getContextPath()+"/coeusliteimages/plus.gif";%>
													<u>Add Exceptions</u>
												</html:link>
												&nbsp;
											</div>
											<div id='hideSpPanel' style='display: none;' class='theader'>
												<% divlink = "javascript:showHide(4)";%>
												<html:link href="<%=divlink%>">
													<%String imageMinus=request.getContextPath()+"/coeusliteimages/minus.gif";%>
													<u>Add Exceptions</u>
												</html:link>
											</div>
										</td>
									</tr>
								</table>
							</td>
						</tr>
						<tr>
							<td>
								<div id='speciesPanel' style='display: none;'>
									<table width="99%" border="0" cellpadding="0" cellspacing="5">

										<tr>
											<td width="20%" nowrap class="copybold" align="left">
												Exception Category:</td>

											<td nowrap class="copy"><html:select
													property="vulnerableSubjectTypeCode"
													styleClass="textbox-long" disabled="<%=modeValue%>">
													<html:option value="">
														<bean:message key="generalInfoLabel.pleaseSelect" />
													</html:option>
													<html:options collection="Species" property="code"
														labelProperty="description" />
												</html:select></td>
										</tr>
										<tr>
											<td width="20%" nowrap class="copybold" align="left">
												Description:</td>

											<td nowrap class="copy"><html:textarea onblur=""
													property="protocolNumber" styleClass="textbox-longer"
													cols="50" readonly="<%=modeValue%>" /></td>
										</tr>
										<tr>
											<td nowrap colspan="2">
												<div id='speciesPanelSave' style='display: none;'>
													<table width="99%" border="0" cellpadding="0"
														cellspacing="5">
														<tr class='tabtable'>
															<td width="15%" nowrap align="left"><html:button
																	property="Save" value="Save" styleClass="clsavebutton" />
															</td>
														</tr>
													</table>
												</div>
											</td>
											<td></td>
										</tr>
									</table>
								</div>
							</td>
						</tr>

					</table>
				</td>
			</tr>




			<!-- Add Vulnerable Subjects: - Start -->
			<tr>
				<td align="left" valign="top" class='core'><table width="100%"
						border="0" align="center" cellpadding="0" cellspacing="0"
						class="tabtable">

						<tr>
							<td height='10'>&nbsp;</td>
						</tr>
						<tr align="center">
							<td colspan="5">

								<table width="100%" border="0" cellpadding="0" class="tabtable">
									<tr>
										<td width="15%" align="left" class="theader">Exception
											Category</td>
										<td width="30%" align="left" class="theader">Description</td>

									</tr>
									<tr bgcolor="#D6DCE5" valign="top"
										onmouseover="className='TableItemOn'"
										onmouseout="className='TableItemOff'">
										<td align="left" nowrap class="copy">Dietary restrictions</td>
										<td width="30%" align="left" nowrap class="copy">Dietary
											restrictions</td>


									</tr>

									<tr>
										<td colspan="5" align="left" valign="top" class='core'></td>
									</tr>

									<tr bgcolor="#DCE5F1" valign="top"
										onmouseover="className='TableItemOn'"
										onmouseout="className='TableItemOff'">
										<td align="left" nowrap class="copy">Housing restriction
										</td>
										<td width="30%" align="left" nowrap class="copy">Housing
											restriction</td>

									</tr>

								</table> <%--}--%>

							</td>
						</tr>
						<tr>
							<td>&nbsp;</td>
						</tr>

					</table></td>
			</tr>


			<!-- Add Vulnerable Subjects: -End -->
			<tr>
				<td height='10'>&nbsp;</td>
			</tr>
		</table>
		<!-- New Template for cwVulnerableSubjects - End  -->
		<html:hidden property="acType" />
		<html:hidden property="protocolNumber" />
		<html:hidden property="sequenceNumber" />
		<html:hidden property="updateTimeStamp" />
		<%--<html:hidden property="count"/>--%>
	</html:form>
	<script>
          DATA_CHANGED = 'false';
          if(errValue && !errLock) {
                DATA_CHANGED = 'true';
          }
          document.vulerable.acType.value = 'I';
          LINK = "<%=request.getContextPath()%>/vulerableSubjects.do"; 
          FORM_LINK = document.vulerable;
          PAGE_NAME = "<bean:message key="vulnSubLabel.VulnerableSubjects"/>";
          function dataChanged(){
                DATA_CHANGED = 'true';
          }
          linkForward(errValue);
        </script>
	<script>
      var help = '<bean:message key="helpTextProtocol.Subjects"/>';
      help = trim(help);
      if(help.length == 0) {
            document.getElementById('helpText').style.display = 'none';
      }
      function trim(s) {
            return s.replace( /^\s*/, "" ).replace( /\s*$/, "" );
      }  
        </script>
</body>
</html:html>
