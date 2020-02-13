/*
 * InstPropStream.java
 *
 * Created on December 13, 2004, 4:02 PM
 */

package edu.mit.coeus.utils.xml.bean.instProp.generator;

import edu.mit.coeus.award.bean.AwardCustomDataBean;
import edu.mit.coeus.exception.CoeusException;
import edu.mit.coeus.instprop.bean.InstituteProposalCustomDataBean;
import edu.mit.coeus.utils.CoeusVector;
import edu.mit.coeus.utils.dbengine.DBException;
import edu.mit.coeus.utils.xml.bean.instProp.*;
import edu.mit.coeus.utils.xml.bean.instProp.impl.*;
import edu.mit.coeus.departmental.bean.DepartmentCurrentReportBean;
import edu.mit.coeus.departmental.bean.DepartmentPendingReportBean;
import edu.mit.coeus.xml.generator.ReportBaseStream;


import java.io.ByteArrayOutputStream;
import java.math.BigDecimal;
import java.util.Calendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Set;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

/**
 *
 * @author  chandrashekara
 */
public class InstPropStream extends ReportBaseStream{
    
    private CoeusVector data;
    private String personName;
    private ByteArrayOutputStream streamData;
    private CoeusVector reportData;
    private static final char PRINT_CURRENT = 'L';
    private static final char PRINT_PENDING = 'O';
    private DepartmentCurrentReportBean currentBean;
    private DepartmentPendingReportBean pendingBean;
    private String EMPTY_STRING = "";
    /** Creates a new instance of InstPropStream */
    public InstPropStream() {
        
    }
    
