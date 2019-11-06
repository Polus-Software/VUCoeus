
package gov.grants.apply.services.applicantwebservices_v2;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import gov.grants.apply.system.applicantcommonelements_v1.OpportunityDetails;


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
 *         &lt;element ref="{http://apply.grants.gov/system/ApplicantCommonElements-V1.0}OpportunityDetails" maxOccurs="unbounded" minOccurs="0"/>
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
    "opportunityDetails"
})
@XmlRootElement(name = "GetOpportunityListResponse")
public class GetOpportunityListResponse {

    @XmlElement(name = "OpportunityDetails", namespace = "http://apply.grants.gov/system/ApplicantCommonElements-V1.0")
    protected List<OpportunityDetails> opportunityDetails;

    /**
     * Gets the value of the opportunityDetails property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the opportunityDetails property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getOpportunityDetails().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link OpportunityDetails }
     * 
     * 
     */
    public List<OpportunityDetails> getOpportunityDetails() {
        if (opportunityDetails == null) {
            opportunityDetails = new ArrayList<OpportunityDetails>();
        }
        return this.opportunityDetails;
    }

}
