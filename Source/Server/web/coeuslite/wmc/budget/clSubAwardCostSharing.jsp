<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN"
	"http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<%@ include file= "/coeuslite/mit/utils/CoeusContextPath.jsp"  %>
<%@ page import="java.util.Vector,edu.wmc.coeuslite.budget.bean.CategoryBean,
                java.util.List,edu.wmc.coeuslite.utils.CoeusLineItemDynaList, org.apache.struts.validator.DynaValidatorForm"%>

<%@ taglib uri="/WEB-INF/struts-bean.tld"  prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld"  prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/coeus-utils.tld"  prefix="coeusUtils"%>

<jsp:useBean id="lstSubAwardCostSharing" scope="session" class="java.util.ArrayList" />
<jsp:useBean id="lstCostSharingData" scope="session" class="java.util.ArrayList" />

<html:html locale="true">
    <head>
        <link href="<bean:write name='ctxtPath'/>/coeuslite/mit/utils/css/coeus_styles.css" rel="stylesheet" type="text/css">
    </head>
<title>Sub Award Budget</title>
<script language="JavaScript">
    var errValue = false;
    var errLock = false;
    
    function showHide(val,value){
    var panel = 'Panel'+value;
    var pan = 'pan'+value;
    var hidePanel  = 'hidePanel'+value;
        if(val == 1){
            document.getElementById(panel).style.display = "none";
            document.getElementById(hidePanel).style.display = "block";
            document.getElementById(pan).style.display = "block";
        }
        else if(val == 2){
            document.getElementById(panel).style.display = "block";
            document.getElementById(hidePanel).style.display = "none";
            document.getElementById(pan).style.display = "none";
        }        
    }
    
    function closeWindow(){
        window.close();
    }    
</script>

<%
   String mode = (String)session.getAttribute("mode"+session.getId());
   String budgetStatusMode = (String)session.getAttribute("budgetStatusMode");
   boolean readOnly = false;
   if(mode!= null && mode.equals("display")){
       readOnly = true;
   }else if(budgetStatusMode != null && budgetStatusMode.equals("complete")){
        readOnly = true;
   }
    try{
    String editImage = request.getContextPath()+"/coeusliteimages/edit.gif";
    String deleteImage = request.getContextPath()+"/coeusliteimages/delete.gif";
    }catch(Exception ex){
        ex.printStackTrace();
    }
%>

<html:base/> 
<body>

