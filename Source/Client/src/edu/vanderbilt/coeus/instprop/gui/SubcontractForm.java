/*
 * SubContractForm.java
 *
 * Created on May 1, 2013
 * Vanderbilt University Office of Research
 */

package edu.vanderbilt.coeus.instprop.gui;


import java.awt.Color;

import edu.mit.coeus.gui.CoeusFontFactory;

import javax.swing.BorderFactory;
import javax.swing.JTable;

public class SubcontractForm extends javax.swing.JComponent {
    
    /** Creates new form SubContractForm */
    public SubcontractForm() {
        initComponents();
    }
    
    public JTable getTableInstance(){
        return tblEditSubContract;
    }
    
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;
        
        jscrPnEdit = new javax.swing.JScrollPane();
        
        tblEditSubContract = new javax.swing.JTable(){
            public void changeSelection(int row, int column, boolean toggle, boolean extend){
                super.changeSelection(row, column, toggle, extend);
                javax.swing.SwingUtilities.invokeLater( new Runnable() {
                    public void run() {
                        getTableInstance().dispatchEvent(new java.awt.event.KeyEvent(
                            getTableInstance(),java.awt.event.KeyEvent.KEY_PRESSED,0,0,java.awt.event.KeyEvent.VK_F2,
                            java.awt.event.KeyEvent.CHAR_UNDEFINED) );
                    }
                });
            }
        };

	    lblTopHeader = new javax.swing.JLabel();
	    btnAdd = new javax.swing.JButton();
	    btnDelete = new javax.swing.JButton();
	    pnlAmount = new javax.swing.JPanel();
	    tblAmount = new javax.swing.JTable();
	
	    setLayout(new java.awt.GridBagLayout());
	
	    setPreferredSize(new java.awt.Dimension(1000, 400));
	    jscrPnEdit.setBorder(javax.swing.BorderFactory.createEtchedBorder());
	    jscrPnEdit.setMaximumSize(new java.awt.Dimension(782, 350));
	    jscrPnEdit.setMinimumSize(new java.awt.Dimension(782, 350));
	    jscrPnEdit.setPreferredSize(new java.awt.Dimension(782, 350));
	    tblEditSubContract.setModel(new javax.swing.table.DefaultTableModel(
	        new Object [][] {
	            {},
	            {},
	            {},
	            {}
	        },
	        new String [] {
	
	        }
	    ));
	    
	    tblEditSubContract.addMouseListener(new java.awt.event.MouseAdapter() {
	        public void mouseClicked(java.awt.event.MouseEvent evt) {
	            tblEditSubContractMouseClicked(evt);
	        }
	    });
	    
	    jscrPnEdit.setViewportView(tblEditSubContract);
	    
	    gridBagConstraints = new java.awt.GridBagConstraints();
	    gridBagConstraints.gridx = 0;
	    gridBagConstraints.gridy = 1;
	    gridBagConstraints.gridheight = 2;
	    gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
	    gridBagConstraints.insets = new java.awt.Insets(3, 3, 0, 0);
	    add(jscrPnEdit, gridBagConstraints);
	    
	    lblTopHeader.setFont(CoeusFontFactory.getLabelFont());
	    lblTopHeader.setText("Approved Subcontracts for this Award");
	    gridBagConstraints = new java.awt.GridBagConstraints();
	    gridBagConstraints.gridx = 0;
	    gridBagConstraints.gridy = 0;
	    gridBagConstraints.gridwidth = 2;
	    gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
	    gridBagConstraints.insets = new java.awt.Insets(2, 3, 0, 0);
	    add(lblTopHeader, gridBagConstraints);
	
	    btnAdd.setFont(CoeusFontFactory.getLabelFont());
	    btnAdd.setMnemonic('A');
	    btnAdd.setText("Add");
	    btnAdd.setMaximumSize(new java.awt.Dimension(63, 23));
	    btnAdd.setMinimumSize(new java.awt.Dimension(63, 23));
	    btnAdd.setPreferredSize(new java.awt.Dimension(63, 23));
	    gridBagConstraints = new java.awt.GridBagConstraints();
	    gridBagConstraints.gridx = 1;
	    gridBagConstraints.gridy = 1;
	    gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
	    gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
	    gridBagConstraints.insets = new java.awt.Insets(2, 5, 0, 135);
	    add(btnAdd, gridBagConstraints);
	
	    btnDelete.setFont(CoeusFontFactory.getLabelFont());
	    btnDelete.setMnemonic('D');
	    btnDelete.setText("Delete");
	    gridBagConstraints = new java.awt.GridBagConstraints();
	    gridBagConstraints.gridx = 1;
	    gridBagConstraints.gridy = 2;
	    gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
	    gridBagConstraints.insets = new java.awt.Insets(5, 5, 0, 135);
	    add(btnDelete, gridBagConstraints);
	
	    pnlAmount.setLayout(new java.awt.GridBagLayout());
	    
	    pnlAmount.setMinimumSize(new java.awt.Dimension(380, 22));
	    pnlAmount.setPreferredSize(new java.awt.Dimension(380, 22));
	    tblAmount.setBackground(javax.swing.UIManager.getDefaults().getColor("Panel.background"));
	    tblAmount.setBorder(BorderFactory.createLineBorder(Color.GRAY));
	    tblAmount.setModel(new javax.swing.table.DefaultTableModel(
	        new Object [][] {
	            {},
	            {},
	            {},
	            {}
	        },
	        new String [] {
	
	        }
	    ));
	    gridBagConstraints = new java.awt.GridBagConstraints();
	    gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
	    pnlAmount.add(tblAmount, gridBagConstraints);

	    gridBagConstraints = new java.awt.GridBagConstraints();
	    gridBagConstraints.gridx = 0;
	    gridBagConstraints.gridy = 3;
	    gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
	    gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
	    gridBagConstraints.insets = new java.awt.Insets(0, 3, 0, 0);
	    add(pnlAmount, gridBagConstraints);
    }

    private void tblEditSubContractMouseClicked(java.awt.event.MouseEvent evt) {
        javax.swing.SwingUtilities.invokeLater( new Runnable() {
            public void run() {
                getTableInstance().dispatchEvent(new java.awt.event.KeyEvent(
                getTableInstance(),java.awt.event.KeyEvent.KEY_PRESSED,0,0,java.awt.event.KeyEvent.VK_F2,
                java.awt.event.KeyEvent.CHAR_UNDEFINED) );
            }
        });
    }

    public javax.swing.JButton btnAdd;
    public javax.swing.JButton btnDelete;
    public javax.swing.JScrollPane jcrPnDisplay;
    public javax.swing.JScrollPane jscrPnEdit;
    public javax.swing.JLabel lblBottomHeader;
    public javax.swing.JLabel lblTopHeader;
    public javax.swing.JPanel pnlAmount;
    public javax.swing.JTable tblAmount;
    public javax.swing.JTable tblDisplaySubcontract;
    public javax.swing.JTable tblEditSubContract;

}
