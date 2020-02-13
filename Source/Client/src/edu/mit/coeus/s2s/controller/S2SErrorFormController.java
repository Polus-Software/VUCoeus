/**
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 *
 * S2SErrorFormController.java
 *
 * Created on May 3, 2005, 4:51 PM
 */

package edu.mit.coeus.s2s.controller;

import edu.mit.coeus.gui.CoeusDlgWindow;
import edu.mit.coeus.gui.CoeusFontFactory;
import edu.mit.coeus.gui.CoeusMessageResources;
import edu.mit.coeus.gui.URLOpener;
import edu.mit.coeus.s2s.bean.FormInfoBean;
import edu.mit.coeus.s2s.bean.S2SHeader;
import edu.mit.coeus.s2s.gui.S2SErrorForm;
import edu.mit.coeus.s2s.validator.S2SValidationException;
import edu.mit.coeus.utils.CoeusGuiConstants;
import edu.mit.coeus.utils.CoeusOptionPane;
import edu.mit.coeus.utils.ScreenFocusTraversalPolicy;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;

/**
 *
 * @author  Geo Thomas
 */
public class S2SErrorFormController  implements ActionListener, MouseListener{
    private CoeusMessageResources coeusMessageResources;
    private S2SErrorForm s2sErrorFrm;
    private S2SErrorFormTableModel s2sErrTableModel;
    private CoeusDlgWindow dlgErrForm;
    /** Creates a new instance of S2SErrorFormController */
    public S2SErrorFormController() {
        s2sErrorFrm = new S2SErrorForm();
        registerComponents();
        setTableData(null);
        formatFields();
        postInitComponents();
    }
    public void registerComponents(){
        s2sErrTableModel = new S2SErrorFormTableModel();
        s2sErrorFrm.tblError.setModel(s2sErrTableModel);
        TableColumn col = s2sErrorFrm.tblError.getColumnModel().getColumn(0);
        col.setCellRenderer(new MessageRenderer());
        s2sErrorFrm.tblError.setTableHeader(null);
        s2sErrorFrm.btnOk.addActionListener(this);
        s2sErrorFrm.lblSchemaUrlVal.addMouseListener(this);
        s2sErrorFrm.lblInstrUrlVal.addMouseListener(this);
    }
    
    public void setFormData(S2SHeader headerData,S2SValidationException s2sValExce){
        s2sErrorFrm.lblPropNumVal.setText(headerData.getSubmissionTitle());
        s2sErrorFrm.lblCfdaNumVal.setText(headerData.getCfdaNumber());
        s2sErrorFrm.lblPrgmNumVal.setText(headerData.getOpportunityId());
        s2sErrorFrm.lblSpNumVal.setText(headerData.getAgency());
        s2sErrorFrm.lblSchemaUrlVal.setText(s2sValExce.getOppSchemaUrl());
        s2sErrorFrm.lblInstrUrlVal.setText(s2sValExce.getOppInstrUrl());
        setTableData(new Vector(s2sValExce.getErrors()));
    }
    public void setTableData(Vector data){
        if(data==null) return;
        s2sErrTableModel.setData(data);
        for(int i=0;i<data.size();i++){
            S2SValidationException.ErrorBean err = (S2SValidationException.ErrorBean)data.elementAt(i);
            Object msgObj = err.getMsgObj();
            if(!(msgObj instanceof FormInfoBean)){
                String text = err.getMsg();
                s2sErrorFrm.tblError.setRowHeight(i,(int)Math.ceil((text.length()/120d))*20);
            }else{
                s2sErrorFrm.tblError.setRowHeight(i,20);
            }
        }
        
    }
/** Specifies the Modal window */
    private void postInitComponents() {

        Component[] components = { s2sErrorFrm.tblError,s2sErrorFrm.btnOk};
        ScreenFocusTraversalPolicy traversePolicy = new ScreenFocusTraversalPolicy( components );
        s2sErrorFrm.setFocusTraversalPolicy(traversePolicy);
        s2sErrorFrm.setFocusCycleRoot(true);
        
        dlgErrForm = new CoeusDlgWindow(CoeusGuiConstants.getMDIForm());
        dlgErrForm.getContentPane().add(s2sErrorFrm);
        dlgErrForm.setTitle("Validation Errors");
        dlgErrForm.setFont(CoeusFontFactory.getLabelFont());
        dlgErrForm.setModal(true);
        dlgErrForm.setResizable(false);
        dlgErrForm.pack();
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        Dimension dlgSize = dlgErrForm.getSize();
        dlgErrForm.setLocation(screenSize.width/2 - (dlgSize.width/2),
        screenSize.height/2 - (dlgSize.height/2));
        
        dlgErrForm.addEscapeKeyListener(
        new AbstractAction("escPressed"){
            public void actionPerformed(ActionEvent ae){
                performCancelAction();
                return;
            }
        });
        dlgErrForm.setDefaultCloseOperation(CoeusDlgWindow.DO_NOTHING_ON_CLOSE);
        dlgErrForm.addWindowListener(new WindowAdapter(){
            public void windowClosing(WindowEvent we){
                performCancelAction();
                return;
            }
        });
        
        dlgErrForm.addComponentListener(
        new ComponentAdapter(){
            public void componentShown(ComponentEvent e){
                setWindowFocus();
            }
        });
    }
    public JComponent getComponentUI(){
        return s2sErrorFrm;
    }
    public void formatFields(){
        s2sErrorFrm.lblCfdaNum.setFont(CoeusFontFactory.getLabelFont());
        s2sErrorFrm.lblErrorHeader.setFont(CoeusFontFactory.getLabelFont());
        s2sErrorFrm.lblInstrUrl.setFont(CoeusFontFactory.getLabelFont());
        s2sErrorFrm.lblPrgmNum.setFont(CoeusFontFactory.getLabelFont());
        s2sErrorFrm.lblProposalNumber.setFont(CoeusFontFactory.getLabelFont());
        s2sErrorFrm.lblSchemaUrl.setFont(CoeusFontFactory.getLabelFont());
        s2sErrorFrm.lblSpNum.setFont(CoeusFontFactory.getLabelFont());
    }
    public void actionPerformed(java.awt.event.ActionEvent e) {
        if(e.getSource().equals(s2sErrorFrm.btnOk)){
            performCancelAction();
        }
    }
    
