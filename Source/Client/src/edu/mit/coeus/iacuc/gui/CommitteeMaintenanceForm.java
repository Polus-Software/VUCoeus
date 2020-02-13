/*
 * @(#)CommitteeMaintenanceForm.java
 *
 * Copyright (c) Massachusetts Institute of Technology
 * Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 *
 */
 /* PMD check performed, and commented unused imports and variables on 09-NOV-2010
 * by Md.Ehtesham Ansari
 */
package edu.mit.coeus.iacuc.gui;

import javax.swing.*;
import java.awt.event.MouseAdapter;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseEvent;
import java.awt.event.ItemEvent;

import java.util.Vector;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import edu.mit.coeus.gui.CoeusFontFactory;
import edu.mit.coeus.gui.CoeusAppletMDIForm;
import edu.mit.coeus.irb.bean.CommitteeMaintenanceFormBean;
import edu.mit.coeus.utils.*;
import edu.mit.coeus.search.gui.*;
import edu.mit.coeus.unit.gui.UnitDetailForm;
import edu.mit.coeus.unit.bean.UnitDetailFormBean;
import edu.mit.coeus.brokers.RequesterBean;
import edu.mit.coeus.brokers.ResponderBean;
import edu.mit.coeus.gui.CoeusMessageResources;

/** This class will display the Committee details for selected committee in
 * <CODE>CommitteeBaseWindow</CODE>. This class is also used to create new Committee.
 *
 * @author Guptha k
 * @version 1.0 September 27, 2002, 8:33 PM
 * @updated Phani
 * @version 1.5 October 3. 2002
 * update Ravikanth
 * @version 1.7 October 7, 2002, 12.57PM
 */
