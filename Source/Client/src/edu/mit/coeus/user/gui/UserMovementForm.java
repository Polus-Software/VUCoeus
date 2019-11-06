/*
 * UserMovementForm.java
 *
 * Created on June 26, 2003, 11:40 PM
 */

package edu.mit.coeus.user.gui;


import javax.swing.JComponent;
import javax.swing.JOptionPane;
import javax.swing.JDialog;
import javax.swing.ImageIcon;
import java.awt.*;
import java.awt.event.*;
import java.lang.Integer;
import java.util.Vector;

import edu.mit.coeus.gui.CoeusFontFactory;
import edu.mit.coeus.bean.UserInfoBean;
import edu.mit.coeus.utils.CoeusOptionPane;
import edu.mit.coeus.utils.AppletServletCommunicator;
import edu.mit.coeus.utils.CoeusGuiConstants;
import edu.mit.coeus.gui.CoeusAppletMDIForm;
import edu.mit.coeus.gui.CoeusMessageResources;
import edu.mit.coeus.gui.CoeusDlgWindow;
import edu.mit.coeus.search.gui.*;
import edu.mit.coeus.unit.gui.UnitDetailForm;
import edu.mit.coeus.unit.bean.UnitDetailFormBean;
import edu.mit.coeus.brokers.RequesterBean;
import edu.mit.coeus.brokers.ResponderBean;
import edu.mit.coeus.user.bean.UserMovementController;
import edu.mit.coeus.utils.CoeusOptionPane;
import edu.mit.coeus.utils.ScreenFocusTraversalPolicy;

/**
 *
 * @author  senthil
 */
