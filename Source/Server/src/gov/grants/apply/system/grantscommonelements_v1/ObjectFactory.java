
package gov.grants.apply.system.grantscommonelements_v1;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.datatype.XMLGregorianCalendar;
import javax.xml.namespace.QName;
import gov.grants.apply.system.grantscommontypes_v1.ActiveExclusionsType;
import gov.grants.apply.system.grantscommontypes_v1.DelinquentFederalDebtType;
import gov.grants.apply.system.grantscommontypes_v1.GrantsGovApplicationStatusType;
import gov.grants.apply.system.grantscommontypes_v1.OperationStatusType;
import gov.grants.apply.system.grantscommontypes_v1.OpportunityCategoryType;
import gov.grants.apply.system.grantscommontypes_v1.SubmissionMethodType;
import gov.grants.apply.system.grantscommontypes_v1.YesNoType;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the gov.grants.apply.system.grantscommonelements_v1 package. 
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

    private final static QName _FileSizeInKB_QNAME = new QName("http://apply.grants.gov/system/GrantsCommonElements-V1.0", "FileSizeInKB");
    private final static QName _SendChangeNotificationEmail_QNAME = new QName("http://apply.grants.gov/system/GrantsCommonElements-V1.0", "SendChangeNotificationEmail");
    private final static QName _DUNS_QNAME = new QName("http://apply.grants.gov/system/GrantsCommonElements-V1.0", "DUNS");
    private final static QName _StatusDetail_QNAME = new QName("http://apply.grants.gov/system/GrantsCommonElements-V1.0", "StatusDetail");
    private final static QName _SendUpdateNotificationEmail_QNAME = new QName("http://apply.grants.gov/system/GrantsCommonElements-V1.0", "SendUpdateNotificationEmail");
    private final static QName _LinkURL_QNAME = new QName("http://apply.grants.gov/system/GrantsCommonElements-V1.0", "LinkURL");
    private final static QName _FileMIMEType_QNAME = new QName("http://apply.grants.gov/system/GrantsCommonElements-V1.0", "FileMIMEType");
    private final static QName _SchemaURL_QNAME = new QName("http://apply.grants.gov/system/GrantsCommonElements-V1.0", "SchemaURL");
    private final static QName _GrantsGovApplicationStatus_QNAME = new QName("http://apply.grants.gov/system/GrantsCommonElements-V1.0", "GrantsGovApplicationStatus");
    private final static QName _CFDADescription_QNAME = new QName("http://apply.grants.gov/system/GrantsCommonElements-V1.0", "CFDADescription");
    private final static QName _AgencyContactInfo_QNAME = new QName("http://apply.grants.gov/system/GrantsCommonElements-V1.0", "AgencyContactInfo");
    private final static QName _SendDeleteNotificationEmail_QNAME = new QName("http://apply.grants.gov/system/GrantsCommonElements-V1.0", "SendDeleteNotificationEmail");
    private final static QName _ResponseMessage_QNAME = new QName("http://apply.grants.gov/system/GrantsCommonElements-V1.0", "ResponseMessage");
    private final static QName _Version_QNAME = new QName("http://apply.grants.gov/system/GrantsCommonElements-V1.0", "Version");
    private final static QName _LinkID_QNAME = new QName("http://apply.grants.gov/system/GrantsCommonElements-V1.0", "LinkID");
    private final static QName _NullifyMissingOptionalElements_QNAME = new QName("http://apply.grants.gov/system/GrantsCommonElements-V1.0", "NullifyMissingOptionalElements");
    private final static QName _AgencyCode_QNAME = new QName("http://apply.grants.gov/system/GrantsCommonElements-V1.0", "AgencyCode");
    private final static QName _NumberOfNotificationsSent_QNAME = new QName("http://apply.grants.gov/system/GrantsCommonElements-V1.0", "NumberOfNotificationsSent");
    private final static QName _GrantsGovTrackingNumber_QNAME = new QName("http://apply.grants.gov/system/GrantsCommonElements-V1.0", "GrantsGovTrackingNumber");
    private final static QName _PackageID_QNAME = new QName("http://apply.grants.gov/system/GrantsCommonElements-V1.0", "PackageID");
    private final static QName _LastUpdatedTimestamp_QNAME = new QName("http://apply.grants.gov/system/GrantsCommonElements-V1.0", "LastUpdatedTimestamp");
    private final static QName _DelinquentFederalDebt_QNAME = new QName("http://apply.grants.gov/system/GrantsCommonElements-V1.0", "DelinquentFederalDebt");
    private final static QName _FundingOpportunityNumber_QNAME = new QName("http://apply.grants.gov/system/GrantsCommonElements-V1.0", "FundingOpportunityNumber");
    private final static QName _OtherOpportunityCategoryExplanation_QNAME = new QName("http://apply.grants.gov/system/GrantsCommonElements-V1.0", "OtherOpportunityCategoryExplanation");
    private final static QName _OpportunityID_QNAME = new QName("http://apply.grants.gov/system/GrantsCommonElements-V1.0", "OpportunityID");
    private final static QName _PostingDate_QNAME = new QName("http://apply.grants.gov/system/GrantsCommonElements-V1.0", "PostingDate");
    private final static QName _AgencyTrackingNumber_QNAME = new QName("http://apply.grants.gov/system/GrantsCommonElements-V1.0", "AgencyTrackingNumber");
    private final static QName _OpportunityCategory_QNAME = new QName("http://apply.grants.gov/system/GrantsCommonElements-V1.0", "OpportunityCategory");
    private final static QName _AgencyNotes_QNAME = new QName("http://apply.grants.gov/system/GrantsCommonElements-V1.0", "AgencyNotes");
    private final static QName _CFDANumber_QNAME = new QName("http://apply.grants.gov/system/GrantsCommonElements-V1.0", "CFDANumber");
    private final static QName _ClosingDate_QNAME = new QName("http://apply.grants.gov/system/GrantsCommonElements-V1.0", "ClosingDate");
    private final static QName _AgencyName_QNAME = new QName("http://apply.grants.gov/system/GrantsCommonElements-V1.0", "AgencyName");
    private final static QName _ModificationComments_QNAME = new QName("http://apply.grants.gov/system/GrantsCommonElements-V1.0", "ModificationComments");
    private final static QName _ActiveExclusions_QNAME = new QName("http://apply.grants.gov/system/GrantsCommonElements-V1.0", "ActiveExclusions");
    private final static QName _InstructionsURL_QNAME = new QName("http://apply.grants.gov/system/GrantsCommonElements-V1.0", "InstructionsURL");
    private final static QName _UserID_QNAME = new QName("http://apply.grants.gov/system/GrantsCommonElements-V1.0", "UserID");
    private final static QName _CompletionStatus_QNAME = new QName("http://apply.grants.gov/system/GrantsCommonElements-V1.0", "CompletionStatus");
    private final static QName _FundingOpportunityTitle_QNAME = new QName("http://apply.grants.gov/system/GrantsCommonElements-V1.0", "FundingOpportunityTitle");
    private final static QName _OfferingAgency_QNAME = new QName("http://apply.grants.gov/system/GrantsCommonElements-V1.0", "OfferingAgency");
    private final static QName _InstructionFileLastUpdatedTimestamp_QNAME = new QName("http://apply.grants.gov/system/GrantsCommonElements-V1.0", "InstructionFileLastUpdatedTimestamp");
    private final static QName _DeleteComments_QNAME = new QName("http://apply.grants.gov/system/GrantsCommonElements-V1.0", "DeleteComments");
    private final static QName _CompetitionTitle_QNAME = new QName("http://apply.grants.gov/system/GrantsCommonElements-V1.0", "CompetitionTitle");
    private final static QName _FileID_QNAME = new QName("http://apply.grants.gov/system/GrantsCommonElements-V1.0", "FileID");
    private final static QName _OpportunityCategoryExplanation_QNAME = new QName("http://apply.grants.gov/system/GrantsCommonElements-V1.0", "OpportunityCategoryExplanation");
    private final static QName _LinkDescription_QNAME = new QName("http://apply.grants.gov/system/GrantsCommonElements-V1.0", "LinkDescription");
    private final static QName _SubmissionTitle_QNAME = new QName("http://apply.grants.gov/system/GrantsCommonElements-V1.0", "SubmissionTitle");
    private final static QName _FileName_QNAME = new QName("http://apply.grants.gov/system/GrantsCommonElements-V1.0", "FileName");
    private final static QName _ArchiveDate_QNAME = new QName("http://apply.grants.gov/system/GrantsCommonElements-V1.0", "ArchiveDate");
    private final static QName _UpdateComments_QNAME = new QName("http://apply.grants.gov/system/GrantsCommonElements-V1.0", "UpdateComments");
    private final static QName _CompetitionID_QNAME = new QName("http://apply.grants.gov/system/GrantsCommonElements-V1.0", "CompetitionID");
    private final static QName _FolderID_QNAME = new QName("http://apply.grants.gov/system/GrantsCommonElements-V1.0", "FolderID");
    private final static QName _SubmissionMethod_QNAME = new QName("http://apply.grants.gov/system/GrantsCommonElements-V1.0", "SubmissionMethod");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: gov.grants.apply.system.grantscommonelements_v1
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link ApplicationFilter }
     * 
     */
    public ApplicationFilter createApplicationFilter() {
        return new ApplicationFilter();
    }

    /**
     * Create an instance of {@link Attachment }
     * 
     */
    public Attachment createAttachment() {
        return new Attachment();
    }

    /**
     * Create an instance of {@link Token }
     * 
     */
    public Token createToken() {
        return new Token();
    }

    /**
     * Create an instance of {@link SecurityMessage }
     * 
     */
    public SecurityMessage createSecurityMessage() {
        return new SecurityMessage();
    }

    /**
     * Create an instance of {@link InstructionFileInfo }
     * 
     */
    public InstructionFileInfo createInstructionFileInfo() {
        return new InstructionFileInfo();
    }

    /**
     * Create an instance of {@link ExpandedApplicationFilter }
     * 
     */
    public ExpandedApplicationFilter createExpandedApplicationFilter() {
        return new ExpandedApplicationFilter();
    }

    /**
     * Create an instance of {@link ErrorDetails }
     * 
     */
    public ErrorDetails createErrorDetails() {
        return new ErrorDetails();
    }

    /**
     * Create an instance of {@link CompetitionInfo }
     * 
     */
    public CompetitionInfo createCompetitionInfo() {
        return new CompetitionInfo();
    }

    /**
     * Create an instance of {@link LastUpdatedTimestampRangeFilter }
     * 
     */
    public LastUpdatedTimestampRangeFilter createLastUpdatedTimestampRangeFilter() {
        return new LastUpdatedTimestampRangeFilter();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://apply.grants.gov/system/GrantsCommonElements-V1.0", name = "FileSizeInKB")
    public JAXBElement<String> createFileSizeInKB(String value) {
        return new JAXBElement<String>(_FileSizeInKB_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://apply.grants.gov/system/GrantsCommonElements-V1.0", name = "SendChangeNotificationEmail", defaultValue = "Y")
    public JAXBElement<String> createSendChangeNotificationEmail(String value) {
        return new JAXBElement<String>(_SendChangeNotificationEmail_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://apply.grants.gov/system/GrantsCommonElements-V1.0", name = "DUNS")
    public JAXBElement<String> createDUNS(String value) {
        return new JAXBElement<String>(_DUNS_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://apply.grants.gov/system/GrantsCommonElements-V1.0", name = "StatusDetail")
    public JAXBElement<String> createStatusDetail(String value) {
        return new JAXBElement<String>(_StatusDetail_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link YesNoType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://apply.grants.gov/system/GrantsCommonElements-V1.0", name = "SendUpdateNotificationEmail", defaultValue = "Y")
    public JAXBElement<YesNoType> createSendUpdateNotificationEmail(YesNoType value) {
        return new JAXBElement<YesNoType>(_SendUpdateNotificationEmail_QNAME, YesNoType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://apply.grants.gov/system/GrantsCommonElements-V1.0", name = "LinkURL")
    public JAXBElement<String> createLinkURL(String value) {
        return new JAXBElement<String>(_LinkURL_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://apply.grants.gov/system/GrantsCommonElements-V1.0", name = "FileMIMEType")
    public JAXBElement<String> createFileMIMEType(String value) {
        return new JAXBElement<String>(_FileMIMEType_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://apply.grants.gov/system/GrantsCommonElements-V1.0", name = "SchemaURL")
    public JAXBElement<String> createSchemaURL(String value) {
        return new JAXBElement<String>(_SchemaURL_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GrantsGovApplicationStatusType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://apply.grants.gov/system/GrantsCommonElements-V1.0", name = "GrantsGovApplicationStatus")
    public JAXBElement<GrantsGovApplicationStatusType> createGrantsGovApplicationStatus(GrantsGovApplicationStatusType value) {
        return new JAXBElement<GrantsGovApplicationStatusType>(_GrantsGovApplicationStatus_QNAME, GrantsGovApplicationStatusType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://apply.grants.gov/system/GrantsCommonElements-V1.0", name = "CFDADescription")
    public JAXBElement<String> createCFDADescription(String value) {
        return new JAXBElement<String>(_CFDADescription_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://apply.grants.gov/system/GrantsCommonElements-V1.0", name = "AgencyContactInfo")
    public JAXBElement<String> createAgencyContactInfo(String value) {
        return new JAXBElement<String>(_AgencyContactInfo_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link YesNoType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://apply.grants.gov/system/GrantsCommonElements-V1.0", name = "SendDeleteNotificationEmail", defaultValue = "Y")
    public JAXBElement<YesNoType> createSendDeleteNotificationEmail(YesNoType value) {
        return new JAXBElement<YesNoType>(_SendDeleteNotificationEmail_QNAME, YesNoType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://apply.grants.gov/system/GrantsCommonElements-V1.0", name = "ResponseMessage")
    public JAXBElement<String> createResponseMessage(String value) {
        return new JAXBElement<String>(_ResponseMessage_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://apply.grants.gov/system/GrantsCommonElements-V1.0", name = "Version")
    public JAXBElement<String> createVersion(String value) {
        return new JAXBElement<String>(_Version_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://apply.grants.gov/system/GrantsCommonElements-V1.0", name = "LinkID")
    public JAXBElement<String> createLinkID(String value) {
        return new JAXBElement<String>(_LinkID_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link YesNoType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://apply.grants.gov/system/GrantsCommonElements-V1.0", name = "NullifyMissingOptionalElements", defaultValue = "N")
    public JAXBElement<YesNoType> createNullifyMissingOptionalElements(YesNoType value) {
        return new JAXBElement<YesNoType>(_NullifyMissingOptionalElements_QNAME, YesNoType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://apply.grants.gov/system/GrantsCommonElements-V1.0", name = "AgencyCode")
    public JAXBElement<String> createAgencyCode(String value) {
        return new JAXBElement<String>(_AgencyCode_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://apply.grants.gov/system/GrantsCommonElements-V1.0", name = "NumberOfNotificationsSent")
    public JAXBElement<String> createNumberOfNotificationsSent(String value) {
        return new JAXBElement<String>(_NumberOfNotificationsSent_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://apply.grants.gov/system/GrantsCommonElements-V1.0", name = "GrantsGovTrackingNumber")
    public JAXBElement<String> createGrantsGovTrackingNumber(String value) {
        return new JAXBElement<String>(_GrantsGovTrackingNumber_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://apply.grants.gov/system/GrantsCommonElements-V1.0", name = "PackageID")
    public JAXBElement<String> createPackageID(String value) {
        return new JAXBElement<String>(_PackageID_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link XMLGregorianCalendar }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://apply.grants.gov/system/GrantsCommonElements-V1.0", name = "LastUpdatedTimestamp")
    public JAXBElement<XMLGregorianCalendar> createLastUpdatedTimestamp(XMLGregorianCalendar value) {
        return new JAXBElement<XMLGregorianCalendar>(_LastUpdatedTimestamp_QNAME, XMLGregorianCalendar.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link DelinquentFederalDebtType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://apply.grants.gov/system/GrantsCommonElements-V1.0", name = "DelinquentFederalDebt")
    public JAXBElement<DelinquentFederalDebtType> createDelinquentFederalDebt(DelinquentFederalDebtType value) {
        return new JAXBElement<DelinquentFederalDebtType>(_DelinquentFederalDebt_QNAME, DelinquentFederalDebtType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://apply.grants.gov/system/GrantsCommonElements-V1.0", name = "FundingOpportunityNumber")
    public JAXBElement<String> createFundingOpportunityNumber(String value) {
        return new JAXBElement<String>(_FundingOpportunityNumber_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://apply.grants.gov/system/GrantsCommonElements-V1.0", name = "OtherOpportunityCategoryExplanation")
    public JAXBElement<String> createOtherOpportunityCategoryExplanation(String value) {
        return new JAXBElement<String>(_OtherOpportunityCategoryExplanation_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://apply.grants.gov/system/GrantsCommonElements-V1.0", name = "OpportunityID")
    public JAXBElement<String> createOpportunityID(String value) {
        return new JAXBElement<String>(_OpportunityID_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://apply.grants.gov/system/GrantsCommonElements-V1.0", name = "PostingDate")
    public JAXBElement<String> createPostingDate(String value) {
        return new JAXBElement<String>(_PostingDate_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://apply.grants.gov/system/GrantsCommonElements-V1.0", name = "AgencyTrackingNumber")
    public JAXBElement<String> createAgencyTrackingNumber(String value) {
        return new JAXBElement<String>(_AgencyTrackingNumber_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link OpportunityCategoryType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://apply.grants.gov/system/GrantsCommonElements-V1.0", name = "OpportunityCategory")
    public JAXBElement<OpportunityCategoryType> createOpportunityCategory(OpportunityCategoryType value) {
        return new JAXBElement<OpportunityCategoryType>(_OpportunityCategory_QNAME, OpportunityCategoryType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://apply.grants.gov/system/GrantsCommonElements-V1.0", name = "AgencyNotes")
    public JAXBElement<String> createAgencyNotes(String value) {
        return new JAXBElement<String>(_AgencyNotes_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://apply.grants.gov/system/GrantsCommonElements-V1.0", name = "CFDANumber")
    public JAXBElement<String> createCFDANumber(String value) {
        return new JAXBElement<String>(_CFDANumber_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://apply.grants.gov/system/GrantsCommonElements-V1.0", name = "ClosingDate")
    public JAXBElement<String> createClosingDate(String value) {
        return new JAXBElement<String>(_ClosingDate_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://apply.grants.gov/system/GrantsCommonElements-V1.0", name = "AgencyName")
    public JAXBElement<String> createAgencyName(String value) {
        return new JAXBElement<String>(_AgencyName_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://apply.grants.gov/system/GrantsCommonElements-V1.0", name = "ModificationComments")
    public JAXBElement<String> createModificationComments(String value) {
        return new JAXBElement<String>(_ModificationComments_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ActiveExclusionsType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://apply.grants.gov/system/GrantsCommonElements-V1.0", name = "ActiveExclusions")
    public JAXBElement<ActiveExclusionsType> createActiveExclusions(ActiveExclusionsType value) {
        return new JAXBElement<ActiveExclusionsType>(_ActiveExclusions_QNAME, ActiveExclusionsType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://apply.grants.gov/system/GrantsCommonElements-V1.0", name = "InstructionsURL")
    public JAXBElement<String> createInstructionsURL(String value) {
        return new JAXBElement<String>(_InstructionsURL_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://apply.grants.gov/system/GrantsCommonElements-V1.0", name = "UserID")
    public JAXBElement<String> createUserID(String value) {
        return new JAXBElement<String>(_UserID_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link OperationStatusType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://apply.grants.gov/system/GrantsCommonElements-V1.0", name = "CompletionStatus")
    public JAXBElement<OperationStatusType> createCompletionStatus(OperationStatusType value) {
        return new JAXBElement<OperationStatusType>(_CompletionStatus_QNAME, OperationStatusType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://apply.grants.gov/system/GrantsCommonElements-V1.0", name = "FundingOpportunityTitle")
    public JAXBElement<String> createFundingOpportunityTitle(String value) {
        return new JAXBElement<String>(_FundingOpportunityTitle_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://apply.grants.gov/system/GrantsCommonElements-V1.0", name = "OfferingAgency")
    public JAXBElement<String> createOfferingAgency(String value) {
        return new JAXBElement<String>(_OfferingAgency_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link XMLGregorianCalendar }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://apply.grants.gov/system/GrantsCommonElements-V1.0", name = "InstructionFileLastUpdatedTimestamp")
    public JAXBElement<XMLGregorianCalendar> createInstructionFileLastUpdatedTimestamp(XMLGregorianCalendar value) {
        return new JAXBElement<XMLGregorianCalendar>(_InstructionFileLastUpdatedTimestamp_QNAME, XMLGregorianCalendar.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://apply.grants.gov/system/GrantsCommonElements-V1.0", name = "DeleteComments")
    public JAXBElement<String> createDeleteComments(String value) {
        return new JAXBElement<String>(_DeleteComments_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://apply.grants.gov/system/GrantsCommonElements-V1.0", name = "CompetitionTitle")
    public JAXBElement<String> createCompetitionTitle(String value) {
        return new JAXBElement<String>(_CompetitionTitle_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://apply.grants.gov/system/GrantsCommonElements-V1.0", name = "FileID")
    public JAXBElement<String> createFileID(String value) {
        return new JAXBElement<String>(_FileID_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://apply.grants.gov/system/GrantsCommonElements-V1.0", name = "OpportunityCategoryExplanation")
    public JAXBElement<String> createOpportunityCategoryExplanation(String value) {
        return new JAXBElement<String>(_OpportunityCategoryExplanation_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://apply.grants.gov/system/GrantsCommonElements-V1.0", name = "LinkDescription")
    public JAXBElement<String> createLinkDescription(String value) {
        return new JAXBElement<String>(_LinkDescription_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://apply.grants.gov/system/GrantsCommonElements-V1.0", name = "SubmissionTitle")
    public JAXBElement<String> createSubmissionTitle(String value) {
        return new JAXBElement<String>(_SubmissionTitle_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://apply.grants.gov/system/GrantsCommonElements-V1.0", name = "FileName")
    public JAXBElement<String> createFileName(String value) {
        return new JAXBElement<String>(_FileName_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://apply.grants.gov/system/GrantsCommonElements-V1.0", name = "ArchiveDate")
    public JAXBElement<String> createArchiveDate(String value) {
        return new JAXBElement<String>(_ArchiveDate_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://apply.grants.gov/system/GrantsCommonElements-V1.0", name = "UpdateComments")
    public JAXBElement<String> createUpdateComments(String value) {
        return new JAXBElement<String>(_UpdateComments_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://apply.grants.gov/system/GrantsCommonElements-V1.0", name = "CompetitionID")
    public JAXBElement<String> createCompetitionID(String value) {
        return new JAXBElement<String>(_CompetitionID_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://apply.grants.gov/system/GrantsCommonElements-V1.0", name = "FolderID")
    public JAXBElement<String> createFolderID(String value) {
        return new JAXBElement<String>(_FolderID_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link SubmissionMethodType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://apply.grants.gov/system/GrantsCommonElements-V1.0", name = "SubmissionMethod")
    public JAXBElement<SubmissionMethodType> createSubmissionMethod(SubmissionMethodType value) {
        return new JAXBElement<SubmissionMethodType>(_SubmissionMethod_QNAME, SubmissionMethodType.class, null, value);
    }

}
