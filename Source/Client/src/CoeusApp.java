import java.awt.*;
import java.awt.event.*;
import java.util.* ;
import  java.util.GregorianCalendar ;
import javax.swing.JPanel;

public class CoeusApp {
    
    
    public static void main(String[] args) {
        
        Frame myFrame = new Frame("Coeus"); // create frame with title
        final edu.mit.coeus.applet.CoeusWebStart myApplet = new edu.mit.coeus.applet.CoeusWebStart(myFrame); // define applet of interest
        myFrame.addWindowListener( new WindowAdapter(){
            public void windowClosing(WindowEvent event) {
                myApplet.stop();
                myApplet.destroy();
                System.exit(0);
            }
        });
        myFrame.resize(380, 150); 
        myFrame.setResizable(false) ;
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        Dimension dlgSize = myFrame.getSize();
        myFrame.setLocation(screenSize.width/2 - (dlgSize.width/2),
        screenSize.height/2 - (dlgSize.height/2));
        
        //myFrame.pack(); // set window to appropriate size (for its elements)
        myApplet.init();
        if(myApplet.isUserIdMode()){
            myFrame.add(myApplet, BorderLayout.CENTER);
            myFrame.setVisible(true); // usual step to make frame visible
            myApplet.setInitFocus();
        }else if(!myApplet.isValidUser()){
            myApplet.stop();
            myApplet.destroy();
            System.exit(0);
        }else{
            myFrame.setVisible(false); // usual step to make frame visible
        }
        
    } // end main
} // end class









