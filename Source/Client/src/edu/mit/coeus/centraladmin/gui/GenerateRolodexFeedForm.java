/*
 * GenerateRolodexFeedForm.java
 *
 * Created on December 24, 2004, 10:26 AM
 */

package edu.mit.coeus.centraladmin.gui;


import edu.mit.coeus.gui.CoeusFontFactory;
import edu.mit.coeus.gui.CoeusAppletMDIForm;
import edu.mit.coeus.gui.CoeusDlgWindow;
import edu.mit.coeus.utils.ScreenFocusTraversalPolicy;
import edu.mit.coeus.gui.CoeusMessageResources;
import edu.mit.coeus.utils.CoeusOptionPane;
import edu.mit.coeus.utils.CoeusGuiConstants;
import edu.mit.coeus.utils.AppletServletCommunicator;
import edu.mit.coeus.exception.*;
import edu.mit.coeus.utils.query.*;
import edu.mit.coeus.brokers.*;
import edu.mit.coeus.utils.CoeusVector;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.*;
import javax.swing.*;
import edu.mit.coeus.utils.TypeConstants;
import edu.mit.coeus.utils.UserUtils;
import java.awt.Color;
import java.awt.Cursor;
import javax.swing.JFrame;

/**
 *
 * @author  shijiv
 */
public class GenerateRolodexFeedForm extends javax.swing.JPanel implements ActionListener,KeyListener {
    
    private static final int WIDTH = 500;
    private static final int HEIGHT = 318;
    private static final String WINDOW_TITLE = "Rolodox Feed";
    private final String BUDGET_SERVLET ="/CentralAdminMaintenanceServlet";
    private static final char INIT_ROLODEX_FEED ='M';
    private static final char GENERATE_ROLODEX_FEED ='N';
    private final String connectTo = CoeusGuiConstants.CONNECTION_URL+ BUDGET_SERVLET;
    
    private CoeusAppletMDIForm mdiForm;
    private CoeusMessageResources coeusMessageResources;
    private CoeusDlgWindow dlgGenerateRolodoxFeed;
    private String selectedTarget;
    private boolean status;
    private JPanel panel;
    /** Creates new form GenerateRolodexFeedForm */
    public GenerateRolodexFeedForm(CoeusAppletMDIForm mdiForm) {
        this.mdiForm=mdiForm;
        panel=this;
        coeusMessageResources=CoeusMessageResources.getInstance();
        initComponents();
        postInitComponents();
        registerComponents();
    }
    
    private void postInitComponents() {
        dlgGenerateRolodoxFeed = new CoeusDlgWindow(mdiForm);
        dlgGenerateRolodoxFeed.setResizable(false);
        dlgGenerateRolodoxFeed.setModal(true);
        dlgGenerateRolodoxFeed.getContentPane().add(this);
        dlgGenerateRolodoxFeed.setFont(CoeusFontFactory.getLabelFont());
        dlgGenerateRolodoxFeed.setDefaultCloseOperation(CoeusDlgWindow.DO_NOTHING_ON_CLOSE);
        dlgGenerateRolodoxFeed.setSize(WIDTH, HEIGHT);
        dlgGenerateRolodoxFeed.setTitle(WINDOW_TITLE);
        
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        Dimension dlgSize = dlgGenerateRolodoxFeed.getSize();
        dlgGenerateRolodoxFeed.setLocation(screenSize.width/2 - (dlgSize.width/2),
        screenSize.height/2 - (dlgSize.height/2));
        
        dlgGenerateRolodoxFeed.addWindowListener(new WindowAdapter(){
            
            public void windowClosing(WindowEvent we){
                performCloseAction();
                return;
            }
        });
        
        dlgGenerateRolodoxFeed.addEscapeKeyListener(
        new AbstractAction("escPressed"){
            public void actionPerformed(ActionEvent ae){
                performCloseAction();
                return;
            }
        });
        
        dlgGenerateRolodoxFeed.addComponentListener(
        new ComponentAdapter(){
            public void componentShown(ComponentEvent e){
                setWindowFocus();
            }
        });
    }
    
