/*
 * @(#)PersonDetailForm.java
 *
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 *
 */

/* PMD check performed, and commented unused imports and variables on 14-MAY-2007
 * by Leena
 */

package edu.mit.coeus.departmental.gui;

import edu.mit.coeus.departmental.bean.PersonCustomElementsInfoBean;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Vector;
//import java.util.Hashtable;
import javax.swing.event.*;

import edu.mit.coeus.gui.*;
import edu.mit.coeus.departmental.bean.DepartmentPersonFormBean;
// JM needed for custom features
import edu.mit.coeus.exception.CoeusClientException;
// JM END
import edu.mit.coeus.propdev.bean.ProposalPersonFormBean;
import edu.mit.coeus.propdev.bean.ProposalCustomElementsInfoBean;
import edu.mit.coeus.customelements.bean.CustomElementsInfoBean;
import edu.mit.coeus.irb.bean.*;
import edu.mit.coeus.utils.*;
import edu.mit.coeus.utils.customelements.*;
import edu.mit.coeus.brokers.RequesterBean;
import edu.mit.coeus.brokers.ResponderBean;
import edu.mit.coeus.propdev.gui.ProposalPersonnelForm;

/** This class constructs the tabbed pages required for Person Details.
 * It includes Organization Details, Personal Details, Misc details and Other Cost elements Details
 *
 * @version :1.0
 * @author Raghunath P.V.
 */


public class PersonDetailForm extends javax.swing.JComponent implements TypeConstants{
    
    private JPanel pnlForm;
    private JTabbedPane tbdPnTabbedPane ;
    private boolean canMainOtherByLoginUser;
    private boolean userCanMaintainUnit;
    private boolean userCanMaintainSelectedPerson;
    private String loginUserName;
    private boolean isOtherMaintained;
    private Vector vecCustColumnsForModule;
    private Vector vecDepartmentOthersFormBean;
    private Vector vecCustomElementsData;
    private JTable tblPropPerson;
    private Vector vecEditableColumns;
    private int canModifyProposal;
    private boolean maintainPerson;
    
    private String personId;
    private char moduleCode;
    private char propOtherFunctionType = MODIFY_MODE;
    
    //Added for Coeus 4.3 PT ID - 2388:Person Enhancements - start
    //denotes the person servlet
    private final String PERSON_SERVLET = "/personMaintenanceServlet";
    //used to denote the form is taken from the Personnel module of the Maintain Menu
    private static final char PERSONNEL_MODULE_CODE = 'N';
    //holds the function type to add a person
    private static final char ADD_PERSON_FN_TYPE='G';
    //holds the function type to get the person custom elements
    private static final char GET_PERSON_CUSTOM_ELEMENTS = 'Q';
    //Added for Coeus 4.3 PT ID - 2388:Person Enhancements - end
    
    private static final char DEPARTMENT_PERSON_MODULE_CODE = 'D';
    private static final char PROPOSAL_PERSON_MODULE_CODE = 'P';
    private static final char GET_EDITABLE_COLUMN_NAMES = 'Y';
    private static final char SAVE_PROPOSAL_PERSON_RECORD = 'X';
    private static final char GET_PROPOSAL_PERSON_DETAILS = 'W';
    
    private boolean propPersonInfoTabChanged;
    
    PersonOrganizationDetail personOrganizationDetail;
    PersonPersonalDetail personPersonalDetail;
    PersonMiscDetail personMiscDetail;
    PersonOtherDetail personOtherDetail;
    CustomElementsForm customElementsForm;
    DepartmentPersonFormBean departmentPersonFormBean;
    PersonTrainingInformationForm personTrainingInformationForm; // added by Nadh
    
    //Case #1602 Start 1
    private PersonContactInfoForm personContactInfoForm;
    private static final char GET_PROP_PERSON_DETAILS_FOR_INV_KEYSTUDY = 'q';
    private boolean validate = true;
    //Case #1602 End 1
    
    //private JDialog dlgParentComponent;
    private CoeusDlgWindow dlgParentComponent;
    
    private CoeusAppletMDIForm mdiForm;
    private char functionType;
    //private String connectionURL = CoeusGuiConstants.CONNECTION_URL;
    private boolean saveRequired;
    
    private String personName;
    private String proposalId;
    
    //holds CoeusMessageResources instance used for reading message Properties.
    private CoeusMessageResources coeusMessageResources;
    
    private Vector data;// holdds training info vectors - added by nadh
    
    
    JButton btnCancel;
    //Bug fix for case #2059 by tarique start 1
    private Integer sortId;
    //Bug fix for case #2059 by tarique end 1
    //Added for Case #2311 start
    private Object windowInstance;
    /** Creates a new instance of PersonDetailForm */
    //Code added for case#3183 - Proposal Hierarchy enhancement
    private boolean disabled;
    //Added for case 3761 - Modify Person right issue - start
    private boolean canModifyPerson;
    //Added for case 3761 - Modify Person right issue - end
    
    //Case :#3149 - Tabbing between fields does not work on others tabs - Start
    private int count = 1;
    //Case :#3149 - End
    
    private boolean canModifyAllFields;
    
    public PersonDetailForm(){
    }
    
    public PersonDetailForm(char functionType, char moduleType){
        
        this.functionType = functionType;
        this.moduleCode = moduleType;
        data = new Vector();
        //case 3465 - START
        maintainPerson = true;
        //case 3465 - END
        //Added for case 3761 - Modify Person right issue - start
        coeusMessageResources = CoeusMessageResources.getInstance();
        //Added for case 3761 - Modify Person right issue - end
    }
    
    
    
