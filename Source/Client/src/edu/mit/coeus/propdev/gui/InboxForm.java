/*
 * InboxUnresolvedForm.java
 *
 * Created on December 24, 2003, 11:28 AM
 */
/* PMD check performed, and commented unused imports and variables on 12-JULY-2010
 * by keerthyjayaraj
 */
package edu.mit.coeus.propdev.gui;

//java imports
import edu.mit.coeus.award.bean.AwardBean;
import edu.mit.coeus.award.controller.AwardBaseWindowController;
import edu.mit.coeus.bean.AuthorizationBean;
import edu.mit.coeus.exception.CoeusException;
import edu.mit.coeus.search.bean.DisplayBean;
import edu.mit.coeus.subcontract.bean.SubContractAmountReleased;
import edu.mit.coeus.user.gui.UserDelegationForm;
import java.text.ParseException;
import javax.swing.table.*;
import java.util.*;
import javax.swing.*;
import java.awt.*;
import javax.swing.event.*;
import java.awt.event.*;
import java.text.SimpleDateFormat;

//coeus imports
import edu.mit.coeus.propdev.bean.InboxBean;
import edu.mit.coeus.propdev.bean.MessageBean;
import edu.mit.coeus.brokers.RequesterBean;
import edu.mit.coeus.brokers.ResponderBean;
import edu.mit.coeus.exception.CoeusClientException;
import edu.mit.coeus.utils.*;
import edu.mit.coeus.gui.*;
import edu.mit.coeus.subcontract.bean.SubContractBean;
import edu.mit.coeus.subcontract.controller.SubcontractBaseWindowController;
import edu.mit.coeus.utils.query.AuthorizationOperator;


/**
 *
 * @author  raghusv
 *
 */
public class InboxForm extends javax.swing.JComponent implements
ListSelectionListener,ActionListener,ModuleConstants{
    
    /** Creates new form InboxUnresolvedForm */
    
//    private Vector inboxVector;
    private DateUtils dtUtils = new DateUtils();
    private static final String REQUIRED_DATEFORMAT = "dd-MMM-yyyy";
    private static final String REQUIRED_DATEFORMAT1 = "MM/dd/yyyy";
    private static final String TIME_STAMP = "MM/dd/yyyy:";
    private static final String DATE_FORMAT = TIME_STAMP +"hh:mm:ss";
    private SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT);
    private InboxBean inbox;
    private MessageBean messageBean;
    //COEUSQA-2755: Problem with Color Coding in Inbox 
   // private java.sql.Timestamp todayDate;
    private java.util.Date todayDate;
    //   private GregorianCalendar calendarTodayDate;
    private boolean rowRemoved=false;
    private TableSorter sorter;
    private int prevRow=0;
    private Vector newInboxVector= new Vector();
    private CoeusAppletMDIForm mdiForm = new CoeusAppletMDIForm();
    private ProposalDetailForm mainProposal;
    //Added for Case#3682 - Enhancements related to Delegations - Start
    private UserDelegationForm userDelegationForm;
//    private String delegationNo = "";
    //Added for Case#3682 - Enhancements related to Delegations - End
    private String proposalNumber;
    //Added for case#4293- Click to launch protocol from unresolved message in Inbox in premium -start
//    private String protocolNumber;
    //Added for case#4293- Click to launch protocol from unresolved message in Inbox in premium -end
    // Modified for the case # COEUSQA-2073 - Improve management of Coeus Inbox - Ability to reorder columns in Inbox - start
//    private Vector vecDataPopulate = new Vector();
    // Modified for the case # COEUSQA-2073 - Improve management of Coeus Inbox - Ability to reorder columns in Inbox - end
    //private String subject
    private CoeusMessageResources coeusMessageResources;
    private boolean unresolved ;
    //Stores the RIGHT ID which is required for checking user rights to view proposal
    private final String VIEW_RIGHT="VIEW_PROPOSAL";
    private static final String MODIFY_PROPOSAL_RIGHT = "MODIFY_PROPOSAL";
    
    
//    private final String MODIFY_RIGHT="MODIFY_PROPOSAL";
    //Added by shiji for Right Checking - step 1 : start
    private final String VIEW_ANY_PROPOSAL_RIGHT="VIEW_ANY_PROPOSAL";
    
    //Added for bug id 1856 step 1 : start
    private final String MODIFY_NARRATIVE_RIGHT="MODIFY_NARRATIVE";
    
    private final String MODIFY_BUDGET_RIGHT="MODIFY_BUDGET";
    //bug id 1856 step 1 : end
    
    private final String MODIFY_ANY_PROPOSAL_RIGHT="MODIFY_ANY_PROPOSAL";
    //Right Checking - step 1 : end
    
    // code added for princeton enhancement case#2802    
    private static final String SUBCONTRACT_SERVLET = "/SubcontractMaintenenceServlet";    
    InboxTableRenderer inboxRenderer;
    
    //Addded for COEUSDEV-299 : double click is no longer working to open protocol from inbox  
    private static final String AUTH_SERVLET = "/AuthorizationServlet";    
    //COEUSDEV-299 : End
    // Modified for the case # COEUSQA-2073 - Improve management of Coeus Inbox - Ability to reorder columns in Inbox - start
    private Vector inboxDataLabels;
    private Vector inboxDataVector;
    private int colCount;
    private String PROPOSAL_ACTION_SERVLET="/ProposalActionServlet";
    private static final char GET_MESSAGE_FOR_INBOX = 'g';
    // Modified for the case # COEUSQA-2073 - Improve management of Coeus Inbox - Ability to reorder columns in Inbox - end
    
    
    
    /**
     * @param InboxVector
     * @param mdiForm
     */    
    // Modified for the case # COEUSQA-2073 - Improve management of Coeus Inbox - Ability to reorder columns in Inbox - start
    //public InboxForm(Vector InboxVector,CoeusAppletMDIForm mdiForm) {
    public InboxForm(Vector inboxVector,Vector inboxLabels, CoeusAppletMDIForm mdiForm) throws Exception {
        this.inboxDataLabels = inboxLabels;
        this.inboxDataVector = inboxVector;
        // Modified for the case # COEUSQA-2073 - Improve management of Coeus Inbox - Ability to reorder columns in Inbox - end
        initComponents();
        this.mdiForm = mdiForm;
        // Modified for the case # COEUSQA-2073 - Improve management of Coeus Inbox - Ability to reorder columns in Inbox - start
//        this.inboxVector=InboxVector;
        //Added for the case# COEUSQA-2529-Premium In-box: user cannot double-click to open Dev Proposal Record-start
        initialSetup(inboxDataVector);
        //Added for the case# COEUSQA-2529-Premium In-box: user cannot double-click to open Dev Proposal Record-end
        addListeners();
        coeusMessageResources= CoeusMessageResources.getInstance();
        //COEUSQA-2755: Problem with Color Coding in Inbox 
        todayDate = CoeusUtils.getDBTimeStamp();
        SimpleDateFormat todateFormat = new SimpleDateFormat("yyyy-MM-dd");
        todayDate = todateFormat.parse(todayDate.toString());
        //calendarTodayDate=new GregorianCalendar();
        //calendarTodayDate.setTimeInMillis(todayDate.getTime());
        //inboxRenderer=new InboxTableRenderer(1);
        txtArMessage.setDisabledTextColor(Color.BLACK);
        txtArMessage.setWrapStyleWord(true);
        txtArMessage.setLineWrap(true);
        //Added for the case# COEUSQA-2529-Premium In-box: user cannot double-click to open Dev Proposal Record-start
        //initialSetup(inboxVector);
        //Added for the case# COEUSQA-2529-Premium In-box: user cannot double-click to open Dev Proposal Record-end
    }

    /** This method is used in constructor as well as in the InboxDetail form
     * In InboxDetailForm used to setup all the data newly whenever refresh button is clicked.
     * @param inboxVector
     */    
    public void initialSetup(Vector inboxVector) throws Exception{
        // Modified for the case # COEUSQA-2073 - Improve management of Coeus Inbox - Ability to reorder columns in Inbox - start
        //setFormData(inboxVector);
        //setFormData(inboxVector,inboxLables);
        //newInboxVector=inboxVector;
        Vector newInboxVectorTmp=updateInboxBean(inboxVector);
        if(newInboxVectorTmp!= null && newInboxVectorTmp.size()>0){
            newInboxVector = (Vector)newInboxVectorTmp.get(0);
            // Added for COEUSDEV-847 : Inbox not working properly both resolved and unresolved tabs - Start
            inboxDataVector = inboxVector;
            // Added for COEUSDEV-847 : Inbox not working properly both resolved and unresolved tabs - End
        }
        //System.out.println("New Inbox Vector size is "+newInboxVector.size() );
        // Moved the code to setTableDetails for COEUSDEV-612:Refresh and UI issues in inbox window in Premium
        setTableDetails(inboxVector);
            
    }
    
    // Added for COEUSDEV-612:Refresh and UI issues in inbox window in Premium
    /**
     * Method to set the table details.
     * @param inboxVector - The search result vector.
     */
    private void setTableDetails(Vector inboxVector){
        buildTable(inboxVector);
        // Modified for the case # COEUSQA-2073 - Improve management of Coeus Inbox - Ability to reorder columns in Inbox - end
        if(tblInbox.getRowCount()>0){
            tblInbox.getSelectionModel().setSelectionMode(
                                    ListSelectionModel.MULTIPLE_INTERVAL_SELECTION );
            // Modified for the case # COEUSQA-2073 - Improve management of Coeus Inbox - Ability to reorder columns in Inbox - start
           // setTableEditor();
            //setTableEditor(inboxLables);
            // Modified for the case # COEUSQA-2073 - Improve management of Coeus Inbox - Ability to reorder columns in Inbox - end
            setUrgencyColor();
            if(tblInbox.getRowCount()>0){
                // Modified for the case # COEUSQA-2073 - Improve management of Coeus Inbox - Ability to reorder columns in Inbox - start  
                javax.swing.table.TableColumn clmName
                    = tblInbox.getColumnModel().getColumn(
                        tblInbox.getColumnCount()-1);
                clmName.setPreferredWidth(0);
                clmName.setMaxWidth(0);
                clmName.setMinWidth(0);
                clmName.setWidth(0);
                javax.swing.table.JTableHeader header
                    = tblInbox.getTableHeader();
                header.setReorderingAllowed(false);
                //header.addMouseListener(new CustomMouseAdapter());
                scrPnUnresolvedDetail.setViewportView(tblInbox);
                //tblInbox.addMouseListener(new CustomMouseAdapter());
                sorter.addMouseListenerToHeaderInTable(tblInbox);
                int selectedRow=0;
                tblInbox.addRowSelectionInterval(0,0);
                
                
                //Modified by shiji for Coeus enhancement Case #1828 : step 1 : start
                //String numValue=(String) tblInbox.getValueAt(selectedRow, 12);
                String numValue=(String) tblInbox.getValueAt(selectedRow, 24);
                // Modified for the case # COEUSQA-2073 - Improve management of Coeus Inbox - Ability to reorder columns in Inbox - end
                //Coeus enhancement Case #1828 : step 1 : end
                Integer indexObject=new Integer(numValue);
                int index=indexObject.intValue();
                inbox = (InboxBean) newInboxVector.get( index );
                messageBean=(MessageBean) inbox.getMessageBean();
                if( messageBean != null ){
                    String messg = messageBean.getMessage();
                    if( messg!=null){
                        //Added for princeton enhancement case#2802 - starts
                        //To filter the sequence number and line number present in the message
                        //in subcontract module
                        if(inbox.getModuleCode() == 4){
                            messg = messg.substring(messg.indexOf("|")+1);
                            messg = messg.substring(messg.indexOf("|")+1).trim();
                        }
                        //Added for princeton enhancement case#2802 - ends
                        txtArMessage.setText(messg);
                        txtArMessage.setCaretPosition(0);
                    }
                }else{
                    txtArMessage.setText("");
                }
            }
            
        }
            
    }
    
    
    
    /**
     *Implementation to set the colors of rows depending on the deadline date to today's date
     *red color rows means 2 or less days,for yellow it's 3 or 4 days,green is 5 to 10 days,white more than 11 days 
     */
    private void setUrgencyColor(){
        TableColumn column = new TableColumn();
        //Modified by shiji for Coeus enhancement Case #1828 : step 2 : start
        // Modified for the case # COEUSQA-2073 - Improve management of Coeus Inbox - Ability to reorder columns in Inbox - start
        //for(int clm=0;clm<12;clm++){
        for(int clm=0;clm<inboxDataLabels.size();clm++){
        // Modified for the case # COEUSQA-2073 - Improve management of Coeus Inbox - Ability to reorder columns in Inbox - end    
        // Coeus enhancement Case #1828 : step 2 : end
            column = tblInbox.getColumnModel().getColumn(clm);
            column.setCellRenderer( new InboxTableRenderer());
        }
    }
   
    
    /**
     *method to set the columns and other properties of the table
     */
    // Modified for the case # COEUSQA-2073 - Improve management of Coeus Inbox - Ability to reorder columns in Inbox - start
    // The datas are set through buldTable method, in order to get visible and non-visible columns
    //private void setTableEditor(){
