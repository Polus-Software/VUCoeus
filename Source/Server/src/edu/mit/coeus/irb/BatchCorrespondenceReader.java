/*
 * BatchCorrespondenceReader.java
 *
 * Created on March 30, 2007, 2:43 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package edu.mit.coeus.irb;

import com.lowagie.text.Document;
import com.lowagie.text.pdf.PdfContentByte;
import com.lowagie.text.pdf.PdfDestination;
import com.lowagie.text.pdf.PdfImportedPage;
import com.lowagie.text.pdf.PdfOutline;
import com.lowagie.text.pdf.PdfReader;
import com.lowagie.text.pdf.PdfWriter;
import edu.mit.coeus.exception.CoeusException;
import edu.mit.coeus.irb.bean.CorrespondenceDetailsBean;
import edu.mit.coeus.irb.bean.ScheduleTxnBean;
import edu.mit.coeus.utils.document.CoeusDocument;
import edu.mit.coeus.utils.document.DocumentReader;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Vector;


/**
 *
 * @author sharathk
 */
public class BatchCorrespondenceReader implements DocumentReader{
    
    /**
     * Creates a new instance of BatchCorrespondenceReader
     */
    public BatchCorrespondenceReader() {
    }
    
    public CoeusDocument read(Map map)throws Exception {
        String docType = (String)map.get("DOCUMENT_TYPE");
        CoeusDocument coeusDocument = new CoeusDocument();
        Vector vecRequest = (Vector)map.get("DATA");
        SimpleDateFormat dateFormat= new SimpleDateFormat("MMddyyyy-hhmmss");  
        
        ScheduleTxnBean scheduleTxnBeanView = new ScheduleTxnBean() ;
        Vector vecAllCorrespondence = scheduleTxnBeanView.getCorrespondenceFile(vecRequest) ;
        
        if (vecAllCorrespondence.size()==1) // convert one byte array to file
        {
            byte[] fileBytes = (byte[])vecAllCorrespondence.get(0) ;
           String documentName = "Correspondence"+dateFormat.format(new Date())+".pdf";
            coeusDocument.setDocumentData(fileBytes);
            coeusDocument.setDocumentName(documentName);
            
        } else // merge all byte array
        {

            String documentName ="Correspondence"+dateFormat.format(new Date())+".pdf";
            Document document = null;
            PdfWriter  writer = null;
            ByteArrayOutputStream byteArrayOutputStream = null;
            
            for (int fileCount =0 ; fileCount < vecAllCorrespondence.size() ; fileCount++ ) {
                byte[] fileBytes = (byte[])vecAllCorrespondence.get(fileCount) ;
                
                CorrespondenceDetailsBean  correspondenceDetailsBean = (CorrespondenceDetailsBean)vecRequest.get(fileCount) ;
                
                // we create a reader for a certain document
                PdfReader reader = new PdfReader(fileBytes);
                
                // we retrieve the total number of pages
                int nop = reader.getNumberOfPages();
                
                if (fileCount == 0) // create the first time
                {
                    // step 1: creation of a document-object
                    document = new Document(reader.getPageSizeWithRotation(1));
                    // step 2: we create a writer that listens to the document
                    //writer = new PdfCopy(document, new FileOutputStream(reportFile));
                    byteArrayOutputStream = new ByteArrayOutputStream();
                    BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(byteArrayOutputStream);
                    writer = PdfWriter.getInstance(document, bufferedOutputStream);
                    // step 3: we open the document
                    document.open();
                    
                } //   end if
                
                // step 4: we add content
                PdfContentByte cb = writer.getDirectContent();
                int pageCount = 0 ;
                while (pageCount < nop) {
                    document.newPage();
                    pageCount++;
                    PdfImportedPage page = writer.getImportedPage(reader, pageCount);
                    // cb.addTemplate(page, .5f, 0, 0, .5f, 0, height/2);
                    //                    a, b, c, d, e, f
                    cb.addTemplate(page, 1, 0, 0, 1, 0, 0);
                    
                    PdfOutline root = cb.getRootOutline();
                    if (pageCount == 1) // first page
                    {
                        String pageName = correspondenceDetailsBean.getProtocolNumber()
                        + " - " + correspondenceDetailsBean.getDescription();
                        cb.addOutline(new PdfOutline(root, new PdfDestination(PdfDestination.XYZ), pageName), pageName);
                    }
                    
                } // end while
                
            }// end for
            
            // step 5: we close the document
            document.close();
            
            coeusDocument.setDocumentData(byteArrayOutputStream == null ? null : byteArrayOutputStream.toByteArray());
            coeusDocument.setDocumentName(documentName);
        } // end else
        
        return coeusDocument;
    }
    
    
    public boolean isAuthorized(List lstAuthorizationBean) throws CoeusException {
        return true;
    }
}
