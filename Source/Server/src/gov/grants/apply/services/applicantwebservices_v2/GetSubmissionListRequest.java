
package gov.grants.apply.services.applicantwebservices_v2;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import gov.grants.apply.system.applicantcommonelements_v1.SubmissionFilter;


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
 *         &lt;element ref="{http://apply.grants.gov/system/ApplicantCommonElements-V1.0}SubmissionFilter" maxOccurs="5" minOccurs="0"/>
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
    "submissionFilter"
})
@XmlRootElement(name = "GetSubmissionListRequest")
public class GetSubmissionListRequest {

    @XmlElement(name = "SubmissionFilter", namespace = "http://apply.grants.gov/system/ApplicantCommonElements-V1.0")
    protected List<SubmissionFilter> submissionFilter;

    /**
     * Gets the value of the submissionFilter property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the submissionFilter property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getSubmissionFilter().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link SubmissionFilter }
     * 
     * 
     */
    public List<SubmissionFilter> getSubmissionFilter() {
        if (submissionFilter == null) {
            submissionFilter = new ArrayList<SubmissionFilter>();
        }
        return this.submissionFilter;
    }

}
