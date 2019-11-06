<%--
/*
 * @(#)CoeusCacheRemove.jsp	1.0 2002/06/05 16:51:22
 *
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 * 
 * @author  RaYaKu
 */
--%>
<%-- A Generic Page to remove the Cache from Browser--%>
<%@ taglib uri="/WEB-INF/response.tld" prefix="res" %>
<%--
response.setHeader("Cache-Control","no-cache"); //HTTP 1.1 IE and 
response.setHeader("Cache-Control","no-store"); //HTTP 1.1 and for all versions of IE & NS
response.setHeader("Pragma","no-cache"); //HTTP 1.0
response.setDateHeader ("Expires", 0); //prevents caching at the proxy server
--%>
<%-- Set Expires HTTP Header to 0: --%>
<res:setIntHeader name="Expires"> 0 </res:setIntHeader>
<%-- Cache-Control:no-cache --%>
<res:addHeader name="Cache-Control">no-cache</res:addHeader> 
<%--   Pragma :no-cache --%>
<res:addHeader name="Pragma">no-cache</res:addHeader>
<%--   Cache-Control:no-store --%>
<res:addHeader name="Cache-Control">no-store</res:addHeader>