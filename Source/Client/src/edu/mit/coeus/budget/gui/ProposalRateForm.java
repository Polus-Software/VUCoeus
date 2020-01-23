/*
 * ProposalRateForm.java
 *
 * Created on September 26, 2003, 12:04 PM
 */

package edu.mit.coeus.budget.gui;

import javax.swing.*;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.util.Hashtable;

import edu.mit.coeus.utils.CurrencyField;
import edu.mit.coeus.gui.CoeusDlgWindow;
import edu.mit.coeus.gui.CoeusFontFactory;
import edu.mit.coeus.budget.bean.InstituteRatesBean;
import edu.mit.coeus.budget.bean.InstituteLARatesBean;
import edu.mit.coeus.budget.bean.ProposalRatesBean;
import edu.mit.coeus.budget.bean.ProposalLARatesBean;

/** Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 * @author  ranjeeva
 */
public class ProposalRateForm extends javax.swing.JComponent{
    
    /** CoeusDlgWindow instance */    
    public CoeusDlgWindow dlgProposalRateForm;
    boolean modal;
    private Component parent;
    /** function Type */    
    public char functionType;
    private static final int WIDTH = 650;
    private static final int HEIGHT = 350;
    
    /** hashtable of instTabPane */    
    public Hashtable instTabPanelHastTab = new Hashtable();
    /** hashtable of laTabPane */    
    public Hashtable laRateTabPanel = new Hashtable();
    
    
    /** Creates new form ProposalRateForm */
    public ProposalRateForm(Component parent, boolean modal) {
        //super(parent, modal);
        this.modal = modal;
        this.parent = parent;
        //initComponents();
        initUiComponents();
        initialiseDialogWindow();
        showForm();
    }
    
    /**
     * To display the window containing the Form
     */
    
    public void showForm(){
        dlgProposalRateForm.setVisible(false);
    }
    
    /** Use to Initialise the Dialog window containing this Proposal Rate Form
     */
    public void initialiseDialogWindow(){
        dlgProposalRateForm = new CoeusDlgWindow((java.awt.Frame) parent,"",modal);
        dlgProposalRateForm.getContentPane().add(this);
        dlgProposalRateForm.pack();
        dlgProposalRateForm.setResizable(false);
        dlgProposalRateForm.setVisible(false);
        dlgProposalRateForm.setFont(CoeusFontFactory.getLabelFont());
        dlgProposalRateForm.setSize(WIDTH,HEIGHT);
        
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        Dimension dlgSize = dlgProposalRateForm.getSize();
        dlgProposalRateForm.setLocation(screenSize.width/2 - (dlgSize.width/2),
        screenSize.height/2 - (dlgSize.height/2));
        
    }
    
    /**
     * Method for Adding each Row of values in beans as Institute Rate Class and Type Panel rows to the TabPane
     * Each Values goes as a panel instance to the TabPane which holds the value of Rate Class and Type 
     */
    
    public JPanel setInstRateClassPanelRows(Object bean) {
        
        JPanel pnlRatesClassTypeRow = new JPanel();
        javax.swing.JLabel lblRatesClassValue = new javax.swing.JLabel();
        javax.swing.JLabel lblRatesTypeValue = new javax.swing.JLabel();
        java.awt.GridBagConstraints gridBagConstraints = new java.awt.GridBagConstraints();
        
        pnlRatesClassTypeRow.setLayout(new java.awt.GridBagLayout());
        pnlRatesClassTypeRow.setMaximumSize(new java.awt.Dimension(650, 20));
        pnlRatesClassTypeRow.setMinimumSize(new java.awt.Dimension(650, 20));
        pnlRatesClassTypeRow.setPreferredSize(new java.awt.Dimension(650, 20));
        
        lblRatesClassValue.setFont(edu.mit.coeus.gui.CoeusFontFactory.getNormalFont());
        
        lblRatesClassValue.setMaximumSize(new java.awt.Dimension(150, 16));
        lblRatesClassValue.setMinimumSize(new java.awt.Dimension(150, 16));
        lblRatesClassValue.setPreferredSize(new java.awt.Dimension(150, 16));
        
        
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 0.2;
        pnlRatesClassTypeRow.add(lblRatesClassValue, gridBagConstraints);
        
        lblRatesTypeValue.setFont(edu.mit.coeus.gui.CoeusFontFactory.getNormalFont());
        
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 1.0;
        pnlRatesClassTypeRow.add(lblRatesTypeValue, gridBagConstraints);
        
        if(bean instanceof InstituteRatesBean) {
            InstituteRatesBean instituteRatesBean = (InstituteRatesBean) bean;
            lblRatesClassValue.setText(" "+instituteRatesBean.getRateClassDescription());
            lblRatesTypeValue.setText(instituteRatesBean.getRateTypeDescription());
        }
        
        if(bean instanceof ProposalRatesBean) {
            ProposalRatesBean proposalRatesBean = (ProposalRatesBean) bean;
            lblRatesClassValue.setText(" "+proposalRatesBean.getRateClassDescription());
            lblRatesTypeValue.setText(proposalRatesBean.getRateTypeDescription());
            
        }
        
        return pnlRatesClassTypeRow;
    }
    
