<%--
    Document   : Award List
    Created on : Dec 22, 2010, 3:27:01 PM
    Author     : vineetha
--%>



<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@page
	import="java.util.HashMap,
                java.util.ArrayList,
                java.util.Hashtable,
                java.util.Iterator,java.util.Vector,
                edu.mit.coeuslite.utils.CoeusLiteConstants"%>

<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ include file="/coeuslite/mit/utils/CoeusContextPath.jsp"%>
<jsp:useBean id="awardList" scope="session" class="java.util.Vector" />
<jsp:useBean id="awardColumnNames" scope="session"
	class="java.util.Vector" />
<bean:size id="awardListSize" name="awardList" />
<% String type = request.getParameter("type");
    if(type == null){
        type = session.getAttribute("type").toString();
        }

       // Vector data = (Vector)session.getAttribute("awardList");
       // int size = data.size();
%>

<html:html>
<head>
<title>Award List</title>
<html:base />

<script language="javascript" type="text/JavaScript"
	src="<bean:write name='ctxtPath'/>/coeuslite/mit/utils/scripts/sorttable.js"></script>
</head>
<%-- <script>
        function openGeneralInfo(proposalNumber){
            document.generalInfoProposal.action = "<%=request.getContextPath()%>/getGeneralInfo.do?proposalNumber="+proposalNumber;
            alert(document.generalInfoProposal.action);
            document.generalInfoProposal.submit();
        }
   </script>--%>
<!-- Added for case#2776 - Allow concurrent Prop dev access in Lite - start -->
<%-- <script>--%>
<%--   var createBudgetPopUp = "<%=request.getParameter("createBudgetPopUp")%>";
        if(createBudgetPopUp == "Y"){
            var msg = '<bean:message bundle="award" key="generalInfoProposal.createNewBudget"/>';
            if (confirm(msg)==true){
                window.location = "<%=request.getContextPath()%>/getBudget.do?proposalNumber=<%=proposalNumber%>&createBudgetPopUp=Y&listPage=Y";
            }
        }--%>

<%--  </script>--%>
<!-- Added for case#2776 - Allow concurrent Prop dev access in Lite - end -->
<html:form action="/getBudget.do">
	<body>
		<%-- <table  width="100%" border="0" cellpadding="0" cellspacing="0" class="tableheader">
        <tr >
            <td style="width: 75%; text-align: left;">Display count : <%=size%></td>
            <td style="width: 25%; text-align: right;"><a href="#">Export to Excel</a></td>
        </tr>
    </table>--%>
		<table width="100%" height="100%" border="0" cellpadding="0"
			cellspacing="0" class="table">
			<tr>
				<td height="653" align="left" valign="top" width="100%">
					<table width="100%" border="0" align="center" cellpadding="0"
						cellspacing="0" class="tabtable">

						<tr>
							<td colspan="4" align="left" valign="top">
								<table width="100%" height="20" border="0" cellpadding="0"
									cellspacing="0" class="tableheader">
									<tr>
										<td>List of <%=type%>
										</td>
									</tr>
								</table>
							</td>
						</tr>
						<tr>
							<td>&nbsp;</td>
						</tr>
						<tr align="center">
							<td>
								<table width="98%" height="100%" border="0" cellpadding="2"
									cellspacing="0" id="t1" class="sortable">
									<tr>

										<%
                                     if(awardColumnNames != null && awardColumnNames.size()>0){
                                         for(int index=0;index<awardColumnNames.size();index++){
                                             edu.mit.coeus.search.bean.DisplayBean displayBean = (edu.mit.coeus.search.bean.DisplayBean)awardColumnNames.elementAt(index);
                                             if(displayBean.isVisible()){
                                                 String strColumnName = displayBean.getValue();
                                                 %>
										<td class="theader"><%=strColumnName%></td>
										<!--<td class="tableheadersub3">&nbsp;&nbsp;</td>-->

										<%
                                             }
                                        }
                                        %>
										<!-- Added for case#2776 - Allow concurrent Prop dev access in Lite -->
										<%--<td class="theader"><bean:message bundle="award" key="generalInfoProposal.Budget"/></td>--%>
										<%
                                     }
                                 %>

									</tr>
									<%
                                        String strBgColor = "#DCE5F1";
                                        int count = 0;
                                 %>
									<logic:present name="awardList">
										<logic:iterate id="award" name="awardList"
											type="java.util.HashMap">
											<%
                                        if (count%2 == 0)
                                            strBgColor = "#D6DCE5";
                                        else
                                            strBgColor="#DCE5F1";
                                 %>
											<tr bgcolor="<%=strBgColor%>" valign="top"
												onmouseover="className='TableItemOn'"
												onmouseout="className='TableItemOff'">
												<%
                                     if(awardColumnNames != null && awardColumnNames.size()>0){

                                         for(int index=0;index<awardColumnNames.size();index++){
                                            edu.mit.coeus.search.bean.DisplayBean displayBean = (edu.mit.coeus.search.bean.DisplayBean)awardColumnNames.elementAt(index);
                                            if(!displayBean.isVisible())
                                                continue;
                                            String key = displayBean.getName();
                                            if(key != null){
                                                String value = award.get(key) == null ? "" : award.get(key).toString();

                                                if(index==1 || index==2){
                                                    if(value != null && value.length() > 60){
                                                        value = value.substring(0,55);
                                                        value = value+" ...";
                                                    }
                                                }%>
												<td align="left" nowrap class="copy"><%=value%></td>
												<%
                                            }
                                        }
                                     }
                                 %>
												<!-- Added for case#2776 - Allow concurrent Prop dev access in Lite - start -->
												<%--<td align="left" nowrap class="copy">
                                        <a href="<%=request.getContextPath()%>/getBudget.do?proposalNumber=<%=proposalNumber%>&listPage=Y&isEditable=null">
                                            <u><bean:message bundle="award" key="generalInfoProposal.Budget"/></u>
                                        </a>
                                    </td>--%>
												<!-- Added for case#2776 - Allow concurrent Prop dev access in Lite - end -->
											</tr>
											<% count++;%>
										</logic:iterate>
									</logic:present>
									<logic:equal name="awardListSize" value="0">
										<tr>
											<td colspan='3' height="23" align=center>
												<div>No rows found</div>
											</td>
										</tr>
									</logic:equal>
								</table>
							</td>
						</tr>

						<tr>
							<td>&nbsp;</td>
						</tr>
					</table>
				</td>
			</tr>
			<tr>
				<td height='10'>&nbsp;</td>
			</tr>
		</table>
	</body>
</html:form>
</html:html>
