
<%--
    Document   : coiHome
    Created on : Mar 10, 2010, 12:39:42 PM
    Author     : Sony
--%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN"
    "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<%@page
	import="java.util.Vector,edu.mit.coeuslite.coiv2.beans.CoiDisclosureBean,
        edu.mit.coeuslite.coiv2.services.CoiCommonService,
        java.util.Date,
        edu.mit.coeuslite.utils.bean.SubHeaderBean,
        edu.mit.coeuslite.coiv2.beans.CoiDisclosureDetailsListBean,java.util.Calendar,java.text.SimpleDateFormat,
        java.util.TreeSet;"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/coeus-utils.tld" prefix="coeusUtils"%>
<%
String path = request.getContextPath();
%>

<script src="<%=path%>/coeuslite/mit/utils/scripts/divSlide.js"></script>
<script src="<%=path%>/coeuslite/mit/utils/scripts/Balloon.js"></script>
<link href="<%=path%>/coeuslite/mit/utils/css/coeus_styles.css"
	rel="stylesheet" type="text/css" />
<link
	href="<%=request.getContextPath()%>/coeuslite/mit/coiv2/css/layout.css"
	rel="stylesheet" type="text/css" />
<link
	href="<%=request.getContextPath()%>/coeuslite/mit/coiv2/css/styles.css"
	rel="stylesheet" type="text/css" />
<link
	href="<%=request.getContextPath()%>/coeuslite/mit/coiv2/css/collapsemenu.css"
	rel="stylesheet" type="text/css" />
<%
            Vector pendingDisclosure = (Vector) request.getAttribute("pendingDiscl");
            Vector approvedDisclosureView = (Vector) request.getAttribute("ApprovedDisclDetView");

            boolean isDue = false;
            if(session.getAttribute("isReviewDue") != null) {
                isDue = (Boolean)session.getAttribute("isReviewDue");
           }
//String path = request.getContextPath();
%>
<html:html locale="true">
<head>
<title>Disclosure Notes</title>

<script type="text/javascript">
            var glob = "null";
            var glob2 = "null";
             var appGlob1 = null;
             var appGlob2 = null;
             var indexapp;
            var a = "null";
            function getPendingDisclosure(coiDisclosureNumber,sequenceNumber,personId)
            {
                document.forms[0].action= '<%=path%>'+"/getAnyDisclosure.do?";
                document.forms[0].submit();
            }
            function onEdit(disclosure,sequnce)
            {
                document.forms[0].action= '<%=path%>'+"/getSelectedDisclosure.do?";
                document.forms[0].submit();
            }




        function selectProjIcon(){
        debugger;

        if(document.getElementById("disclosureByProjectDiv").style.display == 'block'){
        document.getElementById("projectimgtoggle").src="<%=path%>/coeusliteimages/plus.gif";
        document.getElementById("disclosureByProjectDiv").style.display = 'none';
        document.getElementById("disclosureByProjectDiv").style.height = "1px";
        document.getElementById("disclosureByFinancialEntityDiv").style.display = 'none';
        document.getElementById("disclosureByFinancialEntityDiv").style.height ="1px";
        document.getElementById(glob).style.visibility = 'hidden';
        document.getElementById(glob).style.height = "1px";
        document.getElementById(glob2).style.visibility = 'hidden';
        document.getElementById(glob2).style.height = "1px";
        document.getElementById("imgtoggle"+glob).src="<%=path%>/coeusliteimages/plus.gif";
        }else{
        if(document.getElementById("disclosureByFinancialEntityDiv").style.display == 'block'){
        document.getElementById("projectimgtoggle").src="<%=path%>/coeusliteimages/plus.gif";
        document.getElementById("disclosureByProjectDiv").style.display = 'none';
        document.getElementById("disclosureByProjectDiv").style.height = "1px";
        document.getElementById("disclosureByFinancialEntityDiv").style.display = 'none';
        document.getElementById("disclosureByFinancialEntityDiv").style.height ="1px";
        document.getElementById(glob).style.visibility = 'hidden';
        document.getElementById(glob).style.height = "1px";
        document.getElementById(glob2).style.visibility = 'hidden';
        document.getElementById(glob2).style.height = "1px";
        document.getElementById(a).src="<%=path%>/coeusliteimages/plus.gif";
        }else{
        document.getElementById("projectimgtoggle").src="<%=path%>/coeusliteimages/minus.gif";
        setViewByProjects();
        }
}

}
            function selectProjects(index){
            debugger;
                var a = "imgtoggleproject"+index;
                glob2 = index;
                index = 'project'+index;
                glob = index;
                if(document.getElementById(index).style.visibility == 'visible'){
                    document.getElementById(index).style.visibility = 'hidden';
                    document.getElementById(index).style.height = "1px";
                    document.getElementById(a).src="<%=path%>/coeusliteimages/plus.gif";
                }else{
                    lastProj();
                    ds = new DivSlider();
                    ds.showDiv(index,1000);
                    document.getElementById(index).style.visibility = 'visible';
                    document.getElementById(index).style.height = "100px";
                    document.getElementById(a).src="<%=path%>/coeusliteimages/minus.gif";
                }
            }

      //------------------------------ Managed Disclosure -----------------------------//

           function selectManagedDiscIcon(){
                debugger;

                if(document.getElementById("managedDisclosureDiv").style.display == "block"){
                    document.getElementById("managedimgtoggle").src="<%=path%>/coeusliteimages/plus.gif";
                    document.getElementById("managedDisclosureDiv").style.display = 'none';
                    document.getElementById("managedDisclosureDiv").style.height ="1px";
                    document.getElementById("disclosureByManagedFinancialEntityDiv").style.display = 'none';
                    document.getElementById("disclosureByManagedFinancialEntityDiv").style.height ="1px";
                    document.getElementById(glob).style.visibility = 'hidden';
                    document.getElementById(glob).style.height = "1px";
                    document.getElementById(glob2).style.visibility = 'hidden';
                    document.getElementById(glob2).style.height = "1px";
                    document.getElementById("imgtoggleMngd"+glob).src="<%=path%>/coeusliteimages/plus.gif";
                }else{
                    if(document.getElementById("disclosureByManagedFinancialEntityDiv").style.display == 'block'){
                        document.getElementById("disclosureByManagedFinancialEntityDiv").style.display = 'none';
                        document.getElementById("disclosureByManagedFinancialEntityDiv").style.height ="1px";
                        document.getElementById("managedimgtoggle").src="<%=path%>/coeusliteimages/plus.gif";
                        document.getElementById(glob).style.visibility = 'hidden';
                        document.getElementById(glob).style.height = "1px";
                        document.getElementById(glob2).style.visibility = 'hidden';
                        document.getElementById(glob2).style.height = "1px";

                        document.getElementById("imgtoggleMngd"+glob).src="<%=path%>/coeusliteimages/plus.gif";


                    }else {
                         document.getElementById("managedDisclosureDiv").style.display = "block";
                         document.getElementById("managedDisclosureDiv").style.height = "100%";
                         document.getElementById("managedimgtoggle").src="<%=path%>/coeusliteimages/minus.gif";
                    }
                }
              }

              function setViewByManagedFinancialEnity(){
                debugger;

                document.getElementById("managedDisclosureDiv").style.display = 'none';
                document.getElementById("managedDisclosureDiv").style.height ="1px";
                document.getElementById("disclosureByManagedFinancialEntityDiv").style.display = 'block';
                document.getElementById("disclosureByManagedFinancialEntityDiv").style.height ="auto";

               var imgplus = "imgtoggleMngd"+indexapp;
                document.getElementById(imgplus).src="<%=path%>/coeusliteimages/plus.gif";
                document.getElementById(appGlob1).style.visibility = "hidden";
                document.getElementById(appGlob1).style.height = "1px";
            }

             function selectManagedDiscEntityView(index){
                debugger;
                a = "imgtoggleMngd"+index;

               if(document.getElementById("mngdProject"+index).style.visibility == "visible"){

                 document.getElementById("trMngd"+index).style.visibility = "hidden";
                    document.getElementById("trMngd"+index).style.height = "1px";
                    document.getElementById("mngdProject"+index).style.visibility = "hidden";
                    document.getElementById("mngdProject"+index).style.height = "1px";
                     document.getElementById(a).src="<%=path%>/coeusliteimages/plus.gif";
                }else{
                    <%--lastAppProj();
                    ds = new DivSlider();
                    ds.showDiv(index,1000);--%>
                    document.getElementById(a).src="<%=path%>/coeusliteimages/minus.gif";
                    document.getElementById("trMngd"+index).style.visibility = "visible";
                    document.getElementById("trMngd"+index).style.height = "auto";
                    document.getElementById("mngdProject"+index).style.visibility = "visible";
                    document.getElementById("mngdProject"+index).style.height = "auto";

                }
            }

               function setViewByProjectsManaged(){
                debugger;
                document.getElementById("disclosureByManagedFinancialEntityDiv").style.display = 'none';
                document.getElementById("disclosureByManagedFinancialEntityDiv").style.height ="1px";
                document.getElementById("managedDisclosureDiv").style.display = 'block';
                document.getElementById("managedDisclosureDiv").style.height ="auto";

                  var imgprojplus = "imgMngdtoggle"+indexapp;
                document.getElementById(imgprojplus).src="<%=path%>/coeusliteimages/plus.gif";
                document.getElementById(appGlob2).style.visibility = "hidden";
                document.getElementById(appGlob2).style.height = "1px";
            }


             function getManagedDiscPjtView(index) {
                    img ="imgMngdtoggle"+index;
                    appGlob1 = "mngdProject"+index;
                    appGlob2 = "entMngd"+index;
                    indexapp = index;

                 if(document.getElementById("entMngd"+index).style.visibility == "visible"){
                     document.getElementById("rowMngd"+index).style.visibility = "hidden";
                    document.getElementById("rowMngd"+index).style.height = "1px";
                    document .getElementById("entMngd"+index).style.visibility = "hidden";
                    document.getElementById("entMngd"+index).style.height = "1px";
                    document.getElementById(img).src="<%=path%>/coeusliteimages/plus.gif";

                }else{
                     <%--lastApp();
                    ds = new DivSlider();
                    ds.showDiv(index,1000);--%>

                    document.getElementById(img).src="<%=path%>/coeusliteimages/minus.gif";
                    document.getElementById("entMngd"+index).style.visibility = "visible";
                    document.getElementById("entMngd"+index).style.height = "auto";
                    document.getElementById("rowMngd"+index).style.visibility = "visible";
                    document.getElementById("rowMngd"+index).style.height = "auto";
                  }
                }
    //------------------------------ Managed Disclosure End-----------------------------//

