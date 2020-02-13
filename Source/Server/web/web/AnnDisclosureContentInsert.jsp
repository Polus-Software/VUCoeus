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
                 edu.mit.coeus.coi.bean.ComboBoxBean"
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
<jsp:useBean id = "errors" scope = "request" class = "java.util.Vector" />
<bean:size id="totalErrors" name="errors" />
<!-- CASE #1046 Begin -->
<jsp:useBean id="collCOIStatus" scope="session" class="java.util.LinkedList" />
<!-- CASE #1046 End -->

            <table width="100%" cellspacing="0" cellpadding="5" border="0" >
                <tr bgcolor="#F7EEEE">
                <!-- CASE #1374 Comment Begin -->
                <%--
                  <td width="120" height="25">
                    <div align="left">&nbsp;<bean:message key = 'annualDisclosure.selectAll' /> </div>
                  </td>
                  <td width="5" height="25">
                    <div align="left">:</div>
                  </td>
                  <td width="50" height="25">
                    <div align="left">
                      <!-- CASE #819 Comment Begin -->
                      <%--
                      <input type="checkbox" name="chkAllDisclosures" value="" onClick="checkAll(<%=totalPendingDisclosures%>)">
                      --%>
                      <%--
                      <!-- CASE #819 Comment End -->
                      <!-- CASE #819 Begin -->
                      	<input type="checkbox" name="chkAllDisclosures" value="" onClick="checkAll()">
                      <!-- CASE #819 End -->
                      </div>
                  </td>
                  --%>
                  <!-- CASE #1374 Comment End -->
		<!-- CASE #1374 Begin -->
			<!-- Drop-down to change conflict status for all entities. Doesn't work on Safari on Mac-->                  
<script language="javascript">
	var os = "";
	var isMac = 0;
	var isSafari = 0;
	
	try{
		os = navigator.platform;

		if(os.indexOf("Mac") != -1){
			isMac = 1;
		}

		if(navigator.userAgent.indexOf("Safari")!= -1){
			isSafari = 1;
		}
	}
	catch(e){
	}			

	if(!(isMac && isSafari)){
		//alert('in here');  
		document.write('<tr>');
		document.write('<td colspan="6">');
		document.write('For all awards and proposals, set conflict status for <b>');
		document.write("<%=financialEntity.getName()%></b> to:&nbsp;");
		document.write('<select name="conflictStatus" onchange="changeAllStatus();">');
		document.write('<option selected>- Please Select -</option>');
	}
</script>
			<%
			String description1 = "";
			String code1 = "";
			for(int i=0;i<collCOIStatus.size();i++){
				ComboBoxBean objCombBean = (ComboBoxBean)collCOIStatus.get(i);
				code1 = objCombBean.getCode();
				description1 = "";
				if( code1.equals("301") || code1.equals("200") ){
					description1 = objCombBean.getDescription();
			%>

<script language="javascript">
	if(!(isMac && isSafari)){
		//alert("before option tag");
		document.write('<option value="<%=code1%>">');
		document.write('<%=description1%>');
		document.write('</option>');
	}
</script>
			<%
				}
			}
			%>
<script language="javascript">
	if(!(isMac && isSafari)){
		//alert("before ending the select");
		document.write('</select>');
		document.write('</td>');
		document.write('</tr>');
	}
</script>
			

