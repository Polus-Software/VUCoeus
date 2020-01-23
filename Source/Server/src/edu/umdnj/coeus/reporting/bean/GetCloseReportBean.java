/**
 * $Header: /home/cvs/cvsroot/coeus/Source/WEB-INF/classes/edu/umdnj/coeus/reporting/bean/Attic/GetCloseReportBean.java,v 1.1.2.9 2007/09/18 18:23:05 cvs Exp $
 * $Log: GetCloseReportBean.java,v $
 * Revision 1.1.2.9  2007/09/18 18:23:05  cvs
 * Modify code for GAFA and START_DATE handling
 *
 * Revision 1.1.2.8  2007/08/16 15:30:09  cvs
 * Add support for Headers per page
 *
 * Revision 1.1.2.7  2007/08/02 19:19:05  cvs
 * Enforce Snippet integrity in PDF output
 *
 * Revision 1.1.2.6  2007/03/27 15:19:20  cvs
 * Modify code to work with Award FnA Distributions
 *
 * Revision 1.1.2.5  2007/02/14 16:34:38  cvs
 * Check for empty reports
 *
 * Revision 1.1.2.4  2007/01/18 21:26:53  cvs
 * Add support for wider PDF tables
 *
 * Revision 1.1.2.3  2007/01/17 15:26:35  cvs
 * Add CSV Support
 *
 * Revision 1.1.2.2  2006/12/26 20:07:33  cvs
 * Clean up on System.out.println to server-side java
 *
 * Revision 1.1.2.1  2006/12/26 18:07:03  cvs
 * Added support for GetClosedReport data retrieval
 *
 *
 */
/*
 * @(#)GetCloseReportBean.java 
 *
 * Copyright (c) University of Medicine and Dentistry of New Jersey
 *
 * 1 World's Fair Drive, Somerset, New Jersey 08873
 *
 * All rights reserved.
 *
 * 
 * Author: Romerl Elizes
 *
 * Description: Reporting Bean to communicate with Oracle DB.
 * 
 */


package edu.umdnj.coeus.reporting.bean;


import java.util.Vector;

import java.util.HashMap;

import java.util.Hashtable;

import java.sql.Date;

import java.sql.Timestamp;

import java.awt.Color;

import java.awt.Font;

import javax.servlet.*;

import javax.servlet.http.*;

import javax.xml.transform.TransformerException;

import java.io.*;

import edu.mit.coeus.utils.dbengine.DBEngineImpl;

import edu.mit.coeus.utils.dbengine.DBException;

import edu.mit.coeus.utils.dbengine.Parameter;

import edu.mit.coeus.utils.dbengine.DBEngineConstants;

import edu.mit.coeus.utils.CoeusVector;

import edu.mit.coeus.utils.UtilFactory;

import edu.mit.coeus.exception.CoeusException;

import edu.mit.coeus.budget.report.ReportGenerator;

import com.lowagie.text.Chunk;
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Paragraph;
import com.lowagie.text.PageSize;
import com.lowagie.text.Image;
import com.lowagie.text.Element;
import com.lowagie.text.FontFactory;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import org.apache.fop.apps.FOPException;
//4122-upgrade stylevision start
//import org.apache.fop.render.mif.fonts.TimesBold;
//4122-upgrade stylevision end

import org.apache.poi.hssf.usermodel.*;

import edu.umdnj.coeus.reporting.bean.UMDNJSQLUtilities;

import edu.umdnj.coeus.utilities.UMDNJStrings;

//import org.apache.poi.hssf.model.Workbook;

public class GetCloseReportBean extends ReportingBaseBean
{

    private int totalActiveAwards  = 0;
    private int totalClosedAwards  = 0;

    private String UnitName = "";
    private String strCloseDate = "";
    private java.sql.Date closeDate = null;
    
    private UMDNJSQLUtilities pobj = null;

    private GrantsObject grantsobj = null;

    /** Creates a new instance of GetCloseReportBean */
    public GetCloseReportBean() 
    {
	super();
        Initialization();
    }
    
    public GetCloseReportBean(String UnitName, String CloseDate)
    {
        super();
        this.UnitName = UnitName;
        this.strCloseDate = CloseDate;
        if (CloseDate != null && CloseDate.length() > 0)
            InitializeCloseDate(CloseDate);
        Initialization();
    }
    
