<%@page contentType="text/html"%>
<%@page pageEncoding="UTF-8"%>
<%@page import="edu.mit.coeus.bean.CoeusReportGroupBean, java.util.*"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
   "http://www.w3.org/TR/html4/loose.dtd">

<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Budget Print</title>
        <script src="<%=request.getContextPath()%>/coeuslite/mit/utils/scripts/divSlide.js"></script>
        <script>           
            function rowHover(rowId, styleName) {
                elemId = document.getElementById(rowId);
                //elemId.style.cursor = "hand";
                elemId.className = styleName;
            } 
            
            //Added for COEUSQA-1683 Print option to display Version Comments on Budget Reports-Start
            function budgetPrintSummary(actionType,printTypeId, selectedIndex){              
                var checkBoxName = 'chkComments'+selectedIndex;            
                var checksElement = document.getElementsByTagName("INPUT");
                var checkBoxCount = checksElement.length;
                var isCommentChecked;            
                    for (var index = 0; index < checkBoxCount; index++){
                            if (checksElement[index].name == checkBoxName){
                                isCommentChecked = checksElement[index].checked;                                        
                            }                                          
                        }
                    var anchorURL = "<%=request.getContextPath()%>/budgetPrintAction.do?action="+actionType+"&repId="+printTypeId+"&chkComments="+isCommentChecked;                                                
                    window.open(anchorURL);                               
            }
            //Added for COEUSQA-1683 Print option to display Version Comments on Budget Reports-End
        </script>
    </head>
    <body>
        <table align="center" border="0" cellspacing="0" cellpadding="2" width="100%" class="table">
        <tr class="theader">
            <td>
                <table width="100%" cellspacing="0" cellpadding="0">
                    <tr>
                        <th align="left"> Proposal Print </th>
                    </tr>
                </table>
            </td>             
        </tr>
        <tr>
            <td>Click on the link to open report.(opens in new window)</td>            
        </tr>
        <tr>
        <td>
        <table align="center" width="98%" border="0" cellspacing="0" cellpadding="2" class="tabtable">
            <!--Added/Modified for COEUSQA-1683 Print option to display Version Comments on Budget Reports-Start-->
            <tr>
            <td class="copybold" width="40%">
                 <bean:message key="budget.summary.reportName" bundle="budget"/>
            </td>
            <td class="copybold" align="center" width="20%">
                 <bean:message key="budget.printSummary.comments" bundle="budget"/>
            </td>
            <td class="copybold" width="38%">
                 
            </td>
            </tr>  
            <!--Added/Modified for COEUSQA-1683 Print option to display Version Comments on Budget Reports-End-->
                <%
                CoeusReportGroupBean repGrpBean = (CoeusReportGroupBean)request.getAttribute("Reports");
                if(repGrpBean != null && repGrpBean.getReports()!= null) {
                Set set = repGrpBean.getReports().keySet();
                Iterator iterator = set.iterator();
                String key, id, dispValue;
                CoeusReportGroupBean.Report repBean;
                int index = 0;
                while(iterator.hasNext()) {
                key = (String)iterator.next();
                repBean = (CoeusReportGroupBean.Report)repGrpBean.getReports().get(key);
                id = repBean.getId();
                dispValue = repBean.getDispValue();
                //Do not display Budget Total and Industrial Cumulative budget
                //if(id.equals("ProposalBudget/budgettotalbyperiod") || id.equals("ProposalBudget/indsrlcumbudget")) { //JIRA COEUSQA-3296
                //continue;
                //}
                %>  
                <!--Added/Modified for COEUSQA-1683 Print option to display Version Comments on Budget Reports-Start-->
                <tr class="rowLine" id="row<%=index%>" onmouseover="rowHover('row<%=index%>','rowHover rowLine')" onmouseout="rowHover('row<%=index%>','rowLine')" >
                    <td class="copybold">
                        <%String addExceptionLink = "javaScript:budgetPrintSummary('print','"+id+"','"+index+"')";%>                                     
                          <html:link href="<%=addExceptionLink%>"><%=dispValue%></html:link>                          
                    </td> 
                    <td class="copybold" align="center">
                        <input type="checkbox" name="chkComments<%=index%>">
                    </td> 
                    <td>
                        
                    </td>
                </tr>
                <!--Added/Modified for COEUSQA-1683 Print option to display Version Comments on Budget Reports-End-->
                <% 
                index = index + 1;
                }
                }
                %>           
        </table>
        <br>
        </td>
        </tr>
        </table>
    </body>
</html>
