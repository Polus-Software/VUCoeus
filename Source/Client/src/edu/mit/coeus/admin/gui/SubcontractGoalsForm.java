/*
 * SubcontractGoalsForm.java
 *
 * Created on January 4, 2005, 3:27 PM
 */

package edu.mit.coeus.admin.gui;

import edu.mit.coeus.gui.CoeusAppletMDIForm;
import edu.mit.coeus.gui.CoeusFontFactory;
import edu.mit.coeus.award.bean.SubcontactExpenditureBean;
import edu.mit.coeus.award.bean.SubcontactingBudgetBean;
import edu.mit.coeus.brokers.RequesterBean;
import edu.mit.coeus.brokers.ResponderBean;
import edu.mit.coeus.exception.CoeusException;
import edu.mit.coeus.gui.CoeusDlgWindow;
import edu.mit.coeus.gui.CoeusMessageResources;

import edu.mit.coeus.utils.AppletServletCommunicator;
import edu.mit.coeus.utils.CoeusGuiConstants;
import edu.mit.coeus.utils.CoeusOptionPane;
import edu.mit.coeus.utils.LimitedPlainDocument;
import edu.mit.coeus.utils.ScreenFocusTraversalPolicy;
import edu.mit.coeus.utils.TypeConstants;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;

import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Hashtable;
import javax.swing.AbstractAction;
import javax.swing.JComponent;

/**
 *
 * @author  chandrashekara
 */
