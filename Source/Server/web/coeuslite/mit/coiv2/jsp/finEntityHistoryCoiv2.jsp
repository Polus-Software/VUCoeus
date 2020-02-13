<%@page contentType="text/html"%>
<%@page pageEncoding="UTF-8"%>

<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/coeus-utils.tld" prefix="coeusUtils"%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
"http://www.w3.org/TR/html4/loose.dtd">

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<%String path = request.getContextPath();%>
<link rel=stylesheet
	href="<%=path%>/coeuslite/mit/utils/css/coeus_styles.css"
	type="text/css">
<title>Financial Entity History</title>
<style>
#mbox {
	background-color: #6e97cf;
	padding: 0px 8px 8px 8px;
	border: 3px solid #095796;
}

#mbm {
	font-family: sans-serif;
	font-weight: bold;
	float: right;
	padding-bottom: 5px;
}

#ol {
	background-image:
		url('../coeuslite/mit/utils/scripts/modal/overlay.png');
}

.dialog {
	display: none
}

* html #ol {
	background-image: none;
	filter: progid:DXImageTransform.Microsoft.AlphaImageLoader(src="../coeuslite/mit/utils/scripts/modal/overlay.png",
		sizingMethod="scale");
}
</style>
<script
	src="<%=request.getContextPath()%>/coeuslite/mit/utils/scripts/modal/modaldbox.js"></script>
<script type="text/javascript">
    function showDialog(txtAreaID,id)
 {
     var divId;    
     if(id == 'EBF') {
         divId = 'entityBusinessFocus';
      }else if(id == 'MITRCS') {
          divId = 'divMITRcs';
     }
     
     var txtValue=document.getElementById(txtAreaID).value;
     parent.showHistoryPopup(txtValue,divId);
 }
</script>
<script>
            function rowHover(rowId, styleName) {
                elemId = document.getElementById(rowId);
                elemId.style.cursor = "hand";
                //If row is selected retain selection style
                //Apply hover Style only if row is not selected

                    elemId.className = styleName;

            }

            function openFinEntity(entityNumber, sequenceNum) {
                var url = "<%=path%>/viewFinEntityMitCoiV2.do?entityNumber="+entityNumber+"&seqNum="+sequenceNum+"&header=no";
                window.open(url); 
//                "History", "resizable=yes, scrollbars=yes, width=1000, height=500, left=100, top = 100");
            }
        </script>

