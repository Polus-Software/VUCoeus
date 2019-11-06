
package gov.grants.apply.services.applicantwebservices_v2;

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
 *         &lt;element name="OpportunityInfo" maxOccurs="unbounded" minOccurs="0">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;sequence>
 *                   &lt;element ref="{http://apply.grants.gov/system/GrantsCommonElements-V1.0}FundingOpportunityNumber"/>
 *                   &lt;element ref="{http://apply.grants.gov/system/GrantsCommonElements-V1.0}CFDANumber" minOccurs="0"/>
 *                   &lt;element ref="{http://apply.grants.gov/system/GrantsCommonElements-V1.0}CompetitionID" minOccurs="0"/>
 *                   &lt;element name="OpeningDate" type="{http://www.w3.org/2001/XMLSchema}date" minOccurs="0"/>
 *                   &lt;element name="ClosingDate" type="{http://www.w3.org/2001/XMLSchema}date"/>
 *                   &lt;element ref="{http://apply.grants.gov/system/GrantsCommonElements-V1.0}FundingOpportunityTitle" minOccurs="0"/>
 *                   &lt;element ref="{http://apply.grants.gov/system/GrantsCommonElements-V1.0}OfferingAgency" minOccurs="0"/>
 *                   &lt;element ref="{http://apply.grants.gov/system/GrantsCommonElements-V1.0}AgencyContactInfo" minOccurs="0"/>
 *                   &lt;element ref="{http://apply.grants.gov/system/GrantsCommonElements-V1.0}CFDADescription" minOccurs="0"/>
 *                   &lt;element ref="{http://apply.grants.gov/system/GrantsCommonElements-V1.0}SchemaURL" minOccurs="0"/>
 *                   &lt;element ref="{http://apply.grants.gov/system/GrantsCommonElements-V1.0}InstructionsURL" minOccurs="0"/>
 *                   &lt;element name="IsMultiProject" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/>
 *                 &lt;/sequence>
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
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
    "opportunityInfo"
})
@XmlRootElement(name = "GetOpportunitiesResponse")
public class GetOpportunitiesResponse {

    @XmlElement(name = "OpportunityInfo")
    protected List<GetOpportunitiesResponse.OpportunityInfo> opportunityInfo;

    /**
     * Gets the value of the opportunityInfo property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the opportunityInfo property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getOpportunityInfo().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link GetOpportunitiesResponse.OpportunityInfo }
     * 
     * 
     */
    public List<GetOpportunitiesResponse.OpportunityInfo> getOpportunityInfo() {
        if (opportunityInfo == null) {
            opportunityInfo = new ArrayList<GetOpportunitiesResponse.OpportunityInfo>();
        }
        return this.opportunityInfo;
    }


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
     *         &lt;element ref="{http://apply.grants.gov/system/GrantsCommonElements-V1.0}FundingOpportunityNumber"/>
     *         &lt;element ref="{http://apply.grants.gov/system/GrantsCommonElements-V1.0}CFDANumber" minOccurs="0"/>
     *         &lt;element ref="{http://apply.grants.gov/system/GrantsCommonElements-V1.0}CompetitionID" minOccurs="0"/>
     *         &lt;element name="OpeningDate" type="{http://www.w3.org/2001/XMLSchema}date" minOccurs="0"/>
     *         &lt;element name="ClosingDate" type="{http://www.w3.org/2001/XMLSchema}date"/>
     *         &lt;element ref="{http://apply.grants.gov/system/GrantsCommonElements-V1.0}FundingOpportunityTitle" minOccurs="0"/>
     *         &lt;element ref="{http://apply.grants.gov/system/GrantsCommonElements-V1.0}OfferingAgency" minOccurs="0"/>
     *         &lt;element ref="{http://apply.grants.gov/system/GrantsCommonElements-V1.0}AgencyContactInfo" minOccurs="0"/>
     *         &lt;element ref="{http://apply.grants.gov/system/GrantsCommonElements-V1.0}CFDADescription" minOccurs="0"/>
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
        "cfdaNumber",
        "competitionID",
        "openingDate",
        "closingDate",
        "fundingOpportunityTitle",
        "offeringAgency",
        "agencyContactInfo",
        "cfdaDescription",
        "schemaURL",
        "instructionsURL",
        "isMultiProject"
    })
    public static class OpportunityInfo {

        @XmlElement(name = "FundingOpportunityNumber", namespace = "http://apply.grants.gov/system/GrantsCommonElements-V1.0", required = true)
        protected String fundingOpportunityNumber;
        @XmlElement(name = "CFDANumber", namespace = "http://apply.grants.gov/system/GrantsCommonElements-V1.0")
        protected String cfdaNumber;
        @XmlElement(name = "CompetitionID", namespace = "http://apply.grants.gov/system/GrantsCommonElements-V1.0")
        protected String competitionID;
        @XmlElement(name = "OpeningDate")
        @XmlSchemaType(name = "date")
        protected XMLGregorianCalendar openingDate;
        @XmlElement(name = "ClosingDate", required = true)
        @XmlSchemaType(name = "date")
        protected XMLGregorianCalendar closingDate;
        @XmlElement(name = "FundingOpportunityTitle", namespace = "http://apply.grants.gov/system/GrantsCommonElements-V1.0")
        protected String fundingOpportunityTitle;
        @XmlElement(name = "OfferingAgency", namespace = "http://apply.grants.gov/system/GrantsCommonElements-V1.0")
        protected String offeringAgency;
        @XmlElement(name = "AgencyContactInfo", namespace = "http://apply.grants.gov/system/GrantsCommonElements-V1.0")
        protected String agencyContactInfo;
        @XmlElement(name = "CFDADescription", namespace = "http://apply.grants.gov/system/GrantsCommonElements-V1.0")
        protected String cfdaDescription;
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
         * Gets the value of the cfdaDescription property.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getCFDADescription() {
            return cfdaDescription;
        }

        /**
         * Sets the value of the cfdaDescription property.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setCFDADescription(String value) {
            this.cfdaDescription = value;
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

}
