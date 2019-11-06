/*
 * HeaderDecorator.java
 *
 * Created on October 31, 2007, 3:08 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package edu.mit.coeus.utils.document.decorator;

import com.lowagie.text.DocumentException;
import com.lowagie.text.pdf.PdfContentByte;
import java.awt.Color;
import java.io.IOException;

/**
 * decorates pdf header
 * @author sharathk
 */
public class HeaderDecorator implements Decorator{
    
    /**
     * header decoration
     */
    private CommonBean headerBean;
    
    private Color fillColor = null;
    private boolean widthCalculated = false;
    private int x,y;
    
    /**
     * Creates a new instance of HeaderDecorator
     * @param headerBean header decoration
     */
    public HeaderDecorator(CommonBean headerBean) {
        this.headerBean = headerBean;
        fillColor = headerBean.getFont().getColor() == null ? DecoratorConstants.DEFAULT_COLOR : headerBean.getFont().getColor();
    }

    /**
     * decorates pdf footer
     * @param pdfContentByte pdfContentByte
     * @param pageWidth pdf page width
     * @param pageHeight pdf page height
     */
    public void decorate(PdfContentByte pdfContentByte, int pageWidth, int pageHeight) {
        if(headerBean.getText() == null || headerBean.getText().trim().length() == 0) {
            //No Text to write. 
            return;
        }
        pdfContentByte.beginText();
        //Set Font and Size
        pdfContentByte.setFontAndSize(headerBean.getFont().getBaseFont(), headerBean.getFont().getSize());
        pdfContentByte.setColorFill(fillColor);
        
        //Set Text According to Alignment
        if(!widthCalculated) {
            //int x,y;
            int textWidth = (int)pdfContentByte.getEffectiveStringWidth(headerBean.getText(), true);
            
            if(headerBean.getType().equalsIgnoreCase(DecoratorConstants.ALIGN_LEFT)) {
                x = DecoratorConstants.MARGIN;
            }else if(headerBean.getType().equalsIgnoreCase(DecoratorConstants.ALIGN_RIGHT)){
                x = pageWidth - textWidth - DecoratorConstants.MARGIN;
            }else {
                //Align Middle
                x = (pageWidth - textWidth)/2;
            }
            y = pageHeight - DecoratorConstants.MARGIN;
            widthCalculated = true;
        }
        
        pdfContentByte.setTextMatrix(x, y);
        pdfContentByte.showText(headerBean.getText());
        pdfContentByte.endText();
    }
    

    
}
