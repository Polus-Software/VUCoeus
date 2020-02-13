
<%-- A Non view component but helps other view components to know 
		 the contextpath of coeus application in a servlet container
--%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/request.tld" prefix="req"%>
<req:request id="req" />
<bean:define id="ctxtPath" name="req" property="contextPath"
	scope="page" />