    public Object getObjectStream(Hashtable params) throws DBException,CoeusException{
        CoeusVector data = (CoeusVector)params.get("DATA");
        String personName = (String)params.get("PERSON_NAME");
        char reportType = ((Character)params.get("REPORT_TYPE")).charValue();
        try{
            reportData = data;
            CurrentAndPendingSupport mainBean= new CurrentAndPendingSupportImpl();
            mainBean.setPersonName(personName);
            if(reportType==PRINT_CURRENT){
                if(reportData != null && !reportData.isEmpty()){
                    // Added for COEUSQA-1686 - Add additional fields to the Current  Pending Support Schema - Start
                    // Setting the custom element column value
                    HashMap hmCustomElementColumns =  getCurrentCustomDataColumns(reportData);
                    Iterator columnIterator = hmCustomElementColumns.keySet().iterator();
                    int columnLabelIndex = 1;
                    CurrentAndPendingSupportType.CurrentReportCEColumnNamesType currentColumnNameTypeBean = new CurrentAndPendingSupportTypeImpl.CurrentReportCEColumnNamesTypeImpl();
                    while(columnIterator.hasNext()){
                        String columnName =  (String)columnIterator.next();
                        String columnLabel = (String)hmCustomElementColumns.get(columnName);
                        if(columnLabelIndex == 1){
                            currentColumnNameTypeBean.setCEColumnName1(columnLabel);
                        }else if(columnLabelIndex == 2){
                            currentColumnNameTypeBean.setCEColumnName2(columnLabel);
                        }else if(columnLabelIndex == 3){
                            currentColumnNameTypeBean.setCEColumnName3(columnLabel);
                        }else if(columnLabelIndex == 4){
                            currentColumnNameTypeBean.setCEColumnName4(columnLabel);
                        }else if(columnLabelIndex == 5){
                            currentColumnNameTypeBean.setCEColumnName5(columnLabel);
                        }else if(columnLabelIndex == 6){
                            currentColumnNameTypeBean.setCEColumnName6(columnLabel);
                        }else if(columnLabelIndex == 7){
                            currentColumnNameTypeBean.setCEColumnName7(columnLabel);
                        }else if(columnLabelIndex == 8){
                            currentColumnNameTypeBean.setCEColumnName8(columnLabel);
                        }else if(columnLabelIndex == 9){
                            currentColumnNameTypeBean.setCEColumnName9(columnLabel);
                        }else if(columnLabelIndex == 10){
                            currentColumnNameTypeBean.setCEColumnName10(columnLabel);
                            break;
                        }
                        columnLabelIndex++;
                    }
                    mainBean.setCurrentReportCEColumnNames(currentColumnNameTypeBean);
                    // Added for COEUSQA-1686 - Add additional fields to the Current  Pending Support Schema - End
                    
                    for(int index=0; index <reportData.size(); index++) {
                        CurrentAndPendingSupportType.CurrentSupportType currentTypeBean = new CurrentAndPendingSupportTypeImpl.CurrentSupportTypeImpl();
                        currentBean = (DepartmentCurrentReportBean )reportData.get(index);
                        currentTypeBean.setAgency(currentBean.getSponsorName());
                        currentTypeBean.setTitle(currentBean.getTitle());
                        currentTypeBean.setSponsorAwardNumber(currentBean.getSponsorAwardNumber());
                        currentTypeBean.setPI(currentBean.getPrinicipleInvestigator());
                        
                        BigDecimal distributedAmount = new BigDecimal(currentBean.getObliDistrubutableAmount());
                        currentTypeBean.setAwardAmount(distributedAmount.setScale(2,BigDecimal.ROUND_HALF_DOWN));
                        
                        Calendar currentDate = Calendar.getInstance();
                        if(currentBean.getAwardEffectiveDate()!= null){
                            currentDate.setTime(currentBean.getAwardEffectiveDate());
                            currentTypeBean.setEffectiveDate(currentDate);
                        }else{
                            currentTypeBean.setEffectiveDate(null);
                        }
                        
                        currentDate = Calendar.getInstance();
                        if(currentBean.getFinalExpirationDate()!= null){
                            currentDate.setTime(currentBean.getFinalExpirationDate());
                            currentTypeBean.setEndDate(currentDate);
                        }else{
                            currentTypeBean.setEndDate(null);
                        }
                        
                        //Case:3505- Add %effort to current and pending report  Start
                        currentTypeBean.setPercentageEffort(new BigDecimal(currentBean.getPercentageEffort()));
                        currentTypeBean.setAcademicYearEffort(new BigDecimal(currentBean.getAcademicYearEffort()));
                        currentTypeBean.setSummerYearEffort(new BigDecimal(currentBean.getSummerYearEffort()));
                        currentTypeBean.setCalendarYearEffort(new BigDecimal(currentBean.getCalendarYearEffort()));
                        //Case:3505- Add %effort to current and pending report   End
                        
                        // Added for COEUSQA-1686 - Add additional fields to the Current  Pending Support Schema - Start
                        // Based on the columns applicable in all the award for the group code defined in parameter CURRENT_PENDING_REPORT_GROUP_NAME
                        
                        CoeusVector cvCustomElement = currentBean.getCustomElements();
                        if(cvCustomElement != null && !cvCustomElement.isEmpty()){
                            Iterator columnNameIterator = hmCustomElementColumns.keySet().iterator();
                            int custEleColumnNameIndex = 0;
                            while(columnNameIterator.hasNext()){
                                String columnName =  (String)columnNameIterator.next();
                                     
                                for(int custElemIndex=0;custElemIndex<cvCustomElement.size();custElemIndex++){
                                    String columnValue = EMPTY_STRING;
                                    AwardCustomDataBean awardCustomData = (AwardCustomDataBean)cvCustomElement.get(custElemIndex);
                                    if(columnName.trim().equals(awardCustomData.getColumnName().trim())){
                                        columnValue = awardCustomData.getColumnValue();
                                        if(awardCustomData.getColumnValue() == null){
                                            columnValue = EMPTY_STRING;
                                        }
                                        if(custEleColumnNameIndex == 0){
                                            currentTypeBean.setCEColumnValue1(columnValue);
                                            break;
                                        }else if(custEleColumnNameIndex == 1){
                                            currentTypeBean.setCEColumnValue2(columnValue);
                                            break;
                                        }else if(custEleColumnNameIndex == 2){
                                            currentTypeBean.setCEColumnValue3(columnValue);
                                            break;
                                        }else if(custEleColumnNameIndex == 3){
                                            currentTypeBean.setCEColumnValue4(columnValue);
                                            break;
                                        }else if(custEleColumnNameIndex == 4){
                                            currentTypeBean.setCEColumnValue5(columnValue);
                                            break;
                                        }else if(custEleColumnNameIndex == 5){
                                            currentTypeBean.setCEColumnValue6(columnValue);
                                            break;
                                        }else if(custEleColumnNameIndex == 6){
                                            currentTypeBean.setCEColumnValue7(columnValue);
                                            break;
                                        }else if(custEleColumnNameIndex == 7){
                                            currentTypeBean.setCEColumnValue8(columnValue);
                                            break;
                                        }else if(custEleColumnNameIndex == 8){
                                            currentTypeBean.setCEColumnValue9(columnValue);
                                            break;
                                        }else if(custEleColumnNameIndex == 9){
                                            currentTypeBean.setCEColumnValue10(columnValue);
                                            break;
                                        }
                                    }
                                    
                                }
                                custEleColumnNameIndex++;
                            }
                        }
                        // Added for COEUSQA-1686 - Add additional fields to the Current  Pending Support Schema - End
                        
                        // Setting values to the main bean
                        mainBean.getCurrentSupport().add(currentTypeBean);
                    }

                    
                }
            }else if(reportType==PRINT_PENDING){
                
                // Added for COEUSQA-1686 - Add additional fields to the Current  Pending Support Schema - Start
                // Setting the custom elements column applicable for the group code define in CURRENT_PENDING_REPORT_GROUP_NAME
                HashMap hmCustomElementColumns =  getPendingCustomDataColumns(reportData);
                Iterator columnIterator = hmCustomElementColumns.keySet().iterator();
                int columnLabelIndex = 1;
                CurrentAndPendingSupportType.PendingReportCEColumnNamesType pendingColumnNameTypeBean = new CurrentAndPendingSupportTypeImpl.PendingReportCEColumnNamesTypeImpl();
                while(columnIterator.hasNext()){
                    String columnName =  (String)columnIterator.next();
                    String columnLabel = (String)hmCustomElementColumns.get(columnName);
                    if(columnLabelIndex == 1){
                        pendingColumnNameTypeBean.setCEColumnName1(columnLabel);
                    }else if(columnLabelIndex == 2){
                        pendingColumnNameTypeBean.setCEColumnName2(columnLabel);
                    }else if(columnLabelIndex == 3){
                        pendingColumnNameTypeBean.setCEColumnName3(columnLabel);
                    }else if(columnLabelIndex == 4){
                        pendingColumnNameTypeBean.setCEColumnName4(columnLabel);
                    }else if(columnLabelIndex == 5){
                        pendingColumnNameTypeBean.setCEColumnName5(columnLabel);
                    }else if(columnLabelIndex == 6){
                        pendingColumnNameTypeBean.setCEColumnName6(columnLabel);
                    }else if(columnLabelIndex == 7){
                        pendingColumnNameTypeBean.setCEColumnName7(columnLabel);
                    }else if(columnLabelIndex == 8){
                        pendingColumnNameTypeBean.setCEColumnName8(columnLabel);
                    }else if(columnLabelIndex == 9){
                        pendingColumnNameTypeBean.setCEColumnName9(columnLabel);
                    }else if(columnLabelIndex == 10){
                        pendingColumnNameTypeBean.setCEColumnName10(columnLabel);
                        break;
                    }
                    columnLabelIndex++;
                    
                }
                mainBean.setPendingReportCEColumnNames(pendingColumnNameTypeBean);
                 // Added for COEUSQA-1686 - Add additional fields to the Current  Pending Support Schema - End
                
                for(int index=0; index<reportData.size(); index++){
                    CurrentAndPendingSupportType.PendingSupportType pendingTypeBean =
                            new CurrentAndPendingSupportTypeImpl.PendingSupportTypeImpl();
                    pendingBean = (DepartmentPendingReportBean)reportData.get(index);
                    pendingTypeBean.setProposalNumber(pendingBean.getProposalNumber());
                    pendingTypeBean.setAgency(pendingBean.getSponsorName());
                    pendingTypeBean.setPI(pendingBean.getPrincipleInvestigator());
                    pendingTypeBean.setTitle(pendingBean.getTitle());
                    
                    BigDecimal totalAmount = new BigDecimal(pendingBean.getTotalCost());
                    pendingTypeBean.setTotalRequested(totalAmount.setScale(2,BigDecimal.ROUND_HALF_DOWN));
                    
                    Calendar pendingDate = Calendar.getInstance();
                    if(pendingBean.getRequestedStartDateTotal()!= null){
                        pendingDate.setTime(pendingBean.getRequestedStartDateTotal());
                        pendingTypeBean.setEffectiveDate(pendingDate);
                    }else{
                        pendingTypeBean.setEffectiveDate(null);
                    }
                    
                    pendingDate = Calendar.getInstance();
                    if(pendingBean.getRequestEndDateTotal()!= null){
                        pendingDate.setTime(pendingBean.getRequestEndDateTotal());
                        pendingTypeBean.setEndDate(pendingDate);
                    }else{
                        pendingTypeBean.setEndDate(null);
                    }
                    //Case:3505- Add %effort to current and pending report   Start 
                    pendingTypeBean.setPercentageEffort(new BigDecimal(pendingBean.getPercentageEffort()));
                    pendingTypeBean.setAcademicYearEffort(new BigDecimal(pendingBean.getAcademicYearEffort()));
                    pendingTypeBean.setSummerYearEffort(new BigDecimal(pendingBean.getSummerYearEffort()));
                    pendingTypeBean.setCalendarYearEffort(new BigDecimal(pendingBean.getCalendarYearEffort()));
                    //Case:3505- Add %effort to current and pending report  End     
                    
                    // Added for COEUSQA-1686 - Add additional fields to the Current  Pending Support Schema - Start
                    
                    CoeusVector cvCustomElement = pendingBean.getCustomElements();
                    if(cvCustomElement != null && !cvCustomElement.isEmpty()){
                        Iterator columnNameIterator = hmCustomElementColumns.keySet().iterator();
                        int columnNameIndex = 0;
                        while(columnNameIterator.hasNext()){
                            String columnName =  (String)columnNameIterator.next();
                            for(int custElemIndex=0;custElemIndex<cvCustomElement.size();custElemIndex++){
                                String columnValue = EMPTY_STRING;
                                InstituteProposalCustomDataBean ipCustomData = (InstituteProposalCustomDataBean)cvCustomElement.get(custElemIndex);
                                if(columnName.trim().equals(ipCustomData.getColumnName().trim())){
                                    columnValue = ipCustomData.getColumnValue();
                                    if(ipCustomData.getColumnLabel() == null){
                                        columnValue = EMPTY_STRING;
                                    }
                                    if(columnNameIndex == 0){
                                        pendingTypeBean.setCEColumnValue1(columnValue);
                                    }else if(columnNameIndex == 1){
                                        pendingTypeBean.setCEColumnValue2(columnValue);
                                    }else if(columnNameIndex == 2){
                                        pendingTypeBean.setCEColumnValue3(columnValue);
                                    }else if(columnNameIndex == 3){
                                        pendingTypeBean.setCEColumnValue4(columnValue);
                                    }else if(columnNameIndex == 4){
                                        pendingTypeBean.setCEColumnValue5(columnValue);
                                    }else if(columnNameIndex == 5){
                                        pendingTypeBean.setCEColumnValue6(columnValue);
                                    }else if(columnNameIndex == 6){
                                        pendingTypeBean.setCEColumnValue7(columnValue);
                                    }else if(columnNameIndex == 7){
                                        pendingTypeBean.setCEColumnValue8(columnValue);
                                    }else if(columnNameIndex == 8){
                                        pendingTypeBean.setCEColumnValue9(columnValue);
                                    }else if(columnNameIndex == 9){
                                        pendingTypeBean.setCEColumnValue10(columnValue);
                                    }
                                }
                                
                            }
                            columnNameIndex++;
                        }
                    }
                    // Added for COEUSQA-1686 - Add additional fields to the Current  Pending Support Schema - End 
                    
                    
                    // Setting the values to the main bean
                    mainBean.getPendingSupport().add(pendingTypeBean);
                    
                }
            }
            return mainBean;
        }catch (Exception exception) {
            CoeusException coeusException = new CoeusException(exception.getMessage());
            throw coeusException;
        }
    }
    
