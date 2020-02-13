/*
 * EndOfMonthProcessForm.java
 *
 * Created on December 28, 2004, 10:27 AM
 */

package edu.mit.coeus.centraladmin.gui;

import edu.mit.coeus.brokers.RequesterBean;
import edu.mit.coeus.brokers.ResponderBean;
import edu.mit.coeus.exception.CoeusClientException;
import edu.mit.coeus.exception.CoeusException;
import edu.mit.coeus.gui.CoeusAppletMDIForm;
import edu.mit.coeus.gui.CoeusDlgWindow;
import edu.mit.coeus.gui.CoeusFontFactory;
import edu.mit.coeus.gui.CoeusMessageResources;
import edu.mit.coeus.utils.AppletServletCommunicator;
import edu.mit.coeus.utils.CoeusGuiConstants;
import edu.mit.coeus.utils.CoeusOptionPane;
import edu.mit.coeus.utils.CoeusVector;
import edu.mit.coeus.utils.ComboBoxBean;
import edu.mit.coeus.utils.JTextFieldFilter;
import edu.mit.coeus.utils.ScreenFocusTraversalPolicy;
import edu.mit.coeus.utils.UserUtils;
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
import java.util.Hashtable;
import javax.swing.AbstractAction;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import org.apache.batik.dom.util.HashTable;


/**
 *
 * @author  surekhan
 */
public class EndOfMonthProcessForm extends javax.swing.JPanel implements ActionListener{
    private CoeusAppletMDIForm mdiForm = CoeusGuiConstants.getMDIForm();
    private CoeusDlgWindow dlgEndOfMonth;
    private static final int WIDTH = 470;
    private static final int HEIGHT = 210;
    private static final String WINDOW_TITLE = "End Of Month Process";

    private static final String[] comboData = {"January" , "February" , "March" ,"April" , "May",
   "June" , "July" , "August" ,"September", "October" , "November" , "December"};

   private static final String CONNECTION_STRING = CoeusGuiConstants.CONNECTION_URL +
        "/CentralAdminMaintenanceServlet";

   private static final char EOM_DATA = 'G';

   private static final int COUNT = 1;

   private String currentMonth;

   /*Fiscal year is Mandatory. Please input a fiscal year.*/
   private static final String FISCAL_YEAR = "endOfMonthProcess_exceptionCode.1901";

   /*Invalid Fiscal Year. Please input a valid fiscal year.*/
   private static final String INVALID_FISCAL_YEAR = "endOfMonthProcess_exceptionCode.1902";

   /*Month is Mandatory. Please select a month.*/
   private static final String MONTH = "endOfMonthProcess_exceptionCode.1903";

   private CoeusMessageResources coeusMessageResources ;

    /** Creates new form EndOfMonthProcessForm */
    public EndOfMonthProcessForm() {
        coeusMessageResources = CoeusMessageResources.getInstance();
        initComponents();
        postInitComponents();
        registerComponents();
        setFormData();
        display();
    }

    /*to instantiate the components*/
     private void postInitComponents() {
        dlgEndOfMonth = new CoeusDlgWindow(mdiForm);
        dlgEndOfMonth.setResizable(false);
        dlgEndOfMonth.setModal(true);
        dlgEndOfMonth.getContentPane().add(this);
        dlgEndOfMonth.setFont(CoeusFontFactory.getLabelFont());
        dlgEndOfMonth.setDefaultCloseOperation(CoeusDlgWindow.DO_NOTHING_ON_CLOSE);
        dlgEndOfMonth.setSize(WIDTH, HEIGHT);
        dlgEndOfMonth.setTitle(WINDOW_TITLE);

        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        Dimension dlgSize = dlgEndOfMonth.getSize();
        dlgEndOfMonth.setLocation(screenSize.width/2 - (dlgSize.width/2),
        screenSize.height/2 - (dlgSize.height/2));

        dlgEndOfMonth.addWindowListener(new WindowAdapter(){

            public void windowClosing(WindowEvent we){
                dlgEndOfMonth.dispose();
                return;
            }
        });

        dlgEndOfMonth.addEscapeKeyListener(
        new AbstractAction("escPressed"){
            public void actionPerformed(ActionEvent ae){
                dlgEndOfMonth.dispose();
                return;
            }
        });
     }


     /*to display the form*/
     private void display(){
         setRequestFocusInThread(txtFicalYear);
         dlgEndOfMonth.show();
     }

