/*
 * RoleModuleForm.java
 *
 * Created on October 17, 2007, 1:18 PM
 */

package edu.mit.coeus.codetable.client;

import edu.mit.coeus.brokers.RequesterBean;
import edu.mit.coeus.brokers.ResponderBean;
import edu.mit.coeus.exception.CoeusClientException;
import edu.mit.coeus.exception.CoeusException;
import edu.mit.coeus.gui.CoeusDlgWindow;
import edu.mit.coeus.gui.CoeusFontFactory;
//import edu.mit.coeus.mailaction.bean.MailActionInfoBean;
import edu.mit.coeus.personroles.bean.PersonRoleInfoBean;
import edu.mit.coeus.utils.AppletServletCommunicator;
import edu.mit.coeus.utils.CoeusGuiConstants;
import edu.mit.coeus.utils.CoeusVector;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Hashtable;
import javax.swing.AbstractAction;
import javax.swing.DefaultListSelectionModel;
//import javax.swing.JTable;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.JTableHeader;
//import javax.swing.table.TableColumn;

/**
 *
 * @author  talarianand
 */
public class RoleModuleForm extends CoeusDlgWindow implements ActionListener, KeyListener {
    
    private final String PERSON_ROLE_SERVLET ="/personRoleServlet";
    
    private final String connectTo = CoeusGuiConstants.CONNECTION_URL+ PERSON_ROLE_SERVLET;
    
    private final static char GET_ROLE_LIST = 'R';
    
    private final static char GET_ROLE_MODULE = 'A';
    
    private CoeusVector cvRoleData;
    
    private CoeusVector cvRoleTableData;
    
    private CoeusVector cvModuleData;
    
    private CoeusVector cvModuleTableData;
    
    private ModuleTableModel moduleTableModel;
    
    private RoleTableModel roleTableModel;
    
    private final static String EMPTY_STRING = "";
    
    private java.awt.Color disabledBackground = (java.awt.Color) javax.swing.UIManager.
        getDefaults().get("Panel.background");
    
    /** Creates new form RoleModuleForm */
    public RoleModuleForm(Frame parent, String title, boolean modal) {
        super(parent, title, modal);
        initComponents();
        registerComponents();
        setColumnData();
    }
    
    public void registerComponents() {
        roleTableModel = new RoleTableModel();
        btnClose.addActionListener(this);
        tblRoles.setModel(roleTableModel);
        tblRoles.setShowHorizontalLines(false);
        tblRoles.setShowVerticalLines(false);
        tblRoles.setBackground(disabledBackground);
        tblRoles.addKeyListener(this);
        this.addEscapeKeyListener(
                new AbstractAction("escPressed"){
            public void actionPerformed(ActionEvent actionEvent){
                performCloseAction();
            }
        });
        
        java.awt.Color bgColor = (java.awt.Color)javax.swing.UIManager.getDefaults().get("Panel.background");
        txtTitle.setEnabled(false);
        txtTitle.setBackground(bgColor);
        
        moduleTableModel = new ModuleTableModel();
        tblModules.setModel(moduleTableModel);
        tblModules.setShowHorizontalLines(false);
        tblModules.setShowVerticalLines(false);
        tblModules.setBackground(disabledBackground);
        tblModules.setSelectionMode(
                DefaultListSelectionModel.SINGLE_SELECTION);
        
        try {
            cvRoleData = getRoleList();
            roleTableModel.setData(cvRoleData);
            tblRoles.addMouseListener(new MouseAdapter() {
                public void mouseClicked(MouseEvent mouseEvent) {
                    int selectedRow = tblRoles.getSelectedRow();
                    if(selectedRow != -1 && cvRoleData != null && cvRoleData.size() > selectedRow) {
                        try {
                            PersonRoleInfoBean personInfoBean = (PersonRoleInfoBean)cvRoleData.get(selectedRow);
                            String roleName = personInfoBean.getRoleName();
                            txtTitle.setText("The Role "+"\""+roleName+"\" is applicable in:");
                            cvModuleTableData = getModuleList(personInfoBean.getRoleCode());
                            moduleTableModel.setData(cvModuleTableData);
                        } catch(CoeusClientException ce) {
                            ce.printStackTrace();
                        } catch(CoeusException ce) {
                            ce.printStackTrace();
                        }
                    }
                }
            });
        } catch(CoeusClientException ce) {
            ce.printStackTrace();
        } catch(CoeusException ce) {
            ce.printStackTrace();
        }
    }
    
    private void performCloseAction() {
        this.dispose();
    }
    
