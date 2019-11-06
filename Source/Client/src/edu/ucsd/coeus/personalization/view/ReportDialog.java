package edu.ucsd.coeus.personalization.view;

import java.awt.BorderLayout;
import java.awt.Component;

import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

/**
 * 
 * @author rdias
 * some of this code was adapted from coeus option pane
 *
 */
public class ReportDialog {
	
    private static JDialog dialog;
	
    public static void showReportDialog(Component parent,String title,String content,boolean centerp,boolean modal) {
        dialog = new JDialog(JOptionPane.getFrameForComponent(parent),title,modal);
        dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        JPanel jp = new JPanel(new BorderLayout());
        HtmlWindow hw = new HtmlWindow(content);
        JScrollPane vhPane = new JScrollPane(hw, 
     		   JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        dialog.getContentPane().add(jp,BorderLayout.CENTER);
        dialog.getContentPane().add(vhPane,BorderLayout.CENTER);                        
        //dialog.getContentPane().add(createClosePanel(),BorderLayout.SOUTH);
        dialog.pack();
        if (centerp)
            dialog.setLocationRelativeTo(null);
        else
        	dialog.setLocationRelativeTo(parent);
        dialog.setVisible(true);
    }
    
    public static void closeDialog() {
    	if (dialog != null) {
    		dialog.dispose();
    	}
    }
}
