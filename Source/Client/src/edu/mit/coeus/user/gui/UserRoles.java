/*
 * UserRoles.java
 *
 * Created on June 27, 2003, 3:16 PM
 */

package edu.mit.coeus.user.gui;

import javax.swing.*;
import javax.swing.tree.*;
import javax.swing.plaf.basic.*;
import edu.mit.coeus.gui.CoeusFontFactory;
import java.awt.*;
import java.awt.event.*;
import edu.mit.coeus.gui.*;
import edu.mit.coeus.utils.*;

/**
 *
 * @author  senthil
 */
public class UserRoles extends javax.swing.JComponent {
    private DefaultMutableTreeNode root,child1,child2;
    /** Creates new form UserRoles */ 
    public UserRoles() {
        initComponents();
        postInitComponents();
    }
    private void postInitComponents() {
        root = new DefaultMutableTreeNode("Root",true);
        child1 = new DefaultMutableTreeNode("Roles Assigned in MIT",true);
        child1.add( new DefaultMutableTreeNode("Aggregator"));
        child1.add( new DefaultMutableTreeNode("Approver")); 

        child2 = new DefaultMutableTreeNode("Roles Assigned in NOUS",true);
        child2.add( new DefaultMutableTreeNode("Test1"));
        child2.add( new DefaultMutableTreeNode("Test2"));
        root.add(child1);
        root.add(child2);
        
        //txtSubmissionTypeQualCode.setCaretPosition(0);
        trRoles.setModel( new DefaultTreeModel(root) );
        trRoles.setShowsRootHandles( false );
        trRoles.setRootVisible(false );
        DefaultTreeCellRenderer renderer = (DefaultTreeCellRenderer)trRoles.getCellRenderer();
        renderer.setOpenIcon(null);
        renderer.setClosedIcon(null);
        renderer.setLeafIcon(null);
        BasicTreeUI ui = (BasicTreeUI)trRoles.getUI();
        ui.setExpandedIcon(null);
        ui.setCollapsedIcon(null);
    }
    public void showUserRoles(){
        
    }
    public static void main(String args[]) {
        UserRoles usrRole = new UserRoles();
        
        try{
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        }catch(Exception e){
        }
        
        final CoeusDlgWindow dlgParentComponent = new CoeusDlgWindow(new JDialog(),
            "User Roles", true);
        dlgParentComponent.getContentPane().add( usrRole);
        dlgParentComponent.setResizable(false);
        dlgParentComponent.pack();
        Dimension screenSize
                = Toolkit.getDefaultToolkit().getScreenSize();
        Dimension dlgSize = dlgParentComponent.getSize();
        dlgParentComponent.setLocation(screenSize.width/2 - (dlgSize.width/2),
        screenSize.height/2 - (dlgSize.height/2));
        
        dlgParentComponent.addWindowListener( new WindowAdapter() {
            public void windowClosing(WindowEvent we ) {
                System.exit(0);
                //dlgParentComponent.dispose();
            }
        });
        dlgParentComponent.show();
    }
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    private void initComponents() {//GEN-BEGIN:initComponents
        java.awt.GridBagConstraints gridBagConstraints;

        lblUserName = new javax.swing.JLabel();
        pnlBody = new javax.swing.JPanel();
        pnlUserRoles = new javax.swing.JPanel();
        scrPnUserRoles = new javax.swing.JScrollPane();
        trRoles = new javax.swing.JTree();
        btnOk = new javax.swing.JButton();

        setLayout(new java.awt.GridBagLayout());

        setMaximumSize(new java.awt.Dimension(550, 450));
        setMinimumSize(new java.awt.Dimension(550, 450));
        setPreferredSize(new java.awt.Dimension(550, 450));
        lblUserName.setText("Roles For User Name");
        lblUserName.setMaximumSize(new java.awt.Dimension(119, 20));
        lblUserName.setMinimumSize(new java.awt.Dimension(119, 20));
        lblUserName.setPreferredSize(new java.awt.Dimension(119, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        add(lblUserName, gridBagConstraints);

        pnlBody.setLayout(new java.awt.GridBagLayout());

        pnlBody.setMaximumSize(new java.awt.Dimension(550, 430));
        pnlBody.setMinimumSize(new java.awt.Dimension(550, 430));
        pnlBody.setPreferredSize(new java.awt.Dimension(550, 430));
        pnlUserRoles.setLayout(new java.awt.GridBagLayout());

        pnlUserRoles.setMaximumSize(new java.awt.Dimension(450, 430));
        pnlUserRoles.setMinimumSize(new java.awt.Dimension(450, 430));
        pnlUserRoles.setPreferredSize(new java.awt.Dimension(450, 430));
        scrPnUserRoles.setBorder(new javax.swing.border.BevelBorder(javax.swing.border.BevelBorder.LOWERED));
        scrPnUserRoles.setMaximumSize(new java.awt.Dimension(450, 430));
        scrPnUserRoles.setMinimumSize(new java.awt.Dimension(450, 430));
        scrPnUserRoles.setPreferredSize(new java.awt.Dimension(450, 430));
        trRoles.setFont(CoeusFontFactory.getLabelFont());
        scrPnUserRoles.setViewportView(trRoles);

        pnlUserRoles.add(scrPnUserRoles, new java.awt.GridBagConstraints());

        pnlBody.add(pnlUserRoles, new java.awt.GridBagConstraints());

        btnOk.setFont(CoeusFontFactory.getLabelFont());
        btnOk.setText("OK");
        btnOk.setMaximumSize(new java.awt.Dimension(90, 30));
        btnOk.setMinimumSize(new java.awt.Dimension(90, 30));
        btnOk.setPreferredSize(new java.awt.Dimension(90, 30));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.insets = new java.awt.Insets(0, 5, 0, 0);
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHEAST;
        pnlBody.add(btnOk, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        add(pnlBody, gridBagConstraints);

    }//GEN-END:initComponents
    
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel pnlBody;
    private javax.swing.JPanel pnlUserRoles;
    private javax.swing.JTree trRoles;
    private javax.swing.JButton btnOk;
    private javax.swing.JScrollPane scrPnUserRoles;
    private javax.swing.JLabel lblUserName;
    // End of variables declaration//GEN-END:variables
    
}
