package edu.mit.coeus.utils.rtf;


import java.awt.Color;
import javax.swing.JColorChooser;
import javax.swing.JComboBox;

/*
 * ColorComboBox.java
 *
 * Created on October 6, 2004, 12:38 PM
 */

/**
 * @author  sharathk
 */
public class ColorComboBox extends JComboBox {
    
    private ColorBean colorBeanToModify;
    private boolean modify;
    private int lastIndex;
        
    public void setSelectedIndex(int index) {
        if(modify  && getModel().getSize() - 1 == index) {
            //pop up Color Chooser
            JColorChooser colorChooser = new JColorChooser();
            Color color = colorChooser.showDialog(this, "Select Color", Color.blue);
            if(color == null) {
                return ;
            }
            colorBeanToModify = (ColorBean)getModel().getElementAt(index);
            colorBeanToModify.setColor(color);
            //set color name as [R,G,B] Change...
            colorBeanToModify.setColorName("["+color.getRed()+","+color.getGreen()+","+color.getBlue()+"] Change...");
        }
        super.setSelectedIndex(index);
    }
    
    public void allowModify(boolean modify) {
        this.modify = modify;
    }
    
    public void setSelectedItem( Object item){
        ColorBean colorBean = null;
        ColorBean selectedColorBean = null;
        if(item instanceof ColorBean){
            colorBean = (ColorBean)item;
            if(colorBean!= null && colorBean.getColor()!= null){
                    super.setSelectedItem(colorBean);
                    return ;
            }
            selectedColorBean = (ColorBean)getSelectedItem();
            if(colorBean.getColor()!= null && selectedColorBean.getColor()!= null){
                if((!colorBean.getColor().equals(selectedColorBean.getColor()))){
                    if(colorBeanToModify!= null && colorBeanToModify.getColor()!= null){
                        colorBeanToModify.setColor(colorBean.getColor());
                        colorBeanToModify.setColorName("["+colorBean.getColor().getRed()+","+colorBean.getColor().getGreen()+","+colorBean.getColor().getBlue()+"] ");
                    }
                }
                super.setSelectedIndex(getModel().getSize() - 1);
            }
        }
    }

}