public class CommitteeMaintenanceForm extends javax.swing.JPanel
                                                implements ActionListener,
                                                ItemListener {

    // Variables declaration - do not modify
    private javax.swing.JComboBox cmbCommitteeType;
    private CoeusTextField txtMinMembers;
    private CoeusTextField txtLastUpdated;
    private CoeusTextField txtUnitNo;
    private javax.swing.JLabel lblLastUpdated;
    private javax.swing.JLabel lblMaxProtocols;
    private CoeusTextField txtDescription;
    private javax.swing.JTextArea txtArScheduleDesc;
    private javax.swing.JScrollPane scrPnScheduleDesc;
    private javax.swing.JTextArea txtArUnitName;
	private javax.swing.JScrollPane scrPnUnitName;
    private javax.swing.JScrollPane scrPnCommitteeDesc;
    private javax.swing.JTextArea txtArCommitteeDesc;
    private CoeusTextField txtCommitteeId;
    private CoeusTextField txtMaxProtocols;
    private javax.swing.JLabel lblDescription;
    private javax.swing.JButton btnSearch;
    private javax.swing.JLabel lblCommitteeId;
    private javax.swing.JLabel lblScheduleDesc;
    private javax.swing.JLabel lblHomeUnit;
    private CoeusTextField txtCommitteeName;
    private CoeusTextField txtUpdatedUser;
    private javax.swing.JLabel lblCommitteeName;
    private javax.swing.JLabel lblUpdatedUser;
    private javax.swing.JLabel lblType;
    private javax.swing.JComboBox cmbReviewType;
    private javax.swing.JLabel lblReviewType;
    private CoeusTextField txtAdvSubmissionDays;
    private javax.swing.JLabel lblUnitName;
    private javax.swing.JLabel lblMinimumMembers;
    private javax.swing.JLabel lblAdvSubmissionDays;
    // End of variables declaration

    private String commId;
    private char functionType;
    private CommitteeMaintenanceFormBean committeeMaintBean;
    private Vector committeeTypes,reviewTypes;/*,customParameters */   // Commented to use CoeusLabel instead Module Parameter XML file Start :02-Sep-2005
    private DateUtils dateUtils = new DateUtils();
    private boolean saveRequired = false;

    private String previousUnitNo="";
    private String previousUnitName="";
    private String connectionURL = CoeusGuiConstants.CONNECTION_URL;

    private CoeusAppletMDIForm mdiForm = null;

    private String unitName="";
	//Added by Nadh for Save Confirmation Bug Fix 0n 6th Dec - 2004
	private String prevText="";
        
    //holds CoeusMessageResources instance used for reading message Properties.
    private CoeusMessageResources coeusMessageResources;

    /** Creates new <CODE>CommitteeMaintenanceForm</CODE>
     */
    public CommitteeMaintenanceForm() {
        initComponents();
        
    }

    /**
     * This is used to set the focus on editable field based on functionType
     */
    public void setFocus(){
        if(functionType=='A'){
            txtCommitteeId.requestFocusInWindow();
           
        }else if(functionType=='M'){
            
            //included by raghuSV to eliminate focus traversal problem in modify mode...
            //start
            txtCommitteeName.requestFocusInWindow();
            java.awt.Component[] components = {txtCommitteeName,txtArCommitteeDesc,
            txtUnitNo,btnSearch,cmbCommitteeType,txtMinMembers,txtMaxProtocols,
            txtAdvSubmissionDays,cmbReviewType,txtArScheduleDesc,txtCommitteeName};
            ScreenFocusTraversalPolicy  traversePolicy = new ScreenFocusTraversalPolicy( components );
            setFocusTraversalPolicy(traversePolicy);
            setFocusCycleRoot(true);
            //end
            
        }else if(functionType=='D'){
            lblCommitteeId.requestFocusInWindow();
        }
    }
    /** Creates new form <CODE>CommitteeMaintenanceForm</CODE> and fetches the Committee
     * details from the database for the given committee id.
     *
     * @param functionType character which specifies the form opened mode.
     * @param commId String representing the Committee ID whose details has to
     * be fetched.
     */
    public CommitteeMaintenanceForm(char functionType,String commId) {
        
        this.commId = commId;
        this.functionType=functionType;
        initComponents();
        getCommitteeDetails(commId);
        setValues(committeeMaintBean);
        formatFields();
    }

    /** Creates new form <CODE>CommitteeMaintenanceForm</CODE>  with the given
     * <CODE>CommitteMaintenanceFormBean</CODE> data and initializes all the components.
     *
     * @param functionType character which specifies the form opened mode.
     * @param committeeMaintBean <CODE>CommitteeMaintenanceFormBean</CODE> with all the
     * details of a Committee.
     */
    public CommitteeMaintenanceForm(char functionType,
            CommitteeMaintenanceFormBean committeeMaintBean,Vector parameters) {
        this.committeeMaintBean = committeeMaintBean;
        this.functionType=functionType;
        //this.customParameters = parameters; // Commented to use CoeusLabel instead Module Parameter XML file Start :02-Sep-2005
    }

    /** This method is used to set the available types for a Committee, which are
     * retrieved from the database while initializing this component. These details
     * will be set from <CODE>CommitteeDetailsForm</CODE>.
     *
     * @param committeeTypes Collection of <CODE>ComboBoxBean</CODE> with committee type code
     * and descriptions.
     */
    public void setCommitteeTypes(Vector committeeTypes){
        this.committeeTypes = committeeTypes;
    }

    /** This method is used to set the available review types for a Committee, which are
     * retrieved from database while initializing this component. These details will be
     * set from <CODE>CommitteeDetailsForm</CODE>.
     *
     * @param committeeReviewTypes Collection of <CODE>ComboBoxBean</CODE> with committee type code and descriptions.
     */
    public void setCommitteeReviewTypes(Vector committeeReviewTypes){
        this.reviewTypes = committeeReviewTypes;
    }

    /** This method is used to get the initialized <CODE>CommitteeMaintenanceForm</CODE>
     * component to place in the tabbedpane in <CODE>CommitteeDetailsForm</CODE>.
     *
     * @param mdiForm reference to <CODE>CoeusAppletMDIForm</CODE>.
     * @return JPanel reference to this component after initialization of all
     * components along with populated data.
     */
    public JPanel getCommitteMaintenanceForm(CoeusAppletMDIForm mdiForm){
        this.mdiForm = mdiForm;
        initComponents();
        setValues(committeeMaintBean);
        formatFields();
        return this;
    }

    /** This method is used to set the functionType to this component while
     * initializing. This will called from <CODE>CommitteeDetailsForm</CODE>
     *
     * @param functionType character which specifies the form opened mode.
     */
    public void setFunctionType(char functionType){
        this.functionType = functionType;
    }

    /** This method is used to get the functionType from this component.
     * This will be called from <CODE>CommitteeDetailsForm</CODE>.
     *
     * @return functionType character which specifies the form opened mode.
     */
     public char getFunctionType(){
        return functionType;
    }

    /** This method is called from within the constructor to
     * initialize the form.
     */
    private void initComponents() {
        coeusMessageResources = CoeusMessageResources.getInstance();
        java.awt.GridBagConstraints gridBagConstraints;
        
        lblCommitteeId = new javax.swing.JLabel();
        txtCommitteeId = new CoeusTextField(){
            public boolean isFocusTraversable(){
                if(functionType != 'A'){
                    return false;
                }
                return true;
            }
        };
        txtCommitteeId.setDocument(new LimitedPlainDocument(15));
        txtCommitteeId.addKeyListener(new updateChanges());
		txtCommitteeId.addMouseListener(new GetValues());//Added by Nadh for Save Confirmation Bug Fix 0n 6th Dec - 2004

        lblCommitteeName = new javax.swing.JLabel();
        txtCommitteeName = new CoeusTextField();
        txtCommitteeName.setDocument(new LimitedPlainDocument(60));
        txtCommitteeName.addKeyListener(new updateChanges());
		txtCommitteeName.addMouseListener(new GetValues());//Added by Nadh for Save Confirmation Bug Fix 0n 6th Dec - 2004
        lblDescription = new javax.swing.JLabel();
        lblType = new javax.swing.JLabel();
        lblHomeUnit = new javax.swing.JLabel();
        cmbCommitteeType = new JComboBox();
        cmbCommitteeType.addItemListener( this );
        txtUnitNo = new CoeusTextField();
        //txtUnitNo.setDocument(new JTextFieldFilter(JTextFieldFilter.NUMERIC,8));        
        /**
         * Updated For : REF ID 145 Feb' 14 2003
         * Org Unit entries appear to be limited to numerical entries. Can 
         * this be modified to include alphanumeric as well.
         *
         * Updated by Subramanya Feb' 19 2003
         */
        txtUnitNo.setDocument(new JTextFieldFilter(JTextFieldFilter.ALPHA_NUMERIC,8));
        txtUnitNo.addKeyListener(new updateChanges());
		
        //Updated by Subramanya
        txtUnitNo.addFocusListener( new FocusAdapter(){
            public void focusLost( FocusEvent focusEvent ){
                // modified by ravi, removing checking with committeeMaintBean
                // and its unit number because the bean will be null in add mode
                if((txtUnitNo.getText() != null )
                        && (txtUnitNo.getText().trim().length()>0)){
                    if(!focusEvent.isTemporary()){
                    String formUnitNum = txtUnitNo.getText().trim();
                       String newUnitName = getUnitName( formUnitNum );
                       if ( newUnitName == null || newUnitName.
                                                        equalsIgnoreCase("")){
                            CoeusOptionPane.showWarningDialog(
                                        coeusMessageResources.parseMessageKey(
                                            "commMntFrm_exceptionCode.1009"));
                            txtUnitNo.setText(previousUnitNo);
                            btnSearch.requestFocus();
                       }else{
                            previousUnitNo = txtUnitNo.getText();
                            lblUnitName.setText( newUnitName );
                       }
                    }
                }else{
                    lblUnitName.setText( "" );
                }
            }
        });
        
        btnSearch = new javax.swing.JButton();
        btnSearch.addActionListener(this);
        lblUnitName = new javax.swing.JLabel();
        lblMinimumMembers = new javax.swing.JLabel();
        txtMinMembers = new CoeusTextField();
        txtMinMembers.setDocument(new JTextFieldFilter(
                JTextFieldFilter.NUMERIC,3));
        txtMinMembers.addKeyListener(new updateChanges());
		txtMinMembers.addMouseListener(new GetValues());//Added by Nadh for Save Confirmation Bug Fix 0n 6th Dec - 2004
        lblMaxProtocols = new javax.swing.JLabel();
        txtAdvSubmissionDays = new CoeusTextField();
        txtAdvSubmissionDays.setDocument(new JTextFieldFilter(
                JTextFieldFilter.NUMERIC,3));
        txtAdvSubmissionDays.addKeyListener(new updateChanges());
		txtAdvSubmissionDays.addMouseListener(new GetValues());//Added by Nadh for Save Confirmation Bug Fix 0n 6th Dec - 2004
        txtMaxProtocols = new CoeusTextField();
        // added by manoj to ifx the bug #30 file:IRB-SystemTestingDL-01.xls 26/08/2003
        txtMaxProtocols.setDocument(new JTextFieldFilter(
                JTextFieldFilter.NUMERIC,4));
        txtMaxProtocols.addKeyListener(new updateChanges());
		txtMaxProtocols.addMouseListener(new GetValues());//Added by Nadh for Save Confirmation Bug Fix 0n 6th Dec - 2004
        lblAdvSubmissionDays = new javax.swing.JLabel();
        lblReviewType = new javax.swing.JLabel();
        cmbReviewType = new JComboBox();
        cmbReviewType.addItemListener( this );
        lblLastUpdated = new javax.swing.JLabel();
        txtLastUpdated = new CoeusTextField(){
            public boolean isFocusTraversable(){
                return false;
            }
        };
        lblUpdatedUser = new javax.swing.JLabel();
        txtUpdatedUser = new CoeusTextField(){
            public boolean isFocusTraversable(){
                return false;
            }
        };
        lblScheduleDesc = new javax.swing.JLabel();
        scrPnScheduleDesc = new javax.swing.JScrollPane();
        scrPnCommitteeDesc = new javax.swing.JScrollPane();
        scrPnUnitName = new javax.swing.JScrollPane();
        txtArCommitteeDesc = new javax.swing.JTextArea();
        txtArCommitteeDesc.setFont(CoeusFontFactory.getNormalFont());
        txtArCommitteeDesc.addKeyListener(new updateChanges());
		txtArCommitteeDesc.addMouseListener(new GetValues());//Added by Nadh for Save Confirmation Bug Fix 0n 6th Dec - 2004
        txtArCommitteeDesc.setDocument(new LimitedPlainDocument(2000));
        txtArScheduleDesc = new javax.swing.JTextArea();
        txtArScheduleDesc.setFont(CoeusFontFactory.getNormalFont());
        txtArScheduleDesc.addKeyListener(new updateChanges());
		txtArScheduleDesc.addMouseListener(new GetValues());//Added by Nadh for Save Confirmation Bug Fix 0n 6th Dec - 2004
        txtArScheduleDesc.setDocument(new LimitedPlainDocument(2000));
        txtArUnitName = new javax.swing.JTextArea();
        txtArUnitName.setFont(CoeusFontFactory.getNormalFont());

        setLayout(new java.awt.GridBagLayout());

        /* Action listener is added to the txtUnitNo to get the unitName from
         * database for the user entered UnitNumber
         */
        txtUnitNo.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                try {
                if (txtUnitNo.getText().trim().length() <= 0) {
                    log(coeusMessageResources.parseMessageKey(
                                            "commMntFrm_exceptionCode.1008"));
                } else{
                     unitName=getUnitName(txtUnitNo.getText().trim());
                     if ( unitName==null || unitName.equalsIgnoreCase("")){
                        log(coeusMessageResources.parseMessageKey(
                                            "commMntFrm_exceptionCode.1009"));
                    }else{
                        txtUnitNo.setText(txtUnitNo.getText().trim());
                        previousUnitNo = txtUnitNo.getText();
                        lblUnitName.setText(unitName);
                    }
                }
                }catch (Exception e){
                    CoeusOptionPane.showErrorDialog(e.getMessage());
                    txtUnitNo.setText(previousUnitNo);
                    txtUnitNo.requestFocus();
                }
            }
        });

        txtUnitNo.addMouseListener(new MouseAdapter(){
            public void mouseClicked(MouseEvent mouseEvent){
                if(mouseEvent.getClickCount() == 2){
                try{
                    String unitId=txtUnitNo.getText();
                    if( unitId != null && unitId.trim().length() > 0 ) {
                            UnitDetailForm frmUnit = new UnitDetailForm(unitId,'G');
                                    frmUnit.showUnitForm(mdiForm);
                    }
                }catch(Exception exc){
                exc.printStackTrace();
                }
                }
			}
        });        

        lblCommitteeId.setFont(CoeusFontFactory.getLabelFont());
        lblCommitteeId.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblCommitteeId.setText("Committee ID:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(8, 0, 0, 0);
        add(lblCommitteeId, gridBagConstraints);

        txtCommitteeId.setFont(CoeusFontFactory.getNormalFont());
        txtCommitteeId.setMinimumSize(new java.awt.Dimension(124, 20));
        txtCommitteeId.setPreferredSize(new java.awt.Dimension(124, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(8, 4, 0, 0);
        add(txtCommitteeId, gridBagConstraints);

        lblCommitteeName.setFont(CoeusFontFactory.getLabelFont());
        lblCommitteeName.setHorizontalAlignment(
                javax.swing.SwingConstants.RIGHT);
        lblCommitteeName.setText("Committee Name:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(8, 0, 0, 0);
        add(lblCommitteeName, gridBagConstraints);

        txtCommitteeName.setFont(CoeusFontFactory.getNormalFont());
        txtCommitteeName.setMinimumSize(new java.awt.Dimension(124, 20));
        txtCommitteeName.setPreferredSize(new java.awt.Dimension(132, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(8, 4, 0, 8);
        add(txtCommitteeName, gridBagConstraints);

        lblDescription.setFont(CoeusFontFactory.getLabelFont());
        lblDescription.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblDescription.setText("Description:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHEAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 8, 0, 0);
        add(lblDescription, gridBagConstraints);

        //Bug fix code
        //Modified by Vyjayanthi on 26-08-03 to remove the horizontal scrollbar
        //scrPnCommitteeDesc.setHorizontalScrollBarPolicy(
                //javax.swing.JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
        scrPnCommitteeDesc.setVerticalScrollBarPolicy(
                javax.swing.JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        scrPnCommitteeDesc.setPreferredSize(new java.awt.Dimension(18, 80));
        // Added by Chandra
        txtArCommitteeDesc.setLineWrap(true);
        txtArCommitteeDesc.setWrapStyleWord(true);
        // End chandra.
        scrPnCommitteeDesc.setViewportView(txtArCommitteeDesc);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 4;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 8, 8);
        add(scrPnCommitteeDesc, gridBagConstraints);

        lblType.setFont(CoeusFontFactory.getLabelFont());
        lblType.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblType.setText("Type:");
        lblType.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 0, 0, 0);
        add(lblType, gridBagConstraints);

        lblHomeUnit.setFont(CoeusFontFactory.getLabelFont());
        lblHomeUnit.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblHomeUnit.setText("Home Unit:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 8, 0, 0);
        add(lblHomeUnit, gridBagConstraints);

        cmbCommitteeType.setFont(CoeusFontFactory.getNormalFont());
        cmbCommitteeType.setMaximumSize(new java.awt.Dimension(130, 25));
        cmbCommitteeType.setMinimumSize(new java.awt.Dimension(124, 25));
        cmbCommitteeType.setName("cmbCommitteeType");
        cmbCommitteeType.setPreferredSize(new java.awt.Dimension(132, 25));
        cmbCommitteeType.setAutoscrolls(true);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 0, 8);
        add(cmbCommitteeType, gridBagConstraints);

        //Bug Fix:1077 Start 1
        txtUnitNo.setFont(CoeusFontFactory.getNormalFont());
        txtUnitNo.setHorizontalAlignment(javax.swing.JTextField.LEFT);
        //Bug Fix:1077 End 1
        
        txtUnitNo.setMinimumSize(new java.awt.Dimension(135, 20));
        txtUnitNo.setPreferredSize(new java.awt.Dimension(135, 20));
        
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 0, 0);
        add(txtUnitNo, gridBagConstraints);

        btnSearch.setFont(CoeusFontFactory.getLabelFont());
        
        //Bug Fix:1077 Start 2
        btnSearch.setIcon(new javax.swing.ImageIcon(
                getClass().getClassLoader().getResource(CoeusGuiConstants.SEARCH_ICON)));
        
//        btnSearch.setIcon(new javax.swing.ImageIcon(
//                getClass().getResource(CoeusGuiConstants.FIND_ICON)));
        btnSearch.setName("Search");
        btnSearch.setMaximumSize(new java.awt.Dimension(23,23));
        btnSearch.setMinimumSize(new java.awt.Dimension(23,23));
        btnSearch.setPreferredSize(new java.awt.Dimension(23,23));
        //Bug Fix:1077 End 2
        
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 0, 0, 0);
        add(btnSearch, gridBagConstraints);

        lblUnitName.setFont(CoeusFontFactory.getLabelFont());
        lblUnitName.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        lblUnitName.setMinimumSize(new java.awt.Dimension(100, 20));
        lblUnitName.setPreferredSize(new java.awt.Dimension(100, 20));
        lblUnitName.setHorizontalTextPosition(javax.swing.SwingConstants.LEFT);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 0, 8);
        add(lblUnitName, gridBagConstraints);

        lblMinimumMembers.setFont(CoeusFontFactory.getLabelFont());
        lblMinimumMembers.setHorizontalAlignment(
                javax.swing.SwingConstants.RIGHT);
        String minimumMembers = coeusMessageResources.parseLabelKey("committeeMinimumMembers.1107");
        
        // Added to use CoeusLabel instead Module Parameter XML file Start :02-Sep-2005
        lblMinimumMembers.setText(minimumMembers+":");
        //End : 02-Sep-2005
        
        // Commented to use CoeusLabel instead Module Parameter XML file Start :02-Sep-2005
        /*if( customParameters != null ){
            String minimumMembers = (String)customParameters.elementAt(0);
            if(minimumMembers != null){
                lblMinimumMembers.setText( minimumMembers+":" );
            }
            //lblMinimumMembers.setText("Minimum Members:");
        }else{
            lblMinimumMembers.setText("Minimum Members:");
        }*/
        //End 02-Sep-2005
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 8, 0, 0);
        add(lblMinimumMembers, gridBagConstraints);

        txtMinMembers.setFont(CoeusFontFactory.getNormalFont());
        txtMinMembers.setHorizontalAlignment(javax.swing.JTextField.LEFT);
        txtMinMembers.setMinimumSize(new java.awt.Dimension(124, 20));
        txtMinMembers.setPreferredSize(new java.awt.Dimension(124, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 0, 0);
        add(txtMinMembers, gridBagConstraints);

        lblMaxProtocols.setFont(CoeusFontFactory.getLabelFont());
        lblMaxProtocols.setHorizontalAlignment(
                javax.swing.SwingConstants.RIGHT);
        lblMaxProtocols.setText("Maximum Protocols:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 0, 0);
        add(lblMaxProtocols, gridBagConstraints);

        txtAdvSubmissionDays.setFont(CoeusFontFactory.getNormalFont());
        txtAdvSubmissionDays.setHorizontalAlignment(
                javax.swing.JTextField.LEFT);
        txtAdvSubmissionDays.setMinimumSize(new java.awt.Dimension(124, 20));
        txtAdvSubmissionDays.setPreferredSize(new java.awt.Dimension(124, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 0, 0);
        add(txtAdvSubmissionDays, gridBagConstraints);

        txtMaxProtocols.setFont(CoeusFontFactory.getNormalFont());
        txtMaxProtocols.setHorizontalAlignment(javax.swing.JTextField.LEFT);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.ipadx = 120;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 0, 8);
        add(txtMaxProtocols, gridBagConstraints);

        lblAdvSubmissionDays.setFont(CoeusFontFactory.getLabelFont());
        lblAdvSubmissionDays.setHorizontalAlignment(
                javax.swing.SwingConstants.RIGHT);
        lblAdvSubmissionDays.setText("Adv Submission Days:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.insets = new java.awt.Insets(4, 8, 0, 0);
        add(lblAdvSubmissionDays, gridBagConstraints);

        lblReviewType.setFont(CoeusFontFactory.getLabelFont());
        lblReviewType.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblReviewType.setText("Review Type:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 0, 0, 0);
        add(lblReviewType, gridBagConstraints);

        cmbReviewType.setFont(CoeusFontFactory.getNormalFont());
        cmbReviewType.setMaximumSize(new java.awt.Dimension(130, 25));
        cmbReviewType.setMinimumSize(new java.awt.Dimension(124, 25));
        cmbReviewType.setPreferredSize(new java.awt.Dimension(132, 25));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 0, 8);
        add(cmbReviewType, gridBagConstraints);

        lblLastUpdated.setFont(CoeusFontFactory.getLabelFont());
        lblLastUpdated.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblLastUpdated.setText("Last Updated:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 8, 0, 0);
        add(lblLastUpdated, gridBagConstraints);

        txtLastUpdated.setEditable(false);
        txtLastUpdated.setFont(CoeusFontFactory.getNormalFont());
        txtLastUpdated.setMinimumSize(new java.awt.Dimension(124, 20));
        txtLastUpdated.setPreferredSize(new java.awt.Dimension(124, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 0, 0);
        add(txtLastUpdated, gridBagConstraints);

        lblUpdatedUser.setFont(CoeusFontFactory.getLabelFont());
        lblUpdatedUser.setText("Updated User:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 0, 0, 0);
        add(lblUpdatedUser, gridBagConstraints);

        txtUpdatedUser.setEditable(false);
        txtUpdatedUser.setFont(CoeusFontFactory.getNormalFont());
        txtUpdatedUser.setHorizontalAlignment(javax.swing.JTextField.LEFT);
        txtUpdatedUser.setMinimumSize(new java.awt.Dimension(124, 20));
        txtUpdatedUser.setPreferredSize(new java.awt.Dimension(132, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 0, 8);
        add(txtUpdatedUser, gridBagConstraints);

        lblScheduleDesc.setFont(CoeusFontFactory.getLabelFont());
        lblScheduleDesc.setHorizontalAlignment(
                javax.swing.SwingConstants.RIGHT);
        lblScheduleDesc.setText("Schedule Description:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTH;

        gridBagConstraints.insets = new java.awt.Insets(4, 8, 8, 0);
        add(lblScheduleDesc, gridBagConstraints);
        
        //Bug fix code
        //Modified by Vyjayanthi on 26/08/03 to remove the horizontal scrollbar
        //scrPnScheduleDesc.setHorizontalScrollBarPolicy(
                //javax.swing.JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
        scrPnScheduleDesc.setVerticalScrollBarPolicy(
                javax.swing.JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        scrPnScheduleDesc.setPreferredSize(new java.awt.Dimension(18, 80));
       // Added by Chandra
        txtArScheduleDesc.setLineWrap(true);
        txtArScheduleDesc.setWrapStyleWord(true);
        // End Chandra
        scrPnScheduleDesc.setViewportView(txtArScheduleDesc);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.gridwidth = 4;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 8, 8);
        add(scrPnScheduleDesc, gridBagConstraints);
        //txtCommitteeId.setRequestFocusEnabled(true);
       //txtCommitteeId.requestFocus();
        
        // Added by Chandra 12/09/2003
            java.awt.Component[] components = {txtCommitteeId,txtCommitteeName,txtArCommitteeDesc,
            txtUnitNo,btnSearch,cmbCommitteeType,txtMinMembers,txtMaxProtocols,
            txtAdvSubmissionDays,cmbReviewType,txtArScheduleDesc,txtCommitteeId};
            ScreenFocusTraversalPolicy  traversePolicy = new ScreenFocusTraversalPolicy( components );
            setFocusTraversalPolicy(traversePolicy);
            setFocusCycleRoot(true);
        // End Chandra
    }
    /**
     * set the font for all labels in the form
     */
    private void setLabelFont(){

        lblLastUpdated.setFont(CoeusFontFactory.getLabelFont());
        lblMaxProtocols.setFont(CoeusFontFactory.getLabelFont());
        lblDescription.setFont(CoeusFontFactory.getLabelFont());
        lblCommitteeId.setFont(CoeusFontFactory.getLabelFont());
        lblScheduleDesc.setFont(CoeusFontFactory.getLabelFont());
        lblHomeUnit.setFont(CoeusFontFactory.getLabelFont());
        lblCommitteeName.setFont(CoeusFontFactory.getLabelFont());
        lblUpdatedUser.setFont(CoeusFontFactory.getLabelFont());
        lblType.setFont(CoeusFontFactory.getLabelFont());
        lblReviewType.setFont(CoeusFontFactory.getLabelFont());
        lblUnitName.setFont(CoeusFontFactory.getLabelFont());
        lblMinimumMembers.setFont(CoeusFontFactory.getLabelFont());
        lblAdvSubmissionDays.setFont(CoeusFontFactory.getLabelFont());

    }

    /**
     * This method is used to set the enable/disable status for the form controls 
     * depending on the function type.
     */
    public void formatFields() {
        boolean enableStatus = true;
        if (functionType == 'D') {
            enableStatus = false;
        } else {
            enableStatus = true;
        }

        txtArScheduleDesc.setOpaque(enableStatus);
        txtArScheduleDesc.setCaretPosition(0);
        txtArCommitteeDesc.setOpaque(enableStatus);
        txtArCommitteeDesc.setCaretPosition(0);
        txtCommitteeId.setCaretPosition(0);
        txtCommitteeId.setEditable(functionType == 'A' ? true:false);
        // Modified for COEUSQA-2685_IACUC - comm member areas of research should point to IACUC areas of research_start
//        cmbCommitteeType.setEnabled(enableStatus);
        //this drop down is always disabled as Committee is selected from Committee Type window
        cmbCommitteeType.setEnabled(false);
        // Modified for COEUSQA-2685_IACUC - comm member areas of research should point to IACUC areas of research_end
        txtMinMembers.setEditable(enableStatus);
        txtMinMembers.setEditable(enableStatus);
        txtUnitNo.setEditable(enableStatus);
        txtUnitNo.setCaretPosition(0);
        txtArScheduleDesc.setEditable(enableStatus);
        txtArCommitteeDesc.setEditable(enableStatus);
        txtMaxProtocols.setEditable(enableStatus);
        txtCommitteeName.setEditable(enableStatus);
        txtCommitteeName.setCaretPosition(0);
        cmbReviewType.setEnabled(enableStatus);
        txtAdvSubmissionDays.setEditable(enableStatus);
        btnSearch.setEnabled(enableStatus);
    }

    /** Get the Committee details from the database by passing the Committee id.
     * This method communicates with the server using java io streams and gets the
     * committee details and populates the bean.
     *
     * @param commId String representing Committee id to get the details.
     * @return <CODE>CommitteeMaintenanceFormBean</CODE> which will be used to set the Committee
     * details
     */
    public CommitteeMaintenanceFormBean getCommitteeDetails(String commId) {
        committeeMaintBean = new CommitteeMaintenanceFormBean();
        String connectTo = CoeusGuiConstants.CONNECTION_URL + "/comMntServlet";
        /* connect to the database and get the formData for the given
         * committee id
         */
        RequesterBean request = new RequesterBean();
        request.setFunctionType(functionType);
        request.setId(commId);
        AppletServletCommunicator comm =
                new AppletServletCommunicator(connectTo, request);
        comm.send();
        ResponderBean response = comm.getResponse();
        Vector dataObjects = null;
        if (response.isSuccessfulResponse()) {
            dataObjects = response.getDataObjects();
            //0 - set committee details
            committeeMaintBean =
                    (CommitteeMaintenanceFormBean) dataObjects.elementAt(0);
            //1 - set committee types
            committeeTypes = (Vector) dataObjects.elementAt(1);
            //2 - set review types
            reviewTypes = (Vector) dataObjects.elementAt(2);

        } else {
            CoeusOptionPane.showErrorDialog(response.getMessage());
        }
        return committeeMaintBean;
    }

    /** Set the values of form controls. The values are available with
     * the bean given as parameter.
     *
     * @param committeeMaintBean <CODE>CommitteeMaintenanceFormBean</CODE> which contains
     * the Committee details.
     */
    public void setValues(CommitteeMaintenanceFormBean committeeMaintBean) {
        this.committeeMaintBean = committeeMaintBean;
        if(committeeTypes != null && committeeTypes.size()>0){
            cmbCommitteeType.setModel(
                    (new CoeusComboBox(committeeTypes,false)).getModel());
        }
        if(reviewTypes != null && reviewTypes.size()>0){
            cmbReviewType.setModel(
                    (new CoeusComboBox(reviewTypes,false)).getModel());
        }
        if (committeeMaintBean != null) {
            txtCommitteeId.setText(committeeMaintBean.getCommitteeId());
            //setting committee type combo
            ComboBoxBean comboBean = new ComboBoxBean();
            comboBean.setCode(new Integer(
                    committeeMaintBean.getCommitteeTypeCode()).toString());
            comboBean.setDescription(committeeMaintBean.getCommitteeTypeDesc());
            if(functionType =='D'){
                Vector cTypes = new Vector();
                cTypes.addElement(comboBean);
                cmbCommitteeType.setModel(
                        (new CoeusComboBox(cTypes,false)).getModel());
                cmbCommitteeType.setSelectedItem(comboBean);
            }else{
                for(int typeRow = 0; typeRow < cmbCommitteeType.getItemCount();
                        typeRow++){
                    if(((ComboBoxBean)cmbCommitteeType.getItemAt(
                            typeRow)).toString().equals(comboBean.toString())){
                        cmbCommitteeType.setSelectedIndex(typeRow);
                    }
                }
            }

            comboBean = new ComboBoxBean();
            comboBean.setCode(new Integer(
                    committeeMaintBean.getApplReviewTypeCode()).toString());
            comboBean.setDescription(committeeMaintBean.getReviewTypeDesc());
            if(functionType =='D'){
                Vector rTypes = new Vector();
                rTypes.addElement(comboBean);
                cmbReviewType.setModel(
                        (new CoeusComboBox(rTypes,false)).getModel());
                cmbReviewType.setSelectedItem(comboBean);
            }else{
                for(int typeRow = 0; typeRow < cmbReviewType.getItemCount();
                        typeRow++){
                    if(((ComboBoxBean)cmbReviewType.getItemAt(
                            typeRow)).getCode().equals(comboBean.getCode())){
                        cmbReviewType.setSelectedIndex(typeRow);
                    }
                }

            }
            txtMinMembers.setText("" + committeeMaintBean.getMinMembers());
            String lastUpdateTime =
                    committeeMaintBean.getUpdateTimestamp() == null ? null :
                            committeeMaintBean.getUpdateTimestamp().toString();
            if(lastUpdateTime!=null){
                txtLastUpdated.setText(
                        dateUtils.formatDate(lastUpdateTime, "dd-MMM-yyyy"));
            }else{
                txtLastUpdated.setText("");
            }
            txtUnitNo.setText(committeeMaintBean.getUnitNumber());
            lblUnitName.setText(committeeMaintBean.getUnitName());
            txtArCommitteeDesc.setText(committeeMaintBean.getDescription());
            txtArCommitteeDesc.setCaretPosition(0);
            txtArScheduleDesc.setText(
                    committeeMaintBean.getScheduleDescription());
            txtArScheduleDesc.setCaretPosition(0);
            txtMaxProtocols.setText("" + committeeMaintBean.getMaxProtocols());
            txtCommitteeName.setText(committeeMaintBean.getCommitteeName());
//            txtUpdatedUser.setText(committeeMaintBean.getUpdateUser());
            /*
             * UserID to UserName Enhancement - Start
             * Added UserUtils class to change userid to username
             */
            txtUpdatedUser.setText(UserUtils.getDisplayName(committeeMaintBean.getUpdateUser()));
            // UserId to UserName Enhancement - End
            txtAdvSubmissionDays.setText("" +
                    committeeMaintBean.getAdvSubmissionDaysReq());

            previousUnitNo = txtUnitNo.getText();
            previousUnitName = lblUnitName.getText();
        }
        txtCommitteeId.setEditable(false);
        saveRequired = false;
    }

    /** Get the values from the form controls and set it back to Committee bean.
     * This will be called from <CODE>CommitteeDetailsForm</CODE> for saving the information
     * in the database.
     *
     * @return <CODE>CommitteeMaintenanceFormBean</CODE> with all the Committee details.
     */
    public CommitteeMaintenanceFormBean getValues() {
        boolean modified = false;
        if (committeeMaintBean == null) {
            committeeMaintBean = new CommitteeMaintenanceFormBean();
            committeeMaintBean.setAcType("I");
        }
        String committeeTypeCode = ((ComboBoxBean)
                cmbCommitteeType.getModel().getSelectedItem()).getCode();
        String reviewTypeCode = ((ComboBoxBean)
                cmbReviewType.getModel().getSelectedItem()).getCode();
        if(functionType == 'M'){
            if(!committeeMaintBean.getCommitteeId().equals(
                    txtCommitteeId.getText())){
                modified = true;
            }
            if(!committeeMaintBean.getUnitNumber().equals(txtUnitNo.getText())){
                modified = true;
            }
            if(!((committeeMaintBean.getCommitteeName() == null? "" :
                        committeeMaintBean.getCommitteeName()).equals(
                                txtCommitteeName.getText()))){
                modified = true;
            }

            if(!((committeeMaintBean.getDescription() == null ? "" :
                        committeeMaintBean.getDescription()).equals(
                                txtArCommitteeDesc.getText()))){
                modified = true;
            }
            if(!((committeeMaintBean.getScheduleDescription() == null ? "" :
                        committeeMaintBean.getScheduleDescription()).equals(
                                txtArScheduleDesc.getText()))){
                modified = true;
            }
            if(!(committeeMaintBean.getCommitteeTypeCode()
                    == Integer.parseInt(((ComboBoxBean)
                        cmbCommitteeType.getSelectedItem()).getCode()))){
                modified = true;
            }
            if(!(committeeMaintBean.getMinMembers() 
                == Integer.parseInt((txtMinMembers.getText().equals("") 
                        ? "0" : txtMinMembers.getText())))){
                modified = true;
            }
            if(!(committeeMaintBean.getMaxProtocols() == Integer.parseInt(
                    (txtMaxProtocols.getText().equals("") 
                        ? "0" :txtMaxProtocols.getText())))){
                modified = true;
            }
            if(!(committeeMaintBean.getAdvSubmissionDaysReq() 
                == Integer.parseInt((txtAdvSubmissionDays.getText().equals("") 
                    ? "0" :txtAdvSubmissionDays.getText())))){
                modified = true;
            }
            if(!(committeeMaintBean.getApplReviewTypeCode()
                    == Integer.parseInt(reviewTypeCode))){
                modified = true;
            }
            if(modified){
                committeeMaintBean.setAcType("U");
            }

        }
        committeeMaintBean.setCommitteeId(txtCommitteeId.getText());
        committeeMaintBean.setCommitteeName(txtCommitteeName.getText());
        committeeMaintBean.setUnitNumber(txtUnitNo.getText());
        committeeMaintBean.setUnitName(lblUnitName.getText());
        if(txtArCommitteeDesc.getText()!=null){
            committeeMaintBean.setDescription(txtArCommitteeDesc.getText());
        }else{
            committeeMaintBean.setDescription("");
        }
        if(txtArScheduleDesc.getText()!=null){
            committeeMaintBean.setScheduleDescription(
                    txtArScheduleDesc.getText());
        }else{
            committeeMaintBean.setScheduleDescription("");
        }

        committeeMaintBean.setCommitteTypeCode(
                Integer.parseInt(committeeTypeCode));
        try{
            committeeMaintBean.setMinMembers(
                    Integer.parseInt((txtMinMembers.getText().equals("") ? "0" 
                                        :txtMinMembers.getText())));
        }catch(Exception nfe){
            committeeMaintBean.setMinMembers(0);
        }

        try{
            committeeMaintBean.setMaxProtocols(
                    Integer.parseInt((txtMaxProtocols.getText().equals("") ? "0"
                                        :txtMaxProtocols.getText())));
        }catch(Exception nfe){
            committeeMaintBean.setMaxProtocols(0);
        }

        try{
            committeeMaintBean.setAdvSubmissionDaysReq(
                    Integer.parseInt((txtAdvSubmissionDays.getText().equals("") 
                                ? "0" :txtAdvSubmissionDays.getText())));
        }catch(Exception nfe){
            committeeMaintBean.setAdvSubmissionDaysReq(0);
        }

        /* commented by ravi on 14-Oct for using applicable review type only 
         * and to populate data from OSP$PROTOCOL_REVIEW_TYPE;
         committeeMaintBean.setReviewTypeCode(Integer.parseInt(reviewTypeCode));
        // Setting default value  
        committeeMaintBean.setApplReviewTypeCode(1); */
        committeeMaintBean.setApplReviewTypeCode( Integer.parseInt(reviewTypeCode) );

        committeeMaintBean.setUpdateUser(txtUpdatedUser.getText());


        return committeeMaintBean;

    }

    /**
     * This is an inner class used to associate all the fields with
     * key event to fire the changes done in the controls
     */
    private class updateChanges extends KeyAdapter {
		
		//Modified by Nadh for Save Confirmation Bug Fix 0n 6th Dec - 2004 - start
        public void keyReleased(KeyEvent ae) {
			Object ob = ae.getSource();
			if(!((javax.swing.text.JTextComponent)ob).getText().equals(prevText)) {
				prevText = ((javax.swing.text.JTextComponent)ob).getText();
			    saveRequired=true;
			}
        }
        public void keyTyped(KeyEvent ae) {
			Object ob = ae.getSource();
			if(!((javax.swing.text.JTextComponent)ob).getText().equals(prevText)) {
				prevText = ((javax.swing.text.JTextComponent)ob).getText();
			    saveRequired=true;
			}
			else {
				saveRequired=false;
			}
        } // end 
		//Commented by Nadh for Save Confirmation Bug Fix 0n 6th Dec - 2004 - start
//     public void keyPressed(KeyEvent ae) {
//				saveRequired=false;
//    } - end
	}
	//Added by Nadh for Save Confirmation Bug Fix 0n 6th Dec - 2004 - start
	 private class GetValues extends MouseAdapter {
		  public void mouseClicked(MouseEvent mouseEvent){
			  prevText = ((javax.swing.text.JTextComponent)mouseEvent.getSource()).getText();
		  }
	 }//end
	
    /**
     * This method is used to throw the exception with the given custom message.
     *
     * @param mesg String representing the custom message.
     *
     * @throws Exception with the given custom message.
     */
    public void log(String mesg) throws Exception {
        throw new Exception(mesg);
    }

    /** Indicates whether to save the Committee details or not.
     *
     * @return boolean true to save the details else false.
     */
    public boolean isSaveRequired(){
        if(functionType != 'D'){
            return saveRequired;
        }else{
            return false;
        }
    }

    /** This method is used to set the <CODE>saveRequired</CODE> flag from <CODE>CommitteeDetailsForm</CODE>
     *
     * @param saveRequired boolean true if  modifications are to be saved else
     * false.
     */
    public void setSaveRequired(boolean saveRequired){
        this.saveRequired = saveRequired;
    }

    /** Validates the form data before sending to the database.
     *
     * @return boolean true if all form controls validation are successful else
     * false.
     * @throws Exception with custom message if any of the form controls doesn't
     * have valid data.
     */
    public boolean validateData() throws Exception{
        boolean dataOK = false;
        if (unitName.equals("")){
            unitName = getUnitName(txtUnitNo.getText());
        }
        String committeeTypeCode = ((ComboBoxBean)
                cmbCommitteeType.getModel().getSelectedItem()).getCode();
        String reviewTypeCode = ((ComboBoxBean)
                cmbReviewType.getModel().getSelectedItem()).getCode();
        if (functionType == 'A' && (txtCommitteeId.getText() == null
                || txtCommitteeId.getText().trim().length() == 0)) {
            log(coeusMessageResources.parseMessageKey(
                                            "commMntFrm_exceptionCode.1010"));
            txtCommitteeId.requestFocus();
        } else if (functionType == 'A'
                && txtCommitteeId.getText().trim().length() > 15) {
            log(coeusMessageResources.parseMessageKey(
                                            "commMntFrm_exceptionCode.1011"));
            txtCommitteeId.requestFocus();
        } else if (functionType == 'A'
                && validateDuplicate(txtCommitteeId.getText().trim()) ) {
            log("Committee id '" + txtCommitteeId.getText().trim()
                + " is already exists. Please enter a different one.");
            txtCommitteeId.requestFocus();
        } else if (txtCommitteeName.getText() == null
                || txtCommitteeName.getText().trim().length() == 0) {
            log(coeusMessageResources.parseMessageKey(
                                            "commMntFrm_exceptionCode.1012"));
            txtCommitteeName.requestFocus();
        } else if (txtCommitteeName.getText().trim().length() >60) {
            log(coeusMessageResources.parseMessageKey(
                                            "commMntFrm_exceptionCode.1013"));
            txtCommitteeName.requestFocus();
        } else if (txtUnitNo.getText() == null
                || txtUnitNo.getText().trim().length() == 0) {
            log(coeusMessageResources.parseMessageKey(
                                            "commMntFrm_exceptionCode.1014"));
            txtUnitNo.requestFocus();
        } else if (txtUnitNo.getText().trim().length() <= 0) {
            log(coeusMessageResources.parseMessageKey(
                                            "commMntFrm_exceptionCode.1008"));
            txtUnitNo.setText(previousUnitNo);
            lblUnitName.setText(previousUnitName);
            txtUnitNo.requestFocus();
        } else if (txtUnitNo.getText().trim().length() > 8) {
            log(coeusMessageResources.parseMessageKey(
                                            "commMntFrm_exceptionCode.1015"));
            txtUnitNo.setText(previousUnitNo);
            lblUnitName.setText(previousUnitName);
            txtUnitNo.requestFocus();
        }else if((unitName == null) || (unitName.trim().equals(""))){
            log(coeusMessageResources.parseMessageKey(
                                            "commMntFrm_exceptionCode.1016"));
            txtUnitNo.setText(previousUnitNo);
            lblUnitName.setText(previousUnitName);
            txtUnitNo.requestFocus();
        }else if (committeeTypeCode.trim().length() <= 0) {
            log(coeusMessageResources.parseMessageKey(
                                            "commMntFrm_exceptionCode.1017"));
            cmbCommitteeType.requestFocus();
        } else if (committeeTypeCode.trim().length() > 3) {
            log(coeusMessageResources.parseMessageKey(
                                            "commMntFrm_exceptionCode.1018"));
            cmbCommitteeType.requestFocus();
        } else if (reviewTypeCode.trim().length() <= 0) {
            log(coeusMessageResources.parseMessageKey(
                                            "commMntFrm_exceptionCode.1019"));
            cmbReviewType.requestFocus();
        } else if (reviewTypeCode.trim().length() > 3) {
            log(coeusMessageResources.parseMessageKey(
                                            "commMntFrm_exceptionCode.1020"));
            cmbReviewType.requestFocus();
        } else if (txtMinMembers.getText() != null
                &&  txtMinMembers.getText().trim().length() > 3) {
            log(coeusMessageResources.parseMessageKey(
                                            "commMntFrm_exceptionCode.1021"));
            txtMinMembers.requestFocus();
        } else if (txtMaxProtocols.getText() != null
                &&  txtMaxProtocols.getText().trim().length() > 4) {
            log(coeusMessageResources.parseMessageKey(
                                            "commMntFrm_exceptionCode.1022"));
            txtMaxProtocols.requestFocus();
        } else if (txtAdvSubmissionDays.getText() != null
                &&  txtAdvSubmissionDays.getText().trim().length() > 3) {
            log(coeusMessageResources.parseMessageKey(
                                            "commMntFrm_exceptionCode.1023"));
            txtAdvSubmissionDays.requestFocus();
        } //prps code start - jan 28 2004
        //Modified for COEUSDEV-222 : Some IRB-related rights not working correctly - Start
        //To check 'ADD_COMMITTEE' right only when new committee is created.
        else if(functionType == 'A'){
            dataOK = isAuthorizedToUnit() ; 
        }else{
            dataOK = true;
        }
        //COEUSDEV-222 : END
        //prps code end - jan 28 2004 
        
        return dataOK;
    }

    //prps start - jan 28 2004
    // This methos will check if the user has authorization to assign
    // the home unit selected
    private boolean isAuthorizedToUnit() throws Exception
    {
        String connectTo = CoeusGuiConstants.CONNECTION_URL +
            "/comMntServlet";
        
        RequesterBean request = new RequesterBean();
        request.setFunctionType('v');
        request.setId(txtCommitteeId.getText());
        request.setDataObject(txtUnitNo.getText());
        AppletServletCommunicator comm = new AppletServletCommunicator(
        connectTo, request);
        comm.send();
        ResponderBean response = comm.getResponse();
        if (response.isSuccessfulResponse()) 
        {
            return true ;
        }
        else
        {
            log(response.getMessage()) ;
        }
        
        return false ;
    }           
    //prps end - jan 28 2004
    
    
    
    /** Check whether the Committee id entered by the user is unique or not.
     *
     * @param commId String representing the committee Id.
     * @return boolean true if the given committee id is not unique else false.
     */
    public boolean validateDuplicate(String commId) {
        boolean duplicate = false;
        //Modified for COEUSQA-2675 Remaining IACUC Protocol Actions Batch Correspondence-start
        String connectTo = CoeusGuiConstants.CONNECTION_URL + "/iacucCorrespondenceServlet";
        //Modified for COEUSQA-2675 Remaining IACUC Protocol Actions Batch Correspondence-end
        RequesterBean request = new RequesterBean();
        request.setId(commId);
        request.setFunctionType('X');
        AppletServletCommunicator comm =
                new AppletServletCommunicator(connectTo, request);
        comm.send();
        ResponderBean response = comm.getResponse();
        if (response == null) {
            response = new ResponderBean();
            response.setResponseStatus(false);
            response.setMessage(coeusMessageResources.parseMessageKey(
                                            "commMntFrm_exceptionCode.1024"));
        }
        if (response.isSuccessfulResponse()) {
            if (response.getDataObject()!=null) {
                duplicate = true;
            }
        }
        return duplicate;
    }

    /** This method is used to get the Unit Name for the unit number entered in
     * the <CODE>txtUnitNumber</CODE> by the user. If the unit number is not valid it will
     * prompt the user to enter the valid one.
     *
     * @param unitNumber String representing unit number.
     * @return String representing unit name corresponding to the given unit
     * number, if it is a valid unit number else empty string.
     */
    public String getUnitName(String unitNumber){
        boolean success=false;
        String unitName= "";
        RequesterBean requester = new RequesterBean();
        requester.setFunctionType('G');
        UnitDetailFormBean unitDetail = new UnitDetailFormBean();
        unitDetail.setUnitNumber(unitNumber);
        requester.setId(unitNumber);
        requester.setDataObject(unitDetail);
        String connectTo = connectionURL + "/unitServlet";
        AppletServletCommunicator comm =
                new AppletServletCommunicator(connectTo, requester);
        comm.send();
        ResponderBean response = comm.getResponse();
        if (response.isSuccessfulResponse()){
            unitDetail = (UnitDetailFormBean)response.getDataObject();
            if ( unitDetail != null ){
                unitName = unitDetail.getUnitName()
                    == null ? "" : unitDetail.getUnitName();
            }
        }
        return unitName;
    }


    /** This listener is associated with <CODE>txtUnitNumber</CODE>. This is used to
     * get the unit name by searching the database using <CODE>CoeusSearch</CODE> component.
     *
     * @param ae <CODE>ActionEvent</CODE>, a semantic event which indicates that a
     * component-defined action occured.
     */
    public void actionPerformed(ActionEvent ae){
       java.util.HashMap selectedRow = new java.util.HashMap();
        try{
            if ( ae.getSource()==btnSearch ) {
                CoeusSearch coeusSearch
                        = new CoeusSearch(new JFrame(),"leadunitsearch",1);
                coeusSearch.showSearchWindow();
                selectedRow = coeusSearch.getSelectedRow();
                if (selectedRow != null){
                    this.txtUnitNo.setText(
                            selectedRow.get("UNIT_NUMBER").toString());
                    previousUnitNo = this.txtUnitNo.getText();
                    this.lblUnitName.setText(
                            selectedRow.get("UNIT_NAME").toString());
                    previousUnitName = this.lblUnitName.getText();
                }
            }
        }catch(Exception ex){
            ex.printStackTrace();
        }
    }
    /** This method will listen to the changes made in comboboxes in this form
     * and sets the <CODE>saveRequired</CODE> flag to true.
     *
     * @param itemEvent <CODE>ItemEvent</CODE>, a semantic event which indicates that an item was selected or deselected.
     */
    public void itemStateChanged(ItemEvent itemEvent) {
        saveRequired = true;
    }    
   // Commented to use CoeusLabel instead Module Parameter XML file Start :02-Sep-2005
//    /** Getter for property customParameters.
//     * @return Value of property customParameters.
//     */
//    public java.util.Vector getCustomParameters() {
//        return customParameters;
//    }
//    
//    /** Setter for property customParameters.
//     * @param customParameters New value of property customParameters.
//     */
//    public void setCustomParameters(java.util.Vector customParameters) {
//        this.customParameters = customParameters;
//    }
    //End : 02-Sep-2005
    
}
