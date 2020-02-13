/*
 * ProtocolSummaryForm.java
 *
 * Created on June 26, 2003, 2:05 PM
 */

package edu.mit.coeus.irb.gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.event.*;
import java.util.*;
import javax.swing.table.*;
import edu.mit.coeus.gui.CoeusDlgWindow;
import edu.mit.coeus.gui.CoeusFontFactory;
import edu.mit.coeus.gui.CoeusMessageResources;
import edu.mit.coeus.utils.*;
import edu.mit.coeus.brokers.*;
import edu.mit.coeus.irb.bean.*;

/**
 *
 * @author  ravikanth
 */
public class ProtocolSummaryForm extends CoeusDlgWindow
implements ListSelectionListener, ActionListener{
    private ProtocolInfoBean protocolInfoBean;
    private ProtocolSubmissionInfoBean submissionBean;
    private String protocolId;
    
    private boolean showSubmission=true;
    private JTable tblProtocolList;
    private HashMap hmProtocolDetails = new HashMap();
    private Vector invColumnNames, keyColumnNames,revColNames;
    private DefaultTableModel emptyInvModel,emptyKeyModel, emptyRevModel;
    private boolean allowNavigation;
    private DateUtils dtUtils = new DateUtils();
    
    /* holds the Data Vector for person entries. each object contains a vector
     * of ProtocolInvestigatorsBean data objects
     */
    private Vector personData;
    
    /* holds complete Investigator data with key as personId and
     * ProtocolInvestigatorBean as value */
    private Hashtable investigatorData;
    
    /* holds complete Unit details for each investigator with personId as Key
     * and ProtocolInvestigatorUnitsBean as value */
    private Hashtable unitHashData;
    
    private Vector investigators;
    private final String PROTOCOL_SERVLET = "/protocolMntServlet";
    private final char BRIEF_SUMMARY = 'B';
    private int selectedRow = -1;
    private int protocolRowCount = 0;
    private Dimension subDimension;
    private IconRenderer iconRenderer = new IconRenderer();
    private EmptyHeaderRenderer emptyHeaderRenderer = new EmptyHeaderRenderer();
    private EmptyButtonRenderer emptyButtonRenderer = new EmptyButtonRenderer();
    private String connectTo = CoeusGuiConstants.CONNECTION_URL + PROTOCOL_SERVLET;
    private RequesterBean request = new RequesterBean();
    private AppletServletCommunicator comm;
    private ResponderBean response;
    /** holds the values to be displayed in reveiwers table */
    private Vector revTableData = new Vector();
    private Vector aorListData = new Vector();
    private Vector unitTableData = new Vector();
    private Vector unitDataBean = new Vector();
    private TableColumn column;
    private PIRenderer piRenderer = new PIRenderer();
    private CoeusMessageResources messageResources;
    private Vector emptyVector = new Vector();
    /** Creates new form ProtocolSummaryForm */
    public ProtocolSummaryForm(String protocolNumber, boolean allowNavigation) {
        super(CoeusGuiConstants.getMDIForm(), "Protocol Summary",true);
        this.protocolId = protocolNumber;
        this.allowNavigation = allowNavigation;
        messageResources = CoeusMessageResources.getInstance();
    }
    
    public void setProtocolTable( JTable tblProtocolSheet ) {
        this.tblProtocolList = tblProtocolSheet;
    }
    public void showForm() {
        iconRenderer.setSelectedRowIcon(new ImageIcon( getClass().getClassLoader().getResource(
        CoeusGuiConstants.HAND2_ICON )));
        getContentPane().add( createForm() );
        setResizable(false);
        setValues();
        pack();
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        Dimension dlgSize = getSize();
        setLocation(screenSize.width/2 - (dlgSize.width/2),
        screenSize.height/2 - (dlgSize.height/2));
        addKeyListener(new KeyAdapter(){
            public void keyReleased(KeyEvent ke){
                if(ke.getKeyCode() == KeyEvent.VK_ESCAPE){
                    dispose();
                }
            }
        });
        
        addWindowListener( new WindowAdapter() {
            public void windowClosing(WindowEvent we ) {
                //System.exit(0);
                dispose();
            }
        });
        lstUnits.setCellRenderer( new UnitListCellRenderer());
        show();
    }
    public JComponent createForm(){
        initComponents();
        lblDescription.setVisible( !showSubmission );
        lblDescription.setText(messageResources.parseMessageKey(
        "protocolSummary_exceptionCode.7403"));
        btnClose.addActionListener(this);
        btnPrev.addActionListener(this);
        btnNext.addActionListener(this);
        //Code for new Enhancement Added by Vyjayanthi on 27/08/03
        //Start
        btnNotes.addActionListener(this);
        //End
        tblInv.getSelectionModel().addListSelectionListener( this );
        //Added by Vyjayanthi 21/12/2003 - Start
        java.awt.Component[] components = {btnClose, btnPrev, btnNext, btnNotes};
        ScreenFocusTraversalPolicy  traversePolicy = new ScreenFocusTraversalPolicy( components );
        pnlMain.setFocusTraversalPolicy(traversePolicy);
        pnlMain.setFocusCycleRoot(true);
        //End
        return pnlMain;
    }
    
    private void showSubmissionPanel( boolean show){
        lblDescription.setPreferredSize( pnlSubmissionDetails.getPreferredSize());
        pnlSubmissionDetails.setVisible( show );
        lblDescription.setVisible( !show );
        lblDescription.repaint();
        lblDescription.revalidate();
        scrPnReviewers.repaint();
        scrPnReviewers.revalidate();
        pnlSubmissionDetails.repaint();
        pnlSubmissionDetails.revalidate();
    }

        private void setValues(){
        // to reset data while navigating
            Boolean isPresent;
            boolean isAmmendmentRenewalPresent = false;
        protocolInfoBean = null;
        submissionBean = null;
        if( protocolId != null && protocolId.trim().length() > 0 ) {
            HashMap data = getValues();
              if( data != null ) {
                protocolInfoBean = ( ProtocolInfoBean ) data.get("PROTOCOL_DETAILS");
                submissionBean = ( ProtocolSubmissionInfoBean )data.get("SUBMISSION_DETAILS");
                isPresent = (Boolean)data.get("AMMENDMENT_RENEWAL_STATUS");
                isAmmendmentRenewalPresent = isPresent.booleanValue();
                if(isAmmendmentRenewalPresent){
                    txtArNote.setText(messageResources.parseMessageKey(
                    "protocolSummaryForm_IndicatorCode.2564"));
                    txtArNote.setEditable(false);
                    txtArNote.setForeground(Color.blue);
                }else{
                    txtArNote.setText("");
                    txtArNote.setEditable(false);
                }
                setFormData();
                setTableColumnWidths();
            }
            data = null;
        }
        if( tblProtocolList != null ) {
            selectedRow = tblProtocolList.getSelectedRow();
            protocolRowCount = tblProtocolList.getRowCount();
            if( allowNavigation && protocolRowCount-1 > selectedRow ) {
                btnNext.setEnabled( true );
            }else{
                btnNext.setEnabled( false );
            }
            if( allowNavigation && selectedRow > 0 ) {
                btnPrev.setEnabled( true );
            }else{
                btnPrev.setEnabled( false );
            }
            
        }else{
            btnNext.setEnabled( false );
            btnPrev.setEnabled( false );
        }
        
    }

    private HashMap getValues() {
        request.setFunctionType(BRIEF_SUMMARY);
        request.setId(protocolId);
        comm = new AppletServletCommunicator(connectTo, request);
        comm.send();
        response = comm.getResponse();
        if (response.isSuccessfulResponse()) {
            HashMap hashViewSummary = (HashMap)response.getDataObject();
            return hashViewSummary;
        } else {
            CoeusOptionPane.showErrorDialog(response.getMessage());
        }
        
        return null;
    }
    
    private void setFormData() {
        if( protocolInfoBean != null ) {
            lblProtocolValue.setText( protocolInfoBean.getProtocolNumber() );
            lblTypeValue.setText( protocolInfoBean.getProtocolTypeDesc() );
            lblStatusValue.setText( protocolInfoBean.getProtocolStatusDesc() );
            
            if ( protocolInfoBean.getApplicationDate() != null ){
                lblApplDateValue.setText(dtUtils.formatDate(
                protocolInfoBean.getApplicationDate().toString(),"dd-MMM-yyyy"));
            }else{
                lblApplDateValue.setText("");
            }
            
            if ( protocolInfoBean.getApprovalDate() != null ){
                lblApprovalDateValue.setText(dtUtils.formatDate(
                protocolInfoBean.getApprovalDate().toString(),
                "dd-MMM-yyyy"));
            }else{
                lblApprovalDateValue.setText("");
            }
            
            if ( protocolInfoBean.getExpirationDate() != null ){
                lblExprirationDateValue.setText(dtUtils.formatDate(
                protocolInfoBean.getExpirationDate().toString(),
                "dd-MMM-yyyy"));
            }else{
                lblExprirationDateValue.setText("");
            }
            txtArTitle.setText( protocolInfoBean.getTitle() );
            
            investigators = protocolInfoBean.getInvestigators();
            investigatorData = constructInvHashData( investigators );
            if( investigators != null && investigators.size() > 0){
                /* if investigators available then set the person table data */
                ((DefaultTableModel)tblInv.getModel()).setDataVector(personData,
                getInvColumnNames());
                if( tblInv.getRowCount() > 0 ) {
                    lstUnits.setListData( emptyVector );
                    //((DefaultListModel)lstUnits.getModel()).removeAllElements();
                    /* show the first investigators unit details */
                    String firstPerson =(tblInv.getValueAt( 0,3 ) == null ? ""
                    : tblInv.getValueAt( 0,3 ).toString());
                    if( ( firstPerson != null )&& (!firstPerson.equalsIgnoreCase("") )){
                        lstUnits.setListData( getUnitListData( firstPerson ) );
                        tblInv.addRowSelectionInterval( 0, 0 );
                    }
                    
                }
            }else{
                tblInv.setModel( getEmptyInvModel() );
                ((DefaultTableModel)tblInv.getModel()).fireTableDataChanged();
                lstUnits.setListData( emptyVector );
                //((DefaultListModel)lstUnits.getModel()).removeAllElements();
                
            }
            lstAOR.setListData( emptyVector );
            //((DefaultListModel)lstAOR.getModel()).removeAllElements();
            
            Vector aorData = protocolInfoBean.getAreaOfResearch();
            
            //lstAOR.removeAll();
            if( aorData != null && aorData.size() > 0) {
                lstAOR.setListData( getAORListData( aorData ) );
            }
            aorData = null;
            setKeyStudyData( protocolInfoBean.getKeyStudyPersonnel() );
            setSubmissionDetails();
            
        }
    }
    
    private void setSubmissionDetails(){
        if( submissionBean != null ) {
            showSubmission = true;
            subDimension = scrPnSubDetails.getPreferredSize();
            lblCommitteeIdValue.setText( submissionBean.getCommitteeId() );
            lblCommitteeNameValue.setText( submissionBean.getCommitteeName() );
            if( submissionBean.getScheduleDate() != null ) {
                lblSchDateValue.setText(dtUtils.formatDate(
                submissionBean.getScheduleDate().toString(),"dd-MMM-yyyy"));
            }else{
                lblSchDateValue.setText("");
            }
            lblSubmissionTypeValue.setText( submissionBean.getSubmissionTypeDesc() );
            lblReviewTypeValue.setText( submissionBean.getProtocolReviewTypeDesc() );
            lblSubStatusValue.setText( submissionBean.getSubmissionStatusDesc() );
            Vector reviewers = submissionBean.getProtocolReviewer();
            if( reviewers != null ) {
                ((DefaultTableModel)tblReviewers.getModel()).setDataVector(
                constructRevTableData(reviewers),getRevColumnNames());
            }else{
                tblReviewers.setModel(getEmptyReviewersModel());
            }
            reviewers = null;
        }else{
            showSubmission = false;
        }
        showSubmissionPanel( showSubmission );
    }
    
    
    private void setKeyStudyData( Vector keyStudyData ){
        Vector vcDataPopulate = new Vector();
        Vector vcData = null;
        ProtocolKeyPersonnelBean keyBean;
        if( (keyStudyData != null) && (keyStudyData.size()>0) ){
            int keyPersonnelSize = keyStudyData.size();
            for(int inCtrdata=0;inCtrdata < keyPersonnelSize;inCtrdata++){
                keyBean = (ProtocolKeyPersonnelBean)keyStudyData.get(inCtrdata);
                vcData= new Vector();
                vcData.addElement(keyBean.getPersonName());
                vcData.addElement(keyBean.getPersonRole());
                vcData.addElement(keyBean.getAffiliationTypeDescription());
                vcDataPopulate.addElement(vcData);
                vcData = null;
            }
            ((DefaultTableModel)tblKeyPersons.getModel()).setDataVector(
            vcDataPopulate,getKeyColumnNames());
            vcDataPopulate = null;
        }else{
            tblKeyPersons.setModel( getEmptyKeyModel());
        }
    }
    
    /**
     * Supporting method used to get the column names of selected reveviewers
     * table
     * @return  colNames vector which consists of column header names.
     */
    
    private Vector getRevColumnNames(){
        if( revColNames == null ) {
            Enumeration enumColumns
            = tblReviewers.getColumnModel().getColumns();
            revColNames = new Vector();
            while( enumColumns.hasMoreElements()){
                revColNames.addElement(((TableColumn)
                enumColumns.nextElement()).getHeaderValue().toString());
            }
        }
        return revColNames;
    }
    
    /**
     * Supporting method which constructs vector of vectors from
     * the collection of ProtocolReviewerInfoBean.
     *
     * @return  revTableData vector which will be used in displaying
     * reviewers table data
     */
    
    private Vector constructRevTableData(Vector reviewersList ) {
        ProtocolReviewerInfoBean reviewerBean;
        Vector tableRowData;
        int revSize = reviewersList.size();
        
        for( int rowIndex = 0 ; rowIndex < revSize; rowIndex++ ) {
            /* extract person name and reviewer type from bean and construct
             * double dimensional object array.
             */
            tableRowData = new Vector();
            reviewerBean = ( ProtocolReviewerInfoBean )
            reviewersList.get( rowIndex );
            tableRowData.addElement(reviewerBean.getPersonName());
            tableRowData.addElement(reviewerBean.getReviewerTypeDesc());
            revTableData.addElement(tableRowData);
            tableRowData = null;
        }
        return revTableData;
    }
    
    private Vector getAORListData( Vector aorData ) {
        int aorCount = aorData.size();
        ProtocolReasearchAreasBean researchBean;
        for( int indx = 0; indx < aorCount; indx++ ) {
            researchBean = ( ProtocolReasearchAreasBean ) aorData.elementAt(indx);
            aorListData.addElement( researchBean.getResearchAreaCode() + " : " +
            researchBean.getResearchAreaDescription() );
        }
        return aorListData;
    }
    
    //supporting method to construct hash table from the data vector(bean)
    private Hashtable constructInvHashData( Vector dataBean ){
        Hashtable newData = new Hashtable();
        if( dataBean == null || dataBean.size() == 0){
            return newData;
        }
        
        unitHashData = new Hashtable();
        personData = new Vector();
        
        ProtocolInvestigatorsBean protocolInvestigatorBean = new ProtocolInvestigatorsBean();
        
        Vector personTableRow;// = new Vector();
        String personId = null;
        Vector unDetail = new Vector();
        Boolean piVal = new Boolean(true);
        for( int indx = 0; indx < dataBean.size() ; indx ++ ){
            try{
                // construct table data from vector of investigator beans
                protocolInvestigatorBean = ( ProtocolInvestigatorsBean )
                dataBean.get( indx );
                if( protocolInvestigatorBean != null){
                    personId  = protocolInvestigatorBean.getPersonId();
                    /* insert each investigator bean into hastable with personId
                      as key and investigator bean as value */
                    newData.put( personId, protocolInvestigatorBean );
                    personTableRow = new Vector();
                    //personTableRow.removeAllElements();
                    personTableRow.add( "");
                    personTableRow.add(
                    protocolInvestigatorBean.getPersonName());
                    personTableRow.add(
                    protocolInvestigatorBean.getAffiliationTypeDescription());// for affiliation Type
                    personTableRow.add(personId);
                    piVal = protocolInvestigatorBean.isPrincipalInvestigatorFlag() ? Boolean.TRUE
                    : Boolean.FALSE;
                    personTableRow.add( piVal );
                    //unDetail.removeAllElements();
                    unDetail = protocolInvestigatorBean.getInvestigatorUnits();
                    /* add investigator units into hashtable with investigator id
                     as key and unit bean as value */
                    if( personId != null && unDetail != null ){
                        unitHashData.put( personId , (Vector)unDetail.clone() );
                    }
                    protocolInvestigatorBean = null;
                    personData.add( personTableRow );
                }
            }catch( Exception err ){
                err.printStackTrace();
            }
        }
        personTableRow = null;
        personId = null;
        unDetail = null;
        piVal = null;
        return newData;
    }
    
    //supporting method to construct unit list data for a specified
    // investigator id
    private Vector getUnitListData( String personId ){
        // fetch the unit details from hashtable for specified investigator
        unitDataBean = (Vector)unitHashData.get( personId );
        
        if( unitDataBean == null || unitDataBean.size() <= 0 ){
            // if there are no unit details present return empty vector
            return unitTableData;
        }
        return unitDataBean;
    }
    
    private Vector getInvColumnNames(){
        if( invColumnNames == null ) {
            invColumnNames = new Vector();
            Enumeration enumColNames = tblInv.getColumnModel().getColumns();
            while(enumColNames.hasMoreElements()){
                String strName = (String)((TableColumn)
                enumColNames.nextElement()).getHeaderValue();
                invColumnNames.addElement(strName);
            }
        }
        return invColumnNames;
    }
    
    private Vector getKeyColumnNames(){
        if( keyColumnNames == null ) {
            keyColumnNames = new Vector();
            Enumeration enumColNames = tblKeyPersons.getColumnModel().getColumns();
            while(enumColNames.hasMoreElements()){
                String strName = (String)((TableColumn)
                enumColNames.nextElement()).getHeaderValue();
                keyColumnNames.addElement(strName);
            }
        }
        return keyColumnNames;
    }
    
    private DefaultTableModel getEmptyInvModel() {
        if( emptyInvModel == null ) {
            emptyInvModel = new javax.swing.table.DefaultTableModel(
            new Object [][] {}, getInvColumnNames().toArray()) {
                boolean[] canEdit = new boolean [] {
                    false, false, false, false, false
                };
                public boolean isCellEditable(int rowIndex, int columnIndex) {
                    return canEdit [columnIndex];
                }
            };
        }
        return emptyInvModel;
    }
    
    private DefaultTableModel getEmptyKeyModel(){
        if( emptyKeyModel == null ) {
            emptyKeyModel = new javax.swing.table.DefaultTableModel(
            new Object [][] {},getKeyColumnNames().toArray()) {
                boolean[] canEdit = new boolean [] {
                    false, false, false
                };
                public boolean isCellEditable(int rowIndex, int columnIndex) {
                    return canEdit [columnIndex];
                }
            };
        }
        return emptyKeyModel;
    }
    
    private DefaultTableModel getEmptyReviewersModel() {
        if( emptyRevModel == null ) {
            emptyRevModel = new javax.swing.table.DefaultTableModel(
            new Object [][] {},getRevColumnNames().toArray()) {
                boolean[] canEdit = new boolean [] {
                    false, false
                };
                public boolean isCellEditable(int rowIndex, int columnIndex) {
                    return canEdit [columnIndex];
                }
            };
        }
        return emptyRevModel;
    }
    private void setTableColumnWidths(){
        column = tblInv.getColumnModel().getColumn(0);
        column.setPreferredWidth(20);
        //column.setMaxWidth(20);
        column.setMinWidth(20);
        column.setHeaderRenderer(emptyHeaderRenderer);
        column.setCellRenderer( iconRenderer );
        
        column = tblInv.getColumnModel().getColumn(1);
        column.setPreferredWidth(190);
        //column.setMaxWidth(100);
        column.setMinWidth(190);
        column.setHeaderRenderer(emptyButtonRenderer);
        column.setCellRenderer( piRenderer );
        
        column = tblInv.getColumnModel().getColumn(2);
        //column.sizeWidthToFit();
        column.setHeaderRenderer(emptyButtonRenderer);
        column.setCellRenderer( piRenderer );
        
        column = tblInv.getColumnModel().getColumn(3);
        column.setMaxWidth(0);
        column.setMinWidth(0);
        column.setPreferredWidth(0);
        
        column.setHeaderRenderer(emptyHeaderRenderer);
        
        column = tblInv.getColumnModel().getColumn(4);
        column.setMaxWidth(0);
        column.setMinWidth(0);
        column.setPreferredWidth(0);
        column.setHeaderRenderer(emptyHeaderRenderer);
        //tblInv.setRowHeight(22);
        tblInv.setSelectionBackground((java.awt.Color) javax.swing.UIManager.getDefaults().get("Panel.background"));
        tblInv.setSelectionForeground(Color.black);
        tblInv.setColumnSelectionAllowed(false);
        tblInv.getSelectionModel().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tblInv.getTableHeader().setReorderingAllowed(false);
        
        
        column = tblReviewers.getColumnModel().getColumn(0);
        column.setHeaderRenderer(emptyButtonRenderer);
        //column.setPreferredWidth(110);
        column.setMinWidth(130);
        
        column = tblReviewers.getColumnModel().getColumn(1);
        column.setMaxWidth(80);
        //column.sizeWidthToFit();
        column.setHeaderRenderer(emptyButtonRenderer);
        tblReviewers.getTableHeader().setReorderingAllowed(false);
        tblReviewers.getSelectionModel().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        
        column = tblKeyPersons.getColumnModel().getColumn(0);
        column.setHeaderRenderer(emptyButtonRenderer);
        //column.setPreferredWidth(110);
        column.setMinWidth(130);
        
        column = tblKeyPersons.getColumnModel().getColumn(1);
        column.setMinWidth(80);
        //column.sizeWidthToFit();
        column.setHeaderRenderer(emptyButtonRenderer);
        
        column = tblKeyPersons.getColumnModel().getColumn(2);
        column.setMinWidth(80);
        column.setHeaderRenderer(emptyButtonRenderer);
        
        tblKeyPersons.getTableHeader().setReorderingAllowed(false);
        tblKeyPersons.getSelectionModel().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        
    }
    
    /**
     * Method used to handle the value change events for the table.
     * @param listSelectionEvent event which delegates selection changes for a
     * table.
     */
    public void valueChanged( ListSelectionEvent listSelectionEvent ) {
        
        Object source = listSelectionEvent.getSource();
        int selectedRow = tblInv.getSelectedRow();
        if( (selectedRow >= 0 ) && (investigatorData != null)) {
            ProtocolInvestigatorsBean investigatorRow=null;
            investigatorRow = (ProtocolInvestigatorsBean)
            investigatorData.get( tblInv.getValueAt( selectedRow,3) == null
            ? "" : tblInv.getValueAt( selectedRow,3).toString() );
            lstUnits.setListData( emptyVector );
            //((DefaultListModel)lstUnits.getModel()).removeAllElements();
            if(investigatorRow != null){
                Vector unitsForPerson = getUnitListData(investigatorRow.getPersonId());
                if( unitsForPerson != null ){
                    lstUnits.setListData( unitsForPerson );
                }
            }
            setTableColumnWidths();
        }
    }
    public void actionPerformed( ActionEvent ae ) {
        Object source = ae.getSource();
        personData = null;
        investigatorData = null;
        investigators = null;
        unitHashData = null;
        //aorListData.removeAllElements();
        //revTableData.removeAllElements();
        unitTableData = null;
        if( source.equals( btnClose ) ) {
            this.dispose();
        }else if ( source.equals( btnNext ) ){
            //Bug Fix : 899 - START
            aorListData.removeAllElements();
            revTableData.removeAllElements();
            //Bug Fix : 899 - End
            selectedRow++;
            protocolId = (String) tblProtocolList.getValueAt( selectedRow,0);
            tblProtocolList.setRowSelectionInterval(selectedRow,selectedRow);
            setValues();
        }else if ( source.equals( btnPrev ) ){
            aorListData.removeAllElements();
            revTableData.removeAllElements();
            selectedRow--;
            protocolId = (String) tblProtocolList.getValueAt( selectedRow,0);
            tblProtocolList.setRowSelectionInterval(selectedRow,selectedRow);
            setValues();
        }else if (source.equals( btnNotes ) ){
            //Code for new Enhancement to display Protocol Notes
            //Added by Vyjayanthi on 27/08/03
            //Start
            Vector notesData = protocolInfoBean.getVecNotepad();
            if( notesData != null && notesData.size() > 0 ) {
                ProtocolSummaryNotesList notesList = new ProtocolSummaryNotesList(notesData);
                notesList.showForm();
            }else{
                CoeusOptionPane.showInfoDialog(messageResources.parseMessageKey(
                "protocolNotes_exceptionCode.1000"));
            }
            //End
        }
    }
    public static void main(String args[]) {
        try{
            UIManager.setLookAndFeel( UIManager.getSystemLookAndFeelClassName() );
            ProtocolSummaryForm summaryForm = new ProtocolSummaryForm("0000001668",true);
            summaryForm.showForm();
        }catch(Exception e) {
        }
    }
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc=" Generated Code ">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        pnlMain = new javax.swing.JPanel();
        lblProtocolNo = new javax.swing.JLabel();
        lblType = new javax.swing.JLabel();
        lblStatus = new javax.swing.JLabel();
        lblApplDate = new javax.swing.JLabel();
        lblApprovalDate = new javax.swing.JLabel();
        lblExpirationDate = new javax.swing.JLabel();
        lblTitle = new javax.swing.JLabel();
        scrPnTitle = new javax.swing.JScrollPane();
        txtArTitle = new javax.swing.JTextArea();
        lblStatusValue = new javax.swing.JLabel();
        lblApplDateValue = new javax.swing.JLabel();
        lblApprovalDateValue = new javax.swing.JLabel();
        lblExprirationDateValue = new javax.swing.JLabel();
        lblProtocolValue = new javax.swing.JLabel();
        lblTypeValue = new javax.swing.JLabel();
        pnlButtons = new javax.swing.JPanel();
        btnClose = new javax.swing.JButton();
        btnPrev = new javax.swing.JButton();
        btnNext = new javax.swing.JButton();
        btnNotes = new javax.swing.JButton();
        jPanel1 = new javax.swing.JPanel();
        pnlInvestigator = new javax.swing.JPanel();
        scrPnInv = new javax.swing.JScrollPane();
        tblInv = new javax.swing.JTable();
        scrPnUnits = new javax.swing.JScrollPane();
        lstUnits = new javax.swing.JList();
        pnlMiddle = new javax.swing.JPanel();
        scrPnKeyPersons = new javax.swing.JScrollPane();
        tblKeyPersons = new javax.swing.JTable();
        scrPnAOR = new javax.swing.JScrollPane();
        lstAOR = new javax.swing.JList();
        pnlSubmissionDetails = new javax.swing.JPanel();
        scrPnSubDetails = new javax.swing.JScrollPane();
        pnlFlow = new javax.swing.JPanel();
        pnlSubDetails = new javax.swing.JPanel();
        lblCommitteeId = new javax.swing.JLabel();
        lblCommitteeIdValue = new javax.swing.JLabel();
        lblCommitteeName = new javax.swing.JLabel();
        lblCommitteeNameValue = new javax.swing.JLabel();
        lblSchDate = new javax.swing.JLabel();
        lblSchDateValue = new javax.swing.JLabel();
        lblSubmissionType = new javax.swing.JLabel();
        lblSubmissionTypeValue = new javax.swing.JLabel();
        lblReviewType = new javax.swing.JLabel();
        lblReviewTypeValue = new javax.swing.JLabel();
        lblSubStatus = new javax.swing.JLabel();
        lblSubStatusValue = new javax.swing.JLabel();
        scrPnReviewers = new javax.swing.JScrollPane();
        tblReviewers = new javax.swing.JTable();
        lblDescription = new javax.swing.JLabel();
        jcrPnAmRenDetails = new javax.swing.JScrollPane();
        txtArNote = new javax.swing.JTextArea();

        pnlMain.setLayout(new java.awt.GridBagLayout());

        pnlMain.setMinimumSize(new java.awt.Dimension(630, 503));
        pnlMain.setPreferredSize(new java.awt.Dimension(630, 508));
        lblProtocolNo.setFont(CoeusFontFactory.getLabelFont());
        lblProtocolNo.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblProtocolNo.setText("Protocol Number :");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 5, 0, 0);
        pnlMain.add(lblProtocolNo, gridBagConstraints);

        lblType.setFont(CoeusFontFactory.getLabelFont());
        lblType.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblType.setText("Type :");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 5, 0, 0);
        pnlMain.add(lblType, gridBagConstraints);

        lblStatus.setFont(CoeusFontFactory.getLabelFont());
        lblStatus.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblStatus.setText("Status :");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 5, 0, 0);
        pnlMain.add(lblStatus, gridBagConstraints);

        lblApplDate.setFont(CoeusFontFactory.getLabelFont());
        lblApplDate.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblApplDate.setText("Application Date :");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 45, 0, 0);
        pnlMain.add(lblApplDate, gridBagConstraints);

        lblApprovalDate.setFont(CoeusFontFactory.getLabelFont());
        lblApprovalDate.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblApprovalDate.setText("Approval Date :");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 5, 0, 0);
        pnlMain.add(lblApprovalDate, gridBagConstraints);

        lblExpirationDate.setFont(CoeusFontFactory.getLabelFont());
        lblExpirationDate.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblExpirationDate.setText("Expiration Date :");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 45, 0, 0);
        pnlMain.add(lblExpirationDate, gridBagConstraints);

        lblTitle.setFont(CoeusFontFactory.getLabelFont());
        lblTitle.setText("Title :");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHEAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 5, 0, 0);
        pnlMain.add(lblTitle, gridBagConstraints);

        scrPnTitle.setBorder(null);
        scrPnTitle.setMinimumSize(new java.awt.Dimension(60, 30));
        scrPnTitle.setPreferredSize(new java.awt.Dimension(100, 30));
        txtArTitle.setBackground(javax.swing.UIManager.getDefaults().getColor("Panel.background"));
        txtArTitle.setEditable(false);
        txtArTitle.setFont(CoeusFontFactory.getNormalFont());
        txtArTitle.setLineWrap(true);
        txtArTitle.setWrapStyleWord(true);
        txtArTitle.setDisabledTextColor(new java.awt.Color(0, 0, 0));
        txtArTitle.setEnabled(false);
        scrPnTitle.setViewportView(txtArTitle);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 5, 0, 3);
        pnlMain.add(scrPnTitle, gridBagConstraints);

        lblStatusValue.setFont(CoeusFontFactory.getNormalFont());
        lblStatusValue.setPreferredSize(new java.awt.Dimension(136, 16));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 5, 0, 3);
        pnlMain.add(lblStatusValue, gridBagConstraints);

        lblApplDateValue.setFont(CoeusFontFactory.getNormalFont());
        lblApplDateValue.setMaximumSize(new java.awt.Dimension(100, 20));
        lblApplDateValue.setMinimumSize(new java.awt.Dimension(100, 20));
        lblApplDateValue.setPreferredSize(new java.awt.Dimension(100, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 5, 0, 0);
        pnlMain.add(lblApplDateValue, gridBagConstraints);

        lblApprovalDateValue.setFont(CoeusFontFactory.getNormalFont());
        lblApprovalDateValue.setMaximumSize(new java.awt.Dimension(100, 20));
        lblApprovalDateValue.setMinimumSize(new java.awt.Dimension(100, 20));
        lblApprovalDateValue.setPreferredSize(new java.awt.Dimension(100, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 5, 0, 0);
        pnlMain.add(lblApprovalDateValue, gridBagConstraints);

        lblExprirationDateValue.setFont(CoeusFontFactory.getNormalFont());
        lblExprirationDateValue.setMaximumSize(new java.awt.Dimension(100, 20));
        lblExprirationDateValue.setMinimumSize(new java.awt.Dimension(100, 20));
        lblExprirationDateValue.setPreferredSize(new java.awt.Dimension(100, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 5, 0, 0);
        pnlMain.add(lblExprirationDateValue, gridBagConstraints);

        lblProtocolValue.setFont(CoeusFontFactory.getNormalFont());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 5, 0, 0);
        pnlMain.add(lblProtocolValue, gridBagConstraints);

        lblTypeValue.setFont(CoeusFontFactory.getNormalFont());
        lblTypeValue.setPreferredSize(new java.awt.Dimension(136, 16));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 5, 0, 3);
        pnlMain.add(lblTypeValue, gridBagConstraints);

        pnlButtons.setLayout(new java.awt.GridBagLayout());

        btnClose.setFont(CoeusFontFactory.getLabelFont());
        btnClose.setMnemonic('C');
        btnClose.setText("Close");
        btnClose.setMaximumSize(new java.awt.Dimension(100, 26));
        btnClose.setMinimumSize(new java.awt.Dimension(70, 26));
        btnClose.setPreferredSize(new java.awt.Dimension(70, 26));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        pnlButtons.add(btnClose, gridBagConstraints);

        btnPrev.setFont(CoeusFontFactory.getLabelFont());
        btnPrev.setMnemonic('P');
        btnPrev.setText("Prev");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 0, 0, 0);
        pnlButtons.add(btnPrev, gridBagConstraints);

        btnNext.setFont(CoeusFontFactory.getLabelFont());
        btnNext.setMnemonic('N');
        btnNext.setText("Next");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 0, 0, 0);
        pnlButtons.add(btnNext, gridBagConstraints);

        btnNotes.setFont(CoeusFontFactory.getLabelFont());
        btnNotes.setMnemonic('O');
        btnNotes.setText("Notes");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 0, 0, 0);
        pnlButtons.add(btnNotes, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridheight = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 0, 5);
        pnlMain.add(pnlButtons, gridBagConstraints);

        jPanel1.setLayout(new java.awt.GridBagLayout());

        jPanel1.setPreferredSize(new java.awt.Dimension(630, 370));
        pnlInvestigator.setLayout(new java.awt.GridBagLayout());

        pnlInvestigator.setMinimumSize(new java.awt.Dimension(620, 120));
        pnlInvestigator.setPreferredSize(new java.awt.Dimension(620, 120));
        scrPnInv.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)), "Investigators", javax.swing.border.TitledBorder.LEFT, javax.swing.border.TitledBorder.BELOW_TOP, CoeusFontFactory.getLabelFont()));
        scrPnInv.setMaximumSize(new java.awt.Dimension(320, 120));
        scrPnInv.setMinimumSize(new java.awt.Dimension(320, 320));
        scrPnInv.setPreferredSize(new java.awt.Dimension(320, 120));
        tblInv.setBackground(javax.swing.UIManager.getDefaults().getColor("Panel.background"));
        tblInv.setFont(CoeusFontFactory.getNormalFont());
        tblInv.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null}
            },
            new String [] {
                "Icon", "Name", "Affiliation", "Person ID", "PI"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tblInv.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_ALL_COLUMNS);
        tblInv.setShowHorizontalLines(false);
        tblInv.setShowVerticalLines(false);
        scrPnInv.setViewportView(tblInv);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        pnlInvestigator.add(scrPnInv, gridBagConstraints);

        scrPnUnits.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)), "Units", javax.swing.border.TitledBorder.LEFT, javax.swing.border.TitledBorder.BELOW_TOP, CoeusFontFactory.getLabelFont()));
        scrPnUnits.setMaximumSize(new java.awt.Dimension(300, 120));
        scrPnUnits.setMinimumSize(new java.awt.Dimension(290, 120));
        scrPnUnits.setPreferredSize(new java.awt.Dimension(300, 120));
        lstUnits.setBackground(javax.swing.UIManager.getDefaults().getColor("Panel.background"));
        lstUnits.setFont(CoeusFontFactory.getNormalFont());
        scrPnUnits.setViewportView(lstUnits);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        pnlInvestigator.add(scrPnUnits, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 0, 5);
        jPanel1.add(pnlInvestigator, gridBagConstraints);

        pnlMiddle.setLayout(new java.awt.GridBagLayout());

        pnlMiddle.setMinimumSize(new java.awt.Dimension(500, 100));
        pnlMiddle.setPreferredSize(new java.awt.Dimension(620, 100));
        scrPnKeyPersons.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)), "Study Persons", javax.swing.border.TitledBorder.LEFT, javax.swing.border.TitledBorder.BELOW_TOP, CoeusFontFactory.getLabelFont()));
        scrPnKeyPersons.setMaximumSize(new java.awt.Dimension(320, 100));
        scrPnKeyPersons.setMinimumSize(new java.awt.Dimension(320, 100));
        scrPnKeyPersons.setPreferredSize(new java.awt.Dimension(320, 100));
        tblKeyPersons.setBackground(javax.swing.UIManager.getDefaults().getColor("Panel.background"));
        tblKeyPersons.setFont(CoeusFontFactory.getNormalFont());
        tblKeyPersons.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null}
            },
            new String [] {
                "Name", "Role", "Affiliation"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tblKeyPersons.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_ALL_COLUMNS);
        tblKeyPersons.setShowHorizontalLines(false);
        tblKeyPersons.setShowVerticalLines(false);
        scrPnKeyPersons.setViewportView(tblKeyPersons);

        pnlMiddle.add(scrPnKeyPersons, new java.awt.GridBagConstraints());

        scrPnAOR.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)), "Areas of Research", javax.swing.border.TitledBorder.LEFT, javax.swing.border.TitledBorder.BELOW_TOP, CoeusFontFactory.getLabelFont()));
        scrPnAOR.setMaximumSize(new java.awt.Dimension(300, 100));
        scrPnAOR.setMinimumSize(new java.awt.Dimension(300, 100));
        scrPnAOR.setOpaque(false);
        scrPnAOR.setPreferredSize(new java.awt.Dimension(300, 100));
        lstAOR.setBackground(javax.swing.UIManager.getDefaults().getColor("Panel.background"));
        lstAOR.setFont(CoeusFontFactory.getNormalFont());
        scrPnAOR.setViewportView(lstAOR);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        pnlMiddle.add(scrPnAOR, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 5, 0, 5);
        jPanel1.add(pnlMiddle, gridBagConstraints);

        pnlSubmissionDetails.setLayout(new java.awt.GridBagLayout());

        pnlSubmissionDetails.setMinimumSize(new java.awt.Dimension(520, 140));
        pnlSubmissionDetails.setPreferredSize(new java.awt.Dimension(620, 140));
        scrPnSubDetails.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)), "   Submission Details", javax.swing.border.TitledBorder.LEFT, javax.swing.border.TitledBorder.BELOW_TOP, CoeusFontFactory.getLabelFont()));
        scrPnSubDetails.setMaximumSize(new java.awt.Dimension(320, 140));
        scrPnSubDetails.setMinimumSize(new java.awt.Dimension(320, 140));
        scrPnSubDetails.setPreferredSize(new java.awt.Dimension(320, 140));
        pnlFlow.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 0, 0));

        pnlFlow.setMaximumSize(new java.awt.Dimension(320, 114));
        pnlFlow.setMinimumSize(new java.awt.Dimension(320, 114));
        pnlFlow.setPreferredSize(new java.awt.Dimension(320, 90));
        pnlSubDetails.setLayout(new java.awt.GridBagLayout());

        lblCommitteeId.setFont(CoeusFontFactory.getLabelFont());
        lblCommitteeId.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblCommitteeId.setText("Committee Id :");
        lblCommitteeId.setMaximumSize(new java.awt.Dimension(100, 20));
        lblCommitteeId.setPreferredSize(new java.awt.Dimension(100, 16));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHEAST;
        pnlSubDetails.add(lblCommitteeId, gridBagConstraints);

        lblCommitteeIdValue.setFont(CoeusFontFactory.getNormalFont());
        lblCommitteeIdValue.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        lblCommitteeIdValue.setMaximumSize(new java.awt.Dimension(36655, 36655));
        lblCommitteeIdValue.setMinimumSize(new java.awt.Dimension(80, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 5, 0, 0);
        pnlSubDetails.add(lblCommitteeIdValue, gridBagConstraints);

        lblCommitteeName.setFont(CoeusFontFactory.getLabelFont());
        lblCommitteeName.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblCommitteeName.setText("Committee Name :");
        lblCommitteeName.setMaximumSize(new java.awt.Dimension(104, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHEAST;
        pnlSubDetails.add(lblCommitteeName, gridBagConstraints);

        lblCommitteeNameValue.setFont(CoeusFontFactory.getNormalFont());
        lblCommitteeNameValue.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        lblCommitteeNameValue.setMaximumSize(new java.awt.Dimension(36655, 36655));
        lblCommitteeNameValue.setMinimumSize(new java.awt.Dimension(120, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 5, 0, 0);
        pnlSubDetails.add(lblCommitteeNameValue, gridBagConstraints);

        lblSchDate.setFont(CoeusFontFactory.getLabelFont());
        lblSchDate.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblSchDate.setText("Scheduled Date :");
        lblSchDate.setMaximumSize(new java.awt.Dimension(120, 20));
        lblSchDate.setPreferredSize(new java.awt.Dimension(100, 16));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHEAST;
        pnlSubDetails.add(lblSchDate, gridBagConstraints);

        lblSchDateValue.setFont(CoeusFontFactory.getNormalFont());
        lblSchDateValue.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        lblSchDateValue.setMaximumSize(new java.awt.Dimension(36655, 36655));
        lblSchDateValue.setMinimumSize(new java.awt.Dimension(120, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 5, 0, 0);
        pnlSubDetails.add(lblSchDateValue, gridBagConstraints);

        lblSubmissionType.setFont(CoeusFontFactory.getLabelFont());
        lblSubmissionType.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblSubmissionType.setText("Submission Type :");
        lblSubmissionType.setPreferredSize(new java.awt.Dimension(104, 16));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHEAST;
        pnlSubDetails.add(lblSubmissionType, gridBagConstraints);

        lblSubmissionTypeValue.setFont(CoeusFontFactory.getNormalFont());
        lblSubmissionTypeValue.setMaximumSize(new java.awt.Dimension(36655, 36655));
        lblSubmissionTypeValue.setMinimumSize(new java.awt.Dimension(120, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 5, 0, 0);
        pnlSubDetails.add(lblSubmissionTypeValue, gridBagConstraints);

        lblReviewType.setFont(CoeusFontFactory.getLabelFont());
        lblReviewType.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblReviewType.setText("Review Type :");
        lblReviewType.setMaximumSize(new java.awt.Dimension(77, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHEAST;
        pnlSubDetails.add(lblReviewType, gridBagConstraints);

        lblReviewTypeValue.setFont(CoeusFontFactory.getNormalFont());
        lblReviewTypeValue.setMaximumSize(new java.awt.Dimension(36655, 36655));
        lblReviewTypeValue.setMinimumSize(new java.awt.Dimension(135, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 5, 0, 0);
        pnlSubDetails.add(lblReviewTypeValue, gridBagConstraints);

        lblSubStatus.setFont(CoeusFontFactory.getLabelFont());
        lblSubStatus.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblSubStatus.setText("Submission Status :");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHEAST;
        pnlSubDetails.add(lblSubStatus, gridBagConstraints);

        lblSubStatusValue.setFont(CoeusFontFactory.getNormalFont());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 5, 0, 0);
        pnlSubDetails.add(lblSubStatusValue, gridBagConstraints);

        pnlFlow.add(pnlSubDetails);

        scrPnSubDetails.setViewportView(pnlFlow);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        pnlSubmissionDetails.add(scrPnSubDetails, gridBagConstraints);
        scrPnSubDetails.getAccessibleContext().setAccessibleName("Submission Details");

        scrPnReviewers.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)), "Reviewers", javax.swing.border.TitledBorder.LEFT, javax.swing.border.TitledBorder.BELOW_TOP, CoeusFontFactory.getLabelFont()));
        scrPnReviewers.setMaximumSize(new java.awt.Dimension(300, 140));
        scrPnReviewers.setMinimumSize(new java.awt.Dimension(300, 140));
        scrPnReviewers.setPreferredSize(new java.awt.Dimension(300, 140));
        tblReviewers.setBackground(javax.swing.UIManager.getDefaults().getColor("Panel.background"));
        tblReviewers.setFont(CoeusFontFactory.getNormalFont());
        tblReviewers.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null}
            },
            new String [] {
                "Name", "Type"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tblReviewers.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_ALL_COLUMNS);
        tblReviewers.setShowHorizontalLines(false);
        tblReviewers.setShowVerticalLines(false);
        scrPnReviewers.setViewportView(tblReviewers);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        pnlSubmissionDetails.add(scrPnReviewers, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.SOUTH;
        gridBagConstraints.insets = new java.awt.Insets(0, 5, 0, 5);
        jPanel1.add(pnlSubmissionDetails, gridBagConstraints);

        lblDescription.setFont(CoeusFontFactory.getLabelFont());
        lblDescription.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblDescription.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0), "Submission Details", javax.swing.border.TitledBorder.LEFT, javax.swing.border.TitledBorder.BELOW_TOP, CoeusFontFactory.getLabelFont()));
        lblDescription.setMaximumSize(new java.awt.Dimension(620, 120));
        lblDescription.setMinimumSize(new java.awt.Dimension(620, 120));
        lblDescription.setPreferredSize(new java.awt.Dimension(620, 120));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 5, 5, 5);
        jPanel1.add(lblDescription, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.gridwidth = 5;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        pnlMain.add(jPanel1, gridBagConstraints);

        jcrPnAmRenDetails.setBackground(javax.swing.UIManager.getDefaults().getColor("Panel.background"));
        jcrPnAmRenDetails.setBorder(null);
        jcrPnAmRenDetails.setMinimumSize(new java.awt.Dimension(520, 18));
        jcrPnAmRenDetails.setPreferredSize(new java.awt.Dimension(520, 18));
        txtArNote.setBackground(javax.swing.UIManager.getDefaults().getColor("Panel.background"));
        txtArNote.setFont(CoeusFontFactory.getNormalFont());
        txtArNote.setLineWrap(true);
        txtArNote.setWrapStyleWord(true);
        jcrPnAmRenDetails.setViewportView(txtArNote);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridwidth = 5;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 0, 3, 0);
        pnlMain.add(jcrPnAmRenDetails, gridBagConstraints);

        getContentPane().add(pnlMain, java.awt.BorderLayout.CENTER);

    }// </editor-fold>//GEN-END:initComponents
    
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnClose;
    private javax.swing.JButton btnNext;
    private javax.swing.JButton btnNotes;
    private javax.swing.JButton btnPrev;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jcrPnAmRenDetails;
    private javax.swing.JLabel lblApplDate;
    private javax.swing.JLabel lblApplDateValue;
    private javax.swing.JLabel lblApprovalDate;
    private javax.swing.JLabel lblApprovalDateValue;
    private javax.swing.JLabel lblCommitteeId;
    private javax.swing.JLabel lblCommitteeIdValue;
    private javax.swing.JLabel lblCommitteeName;
    private javax.swing.JLabel lblCommitteeNameValue;
    private javax.swing.JLabel lblDescription;
    private javax.swing.JLabel lblExpirationDate;
    private javax.swing.JLabel lblExprirationDateValue;
    private javax.swing.JLabel lblProtocolNo;
    private javax.swing.JLabel lblProtocolValue;
    private javax.swing.JLabel lblReviewType;
    private javax.swing.JLabel lblReviewTypeValue;
    private javax.swing.JLabel lblSchDate;
    private javax.swing.JLabel lblSchDateValue;
    private javax.swing.JLabel lblStatus;
    private javax.swing.JLabel lblStatusValue;
    private javax.swing.JLabel lblSubStatus;
    private javax.swing.JLabel lblSubStatusValue;
    private javax.swing.JLabel lblSubmissionType;
    private javax.swing.JLabel lblSubmissionTypeValue;
    private javax.swing.JLabel lblTitle;
    private javax.swing.JLabel lblType;
    private javax.swing.JLabel lblTypeValue;
    private javax.swing.JList lstAOR;
    private javax.swing.JList lstUnits;
    private javax.swing.JPanel pnlButtons;
    private javax.swing.JPanel pnlFlow;
    private javax.swing.JPanel pnlInvestigator;
    private javax.swing.JPanel pnlMain;
    private javax.swing.JPanel pnlMiddle;
    private javax.swing.JPanel pnlSubDetails;
    private javax.swing.JPanel pnlSubmissionDetails;
    private javax.swing.JScrollPane scrPnAOR;
    private javax.swing.JScrollPane scrPnInv;
    private javax.swing.JScrollPane scrPnKeyPersons;
    private javax.swing.JScrollPane scrPnReviewers;
    private javax.swing.JScrollPane scrPnSubDetails;
    private javax.swing.JScrollPane scrPnTitle;
    private javax.swing.JScrollPane scrPnUnits;
    private javax.swing.JTable tblInv;
    private javax.swing.JTable tblKeyPersons;
    private javax.swing.JTable tblReviewers;
    private javax.swing.JTextArea txtArNote;
    private javax.swing.JTextArea txtArTitle;
    // End of variables declaration//GEN-END:variables
    
    class UnitListCellRenderer extends DefaultListCellRenderer {
        ProtocolInvestigatorUnitsBean unitBean;
        public Component getListCellRendererComponent(JList list, Object value,
        int index, boolean isSelected, boolean cellHasFocus){
            if( value != null && value instanceof ProtocolInvestigatorUnitsBean){
                unitBean = ( ProtocolInvestigatorUnitsBean ) value;
                setText( unitBean.getUnitNumber() + " : "+ unitBean.getUnitName() );
                if( unitBean.isLeadUnitFlag() ) {
                    setForeground( Color.red );
                }else{
                    setForeground( Color.black );
                }
            }
            setFont(CoeusFontFactory.getNormalFont());
            return this;
        }
    }
    
    class PIRenderer extends DefaultTableCellRenderer {
        ProtocolInvestigatorsBean invBean;
        public Component getTableCellRendererComponent(JTable table,
        Object value, boolean isSelected, boolean hasFocus, int row,
        int column) {
            setText((String)value);
            Object val = tblInv.getValueAt(row,4);
            if( val != null && val instanceof Boolean ){
                boolean pi = ((Boolean)val).booleanValue();
                if( pi ) {
                    setForeground(Color.red);
                }else {
                    setForeground(Color.black);
                }
            }
            setFont(CoeusFontFactory.getNormalFont());
            return this;
        }
    }
    
    
    class EmptyButtonRenderer extends DefaultTableCellRenderer {
        JLabel label = new JLabel();
        public Component getTableCellRendererComponent(JTable table, Object value,
        boolean isSelected, boolean hasFocus, int row, int column) {
            label.setText( (String) value );
            label.setFont( CoeusFontFactory.getLabelFont() );
            label.setBackground(Color.lightGray);
            label.setOpaque(true);
            label.setBorder(new javax.swing.border.EmptyBorder(new java.awt.Insets(0, 0, 0, 10)));
            return label;
        }
    }
    
}