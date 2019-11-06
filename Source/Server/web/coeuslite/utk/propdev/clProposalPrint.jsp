<%@page contentType="text/html"%>
<%@page pageEncoding="UTF-8"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ page import="java.util.*, edu.wmc.coeuslite.utils.DynaBeanList, edu.mit.coeus.sponsormaint.bean.SponsorFormsBean"%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1" />
        <title>Proposal Print</title>
        <script src="<%=request.getContextPath()%>/coeuslite/mit/utils/scripts/divSlide.js"></script>
        <SCRIPT LANGUAGE="JavaScript">
        var txtShow = "show";
        var txtHide = "hide";

        function showHidePanel(strDivName, imgId, linkId) {
        var disp = document.getElementById(strDivName).style.display;
        var markUp = document.getElementById(linkId).innerHTML;
        if(markUp == txtHide) {
            hide(strDivName, imgId, linkId);
        }else {
            show(strDivName, imgId, linkId);
        }
        }

        function hide(strDivName, imgId, linkId) {
            //document.getElementById(strDivName).style.display = "none";
            //document.getElementById(imgId).src=plus.src;
            //document.getElementById(linkId).innerHTML=txtShow;
            ds = new DivSlider();
            evalStr = "document.getElementById('"+imgId+"').src=plus.src;";
            evalStr = evalStr + "document.getElementById('"+linkId+"').innerHTML=txtShow;";
            ds.hideDiv(strDivName, evalStr);

        }

        function show(strDivName, imgId, linkId) {
            //document.getElementById(strDivName).style.display = "";
            //document.getElementById(imgId).src=minus.src;
            //document.getElementById(linkId).innerHTML=txtHide;
            evalStr = "document.getElementById('"+imgId+"').src=minus.src;";
            evalStr = evalStr + "document.getElementById('"+linkId+"').innerHTML=txtHide;";
            ds = new DivSlider();
            ds.showDiv(strDivName, evalStr);

        }

        var plus = new Image();
        var minus = new Image();
        function preLoadImages() {
        plus.src = "coeusliteimages/plus.gif";
        minus.src = "coeusliteimages/minus.gif";
        }

            
        var formDetails = new Array();
        var packageIndex = 0;
        var prevDivId = "";
        var prevPackageIndex = -1;
                
        var lastRowId = -1;
        var lastDataRowId;
        var lastLinkId;
        var lastDivId;
        var expandedRow = -1;

        var defaultStyle = "rowLine";
        var selectStyle = "rowHover copybold";
        
        var txtShow = "show";
        var txtHide = "hide";

        function selectRow(rowId, divId, dataRowId, linkId, packageIndex) {
            var row = document.getElementById(rowId); 
            if(rowId == lastRowId) {
                //Same Row just Toggle
                var style = document.getElementById(dataRowId).style.display;
                if(style == "") {
                    style = "none";
                    styleClass = defaultStyle;
                    toggleText = txtShow;
                    expandedRow = -1;
                }else{
                    style = "";
                    styleClass = selectStyle;
                    toggleText = txtHide;
                    expandedRow = rowId;
                }
                //document.getElementById(dataRowId).style.display = style;
                ds = new DivSlider();
                if(style == "") {
                    document.getElementById(dataRowId).style.display = "";
                    ds.showDiv(divId);
                }else {
                    var evalStr = "document.getElementById('"+dataRowId+"').style.display = 'none'";
                    ds.hideDiv(divId, evalStr);
                }
                //row.className = styleClass;
                row.className = defaultStyle;
                
                document.getElementById(linkId).innerHTML=toggleText;
            }else {
                //document.getElementById(dataRowId).style.display = "";
                document.getElementById(dataRowId).style.display = "";
                ds = new DivSlider();
                ds.showDiv(divId);
                row.className = defaultStyle;
                
                document.getElementById(linkId).innerHTML=txtHide;
                //row.className = selectStyle;
                expandedRow = rowId;
                
                //reset last selected row
                if(lastRowId != -1) {
                    row = document.getElementById(lastRowId);
                    //document.getElementById(lastDataRowId).style.display = "none";
                    ds2 = new DivSlider();
                    var evalStr = "document.getElementById('"+lastDataRowId+"').style.display = 'none'"
                    ds2.hideDiv(lastDivId, evalStr);
                    
                    document.getElementById(lastLinkId).innerHTML=txtShow;
                    row.className = defaultStyle;
                }
            }
            
            resetCheckBoxes(prevPackageIndex, false);
            
            prevPackageIndex = packageIndex;
            lastRowId = rowId;
            lastDataRowId = dataRowId;
            lastLinkId = linkId;
            lastDivId = divId;
        }
                
        function rowHover(rowId, styleName) {
            elemId = document.getElementById(rowId);
            elemId.style.cursor = "hand";
            //If row is selected retain selection style
            //Apply hover Style only if row is not selected
            if(rowId != expandedRow) {
                elemId.className = styleName;
            }
        }
        
        function resetCheckBoxes(packageIndex, value){
            //reset checkboxes for previous selected package
            if(prevPackageIndex != -1) {
                checkBoxArray = formDetails[packageIndex];
                for(index = 0; index < checkBoxArray.length; index++) {
                    elementName = checkBoxArray[index];
                    document.proposalPrint.elements[elementName].checked = value;
                }
            }//End if
        }
        
        function validateProposalPrint() {
            formSelected = false;
            //presently selected package would be saves as previousPackageIndex.
            checkBoxArray = formDetails[prevPackageIndex];
            for(index = 0; index < checkBoxArray.length; index++) {
                elementName = checkBoxArray[index];
                if(document.proposalPrint.elements[elementName].checked) {
                    formSelected = true;
                }
            }
            if(!formSelected) {
                alert("Select forms to print");
                return false;
            }
            return true;
        }
        
        <%
            DynaBeanList dynaBeanList= (DynaBeanList)request.getAttribute("grantsGov");
            int availForms = 0;
            if(dynaBeanList != null && dynaBeanList.getList() != null){
                availForms = dynaBeanList.getList().size();
            }
        %>
        var forms = <%=availForms%>;
        
        function printForms() {
            var checkBoxName;
            var selForms = "";
            var available = "";

            for(index=0; index < forms; index++) {
                checkBoxName = "listBean["+index+"].print"
                if(document.grantsGov.elements[checkBoxName].checked) {
                    if(selForms.length > 0) {
                        selForms = selForms + ","+ index;
                    }else {
                        selForms = ""+index;
                    }
                }
            }

            if(selForms.length == 0) {
                alert("<bean:message bundle="proposal" key="grantsgov.selectFormsToPrint"/>");
                return;
            }

            document.grantsGov.elements["simpleBean[0].submitType"].value = "Print";
            document.grantsGov.target="myWindow";
            document.grantsGov.submit();

            document.grantsGov.target="";
            //window.open("grantsGovAction.do?action=Print&selForms="+selForms);
            
        }//End Print Forms
        
        function selectDeselectAll(value){
            var checkBoxName;
            for(index=0; index < forms; index++) {
                checkBoxName = "listBean["+index+"].print"
                document.grantsGov.elements[checkBoxName].checked = value;
            }
        }
        
        function doNothing(){}
        
        preLoadImages();
        </script>

    </head>

    <body>
        <table align="center" border="0" cellspacing="0" cellpadding="3" width="100%" class="table">
            <tbody>
                <html:form action="proposalPrintAction" target="_blank" onsubmit="return validateProposalPrint()">
                    <tr class="theader">
                        <td>
                            <table width="100%" cellspacing="0" cellpadding="0">
                                <tr class="theader">
                                    <td align="left"> Proposal Print </td>
                                </tr>
                            </table>
                           
                        </td>
                    </tr>
                    <tr>
                        <td>
                            <div id="helpText" class='helptext'>            
                                <bean:message bundle="proposal" key="helpTextProposal.Print"/>  
                            </div>                         
                            Click on the Package to view Forms.
                        </td>
                    </tr>
                   
                    <script>
                        var packageName = new Array();
                        var sponsorCode
                        <logic:iterate id="simpleBean" name="proposalPrint" property="beanList" type="edu.mit.coeus.sponsormaint.bean.SponsorFormsBean" indexId="ctr">
                            packageName[<%=ctr%>] = "<bean:write name="simpleBean" property="packageName"/>";
                        </logic:iterate>
                    </script>
                    
                    <tr align="center">
                    <td>
                        <table align="center" width="99%" border="0" cellspacing="0" cellpadding="2" class="tabtable">
                        <tr class="theader">
                            <!-- Form Details Header - START-->
                            <td colspan="3">
                                <table width="100%" border="0" cellspacing="0" cellpadding="0" >
                                    <tr class="theader"><td>
                                        Sponsor Form Packages
                                    </td></tr>
                                </table>
                            </td>
                            <!-- Form Details Header - END-->
                        </tr>
                        
                        <!-- Form Details - START -->
                        <% int prevPackageNumber = -1, packageNumber = -1, packageIndex = 0;
                            String prevSponsorCode = "", sponsorCode = "";
                        %>
                        <logic:iterate id="listBean" name="proposalPrint" property="list" type="org.apache.struts.action.DynaActionForm" indexId="ctr">
                            <% 
                                packageNumber = ((Integer)listBean.get("packageNumber")).intValue();
                                sponsorCode =  (String)listBean.get("sponsorCode");
                                if(prevPackageNumber != packageNumber || !prevSponsorCode.equals(sponsorCode)) {
                                    if(prevPackageNumber != -1){
                                        //new package.%>
                            <script>
                                formDetails[packageIndex] = formPrintElement;
                                packageIndex = packageIndex + 1;
                            </script>
                            <%
                                packageIndex = packageIndex + 1;
                                    }
                                    if(packageIndex > 0){
                                        //end last package.%>
                                        </table></td></tr>
                                    <%}%>
                                    
                            <tr class="rowLine" id="row<%=packageIndex%>" onmouseover="rowHover('row<%=packageIndex%>','rowHover rowLine')" onmouseout="rowHover('row<%=packageIndex%>','rowLine')" onclick="selectRow('row<%=packageIndex%>', 'div<%=sponsorCode+packageNumber%>', '<%=sponsorCode+packageNumber%>', 'toggle<%=packageIndex%>', '<%=packageIndex%>')">
                                <td class="copybold">
                                    <script>document.write(packageName[packageIndex]);</script>
                                </td>
                                <td align="right" class="copybold"><a href="javascript:doNothing()" id="toggle<%=packageIndex%>">show</a></td>
                            </tr>
                            
                            <tr id="<%=sponsorCode+packageNumber%>" style="display:none;" class="copy rowHover">
                            <td colspan="2">
                            <div id="div<%=sponsorCode+packageNumber%>" style="overflow:hidden">
                            <table width="100%" border="0" cellspacing="0" cellpadding="0" class="copy rowHover">
                            <script>
                                var formPrintElement = new Array();
                                var formIndex = 0;
                            </script>
                             <tr class="copy rowHover">
                                <td>Select : <a href="javascript:resetCheckBoxes('<%=packageIndex%>', true)">All</a> | <a href="javascript:resetCheckBoxes('<%=packageIndex%>', false)">None</a> </td>
                             </tr>
                            <%
                                prevPackageNumber = packageNumber;
                                prevSponsorCode = sponsorCode;
                                }%>
                            <tr class="copy rowHover">
                                <td>
                                    <html:checkbox property="print" name= "listBean" indexed="true"/>
                                    <script>
                                        formPrintElement[formIndex] = "listBean[<%=ctr%>].print";
                                        formIndex = formIndex + 1;
                                    </script>
                                    <bean:write property="pageDescription" name="listBean"/>
                                    <html:hidden property="rowId" name="listBean" indexed="true"/>
                                    <html:hidden property="sponsorCode" name="listBean" indexed="true"/>
                                    <html:hidden property="pageDescription" name="listBean" indexed="true"/>
                                    <html:hidden property="packageNumber" name="listBean" indexed="true"/>
                                    <html:hidden property="pageNumber" name="listBean" indexed="true"/>
                                </td>
                            </tr>
                        </logic:iterate>
                        <%if(prevPackageNumber != -1 && packageNumber != -1 ){%>
                            <script>
                                //Last Package Details
                                formDetails[packageIndex] = formPrintElement;
                            </script>
                            
                            </table>
                            </div>
                            </td></tr>
                            
                            <tr class="rowLine">
                                <td align="left">
                                    <html:submit value="Print Selected"/>
                                </td>
                            </tr>
                            <%}else{%>
                            <tr class="rowLine">
                                <td align="left">
                                    No Packages Found.
                                </td>
                            </tr>
                            <%}%>
                            <!-- Form Details - END -->
                        </table>
                    </td>
                    </tr>
                    
                    </html:form>
                    
                    <%-- S2S Forms - START --%>
                    <tr align="center">
                    <td>
                        <br>
                        <table align="center" width="99%" border="0" cellspacing="0" cellpadding="0">
                            <tr class="theader">
                                <!-- Form Details Header - START-->
                                <td>
                                    <table width="100%" border="0" cellspacing="0" cellpadding="2" >
                                        <tr class="theader"><td>
                                            <a href="javascript:showHidePanel('forms','imgForms','linkForms')">
                                                &nbsp;<img src="<%=request.getContextPath()%>/coeusliteimages/minus.gif" border="0" id="imgForms" name="imgForms" alt="Show/Hide">&nbsp;
                                            </a>
                                            Grants.Gov
                                        </td>
                                        <td align="right">
                                            <a id="linkForms" href="javascript:showHidePanel('forms','imgForms','linkForms')">hide</a>
                                        </td>
                                        </tr>
                                    </table>
                                </td>
                                <!-- Form Details Header - END-->
                            </tr>
                            
                            <logic:notEmpty name="grantsGov" property="list" scope="request">
                            
                            <html:form action="grantsGovAction">
                                <logic:iterate id="simpleBean" name="grantsGov" property="beanList" type="org.apache.struts.action.DynaActionForm" indexId="ctr">
                                    <html:hidden property="submitType" name="simpleBean" indexed="true"/>
                                    <html:hidden property="submissionTypeCode" name="simpleBean" indexed="true"/>
                                    <html:hidden property="opportunityId" name="simpleBean" indexed="true"/>
                                    <html:hidden property="proposalNumber" name="simpleBean" indexed="true"/>
                                    <html:hidden property="opportunityTitle" name="simpleBean" indexed="true"/>
                                    <html:hidden property="cfdaNumber" name="simpleBean" indexed="true"/>
                                    <html:hidden property="competitionId" name="simpleBean" indexed="true"/>
                                </logic:iterate>
                            <tr>
                                <td>
                                    <div id="forms" style="overflow:hidden">
                                        <table align="center" width="100%" border="0" cellspacing="0" cellpadding="2" class="tabtable">
                                            <tr class="rowLine">
                                                <td>Select : <a href="javascript:selectDeselectAll(true)">All</a> | <a href="javascript:selectDeselectAll(false)">None</a></td>
                                                <!--<td>Form Name</td>-->
                                            </tr>
                                            <logic:iterate id="listBean" name="grantsGov" property="list" type="org.apache.struts.action.DynaActionForm" indexId="ctr">
                                                <tr class="rowLine">
                                                    <td>
                                                        <html:checkbox property="print" name="listBean" indexed="true"/>
                                                        <html:hidden property="proposalNumber" name="listBean" indexed="true"/>
                                                        <html:hidden property="includeSelected" name="listBean" indexed="true"/>
                                                        <html:hidden property="ns" name="listBean" indexed="true"/>
                                                        <html:hidden property="schUrl" name="listBean" indexed="true"/>
                                                        <html:hidden property="formName" name="listBean" indexed="true"/>
                                                        <html:hidden property="acType" name="listBean" indexed="true"/>
                                                    <!--</td>
                                                    <td>-->
                                                        <bean:write name="listBean" property="formName"/>
                                                    </td>
                                                </tr>
                                            </logic:iterate>
                                            <tr class="rowLine">
                                                <td align="left">
                                                    <input type="button" value="Print Selected" onclick="printForms()"/>
                                                </td>
                                            </tr>
                                        </table>
                                    </div>
                                </td>
                            </tr>
                            </html:form>
                            </logic:notEmpty>
                            <logic:empty name="grantsGov" property="list" scope="request">
                            <tr><td>
                            <div id="forms" style="overflow:hidden">
                                <table align="center" width="100%" border="0" cellspacing="0" cellpadding="2" class="tabtable">
                                <tr><td>Grants.Gov Forms Not Available.</td></tr>
                                </table>
                            </div>
                            </td></tr>
                            </logic:empty>  
                        </table>
                        <br>
                    </td>
                </tr>
                    <%-- S2S Forms - END --%>
                    
                    
            </tbody>
        </table>
        </form> 
<script>
      var help = '<bean:message bundle="proposal" key="helpTextProposal.Print"/>';
      help = trim(help);
      if(help.length == 0) {
        document.getElementById('helpText').style.display = 'none';
      }
      function trim(s) {
            return s.replace( /^\s*/, "" ).replace( /\s*$/, "" );
      }  
</script>        
    </body>
</html>
