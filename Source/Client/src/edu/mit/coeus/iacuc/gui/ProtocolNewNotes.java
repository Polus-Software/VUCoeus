/*
 * ProtocolNewNotes.java
 *
 * Created on June 16, 2003, 4:03 PM
 */

package edu.mit.coeus.iacuc.gui;

import edu.mit.coeus.iacuc.bean.ProtocolNotepadBean;
import edu.mit.coeus.gui.CoeusFontFactory;
import edu.mit.coeus.utils.CoeusDateFormat;
import edu.mit.coeus.utils.CoeusOptionPane;
import edu.mit.coeus.gui.CoeusAppletMDIForm;
import edu.mit.coeus.gui.CoeusMessageResources;
import edu.mit.coeus.gui.CoeusDlgWindow;
import edu.mit.coeus.utils.LimitedPlainDocument;
import edu.mit.coeus.utils.LockObservable;
import edu.mit.coeus.utils.UserUtils;

import java.awt.event.*;
import java.awt.*;
import java.awt.Container.*;
import javax.swing.JInternalFrame.*;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.AbstractAction;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeEvent;
import java.util.Observer;

/** This Form is used to Add New Notes records for a Proposal.
 * @author senthil
 */
public class ProtocolNewNotes extends javax.swing.JComponent implements ActionListener{

    /* This is used to hold MDI form reference */
    private CoeusAppletMDIForm mdiReference;
    private CoeusDlgWindow dlgNotes;
    private CoeusMessageResources messageResources;
            Component parentDialog;
    //holds the ProtocolNotepadBean sent from ProtocolNotepadForm
            ProtocolNotepadBean newProtoNotepadBean;
    //To indicate if the newProtocolNotepadBean has been changed
    private boolean notepadPropertyChanged = false;
    //To indicate if the newProtocolNotepadBean has to be saved to DB thru ProtocolNotepadForm
    private boolean saveRequired = false;
    private LockObservable observable = new LockObservable();
        
    /**
     * This variable is used to store the old Restricted Flag value while in 
     * setChangedData(), These values will be reset to the bean in the case 
     * of a NO or CANCEL option  in YES_NO_CANCEL Dialog box.
     */
    private boolean oldRestrictedFlag = false;
    private String oldComments = "" ;
    
    /** Creates new form ProtocolNewNotes */
    public ProtocolNewNotes() {
        initComponents();
        
    }
    
    /** Creates new form ProtocolNewNotes
     * @param mdiReference mdiReference for this form
     * @param newProtoNotepadBean The new ProtocolNotepadBean
     */
    public ProtocolNewNotes(CoeusAppletMDIForm mdiReference, ProtocolNotepadBean newProtoNotepadBean) {
        this.mdiReference = mdiReference;
        this.newProtoNotepadBean = newProtoNotepadBean;
        initComponents();
        messageResources = CoeusMessageResources.getInstance();
        setListenersForButtons();
        setFormData();
    }

    /** This method is used to set the listeners to the buttons OK and Cancel */
    private void setListenersForButtons(){
        
        //Setting font for labels
        lblBy.setFont(CoeusFontFactory.getLabelFont());
        lblOn.setFont(CoeusFontFactory.getLabelFont());
        chkRestricted.setFont(CoeusFontFactory.getLabelFont());
        txtArComments.setFont(CoeusFontFactory.getNormalFont());
        
        btnOk.setFont(CoeusFontFactory.getLabelFont());
        btnOk.setMnemonic('O');
        btnOk.setText("OK"); 
        btnOk.addActionListener(this);
        String notepadBeanAcType = newProtoNotepadBean.getAcType();
        if((notepadBeanAcType != null)&&(notepadBeanAcType.equalsIgnoreCase("I"))){
            txtArComments.setRequestFocusEnabled(true);
        } else {
            btnOk.setEnabled(false);
            txtArComments.setEditable(false);
            chkRestricted.setEnabled(false); 
            
         //Added by Amit 11/19/2003
            java.awt.Color bgListColor = (java.awt.Color)javax.swing.UIManager.getDefaults().get("Panel.background");       
            txtArComments.setBackground(bgListColor);            
        //end Amit   
            
        }

        btnCancel.setFont(CoeusFontFactory.getLabelFont());
        btnCancel.setMnemonic('C');
        btnCancel.setText("Cancel");
        btnCancel.addActionListener(this);
        
        //Add PropertyChangeListeners for the newProtoNotepadBean
        newProtoNotepadBean.addPropertyChangeListener(
            new PropertyChangeListener(){
                public void propertyChange(PropertyChangeEvent pce){
                    if ( pce.getNewValue() == null && pce.getOldValue() != null ) {
                        notepadPropertyChanged = true;
                    }
                    if( pce.getNewValue() != null && pce.getOldValue() == null ) {
                        notepadPropertyChanged = true;
                    }
                    if( pce.getNewValue()!=null && pce.getOldValue()!=null ) {
                        if (!(  pce.getNewValue().toString().trim().equalsIgnoreCase(pce.getOldValue().toString().trim())))  {
                            notepadPropertyChanged = true;
                        }
                    }
                }
            });
            
        if(!txtArComments.hasFocus()) {
            if(!txtArComments.isRequestFocusEnabled()) { txtArComments.setRequestFocusEnabled(true); }
            txtArComments.requestFocus();
        }
    }
    
