/*
 * CoeusTextPane.java
 *
 * Created on December 22, 2004, 4:12 PM
 * @author  bijosht
 */

/**
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 */

package edu.mit.coeus.utils;

import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.print.PageFormat;
import java.awt.print.Printable;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
 
import javax.print.attribute.HashPrintRequestAttributeSet;
import javax.print.attribute.PrintRequestAttributeSet;
import javax.print.attribute.standard.Copies;
import javax.print.attribute.standard.MediaPrintableArea;
import javax.print.attribute.standard.MediaSizeName;
 
import javax.swing.text.View;
import javax.swing.JTextArea;
import java.awt.Color;
import javax.swing.JTextPane;
import javax.swing.RepaintManager;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;
 /*
  * Class created to print the contents.
  * It extends JtextPane and implements Printable
  */
public class CoeusTextPane extends JTextPane implements Printable
{
  private MediaSizeName defaultSize = MediaSizeName.ISO_A4;
  private MediaPrintableArea defaultPrintableArea = new MediaPrintableArea(15f, 15f, 187f, 267f, MediaPrintableArea.MM);
  private boolean scaleWidthToFit = true;
  private double pageEndY = 0;
  private double pageStartY = 0;
  private int currentPage = -1;
  public boolean printUsingViews = true;
 
  public CoeusTextPane()
  {
    super();
  }
 
 //Printable interface implementation (uses PritView recursivly)
  public int print(Graphics graphics, PageFormat pageFormat, int pageIndex) throws PrinterException
  {
    if (printUsingViews)
    {
      return printUsingViews(graphics, pageFormat, pageIndex);
    }else{
      return simplePrint(graphics, pageFormat, pageIndex);
    }
  }

  //This print method is simpler but has one problem it may cut one line and print upper half of the line on one page and lower half on the second
  public int simplePrint(Graphics g, PageFormat pf, int pageIndex) throws PrinterException
  {
     Graphics2D g2 = (Graphics2D) g;
    //for faster printing, turn off double buffering
    RepaintManager.currentManager(this).setDoubleBufferingEnabled(false);
    Dimension d = this.getSize(); //get size of document
    double panelWidth = d.width; //width in pixels
    double panelHeight = d.height; //height in pixels
    double pageHeight = pf.getImageableHeight(); //height of printer page
    double pageWidth = pf.getImageableWidth(); //width of printer page
 
    double scale = pageWidth / panelWidth;
    int totalNumPages = (int) Math.ceil((scale * panelHeight) / pageHeight);
 
    //make sure not print empty pages
    if (pageIndex >= totalNumPages)
    {
      return Printable.NO_SUCH_PAGE;
    }
    //shift Graphic to line up with beginning of print-imageable region
    g2.translate(pf.getImageableX(), pf.getImageableY());
    if (pf.getOrientation()==PageFormat.LANDSCAPE) {
        //shift Graphic to line up with beginning of next page to print
        g2.translate(0f, -pageIndex * pageHeight);
        //scale the page so the width fits...
        g2.scale(scale, scale);
    }
 
    this.paint(g2); //repaint the page for printing
 
    return Printable.PAGE_EXISTS;
  }
 
