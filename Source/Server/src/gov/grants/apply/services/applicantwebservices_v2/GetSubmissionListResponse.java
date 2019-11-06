
package gov.grants.apply.services.applicantwebservices_v2;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import gov.grants.apply.system.applicantcommonelements_v1.SubmissionDetails;


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
 *         &lt;element name="AvailableApplicationNumber" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element ref="{http://apply.grants.gov/system/ApplicantCommonElements-V1.0}SubmissionDetails" maxOccurs="unbounded" minOccurs="0"/>
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
    "availableApplicationNumber",
    "submissionDetails"
})
@XmlRootElement(name = "GetSubmissionListResponse")
public class GetSubmissionListResponse {

    @XmlElement(name = "AvailableApplicationNumber")
    protected int availableApplicationNumber;
    @XmlElement(name = "SubmissionDetails", namespace = "http://apply.grants.gov/system/ApplicantCommonElements-V1.0")
    protected List<SubmissionDetails> submissionDetails;

    /**
     * Gets the value of the availableApplicationNumber property.
     * 
     */
    public int getAvailableApplicationNumber() {
        return availableApplicationNumber;
    }

    /**
     * Sets the value of the availableApplicationNumber property.
     * 
     */
    public void setAvailableApplicationNumber(int value) {
        this.availableApplicationNumber = value;
    }

    /**
     * Gets the value of the submissionDetails property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the submissionDetails property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getSubmissionDetails().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link SubmissionDetails }
     * 
     * 
     */
    public List<SubmissionDetails> getSubmissionDetails() {
        if (submissionDetails == null) {
            submissionDetails = new ArrayList<SubmissionDetails>();
        }
        return this.submissionDetails;
    }

}
