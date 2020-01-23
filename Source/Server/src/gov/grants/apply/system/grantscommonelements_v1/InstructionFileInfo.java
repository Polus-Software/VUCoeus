
package gov.grants.apply.system.grantscommonelements_v1;

import javax.activation.DataHandler;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlMimeType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for anonymous complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType>
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="FileName" type="{http://apply.grants.gov/system/GrantsCommonTypes-V1.0}FileNameType"/>
 *         &lt;element name="FileExtension" type="{http://apply.grants.gov/system/GrantsCommonTypes-V1.0}FileExtensionType"/>
 *         &lt;element name="FileContentId" type="{http://apply.grants.gov/system/GrantsCommonTypes-V1.0}FileContentIdType"/>
 *         &lt;element name="FileDataHandler" type="{http://www.w3.org/2001/XMLSchema}base64Binary"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "fileName",
    "fileExtension",
    "fileContentId",
    "fileDataHandler"
})
@XmlRootElement(name = "InstructionFileInfo")
public class InstructionFileInfo {

    @XmlElement(name = "FileName", required = true)
    protected String fileName;
    @XmlElement(name = "FileExtension", required = true)
    protected String fileExtension;
    @XmlElement(name = "FileContentId", required = true)
    protected String fileContentId;
    @XmlElement(name = "FileDataHandler", required = true)
    @XmlMimeType("application/octet-stream")
    protected DataHandler fileDataHandler;

    /**
     * Gets the value of the fileName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getFileName() {
        return fileName;
    }

    /**
     * Sets the value of the fileName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setFileName(String value) {
        this.fileName = value;
    }

    /**
     * Gets the value of the fileExtension property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getFileExtension() {
        return fileExtension;
    }

    /**
     * Sets the value of the fileExtension property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setFileExtension(String value) {
        this.fileExtension = value;
    }

    /**
     * Gets the value of the fileContentId property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getFileContentId() {
        return fileContentId;
    }

    /**
     * Sets the value of the fileContentId property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setFileContentId(String value) {
        this.fileContentId = value;
    }

    /**
     * Gets the value of the fileDataHandler property.
     * 
     * @return
     *     possible object is
     *     {@link DataHandler }
     *     
     */
    public DataHandler getFileDataHandler() {
        return fileDataHandler;
    }

    /**
     * Sets the value of the fileDataHandler property.
     * 
     * @param value
     *     allowed object is
     *     {@link DataHandler }
     *     
     */
    public void setFileDataHandler(DataHandler value) {
        this.fileDataHandler = value;
    }

}
