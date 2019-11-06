/** Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 */

package edu.mit.coeus.negotiation.gui;

import edu.mit.coeus.brokers.RequesterBean;
import edu.mit.coeus.brokers.ResponderBean;
import edu.mit.coeus.negotiation.bean.NegotiationAttachmentBean;
import edu.mit.coeus.utils.CoeusVector;
import edu.mit.coeus.utils.UserUtils;
import edu.mit.coeus.utils.documenttype.CoeusDocumentUtils;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseListener;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import java.awt.Color;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import edu.mit.coeus.exception.CoeusException;
import edu.mit.coeus.negotiation.bean.NegotiationActivitiesBean;
import edu.mit.coeus.gui.CoeusFontFactory;
import edu.mit.coeus.gui.URLOpener;
import edu.mit.coeus.utils.AppletServletCommunicator;
import edu.mit.coeus.utils.CoeusGuiConstants;
import edu.mit.coeus.utils.CoeusOptionPane;
import edu.mit.coeus.utils.DateUtils;
import edu.mit.coeus.utils.TypeConstants;
import edu.mit.coeus.utils.document.DocumentBean;
import edu.mit.coeus.utils.document.DocumentConstants;
import edu.mit.coeus.utils.query.QueryEngine;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

/**
 * ActivityDetailsPanel.java
 * Created on July 13, 2004, 10:31 AM
 * @author  Vyjayanthi
 */
