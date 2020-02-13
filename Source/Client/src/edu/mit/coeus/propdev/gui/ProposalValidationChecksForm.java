/*
 * proposalValidationChecks.java
 *
 * Created on January 16, 2004, 12:06 PM
 */

package edu.mit.coeus.propdev.gui;

import edu.mit.coeus.utils.*;
import edu.mit.coeus.gui.*;
import edu.mit.coeus.gui.CoeusAppletMDIForm;
import edu.mit.coeus.gui.CoeusDlgWindow;
import edu.mit.coeus.utils.CoeusGuiConstants;
import edu.mit.coeus.gui.*;
import edu.mit.coeus.businessrules.bean.BusinessRulesBean;
//import edu.mit.coeus.gui.CoeusMessageResources;
import javax.swing.event.*;

import javax.swing.*;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.util.Vector;
import java.awt.event.*;

/**
 *
 * @author  raghusv
 */
public class ProposalValidationChecksForm extends javax.swing.JComponent implements ActionListener {
    
    /** Creates new form proposalValidationChecks */
    private CoeusAppletMDIForm mdiForm;
    
    private BusinessRulesBean businessRulesBean = null;
    
    private CoeusDlgWindow dlgValidationChks;

    private CoeusMessageResources coeusMessageResources;
    
    String msg;
    
    public ProposalValidationChecksForm(CoeusAppletMDIForm mdiForm,Vector vecBusinesRules,String propId) {
        
        initComponents();
        btnPrint.setVisible(false);
        this.mdiForm = mdiForm;
        coeusMessageResources = CoeusMessageResources.getInstance();
        java.awt.Component[] components = {btnOk,btnPrint};
        ScreenFocusTraversalPolicy  traversePolicy = new ScreenFocusTraversalPolicy( components );
        setFocusTraversalPolicy(traversePolicy);
        setFocusCycleRoot(true);
        initialize(vecBusinesRules,propId);
    }

    private void initialize( Vector vecBusinesRules,String propId ){

        btnOk.addActionListener(this);
        btnPrint.addActionListener(this);
        lblPropHeader2.setText(propId);
        for (int i=0 ; i<vecBusinesRules.size() ; i++){
            this.businessRulesBean=(BusinessRulesBean) vecBusinesRules.get(i);
            addComponent(businessRulesBean);
        }
        dlgValidationChks =new CoeusDlgWindow(mdiForm,"Validation Rules",true);

        dlgValidationChks.addWindowListener(new WindowAdapter(){
            public void windowOpened(WindowEvent we){
                 System.out.println("Window is Opened");
                 btnOk.requestFocusInWindow();
                 btnOk.setFocusable(true);
                 btnOk.requestFocus();
            }
        });

        try{
            dlgValidationChks.getContentPane().add( this );
            Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
            Dimension dlgSize = dlgValidationChks.getSize();
            dlgValidationChks.setLocation(200,150);
            dlgValidationChks.setResizable(false);
            dlgValidationChks.pack();
            dlgValidationChks.show();
            
        }catch(Exception exc){
            exc.printStackTrace();
        }
        dlgValidationChks.addEscapeKeyListener( new AbstractAction("escPressed") {
            public void actionPerformed(ActionEvent ar) {
                try{
                    dlgValidationChks.dispose();
                }catch(Exception exc){
                }
            }
        });
    }

