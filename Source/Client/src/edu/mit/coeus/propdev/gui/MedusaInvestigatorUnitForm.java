/*
 * MedusaInvestigatorUnitForm.java
 *
 * Created on December 24, 2003, 11:37 AM
 */

package edu.mit.coeus.propdev.gui;

import edu.mit.coeus.gui.CoeusFontFactory;
import edu.mit.coeus.irb.bean.ProtocolInvestigatorUnitsBean;
import edu.mit.coeus.irb.bean.ProtocolInvestigatorsBean;
import edu.mit.coeus.utils.*;
import edu.mit.coeus.exception.*;
import edu.mit.coeus.propdev.bean.*;
//import edu.mit.coeus.brokers.RequesterBean;
//import edu.mit.coeus.brokers.ResponderBean;
//import edu.mit.coeus.propdev.bean.ProposalDevelopmentFormBean;
import edu.mit.coeus.award.bean.*;
import edu.mit.coeus.instprop.bean.*;

import javax.swing.*;
import javax.swing.event.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
/**
 *
 * @author  chandrashekara
 */

public class MedusaInvestigatorUnitForm extends javax.swing.JComponent 
implements ListSelectionListener
{
    private  AwardInvestigatorsBean  awardInvestigatorsBean;
    private InstituteProposalInvestigatorBean  instituteProposalInvestigatorBean;
    
    //private ProposalAwardHierarchyForm proposalAwardHierarchyForm;
   // private ProposalAwardHierarchyBean proposalAwardHierarchyBean;
    
    private ProposalAwardHierarchyLinkBean proposalAwardHierarchyLinkBean; 
    private ProposalInvestigatorFormBean proposalInvestigatorFormBean;
//    private ProposalDevelopmentFormBean proposalDevelopmentFormBean;
  //  private ProposalLeadUnitFormBean proposalLeadUnitFormBean;
  //  private final String DEV_PROP_DETAILS = "/ProposalMaintenanceServlet";
//    private final String connectTo = CoeusGuiConstants.CONNECTION_URL+ DEV_PROP_DETAILS;
//    private static final char DISPLAY = 'D';
  //  private static final int HAND_COLUMN = 0;
   // private static final int NAME_COLUMN = 1;
    private static final String EMPTY_STRING = "";
    private InvestigatorTableModel investigatorTableModel;
    private UnitTableModel unitTableModel;
    
    private InvestigatorRenderer investigatorRenderer;
    private IconRenderer iconRenderer;
    private EmptyHeaderRenderer emptyHeaderRenderer; 
    private UnitRenderer unitRenderer;
    
    private Vector vecInvestigatorUnitDetails;
    private Vector vecUnits;
    //private Vector vecUnits;
    //COEUSQA:2653 - Add Protocols to Medusa - Start
    private ProtocolInvestigatorsBean irbProtocolInvestigatorsBean;
    private edu.mit.coeus.iacuc.bean.ProtocolInvestigatorsBean iacucProtocolInvestigatorsBean;
    //COEUSQA:2653 - End
    
    /** Creates new form MedusaInvestigatorUnitForm */
    public MedusaInvestigatorUnitForm() {
   // public MedusaInvestigatorUnitForm(Vector vecInvestigatorUnitDetails, ProposalAwardHierarchyLinkBean proposalAwardHierarchyLinkBean) {
       // this.proposalAwardHierarchyLinkBean = proposalAwardHierarchyLinkBean;
       // this.vecInvestigatorUnitDetails = vecInvestigatorUnitDetails;
        initComponents();
        investigatorRenderer = new InvestigatorRenderer();
        unitRenderer = new UnitRenderer();
        iconRenderer= new IconRenderer();
        emptyHeaderRenderer= new EmptyHeaderRenderer();
        investigatorTableModel = new InvestigatorTableModel();
        tblInvestigator.setModel(investigatorTableModel);
        
        unitTableModel = new UnitTableModel();
        tblUnits.setModel(unitTableModel);
        //setFormData();
        registerComponents();
        setTableHeaders();
        display();
    }
    
    private void registerComponents(){
        tblInvestigator.getSelectionModel().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tblUnits.getSelectionModel().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tblInvestigator.getSelectionModel().addListSelectionListener(this);
        
    }
    
     /**
     * Method used to handle the value change events for the table.
     * @param listSelectionEvent event which delegates selection changes for a
     * table.
     */
    public void valueChanged( ListSelectionEvent listSelectionEvent ) {
        ListSelectionModel source = (ListSelectionModel)listSelectionEvent.getSource();
        int selectedRow;
        ProposalInvestigatorFormBean proposalInvestigatorFormBean;
        AwardInvestigatorsBean  awardInvestigatorsBean;
        InstituteProposalInvestigatorBean  instituteProposalInvestigatorBean;
        
        if(source.equals(tblInvestigator.getSelectionModel())) {
            
            selectedRow= tblInvestigator.getSelectedRow();
            if(selectedRow!=-1){
                 if(proposalAwardHierarchyLinkBean.getBaseType().equals(CoeusConstants.DEV_PROP)){
                    proposalInvestigatorFormBean = (ProposalInvestigatorFormBean)vecInvestigatorUnitDetails.get(selectedRow);
                    unitTableModel.setData(proposalInvestigatorFormBean.getInvestigatorUnits());
                    unitTableModel.fireTableDataChanged();
                }else if(proposalAwardHierarchyLinkBean.getBaseType().equals(CoeusConstants.INST_PROP)){
                    instituteProposalInvestigatorBean = (InstituteProposalInvestigatorBean)vecInvestigatorUnitDetails.get(selectedRow);
                    unitTableModel.setData(instituteProposalInvestigatorBean.getInvestigatorUnits());
                    unitTableModel.fireTableDataChanged();
                }else if(proposalAwardHierarchyLinkBean.getBaseType().equals(CoeusConstants.AWARD)){
                    awardInvestigatorsBean = (AwardInvestigatorsBean)vecInvestigatorUnitDetails.get(selectedRow);
                    unitTableModel.setData(awardInvestigatorsBean.getInvestigatorUnits());
                    unitTableModel.fireTableDataChanged();
                }
                //COEUSQA:2653 - Add Protocols to Medusa - Start
                else if(proposalAwardHierarchyLinkBean.getBaseType().equals(CoeusConstants.IRB_PROTOCOL)){
                     irbProtocolInvestigatorsBean = (ProtocolInvestigatorsBean)vecInvestigatorUnitDetails.get(selectedRow);
                     unitTableModel.setData(irbProtocolInvestigatorsBean.getInvestigatorUnits());
                     unitTableModel.fireTableDataChanged();
                }else if(proposalAwardHierarchyLinkBean.getBaseType().equals(CoeusConstants.IACUC_PROTOCOL)){
                     iacucProtocolInvestigatorsBean = (edu.mit.coeus.iacuc.bean.ProtocolInvestigatorsBean)vecInvestigatorUnitDetails.get(selectedRow);
                     unitTableModel.setData(iacucProtocolInvestigatorsBean.getInvestigatorUnits());
                     unitTableModel.fireTableDataChanged();
                }
                //COEUSQA:2653 - End
            }
        }
    }
    
  
    /** Set the Form Data. Get the data for Investigator and Units
     *Get the data for IP, DP and Awards
     */
    
    public void setFormData(){
        
        if(proposalAwardHierarchyLinkBean.getBaseType() == null) return;
        
        if(proposalAwardHierarchyLinkBean.getBaseType().equals(CoeusConstants.DEV_PROP)){
            if(vecInvestigatorUnitDetails!=null && vecInvestigatorUnitDetails.size() > 0){
                for(int index =0; index < vecInvestigatorUnitDetails.size(); index ++){
                    proposalInvestigatorFormBean = (ProposalInvestigatorFormBean)vecInvestigatorUnitDetails.get(index);
                    vecUnits = proposalInvestigatorFormBean.getInvestigatorUnits();
                }
                
          //  }
            investigatorTableModel.setData(vecInvestigatorUnitDetails);
            investigatorTableModel.fireTableDataChanged();
            tblInvestigator.setRowSelectionInterval(0,0);
            if(vecUnits!=null && vecUnits.size() > 0){
                unitTableModel.setData(vecUnits);
                unitTableModel.fireTableDataChanged();
            }
            }else{
                unitTableModel.setData(null);
                unitTableModel.fireTableDataChanged();
            }
        }else if(proposalAwardHierarchyLinkBean.getBaseType().equals(CoeusConstants.INST_PROP)){
            if(vecInvestigatorUnitDetails!=null && vecInvestigatorUnitDetails.size() > 0){
                for(int instIndex =0; instIndex < vecInvestigatorUnitDetails.size(); instIndex++){
                    instituteProposalInvestigatorBean = (InstituteProposalInvestigatorBean)vecInvestigatorUnitDetails.get(instIndex);
                    vecUnits = instituteProposalInvestigatorBean.getInvestigatorUnits();
                }
           // }
            investigatorTableModel.setData(vecInvestigatorUnitDetails);
            investigatorTableModel.fireTableDataChanged();
            tblInvestigator.setRowSelectionInterval(0,0);
            if(vecUnits!=null && vecUnits.size() > 0){
                unitTableModel.setData(vecUnits);
                unitTableModel.fireTableDataChanged();
            }
            }
        }else if(proposalAwardHierarchyLinkBean.getBaseType().equals(CoeusConstants.AWARD)){
//            CoeusVector cvUnits = null;
            if(vecInvestigatorUnitDetails!=null && vecInvestigatorUnitDetails.size() > 0){
                for(int awIndex =0; awIndex < vecInvestigatorUnitDetails.size(); awIndex ++){
                    awardInvestigatorsBean = (AwardInvestigatorsBean)vecInvestigatorUnitDetails.get(awIndex);
                    vecUnits = awardInvestigatorsBean.getInvestigatorUnits();
                }
                
            //}
            investigatorTableModel.setData(vecInvestigatorUnitDetails);
            investigatorTableModel.fireTableDataChanged();
            tblInvestigator.setRowSelectionInterval(0,0);
            if(vecUnits!=null && vecUnits.size() > 0){
                unitTableModel.setData(vecUnits);
                unitTableModel.fireTableDataChanged();
            }
            }
        }
        //COEUSQA:2653 - Add Protocols to Medusa - Start
        else if(proposalAwardHierarchyLinkBean.getBaseType().equals(CoeusConstants.IRB_PROTOCOL)){
            if(vecInvestigatorUnitDetails!=null && vecInvestigatorUnitDetails.size() > 0){
                for(int awIndex =0; awIndex < vecInvestigatorUnitDetails.size(); awIndex ++){
                    irbProtocolInvestigatorsBean = (ProtocolInvestigatorsBean)vecInvestigatorUnitDetails.get(awIndex);
                    vecUnits = irbProtocolInvestigatorsBean.getInvestigatorUnits();
                }
                investigatorTableModel.setData(vecInvestigatorUnitDetails);
                investigatorTableModel.fireTableDataChanged();
                tblInvestigator.setRowSelectionInterval(0,0);
                if(vecUnits!=null && vecUnits.size() > 0){
                    unitTableModel.setData(vecUnits);
                    unitTableModel.fireTableDataChanged();
                }
            }else {
                unitTableModel.setData(null);
                unitTableModel.fireTableDataChanged();
            }
        }else if(proposalAwardHierarchyLinkBean.getBaseType().equals(CoeusConstants.IACUC_PROTOCOL)){
            if(vecInvestigatorUnitDetails!=null && vecInvestigatorUnitDetails.size() > 0){
                for(int awIndex =0; awIndex < vecInvestigatorUnitDetails.size(); awIndex ++){
                    iacucProtocolInvestigatorsBean = (edu.mit.coeus.iacuc.bean.ProtocolInvestigatorsBean)vecInvestigatorUnitDetails.get(awIndex);
                    vecUnits = iacucProtocolInvestigatorsBean.getInvestigatorUnits();
                }
                investigatorTableModel.setData(vecInvestigatorUnitDetails);
                investigatorTableModel.fireTableDataChanged();
                tblInvestigator.setRowSelectionInterval(0,0);
                if(vecUnits!=null && vecUnits.size() > 0){
                    unitTableModel.setData(vecUnits);
                    unitTableModel.fireTableDataChanged();
                }
            }else{
                unitTableModel.setData(null);
                unitTableModel.fireTableDataChanged();
            }
            
        }
        //COEUSQA:2653 - End
    }
            
    public void display(){
        if(tblInvestigator.getRowCount() > 0){
            tblInvestigator.setRowSelectionInterval(0,0);
        }
        this.setVisible(true);
        
    }
    
    /** Set the  headers for the Investigator and Unit table
     */
    private void setTableHeaders(){
        
        tblInvestigator.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
        tblUnits.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
        
        tblInvestigator.setRowHeight(22);
        tblUnits.setRowHeight(22);
        
        JTableHeader tableHeader = tblInvestigator.getTableHeader();
        tableHeader.setReorderingAllowed(false);
        
        TableColumn column = tblInvestigator.getColumnModel().getColumn(0);
        column.setMinWidth(30);
        column.setMaxWidth(50);
        column.setPreferredWidth(30);
        column.setResizable(false);
        column.setCellRenderer(iconRenderer);
        column.setHeaderRenderer(emptyHeaderRenderer);
        
        column = tblInvestigator.getColumnModel().getColumn(1);
        column.setHeaderRenderer(emptyHeaderRenderer);
        column.setCellRenderer(investigatorRenderer);
        column.setMinWidth(210);
        column.setMaxWidth(350);
        column.setPreferredWidth(220);
        
        // Table header for the Unit details
        JTableHeader unitTableHeader = tblUnits.getTableHeader();
        unitTableHeader.setReorderingAllowed(false);
        
        TableColumn unitColumn = tblUnits.getColumnModel().getColumn(0);
        unitColumn.setResizable(false);
        unitColumn.setHeaderRenderer(emptyHeaderRenderer);
        unitColumn.setCellRenderer(unitRenderer);
        unitColumn.setMinWidth(300);
        unitColumn.setMaxWidth(350);
        unitColumn.setPreferredWidth(300);
    }
    
    /* Table model for the Investigator details.Specifies the investigators for the respective
     *Award,Institute Proposal, Development Proposal and SubContract details
     */
    private class InvestigatorTableModel extends AbstractTableModel{
        private String colNames[] = {"",""};
        private Class colClass[] = {ImageIcon.class, String.class};
        
        InvestigatorTableModel(){
        }
        
        public boolean isCellEditable(int row,int column){
            return false;
        }
        
        public int getColumnCount() {
            return colNames.length;
        }
        public int getRowCount() {
            if(vecInvestigatorUnitDetails==null)return 0;
            return vecInvestigatorUnitDetails.size();
        }
        public void setData(Vector data){
            if(vecInvestigatorUnitDetails==null) return;
            vecInvestigatorUnitDetails = data;
        }
        
        public Class getColumnClass(int columnIndex) {
            return colClass [columnIndex];
        }
        
        public String getColumnName(int column) {
            return colNames[column];
        }
        
        public Object getValueAt(int row, int column) {
            switch(column){
                case 0:
                    return EMPTY_STRING;
                case 1:
                    if(proposalAwardHierarchyLinkBean.getBaseType().equals(CoeusConstants.DEV_PROP)){
                        ProposalInvestigatorFormBean proposalInvestigatorFormBean = (ProposalInvestigatorFormBean)vecInvestigatorUnitDetails.get(row);
                        //return new String(proposalInvestigatorFormBean.getPersonName());
                         return proposalInvestigatorFormBean.getPersonName();
                        
                    }else if(proposalAwardHierarchyLinkBean.getBaseType().equals(CoeusConstants.AWARD)){
                        AwardInvestigatorsBean  awardInvestigatorsBean = (AwardInvestigatorsBean)vecInvestigatorUnitDetails.get(row);
                        //return new String(awardInvestigatorsBean.getPersonName());
                        return awardInvestigatorsBean.getPersonName();
                    }else if(proposalAwardHierarchyLinkBean.getBaseType().equals(CoeusConstants.INST_PROP)){
                        InstituteProposalInvestigatorBean  instituteProposalInvestigatorBean = (InstituteProposalInvestigatorBean)vecInvestigatorUnitDetails.get(row);
                        //return new String(instituteProposalInvestigatorBean.getPersonName());
                        return instituteProposalInvestigatorBean.getPersonName();
                    }
                    //COEUSQA:2653 - Add Protocols to Medusa - Start
                    else if(proposalAwardHierarchyLinkBean.getBaseType().equals(CoeusConstants.IRB_PROTOCOL)){
                        ProtocolInvestigatorsBean  irbProtocolInvestigatorsBean = (ProtocolInvestigatorsBean)vecInvestigatorUnitDetails.get(row);
                        return irbProtocolInvestigatorsBean.getPersonName();
                    }else if(proposalAwardHierarchyLinkBean.getBaseType().equals(CoeusConstants.IACUC_PROTOCOL)){
                        edu.mit.coeus.iacuc.bean.ProtocolInvestigatorsBean  iacucProtocolInvestigatorsBean = (edu.mit.coeus.iacuc.bean.ProtocolInvestigatorsBean)vecInvestigatorUnitDetails.get(row);
                        return iacucProtocolInvestigatorsBean.getPersonName();
                    }                    
                    //COEUSQA:2653 - End
            }
            return EMPTY_STRING;
        }
    }// End of Inner class InvestigatorTableModel............
    
    /* Table model for the Unit details.Specifies the unit details for the corresponding
     investigators for the respective Award,Institute Proposal,
     Development Proposal and SubContract details.
     */
    private class UnitTableModel extends AbstractTableModel implements TableModel{
        private String colNames[] = {""};
        private Class colClass[] = {String.class};
        
        private static final String COLON = "  :  ";
        
        UnitTableModel(){
        }
        
        public boolean isCellEditable(int row,int column){
            return false;
        }
        
        public int getColumnCount() {
            return colNames.length;
        }
        
        public int getRowCount() {
            if(vecUnits==null) return 0;
            return vecUnits.size();
        }
        
        public Class getColumnClass(int columnIndex) {
            return colClass [columnIndex];
        }
        
        public String getColumnName(int column) {
            return colNames[column];
        }
        
        public void setData(Vector data){
            if(vecUnits==null) return;
            vecUnits = data;
        }
        
        public Object getValueAt(int row, int column) {
            switch(column){
                case 0:
                    if(proposalAwardHierarchyLinkBean.getBaseType().equals(CoeusConstants.DEV_PROP)){
                        ProposalLeadUnitFormBean proposalLeadUnitFormBean = (ProposalLeadUnitFormBean)vecUnits.get(row);
                        return proposalLeadUnitFormBean.getUnitNumber() + COLON + proposalLeadUnitFormBean.getUnitName();
                    }else if(proposalAwardHierarchyLinkBean.getBaseType().equals(CoeusConstants.AWARD)){
                        AwardUnitBean awardUnitBean = (AwardUnitBean)vecUnits.get(row);
                        return awardUnitBean.getUnitNumber() + COLON + awardUnitBean.getUnitName();
                    }else if(proposalAwardHierarchyLinkBean.getBaseType().equals(CoeusConstants.INST_PROP)){
                        InstituteProposalUnitBean instituteProposalUnitBean = (InstituteProposalUnitBean)vecUnits.get(row);
                        return instituteProposalUnitBean.getUnitNumber() + COLON + instituteProposalUnitBean.getUnitName();
                    }
                    //COEUSQA:2653 - Add Protocols to Medusa - Start
                    else if(proposalAwardHierarchyLinkBean.getBaseType().equals(CoeusConstants.IRB_PROTOCOL)){
                        ProtocolInvestigatorUnitsBean irbProtocolInvestigatorUnitsBean = (ProtocolInvestigatorUnitsBean)vecUnits.get(row);
                        return irbProtocolInvestigatorUnitsBean.getUnitNumber() + COLON + irbProtocolInvestigatorUnitsBean.getUnitName();
                    }
                    else if(proposalAwardHierarchyLinkBean.getBaseType().equals(CoeusConstants.IACUC_PROTOCOL)){
                       edu.mit.coeus.iacuc.bean.ProtocolInvestigatorUnitsBean iacucProtocolInvestigatorUnitsBean = (edu.mit.coeus.iacuc.bean.ProtocolInvestigatorUnitsBean)vecUnits.get(row); 
                       return iacucProtocolInvestigatorUnitsBean.getUnitNumber() + COLON + iacucProtocolInvestigatorUnitsBean.getUnitName();
                    }
                    //COEUSQA:2653 - End
            }
            return EMPTY_STRING;
        }
        
    }// End of Inner class UnitTableModel............
    
    /** Renderer for the Investigator table. Show red color who is the
     *Primary Investigator
     */
    private class InvestigatorRenderer extends DefaultTableCellRenderer {
        public Component getTableCellRendererComponent(JTable table,
        Object value, boolean isSelected, boolean hasFocus, int row,
        int column) {
            
            if(proposalAwardHierarchyLinkBean.getBaseType().equals(CoeusConstants.DEV_PROP)){
                ProposalInvestigatorFormBean proposalInvestigatorFormBean = (ProposalInvestigatorFormBean)vecInvestigatorUnitDetails.get(row);
                setText((String)value);
                if(proposalInvestigatorFormBean.isPrincipleInvestigatorFlag()){
                    setForeground(Color.red);
                }else {
                    setForeground(Color.black);
                }
            }else if(proposalAwardHierarchyLinkBean.getBaseType().equals(CoeusConstants.AWARD)){
                //ProposalInvestigatorFormBean proposalInvestigatorFormBean = (ProposalInvestigatorFormBean)vecInvestigatorUnitDetails.get(row);
                AwardInvestigatorsBean  awardInvestigatorsBean  = (AwardInvestigatorsBean)vecInvestigatorUnitDetails.get(row);
                setText((String)value);
                if(awardInvestigatorsBean.isPrincipalInvestigatorFlag()){
                    setForeground(Color.red);
                }else {
                    setForeground(Color.black);
                }
            }else if(proposalAwardHierarchyLinkBean.getBaseType().equals(CoeusConstants.INST_PROP)){
                InstituteProposalInvestigatorBean  instituteProposalInvestigatorBean  = (InstituteProposalInvestigatorBean)vecInvestigatorUnitDetails.get(row);
                setText((String)value);
                if(instituteProposalInvestigatorBean.isPrincipalInvestigatorFlag()){
                    setForeground(Color.red);
                }else {
                    setForeground(Color.black);
                }
            }
            //COEUSQA:2653 - Add Protocols to Medusa - Start
            else if(proposalAwardHierarchyLinkBean.getBaseType().equals(CoeusConstants.IRB_PROTOCOL)){
                 ProtocolInvestigatorsBean irbProtocolInvestigatorsBean  = (ProtocolInvestigatorsBean)vecInvestigatorUnitDetails.get(row);
                setText((String)value);
                if(irbProtocolInvestigatorsBean.isPrincipalInvestigatorFlag()){
                    setForeground(Color.red);
                }else {
                    setForeground(Color.black);
                }
            }else if(proposalAwardHierarchyLinkBean.getBaseType().equals(CoeusConstants.IACUC_PROTOCOL)){
                edu.mit.coeus.iacuc.bean.ProtocolInvestigatorsBean iacucProtocolInvestigatorsBean  = (edu.mit.coeus.iacuc.bean.ProtocolInvestigatorsBean)vecInvestigatorUnitDetails.get(row);
                setText((String)value);
                if(iacucProtocolInvestigatorsBean.isPrincipalInvestigatorFlag()){
                    setForeground(Color.red);
                }else {
                    setForeground(Color.black);
                }
            }
            //COEUSQA:2653 - End
            return this;
        }
    }
    
    
    /** Renderer for the Unit table. Show red color for the Lead Unit
     */
    private class UnitRenderer extends DefaultTableCellRenderer {
        public Component getTableCellRendererComponent(JTable table,
        Object value, boolean isSelected, boolean hasFocus, int row,
        int column) {
            if(proposalAwardHierarchyLinkBean.getBaseType().equals(CoeusConstants.DEV_PROP)){
                ProposalLeadUnitFormBean proposalLeadUnitFormBean = (ProposalLeadUnitFormBean)vecUnits.get(row);
                setText((String)value);
                if( proposalLeadUnitFormBean.isLeadUnitFlag() ) {
                    setForeground(Color.red);
                }else {
                    setForeground(Color.black);
                }
            }else if(proposalAwardHierarchyLinkBean.getBaseType().equals(CoeusConstants.AWARD)){
                AwardUnitBean awardUnitBean = (AwardUnitBean)vecUnits.get(row);
                setText((String)value);
                if( awardUnitBean.isLeadUnitFlag()) {
                    setForeground(Color.red);
                }else {
                    setForeground(Color.black);
                }
            }else if(proposalAwardHierarchyLinkBean.getBaseType().equals(CoeusConstants.INST_PROP)){
                InstituteProposalUnitBean instituteProposalUnitBean = (InstituteProposalUnitBean)vecUnits.get(row);
                setText((String)value);
                if( instituteProposalUnitBean.isLeadUnitFlag()) {
                    setForeground(Color.red);
                }else {
                    setForeground(Color.black);
                }
            }
            //COEUSQA:2653 - Add Protocols to Medusa - Start
            else if(proposalAwardHierarchyLinkBean.getBaseType().equals(CoeusConstants.IRB_PROTOCOL)){
                ProtocolInvestigatorUnitsBean irbProtocolInvestigatorUnitsBean = (ProtocolInvestigatorUnitsBean)vecUnits.get(row);
                setText((String)value);
                if( irbProtocolInvestigatorUnitsBean.isLeadUnitFlag()) {
                    setForeground(Color.red);
                }else {
                    setForeground(Color.black);
                }
            }else if(proposalAwardHierarchyLinkBean.getBaseType().equals(CoeusConstants.IACUC_PROTOCOL)){
                edu.mit.coeus.iacuc.bean.ProtocolInvestigatorUnitsBean iacucProtocolInvestigatorUnitsBean = (edu.mit.coeus.iacuc.bean.ProtocolInvestigatorUnitsBean)vecUnits.get(row);
                setText((String)value);
                if( iacucProtocolInvestigatorUnitsBean.isLeadUnitFlag()) {
                    setForeground(Color.red);
                }else {
                    setForeground(Color.black);
                }
            }
            //COEUSQA:2653 - End
            return this;
            
        }
    }
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    private void initComponents() {//GEN-BEGIN:initComponents
        pnlInvestigatorUnit = new javax.swing.JPanel();
        scrPnInvestigator = new javax.swing.JScrollPane();
        tblInvestigator = new javax.swing.JTable();
        jcrPnUnits = new javax.swing.JScrollPane();
        tblUnits = new javax.swing.JTable();

        setLayout(new java.awt.BorderLayout());

        pnlInvestigatorUnit.setLayout(new java.awt.GridLayout(1, 2, 0, 5));

        scrPnInvestigator.setBorder(new javax.swing.border.TitledBorder(new javax.swing.border.EtchedBorder(), "Investigators", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.BELOW_TOP, CoeusFontFactory.getLabelFont()));
        scrPnInvestigator.setToolTipText("");
        scrPnInvestigator.setPreferredSize(new java.awt.Dimension(350, 260));
        tblInvestigator.setBackground((java.awt.Color) javax.swing.UIManager.getDefaults().get("Panel.background"));
        tblInvestigator.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Title 1"
            }
        ));
        tblInvestigator.setSelectionBackground((java.awt.Color) javax.swing.UIManager.getDefaults().get("Panel.background"));
        tblInvestigator.setShowHorizontalLines(false);
        tblInvestigator.setShowVerticalLines(false);
        scrPnInvestigator.setViewportView(tblInvestigator);

        pnlInvestigatorUnit.add(scrPnInvestigator);

        jcrPnUnits.setBorder(new javax.swing.border.TitledBorder(new javax.swing.border.EtchedBorder(), "Units", javax.swing.border.TitledBorder.LEFT, javax.swing.border.TitledBorder.BELOW_TOP, CoeusFontFactory.getLabelFont()));
        jcrPnUnits.setPreferredSize(new java.awt.Dimension(350, 260));
        tblUnits.setBackground((java.awt.Color) javax.swing.UIManager.getDefaults().get("Panel.background"));
        tblUnits.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Title 1"
            }
        ));
        tblUnits.setSelectionBackground((java.awt.Color) javax.swing.UIManager.getDefaults().get("Panel.background"));
        tblUnits.setShowHorizontalLines(false);
        tblUnits.setShowVerticalLines(false);
        jcrPnUnits.setViewportView(tblUnits);

        pnlInvestigatorUnit.add(jcrPnUnits);

        add(pnlInvestigatorUnit, java.awt.BorderLayout.CENTER);

    }//GEN-END:initComponents
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    protected javax.swing.JScrollPane jcrPnUnits;
    protected javax.swing.JPanel pnlInvestigatorUnit;
    protected javax.swing.JScrollPane scrPnInvestigator;
    protected javax.swing.JTable tblInvestigator;
    protected javax.swing.JTable tblUnits;
    // End of variables declaration//GEN-END:variables
    
    /**
     * Inner class which is used to provide empty header for the Icon Column.
     */
    
    class EmptyHeaderRenderer extends JList implements TableCellRenderer {
        /**
         * Default constructor to set the default foreground/background
         * and border properties of this renderer for a cell.
         */
        EmptyHeaderRenderer() {
            setOpaque(true);
            setForeground(UIManager.getColor("TableHeader.foreground"));
            setBackground(UIManager.getColor("TableHeader.background"));
            setBorder(new javax.swing.border.EmptyBorder(0, 0, 0, 0));
            ListCellRenderer renderer = getCellRenderer();
            ((JLabel) renderer).setHorizontalAlignment(JLabel.CENTER);
            setCellRenderer(renderer);
        }
        
        public Component getTableCellRendererComponent(JTable table,
        Object value,boolean isSelected, boolean hasFocus, int row, int column){
            return this;
        }
    }
    
    public void setDataValues(Vector vecInvestigators,ProposalAwardHierarchyLinkBean proposalAwardHierarchyLinkBean){
        this.vecInvestigatorUnitDetails = vecInvestigators;
        this.proposalAwardHierarchyLinkBean = proposalAwardHierarchyLinkBean;
        
    }
}
