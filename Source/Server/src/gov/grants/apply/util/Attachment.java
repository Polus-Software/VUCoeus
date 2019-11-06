package gov.grants.apply.util;

import java.io.File;

import org.apache.soap.util.mime.ByteArrayDataSource;



/**
 *
 * @author Brian Husted
 */
public class Attachment {
	
	private File file;
	/**
	 * @return Returns the file.
	 */
	public File getFile() {
		return file;
	}
	/**
	 * @param file The file to set.
	 */
	public void setFile(File file) {
		this.file = file;
	}
    private String fileName;
    private String ContentId;
    private String ContentType;
    private ByteArrayDataSource byteArrayDS;
    
    
	/**
	 * @return Returns the byteArrayDS.
	 */
	public ByteArrayDataSource getByteArrayDS() {
		return byteArrayDS;
	}
	/**
	 * @param byteArrayDS The byteArrayDS to set.
	 */
	public void setByteArrayDS(ByteArrayDataSource byteArrayDS) {
		this.byteArrayDS = byteArrayDS;
	}
 
	public Attachment( String fileName ){
		setFileName( fileName );
    }
	/**
	 * @return Returns the contentId.
	 */
	public String getContentId() {
		return ContentId;
	}
	/**
	 * @param contentId The contentId to set.
	 */
	public void setContentId(String contentId) {
		ContentId = contentId;
	}
	/**
	 * @return Returns the contentType.
	 */
	public String getContentType() {
		return ContentType;
	}
	/**
	 * @param contentType The contentType to set.
	 */
	public void setContentType(String contentType) {
		ContentType = contentType;
	}


	
	
	/**
	 * @return Returns the fileName.
	 */
	public String getFileName() {
		return fileName;
	}
	/**
	 * @param fileName The fileName to set.
	 */
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
}