//------------------------------ pending Disclosure -----------------------------//
              function selectPendingDiscIcon(){
                debugger;

               if(document.getElementById("pendingDisclosureDiv").style.display == "block"){
                    document.getElementById("pendingimgtoggle").src="<%=path%>/coeusliteimages/plus.gif";
                    document.getElementById("pendingDisclosureDiv").style.display = 'none';
                    document.getElementById("pendingDisclosureDiv").style.height ="1px";
                    //document.getElementById("disclosureByPendingFinancialEntityDiv").style.display = 'none';
                 //   document.getElementById("disclosureByPendingFinancialEntityDiv").style.height ="1px";
                 //   document.getElementById(glob).style.visibility = 'hidden';
                 //   document.getElementById(glob).style.height = "1px";
                //    document.getElementById(glob2).style.visibility = 'hidden';
                //    document.getElementById(glob2).style.height = "1px";
                    document.getElementById("imgtogglepend"+glob).src="<%=path%>/coeusliteimages/plus.gif";
                }else{
                    <%--if(document.getElementById("disclosureByPendingFinancialEntityDiv").style.display == 'block'){
                        document.getElementById("disclosureByPendingFinancialEntityDiv").style.display = 'none';
                        document.getElementById("disclosureByPendingFinancialEntityDiv").style.height ="1px";
                        document.getElementById("pendingimgtoggle").src="<%=path%>/coeusliteimages/plus.gif";
                        document.getElementById(glob).style.visibility = 'hidden';
                        document.getElementById(glob).style.height = "1px";
                        document.getElementById(glob2).style.visibility = 'hidden';
                        document.getElementById(glob2).style.height = "1px";

                        document.getElementById("imgtogglepend"+glob).src="<%=path%>/coeusliteimages/plus.gif";


                    }else {--%>
                         document.getElementById("pendingDisclosureDiv").style.display = "block";
                         document.getElementById("pendingDisclosureDiv").style.height = "100%";
                         document.getElementById("pendingimgtoggle").src="<%=path%>/coeusliteimages/minus.gif";
                    }
                //}
              }

               function setViewByPendingFinancialEnity(){
                debugger;

                document.getElementById("pendingDisclosureDiv").style.display = 'none';
                document.getElementById("pendingDisclosureDiv").style.height ="1px";
                document.getElementById("disclosureByPendingFinancialEntityDiv").style.display = 'block';
                document.getElementById("disclosureByPendingFinancialEntityDiv").style.height ="auto";

               var imgplus = "imgtogglepend"+indexapp;
                document.getElementById(imgplus).src="<%=path%>/coeusliteimages/plus.gif";
                document.getElementById(appGlob1).style.visibility = "hidden";
                document.getElementById(appGlob1).style.height = "1px";
            }

             function selectPendingDiscEntityView(index){
                debugger;
                a = "imgtogglepend"+index;

               if(document.getElementById("pendProject"+index).style.visibility == "visible"){

                 document.getElementById("trpend"+index).style.visibility = "hidden";
                    document.getElementById("trpend"+index).style.height = "1px";
                    document.getElementById("pendProject"+index).style.visibility = "hidden";
                    document.getElementById("pendProject"+index).style.height = "1px";
                     document.getElementById(a).src="<%=path%>/coeusliteimages/plus.gif";
                }else{
                    <%--lastAppProj();
                    ds = new DivSlider();
                    ds.showDiv(index,1000);--%>
                    document.getElementById(a).src="<%=path%>/coeusliteimages/minus.gif";
                    document.getElementById("trpend"+index).style.visibility = "visible";
                    document.getElementById("trpend"+index).style.height = "auto";
                    document.getElementById("pendProject"+index).style.visibility = "visible";
                    document.getElementById("pendProject"+index).style.height = "auto";

                }
            }

               function setViewByProjectsPending(){
                debugger;
                document.getElementById("disclosureByPendingFinancialEntityDiv").style.display = 'none';
                document.getElementById("disclosureByPendingFinancialEntityDiv").style.height ="1px";
                document.getElementById("pendingDisclosureDiv").style.display = 'block';
                document.getElementById("pendingDisclosureDiv").style.height ="auto";

                  var imgprojplus = "imgPendtoggle"+indexapp;
                document.getElementById(imgprojplus).src="<%=path%>/coeusliteimages/plus.gif";
                document.getElementById(appGlob2).style.visibility = "hidden";
                document.getElementById(appGlob2).style.height = "1px";
            }


             function getPendingDiscPjtView(index) {
                    img ="imgPendtoggle"+index;
                    appGlob1 = "pendProject"+index;
                    appGlob2 = "entPend"+index;
                    indexapp = index;

                 if(document.getElementById("entPend"+index).style.visibility == "visible"){
                    document.getElementById("rowpend"+index).style.visibility = "hidden";
                    document.getElementById("rowpend"+index).style.height = "1px";
                    document .getElementById("entPend"+index).style.visibility = "hidden";
                    document.getElementById("entPend"+index).style.height = "1px";
                    document.getElementById(img).src="<%=path%>/coeusliteimages/plus.gif";

                }else{
                     <%--lastApp();
                    ds = new DivSlider();
                    ds.showDiv(index,1000);--%>

                    document.getElementById(img).src="<%=path%>/coeusliteimages/minus.gif";
                    document.getElementById("entPend"+index).style.visibility = "visible";
                    document.getElementById("entPend"+index).style.height = "auto";
                    document.getElementById("rowpend"+index).style.visibility = "visible";
                    document.getElementById("rowpend"+index).style.height = "auto";
                  }
                }
  //-------------------------- Pending Disclosure End --------------------------------//

 // -------------------------Approved Disclosure List ----------------------------//

               function selectApprovedDiscIcon(){
                debugger;

            if(document.getElementById("approvedDisclosureDiv").style.display == "block"){
                    document.getElementById("approvedimgtoggle").src="<%=path%>/coeusliteimages/plus.gif";
                    document.getElementById("approvedDisclosureDiv").style.display = 'none';
                    document.getElementById("approvedDisclosureDiv").style.height ="1px";
                    document.getElementById("disclosureByApprvdFinancialEntityDiv").style.display = 'none';
                    document.getElementById("disclosureByApprvdFinancialEntityDiv").style.height ="1px";
                    document.getElementById(glob).style.visibility = 'hidden';
                    document.getElementById(glob).style.height = "1px";
                    document.getElementById(glob2).style.visibility = 'hidden';
                    document.getElementById(glob2).style.height = "1px";
                    document.getElementById("imgtoggleapp"+glob).src="<%=path%>/coeusliteimages/plus.gif";
                }else{
                    if(document.getElementById("disclosureByApprvdFinancialEntityDiv").style.display == 'block'){
                        document.getElementById("disclosureByApprvdFinancialEntityDiv").style.display = 'none';
                        document.getElementById("disclosureByApprvdFinancialEntityDiv").style.height ="1px";
                        document.getElementById("approvedimgtoggle").src="<%=path%>/coeusliteimages/plus.gif";
                        document.getElementById(glob).style.visibility = 'hidden';
                        document.getElementById(glob).style.height = "1px";
                        document.getElementById(glob2).style.visibility = 'hidden';
                        document.getElementById(glob2).style.height = "1px";

                        document.getElementById("imgtoggleapp"+glob).src="<%=path%>/coeusliteimages/plus.gif";


                    }else {
                         document.getElementById("approvedDisclosureDiv").style.display = "block";
                         document.getElementById("approvedDisclosureDiv").style.height = "100%";
                         document.getElementById("approvedimgtoggle").src="<%=path%>/coeusliteimages/minus.gif";
                    }
                }
           //     setViewByProjectsApp();
              }

               function selectApprvdDiscEntityView(index){
                debugger;
                a = "imgtoggleapp"+index;
                indexapp1 = index;
                appGlob1 = "project"+index

               if(document.getElementById("project"+index).style.visibility == "visible"){

                 document.getElementById("tr"+index).style.visibility = "hidden";
                    document.getElementById("tr"+index).style.height = "1px";
                    document.getElementById("project"+index).style.visibility = "hidden";
                    document.getElementById("project"+index).style.height = "1px";
                     document.getElementById(a).src="<%=path%>/coeusliteimages/plus.gif";
                }else{
                    <%--lastAppProj();
                    ds = new DivSlider();
                    ds.showDiv(index,1000);--%>
                    document.getElementById(a).src="<%=path%>/coeusliteimages/minus.gif";
                    document.getElementById("tr"+index).style.visibility = "visible";
                    document.getElementById("tr"+index).style.height = "auto";
                    document.getElementById("project"+index).style.visibility = "visible";
                    document.getElementById("project"+index).style.height = "auto";

                }
            }

                 function getApprvdDiscPjtView(index) {
                    img ="imgApptoggle"+index;
                    appGlob1 = "project"+index;
                    appGlob2 = "ent"+index;
                    indexapp = index;


                 if(document.getElementById("ent"+index).style.visibility == "visible"){

                    document.getElementById("row"+index).style.visibility = "hidden";
                    document.getElementById("row"+index).style.height = "1px";
                    document .getElementById("ent"+index).style.visibility = "hidden";
                    document.getElementById("ent"+index).style.height = "1px";
                     document.getElementById(img).src="<%=path%>/coeusliteimages/plus.gif";
                }else{
                     <%--lastApp();
                    ds = new DivSlider();
                    ds.showDiv(index,1000);--%>
                    document.getElementById(img).src="<%=path%>/coeusliteimages/minus.gif";
                    document.getElementById("row"+index).style.visibility = "visible";
                    document.getElementById("row"+index).style.height = "auto";
                    document.getElementById("ent"+index).style.visibility = "visible";
                    document.getElementById("ent"+index).style.height = "auto";

                }
                }

                 function setViewByProjectsApp(){
                debugger;
                document.getElementById("disclosureByApprvdFinancialEntityDiv").style.display = 'none';
                document.getElementById("disclosureByApprvdFinancialEntityDiv").style.height ="1px";
                document.getElementById("approvedDisclosureDiv").style.display = 'block';
                document.getElementById("approvedDisclosureDiv").style.height ="auto";

                  var imgprojplus = "imgApptoggle"+indexapp;
                document.getElementById(imgprojplus).src="<%=path%>/coeusliteimages/plus.gif";
                document.getElementById(appGlob2).style.visibility = "hidden";
                document.getElementById(appGlob2).style.height = "1px";
            }
            function setViewByApprvdFinancialEnity(){
                debugger;

                document.getElementById("approvedDisclosureDiv").style.display = 'none';
                document.getElementById("approvedDisclosureDiv").style.height ="1px";
                document.getElementById("disclosureByApprvdFinancialEntityDiv").style.display = 'block';
                document.getElementById("disclosureByApprvdFinancialEntityDiv").style.height ="auto";

               var imgplus = "imgtoggleapp"+indexapp1;
                document.getElementById(imgplus).src="<%=path%>/coeusliteimages/plus.gif";
                document.getElementById(appGlob1).style.visibility = "hidden";
                document.getElementById(appGlob1).style.height = "1px";
            }
   // ------------------------------Approved Disclosure End ----------------------------------------- //
     // ------------------------------Closed Disclosure ----------------------------------------- //
               function selectClosedDiscIcon(){
                debugger;

               if(document.getElementById("closedDisclosureDiv").style.display == "block"){
                    document.getElementById("closedimgtoggle").src="<%=path%>/coeusliteimages/plus.gif";
                    document.getElementById("closedDisclosureDiv").style.display = 'none';
                    document.getElementById("closedDisclosureDiv").style.height ="1px";
                    document.getElementById("disclosureByClosedFinancialEntityDiv").style.display = 'none';
                    document.getElementById("disclosureByClosedFinancialEntityDiv").style.height ="1px";
                    document.getElementById(glob).style.visibility = 'hidden';
                    document.getElementById(glob).style.height = "1px";
                    document.getElementById(glob2).style.visibility = 'hidden';
                    document.getElementById(glob2).style.height = "1px";
                    document.getElementById("imgtogglecld"+glob).src="<%=path%>/coeusliteimages/plus.gif";
                }else{
                    if(document.getElementById("disclosureByClosedFinancialEntityDiv").style.display == 'block'){
                        document.getElementById("disclosureByClosedFinancialEntityDiv").style.display = 'none';
                        document.getElementById("disclosureByClosedFinancialEntityDiv").style.height ="1px";
                        document.getElementById("closedimgtoggle").src="<%=path%>/coeusliteimages/plus.gif";
                        document.getElementById(glob).style.visibility = 'hidden';
                        document.getElementById(glob).style.height = "1px";
                        document.getElementById(glob2).style.visibility = 'hidden';
                        document.getElementById(glob2).style.height = "1px";

                        document.getElementById("imgtogglecld"+glob).src="<%=path%>/coeusliteimages/plus.gif";


                    }else {
                         document.getElementById("closedDisclosureDiv").style.display = "block";
                         document.getElementById("closedDisclosureDiv").style.height = "100%";
                         document.getElementById("closedimgtoggle").src="<%=path%>/coeusliteimages/minus.gif";
                    }
                }
              }

              function setViewByClosedFinancialEnity(){
                debugger;

                document.getElementById("closedDisclosureDiv").style.display = 'none';
                document.getElementById("closedDisclosureDiv").style.height ="1px";
                document.getElementById("disclosureByClosedFinancialEntityDiv").style.display = 'block';
                document.getElementById("disclosureByClosedFinancialEntityDiv").style.height ="auto";

               var imgplus = "imgtogglecld"+indexapp;
                document.getElementById(imgplus).src="<%=path%>/coeusliteimages/plus.gif";
                document.getElementById(appGlob1).style.visibility = "hidden";
                document.getElementById(appGlob1).style.height = "1px";
            }

             function selectClosedDiscEntityView(index){
                debugger;
                a = "imgtogglecld"+index;

               if(document.getElementById("projectcld"+index).style.visibility == "visible"){

                 document.getElementById("trcld"+index).style.visibility = "hidden";
                    document.getElementById("trcld"+index).style.height = "1px";
                    document.getElementById("projectcld"+index).style.visibility = "hidden";
                    document.getElementById("projectcld"+index).style.height = "1px";
                     document.getElementById(a).src="<%=path%>/coeusliteimages/plus.gif";
                }else{
                    <%--lastAppProj();
                    ds = new DivSlider();
                    ds.showDiv(index,1000);--%>
                    document.getElementById(a).src="<%=path%>/coeusliteimages/minus.gif";
                    document.getElementById("trcld"+index).style.visibility = "visible";
                    document.getElementById("trcld"+index).style.height = "auto";
                    document.getElementById("projectcld"+index).style.visibility = "visible";
                    document.getElementById("projectcld"+index).style.height = "auto";

                }
            }

               function setViewByProjectsClosed(){
                debugger;
                document.getElementById("disclosureByClosedFinancialEntityDiv").style.display = 'none';
                document.getElementById("disclosureByClosedFinancialEntityDiv").style.height ="1px";
                document.getElementById("closedDisclosureDiv").style.display = 'block';
                document.getElementById("closedDisclosureDiv").style.height ="auto";

                  var imgprojplus = "imgcldtoggle"+indexapp;
                document.getElementById(imgprojplus).src="<%=path%>/coeusliteimages/plus.gif";
                document.getElementById(appGlob2).style.visibility = "hidden";
                document.getElementById(appGlob2).style.height = "1px";
            }


             function getClosedDiscPjtView(index) {
                    img ="imgcldtoggle"+index;
                    appGlob1 = "projectcld"+index;
                    appGlob2 = "entcld"+index;
                    indexapp = index;

                 if(document.getElementById("entcld"+index).style.visibility == "visible"){
                     document.getElementById("rowcld"+index).style.visibility = "hidden";
                    document.getElementById("rowcld"+index).style.height = "1px";
                    document .getElementById("entcld"+index).style.visibility = "hidden";
                    document.getElementById("entcld"+index).style.height = "1px";
                    document.getElementById(img).src="<%=path%>/coeusliteimages/plus.gif";

                }else{
                     <%--lastApp();
                    ds = new DivSlider();
                    ds.showDiv(index,1000);--%>

                    document.getElementById(img).src="<%=path%>/coeusliteimages/minus.gif";
                    document.getElementById("entcld"+index).style.visibility = "visible";
                    document.getElementById("entcld"+index).style.height = "auto";
                    document.getElementById("rowcld"+index).style.visibility = "visible";
                    document.getElementById("rowcld"+index).style.height = "auto";
                  }
                }
// ------------------------------Closed Disclosure End ----------------------------------------- //
//---------------------------------Travel Disclosure Starts---------------------------------------//
  function selectTravelDiscIcon(){
                debugger;

               if(document.getElementById("travelDisclosureDiv").style.display == "block"){
                    document.getElementById("travelimgtoggle").src="<%=path%>/coeusliteimages/plus.gif";
                    document.getElementById("travelDisclosureDiv").style.display = 'none';
                    document.getElementById("travelDisclosureDiv").style.height ="1px";
                    document.getElementById("disclosureByTravelFinancialEntityDiv").style.display = 'none';
                    document.getElementById("disclosureByTravelFinancialEntityDiv").style.height ="1px";
                    document.getElementById(glob).style.visibility = 'hidden';
                    document.getElementById(glob).style.height = "1px";
                    document.getElementById(glob2).style.visibility = 'hidden';
                    document.getElementById(glob2).style.height = "1px";
                    document.getElementById("imgtoggletvl"+glob).src="<%=path%>/coeusliteimages/plus.gif";
                }else{
                    if(document.getElementById("disclosureByTravelFinancialEntityDiv").style.display == 'block'){
                        document.getElementById("disclosureByTravelFinancialEntityDiv").style.display = 'none';
                        document.getElementById("disclosureByTravelFinancialEntityDiv").style.height ="1px";
                        document.getElementById("travelimgtoggle").src="<%=path%>/coeusliteimages/plus.gif";
                        document.getElementById(glob).style.visibility = 'hidden';
                        document.getElementById(glob).style.height = "1px";
                        document.getElementById(glob2).style.visibility = 'hidden';
                        document.getElementById(glob2).style.height = "1px";

                        document.getElementById("imgtoggletvl"+glob).src="<%=path%>/coeusliteimages/plus.gif";


                    }else {
                         document.getElementById("travelDisclosureDiv").style.display = "block";
                         document.getElementById("travelDisclosureDiv").style.height = "100%";
                         document.getElementById("travelimgtoggle").src="<%=path%>/coeusliteimages/minus.gif";
                    }
                }
              }

              function setViewByTravelFinancialEnity(){
                debugger;

                document.getElementById("travelDisclosureDiv").style.display = 'none';
                document.getElementById("travelDisclosureDiv").style.height ="1px";
                document.getElementById("disclosureByTravelFinancialEntityDiv").style.display = 'block';
                document.getElementById("disclosureByTravelFinancialEntityDiv").style.height ="auto";

               var imgplus = "imgtoggletvl"+indexapp;
                document.getElementById(imgplus).src="<%=path%>/coeusliteimages/plus.gif";
                document.getElementById(appGlob1).style.visibility = "hidden";
                document.getElementById(appGlob1).style.height = "1px";
            }

             function selectTravelDiscEntityView(index){
                debugger;
                a = "imgtoggletvl"+index;             

               if(document.getElementById("projectTrvl"+index).style.visibility == "visible"){

                 document.getElementById("trTrvl"+index).style.visibility = "hidden";
                    document.getElementById("trTrvl"+index).style.height = "1px";
                    document.getElementById("projectTrvl"+index).style.visibility = "hidden";
                    document.getElementById("projectTrvl"+index).style.height = "1px";
                     document.getElementById(a).src="<%=path%>/coeusliteimages/plus.gif";
                }else{
                    document.getElementById(a).src="<%=path%>/coeusliteimages/minus.gif";                   
                    document.getElementById("trTrvl"+index).style.visibility = "visible";                  
                    document.getElementById("trTrvl"+index).style.height = "auto";                   
                    document.getElementById("projectTrvl"+index).style.visibility = "visible";                  
                    document.getElementById("projectTrvl"+index).style.height = "auto";

                }
            }

               function setViewByProjectsTravel(){
                debugger;
                document.getElementById("disclosureByTravelFinancialEntityDiv").style.display = 'none';
                document.getElementById("disclosureByTravelFinancialEntityDiv").style.height ="1px";
                document.getElementById("travelDisclosureDiv").style.display = 'block';
                document.getElementById("travelDisclosureDiv").style.height ="auto";

                  var imgprojplus = "imgtvltoggle"+indexapp;
                document.getElementById(imgprojplus).src="<%=path%>/coeusliteimages/plus.gif";
                document.getElementById(appGlob2).style.visibility = "hidden";
                document.getElementById(appGlob2).style.height = "1px";
            }


             function getTravelDiscPjtView(index) {
                    img ="imgtvltoggle"+index;
                    appGlob1 = "projecttvl"+index;
                    appGlob2 = "entTvl"+index;
                    indexapp = index;
                  
                 if(document.getElementById("entTvl"+index).style.visibility == "visible"){
                     document.getElementById("rowtvl"+index).style.visibility = "hidden";
                    document.getElementById("rowtvl"+index).style.height = "1px";
                    document .getElementById("entTvl"+index).style.visibility = "hidden";
                    document.getElementById("entTvl"+index).style.height = "1px";
                    document.getElementById(img).src="<%=path%>/coeusliteimages/plus.gif";

                }else{
                    document.getElementById(img).src="<%=path%>/coeusliteimages/minus.gif";
                    document.getElementById("entTvl"+index).style.visibility = "visible";
                    document.getElementById("entTvl"+index).style.height = "auto";
                    document.getElementById("rowtvl"+index).style.visibility = "visible";
                    document.getElementById("rowtvl"+index).style.height = "auto";
                  }
                }
 //---------------------------------------------travel ends--------------------------------------------------//
        </script>
</head>

<%
            int index = 0;
            String strBgColor = "#DCE5F1";
            Vector projetList = new Vector();

            if(request.getAttribute("projectNameList") != null){
                 projetList = (Vector) request.getAttribute("projectNameList");
              }
             String startDate = "";
            String endDate = "";
             String pendingdisclendDate = "";
            String   closedpjctendDate="";
            CoiCommonService coiCommonService1 = CoiCommonService.getInstance();
            CoiDisclosureDetailsListBean pjtBean;
       %>
<!-- Managed Disclosure List-->
<%--<logic:present name="ApprovedDisclDetView">--%>
<table>
	<tr style="background-color: white">
		<td></td>
	</tr>
</table>
<table class="table" style="width: 100%;" border="0">
	<tr>
		<td><img
			src='<%=request.getContextPath()%>/coeusliteimages/plus.gif'
			border='none' id="managedimgtoggle" name="projectimgtoggle"
			border="none" onclick="javascript:selectManagedDiscIcon();" /> <img
			src='<%=request.getContextPath()%>/coeusliteimages/minus.gif'
			style="visibility: hidden;" border='none' id="managedimgtoggleminus"
			name="projectimgtoggleminus" border="none"
			onclick="javascript:selectManagedDiscIcon;" /> <label
			style="color: #333333; font-family: Arial, Helvetica, sans-serif; font-size: 12px; font-weight: bold;">Managed</label>
		</td>
	</tr>
</table>

<div id="managedDisclosureDiv" style="display: none; height: 1px;">
	<table id="bodyTable" class="table" style="width: 100%;" border="0">
		<tr style="background-color: #6E97CF; height: 22px;">
			<td style="width: 3%; background-color: #538dd5;"></td>
			<td nowrap="nowrap"
				style="width: 15%; color: #FFFFFF; font-size: 12px; background-color: #538dd5;"><strong>Discl.Event
					Type</strong></td>
			<td
				style="width: 18%; color: #FFFFFF; font-size: 12px; background-color: #538dd5;"><strong>Title</strong></td>
			<td
				style="width: 11%; color: #FFFFFF; font-size: 12px; background-color: #538dd5;"><strong>Project
					#</strong></td>
			<td
				style="width: 12%; color: #FFFFFF; font-size: 12px; background-color: #538dd5;"><strong>Start
					Date</strong></td>
			<td
				style="width: 12%; color: #FFFFFF; font-size: 12px; background-color: #538dd5;"><strong>End
					Date</strong></td>
			<td nowrap="nowrap"
				style="width: 16%; color: #FFFFFF; font-size: 12px; background-color: #538dd5;"><strong>Discl.Event
					Status</strong></td>
			<td style="width: 16%;" align="right"><a
				href="javaScript:setViewByManagedFinancialEnity();">View By
					FinancialEntities</a></td>
		</tr>

		<logic:empty name="mngdPjtEntDetailsViews">
			<tr style="height: 1px;">
				<td colspan="2" style="padding-left: 25px;"><font
					style="color: red">No Projects found</font></td>
			</tr>
		</logic:empty>

		<logic:present name="mngdPjtEntDetailsViews">
			<% int mngdIndex = 0;
                            TreeSet entStatusId = new TreeSet();
                            String pjtId = "";
                            String entStatusDescription = "";
                        Vector entDetList = new Vector();
                        if(request.getAttribute("mngdPjtEntDetailsViews") != null){
                             projetList = (Vector) request.getAttribute("mngdPjtEntDetailsViews");
                          }

                          if(request.getAttribute("mngdEntPjtDetView") != null){
                              entDetList = (Vector)request.getAttribute("mngdEntPjtDetView");
                          }
                       %>
			<logic:iterate id="pjtList" name="mngdPjtEntDetailsViews">

				<%
                                if (mngdIndex % 2 == 0) {
                                    strBgColor = "#D6DCE5";
                                } else {
                                    strBgColor = "#DCE5F1";
                                }
								 String moduleNameMngd = "";
                                //clearing the dates
                                    startDate="";
                                    endDate="";
                                    moduleNameMngd="";
                                 pjtBean = (CoiDisclosureDetailsListBean) projetList.get(mngdIndex);

                                if(pjtBean != null){

                                    if(pjtBean.getCoiProjectStartDate() != null) {
                                        startDate = (String) coiCommonService1.getFormatedDate(pjtBean.getCoiProjectStartDate());
                                     }

                                    if(pjtBean.getCoiProjectEndDate() != null) {
                                        endDate = (String) coiCommonService1.getFormatedDate(pjtBean.getCoiProjectEndDate());
                                    }

					int code = pjtBean.getModuleCode();

                                        if(code == 3) {
                                            moduleNameMngd = "Proposal";
                                        }
                                        else if(code == 7) {
                                            moduleNameMngd = "IRB Protocol";
                                        }
                                        else if(code == 9) {
                                            moduleNameMngd = "IACUC Protocol";
                                        }
                                        else if(code == 1) {
                                            moduleNameMngd = "Award";
                                        }
                                        else if(code == 13) {
                                            moduleNameMngd = "Annual";
                                        }
                                        else if(code == 12) {
                                            moduleNameMngd = "Protocol";
                                        }
                                         else if(code == 2) {
                                            moduleNameMngd = "Proposal";
                                        }
                                         else if(code == 11) {
                                            moduleNameMngd = "Proposal";
                                        }
                                         else if(code == 12) {
                                            moduleNameMngd = "Protocol";
                                        }

                                      pjtId = (String)pjtBean.getCoiProjectId();

                                      if(entDetList != null) {
                                          entStatusId = new TreeSet();
                                          String entPjtId = "";
                                        for(int i=0; i < entDetList.size(); i++) {
                                            CoiDisclosureDetailsListBean entBean = (CoiDisclosureDetailsListBean) entDetList.get(i);
                                            entPjtId = (String)entBean.getCoiProjectId();

                                            if(pjtId.trim().equals(entPjtId.trim())) {
                                                String entstatus = entBean.getEntityStatusCode() + ":" + entBean.getEntityStatusDesc();
                                                entStatusId.add(entstatus);
                                            }
                                        }
                                      }

                                      if(entStatusId != null && entStatusId.size() > 0) {
                                         String statusDescCode =  (String)entStatusId.last();
                                         String[] splitList = statusDescCode.split(":");
                                         entStatusDescription =  splitList[1];
                                      }
                                 }
                         %>

				<tr bgcolor="<%=strBgColor%>" id="rowMngd<%=mngdIndex%>"
					class="rowLine" onmouseover="className='rowHover rowLine'"
					onmouseout="className='rowLine'" height="22px">
					<td width="3%"><img
						src='<%=request.getContextPath()%>/coeusliteimages/plus.gif'
						border='none' id="imgtoggleMngd<%=mngdIndex%>"
						name="imgtoggleMngd<%=mngdIndex%>" border="none"
						onclick="javascript:selectManagedDiscEntityView(<%=mngdIndex%>);" />
						<img src='<%=request.getContextPath()%>/coeusliteimages/minus.gif'
						style="visibility: hidden;" border='none'
						id="imgtoggleminusMngd<%=mngdIndex%>"
						name="imgtoggleminusMngd<%=mngdIndex%>" border="none"
						onclick="javascript:selectManagedDiscEntityView(<%=mngdIndex%>);" />
					</td>
					<td style="width: 15%;"><%=moduleNameMngd%></td>
					<td style="width: 18%;"><bean:write name="pjtList"
							property="coiProjectTitle" /></td>
					<td style="width: 11%;"><bean:write name="pjtList"
							property="coiProjectId" /></td>
					<td style="width: 12%;"><%=startDate%></td>
					<td style="width: 12%;"><%=endDate%></td>
					<td style="width: 16%;">
						<%--<bean:write name="pjtList" property="coiDisclosureStatusDesc"/>--%><%=entStatusDescription%></td>
					<td style="width: 16%;">&nbsp;</td>
				</tr>

				<tr id="trMngd<%=mngdIndex%>"
					style="width: 100%; height: 1px; visibility: hidden; overflow: hidden;">
					<td style="width: 100%;" colspan="8">
						<div id="mngdProject<%=mngdIndex%>"
							style="width: 100%; height: 1px; visibility: hidden; background-color: #9DBFE9; overflow-x: hidden; overflow-y: scroll;">
							<table id="bodyTable" class="table"
								style="width: 100%; height: 1px;" border="0">
								<tr style="height: 1px;">
									<td
										style="background-color: #6E97CF; color: #FFFFFF; font-size: 12px; font-weight: bold;">&nbsp;</td>
									<td colspan="3"
										style="background-color: #6E97CF; color: #FFFFFF; font-size: 12px; font-weight: bold;"><strong>Entity
											Name</strong></td>

									<td colspan="3"
										style="background-color: #6E97CF; color: #FFFFFF; font-size: 12px; font-weight: bold;"><strong>Entity
											Status</strong></td>
								</tr>

								<% int mngdIndex1=0; %>
								<logic:iterate id="pjtEntView" name="mngdEntPjtDetView">
									<% String projectName1 = (String) pjtBean.getCoiProjectId();%>
									<logic:equal value="<%=projectName1%>" property="coiProjectId"
										name="pjtEntView">
										<logic:notEmpty name="pjtEntView" property="entityName">
											<% mngdIndex1++; %>
											<tr class="rowLineLight"
												onmouseover="className='rowHover rowLine'"
												onmouseout="className='rowLineLight'" style="height: 1px;"
												width="100%">
												<td style="width: 5%"></td>
												<td colspan="3"><b><bean:write name="pjtEntView"
															property="entityName" /></b></td>
												<td colspan="3"><b><bean:write name="pjtEntView"
															property="entityStatusDesc" /></b></td>
											</tr>
										</logic:notEmpty>
									</logic:equal>

								</logic:iterate>

								<%if(mngdIndex1 == 0){%>
								<tr style="height: 1px;">
									<td colspan="7" style="padding-left: 25px;"><font
										style="color: red">No financial entities found</font></td>
								</tr>
								<%}%>

							</table>
						</div>
					</td>
				</tr>
				<%
                            mngdIndex++;
                         %>
			</logic:iterate>

		</logic:present>

	</table>
</div>

<div id="disclosureByManagedFinancialEntityDiv"
	style="display: none; height: 1px;">
	<table id="bodyTable" class="table" style="width: 100%;" border="0">
		<tr style="background-color: #6E97CF; height: 22px;">
			<td>&nbsp;</td>
			<td style="width: 40%; color: #FFFFFF; font-size: 14px;"><strong>Entity
					Names</strong></td>
			<td style="width: 50%;" align="right"><a
				href="javaScript:setViewByProjectsManaged();">View By Projects</a></td>
			<td><strong>&nbsp;</strong></td>
			<td><strong>&nbsp;</strong></td>
		</tr>

		<logic:empty name="mngdEntityNameLists">
			<tr style="height: 1px;">
				<td colspan="2" style="padding-left: 25px;"><font
					style="color: red">No Entities found</font></td>
			</tr>
		</logic:empty>

		<logic:present name="mngdEntityNameLists">
			<%
                                        int mngdEntity = 0;
                                 %>
			<logic:iterate id="pjtTitle" name="mngdEntityNameLists">
				<%
                                                    if (mngdEntity % 2 == 0) {
                                                            strBgColor = "#D6DCE5";
                                                    } else {
                                                            strBgColor = "#DCE5F1";
                                                    }
                                                %>

				<tr bgcolor="<%=strBgColor%>" id="row1pend<%=mngdEntity%>"
					class="rowLine" onmouseover="className='rowHover rowLine'"
					onmouseout="className='rowLine'" height="22px">
					<td colspan="1" width="5%"><img
						src='<%=request.getContextPath()%>/coeusliteimages/plus.gif'
						border='none' id="imgMngdtoggle<%=mngdEntity%>"
						name="imgMngdtoggle<%=mngdEntity%>" border="none"
						onclick="javascript:getManagedDiscPjtView(<%=mngdEntity%>);" /> <img
						src='<%=request.getContextPath()%>/coeusliteimages/minus.gif'
						style="visibility: hidden;" border='none'
						id="imgMngdtoggleminus<%=mngdEntity%>"
						name="imgMngdtoggleminus<%=mngdEntity%>" border="none"
						onclick="javascript:getManagedDiscPjtView(<%=mngdEntity%>);" /></td>
					<td colspan="4"><b><bean:write name="pjtTitle" /></b></td>
				</tr>

				<tr>
					<td colspan="5">
						<div id="entMngd<%=mngdEntity%>"
							style="height: 1px; width: 100%; visibility: hidden; background-color: #9DBFE9; overflow-x: hidden; overflow-y: scroll;">
							<table id="bodyTable" class="table"
								style="width: 100%; height: 100%;" border="0">
								<tr style="background-color: #6E97CF;">
									<td width="3%" style="background-color: #538dd5;"></td>
									<td nowrap="nowrap"
										style="width: 15%; color: #FFFFFF; font-size: 12px; background-color: #538dd5;"><strong>Discl.Event
											Type</strong></td>
									<td
										style="width: 15%; color: #FFFFFF; font-size: 12px; background-color: #538dd5;"><strong>Title</strong></td>
									<td
										style="width: 15%; color: #FFFFFF; font-size: 12px; background-color: #538dd5;"><strong>Project
											#</strong></td>
									<td
										style="width: 12%; color: #FFFFFF; font-size: 12px; background-color: #538dd5;"><strong>Start
											Date</strong></td>
									<td
										style="width: 12%; color: #FFFFFF; font-size: 12px; background-color: #538dd5;"><strong>End
											Date</strong></td>
									<td nowrap="nowrap"
										style="width: 15%; color: #FFFFFF; font-size: 12px; background-color: #538dd5;"><strong>Discl.Event
											Status</strong></td>
								</tr>
								<logic:empty name="mngdPjtEntDetailsViews">
									<tr style="height: 1px;">
										<td colspan="2" style="padding-left: 25px;"><font
											style="color: red">No Projects found</font></td>
									</tr>
								</logic:empty>
								<logic:present name="mngdPjtEntDetailsViews">
									<% int mngdPjtIndex = 0;
                                                            if(request.getAttribute("mngdPjtEntDetailsViews") != null){
                                                                     projetList = (Vector) request.getAttribute("mngdPjtEntDetailsViews");
                                                              }
                                                       %>
									<logic:iterate id="pjtList" name="mngdPjtEntDetailsViews">

										<%
                                                                if (mngdPjtIndex % 2 == 0) {
                                                                        strBgColor = "#D6DCE5";
                                                                } else {
                                                                        strBgColor = "#DCE5F1";
                                                                }
                                                                String  moduleNmMngd = "";
                                                                //clearing the strings
                                                                startDate="";
                                                                endDate="";
                                                                moduleNmMngd="";
                                                                 pjtBean = (CoiDisclosureDetailsListBean) projetList.get(mngdPjtIndex);
                                                                if(pjtBean != null){

                                                                        if(pjtBean.getCoiProjectStartDate() != null) {
                                                                                startDate = (String) coiCommonService1.getFormatedDate(pjtBean.getCoiProjectStartDate());
                                                                         }

                                                                        if(pjtBean.getCoiProjectEndDate() != null) {
                                                                                endDate = (String) coiCommonService1.getFormatedDate(pjtBean.getCoiProjectEndDate());
                                                                        }


                                                                            int code = pjtBean.getModuleCode();

                                                                            if(code == 3) {
                                                                                    moduleNmMngd = "Proposal";
                                                                            }
                                                                            else if(code == 7) {
                                                                                    moduleNmMngd = "IRB Protocol";
                                                                            }
                                                                            else if(code == 9) {
                                                                                    moduleNmMngd = "IACUC Protocol";
                                                                            }
                                                                            else if(code == 1) {
                                                                                    moduleNmMngd = "Award";
                                                                            }
                                                                            else if(code == 13) {
                                                                                    moduleNmMngd = "Annual";
                                                                            }
                                                                            else if(code == 12) {
                                                                                    moduleNmMngd = "Protocol";
                                                                            }
                                                                             else if(code == 2) {
                                                                                moduleNmMngd = "Proposal";
                                                                            }
                                                                             else if(code == 11) {
                                                                                moduleNmMngd = "Proposal";
                                                                            }
                                                                             else if(code == 12) {
                                                                                moduleNmMngd = "Protocol";
                                                                            }
                                                                 }
                                                             %>
										<tr bgcolor="<%=strBgColor%>" id="row2Pend<%=mngdPjtIndex%>"
											class="rowLine" onmouseover="className='rowHover rowLine'"
											onmouseout="className='rowLine'" height="22px">

											<td width="3%"></td>
											<td style="width: 15%;"><%=moduleNmMngd%></td>
											<td style="width: 15%;"><bean:write name="pjtList"
													property="coiProjectTitle" /></td>
											<td style="width: 15%;"><bean:write name="pjtList"
													property="coiProjectId" /></td>
											<td style="width: 12%;"><%=startDate%></td>
											<td style="width: 12%;"><%=endDate%></td>
											<td style="width: 15%;"><bean:write name="pjtList"
													property="entityStatusDesc" /></td>
										</tr>
										<%mngdPjtIndex++;%>
									</logic:iterate>
									<%if(mngdPjtIndex == 0){%>
									<tr style="height: 1px;">
										<td colspan="2" style="padding-left: 25px;"><font
											style="color: red">No Projects found</font></td>
									</tr>
									<%}%>
								</logic:present>

							</table>
						</div>
					</td>

				</tr>
				<%mngdEntity++;%>
			</logic:iterate>

		</logic:present>

	</table>
</div>

<%--</logic:present>--%>

<!--Pending Disclosure List -->

<%--<logic:present name="ApprovedDisclDetView">--%>
<table>
	<tr style="background-color: white">
		<td></td>
	</tr>
</table>
<table class="table" style="width: 100%;" border="0">
	<tr>
		<td><img
			src='<%=request.getContextPath()%>/coeusliteimages/plus.gif'
			border='none' id="pendingimgtoggle" name="pendingimgtoggle"
			border="none" onclick="javascript:selectPendingDiscIcon();" /> <img
			src='<%=request.getContextPath()%>/coeusliteimages/minus.gif'
			style="visibility: hidden;" border='none' id="pendingimgtoggleminus"
			name="pendingimgtoggleminus" border="none"
			onclick="javascript:selectPendingDiscIcon();" /> <label
			style="color: #333333; font-family: Arial, Helvetica, sans-serif; font-size: 12px; font-weight: bold;">Pending</label>
		</td>
	</tr>
</table>

<div id="pendingDisclosureDiv" style="display: none; height: 1px;">
	<table id="bodyTable" class="table" style="width: 100%;" border="0">
		<tr style="background-color: #6E97CF; height: 22px;">
			<td style="width: 3%; background-color: #538dd5;"></td>
			<td nowrap="nowrap"
				style="width: 15%; color: #FFFFFF; font-size: 12px; background-color: #538dd5;"><strong>Discl.Event
					Type</strong></td>
			<td
				style="width: 18%; color: #FFFFFF; font-size: 12px; background-color: #538dd5;"><strong>Title</strong></td>
			<td
				style="width: 11%; color: #FFFFFF; font-size: 12px; background-color: #538dd5;"><strong>Project
					#</strong></td>
			<td
				style="width: 12%; color: #FFFFFF; font-size: 12px; background-color: #538dd5;"><strong>Start
					Date</strong></td>
			<td
				style="width: 12%; color: #FFFFFF; font-size: 12px; background-color: #538dd5;"><strong>End
					Date</strong></td>
			<td nowrap="nowrap"
				style="width: 16%; color: #FFFFFF; font-size: 12px; background-color: #538dd5;"><strong>Discl.Event
					Status</strong></td>
			<td style="width: 16%; background-color: #538dd5;" align="right"></td>
		</tr>
		<logic:empty name="pendingPjtList">
			<tr style="height: 1px;">
				<td colspan="2" style="padding-left: 25px;"><font
					style="color: red">No Projects found</font></td>
			</tr>
		</logic:empty>

		<logic:present name="pendingPjtList">
			<% int pendIndex = 0;
                         String link = "";
                         String disclosureStatus = "";

                        if(request.getAttribute("pendingPjtList") != null){
                             projetList = (Vector) request.getAttribute("pendingPjtList");
                          }
                       %>
			<logic:iterate id="pjtList" name="pendingPjtList">

				<%
                                if (pendIndex % 2 == 0) {
                                    strBgColor = "#D6DCE5";
                                } else {
                                    strBgColor = "#DCE5F1";
                                }
                         String moduleNamePend = "";
                         int code = 0;

                         Calendar currentDate = Calendar.getInstance();
                         SimpleDateFormat formatter1 = new SimpleDateFormat("MM-dd-yyyy");
                         String dateNow = formatter1.format(currentDate.getTime());
                         Date currDate = (Date) formatter1.parse(dateNow);
                        // Date expirationDate = new Date();
                        // int disclStatus = 0;
                         int reviewcode=0;
                            //clearing the strings
                            startDate="";
                            pendingdisclendDate="";
                            moduleNamePend="";
                            disclosureStatus="";
                                 pjtBean = (CoiDisclosureDetailsListBean) projetList.get(pendIndex);
                                if(pjtBean != null){
                                    if(pjtBean.getReviewStatusCode() != 0)
                                        {
                                        reviewcode=pjtBean.getReviewStatusCode();
                                    }

                                    if(pjtBean.getCoiProjectStartDate() != null) {
                                        startDate = (String) coiCommonService1.getFormatedDate(pjtBean.getCoiProjectStartDate());
                                     }

                                    if(pjtBean.getCoiProjectEndDate() != null) {
                                        pendingdisclendDate = (String) coiCommonService1.getFormatedDate(pjtBean.getCoiProjectEndDate());
                                    }
                                  //  String moduleItemKey = pjtBean.getCoiProjectId();

                                    code = pjtBean.getModuleCode();

                                        if(code == 2) {
                                            moduleNamePend = "Proposal";
                                            if(pjtBean.getCoiProjectEndDate()!=null){
                                                 pendingdisclendDate =(String) coiCommonService1.getFormatedDate(pjtBean.getCoiProjectEndDate());
                                             }
                                            if(pjtBean.getCoiProjectStartDate()!=null){
                                            startDate = (String) coiCommonService1.getFormatedDate(pjtBean.getCoiProjectStartDate());
                                            }
                                          }
                                        else if(code == 3) {
                                            moduleNamePend = "IRB Protocol";
                                         //   pendingdisclendDate="";
                                        }
                                        else if(code == 4) {
                                            moduleNamePend = "IACUC Protocol";
                                       //     pendingdisclendDate="";
                                        }
                                        else if(code == 1) {
                                            moduleNamePend = "Award";
                                        }
                                        else if(code == 5) {
                                            moduleNamePend = "Annual";
                                            startDate = "";
                                        //    pendingdisclendDate = "";
                                        }
                                         else if(code == 6) {
                                            moduleNamePend = "Annual";
                                            startDate = "";
                                         //   pendingdisclendDate = "";
                                        }
                                        else if(code ==8) {
                                            moduleNamePend = "Travel";
                                        }
                                        //disclStatus = pjtBean.getCoiDisclActiveStatus();

                                        if(pendingdisclendDate==null){
                                            pendingdisclendDate="";
                                            }

                                 }
                         %>

				<%if(reviewcode!=1){%>
				<%
                              String moduleItemKey = pjtBean.getCoiProjectId();
                            link = "/getannualdisclosure.do?&param1=" + pjtBean.getCoiDisclosureNumber() + "&param2=" + pjtBean.getCoiSequenceNumber() + "&param5=" + moduleNamePend+ "&param3=" + pjtBean.getPersonId()+ "&param6=" + "pendingDiscl"+"&selectedPjct="+moduleItemKey;
                           if(moduleNamePend!= null && moduleNamePend.equals("Travel")){
                                link="/travelDetails.do?&edit=true&selectedPjct="+moduleItemKey+"&sequenceNumber="+pjtBean.getCoiSequenceNumber()+"&param1="+pjtBean.getCoiDisclosureNumber()+"&param3="+pjtBean.getPersonId();
                            }
                               disclosureStatus = "Pending For Approval";
                            %>
				<%}else{%>
				<%
                        //    link = "/createDisclosureFromInProgress.do?&param1=" + pjtBean.getCoiDisclosureNumber() + "&param2=" + pjtBean.getCoiSequenceNumber()+ "&param3=" + pjtBean.getPersonId()+ "&param5=" + moduleNamePend + "&param6=" + "pendingDiscl&frmPendingInPrg=true&projectID="+pjtBean.getCoiProjectId()+"&operationType=MODIFY&operation=UPDATE&code="+code;
                          //  link = "/findDisclosureProjectType.do?&param1=" + pjtBean.getCoiDisclosureNumber() + "&param2=" + pjtBean.getCoiSequenceNumber()+ "&param3=" + pjtBean.getPersonId()+ "&param5=" + code + "&param6=pendingDiscl&module_item_key="+pjtBean.getCoiProjectId();
                            //disclosureStatus = pendingDisclosuresBean.getDisclosureStatus();
                            link = "/coiInProgress.do?discl="+ pjtBean.getCoiDisclosureNumber()+"&seq="+ pjtBean.getCoiSequenceNumber()+"&personId="+ pjtBean.getPersonId()+"&projectID="+pjtBean.getCoiProjectId()+"&eventType="+pjtBean.getEventType()+"&moduleCode="+code;
                           if(moduleNamePend!= null && moduleNamePend.equals("Travel")){
                                link="/travelDetails.do?&edit=false&projectID="+pjtBean.getCoiProjectId()+"&sequenceNumber="+pjtBean.getCoiSequenceNumber()+"&param1="+pjtBean.getCoiDisclosureNumber()+"&param3="+pjtBean.getPersonId();
                            }
                            disclosureStatus = "In Progress";
                            %>
				<%}%>


				<tr bgcolor="<%=strBgColor%>" id="rowpend<%=pendIndex%>"
					class="rowLine" onmouseover="className='rowHover rowLine'"
					onmouseout="className='rowLine'" height="22px">
					<td width="3%">
						<%--<img src='<%=request.getContextPath()%>/coeusliteimages/plus.gif' border='none' id="imgtogglepend<%=pendIndex%>" name="imgtogglepend<%=pendIndex%>" border="none" onclick="javascript:selectPendingDiscEntityView(<%=pendIndex%>);"/>
                               <img src='<%=request.getContextPath()%>/coeusliteimages/minus.gif' style="visibility: hidden;" border='none' id="imgtoggleminuspend<%=pendIndex%>" name="imgtoggleminuspend<%=pendIndex%>" border="none" onclick="javascript:selectPendingDiscEntityView(<%=pendIndex%>);"/>
                           --%>
					</td>
					<td style="width: 15%;"><html:link
							style="color: #003399;font-family:Arial,Helvetica,sans-serif;font-size:12px;font-weight:bold;float: none;font-size:12px;"
							action="<%=link%>">
							<bean:write name="pjtList" property="eventType" />
						</html:link></td>
					<%if(code == 8) {%>
					<td style="width: 18%;"><bean:write name="pjtList"
							property="eventName" /></td>
					<%}else {%>
					<td style="width: 18%;"><bean:write name="pjtList"
							property="coiProjectTitle" /></td>
					<%}%>
					<td style="width: 11%;"><bean:write name="pjtList"
							property="coiProjectId" /></td>
					<td style="width: 12%;"><%=startDate%></td>
					<td style="width: 12%;"><%=pendingdisclendDate%></td>
					<%--<td style="width:20%;"><%=disclosureStatus%></td>--%>
					<td style="width: 16%;"><bean:write name="pjtList"
							property="reviewStatus" /></td>
					<td style="width: 16%;">&nbsp;</td>
				</tr>
				<%
                            pendIndex++;
                         %>
			</logic:iterate>

		</logic:present>

	</table>
</div>


<table>
	<tr style="background-color: white">
		<td></td>
	</tr>
</table>
<table class="table" style="width: 100%;" border="0">
	<tr>
		<td><img
			src='<%=request.getContextPath()%>/coeusliteimages/plus.gif'
			border='none' id="approvedimgtoggle" name="approvedimgtoggle"
			border="none" onclick="javascript:selectApprovedDiscIcon();" /> <img
			src='<%=request.getContextPath()%>/coeusliteimages/minus.gif'
			style="visibility: hidden;" border='none' id="approvedimgtoggleminus"
			name="approvedimgtoggleminus" border="none"
			onclick="javascript:selectApprovedDiscIcon();" /> <label
			style="color: #333333; font-family: Arial, Helvetica, sans-serif; font-size: 12px; font-weight: bold;">Approved</label>
		</td>
	</tr>
</table>

<div id="approvedDisclosureDiv" style="display: none; height: 1px;">
	<table id="bodyTable" class="table" style="width: 100%;" border="0">
		<tr style="background-color: #6E97CF; height: 22px;">
			<td style="width: 3%; background-color: #538dd5;"></td>
			<td nowrap="nowrap"
				style="width: 15%; color: #FFFFFF; font-size: 12px; background-color: #538dd5;"><strong>Discl.Event
					Type</strong></td>
			<td
				style="width: 18%; color: #FFFFFF; font-size: 12px; background-color: #538dd5;"><strong>Title</strong></td>
			<td
				style="width: 11%; color: #FFFFFF; font-size: 12px; background-color: #538dd5;"><strong>Project
					#</strong></td>
			<td
				style="width: 12%; color: #FFFFFF; font-size: 12px; background-color: #538dd5;"><strong>Start
					Date</strong></td>
			<td
				style="width: 12%; color: #FFFFFF; font-size: 12px; background-color: #538dd5;"><strong>End
					Date</strong></td>
			<td nowrap="nowrap"
				style="width: 16%; color: #FFFFFF; font-size: 12px; background-color: #538dd5;"><strong>Discl.Event
					Status</strong></td>
			<td style="width: 16%;" align="right"><a
				href="javaScript:setViewByApprvdFinancialEnity();">View By
					FinancialEntities</a></td>
		</tr>

		<logic:empty name="apprvdPjtEntDetailsViews">
			<tr style="height: 1px;">
				<td colspan="2" style="padding-left: 25px;"><font
					style="color: red">No Projects found</font></td>
			</tr>
		</logic:empty>

		<logic:present name="apprvdPjtEntDetailsViews">
			<% index = 0;

                         TreeSet entStatusIdApprvd = new TreeSet();
                            String pjtIdApprvd = "";
                            String entStatusDescriptionApprvd = "";
                        Vector entDetListApprvd = new Vector();

                        if(request.getAttribute("apprvdPjtEntDetailsViews") != null){
                             projetList = (Vector) request.getAttribute("apprvdPjtEntDetailsViews");
                          }

                        if(request.getAttribute("apprvdEntPjtDetView") != null){
                              entDetListApprvd = (Vector)request.getAttribute("apprvdEntPjtDetView");
                          }

                       %>
			<logic:iterate id="pjtList" name="apprvdPjtEntDetailsViews">

				<%
                                if (index % 2 == 0) {
                                    strBgColor = "#D6DCE5";
                                } else {
                                    strBgColor = "#DCE5F1";
                                }
                         String moduleName = "";
                            //clearing the strings
                            moduleName="";
                            startDate="";
                            endDate="";

                                 pjtBean = (CoiDisclosureDetailsListBean) projetList.get(index);
                                if(pjtBean != null){

                                    if(pjtBean.getCoiProjectStartDate() != null) {
                                        startDate = (String) coiCommonService1.getFormatedDate(pjtBean.getCoiProjectStartDate());
                                     }

                                    if(pjtBean.getCoiProjectEndDate() != null) {
                                        endDate = (String) coiCommonService1.getFormatedDate(pjtBean.getCoiProjectEndDate());
                                    }

                                        int code = pjtBean.getModuleCode();

                                        if(code == 3) {
                                            moduleName = "Proposal";
                                        }
                                        else if(code == 7) {
                                            moduleName = "IRB Protocol";
                                        }
                                        else if(code == 9) {
                                            moduleName = "IACUC Protocol";
                                        }
                                        else if(code == 1) {
                                            moduleName = "Award";
                                        }
                                        else if(code == 13) {
                                            moduleName = "Annual";
                                        }
                                        else if(code == 12) {
                                            moduleName = "Protocol";
                                        }
                                        else if(code == 2) {
                                            moduleName = "Proposal";
                                        }
                                         else if(code == 11) {
                                            moduleName = "Proposal";
                                        }
                                         else if(code == 12) {
                                            moduleName = "Protocol";
                                        }

                                      pjtIdApprvd = (String)pjtBean.getCoiProjectId();

                                      if(entDetListApprvd != null) {
                                          String entPjtIdApprvd = "";
                                          entStatusIdApprvd = new TreeSet();
                                        for(int i=0; i < entDetListApprvd.size(); i++) {
                                            CoiDisclosureDetailsListBean entBeanApprvd = (CoiDisclosureDetailsListBean) entDetListApprvd.get(i);
                                            entPjtIdApprvd = (String)entBeanApprvd.getCoiProjectId();

                                            if(pjtIdApprvd.trim().equals(entPjtIdApprvd.trim())) {
                                                String entstatus = entBeanApprvd.getEntityStatusCode() + ":" + entBeanApprvd.getEntityStatusDesc();
                                                entStatusIdApprvd.add(entstatus);
                                            }
                                        }
                                      }

                                      if(entStatusIdApprvd != null && entStatusIdApprvd.size() > 0) {
                                         String statusDescCode =  (String)entStatusIdApprvd.last();
                                         String[] splitList = statusDescCode.split(":");
                                         entStatusDescriptionApprvd =  splitList[1];
                                      }

                                 }
                         %>
				<tr bgcolor="<%=strBgColor%>" id="row<%=index%>" class="rowLine"
					onmouseover="className='rowHover rowLine'"
					onmouseout="className='rowLine'" height="22px">
					<td width="3%"><img
						src='<%=request.getContextPath()%>/coeusliteimages/plus.gif'
						border='none' id="imgtoggleapp<%=index%>"
						name="imgtoggleapp<%=index%>" border="none"
						onclick="javascript:selectApprvdDiscEntityView(<%=index%>);" /> <img
						src='<%=request.getContextPath()%>/coeusliteimages/minus.gif'
						style="visibility: hidden;" border='none'
						id="imgtoggleminusapp<%=index%>"
						name="imgtoggleminusapp<%=index%>" border="none"
						onclick="javascript:selectApprvdDiscEntityView(<%=index%>);" /></td>
					<td style="width: 15%"><%=moduleName%></td>
					<td style="width: 18%"><bean:write name="pjtList"
							property="coiProjectTitle" /></td>
					<td style="width: 11%"><bean:write name="pjtList"
							property="coiProjectId" /></td>
					<td style="width: 12%"><%=startDate%></td>
					<td style="width: 12%"><%=endDate%></td>
					<td style="width: 16%">
						<%--<bean:write name="pjtList" property="coiDisclosureStatusDesc"/>--%><%=entStatusDescriptionApprvd%></td>
					<td style="width: 16%">&nbsp;</td>
				</tr>

				<tr id="tr<%=index%>"
					style="width: 100%; height: 1px; visibility: hidden; overflow: hidden;">
					<td style="width: 100%;" colspan="8">
						<div id="project<%=index%>"
							style="width: 100%; height: 1px; visibility: hidden; background-color: #9DBFE9; overflow-x: hidden; overflow-y: scroll;">
							<table id="bodyTable" class="table"
								style="width: 100%; height: 1px;" border="0">
								<tr style="height: 1px;">
									<td
										style="background-color: #6E97CF; color: #FFFFFF; font-size: 12px; font-weight: bold;">&nbsp;</td>
									<td colspan="3"
										style="background-color: #6E97CF; color: #FFFFFF; font-size: 12px; font-weight: bold;"><strong>Entity
											Name</strong></td>

									<td colspan="3"
										style="background-color: #6E97CF; color: #FFFFFF; font-size: 12px; font-weight: bold;"><strong>Entity
											Status</strong></td>
								</tr>

								<% int index1=0; %>
								<logic:empty name="apprvdEntPjtDetView">
									<tr style="height: 1px;">
										<td colspan="2" style="padding-left: 25px;"><font
											style="color: red">No Entities found</font></td>
									</tr>
								</logic:empty>
								<logic:iterate id="pjtEntView" name="apprvdEntPjtDetView">
									<% String projectNameApp = (String) pjtBean.getCoiProjectId();%>
									<logic:equal value="<%=projectNameApp%>"
										property="coiProjectId" name="pjtEntView">
										<logic:notEmpty name="pjtEntView" property="entityName">
											<% index1++; %>
											<tr class="rowLineLight"
												onmouseover="className='rowHover rowLine'"
												onmouseout="className='rowLineLight'" style="height: 1px;"
												width="100%">
												<td style="width: 5%"></td>
												<td colspan="3"><b><bean:write name="pjtEntView"
															property="entityName" /></b></td>
												<td colspan="3"><b><bean:write name="pjtEntView"
															property="entityStatusDesc" /></b></td>
											</tr>
										</logic:notEmpty>
									</logic:equal>
								</logic:iterate>

								<%if(index1 == 0){%>
								<tr style="height: 1px;">
									<td colspan="2" style="padding-left: 25px;"><font
										style="color: red">No financial entities found</font></td>
								</tr>
								<%}%>

							</table>
						</div>
					</td>
				</tr>
				<%
                            index++;
                         %>
			</logic:iterate>

		</logic:present>

	</table>
</div>

<div id="disclosureByApprvdFinancialEntityDiv"
	style="display: none; height: 1px;">
	<table id="bodyTable" class="table" style="width: 100%;" border="0">
		<tr style="background-color: #6E97CF; height: 22px;">
			<td>&nbsp;</td>
			<td style="width: 40%; color: #FFFFFF; font-size: 14px;"><strong>Entity
					Names</strong></td>
			<td style="width: 50%;" align="right"><a
				href="javaScript:setViewByProjectsApp();">View By Projects</a></td>
			<td><strong>&nbsp;</strong></td>
			<td><strong>&nbsp;</strong></td>
		</tr>

		<logic:empty name="apprvdEntityNameLists">
			<tr style="height: 1px;">
				<td colspan="2" style="padding-left: 25px;"><font
					style="color: red">No Entities found</font></td>
			</tr>
		</logic:empty>

		<logic:present name="apprvdEntityNameLists">
			<%
                                    int entity = 0;
                                 %>
			<logic:iterate id="pjtTitle" name="apprvdEntityNameLists">
				<%
                                                if (entity % 2 == 0) {
                                                    strBgColor = "#D6DCE5";
                                                } else {
                                                    strBgColor = "#DCE5F1";
                                                }
                                        %>

				<tr bgcolor="<%=strBgColor%>" id="row1<%=entity%>" class="rowLine"
					onmouseover="className='rowHover rowLine'"
					onmouseout="className='rowLine'" height="22px">
					<td colspan="1" width="5%"><img
						src='<%=request.getContextPath()%>/coeusliteimages/plus.gif'
						border='none' id="imgApptoggle<%=entity%>"
						name="imgtoggleapp<%=entity%>" border="none"
						onclick="javascript:getApprvdDiscPjtView(<%=entity%>);" /> <img
						src='<%=request.getContextPath()%>/coeusliteimages/minus.gif'
						style="visibility: hidden;" border='none'
						id="imgApptoggleminus<%=entity%>"
						name="imgApptoggleminus<%=entity%>" border="none"
						onclick="javascript:getApprvdDiscPjtView(<%=entity%>);" /></td>
					<td colspan="4"><b><bean:write name="pjtTitle" /></b></td>
				</tr>

				<tr>
					<td colspan="5">
						<div id="ent<%=entity%>"
							style="height: 1px; width: 100%; visibility: hidden; background-color: #9DBFE9; overflow-x: hidden; overflow-y: scroll;">
							<table id="bodyTable" class="table"
								style="width: 100%; height: 100%;" border="0">
								<tr style="background-color: #6E97CF;">
									<td width="3%" style="background-color: #538dd5;"></td>
									<td nowrap="nowrap"
										style="width: 15%; color: #FFFFFF; font-size: 12px; background-color: #538dd5;"><strong>Discl.Event
											Type</strong></td>
									<td
										style="width: 15%; color: #FFFFFF; font-size: 12px; background-color: #538dd5;"><strong>Title</strong></td>
									<td
										style="width: 15%; color: #FFFFFF; font-size: 12px; background-color: #538dd5;"><strong>Project
											#</strong></td>
									<td
										style="width: 12%; color: #FFFFFF; font-size: 12px; background-color: #538dd5;"><strong>Start
											Date</strong></td>
									<td
										style="width: 12%; color: #FFFFFF; font-size: 12px; background-color: #538dd5;"><strong>End
											Date</strong></td>
									<td nowrap="nowrap"
										style="width: 15%; color: #FFFFFF; font-size: 12px; background-color: #538dd5;"><strong>Discl.Event
											Status</strong></td>
								</tr>

								<logic:empty name="apprvdPjtEntDetailsViews">
									<tr style="height: 1px;">
										<td colspan="2" style="padding-left: 25px;"><font
											style="color: red">No Projects found</font></td>
									</tr>
								</logic:empty>
								<logic:present name="apprvdPjtEntDetailsViews">
									<% int pjtIndex = 0;
                                                        if(request.getAttribute("apprvdPjtEntDetailsViews") != null){
                                                             projetList = (Vector) request.getAttribute("apprvdPjtEntDetailsViews");
                                                          }
                                                       %>
									<logic:iterate id="pjtList" name="apprvdPjtEntDetailsViews">

										<%
                                                                if (pjtIndex % 2 == 0) {
                                                                    strBgColor = "#D6DCE5";
                                                                } else {
                                                                    strBgColor = "#DCE5F1";
                                                                }
                                                                String  moduleNm = "";
                                                                //clearing the strings
                                                                moduleNm="";
                                                                startDate="";
                                                                endDate="";
                                                                 pjtBean = (CoiDisclosureDetailsListBean) projetList.get(pjtIndex);
                                                                if(pjtBean != null){

                                                                    if(pjtBean.getCoiProjectStartDate() != null) {
                                                                        startDate = (String) coiCommonService1.getFormatedDate(pjtBean.getCoiProjectStartDate());
                                                                     }

                                                                    if(pjtBean.getCoiProjectEndDate() != null) {
                                                                        endDate = (String) coiCommonService1.getFormatedDate(pjtBean.getCoiProjectEndDate());
                                                                    }


                                                                        int code = pjtBean.getModuleCode();

                                                                        if(code == 3) {
                                                                            moduleNm = "Proposal";
                                                                        }
                                                                        else if(code == 7) {
                                                                            moduleNm = "IRB Protocol";
                                                                        }
                                                                        else if(code == 9) {
                                                                            moduleNm = "IACUC Protocol";
                                                                        }
                                                                        else if(code == 1) {
                                                                            moduleNm = "Award";
                                                                        }
                                                                        else if(code == 13) {
                                                                            moduleNm = "Annual";
                                                                        }
                                                                        else if(code == 12) {
                                                                            moduleNm = "Protocol";
                                                                        }
                                                                        else if(code == 2) {
                                                                            moduleNm = "Proposal";
                                                                        }
                                                                         else if(code == 11) {
                                                                            moduleNm = "Proposal";
                                                                        }
                                                                         else if(code == 12) {
                                                                            moduleNm = "Protocol";
                                                                        }

                                                                 }
                                                         %>
										<tr bgcolor="<%=strBgColor%>" id="row2<%=pjtIndex%>"
											class="rowLine" onmouseover="className='rowHover rowLine'"
											onmouseout="className='rowLine'" height="22px">
											<%-- <td width="3%">
                                                               <img src='<%=request.getContextPath()%>/coeusliteimages/plus.gif' border='none' id="imgtoggleapp<%=pjtIndex%>" name="imgtoggleapp<%=pjtIndex%>" border="none" onclick="javascript:selectProj(<%=pjtIndex%>);"/>
                                                               <img src='<%=request.getContextPath()%>/coeusliteimages/minus.gif' style="visibility: hidden;" border='none' id="imgtoggleminusapp<%=pjtIndex%>" name="imgtoggleminusapp<%=pjtIndex%>" border="none" onclick="javascript:selectProj(<%=pjtIndex%>);"/>
                                                            </td>--%>
											<td width="3%"></td>
											<td style="width: 15%;"><%=moduleNm%></td>
											<td style="width: 15%;"><bean:write name="pjtList"
													property="coiProjectTitle" /></td>
											<td style="width: 15%;"><bean:write name="pjtList"
													property="coiProjectId" /></td>
											<td style="width: 12%;"><%=startDate%></td>
											<td style="width: 12%;"><%=endDate%></td>
											<td style="width: 15%;"><bean:write name="pjtList"
													property="entityStatusDesc" /></td>
										</tr>
										<%pjtIndex++;%>
									</logic:iterate>

								</logic:present>

							</table>
						</div>
					</td>

				</tr>
				<%entity++;%>
			</logic:iterate>

		</logic:present>

	</table>
</div>

<%--</logic:present>--%>

<!--Closed Disclosure List -->

<%--<logic:present name="ApprovedDisclDetView">--%>
<table>
	<tr style="background-color: white">
		<td></td>
	</tr>
</table>
<table class="table" style="width: 100%;" border="0">
	<tr>
		<td><img
			src='<%=request.getContextPath()%>/coeusliteimages/plus.gif'
			border='none' id="closedimgtoggle" name="closedimgtoggle"
			border="none" onclick="javascript:selectClosedDiscIcon();" /> <img
			src='<%=request.getContextPath()%>/coeusliteimages/minus.gif'
			style="visibility: hidden;" border='none' id="closedimgtoggleminus"
			name="closedimgtoggleminus" border="none"
			onclick="javascript:selectClosedDiscIcon();" /> <label
			style="color: #333333; font-family: Arial, Helvetica, sans-serif; font-size: 12px; font-weight: bold;">Closed</label>
		</td>
	</tr>
</table>

<div id="closedDisclosureDiv" style="display: none; height: 1px;">
	<table id="bodyTable" class="table" style="width: 100%;" border="0">
		<tr style="background-color: #6E97CF; height: 22px;">
			<td style="width: 3%; background-color: #538dd5;"></td>
			<td nowrap="nowrap"
				style="width: 15%; color: #FFFFFF; font-size: 12px; background-color: #538dd5;"><strong>Discl.Event
					Type</strong></td>
			<td
				style="width: 18%; color: #FFFFFF; font-size: 12px; background-color: #538dd5;"><strong>Title</strong></td>
			<td
				style="width: 11%; color: #FFFFFF; font-size: 12px; background-color: #538dd5;"><strong>Project
					#</strong></td>
			<td
				style="width: 12%; color: #FFFFFF; font-size: 12px; background-color: #538dd5;"><strong>Start
					Date</strong></td>
			<td
				style="width: 12%; color: #FFFFFF; font-size: 12px; background-color: #538dd5;"><strong>End
					Date</strong></td>
			<td nowrap="nowrap"
				style="width: 16%; color: #FFFFFF; font-size: 12px; background-color: #538dd5;"><strong>Discl.Event
					Status</strong></td>
			<td style="width: 16%;" align="right"><a
				href="javaScript:setViewByClosedFinancialEnity();">View By
					FinancialEntities</a></td>
		</tr>

		<logic:empty name="cldPjtEntDetailsViews">
			<tr style="height: 1px;">
				<td colspan="2" style="padding-left: 25px;"><font
					style="color: red">No Projects found</font></td>
			</tr>
		</logic:empty>

		<logic:present name="cldPjtEntDetailsViews">
			<% int cldIndex = 0;
                            TreeSet entStatusIdClsd = new TreeSet();
                            String pjtIdClsd = "";
                            String entStatusDescriptionClsd = "";
                        Vector entDetListClsd = new Vector();
                        if(request.getAttribute("cldPjtEntDetailsViews") != null){
                             projetList = (Vector) request.getAttribute("cldPjtEntDetailsViews");
                          }

                        if(request.getAttribute("cldEntPjtDetView") != null){
                              entDetListClsd = (Vector)request.getAttribute("cldEntPjtDetView");
                          }
                       %>
			<logic:iterate id="pjtList" name="cldPjtEntDetailsViews">

				<%
                                if (cldIndex % 2 == 0) {
                                    strBgColor = "#D6DCE5";
                                } else {
                                    strBgColor = "#DCE5F1";
                                }

				String moduleNameCld = "";
                                //clearing the strings
                                moduleNameCld="";
                                startDate="";
                                closedpjctendDate="";
                                 pjtBean = (CoiDisclosureDetailsListBean) projetList.get(cldIndex);
                                if(pjtBean != null){

                                    if(pjtBean.getCoiProjectStartDate() != null) {
                                        startDate = (String) coiCommonService1.getFormatedDate(pjtBean.getCoiProjectStartDate());
                                     }

                                    if(pjtBean.getCoiProjectEndDate() != null) {
                                      closedpjctendDate = (String) coiCommonService1.getFormatedDate(pjtBean.getCoiProjectEndDate());
                                    }

                                        int code = pjtBean.getModuleCode();

                                        if(code == 3) {
                                            moduleNameCld = "Proposal";
                                        }
                                        else if(code == 7) {
                                            moduleNameCld = "IRB Protocol";
                                        }
                                        else if(code == 9) {
                                            moduleNameCld = "IACUC Protocol";
                                        }
                                        else if(code == 1) {
                                            moduleNameCld = "Award";
                                        }
                                        else if(code == 13) {
                                            moduleNameCld = "Annual";
                                        }
                                        else if(code == 12) {
                                            moduleNameCld = "Protocol";
                                        }
                                         else if(code == 2) {
                                            moduleNameCld = "Proposal";
                                        }
                                         else if(code == 11) {
                                            moduleNameCld = "Proposal";
                                        }
                                         else if(code == 12) {
                                            moduleNameCld = "Protocol";
                                        }

                                      pjtIdClsd = (String)pjtBean.getCoiProjectId();

                                      if(entDetListClsd != null) {
                                          entStatusIdClsd = new TreeSet();
                                          String entPjtIdClsd = "";
                                        for(int i=0; i < entDetListClsd.size(); i++) {
                                            CoiDisclosureDetailsListBean entBean = (CoiDisclosureDetailsListBean) entDetListClsd.get(i);
                                            entPjtIdClsd = (String)entBean.getCoiProjectId();

                                            if(pjtIdClsd.trim().equals(entPjtIdClsd.trim())) {
                                                String entstatus = entBean.getEntityStatusCode() + ":" + entBean.getEntityStatusDesc();
                                                entStatusIdClsd.add(entstatus);
                                            }
                                        }
                                      }

                                      if(entStatusIdClsd != null && entStatusIdClsd.size() > 0) {
                                         String statusDescCode =  (String)entStatusIdClsd.last();
                                         String[] splitList = statusDescCode.split(":");
                                         entStatusDescriptionClsd =  splitList[1];
                                      }
                                 }
                         %>
				<tr bgcolor="<%=strBgColor%>" id="rowcld<%=cldIndex%>"
					class="rowLine" onmouseover="className='rowHover rowLine'"
					onmouseout="className='rowLine'" height="22px">
					<td width="3%"><img
						src='<%=request.getContextPath()%>/coeusliteimages/plus.gif'
						border='none' id="imgtogglecld<%=cldIndex%>"
						name="imgtogglecld<%=cldIndex%>" border="none"
						onclick="javascript:selectClosedDiscEntityView(<%=cldIndex%>);" />
						<img src='<%=request.getContextPath()%>/coeusliteimages/minus.gif'
						style="visibility: hidden;" border='none'
						id="imgtoggleminuscld<%=cldIndex%>"
						name="imgtoggleminuscld<%=cldIndex%>" border="none"
						onclick="javascript:selectClosedDiscEntityView(<%=cldIndex%>);" />
					</td>
					<td style="width: 15%;"><%=moduleNameCld%></td>
					<td style="width: 18%;"><bean:write name="pjtList"
							property="coiProjectTitle" /></td>
					<td style="width: 11%;"><bean:write name="pjtList"
							property="coiProjectId" /></td>
					<td style="width: 12%;"><%=startDate%></td>
					<td style="width: 12%;"><%=closedpjctendDate%></td>
					<td style="width: 16%;">
						<%--<bean:write name="pjtList" property="coiDisclosureStatusDesc"/>--%><%=entStatusDescriptionClsd%></td>
					<td style="width: 16%">&nbsp;</td>
				</tr>
				<tr id="trcld<%=cldIndex%>"
					style="width: 100%; height: 1px; visibility: hidden; overflow: hidden;">
					<td style="width: 100%;" colspan="8">
						<div id="projectcld<%=cldIndex%>"
							style="width: 100%; height: 1px; visibility: hidden; background-color: #9DBFE9; overflow-x: hidden; overflow-y: scroll;">
							<table id="bodyTable" class="table"
								style="width: 100%; height: 1px;" border="0">
								<tr style="height: 1px;">
									<td
										style="background-color: #6E97CF; color: #FFFFFF; font-size: 12px; font-weight: bold;">&nbsp;</td>
									<td colspan="3"
										style="background-color: #6E97CF; color: #FFFFFF; font-size: 12px; font-weight: bold;"><strong>Entity
											Name</strong></td>

									<td colspan="3"
										style="background-color: #6E97CF; color: #FFFFFF; font-size: 12px; font-weight: bold;"><strong>Entity
											Status</strong></td>
								</tr>

								<% int cldIndex1=0; %>
								<logic:iterate id="pjtEntView" name="cldEntPjtDetView">
									<% String projectNameCld = (String) pjtBean.getCoiProjectId();%>
									<logic:equal value="<%=projectNameCld%>"
										property="coiProjectId" name="pjtEntView">
										<logic:notEmpty name="pjtEntView" property="entityName">
											<% cldIndex1++; %>
											<tr class="rowLineLight"
												onmouseover="className='rowHover rowLine'"
												onmouseout="className='rowLineLight'" style="height: 1px;"
												width="100%">
												<td style="width: 5%"></td>
												<td colspan="3"><b><bean:write name="pjtEntView"
															property="entityName" /></b></td>
												<td colspan="3"><b><bean:write name="pjtEntView"
															property="entityStatusDesc" /></b></td>
											</tr>
										</logic:notEmpty>
									</logic:equal>
								</logic:iterate>

								<%if(cldIndex1 == 0){%>
								<tr style="height: 1px;">
									<td colspan="2" style="padding-left: 25px;"><font
										style="color: red">No financial entities found</font></td>
								</tr>
								<%}%>

							</table>
						</div>
					</td>
				</tr>
				<%
                            cldIndex++;
                         %>
			</logic:iterate>

		</logic:present>

	</table>
</div>

<div id="disclosureByClosedFinancialEntityDiv"
	style="display: none; height: 1px;">
	<table id="bodyTable" class="table" style="width: 100%;" border="0">
		<tr style="background-color: #6E97CF; height: 22px;">
			<td>&nbsp;</td>
			<td style="width: 40%; color: #FFFFFF; font-size: 14px;"><strong>Entity
					Names</strong></td>
			<td style="width: 50%;" align="right"><a
				href="javaScript:setViewByProjectsClosed();">View By Projects</a></td>
			<td><strong>&nbsp;</strong></td>
			<td><strong>&nbsp;</strong></td>
		</tr>

		<logic:empty name="cldEntityNameLists">
			<tr style="height: 1px;">
				<td colspan="2" style="padding-left: 25px;"><font
					style="color: red">No Entities found</font></td>
			</tr>
		</logic:empty>

		<logic:present name="cldEntityNameLists">
			<%
                            int cldentity = 0;
                     %>
			<logic:iterate id="pjtTitle" name="cldEntityNameLists">
				<%
                            if (cldentity % 2 == 0) {
                                    strBgColor = "#D6DCE5";
                            } else {
                                    strBgColor = "#DCE5F1";
                            }
                        %>

				<tr bgcolor="<%=strBgColor%>" id="row1<%=cldentity%>"
					class="rowLine" onmouseover="className='rowHover rowLine'"
					onmouseout="className='rowLine'" height="22px">
					<td colspan="1" width="5%"><img
						src='<%=request.getContextPath()%>/coeusliteimages/plus.gif'
						border='none' id="imgcldtoggle<%=cldentity%>"
						name="imgcldtoggle<%=cldentity%>" border="none"
						onclick="javascript:getClosedDiscPjtView(<%=cldentity%>);" /> <img
						src='<%=request.getContextPath()%>/coeusliteimages/minus.gif'
						style="visibility: hidden;" border='none'
						id="imgcldtoggleminus<%=cldentity%>"
						name="imgcldtoggleminus<%=cldentity%>" border="none"
						onclick="javascript:getClosedDiscPjtView(<%=cldentity%>);" /></td>
					<td colspan="4"><b><bean:write name="pjtTitle" /></b></td>
				</tr>

				<tr>
					<td colspan="5">
						<div id="entcld<%=cldentity%>"
							style="height: 1px; width: 100%; visibility: hidden; background-color: #9DBFE9; overflow-x: hidden; overflow-y: scroll;">
							<table id="bodyTable" class="table"
								style="width: 100%; height: 100%;" border="0">
								<tr style="background-color: #6E97CF;">
									<td width="3%" style="background-color: #538dd5;"></td>
									<td nowrap="nowrap"
										style="width: 20%; color: #FFFFFF; font-size: 12px; background-color: #538dd5;"><strong>Discl.Event
											Type</strong></td>
									<td
										style="width: 15%; color: #FFFFFF; font-size: 12px; background-color: #538dd5;"><strong>Title</strong></td>
									<td
										style="width: 15%; color: #FFFFFF; font-size: 12px; background-color: #538dd5;"><strong>Project
											#</strong></td>
									<td
										style="width: 12%; color: #FFFFFF; font-size: 12px; background-color: #538dd5;"><strong>Start
											Date</strong></td>
									<td
										style="width: 12%; color: #FFFFFF; font-size: 12px; background-color: #538dd5;"><strong>End
											Date</strong></td>
									<td nowrap="nowrap"
										style="width: 15%; color: #FFFFFF; font-size: 12px; background-color: #538dd5;"><strong>Discl.Event
											Status</strong></td>
								</tr>

								<logic:empty name="cldPjtEntDetailsViews">
									<tr style="height: 1px;">
										<td colspan="2" style="padding-left: 25px;"><font
											style="color: red">No Projects found</font></td>
									</tr>
								</logic:empty>
								<logic:present name="cldPjtEntDetailsViews">
									<% int cldpjtIndex = 0;
                                                if(request.getAttribute("cldPjtEntDetailsViews") != null){
                                                         projetList = (Vector) request.getAttribute("cldPjtEntDetailsViews");
                                                  }
                                           %>
									<logic:iterate id="pjtList" name="cldPjtEntDetailsViews">

										<%
                                                    if (cldpjtIndex % 2 == 0) {
                                                            strBgColor = "#D6DCE5";
                                                    } else {
                                                            strBgColor = "#DCE5F1";
                                                    }
                                                    String  moduleNmCld = "";
                                                    //clearing the strings
                                                    moduleNmCld="";
                                                    startDate="";
                                                    endDate="";

                                                     pjtBean = (CoiDisclosureDetailsListBean) projetList.get(cldpjtIndex);
                                                    if(pjtBean != null){

                                                        if(pjtBean.getCoiProjectStartDate() != null) {
                                                                startDate = (String) coiCommonService1.getFormatedDate(pjtBean.getCoiProjectStartDate());
                                                         }

                                                        if(pjtBean.getCoiProjectEndDate() != null) {
                                                                endDate = (String) coiCommonService1.getFormatedDate(pjtBean.getCoiProjectEndDate());
                                                        }


                                                        int code = pjtBean.getModuleCode();

                                                        if(code == 3) {
                                                                moduleNmCld = "Proposal";
                                                        }
                                                        else if(code == 7) {
                                                                moduleNmCld = "IRB Protocol";
                                                        }
                                                        else if(code == 9) {
                                                                moduleNmCld = "IACUC Protocol";
                                                        }
                                                        else if(code == 1) {
                                                                moduleNmCld = "Award";
                                                        }
                                                        else if(code == 13) {
                                                                moduleNmCld = "Annual";
                                                        }
                                                        else if(code == 12) {
                                                                moduleNmCld = "Protocol";
                                                        }
                                                         else if(code == 2) {
                                                            moduleNmCld = "Proposal";
                                                        }
                                                         else if(code == 11) {
                                                            moduleNmCld = "Proposal";
                                                        }
                                                         else if(code == 12) {
                                                            moduleNmCld = "Protocol";
                                                        }

                                                     }
                                                 %>
										<tr bgcolor="<%=strBgColor%>" id="rowcld<%=cldpjtIndex%>"
											class="rowLine" onmouseover="className='rowHover rowLine'"
											onmouseout="className='rowLine'" height="22px">

											<td width="3%"></td>
											<td style="width: 20%;"><%=moduleNmCld%></td>
											<td style="width: 15%;"><bean:write name="pjtList"
													property="coiProjectTitle" /></td>
											<td style="width: 15%;"><bean:write name="pjtList"
													property="coiProjectId" /></td>
											<td style="width: 12%;"><%=startDate%></td>
											<td style="width: 12%;"><%=endDate%></td>
											<td style="width: 15%;"><bean:write name="pjtList"
													property="entityStatusDesc" /></td>
										</tr>
										<%cldpjtIndex++;%>
									</logic:iterate>
									<%if(cldpjtIndex == 0){%>
									<tr style="height: 1px;">
										<td colspan="2" style="padding-left: 25px;"><font
											style="color: red">No Projects found</font></td>
									</tr>
									<%}%>
								</logic:present>

							</table>
						</div>
					</td>

				</tr>
				<%cldentity++;%>
			</logic:iterate>

		</logic:present>

	</table>
</div>

<!--travel disclosure---->
<table>
	<tr style="background-color: white">
		<td></td>
	</tr>
</table>
<table class="table" style="width: 100%;" border="0">
	<tr>
		<td><img
			src='<%=request.getContextPath()%>/coeusliteimages/plus.gif'
			border='none' id="travelimgtoggle" name="travelimgtoggle"
			border="none" onclick="javascript:selectTravelDiscIcon();" /> <img
			src='<%=request.getContextPath()%>/coeusliteimages/minus.gif'
			style="visibility: hidden;" border='none' id="travelimgtoggleminus"
			name="travelimgtoggleminus" border="none"
			onclick="javascript:selectTravelDiscIcon();" /> <label
			style="color: #333333; font-family: Arial, Helvetica, sans-serif; font-size: 12px; font-weight: bold;">Reported
				Travel Events</label></td>
	</tr>
</table>
<div id="travelDisclosureDiv" style="display: none; height: 1px;">
	<table id="bodyTable" class="table" style="width: 100%;" border="0">
		<tr style="background-color: #6E97CF; height: 22px;">
			<td style="width: 3%; background-color: #538dd5;"></td>
			<td nowrap="nowrap"
				style="width: 15%; color: #FFFFFF; font-size: 12px; background-color: #538dd5;"><strong>Discl.Event
					Type</strong></td>
			<td
				style="width: 18%; color: #FFFFFF; font-size: 12px; background-color: #538dd5;"><strong>Title</strong></td>
			<td
				style="width: 11%; color: #FFFFFF; font-size: 12px; background-color: #538dd5;"><strong>Project
					#</strong></td>
			<td
				style="width: 12%; color: #FFFFFF; font-size: 12px; background-color: #538dd5;"><strong>Start
					Date</strong></td>
			<td
				style="width: 12%; color: #FFFFFF; font-size: 12px; background-color: #538dd5;"><strong>End
					Date</strong></td>
			<td nowrap="nowrap"
				style="width: 16%; color: #FFFFFF; font-size: 12px; background-color: #538dd5;"><strong>Discl.Event
					Status</strong></td>
			<td style="width: 16%; color: #FFFFFF; background-color: #538dd5;"></td>
		</tr>


		<logic:empty name="tvlPjtEntDetailsViews">
			<tr style="height: 1px;">
				<td colspan="2" style="padding-left: 25px;"><font
					style="color: red">No Projects found</font></td>
			</tr>
		</logic:empty>
		<logic:present name="tvlPjtEntDetailsViews">
			<% int tvlIndex = 0;
                            TreeSet entStatusIdTvl = new TreeSet();
                            String pjtIdTvl= "";
                            String entStatusDescriptionTvl = "";
                        Vector entDetListTvl = new Vector();
                        if(request.getAttribute("tvlPjtEntDetailsViews") != null){
                             projetList = (Vector) request.getAttribute("tvlPjtEntDetailsViews");
                          }

                        if(request.getAttribute("tvlEntPjtDetView") != null){
                              entDetListTvl = (Vector)request.getAttribute("tvlEntPjtDetView");
                          }
                       %>
			<logic:iterate id="pjtList" name="tvlPjtEntDetailsViews">

				<%
                                if (tvlIndex % 2 == 0) {
                                    strBgColor = "#D6DCE5";
                                } else {
                                    strBgColor = "#DCE5F1";
                                }

				String moduleNameTvl = "";
                                //clearing the strings
                                moduleNameTvl="";
                                startDate="";
                                closedpjctendDate="";
                                 pjtBean = (CoiDisclosureDetailsListBean) projetList.get(tvlIndex);
                                if(pjtBean != null){

                                    if(pjtBean.getCoiProjectStartDate() != null) {
                                        startDate = (String) coiCommonService1.getFormatedDate(pjtBean.getCoiProjectStartDate());
                                     }

                                    if(pjtBean.getCoiProjectEndDate() != null) {
                                      closedpjctendDate = (String) coiCommonService1.getFormatedDate(pjtBean.getCoiProjectEndDate());
                                    }

                                        int code_t = pjtBean.getModuleCode();

                                        if(code_t == 3) {
                                            moduleNameTvl = "Proposal";
                                        }
                                        else if(code_t == 7) {
                                            moduleNameTvl = "IRB Protocol";
                                        }
                                        else if(code_t == 9) {
                                            moduleNameTvl = "IACUC Protocol";
                                        }
                                        else if(code_t == 1) {
                                            moduleNameTvl = "Award";
                                        }
                                        else if(code_t == 13) {
                                            moduleNameTvl = "Annual";
                                        }
                                        else if(code_t == 12) {
                                            moduleNameTvl = "Protocol";
                                        }
                                         else if(code_t == 2) {
                                           moduleNameTvl = "Proposal";
                                        }
                                         else if(code_t == 11) {
                                            moduleNameTvl = "Proposal";
                                        }
                                         else if(code_t == 12) {
                                            moduleNameTvl = "Protocol";
                                        }
                                         else if(code_t == 0) {
                                            moduleNameTvl = "Travel";
                                        }

                                      pjtIdTvl = (String)pjtBean.getCoiProjectId();

                                      if(entDetListTvl != null) {
                                          entStatusIdTvl = new TreeSet();
                                          String entPjtIdClsd = "";
                                        for(int i=0; i < entDetListTvl.size(); i++) {
                                            CoiDisclosureDetailsListBean entBean = (CoiDisclosureDetailsListBean) entDetListTvl.get(i);
                                            entPjtIdClsd = (String)entBean.getCoiProjectId();

                                            if(pjtIdTvl.trim().equals(entPjtIdClsd.trim())) {
                                                String entstatus = entBean.getEntityStatusCode() + ":" + entBean.getEntityStatusDesc();
                                                entStatusIdTvl.add(entstatus);
                                            }
                                        }
                                      }

                                      if(entStatusIdTvl != null && entStatusIdTvl.size() > 0) {
                                         String statusDescCode =  (String)entStatusIdTvl.last();
                                         String[] splitList = statusDescCode.split(":");
                                         entStatusDescriptionTvl =  splitList[1];
                                      }
                                 }
                         %>
				<tr bgcolor="<%=strBgColor%>" id="rowcld<%=tvlIndex%>"
					class="rowLine" onmouseover="className='rowHover rowLine'"
					onmouseout="className='rowLine'" height="22px">
					<td width="3%"></td>
					<td style="width: 15%;"><%=moduleNameTvl%></td>
					<td style="width: 18%;"><bean:write name="pjtList"
							property="eventName" /></td>
					<td style="width: 11%;"><bean:write name="pjtList"
							property="coiProjectId" /></td>
					<td style="width: 12%;"><%=startDate%></td>
					<td style="width: 12%;"><%=closedpjctendDate%></td>
					<td style="width: 16%;">
						<%--<bean:write name="pjtList" property="coiDisclosureStatusDesc"/>--%><%=entStatusDescriptionTvl%></td>
					<td style="width: 16%">&nbsp;</td>
				</tr>
				<tr id="trTrvl<%=tvlIndex%>"
					style="width: 100%; height: 1px; visibility: hidden; overflow: hidden;">
					<td style="width: 100%;" colspan="8">
						<div id="projectTrvl<%=tvlIndex%>"
							style="width: 100%; height: 1px; visibility: hidden; background-color: #9DBFE9; overflow-x: hidden; overflow-y: scroll;">
							<table id="bodyTable" class="table"
								style="width: 100%; height: 1px;" border="0">
								<tr style="height: 1px;">
									<td
										style="background-color: #6E97CF; color: #FFFFFF; font-size: 12px; font-weight: bold;">&nbsp;</td>
									<td colspan="3"
										style="background-color: #6E97CF; color: #FFFFFF; font-size: 12px; font-weight: bold;"><strong>Entity
											Name</strong></td>

									<td colspan="3"
										style="background-color: #6E97CF; color: #FFFFFF; font-size: 12px; font-weight: bold;"><strong>Entity
											Status</strong></td>
								</tr>

								<% int tvlIndex1=0;%>
								<logic:iterate id="pjtEntView" name="tvlEntPjtDetView">
									<% String projectNameTvl = (String) pjtBean.getCoiProjectId();%>
									<logic:equal value="<%=projectNameTvl%>"
										property="coiProjectId" name="pjtEntView">
										<logic:notEmpty name="pjtEntView" property="entityName">
											<% tvlIndex1++; %>
											<tr class="rowLineLight"
												onmouseover="className='rowHover rowLine'"
												onmouseout="className='rowLineLight'" style="height: 1px;"
												width="100%">
												<td style="width: 5%"></td>
												<td colspan="3"><b><bean:write name="pjtEntView"
															property="entityName" /></b></td>
												<td colspan="3"><b><bean:write name="pjtEntView"
															property="entityStatusDesc" /></b></td>
											</tr>
										</logic:notEmpty>
									</logic:equal>
								</logic:iterate>

								<%if(tvlIndex1 == 0){%>
								<tr style="height: 1px;">
									<td colspan="2" style="padding-left: 25px;"><font
										style="color: red">No financial entities found</font></td>
								</tr>
								<%}%>

							</table>
						</div>
					</td>
				</tr>
				<%
                            tvlIndex++;
                         %>
			</logic:iterate>

		</logic:present>

	</table>
</div>

<div id="disclosureByTravelFinancialEntityDiv"
	style="display: none; height: 1px;">
	<table id="bodyTable" class="table" style="width: 100%;" border="0">
		<tr style="background-color: #6E97CF; height: 22px;">
			<td>&nbsp;</td>
			<td style="width: 40%; color: #FFFFFF; font-size: 14px;"><strong>Entity
					Names</strong></td>
			<td style="width: 50%;" align="right"><a
				href="javascript:setViewByProjectsTravel();">View By Projects</a></td>
			<td><strong>&nbsp;</strong></td>
			<td><strong>&nbsp;</strong></td>
		</tr>

		<logic:empty name="tvlEntityNameLists">
			<tr style="height: 1px;">
				<td colspan="2" style="padding-left: 25px;"><font
					style="color: red">No Entities found</font></td>
			</tr>
		</logic:empty>
		<logic:present name="tvlEntityNameLists">
			<%
                            int tentity = 0;
                     %>
			<logic:iterate id="pjtTitle" name="tvlEntityNameLists">
				<%
                            if (tentity % 2 == 0) {
                                    strBgColor = "#D6DCE5";
                            } else {
                                    strBgColor = "#DCE5F1";
                            }
                        %>

				<tr bgcolor="<%=strBgColor%>" id="row1<%=tentity%>" class="rowLine"
					onmouseover="className='rowHover rowLine'"
					onmouseout="className='rowLine'" height="22px">
					<td colspan="1" width="5%"><img
						src='<%=request.getContextPath()%>/coeusliteimages/plus.gif'
						border='none' id="imgtvltoggle<%=tentity%>"
						name="imgtvltoggle<%=tentity%>" border="none"
						onclick="javascript:getTravelDiscPjtView(<%=tentity%>);" /> <img
						src='<%=request.getContextPath()%>/coeusliteimages/minus.gif'
						style="visibility: hidden;" border='none'
						id="imgtvltoggleminus<%=tentity%>"
						name="imgtvltoggleminus<%=tentity%>" border="none"
						onclick="javascript:getTravelDiscPjtView(<%=tentity%>);" /></td>
					<td colspan="4"><b><bean:write name="pjtTitle" /></b></td>
				</tr>

				<tr>
					<td colspan="5">
						<div id="entTvl<%=tentity%>"
							style="height: 1px; width: 100%; visibility: hidden; background-color: #9DBFE9; overflow-x: hidden; overflow-y: scroll;">
							<table id="bodyTable" class="table"
								style="width: 100%; height: 100%;" border="0">
								<tr style="background-color: #6E97CF;">
									<td width="3%" style="background-color: #538dd5;"></td>
									<td nowrap="nowrap"
										style="width: 20%; color: #FFFFFF; font-size: 12px; background-color: #538dd5;"><strong>Discl.Event
											Type</strong></td>
									<td
										style="width: 15%; color: #FFFFFF; font-size: 12px; background-color: #538dd5;"><strong>Title</strong></td>
									<td
										style="width: 15%; color: #FFFFFF; font-size: 12px; background-color: #538dd5;"><strong>Project
											#</strong></td>
									<td
										style="width: 12%; color: #FFFFFF; font-size: 12px; background-color: #538dd5;"><strong>Start
											Date</strong></td>
									<td
										style="width: 12%; color: #FFFFFF; font-size: 12px; background-color: #538dd5;"><strong>End
											Date</strong></td>
									<td nowrap="nowrap"
										style="width: 15%; color: #FFFFFF; font-size: 12px; background-color: #538dd5;"><strong>Discl.Event
											Status</strong></td>
								</tr>

								<logic:empty name="tvlPjtEntDetailsViews">
									<tr style="height: 1px;">
										<td colspan="2" style="padding-left: 25px;"><font
											style="color: red">No Projects found</font></td>
									</tr>
								</logic:empty>
								<logic:present name="tvlPjtEntDetailsViews">
									<% int tvlpjtIndex = 0;
                                                if(request.getAttribute("tvlPjtEntDetailsViews") != null){
                                                         projetList = (Vector) request.getAttribute("tvlPjtEntDetailsViews");
                                                  }
                                           %>
									<logic:iterate id="pjtList" name="tvlPjtEntDetailsViews">

										<%
                                                    if (tvlpjtIndex % 2 == 0) {
                                                            strBgColor = "#D6DCE5";
                                                    } else {
                                                            strBgColor = "#DCE5F1";
                                                    }
                                                    String  moduleNmTvl = "";
                                                    //clearing the strings
                                                    moduleNmTvl="";
                                                    startDate="";
                                                    endDate="";

                                                     pjtBean = (CoiDisclosureDetailsListBean) projetList.get(tvlpjtIndex);
                                                    if(pjtBean != null){

                                                        if(pjtBean.getCoiProjectStartDate() != null) {
                                                                startDate = (String) coiCommonService1.getFormatedDate(pjtBean.getCoiProjectStartDate());
                                                         }

                                                        if(pjtBean.getCoiProjectEndDate() != null) {
                                                                endDate = (String) coiCommonService1.getFormatedDate(pjtBean.getCoiProjectEndDate());
                                                        }


                                                        int code_t1 = pjtBean.getModuleCode();

                                                        if(code_t1 == 3) {
                                                                moduleNmTvl = "Proposal";
                                                        }
                                                        else if(code_t1 == 7) {
                                                                moduleNmTvl = "IRB Protocol";
                                                        }
                                                        else if(code_t1 == 9) {
                                                                moduleNmTvl = "IACUC Protocol";
                                                        }
                                                        else if(code_t1 == 1) {
                                                               moduleNmTvl = "Award";
                                                        }
                                                        else if(code_t1 == 13) {
                                                                moduleNmTvl = "Annual";
                                                        }
                                                        else if(code_t1 == 12) {
                                                                moduleNmTvl = "Protocol";
                                                        }
                                                         else if(code_t1 == 2) {
                                                            moduleNmTvl = "Proposal";
                                                        }
                                                         else if(code_t1 == 11) {
                                                            moduleNmTvl = "Proposal";
                                                        }
                                                         else if(code_t1 == 12) {
                                                            moduleNmTvl = "Protocol";
                                                        }
                                                         else if(code_t1 == 0) {
                                                            moduleNmTvl = "Travel";
                                                        }

                                                     }
                                                 %>
										<tr bgcolor="<%=strBgColor%>" id="rowtvl<%=tvlpjtIndex%>"
											class="rowLine" onmouseover="className='rowHover rowLine'"
											onmouseout="className='rowLine'" height="22px">

											<td width="3%"></td>
											<td style="width: 20%;"><%=moduleNmTvl%></td>
											<td style="width: 15%;"><bean:write name="pjtList"
													property="eventName" /></td>
											<td style="width: 15%;"><bean:write name="pjtList"
													property="coiProjectId" /></td>
											<td style="width: 12%;"><%=startDate%></td>
											<td style="width: 12%;"><%=endDate%></td>
											<td style="width: 15%;"><bean:write name="pjtList"
													property="entityStatusDesc" /></td>
										</tr>
										<%tvlpjtIndex++;%>
									</logic:iterate>
									<%if(tvlpjtIndex == 0){%>
									<tr style="height: 1px;">
										<td colspan="2" style="padding-left: 25px;"><font
											style="color: red">No Projects found</font></td>
									</tr>
									<%}%>
								</logic:present>

							</table>
						</div>
					</td>

				</tr>
				<%tentity++;%>
			</logic:iterate>

		</logic:present>
	</table>
</div>


</html:html>