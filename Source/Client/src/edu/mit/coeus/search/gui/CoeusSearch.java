/*
 * @(#)CoeusSearch.java
 *
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 *
 * Created on August 22, 2002, 1:57 PM
 * @author  Geo Thomas
 * @version 1.0
 * @modified by Sagin
 * @date 25-10-02
 * Description : Java V1.3 compatibility, setLocationRelativeTo() method of JDialog
 *               will work only in V1.4. So replaced it with setLocation()
 *
 */

/* PMD check performed, and commented unused imports and variables on 24-MAY-2007
 * by Leena
 */
package edu.mit.coeus.search.gui;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.table.*;

import java.util.Hashtable;
import java.util.HashMap;
import java.util.Vector;
import java.util.StringTokenizer;

//import java.io.InputStream;
//import java.io.ObjectInputStream;
//import java.io.ObjectOutputStream;
//import java.io.IOException;

//import java.net.URLEncoder;
//import java.net.URL;
//import java.net.URLConnection;

import edu.mit.coeus.gui.CoeusFontFactory;
import edu.mit.coeus.gui.CoeusDlgWindow;
import edu.mit.coeus.utils.*;
import edu.mit.coeus.search.bean.CriteriaBean;
import edu.mit.coeus.search.bean.ColumnBean;
import edu.mit.coeus.search.bean.DisplayBean;
import edu.mit.coeus.search.bean.AttributeBean;
import edu.mit.coeus.brokers.ResponderBean;
import edu.mit.coeus.brokers.RequesterBean;
import edu.mit.coeus.search.bean.SearchInfoHolderBean;
import edu.mit.coeus.gui.CoeusMessageResources;

/**
 * The class is used to peform any search in the coeus application. The form will be
 * created based on the request identifier and request type. The search form will be
 * created based on the information which is extracted from the XML file that resides
 * at the server side. This is a wrapper class for the <code>Coeus generic Search.</code>
 * It will show the search dialog window according to the <code>request type.</code>
 * The following request types can be applied for the search.
 *<li> NO_TAB : Search window without any tab pages.
 *<li> TWO_TAB : Search window with two tab pages as search page and search result page
 *<li> TWO_TAB_MULTIPLE_SELECTION : Search window with two tab pages as search page
 * and search result page, which allows multiple selection of records.
 */

public class CoeusSearch implements ActionListener,java.io.Serializable{
    
    private final String CONNECTION_URL = CoeusGuiConstants.CONNECTION_URL;
    
    //Static variables for defining the type of the search
    public final static int NO_TAB = 0;
    public final static int TWO_TABS = 1;
    
    //holds the table selected Type with Number of Tabs Flag
    public final static int TWO_TABS_WITH_MULTIPLE_SELECTION = 3;
    
    //search request identifier
    private String searchReq;
    //request type
    private int reqType;
    //search panel
    private JPanel pnlSearch;
    //search result table
    private JTable tblSearchResult;
    //search result data row
    private HashMap resultData;
    //parent frame
    private Component parentFrame;
    //search display title
    private String searchDispLabel;
    //search window dialog
    private CoeusDlgWindow dlgSearchWindow;
    //main search tabbed pane
    private JTabbedPane tbdSearchPane;
    //class instance which holds search criteria details
    private SearchWindow searchWindow;
    //class instance which holds search result details
    private SearchResultWindow searchResWindow;
    //class instance which holds search information
    private SearchInfoHolderBean searchInfoHolder;
    //first value of the selected row
    private String selectedFirstColumnValue;
    //search result records, which holds the collection of hashtables
    private Vector resultRecords;
    //font for label
    private Font labelFont = CoeusFontFactory.getLabelFont();
    //normal font
//    private Font normalFont = CoeusFontFactory.getNormalFont();
    
    //holds the multiple selected rows as collection of HashMap elements in the
    //vector
    private Vector multipleSelectedRows = null;
    
    //made the Hashtable result as global to pass it to SaveAs dialog - Senthil AR
    private Hashtable result;
    
    // Added the saveAsDisplayLabels vector to get the column names if user pressed cancel in the
    // search window
    private Vector saveAsDisplayLablels = new Vector();
    
    //holds CoeusMessageResources instance used for reading message Properties.
    private CoeusMessageResources coeusMessageResources;
    //prps start
    JTable tblGlobalsearch ;
    //prps end
    
    private Vector customQueryData;//Added by Nadh for CoeusSearch enhancement(CustomQuery)   start : 3-aug-2005
    
    //COEUSQA:3005 - Dependency between the Location Name and Location Type in IACUC - Start
    private String searchLocTypeCode;
    //COEUSQA:3005 - End
    /**
     *  Default constructor
     */
    public CoeusSearch(){
    }
    /**
     *  Three argument constructor. It will intialize the class variables and
     *  create the empty result table as well.
     *  @param parent frame
     *  @param request identifier
     *  @param request type
     */
    public CoeusSearch(Component parentFrame,String searchReq,int reqType) throws Exception{
        this.parentFrame = parentFrame;
        this.searchReq = searchReq;
        this.reqType = reqType;
        buildEmptyResTable();
        coeusMessageResources = CoeusMessageResources.getInstance();
    }
    
    //COEUSQA:3005 - Dependency between the Location Name and Location Type in IACUC - Start
    /**
     *  Four argument constructor. It will intialize the class variables and
     *  create the empty result table as well.
     *  @param parent frame
     *  @param request identifier
     *  @param request locationTypeCode
     *  @param request type
     */
    public CoeusSearch(Component parentFrame,String searchReq, String locTypeCode, int reqType) throws Exception{
        this.parentFrame = parentFrame;
        this.searchReq = searchReq;
        this.searchLocTypeCode = locTypeCode;
        this.reqType = reqType;
        buildEmptyResTable();
        coeusMessageResources = CoeusMessageResources.getInstance();
    }
    //COEUSQA:3005 - End
    
