/*
 * AmendmentRenewalList.java
 *
 * Created on August 21, 2003, 5:28 PM
 */
/* PMD check performed, and commented unused imports and variables on 16-JULY-2010
 * by Md.Ehtesham Ansari
 */
package edu.mit.coeus.iacuc.gui;

import edu.mit.coeus.gui.CoeusDlgWindow;
import java.awt.Dimension;
import java.awt.Toolkit;
//import javax.swing.JPanel;
//import javax.swing.border.TitledBorder;
import javax.swing.table.TableColumn;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.ListSelectionEvent;
//import java.awt.event.ActionListener;
//import java.awt.event.ActionEvent;
import java.awt.event.*;

import java.util.Vector;
import java.util.Enumeration;
import java.util.HashMap;

import javax.swing.table.DefaultTableModel;
//import javax.swing.table.DefaultTableCellRenderer;
//import javax.swing.table.TableColumn;

import edu.mit.coeus.iacuc.bean.*;
import edu.mit.coeus.brokers.*;
import edu.mit.coeus.exception.*;

import edu.mit.coeus.gui.CoeusFontFactory;
import edu.mit.coeus.gui.CoeusMessageResources;
import edu.mit.coeus.gui.CoeusInternalFrame;
import edu.mit.coeus.gui.CoeusAppletMDIForm;
import edu.mit.coeus.utils.*;

//import edu.mit.coeus.utils.CoeusGuiConstants;
//import edu.mit.coeus.utils.CoeusOptionPane;
//import edu.mit.coeus.utils.CoeusDateFormat;
/**
 *
 * @author  ravikanth
 */
