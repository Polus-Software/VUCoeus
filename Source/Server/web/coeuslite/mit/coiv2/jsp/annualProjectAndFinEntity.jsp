<%@ page import="org.apache.struts.validator.DynaValidatorForm,edu.mit.coeuslite.coiv2.formbeans.CoiPersonProjectDetailsForm,java.util.HashMap,edu.mit.coeuslite.coiv2.utilities.CoiConstants,java.util.*,
         edu.mit.coeuslite.coiv2.beans.CoiPersonProjectDetails,java.util.Vector,edu.mit.coeus.coi.bean.ComboBoxBean,org.apache.struts.taglib.html.JavascriptValidatorTag,
         edu.mit.coeuslite.coiv2.services.CoiCommonService,edu.mit.coeuslite.coiv2.beans.CoiProjectEntityDetailsBean,edu.mit.coeuslite.coiv2.beans.CoiFinancialEntityBean,edu.mit.coeuslite.coiv2.beans.CoiAnnualPersonProjectDetails"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" 	prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" 	prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" 	prefix="logic" %>
<%@ taglib uri="/WEB-INF/coeus-utils.tld"	prefix="coeusUtils" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
 
<html xmlns="http://www.w3.org/1999/xhtml">   
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
        <title>C O I</title>
        <link href="<%=request.getContextPath()%>/coeuslite/mit/coiv2/css/layout.css" rel="stylesheet" type="text/css" />
        <link href="<%=request.getContextPath()%>/coeuslite/mit/coiv2/css/styles.css" rel="stylesheet" type="text/css" />
        <link href="<%=request.getContextPath()%>/coeuslite/mit/coiv2/css/collapsemenu.css" rel="stylesheet" type="text/css" />
        <link href="<%=request.getContextPath()%>/coeuslite/dartmouth/utils/css/coeus_styles.css" rel="stylesheet" type="text/css" />
        <style>
            #mbox{background-color:#6e97cf; padding:0px 8px 8px 8px; border:3px solid #095796;}
            #mbm{font-family:sans-serif;font-weight:bold;float:right;padding-bottom:5px;}
            #ol{background-image: url('../coeuslite/mit/utils/scripts/modal/overlay.png');}
            .dialog {display:none}

            * html #ol{background-image:none; filter:progid:DXImageTransform.Microsoft.AlphaImageLoader(src="../coeuslite/mit/utils/scripts/modal/overlay.png", sizingMethod="scale");}

        </style>
        <script src="<%=request.getContextPath()%>/coeuslite/mit/utils/scripts/modal/modaldbox.js"></script>
         <script type="text/javascript">
    function showDialog(txtAreaID)
 {
     var width  = 650;//document.getElementById('divTXTDetails').style.pixelWidth;
     var height = 170;//document.getElementById('divTXTDetails').style.pixelHeight;

     sm('divTXTDetails',width,height);

     var txtValue=document.getElementById(txtAreaID).value;
     
     document.getElementById('TxtAreaComments').innerHTML=txtValue;
     document.getElementById("mbox").style.left="385";//450
     document.getElementById("mbox").style.top="250";//250
 }
