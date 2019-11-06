package edu.mit.coeus.utils.question.gui;

import javax.swing.*;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.CaretListener;
import java.awt.*;
import java.awt.event.*;

import edu.mit.coeus.gui.CoeusFontFactory;
import edu.mit.coeus.organization.bean.YNQExplanationBean;
import java.util.Hashtable;


public class  MoreButton extends JButton {

    private Hashtable hExplanation;
	private String questionId;
	private String question;
	private char functionType;

	public MoreButton(){
	}

	public MoreButton(char functionType,Hashtable hExplanation,String questionId,String question){
	    this.functionType = functionType;
		this.hExplanation = hExplanation;
		this.questionId = questionId;
		this.question = question;
        }

	public void setFunctionType(char functionType){
		this.functionType = functionType;
	}

	public void setQuestionId(String questionId){
		this.questionId = questionId;
	}
	public void setQuestion(String question){
		this.question = question;
	}
	public void setExplanation(Hashtable hExplanation){
		this.hExplanation = hExplanation;
	}


	/**
     * create tabs for more explanation on click of More JButton
     */

	public void showMore(){

        String explanation="";
        String policy="";
        String regulation="";

        if (hExplanation!=null){
            YNQExplanationBean explanationBean = (YNQExplanationBean) hExplanation.get(questionId+"E");
            if (explanationBean!=null){
                explanation = explanationBean.getExplanation();
            }
            explanationBean = (YNQExplanationBean) hExplanation.get(questionId+"P");
            if (explanationBean!=null){
                policy = explanationBean.getExplanation();
            }
            explanationBean = (YNQExplanationBean) hExplanation.get(questionId+"R");
            if (explanationBean!=null){
                regulation = explanationBean.getExplanation();
            }
        }
        QuestionMoreForm more =
                new QuestionMoreForm(functionType,questionId,question,explanation,policy,regulation);
        more.setVisible(true);
    }

	/*
	public static void main(String[] args) 
	{
		Hashtable hExplanation = new Hashtable();
		
		YNQExplanationBean exp1 = new YNQExplanationBean();
		exp1.setQuestionId("Q1");
		exp1.setExplanation("test explanation");
		exp1.setExplanationType("E");

		YNQExplanationBean exp2 = new YNQExplanationBean();
		exp2.setQuestionId("Q1");
		exp2.setExplanation("test explanation");
		exp2.setExplanationType("P");

		YNQExplanationBean exp3 = new YNQExplanationBean();
		exp3.setQuestionId("Q1");
		exp3.setExplanation("test explanation");
		exp3.setExplanationType("E");

		YNQExplanationBean exp4 = new YNQExplanationBean();
		exp4.setQuestionId("Q1");
		exp4.setExplanation("test explanation");
		exp4.setExplanationType("E");

		hExplanation.put("Q1E",exp1);
		hExplanation.put("Q1P",exp2);
		hExplanation.put("Q1E",exp3);
		hExplanation.put("Q1E",exp4);

		JPanel jp = new JPanel();
		MoreButton more = new MoreButton('D',hExplanation,"Q1","sample question");
		jp.add(more);

		JFrame frame = new JFrame();
	     frame.getContentPane().add(jp);
	     frame.setVisible(true);

	}*/
}
