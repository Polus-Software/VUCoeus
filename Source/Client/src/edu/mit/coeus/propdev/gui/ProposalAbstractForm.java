/*
 * ProposalAbstractForm.java
 *
 * Created on May 31, 2003, 6:21 PM
 */

package edu.mit.coeus.propdev.gui;

import edu.mit.coeus.gui.*;
import edu.mit.coeus.propdev.bean.*;
import edu.mit.coeus.gui.toolbar.CoeusToolBarButton;
import edu.mit.coeus.gui.menu.CoeusMenuItem;
import edu.mit.coeus.gui.menu.CoeusMenu;
import edu.mit.coeus.gui.menu.CoeusFileMenu;
import edu.mit.coeus.utils.*;
import edu.mit.coeus.utils.roles.*;
import edu.mit.coeus.utils.tree.UserRoleNodeRenderer;
import edu.mit.coeus.propdev.gui.*;

import edu.mit.coeus.brokers.RequesterBean;
import edu.mit.coeus.brokers.ResponderBean;
import edu.mit.coeus.bean.*;

import javax.swing.*;
import javax.swing.event.*;
import javax.swing.table.DefaultTableModel;
import java.beans.*;

import java.awt.*;
import java.awt.event.*;

import java.util.*;

/** This Form is used to display all the Abstract text for a
 * Selected Proposal.
 * @author Senthil
 */