//    private void setTableEditor(Vector inboxLables){
//        // Modified for the case # COEUSQA-2073 - Improve management of Coeus Inbox - Ability to reorder columns in Inbox - end
//        JTableHeader header = tblInbox.getTableHeader();
//        header.setReorderingAllowed(false);
//        tblInbox.setRowHeight(22);
//        tblInbox.getTableHeader().
//        setFont(CoeusFontFactory.getLabelFont());
//        TableColumn clmName;
//        if( sorter == null ) {
//                sorter = new TableSorter((DefaultTableModel)tblInbox.getModel(),false);
//               // sorter = new TableSorter((new DefaultTableModel(disLabelList,0)),false);
//                // Modified for the case # COEUSQA-2073 - Improve management of Coeus Inbox - Ability to reorder columns in Inbox - end
//                tblInbox.setModel(sorter);
//                sorter.addMouseListenerToHeaderInTable(tblInbox);
//        }
//        
//        tblInbox.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        // Modified for the case # COEUSQA-2073 - Improve management of Coeus Inbox - Ability to reorder columns in Inbox - start
//        TableColumn clmName = tblInbox.getColumn("From ");
//        clmName.setMinWidth(90);
//        
//        clmName = tblInbox.getColumn("Date Sent");
//        clmName.setMinWidth(120);
//        
//        clmName = tblInbox.getColumn("Subject");
//        clmName.setMinWidth(90);
//        
//        //Added by shiji for Coeus enhancement Case #1828 : step 3 : start
//        clmName = tblInbox.getColumn("Module");
//        clmName.setMinWidth(120);
//        
//        clmName = tblInbox.getColumn("Item");
//        clmName.setMinWidth(90);
//        //Coeus enhancement Case #1828 : step 3 : end
//        clmName = tblInbox.getColumn("Deadline");
//        clmName.setMinWidth(90);
//        
//        clmName = tblInbox.getColumn("Status");
//        clmName.setMinWidth(120);
//        
//        clmName = tblInbox.getColumn("Sponsor Name");
//        clmName.setMinWidth(130);
//        
//        clmName = tblInbox.getColumn("Lead Unit");
//        clmName.setMinWidth(90);
//        
//        clmName = tblInbox.getColumn("Lead Unit Name");
//        clmName.setMinWidth(110);
//        
//        clmName = tblInbox.getColumn("PI");
//        clmName.setMinWidth(120);
//        
//        clmName = tblInbox.getColumn("Title");
//        clmName.setMinWidth(200);
//        
//        clmName = tblInbox.getColumn("Index");
//        clmName.setMinWidth(0);
//        clmName.setMaxWidth(0);
//        clmName.setPreferredWidth(0);
        // Modified for the case # COEUSQA-2073 - Improve management of Coeus Inbox - Ability to reorder columns in Inbox - end
//    }
    
    /** When a vector of beans is passed to this method it sets the table data
     * @param InboxVector
     */
    // Modified for the case # COEUSQA-2073 - Improve management of Coeus Inbox - Ability to reorder columns in Inbox - start
    // The datas are set through buldTable method, in order to get visible and non-visible columns
//    public void setFormData(Vector InboxVector){
//        vecDataPopulate = new Vector();
//        Vector vecData = null;
//        InboxBean inboxBean=new InboxBean();
//        //emptyTable();
//        if((InboxVector != null) &&
//        (InboxVector.size() > 0)){
//            int InboxSize = InboxVector.size();
//            for(int index = 0; index < InboxSize; index++){
//                inboxBean = (InboxBean)InboxVector.get(index);
//                //Modified for the case# COEUSQA-2073- - Improve management of Coeus Inbox - Ability to reorder columns in Inbox - start
//                String moduleItem = inboxBean.getItem();
//
//                int moduleCode = inboxBean.getModuleCode();
//
//                String moduleDesc =  inboxBean.getModule();
//
//                String toUser = inboxBean.getToUser();
//
//                String messageId = inboxBean.getMessageId();
//
//                String fromUser =  inboxBean.getFromUser();
//
//                GregorianCalendar arrivalDate = new GregorianCalendar();
//                arrivalDate.setTimeInMillis( inboxBean.getArrivalDate().getTime() );
//                String halfDate=inboxBean.getArrivalDate().toString();
//                halfDate = dtUtils.formatDate(halfDate,REQUIRED_DATEFORMAT1);
//                String date = convertTimeToString(arrivalDate,halfDate);
//
//                java.sql.Timestamp beginDate = inboxBean.getArrivalDate();
//
//                java.util.Date dateConverted = (java.util.Date)beginDate;
//
//                char sub=inboxBean.getSubjectType();
//
//                String subjectDescription = inboxBean.getSubjectDescription();
//
//                char openedFlag = inboxBean.getOpenedFlag();
//
//                String updateTimeStemp=null;
//                if(inboxBean.getUpdateTimeStamp()!=null){
//                    updateTimeStemp=inboxBean.getUpdateTimeStamp().toString();
//                }
//
//                String upateUser = inboxBean.getUpdateUser();
//
//                String message = inboxBean.getMessageBean().getMessage();
//
//                String fromUserName = inboxBean.getFromUser();
//
//                String deadline=null;
//                 if(inboxBean.getProposalDeadLineDate()!=null){
//                     deadline=inboxBean.getProposalDeadLineDate().toString();
//                }
//
//                String title = inboxBean.getProposalTitle();
//
//                String sponsorCode = inboxBean.getSponsorCode();
//
//                String sponsorName = inboxBean.getSponsorName();
//
//                String sysDate=null;
//                 if(inboxBean.getSysDate()!=null){
//                     sysDate=inboxBean.getSysDate().toString();
//                }
//
//                String unitNumber = inboxBean.getUnitNumber();
//
//                String unitName = inboxBean.getUnitName();
//
//                String piName = inboxBean.getPersonName();
//
//                int statusCode = inboxBean.getCreationStatus();
//
//                String statusDescription = inboxBean.getCreationStatusDescription();
//
////                String userName=inboxBean.getUserName();
////                GregorianCalendar arrivalDate = new GregorianCalendar();
////                arrivalDate.setTimeInMillis( inboxBean.getArrivalDate().getTime() );
////
////                String halfDate=inboxBean.getArrivalDate().toString();
////                halfDate = dtUtils.formatDate(halfDate,REQUIRED_DATEFORMAT1);
////                String date = convertTimeToString(arrivalDate,halfDate);
////
////                java.sql.Timestamp beginDate = inboxBean.getArrivalDate();
////
////                java.util.Date dateConverted = (java.util.Date)beginDate;
////
////
////                char sub=inboxBean.getSubjectType();
////                String subject=null;
////                if(sub=='N'){
////                    subject="Notification";
////                }else if(sub=='A'){
////                    subject="Approval";
////                }else if(sub=='R'){
////                    subject="Rejected";
////                }
////                //Added for Case#3682 - Enhancements related to Delegations - Start
////                if(sub == 'D'){
////                    subject = "Delegation";
////                }
////                //Added for Case#3682 - Enhancements related to Delegations - End
////
////                String proposal=null;
////                if(inboxBean.getProposalNumber()!=null){
////                    proposal=inboxBean.getProposalNumber();
////                }
////
////                String deadline=null;
////                if(inboxBean.getProposalDeadLineDate()!=null){
////                    deadline=inboxBean.getProposalDeadLineDate().toString();
////                }
////
////                String status = inboxBean.getCreationStatusDescription();
////                String sponsorName=inboxBean.getSponsorName();
////                String leadUnitNo=inboxBean.getUnitNumber();
////                String leadUnitName=inboxBean.getUnitName();
////                String PI=inboxBean.getPersonName();
////                String title=inboxBean.getProposalTitle();
////                //Added by shiji for Coeus enhancement Case #1828 : step 4 : start
////                String module= inboxBean.getModule();
////                String item=inboxBean.getProposalNumber();
//                //Coeus enhancement Case #1828 : step 4 : end
//                vecData= new Vector();
//
//                vecData.addElement(moduleItem == null ?"":moduleItem);
//
//                vecData.addElement(moduleCode == 0 ?"":moduleCode);
//
//                vecData.addElement(moduleDesc == null ?"":moduleDesc);
//
//                vecData.addElement(toUser == null ?"":toUser);
//
//                vecData.addElement(messageId == null ?"":messageId);
//
//                vecData.addElement(fromUser == null ?"":fromUser);
//
//                vecData.addElement(fromUserName == null ?"":fromUserName);
//
//                vecData.addElement(dateConverted == null ?"":dateConverted);
//
//                vecData.addElement(sub);
//
//                vecData.addElement(subjectDescription == null ?"":subjectDescription);
//
//                vecData.addElement(openedFlag);
//
//                vecData.addElement(updateTimeStemp == null ?"":updateTimeStemp);
//
//                vecData.addElement(upateUser == null ?"":upateUser);
//
//                vecData.addElement(message == null ?"" :message);
//
//                vecData.addElement(deadline == null ?"":deadline);
//
//                vecData.addElement(title == null ?"":title);
//
//                vecData.addElement(sponsorCode == null ?"":sponsorCode);
//
//                vecData.addElement(sponsorName == null ?"":sponsorName);
//
//                vecData.addElement(sysDate == null ?"":sysDate);
//
//                vecData.addElement(unitNumber == null ?"":unitNumber);
//
//                vecData.addElement(unitName == null ?"":unitName);
//
//                vecData.addElement(piName == null ?"":piName);
//
//                vecData.addElement(statusCode == 0 ?"":statusCode);
//
//                vecData.addElement(statusDescription == null ?"":statusDescription);
//
////    //Modified for the case# COEUSQA-2073- - Improve management of Coeus Inbox - Ability to reorder columns in Inbox - end
////                vecData.addElement(userName == null ?inboxBean.getFromUser():userName);
////                // Commented by chandra - Bug fix 1082.
////                // Add Date as a filed to the component insted of string
//////                vecData.addElement(date == null ? "" : date);
////                vecData.addElement(dateConverted==null ? null :dateConverted);
//////                vecData.addElement(subject== null? "" : subject);
////                //Added by shiji for Coeus enhancement Case #1828 : step 5 : start
////                //vecData.addElement(proposal == null ? "" : proposal);
////                vecData.addElement(module == null ? "" : module);
////                vecData.addElement(item == null ? "" : item);
////                //Coeus enhancement Case #1828 : step 5 : end
////                vecData.addElement(deadline == null ? "" : deadline);
////                vecData.addElement(status ==null? "" : status);
////                vecData.addElement(sponsorName == null ? "" : sponsorName);
////                vecData.addElement(leadUnitNo == null ? "" : leadUnitNo);
////                vecData.addElement(leadUnitName == null ? "" : leadUnitName);
////                vecData.addElement(PI==null? "" : PI);
////                vecData.addElement(title == null ? "" : title);
//                Integer indexVal=new Integer(index);
//                String indexValue= indexVal.toString();
//                vecData.addElement(indexValue);
//                 //Modified for the case# COEUSQA-2073- - Improve management of Coeus Inbox - Ability to reorder columns in Inbox - end
//                rowRemoved = true;
//                vecDataPopulate.addElement(vecData);
//            }
//            // Modified for the case # COEUSQA-2073 - Improve management of Coeus Inbox - Ability to reorder columns in Inbox - start
//            ((DefaultTableModel)tblInbox.getModel()).setDataVector(vecDataPopulate,getColumnNames());
//            ((DefaultTableModel)tblInbox.getModel()).fireTableDataChanged();
//        }else{
//            ((DefaultTableModel)tblInbox.getModel()).setDataVector(
//            new Object[][]{},getColumnNames().toArray());
//            ((DefaultTableModel)tblInbox.getModel()).fireTableDataChanged();
//            setTableEditor(inboxLables);
//            // Modified for the case # COEUSQA-2073 - Improve management of Coeus Inbox - Ability to reorder columns in Inbox - end
//        }
//    }
    
