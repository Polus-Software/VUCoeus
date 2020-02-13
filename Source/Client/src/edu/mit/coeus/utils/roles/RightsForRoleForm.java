/*
 * @(#)RightsForRoleForm.java
 *
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 *
 */
package edu.mit.coeus.utils.roles;

import java.awt.event.*;
import java.awt.*;
import java.util.*;

import javax.swing.*;
import javax.swing.table.*;

import edu.mit.coeus.gui.*;
import edu.mit.coeus.utils.*;
import edu.mit.coeus.bean.*;

import edu.mit.coeus.brokers.RequesterBean;
import edu.mit.coeus.brokers.ResponderBean;

/** 
 * <CODE>RightsForRoleForm </CODE>is a form object which display
 * and it is used to <CODE> display </CODE> the rights for the selected roles.
 * This class will be instantiated from <CODE>UserRolesMaintenance</CODE>.
 * @version 1.0 April 12, 2003, 12:17 PM
 * @author Raghunath P.V.
 */

public class RightsForRoleForm extends javax.swing.JComponent implements ActionListener{

    //holds the Parent window component.
    private CoeusDlgWindow dlgParentComponent;
    private CoeusAppletMDIForm mdiForm;
    private String title;
    private Vector vecRoleData;
    private String roleId;
    private String roleName;
    
    /**
     * Creates new RightsForRoleForm form 
     * @param dlgPrnt parent dialog window component.
     */
    public RightsForRoleForm(String roleId, String roleName) {

        this.roleId = roleId;
        this.roleName = roleName;
        this.title = "Rights available for "+roleName;
        if(roleId != null){
            this.vecRoleData = getRightDescriptionValues(roleId);
        }
        dlgParentComponent = new CoeusDlgWindow(CoeusGuiConstants.getMDIForm(),
                title, true);
        dlgParentComponent.getContentPane().add(createLookupWindow());
        dlgParentComponent.pack();
        dlgParentComponent.setResizable(false);
        Dimension screenSize
                = Toolkit.getDefaultToolkit().getScreenSize();
        Dimension dlgSize = dlgParentComponent.getSize();
        dlgParentComponent.setLocation(screenSize.width/2 - (dlgSize.width/2),
        screenSize.height/2 - (dlgSize.height/2));
        btnOk.requestFocusInWindow();
        dlgParentComponent.show();
    }

    private JComponent createLookupWindow(){

        initComponents();
        postInitComponent();
        setFormData();
        setTableSettings();
        return this;
    }

    private Vector getRightDescriptionValues(String id){

        Vector rightsList = null;
        String connectTo = CoeusGuiConstants.CONNECTION_URL + CoeusGuiConstants.FUNCTION_SERVLET;

        RequesterBean request = new RequesterBean();

        request.setDataObject("GET_ROLE_RIGHTS");
        request.setId(id);

        AppletServletCommunicator comm
                = new AppletServletCommunicator(connectTo, request);

        comm.send();
        ResponderBean response = comm.getResponse();

        if (response!=null){
            if (response.isSuccessfulResponse()){
                rightsList = (Vector)response.getDataObject();
            }
        }

        return rightsList;

    }

    private void setTableSettings(){

        //System.out.println(" in setTableSettings");
        TableColumn column = tblRoleInformation.getColumnModel().getColumn(0);

        column.setMaxWidth(20);
        column.setMinWidth(20);
        column.setPreferredWidth(20);
        column.setHeaderValue("  ");
        column.setCellRenderer( new StatusCellRenderer() );

        JTableHeader header = tblRoleInformation.getTableHeader();
        header.setReorderingAllowed(false);
        header.setResizingAllowed(true);
        
        tblRoleInformation.setOpaque( false );
        tblRoleInformation.setCellSelectionEnabled( false );

        //tblRoleInformation.setShowVerticalLines(false);
        //tblRoleInformation.setShowHorizontalLines(false);

        column = tblRoleInformation.getColumnModel().getColumn(1);
        column.setMinWidth(200);
        column.setMaxWidth(200);
        column.setPreferredWidth(200);
           

        column = tblRoleInformation.getColumnModel().getColumn(2);
        column.setMinWidth(225);
        column.setMaxWidth(325);
        column.setPreferredWidth(325);
    }

