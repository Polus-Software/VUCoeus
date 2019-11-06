/*
 * @(#)QuestionnaireQuestionFormController.java 1.0 09/12/07
 *
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 */

package edu.mit.coeus.mapsrules.controller;

import edu.mit.coeus.exception.CoeusException;
import edu.mit.coeus.exception.CoeusUIException;
import edu.mit.coeus.gui.CoeusDlgWindow;
import edu.mit.coeus.gui.CoeusFontFactory;
import edu.mit.coeus.gui.event.Controller;
import edu.mit.coeus.mapsrules.gui.QuestionnaireQuestionForm;
import edu.mit.coeus.questionnaire.bean.QuestionnaireQuestionsBean;
import edu.mit.coeus.utils.CoeusGuiConstants;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.AbstractAction;

/**
 * This class is used as the controller for the QuestionnaireQuestionForm
 * in the maprules package
 *
 * @author leenababu
 */
public class QuestionnaireQuestionFormController extends Controller
        implements ActionListener{
    private QuestionnaireQuestionForm questionnaireQnForm;
    private CoeusDlgWindow dlgWindow;
    private int WIDTH = 595;
    private int HEIGHT = 180;
    private String title = "Question Details";
    /** Creates a new instance of QuestionnaireQuestionFormController */
    public QuestionnaireQuestionFormController() {
        postInitComponents();
        formatFields();
        registerComponents();
    }
    
    public void postInitComponents(){
        questionnaireQnForm = new QuestionnaireQuestionForm();
        dlgWindow = new CoeusDlgWindow(CoeusGuiConstants.getMDIForm(), title, true);
        dlgWindow.getContentPane().add(questionnaireQnForm);
        dlgWindow.setResizable(false);
        dlgWindow.setFont(CoeusFontFactory.getLabelFont());
        dlgWindow.setSize(WIDTH, HEIGHT);
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        Dimension dlgSize = dlgWindow.getSize();
        dlgWindow.setLocation(screenSize.width/2 - (dlgSize.width/2),
                screenSize.height/2 - (dlgSize.height/2));
        dlgWindow.addComponentListener(
                new ComponentAdapter(){
            public void componentShown(ComponentEvent e){
                questionnaireQnForm.btnCancel.requestFocusInWindow();
            }
        });
        
        dlgWindow.addEscapeKeyListener(new AbstractAction("escPressed"){
            public void actionPerformed(ActionEvent ae){
                dlgWindow.dispose();
            }
        });
        
        dlgWindow.setDefaultCloseOperation(CoeusDlgWindow.DO_NOTHING_ON_CLOSE);
        dlgWindow.addWindowListener(new WindowAdapter(){
            public void windowOpening(WindowEvent we){
            }
            public void windowClosing(WindowEvent we){
                dlgWindow.dispose();
            }
        });
    }
    
    public void display() {
        dlgWindow.setVisible(true);
    }
    
    public void formatFields() {
        questionnaireQnForm.txtArDescription.setBackground((java.awt.Color) javax.swing.UIManager.
                getDefaults().get("Panel.background"));
    }
    
    public Component getControlledUI() {
        return null;
    }
    
    public Object getFormData() {
        return null;
    }
    
    public void registerComponents() {
        questionnaireQnForm.btnCancel.addActionListener(this);
    }
    
    public void saveFormData() throws CoeusException {
        
    }
    
    public void setFormData(Object data) throws CoeusException {
        QuestionnaireQuestionsBean questionnaireQnBean =
                (QuestionnaireQuestionsBean)data;
        questionnaireQnForm.lblQuestionId.setText(questionnaireQnBean.getQuestionId().toString());
        questionnaireQnForm.lblAnswerType.setText(questionnaireQnBean.getAnswerDataType());
        questionnaireQnForm.lblValidAnswer.setText(questionnaireQnBean.getValidAnswer());
        questionnaireQnForm.txtArDescription.setText(questionnaireQnBean.getDescription());
    }
    
    public boolean validate() throws CoeusUIException {
        return false;
    }
    
    public void actionPerformed(ActionEvent e){
        if(e.getSource().equals(questionnaireQnForm.btnCancel)){
            dlgWindow.dispose();
        }
    }
}
