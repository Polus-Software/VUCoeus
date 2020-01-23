<%--
/*
 * @(#) PersonDetailsContent.jsp 1.0 2002/06/10
 *
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 *
 * @author  Geo Thomas.
 */
--%>
<%--
A view component to dispaly details of the selected person.
--%>

<%@ page import="java.util.Vector,edu.mit.coeus.utils.UtilFactory,edu.mit.coeus.coi.bean.*" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/coeus-utils.tld" prefix="coeusUtils" %>

<jsp:useBean id = "personInfo" class = "edu.mit.coeus.bean.PersonInfoBean" scope="session" />
<%@ include file="CoeusContextPath.jsp"  %>
<table width="100%" cellpadding="0" cellspacing="0" border="0">
      <tr>
        <td background="<bean:write name="ctxtPath"/>/images/separator.gif" width="1">
            <image src="<bean:write name="ctxtPath"/>/images/separator.gif" width="1">
        </td>
        <td width="645">
         <table width="100%" border="0" cellspacing="0" cellpadding="0">
           <tr bgcolor="#cccccc"> 
            <td height="23"> &nbsp;<font face="Verdana, Arial, Helvetica, sans-serif" size="2">
                <b><bean:message key="personInfo.header" /> - <%=UtilFactory.dispEmptyStr(personInfo.getFullName())%></b></font>
            </td>
          </tr>
          <tr valign="top">
            <td height="23">
            <logic:present name="personInfo" property="ownInfo">
             <logic:equal name="personInfo" property="ownInfo" value="true">
                <div align="center"><b><bean:message key="personInfo.form.label.error" />
                </b></div>
             </logic:equal>
             <logic:notEqual name="personInfo" property="ownInfo" value="true">
              <table width="100%" border="0" cellspacing="0" cellpadding="0">
                <tr> 
                  <td background="<bean:write name="ctxtPath"/>/images/tabback.gif">
                    <table border=0 cellpadding=0 cellspacing=0>
                      <tr> 
                        <td><img src="<bean:write name="ctxtPath"/>/images/pdetails.gif" width="120" height="24"></td>
                      </tr>
                    </table>
                  </td>
                </tr>
              </table>
              <table width="100%" border="0" cellspacing="0" cellpadding="0">
                <tr> 
                  <td colspan="6" height="5"></td>
                </tr>
                <tr bgcolor="#FBF7F7"> 
                  <td width="120" height="25"> 
                    <div align="left">&nbsp;<bean:message key="personInfo.form.label.fullName"/></div>
                  </td>
                  <td width="5" height="25"> 
                    <div align="left">:</div>
                  </td>
                  <td width="142" height="25"> 
                    <div align="left"><b>&nbsp;<%=UtilFactory.dispEmptyStr(personInfo.getFullName())%></b></div>
                  </td>
                  <td width="113" height="25"> 
                    <div align="left">&nbsp;<bean:message key="personInfo.form.label.priorName"/></div>
                  </td>
                  <td width="5" height="25"> 
                    <div align="left">:</div>
                  </td>
                  <td width="261" height="25"> 
                    <div align="left"><b>&nbsp;<%=UtilFactory.dispEmptyStr(personInfo.getPriorName())%></b></div>
                  </td>
                </tr>
                <tr bgcolor="#F7EEEE"> 
                  <td width="120" height="25"> 
                    <div align="left">&nbsp;<bean:message key="personInfo.form.label.userName"/></div>
                  </td>
                  <td width="5" height="25"> 
                    <div align="left">:</div>
                  </td>
                  <td width="142" height="25"> 
                    <div align="left"><b>&nbsp;<%=UtilFactory.dispEmptyStr(personInfo.getUserName())%></b></div>
                  </td>
                  <td width="113" height="25"> 
                    <div align="left">&nbsp;<bean:message key="personInfo.form.label.homeUnit"/></div>
                  </td>
                  <td width="5" height="25"> 
                    <div align="left">:</div>
                  </td>
                  <td width="261" height="25"> 
                    <div align="left"><b>&nbsp;<%=UtilFactory.dispEmptyStr(personInfo.getHomeUnit())%></b></div>
                  </td>
                </tr>
                <tr bgcolor="#FBF7F7"> 
                  <td width="120" height="25"> 
                    <div align="left">&nbsp;<bean:message key="personInfo.form.label.email"/></div>
                  </td>
                  <td width="5" height="25"> 
                    <div align="left">:</div>
                  </td>
                  <td width="142" height="25"> 
                    <div align="left"><b>&nbsp;<%=UtilFactory.dispEmptyStr(personInfo.getEmail())%></b></div>
                  </td>
                  <td width="113" height="25"> 
                    <div align="left">&nbsp;<bean:message key="personInfo.form.label.dirTitle"/></div>
                  </td>
                  <td width="5" height="25"> 
                    <div align="left">:</div>
                  </td>
                  <td width="261" height="25"> 
                    <div align="left"><b>&nbsp;<%=UtilFactory.dispEmptyStr(personInfo.getDirTitle())%></b></div>
                  </td>
                </tr>
                <tr bgcolor="#F7EEEE"> 
                  <td width="120" height="25"> 
                    <div align="left">&nbsp;<bean:message key="personInfo.form.label.offLocation"/></div>
                  </td>
                  <td width="5" height="25"> 
                    <div align="left">:</div>
                  </td>
                  <td width="142" height="25"> 
                    <div align="left"><b>&nbsp;<%=UtilFactory.dispEmptyStr(personInfo.getOffLocation())%></b></div>
                  </td>
                  <td width="113" height="25"> 
                    <div align="left">&nbsp;<bean:message key="personInfo.form.label.offPhone"/></div>
                  </td>
                  <td width="5" height="25"> 
                    <div align="left">:</div>
                  </td>
                  <td width="261" height="25"> 
                    <div align="left"><b>&nbsp;<%=UtilFactory.dispEmptyStr(personInfo.getOffPhone())%></b></div>
                  </td>
                </tr>
                <tr bgcolor="#FBF7F7"> 
                  <td width="120" height="25"> 
                    <div align="left">&nbsp;<bean:message key="personInfo.form.label.secOffLocation"/></div>
                  </td>
                  <td width="5" height="25"> 
                    <div align="left">:</div>
                  </td>
                  <td width="142" height="25"> 
                    <div align="left"><b>&nbsp;<%=UtilFactory.dispEmptyStr(personInfo.getSecOffLoc())%></b></div>
                  </td>
                  <td width="113" height="25"> 
                    <div align="left">&nbsp;<bean:message key="personInfo.form.label.secOffPhone"/></div>
                  </td>
                  <td width="5" height="25"> 
                    <div align="left">:</div>
                  </td>
                  <td width="261" height="25"> 
                    <div align="left"><b>&nbsp;<%=UtilFactory.dispEmptyStr(personInfo.getSecOffPhone())%></b></div>
                  </td>
                </tr>
              </table>
              </logic:notEqual>
             </logic:present>
            </td>
          </tr>
        </table>
     </td>
    </tr>
</table>