    /** Constructor used to create <CODE>PersonDetailForm</CODE> with
     * specific functionType, loginName and the Person Name.
     * @param perName represent the person for whom the details to be shown.
     * @param loginName a String represents the Login Id
     * @param functionType a char represents the following types
     *  <B>'A'</B> specifies that the form is in Add Mode
     *  <B>'M'</B> specifies that the form is in Modify Mode
     *  <B>'D'</B> specifies that the form is in Display Mode
     */
    public PersonDetailForm(String perName, String loginName,
            char functionType, char moduleType, int modifyProposal, String proposalId){
        this.functionType = functionType;
        this.personName = perName;
        this.loginUserName = loginName;
        this.moduleCode = moduleType;
        this.proposalId = proposalId;
        this.canModifyProposal = modifyProposal;
        data = new Vector();
        
        if(canModifyProposal == 1){
            maintainPerson = true;
        }
        
        if(personName != null && personName.length() > 0
                && loginUserName != null && loginUserName.length() > 0 ){
            
            if(personName.equalsIgnoreCase(loginUserName)){
                this.canMainOtherByLoginUser = true;
            }
            coeusMessageResources = CoeusMessageResources.getInstance();
            
            //departmentPersonFormBean = new DepartmentPersonFormBean();
            //Modified for Coeus 4.3 PT ID - 2388:Person Enhancements - start
            //Included the Personnel module
            if(moduleCode == DEPARTMENT_PERSON_MODULE_CODE ||
                    (moduleCode == PERSONNEL_MODULE_CODE && functionType!=ADD_MODE)){
                //Modified for Coeus 4.3 PT ID - 2388:Person Enhancements - end
                departmentPersonFormBean = getPersonDetailsfromDB(personName);
            }else if(moduleCode == PROPOSAL_PERSON_MODULE_CODE){
                departmentPersonFormBean = getProposalPersonDetailsFromDB(personName);
            }
            coeusMessageResources = CoeusMessageResources.getInstance();
            
            //Modified for Coeus 4.3 PT ID - 2388:Person Enhancements -start
            //Set the title of the Dialog window as Add Person and Modify Person
            //when in ADD_MODE and MODIFY_MODE resp.
            String title = "Person Details";
            if(moduleCode == PERSONNEL_MODULE_CODE){
                if(functionType == ADD_MODE){
                    title = "Add Person";
                }else if(functionType == MODIFY_MODE){
                    title = "Modify Person";
                }
            }
            dlgParentComponent = new CoeusDlgWindow(CoeusGuiConstants.getMDIForm(),
                    title, true);
            //Modified for Coeus 4.3 PT ID - 2388:Person Enhancements - end
            
            dlgParentComponent.getContentPane().add(createPersonDetails(
                    CoeusGuiConstants.getMDIForm()));
            
            //Case #1602 Start 2
            
            //Modified for Coeus 4.3 PT ID - 2388:Person Enhancements -start
            //To increase the height when the module type is Personnel, as there is
            // six extra components included
            //dlgParentComponent.setSize(675, 300);
            if(moduleCode == PERSONNEL_MODULE_CODE){
                //Modified for Case#2918 - Use of Salary Anniversary Date for calculating inflation in budget development module
                //dlgParentComponent.setSize(675, 375);
                dlgParentComponent.setSize(690, 390);
            }else{
                //Modified for Case#2918 - Use of Salary Anniversary Date for calculating inflation in budget development module
                //COEUSQA-1674 - Allow Division Lead Unit to be modified in the person details - Start
                //dlgParentComponent.setSize(675, 300);
                dlgParentComponent.setSize(690, 340);
                //COEUSQA-1674 - Allow Division Lead Unit to be modified in the person details - End
            }
            //Modified for Coeus 4.3 PT ID - 2388:Person Enhancements -end
            
            //dlgParentComponent.pack();
            //Case #1602 End 2
            
            //dlgParentComponent.setResizable(false);
            Dimension screenSize
                    = Toolkit.getDefaultToolkit().getScreenSize();
            
            Dimension dlgSize = dlgParentComponent.getSize();
            dlgParentComponent.setLocation(screenSize.width/2 - (dlgSize.width/2),
                    screenSize.height/2 - (dlgSize.height/2));
            dlgParentComponent.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
            dlgParentComponent.addEscapeKeyListener(
                    new AbstractAction("escPressed"){
                public void actionPerformed(ActionEvent ae){
                    performWindowClosing();
                }
            });
            dlgParentComponent.addWindowListener(new WindowAdapter(){
                public void windowClosing(WindowEvent we){
                    performWindowClosing();
                    return;
                }
                
                public void windowOpened(WindowEvent we){
                    btnCancel.requestFocusInWindow();
                }
            });
            dlgParentComponent.setVisible(true);
            btnCancel.requestFocusInWindow();
        
            //dlgParentComponent.show();
        }
    }
  
    
    public PersonDetailForm(String perName, String loginName, char functionType){
        
        this.functionType = functionType;
        this.personName = perName;
        this.loginUserName = loginName;
        data = new Vector(); //added by Nadh
        if(personName != null && personName.length() > 0
                && loginUserName != null && loginUserName.length() > 0 ){
            
            if(personName.equalsIgnoreCase(loginUserName)){
                this.canMainOtherByLoginUser = true;
            }
            coeusMessageResources = CoeusMessageResources.getInstance();
            moduleCode = DEPARTMENT_PERSON_MODULE_CODE;
            //departmentPersonFormBean = new DepartmentPersonFormBean();
            //Modified for Coeus 4.3 PT ID - 2388:Person Enhancements - start
            //Included the Personnel module
            if(moduleCode == DEPARTMENT_PERSON_MODULE_CODE || moduleCode == PERSONNEL_MODULE_CODE){
                //Modified for Coeus 4.3 PT ID - 2388:Person Enhancements - end
                
                departmentPersonFormBean = getPersonDetailsfromDB(personName);
            }else if(moduleCode == PROPOSAL_PERSON_MODULE_CODE){
                departmentPersonFormBean = getProposalPersonDetailsFromDB(personName);
            }
            
            coeusMessageResources = CoeusMessageResources.getInstance();
            
            //Modified for Coeus 4.3 PT ID - 2388:Person Enhancements -start
            //Set the title of the Dialog window as Add Person and Modify Person
            //when in ADD_MODE and MODIFY_MODE resp.
            String title = "Person Details";
            if(moduleCode == PERSONNEL_MODULE_CODE){
                if(functionType == ADD_MODE){
                    title = "Add Person";
                }else if(functionType == MODIFY_MODE){
                    title = "Modify Person";
                }
            }
            dlgParentComponent = new CoeusDlgWindow(CoeusGuiConstants.getMDIForm(),
                    title, true);
            //Modified for Coeus 4.3 PT ID - 2388:Person Enhancements - end
            
            dlgParentComponent.getContentPane().add(createPersonDetails(
                    CoeusGuiConstants.getMDIForm()));
            
            //Case #1602 Start 3
            //Modified for Coeus 4.3 PT ID - 2388:Person Enhancements -start
            //To increase the height when the module type is Personnel, as there is
            // six extra components included
            //dlgParentComponent.setSize(675, 300);
            if(moduleCode == PERSONNEL_MODULE_CODE){
                //Modified for Case#2918 - Use of Salary Anniversary Date for calculating inflation in budget development module
                //dlgParentComponent.setSize(675, 375);
                dlgParentComponent.setSize(690, 390);
            }else{
                //Modified for Case#2918 - Use of Salary Anniversary Date for calculating inflation in budget development module
                //dlgParentComponent.setSize(675, 300);
                //COEUSQA-1674 - Allow Division Lead Unit to be modified in the person details - Start
                //dlgParentComponent.setSize(690, 300);
                dlgParentComponent.setSize(690, 330);
                //COEUSQA-1674 - Allow Division Lead Unit to be modified in the person details - End
            }
            //Modified for Coeus 4.3 PT ID - 2388:Person Enhancements -end
            //dlgParentComponent.pack();
            //Case #1602 End 3
            
            // Added by chandra - 8/25/2003
            dlgParentComponent.setResizable(false);
            
            Dimension screenSize
                    = Toolkit.getDefaultToolkit().getScreenSize();
            Dimension dlgSize = dlgParentComponent.getSize();
            dlgParentComponent.setLocation(screenSize.width/2 - (dlgSize.width/2),
                    screenSize.height/2 - (dlgSize.height/2));
            dlgParentComponent.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
            dlgParentComponent.addEscapeKeyListener(
                    new AbstractAction("escPressed"){
                public void actionPerformed(ActionEvent ae){
                    performWindowClosing();
                }
            });
            
            dlgParentComponent.addWindowListener(new WindowAdapter(){
                public void windowClosing(WindowEvent we){
                    performWindowClosing();
                    return;
                }
                public void windowOpened(WindowEvent we){
                    btnCancel.requestFocusInWindow();
                }
            });
            dlgParentComponent.show();
        }
    }
    
    
    //Case #1602 Start 4
    public PersonDetailForm(String personID, String loginName,
            char functionType, char modCode, String porposalId){
        
        this.functionType = functionType;
        this.personName = personID; // since its an instance variable did not change the personName  to personID
        this.loginUserName = loginName;
        this.proposalId = porposalId;
        data = new Vector();
        
        if(modCode == 'I' || modCode == 'K'){
            this.moduleCode = DEPARTMENT_PERSON_MODULE_CODE;
            departmentPersonFormBean = getPropPersonDetails(personName);
        }
        coeusMessageResources = CoeusMessageResources.getInstance();
        
        dlgParentComponent = new CoeusDlgWindow(CoeusGuiConstants.getMDIForm(),
                "Person Details", true);
        dlgParentComponent.getContentPane().add(createPersonDetails(
                CoeusGuiConstants.getMDIForm()));
        //Modified for Coeus 4.3 PT ID - 2388:Person Enhancements -start
        //To increase the height when the module type is Personnel, as there is
        // six extra components included
        //dlgParentComponent.setSize(675, 300);
        if(moduleCode == PERSONNEL_MODULE_CODE){
            //Modified for Case#2918 - Use of Salary Anniversary Date for calculating inflation in budget development module
            dlgParentComponent.setSize(675, 375);
        }else{
            //Modified for Case#2918 - Use of Salary Anniversary Date for calculating inflation in budget development module
            //dlgParentComponent.setSize(675, 300);
            dlgParentComponent.setSize(690, 300);
        }
        //Modified for Coeus 4.3 PT ID - 2388:Person Enhancements -end
        dlgParentComponent.setResizable(false);
        
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        Dimension dlgSize = dlgParentComponent.getSize();
        dlgParentComponent.setLocation(screenSize.width/2 - (dlgSize.width/2),
                screenSize.height/2 - (dlgSize.height/2));
        
        dlgParentComponent.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
        
        dlgParentComponent.addEscapeKeyListener(
                new AbstractAction("escPressed"){
            public void actionPerformed(ActionEvent ae){
                performWindowClosing();
            }
        });
        
        dlgParentComponent.addWindowListener(new WindowAdapter(){
            public void windowClosing(WindowEvent we){
                performWindowClosing();
                return;
            }
            public void windowOpened(WindowEvent we){
                btnCancel.requestFocusInWindow();
            }
        });
        dlgParentComponent.show();
    }
    //Case #1602 End 4
    