    private void registerComponents() {
        java.awt.Component[] components = {btnGenerate,rdBtnProduction,btnClose};
        ScreenFocusTraversalPolicy traversePolicy = new ScreenFocusTraversalPolicy( components );
        this.setFocusTraversalPolicy(traversePolicy);
        this.setFocusCycleRoot(true);
        
        btnGenerate.addActionListener(this);
        btnClose.addActionListener(this);
        rdBtnProduction.addActionListener(this);
        rdBtnDevelopment.addActionListener(this);
        rdBtnTest.addActionListener(this);
        rdBtnProduction.addKeyListener(this);
        rdBtnDevelopment.addKeyListener(this);
        rdBtnTest.addKeyListener(this);
        
    }
    
    private void performCloseAction() {
        dlgGenerateRolodoxFeed.setVisible(false);
    }
    
    private void performGenerateAction() {
        if(selectedTarget.equals(null)) {
            CoeusOptionPane.showErrorDialog(coeusMessageResources.parseMessageKey("generateRolodexFeedExceptionCode.3001"));
            return;
        }
        this.setCursor(new Cursor(Cursor.WAIT_CURSOR));
        generateRolodexFeed();
        
        if(status) {
            txtStatus.setText("Done");
        }
        
    }
    
    
    private void setWindowFocus() {
        rdBtnProduction.requestFocusInWindow();
    }
    
    public void display() {
        try{
            initRolodexFeed();
        }catch(CoeusClientException coeusClientException) {
            CoeusOptionPane.showDialog(coeusClientException);
        }
        dlgGenerateRolodoxFeed.setVisible(true);
        
    }
    
    private void generateRolodexFeed()  {
        txtStatus.setText("Generating Rolodex feed");
        Thread thread = new Thread(new Runnable() {
            public void run() {
                try {
                    RequesterBean requesterBean = new RequesterBean();
                    ResponderBean responderBean = new ResponderBean();
                    
                    requesterBean.setFunctionType(GENERATE_ROLODEX_FEED);
                    //customElemInfoBean.setAcType(TypeConstants.DELETE_RECORD);
                    
                    requesterBean.setDataObject(selectedTarget);
                    AppletServletCommunicator comm
                    = new AppletServletCommunicator(connectTo, requesterBean);
                    
                    comm.send();
                    boolean isErrorOccured = false;
                    responderBean = comm.getResponse();
                    
                    if(responderBean!= null){
                        if(responderBean.isSuccessfulResponse()){
							CoeusVector cvStatus = (CoeusVector)responderBean.getDataObject();
                            status = ((Boolean)cvStatus.get(0)).booleanValue();
							isErrorOccured = ((Boolean)cvStatus.get(1)).booleanValue();
                            if(status) {
                                txtStatus.setText("Done");
                                btnGenerate.setEnabled(false);
                               
                            }else if(!status) {
                                txtStatus.setText("Feed generation failed.");
								if (isErrorOccured) {
									CoeusOptionPane.showErrorDialog(coeusMessageResources.parseMessageKey("generateSponsorFeedExceptionCode.2051"));
								}
							}
						}else {
							if (isErrorOccured) {
								CoeusOptionPane.showErrorDialog(coeusMessageResources.parseMessageKey("generateSponsorFeedExceptionCode.2051"));
							}
                            throw new CoeusClientException(responderBean.getMessage(),CoeusClientException.ERROR_MESSAGE);
                        }
                        panel.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
                    }
                }catch(CoeusClientException coeusClientException) {
                    CoeusOptionPane.showDialog(coeusClientException);
                    panel.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
                    txtStatus.setText("Feed generation failed.");
                }catch (Exception exception){
					CoeusOptionPane.showErrorDialog(exception.getMessage());
					panel.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
					txtStatus.setText("Feed generation failed.");
				}
            }
        });
        thread.start();
    }
    
    
    
