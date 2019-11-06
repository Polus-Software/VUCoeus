/**
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 */
/*
 * MapMaintenanceBaseWindow.java
 *
 * Created on October 17, 2005, 10:01 AM
 */

package edu.mit.coeus.mapsrules.gui;

import java.awt.event.*;
import java.util.Vector;
import javax.swing.*;
import javax.swing.event.*;

import edu.mit.coeus.gui.*;
import edu.mit.coeus.gui.menu.*;
import edu.mit.coeus.utils.CoeusGuiConstants;
import edu.mit.coeus.gui.toolbar.*;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
/**
 *
 * @author  shijiv
 */
public class MapMaintenanceBaseWindow extends CoeusInternalFrame{
    private String title;
    private CoeusAppletMDIForm mdiForm;
    public  CoeusMenu mnuFile, mnuEdit;
    public  CoeusMenuItem mnuItmAdd,mnuItmModify,mnuItmDelete;
    public  CoeusToolBarButton btnAdd, btnModify,btnDelete, btnClose;
    public JTable tblMap,tblMapDetails;
    public JScrollPane scrPnMap,scrPnMapDetails;
    public JPanel panel;
    
    public javax.swing.JPanel pnlSelectedMapList;
    public javax.swing.JPanel pnlSelectedMapListHeaderRow;
    public javax.swing.JPanel pnlSelectedMapListPanelRow;
    public javax.swing.JSeparator sptrSelectedMapListTop;
    public javax.swing.JLabel lblSelectedMapListHeaderData;
    public javax.swing.JLabel lblSelectedMapDescr;
    public javax.swing.JSeparator sptrSelectedMapList;
    public javax.swing.JLabel lblSelectedMapListApproveBy;
    public javax.swing.JLabel lblSelectedMapListUserId;
    public javax.swing.JLabel lblSelectedMapListDescription;
    
    
    /** Creates a new instance of MapMaintenanceBaseWindow */
    public MapMaintenanceBaseWindow(String title, CoeusAppletMDIForm mdiForm) {
        super(title, mdiForm);
        this.title=title;
        this.mdiForm = mdiForm;
        initComponents();
    }
    
    public void saveActiveSheet() {
    }
    
    public void saveAsActiveSheet() {
    }
    
