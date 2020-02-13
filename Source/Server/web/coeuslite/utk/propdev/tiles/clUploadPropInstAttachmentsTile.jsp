<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles" %>

<%@include file="/coeuslite/utk/propdev/definitions/clUploadInformation.jsp" %>


<tiles:insert  beanName="clUploadAttachments" beanScope="request">
	<tiles:put name="body" value="/coeuslite/utk/propdev/clUploadPropInstAttachments.jsp" />
</tiles:insert>