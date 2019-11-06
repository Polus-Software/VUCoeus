/*
 * InvestigatorForm.java
 *
 * Created on March 1, 2004, 10:09 AM
 */

package edu.mit.coeus.utils.investigator;

/**
 *
 * @author  chandrashekara
 */
import java.awt.Dimension;

import javax.swing.JButton;

import edu.mit.coeus.gui.CoeusFontFactory;
// JM 11-14-2012 import tooltips
import edu.vanderbilt.coeus.gui.CoeusToolTip;
// JM END
import edu.vanderbilt.coeus.utils.CustomFunctions;

public class InvestigatorForm extends javax.swing.JComponent {

	// JM 11-14-2012 custom button labels; 1-27-2016 changed declaration to non-static for Find Employee
	private static String FIND_NON_EMPLOYEE = "Find Non-Employee";
	public String FIND_EMPLOYEE = "Find VU Employee";
	private static final int BUTTON_WIDTH = 184;
	// JM END
	
	// JM 2-2-2016 dynamic Find Employee button label
	private String acronym, sister;
	// JM END  

    /** Creates new form InvestigatorForm */
    public InvestigatorForm() {
        initComponents();
    }

    public javax.swing.JTable getInvestigatorTableInstance(){
        return tblInvestigator;
    }
    
    public javax.swing.JTable getUnitTableInstance(){
        return tblUnits;
    }
    
