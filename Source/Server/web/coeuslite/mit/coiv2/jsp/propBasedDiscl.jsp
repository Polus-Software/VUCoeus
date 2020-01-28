<%--
    Document   : propBasedDiscl
    Created on : Mar 16, 2010, 7:39:23 PM
    Author     : Sony
--%>

<%@page contentType="text/html" pageEncoding="UTF-8" import="edu.mit.coeuslite.coiv2.beans.CoiDisclosureBean,edu.mit.coeuslite.coiv2.services.CoiCommonService,java.util.Vector,edu.mit.coeuslite.coiv2.beans.CoiProposalBean,org.apache.struts.taglib.html.JavascriptValidatorTag,java.util.HashMap,edu.mit.coeuslite.coiv2.utilities.CoiConstants"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" 	prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" 	prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" 	prefix="logic" %>
<%@ taglib uri="/WEB-INF/coeus-utils.tld"	prefix="coeusUtils" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%--<html xmlns="http://www.w3.org/1999/xhtml">--%>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1" />
    <title>C O I</title>
    <link href="<%=request.getContextPath()%>/coeuslite/mit/coiv2/css/layout.css" rel="stylesheet" type="text/css" />
    <link href="<%=request.getContextPath()%>/coeuslite/mit/coiv2/css/styles.css" rel="stylesheet" type="text/css" />
    <link href="<%=request.getContextPath()%>/coeuslite/mit/coiv2/css/collapsemenu.css" rel="stylesheet" type="text/css" />
    <%String path = request.getContextPath();            
            String disable = "";            
                String operationType = (String) request.getAttribute("operation");
                CoiDisclosureBean disclosureBean = (CoiDisclosureBean) request.getSession().getAttribute("disclosureBeanSession");
                String target = "#";
                //if (disclosureBean == null || disclosureBean.getCoiDisclosureNumber() == null) {
                  //  target = path + "/getCompleteDisclCoiv2.do?projectType=Proposal";
                //} else {
                    target = path + "/coiCommonSave.do";
                //}
                 int index = 0;
    %>
</head>
<script language="javascript" type="text/JavaScript" src="<%=request.getContextPath()%>/coeuslite/mit/utils/scripts/coeusLiteCalendar.js"></script>
<script type="text/javascript">
    var ctxpath='<%=request.getContextPath()%>';
    function addDisclSubmit(count){
         var canContinue=false;
      //  var cc=parseInt(count);
        var cc=parseInt(count);
       
       if(cc > 0){

           for(i=0;i<cc;i++ ){
           if(document.forms[0].checkedPropsalProjects[i] == undefined) {
                if(document.forms[0].checkedPropsalProjects.checked==true){
                    canContinue=true;
                }
           }
          else if(document.forms[0].checkedPropsalProjects[i].checked==true)
             {
                canContinue=true;
                break;
             }
          }
        }
        else{
        if(document.forms[0].checkedPropsalProjects.checked==true)
         {
            canContinue=true;
         }
        }

     if(canContinue==false)
          {
              alert("Please select a proposal to continue");
          }else{
            document.forms[0].action='<%=target%>';
            document.forms[0].submit();
            }
        }
    function showPjtDet(){
        document.getElementById("pjtDet").style.visibility="visible";
        document.getElementById("pjtDet").style.height="180px";
        document.getElementById("pjtDet").style.overflow="visible";
        document.forms[0].coiProjectTitle.focus();
    }
    function validateNewproject()
    {
        if(document.forms[0].coiProjectTitle.value==null || document.forms[0].coiProjectTitle.value=='')
        {
            alert("Please enter a project title");
            document.forms[0].coiProjectTitle.focus();
            return false;
        }
       <%-- if(document.forms[0].coiProjectId.value==null || document.forms[0].coiProjectId.value=='')
        {
            alert("Please enter a project id");
             document.forms[0].coiProjectId.focus();
            return false;
        }--%>
         if(document.forms[0].coiProjectType.value==null || document.forms[0].coiProjectType.value=='')
        {
            alert("Please enter a project type");
             document.forms[0].coiProjectType.focus();
            return false;
        }
        if(document.forms[0].coiProjectSponser.value==null || document.forms[0].coiProjectSponser.value=='')
        {
            alert("Please enter a project sponser");
             document.forms[0].coiProjectSponser.focus();
            return false;
        }
        if(document.forms[0].coiProjectFundingAmount.value==null || document.forms[0].coiProjectFundingAmount.value=='')
        {
            alert("Please enter a funding amount");
             document.forms[0].coiProjectFundingAmount.focus();
            return false;
        }
        if(document.forms[0].coiProjectStartDate.value==null || document.forms[0].coiProjectStartDate.value=='')
        {
            alert("Please enter a start date");
             document.forms[0].coiProjectStartDate.focus();
            return false;
        }else{
        if(document.forms[0].coiProjectEndDate.value==null || document.forms[0].coiProjectEndDate.value=='')
        {
            alert("Please enter a end date");
             document.forms[0].coiProjectEndDate.focus();
            return false;
        }else{
            checkDate();
        }
        }

        return true;
    }
    function saveProject()
    {
        debugger;
        var success=validateNewproject();
        var operationType='<%= request.getAttribute("operation")%>';
        if(success==true){
            document.forms[0].action='<%=path%>'+"/saveNonIntegratedProposals.do?&operation="+operationType;
            document.forms[0].submit();
        }

        }
   function checkDate(){
   debugger;

   var startDate=document.forms[0].coiProjectStartDate.value;
   var endDate=document.forms[0].coiProjectEndDate.value;
    if (Date.parse(startDate) > Date.parse(endDate)) {
      alert("Invalid Date Range!\nStart Date cannot be after End Date!")
       document.forms[0].coiProjectStartDate.focus();

      return false;
    }
      else{
      //alert("Entering to submit");
      var operationType='<%= request.getAttribute("operation")%>';
            document.forms[0].action='<%=path%>'+"/saveNonIntegratedProposals.do?&operation="+operationType;
            document.forms[0].submit();
            //alert("Submitted");
        }

}

