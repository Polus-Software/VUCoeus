/*
 * RolesPanel.java
 *
 * Created on March 14, 2003, 3:39 PM
 */

package edu.mit.coeus.search.gui;

import java.awt.Color;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.DefaultListModel;

import java.util.Vector;


import edu.mit.coeus.gui.CoeusFontFactory;
import edu.mit.coeus.gui.CoeusMessageResources;
import edu.mit.coeus.utils.ComboBoxBean;
import edu.mit.coeus.utils.CoeusGuiConstants;
/**
 * Class which is used to show the role selection part of the ProposalSearch window.
 *
 * @author  ravikanth
 */
public class RolesPanel extends javax.swing.JPanel {
    
    Vector rolesList;
    DefaultListModel selectedListModel,availableListModel;
    //holds CoeusMessageResources instance used for reading message Properties.
    private CoeusMessageResources coeusMessageResources;
    
    /** Creates new form RolesPanel */
    public RolesPanel() {
        initComponents();
        coeusMessageResources = CoeusMessageResources.getInstance();
        txtArDescription.setText( 
            coeusMessageResources.parseMessageKey( "rolesPanel_description1.exceptionCode.3004" )
            +" " + CoeusGuiConstants.getMDIForm().getUserName() + " " +
            coeusMessageResources.parseMessageKey( "rolesPanel_description2.exceptionCode.3005" )
         );
        
        try{
        selectedListModel = ( DefaultListModel ) lstSelectedRoles.getModel();
        availableListModel = ( DefaultListModel ) lstAvailableRoles.getModel();
        btnAdd.addActionListener(new ButtonListener());
        btnRemove.addActionListener(new ButtonListener());
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    private void initComponents() {//GEN-BEGIN:initComponents
        java.awt.GridBagConstraints gridBagConstraints;

        scrPnAvailableRoles = new javax.swing.JScrollPane();
        lstAvailableRoles = new javax.swing.JList();
        btnAdd = new javax.swing.JButton();
        btnRemove = new javax.swing.JButton();
        scrPnSelectedRoles = new javax.swing.JScrollPane();
        lstSelectedRoles = new javax.swing.JList();
        txtArDescription = new javax.swing.JTextArea();

        setLayout(new java.awt.GridBagLayout());

        scrPnAvailableRoles.setBorder(new javax.swing.border.TitledBorder(new javax.swing.border.EmptyBorder(new java.awt.Insets(1, 1, 1, 1)), "Available Roles", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, CoeusFontFactory.getLabelFont(), Color.red));
        scrPnAvailableRoles.setPreferredSize(new java.awt.Dimension(230, 140));
        scrPnAvailableRoles.setMinimumSize(new java.awt.Dimension(120, 60));
        lstAvailableRoles.setModel(new DefaultListModel());
        lstAvailableRoles.setFont(CoeusFontFactory.getNormalFont());
        lstAvailableRoles.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        lstAvailableRoles.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lstAvailableRolesMouseClicked(evt);
            }
        });