    private void initComponents() {
        setFrameToolBar(getMapsToolBar());
        prepareMenu();
        java.awt.GridBagConstraints gridBagConstraints;
        panel= new JPanel();
        panel.setPreferredSize(new Dimension(this.getWidth(),this.getHeight()));
        panel.setMinimumSize(new Dimension(this.getWidth()-200,this.getHeight()-200));
        panel.setLayout(new java.awt.GridBagLayout());
        getContentPane().add(panel);
        tblMap = new JTable();
        
        scrPnMap = new JScrollPane();
        scrPnMap.setPreferredSize(new Dimension(this.getWidth()-23, (this.getHeight()/4)+10));
        scrPnMap.setMinimumSize(new Dimension(this.getWidth()-43, (this.getHeight()/4)-80));
        scrPnMap.getViewport().setBackground(Color.white);
        scrPnMap.setBorder(new javax.swing.border.BevelBorder(javax.swing.border.BevelBorder.LOWERED));
        scrPnMap.setViewportView(tblMap);
        
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(0, 2, 0, 0);
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 0.5;
        gridBagConstraints.weighty = 0.5;
        panel.add(scrPnMap, gridBagConstraints);
        
        pnlSelectedMapList = new javax.swing.JPanel();
        pnlSelectedMapListHeaderRow = new javax.swing.JPanel();
        pnlSelectedMapListPanelRow = new javax.swing.JPanel();
        pnlSelectedMapList.setLayout(new javax.swing.BoxLayout(pnlSelectedMapList, javax.swing.BoxLayout.Y_AXIS));

        pnlSelectedMapList.setBackground(new java.awt.Color(255, 255, 255));
        pnlSelectedMapList.setBorder(null);
        pnlSelectedMapListHeaderRow.setLayout(new java.awt.GridBagLayout());

        pnlSelectedMapListHeaderRow.setBackground(new java.awt.Color(255, 255, 255));
        pnlSelectedMapListHeaderRow.setMaximumSize(new Dimension(this.getWidth()-23, 22));
        pnlSelectedMapListHeaderRow.setMinimumSize(new Dimension(this.getWidth()-43, 22));
        //Modified for Coeus 4.3 Routing enhancement -PT ID:2785 - start
        //Reduced the width to avoid horizontal scrollbar
        //pnlSelectedMapListHeaderRow.setPreferredSize(new Dimension(this.getWidth()-23, 22));
        pnlSelectedMapListHeaderRow.setPreferredSize(new Dimension(this.getWidth()-50, 22));
        //Modified for Coeus 4.3 Routing enhancement -PT ID:2785 - end
        
        sptrSelectedMapListTop = new javax.swing.JSeparator();
        sptrSelectedMapListTop.setMaximumSize(new Dimension(this.getWidth()-23, 1));
        sptrSelectedMapListTop.setMinimumSize(new Dimension(this.getWidth()-43, 1));
        sptrSelectedMapListTop.setPreferredSize(new Dimension(this.getWidth()-23, 1));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weighty = 1.0;
        //pnlSelectedMapListHeaderRow.add(sptrSelectedMapListTop, gridBagConstraints);
        
        lblSelectedMapListHeaderData = new javax.swing.JLabel();
        lblSelectedMapListHeaderData.setFont(CoeusFontFactory.getNormalFont());
        lblSelectedMapListHeaderData.setForeground(Color.blue);
        lblSelectedMapListHeaderData.setMaximumSize(new java.awt.Dimension(32767, 32767));
        lblSelectedMapListHeaderData.setMinimumSize(new Dimension(190,16));
        lblSelectedMapListHeaderData.setPreferredSize(new Dimension(190,16));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.insets = new java.awt.Insets(0, 10, 0, 0);
        pnlSelectedMapListHeaderRow.add(lblSelectedMapListHeaderData, gridBagConstraints);
        
        sptrSelectedMapList = new javax.swing.JSeparator();
        sptrSelectedMapList.setMaximumSize(new Dimension(this.getWidth()-23, 1));
        sptrSelectedMapList.setMinimumSize(new Dimension(this.getWidth()-43, 1));
        sptrSelectedMapList.setPreferredSize(new Dimension(this.getWidth()-23, 1));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.insets = new java.awt.Insets(2, 0, 0, 0);
       //pnlSelectedMapListHeaderRow.add(sptrSelectedMapList, gridBagConstraints);
        
        lblSelectedMapDescr = new javax.swing.JLabel();
        lblSelectedMapDescr.setFont(CoeusFontFactory.getNormalFont());
        lblSelectedMapDescr.setForeground(Color.blue);
        lblSelectedMapDescr.setMaximumSize(new java.awt.Dimension(50, 16));
        lblSelectedMapDescr.setMinimumSize(new java.awt.Dimension(50, 16));
        lblSelectedMapDescr.setPreferredSize(new java.awt.Dimension(50, 16));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        pnlSelectedMapListHeaderRow.add(lblSelectedMapDescr, gridBagConstraints);

        pnlSelectedMapList.add(pnlSelectedMapListHeaderRow);
        
        pnlSelectedMapListPanelRow.setLayout(new java.awt.GridBagLayout());

        pnlSelectedMapListPanelRow.setBackground(new java.awt.Color(255, 255, 255));
        pnlSelectedMapListPanelRow.setMaximumSize(new Dimension(this.getWidth()-23, 30));
        pnlSelectedMapListPanelRow.setMinimumSize(new Dimension(this.getWidth()-43, 30));
        //Modified for Coeus 4.3 Routing enhancement -PT ID:2785 - start
        //Reduced the width to avoid horizontal scrollbar
        //pnlSelectedMapListPanelRow.setPreferredSize(new Dimension(this.getWidth()-23, 30));
        pnlSelectedMapListPanelRow.setPreferredSize(new Dimension(this.getWidth()-50, 30));
        //Modified for Coeus 4.3 Routing enhancement -PT ID:2785 - end
        lblSelectedMapListApproveBy = new javax.swing.JLabel();
        lblSelectedMapListApproveBy.setFont(CoeusFontFactory.getNormalFont());
        lblSelectedMapListApproveBy.setForeground(Color.red);
        lblSelectedMapListApproveBy.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        //lblSelectedMapListApproveBy.setText("Approve By");
        lblSelectedMapListApproveBy.setVerticalAlignment(javax.swing.SwingConstants.TOP);
        lblSelectedMapListApproveBy.setMaximumSize(new java.awt.Dimension(85, 20));
        lblSelectedMapListApproveBy.setMinimumSize(new java.awt.Dimension(85, 20));
        lblSelectedMapListApproveBy.setPreferredSize(new java.awt.Dimension(85, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets=new java.awt.Insets(0, 12, 0, 0);
        pnlSelectedMapListPanelRow.add(lblSelectedMapListApproveBy, gridBagConstraints);
        
        lblSelectedMapListUserId = new javax.swing.JLabel();
        lblSelectedMapListUserId.setFont(CoeusFontFactory.getNormalFont());
        //lblSelectedMapListUserId.setText("user");
        lblSelectedMapListUserId.setVerticalAlignment(javax.swing.SwingConstants.TOP);
        lblSelectedMapListUserId.setMaximumSize(new java.awt.Dimension(100, 20));
        lblSelectedMapListUserId.setMinimumSize(new java.awt.Dimension(100, 20));
        lblSelectedMapListUserId.setPreferredSize(new java.awt.Dimension(100, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weighty = 1.0;
        pnlSelectedMapListPanelRow.add(lblSelectedMapListUserId, gridBagConstraints);
        
        lblSelectedMapListDescription = new javax.swing.JLabel();
        lblSelectedMapListDescription.setFont(CoeusFontFactory.getNormalFont());
        //lblSelectedMapListDescription.setText("Description");
        lblSelectedMapListDescription.setVerticalAlignment(javax.swing.SwingConstants.TOP);
        lblSelectedMapListDescription.setMaximumSize(new java.awt.Dimension(32767, 32767));
        lblSelectedMapListDescription.setMinimumSize(new java.awt.Dimension(520, 20));
        lblSelectedMapListDescription.setPreferredSize(new java.awt.Dimension(520, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets=new java.awt.Insets(0, 10, 0, 0);
        pnlSelectedMapListPanelRow.add(lblSelectedMapListDescription, gridBagConstraints);

        pnlSelectedMapList.add(pnlSelectedMapListPanelRow);
        
        scrPnMapDetails = new JScrollPane();
        scrPnMapDetails.setPreferredSize(new Dimension(this.getWidth()-23, this.getHeight()-((this.getHeight()/4)+57)));
        scrPnMapDetails.setMinimumSize(new Dimension(this.getWidth()-43, (this.getHeight()-((this.getHeight()/4)))+120));
        scrPnMapDetails.getViewport().setBackground(Color.white);
        scrPnMapDetails.setBorder(new javax.swing.border.BevelBorder(javax.swing.border.BevelBorder.LOWERED));
        scrPnMapDetails.setVerticalScrollBarPolicy(javax.swing.JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrPnMapDetails.setViewportView(pnlSelectedMapList);
        
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = GridBagConstraints.REMAINDER;
        gridBagConstraints.gridheight = 3;
        gridBagConstraints.insets = new java.awt.Insets(8, 2, 5, 0);
        gridBagConstraints.anchor = java.awt.GridBagConstraints.SOUTHWEST;
        gridBagConstraints.weightx = 0.5;
        gridBagConstraints.weighty = 0.5;
        panel.add(scrPnMapDetails, gridBagConstraints);
    }
     
    //  To get the toolbar for Map maintainance base window
     public JToolBar getMapsToolBar() {
         JToolBar toolBar = new JToolBar();
        
        btnAdd = new CoeusToolBarButton(new ImageIcon(
        getClass().getClassLoader().getResource(CoeusGuiConstants.ADD_ICON)),
        null, "Add Unit Map");
        
        btnModify= new CoeusToolBarButton(new ImageIcon(
        getClass().getClassLoader().getResource(CoeusGuiConstants.EDIT_ICON)),
        null, "Modify Unit Map");
        
        btnDelete = new CoeusToolBarButton(new ImageIcon(
        getClass().getClassLoader().getResource(CoeusGuiConstants.DELETE_ICON)),
        null, "Delete Unit Map");
        
        btnClose = new CoeusToolBarButton(new ImageIcon(
        getClass().getClassLoader().getResource(CoeusGuiConstants.CLOSE_ICON)),
        null, "Close");
        
        toolBar.add(btnAdd);
        toolBar.add(btnModify);
        toolBar.add(btnDelete);
        toolBar.addSeparator();
        toolBar.add(btnClose);
        toolBar.addSeparator();
       
        return toolBar;
     }
    
     //To set the edit menu 
     public void prepareMenu() {
         if(mnuEdit == null){
             // Holds the data for the Edit Menu.
             Vector vecEditMenu = new Vector();
             // Build the menu item for the Edit Menu
             
             mnuItmAdd= new CoeusMenuItem("Add",null,true,true);
             mnuItmAdd.setMnemonic('A');
             
             mnuItmModify= new CoeusMenuItem("Modify",null,true,true);
             mnuItmModify.setMnemonic('M');
             
             mnuItmDelete= new CoeusMenuItem("Delete",null,true,true);
             mnuItmDelete.setMnemonic('D');
             
             vecEditMenu.add(mnuItmAdd);
             vecEditMenu.add(mnuItmModify);
             vecEditMenu.add(mnuItmDelete);
             
             mnuEdit = new CoeusMenu("Edit", null, vecEditMenu, true, true);
             mnuEdit.setMnemonic('E');
             
         }
     }
     
     //To load the menu
     private void loadMenus() {
       mdiForm.getCoeusMenuBar().add(mnuEdit, 1);
    }
    
    // To unload the menu
    private void unloadMenus() {
        mdiForm.removeMenu(mnuEdit);
    }
    
    public void internalFrameActivated(InternalFrameEvent e) {
        super.internalFrameActivated(e);
        loadMenus();
    }
    
    
    public void internalFrameDeactivated(InternalFrameEvent e) {
        unloadMenus();
        super.internalFrameDeactivated(e);
        mdiForm.getCoeusMenuBar().revalidate();
    }
    

}
