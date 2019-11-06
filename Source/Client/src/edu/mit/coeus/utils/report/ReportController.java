/*
 * ReportController.java
 *
 * Created on December 21, 2005, 10:14 AM
 */
/* PMD check performed, and commented unused imports and variables 
 * on 04-Feb-2011 by Md.Ehtesham Ansari
 */

package edu.mit.coeus.utils.report;

import edu.mit.coeus.bean.CoeusReportGroupBean;
import edu.mit.coeus.brokers.RequesterBean;
import edu.mit.coeus.brokers.ResponderBean;
import edu.mit.coeus.exception.CoeusException;
import edu.mit.coeus.gui.CoeusDlgWindow;
import edu.mit.coeus.gui.CoeusFontFactory;
import edu.mit.coeus.gui.URLOpener;
import edu.mit.coeus.utils.AppletServletCommunicator;
import edu.mit.coeus.utils.CoeusGuiConstants;
import edu.mit.coeus.utils.CoeusOptionPane;
import edu.mit.coeus.utils.ScreenFocusTraversalPolicy;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Hashtable;
import java.util.Vector;
import javax.swing.AbstractAction;
import javax.swing.AbstractCellEditor;
import javax.swing.DefaultListSelectionModel;
import javax.swing.JCheckBox;
import javax.swing.JTable;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableColumn;

/**
 *
 * @author  geot
 */
