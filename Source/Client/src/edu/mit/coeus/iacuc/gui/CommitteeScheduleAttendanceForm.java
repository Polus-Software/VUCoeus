/*
 * @(#)CommitteeScheduleAttendanceForm.java Created on November 18, 2002, 10:06 AM
 *
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 */

/* PMD check performed, and commented unused imports and variables on 15-SEP-2010
 * by Johncy M John
 */

package edu.mit.coeus.iacuc.gui;

import java.awt.*;
import java.awt.event.*;
import java.util.Vector;
import java.util.HashMap;
import javax.swing.*;
import javax.swing.table.*;
import javax.swing.event.*;

import edu.mit.coeus.utils.*;
import edu.mit.coeus.gui.*;
import edu.mit.coeus.search.gui.*;
import edu.mit.coeus.rolodexmaint.gui.*;
import edu.mit.coeus.departmental.gui.PersonDetailForm;
import edu.mit.coeus.brokers.*;
import edu.mit.coeus.iacuc.bean.AbsenteesInfoBean;
import edu.mit.coeus.iacuc.bean.AttendanceInfoBean;
import edu.mit.coeus.iacuc.bean.CommitteeActiveMemberBean;
       
/**
 * This component is used to Maintain the Attendents List for Specific Committee
 * Schedule. It provides provision for adding attdents who are termed as guests
 * from person or rolodex search. Initially Attendents List will be created
 * from the Active Member List. Alternate For provision is allowed to Active
 * Members of the committee.
 *
 * @author  subramanya
 */
