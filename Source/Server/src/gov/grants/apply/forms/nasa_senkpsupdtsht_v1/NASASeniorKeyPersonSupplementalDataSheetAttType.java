//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v1.0.3-b18-fcs 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2009.05.19 at 04:23:48 EDT 
//


package gov.grants.apply.forms.nasa_senkpsupdtsht_v1;


/**
 * Java content class for NASA_SeniorKeyPersonSupplementalDataSheetAttType complex type.
 * <p>The following schema fragment specifies the expected content contained within this java content object. (defined at file:/C:/s2s/build/cust/NASA_SeniorKeyPersonSupplementalDataSheet-V1.0.xsd line 106)
 * <p>
 * <pre>
 * &lt;complexType name="NASA_SeniorKeyPersonSupplementalDataSheetAttType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="Senior_Key_Person" type="{http://apply.grants.gov/forms/NASA_SenKPSupDtSht-V1.0}Senior_Key_PersonType" maxOccurs="8"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 */
public interface NASASeniorKeyPersonSupplementalDataSheetAttType {


    /**
     * Gets the value of the SeniorKeyPerson property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the SeniorKeyPerson property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getSeniorKeyPerson().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link gov.grants.apply.forms.nasa_senkpsupdtsht_v1.SeniorKeyPersonType}
     * 
     */
    java.util.List getSeniorKeyPerson();

}