    private void initRolodexFeed() throws CoeusClientException {
        RequesterBean requesterBean = new RequesterBean();
        ResponderBean responderBean = new ResponderBean();
        
        
        requesterBean.setFunctionType(INIT_ROLODEX_FEED);
        //customElemInfoBean.setAcType(TypeConstants.DELETE_RECORD);
        //requesterBean.setDataObject(message);
        AppletServletCommunicator comm
        = new AppletServletCommunicator(connectTo, requesterBean);
        
        comm.send();
        responderBean = comm.getResponse();
        
        if(responderBean!= null){
            if(responderBean.isSuccessfulResponse()){
                CoeusVector cvInitMasterData = (CoeusVector)responderBean.getDataObject();
                //boolean hasTableChanged =((Boolean)cvInitMasterData.get(0)).booleanValue();
				if (cvInitMasterData != null && cvInitMasterData.size() > 0) {
					String development= cvInitMasterData.get(0).toString();
					String production= cvInitMasterData.get(1).toString();
					String test= cvInitMasterData.get(2).toString();
					String rolodexFeedDate=cvInitMasterData.get(3).toString();
					String rolodexFeedUser = cvInitMasterData.get(4).toString();
					lblDateValue.setText(rolodexFeedDate);
//					lblByValue.setText(rolodexFeedUser);
                                        /*
                                         * UserID to UserName Enhancement - Start
                                         * Added UserUtils class to change userid to username
                                         */
                                        lblByValue.setText(UserUtils.getDisplayName(rolodexFeedUser));
                                        // UserId to UserName Enhancement - End
					txtStatus.setText("Ready");
					rdBtnProduction.setSelected(true);
					selectedTarget="Production";
					txtProduction.setEditable(true);
					txtProduction.setRequestFocusEnabled(false);
					txtProduction.setBackground(Color.yellow);
					txtProduction.setText(production);
					txtDevelopment.setText(development);
					txtTest.setText(test);
					txtDevelopment.setFont(CoeusFontFactory.getNormalFont());
					txtTest.setFont(CoeusFontFactory.getNormalFont());
				}
            } else {
                throw new CoeusClientException(responderBean.getMessage(),CoeusClientException.ERROR_MESSAGE);
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

        btnGrpTargetDir = new javax.swing.ButtonGroup();
        btnGenerate = new javax.swing.JButton();
        btnClose = new javax.swing.JButton();
        pnlTargetDirectory = new javax.swing.JPanel();
        rdBtnProduction = new javax.swing.JRadioButton();
        rdBtnTest = new javax.swing.JRadioButton();
        rdBtnDevelopment = new javax.swing.JRadioButton();
        txtProduction = new edu.mit.coeus.utils.CoeusTextField();
        txtTest = new edu.mit.coeus.utils.CoeusTextField();
        txtDevelopment = new edu.mit.coeus.utils.CoeusTextField();
        lblDate = new javax.swing.JLabel();
        lblDateValue = new javax.swing.JLabel();
        lblBy = new javax.swing.JLabel();
        lblByValue = new javax.swing.JLabel();
        txtStatus = new edu.mit.coeus.utils.CoeusTextField();
        lblPreviousFeed = new javax.swing.JLabel();

        setLayout(new java.awt.GridBagLayout());

        setMinimumSize(new java.awt.Dimension(382, 300));
        setPreferredSize(new java.awt.Dimension(500, 318));
        btnGenerate.setFont(CoeusFontFactory.getLabelFont());
        btnGenerate.setMnemonic('G');
        btnGenerate.setText("Generate");
        btnGenerate.setMaximumSize(new java.awt.Dimension(107, 25));
        btnGenerate.setMinimumSize(new java.awt.Dimension(107, 25));
        btnGenerate.setPreferredSize(new java.awt.Dimension(107, 25));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.SOUTHWEST;
        gridBagConstraints.weighty = 0.5;
        gridBagConstraints.insets = new java.awt.Insets(5, 0, 0, 33);
        add(btnGenerate, gridBagConstraints);

        btnClose.setFont(CoeusFontFactory.getLabelFont());
        btnClose.setMnemonic('C');
        btnClose.setText("Close");
        btnClose.setMaximumSize(new java.awt.Dimension(107, 25));
        btnClose.setMinimumSize(new java.awt.Dimension(107, 25));
        btnClose.setPreferredSize(new java.awt.Dimension(107, 25));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weighty = 0.5;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 5, 33);
        add(btnClose, gridBagConstraints);

        pnlTargetDirectory.setLayout(new java.awt.GridBagLayout());

        pnlTargetDirectory.setBorder(new javax.swing.border.TitledBorder(null, "Target Diretory", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, CoeusFontFactory.getLabelFont(), new java.awt.Color(255, 51, 51)));
        pnlTargetDirectory.setMinimumSize(new java.awt.Dimension(376, 88));
        pnlTargetDirectory.setPreferredSize(new java.awt.Dimension(484, 107));
        rdBtnProduction.setFont(CoeusFontFactory.getLabelFont());
        rdBtnProduction.setText("Production");
        btnGrpTargetDir.add(rdBtnProduction);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weighty = 0.5;
        pnlTargetDirectory.add(rdBtnProduction, gridBagConstraints);

        rdBtnTest.setFont(CoeusFontFactory.getLabelFont());
        rdBtnTest.setText("Test");
        btnGrpTargetDir.add(rdBtnTest);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weighty = 0.5;
        pnlTargetDirectory.add(rdBtnTest, gridBagConstraints);

        rdBtnDevelopment.setFont(CoeusFontFactory.getLabelFont());
        rdBtnDevelopment.setText("Development");
        btnGrpTargetDir.add(rdBtnDevelopment);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weighty = 0.5;
        pnlTargetDirectory.add(rdBtnDevelopment, gridBagConstraints);

        txtProduction.setEditable(false);
        txtProduction.setFont(CoeusFontFactory.getLabelFont());
        txtProduction.setMinimumSize(new java.awt.Dimension(359, 21));
        txtProduction.setPreferredSize(new java.awt.Dimension(414, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.weightx = 0.5;
        gridBagConstraints.weighty = 0.5;
        pnlTargetDirectory.add(txtProduction, gridBagConstraints);

        txtTest.setEditable(false);
        txtTest.setFont(CoeusFontFactory.getLabelFont());
        txtTest.setMinimumSize(new java.awt.Dimension(359, 21));
        txtTest.setPreferredSize(new java.awt.Dimension(414, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.weightx = 0.5;
        gridBagConstraints.weighty = 0.5;
        pnlTargetDirectory.add(txtTest, gridBagConstraints);

        txtDevelopment.setEditable(false);
        txtDevelopment.setFont(CoeusFontFactory.getLabelFont()
        );
        txtDevelopment.setMinimumSize(new java.awt.Dimension(359, 21));
        txtDevelopment.setPreferredSize(new java.awt.Dimension(414, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.weightx = 0.5;
        gridBagConstraints.weighty = 0.5;
        pnlTargetDirectory.add(txtDevelopment, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.weightx = 0.5;
        gridBagConstraints.weighty = 0.5;
        gridBagConstraints.insets = new java.awt.Insets(0, 2, 0, 0);
        add(pnlTargetDirectory, gridBagConstraints);

        lblDate.setFont(CoeusFontFactory.getLabelFont());
        lblDate.setText("Date:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 0.5;
        gridBagConstraints.weighty = 0.5;
        gridBagConstraints.insets = new java.awt.Insets(0, 41, 0, 0);
        add(lblDate, gridBagConstraints);

        lblDateValue.setFont(new java.awt.Font("Microsoft Sans Serif", 0, 11));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.weighty = 0.5;
        add(lblDateValue, gridBagConstraints);

        lblBy.setFont(CoeusFontFactory.getLabelFont());
        lblBy.setText("By:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 0.5;
        gridBagConstraints.weighty = 0.5;
        gridBagConstraints.insets = new java.awt.Insets(0, 41, 0, 0);
        add(lblBy, gridBagConstraints);

        lblByValue.setFont(new java.awt.Font("Microsoft Sans Serif", 0, 11));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.weighty = 0.5;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 86);
        add(lblByValue, gridBagConstraints);

        txtStatus.setEditable(false);
        txtStatus.setMinimumSize(new java.awt.Dimension(376, 18));
        txtStatus.setPreferredSize(new java.awt.Dimension(484, 18));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.weighty = 0.5;
        gridBagConstraints.insets = new java.awt.Insets(0, 2, 0, 0);
        add(txtStatus, gridBagConstraints);

        lblPreviousFeed.setFont(CoeusFontFactory.getLabelFont());
        lblPreviousFeed.setForeground(new java.awt.Color(255, 51, 51));
        lblPreviousFeed.setText("Previous Feed:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weighty = 0.5;
        gridBagConstraints.insets = new java.awt.Insets(0, 15, 0, 0);
        add(lblPreviousFeed, gridBagConstraints);

    }//GEN-END:initComponents
    
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    public javax.swing.JButton btnClose;
    public javax.swing.JButton btnGenerate;
    public javax.swing.ButtonGroup btnGrpTargetDir;
    public javax.swing.JLabel lblBy;
    public javax.swing.JLabel lblByValue;
    public javax.swing.JLabel lblDate;
    public javax.swing.JLabel lblDateValue;
    public javax.swing.JLabel lblPreviousFeed;
    public javax.swing.JPanel pnlTargetDirectory;
    public javax.swing.JRadioButton rdBtnDevelopment;
    public javax.swing.JRadioButton rdBtnProduction;
    public javax.swing.JRadioButton rdBtnTest;
    public edu.mit.coeus.utils.CoeusTextField txtDevelopment;
    public edu.mit.coeus.utils.CoeusTextField txtProduction;
    public edu.mit.coeus.utils.CoeusTextField txtStatus;
    public edu.mit.coeus.utils.CoeusTextField txtTest;
    // End of variables declaration//GEN-END:variables
    
   /* public static void main(String args[]) {
        GenerateRolodexFeedForm generateRolodexFeedForm =new GenerateRolodexFeedForm();
        JFrame frame= new JFrame();
        frame.getContentPane().add(generateRolodexFeedForm);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(500, 318);
        frame.show();
    }*/
    
    public void actionPerformed(ActionEvent e) {
        Object source = e.getSource();
        if(source.equals(btnGenerate)) {
            performGenerateAction();
        }else if(source.equals(btnClose)) {
            performCloseAction();
        }else if(source.equals(rdBtnDevelopment)) {
            selectedTarget="Development";
            txtDevelopment.setEditable(true);
            txtDevelopment.setFont(CoeusFontFactory.getLabelFont());
            txtDevelopment.setRequestFocusEnabled(false);
            txtDevelopment.setBackground(Color.yellow);
            txtProduction.setEditable(false);
            txtProduction.setFont(CoeusFontFactory.getNormalFont());
            txtTest.setEditable(false);
            txtTest.setFont(CoeusFontFactory.getNormalFont());
        }else if(source.equals(rdBtnProduction)) {
            selectedTarget="Production";
            txtProduction.setEditable(true);
            txtProduction.setFont(CoeusFontFactory.getLabelFont());
            txtProduction.setRequestFocusEnabled(false);
            txtProduction.setBackground(Color.yellow);
            txtTest.setEditable(false);
            txtTest.setFont(CoeusFontFactory.getNormalFont());
            txtDevelopment.setEditable(false);
            txtDevelopment.setFont(CoeusFontFactory.getNormalFont());
        }else if(source.equals(rdBtnTest)) {
            selectedTarget="Test";
            txtTest.setEditable(true);
            txtTest.setFont(CoeusFontFactory.getLabelFont());
            txtTest.setRequestFocusEnabled(false);
            txtTest.setBackground(Color.yellow);
            txtDevelopment.setEditable(false);
            txtDevelopment.setFont(CoeusFontFactory.getNormalFont());
            txtProduction.setEditable(false);
            txtProduction.setFont(CoeusFontFactory.getNormalFont());
        }
    }
    
    public void keyPressed(KeyEvent e) {
         int source=e.getKeyCode();
        if(source == KeyEvent.VK_DOWN) {
            if(selectedTarget.equals("Production")) {
                selectedTarget="Test";
                rdBtnTest.doClick();
            }else if(selectedTarget.equals("Test")) {
                selectedTarget="Development";
                rdBtnDevelopment.doClick();
            }else if(selectedTarget.equals("Development")){
                selectedTarget="Production";
                rdBtnProduction.doClick();
            }
        }else if(source == KeyEvent.VK_UP) {
            if(selectedTarget.equals("Production")) {
                selectedTarget="Development";
                rdBtnDevelopment.doClick();
            }else if(selectedTarget.equals("Test")) {
                selectedTarget="Production";
                rdBtnProduction.doClick();
            }else if(selectedTarget.equals("Development")){
                selectedTarget="Test";
                rdBtnTest.doClick();
            }
        }
    }
    
    public void keyReleased(KeyEvent e) {
    }
    
    public void keyTyped(KeyEvent e) {
    }
    
}