function CheckNumeric (e)
{
var key;
key = e.which ? e.which : e.keyCode;
if (key!=8 && key!=9 && key!=46){
if((key>=48 && key<=57 )) {
e.returnValue= true;
}
else
{
if(key!=45  && key!=92 && key!= 47 && key!=37 && key!=38 && key!=39 && key!=40){
alert("please enter Numeric only.select a valid date from date picker");
e.returnValue = false;
}
}
}
}


function CheckFund (e)
{


var key;

key = e.which ? e.which : e.keyCode;
if (key!=8 && key!=9 && key!=46){
if((key>=48 && key<=57)) {
e.returnValue= true;
}
else
{
    if(key==46){
e.returnValue= true;
}else{

alert("please enter valid funding amount.");

e.returnValue = false;
//alert(document.forms[0].coiProjectFundingAmount.value+"ert1")
document.forms[0].coiProjectFundingAmount.value='';
document.forms[0].coiProjectFundingAmount.focus();
//alert(document.forms[0].coiProjectFundingAmount.value+"ert")
}
//alert("1");
}
//alert("2");
}
//alert("3");
}


function isValidDate(dateStr,dateProp)
       {
           debugger;
           var strProperty=dateProp;
           dateStr =fomatToMMDDYY(dateStr);

        if(strProperty=='coiProjectStartDate'){
            document.forms[0].coiProjectStartDate.value='';
             document.forms[0].coiProjectStartDate.value=dateStr;
        }else{
            document.forms[0].coiProjectEndDate.value='';
            document.forms[0].coiProjectEndDate.value=dateStr;
        }
       var datePat = /^(\d{1,2})(\/|-)(\d{1,2})\2(\d{2}|\d{4})$/;
        if(dateStr!=''){
        var matchArray = dateStr.match(datePat); // is the format ok?
        if (matchArray == null)
        {
            alert("Invalid date format. Please enter the date in the MM/DD/YY format (example: 1/15/08) or select a date by clicking the calendar icon.")
            if(strProperty=='coiProjectStartDate'){
                //alert("entering since true");
                document.forms[0].coiProjectStartDate.value='';
                document.forms[0].coiProjectStartDate.focus();
                //document.forms[0].coiProjectStartDate.focus();
            }else{
                 document.forms[0].coiProjectEndDate.value='';
                 document.forms[0].coiProjectEndDate.focus();
            }
            //ctrl.focus();
            return false;
        }
        month = matchArray[1]; // parse date into variables
        day = matchArray[3];
        year = matchArray[4];
        if (month < 1 || month > 12)
        {
        // check month range
            alert("Month must be between 1 and 12.");
            return false;
        }
        if (day < 1 || day > 31)
        {
            alert("Day must be between 1 and 31.");
            return false;
        }
        if ((month==4 || month==6 || month==9 || month==11) && day==31)
        {
            alert("Month "+month+" doesn't have 31 days!")
            return false
        }
        if (month == 2)
        {
        // check for february 29th
            var isleap = (year % 4 == 0 && (year % 100 != 0 || year % 400 == 0));
            if (day>29 || (day==29 && !isleap))
            {
            alert("February " + year + " doesn't have " + day + " days!");
            return false;
            }
        }

        }

        return true; // date is valid
    }

    function isDate(dtStr){
        //alert("Entering")
        //alert(dtStr)
	var daysInMonth = DaysArray(12)
	var pos1=dtStr.indexOf(dtCh)
	var pos2=dtStr.indexOf(dtCh,pos1+1)
	var strDay=dtStr.substring(0,pos1)
	var strMonth=dtStr.substring(pos1+1,pos2)
	var strYear=dtStr.substring(pos2+1)
	strYr=strYear
	if (strDay.charAt(0)=="0" && strDay.length>1) strDay=strDay.substring(1)
	if (strMonth.charAt(0)=="0" && strMonth.length>1) strMonth=strMonth.substring(1)
	for (var i = 1; i <= 3; i++) {
		if (strYr.charAt(0)=="0" && strYr.length>1) strYr=strYr.substring(1)
	}
	month=parseInt(strMonth)
	day=parseInt(strDay)
	year=parseInt(strYr)
	if (pos1==-1 || pos2==-1){
		alert("The date format should be : dd/mm/yyyy")
		return false
	}
	if (strMonth.length<1 || month<1 || month>12){
		alert("Please enter a valid month")
		return false
	}
	if (strDay.length<1 || day<1 || day>31 || (month==2 && day>daysInFebruary(year)) || day > daysInMonth[month]){
		alert("Please enter a valid day")
		return false
	}
	if (strYear.length != 2 || year==0 || year<minYear || year>maxYear){
		alert("Please enter a valid 4 digit year between "+minYear+" and "+maxYear)
		return false
	}
	if (dtStr.indexOf(dtCh,pos2+1)!=-1 || isInteger(stripCharsInBag(dtStr, dtCh))==false){
		alert("Please enter a valid date")
		return false
	}
return true
}

