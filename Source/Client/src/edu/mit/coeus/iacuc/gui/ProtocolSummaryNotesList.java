/*
 * ProtocolSummaryNotesList.java
 *
 * Created on September 6, 2003, 11:46 AM
 */

package edu.mit.coeus.iacuc.gui;

import edu.mit.coeus.gui.CoeusFontFactory;
import edu.mit.coeus.gui.CoeusDlgWindow;

import edu.mit.coeus.iacuc.bean.ProtocolNotepadBean;
import edu.mit.coeus.utils.CoeusGuiConstants;
import edu.mit.coeus.utils.CoeusDateFormat;
import edu.mit.coeus.utils.ScreenFocusTraversalPolicy;
import edu.mit.coeus.utils.UserUtils;

import java.awt.*;
import java.util.Vector;
import java.util.Enumeration;

import javax.swing.UIManager;
import javax.swing.ListSelectionModel;
import javax.swing.table.TableColumn;
import javax.swing.table.DefaultTableModel;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
/**
 *
 * @author  ravikanth
 */
public class ProtocolSummaryNotesList extends edu.mit.coeus.gui.CoeusDlgWindow 
    implements ActionListener {
    
    private Vector notesList;
    ProtocolNewNotes protocolNewNotes ;
    
    private static final Color DEFAULT_TABLE_SELECTION_COLOR = (Color)UIManager.getDefaults().get("Table.selectionBackground");

    /** Creates new form ProtocolSummaryNotesList */
    public ProtocolSummaryNotesList(Vector notesList) {
        super(CoeusGuiConstants.getMDIForm(), "Protocol Notes",true);
        this.notesList = notesList;
        
    }

    public void showForm() {
        initComponents();
        //Modified by Vyjayanthi on 13/01/2004 to change the selection color
        //Added by Amit 11/19/2003        
            java.awt.Color bgListColor = (java.awt.Color)javax.swing.UIManager.getDefaults().get("Panel.background");
        
            tblNotes.setBackground(bgListColor);    
//            tblNotes.setSelectionBackground(bgListColor ); //Commented by Vyjayanthi
//            tblNotes.setSelectionForeground(java.awt.Color.black); //Commented by Vyjayanthi
            tblNotes.setSelectionBackground(DEFAULT_TABLE_SELECTION_COLOR); //Added by Vyjayanthi
            tblNotes.setSelectionForeground(Color.WHITE); //Added by Vyjayanthi
        //end Amit   

        pack();
        setFormData();
        btnView.addActionListener( this );
        btnClose.addActionListener( this );
        tblNotes.getSelectionModel().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tblNotes.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent me) {
                if (me.getClickCount() == 2) {
                    try {
                        int selectedRow = tblNotes.getSelectedRow();
                        if( selectedRow != -1 ) {
                            Integer tblRowSeqNumber = (Integer)tblNotes.getValueAt(selectedRow, 4);
                            Integer tblRowEntryNumber = (Integer)tblNotes.getValueAt(selectedRow, 5);
                            showProtocolNote(tblRowSeqNumber, tblRowEntryNumber);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        
        setResizable( false );
        setLocation(CoeusDlgWindow.CENTER);
        
        tblNotes.requestFocusInWindow();
        //Added by Vyjayanthi 21/12/2003 - Start
        java.awt.Component[] components = {tblNotes, btnView, btnClose };
        ScreenFocusTraversalPolicy  traversePolicy = new ScreenFocusTraversalPolicy( components );
        pnlMain.setFocusTraversalPolicy(traversePolicy);
        pnlMain.setFocusCycleRoot(true);
        if( tblNotes.getRowCount() > 0){
            tblNotes.setRowSelectionInterval(0,0);
        }
        //End
        
        setVisible(true);
    }
    
    private void showProtocolNote(Integer tblRowSeqNumber, Integer tblRowEntryNumber) {
        int newVecSize = notesList.size();
        ProtocolNotepadBean selProtoNotepadBean=null;
        for( int indx = 0 ; indx < newVecSize; indx++) {
            ProtocolNotepadBean protocolNotepadBean=null;
            protocolNotepadBean = 
                (ProtocolNotepadBean) notesList.elementAt( indx );
            if((tblRowEntryNumber.intValue()) == (protocolNotepadBean.getEntryNumber())){
                    selProtoNotepadBean = protocolNotepadBean;
            }
        } //end for
        if (selProtoNotepadBean != null) {
            //Get Database Timestamp
            //java.sql.Timestamp dbTimeStamp = Utils.getDBTimeStamp();
            //selProtoNotepadBean.setUpdateTimestamp(dbTimeStamp);
            
            protocolNewNotes = new ProtocolNewNotes(CoeusGuiConstants.getMDIForm(),
                                selProtoNotepadBean);
            protocolNewNotes.showProtocolNewNote();
        }
    }
    
    public void actionPerformed( ActionEvent ae ) {
        Object source = ae.getSource();
        if( source.equals( btnClose ) ) {
            this.dispose();
        }else if ( source.equals( btnView ) ){
            try {
                int selectedRow = tblNotes.getSelectedRow();
                if( selectedRow != -1) {
                    Integer tblRowSeqNumber = (Integer)tblNotes.getValueAt(selectedRow, 4);
                    Integer tblRowEntryNumber = (Integer)tblNotes.getValueAt(selectedRow, 5);
                    showProtocolNote(tblRowSeqNumber, tblRowEntryNumber);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    
    private void setEditors(){
         
        TableColumn column = tblNotes.getColumnModel().getColumn(0);
        column.setPreferredWidth(125);
        column.setMinWidth(125);
        
        column = tblNotes.getColumnModel().getColumn(1);
        /*
         * UserId to UserName Enhancement - Start
         * Modified the width of the user id to display username
         */      
//        column.setPreferredWidth(75);
//        column.setMinWidth(75);
        column.setPreferredWidth(97);
        column.setMinWidth(97);
        //UserId to UserName Enhancement - End

        
        column = tblNotes.getColumnModel().getColumn(2);
        column.setPreferredWidth(132);
        column.setMinWidth(132);
        
        column = tblNotes.getColumnModel().getColumn(3);
        column.setPreferredWidth(74);
        column.setMinWidth(74);
        
        column = tblNotes.getColumnModel().getColumn(4);
        column.setMinWidth(0); //Hidden column
        column.setMaxWidth(0);
        column.setPreferredWidth(0);
        
        column = tblNotes.getColumnModel().getColumn(5); 
        column.setMinWidth(0); //Hidden column
        column.setMaxWidth(0);
        column.setPreferredWidth(0);
        
        tblNotes.getTableHeader().setReorderingAllowed( false );
        tblNotes.getTableHeader().setResizingAllowed(true);
        tblNotes.setFont(CoeusFontFactory.getNormalFont());
        tblNotes.getTableHeader().setFont(CoeusFontFactory.getLabelFont());
    }
    /** This method uses the notes Vector for the protocol
     * protoVecNotes and displays the contents of the vector in the table
     *
     */
    private void setFormData(){
        if ((notesList != null) && ((notesList.size())>0)){
            try{
                int dataSize = notesList.size();
                ProtocolNotepadBean protocolNotepadBean=null;
                Vector tableData = new Vector() ;
                for( int indx = 0 ; indx < dataSize; indx++) {
                    protocolNotepadBean = 
                        (ProtocolNotepadBean) notesList.elementAt( indx );
                    
                    Vector tableRow = new Vector();
                    if(protocolNotepadBean != null){
                        tableRow.addElement( protocolNotepadBean.getComments() == null ? "" : protocolNotepadBean.getComments());
//                        tableRow.addElement( protocolNotepadBean.getUpdateUser() == null ? "" : protocolNotepadBean.getUpdateUser());
                        /*
                         * UserID to UserName Enhancement - Start
                         * Added UserUtils class to change userid to username
                         */
                        tableRow.addElement( protocolNotepadBean.getUpdateUser() == null ? "" : UserUtils.getDisplayName(protocolNotepadBean.getUpdateUser()));
                        // UserId to UserName Enhancement - End
                        //tableRow.addElement( protocolNotepadBean.getUpdateTimestamp() == null ? "" : protocolNotepadBean.getUpdateTimestamp());
                        tableRow.addElement( protocolNotepadBean.getUpdateTimestamp() == null ?
                        "" : CoeusDateFormat.format(protocolNotepadBean.getUpdateTimestamp().toString()));
                        tableRow.addElement( new Boolean(protocolNotepadBean.isRestrictedFlag()));
                        //Integer seqNumInt = Integer(1);
                        Integer seqNumInt = new Integer(protocolNotepadBean.getSequenceNumber());
                        tableRow.addElement( seqNumInt );
                        Integer entNumInt = new Integer(protocolNotepadBean.getEntryNumber());
                        tableRow.addElement( entNumInt );
                    }
                    tableData.addElement( tableRow );
                }
                ((DefaultTableModel)tblNotes.getModel()).setDataVector(tableData, getColumnNames() );
                ((DefaultTableModel)tblNotes.getModel()).fireTableDataChanged();
                setEditors();
            }catch(Exception e){
                e.printStackTrace();
            }
        } else {
        }
    }

    private Vector getColumnNames(){
        Enumeration enumColNames = tblNotes.getColumnModel().getColumns();
        Vector vecColNames = new Vector();
        while(enumColNames.hasMoreElements()){
            String strName = (String)((TableColumn)
            enumColNames.nextElement()).getHeaderValue();
            vecColNames.addElement(strName);
        }
        return vecColNames;
    }
    
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    private void initComponents() {//GEN-BEGIN:initComponents
        java.awt.GridBagConstraints gridBagConstraints;

        pnlMain = new javax.swing.JPanel();
        scrPnNotes = new javax.swing.JScrollPane();
        tblNotes = new javax.swing.JTable();
        btnView = new javax.swing.JButton();
        btnClose = new javax.swing.JButton();

        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                closeDialog(evt);
            }
        });

        pnlMain.setLayout(new java.awt.GridBagLayout());

        scrPnNotes.setMinimumSize(new java.awt.Dimension(300, 200));
        tblNotes.setFont(CoeusFontFactory.getNormalFont());
        tblNotes.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Comment", "By", "Time", "Restricted", "SeqNumber", "EntryNumber"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.Boolean.class, java.lang.Integer.class, java.lang.Integer.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tblNotes.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
        scrPnNotes.setViewportView(tblNotes);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridheight = 10;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.insets = new java.awt.Insets(10, 10, 10, 0);
        pnlMain.add(scrPnNotes, gridBagConstraints);

        btnView.setFont(CoeusFontFactory.getLabelFont());
        btnView.setMnemonic('V');
        btnView.setText("View");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(10, 10, 0, 10);
        pnlMain.add(btnView, gridBagConstraints);

        btnClose.setFont(CoeusFontFactory.getLabelFont());
        btnClose.setMnemonic('C');
        btnClose.setText("Close");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.insets = new java.awt.Insets(10, 10, 10, 10);
        pnlMain.add(btnClose, gridBagConstraints);

        getContentPane().add(pnlMain, java.awt.BorderLayout.CENTER);

    }//GEN-END:initComponents
    
    /** Closes the dialog */
    private void closeDialog(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_closeDialog
        setVisible(false);
        dispose();
    }//GEN-LAST:event_closeDialog
    

    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnClose;
    private javax.swing.JButton btnView;
    private javax.swing.JPanel pnlMain;
    private javax.swing.JScrollPane scrPnNotes;
    private javax.swing.JTable tblNotes;
    // End of variables declaration//GEN-END:variables
    
}