    //Added by Nadh for CoeusSearch enhancement(CustomQuery)   start : 3-aug-2005
    private String customQuery = null;
    public void getSearchQueryResult(String customQuery) throws Exception{
        this.customQuery = customQuery;
        result =  getResult(null);
        this.customQuery = null;
        if(result==null){
            throw new Exception(coeusMessageResources.parseMessageKey(
                    "searchResultWin_exceptionCode.1110"));
        }
        this.resultRecords = (Vector)result.get("reslist");
        this.searchResWindow = new SearchResultWindow(result,this);
        this.tblSearchResult = searchResWindow.getSearchResTable();
    }//end 3-aug-2005
    
    /**
     *  Method to show the search dialog window above the parent frame.
     */
    public void showSearchWindow() throws Exception{
        if(!newConnection){
            this.searchInfoHolder = fetchSearchInfoHolder();
        }
        this.searchDispLabel = searchInfoHolder.getDisplayLabel();
        newConnection = false;
        tbdSearchPane = new CoeusTabbedPane(CoeusTabbedPane.CTRL_T);
        tbdSearchPane.add(new JPanel());
        tbdSearchPane.add(new JPanel());
        tbdSearchPane.setFont(labelFont);
        tbdSearchPane.addChangeListener(new ChangeListener(){
            public void stateChanged(ChangeEvent event){
                switch(tbdSearchPane.getSelectedIndex()){
                    case(0):
                        btnOK.setEnabled(false);
                        btnFind.setEnabled(true);
                        btnClear.setEnabled(true);
                        btnFind.setEnabled(true);
                        pnlRight.revalidate();
                        break;
                    case(1):
                        btnOK.setEnabled(true);
                        btnCancel.setEnabled(true);
                        btnClear.setEnabled(false);
                        btnFind.setEnabled(false);
                        pnlRight.revalidate();
                        break;
                }
            }
        });
        /*Check the instance of the parent frame and creating instance of
            CoeusDlgWindow accordingly
         */
        if(parentFrame instanceof JDialog){
            dlgSearchWindow = new CoeusDlgWindow(((JDialog)parentFrame),true);
        }else if(parentFrame instanceof JFrame){
            dlgSearchWindow = new CoeusDlgWindow(((JFrame)parentFrame),true);
        }
        String exMsg = "";
        try{
            JPanel pnlSearch = getSearchPanel();
            dlgSearchWindow.getContentPane().add(pnlSearch);
            dlgSearchWindow.setResizable(false);
        }catch(Exception ex){
            //ex.printStackTrace();
            exMsg = ex.getMessage();
            CoeusOptionPane.showWarningDialog(exMsg);
        }
        dlgSearchWindow.setTitle(searchDispLabel);
        dlgSearchWindow.getRootPane().setDefaultButton(btnOK);
        dlgSearchWindow.pack();
        //set the location at the middle of the screen
        //dlgSearchWindow.setLocationRelativeTo(null);
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        Dimension dlgSize = dlgSearchWindow.getSize();
        dlgSearchWindow.setLocation(screenSize.width/2 - (dlgSize.width/2),
                screenSize.height/2 - (dlgSize.height/2));
        /* begin: fixID: 192 */
        /* added on 03-MAR-2003 to provide ESC key functionality for dialog window */
        dlgSearchWindow.addKeyListener(new KeyAdapter(){
            public void keyReleased(KeyEvent e){
                if(e.getKeyCode() == KeyEvent.VK_ESCAPE){
                    if(!CoeusOptionPane.isPropagating()){
                        dlgSearchWindow.dispose();
                    }else{
                        CoeusOptionPane.setPropagating(false);
                    }
                }
            }
        });
        dlgSearchWindow.addWindowListener(new WindowAdapter(){
            public void windowOpened(WindowEvent we ) {
//                btnCancel.requestFocus();
                dlgSearchWindow.setVisible(false); //added to resolve java 7 issue: unable to enter values into the dlgSearchWindow search table
                searchWindow.getCriteriaTable().requestFocusInWindow();
                searchWindow.getCriteriaTable().editCellAt(0,0);
                searchWindow.getCriteriaTable().getEditorComponent().requestFocusInWindow();
                dlgSearchWindow.setVisible(true);//added to resolve java 7 issue: unable to enter values into the dlgSearchWindow search table
            }
        });
//added to resolve java 7 issue: unable to enter values into the dlgSearchWindow search table -- Start
        dlgSearchWindow.addWindowFocusListener(new WindowAdapter() {
            public void windowGainedFocus(WindowEvent e) {
                searchWindow.getCriteriaTable().requestFocusInWindow();
                searchWindow.getCriteriaTable().editCellAt(0,0);
                searchWindow.getCriteriaTable().getEditorComponent().requestFocusInWindow();
            }
        });
//added to resolve java 7 issue: unable to enter values into the dlgSearchWindow search table -- end
        /* end : fixId: 192 */
        //prps start
//       try
//       {
//           tblGlobalsearch.requestFocus() ;
//       }catch(Exception ex)
//       {
//        ex.printStackTrace() ;
//       }
        //prps end
        java.awt.Component[] compo = {tblGlobalsearch,btnOK,btnCancel,btnFind,btnClear};//
        ScreenFocusTraversalPolicy traversalPolicy = new ScreenFocusTraversalPolicy(compo);
        pnlSearch.setFocusTraversalPolicy(traversalPolicy );
        pnlSearch.setFocusCycleRoot(true);
        
        dlgSearchWindow.show();
    }
    
    /**
     *  Method to set the search result table. This method is exposed to
     *  only classes which is in the same package.
     *  @param table to set the search result
     */
    void setSearchResTable(JTable tblSearchRes){
        this.tblSearchResult = tblSearchRes;
    }
    /**
     *  Method to get the search result table
     * @return search result table
     */
    public JTable getSearchResTable(){
        return tblSearchResult;
    }
    /**
     *  Method to get the empty result table.
     *  @return empty result table
     */
    public JTable getEmptyResTable(){
        return emptyResTable;
    }
    
