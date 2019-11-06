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
                 edu.mit.coeus.coi.bean.ComboBoxBean" errorPage = "ErrorPage.jsp" %>

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
<%
    //Create a vector of ComboBoxBean beans which holds options information
    //of appliesTo (html select) component.

    //Vector vConflictStatus = new Vector();
    //vConflictStatus.add(new ComboBoxBean("200","No conflict"));
    //vConflictStatus.add(new ComboBoxBean("301","PI Identified Conflict"));
    //pageContext.setAttribute("optionsConflictStatus",vConflictStatus);
 %>
<table width="100%" border="0" cellpadding="0" cellspacing="0">
  <tr>
    <html:form name="frmAnnualDisclosures" action="/annualDisclosures.do" type="edu.mit.coeus.coi.action.AnnualDisclosuresActionForm" >
    <td background="<bean:write name="ctxtPath"/>/images/separator.gif" width="1">
    <image src="<bean:write name="ctxtPath"/>/images/separator.gif" width="1">
    </td>
     <td width="645">
      <table width="100%" border="0" cellspacing="0" cellpadding="0">
          <tr bgcolor="#cccccc">
            <td height="23" class="header"> &nbsp;<bean:message key='annualDisclosure.title' />&nbsp;&nbsp; 
            <bean:write name ="loggedinpersonname" /></td>
          </tr>
          <tr>
            <td height="23">
	  <%--
              Display any error messages existing in request scope
              <html:errors/>
          --%>
                <table>
                <logic:present name="errors"  scope="request" >
                  <logic:iterate id="error" name="errors"  type ="edu.mit.coeus.coi.bean.AnnDisclosureErrorBean">
                    <logic:notEqual name ="error" property ="entityNumber" value ="0">
                    <tr>
                        <td>
                            <font face="Verdana, Arial, Helvetica, sans-serif" size="2" color="red"><b>
                                AnnualDisclosure not complete &nbsp;&nbsp;<bean:write  name ="error" property ="entityNumber" />&nbsp;&nbsp;<bean:write  name ="error" property ="entityName" />
                            </b></font>
                        </td>
                    </tr>
                    </logic:notEqual>
                    <logic:equal name ="error" property ="entityNumber" value ="0">
                        <tr>
                            <td><font face="Verdana, Arial, Helvetica, sans-serif" size="2" color="red"><b>
                                    AnnualDisclosures complete
                                </b></font>
                            </td>
                        </tr>
                    </logic:equal>
                  </logic:iterate>
                </logic:present>
            </table>

              <table width="100%" border="0" cellspacing="0" cellpadding="0">
                <tr bgcolor="#82223A">
                  <td height="20"><font face="Verdana, Arial, Helvetica, sans-serif" size="2" color="#FFFFFF"><b>&nbsp;
                    <bean:message key='annualDisclosure.FinEntityTitle' /></b></font></td>
                </tr>
              </table>
              <%-- Display of financial entity information here...--%>
              <table width="100%" border="0" cellspacing="0" cellpadding="0">
                <tr>
                  <td colspan="6" height="5"></td>
                </tr>
                <tr bgcolor="#FBF7F7">
                  <td width="50" height="25">
                    <div align="left">&nbsp;<bean:message key = 'annualDisclosure.name' /> </div>
                  </td>
                  <td width="5" height="25">
                    <div align="left">:</div>
                  </td>

                  <td width="180" height="25">
                    <div align="left"> <b>&nbsp; <bean:write name='financialEntity' property='name' /> </b></div>
                    <html:hidden property ="name" />
                  </td>
                  <td width="162" height="25">
                    <div align="left"><bean:message key = 'annualDisclosure.Type' />&nbsp;</div>
                  </td>
                  <td width="5" height="25">
                    <div align="left">:</div>
                  </td>

                  <td width="252" height="25">
                    <div align="left"><b>&nbsp;<bean:write name='financialEntity' property='type'/></b></div>
                    <html:hidden property ="type" />
                  </td>
                </tr>

                <tr bgcolor="#F7EEEE">
                  <td width="140" height="25">
                    <div align="left">&nbsp;<bean:message key = 'annualDisclosure.status' /></div>
                  </td>
                  <td width="5" height="25">
                    <div align="left">:</div>
                  </td>
                  <td width="84" height="25">
                  <div align="left"><b>&nbsp;

                  <bean:write name="financialEntity" property ="status" />
                 <%-- <logic:present name="entity" property="status">
                    <logic:equal name="financialEntity" property="status"  value = '1'>
                        <bean:message key="annualDisclosure.active"/>
                    </logic:equal>

                    <logic:notEqual name="financialEntity" property="status"  value = '1'>
                        <bean:message key="annualDisclosure.inactive"/>
                   </logic:notEqual>
                  </logic:present> --%>
                   </b></div>
                  </td>

                  <td width="162" height="25">
                    <div align="left"><bean:message key = 'annualDisclosure.shareOwnership' /></div>
                  </td>
                  <td width="5" height="25">
                    <div align="left">:</div>
                  </td>
                  <td width="252" height="25">

                    <div align="left"><b>&nbsp;
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
            <%-- Financial entity display ends here.............--%>
                <tr bgcolor="#FBF7F7">
                  <td width="140" height="25">
                    <div align="left">&nbsp;<bean:message key = 'annualDisclosure.selectAll' /> </div>
                  </td>
                  <td width="5" height="25">
                    <div align="left">:</div>
                  </td>
                  <td width="84" height="25">
                    <div align="left">
                    <%--modified by GEO---%>
                    <%--
                      <input type="checkbox" name="chkAllDisclosures" value="" onClick="checkAll()">
                      --%>
                      <input type="checkbox" name="chkAllDisclosures" value="" onClick="checkAll(<%=totalPendingDisclosures%>)">
                      </div>
                  </td>
                  <td width="162" height="25">
                    <div align="left"><bean:message key = 'annualDisclosure.selectConflictStatusTo' /> </div>
                  </td>
                  <td width="5" height="25">
                    <div align="left">:</div>
                  </td>

                  <td width="252" height="25">
                    <div align="left"><b>

                    <%--  <html:select property="conflictStatus" >
                        <html:options collection="optionsConflictStatus" property="code"    labelProperty="description"/>
                      </html:select>
                      <html:hidden property="conflictStatus" />
                     --%>
                	  <select name="conflictStatus" >
                        <option value="200">No conflict</option>
                        <option value="301">PI Identified Conflict</option>
                      </select>

                      </b></div>
                  </td>
                </tr>
                <tr bgcolor="#FFFFFF">
                  <td colspan="6" height="24">
                   <%-- <div align="right"><html:link href="javascript:changeStat(<%=totalPendingDisclosures%>);">--%>
                     <div align="right"><a href="javascript:changeStatus(<%=totalPendingDisclosures%>);">
                            <img src="<bean:write name="ctxtPath"/>/images/achanges.gif" width="120" height="26" border="0">
                            </a>
                    </div>
                   <%-- <div align="right"><img src="<bean:write name="ctxtPath"/>/images/achanges.gif" width="120" height="26" onClick="javascript:changeStatus(<%=totalPendingDisclosures%>)"></div>
                    <div align="right"><input type ="image" src="images/achanges.gif" width="120" height="26" border="0" onClick ="changeStatus(<%=totalPendingDisclosures%>);"> </div> --%>
                  </td>
                </tr>
              </table>
              <table width="100%" border="0" cellspacing="0" cellpadding="0">
                <tr bgcolor="#82223A">
                  <td height="20"><font face="Verdana, Arial, Helvetica, sans-serif" size="2" color="#FFFFFF"><b>&nbsp;&nbsp;<bean:message key = 'annualDisclosure.coiDisclosure' /> </b></font></td>
                </tr>
              </table>

            <%--  Display of the Pending Disclosures informatin for the selected Financial entity starts here.....--%>

            <%--   <table width="101%" border="0" cellspacing="1" cellpadding="0">  --%>
                <table width="748" border="0" cellspacing="1" cellpadding="0">
              <tr>
                <td width="24" height="25" bgcolor="#CC9999">
                  <div align="center">&nbsp;</div>
                </td>
                <td width="20" height="25" bgcolor="#CC9999">
                  <div align="center"><font color="#FFFFFF"> <bean:message key = 'annualDisclosure.disclosureNumber' />
                    </font></div>
                </td>
                <td height="25" bgcolor="#CC9999" width="100">
                  <div align="center"><font color="#FFFFFF"> <bean:message key = 'annualDisclosure.conflictStatus' />
                    </font></div>
                </td>
                <td width="30" height="25" bgcolor="#CC9999">
                  <div align="center"><font color="#FFFFFF"> <bean:message key = 'annualDisclosure.disclosureFor' />
                    </font></div>
                </td>
                <td width="90" height="25" bgcolor="#CC9999">
                  <div align="center"><font color="#FFFFFF"> <bean:message key = 'annualDisclosure.number' />
                    </font></div>
                </td>
                <td width="109" height="25" bgcolor="#CC9999">
                  <div align="center"><font color="#FFFFFF"> <bean:message key = 'annualDisclosure.sponsor' />
                    </font></div>
                </td>
                <td width="300" height="25" bgcolor="#CC9999">
                  <div align="center"><font color="#FFFFFF"> <bean:message key = 'annualDisclosure.coiDisclosureTitle' />
                    </font></div>
                </td>
              </tr>
              <logic:present name="allPendingDisclosures"  scope="session" > <logic:iterate id="disclosure" name="allPendingDisclosures"  type ="edu.mit.coeus.coi.bean.AnnDisclosureDetailsBean">
              <tr>
                <td width="24" height="25" bgcolor="#FBF7F7">
                  <div align="center">
                    <input type="checkbox" name= "chkConflictStatus" value="checked">
                  </div>
                </td>
                <td width="20" height="25" bgcolor="#FBF7F7">
                  <div align="center"> <html:link  page="/viewCOIDisclosureDetails.do"
                                    paramId="disclNo"
                                    paramName="disclosure"
                                    paramProperty="disclosureNumber" > <coeusUtils:formatOutput name="disclosure" property="disclosureNumber"/>
                    </html:link> </div>
                </td>
                <td width="100" height="25" bgcolor="#FBF7F7">
                  <div align="center">
                    <select name="disclConflictStatus" >
                      <option value='200'>No conflict</option>
                      <%  String status =  disclosure.getConflictStatus();
                        String selected="";
                        if ( (status != null) && (status.equals("301")) ) {
                            selected = "selected";
                        }
                    %>
                      <option <%=selected%> value='301'>PI Identified Conflict</option>
                    </select>
                    <%--  <html:select property="conflictStatus" >
                            <html:options collection="optionsConflictStatus" property="code"    labelProperty="description"/>
                        </html:select>
                       --%> </div>
                </td>
                <td width="30" height="25" bgcolor="#FBF7F7">
                  <div align="center"><bean:write name='disclosure' property='disclosureFor'/></div>
                </td>
                <td width="90" height="25" bgcolor="#FBF7F7">
                  <div align="center"><bean:write name='disclosure' property='number'/></div>
                </td>
                <td width="109" height="25" bgcolor="#FBF7F7">
                 <div align="center"><coeusUtils:formatOutput name='disclosure' property='sponsor' defaultValue="&nbsp;"/></div>
                </td>
                <td width="300" height="25" bgcolor="#FBF7F7">
                    <div align="center">&nbsp;<coeusUtils:formatOutput name='disclosure' property='title' defaultValue="&nbsp;" /></div>
                </td>
              </tr>
              </logic:iterate> </logic:present> <%--  Display of the Pending Disclosures informatin ends here.....--%>
              <tr>
                <td height="31" colspan="2">&nbsp;</td>
                <td colspan="6" height="31">
                  <div align="right"><a href="javascript:history.back();"> <img src="<bean:write name="ctxtPath"/>/images/goback.gif" width="55" height="22" border="0"></a>
                    <html:link href="javascript:annDisclosuresUpdate(1);" > <img src="<bean:write name="ctxtPath"/>/images/saveproceednextent.gif"
                    width="198" height="24" border="0"> </html:link> <html:link  href="javascript:annDisclosuresUpdate(2);"  >
                    <img src="<bean:write name="ctxtPath"/>/images/saveandexit.gif"
                    width="94" height="24" border="0"> </html:link> <html:link  page="/welcome.jsp"  >
                    <img src="<bean:write name="ctxtPath"/>/images/exit.gif" width="50"
                    height="22" border="0"> </html:link> </div>
                </td>
              </tr>
            </table>


            </td>
          </tr>

      </table>
      </td>



   </html:form>
   </tr>
 </table>