    private void Initialization()
    {
	totalActiveAwards  = 0;
	totalClosedAwards  = 0;
        pobj = new UMDNJSQLUtilities();
    }
    
    public String getSpecificHTMLOutput()
    {
        Vector result = pobj.getCloseReport(UnitName,closeDate);
        StringBuffer buf = new StringBuffer("");
        if (result != null && result.size() > 0)
        {
            HashMap row = null;
            int listSize = 0;
            if (result != null)
               listSize = result.size();
             if (listSize > 0)
             {
                String UnitSchool = getUnitSchool(UnitName);
                buf.append("<h2>Detailed List of Closed Awards for "+UnitSchool+"</h2>");
		buf.append("<table border=1>");	
                buf.append("<tr>");
                    buf.append(printObj("<b><u>Award Status</u></b>"));
                    buf.append(printObj("<b><u>PI Name</u></b>"));
                    buf.append(printObj("<b><u>PI Unit Name</u></b>"));
                    buf.append(printObj("<b><u>Title</u></b>"));
                    buf.append(printObj("<b><u>Anticipated End Date</u></b>"));
                    buf.append(printObj("<b><u>Begin Date</u></b>"));
                    buf.append(printObj("<b><u>Sponsor Name</u></b>"));
                    buf.append(printObj("<b><u>OSRP Award Number</u></b>"));
                    buf.append(printObj("<b><u>Anticipated Total Amount</u></b>")); 
                buf.append("</tr>");
            	for(int rowNum = 0; rowNum < listSize; rowNum++)
		{
                    buf.append("<tr>");
                    row=(HashMap)result.elementAt(rowNum); 
                    Object str1  = row.get("DESCRIPTION");
                    Object str2  = row.get("PERSON_NAME");
                    Object str3  = row.get("UNIT_NAME");
                    Object str4  = row.get("TITLE");
                    Object str5  = row.get("OBLIGATION_EXPIRATION_DATE");
                    Object str6  = row.get("CURRENT_FUND_EFFECTIVE_DATE");
                    Object str7  = row.get("SPONSOR_NAME");
                    Object str8  = row.get("MIT_AWARD_NUMBER");
                    Object str9  = row.get("ANTICIPATED_TOTAL_AMOUNT"); 
                    buf.append(printObj(str1));
                    buf.append(printObj(str2));
                    buf.append(printObj(str3));
                    buf.append(printObj(str4));
                    buf.append(printObj(str5));
                    buf.append(printObj(str6));
                    buf.append(printObj(str7));
                    buf.append(printObj(str8));
                    buf.append(printObj(str9));
                    buf.append("</tr>");
                }
    

        	buf.append("</table>");
            }
            else
		buf.append(UMDNJStrings.NoRecordsComingBack);
        }
        else
            buf.append(UMDNJStrings.NoRecordsComingBack);
        return buf.toString();
        
    }
    
    
    public void getSpecificPDFOutput(HttpServletResponse res)
         throws ServletException, IOException
    {
	totalActiveAwards  = 0;
	totalClosedAwards  = 0;
        Vector result = pobj.getCloseReport(UnitName,closeDate);
        if (result != null && result.size() > 0)
        {
            String UnitSchool = getUnitSchool(UnitName);
            Document document = new Document(PageSize.LETTER);
	    width = document.getPageSize().width();
            try 
            {
                HashMap row = null;
                int listSize = 0;
                if (result != null)
                    listSize = result.size();
                if (listSize > 0)
                {
                    res.setContentType("application/pdf");
                    PdfWriter.getInstance(document, res.getOutputStream());
                    
                    document.open();
 
                    PdfPTable table  = new PdfPTable(8);
                    table.getDefaultCell().setBorder(0);
                    table.setHorizontalAlignment(0);
                    table.setTotalWidth(width - 72);
                    table.setLockedWidth(true);
                    table.setHeaderRows(7);
                    FormatHeader(table);                

                    for (int inum = 0; inum < listSize; inum++)
                    {           
                        row=(HashMap)result.elementAt(inum); 
                        FormatRecord(row, table);
                    }

                    FormatFooter(table);                
                    document.add(table);
                    table.setWidthPercentage(100);   
                }
            }
            catch(DocumentException de) {
                de.printStackTrace();
                System.err.println("document: " + de.getMessage());
            }
            document.close();
       }
       else
       {
           res.setContentType("text/html");
           PrintWriter writer = res.getWriter();
           writer.println("<html><body>");
           String UnitSchool = getUnitSchool(UnitName);
           writer.print(UnitSchool + " does not have grants");
           writer.println("</body></html>"); 
       }
    }