    public void mouseClicked(java.awt.event.MouseEvent e) {
        if(e.getClickCount()==2){
            Object source = e.getSource();
            System.out.println("url is"+((JLabel)source).getText());
            try{
               URLOpener.openUrl(((JLabel)source).getText());
            }catch (Exception ex){
                ex.printStackTrace();
                CoeusOptionPane.showInfoDialog(ex.getMessage());
            }
        }
    }
    
    public void mouseEntered(java.awt.event.MouseEvent e) {
    }
    
    public void mouseExited(java.awt.event.MouseEvent e) {
    }
    
    public void mousePressed(java.awt.event.MouseEvent e) {
    }
    
    public void mouseReleased(java.awt.event.MouseEvent e) {
    }
    private void performCancelAction(){
        dlgErrForm.dispose();
    }
    private void setWindowFocus(){
        this.s2sErrorFrm.btnOk.requestFocusInWindow();
    }
    public void display(){
        this.dlgErrForm.setVisible(true);
    }
    public class S2SErrorFormTableModel extends AbstractTableModel{
        private String[] colName = {" "};
        private Class[] colClass = {String.class};
        private Vector errorList = new Vector();

        boolean[] canEdit = new boolean [] {
            false
        };

        public boolean isCellEditable(int rowIndex, int columnIndex) {
            return canEdit [columnIndex];
        }

        public int getColumnCount() {
            return colName.length;
        }
        
        public String getColumnName(int col){
            return colName[col];
        }
        
        public Class getColumnClass(int col){
            return colClass[col];
        }
        
        public void setData(Vector data){
            this.errorList = data;
            fireTableDataChanged();
        }
        
        public int getRowCount() {
            if(errorList==null){
                return 0;
            }else{
                return errorList.size();
            }
        }
        public Object getValueAt(int row) {
            if(getRowCount()==0) return null;
            S2SValidationException.ErrorBean err = (S2SValidationException.ErrorBean)errorList.elementAt(row);
            return errorList.elementAt(row);
        }

