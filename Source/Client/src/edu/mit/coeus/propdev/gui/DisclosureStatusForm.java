/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * DisclosureStatusForm.java
 *
 * Created on Nov 23, 2011, 11:15:15 AM
 */

package edu.mit.coeus.propdev.gui;
import edu.mit.coeus.brokers.RequesterBean;
import edu.mit.coeus.brokers.ResponderBean;
import edu.mit.coeus.gui.CoeusDlgWindow;
import edu.mit.coeus.gui.CoeusFontFactory;
import edu.mit.coeus.gui.CoeusMessageResources;
import edu.mit.coeus.utils.AppletServletCommunicator;
import edu.mit.coeus.utils.CoeusGuiConstants;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.util.Vector;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumn;
/**
 *
 * @author twinkle
 */
public class DisclosureStatusForm extends javax.swing.JPanel {
 /** CoeusDlgWindow instance */
    public CoeusDlgWindow dlgDisclStatusForm;

    private String title = "COI Disclosure Status";

    /** Servlet URL */
    private final String conURL = "/ProposalActionServlet";
    private final String SUBMISSION_DETAILS_SERVLET = "/SubmissionDetailsServlet" ;
    private final String IACUC_SUBMISSION_DETAILS_SERVLET = "/IacucProtoSubmissionDetailsServlet" ;

    private final String AWARD_SUBMISSION_DETAILS_SERVLET  = "/AwardMaintenanceServlet" ;
    private static final char GET_DISCLOSURE_STATUS = '2';
    Vector personDisclDetailsVector= new Vector();
    private String proNumber;
    private int moduleCode;

     /** holds CoeusMessageResources instance used for reading message Properties. */
    private CoeusMessageResources coeusMessageResources;
public DisclosureStatusForm(String  eventDisclNum, int disclmodulecode) {
       proNumber=eventDisclNum;
       moduleCode=disclmodulecode;
       initComponents();
       postInitComponents();
       setFormData();
       setTableEditors();

       initDialogWindow();
       initForm();
       display();
    }

    /** Creates new form DisclosureStatusForm */

         public void display() {
        dlgDisclStatusForm.setVisible(true);
    }
 public void initForm() {

        coeusMessageResources = CoeusMessageResources.getInstance();
       formatTable();
    }
  private void formatTable() {
            jTable2.getSelectionModel().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    }

  private void setTableEditors(){


        jTable2.setOpaque(false);
        jTable2.setShowVerticalLines(false);
        jTable2.setShowHorizontalLines(false);
        jTable2.setRowHeight(22);
        jTable2.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        jTable2.setFont( CoeusFontFactory.getNormalFont() );
        JTableHeader header = jTable2.getTableHeader();
        header.setReorderingAllowed(false);
        TableColumn clmName = jTable2.getColumnModel().getColumn(0);
        clmName.setPreferredWidth(160);
        clmName.setMinWidth(160);
        clmName = jTable2.getColumnModel().getColumn(1);
        clmName.setPreferredWidth(160);
        clmName.setMinWidth(160);
        clmName = jTable2.getColumnModel().getColumn(2);
        clmName.setPreferredWidth(150);
        clmName.setMinWidth(150);
//        clmName = jTable2.getColumnModel().getColumn(3);
//        clmName.setPreferredWidth(120);
//        clmName.setMinWidth(100);
     }
   public void setFormData() {
     retrievedisclosureDetails();
      int rowCount = jTable2.getRowCount();
      jLabel4.setText(proNumber);
     if(personDisclDetailsVector.size()>0){
         for(int i=0;i<personDisclDetailsVector.size();i++){
             Vector personDisclDetailsVector1= (Vector)personDisclDetailsVector.get(i);
              for(int j=0;j<personDisclDetailsVector1.size();j++){
            // ((DefaultTableModel) jTable2.getModel()).addRow((Vector)personDisclDetailsVector.elementAt(i));
                  Vector personDisclDetailsVector2=(Vector)personDisclDetailsVector1.get(j);
               if(personDisclDetailsVector2.size()>0)
         {
             String title = "";
             if(personDisclDetailsVector2.size() > 3){
                 title = personDisclDetailsVector2.get(3).toString();
                 if (title.length()>80){title=title.substring(0,80);}
             }
             else{title="";}
             jLabel1.setText(title);
         }
              ((DefaultTableModel) jTable2.getModel()).addRow((Vector)personDisclDetailsVector1.get(j));


                rowCount = jTable2.getRowCount() -1 ;
                setTableEditors();
                jTable2.scrollRectToVisible(
                        jTable2.getCellRect(rowCount ,0, true));

                 jTable2.setRowSelectionInterval(rowCount, rowCount);
                  jTable2.requestFocusInWindow();
        jTable2.editCellAt(rowCount,2);
         // jTextField2.setText(personDisclDetailsVector.get(4));
              }
//         if(personDisclDetailsVector1.size()>0)
//         {
//             String title = "";
//             if(personDisclDetailsVector1.size() > 4){
//                 title = personDisclDetailsVector1.get(4).toString();
//             }
//             jLabel4.setText(title);
//         }
         }
     }

    }
 public void retrievedisclosureDetails(){
          Vector requestParameter = new Vector();
          requestParameter.add(proNumber);
          String connectTo="";
          RequesterBean requesterBean  = new RequesterBean();
          requesterBean.setFunctionType(GET_DISCLOSURE_STATUS);
          // requesterBean.setDataObject(requestParameter);
          requesterBean.setDataObject(proNumber);
          if(moduleCode==3) 
          {
          connectTo = CoeusGuiConstants.CONNECTION_URL + conURL;
          }
          if(moduleCode==7)
          {
          connectTo = CoeusGuiConstants.CONNECTION_URL + SUBMISSION_DETAILS_SERVLET;
          }
          if(moduleCode==9)
          {
          connectTo = CoeusGuiConstants.CONNECTION_URL + IACUC_SUBMISSION_DETAILS_SERVLET;
          }

          if(moduleCode==1)
          {
          connectTo = CoeusGuiConstants.CONNECTION_URL + AWARD_SUBMISSION_DETAILS_SERVLET;
          }
          AppletServletCommunicator comm = new AppletServletCommunicator(connectTo,requesterBean);
          comm.send();
          ResponderBean responderBean = comm.getResponse();
          try{
              if(responderBean.isSuccessfulResponse()){
                  if ( responderBean !=null ){
                personDisclDetailsVector = (Vector) responderBean.getDataObjects();
              //personIDVector =(Vector) responseParameters.get(1);
                  }
              }
          }catch(Exception e) {
            e.printStackTrace();
        }


    }
 public void initDialogWindow(){

        dlgDisclStatusForm = new CoeusDlgWindow(CoeusGuiConstants.getMDIForm(),title,true);
        dlgDisclStatusForm.getContentPane().add(this);
        dlgDisclStatusForm.pack();
        dlgDisclStatusForm.setFont(CoeusFontFactory.getLabelFont());

        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        Dimension dlgSize = dlgDisclStatusForm.getSize();
        dlgDisclStatusForm.setLocation(screenSize.width/2 - (dlgSize.width/2),
                screenSize.height/2 - (dlgSize.height/2));

        dlgDisclStatusForm.setResizable(false);

    }
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        jButton1 = new javax.swing.JButton();
        closeButton = new javax.swing.JButton();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTable2 = new javax.swing.JTable();
        jLabel3 = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();

