/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.mit.coeus.s2s.controller;

import edu.mit.coeus.exception.CoeusException;
import edu.mit.coeus.exception.CoeusUIException;
import edu.mit.coeus.gui.CoeusAppletMDIForm;
import edu.mit.coeus.gui.CoeusDlgWindow;
import edu.mit.coeus.gui.CoeusFontFactory;
import edu.mit.coeus.gui.event.Controller;
import edu.mit.coeus.utils.CoeusGuiConstants;
import edu.mit.coeus.utils.TypeConstants;
import edu.mit.coeus.utils.CoeusFileChooser;
import edu.mit.coeus.utils.CoeusOptionPane;
import edu.mit.coeus.utils.document.DocumentBean;
import edu.mit.coeus.utils.document.DocumentConstants;

import edu.mit.coeus.brokers.RequesterBean;
import edu.mit.coeus.brokers.ResponderBean;
import edu.mit.coeus.gui.CoeusMessageResources;
import edu.mit.coeus.gui.URLOpener;
import edu.mit.coeus.s2s.bean.UserAttachedS2SFormBean;
import edu.mit.coeus.s2s.bean.UserAttachedS2SFormAttachmentBean;
import edu.mit.coeus.utils.AppletServletCommunicator;

import java.util.List;
import java.util.ArrayList;
import java.util.Vector;
import java.util.HashMap;
import java.util.Map;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.table.JTableHeader;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.ListSelectionModel;
import java.awt.Cursor;
import java.awt.Color;
import java.awt.BorderLayout;
import javax.swing.DefaultCellEditor;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.JLabel;
import javax.swing.filechooser.FileFilter;
import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Timestamp;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.AbstractAction;

import edu.mit.coeus.s2s.gui.UserAttachedS2SForms;
import edu.mit.coeus.utils.DateUtils;
import edu.mit.coeus.utils.S2SConstants;
import edu.mit.coeus.utils.UserUtils;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.event.ListSelectionListener;

/**
 *
 * @author vishal
 */
public class UserAttachedS2SFormsController extends Controller implements ActionListener, ListSelectionListener , MouseListener{

    private UserAttachedS2SForms userAttachedS2SForms;
    private UserAttachedS2SFormsTableModel userAttachedS2SFormsTblModel;
    private UserAttachS2SFormEditor userAttachS2SFormEditor;
    private UserAttachS2SFormFileFilter userAttachS2SFormFileFilter;
    private UserAttachedS2SFormBean userAttachedS2SFormBean;
    private CoeusFileChooser fileChooser;
    private CoeusMessageResources coeusMessageResources;
    private static final String connect = CoeusGuiConstants.CONNECTION_URL + "/S2SServlet";//change
    private static final String streaming = CoeusGuiConstants.CONNECTION_URL + "/StreamingServlet";
    private String proposalNumber;
    private String userId;
    private CoeusAppletMDIForm mdiForm;
    private CoeusDlgWindow userAttachedS2SFormsDlgWindow;
    private List data;
    private List deletedData;
    private List lstFileNames;
    private boolean removing = false;
    private int deletingRow = -1;
    private String TITLE = "User Attached S2S Forms";
    private int width = 840;
    private int height = 150;
    private int detailHeight = 250;
    private int columnWidth[] = {50, 175, 150, 150, 75, 120};
    protected static final String EMPTY_STRING = "";
    public static final int USER_ATTACHD_FORM_NUM_COLUMN = 0;
    public static final int DESCRIPTION_COLUMN = 1;
    public static final int FORM_NAME_COLUMN = 2;
    public static final int FILE_NAME_COLUMN = 3;
    public static final int PDF_COLUMN = 4;
    public static final int XML_COLUMN = 5;
    private static final String ENTER_DESCRIPTION = "userAttachS2S_exceptionCode.1552";
    private static final String ENTER_PDF_FILE = "userAttachS2S_exceptionCode.1553";
    private static final String WANT_TO_SAVE_CHANGES = "userAttachS2S_exceptionCode.1210";
    private static final String SELECT_ROW_TO_DELETE = "userAttachS2S_exceptionCode.1555";
    private static final String DELETE_ROW = "userAttachS2S_exceptionCode.1556";
    private static final String DATE_FORMAT = "dd-MMM-yyyy hh:mm:ss a";
    /*
     * Creates new instance of UserAttachedS2SFormController
     */
    public UserAttachedS2SFormsController() {
        initComponents();
        setFunctionType(TypeConstants.DISPLAY_MODE);
        registerComponents();
    }

    /*
     * Creates new instance of UserAttachedS2SFormController
     * @param functionType
     */
    public UserAttachedS2SFormsController(String proposalNumber, char functionType) {
        this.proposalNumber = proposalNumber;
        initComponents();
        if (userAttachedS2SFormBean == null) {
            userAttachedS2SFormBean = new UserAttachedS2SFormBean();
        }
        userAttachedS2SFormBean.setProposalNumber(proposalNumber);
        try {
            setFormData(new ArrayList());
        } catch (CoeusException ex) {
            Logger.getLogger(UserAttachedS2SFormsController.class.getName()).log(Level.SEVERE, null, ex);
        }
        setFunctionType(functionType);
        registerComponents();
    }

    /**
     * initializes the dialog window
     */
    private void initComponents() {
        coeusMessageResources = CoeusMessageResources.getInstance();
        mdiForm = CoeusGuiConstants.getMDIForm();
        userAttachedS2SForms = new UserAttachedS2SForms();
        userId = mdiForm.getUserId();

        JTableHeader header = userAttachedS2SForms.tblUserS2SForms.getTableHeader();
        header.setFont(CoeusFontFactory.getLabelFont());

        userAttachedS2SFormsTblModel = new UserAttachedS2SFormsTableModel(getFunctionType(), userId);
        userAttachedS2SForms.tblUserS2SForms.setModel(userAttachedS2SFormsTblModel);
        for (int index = 0; index < columnWidth.length; index++) {
            userAttachedS2SForms.tblUserS2SForms.getColumnModel().getColumn(index).setMinWidth(columnWidth[index] / 2);
            userAttachedS2SForms.tblUserS2SForms.getColumnModel().getColumn(index).setPreferredWidth(columnWidth[index]);
        }

        UserAttachedS2SFormRenderer userAttachedS2SFormRenderer = new UserAttachedS2SFormRenderer();
        userAttachedS2SForms.tblUserS2SForms.getColumnModel().getColumn(PDF_COLUMN).setCellRenderer(userAttachedS2SFormRenderer);
        userAttachedS2SForms.tblUserS2SForms.getColumnModel().getColumn(XML_COLUMN).setCellRenderer(userAttachedS2SFormRenderer);

        if (getFunctionType() != TypeConstants.DISPLAY_MODE) {
            userAttachS2SFormEditor = new UserAttachS2SFormEditor();
            userAttachedS2SForms.tblUserS2SForms.getColumnModel().getColumn(DESCRIPTION_COLUMN).setCellEditor(userAttachS2SFormEditor);
            userAttachedS2SForms.tblUserS2SForms.getColumnModel().getColumn(PDF_COLUMN).setCellEditor(userAttachS2SFormEditor);
            userAttachedS2SForms.tblUserS2SForms.getColumnModel().getColumn(XML_COLUMN).setCellEditor(userAttachS2SFormEditor);
        }

        userAttachedS2SForms.tblUserS2SForms.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);