    /** Action Performed Method
     * @param actionEvent Action Event Object
     */    
    public void actionPerformed(java.awt.event.ActionEvent actionEvent) {
        Object actionSource = actionEvent.getSource();
        try{
            if(actionSource.equals(btnOk)){
                setChangedData('O');
                dlgNotes.dispose();
                // Added by chandra to notify the change in data
                observable.setLockStatus(-1);
                observable.notifyObservers();
                //End chandra
            } else if (actionSource.equals(btnCancel)){
                validateData();
                // Added by chandra to notify the change in data
                observable.setLockStatus(-1);
                observable.notifyObservers();
                //End chandra
            } else {
                //System.out.println("actionSource is not Ok or Cancel");
            }
        }catch(Exception e){
            e.printStackTrace();
            CoeusOptionPane.showInfoDialog( e.getMessage() );
        }
    }
    /** Set Changed Data Method
     * This method sets data from the form to the newProtoNotepadBean
     * @throws Exception Throws Exception if Comments String is empty or null
     */
    public void setChangedData() throws Exception{
        setChangedData('Z');
    }
    /** Set Changed Data Method
     * This method sets data from the form to the newProtoNotepadBean
     * @param type indicates if OK button is pressed or Cancel button is pressed.
     * @throws Exception Throws exception if comments field is empty or null
     */
    public void setChangedData(char type) throws Exception{
        boolean isRestricted = chkRestricted.isSelected();
        String newComment = txtArComments.getText();
        if((newComment != null)&&(newComment.trim().length() > 0)){
            setSaveRequired(true);
            oldRestrictedFlag = newProtoNotepadBean.isRestrictedFlag();
            oldComments = newProtoNotepadBean.getComments();
            newProtoNotepadBean.setRestrictedFlag(isRestricted);
            newProtoNotepadBean.setComments(newComment);
        } else if(type=='O'){
            throw new Exception(messageResources.parseMessageKey(
            "protocol_NotepadFrm_exceptionCode.1000" ));
        }
    }
    
    /** This method uses the notes Vector for the protocol
     * protoVecNotes and displays the contents of the vector in the table
     *
     */
    private void setFormData(){
        if (newProtoNotepadBean != null){
            /*
             * UserID to UserName Enhancement - Start
             * Added UserUtils class to change userid to username
             */
//            lblLoggedInUser.setText(newProtoNotepadBean.getUpdateUser());
            lblLoggedInUser.setText(UserUtils.getDisplayName(newProtoNotepadBean.getUpdateUser()));
            // UserId to UserName Enhancement - End
            lblLoggedInUser.setFont(CoeusFontFactory.getNormalFont());
            try {
                lblDateTimeStamp.setText(CoeusDateFormat.format(
                newProtoNotepadBean.getUpdateTimestamp().toString()));
                lblDateTimeStamp.setFont(CoeusFontFactory.getNormalFont());
                chkRestricted.setSelected(newProtoNotepadBean.isRestrictedFlag());
                txtArComments.setText(newProtoNotepadBean.getComments());
                txtArComments.setCaretPosition(0); //Added by Vyjayanthi on 13/01/2004
            }catch(Exception e){
                CoeusOptionPane.showInfoDialog(e.getMessage());
            }
        }
    }
    
    /** This method is used by the ProtocolNotepadForm to Display
     * the Dialog which will contain the ProtocolNewNotes Form
     */
    public void showProtocolNewNote(){
        String title = "New Note for Protocol - ";
        if( newProtoNotepadBean.getAcType() == null ){
            title = "Note for Protocol - ";
        }
        dlgNotes = new CoeusDlgWindow(mdiReference,
        title + newProtoNotepadBean.getProtocolNumber(),true);
        
        dlgNotes.getContentPane().add(this);
        dlgNotes.setResizable(false);
        dlgNotes.pack();
        Dimension screenSize = Toolkit.getDefaultToolkit().
        getScreenSize();
        Dimension dlgSize = dlgNotes.getSize();
        dlgNotes.setLocation(screenSize.width/2 - (dlgSize.width/2),
        screenSize.height/2 - (dlgSize.height/2));
        dlgNotes.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
        
        dlgNotes.addWindowListener(new WindowAdapter(){
            public void windowActivated(WindowEvent e) {
                txtArComments.requestFocusInWindow();
            }
            public void windowClosing(WindowEvent we){
                //dlgNotes.dispose();
                try{
                    validateData();
                    
                }catch(Exception ex){
                    CoeusOptionPane.showInfoDialog(ex.getMessage());
                }
            }
        });
        dlgNotes.addEscapeKeyListener(
        new AbstractAction("escPressed"){
            public void actionPerformed(ActionEvent ae){
                try{
                    validateData();
                    
                }catch(Exception ex){
                    CoeusOptionPane.showInfoDialog(ex.getMessage());
                }
            }
        });
        dlgNotes.show();
    }
    
