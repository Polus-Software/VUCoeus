package edu.vanderbilt.coeus.gui.toolbar;

import edu.mit.coeus.gui.CoeusAppletMDIForm;
import edu.mit.coeus.gui.CoeusMessageResources;
import edu.mit.coeus.gui.menu.CoeusPopupMenu;
import edu.mit.coeus.gui.toolbar.CoeusToolBarButton;
import edu.mit.coeus.utils.CoeusGuiConstants;
import edu.vanderbilt.coeus.utils.CustomFunctions;

import javax.swing.*;

import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

public class CustomToolBar extends JToolBar implements ActionListener {
    public CoeusToolBarButton contactCoeusHelp;
    private CoeusPopupMenu cpm; 
    private JToolBar toolbar;
    
    private CoeusAppletMDIForm mdiForm;
    
    private CoeusMessageResources coeusMessageResources;
    
    /**  Creates a new CoeusToolBarFactory
     * @param mdiForm  CoeusAppletMDIForm
     */
    public CustomToolBar(CoeusAppletMDIForm mdiForm) {
        super();
        this.mdiForm = mdiForm;
        coeusMessageResources = CoeusMessageResources.getInstance();
        createToolBar();
    }
    
    /**
     * getDefaultToolbar returns the JToolbar in the MDI form with the default toolbar
     * with set icons,text and tooltip specified for the Default toolbar,
     * the icon,text and tooltip is added to the toolbar,
     * the toolbar consist of popup menu also.
     *
     */
    private void createToolBar() {
        toolbar = new JToolBar();
        contactCoeusHelp = new CoeusToolBarButton(new ImageIcon(
                getClass().getClassLoader().getResource(CoeusGuiConstants.HELP_ICON)), "Contact Coeus Help", "Contact Coeus Help");

        toolbar.addSeparator();
        toolbar.add(contactCoeusHelp);
        
        toolbar.setFloatable(false);
        setTextLabels(false);
        MouseListener pl = new PopupListener();
        cpm = new CoeusPopupMenu();
        toolbar.addMouseListener(pl);
        
        contactCoeusHelp.addActionListener(this);
    }
    
    /**
     * This method gets the JToolBar
     *
     * @return JToolBar coeus maintain menu
     */
    public JToolBar getToolBar(){
        return toolbar;
    }
    

    /**
     * This method is used to capture the toolbar button action events. for example
     * rolodex, sponsor module menuitem.
     *
     * @param ae  ActionEvent
     */
    public void actionPerformed(ActionEvent ae) {
        JDesktopPane desktop = mdiForm.getDeskTopPane();
        Object menuItem = ae.getSource();
        try {
        	if (menuItem.equals(contactCoeusHelp)) {
            	System.out.println("Here is where we will email Coeus Help");
            	contactCoeusHelp();
            }
        }
        catch(Exception e) {
            e.printStackTrace();
        }
    }
    
    /**
     * Method to open mail window for contacting Coeus Help
     * @return void
     * @throws URISyntaxException 
     */
    public void contactCoeusHelp() {
    	CustomFunctions custom = new CustomFunctions();
    	String[] params = custom.getParameterValues("CMS_REPLY_TO_ID");
    	String emailAddress = params[0];
    	System.out.println("Send emails to " + emailAddress);
    	
    	Desktop desktop;
  	  	URI mailto;
    	if (Desktop.isDesktopSupported() 
    	    && (desktop = Desktop.getDesktop()).isSupported(Desktop.Action.MAIL)) {
			try {
				mailto = new URI("mailto:" + emailAddress + "?subject=Coeus%20Test");
				try {
					desktop.mail(mailto);
				} catch (IOException e) {
					System.out.println("Unable to initiate mail desktop mail application");
					e.printStackTrace();
				}
			} catch (URISyntaxException e) {
				System.out.println("Invalid email address for dektop mail appliation");
				e.printStackTrace();
			}
    	}
    }
    
    /**
     * This method sets the text to the toolbar button if the set text is enabled.
     *
     * @param labelsAreEnabled boolean
     */
    public void setTextLabels(boolean labelsAreEnabled) {
        Component c;
        int i = 0;
        while ((c = toolbar.getComponentAtIndex(i++)) != null) {
            if (c instanceof CoeusToolBarButton) {
                CoeusToolBarButton button = (CoeusToolBarButton) c;
                if (labelsAreEnabled)
                    button.setText(button.getText());
                else
                    button.setText(null);
            }
        }
    }
        
    /**
     * This class implemented for the toolbar ,it will popup the menu on click
     * of the mouse on the toolbar,this class extends MouseAdapter.
     *
     */
    class PopupListener extends MouseAdapter {
        /**
         * This method is used to handle the mousePressed event.
         *
         * @param e MouseEvent
         */
        public void mousePressed(MouseEvent e) {
            maybeShowPopup(e);
        }
        
        /**
         * This method is used to handle the mouseReleased event.
         *
         * @param e MouseEvent
         */
        public void mouseReleased(MouseEvent e) {
            maybeShowPopup(e);
        }
        
        private void maybeShowPopup(MouseEvent e) {
            if (e.isPopupTrigger()) {
                cpm.show(e.getComponent(),
                e.getX(), e.getY());
            }
        }
    }
    
}


