/*
 * WaterMarkDecorator.java
 *
 * Created on November 2, 2007, 11:30 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package edu.mit.coeus.utils.document.decorator;

import com.lowagie.text.BadElementException;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Element;
import com.lowagie.text.Image;
import com.lowagie.text.pdf.PdfContentByte;
import edu.mit.coeus.utils.UtilFactory;
import java.awt.Color;
//import java.awt.Color;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * decorates pdf with watermark
 * @author sharathk
 */
public class WaterMarkDecorator implements Decorator{
    
    /**
     * watermark decoration
     */
    private CommonBean watermarkBean;
    
    private Color fillColor = null;
    private Image image = null;
    private boolean widthCalculated = false;
    float x, y, x1, y1, angle;
    
    /**
     * Creates a new instance of WaterMarkDecorator
     * @param watermarkBean watermark decorations
     */
    public WaterMarkDecorator(CommonBean watermarkBean){
        this.watermarkBean = watermarkBean;
        fillColor = watermarkBean.getFont().getColor() == null ? DecoratorConstants.DEFAULT_COLOR : watermarkBean.getFont().getColor();
        
        if(watermarkBean.getType().equalsIgnoreCase(DecoratorConstants.WATERMARK_TYPE_IMAGE)) {
            try{
                URL url = getClass().getResource(watermarkBean.getText());
                image = Image.getInstance(url);
            }catch (BadElementException badElementException) {
                //badElementException.printStackTrace();
                UtilFactory.log(badElementException.getMessage(), badElementException, "WaterMarkDecorator", "decorate");
            }catch (MalformedURLException malformedURLException) {
                //malformedURLException.printStackTrace();
                UtilFactory.log(malformedURLException.getMessage(), malformedURLException, "WaterMarkDecorator", "decorate");
            }catch (IOException iOException) {
                //iOException.printStackTrace();
                UtilFactory.log(iOException.getMessage(), iOException, "WaterMarkDecorator", "decorate");
            }
        }
        
    }
    
    /**
     * decorates pdf with watermark
     * @param pdfContentByte pdfContentByte
     * @param pageWidth pdf page width
     * @param pageHeight pdfPage Height
     */
    public void decorate(PdfContentByte pdfContentByte, int pageWidth, int pageHeight) {
        try{
            if(watermarkBean.getType().equalsIgnoreCase(DecoratorConstants.WATERMARK_TYPE_IMAGE) && image != null) {
                
                // watermark under the existing page
                //Image image = Image.getInstance(watermarkBean.getText());
                float height = image.getPlainHeight();
                float width = image.getPlainWidth();
                image.setAbsolutePosition((pageWidth - width)/2, (pageHeight - height)/2);
                pdfContentByte.addImage(image);
                return ;
            }

            pdfContentByte.beginText();
            //Set Font and Size
            pdfContentByte.setFontAndSize(watermarkBean.getFont().getBaseFont(), watermarkBean.getFont().getSize());
            pdfContentByte.setColorFill(fillColor);
            
            if(!widthCalculated) {
                int textWidth = (int)pdfContentByte.getEffectiveStringWidth(watermarkBean.getText(), false);
                int diagonal = (int)Math.sqrt((pageWidth * pageWidth) + (pageHeight * pageHeight));
                int pivotPoint = (diagonal - textWidth)/2;
                
                angle = (float)Math.atan((float)pageHeight/pageWidth);
                
                //float x,y;
                //x = (float)(pivotPoint * Math.cos(angle));
                //y = (float)(pivotPoint * Math.sin(angle));
                x = (float)(pivotPoint * pageWidth)/diagonal;
                y = (float)(pivotPoint * pageHeight)/diagonal;
                
                //move  X and Y so as to center the Font on the diagonal
                float x1, y1;
                x1 = (float)(((float)watermarkBean.getFont().getSize()/2) * Math.sin(angle));
                y1 = (float)(((float)watermarkBean.getFont().getSize()/2) * Math.cos(angle));
                widthCalculated = true;
            }
            
            pdfContentByte.showTextAligned(Element.ALIGN_LEFT, watermarkBean.getText(), x+x1, y-y1, (float)Math.toDegrees(angle));
            pdfContentByte.endText();
            
        }catch (BadElementException badElementException) {
            //badElementException.printStackTrace();
            UtilFactory.log(badElementException.getMessage(), badElementException, "WaterMarkDecorator", "decorate");      
        }catch (DocumentException documentException) {
            //documentException.printStackTrace();
            UtilFactory.log(documentException.getMessage(), documentException, "WaterMarkDecorator", "decorate");      
        }
    }
        
}