    private void setColumnData() {
        JTableHeader tableHeader = tblRoles.getTableHeader();
        tableHeader.setReorderingAllowed(false);
        tableHeader.setFont(CoeusFontFactory.getLabelFont());
        tableHeader.setPreferredSize(new Dimension(0,22));
        
        tableHeader = tblModules.getTableHeader();
        tableHeader.setReorderingAllowed(false);
        tableHeader.setFont(CoeusFontFactory.getLabelFont());
        tableHeader.setPreferredSize(new Dimension(0, 0));
        
        tblRoles.setSelectionMode(
                DefaultListSelectionModel.SINGLE_SELECTION);
        if(cvRoleTableData != null && cvRoleTableData.size() >0) {
            tblRoles.setRowSelectionInterval(0,0);
            try {
                PersonRoleInfoBean personInfoBean = (PersonRoleInfoBean)cvRoleData.get(0);
                String roleName = personInfoBean.getRoleName();
                txtTitle.setText("The Role "+"\""+roleName+"\" is applicable in:");
                cvModuleTableData = getModuleList(personInfoBean.getRoleCode());
                moduleTableModel.setData(cvModuleTableData);
            } catch(CoeusClientException ce) {
                ce.printStackTrace();
            } catch(CoeusException ce) {
                ce.printStackTrace();
            }
        }
    }
    
    public CoeusVector getRoleList() throws CoeusClientException, CoeusException {
        RequesterBean requesterBean = new RequesterBean();
        ResponderBean responderBean = new ResponderBean();
        Hashtable htMailListData = null;
        
        requesterBean.setFunctionType(GET_ROLE_LIST);
        AppletServletCommunicator comm
                = new AppletServletCommunicator(connectTo, requesterBean);
        
        comm.send();
        responderBean = comm.getResponse();
        if(responderBean!= null){
            if(responderBean.hasResponse()){
                htMailListData = (Hashtable)responderBean.getDataObject();
                cvRoleData = (CoeusVector) htMailListData.get(PersonRoleInfoBean.class);
            }else{
                throw new CoeusClientException(responderBean.getMessage(),CoeusClientException.ERROR_MESSAGE);
            }
        }
        return cvRoleData;
    }
    
    public CoeusVector getModuleList(String roleId) throws CoeusClientException, CoeusException {
        RequesterBean requesterBean = new RequesterBean();
        ResponderBean responderBean = new ResponderBean();
        Hashtable htMailListData = null;
        
        requesterBean.setFunctionType(GET_ROLE_MODULE);
        requesterBean.setDataObject(roleId);
        AppletServletCommunicator comm
                = new AppletServletCommunicator(connectTo, requesterBean);
        
        comm.send();
        responderBean = comm.getResponse();
        if(responderBean!= null){
            if(responderBean.hasResponse()){
                htMailListData = (Hashtable)responderBean.getDataObject();
                cvModuleTableData = (CoeusVector) htMailListData.get(PersonRoleInfoBean.class);
            }else{
                throw new CoeusClientException(responderBean.getMessage(),CoeusClientException.ERROR_MESSAGE);
            }
        }
        return cvModuleTableData;
    }
    
    class RoleTableModel extends AbstractTableModel {
        String colNames[] = {"Person Roles In Coeus"};
        Class[] colTypes = new Class[] {String.class};
        
        RoleTableModel() {
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
        
        public void setData(CoeusVector cvCustElements){
            cvRoleTableData = cvCustElements;
            fireTableDataChanged();
        }
        
        public int getRowCount() {
            if(cvRoleTableData == null ||  cvRoleTableData.size()== 0){
                return 0;
            }else{
                return cvRoleTableData.size();
            }
        }
        
        public String getColumnName(int column) {
            return colNames[column];
        }
        
        public Object getValueAt(int row,int column) {
            PersonRoleInfoBean personBean = (PersonRoleInfoBean) cvRoleTableData.get(row);
            switch(column) {
                case 0:
                    return personBean.getRoleName();
            }
            return EMPTY_STRING;
        }
        
        public void addRow() {
            
        }
    }
    
    class ModuleTableModel extends AbstractTableModel {
        String colNames[] = {""};
        Class[] colTypes = new Class[] {String.class};
        