    private void FormatHeader(PdfPTable table)
    	throws DocumentException
    {
        String UnitSchool = getUnitSchool(UnitName);
        Paragraph a = new Paragraph();
        a.add(new Chunk(UnitSchool+"\nReport on Awards That Should Be Closed - "+strCloseDate,
        		FontFactory.getFont(FontFactory.TIMES, 12, Font.BOLD, Color.BLACK)));                    
        PdfPCell cell1 = new PdfPCell(a);
        cell1.setColspan(8);
        cell1.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell1.setNoWrap(true);
        cell1.setBorderColor(Color.white);
        table.addCell(cell1);

        Paragraph b1 = new Paragraph();
        b1.add(new Chunk("",
        		FontFactory.getFont(FontFactory.TIMES, 10, Font.BOLD, Color.WHITE)));
        PdfPCell cellb1 = new PdfPCell(b1);
        cellb1.setColspan(8);
        cellb1.setNoWrap(true);
        cellb1.setBackgroundColor(Color.WHITE);
        cellb1.setBorderColor(Color.WHITE);
        table.addCell(cellb1);
        
        
        Paragraph b = new Paragraph();
        b.add(new Chunk("",
        		FontFactory.getFont(FontFactory.TIMES, 10, Font.BOLD, Color.BLACK)));
        PdfPCell cell2 = new PdfPCell(b);
        cell2.setColspan(8);
        cell2.setNoWrap(true);
        cell2.setBackgroundColor(Color.BLACK);
        table.addCell(cell2);
        
        Paragraph c1 = new Paragraph();
        c1.add(new Chunk("Status",
        		FontFactory.getFont(FontFactory.HELVETICA, 7, Font.BOLD, Color.BLACK)));
        PdfPCell cellc1 = new PdfPCell(c1);
        cellc1.setHorizontalAlignment(Element.ALIGN_CENTER);
        cellc1.setNoWrap(true);
        cellc1.setBorderColor(Color.white);
        table.addCell(cellc1);

        Paragraph c2 = new Paragraph();
        c2.add(new Chunk("Award Number\n",
        		FontFactory.getFont(FontFactory.HELVETICA, 7, Font.BOLD, Color.BLACK)));
        PdfPCell cellc2 = new PdfPCell(c2);
        cellc2.setHorizontalAlignment(Element.ALIGN_CENTER);
        cellc2.setNoWrap(true);
        cellc2.setBorderColor(Color.white);
        table.addCell(cellc2);
        
        Paragraph c3 = new Paragraph();
        c3.add(new Chunk("PI Name\nProject Title\nSponsor Name",
        		FontFactory.getFont(FontFactory.HELVETICA, 7, Font.BOLD, Color.BLACK)));
        PdfPCell cellc3 = new PdfPCell(c3);
        cellc3.setHorizontalAlignment(Element.ALIGN_LEFT);
        cellc3.setColspan(3);
        cellc3.setNoWrap(true);
        cellc3.setBorderColor(Color.white);
        table.addCell(cellc3);

        Paragraph c4 = new Paragraph();
        c4.add(new Chunk("Project Period\nEnd Date",
        		FontFactory.getFont(FontFactory.HELVETICA, 7, Font.BOLD, Color.BLACK)));
        PdfPCell cellc4 = new PdfPCell(c4);
        cellc4.setHorizontalAlignment(Element.ALIGN_CENTER);
        cellc4.setNoWrap(true);
        cellc4.setBorderColor(Color.white);
        table.addCell(cellc4);

        Paragraph c5 = new Paragraph();
        c5.add(new Chunk("Total Project\nStart Date",
        		FontFactory.getFont(FontFactory.HELVETICA, 7, Font.BOLD, Color.BLACK)));
        PdfPCell cellc5 = new PdfPCell(c5);
        cellc5.setHorizontalAlignment(Element.ALIGN_CENTER);
        cellc5.setNoWrap(true);
        cellc5.setBorderColor(Color.white);
        table.addCell(cellc5);

        Paragraph c6 = new Paragraph();
        c6.add(new Chunk("Award Amount",
        		FontFactory.getFont(FontFactory.HELVETICA, 7, Font.BOLD, Color.BLACK)));
        PdfPCell cellc6 = new PdfPCell(c6);
        cellc6.setHorizontalAlignment(Element.ALIGN_CENTER);
        cellc6.setNoWrap(true);
        cellc6.setBorderColor(Color.white);
        table.addCell(cellc6);

        Paragraph d = new Paragraph();
        b.add(new Chunk("",
        		FontFactory.getFont(FontFactory.TIMES, 10, Font.BOLD, Color.BLACK)));
        PdfPCell celld = new PdfPCell(d);
        celld.setColspan(8);
        celld.setNoWrap(true);
        celld.setBackgroundColor(Color.BLACK);
        table.addCell(celld);

        Paragraph d1 = new Paragraph();
        d1.add(new Chunk("",
        		FontFactory.getFont(FontFactory.TIMES, 10, Font.BOLD, Color.WHITE)));
        PdfPCell celld1 = new PdfPCell(d1);
        celld1.setColspan(8);
        celld1.setNoWrap(true);
        celld1.setBackgroundColor(Color.WHITE);
        celld1.setBorderColor(Color.WHITE);
        table.addCell(celld1);
        
        Paragraph f = new Paragraph();
        f.add(new Chunk("",
        		FontFactory.getFont(FontFactory.HELVETICA, 10, Font.BOLD, Color.BLACK)));
        PdfPCell cellf = new PdfPCell(f);
        cellf.setColspan(8);
        cellf.setNoWrap(true);
        cellf.setBackgroundColor(Color.white);
        cellf.setBorderColor(Color.white);
        table.addCell(cellf);
    }
    
