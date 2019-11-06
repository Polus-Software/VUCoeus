/*
 * RoutingAttachmentForm.java
 *
 * Created on May 16, 2011, 2:08 PM
 *
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 */

/* PMD check performed, and commented unused imports and variables on 02-JUNE-2011
 * by Manjunatha
 */

package edu.mit.coeus.routing.gui;

import edu.mit.coeus.bean.CoeusAttachmentBean;
import edu.mit.coeus.exception.CoeusException;
import edu.mit.coeus.gui.CoeusDlgWindow;
import edu.mit.coeus.gui.CoeusFontFactory;
import edu.mit.coeus.gui.CoeusMessageResources;
import edu.mit.coeus.gui.URLOpener;
import edu.mit.coeus.routing.bean.RoutingAttachmentBean;
import edu.mit.coeus.utils.AppletServletCommunicator;
import edu.mit.coeus.utils.CoeusGuiConstants;
import edu.mit.coeus.utils.CoeusOptionPane;
import edu.mit.coeus.utils.CoeusVector;
import edu.mit.coeus.utils.document.DocumentBean;
import edu.mit.coeus.utils.document.DocumentConstants;
import edu.mit.coeus.utils.documenttype.CoeusDocumentUtils;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import javax.swing.AbstractCellEditor;
import javax.swing.JButton;
import javax.swing.table.DefaultTableModel;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableColumn;
import edu.mit.coeus.brokers.*;

/**
 *
 * @author  manjunathabn
 */
public class RoutingAttachmentForm extends javax.swing.JPanel {
    public CoeusDlgWindow dlgWindow;
    private static String title = "Attachments";
    private AttachmentCellEditor attachmentCellEditor;
    private AttachmentCellRenderer attachmentCellRenderer;
    private static CoeusVector vecAttachmentsBean;
    private static int ROW_COUNT;
    private static int COLUMN_COUNT = 3;
    private CoeusMessageResources coeusMessageResources = CoeusMessageResources.getInstance();
    
    
    /**
     * Creates new form RoutingAttachmentForm
     */
    public RoutingAttachmentForm(int size) {
        ROW_COUNT = size;
        initComponents();
        DefaultTableModel tblModel = new DefaultTableModel(ROW_COUNT,COLUMN_COUNT) {
            public boolean isCellEditable(int row, int column) {
                if(column == 0)
                    return true;
                else
                    return false;
            }
        };
        tblAttachments.setModel(tblModel);
        registerComponents();
        dlgWindow = new CoeusDlgWindow(CoeusGuiConstants.getMDIForm(), title, true);
        dlgWindow.getContentPane().add(this);
        dlgWindow.pack();
        dlgWindow.setResizable(false);
        dlgWindow.setLocation(CoeusDlgWindow.CENTER);
    }
    
    /**
     * Method used to set the data to be shown.
     */
    public void setData(String userName, String userID, String action, CoeusVector vecAttachmentsBean) {
        lblUserName.setText(userName+" ("+userID+")");
        lblAction.setText(action);
        this.vecAttachmentsBean = vecAttachmentsBean;
        
        tblAttachments.getColumnModel().getColumn(0).setHeaderValue("");
        tblAttachments.getColumnModel().getColumn(1).setHeaderValue("Description");
        tblAttachments.getColumnModel().getColumn(2).setHeaderValue("");
        
        for(int i=0;i<vecAttachmentsBean.size();i++) {
            RoutingAttachmentBean routingAttachmentBean = (RoutingAttachmentBean)vecAttachmentsBean.get(i);
            tblAttachments.setValueAt(routingAttachmentBean.getDescription(),i,1);
            tblAttachments.setValueAt(routingAttachmentBean.getUpdateTimestamp(),i,2);
        }
    }
    
    /**
     * Method to display the form
     */
    public void display(){
        dlgWindow.setVisible(true);
    }
    
