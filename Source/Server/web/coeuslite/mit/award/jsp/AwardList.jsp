<%--
    Document   : Award List
    Created on : Dec 22, 2010, 3:27:01 PM
    Author     : vineetha
--%>


<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@page
	import="java.util.Map,java.util.HashMap,
                java.util.ArrayList,
                java.util.Hashtable,
                javax.servlet.jsp.JspWriter,
                javax.servlet.jsp.JspException,
                java.util.Iterator,java.util.Vector, 
                java.util.Set,
                java.util.HashSet,
                edu.mit.coeuslite.utils.CoeusLiteConstants"%>

<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ include file="/coeuslite/mit/utils/CoeusContextPath.jsp"%>
<jsp:useBean id="childHierarchy" scope="request"
	class="java.util.Vector" />
<jsp:useBean id="awardList" scope="request" class="java.util.Vector" />
<jsp:useBean id="awardColumnNames" scope="session"
	class="java.util.Vector" />
<jsp:useBean id="widthList" scope="session" class="java.util.HashMap" />
<bean:size id="awardListSize" name="awardList" />

<%!
    Vector parentList = new Vector();
    Vector childList = new Vector();
    Vector nodes = new Vector();
    String selectedAward = "";
    Vector columNames = new Vector();
    Vector selectedChildList = new Vector();
    ArrayList awardSubChildList = new ArrayList();
    Vector IteratorChildList = new Vector();
    ArrayList subChildList = new ArrayList();
    HashMap widthLst = new HashMap();
    Map child = new HashMap();
    int childCount = 0;
    String awrdNo = "";
    String value1="";
    int rowCount = 0;
    int totalChildCount = 0;
    boolean childListAdded = false;
    //JspWriter out = pageContext.getOut();

%>

<%
   parentList = (Vector)request.getAttribute("awardList");
   childList = (Vector)request.getAttribute("childHierarchy");
   columNames = (Vector)session.getAttribute("awardColumnNames");
   widthLst = (HashMap) session.getAttribute("widthList");
   IteratorChildList = (Vector)request.getAttribute("childHierarchy");
    String path = request.getContextPath();
    String type = request.getParameter("type");
    if(type == null){
        type = session.getAttribute("type").toString();
     }
    if(type != null) {
        type = type.replaceAll("_", " ");
    }

     
%>

<html:html>
<head>
<title>Award List</title>
<html:base />
<script src="<%=path%>/coeuslite/dartmouth/utils/scripts/divSlide.js"></script>
<script src="<%=path%>/coeuslite/dartmouth/utils/scripts/Balloon.js"></script>
<script language="javascript" type="text/JavaScript"
	src="<bean:write name='ctxtPath'/>/coeuslite/mit/utils/scripts/sorttable.js"></script>

