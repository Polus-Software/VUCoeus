/*
 * @(#)TypeForm.java 1.0 8/21/02
 *
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 */
package edu.mit.coeus.organization.gui;

import edu.mit.coeus.gui.CoeusFontFactory;
import edu.mit.coeus.gui.CoeusAppletMDIForm;
import edu.mit.coeus.organization.bean.OrganizationListBean;
import edu.mit.coeus.utils.CoeusGuiConstants;
import edu.mit.coeus.utils.CoeusOptionPane;
import edu.mit.coeus.gui.CoeusMessageResources;
import edu.mit.coeus.utils.ScreenFocusTraversalPolicy;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Vector;

/**
 * This class prepares the panel that contain all the form controls for
 * Organization Type form.
 *
 * @version :1.0 August 25, 2002, 1:35 PM
 * @author Guptha K
 * @modified by Sagin
 * @date 24-10-02
 * Description : As part of Java V1.3 compatibility, replaced all null Vectors
 *               with new Vector() instance.
 *
 */

public class TypeForm extends JPanel {
    private JLabel lblSelOrgType;
    private JLabel lblOrgList;

    private JButton btnAdd;
    private JButton btnRemove;

    private JTable tblSelOrgType;
    private JTable tblOrgList;

    private JPanel mainPanel;
    private JPanel pnlOrganizationType;

    private char functionType;
    OrganizationListBean[] selListData;
    OrganizationListBean[] listData;
    private CoeusAppletMDIForm mdiForm;
    private boolean saveRequired;

    // add functionality
    private final char ADD_FUNCTION = 'I';
    // modify functionality
    private final char MODIFY_FUNCTION = 'U';
    // display functionality
    private final char DISPLAY_FUNCTION = 'D';
       
    //holds CoeusMessageResources instance used for reading message Properties.
    private CoeusMessageResources coeusMessageResources;
    //Bug Fix 2114 - start
    private boolean dataPresent = true;
    //Bug Fix 2114 - End

   /**
     * Constructor, which constructs the Organization maintenance TypeForm.
     * The functionality of the form (enable/disable the
     * components) will be decided based on the parameter functionType.
     *
     * @param functionType char  decides to enable/disable the form controls 
     *                           A- Add mode
     *                           D- Display mode M- Modify mode
     * @param orgQuestionList
     * @param OrganizationListBean array of beans contains the organization type list
     * @param OrganizationListBean array of beans contains the selected organization type list
     */

    public TypeForm(char functionType, OrganizationListBean[] selListData,
                    OrganizationListBean[] listData) {
        this.functionType = functionType;
        this.selListData = selListData;
        this.listData = listData;
        initComponents();
        
        java.awt.Component[] component={tblSelOrgType,btnAdd, btnRemove, tblOrgList};
        ScreenFocusTraversalPolicy traversalPolicy = new ScreenFocusTraversalPolicy(component);
        setFocusTraversalPolicy(traversalPolicy);
        setFocusCycleRoot(true);
        
        setSelectedListData();
        setListData();
        formatFields();
    }

    /**
     * Initialize the form controls.
     */
    public void initComponents() {
        coeusMessageResources = CoeusMessageResources.getInstance();
        
        OrganizationTypeLayout customLayout = new OrganizationTypeLayout();
        pnlOrganizationType = new JPanel();
        pnlOrganizationType.setLayout(customLayout);
        
        lblSelOrgType = new JLabel();
        pnlOrganizationType.add(lblSelOrgType);
        Vector vecCols = new Vector();
        vecCols.add("Type");
        vecCols.add("Description");

        tblSelOrgType = new JTable(new DefaultTableModel(new Vector(), vecCols) {
            public boolean isCellEditable(int row, int col) {
                return false;
            }
        });
        tblSelOrgType.getTableHeader().setReorderingAllowed( false );
        tblSelOrgType.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);      
        
