//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v1.0.5-b16-fcs 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2009.03.17 at 08:39:35 PM GMT+05:30 
//


package edu.mit.coeus.utils.xml.bean.protocol;


/**
 * Java content class for ProtocolRolesType complex type.
 * <p>The following schema fragment specifies the expected content contained within this java content object. (defined at file:/E:/Share/3091/ProtocolSummary.xsd line 616)
 * <p>
 * <pre>
 * &lt;complexType name="ProtocolRolesType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="Role" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="RoleName" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="UserRoles" type="{http://www.w3.org/2001/ProtocolSummarySchema}ProtocolUserRolesType" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 */
public interface ProtocolRolesType {


    /**
     * Gets the value of the UserRoles property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the UserRoles property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getUserRoles().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link edu.mit.coeus.utils.xml.bean.protocol.ProtocolUserRolesType}
     * 
     */
    java.util.List getUserRoles();

    /**
     * Gets the value of the roleName property.
     * 
     * @return
     *     possible object is
     *     {@link java.lang.String}
     */
    java.lang.String getRoleName();

    /**
     * Sets the value of the roleName property.
     * 
     * @param value
     *     allowed object is
     *     {@link java.lang.String}
     */
    void setRoleName(java.lang.String value);

    /**
     * Gets the value of the role property.
     * 
     */
    int getRole();

    /**
     * Sets the value of the role property.
     * 
     */
    void setRole(int value);

}