    /**
     * Method for Adding each Row of values in beans as LA Rate Class and Type Panel row to the TabPane
     * Each Values goes as a panel instance to the TabPane which holds the value LA Rate Class and Type
     */
    
    public JPanel setLARateClassPanelRows(Object bean) {
        
        JPanel pnlLARatesClassTypeRow = new JPanel();
        pnlLARatesClassTypeRow.setLayout(new java.awt.GridBagLayout());
        pnlLARatesClassTypeRow.setMaximumSize(new java.awt.Dimension(650, 20));
        pnlLARatesClassTypeRow.setMinimumSize(new java.awt.Dimension(650, 20));
        pnlLARatesClassTypeRow.setPreferredSize(new java.awt.Dimension(650, 20));
        
        javax.swing.JLabel lblLabRatesClassValue =  new javax.swing.JLabel();
        javax.swing.JLabel lblLabRatesTypeValue =  new javax.swing.JLabel();
        java.awt.GridBagConstraints gridBagConstraints = new java.awt.GridBagConstraints();
        
        lblLabRatesClassValue.setFont(edu.mit.coeus.gui.CoeusFontFactory.getNormalFont());
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        pnlLARatesClassTypeRow.add(lblLabRatesClassValue, gridBagConstraints);
        
        lblLabRatesTypeValue.setFont(edu.mit.coeus.gui.CoeusFontFactory.getNormalFont());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 1.0;
        pnlLARatesClassTypeRow.add(lblLabRatesTypeValue, gridBagConstraints);
        
        if(bean instanceof InstituteLARatesBean) {
            InstituteLARatesBean instituteLARatesBean = (InstituteLARatesBean) bean;
            lblLabRatesTypeValue.setText(" "+instituteLARatesBean.getRateClassDescription());
            lblLabRatesClassValue.setText(instituteLARatesBean.getRateTypeDescription());
        }
        
        if(bean instanceof ProposalLARatesBean) {
            ProposalLARatesBean proposalLARatesBean = (ProposalLARatesBean) bean;
            lblLabRatesTypeValue.setText(" "+proposalLARatesBean.getRateClassDescription());
            lblLabRatesClassValue.setText(proposalLARatesBean.getRateTypeDescription());
            
        }
        
        return pnlLARatesClassTypeRow;
    }
  
    /**
     * To Set the Title of the Proposal Rate Dialog Window
     */
    
    public void  setTitle(String title) {
        dlgProposalRateForm.setTitle(title);
    }
    
    /**
     * To get the desired Title of the Proposal Rate Dialog Window
     */
    