        TableColumn colType = tblSelOrgType.getColumnModel().getColumn(0);
        colType.setMinWidth(50);
        //colType.setMaxWidth(50);
        colType.setPreferredWidth(50);
        
        colType = tblSelOrgType.getColumnModel().getColumn(1);
        colType.setMinWidth(150);
        //colType.setMaxWidth(50);
        colType.setPreferredWidth(150);

        JScrollPane scrOrgType = new JScrollPane();
        scrOrgType.setMinimumSize(new Dimension(22, 15));
        scrOrgType.setPreferredSize(new Dimension(600, 400));
        scrOrgType.setViewportView(tblSelOrgType);

        pnlOrganizationType.add(scrOrgType);

        btnAdd = new JButton("<<");
        btnAdd.setFont(CoeusFontFactory.getLabelFont());
        btnAdd.setHorizontalTextPosition(JButton.LEFT);
        btnAdd.setName("Add");
        btnAdd.setMaximumSize(new Dimension(20, 25));
        btnAdd.setMnemonic('A');
        pnlOrganizationType.add(btnAdd);
        btnAdd.addActionListener(new OrgListListener());
        btnRemove = new JButton(">>");
        btnRemove.setFont(CoeusFontFactory.getLabelFont());
        btnRemove.setMnemonic('R');
        btnRemove.setName("Remove");
        pnlOrganizationType.add(btnRemove);
        btnRemove.addActionListener(new OrgListListener());
        vecCols = new Vector();
        vecCols.add("Type");
        vecCols.add("Description");
        tblOrgList = new JTable(new DefaultTableModel(new Vector(), vecCols) {
            public boolean isCellEditable(int row, int col) {
                return false;
            }
        });
        tblOrgList.getTableHeader().setReorderingAllowed( false );
        tblOrgList.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        
        TableColumn colOrgType = tblOrgList.getColumnModel().getColumn(0);
        colOrgType.setMinWidth(50);
        //colOrgType.setMaxWidth(50);
        colOrgType.setPreferredWidth(50);
        
        colOrgType = tblOrgList.getColumnModel().getColumn(1);
        colOrgType.setMinWidth(130);
        //colOrgType.setMaxWidth(50);
        colOrgType.setPreferredWidth(130);

        JScrollPane scrOrgList = new JScrollPane();
        scrOrgList.setMinimumSize(new Dimension(22, 15));
        scrOrgList.setPreferredSize(new Dimension(600, 400));
        scrOrgList.setViewportView(tblOrgList);
        scrOrgList.getViewport().setBackground(Color.white);
        scrOrgList.setBackground(java.awt.Color.white);
        scrOrgList.setForeground(java.awt.Color.white);
        pnlOrganizationType.add(scrOrgList);

        lblOrgList = new JLabel();
        pnlOrganizationType.add(lblOrgList);

        add(pnlOrganizationType);

        tblOrgList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tblSelOrgType.setSelectionMode(
                tblSelOrgType.getSelectionModel().SINGLE_SELECTION);

