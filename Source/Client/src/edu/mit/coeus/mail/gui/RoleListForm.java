/*
 * RoleListForm.java
 *
 * Created on June 8, 2007, 10:25 AM
 *
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 *
 */

package edu.mit.coeus.mail.gui;

import edu.mit.coeus.gui.CoeusAppletMDIForm;
import edu.mit.coeus.gui.CoeusDlgWindow;
import edu.mit.coeus.gui.CoeusFontFactory;
import edu.mit.coeus.mail.bean.PersonRecipientBean;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Vector;
import javax.swing.AbstractAction;
import javax.swing.DefaultListSelectionModel;
import javax.swing.JTable;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumn;

/**
 *
 * @author  talarianand
 */
public class RoleListForm extends javax.swing.JPanel implements ActionListener {
    
    private Vector vecPersonInfo;
    
    private static final int PERSON_NAME = 0;
    
    private static final int MAIL_ID = 1;
    
    private static final String EMPTY_STRING = "";
    
    private CoeusDlgWindow dlgRoleList;
    
    private CoeusAppletMDIForm mdiForm;
    
    private static final String WINDOW_TITLE = "Person Details";
    
    private static final int WIDTH = 350;
    
    private static final int HEIGHT = 315;
    
    private PersonInfoTableModel personTableModel;
    
    /** Creates new form RoleListForm */
    public RoleListForm(CoeusAppletMDIForm mdiForm, Vector vecData) {
        this.mdiForm = mdiForm;
        this.vecPersonInfo = vecData;
        initComponents();
        registerComponents();
        postInitComponents();
        setTableColumn();
    }
    
    public void registerComponents() {
        personTableModel = new PersonInfoTableModel();
        btnClose.addActionListener(this);
        tblRoleList.setModel(personTableModel);
    }
    
    private void postInitComponents() {
        dlgRoleList = new CoeusDlgWindow(mdiForm,true);
        dlgRoleList.setResizable(false);
        dlgRoleList.setModal(true);
        dlgRoleList.getContentPane().add(this);
        dlgRoleList.setFont(CoeusFontFactory.getLabelFont());
        dlgRoleList.setDefaultCloseOperation(CoeusDlgWindow.DO_NOTHING_ON_CLOSE);
        dlgRoleList.setSize(WIDTH, HEIGHT);
        dlgRoleList.setTitle(WINDOW_TITLE);
        
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        Dimension dlgSize = dlgRoleList.getSize();
        dlgRoleList.setLocation(screenSize.width/2 - (dlgSize.width/2),
        screenSize.height/2 - (dlgSize.height/2));
        
        dlgRoleList.addWindowListener(new WindowAdapter(){
            
            public void windowClosing(WindowEvent we) {
                    performCloseAction();
                    return;
            }
        });
        
        dlgRoleList.addEscapeKeyListener(
        new AbstractAction("escPressed"){
            public void actionPerformed(ActionEvent ae) {
                    performCloseAction();
                    return;
            }
        });
        
        dlgRoleList.addComponentListener(
        new ComponentAdapter(){
            public void componentShown(ComponentEvent e){
                setWindowFocus();
            }
        });
    }
    
    /**
     * Used to set the columns to table
     */
    private void setTableColumn() {
        JTableHeader tableHeader = tblRoleList.getTableHeader();
        tableHeader.setReorderingAllowed(false);
        tableHeader.setFont(CoeusFontFactory.getLabelFont());
        tblRoleList.setSelectionMode(DefaultListSelectionModel.SINGLE_SELECTION);
        tblRoleList.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        tblRoleList.setCellSelectionEnabled(false);
        
        TableColumn column = tblRoleList.getColumnModel().getColumn(PERSON_NAME);
        column.setPreferredWidth(100);
        column.setResizable(true);
        
        column = tblRoleList.getColumnModel().getColumn(MAIL_ID);
        column.setPreferredWidth(150);
        column.setResizable(true);
        
        java.awt.Color bgColor = (java.awt.Color)javax.swing.UIManager.getDefaults().get("Panel.background");
        tblRoleList.setBackground(bgColor);
    }
    
    public void actionPerformed(ActionEvent actionEvent) {
        Object source = actionEvent.getSource();
        if(source.equals(btnClose)) {
            performCloseAction();
        }
    }
    
    private void performCloseAction() {
        dlgRoleList.setVisible(false);
    }
    
    private void setWindowFocus() {
        btnClose.requestFocusInWindow();
    }
    
    public void display() {
        dlgRoleList.setVisible(true);
    }
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc=" Generated Code ">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        pnlMain = new javax.swing.JPanel();
        scrPnlList = new javax.swing.JScrollPane();
        tblRoleList = new edu.mit.coeus.utils.table.CoeusTable();
        btnClose = new javax.swing.JButton();

        setLayout(new java.awt.GridBagLayout());

        pnlMain.setLayout(new java.awt.GridBagLayout());

        pnlMain.setMinimumSize(new java.awt.Dimension(500, 300));
        pnlMain.setPreferredSize(new java.awt.Dimension(340, 280));
        scrPnlList.setMinimumSize(new java.awt.Dimension(500, 500));
        scrPnlList.setPreferredSize(new java.awt.Dimension(270, 270));
        tblRoleList.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        scrPnlList.setViewportView(tblRoleList);

        pnlMain.add(scrPnlList, new java.awt.GridBagConstraints());

        btnClose.setFont(CoeusFontFactory.getLabelFont());
        btnClose.setMnemonic('C');
        btnClose.setText("Close");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTH;
        pnlMain.add(btnClose, gridBagConstraints);

        add(pnlMain, new java.awt.GridBagConstraints());

    }// </editor-fold>//GEN-END:initComponents
    
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    public javax.swing.JButton btnClose;
    public javax.swing.JPanel pnlMain;
    public javax.swing.JScrollPane scrPnlList;
    public edu.mit.coeus.utils.table.CoeusTable tblRoleList;
    // End of variables declaration//GEN-END:variables
    
    class PersonInfoTableModel extends AbstractTableModel {
        String colNames[] = {"Person Name", "Mail Id"};
        Class[] colTypes = new Class[] {String.class,String.class};
        
        PersonInfoTableModel() {
        }
        
        public boolean isCellEditable() {
            return false;
        }
        
        public int getColumnCount() {
            return colNames.length;
        }
        
        public Class getColumnClass(int columnIndex) {
            return colTypes[columnIndex];
        }
        
        public int getRowCount() {
            return vecPersonInfo.size();  
        }
        
        public String getColumnName(int column) {
            return colNames[column];
        }
        //COEUSDEV-75:Rework email engine so the email body is picked up from one place
        public Object getValueAt(int row,int column) {
            PersonRecipientBean bean = (PersonRecipientBean) vecPersonInfo.get(row);
            switch(column) {
                case PERSON_NAME:
                    return bean.getPersonName();
                case MAIL_ID:
                    return bean.getEmailId();
            }
            return EMPTY_STRING;
        }
        
        public void addRow() {
        }
        
        public void setValueAt(Object value,int row,int column) {
            if(vecPersonInfo== null) {
                return;
            }
            PersonRecipientBean personRecipientBean = (PersonRecipientBean) vecPersonInfo.get(row);
            switch(column) {
                case PERSON_NAME:
                   personRecipientBean.setPersonName(value.toString());
                case MAIL_ID:
                    personRecipientBean.setEmailId(value.toString());
            }
        }
        //COEUSDEV-75:End
    }
}
