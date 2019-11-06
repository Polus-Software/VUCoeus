<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/coeus-utils.tld"  prefix="coeusUtils"%>
<%@ include file= "/coeuslite/mit/utils/CoeusContextPath.jsp"  %>
<%@page import="java.sql.Date,edu.utk.coeuslite.propdev.bean.EPSProposalHeaderBean, java.text.SimpleDateFormat"%>
<jsp:useBean  id="epsProposalHeaderBean"  scope="session" class="edu.utk.coeuslite.propdev.bean.EPSProposalHeaderBean"/>
<html>
<head>
<title>Proposal Details</title>
</head>
<%try{%>
<body topmargin="0" leftmargin="0">

<%  
    EPSProposalHeaderBean proposalHeaderBean=(EPSProposalHeaderBean)session.getAttribute("epsProposalHeaderBean");
    String EMPTYSTRING ="";
    String updateTimestamp = proposalHeaderBean.getUpdateTimestamp();
    String updateUser =proposalHeaderBean.getUpdateUser().toLowerCase() ;
    String createTimestamp =proposalHeaderBean.getCreateTimestamp() ;
    String createUser = proposalHeaderBean.getCreateUser().toLowerCase() ;
    String title = proposalHeaderBean.getTitle();
    String isHierarchy = proposalHeaderBean.getIsHierarchy();
    isHierarchy = (isHierarchy==null) ? "" : isHierarchy;
    String isParent = proposalHeaderBean.getIsParent();    
    isParent = (isParent==null) ? "" : isParent;
    if(title!=null && (!title.equals(EMPTYSTRING))){
        title=title.length()>60?title.substring(0,61)+"...":title;
    }
    
    String agencyCode = proposalHeaderBean.getSponsorCode();   
    String agencySponsor = proposalHeaderBean.getSponsorName();
    
    if(agencyCode != null && (!agencyCode.equals(EMPTYSTRING))){
        agencySponsor = agencyCode +" : " +agencySponsor;
    }    
    
    if(agencySponsor!=null && (!agencySponsor.equals(EMPTYSTRING))){
        agencySponsor=(agencySponsor.length()>52)?agencySponsor.substring(0,49)+"...":agencySponsor;
    }
    
    Date endDate=proposalHeaderBean.getProposalEndDate();
    Date startDate = proposalHeaderBean.getProposalStartDate();
    SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
    String strEndDate = dateFormat.format(endDate);
    String strStartDate = dateFormat.format(startDate);
    String lineImage = request.getContextPath()+"/coeusliteimages/line4.gif";
    String parentImage = request.getContextPath()+"/coeusliteimages/parent.gif";
    String childImage = request.getContextPath()+"/coeusliteimages/child.gif";
    String display =(String) request.getAttribute("display");
    String mode=(String)session.getAttribute("mode"+session.getId());
    boolean modeValue=false;
    if(mode!=null && !mode.equals("")) {   
      if(mode.equalsIgnoreCase("display")){
              modeValue=true;
      }
    }     
    
 %>
                            <table width='100%' border="0" cellpadding="0"  cellspacing="0" class="table">
                                <tr>
                                     <% if(proposalHeaderBean.getPersonName()== null){%>
                                     <td align="left" width='20%'  nowrap class='copybold'>&nbsp;&nbsp;&nbsp;<bean:message bundle="budget" key="headerLabel.Investigator"/></td>
                                     <td class='copy' width='40%' colspan='1'  nowrap> </td>
                                     <%}else{%>
                                     <td align="left" width='20%'  nowrap class='copybold'>&nbsp;&nbsp;&nbsp;<bean:message bundle="budget" key="headerLabel.Investigator"/></td>
                                     <td class='copy' width='40%' colspan='1' nowrap> <%=proposalHeaderBean.getPersonName() %></td>
                                     <%}%>
                                     <%if(proposalHeaderBean.getProposalStatusDescription() == null){%>
                                     <td align="left" width='15%'  nowrap class='copybold'><bean:message bundle="budget" key="headerLabel.proposalNo"/> :</td>
                                     <td class='copy' width='20%' nowrap><%=proposalHeaderBean.getProposalNumber()%> </td>
                                     <%}else{%>
                                     <td align="left" width='15%'  nowrap class='copybold'><bean:message bundle="budget" key="headerLabel.proposalNo"/> :</td>
                                     <td class='copy' width='20%' nowrap><%=proposalHeaderBean.getProposalNumber()%> (<%=proposalHeaderBean.getProposalStatusDescription()%>)</td>
                                     <%}%>
                                     <td rowspan="3" align="right" width="1%">
                                     <!-- Grants.gov -Start -->
                                     <logic:equal name="grantsGovExist" scope="session" value="1">
                                     <% String grantsGovImage = request.getContextPath()+"/coeusliteimages/grantsgov.gif"; %>
                                     <html:img height='50' src="<%=grantsGovImage%>" />
                                     </logic:equal>
                                     <!-- Grants.gov -End -->                
                                     </td>                                     
                                  </tr>
                                  <tr>
                                  <%if(agencySponsor == null){%>
                                    <td align="left" width='20%' class='copybold'  nowrap>&nbsp;&nbsp;&nbsp;<bean:message bundle="budget" key="headerLabel.Agency/Sponsor"/>:&nbsp;</td>
                                    <td class='copy' width='40%' colspan='1'  nowrap></td>
                                    <%}else{%>
                                    <td align="left" width='20%' class='copybold' nowrap>&nbsp;&nbsp;&nbsp;<bean:message bundle="budget" key="headerLabel.Agency/Sponsor"/>:&nbsp;</td>
                                    <td class='copy' width='40%' colspan='1' nowrap><%=agencySponsor%></td>
                                    <%}%>                                    
                                    <%if((proposalHeaderBean.getProposalStartDate() == null)||(proposalHeaderBean.getProposalEndDate() == null)){%>
                                    <td align="left" width='15%'class='copybold'  nowrap><bean:message bundle="budget" key="headerLabel.ProposalPeriod"/>&nbsp;</td>
                                    <td class='copy' width='20%'  nowrap></td>
                                    <%}else{%>
                                    <td align="left" width='15%' class='copybold'  nowrap><bean:message bundle="budget" key="headerLabel.ProposalPeriod"/>&nbsp;</td>
                                    <td class='copy' width='20%'  nowrap><%=strStartDate%> - <%=strEndDate%></td>
                                    <%}%>
                                </tr>
                                
                                <tr>
                                <%if(title == null){%>
                                <td  align="left"  width='5%' >
                                <font class='copybold' >&nbsp;&nbsp;&nbsp;<bean:message bundle="budget" key="headerLabel.Title"/>:</font> &nbsp;&nbsp;&nbsp;&nbsp;</td>
                                <td colspan="3" ><font class='copy'></font></td>
                                <%}else{%>
                                <td  align="left"  width='5%' >
                                <font class='copybold' >&nbsp;&nbsp;&nbsp;<bean:message bundle="budget" key="headerLabel.Title"/>:</font> &nbsp;&nbsp;&nbsp;&nbsp;</td>
                                <td colspan="3"><font class='copy'> <%=title%></font></td>
                                <%}%>
                                </tr>
                <%if(isHierarchy.equals("Y")){%>
                <tr>
                            <td align="left" width='20%' class='copybold'  nowrap>&nbsp;&nbsp;&nbsp;</td>
                            <td class='copy' width='40%' colspan='1'  nowrap></td>             
                    <%if(isHierarchy.equals("Y")){
                          if(isParent.equals("Y")){%>
                            <td align="left" width='15%'class='copybold'  nowrap><bean:message bundle="proposal" key="generalInfoProposal.proposalHierarchy"/>:&nbsp;</td>
                            <td class='copy' width='20%'  nowrap><html:img src="<%=parentImage%>" border="0"/></td>
                        <%}else{%>
                            <td align="left" width='15%'class='copybold'  nowrap><bean:message bundle="proposal" key="generalInfoProposal.proposalHierarchy"/>:&nbsp;</td>
                            <td class='copy' width='20%'  nowrap><html:img src="<%=childImage%>" border="0"/></td>
                        <%}
                    } else {%>
                        <td align="left" width='15%'class='copybold'  nowrap>&nbsp;&nbsp;&nbsp;&nbsp;</td>
                        <td class='copy' width='20%'  nowrap></td>                                 
                    <%}%>        
                </tr>  
                 <%}%>
                        </table>
</body>
<%}catch(Exception e){ e.printStackTrace(); }%>
</html>