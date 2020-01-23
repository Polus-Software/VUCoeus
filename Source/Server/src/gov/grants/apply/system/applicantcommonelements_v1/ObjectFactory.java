
package gov.grants.apply.system.applicantcommonelements_v1;

import javax.xml.bind.annotation.XmlRegistry;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the gov.grants.apply.system.applicantcommonelements_v1 package. 
 * <p>An ObjectFactory allows you to programatically 
 * construct new instances of the Java representation 
 * for XML content. The Java representation of XML 
 * content can consist of schema derived interfaces 
 * and classes representing the binding of schema 
 * type definitions, element declarations and model 
 * groups.  Factory methods for each of these are 
 * provided in this class.
 * 
 */
@XmlRegistry
public class ObjectFactory {


    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: gov.grants.apply.system.applicantcommonelements_v1
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link SubmissionDetails }
     * 
     */
    public SubmissionDetails createSubmissionDetails() {
        return new SubmissionDetails();
    }

    /**
     * Create an instance of {@link OpportunityFilter }
     * 
     */
    public OpportunityFilter createOpportunityFilter() {
        return new OpportunityFilter();
    }

    /**
     * Create an instance of {@link OpportunityDetails }
     * 
     */
    public OpportunityDetails createOpportunityDetails() {
        return new OpportunityDetails();
    }

    /**
     * Create an instance of {@link CFDADetails }
     * 
     */
    public CFDADetails createCFDADetails() {
        return new CFDADetails();
    }

    /**
     * Create an instance of {@link SubmissionFilter }
     * 
     */
    public SubmissionFilter createSubmissionFilter() {
        return new SubmissionFilter();
    }

}
