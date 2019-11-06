/*
 * CopiedFromPropNum.java
 * 
 * @version: 1.0
 * @author: Jill McAfee, Vanderbilt University Medical Center
 * @created: May 17, 2016
 */
package edu.vanderbilt.coeus.propdev;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;

import javax.swing.*;
import javax.swing.text.html.*;

import edu.vanderbilt.coeus.utils.*;

/**
 * This is a class for showing a pop-up with the proposal number from which the current proposal was copied
 */
public class CopiedFromPropNum implements ActionListener, MouseListener {
    private String txtCopiedFromPropNum;
    private static Color bkColor = Color.WHITE;
    private static int WIDTH = 260;
    private static int HEIGHT = 120;
    private static String panelTitle = "Copied From Prop #";
    
    /**
     * Constructor
     */
    public CopiedFromPropNum(String proposalNumber) {
    	txtCopiedFromPropNum = proposalNumber;
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
	 * This method creates a usable version of the help message
	 * and places it in an editorpane
	 * @return JEditorPane
	 */
    private JEditorPane createMessageArea() {
		JEditorPane textArea = new JEditorPane();
		textArea.setContentType("text/html");
		
		String message = "<BR>This proposal was copied from proposal number <span style=\"color: red; font-weight: bold;\">"; 
		textArea.setText(message +  txtCopiedFromPropNum + "</span>");
		
		if (txtCopiedFromPropNum == null) {
			message = "<BR>This proposal was not copied.";
			textArea.setText(message);
		}
		
		textArea.setCaretPosition(0);
    	
    	Font font = new Font("MS Sans Serif", Font.PLAIN, 12);
    	String bodyRule = "body { font-family: " + font.getFamily() + "; " +
        	"font-size: " + font.getSize() + "pt; text-align: center; vertical-align: center; }";
    	((HTMLDocument)textArea.getDocument()).getStyleSheet().addRule(bodyRule);
    	textArea.setFont(font);
    	textArea.setEditable(false);
    	textArea.setVisible(true);

    	return textArea;
    }

    /** 
     * This method creates the popup frame and adds the help message
     */
	public void popupFrame() {
		JFrame.setDefaultLookAndFeelDecorated(true);
		JFrame frame = new JFrame(panelTitle);
		frame.setLayout(new BorderLayout());
    	frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    	frame.setIconImages(getIcons());
    	
		JScrollPane scrollPane = new JScrollPane(createMessageArea());
		scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		scrollPane.setSize(WIDTH,HEIGHT);
		scrollPane.setBackground(bkColor);

    	frame.getContentPane().add(scrollPane,BorderLayout.CENTER);
    	frame.setSize(WIDTH,HEIGHT);
    	frame.setAlwaysOnTop(true);
    	
    	Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
    	frame.setLocation(dim.width/2-frame.getSize().width/2, dim.height/2-frame.getSize().height/2);
    	
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