        userAttachedS2SFormsDlgWindow = new CoeusDlgWindow(mdiForm, TITLE, true);
        userAttachedS2SFormsDlgWindow.getContentPane().add(userAttachedS2SForms);
        userAttachedS2SFormsDlgWindow.setSize(width, height + detailHeight);

        userAttachedS2SForms.setPreferredSize(new Dimension(width, height + detailHeight));

        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        userAttachedS2SFormsDlgWindow.setLocation(screenSize.width / 2 - (width / 2), screenSize.height / 2 - ((height + detailHeight) / 2));
    }

    /**
     * registers components with event handlers and enables/disables components depending on function type
     */
    @Override
    public void registerComponents() {
        userAttachedS2SForms.btnCancel.addActionListener(this);
        if (getFunctionType() != TypeConstants.DISPLAY_MODE) {
            userAttachedS2SForms.btnOk.addActionListener(this);
            userAttachedS2SForms.btnAdd.addActionListener(this);
            userAttachedS2SForms.btnDelete.addActionListener(this);
            userAttachedS2SForms.btnUploadPDF.addActionListener(this);
            userAttachedS2SForms.btnTranslate.addActionListener(this);
            userAttachedS2SForms.lstAttachments.addMouseListener(this);
        } else {
            userAttachedS2SForms.btnOk.setEnabled(false);
            userAttachedS2SForms.btnAdd.setEnabled(false);
            userAttachedS2SForms.btnDelete.setEnabled(false);
            userAttachedS2SForms.btnUploadPDF.setEnabled(false);
            userAttachedS2SForms.btnTranslate.setEnabled(false);
            userAttachedS2SForms.lstAttachments.addMouseListener(this);
        }       
        userAttachedS2SForms.btnViewPDF.addActionListener(this);
        userAttachedS2SForms.btnViewXML.addActionListener(this); // disable View XML

        //Make table single selection
        userAttachedS2SForms.tblUserS2SForms.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        userAttachedS2SForms.tblUserS2SForms.getSelectionModel().addListSelectionListener(this);
        userAttachedS2SFormsDlgWindow.addEscapeKeyListener(
                new AbstractAction("escPressed") {

                   // @Override
                    public void actionPerformed(ActionEvent e) {
                        close();
                        return;
                    }
                });

        userAttachedS2SFormsDlgWindow.setDefaultCloseOperation(CoeusDlgWindow.DO_NOTHING_ON_CLOSE);

        userAttachedS2SFormsDlgWindow.addWindowListener(
                new WindowAdapter() {

                    @Override
                    public void windowClosing(WindowEvent we) {
                        close();
                    }
                });

    }

    /*
     * Method to dispose the UserAttachedS2SForm window
     */
    private void close() {
        boolean saveRequired = false;
        String messageKey = null;
        if (getFunctionType() != TypeConstants.DISPLAY_MODE && isSaveRequired()) {
            saveRequired = true;
            messageKey = WANT_TO_SAVE_CHANGES;
        }

        if (saveRequired) {
            int selection = CoeusOptionPane.showQuestionDialog(coeusMessageResources.parseMessageKey(messageKey), CoeusOptionPane.OPTION_YES_NO_CANCEL, CoeusOptionPane.DEFAULT_YES);
            if (selection == CoeusOptionPane.SELECTION_YES) {
                save(true);
            } else if (selection == CoeusOptionPane.SELECTION_NO) {
                userAttachedS2SFormsDlgWindow.dispose();
            }
        } else {
            userAttachedS2SFormsDlgWindow.dispose();
        }

//        userAttachedS2SFormsDlgWindow.dispose();
    }

    /**
     * displays the User Attached S2S Forms Dialog Window
     */
    @Override
    public void display() {
        userAttachedS2SFormsDlgWindow.setVisible(true);
    }

    @Override
    public Component getControlledUI() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void setFormData(Object data) throws CoeusException {
        RequesterBean requesterBean = new RequesterBean();
        requesterBean.setDataObject(userAttachedS2SFormBean);
        requesterBean.setFunctionType(S2SConstants.GET_USER_ATTACHED_S2S_FORM);
        AppletServletCommunicator appletServletCommunicator = new AppletServletCommunicator();
        appletServletCommunicator.setConnectTo(connect);
        appletServletCommunicator.setRequest(requesterBean);
        appletServletCommunicator.send();
        ResponderBean responderBean = appletServletCommunicator.getResponse();

        Vector vecResultData = new Vector();
        if (responderBean.hasResponse()) {
            vecResultData = responderBean.getDataObjects();
            data = vecResultData.get(0);
        }
        userAttachedS2SForms.lblProposalNumber.setText(this.proposalNumber);



        userAttachedS2SForms.lblProposalNumber.setText(this.proposalNumber);
        this.data = (List) data;
        userAttachedS2SFormsTblModel.setData(this.data);

        if (getFunctionType() != TypeConstants.DISPLAY_MODE) {
            userAttachS2SFormEditor.setData(this.data);
        }

        //Select the first Row if exists
        if (this.data != null && this.data.size() > 0) {
            userAttachedS2SForms.tblUserS2SForms.setRowSelectionInterval(0, 0);
        }

    }

    @Override
    public Object getFormData() {
        return data;
    }

    @Override
    public void formatFields() {
    }

    @Override
    public boolean validate() throws CoeusUIException {
        UserAttachedS2SFormBean userAttachS2SFormBean;
        for (int index = 0; index < data.size(); index++) {
            userAttachS2SFormBean = (UserAttachedS2SFormBean) data.get(index);
            validate(userAttachS2SFormBean, index);
        }
        return true;
    }

    private boolean validate(UserAttachedS2SFormBean userAttachS2SFormBean, int index) throws edu.mit.coeus.exception.CoeusUIException {
        if (userAttachS2SFormBean.getDescription() == null || userAttachS2SFormBean.getDescription().trim().equals("")) {
            CoeusUIException coeusUIException = new CoeusUIException(coeusMessageResources.parseMessageKey(ENTER_DESCRIPTION), CoeusUIException.WARNING_MESSAGE);
            coeusUIException.setTabIndex(index);
            throw coeusUIException;
        } else if (userAttachS2SFormBean.getPdfFileName() == null) {
            CoeusUIException coeusUIException = new CoeusUIException(coeusMessageResources.parseMessageKey(ENTER_PDF_FILE), CoeusUIException.WARNING_MESSAGE);
            coeusUIException.setTabIndex(index);
            throw coeusUIException;
        }
        // PDF is not mandatory

        return true;
    }

    @Override
    public void saveFormData() throws CoeusException {
    }

    @Override
    public boolean isSaveRequired() {
        boolean saveRequired = false;
        if (deletedData != null && deletedData.size() > 0) {
            saveRequired = true;
        }
        UserAttachedS2SFormBean userAttachS2SFormBean;
        for (int index = 0; index < data.size(); index++) {
            userAttachS2SFormBean = (UserAttachedS2SFormBean) data.get(index);
            if (userAttachS2SFormBean.getAcType() != null
                    || userAttachS2SFormBean.getPdfAcType() != null) {
                saveRequired = true;
            }
        }
        return saveRequired;
    }

    /**
     * populates the contents of the pdf file before saving
     * @return List of UserAttachedS2SFormBean to be saved
     * @throws java.io.IOException if the PDF file has to be uploaded and cannot be read
     */
    private List getDataToSave() throws IOException {
        if (lstFileNames == null) {
            lstFileNames = new ArrayList();
        } else {
            lstFileNames.clear();
        }

        List lstDataToSave = new ArrayList();
        if (deletedData != null && deletedData.size() > 0) {
            lstDataToSave.addAll(deletedData);
        }
        if (data != null && data.size() > 0) {
            UserAttachedS2SFormBean userAttachS2SFormBean;
            for (int index = 0; index < data.size(); index++) {
                userAttachS2SFormBean = getDataToSave(index);
                lstDataToSave.add(userAttachS2SFormBean);
            }
        }
        return lstDataToSave;
    }

	private UserAttachedS2SFormBean getDataToSave(int selectedIndex)
			throws FileNotFoundException, IOException {
		UserAttachedS2SFormBean userAttachS2SFormBean;
		userAttachS2SFormBean = (UserAttachedS2SFormBean) data.get(selectedIndex);
		if (userAttachS2SFormBean.getPdfAcType() != null) {
		    FileInputStream fileInputStream = new FileInputStream(userAttachS2SFormBean.getPdfFileName());
		    try{
		        byte byteData[] = new byte[fileInputStream.available()];
		        fileInputStream.read(byteData);
		        userAttachS2SFormBean.setUserAttachedS2SPDF(byteData);
		    }finally{
		    	if(fileInputStream!=null) fileInputStream.close();
		    }
	        if (lstFileNames == null) {
	            lstFileNames = new ArrayList();
	        } else {
	            lstFileNames.clear();
	        }
		    lstFileNames.add(userAttachS2SFormBean.getPdfFileName());
		    userAttachS2SFormBean.setPdfFileName(new File(userAttachS2SFormBean.getPdfFileName()).getName());
		}
		return userAttachS2SFormBean;
	}

    /**
     * this method refreshes the display.
     * is called if any error occurs after save.
     * @param lstDataToSave List of UserAttachedS2SFormBean
     * @param list translation comments
     * @param timestamp timestamp of database operations(i.e.  pdf saved)
     * @return string message to be displayed
     */
    private String refresh(List lstDataToSave, List list, Timestamp timestamp) {
        StringBuffer message = new StringBuffer();
        UserAttachedS2SFormBean userAttachS2SFormBean;

        String str = null, fileName;
        for (int index = 0; index < lstDataToSave.size(); index++) {
            userAttachS2SFormBean = (UserAttachedS2SFormBean) lstDataToSave.get(index);

            //No need to update/refresh Deleted beans
            if ((userAttachS2SFormBean.getAcType() != null && userAttachS2SFormBean.getAcType().equals(TypeConstants.DELETE_RECORD))
                    || (userAttachS2SFormBean.getAcType() != null && !userAttachS2SFormBean.getAcType().equals(TypeConstants.INSERT_RECORD) && userAttachS2SFormBean.getPdfAcType() == null)) {
                continue;
            }

            //If Beans are Inserted. update UserAttachedFormNumber and timestamp.
            if (userAttachS2SFormBean.getAcType() != null && userAttachS2SFormBean.getAcType().equals(TypeConstants.INSERT_RECORD)) {
                userAttachS2SFormBean.setAwUserAttachedFormNumber(userAttachS2SFormBean.getUserAttachedFormNumber());
                userAttachS2SFormBean.setUpdateTimestamp(timestamp);
            }

            if (message.length() != 0) {
                message.append("\n\n"); //Append Next Line and an Empty Line
            }
            message.append("User Attached Form Number Num:" + userAttachS2SFormBean.getUserAttachedFormNumber());
            message.append(", File Name:");
            if (userAttachS2SFormBean.getPdfAcType() != null) {
                fileName = lstFileNames.get(0).toString();
                lstFileNames.remove(0); //So that 0th element would be next element
                if (timestamp != null) {
                    userAttachS2SFormBean.setPdfUpdateUser(mdiForm.getUserId());
                    userAttachS2SFormBean.setPdfUpdateTimestamp(timestamp);
                }
            } else {
                fileName = userAttachS2SFormBean.getPdfFileName();
            }
            message.append(fileName);
            message.append("\n" + str);
            userAttachS2SFormBean.setPdfFileName(fileName);

        }//End For

        displayDetails();

        return message.toString();
    }

    //@Override
    public void actionPerformed(ActionEvent actionEvent) {
        try {
            userAttachedS2SFormsDlgWindow.setCursor(new Cursor(Cursor.WAIT_CURSOR));
            blockEvents(true);

            Object source = actionEvent.getSource();
            displayDetails();   
            if (source.equals(userAttachedS2SForms.btnOk)) {
                save(true);
            } else if (source.equals(userAttachedS2SForms.btnCancel)) {
                close();
            } else if (source.equals(userAttachedS2SForms.btnAdd)) {
                addRow();
            } else if (source.equals(userAttachedS2SForms.btnDelete)) {
                int selectedRow = userAttachedS2SForms.tblUserS2SForms.getSelectedRow();
                if (selectedRow == -1) {
                    CoeusOptionPane.showInfoDialog(coeusMessageResources.parseMessageKey(SELECT_ROW_TO_DELETE));
                    return;
                } else {
                    int selection = CoeusOptionPane.showQuestionDialog(coeusMessageResources.parseMessageKey(DELETE_ROW), CoeusOptionPane.OPTION_YES_NO, CoeusOptionPane.DEFAULT_NO);
                    if (selection == CoeusOptionPane.SELECTION_YES) {
                        delete(selectedRow);
                    }
                }
            } else if (source.equals(userAttachedS2SForms.btnUploadPDF)) {
                uploadPdf();
            }else if(source.equals(userAttachedS2SForms.btnTranslate)) {
                translate();                
            } else if (source.equals(userAttachedS2SForms.btnViewPDF)) {
                displayContents(S2SConstants.PDF);
            }else if (source.equals(userAttachedS2SForms.btnViewXML)) {
                displayContents(S2SConstants.XML);
            }
        } catch (Exception exception) {
            CoeusOptionPane.showErrorDialog(exception.getMessage());
        } finally {
            userAttachedS2SFormsDlgWindow.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
            blockEvents(false);
        }
    }
    int lastSelectedRow = -1;
    public void valueChanged(javax.swing.event.ListSelectionEvent listSelectionEvent) {    
        if(!listSelectionEvent.getValueIsAdjusting()) {           
            displayDetails();
            
        }
         if(!removing){              
            int selectedRow = -1;
            selectedRow = userAttachedS2SForms.tblUserS2SForms.getSelectedRow();
            
            if(selectedRow == -1){
                return ;
            }            
            UserAttachedS2SFormBean userAttachedS2SFormBean = (UserAttachedS2SFormBean)data.get(selectedRow);
            boolean enableViewPdf = false;
            if(userAttachedS2SFormBean.getPdfFileName() != null) {
                enableViewPdf = true;
            }
            userAttachedS2SForms.btnViewPDF.setEnabled(enableViewPdf); 
            lastSelectedRow = selectedRow;
        }

    }
    /**
     * Validate and Save the Data to Server
     */
    private void save(boolean closeAfterSave) {
        List lstDataToSave = null;
        try {
            userAttachS2SFormEditor.stopCellEditing();
            int selectedRow = userAttachedS2SForms.tblUserS2SForms.getSelectedRow();
            UserAttachedS2SFormBean userAttachS2SFormBean;
            if (selectedRow != -1) {
                userAttachS2SFormBean = (UserAttachedS2SFormBean) data.get(selectedRow);
                if (userAttachS2SFormBean.getAcType() == null) {
                    userAttachS2SFormBean.setAcType(TypeConstants.UPDATE_RECORD);
                }
            }
            validate();

            lstDataToSave = getDataToSave();

            RequesterBean requesterBean = new RequesterBean();
            requesterBean.setDataObject(lstDataToSave);
            requesterBean.setFunctionType(S2SConstants.SAVE_USER_ATTACHED_S2S_FORM);
            AppletServletCommunicator appletServletCommunicator = new AppletServletCommunicator(connect, requesterBean);
            appletServletCommunicator.send();

            ResponderBean responderBean = appletServletCommunicator.getResponse();
            boolean success = responderBean.hasResponse();
            List list = null;

            if (success) {
                Object object = responderBean.getDataObject();
                list = (List) object;
            }

            if (success && !closeAfterSave) {
                deletedData = null;
                Object object = responderBean.getDataObject();
                if (object instanceof List) {
                    list = (List) object;
//                    sync((List)list.get(0));
                }
                userAttachedS2SFormsTblModel.fireTableDataChanged();
                displayDetails();
                userAttachedS2SForms.tblUserS2SForms.setRowSelectionInterval(selectedRow, selectedRow);
            }

            if (closeAfterSave) {
                userAttachedS2SFormsDlgWindow.dispose();
            }
        } catch (CoeusUIException coeusUIException) {
            userAttachedS2SForms.tblUserS2SForms.setRowSelectionInterval(coeusUIException.getTabIndex(), coeusUIException.getTabIndex());
            CoeusOptionPane.showDialog(coeusUIException);
            //Don't refresh here, since this exception is thrown by validate method
        } catch (Exception exception) {
           //COEUSQA-4110 : Grants.gov User Tool - No error message when not using correct form/package
            if(exception.getMessage() == null){
                CoeusOptionPane.showErrorDialog("The file you uploaded is not Valid. Please upload the appropriate Grants.gov form file.");
            }else{
            CoeusOptionPane.showErrorDialog(exception.getMessage());
            }
           //COEUSQA-4110 : Grants.gov User Tool - No error message when not using correct form/package 
            exception.printStackTrace();
            refresh(lstDataToSave, null, null);
        }
    }
     /**
     * Validate and Translate the Data to Server
     */
    private void translate() {
        List lstDataToSave = new ArrayList();
        
        try {
            userAttachS2SFormEditor.stopCellEditing();
            int selectedRow = userAttachedS2SForms.tblUserS2SForms.getSelectedRow();
            UserAttachedS2SFormBean userAttachS2SFormBean = null;
            if (selectedRow != -1) {
            	System.out.println("Selected row :: " + selectedRow);
            	userAttachS2SFormBean = getDataToSave(selectedRow);
//                userAttachS2SFormBean = (UserAttachedS2SFormBean) data.get(selectedRow);
                if (userAttachS2SFormBean.getAcType() == null) {
                    userAttachS2SFormBean.setAcType(TypeConstants.UPDATE_RECORD);
                }
                if(deletedData!=null && !deletedData.isEmpty()){
                	lstDataToSave.addAll(deletedData);
                }
                lstDataToSave.add(userAttachS2SFormBean);

                // JM 5-2015 added to ignore forms we have coded in house
    		    //System.out.println("PDF file name :: " + userAttachS2SFormBean.getPdfFileName());
                if (userAttachS2SFormBean.getPdfFileName().contains("PlannedReport") ||
                		userAttachS2SFormBean.getPdfFileName().contains("CumulativeInclusion")) {
                    CoeusOptionPane.showErrorDialog("The form that you uploaded is managed through a questionnaire.  Please remove this file and navigate to Grants.gov tab to select this form for inclusion.");            	
                }
                else {
                // JM END
            
		            validate();
		//            lstDataToSave = getDataToSave();
		            RequesterBean requesterBean = new RequesterBean();
		            requesterBean.setDataObject(lstDataToSave);
		            requesterBean.setFunctionType(S2SConstants.TRANSLATE_USER_ATTACHED_S2S_FORM);
		            AppletServletCommunicator appletServletCommunicator = new AppletServletCommunicator(connect, requesterBean);
		            appletServletCommunicator.send();
		
		            ResponderBean responderBean = appletServletCommunicator.getResponse();
		            boolean success = responderBean.hasResponse();
		            List list = null;
		
		            if (success) {
		                Object object = responderBean.getDataObject();
		                list = (List) object;
		            }
		
	                deletedData = null;
	//                Object object = responderBean.getDataObject();
	                if (!list.isEmpty() && list.get(0) instanceof List) {
	//                	list = (List) object;
	                	List savedData = (List)list.get(0);
	                	if(!savedData.isEmpty()){
	                		data.remove(selectedRow);
	                		((ArrayList)data).addAll(selectedRow, savedData);
	                	}
	                	else {
	                		if(lstFileNames!=null && !lstFileNames.isEmpty())
	                			userAttachS2SFormBean.setPdfFileName(lstFileNames.get(0).toString());
	                	}
//                    data=(List)list.get(0);
	                }
	                userAttachedS2SFormsTblModel.setData(this.data);
	                userAttachedS2SFormsTblModel.fireTableDataChanged();
	//                displayDetails();
	                userAttachedS2SForms.tblUserS2SForms.setRowSelectionInterval(selectedRow, selectedRow);
		            
                }
            } // JM 5-2015 - extra brace to close else clause from added if
        } catch (CoeusUIException coeusUIException) {
            userAttachedS2SForms.tblUserS2SForms.setRowSelectionInterval(coeusUIException.getTabIndex(), coeusUIException.getTabIndex());
            CoeusOptionPane.showDialog(coeusUIException);
            //Don't refresh here, since this exception is thrown by validate method
        } catch (Exception exception) {
               if(exception.getMessage() == null){
            	   // JM 5-15-2015 made this message a little nicer
                CoeusOptionPane.showErrorDialog("The file that you uploaded is not a translatable Grants.gov form. Please upload the appropriate Grants.gov form file, or contact Coeus Support.");
            }else{
            CoeusOptionPane.showErrorDialog(exception.getMessage());
             exception.printStackTrace();
            refresh(lstDataToSave, null, null);
               }        
        }
    }
    /**
     * displays details panel and updates the information on the detail panel.
     */
    private void displayDetails(){        
        
        //Set the Comments to previously selected Row
        if(!removing){
            int selectedRow = -1;

            
            //Clear Details Screen before setting values      
            userAttachedS2SForms.pnlPDFLastUpdated.txtLastUpdated.setText(EMPTY_STRING);              
            userAttachedS2SForms.pnlPDFLastUpdated.txtUser.setText(EMPTY_STRING);            
            userAttachedS2SForms.lblNamespaceValue.setText(EMPTY_STRING); 
            userAttachedS2SForms.lstAttachments.setListData(new String[0]);
          
            
            selectedRow = userAttachedS2SForms.tblUserS2SForms.getSelectedRow();
            
            if(selectedRow == -1){
                return ;
            }
            
            UserAttachedS2SFormBean userAttachedS2SFormBean = (UserAttachedS2SFormBean)data.get(selectedRow);
             DateUtils dateUtils = new DateUtils();
           
                              
                /*
                 * UserId to UserName Enhancement - Start
                 * Added to avoid the repetetive database interaction for getting 
                 * the username instead of going to the database everytime mainting the values locally in HashMap
                 */
                //subAwardBudget.pnlSubAwardLastUpdated.txtUser.setText(budgetSubAwardBean.getUpdateUser());
                 
                if(userAttachedS2SFormBean.getUpdateTimestamp() != null){
                    userAttachedS2SForms.pnlPDFLastUpdated.txtLastUpdated.setText(dateUtils.formatDate(userAttachedS2SFormBean.getUpdateTimestamp(), DATE_FORMAT));
                }
                userAttachedS2SForms.pnlPDFLastUpdated.txtUser.setText(UserUtils.getDisplayName(userAttachedS2SFormBean.getUpdateUser()));
                userAttachedS2SForms.lblNamespaceValue.setText(userAttachedS2SFormBean.getNamespace());
           
           
            
            //Set Attachments - START
            List lstAttachments = userAttachedS2SFormBean.getAttachments();
            if(lstAttachments != null && lstAttachments.size() > 0) {
                String arrContentId[] = new String[lstAttachments.size()];
                UserAttachedS2SFormAttachmentBean userAttachedS2SFormAttachmentBean;
                for(int index = 0; index < arrContentId.length; index++){
                    userAttachedS2SFormAttachmentBean = (UserAttachedS2SFormAttachmentBean)lstAttachments.get(index);
                    arrContentId[index] = userAttachedS2SFormAttachmentBean.getContentId();
                }
                userAttachedS2SForms.lstAttachments.setListData(arrContentId);
            }
            //Set Attachments - END
          
            
            boolean enableViewPdf = false;
            if(userAttachedS2SFormBean.getPdfFileName() != null) {
                enableViewPdf = true;
            }
            userAttachedS2SForms.btnViewPDF.setEnabled(enableViewPdf);
            
            
            //Enable/Disable buttons for selected sub award - END
            lastSelectedRow = selectedRow;
        }
    }
    
    /**
     * adds an empty row at the end of the user forms(s)
     */
    private void addRow() {
        UserAttachedS2SFormBean userAttachS2SFormBean = new UserAttachedS2SFormBean();
        userAttachS2SFormBean.setProposalNumber(proposalNumber);
        userAttachS2SFormBean.setAcType(TypeConstants.INSERT_RECORD);
        userAttachS2SFormBean.setUpdateUser(userId);

        int rowIndex = 0;
        if (data == null || data.isEmpty()) {
            userAttachS2SFormBean.setUserAttachedFormNumber(1);
        } else {
            rowIndex = data.size();
            UserAttachedS2SFormBean lastBean = (UserAttachedS2SFormBean) data.get(rowIndex - 1);
            userAttachS2SFormBean.setUserAttachedFormNumber(lastBean.getUserAttachedFormNumber() + 1);
        }

        data.add(userAttachS2SFormBean);
        userAttachedS2SFormsTblModel.fireTableRowsInserted(rowIndex, rowIndex);
        userAttachedS2SForms.tblUserS2SForms.setRowSelectionInterval(rowIndex, rowIndex);

        boolean enableViewPdf = false;
        if (userAttachedS2SFormBean.getPdfFileName() != null) {
            enableViewPdf = true;
        }
        userAttachedS2SForms.btnViewPDF.setEnabled(enableViewPdf);

    }

    /**
     * deletes the selected row
     * @param selectedRow row to be deleted
     */
    private void delete(int selectedRow) {
        removing = true;
        deletingRow = selectedRow;
        deleteRow(selectedRow);

        //Select a Row
        int selectRow = 0;
        int rowCount = userAttachedS2SFormsTblModel.getRowCount();
        if (selectedRow == 0 && rowCount > 0) {
            //Select First Row
            selectRow = 0;
        } else if (selectedRow == rowCount) {
            //Select Last Row
            selectRow = rowCount - 1;
        } else {
            //Select This Row
            selectRow = selectedRow;
        }
        removing = false;
        if (selectRow != -1) {
            userAttachedS2SForms.tblUserS2SForms.setRowSelectionInterval(selectRow, selectRow);
        }else{
            //If All rows Deleted, then Details panel should be cleared
            displayDetails();
        }
        deletingRow = -1;
    }
    private void displayAttachment(UserAttachedS2SFormAttachmentBean userAttachedS2SFormAttachmentBean) {
        try {
            userAttachedS2SFormsDlgWindow.setCursor(new Cursor(Cursor.WAIT_CURSOR));
            blockEvents(true);
            
            DocumentBean documentBean = new DocumentBean();
            Map map = new HashMap();
            map.put("proposalNumber", userAttachedS2SFormAttachmentBean.getProposalNumber());           
            map.put(S2SConstants.USER_ATTACHED_FORM_NUMBER, ""+userAttachedS2SFormAttachmentBean.getUserAttachedFormNumber());
            map.put(S2SConstants.CONTENT_ID, ""+userAttachedS2SFormAttachmentBean.getContentId());
            map.put(S2SConstants.FILE, S2SConstants.ATTACHMENT);
            map.put(DocumentConstants.READER_CLASS, "edu.mit.coeus.s2s.formattachment.UserAttachmentS2SDocReader");
            documentBean.setParameterMap(map);
            
            RequesterBean requesterBean = new RequesterBean();
            requesterBean.setDataObject(documentBean);
            requesterBean.setFunctionType(DocumentConstants.GENERATE_STREAM_URL);
            
            AppletServletCommunicator appletServletCommunicator = new AppletServletCommunicator();
            appletServletCommunicator.setConnectTo(streaming);
            appletServletCommunicator.setRequest(requesterBean);
            appletServletCommunicator.send();
            ResponderBean responderBean = appletServletCommunicator.getResponse();
            
            Object data = null;
            if(responderBean.hasResponse()) {
                data = responderBean.getDataObject();
                if(data != null && data instanceof Map) {
                   map = (Map)data;
                   String url = (String)map.get(DocumentConstants.DOCUMENT_URL);
                   URL urlObj = new URL(url);
                   URLOpener.openUrl(urlObj);
                }
            }
            
            //URLOpener.openUrl(((JTextField)source).getText());
            
        } catch (Exception coeusException) {
            CoeusOptionPane.showErrorDialog(coeusException.getMessage() == null ? "Error Occured" : coeusException.getMessage());
        }finally{
            userAttachedS2SFormsDlgWindow.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
            blockEvents(false);
        }
    }

    public void mouseClicked(MouseEvent mouseEvent) {
        if (mouseEvent.getClickCount() == 2) {
            //Display Attachment in browser
            int selectedIndex;
            selectedIndex = userAttachedS2SForms.tblUserS2SForms.getSelectedRow();
            UserAttachedS2SFormBean userAttachedS2SFormBean = (UserAttachedS2SFormBean) data.get(selectedIndex);
            selectedIndex = userAttachedS2SForms.lstAttachments.getSelectedIndex();
            UserAttachedS2SFormAttachmentBean userAttachedS2SFormAttachmentBean = (UserAttachedS2SFormAttachmentBean) userAttachedS2SFormBean.getAttachments().get(selectedIndex);
            displayAttachment(userAttachedS2SFormAttachmentBean);
        }
    }

    public void mousePressed(MouseEvent e) {
    }

    public void mouseReleased(MouseEvent e) {
    }

    public void mouseEntered(MouseEvent e) {
    }

    public void mouseExited(MouseEvent e) {
    }
    /*
     * Method upload the pdf file
     */
    private void uploadPdf() {
        int selectedRow = userAttachedS2SForms.tblUserS2SForms.getSelectedRow();
        if (selectedRow == -1) {
            CoeusOptionPane.showInfoDialog("Select a Row");
            return;
        }
        String fileName = displayPDFFileDialog();
        if (fileName == null) {
            //Cancelled
            return;
        }
        UserAttachedS2SFormBean userAttachS2SFormBean = (UserAttachedS2SFormBean) data.get(selectedRow);
        userAttachS2SFormBean.setPdfFileName(fileName);
        userAttachS2SFormBean.setPdfAcType(TypeConstants.UPDATE_RECORD);
        userAttachedS2SForms.btnTranslate.setEnabled(true);
        userAttachedS2SFormsTblModel.fireTableRowsUpdated(selectedRow, selectedRow);
    }

    /**
     * returns an selected PDF file.
     * returns null if user cancels the file selection.
     */
    private String displayPDFFileDialog() {
        String fileName = null;
        if (fileChooser == null) {
            fileChooser = new CoeusFileChooser(CoeusGuiConstants.getMDIForm());
            if (userAttachS2SFormFileFilter == null) {
                userAttachS2SFormFileFilter = new UserAttachS2SFormFileFilter();
            }
            fileChooser.setFileFilter(userAttachS2SFormFileFilter);
            fileChooser.setAcceptAllFileFilterUsed(false);
        }

        fileChooser.showFileChooser();
        if (fileChooser.isFileSelected()) {
            File file = fileChooser.getFileName();
            fileName = file.getAbsolutePath();
        }

        return fileName;
    }

    /**
     * deletes the selected row.
     * @param selectedRow row to be deleted.
     */
    private void deleteRow(int selectedRow) {
        UserAttachedS2SFormBean userAttachS2SFormBean = (UserAttachedS2SFormBean) data.get(selectedRow);
        if (userAttachS2SFormBean.getAcType() == null || userAttachS2SFormBean.getAcType().equals(TypeConstants.UPDATE_RECORD)) {
            userAttachS2SFormBean.setAcType(TypeConstants.DELETE_RECORD);
            userAttachS2SFormBean.setUpdateUser(userId);
            if (deletedData == null) {
                deletedData = new ArrayList();
            }
            deletedData.add(userAttachS2SFormBean);
        }
        data.remove(selectedRow);

        userAttachedS2SFormsTblModel.fireTableRowsDeleted(selectedRow, selectedRow);
    }//End Method Delete Row

    private void displayContents(String file) throws CoeusException, MalformedURLException {
        int selectedRow = userAttachedS2SForms.tblUserS2SForms.getSelectedRow();
        if (selectedRow == -1) {
            CoeusOptionPane.showInfoDialog("Select a Row");
            return;
        }
        UserAttachedS2SFormBean userAttachFormBean = (UserAttachedS2SFormBean) this.data.get(selectedRow);
        if (file.equals(S2SConstants.PDF) && userAttachFormBean.getPdfAcType() != null) {
            //PDF Updated. open file from local filesystem
            File fileObj = new File(userAttachFormBean.getPdfFileName());
            URL url = fileObj.toURL();
            URLOpener.openUrl(url);
        } else {
            displayContents(file, userAttachFormBean);
        }
    }

    /**
     * displays the contents of the PDF file
     * @param content specified PDF
     * @param userAttachFormBean UserAttachedS2SFormBean
     */
    private void displayContents(String file, UserAttachedS2SFormBean userAttachFormBean) {
        try {
            userAttachedS2SFormsDlgWindow.setCursor(new Cursor(Cursor.WAIT_CURSOR));
            blockEvents(true);

            DocumentBean documentBean = new DocumentBean();
            Map map = new HashMap();
            map.put("proposalNumber", userAttachFormBean.getProposalNumber());
            map.put(S2SConstants.USER_ATTACHED_FORM_NUMBER, "" + userAttachFormBean.getUserAttachedFormNumber());
            map.put(S2SConstants.FILE, file);
            map.put(S2SConstants.NAMESPACE,userAttachFormBean.getNamespace());
            map.put(DocumentConstants.READER_CLASS, "edu.mit.coeus.s2s.formattachment.UserAttachmentS2SDocReader");
            documentBean.setParameterMap(map);

            RequesterBean requesterBean = new RequesterBean();
            requesterBean.setDataObject(documentBean);
            requesterBean.setFunctionType(DocumentConstants.GENERATE_STREAM_URL);

            AppletServletCommunicator appletServletCommunicator = new AppletServletCommunicator();
            appletServletCommunicator.setConnectTo(streaming);
            appletServletCommunicator.setRequest(requesterBean);
            appletServletCommunicator.send();
            ResponderBean responderBean = appletServletCommunicator.getResponse();

            Object data = null;
            if (responderBean.hasResponse()) {
                data = responderBean.getDataObject();
                if (data != null && data instanceof Map) {
                    map = (Map) data;
                    String url = (String) map.get(DocumentConstants.DOCUMENT_URL);
                    URL urlObj = new URL(url);
                    URLOpener.openUrl(urlObj);
                }
            }
        } catch (Exception coeusException) {
            CoeusOptionPane.showErrorDialog(coeusException.getMessage() == null ? "Error Occured" : coeusException.getMessage());
        } finally {
            userAttachedS2SFormsDlgWindow.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
            blockEvents(false);
        }
    }

    class UserAttachS2SFormEditor extends DefaultCellEditor implements ActionListener {

        private List data;
        private JPanel pnlEditor;
        private JTextField txtFile;
        private JButton btnBrowse;
        private JButton btnDisplayPDF;
        private JButton btnDisplayXML;
        private int editingColumn;
        private int editingRow;
        private String fileName;

        public UserAttachS2SFormEditor() {
            super(new JTextField());
            pnlEditor = new JPanel();
            pnlEditor.setLayout(new BorderLayout(0, 0));
            JPanel pnlButtons = new JPanel();
            pnlButtons.setLayout(new BorderLayout(0, 0));

            txtFile = new JTextField();
            txtFile.setEditable(false);
            txtFile.setBackground(Color.white);
            btnBrowse = new JButton("...");
            btnBrowse.setToolTipText("Browse");
            btnBrowse.setPreferredSize(new Dimension(15, btnBrowse.getHeight()));
            btnDisplayPDF = new JButton(new ImageIcon(getClass().getClassLoader().getResource(CoeusGuiConstants.DISPLAY_ICON)));
            btnDisplayPDF.setToolTipText("Display PDF");
            btnDisplayPDF.setPreferredSize(new Dimension(15, btnDisplayPDF.getHeight()));
            pnlEditor.add(txtFile, BorderLayout.CENTER);

            pnlButtons.add(btnBrowse, BorderLayout.WEST);
            pnlButtons.add(btnDisplayPDF, BorderLayout.EAST);

            pnlEditor.add(pnlButtons, BorderLayout.EAST);

            btnDisplayXML = new JButton("View XML", new ImageIcon(getClass().getClassLoader().getResource(CoeusGuiConstants.DISPLAY_ICON)));
            btnDisplayXML.setToolTipText("Display XML");

            btnBrowse.addActionListener(this);
            btnDisplayPDF.addActionListener(this);
            btnDisplayXML.addActionListener(this);
        }

        public void setData(List data) {
            this.data = data;
        }

        @Override
        public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
            Component retValue;
            editingColumn = column;
            editingRow = row;
            if (column == UserAttachedS2SFormsController.PDF_COLUMN) {
                fileName = null;
                UserAttachedS2SFormBean userAttachFormBean = (UserAttachedS2SFormBean) data.get(row);
                txtFile.setText(userAttachFormBean.getPdfFileName());
                return pnlEditor;
            }
            retValue = super.getTableCellEditorComponent(table, value, isSelected, row, column);
            return retValue;
        }

        @Override
        public int getClickCountToStart() {
            return 1;
        }

        //@Override
        public void actionPerformed(java.awt.event.ActionEvent actionEvent) {
            Object source = actionEvent.getSource();
            if (source.equals(btnBrowse)) {
            } else if (source.equals(btnDisplayPDF)) {
                UserAttachedS2SFormBean userAttachFormBean = (UserAttachedS2SFormBean) data.get(editingRow);
                displayContents(S2SConstants.PDF, userAttachFormBean);
            } else if (source.equals(btnDisplayXML)) {
                UserAttachedS2SFormBean userAttachFormBean = (UserAttachedS2SFormBean) data.get(editingRow);
                displayContents(S2SConstants.XML, userAttachFormBean);
            }
        }

        @Override
        public Object getCellEditorValue() {
            Object retValue;
            if (editingColumn == UserAttachedS2SFormsController.PDF_COLUMN) {
                return fileName;
            }
            retValue = super.getCellEditorValue();
            return retValue;
        }
    }//End UserAttachS2SFormEditor
} //End UserAttachedS2SFormsController

