//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v1.0.4-b16-fcs 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2011.02.21 at 04:06:33 IST 
//


package edu.mit.coeuslite.award.print.awardsummary;


/**
 * Java content class for Investigatortype complex type.
 * <p>The following schema fragment specifies the expected content contained within this java content object. (defined at file:/D:/CHECK%20OUT/Coeus%204.5/Source/Server/src/edu/mit/coeuslite/award/schema/AwardSummary.xsd line 84)
 * <p>
 * <pre>
 * &lt;complexType name="Investigatortype">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="InvestigatorName" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="PIFlag" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 */
public interface Investigatortype {


    /**
     * Gets the value of the piFlag property.
     * 
     */
    boolean isPIFlag();

    /**
     * Sets the value of the piFlag property.
     * 
     */
    void setPIFlag(boolean value);

    /**
     * Gets the value of the investigatorName property.
     * 
     * @return
     *     possible object is
     *     {@link java.lang.String}
     */
    java.lang.String getInvestigatorName();

    /**
     * Sets the value of the investigatorName property.
     * 
     * @param value
     *     allowed object is
     *     {@link java.lang.String}
     */
    void setInvestigatorName(java.lang.String value);

}