    private void FormatRecord(HashMap row, PdfPTable table)
    	throws DocumentException
    {
        PdfPTable subtable  = new PdfPTable(8);
        subtable.getDefaultCell().setBorder(0);
        subtable.setHorizontalAlignment(0);
        subtable.setTotalWidth(width - 72);
        subtable.setLockedWidth(true);

	Object cellAWARD_STATUS   = row.get("DESCRIPTION");
	String strAWARD_STATUS = getCellValue(cellAWARD_STATUS).trim();
        Color statuscolor;
        if (strAWARD_STATUS.compareTo("Active")==0) 
        {
           statuscolor = Color.RED ;
           totalActiveAwards++;
        }
        else
        {
           statuscolor = Color.BLACK;
           totalClosedAwards++;
        }

        Paragraph a = new Paragraph();
        a.add(new Chunk(strAWARD_STATUS,
        		FontFactory.getFont(FontFactory.TIMES, 7, Font.BOLD, statuscolor)));                    
        PdfPCell cella = new PdfPCell(a);
        cella.setHorizontalAlignment(Element.ALIGN_LEFT);
        cella.setNoWrap(true);
        cella.setBorderColor(Color.white);
        subtable.addCell(cella);

	Object cellAWARD_NUMBER   = row.get("MIT_AWARD_NUMBER");
	String strAWARD_NUMBER = getCellValue(cellAWARD_NUMBER).trim();
        FormatDataCell(subtable,strAWARD_NUMBER,Color.white,Color.white,Element.ALIGN_LEFT,Font.PLAIN,7,1);

	Object cellPI_NAME    = row.get("PERSON_NAME");
	String strPI_NAME = getCellValue(cellPI_NAME).trim();
        FormatDataCell(subtable,strPI_NAME,Color.white,Color.white,Element.ALIGN_LEFT,Font.PLAIN,7,3);

	Object cellEND_DATE = row.get("OBLIGATION_EXPIRATION_DATE");
	String strEND_DATE = getCellValue(cellEND_DATE).trim();
        FormatDataCell(subtable,strEND_DATE,Color.white,Color.white,Element.ALIGN_LEFT,Font.PLAIN,7,1);

	Object cellBEGIN_DATE = row.get("CURRENT_FUND_EFFECTIVE_DATE");
	String strBEGIN_DATE = getFormattedDate(getCellValue(cellBEGIN_DATE).trim());
        FormatDataCell(subtable,strBEGIN_DATE,Color.white,Color.white,Element.ALIGN_LEFT,Font.PLAIN,7,1);

	Object cellTOTAL_AMOUNT = row.get("ANTICIPATED_TOTAL_AMOUNT");
	double dTOTAL_AMOUNT    = getNumericCellValue(cellTOTAL_AMOUNT);
	StringBuffer dbuf = new StringBuffer("");
        dbuf.append(dTOTAL_AMOUNT);
        FormatDataCell(subtable,dbuf.toString(),Color.white,Color.white,Element.ALIGN_RIGHT,Font.PLAIN,7,1);

        FormatDataCell(subtable," ",Color.white,Color.white,Element.ALIGN_LEFT,Font.BOLD,7,1);

	String strGAFA_NUMBER = "";
        FormatDataCell(subtable,strGAFA_NUMBER,Color.white,Color.white,Element.ALIGN_LEFT,Font.PLAIN,7,1);

	Object cellTITLE     = row.get("TITLE" );
	String strTITLE = getCellValue(cellTITLE).trim();
        FormatDataCell(subtable,strTITLE,Color.white,Color.white,Element.ALIGN_LEFT,Font.ITALIC,7,6);

        FormatDataCell(subtable," ",Color.white,Color.white,Element.ALIGN_LEFT,Font.BOLD,7,2);

	Object cellSPONSOR_NAME   = row.get("SPONSOR_NAME");
	String strSPONSOR_NAME = getCellValue(cellSPONSOR_NAME).trim();
        FormatDataCell(subtable,strSPONSOR_NAME,Color.white,Color.white,Element.ALIGN_LEFT,Font.PLAIN,7,6);

        FormatDataCell(subtable," ",Color.white,Color.white,Element.ALIGN_LEFT,Font.BOLD,10,8);

        PdfPCell cell11 = new PdfPCell(subtable);
        cell11.setBorderColor(Color.white);
        cell11.setColspan(8);
        cell11.setNoWrap(true);
        table.addCell(cell11);
    }
    
