/*
 * S2SNSPrefixMapper.java
 *
 * Created on December 19, 2006, 2:20 PM
 */

package edu.mit.coeus.s2s.util.custjxb;

/**
 *
 * @author  geot
 */
public class S2SNSPrefixMapper extends com.sun.xml.bind.marshaller.NamespacePrefixMapper {
    
    /** Creates a new instance of S2SNSPrefixMapper */
    public S2SNSPrefixMapper() {
    }
    
    public String getPreferredPrefix(String namespaceUri, String suggestion, boolean requiredPrefix) {
        if(namespaceUri.equalsIgnoreCase("http://apply.grants.gov/forms/RR_SubawardBudget-V1.0")){
            return "RR_SubawardBudget";
        }
        if(namespaceUri.equalsIgnoreCase("http://apply.grants.gov/forms/RR_SubawardBudget-V1-1")){
            return "RR_SubawardBudget";
        }
        if(namespaceUri.equalsIgnoreCase("http://apply.grants.gov/forms/RR_SubawardBudget-V1-2")){
            return "RR_SubawardBudget";
        }
        if(namespaceUri.equalsIgnoreCase("http://apply.grants.gov/forms/RR_FedNonFed_SubawardBudget-V1-2")){
            return "RR_FedNonFed_SubawardBudget";
        }
        return suggestion;
    }
    
}