// Modified for the case # COEUSQA-2073 - Improve management of Coeus Inbox - Ability to reorder columns in Inbox - end    
//    private Vector getColumnNames(){
//        Enumeration enumColNames = tblInbox.getColumnModel().getColumns();
//        Vector vecColNames = new Vector();
//        while(enumColNames.hasMoreElements()){
//            String strName = (String)((TableColumn)
//            enumColNames.nextElement()).getHeaderValue();
//            vecColNames.addElement(strName);
//        }
//        return vecColNames;
//    }
    
    
    
    /** Whenever moving between rows of the table this method is called and
     * implementation is done so that it takes up the current row bean and accesses the
     * message bean to get the message and displays it in message text area box
     * @param e
     */
    public void valueChanged(ListSelectionEvent e) {
        if(tblInbox.getRowCount()>0 ){
            int selectedRow=tblInbox.getSelectedRow();
            if(selectedRow!=-1 && prevRow != selectedRow && !rowRemoved ){
                //Modified by shiji for Coeus enhancement Case #1828 : step 6 : start
                // Modified for the case # COEUSQA-2073 - Improve management of Coeus Inbox - Ability to reorder columns in Inbox - start
                // Get the Index value form last column
                //String numValue=(String) tblInbox.getValueAt(selectedRow, 12);
                String numValue=(String) tblInbox.getValueAt(selectedRow, 24);
                // Modified for the case # COEUSQA-2073 - Improve management of Coeus Inbox - Ability to reorder columns in Inbox - start
                //Coeus enhancement Case #1828 : step 6 :end
                Integer indexObject=new Integer(numValue);
                int index=indexObject.intValue();
                inbox = (InboxBean) newInboxVector.get( index );
                
                messageBean=(MessageBean) inbox.getMessageBean();
                if( messageBean != null ){
                    String messg = messageBean.getMessage();
                    if( messg!=null){
                        //Added for princeton enhancement case#2802 - starts
                        //To filter the sequence number and line number present in the message
                        //in subcontract module
                        if(inbox.getModuleCode() == 4){
                            messg = messg.substring(messg.indexOf("|")+1);
                            messg = messg.substring(messg.indexOf("|")+1).trim();
                        }
                        //Added for princeton enhancement case#2802 - ends
                        txtArMessage.setText(messg);
                        txtArMessage.setCaretPosition(0);
                    }
                }else{
                    txtArMessage.setText("");
                }
            }
            prevRow=selectedRow;
            rowRemoved = false;
        }
    }
    
   /* public InboxBean rowDelete(){
        int rowCount =tblUnresolvedDetail.getRowCount();
        if(rowCount >0){
            int selRow=tblUnresolvedDetail.getSelectedRow();
            System.out.println("THE SELECTED ROW IS  "+selRow);
            InboxBean inbxBean=new InboxBean();
            inbxBean=(InboxBean) inboxVector.get(selRow);
            int row = sorter.getIndexForRow( selRow);
            sorter.removeRow( row );
            //((DefaultTableModel)tblUnresolvedDetail.getModel()).removeRow(selRow);
            if(rowCount > 1){
                selRow=selRow;
                tblUnresolvedDetail.addRowSelectionInterval(selRow, selRow);
            }
            return inbxBean;
            //tblUnresolvedDetail.revalidate();
        }
        return null;
    }*/
    
    
    
    /** This method takes up two dates of the same date and converts to required format
     * example: it takes strDate as 02/22/2003 and full greg calender date and
     * converts to 02/20/2003 11:24:54
     * @param arrTime
     * @param strDate
     * @return  formatted string
     */
    public String convertTimeToString(GregorianCalendar arrTime,String strDate){
        GregorianCalendar gCal = new GregorianCalendar();
        String strTime = "";
        if(arrTime!=null){
            gCal = arrTime;
            
            String hours = "";
            int hr = gCal.get(Calendar.HOUR_OF_DAY);   // If hour is a single digit append 0 before that
            if(hr<=9){
                hours = "0"+hr;
            }else{
                hours = ""+hr;
            }
            
            String minutes = "";
            int mins = gCal.get(Calendar.MINUTE);     //  If minute is a single digit append 0 before that
            if(mins<=9){
                minutes = "0"+mins;
            }else{
                minutes = ""+mins;
            }
            
            String seconds ="";
            int secs= gCal.get(Calendar.SECOND);    //  If second is a single digit append 0 before that
            if(secs<=9){
                seconds = "0"+secs;
            }else{
                seconds = ""+secs;
            }
            strTime = hours+":"+minutes+":"+seconds;
        }
        strTime = strDate +"  "+strTime ;
        return strTime;
    }
    
    
    //when double clicked shows the proposal detail form in display mode.
    //Modified for Coeus enhancement Case #1828 - start
    private void addListeners(){
        tblInbox.getSelectionModel().addListSelectionListener(this);
        try{
            tblInbox.addMouseListener(new MouseAdapter(){
                public void mouseClicked(MouseEvent me){
                    if (me.getClickCount() == 2) {
                        if(getModuleCode() == PROPOSAL_DEV_MODULE_CODE) {
                            int selRow = tblInbox.getSelectedRow();
                            String propNo = null;
                            if( selRow != -1 && tblInbox.getRowCount() > 0 ){
                                // Modified for the case # COEUSQA-2073 - Improve management of Coeus Inbox - Ability to reorder columns in Inbox - start
                                //Get the proposal Number from 1st column
                                //propNo = (String) tblInbox.getValueAt( selRow,4 );
                                propNo = (String) tblInbox.getValueAt( selRow,4 );
                                // Modified for the case # COEUSQA-2073 - Improve management of Coeus Inbox - Ability to reorder columns in Inbox - end
                                proposalNumber = propNo;
                                // Bug Fix  #990 - Start Added by chandra 6th July
                                if(proposalNumber.trim().equals("")){
                                    return ;
                                }// Bug fix #990 - End Chandra 6th July
                                if( hasDisplayProposalRights() ){
                                    try{
                                        checkDuplicateShow(propNo, 'D');
                                    }catch(Exception exc){
                                        
                                    }
                                }else{
                                    CoeusOptionPane.showInfoDialog("The user has no rights to display this proposal");
                                }
                            }
                        }
                        //Added for the case 4293 -Click to launch protocol from unresolved message in Inbox in premium -start
                        else if(getModuleCode() == PROTOCOL_MODULE_CODE) {
                            int selRow = tblInbox.getSelectedRow();
//                            String protoNo = null;
                            if( selRow != -1 && tblInbox.getRowCount() > 0 ){
                                // Modified for the case # COEUSQA-2073 - Improve management of Coeus Inbox - Ability to reorder columns in Inbox - start
                                //Get the protocol NUmber value form 1st column
                                //protoNo = (String) tblInbox.getValueAt( selRow,4 );
                                String protocolNumber = (String) tblInbox.getValueAt( selRow,4 );
                                // Modified for the case # COEUSQA-2073 - Improve management of Coeus Inbox - Ability to reorder columns in Inbox - end
//                                protocolNumber = protoNo;
                                //Modified for the case# COEUSDEV-299 : double click is no longer working to open protocol from inbox  - start
                                //ProtocolInfoBean protocolInfoBean;
                                try {
                                    //protocolInfoBean = getProtocolDetails(protocolNumber);
                                    //if(protocolInfoBean != null && protocolInfoBean.getProtocolNumber() != null){
                                       // if(!protocolNumber.trim().equals("")){
                                //Modified for the case# COEUSDEV-299 : double click is no longer working to open protocol from inbox  - end
                                       if(protocolNumber != null && !"".equals(protocolNumber)){
                                            if(hasDisplayIRBProtocolRights(protocolNumber)){
                                                edu.mit.coeus.irb.gui.ProtocolDetailForm protocolForm;
                                                try {
                                                    protocolForm = new edu.mit.coeus.irb.gui.ProtocolDetailForm(TypeConstants.DISPLAY_MODE, protocolNumber, CoeusGuiConstants.getMDIForm());
                                                    protocolForm.showDialogForm();
                                                } catch (Exception ex) {
                                                    ex.printStackTrace();
                                                }
                                            }
                                        }
                                   //Modified for the case#COEUSDEV-299 : double click is no longer working to open protocol from inbox  - start    
                                   // }
                                    
                                  //  else{
                                     //   CoeusOptionPane.showInfoDialog("The user has no rights to display this protocol");
                                  //  }
                                  //Modified for the case# COEUSDEV-299 : double click is no longer working to open protocol from inbox  - End     
                                } catch (Exception ex) {
                                    ex.printStackTrace();
                                }
                            }
                        }
                         
                        else if(getModuleCode()==AWARD_MODULE_CODE){
                            int selRow = tblInbox.getSelectedRow();
                             if( selRow != -1 && tblInbox.getRowCount() > 0 ){
                                  String awardNumber = (String) tblInbox.getValueAt( selRow,4 );
                                  try {
                                      if(awardNumber != null && !"".equals(awardNumber)){
                                      AwardBean awardBean = new AwardBean();
                                      awardBean.setMitAwardNumber(awardNumber);
                                      AwardBaseWindowController awardBaseWindowController = new AwardBaseWindowController("Display Award : ", 'D', awardBean);
                                      awardBaseWindowController.display();
                                      }
                                  }catch (Exception ex) {
                                          ex.printStackTrace();}
                        }
                        }
                        //Added for COEUSDEV-299 : double click is no longer working to open protocol from inbox  - start
                        //Display subcontract in display mode
                        else if(getModuleCode() == SUBCONTRACTS_MODULE_CODE){
                             int selRow = tblInbox.getSelectedRow();
                             String subContractNumber = null;
                             if( selRow != -1 && tblInbox.getRowCount() > 0 ){
                                 // Modified for the case # COEUSQA-2073 - Improve management of Coeus Inbox - Ability to reorder columns in Inbox - start
                                 // Get the subcontract number form 1st column
                                 //subContractNumber = (String) tblInbox.getValueAt( selRow,4 );
                                 subContractNumber = (String) tblInbox.getValueAt( selRow,4 );
                                 // Modified for the case # COEUSQA-2073 - Improve management of Coeus Inbox - Ability to reorder columns in Inbox - end
                                 try{
                                     checkDuplicateShow(subContractNumber, TypeConstants.DISPLAY_MODE);
                                 }catch(Exception ex){
                                     ex.printStackTrace();
                                 }
                             }
                                 
                        }
                        //COEUSDEV-299 : end
                        
                        //Added for the case 4293 -Click to launch protocol from unresolved message in Inbox in premium -end
                        //Added for Case#3682 - Enhancements related to Delegations - Start
                        else if(getModuleCode() == 0){
                            int selRow = tblInbox.getSelectedRow();                            
                            if( selRow != -1 && tblInbox.getRowCount() > 0 ){
                                displayUserDelegation();
                            }
                        }
                        //Added for Case#3682 - Enhancements related to Delegations - End
                        //IACUC Protocols
                        else if(getModuleCode() == IACUC_MODULE_CODE) {
                            int selRow = tblInbox.getSelectedRow();
                            if( selRow != -1 && tblInbox.getRowCount() > 0 ){
                                String protocolNumber = (String) tblInbox.getValueAt( selRow,4 );
                                try {
                                       if(protocolNumber != null && !"".equals(protocolNumber)){
                                            if(hasDisplayIACUCProtocolRights(protocolNumber)){
                                                edu.mit.coeus.iacuc.gui.ProtocolDetailForm protocolForm;
                                                try {
                                                    protocolForm = new edu.mit.coeus.iacuc.gui.ProtocolDetailForm(TypeConstants.DISPLAY_MODE, protocolNumber, CoeusGuiConstants.getMDIForm());
                                                    protocolForm.showDialogForm();
                                                } catch (Exception ex) {
                                                    ex.printStackTrace();
                                                }
                                            }
                                        }
                                   //Modified for the case#COEUSDEV-299 : double click is no longer working to open protocol from inbox  - start    
                                   // }
                                    
                                  //  else{
                                     //   CoeusOptionPane.showInfoDialog("The user has no rights to display this protocol");
                                  //  }
                                  //Modified for the case# COEUSDEV-299 : double click is no longer working to open protocol from inbox  - End     
                                } catch (Exception ex) {
                                    ex.printStackTrace();
                                }
                            }
                        }
                        else {
                            return;
                        }
                    }
                }
            });
        }catch(Exception exc){
            exc.printStackTrace();
        }
    }
    //Coeus enhancement Case #1828 - end
    
    //Added for Case#3682 - Enhancements related to Delegations - Start
    /*
     *Display Delegations window
     */
    private void displayUserDelegation() {
        userDelegationForm = new UserDelegationForm(mdiForm,true);
        userDelegationForm.display();
    }
    //Added for Case#3682 - Enhancements related to Delegations - End
    
    
    /**
     * This method is used to check whether the given Proposal number is already
     * opened in the given mode or not.
     */
    private void checkDuplicateShow(String refId, char mode)throws Exception {
        boolean duplicate = false;
        try{
            refId = refId == null ? "" : refId;
            //Modified for COEUSDEV-299 : double click is no longer working to open protocol from inbox - Start
            //Check duplicate frame for based on module (Proposal Development and Subcontract)
//               duplicate = mdiForm.checkDuplicate(
//            CoeusGuiConstants.PROPOSAL_DETAILS_FRAME_TITLE, refId, mode );
            if(getModuleCode() == PROPOSAL_DEV_MODULE_CODE){
                duplicate = mdiForm.checkDuplicate(
                        CoeusGuiConstants.PROPOSAL_DETAILS_FRAME_TITLE, refId, mode );
            }else if(getModuleCode() == SUBCONTRACTS_MODULE_CODE){
                duplicate = mdiForm.checkDuplicate(
                        CoeusGuiConstants.CORRECT_SUBCONTRACT_BASE_WINDOW, refId, mode );
            }
            //COEUSDEV-299 : End
        }catch(Exception e){
            // Exception occured.  Record may be already opened in requested mode
            //   or if the requested mode is edit mode and application is already
            //   editing any other record.
            duplicate = true;
            if(e.getMessage().length() > 0 ) {
                CoeusOptionPane.showInfoDialog(e.getMessage());
            }
            // Modified for COEUSDEV-299 : double click is no longer working to open protocol from inbox - Start
            // try to get the requested frame which is already opened
//            CoeusInternalFrame frame = mdiForm.getFrame(
//            CoeusGuiConstants.PROPOSAL_DETAILS_FRAME_TITLE,refId);
            CoeusInternalFrame frame = null;
            if(getModuleCode() == PROPOSAL_DEV_MODULE_CODE){
                // try to get the requested frame which is already opened
                 frame = mdiForm.getFrame(
                        CoeusGuiConstants.PROPOSAL_DETAILS_FRAME_TITLE,refId);
            }else if(getModuleCode() == SUBCONTRACTS_MODULE_CODE){
                 frame = mdiForm.getFrame(
                        CoeusGuiConstants.CORRECT_SUBCONTRACT_BASE_WINDOW,refId);
            }
            //COEUSDEV-299 : End
            if(frame == null){
                // if no frame opened for the requested record then the
                //   requested mode is edit mode. So get the frame of the
                //   editing record.
                //Modified for COEUSDEV-299 : double click is no longer working to open protocol from inbox - Start
//                   frame = mdiForm.getEditingFrame(
//                CoeusGuiConstants.PROPOSAL_DETAILS_FRAME_TITLE );
                if(getModuleCode() == PROPOSAL_DEV_MODULE_CODE){
                    frame = mdiForm.getEditingFrame(
                            CoeusGuiConstants.PROPOSAL_DETAILS_FRAME_TITLE );
                }else if(getModuleCode() == SUBCONTRACTS_MODULE_CODE){
                    frame = mdiForm.getEditingFrame(
                            CoeusGuiConstants.CORRECT_SUBCONTRACT_BASE_WINDOW );
                }
                //COEUSDEV-299 : End
            }
            if (frame != null){
                frame.setSelected(true);
                frame.setVisible(true);
            }
            return;
        }
        try{
            //Modified for COEUSDEV-299 : double click is no longer working to open protocol from inbox - Start
            if(getModuleCode() == PROPOSAL_DEV_MODULE_CODE){
                mainProposal = new ProposalDetailForm( mode, refId, mdiForm);
                
                if(  mode != 'D' ) {
                    //mainProposal.registerObserver( this );
                    //int selRow = tblProposal.getSelectedRow();
                    //if( selRow != -1 ) {
                    //  baseTableRow = Integer.parseInt((String)tblProposal.getValueAt(
                    //selRow,tblProposal.getColumnCount()-1));
                    //}
                }
                mainProposal.showDialogForm();
                //mainProposal.setProposalSheet(tblProposal);
            }else if(getModuleCode() == SUBCONTRACTS_MODULE_CODE){
                showSubcontract(mode, refId);
            }
            //COEUSDEV-299 : End
        }catch ( Exception ex) {
            ex.printStackTrace();
            try{
                if (!mainProposal.isModifiable() ) {
                    //Modified for case# 3439 to include the locking message - start
//                    String msg = coeusMessageResources.parseMessageKey(
//                    "proposal_row_clocked_exceptionCode.777777");
                    int resultConfirm = CoeusOptionPane.showQuestionDialog(ex.getMessage(),
                    CoeusOptionPane.OPTION_YES_NO,
                    CoeusOptionPane.DEFAULT_YES);
                    //Modified for case# 3439 to include the locking message - end
                    if (resultConfirm == 0) {
                        //showProposalDetails(refId);
                    }
                }else {
                    throw new Exception(ex.getMessage());
                }
            }catch (Exception excep){
                //excep.printStackTrace();
                throw new Exception(excep.getMessage());
            }
        }
    }
    
    private boolean hasDisplayProposalRights(){
        /** check first for the user has any OSP right
         */
       /* boolean hasRights = false;
        Vector vecFnParams = new Vector();
        String connectTo = CoeusGuiConstants.CONNECTION_URL + "/coeusFunctionsServlet";
        RequesterBean request = new RequesterBean();
        request.setId(mdiForm.getUserName());
        request.setDataObject("GET_USER_HAS_ANY_OSP_RIGHTS");
        AppletServletCommunicator comm
        = new AppletServletCommunicator(connectTo, request);
        comm.send();
        ResponderBean response = comm.getResponse();
        if (response!=null){
            if (response.isSuccessfulResponse()){
                int right = Integer.parseInt(response.getId());
                if (right == 1) {
                    hasRights = true;
                }
            }
        }
        /* check if the user has MODIFY_PROPOSAL right. Case #1860
        */
        /*if (!hasRights) {
            connectTo = CoeusGuiConstants.CONNECTION_URL + "/ProposalMaintenanceServlet";
            vecFnParams.addElement(mdiForm.getUserName());
            vecFnParams.addElement(proposalNumber);
            vecFnParams.addElement(MODIFY_PROPOSAL_RIGHT);
            request.setDataObjects(vecFnParams);
            request.setFunctionType('U');
            comm.setConnectTo(connectTo);
            comm.setRequest(request);
            comm.send();
            response = comm.getResponse();
            if (response!=null){
                if (response.isSuccessfulResponse()){
                    hasRights = ((Boolean) response.getDataObject()).booleanValue();
                }
            }
            /** check if the user has VIEW_PROPOSAL right
         */
          /*  if(!hasRights){
                connectTo = CoeusGuiConstants.CONNECTION_URL + "/ProposalMaintenanceServlet";
                vecFnParams.addElement(mdiForm.getUserName());
                vecFnParams.addElement(proposalNumber);
                vecFnParams.addElement(VIEW_RIGHT);
                request.setDataObjects(vecFnParams);
                request.setFunctionType('U');
                /** Bug fix #1922 - start
           */
            /*    comm.setConnectTo(connectTo);
                /** Bug Fix 1922 - End
             */
               /* comm.setRequest(request);
                comm.send();
                response = comm.getResponse();
                if (response!=null){
                    if (response.isSuccessfulResponse()){
                        hasRights = ((Boolean) response.getDataObject()).booleanValue();
                    }
                }
            }
        }*/
        
        
        
        
        boolean hasRights = false;
        String proposalNumber;
        Vector vecFnParams = new Vector();
        int selectedRow = tblInbox.getSelectedRow();
        // Modified for the case # COEUSQA-2073 - Improve management of Coeus Inbox - Ability to reorder columns in Inbox - start
        //Get the unit unmber from 20th column
        //String unitNumber = (String)tblInbox.getValueAt(selectedRow, 8);
        String unitNumber = (String)tblInbox.getValueAt(selectedRow, 8);
        // Modified for the case # COEUSQA-2073 - Improve management of Coeus Inbox - Ability to reorder columns in Inbox - end
        String connectTo = CoeusGuiConstants.CONNECTION_URL + "/coeusFunctionsServlet";
        RequesterBean request = new RequesterBean();
        AppletServletCommunicator comm
        = new AppletServletCommunicator(connectTo, request);
        request.setId(mdiForm.getUserName());
        request.setFunctionType('b');
        request.setDataObject("GET_USER_HAS_ANY_OSP_RIGHTS");
        comm.send();
        ResponderBean response = comm.getResponse();
        if (response!=null){
            if (response.isSuccessfulResponse()){
                int right = Integer.parseInt(response.getId());
                if (right == 1) {
                    hasRights = true;
                }
            }
        }
        
        /** Check if the User has VIEW_ANY_PROPOSAL in lead unit of the proposal.
         *pass the lead unit number of the selected proposal and find
         *whether right is there are not.
         *Call FN_USER_HAS_RIGHT(userId, leadUnit, VIEW_ANY_PROPOSAL) for right
         *checking
         */
        if(!hasRights) {
            connectTo = CoeusGuiConstants.CONNECTION_URL + "/ProposalMaintenanceServlet";
            request.setId(unitNumber);
            vecFnParams.addElement(mdiForm.getUserName());
            vecFnParams.addElement(unitNumber);
            vecFnParams.addElement(VIEW_ANY_PROPOSAL_RIGHT);
            request.setFunctionType('b');
            request.setDataObjects(vecFnParams);
            comm.setConnectTo(connectTo);
            comm.setRequest(request);
            comm.send();
            response = comm.getResponse();
            if (response!=null){
                if (response.isSuccessfulResponse()){
                    hasRights = ((Boolean) response.getDataObject()).booleanValue();
                }
            }
        }
        /** If the user doesn't find the MODIFY_ANY_PROPOSAL right at the top level,
         *then pass the lead unit of the selected proposal and find
         *whether MODIFY_ANY_PROPOSAL right is there are not.
         *Call FN_USER_HAS_RIGHT(userId, leadUnit, MODIFY_ANY_PROPOSAL) for right
         *checking
         */
        if(!hasRights) {
            connectTo = CoeusGuiConstants.CONNECTION_URL + "/ProposalMaintenanceServlet";
            vecFnParams.setElementAt(MODIFY_ANY_PROPOSAL_RIGHT, 2);
            request.setDataObjects(vecFnParams);
            comm.setConnectTo(connectTo);
            comm.setRequest(request);
            comm.send();
            response = comm.getResponse();
            if (response!=null){
                if (response.isSuccessfulResponse()){
                    hasRights = ((Boolean) response.getDataObject()).booleanValue();
                }
            }
        }
        
        /** If the user doesn't find the right at the lead unit level ,
         *then pass the proposal number of the selected proposal and find
         *whether MODIFY_PROPOSAL right is there are not.
         *Call FN_USER_HAS_PROP_RIGHT(userId, proposalNumber, MODIFY_PROPOSAL) for right
         *checking
         */
        if (!hasRights) {
            connectTo = CoeusGuiConstants.CONNECTION_URL + "/ProposalMaintenanceServlet";
            selectedRow = tblInbox.getSelectedRow();
            // Modified for the case # COEUSQA-2073 - Improve management of Coeus Inbox - Ability to reorder columns in Inbox - start
            //Get the proposal number from first column
            //proposalNumber = tblInbox.getValueAt(selectedRow, 4).toString();
            // Modified for the case # COEUSQA-2073 - Improve management of Coeus Inbox - Ability to reorder columns in Inbox - end
            proposalNumber = tblInbox.getValueAt(selectedRow, 4).toString();
            vecFnParams.setElementAt(proposalNumber, 1);
            vecFnParams.setElementAt(MODIFY_PROPOSAL_RIGHT, 2);
            request.setDataObjects(vecFnParams);
            request.setFunctionType('U');
            comm.setConnectTo(connectTo);
            comm.setRequest(request);
            comm.send();
            response = comm.getResponse();
            
            if (response!=null){
                if (response.isSuccessfulResponse()){
                    hasRights  = ((Boolean) response.getDataObject()).booleanValue();
                }
            }
        }
        //Check if User has MODIFY_NARRATIVE for this particular proposal
        if(!hasRights) {
            connectTo = CoeusGuiConstants.CONNECTION_URL + "/ProposalMaintenanceServlet";
            vecFnParams.setElementAt(MODIFY_NARRATIVE_RIGHT, 2);
            request.setDataObjects(vecFnParams);
            comm.setConnectTo(connectTo);
            comm.setRequest(request);
            comm.send();
            response = comm.getResponse();
            
            if (response!=null){
                if (response.isSuccessfulResponse()){
                    hasRights  = ((Boolean) response.getDataObject()).booleanValue();
                }
            }
        }
        
        //Check if User has MODIFY_BUDGET for this particular proposal
        if(!hasRights) {
            connectTo = CoeusGuiConstants.CONNECTION_URL + "/ProposalMaintenanceServlet";
            vecFnParams.setElementAt(MODIFY_BUDGET_RIGHT, 2);
            request.setDataObjects(vecFnParams);
            comm.setConnectTo(connectTo);
            comm.setRequest(request);
            comm.send();
            response = comm.getResponse();
            
            if (response!=null){
                if (response.isSuccessfulResponse()){
                    hasRights  = ((Boolean) response.getDataObject()).booleanValue();
                }
            }
        }
        
        /** If the user doesn't find the right at the lead unit level ,
         *then pass the proposal number of the selected proposal and find
         *whether VIEW_PROPOSAL right is there are not.
         *Call FN_USER_HAS_PROP_RIGHT(userId, proposalNumber, VIEW_PROPOSAL) for right
         *checking
         */
        if(!hasRights) {
            connectTo = CoeusGuiConstants.CONNECTION_URL + "/ProposalMaintenanceServlet";
            vecFnParams.setElementAt(VIEW_RIGHT, 2);
            request.setDataObjects(vecFnParams);
            comm.setConnectTo(connectTo);
            comm.setRequest(request);
            comm.send();
            response = comm.getResponse();
            
            if (response!=null){
                if (response.isSuccessfulResponse()){
                    hasRights  = ((Boolean) response.getDataObject()).booleanValue();
                }
            }
        }
        
        
        return hasRights;
    }
    
    public void actionPerformed(ActionEvent ae) {
    }
    
    
    /** This method is used to remove the selected row. This is used twice by
     * InboxDetailForm while deleting the row and while changing status
     * @return  InboxBean of the selected row
     */
    public InboxBean getRowBean(int selRow){
        int rowCount =tblInbox.getRowCount();
        InboxBean inbxBean=null;
        if(rowCount>0){
            //Modified by shiji for Coeus enhancement Case #1828 : step 7 : start
            // Modified for the case # COEUSQA-2073 - Improve management of Coeus Inbox - Ability to reorder columns in Inbox - start
            //Get the index value from 25th column
            //String numValue=(String) tblInbox.getValueAt(selRow, 12);
            String numValue=(String) tblInbox.getValueAt(selRow, 24);
            // Modified for the case # COEUSQA-2073 - Improve management of Coeus Inbox - Ability to reorder columns in Inbox - end
            //Coeus enhancement Case #1828 : step 7 : end
            Integer indexObject=new Integer(numValue);
            int index=indexObject.intValue();
            inbxBean=(InboxBean) newInboxVector.get(index);
            int row = sorter.getIndexForRow( selRow);
            sorter.removeRow( row );
            txtArMessage.setText("");
            
            //            if(rowCount > 0 && !(selRow == rowCount-1) ){
            //                tblInbox.addRowSelectionInterval(selRow, selRow);
            //            }else if( rowCount > 1 && selRow == rowCount -1 ) {
            //                tblInbox.addRowSelectionInterval(selRow-1,selRow -1);
            //            }
        }
        return inbxBean;
    }
    
    // Added for COEUSDEV-612:Refresh and UI issues in inbox window in Premium
    /**
     * Method to get the row data in HasMap format as you get in the search result.
     * @param selRow - the selected Row.
     * @return hmData - The row details in HashMap format.
     */
      public Vector getRowData(int[] selRows){
          Vector dataVector = new Vector();
          Vector beanVector = new Vector();
          for(int i=0;i<selRows.length;i++){
              int index  = Integer.parseInt(( String )tblInbox.getValueAt(selRows[i], 24));
              dataVector.add(inboxDataVector.elementAt(index));
              beanVector.add(newInboxVector.elementAt(index));
          }
          inboxDataVector.removeAll(dataVector);
          newInboxVector.removeAll(beanVector);
          Vector retVector = new Vector();
          retVector.add(dataVector);
          retVector.add(beanVector);
          return retVector;
      }
    
    //Added for Case#3682 - Enhancements related to Delegations - start
    /*
     * This method is used to check if status of inbox message selected
     * can be changed.
     */
    public boolean canChangeStatus(){
        int count=tblInbox.getRowCount();
        boolean value = true;
        InboxBean inbox ;
        if(count >0){
            // Modified with COEUSDEV-612:Refresh and UI issues in inbox window in Premium
            int[] selectedRows=tblInbox.getSelectedRows();
            for(int i = 0 ; i<selectedRows.length;i++){
                // Modified for the case # COEUSQA-2073 - Improve management of Coeus Inbox - Ability to reorder columns in Inbox - start
                // Get the value of index from first column
                //String numValue=(String) tblInbox.getValueAt(selectedRow, 12);
                String numValue=(String) tblInbox.getValueAt(selectedRows[i], 24);
                // Modified for the case # COEUSQA-2073 - Improve management of Coeus Inbox - Ability to reorder columns in Inbox - end
                Integer indexObject=new Integer(numValue);
                int index=indexObject.intValue();
                inbox = (InboxBean) newInboxVector.get( index );
                char subject = inbox.getSubjectType();
                if(subject == 'A' ){
                    //Added for the case# COEUSQA-2059 & COEUSQA-2302 - Alternate Approver cannot remove an unresolved message - start
                    //We are allowing the user to delete or change the status for the following proposal and protocol status
                    // status == 4 => Approved
                    // status == 5 => Submitted
                    // status == 6 => Post-Submission Approval
                    // status == 7 => Post-Submission Rejection
                    // status == 200 => Active - Open to Enrollment
                    // status == 201 => Active - Closed to Enrollment
                    // status == 202 => Active - Data Analysis Only
                    // status == 203 => Active - Exempt
                    // status == 300 => Closed Administratively for lack of response
                    // status == 301 => Closed by Investigator
                    int status = inbox.getCreationStatus();
                    if(status != 0 && status == 4 || status ==  5 ||  status == 6 || status == 7 ||
                            status == 200 || status ==  201 || status == 202 || status == 203 || status == 300 ||
                            status == 301
                            ){
                        value = true;
                    }else{
                        //Added for the case# COEUSQA-2059 & COEUSQA-2302 - Alternate Approver cannot remove an unresolved message - end
                        value = false;
                    }
                }
                if(subject == 'D' ){
                    if(inbox.getCreationStatus() == 1){
                        value = false;
                    }else{
                        if(isUnresolved()){
                            value = true;
                        }else if(!isUnresolved()){
                            value = false;
                        }
                    }
                }
                if(!value){
                    break;
                }
            }
            // COEUSDEV-612:End
        }
        return value ;
    }
    //Added for Case#3682 - Enhancements related to Delegations - end
    
    /** This method used by InboxDetailForm to check if the row can be
     * deleted.
     * @return
     */
    public boolean canDelete(){
        int count=tblInbox.getRowCount();
        boolean value = true;
        InboxBean inbox ;
        if(count >0){
            int selectedRow=tblInbox.getSelectedRow();
            //Modified by shiji for Coeus enhancement Case #1828 : step 8 : start
            // Modified for the case # COEUSQA-2073 - Improve management of Coeus Inbox - Ability to reorder columns in Inbox - start
            //Get the value of index from 25th column
            //String numValue=(String) tblInbox.getValueAt(selectedRow, 12);
            String numValue=(String) tblInbox.getValueAt(selectedRow, 24);
            // Modified for the case # COEUSQA-2073 - Improve management of Coeus Inbox - Ability to reorder columns in Inbox - end
            //Coeus enhancement Case #1828 : step 8 : end
            Integer indexObject=new Integer(numValue);
            int index=indexObject.intValue();
            inbox = (InboxBean) newInboxVector.get( index );
            char subject = inbox.getSubjectType();
            if(subject == 'A' ){
                //Added for the case# COEUSQA-2059 & COEUSQA-2302 - Alternate Approver cannot remove an unresolved message - start
                //We are allowing the user to delete or change the status for the following proposal and protocol status
                // status == 4 => Approved
                // status == 5 => Submitted
                // status == 6 => Post-Submission Approval
                // status == 7 => Post-Submission Rejection
                // status == 200 => Active - Open to Enrollment
                // status == 201 => Active - Closed to Enrollment
                // status == 202 => Active - Data Analysis Only
                // status == 203 => Active - Exempt
                // status == 300 => Closed Administratively for lack of response
                // status == 301 => Closed by Investigator
                int status = inbox.getCreationStatus();
                if(status != 0 && status == 4 || status ==  5 ||  status == 6 || status == 7 ||
                        status == 200 || status ==  201 || status == 202 || status == 203 || status == 300 ||
                        status == 301
                        ){
                    value = true;
                }else{
                    //Added for the case# COEUSQA-2059 & COEUSQA-2302 - Alternate Approver cannot remove an unresolved message - end
                    value = false;
                }
            }
            //Added for Case#3682 - Enhancements related to Delegations - Start                      
            if(subject == 'D' ){                
                if(inbox.getCreationStatus() == 1){
                    value = false;                
                }else{
                    if(isUnresolved()){
                        value = false;
                    }else if(!isUnresolved()){
                        value = true; 
                    }                    
                }                
            }
            //Added for Case#3682 - Enhancements related to Delegations - End
        }
        return value;
    }
    
    // Added for COEUSDEV-612:Refresh and UI issues in inbox window in Premium
    /** To add a row,Whenever called with the inboxBean it constructs a new row and adds to
     * the table as the first row.
     * @param inboxBean
     * @param hmRowData - The row data
     */
    
    public void addRowToTable(Vector dataVector,Vector beanVector){
        for(int i = 0; i<dataVector.size();i++ ){
            newInboxVector.add(0,beanVector.get(i));
            inboxDataVector.add(0,dataVector.get(i));
        }
    }
 // Commented this method and overridden the same with COEUSDEV-612:Refresh and UI issues in inbox window in Premium   
