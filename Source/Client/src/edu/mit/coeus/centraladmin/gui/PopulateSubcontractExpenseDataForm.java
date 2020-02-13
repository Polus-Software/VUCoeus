/*
 * PopulateSubcontractExpenseDataForm.java
 *
 * Created on December 29, 2004, 6:01 PM
 */

package edu.mit.coeus.centraladmin.gui;

import edu.mit.coeus.brokers.RequesterBean;
import edu.mit.coeus.brokers.ResponderBean;
import edu.mit.coeus.exception.CoeusClientException;
import edu.mit.coeus.gui.CoeusAppletMDIForm;
import edu.mit.coeus.gui.CoeusDlgWindow;
import edu.mit.coeus.gui.CoeusFontFactory;
import edu.mit.coeus.utils.AppletServletCommunicator;
import edu.mit.coeus.utils.CoeusGuiConstants;
import edu.mit.coeus.utils.CoeusOptionPane;
import edu.mit.coeus.utils.CoeusVector;
import edu.mit.coeus.utils.ScreenFocusTraversalPolicy;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Calendar;
import java.util.Date;
import javax.swing.AbstractAction;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import edu.mit.coeus.budget.gui.StatusWindow;
import edu.mit.coeus.exception.CoeusException;
import edu.mit.coeus.exception.CoeusUIException;
import edu.mit.coeus.gui.CoeusMessageResources;
import edu.mit.coeus.utils.JTextFieldFilter;

/**
 *
 * @author  surekhan
 */
public class PopulateSubcontractExpenseDataForm extends javax.swing.JPanel implements ActionListener{
    
    private CoeusAppletMDIForm mdiForm = CoeusGuiConstants.getMDIForm();
    private CoeusDlgWindow dlgPopulateSubContract;
    private static final int WIDTH = 480;
    private static final int HEIGHT = 190;
    private static final String WINDOW_TITLE = "Coeus";
    private String currentYear;
    private static final String CONNECTION_STRING = CoeusGuiConstants.CONNECTION_URL +
        "/CentralAdminMaintenanceServlet";
    private StatusWindow statusWindow;
    
    /*Period End is mandatory. Please enter a period end */
    private static final String PERIOD_END = "subcontractExpenseData_exceptionCode.1951";
    
    /*Period start cannot be later than period end.*/
    private static final String PERIOD_START = "subcontractExpenseData_exceptionCode.1952";
    
    private CoeusMessageResources coeusMessageResources;
    
    private static final String[] comboData = {"January" , "February" , "March" ,"April" , "May",
   "June" , "July" , "August" ,"September", "October" , "November" , "December"};
    
    /** Creates new form PopulateSubcontractExpenseDataForm */
    public PopulateSubcontractExpenseDataForm() {
        coeusMessageResources = CoeusMessageResources.getInstance();
        initComponents();
        postInitComponents();
        registerComponents();
        //case 2718 start
//        setFormData();
        //case 2718 end
        display();
    }
    
    
    /*to instantiate the components*/
     private void postInitComponents() {
        statusWindow = new StatusWindow(mdiForm, true);
        dlgPopulateSubContract = new CoeusDlgWindow(mdiForm);
        dlgPopulateSubContract.setResizable(false);
        dlgPopulateSubContract.setModal(true);
        dlgPopulateSubContract.getContentPane().add(this);
        dlgPopulateSubContract.setFont(CoeusFontFactory.getLabelFont());
        dlgPopulateSubContract.setDefaultCloseOperation(CoeusDlgWindow.DO_NOTHING_ON_CLOSE);
        dlgPopulateSubContract.setSize(WIDTH, HEIGHT);
        dlgPopulateSubContract.setTitle(WINDOW_TITLE); 
        
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        Dimension dlgSize = dlgPopulateSubContract.getSize();
        dlgPopulateSubContract.setLocation(screenSize.width/2 - (dlgSize.width/2),
        screenSize.height/2 - (dlgSize.height/2));
        
        dlgPopulateSubContract.addWindowListener(new WindowAdapter(){
            
            public void windowClosing(WindowEvent we){
                dlgPopulateSubContract.dispose();
                return;
            }
        });
        
        dlgPopulateSubContract.addEscapeKeyListener(
        new AbstractAction("escPressed"){
            public void actionPerformed(ActionEvent ae){
                dlgPopulateSubContract.dispose();
                return;
            }
        });
     }
     
     
     /*to display the form*/
     private void display(){
         setRequestFocusInThread(txtStartPeriod);
         dlgPopulateSubContract.show();
     }
     
