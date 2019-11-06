/*
 * @(#)QuestionMoreForm.java 1.0 8/31/02
 *
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 */

package edu.mit.coeus.utils.question.gui;

import edu.mit.coeus.utils.question.bean.YNQExplanationBean;
import edu.mit.coeus.gui.CoeusFontFactory;
import edu.mit.coeus.utils.CoeusGuiConstants;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Vector;
import java.text.SimpleDateFormat;
import edu.mit.coeus.gui.CoeusDlgWindow;
import edu.mit.coeus.utils.CoeusTabbedPane;

/**
 * This class constructs the Organization QuestionMoreForm
 *
 * @version :1.0 August 31, 2002, 1:35 PM
 * @author Guptha K
 *
 */
public class QuestionMoreForm extends CoeusDlgWindow{

    char functionType;
    String questionId,question,explanation,policy,regulation;
    /**
     * Default constructor
     */
    public QuestionMoreForm(){
    }

    /**
     * constructor, which constructs the Organization maintenance questions more form.
     * The functionality of the form (enable/disable the
     * components) will be decided based on the parameter functionType.
     *
     * @param functionType char  decides to enable/disable the form controls A- Add mode
     *                           D- Display mode M- Modify mode
     * @param questionId String
     * @param question String
     * @param explanation String
     * @param policy String
     * @param regulation String
     */
    public QuestionMoreForm(char functionType, String questionId,String question,
                                                   String explanation,String policy,String regulation){
        super(CoeusGuiConstants.getMDIForm(),"Question: More",true);
        this.functionType = functionType;
        this.questionId = questionId==null?"":questionId;
        this.question = question==null?"":question;
        this.explanation = explanation;
        this.policy = policy;
        this.regulation = regulation;
        createQuestionsMorePanel();
    }

	/**
	 * Create a JPanel and add the components into it.
	 */
  	public void createQuestionsMorePanel(){
        JPanel pnlMore = new JPanel();
        pnlMore.setLayout(new BorderLayout());
        pnlMore.add(createForm(),BorderLayout.CENTER);
        JPanel pnlOk = new JPanel();
        pnlOk.setLayout( new javax.swing.BoxLayout(pnlOk, BoxLayout.Y_AXIS ));
        JButton btnOk = new JButton("   OK  ");
        btnOk.setEnabled(false);
        btnOk.setFont(CoeusFontFactory.getLabelFont());
        btnOk.addActionListener(new ActionListener(){
                public void actionPerformed(ActionEvent ae){
                }
        });
        btnOk.setMnemonic('O');
        JButton btnCancel = new JButton("Cancel");
        btnCancel.addActionListener(new ActionListener(){
                public void actionPerformed(ActionEvent ae){
                    dispose();
                }
        });
        btnCancel.setMnemonic('C');
        btnCancel.setFont(CoeusFontFactory.getLabelFont());
        pnlOk.add(Box.createRigidArea(new Dimension(30, 40)));
        pnlOk.add(btnOk);
        pnlOk.add(Box.createRigidArea(new Dimension(30, 10)));
        pnlOk.add(btnCancel);
        pnlMore.add(pnlOk, BorderLayout.EAST);
        getContentPane().add(pnlMore, BorderLayout.CENTER);
        setSize(500, 300);
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        Dimension dlgSize = getSize();
        setLocation(screenSize.width/2 - (dlgSize.width/2),
                    screenSize.height/2 - (dlgSize.height/2));
        screenSize = null;
        }

    /**
     * Create organization maintenance QuestionMore tabs.
     * @return JTabbedPane.
     */
    public JTabbedPane createForm() {
        JTabbedPane tabbedPane = new CoeusTabbedPane(CoeusTabbedPane.CTRL_T);
        tabbedPane.addKeyListener( new KeyAdapter(){
            public void keyReleased( KeyEvent key ){
                if (  key.getKeyCode() == KeyEvent.VK_ESCAPE  ) {
                    dispose();
                }
        }});
        // set the tab into tab pane
        tabbedPane.addTab("Question",  getPanelFor(questionId+ ": "+question) );
        tabbedPane.addTab("Explanation",  getPanelFor(explanation) );
        tabbedPane.addTab("Policy",   getPanelFor(policy) );
        tabbedPane.addTab("Regulation",  getPanelFor(regulation) );
        tabbedPane.setFont(CoeusFontFactory.getNormalFont());
        tabbedPane.setSelectedIndex(0);
        tabbedPane.requestFocusInWindow();
		return tabbedPane;
    }

    /**
     * It creates a JPanel a sets the enabled status to the components in it
     * based on the string value passed as a parameter.
     * @param str, a String member variable which hold values like
     * Question, Explanation,Policy, Regulation.
     * @return JPanel
     */
    public JPanel getPanelFor(String str){
        JPanel pnlExp = new JPanel();
        if(str.trim().equals(":")) return pnlExp;
        pnlExp.setLayout(new BorderLayout());
        JTextArea txtrExp = new JTextArea();
        txtrExp.addKeyListener( new KeyAdapter(){
            public void keyReleased( KeyEvent key ){
                if (  key.getKeyCode() == KeyEvent.VK_ESCAPE  ) {
                    dispose();
                }
        }});
        txtrExp.setWrapStyleWord(true);
        txtrExp.setLineWrap(true);
        txtrExp.setText(str);
        txtrExp.setFont(CoeusFontFactory.getNormalFont());
        pnlExp.add(txtrExp,BorderLayout.CENTER);
        boolean enableStatus = true;
        switch (functionType) {
            case 'D': // display only
                enableStatus = false;
                break;
            case 'M': // allows modification
                enableStatus = true;
                break;
            default: // plain
                enableStatus = true;
                break;
        }
        txtrExp.setEditable(false);
        txtrExp.setBackground(this.getBackground());
        return pnlExp;
    }
}
