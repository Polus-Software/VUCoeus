package edu.vanderbilt.coeus.s2s.generator.stream;

import edu.mit.coeus.exception.CoeusException;
import edu.mit.coeus.s2s.bean.BudgetPeriodDataBean;
import edu.mit.coeus.s2s.bean.CompensationBean;
import edu.mit.coeus.s2s.bean.OtherPersonnelBean;
import edu.mit.coeus.s2s.generator.AttachmentValidator;
import edu.mit.coeus.s2s.generator.stream.CompensationStream_V1_1;
import edu.mit.coeus.utils.CoeusVector;
import edu.mit.coeus.utils.UtilFactory;
import edu.mit.coeus.utils.dbengine.DBException;
import edu.mit.coeus.xml.generator.CoeusXMLException;
import gov.grants.apply.forms.rr_budget10_1_4_v1.BudgetYearDataType;
import gov.grants.apply.forms.rr_budget10_1_4_v1.ObjectFactory;
import gov.grants.apply.forms.rr_budget_v1_1.SectBCompensationDataType;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;
import javax.xml.bind.JAXBException;

public class OtherPersonnelStreamforBud10_V1_4 extends AttachmentValidator {
    private ObjectFactory objFactory = new ObjectFactory();
    private gov.grants.apply.system.globallibrary_v2.ObjectFactory globalObjFactory = new gov.grants.apply.system.globallibrary_v2.ObjectFactory();
    private OtherPersonnelBean otherPersonnelBean;
    private UtilFactory utilFactory;

