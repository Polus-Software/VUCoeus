<%@ page contentType="text/html;charset=UTF-8" language="java" 
	%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>

<%@ include file= "/coeuslite/mit/utils/CoeusContextPath.jsp" %>
<html:html locale="true">
    <head>
    <title>Submit To Sponsor</title>
    <html:base/>
    <meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
    <%
        String proposalNumber = (String)request.getAttribute("proposalNumber");
        boolean disabledFlag = false;    
        if(proposalNumber == null){
            proposalNumber = "";
        }
    %>

    <script>
        
        function submitData(){
            document.submitTosponsorDetails.action = "<%=request.getContextPath()%>/feedInstProposal.do";
            document.submitTosponsorDetails.submit();
        }
        
        function open_search(){
            var winleft = (screen.width - 650) / 2;
            var winUp = (screen.height - 450) / 2;
            var win = "scrollbars=1,resizable=1,width=800,height=450,left="+winleft+",top="+winUp;
            //Commented and Added for COEUSQA-2428_CLONE -LIte - Prop Dev - Application does not return any rows while searching for Original proposal_Start
            //var newWin = window.open('<%=request.getContextPath()%>/irbSearch.do?type=<bean:message key="searchWindow.title.proposal"/>&search=true&searchName=PROPOSALSEARCH', "list", win);
            var newWin = window.open('<%=request.getContextPath()%>/irbSearch.do?type=<bean:message key="searchWindow.title.proposal"/>&search=true&searchName=INSTITUTEPROPOSALSEARCH', "list", win);
            //Commented and Added for COEUSQA-2428_CLONE -LIte - Prop Dev - Application does not return any rows while searching for Original proposal_End
            newWin.window.focus();
        }
        
        function fetch_Data(result){
            document.submitTosponsorDetails.instPropNumber.value = result["PROPOSAL_NUMBER"];
            checkLength();
        }
                
       
        function checkFlag(value){           
            if(value == 2){
                if(document.submitTosponsorDetails.generated.checked == true){
                    document.submitTosponsorDetails.notGenerated.checked = false;
                }
            }else if(value == 1){
                if(document.submitTosponsorDetails.notGenerated.checked == true){
                    document.submitTosponsorDetails.generated.checked = false;
                }
            }
        }

        function checkLength(){           
            if(document.submitTosponsorDetails.instPropNumber.value.length > 0){
                document.submitTosponsorDetails.notGenerated.disabled = true ;
                document.submitTosponsorDetails.generated.disabled = true ;
                document.submitTosponsorDetails.generated.checked = false;
                document.submitTosponsorDetails.notGenerated.checked = false;
            }else{
                document.submitTosponsorDetails.notGenerated.disabled = false ;
                document.submitTosponsorDetails.generated.disabled = false;
            }
        }
    </script>    
  
    <html:base/> 

    </head>
    <html:form action="/feedInstProposal" method="POST">
    <body>    
    <table width="100%" border="0" cellpadding="0" cellspacing="1" class="tabtable">
            <tr class = "copybold">
                <td align="left" width='99%'>
                    <html:errors header="" footer="" />
                    <logic:messagesPresent message = "true">  
                    <font color='red'>
                    <html:messages id="message" message="true" property="doNotGenerate" bundle="proposal">                
                       <li><bean:write name = "message"/></li>
                    </html:messages>
                    <html:messages id="message" message="true" property="invalidInstProp" bundle="proposal">                
                       <li><bean:write name = "message"/></li>
                    </html:messages>
                    <html:messages id="message" message="true" property="selectOption" bundle="proposal">                
                       <li><bean:write name = "message"/></li>
                    </html:messages>
                    </font>
                </logic:messagesPresent>   
            </td>           
        </tr>
        <tr>
            <td>
                <table width="100%" border="0" cellpadding="2" cellspacing="0" class="tabtable">
                
                    <tr>
                        <td class="tableheader">
                            <bean:message bundle="proposal" key="generalInfo.develop"/>
                                <%=proposalNumber%>
                            <bean:message bundle="proposal" key="generalInfo.revision"/>
                        </td>
                    </tr>
                                                          
                </table>
                
                <table width="100%" border="0" cellpadding="0" cellspacing="1" class="tabtable">
                    <tr>
                        <td  class="copybold" nowrap>
                            <bean:message bundle="proposal" key="generalInfo.knowPropNum"/>
                        </td>
                    </tr>
                    
                    <tr>
                        <td width="30%" class='copy'>
                            <bean:message bundle="proposal" key="generalInfo.orgPropNum" />
                        </td>
                        <td width ="10%"class='copy'>
                            <html:text property="instPropNumber" onkeyup="checkLength()" maxlength="9" />
                        </td>
                        <td class='copy'>
                            <html:link href="javascript:open_search()">
                                <bean:message key="label.search"/> 
                            </html:link>
                        </td>
                    </tr>
                </table>
                
                <table width="100%" border="0" cellpadding="2" cellspacing="1" class="tabtable">
                    <tr>
                        <td class="copybold">
                            <bean:message bundle="proposal" key="generalInfo.notKnowPropNum"/>
                        </td>
                    </tr>
                    
                    <tr>
                        <td width="30%" class='copy' align='left'>
                            <bean:message bundle="proposal" key="generalInfo.generate" />
                        </td>
                        
                        <td width="30%" class='copy' align='left'>
                            <html:checkbox property="generated" value="2" onclick="checkFlag(2)" />
                        </td>
                    </tr>
                    
                    <tr>
                        <td width="30%" class='copy' align='left'>
                            <bean:message bundle="proposal" key="generalInfo.choose"/>
                        </td>
                    </tr>

                    <tr>
                        <td width="30%" class='copy' align='left'>
                            <bean:message bundle="proposal" key="generalInfo.notGenerate"/>
                        </td>
                        
                        <td width="30%" class='copy' align='left'>
                            <html:checkbox property="notGenerated" value="1" onclick="checkFlag(1)" />
                        </td>
                    </tr>
                    
                    <tr>
                        <td width="30%" class='copy' align='left'>
                            <bean:message bundle="proposal" key="generalInfo.notSubmit"/>
                        </td>                        
                    </tr>                   
                    
                </table>
                
                <table width="100%" border="0" cellpadding="2" cellspacing="1" class="tabtable">
                    <tr>
                        <td align=center>
                            <html:button property="ok" value="Submit" styleClass="clbutton" onclick="submitData()"/>
                        </td>
                    </tr>
                </table>
                
            </td>
        </tr>
        </table>
        </html:form>
    </body>
</html:html>

