/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * personNotificationForm.java
 *
 * Created on Jan 04, 2012, 11:21:27 AM
 */
package edu.mit.coeus.iacuc.gui;

import edu.mit.coeus.brokers.RequesterBean;
import edu.mit.coeus.brokers.ResponderBean;
import edu.mit.coeus.gui.CoeusDlgWindow;
import edu.mit.coeus.gui.CoeusFontFactory;
import edu.mit.coeus.gui.CoeusMessageResources;

import edu.mit.coeus.utils.AppletServletCommunicator;
import edu.mit.coeus.utils.CoeusGuiConstants;
import edu.mit.coeus.utils.CoeusOptionPane;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.util.Vector;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumn;

/**
 *
 * @author twinkle
 */
public class ProtocolNotificationForm extends javax.swing.JPanel {

    /** CoeusDlgWindow instance */
    public CoeusDlgWindow dlgNotificationForm;
    private String title = "Send Notification";
    /** Servlet URL */
    private final String iacucConURL = "/IacucProtoSubmissionDetailsServlet";
    private final String SUBMISSION_DETAILS_SERVLET = "/SubmissionDetailsServlet" ;
    private Vector personNotifyDetailsVector = new Vector();
    private Vector personIDVector = new Vector();
    private Vector personIdToSendNotification = new Vector();
    private String proposalNumber;
    /** static variables for functiontype while server side call */
    private static final char GET_NOTIFY_DETAILS = 'n';
    private static final char PROTOCOL_PERSON_SEND = 'p';
    /** holds CoeusMessageResources instance used for reading message Properties. */
    private CoeusMessageResources coeusMessageResources;
    private int protocolSequenceNumber;
    private String leadUnitNumber;
    private int module_code;
    /** Creates new form personNotificationForm */
    public ProtocolNotificationForm() {
    }
    public ProtocolNotificationForm(String ProposalNum, int sequenceId, int modulecode) {
        proposalNumber = ProposalNum;
        protocolSequenceNumber = sequenceId;
        module_code=modulecode;
        initComponents();
        postInitComponents();
        setFormData();
        setTableEditors();
        initDialogWindow();
        initForm();
        display();
    }
    /**
     * To Display the Form after initialisation
     */
    public void display() {
        dlgNotificationForm.setVisible(true);
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

        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        selectAllCheck = new javax.swing.JCheckBox();
        selectAllLabel = new javax.swing.JLabel();
        jBtnPanel = new javax.swing.JPanel();
        sendNotificationButton = new javax.swing.JButton();
        closeButton = new javax.swing.JButton();

        setMaximumSize(new java.awt.Dimension(460, 300));
        setMinimumSize(new java.awt.Dimension(460, 300));
        setPreferredSize(new java.awt.Dimension(460, 300));
        setLayout(new java.awt.GridBagLayout());

        jScrollPane1.setAutoscrolls(true);
        jScrollPane1.setMaximumSize(new java.awt.Dimension(350, 250));
        jScrollPane1.setMinimumSize(new java.awt.Dimension(350, 250));
        jScrollPane1.setPreferredSize(new java.awt.Dimension(350, 250));

        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Select", "Name", "Last Notification"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Boolean.class, java.lang.String.class, java.lang.String.class
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }
        });
        jTable1.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_ALL_COLUMNS);
        jTable1.setColumnSelectionAllowed(true);
        jTable1.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jTable1.setMaximumSize(new java.awt.Dimension(380, 200));
        jTable1.setMinimumSize(new java.awt.Dimension(380, 200));
        jTable1.setPreferredSize(new java.awt.Dimension(380, 200));
        jTable1.setSelectionBackground(new java.awt.Color(255, 255, 255));
        jTable1.setSelectionForeground(new java.awt.Color(0, 0, 0));
        jTable1.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        jScrollPane1.setViewportView(jTable1);
        jTable1.getColumnModel().getSelectionModel().setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.gridheight = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.ipadx = 30;
        gridBagConstraints.ipady = 10;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(34, 8, 11, 0);
        add(jScrollPane1, gridBagConstraints);

        selectAllCheck.setName("selectAllChk"); // NOI18N
        selectAllCheck.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                selectAllCheckActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.ipadx = -1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(10, 10, 0, 0);
        add(selectAllCheck, gridBagConstraints);

        selectAllLabel.setText("Select All/None");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.ipady = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(10, 0, 0, 0);
        add(selectAllLabel, gridBagConstraints);

        jBtnPanel.setMaximumSize(new java.awt.Dimension(80, 100));
        jBtnPanel.setMinimumSize(new java.awt.Dimension(80, 100));
        jBtnPanel.setPreferredSize(new java.awt.Dimension(80, 100));
        jBtnPanel.setLayout(new java.awt.GridBagLayout());

        sendNotificationButton.setFont(new java.awt.Font("Tahoma", 1, 11));
        sendNotificationButton.setText("Send");
        sendNotificationButton.setToolTipText("Send Notification");
        sendNotificationButton.setMaximumSize(new java.awt.Dimension(67, 23));
        sendNotificationButton.setMinimumSize(new java.awt.Dimension(67, 23));
        sendNotificationButton.setName("sendButton"); // NOI18N
        sendNotificationButton.setPreferredSize(new java.awt.Dimension(67, 23));
        sendNotificationButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                sendNotificationButtonActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTH;
        gridBagConstraints.insets = new java.awt.Insets(11, 10, 0, 3);
        jBtnPanel.add(sendNotificationButton, gridBagConstraints);

        closeButton.setFont(new java.awt.Font("Tahoma", 1, 11));
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
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTH;
        gridBagConstraints.insets = new java.awt.Insets(7, 10, 204, 3);
        jBtnPanel.add(closeButton, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridheight = 3;
        gridBagConstraints.ipady = 168;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTH;
        gridBagConstraints.insets = new java.awt.Insets(21, 0, 11, 0);
        add(jBtnPanel, gridBagConstraints);
    }// </editor-fold>//GEN-END:initComponents
   private void postInitComponents() {
        setUnitLeadModel();
        jTable1.getTableHeader().setFont(CoeusFontFactory.getLabelFont());
        jTable1.setFont(CoeusFontFactory.getNormalFont());
    }
    /* supporting method to set the table model table */
   private void setUnitLeadModel() {
       jTable1.setModel(new javax.swing.table.DefaultTableModel(new Object[][]{},
                getUnitTableColumnNames().toArray()) {
            Class[] types = new Class[]{
                java.lang.Boolean.class, java.lang.String.class,
                java.lang.String.class
            };
            @Override
            public Class getColumnClass(int columnIndex) {
                return types[columnIndex];
            }
            @Override
            public boolean isCellEditable(int row, int col) {
                if (col == 1 || col == 2) { //|| col == 4
                    return false;
                } else {
                    return true;
                }
            }
            @Override
            public void setValueAt(Object value, int row, int col) {
                if (col == 0) {
                    boolean set = ((Boolean) value).booleanValue();
                    super.setValueAt(value, row, col);
                } else {
                    super.setValueAt(value, row, col);
                }
            }
        });
    }
    private Vector getUnitTableColumnNames() {
        Vector notificatinTableHeaders = new Vector();
        notificatinTableHeaders.add("Select");
        notificatinTableHeaders.add("Name");
        notificatinTableHeaders.add("Last Notification");
        return notificatinTableHeaders;
    }
    private void setTableEditors() {
        jTable1.setOpaque(false);
        jTable1.setShowVerticalLines(false);
        jTable1.setShowHorizontalLines(false);
        jTable1.setRowHeight(22);
        jTable1.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        jTable1.setFont(CoeusFontFactory.getNormalFont());
        JTableHeader header = jTable1.getTableHeader();
        header.setReorderingAllowed(false);
        TableColumn clmName = jTable1.getColumnModel().getColumn(0);
        clmName.setPreferredWidth(60);
        clmName.setMinWidth(60);
        clmName = jTable1.getColumnModel().getColumn(1);
        clmName.setPreferredWidth(170);
        clmName.setMinWidth(170);
        clmName = jTable1.getColumnModel().getColumn(2);
        clmName.setPreferredWidth(150);
        clmName.setMinWidth(150);
    }
    /** Table Model for the Table  */
    class PersonNotificationListTableModel extends DefaultTableModel {
        /** Vector of UnitMap beans */
        Vector mapBeans;
        /** Column Indexing */
        final int ID = 0;
        /** Column Indexing */
        final int TYPE = 1;
        /** Column Indexing */
        final int DESCRIPTION = 2;
        /** setting column Types */
        private Class columnTypes[] = {String.class, String.class, String.class};
        /** setting column Names */
        private String columnNames[] = {"Check Box", "Person Name", "Last Notification Date"};
        /** Constructor */
        PersonNotificationListTableModel() {
        }
        /** Return the ColumnClass for the Index
         *@param columnIndex int column Index for which the Column Class is required
         *@return Class
         */
        @Override
        public Class getColumnClass(int columnIndex) {
            return columnTypes[columnIndex];
        }
        /** Is the Cell Editable at rowIndex and columnIndex
         * @param rowIndex rowIndex
         * @param columnIndex columnIndex
         * @return if <true> the Cell is editable
         */
        @Override
        public boolean isCellEditable(int rowIndex, int columnIndex) {
            return false;
        }

        /** to get the Column Name for a columnIndex
         * @param columnIndex columnIndex for which Column Name is retrieved
         * @return String Column Name
         */
        @Override
        public String getColumnName(int columnIndex) {
            return columnNames[columnIndex];
        }

        /** get the Column Count
         * @return int Column Count
         */
        @Override
        public int getColumnCount() {
            return columnNames.length;
        }

        /** To get the RowCount
         * @return int Row Count
         */
        @Override
        public int getRowCount() {
            if (mapBeans == null) {
                return 0;
            }
            return mapBeans.size();
        }

        /** Set the Vector of beans
         * @param mapBeans Vector of unitMap beans
         */
        public void setData(Vector mapBeans) {
            this.mapBeans = mapBeans;
        }

        /** Method to get the value for a row and column
         * @param row int
         * @param column int
         * @return Object
         */
        @Override
        public Object getValueAt(int row, int column) {
            return null;
        }

        /** Method to set the value for a row and column
         * @param value Object
         * @param row int
         * @param column int
         */
        @Override
        public void setValueAt(Object value, int row, int column) {
        }
    }

    private void sendNotificationButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_sendNotificationButtonActionPerformed
        // TODO add your handling code here:
        sendPersonNotification();

    }//GEN-LAST:event_sendNotificationButtonActionPerformed

    private void closeButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_closeButtonActionPerformed
        // TODO add your handling code here:
        dlgNotificationForm.dispose();

    }//GEN-LAST:event_closeButtonActionPerformed

    private void selectAllCheckActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_selectAllCheckActionPerformed
        // TODO add your handling code here:
        selectAllPerson();
    }//GEN-LAST:event_selectAllCheckActionPerformed

    /**
     * Initialises the Dialog window used to show the form
     */
    public void initDialogWindow() {

        dlgNotificationForm = new CoeusDlgWindow(CoeusGuiConstants.getMDIForm(), title, true);
        dlgNotificationForm.getContentPane().add(this);
        dlgNotificationForm.pack();
        dlgNotificationForm.setFont(CoeusFontFactory.getLabelFont());

        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        Dimension dlgSize = dlgNotificationForm.getSize();
        dlgNotificationForm.setLocation(screenSize.width / 2 - (dlgSize.width / 2),
                screenSize.height / 2 - (dlgSize.height / 2));

        dlgNotificationForm.setResizable(false);

    }

    /**
     * Initialise the Form like registering listeners for components,setting the Form
     * data formatting the Componets based on initial data based on the parameters
     */
    public void initForm() {

        coeusMessageResources = CoeusMessageResources.getInstance();
        formatTable();
    }

    /** TableHeader Renderer instance */
    class TableHeaderRenderer extends DefaultTableCellRenderer {

        /** Label instance */
        private JLabel label;
        /** TableHeaderRenderer */
        TableHeaderRenderer() {
            label = new JLabel();
            label.setHorizontalAlignment(JLabel.CENTER);
            label.setFont(CoeusFontFactory.getLabelFont());
            label.setBorder(BorderFactory.createRaisedBevelBorder());
        }
    }

    /** Customizes the table */
    private void formatTable() {
        jTable1.getSelectionModel().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    }
    private void sendPersonNotification() {
        for (int i = 0; i < jTable1.getRowCount(); i++) {
            Boolean sendNotificationFlag = (Boolean) jTable1.getValueAt(i, 0);
            if (sendNotificationFlag) {
                personIdToSendNotification.add((String) personIDVector.get(i));
            }
        }
        if (personIdToSendNotification.size() > 0) {
            Vector requestParameter = new Vector();
            requestParameter.add(personIdToSendNotification);
            requestParameter.add(proposalNumber);
            requestParameter.add(protocolSequenceNumber);
            requestParameter.add(leadUnitNumber);
            RequesterBean requesterBean = new RequesterBean();
            requesterBean.setFunctionType(PROTOCOL_PERSON_SEND);
            requesterBean.setDataObjects(requestParameter);
            String connectTo="";
            if(module_code==9){
              connectTo = CoeusGuiConstants.CONNECTION_URL + iacucConURL;
            }else if(module_code==7){
              connectTo = CoeusGuiConstants.CONNECTION_URL + SUBMISSION_DETAILS_SERVLET;
            }
         
            AppletServletCommunicator comm = new AppletServletCommunicator(connectTo, requesterBean);
            comm.send();
            ResponderBean responderBean = comm.getResponse();
            try {
                if (responderBean.isSuccessfulResponse()) {
                    if (responderBean != null) {
                        int mailSentCount=Integer.parseInt(responderBean.getDataObject().toString());
                      if(mailSentCount>0){
                          CoeusOptionPane.showInfoDialog(" All Notifications are Sent.  ");
                      }
                      else{
                          CoeusOptionPane.showInfoDialog(" Unable to send the notification.  ");
                      }
                        dlgNotificationForm.dispose();
                    }
                }else{
                    CoeusOptionPane.showErrorDialog("Unable to send the email notifications.");
                    dlgNotificationForm.dispose();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        } else {
            CoeusOptionPane.showInfoDialog("  Please select a person from the list to Send Notification.  ");
        }
    }
    private void selectAllPerson() {
        if (selectAllCheck.isSelected()) {
            for (int i = 0; i < jTable1.getRowCount(); i++) {
                jTable1.setValueAt(new Boolean(true), i, 0);
            }
        } else {
            for (int i = 0; i < jTable1.getRowCount(); i++) {
                jTable1.setValueAt(new Boolean(false), i, 0);
            }
        }
    }

    /** Set the Form with data with data got from server */
    public void setFormData() {
        retrievePersonsToSendNotificationDetails();
        int rowCount = jTable1.getRowCount();

        if (personNotifyDetailsVector.size() > 0) {
            for (int i = 0; i < personNotifyDetailsVector.size(); i++) {
                ((DefaultTableModel) jTable1.getModel()).addRow((Vector) personNotifyDetailsVector.elementAt(i));
            }
            rowCount = jTable1.getRowCount() - 1;
            setTableEditors();
            jTable1.scrollRectToVisible(
                    jTable1.getCellRect(rowCount, 0, true));

            jTable1.setRowSelectionInterval(rowCount, rowCount);
            jTable1.requestFocusInWindow();
            jTable1.editCellAt(rowCount, 2);
        }

    }

    public void retrievePersonsToSendNotificationDetails() {
        Vector requestParameter = new Vector();
        requestParameter.add(proposalNumber);
        RequesterBean requesterBean = new RequesterBean();
        requesterBean.setFunctionType(GET_NOTIFY_DETAILS);
        requesterBean.setDataObject(proposalNumber);
        String connectTo = "";
        if(module_code==9){
          connectTo = CoeusGuiConstants.CONNECTION_URL + iacucConURL;
        }else if(module_code==7){
          connectTo = CoeusGuiConstants.CONNECTION_URL + SUBMISSION_DETAILS_SERVLET;
        }

        AppletServletCommunicator comm = new AppletServletCommunicator(connectTo, requesterBean);
        comm.send();
        ResponderBean responderBean = comm.getResponse();
        try {
            if (responderBean.isSuccessfulResponse()) {
                if (responderBean != null) {
                    Vector responseParameters = (Vector) responderBean.getDataObjects();
                    personNotifyDetailsVector = (Vector) responseParameters.get(0);
                    personIDVector = (Vector) responseParameters.get(1);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
    /** Get the MapDetails from server using Applet Servlet Communicator */
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton closeButton;
    private javax.swing.JPanel jBtnPanel;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jTable1;
    private javax.swing.JCheckBox selectAllCheck;
    private javax.swing.JLabel selectAllLabel;
    private javax.swing.JButton sendNotificationButton;
    // End of variables declaration//GEN-END:variables
}
