
package gov.grants.apply.system.applicantcommonelements_v1;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.datatype.XMLGregorianCalendar;


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
 *         &lt;element ref="{http://apply.grants.gov/system/GrantsCommonElements-V1.0}FundingOpportunityTitle" minOccurs="0"/>
 *         &lt;element ref="{http://apply.grants.gov/system/GrantsCommonElements-V1.0}CompetitionID" minOccurs="0"/>
 *         &lt;element ref="{http://apply.grants.gov/system/GrantsCommonElements-V1.0}CompetitionTitle" minOccurs="0"/>
 *         &lt;element ref="{http://apply.grants.gov/system/GrantsCommonElements-V1.0}PackageID" minOccurs="0"/>
 *         &lt;element ref="{http://apply.grants.gov/system/ApplicantCommonElements-V1.0}CFDADetails" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="OpeningDate" type="{http://www.w3.org/2001/XMLSchema}date" minOccurs="0"/>
 *         &lt;element name="ClosingDate" type="{http://www.w3.org/2001/XMLSchema}date" minOccurs="0"/>
 *         &lt;element ref="{http://apply.grants.gov/system/GrantsCommonElements-V1.0}OfferingAgency" minOccurs="0"/>
 *         &lt;element ref="{http://apply.grants.gov/system/GrantsCommonElements-V1.0}AgencyContactInfo" minOccurs="0"/>
 *         &lt;element ref="{http://apply.grants.gov/system/GrantsCommonElements-V1.0}SchemaURL" minOccurs="0"/>
 *         &lt;element ref="{http://apply.grants.gov/system/GrantsCommonElements-V1.0}InstructionsURL" minOccurs="0"/>
 *         &lt;element name="IsMultiProject" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/>
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
    "fundingOpportunityTitle",
    "competitionID",
    "competitionTitle",
    "packageID",
    "cfdaDetails",
    "openingDate",
    "closingDate",
    "offeringAgency",
    "agencyContactInfo",
    "schemaURL",
    "instructionsURL",
    "isMultiProject"
})
@XmlRootElement(name = "OpportunityDetails")
public class OpportunityDetails {

    @XmlElement(name = "FundingOpportunityNumber", namespace = "http://apply.grants.gov/system/GrantsCommonElements-V1.0")
    protected String fundingOpportunityNumber;
    @XmlElement(name = "FundingOpportunityTitle", namespace = "http://apply.grants.gov/system/GrantsCommonElements-V1.0")
    protected String fundingOpportunityTitle;
    @XmlElement(name = "CompetitionID", namespace = "http://apply.grants.gov/system/GrantsCommonElements-V1.0")
    protected String competitionID;
    @XmlElement(name = "CompetitionTitle", namespace = "http://apply.grants.gov/system/GrantsCommonElements-V1.0")
    protected String competitionTitle;
    @XmlElement(name = "PackageID", namespace = "http://apply.grants.gov/system/GrantsCommonElements-V1.0")
    protected String packageID;
    @XmlElement(name = "CFDADetails")
    protected List<CFDADetails> cfdaDetails;
    @XmlElement(name = "OpeningDate")
    @XmlSchemaType(name = "date")
    protected XMLGregorianCalendar openingDate;
    @XmlElement(name = "ClosingDate")
    @XmlSchemaType(name = "date")
    protected XMLGregorianCalendar closingDate;
    @XmlElement(name = "OfferingAgency", namespace = "http://apply.grants.gov/system/GrantsCommonElements-V1.0")
    protected String offeringAgency;
    @XmlElement(name = "AgencyContactInfo", namespace = "http://apply.grants.gov/system/GrantsCommonElements-V1.0")
    protected String agencyContactInfo;
    @XmlElement(name = "SchemaURL", namespace = "http://apply.grants.gov/system/GrantsCommonElements-V1.0")
    protected String schemaURL;
    @XmlElement(name = "InstructionsURL", namespace = "http://apply.grants.gov/system/GrantsCommonElements-V1.0")
    protected String instructionsURL;
    @XmlElement(name = "IsMultiProject")
    protected Boolean isMultiProject;

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
     * Gets the value of the fundingOpportunityTitle property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getFundingOpportunityTitle() {
        return fundingOpportunityTitle;
    }

    /**
     * Sets the value of the fundingOpportunityTitle property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setFundingOpportunityTitle(String value) {
        this.fundingOpportunityTitle = value;
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

    /**
     * Gets the value of the competitionTitle property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCompetitionTitle() {
        return competitionTitle;
    }

    /**
     * Sets the value of the competitionTitle property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCompetitionTitle(String value) {
        this.competitionTitle = value;
    }

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
     * Gets the value of the cfdaDetails property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the cfdaDetails property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getCFDADetails().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link CFDADetails }
     * 
     * 
     */
    public List<CFDADetails> getCFDADetails() {
        if (cfdaDetails == null) {
            cfdaDetails = new ArrayList<CFDADetails>();
        }
        return this.cfdaDetails;
    }

    /**
     * Gets the value of the openingDate property.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getOpeningDate() {
        return openingDate;
    }

    /**
     * Sets the value of the openingDate property.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setOpeningDate(XMLGregorianCalendar value) {
        this.openingDate = value;
    }

    /**
     * Gets the value of the closingDate property.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getClosingDate() {
        return closingDate;
    }

    /**
     * Sets the value of the closingDate property.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setClosingDate(XMLGregorianCalendar value) {
        this.closingDate = value;
    }

    /**
     * Gets the value of the offeringAgency property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getOfferingAgency() {
        return offeringAgency;
    }

    /**
     * Sets the value of the offeringAgency property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setOfferingAgency(String value) {
        this.offeringAgency = value;
    }

    /**
     * Gets the value of the agencyContactInfo property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAgencyContactInfo() {
        return agencyContactInfo;
    }

    /**
     * Sets the value of the agencyContactInfo property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAgencyContactInfo(String value) {
        this.agencyContactInfo = value;
    }

    /**
     * Gets the value of the schemaURL property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSchemaURL() {
        return schemaURL;
    }

    /**
     * Sets the value of the schemaURL property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSchemaURL(String value) {
        this.schemaURL = value;
    }

    /**
     * Gets the value of the instructionsURL property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getInstructionsURL() {
        return instructionsURL;
    }

    /**
     * Sets the value of the instructionsURL property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setInstructionsURL(String value) {
        this.instructionsURL = value;
    }

    /**
     * Gets the value of the isMultiProject property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isIsMultiProject() {
        return isMultiProject;
    }

    /**
     * Sets the value of the isMultiProject property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setIsMultiProject(Boolean value) {
        this.isMultiProject = value;
    }

}
