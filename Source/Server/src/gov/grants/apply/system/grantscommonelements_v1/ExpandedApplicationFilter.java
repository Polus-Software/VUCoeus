
package gov.grants.apply.system.grantscommonelements_v1;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import gov.grants.apply.system.grantscommontypes_v1.ExpandedApplicationFilterType;


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
 *         &lt;element name="FilterType" type="{http://apply.grants.gov/system/GrantsCommonTypes-V1.0}ExpandedApplicationFilterType"/>
 *         &lt;element name="FilterValue" type="{http://www.w3.org/2001/XMLSchema}string"/>
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
    "filterType",
    "filterValue"
})
@XmlRootElement(name = "ExpandedApplicationFilter")
public class ExpandedApplicationFilter {

    @XmlElement(name = "FilterType", required = true)
    @XmlSchemaType(name = "string")
    protected ExpandedApplicationFilterType filterType;
    @XmlElement(name = "FilterValue", required = true)
    protected String filterValue;

    /**
     * Gets the value of the filterType property.
     * 
     * @return
     *     possible object is
     *     {@link ExpandedApplicationFilterType }
     *     
     */
    public ExpandedApplicationFilterType getFilterType() {
        return filterType;
    }

    /**
     * Sets the value of the filterType property.
     * 
     * @param value
     *     allowed object is
     *     {@link ExpandedApplicationFilterType }
     *     
     */
    public void setFilterType(ExpandedApplicationFilterType value) {
        this.filterType = value;
    }

    /**
     * Gets the value of the filterValue property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getFilterValue() {
        return filterValue;
    }

    /**
     * Sets the value of the filterValue property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setFilterValue(String value) {
        this.filterValue = value;
    }

}
