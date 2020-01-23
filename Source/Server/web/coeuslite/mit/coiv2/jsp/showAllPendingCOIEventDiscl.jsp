<%-- 
    Document   : showAllPendingCOIEventDiscl
    Created on : Sep 5, 2011, 4:19:33 PM
    Author     : twinkle
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
   "http://www.w3.org/TR/html4/loose.dtd">
<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ page
	import="java.util.Vector,edu.mit.coeuslite.coiv2.beans.CoiDisclosureBean,edu.mit.coeuslite.coiv2.services.CoiCommonService,java.util.Date;"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>C O I</title>
<%String path = request.getContextPath();%>
<script language="javascript" type="text/JavaScript"
	src="<%=path%>/coeuslite/mit/utils/scripts/sorttable.js"></script>
<script src="<%=path%>/coeuslite/dartmouth/utils/scripts/divSlide.js"></script>
<script src="<%=path%>/coeuslite/dartmouth/utils/scripts/Balloon.js"></script>
<link href="<%=path%>/coeuslite/dartmouth/utils/css/coeus_styles.css"
	rel="stylesheet" type="text/css" />
<link
	href="<%=request.getContextPath()%>/coeuslite/mit/coiv2/css/layout.css"
	rel="stylesheet" type="text/css" />
<link
	href="<%=request.getContextPath()%>/coeuslite/mit/coiv2/css/styles.css"
	rel="stylesheet" type="text/css" />
<link
	href="<%=request.getContextPath()%>/coeuslite/mit/coiv2/css/collapsemenu.css"
	rel="stylesheet" type="text/css" />
</head>
<script src="js/jquery.js" type="text/javascript"></script>


