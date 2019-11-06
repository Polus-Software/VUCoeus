package edu.vanderbilt.coeus.s2s.generator.stream;

import edu.mit.coeus.exception.CoeusException;
import edu.mit.coeus.propdev.bean.ProposalNarrativeFormBean;
import edu.mit.coeus.propdev.bean.ProposalNarrativePDFSourceBean;
import edu.mit.coeus.s2s.bean.BudgetPeriodDataBean;
import edu.mit.coeus.s2s.bean.KeyPersonBean;
import edu.mit.coeus.s2s.bean.S2STxnBean;
import edu.mit.coeus.s2s.generator.AttachmentValidator;
import edu.mit.coeus.s2s.generator.stream.AttachedFileDataTypeStream;
import edu.mit.coeus.utils.CoeusVector;
import edu.mit.coeus.utils.UtilFactory;
import edu.mit.coeus.utils.dbengine.DBException;
import edu.mit.coeus.xml.generator.CoeusXMLException;
import edu.mit.coeus.xml.generator.CoeusXMLGenrator;
import gov.grants.apply.coeus.extrakeyperson.ExtraKeyPersonList;
import gov.grants.apply.coeus.extrakeyperson.ExtraKeyPersonListType;
import gov.grants.apply.forms.rr_budget_1_4_v1.BudgetYearDataType;
import gov.grants.apply.system.attachments_v1.ObjectFactory;
import gov.grants.apply.system.globallibrary_v2.HumanNameDataType;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Date;
import java.util.Calendar;
import java.util.List;
import java.util.TimeZone;
import java.util.Vector;
import javax.xml.bind.JAXBException;
import org.apache.fop.apps.FOPException;
import org.w3c.dom.Document;

public class KeyPersonStreamForBud_V1_4 extends AttachmentValidator {
    private gov.grants.apply.forms.rr_budget_1_4_v1.ObjectFactory objFactory = new gov.grants.apply.forms.rr_budget_1_4_v1.ObjectFactory();
    private gov.grants.apply.system.globallibrary_v2.ObjectFactory globalObjFactory = new gov.grants.apply.system.globallibrary_v2.ObjectFactory();
    private gov.grants.apply.coeus.extrakeyperson.ObjectFactory extraKeyPerObjFctory;
    private ObjectFactory attachmentsObjFactory = new ObjectFactory();
    private gov.grants.apply.system.global_v1.ObjectFactory globObjFactory = new gov.grants.apply.system.global_v1.ObjectFactory();
    private KeyPersonBean keyPersonBean;
    private UtilFactory utilFactory;
    private Calendar calendar;
    private S2STxnBean s2sTxnBean = new S2STxnBean();
    private Vector extraAttachments = new Vector();