//    public void addRowToTable(InboxBean inboxBean,char flag){
//        
//        Vector vecRowData=new Vector();
        // Modified for the case # COEUSQA-2073 - Improve management of Coeus Inbox - Ability to reorder columns in Inbox - start
        // The datas are set through buldTable method, in order to get visible and non-visible columns
//        String userName=inboxBean.getUserName();
//        GregorianCalendar arrivalDate = new GregorianCalendar();
//        arrivalDate.setTimeInMillis( inboxBean.getArrivalDate().getTime() );
//        String halfDate=inboxBean.getArrivalDate().toString();
//        halfDate = dtUtils.formatDate(halfDate.toString(),REQUIRED_DATEFORMAT1);
//        String date = convertTimeToString(arrivalDate,halfDate);
//        char sub=inboxBean.getSubjectType();
//
//        java.sql.Timestamp beginDate = inboxBean.getArrivalDate();
//        java.util.Date dateConverted = (java.util.Date)beginDate;
//
//
//        String subject=null;
//        if(sub=='N'){
//            subject="Notification";
//        }else if(sub=='A'){
//            subject="Approval";
//        }else if(sub == 'R'){
//            subject="Rejected";
//        }
//        //Added for Case#3682 - Enhancements related to Delegations - Start
//        else if(sub == 'D'){
//            subject="Delegation";
//        }
//        //Added for Case#3682 - Enhancements related to Delegations - End
//        String proposal=null;
//        if(inboxBean.getProposalNumber()!=null){
//            proposal=inboxBean.getProposalNumber();
//        }
//        String deadline=null;
//        if(inboxBean.getProposalDeadLineDate()!=null){
//            deadline=inboxBean.getProposalDeadLineDate().toString();
//        }
//
//        String status = inboxBean.getCreationStatusDescription();
//        String sponsorName=inboxBean.getSponsorName();
//        String leadUnitNo=inboxBean.getUnitNumber();
//        String leadUnitName=inboxBean.getUnitName();
//        String pi=inboxBean.getPersonName();
//        String title=inboxBean.getProposalTitle();
//        //Modified by shiji for Coeus enhancement Case #1828 : step 14 : start
//        String module= inboxBean.getModule();
//        String item=inboxBean.getProposalNumber();
//        //Coeus enhancement Case #1828 : step 14 : end
        
