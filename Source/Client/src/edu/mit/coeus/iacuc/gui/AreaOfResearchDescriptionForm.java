/*
 * @(#)AreaOfResearchDescriptionForm.java
 *
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 *
 */

/* PMD check performed, and commented unused imports and variables on 09-AUGUST-2010
 * by Johncy M John
 */

package edu.mit.coeus.iacuc.gui;

import edu.mit.coeus.gui.CoeusAppletMDIForm;
import edu.mit.coeus.gui.CoeusDlgWindow;
import edu.mit.coeus.gui.CoeusMessageResources;
import edu.mit.coeus.utils.JTextFieldFilter;
import edu.mit.coeus.utils.LimitedPlainDocument;
import javax.swing.*;
import java.awt.event.*;
import java.awt.*;
//import edu.mit.coeus.utils.*;
//import edu.mit.coeus.gui.*;
import edu.mit.coeus.iacuc.bean.*;
import edu.mit.coeus.utils.CoeusTextField;
import edu.mit.coeus.utils.CoeusOptionPane;
import edu.mit.coeus.gui.CoeusFontFactory;

/**
 * The class construct the dialog window for with Area Of Research Code, Parent 
 * Research Code and its description. This component is used in Tree Node Add &
 * Modify(Area of Research) Operation. It provides the privliages ( like enable
 * /disable certain visual components) confined to specific type of Operation.
 *
 * @author  Subramanya
 * @version 1.0 September 23, 2002, 8:50 PM
 */