    public BudgetYearDataType.OtherPersonnelType getOtherPersonnel(BudgetPeriodDataBean periodBean) throws CoeusXMLException, CoeusException, DBException, JAXBException {
        BudgetYearDataType.OtherPersonnelType otherPersonnelType = this.objFactory.createBudgetYearDataTypeOtherPersonnelType();
        BudgetYearDataType.OtherPersonnelType.OtherType otherOtherPersonnelType = this.objFactory.createBudgetYearDataTypeOtherPersonnelTypeOtherType();
        BudgetYearDataType.OtherPersonnelType.PostDocAssociatesType postDocType = this.objFactory.createBudgetYearDataTypeOtherPersonnelTypePostDocAssociatesType();
        BudgetYearDataType.OtherPersonnelType.GraduateStudentsType gradStudentType = this.objFactory.createBudgetYearDataTypeOtherPersonnelTypeGraduateStudentsType();
        BudgetYearDataType.OtherPersonnelType.SecretarialClericalType secType = this.objFactory.createBudgetYearDataTypeOtherPersonnelTypeSecretarialClericalType();
        BudgetYearDataType.OtherPersonnelType.UndergraduateStudentsType underGradType = this.objFactory.createBudgetYearDataTypeOtherPersonnelTypeUndergraduateStudentsType();
        otherPersonnelType.setOtherPersonnelTotalNumber(periodBean.getOtherPersonnelTotalNumber().intValue());
        otherPersonnelType.setTotalOtherPersonnelFund(periodBean.getTotalOtherPersonnelFunds());
        CoeusVector cvOtherPersonnel = periodBean.getOtherPersonnel();
        CompensationStream_V1_1 compensationStream = new CompensationStream_V1_1();
        for (int i = 0; i < cvOtherPersonnel.size(); ++i) {
            SectBCompensationDataType sectBCompType;
            OtherPersonnelBean otherPersonnelBean = (OtherPersonnelBean)cvOtherPersonnel.elementAt(i);
            String personnelType = otherPersonnelBean.getPersonnelType();
            if (personnelType.equals("PostDoc")) {
                postDocType.setNumberOfPersonnel(otherPersonnelBean.getNumberPersonnel().intValue());
                postDocType.setProjectRole(otherPersonnelBean.getRole());
                sectBCompType = compensationStream.getSectBComp(otherPersonnelBean.getCompensation());
                postDocType.setRequestedSalary(sectBCompType.getRequestedSalary());
                postDocType.setFringeBenefits(sectBCompType.getFringeBenefits());
                postDocType.setAcademicMonths(sectBCompType.getAcademicMonths());
                postDocType.setCalendarMonths(sectBCompType.getCalendarMonths());
                postDocType.setFundsRequested(sectBCompType.getFundsRequested());
                postDocType.setSummerMonths(sectBCompType.getSummerMonths());
                otherPersonnelType.setPostDocAssociates(postDocType);
                continue;
            }
            if (personnelType.equals("Grad")) {
                gradStudentType.setNumberOfPersonnel(otherPersonnelBean.getNumberPersonnel().intValue());
                gradStudentType.setProjectRole(otherPersonnelBean.getRole());
                sectBCompType = compensationStream.getSectBComp(otherPersonnelBean.getCompensation());
                gradStudentType.setRequestedSalary(sectBCompType.getRequestedSalary());
                gradStudentType.setFringeBenefits(sectBCompType.getFringeBenefits());
                gradStudentType.setAcademicMonths(sectBCompType.getAcademicMonths());
                gradStudentType.setCalendarMonths(sectBCompType.getCalendarMonths());
                gradStudentType.setFundsRequested(sectBCompType.getFundsRequested());
                gradStudentType.setSummerMonths(sectBCompType.getSummerMonths());
                otherPersonnelType.setGraduateStudents(gradStudentType);
                continue;
            }
            if (personnelType.equals("UnderGrad")) {
                underGradType.setNumberOfPersonnel(otherPersonnelBean.getNumberPersonnel().intValue());
                underGradType.setProjectRole(otherPersonnelBean.getRole());
                sectBCompType = compensationStream.getSectBComp(otherPersonnelBean.getCompensation());
                underGradType.setRequestedSalary(sectBCompType.getRequestedSalary());
                underGradType.setFringeBenefits(sectBCompType.getFringeBenefits());
                underGradType.setAcademicMonths(sectBCompType.getAcademicMonths());
                underGradType.setCalendarMonths(sectBCompType.getCalendarMonths());
                underGradType.setFundsRequested(sectBCompType.getFundsRequested());
                underGradType.setSummerMonths(sectBCompType.getSummerMonths());
                otherPersonnelType.setUndergraduateStudents(underGradType);
                continue;
            }
            if (personnelType.equals("Sec")) {
                secType.setNumberOfPersonnel(otherPersonnelBean.getNumberPersonnel().intValue());
                secType.setProjectRole(otherPersonnelBean.getRole());
                sectBCompType = compensationStream.getSectBComp(otherPersonnelBean.getCompensation());
                secType.setRequestedSalary(sectBCompType.getRequestedSalary());
                secType.setFringeBenefits(sectBCompType.getFringeBenefits());
                secType.setAcademicMonths(sectBCompType.getAcademicMonths());
                secType.setCalendarMonths(sectBCompType.getCalendarMonths());
                secType.setFundsRequested(sectBCompType.getFundsRequested());
                secType.setSummerMonths(sectBCompType.getSummerMonths());
                otherPersonnelType.setSecretarialClerical(secType);
                continue;
            }
            otherOtherPersonnelType = this.objFactory.createBudgetYearDataTypeOtherPersonnelTypeOtherType();
            otherOtherPersonnelType.setNumberOfPersonnel(otherPersonnelBean.getNumberPersonnel().intValue());
            otherOtherPersonnelType.setProjectRole(otherPersonnelBean.getRole());
            sectBCompType = compensationStream.getSectBComp(otherPersonnelBean.getCompensation());
            otherOtherPersonnelType.setRequestedSalary(sectBCompType.getRequestedSalary());
            otherOtherPersonnelType.setFringeBenefits(sectBCompType.getFringeBenefits());
            otherOtherPersonnelType.setAcademicMonths(sectBCompType.getAcademicMonths());
            otherOtherPersonnelType.setCalendarMonths(sectBCompType.getCalendarMonths());
            otherOtherPersonnelType.setFundsRequested(sectBCompType.getFundsRequested());
            otherOtherPersonnelType.setSummerMonths(sectBCompType.getSummerMonths());
            otherPersonnelType.getOther().add(otherOtherPersonnelType);
        }
        return otherPersonnelType;
    }
}