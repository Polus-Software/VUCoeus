/*
 * CoeusHelpGidget.java
 * 
 * Gidget for help buttons
 * 
 * @version: 1.0
 * @author: Jill McAfee, Vanderbilt University Office of Research
 * @created: June 28, 2011
 */
package edu.vanderbilt.coeus.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Desktop;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;
import javax.swing.text.html.*;

import edu.mit.coeus.utils.CoeusGuiConstants;
import edu.vanderbilt.coeus.utils.DisplayOptions;

/**
 * This is a class for creating tool tips (gidgets) from messages in the HelpMessages.properties file.
 *
 * @version 1.0 June 23, 2011
 * @author  Jill McAfee
 * @author  Vanderbilt University
 */
public class CoeusHelpGidget implements ActionListener, MouseListener {
    private CoeusHelpMessage coeusHelpMessage;
    private JButton button;
    private String helpCode;
    private JComponent caller;
    private static Color bkColor = Color.WHITE;
    private static Color borderColor = Color.BLUE;
    private static int WIDTH = 450;
    private static int HEIGHT = 250;
    private static String panelTitle = "How to Use This Page";
    
    /**
     * Constructor
     */
    public CoeusHelpGidget() {
    }
    
    /**
     * Instantiates a new gidget for this helpCode for soft gidgets
     * @param String helpCode code
     */
    public CoeusHelpGidget(String code) {
    	helpCode = code;
    }
    
    /**
     * Instantiates a new gidget for this helpCode
     * @param JComponent component calling component
     * @param String helpCode code
     */
    public CoeusHelpGidget(JComponent component,String code) {
    	helpCode = code;
    	caller = component;
    }
    
    /** 
     * This method gets the size of the component so we will know where to put the gidget
     * 
     */
    public Integer getOffset() {
    	JLabel label = (JLabel) caller;
    	FontMetrics fm = label.getFontMetrics(label.getFont());
    	Integer compSize = fm.stringWidth(label.getText()) + 8;
    	return compSize;
    }
    
    /**
     * This method is called to create the help gidget as a button
     * @return the button
     */
    public JButton getGidget() {
    	button = new JButton();
    	button.setIcon(getHelpIcon());
    	button.addActionListener(this);
    	button.setMaximumSize(new java.awt.Dimension(22, 22));
    	button.setMinimumSize(new java.awt.Dimension(22, 22));
    	button.setPreferredSize(new java.awt.Dimension(22, 22));
    	return button;
    }
    
    /**
     * This method is called to create the help gidget as an icon
     * @return the button
     */
    public ImageIcon gidgetSoft() {
    	ImageIcon icon = getHelpIcon();
    	JLabel lbl = new JLabel(icon);
    	lbl.addMouseListener(this);
    	return icon;
    }
    
    /**
     * This method creates the ImageIcon for the help gidget
     * @return ImageIcon image icon
     */
    private ImageIcon getHelpIcon() {
    	ImageIcon icon = new ImageIcon();
    	BufferedImage image = getHelpImage("");
    	icon.setImage(image);
    	return icon;
    }
    
    /**
     * Get icons for frame
     * @return java.util.List<Image> icons
     */
    private java.util.List<Image> getIcons() {
    	DisplayOptions displayOptions = new DisplayOptions();
        java.util.List<Image> icons = new ArrayList<Image>();
        icons = displayOptions.getInstitutionIcons();
    	return icons;
    }
    
    /**
     * This method creates the ImageIcon for the help gidget
     * @return ImageIcon image icon
     */
    private BufferedImage getHelpImage(String size) {
    	BufferedImage image = null;
    	String IMAGE_TYPE = null;
    	if (size == "LARGE") {
    		// do nothing
    	} else {
    		IMAGE_TYPE = CoeusGuiConstants.HELP_ICON;
    	}
    	try {
    		image = ImageIO.read(getClass().getClassLoader().getResource(IMAGE_TYPE));
    	} catch (IOException e) {
    		System.out.println("Could not retrieve help icon");
    		e.printStackTrace();
    	}
    	return image;
    }

    
    /** This method retrieves the help message in the properties file
     * @param String helpCode code for help message for current context
     * @return helpMessage from retrieved from properties file
     */
    private String getHelpMessage() {
    	coeusHelpMessage = new CoeusHelpMessage();
    	String mesg = coeusHelpMessage.parseMessageKey(helpCode);
    	return mesg;
    }
    
	/**
	 * This method creates a usable version of the help message
	 * and places it in an editorpane
	 * @return JEditorPane
	 */
    private JEditorPane createMessageArea() {
		JEditorPane helpArea = new JEditorPane();
		helpArea.setContentType("text/html");
    	helpArea.setText(getHelpMessage());
    	helpArea.setCaretPosition(0);
    	
    	Font font = new Font("MS Sans Serif", Font.PLAIN, 12);
    	String bodyRule = "body { font-family: " + font.getFamily() + "; " +
        	"font-size: " + font.getSize() + "pt; }";
    	((HTMLDocument)helpArea.getDocument()).getStyleSheet().addRule(bodyRule);
    	helpArea.setFont(font);
    	helpArea.setEditable(false);
    	helpArea.setVisible(true);
    	
    	helpArea.addHyperlinkListener(new HyperlinkListener() {
    	    public void hyperlinkUpdate(HyperlinkEvent e) {
    	        if (e.getEventType() == HyperlinkEvent.EventType.ACTIVATED) {
    	        	try {
						Desktop.getDesktop().browse(e.getURL().toURI());
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					} catch (URISyntaxException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
    	        }
    	    }
    	});
    	
    	return helpArea;
    }
    
    public JLabel createHelpMessage() {
    	return new JLabel(getHelpMessage());
    }
    
    /**
     * Creates a vertically scrolling pane with help message
     * @return JScrollPane
     */
    
    public JScrollPane createHelpScrollArea() {
		JScrollPane scrollPane = new JScrollPane();
    	Border border = BorderFactory.createLineBorder(borderColor);
    	scrollPane.setBorder(border);
		scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		scrollPane.setBackground(bkColor);
		scrollPane.setViewportView(createMessageArea());
    	return scrollPane;
    }
    
    /**
     * Creates a vertically scrolling pane with help message
     * @return JScrollPane
     */
    public JPanel createHelpPanel() {
    	JPanel panel = new JPanel();
    	TitledBorder titleBorder;
    	titleBorder = BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED),panelTitle);
    	panel.setBorder(titleBorder);
    	return panel;
    }
    
    /** 
     * This method creates the popup frame and adds the help message
     */
	public void popupFrame() {
		JFrame.setDefaultLookAndFeelDecorated(true);
		JFrame frame = new JFrame("Help");
		frame.setLayout(new BorderLayout());
    	frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    	frame.setIconImages(getIcons());
		frame.setLocationRelativeTo(caller);
    	
		JScrollPane scrollPane = new JScrollPane(createMessageArea());
		scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		scrollPane.setSize(WIDTH,HEIGHT);
		scrollPane.setBackground(bkColor);

    	frame.getContentPane().add(scrollPane,BorderLayout.CENTER);
    	frame.setSize(WIDTH,HEIGHT);
    	frame.setAlwaysOnTop(true);
    	
		frame.setVisible(true);
    }
	
    /** Handles action to popup frame with help message
     * @param Actionevent e
     */
    public void actionPerformed(ActionEvent e) {
    	popupFrame();
    }

	public void mouseClicked(MouseEvent arg0) {
		popupFrame();
	}

	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	public void mousePressed(MouseEvent e) {
		popupFrame();
		
	}

	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
		
	} 
}