    private void setFormData(){

        Vector vcData = null;
        Vector vcDataPopulate = new Vector();
        RoleRightInfoBean roleRightInfoBean = null;
        if(vecRoleData != null){
            int dataSize = vecRoleData.size();
            for(int index = 0 ; index < dataSize; index++){
                roleRightInfoBean = (RoleRightInfoBean)vecRoleData.elementAt(index);
                if(roleRightInfoBean != null){

                    String rightType = roleRightInfoBean.getRightId();
                    String rightDesc = roleRightInfoBean.getDescription();
                    boolean descended = roleRightInfoBean.isDescendFlag();

                    vcData= new Vector();

                    if(descended){
                        vcData.addElement("A");
                    }else{
                        vcData.addElement("I");
                    }
                    vcData.addElement(rightType == null ? "" : rightType);
                    vcData.addElement(rightDesc == null ? "" : rightDesc);
                    vcDataPopulate.addElement(vcData);
                }
            }
            ((DefaultTableModel)tblRoleInformation.getModel()).
                        setDataVector(vcDataPopulate,getColumnNames());
            ((DefaultTableModel)tblRoleInformation.getModel()).
                fireTableDataChanged();
        }
    }

    private Vector getColumnNames(){

        Enumeration enumColNames = tblRoleInformation.getColumnModel().getColumns();
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
    // <editor-fold defaultstate="collapsed" desc=" Generated Code ">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        scrPnDetail = new javax.swing.JScrollPane();
        tblRoleInformation = new javax.swing.JTable();
        btnOk = new javax.swing.JButton();

        setLayout(new java.awt.GridBagLayout());

        scrPnDetail.setMaximumSize(new java.awt.Dimension(565, 260));
        scrPnDetail.setMinimumSize(new java.awt.Dimension(565, 260));
        scrPnDetail.setPreferredSize(new java.awt.Dimension(565, 260));
        tblRoleInformation.setBackground(javax.swing.UIManager.getDefaults().getColor("Panel.background"));
        tblRoleInformation.setFont(CoeusFontFactory.getNormalFont());
        tblRoleInformation.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Title 1", "Right", "Description"
            }
        ));
        tblRoleInformation.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
        tblRoleInformation.setSelectionBackground(new java.awt.Color(0, 51, 153));
        tblRoleInformation.setSelectionForeground(new java.awt.Color(255, 255, 51));
        scrPnDetail.setViewportView(tblRoleInformation);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 8;
        gridBagConstraints.gridheight = 4;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 0.5;
        gridBagConstraints.weighty = 0.5;
        gridBagConstraints.insets = new java.awt.Insets(5, 3, 5, 3);
        add(scrPnDetail, gridBagConstraints);

        btnOk.setFont(CoeusFontFactory.getLabelFont());
        btnOk.setMnemonic('O');
        btnOk.setText("OK");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.gridwidth = 8;
        gridBagConstraints.insets = new java.awt.Insets(3, 0, 3, 0);
        add(btnOk, gridBagConstraints);

    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    public javax.swing.JButton btnOk;
    public javax.swing.JScrollPane scrPnDetail;
    public javax.swing.JTable tblRoleInformation;
    // End of variables declaration//GEN-END:variables

   
    private void postInitComponent(){

        this.tblRoleInformation.getTableHeader().setFont(CoeusFontFactory.getLabelFont());
        this.scrPnDetail.setViewportView( this.tblRoleInformation );
        tblRoleInformation.setEnabled(false);
        this.btnOk.addActionListener( this );
    }

    /**
     * This method is used to capture the action events fired by ok/cancel
     * button of this table window compoent.
     * @param actionEvent component (button) Action instance
     */
    public void actionPerformed( ActionEvent actionEvent ) {

        Object actionSource = actionEvent.getSource();
        if( actionSource.equals( this.btnOk ) ){
            if( dlgParentComponent != null ){
                dlgParentComponent.dispose();
            }
        }
    }

     class StatusCellRenderer extends DefaultTableCellRenderer {

        ImageIcon activeUserIcon = new ImageIcon( getClass().
                                getResource( "/images/descendy.gif" ) );
        ImageIcon inactiveUserIcon = new ImageIcon( getClass().
                                getResource( "/images/descendn.gif" ) );

        public Component getTableCellRendererComponent(JTable table,
        Object value, boolean isSelected, boolean hasFocus, int row,
        int column) {

            setOpaque(false);
            if( value != null ) {
                if( value.toString().equals("A") ) {
                    setIcon( activeUserIcon );
                }else if( value.toString().equals("I") ) {
                    setIcon( inactiveUserIcon );//descendn
                }
            }
            return this;

        }

    }

}
