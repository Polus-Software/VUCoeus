<%--
/*
 * @(#)CoeusContextPath.jsp 1.0  2002/05/08 9:45:14 AM
 *
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 *
 * @author  RaYaKu
 */
--%>
<%-- A Non view component but helps other view components to know 
		 the contextpath of coeus application in a servlet container
--%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/request.tld" prefix="req"%>
<req:request id="req" />
<bean:define id="ctxtPath" name="req" property="contextPath"
	scope="page" />