class UserAttachedS2SFormsTableModel extends DefaultTableModel {

    private char mode;
    private String userId;
    private List data;
    private String columnNames[] = {"No", "Description", "Form Name", "File name", "PDF", "XML"};

    public UserAttachedS2SFormsTableModel(char mode, String userId) {
        this.mode = mode;
        this.userId = userId;
    }

    @Override
    public void setValueAt(Object value, int rowIndex, int columnIndex) {
        UserAttachedS2SFormBean userAttacheds2sFormBean = (UserAttachedS2SFormBean) data.get(rowIndex);
        switch (columnIndex) {
            case UserAttachedS2SFormsController.DESCRIPTION_COLUMN:
                if (userAttacheds2sFormBean.getDescription() != null && userAttacheds2sFormBean.getDescription().equals(value.toString())) {
                    return;
                }
                userAttacheds2sFormBean.setDescription(value.toString());
                if (userAttacheds2sFormBean.getAcType() == null) {
                    userAttacheds2sFormBean.setAcType(TypeConstants.UPDATE_RECORD);
                }
                userAttacheds2sFormBean.setUpdateUser(userId);
                break;
            case UserAttachedS2SFormsController.PDF_COLUMN:
                if (value == null) {
                    return;
                }
                userAttacheds2sFormBean.setPdfFileName(value.toString());
                if (userAttacheds2sFormBean.getPdfAcType() == null) {
                    userAttacheds2sFormBean.setPdfAcType(TypeConstants.UPDATE_RECORD);
                }
                userAttacheds2sFormBean.setPdfUpdateUser(userId);

                break;
        }
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        UserAttachedS2SFormBean userAttacheds2sFormBean = (UserAttachedS2SFormBean) data.get(rowIndex);

        switch (columnIndex) {
            case UserAttachedS2SFormsController.USER_ATTACHD_FORM_NUM_COLUMN:
                return "" + (rowIndex + 1);
            case UserAttachedS2SFormsController.DESCRIPTION_COLUMN:
                return userAttacheds2sFormBean.getDescription();
            case UserAttachedS2SFormsController.FORM_NAME_COLUMN:
                return userAttacheds2sFormBean.getFormName();
            case UserAttachedS2SFormsController.FILE_NAME_COLUMN:
                return userAttacheds2sFormBean.getPdfFileName();
            case UserAttachedS2SFormsController.PDF_COLUMN:
                return new Boolean(userAttacheds2sFormBean.getPdfFileName() != null);
            case UserAttachedS2SFormsController.XML_COLUMN:
                return new Boolean(userAttacheds2sFormBean.getFormName() != null);
        }

        return null;
    }