</script>
        <%
                
                String[] val = (String[]) request.getAttribute("financialArrayEntityList");
                    String path = request.getContextPath(); 
                 /*  HashMap thisUserRights = (HashMap) request.getAttribute("rights");
                    Integer permissionTYpe = (Integer) thisUserRights.get(CoiConstants.DISCL);
                    boolean disable = true;
                    String disabled="";
                    if (permissionTYpe == 2) {
                        disabled="";
                        disable = false;
                    }*/
                    int totalNumOfPjt = 0;
                    int projectNum = 0;                    
                    if(request.getAttribute("totalProjectSize") != null) {
                           totalNumOfPjt = Integer.parseInt(request.getAttribute("totalProjectSize").toString());
                    }

                    if(request.getAttribute("projectNum") != null) {
                        projectNum = Integer.parseInt(request.getAttribute("projectNum").toString());
                    }                
        %> 
        <html:javascript formName="finEntity"
                         dynamicJavascript="true"
                         staticJavascript="true"/>
    </head>
       <script type="text/javascript">
        var result=[];
       var globinFinEnity=" ";
        var globtext=" ";
              function checkConflict(id,totalIndex){
            debugger;
            //last();
            var s='Conflict';
            globinFinEnity = "str"+totalIndex;

             document.getElementById(globinFinEnity).style.visibility = 'visible';
             document.getElementById(globinFinEnity).style.height = "auto";
            

        }       
        function saveFinEntityConflict()
        {  
             var flag=false;
              var relationFlag=false;
              var exceedsLimit = false;             
               <%
                   Vector financialList = (Vector) request.getAttribute("financialEntityList");
                   int size=financialList.size();

                      for(int i=0;i<size;i++)
                       {
                          CoiFinancialEntityBean finBean = (CoiFinancialEntityBean) financialList.get(i);
                          String typeName = finBean.getCode();
                   %>
                          var id="<%=typeName%>"; 
                          var relationId="orgRelDesc<%=typeName%>";
                          var reldesc = document.getElementById(relationId).value.replace(/^\s+|\s+$/g, '');
                          var relid = "";
                          if(document.getElementById(id).value != null) {
                              relid = document.getElementById(id).value.replace(/^\s+|\s+$/g, '');
                          }
                          
                    if((document.getElementById(id).value==null) ||(document.getElementById(id).value=="") ||(1 > relid.length)){
                            flag=true;
                        }
                    if((document.getElementById(relationId).value==null) ||(document.getElementById(relationId).value == "")||(1 > reldesc.length))
                            {
                               relationFlag=true;
                            }
                    if((document.getElementById(relationId).value.length >4000))
                        {
                           exceedsLimit=true; 
                        }
                   <%  } %>
                       
                    if(flag)
                        {
                              alert("Please select the COI status...!!");
                        }
                         else if(relationFlag)
                            {
                              alert("Please enter the relationship description...!!");
                            }
                         else if(exceedsLimit)
                            {
                              alert("Relationship description should not exceed 4000 characters.");
                            }  
                    else
                        { 
                       var moduleItemKey='<%=request.getAttribute("moduleItemKeyConflict")%>'
                        document.forms[0].action= '<%=path%>' + "/coiSaveConflictStatus.do?moduleItemKey="+moduleItemKey;
                        document.forms[0].submit();
                    }
        }
        function changeStatusCode()
        {
            var str=[];
            var k=0;
        <% for (int i = 0; i < val.length; i++) {%>
                str[k]='<%=val[i]%>';
                k++;
        <% } %>
                for(var i=0; i<str.length; i++)
                {
                    var propertyName=str[i];
                    var value=document.forms[0].coiStatusCode.value;
                    document.getElementById(propertyName).value=value;
                }
            }
              function exitToCoi(){
                var answer = confirm("This operation will discard current changes.  Prior screen entries will be saved.  You may continue this disclosure event by selecting it from the Pending section of your COI home page.  Click 'OK' to continue.");
                     if(answer) {
                        document.location = '<%=request.getContextPath()%>/exitDisclosure.do';
                        window.location;
                     }
             }
      function skipProject()
        {        
           var moduleItemKey='<%=request.getAttribute("moduleItemKeyConflict")%>'
            document.forms[0].action= '<%=path%>' + "/coiSkipProject.do?selectedProject="+moduleItemKey;
            document.forms[0].submit();
        }
    </script>
<html:form action="/annualSaveProjectDetails.do">
   <table id="bodyTable" class="table" style="width: 100%;" border="0">
<%int count = 0;
    String moduleNm = "";
  
      CoiAnnualPersonProjectDetails  coiProjectDetail =(CoiAnnualPersonProjectDetails)request.getAttribute("coiProjectDetail");
            int modulecode=coiProjectDetail.getModuleCode();
         if(coiProjectDetail!=null){ 
            if(coiProjectDetail.getModuleCode()==3){
               moduleNm = "Proposal"; 
            }
            else if(coiProjectDetail.getModuleCode()==2){
               moduleNm = "Institute Proposal"; 
            } 
            else if(coiProjectDetail.getModuleCode()==7){
                moduleNm = "IRB Protocol"; 
             }  
            else if(coiProjectDetail.getModuleCode()==9){
                moduleNm = "IACUC Protocol"; 
            }  
            else if(coiProjectDetail.getModuleCode()==1){
                moduleNm = "Award"; 
            } 
            else if(coiProjectDetail.getModuleCode()==0){
                moduleNm = "Travel"; 
            }                      
        }
