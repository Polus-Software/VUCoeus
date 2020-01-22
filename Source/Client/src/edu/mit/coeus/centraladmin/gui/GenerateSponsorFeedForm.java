/*
 * GenerateSponsorFeedForm.java
 *
 * Created on December 21, 2004, 10:43 AM
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
public class GenerateSponsorFeedForm extends javax.swing.JPanel implements ActionListener,KeyListener{
    
    private static final int WIDTH = 500;
    private static final int HEIGHT = 318;
    private static final String WINDOW_TITLE = "Sponsor Feed";
    private final String BUDGET_SERVLET ="/CentralAdminMaintenanceServlet";
    private static final char INIT_SPONSOR_FEED ='O';
    private static final char GENERATE_SPONSOR_FEED ='P';
    private final String connectTo = CoeusGuiConstants.CONNECTION_URL+ BUDGET_SERVLET;
    
    private CoeusAppletMDIForm mdiForm;
    private CoeusMessageResources coeusMessageResources;
    private CoeusDlgWindow dlgGenerateSponsorFeed;
    private String selectedTarget;
    private boolean status;
    private JPanel panel;
    /** Creates new form GenerateSponsorFeedForm */
    public GenerateSponsorFeedForm(CoeusAppletMDIForm mdiForm) {
        this.mdiForm = mdiForm;
        panel=this;
        coeusMessageResources=CoeusMessageResources.getInstance();
        initComponents();
        postInitComponents();
        registerComponents();
    }
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    
    private void postInitComponents() {
        dlgGenerateSponsorFeed = new CoeusDlgWindow(mdiForm);
        dlgGenerateSponsorFeed.setResizable(false);
        dlgGenerateSponsorFeed.setModal(true);
        dlgGenerateSponsorFeed.getContentPane().add(this);
        dlgGenerateSponsorFeed.setFont(CoeusFontFactory.getLabelFont());
        dlgGenerateSponsorFeed.setDefaultCloseOperation(CoeusDlgWindow.DO_NOTHING_ON_CLOSE);
        dlgGenerateSponsorFeed.setSize(WIDTH, HEIGHT);
        dlgGenerateSponsorFeed.setTitle(WINDOW_TITLE);
        
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        Dimension dlgSize = dlgGenerateSponsorFeed.getSize();
        dlgGenerateSponsorFeed.setLocation(screenSize.width/2 - (dlgSize.width/2),
        screenSize.height/2 - (dlgSize.height/2));
        
        dlgGenerateSponsorFeed.addWindowListener(new WindowAdapter(){
            
            public void windowClosing(WindowEvent we){
                performCloseAction();
                return;
            }
        });
        
        dlgGenerateSponsorFeed.addEscapeKeyListener(
        new AbstractAction("escPressed"){
            public void actionPerformed(ActionEvent ae){
                performCloseAction();
                return;
            }
        });
        
        dlgGenerateSponsorFeed.addComponentListener(
        new ComponentAdapter(){
            public void componentShown(ComponentEvent e){
                setWindowFocus();
            }
        });
    }
    
    private void registerComponents() {
        java.awt.Component[] components = {rdBtnProduction,btnClose,btnGenerateFeed};
        ScreenFocusTraversalPolicy traversePolicy = new ScreenFocusTraversalPolicy( components );
        this.setFocusTraversalPolicy(traversePolicy);
        this.setFocusCycleRoot(true);
        
        btnGenerateFeed.addActionListener(this);
        btnClose.addActionListener(this);
        rdBtnProduction.addActionListener(this);
        rdBtnDevelopment.addActionListener(this);
        rdBtnTest.addActionListener(this);
        rdBtnProduction.addKeyListener(this);
        rdBtnDevelopment.addKeyListener(this);
        rdBtnTest.addKeyListener(this);
        
    }
    
    private void performCloseAction() {
        dlgGenerateSponsorFeed.setVisible(false);
    }
    
    private void performGenerateAction() {
        if(selectedTarget.equals(null)) {
            CoeusOptionPane.showErrorDialog(coeusMessageResources.parseMessageKey("generateSponsorFeedFeedExceptionCode.2051"));
            return;
        }
        this.setCursor(new Cursor(Cursor.WAIT_CURSOR));
        generateSponsorFeed();
        if(status) {
            txtStatus.setText("Done");
        }
    }
    
    public void setWindowFocus() {
        rdBtnProduction.requestFocusInWindow();
    }
    
    public void display() {
        try{
            initSponsorFeed();
        }catch(CoeusClientException coeusClientException) {
            CoeusOptionPane.showDialog(coeusClientException);
        }
        dlgGenerateSponsorFeed.setVisible(true);
    }
    
    private void generateSponsorFeed() {
        txtStatus.setText("Generating Sponsor feed");
        Thread thread = new Thread(new Runnable() {
            public void run() {
                try {
                    RequesterBean requesterBean = new RequesterBean();
                    ResponderBean responderBean = new ResponderBean();
                    
                    requesterBean.setFunctionType(GENERATE_SPONSOR_FEED);
                    //customElemInfoBean.setAcType(TypeConstants.DELETE_RECORD);
                   
                    requesterBean.setDataObject(selectedTarget);
                    AppletServletCommunicator comm
                    = new AppletServletCommunicator(connectTo, requesterBean);
                    
                    comm.send();
                    responderBean = comm.getResponse();
                    boolean isErrorOccured = false;
                    if(responderBean!= null){
                        if(responderBean.isSuccessfulResponse()){
                            CoeusVector cvStatus = (CoeusVector)responderBean.getDataObject();
                            status = ((Boolean)cvStatus.get(0)).booleanValue();
							isErrorOccured = ((Boolean)cvStatus.get(1)).booleanValue();
                            if(status) {
                                txtStatus.setText("Done");
                                btnGenerateFeed.setEnabled(false);
                            }else if(!status) {
                                txtStatus.setText("Feed generation failed.");
								if (isErrorOccured) {
									CoeusOptionPane.showErrorDialog(coeusMessageResources.parseMessageKey("generateSponsorFeedExceptionCode.2051"));
								}
                            }
                        }else{
							if (isErrorOccured) {
								CoeusOptionPane.showErrorDialog(coeusMessageResources.parseMessageKey("generateSponsorFeedExceptionCode.2051"));
							}
                            throw new CoeusClientException(responderBean.getMessage(),CoeusClientException.ERROR_MESSAGE);
                        }
                    }
                    panel.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
                }catch(CoeusClientException coeusClientException) {
                    CoeusOptionPane.showDialog(coeusClientException);
                    panel.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
                    txtStatus.setText("Feed generation failed.");
                } catch (Exception exception){
					CoeusOptionPane.showErrorDialog(exception.getMessage());
					panel.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
					txtStatus.setText("Feed generation failed.");
				}
            }
        });
        thread.start();
    }
    
    private void initSponsorFeed() throws CoeusClientException {
        RequesterBean requesterBean = new RequesterBean();
        ResponderBean responderBean = new ResponderBean();
        
        
        requesterBean.setFunctionType(INIT_SPONSOR_FEED);
        //customElemInfoBean.setAcType(TypeConstants.DELETE_RECORD);
        //requesterBean.setDataObject(message);
        AppletServletCommunicator comm
        = new AppletServletCommunicator(connectTo, requesterBean);
        
        comm.send();
        responderBean = comm.getResponse();
        
        if(responderBean!= null){
            if(responderBean.isSuccessfulResponse()){
                CoeusVector cvInitMasterData = (CoeusVector)responderBean.getDataObject();
				if (cvInitMasterData != null && cvInitMasterData.size() > 0) {
					boolean hasTableChanged =((Boolean)cvInitMasterData.get(0)).booleanValue();
					String development= cvInitMasterData.get(1).toString();
					String production= cvInitMasterData.get(2).toString();
					String test= cvInitMasterData.get(3).toString();
					String sponsorFeedDate=cvInitMasterData.get(4).toString();
					String sponsorFeedUser=cvInitMasterData.get(5).toString();
					if(hasTableChanged) {
						lblSponsorInf.setText("Sponsor information in Coeus has");
						lblSponsorInfRemainder.setText("changed since the last feed");
					}else {
						lblSponsorInf.setText("Sponsor information has not");
						lblSponsorInfRemainder.setText("changed since the last feed");
					}
					lblDateField.setText(sponsorFeedDate);
                                        //lblByField.setText(sponsorFeedUser);
                                        /*
                                         * UserID to UserName Enhancement - Start
                                         * Added UserUtils class to change userid to username
                                         */                                        
					lblByField.setText(UserUtils.getDisplayName(sponsorFeedUser));
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
            }else{
                throw new CoeusClientException(responderBean.getMessage(),CoeusClientException.ERROR_MESSAGE);
            }
        }
    }
    
	private void initComponents() {//GEN-BEGIN:initComponents
		java.awt.GridBagConstraints gridBagConstraints;
		
		btnGrpTargetDir = new javax.swing.ButtonGroup();
		btnGenerateFeed = new javax.swing.JButton();
		btnClose = new javax.swing.JButton();
		pnlTargetDirectory = new javax.swing.JPanel();
		rdBtnProduction = new javax.swing.JRadioButton();
		rdBtnTest = new javax.swing.JRadioButton();
		rdBtnDevelopment = new javax.swing.JRadioButton();
		txtProduction = new edu.mit.coeus.utils.CoeusTextField();
		txtTest = new edu.mit.coeus.utils.CoeusTextField();
		txtDevelopment = new edu.mit.coeus.utils.CoeusTextField();
		lblSponsorInf = new javax.swing.JLabel();
		lblPreviousFeed = new javax.swing.JLabel();
		lblDate = new javax.swing.JLabel();
		lblBy = new javax.swing.JLabel();
		lblDateField = new javax.swing.JLabel();
		lblByField = new javax.swing.JLabel();
		txtStatus = new edu.mit.coeus.utils.CoeusTextField();
		lblSponsorInfRemainder = new javax.swing.JLabel();
		
		
		setLayout(new java.awt.GridBagLayout());
		
		setMinimumSize(new java.awt.Dimension(382, 314));
		setPreferredSize(new java.awt.Dimension(500, 318));
		btnGenerateFeed.setFont(CoeusFontFactory.getLabelFont());
		btnGenerateFeed.setMnemonic('G');
		btnGenerateFeed.setLabel("Generate ");
		btnGenerateFeed.setMaximumSize(new java.awt.Dimension(107, 25));
		btnGenerateFeed.setMinimumSize(new java.awt.Dimension(107, 25));
		btnGenerateFeed.setPreferredSize(new java.awt.Dimension(107, 25));
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 1;
		gridBagConstraints.gridy = 0;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.SOUTH;
		gridBagConstraints.weighty = 0.5;
		gridBagConstraints.insets = new java.awt.Insets(0, 0, 2, 20);
		add(btnGenerateFeed, gridBagConstraints);
		
		btnClose.setFont(CoeusFontFactory.getLabelFont());
		btnClose.setMnemonic('C');
		btnClose.setText("Close");
		btnClose.setMaximumSize(new java.awt.Dimension(107, 25));
		btnClose.setMinimumSize(new java.awt.Dimension(107, 25));
		btnClose.setPreferredSize(new java.awt.Dimension(107, 25));
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 1;
		gridBagConstraints.gridy = 1;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTH;
		gridBagConstraints.weighty = 0.5;
		gridBagConstraints.insets = new java.awt.Insets(2, 0, 0, 20);
		add(btnClose, gridBagConstraints);
		
		pnlTargetDirectory.setLayout(new java.awt.GridBagLayout());
		
		pnlTargetDirectory.setBorder(new javax.swing.border.TitledBorder(null, "Target Directory", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, CoeusFontFactory.getLabelFont(), new java.awt.Color(255, 51, 51)));
		pnlTargetDirectory.setMinimumSize(new java.awt.Dimension(450, 100));
		pnlTargetDirectory.setPreferredSize(new java.awt.Dimension(485, 107));
		rdBtnProduction.setFont(CoeusFontFactory.getLabelFont());
		rdBtnProduction.setText("Production");
		btnGrpTargetDir.add(rdBtnProduction);
		rdBtnProduction.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				rdBtnProductionActionPerformed(evt);
			}
		});
		
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 0;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
		gridBagConstraints.insets = new java.awt.Insets(0, 0, 3, 5);
		pnlTargetDirectory.add(rdBtnProduction, gridBagConstraints);
		
		rdBtnTest.setFont(CoeusFontFactory.getLabelFont());
		rdBtnTest.setText("Test");
		btnGrpTargetDir.add(rdBtnTest);
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 1;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
		gridBagConstraints.insets = new java.awt.Insets(0, 0, 3, 5);
		pnlTargetDirectory.add(rdBtnTest, gridBagConstraints);
		
		rdBtnDevelopment.setFont(CoeusFontFactory.getLabelFont()
		);
		rdBtnDevelopment.setText("Development");
		btnGrpTargetDir.add(rdBtnDevelopment);
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 2;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
		gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 5);
		pnlTargetDirectory.add(rdBtnDevelopment, gridBagConstraints);
		
		txtProduction.setEditable(false);
		txtProduction.setFont(CoeusFontFactory.getLabelFont()
		);
		txtProduction.setMinimumSize(new java.awt.Dimension(345, 21));
		txtProduction.setPreferredSize(new java.awt.Dimension(430, 21));
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
		gridBagConstraints.insets = new java.awt.Insets(0, 2, 3, 0);
		pnlTargetDirectory.add(txtProduction, gridBagConstraints);
		
		txtTest.setEditable(false);
		txtTest.setFont(CoeusFontFactory.getLabelFont());
		txtTest.setMinimumSize(new java.awt.Dimension(345, 21));
		txtTest.setPreferredSize(new java.awt.Dimension(430, 21));
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 1;
		gridBagConstraints.gridy = 1;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
		gridBagConstraints.insets = new java.awt.Insets(0, 2, 3, 0);
		pnlTargetDirectory.add(txtTest, gridBagConstraints);
		
		txtDevelopment.setEditable(false);
		txtDevelopment.setFont(CoeusFontFactory.getLabelFont());
		txtDevelopment.setMinimumSize(new java.awt.Dimension(345, 21));
		txtDevelopment.setPreferredSize(new java.awt.Dimension(430, 21));
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 1;
		gridBagConstraints.gridy = 2;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
		gridBagConstraints.insets = new java.awt.Insets(0, 2, 0, 0);
		pnlTargetDirectory.add(txtDevelopment, gridBagConstraints);
		
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 2;
		gridBagConstraints.gridwidth = 2;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
		gridBagConstraints.weightx = 0.5;
		gridBagConstraints.weighty = 0.5;
		gridBagConstraints.insets = new java.awt.Insets(0, 2, 0, 0);
		add(pnlTargetDirectory, gridBagConstraints);
		
		lblSponsorInf.setFont(CoeusFontFactory.getLabelFont());
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 0;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
		gridBagConstraints.weighty = 0.5;
		gridBagConstraints.insets = new java.awt.Insets(5, 2, 11, 0);
		add(lblSponsorInf, gridBagConstraints);
		
		lblPreviousFeed.setFont(CoeusFontFactory.getLabelFont());
		lblPreviousFeed.setForeground(new java.awt.Color(255, 51, 51));
		lblPreviousFeed.setText("Previous Feed:");
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 3;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
		gridBagConstraints.weighty = 0.5;
		gridBagConstraints.insets = new java.awt.Insets(2, 10, 0, 0);
		add(lblPreviousFeed, gridBagConstraints);
		
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
		
		lblDateField.setFont(new java.awt.Font("Microsoft Sans Serif", 0, 11));
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 4;
		gridBagConstraints.weighty = 0.5;
		add(lblDateField, gridBagConstraints);
		
		lblByField.setFont(new java.awt.Font("Microsoft Sans Serif", 0, 11));
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 5;
		gridBagConstraints.weighty = 0.5;
		gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 86);
		add(lblByField, gridBagConstraints);
		
		txtStatus.setEditable(false);
		txtStatus.setMinimumSize(new java.awt.Dimension(450, 18));
		txtStatus.setPreferredSize(new java.awt.Dimension(485, 18));
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 6;
		gridBagConstraints.gridwidth = 2;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
		gridBagConstraints.weighty = 0.5;
		gridBagConstraints.insets = new java.awt.Insets(0, 2, 0, 0);
		add(txtStatus, gridBagConstraints);
		
		lblSponsorInfRemainder.setFont(CoeusFontFactory.getLabelFont());
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 0;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.SOUTHWEST;
		gridBagConstraints.weighty = 0.5;
		gridBagConstraints.insets = new java.awt.Insets(10, 2, 0, 0);
		add(lblSponsorInfRemainder, gridBagConstraints);
		
	}//GEN-END:initComponents
    
    private void rdBtnProductionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rdBtnProductionActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_rdBtnProductionActionPerformed
    
    
	// Variables declaration - do not modify//GEN-BEGIN:variables
	public javax.swing.JButton btnClose;
	public javax.swing.JButton btnGenerateFeed;
	public javax.swing.ButtonGroup btnGrpTargetDir;
	public javax.swing.JLabel lblBy;
	public javax.swing.JLabel lblByField;
	public javax.swing.JLabel lblDate;
	public javax.swing.JLabel lblDateField;
	public javax.swing.JLabel lblPreviousFeed;
	public javax.swing.JLabel lblSponsorInf;
	public javax.swing.JLabel lblSponsorInfRemainder;
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
        GenerateSponsorFeedForm generateSponsorFeedForm =new GenerateSponsorFeedForm();
        JFrame frame= new JFrame();
        frame.getContentPane().add(generateSponsorFeedForm);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(500, 318);
        frame.show();
    }*/
    
    public void actionPerformed(ActionEvent e) {
        Object source = e.getSource();
        if(source.equals(btnGenerateFeed)) {
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