<!-- CASE #1374 End -->			
		</table>
		<!-- CASE #1374 Comment Begin -->
                  <%--
                  <td width="140" height="25">
                    <div align="left">
			--%>
                    <%--  <html:select property="conflictStatus" >
                        <html:options collection="optionsConflictStatus" property="code"    labelProperty="description"/>
                      </html:select>
                      <html:hidden property="conflictStatus" />
                     --%>
                     <!-- CASE #1046 Comment Begin -->
                     <%--
                	  <select name="conflictStatus" style="font-family: verdana; font-size: 10px">
                        <option value="200">No conflict</option>
                        <option value="301">PI Identified Conflict</option>
                      </select>
                      --%>
                      <!-- CASE #1046 Comment End -->
                      <!-- CASE #1046 Begin -->
                      <%--
			<div>
			<select name="conflictStatus" onchange="javascript:changeStatus();">
				<option selected>- Select -</option>
				<%
				for(int i=0;i<collCOIStatus.size();i++){
					ComboBoxBean objCombBean = (ComboBoxBean)collCOIStatus.get(i);
					String code = objCombBean.getCode();
					if( code.equals("301") || code.equals("200") ){
						String description = objCombBean.getDescription();
						//String selected = ( code.equals("200") ? "selected" : "");
					%>
						<option value="<%= code %>"><%= description %></option>
					<%
					}
				}
				%>
			</select>
			</div>

                      <!-- CASE #1046 End -->

                      </div>
                  </td>
                  --%>
                  <!--
                  <td height="25">
                  -->
                   <%-- <div align="right"><html:link href="javascript:changeStat(<%=totalPendingDisclosures%>);">
                   --%>
                     <div >
                     <!-- CASE #812 Comment Begin -->
                     <%--<a href="javascript:changeStatus(<%=totalPendingDisclosures%>);">
                            <img src="<bean:write name="ctxtPath"/>/images/applychanges.gif"  border="0">
                            </a>
                      --%>
                      <!-- CASE #812 Comment End -->
                      <!-- CASE #812 Begin -->
                      <%--<a href="javascript:changeStatus();">
                      <img src="<bean:write name="ctxtPath"/>/images/applychanges.gif"  border="0">
			</a>--%>
                      <!-- CASE #812 End -->
                    </div>
                   <%-- <div align="right"><img src="<bean:write name="ctxtPath"/>/images/applychanges.gif" width="120" height="26" onClick="javascript:changeStatus(<%=totalPendingDisclosures%>)"></div>
                    <div align="right"><input type ="image" src="images/achanges.gif" width="120" height="26" border="0" onClick ="changeStatus(<%=totalPendingDisclosures%>);"> </div>
                    --%>
                    <!--
		</td>
                </tr>
		</table>
		-->
		<!-- CASE #1374 Comment End -->
                <table width="100%" border="0" cellspacing="1" cellpadding="2">


              <tr>
                <td width="320" height="25" bgcolor="#CC9999">
                  <div align="center"><font color="#FFFFFF"> <bean:message key = 'annualDisclosure.coiDisclosureTitle' />
                    </font></div>
                </td>              
                <td width="100" height="25" bgcolor="#CC9999">
                  <div align="center"><font color="#FFFFFF"> <bean:message key = 'annualDisclosure.conflictStatus' />
                    </font></div>
                </td>
                <td width="40" height="25" bgcolor="#CC9999">
                  <div align="center"><font color="#FFFFFF"> <bean:message key = 'annualDisclosure.disclosureFor' />
                    </font></div>
                </td>
                <td width="88" height="25" bgcolor="#CC9999">
                  <div align="center"><font color="#FFFFFF"> <bean:message key = 'annualDisclosure.number' />
                    </font></div>
                </td>
                <td width="65" height="25" bgcolor="#CC9999">
                  <div align="center"><font color="#FFFFFF"> <bean:message key = 'annualDisclosure.sponsor' />
                    </font></div>
                </td>
                <td width="44" height="25" bgcolor="#CC9999">
                  <div align="center"><font color="#FFFFFF"> <bean:message key = 'annualDisclosure.disclosureNumber' />
                    </font></div>
                </td>                
              </tr>
              <!-- CASE #1046 Begin -->
              <% int disclIndex = 0; %>
              <!-- CASE #1046 End -->
              <logic:present name="allPendingDisclosures"  scope="session" >
              <logic:iterate id="disclosure" name="allPendingDisclosures"  
              		type ="edu.mit.coeus.coi.bean.AnnDisclosureDetailsBean">
              <tr>
                <td height="25" bgcolor="#FBF7F7">
                    <div align="left"><coeusUtils:formatOutput name='disclosure' property='title' defaultValue="&nbsp;" /></div>
                </td>              
                <td height="25" bgcolor="#FBF7F7">
                  <!-- CASE #1046 Comment Begin -->
                  <%--
                  <!-- CASE #812 Begin -->
                  <!-- Change the order of the option tags, so that if the entity conflict status is
                  not found, the drop down defaults to Not Previously Reported -->
                  <div>
                    <select name="disclConflictStatus" >
                      <%  String status =  disclosure.getConflictStatus();
                        String selected="";
                        if ( (status != null) && (status.equals("100")) ) {
			                            selected = "selected";
                        }
                    %>
                    	<option <%=selected%> value='100'>Not Previously Reported</option>
                    <%	selected = "";
                    	if( (status != null) && (status.equals("301")) ){
                    		selected = "selected";
                    	}
                    %>
                      	<option <%=selected%> value='301'>PI Identified Conflict</option>
                    <%
                        selected="";
                        if ( (status != null) && (status.equals("200")) ) {
                            selected = "selected";
                        }
                    %>
                    	<option <%=selected%> value='200'>No conflict</option>
                    </select>
                    --%>
                  <!-- CASE #812 End -->
                  <!-- CASE #402 End -->
                  <%--
                  <!-- CASE #402 Comment Begin -->

                  	<select name="disclConflictStatus" style="font-family: verdana; font-size: 10px">
			<%	String status = disclosure.getConflictStatus();

				String strSelectedNPR = "";
				String strSelectedNC = "";
				String strSelectedPIC = "";

				strSelectedNPR = "100".equals(status.trim())?"selected":"";
				strSelectedNC = "200".equals(status.trim())?"selected":"";
				strSelectedPIC = "301".equals(status.trim())?"selected":"";
			%>
				<option <%= strSelectedNC %> value="200">No conflict</option>
				<option <%= strSelectedPIC %> value="301">PI Identified Conflict</option>
				<option <%= strSelectedNPR %> value="100">Not Previously Reported</option>
			</select>

                  <!-- CASE #402 Comment End -->
                  --%>

                    <%--  <html:select property="conflictStatus" >
                            <html:options collection="optionsConflictStatus" property="code"    labelProperty="description"/>
                        </html:select>
                       --%>
                       <%--
                       </div>
                    --%>
                    <!-- CASE #1046 Comment End -->
                 <!-- CASE #1046 Begin-->
			<div align="center">
			<select name='disclConflictStatus<%=disclIndex%>'">
                      <%  String status =  disclosure.getConflictStatus();
                      //System.out.println("status: "+status);
                      	String code = "";
                      	String description  = "";
                        String selected="";
                    %>
                    	<option>- Please Select -</option>
                    <%
                        for(int i=0; i<collCOIStatus.size(); i++){
                        	ComboBoxBean comboBox = (ComboBoxBean)collCOIStatus.get(i);
                        	code = comboBox.getCode();
                        	if(code.equals("301")){
                        		description = comboBox.getDescription();
                        		break;
                        	}
                        }
                    	if( (status != null) && (status.equals("301")) ){
                    		selected = "selected";
                    	}
                    %>
                      	<option <%=selected%> value='301'><%=description%></option>
                    <%
                        for(int i=0; i<collCOIStatus.size(); i++){
                        	ComboBoxBean comboBox = (ComboBoxBean)collCOIStatus.get(i);
                        	code = comboBox.getCode();
                        	if(code.equals("200")){
                        		description = comboBox.getDescription();
                        		break;
                        	}
                        }
                        selected="";
                        if ( (status != null) && (status.equals("200")) ) {
                            selected = "selected";
                        }
                    %>
                    	<option <%=selected%> value='200'><%=description%></option>
                    </select>
                    <% disclIndex++; %>
			</div>
                 <!-- CASE #1046 End -->
                </td>
                <td height="25" bgcolor="#FBF7F7" align="center">
                  <div ><bean:write name='disclosure' property='disclosureFor'/></div>
                </td>
                <td height="25" bgcolor="#FBF7F7">
                  <div ><bean:write name='disclosure' property='number'/></div>
                </td>
                <td height="25" bgcolor="#FBF7F7" align="center">
                 <div ><coeusUtils:formatOutput name='disclosure' property='sponsor' defaultValue="&nbsp;"/></div>
                </td>
                <td height="25" bgcolor="#FBF7F7">
                <div>
			<bean:write name="disclosure" property="disclosureNumber"/>
		</div>
                </td>                
              </tr>
              </logic:iterate>
              </logic:present>
              <%--  Display of the Pending Disclosures information ends here.....--%>
              <tr>
                <td height="31" colspan="2">&nbsp;</td>
                <td colspan="6" height="31">
                  <div align="right">
                    <!-- CASE #404 Begin -->
                    <html:image page="/images/saveproceednextent.gif" border="0" />
                    <html:hidden name="financialEntity" property="number" />
                    <!-- CASE #404 End -->
                    <html:link  forward="loginCOI"  >
                    <img src="<bean:write name="ctxtPath"/>/images/exit.gif" width="50"
                    height="22" border="0">
                    </html:link>
                    </div>
                </td>
              </tr>
            </table>