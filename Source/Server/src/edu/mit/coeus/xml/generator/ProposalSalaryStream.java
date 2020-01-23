/*
 * ProposalSalaryStream.java
 *
 * Created on June 7, 2005, 3:51 PM
 */

package edu.mit.coeus.xml.generator;

import edu.mit.coeus.budget.bean.BudgetTotalBean;
import edu.mit.coeus.utils.*;
import edu.mit.coeus.utils.query.*;
import edu.mit.coeus.utils.UtilFactory;
import edu.mit.coeus.utils.dbengine.DBException;
import edu.mit.coeus.exception.CoeusException;
import edu.mit.coeus.utils.xml.bean.budget.budgetSalary.*;
import java.math.BigDecimal;
import java.util.*;
import javax.xml.bind.JAXBException;

/**
 *
 * @author  jenlu
 */
public class ProposalSalaryStream extends ReportBaseStream{
    private ObjectFactory objFactory;
    private CoeusXMLGenrator xmlGenerator;
    
    private String propNumber;
    private int versionNumber;
    private int budgetTotolPeriods;
    private Date startDate;
    private Date endDate;
    private CoeusVector cvPropSalaryData;
    private static final String packageName = "edu.mit.coeus.utils.xml.bean.budget.budgetSalary";
   
    
    /** Creates a new instance of ProposalSalaryStream */
    public ProposalSalaryStream() {
        objFactory = new ObjectFactory();
        xmlGenerator = new CoeusXMLGenrator();
    }
    
    public org.w3c.dom.Document getStream(Hashtable params) throws DBException,CoeusException {
        propNumber = (String)params.get("PROPOSAL_NUM");
        versionNumber = Integer.parseInt(params.get("VERSION_NUM").toString());
        startDate = (Date)params.get("START_DATE");
        endDate = (Date)params.get("END_DATE");
        cvPropSalaryData = (CoeusVector)params.get("DATA");
        
        BudgetSalaryType budgetSalaryType = getBudgetSalay();
        return xmlGenerator.marshelObject(budgetSalaryType,packageName);
        
     }
    
    public Object getObjectStream(Hashtable params) throws DBException,CoeusException{
        propNumber = (String)params.get("PROPOSAL_NUM");
        versionNumber = Integer.parseInt(params.get("VERSION_NUM").toString());
        startDate = (Date)params.get("START_DATE");
        endDate = (Date)params.get("END_DATE");
        cvPropSalaryData = (CoeusVector)params.get("DATA");
        
        BudgetSalaryType budgetSalaryType = getBudgetSalay();
        return budgetSalaryType;
    }
    
    private BudgetSalaryType getBudgetSalay()throws CoeusXMLException,DBException,CoeusException{
        BudgetSalaryType budgetSalaryType = null;
        try{       
            budgetSalaryType = objFactory.createBudgetSalary();
            budgetSalaryType.setProposalNumber(propNumber);
            budgetSalaryType.setBudgetVersion(versionNumber);
            Calendar currentDate = Calendar.getInstance();
            currentDate.setTime(new Date());
            budgetSalaryType.setCurrentDate(currentDate);
            if (startDate != null){
                Calendar sDate = Calendar.getInstance();
                sDate.setTime(startDate);
                budgetSalaryType.setStartDate(sDate);
            }
            if (endDate != null){
                Calendar eDate = Calendar.getInstance();
                eDate.setTime(endDate);
                budgetSalaryType.setEndDate(eDate);
            }
            BudgetTotalBean bTBean = (BudgetTotalBean)cvPropSalaryData.get(0);
            budgetTotolPeriods = bTBean.getBudgetPeriods();
            budgetSalaryType.setTotalPeriod(budgetTotolPeriods);
            budgetSalaryType.getSalary().addAll(getSalaries());
        }catch(JAXBException jaxbEx){
            UtilFactory.log(jaxbEx.getMessage(),jaxbEx,"ProposalSalaryStream","getStream()");
            throw new CoeusXMLException(jaxbEx.getMessage());
        }
        return budgetSalaryType;
    }
    
    private Vector getSalaries() throws JAXBException,CoeusException,DBException{
        Vector vcSalary = new Vector();
        BudgetTotalBean budgetTotalBean;
        int salarySize = cvPropSalaryData==null?0:cvPropSalaryData.size();
        for (int salaryIndex = 0; salaryIndex < salarySize; salaryIndex++ ){
            SalaryType salaryType = objFactory.createSalaryType();
            budgetTotalBean = (BudgetTotalBean)cvPropSalaryData.elementAt(salaryIndex);
            if (budgetTotalBean.getCostElementDescription() != null && budgetTotalBean.getCostElementDescription().equals("")){
                salaryType.setCostElementDesc(UtilFactory.convertNull(budgetTotalBean.getCostElement()));
            }
            else{
                salaryType.setName(UtilFactory.convertNull(budgetTotalBean.getCostElementDescription()));
                salaryType.setTotal((new BigDecimal(budgetTotalBean.getTotal()).setScale(2,BigDecimal.ROUND_HALF_DOWN)));
                for (int periodIndex = 0; periodIndex < budgetTotolPeriods; periodIndex++){
                    BudgetPeriodData budgetPeriodData = objFactory.createBudgetPeriodData();
                    budgetPeriodData.setBudgetPeriodID(periodIndex+1);
                    if (budgetTotalBean.getPeriodCost(periodIndex) != budgetTotalBean.INITIAL_VALUE) {                        
                        budgetPeriodData.setPeriodCost((new BigDecimal(budgetTotalBean.getPeriodCost(periodIndex)).setScale(2,BigDecimal.ROUND_HALF_DOWN)));                          
                    }
                    salaryType.getPeriod().add(budgetPeriodData);   
                }
            }  
            vcSalary.addElement(salaryType);    
        }
        return vcSalary;
        
    }
}
 