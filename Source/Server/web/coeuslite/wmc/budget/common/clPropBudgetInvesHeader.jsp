<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ include file= "/coeuslite/mit/utils/CoeusContextPath.jsp"  %>
<%@page import="java.util.HashMap,java.sql.Date,edu.wmc.coeuslite.budget.bean.ProposalBudgetHeaderBean
,java.text.SimpleDateFormat,edu.mit.coeus.budget.bean.BudgetInfoBean"%>
<jsp:useBean id="PAGE" scope="request" class="java.lang.String" />
<html>
<head>
<title>Proposal Details</title>
</head>

<body topmargin="0" leftmargin="0">
<%  //String pageMode = request.getParameter("PAGE");
    String EMPTYSTRING ="";
    ProposalBudgetHeaderBean proposalBudgetHeaderBean 
                = (ProposalBudgetHeaderBean)session.getAttribute("ProposalBudgetHeaderBean");
    BudgetInfoBean budgetInfoBean = (BudgetInfoBean)session.getAttribute("budgetInfoBean");
    
    String proposalNo = proposalBudgetHeaderBean.getProposalNumber();
    String personName = proposalBudgetHeaderBean.getPersonName();
    personName=(personName == null)?EMPTYSTRING:personName;
    
    String title=proposalBudgetHeaderBean.getTitle();
    title = (title == null)?EMPTYSTRING:title;
    title = (title.length()>60)?title.substring(0,61)+"...":title;
    
    String sponsorName=proposalBudgetHeaderBean.getSponsorName();
    sponsorName = (sponsorName == null)?EMPTYSTRING:sponsorName;
    sponsorName = (sponsorName.length()>40)?sponsorName.substring(0,41):sponsorName;
    
    String proposalStatus=proposalBudgetHeaderBean.getProposalStatus();
    int versionNumber=budgetInfoBean.getVersionNumber();
    
    SimpleDateFormat smDate = new SimpleDateFormat("MM/dd/yyyy");
    Date startDate = proposalBudgetHeaderBean.getProposalStartDate();
    Date endDate = proposalBudgetHeaderBean.getProposalEndDate();
    String showStartDate = smDate.format(startDate);
    String showEndDate = smDate.format(endDate);
 %>
            <table width="100%" border="0" cellpadding="0" cellspacing="0" class="table" >
                  <tr>
                     <td  align="right"  width='10%' nowrap class='copybold'><bean:message bundle="budget" key="headerLabel.Investigator"/>&nbsp;&nbsp;</td>
                     <td class='copy'  nowrap> <%=personName%></td>
                     <%if(proposalStatus != null) {%>
                     <td  align="right" width='10%'  nowrap class='copybold'><bean:message bundle="budget" key="headerLabel.proposalNo"/>: &nbsp;&nbsp;</td>
                     <td class='copy' width='35%' nowrap><%=proposalNo%> (<%=proposalStatus%>)</td>
                     <%}else{%>
                     <td  align="right" width='10%'  nowrap class='copybold'><bean:message bundle="budget" key="headerLabel.proposalNo"/>: &nbsp;&nbsp;</td>
                     <td class='copy' width='35%' nowrap><%=proposalNo%> </td>
                     <%}%>
                  </tr>
                  <tr>
                    <td align="right"  width='10%' class='copybold'  nowrap><bean:message bundle="budget" key="headerLabel.Agency/Sponsor"/>:&nbsp;&nbsp;</td>
                    <td class='copy'  nowrap><%=sponsorName%></td>
                    <td  align="right"  width='10%' class='copybold'  nowrap><bean:message bundle="budget" key="headerLabel.ProposalPeriod"/>&nbsp;&nbsp;</td>
                    <td class='copy' width='35%'  nowrap><%=showStartDate%>  -  <%=showEndDate%> </td>
                  </tr>
                  <tr>
                        <td align="right"  width='10%' class='copybold'  nowrap>
                         
                        <bean:message bundle="budget" key="headerLabel.Title"/>:&nbsp;&nbsp;</td>
                        <td class='copy'  nowrap><%=title%></td>
                        
                        
                        <td align="right"  width='10%' class='copybold' nowrap>
                        <%/* if(!((pageMode!=null) && pageMode.equals("V")) && !(request.getAttribute("PAGE")!=null && 
                                                          request.getAttribute("PAGE").equals("V"))) {*/
                          if(!(request.getAttribute("PAGE")!=null && 
                                                          request.getAttribute("PAGE").equals("V"))) {
                             
                            %>
                        <bean:message bundle="budget" key="budgetVersions.Version"/>:<%}%> &nbsp;&nbsp;</td>
                        <td class='copy' width='35%'  nowrap>
                            <%/*if(!((pageMode!=null) && pageMode.equals("V")) && !(request.getAttribute("PAGE")!=null && 
                                                          request.getAttribute("PAGE").equals("V"))) {*/
                              if(!(request.getAttribute("PAGE")!=null && 
                                                          request.getAttribute("PAGE").equals("V"))) {
                            %>
                        <%=versionNumber%> <%}%>&nbsp;</td> 
                        
                  </tr>
                  </table>
                  
</body>

</html>
