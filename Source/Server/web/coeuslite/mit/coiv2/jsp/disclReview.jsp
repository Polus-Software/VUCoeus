<%--
    Document   : showAllPendingCOIDiscl
    Created on : Sep 2, 2011, 10:14:39 AM
    Author     : twinkle
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
   "http://www.w3.org/TR/html4/loose.dtd">
<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ page import="java.util.Vector,edu.mit.coeuslite.coiv2.beans.CoiDisclosureBean,edu.mit.coeuslite.coiv2.services.CoiCommonService,java.util.Date"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
<title>C O I</title>
<%String path = request.getContextPath();%>
<script src="<%=path%>/coeuslite/dartmouth/utils/scripts/divSlide.js"></script>
<script src="<%=path%>/coeuslite/dartmouth/utils/scripts/Balloon.js"></script>
<script language="javascript" type="text/JavaScript" src="<%=path%>/coeuslite/mit/utils/scripts/sorttable.js"></script>
<link href="<%=path%>/coeuslite/dartmouth/utils/css/coeus_styles.css" rel="stylesheet" type="text/css"/>
<link href="<%=request.getContextPath()%>/coeuslite/mit/coiv2/css/layout.css" rel="stylesheet" type="text/css" />
<link href="<%=request.getContextPath()%>/coeuslite/mit/coiv2/css/styles.css" rel="stylesheet" type="text/css" />
<link href="<%=request.getContextPath()%>/coeuslite/mit/coiv2/css/collapsemenu.css" rel="stylesheet" type="text/css" />
</head>
<script src="js/jquery.js" type="text/javascript"></script>


<body>
<table id="bodyTable" class="table" style="width: 960px;" border="0" align="center">
<tr style="background-color:#6E97CF;height: 22px;width: 960px;">
    <td colspan="8" style="background-color:#6E97CF;color:#FFFFFF;font-size:14px;font-weight:bold;margin: 0;padding: 2px 0 2px 10px;position: relative;text-align: left;"><strong>&nbsp;&nbsp;&nbsp;List Of Pending Disclosures</strong></td>
</tr>
<tr>
<td style="background-color:#6E97CF;color:#173B63;font-size:12px;font-weight:bold;">&nbsp;</td>
<td style="background-color:#6E97CF;color:#173B63;font-size:12px;font-weight:bold;"><strong>Owned By</strong></td>
<td style="background-color:#6E97CF;color:#173B63;font-size:12px;font-weight:bold;"><strong>Disclosure Number</strong></td>
<td style="background-color:#6E97CF;color:#173B63;font-size:12px;font-weight:bold;">Disclosure Status</td>
<td style="background-color:#6E97CF;color:#173B63;font-size:12px;font-weight:bold;">Disposition Status</td>
<td style="background-color:#6E97CF;color:#173B63;font-size:12px;font-weight:bold;"><strong>Update Timestamp</strong></td>
<td style="background-color:#6E97CF;color:#173B63;font-size:12px;font-weight:bold;"><strong>Submit Date</strong></td>
<td style="background-color:#6E97CF;color:#173B63;font-size:12px;font-weight:bold;"><strong>Expiration Date</strong></td>
</tr>
<logic:present name="entityNameList">
       <%
            session.removeAttribute("isEvent");
            String strBgColor = "#DCE5F1";
            Vector projectNameList = (Vector)request.getAttribute("entityNameList");
            int index = 0;
        %>
<logic:iterate id="pjtTitle" name="entityNameList">
     <%
                                if (index%2 == 0) {
                                strBgColor = "#D6DCE5";
                                } else {
                                strBgColor="#DCE5F1";
                                }
     %>
<%
             CoiDisclosureBean beanHeader = (CoiDisclosureBean) projectNameList.get(index);
             CoiDisclosureBean projectName = (CoiDisclosureBean)projectNameList.get(index);
            request.getSession().removeAttribute("selectedPjct");
            request.getSession().removeAttribute("disclPjctModuleName");
             String name = projectName.getCoiDisclosureNumber();
              request.getSession().setAttribute("selectedPjct", name);
             request.getSession().setAttribute("selectedPjct", name);
             String projectType=beanHeader.getModuleName();
              String name1 = projectName.getCoiDisclosureNumber();
             request.getSession().setAttribute("pjctType", projectType);
             CoiDisclosureBean bean = (CoiDisclosureBean) projectNameList.get(index);
             String moduleName = "";
             Integer code = bean.getModuleCode();
                          String moduleKey=bean.getModuleItemKey();
             Integer seqNo= (Integer)bean.getSequenceNumber();
request.getSession().setAttribute("selectedPjctSeqNo", seqNo);
request.getSession().setAttribute("selectedPjctsModuleItemKeySeqNo", moduleKey);
request.getSession().setAttribute("frmDiscl", true);
                if(code == 1) {
                    moduleName = "Award";
                }
                else if(code == 2) {
                    moduleName = "Proposal";
                }
               else if(code == 3) {
                    moduleName = "IRB Protocol";
                }
                else if(code == 4) {
                    moduleName = "IACUC Protocol";
                }
                else if(code == 5) {
                    moduleName = "Annual";
                }
                else if(code == 6) {
                    moduleName = "Annual";
                }
                else if(code == 11) {
                    moduleName = "Proposal";
                }
                 else if(code == 12) {
                    moduleName = "Protocol";
                }

                else if(code == 13) {
                    moduleName = "Annual";
                }
             CoiCommonService coiCommonService = CoiCommonService.getInstance();
             String updateHeader = coiCommonService.getFormatedDate(beanHeader.getUpdateTimestamp());
             String expireHeader = coiCommonService.getFormatedDate(beanHeader.getExpirationDate());
%>
  <tr bgcolor="<%=strBgColor%>" id="row<%=index%>" class="rowLine" onmouseover="className='rowHover rowLine'" onmouseout="className='rowLine'" style="height: 22px;">
  <td width="2%">
      <% String link1 = request.getContextPath()+"/getannualdisclosure.do?&param1=" + bean.getCoiDisclosureNumber() +"&param2=" + bean.getSequenceNumber()+"&param7="+ bean.getPersonId()+"&param5="+moduleName+"&param6="+"throughShowAllDiscl";%>
      </td>
   <td>
       <a href='<%=link1%>'><b><bean:write name="pjtTitle" property="userName"/></b></a>
   </td>
   <td>
       <a href='<%=link1%>'><b><bean:write name="pjtTitle" property="coiDisclosureNumber"/></b></a>
   </td>
  <td>
      <a href='<%=link1%>'> <b><bean:write name="pjtTitle" property="disclosureStatus"/></b></a>
   </td>
   <td>
       <a href='<%=link1%>'>  <b><bean:write name="pjtTitle" property="dispositionStatus"/></b></a>
</td>
   <td>
       <a href='<%=link1%>'> <b><%=updateHeader%></b></a>
   </td>
   <td>
          <a href='<%=link1%>'> <b><%=updateHeader%></b></a>
   </td>
   <td>
       <a href='<%=link1%>'> <b><%=expireHeader%></b></a>
   </td>
</tr>
<%index++;%>
</logic:iterate>
</logic:present>
</table>
  </body>