        scrPnAvailableRoles.setViewportView(lstAvailableRoles);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridheight = 5;
        gridBagConstraints.fill = java.awt.GridBagConstraints.VERTICAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 0, 5, 5);
        add(scrPnAvailableRoles, gridBagConstraints);

        btnAdd.setMnemonic('A');
        btnAdd.setFont(CoeusFontFactory.getLabelFont());
        btnAdd.setText("<< Add");
        btnAdd.setEnabled(false);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(24, 5, 0, 5);
        add(btnAdd, gridBagConstraints);

        btnRemove.setMnemonic('R');
        btnRemove.setFont(CoeusFontFactory.getLabelFont());
        btnRemove.setText("Remove >>");
        btnRemove.setEnabled(false);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 0, 5);
        add(btnRemove, gridBagConstraints);

        scrPnSelectedRoles.setBorder(new javax.swing.border.TitledBorder(new javax.swing.border.EmptyBorder(new java.awt.Insets(1, 1, 1, 1)), "Selected Roles", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, CoeusFontFactory.getLabelFont(), Color.red));
        scrPnSelectedRoles.setPreferredSize(new java.awt.Dimension(230, 140));
        scrPnSelectedRoles.setMinimumSize(new java.awt.Dimension(120, 60));
        lstSelectedRoles.setModel(new DefaultListModel());
        lstSelectedRoles.setFont(CoeusFontFactory.getNormalFont());
        lstSelectedRoles.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        lstSelectedRoles.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lstSelectedRolesMouseClicked(evt);
            }
        });

        scrPnSelectedRoles.setViewportView(lstSelectedRoles);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridheight = 5;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 0);
        add(scrPnSelectedRoles, gridBagConstraints);

        txtArDescription.setWrapStyleWord(true);
        txtArDescription.setLineWrap(true);
        txtArDescription.setEditable(false);
        txtArDescription.setFont(CoeusFontFactory.getLabelFont());
        txtArDescription.setOpaque(false);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 0, 5);
        add(txtArDescription, gridBagConstraints);

    }//GEN-END:initComponents

    private void lstAvailableRolesMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lstAvailableRolesMouseClicked
        // Add your handling code here:
        
        // if no role is selected disable the add button
        if(!lstAvailableRoles.isSelectionEmpty()){
            btnAdd.setEnabled(true);
        }else{
            btnAdd.setEnabled(false);
        }
    }//GEN-LAST:event_lstAvailableRolesMouseClicked

    private void lstSelectedRolesMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lstSelectedRolesMouseClicked
        // Add your handling code here:
        
        // if no role is selected disable the remove button
        if(!lstSelectedRoles.isSelectionEmpty()){
            btnRemove.setEnabled(true);
        }else{
            btnRemove.setEnabled(false);
        }
        
    }//GEN-LAST:event_lstSelectedRolesMouseClicked
    /**
     * Method used to return all the selected roles as a collection of 
     * ComboBoxBean.
     * 
     * @return Collection of ComboBoxBean with role id as code and role name as
     * description.
     */
    public Vector getRolesList(){
        if(!selectedListModel.isEmpty()){
            Vector roles = new Vector();
            for(int index=0; index < selectedListModel.size(); index++ ){
                roles.addElement((ComboBoxBean)selectedListModel.elementAt(index));
            }
            return roles;
        }
        return null;
    }
    
    /**
     * Method used to set the given collection of ComboBoxBean as selected user
     * roles.
     *
     * @param roles Collection of ComboBoxBean
     */
    public void setRolesList(Vector roles){
        this.rolesList = roles;
        if(rolesList != null && rolesList.size() > 0 ){
            selectedListModel.clear();
            availableListModel.clear();
            btnAdd.setEnabled( false );
            btnRemove.setEnabled( false );
            for(int rolesIndex = 0 ; rolesIndex < rolesList.size(); rolesIndex++){
                selectedListModel.addElement((ComboBoxBean)rolesList.elementAt(rolesIndex));
            }
        }
    }
    
    class ButtonListener implements ActionListener {
        public void actionPerformed(ActionEvent ae){
            Object selectedItem;
            if(ae.getSource() == btnAdd ){
                if(!lstAvailableRoles.isSelectionEmpty()){
                    selectedItem = (ComboBoxBean)lstAvailableRoles.getSelectedValue();
                    availableListModel.removeElement(selectedItem);
                    if(availableListModel.isEmpty()){
                        btnAdd.setEnabled(false);
                    }else{
                        lstAvailableRoles.setSelectedIndex(0);
                    }
                    selectedListModel.addElement(selectedItem);
                }
                
            }else if(ae.getSource() == btnRemove ){
                if(!lstSelectedRoles.isSelectionEmpty()){
                    selectedItem = (ComboBoxBean)lstSelectedRoles.getSelectedValue();
                    selectedListModel.removeElement(selectedItem);
                    if(selectedListModel.isEmpty()){
                        btnRemove.setEnabled(false);
                    }else{
                        lstSelectedRoles.setSelectedIndex(0);
                    }
                    availableListModel.addElement(selectedItem);
                }
            }
        }
    }
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JScrollPane scrPnSelectedRoles;
    private javax.swing.JList lstSelectedRoles;
    private javax.swing.JList lstAvailableRoles;
    private javax.swing.JScrollPane scrPnAvailableRoles;
    private javax.swing.JButton btnRemove;
    private javax.swing.JButton btnAdd;
    private javax.swing.JTextArea txtArDescription;
    // End of variables declaration//GEN-END:variables
    
}
