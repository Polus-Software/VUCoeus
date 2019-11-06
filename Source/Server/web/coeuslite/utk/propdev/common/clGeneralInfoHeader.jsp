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
    // Added for the Exception logged in Catalina.out
    if(proposalHeaderBean != null && proposalHeaderBean.getProposalNumber() != null && !"".equals(proposalHeaderBean.getProposalNumber())){
    String EMPTYSTRING ="";
    String updateTimestamp = proposalHeaderBean.getUpdateTimestamp();
    String updateUser = "";
    if(proposalHeaderBean.getUpdateUser() != null){
        updateUser = proposalHeaderBean.getUpdateUser().toLowerCase() ;
    }
    String createTimestamp =proposalHeaderBean.getCreateTimestamp() ;
    
    String createUser = "";
    if(proposalHeaderBean.getCreateUser() != null){
        createUser = proposalHeaderBean.getCreateUser().toLowerCase() ;
    }
    String title = proposalHeaderBean.getTitle();
    //String isHierarchy = proposalHeaderBean.getIsHierarchy();
    //isHierarchy = (isHierarchy==null) ? "" : isHierarchy;
    //String isParent = proposalHeaderBean.getIsParent();    
    //isParent = (isParent==null) ? "" : isParent;
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
    String strEndDate = "";
    String strStartDate = "";
    if(endDate != null && !"".equals(endDate)){
        strEndDate = dateFormat.format(endDate);
    }
    
    //Commented and modified for COEUSQA-3517 : Lite Dev Proposal header on General Info window does not display full Proposal Period data - start
    /*if(strStartDate != null && !"".equals(strStartDate)){
        strStartDate = dateFormat.format(startDate);
    }*/
    if(startDate != null && !"".equals(startDate)){
        strStartDate = dateFormat.format(startDate);
    }
    //Commented and modified for COEUSQA-3517 : Lite Dev Proposal header on General Info window does not display full Proposal Period data - end
    
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
    //added for user Name id
        String updateUserName =proposalHeaderBean.getUpdateUserName();
        String createUserName =proposalHeaderBean.getCreateUserName();
 %>
    <table width='100%' border="0" cellpadding="0" cellspacing="0" class="table">
        <tr>
            <% if(proposalHeaderBean.getPersonName()== null){%>
                <td  nowrap class='copybold' width='60'>&nbsp;&nbsp;&nbsp;<bean:message bundle="budget" key="headerLabel.Investigator"/></td>
                <td class='copy'  nowrap></td>
             <%}else{%>
                <td  nowrap class='copybold' width='60' >&nbsp;&nbsp;&nbsp;<bean:message bundle="budget" key="headerLabel.Investigator"/></td>
                <td class='copy' nowrap > <%=proposalHeaderBean.getPersonName() %></td>
             <%}%>
             <%if(proposalHeaderBean.getProposalStatusDescription() == null){%>
                <td  nowrap class='copybold' align=right><bean:message bundle="budget" key="headerLabel.proposalNo"/> :</td>
                <td class='copy' nowrap><%=proposalHeaderBean.getProposalNumber()%></td>
             <%}else{%>
                <td  nowrap class='copybold' align=right><bean:message bundle="budget" key="headerLabel.proposalNo"/> :</td>
                <td class='copy' nowrap><%=proposalHeaderBean.getProposalNumber()%> (<%=proposalHeaderBean.getProposalStatusDescription()%>)</td>
             <%}%>
            <td rowspan="3" align="center">
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
                <td  class='copybold' width='60'  nowrap>&nbsp;&nbsp;&nbsp;<bean:message bundle="budget" key="headerLabel.Agency/Sponsor"/>:&nbsp;</td>
                <td class='copy' nowrap></td>
            <%}else{%>
                <td  class='copybold'width='60' nowrap>&nbsp;&nbsp;&nbsp;<bean:message bundle="budget" key="headerLabel.Agency/Sponsor"/>:&nbsp;</td>
                <td class='copy' nowrap><%=agencySponsor%></td>
            <%}%>                                   
            <%if((proposalHeaderBean.getProposalStartDate() == null)||(proposalHeaderBean.getProposalEndDate() == null)){%>
                <td  class='copybold' align=right ><bean:message bundle="budget" key="headerLabel.ProposalPeriod"/>&nbsp;</td>
                <td align="left" class='copy' nowrap></td>
            <%}else{%>
                <td class='copybold' align=right ><bean:message bundle="budget" key="headerLabel.ProposalPeriod"/>&nbsp;</td>
                <td align="left" class='copy'  nowrap><%=strStartDate%> - <%=strEndDate%></td>
            <%}%>
        </tr>
        <tr>
                      <%if(title == null){%>
            <td>
            <font class='copybold' width='60' >&nbsp;&nbsp;&nbsp;<bean:message bundle="budget" key="headerLabel.Title"/>:</font> &nbsp;&nbsp;&nbsp;&nbsp;</td>
            <td colspan="3" ><font class='copy'></font></td>
                                <%}else{%>
            <td >
            <font class='copybold' width='60' >&nbsp;&nbsp;&nbsp;<bean:message bundle="budget" key="headerLabel.Title"/>:</font> &nbsp;&nbsp;&nbsp;&nbsp;</td>
            <td colspan="3"><font class='copy'> <%=title%></font></td>
                                <%}%>
        </tr>
        <%--if(isHierarchy.equals("Y")){%>
        <tr>
                    <td align="right" width='20%' class='copybold' nowrap>&nbsp;&nbsp;&nbsp;&nbsp;</td>
                    <td class='copy' width='40%' nowrap></td>             
            <%if(isHierarchy.equals("Y")){
                  if(isParent.equals("Y")){%>
                    <td align="right" width='20%' class='copybold'  nowrap><bean:message bundle="proposal" key="generalInfoProposal.proposalHierarchy"/>:&nbsp;</td>
                    <td class='copy' width='40%' nowrap><html:img src="<%=parentImage%>" border="0"/></td>
                <%}else{%>
                    <td align="right" width='20%' class='copybold'  nowrap><bean:message bundle="proposal" key="generalInfoProposal.proposalHierarchy"/>:&nbsp;</td>
                    <td class='copy' width='40%' nowrap><html:img src="<%=childImage%>" border="0"/></td>
                <%}
            } else {%>
                <td align="right" width='20%' class='copybold' nowrap>&nbsp;&nbsp;&nbsp;&nbsp;</td>
                <td class='copy' width='40%' nowrap></td>                                 
            <%}%>        
        </tr>
        <%}--%>
        <tr>
            <td class='copybold'  nowrap colspan="5"><html:img src="<%=lineImage%>" width="100%" height="2" border="0"/></td>
        </tr>
        <tr>
            <!-- Commented/Added for case#2959 - Proposal Lead Unit Missing in Lite - start -->
            <%--            
                                <%if((proposalHeaderBean.getUnitNumber()== null)||(proposalHeaderBean.getUnitName()== null)) {%>
            <td  class='copybold' >&nbsp;&nbsp;&nbsp;<bean:message bundle="proposal" key="generalInfoProposal.leadUnit"/></td>
            <td class='copy'  colspan="3" align=left nowrap></td>                    
                                <%}else{%>
            <td class='copybold' >&nbsp;&nbsp;&nbsp;<bean:message bundle="proposal" key="generalInfoProposal.leadUnit"/></td>
            <td class='copy' colspan="3" align=left nowrap><%=proposalHeaderBean.getUnitNumber()%> :&nbsp;<%=proposalHeaderBean.getUnitName()%></td>                    
                                <%}%>
            --%>
            <%
                String unitNumber = proposalHeaderBean.getUnitNumber();
                String unitName = proposalHeaderBean.getUnitName();
                if(unitNumber == null || unitName == null){
                    unitNumber = proposalHeaderBean.getLeadUnitNumber();
                    unitName = proposalHeaderBean.getLeadUnitName();
                }                
            %>
            <!-- Commented/Added for case#2959 - Proposal Lead Unit Missing in Lite - end -->
            <td class='copybold' >&nbsp;&nbsp;&nbsp;<bean:message bundle="proposal" key="generalInfoProposal.leadUnit"/></td>
            <td class='copy' colspan="3" align=left nowrap><%=unitNumber%> :&nbsp;<%=unitName%></td>            
        </tr>

        <%//if((updateTimestamp!=null && !updateTimestamp.equals(""))||(updateUser!=null && !updateUser.equals(""))){%>
                             <%if((updateUserName==null) || updateUserName.equals("")){
                                updateUserName = updateUser;}
                            else{updateUserName = updateUserName;}%>
        <tr>
            <td  class='copybold' >&nbsp;&nbsp;&nbsp;<bean:message bundle="proposal" key="generalInfoProposal.lastUpdate"/>:</td>  
            <td class='copy' colspan='3'><%=updateTimestamp%> <b>by </b> <%=updateUserName%> </td>
        </tr>
                            
                   <%//}%>         
        
                            
    </table>
    <%}%>
</body>
<%}catch(Exception e){ e.printStackTrace(); }%>
</html>