        private void FormatFooter(PdfPTable table)
    	throws DocumentException
    {
        Paragraph a = new Paragraph();
        a.add(new Chunk("",
        		FontFactory.getFont(FontFactory.TIMES, 10, Font.BOLD, Color.WHITE)));
        PdfPCell cella = new PdfPCell(a);
        cella.setColspan(8);
        cella.setNoWrap(true);
        cella.setBackgroundColor(Color.WHITE);
        cella.setBorderColor(Color.WHITE);
        table.addCell(cella);

        Paragraph c = new Paragraph();
        c.add(new Chunk("",
        		FontFactory.getFont(FontFactory.TIMES, 10, Font.PLAIN, Color.BLACK)));
        PdfPCell cellc = new PdfPCell(c);
        cellc.setColspan(8);
        cellc.setNoWrap(true);
        cellc.setBackgroundColor(Color.BLACK);
        cellc.setBorderColor(Color.BLACK);
        table.addCell(cellc);

        Paragraph d1 = new Paragraph();
        d1.add(new Chunk("Total Active Awards:",
        		FontFactory.getFont(FontFactory.TIMES, 10, Font.BOLD, Color.BLACK)));                    
        PdfPCell celld1 = new PdfPCell(d1);
        celld1.setHorizontalAlignment(Element.ALIGN_LEFT);
        celld1.setColspan(2);
        celld1.setNoWrap(true);
        celld1.setBorderColor(Color.lightGray);
        celld1.setBackgroundColor(Color.lightGray);
        table.addCell(celld1);


        Paragraph e = new Paragraph();
	StringBuffer buf = new StringBuffer("");
        buf.append(totalActiveAwards);
        e.add(new Chunk(buf.toString(),
        		FontFactory.getFont(FontFactory.TIMES, 10, Font.BOLD, Color.BLACK)));                    
        PdfPCell celle = new PdfPCell(e);
        celle.setColspan(2);
        celle.setHorizontalAlignment(Element.ALIGN_LEFT);
        celle.setNoWrap(true);
        celle.setBorderColor(Color.lightGray);
        celle.setBackgroundColor(Color.lightGray);
        table.addCell(celle);

        Paragraph f = new Paragraph();
        f.add(new Chunk("Total Closed Awards:",
        		FontFactory.getFont(FontFactory.TIMES, 10, Font.BOLD, Color.BLACK)));                    
        PdfPCell cellf = new PdfPCell(f);
        cellf.setHorizontalAlignment(Element.ALIGN_LEFT);
        cellf.setColspan(2);
        cellf.setNoWrap(true);
        cellf.setBorderColor(Color.lightGray);
        cellf.setBackgroundColor(Color.lightGray);
        table.addCell(cellf);

	StringBuffer abuf = new StringBuffer("");
        abuf.append(totalClosedAwards);
        Paragraph g = new Paragraph();
        g.add(new Chunk(abuf.toString(),
        		FontFactory.getFont(FontFactory.TIMES, 10, Font.BOLD, Color.BLACK)));                    
        PdfPCell cellg = new PdfPCell(g);
        cellg.setHorizontalAlignment(Element.ALIGN_LEFT);
        cellg.setColspan(2);
        cellg.setNoWrap(true);
        cellg.setBorderColor(Color.lightGray);
        cellg.setBackgroundColor(Color.lightGray);
        table.addCell(cellg);

    }


    
    