     /*to register the listeners and set the focus traversal*/
     private void registerComponents(){
          btnClose.addActionListener(this);
          btnProcess.addActionListener(this);

          java.awt.Component[] component = {txtFicalYear,cmbMonth,btnProcess,btnClose};
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

      /*to set the data to the form*/
      private void setFormData(){
          try{
          txtFicalYear.setDocument(new JTextFieldFilter(JTextFieldFilter.NUMERIC, 4));
          CoeusVector cvData = new CoeusVector();
          for(int i = 0;i < comboData.length;i++){
              cvData.addElement(comboData[i]);
          }
          cmbMonth.setModel(new DefaultComboBoxModel(cvData));
          Hashtable formData = getEomData();
          String fiscalYear = (String)formData.get(new Integer(0));
          txtFicalYear.setText(fiscalYear);
          String count = formData.get(new Integer(COUNT)).toString();
          String date = (String)formData.get(new Integer(2));
          String user = (String)formData.get(new Integer(3));
          lblDateText.setText(date);
          /*
           * UserID to UserName Enhancement - Start
           * Added UserUtils class to change userid to username
           */
          lblByText.setText(UserUtils.getDisplayName(user));
          // UserId to UserName Enhancement - End
          if(count != null){
              int countValue = Integer.parseInt(count);
              if(countValue < 1){
                  btnProcess.setEnabled(false);
              }else{
                  btnProcess.setEnabled(true);
              }
          }
         String statusText = count + " awards to be processed. ";
         txtStatus.setText(statusText);

         Calendar calendar = Calendar.getInstance();
         calendar.setTime(new Date());
         int month = calendar.getTime().getMonth();
         currentMonth = String.valueOf(month);
	 for(int j=0;j<comboData.length;j++){
             if(month == j){
                 cmbMonth.setSelectedItem(comboData[j]);
             }
         }


          }catch(CoeusClientException e){
              e.printStackTrace();
          }catch(NumberFormatException exception){
              exception.printStackTrace();
          }

      }

      /*to get the data from the server*/
      private Hashtable getEomData() throws CoeusClientException{
          RequesterBean requester = new RequesterBean();
          requester.setFunctionType(EOM_DATA);
          AppletServletCommunicator comm = new AppletServletCommunicator(CONNECTION_STRING, requester);
          comm.send();
          ResponderBean response = comm.getResponse();
          if(response.isSuccessfulResponse()){
              Hashtable htValues = (Hashtable)response.getDataObject();
              return htValues;
          }else {
              throw new CoeusClientException(response.getMessage());
          }
      }

      /*to perform validations,if all pass then return true else return false*/
      private boolean validations(){
          if(txtFicalYear.getText().equals("") || txtFicalYear.getText() == null){
              CoeusOptionPane.showInfoDialog(coeusMessageResources.parseMessageKey(FISCAL_YEAR));
              setRequestFocusInThread(txtFicalYear);
              return false;
          }

          if(Integer.parseInt(txtFicalYear.getText()) <= 1950 || Integer.parseInt(txtFicalYear.getText()) >= 3000){
             CoeusOptionPane.showErrorDialog(coeusMessageResources.parseMessageKey(INVALID_FISCAL_YEAR));
             setRequestFocusInThread(txtFicalYear);
             return false;
          }

          if(cmbMonth.getSelectedItem() == null || cmbMonth.getSelectedItem().equals("")){
             CoeusOptionPane.showInfoDialog(coeusMessageResources.parseMessageKey(MONTH));
             setRequestFocusInThread(cmbMonth);
             return false;
          }
          return true; 
      }


      /** This method is called from within the constructor to
       * initialize the form.
       * WARNING: Do NOT modify this code. The content of this method is
       * always regenerated by the Form Editor.
       */
    private void initComponents() {//GEN-BEGIN:initComponents
        java.awt.GridBagConstraints gridBagConstraints;
        javax.swing.JLabel lblFiscalYear;

        lblFiscalYear = new javax.swing.JLabel();
        lblMonth = new javax.swing.JLabel();
        txtFicalYear = new javax.swing.JTextField();
        cmbMonth = new javax.swing.JComboBox();
        lblPreviousRun = new javax.swing.JLabel();
        lblDate = new javax.swing.JLabel();
        lblBy = new javax.swing.JLabel();
        lblDateText = new javax.swing.JLabel();
        lblByText = new javax.swing.JLabel();
        btnProcess = new javax.swing.JButton();
        btnClose = new javax.swing.JButton();
        txtStatus = new edu.mit.coeus.utils.CoeusTextField();

        setLayout(new java.awt.GridBagLayout());

        lblFiscalYear.setFont(CoeusFontFactory.getLabelFont());
        lblFiscalYear.setText(" Fiscal Year:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 3, 0, 0);
        add(lblFiscalYear, gridBagConstraints);

        lblMonth.setFont(CoeusFontFactory.getLabelFont());
        lblMonth.setText("Month:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 0, 0);
        add(lblMonth, gridBagConstraints);

        txtFicalYear.setFont(CoeusFontFactory.getNormalFont());
        txtFicalYear.setMinimumSize(new java.awt.Dimension(80, 23));
        txtFicalYear.setPreferredSize(new java.awt.Dimension(80, 23));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        add(txtFicalYear, gridBagConstraints);

        cmbMonth.setFont(CoeusFontFactory.getNormalFont());
        cmbMonth.setMinimumSize(new java.awt.Dimension(160, 23));
        cmbMonth.setPreferredSize(new java.awt.Dimension(160, 23));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 0, 0, 0);
        add(cmbMonth, gridBagConstraints);

        lblPreviousRun.setFont(CoeusFontFactory.getLabelFont()
        );
        lblPreviousRun.setForeground(new java.awt.Color(255, 51, 51));
        lblPreviousRun.setText("Previous Run:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(10, 5, 0, 0);
        add(lblPreviousRun, gridBagConstraints);

        lblDate.setFont(CoeusFontFactory.getLabelFont());
        lblDate.setText("Date:     ");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(7, 0, 0, 0);
        add(lblDate, gridBagConstraints);

        lblBy.setFont(CoeusFontFactory.getLabelFont());
        lblBy.setText("By:      ");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        add(lblBy, gridBagConstraints);

        lblDateText.setFont(CoeusFontFactory.getNormalFont());
        lblDateText.setMaximumSize(new java.awt.Dimension(250, 21));
        lblDateText.setMinimumSize(new java.awt.Dimension(250, 21));
        lblDateText.setPreferredSize(new java.awt.Dimension(250, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(7, 0, 0, 0);
        add(lblDateText, gridBagConstraints);

        lblByText.setMaximumSize(new java.awt.Dimension(250, 21));
        lblByText.setMinimumSize(new java.awt.Dimension(250, 21));
        lblByText.setPreferredSize(new java.awt.Dimension(250, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        add(lblByText, gridBagConstraints);

        btnProcess.setFont(CoeusFontFactory.getLabelFont());
        btnProcess.setMnemonic('P');
        btnProcess.setText("Process");
        btnProcess.setMaximumSize(new java.awt.Dimension(65, 23));
        btnProcess.setMinimumSize(new java.awt.Dimension(85, 23));
        btnProcess.setPreferredSize(new java.awt.Dimension(85, 23));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHEAST;
        add(btnProcess, gridBagConstraints);

        btnClose.setFont(CoeusFontFactory.getLabelFont());
        btnClose.setMnemonic('C');
        btnClose.setText("Close");
        btnClose.setMaximumSize(new java.awt.Dimension(65, 23));
        btnClose.setMinimumSize(new java.awt.Dimension(85, 23));
        btnClose.setPreferredSize(new java.awt.Dimension(85, 23));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHEAST;
        gridBagConstraints.insets = new java.awt.Insets(5, 0, 0, 0);
        add(btnClose, gridBagConstraints);

        txtStatus.setEditable(false);
        txtStatus.setText("0awards to be processed");
        txtStatus.setMinimumSize(new java.awt.Dimension(450, 22));
        txtStatus.setPreferredSize(new java.awt.Dimension(450, 22));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(17, 0, 0, 0);
        add(txtStatus, gridBagConstraints);

    }//GEN-END:initComponents
    /*the action performed on the click of the buttons*/
    public void actionPerformed(java.awt.event.ActionEvent actionEvent) {
        Object source = actionEvent.getSource();
        try{
            dlgEndOfMonth.setCursor(new Cursor(Cursor.WAIT_CURSOR));
        if(source.equals(btnClose)){
            dlgEndOfMonth.dispose();
        }else if(source.equals(btnProcess)){
            if(validations()){
                try{
                    processData();
                }catch(CoeusClientException e){
                    e.printStackTrace();
                }
            }
        }
        }finally{
            dlgEndOfMonth.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
        }
    }

    /* to process the data by sending the fiscal year and the month and enabling and disabling
     *the process button based on the return value from the server*/
    private void processData()throws CoeusClientException {
        CoeusVector cvData = new CoeusVector();
        txtStatus.setText("Processing...");
        RequesterBean requester = new RequesterBean();
        String year = txtFicalYear.getText();
        cvData.add(0, year);
        cvData.add(1,/*currentMonth*/String.valueOf(cmbMonth.getSelectedIndex()+1));  // ** ASU
        requester.setFunctionType('H');
        requester.setDataObject(cvData);
        AppletServletCommunicator comm = new AppletServletCommunicator(CONNECTION_STRING, requester);
        comm.send();
        ResponderBean response = comm.getResponse();
        if(response.isSuccessfulResponse()){
            String value = response.getDataObject().toString();
            if(value == "0"){
                btnProcess.setEnabled(false);
                txtStatus.setText("Complete...");
            }else{
                txtStatus.setText("Error");
            }
        }else {
            throw new CoeusClientException(response.getMessage());
        }
    }


    // Variables declaration - do not modify//GEN-BEGIN:variables
    public javax.swing.JButton btnClose;
    public javax.swing.JButton btnProcess;
    public javax.swing.JComboBox cmbMonth;
    public javax.swing.JLabel lblBy;
    public javax.swing.JLabel lblByText;
    public javax.swing.JLabel lblDate;
    public javax.swing.JLabel lblDateText;
    public javax.swing.JLabel lblMonth;
    public javax.swing.JLabel lblPreviousRun;
    public javax.swing.JTextField txtFicalYear;
    public edu.mit.coeus.utils.CoeusTextField txtStatus;
    // End of variables declaration//GEN-END:variables

}
