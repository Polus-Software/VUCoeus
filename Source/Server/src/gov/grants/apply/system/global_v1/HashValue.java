//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v1.0.4-b16-fcs 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2018.05.31 at 06:38:18 IST 
//


package gov.grants.apply.system.global_v1;


/**
 * Hash value used in Header and Footer schemas
 * Java content class for HashValue element declaration.
 * <p>The following schema fragment specifies the expected content contained within this java content object. (defined at https://trainingapply.grants.gov/apply/system/schemas/Global-V1.0.xsd line 17)
 * <p>
 * <pre>
 * &lt;element name="HashValue">
 *   &lt;complexType>
 *     &lt;simpleContent>
 *       &lt;extension base="&lt;http://www.w3.org/2001/XMLSchema>base64Binary">
 *         &lt;attribute ref="{http://apply.grants.gov/system/Global-V1.0}hashAlgorithm use="required""/>
 *       &lt;/extension>
 *     &lt;/simpleContent>
 *   &lt;/complexType>
 * &lt;/element>
 * </pre>
 * 
 */
public interface HashValue
    extends javax.xml.bind.Element, gov.grants.apply.system.global_v1.HashValueType
{


}