    public String  getFormTitle(String proposalNo,String versionNumber,String functionTypeValue) {
        return functionTypeValue+" Rates for Proposal "+proposalNo+", Version "+versionNumber ;//"Modify/Display Rates for Proposal 11111,Version 1";
    }
    
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    private void initUiComponents() {
        java.awt.GridBagConstraints gridBagConstraints;
        
        pnlProposalRateMainPanel = new javax.swing.JPanel();
        btnOK = new javax.swing.JButton();
        btnCancel = new javax.swing.JButton();
        btnReset = new javax.swing.JButton();
        btnSync = new javax.swing.JButton();
        tbdRatesTabPane = new javax.swing.JTabbedPane();
        pnlInstituteTabPane = new javax.swing.JPanel();
        pnlInstTabHeader = new javax.swing.JPanel();
        lbllnstituteTabHead = new javax.swing.JLabel();
        txtArInstFiscalYearHead = new javax.swing.JTextArea();
        txtArInstCampusHead = new javax.swing.JTextArea();
        txtArInstActivityTypeHead = new javax.swing.JTextArea();
        txtArInstStartDateHead = new javax.swing.JTextArea();
        txtArInstituteRateHead = new javax.swing.JTextArea();
        txtArInstApplicRateHead = new javax.swing.JTextArea();
        pnlInstTabDataPanel = new javax.swing.JPanel();
        scrPnInstTabDataPanel = new javax.swing.JScrollPane();
        pnlInstRateTabularPanel = new javax.swing.JPanel();
        pnlEachInstRatePanelGroup = new javax.swing.JPanel();
        pnlInstRatesClassData = new javax.swing.JPanel();
        lblInstRatesClassValue = new javax.swing.JLabel();
        lblInstRatesTypeValue = new javax.swing.JLabel();
        pnlInstRatesEachTabularValue = new javax.swing.JPanel();
        lblInstBlankSpace = new javax.swing.JLabel();
        lblInstRatesYearValue = new javax.swing.JLabel();
        lblInstCampusValue = new javax.swing.JLabel();
        lblInstActivityTypevalue = new javax.swing.JLabel();
        lblInstStartDateValue = new javax.swing.JLabel();
        lblInstInstituteRateValue = new javax.swing.JLabel();
        // txtInstApplicRatesValue = new javax.swing.JTextField();
        
        pnlLATabPane = new javax.swing.JPanel();
        pnlLATabHeader = new javax.swing.JPanel();
        lblLARateClassHead = new javax.swing.JLabel();
        txtArLAFiscalYearHead = new javax.swing.JTextArea();
        txtArLACampusHead = new javax.swing.JTextArea();
        txtArLAStartDateHead = new javax.swing.JTextArea();
        txtArLAEndDateHead = new javax.swing.JTextArea();
        txtArLAApplicDateHead = new javax.swing.JTextArea();
        pnlLARateDataPanel = new javax.swing.JPanel();
        scrPnLARatesData = new javax.swing.JScrollPane();
        pnlLATabularPanel = new javax.swing.JPanel();
        pnlEachLARatePanelGroup = new javax.swing.JPanel();
        pnlLabRatesClassData = new javax.swing.JPanel();
        lblLabRatesClassValue = new javax.swing.JLabel();
        lblLabRatesTypeValue = new javax.swing.JLabel();
        pnlLabRatesEachTabularValue = new javax.swing.JPanel();
        lblLABlankSpace = new javax.swing.JLabel();
        lblLARatesYearValue = new javax.swing.JLabel();
        lblLACampusValue = new javax.swing.JLabel();
        lblLAStartDateValue = new javax.swing.JLabel();
        lblLAInstituteRateValue = new javax.swing.JLabel();
        // txtLAApplicRatesValue = new javax.swing.JTextField();
        
        this.setLayout(new java.awt.GridBagLayout());
        setBackground((java.awt.Color) javax.swing.UIManager.getDefaults().get("Panel.background"));
        
        pnlProposalRateMainPanel.setLayout(new java.awt.GridBagLayout());
        pnlProposalRateMainPanel.setAlignmentX(0.0F);
        pnlProposalRateMainPanel.setAlignmentY(0.0F);
        pnlProposalRateMainPanel.setMinimumSize(new java.awt.Dimension(650, 350));
        pnlProposalRateMainPanel.setPreferredSize(new java.awt.Dimension(650, 350));
        btnOK.setFont(edu.mit.coeus.gui.CoeusFontFactory.getLabelFont());
        btnOK.setMnemonic('O');
        btnOK.setText("OK");
        btnOK.setAlignmentY(0.0F);
        
        btnOK.setMaximumSize(new java.awt.Dimension(75, 25));
        btnOK.setMinimumSize(new java.awt.Dimension(75, 25));
        btnOK.setPreferredSize(new java.awt.Dimension(75, 25));
        
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(25, 0, 5, 0);
        pnlProposalRateMainPanel.add(btnOK, gridBagConstraints);
        
        btnCancel.setFont(edu.mit.coeus.gui.CoeusFontFactory.getLabelFont());
        btnCancel.setMnemonic('C');
        btnCancel.setText("Cancel");
        
        btnCancel.setMaximumSize(new java.awt.Dimension(75, 25));
        btnCancel.setMinimumSize(new java.awt.Dimension(75, 25));
        btnCancel.setPreferredSize(new java.awt.Dimension(75, 25));
        
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 5, 0);
        pnlProposalRateMainPanel.add(btnCancel, gridBagConstraints);
        
        btnReset.setFont(edu.mit.coeus.gui.CoeusFontFactory.getLabelFont());
        btnReset.setMnemonic('R');
        btnReset.setText("Reset");
        
