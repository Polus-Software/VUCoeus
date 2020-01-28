

<!--contents of download.jsp-->
<%@ page import="java.util.*,java.io.*,edu.mit.coeuslite.coiv2.beans.Coiv2AttachmentBean"%>

<%
            Coiv2AttachmentBean attachmentBean = new Coiv2AttachmentBean();
            attachmentBean = (Coiv2AttachmentBean) request.getAttribute("downloadAttachmentBean");
            String downloadFileName = attachmentBean.getFileName();//(String)request.getAttribute("downloadFileName");
            String fileType = "";
            char array[] = attachmentBean.getFileName().toCharArray();

            for (int i = array.length - 1; i >= 0; i--) {
                if (array[i] != '.') {
                    fileType = fileType + array[i];
                } else if (array[i] == '.') {
                    break;
                }
            }
            int i, len = fileType.length();
            StringBuffer dest = new StringBuffer(len);

            for (i = (len - 1); i >= 0; i--) {
                dest.append(fileType.charAt(i));
            }

            response.setContentType("application/" + new String(dest));
            response.setHeader("Cache-Control", "cache");
            response.setHeader("Cache-Control", "must-revalidate");
            response.setHeader("Content-Disposition", "attachment; filename=" + downloadFileName);

            ServletOutputStream outs = response.getOutputStream();
            //out.clearBuffer();
            outs.write(attachmentBean.getFileBytes());
            outs.flush();
            outs.close();

%>
