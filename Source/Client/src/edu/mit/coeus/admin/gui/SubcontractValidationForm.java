/*
 * SubcontractValidationForm.java
 *
 * Created on January 6, 2005, 10:52 AM
 */

package edu.mit.coeus.admin.gui;

import edu.mit.coeus.exception.CoeusException;
import edu.mit.coeus.gui.CoeusAppletMDIForm;
import edu.mit.coeus.gui.CoeusDlgWindow;
import edu.mit.coeus.gui.CoeusFontFactory;
import edu.mit.coeus.gui.CoeusMessageResources;
import edu.mit.coeus.utils.CoeusOptionPane;
import edu.mit.coeus.utils.CoeusVector;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.AbstractAction;
import javax.swing.JList;
import javax.swing.JPanel;


/**
 *
 * @author  chandrashekara
 */
public class SubcontractValidationForm extends javax.swing.JComponent implements ActionListener {
    private static final String WINDOW_TITLE = "Subcontracting Validation Checks";
    private static final int WIDTH = 580;
    private static final int HEIGHT = 400;
    private CoeusAppletMDIForm mdiForm;
    private CoeusVector validationData;
    private CoeusDlgWindow dlgValidation;
    private CoeusMessageResources coeusMessageResources;
    /** Creates new form SubcontractValidationForm */
    public SubcontractValidationForm(CoeusAppletMDIForm mdiForm,CoeusVector validationData) {
        this.mdiForm = mdiForm;
        this.validationData= validationData;
        coeusMessageResources = CoeusMessageResources.getInstance();
        initComponents();
        registerComponents();
        setFormData();
        postInitComponents();
    }
    
    private void registerComponents(){
        btnOK.addActionListener(this);
        btnPrint.addActionListener(this);
        
    }
    
    public void setFormData(){
        lstMessages.setListData(validationData);
        lstMessages.setFixedCellHeight(20);
        
        lstMessages.setSelectionBackground((java.awt.Color) javax.swing.UIManager.getDefaults().get("Panel.background"));
        lstMessages.setSelectionForeground(java.awt.Color.BLACK);
    }
    
    public void actionPerformed(java.awt.event.ActionEvent actionEvent) {
        Object source = actionEvent.getSource();
        if(source.equals(btnOK)){
            performCloseAction();
        }else if(source.equals(btnPrint)){
            performPrintAction();
        }
    }
    
    
    private void performPrintAction(){
        CoeusOptionPane.showInfoDialog(
            coeusMessageResources.parseMessageKey("funcNotImpl_exceptionCode.1100"));
    }
    
     // Initialize and create the dialog box and set its properticies
    private void postInitComponents() {
        dlgValidation = new CoeusDlgWindow(mdiForm,true);
        dlgValidation.setResizable(false);
        dlgValidation.setModal(true);
        dlgValidation.getContentPane().add(this);
        dlgValidation.setFont(CoeusFontFactory.getLabelFont());
        dlgValidation.setDefaultCloseOperation(CoeusDlgWindow.DO_NOTHING_ON_CLOSE);
        dlgValidation.setSize(WIDTH, HEIGHT);
        dlgValidation.setTitle(WINDOW_TITLE);
        
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        Dimension dlgSize = dlgValidation.getSize();
        dlgValidation.setLocation(screenSize.width/2 - (dlgSize.width/2),
        screenSize.height/2 - (dlgSize.height/2));
        
        dlgValidation.addWindowListener(new WindowAdapter(){
            
            public void windowClosing(WindowEvent we) {
                    performCloseAction();
                    return;
            }
        });
        
        dlgValidation.addEscapeKeyListener(
        new AbstractAction("escPressed"){
            public void actionPerformed(ActionEvent ae) {
                    performCloseAction();
                    return;
            }
        });
        
        dlgValidation.addComponentListener(
        new ComponentAdapter(){
            public void componentShown(ComponentEvent e){
                setWindowFocus();
            }
        });
    }
    
    private void performCloseAction(){
        dlgValidation.setVisible(false);
    }
    
    private void setWindowFocus(){
        btnOK.requestFocusInWindow();
    }
    
    public void display(){
        dlgValidation.setVisible(true);
    }
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    private void initComponents() {//GEN-BEGIN:initComponents
        java.awt.GridBagConstraints gridBagConstraints;

        btnOK = new javax.swing.JButton();
        btnPrint = new javax.swing.JButton();
        lblMessages = new javax.swing.JLabel();
        scrPnMessages = new javax.swing.JScrollPane();
        lstMessages = new javax.swing.JList();

        setLayout(new java.awt.GridBagLayout());

        btnOK.setFont(CoeusFontFactory.getLabelFont());
        btnOK.setMnemonic('O');
        btnOK.setText("OK");
        btnOK.setMaximumSize(new java.awt.Dimension(61, 26));
        btnOK.setMinimumSize(new java.awt.Dimension(61, 26));
        btnOK.setPreferredSize(new java.awt.Dimension(61, 26));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(3, 4, 0, 0);
        add(btnOK, gridBagConstraints);

        btnPrint.setFont(CoeusFontFactory.getLabelFont());
        btnPrint.setMnemonic('P');
        btnPrint.setText("Print");
        btnPrint.setMinimumSize(new java.awt.Dimension(62, 26));
        btnPrint.setPreferredSize(new java.awt.Dimension(62, 26));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(3, 4, 0, 0);
        add(btnPrint, gridBagConstraints);

        lblMessages.setFont(CoeusFontFactory.getLabelFont());
        lblMessages.setText("Messages");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTH;
        gridBagConstraints.insets = new java.awt.Insets(3, 0, 0, 0);
        add(lblMessages, gridBagConstraints);

        scrPnMessages.setMinimumSize(new java.awt.Dimension(500, 350));
        scrPnMessages.setPreferredSize(new java.awt.Dimension(500, 350));
        lstMessages.setBackground((java.awt.Color) javax.swing.UIManager.getDefaults().get("Panel.background"));
        lstMessages.setFont(CoeusFontFactory.getNormalFont());
        lstMessages.setAutoscrolls(false);
        scrPnMessages.setViewportView(lstMessages);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 0, 0);
        add(scrPnMessages, gridBagConstraints);

    }//GEN-END:initComponents

    
    
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    public javax.swing.JButton btnOK;
    public javax.swing.JButton btnPrint;
    public javax.swing.JLabel lblMessages;
    public javax.swing.JList lstMessages;
    public javax.swing.JScrollPane scrPnMessages;
    // End of variables declaration//GEN-END:variables
    
}