    private void initComponents() {
        
    	
        java.awt.GridBagConstraints gridBagConstraints;

        jcrPnInvestigator = new javax.swing.JScrollPane();
        tblInvestigator = new javax.swing.JTable(){
            public void changeSelection(int row, int column, boolean toggle, boolean extend){
                super.changeSelection(row, column, toggle, extend);
                //Added for case id 2229 - start
                if(tblInvestigator.isCellEditable(row,column)){
                    tblInvestigator.editCellAt(row, column);
                }
            }
        };
	    jcrPnUnits = new javax.swing.JScrollPane();
	    tblUnits = new javax.swing.JTable(){
	        public void changeSelection(int row, int column, boolean toggle, boolean extend){
	            super.changeSelection(row, column, toggle, extend);
	            //Added for case id 2229 - start
	            if(tblUnits.isCellEditable(row, column)){
	                tblUnits.editCellAt(row, column);
	            }
	
	        }
	    };
	    btnAdd = new javax.swing.JButton();
	    btnDelete = new javax.swing.JButton();
	    btnFindPerson = new javax.swing.JButton();
	    btnRolodex = new javax.swing.JButton();
	    btnAddUnit = new javax.swing.JButton();
	    btnDelUnit = new javax.swing.JButton();
	    btnFindUnit = new javax.swing.JButton();
	    btnCreditSplit = new javax.swing.JButton();
	    btnAdminType = new javax.swing.JButton();
	    pnlAdministrator = new javax.swing.JPanel();
	    lblAdministrator = new javax.swing.JLabel();
	    lblAdministratorResult = new javax.swing.JLabel();
	    lblAdminPhone = new javax.swing.JLabel();
	    lblAdminPhoneNo = new javax.swing.JLabel();
	    lblAdminEmail = new javax.swing.JLabel();
	    lblAdminEmailValue = new javax.swing.JLabel();
	    btnSyncInv = new javax.swing.JButton();
	    btnDelSyncInv = new javax.swing.JButton();
	
	    setLayout(new java.awt.GridBagLayout());
	    
	    // JM 1-27-2016 set button size
	    JButton[] buttons = {btnAddUnit,btnDelUnit,btnFindUnit,btnCreditSplit,
	    		btnAdd,btnDelete,btnFindPerson,btnRolodex,btnAdminType,btnSyncInv,btnDelSyncInv};
	    
	    for (int i=0; i < buttons.length; i++) {
	    	buttons[i].setPreferredSize(new Dimension(BUTTON_WIDTH,26));
	    }
	    // JM END

	    jcrPnInvestigator.setBorder(javax.swing.BorderFactory.createEtchedBorder());
	    jcrPnInvestigator.setMinimumSize(new java.awt.Dimension(350, 250));
	    jcrPnInvestigator.setPreferredSize(new java.awt.Dimension(350, 250));
	
	    tblInvestigator.setModel(new javax.swing.table.DefaultTableModel(
	        new Object [][] {
	            {},
	            {},
	            {},
	            {}
	        },
	        new String [] {
	
	        }
	    ));
	    tblInvestigator.addMouseListener(new java.awt.event.MouseAdapter() {
	        public void mouseClicked(java.awt.event.MouseEvent evt) {
	            tblInvestigatorMouseClicked(evt);
	        }
	    });
	    jcrPnInvestigator.setViewportView(tblInvestigator);

	    gridBagConstraints = new java.awt.GridBagConstraints();
	    gridBagConstraints.gridx = 0;
	    gridBagConstraints.gridy = 0;
	    gridBagConstraints.gridwidth = 6;
	    gridBagConstraints.gridheight = 7;
	    gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
	    gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
	    gridBagConstraints.weightx = 1.0;
	    gridBagConstraints.insets = new java.awt.Insets(5, 5, 0, 0);
	    add(jcrPnInvestigator, gridBagConstraints);
	
	    jcrPnUnits.setBorder(javax.swing.BorderFactory.createEtchedBorder());
	    jcrPnUnits.setMinimumSize(new java.awt.Dimension(350, 150));
	    jcrPnUnits.setPreferredSize(new java.awt.Dimension(350, 150));
	
	    tblUnits.setModel(new javax.swing.table.DefaultTableModel(
	        new Object [][] {
	            {},
	            {},
	            {},
	            {}
	        },
	        new String [] {
	
	        }
	    ));
	    tblUnits.addMouseListener(new java.awt.event.MouseAdapter() {
	        public void mouseClicked(java.awt.event.MouseEvent evt) {
	            tblUnitsMouseClicked(evt);
	        }
	    });
	    jcrPnUnits.setViewportView(tblUnits);
	
	    gridBagConstraints = new java.awt.GridBagConstraints();
	    gridBagConstraints.gridx = 0;
	    gridBagConstraints.gridy = 7;
	    gridBagConstraints.gridwidth = 6;
	    gridBagConstraints.gridheight = 3;
	    gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
	    gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
	    gridBagConstraints.weightx = 1.0;
	    gridBagConstraints.insets = new java.awt.Insets(5, 5, 0, 0);
	    add(jcrPnUnits, gridBagConstraints);
	
	    btnAdd.setFont(CoeusFontFactory.getLabelFont());
	    btnAdd.setMnemonic('A');
	    btnAdd.setText("Add");
	    gridBagConstraints = new java.awt.GridBagConstraints();
	    gridBagConstraints.gridx = 6;
	    gridBagConstraints.gridy = 0;
	    gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
	    gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
	    gridBagConstraints.insets = new java.awt.Insets(5, 5, 0, 5);
	    add(btnAdd, gridBagConstraints);
	
	    btnDelete.setFont(CoeusFontFactory.getLabelFont());
	    btnDelete.setMnemonic('D');
	    btnDelete.setText("Delete");
	    gridBagConstraints = new java.awt.GridBagConstraints();
	    gridBagConstraints.gridx = 6;
	    gridBagConstraints.gridy = 1;
	    gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
	    gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
	    gridBagConstraints.insets = new java.awt.Insets(5, 5, 0, 5);
	    add(btnDelete, gridBagConstraints);
	
	    btnFindPerson.setFont(CoeusFontFactory.getLabelFont());
	    btnFindPerson.setMnemonic('P');
	    // JM 11-14-2012 updated button label
	    //btnFindPerson.setText("Find Person");
	    btnFindPerson.setText(FIND_EMPLOYEE);
	    btnFindPerson.setToolTipText(CoeusToolTip.getToolTip("personBtn_toolTip.1000"));
	    // JM END
	    gridBagConstraints = new java.awt.GridBagConstraints();
	    gridBagConstraints.gridx = 6;
	    gridBagConstraints.gridy = 2;
	    gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
	    gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
	    gridBagConstraints.insets = new java.awt.Insets(5, 5, 0, 5);
	    add(btnFindPerson, gridBagConstraints);
	
	    btnRolodex.setFont(CoeusFontFactory.getLabelFont());
	    btnRolodex.setMnemonic('R');
	    // JM 11-14-2012 updated button label
	    //btnRolodex.setText("Rolodex");
	    btnRolodex.setText(FIND_NON_EMPLOYEE);
	    btnRolodex.setToolTipText(CoeusToolTip.getToolTip("rolodexBtn_toolTip.1000"));
	    // JM END
	    gridBagConstraints = new java.awt.GridBagConstraints();
	    gridBagConstraints.gridx = 6;
	    gridBagConstraints.gridy = 3;
	    gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
	    gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
	    gridBagConstraints.insets = new java.awt.Insets(5, 5, 0, 5);
	    add(btnRolodex, gridBagConstraints);
	
	    btnAddUnit.setFont(CoeusFontFactory.getLabelFont());
	    btnAddUnit.setMnemonic('n');
	    btnAddUnit.setText("Add Unit");
	    gridBagConstraints = new java.awt.GridBagConstraints();
	    gridBagConstraints.gridx = 6;
	    gridBagConstraints.gridy = 7;
	    gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
	    gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
	    gridBagConstraints.insets = new java.awt.Insets(5, 5, 0, 5);
	    add(btnAddUnit, gridBagConstraints);
	
	    btnDelUnit.setFont(CoeusFontFactory.getLabelFont());
	    btnDelUnit.setMnemonic('l');
	    btnDelUnit.setText("Del Unit");
	    gridBagConstraints = new java.awt.GridBagConstraints();
	    gridBagConstraints.gridx = 6;
	    gridBagConstraints.gridy = 8;
	    gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
	    gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
	    gridBagConstraints.insets = new java.awt.Insets(5, 5, 0, 5);
	    add(btnDelUnit, gridBagConstraints);
	
	    btnFindUnit.setFont(CoeusFontFactory.getLabelFont());
	    btnFindUnit.setMnemonic('U');
	    btnFindUnit.setText("Find Unit");
	    gridBagConstraints = new java.awt.GridBagConstraints();
	    gridBagConstraints.gridx = 6;
	    gridBagConstraints.gridy = 9;
	    gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
	    gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
	    gridBagConstraints.insets = new java.awt.Insets(5, 5, 0, 5);
	    add(btnFindUnit, gridBagConstraints);
	
	    btnCreditSplit.setFont(CoeusFontFactory.getLabelFont());
	    btnCreditSplit.setMnemonic('T');
	    btnCreditSplit.setText("Credit Split");
	    gridBagConstraints = new java.awt.GridBagConstraints();
	    gridBagConstraints.gridx = 6;
	    gridBagConstraints.gridy = 4;
	    gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
	    gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTH;
	    gridBagConstraints.insets = new java.awt.Insets(5, 5, 0, 5);
	    add(btnCreditSplit, gridBagConstraints);
	
	    btnAdminType.setFont(CoeusFontFactory.getLabelFont());
	    btnAdminType.setMnemonic('m');
	    btnAdminType.setText("Administrators");
	    btnAdminType.setToolTipText("Add/Delete Administrator");
	    gridBagConstraints = new java.awt.GridBagConstraints();
	    gridBagConstraints.gridx = 6;
	    gridBagConstraints.gridy = 10;
	    gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
	    gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
	    gridBagConstraints.insets = new java.awt.Insets(6, 5, 0, 5);
	    add(btnAdminType, gridBagConstraints);
	
	    pnlAdministrator.setMinimumSize(new java.awt.Dimension(350, 50));
	    pnlAdministrator.setPreferredSize(new java.awt.Dimension(350, 50));
	    pnlAdministrator.setLayout(new java.awt.GridBagLayout());
	
	    lblAdministrator.setFont(CoeusFontFactory.getLabelFont());
	    lblAdministrator.setMaximumSize(new java.awt.Dimension(150, 15));
	    lblAdministrator.setMinimumSize(new java.awt.Dimension(150, 15));
	    lblAdministrator.setPreferredSize(new java.awt.Dimension(150, 15));
	    gridBagConstraints = new java.awt.GridBagConstraints();
	    gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
	    gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
	    gridBagConstraints.insets = new java.awt.Insets(7, 15, 0, 0);
	    pnlAdministrator.add(lblAdministrator, gridBagConstraints);
	
	    lblAdministratorResult.setMaximumSize(new java.awt.Dimension(250, 15));
	    lblAdministratorResult.setMinimumSize(new java.awt.Dimension(250, 15));
	    lblAdministratorResult.setPreferredSize(new java.awt.Dimension(250, 15));
	    gridBagConstraints = new java.awt.GridBagConstraints();
	    gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
	    gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
	    gridBagConstraints.insets = new java.awt.Insets(6, 0, 0, 0);
	    pnlAdministrator.add(lblAdministratorResult, gridBagConstraints);
	
	    lblAdminPhone.setFont(CoeusFontFactory.getLabelFont());
	    lblAdminPhone.setText("Phone No:");
	    lblAdminPhone.setMaximumSize(new java.awt.Dimension(65, 15));
	    lblAdminPhone.setMinimumSize(new java.awt.Dimension(65, 15));
	    lblAdminPhone.setPreferredSize(new java.awt.Dimension(65, 15));
	    gridBagConstraints = new java.awt.GridBagConstraints();
	    gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
	    gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
	    gridBagConstraints.insets = new java.awt.Insets(7, 0, 0, 0);
	    pnlAdministrator.add(lblAdminPhone, gridBagConstraints);
	
	    lblAdminPhoneNo.setMaximumSize(new java.awt.Dimension(100, 15));
	    lblAdminPhoneNo.setMinimumSize(new java.awt.Dimension(100, 15));
	    lblAdminPhoneNo.setPreferredSize(new java.awt.Dimension(100, 15));
	    gridBagConstraints = new java.awt.GridBagConstraints();
	    gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
	    gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
	    gridBagConstraints.insets = new java.awt.Insets(6, 0, 0, 0);
	    pnlAdministrator.add(lblAdminPhoneNo, gridBagConstraints);
	
	    lblAdminEmail.setFont(CoeusFontFactory.getLabelFont());
	    lblAdminEmail.setText("Email:");
	    lblAdminEmail.setMaximumSize(new java.awt.Dimension(40, 15));
	    lblAdminEmail.setMinimumSize(new java.awt.Dimension(40, 15));
	    lblAdminEmail.setPreferredSize(new java.awt.Dimension(40, 15));
	    gridBagConstraints = new java.awt.GridBagConstraints();
	    gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
	    gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
	    gridBagConstraints.insets = new java.awt.Insets(7, 0, 0, 0);
	    pnlAdministrator.add(lblAdminEmail, gridBagConstraints);
	
	    lblAdminEmailValue.setMaximumSize(new java.awt.Dimension(100, 15));
	    lblAdminEmailValue.setMinimumSize(new java.awt.Dimension(100, 15));
	    lblAdminEmailValue.setPreferredSize(new java.awt.Dimension(100, 15));
	    gridBagConstraints = new java.awt.GridBagConstraints();
	    gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
	    gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
	    gridBagConstraints.weightx = 1.0;
	    gridBagConstraints.weighty = 1.0;
	    gridBagConstraints.insets = new java.awt.Insets(6, 0, 0, 0);
	    pnlAdministrator.add(lblAdminEmailValue, gridBagConstraints);
	
	    gridBagConstraints = new java.awt.GridBagConstraints();
	    gridBagConstraints.gridx = 0;
	    gridBagConstraints.gridy = 10;
	    gridBagConstraints.gridwidth = 6;
	    gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
	    gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
	    gridBagConstraints.weightx = 1.0;
	    gridBagConstraints.weighty = 1.0;
	    gridBagConstraints.insets = new java.awt.Insets(5, 5, 0, 0);
	    add(pnlAdministrator, gridBagConstraints);
	
	    btnSyncInv.setFont(CoeusFontFactory.getLabelFont());
	    btnSyncInv.setMnemonic('S');
	    btnSyncInv.setText("Sync to Children");
	    gridBagConstraints = new java.awt.GridBagConstraints();
	    gridBagConstraints.gridx = 6;
	    gridBagConstraints.gridy = 5;
	    gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
	    gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTH;
	    gridBagConstraints.insets = new java.awt.Insets(5, 5, 0, 5);
	    add(btnSyncInv, gridBagConstraints);
	
	    btnDelSyncInv.setFont(CoeusFontFactory.getLabelFont());
	    btnDelSyncInv.setMnemonic('e');
	    btnDelSyncInv.setText("Delete & Sync");
	    gridBagConstraints = new java.awt.GridBagConstraints();
	    gridBagConstraints.gridx = 6;
	    gridBagConstraints.gridy = 6;
	    gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
	    gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTH;
	    gridBagConstraints.weighty = 1.0;
	    gridBagConstraints.insets = new java.awt.Insets(5, 5, 0, 5);
	    add(btnDelSyncInv, gridBagConstraints);
    }

