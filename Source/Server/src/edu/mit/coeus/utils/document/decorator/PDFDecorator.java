/*
 * PDFDecorator.java
 *
 * Created on October 30, 2007, 4:05 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package edu.mit.coeus.utils.document.decorator;

import com.lowagie.text.DocumentException;
import com.lowagie.text.Rectangle;
import com.lowagie.text.pdf.PdfContentByte;
import com.lowagie.text.pdf.PdfReader;
import com.lowagie.text.pdf.PdfStamper;
import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
//import java.io.FileOutputStream;
import java.io.IOException;
//import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Use this class to decorate a PDF with header, footer and watermark
 * @author sharathk
 */
public class PDFDecorator{
    
    /**
     * constructor
     */
    public PDFDecorator() {
        
    }

    /**
     * returns the DecoratorBean instance for the status, else returns null.
     * @param status status
     * @return returns DecoratorBean
     * @throws java.lang.Exception throws exception if any error occurs
     */
    /*public DecoratorBean find(String status)throws Exception {
        DecoratorParser decoratorParser = new DecoratorParser();
        return decoratorParser.find(status);
    }*/

    /**
     * returns the DecoratorBean instance for the status and group, else returns null.
     * @param group group
     * @param status status
     * @return returns DecoratorBean
     * @throws java.lang.Exception throws exception if any error occurs
     */
    public DecoratorBean find(String group, String status)throws Exception {
        DecoratorParser decoratorParser = new DecoratorParser();
        return decoratorParser.find(group, status);
    }

    public DecoratorBean find(String group, String status, String docType)throws Exception {
        DecoratorParser decoratorParser = new DecoratorParser();
        return decoratorParser.find(group, status, docType);
    }

    /**
     * decorates the pdf
     * @param decoratorBean encapsulates all decorations for the pdf
     * @param pdfContent pdfContent
     * @return decorated pdf bytes
     * @throws com.lowagie.text.DocumentException throws this exception if cannot decorate the pdf
     * @throws java.io.IOException throws this exception if cannot open/read the pdf contents
     */
    public ByteArrayOutputStream decorate(DecoratorBean decoratorBean, byte pdfContent[])throws DocumentException, IOException {
        List lstDecorations = new ArrayList();
        if(decoratorBean.getHeader() != null && !decoratorBean.getHeader().isEmpty()) {
            for(int index = 0; index < decoratorBean.getHeader().size(); index++) {
                lstDecorations.add(new HeaderDecorator((CommonBean)decoratorBean.getHeader().get(index)));
            }
        }
        
        if(decoratorBean.getFooter() != null && !decoratorBean.getFooter().isEmpty()) {
            for(int index = 0; index < decoratorBean.getFooter().size(); index++) {
                lstDecorations.add(new FooterDecorator((CommonBean)decoratorBean.getFooter().get(index)));
            }
        }
        
        if(decoratorBean.getWatermark() != null && !decoratorBean.getWatermark().isEmpty()) {
            for(int index = 0; index < decoratorBean.getWatermark().size(); index++) {
                lstDecorations.add(new WaterMarkDecorator((CommonBean)decoratorBean.getWatermark().get(index)));
            }
        }
        
        PdfReader reader = new PdfReader(pdfContent);
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        PdfStamper stamp = new PdfStamper(reader, byteArrayOutputStream);
        decorate(stamp, lstDecorations);
        
        return byteArrayOutputStream;
    }
    
    
    
    /**
     * Decorates the PDF
     * 
     * @param pdfStamper pdfStamper instance - wrapper for pdf content byte and assists in decorating PDF
     * @throws com.lowagie.text.DocumentException throws this exception if any error occurs while decorating the document
     * @throws java.io.IOException throws this exception if cannot open/read the file for decoration
     */
    public void decorate(PdfStamper pdfStamper, List lstDecorations)throws DocumentException, IOException {
        PdfReader pDFReader = pdfStamper.getReader();
        
        int noOfPages = pDFReader.getNumberOfPages();
        int page = 0;
        
        Decorator decorator;
        
        PdfContentByte over;
        Rectangle rectangle;
        int pageHeight, pageWidth;
        while (page < noOfPages) {
            page++;
            over = pdfStamper.getOverContent(page);
            rectangle = pDFReader.getPageSizeWithRotation(page);
            pageHeight = (int)rectangle.getHeight();
            pageWidth = (int)rectangle.getWidth();
            
            for(int index=0; index < lstDecorations.size(); index++) {
                decorator = (Decorator)lstDecorations.get(index);
                if(decorator instanceof WaterMarkDecorator) {
                    over = pdfStamper.getUnderContent(page);
                }
                decorator.decorate(over, pageWidth, pageHeight);
                
            }//End For
        }//End While
        
        pdfStamper.close();
        
    }//End decorate
    
    
}//End PDFDecorator