<script type="text/javascript">
            function openGeneralInfo(proposalNumber){
                document.AllAwardRecordFormBean.action = "<%=request.getContextPath()%>/getAwardInfo.do?awardNumber="+proposalNumber;
                alert(document.AllAwardRecordFormBean.action);
                document.AllAwardRecordFormBean.submit();
            }

            function selectProjMinus(childPosition,rowId){
           
        
               var a ;
               var b ;
             
                    a = "imgtoggle"+ rowId;
                    b = "imgtoggleminus"+ rowId;
                   //for activating the plus symbol and blocking the minus img
                        document.getElementById(b).style.display='none';
                        document.getElementById(b).style.height = "1px";
                        document.getElementById(a).style.display='';
                        document.getElementById(a).style.height = "auto";
                        var large=childPosition[0];
                        var small=childPosition[0];
                   //to find the smallest among the list
                   for(var j=1;j<childPosition.length;j++)
                      {
                          if(small>childPosition[j]){small=childPosition[j];}
                      }
                   //to find the largest among the list
                   for(var j=1;j<childPosition.length;j++) 
                      {
                          if(large<childPosition[j]){large=childPosition[j];}
                      }
                     
               var tmp; 
               for(var i=small; i <=large; i++) {
                   tmp=document.getElementById(i);
                    if(tmp!=null){
                        a = "imgtoggle"+ i;
                        b = "imgtoggleminus"+i;
                        var img=document.getElementById(b);
                        if(img!=null){img.style.display='none';img.style.height = "1px";}
                        img= document.getElementById(a);
                        if(img!=null){img.style.display='';img.style.height = "auto";}
                       img=document.getElementById(i);
                       if(img!=null){img.style.display='none';img.style.height = "1px";}
                         
                }
            }
            }

         function selectProjPlus(childPosition,rowId){
               var a;
               var b;
               rowId;
                
                    a = "imgtoggle"+ rowId; 
                    b = "imgtoggleminus"+ rowId;
                     //for activating the minus symbol and blocking the plus img
                        document.getElementById(a).style.display='none';
                        document.getElementById(a).style.height = "1px";
                        document.getElementById(b).style.display='';
                        document.getElementById(b).style.height = "auto";
               
                var tmp;
               for(var i=0; i < childPosition.length; i++) {
                   tmp=document.getElementById(childPosition[i]); 
                   if(tmp!=null){ // alert(childPosition[i].toString());
                        a = "imgtoggle"+childPosition[i]; 
                        b = "imgtoggleminus"+ childPosition[i]; 
                        var img=document.getElementById(b);
                        if(img!=null){img.style.display='none';img.style.height = "1px";}
                        img= document.getElementById(a); 
                        if(img!=null){img.style.display='';img.style.height = "auto";} 
                        tmp.style.display='';tmp.style.height = "auto";
                     } 
                }
        }


         function last(){
          var lastinx = "<%=request.getSession().getAttribute("lastIndex")%>";
          document.getElementById(a).src="<%=path%>/coeusliteimages/minus.gif";
                for(var i=0;i<chldCount;i++){
                document.getElementById(rowId+i).style.visibility = 'hidden';
                document.getElementById(rowId+i).style.height="1px";
                
                }
       }
       </script>
</head>


