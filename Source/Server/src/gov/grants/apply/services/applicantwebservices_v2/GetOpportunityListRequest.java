
package gov.grants.apply.services.applicantwebservices_v2;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import gov.grants.apply.system.applicantcommonelements_v1.OpportunityFilter;


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
 *         &lt;choice>
 *           &lt;element ref="{http://apply.grants.gov/system/GrantsCommonElements-V1.0}PackageID" minOccurs="0"/>
 *           &lt;element ref="{http://apply.grants.gov/system/ApplicantCommonElements-V1.0}OpportunityFilter" minOccurs="0"/>
 *         &lt;/choice>
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
    "packageID",
    "opportunityFilter"
})
@XmlRootElement(name = "GetOpportunityListRequest")
public class GetOpportunityListRequest {

    @XmlElement(name = "PackageID", namespace = "http://apply.grants.gov/system/GrantsCommonElements-V1.0")
    protected String packageID;
    @XmlElement(name = "OpportunityFilter", namespace = "http://apply.grants.gov/system/ApplicantCommonElements-V1.0")
    protected OpportunityFilter opportunityFilter;

    /**
     * Gets the value of the packageID property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPackageID() {
        return packageID;
    }

    /**
     * Sets the value of the packageID property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPackageID(String value) {
        this.packageID = value;
    }

    /**
     * Gets the value of the opportunityFilter property.
     * 
     * @return
     *     possible object is
     *     {@link OpportunityFilter }
     *     
     */
    public OpportunityFilter getOpportunityFilter() {
        return opportunityFilter;
    }

    /**
     * Sets the value of the opportunityFilter property.
     * 
     * @param value
     *     allowed object is
     *     {@link OpportunityFilter }
     *     
     */
    public void setOpportunityFilter(OpportunityFilter value) {
        this.opportunityFilter = value;
    }

}
