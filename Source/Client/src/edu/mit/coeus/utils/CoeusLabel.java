/*
 * CoeusLable.java
 *
 * Created on July 14, 2005, 2:14 PM
 */

package edu.mit.coeus.utils;

import edu.mit.coeus.gui.CoeusFontFactory;
import edu.mit.coeus.gui.CoeusMessageResources;
import javax.swing.Icon;
import javax.swing.JLabel;

/**
 *
 * @author  nadhgj
 */
public class CoeusLabel extends JLabel{
    
    /** reference object of CoeusMessageResources which will be used to get the
     * text to be displayed on the Label */
    private CoeusMessageResources coeusMessageResources = null;
    /** Creates a new instance of CoeusLable */
    public CoeusLabel() {
        super();
        setFont(CoeusFontFactory.getLabelFont());
    }
    
    public CoeusLabel(String text){
        super(text); 
        setFont(CoeusFontFactory.getNormalFont());
    }
    
    public CoeusLabel(String text, int horizantalAlignment){
        super(text,horizantalAlignment); 
        setFont(CoeusFontFactory.getNormalFont());
    }
    
    public CoeusLabel(String text, Icon icon, int horizantalAlignment){
        super(text,icon,horizantalAlignment); 
        setFont(CoeusFontFactory.getNormalFont());
    }
    
    public CoeusLabel(Icon image){
        super(image);
        setFont(CoeusFontFactory.getNormalFont());
    }
    
    public CoeusLabel(Icon image, int horizantalAlignment){
        super(image,horizantalAlignment); 
        setFont(CoeusFontFactory.getNormalFont());
    }
    
    private String getLabel(String text) {
        initPropsFile();
        String s  = coeusMessageResources.parseLabelKey(text);
        return s;
    }
    
    public void setText(String text) {
        super.setText(getLabel(text));
    }
    
    private void initPropsFile() {
        if(coeusMessageResources == null)
            coeusMessageResources = CoeusMessageResources.getInstance();
    }
}