//        String moduleItem = inboxBean.getItem();
//        
//        int moduleCode = inboxBean.getModuleCode();
//        
//        String moduleDesc =  inboxBean.getModule();
//        
//        String toUser = inboxBean.getToUser();
//        
//        String messageId = inboxBean.getMessageId();
//        
//        String fromUser =  inboxBean.getFromUser();
//        
//        GregorianCalendar arrivalDate = new GregorianCalendar();
//        arrivalDate.setTimeInMillis( inboxBean.getArrivalDate().getTime() );
//        String halfDate=inboxBean.getArrivalDate().toString();
//        halfDate = dtUtils.formatDate(halfDate,REQUIRED_DATEFORMAT1);
////        String date = convertTimeToString(arrivalDate,halfDate);
//        
//        java.sql.Timestamp beginDate = inboxBean.getArrivalDate();
//        
//        java.util.Date dateConverted = (java.util.Date)beginDate;
//        
//        char sub=inboxBean.getSubjectType();
//        
//        String subjectDescription = inboxBean.getSubjectDescription();
//        
//        char openedFlag = inboxBean.getOpenedFlag();
//        
//        String updateTimeStemp=null;
//        if(inboxBean.getUpdateTimeStamp()!=null){
//            updateTimeStemp=inboxBean.getUpdateTimeStamp().toString();
//        }
//        
//        String upateUser = inboxBean.getUpdateUser();
//        
//        String message = inboxBean.getMessageBean().getMessage();
//        
//        String fromUserName = inboxBean.getFromUser();
//        
//        String deadline=null;
//        if(inboxBean.getProposalDeadLineDate()!=null){
//            deadline=inboxBean.getProposalDeadLineDate().toString();
//        }
//        
//        String title = inboxBean.getProposalTitle();
//        
//        String sponsorCode = inboxBean.getSponsorCode();
//        
//        String sponsorName = inboxBean.getSponsorName();
//        
//        String sysDate=null;
//        if(inboxBean.getSysDate()!=null){
//            sysDate=inboxBean.getSysDate().toString();
//        }
//        
//        String unitNumber = inboxBean.getUnitNumber();
//        
//        String unitName = inboxBean.getUnitName();
//        
//        String piName = inboxBean.getPersonName();
//        
//        int statusCode = inboxBean.getCreationStatus();
//        
//        String statusDescription = inboxBean.getCreationStatusDescription();
        
//        vecRowData.addElement(userName == null ?inboxBean.getFromUser():userName);
//        // Commented by chandra to  fix 1082
//        // vecRowData.addElement(date == null ? "" : date);
//        // End Chandra
//        vecRowData.addElement(dateConverted == null ? null : dateConverted);
//        vecRowData.addElement(subject== null? "" : subject);
//        //Modified by shiji for Coeus enhancement Case #1828 : step 15 : start
//        //vecRowData.addElement(proposal == null ? "" : proposal);
//        vecRowData.addElement(module == null ? "" : module);
//        vecRowData.addElement(item == null ? "" : item);
//        //Coeus enhancement Case #1828 : step 15 : end
//        vecRowData.addElement(deadline == null ? "" : deadline);
//        vecRowData.addElement(status ==null? "" : status);
//        vecRowData.addElement(sponsorName == null ? "" : sponsorName);
//        vecRowData.addElement(leadUnitNo == null ? "" : leadUnitNo);
//        vecRowData.addElement(leadUnitName == null ? "" : leadUnitName);
//        vecRowData.addElement(pi==null? "" : pi);
//        vecRowData.addElement(title == null ? "" : title);
        