<html:form action="/getBudget.do">
	<body>
		<%!
      String trBgColor;
        private Vector isChildPresent(String awardNumber) {
             Iterator itr = childList.iterator();
             Vector selectedChildList = new Vector();
             String childAwardNum = "";

             while(itr.hasNext()) {
                 HashMap list = (HashMap)itr.next();
                 String parentNode = list.get("PARENT_MIT_AWARD_NUMBER").toString();

                 if(parentNode.equals(awardNumber)) {
                     childAwardNum = list.get("AWARD_NUMBER").toString();
                     selectedChildList.add(list);
                  }
                 }

             return selectedChildList;
             }

         private int getChildCount(String awardNumber) {
             Iterator itr = childList.iterator();
             int chldCount = 0;
             String childAwardNum = "";

             while(itr.hasNext()) {
                 HashMap list = (HashMap)itr.next();
                 String parentNode = list.get("PARENT_MIT_AWARD_NUMBER").toString();

                 if(parentNode.equals(awardNumber)) {
                     chldCount++;
                  }
                 }

             return chldCount;
             }

          private int getTotalChildCount(String awardNumber) {
             Iterator itr = childList.iterator();
             ArrayList awrdNos = new ArrayList();
             String childAwardNum = "";

             while(itr.hasNext()) {
                 HashMap list = (HashMap)itr.next();
                 String parentNode = list.get("PARENT_MIT_AWARD_NUMBER").toString();
                 String awrdNo = list.get("AWARD_NUMBER").toString();
                 if(parentNode.equals(awardNumber)) {
                     totalChildCount++;
                     awrdNos.add(awrdNo);
                 }
                 }
                 if(awrdNos != null && awrdNos.size() >0) {
                     for(int i=0; i < awrdNos.size(); i++) {
                         String number = awrdNos.get(i).toString();
                         getTotalChildCount(number);
                       }
                     }
             return totalChildCount;
             }
          private ArrayList getChildPosition(String awardNumber) {
                int position = rowCount;
                int tmp,count;
                 
                ArrayList childPosition = new ArrayList();
                Vector subchildList = isChildPresent(awardNumber);
                Iterator itr1 = subchildList.iterator();
                while(itr1.hasNext()) {
                    HashMap map = (HashMap)itr1.next();
                    position = position + 1;
                    childPosition.add(position); 
                    String awrdNum = map.get("AWARD_NUMBER").toString();
                     tmp=totalChildCount;
                     totalChildCount=0;
                     count = getTotalChildCount(awrdNum);
                     totalChildCount=tmp;
                     position=position+count;
                     
                    }
                    childPosition.add(position); 
                    Set positionSet = new HashSet();
                     positionSet.addAll(childPosition);
                     childPosition.clear();
                     childPosition.addAll(positionSet);

              return childPosition;
              }
  private ArrayList getChildPositionForPlusOnly(String awardNumber) {
                int position = rowCount;
                int tmp,count=0;

                ArrayList childPosition = new ArrayList();
                Vector subchildList = isChildPresent(awardNumber);
                Iterator itr1 = subchildList.iterator();
                while(itr1.hasNext()) {
                    HashMap map = (HashMap)itr1.next();
                    position = position + 1;
                    childPosition.add(position);
                    String awrdNum = map.get("AWARD_NUMBER").toString();
                     tmp=totalChildCount;
                     totalChildCount=0;
                     count = getTotalChildCount(awrdNum);
                     totalChildCount=tmp;
                     position=position+count;
                     }
                if(count==0) {childPosition.add(position); }
                    Set positionSet = new HashSet();
                     positionSet.addAll(childPosition);
                     childPosition.clear();
                     childPosition.addAll(positionSet);

              return childPosition;
              }

           %>
		<!-- JM 7-25-2012 set table width to match header -->
		<table width="970" height="100%" border="0" cellpadding="0"
			cellspacing="0" class="table">
			<tr>
				<td align="left" valign="top" width="auto">
					<table width="100%" border="0" align="center" cellpadding="0"
						cellspacing="0" class="tabtable">

						<tr>
							<td colspan="4" align="left" valign="top">
								<table width="100%" height="25" border="0" cellpadding="0"
									cellspacing="0" class="tableheader">

									<tr>
										<td>&nbsp;&nbsp;&nbsp;List of <%=type%>
										</td>
									</tr>
								</table>
							</td>
						</tr>
						<tr>
							<td height="2px">&nbsp;</td>
						</tr>
						<!-- JM 7-19-2012 added background color -->
						<tr align="center" style="background-color: #fedc92;">
							<td>
								<table width="98%" height="100%" border="0" cellpadding="2"
									cellspacing="0" class="table">
									<tr>
										<td class="theader" style="width: 18px;">&nbsp;&nbsp;&nbsp;</td>
										<%
                                     if(awardColumnNames != null && awardColumnNames.size()>0){
                                         for(int index=0;index<awardColumnNames.size();index++){
                                             edu.mit.coeus.search.bean.DisplayBean displayBean = (edu.mit.coeus.search.bean.DisplayBean)awardColumnNames.elementAt(index);
                                             if(displayBean.isVisible()){
                                                 String strColumnName = displayBean.getValue();
                                                 String clmwidth = widthList.get(displayBean.getName()).toString();
                                                 %>
										<td width="<%=clmwidth%>%" class="theader"><%=strColumnName%></td>
										<%
                                             }
                                        }
                                     }
                                 %>

									</tr>
									<%
                                        String strBgColor = "#DCE5F1";
                                        int count = 0;
                                        rowCount = 0;
                                 %>
									<logic:present name="awardList">
										<logic:iterate id="award" name="awardList"
											type="java.util.HashMap">
											<%
                                        if (rowCount%2 == 0){
                                            strBgColor = "#D6DCE5";}
                                        else{
                                           strBgColor="#DCE5F1";}
                                           

                                        String awrdNum = "";
                                        if(awardColumnNames != null && awardColumnNames.size()>0){

                                         for(int index=0;index<awardColumnNames.size();index++){
                                            edu.mit.coeus.search.bean.DisplayBean displayBean = (edu.mit.coeus.search.bean.DisplayBean)awardColumnNames.elementAt(index);

                                          String label = displayBean.getName();
                                         if(label != null && label.equals("AWARD_NUMBER") ){
                                            awrdNum = award.get(label) == null ? "" : award.get(label).toString();
                                            break;
                                         }


                                          }
                                         }
                                        int chldCount = getChildCount(awrdNum);
                                        totalChildCount = 0;
                                        int ttlCount = getTotalChildCount(awrdNum);
                                        ArrayList childPosiForPlus=getChildPositionForPlusOnly(awrdNum);
                                        ArrayList childPosition = getChildPosition(awrdNum);

                                 %>
											<tr bgcolor="<%=strBgColor%>" valign="top"
												onmouseover="className='TableItemOn'"
												onmouseout="className='TableItemOff'" class="TableItemOff">
												<%
                                    if(type.equals("All Parent Awards") || type.equals("All Active Parent Awards") || chldCount == 0) {
                                        %>
												<td style="width: 18px;">&nbsp;&nbsp;&nbsp;</td>
												<%
                                        } else {
                                %>
												<td><img
													src='<%=request.getContextPath()%>/coeusliteimages/plus.gif'
													border='none' style="overflow: hidden;"
													id="imgtoggle<%=rowCount%>" name="imgtoggle<%=rowCount%>"
													border="none"
													onclick="javascript:selectProjPlus(<%=childPosiForPlus%>,<%=rowCount%>); " />
													<img
													src='<%=request.getContextPath()%>/coeusliteimages/minus.gif'
													style="display: none; overflow: hidden;" border='none'
													id="imgtoggleminus<%=rowCount%>"
													name="imgtoggleminus<%=rowCount%>" border="none"
													onclick="javascript:selectProjMinus(<%=childPosition%>,<%=rowCount%>);" />
												</td>

												<% }
                                    rowCount++;
                                    String awardNumber="";
                                     if(awardColumnNames != null && awardColumnNames.size()>0){

                                         for(int index=0;index<awardColumnNames.size();index++){
                                            edu.mit.coeus.search.bean.DisplayBean displayBean = (edu.mit.coeus.search.bean.DisplayBean)awardColumnNames.elementAt(index);
                                            if(!displayBean.isVisible())
                                                continue;
                                            
                                            String key = displayBean.getName();
                                            String clmwidth = widthLst.get(displayBean.getName()).toString();
                                            if(key != null){
                                                String value = award.get(key) == null ? "" : award.get(key).toString();
                                                int clWidthIntxy=Integer.parseInt(clmwidth);
                                                clWidthIntxy=clWidthIntxy*2;
                                                if(index == 0){awardNumber=value;}
                                                else{
                                                   if((key.equals("ANTICIPATED_TOTAL_AMOUNT"))||(key.equals("ANT_DISTRIBUTABLE_AMOUNT"))||(key.equals("OBLI_DISTRIBUTABLE_AMOUNT"))){
                                                    double tmpValue= Double.parseDouble(value);
                                                    value=java.text.NumberFormat.getCurrencyInstance(java.util.Locale.US).format(tmpValue);
                                                    }
                                                   
                                                    else if(value != null && value.length() > clWidthIntxy){
                                                        value = value.substring(0,clWidthIntxy-5);
                                                        value = value+" ...";
                                                    }
                                                }%>
												<td style="white-space: normal;" align="left"
													width="<%=clmwidth%>%"><a
													href="<%=request.getContextPath()%>/getAwardInfo.do?awardNumber=<%=awardNumber%>"><u><%=value%>
													</u></a></td>
												<% 
                                            }
                                        }
                                     }
                                 %>

											</tr>
											<%
                              String strBgColorChild="ADDFFF";
                            //  String strBgColorChildSecond="#A0CFEC";
                              trBgColor=strBgColorChild;
                              createChildHierarchy(awardNumber,out,request); 
                              
                                %>
										</logic:iterate>
										<%request.getSession().setAttribute("lastIndex",rowCount);%>
									</logic:present>
								</table> <%!
                                    private void createChildHierarchy(String awrdNum, JspWriter out,HttpServletRequest request) {
                                       selectedAward =  awrdNum;
                                       selectedChildList = isChildPresent(selectedAward);
                                      
                                       String currentAwardNo = "";
                                       //String strBgColorChildFirst="#ADDFFF";
                                      // String strBgColorChildSecond="#A0CFEC";
                                       String strBgColor = "#"+trBgColor;
                                       
                                       try{
                                       
                                       Integer clr= Integer.parseInt(trBgColor, 16);
                                       clr+=Integer.parseInt("a0000",16); 
                                       trBgColor=Integer.toHexString(clr);
                                       int chldCount = 0;
                                      if(selectedChildList != null && selectedChildList.size() > 0) {
                                          
                                        Iterator itr = selectedChildList.iterator();
                                        while(itr.hasNext()) {
                                            child = (HashMap)itr.next();
                                        
                                            for(int index=0;index<columNames.size();index++){
                                                edu.mit.coeus.search.bean.DisplayBean displayBean = (edu.mit.coeus.search.bean.DisplayBean)columNames.elementAt(index);
                                                String label = displayBean.getName();
                                              if(label != null && label.equals("AWARD_NUMBER")){
                                               currentAwardNo = child.get(label) == null ? "" : child.get(label).toString();
                                               break;
                                              }
                                           }
                                            chldCount = getChildCount(currentAwardNo);
                                            totalChildCount = 0;
                                            int ttlCount;
                                            ttlCount = getTotalChildCount(awrdNum);
                                            ArrayList childPosition = getChildPosition(currentAwardNo);
                                            ArrayList childPosiForPlus=getChildPositionForPlusOnly(currentAwardNo);
                                          
                                           // if (rowCount%2 == 0){strBgColor = "#D6DCE5";}
                                          //  else                {strBgColor="#DCE5F1";}
                                            out.println("<tr id="+rowCount+" style=\"height:1px;display:none; \" bgcolor=\"#DCE5F1\"  onmouseover=\"className='TableItemOn'\" onmouseout=\"className='TableItemOff'\" class=\"TableItemOff\">");
                                            out.println("<td>");
                                            if(chldCount > 0) {
                                               out.println("<img src=\""+request.getContextPath()+"/coeusliteimages/plus.gif\" border=\"none\" style=\"overflow: hidden;\" id=\"imgtoggle"+rowCount+"\" name=\"imgtoggle"+rowCount+"\" border=\"none\" onclick=\"javascript:selectProjPlus("+childPosiForPlus+","+rowCount+");\"/>");
                                               out.println("<img src=\""+request.getContextPath()+"/coeusliteimages/minus.gif\" border=\"none\" style=\"display:  none;overflow: hidden;\" id=\"imgtoggleminus"+rowCount+"\" name=\"imgtoggleminus"+rowCount+"\" border=\"none\" onclick=\"javascript:selectProjMinus("+childPosition+","+rowCount+");\"/>");
                                            }
                                            else {out.println("&nbsp;"); }
                                           out.println("</td>");

                                           rowCount++;
                                              if(columNames != null && columNames.size()>0){
                                                 for(int index=0;index<columNames.size();index++){
                                                    edu.mit.coeus.search.bean.DisplayBean displayBean = (edu.mit.coeus.search.bean.DisplayBean)columNames.elementAt(index);
                                                    if(!displayBean.isVisible())
                                                        continue;
                                                    String key = displayBean.getName();
                                                    String clwidth = widthLst.get(displayBean.getName()).toString();
                                                 int clWidthInt=Integer.parseInt(clwidth);
                                                clWidthInt=clWidthInt*2;
                                                       if(key != null){
                                                        value1 = child.get(key) == null ? "" : child.get(key).toString();
                                                        if(index == 0){awrdNo=value1;}
                                                        else{
                                                           if((key.equals("ANTICIPATED_TOTAL_AMOUNT"))||(key.equals("ANT_DISTRIBUTABLE_AMOUNT"))||(key.equals("OBLI_DISTRIBUTABLE_AMOUNT"))){
                                                    double tmpValue1= Double.parseDouble(value1);
                                                    value1=java.text.NumberFormat.getCurrencyInstance(java.util.Locale.US).format(tmpValue1);
                                                    }
                                                   
                                                    else if(value1 != null && value1.length() > clWidthInt){
                                                                value1 = value1.substring(0,clWidthInt-5);
                                                                value1 = value1+" ..."; 
                                                            }
                                                  }
   
                                                        //width adjust      
                                                        out.println("<td style=\"white-space:normal;\"  align=\"left\" width=\""+clwidth+"%\">");
                                                        out.println("<a href=\""+request.getContextPath()+"/getAwardInfo.do?awardNumber="+awrdNo+"\"><u>"+value1 +"</u></a>");
                                                        out.println("</td>");
                                                     }
                                                    }
                                                 }
                                        out.println("</tr>");
                                        
                                       
                                        createChildHierarchy(currentAwardNo,out,request); 
                                                  }

                                     

                                 
                                     
                                      }}catch(Exception ie) {
                                        }

                                     
                                     }

                                     %>
							</td>
						</tr>
						<logic:lessEqual name="awardListSize" value="0">
							<tr>
								<td colspan='3' height="23" align=center>
									<div style="color: black">No Awards found</div>
								</td>
							</tr>
						</logic:lessEqual>

					</table>

				</td>
			</tr>
			<tr>
				<td height='10'>&nbsp;</td>
			</tr>
		</table>
	</body>
</html:form>
</html:html>
