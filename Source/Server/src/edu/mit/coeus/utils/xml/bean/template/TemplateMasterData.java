//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v1.0.2-b15-fcs 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2005.05.13 at 09:45:36 EDT 
//


package edu.mit.coeus.utils.xml.bean.template;


/**
 * Java content class for templateMasterData complex type.
 * <p>The following schema fragment specifies the expected content contained within this java content object. (defined at file:/C:/TEMP/template.xsd line 19)
 * <p>
 * <pre>
 * &lt;complexType name="templateMasterData">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="templateCode" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/>
 *         &lt;element name="description" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="templateStatus" type="{}templateStatusType" minOccurs="0"/>
 *         &lt;element name="primeSponsor" type="{}sponsorType" minOccurs="0"/>
 *         &lt;element name="nonCompetingCont" type="{}nonCompetingContType" minOccurs="0"/>
 *         &lt;element name="competingRenewal" type="{}competingRenewalType" minOccurs="0"/>
 *         &lt;element name="basisPayment" type="{}basisPaymentType" minOccurs="0"/>
 *         &lt;element name="paymentMethod" type="{}paymentMethodType" minOccurs="0"/>
 *         &lt;element name="paymentFreq" type="{}paymentFreqType" minOccurs="0"/>
 *         &lt;element name="invoiceCopies" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/>
 *         &lt;element name="finalInvoiceDue" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/>
 *         &lt;element name="invoiceInstructions" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="CurrentDate" type="{http://www.w3.org/2001/XMLSchema}date" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 */
public interface TemplateMasterData {


    /**
     * 
     * @return
     *     possible object is
     *     {@link edu.mit.coeus.utils.xml.bean.template.PaymentMethodType}
     */
    edu.mit.coeus.utils.xml.bean.template.PaymentMethodType getPaymentMethod();

    /**
     * 
     * @param value
     *     allowed object is
     *     {@link edu.mit.coeus.utils.xml.bean.template.PaymentMethodType}
     */
    void setPaymentMethod(edu.mit.coeus.utils.xml.bean.template.PaymentMethodType value);

    /**
     * 
     * @return
     *     possible object is
     *     {@link edu.mit.coeus.utils.xml.bean.template.SponsorType}
     */
    edu.mit.coeus.utils.xml.bean.template.SponsorType getPrimeSponsor();

    /**
     * 
     * @param value
     *     allowed object is
     *     {@link edu.mit.coeus.utils.xml.bean.template.SponsorType}
     */
    void setPrimeSponsor(edu.mit.coeus.utils.xml.bean.template.SponsorType value);

    /**
     * 
     * @return
     *     possible object is
     *     {@link edu.mit.coeus.utils.xml.bean.template.CompetingRenewalType}
     */
    edu.mit.coeus.utils.xml.bean.template.CompetingRenewalType getCompetingRenewal();

    /**
     * 
     * @param value
     *     allowed object is
     *     {@link edu.mit.coeus.utils.xml.bean.template.CompetingRenewalType}
     */
    void setCompetingRenewal(edu.mit.coeus.utils.xml.bean.template.CompetingRenewalType value);

    /**
     * 
     * @return
     *     possible object is
     *     {@link edu.mit.coeus.utils.xml.bean.template.BasisPaymentType}
     */
    edu.mit.coeus.utils.xml.bean.template.BasisPaymentType getBasisPayment();

    /**
     * 
     * @param value
     *     allowed object is
     *     {@link edu.mit.coeus.utils.xml.bean.template.BasisPaymentType}
     */
    void setBasisPayment(edu.mit.coeus.utils.xml.bean.template.BasisPaymentType value);

    /**
     * 
     * @return
     *     possible object is
     *     {@link java.lang.String}
     */
    java.lang.String getInvoiceInstructions();

    /**
     * 
     * @param value
     *     allowed object is
     *     {@link java.lang.String}
     */
    void setInvoiceInstructions(java.lang.String value);

    /**
     * 
     */
    int getFinalInvoiceDue();

    /**
     * 
     */
    void setFinalInvoiceDue(int value);

    /**
     * 
     */
    int getInvoiceCopies();

    /**
     * 
     */
    void setInvoiceCopies(int value);

    /**
     * 
     */
    int getTemplateCode();

    /**
     * 
     */
    void setTemplateCode(int value);

    /**
     * 
     * @return
     *     possible object is
     *     {@link java.lang.String}
     */
    java.lang.String getDescription();

    /**
     * 
     * @param value
     *     allowed object is
     *     {@link java.lang.String}
     */
    void setDescription(java.lang.String value);

    /**
     * 
     * @return
     *     possible object is
     *     {@link edu.mit.coeus.utils.xml.bean.template.NonCompetingContType}
     */
    edu.mit.coeus.utils.xml.bean.template.NonCompetingContType getNonCompetingCont();

    /**
     * 
     * @param value
     *     allowed object is
     *     {@link edu.mit.coeus.utils.xml.bean.template.NonCompetingContType}
     */
    void setNonCompetingCont(edu.mit.coeus.utils.xml.bean.template.NonCompetingContType value);

    /**
     * 
     * @return
     *     possible object is
     *     {@link java.util.Calendar}
     */
    java.util.Calendar getCurrentDate();

    /**
     * 
     * @param value
     *     allowed object is
     *     {@link java.util.Calendar}
     */
    void setCurrentDate(java.util.Calendar value);

    /**
     * 
     * @return
     *     possible object is
     *     {@link edu.mit.coeus.utils.xml.bean.template.PaymentFreqType}
     */
    edu.mit.coeus.utils.xml.bean.template.PaymentFreqType getPaymentFreq();

    /**
     * 
     * @param value
     *     allowed object is
     *     {@link edu.mit.coeus.utils.xml.bean.template.PaymentFreqType}
     */
    void setPaymentFreq(edu.mit.coeus.utils.xml.bean.template.PaymentFreqType value);

    /**
     * 
     * @return
     *     possible object is
     *     {@link edu.mit.coeus.utils.xml.bean.template.TemplateStatusType}
     */
    edu.mit.coeus.utils.xml.bean.template.TemplateStatusType getTemplateStatus();

    /**
     * 
     * @param value
     *     allowed object is
     *     {@link edu.mit.coeus.utils.xml.bean.template.TemplateStatusType}
     */
    void setTemplateStatus(edu.mit.coeus.utils.xml.bean.template.TemplateStatusType value);

}