public class ActivityDetailsPanel extends javax.swing.JPanel 
implements ActionListener, MouseListener {
    
    /** Set if restricted view is visible, false otherwise */
    private boolean restrictedViewVisible;
    
    /** Holds the row number in the order of display
     * required to get the selected activity
     */
    private int rowIndex = 0;

    /** Holds the selected activity
     */
    public static int selectedRow = 0;
    
    /** Holds an instance of <CODE>NegotiationActivitiesBean</CODE>
     */
    private NegotiationActivitiesBean negotiationActivitiesBean;
    
    private DateUtils dateUtils;
    private String queryKey;
    private char functionType;
    private static final String REQUIRED_DATEFORMAT = "MM/dd/yyyy";
    private static final String NEGOTIATION = "Negotiation";
    //New local variables added with case 2806 - Upload attachments to Negotiation module - Start
    private static final String STREAMING_SERVLET = CoeusGuiConstants.CONNECTION_URL + "/StreamingServlet";
    private static final String NEGOTIATION_SERVLET = CoeusGuiConstants.CONNECTION_URL + "/NegotiationMaintenanceServlet";
    private static final char GET_BLOB_DATA_FOR_NEGOTIATION = 'y';
     private char parentFunctionType;
     NegotiationAttachmentBean attachmentBean;
    //New local variables added with case 2806 - Upload attachments to Negotiation module - End
    /** Creates new form ActivityDetailsPanel */
    public ActivityDetailsPanel() {
        initComponents();
        dateUtils = new DateUtils();
    }
    
    /** Creates new form ActivityDetailsPanel
     * @param negotiationActivitiesBean to set the form data
     * @param functionType holds the function type to disable the check box in Display mode
     */
    public ActivityDetailsPanel(NegotiationActivitiesBean negotiationActivitiesBean, 
    char functionType ) {
        this();
        this.negotiationActivitiesBean = negotiationActivitiesBean;
        this.functionType = functionType;
        queryKey = NEGOTIATION + negotiationActivitiesBean.getNegotiationNumber();
        setFormData();
    }
    
    /** Sets the form data
     */
    private void setFormData(){
        if( negotiationActivitiesBean == null ) return ;
        if( negotiationActivitiesBean.getActivityDate() != null ){
            lblActivityDateVal.setText( dateUtils.formatDate(
            negotiationActivitiesBean.getActivityDate().toString(), REQUIRED_DATEFORMAT) );
        }        
        lblActivityTypeVal.setText(negotiationActivitiesBean.getActivityTypeDescription());
        if( negotiationActivitiesBean.getCreateDate() != null ){
            lblCreateDateVal.setText( dateUtils.formatDate(
            negotiationActivitiesBean.getCreateDate().toString(), REQUIRED_DATEFORMAT) );
        }
        if( negotiationActivitiesBean.getFollowUpDate() != null ){
            lblFollowupDateVal.setText( dateUtils.formatDate(
            negotiationActivitiesBean.getFollowUpDate().toString(), REQUIRED_DATEFORMAT) );
        }
        lblLastUpdateByVal.setText(negotiationActivitiesBean.getLastUpdatedBy());
        if( negotiationActivitiesBean.getUpdateTimestamp() != null ){
            java.util.Date date = (java.util.Date)negotiationActivitiesBean.getUpdateTimestamp();
            lblLastUpdateVal.setText(dateUtils.formatDate(date.toString(), REQUIRED_DATEFORMAT));
        }
        chkOspOnly.setSelected(negotiationActivitiesBean.isRestrictedView());
        txtArDescription.setText(negotiationActivitiesBean.getDescription());
        txtArDescription.setCaretPosition(0);
        chkOspOnly.addActionListener(this);
        txtArDescription.addMouseListener(this);
        addMouseListener(this);
        //Added for case 2806 - Upload attachments to neg module - Start
        btnIcon.addActionListener(this);
        attachmentBean = negotiationActivitiesBean.getAttachmentBean();
        //Modified for COEUSDEV-294 :  Error adding activity to a negotiation - Start
//        if(attachmentBean!=null){
        //When no attachment is available attachment icon is not displayed
        if(attachmentBean!=null && (attachmentBean.getFileName() != null && !attachmentBean.getFileName().equals(""))){
        //COEUSDEV-294 : End
            CoeusDocumentUtils documentTypes = CoeusDocumentUtils.getInstance();
            ImageIcon icon = documentTypes.getAttachmentIcon(attachmentBean);
            btnIcon.setToolTipText(attachmentBean.getFileName());
            btnIcon.setIcon(icon);
            btnIcon.setDisabledIcon(icon);
            
        }else{
            btnIcon.setVisible(false);
        }
        //Added for case 2806 - Upload attachments to neg module - End
    }
    
    /** This method triggers all actions based on the event occured
     * @param actionEvent takes the actionEvent */
    public void actionPerformed(ActionEvent actionEvent) {
        if( actionEvent.getSource().equals(chkOspOnly) ){
            negotiationActivitiesBean.setRestrictedView(chkOspOnly.isSelected());
            if(negotiationActivitiesBean.getAcType() == null){
                negotiationActivitiesBean.setAcType(TypeConstants.UPDATE_RECORD);
            }
            
            //Update the changes to query engine
            try{
                QueryEngine.getInstance().update(queryKey, negotiationActivitiesBean);
            }catch (CoeusException coeusException){
                coeusException.printStackTrace();
            }
            
            //Sets the vertical scrollbar to indicate the selection
            scrPnDescription.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
            txtArDescription.setForeground(Color.BLUE);
            lblIcon.setIcon(new ImageIcon(getClass().getClassLoader().getResource(CoeusGuiConstants.NEGOTIATION_FOLDMARK_ICON)));
            selectedRow = getRowIndex();
        }
        //Added for case 2806 - Upload attachments to neg module - Start
        else if(actionEvent.getSource().equals(btnIcon)){
            if(negotiationActivitiesBean!=null && attachmentBean!=null){
                performViewAction();
            }
        }
        //Added for case 2806 - Upload attachments to neg module -End
    }
    
    public void mouseClicked(java.awt.event.MouseEvent mouseEvent) {
        //Sets the vertical scrollbar to indicate the selection
        scrPnDescription.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        txtArDescription.setForeground(Color.BLUE);
        lblIcon.setIcon(new ImageIcon(getClass().getClassLoader().getResource(CoeusGuiConstants.NEGOTIATION_FOLDMARK_ICON)));
        selectedRow = getRowIndex();
    }
    //Added for case 2806 - Upload attachments to neg module - Start
    private void performViewAction(){
        
        if(canViewAttachment()){
            RequesterBean request = new RequesterBean();
            Vector dataObjects = new Vector();
 
            if(negotiationActivitiesBean!=null 
                    && attachmentBean!=null
                        && attachmentBean.getFileBytes() == null){ 
                //this means that there is a blob in db but it is not fetched right now
                attachmentBean.setFileBytes(getBlobDataForNegActivity().getFileBytes());
            }
            dataObjects.add(0, attachmentBean);
            dataObjects.add(1,CoeusGuiConstants.getMDIForm().getUnitNumber());
            DocumentBean documentBean = new DocumentBean();
            Map map = new HashMap();
            map.put("DATA", dataObjects);
            map.put("USER_ID", CoeusGuiConstants.getMDIForm().getUserId());
            map.put(DocumentConstants.DOC_ON_URL_GENERATION, new Boolean(true));
            map.put(DocumentConstants.READER_CLASS, "edu.mit.coeus.negotiation.NegotiationAttachmentReader");
            documentBean.setParameterMap(map);
            request.setDataObject(documentBean);
            request.setFunctionType(DocumentConstants.GENERATE_STREAM_URL);
            //For Streaming
            AppletServletCommunicator comm
                    = new AppletServletCommunicator(STREAMING_SERVLET, request);
            comm.send();
            ResponderBean response = comm.getResponse();
            if(response.isSuccessfulResponse()){
                try{
                    map = (Map)response.getDataObject();
                    String reportUrl = (String)map.get(DocumentConstants.DOCUMENT_URL);
                    reportUrl = reportUrl.replace('\\', '/') ;
                    URL urlObj = new URL(reportUrl);
                    URLOpener.openUrl(urlObj);
                }catch (Exception e){
                    e.printStackTrace();
                    CoeusOptionPane.showInfoDialog( e.getMessage() );
                }
            }
        }
    }

    private NegotiationAttachmentBean getBlobDataForNegActivity(){
        HashMap hmBlobData = new HashMap();
        NegotiationAttachmentBean negotiationAttachmentBean = new NegotiationAttachmentBean();
        negotiationAttachmentBean.setNegotiationNumber(negotiationActivitiesBean.getNegotiationNumber());
        negotiationAttachmentBean.setActivityNumber(negotiationActivitiesBean.getActivityNumber());
        RequesterBean request = new RequesterBean();
        request.setFunctionType(GET_BLOB_DATA_FOR_NEGOTIATION);
        request.setDataObject(negotiationAttachmentBean);
        AppletServletCommunicator comm = new AppletServletCommunicator(NEGOTIATION_SERVLET, request);
        comm.send();
        
        ResponderBean response = comm.getResponse();
        negotiationAttachmentBean = (NegotiationAttachmentBean)response.getDataObject();
        return negotiationAttachmentBean;
    }
    
     public void setParentFunctionType(char pFunctionType) {
        this.parentFunctionType = pFunctionType;
    }
    
     private boolean canViewAttachment(){
         boolean canView = false;
         //coeusdev 499 start
         if ( parentFunctionType == TypeConstants.MODIFY_MODE || parentFunctionType == TypeConstants.DISPLAY_MODE){
             canView = true;
         }else  if(functionType == TypeConstants.DISPLAY_MODE && chkOspOnly.isSelected()){
//         if ( parentFunctionType == TypeConstants.MODIFY_MODE){
//             canView = true;
//         }else  if(functionType == TypeConstants.DISPLAY_MODE && chkOspOnly.isSelected()){
         //coeusdev 499
             canView = false;
         }else{
             canView = true;
         }
         return canView;
     }
   //Added for case 2806 - Upload attachments to neg module - End
    
    public void mouseEntered(java.awt.event.MouseEvent mouseEvent) {
    }    
    
    public void mouseExited(java.awt.event.MouseEvent mouseEvent) {
    }
    
    public void mousePressed(java.awt.event.MouseEvent mouseEvent) {
    }
    
    public void mouseReleased(java.awt.event.MouseEvent mouseEvent) {
    }

    /** Return this child panel to the main panel
     * @return the current instance
     */
    public JPanel getChildPanel(){
        if( isRestrictedViewVisible() ){
            lblOspOnly.setVisible(true);
            chkOspOnly.setVisible(true);
        }else{
            lblOspOnly.setVisible(false);
            chkOspOnly.setVisible(false);
        }
        if( functionType == TypeConstants.DISPLAY_MODE ){
            chkOspOnly.setEnabled(false);
        }
        //Check for attachment rights -Case 2806
        if(!canViewAttachment()){
            btnIcon.setEnabled(false);
        }
        return this;
    }

    /**
     * Getter for property restrictedViewVisible.
     * @return Value of property restrictedViewVisible.
     */
    public boolean isRestrictedViewVisible() {
        return restrictedViewVisible;
    }    

    /**
     * Setter for property restrictedViewVisible.
     * @param restrictedViewVisible New value of property restrictedViewVisible.
     */
    public void setRestrictedViewVisible(boolean restrictedViewVisible) {
        this.restrictedViewVisible = restrictedViewVisible;
    }   
    
    /**
     * Getter for property rowIndex.
     * @return Value of property rowIndex.
     */
    public int getRowIndex() {
        return rowIndex;
    }    

    /**
     * Setter for property rowIndex.
     * @param rowIndex New value of property rowIndex.
     */
    public void setRowIndex(int rowIndex) {
        this.rowIndex = rowIndex;
    }    

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc=" Generated Code ">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        lblActivityType = new javax.swing.JLabel();
        lblFollowupDate = new javax.swing.JLabel();
        lblDescription = new javax.swing.JLabel();
        lblIcon = new javax.swing.JLabel();
        scrPnDescription = new javax.swing.JScrollPane();
        txtArDescription = new javax.swing.JTextArea();
        lblActivityDate = new javax.swing.JLabel();
        lblLastUpdate = new javax.swing.JLabel();
        lblActivityTypeVal = new javax.swing.JLabel();
        lblFollowupDateVal = new javax.swing.JLabel();
        lblActivityDateVal = new javax.swing.JLabel();
        lblLastUpdateVal = new javax.swing.JLabel();
        lblCreateDate = new javax.swing.JLabel();
        lblCreateDateVal = new javax.swing.JLabel();
        lblLastUpdateBy = new javax.swing.JLabel();
        lblLastUpdateByVal = new javax.swing.JLabel();
        lblOspOnly = new javax.swing.JLabel();
        chkOspOnly = new javax.swing.JCheckBox();
        lblAttachmentName = new javax.swing.JLabel();
        btnIcon = new javax.swing.JButton();

        setLayout(new java.awt.GridBagLayout());

        lblActivityType.setFont(CoeusFontFactory.getLabelFont());
        lblActivityType.setText("Activity Type:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(5, 0, 5, 2);
        add(lblActivityType, gridBagConstraints);

        lblFollowupDate.setFont(CoeusFontFactory.getLabelFont());
        lblFollowupDate.setText("Followup Date:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 5, 2);
        add(lblFollowupDate, gridBagConstraints);

        lblDescription.setFont(CoeusFontFactory.getLabelFont());
        lblDescription.setText("Description:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 5, 5, 2);
        add(lblDescription, gridBagConstraints);

        lblIcon.setFont(CoeusFontFactory.getLabelFont());
        lblIcon.setMaximumSize(new java.awt.Dimension(23, 23));
        lblIcon.setMinimumSize(new java.awt.Dimension(23, 23));
        lblIcon.setPreferredSize(new java.awt.Dimension(23, 23));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 2);
        add(lblIcon, gridBagConstraints);

        scrPnDescription.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        scrPnDescription.setMinimumSize(new java.awt.Dimension(100, 40));
        scrPnDescription.setPreferredSize(new java.awt.Dimension(100, 40));
        txtArDescription.setBackground(javax.swing.UIManager.getDefaults().getColor("Panel.background"));
        txtArDescription.setEditable(false);
        txtArDescription.setFont(CoeusFontFactory.getNormalFont());
        txtArDescription.setLineWrap(true);
        txtArDescription.setWrapStyleWord(true);
        scrPnDescription.setViewportView(txtArDescription);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.gridwidth = 7;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 5, 5, 5);
        add(scrPnDescription, gridBagConstraints);

        lblActivityDate.setFont(CoeusFontFactory.getLabelFont());
        lblActivityDate.setText("Activity Date:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(5, 0, 5, 2);
        add(lblActivityDate, gridBagConstraints);

        lblLastUpdate.setFont(CoeusFontFactory.getLabelFont());
        lblLastUpdate.setText("Last Update:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 5, 2);
        add(lblLastUpdate, gridBagConstraints);

        lblActivityTypeVal.setFont(CoeusFontFactory.getNormalFont());
        lblActivityTypeVal.setMaximumSize(new java.awt.Dimension(150, 15));
        lblActivityTypeVal.setMinimumSize(new java.awt.Dimension(150, 15));
        lblActivityTypeVal.setPreferredSize(new java.awt.Dimension(150, 15));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 0, 5, 2);
        add(lblActivityTypeVal, gridBagConstraints);

        lblFollowupDateVal.setFont(CoeusFontFactory.getNormalFont());
        lblFollowupDateVal.setMaximumSize(new java.awt.Dimension(150, 15));
        lblFollowupDateVal.setMinimumSize(new java.awt.Dimension(150, 15));
        lblFollowupDateVal.setPreferredSize(new java.awt.Dimension(150, 15));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 5, 2);
        add(lblFollowupDateVal, gridBagConstraints);

        lblActivityDateVal.setFont(CoeusFontFactory.getNormalFont());
        lblActivityDateVal.setMaximumSize(new java.awt.Dimension(100, 15));
        lblActivityDateVal.setMinimumSize(new java.awt.Dimension(100, 15));
        lblActivityDateVal.setPreferredSize(new java.awt.Dimension(100, 15));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 0, 5, 5);
        add(lblActivityDateVal, gridBagConstraints);

        lblLastUpdateVal.setFont(CoeusFontFactory.getNormalFont());
        lblLastUpdateVal.setMaximumSize(new java.awt.Dimension(100, 15));
        lblLastUpdateVal.setMinimumSize(new java.awt.Dimension(100, 15));
        lblLastUpdateVal.setPreferredSize(new java.awt.Dimension(100, 15));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 5, 5);
        add(lblLastUpdateVal, gridBagConstraints);

        lblCreateDate.setFont(CoeusFontFactory.getLabelFont());
        lblCreateDate.setText("Create Date:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 5;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 2);
        add(lblCreateDate, gridBagConstraints);

        lblCreateDateVal.setFont(CoeusFontFactory.getNormalFont());
        lblCreateDateVal.setMaximumSize(new java.awt.Dimension(100, 15));
        lblCreateDateVal.setMinimumSize(new java.awt.Dimension(100, 15));
        lblCreateDateVal.setPreferredSize(new java.awt.Dimension(100, 15));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 6;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 0, 5, 5);
        add(lblCreateDateVal, gridBagConstraints);

        lblLastUpdateBy.setFont(CoeusFontFactory.getLabelFont());
        lblLastUpdateBy.setText("Last Update By:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 5;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 5, 2);
        add(lblLastUpdateBy, gridBagConstraints);

        lblLastUpdateByVal.setFont(CoeusFontFactory.getNormalFont());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 6;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 5, 5);
        add(lblLastUpdateByVal, gridBagConstraints);

        lblOspOnly.setFont(CoeusFontFactory.getLabelFont());
        lblOspOnly.setText("OSP only:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 5;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 5, 2);
        add(lblOspOnly, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 6;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 5, 0);
        add(chkOspOnly, gridBagConstraints);

        lblAttachmentName.setFont(CoeusFontFactory.getNormalFont());
        lblAttachmentName.setAutoscrolls(true);
        lblAttachmentName.setMaximumSize(new java.awt.Dimension(150, 15));
        lblAttachmentName.setMinimumSize(new java.awt.Dimension(150, 15));
        lblAttachmentName.setPreferredSize(new java.awt.Dimension(150, 15));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 8;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridheight = 4;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 0, 5, 2);
        add(lblAttachmentName, gridBagConstraints);

        btnIcon.setContentAreaFilled(false);
        btnIcon.setPreferredSize(new java.awt.Dimension(25, 25));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 7;
        gridBagConstraints.gridy = 0;
        add(btnIcon, gridBagConstraints);

    }// </editor-fold>//GEN-END:initComponents
   
    // Variables declaration - do not modify//GEN-BEGIN:variables
    public javax.swing.JButton btnIcon;
    public javax.swing.JCheckBox chkOspOnly;
    public javax.swing.JLabel lblActivityDate;
    public javax.swing.JLabel lblActivityDateVal;
    public javax.swing.JLabel lblActivityType;
    public javax.swing.JLabel lblActivityTypeVal;
    public javax.swing.JLabel lblAttachmentName;
    public javax.swing.JLabel lblCreateDate;
    public javax.swing.JLabel lblCreateDateVal;
    public javax.swing.JLabel lblDescription;
    public javax.swing.JLabel lblFollowupDate;
    public javax.swing.JLabel lblFollowupDateVal;
    public javax.swing.JLabel lblIcon;
    public javax.swing.JLabel lblLastUpdate;
    public javax.swing.JLabel lblLastUpdateBy;
    public javax.swing.JLabel lblLastUpdateByVal;
    public javax.swing.JLabel lblLastUpdateVal;
    public javax.swing.JLabel lblOspOnly;
    public javax.swing.JScrollPane scrPnDescription;
    public javax.swing.JTextArea txtArDescription;
    // End of variables declaration//GEN-END:variables
    
}