public class ProposalAbstractForm extends CoeusInternalFrame implements
ActionListener,TypeConstants {
    
    //contains the proposalNumber from the ProposalDetailForm.
    private String proposalNumber;
    private String sponsorCodeName;
    private String title;
    private char functionType;
    private final String PROPOSAL_MAINT = "/ProposalMaintenanceServlet";
    
    private CoeusToolBarButton  btnSave, btnClose;
    private CoeusMenuItem save,close;
    
    //holds the reference for mdi form
    private CoeusAppletMDIForm mdiForm = null;
    
    // holds the AbstractTypeFormBean which has the Description on the Tab
    AbstractTypeFormBean abstractTypeFormBean = null;
    
    // holds the ProposalAbstractFormBean which has the contents of the Text Area, for a tab for a Proposal.
    ProposalAbstractFormBean proposalAbstractFormBean = null;
    
    // holds the ProposalAbstractFormBean which has the contents of the Text Area, for a tab for a Proposal.
    ProposalAbstractFormBean abstactBeanFromDb = null;
    
    // holds the AbstractTypeFormBean
    Vector vAbstractType = new Vector();
    
    // holds the size of the vAbstractType vector
    int abstractTypeVecSize = 0;
    
    // holds the ProposalAbstractFormBean
    Vector vProposalAbstract = new Vector();
    
    // holds the size of the vProposalAbstract vector
    int proposalAbstractVecSize = 0;
    
    // This is a vector of Vectors which contains the 0) vAbstractType vector 1)vProposalAbstract vector
    Vector vecDBData;
    
    // holds the consolidated ProposalAbstractFormBean with desc and TextArea contents.
    Vector vAbstractBeans = new Vector();
    
    // holds the size of the vAbstractBeans vector
    int abstractBeansVecSize = 0;
    
    // holds the consolidated ProposalAbstractFormBean with desc, TextArea contents and listener added.
    Vector vAbstractFinalList = new Vector();
    
    // holds the size of the vAbstractFinalList vector
    int abstractFinalListVecSize = 0;
    
    // holds the consolidated ProposalAbstractFormBean with desc and TextArea contents.
    Vector vAbstractUpdList = new Vector();
    
    // holds the vector of ProposalAbstractFormBean with only those desc and TextArea contents that need to be updated.
    Vector finalVector = new Vector();
    
    //holds CoeusMessageResources instance used for reading message Properties.
    private CoeusMessageResources coeusMessageResources;
    
    //holds the Tabbed pane of abstract screens
    private JTabbedPane tbdPnProposal = null;
    
    //holds the content of each of the tabs on the tabbed pane
    private JPanel absJPanel[] ;
    
    //holds the save required flag
    private boolean saveRequired = false;
    
    private static final String PROP_ABSTRACT_FORM_TITLE = "Proposal Abstract Form";
    /** Creates the ProposalAbstractForm.
     * @param title Title of the Form.
     * @param mdiForm mdiForm
     */
    public ProposalAbstractForm(char functionType, String proposalNumber, CoeusAppletMDIForm mdiForm) {
        super(PROP_ABSTRACT_FORM_TITLE, mdiForm);
        this.title = PROP_ABSTRACT_FORM_TITLE;
        this.proposalNumber = proposalNumber;
        this.functionType = functionType;
        this.mdiForm = mdiForm;
        this.setFrameToolBar(getToolBar());
        setFrameIcon(mdiForm.getCoeusIcon());
        this.setBackground((java.awt.Color) javax.swing.UIManager.getDefaults().get("Panel.background"));
    }
    
    private JToolBar getToolBar() {
        JToolBar toolbar = new JToolBar();
        btnSave = new CoeusToolBarButton(new ImageIcon(
        getClass().getClassLoader().getResource(CoeusGuiConstants.SAVE_ICON)),
        null, "Save");
        btnSave.setDisabledIcon(new ImageIcon(
        getClass().getClassLoader().getResource(CoeusGuiConstants.DSAVE_ICON)));
        btnClose = new CoeusToolBarButton(new ImageIcon(
        getClass().getClassLoader().getResource(CoeusGuiConstants.CLOSE_ICON)),
        null, "Close");
        btnSave.addActionListener(this);
        btnClose.addActionListener(this);
        toolbar.add(btnSave);
        toolbar.add(btnClose);
        if(functionType == DISPLAY_MODE){
            btnSave.setEnabled(false);
        }
        return toolbar;
    }
    /** This method is used to show the ProposalAbstractForm.
     */
    public void showDialogForm() {
        try{
            initComponents();
            lblProposalNoValue.setText(proposalNumber);
            lblSponsorValue.setText(sponsorCodeName);
            createProposalPanel();
            
            
            
            
            /* This will catch the window closing event and
             * checks any data is modified.If any changes are done by
             * the user the system will ask for confirmation of Saving the info.
             */
            this.addVetoableChangeListener(new VetoableChangeListener(){
                public void vetoableChange(PropertyChangeEvent pce)
                throws PropertyVetoException {
                    if (pce.getPropertyName().equals(
                    JInternalFrame.IS_CLOSED_PROPERTY) ) {
                        boolean changed = ((Boolean) pce.getNewValue()).booleanValue();
                        if( changed ) {
                            try {
                                setDataToBean();
                                closeProposalAbstractForm();
                            } catch ( Exception e) {
                                if(!( e.getMessage().equals(
                                coeusMessageResources.parseMessageKey(
                                "Proposal_AbsForm_exceptionCode.1130")) )){
                                    CoeusOptionPane.showErrorDialog(e.getMessage());
                                }
                                throw new PropertyVetoException(
                                coeusMessageResources.parseMessageKey(
                                "Proposal_AbsForm_exceptionCode.1130"),null);
                            }
                        }
                    }
                }
            });
            
            setTitle( CoeusGuiConstants.PROPOSAL_ABSTRACTS_FRAME_TITLE);
            
            mdiForm.putFrame( CoeusGuiConstants.PROPOSAL_ABSTRACTS_FRAME_TITLE,
            this.proposalNumber, this.functionType, this );
            mdiForm.getDeskTopPane().add(this);
            setVisible(true);
            setSelected(true);
            
            //Added for case#2349 - Update timestamp for the Abstracts Module - start
            setUserTimeStamp(tbdPnProposal.getSelectedIndex(), false);
            //Added for case#2349 - Update timestamp for the Abstracts Module - end
            
           //Added Ajay to set the focus to TextArea: Start 24-08-2004
            SwingUtilities.invokeLater(new Runnable() {
                public void run()  {
                    javax.swing.JTextArea txtArea ;
                    int selIndex = tbdPnProposal.getSelectedIndex();
                    txtArea = ((ProposalAbstractTab) absJPanel[0]).getTextArea();
                    txtArea.requestFocusInWindow();
                }
            });
            //Added Ajay to set the focus to TextArea: End 24-08-2004                    
            
        } catch (Exception e){
            e.printStackTrace();
        }
    }
    
    /**
     * initialize the proposal components.
     * Creates a panel and add the components.
     */
    private void createProposalPanel() throws Exception{
        getDataFromDB();
        pnlTabbedPanes.setLayout(new BorderLayout());
        pnlTabbedPanes.add(createForm(),BorderLayout.CENTER );      
        coeusMessageResources = CoeusMessageResources.getInstance();
    }
    
    
    /**
     * This method will take the two vectors, vAbstractType Vector which contains AbstractTypeFormBean
     * and vProposalAbstract Vector which contains proposalAbstractFormBean and then create a 3rd Vector
     * vAbstractBeans which will contain proposalAbstractFormBeans which have both the description
     * and the abstract text.
     *
     */
    private void createAbstractBean(){
        if ((vecDBData.elementAt(0) != null)&&
        (vecDBData.elementAt(1) != null)){
            try{
                vAbstractType = (Vector) vecDBData.elementAt(0);
                vProposalAbstract = (Vector) vecDBData.elementAt(1);
                
                abstractTypeVecSize = vAbstractType.size();
                proposalAbstractVecSize = vProposalAbstract.size();
                
                
                for( int indx = 0 ; indx < abstractTypeVecSize; indx++) {
                    
                    abstractTypeFormBean =
                    (AbstractTypeFormBean) vAbstractType.elementAt( indx );
                    proposalAbstractFormBean =
                    new ProposalAbstractFormBean();
                    proposalAbstractFormBean.setProposalNumber(proposalNumber);
                    int absTypeIdAbstractTypeBean = abstractTypeFormBean.getAbstractTypeId();
                    proposalAbstractFormBean.setAbstractTypeCode(absTypeIdAbstractTypeBean);
                    
                    for( int i = 0 ; i < proposalAbstractVecSize; i++) {
                        abstactBeanFromDb =
                        (ProposalAbstractFormBean) vProposalAbstract.elementAt( i );
                        
                        int absTypeIdProposalAbstractBean = abstactBeanFromDb.getAbstractTypeCode();
                        if (absTypeIdAbstractTypeBean == absTypeIdProposalAbstractBean){
                            proposalAbstractFormBean = abstactBeanFromDb;
                            i = proposalAbstractVecSize;
                        } //end of if
                    } //end of for
                    vAbstractBeans.addElement(proposalAbstractFormBean);
                } // end of for
            }catch(Exception e){
                e.printStackTrace();
            }
        }else if (vecDBData.elementAt(1) == null) {
            try{
                vAbstractType = (Vector) vecDBData.elementAt(0);
                
                int abstractTypeVecSize = vAbstractType.size();
                
                
                for( int indx = 0 ; indx < abstractTypeVecSize; indx++) {
                    
                    abstractTypeFormBean =
                    (AbstractTypeFormBean) vAbstractType.elementAt( indx );
                    proposalAbstractFormBean =
                    new ProposalAbstractFormBean();
                    proposalAbstractFormBean.setProposalNumber(proposalNumber);
                    proposalAbstractFormBean.setAbstractTypeCode(abstractTypeFormBean.getAbstractTypeId());
                    proposalAbstractFormBean.setAbstractText("");
                    
                    vAbstractBeans.addElement(proposalAbstractFormBean);
                } // end of for
            }catch(Exception e){
                e.printStackTrace();
            }
        } else {
        }
        // Constructing the panel to be placed inside the tabbedPane
        abstractBeansVecSize = vAbstractBeans.size();
    }
    
    /**
     * This method will take the vAbstractBeans Vector which contains proposalAbstractFormBean and
     * then add the Property Change listener to each of the  proposalAbstractFormBean elements of the
     * vAbstractBeans vector and set the beans to a new vector.
     * and the abstract text.
     *
     */
    private Vector addPropertyListener(Vector vAbsFinList){
        Vector result = new Vector();
        ProposalAbstractFormBean newBean;
        for( int l = 0 ; l < vAbsFinList.size(); l++) {
            proposalAbstractFormBean =
            (ProposalAbstractFormBean) vAbsFinList.elementAt(l);
            newBean = new ProposalAbstractFormBean();
            newBean.setAbstractText( proposalAbstractFormBean.getAbstractText());
            newBean.setAbstractTypeCode( proposalAbstractFormBean.getAbstractTypeCode());
            newBean.setAcType( proposalAbstractFormBean.getAcType());
            newBean.setAwAbstractTypeCode( proposalAbstractFormBean.getAwAbstractTypeCode());
            newBean.setProposalNumber( proposalAbstractFormBean.getProposalNumber());
            newBean.setUpdateTimestamp( proposalAbstractFormBean.getUpdateTimestamp());
            newBean.setUpdateUser( proposalAbstractFormBean.getUpdateUser());
            
            newBean.addPropertyChangeListener(
            new PropertyChangeListener(){
                public void propertyChange(PropertyChangeEvent pce){
                    if ( pce.getNewValue() == null && pce.getOldValue() != null ) {
                        saveRequired = true;
                    }
                    if( pce.getNewValue() != null && pce.getOldValue() == null ) {
                        saveRequired = true;
                    }
                    if( pce.getNewValue()!=null && pce.getOldValue()!=null ) {
                        if (!(  pce.getNewValue().toString().trim().equalsIgnoreCase(pce.getOldValue().toString().trim())))  {
                            saveRequired = true;
                        }
                    }
                }
            });
            result.addElement(newBean);
        }
        return result ;
    }
    
    /** This method is used to create the tabbedpane along with the components
     * which are used in <CODE>ProposalDetailsForm</CODE>.
     *
     */
    
    private JTabbedPane createForm(){
        
        //setValues(getDataFromDB());
        tbdPnProposal = new CoeusTabbedPane(CoeusTabbedPane.CTRL_T);
        tbdPnProposal.setFont( CoeusFontFactory.getNormalFont() );
        
        // adding listener
        tbdPnProposal.addChangeListener(new ChangeTab());
        
        
        // create NameAddress tab
        FlowLayout layout = new FlowLayout(FlowLayout.LEFT);
        JPanel pnlDetail = new JPanel(layout);
        
        createAbstractBean();
        vAbstractFinalList = addPropertyListener(vAbstractBeans);
        abstractFinalListVecSize = vAbstractFinalList.size();
        
        proposalAbstractFormBean = null;
        if ((vAbstractFinalList != null)&&(abstractFinalListVecSize > 0)) {
            absJPanel = new JPanel[abstractFinalListVecSize];
            for( int j = 0 ; j < abstractFinalListVecSize; j++) {
                proposalAbstractFormBean =
                (ProposalAbstractFormBean) vAbstractFinalList.elementAt(j);
                absJPanel[j] = new
                ProposalAbstractTab(proposalAbstractFormBean.getAbstractText());
                ((ProposalAbstractTab) absJPanel[j]).formatFields(functionType != DISPLAY_MODE ? true : false );
                tbdPnProposal.addTab(getTabLabel(proposalAbstractFormBean.getAbstractTypeCode()) ,
                absJPanel[j]);
                
                if (proposalAbstractFormBean.getAbstractText() != null &&
                proposalAbstractFormBean.getAbstractText().trim().length() > 0) {
                    tbdPnProposal.setIconAt(j, new ImageIcon(getClass().getClassLoader().getResource(
                    CoeusGuiConstants.ABSTRACT_ICON)));
                }
            }
            tbdPnProposal.setSelectedIndex(0);
        }
        
        return tbdPnProposal;
        
    }
    
    private String getTabLabel(int tabId) {
        String desc=null;
        int abstractTypeVecSize = vAbstractType.size();
        for( int indx = 0 ; indx < abstractTypeVecSize; indx++) {
            AbstractTypeFormBean abstractTypeFormBean =
            (AbstractTypeFormBean) vAbstractType.elementAt( indx );
            if (abstractTypeFormBean.getAbstractTypeId()== tabId){
                desc = abstractTypeFormBean.getDescription();
                break;
            }
        }
        return desc;
    }
    /**
     *Gets data from the Database tables
     */
    private void getDataFromDB() {
        
        String connectTo = CoeusGuiConstants.CONNECTION_URL
        + PROPOSAL_MAINT;
        /* connect to the database and get the formData for the
         * given proposalNumber
         */
        
        RequesterBean request = new RequesterBean();
        
        request.setId(proposalNumber);
        request.setFunctionType('J');
        
        AppletServletCommunicator comm = new AppletServletCommunicator(
        connectTo, request);
        comm.send();
        ResponderBean response = comm.getResponse();
        
        vecDBData = (Vector) response.getDataObjects();
        
    }
    
    /**
     *Gets data from the form and updates bean Vector.
     */
    private void setDataToBean() {
        
        proposalAbstractFormBean = null;
        for ( int k = 0 ; k < absJPanel.length ; k++) {
            String textAreaContent = ((ProposalAbstractTab)absJPanel[k]).getAbstractText();
            proposalAbstractFormBean = (ProposalAbstractFormBean)vAbstractFinalList.elementAt(k);
            proposalAbstractFormBean.setAbstractText(textAreaContent);
            vAbstractFinalList.set(k, proposalAbstractFormBean);
        }
    }
    
    /**
     *Gets data from the form
     */
    private Vector getFormData() {
        vAbstractUpdList = new Vector();
        String strPreviousText = "";
        proposalAbstractFormBean = null;
        for ( int k = 0 ; k < absJPanel.length ; k++) {
            String textAreaContent = "";
            textAreaContent = ((ProposalAbstractTab)absJPanel[k]).getAbstractText();
            textAreaContent = (textAreaContent == null?"":textAreaContent);
            proposalAbstractFormBean = (ProposalAbstractFormBean)vAbstractBeans.elementAt(k);
            strPreviousText = proposalAbstractFormBean.getAbstractText();
            strPreviousText = (strPreviousText == null? "" : strPreviousText);
            if(!textAreaContent.equals(strPreviousText)){
                if(strPreviousText.equals("")){
                    proposalAbstractFormBean.setAcType(INSERT_RECORD);
                }
                else if(textAreaContent.equals("")){
                    proposalAbstractFormBean.setAcType(DELETE_RECORD);
                }
                else{
                    proposalAbstractFormBean.setAcType(UPDATE_RECORD);
                }
                proposalAbstractFormBean.setAbstractText(textAreaContent);
                vAbstractUpdList.addElement(proposalAbstractFormBean);
            }
        }
        return vAbstractUpdList;
    }
    
    private void updDataToDb(Vector absFinalList) {
        String connectTo = CoeusGuiConstants.CONNECTION_URL + PROPOSAL_MAINT;
        /* connect to the database and get the formData for the
         * given proposalNumber and roleId
         */
        RequesterBean request = new RequesterBean();
        request.setUserName(CoeusGuiConstants.getMDIForm().getUserName());
        request.setDataObjects(absFinalList);
        request.setFunctionType('j');
        AppletServletCommunicator comm = new AppletServletCommunicator(
        connectTo, request);
        comm.send();
        ResponderBean response = comm.getResponse();
        saveRequired = false;
    }
    
    /**
     * Closing the ProposalAbstractForm when Close button is pressed or
     * Window close title bar button is pressed.
     */
    private void closeProposalAbstractForm() throws Exception{
        CoeusInternalFrame frame=null;
        if ( ! isSaveRequired()) {
            mdiForm.removeFrame(CoeusGuiConstants.PROPOSAL_ABSTRACTS_FRAME_TITLE,proposalNumber);
            this.dispose();
            frame = mdiForm.getFrame(
            CoeusGuiConstants.PROPOSAL_DETAILS_FRAME_TITLE,proposalNumber);
            if(frame != null){
                frame.setSelected(true);
                frame.setVisible(true);
            }
        }else{
            // Added by chandra 07/02/04- start- save confirmation is thrwing in display mode
            if(functionType!=TypeConstants.DISPLAY_MODE){
                // Added by chandra 07/02/04- end
                String msg = coeusMessageResources.parseMessageKey(
                "saveConfirmCode.1002");
                int confirm = CoeusOptionPane.showQuestionDialog(msg,
                CoeusOptionPane.OPTION_YES_NO_CANCEL,
                CoeusOptionPane.DEFAULT_YES);
                
                switch(confirm){
                    case(JOptionPane.YES_OPTION):
                        
                        saveAbstractDetails();
                        mdiForm.removeFrame(CoeusGuiConstants.PROPOSAL_ABSTRACTS_FRAME_TITLE,proposalNumber);
                        this.dispose();
                        frame = mdiForm.getFrame(
                        CoeusGuiConstants.PROPOSAL_DETAILS_FRAME_TITLE, proposalNumber);
                        if(frame != null){
                            frame.setSelected(true);
                            frame.setVisible(true);
                        }
                        
                        break;
                        
                    case(JOptionPane.NO_OPTION):
                        
                        mdiForm.removeFrame(CoeusGuiConstants.PROPOSAL_ABSTRACTS_FRAME_TITLE,proposalNumber);
                        saveRequired = false;
                        this.dispose();
                        frame = mdiForm.getFrame(
                        CoeusGuiConstants.PROPOSAL_DETAILS_FRAME_TITLE,proposalNumber);
                        if(frame != null){
                            frame.setSelected(true);
                            frame.setVisible(true);
                        }
                        break;
                        
                    case ( JOptionPane.CANCEL_OPTION ):
                    case ( JOptionPane.CLOSED_OPTION ):
                        throw new PropertyVetoException(
                        coeusMessageResources.parseMessageKey(
                        "Proposal_AbsForm_exceptionCode.1130"),null);
                }
            }else{
                // Added by chandra 07/02/04 start- Dispose the window in the display mode without
                // any messages
                mdiForm.removeFrame(CoeusGuiConstants.PROPOSAL_ABSTRACTS_FRAME_TITLE,proposalNumber);
                this.dispose();
                frame = mdiForm.getFrame(
                CoeusGuiConstants.PROPOSAL_DETAILS_FRAME_TITLE,proposalNumber);
                if(frame != null){
                    frame.setSelected(true);
                    frame.setVisible(true);
                }
            }// Added by chandra 07/02/04 End
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

        pnlAbstractPanel = new javax.swing.JPanel();
        pnlTabbedPanes = new javax.swing.JPanel();
        pnlProposalSponsor = new javax.swing.JPanel();
        lblProposalNo = new javax.swing.JLabel();
        lblSponsor = new javax.swing.JLabel();
        lblProposalNoValue = new javax.swing.JLabel();
        lblSponsorValue = new javax.swing.JLabel();
        pnlUserTimeStamp = new javax.swing.JPanel();
        lblUpdateTimestamp = new javax.swing.JLabel();
        txtUpdateTimestamp = new javax.swing.JTextField();

        getContentPane().setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 0, 0));

        setMaximumSize(new java.awt.Dimension(1206, 800));
        setMinimumSize(new java.awt.Dimension(1206, 800));
        setPreferredSize(new java.awt.Dimension(1206, 800));
        pnlAbstractPanel.setLayout(new java.awt.GridBagLayout());

        pnlAbstractPanel.setMaximumSize(new java.awt.Dimension(980, 590));
        pnlAbstractPanel.setMinimumSize(new java.awt.Dimension(980, 590));
        pnlAbstractPanel.setPreferredSize(new java.awt.Dimension(980, 590));
        pnlTabbedPanes.setLayout(new java.awt.GridBagLayout());

        pnlTabbedPanes.setMinimumSize(new java.awt.Dimension(750, 560));
        pnlTabbedPanes.setPreferredSize(new java.awt.Dimension(950, 590));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 2, 0);
        pnlAbstractPanel.add(pnlTabbedPanes, gridBagConstraints);

        pnlProposalSponsor.setLayout(new java.awt.GridBagLayout());

        pnlProposalSponsor.setMaximumSize(new java.awt.Dimension(790, 60));
        pnlProposalSponsor.setMinimumSize(new java.awt.Dimension(750, 40));
        pnlProposalSponsor.setPreferredSize(new java.awt.Dimension(770, 40));
        lblProposalNo.setFont(CoeusFontFactory.getLabelFont());
        lblProposalNo.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        lblProposalNo.setText("Proposal Number: ");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        pnlProposalSponsor.add(lblProposalNo, gridBagConstraints);

        lblSponsor.setFont(CoeusFontFactory.getLabelFont());
        lblSponsor.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblSponsor.setText("Sponsor: ");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        pnlProposalSponsor.add(lblSponsor, gridBagConstraints);

        lblProposalNoValue.setFont(CoeusFontFactory.getLabelFont());
        lblProposalNoValue.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        lblProposalNoValue.setText("jLabel1");
        lblProposalNoValue.setMaximumSize(new java.awt.Dimension(640, 16));
        lblProposalNoValue.setMinimumSize(new java.awt.Dimension(640, 16));
        lblProposalNoValue.setPreferredSize(new java.awt.Dimension(640, 16));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 1.0;
        pnlProposalSponsor.add(lblProposalNoValue, gridBagConstraints);

        lblSponsorValue.setFont(CoeusFontFactory.getLabelFont());
        lblSponsorValue.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        lblSponsorValue.setText("jLabel1");
        lblSponsorValue.setMinimumSize(new java.awt.Dimension(640, 16));
        lblSponsorValue.setPreferredSize(new java.awt.Dimension(640, 16));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 1.0;
        pnlProposalSponsor.add(lblSponsorValue, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        pnlAbstractPanel.add(pnlProposalSponsor, gridBagConstraints);

        pnlUserTimeStamp.setLayout(new java.awt.GridBagLayout());

        pnlUserTimeStamp.setMaximumSize(new java.awt.Dimension(500, 40));
        pnlUserTimeStamp.setMinimumSize(new java.awt.Dimension(500, 40));
        pnlUserTimeStamp.setPreferredSize(new java.awt.Dimension(500, 40));
        lblUpdateTimestamp.setFont(CoeusFontFactory.getLabelFont());
        lblUpdateTimestamp.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblUpdateTimestamp.setText("Last Updated by :");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        pnlUserTimeStamp.add(lblUpdateTimestamp, gridBagConstraints);

        txtUpdateTimestamp.setEditable(false);
        txtUpdateTimestamp.setFont(new java.awt.Font("Microsoft Sans Serif", 0, 11));
        txtUpdateTimestamp.setMaximumSize(new java.awt.Dimension(380, 20));
        txtUpdateTimestamp.setMinimumSize(new java.awt.Dimension(380, 20));
        txtUpdateTimestamp.setPreferredSize(new java.awt.Dimension(380, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHEAST;
        pnlUserTimeStamp.add(txtUpdateTimestamp, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 0, 0, 0);
        pnlAbstractPanel.add(pnlUserTimeStamp, gridBagConstraints);

        getContentPane().add(pnlAbstractPanel);

    }// </editor-fold>//GEN-END:initComponents
    
    /** This abstract method must be implemented by all classes which inherits this class.
     * Used for saving the current activesheet when clicked Save from File menu.
     */
    public void saveActiveSheet() {
        if(!(functionType == DISPLAY_MODE)){
            saveAbstractDetails();
            getDataFromDB();
            createAbstractBean();
        }
    }
    
    /** To Capture the Toolbar button click and the menu action
     * performed for the Save and Close actions.
     * @param actionEvent Action Event object
     */
    public void actionPerformed(java.awt.event.ActionEvent actionEvent) {
        
        Object source = actionEvent.getSource();
        setDataToBean();
        if( source.equals( btnClose )){
            try {
                closeProposalAbstractForm();
            } catch(Exception e){
                e.printStackTrace();
            }
        }else if ( source.equals( btnSave )){
            saveAbstractDetails();
            
            // Added by chandra 07/02/2004 - start . the icon has to be displayed
            // as soon as the user clicks on save button.
            for(int i=0;i<tbdPnProposal.getTabCount();i++){
                
                String abstractText = ((ProposalAbstractTab) absJPanel[i]).getAbstractText();
                abstractText = ( abstractText == null ? "" : abstractText );
                
                if (abstractText!=null) {
                    if (abstractText.trim().length() > 0){
                        tbdPnProposal.setIconAt(i, new ImageIcon(getClass().getClassLoader().getResource(
                        CoeusGuiConstants.ABSTRACT_ICON)));
                    }else{
                        tbdPnProposal.setIconAt(i,null);
                    }
                }
            }
            // Added by chandra 07/02/2004 - End
            getDataFromDB();
            createAbstractBean();
            //Added for case#2349 - Update timestamp for the Abstracts Module - start                        
            setUserTimeStamp(tbdPnProposal.getSelectedIndex(), true);
            //Added for case#2349 - Update timestamp for the Abstracts Module - end            
        }
    }
    
    private void saveAbstractDetails(){
        Vector finalVector = getFormData();
        //Added for case#2349 - Update timestamp for the Abstracts Module
        getLatestTimeStamp(finalVector);        
        updDataToDb(finalVector);
        
    }
    
    /** The method used to get the save status of the ProposalAbstractForm
     * @return save boolean true if there are any unsaved modifications,
     * else false.
     */
    protected boolean isSaveRequired() {
        return saveRequired;
    }
    
    /** Getter for property proposalNumber.
     * @return Value of property proposalNumber.
     */
    public java.lang.String getProposalNumber() {
        return proposalNumber;
    }
    
    /** Setter for property proposalNumber.
     * @param proposalNumber New value of property proposalNumber.
     */
    public void setProposalNumber(java.lang.String proposalNumber) {
        this.proposalNumber = proposalNumber;
    }
    
    /** Getter for property title.
     * @return Value of property title.
     */
    public java.lang.String getTitle() {
        return title;
    }
    
    /** Setter for property title.
     * @param title New value of property title.
     */
    public void setTitle(java.lang.String title) {
        this.title = title;
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
    
    /** Getter for property sponsorCodeName.
     * @return Value of property sponsorCodeName.
     */
    public String getSponsorCodeName() {
        return sponsorCodeName;
    }
    
    /** Setter for property sponsorCodeName.
     * @param sponsorCodeName New value of property sponsorCodeName.
     */
    public void setSponsorCodeName(String sponsorCodeName) {
        this.sponsorCodeName = sponsorCodeName;
    }
    
    public void saveAsActiveSheet() {
    }
    
    class ChangeTab implements ChangeListener{
        
        /** This method is coded to add/remove the Sigma Icon to the
         * tabs in the tabbedpane based on the event of the tab being
         * selected and then Deselected.
         * @param e Event Object
         */
        public void stateChanged(ChangeEvent e){
            tbdPnProposal.validate();
            javax.swing.JTextArea txtArea ;
            
            for(int i=0;i<tbdPnProposal.getTabCount();i++){
                txtArea = ((ProposalAbstractTab) absJPanel[i]).getTextArea();
                
                //Commented since the bug -- focus was not set to Text Area 
                //                txtArea.requestFocusInWindow();
                //                txtArea.setCaretPosition(0);
                
                String abstractText = ((ProposalAbstractTab) absJPanel[i]).getAbstractText();
                abstractText = ( abstractText == null ? "" : abstractText );
                if (abstractText!=null) {
                    if (abstractText.trim().length() > 0){
                        tbdPnProposal.setIconAt(i, new ImageIcon(getClass().getClassLoader().getResource(
                        CoeusGuiConstants.ABSTRACT_ICON)));
                    }else{
                        saveRequired = false;
                        tbdPnProposal.setIconAt(i,null);
                    }
                }else{
                    saveRequired = false;
                }
                
            }//End For        
            
            //Added for case#2349 - Update timestamp for the Abstracts Module - start
            setUserTimeStamp(tbdPnProposal.getSelectedIndex(), false);
            //Added for case#2349 - Update timestamp for the Abstracts Module - end            
            
            //Added Ajay to set the focus to TextArea: Start 24-08-2004
            SwingUtilities.invokeLater(new Runnable() {
                public void run()  {
                    javax.swing.JTextArea txtArea ;
                    int selIndex = tbdPnProposal.getSelectedIndex();
                    txtArea = ((ProposalAbstractTab) absJPanel[selIndex]).getTextArea();
                    txtArea.requestFocusInWindow();
                }
            });
            //Added Ajay to set the focus to TextArea Start 24-08-2004                    
        }
    }
    
    //Added for case#2349 - Update timestamp for the Abstracts Module - start
    /**
     * This method sets the UpdateTimestamp and UpdateUser information based on the selected tab
     * @param selectedTabIndex int
     * @param afterSave boolean
     */
    private void setUserTimeStamp(int selectedTabIndex, boolean afterSave){
        Vector vecAbstracts = null;
        if(afterSave){
            vecAbstracts = (Vector)vecDBData.get(1);
            replaceChangedBeans(vecAbstracts, vAbstractFinalList);
        }
        for(int index = 0; index < vAbstractFinalList.size(); index++){
            if(index == selectedTabIndex){
                ProposalAbstractFormBean proposalAbstractFormBean = 
                        (ProposalAbstractFormBean)vAbstractFinalList.get(index);
                if(proposalAbstractFormBean.getUpdateTimestamp() != null 
                        && !proposalAbstractFormBean.getUpdateTimestamp().equals("")){
                    
                    txtUpdateTimestamp.setText(
                            UserUtils.getDisplayName(proposalAbstractFormBean.getUpdateUser())
                            +" at "
                            +CoeusDateFormat.format(proposalAbstractFormBean.getUpdateTimestamp().toString()));
                }else{
                    txtUpdateTimestamp.setText("");
                }                
                break;
            }
        }
    }
    
    /**
     * This method replaces the changed beans in the collection vAbstractFinalList
     * @param vecAbstracts Vector
     * @param vAbstractFinalList Vector
     */
    private void replaceChangedBeans(Vector vecAbstracts, Vector vAbstractFinalList){
        ProposalAbstractFormBean proposalAbstractFormBean1 = null;
        ProposalAbstractFormBean proposalAbstractFormBean2 = null;
        if(vecAbstracts != null && vecAbstracts.size() > 0){
            for(int index = 0; index < vecAbstracts.size(); index++){
                proposalAbstractFormBean1 = (ProposalAbstractFormBean)vecAbstracts.get(index);
                for(int index1 = 0; index1 < vAbstractFinalList.size(); index1++){
                    proposalAbstractFormBean2 = (ProposalAbstractFormBean)vAbstractFinalList.get(index1);
                    if(proposalAbstractFormBean1.getAbstractTypeCode() == proposalAbstractFormBean2.getAbstractTypeCode()){
                        vAbstractFinalList.remove(index1);
                        vAbstractFinalList.add(index1, proposalAbstractFormBean1);
                        break;
                    }
                }            
            }   
        }
        for(int index = 0; index < vAbstractFinalList.size(); index++){
            proposalAbstractFormBean2 = (ProposalAbstractFormBean)vAbstractFinalList.get(index);
            if(proposalAbstractFormBean2.getAbstractText() == null){
                proposalAbstractFormBean2.setUpdateTimestamp(null);
                proposalAbstractFormBean2.setUpdateUser(null);
                vAbstractFinalList.remove(index);
                vAbstractFinalList.add(index, proposalAbstractFormBean2);                
            }
        }        
    }
    
    /**
     * This method sets the latest timestamp to the beans that are sent to db
     * @param finalVector Vector     
     */
    private void getLatestTimeStamp(Vector finalVector){
        Vector vecAbstracts = (Vector)vecDBData.get(1);
        ProposalAbstractFormBean proposalAbstractFormBean1 = null;
        ProposalAbstractFormBean proposalAbstractFormBean2 = null;
        if(finalVector != null && vecAbstracts != null){
            for(int index = 0; index < finalVector.size(); index++){
                proposalAbstractFormBean1 = (ProposalAbstractFormBean)finalVector.get(index);
                for(int index1 = 0; index1 < vecAbstracts.size(); index1++){
                    proposalAbstractFormBean2 = (ProposalAbstractFormBean)vecAbstracts.get(index1);
                    if(proposalAbstractFormBean1.getAbstractTypeCode() == proposalAbstractFormBean2.getAbstractTypeCode()){
                        proposalAbstractFormBean1.setUpdateTimestamp(proposalAbstractFormBean2.getUpdateTimestamp());
                        proposalAbstractFormBean1.setUpdateUser(proposalAbstractFormBean2.getUpdateUser());
                        break;
                    }                    
                }
                finalVector.remove(index);
                finalVector.add(index, proposalAbstractFormBean1);                
            }
        }        
    }
    //Added for case#2349 - Update timestamp for the Abstracts Module - end
    
    //
    // Variables declaration - do not modify//GEN-BEGIN:variables
    public javax.swing.JLabel lblProposalNo;
    public javax.swing.JLabel lblProposalNoValue;
    public javax.swing.JLabel lblSponsor;
    public javax.swing.JLabel lblSponsorValue;
    public javax.swing.JLabel lblUpdateTimestamp;
    public javax.swing.JPanel pnlAbstractPanel;
    public javax.swing.JPanel pnlProposalSponsor;
    public javax.swing.JPanel pnlTabbedPanes;
    public javax.swing.JPanel pnlUserTimeStamp;
    public javax.swing.JTextField txtUpdateTimestamp;
    // End of variables declaration//GEN-END:variables
    
}