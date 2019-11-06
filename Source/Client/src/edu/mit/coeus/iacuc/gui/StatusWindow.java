/*
 * StatusWindow.java
 *
 * Created on November 14, 2003, 10:28 AM
 */

package edu.mit.coeus.iacuc.gui;

/** Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 * @author sharathk
 */
import java.awt.*;
import javax.swing.*;
import javax.swing.border.LineBorder;

import edu.mit.coeus.gui.*;

public class StatusWindow{
    
    private JWindow window;
    private JPanel panel;
    
    private JLabel lblHeader, lblMessage, lblFooter;
        
    private Container container;
    
    private static final int WIDTH = 300;
    private static final int HEIGHT = 75;
    
    /** Creates a new instance of StatusWindow */
    public StatusWindow() {
        window = new JWindow();
        initComponents();
        postInitComponents();
    }
    
    /** Creates a new instance of StatusWindow */
    public StatusWindow(Frame owner, boolean modal) {
        window = new JWindow(owner);
        initComponents();
        postInitComponents();
    }
    
    private void initComponents() {
        panel = new JPanel();
        panel.setBorder(new LineBorder(Color.BLACK));
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
                
        lblHeader = new JLabel("Processing Information");
        lblHeader.setFont(CoeusFontFactory.getLabelFont());
        
        lblMessage = new JLabel();
        lblMessage.setFont(CoeusFontFactory.getNormalFont());
        
        lblFooter = new JLabel("Please Wait...");
        lblFooter.setFont(CoeusFontFactory.getNormalFont());
        
        panel.add(lblHeader);
        panel.add(lblMessage);
        panel.add(lblFooter);
        
        panel.setBackground(new Color(156, 230, 254));
    }
    
    private void postInitComponents() {
        container = window.getContentPane();
        container.add(panel);
        window.setSize(WIDTH, HEIGHT);
        int screenWidth, screenHeight;
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        screenWidth = (int)screenSize.getWidth();
        screenHeight = (int)screenSize.getHeight();
        window.setLocation((screenWidth - WIDTH)/2, (screenHeight - HEIGHT)/2);
        
    }
    
    public void setHeader(String header) {
        lblHeader.setText(header);
    }
    
    public void setMessage(String message) {
        lblMessage.setText(message);
    }
    
    public void setFooter(String footer) {
        lblFooter.setText(footer);
    }
    
    public void display() {
        window.setVisible(true);
    }
   
    public void setVisible(boolean visible) {
       window.setVisible(visible);
    }
    
    //For Testing purpose Only 
    public static void main(String s[]) {
        StatusWindow w = new StatusWindow();
        w.display();
    }
    
}