public class AreaOfResearchDescriptionForm extends JComponent
                                            implements ActionListener {

    //Main MDI Form.
    private CoeusAppletMDIForm mdiForm = null;

    // AreaOfResearchTreeNodeBean instance to hold the data value of new AOR.
    private AreaOfResearchTreeNodeBean updateNodeInfo = null;

    // Hold the Add/Modify JDialog instance.
    private CoeusDlgWindow  dlgAddModify = null;
    //private JDialog  dlgAddModify = null;

    //holds the data changed falg  for key event
    private boolean dataChanged = false;

    //holds the Add/modify window signal falg.
    private boolean isAddWindow = false;
    
    //holds the "Cancel Button" clicked/not signal falg.
    private boolean cancelClik = false;
    
    //holds CoeusMessageResources instance used for reading message Properties.
    private CoeusMessageResources coeusMessageResources;

    private CoeusFontFactory coeusFontFactory;
    //holds the component view compoenents
    private JPanel pnlResearchData;
    private JLabel lblParentCode;
    private JLabel lblResearchCode;
    private JLabel lblResDesc;
    private CoeusTextField txtParentCode;
    private CoeusTextField txtResearchCode;
    private JScrollPane jcrPnResearchDesc;
    private JTextArea txtArResDes;
    private JPanel pnlButton;
    private JButton btnOk;
    private JButton btnCancel;

    /**
     * Creates new form AreaOfResearchDescriptionForm
     * @param   mdiForm             main window form of Area Of Research Window.
     *                              Add/Modify Screen.
     * @param   newNodeInfo         Area Of Research Data Bean .
     * @param   isAORCodeEnabled    True- Add / False - Modify
     */
    public AreaOfResearchDescriptionForm( CoeusAppletMDIForm mdiForm,
                                       AreaOfResearchTreeNodeBean newNodeInfo,
                                       boolean isAORCodeEnabled ) {

        this.mdiForm = mdiForm;
        dlgAddModify = new CoeusDlgWindow( mdiForm,  true );
        this.updateNodeInfo = newNodeInfo;
        initComponents();
        this.isAddWindow = isAORCodeEnabled;
        dlgAddModify.setSize( new Dimension( 400, 290 ) );
        dlgAddModify.setResizable( false );
        dlgAddModify.setLocationRelativeTo( null );
        txtArResDes.requestFocus();
    }

    /** This method is called from within the constructor to
     * initialize the form.
     */
    private void initComponents(){

        coeusMessageResources = CoeusMessageResources.getInstance();
        
        pnlResearchData = new javax.swing.JPanel();
        lblParentCode = new javax.swing.JLabel();
        lblResearchCode = new javax.swing.JLabel();
        lblResDesc = new javax.swing.JLabel();
        txtParentCode = new CoeusTextField(
                                             updateNodeInfo.getParentNodeID() );
        txtParentCode.setFont(CoeusFontFactory.getNormalFont());

        txtResearchCode = new CoeusTextField();
        txtResearchCode.setFont(CoeusFontFactory.getNormalFont());

        txtResearchCode.addKeyListener( new KeyAdapter(){
            public void keyReleased( KeyEvent key ){                
                if( txtResearchCode.getText().trim().length() > 0 &&
                          txtArResDes.getText().trim().length() > 0  ){
                    dataChanged = true;        
                    btnOk.setEnabled( true );
                }else{
                    dataChanged = false;        
                }

           }
        });

        JTextFieldFilter resCodeFilter = new JTextFieldFilter();
        resCodeFilter.setMaxLength( 8 );
        resCodeFilter.AddAcceptedCharacters(".");
        txtResearchCode.setDocument( resCodeFilter );
        txtResearchCode.setText( updateNodeInfo.getNodeID() );

        jcrPnResearchDesc = new javax.swing.JScrollPane();
        txtArResDes = new javax.swing.JTextArea();
        txtArResDes.setFont( CoeusFontFactory.getNormalFont() );
        txtArResDes.addKeyListener( new KeyAdapter(){
            public void keyReleased( KeyEvent key ){
                
                if ( ( txtResearchCode.getText().trim().length() > 0 ) &&
                     ( txtArResDes.getText().trim().length() > 0 ) ) {
                         dataChanged = true;
                         btnOk.setEnabled( true );
                }else {
                         dataChanged = false;
                         btnOk.setEnabled( false );
                }

            }
        });

        JTextFieldFilter resDescFilter = new JTextFieldFilter();
        resDescFilter.setMaxLength( 200 );

        pnlButton = new JPanel();
        btnOk = new JButton(); 
        btnOk.addActionListener( this );
        btnOk.setMnemonic( 'O' );
        
        btnCancel = new JButton();
        btnCancel.addActionListener( this );
        btnCancel.setMnemonic( 'C' );

        dlgAddModify.getContentPane().setLayout(new GridBagLayout());
        GridBagConstraints gridBagConstraints1;

        pnlResearchData.setLayout( new GridBagLayout() );
        GridBagConstraints gridBagConstraints2;

        lblParentCode.setText("Parent Research Area Code");              
        lblParentCode.setFont( CoeusFontFactory.getLabelFont() );
        
        gridBagConstraints2 = new java.awt.GridBagConstraints();
        gridBagConstraints2.gridx = 0;
        gridBagConstraints2.gridy = 0;
        gridBagConstraints2.ipadx = 50;
        gridBagConstraints2.ipady = 2;
        gridBagConstraints2.insets = new Insets(6, 6, 0, 0);
        gridBagConstraints2.anchor = GridBagConstraints.WEST;
        pnlResearchData.add(lblParentCode, gridBagConstraints2);

        lblResearchCode.setText("Research Area Code");
        lblResearchCode.setPreferredSize( new Dimension(75,50));
        lblResearchCode.setFont( CoeusFontFactory.getLabelFont() );

        gridBagConstraints2 = new GridBagConstraints();
        gridBagConstraints2.gridx = 0;
        gridBagConstraints2.gridy = 1;
        gridBagConstraints2.ipady = 2;
        gridBagConstraints2.insets = new Insets(6, 6, 0, 0);
        gridBagConstraints2.anchor = GridBagConstraints.WEST;
        pnlResearchData.add(lblResearchCode, gridBagConstraints2);

        lblResDesc.setText("Research Area");
        lblResDesc.setPreferredSize( new Dimension(75,50));
        lblResDesc.setFont( CoeusFontFactory.getLabelFont() );

        gridBagConstraints2 = new GridBagConstraints();
        gridBagConstraints2.gridx = 0;
        gridBagConstraints2.gridy = 2;
        gridBagConstraints2.insets = new Insets(4, 6, 0, 0);
        gridBagConstraints2.anchor = GridBagConstraints.WEST;
        pnlResearchData.add(lblResDesc, gridBagConstraints2);

        txtParentCode.setMinimumSize(new Dimension(63, 20));
        txtParentCode.setPreferredSize(new Dimension(60, 20));
        txtParentCode.setEditable( false );

        gridBagConstraints2 = new GridBagConstraints();
        gridBagConstraints2.gridx = 1;
        gridBagConstraints2.gridy = 0;
        gridBagConstraints2.insets = new Insets(4, 6, 0, 7);
        gridBagConstraints2.anchor = GridBagConstraints.WEST;
        pnlResearchData.add(txtParentCode, gridBagConstraints2);

        txtResearchCode.setMinimumSize(new Dimension(63, 20));
        txtResearchCode.setPreferredSize(new Dimension(60, 20));

        gridBagConstraints2 = new GridBagConstraints();
        gridBagConstraints2.gridx = 1;
        gridBagConstraints2.gridy = 1;
        gridBagConstraints2.fill = GridBagConstraints.HORIZONTAL;
        gridBagConstraints2.insets = new Insets(4, 6, 0, 7);
        gridBagConstraints2.anchor = GridBagConstraints.WEST;
        pnlResearchData.add(txtResearchCode, gridBagConstraints2);

        txtArResDes.setColumns(25);
        txtArResDes.setRows(4);
        // Added by chandra - 26/8/2003
        txtArResDes.setLineWrap(true);
        txtArResDes.setWrapStyleWord(true);
        // Chandra End
        LimitedPlainDocument txtArDoc = new LimitedPlainDocument(200);
        txtArResDes.setDocument( txtArDoc );
        txtArResDes.setText( updateNodeInfo.getRADescription().trim() );

        txtArResDes.setPreferredSize( new Dimension( 100, 75 ) );
        jcrPnResearchDesc.setViewportView(txtArResDes);

        gridBagConstraints2 = new GridBagConstraints();
        gridBagConstraints2.gridx = 0;
        gridBagConstraints2.gridy = 3;
        gridBagConstraints2.gridwidth = 2;
        gridBagConstraints2.fill = GridBagConstraints.HORIZONTAL;
        gridBagConstraints2.ipadx = 54;
        gridBagConstraints2.ipady = 101;
        gridBagConstraints2.insets = new Insets(4, 6, 5, 6);
        pnlResearchData.add(jcrPnResearchDesc, gridBagConstraints2);

        gridBagConstraints1 = new GridBagConstraints();
        dlgAddModify.getContentPane().add(pnlResearchData, gridBagConstraints1);

        pnlButton.setLayout(new GridBagLayout());
        java.awt.GridBagConstraints gridBagConstraints3;

        btnOk.setText("OK");
        // Added by Chandra - 8/27/03
        btnOk.setFont(coeusFontFactory.getLabelFont());
        
        btnOk.setEnabled( false );

        gridBagConstraints3 = new GridBagConstraints();
        //modified by manoj to fix the bug 02/09/2003
        btnOk.setPreferredSize(new Dimension(79, 20));
        btnOk.setMinimumSize(new Dimension(79,20));
        btnOk.setMaximumSize(new Dimension(79,20));
        gridBagConstraints3.gridx = 0;
        gridBagConstraints3.gridy = 0;
        //gridBagConstraints3.ipadx = 24;
        gridBagConstraints3.insets = new Insets(2, 10, 0, 2);
        gridBagConstraints3.anchor = GridBagConstraints.NORTH;
        pnlButton.add(btnOk, gridBagConstraints3);

        btnCancel.setText("Cancel");
        // Added by Chandra - 8/27/03
        btnCancel.setFont(coeusFontFactory.getLabelFont());
        // End
       
        //modified by manoj to fix the bug 02/09/2003        
        btnCancel.setPreferredSize(new Dimension(79, 20));
        btnCancel.setMinimumSize(new Dimension(79,20));
        btnCancel.setMaximumSize(new Dimension(79,20));        
        gridBagConstraints3 = new GridBagConstraints();
        gridBagConstraints3.gridx = 0;
        gridBagConstraints3.gridy = 1;
        gridBagConstraints3.insets = new Insets(5, 10, 0, 1);
        pnlButton.add(btnCancel, gridBagConstraints3);

        gridBagConstraints1 = new GridBagConstraints();
        gridBagConstraints1.anchor = GridBagConstraints.NORTH;
        dlgAddModify.getContentPane().add(pnlButton, gridBagConstraints1);


    }


    /**
     * This Method is used to Enable and disable Area of Research Code
     * TextField to providing editing or only display option. This is in
     * sync with Modify/Edit routine respectively.
     * @param   isEnabled       flag boolean value.
     */
    public void setResearchAreaCodeComponent( boolean isEnabled ){
        txtResearchCode.setBackground( java.awt.Color.lightGray );
        txtResearchCode.setEnabled( false );
    }



    /** Method Overriden to Handle Button Actions like OK/CANCEL.
     * @param  actionType      Type of Button Pressed.     
     */
    public void actionPerformed( ActionEvent actionType ){

            Object actSource = actionType.getSource();
            if( actSource.equals( btnOk ) ){

                updateAreaOfResearchNodeBean();
                dlgAddModify.dispose();

            }else if( actSource.equals( btnCancel ) ){
                
                dialogCloseConfirmation();

            }

    }


    /**
     * This method is used to show the Coeus Dialog.
     * @param str represent the Name of the Window like Add/Modify
     */
    public void showDialog( String str ){

        dlgAddModify.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
        dlgAddModify.addWindowListener(new WindowAdapter(){
            public void windowOpened(WindowEvent we){
                dataChanged = false;
                if( isAddWindow ){ 
                    txtResearchCode.requestFocus();
                }else{
                    txtResearchCode.setEditable( isAddWindow );
                    txtArResDes.requestFocus();
                }

            }

            public void windowClosing(WindowEvent we){
                dialogCloseConfirmation();
            }
        });
        dlgAddModify.addEscapeKeyListener(
            new AbstractAction("escPressed") {
                public void actionPerformed(ActionEvent ae) {
                    dialogCloseConfirmation();
                }
        });
            
        dlgAddModify.setTitle( str );
        dlgAddModify.show();

    }

    //supporting method for close confiramtion.
    private void dialogCloseConfirmation(){
        
        //COEUSQA-2684  IACUC - Areas of research maintenance screen for IACUC areas of research
        setCancelClik(true);
        if( dataChanged ) {

            int isClose = CoeusOptionPane.showQuestionDialog(
                                coeusMessageResources.parseMessageKey(
                                        "saveConfirmCode.1002"), 
                                CoeusOptionPane.OPTION_YES_NO_CANCEL, 
                                CoeusOptionPane.DEFAULT_YES);

            if( isClose == JOptionPane.NO_OPTION ){
                    dlgAddModify.dispose();
            }else if( isClose == JOptionPane.YES_OPTION ){
                    updateAreaOfResearchNodeBean();
                    dlgAddModify.dispose();
            }
        }else{

            dlgAddModify.dispose();

        }
    }

    //supporting method to update AreaOfResearchTreeNode Bean Data
    private void updateAreaOfResearchNodeBean(){

        updateNodeInfo.setResearchAreaCode(
                                             txtResearchCode.getText().trim() );
        updateNodeInfo.setRADescription( txtArResDes.getText().trim() );
    }

    /**
     * Method gives the Area Of Research Data bean of newly add node.
     *
     * @return AreaOfResearchTreeNodeBean newly added Data Area Of Research
     *                                    Data Bean.
     */
    public AreaOfResearchTreeNodeBean getNewResearchArea(){
         return updateNodeInfo;
    }

     /**
     * Method gives the status of "Cancel" button , Clicked or not.  
     * @return boolean value for Cancel button's clicked status.
     */
    public boolean isCancelClik() {
        return cancelClik;
    }

    /**
     * Method sets the status of "Cancel" button , Clicked or not.  
     * @param boolean value for Cancel button's clicked status.
     */
    public void setCancelClik(boolean cancelClik) {
        this.cancelClik = cancelClik;
    }
}