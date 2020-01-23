<!--  Copyright (c) Massachusetts Institute of Technology
  77 Massachusetts Avenue, Cambridge, MA 02139-4307
All rights reserved.-->
<%@ page import="org.apache.struts.taglib.html.JavascriptValidatorTag"%>
<%@page contentType="text/html"%>
<%@page pageEncoding="UTF-8"%>

<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ include file= "/coeuslite/mit/utils/CoeusContextPath.jsp"  %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
"http://www.w3.org/TR/html4/loose.dtd">

<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Print Questionnaire</title>
        <link href="<bean:write name='ctxtPath'/>/coeuslite/mit/utils/css/coeus_styles.css" rel="stylesheet" type="text/css">
        <link href="/coeuslite/mit/utils/css/ipf.css" rel="stylesheet" type="text/css">
        <script>
                function processData(operation){
                    var printQuestion;
                    var printAllQuestion;
                    for(var i = 0; i < document.forms[0].printQuestions.length; i++) {
                        if(document.forms[0].printQuestions[i].checked) {
                            printQuestion = document.forms[0].printQuestions[i].value;
                        }
                    }
                    for(var i = 0; i < document.forms[0].printAllQuestions.length; i++) {
                        if(document.forms[0].printAllQuestions[i].checked) {
                            printAllQuestion = document.forms[0].printAllQuestions[i].value;
                        }
                    }
                    window.open("<%=request.getContextPath()%>/saveQuestionnaireData.do?operation="+operation+"&printQuestions="+printQuestion+"&printAllQuestions="+printAllQuestion);
                    window.close();
                }
                
                function hideSubOptions() {
                    for(var i = 0; i < document.forms[0].printAllQuestions.length; i++) {
                        if(!document.forms[0].printQuestions[1].checked) {
                            document.forms[0].printAllQuestions[i].disabled = true;
                        }
                    }
                }

                function displaySubOptions() {
                    for(var i = 0; i < document.forms[0].printAllQuestions.length; i++) {
                        if(document.forms[0].printQuestions[1].checked) {
                            document.forms[0].printAllQuestions[i].disabled = false;
                        }
                    }
                }

        </script>
    </head>
    <body class="tabtable" onload="hideSubOptions()">
        <form  action="/saveQuestionnaireData" method="POST" name="dynaBeanList">
            <table width="100%" height="100%"  border="0" cellpadding="0" cellspacing="0">
                <tr>
                    <td width="20%"></td>
                    <td class="copy">
                        <input type="radio" name = "printQuestions" value="Y" onclick="javascript:hideSubOptions()" checked/><bean:message bundle="proposal" key="questionnaire.printing.printquestions"/>
                                                                                                                                    </td>
                </tr>
                <tr>
                    <td width="20%"></td>
                    <td class="copy">
                        <input type="radio" name = "printQuestions" value="N" onclick="javascript:displaySubOptions()"/><bean:message bundle="proposal" key="questionnaire.printing.printquestionanswers"/>
                    </td>
                </tr>
                <tr>
                    <td width="20%"></td>
                    <td>
                        <table width="100%" border="0" cellpadding="0" cellspacing="0" >
                            <tr>
                                <td width="7%"></td>
                                <td class="copy">
                                    <input type="radio" name="printAllQuestions" value="Y" checked/><bean:message bundle="proposal" key="questionnaire.printing.allquestions"/>
                                </td>
                            </tr>
                            <tr>
                                <td width="7%"></td>
                                <td class="copy">
                                    <input type="radio" name="printAllQuestions" value="N"/><bean:message bundle="proposal" key="questionnaire.printing.answerdquestions"/>
                                </td>
                            </tr>
                        </table>
                    </td>
                </tr>
                <tr>
                    <td width="20%"></td>
                    <td class="copy">
                        <input type="button" class="clsavebutton" value="Print" onclick="javascript:processData('PRINT')"/>
                        &nbsp;&nbsp;&nbsp;&nbsp;<input type="button" class="clsavebutton" value="Cancel" onclick="javascript:window.close()"/>
                    </td>
                </tr>
                <input type="hidden" name="printQuestionsOnly"/>
            </table>
        </form>
    </body>
</html>