        tblOrgList.getTableHeader().setFont(CoeusFontFactory.getLabelFont());
        tblSelOrgType.getTableHeader().setFont(CoeusFontFactory.getLabelFont());
        tblOrgList.setFont(CoeusFontFactory.getNormalFont());
        tblSelOrgType.setFont(CoeusFontFactory.getNormalFont());
    }


    /**
     * This method is used to get the boolean value saveRequired
     */
    public boolean isSaveRequired() {
        return saveRequired;
    }
    
    //Added by Amit 11/21/2003
    /** This method use to implement focus on first editable component in this page.
     */
    public void setDefaultFocusForComponent(){
        if(!( functionType == CoeusGuiConstants.DISPLAY_MODE )) {            
            if(tblSelOrgType.getRowCount() > 0 ) {
                tblSelOrgType.requestFocusInWindow();
                tblSelOrgType.setRowSelectionInterval(0, 0);
                tblSelOrgType.setColumnSelectionInterval(0,0);
            }            
        }
    }    
    //end Amit       

    /**
     * This method is used to set all the selected list of information to the
     * table Which contains selected types information
     */
    public void setSelectedListData() {
        if (selListData != null) {
            //Bug Fix 2114 - start
            int length = selListData.length;
            if(length > 0){
                for (int i = 0; i < length; i++) {
                    Vector vec = new Vector();
                    vec.add(new Integer(
                    selListData[i].getOrganizationTypeCode()).toString());
                    vec.add(selListData[i].getDescription());
                    ((DefaultTableModel) tblSelOrgType.getModel()).addRow(vec);
                }
            }else{
                dataPresent = false;
            }//Bug Fix 2114 - End
        }
    }

    /**
     * This method is used to set all the available types other than alreadt
     * selected for the organization
     *
     */
    public void setListData() {
        if (listData != null) {
            for (int i = 0; i < listData.length; i++) {
                Vector vec = new Vector();
                //to add only remaining information other than the selected list
                boolean found = false;
                if (tblSelOrgType.getRowCount() > 0) {
                    for (int count = 0; count < tblSelOrgType.getRowCount(); count++) {
                        String value = (tblSelOrgType.getValueAt(count, 0)
                                == null ? "" : tblSelOrgType.getValueAt(count,
                                        0).toString().trim());
                        if ((!value.equals("")) &&
                                (listData[i].getOrganizationTypeCode() ==
                                Integer.parseInt(value))) {
                            found = true;
                        }
                    }
                }
                if (!found) {
                    vec.add(new Integer(
                            listData[i].getOrganizationTypeCode()).toString());
                    vec.add(listData[i].getDescription());
                    ((DefaultTableModel) tblOrgList.getModel()).addRow(vec);
                }
            }
        }
    }

    /**
     * set enabled or diabled for form controls as per the functionality
     *
     * @param functionType the functionality type
     *                 'D' - display
     *                 'M' - modify
     */
    public void formatFields(char functionType) {
        boolean enableStatus = true;
        if (functionType == DISPLAY_FUNCTION) {
            enableStatus = false;
        } else {
            enableStatus = true;
        }
    }

    /**
     * This method is used to validate the information before saving the
     * organization details
     */
    public boolean validateData() {
        boolean dataOK = false;
        if (tblSelOrgType.getRowCount() <= 0) {
            log(coeusMessageResources.parseMessageKey(
                                            "orgTypeFrm_exceptionCode.1143"));
        } else {
            dataOK = true;
        }
        return dataOK;
    }

    /**
     * Display alert message
     *
     * @param mesg the message to be displayed
     */
    public void log(String mesg) {
        CoeusOptionPane.showErrorDialog(mesg);
    }

    /**
     * get the Panel of organization Type form. This panel contains all the
     * necessary form controls
     *
     * @return JPanel contains the organization Type form controls
     */
    public JPanel getOrganizationTypePanel() {
        return pnlOrganizationType;
    }

    /** 
     * This method is used to set the information for saving the selected List
     * information for this Organization data or to remove from the data base
     * if any deselected from the existing list.
     */
    public OrganizationListBean[] getFormData() {

        /* If the mode is in insert mode with function Type is 'I' then get all
         * the information and save into the database
         */
        int selOrgListSize = tblSelOrgType.getRowCount();
        Vector newOrgListData = null;
        /** Bug Fix 2114 - start
         */
        if(!dataPresent && functionType!= ADD_FUNCTION){
            newOrgListData = new Vector();
            if (selOrgListSize > 0) {
                for (int rowCount = 0; rowCount < selOrgListSize; rowCount++) {
                    OrganizationListBean orgData = new OrganizationListBean();
                    orgData.setOrganizationTypeCode(
                            Integer.parseInt(tblSelOrgType.getValueAt(rowCount,
                                    0).toString().trim()));
                    orgData.setDescription(
                            (tblSelOrgType.getValueAt(rowCount, 1) == null ? "" :
                            tblSelOrgType.getValueAt(rowCount, 1).toString().trim()));
                    orgData.setAcType("I");
                    newOrgListData.add(orgData);
                }
            } /** Bug Fix 2114 - End*/
        }else if (functionType == ADD_FUNCTION ) {
            newOrgListData = new Vector();
            if (selOrgListSize > 0) {
                for (int rowCount = 0; rowCount < selOrgListSize; rowCount++) {
                    OrganizationListBean orgData = new OrganizationListBean();
                    orgData.setOrganizationTypeCode(
                            Integer.parseInt(tblSelOrgType.getValueAt(rowCount,
                                    0).toString().trim()));
                    orgData.setDescription(
                            (tblSelOrgType.getValueAt(rowCount, 1) == null ? "" :
                            tblSelOrgType.getValueAt(rowCount, 1).toString().trim()));
                    orgData.setAcType("I");
                    newOrgListData.add(orgData);
                }
            }
        } else if (functionType == MODIFY_FUNCTION) {
            newOrgListData = new Vector();
            int dataCount = 0;
            /* This is if the operation is Modify and the data is changed in the
             * selected Organization lsit table like removing the existing data
             * then we need to delete the information from the ListInformation
             * in the data base
             */
            if (selListData.length > 0) {
                for (int count = 0; count < selListData.length; count++) {
                    if (selOrgListSize > 0) {
                        OrganizationListBean orgData =
                                (OrganizationListBean) selListData[count];
                        boolean found = false;
                        for (int rowCount = 0; rowCount < selOrgListSize; rowCount++) {
                            if (orgData.getOrganizationTypeCode() ==
                                    Integer.parseInt((tblSelOrgType.getValueAt(
                                            rowCount, 0) == null ? "0" :
                                    tblSelOrgType.getValueAt(rowCount,
                                            0).toString().trim()))) {
                                orgData.setOrganizationTypeCode(
                                        Integer.parseInt(tblSelOrgType.getValueAt(
                                                rowCount, 0) == null ? "0" :
                                        tblSelOrgType.getValueAt(rowCount,
                                                0).toString().trim()));
                                orgData.setDescription(
                                        (tblSelOrgType.getValueAt(rowCount, 1) == null ?
                                        "" : tblSelOrgType.getValueAt(rowCount,
                                                1).toString().trim()));
                                found = true;
                            }
                        }
                        if (found) {
                            orgData.setAcType("U");
                        } else {
                            orgData.setAcType("D");
                        }
                        newOrgListData.add(orgData);
                    }
                }
            }

            /* This is if the operation is Modify and the data is added in the
             * selected Organization list table from the ListInformation then we
             * need to insert the information in the List Information in the
             * data base
             */
            if (tblSelOrgType.getRowCount() > 0) {
                for (int count = 0; count < tblSelOrgType.getRowCount(); count++) {
                    boolean found = false;
                    int selListSize = selListData.length;
                    if (selListSize > 0) {
                        for (int rowCount = 0; rowCount < selListSize; rowCount++) {
                            OrganizationListBean orgData = selListData[rowCount];
                            if (Integer.parseInt(tblSelOrgType.getValueAt(count,
                                    0).toString().trim()) ==
                                    orgData.getOrganizationTypeCode()) {
                                found = true;
                            }
                        }
                        if (!found) {
                            OrganizationListBean newData =
                                    new OrganizationListBean();
                            newData.setOrganizationTypeCode(Integer.parseInt(
                                    tblSelOrgType.getValueAt(count, 0) == null ?
                                    "0" : tblSelOrgType.getValueAt(count,
                                            0).toString().trim()));
                            newData.setDescription((tblSelOrgType.getValueAt(
                                    count, 1) == null ? "" :
                                    tblSelOrgType.getValueAt(count,
                                            1).toString().trim()));
                            newData.setAcType("I");
                            newOrgListData.add(newData);
                        }
                    }
                }
            }
        }

        OrganizationListBean[] newData =
                new OrganizationListBean[newOrgListData.size()];
        for (int listCount = 0; listCount < newOrgListData.size(); listCount++) {
            OrganizationListBean newInfo =
                    (OrganizationListBean) newOrgListData.elementAt(listCount);
            newData[listCount] = newInfo;
        }

        return newData;

    }

    /**
     * This is used to move the selected Type information from the available
     * info to the selected info
     */
    private void selectOrgData() {
        Vector vecRow = new Vector();
        int rowNum = tblOrgList.getSelectedRow();
        int colCount = tblOrgList.getColumnCount();
        DefaultTableModel model = (DefaultTableModel) tblOrgList.getModel();
        if ((rowNum != -1) && (rowNum < model.getRowCount())) {
            for (int cols = 0; cols < colCount; cols++) {
                vecRow.add((String) model.getValueAt(rowNum, cols));
            }
            model.removeRow(rowNum);
            model.fireTableDataChanged();
            insertRowInSortedTable(tblSelOrgType, vecRow);
        }
    }

    /**
     * insert the row into table
     *
     * @param table table where the data is to be inserted
     * @param vtr vector contains the data to be inserted
     */
    private void insertRowInSortedTable(JTable table, Vector vtr) {
        DefaultTableModel tableModel = (DefaultTableModel) table.getModel();
        Vector dVector = tableModel.getDataVector();
        int rowCount = dVector.size();
        int rowIndex = 0;
        for (rowIndex = 0; rowIndex < rowCount; rowIndex++) {
            Vector vecElement = (Vector) dVector.elementAt(rowIndex);
            if ((((String) vecElement.elementAt(1)).compareTo(
                    (String) vtr.elementAt(1))) < 0) {
                continue;
            } else
                break;
        }
        tableModel.insertRow(rowIndex, vtr);
        tableModel.fireTableDataChanged();
    }

    /**
     * This is used to deselecte Type information from the selected list and
     * to place in the available list
     */

    private void removeSelection() {
        Vector vecRow = new Vector();
        int rowNum = tblSelOrgType.getSelectedRow();
        int colCount = tblSelOrgType.getColumnCount();
        DefaultTableModel model = (DefaultTableModel) tblSelOrgType.getModel();
        if ((rowNum != -1) && (rowNum < model.getRowCount())) {
            for (int cols = 0; cols < colCount; cols++) {
                vecRow.add((String) model.getValueAt(rowNum, cols));
            }
            model.removeRow(rowNum);
            model.fireTableDataChanged();
            insertRowInSortedTable(tblOrgList, vecRow);
        }

    }


    /**
     * This is used to format the fields based on the functionType
     */
    private void formatFields() {
        boolean enableStatus = true;
        if (functionType == DISPLAY_FUNCTION) {
            enableStatus = false;
        } else {
            enableStatus = true;
        }
        btnAdd.setEnabled(enableStatus);
        btnRemove.setEnabled(enableStatus);
        if (!enableStatus) {
            tblSelOrgType.setBackground(this.getBackground());
            tblOrgList.setBackground(this.getBackground());
            pnlOrganizationType.setBackground(this.getBackground());

        }
    }

    /**
     * This is used to catch the action events in Type form for adding selected
     * Types and for deselecting the information
     */
    class OrgListListener implements ActionListener {
        public void actionPerformed(ActionEvent ae) {
            if (ae.getSource() instanceof JButton) {
                saveRequired = true;
                if (((JButton) ae.getSource()).getName().equals("Add")) {
                    selectOrgData();
                } else if (((JButton) ae.getSource()).getName().equals("Remove")) {
                    removeSelection();
                }
            }
        }
    }
}