        jButton1.setText("jButton1");

        setEnabled(false);
        setMaximumSize(new java.awt.Dimension(574, 325));
        setMinimumSize(new java.awt.Dimension(574, 325));
        setPreferredSize(new java.awt.Dimension(574, 325));
        setLayout(new java.awt.GridBagLayout());

        closeButton.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        closeButton.setText("Close");
        closeButton.setMaximumSize(new java.awt.Dimension(67, 23));
        closeButton.setMinimumSize(new java.awt.Dimension(67, 23));
        closeButton.setPreferredSize(new java.awt.Dimension(67, 23));
        closeButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                closeButtonActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.insets = new java.awt.Insets(11, 18, 0, 10);
        add(closeButton, gridBagConstraints);

        jScrollPane2.setMaximumSize(new java.awt.Dimension(2147483647, 64));

        jTable2.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null}
            },
            new String [] {
                "Person Name", "Role", "COI Disclosure Status"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jTable2.setColumnSelectionAllowed(true);
        jScrollPane2.setViewportView(jTable2);
        jTable2.getColumnModel().getSelectionModel().setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.ipadx = 477;
        gridBagConstraints.ipady = 137;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(11, 10, 92, 0);
        add(jScrollPane2, gridBagConstraints);

        jLabel3.setFont(new java.awt.Font("Tahoma", 1, 11));
        jLabel3.setText("Project#:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(24, 10, 0, 0);
        add(jLabel3, gridBagConstraints);

        jLabel1.setText("jLabel1");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(6, 6, 0, 0);
        add(jLabel1, gridBagConstraints);

        jLabel2.setFont(new java.awt.Font("Tahoma", 1, 11));
        jLabel2.setText("Title:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(6, 35, 0, 0);
        add(jLabel2, gridBagConstraints);

        jLabel4.setText("jLabel4");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(24, 6, 0, 0);
        add(jLabel4, gridBagConstraints);
    }// </editor-fold>//GEN-END:initComponents

    private void closeButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_closeButtonActionPerformed
        // TODO add your handling code here:
        dlgDisclStatusForm.dispose();
    }//GEN-LAST:event_closeButtonActionPerformed
 private void postInitComponents(){

        setUnitLeadModel();
        jTable2.getTableHeader().setFont(CoeusFontFactory.getLabelFont());
        jTable2.setFont(CoeusFontFactory.getNormalFont());
 }

     private void setUnitLeadModel(){

        jTable2.setModel(new javax.swing.table.DefaultTableModel(new Object[][]{},
                getUnitTableColumnNames().toArray()){
            Class[] types = new Class [] {
                java.lang.String.class,java.lang.String.class,
                java.lang.String.class
            };

            @Override
            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }
            @Override
            public boolean isCellEditable(int row, int col){
                 if(col == 1 || col == 2 ){ //|| col == 4

                        return false;
                    }
                 else{
                     return true;}

            }

            @Override
            public void setValueAt(Object value, int row, int col) {

             
                    super.setValueAt(value,row,col);

               
            }
        });
    }


 private Vector getUnitTableColumnNames(){

        Vector disclosureStatusTableHeaders = new Vector();
        disclosureStatusTableHeaders.add( "Person Name" );
//         disclosureStatusTableHeaders.add( "Department"  );
        disclosureStatusTableHeaders.add( "Role");
        disclosureStatusTableHeaders.add( "Disclosure Status");
        return disclosureStatusTableHeaders ;
    }
    // End of variables declaration

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton closeButton;
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTable jTable2;
    // End of variables declaration//GEN-END:variables



}