public class SubcontractGoalsForm extends javax.swing.JComponent implements 
    ActionListener{
    
    private static final String EMPTY_STRING = "";
    //coeusdev-1081 start
//    private static final int WIDTH = 400;
//    private static final int HEIGHT = 410;
    private static final int WIDTH = 450;
    private static final int HEIGHT = 500;
    //coeusdev-1081 end
    private CoeusAppletMDIForm mdiForm;
    private SubcontactExpenditureBean subcontactExpenditureBean;
    private SubcontactingBudgetBean subcontactingBudgetBean;
    private CoeusDlgWindow dlgGoals;
    private static final String WINDOW_TITLE = "Subcontracting Goals";
    private boolean modified;
    private static final char SAVE_GOALS_DATA  = 'H';
    private static final String GET_SERVLET = "/SubcontractMaintenenceServlet";
    private static final String connect = CoeusGuiConstants.CONNECTION_URL + GET_SERVLET;
    private CoeusMessageResources coeusMessageResources;
    private String awardNumber;
    /** Creates new form SubcontractGoalsForm */
    public SubcontractGoalsForm(CoeusAppletMDIForm mdiForm,String awardNumber) {
        this.mdiForm = mdiForm;
        this.awardNumber = awardNumber;
        coeusMessageResources = CoeusMessageResources.getInstance();
        initComponents();
        registerComponents();
        postInitComponents();
    }
    
    private void registerComponents(){
        txtExHBCU.setEditable(false);
        txtExHubZone.setEditable(false);
        txtExLargeBusiness.setEditable(false);
        txtExSDB.setEditable(false);
        txtExSmallBusiness.setEditable(false);
        txtExTotal.setEditable(false);
        txtExVeteran.setEditable(false);
        txtExSDVet.setEditable(false);
        txtExWomanOwned.setEditable(false);
        txtTotal.setEditable(false);
        //coeusdev-1081 start
        txtExANCCBSBA.setEditable(false);
        txtExANCSB.setEditable(false);
        //coeusdev-1081 end
        txtExTotal.setFont(CoeusFontFactory.getLabelFont());
        
        btnCancel.addActionListener(this);
        btnOK.addActionListener(this);
        
        txtLargeBusiness.addFocusListener(new CustomFocusAdapter());
        txtSmallBusiness.addFocusListener(new CustomFocusAdapter());
        txtTotal.addFocusListener(new CustomFocusAdapter());
        
        //coeusdev-1081 start
//        java.awt.Component[] components = {txtLargeBusiness,txtSmallBusiness,
//        txtWoman,txtSDB,txtHubZone,txtHBCU,txtVeran,txtSDVet,txtArComments,btnOK,btnCancel};  
        java.awt.Component[] components = {txtLargeBusiness,txtSmallBusiness,
        txtWoman,txtSDB,txtHubZone,txtHBCU,txtVeran,txtSDVet,txtANCCBSBA,txtANCSB,txtArComments,btnOK,btnCancel};
        //coeusdev-1081 end

        ScreenFocusTraversalPolicy traversePolicy = new ScreenFocusTraversalPolicy( components );
        txtArComments.setDocument(new LimitedPlainDocument(500));
        
        setFocusTraversalPolicy(traversePolicy);
        setFocusCycleRoot(true);
    }
    
    public void setFormData(Hashtable data) {
        subcontactingBudgetBean = (SubcontactingBudgetBean)data.get(SubcontactingBudgetBean.class);
        subcontactExpenditureBean = (SubcontactExpenditureBean)data.get(SubcontactExpenditureBean.class);
        if(subcontactingBudgetBean!= null){
            setBudgetData(subcontactingBudgetBean);
        }
        if(subcontactExpenditureBean!= null){
            setExpenditureData(subcontactExpenditureBean);
        }
        if(subcontactingBudgetBean.getComments()!= null){
            txtArComments.setText(subcontactingBudgetBean.getComments().trim());
        }
        lblHeader.setText("Budgeted Amounts for Award:  "+awardNumber);
    }
    
    private void setBudgetData(SubcontactingBudgetBean bean){
        txtHBCU.setValue(bean.getHBCUGoal());
        txtHubZone.setValue(bean.getHubZoneGoal());
        txtLargeBusiness.setValue(bean.getLargeBusinessGoal());
        txtSDB.setValue(bean.getSDBGoal());
        txtSmallBusiness.setValue(bean.getSmallBusinessGoal());
        txtTotal.setValue(bean.getLargeBusinessGoal() + bean.getSmallBusinessGoal());
        txtVeran.setValue(bean.getVeterenOwnedGoal());
        txtWoman.setValue(bean.getWomanOwnedGoal());
        txtSDVet.setValue(bean.getSDVetOwnedGoal());
        //coeusdev-1081 start
        txtANCCBSBA.setValue(bean.getAncitNoCbsbaGoal());
        txtANCSB.setValue(bean.getAncitNoSbGoal());
        //coeusdev-1081 end

    }
    
    private void setExpenditureData(SubcontactExpenditureBean bean){
        txtExHBCU.setValue(bean.getHistoricalBlackCollegeAmt());
        txtExHubZone.setValue(bean.getHubZoneGoal());
        txtExLargeBusiness.setValue(bean.getLargeBusinessGoal());
//        txtExSDB.setValue(bean.getSDBGoal());
        txtExSDB.setValue(bean.getA8DisadvantageAmt());
        txtExSmallBusiness.setValue(bean.getSmallBusinessGoal());
        txtExTotal.setValue(bean.getLargeBusinessGoal()+bean.getSmallBusinessGoal());
        txtExVeteran.setValue(bean.getVeterenOwnedGoal());
        txtExWomanOwned.setValue(bean.getWomanOwnedGoal());
        txtExSDVet.setValue(bean.getServiceDisabledVetOwnedAmt());
        //coeusdev-1081 start
        txtExANCCBSBA.setValue(bean.getAncitNoCbsbaGoal());
        txtExANCSB.setValue(bean.getAncitNoSbGoal());
       //coeusdev-1081 end

    }
    public void actionPerformed(java.awt.event.ActionEvent actionEvent) {
        Object source  = actionEvent.getSource();
        try{
            dlgGoals.setCursor(new Cursor(Cursor.WAIT_CURSOR));
            if(source.equals(btnCancel)){
                performCloseAction();
            }else if(source.equals(btnOK)){
                performOKAction();
            }
        }catch (CoeusException coeusException){
            CoeusOptionPane.showErrorDialog(coeusException.getMessage());
        }
        finally{
            dlgGoals.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
        }
    }
    
     // Initialize and create the dialog box and set its properticies
    private void postInitComponents() {
        dlgGoals = new CoeusDlgWindow(mdiForm,true);
        dlgGoals.setResizable(false);
        dlgGoals.setModal(true);
        dlgGoals.getContentPane().add(this);
        dlgGoals.setFont(CoeusFontFactory.getLabelFont());
        dlgGoals.setDefaultCloseOperation(CoeusDlgWindow.DO_NOTHING_ON_CLOSE);
        dlgGoals.setSize(WIDTH, HEIGHT);
        dlgGoals.setTitle(WINDOW_TITLE);
        // This method will place the dialog box at the center of the screen
        dlgGoals.setLocationRelativeTo(mdiForm);
        
//        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
//        Dimension dlgSize = dlgGoals.getSize();
//        dlgGoals.setLocation(screenSize.width/2 - (dlgSize.width/2),
//        screenSize.height/2 - (dlgSize.height/2));
        
        dlgGoals.addWindowListener(new WindowAdapter(){
            
            public void windowClosing(WindowEvent we) {
                try{
                    performCloseAction();
                    return;
                }catch (CoeusException coeusException){
                    CoeusOptionPane.showErrorDialog(coeusException.getMessage());
                }
            }
        });
        
        dlgGoals.addEscapeKeyListener(
        new AbstractAction("escPressed"){
            public void actionPerformed(ActionEvent ae) {
                try{
                    performCloseAction();
                    return;
                }catch (CoeusException coeusException){
                    CoeusOptionPane.showErrorDialog(coeusException.getMessage());
                }
            }
        });
        
        dlgGoals.addComponentListener(
        new ComponentAdapter(){
            public void componentShown(ComponentEvent e){
                setWindowFocus();
            }
        });
    }
    
    private void performOKAction() throws CoeusException{
        if(isDataChanged()){
            saveFormData();
        }
        dlgGoals.setVisible(false);
    }
    
    private void performCloseAction() throws CoeusException{
        if(isDataChanged()){
            int option = CoeusOptionPane.showQuestionDialog(
            coeusMessageResources.parseMessageKey("saveConfirmCode.1002"),
            CoeusOptionPane.OPTION_YES_NO_CANCEL,CoeusOptionPane.DEFAULT_CANCEL);
            if(option == CoeusOptionPane.SELECTION_YES){
                saveFormData();
                dlgGoals.setVisible(false);
            }else if(option == CoeusOptionPane.SELECTION_NO){
                dlgGoals.setVisible(false);
            }else if(option==CoeusOptionPane.SELECTION_CANCEL){
                return;
            }
        }else{
            dlgGoals.setVisible(false);
        }
    }
    
    private void setWindowFocus(){
        txtArComments.setCaretPosition(0);
        txtLargeBusiness.requestFocusInWindow();
    }
    
    public void display(){
        dlgGoals.setVisible(true);
    }
    
    private void saveFormData() throws CoeusException{
        subcontactingBudgetBean.setComments(txtArComments.getText().trim());
        subcontactingBudgetBean.setHBCUGoal(Double.parseDouble(txtHBCU.getValue()));
        subcontactingBudgetBean.setHubZoneGoal(Double.parseDouble(txtHubZone.getValue()));
        subcontactingBudgetBean.setLargeBusinessGoal(Double.parseDouble(txtLargeBusiness.getValue()));
        subcontactingBudgetBean.setSDBGoal(Double.parseDouble(txtSDB.getValue()));
        subcontactingBudgetBean.setSmallBusinessGoal(Double.parseDouble(txtSmallBusiness.getValue()));
        subcontactingBudgetBean.setVeterenOwnedGoal(Double.parseDouble(txtVeran.getValue()));
        subcontactingBudgetBean.setWomanOwnedGoal(Double.parseDouble(txtWoman.getValue()));
        subcontactingBudgetBean.setSDVetOwnedGoal(Double.parseDouble(txtSDVet.getValue()));
        //coeusdev-1081 start
        subcontactingBudgetBean.setAncitNoCbsbaGoal(Double.parseDouble(txtANCCBSBA.getValue()));
        subcontactingBudgetBean.setAncitNoSbGoal(Double.parseDouble(txtANCSB.getValue()));
        //coeusdev-1081 end
        if(subcontactingBudgetBean.getMitAwardNumber()==null){
            subcontactingBudgetBean.setAcType(TypeConstants.INSERT_RECORD);
        }else{
            subcontactingBudgetBean.setAcType(TypeConstants.UPDATE_RECORD);
        }
        subcontactingBudgetBean.setMitAwardNumber(awardNumber);
        saveGoalsData(subcontactingBudgetBean);
    }
    
    private void saveGoalsData(SubcontactingBudgetBean subcontactingBudgetBean)
    throws CoeusException{
        RequesterBean requester;
        ResponderBean responder;
        requester = new RequesterBean();
        requester.setFunctionType(SAVE_GOALS_DATA);
        requester.setDataObject(subcontactingBudgetBean);
        AppletServletCommunicator comm
        = new AppletServletCommunicator(connect, requester);
        
        comm.send();
        responder = comm.getResponse();
        if(responder!= null){
            if(responder.isSuccessfulResponse()){
            }else{
                throw new CoeusException(responder.getMessage(),0);
            }
        }
    }
    
    public boolean isDataChanged(){
        double value = 0.0;
        double textValue = 0.0;
        // Check for data chandge in LargeBusinees Value
        value = subcontactingBudgetBean.getLargeBusinessGoal();
        textValue= Double.parseDouble(txtLargeBusiness.getValue());
        if(value!= 0.0) {
            if(textValue!=value){
                modified = true;
                return true;
            }
        }else if(textValue>0.0){
            modified = true;
            return true;
        }
        
        // Check for data chandge in smallBusiness Value
        value = subcontactingBudgetBean.getSmallBusinessGoal();
        textValue= Double.parseDouble(txtSmallBusiness.getValue());
        if(value!= 0.0) {
            if(textValue!=value){
                modified = true;
                return true;
            }
        }else if(textValue>0.0){
            modified = true;
            return true;
        }
        
        // Check for data chandge in HBCU Value
        value = subcontactingBudgetBean.getHBCUGoal();
        textValue= Double.parseDouble(txtHBCU.getValue());
        if(value!= 0.0) {
            if(textValue!=value){
                modified = true;
                return true;
            }
        }else if(textValue>0.0){
            modified = true;
            return true;
        }
        
        // Check for data chandge in HubZoneGoal Value
        value = subcontactingBudgetBean.getHubZoneGoal();
        textValue= Double.parseDouble(txtHubZone.getValue());
        if(value!= 0.0) {
            if(textValue!=value){
                modified = true;
                return true;
            }
        }else if(textValue>0.0){
            modified = true;
            return true;
        }
        
        // Check for data chandge in SDBGoal Value
        value = subcontactingBudgetBean.getSDBGoal();
        textValue= Double.parseDouble(txtSDB.getValue());
        if(value!= 0.0) {
            if(textValue!=value){
                modified = true;
                return true;
            }
        }else if(textValue>0.0){
            modified = true;
            return true;
        }
        
        // Check for data chandge in VeterenOwnedGoal Value
        value = subcontactingBudgetBean.getVeterenOwnedGoal();
        textValue= Double.parseDouble(txtVeran.getValue());
        if(value!= 0.0) {
            if(textValue!=value){
                modified = true;
                return true;
            }
        }else if(textValue>0.0){
            modified = true;
            return true;
        }
        
        // Check for data chandge in SDvetOwnedGoal Value
        value = subcontactingBudgetBean.getSDVetOwnedGoal();
        textValue= Double.parseDouble(txtSDVet.getValue());
        if(value!= 0.0) {
            if(textValue!=value){
                modified = true;
                return true;
            }
        }else if(textValue>0.0){
            modified = true;
            return true;
        }
        
        // Check for data chandge in WomanOwnedGoal Value
        value = subcontactingBudgetBean.getWomanOwnedGoal();
        textValue= Double.parseDouble(txtWoman.getValue());
        if(value!= 0.0) {
            if(textValue!=value){
                modified = true;
                return true;
            }
        }else if(textValue>0.0){
            modified = true;
            return true;
        }
        //coeusdev-1081 start
        value = subcontactingBudgetBean.getAncitNoCbsbaGoal();
        textValue= Double.parseDouble(txtANCCBSBA.getValue());
        if(value!= 0.0) {
            if(textValue!=value){
                modified = true;
                return true;
            }
        }else if(textValue>0.0){
            modified = true;
            return true;
        }

        value = subcontactingBudgetBean.getAncitNoSbGoal();
        textValue= Double.parseDouble(txtANCSB.getValue());
        if(value!= 0.0) {
            if(textValue!=value){
                modified = true;
                return true;
            }
        }else if(textValue>0.0){
            modified = true;
            return true;
        }
        //coeusdev-1081 end

        // Check whether comments are changed or not.
        if((subcontactingBudgetBean.getComments()!= null) && !subcontactingBudgetBean.getComments().trim().equals(EMPTY_STRING)) {
            if(!txtArComments.getText().trim().equals(subcontactingBudgetBean.getComments().trim())){
                modified = true;
                return true;
            }
        }else if(txtArComments.getText().trim().length()>0){
            modified = true;
            return true;
        }
        
        return modified;
    }
    
    
    public class CustomFocusAdapter extends FocusAdapter{
        public void focusGained(FocusEvent focusEvent) {
            if(focusEvent.isTemporary()) return ;
        }
        
        public void focusLost(FocusEvent focusEvent) {
            if(focusEvent.isTemporary()) return ;
            Object source = focusEvent.getSource();
            if(source.equals(txtLargeBusiness)){
                double cost = new Double(txtLargeBusiness.getValue()).doubleValue();
                cost = cost + new Double(txtSmallBusiness.getValue()).doubleValue();
                txtTotal.setValue(cost);
            }else if(source.equals(txtSmallBusiness)){
                double cost = new Double(txtSmallBusiness.getValue()).doubleValue();
                cost = cost + new Double(txtLargeBusiness.getValue()).doubleValue();
                txtTotal.setValue(cost);
            }
        }
    }
    

    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        lblHeader = new javax.swing.JLabel();
        lblGoals = new javax.swing.JLabel();
        lblExpenditure = new javax.swing.JLabel();
        lblLargeBusiness = new javax.swing.JLabel();
        txtLargeBusiness = new edu.mit.coeus.utils.DollarCurrencyTextField();
        lblSmallBusiness = new javax.swing.JLabel();
        lblTotal = new javax.swing.JLabel();
        txtSmallBusiness = new edu.mit.coeus.utils.DollarCurrencyTextField();
        txtTotal = new edu.mit.coeus.utils.DollarCurrencyTextField();
        lblWoman = new javax.swing.JLabel();
        txtWoman = new edu.mit.coeus.utils.DollarCurrencyTextField();
        lblSBD = new javax.swing.JLabel();
        txtSDB = new edu.mit.coeus.utils.DollarCurrencyTextField();
        lblHubZone = new javax.swing.JLabel();
        txtHubZone = new edu.mit.coeus.utils.DollarCurrencyTextField();
        lblHBCU = new javax.swing.JLabel();
        txtHBCU = new edu.mit.coeus.utils.DollarCurrencyTextField();
        lblVeteranOwned = new javax.swing.JLabel();
        txtVeran = new edu.mit.coeus.utils.DollarCurrencyTextField();
        lblSDVet = new javax.swing.JLabel();
        txtSDVet = new edu.mit.coeus.utils.DollarCurrencyTextField();
        scrPnComments = new javax.swing.JScrollPane();
        txtArComments = new javax.swing.JTextArea();
        txtExLargeBusiness = new edu.mit.coeus.utils.DollarCurrencyTextField();
        txtExSmallBusiness = new edu.mit.coeus.utils.DollarCurrencyTextField();
        txtExTotal = new edu.mit.coeus.utils.DollarCurrencyTextField();
        txtExWomanOwned = new edu.mit.coeus.utils.DollarCurrencyTextField();
        txtExSDB = new edu.mit.coeus.utils.DollarCurrencyTextField();
        txtExHubZone = new edu.mit.coeus.utils.DollarCurrencyTextField();
        txtExHBCU = new edu.mit.coeus.utils.DollarCurrencyTextField();
        txtExVeteran = new edu.mit.coeus.utils.DollarCurrencyTextField();
        txtExSDVet = new edu.mit.coeus.utils.DollarCurrencyTextField();
        btnOK = new javax.swing.JButton();
        btnCancel = new javax.swing.JButton();
        lblComment = new javax.swing.JLabel();
        lblANCCBSBA = new javax.swing.JLabel();
        lblANCSB = new javax.swing.JLabel();
        txtANCCBSBA = new edu.mit.coeus.utils.DollarCurrencyTextField();
        txtExANCCBSBA = new edu.mit.coeus.utils.DollarCurrencyTextField();
        txtANCSB = new edu.mit.coeus.utils.DollarCurrencyTextField();
        txtExANCSB = new edu.mit.coeus.utils.DollarCurrencyTextField();

        setFocusTraversalPolicyProvider(true);
        setPreferredSize(new java.awt.Dimension(450, 500));
        setLayout(new java.awt.GridBagLayout());

        lblHeader.setFont(CoeusFontFactory.getLabelFont());
        lblHeader.setText("jLabel1");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 6, 24);
        add(lblHeader, gridBagConstraints);

        lblGoals.setFont(CoeusFontFactory.getLabelFont());
        lblGoals.setText("Goals");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTH;
        gridBagConstraints.insets = new java.awt.Insets(10, 0, 0, 0);
        add(lblGoals, gridBagConstraints);

        lblExpenditure.setFont(CoeusFontFactory.getLabelFont());
        lblExpenditure.setText("Expenditure");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTH;
        gridBagConstraints.insets = new java.awt.Insets(10, 8, 0, 0);
        add(lblExpenditure, gridBagConstraints);

        lblLargeBusiness.setFont(CoeusFontFactory.getLabelFont());
        lblLargeBusiness.setText("Large Business:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHEAST;
        gridBagConstraints.insets = new java.awt.Insets(10, 3, 0, 0);
        add(lblLargeBusiness, gridBagConstraints);

        txtLargeBusiness.setFont(CoeusFontFactory.getNormalFont());
        txtLargeBusiness.setMinimumSize(new java.awt.Dimension(100, 20));
        txtLargeBusiness.setPreferredSize(new java.awt.Dimension(100, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(10, 3, 4, 8);
        add(txtLargeBusiness, gridBagConstraints);

        lblSmallBusiness.setFont(CoeusFontFactory.getLabelFont());
        lblSmallBusiness.setText("Small Business:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHEAST;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 0, 0);
        add(lblSmallBusiness, gridBagConstraints);

        lblTotal.setFont(CoeusFontFactory.getLabelFont());
        lblTotal.setText("Total:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHEAST;
        gridBagConstraints.insets = new java.awt.Insets(6, 12, 0, 0);
        add(lblTotal, gridBagConstraints);

        txtSmallBusiness.setFont(CoeusFontFactory.getNormalFont());
        txtSmallBusiness.setMinimumSize(new java.awt.Dimension(100, 20));
        txtSmallBusiness.setPreferredSize(new java.awt.Dimension(100, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 0, 0);
        add(txtSmallBusiness, gridBagConstraints);

        txtTotal.setFont(CoeusFontFactory.getLabelFont());
        txtTotal.setMinimumSize(new java.awt.Dimension(100, 20));
        txtTotal.setPreferredSize(new java.awt.Dimension(100, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 0, 0);
        add(txtTotal, gridBagConstraints);

        lblWoman.setFont(CoeusFontFactory.getLabelFont());
        lblWoman.setText("Woman Owned:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHEAST;
        gridBagConstraints.insets = new java.awt.Insets(6, 3, 0, 0);
        add(lblWoman, gridBagConstraints);

        txtWoman.setFont(CoeusFontFactory.getNormalFont());
        txtWoman.setMinimumSize(new java.awt.Dimension(100, 20));
        txtWoman.setPreferredSize(new java.awt.Dimension(100, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(6, 3, 0, 0);
        add(txtWoman, gridBagConstraints);

        lblSBD.setFont(CoeusFontFactory.getLabelFont());
        lblSBD.setText("SDB:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHEAST;
        gridBagConstraints.insets = new java.awt.Insets(6, 3, 0, 0);
        add(lblSBD, gridBagConstraints);

        txtSDB.setMinimumSize(new java.awt.Dimension(100, 20));
        txtSDB.setPreferredSize(new java.awt.Dimension(100, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(6, 3, 0, 0);
        add(txtSDB, gridBagConstraints);

        lblHubZone.setFont(CoeusFontFactory.getLabelFont());
        lblHubZone.setText("Hub Zone:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHEAST;
        gridBagConstraints.insets = new java.awt.Insets(6, 3, 0, 0);
        add(lblHubZone, gridBagConstraints);

        txtHubZone.setFont(CoeusFontFactory.getNormalFont());
        txtHubZone.setMinimumSize(new java.awt.Dimension(100, 20));
        txtHubZone.setPreferredSize(new java.awt.Dimension(100, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(6, 3, 0, 0);
        add(txtHubZone, gridBagConstraints);

        lblHBCU.setFont(CoeusFontFactory.getLabelFont());
        lblHBCU.setText("HBCU:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHEAST;
        gridBagConstraints.insets = new java.awt.Insets(6, 3, 0, 0);
        add(lblHBCU, gridBagConstraints);

        txtHBCU.setFont(CoeusFontFactory.getNormalFont());
        txtHBCU.setMinimumSize(new java.awt.Dimension(100, 20));
        txtHBCU.setPreferredSize(new java.awt.Dimension(100, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(6, 3, 0, 0);
        add(txtHBCU, gridBagConstraints);

        lblVeteranOwned.setFont(CoeusFontFactory.getLabelFont());
        lblVeteranOwned.setText("Veteran Owned:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 9;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHEAST;
        gridBagConstraints.insets = new java.awt.Insets(6, 3, 0, 0);
        add(lblVeteranOwned, gridBagConstraints);

        txtVeran.setFont(CoeusFontFactory.getNormalFont());
        txtVeran.setMinimumSize(new java.awt.Dimension(100, 20));
        txtVeran.setPreferredSize(new java.awt.Dimension(100, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 9;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(6, 3, 0, 0);
        add(txtVeran, gridBagConstraints);

        lblSDVet.setFont(CoeusFontFactory.getLabelFont());
        lblSDVet.setText("SD Vet Owned:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 10;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHEAST;
        gridBagConstraints.insets = new java.awt.Insets(6, 3, 0, 0);
        add(lblSDVet, gridBagConstraints);

        txtSDVet.setFont(CoeusFontFactory.getNormalFont());
        txtSDVet.setMinimumSize(new java.awt.Dimension(100, 20));
        txtSDVet.setPreferredSize(new java.awt.Dimension(100, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 10;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(6, 3, 0, 0);
        add(txtSDVet, gridBagConstraints);

        scrPnComments.setMinimumSize(new java.awt.Dimension(305, 100));
        scrPnComments.setPreferredSize(new java.awt.Dimension(280, 100));

        txtArComments.setFont(CoeusFontFactory.getNormalFont());
        txtArComments.setLineWrap(true);
        txtArComments.setWrapStyleWord(true);
        scrPnComments.setViewportView(txtArComments);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 14;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        add(scrPnComments, gridBagConstraints);

        txtExLargeBusiness.setFont(CoeusFontFactory.getNormalFont());
        txtExLargeBusiness.setMinimumSize(new java.awt.Dimension(100, 20));
        txtExLargeBusiness.setPreferredSize(new java.awt.Dimension(100, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(10, 7, 0, 0);
        add(txtExLargeBusiness, gridBagConstraints);

        txtExSmallBusiness.setFont(CoeusFontFactory.getNormalFont());
        txtExSmallBusiness.setMinimumSize(new java.awt.Dimension(100, 20));
        txtExSmallBusiness.setPreferredSize(new java.awt.Dimension(100, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(3, 7, 0, 0);
        add(txtExSmallBusiness, gridBagConstraints);

        txtExTotal.setFont(CoeusFontFactory.getNormalFont());
        txtExTotal.setMinimumSize(new java.awt.Dimension(100, 20));
        txtExTotal.setPreferredSize(new java.awt.Dimension(100, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(3, 7, 0, 0);
        add(txtExTotal, gridBagConstraints);

        txtExWomanOwned.setFont(CoeusFontFactory.getNormalFont());
        txtExWomanOwned.setMinimumSize(new java.awt.Dimension(100, 20));
        txtExWomanOwned.setPreferredSize(new java.awt.Dimension(100, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(6, 7, 0, 0);
        add(txtExWomanOwned, gridBagConstraints);

        txtExSDB.setFont(CoeusFontFactory.getNormalFont());
        txtExSDB.setMinimumSize(new java.awt.Dimension(100, 20));
        txtExSDB.setPreferredSize(new java.awt.Dimension(100, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(6, 7, 0, 0);
        add(txtExSDB, gridBagConstraints);

        txtExHubZone.setFont(CoeusFontFactory.getNormalFont());
        txtExHubZone.setMinimumSize(new java.awt.Dimension(100, 20));
        txtExHubZone.setPreferredSize(new java.awt.Dimension(100, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(6, 7, 0, 0);
        add(txtExHubZone, gridBagConstraints);

        txtExHBCU.setFont(CoeusFontFactory.getNormalFont());
        txtExHBCU.setMinimumSize(new java.awt.Dimension(100, 20));
        txtExHBCU.setPreferredSize(new java.awt.Dimension(100, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(6, 7, 0, 0);
        add(txtExHBCU, gridBagConstraints);

        txtExVeteran.setFont(CoeusFontFactory.getNormalFont());
        txtExVeteran.setMinimumSize(new java.awt.Dimension(100, 20));
        txtExVeteran.setPreferredSize(new java.awt.Dimension(100, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 9;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(6, 7, 0, 0);
        add(txtExVeteran, gridBagConstraints);

        txtExSDVet.setFont(CoeusFontFactory.getNormalFont());
        txtExSDVet.setMinimumSize(new java.awt.Dimension(100, 20));
        txtExSDVet.setPreferredSize(new java.awt.Dimension(100, 20));
        txtExSDVet.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtExSDVetActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 10;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(6, 7, 0, 0);
        add(txtExSDVet, gridBagConstraints);

        btnOK.setFont(CoeusFontFactory.getLabelFont());
        btnOK.setMnemonic('O');
        btnOK.setText("OK");
        btnOK.setMinimumSize(new java.awt.Dimension(73, 26));
        btnOK.setPreferredSize(new java.awt.Dimension(73, 26));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(11, 6, 0, 4);
        add(btnOK, gridBagConstraints);

        btnCancel.setFont(CoeusFontFactory.getLabelFont());
        btnCancel.setMnemonic('C');
        btnCancel.setText("Cancel");
        btnCancel.setMinimumSize(new java.awt.Dimension(74, 26));
        btnCancel.setPreferredSize(new java.awt.Dimension(74, 26));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(4, 6, 0, 4);
        add(btnCancel, gridBagConstraints);

        lblComment.setFont(CoeusFontFactory.getLabelFont());
        lblComment.setText("Comments:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 13;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 3, 0, 0);
        add(lblComment, gridBagConstraints);

        lblANCCBSBA.setFont(CoeusFontFactory.getLabelFont());
        lblANCCBSBA.setText("ANC&IT not CBSBA:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 11;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHEAST;
        gridBagConstraints.insets = new java.awt.Insets(6, 3, 0, 0);
        add(lblANCCBSBA, gridBagConstraints);

        lblANCSB.setFont(CoeusFontFactory.getLabelFont());
        lblANCSB.setText("ANC&IT not SB:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 12;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHEAST;
        gridBagConstraints.insets = new java.awt.Insets(6, 3, 0, 0);
        add(lblANCSB, gridBagConstraints);

        txtANCCBSBA.setFont(CoeusFontFactory.getNormalFont());
        txtANCCBSBA.setMinimumSize(new java.awt.Dimension(100, 20));
        txtANCCBSBA.setPreferredSize(new java.awt.Dimension(100, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 11;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(6, 3, 0, 0);
        add(txtANCCBSBA, gridBagConstraints);

        txtExANCCBSBA.setFont(CoeusFontFactory.getNormalFont());
        txtExANCCBSBA.setMinimumSize(new java.awt.Dimension(100, 20));
        txtExANCCBSBA.setPreferredSize(new java.awt.Dimension(100, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 11;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(6, 7, 0, 0);
        add(txtExANCCBSBA, gridBagConstraints);

        txtANCSB.setFont(CoeusFontFactory.getNormalFont());
        txtANCSB.setMinimumSize(new java.awt.Dimension(100, 20));
        txtANCSB.setPreferredSize(new java.awt.Dimension(100, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 12;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(6, 3, 0, 0);
        add(txtANCSB, gridBagConstraints);

        txtExANCSB.setFont(CoeusFontFactory.getNormalFont());
        txtExANCSB.setMinimumSize(new java.awt.Dimension(100, 20));
        txtExANCSB.setPreferredSize(new java.awt.Dimension(100, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 12;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(6, 7, 0, 0);
        add(txtExANCSB, gridBagConstraints);
    }// </editor-fold>//GEN-END:initComponents

    private void txtExSDVetActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtExSDVetActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtExSDVetActionPerformed


 
    
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnCancel;
    private javax.swing.JButton btnOK;
    private javax.swing.JLabel lblANCCBSBA;
    private javax.swing.JLabel lblANCSB;
    private javax.swing.JLabel lblComment;
    private javax.swing.JLabel lblExpenditure;
    private javax.swing.JLabel lblGoals;
    private javax.swing.JLabel lblHBCU;
    private javax.swing.JLabel lblHeader;
    private javax.swing.JLabel lblHubZone;
    private javax.swing.JLabel lblLargeBusiness;
    private javax.swing.JLabel lblSBD;
    private javax.swing.JLabel lblSDVet;
    private javax.swing.JLabel lblSmallBusiness;
    private javax.swing.JLabel lblTotal;
    private javax.swing.JLabel lblVeteranOwned;
    private javax.swing.JLabel lblWoman;
    private javax.swing.JScrollPane scrPnComments;
    private edu.mit.coeus.utils.DollarCurrencyTextField txtANCCBSBA;
    private edu.mit.coeus.utils.DollarCurrencyTextField txtANCSB;
    private javax.swing.JTextArea txtArComments;
    private edu.mit.coeus.utils.DollarCurrencyTextField txtExANCCBSBA;
    private edu.mit.coeus.utils.DollarCurrencyTextField txtExANCSB;
    private edu.mit.coeus.utils.DollarCurrencyTextField txtExHBCU;
    private edu.mit.coeus.utils.DollarCurrencyTextField txtExHubZone;
    private edu.mit.coeus.utils.DollarCurrencyTextField txtExLargeBusiness;
    private edu.mit.coeus.utils.DollarCurrencyTextField txtExSDB;
    private edu.mit.coeus.utils.DollarCurrencyTextField txtExSDVet;
    private edu.mit.coeus.utils.DollarCurrencyTextField txtExSmallBusiness;
    private edu.mit.coeus.utils.DollarCurrencyTextField txtExTotal;
    private edu.mit.coeus.utils.DollarCurrencyTextField txtExVeteran;
    private edu.mit.coeus.utils.DollarCurrencyTextField txtExWomanOwned;
    private edu.mit.coeus.utils.DollarCurrencyTextField txtHBCU;
    private edu.mit.coeus.utils.DollarCurrencyTextField txtHubZone;
    private edu.mit.coeus.utils.DollarCurrencyTextField txtLargeBusiness;
    private edu.mit.coeus.utils.DollarCurrencyTextField txtSDB;
    private edu.mit.coeus.utils.DollarCurrencyTextField txtSDVet;
    private edu.mit.coeus.utils.DollarCurrencyTextField txtSmallBusiness;
    private edu.mit.coeus.utils.DollarCurrencyTextField txtTotal;
    private edu.mit.coeus.utils.DollarCurrencyTextField txtVeran;
    private edu.mit.coeus.utils.DollarCurrencyTextField txtWoman;
    // End of variables declaration//GEN-END:variables
    
}