  //This print method uses views(splits text into views) and if the view (one line for example) does not fit entirely on the current page it will be printed entirely one the next page
  public int printUsingViews(Graphics graphics, PageFormat pageFormat, int pageIndex) throws PrinterException
  {
    Dimension d = this.getSize(); //get size of document
    double panelWidth = d.width; //width in pixels
    double panelHeight = d.height; //height in pixels
 
    double pageHeight = pageFormat.getImageableHeight(); //height of printer page
    double pageWidth = pageFormat.getImageableWidth(); //width of printer page
 
    double scale = pageWidth / panelWidth;
    View rootView;
    Graphics2D g2 = (Graphics2D) graphics;
    rootView = this.getUI().getRootView(this);
    g2.scale(scale, scale);
    g2.setClip((int) (pageFormat.getImageableX() / scale), (int) (pageFormat.getImageableY() / scale), (int) (pageFormat.getImageableWidth() / scale), (int) (pageFormat.getImageableHeight() / scale));
    if (pageIndex > currentPage)
    {
      currentPage = pageIndex;
      pageStartY = pageStartY + pageEndY;
      pageEndY = g2.getClipBounds().getHeight();
    }
 
    g2.translate(g2.getClipBounds().getX(), g2.getClipBounds().getY());
 
    Rectangle allocationToPrint = new Rectangle(0, (int) -pageStartY, (int) (getMinimumSize().getWidth()), (int) (getPreferredSize().getHeight()));
 
    if (printView(g2, allocationToPrint, rootView))
    {
      return Printable.PAGE_EXISTS;
    }
    else
    {
      pageStartY = 0;
      pageEndY = 0;
      currentPage = -1;
      return Printable.NO_SUCH_PAGE;
    }
  }
 
  //Used in printUsingView method recursively
  protected boolean printView(Graphics2D graphics2D, Shape allocationToPrint, View view)
  {
    boolean pageExists = false;
    Rectangle clipRectangle = graphics2D.getClipBounds();
    Shape childAllocation;
    View childView;
 
    if ((view.getViewCount() > 0)&&(!view.getClass().getName().equals("javax.swing.text.html.TableView$CellView")))
    {
      for (int i = 0; i < view.getViewCount(); i++)
      {
        childAllocation = view.getChildAllocation(i, allocationToPrint);
 
        if (childAllocation != null)
        {
          childView = view.getView(i);
 
          if (printView(graphics2D, childAllocation, childView))
          {
            pageExists = true;
          }
        }
      }
    }
    else
    {
      double allocBoundsHeight = allocationToPrint.getBounds().getHeight();
      double allocBoundsY = allocationToPrint.getBounds().getY();
      double allocBoundsMaxY = allocationToPrint.getBounds().getMaxY();
      double clipY = clipRectangle.getY();
      double clipMaxY = clipRectangle.getMaxY();
      double clipHeight = clipRectangle.getHeight();
      boolean allocIntersectsClip = allocationToPrint.intersects(clipRectangle);
 
      if (allocBoundsMaxY > clipY)
      {
        pageExists = true;
 
        if ((allocBoundsHeight > clipHeight) && allocIntersectsClip)
        {
          view.paint(graphics2D, allocationToPrint);
        }
        else
        {
          if (allocBoundsY >= clipY) //allocBoundsY is changing because initially allocation slides backwards and view.getChildAllocation returns backwardly sliden allocations
          //for example for page 0 firstView allocation is 0,0,h,w while for page 1 allocation is 0,-954,h,w where -954 is pageEndY with minus
          {
            if ((allocBoundsMaxY <= clipMaxY))
            //allocBoundsMaxY is changing because initially allocation slides backwards and view.getChildAllocation returns backwardly sliden allocations
            {
              if (allocBoundsY <= pageEndY)
              {
                view.paint(graphics2D, allocationToPrint);
              }
            }
            else
            { //allocation maximum excedes clip max Y and should not be printed
              
              if (allocBoundsY < pageEndY) //if allocation maximum is less than page end than reduce page end to set next initial allocation
              {
                pageEndY = allocationToPrint.getBounds().getY();
              }
            }
          }
        }
      }else if (allocBoundsMaxY == clipY){
//        strange to get here, System.out.println("(allocBoundsMaxY == clipY) ->"+allocBoundsMaxY +","+ clipY);
      }
    }
    return pageExists;
  }
 
  //Helper methods for printing with or withoud print dialog
  public void printWithoutDialog()
  {
    printWithoutDialog("Java Printing", 1);
  }
 
  public void printWithoutDialog(int copiesQuantity)
  {
    printWithoutDialog("Java Printing", copiesQuantity);
  }
 
