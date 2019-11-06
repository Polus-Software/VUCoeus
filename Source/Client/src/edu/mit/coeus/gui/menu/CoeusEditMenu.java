/*
 * CoeusEditMenu.java
 *
 * Created on December 23, 2004, 10:34 AM
 */

package edu.mit.coeus.gui.menu;

import edu.mit.coeus.gui.CoeusAppletMDIForm;
import edu.mit.coeus.gui.CoeusMessageResources;
import java.awt.MenuItem;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import javax.swing.JMenu;
import javax.swing.KeyStroke;

/**
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 * @author  jobinelias
 */
public class CoeusEditMenu extends JMenu implements ActionListener {
	
	public CoeusMenuItem mnItmUnDo,mnItmCut,mnItmCopy,mnItmClear,mnItmPaste,mnItmSelectAll;
	
	private final String SEPERATOR="seperator";

    private CoeusMenu coeusMenu;
    

    private CoeusAppletMDIForm mdiForm;

    //holds CoeusMessageResources instance used for reading message Properties.
    private CoeusMessageResources coeusMessageResources;
	
	/** Creates a new instance of CoeusEditMenu */
	public CoeusEditMenu(CoeusAppletMDIForm mdiForm) {
		super();
		this.mdiForm = mdiForm;
        coeusMessageResources = CoeusMessageResources.getInstance();
        createMenu();
	}
	
	/**
     * This method is used to get the Admin menu
     *
     * @return JMenu coeus Admin menu
     */
    public JMenu getMenu(){
        return coeusMenu;
    }

    /**
     * This method is used to create Admin menu for coeus application.
     */
    private void createMenu(){
        java.util.Vector vecEdit = new java.util.Vector();

        mnItmUnDo = new CoeusMenuItem("Undo",null,true,true);
        mnItmUnDo.setMnemonic('U');
		mnItmUnDo.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Z,KeyEvent.CTRL_MASK));
		
        mnItmCut = new CoeusMenuItem("Cut",null,true,true);
        mnItmCut.setMnemonic('t');
		mnItmCut.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_X,KeyEvent.CTRL_MASK));
		
        mnItmCopy = new CoeusMenuItem("Copy",null,true,true);
        mnItmCopy.setMnemonic('C');
		mnItmCopy.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_C,KeyEvent.CTRL_MASK));
		
        mnItmClear = new CoeusMenuItem("Clear",null,true,true);
        mnItmClear.setMnemonic('a');
		
		mnItmPaste = new CoeusMenuItem("Paste",null,true,true);
        mnItmPaste.setMnemonic('P');
		mnItmPaste.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_V,KeyEvent.CTRL_MASK));
		
		mnItmSelectAll = new CoeusMenuItem("Select All",null,true,true);
        mnItmSelectAll.setMnemonic('l');
		mnItmSelectAll.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_A, KeyEvent.CTRL_MASK));
        vecEdit.add(mnItmUnDo);
        vecEdit.add(mnItmCut);
        vecEdit.add(mnItmCopy);
        vecEdit.add(mnItmPaste);
		vecEdit.add(mnItmClear);
		vecEdit.add(mnItmSelectAll);
        
        coeusMenu = new CoeusMenu("Edit",null,vecEdit,true,true);
        coeusMenu.setMnemonic('E');

        //add listener
        mnItmUnDo.addActionListener(this);
        mnItmCut.addActionListener(this);
        mnItmCopy.addActionListener(this);
        mnItmPaste.addActionListener(this);
        mnItmClear.addActionListener(this);
        mnItmSelectAll.addActionListener(this);
  
    }
	
	public void actionPerformed(java.awt.event.ActionEvent e) {
	}
	
}