    // Get the data , person name and mode from servlet and set the values to the
    //JAXBeans and add to the stream .
    /*public ByteArrayOutputStream getStreamData(CoeusVector data,String personName,
        char reportType) throws JAXBException{
     
        Hashtable hashtable = new Hashtable();
        hashtable.put("DATA", data);
        hashtable.put("PERSON_NAME", personName);
        hashtable.put("REPORT_TYPE", new Character(reportType));
        CurrentAndPendingSupport mainBean = (CurrentAndPendingSupport)getObjectStream(hashtable);
     
        JAXBContext jaxbContext = JAXBContext.newInstance("edu.mit.coeus.utils.xml.bean.instProp");
        Marshaller marshaller = jaxbContext.createMarshaller();
        ObjectFactory objFactory = new ObjectFactory();
        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, new Boolean(true));
        ByteArrayOutputStream streamData = new ByteArrayOutputStream();
      //  marshaller.marshal(mainBean,System.out);
        marshaller.marshal(mainBean, streamData);
        return streamData;
    }*/
    
    //COEUSQA-1686 - Add additional fields to the Current  Pending Support Schema - Start
    /** This method will add the custom data column values from all the current reports and store them
     *  in a array
     *  @return Object[]
     */
    public HashMap getCurrentCustomDataColumns(CoeusVector cvCustomElement){
        HashMap hmUniqueColumnData = new HashMap();
        for(Object reportFormData : cvCustomElement){
            DepartmentCurrentReportBean currentReportBean = (DepartmentCurrentReportBean)reportFormData;
            CoeusVector cvCustomData = currentReportBean.getCustomElements();
            if(cvCustomData!=null){
                for(Object cvCustomDataBean : cvCustomData){
                    AwardCustomDataBean awardCustomDataBean = (AwardCustomDataBean)cvCustomDataBean;
                    if(awardCustomDataBean.getGroupCode()!=null && awardCustomDataBean.getGroupCode().equals(currentReportBean.getGroupCode())){
                        hmUniqueColumnData.put(awardCustomDataBean.getColumnName(),awardCustomDataBean.getColumnLabel());
                    }
                }
            }
        }
        return hmUniqueColumnData;
    }
    
    /** This method will add the custom data column values from all the pending reports and store them
     *  in a array
     *  @return Object[]
     */
    public HashMap getPendingCustomDataColumns(CoeusVector cvCustomElement){
        HashMap hmUniqueColumnData = new HashMap();
        for(Object reportFormData : cvCustomElement){
            DepartmentPendingReportBean pendingReportBean = (DepartmentPendingReportBean)reportFormData;
            CoeusVector cvCustomData = pendingReportBean.getCustomElements();
            if(cvCustomData!=null){
                for(Object cvCustomDataBean : cvCustomData){
                    InstituteProposalCustomDataBean ipCustomData = (InstituteProposalCustomDataBean)cvCustomDataBean;
                    if(ipCustomData.getGroupCode()!=null && ipCustomData.getGroupCode().equals(pendingReportBean.getGroupCode())){
                        String columnName = ipCustomData.getColumnName();
                        hmUniqueColumnData.put(ipCustomData.getColumnName(),ipCustomData.getColumnLabel());
                    }
                }
            }
        }
        
        return hmUniqueColumnData;
    }
    //COEUSQA-1686 - Add additional fields to the Current  Pending Support Schema - Start
    
}