  public void printWithoutDialog(String jobName, int copiesQuantity)
  {
    setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
 
    PrintRequestAttributeSet aset = new HashPrintRequestAttributeSet();
    aset.add(new Copies(copiesQuantity));
    aset.add(defaultSize);
    aset.add(defaultPrintableArea);
 
    PrinterJob job = PrinterJob.getPrinterJob();
    job.setJobName(jobName);
    job.setPrintable(this);
 
    try
    {
      job.print(aset);
    }
    catch (Exception ex)
    {
      ex.printStackTrace();
    }
    setCursor(Cursor.getDefaultCursor());
  }
 
 
  public boolean showPrintDialog()
  {
    return showPrintDialog("Java Printing");
  }
 
 
  public boolean showPrintDialog(String jobName)
  {
    setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
 
    PrintRequestAttributeSet aset = new HashPrintRequestAttributeSet();
    aset.add(defaultSize);
    aset.add(defaultPrintableArea);
    PrinterJob job = PrinterJob.getPrinterJob();
    job.setPrintable(this);
    job.setJobName(jobName);
 
    if (job.printDialog(aset))
    {
      try
      {
        job.print(aset);
        setCursor(Cursor.getDefaultCursor());
 
        return true;
      }
      catch (Exception ex)
      {
        ex.printStackTrace();
 
        return false;
      }
    }
    else
    {
      setCursor(Cursor.getDefaultCursor());
 
      return false;
    }
  }
  
  //Main Method for testing
  public static void main(String[] args)
  {
    javax.swing.JFrame frame = new javax.swing.JFrame("PrintableTextArea");
    javax.swing.JScrollPane scroll = new javax.swing.JScrollPane();
    frame.getContentPane().setLayout(new java.awt.BorderLayout());
 
    final CoeusTextPane pane = new CoeusTextPane();
    pane.setStyle(pane);
    scroll.getViewport().add(pane, null);
    frame.getContentPane().add(scroll, java.awt.BorderLayout.CENTER);
    frame.getContentPane().add(new javax.swing.JButton(new javax.swing.AbstractAction("PRINT")
      {
        public void actionPerformed(java.awt.event.ActionEvent e)
        {
          pane.showPrintDialog();
        }
      }), java.awt.BorderLayout.SOUTH);
    frame.setSize(600, 450);
    frame.setDefaultCloseOperation(javax.swing.JFrame.EXIT_ON_CLOSE);
    frame.setVisible(true);
  }
  
  private void setStyle(JTextPane textPane) {
    
    textPane.setText(" sdkfhs  sdfshdflkhasd fsadhflkshdf ls  shfsdhffh  sdfhsdhfksdfhsdfh  ksdf hdsfh shfskdh sfs");
    
    StyledDocument doc = textPane.getStyledDocument();
    
    // Makes text red
    Style style = textPane.addStyle("Red", null);
    StyleConstants.setForeground(style, Color.red);
    
    // Inherits from "Red"; makes text red and underlined
    style = textPane.addStyle("Red Underline", style);
    StyleConstants.setUnderline(style, true);
    
    // Makes text 24pts
    style = textPane.addStyle("24pts", null);
    StyleConstants.setFontSize(style, 24);
    
    // Makes text 12pts
    style = textPane.addStyle("12pts", null);
    StyleConstants.setFontSize(style, 20);
    
    // Makes text italicized
    style = textPane.addStyle("Italic", null);
    StyleConstants.setItalic(style, true);
    
    // A style can have multiple attributes; this one makes text bold and italic
    style = textPane.addStyle("Bold Italic", null);
    StyleConstants.setBold(style, true);
    StyleConstants.setItalic(style, true);
    
    // Set text in the range [5, 7) red
    doc.setCharacterAttributes(5, 2, textPane.getStyle("Red"), true);
    
    // Italicize the entire paragraph containing the position 12
    doc.setParagraphAttributes(12, 1, textPane.getStyle("Italic"), true);
    
    doc.setCharacterAttributes(10, 10, textPane.getStyle("12pts"), true);
    
    
  }
  
}


