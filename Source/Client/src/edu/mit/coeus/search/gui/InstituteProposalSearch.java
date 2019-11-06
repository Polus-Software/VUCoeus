/** Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 */

package edu.mit.coeus.search.gui;

import edu.mit.coeus.utils.*;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Vector;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionListener;
import java.sql.Timestamp;
import java.sql.Date;
import java.text.*;

import edu.mit.coeus.instprop.bean.InstituteProposalBean;

/**
 * InstituteProposalSearch.java
 * @author  Vyjayanthi
 * Created on May 12, 2004, 10:54 AM
 */
public class InstituteProposalSearch extends CoeusSearch
implements ListSelectionListener {
    
    /** Holds an instance of <CODE>InstituteProposalDetails</CODE> */
    private static InstituteProposalDetails pnlInstituteProposalDetails =
    new InstituteProposalDetails();
    
    /** Holds an instance of ListSelectionModel */
    private ListSelectionModel searchSelectionModel;
    
    /** Holds a reference to the search results data */
    private Vector vecInstituteProposal;
    
    /** To indicate whether the table contains data or not */
    private static boolean tableEmpty;
    
    /** Holds an instance of JPanel containing results table and details panel */
    private static JPanel pnlSearchResult = new JPanel(new BorderLayout());
    
    private static final String ZERO = "0";
    private static final String TITLE = "TITLE";
    private static final String START_DATE_INITIAL = "REQUESTED_START_DATE_INITIAL";
    private static final String END_DATE_INITIAL = "REQUESTED_END_DATE_INITIAL";
    private static final String DIRECT_COST_INITIAL = "TOTAL_DIRECT_COST_INITIAL";
    private static final String INDIRECT_COST_INITIAL = "TOTAL_INDIRECT_COST_INITIAL";
    private static final String START_DATE_TOTAL = "REQUESTED_START_DATE_TOTAL";
    private static final String END_DATE_TOTAL = "REQUESTED_END_DATE_TOTAL";
    private static final String DIRECT_COST_TOTAL = "TOTAL_DIRECT_COST_TOTAL";
    private static final String INDIRECT_COST_TOTAL = "TOTAL_INDIRECT_COST_TOTAL";
    private DateUtils dateUtils = new DateUtils();
    private SimpleDateFormat simpleDateFormat;
    
    //Modified for case#2393 - Copying proposal title
    private static final String SIMPLE_DATE_FORMAT = "yyyy/MM/dd";
    
    private static final String DATE_SEPARATERS = "-:/.,|";
    
    //COEUSQA-1477 Dates in Search Results - Start
    private static final String DATE_FORMAT_DELIMITER = "/";
    private static final String DATE_FORMAT_USER_DELIMITER = "-"; 
    private static final String DATE_FORMAT_YEAR_DELIMITER = "y";
    private static final String DATE_FORMAT_MONTH_DELIMITER = "m";
    private static final String DATE_FORMAT_DATE_DELIMITER = "d";
    //COEUSQA-1477 Dates in Search Results - End
    
    /** Creates a new instance of InstituteProposalSearch */
    public InstituteProposalSearch() {
    }
    
    /** Creates a new instance of InstituteProposalSearch
     * @param parentFrame holds the mdiForm
     * @param searchReq holds the identifier
     * @param reqType the tabs to be displayed
     * @throws Exception an exception of the super class
     */
    public InstituteProposalSearch(Component parentFrame, String searchReq, int reqType)
    throws Exception {
        super(parentFrame, searchReq, reqType);
        //Added for case#2393 - Copying proposal title
        //COEUSQA-1477 Dates in Search Results - Start
        //String dateFormat = CoeusServerProperties.getProperty(CoeusPropertyKeys.SEARCH_DATE_FORMAT, SIMPLE_DATE_FORMAT);
        String dateFormat = SIMPLE_DATE_FORMAT;
        //COEUSQA-1477 Dates in Search Results - End
        simpleDateFormat = new SimpleDateFormat(dateFormat);
    }
    
    /**
     * Method used to create the component used to show as search result. Useful
     * for subclasses to override and display the extra details other than the display
     * list specified in the XML file, using the data in the results vector.
     * @param resultsTable table with the result data
     * @param results which contains the hashmaps for each result row.
     * @return pnlSearchResult the panel containing the scrollpane and details
     */
    protected JComponent buildSearchResultsPanel(JTable resultsTable, Vector results ) {
        JScrollPane scrPnTable = super.packTable(resultsTable);
        scrPnTable.setPreferredSize(new Dimension(350, 180));
        vecInstituteProposal = results;
        
        //Add the list selection listener to the table's selection model
        searchSelectionModel = resultsTable.getSelectionModel();
        searchSelectionModel.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        searchSelectionModel.removeListSelectionListener(this);
        searchSelectionModel.addListSelectionListener(this);
        
        pnlSearchResult.removeAll();
        pnlInstituteProposalDetails.resetData();
        if( resultsTable.getRowCount() > 0 ){
            tableEmpty = false;
            resultsTable.clearSelection();
            resultsTable.setRowSelectionInterval(0,0);
        }else{
            tableEmpty = true;
        }
        pnlSearchResult.add(scrPnTable, BorderLayout.CENTER);
        pnlSearchResult.add(pnlInstituteProposalDetails, BorderLayout.SOUTH);
        return pnlSearchResult;
    }
    
    /** This method sets the panel data based on the valueChanged of listSelectionEvent
     * @param listSelectionEvent takes the listSelectionEvent
     */
    public void valueChanged(javax.swing.event.ListSelectionEvent listSelectionEvent) {
        java.util.Date date;
        try{
            if( tableEmpty ) return ;            
            Object source = listSelectionEvent.getSource();
            int selectedRow = getSearchResTable().getSelectedRow();
            if( source.equals(searchSelectionModel) && selectedRow != -1 ){
//                HashMap hmResults = (HashMap)vecInstituteProposal.get(selectedRow);
                //Added for bug fix Start #1775 
                HashMap hmResults = getSelectedMap((String)getSearchResTable().getValueAt(selectedRow,0));
                //Bug fix end #1775
                InstituteProposalBean instituteProposalBean = new InstituteProposalBean();
                instituteProposalBean.setTitle(
                hmResults.get(TITLE) == null ? null :
                    (String)hmResults.get(TITLE));
                    
                    //Bug Fix: 1415 Start 1
                    Object startDate = hmResults.get(START_DATE_INITIAL);
                    
                    if(startDate != null){
                        String strDate = startDate.toString();
                        //COEUSQA-1477 Dates in Search Results - Start
                        //date = (java.util.Date)
                        //    simpleDateFormat.parse(dateUtils.restoreDate(strDate,DATE_SEPARATERS));
                        String dateValue = formatDateForSearchResults(startDate.toString());
                        date = (java.util.Date)simpleDateFormat.parse(dateValue);
                        //COEUSQA-1477 Dates in Search Results - End
                        instituteProposalBean.setRequestStartDateInitial(new java.sql.Date(date.getTime()));
                    }else{
                        instituteProposalBean.setRequestStartDateInitial(null);
                    }
                    
//                    instituteProposalBean.setRequestStartDateInitial(
//                    hmResults.get(START_DATE_INITIAL) == null ? null :
//                        new java.sql.Date(date.getTime()));
                    
                    
                        Object endingDate = hmResults.get(END_DATE_INITIAL);
                         if(endingDate != null){
                            String endDate = hmResults.get(END_DATE_INITIAL).toString();
                            //COEUSQA-1477 Dates in Search Results - Start
                            //date = (java.util.Date)
                            //    simpleDateFormat.parse(dateUtils.restoreDate(endDate,DATE_SEPARATERS)); 
                            String dateValue = formatDateForSearchResults(endDate.toString());
                            date = (java.util.Date)simpleDateFormat.parse(dateValue);
                            //COEUSQA-1477 Dates in Search Results - End
                            instituteProposalBean.setRequestEndDateInitial(
                            //bug fix Start #1775
                                //new java.sql.Date(date.getYear(),date.getMonth(),date.getDay()));
                            new java.sql.Date(date.getTime()));
                            //bug fix End #1775
                        }else{
                            instituteProposalBean.setRequestEndDateInitial(null);
                        }
                        //new java.sql.Date(date.getYear(),date.getMonth(),date.getDay());
//                        instituteProposalBean.setRequestEndDateInitial(
//                        hmResults.get(END_DATE_INITIAL) == null ? null :
//                            new java.sql.Date(date.getYear(),date.getMonth(),date.getDay()));
                        //Bug Fix: 1415 End 1
                        
                            instituteProposalBean.setTotalDirectCostInitial(
                            Double.parseDouble( hmResults.get(DIRECT_COST_INITIAL) == null ? ZERO :
                                hmResults.get(DIRECT_COST_INITIAL).toString()));
                                instituteProposalBean.setTotalInDirectCostInitial(
                                Double.parseDouble( hmResults.get(INDIRECT_COST_INITIAL) == null ?
                                ZERO : hmResults.get(INDIRECT_COST_INITIAL).toString()));
                                
                                //Bug Fix: 1415 Start 2
                                Object reqStartDate = hmResults.get(START_DATE_TOTAL);
                                if(reqStartDate != null){
                                    String requestStartDate = hmResults.get(START_DATE_TOTAL).toString();
                                    //COEUSQA-1477 Dates in Search Results - Start
                                    //date = (java.util.Date)simpleDateFormat.parse(dateUtils.restoreDate(requestStartDate,DATE_SEPARATERS)); 
                                    String dateValue = formatDateForSearchResults(requestStartDate.toString());
                                    date = (java.util.Date)simpleDateFormat.parse(dateValue); 
                                    //COEUSQA-1477 Dates in Search Results - End
                                    instituteProposalBean.setRequestStartDateTotal(new java.sql.Date(date.getTime()));
                                }else{
                                    instituteProposalBean.setRequestStartDateTotal(null);
                                }
                                
//                                instituteProposalBean.setRequestStartDateTotal(
//                                hmResults.get(START_DATE_TOTAL) == null ? null :
//                                    new java.sql.Date(date.getTime()));
                                    
                                    
                                Object reqEndDate = hmResults.get(END_DATE_TOTAL);
                                if(reqEndDate != null){
                                    String requestEndDate = hmResults.get(END_DATE_TOTAL).toString();
                                    //COEUSQA-1477 Dates in Search Results - Start
                                    //date = (java.util.Date)simpleDateFormat.parse(dateUtils.restoreDate(requestEndDate,DATE_SEPARATERS)); 
                                    String dateValue = formatDateForSearchResults(requestEndDate.toString());
                                    date = (java.util.Date)simpleDateFormat.parse(dateValue); 
                                    //COEUSQA-1477 Dates in Search Results - End
                                    instituteProposalBean.setRequestEndDateTotal(new java.sql.Date(date.getTime()));
                                }else{
                                    instituteProposalBean.setRequestEndDateTotal(null);
                                }
//                                    instituteProposalBean.setRequestEndDateTotal(
//                                    hmResults.get(END_DATE_TOTAL) == null ? null :
//                                        new java.sql.Date(date.getTime()));
                                //Bug Fix: 1415 End 2
                                        
                                        instituteProposalBean.setTotalDirectCostTotal(
                                        Double.parseDouble(hmResults.get(DIRECT_COST_TOTAL) == null ?
                                        ZERO : hmResults.get(DIRECT_COST_TOTAL).toString()));
                                        instituteProposalBean.setTotalInDirectCostTotal(
                                        Double.parseDouble(hmResults.get(INDIRECT_COST_TOTAL) == null ?
                                        ZERO : hmResults.get(INDIRECT_COST_TOTAL).toString()));
                                        
                                        pnlInstituteProposalDetails.setFormData(instituteProposalBean);
            }
        }catch(ParseException parseException){
            parseException.printStackTrace();
        }
        
    }
    //Added for bug fix Start #1775 
    private HashMap getSelectedMap(String proposalNumber) {
        HashMap selectedMap = null;
        for(int index=0; index<vecInstituteProposal.size();index++) {
            selectedMap = (HashMap)vecInstituteProposal.get(index);
            if(selectedMap.get("PROPOSAL_NUMBER").toString().equals(proposalNumber))
                break;
        }
        return selectedMap;
    }//bug fix End #1775
    
    //COEUSQA-1477 Dates in Search Results - Start
    /**
     * This method format the date which can be passed as a 
     * parameter to be Date() constructor.
     * @param value
     * @return returns dateValue which is formatted date value
     */
    public String formatDateForSearchResults(String value){
        String dateValue = "";
        String validDateFormat = "";
        String dateFormat = CoeusServerProperties.getProperty(CoeusPropertyKeys.SEARCH_DATE_FORMAT);
        if(dateFormat.length()>0){
            DateUtils dtUtils = new DateUtils();
            HashMap hmDateFormats = dtUtils.loadFormatsForSearchResults();
            if(hmDateFormats.get(dateFormat)!=null){
                validDateFormat = hmDateFormats.get(dateFormat).toString();
            }
            if(!(validDateFormat.length()>0)){
                //assign default date value
                dateFormat = CoeusConstants.ORACLE_DATE_YYYY_MM_DD_SLASH;
            }
        }else{
            dateFormat = CoeusConstants.ORACLE_DATE_YYYY_MM_DD_SLASH;
        }
        if(value!=null && value.length()>0){
            //to check if date contains user defined delimiter
            if(value.contains(DATE_FORMAT_USER_DELIMITER)){
                 //to replace the user defined delimiter to default date format
                 dateValue = value.replaceAll(DATE_FORMAT_USER_DELIMITER,DATE_FORMAT_DELIMITER);
            }else{
                dateValue = value;
            }
            if(dateValue.indexOf(DATE_FORMAT_DELIMITER)>0){
                String [] dateComponents = dateValue.split(DATE_FORMAT_DELIMITER);
                if((dateComponents[dateComponents.length-1]).length()<=4){
                    dateValue = fetchDateValuesForSearchResults(dateComponents, dateFormat);
                }else{
                    String date = (dateComponents[dateComponents.length-1]);
                    String patternValue = date.substring(0,date.indexOf(" "));
                    String time = date.substring((date.indexOf(patternValue)+patternValue.length()),date.length());
                    dateComponents[dateComponents.length-1] = patternValue;
                    date = fetchDateValuesForSearchResults(dateComponents, dateFormat);
                    dateValue = date+time;
                }
            }
        }
        return dateValue;
    }

    /**
     * This method format the date which can be passed as a 
     * parameter to be Date() constructor.
     * @param dateComponents
     * @param dateFormat
     * @return returns dateValue which is formatted date value
     */
    public String fetchDateValuesForSearchResults(String[] dateComponents, String dateFormat) {
        HashMap hmDateFormat = new HashMap(4);
        if(dateComponents!=null && dateFormat!=null){
            if(dateFormat.contains(DATE_FORMAT_USER_DELIMITER)){
                dateFormat = dateFormat.replaceAll(DATE_FORMAT_USER_DELIMITER,DATE_FORMAT_DELIMITER);
            }
            //to check whether the date contains "/"
            if(dateFormat.indexOf(DATE_FORMAT_DELIMITER)>0){
                String [] dateDefaultComponents = dateFormat.split(DATE_FORMAT_DELIMITER);
                Integer counter = new Integer(0);
                //to add to the collection object if data matches the repective delimiter
                for(String data:dateDefaultComponents){
                    //to remove "fm" from "Month"
                    if(data.contains("fm")){
                        data = data.replaceAll("fm","");
                    }
                    if(data.substring(0,1).equalsIgnoreCase(DATE_FORMAT_YEAR_DELIMITER)){
                        hmDateFormat.put("Year",counter);
                    }else if(data.substring(0,1).equalsIgnoreCase(DATE_FORMAT_MONTH_DELIMITER)){
                        hmDateFormat.put("Month",counter);
                    }else if(data.substring(0,1).equalsIgnoreCase(DATE_FORMAT_DATE_DELIMITER)){
                        hmDateFormat.put("Date",counter);
                    }
                    counter++;
                }
            }
        }
        //to format the date if it month is in Words
        Integer month=01;
        if(dateComponents[(Integer)hmDateFormat.get("Month")].length()>2){
            Enumeration monthNames =  DateUtils.getMonths().keys();
            while( monthNames.hasMoreElements() ){
                String monthName = (String) monthNames.nextElement();
                if( monthName.startsWith(dateComponents[(Integer)hmDateFormat.get("Month")]) ){
                    String monthNumber = (String) DateUtils.getMonths().get(monthName);
                    month = Integer.parseInt(monthNumber);
                    break;
                }
            }
        }else{
            month = (Integer)hmDateFormat.get("Month");
        }
        //formation of the date in the default date format
        String date = dateComponents[(Integer)hmDateFormat.get("Year")]+DATE_FORMAT_DELIMITER
                +month
                +DATE_FORMAT_DELIMITER+dateComponents[(Integer)hmDateFormat.get("Date")];
        return date;
    }
    //COEUSQA-1477 Dates in Search Results - End
}