        btnReset.setMaximumSize(new java.awt.Dimension(75, 25));
        btnReset.setMinimumSize(new java.awt.Dimension(75, 25));
        btnReset.setPreferredSize(new java.awt.Dimension(75, 25));
        
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 5, 0);
        pnlProposalRateMainPanel.add(btnReset, gridBagConstraints);
        
        btnSync.setFont(edu.mit.coeus.gui.CoeusFontFactory.getLabelFont());
        btnSync.setMnemonic('S');
        btnSync.setText("Sync");
        
        btnSync.setMaximumSize(new java.awt.Dimension(75, 25));
        btnSync.setMinimumSize(new java.awt.Dimension(75, 25));
        btnSync.setPreferredSize(new java.awt.Dimension(75, 25));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTH;
        gridBagConstraints.weightx = 1.0;
        pnlProposalRateMainPanel.add(btnSync, gridBagConstraints);
        
        tbdRatesTabPane.setBackground((java.awt.Color) javax.swing.UIManager.getDefaults().get("Panel.background"));
        tbdRatesTabPane.setMinimumSize(new java.awt.Dimension(550, 330));
        tbdRatesTabPane.setMaximumSize(new java.awt.Dimension(550, 330));
        tbdRatesTabPane.setPreferredSize(new java.awt.Dimension(550, 330));
        tbdRatesTabPane.setFont(CoeusFontFactory.getLabelFont());
        pnlInstituteTabPane.setLayout(new java.awt.GridBagLayout());
        pnlInstituteTabPane.setFont(edu.mit.coeus.gui.CoeusFontFactory.getLabelFont());
        
        pnlInstituteTabPane.setBorder(new javax.swing.border.BevelBorder(javax.swing.border.BevelBorder.LOWERED));
        
        pnlInstituteTabPane.setMaximumSize(new java.awt.Dimension(654, 364));
        pnlInstTabHeader.setLayout(new java.awt.GridBagLayout());
        
        pnlInstTabHeader.setMaximumSize(new java.awt.Dimension(650, 70));
        pnlInstTabHeader.setMinimumSize(new java.awt.Dimension(650, 70));
        pnlInstTabHeader.setPreferredSize(new java.awt.Dimension(650, 70));
        lbllnstituteTabHead.setFont(edu.mit.coeus.gui.CoeusFontFactory.getLabelFont());
        //lbllnstituteTabHead.setIcon(new javax.swing.ImageIcon("")); JM 5-21-2015 this throws nullpointerexception in 1.8
        
        lbllnstituteTabHead.setMaximumSize(new java.awt.Dimension(55, 16));
        lbllnstituteTabHead.setMinimumSize(new java.awt.Dimension(55, 16));
        lbllnstituteTabHead.setPreferredSize(new java.awt.Dimension(55, 16));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 0.5;
        gridBagConstraints.weighty = 1.0;
        pnlInstTabHeader.add(lbllnstituteTabHead, gridBagConstraints);
        
        txtArInstFiscalYearHead.setBackground((java.awt.Color) javax.swing.UIManager.getDefaults().get("Panel.background"));
        txtArInstFiscalYearHead.setEditable(false);
        txtArInstFiscalYearHead.setFont(edu.mit.coeus.gui.CoeusFontFactory.getLabelFont());
        txtArInstFiscalYearHead.setText("Fiscal\nYear");
        
        txtArInstFiscalYearHead.setMaximumSize(new java.awt.Dimension(50, 35));
        txtArInstFiscalYearHead.setMinimumSize(new java.awt.Dimension(50, 35));
        txtArInstFiscalYearHead.setPreferredSize(new java.awt.Dimension(50, 35));
        txtArInstFiscalYearHead.setRequestFocusEnabled(false);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 0.5;
        gridBagConstraints.weighty = 1.0;
        pnlInstTabHeader.add(txtArInstFiscalYearHead, gridBagConstraints);
        
        txtArInstCampusHead.setBackground((java.awt.Color) javax.swing.UIManager.getDefaults().get("Panel.background"));
        txtArInstCampusHead.setEditable(false);
        txtArInstCampusHead.setFont(edu.mit.coeus.gui.CoeusFontFactory.getLabelFont());
        txtArInstCampusHead.setLineWrap(true);
        txtArInstCampusHead.setText("On/Off\nCampus");
        
        txtArInstCampusHead.setMaximumSize(new java.awt.Dimension(50, 35));
        txtArInstCampusHead.setMinimumSize(new java.awt.Dimension(50, 35));
        txtArInstCampusHead.setPreferredSize(new java.awt.Dimension(50, 35));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 0.5;
        gridBagConstraints.weighty = 1.0;
        pnlInstTabHeader.add(txtArInstCampusHead, gridBagConstraints);
        
        txtArInstActivityTypeHead.setBackground((java.awt.Color) javax.swing.UIManager.getDefaults().get("Panel.background"));
        txtArInstActivityTypeHead.setEditable(false);
        txtArInstActivityTypeHead.setFont(edu.mit.coeus.gui.CoeusFontFactory.getLabelFont());
        txtArInstActivityTypeHead.setText("Activity Type");
        
        txtArInstActivityTypeHead.setMaximumSize(new java.awt.Dimension(120, 35));
        txtArInstActivityTypeHead.setMinimumSize(new java.awt.Dimension(120, 35));
        txtArInstActivityTypeHead.setPreferredSize(new java.awt.Dimension(120, 35));
        txtArInstActivityTypeHead.setRequestFocusEnabled(false);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 0.5;
        gridBagConstraints.weighty = 1.0;
        pnlInstTabHeader.add(txtArInstActivityTypeHead, gridBagConstraints);
        
        txtArInstStartDateHead.setBackground((java.awt.Color) javax.swing.UIManager.getDefaults().get("Panel.background"));
        txtArInstStartDateHead.setEditable(false);
        txtArInstStartDateHead.setFont(edu.mit.coeus.gui.CoeusFontFactory.getLabelFont());
        txtArInstStartDateHead.setLineWrap(true);
        txtArInstStartDateHead.setText("Start\nDate");
        
        txtArInstStartDateHead.setMinimumSize(new java.awt.Dimension(50, 35));
        txtArInstStartDateHead.setPreferredSize(new java.awt.Dimension(50, 35));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 0.5;
        gridBagConstraints.weighty = 1.0;
        pnlInstTabHeader.add(txtArInstStartDateHead, gridBagConstraints);
        
        txtArInstituteRateHead.setBackground((java.awt.Color) javax.swing.UIManager.getDefaults().get("Panel.background"));
        txtArInstituteRateHead.setEditable(false);
        txtArInstituteRateHead.setFont(edu.mit.coeus.gui.CoeusFontFactory.getLabelFont());
        txtArInstituteRateHead.setLineWrap(true);
        txtArInstituteRateHead.setText("Institute\nRate");
        
        txtArInstituteRateHead.setMinimumSize(new java.awt.Dimension(50, 35));
        txtArInstituteRateHead.setPreferredSize(new java.awt.Dimension(50, 35));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 5;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 0.5;
        gridBagConstraints.weighty = 1.0;
        pnlInstTabHeader.add(txtArInstituteRateHead, gridBagConstraints);
        
        txtArInstApplicRateHead.setBackground((java.awt.Color) javax.swing.UIManager.getDefaults().get("Panel.background"));
        txtArInstApplicRateHead.setEditable(false);
        txtArInstApplicRateHead.setFont(edu.mit.coeus.gui.CoeusFontFactory.getLabelFont());
        txtArInstApplicRateHead.setLineWrap(true);
        txtArInstApplicRateHead.setText("Applicable\nRate");
        
        txtArInstApplicRateHead.setMinimumSize(new java.awt.Dimension(50, 35));
        txtArInstApplicRateHead.setPreferredSize(new java.awt.Dimension(50, 35));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 6;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.RELATIVE;
        gridBagConstraints.gridheight = java.awt.GridBagConstraints.RELATIVE;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        pnlInstTabHeader.add(txtArInstApplicRateHead, gridBagConstraints);
        
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        pnlInstituteTabPane.add(pnlInstTabHeader, gridBagConstraints);
        
        pnlInstRateTabularPanel.setLayout(new javax.swing.BoxLayout(pnlInstRateTabularPanel, javax.swing.BoxLayout.Y_AXIS));
        
        pnlInstTabDataPanel.setLayout(new java.awt.GridBagLayout());
        pnlInstTabDataPanel.setMaximumSize(new java.awt.Dimension(650, 300));
        pnlInstTabDataPanel.setMinimumSize(new java.awt.Dimension(650, 300));
        pnlInstTabDataPanel.setPreferredSize(new java.awt.Dimension(650, 300));
        scrPnInstTabDataPanel.setHorizontalScrollBarPolicy(javax.swing.JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrPnInstTabDataPanel.setMaximumSize(new java.awt.Dimension(650, 245)); //300
        scrPnInstTabDataPanel.setMinimumSize(new java.awt.Dimension(650, 245)); //300
        scrPnInstTabDataPanel.setPreferredSize(new java.awt.Dimension(650, 245)); //300
        scrPnInstTabDataPanel.setViewportView(pnlInstRateTabularPanel);
        scrPnInstTabDataPanel.setBorder(new javax.swing.border.BevelBorder(javax.swing.border.BevelBorder.LOWERED));
        scrPnInstTabDataPanel.getVerticalScrollBar().setUnitIncrement(20);
        scrPnInstTabDataPanel.getVerticalScrollBar().setBlockIncrement(10);
        
        //pnlInstRateTabularPanel = getRateTabularPanelInstance();
        //pnlInstRateTabularPanel = new JPanel();
        //pnlEachInstRatePanelGroup.setLayout(new javax.swing.BoxLayout(pnlEachInstRatePanelGroup, javax.swing.BoxLayout.Y_AXIS));
        
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        pnlInstTabDataPanel.add(scrPnInstTabDataPanel, gridBagConstraints);
        
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        pnlInstituteTabPane.add(pnlInstTabDataPanel, gridBagConstraints);
        
        // JM 5-21-2015 this throws nullpointerexception in 1.8
        //tbdRatesTabPane.addTab("Institute  ", new javax.swing.ImageIcon(""), pnlInstituteTabPane, ""); 
        tbdRatesTabPane.addTab("Institute  ", null, pnlInstituteTabPane, "");
        
        pnlLATabPane.setLayout(new java.awt.GridBagLayout());
        pnlLATabPane.setFont(edu.mit.coeus.gui.CoeusFontFactory.getLabelFont());
        
        pnlLATabPane.setBorder(new javax.swing.border.BevelBorder(javax.swing.border.BevelBorder.LOWERED));
        pnlLATabPane.setMaximumSize(new java.awt.Dimension(654, 364));
        pnlLATabPane.setMinimumSize(new java.awt.Dimension(654, 364));
        pnlLATabPane.setPreferredSize(new java.awt.Dimension(654, 364));
        pnlLATabHeader.setLayout(new java.awt.GridBagLayout());
        
        pnlLATabHeader.setMaximumSize(new java.awt.Dimension(655, 35));
        pnlLATabHeader.setMinimumSize(new java.awt.Dimension(655, 35));
        pnlLATabHeader.setPreferredSize(new java.awt.Dimension(655, 35));
        lblLARateClassHead.setFont(edu.mit.coeus.gui.CoeusFontFactory.getNormalFont());
        
        lblLARateClassHead.setMaximumSize(new java.awt.Dimension(55, 16));
        lblLARateClassHead.setMinimumSize(new java.awt.Dimension(55, 16));
        lblLARateClassHead.setPreferredSize(new java.awt.Dimension(55, 16));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 0.5;
        gridBagConstraints.weighty = 1.0;
        pnlLATabHeader.add(lblLARateClassHead, gridBagConstraints);
        
        txtArLAFiscalYearHead.setBackground((java.awt.Color) javax.swing.UIManager.getDefaults().get("Panel.background"));
        txtArLAFiscalYearHead.setEditable(false);
        txtArLAFiscalYearHead.setFont(edu.mit.coeus.gui.CoeusFontFactory.getLabelFont());
        txtArLAFiscalYearHead.setText("Fiscal\nYear");
        
        txtArLAFiscalYearHead.setMinimumSize(new java.awt.Dimension(50, 35));
        txtArLAFiscalYearHead.setPreferredSize(new java.awt.Dimension(50, 35));
        txtArLAFiscalYearHead.setRequestFocusEnabled(false);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 0.5;
        pnlLATabHeader.add(txtArLAFiscalYearHead, gridBagConstraints);
        
        txtArLACampusHead.setBackground((java.awt.Color) javax.swing.UIManager.getDefaults().get("Panel.background"));
        txtArLACampusHead.setEditable(false);
        txtArLACampusHead.setFont(edu.mit.coeus.gui.CoeusFontFactory.getLabelFont());
        txtArLACampusHead.setLineWrap(true);
        txtArLACampusHead.setText("On/Off\nCampus");
        
        txtArLACampusHead.setMinimumSize(new java.awt.Dimension(50, 35));
        txtArLACampusHead.setPreferredSize(new java.awt.Dimension(50, 35));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 0.5;
        pnlLATabHeader.add(txtArLACampusHead, gridBagConstraints);
        
        txtArLAStartDateHead.setBackground((java.awt.Color) javax.swing.UIManager.getDefaults().get("Panel.background"));
        txtArLAStartDateHead.setEditable(false);
        txtArLAStartDateHead.setFont(edu.mit.coeus.gui.CoeusFontFactory.getLabelFont());
        txtArLAStartDateHead.setLineWrap(true);
        txtArLAStartDateHead.setText("Start\nDate");
        
        txtArLAStartDateHead.setMinimumSize(new java.awt.Dimension(50, 35));
        txtArLAStartDateHead.setPreferredSize(new java.awt.Dimension(50, 35));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 0.5;
        pnlLATabHeader.add(txtArLAStartDateHead, gridBagConstraints);
        
        txtArLAEndDateHead.setBackground((java.awt.Color) javax.swing.UIManager.getDefaults().get("Panel.background"));
        txtArLAEndDateHead.setEditable(false);
        txtArLAEndDateHead.setFont(edu.mit.coeus.gui.CoeusFontFactory.getLabelFont());
        txtArLAEndDateHead.setLineWrap(true);
        txtArLAEndDateHead.setText("Institute\nRate");
        
        txtArLAEndDateHead.setMinimumSize(new java.awt.Dimension(50, 35));
        txtArLAEndDateHead.setPreferredSize(new java.awt.Dimension(50, 35));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 0.5;
        pnlLATabHeader.add(txtArLAEndDateHead, gridBagConstraints);
        
        txtArLAApplicDateHead.setBackground((java.awt.Color) javax.swing.UIManager.getDefaults().get("Panel.background"));
        txtArLAApplicDateHead.setEditable(false);
        txtArLAApplicDateHead.setFont(edu.mit.coeus.gui.CoeusFontFactory.getLabelFont());
        txtArLAApplicDateHead.setLineWrap(true);
        txtArLAApplicDateHead.setText("Applicable\nRate");
        
        txtArLAApplicDateHead.setMinimumSize(new java.awt.Dimension(50, 35));
        txtArLAApplicDateHead.setPreferredSize(new java.awt.Dimension(50, 35));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 5;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.RELATIVE;
        gridBagConstraints.gridheight = java.awt.GridBagConstraints.RELATIVE;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 1.0;
        pnlLATabHeader.add(txtArLAApplicDateHead, gridBagConstraints);
        
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 1.0;
        pnlLATabPane.add(pnlLATabHeader, gridBagConstraints);
        
        pnlLARateDataPanel.setLayout(new java.awt.GridBagLayout());
        
        pnlLARateDataPanel.setMaximumSize(new java.awt.Dimension(650, 300));
        pnlLARateDataPanel.setMinimumSize(new java.awt.Dimension(650, 300));
        pnlLARateDataPanel.setPreferredSize(new java.awt.Dimension(650, 300));
        
        scrPnLARatesData.setHorizontalScrollBarPolicy(javax.swing.JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrPnLARatesData.setMaximumSize(new java.awt.Dimension(650, 245)); //300
        scrPnLARatesData.setMinimumSize(new java.awt.Dimension(650, 245)); //300
        scrPnLARatesData.setPreferredSize(new java.awt.Dimension(650, 245)); //300
        scrPnLARatesData.setBorder(new javax.swing.border.BevelBorder(javax.swing.border.BevelBorder.LOWERED));
        scrPnLARatesData.getVerticalScrollBar().setUnitIncrement(20);
        scrPnLARatesData.getVerticalScrollBar().setBlockIncrement(10);
        
        pnlLATabularPanel.setLayout(new javax.swing.BoxLayout(pnlLATabularPanel, javax.swing.BoxLayout.Y_AXIS));
        //pnlLATabularPanel = getRateTabularPanelInstance();
        //pnlEachLARatePanelGroup.setLayout(new javax.swing.BoxLayout(pnlEachLARatePanelGroup, javax.swing.BoxLayout.Y_AXIS));
        scrPnLARatesData.setViewportView(pnlLATabularPanel);
        
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        pnlLARateDataPanel.add(scrPnLARatesData, gridBagConstraints);
        
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        pnlLATabPane.add(pnlLARateDataPanel, gridBagConstraints);
        /**  The tab page has to be added only if the 
         *LA rates are present for that Department.
         *Added by chandra - 02/03/2004 - start
         *bug Fix id GNBGT-DEF-004 
         */
        //tbdRatesTabPane.addTab("       LA       ", new javax.swing.ImageIcon(""), pnlLATabPane, "");
        
        // Added by chandra - 02/03/2004- End
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridheight = 10;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(3, 0, 0, 0);
        pnlProposalRateMainPanel.add(tbdRatesTabPane, gridBagConstraints);
        
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 1.0;
        this.add(pnlProposalRateMainPanel, gridBagConstraints);
        
    }
    
    public JPanel  getRateTabularPanelInstance() {
        JPanel pnlRateTabularPanel = new javax.swing.JPanel();
        pnlRateTabularPanel.setLayout(new javax.swing.BoxLayout(pnlInstRateTabularPanel, javax.swing.BoxLayout.Y_AXIS));
        return pnlRateTabularPanel;
    }
    
    /** Closes the dialog */
    /*
    private void closeDialog(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_closeDialog
        setVisible(false);
       // dispose();
    }//GEN-LAST:event_closeDialog
     */
    
    
    
    
    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        new ProposalRateForm(new javax.swing.JFrame(), true);
    }
    
    /** each Institute TabPaneGroup which consist of the rate calss and Rate Type header and
     * the values for each Rate Class and Rate Type
     */    
    public javax.swing.JPanel pnlEachInstRatePanelGroup;
    /** each LATabPaneGroup which consist of the rate calss and Rate Type header and
     * the values for each Rate Class and Rate Type
     */    
    public javax.swing.JPanel pnlEachLARatePanelGroup;
    /** CurrencyField Institute Applicable Rates */    
    public CurrencyField txtInstApplicRatesValue = new CurrencyField();
    /** CurrencyField InstituteLA Applicable Rates */    
    public CurrencyField txtLAApplicRatesValue = new CurrencyField();
    
    // Variables declaration
    public javax.swing.JLabel lblLabRatesTypeValue;
    public javax.swing.JTextArea txtArInstituteRateHead;
    public javax.swing.JPanel pnlLabRatesClassData;
    public javax.swing.JPanel pnlProposalRateMainPanel;
    public javax.swing.JPanel pnlInstRatesClassData;
    public javax.swing.JScrollPane scrPnInstTabDataPanel;
    public javax.swing.JLabel lblInstActivityTypevalue;
    public javax.swing.JLabel lblInstStartDateValue;
    public javax.swing.JLabel lblLabRatesClassValue;
    
    public javax.swing.JTextArea txtArLAApplicDateHead;
    public javax.swing.JLabel lblInstRatesClassValue;
    public javax.swing.JTextArea txtArLAFiscalYearHead;
    public javax.swing.JPanel pnlInstRatesEachTabularValue;
    public javax.swing.JPanel pnlLATabPane;
    public javax.swing.JLabel lblLACampusValue;
    public javax.swing.JLabel lblInstInstituteRateValue;
    public javax.swing.JTextArea txtArInstStartDateHead;
    public javax.swing.JTextArea txtArLACampusHead;
    public javax.swing.JLabel lblLAStartDateValue;
    public javax.swing.JPanel pnlLARateDataPanel;
    public javax.swing.JLabel lblLAInstituteRateValue;
    public javax.swing.JTabbedPane tbdRatesTabPane;
    public javax.swing.JTextArea txtArLAStartDateHead;
    public javax.swing.JLabel lblInstBlankSpace;
    public javax.swing.JLabel lblInstCampusValue;
    public javax.swing.JPanel pnlLabRatesEachTabularValue;
    public javax.swing.JPanel pnlInstTabHeader;
    public javax.swing.JTextArea txtArInstFiscalYearHead;
    public javax.swing.JLabel lbllnstituteTabHead;
    public javax.swing.JLabel lblLARateClassHead;
    public javax.swing.JButton btnReset;
    public javax.swing.JPanel pnlInstituteTabPane;
    public javax.swing.JTextArea txtArLAEndDateHead;
    public javax.swing.JLabel lblInstRatesYearValue;
    public javax.swing.JTextArea txtArInstApplicRateHead;
    public javax.swing.JTextArea txtArInstActivityTypeHead;
    public javax.swing.JPanel pnlLATabHeader;
    
    public javax.swing.JScrollPane scrPnLARatesData;
    public javax.swing.JLabel lblInstRatesTypeValue;
    public javax.swing.JPanel pnlLATabularPanel;
    public javax.swing.JLabel lblLARatesYearValue;
    public javax.swing.JTextArea txtArInstCampusHead;
    public javax.swing.JPanel pnlInstTabDataPanel;
    public javax.swing.JPanel pnlInstRateTabularPanel;
    public javax.swing.JButton btnOK;
    public javax.swing.JButton btnCancel;
    public javax.swing.JButton btnSync;
    public javax.swing.JLabel lblLABlankSpace;
    // End of variables declaration
    
    
    
}