    /**
     *  Method to get the selected row
     *  @return get the selected row in the form of a <code>hashmap</code>, in which
     *  the name specified in the xml file will be the key and extracted result will be
     *  the value.
     */
    public HashMap getSelectedRow(){
        return resultData;
    }
    
    /**
     * Updated For : REF ID 149 Feb' 14 2003
     * Person Search allows for multiple entries, however,
     * the user can only add 1 at a time
     *
     * Updated by Subramanya Feb' 17 2003
     */
    
    /**
     *  Method to get the selected rows as vector ( used for multiple selection )
     * @return Vector collection of slected row where in each element Hashmap.
     * @throws Excpetion
     */
    public Vector getMultipleSelectedRows() throws Exception{
        return multipleSelectedRows;
    }
    /**
     *  Method to get all search result records
     * @return search result, <code>collection</code> of <code>HashMap</code>
     */
    public Vector getResultRecords(){
        return resultRecords;
    }
    
    /**
     *  Method to get the search result selected value
     * @return first column value from the selected row
     */
    public String getSelectedValue(){
        return selectedFirstColumnValue;
    }
    
    /**
     *  Method to get the search result window
     * @return Search result window
     */
    public SearchResultWindow getResultWindow(){
        return searchResWindow;
    }
    /*
     * Method used to get the search panel
     * @return search panel
     */
    
    /* modified by ravi on 14-03-2003 : made the method available to subclasses ,
       so that it can be overriden. */
    protected JPanel getSearchPanel() throws Exception{
        searchWindow = new SearchWindow(this,searchInfoHolder);
        JTable tempTable = searchWindow.getCriteriaTable();
        //prps start
        tblGlobalsearch = tempTable ;
        //prps end
        pnlSearch = buildMainPanel(tempTable);
        return pnlSearch;
    }
    
    /**
     * This method returns the SearchInfoHolderBean got from server to the subclasses
     *
     * @return reference of SearchInfoHolderBean.
     */
    protected SearchInfoHolderBean getSearchInfoHolder(){
        return searchInfoHolder;
    }
    
    /** Added this method to get the SearchInfoHolderBean
     */
    public SearchInfoHolderBean getSearchInfoHolderBean(){
        return searchInfoHolder;
    }
    