//        vecRowData.addElement(moduleItem == null ?"":moduleItem);
//        
//        vecRowData.addElement(moduleCode == 0 ?"":moduleCode);
//        
//        vecRowData.addElement(moduleDesc == null ?"":moduleDesc);
//        
//        vecRowData.addElement(toUser == null ?"":toUser);
//        
//        vecRowData.addElement(messageId == null ?"":messageId);
//        
//        vecRowData.addElement(fromUser == null ?"":fromUser);
//        
//        vecRowData.addElement(fromUserName == null ?"":fromUserName);
//        
//        vecRowData.addElement(dateConverted == null ?"":dateConverted);
//        
//        vecRowData.addElement(sub);
//        
//        vecRowData.addElement(subjectDescription == null ?"":subjectDescription);
//        
//        vecRowData.addElement(openedFlag);
//        
//        vecRowData.addElement(updateTimeStemp == null ?"":updateTimeStemp);
//        
//        vecRowData.addElement(upateUser == null ?"":upateUser);
//        
//        vecRowData.addElement(message == null ?"" :message);
//        
//        vecRowData.addElement(deadline == null ?"":deadline);
//        
//        vecRowData.addElement(title == null ?"":title);
//        
//        vecRowData.addElement(sponsorCode == null ?"":sponsorCode);
//        
//        vecRowData.addElement(sponsorName == null ?"":sponsorName);
//        
//        vecRowData.addElement(sysDate == null ?"":sysDate);
//        
//        vecRowData.addElement(unitNumber == null ?"":unitNumber);
//        
//        vecRowData.addElement(unitName == null ?"":unitName);
//        
//        vecRowData.addElement(piName == null ?"":piName);
//        
//        vecRowData.addElement(statusCode == 0 ?"":statusCode);
//        
//        vecRowData.addElement(statusDescription == null ?"":statusDescription);
//        // Modified for the case # COEUSQA-2073 - Improve management of Coeus Inbox - Ability to reorder columns in Inbox - end
//        int index=newInboxVector.size();
//        Integer indexVal=new Integer(index);
//        String indexValue= indexVal.toString();
//        vecRowData.addElement(indexValue);
//        
//        newInboxVector.add(inboxBean);
//        //        if( index > 0 ) {
//        //            sorter.insertRow(index-1,vecRowData);
//        //        }else{
//        //            sorter.insertRow(index,vecRowData);
//        //        }
//        
//        // Modified for the case # COEUSQA-2073 - Improve management of Coeus Inbox - Ability to reorder columns in Inbox - start
//        //((DefaultTableModel)tblInbox.getModel()).addRow(vecRowData);
//        ((DefaultTableModel)tblInbox.getModel()).insertRow(0,vecRowData);
//        // Modified for the case # COEUSQA-2073 - Improve management of Coeus Inbox - Ability to reorder columns in Inbox - end
//        setUrgencyColor();
//        tblInbox.addRowSelectionInterval(0,0);
//    }
    
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc=" Generated Code ">                          
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;
        // Modified for the case # COEUSQA-2073 - Improve management of Coeus Inbox - Ability to reorder columns in Inbox - start
        // Commented for COEUSDEV-612:Refresh and UI issues in inbox window in Premium
//        Vector disLabelList = new Vector();
//        int disColCnt = inboxDataLabels.size();
//        for(int disColIndex=0;disColIndex<disColCnt;disColIndex++){
//            DisplayBean display = (DisplayBean)inboxDataLabels.elementAt(disColIndex);
////            if(display.isVisible()){
//            disLabelList.addElement(display.getValue());
//            
////            }
//        }
//        disLabelList.addElement("Index");
        // Modified for the case # COEUSQA-2073 - Improve management of Coeus Inbox - Ability to reorder columns in Inbox - end
        scrPnUnresolvedDetail = new javax.swing.JScrollPane();
        tblInbox = new javax.swing.JTable();
        scrPnMessage = new javax.swing.JScrollPane();
        txtArMessage = new javax.swing.JTextArea();

        setLayout(new java.awt.GridBagLayout());

        scrPnUnresolvedDetail.setMaximumSize(new java.awt.Dimension(983, 432));
        scrPnUnresolvedDetail.setMinimumSize(new java.awt.Dimension(983, 432));
        scrPnUnresolvedDetail.setPreferredSize(new java.awt.Dimension(983, 432));
        // Modified for the case # COEUSQA-2073 - Improve management of Coeus Inbox - Ability to reorder columns in Inbox - start
        // Commented for COEUSDEV-612:Refresh and UI issues in inbox window in Premium
//        tblInbox.setModel(new DefaultTableModel(new String[][]{},
//            new String [] {"From ", "Date Sent", "Subject",
//                "Module", "Item", "Deadline",
//                "Status", "Sponsor Name","Lead Unit","Lead Unit Name","PI","Title","Index"}
//        ){
//            public boolean isCellEditable(int row,int col){
//                return false;
//            }
//        });
        
//        tblInbox.setModel(new DefaultTableModel(new Object[][]{},
//            disLabelList.toArray()
//        ){
//            public boolean isCellEditable(int row,int col){
//                return false;
//            }
//        });
        // Modified for the case # COEUSQA-2073 - Improve management of Coeus Inbox - Ability to reorder columns in Inbox - end
        scrPnUnresolvedDetail.setViewportView(tblInbox);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 3, 0, 0);
        add(scrPnUnresolvedDetail, gridBagConstraints);

        scrPnMessage.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEtchedBorder(), "Message", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, CoeusFontFactory.getLabelFont()));
        scrPnMessage.setMinimumSize(new java.awt.Dimension(983, 100));
        scrPnMessage.setPreferredSize(new java.awt.Dimension(983, 100));
        txtArMessage.setDocument(new LimitedPlainDocument(2000));
        txtArMessage.setFont(CoeusFontFactory.getNormalFont());
        txtArMessage.setWrapStyleWord(true);
        txtArMessage.setEnabled(false);
        scrPnMessage.setViewportView(txtArMessage);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 0, 0);
        add(scrPnMessage, gridBagConstraints);

    }// </editor-fold>                        
    
    // Variables declaration - do not modify                     
    public javax.swing.JScrollPane scrPnMessage;
    public javax.swing.JScrollPane scrPnUnresolvedDetail;
    public javax.swing.JTable tblInbox;
    public javax.swing.JTextArea txtArMessage;
    // End of variables declaration                   
    
    /** Takes up two dates and gives the difference.First date is future date and second
     * date is present date
     * @param cal1
     * @param cal2
     * @return days
     */    
//    public long getNumberOfDays(GregorianCalendar cal1,GregorianCalendar cal2) {
//        
//        long ms=0;
//        if(cal1.after(cal2)){
//            ms=cal1.getTime().getTime() - cal2.getTime().getTime();
//        }
//        long s=ms/1000;
//        long m=s/60;
//        long h=m/60;
//        return h/24;
//    }
    
    /** Removes all the rows of the table. Used whenever we are refreshing the inbox. */    
    public void emptyTable(){
//        int count=tblInbox.getRowCount();
//        ((DefaultTableModel)tblInbox.getModel()).setDataVector(
//            new Object[][]{},getColumnNames().toArray());
//        ((DefaultTableModel)tblInbox.getModel()).fireTableDataChanged();
//        setTableEditor();
    }
    
    
    class InboxTableRenderer extends DefaultTableCellRenderer {
        JTextField txtComponent;
//        private int rowIndex;
        private String strDateValue=null;
        private java.util.Date deadlineDate=null;
//        private GregorianCalendar calendarDeadlineDate;
        //COEUSQA-1477 Dates in Search Results - Start
        private String AM_PM_FORMAT = "";
        private String lastUpdateFormat = "";
        //COEUSQA-1477 Dates in Search Results - End
        
        InboxTableRenderer() {
            txtComponent = new JTextField();
//            this.rowIndex=rowIndex;
            //COEUSQA-1477 Dates in Search Results - Start
            //to fetch the user defined date format
            AM_PM_FORMAT = CoeusServerProperties.getProperty(CoeusPropertyKeys.SEARCH_DATE_FORMAT);            
            DateUtils dtUtils = new DateUtils();
            //to load the valid date formats
            HashMap hmDateFormat = dtUtils.loadSearchResultDateFormats();
            if(hmDateFormat.get(AM_PM_FORMAT)!=null){
                lastUpdateFormat = hmDateFormat.get(AM_PM_FORMAT).toString();
            }else{
                lastUpdateFormat = CoeusConstants.JAVA_DATE_YYYY_MM_DD_SLASH;
            }
            //COEUSQA-1477 Dates in Search Results - End
        }
        public Component getTableCellRendererComponent(JTable table,
        Object value, boolean isSelected, boolean hasFocus, int row,
        int column) {
            if(column==1){
                if(value == null || value.toString().equals("")){
                    setText("");
                    }
            }
            if( column >= 0 ){
                InboxBean inbx;
                // Modified for the case # COEUSQA-2073 - Improve management of Coeus Inbox - Ability to reorder columns in Inbox - start
                //String subjType = ( String ) tblInbox.getValueAt(row, 2);
                String subjType = ( String ) tblInbox.getValueAt(row, 2);
                //Modified by shiji for Coeus enhancement Case #1828 : step 9 : start
                //String numValue=(String) tblInbox.getValueAt(row , 12);
                String numValue=(String) tblInbox.getValueAt(row , 24);
                // Modified for the case # COEUSQA-2073 - Improve management of Coeus Inbox - Ability to reorder columns in Inbox - end
                //Coeus enhancement Case #1828 : step 9 : end
                Integer indexObject=new Integer(numValue);
                int index=indexObject.intValue();
                inbx = (InboxBean) newInboxVector.get( index );
                String msgId = inbx.getMessageId();
                Integer msgObj = new Integer(msgId);
                int msg = msgObj.intValue();
                subjType.trim();
                if( subjType.equalsIgnoreCase("Approval") && msg == 1 ){
                    setFont( CoeusFontFactory.getLabelFont() );
                }else {
                    setFont( CoeusFontFactory.getNormalFont());
                }
            }
            //Modified by shiji for Coeus enhancement Case #1828 : step 10 : start
            // Modified for the case # COEUSQA-2073 - Improve management of Coeus Inbox - Ability to reorder columns in Inbox - start
            //Get the deadline date form 15 the column
            //if(column==5){
            if(column==5){
            // Modified for the case # COEUSQA-2073 - Improve management of Coeus Inbox - Ability to reorder columns in Inbox - end
            //Coeus enhancement Case #1828 : step 10 : end
                if(value == null || value.toString().equals("")){
                    setText("");
                    }else{
                        //COEUSQA-1477 Dates in Search Results - Start
                        //value = dtUtils.formatDate(value.toString(),REQUIRED_DATEFORMAT);
                        SimpleDateFormat deadLineFormat = new SimpleDateFormat(lastUpdateFormat);
                        java.sql.Timestamp valueTimeStamp =  java.sql.Timestamp.valueOf(value.toString());
                        value = deadLineFormat.format(valueTimeStamp);
                        value = value.toString().substring(0,value.toString().indexOf(" "));
                        //COEUSQA-1477 Dates in Search Results - End
                        setText(value.toString());
                    }
            }
            if(isSelected){
                setBackground((java.awt.Color) javax.swing.UIManager.getDefaults().get("Table.selectionBackground"));
                setForeground(Color.WHITE);
                setText(value.toString() );
                
            }else if(column>=0 ){
                setForeground(Color.BLACK);
                //Modified by shiji for Coeus enhancement Case #1828 : step 11 : start
                // Modified for the case # COEUSQA-2073 - Improve management of Coeus Inbox - Ability to reorder columns in Inbox - start
                 //Get the deadline date form 15 the column
                //strDateValue=(String) tblInbox.getValueAt(row,5);
                strDateValue=(String) tblInbox.getValueAt(row,5);
//                strDateValue=(String)getValueAt(row,"DEADLINE_DATE");
                // Modified for the case # COEUSQA-2073 - Improve management of Coeus Inbox - Ability to reorder columns in Inbox - end
                //Coeus enhancement Case #1828 : step 11 : end
//                strDateValue.trim();
//                if( !strDateValue.equalsIgnoreCase("") && strDateValue!=null){
                if(strDateValue!=null && !"".equals(strDateValue)){
                    try {
                        SimpleDateFormat deadLineFormat = new SimpleDateFormat("yyyy-MM-dd");
                        deadlineDate = deadLineFormat.parse(strDateValue.trim());
                    } catch (ParseException ex) {}
                    // Modified for the case # COEUSQA-2073 - Improve management of Coeus Inbox - Ability to reorder columns in Inbox - start
//                    deadlineDate = new java.util.Date(todayDate.getTime());
                    //deadlineDate = deadlineDate.valueOf(strDateValue);
                    // Modified for the case # COEUSQA-2073 - Improve management of Coeus Inbox - Ability to reorder columns in Inbox - end
                    if( deadlineDate != null ) {
//                        calendarDeadlineDate=new GregorianCalendar();
//                        calendarDeadlineDate.setTimeInMillis(deadlineDate.getTime());
//                        long days = getNumberOfDays(calendarDeadlineDate, calendarTodayDate );
                        int days = dtUtils.calculateDateDiff(Calendar.DATE,todayDate,deadlineDate);
                        //Above function returns 1 day less so incrementing manually
//                        if(days!=0){
//                            days=days+1;
//                        }
                                                
                        //Case 2306,2480 start
                        //COEUSQA-2755: Problem with Color Coding in Inbox 
                        //Apply color coding for both Resolved and Unresolved messages
                        // if(isUnresolved()){
                            if(days>=0 && days<=2){
                                setBackground(Color.RED);
                                setForeground(Color.BLACK);
                            }else if(days>2 && days<=4){
                                setBackground(Color.YELLOW);
                                setForeground(Color.BLACK);
                            }else if(days>=5 && days <=10){
                                setBackground(Color.GREEN);
                                setForeground(Color.BLACK);
                            } //else if(days>11){
                            else {
                                setBackground(Color.WHITE);
                                setForeground(Color.BLACK);
                            }
//                        }else{
//                            setBackground(Color.WHITE);
//                            setForeground(Color.BLACK);
//                        }//Case 2306,2480 End
                            
                    }
                }else{
                    setBackground(Color.WHITE);
                }
                setText(value.toString() );
            }else{
                setBackground(Color.WHITE);
                setForeground(Color.BLACK);
            }
         /** If the column is 1 then set the date format for the timestamp
          * Added by chnadra for date formatting for bug fix 1082
          */
            // Modified for the case # COEUSQA-2073 - Improve management of Coeus Inbox - Ability to reorder columns in Inbox - start
            //get the Arrival date,updated time stamp and sys date value
            //if(column==1){
            if(column==1 || column==22 || column==18){
                // Modified for the case # COEUSQA-2073 - Improve management of Coeus Inbox - Ability to reorder columns in Inbox - end
                //Modified for COEUSDEV-321 :  Incorrect Date Sent field in Inbox window for IRB protoocl messages - Start
                //To display date in date sent column in 'MM/DD/YYYY HH:MM A'format
//                if(value!= null && !(value.toString().trim().equals(""))){
//                    value = dateFormat.format(value);
//                    setText(value.toString());
//                }else{
//                    setText(null);
//                }
                //Commented for COEUSQA-1477 Dates in Search Results
                //String lastUpdateFormat = REQUIRED_DATEFORMAT1+ " hh:mm a";
                
                if(value!= null && !(value.toString().trim().equals(""))){
                    //COEUSQA-1477 Dates in Search Results - Start
                    //dateFormat.applyPattern(lastUpdateFormat);
                    // Modified for the case # COEUSQA-2073 - Improve management of Coeus Inbox - Ability to reorder columns in Inbox - start
                    //value = dateFormat.format(value);
                    //value = dateFormat.format(valueTimeStamp);
                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat(lastUpdateFormat);
                    java.sql.Timestamp valueTimeStamp =  java.sql.Timestamp.valueOf(value.toString());
                    value = simpleDateFormat.format(valueTimeStamp);
                    //COEUSQA-1477 Dates in Search Results - End
                    // Modified for the case # COEUSQA-2073 - Improve management of Coeus Inbox - Ability to reorder columns in Inbox - end
                    setText(value.toString());
                }else{
                    setText(null);
                }
                //COEUSDEV-321 : End
            }// End chandra
            
            return this; //super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
        }
    }
    
    /** Accesses the proposal number from the table and returns the same when called.
     * @return Proposal Number
     */    
    public String getItemNumber(){
        int rows=tblInbox.getRowCount();
        if(rows>0){
            int selRow=tblInbox.getSelectedRow();
            // Modified for the case # COEUSQA-2073 - Improve management of Coeus Inbox - Ability to reorder columns in Inbox - start
            //get the module item key value
//            String itemNum=( String )tblInbox.getValueAt(selRow, 4);
            String itemNum=( String )tblInbox.getValueAt(selRow, 4);
            // Modified for the case # COEUSQA-2073 - Improve management of Coeus Inbox - Ability to reorder columns in Inbox - end
            return itemNum;
        }
        return null;
    }
    
    public int getModuleCode() {
       int rows=tblInbox.getRowCount();
       int moduleCode=0;
        if(rows>0){
            int selRow=tblInbox.getSelectedRow();
            // Modified for the case # COEUSQA-2073 - Improve management of Coeus Inbox - Ability to reorder columns in Inbox - start
            //get the module name value formt he third column
            //String module=( String )tblInbox.getValueAt(selRow, 3);
            if(selRow >= 0){//Added this check to avoid exceptions:COEUSDEV-612:Refresh and UI issues in inbox window in Premium
                String module=( String )tblInbox.getValueAt(selRow, 3);
                // Modified for the case # COEUSQA-2073 - Improve management of Coeus Inbox - Ability to reorder columns in Inbox - end
                if(module.equals("Award")) {
                    moduleCode = AWARD_MODULE_CODE;
                }else if(module.equals("Development Proposal")) {
                    moduleCode = PROPOSAL_DEV_MODULE_CODE;
                }else if(module.equals("Institute Proposal")) {
                    moduleCode = PROPOSAL_INSTITUTE_MODULE_CODE;
                }else if(module.equals("IRB Protocol")) {
                    moduleCode = PROTOCOL_MODULE_CODE;
                }
                //Added for princeton enhancements case#2802
                else if(module.endsWith("Subcontracts")){
                    moduleCode = SUBCONTRACTS_MODULE_CODE ;
                }else if(module.equals("IACUC Protocol")){
                    moduleCode = IACUC_MODULE_CODE;
                }
            }
            return moduleCode;
        }
        return moduleCode; 
    }
    
    /** Gets the status from table and returns the same.
     * @return Status
     */    
