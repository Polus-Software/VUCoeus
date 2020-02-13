
package gov.grants.apply.system.grantscommonelements_v1;

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
 *         &lt;element name="BeginValue" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0"/>
 *         &lt;element name="EndValue" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0"/>
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
    "beginValue",
    "endValue"
})
@XmlRootElement(name = "LastUpdatedTimestampRangeFilter")
public class LastUpdatedTimestampRangeFilter {

    @XmlElement(name = "BeginValue")
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar beginValue;
    @XmlElement(name = "EndValue")
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar endValue;

    /**
     * Gets the value of the beginValue property.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getBeginValue() {
        return beginValue;
    }

    /**
     * Sets the value of the beginValue property.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setBeginValue(XMLGregorianCalendar value) {
        this.beginValue = value;
    }

    /**
     * Gets the value of the endValue property.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getEndValue() {
        return endValue;
    }

    /**
     * Sets the value of the endValue property.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setEndValue(XMLGregorianCalendar value) {
        this.endValue = value;
    }

}