    private void validateData()throws Exception{
        setChangedData();
        if ( notepadPropertyChanged ) {
            String msg = messageResources.parseMessageKey(
            "saveConfirmCode.1002");
            
            int confirm = CoeusOptionPane.showQuestionDialog(msg,
            CoeusOptionPane.OPTION_YES_NO_CANCEL,
            CoeusOptionPane.DEFAULT_YES);
            switch(confirm){
                case ( JOptionPane.NO_OPTION ) :
                    newProtoNotepadBean.setRestrictedFlag(oldRestrictedFlag);
                    oldRestrictedFlag = false;
                    newProtoNotepadBean.setComments(oldComments);
                    oldComments = "";
                    setSaveRequired( false );
                    dlgNotes.dispose();
                    break;
                case ( JOptionPane.YES_OPTION ) :
                    setSaveRequired( true );
                    dlgNotes.dispose();
                    break;
                case ( JOptionPane.CANCEL_OPTION ) :
                    newProtoNotepadBean.setRestrictedFlag(oldRestrictedFlag);
                    oldRestrictedFlag = false;
                    newProtoNotepadBean.setComments(oldComments);
                    oldComments = "";
                    setSaveRequired( false );
                    dlgNotes.setVisible( true );
                    break;
            }
            
        }else{
            dlgNotes.dispose();
        }
    }
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    private void initComponents() {//GEN-BEGIN:initComponents
        java.awt.GridBagConstraints gridBagConstraints;

        pnlUserDetails = new javax.swing.JPanel();
        lblBy = new javax.swing.JLabel();
        lblLoggedInUser = new javax.swing.JLabel();
        lblOn = new javax.swing.JLabel();
        lblDateTimeStamp = new javax.swing.JLabel();
        chkRestricted = new javax.swing.JCheckBox();
        pnlTextArea = new javax.swing.JPanel();
        scrPnArComments = new javax.swing.JScrollPane();
        txtArComments = new javax.swing.JTextArea();
        pnlButtons = new javax.swing.JPanel();
        btnOk = new javax.swing.JButton();
        btnCancel = new javax.swing.JButton();

        setLayout(new java.awt.GridBagLayout());

        setMaximumSize(new java.awt.Dimension(550, 275));
        setMinimumSize(new java.awt.Dimension(550, 275));
        setPreferredSize(new java.awt.Dimension(550, 275));
        pnlUserDetails.setLayout(new java.awt.GridBagLayout());

        pnlUserDetails.setMaximumSize(new java.awt.Dimension(550, 20));
        pnlUserDetails.setMinimumSize(new java.awt.Dimension(550, 20));
        pnlUserDetails.setPreferredSize(new java.awt.Dimension(550, 20));
        lblBy.setText(" By : ");
        lblBy.setMaximumSize(new java.awt.Dimension(35, 20));
        lblBy.setMinimumSize(new java.awt.Dimension(35, 20));
        lblBy.setPreferredSize(new java.awt.Dimension(35, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        pnlUserDetails.add(lblBy, gridBagConstraints);

        lblLoggedInUser.setText("User Name");
        lblLoggedInUser.setMaximumSize(new java.awt.Dimension(200, 20));
        lblLoggedInUser.setMinimumSize(new java.awt.Dimension(150, 20));
        lblLoggedInUser.setPreferredSize(new java.awt.Dimension(200, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        pnlUserDetails.add(lblLoggedInUser, gridBagConstraints);

        lblOn.setText(" On : ");
        lblOn.setMaximumSize(new java.awt.Dimension(35, 20));
        lblOn.setMinimumSize(new java.awt.Dimension(35, 20));
        lblOn.setPreferredSize(new java.awt.Dimension(35, 20));
        pnlUserDetails.add(lblOn, new java.awt.GridBagConstraints());

        lblDateTimeStamp.setText("Date Time Stamp");
        lblDateTimeStamp.setMaximumSize(new java.awt.Dimension(150, 20));
        lblDateTimeStamp.setMinimumSize(new java.awt.Dimension(150, 20));
        lblDateTimeStamp.setPreferredSize(new java.awt.Dimension(150, 20));
        pnlUserDetails.add(lblDateTimeStamp, new java.awt.GridBagConstraints());

        chkRestricted.setText("Restricted");
        chkRestricted.setMaximumSize(new java.awt.Dimension(90, 20));
        chkRestricted.setMinimumSize(new java.awt.Dimension(90, 20));
        chkRestricted.setPreferredSize(new java.awt.Dimension(90, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        pnlUserDetails.add(chkRestricted, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHEAST;
        add(pnlUserDetails, gridBagConstraints);

        pnlTextArea.setLayout(new java.awt.GridBagLayout());

        pnlTextArea.setBorder(new javax.swing.border.TitledBorder(null, "Note:", javax.swing.border.TitledBorder.LEFT, javax.swing.border.TitledBorder.TOP, CoeusFontFactory.getLabelFont()));
        pnlTextArea.setMaximumSize(new java.awt.Dimension(445, 250));
        pnlTextArea.setMinimumSize(new java.awt.Dimension(445, 250));
        pnlTextArea.setPreferredSize(new java.awt.Dimension(445, 250));
        scrPnArComments.setMinimumSize(new java.awt.Dimension(400, 200));
        scrPnArComments.setPreferredSize(new java.awt.Dimension(430, 220));
        LimitedPlainDocument plainDocument = new LimitedPlainDocument(3878);
        txtArComments.setDocument(plainDocument);
        txtArComments.setLineWrap(true);
        txtArComments.setWrapStyleWord(true);
        scrPnArComments.setViewportView(txtArComments);

        pnlTextArea.add(scrPnArComments, new java.awt.GridBagConstraints());

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.insets = new java.awt.Insets(0, 5, 0, 5);
        add(pnlTextArea, gridBagConstraints);

        pnlButtons.setLayout(new java.awt.GridBagLayout());

        pnlButtons.setMaximumSize(new java.awt.Dimension(80, 100));
        pnlButtons.setMinimumSize(new java.awt.Dimension(80, 100));
        pnlButtons.setPreferredSize(new java.awt.Dimension(80, 100));
        btnOk.setText("OK");
        btnOk.setMaximumSize(new java.awt.Dimension(75, 26));
        btnOk.setMinimumSize(new java.awt.Dimension(75, 26));
        btnOk.setPreferredSize(new java.awt.Dimension(75, 26));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 7, 5);
        pnlButtons.add(btnOk, gridBagConstraints);

        btnCancel.setText("Cancel");
        btnCancel.setMaximumSize(new java.awt.Dimension(75, 26));
        btnCancel.setMinimumSize(new java.awt.Dimension(75, 26));
        btnCancel.setPreferredSize(new java.awt.Dimension(75, 26));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.insets = new java.awt.Insets(5, 0, 0, 5);
        pnlButtons.add(btnCancel, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHEAST;
        add(pnlButtons, gridBagConstraints);

    }//GEN-END:initComponents

    /** Getter for property parentDialog.
     * @return Value of property parentDialog.
     */
    public Component getParentDialog() {
        return parentDialog;
    }    
    
    /** Setter for property parentDialog.
     * @param parentDialog New value of property parentDialog.
     */
    public void setParentDialog(Component parentDialog) {
        this.parentDialog = parentDialog;
    }    
    
    /** Getter for property newProtoNotepadBean.
     * @return Value of property newProtoNotepadBean.
     */
    public ProtocolNotepadBean getNewProtoNotepadBean() {
        return newProtoNotepadBean;
    }
    
    /** Setter for property newProtoNotepadBean.
     * @param newProtoNotepadBean New value of property newProtoNotepadBean.
     */
    public void setNewProtoNotepadBean(ProtocolNotepadBean newProtoNotepadBean) {
        this.newProtoNotepadBean = newProtoNotepadBean;
    }
    
    /** Getter for property saveRequired.
     * @return Value of property saveRequired.
     */
    public boolean isSaveRequired() {
        return saveRequired;
    }
    
    /** Setter for property saveRequired.
     * @param saveRequired New value of property saveRequired.
     */
    public void setSaveRequired(boolean saveRequired) {
        this.saveRequired = saveRequired;
    }
    
    public void registerLockObservable(Observer observer) {
         observable.addObserver(observer); 
     }
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextArea txtArComments;
    private javax.swing.JLabel lblLoggedInUser;
    private javax.swing.JLabel lblBy;
    private javax.swing.JPanel pnlButtons;
    private javax.swing.JScrollPane scrPnArComments;
    private javax.swing.JPanel pnlUserDetails;
    private javax.swing.JPanel pnlTextArea;
    private javax.swing.JButton btnOk;
    private javax.swing.JLabel lblDateTimeStamp;
    private javax.swing.JButton btnCancel;
    private javax.swing.JLabel lblOn;
    private javax.swing.JCheckBox chkRestricted;
    // End of variables declaration//GEN-END:variables
    
}
