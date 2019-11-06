
package gov.grants.apply.system.grantscommonelements_v1;

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
 *         &lt;element name="CompetitionID" type="{http://apply.grants.gov/system/GrantsCommonTypes-V1.0}CompetitionIDType"/>
 *         &lt;element name="CompetitionTitle" type="{http://apply.grants.gov/system/GrantsCommonTypes-V1.0}CompetitionTitleType"/>
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
    "competitionID",
    "competitionTitle"
})
@XmlRootElement(name = "CompetitionInfo")
public class CompetitionInfo {

    @XmlElement(name = "CompetitionID", required = true)
    protected String competitionID;
    @XmlElement(name = "CompetitionTitle", required = true)
    protected String competitionTitle;

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

}