public class AmendmentRenewalList extends javax.swing.JComponent 
    implements ActionListener {
    
    private Vector amendRevList;
    private CoeusMessageResources messageResource;
    private char functionType;
    private Vector columnNames;
    private VersionTableModel tableModel;
    private String protocolId;
    private HashMap hmAmendRenewal = new HashMap();
    private CoeusAppletMDIForm mdiForm = CoeusGuiConstants.getMDIForm();
    
    private final char CHECK_IF_EDITABLE = 'C' ;
    //Added for COEUSDEV-86 : Questionnaire for a Submission  - Start
    private static final String AMEND_RENEW_APPROVED = "Approved";
    //Added for COEUSDEV-86 : Questionnaire for a Submission  - End
    private static final String AMENDMENT_TYPE = "Amendment";
    private static final String RENEWAL_TYPE = "Renewal";
    private static final String RENEWAL_WITH_AMEND_TYPE = "Renewal/Amendment";
    /*COEUSQA-1724-Constants Added for new Amendment/Renewal type Title*/
    private static final String CONTINUATION_RENEWAL_TYPE = "Continuation/Continuing Review";
    private static final String CONTINUATION_RENEWAL_WITH_AMEND_TYPE = "Continuation/Continuing Review with Amendment";
    
    /** Creates new form AmendmentRenewalList */
    public AmendmentRenewalList( Vector amendRevList, char functionType ) {
        this.amendRevList = amendRevList;
        messageResource = CoeusMessageResources.getInstance();
        initComponents();
        
         //Added by Amit 11/18/2003
        if(functionType == CoeusGuiConstants.DISPLAY_MODE){

            java.awt.Color bgListColor = (java.awt.Color)javax.swing.UIManager.getDefaults().get("Panel.background");
        
            tblVersions.setBackground(bgListColor);    
            /*tblVersions.setSelectionBackground(bgListColor );
            tblVersions.setSelectionForeground(java.awt.Color.BLACK); */
            txtArSummary.setBackground(bgListColor);    
        }
        else{
            tblVersions.setBackground(java.awt.Color.white);            
            /*tblVersions.setSelectionBackground(java.awt.Color.white);
            tblVersions.setSelectionForeground(java.awt.Color.black); */
            txtArSummary.setBackground(java.awt.Color.white);            
        }
        //end Amit 
        
        // Added by Chandra 12/09/2003
        java.awt.Component[] components = {tblVersions,btnEdit,btnView,txtArSummary};
        ScreenFocusTraversalPolicy  traversePolicy = new ScreenFocusTraversalPolicy( components );
        pnlMain.setFocusTraversalPolicy(traversePolicy);
        pnlMain.setFocusCycleRoot(true);
        // End Chandra
        
        tableModel = new VersionTableModel(
                    ((DefaultTableModel)tblVersions.getModel()).getDataVector(),
                    getColumnNames());
        tblVersions.setModel(tableModel);
        //Added for COEUSDEV-86 : Questionnaire for a Submission  - Start
        //To view the approved amendment/renewal details
        btnDetails.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                showAmendRenewDetails();
                
            }
        });
        //COEUSDEV-86 : End
        tblVersions.getSelectionModel().addListSelectionListener(
            new ListSelectionListener() {
                public void valueChanged(ListSelectionEvent listSelectionEvent ) {
                    String protocolNo = protocolId + 
                        tableModel.getSelectedModuleType() + 
                        tableModel.getSelectedVersionNumber();
                    ProtocolAmendRenewalBean amendBean = 
                        (ProtocolAmendRenewalBean)hmAmendRenewal.get(protocolNo);
                    //Added for COEUSDEV-86 : Questionnaire for a Submission  - Start
                    //When status description is Approved, edit and view buttons are disabled, details buttons is enabled
                    if(amendBean != null){
                        String description = amendBean.getProtocolStatusDescription();
                        if(description != null && description.equalsIgnoreCase(AMEND_RENEW_APPROVED)){
                            btnView.setEnabled(false);
                            btnEdit.setEnabled(false);
                            btnDetails.setEnabled(true);
                        }else{
                            btnView.setEnabled(true);
                            btnEdit.setEnabled(true);
                            btnDetails.setEnabled(false);
                        }
                    }
                    //COEUSDEV-86 : End 
                    if( amendBean != null ) {
                        txtArSummary.setText( amendBean.getSummary() );
                    }else{
                        txtArSummary.setText(null);
                    }
                    txtArSummary.setCaretPosition(0);
                }
        });
        
        btnEdit.addActionListener( this );
        btnView.addActionListener( this );
        
        //raghuSV: to show the dialog windown upon double click 
        //starts...
        tblVersions.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent me) {
                if (me.getClickCount() == 2) {
                    
                    //Added for COEUSDEV-86 : Questionnaire for a Submission  - Start
//                    showProtocolDetails(CoeusGuiConstants.DISPLAY_MODE);
                    String amendDescription = (String)tblVersions.getValueAt(tblVersions.getSelectedRow(),2);
                    if(amendDescription != null && amendDescription.equalsIgnoreCase(AMEND_RENEW_APPROVED)){
                        showAmendRenewDetails();
                    }else{
                        showProtocolDetails(CoeusGuiConstants.DISPLAY_MODE);
                    }
                    //COEUSDEV-86 : End
                }
            }
        });
        //ends
        
        setValues( amendRevList );
    }
    
    public void setValues( Vector amendRevList ) {
        this.amendRevList = amendRevList;
        hmAmendRenewal.clear();
        if( amendRevList != null && amendRevList.size() > 0 ) {
            tableModel.setDataBeans( amendRevList );
            tblVersions.setRowSelectionInterval(0,0);
          
        }else{
            setColumnWidths();
        }
        if( tblVersions.getRowCount() == 0 ) {
            btnEdit.setEnabled(false);
            btnView.setEnabled(false);
            btnDetails.setEnabled(false);
        }else{
            //Added for COEUSDEV-86 : Questionnaire for a Submission  - Start
            String amendDescription = (String)tblVersions.getValueAt(0,2);
            if(amendDescription != null && amendDescription.equalsIgnoreCase(AMEND_RENEW_APPROVED)){
                btnView.setEnabled(false);
                btnEdit.setEnabled(false);
                btnDetails.setEnabled(true);
                
            }else{
                btnView.setEnabled(true);
                btnEdit.setEnabled(true);
                btnDetails.setEnabled(false);
            }
            //COEUSDEV-86 : End
        }
    }
    private void setColumnWidths() {
        TableColumn column = tblVersions.getColumnModel().getColumn(0);
        //column.setMaxWidth(100);
        column.setMinWidth(130);
        //column.setPreferredWidth(0);
        
        column = tblVersions.getColumnModel().getColumn(1);
        //column.setMaxWidth(100);
        column.setMinWidth(100);
        //column.setPreferredWidth(100);        

        column = tblVersions.getColumnModel().getColumn(2);
        //column.setMaxWidth(180);
        column.setMinWidth(100);

        column = tblVersions.getColumnModel().getColumn(3);
        //column.setMaxWidth(180);
        column.setMinWidth(165);
        //column.setPreferredWidth(180);        
        tblVersions.getTableHeader().setFont(CoeusFontFactory.getLabelFont());
        tblVersions.getTableHeader().setReorderingAllowed(false);
    }
    
    
    public void actionPerformed( ActionEvent ae ) {
        Object source = ae.getSource();
        if ( source.equals( btnEdit ) ){
            showProtocolDetails(CoeusGuiConstants.MODIFY_MODE);
        }else if ( source.equals( btnView ) ){
            showProtocolDetails(CoeusGuiConstants.DISPLAY_MODE);
        }
    }
    private void showProtocolDetails(char fnType ) {
        int selRow = tblVersions.getSelectedRow();
        boolean showProtocol = true;
        try{
/*            ProtocolAmendRenewalBean amendRenBean = tableModel.getSelectedBean();
            if( amendRenBean != null && amendRenBean.getProtocolNumber()!= null) {
                throw new Exception(
                    messageResource.parseMessageKey("amendRenewalList_exceptionCode.240903"));
            }*/
            if( fnType == CoeusGuiConstants.MODIFY_MODE ) {
                if( !isStatusOkForEditing() ){
                    showProtocol = false;
                }
            }
            if( selRow != -1 && showProtocol) {
                checkDuplicateAndShow(
                    getModuleCode(tableModel.getSelectedModuleType()),fnType);
            }
        }catch(Exception e) {
            CoeusOptionPane.showInfoDialog(e.getMessage());
        }
    
    }
    
    /**
     * This method is used to check whether the given protocol number is already 
     * opened in the given mode or not. 
     */
    private void checkDuplicateAndShow(int moduleCode, char mode)throws Exception {
        boolean duplicate = false;
        String moduleName = "";
        String refId = "";
        try{
            if( moduleCode == CoeusGuiConstants.PROTOCOL_AMENDMENT_CODE ) {
                moduleName = CoeusGuiConstants.IACUC_AMENDMENT_DETAILS_TITLE;
            }else if( moduleCode == CoeusGuiConstants.PROTOCOL_RENEWAL_CODE ) {
                moduleName = CoeusGuiConstants.IACUC_RENEWAL_DETAILS_TITLE;
            }
            /*COEUSQA-1724-New Condition Added to check the module code for new Amendment/Renewal type - Start*/
            else if( moduleCode == CoeusGuiConstants.PROTOCOL_AMENDMENT_WITH_RENEWAL_CODE) {
                moduleName = CoeusGuiConstants.IACUC_RENEWAL_WITH_AMENDMENT_TITLE;
            }else if( moduleCode == CoeusGuiConstants.PROTOCOL_CONTINUING_WITH_RENEWAL_CODE) {
                moduleName = CoeusGuiConstants.IACUC_CONTINUATION_RENEWAL_DETAILS_TITLE;
            }else if( moduleCode == CoeusGuiConstants.PROTOCOL_CONTINUING_WITH_RENEWAL_AMEND_CODE) {
                moduleName = CoeusGuiConstants.IACUC_CONTINUATION_RENEWAL_AMEND_DETAILS_TITLE;
            }
            /*COEUSQA-1724-New Condition Added to check the module code for new Amendment/Renewal type - End*/
            refId = protocolId + " Version "+ tableModel.getSelectedVersionNumber();
            duplicate = mdiForm.checkDuplicate(moduleName, refId, mode );
                
        }catch(Exception e){
            /* Exception occured.  Record may be already opened in requested mode
               or if the requested mode is edit mode and application is already
               editing any other record. */
            duplicate = true;
            if(e.getMessage().length() > 0 ) {
                CoeusOptionPane.showInfoDialog(e.getMessage());
            }
            /* try to get the requested frame which is already opened */
            CoeusInternalFrame frame = mdiForm.getFrame(moduleName,refId);
            if(frame == null){
                /* if no frame opened for the requested record then the 
                   requested mode is edit mode. So get the frame of the
                   editing record. */
                frame = mdiForm.getEditingFrame( moduleName );
            }
            if (frame != null){
                frame.setSelected(true);
                frame.setVisible(true);
            }
            return;
        }
        ProtocolDetailForm detailForm = null;
        try{
            String protocolNo = constructNewProtocolNumber();
            detailForm = new ProtocolDetailForm(protocolNo, mode, moduleCode);
            detailForm.showDialogForm();
        }catch ( Exception ex) {
            ex.printStackTrace();
            try{
                if (!detailForm.isModifiable() ) {
                    String msg = messageResource.parseMessageKey(ex.getMessage());
                    int resultConfirm = CoeusOptionPane.showQuestionDialog(msg,
                    CoeusOptionPane.OPTION_YES_NO,
                    CoeusOptionPane.DEFAULT_YES);
                    if (resultConfirm == 0) {
                        showProtocolDetails(CoeusGuiConstants.DISPLAY_MODE);
                    }
                }else {
                    throw new Exception(ex.getMessage());
                }
            }catch (Exception excep){
                excep.printStackTrace();
                throw new Exception(excep.getMessage());
            }
        }
    }
    
    private int getModuleCode(String protocolNo ) {
        int moduleCode = 0;
        if( protocolNo.indexOf(CoeusConstants.IACUC_AMENDMENT) != -1 ) {
            moduleCode = CoeusGuiConstants.PROTOCOL_AMENDMENT_CODE;
        }else if( protocolNo.indexOf(CoeusConstants.IACUC_RENEWAL) != -1 ){
            moduleCode = CoeusGuiConstants.PROTOCOL_RENEWAL_CODE;
        }else if( protocolNo.indexOf(CoeusConstants.IACUC_RENEWAL_WITH_AMENDMENT) != -1 ){
            moduleCode = CoeusGuiConstants.PROTOCOL_AMENDMENT_WITH_RENEWAL_CODE;
        }
        /*COEUSQA-1724-New Condition Added to get the module code for new Amendment/Renewal type - Start*/
        else if( protocolNo.indexOf(CoeusConstants.IACUC_CONTINUATION_CONTINUING_REVIEW) != -1 ){
            moduleCode = CoeusGuiConstants.PROTOCOL_CONTINUING_WITH_RENEWAL_CODE;
        }else if( protocolNo.indexOf(CoeusConstants.IACUC_CONTI_CONTINUING_REVIEW_WITH_AMEND) != -1 ){
            moduleCode = CoeusGuiConstants.PROTOCOL_CONTINUING_WITH_RENEWAL_AMEND_CODE;
        }
        /*COEUSQA-1724-New Condition Added to check the module code for new Amendment/Renewal type - End*/
        else{
            moduleCode = CoeusGuiConstants.PROTOCOL_DETAIL_CODE;
        }
        return moduleCode;
    }
    
    private String constructNewProtocolNumber() {
        StringBuffer sbNewProtocolNo = new StringBuffer(protocolId);
        String versionNo = tableModel.getSelectedVersionNumber();
        String moduleType = tableModel.getSelectedModuleType();
        sbNewProtocolNo.append(moduleType);
        sbNewProtocolNo.append(versionNo);
        return sbNewProtocolNo.toString();
    }
    
    //Added by Amit 11/21/2003
    /** This method use to implement focus on first editable component in this page.
     */
    public void setDefaultFocusForComponent(){
        if(!( functionType == CoeusGuiConstants.DISPLAY_MODE )) {            
        
            if(tblVersions.getRowCount() > 0 ) {
                tblVersions.requestFocusInWindow();
                tblVersions.setRowSelectionInterval(0, 0);
                tblVersions.setColumnSelectionInterval(0,0);
            }            
        }
    }    
    //end Amit      
    
    
    private boolean isStatusOkForEditing() {
        boolean statusOk = false ;
        String connectTo = CoeusGuiConstants.CONNECTION_URL+ "/IacucProtocolServlet";
        // connect to the database and get the formData for the given organization id
        RequesterBean request = new RequesterBean();
        
        request.setFunctionType(CHECK_IF_EDITABLE);
        request.setId(constructNewProtocolNumber());
        AppletServletCommunicator comm
        = new AppletServletCommunicator(connectTo, request);
        comm.send();
        ResponderBean response = comm.getResponse();
        statusOk = response.isSuccessfulResponse() ;
        System.out.println("** Server side validation for edit returned with " + statusOk) ;
        if (statusOk == true) {
            // continue with row lock check
            statusOk = true ;
        }
        else {
            if(response.getDataObject() != null){
                Object obj = response.getDataObject();
                if(obj instanceof CoeusException){
                    CoeusOptionPane.showDialog(new CoeusClientException((CoeusException)obj));
                }
            }else{
                CoeusOptionPane.showErrorDialog(response.getMessage()) ;
                statusOk = false ;
            }
        }
        
        return  statusOk ;
        
    }
    
    
    class VersionTableModel extends DefaultTableModel {
        public VersionTableModel(Vector data, Vector columnNames){
            super(data, columnNames);
        }
        public void setDataBeans(Vector vecBeans){
            Vector data = getData(vecBeans);
            setDataVector(data,columnIdentifiers);
            fireTableDataChanged();
            setColumnWidths();
        }
        protected Vector getData(Vector vecBeans) {
            Vector tableData = new Vector();
            if( vecBeans != null && vecBeans.size() > 0 ) {
                tableData = new Vector();
                int beanCount = vecBeans.size();
                ProtocolAmendRenewalBean amendBean = null;
                for(int beanIndex = 0; beanIndex < beanCount; beanIndex++) {
                    amendBean = (ProtocolAmendRenewalBean) vecBeans.elementAt(beanIndex);
                    tableData.addElement(constructTableRowData(amendBean));
                    hmAmendRenewal.put( amendBean.getProtocolAmendRenewalNumber(),amendBean );
                }
            }
            return tableData;
        }
        
        protected Vector constructTableRowData(ProtocolAmendRenewalBean amendBean ) {
            Vector rowData = new Vector();
            String versionNo = "",moduleType = "", createdTimestamp = "";
            String protocolNo = null;
            if( amendBean != null ) {
                protocolNo = amendBean.getProtocolAmendRenewalNumber();
                if( protocolNo.length() >= 14 ) {
                    versionNo = protocolNo.substring(11); 
                    protocolId = protocolNo.substring(0,10);
                    if( protocolNo.indexOf(CoeusConstants.IACUC_AMENDMENT) != -1 ) {
                        moduleType = AMENDMENT_TYPE;                        
                    }else if( protocolNo.indexOf(CoeusConstants.IACUC_RENEWAL) != -1) {
                        moduleType = RENEWAL_TYPE;                        
                    }else if(protocolNo.indexOf(CoeusConstants.IACUC_RENEWAL_WITH_AMENDMENT) != -1) {
                        moduleType = RENEWAL_WITH_AMEND_TYPE;
                    }
                    /*COEUSQA-1724-New Condition Added to check the module type for new Amendment/Renewal type - Start*/
                    else if(protocolNo.indexOf(CoeusConstants.IACUC_CONTINUATION_CONTINUING_REVIEW) != -1) {
                        moduleType = CONTINUATION_RENEWAL_TYPE;
                    }else if(protocolNo.indexOf(CoeusConstants.IACUC_CONTI_CONTINUING_REVIEW_WITH_AMEND) != -1) {
                        moduleType = CONTINUATION_RENEWAL_WITH_AMEND_TYPE;
                    }
                    /*COEUSQA-1724-New Condition Added to check the module type for new Amendment/Renewal type - End*/
                    //moduleType = ""+protocolNo.charAt(10);
                }
                createdTimestamp = CoeusDateFormat.format(
                            amendBean.getCreatedDate().toString() );                
                rowData.addElement(moduleType);
                rowData.addElement(versionNo);
                rowData.addElement( amendBean.getProtocolStatusDescription() );
                rowData.addElement(createdTimestamp);
            }
            return rowData;
        }
        public String getSelectedModuleType(){
            String selectedModuleType = "";
            int selRow = tblVersions.getSelectedRow();
            if( selRow != -1 && dataVector != null ){
                String moduleTypeCode = (String) getValueAt(selRow,0);
                if(AMENDMENT_TYPE.equals(moduleTypeCode)){
                    selectedModuleType = Character.toString(CoeusConstants.IACUC_AMENDMENT);
                }else if(RENEWAL_TYPE.equals(moduleTypeCode)){
                    selectedModuleType = Character.toString(CoeusConstants.IACUC_RENEWAL);
                }else if(RENEWAL_WITH_AMEND_TYPE.equals(moduleTypeCode)){
                    selectedModuleType = Character.toString(CoeusConstants.IACUC_RENEWAL_WITH_AMENDMENT);
                }
                /*COEUSQA-1724-New Condition Added to get the selected module type for new Amendment/Renewal type - Start*/
                else if(CONTINUATION_RENEWAL_TYPE.equals(moduleTypeCode)){
                    selectedModuleType = Character.toString(CoeusConstants.IACUC_CONTINUATION_CONTINUING_REVIEW);
                }else if(CONTINUATION_RENEWAL_WITH_AMEND_TYPE.equals(moduleTypeCode)){
                    selectedModuleType = Character.toString(CoeusConstants.IACUC_CONTI_CONTINUING_REVIEW_WITH_AMEND);
                }
                /*COEUSQA-1724-New Condition Added to get the selected module type for new Amendment/Renewal type - End*/
            }
            return selectedModuleType;
        }
        
        public String getSelectedVersionNumber(){
            String versionNumber = "";
            int selRow = tblVersions.getSelectedRow();
            if( selRow != -1 && dataVector != null ){
                versionNumber = (String) getValueAt(selRow,1);
            }
            return versionNumber;
        }
        
        public boolean isCellEditable(int row, int col) {
            return false;
        }
        
        public ProtocolAmendRenewalBean getSelectedBean(){
            int selRow = tblVersions.getSelectedRow();
            if( selRow != -1 && dataVector != null ){
                return ( ProtocolAmendRenewalBean )hmAmendRenewal.get(constructNewProtocolNumber());
            }
            return null;
        }
            
        
    }

    
    /**
     * This method is used to return the names of the columns used in the table
     * @return Collection of column names in the table used to show the versions
     * of the amendments and revisions.
     */
    private Vector getColumnNames() {
        if( columnNames == null ) {
            columnNames = new Vector(); 
            Enumeration enumColNames = tblVersions.getColumnModel().getColumns();
            while(enumColNames.hasMoreElements()){
                String strName = (String)((TableColumn)enumColNames.nextElement()).getHeaderValue();
                columnNames.addElement(strName);
            }
        }
        return columnNames;
    
    }
    
    //Added for COEUSDEV-86 : Questionnaire for a Submission  - Start
    /*
     * Method to open a window for amendment/renewal details
     *
     */
    private void showAmendRenewDetails(){
        try{
            String protocolNo = protocolId +
                    tableModel.getSelectedModuleType() +
                    tableModel.getSelectedVersionNumber();
            ProtocolAmendRenewalBean amendBean =
                    (ProtocolAmendRenewalBean)hmAmendRenewal.get(protocolNo);
            String title = "IACUC Protocol Amendment Details - "+amendBean.getProtocolAmendRenewalNumber();
            if(amendBean != null && amendBean.getProtocolAmendRenewalNumber()!= null &&
                    amendBean.getProtocolAmendRenewalNumber().indexOf(CoeusConstants.IACUC_RENEWAL) > -1){
                title = "IACUC Protocol Renewal Details - "+amendBean.getProtocolAmendRenewalNumber();
            }
            /*COEUSQA-1724-New Condition Added to show new Amendment/Renewal type Details - Start*/
            else if(amendBean != null && amendBean.getProtocolAmendRenewalNumber()!= null &&
                    amendBean.getProtocolAmendRenewalNumber().indexOf(CoeusConstants.IACUC_RENEWAL_WITH_AMENDMENT) > -1){
                title = "IACUC Protocol Renewal/Amendment Details - "+amendBean.getProtocolAmendRenewalNumber();
            }else if(amendBean != null && amendBean.getProtocolAmendRenewalNumber()!= null &&
                    amendBean.getProtocolAmendRenewalNumber().indexOf(CoeusConstants.IACUC_CONTINUATION_CONTINUING_REVIEW) > -1){
                title = "IACUC Protocol Continuation/Continuing Review Details - "+amendBean.getProtocolAmendRenewalNumber();
            }else if(amendBean != null && amendBean.getProtocolAmendRenewalNumber()!= null &&
                    amendBean.getProtocolAmendRenewalNumber().indexOf(CoeusConstants.IACUC_CONTI_CONTINUING_REVIEW_WITH_AMEND) > -1){
                title = "IACUC Protocol Continuation/Continuing Review with Amendment Details - "+amendBean.getProtocolAmendRenewalNumber();
            }
            /*COEUSQA-1724-New Condition Added to show new Amendment/Renewal type Details - End*/
            CoeusDlgWindow dlgProtoSubQuestionnaire= new CoeusDlgWindow(mdiForm, title, true);
            AmendmentRenewalDetailsForm amendRenewForm = new AmendmentRenewalDetailsForm(amendBean,dlgProtoSubQuestionnaire);
            dlgProtoSubQuestionnaire.setResizable(false);
            dlgProtoSubQuestionnaire.getContentPane().add(amendRenewForm);
            dlgProtoSubQuestionnaire.setFont(CoeusFontFactory.getLabelFont());
            dlgProtoSubQuestionnaire.setSize(800, 500);
            
            Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
            Dimension dlgSize = dlgProtoSubQuestionnaire.getSize();
            dlgProtoSubQuestionnaire.setLocation(screenSize.width/2 - (dlgSize.width/2),
                    screenSize.height/2 - (dlgSize.height/2));
            dlgProtoSubQuestionnaire.show();
        }catch(Exception ex){
            ex.printStackTrace();
        }
    }
    //COEUSDEV-86 : END
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc=" Generated Code ">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        pnlMain = new javax.swing.JPanel();
        scrPnVersions = new javax.swing.JScrollPane();
        tblVersions = new javax.swing.JTable();
        scrPnSummary = new javax.swing.JScrollPane();
        txtArSummary = new javax.swing.JTextArea();
        btnEdit = new javax.swing.JButton();
        btnView = new javax.swing.JButton();
        btnDetails = new javax.swing.JButton();

        setLayout(new java.awt.BorderLayout());

        pnlMain.setLayout(new java.awt.GridBagLayout());

        scrPnVersions.setBorder(new javax.swing.border.EtchedBorder());
        scrPnVersions.setMinimumSize(new java.awt.Dimension(500, 285));
        scrPnVersions.setPreferredSize(new java.awt.Dimension(500, 285));
        tblVersions.setFont(CoeusFontFactory.getNormalFont());
        tblVersions.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Type", "Version No.", "Status", "Created Date"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tblVersions.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
        scrPnVersions.setViewportView(tblVersions);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridheight = 5;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 0, 5);
        pnlMain.add(scrPnVersions, gridBagConstraints);

        scrPnSummary.setBorder(new javax.swing.border.TitledBorder(new javax.swing.border.EtchedBorder(), "Summary", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, CoeusFontFactory.getLabelFont()));
        scrPnSummary.setMinimumSize(new java.awt.Dimension(500, 105));
        scrPnSummary.setPreferredSize(new java.awt.Dimension(500, 105));
        txtArSummary.setEditable(false);
        txtArSummary.setFont(CoeusFontFactory.getNormalFont());
        txtArSummary.setLineWrap(true);
        scrPnSummary.setViewportView(txtArSummary);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(15, 5, 5, 0);
        pnlMain.add(scrPnSummary, gridBagConstraints);

        btnEdit.setFont(CoeusFontFactory.getLabelFont());
        btnEdit.setMnemonic('E');
        btnEdit.setText("Edit");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.SOUTH;
        gridBagConstraints.insets = new java.awt.Insets(15, 5, 0, 5);
        pnlMain.add(btnEdit, gridBagConstraints);

        btnView.setFont(CoeusFontFactory.getLabelFont());
        btnView.setMnemonic('V');
        btnView.setText("View");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTH;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 0, 5);
        pnlMain.add(btnView, gridBagConstraints);

        btnDetails.setFont(CoeusFontFactory.getLabelFont());
        btnDetails.setText("Details");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 0, 5);
        pnlMain.add(btnDetails, gridBagConstraints);

        add(pnlMain, java.awt.BorderLayout.CENTER);

    }// </editor-fold>//GEN-END:initComponents
    
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnDetails;
    private javax.swing.JButton btnEdit;
    private javax.swing.JButton btnView;
    private javax.swing.JPanel pnlMain;
    private javax.swing.JScrollPane scrPnSummary;
    private javax.swing.JScrollPane scrPnVersions;
    private javax.swing.JTable tblVersions;
    private javax.swing.JTextArea txtArSummary;
    // End of variables declaration//GEN-END:variables
    
}
