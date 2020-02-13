package edu.ucsd.coeus.personalization.view;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;

import javax.swing.JEditorPane;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;
import javax.swing.text.html.HTMLDocument;

import edu.mit.coeus.utils.CoeusOptionPane;
import edu.ucsd.coeus.personalization.ClientUtils;

public class HtmlWindow extends JEditorPane {
	
	HTMLDocument hdoc;
	
	
    public HtmlWindow(String htmlStr){
    	 super("text/html",htmlStr);
    	hdoc = new HTMLDocument();
    	this.setDocument(hdoc);
    	this.setText(htmlStr);
    	this.setContentType("text/html");
        setEditable(false);
        setSize(new Dimension(800,300));
        setBackground(new Color(0xffffdd));
        addHyperlinkListener(new HyperlinkListener(){
            public void hyperlinkUpdate(HyperlinkEvent e){
                HyperlinkEvent.EventType type = e.getEventType();
                if (type.equals(HyperlinkEvent.EventType.ENTERED)){
                    setCursor(Cursor.getPredefinedCursor( Cursor.HAND_CURSOR) );
                } else if (type.equals(HyperlinkEvent.EventType.EXITED)) {
                    setCursor( Cursor.getDefaultCursor() );
                } else {
        			if (e.getURL().getPath().endsWith("print")) { /* Show Print dialog */
    					CoeusOptionPane.showErrorDialog("Printing is disabled. Unable to verify DocumentRenderer license");
//        		    	  DocumentRenderer dr = new DocumentRenderer();
//        		    	  dr.setScaleWidthToFit(true);
//        		    	  dr.setLandscape(true);
//        		    	  dr.print(hdoc);
        			} else {
        				openPage( e.getURL() );
        			}
                }
            }
            private void openPage(java.net.URL url){
        		ClientUtils.openURL(url.toString());
                return;
            }
        });
        
    }	
    
    public HTMLDocument getHTMLDocument() {
    	return hdoc;
    }
	
	
        	

}