    @Override
    public int getRowCount() {
        if (data == null) {
            return 0;
        }
        return data.size();
    }

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        if (mode == TypeConstants.DISPLAY_MODE) {
            return false;
        }

        if (columnIndex == UserAttachedS2SFormsController.DESCRIPTION_COLUMN) {
            return true;
        }
        return false;
    }

    @Override
    public String getColumnName(int columnIndex) {
        return columnNames[columnIndex];
    }

    @Override
    public Class getColumnClass(int columnIndex) {
        return String.class;
    }

    @Override
    public int getColumnCount() {
        return columnNames.length;
    }

    /**
     * Getter for property data.
     * @return Value of property data.
     */
    public java.util.List getData() {
        return data;
    }

    /**
     * Setter for property data.
     * @param data New value of property data.
     */
    public void setData(java.util.List data) {
        this.data = data;
    }
} //End UserAttachedS2SFormsControllerTableModel

class UserAttachedS2SFormRenderer extends DefaultTableCellRenderer {

    private JLabel lblSaved, lblNotSaved;

    public UserAttachedS2SFormRenderer() {
        ImageIcon savedImageIcon = new ImageIcon(getClass().getClassLoader().getResource(CoeusGuiConstants.COMPLETE_ICON));
        ImageIcon notSavedImageIcon = new ImageIcon(getClass().getClassLoader().getResource(CoeusGuiConstants.NONE_ICON));
        lblSaved = new JLabel(savedImageIcon);
        lblNotSaved = new JLabel(notSavedImageIcon);
    }

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        if (column == UserAttachedS2SFormsController.PDF_COLUMN && value instanceof Boolean) {
            Boolean bool = (Boolean) value;
            if (bool.booleanValue() == true) {
                return lblSaved;
            } else {
                lblNotSaved.setText(UserAttachedS2SFormsController.EMPTY_STRING);
                return lblNotSaved;
            }
        } else if (column == UserAttachedS2SFormsController.XML_COLUMN && value instanceof UserAttachedS2SFormBean) {
            lblNotSaved.setText("Not Generated");
            return lblNotSaved;
        } else if (column == UserAttachedS2SFormsController.XML_COLUMN && value instanceof Boolean) {
            Boolean bool = (Boolean) value;
            if (bool.booleanValue() == true) {
                return lblSaved;
            } else {
                lblNotSaved.setText(UserAttachedS2SFormsController.EMPTY_STRING);
                return lblNotSaved;
            }
        }
        return super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
    }
}//End UserAttachedS2SFormRenderer

class UserAttachS2SFormFileFilter extends FileFilter {

    @Override
    public boolean accept(File file) {
        if (file.getName().endsWith("pdf") || file.isDirectory()) {
            return true;
        }
        return false;
    }

    @Override
    public String getDescription() {
        return "PDF Files";
    }
} //End UserAttachS2SFormFileFilter