//    public String getStatus(){
//        int rows=tblInbox.getRowCount();
//        if(rows>0){
//            int selRow=tblInbox.getSelectedRow();
//            String status=( String )tblInbox.getValueAt(selRow, 5);
//            return status;
//        }
//        return null;
//    }
    
    /**
     *function to return the status code 
     *@return statusCode
     */
    public int getStatusCode(){
        int rows=tblInbox.getRowCount();
        int selectedRow = tblInbox.getSelectedRow();
        InboxBean inbox;
        if(rows>0 && selectedRow != -1 ){
            // Modified for the case # COEUSQA-2073 - Improve management of Coeus Inbox - Ability to reorder columns in Inbox - start
            //Get the index or row value from 25th column
            //Modified by shiji for Coeus enhancement Case #1828 : step 12 : start
//            String numValue=(String) tblInbox.getValueAt(selectedRow, 12);
            String numValue=(String) tblInbox.getValueAt(selectedRow, 24);
            // Modified for the case # COEUSQA-2073 - Improve management of Coeus Inbox - Ability to reorder columns in Inbox - end
            //Coeus enhancement Case #1828 : step 12 : end
            Integer indexObject=new Integer(numValue);
            int index=indexObject.intValue();
            inbox = (InboxBean) newInboxVector.get( index );
            int statusCode = inbox.getCreationStatus();
            return statusCode;
        }
        return -1;
    }
    
    /**
     *Function used to show the message in the message box while changing between tabbed panes
     */
    public void showRowMessage(){
        if(tblInbox.getRowCount()>0 ){
            int selectedRow=tblInbox.getSelectedRow();
            if(selectedRow >=0){
                //Modified by shiji for Coeus enhancement Case #1828 : step 13 : start
                // Modified for the case # COEUSQA-2073 - Improve management of Coeus Inbox - Ability to reorder columns in Inbox - start
                //get the index value
                //String numValue=(String) tblInbox.getValueAt(selectedRow, 12);
                String numValue=(String) tblInbox.getValueAt(selectedRow, 24);
                // Modified for the case # COEUSQA-2073 - Improve management of Coeus Inbox - Ability to reorder columns in Inbox - end
               // Coeus enhancement Case #1828 : step 13 : end 
                Integer indexObject=new Integer(numValue);
                int index=indexObject.intValue();
                inbox = (InboxBean) newInboxVector.get( index );
                messageBean=(MessageBean) inbox.getMessageBean();
                if( messageBean != null ){
                    String messg = messageBean.getMessage();
                    if( messg!=null){
                        //Added for princeton enhancement case#2802 - starts
                        //To filter the sequence number and line number present in the message
                        //in subcontract module
                        if(inbox.getModuleCode() == 4){
                            messg = messg.substring(messg.indexOf("|")+1);
                            messg = messg.substring(messg.indexOf("|")+1).trim();
                        }
                        //Added for princeton enhancement case#2802 - ends                        
                       txtArMessage.setText(messg);
                       txtArMessage.setCaretPosition(0);
                      }
                    }else{
    //                    System.out.println("Message bean is null");
                        txtArMessage.setText("");
                    }
              }
        }else{
            txtArMessage.setText("");
        }
    }

    /**
     * Getter for property unresolved.
     * @return Value of property unresolved.
     */
    public boolean isUnresolved() {
        return unresolved;
    }    
   
    /**
     * Setter for property unresolved.
     * @param unresolved New value of property unresolved.
     */
    public void setUnresolved(boolean unresolved) {
        this.unresolved = unresolved;
    }
    
    /** 
     * code added for princeton enhancement case#2802
     * This method used by InboxDetailForm to check if the row can be
     * deleted.
     * @return boolean
     */
    public boolean canResolved(){
        int count=tblInbox.getRowCount();
        boolean value = true;
        InboxBean inbox ;
        if(count >0){
            int[] selectedRow=tblInbox.getSelectedRows();
            for(int row = 0 ; row < selectedRow.length ; row++){
                // Modified for the case # COEUSQA-2073 - Improve management of Coeus Inbox - Ability to reorder columns in Inbox - start
                //String numValue=(String) tblInbox.getValueAt(selectedRow[row], 12);
                String numValue=(String) tblInbox.getValueAt(selectedRow[row], 24);
                // Modified for the case # COEUSQA-2073 - Improve management of Coeus Inbox - Ability to reorder columns in Inbox - end
                Integer indexObject=new Integer(numValue);
                int index=indexObject.intValue();
                inbox = (InboxBean) newInboxVector.get( index );
                if(inbox.getModuleCode() == 4){
                    try{
                        SubContractAmountReleased bean = new SubContractAmountReleased();
                        bean.setSubContractCode(inbox.getItem());
                        String message = ((MessageBean) inbox.getMessageBean()).getMessage();
                        bean.setSequenceNumber(Integer.parseInt(message.substring(0, message.indexOf("|"))));
                        message = message.substring(message.indexOf("|")+1);
                        bean.setLineNumber(Integer.parseInt(message.substring(0, message.indexOf("|"))));
                        bean = getInvoiceData(bean);
                        if(bean.getStatusCode() != null && !bean.getStatusCode().equals("A")
                            && !bean.getStatusCode().equals("R")){
                            value = false;
                            break;
                        }
                    } catch(Exception e){
                            CoeusOptionPane.showErrorDialog("Invalid Message");
                    }
                }
                //Added for Case#3682 - Enhancements related to Delegations - Start
                else if(inbox.getModuleCode() == 0){
                    if(inbox.getCreationStatus() == 1){
                         value = false;
                    }
                }
                //Added for Case#3682 - Enhancements related to Delegations - End                
            }
        }
        return value;
    }
    
    
    /**
     * code added for princeton enhancement case#2802
     * To get the status of the particular invoice
     * @param subContractAmountReleased 
     * @return SubContractAmountReleased bean
     */
    public SubContractAmountReleased getInvoiceData(SubContractAmountReleased subContractAmountReleased){
       // CoeusVector cvAmountInfo = null;
        RequesterBean requesterBean = new RequesterBean();
        requesterBean.setFunctionType('i');
        requesterBean.setDataObject(subContractAmountReleased);
        AppletServletCommunicator comm = new AppletServletCommunicator(
                CoeusGuiConstants.CONNECTION_URL + SUBCONTRACT_SERVLET, requesterBean);
        comm.send();
        ResponderBean responderBean = comm.getResponse();
        if(!responderBean.isSuccessfulResponse()){
            CoeusOptionPane.showErrorDialog(responderBean.getMessage());
        }
        if(responderBean.getDataObject() != null){
            subContractAmountReleased = (SubContractAmountReleased) responderBean.getDataObject();
        }
        return subContractAmountReleased;
    }
    //Added for case#4293-Click to launch protocol from unresolved message in Inbox in premium -start
    /**
     *This method get protocol detail, by passing protocol number
     *@param String protocolNumber
     */