    /**
     * Method used to get search result data as hashtable. It will construct
     * a hsahtable by taking the data entered in the search grid. Hashtable contains
     * two entries.
     * <li> A <code>HashMap</code> with the ref string as "result",
     * which holds the result as a collection, in which column name as key and result value as value.
     * <li> A <code>Hashtable</code> of display list with the ref id as "displaylist"
     * @return result, hashtable of having displaylsit and result
     * @param criteria table
     */
    private Hashtable getResult(JTable tblCrt) throws Exception{
        result = null;
//        InputStream in=null;
        Vector criteriaList = searchWindow.getSearchInfoHolder().getCriteriaList();
        
        //extract the data from the table and keep it in a vector 'columnList'
        Vector columnList = new Vector(3,2);
        boolean noSearchCriteriaEntered = true;
        if(tblCrt != null) {//Modified by Nadh for CoeusSearch enhancement(CustomQuery)   start : 3-aug-2005
            DefaultCellEditor editor = (DefaultCellEditor)tblCrt.getCellEditor();
            if (editor != null){
                editor.stopCellEditing();
            }
            int colCount = tblCrt.getColumnCount();
            int tableRowCount = tblCrt.getRowCount();
            
            //        Vector criteriaList = searchWindow.getSearchInfoHolder().getCriteriaList();
            //
            //        //extract the data from the table and keep it in a vector 'columnList'
            //        Vector columnList = new Vector(3,2);
            //        boolean noSearchCriteriaEntered = true;
            for(int j=0;j<colCount;j++){
                CriteriaBean criteria = (CriteriaBean)criteriaList.get(j);
                String criteriaName = criteria.getName().trim();
                ColumnBean column = new ColumnBean(criteriaName);
                for(int i=0;i<tableRowCount;i++){
                    Object fieldObject = tblCrt.getValueAt(i,j);
                    String fieldValue = null;
                    if(fieldObject instanceof ComboBoxBean){
                        fieldValue = ((ComboBoxBean)fieldObject).getCode();
                    }else{
                        fieldValue = (String)fieldObject;
                    }
                    //fieldValue = (String)tblCrt.getValueAt(i,j);
                    if(fieldValue!=null && !fieldValue.trim().equals("")){
                        
                    /* to provide wild card searching using "*". Replace all
                     * occurances of * with "%" symbol by prefixing "LIKE" to the fieldValue
                     */
                        // Case# 2615:Searching for names containing test string 'like' with wildcards- Start
//                        if( fieldValue.trim().indexOf('*') != -1 ){
//                            String tempVal = "LIKE ";
//                            fieldValue = fieldValue.replace('*','%');
//                            if( fieldValue.toUpperCase().indexOf("LIKE") == -1 ){
//                                fieldValue = tempVal + fieldValue;
//                            }
//                            
//                        }
                        String ucaseFieldValue = fieldValue.trim().toUpperCase();
                        if( ucaseFieldValue.indexOf('*') != -1 ){
                            String tempVal = "LIKE ";
                            fieldValue = fieldValue.replace('*','%');
                            // Append LIKE in front of the fieldValue only if fieldValue doesnt start with LIKE.
                            if( !ucaseFieldValue.startsWith("LIKE ") ){
                                fieldValue = tempVal + fieldValue;
                            }
                            
                        }
                       // Case# 2615:Searching for names containing test string 'like' with wildcards- End 
                        AttributeBean attribute = new AttributeBean((""+i),fieldValue);
                        if(attribute!=null){
                            /* begin : fixID: 192 */
                        /* modified on 04-MAR-2003 to check whether the entered value
                          in criteria field is valid for a particular datatype or not.*/
                            String operatorValue = attribute.getValueType();
                            String attributeValue = attribute.getAttValue();
                            boolean isLike = false;
                            if(attributeValue != null ){
                                attributeValue = attributeValue.trim().toUpperCase();
                                if( operatorValue != null
                                        && operatorValue.toUpperCase().trim().indexOf("LIKE") != -1){
                                    /* valid LIKE condition, trim % symbols from the value */
                                    isLike = true;
                                    attributeValue = ignorePercentSymbol(attributeValue);
                                }
                                if(attributeValue.length() > 0 ) {
                                    if( criteria.isNumber() ){
                                        try{
                                            /* try to convert the entered value to number */
                                            Integer.parseInt(attributeValue);
                                        }catch (NumberFormatException nfe){
                                            /* if unable to parse the value as number, throw exception*/
                                            throw new Exception("Invalid "+
                                                    criteria.getFieldBean().getLabel() +
                                                    " in row :" +(i+1));
                                        }
                                    }else if( criteria.isDate() &&
                                            !operatorValue.equalsIgnoreCase("IS NULL") &&
                                            //Added for search between dates enhancement
                                            !operatorValue.trim().equalsIgnoreCase("BETWEEN")) {
                                        /* criteria type for the column is date*/
                                        /* try to convert the entered value to dd-MMM-yyyy format */
                                        DateUtils dtUtils = new DateUtils();
                                        String formattedDate = dtUtils.formatDate(attributeValue,"/-:,","dd-MMM-yyyy");
                                        boolean invalidDate = false;
                                        if(formattedDate == null){
                                            /* not a valid date */
                                            invalidDate = true;
                                            if( isLike ){
                                                /* entered value is part of LIKE */
                                                isLike = false;
                                                try{
                                                    /* check whether entered value is number (i.e day or year)*/
                                                    Integer.parseInt(attributeValue);
                                                    invalidDate = false;
                                                }catch (NumberFormatException nfe){
                                                    /* entered value is not a valid number */
                                                    /* check whether it is an abbrevation of any of the month */
                                                    String allMonthAbbrs = "JAN/FEB/MAR/APR/MAY/JUN/JUL/AUG/SEP/OCT/NOV/DEC";
                                                    StringTokenizer monthTokenizer = new StringTokenizer(allMonthAbbrs,"/");
                                                    String monthToken = "";
                                                    while(monthTokenizer.hasMoreTokens()){
                                                        monthToken = monthTokenizer.nextToken();
                                                        if(monthToken.indexOf(attributeValue) != -1){
                                                            /* entered value is an abbrevation of one of the months*/
                                                            invalidDate = false;
                                                            break;
                                                        }
                                                    }
                                                }
                                            }
                                            if(invalidDate){
                                            /* entered value is not a valid date or number
                                             or an abbrevation of any month, so throw
                                             exception */
                                                throw new Exception("Invalid "+
                                                        criteria.getFieldBean().getLabel() +
                                                        " in row :" +(i+1));
                                            }
                                        }
                                    }
                                }
                            }
                            /* end : fixID: 192 */
                            column.addAttribute(attribute);
                            noSearchCriteriaEntered = false;
                        }
                    }
                }
                columnList.addElement(column);
            }
        } else if(customQuery != null && !customQuery.equals("")) {//Added by Nadh for CoeusSearch enhancement(CustomQuery)   start : 3-aug-2005
            Vector vecQueryList = searchInfoHolder.getCustomQueryList();
            edu.mit.coeus.search.bean.CustomQueryBean customQueryBean = (edu.mit.coeus.search.bean.CustomQueryBean)vecQueryList.get(Integer.parseInt(customQuery));
            customQuery = customQueryBean.getQuery();
            noSearchCriteriaEntered = false;
        }//End 3-aug-2005
        
        if(noSearchCriteriaEntered){
            throw new Exception(coeusMessageResources.parseMessageKey(
                    "search_exceptionCode.1108"));
        }
        //Added with case 2726 :Apply Row Level Security on Search Results Screen
        //Set User Id to COEUS parameter.
        String newRemClause = Utils.replaceString(
                                searchInfoHolder.getRemClause().toString(),"COEUS",CoeusGuiConstants.getMDIForm().getUserName().toUpperCase().trim());
        //COEUSQA:3005 - Dependency between the Location Name and Location Type in IACUC - Start
        if(searchLocTypeCode != null) {
            newRemClause = Utils.replaceString(
                    searchInfoHolder.getRemClause().toString(),"LOCATIONTYPECODE", searchLocTypeCode);
        }
        //COEUSQA:3005 - End
       searchInfoHolder.setRemClause(newRemClause);
       //2726 End
        RequesterBean requester = new RequesterBean();
        // modified by ravi for removing session maintenance
        Vector objects = new Vector();
        objects.addElement(searchInfoHolder);
        objects.addElement(columnList);
        objects.addElement(customQuery);//Added by Nadh for CoeusSearch enhancement(CustomQuery)   start : 3-aug-2005
        requester.setDataObjects(objects);
        // prepare url for search result servlet
        String SEARCH_CONNECTION_URL = CONNECTION_URL+"/coeusSearchResultServlet";
        AppletServletCommunicator comm = new AppletServletCommunicator(
                SEARCH_CONNECTION_URL, requester);
        dlgSearchWindow.setCursor(new Cursor(java.awt.Cursor.WAIT_CURSOR));
        /* updated by ravi on 18-02-03 for ignoring mouse clicks
           when the system is busy with some operation */
        dlgSearchWindow.getGlassPane().setVisible(true);
        comm.send();
        ResponderBean response = comm.getResponse();
        dlgSearchWindow.setCursor(new Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        dlgSearchWindow.getGlassPane().setVisible(false);
        if(!response.isSuccessfulResponse()){
            throw new Exception(response.getMessage());
        }
        result = (Hashtable)response.getDataObject();
        
        return result;
    }
    /**
     *  Method to perform click action on the button in any key stroke
     *  @param Keycode
     */
    
    public void fireAction(int keyCode){
        switch(keyCode){
            case(KeyEvent.VK_ENTER):
//                System.out.println("firing Enter action");
                btnFind.doClick();
//                System.out.println("resetting focus to table");
//                tblGlobalsearch.requestFocusInWindow();
                break;
/*            case(KeyEvent.VK_ESCAPE):
                btnCancel.doClick();
                break;*/
        }
    }
    
    /* added on 04-MAR-2003 to fix the bug id: 192 */
    /**
     * Method used to remove all the percentage symbols in the given value.
     *
     */
    private String ignorePercentSymbol(String value){
        StringBuffer trimmedString = new StringBuffer();
        if(value != null){
            char character = ' ';
            for( int charIndex=0; charIndex < value.length(); charIndex++ ){
                character = value.charAt(charIndex);
                if( character != '%' ){
                    trimmedString.append(character);
                }
            }
        }
        return trimmedString.toString().trim();
    }
    /* end of fix ID: 192 */
    
    
    /**
     *  Overridden method for the action listener.
     */
    public void actionPerformed(java.awt.event.ActionEvent actionEvent) {
        try{
            Object source = actionEvent.getSource();
            if(source instanceof javax.swing.JButton){
                if(((JButton)source).getName().equals("SearchWindowFind")){
                    customQuery = null;
                    JTable tblCrt = searchWindow.getCriteriaTable();
                    DefaultCellEditor editor =(DefaultCellEditor)tblCrt.getCellEditor();
                    if (editor != null){
                        editor.stopCellEditing();
                    }
                    result =  getResult(tblCrt);
                    if(result==null){
                        throw new Exception(coeusMessageResources.parseMessageKey(
                                "searchResultWin_exceptionCode.1110"));
                    }
                    this.resultRecords = (Vector)result.get("reslist");
                    this.searchResWindow = new SearchResultWindow(result,this);
                    this.tblSearchResult = searchResWindow.getSearchResTable();
                    switch(reqType){
                        case(NO_TAB):
                            dlgSearchWindow.dispose();
                            break;
                        case(TWO_TABS):
                            TableColumn column
                                    = tblSearchResult.getColumnModel().getColumn(
                                    tblSearchResult.getColumnCount()-1);
                            column.setPreferredWidth(0);
                            column.setMaxWidth(0);
                            column.setMinWidth(0);
                            tbdSearchPane.setComponentAt(1,buildSearchResultsPanel(tblSearchResult,(Vector)result.get("reslist")));
                            tbdSearchPane.setSelectedIndex(1);
                            tblSearchResult.requestFocusInWindow();
                            
                            btnOK.setEnabled(true);
                            btnClear.setEnabled(false);
                            btnFind.setEnabled(false);
                            pnlRight.revalidate();
                            
                            setDefaultFocusForComponent();
                            break;
                            /**
                             * Updated For : REF ID 149 Feb' 14 2003
                             * Person Search allows for multiple entries, however,
                             * the user can only add 1 at a time
                             *
                             * Updated by Subramanya Feb' 17 2003
                             * This condition will handle CoeusSearch Window with Two Tabs and Multiple
                             * selection provided for the result window.
                             */
                        case(TWO_TABS_WITH_MULTIPLE_SELECTION):
                            TableColumn clm
                                    = tblSearchResult.getColumnModel().getColumn(
                                    tblSearchResult.getColumnCount()-1);
                            clm.setPreferredWidth(0);
                            clm.setMaxWidth(0);
                            clm.setMinWidth(0);
                            tbdSearchPane.setComponentAt(1,buildSearchResultsPanel(tblSearchResult,(Vector)result.get("reslist")));
                            tbdSearchPane.setSelectedIndex(1);
                            tblSearchResult.setSelectionMode( ListSelectionModel.MULTIPLE_INTERVAL_SELECTION );
                            btnOK.setEnabled(true);
                            btnClear.setEnabled(false);
                            btnFind.setEnabled(false);
                            pnlRight.revalidate();
                            setDefaultFocusForComponent();
                            //btnFind.disable();
                            //btnClear.disable();
                            //for the option to place checkbox
                            break;
                    }
                }else if(((JButton)source).getName().equals("SearchWindowClear")){
                    //modified by Vyjayanthi to clear the contents of the table in the editing mode
                    int editRow = tblGlobalsearch.getEditingRow();
                    int editCol = tblGlobalsearch.getEditingColumn();
                    if( editRow!= -1 && editCol != -1 ) {
                        tblGlobalsearch.getCellEditor(editRow,editCol).stopCellEditing();
                    }
                    searchWindow.clearAll();
                }else if(((JButton)source).getName().equals("SearchWindowCancel")){
                    saveAsDisplayLablels = new Vector();
                    buildEmptyResTable();
                    //to clear the previous selected data as well
                    this.resultData = null;
                    if (result == null){
                        result = new Hashtable();
                    }
                    result.put("displaylabels",saveAsDisplayLablels);
                    dlgSearchWindow.dispose();
                }else if(((JButton)source).getName().equals("SearchWindowOK")){
                    if(searchResWindow==null || searchResWindow.getSelectedRow()==null
                            || searchResWindow.getSelectedRow().isEmpty()){
                        throw new Exception(coeusMessageResources.parseMessageKey(
                                "search_exceptionCode.1119"));
                    }
                    this.resultData = searchResWindow.getSelectedRow();
                    this.selectedFirstColumnValue = searchResWindow.getSelectedValue();
                    this.multipleSelectedRows = searchResWindow.getMultipleSelectedRows();
                    dlgSearchWindow.dispose();
                }
            }
        }catch(Exception ex){
            //ex.printStackTrace();
            // btnClear.requestFocusInWindow();
            CoeusOptionPane.showInfoDialog(ex.getMessage());
            //tblGlobalsearch.requestFocusInWindow();
        }
//        finally{
//            System.out.println("resetting focus to table");
//            tblGlobalsearch.requestFocusInWindow();
//        }
    }
    /*
     *  Method to pack the table in scroll panel
     */
    protected JScrollPane packTable(JTable tblSearch){
        JScrollPane scrlPnTable = new JScrollPane();
        scrlPnTable.setMinimumSize(new Dimension(22, 15));
        scrlPnTable.setPreferredSize(new Dimension(600, 280));
        scrlPnTable.setViewportView(tblSearch);
        scrlPnTable.setForeground(java.awt.Color.white);
        scrlPnTable.getViewport().setBackground(java.awt.Color.white);
        
        return scrlPnTable;
    }
    /*
     *  Method to fetch the search info holder from the server
     */
    // Modified for COEUSDEV-847 : Inbox not working properly both resolved and unresolved tabs - start
//    private SearchInfoHolderBean fetchSearchInfoHolder() throws Exception{
      public SearchInfoHolderBean fetchSearchInfoHolder() throws Exception{
//        ObjectInputStream inputFromServlet = null;
//        boolean isValidationSuccess=true;
//        JPanel pnlSearch = null;
        /*try {
            String webServerStr = CONNECTION_URL+"/coeusSearchServlet";
         
            // prepare the servlet url
            StringBuffer servletGetReq = new StringBuffer(webServerStr);
            servletGetReq.append("?");
            servletGetReq.append(URLEncoder.encode("searchreq"));
            servletGetReq.append("=");
            servletGetReq.append(URLEncoder.encode(searchReq));
            // connect to the servlet
            URL coeusSearchURL = new URL(servletGetReq.toString());
         
            URLConnection servletConnection = coeusSearchURL.openConnection();
            servletConnection.setDoInput(true);
            servletConnection.setDoOutput(true);
            servletConnection.setUseCaches(false);
         
            // Read the input from the servlet.
            // The servlet will return a message to be displayed on the applet
            inputFromServlet = new ObjectInputStream(servletConnection.getInputStream());
            ResponderBean responder = (ResponderBean)inputFromServlet.readObject();
            if(!responder.isSuccessfulResponse()){
                throw new Exception(responder.getMessage());
            }
            searchInfoHolder = (SearchInfoHolderBean)responder.getDataObject();
        }finally {
            if (inputFromServlet != null) {
                inputFromServlet.close();
            }
        }
         */
        RequesterBean requester = new RequesterBean();
        requester.setDataObject(searchReq);
        String SEARCH_RESULT_CONNECTION_URL = CONNECTION_URL+"/coeusSearchServlet";
        AppletServletCommunicator comm = new AppletServletCommunicator(
                SEARCH_RESULT_CONNECTION_URL, requester);
        comm.send();
        ResponderBean response = comm.getResponse();
        if(!response.isSuccessfulResponse()){
            throw new Exception(response.getMessage());
        }
        searchInfoHolder = (SearchInfoHolderBean)response.getDataObject();
        return searchInfoHolder;
    }
    private JTable emptyResTable;
    private boolean newConnection;
    /*
     *  Method to build the empty table
     */
    private JTable buildEmptyResTable() throws Exception{
        if(emptyResTable==null){
            this.searchInfoHolder = fetchSearchInfoHolder();
            setCustomQueryData(this.searchInfoHolder.getCustomQueryList());//Added by Nadh for CoeusSearch enhancement(CustomQuery)   start : 3-aug-2005
            newConnection = true;
        }
        Vector displayList = searchInfoHolder.getDisplayList();
        int disCnt = displayList.size();
        Vector displayLabels = new Vector(3,2);
        int[] columnLengths = new int[disCnt];
        int tmpIndex = 0;
        for(int disIndex=0;disIndex<disCnt;disIndex++){
            DisplayBean display = (DisplayBean)displayList.elementAt(disIndex);
            //Commented on 24-05-2007 for bug fix in Person Search 
            //Was not able to view the details of a person after adding to the empty base search window
            //To include the hidden fields also in the model while creating the model,
            //if(display.isVisible()){
            displayLabels.addElement(display.getValue());
            columnLengths[tmpIndex++] = display.getSize();
            //}//Commented on 24-05-2007 for bug fix in Person Search
            saveAsDisplayLablels.addElement(display);
        }
//        displayLabels.addElement("index");
        emptyResTable = new JTable( new DefaultTableModel(displayLabels,0){
            public boolean isCellEditable(int row,int col){
                return false;
            }
        });
        int colIndex = 0;
        for(; colIndex<displayLabels.size()-1;colIndex++){
            //Modified on 24-05-2007 for bug fix in Person Search - start
            DisplayBean display = (DisplayBean)displayList.elementAt(colIndex);
            TableColumn column = emptyResTable.getColumnModel().getColumn(colIndex);
            //if field is to be not visible, the column size is set to 0
            if(display.isVisible()){
                column.setPreferredWidth(columnLengths[colIndex]);
            }else{
                column.setHeaderValue(" ");
                column.setMaxWidth(0);
                column.setMinWidth(0);
                column.setPreferredWidth(0);
//                column.setWidth(0);
            }
            //Modified on 24-05-2007 for bug fix in Person Search - end
        }
        
        emptyResTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        TableColumn column = emptyResTable.getColumnModel().getColumn(
                emptyResTable.getColumnCount()-1);
        column.setPreferredWidth(0);
        column.setMaxWidth(0);
        column.setMinWidth(0);
        javax.swing.table.JTableHeader header
                = emptyResTable.getTableHeader();
        header.setFont(labelFont);
        //header.setResizingAllowed(false);
        header.setReorderingAllowed(false);
        //emptyResTable.setCellSelectionEnabled(true);
        emptyResTable.setRowSelectionAllowed(true);
        return emptyResTable;
    }
    /*
     *  Method to build the main panel
     */
    protected JPanel buildMainPanel(JTable tblSearch){
        final JPanel pnlMain = new JPanel();
        JPanel pnlLeft = new JPanel();
        
        pnlMain.setLayout(new BorderLayout());
/*      pnlMain.setLayout(new GridBagLayout());
        //setLayout(new GridBagLayout());
        GridBagConstraints gridBagConstraints1;
 
        gridBagConstraints1 = new GridBagConstraints();
        gridBagConstraints1.gridx = 1;
        gridBagConstraints1.gridy = 0;
        gridBagConstraints1.anchor = GridBagConstraints.NORTH;
        //add(pnlRight, gridBagConstraints1);
 */
        pnlLeft.setLayout(new BorderLayout());
        switch(reqType){
            case(NO_TAB):
                pnlLeft.add(packTable(tblSearch),BorderLayout.CENTER);
                break;
            case(TWO_TABS):
                tbdSearchPane.setComponentAt(0,packTable(tblSearch));
                tbdSearchPane.setComponentAt(1,buildSearchResultsPanel(emptyResTable,null));
                tbdSearchPane.setTitleAt(0,searchDispLabel);
                tbdSearchPane.setTitleAt(1,(searchDispLabel+" Result"));
                tbdSearchPane.setSelectedIndex(0);
                
                tbdSearchPane.addChangeListener(new ChangeListener(){
                    public void stateChanged(ChangeEvent ce){
                        JTabbedPane pn = (JTabbedPane)ce.getSource();
                        int selectedTab = pn.getSelectedIndex();
                        
                        try {
                            switch ( selectedTab ) {
                                case 0 :
                                    java.awt.Component[] compo = {tblGlobalsearch,btnOK,btnCancel,btnFind,btnClear};
                                    ScreenFocusTraversalPolicy traversalPolicy = new ScreenFocusTraversalPolicy(compo);
                                    pnlMain.setFocusTraversalPolicy(traversalPolicy );
                                    pnlMain.setFocusCycleRoot(true);
                                    
                                    setDefaultFocusForComponent();
                                    break;
                                case 1 :
                                    if(tblSearchResult == null) {
                                        break;
                                    }
                                    Component[] compo1 = {tblSearchResult,btnOK,btnCancel,btnFind,btnClear};
                                    ScreenFocusTraversalPolicy traversalPolicy1 = new ScreenFocusTraversalPolicy(compo1);
                                    pnlMain.setFocusTraversalPolicy(traversalPolicy1 );
                                    pnlMain.setFocusCycleRoot(true);
                                    
                                    setDefaultFocusForComponent();
                                    break;
                            }
                        }catch(Exception e) {
                            tbdSearchPane.setSelectedIndex(0);
                            CoeusOptionPane.showErrorDialog(e.getMessage());
                        }
                    }
                });
                
                pnlLeft.add(tbdSearchPane);
                break;
                /**
                 * Updated For : REF ID 149 Feb' 14 2003
                 * Person Search allows for multiple entries, however,
                 * the user can only add 1 at a time
                 *
                 * Updated by Subramanya Feb' 17 2003
                 * This condition will handle CoeusSearch Window with Two Tabs and Multiple
                 * selection provided for the result window.
                 */
            case(TWO_TABS_WITH_MULTIPLE_SELECTION):
                tbdSearchPane.setComponentAt(0,packTable(tblSearch));
                tbdSearchPane.setComponentAt(1,buildSearchResultsPanel(emptyResTable,null));
                tbdSearchPane.setTitleAt(0,searchDispLabel);
                tbdSearchPane.setTitleAt(1,(searchDispLabel+" Result"));
                tbdSearchPane.setSelectedIndex(0);
                tblSearch.setSelectionMode( ListSelectionModel.MULTIPLE_INTERVAL_SELECTION );
                pnlLeft.add(tbdSearchPane);
                break;
        }
        
        /*pnlMain.add(buildButtonPanel(), gridBagConstraints1);
         
        gridBagConstraints1 = new GridBagConstraints();
        gridBagConstraints1.gridx = 0;
        gridBagConstraints1.gridy = 0;
         
        pnlMain.add(pnlLeft, gridBagConstraints1);*/
        pnlMain.add(pnlLeft, BorderLayout.CENTER);
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(buildButtonPanel());
        pnlMain.add(buttonPanel,BorderLayout.EAST);
        return pnlMain;
    }
    
    private JButton btnOK;
    private JButton btnCancel;
    private JButton btnFind;
    private JButton btnClear;
    private JPanel pnlRight;
    
    /*
     *  Method to build button panel
     */
    private JPanel buildButtonPanel(){
        pnlRight = new JPanel();
        btnOK = new JButton();
        btnCancel = new JButton();
        btnFind = new JButton();
        btnClear = new JButton();
        GridBagConstraints gridBagConstraints2;
        pnlRight.setLayout(new GridBagLayout());
        
        btnOK.setText("OK");
        btnOK.setMnemonic('O');
        btnOK.setName("SearchWindowOK");
        btnOK.setFont(labelFont);
        btnOK.addActionListener(this);
        btnOK.setPreferredSize(new Dimension(80,25));
        
        btnCancel.setText("Cancel");
        btnCancel.setName("SearchWindowCancel");
        btnCancel.setMnemonic('C');
        btnCancel.setFont(labelFont);
        btnCancel.addActionListener(this);
        btnCancel.setPreferredSize(new Dimension(80,25));
        
        btnFind.setText("Find");
        btnFind.setMnemonic('F');
        btnFind.setName("SearchWindowFind");
        btnFind.setFont(labelFont);
        btnFind.addActionListener(this);
        btnFind.setPreferredSize(new Dimension(80,25));
        
        btnClear.setText("Clear");
        btnClear.setMnemonic('l');
        btnClear.setName("SearchWindowClear");
        btnClear.setFont(labelFont);
        btnClear.addActionListener(this);
        btnClear.setPreferredSize(new Dimension(80,25));
        
        switch(reqType){
            //create only one tab if the request type is NO_TAB
            case(NO_TAB):
                gridBagConstraints2 = new GridBagConstraints();
                gridBagConstraints2.insets = new Insets(5, 1, 2, 0);
                pnlRight.add(btnFind, gridBagConstraints2);
                
                gridBagConstraints2 = new GridBagConstraints();
                gridBagConstraints2.gridx = 0;
                gridBagConstraints2.gridy = 1;
                gridBagConstraints2.insets = new Insets(2, 1, 8, 0);
                pnlRight.add(btnClear, gridBagConstraints2);
                
                gridBagConstraints2 = new GridBagConstraints();
                gridBagConstraints2.gridx = 0;
                gridBagConstraints2.gridy = 2;
                gridBagConstraints2.insets = new Insets(9, 1, 9, 0);
                pnlRight.add(btnCancel, gridBagConstraints2);
                
                break;
                //create two tab pane one tab if the request type is TWO_TABs &
                // TWO_TABS with Multiple Row Selection.
            case(TWO_TABS):
            case( TWO_TABS_WITH_MULTIPLE_SELECTION ):
                gridBagConstraints2 = new GridBagConstraints();
                gridBagConstraints2.gridx = 0;
                gridBagConstraints2.gridy = 0;
                gridBagConstraints2.insets = new Insets(20, 1, 2, 0);
                pnlRight.add(btnOK, gridBagConstraints2);
                
                gridBagConstraints2 = new GridBagConstraints();
                gridBagConstraints2.gridx = 0;
                gridBagConstraints2.gridy = 1;
                gridBagConstraints2.insets = new Insets(2, 1, 8, 0);
                pnlRight.add(btnCancel, gridBagConstraints2);
                
                gridBagConstraints2 = new GridBagConstraints();
                gridBagConstraints2.insets = new Insets(8, 1, 2, 0);
                gridBagConstraints2.gridx = 0;
                gridBagConstraints2.gridy = 2;
                pnlRight.add(btnFind, gridBagConstraints2);
                
                gridBagConstraints2 = new GridBagConstraints();
                gridBagConstraints2.gridx = 0;
                gridBagConstraints2.gridy = 3;
                gridBagConstraints2.insets = new Insets(2, 1, 8, 0);
                pnlRight.add(btnClear, gridBagConstraints2);
                if(tbdSearchPane!=null){
                    switch(tbdSearchPane.getSelectedIndex()){
                        case(0):
                            btnOK.setEnabled(false);
                            break;
                        case(1):
                            btnFind.setEnabled(false);
                            btnClear.setEnabled(false);
                            break;
                    }
                }
                break;
        }
        btnFind.setSelected(true);
        btnFind.setFocusPainted(true);
        btnOK.setEnabled(false);
        
        return pnlRight;
    }
    
    /**
     * Method used to create the component used to show as search result. Useful
     * for subclasses to override and display the extra details other than the display
     * list specified in the XML file, using the data in the results vector.
     * @param JTable table with the result data
     * @param Vector which contains the hashtables for each result row.
     */
    protected JComponent buildSearchResultsPanel(JTable resultsTable, Vector results ) {
        return packTable(resultsTable);
    }
    
    /**
     * This will return the search result
     */
    public Hashtable getSearchResults() {
        return result;
    }
    
    //Added by Amit 11/24/2003
    /** This method use to implement focus on first editable component in this page.
     */
    public void setDefaultFocusForComponent(){
        
        if((tblSearchResult != null ) && (tblSearchResult.getRowCount() > 0)) {
            //Bug Fix : 1022 - START
            EventQueue.invokeLater(new Runnable() {
                public void run() {
                    tblSearchResult.requestFocusInWindow();
                }
            });
            //Bug Fix : 1022 - END
            
            tblSearchResult.setRowSelectionAllowed(true);
            tblSearchResult.requestFocusInWindow();
            tblSearchResult.setRowSelectionInterval(0,0);
            tblSearchResult.setColumnSelectionInterval(0,0);
            
        }
    }
    //end Amit
    
    public void sortByColumns(JTable sourceTable,Vector columns) {
        if(searchResWindow != null){
            searchResWindow.sortByColumns(sourceTable,columns);
        }
    }
    
    //Added by Nadh for CoeusSearch enhancement(CustomQuery)   start : 3-aug-2005
    /**
     * Getter for property customQueryData.
     * @return Value of property customQueryData.
     */
    public java.util.Vector getCustomQueryData() {
        return customQueryData;
    }
    
    private void setCustomQueryData(Vector vecData) {
        if(vecData !=null && vecData.size()>0) {
            customQueryData = new Vector();
            for(int i=0;i<vecData.size();i++) {
                edu.mit.coeus.search.bean.CustomQueryBean customQueryBean = (edu.mit.coeus.search.bean.CustomQueryBean) vecData.get(i);
                customQueryData.addElement(customQueryBean.getDisplayName());
            }
        }
    }//end 3-aug-2005
    //Modified for the case # COEUSQA-2073 - Improve management of Coeus Inbox - Ability to reorder columns in Inbox - start  
    /** To get the Resolved and Unresolved messages throught the coeusSearch.xml
     * @param userId and searchName
     * @return  list of resovled and unresolved messages 
     */
    public Hashtable getInboxDetailsFromSearch(String userId, String searchName) throws Exception{
        
          Vector dataObjects = null;   
        RequesterBean requester = new RequesterBean();
        this.searchReq = searchName;
        this.searchInfoHolder = fetchSearchInfoHolder();
        String newRemClause = Utils.replaceString(
                searchInfoHolder.getRemClause().toString(),"COEUS",CoeusGuiConstants.getMDIForm().getUserName().toUpperCase().trim());
        searchInfoHolder.setRemClause(newRemClause);
        searchInfoHolder.setCriteriaList(new Vector());
        Vector objects = new Vector();
        objects.addElement(searchInfoHolder);
        objects.addElement(new Vector());
        objects.addElement(null);//Added by Nadh for CoeusSearch enhancement(CustomQuery)   start : 3-aug-2005
        requester.setDataObjects(objects);
        // prepare url for search result servlet
        String SEARCH_CONNECTION_URL = CONNECTION_URL+"/coeusSearchResultServlet";
        AppletServletCommunicator comm = new AppletServletCommunicator(
                SEARCH_CONNECTION_URL, requester);
               comm.send();
        ResponderBean response = comm.getResponse();
        if(!response.isSuccessfulResponse()){
            if(!response.getMessage().equals("No rows found with the current selection criteria")){
                throw new Exception(response.getMessage());
            }
        }
        result = (Hashtable)response.getDataObject();
        
        return result;
    }
    //Modified for the case # COEUSQA-2073 - Improve management of Coeus Inbox - Ability to reorder columns in Inbox - end
}