    private void addComponent(BusinessRulesBean businessRulesBean){
        
        final int SIZE_X=140, SIZE_Y= 16;
        javax.swing.JScrollPane scrPnDesc;
	javax.swing.JTextArea txtArDesc;
	scrPnDesc = new javax.swing.JScrollPane();
	txtArDesc = new javax.swing.JTextArea();
        
        JPanel pnlValidationComponent;
        java.awt.GridBagConstraints gridBagConstraints;
        pnlValidationComponent = new JPanel();
        
        JLabel lblRuleTypeValue = new JLabel();
        JLabel lblUnitValue =new JLabel();
        JLabel lblDescValue =new JLabel();
        JSeparator jSeparator1 = new JSeparator();
        JLabel  lblRuleType =new JLabel();
        JLabel  lblDesc =new JLabel();
        JLabel  lblUnit =new JLabel();
        String desc = businessRulesBean.getDescription();
        String ruleType = businessRulesBean.getRuleType();
        String unitName = businessRulesBean.getUnitName();   

        desc= (desc == null?"":desc);
        ruleType= (ruleType == null?"":ruleType);
        unitName= (unitName == null?"":unitName);
        
        ruleType.trim();
        if(ruleType.equalsIgnoreCase("V") ){
            ruleType = "Validation";
        }
        pnlValidationComponent.setLayout(new java.awt.GridBagLayout());
        pnlValidationComponent.setLayout(new java.awt.GridBagLayout());
        pnlValidationComponent.setBackground(new java.awt.Color(255, 255, 255));
        pnlValidationComponent.setMaximumSize(new java.awt.Dimension(443, 90));
        pnlValidationComponent.setMinimumSize(new java.awt.Dimension(443, 90));
        pnlValidationComponent.setPreferredSize(new java.awt.Dimension(443, 90));
        
        lblRuleTypeValue.setText(ruleType);
        lblRuleTypeValue.setMaximumSize(new java.awt.Dimension(SIZE_X, 16));
        lblRuleTypeValue.setMinimumSize(new java.awt.Dimension(SIZE_X, 16));
        lblRuleTypeValue.setPreferredSize(new java.awt.Dimension(SIZE_X, 16));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(8, 4, 0, 0);
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 1.0;
        pnlValidationComponent.add(lblRuleTypeValue, gridBagConstraints);

        lblUnitValue.setText(unitName);
        lblUnitValue.setMaximumSize(new java.awt.Dimension(SIZE_X, 16));
        lblUnitValue.setMinimumSize(new java.awt.Dimension(SIZE_X, 16));
        lblUnitValue.setPreferredSize(new java.awt.Dimension(SIZE_X, 16));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(3, 4, 0, 0);
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 1.0;
        pnlValidationComponent.add(lblUnitValue, gridBagConstraints);
        
        //scrPnDesc.setBorder(new javax.swing.border.EmptyBorder(new java.awt.Insets(1, 1, 1, 1)));
        scrPnDesc.setMaximumSize(new java.awt.Dimension(SIZE_X, 30));
        scrPnDesc.setMinimumSize(new java.awt.Dimension(SIZE_X, 30));
        scrPnDesc.setPreferredSize(new java.awt.Dimension(SIZE_X, 30));
        scrPnDesc.setBorder(new javax.swing.border.EmptyBorder(new java.awt.Insets(0, 0, 0, 0)));
       // scrPnDesc.setBorder(javax.swing.border.EmptyBorder);
        //txtArTitle1.setBackground((java.awt.Color) javax.swing.UIManager.getDefaults().get("Panel.background"));
        txtArDesc.setBackground(java.awt.Color.WHITE);
        txtArDesc.setDocument(new LimitedPlainDocument(150));
        txtArDesc.setEditable(false);
        txtArDesc.setFont(CoeusFontFactory.getNormalFont());
        txtArDesc.setLineWrap(true);
        txtArDesc.setWrapStyleWord(true);
        txtArDesc.setBorder(null);
        txtArDesc.setText(desc);
        txtArDesc.setCaretPosition(0);

        scrPnDesc.setViewportView(txtArDesc);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(3, 4, 0, 0);
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 1.0;
        pnlValidationComponent.add(scrPnDesc, gridBagConstraints);
        
        jSeparator1.setForeground(new java.awt.Color(255, 0, 51));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(5, 0, 0, 0);
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        pnlValidationComponent.add(jSeparator1, gridBagConstraints);

        lblRuleType.setFont(CoeusFontFactory.getLabelFont());
        lblRuleType.setText("Rule Type :");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(9, 15, 0, 0);
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        pnlValidationComponent.add(lblRuleType, gridBagConstraints);

        lblDesc.setFont(CoeusFontFactory.getLabelFont());
        lblDesc.setText("Description :");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.insets = new java.awt.Insets(3, 15, 0, 0);
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHEAST;
        pnlValidationComponent.add(lblDesc, gridBagConstraints);

        lblUnit.setFont(CoeusFontFactory.getLabelFont());
        lblUnit.setText("Unit :");
        lblUnit.setMaximumSize(new java.awt.Dimension(38, 16));
        lblUnit.setMinimumSize(new java.awt.Dimension(38, 16));
        lblUnit.setPreferredSize(new java.awt.Dimension(38, 16));
        lblUnit.setHorizontalAlignment(JLabel.RIGHT);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.insets = new java.awt.Insets(3, 15, 0, 0);
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        pnlValidationComponent.add(lblUnit, gridBagConstraints);
        pnlValidationComponent.setAlignmentX((float) 0.0);
        pnlValidationComponent.setAlignmentY((float) 0.0);
        pnlMainValidationCheck.add(pnlValidationComponent);
        
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    private void initComponents() {//GEN-BEGIN:initComponents
        java.awt.GridBagConstraints gridBagConstraints;

        jScrollPane1 = new javax.swing.JScrollPane();
        pnlMainValidationCheck = new javax.swing.JPanel();
        pnlValidationCheckHeader = new javax.swing.JPanel();
        lblPropHeader1 = new javax.swing.JLabel();
        lblPropHeader2 = new javax.swing.JLabel();
        lblPropHeader3 = new javax.swing.JLabel();
        btnPrint = new javax.swing.JButton();
        btnOk = new javax.swing.JButton();

        setLayout(new java.awt.GridBagLayout());

        jScrollPane1.setMinimumSize(new java.awt.Dimension(400, 350));
        jScrollPane1.setPreferredSize(new java.awt.Dimension(450, 450));
        pnlMainValidationCheck.setLayout(new javax.swing.BoxLayout(pnlMainValidationCheck, javax.swing.BoxLayout.Y_AXIS));

        pnlMainValidationCheck.setBackground(new java.awt.Color(255, 255, 255));
        pnlValidationCheckHeader.setLayout(new java.awt.GridBagLayout());

        pnlValidationCheckHeader.setBackground(new java.awt.Color(255, 255, 255));
        pnlValidationCheckHeader.setAlignmentX(0.0F);
        pnlValidationCheckHeader.setAlignmentY(0.0F);
        pnlValidationCheckHeader.setMaximumSize(new java.awt.Dimension(400, 20));
        pnlValidationCheckHeader.setMinimumSize(new java.awt.Dimension(400, 20));
        pnlValidationCheckHeader.setPreferredSize(new java.awt.Dimension(400, 20));
        lblPropHeader1.setFont(CoeusFontFactory.getLabelFont());
        lblPropHeader1.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        lblPropHeader1.setText("Proposal   ");
        lblPropHeader1.setHorizontalTextPosition(javax.swing.SwingConstants.LEADING);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.insets = new java.awt.Insets(0, 5, 0, 0);
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        pnlValidationCheckHeader.add(lblPropHeader1, gridBagConstraints);

        lblPropHeader2.setFont(CoeusFontFactory.getLabelFont());
        lblPropHeader2.setForeground(new java.awt.Color(255, 0, 51));
        lblPropHeader2.setText("\" Prop Number \" ");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        pnlValidationCheckHeader.add(lblPropHeader2, gridBagConstraints);

        lblPropHeader3.setFont(CoeusFontFactory.getLabelFont());
        lblPropHeader3.setText("  failed the following validations");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 1.0;
        pnlValidationCheckHeader.add(lblPropHeader3, gridBagConstraints);

        pnlMainValidationCheck.add(pnlValidationCheckHeader);

        jScrollPane1.setViewportView(pnlMainValidationCheck);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 17;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        add(jScrollPane1, gridBagConstraints);

        btnPrint.setFont(CoeusFontFactory.getLabelFont());
        btnPrint.setMnemonic('P');
        btnPrint.setText("Print");
        btnPrint.setMinimumSize(new java.awt.Dimension(75, 26));
        btnPrint.setPreferredSize(new java.awt.Dimension(75, 26));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 17;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.SOUTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 5, 8, 4);
        add(btnPrint, gridBagConstraints);

        btnOk.setFont(CoeusFontFactory.getLabelFont());
        btnOk.setMnemonic('O');
        btnOk.setText("OK");
        btnOk.setPreferredSize(new java.awt.Dimension(75, 26));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 17;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.SOUTHWEST;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 5, 7, 4);
        add(btnOk, gridBagConstraints);

    }//GEN-END:initComponents

    public void actionPerformed(ActionEvent ae) {
        Object src = ae.getSource();
        if(src.equals(btnOk) ){
            dlgValidationChks.dispose();
        }else if (src.equals(btnPrint)){
            //need to be implemented
            msg = coeusMessageResources.parseMessageKey(
                                    "funcNotImpl_exceptionCode.1100");
            CoeusOptionPane.showInfoDialog(msg);
        }
    }    

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnOk;
    private javax.swing.JButton btnPrint;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel lblPropHeader1;
    private javax.swing.JLabel lblPropHeader2;
    private javax.swing.JLabel lblPropHeader3;
    private javax.swing.JPanel pnlMainValidationCheck;
    private javax.swing.JPanel pnlValidationCheckHeader;
    // End of variables declaration//GEN-END:variables

}