    /** This method is used to set the listeners to the components. */
    private void registerComponents(){
        attachmentCellEditor = new AttachmentCellEditor();
        attachmentCellRenderer = new AttachmentCellRenderer();
        
        JTableHeader tableHeader  = tblAttachments.getTableHeader();
        tableHeader.setReorderingAllowed(false);
        tableHeader.setFont(CoeusFontFactory.getLabelFont());
        tableHeader.setPreferredSize(new Dimension(0,22));
        tableHeader.setResizingAllowed(false);
        tblAttachments.setRowHeight(20);
        tblAttachments.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        
        TableColumn column = tblAttachments.getColumnModel().getColumn(0);
        column.setMinWidth(20);
        column.setPreferredWidth(20);
        column.setCellRenderer(attachmentCellRenderer);
        column.setCellEditor(attachmentCellEditor);
        
        column = tblAttachments.getColumnModel().getColumn(1);
        column.setMinWidth(353);
        column.setPreferredWidth(353);
        column.setCellRenderer(new DefaultTableCellRenderer());
        
        column = tblAttachments.getColumnModel().getColumn(2);
        column.setMinWidth(120);
        column.setPreferredWidth(120); //25
        column.setCellRenderer(new DefaultTableCellRenderer());
    }
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc=" Generated Code ">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        jPanel1 = new javax.swing.JPanel();
        lblUserName = new javax.swing.JLabel();
        lblAction = new javax.swing.JLabel();
        scrPnAttachments = new javax.swing.JScrollPane();
        tblAttachments = new javax.swing.JTable();
        btnClose = new javax.swing.JButton();

        setLayout(new java.awt.GridBagLayout());

        setMinimumSize(new java.awt.Dimension(615, 240));
        setPreferredSize(new java.awt.Dimension(615, 240));
        jPanel1.setLayout(new java.awt.GridBagLayout());

        jPanel1.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jPanel1.setMinimumSize(new java.awt.Dimension(520, 230));
        jPanel1.setPreferredSize(new java.awt.Dimension(520, 230));
        lblUserName.setFont(CoeusFontFactory.getLabelFont());
        lblUserName.setText("lblUserName");
        lblUserName.setMaximumSize(new java.awt.Dimension(150, 14));
        lblUserName.setMinimumSize(new java.awt.Dimension(90, 14));
        lblUserName.setPreferredSize(new java.awt.Dimension(90, 14));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.ipadx = 100;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(8, 0, 4, 30);
        jPanel1.add(lblUserName, gridBagConstraints);

        lblAction.setFont(CoeusFontFactory.getLabelFont());
        lblAction.setText("lblAction");
        lblAction.setMinimumSize(new java.awt.Dimension(50, 14));
        lblAction.setPreferredSize(new java.awt.Dimension(50, 14));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.ipadx = 70;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(8, 30, 4, 72);
        jPanel1.add(lblAction, gridBagConstraints);

        scrPnAttachments.setMinimumSize(new java.awt.Dimension(512, 182));
        scrPnAttachments.setPreferredSize(new java.awt.Dimension(512, 182));
        tblAttachments.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {

            }
        ));
        scrPnAttachments.setViewportView(tblAttachments);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(8, 0, 1, 0);
        jPanel1.add(scrPnAttachments, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.insets = new java.awt.Insets(18, 0, 16, 0);
        add(jPanel1, gridBagConstraints);

        btnClose.setFont(CoeusFontFactory.getLabelFont());
        btnClose.setText("Close");
        btnClose.setMaximumSize(new java.awt.Dimension(85, 23));
        btnClose.setMinimumSize(new java.awt.Dimension(85, 23));
        btnClose.setPreferredSize(new java.awt.Dimension(85, 23));
        btnClose.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCloseActionPerformed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(53, 3, 256, 3);
        add(btnClose, gridBagConstraints);

    }// </editor-fold>//GEN-END:initComponents
    
    /**
     * Method to perform action when button close
     * is clicked
     */
    private void btnCloseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCloseActionPerformed
// TODO add your handling code here:
        dlgWindow.setVisible(false);
    }//GEN-LAST:event_btnCloseActionPerformed
    
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnClose;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JLabel lblAction;
    private javax.swing.JLabel lblUserName;
    private javax.swing.JScrollPane scrPnAttachments;
    public static javax.swing.JTable tblAttachments;
    // End of variables declaration//GEN-END:variables
    
    
    class AttachmentCellRenderer extends DefaultTableCellRenderer{
        private JButton btnDetails;
        
        public AttachmentCellRenderer(){
            btnDetails = new JButton();
        }
        public java.awt.Component getTableCellRendererComponent(JTable table, Object value,
                boolean isSelected, boolean hasFocus, int row, int col){
            if(col==0){
                CoeusAttachmentBean attachment = (CoeusAttachmentBean)vecAttachmentsBean.get(row);
                CoeusDocumentUtils docTypeUtils  = CoeusDocumentUtils.getInstance();
                btnDetails.setIcon(docTypeUtils.getAttachmentIcon(attachment));
                return btnDetails;
            }
            return null;
        }
    }
    
    class AttachmentCellEditor extends AbstractCellEditor implements TableCellEditor,
            ActionListener{
        private JButton btnDetails;
        private int column;
        private JTable table;
        /* Creates a CostSharing Editor*/
        public AttachmentCellEditor() {
            btnDetails = new JButton();
            btnDetails.addActionListener(this);
        }
        
        public void actionPerformed(ActionEvent e){
            if(e.getSource().equals(btnDetails)){
                viewAttachment();
                tblAttachments.getCellEditor().stopCellEditing();
            }
        }
        
        /* Returns the CellEditor value*/
        public Object getCellEditorValue() {
            if(column == 0){
                return btnDetails;
            }
            return "";
        }
        
        
        /* returns the cellEditor component*/
        public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
            this.column = column;
            this.table = table;
            if(column == 0){
                CoeusAttachmentBean attachment = (CoeusAttachmentBean)vecAttachmentsBean.get(row);
                CoeusDocumentUtils docTypeUtils  = CoeusDocumentUtils.getInstance();
                btnDetails.setIcon(docTypeUtils.getAttachmentIcon(attachment));
                return btnDetails;
            }
            return null;
        }
    }
    
    
    /**
     * Method to view the attached document
     */
    public void viewAttachment(){
        int selectedRow = tblAttachments.getSelectedRow();
        if( selectedRow != -1){
            RoutingAttachmentBean routingAttachmentBean = (RoutingAttachmentBean)vecAttachmentsBean.get(selectedRow);
            DocumentBean documentBean = new DocumentBean();
            Map map = new HashMap();
            map.put("DATA", routingAttachmentBean);
            map.put("USER_ID", CoeusGuiConstants.getMDIForm().getUserId());
            map.put("FUNCTION_TYPE", "GET_ROUTING_ATTACHMENT");
            map.put(DocumentConstants.DOC_ON_URL_GENERATION, new Boolean(true));
            map.put(DocumentConstants.READER_CLASS, "edu.mit.coeus.routing.RoutingAttachmentsReader");
            documentBean.setParameterMap(map);
            RequesterBean requester = new RequesterBean();
            requester.setDataObject(documentBean);
            requester.setFunctionType(DocumentConstants.GENERATE_STREAM_URL);
            //For Streaming
            String STREAMING_SERVLET = CoeusGuiConstants.CONNECTION_URL + "/StreamingServlet";
            
            AppletServletCommunicator comm
                    = new AppletServletCommunicator(STREAMING_SERVLET, requester);
            comm.send();
            
            ResponderBean response = comm.getResponse();
            if(response.isSuccessfulResponse()){
                map = (Map)response.getDataObject();
                String reportUrl = (String)map.get(DocumentConstants.DOCUMENT_URL);
                reportUrl = reportUrl.replace('\\', '/') ;
                try {
                    URL urlObj = new URL(reportUrl);
                    URLOpener.openUrl(urlObj);
                } catch (MalformedURLException ex) {
                    ex.printStackTrace();
                } catch (CoeusException ex) {
                    ex.printStackTrace();
                }
            }else {
                CoeusOptionPane.showInfoDialog(coeusMessageResources.parseMessageKey(response.getMessage()));
            }
        }
    }
    
    
}