     /*to register the listeners and set the focus traversal*/
     private void registerComponents(){
          btnCancel.addActionListener(this);
          btnOK.addActionListener(this);
          
          java.awt.Component[] component = {txtStartPeriod,txtPeriodEnd,btnOK,btnCancel};
          ScreenFocusTraversalPolicy policy = new ScreenFocusTraversalPolicy(component);
          setFocusTraversalPolicy(policy);
          setFocusCycleRoot(true);
      }
      
     
     /** Supporting method which will be used for the focus lost for date
       *fields. This will be fired when the request focus for the specified
       *date field is invoked
       */
      private void setRequestFocusInThread(final Component component) {
          SwingUtilities.invokeLater( new Runnable() {
              public void run() {
                  component.requestFocusInWindow();
              }
          });
      }
      
      /*to set the form data*/
     private void setFormData() {
         int val = 0;
         String mon = "";
         int currMonth = 0;
         try{
             CoeusVector cvData = new CoeusVector();
             CoeusVector cvTemp = new CoeusVector();
             for(int i = 0;i < comboData.length;i++){
                 cvData.addElement(comboData[i]);
             }
             txtStartPeriod.setDocument(new JTextFieldFilter(JTextFieldFilter.NUMERIC, 6));
             txtPeriodEnd.setDocument(new JTextFieldFilter(JTextFieldFilter.NUMERIC, 6));
             Calendar calendar = Calendar.getInstance();
             String fiscalMonth = getFiscalMonth();
             int fisMonth = Integer.parseInt(fiscalMonth);
             for(int j = 0;j< cvData.size();j++){
                 if(j == fisMonth-1){
                     cvTemp.add(cvData.get(j));
                     val = j;
                 }
             }
             
             for(int l = val+1;l <comboData.length;l++){
                 cvTemp.addElement(comboData[l]);
               }
             for(int p = 0;p <val ;p++){
                 cvTemp.addElement(comboData[p]);
             }
             
             
             calendar.setTime(new Date());
             int mn = calendar.getTime().getMonth();
             if(mn + 1 >= fisMonth ){
               int year = calendar.get(Calendar.YEAR) + 1;
               currentYear = String.valueOf(year);  
             }else{
                 int year = calendar.get(Calendar.YEAR);
               currentYear = String.valueOf(year);  
             }
             for(int s=0;s<cvData.size();s++){
                 if(mn == s){
                     mon = cvData.get(s).toString();
                     
                 }
             }
             
             for(int x = 0;x <cvTemp.size() ;x++){
                if(cvTemp.get(x).equals(mon)){
                    currMonth = x;
                }
             }
             String currentMonth = String.valueOf(currMonth + 1);
             
             if(currentMonth.trim().length() == 1){
                 txtStartPeriod.setText(currentYear+"0"+currentMonth);
                 txtPeriodEnd.setText(currentYear+"0"+currentMonth);
             }else{
                 txtStartPeriod.setText(currentYear+currentMonth);
                 txtPeriodEnd.setText(currentYear+currentMonth);
             }
             
         }catch(CoeusClientException e){
             e.printStackTrace();
         }
     }
     
     /*to get the month from the parameter table*/
     private String getFiscalMonth() throws CoeusClientException{
         RequesterBean requester = new RequesterBean();
         requester.setFunctionType('L');
         AppletServletCommunicator comm = new AppletServletCommunicator(CONNECTION_STRING, requester);
         comm.send();
         ResponderBean response = comm.getResponse();
         //case 2718 start
//         if(response.isSuccessfulResponse()){
         if(response.isSuccessfulResponse() && response.getDataObject() != null ){
         //case 2718 end
             String value = response.getDataObject().toString();
             return value;
         }
         else {
             //case 2718 start
//             throw new CoeusClientException(response.getMessage());
             throw new CoeusClientException("Missing parameter value of FISCAL_YEAR_START_MONTH");             
             //case 2718 end
         }
     }
     
     
     /*the action performed on the click of the buttons*/
     public void actionPerformed(java.awt.event.ActionEvent actionEvent) {
         Object source = actionEvent.getSource();
         try{
             dlgPopulateSubContract.setCursor(new Cursor(Cursor.WAIT_CURSOR));
             if(source.equals(btnCancel)){
                 dlgPopulateSubContract.dispose();
             }else if(source.equals(btnOK)){
                 try{
                     if(validations()){
                         showSavingForm();
                     }
                 }catch(CoeusClientException e){
                     e.printStackTrace();
                 }
             }
         }finally{
             dlgPopulateSubContract.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
         }
     }
     