//    private ProtocolInfoBean getProtocolDetails(String protocolNumber)throws CoeusException{
//        ProtocolInfoBean infoBean = new ProtocolInfoBean();
//        if (protocolNumber != null && protocolNumber.trim().length() > 0 && !protocolNumber.equals("")) {
//            String connectTo = CoeusGuiConstants.CONNECTION_URL + "/protocolMntServlet";
//            RequesterBean requester = new RequesterBean();
//            requester.setFunctionType('f');
//            requester.setDataObject(protocolNumber);
//            AppletServletCommunicator comm = new AppletServletCommunicator(connectTo, requester);
//            comm.send();
//            ResponderBean response = comm.getResponse();
//            if(!response.isSuccessfulResponse()){
//                throw new CoeusException(response.getMessage(), 1);
//            }else{
//                infoBean = (ProtocolInfoBean)response.getDataObject();
//            }
//        }
//        return infoBean;
//    }
    
    /**
     * The function to check whether the user has rights to view the IRB protocol.
     */
      private boolean hasDisplayIRBProtocolRights(String protocolNo) {
      
        boolean hasRight = false;
//        int selectedRow = tblInbox.getSelectedRow();
//        if(selectedRow != -1){
            // Modified for the case # COEUSQA-2073 - Improve management of Coeus Inbox - Ability to reorder columns in Inbox - start
            //String protocolNo = (String)tblInbox.getValueAt(selectedRow,4);
//            String protocolNo = (String)tblInbox.getValueAt(selectedRow,4);
            // Modified for the case # COEUSQA-2073 - Improve management of Coeus Inbox - Ability to reorder columns in Inbox - end
            
//            Vector dataObjects = null;
            
            String PROTOCOL_SERVLET = "/protocolMntServlet";
            
            String connectTo = CoeusGuiConstants.CONNECTION_URL+ PROTOCOL_SERVLET;
            RequesterBean request = new RequesterBean();
            request.setFunctionType('D');
            request.setId(protocolNo);
            AppletServletCommunicator comm
                    = new AppletServletCommunicator(connectTo, request);
            comm.send();
            ResponderBean response = comm.getResponse();
            if (response == null) {
                response = new ResponderBean();
                response.setResponseStatus(false);
                response.setMessage(coeusMessageResources.parseMessageKey(
                        "server_exceptionCode.1000"));
            }
            if (response.isSuccessfulResponse()) {
                hasRight = true;
            }else {
                if(response.getDataObject() != null ){
                    Object obj = response.getDataObject();
                    if(obj instanceof CoeusException){
                        CoeusOptionPane.showErrorDialog( ( (CoeusException)obj ).getMessage() );
                    }else{
                        CoeusOptionPane.showErrorDialog( response.getMessage() );
                    }//End of inner else
                }
            }//End of outer else
//        }//End of outer if
        //Bug Fix 2083 End 4
        
        return hasRight;
    }
      
      /**
       * The function to check whether the user has rights to view the IACUC protocol.
       */
      private boolean hasDisplayIACUCProtocolRights(String protocolNumber) {
          
          boolean hasRight = false;
          String connectTo = CoeusGuiConstants.CONNECTION_URL+ "/IacucProtocolServlet";
          RequesterBean request = new RequesterBean();
          request.setFunctionType('D');
          request.setId(protocolNumber);
          AppletServletCommunicator comm = new AppletServletCommunicator(connectTo, request);
          comm.send();
          ResponderBean response = comm.getResponse();
          if (response == null) {
              response = new ResponderBean();
              response.setResponseStatus(false);
              response.setMessage(coeusMessageResources.parseMessageKey(
                      "server_exceptionCode.1000"));
          }
          if (response.isSuccessfulResponse()) {
              hasRight = true;
          }else {
              if(response.getDataObject() != null ){
                  Object obj = response.getDataObject();
                  if(obj instanceof CoeusException){
                      CoeusOptionPane.showErrorDialog( ( (CoeusException)obj ).getMessage() );
                  }else{
                      CoeusOptionPane.showErrorDialog( response.getMessage() );
                  }//End of inner else
              }
          }
          return hasRight;
      }
      //Added for case#4293-Click to launch protocol from unresolved message in Inbox in premium -end
      
      //Added for COEUSDEV-299 : double click is no longer working to open protocol from inbox - Start
      /**
       * Method to open the Subcontract module for the given subcontract code
       * @param mode
       * @param refId
       * @throws java.lang.Exception
       */
      private void showSubcontract(char mode,String refId )throws Exception {
          try{
              CoeusVector cvAmountInfo = getSubcontractAmountData(refId);
              if(cvAmountInfo == null){
                  cvAmountInfo = new CoeusVector();
              }
              CoeusVector cvData = new CoeusVector();
              cvData.add(new Double(cvAmountInfo.sum("obligatedAmount")));
              cvData.add(new Double(cvAmountInfo.sum("anticipatedAmount")));
              cvData.add(new Double(0));
              cvData.add(new Double(0));
              
              try {
                  if (authorizationCheck("VIEW_SUBCONTRACT")){
                      SubContractBean subContractBean = new SubContractBean();
                      subContractBean.setSubContractCode(refId);
                      SubcontractBaseWindowController subcontractBaseWindowController =
                              new SubcontractBaseWindowController("Display Subcontract ", CoeusGuiConstants.DISPLAY_MODE, subContractBean, cvData);
                      subcontractBaseWindowController.display();
                  }else{
                      CoeusOptionPane.showInfoDialog("The user has no rights to display this subcontract");
                  }
              }catch(CoeusClientException coeusClientException) {
                  CoeusOptionPane.showDialog(coeusClientException);
              }
          }catch ( Exception ex) {
              
              try{
                  throw new Exception(ex.getMessage());
              }catch (Exception excep){
                  throw new Exception(excep.getMessage());
              }
          }
      }
      
      /**
       * Method to check that the user has Subcontract module
       * Modify/View rights
       * @param right
       * @return boolean
       */
      private boolean authorizationCheck(String right) {
          RequesterBean requester;
          ResponderBean responder;
          boolean subcontractRight = false;
          requester = new RequesterBean();
          Hashtable authorizations = new Hashtable();
          
          AuthorizationBean authorizationBean;
          AuthorizationOperator authorizationOperator;
          //String MODIFY_SUBCONTRACT = "MODIFY_SUBCONTRACT";
          String VIEW_SUBCONTRACT = "VIEW_SUBCONTRACT";
          
          if(right != null && right.endsWith(VIEW_SUBCONTRACT)){
              // Determine whether user has right to display an subcontract
              authorizationBean = new AuthorizationBean();
              authorizationBean.setFunction(VIEW_SUBCONTRACT);
              authorizationBean.setFunctionType("RIGHT_ID");
              authorizationBean.setPerson(CoeusGuiConstants.getMDIForm().getUserId());
              authorizationOperator = new AuthorizationOperator(authorizationBean);
              authorizations.put(VIEW_SUBCONTRACT, authorizationOperator);
          }
          
          requester.setAuthorizationOperators(authorizations);
          requester.setIsAuthorizationRequired(true);
          
          AppletServletCommunicator comm = new AppletServletCommunicator(CoeusGuiConstants.CONNECTION_URL + AUTH_SERVLET, requester);
          
          comm.send();
          responder = comm.getResponse();
          if(responder.isSuccessfulResponse()){
              authorizations = responder.getAuthorizationOperators();
              subcontractRight = ((Boolean)authorizations.get(right)).booleanValue();
          }else{
              CoeusOptionPane.showInfoDialog(responder.getMessage());
          }
          return subcontractRight;
      }
      
      /**
       * Method to get the Subcontract Amount info datas for the given subcontract code.
       * @param subcontractcode
       * @return CoeusVector
       */
      public CoeusVector getSubcontractAmountData(String subcontractcode){
          CoeusVector cvAmountInfo = null;
          RequesterBean requesterBean = new RequesterBean();
          requesterBean.setFunctionType('a');
          requesterBean.setDataObject(subcontractcode);
          AppletServletCommunicator comm = new AppletServletCommunicator(
                  CoeusGuiConstants.CONNECTION_URL + SUBCONTRACT_SERVLET, requesterBean);
          comm.send();
          ResponderBean responderBean = comm.getResponse();
          if(!responderBean.isSuccessfulResponse()){
              CoeusOptionPane.showErrorDialog(responderBean.getMessage());
          }
          if(responderBean.getDataObject() != null){
              cvAmountInfo = (CoeusVector) responderBean.getDataObject();
          }
          return cvAmountInfo;
      }
      //COEUSDEV-299 : End
      
      // Added for the case # COEUSQA-2073 - Improve management of Coeus Inbox - Ability to reorder columns in Inbox - start
      // Modified for COEUSDEV-612:Refresh and UI issues in inbox window in Premium
      /**
       * Method to get the create table.
       * @param inboxVector
       */
    private void buildTable(Vector inboxVector){

        tblInbox.setFont(CoeusFontFactory.getNormalFont());
        Vector disLabelList = buildTableColumns();
        Vector resRowList = buildTableData(inboxVector);
        
        
        /**
         * Updated For : Table Sorting Case Insensitive
         *
         * Updated by Subramanya Feb' 20 2003
         */
        //commented by nadh
       /*sorter = new TableSorter(
            new DefaultTableModel(resRowList,disLabelList){
                    public boolean isCellEditable(int row,int col){
                        return false;
                    }
                }, false ); //ADDED THIS
        sorter.addMouseListenerToHeaderInTable(tblSearchRes); //ADDED THIS */
        
        //Added for supporting multiple sorting - nadh - 18-01-2005
//        multiColumnSorter = new MultipleTableColumnSorter(new DefaultTableModel(resRowList,disLabelList){
//            public boolean isCellEditable(int row,int col){
//                return false;
//            }
//        });
//        tblSearchRes.setModel(multiColumnSorter);
//        multiColumnSorter.setTableHeader(tblSearchRes.getTableHeader());
        
        sorter = new TableSorter(new DefaultTableModel(resRowList,disLabelList){
            public boolean isCellEditable(int row,int col){
                return false;
            }
        });
        tblInbox.setModel(sorter);
        //sorter.addMouseListenerToHeaderInTable(tblInbox);
//        sorter.setTableHeader(tblSearchRes.getTableHeader());
        //end - 18-01-2005
        
        
        int tmpColCnt = inboxDataLabels.size();
        for( int colIndex = 0;colIndex<tmpColCnt-1;colIndex++){
            DisplayBean display = (DisplayBean)inboxDataLabels.elementAt(colIndex);
            TableColumn column = tblInbox.getColumnModel().getColumn(colIndex);
            if(!display.isVisible()){
//                TableHeader tblHeader = tblSearchRes.getTableHeader();
                column.setHeaderValue(" ");// Space given for the bug Fix#1609
                column.setMaxWidth(0);
                column.setMinWidth(0);
                column.setPreferredWidth(0);
//                column.setWidth(0);
            }else{
                column.setPreferredWidth(display.getSize());
            }
            //Case#2908 - Exports from Search Results Do Not Preserve Data Format  - Start
            column.setIdentifier(display);
            //Case#2908 - End
        }
        
        tblInbox.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        if(tblInbox.getColumnCount()>0){
            TableColumn clmName = tblInbox.getColumnModel().getColumn(
            tblInbox.getColumnCount()-1);
            clmName.setMaxWidth(0);
            clmName.setMinWidth(0);
            //clmName.setWidth(0);
            clmName.setPreferredWidth(0);
        }
        tblInbox.setRowHeight(20);
        tblInbox.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        ListSelectionModel rowSM = tblInbox.getSelectionModel();
        rowSM.addListSelectionListener(this);
        
        DefaultCellEditor editor =(DefaultCellEditor)tblInbox.getCellEditor();
        if (editor != null){
            editor.stopCellEditing();
        }
        //        tblSearchRes.setCellSelectionEnabled(true);
        tblInbox.setRowSelectionAllowed(true);
        tblInbox.getTableHeader().setReorderingAllowed(false);
        tblInbox.getTableHeader().setFont(CoeusFontFactory.getLabelFont());
        tblInbox.requestFocusInWindow();
        if(tblInbox.getRowCount() > 0){
            tblInbox.setRowSelectionInterval(0,0);
        }
//        tblSearchRes.addKeyListener(new KeyAdapter(){
//            public void keyReleased(KeyEvent kEvent){
//                coeusSearch.fireAction(kEvent.getKeyCode());
//            }
//        });
//        return tblSearchRes;
    }/*Get resolved and unresolved data and update the inbox bean
     **/
    /**
     * Method to get the updateed inboxBean.
     * @param inboxVector
     * @return JTable
     */
     private Vector updateInboxBean(Vector vecUnResolAndResolInboxList) throws Exception {
        
        Vector dataObjects = null;
        //MessageBean messageBean = new MessageBean();
        String connectTo = CoeusGuiConstants.CONNECTION_URL +
        PROPOSAL_ACTION_SERVLET;
        RequesterBean request = new RequesterBean();
        request.setFunctionType(GET_MESSAGE_FOR_INBOX);
        request.setDataObject(vecUnResolAndResolInboxList);
        AppletServletCommunicator comm
        = new AppletServletCommunicator( connectTo, request );
        comm.send();
        ResponderBean response = comm.getResponse();
        if (response == null) {
            response = new ResponderBean();
            response.setResponseStatus(false);
        }
        if (response.isSuccessfulResponse()) {
            dataObjects = response.getDataObjects();
            //messageBean = (MessageBean)dataObjects.get(0);
        } else {
            if (response.isLocked()) {
                
            }else {
                throw new Exception(response.getMessage());
            }
        }
        //System.out.println("IN GETINBOXDATA TO CHECK THE SIZE OF DATAOBJECTS"+ dataObjects.size() );
        return dataObjects;
    }
   // Modified for the case # COEUSQA-2073 - Improve management of Coeus Inbox - Ability to reorder columns in Inbox - end  
     
     // Added for COEUSDEV-612:Refresh and UI issues in inbox window in Premium
     /*
      * Method to build the column list
      */
     private Vector buildTableColumns(){
         Vector disLabelList = new Vector(3,2);
         int disColCnt = inboxDataLabels.size();
         for(int disColIndex=0;disColIndex<disColCnt;disColIndex++){
             DisplayBean display = (DisplayBean)inboxDataLabels.elementAt(disColIndex);
             disLabelList.addElement(display.getValue());
         }
         disLabelList.addElement("index");
         return disLabelList;
     }
     
     // Added for COEUSDEV-612:Refresh and UI issues in inbox window in Premium
     /**
      * Method to build the table data list.
      */
     private Vector buildTableData(Vector inboxVector){
         Vector resRowList = new Vector(3,2);
         int disColCnt = inboxDataLabels.size();
         for(int k=0, dataSize = inboxVector.size(); k<dataSize; k++){
             HashMap searchResultRow = (HashMap)inboxVector.elementAt(k);
             Vector resultColumn = new Vector(3,2);
             for(int dispIndex=0;dispIndex<disColCnt;dispIndex++){
                 DisplayBean display = (DisplayBean)inboxDataLabels.elementAt(dispIndex);
                 String fieldName = display.getName();
                 String resValue = Utils.convertNull(searchResultRow.get(fieldName));
                 resultColumn.addElement(Utils.convertNull(resValue));
             }
             resultColumn.addElement(""+k);
             resRowList.addElement(resultColumn);
         }
         return resRowList;
     }
     
     /**
      * Method to refresh the UI
      */
     public void refresh(){
         setTableDetails(inboxDataVector);
     }
     // COEUSDEV-612:End
    
}
