/*
 * ProtocolNotepadForm.java
 *
 * Created on June 16, 2003, 12:36 PM
 */

/* PMD check performed, and commented unused imports and variables on 14-SEP-2010
 * by Johncy M John
 */

package edu.mit.coeus.irb.gui;

import edu.mit.coeus.gui.CoeusFontFactory;
import edu.mit.coeus.gui.CoeusAppletMDIForm;
import edu.mit.coeus.irb.bean.ProtocolNotepadBean;
import edu.mit.coeus.gui.CoeusMessageResources;
import edu.mit.coeus.utils.*;
import edu.mit.coeus.exception.*;
import edu.mit.coeus.brokers.*;
import javax.swing.JComponent;
import java.awt.event.*;
import java.util.*;
import javax.swing.table.*;

/** This Form is used to list all the Notes for a Particular
 * Proposal Number and sequence number.
 * @author senthil
 */
public class ProtocolNotepadForm extends javax.swing.JComponent 
    implements ActionListener, Observer  {
    
    private char functionType;
    /**
     * This vector contains the vector of notepad beans that need to be displayed 
     * This vector also contains the newly inserted ProtocolNotes using ProtocolNewNotes
     * on the ProtocolNotepadForm
     */
    private Vector protocolVecNotes;
    // To indicate is there are new beans to be added to the database
    private boolean saveRequired;    
    // holds protocol number for this set of Notepadbeans received from DB
    private String protocolNumber;
    // This is used to hold MDI form reference
    private CoeusAppletMDIForm mdiReference;
    // used to store the Maximum value of Entry number which can be updated to new notepad beans.
    private int maxEntryNumber = 0 ;
    // the row number of the selected row 
    private int selectedRow = 0;
    // element position in the Vector where the selected bean is stored
    private int vecElementPosition = 0;
    // reference of the ProtocolNewNotes Dialog form
    ProtocolNewNotes protocolNewNotes ;
    private static final char SAVE_NOTEPAD = 'j';
    private final String PROTOCOL_SERVLET = "/protocolMntServlet";
    private final String connectTo = CoeusGuiConstants.CONNECTION_URL+ PROTOCOL_SERVLET;
    private int sequenceNumber;
    private CoeusMessageResources coeusMessageResources;
    private int recordLockedStatus = -1;
    private LockObservable lockObservable = new LockObservable();
    // Added for COEUSQA-2692_Allow an investigator to abandon an IRB protocol that has never been approved_start
    private static final int ABANDON_PROTOCOL_STATUS_CODE = 313;
    // Added for COEUSQA-2692_Allow an investigator to abandon an IRB protocol that has never been approved_end
    /** Creates new form ProtocolNotepadForm */
    public ProtocolNotepadForm() {
        initComponents();
    }
    /** Creates new form ProtocolNotepadForm
     * @param protoVecNotes Vector of ProtocolNotepadBean
     * @param fnType Function Type from the ProtocolDetailForm
     */
    public ProtocolNotepadForm(Vector protoVecNotes,char fnType){
        this.functionType = fnType;
        this.protocolVecNotes = protoVecNotes;
        if (protocolVecNotes == null) {
            protocolVecNotes = new Vector(); 
        }
    }
    /** Creates new form ProtocolNotepadForm
     * @param protocolNumber Protocol Number
     * @param SeqNumber Sequence Number
     * @param fnType Function Type
     */
    public ProtocolNotepadForm(String protocolNumber, String SeqNumber, char fnType) {
        initComponents();
    }
    /** This method is used to initialize the components, set the data in the components.
      * This method is invoked in the <CODE>ProtocolDetailForm</CODE>.
      * @param mdiForm is a reference of CoeusAppletMDIForm
      * @return a JPanel containing all the components with the data populated.
      */
    // Modified for COEUSQA-2692_Allow an investigator to abandon an IRB protocol that has never been approved_start
    public JComponent showProtocolNotepadForm(CoeusAppletMDIForm
        mdiForm,int statusCode){
        // Modified for COEUSQA-2692_Allow an investigator to abandon an IRB protocol that has never been approved_end
        this.mdiReference = mdiForm;
        initComponents();
        
        java.awt.Component[] components = {tblProtocolNotes,btnAdd};
        ScreenFocusTraversalPolicy  traversePolicy = new ScreenFocusTraversalPolicy( components );
        this.setFocusTraversalPolicy(traversePolicy);
        this.setFocusCycleRoot(true);        

         //Added by Amit 11/18/2003
        if(functionType == CoeusGuiConstants.DISPLAY_MODE){

            java.awt.Color bgListColor = (java.awt.Color)javax.swing.UIManager.getDefaults().get("Panel.background");
        
            tblProtocolNotes.setBackground(bgListColor);    
            /*tblProtocolNotes.setSelectionBackground(bgListColor );
            tblProtocolNotes.setSelectionForeground(java.awt.Color.BLACK);*/
        }
        else{
            /*tblProtocolNotes.setBackground(java.awt.Color.white);            
            tblProtocolNotes.setSelectionBackground(java.awt.Color.white);
            tblProtocolNotes.setSelectionForeground(java.awt.Color.black); */           
        }
        //end Amit 
        
        // This method adds the listeners to all the buttons and to the Jtable
        setListenersForButtons();
        // This method Adds the data to the form i.e. Jtable
        setFormData();
        // This method does the settings to the jtable.
        setEditors();
        // Added for COEUSQA-2692_Allow an investigator to abandon an IRB protocol that has never been approved_start
        enableDisableButton(statusCode);
        // Added for COEUSQA-2692_Allow an investigator to abandon an IRB protocol that has never been approved_end
        return this;
    }
    
    // Added for COEUSQA-2692_Allow an investigator to abandon an IRB protocol that has never been approved_start
    /**
     * Supporting method for enabling and disabling Add button
     * @param statusCode
     */
    private void enableDisableButton(int statusCode){
        if(statusCode == ABANDON_PROTOCOL_STATUS_CODE){
            btnAdd.setEnabled(false);
        }
    }
    // Added for COEUSQA-2692_Allow an investigator to abandon an IRB protocol that has never been approved_end
    // This method is used to set the listeners to the components.
    
    private void setListenersForButtons(){
        coeusMessageResources = CoeusMessageResources.getInstance();
       // btnAdd.setText("Add");
        //btnAdd.setMnemonic('A');
        btnAdd.setFont(CoeusFontFactory.getLabelFont());
//        if (functionType == 'D' && !canModify ) {
//            btnAdd.setEnabled(false);
//        }else{
//            btnAdd.addActionListener(this);
//        }
        btnAdd.addActionListener(this);
        tblProtocolNotes.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent me) {
                if (me.getClickCount() == 2) {
                    try {
                        selectedRow = tblProtocolNotes.getSelectedRow();
                        Integer tblRowSeqNumber = (Integer)
                        tblProtocolNotes.getValueAt(
                        selectedRow, 4);
                        Integer tblRowEntryNumber = (Integer)
                        tblProtocolNotes.getValueAt(
                        selectedRow, 5);
                        showProtocolNote(tblRowSeqNumber, tblRowEntryNumber);
                    } catch( CoeusException coe) {
                        CoeusOptionPane.showDialog(new CoeusClientException(coe));
                    }catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    public void registerLockObserver( Observer observer) {
        lockObservable.addObserver( observer );
    }
    
    private boolean isAuthorized() throws CoeusException {
        RequesterBean request = new RequesterBean();
        request.setFunctionType('w');
        request.setId( protocolNumber );
        AppletServletCommunicator comm = new AppletServletCommunicator(
                CoeusGuiConstants.CONNECTION_URL + "/protocolMntServlet" ,request);
        comm.send();
        ResponderBean response = comm.getResponse();
        if (response.isSuccessfulResponse()) {
            return true;
        }else{
            throw (CoeusException)response.getDataObject();
        }
        
    }
    private void showProtocolNote(Integer tblRowSeqNumber, 
        Integer tblRowEntryNumber)  throws CoeusException{
        int newVecSize = protocolVecNotes.size();
        ProtocolNotepadBean selProtoNotepadBean=null;
        for( int i = 0 ; i < newVecSize; i++) {
            ProtocolNotepadBean protocolNotepadBean=null;
            protocolNotepadBean = 
                (ProtocolNotepadBean) protocolVecNotes.elementAt( i );
            if((tblRowEntryNumber.intValue()) == (protocolNotepadBean.getEntryNumber())){
                    selProtoNotepadBean = protocolNotepadBean;
                    vecElementPosition = i ;
            }
        } //end for
        if (selProtoNotepadBean != null) {
//            modified by ravi to implement authorization to view restricted notes - START
            boolean viewRestrictedNotes = true;
            if("I".equalsIgnoreCase(selProtoNotepadBean.getAcType())){
                //Get Database Timestamp
                java.sql.Timestamp dbTimeStamp = CoeusUtils.getDBTimeStamp();
                selProtoNotepadBean.setUpdateTimestamp(dbTimeStamp);
            }else if( selProtoNotepadBean.isRestrictedFlag()){
                RequesterBean request = new RequesterBean();
                request.setFunctionType('x');
                request.setId( selProtoNotepadBean.getProtocolNumber() );
                AppletServletCommunicator comm = new AppletServletCommunicator(
                        CoeusGuiConstants.CONNECTION_URL + "/protocolMntServlet" ,request);
                comm.send();
                ResponderBean response = comm.getResponse();
                if (response.isSuccessfulResponse()) {
                    viewRestrictedNotes = true;
                }else{
                    throw (CoeusException)response.getDataObject();
                }
            }
            if( viewRestrictedNotes ) {
                protocolNewNotes = new ProtocolNewNotes(mdiReference,
                                    selProtoNotepadBean);
                protocolNewNotes.showProtocolNewNote();

                selProtoNotepadBean = protocolNewNotes.getNewProtoNotepadBean();
                try{
                    selProtoNotepadBean = 
                              protocolNewNotes.getNewProtoNotepadBean();
                    if(("I".equalsIgnoreCase(selProtoNotepadBean.getAcType()))&&
                            (protocolNewNotes.isSaveRequired())) {
                            tblProtocolNotes.setValueAt((selProtoNotepadBean.getComments() == null 
                                ? "" : selProtoNotepadBean.getComments()),selectedRow,0);
                            tblProtocolNotes.setValueAt(CoeusDateFormat.format(selProtoNotepadBean.getUpdateTimestamp().toString()),selectedRow,2);
                            tblProtocolNotes.setValueAt(new Boolean(selProtoNotepadBean.isRestrictedFlag()),selectedRow,3);
                            protocolVecNotes.remove(vecElementPosition);
                            protocolVecNotes.insertElementAt(selProtoNotepadBean,vecElementPosition);
                    } else {
                        //The bean was only sent for display, so do nothing.
                    }
                }catch(Exception e) {
                    e.printStackTrace();
                    CoeusOptionPane.showInfoDialog( e.getMessage() ); 
                }
            }
//            modified by ravi to implement authorization to view restricted notes - END
        }
    }
    
    private void setEditors(){
        

        tblProtocolNotes.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
        TableColumn column = tblProtocolNotes.getColumnModel().getColumn(0);
        column.setMinWidth(260);
        //column.setPreferredWidth(350);
        
        column = tblProtocolNotes.getColumnModel().getColumn(1);
        column.setMinWidth(110);
        //column.setPreferredWidth(75);

        column = tblProtocolNotes.getColumnModel().getColumn(2);
        column.setMinWidth(150);
        //column.setPreferredWidth(150);
        
        column = tblProtocolNotes.getColumnModel().getColumn(3);
        column.setMinWidth(75);
        //column.setPreferredWidth(100);
        
        column = tblProtocolNotes.getColumnModel().getColumn(4);
        column.setMinWidth(0); //Hidden column
        column.setMaxWidth(0);
        column.setPreferredWidth(0);
        
        column = tblProtocolNotes.getColumnModel().getColumn(5); 
        column.setMinWidth(0); //Hidden column
        column.setMaxWidth(0);
        column.setPreferredWidth(0);
        
        tblProtocolNotes.getTableHeader().setReorderingAllowed( false );
        tblProtocolNotes.getTableHeader().setResizingAllowed(true);
        tblProtocolNotes.setFont(CoeusFontFactory.getNormalFont());
        tblProtocolNotes.getTableHeader().setFont(CoeusFontFactory.getLabelFont());
    }
    /** This method uses the notes Vector for the protocol
     * protoVecNotes and displays the contents of the vector in the table
     *
     */
    private void setFormData(){
        if ((protocolVecNotes != null) && ((protocolVecNotes.size())>0)){
            try{
                int dataSize = protocolVecNotes.size();
                ProtocolNotepadBean protocolNotepadBean=null;
                Vector tableData = new Vector() ;
                for( int indx = 0 ; indx < dataSize; indx++) {
                    protocolNotepadBean = 
                        (ProtocolNotepadBean) protocolVecNotes.elementAt( indx );
                    //COEUSQA-2796 - IACUC and IRB - notes being duplicated in withdrawn protocols in Premium
                    //Get and store the maximum sequence number - used in 'DISPLAY' mode
                    if(sequenceNumber < protocolNotepadBean.getSequenceNumber()) {
                        sequenceNumber = protocolNotepadBean.getSequenceNumber(); 
                    }
                    //get and store the maximum entry number
                    if (maxEntryNumber < protocolNotepadBean.getEntryNumber()){
                        maxEntryNumber = protocolNotepadBean.getEntryNumber();
                    };
                    //get the Proposal Number
                    protocolNumber = protocolNotepadBean.getProtocolNumber();
                    
                    Vector tableRow = new Vector();
                    if(protocolNotepadBean != null){
                        tableRow.addElement( protocolNotepadBean.getComments() == null ? "" : protocolNotepadBean.getComments());
//                        tableRow.addElement( protocolNotepadBean.getUpdateUser() == null ? "" : protocolNotepadBean.getUpdateUser());
                        /*
                         * UserId to UserName Enhancement - Start
                         *Added new property getUpdateUserName to get username
                         */
                        tableRow.addElement( protocolNotepadBean.getUpdateUser() == null ? "" : protocolNotepadBean.getUpdateUserName());
                        // USerId to UserName Enhancement - End.
                        //tableRow.addElement( protocolNotepadBean.getUpdateTimestamp() == null ? "" : protocolNotepadBean.getUpdateTimestamp());
                        tableRow.addElement( protocolNotepadBean.getUpdateTimestamp() == null ?
                        "" : CoeusDateFormat.format(protocolNotepadBean.getUpdateTimestamp().toString()));
                        tableRow.addElement( new Boolean(protocolNotepadBean.isRestrictedFlag()));
                        //Integer seqNumInt = Integer(1);
                        Integer seqNumInt = new Integer(protocolNotepadBean.getSequenceNumber());
                        tableRow.addElement( seqNumInt );
                        Integer entNumInt = new Integer(protocolNotepadBean.getEntryNumber());
                        tableRow.addElement( entNumInt );
                    }
                    tableData.addElement( tableRow );
                }
                ((DefaultTableModel)tblProtocolNotes.getModel()).setDataVector(tableData, getColumnNames() );
                ((DefaultTableModel)tblProtocolNotes.getModel()).fireTableDataChanged();
            }catch(Exception e){
                e.printStackTrace();
            }
        } else {
            ((DefaultTableModel)tblProtocolNotes.getModel()).setDataVector(new Object[][]{}, getColumnNames().toArray() );
            ((DefaultTableModel)tblProtocolNotes.getModel()).fireTableDataChanged();
        }
    }
    /**
     * This method is used to get the Column Names of Protocol Notepad
     * table data.
     * @return Vector collection of column names of Protocol Notepad Table.
     */

    private Vector getColumnNames(){
        Enumeration enumColNames = tblProtocolNotes.getColumnModel().getColumns();
        Vector vecColNames = new Vector();
        while(enumColNames.hasMoreElements()){
            String strName = (String)((TableColumn)
            enumColNames.nextElement()).getHeaderValue();
            vecColNames.addElement(strName);
        }
        return vecColNames;
    }
    /** Action Performed Method
     * @param actionEvent Action Event Object
     */    
    public void actionPerformed(java.awt.event.ActionEvent actionEvent) {
        Object actionSource = actionEvent.getSource();
        
        lockObservable.setLockStatus(-1);
        lockObservable.notifyObservers();
        recordLockedStatus=lockObservable.getLockStatus();
        if(actionSource.equals(btnAdd)){
           try{
               if( functionType != 'D' || ( isAuthorized() && canLockProtocol() ) ){
                   //add ProtocolNewNotes logic
                   //Get Database Timestamp
                   java.sql.Timestamp dbTimeStamp = CoeusUtils.getDBTimeStamp();
                   ProtocolNotepadBean newProtoNotepadBean = 
                        new ProtocolNotepadBean();

                   newProtoNotepadBean.setProtocolNumber(protocolNumber);
                   newProtoNotepadBean.setUpdateTimestamp(dbTimeStamp);
                   //COEUSQA-2796 - IACUC and IRB - notes being duplicated in withdrawn protocols in Premium
                   //In 'MODIFY' mode, set the sequence number of newly added notes to '0' 
                   if(functionType == TypeConstants.DISPLAY_MODE) {
                        newProtoNotepadBean.setSequenceNumber(sequenceNumber);
                   }
                   newProtoNotepadBean.setUpdateUser(mdiReference.getUserName());
                   newProtoNotepadBean.setAcType("I");
                   
                   
                   
                   protocolNewNotes = new ProtocolNewNotes(mdiReference,
                                    newProtoNotepadBean);
                   protocolNewNotes.registerLockObservable(this);
                   
                   protocolNewNotes.showProtocolNewNote();
                   newProtoNotepadBean = protocolNewNotes.getNewProtoNotepadBean();
                   addNewNotepadBeanToDB(newProtoNotepadBean);
                   // Added by chandra to save the protocol notes - start
                   if(functionType=='D'){
                           saveNotePad(newProtoNotepadBean,protocolNewNotes.isSaveRequired());
                           setSaveRequired(false);
                   }// End Chandra
               }

            }catch(CoeusException coe ) {
                coe.printStackTrace();
                CoeusOptionPane.showDialog(new CoeusClientException(coe));
            }catch(Exception e){
                e.printStackTrace();
            }
        }
    }
    /** Added by chandra. As soon as the notes are entered into the 
     *notepad the data has to be saved.
     *If no data is changed then it has to release the lock.
     */
    private void saveNotePad(ProtocolNotepadBean protocolNotepadBean,boolean save)
        throws CoeusException{
        RequesterBean requester = new RequesterBean();
        //check whether data is changed or not
        if(!save){
            requester.setDataObject(protocolNotepadBean.getProtocolNumber());
            requester.setFunctionType('Z');
        }else{
            requester.setFunctionType(SAVE_NOTEPAD);
            requester.setDataObject(protocolNotepadBean);
        }
        
        AppletServletCommunicator comm
        = new AppletServletCommunicator(connectTo, requester);
        
        comm.send();
        ResponderBean responder = comm.getResponse();
        if(responder!= null){
            if(responder.isSuccessfulResponse()){
            }else{
                throw new CoeusException(responder.getMessage(),0);
            }
        }else{
            throw new CoeusException(responder.getMessage(),0);
        }
    }
    
    private boolean canLockProtocol() {
        if( recordLockedStatus == -1 ) {
            try{
                if( RecordLocker.lock("PROTOCOL", protocolNumber ) ){
                    recordLockedStatus = CoeusGuiConstants.LOCK_SUCCESSFUL;
                    lockObservable.setLockStatus( CoeusGuiConstants.LOCK_SUCCESSFUL );
                    lockObservable.notifyObservers();
                    return true;
                }
            }catch( CoeusException coe ) {
                recordLockedStatus = CoeusGuiConstants.LOCK_UNSUCCESSFUL;
                CoeusOptionPane.showDialog(new CoeusClientException(coe));
                lockObservable.setLockStatus( CoeusGuiConstants.LOCK_UNSUCCESSFUL );
                lockObservable.notifyObservers();
                return false;
            }
        }else if( recordLockedStatus == CoeusGuiConstants.LOCK_SUCCESSFUL){
            return true;
        }
        CoeusOptionPane.showInfoDialog(coeusMessageResources.parseMessageKey(
            "protocolDetForm_exceptionCode.1019"));
        return false;
    }

    private void addNewNotepadBeanToDB(ProtocolNotepadBean newProtoNotepadBean){
       
      /** 
       *ProtocolNumber and SequenceNumber will be set in 
       * ProtocolUpdateTxnBean 
       */

       maxEntryNumber = maxEntryNumber + 1 ;
       newProtoNotepadBean.setEntryNumber(maxEntryNumber);

       if((newProtoNotepadBean.getComments() != null)&&
            (newProtoNotepadBean.getComments().trim().length() > 0)&&
                (protocolNewNotes.isSaveRequired())){
                setSaveRequired(true);
                if( protocolVecNotes == null ) {
                    protocolVecNotes = new Vector();
                }
                //Vector containing the ProtocolNotepadBeans that needed to be added to the Database
                protocolVecNotes.addElement(newProtoNotepadBean);

                //Vector to update the table
                Vector addedRow = new Vector(4);
                addedRow.addElement(newProtoNotepadBean.getComments());
//                addedRow.addElement(newProtoNotepadBean.getUpdateUser());
                /*
                 * UserId to UserName Enhancement - Start
                 * Added UserUtils class to chnage userid to username 
                 */
                addedRow.addElement(UserUtils.getDisplayName(newProtoNotepadBean.getUpdateUser()));
                // UserId to UserName Enhancement - End
                addedRow.addElement(CoeusDateFormat.format(newProtoNotepadBean.getUpdateTimestamp().toString()));
                addedRow.addElement(new Boolean(newProtoNotepadBean.isRestrictedFlag()));
                addedRow.addElement( new Integer(newProtoNotepadBean.getSequenceNumber()));
                addedRow.addElement( new Integer(newProtoNotepadBean.getEntryNumber()));

                ((DefaultTableModel)tblProtocolNotes.getModel()).insertRow(0, addedRow );
                ((DefaultTableModel)tblProtocolNotes.getModel()).fireTableDataChanged(); 
       }
  
    }
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    private void initComponents() {//GEN-BEGIN:initComponents
        java.awt.GridBagConstraints gridBagConstraints;

        jMenuBar1 = new javax.swing.JMenuBar();
        jMenu1 = new javax.swing.JMenu();
        pnlProtocolNotesGridViewer = new javax.swing.JPanel();
        scrPnProtocolNotesViewer = new javax.swing.JScrollPane();
        tblProtocolNotes = new javax.swing.JTable();
        pnlButtons = new javax.swing.JPanel();
        btnAdd = new javax.swing.JButton();

        jMenu1.setText("Menu");
        jMenuBar1.add(jMenu1);

        setLayout(new java.awt.GridBagLayout());

        setMaximumSize(new java.awt.Dimension(750, 425));
        setMinimumSize(new java.awt.Dimension(750, 410));
        setPreferredSize(new java.awt.Dimension(750, 410));
        pnlProtocolNotesGridViewer.setMaximumSize(new java.awt.Dimension(630, 400));
        pnlProtocolNotesGridViewer.setMinimumSize(new java.awt.Dimension(630, 390));
        pnlProtocolNotesGridViewer.setPreferredSize(new java.awt.Dimension(630, 390));
        scrPnProtocolNotesViewer.setBorder(new javax.swing.border.EtchedBorder());
        scrPnProtocolNotesViewer.setMinimumSize(new java.awt.Dimension(600, 375));
        scrPnProtocolNotesViewer.setPreferredSize(new java.awt.Dimension(600, 375));
        tblProtocolNotes.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Comment", "By", "Time", "Restricted", "SeqNumber", "EntryNumber"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Boolean.class, java.lang.Integer.class, java.lang.Integer.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        scrPnProtocolNotesViewer.setViewportView(tblProtocolNotes);

        pnlProtocolNotesGridViewer.add(scrPnProtocolNotesViewer);

        add(pnlProtocolNotesGridViewer, new java.awt.GridBagConstraints());

        pnlButtons.setLayout(new java.awt.GridBagLayout());

        pnlButtons.setMaximumSize(new java.awt.Dimension(120, 50));
        pnlButtons.setMinimumSize(new java.awt.Dimension(120, 50));
        pnlButtons.setPreferredSize(new java.awt.Dimension(120, 50));
        btnAdd.setMnemonic('N');
        btnAdd.setText("Add Note");
        btnAdd.setMaximumSize(new java.awt.Dimension(100, 26));
        btnAdd.setMinimumSize(new java.awt.Dimension(100, 26));
        btnAdd.setPreferredSize(new java.awt.Dimension(100, 26));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        pnlButtons.add(btnAdd, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHEAST;
        add(pnlButtons, gridBagConstraints);

    }//GEN-END:initComponents

    /** Getter for property saveRequired.
     * @return Value of property saveRequired.
     */
    public boolean isSaveRequired() {
        return saveRequired;
    } 
    
    //Added by Amit 11/21/2003
    /** This method use to implement focus on first editable component in this page.
     */
    public void setDefaultFocusForComponent(){
        //if(!( functionType == CoeusGuiConstants.DISPLAY_MODE )) {            
        
            if(tblProtocolNotes.getRowCount() > 0 ) {
                tblProtocolNotes.requestFocusInWindow();
                tblProtocolNotes.setRowSelectionInterval(0, 0);
                tblProtocolNotes.setColumnSelectionInterval(0,1);
            }else if( btnAdd.isEnabled() ) {
                btnAdd.requestFocusInWindow();
            }            
        //}
    }    
    //end Amit      

    /** Setter for property saveRequired.
     * @param saveRequired New value of property saveRequired.
     */
    public void setSaveRequired(boolean saveRequired) {
        this.saveRequired = saveRequired;
    }    
    /** Getter for property functionType.
     * @return Value of property functionType.
     */
    public char getFunctionType() {
        return functionType;
    }    
    
    /** Setter for property functionType.
     * @param functionType New value of property functionType.
     */
    public void setFunctionType(char functionType) {
        this.functionType = functionType;
    }    
    
    /** Getter for property protocolVecNotes.
     * @return Value of property protocolVecNotes.
     */
    public Vector getProtocolVecNotes() {
        return protocolVecNotes;
    }
    
    /** Setter for property protocolVecNotes.
     * @param protocolVecNotes New value of property protocolVecNotes.
     */
    public void setProtocolVecNotes(Vector protocolVecNotes) {
        this.protocolVecNotes = protocolVecNotes;
        setFormData();
        setEditors();
    }
    
    /** Getter for property protocolNumber.
     * @return Value of property protocolNumber.
     */
    public java.lang.String getProtocolNumber() {
        return protocolNumber;
    }    
    
    /** Setter for property protocolNumber.
     * @param protocolNumber New value of property protocolNumber.
     */
    public void setProtocolNumber(java.lang.String protocolNumber) {
        this.protocolNumber = protocolNumber;
    }
    
    public void update(Observable o, Object arg) {
        if( o instanceof LockObservable ) {
            recordLockedStatus = ((LockObservable)o).getLockStatus();
        }
    }
    
    /** Getter for property sequenceNumber.
     * @return Value of property sequenceNumber.
     *
     */
    public int getSequenceNumber() {
        return sequenceNumber;
    }
    
    /** Setter for property sequenceNumber.
     * @param sequenceNumber New value of property sequenceNumber.
     *
     */
    public void setSequenceNumber(int sequenceNumber) {
        this.sequenceNumber = sequenceNumber;
    }
    
     public void registerLockObservable(Observer observer) {
         lockObservable.addObserver(observer); 
     }
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    public javax.swing.JButton btnAdd;
    public javax.swing.JMenu jMenu1;
    public javax.swing.JMenuBar jMenuBar1;
    public javax.swing.JPanel pnlButtons;
    public javax.swing.JPanel pnlProtocolNotesGridViewer;
    public javax.swing.JScrollPane scrPnProtocolNotesViewer;
    public javax.swing.JTable tblProtocolNotes;
    // End of variables declaration//GEN-END:variables
    
}