</head>
<body class="table">
	<b>History of changes:</b>
	<logic:equal name="certificatesSize" value="0">
		<table width="100%" align="right" border="0">
			<tr>
				<td align='center'><bean:message bundle="coi"
						key="financialEntity.noFinancialEnt" /></td>
			</tr>
		</table>
	</logic:equal>
	<logic:present name="AnnFinEntityHistory" scope="request">
		<table width="100%" align="center" border="0" cellspacing="0"
			cellpadding="3" class="tabtable">
			<tr>
				<td width="20%" align="left" class="theader" nowrap><bean:message
						bundle="coi" key="label.status" /></td>
				<%--<td width="7%" align="left" class="theader" nowrap><bean:message bundle="coi" key="label.entityRelType"/></td>--%>
				<td width="35%" align="left" class="theader" nowrap>Entity
					Business Focus</td>
				<%--<td width="35%" align="left" class="theader" nowrap><bean:message bundle="coi" key="label.organizationDesc"/></td>--%>
				<td width="35%" align="left" class="theader" nowrap>Use of MIT
					Resources</td>
				<td width="10%" align="left" class="theader" nowrap colspan="4"><bean:message
						bundle="coi" key="label.lastUpdated" /></td>
				<%--      <noscript>
                <td width="5%" align="left" class="theader" nowrap></td>
                </noscript>--%>
			</tr>
			<logic:iterate id="data" name="AnnFinEntityHistory"
				type="org.apache.commons.beanutils.DynaBean" indexId="ctr">

				<tr class="rowLine" id="row<%=ctr%>"
					onmouseover="rowHover('row<%=ctr%>','rowHover rowLine')"
					onmouseout="rowHover('row<%=ctr%>','rowLine')">
					<td width="20%" align="left" class="copy">
						<%--onclick="openFinEntity('<bean:write name="data" property="entityNumber"/>','<bean:write name="data" property="sequenceNum"/>')--%>
						<logic:present name="data" property="statusCode">
							<logic:equal name="data" property="statusCode" value='1'>
								<b><font color="#0A29A4">[<bean:message bundle="coi"
											key="financialEntity.active" />]
								</font></b>
							</logic:equal>

							<logic:notEqual name="data" property="statusCode" value='1'>
								<b> <font color="#0A29A4">[<bean:message bundle="coi"
											key="financialEntity.inactive" />]
								</font></b>
							</logic:notEqual>
						</logic:present>
					</td>

					<%--<td width="7%">
                        <coeusUtils:formatOutput name="data" property="relationShipTypeDesc"/>
                        </td>--%>

					<%Object commentObj=data.get("relationShipDesc");
                     Object comments=commentObj;
        String comment="";
        if(commentObj==null){comment="";}
        else{comment=(String)commentObj;
            if(comment.trim().length()>64)
                {comment=(String)comment.substring(0,64);
                 comment+= "<a href=\"javaScript:showDialog('txtAreaCommentId','EBF')\"> [...]</a>";
                                  
                 }}%>

					<td width="35%"><%=comment%></td>

					<%commentObj=data.get("orgRelnDesc");
        if(commentObj==null){comment="";}
        else{comment=(String)commentObj;
            if(comment.trim().length()>43)
                {comment=(String)comment.substring(0,43)+"[...]";}}%>
					<%--<td width="35%"><%=comment%></td>--%>
					<%Object commentObjt=data.get("resoucreMit");
                     Object resource=commentObjt;
        String resourcesMit="";
        if(commentObjt==null){resourcesMit="";}
        else{resourcesMit=(String)commentObjt;
            if(resourcesMit.trim().length()>64)
                {resourcesMit=(String)resourcesMit.substring(0,64);
                 resourcesMit+= "<a href=\"javaScript:showDialog('txtAreaCommentId1','MITRCS')\"> [...]</a>";

                 }}%>
					<td width="35%"><%=resourcesMit%></td>
					<%--<bean:write name="data" property="resoucreMit"/></td>--%>

					<td width="10%" nowrap><coeusUtils:formatDate name="data"
							formatString="yyyy-MM-dd  hh:mm a" property="updtimestamp" />
						&nbsp;by <bean:write name="data" property="upduser" /></td>
					<td color="#D6DCE5" nowrap
						onclick="openFinEntity('<bean:write name="data" property="entityNumber"/>','<bean:write name="data" property="sequenceNum"/>')">
						<a href style="font-weight: bolder; font-size: small;">View</a>
					</td>
					<td class="copy"><input type="hidden" name="actionFrom"
						value="coiAnnFin" /> <!--            coeus -3424 starts--> <input
						id="txtAreaCommentId" name="finEntDet" type="hidden"
						value="<%=comments%>" /> <!--            coeus -3424 ends-->
						&nbsp;&nbsp;</td>
					<td class="copy"><input type="hidden" name="actionFrom"
						value="coiAnnFin" /> <!--            coeus -3424 starts--> <input
						id="txtAreaCommentId1" name="finEntDet" type="hidden"
						value="<%=resource%>" /> <!--            coeus -3424 ends-->
						&nbsp;&nbsp;</td>
					<noscript>
						<td width="5%" class='copy'><a target="_blank"
							href='<%=path%>/viewFinEntity.do?entityNumber=<bean:write name="data" property="entityNumber"/>&seqNum=<bean:write name="data" property="sequenceNum"/>&header=no'>view</a>
						</td>
					</noscript>
				</tr>

			</logic:iterate>
			</logic:present>
		</table>

		<div id="divTXTDetails" class="dialog"
			style="width: auto; overflow: hidden; position: absolute;">
			<table width="100%" border="0" cellpadding="1" cellspacing="1"
				class="table">
				<tr style="background-color: #6E97CF; font-size: 12px; margin: 0;">
					<td style="padding: 2px 0px 5px 0px"><font color="#333333"
						size="2px"><b>Entity Business Focus</b></font></td>
				</tr>
				<tr style="height: 100px; width: 200px;">
					<%-- <td align="center"> <textarea id="TxtAreaComments" cols="3" style=" height: 70px;resize:none; width: 430px;" disabled ></textarea></td>--%>
					<td align="left" style="height: 100px;">
						<div
							style="height: 100px; width: 650px; overflow-x: scroll; overflow-y: scroll; overflow: auto;">
							<label id="TxtAreaComments"></label>
						</div>
					</td>
				</tr>
				<tr style="background-color: #6E97CF;">
					<td align="center"><input type="button" value="Close"
						class="clsavebutton" onclick="hm('divTXTDetails');" /></td>




				</tr>
			</table>
		</div>
		<div id="divTXTDetails1" class="dialog"
			style="width: auto; overflow: hidden; position: absolute;">
			<table width="100%" border="0" cellpadding="1" cellspacing="1"
				class="table">
				<tr style="background-color: #6E97CF; font-size: 12px; margin: 0;">
					<td style="padding: 2px 0px 5px 0px"><font color="#333333"
						size="2px"><b>Use of MIT resources</b></font></td>
				</tr>
				<tr style="height: 100px; width: 200px;">
					<%-- <td align="center"> <textarea id="TxtAreaComments" cols="3" style=" height: 70px;resize:none; width: 430px;" disabled ></textarea></td>--%>
					<td align="left" style="height: 100px;">
						<div
							style="height: 100px; width: 650px; overflow-x: scroll; overflow-y: scroll; overflow: auto;">
							<label id="TxtAreaComments"></label>
						</div>
					</td>
				</tr>
				<tr style="background-color: #6E97CF;">
					<td align="center"><input type="button" value="Close"
						class="clsavebutton" onclick="hm('divTXTDetails1');" /></td>

				</tr>
			</table>
		</div>
</body>

</html>
