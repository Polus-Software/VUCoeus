<%@page contentType="text/html"%>
<%@page pageEncoding="UTF-8"%>
<%@page
	import="edu.mit.coeus.utils.CoeusFunctions, edu.mit.coeus.propdev.bean.*, java.util.*"%>
<p class="copybold">
	<%
    String parameterValue = new CoeusFunctions().getParameterValue("PUBLIC_MESSAGE_ID");
    HashMap data = new HashMap();
    if (parameterValue != null && parameterValue.trim().length() > 0) {
    data.put("MESSAGE_ID", parameterValue);
    ProposalActionTxnBean proposalActionTxnBean = new ProposalActionTxnBean();
    MessageBean messageBean = proposalActionTxnBean.getMessage(parameterValue);
    out.println(messageBean==null?"":messageBean.getMessage());
    }
    %>
</p>