public class UserMovementForm extends javax.swing.JComponent
implements ActionListener {
    
    //UserInfoBean instance that is being currently moved to a different unit.
    UserInfoBean userInfoBean;
    
    /* This is used to hold MDI form reference */
    private CoeusAppletMDIForm mdiReference;
    private CoeusDlgWindow dlgMoveUser;
    private CoeusMessageResources messageResources;
    Component parentDialog;
    
    //flag to show if the UserInfoBean needs to be saved
    private boolean saveRequired = false;
    //holds the New Unit Id entered on the form while validation is in progress.
    String newUnitId;
    
    private  final  ImageIcon searchIcon =
    new ImageIcon(getClass().getClassLoader().getResource(
    CoeusGuiConstants.SEARCH_ICON));
    
    private Frame owner;
    private boolean modal;
    private boolean userMoved;
    private boolean addUser, maintainUser;
    private UserMovementController userMovementController;
    
    private int WIDTH = 775;
    private int HEIGHT = 200;
    
    private static final String EMPTY = "";
    private static final String INVALID_UNIT_NUMBER = "Invalid Unit Number. Please enter a valid Unit Number";
    private static final String SAVE_CHANGES = "Do you want to save the changes?";
    private static final String COULD_NOT_SAVE = "Could Not Save";
    private static final String CANNOT_MOVE_USER = "You can not move user to the unit ";
    
    /** Creates new form UserMovementForm */
    public UserMovementForm(Frame owner, boolean modal) {
        this.owner = owner;
        this.modal = modal;
        mdiReference = CoeusGuiConstants.getMDIForm();
        userMovementController = new UserMovementController();
        initComponents();
        btnSearchUnit.setPreferredSize(new java.awt.Dimension(25, 20));
        postInitComponents();
    }
    
    /** Creates new form UserMovementForm */
    public UserMovementForm(String userId) {
        initComponents();
        btnSearchUnit.setPreferredSize(new java.awt.Dimension(25, 20));
    }
    
    /** Creates new form UserMovementForm */
    public UserMovementForm(UserInfoBean userInfoBean) {
        this.userInfoBean = userInfoBean;
        initComponents();
        postInitComponents();
        btnSearchUnit.setPreferredSize(new java.awt.Dimension(25, 20));
        setFormData();
        //showUserMovementForm();
    }
    
    public void display(UserInfoBean userInfoBean) {
        this.userInfoBean = userInfoBean;
        userMoved = false;
        setFormData();
        dlgMoveUser.setTitle("Move "
        +userInfoBean.getUserId()+" from unit "+userInfoBean.getUnitNumber()+" to another");
        //showUserMovementForm();
        txtNewUnit.setText(EMPTY);
        lblNewUnitName.setText(EMPTY);
        dlgMoveUser.setLocation(CoeusDlgWindow.CENTER);
        dlgMoveUser.setVisible(true);
    }
    
    
    /** This method is called from within the constructor to
     * initialize the form.
     * This method initializes the variables which cannot be intialized
     * in the initComponents method.
     */
    private void postInitComponents() {
        
        chkNonMitPerson.setEnabled(false);
        
        messageResources = CoeusMessageResources.getInstance();
        java.awt.Component[] components = {txtCurrentUnitId,btnSearchUnit,btnOk, btnCancel,txtCurrentUnitId};        
        ScreenFocusTraversalPolicy traversePolicy = new ScreenFocusTraversalPolicy( components );
        setFocusTraversalPolicy(traversePolicy);
        setFocusCycleRoot(true);    
        btnSearchUnit.setIcon(searchIcon);
        btnSearchUnit.addActionListener(this);
        
        btnOk.setMnemonic('O');
        btnOk.setText("OK");
        btnOk.addActionListener(this);
        
        btnCancel.setMnemonic('C');
        btnCancel.setText("Cancel");
        btnCancel.addActionListener(this);
        
        txtCurrentUnitId.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent me) {
                if (me.getClickCount() == 2) {
                    try {
                        UnitDetailForm unitDetailForm =
                        new UnitDetailForm(txtCurrentUnitId.getText(),'G');
                        unitDetailForm.showUnitForm(mdiReference);
                    } catch (Exception e) {
                        e.printStackTrace();
                        CoeusOptionPane.showInfoDialog( e.getMessage() );
                    }
                }
            }
        });
        txtNewUnit.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent me) {
                if (me.getClickCount() == 2) {
                    try {
                        newUnitId = txtNewUnit.getText();
                        if ((newUnitId == null) || (newUnitId.trim().length() == 0)){
                            //do nothing
                            displaySearch();
                        } else {
                            if (validateUnitId()){
                                UnitDetailForm unitDetailForm =
                                new UnitDetailForm(txtNewUnit.getText(),'G');
                                unitDetailForm.showUnitForm(mdiReference);
                            } else {
                                //not valid Unit id send message and bring focus to new unit id text box
                                CoeusOptionPane.showInfoDialog(INVALID_UNIT_NUMBER);
                                return ;
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        CoeusOptionPane.showInfoDialog( e.getMessage() );
                    }
                }
            }
        });
        txtNewUnit.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                //do nothing
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                if (!evt.isTemporary()) {
                    newUnitId = txtNewUnit.getText();
                    if ((newUnitId == null) || (newUnitId.trim().length() == 0)){
                        //do nothing
                        lblNewUnitName.setText(EMPTY);
                    } else {
                        if (validateUnitId()){
                            
                        } else {
                            //not valid Unit id send message and bring focus to new unit id text box
                            lblNewUnitName.setText(EMPTY);
                        }
                    }
                }
            }
        });
        
        //Setting up Dialog Window
        dlgMoveUser = new CoeusDlgWindow(owner,modal);
        
        dlgMoveUser.getContentPane().add(this);
        dlgMoveUser.setSize(WIDTH, HEIGHT);
        dlgMoveUser.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
        dlgMoveUser.setResizable(false);
        
        
        dlgMoveUser.addWindowListener(new WindowAdapter(){
            public void windowActivated(WindowEvent e) {
                txtNewUnit.requestFocusInWindow();
            }
            public void windowClosing(WindowEvent we){
                try{
                    validateData();
                }catch(Exception ex){
                    CoeusOptionPane.showInfoDialog(ex.getMessage());
                }
            }
        });
        dlgMoveUser.addKeyListener(new KeyAdapter(){
            public void keyReleased(KeyEvent e){
                if(e.getKeyCode() == KeyEvent.VK_ESCAPE){
                    if(!CoeusOptionPane.isPropagating()){
                        try{
                            validateData();
                        }catch(Exception ex){
                            CoeusOptionPane.showInfoDialog(ex.getMessage());
                        }
                    }else{
                        CoeusOptionPane.setPropagating(
                        false);
                    }
                }
            }
        });
        
    }
    
    private void setFormData() {
        
        txtUserId.setText(userInfoBean.getUserId());
        
        switch (userInfoBean.getStatus()) {
            case ('I'):
                txtStatus.setText("Inactive");
                break;
            case ('A'):
                txtStatus.setText("Active");
                break;
            default:
                txtStatus.setText("");
                System.out.println("Status flag is not I or A"+userInfoBean.getStatus());
                break;
        }
        chkNonMitPerson.setSelected(userInfoBean.isNonEmployee());
        txtUserName.setText(userInfoBean.getUserName());
        txtCurrentUnitId.setText(userInfoBean.getUnitNumber());
        txtCurrentUnitName.setText(userInfoBean.getUnitName());
        
    }
    
    /*
    private void showUserMovementForm() {
        dlgMoveUser = new CoeusDlgWindow(owner,"Move "
            +userInfoBean.getUserId()+" from unit "+userInfoBean.getUnitNumber()+" to another",modal);
     
        dlgMoveUser.getContentPane().add(this);
        dlgMoveUser.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
        dlgMoveUser.setResizable(false);
     
        dlgMoveUser.pack();
     
        Dimension screenSize = Toolkit.getDefaultToolkit().
        getScreenSize();
        Dimension dlgSize = dlgMoveUser.getSize();
        dlgMoveUser.setLocation(screenSize.width/2 - (dlgSize.width/2),
        screenSize.height/2 - (dlgSize.height/2));
     
        dlgMoveUser.addWindowListener(new WindowAdapter(){
            public void windowActivated(WindowEvent e) {
                txtNewUnit.requestFocusInWindow();
            }
            public void windowClosing(WindowEvent we){
                try{
                    validateData();
                }catch(Exception ex){
                    CoeusOptionPane.showInfoDialog(ex.getMessage());
                }
            }
        });
        dlgMoveUser.addKeyListener(new KeyAdapter(){
            public void keyReleased(KeyEvent e){
                if(e.getKeyCode() == KeyEvent.VK_ESCAPE){
                    if(!CoeusOptionPane.isPropagating()){
                        try{
                            validateData();
                        }catch(Exception ex){
                            CoeusOptionPane.showInfoDialog(ex.getMessage());
                        }
                    }else{
                        CoeusOptionPane.setPropagating(
                        false);
                    }
                }
            }
        });
        dlgMoveUser.show();
        dlgMoveUser.setVisible(true);
    }*/
    
    private void validateData() throws Exception{
        newUnitId = txtNewUnit.getText();
        if ((newUnitId == null) || (newUnitId.trim().length() == 0)){
            dlgMoveUser.dispose();
        } else {
            if (validateUnitId()){
                yesNoCancelOptionPane();
            } else {
                //not valid Unit id send message and bring focus to new unit id text box
                throw new Exception(messageResources.parseMessageKey(
                "userMovement_exceptionCode.1000" ));
            }
        }
    }
    
    private boolean validateUnitId(){
        boolean isValid = false;
        //Query Database to find out if this UnitId is present in the database
        UnitDetailFormBean unitDetailRes = new UnitDetailFormBean();
        String connectTo = CoeusGuiConstants.CONNECTION_URL + "/coeusFunctionsServlet";
        //String connectTo = "http://localhost:8080/CoeusApplet" + "/coeusFunctionsServlet";
        RequesterBean request = new RequesterBean();
        request.setDataObject("GET_UNITINFO");
        request.setId(newUnitId.trim());
        AppletServletCommunicator comm = new AppletServletCommunicator(connectTo, request);
        comm.send();
        ResponderBean response = comm.getResponse();
        if (response == null) {
            isValid = false;
        }else if (response.getDataObject() == null){
            isValid = false;
        }else {
            unitDetailRes = (UnitDetailFormBean) response.getDataObject();
            lblNewUnitName.setText(unitDetailRes.getUnitName());
            isValid = true;
        }
        return isValid;
    }
    
    private void yesNoCancelOptionPane(){
        String msg = messageResources.parseMessageKey(
        "saveConfirmCode.1002");
        
        int confirm = CoeusOptionPane.showQuestionDialog(msg,
        CoeusOptionPane.OPTION_YES_NO_CANCEL,
        CoeusOptionPane.DEFAULT_YES);
        switch(confirm){
            case ( JOptionPane.NO_OPTION ) :
                setSaveRequired( false );
                dlgMoveUser.dispose();
                break;
            case ( JOptionPane.YES_OPTION ) :
                userInfoBean.setUnitNumber(newUnitId);
                userInfoBean.setUnitName(lblNewUnitName.getText());
                setSaveRequired( true );
                dlgMoveUser.dispose();
                break;
            case ( JOptionPane.CANCEL_OPTION ) :
                setSaveRequired( false );
                dlgMoveUser.setVisible( true );
                break;
        }
    }
    
    /** Action Performed Method
     * @param actionEvent Action Event Object
     */
    public void actionPerformed(java.awt.event.ActionEvent actionEvent) {
        Object actionSource = actionEvent.getSource();
        try{
            if(actionSource.equals(btnOk)){
                save();
            } else if (actionSource.equals(btnCancel)){
                //validateData();
                if(! txtNewUnit.getText().trim().equals(EMPTY)) {
                    int selection = CoeusOptionPane.showQuestionDialog(SAVE_CHANGES, CoeusOptionPane.OPTION_YES_NO_CANCEL, CoeusOptionPane.DEFAULT_YES);
                    if(selection == CoeusOptionPane.SELECTION_YES) {
                        save();
                    }
                    else if(selection == CoeusOptionPane.SELECTION_NO) {
                        dlgMoveUser.setVisible(false);
                    }
                    else if(selection == CoeusOptionPane.SELECTION_CANCEL) {
                        return ;
                    }
                }
                else{
                    dlgMoveUser.setVisible(false);
                }
            } else if (actionSource.equals(btnSearchUnit)){
                displaySearch();
            } else {
                System.out.println("actionSource is not Ok or Cancel");
            }
        }catch(Exception e){
            e.printStackTrace();
            CoeusOptionPane.showInfoDialog( e.getMessage() );
        }
    }
    
    private boolean checkUserRights() throws Exception{
        boolean hasRights = false;
        String strRights = "";
        //String connectTo = "http://localhost:8080/CoeusApplet" + "/coeusFunctionsServlet";
        String connectTo = "/coeusFunctionsServlet";
        RequesterBean request = new RequesterBean();
        request.setDataObject("GET_USER_RIGHTS");
        Vector userUnitID = new Vector(2);
        userUnitID.add(mdiReference.getUserName());
        userUnitID.add(newUnitId);
        request.setDataObjects(userUnitID);
        AppletServletCommunicator comm = new AppletServletCommunicator(connectTo, request);
        comm.send();
        ResponderBean response = comm.getResponse();
        if (response == null) {
            hasRights = false;
        } else {
            strRights = (String) response.getDataObject();
        }
        if (strRights == null){
            hasRights = false;
        }else if (strRights.equalsIgnoreCase("HASRIGHTS")){
            hasRights = true;
        }else if (strRights.equalsIgnoreCase("NORIGHTS")){
            hasRights = false;
            //SEND A MESSAGE
            throw new Exception(messageResources.parseMessageKey(
            "You can not move user to the unit " + newUnitId + "."));
        } else {
            System.out.println("response.getDataObject() = "+strRights);
        }
        return hasRights ;
    }
    
    private void checkUserUnitMapInfo() throws Exception{
        //String connectTo = "http://localhost:8080/CoeusApplet" + "/coeusFunctionsServlet";
        String connectTo = "/coeusFunctionsServlet";
        RequesterBean request = new RequesterBean();
        request.setDataObject("GET_USERUNIT_MAPINFO");
        //request.setId(newUnitId);
        Vector userUnitID = new Vector(2);
        userUnitID.add(userInfoBean.getUserId());
        userUnitID.add(userInfoBean.getUnitNumber());
        request.setDataObjects(userUnitID);
        AppletServletCommunicator comm = new AppletServletCommunicator(connectTo, request);
        comm.send();
        ResponderBean response = comm.getResponse();
        if (response == null) {
            //do nothing
        }else if (response.getDataObject() == null){
            //do nothing
        }else {
            Integer count = (Integer) response.getDataObject();
            if (count.intValue() > 0) {
                //Send an alert message to the user
                throw new Exception(messageResources.parseMessageKey(
                "The user "+userInfoBean.getUserId()+
                " is currently included as an approver in at least one map in unit "
                +userInfoBean.getUnitNumber()+
                " You may want to update your maps."));
            }
        }
    }
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    private void initComponents() {//GEN-BEGIN:initComponents
        java.awt.GridBagConstraints gridBagConstraints;

        pnlDetail = new javax.swing.JPanel();
        lblUserId = new javax.swing.JLabel();
        txtUserId = new javax.swing.JTextField();
        lblStatus = new javax.swing.JLabel();
        txtStatus = new javax.swing.JTextField();
        chkNonMitPerson = new javax.swing.JCheckBox();
        lblUserName = new javax.swing.JLabel();
        txtUserName = new javax.swing.JTextField();
        lblCurrentUnit = new javax.swing.JLabel();
        txtCurrentUnitId = new javax.swing.JTextField();
        txtCurrentUnitName = new javax.swing.JTextField();
        lblNewUnit = new javax.swing.JLabel();
        txtNewUnit = new javax.swing.JTextField();
        btnSearchUnit = new javax.swing.JButton();
        lblNewUnitName = new javax.swing.JLabel();
        pnlButton = new javax.swing.JPanel();
        btnOk = new javax.swing.JButton();
        btnCancel = new javax.swing.JButton();

        setLayout(new java.awt.GridBagLayout());

        setMaximumSize(new java.awt.Dimension(750, 175));
        setMinimumSize(new java.awt.Dimension(750, 175));
        setPreferredSize(new java.awt.Dimension(750, 175));
        pnlDetail.setLayout(new java.awt.GridBagLayout());

        pnlDetail.setBorder(new javax.swing.border.BevelBorder(javax.swing.border.BevelBorder.LOWERED));
        pnlDetail.setMaximumSize(new java.awt.Dimension(650, 175));
        pnlDetail.setMinimumSize(new java.awt.Dimension(650, 175));
        pnlDetail.setPreferredSize(new java.awt.Dimension(650, 175));
        lblUserId.setFont(CoeusFontFactory.getLabelFont());
        lblUserId.setText("User Id:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 10);
        pnlDetail.add(lblUserId, gridBagConstraints);

        txtUserId.setEditable(false);
        txtUserId.setFont(CoeusFontFactory.getNormalFont());
        txtUserId.setMaximumSize(new java.awt.Dimension(100, 20));
        txtUserId.setMinimumSize(new java.awt.Dimension(100, 20));
        txtUserId.setPreferredSize(new java.awt.Dimension(100, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 5, 0);
        pnlDetail.add(txtUserId, gridBagConstraints);

        lblStatus.setFont(CoeusFontFactory.getLabelFont());
        lblStatus.setText("Status:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 10);
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        pnlDetail.add(lblStatus, gridBagConstraints);

        txtStatus.setEditable(false);
        txtStatus.setFont(CoeusFontFactory.getNormalFont());
        txtStatus.setMaximumSize(new java.awt.Dimension(150, 20));
        txtStatus.setMinimumSize(new java.awt.Dimension(150, 20));
        txtStatus.setPreferredSize(new java.awt.Dimension(100, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 5);
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        pnlDetail.add(txtStatus, gridBagConstraints);

        chkNonMitPerson.setFont(CoeusFontFactory.getLabelFont());
        chkNonMitPerson.setText("Non Mit Person:");
        chkNonMitPerson.setHorizontalTextPosition(javax.swing.SwingConstants.LEADING);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 5;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(0, 5, 0, 10);
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        pnlDetail.add(chkNonMitPerson, gridBagConstraints);

        lblUserName.setFont(CoeusFontFactory.getLabelFont());
        lblUserName.setText("User Name:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 10);
        pnlDetail.add(lblUserName, gridBagConstraints);

        txtUserName.setEditable(false);
        txtUserName.setFont(CoeusFontFactory.getNormalFont());
        txtUserName.setMaximumSize(new java.awt.Dimension(100, 20));
        txtUserName.setMinimumSize(new java.awt.Dimension(100, 20));
        txtUserName.setPreferredSize(new java.awt.Dimension(450, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 5;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 5, 0);
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        pnlDetail.add(txtUserName, gridBagConstraints);

        lblCurrentUnit.setFont(CoeusFontFactory.getLabelFont());
        lblCurrentUnit.setText("Current Unit:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 10, 0, 10);
        pnlDetail.add(lblCurrentUnit, gridBagConstraints);

        txtCurrentUnitId.setEditable(false);
        txtCurrentUnitId.setFont(CoeusFontFactory.getNormalFont());
        txtCurrentUnitId.setMaximumSize(new java.awt.Dimension(100, 20));
        txtCurrentUnitId.setMinimumSize(new java.awt.Dimension(100, 20));
        txtCurrentUnitId.setPreferredSize(new java.awt.Dimension(100, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 5, 0);
        pnlDetail.add(txtCurrentUnitId, gridBagConstraints);

        txtCurrentUnitName.setEditable(false);
        txtCurrentUnitName.setFont(CoeusFontFactory.getNormalFont());
        txtCurrentUnitName.setForeground(new java.awt.Color(0, 0, 0));
        txtCurrentUnitName.setMaximumSize(new java.awt.Dimension(350, 20));
        txtCurrentUnitName.setMinimumSize(new java.awt.Dimension(100, 20));
        txtCurrentUnitName.setPreferredSize(new java.awt.Dimension(345, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridwidth = 4;
        gridBagConstraints.insets = new java.awt.Insets(0, 5, 5, 0);
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        pnlDetail.add(txtCurrentUnitName, gridBagConstraints);

        lblNewUnit.setFont(CoeusFontFactory.getLabelFont());
        lblNewUnit.setText("New Unit:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 10);
        pnlDetail.add(lblNewUnit, gridBagConstraints);

        txtNewUnit.setFont(CoeusFontFactory.getNormalFont());
        txtNewUnit.setMaximumSize(new java.awt.Dimension(100, 20));
        txtNewUnit.setMinimumSize(new java.awt.Dimension(100, 20));
        txtNewUnit.setPreferredSize(new java.awt.Dimension(100, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        pnlDetail.add(txtNewUnit, gridBagConstraints);

        btnSearchUnit.setFont(CoeusFontFactory.getLabelFont());
        btnSearchUnit.setPreferredSize(new java.awt.Dimension(35, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.insets = new java.awt.Insets(0, 5, 0, 5);
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        pnlDetail.add(btnSearchUnit, gridBagConstraints);

        lblNewUnitName.setFont(CoeusFontFactory.getNormalFont());
        lblNewUnitName.setMinimumSize(new java.awt.Dimension(200, 20));
        lblNewUnitName.setPreferredSize(new java.awt.Dimension(305, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        pnlDetail.add(lblNewUnitName, gridBagConstraints);

        add(pnlDetail, new java.awt.GridBagConstraints());

        pnlButton.setLayout(new java.awt.GridBagLayout());

        pnlButton.setMaximumSize(new java.awt.Dimension(100, 100));
        pnlButton.setMinimumSize(new java.awt.Dimension(100, 100));
        pnlButton.setPreferredSize(new java.awt.Dimension(100, 100));
        btnOk.setFont(CoeusFontFactory.getLabelFont());
        btnOk.setText("Ok");
        btnOk.setMaximumSize(new java.awt.Dimension(90, 30));
        btnOk.setMinimumSize(new java.awt.Dimension(90, 30));
        btnOk.setPreferredSize(new java.awt.Dimension(90, 30));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTH;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 5, 0);
        pnlButton.add(btnOk, gridBagConstraints);

        btnCancel.setFont(CoeusFontFactory.getLabelFont());
        btnCancel.setText("Cancel");
        btnCancel.setMaximumSize(new java.awt.Dimension(90, 30));
        btnCancel.setMinimumSize(new java.awt.Dimension(90, 30));
        btnCancel.setPreferredSize(new java.awt.Dimension(90, 30));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTH;
        gridBagConstraints.insets = new java.awt.Insets(5, 0, 0, 0);
        pnlButton.add(btnCancel, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHEAST;
        add(pnlButton, gridBagConstraints);

    }//GEN-END:initComponents
    
    /** Getter for property userInfoBean.
     * @return Value of property userInfoBean.
     */
    public edu.mit.coeus.bean.UserInfoBean getUserInfoBean() {
        return userInfoBean;
    }
    
    /** Setter for property userInfoBean.
     * @param userInfoBean New value of property userInfoBean.
     */
    public void setUserInfoBean(edu.mit.coeus.bean.UserInfoBean userInfoBean) {
        this.userInfoBean = userInfoBean;
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
    
    public void displaySearch() {
        //Show search screen and get the selected unit to newUnitId text box.
        //open search window with two tabs
        try{
            CoeusSearch coeusSearch = new CoeusSearch(dlgMoveUser,
            "leadUnitSearch", CoeusSearch.TWO_TABS);
            coeusSearch.showSearchWindow();
            if(coeusSearch.getSelectedRow() == null) return ;
            txtNewUnit.setText(coeusSearch.getSelectedRow().get("UNIT_NUMBER").toString().trim());
            lblNewUnitName.setText(coeusSearch.getSelectedRow().get("UNIT_NAME").toString());
        }catch (Exception exception) {
            CoeusOptionPane.showInfoDialog(exception.getMessage());
        }
    }
    
    private void save()throws Exception {
        // Added by chandra 13/02/04 - start
        boolean isValidUnitId = validateUnitId();
        if(!isValidUnitId){
            CoeusOptionPane.showInfoDialog(INVALID_UNIT_NUMBER);
                return ;
        }// Added by chandra 13/02/04 - End
        if(txtNewUnit.getText().trim().equals(EMPTY)) {
                CoeusOptionPane.showInfoDialog(INVALID_UNIT_NUMBER);
                return ;
            }
        if (userMovementController.isUserMapped(userInfoBean)) {
            CoeusOptionPane.showInfoDialog("The user \""+userInfoBean.getUserId()+
            "\" is currently included as an approver in at least one map in unit "
            +userInfoBean.getUnitNumber()+
            " You may want to update \n"+ " your maps.");
            //return ;
        }
       // else { Bug fix #1733 - start. It should show the message and then move
                // the user to the desired unit.
            if(!canAddUser() || !canMaintainUser()) {
                CoeusOptionPane.showInfoDialog(CANNOT_MOVE_USER+txtNewUnit.getText().trim());
                return ;
            }
            if(validateUnitId()) {
                //Save
                userInfoBean.setUnitNumber(txtNewUnit.getText().trim());
                userInfoBean.setUnitName(lblNewUnitName.getText().trim());
                userInfoBean.setUpdateUser(mdiReference.getUserId());
                boolean saved = userMovementController.save(userInfoBean);
                if(! saved) {
                    CoeusOptionPane.showInfoDialog(COULD_NOT_SAVE);
                    return ;
                }
                else{
                    userMoved = true;
                    this.userInfoBean = userInfoBean;
                    setSaveRequired( true );
                    dlgMoveUser.dispose();
                }
            }
            else {
                CoeusOptionPane.showInfoDialog(INVALID_UNIT_NUMBER);
                return ;
            }
       // }Bug fix #1733 - End
        
    }
    
    private boolean hasRights() {
        return false;
    }
    
    private boolean unitMapInfo() {
        return false;
    }
    
    public boolean isUserMoved() {
        return userMoved;
    }
    
    public UserInfoBean getMovedUser() {
        return this.userInfoBean;
    }
    
    /** Getter for property addUser.
     * @return Value of property addUser.
     */
    public boolean canAddUser() {
        return addUser;
    }
    
    /** Setter for property addUser.
     * @param addUser New value of property addUser.
     */
    public void setAddUser(boolean addUser) {
        this.addUser = addUser;
    }
    
    /** Getter for property maintainUser.
     * @return Value of property maintainUser.
     */
    public boolean canMaintainUser() {
        return maintainUser;
    }
    
    /** Setter for property maintainUser.
     * @param maintainUser New value of property maintainUser.
     */
    public void setMaintainUser(boolean maintainUser) {
        this.maintainUser = maintainUser;
    }
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel lblUserName;
    private javax.swing.JTextField txtUserId;
    private javax.swing.JCheckBox chkNonMitPerson;
    private javax.swing.JTextField txtStatus;
    private javax.swing.JLabel lblUserId;
    private javax.swing.JTextField txtCurrentUnitId;
    private javax.swing.JLabel lblNewUnit;
    private javax.swing.JTextField txtUserName;
    private javax.swing.JLabel lblStatus;
    private javax.swing.JLabel lblCurrentUnit;
    private javax.swing.JPanel pnlButton;
    private javax.swing.JPanel pnlDetail;
    private javax.swing.JTextField txtNewUnit;
    private javax.swing.JTextField txtCurrentUnitName;
    private javax.swing.JButton btnOk;
    private javax.swing.JButton btnCancel;
    private javax.swing.JButton btnSearchUnit;
    private javax.swing.JLabel lblNewUnitName;
    // End of variables declaration//GEN-END:variables
    
}