     /*to perform the validations and returns true if all the validations pass else false*/
     private boolean validations() {
         if(txtStartPeriod.getText().equals("") || txtStartPeriod.getText() == null || txtStartPeriod.getText().length() == 0){
             txtStartPeriod.setText("000000");
             return true;
         }
         if(txtPeriodEnd.getText().equals("") || txtPeriodEnd.getText() == null || txtPeriodEnd.getText().length() == 0){
             CoeusOptionPane.showErrorDialog(coeusMessageResources.parseMessageKey(PERIOD_END));
             setRequestFocusInThread(txtPeriodEnd);
             return false;
         }
         if(txtStartPeriod.getText().trim().length() != 6){
             CoeusOptionPane.showErrorDialog("Invalid period start. Period start should be in the format YYYYMM \n where YYYY is the fiscal year and MM is fiscal month (01 For July, 02 For August etc.). ");
             setRequestFocusInThread(txtStartPeriod);
             return false;
         }
         
         if(txtPeriodEnd.getText().trim().length() != 6){
             CoeusOptionPane.showErrorDialog("Invalid period end. Period end should be in the format YYYYMM \n where YYYY is the fiscal year and MM is fiscal month (01 For July, 02 For August etc.). ");
             setRequestFocusInThread(txtPeriodEnd);
             return false;
         }
         
         if(Integer.parseInt(txtStartPeriod.getText()) > Integer.parseInt(txtPeriodEnd.getText())){
             CoeusOptionPane.showErrorDialog(coeusMessageResources.parseMessageKey(PERIOD_START));
             setRequestFocusInThread(txtStartPeriod);
             return false;
         }
         
         return true;
     }
     
     /*to show the savings form*/
     private void showSavingForm() throws CoeusClientException{
         statusWindow.setHeader("Accessing Data Warehouse.Please Wait...");
         statusWindow.display();
         Thread thread = new Thread(new Runnable() {
             public void run() {
                 if(validations()){
                     try{
                         populateExpenseData();
                     }catch(CoeusClientException exception){
                         CoeusOptionPane.showInfoDialog(exception.getMessage());
                         dlgPopulateSubContract.dispose();
                     }
                 }
                 
                 statusWindow.setVisible(false);
                 
             }
         });
         thread.start();
     }
     
     /*to process the popoulate subcontract expense data function in the data base*/
     private void populateExpenseData() throws CoeusClientException{
         CoeusVector cvData = new CoeusVector();
         RequesterBean requester = new RequesterBean();
         String periodStart = txtStartPeriod.getText();
         String periodEnd = txtPeriodEnd.getText();
         cvData.add(0,periodStart );
         cvData.add(1,periodEnd);
         requester.setFunctionType('I');
         requester.setDataObject(cvData);
         AppletServletCommunicator comm = new AppletServletCommunicator(CONNECTION_STRING, requester);
         comm.send();
         ResponderBean response = comm.getResponse();
         if(response.isSuccessfulResponse()){
             String value = response.getDataObject().toString();
             dlgPopulateSubContract.dispose();
         }else {
             throw new CoeusClientException(response.getMessage());
         }
     }
     
     /** This method is called from within the constructor to
      * initialize the form.
      * WARNING: Do NOT modify this code. The content of this method is
      * always regenerated by the Form Editor.
      */
    private void initComponents() {//GEN-BEGIN:initComponents
        java.awt.GridBagConstraints gridBagConstraints;

        lblHeading = new javax.swing.JLabel();
        lblProvide = new javax.swing.JLabel();
        lblEndDate = new javax.swing.JLabel();
        lblStartDate = new javax.swing.JLabel();
        pnlPeriods = new javax.swing.JPanel();
        lblStart = new javax.swing.JLabel();
        txtStartPeriod = new javax.swing.JTextField();
        lblEnd = new javax.swing.JLabel();
        txtPeriodEnd = new javax.swing.JTextField();
        btnOK = new javax.swing.JButton();
        btnCancel = new javax.swing.JButton();
        lblIcon = new javax.swing.JLabel();
        lblEndIcon = new javax.swing.JLabel();
        lblStartIcon = new javax.swing.JLabel();

        setLayout(new java.awt.GridBagLayout());

        lblHeading.setFont(new java.awt.Font("MS Sans Serif", 1, 15));
        lblHeading.setText("Populate subcontract expense data from warehouse.");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 0, 0);
        add(lblHeading, gridBagConstraints);