function fomatToMMDDYY(datestr){
    debugger;
    //alert(datestr);
    var strlen=datestr.length;
    //alert(strlen);
    for(var i=0;i<=strlen;i++){
        datestr=datestr.replace("-","/")
   }
   // alert("datestr")
    for(var i=0;i<=strlen;i++){
        datestr=datestr.replace("\\","/")
    }
    return datestr;
}

function checkValue(){
    alert(document.forms[0].coiProjectEndDate.value);
    if(document.forms[0].coiProjectEndDate.value==''){

    }else{
      document.forms[0].coiProjectEndDate.value=='';
    }
}


</script>

<html:form action="/createDisclosureCoiv2.do" method="post" >
<table class="table" style="width: 100%;height:auto;" border="0" >
    <tr><td colspan="5"  align="right" >
    <% if(disable==null ||disable.equals("")){ %>
        <a href="javaScript:showPjtDet();"><u>Add New Proposal Information</u></a>
    <%}%>
    </td></tr>
<logic:notPresent name="proposalList">
<tr><td colspan="5"><p style="color: #ff0000;font-size: 12px;">There are no proposals in the system that requires your COI disclosure.</p></td></tr>
</logic:notPresent>
<logic:present name="proposalList">

<logic:notEmpty name="proposalList">
<tr><td colspan="5">Please select a proposal from the list to create a new COI disclosure</td></tr>
<tr style="background-color:#6E97CF;">
    <td style="width:100px; " ><strong> Proposal #</strong></td>
    <td style="width:auto;" ><strong> Title</strong></td>
    <td style="width:120px; " ><strong> Sponsor</strong></td>
    <td style="width:90px; " ><strong> Start Date</strong></td>
    <td style="width:90px; " ><strong> End Date</strong></td>
