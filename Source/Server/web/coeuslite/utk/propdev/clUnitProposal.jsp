<%@ page contentType="text/html;charset=UTF-8" language="java" 
    import="edu.mit.coeus.utils.ComboBoxBean,
   edu.mit.coeus.search.bean.DisplayBean,
                java.util.Vector,
                java.util.HashMap,
                edu.mit.coeus.utils.UtilFactory"%>
   


<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/coeus-utils.tld" prefix="coeusUtils" %>
<jsp:useBean  id="userUnits" scope="request" class="java.util.Vector"/>
<bean:size id="userUnitsSize" name="userUnits"/>

<%@ include file= "/coeuslite/mit/utils/CoeusContextPath.jsp"  %>

<html:html locale="true">
  <head>
    
    <title>Select Unit For New Proposal</title>
    <html:base/>
    
    <script>
        function openGeneralInfo(unitNumber,unitName){   
            document.generalInfoProposal.action = "<%=request.getContextPath()%>/getGeneralInfo.do?unitNumber="+unitNumber+"&unitName="+unitName;
            document.generalInfoProposal.submit();
        }
    </script>
  </head>
  <body>
  <html:form action="/getGeneralInfo.do" method="POST">
   <table width="100%" height="20%"  border="0" cellpadding="0" cellspacing="0" class="table"> 
      <tr >
        <td class="copybold"><img src="<bean:write name='ctxtPath'/>/coeusliteimages/spacer.gif" width="8" height="1">
            <font color='#173B63'><bean:message bundle="proposal" key="proposal.unitDetailsWindowTitle"/></font></td>
     </tr>
     <tr>
        <td  align="left" valign="top" class='copybold'>
          <table  width="99%"  border="0" align="center" cellpadding="4" cellspacing="0" class="tabtable">                        
            <tr>
                <td colspan="4" align="left" valign="top">
                    <table width="100%" height="20"  border="0" cellpadding="0" cellspacing="0" class="tableheader">
                    <tr  STYLE="background-image:url('<bean:write name='ctxtPath'/>/coeusliteimages/body_background.gif');border:0">
                      <td width='15%'><bean:message bundle="proposal" key="generalInfoProposal.unitNumber"/></td>
                      <td align="left"><bean:message bundle="proposal" key="generalInfoProposal.unitName"/></td>
                    </tr>
                </table>
              </td>
            </tr>
            <% int index = 0;%>
            <logic:iterate id="units" name="userUnits" type="org.apache.commons.beanutils.DynaBean">
                       <% String unitNumber = (String)units.get("unitNumber");
                          String unitName  = (String)units.get("unitName");  
                        %>
            
                       <tr bgColor="<%= ((++index)%2==0?"#D6DCE5":"#DCE5F1") %>">
                            <td width="15%" class='copy'>
                              <img src="<bean:write name='ctxtPath'/>/coeusliteimages/spacer.gif" width="5" height="1">
                                <a href="javascript:openGeneralInfo('<%=unitNumber%>','<%=unitName%>')">
                                    <coeusUtils:formatOutput name="units" property="unitNumber"/>
                               </a>
                            </td>
                            <td class='copy'>
                                <a href="javascript:openGeneralInfo('<%=unitNumber%>','<%=unitName%>')">
                                    <coeusUtils:formatOutput name="units" property="unitName"/>
                                 </a> 
                            </td>                  
                        </tr>
            </logic:iterate>          
                         
                     
            
            <%-- --%>
                           
          </table>
        </td>
      </tr>
      <tr>
        <td><img src="<bean:write name='ctxtPath'/>/coeusliteimages/spacer.gif" width="1" height="2"></td>
     </tr>     
  </table>
  </body>
  </html:form>
  </body>
</html:html>