public abstract class ReportController implements ActionListener{
    private static final int REPORT_COLUMN = 0;
    //Added for COEUSQA-1683 Print option to display Version Comments on Budget Reports-Start
    private static final int COMMENTS_COLUMN = 1;
    private static final int COMMENT_COLUMN_WIDTH = 150;
    private Vector vecSelectedComments;
    //Added for COEUSQA-1683 Print option to display Version Comments on Budget Reports-End
    private CoeusReportGroupBean repGrpBean;
    private Vector reports;
    private ReportForm reportForm;
    private ReportsTableModel tblModel;
    private CoeusDlgWindow dlgReports;
    private String reportGrpName;
    private String sltdReportId;
    private static final int WIDTH = 650;
    private static final int HEIGHT = 500;
    
    
    /** Creates a new instance of ReportController */
    protected ReportController(String reportGrpName) throws CoeusException{
        this.reportGrpName = reportGrpName;
        reportForm = new ReportForm();
        tblModel = new ReportsTableModel();
        fetchReportGrp();
        registerComponents();
        postInitComponents();
//        display();
    }
    //Modified for COEUSQA-1683 Print option to display Version Comments on Budget Reports
    public abstract Hashtable getPrintData(boolean isCommentSelected) throws CoeusException;
    public void print(CoeusReportGroupBean.Report report, boolean isCommentSelected) throws CoeusException{
        RequesterBean requester = new RequesterBean();
        requester.setId(report.getId());
        setSltdReportId(report.getId());
        //Modified for COEUSQA-1683 Print option to display Version Comments on Budget Reports
        requester.setDataObject(getPrintData(isCommentSelected));
        requester.setFunctionType('R');
        appSerComm.setRequest(requester);
        appSerComm.send();
        ResponderBean res = appSerComm.getResponse();
        String pdf = (String)res.getDataObject();
        try{
            //Added for Case 3131 -  Proposal Hierarchy : Budget Summary Printing - Start
            if(pdf !=null && !pdf.equals("")){
            URL url = new URL(pdf);
            URLOpener.openUrl(url);
            } //Added for Case 3131 -  Proposal Hierarchy : Budget Summary Printing - End
        }catch (MalformedURLException malformedURLException) {
            throw new CoeusException(malformedURLException.getMessage());
        }
    }
    private void postInitComponents(){
        dlgReports = new CoeusDlgWindow(CoeusGuiConstants.getMDIForm());
        dlgReports.getContentPane().add(reportForm);
        dlgReports.setTitle("Print : "+repGrpBean.getDisplay());
        dlgReports.setFont(CoeusFontFactory.getLabelFont());
        dlgReports.setModal(true);
        dlgReports.setResizable(false);
        dlgReports.setSize(WIDTH,HEIGHT);
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        Dimension dlgSize = dlgReports.getSize();
        dlgReports.setLocation(screenSize.width/2 - (dlgSize.width/2),
        screenSize.height/2 - (dlgSize.height/2));
        
        dlgReports.addEscapeKeyListener(
        new AbstractAction("escPressed"){
            public void actionPerformed(ActionEvent ae){
                closeWindow();
                return;
            }
        });
        dlgReports.setDefaultCloseOperation(CoeusDlgWindow.DO_NOTHING_ON_CLOSE);
        dlgReports.addWindowListener(new WindowAdapter(){
            public void windowClosing(WindowEvent we){
                closeWindow();
                return;
            }
        });
        
        dlgReports.addComponentListener(
        new ComponentAdapter(){
            public void componentShown(ComponentEvent e){
                setWindowFocus();
            }
        });
    }
    private void registerComponents(){
        reportForm.tblReports.setModel(tblModel);
        reportForm.btnPrint.addActionListener(this);
        reportForm.btnClose.addActionListener(this);
        reportForm.btnPrint.setFont(CoeusFontFactory.getLabelFont());
        reportForm.btnClose.setFont(CoeusFontFactory.getLabelFont());
        JTableHeader tableHeader = reportForm.tblReports.getTableHeader();
        tableHeader.setReorderingAllowed(false);
        tableHeader.setFont(CoeusFontFactory.getLabelFont());
        reportForm.tblReports.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_ALL_COLUMNS);
        reportForm.tblReports.setRowHeight(22);
        reportForm.tblReports.setShowHorizontalLines(true);
        reportForm.tblReports.setShowVerticalLines(true);
        reportForm.tblReports.setOpaque(false);
        reportForm.tblReports.setSelectionMode(DefaultListSelectionModel.SINGLE_SELECTION);
        TableColumn reportColumn = reportForm.tblReports.getColumnModel().getColumn(0);
        reportColumn.setPreferredWidth(215);
        reportColumn.setResizable(true);
        //Added for COEUSQA-1683 Print option to display Version Comments on Budget Reports-Start
        reportColumn = reportForm.tblReports.getColumnModel().getColumn(COMMENTS_COLUMN);
        reportColumn.setMaxWidth(COMMENT_COLUMN_WIDTH);
        reportColumn.setMinWidth(COMMENT_COLUMN_WIDTH);
        reportColumn.setPreferredWidth(COMMENT_COLUMN_WIDTH);
        reportColumn.setResizable(false);
        reportColumn.setCellEditor(new CommentsCheckBoxEditor()); 
        //Added for COEUSQA-1683 Print option to display Version Comments on Budget Reports-End
        // Added for COEUSQA-2889 PDF Print>Budget Summary by Period Always Prints ON CAMPUS
        // Setting the first row as a default.
         if(tblModel.getRowCount() > 0){
            reportForm.tblReports.setRowSelectionInterval(0,0);
        }
        // COEUSQA-2889 - End
        java.awt.Component[] components = {
            reportForm.btnPrint,reportForm.btnClose
        };
        ScreenFocusTraversalPolicy  traversePolicy = new ScreenFocusTraversalPolicy( components );
        reportForm.setFocusTraversalPolicy(traversePolicy);
        reportForm.setFocusCycleRoot(true);        
    }
    private final String connectTo = CoeusGuiConstants.CONNECTION_URL+"/ReportConfigServlet";
    private AppletServletCommunicator appSerComm;
    private void fetchReportGrp() throws CoeusException{
        appSerComm = new AppletServletCommunicator();
        appSerComm.setConnectTo(connectTo);
        RequesterBean requester = new RequesterBean();
        requester.setDataObject(reportGrpName);
        requester.setFunctionType('G');
        appSerComm.setRequest(requester);
        appSerComm.send();
        ResponderBean res = appSerComm.getResponse();
        repGrpBean = (CoeusReportGroupBean)res.getDataObject();
        //get the data from server
        tblModel.setData(repGrpBean);
    }
    private void closeWindow(){
        dlgReports.dispose();
    }
    private void setWindowFocus(){
        reportForm.btnPrint.requestFocusInWindow();
    }    
    public void display(){
        dlgReports.setVisible(true);
    }
    public void actionPerformed(java.awt.event.ActionEvent e) {
        Object source = e.getSource();
        try{
            dlgReports.setCursor(new Cursor(Cursor.WAIT_CURSOR));
            if(source.equals(reportForm.btnPrint)){
                //print
                //Modified for COEUSQA-1683 Print option to display Version Comments on Budget Reports-Start
                print((CoeusReportGroupBean.Report)tblModel.getValueAt(reportForm.tblReports.getSelectedRow()),
                        (Boolean)tblModel.getValueAt(reportForm.tblReports.getSelectedRow(),COMMENTS_COLUMN));
                //Modified for COEUSQA-1683 Print option to display Version Comments on Budget Reports-End
            }else if(source.equals(reportForm.btnClose)){
                //close the dialog
                closeWindow();
            }
        }catch(Exception ex){
            ex.printStackTrace();
            CoeusOptionPane.showErrorDialog(ex.getMessage());
        }
        finally{
            dlgReports.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
        }
    }
    
    /**
     * Getter for property sltdReportId.
     * @return Value of property sltdReportId.
     */
    public java.lang.String getSltdReportId() {
        return sltdReportId;
    }    
    
    /**
     * Setter for property sltdReportId.
     * @param sltdReportId New value of property sltdReportId.
     */
    public void setSltdReportId(java.lang.String sltdReportId) {
        this.sltdReportId = sltdReportId;
    }
    
    public class ReportsTableModel extends AbstractTableModel{
        //Modified for COEUSQA-1683 Print option to display Version Comments on Budget Reports
        private String colName[] = {"Report Name","Print Budget Comments"};
        private Class colClass[] ={String.class,Boolean.class};
        
        public boolean isCellEditable(int row, int col){
            //Modified for COEUSQA-1683 Print option to display Version Comments on Budget Reports-Start
            if(REPORT_COLUMN == col){
            return false;
            }else{
            return true;    
            }
            //Modified for COEUSQA-1683 Print option to display Version Comments on Budget Reports-End
        }
        
        public Class getColumnClass(int col){
            return colClass[col];
        }
        
        public String getColumnName(int col){
            return colName[col];
        }
        
        public int getColumnCount() {
            return colName.length;
        }
        
        public void setData(CoeusReportGroupBean repGrp){
            repGrpBean = repGrp;
            java.util.Collection col = repGrpBean.getReports().values();
            reports = new Vector(repGrpBean.getReports().values());
            vecSelectedComments = new Vector();
            //Added for COEUSQA-1683 Print option to display Version Comments on Budget Reports-Start
                if(reports != null && !reports.isEmpty()){                
                    for(int index=0;index<reports.size();index++){
                        vecSelectedComments.add(index,(Boolean)false);
                    }
                }
            //Added for COEUSQA-1683 Print option to display Version Comments on Budget Reports-End
        }
        
        public int getRowCount() {
            if(repGrpBean==null){
                return 0;
            }else{
                return repGrpBean.getReports().size();
            }
        }
        
        public Object getValueAt(int row, int col) {
            CoeusReportGroupBean.Report report =
            (CoeusReportGroupBean.Report)reports.get(row);
            switch(col){
                case REPORT_COLUMN:
                    return report.getDispValue();
                //Added for COEUSQA-1683 Print option to display Version Comments on Budget Reports-Start
                case COMMENTS_COLUMN:                     
                    return isCommentSelected(row);                     
                //Added for COEUSQA-1683 Print option to display Version Comments on Budget Reports-End
            }
            return "";
        }
        public Object getValueAt(int row) {
            CoeusReportGroupBean.Report report =
            (CoeusReportGroupBean.Report)reports.get(row);
            return report;
        }
    }
    
     //Added for COEUSQA-1683 Print option to display Version Comments on Budget Reports-Start
     /*
      * class created to set and get the comment flag
      * when table cell is modified
      */
     class CommentsCheckBoxEditor extends AbstractCellEditor implements TableCellEditor{
        private JCheckBox chkComments;
        private int column;
        CommentsCheckBoxEditor() {
            chkComments = new JCheckBox();
            chkComments.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    if(e.getSource() == chkComments){
                        stopCellEditing();                                               
                    }
                }
            });            
        }   

      /*
       * New method created to get the cell value
       * of table index when table cell is modified
       * @return object componenet
       */
        public Object getCellEditorValue() {
            this.column = column;
            switch(column) {
                case COMMENTS_COLUMN:
                    return chkComments.isSelected();
                default:
                    break;
            }
            return chkComments;
        }

      /*
       * New method created set the selected comment checkBox value
       * @param JTable table
       * @param Object value
       * @param boolean isSelected
       * @param int row
       * @param int column
       * @return Component to display
       */
        public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
            this.column = column;
            switch(column) {
                case COMMENTS_COLUMN:  
                    if((Boolean)value){
                        chkComments.setSelected(false);
                        setCommentSelected(false, row);
                    }else{
                        chkComments.setSelected(true);    
                        setCommentSelected(true, row);
                    }                     
                        chkComments.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
                    return chkComments;
                default:
                    break;
            }
            return null;
        }                    
    }
    
    /*Setter for budget comment isSelected      
     *@ param boolean isSelected
     *@ param int selectedRow  row
     */
    private void setCommentSelected(boolean isSelected, int row){
        vecSelectedComments.remove(row);
        vecSelectedComments.add(row, isSelected);
    }
    /*Getter for budget comment isSelected            
     *@ param int selectedRow  row
     *@ return boolean valye
     */
    private boolean isCommentSelected(int row){
        return (Boolean)vecSelectedComments.get(row);
    }
    //Added for COEUSQA-1683 Print option to display Version Comments on Budget Reports-End
    
}