<body>
	<table id="bodyTable" class="table" style="width: 985px;" border="0"
		align="center">

		<tr style="background-color: #6E97CF; height: 22px; width: 985px;"
			class="theader">
			<td colspan="9"
				style="background-color: #6E97CF; color: #FFFFFF; font-size: 14px; font-weight: bold; margin: 0; padding: 2px 0 2px 10px; position: relative; text-align: left;"><strong>List
					Of Pending Event Reviews</strong></td>
		</tr>
		<tr>
			<td colspan="9"
				style="background-color: #6E97CF; color: #173B63; font-size: 12px; font-weight: bold;">Pending
				Event Reviews with No Financial Entities</td>
		</tr>
		<tr>
			<td
				style="background-color: #6E97CF; color: #173B63; font-size: 12px; font-weight: bold;">&nbsp;</td>
			<td
				style="background-color: #6E97CF; color: #173B63; font-size: 12px; font-weight: bold;"><strong>Owned
					By</strong></td>

			<td
				style="background-color: #6E97CF; color: #173B63; font-size: 12px; font-weight: bold;"><strong>Review
					Status</strong></td>
			<td
				style="background-color: #6E97CF; color: #173B63; font-size: 12px; font-weight: bold;"><strong>Department</strong></td>
			<td
				style="background-color: #6E97CF; color: #173B63; font-size: 12px; font-weight: bold;"><strong>Last
					Update</strong></td>
			<td
				style="background-color: #6E97CF; color: #173B63; font-size: 12px; font-weight: bold;"><strong>Submit
					Date</strong></td>
			<td
				style="background-color: #6E97CF; color: #173B63; font-size: 12px; font-weight: bold;"><strong>Expiration
					Date</strong></td>
			<td
				style="background-color: #6E97CF; color: #173B63; font-size: 12px; font-weight: bold;"><strong>&nbsp;&nbsp;&nbsp;</strong></td>
			<td
				style="background-color: #6E97CF; color: #173B63; font-size: 12px; font-weight: bold;"><strong>&nbsp;&nbsp;&nbsp;</strong></td>
		</tr>
		<logic:present name="pendingEventDiscl" scope="request">
			<%
                    String strBgColor = "#DCE5F1";
                    Vector projectNameList = (Vector)request.getAttribute("pendingEventDiscl");
                    int index = 0;
        %>
			<logic:iterate id="projectList" name="pendingEventDiscl"
				type="edu.mit.coeuslite.coiv2.beans.CoiDisclosureBean">
				<%
                                if (index%2 == 0) {
                                strBgColor = "#D6DCE5";
                                } else {
                                strBgColor="#DCE5F1";
                                }
     %>
				<%
     CoiDisclosureBean pendingDisclosuresBean = (CoiDisclosureBean) projectNameList.get(index);
                 request.getSession().removeAttribute("selectedPjct");
            request.getSession().removeAttribute("disclPjctModuleName");
            // String name = pendingDisclosuresBean.getCoiDisclosureNumber();
             // request.getSession().setAttribute("selectedPjct", name);
            // request.getSession().setAttribute("selectedPjct", name);
           
            boolean isFEPresent = false; 
            CoiDisclosureBean beanHeader = (CoiDisclosureBean) projectNameList.get(index);
             CoiCommonService coiCommonService = CoiCommonService.getInstance();
             String updateHeader = coiCommonService.getFormatedDate(beanHeader.getUpdateTimestamp());
              
             String expireHeader = coiCommonService.getFormatedDate(beanHeader.getExpirationDate());
              if(beanHeader.getIsFEPresent()!=null && beanHeader.getIsFEPresent() == 1) {
                 isFEPresent = true;
             }
              String moduleName = "";
             String name1 = pendingDisclosuresBean.getCoiDisclosureNumber();
             Integer code=pendingDisclosuresBean.getModuleCode();
                          String moduleKey=pendingDisclosuresBean.getModuleItemKey();
                          request.getSession().setAttribute("selectedPjct",moduleKey);
             Integer seqNo= (Integer)pendingDisclosuresBean.getSequenceNumber();
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
                else if(code == 8) {
                    moduleName = "Travel";
                } 
           if(!isFEPresent) { 
     %>

				<tr bgcolor="<%=strBgColor%>" id="row<%=index%>" class="rowLine"
					onmouseover="className='rowHover rowLine'"
					onmouseout="className='rowLine'" style="height: 22px;">
					<td>&nbsp; <%
            session.setAttribute("isEvent", true);
            String link1 = request.getContextPath()+"/getannualdisclosure.do?&param1=" + pendingDisclosuresBean.getCoiDisclosureNumber() +"&param2=" + pendingDisclosuresBean.getSequenceNumber()+"&param7="+ pendingDisclosuresBean.getPersonId()+"&param5="+moduleName+"&param6="+"throughShowAllDiscl"+"&selectedPjct="+moduleKey+"&fromReview=showAllEventReview";
        %>
					</td>
					<td><a href='<%=link1%>'> <b><bean:write
									name="projectList" property="userName" /></b></a></td>
					<!--    <td>
            <a href='<%=link1%>'> <b><%=moduleName%></b></a>
          </td>-->

					<td><a href='<%=link1%>'> <b><bean:write
									name="projectList" property="reviewStatus" /></b></a></td>
					<td><a href='<%=link1%>'> <b><bean:write
									name="projectList" property="department" /></b></a></td>
					<td><a href='<%=link1%>'> <b><bean:write
									name="projectList" property="updateTimestamp"
									format="MM/dd/yyyy H:MM" /></b></a></td>
					<td><a href='<%=link1%>'> <b><bean:write
									name="projectList" property="updateTimestamp"
									format="MM/dd/yyyy" /></b></a></td>
					<td><a href='<%=link1%>'> <b><bean:write
									name="projectList" property="expirationDate"
									format="MM/dd/yyyy" /></b></a></td>

					<%String linkPrint = "/approveddisclosureprint.do?selected=current&fromReview=showAllEventReview&param1=" + pendingDisclosuresBean.getCoiDisclosureNumber() +"&param2=" + pendingDisclosuresBean.getSequenceNumber()+"&param7="+ pendingDisclosuresBean.getPersonId();%>
					<td width=""><u><html:link action='<%=linkPrint%>'
								target="_blank">Print</html:link></u></td>
					<td width="">
						<%String linkAppr = request.getContextPath()+"/setstatus.do?&param=approve&param1=" + pendingDisclosuresBean.getCoiDisclosureNumber() +"&param2=" + pendingDisclosuresBean.getSequenceNumber()+"&param3="+ pendingDisclosuresBean.getPersonId()+"&param6="+"throughShowAllDiscl"+"&fromReview=showAllEventReview"+"&param5="+moduleName+"&selectedPjct="+moduleKey;%>
						<u><a href="<%=linkAppr%>">Approve</a></u>
					</td>
				</tr>
				<%}index++;%>
			</logic:iterate>
		</logic:present>

		<tr>
			<td colspan="9">&nbsp;</td>
		</tr>
		<tr>
			<td colspan="9"
				style="background-color: #6E97CF; color: #173B63; font-size: 12px; font-weight: bold;">Pending
				Event Reviews with Financial Entities</td>
		</tr>
		<tr>
			<td
				style="background-color: #6E97CF; color: #173B63; font-size: 12px; font-weight: bold;">&nbsp;</td>
			<td
				style="background-color: #6E97CF; color: #173B63; font-size: 12px; font-weight: bold;"><strong>Owned
					By</strong></td>

			<td
				style="background-color: #6E97CF; color: #173B63; font-size: 12px; font-weight: bold;"><strong>Review
					Status</strong></td>
			<td
				style="background-color: #6E97CF; color: #173B63; font-size: 12px; font-weight: bold;"><strong>Department</strong></td>
			<td
				style="background-color: #6E97CF; color: #173B63; font-size: 12px; font-weight: bold;"><strong>Last
					Update</strong></td>
			<td
				style="background-color: #6E97CF; color: #173B63; font-size: 12px; font-weight: bold;"><strong>Submit
					Date</strong></td>
			<td
				style="background-color: #6E97CF; color: #173B63; font-size: 12px; font-weight: bold;"><strong>Expiration
					Date</strong></td>
			<td
				style="background-color: #6E97CF; color: #173B63; font-size: 12px; font-weight: bold;"><strong>&nbsp;&nbsp;&nbsp;</strong></td>
			<td
				style="background-color: #6E97CF; color: #173B63; font-size: 12px; font-weight: bold;"><strong>&nbsp;&nbsp;&nbsp;</strong></td>
		</tr>
		<logic:present name="pendingEventDiscl" scope="request">
			<%
                            String strBgColor1 = "#DCE5F1";
                            Vector projectNameList1 = (Vector)request.getAttribute("pendingEventDiscl");
                            int index1 = 0;
                %>
			<logic:iterate id="projectList1" name="pendingEventDiscl"
				type="edu.mit.coeuslite.coiv2.beans.CoiDisclosureBean">
				<%
                                        if (index1%2 == 0) {
                                        strBgColor1 = "#D6DCE5";
                                        } else {
                                        strBgColor1="#DCE5F1";
                                        }
             %>
				<%
             CoiDisclosureBean pendingDisclosuresBean1 = (CoiDisclosureBean) projectNameList1.get(index1);
                         request.getSession().removeAttribute("selectedPjct");
                    request.getSession().removeAttribute("disclPjctModuleName");
                    // String name = pendingDisclosuresBean.getCoiDisclosureNumber();
                     // request.getSession().setAttribute("selectedPjct", name);
                    // request.getSession().setAttribute("selectedPjct", name);

                    boolean isFEPresent1 = false;
                    CoiDisclosureBean beanHeader1 = (CoiDisclosureBean) projectNameList1.get(index1);                    

                      if(beanHeader1.getIsFEPresent()!=null && beanHeader1.getIsFEPresent() == 1) {

                         isFEPresent1 = true;
                     }
                      String moduleName1 = "";
                    
                     Integer code1=pendingDisclosuresBean1.getModuleCode();
                                  String moduleKey1=pendingDisclosuresBean1.getModuleItemKey();
                                  request.getSession().setAttribute("selectedPjct",moduleKey1);
                     Integer seqNo1= (Integer)pendingDisclosuresBean1.getSequenceNumber();
        request.getSession().setAttribute("selectedPjctSeqNo", seqNo1);
        request.getSession().setAttribute("selectedPjctsModuleItemKeySeqNo", moduleKey1);
        request.getSession().setAttribute("frmDiscl", true);

                        if(code1 == 1) {
                            moduleName1 = "Award";
                        }
                        else if(code1 == 2) {
                            moduleName1 = "Proposal";
                        }
                       else if(code1 == 3) {
                            moduleName1 = "IRB Protocol";
                        }
                        else if(code1 == 4) {
                            moduleName1 = "IACUC Protocol";
                        }
                        else if(code1 == 5) {
                            moduleName1 = "Annual";
                        }
                        else if(code1 == 6) {
                            moduleName1 = "Annual";
                        }
                        else if(code1 == 11) {
                            moduleName1 = "Proposal";
                        }
                         else if(code1 == 12) {
                            moduleName1 = "Protocol";
                        }

                        else if(code1 == 13) {
                            moduleName1 = "Annual";
                        }
                         else if(code1 == 8) {
                            moduleName1 = "Travel";
                        }
              if(isFEPresent1) {
             %>
				<tr bgcolor="<%=strBgColor1%>" id="row<%=index1%>" class="rowLine"
					onmouseover="className='rowHover rowLine'"
					onmouseout="className='rowLine'" style="height: 22px;">
					<td>&nbsp; <%
                    session.setAttribute("isEvent", true);
                    String link1 = request.getContextPath()+"/getannualdisclosure.do?&param1=" + pendingDisclosuresBean1.getCoiDisclosureNumber() +"&param2=" + pendingDisclosuresBean1.getSequenceNumber()+"&param7="+ pendingDisclosuresBean1.getPersonId()+"&param5="+moduleName1+"&param6="+"throughShowAllDiscl"+"&selectedPjct="+moduleKey1+"&fromReview=showAllEventReview";
                %>
					</td>
					<td><a href='<%=link1%>'> <b><bean:write
									name="projectList1" property="userName" /></b></a></td>
					<!--    <td>
                    <a href='<%=link1%>'> <b><%=moduleName1%></b></a>
                  </td>-->

					<td><a href='<%=link1%>'> <b><bean:write
									name="projectList1" property="reviewStatus" /></b></a></td>
					<td><a href='<%=link1%>'> <b><bean:write
									name="projectList1" property="department" /></b></a></td>
					<td><a href='<%=link1%>'> <b><bean:write
									name="projectList1" property="updateTimestamp"
									format="MM/dd/yyyy H:MM" /></b></a></td>
					<td><a href='<%=link1%>'> <b><bean:write
									name="projectList1" property="updateTimestamp"
									format="MM/dd/yyyy" /></b></a></td>
					<td><a href='<%=link1%>'> <b><bean:write
									name="projectList1" property="expirationDate"
									format="MM/dd/yyyy" /></b></a></td>
					<%String linkPrint = "/approveddisclosureprint.do?selected=current&fromReview=showAllEventReview&param1=" + pendingDisclosuresBean1.getCoiDisclosureNumber() +"&param2=" + pendingDisclosuresBean1.getSequenceNumber()+"&param7="+ pendingDisclosuresBean1.getPersonId();%>
					<td width=""><u><html:link action='<%=linkPrint%>'
								target="_blank">Print</html:link></u></td>

					<td width="">&nbsp;</td>
				</tr>
				<%}index1++;%>

			</logic:iterate>
		</logic:present>
		<logic:present name="noRightAtPerHomeUnit">
			<script language="javaScript">
            alert("You do not have the right to view this disclosure.");
        </script>
		</logic:present>
	</table>
</body>