    private void tblUnitsMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblUnitsMouseClicked
        //Commented for case id 2229 - start
//        javax.swing.SwingUtilities.invokeLater( new Runnable() {
//            public void run() {
//                getInvestigatorTableInstance().dispatchEvent(new java.awt.event.KeyEvent(
//                getInvestigatorTableInstance(),java.awt.event.KeyEvent.KEY_PRESSED,0,0,java.awt.event.KeyEvent.VK_F2,
//                java.awt.event.KeyEvent.CHAR_UNDEFINED) );
//            }
//        });
        //Commented for case id 2229 - end
    }//GEN-LAST:event_tblUnitsMouseClicked

    private void tblInvestigatorMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblInvestigatorMouseClicked
         //Commented for case id 2229 - start
//         javax.swing.SwingUtilities.invokeLater( new Runnable() {
//            public void run() {
//                getUnitTableInstance().dispatchEvent(new java.awt.event.KeyEvent(
//                getUnitTableInstance(),java.awt.event.KeyEvent.KEY_PRESSED,0,0,java.awt.event.KeyEvent.VK_F2,
//                java.awt.event.KeyEvent.CHAR_UNDEFINED) );
//            }
//        });
         //Commented for case id 2229 - end

    }//GEN-LAST:event_tblInvestigatorMouseClicked

    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    public javax.swing.JButton btnAdd;
    public javax.swing.JButton btnAddUnit;
    public javax.swing.JButton btnAdminType;
    public javax.swing.JButton btnCreditSplit;
    public javax.swing.JButton btnDelSyncInv;
    public javax.swing.JButton btnDelUnit;
    public javax.swing.JButton btnDelete;
    public javax.swing.JButton btnFindPerson;
    public javax.swing.JButton btnFindUnit;
    public javax.swing.JButton btnRolodex;
    public javax.swing.JButton btnSyncInv;
    public javax.swing.JScrollPane jcrPnInvestigator;
    public javax.swing.JScrollPane jcrPnUnits;
    public javax.swing.JLabel lblAdminEmail;
    public javax.swing.JLabel lblAdminEmailValue;
    public javax.swing.JLabel lblAdminPhone;
    public javax.swing.JLabel lblAdminPhoneNo;
    public javax.swing.JLabel lblAdministrator;
    public javax.swing.JLabel lblAdministratorResult;
    public javax.swing.JPanel pnlAdministrator;
    public javax.swing.JTable tblInvestigator;
    public javax.swing.JTable tblUnits;
    // End of variables declaration//GEN-END:variables
 
}