        private String getUnitSchool(String UnitName)
        {
            String strCand = "";
            if (UnitName.compareTo("DS")==0)
                strCand = "New Jersey Dental School";
            
            else if (UnitName.compareTo("NJMS") == 0)
                strCand = "New Jersey Medical School";
            else if (UnitName.compareTo("RWJ") == 0)
                strCand = "Robert Wood Johnson Medical School";
            else if (UnitName.compareTo("SOM") == 0)
                strCand = "School of Osteopathic Medicine";
            else if (UnitName.compareTo("SHRP") == 0)
                strCand = "School of Health-Related Professions";
            else if (UnitName.compareTo("SPH") == 0)
                strCand = "School of Public Health";
            else if (UnitName.compareTo("SN") == 0)
                strCand = "School of Nursing";
            return strCand;
        }
         
    public void getSpecificXLSOutput(HttpServletResponse res)
         throws ServletException, IOException
    {
        Vector result = pobj.getCloseReport(UnitName,closeDate);
        if (result != null && result.size() > 0)
        {
            HashMap dbrow = null;
	    HSSFWorkbook wb = new HSSFWorkbook();
            String UnitSchool = getUnitSchool(UnitName);
            res.setContentType("application/vnd.ms-excel");
            ServletOutputStream out = res.getOutputStream();
	
	    //setting the sheet name as file name without extension.

	    HSSFSheet sheet = wb.createSheet("Closed Awards for "+UnitSchool);

	    // Create a row and put some cells in it. Rows are 0 based.

	    HSSFRow row = null;

	    HSSFCell cell;        

	    HSSFCellStyle cellStyle = wb.createCellStyle();

	    cellStyle.setDataFormat(HSSFDataFormat.getBuiltinFormat("text"));
        

	    HSSFFont headerFont = wb.createFont();

	    headerFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
        
	    HSSFCellStyle headerStyle = wb.createCellStyle();

	    headerStyle.setFont(headerFont);
            StringBuffer sbuf = new StringBuffer();

            row = sheet.createRow((short)0);

                cell = row.createCell((short)0);
                    cell.setCellValue("Award Status");
                cell = row.createCell((short)1);
                    cell.setCellValue("PI Name");
                cell = row.createCell((short)2);
                    cell.setCellValue("PI Unit");
                cell = row.createCell((short)3);
                    cell.setCellValue("Title");
                cell = row.createCell((short)4);
                    cell.setCellValue("End Date");
                cell = row.createCell((short)5);
                    cell.setCellValue("Begin Date");
                cell = row.createCell((short)6);
                    cell.setCellValue("Sponsor Name");
                cell = row.createCell((short)7);
                    cell.setCellValue("Award Number");
                cell = row.createCell((short)8);
                    cell.setCellValue("Total Amount"); 

                     //Make Headers BOLD

           	cell.setCellStyle(headerStyle);
            int listSize = result.size();
            
            for (int i=0; i < listSize; i++) 
            {
                dbrow=(HashMap)result.elementAt(i); 
                row = sheet.createRow((short)i + 1); //+1 since First Row is Header Row

                    Object str1  = dbrow.get("DESCRIPTION");
                    Object str2  = dbrow.get("PERSON_NAME");
                    Object str3  = dbrow.get("UNIT_NAME");
                    Object str4  = dbrow.get("TITLE");
                    Object str5  = dbrow.get("OBLIGATION_EXPIRATION_DATE");
                    Object str6  = dbrow.get("CURRENT_FUND_EFFECTIVE_DATE");
                    Object str7  = dbrow.get("SPONSOR_NAME");
                    Object str8  = dbrow.get("MIT_AWARD_NUMBER");
                    Object str9 = dbrow.get("ANTICIPATED_TOTAL_AMOUNT"); 
                    addToXLSCell(str1,0,row);
                    addToXLSCell(str2,1,row);
                    addToXLSCell(str3,2,row);
                    addToXLSCell(str4,3,row);
                    addToXLSCell(str5,4,row);
                    addToXLSCell(str6,5,row);
                    addToXLSCell(str7,6,row);
                    addToXLSCell(str8,7,row);
                    addToXLSCell(str9,8,row);

            }

            wb.write(out);
            out.close();
       }
       else
       {
           res.setContentType("text/html");
           PrintWriter writer = res.getWriter();
           writer.println("<html><body>");
           String UnitSchool = getUnitSchool(UnitName);
           writer.print(UnitSchool + " does not have grants");
           writer.println("</body></html>"); 
       }

    }    
    