</tr>
</logic:notEmpty>

<logic:empty name="proposalList">
<tr><td colspan="5"><p style="color: #ff0000;font-size: 12px;">No Proposal is created for the user</p></td></tr>
</logic:empty>

            <%
                        String[] rowColors = {"#D6DCE5", "#DCE5F1"};
                        int i = 0;
                        String rowColor = "";
            %>
        <logic:present name="invaliddate">
            <logic:messagesPresent>
                <font color="red"  >
                    <bean:write name="invaliddate"/>
                </font>
            </logic:messagesPresent>
        </logic:present>
            <%Vector propsal = (Vector) request.getAttribute("proposalList");%>
<logic:iterate id="allProposals" name="proposalList">
                <%
                            if (i == 0) {
                                rowColor = rowColors[0];
                                i = 1;
                            } else {
                                rowColor = rowColors[1];
                                i = 0;
                            }
                            CoiProposalBean propBean = new CoiProposalBean();
                            String checked = "";
                            if (propsal != null && !propsal.isEmpty()) {
                                propBean = (CoiProposalBean) propsal.get(index);
                                if (propBean.isChecked() == true) {
                                    checked = "checked";
                                }
                            }
                            CoiProposalBean propBean1 = (CoiProposalBean) propsal.get(index);
                               CoiCommonService coiCommonService1 = CoiCommonService.getInstance();
                               String endDate="",startDate="";
                               if(propBean1.getStartDate()!=null){
                                 startDate = coiCommonService1.getFormatedDate(propBean1.getStartDate());
                                                         }
                               if(propBean1.getEndDate()!=null){
                                 endDate = coiCommonService1.getFormatedDate(propBean1.getEndDate());
                                                         }
                            index++;
                %>
<tr bgcolor="<%=rowColor%>" class="rowLine" onmouseover="className='rowHover rowLine'" onmouseout="className='rowLine'" >
    <td>
        <input type="radio" <%=disable%> <%=checked%> name="checkedPropsalProjects" 
               value="<bean:write name="allProposals" property="proposalNumber"/>;<bean:write name="allProposals" property="title"/>;<bean:write name="allProposals" property="sponsorName"/>;<bean:write name="allProposals" property="startDate"/>;<bean:write name="allProposals" property="endDate"/>;<bean:write name="allProposals" property="nonIntegrated"/>;<bean:write name="allProposals" property="totalCost"/>;<bean:write name="allProposals" property="proposalTypeDesc"/>;<bean:write name="allProposals" property="roleName"/>"/>
        <bean:write name="allProposals" property="proposalNumber"/>
    </td>
    <td><bean:write name="allProposals" property="title"/></td>
    <td><bean:write name="allProposals" property="sponsorName"/></td>
    <td><%=startDate%></td>
    <td><%=endDate%></td>
</tr>
</logic:iterate>

<logic:notEmpty name="proposalList">
<tr><td align="left" colspan="5">
    <% String link="javaScript:addDisclSubmit('" + index + "')"; %>
    <html:button styleClass="clsavebutton"  value="Continue" property="save"  onclick="<%=link%>"/>
    </td>
</tr>
</logic:notEmpty>
</logic:present>

<tr bgcolor="#D1E5FF"><td colspan="7">
    <div class="myForm" id="pjtDet" style="visibility: hidden; overflow:hidden; height: 1px;width:700px" align="center">
         <table style="width: 100%">
             <tr><td>&nbsp;</td></tr>
             <tr>
                 <td style="width: 15%;color: #333333;font-family:Arial,Helvetica,sans-serif;font-size:12px;font-weight:bold;">Title</td>
                 <td width="35%"><html:text property="coiProjectTitle" name="coiPersonProjectDetailsForm" styleClass="textbox"  maxlength="180" style="width:144px;"/></td>
                 <td style="color: #333333;font-family:Arial,Helvetica,sans-serif;font-size:12px;font-weight:bold;">Project Role</td>
                 <td width="35%"><html:text property="coiProjectRole" name="coiPersonProjectDetailsForm" styleClass="textbox"  maxlength="180" style="width:144px;"/></td>
             </tr>
             <tr>
                 <%--<td style="color: #333333;font-family:Arial,Helvetica,sans-serif;font-size:12px;font-weight:bold;">Project#</td>
                 <td ><html:text property="coiProjectId" name="coiPersonProjectDetailsForm" styleClass="textbox"  maxlength="20" style="width:144px;"/></td>--%>
                 <td style="color: #333333;font-family:Arial,Helvetica,sans-serif;font-size:12px;font-weight:bold;">Sponsor</td>
                 <td><html:text property="coiProjectSponser" name="coiPersonProjectDetailsForm" styleClass="textbox"  maxlength="200" style="width:144px;"/></td>
             </tr>