        lblProvide.setFont(CoeusFontFactory.getNormalFont());
        lblProvide.setText("Provide a Start and End of fiscal period in the format YYYYMM.");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 30, 0, 0);
        add(lblProvide, gridBagConstraints);

        lblEndDate.setFont(CoeusFontFactory.getNormalFont());
        lblEndDate.setText("End date is mandatory, Start Date is optional");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 30, 0, 0);
        add(lblEndDate, gridBagConstraints);

        lblStartDate.setFont(CoeusFontFactory.getNormalFont());
        lblStartDate.setText("<html>If Start date is left blank, all available data till the End date will be <br> brought forward from the ware house</html>");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 30, 0, 0);
        add(lblStartDate, gridBagConstraints);

        pnlPeriods.setLayout(new java.awt.GridBagLayout());

        lblStart.setFont(CoeusFontFactory.getLabelFont()
        );
        lblStart.setText("Period Start");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 0, 0);
        pnlPeriods.add(lblStart, gridBagConstraints);

        txtStartPeriod.setFont(CoeusFontFactory.getNormalFont());
        txtStartPeriod.setMinimumSize(new java.awt.Dimension(115, 23));
        txtStartPeriod.setPreferredSize(new java.awt.Dimension(115, 23));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 0, 0);
        pnlPeriods.add(txtStartPeriod, gridBagConstraints);

        lblEnd.setFont(CoeusFontFactory.getLabelFont());
        lblEnd.setText("Period End");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.insets = new java.awt.Insets(0, 5, 0, 0);
        pnlPeriods.add(lblEnd, gridBagConstraints);

        txtPeriodEnd.setFont(CoeusFontFactory.getNormalFont());
        txtPeriodEnd.setMinimumSize(new java.awt.Dimension(115, 23));
        txtPeriodEnd.setPreferredSize(new java.awt.Dimension(115, 23));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 0, 0);
        pnlPeriods.add(txtPeriodEnd, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(12, 3, 0, 0);
        add(pnlPeriods, gridBagConstraints);

        btnOK.setFont(CoeusFontFactory.getLabelFont());
        btnOK.setMnemonic('O');
        btnOK.setText("OK");
        btnOK.setMaximumSize(new java.awt.Dimension(75, 24));
        btnOK.setMinimumSize(new java.awt.Dimension(75, 24));
        btnOK.setPreferredSize(new java.awt.Dimension(75, 24));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(5, 9, 0, 0);
        add(btnOK, gridBagConstraints);

        btnCancel.setFont(CoeusFontFactory.getLabelFont());
        btnCancel.setMnemonic('C');
        btnCancel.setText("Cancel");
        btnCancel.setMaximumSize(new java.awt.Dimension(75, 24));
        btnCancel.setMinimumSize(new java.awt.Dimension(75, 24));
        btnCancel.setPreferredSize(new java.awt.Dimension(75, 24));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 9, 0, 0);
        add(btnCancel, gridBagConstraints);

        lblIcon.setIcon(new ImageIcon(getClass().getClassLoader().getResource(CoeusGuiConstants.BLACK_ICON)));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 15, 0, 0);
        add(lblIcon, gridBagConstraints);

        lblEndIcon.setIcon(new ImageIcon(getClass().getClassLoader().getResource(CoeusGuiConstants.BLACK_ICON)));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 15, 0, 0);
        add(lblEndIcon, gridBagConstraints);

        lblStartIcon.setIcon(new ImageIcon(getClass().getClassLoader().getResource(CoeusGuiConstants.BLACK_ICON)));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 15, 0, 0);
        add(lblStartIcon, gridBagConstraints);

    }//GEN-END:initComponents
    
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    public javax.swing.JButton btnCancel;
    public javax.swing.JButton btnOK;
    public javax.swing.JLabel lblEnd;
    public javax.swing.JLabel lblEndDate;
    public javax.swing.JLabel lblEndIcon;
    public javax.swing.JLabel lblHeading;
    public javax.swing.JLabel lblIcon;
    public javax.swing.JLabel lblProvide;
    public javax.swing.JLabel lblStart;
    public javax.swing.JLabel lblStartDate;
    public javax.swing.JLabel lblStartIcon;
    public javax.swing.JPanel pnlPeriods;
    public javax.swing.JTextField txtPeriodEnd;
    public javax.swing.JTextField txtStartPeriod;
    // End of variables declaration//GEN-END:variables
    
}
