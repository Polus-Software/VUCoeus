/*
 * Created on Jun 21, 2004
 *
 */
package gov.grants.apply.struts.util;

import gov.grants.apply.util.Attachment;

import java.io.File;
import java.util.Date;

import org.apache.soap.util.mime.ByteArrayDataSource;
import org.apache.struts.upload.FormFile;

/**
 * This is an utility class.
 * 
 * @author Brian Husted
 *  
 */
public class Utils {
	public static final String DISPLAY_DATE_FORMAT = "MM/dd/yy h:mm a";

	public static final java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat(
			DISPLAY_DATE_FORMAT);

	/**
	 * Stores attachment.
	 * 
	 * @param file
	 *            the FormFile.
	 * @return the File.
	 * @throws Exception
	 */
	public static Attachment createAttachment(FormFile file) throws Exception {
		Attachment attach = null;
		File f = new File(file.getFileName());

		attach = new Attachment(f.getName());
		attach.setContentType(new javax.activation.MimetypesFileTypeMap()
				.getContentType(f));
		attach.setContentId("cid:" + f.getName() + "_"
				+ System.currentTimeMillis() + "@localhost.org");
		attach.setByteArrayDS(new ByteArrayDataSource(file.getFileData(),
				attach.getContentType()));
		attach.setFile(f);

		return attach;

	}

	/**
	 * @param d
	 *            the Date.
	 * @return the string of the formated date.
	 */
	public static String formatDate(Date d) {
		try {
			return sdf.format(d);
		} catch (Exception ex) {
			return "";
		}

	}

	public static String formatDate(long date) {
		return formatDate(new Date(date));
	}

}