    public void showPersonDetailForm(){
        
        mdiForm = CoeusGuiConstants.getMDIForm();
        loginUserName = mdiForm.getUserName();
        //Modified for Coeus 4.3 PT ID - 2388:Person Enhancements -start
        //Included the Personnel module
        if(moduleCode == DEPARTMENT_PERSON_MODULE_CODE ||
                (moduleCode == PERSONNEL_MODULE_CODE && functionType!=ADD_MODE)){
            //Modified for Coeus 4.3 PT ID - 2388:Person Enhancements -end
            if(personName != null && personName.length() > 0
                    && loginUserName != null && loginUserName.length() > 0 ){
                
                if(personName.equalsIgnoreCase(loginUserName)){
                    this.canMainOtherByLoginUser = true;
                }
                departmentPersonFormBean = getPersonDetailsfromDB(personName);
                //Added for case 3761 - Modify Person right issue - start
                if(moduleCode == PERSONNEL_MODULE_CODE && functionType != DISPLAY_MODE){
                    if(!canModifyPerson){
                        CoeusOptionPane.showErrorDialog(coeusMessageResources.parseMessageKey("person_exceptionCode.1001"));
                        return;
                    }
                }
                //Added for case 3761 - Modify Person right issue - end
            }
        }else if(moduleCode == PROPOSAL_PERSON_MODULE_CODE){
            if(personId != null){
                departmentPersonFormBean = getProposalPersonDetailsFromDB(personId);
            }
            if(personName.equalsIgnoreCase(loginUserName)){
                this.canMainOtherByLoginUser = true;
            }
            //Code commented and modified for case#3183 - Proposal Hierarchy enhancement
            //for displaying the details according to the mode.
            //if(canModifyProposal == 1){
            if(canModifyProposal == 1 && !isDisabled()){
                maintainPerson = true;
            }
        }
        //Added for Coeus 4.3 PT ID - 2388:Person Enhancements -start
        //To get the custom elements to be shown when in the add mode
        else if(moduleCode == PERSONNEL_MODULE_CODE && functionType == ADD_MODE){
            vecCustColumnsForModule = getCustColumnForPerson();
        }
        //Added for Coeus 4.3 PT ID - 2388:Person Enhancements -end
        coeusMessageResources = CoeusMessageResources.getInstance();
        
        //Modified for Coeus 4.3 PT ID - 2388:Person Enhancements -start
        //Set the title of the Dialog window as Add Person and Modify Person
        //when in ADD_MODE and MODIFY_MODE resp.
        String title = "Person Details";
        if(moduleCode == PERSONNEL_MODULE_CODE){
            if(functionType == ADD_MODE){
                title = "Add Person";
            }else if(functionType == MODIFY_MODE){
                title = "Modify Person";
            }
        }
        dlgParentComponent = new CoeusDlgWindow(CoeusGuiConstants.getMDIForm(),
                title, true);
        //Modified for Coeus 4.3 PT ID - 2388:Person Enhancements - end
        
        dlgParentComponent.getContentPane().add(createPersonDetails(
                CoeusGuiConstants.getMDIForm()));
        
        //Case #1602 Start 5
        //Modified for Coeus 4.3 PT ID - 2388:Person Enhancements -start
        //To increase the height when the module type is Personnel, as there is
        // six extra components included
        //dlgParentComponent.setSize(675, 300);
        if(moduleCode == PERSONNEL_MODULE_CODE){
            //Modified for Case#2918 - Use of Salary Anniversary Date for calculating inflation in budget development module
            //dlgParentComponent.setSize(675, 375);
            dlgParentComponent.setSize(690, 390);
        }
        //Added for case 3009 -Make Person Name Editable - start
        else if(moduleCode == PROPOSAL_PERSON_MODULE_CODE){
            //Modified for Case#2918 - Use of Salary Anniversary Date for calculating inflation in budget development module
            //dlgParentComponent.setSize(675, 340);
           //Modified for Case :#2918 - Use of Salary Anniversary Date for calculating inflation in budget development module 
            //COEUSQA-1674 - Allow Division Lead Unit to be modified in the person details - Start
            //dlgParentComponent.setSize(690, 360);            
            dlgParentComponent.setSize(690, 390);
            //COEUSQA-1674 - Allow Division Lead Unit to be modified in the person details - End
        }
        //Added for case 3009 -Make Person Name Editable - end
        else{
            //Modified for Case#2918 - Use of Salary Anniversary Date for calculating inflation in budget development module
            //dlgParentComponent.setSize(675, 300);
            dlgParentComponent.setSize(690, 300);
        }
        //Modified for Coeus 4.3 PT ID - 2388:Person Enhancements -end
        //dlgParentComponent.pack();
        //Case #1602 End 5
        
        Dimension screenSize
                = Toolkit.getDefaultToolkit().getScreenSize();
        Dimension dlgSize = dlgParentComponent.getSize();
        dlgParentComponent.setLocation(screenSize.width/2 - (dlgSize.width/2),
                screenSize.height/2 - (dlgSize.height/2));
        dlgParentComponent.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
        dlgParentComponent.addEscapeKeyListener(
                new AbstractAction("escPressed"){
            public void actionPerformed(ActionEvent ae){
                performWindowClosing();
            }
        });
        dlgParentComponent.addWindowListener(new WindowAdapter(){
            public void windowClosing(WindowEvent we){
                performWindowClosing();
                return;
            }
            public void windowOpened(WindowEvent we){
                btnCancel.requestFocusInWindow();
            }
        });
        dlgParentComponent.setResizable(false);
        dlgParentComponent.show();
        
    }
    
