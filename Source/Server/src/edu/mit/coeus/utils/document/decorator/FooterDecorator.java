/*
 * FooterDecorator.java
 *
 * Created on October 31, 2007, 1:25 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package edu.mit.coeus.utils.document.decorator;

import com.lowagie.text.DocumentException;
import com.lowagie.text.pdf.PdfContentByte;
import java.awt.Color;
//import edu.mit.coeus.utils.document.DocumentConstants;
import java.io.IOException;

/**
 * decorates the footer
 * @author sharathk
 */
public class FooterDecorator implements Decorator{
    
    /**
     * footer decorations
     */
    private CommonBean footerBean;
    
    private Color fillColor = null;
    private boolean widthCalculated = false;
    private int x,y;
    
    /**
     * Creates a new instance of FooterDecorator
     * @param footerBean footer decoration
     */
    public FooterDecorator(CommonBean footerBean){
        this.footerBean = footerBean;
        fillColor = footerBean.getFont().getColor() == null ? DecoratorConstants.DEFAULT_COLOR : footerBean.getFont().getColor();
    }
    
    /**
     * decorates the pdf
     * @param pdfContentByte pdfContentByte
     * @param pageWidth pdf page width
     * @param pageHeight pdf page height
     */
    public void decorate(PdfContentByte pdfContentByte, int pageWidth, int pageHeight) {
        if(footerBean.getText() == null || footerBean.getText().trim().length() == 0) {
            //No Text to write. 
            return;
        }
        pdfContentByte.beginText();
        //Set Font and Size
        pdfContentByte.setFontAndSize(footerBean.getFont().getBaseFont(), footerBean.getFont().getSize());
        pdfContentByte.setColorFill(fillColor);
        
        if(!widthCalculated) {
            //Set Text According to Alignment
            //int x,y;
            int textWidth = (int)pdfContentByte.getEffectiveStringWidth(footerBean.getText(), true);
            if(footerBean.getType().equalsIgnoreCase(DecoratorConstants.ALIGN_LEFT)) {
                x = DecoratorConstants.MARGIN;
            }else if(footerBean.getType().equalsIgnoreCase(DecoratorConstants.ALIGN_RIGHT)){
                x = pageWidth - textWidth - DecoratorConstants.MARGIN;
            }else {
                //Align Middle
                x = (pageWidth - textWidth)/2;
            }
            y = DecoratorConstants.MARGIN;
            widthCalculated = true;
        }
        
        pdfContentByte.setTextMatrix(x, y);
        pdfContentByte.showText(footerBean.getText());
        pdfContentByte.endText();
        
        
    }
    
    
}