<html:form action="/getSubAwardBudget.do" method="post"  enctype="multipart/form-data">
    <table width="100%" height="100%"  border="0" cellpadding="0" cellspacing="0" class="table">
        <!-- List of Uploaded Documents: for Show All - Start -->
        <tr>
            <td>
                <logic:present name = "lstSubAwardCostSharing">
                    <logic:notEmpty name = "lstSubAwardCostSharing">
                        <tr>
                            <td  align="left" valign="top" class='core'>
                                <table width="100%"  border="0" align="center" cellpadding="0" cellspacing="0" class="tabtable">                                    
                                    <tr align="center">
                                        <td colspan="10">
                                            <table width="100%"  border="0" cellpadding="0" cellspacing="0" id="t1" class="sortable" >
                                                <tr>                   
                                                    <td width="60%" align="left" class="theader"><bean:message bundle="budget" key="subAwardCostSharing.label"/></td>                                                    
                                                </tr>
                                                <% 
                                                String strBgColor = "#DCE5F1";
                                                int count = 0;
                                                %>
                                                
                                                <logic:iterate id="subAwardBudgetList" name="lstSubAwardCostSharing" type="java.util.ArrayList">
                                                    <% 
                                                    List organizationName = (List)subAwardBudgetList;
                                                    session.setAttribute("lstCostSharingData",organizationName);
                                                    edu.mit.coeus.budget.bean.BudgetSubAwardDetailBean budgetSubAwardDetailBean = (edu.mit.coeus.budget.bean.BudgetSubAwardDetailBean)organizationName.get(0);
                                                    if (count%2 == 0)
                                                        strBgColor = "#D6DCE5";
                                                    else
                                                        strBgColor="#DCE5F1";
                                                    %>
                                                    <tr  bgcolor="<%=strBgColor%>" width="15%" valign="top"  onmouseover="className='TableItemOn'"  onmouseout="className='TableItemOff'">                            
                                                        <td width="15%" height='20' align="left" class="copy">
                                                            <%String divName="Panel"+count;%>
                                                            <div id='<%=divName%>'>
                                                                
                                                                <%String divlink = "javascript:showHide(1,'"+count+"')";%>
                                                                <html:link href="<%=divlink%>">                                                                   
                                                                    <%String imagePlus=request.getContextPath()+"/coeusliteimages/plus.gif";%>
                                                                    <html:img src="<%=imagePlus%>" border="0"/>                                                                    
                                                                </html:link> &nbsp;&nbsp;<%=budgetSubAwardDetailBean.getOrganizationName()%>
                                                            </div>
                                                            <% String divsnName="hidePanel"+count;%>
                                                            <div id='<%=divsnName%>' style='display:none;'> 
                                                                
                                                                <% String divsnlink = "javascript:showHide(2,'"+count+"')";%>
                                                                <html:link href="<%=divsnlink%>">                                                                   
                                                                    <%String imageMinus=request.getContextPath()+"/coeusliteimages/minus.gif";%>
                                                                    <html:img src="<%=imageMinus%>" border="0"/>
                                                                </html:link> &nbsp;&nbsp;<%=budgetSubAwardDetailBean.getOrganizationName()%>
                                                            </div>
                                                        </td>
                                                    </tr>
                                                    
                                                    <tr>
                                                        <td colspan="9" >
                                                            <%String divisionName="pan"+count;
                                                            int rowCount=0;
                                                            %>
                                                            <div id='<%=divisionName%>' style='display:none;'>
                                                                <%boolean isEntered = false;%>
                                                                <table width="100%" height="100%" border="0">
                                                                    <tr class="theader">
                                                                        <td width="50"><bean:message bundle="budget" key="subAwardCostSharing.budgetPeriod"/></td>
                                                                        <td width="30"><bean:message bundle="budget" key="subAwardCostSharing.projectYear"/></td>
                                                                        <td width="60"><bean:message bundle="budget" key="subAwardCostSharing.amount"/></td>
                                                                    </tr>                                                                    
                                                                        <logic:iterate id="costSharingList" name="lstCostSharingData" type="edu.mit.coeus.budget.bean.BudgetSubAwardDetailBean">
                                                                            <%if (rowCount%2 == 0) {
                                                                            strBgColor = "#D6DCE5"; 
                                                                            }
                                                                            else { 
                                                                            strBgColor="#DCE5F1"; 
                                                                            } %>
                                                                            <tr bgcolor="<%=strBgColor%>" onmouseover="className='TableItemOn'" onmouseout="className='TableItemOff'">
                                                                                <%
                                                                                StringBuffer periodYear = new StringBuffer(costSharingList.getPeriodStartDate().toString());
                                                                                periodYear = new StringBuffer(periodYear.substring(0,periodYear.indexOf("-")));
                                                                                double costSharingAmount = costSharingList.getCostSharingAmount();                                                                                
                                                                                %>
                                                                                <td width="50"><%=costSharingList.getBudgetPeriod()%> </td>
                                                                                <td width="30"><%=periodYear%> </td>
                                                                                <td width="60"><coeusUtils:formatString name="costSharingList" property="costSharingAmount" formatType="currencyFormat"/> </td>
                                                                            </tr>
                                                                            <%rowCount++;%>
                                                                        </logic:iterate>                                                                    
                                                                </table>
                                                            </div>                                                            
                                                        </td>
                                                    </tr>
                                                    <% count++;%>
                                                </logic:iterate>
                                            </table>
                                        </td>
                                    </tr>
                                    
                                </table>
                            </td>
                        </tr>
                    </logic:notEmpty>
                </logic:present>
                <logic:present name = "lstSubAwardCostSharing">
                    <logic:empty name = "lstSubAwardCostSharing">
                        <tr>
                            <td>
                                <table>
                                    <tr>
                                        <td class="copy" height="100%" width="100%">
                                            <font color="red">
                                                <bean:message bundle="budget" key="subAwardCostSharing.NoData"/>
                                            </font>
                                        </td>                                       
                                    </tr> 
                                </table>
                            </td>
                        </tr> 
                    </logic:empty>
                </logic:present>
            </td>
        </tr>
        
        <tr>                   
            <td width="60%" align="right" class="theader">
                <%
                String subAwardCloseLink = "javascript:closeWindow()";
                %>
                <html:link href="<%=subAwardCloseLink%>" >
                    <bean:message bundle="budget" key="subAwardCostSharing.close"/>
                </html:link>
            </td>
        </tr>
 
    </table>
</html:form>
</body>
</html:html>