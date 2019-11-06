
package gov.grants.apply.services.applicantwebservices_v2;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
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
 *         &lt;element ref="{http://apply.grants.gov/system/GrantsCommonElements-V1.0}FundingOpportunityNumber" minOccurs="0"/>
 *         &lt;element ref="{http://apply.grants.gov/system/GrantsCommonElements-V1.0}CFDANumber" minOccurs="0"/>
 *         &lt;element ref="{http://apply.grants.gov/system/GrantsCommonElements-V1.0}CompetitionID" minOccurs="0"/>
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
    "fundingOpportunityNumber",
    "cfdaNumber",
    "competitionID"
})
@XmlRootElement(name = "GetOpportunitiesRequest")
public class GetOpportunitiesRequest {

    @XmlElement(name = "FundingOpportunityNumber", namespace = "http://apply.grants.gov/system/GrantsCommonElements-V1.0")
    protected String fundingOpportunityNumber;
    @XmlElement(name = "CFDANumber", namespace = "http://apply.grants.gov/system/GrantsCommonElements-V1.0")
    protected String cfdaNumber;
    @XmlElement(name = "CompetitionID", namespace = "http://apply.grants.gov/system/GrantsCommonElements-V1.0")
    protected String competitionID;

    /**
     * Gets the value of the fundingOpportunityNumber property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getFundingOpportunityNumber() {
        return fundingOpportunityNumber;
    }

    /**
     * Sets the value of the fundingOpportunityNumber property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setFundingOpportunityNumber(String value) {
        this.fundingOpportunityNumber = value;
    }

    /**
     * Gets the value of the cfdaNumber property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCFDANumber() {
        return cfdaNumber;
    }

    /**
     * Sets the value of the cfdaNumber property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCFDANumber(String value) {
        this.cfdaNumber = value;
    }

    /**
     * Gets the value of the competitionID property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCompetitionID() {
        return competitionID;
    }

    /**
     * Sets the value of the competitionID property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCompetitionID(String value) {
        this.competitionID = value;
    }

}