    private void InitializeCloseDate(String CloseDate)
    {
        //CloseDate description
        // 01-01-2006
        // 0123456789
        String strmonth = CloseDate.substring(0,2);
        String strday = CloseDate.substring(3,5);
        String stryear = CloseDate.substring(6);
        try {
            
            int imonth = Integer.parseInt(strmonth);
            int iday = Integer.parseInt(strday);
            int iyear = Integer.parseInt(stryear);
            
            closeDate = new java.sql.Date(iyear-1900,imonth,iday);
        }
        catch (NumberFormatException ex) {
            System.out.println("NumberFormatException : Fiscal Year "+CloseDate+" is not recognized.");
            ex.printStackTrace(System.out);
        }
        catch (IllegalArgumentException ex) {
            System.out.println("IllegalArgumentException: Fiscal Year "+CloseDate+" is not recognized.");
            ex.printStackTrace(System.out);
        }
    }
    
    
    public String getSpecificCSVOutput()
    {
        Vector result = pobj.getCloseReport(UnitName,closeDate);
        StringBuffer buf = new StringBuffer("");
        if (result != null && result.size() > 0)
        {
            HashMap row = null;
            int listSize = 0;
            if (result != null)
               listSize = result.size();
             if (listSize > 0)
             {
                    buf.append(printPlainObj("Award Status"));
                    buf.append(",");
                    buf.append(printPlainObj("PI Name"));
                    buf.append(",");
                    buf.append(printPlainObj("PI Unit Name"));
                    buf.append(",");
                    buf.append(printPlainObj("Title"));
                    buf.append(",");
                    buf.append(printPlainObj("Anticipated End Date"));
                    buf.append(",");
                    buf.append(printPlainObj("Begin Date"));
                    buf.append(",");
                    buf.append(printPlainObj("Sponsor Name"));
                    buf.append(",");
                    buf.append(printPlainObj("OSRP Award Number"));
                    buf.append(",");
                    buf.append(printPlainObj("Anticipated Total Amount")); 
                buf.append("\n");
            	for(int rowNum = 0; rowNum < listSize; rowNum++)
		{
                    row=(HashMap)result.elementAt(rowNum); 
                    Object str1  = row.get("DESCRIPTION");
                    Object str2  = row.get("PERSON_NAME");
                    Object str3  = row.get("UNIT_NAME");
                    Object str4  = row.get("TITLE");
                    Object str5  = row.get("OBLIGATION_EXPIRATION_DATE");
                    Object str6  = row.get("CURRENT_FUND_EFFECTIVE_DATE");
                    Object str7  = row.get("SPONSOR_NAME");
                    Object str8  = row.get("MIT_AWARD_NUMBER");
                    Object str9  = row.get("ANTICIPATED_TOTAL_AMOUNT"); 
                    buf.append(printPlainObj(str1));
                    buf.append(",");
                    buf.append(printPlainObj(str2));
                    buf.append(",");
                    buf.append(printPlainObj(str3));
                    buf.append(",");
                    buf.append(printPlainObj(str4));
                    buf.append(",");
                    buf.append(printPlainObj(str5));
                    buf.append(",");
                    buf.append(printPlainObj(str6));
                    buf.append(",");
                    buf.append(printPlainObj(str7));
                    buf.append(",");
                    buf.append(printPlainObj(str8));
                    buf.append(",");
                    buf.append(printPlainObj(str9));
                    buf.append("\n");
                }
            }
            else
		buf.append(UMDNJStrings.NoRecordsComingBack);
        }
        else
            buf.append(UMDNJStrings.NoRecordsComingBack);
        return buf.toString();
        
    }
}