%>    

          <tr><td colspan="5"><b>For your project listed below, think about your relationship with all your Financial Entities and update the Financial Interest status as appropriate:</b></td></tr>
          <tr><td align="right" width="15%" style="float: none" nowrap><b><%=moduleNm%> # :</b></td>
              
              <td style="float: none" align="left" width="30%" colspan="4"><bean:write name="coiProjectDetail" property="coiProjectId"/></td>
              
         </tr>
          <tr>
             <%
             String project_Type=(String) request.getAttribute("projectType");
             if((project_Type.equalsIgnoreCase("Annual"))||(project_Type.equalsIgnoreCase("Revision"))){  %>
             <td  align="right"  style="float: none" nowrap><b>Title :</b></td>
             <td style="float: none" align="left" colspan="3"><bean:write name="coiProjectDetail" property="coiProjectTitle"/></td>
            <td align="right" style="padding-right:6px;"><html:button styleClass="clsavebutton"  value="Skip Project" property="skip"  onclick="javaScript:skipProject();"/></td>
              <%}else{ %>
                <td align="right" style="float: none" nowrap><b>Title :</b></td>
                <td style="float: none" align="left" colspan="4"><bean:write name="coiProjectDetail" property="coiProjectTitle"/></td>
              <%}%>
          </tr>

          <tr><td align="right" width="15%" style="float: none" nowrap>
                        <logic:equal value="Protocol" property="coiProjectType" name="coiProjectDetail">
                            <b>Application Date:</b>
                        </logic:equal>
                        <logic:notEqual value="Protocol" property="coiProjectType" name="coiProjectDetail">
                            <b>Start Date:</b>
                        </logic:notEqual></td>
              <td style="float: none" align="left" width="15%"><bean:write name="coiProjectDetail" property="coiProjectStartDate" format="dd-MMM-yyyy"/>
              </td>
              <td align="right" width="23%" style="float: none" nowrap>
                         <logic:equal value="Protocol" property="coiProjectType" name="coiProjectDetail">
                            <b>Expiration Date:</b> 
                        </logic:equal>
                        <logic:notEqual value="Protocol" property="coiProjectType" name="coiProjectDetail">
                            <b>End Date:</b>
                         </logic:notEqual></td>
              <td style="float: none" align="left" width="15%"><bean:write name="coiProjectDetail" property="coiProjectEndDate" format="dd-MMM-yyyy"/>
              </td>
                     <%    
     if((project_Type.equalsIgnoreCase("Annual"))||(project_Type.equalsIgnoreCase("Revision"))){  %>
                      <td nowrap align="right" width="35%">
                          <b>Project <%=projectNum%> of <%=totalNumOfPjt%> projects</b></td>
                  <%} else{%>
                  <td nowrap align="right" width="65%"></td>
                  <%}%>
              </tr> 
<tr><td class="copybold"  colspan="5"><img width="100%" height="2" border="0" src="<%=request.getContextPath()%>/coeusliteimages/line4.gif"></img></td></tr>
    <tr>

 
        <td colspan="4" width="40%" nowrap><b> Set Financial Interest Status for all Financial Entities to:</b>
            <html:select   property="coiStatusCode"  onchange="changeStatusCode();" onclick="changeStatusCode();" style="width:190px" value="--------Please Select---------">
                <html:option value="--------Please Select---------"/>
                <html:options collection="typeList" property="code"    labelProperty="description"  />
            </html:select>
    </td>
    <%
    String projectType=(String) request.getAttribute("projectType");     
     if((projectType.equalsIgnoreCase("Annual"))||(projectType.equalsIgnoreCase("Revision"))){  %>
   
     <td width="35%"  nowrap>
        &nbsp;&nbsp;<html:button styleClass="clsavebutton"  value="Save & Continue" property="save"  onclick="javaScript:saveFinEntityConflict();"/>
        <html:button property="saveAndProceed" styleClass="clsavebutton"  value="Quit" onclick="javascript:exitToCoi()" />
                 </td>
         <%}%>
    </tr>
<tr> <td class="copybold"  colspan="5"><img width="100%" height="2" border="0" src="<%=request.getContextPath()%>/coeusliteimages/line4.gif"></img></td></tr>
<%          Vector finList = (Vector) request.getAttribute("financialEntityList");
            Vector typeList = (Vector) request.getAttribute("typeList");
            int inx=0;
%>
<html:hidden property="moduleItemKey"/>
<%
                            String[] rowColors = {"#D6DCE5", "#DCE5F1"};
                            int i = 0;
                            String rowColor = "";
                            Vector projectDetails = (Vector) request.getAttribute("projectDetails");

