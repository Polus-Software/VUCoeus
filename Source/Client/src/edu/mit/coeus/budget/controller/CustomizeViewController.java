/*
 * CustomizeViewController.java
 *
 * Created on December 2, 2003, 11:16 AM
 */

package edu.mit.coeus.budget.controller;

/** Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 * @author sharathk
 */

import java.util.Enumeration;

import java.awt.Frame;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;

import edu.mit.coeus.gui.CoeusDlgWindow;
import edu.mit.coeus.gui.CoeusFontFactory;
import edu.vanderbilt.coeus.utils.CustomFunctions;
import edu.mit.coeus.budget.gui.CustomizeViewForm;

public class CustomizeViewController extends Controller
implements ChangeListener{
    
    private CustomizeViewForm customizeViewForm = new CustomizeViewForm();
    private CoeusDlgWindow coeusDlgWindow;
    
    private JCheckBox chkComponents[] = {
        customizeViewForm.chkCostElement,
        customizeViewForm.chkCostElementDescription,
        customizeViewForm.chkLineItemDesccription,
        customizeViewForm.chkQuantity,
        customizeViewForm.chkCost,
        customizeViewForm.chkStartDate,
        customizeViewForm.chkEndDate,
                
        customizeViewForm.chkCategory,
        customizeViewForm.chkUnderrecovery,
        customizeViewForm.chkCostShare,
        customizeViewForm.chkCampusFlag        
        };
    
    private static final String TITLE = "Customize View";

    private static final int WIDTH = 500;
    private static final int HEIGHT = 286;//COEUSDEV:241
    
    /** Creates a new instance of CustomizeViewController */
    public CustomizeViewController(Frame owner, boolean modal) {
        //instance creation of CustomizeViewForm moved to first line since 
        //checkbox array needs its instance.
        coeusDlgWindow = new CoeusDlgWindow(owner, modal);
        
        coeusDlgWindow.setSize(WIDTH, HEIGHT);
        coeusDlgWindow.setResizable(false);
        coeusDlgWindow.getContentPane().add(customizeViewForm);
        coeusDlgWindow.setDefaultCloseOperation(CoeusDlgWindow.HIDE_ON_CLOSE);
        coeusDlgWindow.setTitle(TITLE);
        
        registerComponents();
        formatFields();
    }

    public void display() {
        customizeViewForm.btnApply.setEnabled(false);
        coeusDlgWindow.setLocation(CoeusDlgWindow.CENTER);
        coeusDlgWindow.setVisible(true);
    }
    
    public void setVisible(boolean visible) {
        coeusDlgWindow.setVisible(visible);
    }
    
    public void formatFields() {
        customizeViewForm.chkCostElement.setSelected(true);
        customizeViewForm.chkCostElementDescription.setSelected(true);
        customizeViewForm.chkLineItemDesccription.setSelected(true);
        customizeViewForm.chkStartDate.setSelected(true);
        customizeViewForm.chkEndDate.setSelected(true);
        customizeViewForm.chkCost.setSelected(true);
        customizeViewForm.chkQuantity.setSelected(true);
        
        customizeViewForm.chkCategory.setSelected(false);
        customizeViewForm.chkUnderrecovery.setSelected(false);
        customizeViewForm.chkCostShare.setSelected(false);
        customizeViewForm.chkCampusFlag.setSelected(false);
        
        /* JM 5-25-2016 get show calculated amounts parameter */
    	CustomFunctions custom = new CustomFunctions();
    	String[] params = (String[]) custom.getParameterValues("BUDGET_SHOW_CALCULATED_AMTS");
    	boolean showCalculatedAmts = "1".equals(params[0]);
    	customizeViewForm.chkShowCalculatedAmts.setSelected(showCalculatedAmts); 
    	/* JM END */
    }
    
    /**
     * returns count of checked checkbox.
     */
    public int getSelectedCount() {
        int selected = 0;
        for(int index = 0; index < chkComponents.length; index++) {
            selected = chkComponents[index].isSelected() ? selected + 1 : selected;
        }
        return selected;
    }
    
    public JCheckBox[] getCheckBoxComponents() {
        return chkComponents;
    }
    
    public java.awt.Component getControlledUI() {
        return customizeViewForm;
    }
    
    public Object getFormData() {
        return null;
    }
    
    public void registerComponents() {
        customizeViewForm.rdBtnCategory.addChangeListener(this);
        customizeViewForm.rdBtnDefault.addChangeListener(this);
        for(int index = 0; index < chkComponents.length; index++) {
            chkComponents[index].addChangeListener(this);
        }
        
        //Escape key listener
        coeusDlgWindow.addEscapeKeyListener(
        new AbstractAction("escPressed"){
            public void actionPerformed(ActionEvent ae){
                coeusDlgWindow.setVisible(false);
            }
        });

    }
    
    public void saveFormData() {
    }
    
    public void setFormData(Object data) {
    }
    
    public boolean validate() throws edu.mit.coeus.exception.CoeusUIException {
        return true;
    }
    
    public void refresh() {
        formatFields();
    }
    
    public void stateChanged(ChangeEvent changeEvent) {
        if(! customizeViewForm.btnApply.isEnabled()) {
            customizeViewForm.btnApply.setEnabled(true);
        }
        
        if(customizeViewForm.rdBtnCategory.isSelected()) {
            formatFields();
            setEnabled(false);
        }else if(customizeViewForm.rdBtnDefault.isSelected()) {
            setEnabled(true);
        }
    }
    
    private void setEnabled(boolean enable) {
        customizeViewForm.chkCostElement.setEnabled(enable);
        customizeViewForm.chkCostElementDescription.setEnabled(enable);
        customizeViewForm.chkLineItemDesccription.setEnabled(enable);
        customizeViewForm.chkStartDate.setEnabled(enable);
        customizeViewForm.chkEndDate.setEnabled(enable);
        customizeViewForm.chkCost.setEnabled(enable);
        customizeViewForm.chkQuantity.setEnabled(enable);
        
        customizeViewForm.chkCategory.setEnabled(enable);
        customizeViewForm.chkUnderrecovery.setEnabled(enable);
        customizeViewForm.chkCostShare.setEnabled(enable);
        customizeViewForm.chkCampusFlag.setEnabled(enable);
    }
    
    /**
     * sets Radio button states
     */
    public void setRadioButtonState(boolean selected[]) {
        if(selected.length != customizeViewForm.btnGrpViews.getButtonCount()) return ;
        
        int index = 0;
        Enumeration enumeration =  customizeViewForm.btnGrpViews.getElements();
        while(enumeration.hasMoreElements()) {
            ((JRadioButton)enumeration.nextElement()).setSelected(selected[index]);
            index++;
        }
    }//End setRadioButtonState
    
    public void setSelectedCalculatedAmounts(boolean selected) {
        customizeViewForm.chkShowCalculatedAmts.setSelected(selected);
    }
    
}