<logic:present name="message">
    <logic:equal value="true" name="message">
             <tr>
                 <td colspan="4"><p style="color: #ff0000;font-size: 12px;"><b>Project Id</b> Already Exist.!  &nbsp;Choose Another One. </p></td>
             </tr>
    </logic:equal>
    
    <logic:equal value="true1" name="message">
             <tr>
                 <td colspan="4"><p style="color: #ff0000; font-family:Arial,Helvetica,sans-serif; font-size: 12px;"><b>Project Id</b> Used In CurrentList.! &nbsp;Choose Another One.</p></td>
             </tr>
    </logic:equal>
</logic:present>
             <tr>
                 <td style="color: #333333;font-family:Arial,Helvetica,sans-serif;font-size:12px;font-weight:bold;">Project Type</td>
                 <td><html:select property="coiProjectType" name="coiPersonProjectDetailsForm"  styleClass="textbox-long" style="width:144px;"><html:option value=""><bean:message key="specialReviewLabel.pleaseSelect"/></html:option>
                     <html:options collection="propsalType" property="code" labelProperty="description"  />>
                     </html:select>
                 </td>
                 <td style="color: #333333;font-family:Arial,Helvetica,sans-serif;font-size:12px;font-weight:bold;">Funding Amount</td>
                 <td><html:text property="coiProjectFundingAmount" name="coiPersonProjectDetailsForm" styleClass="textbox"  maxlength="30" style="width:144px;"   onkeypress="CheckFund(event)"/></td>
             </tr>
             <tr>
                 <td style="color: #333333;font-family:Arial,Helvetica,sans-serif;font-size:12px;font-weight:bold;">Project Start Date(mm/dd/yy)</td>
                 <td><html:text property="coiProjectStartDate" styleClass="textbox" name="coiPersonProjectDetailsForm"  maxlength="10"  style="width:144px;"  onkeypress="CheckNumeric(event)" onblur="isValidDate(document.forms[0].coiProjectStartDate.value,'coiProjectStartDate')" />
                          <a id="hlIRBDate" onclick="displayCalendarWithTopLeft('coiProjectStartDate',8,25)" tabindex="32" href="javascript:void(0);" runat="server">
                              <img id="imgIRBDate" title="" height="16" alt=""  src="<%=request.getContextPath()%>/coeusliteimages/cal.gif" width="16" border="0" runat="server" />
                           </a>
                 </td>
                 <td style="color: #333333;font-family:Arial,Helvetica,sans-serif;font-size:12px;font-weight:bold;">Project End Date(mm/dd/yy)</td>
                 <td><html:text  property="coiProjectEndDate" styleClass="textbox" name="coiPersonProjectDetailsForm"  maxlength="10" style="width:144px;"  onkeypress="CheckNumeric(event)" onblur="isValidDate(document.forms[0].coiProjectEndDate.value,'coiProjectEndDate')"/>
                           <a id="hlIRBDate" onclick="displayCalendarWithTopLeft('coiProjectEndDate',8,25)" tabindex="32" href="javascript:void(0);" runat="server">
                               <img id="imgIRBDate" title="" height="16" alt="" src="<%=request.getContextPath()%>/coeusliteimages/cal.gif" width="16" border="0" runat="server"/>
                           </a>
                 </td>
             </tr>
        </table>

<html:button styleClass="clsavebutton" onclick="javaScript:saveProject();" property="button" value="Add to list"></html:button>
</div>

</div></td></tr>
     <logic:messagesPresent>
                <script type="text/javascript" >
                    showPjtDet();
                    count= <%=index%>;
                </script>
     </logic:messagesPresent>



</table>
</html:form>
<%--</html>--%>

