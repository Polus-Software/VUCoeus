
package gov.grants.apply.services.applicantwebservices_v2;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import gov.grants.apply.system.grantscommonelements_v1.ApplicationFilter;


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
 *         &lt;element ref="{http://apply.grants.gov/system/GrantsCommonElements-V1.0}ApplicationFilter" maxOccurs="unbounded" minOccurs="0"/>
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
    "applicationFilter"
})
@XmlRootElement(name = "GetApplicationListRequest")
public class GetApplicationListRequest {

    @XmlElement(name = "ApplicationFilter", namespace = "http://apply.grants.gov/system/GrantsCommonElements-V1.0")
    protected List<ApplicationFilter> applicationFilter;

    /**
     * Gets the value of the applicationFilter property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the applicationFilter property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getApplicationFilter().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link ApplicationFilter }
     * 
     * 
     */
    public List<ApplicationFilter> getApplicationFilter() {
        if (applicationFilter == null) {
            applicationFilter = new ArrayList<ApplicationFilter>();
        }
        return this.applicationFilter;
    }

}