public class CommitteeScheduleAttendanceForm extends javax.swing.JComponent
                                                implements
                                                ListSelectionListener,
                                                ActionListener {
    //holds reference to  CoeusAppletMDIForm
    private CoeusAppletMDIForm mdiForm = null;
    
    private String scheduleId;

    //holds the boolean flag for attendents table selection
    //private boolean firstEntry = false;

    //holds the constant for display mode type
    private static final char DISPLAY_MODE = 'D';

    //holds the constant for update  AC type
    private static final String UPDATE_ACTYPE = "U";

    //holds the constant for Insert AC type
    private static final String INSERT_ACTYPE = "I";

    //holds the fucntion type
    private char functionType = DISPLAY_MODE;

    //holds collection of current table Attedents
    private Vector currentAttendents = null;


    //holds collection of current table Absentees
    private Vector currentAbsentees = null;

    // this will hold the attendents deleted
    private Vector deletedAttendent = null ;
    
    //holds the so far saved info
    private boolean isFormDataUpdated = false;

    //holds CoeusMessageResources instance used for reading message Properties.
    private CoeusMessageResources coeusMessageResources;

    //holds committee Id
    private String committeeId;

    //holds constant value for the boolean flag width of a table
    private static final int BOOLEAN_COLUMN_SIZE = 100;

    //holds constant value for the boolean flag width of a table
    private static final int HANDICON_COLUMN_SIZE = 30;

    //holds the zero count value
    private static final int ZERO_COUNT = 0;

    //holds the last selected Row
    private int lastSelectedRow = 0;

    private int prevRow=0;
    
    private boolean updateCommentsData=true;
    
    //holds the coeus dialog
    private CoeusDlgWindow dlgActiveMembers = null;

    private CoeusUtils coeusUtils = CoeusUtils.getInstance();
    
    //COEUSQA-2804 - IACUC cannot indicate alternate for IACUC schedule
    private static final int IACUC_COMMITTEE_TYPE_CODE = 2;
     
    /** Creates new form CommitteeScheduleAttendanceForm
     */
    public CommitteeScheduleAttendanceForm() {
    }

    /**
     * Creates new form CommitteeScheduleAttendanceForm with specific mdi form.
     * @param fnType Function Type character variable 'D', 'E' for display/edit.
     * @param attendentsData collection of AttendanceInfoBean
     * @param mdiForm  CoeusAppletMDIForm
     */
    public CommitteeScheduleAttendanceForm( char fnType, Vector attendentsData,
                                            CoeusAppletMDIForm mdiForm ) {
        this.functionType = fnType;
        this.mdiForm = mdiForm;
        initComponents();
        postInitComponents();
    }

     /**
     * Creates new form CommitteeScheduleAttendanceForm with specific mdi form.
     * @param mdiForm  CoeusAppletMDIForm
     * @param comID String variable represent Committee ID
     * @param attendentsData collection of AttedanceInfoBean
     * @param absenteesData collection of AbsenteeInfoBean
     */
    public CommitteeScheduleAttendanceForm( CoeusAppletMDIForm mdiForm,
                                                        char fnType,
                                                        String comID,
                                                        Vector attendentsData,
                                                        Vector absenteesData ) {
        this.mdiForm = mdiForm;
        this.functionType = fnType;
        this.committeeId = comID;
        this.currentAttendents = attendentsData;        
        this.currentAbsentees = absenteesData;
        initComponents();

        // Added by chandra 12/09/2003
        java.awt.Component[] components = {tblAttendents,btnMembers,btnPerson,btnRolodex,btnDelete,txtArComments};
        ScreenFocusTraversalPolicy  traversePolicy = new ScreenFocusTraversalPolicy( components );
        setFocusTraversalPolicy(traversePolicy);
        setFocusCycleRoot(true);
        // End chandra
        
        postInitComponents(); 
    }
    
    /**
     * Creates new form CommitteeScheduleAttendanceForm with specific mdi form.
     * @param mdiForm  CoeusAppletMDIForm
     * @param comID String variable represent Committee ID
     * @param attendentsData collection of AttedanceInfoBean
     * @param absenteesData collection of AbsenteeInfoBean
     */
    public CommitteeScheduleAttendanceForm( CoeusAppletMDIForm mdiForm,
                                                        char fnType,
                                                        String comID,
                                                        String scheduleID,
                                                        Vector attendentsData,
                                                        Vector absenteesData ) {
        this(mdiForm, fnType, comID, attendentsData, absenteesData);
        this.scheduleId = scheduleID;
    }
    
    


    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc=" Generated Code ">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        scrPnAttendents = new javax.swing.JScrollPane();
        tblAttendents = new javax.swing.JTable();
        scrPnComments = new javax.swing.JScrollPane();
        txtArComments = new javax.swing.JTextArea();
        scrPnAbsentees = new javax.swing.JScrollPane();
        tblAbsentees = new javax.swing.JTable();
        btnMembers = new javax.swing.JButton();
        btnPerson = new javax.swing.JButton();
        btnRolodex = new javax.swing.JButton();
        btnDelete = new javax.swing.JButton();

        setLayout(new java.awt.GridBagLayout());

        scrPnAttendents.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEtchedBorder(), "Attendance", javax.swing.border.TitledBorder.LEFT, javax.swing.border.TitledBorder.TOP, CoeusFontFactory.getLabelFont()));
        scrPnAttendents.setPreferredSize(new java.awt.Dimension(700, 265));
        tblAttendents.setFont(CoeusFontFactory.getNormalFont());
        tblAttendents.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Empty", "Name", "Guest Flag", "Alternate", "Alternate For"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Object.class, java.lang.Object.class, java.lang.Boolean.class, java.lang.Boolean.class, java.lang.Object.class
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
        tblAttendents.setPreferredScrollableViewportSize(new java.awt.Dimension(650, 165));
        scrPnAttendents.setViewportView(tblAttendents);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridwidth = 9;
        gridBagConstraints.gridheight = 6;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.ipadx = 1;
        add(scrPnAttendents, gridBagConstraints);

        scrPnComments.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEtchedBorder(), "Comments", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, CoeusFontFactory.getLabelFont()));
        scrPnComments.setPreferredSize(new java.awt.Dimension(700, 100));
        txtArComments.setDocument(new LimitedPlainDocument(2000));
        txtArComments.setFont(CoeusFontFactory.getNormalFont());
        txtArComments.setEnabled(false);
        scrPnComments.setViewportView(txtArComments);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.gridwidth = 9;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        add(scrPnComments, gridBagConstraints);

        scrPnAbsentees.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEtchedBorder(), "Absentees", javax.swing.border.TitledBorder.LEFT, javax.swing.border.TitledBorder.TOP, CoeusFontFactory.getLabelFont()));
        scrPnAbsentees.setPreferredSize(new java.awt.Dimension(700, 165));
        tblAbsentees.setFont(CoeusFontFactory.getNormalFont());
        tblAbsentees.setModel( new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Empty", "Member Name"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Object.class, java.lang.String.class
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
        tblAbsentees.setRowSelectionAllowed(false);
        scrPnAbsentees.setViewportView(tblAbsentees);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.gridwidth = 9;
        gridBagConstraints.gridheight = 4;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTH;
        add(scrPnAbsentees, gridBagConstraints);

        btnMembers.setFont(CoeusFontFactory.getLabelFont());
        btnMembers.setText("Members");
        btnMembers.setToolTipText("Members of the committee");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 9;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 5, 4, 0);
        add(btnMembers, gridBagConstraints);

        btnPerson.setFont(CoeusFontFactory.getLabelFont());
        btnPerson.setText("Person");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 9;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 5, 4, 0);
        add(btnPerson, gridBagConstraints);

        btnRolodex.setFont(CoeusFontFactory.getLabelFont());
        btnRolodex.setText("Rolodex");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 9;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 5, 4, 0);
        add(btnRolodex, gridBagConstraints);

        btnDelete.setFont(CoeusFontFactory.getLabelFont());
        btnDelete.setText("Delete");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 9;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 5, 4, 0);
        add(btnDelete, gridBagConstraints);

    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    public javax.swing.JButton btnDelete;
    public javax.swing.JButton btnMembers;
    public javax.swing.JButton btnPerson;
    public javax.swing.JButton btnRolodex;
    public javax.swing.JScrollPane scrPnAbsentees;
    public javax.swing.JScrollPane scrPnAttendents;
    public javax.swing.JScrollPane scrPnComments;
    public javax.swing.JTable tblAbsentees;
    public javax.swing.JTable tblAttendents;
    public javax.swing.JTextArea txtArComments;
    // End of variables declaration//GEN-END:variables


   /** This method is called from within the constructor to
     * initialize the form after the Initialization of components invoked.
     */
    private void postInitComponents() {
        tblAttendents.getTableHeader().setFont(CoeusFontFactory.getLabelFont());
        tblAttendents.getSelectionModel().addListSelectionListener( this );

        setAttedentsFormData();        
        txtArComments.setLineWrap(true);
        txtArComments.setWrapStyleWord(true);
        tblAbsentees.getTableHeader().setFont(CoeusFontFactory.getLabelFont());
        //if Absentee table has more then one row by default select first row
        if( tblAbsentees.getRowCount() > ZERO_COUNT ){
            tblAbsentees.getSelectionModel().setSelectionMode(
                                          ListSelectionModel.SINGLE_SELECTION );
//            tblAbsentees.addRowSelectionInterval( ZERO_COUNT, ZERO_COUNT );            
        }
        /*if Attendent table has more then one row by default select first row
         * set the respective comment Text Area
         */
        if( tblAttendents.getRowCount() > ZERO_COUNT ){
            tblAttendents.getSelectionModel().setSelectionMode(
                                          ListSelectionModel.SINGLE_SELECTION );
            tblAttendents.addRowSelectionInterval( ZERO_COUNT, ZERO_COUNT );
            txtArComments.setEnabled( true );
            // Added by chandra - TextAreas.doc
            txtArComments.setLineWrap(true);
            txtArComments.setWrapStyleWord(true);
            // End
            txtArComments.setText(
            (( AttendanceInfoBean ) currentAttendents.get( ZERO_COUNT )).getComments());
            txtArComments.setCaretPosition(0);
//            setAbesenteeFormData();
        }
        setAbesenteeFormData();
        setHandIconProperties( tblAttendents );
        setHandIconProperties( tblAbsentees );

        if( functionType != DISPLAY_MODE ){
         txtArComments.addFocusListener( new FocusAdapter(){
            public void focusLost( FocusEvent focusEvent ) {
                
                //used to set the edited text value of comment text area to
                //the respective data bean
                if(tblAttendents.getRowCount()>0){
                    int selRow = tblAttendents.getSelectedRow();
                    if(selRow >=0){
                        AttendanceInfoBean selectedAttnBean =  (AttendanceInfoBean )
                                         currentAttendents.get(selRow);
                        if(isFormDataUpdated!=true){
                            if(selectedAttnBean.getComments()==null && 
                                txtArComments.getText()!=null && txtArComments.getText().trim().length()>0){
                                isFormDataUpdated=true;
                             }else if(selectedAttnBean.getComments() != null && 
                                !selectedAttnBean.getComments().equalsIgnoreCase(
                                    txtArComments.getText())){
                                    isFormDataUpdated = true;
                             }
                        }
                        selectedAttnBean.setComments( txtArComments.getText() );
                        //isFormDataUpdated = true;
                        //update the AcType if is Null
                        if( selectedAttnBean.getAcType() == null ){
                            selectedAttnBean.setAcType( UPDATE_ACTYPE );
                        }
                        currentAttendents.set(selRow, selectedAttnBean);
                    }
                }else{
                btnMembers.requestFocusInWindow();
                    btnMembers.setFocusable(true);
                }
            }
         });
        }

        TableColumn clmAttendent = tblAttendents.getColumn( "Guest Flag" );
        clmAttendent.setMaxWidth( BOOLEAN_COLUMN_SIZE );
        clmAttendent = tblAttendents.getColumn( "Alternate" );
        
        clmAttendent.setMaxWidth( BOOLEAN_COLUMN_SIZE );

        tblAttendents.addMouseListener( new MouseAdapter(){
            public void mouseReleased( MouseEvent mouseEvent ){
                int selectedRow = tblAttendents.getSelectedRow();
                AttendanceInfoBean selectedAttnData = ( AttendanceInfoBean )
                                           currentAttendents.get( selectedRow );
                if( mouseEvent.getClickCount() == 1 &&
                    tblAttendents.getSelectedColumn() ==  3 &&
                    functionType != DISPLAY_MODE ){
                        //used for Alternate For Boolean Selection for Active Members
                if( selectedAttnData.getGuestFlag() ){
                    return;
                }
                if( selectedAttnData.getAlternateFlag() ){
                    selectedAttnData.setAlternateFlag( false );
                    selectedAttnData.setAlternateFor( null );
                    selectedAttnData.setAlternatePersonName( null );
                    String acType = selectedAttnData.getAcType();
                    if( acType == null ||  
                                    !acType.equalsIgnoreCase( INSERT_ACTYPE )){                        
                        selectedAttnData.setAcType( UPDATE_ACTYPE );
                    }
                    tblAttendents.setValueAt(
                                        new Boolean(false), selectedRow, 3 );
                    tblAttendents.setValueAt( null, selectedRow, 4 );
                    isFormDataUpdated = true;
                    return ;
                }
               if( userHasAlternateRole(selectedAttnData.getPersonId(),scheduleId) ){ 
                   dlgActiveMembers = new CoeusDlgWindow(CoeusGuiConstants.getMDIForm(),
                                            "Active Members List",true );
                   //COEUSQA-2804 - IACUC cannot indicate alternate for IACUC schedule
                   final edu.mit.coeus.iacuc.gui.CommitteeScheduleActiveMembers  activeMemberDialog =
                            new edu.mit.coeus.iacuc.gui.CommitteeScheduleActiveMembers( committeeId,
                                                                scheduleId,
                                                                dlgActiveMembers, 
                                                                true );
                   dlgActiveMembers.getContentPane().add( activeMemberDialog );
                   dlgActiveMembers.setLocationRelativeTo( null );
                   dlgActiveMembers.pack();
                   activeMemberDialog.requestDefaultFocusForComponent();
                   dlgActiveMembers.addEscapeKeyListener(
                        new AbstractAction("escPressed"){
                            public void actionPerformed(ActionEvent ae){
                                activeMemberDialog.clearMemberSelection();
                                dlgActiveMembers.dispose();
                            }
                   });
                   dlgActiveMembers.addWindowListener( new WindowAdapter(){
                        public void windowClosing(WindowEvent we){
                            activeMemberDialog.clearMemberSelection();
                            dlgActiveMembers.dispose();
                        }
                   });
                   dlgActiveMembers.show();

                   Vector selectedActiveMebmers =
                                            activeMemberDialog.getSelectedMembers();
                   CommitteeActiveMemberBean rowData = null;
                   String perID = "";
                   String perName = "";

                   //get the selected members from the Active member window
                   if( selectedActiveMebmers != null &&
                                selectedActiveMebmers.size() > ZERO_COUNT ){
                        rowData = (CommitteeActiveMemberBean)
                                            selectedActiveMebmers.get( ZERO_COUNT );
                        perName =  rowData.getPersonName();
                        perID = rowData.getPersonID();

                        //check for duplicate in the Attedent List
                       boolean isDuplicate = checkDuplicateAttendentName( perID,
                                                                          perName );
                       //check for the duplicate entry list
                        if( ! isDuplicate ){
                            tblAttendents.setValueAt(
                                                new Boolean(true), selectedRow, 3 );
                            tblAttendents.setValueAt( perName, selectedRow, 4 );

                            selectedAttnData.setAlternateFlag( true );
                            if( selectedAttnData.getAcType() == null ){
                                selectedAttnData.setAcType( UPDATE_ACTYPE );
                            }
                            isFormDataUpdated = true;
                            selectedAttnData.setAlternateFor( perID );
                            currentAttendents.remove( selectedRow );
                            currentAttendents.add( selectedRow, selectedAttnData );
                        }else{
                            //raghuSV: to show the message rightly
                            //starts..
    //                        String name = (String)tblAttendents.getValueAt(selectedRow,1);
                            //CoeusOptionPane.showInfoDialog(
                            //    coeusMessageResources.parseMessageKey("commSchdAtteFrm_exceptionCode.1607"));
                            CoeusOptionPane.showInfoDialog(perName+" is already listed as an Attendee for this meeting");    
                            //ends.
                         }
                    }
               }else{
                    CoeusOptionPane.showInfoDialog(
                        coeusMessageResources.parseMessageKey("commSchAttendenceForm_exceptionCode.1218"));
               }
               //used for detail pop window for person, rolodex and committee
               //detail
            }else if( mouseEvent.getClickCount() == 2 &&
                    tblAttendents.getSelectedColumn() ==  1 ){
                
                    if( selectedAttnData.getGuestFlag() ){
                        if( selectedAttnData.getNonEmployeeFlag() ){
                         //disply roldex detail screen.
                           RolodexMaintenanceDetailForm rolodexForm =
                            new RolodexMaintenanceDetailForm( 'V',
                                            selectedAttnData.getPersonId());
                           rolodexForm.showForm( mdiForm, "Rolodex Details",
                                                                        true );
                        }else{
                         try{
                             //Bug Fix: Pass the person id to get the person details Start.
                              //int selRow = tblAttendents.getSelectedRow();
                              String loginUserName = mdiForm.getUserName();
                              //String personName=(String)tblAttendents.getValueAt(selRow,1);
                              /*Bug Fix to get the person details with the person id instead of the person name*/
                              //PersonInfoFormBean PersonInfoFormBean = (PersonInfoFormBean)coeusUtils.getPersonInfoID(personName);
                              //PersonDetailForm personDetailForm 
                                //= new PersonDetailForm(PersonInfoFormBean.getPersonID(),loginUserName,'D');
                             PersonDetailForm personDetailForm 
                                = new PersonDetailForm(selectedAttnData.getPersonId() ,loginUserName,'D');
                             //Bug Fix: Pass the person id to get the person details End.
                             
                          }catch(Exception exception){
                              exception.printStackTrace();
                          }
                        }
                    }else{
                        //calls the Member details window to show member details
                        new MemberDetailsForm( committeeId,
                                           selectedAttnData.getPersonId(),'D');
                    }
            }
            }
        });
        
        //Added by Vyjayanthi on 13/01/2004
        //Bug-fix code to display member details on double click of absentees - Start
        tblAbsentees.addMouseListener(new MouseAdapter(){
            public void mouseClicked(MouseEvent mouseEvent){
                if( mouseEvent.getClickCount() == 2 ){
                    int selectedRow = tblAbsentees.getSelectedRow();
                    /*
                     *  Commented by Geo to refix the bug
                     *
                    AttendanceInfoBean selectedAttnData = ( AttendanceInfoBean )
                                           currentAbsentees.get( selectedRow );
                    new MemberDetailsForm( committeeId, selectedAttnData.getPersonId(),
                    TypeConstants.DISPLAY_MODE);
                    */
                    AbsenteesInfoBean selectedAbsData = ( AbsenteesInfoBean )
                                           currentAbsentees.get( selectedRow );
                    new MemberDetailsForm( committeeId, selectedAbsData.getPersonId(),
                    TypeConstants.DISPLAY_MODE);
                    
                }
            }
        });
        //Bug-fix code to display member details on double click of absentees - End
        
        coeusMessageResources = CoeusMessageResources.getInstance();
        if( this.functionType == this.DISPLAY_MODE ){
            
            //for Attendents....
            tblAttendents.setBackground((Color) UIManager.getDefaults().get("Panel.background"));
            tblAttendents.setSelectionBackground((Color) UIManager.getDefaults().get("Panel.background"));
            tblAttendents.setSelectionForeground(Color.black);
            
            //For Absentees...
            tblAbsentees.setBackground((Color) UIManager.getDefaults().get("Panel.background"));
//            tblAbsentees.setSelectionBackground((Color) UIManager.getDefaults().get("Panel.background"));
//            tblAbsentees.setSelectionForeground(Color.black);
            
            //For comments...
            txtArComments.setBackground((Color) UIManager.getDefaults().get("Panel.background"));
            txtArComments.setForeground(Color.black);
            
            btnMembers.setEnabled( false );
            btnRolodex.setEnabled( false );
            btnPerson.setEnabled( false );
            btnDelete.setEnabled( false );
            txtArComments.setEditable( false );
        }else{
            btnMembers.addActionListener( this );
            btnMembers.setMnemonic( 'M' );
            btnRolodex.addActionListener( this );
            btnRolodex.setMnemonic( 'R' );
            btnPerson.addActionListener( this );
            btnPerson.setMnemonic( 'P' );
            btnDelete.addActionListener( this );
            btnDelete.setMnemonic( 'D' );
        }
    }

    private boolean userHasAlternateRole(String personId, String scheduleId){
       RequesterBean requester = new RequesterBean();
       requester.setFunctionType('I');// to check ALTERNATE_ROLE
       Vector dataObjects = new Vector();
       dataObjects.addElement(personId);
       dataObjects.addElement(scheduleId);
       //COEUSQA-2804 - IACUC cannot indicate alternate for IACUC schedule
       dataObjects.addElement(IACUC_COMMITTEE_TYPE_CODE);
       requester.setDataObjects(dataObjects);
       String connectTo = CoeusGuiConstants.CONNECTION_URL + "/scheduleMaintSrvlt";
       AppletServletCommunicator comm
                = new AppletServletCommunicator(connectTo, requester);
       comm.send();
       ResponderBean response = comm.getResponse();
       if (response!=null && response.isSuccessfulResponse()){
            Integer hasRight = (Integer)response.getDataObject();
            return (hasRight.intValue() == 1) ? true : false;
        }
        
        return false;
    }
    //Added by Amit 11/21/2003
    /** This method use to implement focus on first editable component in this page.
     */
    public void setDefaultFocusForComponent(){        
            if(tblAttendents.getRowCount() > 0 ) {
                int rowNum = tblAttendents.getSelectedRow();
                tblAttendents.requestFocusInWindow();
                if(rowNum > 0){                    
                    tblAttendents.setRowSelectionInterval(rowNum, rowNum);
                    btnDelete.setEnabled(true);
                    prevRow=tblAttendents.getSelectedRow();
                }
                tblAttendents.setColumnSelectionInterval(1,1);
            }else{
                btnMembers.requestFocusInWindow();
                btnDelete.setEnabled(false);
            }                    
    }    
    //end Amit       

    //supporting method to set the Attendents Data
    private void setAttedentsFormData(){
        Vector attendentTableData = new Vector();
        AttendanceInfoBean attnData = null;
        Vector attendentTableRowData = null;

        if( currentAttendents != null ) {

            //iteration through the attedents bean and construct table data
            //vector
            for( int indx = ZERO_COUNT; indx<currentAttendents.size(); indx++ ){
                attnData = ( AttendanceInfoBean ) currentAttendents.get( indx );
                attendentTableRowData = new Vector();
                attendentTableRowData.add( "" );
                attendentTableRowData.add( attnData.getPersonName() );
                attendentTableRowData.add(new Boolean(attnData.getGuestFlag()));
                attendentTableRowData.add(
                                      new Boolean(attnData.getAlternateFlag()));
                attendentTableRowData.add( attnData.getAlternatePersonName() );
                attendentTableData.add( attendentTableRowData );
            }
			if (currentAttendents.size() >= 0) {
				attnData = ( AttendanceInfoBean ) currentAttendents.get( 0 );
				txtArComments.setText(attnData.getComments());
			}
        }
        
        ((DefaultTableModel)tblAttendents.getModel()).setDataVector(
                                            attendentTableData,
                                            getAtendentsTableColumnNames());
        
        
    }

     //supporting method to set the Attendents Data
    private void setAbesenteeFormData(){

        Vector absenteeTableData = new Vector();
        AbsenteesInfoBean absnData = null;
        Vector absenteeTableRowData = null;

        //this block will construct absentee table data vector from
        //absenteeInfo Bean
        if( currentAbsentees != null ) {
            //iteration through absentee list and construct table data vector
            for( int indx = ZERO_COUNT;indx < currentAbsentees.size(); indx++ ){
                absnData = ( AbsenteesInfoBean ) currentAbsentees.get( indx );
                absenteeTableRowData = new Vector();
                absenteeTableRowData.add( "" );
                absenteeTableRowData.add( absnData.getPersonName() );
                absenteeTableData.add( absenteeTableRowData );
            }
        }
        ((DefaultTableModel)tblAbsentees.getModel()).setDataVector(
                                            absenteeTableData,
                                            getAbsenteesTableColumnNames());
    }


    //supporting method to get the column Name of Absentees Table.
    private Vector getAbsenteesTableColumnNames(){
        Vector absnName = new Vector();
        absnName.add( "" );
        absnName.add( "Member Name" );
        return absnName;
    }

    //supporting method to get the column Name of Absentees Table.
    private Vector getAtendentsTableColumnNames(){
        Vector attnName = new Vector();
        attnName.add( "" );
        attnName.add( "Name" );
        attnName.add( "Guest Flag" );
        attnName.add( "Alternate" );
        attnName.add( "Alternate For" );
        return attnName;
    }



    /**
     * This Method is used to Find-Member-Person-Rolodex Actions
     * @param btnAction denote the ActionEvent for the buttons.
     */
    public void actionPerformed( ActionEvent btnActionEvent ){

        Object actionSource = btnActionEvent.getSource();
        CoeusSearch attndSearch = null;
        HashMap searchResultSet =  null;

        //action associated with member button
        if( actionSource.equals( btnMembers ) ){
            
            //raghuSV : below code helps to maintain the consistency of the row addition.
            //starts....
            
            updateCommentsData=false;
            
            int selected=0;
            if(tblAttendents.getRowCount()== 1){
                selected=tblAttendents.getSelectedRow(); 
                AttendanceInfoBean prevAttenBean = (AttendanceInfoBean)
                                           currentAttendents.get( 0 );
//                prevAttenBean.setComments(txtArComments.getText());
            }
            //ends
            
            
            dlgActiveMembers = new CoeusDlgWindow(CoeusGuiConstants.getMDIForm(), 
                                    "Active Members List",true );
            // Modified for COEUSQA-2686 IACUC - Changes to schedule maintenance for 4.4.3 release_start
            final CommitteeScheduleActiveMembers  activeMemberDialog =
                        new CommitteeScheduleActiveMembers( this.committeeId,
                                                            scheduleId,
                                                            dlgActiveMembers,
                                                            false );
            // Modified for COEUSQA-2686 IACUC - Changes to schedule maintenance for 4.4.3 release_end
            dlgActiveMembers.getContentPane().add( activeMemberDialog );
            dlgActiveMembers.setLocationRelativeTo( null );
            dlgActiveMembers.pack();
           activeMemberDialog.requestDefaultFocusForComponent();
           dlgActiveMembers.addEscapeKeyListener(
                new AbstractAction("escPressed"){
                    public void actionPerformed(ActionEvent ae){
                        activeMemberDialog.clearMemberSelection();
                        dlgActiveMembers.dispose();
                    }
           });
           dlgActiveMembers.addWindowListener( new WindowAdapter(){
                public void windowClosing(WindowEvent we){
                    activeMemberDialog.clearMemberSelection();
                    dlgActiveMembers.dispose();
                }
           });
           dlgActiveMembers.show();

            Vector selectedActiveMebmers =
                                        activeMemberDialog.getSelectedMembers();

            CommitteeActiveMemberBean rowData = null;
            String perID, perName;
            boolean blnEmpFlag = false;
            boolean alternatePerson;

            if( selectedActiveMebmers != null &&
                selectedActiveMebmers.size() > ZERO_COUNT ){
                //insertion rotine for the selected active member list
                for( int indx = ZERO_COUNT;
                                   indx < selectedActiveMebmers.size(); indx++){
                    rowData = (CommitteeActiveMemberBean)
                                            selectedActiveMebmers.get( indx );
                    perID = rowData.getPersonID();
                    perName =  rowData.getPersonName();
                    blnEmpFlag = rowData.isEmployee();
                    alternatePerson  = rowData.isAlternatePerson();
                    //check for duplicate in the Attedent List
                    boolean isDuplicate = checkDuplicateAttendentName( perID,
                                                                       perName);
                    //check for duplicate entry continue if entry exists else
                    //remove from the absenttee list if the slected entry exists
                    if( isDuplicate ){
                        continue;
                    }else{
                        removeFromAbsenteesList( perID,alternatePerson );
                        constructNewAttendentPerson( perID, perName, !blnEmpFlag,
                                                                false, false,alternatePerson );
                        
                    }
                }
                
            }
            if(tblAttendents.getRowCount()>0){
            btnDelete.setEnabled(true);
        }
        else{
            btnDelete.setEnabled(false);
        }
            //action for Person button
        }else if( actionSource.equals( btnPerson ) ){
            
            int selected=0;
            if(tblAttendents.getRowCount()== 1){
                selected=tblAttendents.getSelectedRow(); 
                AttendanceInfoBean prevAttenBean = (AttendanceInfoBean)
                                           currentAttendents.get( 0 );
                prevAttenBean.setComments(txtArComments.getText());
            }
            //ends

            try{
                attndSearch = new CoeusSearch( mdiForm,
                                               CoeusGuiConstants.PERSON_SEARCH,
                                               CoeusSearch.TWO_TABS_WITH_MULTIPLE_SELECTION ) ;

                attndSearch.showSearchWindow();
                Vector vSelectedPersons = attndSearch.getMultipleSelectedRows() ;
                
                if (vSelectedPersons != null)
                {
                    for(int indx = 0; indx < vSelectedPersons.size(); indx++ )
                    {
                         searchResultSet = (HashMap)vSelectedPersons.get( indx ) ;                    
                
                        if( searchResultSet == null || searchResultSet.isEmpty() )
                        {
                            continue ;
                        }
                         

                        String personName = checkForNull(
                                                    searchResultSet.get( "FULL_NAME" ));
                        String personID = checkForNull(
                                                    searchResultSet.get( "PERSON_ID" ));
                        //construct the new person entry with default guest falg on
                        constructNewAttendent( personID, personName, false, true, false );
                    }
                }   


            }catch( Exception err ){
                CoeusOptionPane.showErrorDialog( err.getMessage());
            }
            //action for rolodex button
        }else if( actionSource.equals( btnRolodex ) ){
            
            int selected=0;
            if(tblAttendents.getRowCount()== 1){
                selected=tblAttendents.getSelectedRow(); 
                AttendanceInfoBean prevAttenBean = (AttendanceInfoBean)
                                           currentAttendents.get( 0 );
                prevAttenBean.setComments(txtArComments.getText());
            }
            
            try{
                attndSearch = new CoeusSearch( mdiForm,
                                               CoeusGuiConstants.ROLODEX_SEARCH,
                                               CoeusSearch.TWO_TABS_WITH_MULTIPLE_SELECTION ) ;
                attndSearch.showSearchWindow();
                 //prps code start May 12th 2003
                Vector vSelectedPersons = attndSearch.getMultipleSelectedRows() ;
                if (vSelectedPersons != null)
                {
                    for(int indx = 0; indx < vSelectedPersons.size(); indx++ )
                    {
                         searchResultSet = (HashMap)vSelectedPersons.get( indx ) ;                    
                
                        if( searchResultSet == null || searchResultSet.isEmpty() )
                        {
                            continue;
                        }

                        String rolodexID = checkForNull(
                                        searchResultSet.get( "ROLODEX_ID" ) );
                        String firstName = checkForNull(
                                                searchResultSet.get( "FIRST_NAME" ) );
                        String lastName = checkForNull(
                                                 searchResultSet.get( "LAST_NAME" ) );
                        String middleName = checkForNull(
                                                searchResultSet.get( "MIDDLE_NAME" ) );
                        String namePreffix = checkForNull(
                                                    searchResultSet.get( "PREFIX" ) );
                        String nameSuffix = checkForNull(
                                                    searchResultSet.get( "SUFFIX" ) );
                        String rolodexName = null;

                        if ( lastName.length() > ZERO_COUNT ) {
                            rolodexName = ( lastName +", " + nameSuffix + " " +
                                            namePreffix + " " +
                                            firstName + " " + middleName ).trim();
                        } else {
                            rolodexName = checkForNull(
                                                searchResultSet.get("ORGANIZATION") );
                        }
                       //construct the rolodex entry with defult guest and non emp falg
                        //on
                       constructNewAttendent(rolodexID, rolodexName, true, true, false);
                     
                    }//end for    
                    
                    
                }// end if    
                    
            }catch( Exception err ){
                CoeusOptionPane.showErrorDialog( err.getMessage());
            }

        }else if( actionSource.equals( btnDelete ) )
        {
            // get the attendent info bean and use that to build the absentees info bean
            // and then add that to the absentees table
            
            if(tblAttendents.getRowCount()== 0 || tblAttendents.getSelectedRowCount()==0){
                tblAttendents.setFocusable(false);
                btnMembers.setFocusable(true);
                btnMembers.requestFocusInWindow();
                btnMembers.requestFocus();
                
            }
            if(tblAttendents.getRowCount()>0 || tblAttendents.getSelectedRowCount()!=0){
                
            if (tblAttendents.getSelectedRowCount() >0) 
            {
                int selRow = tblAttendents.getSelectedRow() ;
               
                AttendanceInfoBean attendInfoBean = (AttendanceInfoBean)currentAttendents.get(selRow) ;
                // only members are added to absentees list
                if (attendInfoBean.getGuestFlag() == false)
                {
                    if(!attendInfoBean.isAlternatePerson()){// Case #1588
                        AbsenteesInfoBean absentInfoBean = new AbsenteesInfoBean() ;
                        absentInfoBean.setPersonId(attendInfoBean.getPersonId());
                        absentInfoBean.setPersonName(attendInfoBean.getPersonName()) ;
                        if (currentAbsentees == null) 
                        {
                          currentAbsentees = new Vector() ;  
                        }
                        currentAbsentees.add(absentInfoBean) ;
                    }
                   
                 }    
                    currentAttendents.remove(selRow) ; // remove it from the list
                    
                    // mark the attendant for deletion and save it in deletedAttendent 
                     attendInfoBean.setAcType("D") ; 
                    if (deletedAttendent == null)
                    {
                        deletedAttendent = new Vector() ;
                    }
                        deletedAttendent.add(attendInfoBean) ;
                    isFormDataUpdated = true ; //prps added om june 12 2003
                    
                    
                 // remove row from attendees table
                ((DefaultTableModel)tblAttendents.getModel()).removeRow(selRow);
                 tblAttendents.revalidate();
                
 
        //prps start
                 
            int rowCount = ((DefaultTableModel)tblAbsentees.getModel()).getRowCount() ;
                 
            for( int indx = ZERO_COUNT ; indx < rowCount; indx++)
            {
                // delete all rows 
                ((DefaultTableModel)tblAbsentees.getModel()).removeRow(ZERO_COUNT) ;
            }     
                 
            //Vector absenteeTableData = new Vector();
                
            if (currentAbsentees != null)
            {    
                for( int indx = ZERO_COUNT ; indx < currentAbsentees.size(); indx++)
                {
                    AbsenteesInfoBean absnData = ( AbsenteesInfoBean ) currentAbsentees.get( indx );
                    Vector newAbsentData = new Vector();
                    newAbsentData.add( "" );
                    newAbsentData.add( absnData.getPersonName() );
                    //absenteeTableData.add( newAbsentData );
                    // add row to absentees table
                    ((DefaultTableModel)tblAbsentees.getModel()).addRow(newAbsentData ) ;
                }
            }        
            
           //prps end 
            //Included by raghuSV to happen the selection of row appropriately
            //starts...
            int selectedRow=0;
            if(tblAttendents.getRowCount()>0 || tblAttendents.getSelectedRow()>0 )
            {
               if(selRow==0){
                    selectedRow=selRow;
               }
                else{
                     selectedRow=selRow-1;
                    
                }
			   //Added for bug Fix in Comments by Jobin on 30/11/2004 - start
			   lastSelectedRow = selectedRow;
			   attendInfoBean = (AttendanceInfoBean)currentAttendents.get(selectedRow);
			   txtArComments.setText(attendInfoBean.getComments());
			   // - end
               tblAttendents.setRowSelectionInterval(selectedRow, selectedRow);
               
            }
			//Added to remove all the comments if the row is 0 by Jobin on 30/11/2004 - start
			if (tblAttendents.getRowCount() == 0) {
			   txtArComments.setText("");
			} // end - Jobin
            //ends    
                 
            }
        }else if(tblAttendents.isFocusOwner()){
                btnMembers.requestFocusInWindow();
        }
            
        if(tblAttendents.getRowCount()>0){
            btnDelete.setEnabled(true);
        }
        else{
            btnDelete.setEnabled(false);
            btnMembers.requestFocusInWindow();
         }    
        }
        if(tblAttendents.getRowCount()>0){
            btnDelete.setEnabled(true);
        }
        else{
            btnDelete.setEnabled(false);
        }
    }


    //supporting method to check for null value
    private String checkForNull( Object value ){
        //rotine check for null entries if so makes is empty string
        return value == null ?  "" : value.toString();
    }

    /**
     * This method is used to capture the selction events of the Attendents
     * table.
     * @param listSelectionEvent ListSelectionEvent
     */
    public void valueChanged(ListSelectionEvent listSelectionEvent ) {
		// commented for bug fix committee details saving - start
       /*  String comment = null;
        //event catpured during table row selection to set the respective
        //comments
        int selectedRow = tblAttendents.getSelectedRow();
        
         if( selectedRow >= ZERO_COUNT ){
                       
           if(lastSelectedRow!=-1){
             //prps start - jan 08 2003  
               if (lastSelectedRow > selectedRow)
               {
                   lastSelectedRow = selectedRow ;
               }
              // prps end  - jan 08 2003   
               
                AttendanceInfoBean  updAttenBean = (AttendanceInfoBean)
                                           currentAttendents.get( lastSelectedRow );
                String currentComments = txtArComments.getText();
                String beanComments = updAttenBean.getComments();
                beanComments = beanComments == null ? "" : beanComments;
                if( !currentComments.equals(beanComments ) ) {
                 updAttenBean.setComments( txtArComments.getText() );
                 if( updAttenBean.getAcType() == null ) {
                    updAttenBean.setAcType( UPDATE_ACTYPE );
                 }
                 isFormDataUpdated = true;
                 currentAttendents.set(lastSelectedRow, updAttenBean);
                }
               
           }
             
           AttendanceInfoBean selectedAttnBean = (AttendanceInfoBean)
                                           currentAttendents.get( selectedRow );
           if( selectedAttnBean != null ){
                comment = selectedAttnBean.getComments();
                if( comment == null ){
                    comment = "";
                }
                txtArComments.setText( comment );
                txtArComments.setCaretPosition(0);
           }
          lastSelectedRow = selectedRow;
 
        }*/
		/*ended comments by Jobin*/
		// added for bug fix committee details saving - start
		int selectedRow = tblAttendents.getSelectedRow();
		if(currentAttendents !=null && currentAttendents.size() > 0 && selectedRow != -1) {
                AttendanceInfoBean  updAttenBean = (AttendanceInfoBean)currentAttendents.get(lastSelectedRow);
                
  			if (updAttenBean.getComments() == null ) {
				   updAttenBean.setComments("");
			}
                if(!txtArComments.getText().equals(updAttenBean.getComments())) {
                    updAttenBean.setComments(txtArComments.getText());
                    if(updAttenBean.getAcType()==null){
                        updAttenBean.setAcType(TypeConstants.UPDATE_RECORD);
						isFormDataUpdated = true;
                    }
                }
			lastSelectedRow = selectedRow;
			//Setting text to last selected Row - End
			updAttenBean = null;
            updAttenBean = (AttendanceInfoBean) currentAttendents.get(selectedRow);
			if (updAttenBean.getComments() == null ) {
				   updAttenBean.setComments("");
			}
            txtArComments.setText(updAttenBean.getComments()) ;
            txtArComments.setCaretPosition(0);
                
            }//end Jobin
    }


    //supporting method to construct New Attendent Entry
    private AttendanceInfoBean constructNewAttendent( String personID,
                                        String personName,
                                        boolean isNonEmployee,
                                        boolean isGuest,
                                        boolean isAlternateFor){

     boolean isDuplicate = checkDuplicateAttendentName( personID, personName );
     //if respective person is duplicate return the control back to the caller.
     if( isDuplicate ){
        return null;
     }
     Vector newAttendentData = new Vector();
        newAttendentData.add( "" );
        newAttendentData.add( personName );
        newAttendentData.add( new Boolean( isGuest ) );
        newAttendentData.add( new Boolean( isAlternateFor) );
        newAttendentData.add( "" ); //default value
        ((DefaultTableModel)tblAttendents.getModel()).addRow(newAttendentData );
        tblAttendents.revalidate();

        //construct new attendent info bean and adds to the current attdent list
        AttendanceInfoBean newAttnBean = new AttendanceInfoBean();
        newAttnBean.setPersonId( personID );
        newAttnBean.setPersonName( personName );
        newAttnBean.setNonEmployeeFlag( isNonEmployee );
        newAttnBean.setGuestFlag( isGuest );
        newAttnBean.setAlternateFlag( isAlternateFor );
        newAttnBean.setComments("");
        newAttnBean.setAcType( INSERT_ACTYPE );

        //check for the current attedent list exits or not.
        if ( currentAttendents == null ) {
            currentAttendents = new Vector();
            txtArComments.setEnabled( true );
        }
        currentAttendents.add( newAttnBean );
        txtArComments.requestFocus();
        txtArComments.setText( "" );

        isFormDataUpdated = true;
        int newInsertedRowIndex = tblAttendents.getRowCount() - 1;
		lastSelectedRow  = newInsertedRowIndex; // Added for bug fix committee details saving 
        tblAttendents.setRowSelectionInterval( newInsertedRowIndex,
                                               newInsertedRowIndex );
        tblAttendents.scrollRectToVisible( tblAttendents.getCellRect(
                                                newInsertedRowIndex ,
                                                ZERO_COUNT, true));
		return newAttnBean;
    }
    
    //supporting method to construct New Attendent Entry
    private AttendanceInfoBean constructNewAttendentPerson( String personID,
                                        String personName,
                                        boolean isNonEmployee,
                                        boolean isGuest,
                                        boolean isAlternateFor,
                                        boolean alternatePerson){

     boolean isDuplicate = checkDuplicateAttendentName( personID, personName );
     //if respective person is duplicate return the control back to the caller.
     if( isDuplicate ){
        return null;
     }
     Vector newAttendentData = new Vector();
        newAttendentData.add( "" );
        newAttendentData.add( personName );
        newAttendentData.add( new Boolean( isGuest ) );
        newAttendentData.add( new Boolean( isAlternateFor) );
        newAttendentData.add( "" ); //default value
        newAttendentData.add( new Boolean(alternatePerson )); 
        ((DefaultTableModel)tblAttendents.getModel()).addRow(newAttendentData );
        tblAttendents.revalidate();

        //construct new attendent info bean and adds to the current attdent list
        AttendanceInfoBean newAttnBean = new AttendanceInfoBean();
        newAttnBean.setPersonId( personID );
        newAttnBean.setPersonName( personName );
        newAttnBean.setNonEmployeeFlag( isNonEmployee );
        newAttnBean.setGuestFlag( isGuest );
        newAttnBean.setAlternateFlag( isAlternateFor );
        newAttnBean.setComments("");
        newAttnBean.setAlternatePerson(alternatePerson);
        newAttnBean.setAcType( INSERT_ACTYPE );

        //check for the current attedent list exits or not.
        if ( currentAttendents == null ) {
            currentAttendents = new Vector();
            txtArComments.setEnabled( true );
        }
        currentAttendents.add( newAttnBean );
        txtArComments.requestFocus();
        txtArComments.setText( "" );

        isFormDataUpdated = true;
        int newInsertedRowIndex = tblAttendents.getRowCount() - 1;
		lastSelectedRow  = newInsertedRowIndex; // Added for bug fix committee details saving 
        tblAttendents.setRowSelectionInterval( newInsertedRowIndex,
                                               newInsertedRowIndex );
        tblAttendents.scrollRectToVisible( tblAttendents.getCellRect(
                                                newInsertedRowIndex ,
                                                ZERO_COUNT, true));
		return newAttnBean;
    }


    //supporting method for setting the hand icon renderer for table
    private void setHandIconProperties( JTable table ){
        try
        {
        TableColumn column = table.getColumnModel().getColumn(ZERO_COUNT);
        column.setMinWidth(HANDICON_COLUMN_SIZE);
        column.setMaxWidth(HANDICON_COLUMN_SIZE);
        column.setPreferredWidth(HANDICON_COLUMN_SIZE);
        column.setHeaderRenderer( new EmptyHeaderRenderer() );
        column.setResizable( false );
        column.setCellRenderer( new IconRenderer() );
        table.setRowHeight( 22 );
        
        tblAttendents.getTableHeader().setResizingAllowed(true);
        tblAttendents.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        
        column = tblAttendents.getColumnModel().getColumn(1);
        column.setMinWidth(220);
        
        column = tblAttendents.getColumnModel().getColumn(2);
        column.setMinWidth(100);
        
        column = tblAttendents.getColumnModel().getColumn(3);
        column.setMinWidth(100);
        
        column = tblAttendents.getColumnModel().getColumn(4);
        column.setMinWidth(222);

        JTableHeader header = table.getTableHeader();
        header.setReorderingAllowed(false);
        //header.setResizingAllowed(false);
        table.setOpaque(false);
        table.setShowVerticalLines(false);
        table.setShowHorizontalLines(false);
        }
        catch(Exception ex)
        {
        
        ex.printStackTrace() ;
        
        }
        
    }

    //supporting method to check the duplicate Name Appearence
    private boolean checkDuplicateAttendentName( String personID,
                                                            String personName ){
        boolean isDuplicate = false;
        //routine to iterate through the collection of attdents to check
        //respective person already exists. if so pop's up message saying
        // "personname"  already exists.
        if( currentAttendents != null ){

            AttendanceInfoBean currentAttnBean = null;
            String altPersonID = null;
            String perID = null;
            for( int indx = ZERO_COUNT;
                                    indx < currentAttendents.size(); indx++ ){
                 currentAttnBean = (AttendanceInfoBean)
                                                currentAttendents.get( indx );

                 perID = currentAttnBean.getPersonId();
                 altPersonID = currentAttnBean.getAlternateFor();
                 if( ( perID != null && perID.equalsIgnoreCase(personID)) ||
                     ( altPersonID!= null && altPersonID.equalsIgnoreCase(
                                                                  personID))){
                     isDuplicate = true;
                     break;
                 }
            }
        }
        return isDuplicate ;
    }


    //supporting method to remove member from the absentee list if person exists
    private void removeFromAbsenteesList( String personID,boolean alternate ){

        AbsenteesInfoBean absnData = null;
        //iterate through the absentee list and remove the person entry if it
        //exists
       // if( currentAbsentees != null && tblAbsentees.getRowCount() > 0 ) {
        if( currentAbsentees != null ) {
            // when a member is added to attendees immediately the absentees
            // list doesnt show up and when user is deleted from the attendee and then again try
            // to add a member it throws exception as the for loop was looping
            // for cuurentAbsentees.size times. -> needs fixing
           
            //fix is to remove all te items from the absentee table and refresh
            // it with the rows from currentAbsentees
            //prps added this line
              
                      
            for( int indx = ZERO_COUNT, loopIndx = ZERO_COUNT ; loopIndx < currentAbsentees.size(); indx++, loopIndx++ ){
                absnData = ( AbsenteesInfoBean ) currentAbsentees.get( indx );
                if( absnData.getPersonId().equalsIgnoreCase( personID ) ){
                    // prps commented this
                    //((DefaultTableModel)tblAbsentees.getModel()).removeRow( indx );
                  currentAbsentees.remove( indx );
                  indx-- ;// decrement indx when a row is deleted
                  break;
                }
            }
            
           //prps start
            
            int rowCount = ((DefaultTableModel)tblAbsentees.getModel()).getRowCount() ;
                 
            for( int indx = ZERO_COUNT ; indx < rowCount; indx++)
            {
                // delete all rows 
                ((DefaultTableModel)tblAbsentees.getModel()).removeRow(ZERO_COUNT) ;
            }     
              
            
            //Vector absenteeTableData = new Vector();
                
            for( int indx = ZERO_COUNT ; indx < currentAbsentees.size(); indx++)
            {
                absnData = ( AbsenteesInfoBean ) currentAbsentees.get( indx );
                Vector newAbsentData = new Vector();
                    newAbsentData.add( "" );
                    newAbsentData.add( absnData.getPersonName() );
                    // add row to absentees table
                    ((DefaultTableModel)tblAbsentees.getModel()).addRow(newAbsentData ) ;
            }
            
             
           //prps end 
           
        }
    }

    /**
     * This method is used to set the flag for the form data updates
     * @param isUpdated boolean value represent this Form Data Updated.
     */
    public void setFormDataUpdatedFlag( boolean isUpdated ){
        isFormDataUpdated = isUpdated;
    }
    
    
    /**
     * This method is used to get the flag for the form data updates
     * @return boolean True if Form Data updated else False.
     */
    public boolean isFormDataUpdatedFlag(){
        if( this.functionType == this.DISPLAY_MODE ){
            return false;
        }
        int selectedRow = tblAttendents.getSelectedRow();
        //String comment = null;
		// Added for bug fix committee details saving - start
		AttendanceInfoBean selectedAttnBean;
		if(selectedRow != -1) {
            selectedAttnBean = (AttendanceInfoBean) currentAttendents.get(selectedRow);
			
            if(txtArComments.getText() == null){
                    txtArComments.setText("");
            }
            if(selectedAttnBean.getComments() != null && !selectedAttnBean.getComments().equals(txtArComments.getText())) {
                selectedAttnBean.setComments(txtArComments.getText());
                isFormDataUpdated = true;
                if(selectedAttnBean.getAcType() == null) {
                    selectedAttnBean.setAcType(UPDATE_ACTYPE);
                }
            }
        }//end Jobin(30/11/2004)
		// commented for bug fix committee details saving - start
        //event catpured during table row selection to set the respective
        //comments
      /*  if( selectedRow >= ZERO_COUNT && selectedRow <=
                                            tblAttendents.getRowCount() ){
             selectedAttnBean = (AttendanceInfoBean)
                                           currentAttendents.get( selectedRow );
             String currentComments = txtArComments.getText();
             String beanComments = selectedAttnBean.getComments();
             beanComments = beanComments == null ? "" : beanComments;
             if( !currentComments.equals(beanComments ) ) {
                 selectedAttnBean.setComments( txtArComments.getText() );
                if( selectedAttnBean.getAcType() == null ){
                    selectedAttnBean.setAcType( UPDATE_ACTYPE );
                }
                 isFormDataUpdated = true;
             }
        }*/ //end comments Jobin
        
        return isFormDataUpdated;
    }   

    /**
     * Method to Get all the Attedent Entries. 
     * @return Vector contains AttedentInfoBean
     */
    public Vector getFormData(){

        if( this.functionType == this.DISPLAY_MODE ){
            return null ;
        }
        
        // add the absentees to attendents l;ist but with AC_TYPE as 'D'
        
        if (deletedAttendent != null)
        {    
            for (int i =0; i< deletedAttendent.size(); i++)
             {
                // update add them to the attendents list 
                currentAttendents.add(i,deletedAttendent.get(i)) ; 
             }    
             // clear the delete list
            deletedAttendent = null ;
        }
        return currentAttendents ;
    }
       
    
    /**
     * Method to set the Attedent Entries.
     * @param Vector contains AttedentInfoBean
     * @param attendentsData collection of AttedanceInfoBean
     * @param absenteesData collection of AbsenteeInfoBean
     */
    public void setFormData( String committeeID, Vector attendentsData,
                                                   Vector absenteesData ){

        if( this.functionType != this.DISPLAY_MODE ){
		// added for bug fix committee details saving - start	
		lastSelectedRow = 0; //- end
        this.committeeId = committeeID;
        this.currentAttendents = attendentsData;
        this.currentAbsentees = absenteesData;
        int selectedRow=0;
        //String comments=null;
        if(tblAttendents.getRowCount()>0){
            selectedRow=tblAttendents.getSelectedRow();
        }
        setAttedentsFormData();
        
        if(tblAttendents.getRowCount()>0)
        {
            tblAttendents.setRowSelectionInterval(0,0);
            
        }
     
       
        //if Absentee table has more then one row by default select first row
        if( tblAbsentees.getRowCount() > ZERO_COUNT ){
//            tblAbsentees.addRowSelectionInterval( ZERO_COUNT, ZERO_COUNT );            
        }
 
        /*if Attendent table has more then one row by default select first row
         * set the respective comment Text Area
         */
        if( tblAttendents.getRowCount() > ZERO_COUNT ){
            txtArComments.setEnabled( true );
            setAbesenteeFormData();
        }
        
        
        TableColumn clmAttendent = tblAttendents.getColumn( "Guest Flag" );
        clmAttendent.setMinWidth( BOOLEAN_COLUMN_SIZE );
        clmAttendent = tblAttendents.getColumn( "Alternate" );
        clmAttendent.setMinWidth( BOOLEAN_COLUMN_SIZE );
        
        
        
        setHandIconProperties( tblAttendents );
        setHandIconProperties( tblAbsentees );
        
        
        isFormDataUpdated = false;
        }
    }

    //supporting method to create coeus Daialog Window.
//    private CoeusDlgWindow createCoeusDlgWindow( String name, boolean isModal ){
//
//       return new CoeusDlgWindow( mdiForm, name , isModal ){
//                protected JRootPane createRootPane() {
//                ActionListener actionListener = new ActionListener() {
//                    public void actionPerformed(ActionEvent actionEvent) {
//                        dlgActiveMembers.dispose();
//                    }
//                };
//                JRootPane rootPane = new JRootPane();
//                KeyStroke stroke = KeyStroke.getKeyStroke(
//                                                        KeyEvent.VK_ESCAPE,0);
//                rootPane.registerKeyboardAction(actionListener, stroke,
//                JComponent.WHEN_IN_FOCUSED_WINDOW);
//                return rootPane;
//                }};
//    }
    
    /** Getter for property scheduleId.
     * @return Value of property scheduleId.
     */
    public java.lang.String getScheduleId() {
        return scheduleId;
    }
    
    /** Setter for property scheduleId.
     * @param scheduleId New value of property scheduleId.
     */
    public void setScheduleId(java.lang.String scheduleId) {
        this.scheduleId = scheduleId;
    }
    
}