    public BudgetYearDataType.KeyPersonsType getKeyPersons(BudgetPeriodDataBean periodBean) throws CoeusXMLException, CoeusException, DBException, JAXBException {
        String role;
        BudgetYearDataType.KeyPersonsType keyPersonsType = this.objFactory.createBudgetYearDataTypeKeyPersonsType();
        keyPersonsType.setTotalFundForKeyPersons(periodBean.getTotalFundsKeyPersons());
        int isNih = this.s2sTxnBean.getIsNIHSponsor(periodBean.getProposalNumber());
        CoeusVector cvKeyPersons = periodBean.getKeyPersons();
        for (int i = 0; i < cvKeyPersons.size(); ++i) {
            KeyPersonBean keyPersonBean = (KeyPersonBean)cvKeyPersons.elementAt(i);
            BudgetYearDataType.KeyPersonsType.KeyPersonType keyPersonType = this.objFactory.createBudgetYearDataTypeKeyPersonsTypeKeyPersonType();
            role = keyPersonBean.getRole();
            if (isNih == 1 && role.equals("Co-PD/PI")) {
                role = "Co-Investigator";
            }
            keyPersonType.setProjectRole(role);
            keyPersonType.setName(this.getName(keyPersonBean));
            keyPersonType.setAcademicMonths(keyPersonBean.getAcademicMonths());
            String baseSalary = "0.00";
            baseSalary = periodBean.getBudgetPeriod() > 10 ? this.s2sTxnBean.getBaseSalaryForPeriod(periodBean.getProposalNumber(), periodBean.getVersion(), 10, keyPersonBean.getPersonId()) : this.s2sTxnBean.getBaseSalaryForPeriod(periodBean.getProposalNumber(), periodBean.getVersion(), periodBean.getBudgetPeriod(), keyPersonBean.getPersonId());
            if (baseSalary == null) {
                keyPersonType.setBaseSalary(keyPersonBean.getBaseSalary());
            } else {
                BigDecimal bdBaseSalary = new BigDecimal("0");
                bdBaseSalary = new BigDecimal(baseSalary);
                bdBaseSalary = bdBaseSalary.setScale(2, 4);
                keyPersonType.setBaseSalary(bdBaseSalary);
            }
            keyPersonType.setCalendarMonths(keyPersonBean.getCalendarMonths());
            keyPersonType.setFringeBenefits(keyPersonBean.getFringe());
            keyPersonType.setFundsRequested(keyPersonBean.getFundsRequested());
            keyPersonType.setRequestedSalary(keyPersonBean.getRequestedSalary());
            keyPersonType.setSummerMonths(keyPersonBean.getSummerMonths());
            keyPersonsType.getKeyPerson().add(keyPersonType);
        }
        CoeusVector cvExtraPersons = periodBean.getExtraKeyPersons();
        if (cvExtraPersons != null && cvExtraPersons.size() > 0) {
            BigDecimal extraFunds = new BigDecimal("0");
            this.extraKeyPerObjFctory = new gov.grants.apply.coeus.extrakeyperson.ObjectFactory();
            ExtraKeyPersonList extraKeyPersonList = this.extraKeyPerObjFctory.createExtraKeyPersonList();
            extraKeyPersonList.setProposalNumber(periodBean.getProposalNumber());
            extraKeyPersonList.setBudgetPeriod(new BigInteger(Integer.toString(periodBean.getBudgetPeriod())));
            List extraPersonList = extraKeyPersonList.getKeyPersons();
            for (int i2 = 0; i2 < cvExtraPersons.size(); ++i2) {
                KeyPersonBean keyPersonBean = (KeyPersonBean)cvExtraPersons.elementAt(i2);
                extraFunds = extraFunds.add(keyPersonBean.getFundsRequested());
                ExtraKeyPersonListType.KeyPersonsType extraKeyPersonType = this.extraKeyPerObjFctory.createExtraKeyPersonListTypeKeyPersonsType();
                ExtraKeyPersonListType.KeyPersonsType.CompensationType compType = this.extraKeyPerObjFctory.createExtraKeyPersonListTypeKeyPersonsTypeCompensationType();
                extraKeyPersonType.setFirstName(keyPersonBean.getFirstName());
                extraKeyPersonType.setMiddleName(keyPersonBean.getMiddleName());
                extraKeyPersonType.setLastName(keyPersonBean.getLastName());
                role = keyPersonBean.getRole();
                if (isNih == 1 && role.equals("Co-PD/PI")) {
                    role = "Co-Investigator";
                }
                extraKeyPersonType.setProjectRole(role);
                compType.setAcademicMonths(keyPersonBean.getAcademicMonths());
                compType.setCalendarMonths(keyPersonBean.getCalendarMonths());
                compType.setSummerMonths(keyPersonBean.getSummerMonths());
                String baseExtraSalary = "0.00";
                baseExtraSalary = periodBean.getBudgetPeriod() > 10 ? this.s2sTxnBean.getBaseSalaryForPeriod(periodBean.getProposalNumber(), periodBean.getVersion(), 10, keyPersonBean.getPersonId()) : this.s2sTxnBean.getBaseSalaryForPeriod(periodBean.getProposalNumber(), periodBean.getVersion(), periodBean.getBudgetPeriod(), keyPersonBean.getPersonId());
                if (baseExtraSalary == null) {
                    compType.setBaseSalary(keyPersonBean.getBaseSalary());
                } else {
                    BigDecimal bdBaseSalary = new BigDecimal("0");
                    bdBaseSalary = new BigDecimal(baseExtraSalary);
                    bdBaseSalary = bdBaseSalary.setScale(2, 4);
                    compType.setBaseSalary(bdBaseSalary);
                }
                compType.setFringeBenefits(keyPersonBean.getFringe());
                compType.setFundsRequested(keyPersonBean.getFundsRequested());
                compType.setRequestedSalary(keyPersonBean.getRequestedSalary());
                extraKeyPersonType.setCompensation(compType);
                extraPersonList.add(extraKeyPersonType);
            }
            keyPersonsType.setTotalFundForAttachedKeyPersons(extraFunds);
            CoeusXMLGenrator xmlGen = new CoeusXMLGenrator();
            Document extraKeyPerDoc = xmlGen.marshelObject((Object)extraKeyPersonList, "gov.grants.apply.coeus.extrakeyperson");
            byte[] pdfBytes = null;
            try {
                InputStream templateIS = this.getClass().getResourceAsStream("/edu/mit/coeus/s2s/template/ExtraKeyPersonAttachment.xsl");
                byte[] tmplBytes = new byte[templateIS.available()];
                templateIS.read(tmplBytes);
                pdfBytes = xmlGen.generatePdfBytes(extraKeyPerDoc, tmplBytes, null, periodBean.getProposalNumber() + "_" + periodBean.getBudgetPeriod() + "_ExKeyPerson");
            }
            catch (FOPException ex) {
                UtilFactory.log((String)ex.getMessage(), (Throwable)ex, (String)"KeyPersonStream", (String)"getKeyPersons");
                throw new CoeusException(ex.getMessage());
            }
            catch (IOException ex) {
                UtilFactory.log((String)ex.getMessage(), (Throwable)ex, (String)"KeyPersonStream", (String)"getKeyPersons");
                throw new CoeusException(ex.getMessage());
            }
            ProposalNarrativeFormBean propNarrBean = new ProposalNarrativeFormBean();
            propNarrBean.setProposalNumber(periodBean.getProposalNumber());
            propNarrBean.setModuleTitle("BudgetPeriod_" + periodBean.getBudgetPeriod());
            propNarrBean.setComments("Auto generated document for extra key persons");
            propNarrBean.setModuleStatusCode('C');
            propNarrBean.setNarrativeTypeCode(11);
            ProposalNarrativePDFSourceBean propNarrPDFBean = new ProposalNarrativePDFSourceBean();
            propNarrPDFBean.setProposalNumber(periodBean.getProposalNumber());
            propNarrPDFBean.setAcType("I");
            propNarrPDFBean.setFileBytes(pdfBytes);
            propNarrPDFBean.setFileName(AttachedFileDataTypeStream.addExtension((String)(periodBean.getProposalNumber() + "_EXTRA_KEYPERSONS")));
            this.s2sTxnBean.insertAutoGenNarrativeDetails(propNarrBean, propNarrPDFBean, periodBean.getBudgetPeriod() == 1);
        }
        return keyPersonsType;
    }

    private HumanNameDataType getName(KeyPersonBean keyPersonBean) throws CoeusException, JAXBException {
        HumanNameDataType humanNameDataType = this.globalObjFactory.createHumanNameDataType();
        humanNameDataType.setFirstName(keyPersonBean.getFirstName());
        humanNameDataType.setLastName(keyPersonBean.getLastName());
        humanNameDataType.setMiddleName(keyPersonBean.getMiddleName());
        return humanNameDataType;
    }

    private static BigDecimal convDoubleToBigDec(double d) {
        return new BigDecimal(d);
    }

    private Calendar getCal(Date date) {
        if (date == null) {
            return null;
        }
        Calendar cal = Calendar.getInstance(TimeZone.getDefault());
        cal.setTime(date);
        return cal;
    }

    private Calendar getTodayDate() {
        Calendar cal = Calendar.getInstance(TimeZone.getDefault());
        java.util.Date today = cal.getTime();
        cal.setTime(today);
        return cal;
    }

    public Vector getExtraAttachments() {
        return this.extraAttachments;
    }

    public void setExtraAttachments(Vector extraAttachments) {
        this.extraAttachments = extraAttachments;
    }
}