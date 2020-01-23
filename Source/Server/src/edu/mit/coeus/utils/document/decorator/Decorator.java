/*
 * Decorator.java
 *
 * Created on October 31, 2007, 3:10 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package edu.mit.coeus.utils.document.decorator;

import com.lowagie.text.pdf.PdfContentByte;

/**
 *
 * @author sharathk
 */
public interface Decorator {
    
    public void decorate(PdfContentByte pdfContentByte, int width, int height);
    
}
