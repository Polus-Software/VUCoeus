/** Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 */

package edu.mit.coeus.instprop.gui;

import java.awt.Toolkit;
import java.awt.Dimension;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentAdapter;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.table.JTableHeader;
import javax.swing.ListSelectionModel;
import javax.swing.table.TableColumnModel;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;

import edu.mit.coeus.gui.CoeusFontFactory;
import edu.mit.coeus.gui.CoeusDlgWindow;
import edu.mit.coeus.utils.ScreenFocusTraversalPolicy;
import edu.mit.coeus.gui.CoeusMessageResources;
import edu.mit.coeus.utils.CoeusOptionPane;
import edu.mit.coeus.utils.CoeusGuiConstants;
import edu.mit.coeus.utils.CoeusVector;
import edu.mit.coeus.utils.TypeConstants;
import edu.mit.coeus.exception.CoeusException;
import edu.mit.coeus.utils.query.QueryEngine;
import edu.mit.coeus.award.bean.AwardFundingProposalBean;
import edu.mit.coeus.instprop.bean.InstituteProposalBean;

/**
 * AwardListForProposal.java
 * Created on May 17, 2004, 6:35 PM
 * @author  Vyjayanthi
 */
public class AwardListForProposal extends javax.swing.JComponent
implements ActionListener {
    
    CoeusDlgWindow dlgAwardListForProposal;

    /** Holds an instance of <CODE>CoeusMessageResources</CODE> 
     * for reading message Properties
     */
    private CoeusMessageResources coeusMessageResources = CoeusMessageResources.getInstance();

    /** Holds an instance of the query engine
     */
    private QueryEngine queryEngine = QueryEngine.getInstance();
    
    /** Holds the awards associated with the institute proposal */
    private CoeusVector cvAward;
    
    private String queryKey;
    
    private AwardTableModel awardTableModel;
    
    private InstituteProposalBean instituteProposalBean;
    
    /** Used to indicate whether the screen has to be closed or remain open
     * Holds true if dialog should not be disposed, false otherwise */
    private boolean cancelled;
    
    private static final String EMPTY_STRING = "";
    
    private static final int WIDTH = 535;
    private static final int HEIGHT = 355;
    private static final String REMOVE_AWARDS = "instPropo_exceptionCode.1003";
    private static final int AWARD_COLUMN = 0;
    private static final int AWARD_SEQ_COLUMN = 1;
    private static final int PROP_SEQ_COLUMN = 2;
    private static final int ACCOUNT_NUM_COLUMN = 3;
    private static final int PENDING_STATUS = 1;
    private static final String PENDING = "Pending";
    
    private static final String MIT_AWARD_NUMBER_FIELD = "mitAwardNumber";
    private static final String SEQUENCE_NUMBER_FIELD = "sequenceNumber";
    private static final String PROP_SEQ_NUMBER_FIELD = "proposalSequenceNumber";
    private static final String ACCOUNT_NUMBER_FIELD = "awardAccountNumber";
    
    private static final String AWARD_NUMBER = "Award Number";
    private static final String AWARD_SEQ = "Award Sequence";
    private static final String PROP_SEQ = "Prop Sequence";
//JM    private static final String ACCOUNT_NUMBER = "Account Number";
    private static final String ACCOUNT_NUMBER = "Center Number"; //JM 5-25-2011 updated to Center per 4.4.2
    
    private static final String AWARD_COL = "0";
    private static final String AWARD_SEQ_COL = "1";
    private static final String PROP_SEQ_COL = "2";
    private static final String ACCOUNT_NUM_COL = "3";
    
    /** Creates new form AwardListForProposal
     * @param instituteProposalBean
     * @param queryKey to query from the query engine
     */
    public AwardListForProposal(InstituteProposalBean instituteProposalBean, String queryKey) {
        initComponents();
        postInitComponents();
        this.queryKey = queryKey;
        this.instituteProposalBean = instituteProposalBean;
        setFormData();
    }
    
    /** Method to set all listeners and set default properties */
    private void postInitComponents(){
        awardTableModel = new AwardTableModel();
        dlgAwardListForProposal = new CoeusDlgWindow(CoeusGuiConstants.getMDIForm(), true);
        dlgAwardListForProposal.getContentPane().add(this);
        dlgAwardListForProposal.setResizable(false);
        dlgAwardListForProposal.setTitle("Award List for Proposal");
        dlgAwardListForProposal.setSize(WIDTH, HEIGHT);
        
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        Dimension dlgSize = dlgAwardListForProposal.getSize();
        dlgAwardListForProposal.setLocation(screenSize.width/2 - (dlgSize.width/2),
        screenSize.height/2 - (dlgSize.height/2));

        dlgAwardListForProposal.addComponentListener(
            new ComponentAdapter(){
                public void componentShown(ComponentEvent e){
                    btnSelectAll.requestFocus();
                }
        });
        
        /** Code for focus traversal - start */
        
        java.awt.Component[] components = { tblAward, btnOk, btnCancel, btnSelectAll};
        ScreenFocusTraversalPolicy traversePolicy = new ScreenFocusTraversalPolicy( components );
        this.setFocusTraversalPolicy(traversePolicy);
        this.setFocusCycleRoot(true);
        
        /** Code for focus traversal - end */
        
        //Add Listeners
        btnOk.addActionListener(this);
        btnCancel.addActionListener(this);
        btnSelectAll.addActionListener(this);
       
    }
    
    /** To set the data to the form
     */
    private void setFormData(){
        lblHeading.setText("The proposal '" + 
        instituteProposalBean.getProposalNumber() + "' is funding following awards.");
        
        try{
            cvAward = queryEngine.executeQuery(queryKey, AwardFundingProposalBean.class, 
            CoeusVector.FILTER_ACTIVE_BEANS);
        }catch (CoeusException coeusException){
            coeusException.printStackTrace();
        }        
        
        if( cvAward == null ) return ;
        
        //Set the table data
        awardTableModel.setData(cvAward);
        tblAward.setModel(awardTableModel);
        
        JTableHeader tableHeader = tblAward.getTableHeader();
        tableHeader.addMouseListener(new ColumnHeaderListener());
        
        //Set table headers properties
        tableHeader.setFont(CoeusFontFactory.getLabelFont());
        tableHeader.setReorderingAllowed(false);
        tblAward.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        
        TableColumn tableColumn = tblAward.getColumnModel().getColumn(AWARD_COLUMN);
        tableColumn.setMinWidth(100);
        tableColumn.setPreferredWidth(100);
        
        tableColumn = tblAward.getColumnModel().getColumn(AWARD_SEQ_COLUMN);
        tableColumn.setMinWidth(120);
        tableColumn.setPreferredWidth(120);

        tableColumn = tblAward.getColumnModel().getColumn(PROP_SEQ_COLUMN);
        tableColumn.setMinWidth(100);
        tableColumn.setPreferredWidth(100);

        tableColumn = tblAward.getColumnModel().getColumn(ACCOUNT_NUM_COLUMN);
        tableColumn.setMinWidth(105);
        tableColumn.setPreferredWidth(105);
    }
    
    /** To display the screen
     * @return instituteProposalBean
     */
    public InstituteProposalBean display(){
        if( cvAward != null && cvAward.size() == 0 ){
            instituteProposalBean.setStatusCode(PENDING_STATUS);
            instituteProposalBean.setStatusDescription(PENDING);
            if( instituteProposalBean.getAcType() == null ){
                instituteProposalBean.setAcType(TypeConstants.UPDATE_RECORD);
            }
        }else{
            dlgAwardListForProposal.show();
        }
        return instituteProposalBean;
    }
    
    /** This method triggers all actions based on the event occured
     * @param actionEvent takes the actionEvent
     */
    public void actionPerformed(ActionEvent actionEvent) {
        Object source = actionEvent.getSource();
        if( source.equals(btnOk) ){
            deleteAwards();
            if( !cancelled ){
                dlgAwardListForProposal.dispose();
            }
        }else if( source.equals(btnCancel) ){
            dlgAwardListForProposal.dispose();
        }else if( source.equals(btnSelectAll) ){
            if( tblAward.getRowCount() != 0 ){
                tblAward.setRowSelectionInterval(0, tblAward.getRowCount() - 1);
            }
        }        
    }
    
    /** Deletes the selected awards in the table
     */
    private void deleteAwards(){
        int[] selectedRows = tblAward.getSelectedRows();
        if( selectedRows.length <= 0 ){
            //Reset the flag to close the screen
            cancelled = false;
            return ;
        }
        int option = CoeusOptionPane.showQuestionDialog(
            coeusMessageResources.parseMessageKey(REMOVE_AWARDS), 
            CoeusOptionPane.OPTION_YES_NO_CANCEL, CoeusOptionPane.DEFAULT_YES);
        switch( option ){
            case JOptionPane.YES_OPTION:
                try{                    
                    for( int index = 0; index < selectedRows.length; index++ ){
                        AwardFundingProposalBean fundingProposalBean = 
                            (AwardFundingProposalBean)cvAward.get(selectedRows[index]);
                        fundingProposalBean.setAcType(TypeConstants.DELETE_RECORD);
                        queryEngine.delete(queryKey, fundingProposalBean);
                    }
                    
                }catch (CoeusException coeusException){
                    coeusException.printStackTrace();
                }
                
                if( cvAward.size() == selectedRows.length ){
                    //Update status to pending and disable the menu item Unlock Proposal
                    instituteProposalBean.setStatusCode(PENDING_STATUS);
                    instituteProposalBean.setStatusDescription(PENDING);
                    if( instituteProposalBean.getAcType() == null ){
                        instituteProposalBean.setAcType(TypeConstants.UPDATE_RECORD);
                    }
                    try{
                        queryEngine.update(queryKey, instituteProposalBean);
                    }catch (CoeusException coeusException){
                        coeusException.printStackTrace();
                    }
                }
                //Reset the flag to close the screen
                cancelled = false;
                break;
            case JOptionPane.NO_OPTION:
                //Reset the flag to close the screen
                cancelled = false;
                break;
            case JOptionPane.CANCEL_OPTION:
                //Set the flag to keep the screen open
                cancelled = true;
                break;
        }
        
    }
    
    /** This class will sort the column values in ascending and descending order
     *based on number of clicks.
     */
    
    public class ColumnHeaderListener extends java.awt.event.MouseAdapter {
        String nameBeanId [][] ={
            {AWARD_COL, MIT_AWARD_NUMBER_FIELD},
            {AWARD_SEQ_COL, SEQUENCE_NUMBER_FIELD},
            {PROP_SEQ_COL, PROP_SEQ_NUMBER_FIELD},
            {ACCOUNT_NUM_COL, ACCOUNT_NUMBER_FIELD}
        };
        boolean sort = true;
   
        public void mouseClicked(java.awt.event.MouseEvent evt) {
            try {
                
                JTable table = ((JTableHeader)evt.getSource()).getTable();
                TableColumnModel colModel = table.getColumnModel();
                
                // The index of the column whose header was clicked
                int vColIndex = colModel.getColumnIndexAtX(evt.getX());
                if(cvAward != null && cvAward.size() > 0 &&
                nameBeanId [vColIndex][1].length() > 1 ){
                    cvAward.sort(nameBeanId [vColIndex][1],sort,true);
                    if(sort){
                        sort = false;
                    }else{
                        sort = true;
                    }
                    ((DefaultTableModel)tblAward.getModel()).fireTableRowsUpdated(0, tblAward.getRowCount());
                }
            } catch(Exception exception) {
                exception.printStackTrace();
            }
        }
    }// End of ColumnHeaderListener
    
    //Inner class AwardTableModel - Start
    class AwardTableModel extends DefaultTableModel {
        String colNames[] = {AWARD_NUMBER, AWARD_SEQ, PROP_SEQ, ACCOUNT_NUMBER};
        private CoeusVector cvData;
        
        Class[] types = new Class [] {
            String.class,String.class,String.class,String.class
        };
        
        public Class getColumnClass(int columnIndex) {
            return types [columnIndex];
        }
        
        public boolean isCellEditable(int row, int col){
            return false;
        }
        
        public void setData(CoeusVector cvData){
            this.cvData = cvData;
        }
        
        public Object getValueAt(int row, int column) {
            AwardFundingProposalBean fundingProposalBean = 
                (AwardFundingProposalBean)cvData.get(row);
            switch(column){
                case AWARD_COLUMN:
                    return fundingProposalBean.getMitAwardNumber();
                case AWARD_SEQ_COLUMN:
                    return new Integer(fundingProposalBean.getSequenceNumber());
                case PROP_SEQ_COLUMN:
                    return new Integer(fundingProposalBean.getProposalSequenceNumber());
                case ACCOUNT_NUM_COLUMN:
                    return fundingProposalBean.getAwardAccountNumber();
            }
            return EMPTY_STRING;
        }
        
        public String getColumnName(int column) {
            return colNames[column];
        }
        
        public int getColumnCount() {
            return colNames.length;
        }
        
        public int getRowCount() {
            if( cvData == null ) return 0;
            return cvData.size();
        }
        
    }//Inner class AwardTableModel - End
    
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    private void initComponents() {//GEN-BEGIN:initComponents
        java.awt.GridBagConstraints gridBagConstraints;

        lblHeading = new javax.swing.JLabel();
        btnOk = new javax.swing.JButton();
        btnCancel = new javax.swing.JButton();
        btnSelectAll = new javax.swing.JButton();
        scrPnAward = new javax.swing.JScrollPane();
        tblAward = new javax.swing.JTable();

        setLayout(new java.awt.GridBagLayout());

        lblHeading.setFont(CoeusFontFactory.getLabelFont());
        lblHeading.setText("The proposal");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 0);
        add(lblHeading, gridBagConstraints);

        btnOk.setFont(CoeusFontFactory.getLabelFont());
        btnOk.setMnemonic('O');
        btnOk.setText("OK");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTH;
        gridBagConstraints.insets = new java.awt.Insets(0, 5, 5, 5);
        add(btnOk, gridBagConstraints);

        btnCancel.setFont(CoeusFontFactory.getLabelFont());
        btnCancel.setMnemonic('C');
        btnCancel.setText("Cancel");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTH;
        gridBagConstraints.insets = new java.awt.Insets(0, 5, 5, 5);
        add(btnCancel, gridBagConstraints);

        btnSelectAll.setFont(CoeusFontFactory.getLabelFont());
        btnSelectAll.setMnemonic('A');
        btnSelectAll.setText("Select All");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTH;
        gridBagConstraints.insets = new java.awt.Insets(0, 5, 5, 5);
        add(btnSelectAll, gridBagConstraints);

        scrPnAward.setMinimumSize(new java.awt.Dimension(430, 300));
        scrPnAward.setPreferredSize(new java.awt.Dimension(430, 300));
        tblAward.setBackground(javax.swing.UIManager.getDefaults().getColor("Panel.background"));
        tblAward.setFont(CoeusFontFactory.getNormalFont());
        tblAward.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Award Number", "Award Sequence", "Prop Sequence", "Account Number"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tblAward.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
        tblAward.setAutoscrolls(false);
        scrPnAward.setViewportView(tblAward);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridheight = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.insets = new java.awt.Insets(0, 5, 5, 0);
        add(scrPnAward, gridBagConstraints);

    }//GEN-END:initComponents

    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    public javax.swing.JButton btnCancel;
    public javax.swing.JButton btnOk;
    public javax.swing.JButton btnSelectAll;
    public javax.swing.JLabel lblHeading;
    public javax.swing.JScrollPane scrPnAward;
    public javax.swing.JTable tblAward;
    // End of variables declaration//GEN-END:variables
    
}