        ModuleTableModel() {
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
        
        public void setData(CoeusVector cvCustElements){
            cvModuleData = cvCustElements;
            fireTableDataChanged();
        }
        
        public int getRowCount() {
            if(cvModuleData == null ||  cvModuleData.size()== 0){
                return 0;
            }else{
                return cvModuleData.size();
            }
        }
        
        public String getColumnName(int column) {
            return colNames[column];
        }
        
        public Object getValueAt(int row,int column) {
            PersonRoleInfoBean personBean = (PersonRoleInfoBean) cvModuleData.get(row);
            switch(column) {
                case 0:
                    String module = personBean.getModuleDescription();
                    String subModule = personBean.getSubModuleDescription();
                    if(subModule != null && subModule.trim() != "") {
                        module = module + " ( "+subModule+" )";
                    }
                    return module;
            }
            return EMPTY_STRING;
        }
        
        public void addRow() {
            
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

        jSplitPane1 = new javax.swing.JSplitPane();
        pnlRoles = new javax.swing.JPanel();
        scrlRole = new javax.swing.JScrollPane();
        tblRoles = new javax.swing.JTable();
        pnlModules = new javax.swing.JPanel();
        scrlModule = new javax.swing.JScrollPane();
        tblModules = new javax.swing.JTable();
        txtTitle = new edu.mit.coeus.utils.CoeusTextField();
        btnClose = new edu.mit.coeus.utils.CoeusButton();

        getContentPane().setLayout(new java.awt.GridBagLayout());

        jSplitPane1.setMinimumSize(new java.awt.Dimension(600, 450));
        jSplitPane1.setPreferredSize(new java.awt.Dimension(600, 450));
        pnlRoles.setLayout(new java.awt.GridBagLayout());

        pnlRoles.setMinimumSize(new java.awt.Dimension(250, 500));
        pnlRoles.setPreferredSize(new java.awt.Dimension(250, 500));
        scrlRole.setMinimumSize(new java.awt.Dimension(250, 445));
        scrlRole.setPreferredSize(new java.awt.Dimension(250, 445));
        tblRoles.setModel(new javax.swing.table.DefaultTableModel(
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
        scrlRole.setViewportView(tblRoles);

        pnlRoles.add(scrlRole, new java.awt.GridBagConstraints());

        jSplitPane1.setLeftComponent(pnlRoles);

        pnlModules.setLayout(new java.awt.GridBagLayout());

        pnlModules.setMinimumSize(new java.awt.Dimension(100, 500));
        pnlModules.setPreferredSize(new java.awt.Dimension(100, 500));
        scrlModule.setMinimumSize(new java.awt.Dimension(340, 425));
        scrlModule.setPreferredSize(new java.awt.Dimension(340, 425));
        tblModules.setModel(new javax.swing.table.DefaultTableModel(
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
        scrlModule.setViewportView(tblModules);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        pnlModules.add(scrlModule, gridBagConstraints);

        txtTitle.setBorder(null);
        txtTitle.setFont(CoeusFontFactory.getLabelFont());
        txtTitle.setMinimumSize(new java.awt.Dimension(200, 20));
        txtTitle.setPreferredSize(new java.awt.Dimension(200, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        pnlModules.add(txtTitle, gridBagConstraints);

        jSplitPane1.setRightComponent(pnlModules);

        getContentPane().add(jSplitPane1, new java.awt.GridBagConstraints());

        btnClose.setMnemonic('C');
        btnClose.setText("Close");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        getContentPane().add(btnClose, gridBagConstraints);

    }// </editor-fold>//GEN-END:initComponents

    public void actionPerformed(ActionEvent actionEvent) {
        Object source = actionEvent.getSource();
        if(source.equals(btnClose)) {
            this.dispose();
        }
    }

    public void keyTyped(KeyEvent keyEvent) {
    }

    public void keyPressed(KeyEvent keyEvent) {
        int source = keyEvent.getKeyCode();
        int selectedRow = tblRoles.getSelectedRow();
        if(source == KeyEvent.VK_DOWN || source == KeyEvent.VK_TAB) {
            selectedRow = selectedRow + 1;
        } else if(source == KeyEvent.VK_UP) {
            selectedRow = selectedRow - 1;
        }
        if(selectedRow != -1 && cvRoleData != null && cvRoleData.size() > 0) {
            try {
                PersonRoleInfoBean personInfoBean = (PersonRoleInfoBean)cvRoleData.get(selectedRow);
                String roleName = personInfoBean.getRoleName();
                txtTitle.setText("The Role "+"\""+roleName+"\" is applicable in:");
                cvModuleTableData = getModuleList(personInfoBean.getRoleCode());
                moduleTableModel.setData(cvModuleTableData);
            } catch(CoeusClientException ce) {
                ce.printStackTrace();
            } catch(CoeusException ce) {
                ce.printStackTrace();
            }
        }
    }

    public void keyReleased(KeyEvent keyEvent) {
    }
    
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private edu.mit.coeus.utils.CoeusButton btnClose;
    private javax.swing.JSplitPane jSplitPane1;
    private javax.swing.JPanel pnlModules;
    private javax.swing.JPanel pnlRoles;
    private javax.swing.JScrollPane scrlModule;
    private javax.swing.JScrollPane scrlRole;
    private javax.swing.JTable tblModules;
    private javax.swing.JTable tblRoles;
    private edu.mit.coeus.utils.CoeusTextField txtTitle;
    // End of variables declaration//GEN-END:variables
    
}
