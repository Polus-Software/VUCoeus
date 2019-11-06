/*
 * PrintCertifications.java
 *
 * Created on February 11, 2005, 2:51 PM
 */

package edu.mit.coeus.propdev.gui;

import edu.mit.coeus.brokers.RequesterBean;
import edu.mit.coeus.brokers.ResponderBean;
import edu.mit.coeus.gui.CoeusDlgWindow;
import edu.mit.coeus.gui.CoeusFontFactory;
import edu.mit.coeus.gui.CoeusMessageResources;
import edu.mit.coeus.gui.URLOpener;
import edu.mit.coeus.utils.CoeusGuiConstants;
import edu.mit.coeus.propdev.bean.*;
import edu.mit.coeus.utils.AppletServletCommunicator;
import edu.mit.coeus.utils.CoeusOptionPane;
import edu.mit.coeus.utils.CoeusVector;

import java.applet.AppletContext;
import java.awt.event.ActionListener;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Hashtable;
import java.util.Vector;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumn;


/**
 *
 * @author  jenlu
 */
public class PrintCertifications extends edu.mit.coeus.gui.CoeusDlgWindow 
    implements ActionListener {
    
//    private Vector recipienIinvestigators;   
    private String propNum;
    private String propTitle;
    private String agencyName;
    private String agencyId;
    private String primeAgencyId;
    private Vector personData;
    private Vector printData;
    //start case 2358
    private ProposalDevelopmentFormBean proposalDevelopmentFormBean;
    //holds CoeusMessageResources instance used for reading message Properties.
    private CoeusMessageResources coeusMessageResources;
    //end case 2358
    private static final char PRINT_CERTIFICATION = 'C';
    //private static final String connect = CoeusGuiConstants.CONNECTION_URL+"/printServlet";   
    private static final String  connect = CoeusGuiConstants.CONNECTION_URL + "/ReportConfigServlet";
    
    
    /** Creates new form PrintCertifications */
    public PrintCertifications(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
    }
    //start case 2358
    public PrintCertifications( ProposalDevelopmentFormBean proposalDevelopmentFormBean) {
//    public PrintCertifications( String propTitle, String agencyName,String agencyId,String primeAgencyId, Vector investigators) {    
        super( CoeusGuiConstants.getMDIForm(),"Print Certifications", true );
//        this.propTitle = propTitle;
//        this.agencyName = agencyName;
//        this.agencyId = agencyId;
//        this.primeAgencyId = primeAgencyId;
        this.proposalDevelopmentFormBean = proposalDevelopmentFormBean;
        Vector investigators = proposalDevelopmentFormBean.getInvestigators();
        ProposalInvestigatorFormBean propInvBean = new ProposalInvestigatorFormBean();
        propInvBean = (ProposalInvestigatorFormBean)investigators.get(0);

        this.propNum= propInvBean.getProposalNumber(); 
//        this.recipienIinvestigators= investigators;
        //end case 2358
        initComponents();
        setTableData(investigators);
        setLocation(this.CENTER);
        postInitComponents();
        this.show();
    }
    
    // supporting method to set listeners to all buttons and table models.
    private void postInitComponents(){
        tblPerson.setRowSelectionInterval(0,0);
        btnClose.addActionListener( this );
        btnPrint.addActionListener( this );
        btnPrintAll.addActionListener( this );        
    }
         
    
    private void setTableData(Vector dataBean) {
        
        ProposalInvestigatorFormBean propInvestigatorBean = null;
        
        Vector personTableRow = null;
        personData = new Vector();
        if (dataBean != null && dataBean.size() > 0 ){
            for (int index = 0 ; index < dataBean.size(); index++){
                propInvestigatorBean = new ProposalInvestigatorFormBean();
                propInvestigatorBean = ( ProposalInvestigatorFormBean )
                dataBean.get( index );
                if( propInvestigatorBean != null){
                    if (index == 0 ){
                         this.propNum= propInvestigatorBean.getProposalNumber();
                    }
                    personTableRow = new Vector();
                    personTableRow.add(propInvestigatorBean.getPersonName());
                    personTableRow.add(propInvestigatorBean);  
                    
                    personData.add(personTableRow);
                   
                    
                }
            }
            
            // for column names
            Vector personHeaders = new Vector();
            personHeaders.add( "Person Name");
            personHeaders.add( "");
         
            DefaultTableModel tableModel =
            new DefaultTableModel(personData,personHeaders){
                public boolean isCellEditable(int rowIndex, int columnIndex){
                    return false;
                }
            };
            tblPerson.setModel(tableModel);           
        }
        
            tblPerson.setOpaque(false);
            tblPerson.setShowVerticalLines(false);
            tblPerson.setShowHorizontalLines(false);
            tblPerson.setRowHeight(22);
            tblPerson.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
            tblPerson.setFont( CoeusFontFactory.getNormalFont() );
            JTableHeader header = tblPerson.getTableHeader();
            header.setReorderingAllowed(false);

            TableColumn clmName = tblPerson.getColumnModel().getColumn(0);
            clmName.setPreferredWidth(350);
            clmName.setMaxWidth(400);
            clmName.setMinWidth(350);
        
            clmName = tblPerson.getColumnModel().getColumn(1);
            clmName.setPreferredWidth(0);
            clmName.setMinWidth(0);
    }
        public void actionPerformed(java.awt.event.ActionEvent ae) {
        Object comSelected = ae.getSource();
        if (comSelected == btnClose) {
            this.dispose();
        }
        else if (comSelected == btnPrint){
            int [] rows = tblPerson.getSelectedRows();
            Vector veSelectData;
            ProposalInvestigatorFormBean propInvBean ;
            printData = new Vector();
//            lblStatus.setText("Printing Certification ...");
            for (int index = 0 ; index < rows.length; index++){
                veSelectData = new Vector();
                propInvBean = (ProposalInvestigatorFormBean)tblPerson.getValueAt(rows[index],1);
                //start case 2358 
                Vector veAnswers = propInvBean.getInvestigatorAnswers();
                if(veAnswers != null && veAnswers.size() < 1 ){
                    CoeusOptionPane.showInfoDialog("Please answer certification questions for "+propInvBean.getPersonName()+ " before printing.  ");
                    return;
                }
                //end case 2358
                veSelectData.add(propInvBean.getPersonId());
                veSelectData.add(propInvBean.getPersonName());
                veSelectData.add(propInvBean.isPrincipleInvestigatorFlag()? "true":"false" );
                printData.add(veSelectData);
            }
            if ( printData.size() > 0){
                 generateReport();
            }        
//            lblStatus.setText("Ready");
        }
        else if (comSelected == btnPrintAll) {
//            lblStatus.setText("Printing Certification ...");
            int rowCount = tblPerson.getRowCount();
            Vector veRowData;
            ProposalInvestigatorFormBean propInvBean ;
            printData = new Vector();
            for (int index = 0 ; index < rowCount; index++){
                veRowData = new Vector();
                propInvBean = (ProposalInvestigatorFormBean)tblPerson.getValueAt(index,1);
                //start case 2358 
                Vector veAnswers = propInvBean.getInvestigatorAnswers();
                if(veAnswers != null && veAnswers.size() < 1 ){
                    CoeusOptionPane.showInfoDialog("Please answer certification questions for "+propInvBean.getPersonName()+ " before printing.  ");
                    return;
                }
                //end case 2358
                veRowData.add(propInvBean.getPersonId());
                veRowData.add(propInvBean.getPersonName());
                veRowData.add(propInvBean.isPrincipleInvestigatorFlag()? "true":"false" );
                printData.add(veRowData);
            }
            if ( printData.size() > 0){
                 generateReport();
            }   
//            lblStatus.setText("Ready");
            //case 1741 begin
            this.dispose();
            //case 1741 end
        }
    }
    private boolean generateReport() {
        RequesterBean requesterBean = new RequesterBean();
        
        CoeusVector cvDataToServer = new CoeusVector();
        //start case 2358
//        cvDataToServer.add(propNum); //0
//        cvDataToServer.add(propTitle); //1
//        cvDataToServer.add(agencyName); //2
//        cvDataToServer.add(agencyId); //3
//        cvDataToServer.add(primeAgencyId); //4
//        cvDataToServer.add(printData); //5 
        cvDataToServer.add(proposalDevelopmentFormBean); //0
        cvDataToServer.add(printData); //1
        requesterBean.setDataObjects(cvDataToServer);
        requesterBean.setFunctionType(PRINT_CERTIFICATION);
        
        //For Streaming
        Hashtable hashtable = new Hashtable();
        
        //Vector vector = (Vector)cvDataToServer.get(1);
        //cvDataToServer.add(1,vector.get(0));
        
        hashtable.put("DATA", cvDataToServer);
        requesterBean.setDataObject(hashtable);
        requesterBean.setId("Proposal/PrintCertification");
        requesterBean.setFunctionType('R');
        //For Streaming
        
        AppletServletCommunicator appletServletCommunicator = new AppletServletCommunicator(connect, requesterBean);
        appletServletCommunicator.setRequest(requesterBean);
        appletServletCommunicator.send();
        ResponderBean responderBean = appletServletCommunicator.getResponse();
        if(responderBean == null) {
            CoeusOptionPane.showErrorDialog("Could not contact server");
            return false;
        }
        if (!responderBean.isSuccessfulResponse()) {
            CoeusOptionPane.showErrorDialog(responderBean.getMessage());
            return false;
        }
        String url = (String)responderBean.getDataObject();
        /*url = url.replace('\\', '/');
        AppletContext coeusContext = CoeusGuiConstants.getMDIForm().getCoeusAppletContext();
        try{
            URL templateURL = new URL(CoeusGuiConstants.CONNECTION_URL + url);
            if(coeusContext != null){
                coeusContext.showDocument( templateURL, "_blank" );
            }else{
                javax.jnlp.BasicService bs = (javax.jnlp.BasicService)javax.jnlp.ServiceManager.lookup("javax.jnlp.BasicService");
                bs.showDocument(templateURL);
            }
            return true;
        } catch (Exception ex) {
            ex.printStackTrace();
            CoeusOptionPane.showErrorDialog(""+ex.getMessage());
            return false;
        }*/
        try{
            URL reportUrl = new URL(url);
            URLOpener.openUrl(reportUrl);
            return true;
        }catch (Exception exception) {
            CoeusOptionPane.showErrorDialog(""+exception.getMessage());
            return false;
        }
    }
    
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    private void initComponents() {//GEN-BEGIN:initComponents
        java.awt.GridBagConstraints gridBagConstraints;

        jPanel1 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblPerson = new javax.swing.JTable();
        lblStatus = new javax.swing.JLabel();
        btnClose = new javax.swing.JButton();
        btnPrint = new javax.swing.JButton();
        btnPrintAll = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        jPanel1.setLayout(new java.awt.GridBagLayout());

        jScrollPane1.setMinimumSize(new java.awt.Dimension(300, 200));
        jScrollPane1.setPreferredSize(new java.awt.Dimension(350, 250));
        jScrollPane1.setAutoscrolls(true);
        tblPerson.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Investagor", "personBean"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.Object.class
            };
            boolean[] canEdit = new boolean [] {
                false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane1.setViewportView(tblPerson);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridheight = 4;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(6, 10, 0, 0);
        jPanel1.add(jScrollPane1, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(0, 10, 0, 0);
        jPanel1.add(lblStatus, gridBagConstraints);

        btnClose.setLabel("Close");
        btnClose.setMaximumSize(new java.awt.Dimension(47, 23));
        btnClose.setMinimumSize(new java.awt.Dimension(47, 23));
        btnClose.setPreferredSize(new java.awt.Dimension(47, 23));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(26, 5, 2, 4);
        jPanel1.add(btnClose, gridBagConstraints);

        btnPrint.setLabel("Print");
        btnPrint.setPreferredSize(new java.awt.Dimension(69, 23));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(3, 5, 2, 4);
        jPanel1.add(btnPrint, gridBagConstraints);

        btnPrintAll.setLabel("Print All");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTH;
        gridBagConstraints.insets = new java.awt.Insets(3, 5, 2, 4);
        jPanel1.add(btnPrintAll, gridBagConstraints);

        getContentPane().add(jPanel1, java.awt.BorderLayout.CENTER);

        pack();
    }//GEN-END:initComponents
    
    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        new PrintCertifications(new javax.swing.JFrame(), true).show();
    }
    

 
    // Variables declaration - do not modify//GEN-BEGIN:variables
    javax.swing.JButton btnClose;
    javax.swing.JButton btnPrint;
    javax.swing.JButton btnPrintAll;
    javax.swing.JPanel jPanel1;
    javax.swing.JScrollPane jScrollPane1;
    javax.swing.JLabel lblStatus;
    javax.swing.JTable tblPerson;
    // End of variables declaration//GEN-END:variables
    
}