        public Object getValueAt(int row,int col) {
            switch(col){
                case(0):
                    return getValueAt(row);
            }
            return null;
        }
    }// end of table model class
    /** An inner class renderer used for the table rows as a textarea component, to hold the data
     *according to the specified size
     */
    private class MessageRenderer extends JTextArea implements TableCellRenderer{
        FormInfoBean frmBean;
        S2SValidationException.ErrorBean err;
        JLabel lblFrmHeader;
        MessageRenderer(){
            setLineWrap(true);
            setEditable(false);
            setWrapStyleWord(true);
            setFont(CoeusFontFactory.getNormalFont());
            lblFrmHeader = new JLabel();
            lblFrmHeader.setFont(CoeusFontFactory.getLabelFont());
        }
        public java.awt.Component getTableCellRendererComponent(javax.swing.JTable table,
        Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            err = (S2SValidationException.ErrorBean)value;
            Object msgObj = err.getMsgObj();
            if(msgObj instanceof FormInfoBean){
                frmBean = (FormInfoBean)msgObj;
                lblFrmHeader.setText(frmBean.getFormName());
                return lblFrmHeader;
            }
            
            String text = value==null?" ":err.getMsg();
            setText(text);
            return this;
        }

    }    
    public static void main(String args[]){
        S2SErrorFormController c = new S2SErrorFormController();
        S2SValidationException exce = new S2SValidationException();
        FormInfoBean fr = new FormInfoBean();
        fr.setNs("hht");
        fr.setFormName("hgfgf");
        exce.addError(fr,exce.WARNING);
        exce.addError("1error1 rdjhbrgm w ertjkewrtert wert ewrtewr " +
        "tewrt interface t ewrtew rt ewrt ewrtew rt ert ertjertijerte " +
        "tewrt interface t ewrtew rt ewrt ewrtew rt ert ertjertijerte " +
        "tewrt interface t ewrtew rt ewrt ewrtew rt ert ertjertijerte " +
        "tewrt interface t ewrtew rt ewrt ewrtew rt ert ertjertijerte " +
        "rt er terwit ertet ertert ewrt bte rt ewrt erwt ert",exce.ERROR);
        exce.addError("2error1 rdjhbrgm w ertjkewrtert wert ewrtewr " +
        "tewrt interface t ewrtew rt ewrt ewrtew rt ert ertjertijerte " +
        "tewrt interface t ewrtew rt ewrt ewrtew rt ert ertjertijerte " +
        "tewrt interface t ewrtew rt ewrt ewrtew rt ert ertjertijerte " +
        "tewrt interface t ewrtew rt ewrt ewrtew rt ert ertjertijerte " +
        "tewrt interface t ewrtew rt ewrt ewrtew rt ert ertjertijerte " +
        "tewrt interface t ewrtew rt ewrt ewrtew rt ert ertjertijerte " +
        "tewrt interface t ewrtew rt ewrt ewrtew rt ert ertjertijerte " +
        "tewrt interface t ewrtew rt ewrt ewrtew rt ert ertjertijerte " +
        "tewrt interface t ewrtew rt ewrt ewrtew rt ert ertjertijerte " +
        "tewrt interface t ewrtew rt ewrt ewrtew rt ert ertjertijerte " +
        "tewrt interface t ewrtew rt ewrt ewrtew rt ert ertjertijerte " +
        "rt er terwit ertet ertert ewrt bte rt ewrt erwt ert",exce.ERROR);
        exce.addError("3error1 rdjhbrgm w ertjkewrtert wert ewrtewr " +
        "tewrt interface t ewrtew rt ewrt ewrtew rt ert ertjertijerte " +
        "tewrt interface t ewrtew rt ewrt ewrtew rt ert ertjertijerte " +
        "rt er terwit ertet ertert ewrt bte rt ewrt erwt ert",exce.ERROR);
        exce.addError("4error1 rdjhbrgm w ertjkewrtert wert ewrtewr " +
        "tewrt interface t ewrtew rt ewrt ewrtew rt ert ertjertijerte " +
        "tewrt interface t ewrtew rt ewrt ewrtew rt ert ertjertijerte " +
        "tewrt interface t ewrtew rt ewrt ewrtew rt ert ertjertijerte " +
        "tewrt interface t ewrtew rt ewrt ewrtew rt ert ertjertijerte " +
        "tewrt interface t ewrtew rt ewrt ewrtew rt ert ertjertijerte " +
        "tewrt interface t ewrtew rt ewrt ewrtew rt ert ertjertijerte " +
        "tewrt interface t ewrtew rt ewrt ewrtew rt ert ertjertijerte " +
        "tewrt interface t ewrtew rt ewrt ewrtew rt ert ertjertijerte " +
        "tewrt interface t ewrtew rt ewrt ewrtew rt ert ertjertijerte " +
        "tewrt interface t ewrtew rt ewrt ewrtew rt ert ertjertijerte " +
        "tewrt interface t ewrtew rt ewrt ewrtew rt ert ertjertijerte " +
        "tewrt interface t ewrtew rt ewrt ewrtew rt ert ertjertijerte " +
        "tewrt interface t ewrtew rt ewrt ewrtew rt ert ertjertijerte " +
        "tewrt interface t ewrtew rt ewrt ewrtew rt ert ertjertijerte " +
        "rt er terwit ertet ertert ewrt bte rt ewrt erwt ert ****testing****",exce.ERROR);
        c.setFormData(new S2SHeader(),exce);
        c.display();
    }
}

