<%-- 
    Document   : reviewerNote.jsp
    Created on : 19 Apr, 2012, 3:50:17 PM
    Author     : indhulekha
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
   "http://www.w3.org/TR/html4/loose.dtd">
<%@ include file="/coeuslite/mit/utils/CoeusContextPath.jsp"%>

<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%
    String path = request.getContextPath();
    String reviewingDisclNum = null;
    Integer reviewingSeqNum = null;
    String reviewerPersonId = null;

    if(request.getAttribute("reviewingDisclNum") != null) {
        reviewingDisclNum = request.getAttribute("reviewingDisclNum").toString();
     }
     if(request.getAttribute("reviewingSeqNum") != null) {
         reviewingSeqNum = Integer.parseInt(request.getAttribute("reviewingSeqNum").toString());
     }
    if(request.getAttribute("reviewerPersonId") != null) {
        reviewerPersonId = request.getAttribute("reviewerPersonId").toString();
    }
%>
<html:html locale="true">
<head>
<title>Reviewer Comment</title>
<script type="text/javascript">
            function saveAndcomplteReview(){
                var success=comments();
                if(success == true){
                 document.forms[0].action= "<%=path%>/saveReviewAction.do?reviewingDisclNum=<%=reviewingDisclNum%>&reviewingSeqNum=<%=reviewingSeqNum%>&reviewerPersonId=<%=reviewerPersonId%>";
                 document.forms[0].submit();
             }
            }

            function saveAndReturnToDisclosure() {
                var success=comments();
                if(success==true){
                document.forms[0].action= "<%=path%>/saveReviewAction.do?reviewingDisclNum=<%=reviewingDisclNum%>&reviewingSeqNum=<%=reviewingSeqNum%>&reviewerPersonId=<%=reviewerPersonId%>&returnToDiscl=true";
                 document.forms[0].submit();
             }
            }

            function saveAndAddNewComment(){
                var success = comments();
                if(success==true){
                 document.forms[0].action= "<%=path%>/saveReviewAction.do?reviewingDisclNum=<%=reviewingDisclNum%>&reviewingSeqNum=<%=reviewingSeqNum%>&reviewerPersonId=<%=reviewerPersonId%>&addNewComment=true";
                 document.forms[0].submit();
            }
            } 
            function  comments()
            {     
                if(document.forms[0].ReviewerComment.value==null || document.forms[0].ReviewerComment.value=='')
                {
                    alert('Please enter the comment');
                     document.forms[0].ReviewerComment.focus();
                      return false;
                }
                return true;
            }  
        </script>
</head>
<body>
	<html:form action="/saveNotesCoiv2.do">
		<div>
			<table style="width: 100%;" height="100%" border="0" cellpadding="0"
				cellspacing="0" class="table" align='center'>
				<tr>
					<td height="20" width="50%" align="left" valign="top"
						class="theader" colspan="3">Reviewer Comment</td>
				</tr>
				<tr>
					<td align="left" class="copybold" style="width: 20%" colspan="3">
						Private Flag : <input TYPE=checkbox name=restrictedView VALUE='R'><br />
					<br />
					</td>
				</tr>
				<tr>
					<td align="left" colspan="3" valign="top"
						style="padding-left: 12px;"><label
						style="vertical-align: top"><b>Comment :</b></label>
					<%--</td>--%> <%--<td align="left" colspan="2" width="90%">--%> <textarea
							id="ReviewerComment" name="ReviewerComment" cols="80" rows="6"></textarea><br />
					</td>

				</tr>

				<tr>
					<td class='savebutton' width="5%" style="padding-left: 78px;">
						<html:button property="Save" styleClass="clsavebutton"
							style="width:185px;" onclick="javaScript:saveAndcomplteReview();">
                                Save & Complete Review
                            </html:button>
					</td>
					<td class='savebutton' width="5%"><html:button property="Save"
							styleClass="clsavebutton" style="width:185px;"
							onclick="javaScript:saveAndReturnToDisclosure();">
                                Save & Return
                            </html:button></td>
					<td class='savebutton'><html:button property="Save"
							styleClass="clsavebutton" style="width:185px;"
							onclick="javaScript:saveAndAddNewComment();">
                                Save & Add New Comment
                            </html:button></td>
				</tr>
			</table>
		</div>
	</html:form>
</body>
</html:html>