%>
<logic:iterate id="financialEntity" name="financialEntityList">
                    <%
                                if (i == 0) {
                                    rowColor = rowColors[0];
                                    i = 1;
                                } else {
                                    rowColor = rowColors[1];
                                    i = 0;
                                }
                    %>
                   <tr>
                        <td bgcolor="#376DAA" colspan="5"><br/>
                            <div style="background-color:#376DAA "/>
                        </td>
                    </tr>

                    <tr class="copybold" bgcolor="#9DBFE9"  class="rowLine">
    <td colspan="5">
        <br/>
                            <div style="font-size: 13; float: left;"><b>Financial Entity:&emsp;&emsp;<bean:write name="financialEntity" property="description"/></b> </div>
                            <div style="font-size: 13; float: right;"><b>COI Status:&nbsp;</b>
                            
                        <%
                                            
                                            CoiFinancialEntityBean typeBean = (CoiFinancialEntityBean) finList.get(count);
                                            String nameType = typeBean.getCode();
                                            String beancodeConflict=null;
                                            count++;
                                            String statusCode = typeBean.getStatusCode().toString();
                                            String comment=typeBean.getRelationshipDescription();
                                            if(comment==null){comment="";}
                                            String orgRelationshipDescription=typeBean.getOrgRelationDescription();
                                            if(orgRelationshipDescription==null){orgRelationshipDescription="";}
                                           
                                            
                                %>
                                <select id="<%=nameType%>" name="<%=nameType%>"  style="width:190px">
                                    <option value=" ">--------Please Select---------</option>
                                    <%
                                                if (typeList != null) {
                                                ComboBoxBean bean;
                                                for (int index = 0; index < typeList.size(); index++) {
                                                bean = (ComboBoxBean) typeList.get(index);
                                                beancodeConflict= bean.getDescription();
                                                String selected = "";
                                                if (statusCode != null && bean.getCode().equals(statusCode)) {
                                                    selected = "selected";
                                                }
                                    %>
                                    <option  <%=selected%> value="<%=bean.getCode()%>" onclick="javascript:checkConflict('<%=beancodeConflict%>','<%= inx%>');"> <%=bean.getDescription() %>

                                    </option>
                                    <% }
                                    }
                                    %>
                                </select>  
                                </div> 
     </td></tr>

                                 <%String commentObj=comment;

        String comments="";
        if(commentObj==null){comments="";}
        else{comments=(String)commentObj;
            if(comments.trim().length()>210)
                {comments=(String)comments.substring(0,210);
                 comments+="<a href=\"javaScript:showDialog('txtAreaCommentId"+nameType+"')\">&nbsp; [...]</a>";

                 }}%>
                 <tr>
                     <td>&nbsp;</td>
                 </tr>
                 <tr><td width="10%"></td><td colspan="4"><b><bean:message bundle="coi" key="financialEntity.Qstn1"/>.</b></td></tr>
                 <tr><td colspan="5"><div style="margin-left:115px;"><font color="black"><i>" <%=comments%> "</i></font></div></td>
                     <textarea  style="display: none" id="relDesc<%=nameType%>" name="relDesc<%=nameType%>"  cols="77" rows="3"><%=comment%></textarea>

</tr>
                     <tr><td width="10%"></td><td colspan="4">

            <%--<input type="hidden" name="actionFrom" value="coiAnnFin"/>--%>
<!--            coeus -3424 starts-->
            <input id="txtAreaCommentId<%=nameType%>" name="finEntDet" type="hidden" value="<%=comment%>" />
<!--            coeus -3424 ends-->
           &nbsp;&nbsp;
    </td></tr> <tr style="height: 8px;"><td></td></tr>
                            <%comment=typeBean.getOrgRelationDescription();if(comment==null){comment="";}%>
                            <tr><td width="13%"></td><td colspan="4"><font color="red" face="bold">* </font><b>Describe the relationship between this project and this entity, include any involvement of students and staff. </b></td></tr>
 <tr><td width="15%"></td><td colspan="4"><b>If there is no relationship, explain why.</b></td></tr>

 <tr><td colspan="5" align="right"><textarea id="orgRelDesc<%=nameType%>" name="orgRelDesc<%=nameType%>" cols="77" rows="8" style="width: 85%;"><%=orgRelationshipDescription.trim()%></textarea></td></tr>
<tr><td class="copybold"  colspan="5"><img width="100%" height="2" border="0" src="<%=request.getContextPath()%>/coeusliteimages/line4.gif" /></td></tr>
                        <%inx++;%>
</logic:iterate> 
                
<tr><td colspan="5">
 <html:button styleClass="clsavebutton"  value="Save & Continue" property="save"  onclick="javaScript:saveFinEntityConflict();"/>
 &nbsp;<html:button property="saveAndProceed" styleClass="clsavebutton"  value="Quit" onclick="javascript:exitToCoi()" />
 </td></tr>
                
</table>
    <div id="divTXTDetails" class="dialog" style="width:auto;overflow: hidden;position:absolute;">
    <table width="100%" border="0" cellpadding="1" cellspacing="1" class="table"  >
            <tr  style="background-color:#6E97CF;font-size:12px;margin: 0;">
                <td style="padding: 2px 0px 5px 0px"> <font color="#FFFFFF" size="2px"><b>Entity Business Focus</b></font></td>
            </tr>
            <tr style="height: 100px;width:200px;">
               <%-- <td align="center"> <textarea id="TxtAreaComments" cols="3" style=" height: 70px;resize:none; width: 430px;" disabled ></textarea></td>--%>
                <td align="left" style="height: 100px;">
                    <div style="height: 100px;width: 650px;overflow-x: scroll; overflow-y: scroll;overflow: auto;">
                    <label id="TxtAreaComments"></label>
                    </div> 
                </td>
            </tr>
           <tr style="background-color:#6E97CF;">
              <td align="center" >
                  <input type="button" value="Close" class="clsavebutton" onclick="hm('divTXTDetails');"/>
              </td>

           </tr>
        </table>
    </div>
</html:form>

</html>