    /**
     * This method creates the Buttons, adds listeners to the buttons.
     * @mdiForm a refrence of CoeusAppletMDIForm
     */
    private JComponent createPersonDetails(CoeusAppletMDIForm mdiForm){
        
        this.mdiForm = mdiForm;
        pnlForm = new JPanel();
        pnlForm.setLayout(new BorderLayout(10,10));
        pnlForm.add(createForm(),BorderLayout.CENTER);
        pnlForm.setSize(650,250);
        pnlForm.setLocation(200,200);
        
        JPanel pnlOk = new JPanel();
        JPanel pnlButtons = new JPanel();
        pnlButtons.setLayout(new BorderLayout(10,10));
        JButton btnOk = new JButton();
        btnCancel = new JButton();
        java.awt.GridBagConstraints gridBagConstraints
                = new java.awt.GridBagConstraints();
        
        pnlOk.setLayout(new java.awt.GridBagLayout());
        btnOk.setMnemonic('O');
        btnOk.setText("OK");
        if( moduleCode == DEPARTMENT_PERSON_MODULE_CODE ){
            if((! isOtherMaintained ) || (functionType == DISPLAY_MODE)){
                btnOk.setVisible(false);
            }else{
                btnOk.setEnabled(true);
            }
        }else if(moduleCode == PROPOSAL_PERSON_MODULE_CODE){
            //System.out.println("Here in 1 >>>>>>>>>>>>>>>>>");
            //Code commented and modified for case#3183 - Proposal Hierarchy enhancement
            //for displaying the details according to the mode.
            //if(( maintainPerson ) || (functionType != DISPLAY_MODE)){
            //Modified for COEUSQA-2293 - Citizenship Info field (other/custom data) should be editable in Person details on a proposal - Start
//            if((( maintainPerson ) || (functionType != DISPLAY_MODE)) && !isDisabled()){
//                //System.out.println("Here in 2 >>>>>>>>>>>>>>>>>");
//                btnOk.setEnabled(true);
//            }else{
//                btnOk.setVisible(false);
//            }
            if((! maintainPerson ) || (functionType == DISPLAY_MODE) || isDisabled()){
                btnOk.setVisible(false);
            }else{
                btnOk.setEnabled(true);
            }
            //COEUSQA-2293 : End
        }
        //Added for Coeus 4.3 PT ID - 2388:Person Enhancements -start
        //Didable the ok button when in the dispaly mode
        else if(moduleCode == PERSONNEL_MODULE_CODE){
            if(functionType == DISPLAY_MODE){
                btnOk.setEnabled(false);
            }
        }
        //Added for Coeus 4.3 PT ID - 2388:Person Enhancements -end
         //Case :#3149 - Tabbing between fields does not work on others tabs - Start
        final JButton btnOkPressed = btnOk;
        //Case :#3149 - End
        btnOk.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                //Case :#3149 - Tabbing between fields does not work on others tabs - Start
                int row = 0 ;
                int column =0;
                JTable customTable = null;
                boolean isotherTabSelected = false;
                //Modified for Case :#4180-in proposal person window, double-click and open person details window, nothing happens - Start
                if(moduleCode == PROPOSAL_PERSON_MODULE_CODE && customElementsForm != null && isOtherMaintained){
                    customTable = customElementsForm.getTable();
                    row = customElementsForm.getRow();
                    column = customElementsForm.getColumn();
                }else if(personOtherDetail != null){//Case# :4180 - End
                    customTable = personOtherDetail.getTable();
                    row = personOtherDetail.getRow();
                    column = personOtherDetail.getColumn();
                }
                
                //Case :#3149 - End
               
                try{
                    if(isSaveRequired()){
                        //Added for Coeus 4.3 PT ID - 2388:Person Enhancements -start
                        //To save the person details when the mmodule is Personnel
                        if(moduleCode == PERSONNEL_MODULE_CODE){
                            updatePersonDetails();
                        }//Modified for Coeus 4.3 PT ID - 2388:Person Enhancements -end
                        else if(moduleCode == DEPARTMENT_PERSON_MODULE_CODE){
                            updateOtherDetails();
                        }else if(moduleCode == PROPOSAL_PERSON_MODULE_CODE){
                            //System.out.println("Here in Action Performed PROPOSAL_PERSON_MODULE_CODE");
                            updateProposalPersonDetails();
                        }
                    }
                    String fullName = PersonOrganizationDetail.PERSON_FULLNAME;
                    String personTitle = PersonOrganizationDetail.TITLE;
                    int rowNum = tblPropPerson.getSelectedRow();
                    if(rowNum != -1){
                        /*Bug fix,to avoid the person name getting replaced with the departmental name*/
                        //Added by shiji for fixing bug id :1925 : start
                        //Modified for case #2311 start
                        //For Case #2311 setting value is not required.
                        if(getWindowInstance() instanceof ProposalPersonnelForm){
                            tblPropPerson.setValueAt(fullName, rowNum, 0);
                            tblPropPerson.setValueAt(personTitle, rowNum, 1);
                            tblPropPerson.setValueAt(personId,  rowNum, 2);
                        }
                        //Modified for case #2311 end
                        //bug id : 1925 : end
                        //Bug fix : 2311 - START
                        //commented for avoiding ArrayIndexBounds Exception in Proposal Personnel on click on person details.
//                        tblPropPerson.setValueAt(fullName, rowNum, 1);
//                        tblPropPerson.setValueAt(personTitle, rowNum, 6);
//                        tblPropPerson.setValueAt(personId,  rowNum, 0);
//                        tblPropPerson.setValueAt(personId,  rowNum, 12);
                        //Bug fix : 2311 - END
                    }
                    //Case #1602 Start 16
                    if(validate){
                        dlgParentComponent.dispose();
                        validate = false;
                    }
                    //Case #1602 End16
                    
                }catch(Exception e){
                    e.printStackTrace();
                    CoeusOptionPane.showErrorDialog(e.getMessage());
                    //Case :#3149 - Tabbing between fields does not work on others tabs - Start
                    boolean[] lookUpAvailable = null;
                    boolean editableCustom = false;
                    if(customElementsForm != null && isOtherMaintained){
                        lookUpAvailable = customElementsForm.getLookUpAvailable();
                        customTable = customElementsForm.getTable();
                        row = customElementsForm.getmandatoryRow();
                        column = customElementsForm.getmandatoryColumn();
                        count = 0;
                        editableCustom = true;
                    }else if(personOtherDetail.getOtherTabMandatory() && personOtherDetail != null){
                        lookUpAvailable = personOtherDetail.getLookUpAvailable();
                        customTable = personOtherDetail.getTable();
                        row = personOtherDetail.getMandatoryRow();
                        column = personOtherDetail.getMandatoryColumn();
                        count = 0;
                        editableCustom = true;
                    }
                    if(editableCustom){
                        if(lookUpAvailable[row]){
                            customTable.editCellAt(row,column+1);
                            customTable.setRowSelectionInterval(row,row);
                            customTable.setColumnSelectionInterval(column+1,column+1);
                            
                        }else{
                            customTable.editCellAt(row,column);
                            customTable.setRowSelectionInterval(row,row);
                            customTable.setColumnSelectionInterval(column,column);
                        }
                    }
                
                    //Case :#3149 - End
                    dlgParentComponent.setVisible( true );
                }
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(20, 5, 0, 8);
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTH;
        pnlOk.add(btnOk, gridBagConstraints);
        
        btnCancel.setMnemonic('C');
        btnCancel.setText("Cancel");
        /*
         * This listener is associated with Cancel button to check before
         * closing the window for confirmation of changes done
         */
        btnCancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                performWindowClosing();
                //System.out.println("Cancel button is pressed");
            }
        });
        btnOk.setFont(CoeusFontFactory.getLabelFont());
        btnCancel.setFont(CoeusFontFactory.getLabelFont());
        
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 0, 8);
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTH;
        pnlOk.add(btnCancel, gridBagConstraints);
        pnlOk.setAlignmentX(java.awt.Container.TOP_ALIGNMENT);
        
        pnlButtons.add(pnlOk,BorderLayout.NORTH);
        pnlForm.add(pnlButtons, BorderLayout.EAST);
        this.setLayout(new BorderLayout());
        add(pnlForm);
        btnCancel.requestFocusInWindow();
        return this;
    }
    
    /**
     * This method is used to perform the Window closing operation
     */
    private void performWindowClosing(){
        try{
           
            int option = JOptionPane.NO_OPTION;
            if(functionType != DISPLAY_MODE || isOtherMaintained){
                if(isSaveRequired()){
                    option
                            = CoeusOptionPane.showQuestionDialog(
                            coeusMessageResources.parseMessageKey(
                            "saveConfirmCode.1002"),
                            CoeusOptionPane.OPTION_YES_NO_CANCEL,
                            CoeusOptionPane.DEFAULT_YES);
                }
                if(option == JOptionPane.YES_OPTION){
                    try{
                        if(moduleCode == DEPARTMENT_PERSON_MODULE_CODE){
                            updateOtherDetails();
                        }else if(moduleCode == PROPOSAL_PERSON_MODULE_CODE){
                            updateProposalPersonDetails();
                        }
                        //Added for Coeus 4.3 PT ID - 2388:Person Enhancements -start
                        //Save the person details when the module is Personnel
                        else if(moduleCode == PERSONNEL_MODULE_CODE){
                            updatePersonDetails();
                        }
                        //Added for Coeus 4.3 PT ID - 2388:Person Enhancements - end
                    }catch(Exception e){
                        e.printStackTrace();
                        CoeusOptionPane.showErrorDialog(e.getMessage());
                        dlgParentComponent.setVisible(true);
                    }
                }else if(option == JOptionPane.NO_OPTION){
                    saveRequired = false;
                    dlgParentComponent.dispose();
                    //Case :#3149 - Tabbing between fields does not work on others tabs - Start
                }else if(option == JOptionPane.CANCEL_OPTION){
                    //Modified for Case :#4180-in proposal person window, double-click and open person details window, nothing happens - Start
                    JTable customTable = null ;
                    int row = 0;
                    int column = 0;
                    boolean editableCustom = false;
                    boolean[] lookUpAvailable = null;
                    if(personOtherDetail != null){
                        customTable = personOtherDetail.getTable();
                        row = personOtherDetail.getRow();
                        column = personOtherDetail.getColumn();
                        lookUpAvailable = personOtherDetail.getLookUpAvailable();
                        editableCustom = true;
                    }else if(customElementsForm != null && isOtherMaintained){
                        customTable = customElementsForm.getTable();
                        row = customElementsForm.getRow();
                        column = customElementsForm.getColumn();
                        lookUpAvailable = customElementsForm.getLookUpAvailable();
                        editableCustom = true;
                    }
                    if(editableCustom){//Case:# 4180 - End
                        count = 0;
                        if(tbdPnTabbedPane.getSelectedIndex() == 4){
                            if(column == 3 && !lookUpAvailable[row]){
                                row++;
                            }
                            if(lookUpAvailable[row]){
                                customTable.editCellAt(row,column);
                                customTable.setRowSelectionInterval(row,row);
                                customTable.setColumnSelectionInterval(column,column);
                                
                            }else{
                                customTable.editCellAt(row,column);
                                customTable.setRowSelectionInterval(row,row);
                                customTable.setColumnSelectionInterval(column,column);
                            }
                        }
                    }
                }
                //Case :#3149 -  End
            }else{
                dlgParentComponent.dispose();
            }
        }catch(Exception e ){
            CoeusOptionPane.showInfoDialog(e.getMessage());
            dlgParentComponent.setVisible(true);
        }
    }
    
    /**
     * This method is used to set the Logged in  UserId
     * @param userName a String representing the Loggedin User
     */
    public void setLoginUserName(String userName){
        this.loginUserName = userName;
    }
    
    /**
     * This method is used to get the saveRequired Flag
     * @returns boolean true if changes are made in the form, else false
     */
    public boolean isSaveRequired(){
        try{
            //Bug fix : Save person details if the person has canMaintain rights
            //no need to check for function type Start
            //if( functionType != DISPLAY_MODE ) {
            //Modified for Coeus 4.3 PT ID - 2388:Person Enhancements -start
            //To check for save required while the module type is Personnel and not display mode
            if(maintainPerson ||
                    ((moduleCode == PERSONNEL_MODULE_CODE) && (functionType!=DISPLAY_MODE ))) {
                //Modified for Coeus 4.3 PT ID - 2388:Person Enhancements -end
                //Bug fix : Save person details if the person has canMaintain rights
                //no need to check for function type End
                
                personMiscDetail.getFormData();
                personOrganizationDetail.getFormData();
                personPersonalDetail.getFormData();
                
                //Case #1602 Start 6
                personContactInfoForm.getFormData();
                //Case #1602 End 6
                
                if ( personOrganizationDetail.isSaveRequired()
                || personMiscDetail.isSaveRequired()
                || personPersonalDetail.isSaveRequired()
                //Case #1602 Start 7
                || personContactInfoForm.isSaveRequired()
                //Case #1602 End 7
                ){
                    
                    saveRequired = true;
                    propPersonInfoTabChanged = true;
                }
                //Modified for Coeus 4.3 PT ID - 2388:Person Enhancements -start
                //Included the Personnel module also
                if(moduleCode == DEPARTMENT_PERSON_MODULE_CODE || moduleCode == PERSONNEL_MODULE_CODE){
                    //Modified for Coeus 4.3 PT ID - 2388:Person Enhancements -end
                    if(personOtherDetail.isSaveRequired()){
                        saveRequired = true;
                    }
                }else if(moduleCode == PROPOSAL_PERSON_MODULE_CODE){
                    if(customElementsForm.isSaveRequired()){
                        saveRequired = true;
                    }
                }
            }
        }catch(Exception e){
            e.printStackTrace();
            //saveRequired = true;
        }
        //System.out.println("in isSaveRequired() saveRequired : "+saveRequired);
        return saveRequired;
    }
    
    /**
     * This method updates the Other cost element details into the database.
     */
    private void updateOtherDetails() throws Exception{
        
        if(!isOtherMaintained){
            //Do Nothing
        }else{
            //Validate and get data
            if(!personOtherDetail.validateData()){
                tbdPnTabbedPane.setSelectedIndex(3);
            }else{
                Vector vecDataFromTab = (Vector)personOtherDetail.getOtherColumnElementData();
                String connectTo = CoeusGuiConstants.CONNECTION_URL
                        + PERSON_SERVLET;
                /* connect to the database and get the formData for the
                 * given committee id
                 */
                
                RequesterBean request = new RequesterBean();
                request.setFunctionType(SAVE_RECORD);
                request.setId(personId);
                request.setDataObject(vecDataFromTab);
                AppletServletCommunicator comm = new AppletServletCommunicator(
                        connectTo, request);
                comm.send();
                ResponderBean response = comm.getResponse();
                if (response!=null){
                    if (response.isSuccessfulResponse()){
                        dlgParentComponent.dispose();
                    }
                }
            }
        }
    }
    private void updateProposalPersonDetails() throws Exception{
        
        if(canModifyProposal == 0){
            //Do Nothing
        }else{
            //Validate and get data
            if(!personOrganizationDetail.validateData()){
                tbdPnTabbedPane.setSelectedIndex(0);
            }
            
            //Modified forCoeus4.3 enhancement - PT ID - 2356 -Graduation Year Validation- start
            //To bring the focus back to the tab1 if the user is in some other tab
            //Included the try-catch block
            try{
                if(!personPersonalDetail.validateData()){
                    tbdPnTabbedPane.setSelectedIndex(1);
                }
            }catch(Exception e){
                tbdPnTabbedPane.setSelectedIndex(1);
                throw new Exception(e.getMessage());
            }
            //Modified forCoeus4.3 enhancement - PT ID - 2356 -Graduation Year Validation- end
            //Case #1602 Start 15
            if(!personContactInfoForm.validateData()){
                tbdPnTabbedPane.setSelectedIndex(2);
                validate = false;
                return ;
            }if(!personMiscDetail.validateData()){
                //tbdPnTabbedPane.setSelectedIndex(2);
                tbdPnTabbedPane.setSelectedIndex(3);
            }if (personTrainingInformationForm != null) {
                if(!personTrainingInformationForm.validateData()){
                    //tbdPnTabbedPane.setSelectedIndex(3);
                    tbdPnTabbedPane.setSelectedIndex(4);
                }
            }
            //Modifed for case 3009 - Make perosn name editable - start
            //Do validation for other tab only if its editable
             if(isOtherMaintained && !customElementsForm.validateData()){
                    //tbdPnTabbedPane.setSelectedIndex(4);
                    tbdPnTabbedPane.setSelectedIndex(5);
             }
            //Modifed for case 3009 - Make perosn name editable - end
            //Case #1602 End 15
            
            else{
                Vector vecCustomElementsData = (Vector)customElementsForm.getOtherColumnElementData();
                Vector personDataToServer = new Vector();
                Vector proposalCustomElementsData = null;
                if(vecCustomElementsData != null){
                    proposalCustomElementsData = new Vector();
                    int size = vecCustomElementsData.size();
                    for(int index = 0; index < size; index++){
                        
                        CustomElementsInfoBean cBean = (CustomElementsInfoBean)vecCustomElementsData.elementAt(index);
                        ProposalCustomElementsInfoBean propCBean = new  ProposalCustomElementsInfoBean(cBean);
                        propCBean.setProposalNumber(proposalId);
                        propCBean.setPersonId(personId);
                        proposalCustomElementsData.addElement(propCBean);
                    }
                }
                printBean(proposalCustomElementsData);
                
                ProposalPersonFormBean proposalPersonFormBean = new ProposalPersonFormBean(departmentPersonFormBean);
                proposalPersonFormBean.setProposalNumber(proposalId);
                //Bug fix for case #2059 by tarique start 2
                proposalPersonFormBean.setSortId(getSortId());
                //Bug fix for case #2059 by tarique end 2
                if(propPersonInfoTabChanged){
                    proposalPersonFormBean.setAcType(UPDATE_RECORD);
                }
                proposalPersonFormBean.setPersonId(personId);
                //System.out.println(" 2 ");
                personDataToServer.addElement( proposalPersonFormBean );
                personDataToServer.addElement( proposalCustomElementsData );
                
                String connectTo = CoeusGuiConstants.CONNECTION_URL
                        + "/ProposalMaintenanceServlet";
                /* connect to the database and get the formData for the
                 * given committee id
                 */
                
                RequesterBean request = new RequesterBean();
                request.setFunctionType(SAVE_PROPOSAL_PERSON_RECORD);
                request.setDataObject(personDataToServer);
                AppletServletCommunicator comm = new AppletServletCommunicator(
                        connectTo, request);
                comm.send();
                ResponderBean response = comm.getResponse();
                //System.out.println(" 3 ");
                if (response!=null){
                    if (response.isSuccessfulResponse()){
                        dlgParentComponent.dispose();
                    }
                }
            }
        }
    }
    //Added for Coeus 4.3 PT ID - 2388:Person Enhancements -start
    /**
     * Saves the person details
     * @throws Exception
     */
    private void updatePersonDetails() throws Exception{
        
        try{
            if(!personOrganizationDetail.validateData()){
                tbdPnTabbedPane.setSelectedIndex(0);
            }
        }catch(Exception e){
            tbdPnTabbedPane.setSelectedIndex(0);
            throw new Exception(e.getMessage());
        }
        try{
            if(!personPersonalDetail.validateData()){
                tbdPnTabbedPane.setSelectedIndex(1);
            }
        }catch(Exception e){
            tbdPnTabbedPane.setSelectedIndex(1);
            throw new Exception(e.getMessage());
        }
        
        if(!personContactInfoForm.validateData()){
            tbdPnTabbedPane.setSelectedIndex(2);
            validate = false;
            return ;
        }
        if(!personMiscDetail.validateData()){
            tbdPnTabbedPane.setSelectedIndex(3);
        }
        try{
            if(!personOtherDetail.validateData()){
                tbdPnTabbedPane.setSelectedIndex(4);
            }
        }catch(Exception e){
            tbdPnTabbedPane.setSelectedIndex(4);
            throw new Exception(e.getMessage());
        }
        // Vector vecCustomElementsData = (Vector)customElementsForm.getOtherColumnElementData();
        Vector vecDataFromTab = (Vector)personOtherDetail.getOtherColumnElementData();
        Vector personDataToServer = new Vector();
        PersonCustomElementsInfoBean personCustomBean = null;
        String acType = (functionType == ADD_MODE)? INSERT_RECORD:UPDATE_RECORD;
        if(vecDataFromTab!=null){
            for(int i = 0; i < vecDataFromTab.size(); i++){
                personCustomBean = (PersonCustomElementsInfoBean)vecDataFromTab.get(i);
                personCustomBean.setPersonId(departmentPersonFormBean.getPersonId());
                //Commented for case 3723 - Person Information is not saved. - start
                //acType is correctly set in PersonOtherDetailForm
                //personCustomBean.setAcType(acType);
                //Commented for case 3723 - Person Information is not saved. - start
            }
        }
        
        if(functionType == ADD_MODE){
            departmentPersonFormBean.setAcType(INSERT_RECORD);
        }else if(functionType == MODIFY_MODE){
            departmentPersonFormBean.setAcType(UPDATE_RECORD);
        }
        personDataToServer.addElement( departmentPersonFormBean );
        personDataToServer.addElement( vecDataFromTab );
        
        String connectTo = CoeusGuiConstants.CONNECTION_URL
                + PERSON_SERVLET;
        
        RequesterBean request = new RequesterBean();
        request.setFunctionType(ADD_PERSON_FN_TYPE);
        request.setDataObjects(personDataToServer);
        AppletServletCommunicator comm = new AppletServletCommunicator(
                connectTo, request);
        comm.send();
        ResponderBean response = comm.getResponse();
        if (response!=null){
            if (response.isSuccessfulResponse()){
                dlgParentComponent.dispose();
            }else{
                throw new Exception(response.getMessage());
            }
        }
        
    }
    
    
    /**
     * This method creates the tabbed pane and all the JComponet like Organization, Personal, Misc, Other tabs
     * @mdiForm a refrence of CoeusAppletMDIForm
     */
    public JTabbedPane createForm() {
        
        tbdPnTabbedPane = new CoeusTabbedPane(CoeusTabbedPane.CTRL_T);
        //Case :#3149- Tabbing between fields does not work on others tabs - Start
        tbdPnTabbedPane.addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent e) {
                //Modified for Case#4346 -  Person training information not consistently displayed, saved -Start
                //Checking the Person form is in modify mode
                //if(tbdPnTabbedPane.getSelectedIndex() == 4 && count != 0){
                if(tbdPnTabbedPane.getSelectedIndex() == 4 && count != 0 && functionType == MODIFY_MODE){//Case#4346 - End
                    //Modified for Case :#4180-in proposal person window, double-click and open person details window, nothing happens - Start
                    //Added for COEUSQA-1386 Person custom elements are not being brought forward to proposal person - Start
                    //Focus is not given to the custom fields when proposal person is opened in display mode
                    if(moduleCode == 'P' && functionType == TypeConstants.DISPLAY_MODE){
                        return;
                    }
                    //COEUSQA-1386 : End
                    boolean lookUpAvailable[] = null;
                    JTable customTable = null;
                    int row = 0;
                    int column = 1;
                    boolean editableCustom = false;
                    if(customElementsForm != null && personOtherDetail == null && isOtherMaintained){
                        lookUpAvailable = customElementsForm.getLookUpAvailable();
                        customTable = customElementsForm.getTable();
                        customElementsForm.setTabFocus();
                        editableCustom = true;
                    }else if(personOtherDetail != null ){
                        lookUpAvailable = personOtherDetail.getLookUpAvailable();
                        customTable = personOtherDetail.getTable();
                        personOtherDetail.setTabFocus();
                        editableCustom = true;
                    }
                    if(editableCustom){
                        if(lookUpAvailable[row]){
                            customTable.editCellAt(row,column+1);
                            customTable.setRowSelectionInterval(row,row);
                            customTable.setColumnSelectionInterval(column+1,column+1);
                            
                        }else{
                            customTable.editCellAt(row,column);
                            customTable.setRowSelectionInterval(row,row);
                            customTable.setColumnSelectionInterval(column,column);
                            
                            
                            
                        }
                        count++;
                    }
                }

                tbdPnTabbedPane.addMouseListener(new MouseAdapter(){
                    public void mouseClicked(MouseEvent e){
                        //Modified for Case#4346 -  Person training information not consistently displayed, saved -Start
                        //Checking the Person form is in modify mode
                        //if(tbdPnTabbedPane.getSelectedIndex() == 4 ){
                        if(tbdPnTabbedPane.getSelectedIndex() == 4 && functionType == MODIFY_MODE  ){//Case#4346 - End
                            //Modified for Case :#4180-in proposal person window, double-click and open person details window, nothing happens - Start
                            if(customElementsForm != null && personOtherDetail == null && isOtherMaintained){
                                customElementsForm.setTableFocus();
                            }else if(personOtherDetail != null){
                            //Case :#4180 - End
                                personOtherDetail.setTableFocus();
                            }
                        }
                        
                    }
                });
                
            }
        });
        //Case :#3149 - End
        if(moduleCode == DEPARTMENT_PERSON_MODULE_CODE){
            if(canMainOtherByLoginUser || userCanMaintainSelectedPerson || userCanMaintainUnit){
                isOtherMaintained = true;
            }else{
                isOtherMaintained = false;
                functionType = DISPLAY_MODE;
            }
        }else if(moduleCode == PROPOSAL_PERSON_MODULE_CODE){
            
            if(!maintainPerson){
                functionType = DISPLAY_MODE;
            }
            //Modified for COEUSQA-2293 - Citizenship Info field (other/custom data) should be editable in Person details on a proposal - Start
//            if((canMainOtherByLoginUser || userCanMaintainSelectedPerson || userCanMaintainUnit)
            if((canMainOtherByLoginUser)//COEUSQA-2293 :End
                //Code added for case#3183 - Proposal Hierarchy enhancement
                //for displaying the details according to the mode.
                && !isDisabled()){
                isOtherMaintained = true;
            }
            //Commented for COEUSQA-2293 - Citizenship Info field (other/custom data) should be editable in Person details on a proposal - Start
            //Person function type is used for checking
//            else{
//                isOtherMaintained = false;
//                propOtherFunctionType = DISPLAY_MODE;
//            }
            //COEUSQA-2293 : End
        }
        //Added for Coeus 4.3 PT ID - 2388:Person Enhancements -start
        //Set Other Tab to be editable when module is PERSONNEL and not in DISPLAY_MODE
        else if(moduleCode == PERSONNEL_MODULE_CODE && functionType != DISPLAY_MODE){
            isOtherMaintained = true;
        }
        //Added for Coeus 4.3 PT ID - 2388:Person Enhancements -end
        FlowLayout layout = new FlowLayout(FlowLayout.LEFT);
        
        JPanel pnlOrganization = new JPanel(layout);
        JPanel pnlPersonal = new JPanel(layout);
        JPanel pnlMisc = new JPanel(layout);
        JPanel pnlOther = new JPanel(layout);
        JPanel pnlTraining = new JPanel(layout); //added by nadh
        
        //Case #1602 Start 8
        JPanel pnlContactInfo = new JPanel();
        //pnlContactInfo.setSize(200, 400)
        //Case #1602 End 8
        
        //Added for Coeus 4.3 PT ID - 2388:Person Enhancements -start
        //Initializes the departmentPersonFormBean and set the status of the
        //person to be active initially
        if(functionType == ADD_MODE){
            departmentPersonFormBean = new DepartmentPersonFormBean();
            departmentPersonFormBean.setStatus("A");
        }
        //Added for Coeus 4.3 PT ID - 2388:Person Enhancements -end
        //Added COEUSQA-2293 - Citizenship Info field (other/custom data) should be editable in Person details on a proposal - Start
        JComponent otherTabComponent = null;
        //COEUSQA-2293 : End
        //Modified for Coeus 4.3 PT ID - 2388:Person Enhancements -start
        //included the Personnel module code and changed the argument value passed to
        //the form constructors as moduleCode
        if(moduleCode == DEPARTMENT_PERSON_MODULE_CODE || moduleCode == PERSONNEL_MODULE_CODE){
        	// JM 02-26-2013 check permissions; set editable based on permissions
            canModifyAllFields = false;
            try {
				canModifyAllFields = edu.vanderbilt.coeus.utils.DisplayOptions.canModifyAllFields(loginUserName);
				//System.out.println("canModifyAllFields :: " + canModifyAllFields);
            } catch (CoeusClientException e) {
				System.out.println("Cannot get rights for user " + loginUserName);
			}
            
            personOtherDetail =
                    new PersonOtherDetail(functionType, isOtherMaintained,
                    vecCustColumnsForModule, vecDepartmentOthersFormBean, personId);
            
            // JM 02-26-2013 passing canModifyAllFields to tabs
            personOrganizationDetail =
                    //new PersonOrganizationDetail(functionType, DEPARTMENT_PERSON_MODULE_CODE, isOtherMaintained, departmentPersonFormBean);
            		//new PersonOrganizationDetail(functionType, moduleCode, isOtherMaintained, departmentPersonFormBean);
            		new PersonOrganizationDetail(functionType, moduleCode, isOtherMaintained, departmentPersonFormBean,canModifyAllFields);
            personOrganizationDetail.showOraganization();
            
            personPersonalDetail =
                    //new PersonPersonalDetail(functionType, moduleCode, isOtherMaintained, departmentPersonFormBean,loginUserName);
            		new PersonPersonalDetail(functionType, moduleCode, isOtherMaintained, departmentPersonFormBean,canModifyAllFields);
            //new PersonPersonalDetail(functionType, DEPARTMENT_PERSON_MODULE_CODE, isOtherMaintained, departmentPersonFormBean);
            
            personPersonalDetail.showPersonal();
            
            personMiscDetail =
                    //new PersonMiscDetail(functionType, moduleCode, isOtherMaintained, departmentPersonFormBean);
            		new PersonMiscDetail(functionType, moduleCode, isOtherMaintained, departmentPersonFormBean, canModifyAllFields);
            //new PersonMiscDetail(functionType, DEPARTMENT_PERSON_MODULE_CODE, isOtherMaintained, departmentPersonFormBean);
            personMiscDetail.showMisc();
            
            // added by Nadh for enhancement to show traing info tab in person details form
            //start 2 aug 2004
            if(departmentPersonFormBean!= null && functionType!=ADD_MODE){
                personTrainingInformationForm =
                        new PersonTrainingInformationForm(functionType,moduleCode, departmentPersonFormBean.getFullName(), data);
                //new PersonTrainingInformationForm(functionType,DEPARTMENT_PERSON_MODULE_CODE, departmentPersonFormBean.getFullName(), data);
            }
            //Nadh end 2 aug 2004
            
            //Case #1602 Start 9
            personContactInfoForm =
                    //new PersonContactInfoForm(functionType,moduleCode, isOtherMaintained, departmentPersonFormBean);
                	new PersonContactInfoForm(functionType,moduleCode, isOtherMaintained, departmentPersonFormBean, canModifyAllFields);
            	// JM END
            //new PersonContactInfoForm(functionType,DEPARTMENT_PERSON_MODULE_CODE, isOtherMaintained, departmentPersonFormBean);
            //Modified for Coeus 4.3 PT ID - 2388:Person Enhancements - end
            personContactInfoForm.formatFields();
            //Case #1602 End 9
            //COEUSQA-1674 - Allow Division Lead Unit to be modified in the person details - Start
            personOrganizationDetail.lblDivision.setVisible(false);
            personOrganizationDetail.txtDivision.setVisible(false);
            //COEUSQA-1674 - Allow Division Lead Unit to be modified in the person details - End
            pnlOrganization.add(personOrganizationDetail);
            pnlPersonal.add(personPersonalDetail);
            
            //Case #1602 Start 10
            pnlContactInfo.add(personContactInfoForm);
            //Case #1602 End 10
            
            pnlMisc.add(personMiscDetail);
            pnlOther.add(personOtherDetail);
            //Added for COEUSQA-2293 - Citizenship Info field (other/custom data) should be editable in Person details on a proposal - Start
            otherTabComponent = pnlOther;
            //COEUSQA-2293 : End
            if(personTrainingInformationForm!= null){
                pnlTraining.add(personTrainingInformationForm); //added by nadh
            }
        }else if(moduleCode == PROPOSAL_PERSON_MODULE_CODE){
            
            vecEditableColumns = getEditableColumnNames();
            
            //Code commented and modified for case#3183 - Proposal Hierarchy enhancement
            //for displaying the details according to the mode.
            //if(canModifyProposal == 1){
            //Modified for COEUSDEV-149 :Proposal Persons - Degree Details - Approval In Progress  - Start
            //Proposal person form is gets editable only if the user has MODIFY_PROPOSAL right in the proposal and the proposal
            //is opened in editable mode
//            if(canModifyProposal == 1 && !isDisabled()){
            if(canModifyProposal == 1 && functionType != DISPLAY_MODE){//COEUSDEV-149 : END
                maintainPerson = true;
                canModifyAllFields = true;
               
            }
            //Added for case 3009 - Make person name editable - start
            //Commented for COEUSQA-2293 - Citizenship Info field (other/custom data) should be editable in Person details on a proposal - Start
//            else{
//                maintainPerson = false;
//                 propOtherFunctionType = DISPLAY_MODE;
//            }
            //COEUSQA-2293 : End
            //Added for case 3009 - Make person name editable - end
            
            //Bug Fix: The fields were not disabled in display mode Start
            boolean otherMaintained = false;
            //Code commented and modified for case#3183 - Proposal Hierarchy enhancement
            //for displaying the details according to the mode.
            //if(maintainPerson){
            if(maintainPerson && !isDisabled()){
                otherMaintained = true;
            }
           
            /*customElementsForm =
                    new CustomElementsForm(propOtherFunctionType,  isOtherMaintained, vecCustomElementsData);*/
             //Modified for COEUSDEV-149 :Proposal Persons - Degree Details - Approval In Progress  - Start
//            customElementsForm =
//                    new CustomElementsForm(propOtherFunctionType,  otherMaintained, vecCustomElementsData);
            //COEUSDEV-149 : END
            //Modified for COEUSQA-2293 - Citizenship Info field (other/custom data) should be editable in Person details on a proposal - Start
            //Custom elements will behave same as other tab
            //MODIFY_PROPOSAL right is used for custom elements also, not checking for MAINTAIN_PERSON_INFO right
//            customElementsForm =
//                    new CustomElementsForm(propOtherFunctionType,  maintainPerson, vecCustomElementsData);
            customElementsForm =
                    new CustomElementsForm(functionType,  maintainPerson, vecCustomElementsData);
            //COEUSQA-2293 : End
            //Bug Fix: The fields were not disabled in display mode End
            
            personOrganizationDetail =
                    new PersonOrganizationDetail(functionType, PROPOSAL_PERSON_MODULE_CODE, maintainPerson, departmentPersonFormBean);
            personOrganizationDetail.setVecEditableColumnNames(vecEditableColumns);
            personOrganizationDetail.showOraganization();
            
            personPersonalDetail =
                    new PersonPersonalDetail(functionType, PROPOSAL_PERSON_MODULE_CODE, maintainPerson, departmentPersonFormBean);
            personPersonalDetail.setVecEditableColumnNames(vecEditableColumns);
            personPersonalDetail.showPersonal();
            
            personMiscDetail =
                    new PersonMiscDetail(functionType, PROPOSAL_PERSON_MODULE_CODE, maintainPerson, departmentPersonFormBean);
            personMiscDetail.setVecEditableColumnNames(vecEditableColumns);
            personMiscDetail.showMisc();
            
            //Case #1602 Start 11
            //@todo
            personContactInfoForm =
                    new PersonContactInfoForm(functionType,PROPOSAL_PERSON_MODULE_CODE, maintainPerson, departmentPersonFormBean);
            personContactInfoForm.setVecEditableColumnNames(vecEditableColumns);
            personContactInfoForm.formatFields();
            //Case #1602 End 11
            
            pnlOrganization.add(personOrganizationDetail);
            pnlPersonal.add(personPersonalDetail);
            
            //Case #1602 Start 12
            pnlContactInfo.add(personContactInfoForm);
            //Case #1602 End 12
            pnlMisc.add(personMiscDetail);
            
            pnlOther.add(customElementsForm);
            //Added for COEUSQA-2293 - Citizenship Info field (other/custom data) should be editable in Person details on a proposal - Start
            javax.swing.JScrollPane  jscrPnOther = new javax.swing.JScrollPane();
            jscrPnOther.setViewportView(pnlOther);
            jscrPnOther.setHorizontalScrollBarPolicy(javax.swing.JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
            jscrPnOther.setVerticalScrollBarPolicy(javax.swing.JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
            otherTabComponent = jscrPnOther;
            //COEUSQA-2293 : End
        }
        tbdPnTabbedPane.setFont(CoeusFontFactory.getNormalFont());
        tbdPnTabbedPane.addTab("Organization", pnlOrganization );
        tbdPnTabbedPane.addTab("Personal",  pnlPersonal);
        
        //Case #1602 Start 13
        tbdPnTabbedPane.addTab("Contact Info", pnlContactInfo);
        //Case #1602 End 13
        
        tbdPnTabbedPane.addTab("Misc", pnlMisc);
        //Modified for Coeus 4.3 PT ID - 2388:Person Enhancements -start
        //To show the Training tab in the display mode
        if(moduleCode == DEPARTMENT_PERSON_MODULE_CODE ||
                (moduleCode == PERSONNEL_MODULE_CODE && functionType == DISPLAY_MODE)){
            //Modified for Coeus 4.3 PT ID - 2388:Person Enhancements -end
            tbdPnTabbedPane.addTab("Training", pnlTraining);//Added by Nadh for enhancement to show traing info tab along with other tabs in person details form
        }
        //Modified for COEUSQA-2293 - Citizenship Info field (other/custom data) should be editable in Person details on a proposal - Start
        //For person panel is added, for proposal person JScrollPane is added
//        tbdPnTabbedPane.addTab("Other", jscrPnOther);
        tbdPnTabbedPane.addTab("Other", otherTabComponent);
        //COEUSQA-2293 : End
        tbdPnTabbedPane.setSelectedIndex(0);
        return tbdPnTabbedPane;
    }
    
    private Vector getEditableColumnNames(){
        
        Vector vecDataFromServer = null;
        String connectTo = CoeusGuiConstants.CONNECTION_URL + "/ProposalMaintenanceServlet";
        RequesterBean request = new RequesterBean();
        request.setFunctionType(GET_EDITABLE_COLUMN_NAMES);
        AppletServletCommunicator comm
                = new AppletServletCommunicator(connectTo, request);
        comm.send();
        ResponderBean response = comm.getResponse();
        
        if (response!=null){
            if (response.isSuccessfulResponse()){
                vecDataFromServer = (Vector)response.getDataObject();
            }
        }
        return vecDataFromServer;
    }
    
    private void printBean(Vector vecCustColumnsForModuleVector){
        if(vecCustColumnsForModuleVector != null){
            int size = vecCustColumnsForModuleVector.size();
            for(int index = 0 ; index < size; index ++){
                CustomElementsInfoBean cBean = (CustomElementsInfoBean)vecCustColumnsForModuleVector.elementAt(index);
                if(cBean != null){
                    //System.out.println("ActYpe is "+cBean.getAcType());
                    //System.out.println("getUpdateTimestamp is "+cBean.getUpdateTimestamp());
                }else{
                    //System.out.println("cBean is NULL");
                }
            }
        }
    }
    /**
     * This method gets the organization, personal, misc, other details
     * from the database based on selected person.
     * @param personName a String representing the person for whom the details to be retrieved.
     * @returns DepartmentPersonFormBean contains details of all the 4 tabs.
     */
    private DepartmentPersonFormBean getProposalPersonDetailsFromDB(String personId){
        
        DepartmentPersonFormBean depPersonFormBean = null;
        String connectTo = CoeusGuiConstants.CONNECTION_URL + "/ProposalMaintenanceServlet";
        RequesterBean request = new RequesterBean();
        request.setFunctionType(GET_PROPOSAL_PERSON_DETAILS);
        Vector dataToServer = new Vector();
        dataToServer.addElement(proposalId);
        dataToServer.addElement(personId);
        request.setDataObject(dataToServer);
        AppletServletCommunicator comm
                = new AppletServletCommunicator(connectTo, request);
        comm.send();
        ResponderBean response = comm.getResponse();
        
        if (response!=null){
            if (response.isSuccessfulResponse()){
                Vector serverDataObjects = (Vector)response.getDataObjects();
                if(serverDataObjects != null && !serverDataObjects.isEmpty() && serverDataObjects.size() > 0){
                    //Modified code for case #2059 by tarique start 3
                    ProposalPersonFormBean proposalPersonFormBean = (ProposalPersonFormBean)serverDataObjects.elementAt(0);
                    setSortId(proposalPersonFormBean.getSortId());
                    depPersonFormBean = (DepartmentPersonFormBean)proposalPersonFormBean;
                    //Modified code for case #2059 by tarique end 3
                    vecCustomElementsData = (Vector)serverDataObjects.elementAt(1);
                    //System.out.println("In Client PersonDetailForm AcType is ");
                    //System.out.println("**************************************");
                    printBean(vecCustomElementsData);
                    //Modified for COEUSQA-2293 - Citizenship Info field (other/custom data) should be editable in Person details on a proposal - Start
//                    userCanMaintainUnit = ((Boolean)(serverDataObjects.elementAt(2))).booleanValue();
//                    userCanMaintainSelectedPerson = ((Boolean)(serverDataObjects.elementAt(3))).booleanValue();
                    //COEUSQA-2293 : End
                }
            }
        }
        return depPersonFormBean;
    }
    //Added for Coeus 4.3 PT ID - 2388:Person Enhancements -start
    /**
     * Get the person custom elements
     * @return Vector
     */
    public Vector getCustColumnForPerson(){
        Vector vecCustModulesForPerson = null;
        String connectTo = CoeusGuiConstants.CONNECTION_URL + PERSON_SERVLET;
        RequesterBean request = new RequesterBean();
        request.setFunctionType(GET_PERSON_CUSTOM_ELEMENTS);
        request.setId(personName);
        AppletServletCommunicator comm
                = new AppletServletCommunicator(connectTo, request);
        comm.send();
        ResponderBean response = comm.getResponse();
        
        if (response!=null){
            if (response.isSuccessfulResponse()){
                Vector serverDataObjects = response.getDataObjects();
                vecCustModulesForPerson = (Vector)serverDataObjects.get(0);
            }
        }
        return vecCustModulesForPerson;
    }
    //Added for Coeus 4.3 PT ID - 2388:Person Enhancements -end
    
    /**
     * This method gets the organization, personal, misc, other details
     * from the database based on selected person.
     * @param personName a String representing the person for whom the details to be retrieved.
     * @returns DepartmentPersonFormBean contains details of all the 4 tabs.
     */
    private DepartmentPersonFormBean getPersonDetailsfromDB(String personName){
        
        DepartmentPersonFormBean depPersonFormBean = null;
        String connectTo = CoeusGuiConstants.CONNECTION_URL + PERSON_SERVLET;
        RequesterBean request = new RequesterBean();
        request.setFunctionType(DISPLAY_MODE);
        request.setId(personName);
        AppletServletCommunicator comm
                = new AppletServletCommunicator(connectTo, request);
        comm.send();
        ResponderBean response = comm.getResponse();
        
        if (response!=null){
            if (response.isSuccessfulResponse()){
                Vector serverDataObjects = (Vector)response.getDataObjects();
                personId = (String)response.getId();
                if(serverDataObjects != null && !serverDataObjects.isEmpty() && serverDataObjects.size() > 0){
                    
                    depPersonFormBean = (DepartmentPersonFormBean)serverDataObjects.elementAt(0);
                    userCanMaintainUnit = ((Boolean)(serverDataObjects.elementAt(1))).booleanValue();
                    userCanMaintainSelectedPerson = ((Boolean)(serverDataObjects.elementAt(2))).booleanValue();
                    vecCustColumnsForModule = (Vector)serverDataObjects.elementAt(3);
                    vecDepartmentOthersFormBean = (Vector)serverDataObjects.elementAt(4);
                    //Added by Nadh for enhancement to show traing info tab in person details form
                    //start 2 aug 2004
                    data.addElement((Vector)serverDataObjects.elementAt(5));
                    data.addElement((Vector)serverDataObjects.elementAt(6));
                    //Added for case 3761 - Modify Person right issue - start
                    canModifyPerson = ((Boolean)serverDataObjects.elementAt(7)).booleanValue();
                    //Added for case 3761 - Modify Person right issue - end
                    //nadh end 2 aug 2004
                }
            }
        }
        return depPersonFormBean;
    }
    
    /** Getter for property moduleCode.
     * @return Value of property moduleCode.
     */
    public char getModuleCode() {
        return moduleCode;
    }
    
    /** Setter for property moduleCode.
     * @param moduleCode New value of property moduleCode.
     */
    public void setModuleCode(char moduleCode) {
        this.moduleCode = moduleCode;
    }
    
    /** Getter for property canModifyProposal.
     * @return Value of property canModifyProposal.
     */
    public int getCanModifyProposal() {
        return canModifyProposal;
    }
    
    /** Setter for property canModifyProposal.
     * @param canModifyProposal New value of property canModifyProposal.
     */
    public void setCanModifyProposal(int canModifyProposal) {
        this.canModifyProposal = canModifyProposal;
        //COEUSQA-2647 - START
        if(canModifyProposal == 0){//Don't have rights to Modify Proposal Data
            functionType = TypeConstants.DISPLAY_MODE;
        }
        //COEUSQA-2647 - START
    }
    
    /** Getter for property proposalId.
     * @return Value of property proposalId.
     */
    public java.lang.String getProposalId() {
        return proposalId;
    }
    
    /** Setter for property proposalId.
     * @param proposalId New value of property proposalId.
     */
    public void setProposalId(java.lang.String proposalId) {
        this.proposalId = proposalId;
    }
    
    /** Getter for property personName.
     * @return Value of property personName.
     */
    public java.lang.String getPersonName() {
        return personName;
    }
    
    /** Setter for property personName.
     * @param personName New value of property personName.
     */
    public void setPersonName(java.lang.String personName) {
        this.personName = personName;
    }
    
    /** Getter for property personId.
     * @return Value of property personId.
     */
    public java.lang.String getPersonId() {
        return personId;
    }
    
    /** Setter for property personId.
     * @param personId New value of property personId.
     */
    public void setPersonId(java.lang.String personId) {
        this.personId = personId;
    }
    
    /** Getter for property tblPropPerson.
     * @return Value of property tblPropPerson.
     */
    public javax.swing.JTable getTblPropPerson() {
        return tblPropPerson;
    }
    
    /** Setter for property tblPropPerson.
     * @param tblPropPerson New value of property tblPropPerson.
     */
    public void setTblPropPerson(javax.swing.JTable tblPropPerson) {
        this.tblPropPerson = tblPropPerson;
    }
    
    //Case 1602 Start 14
    /**
     * This method gets the organization, personal, misc, other details
     * from the database based on selected person Id when this window is
     * opened from Person Module(Investigator/KeyStudy)
     * @param personID a String representing the person for whom the details to be retrieved.
     * @returns DepartmentPersonFormBean contains details of all the 4 tabs.
     */
    private DepartmentPersonFormBean getPropPersonDetails(String personID){
        DepartmentPersonFormBean departPersonFormBean = null;
        String connectTo = CoeusGuiConstants.CONNECTION_URL + "/ProposalMaintenanceServlet";
        RequesterBean request = new RequesterBean();
        
        request.setFunctionType(GET_PROP_PERSON_DETAILS_FOR_INV_KEYSTUDY);
        Vector dataToServer = new Vector();
        dataToServer.addElement(proposalId);
        dataToServer.addElement(personID);
        request.setDataObject(dataToServer);
        AppletServletCommunicator comm
                = new AppletServletCommunicator(connectTo, request);
        comm.send();
        ResponderBean response = comm.getResponse();
        
        if (response!=null){
            if (response.isSuccessfulResponse()){
                Vector serverDataObjects = (Vector)response.getDataObjects();
                personId = (String)response.getId();
                if(serverDataObjects != null && !serverDataObjects.isEmpty() && serverDataObjects.size() > 0){
                    
                    departPersonFormBean = (DepartmentPersonFormBean)serverDataObjects.elementAt(0);
                    vecCustColumnsForModule = (Vector)serverDataObjects.elementAt(1);
                    vecDepartmentOthersFormBean = (Vector)serverDataObjects.elementAt(2);
                    data.addElement((Vector)serverDataObjects.elementAt(3));
                    data.addElement((Vector)serverDataObjects.elementAt(4));
                }
            }
        }
        return departPersonFormBean;
    }
    //Bug fix for case #2059 by tarique start 4
    /**
     * Getter for property sortId.
     * @return Value of property sortId.
     */
    public java.lang.Integer getSortId() {
        return sortId;
    }
    
    /**
     * Setter for property sortId.
     * @param sortId New value of property sortId.
     */
    public void setSortId(java.lang.Integer sortId) {
        this.sortId = sortId;
    }
    
    
    //Bug fix for case #2059 by tarique end 4
    //Case 1602 End 14
    //Added for Case #2311 start
    /**
     * Getter for property windowInstance.
     * @return Value of property windowInstance.
     */
    public java.lang.Object getWindowInstance() {
        return windowInstance;
    }
    
    /**
     * Setter for property windowInstance.
     * @param windowInstance New value of property windowInstance.
     */
    public void setWindowInstance(java.lang.Object windowInstance) {
        this.windowInstance = windowInstance;
    }
    //Added for case #2311 end
    
    //Added for Coeus 4.3 PT ID - 2388:Person Enhancements -start
    /**
     * Returns the departmentPersonFormBean
     * @return DepartmentPersonFormBean
     */
    public DepartmentPersonFormBean getDeptPersonFormBean(){
        return departmentPersonFormBean;
    }
    
    /**
     * Returns saveRequired
     * @return boolean
     */
    public boolean getSaveRequired(){
        return saveRequired;
    }
    //Added for Coeus 4.3 PT ID - 2388:Person Enhancements - end
   
    /**
     * Code added for case#3183 - Proposal Hierarchy enhancement
     * Getter for property disabled.
     * @return Value of property disabled.
     */        
    public boolean isDisabled() {
        return disabled;
    }
    
    /**
     * Code added for case#3183 - Proposal Hierarchy enhancement
     * Setter for property disabled.
     * @param disabled New value of property disabled.
     */     
    public void setDisabled(boolean disabled) {
        this.disabled = disabled;
    }
}
