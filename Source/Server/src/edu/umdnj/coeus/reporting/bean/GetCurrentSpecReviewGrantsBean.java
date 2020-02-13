/**
 * $Header: /home/cvs/cvsroot/coeus/Source/WEB-INF/classes/edu/umdnj/coeus/reporting/bean/Attic/GetCurrentSpecReviewGrantsBean.java,v 1.1.2.6 2007/09/18 18:23:05 cvs Exp $
 * $Log: GetCurrentSpecReviewGrantsBean.java,v $
 * Revision 1.1.2.6  2007/09/18 18:23:05  cvs
 * Modify code for GAFA and START_DATE handling
 *
 * Revision 1.1.2.5  2007/08/16 15:30:09  cvs
 * Add support for Headers per page
 *
 * Revision 1.1.2.4  2007/08/02 19:19:05  cvs
 * Enforce Snippet integrity in PDF output
 *
 * Revision 1.1.2.3  2007/07/31 13:36:19  cvs
 * Add Annual Reports server-side code
 * Move many methods to base class Reporting Base Bean
 *
 * Revision 1.1.2.2  2007/07/17 15:59:26  cvs
 * Display reports that total costs of 0 but with valid dates
 *
 * Revision 1.1.2.1  2007/06/21 19:14:46  cvs
 * Add support for IRB and IACUC Grant reports
 *
 *
 */
/*
 * @(#)GetCurrentSpecReviewGrantsBean.java 
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

import edu.mit.coeus.utils.DateUtils;

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
import org.apache.poi.hssf.usermodel.*;
//4122-upgrade stylevision start
//import org.apache.fop.render.mif.fonts.TimesBold;
//4122-upgrade stylevision end
import edu.umdnj.coeus.reporting.bean.UMDNJSQLUtilities;
import edu.umdnj.coeus.utilities.UMDNJStrings;

//import org.apache.poi.hssf.model.Workbook;

public class GetCurrentSpecReviewGrantsBean extends ReportingBaseBean
{

    private int runningTotalDirectCost;
    private int runningTotalIndirectCost;
    private int runningTotalCost;

    private String UnitName = "";
    java.util.Date CurrentDate = null;
    private Timestamp CurrentTimeStamp = null;
    private String DeptName = "";
    private boolean bNIH = false;
    private int iSpecReviewType = 1;
    private String ReportType = "IRB";
    
    private UMDNJSQLUtilities pobj = null;

    private GrantsObject grantsobj = null;

    /** Creates a new instance of GetCurrentSpecReviewGrantsBean */
    public GetCurrentSpecReviewGrantsBean() 
    {
	super();
        Initialization();
    }
    
    public GetCurrentSpecReviewGrantsBean(String UnitName)
    {
        super();
        this.UnitName = UnitName;
        Initialization();
    }
    
    private void Initialization()
    {
        pobj = new UMDNJSQLUtilities();
        DeptName = "";
        CurrentDate = new java.util.Date();
        CurrentTimeStamp = new Timestamp(CurrentDate.getTime());
    }
    
    public String getSpecificHTMLOutput()
    {
        Vector result = pobj.getCurrentSpecReviewGrants(UnitName,CurrentDate,iSpecReviewType);
        StringBuffer buf = new StringBuffer("");
        if (result != null && result.size() > 0)
        {
            HashMap row = null;
            int listSize = 0;
            if (result != null)
               listSize = result.size();
             if (listSize > 0)
             {
                int currow = 0;
                DeptName = getUnitSchool(UnitName);
                if (DeptName.length() == 0)
                {
                  row = (HashMap)result.elementAt(0);
                  Object str = row.get("UNIT_NAME");
                  DeptName = (String)str;
                }
                buf.append("<h2>"+ReportType+" Award List for "+DeptName+", Date:"+getCellValue(CurrentTimeStamp)+"</h2><br>");
		buf.append("<table cellspacing=0 cellpadding=2>");	
                buf.append(getHTMLHeadings());

            	for(int rowNum = 0; rowNum < listSize; rowNum++)
		{
                    row=(HashMap)result.elementAt(rowNum); 
                    if (handleMITAwardFor0001(row)==true)
                        continue;
                    currow++;
                    buf.append(FormatHtmlRecord(row,currow));
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
        Vector result = pobj.getCurrentSpecReviewGrants(UnitName,CurrentDate,iSpecReviewType);
        if (result != null && result.size() > 0)
        {
            DeptName = getUnitSchool(UnitName);
            if (DeptName.length() == 0)
            {
              HashMap arow = (HashMap)result.elementAt(0);
              Object str = arow.get("UNIT_NAME");
              DeptName = (String)str;
            } 
            Document document = new Document(PageSize.A4.rotate());
	    width = document.getPageSize().width();
            try 
            {
                HashMap row = null;
                int listSize = 0;
                if (result != null)
                    listSize = result.size();
                int ititlechanged = 0;
                if (listSize > 0)
                {
                    res.setContentType("application/pdf");
                    PdfWriter.getInstance(document, res.getOutputStream());
                    
                    document.open();
 
                    PdfPTable table  = new PdfPTable(9);
                    float[] widths = {0.15f, 0.15f, 0.1f, 0.1f, 0.1f, 0.1f, 0.1f, 0.1f, 0.1f};
                    table.setWidths(widths);
                    table.getDefaultCell().setBorder(0);
                    table.setHorizontalAlignment(0);
                    table.setTotalWidth(width - 72);
                    table.setLockedWidth(true);
                    table.setHeaderRows(4);

                    Vector avector = new Vector();
                    for (int inum = 0; inum < listSize; inum++)
                    {
                        row = (HashMap)result.elementAt(inum);
                        if (handleMITAwardFor0001(row)==true)
                            continue;
                        avector.addElement(row);
                    }

                    if (avector.size() < 0)
                        avector = result;
                    listSize = avector.size();
                    
                    FormatHeader(table);
                    for (int inum = 0; inum < listSize; inum++)
                    {
                            row = (HashMap)avector.elementAt(inum);                            
                            FormatRecord(row, table);
                    }
                    displayTotals(table);                    	

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
           DeptName = getUnitSchool(UnitName);
           writer.print(DeptName + " does not have grants");
           if (bNIH == true)
               writer.println(" sponsored by the National Institutes of Health.");
           else
               writer.println(".");
           writer.println("</body></html>"); 
       }
    }

    private void FormatHeader(PdfPTable table)
    	throws DocumentException
    {
        Paragraph a = new Paragraph();
        String strhead = DeptName+"\nCurrent Active Research, Service, and Training Grants\nActive "+ReportType+" Grants and Contracts";
        strhead += "\nDate: "+DateUtils.formatDate(CurrentTimeStamp,"MM/dd/yyyy");
        a.add(new Chunk(strhead,
        		FontFactory.getFont(FontFactory.TIMES, 12, Font.BOLD, Color.BLACK)));                    
        PdfPCell cell1 = new PdfPCell(a);
        cell1.setColspan(9);
        cell1.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell1.setNoWrap(true);
        cell1.setBorderColor(Color.white);
        table.addCell(cell1);
        
        Paragraph b = new Paragraph();
        b.add(new Chunk("",
        		FontFactory.getFont(FontFactory.TIMES, 12, Font.BOLD, Color.BLACK)));
        PdfPCell cell2 = new PdfPCell(b);
        cell2.setColspan(9);
        cell2.setNoWrap(true);
        cell2.setBackgroundColor(Color.BLACK);
        table.addCell(cell2);
        
        Paragraph c1 = new Paragraph();
        c1.add(new Chunk("ORSP Number\nProject Period\nProject Total Cost",
        		FontFactory.getFont(FontFactory.HELVETICA, 8, Font.BOLD, Color.BLACK)));
        PdfPCell cellc1 = new PdfPCell(c1);
        cellc1.setNoWrap(true);
        cellc1.setBorderColor(Color.white);
        table.addCell(cellc1);

        Paragraph c2 = new Paragraph();
        c2.add(new Chunk("PI Name\nProject Title",
        		FontFactory.getFont(FontFactory.HELVETICA, 8, Font.BOLD, Color.BLACK)));
        PdfPCell cellc2 = new PdfPCell(c2);
        cellc2.setNoWrap(true);
        cellc2.setBorderColor(Color.white);
        table.addCell(cellc2);

        Paragraph c3 = new Paragraph();
        c3.add(new Chunk("Sponsor Name",
        		FontFactory.getFont(FontFactory.HELVETICA, 8, Font.BOLD, Color.BLACK)));
        PdfPCell cellc3 = new PdfPCell(c3);
        cellc3.setColspan(2);
        cellc3.setNoWrap(true);
        cellc3.setBorderColor(Color.white);
        table.addCell(cellc3);

        Paragraph c4 = new Paragraph();
        c4.add(new Chunk("Award Period\nStart Date",
        		FontFactory.getFont(FontFactory.HELVETICA, 8, Font.BOLD, Color.BLACK)));
        PdfPCell cellc4 = new PdfPCell(c4);
        cellc4.setNoWrap(true);
        cellc4.setHorizontalAlignment(Element.ALIGN_CENTER);
        cellc4.setBorderColor(Color.white);
        table.addCell(cellc4);

        Paragraph c5 = new Paragraph();
        c5.add(new Chunk("Award Period\nEnd Date",
        		FontFactory.getFont(FontFactory.HELVETICA, 8, Font.BOLD, Color.BLACK)));
        PdfPCell cellc5 = new PdfPCell(c5);
        cellc5.setNoWrap(true);
        cellc5.setHorizontalAlignment(Element.ALIGN_CENTER);
        cellc5.setBorderColor(Color.white);
        table.addCell(cellc5);

        Paragraph c6 = new Paragraph();
        c6.add(new Chunk("Award Period\nDirect Cost",
        		FontFactory.getFont(FontFactory.HELVETICA, 8, Font.BOLD, Color.BLACK)));
        PdfPCell cellc6 = new PdfPCell(c6);
        cellc6.setNoWrap(true);
        cellc6.setHorizontalAlignment(Element.ALIGN_CENTER);
        cellc6.setBorderColor(Color.white);
        table.addCell(cellc6);

        Paragraph c7 = new Paragraph();
        c7.add(new Chunk("Award Period\nIndirect Cost",
        		FontFactory.getFont(FontFactory.HELVETICA, 8, Font.BOLD, Color.BLACK)));
        PdfPCell cellc7 = new PdfPCell(c7);
        cellc7.setNoWrap(true);
        cellc7.setHorizontalAlignment(Element.ALIGN_CENTER);
        cellc7.setBorderColor(Color.white);
        table.addCell(cellc7);

        Paragraph c8 = new Paragraph();
        c8.add(new Chunk("Award Period\nTotal Cost",
        		FontFactory.getFont(FontFactory.HELVETICA, 8, Font.BOLD, Color.BLACK)));
        PdfPCell cellc8 = new PdfPCell(c8);
        cellc8.setNoWrap(true);
        cellc8.setHorizontalAlignment(Element.ALIGN_CENTER);
        cellc8.setBorderColor(Color.white);
        table.addCell(cellc8);

        Paragraph d = new Paragraph();
        d.add(new Chunk("",
        		FontFactory.getFont(FontFactory.TIMES, 12, Font.BOLD, Color.BLACK)));
        PdfPCell cell4 = new PdfPCell(d);
        cell4.setColspan(9);
        cell4.setNoWrap(true);
        cell4.setBackgroundColor(Color.BLACK);
        table.addCell(cell4);
    }
    
    private void FormatRecord(HashMap row, PdfPTable table)
    	throws DocumentException
    {
        	Object cell = row.get("MIT_AWARD_NUMBER");
        	String straccount = ((String)cell).trim();	
                int index1 = straccount.indexOf('-');
                if (index1 > -1)
                    straccount = straccount.substring(0,index1);
        	grantsobj = new GrantsObject(straccount);    
        	populateCurrentGrantsObject(row);
        	handleCostValues(row);
                grantsobj.setTotalCost(grantsobj.getDirectCost()+grantsobj.getIndirectCost());
        	displayGrantsObjContents(table);
        	runningTotalDirectCost += grantsobj.getDirectCost();
        	runningTotalIndirectCost += grantsobj.getIndirectCost();
        	runningTotalCost += grantsobj.getTotalCost();
    }
    
    private void FormatSubHeader(HashMap row, PdfPTable table)
    {
    	Object cell = row.get("UNIT_NAME");
    	String strtitle = ((String)cell).trim();

    	int index = strtitle.indexOf("-"+UnitName);
    	if (index > 0)
    		strtitle = strtitle.substring(0,index);
    	
        Paragraph a = new Paragraph();
        a.add(new Chunk(strtitle,
        		FontFactory.getFont(FontFactory.TIMES, 10, Font.BOLD, Color.BLACK)));                    
        PdfPCell cell1 = new PdfPCell(a);
        cell1.setColspan(8);
        cell1.setHorizontalAlignment(Element.ALIGN_LEFT);
        cell1.setNoWrap(true);
        cell1.setBorderColor(Color.white);
        table.addCell(cell1);
    }
    
    private void populateCurrentGrantsObject(HashMap row)
    {
        Object begindatecell  = row.get("CURRENT_FUND_EFFECTIVE_DATE");
        Object enddatecell  = row.get("OBLIGATION_EXPIRATION_DATE");
        Object totalamountcell  = row.get("ANTICIPATED_TOTAL_AMOUNT");
        Object titlecell  = row.get("TITLE");
        Object sponsorcell  = row.get("SPONSOR_NAME");
        Object investigatorcell  = row.get("PERSON_NAME"); 
        Object totalenddatecell = row.get("END_DATE");

	grantsobj.setBeginProjectDate(getCellValue(begindatecell).trim());
	grantsobj.setEndProjectDate(getCellValue(enddatecell).trim());
	grantsobj.setProjectTotalCost(getNumericCellValue(totalamountcell));
	grantsobj.setProjectTitle(getCellValue(titlecell).trim());
	grantsobj.setSponsorName(getCellValue(sponsorcell).trim());
	grantsobj.setInvestigatorName(getCellValue(investigatorcell).trim());
        if (fiscalYear == 0)
        {
            grantsobj.setAwardBeginDate(getCellValue(begindatecell).trim());    	
            grantsobj.setAwardEndDate(getCellValue(totalenddatecell).trim());    	
            grantsobj.setTotalCost(getNumericCellValue(totalamountcell));
        }
}

	private void handleCostValues(HashMap row)
	{
		if (grantsobj == null)
			return;

                Object totalbegindatecell = row.get("START_DATE");
                grantsobj.setAwardBeginDate(getCellValue(totalbegindatecell).trim());    	

                Object totalenddatecell = row.get("END_DATE");
                grantsobj.setAwardEndDate(getCellValue(totalenddatecell).trim());    	
                
		Object directcostcell = row.get("DIRECT_COST");
		double val1 = getNumericCellValue(directcostcell);
		grantsobj.addDirectCost(val1);
		
		Object indirectcostcell = row.get("INDIRECT_COST");
		double val2 = getNumericCellValue(indirectcostcell);
		grantsobj.addIndirectCost(val2);
	}

	private void displayGrantsObjContents(PdfPTable table)
    	throws DocumentException
	{
            if (grantsobj.getAwardBeginDate().length()==0 && 
                    grantsobj.getAwardEndDate().length()==0)
                return;
		
        PdfPTable subtable  = new PdfPTable(9);
        float[] widths = {0.15f, 0.15f, 0.1f, 0.1f, 0.1f, 0.1f, 0.1f, 0.1f, 0.1f};
        subtable.setWidths(widths);
        subtable.getDefaultCell().setBorder(0);
        subtable.setHorizontalAlignment(0);
        subtable.setTotalWidth(width - 72);
        subtable.setLockedWidth(true);

        String formatAccountNumber = grantsobj.getAccountNumber();
        if (grantsobj.getGafaNumber() != null && grantsobj.getGafaNumber().length()>0)
           formatAccountNumber += " ("  + grantsobj.getGafaNumber() + ")";
        FormatDataCell(subtable,formatAccountNumber,Color.white,Color.white,Element.ALIGN_LEFT,Font.PLAIN,8,1);
        FormatDataCell(subtable,grantsobj.getInvestigatorName(),Color.white,Color.white,Element.ALIGN_LEFT,Font.PLAIN,7,1);
        FormatDataCell(subtable,grantsobj.getSponsorName(),Color.white,Color.white,Element.ALIGN_LEFT,Font.PLAIN,7,2);
        FormatDataCell(subtable,grantsobj.getAwardBeginDate(),Color.white,Color.white,Element.ALIGN_CENTER,Font.PLAIN,8,1);
        FormatDataCell(subtable,grantsobj.getAwardEndDate(),Color.white,Color.white,Element.ALIGN_CENTER,Font.PLAIN,8,1);
        FormatDataCell(subtable,getCurrencyFormattedNumber(grantsobj.getDirectCost()),Color.white,Color.white,Element.ALIGN_CENTER,Font.PLAIN,8,1);
        FormatDataCell(subtable,getCurrencyFormattedNumber(grantsobj.getIndirectCost()),Color.white,Color.white,Element.ALIGN_CENTER,Font.PLAIN,8,1);
        FormatDataCell(subtable,getCurrencyFormattedNumber(grantsobj.getTotalCost()),Color.white,Color.white,Element.ALIGN_CENTER,Font.PLAIN,8,1);

        StringBuffer strbufd = new StringBuffer("");
        strbufd.append(getFormattedDate(grantsobj.getBeginProjectDate()));
        strbufd.append(" - ");
        strbufd.append(getFormattedDate(grantsobj.getEndProjectDate()));

        FormatDataCell(subtable,strbufd.toString(),Color.white,Color.white,Element.ALIGN_LEFT,Font.PLAIN,8,1);
        FormatDataCell(subtable,grantsobj.getProjectTitle(),Color.white,Color.white,Element.ALIGN_LEFT,Font.ITALIC,8,8);
        FormatDataCell(subtable,getCurrencyFormattedNumber(grantsobj.getProjectTotalCost()),Color.white,Color.white,Element.ALIGN_LEFT,Font.PLAIN,8,9);
        FormatDataCell(subtable," ",Color.white,Color.white,Element.ALIGN_LEFT,Font.PLAIN,8,9);

        PdfPCell cell11 = new PdfPCell(subtable);
        cell11.setBorderColor(Color.white);
        cell11.setColspan(9);
        cell11.setNoWrap(true);
        table.addCell(cell11);
	}

	private void displayTotals(PdfPTable table)
	{
        Paragraph a = new Paragraph();
        a.add(new Chunk("",
        		FontFactory.getFont(FontFactory.HELVETICA, 8, Font.PLAIN, Color.BLACK)));
        PdfPCell cell1 = new PdfPCell(a);
        cell1.setBorderColor(Color.white);
        cell1.setColspan(6);
        cell1.setNoWrap(true);
        table.addCell(cell1);

        Paragraph b = new Paragraph();
        b.add(new Chunk("",
        		FontFactory.getFont(FontFactory.TIMES, 8, Font.PLAIN, Color.BLACK)));
        PdfPCell cell2 = new PdfPCell(b);
        cell2.setBorderColor(Color.white);
        cell2.setColspan(3);
        cell2.setNoWrap(true);
        table.addCell(cell2);

        Paragraph c = new Paragraph();
        a.add(new Chunk(" ",
        		FontFactory.getFont(FontFactory.HELVETICA, 8, Font.PLAIN, Color.BLACK)));
        PdfPCell cell3 = new PdfPCell(c);
        cell3.setBorderColor(Color.white);
        cell3.setColspan(6);
        cell3.setNoWrap(true);
        table.addCell(cell3);

        Paragraph e = new Paragraph();
        e.add(new Chunk(getCurrencyFormattedNumber(runningTotalDirectCost),
        		FontFactory.getFont(FontFactory.HELVETICA, 8, Font.PLAIN, Color.BLACK)));
        PdfPCell cell5 = new PdfPCell(e);
        cell5.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell5.setBorderColor(Color.white);
        cell5.setBackgroundColor(Color.lightGray);
        cell5.setNoWrap(true);
        table.addCell(cell5);

        Paragraph f = new Paragraph();
        f.add(new Chunk(getCurrencyFormattedNumber(runningTotalIndirectCost),
        		FontFactory.getFont(FontFactory.HELVETICA, 8, Font.PLAIN, Color.BLACK)));
        PdfPCell cell6 = new PdfPCell(f);
        cell6.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell6.setBorderColor(Color.white);
        cell6.setBackgroundColor(Color.lightGray);
        cell6.setNoWrap(true);
        table.addCell(cell6);

        Paragraph g = new Paragraph();
        g.add(new Chunk(getCurrencyFormattedNumber(runningTotalCost),
        		FontFactory.getFont(FontFactory.HELVETICA, 8, Font.PLAIN, Color.BLACK)));
        PdfPCell cell7 = new PdfPCell(g);
        cell7.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell7.setBorderColor(Color.white);
        cell7.setBackgroundColor(Color.lightGray);
        cell7.setNoWrap(true);
        table.addCell(cell7);
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
        Vector result = pobj.getCurrentSpecReviewGrants(UnitName,CurrentDate,iSpecReviewType);
        if (result != null && result.size() > 0)
        {
            HashMap dbrow = null;
	    HSSFWorkbook wb = new HSSFWorkbook();
            String UnitSchool = getUnitSchool(UnitName);
            res.setContentType("application/vnd.ms-excel");
            ServletOutputStream out = res.getOutputStream();
	
	    //setting the sheet name as file name without extension.

	    HSSFSheet sheet = wb.createSheet("Grants for "+UnitSchool);

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
                    cell.setCellValue("Award Number");
           	    cell.setCellStyle(headerStyle);
                cell = row.createCell((short)1);
                    cell.setCellValue("Unit Name");
           	    cell.setCellStyle(headerStyle);
                cell = row.createCell((short)2);
                    cell.setCellValue("Current Effective Date");
           	    cell.setCellStyle(headerStyle);
                cell = row.createCell((short)3);
                    cell.setCellValue("Obligation Expiration Date");
           	    cell.setCellStyle(headerStyle);
                cell = row.createCell((short)4);
                    cell.setCellValue("Anticipated Total Amount");
           	    cell.setCellStyle(headerStyle);
                cell = row.createCell((short)5);
                    cell.setCellValue("Parent Unit");
           	    cell.setCellStyle(headerStyle);
                cell = row.createCell((short)6);
                    cell.setCellValue("Sponsor Name");
           	    cell.setCellStyle(headerStyle);
                cell = row.createCell((short)7);
                    cell.setCellValue("Investigator"); 
           	    cell.setCellStyle(headerStyle);
                cell = row.createCell((short)8);
                    cell.setCellValue("Start Date");
           	    cell.setCellStyle(headerStyle);
                cell = row.createCell((short)9);
                    cell.setCellValue("End Date");
           	    cell.setCellStyle(headerStyle);
                cell = row.createCell((short)10);
                    cell.setCellValue("Direct Cost");
           	    cell.setCellStyle(headerStyle);
                cell = row.createCell((short)11);
                    cell.setCellValue("Indirect Cost");
           	    cell.setCellStyle(headerStyle);
                cell = row.createCell((short)12);
                    cell.setCellValue("Title");
           	    cell.setCellStyle(headerStyle);

            int listSize = result.size();
            int ilist = 0;
            for (int i=0; i < listSize; i++) 
            {
                dbrow=(HashMap)result.elementAt(i); 
                if (handleMITAwardFor0001(dbrow)==true)
                        continue;
                row = sheet.createRow((short)ilist + 1); //+1 since First Row is Header Row
                ilist++;

                    Object str1  = dbrow.get("MIT_AWARD_NUMBER");
                    Object str2  = dbrow.get("UNIT_NAME");
                    Object str3  = dbrow.get("CURRENT_FUND_EFFECTIVE_DATE");
                    Object str4  = dbrow.get("OBLIGATION_EXPIRATION_DATE");
                    Object str5  = dbrow.get("ANTICIPATED_TOTAL_AMOUNT");
                    Object str6  = dbrow.get("PARENT_UNIT_NUMBER");
                    Object str7  = dbrow.get("SPONSOR_NAME");
                    Object str8  = dbrow.get("PERSON_NAME"); 
                    Object str9 = dbrow.get("START_DATE");
                    Object str10 = dbrow.get("END_DATE");
                    Object str11 = dbrow.get("DIRECT_COST");
                    Object str12 = dbrow.get("INDIRECT_COST");
                    Object str13  = dbrow.get("TITLE");
                    addToXLSCell(str1,0,row);
                    addToXLSCell(str2,1,row);
                    addToXLSCell(str3,2,row);
                    addToXLSCell(str4,3,row);
                    addToXLSCell(str5,4,row);
                    addToXLSCell(str6,5,row);
                    addToXLSCell(str7,6,row);
                    addToXLSCell(str8,7,row);
                    addToXLSCell(str9,8,row);
                    addToXLSCell(str10,9,row);
                    addToXLSCell(str11,10,row);
                    addToXLSCell(str12,11,row);
                    addToXLSCell(str13,12,row);

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
           if (bNIH == true)
               writer.println(" sponsored by the National Institutes of Health.");
           else
               writer.println(".");
           writer.println("</body></html>"); 
       }

    }    
            
    public void setSpecReviewType(String SpecReviewType)
    {
        if (SpecReviewType != null && SpecReviewType.compareTo("1")==0)
        {
            iSpecReviewType = 1;
            ReportType = "IRB";
        }
        else
        {
            iSpecReviewType = 2;
            ReportType = "IACUC";
        }
    }
    
    public String getSpecificCSVOutput()
    {
        Vector result = pobj.getCurrentSpecReviewGrants(UnitName,CurrentDate,iSpecReviewType);
        StringBuffer buf = new StringBuffer("");
        if (result != null && result.size() > 0)
        {
            HashMap row = null;
            int listSize = 0;
            if (result != null)
               listSize = result.size();
             if (listSize > 0)
             {
                    buf.append(printPlainObj("Award Number"));
                    buf.append(",");
                    buf.append(printPlainObj("Unit Name"));
                    buf.append(",");
                    buf.append(printPlainObj("Current Effective Date"));
                    buf.append(",");
                    buf.append(printPlainObj("Obligation Expiration Date"));
                    buf.append(",");
                    buf.append(printPlainObj("Anticipated Total Amount"));
                    buf.append(",");
                    buf.append(printPlainObj("Parent Unit"));
                    buf.append(",");
                    buf.append(printPlainObj("Sponsor Name"));
                    buf.append(",");
                    buf.append(printPlainObj("Investigator")); 
                    buf.append(",");              
                    buf.append(printPlainObj("Start Date"));
                    buf.append(",");              
                    buf.append(printPlainObj("End Date"));
                    buf.append(",");
                    buf.append(printPlainObj("Direct Cost"));
                    buf.append(",");
                    buf.append(printPlainObj("Indirect Cost"));
                    buf.append(",");
                    buf.append(printPlainObj("Title"));
                buf.append("\n");
            	for(int rowNum = 0; rowNum < listSize; rowNum++)
		{
                    row=(HashMap)result.elementAt(rowNum); 
                    if (handleMITAwardFor0001(row)==true)
                        continue;
                    Object str1  = row.get("MIT_AWARD_NUMBER");
                    Object str2  = row.get("UNIT_NAME");
                    Object str3  = row.get("CURRENT_FUND_EFFECTIVE_DATE");
                    Object str4  = row.get("OBLIGATION_EXPIRATION_DATE");
                    Object str5  = row.get("ANTICIPATED_TOTAL_AMOUNT");
                    Object str6  = row.get("PARENT_UNIT_NUMBER");
                    Object str7  = row.get("SPONSOR_NAME");
                    Object str8  = row.get("PERSON_NAME"); 
                    Object str9 = row.get("START_DATE");
                    Object str10 = row.get("END_DATE");
                    Object str11 = row.get("DIRECT_COST");
                    Object str12 = row.get("INDIRECT_COST");
                    Object str13  = row.get("TITLE");
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
                    buf.append(",");
                    buf.append(printPlainObj(str10));
                    buf.append(",");
                    buf.append(printPlainObj(str11));
                    buf.append(",");
                    buf.append(printPlainObj(str12));
                    buf.append(",");
                    buf.append(printPlainObj(str13));
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
    
    public void setDeptName(String val)
    {
        DeptName = val;
    }
    
    private boolean handleMITAwardFor0001(HashMap row)
    {
        	Object cell = row.get("MIT_AWARD_NUMBER");
        	String straccount = ((String)cell).trim();
        	int index1 = straccount.indexOf("-001");
                boolean bfound = false;
                if (index1 > 0)
                   bfound = true;
                return bfound;
    }

     public String getHTMLHeadings()
     {
         StringBuffer buf = new StringBuffer("");
         buf.append("<tr>");
         buf.append("<td style='border-bottom-color:black'><b><u>Award Number</u></b>");
         buf.append("<td style='border-bottom-color:black'><b><u>Unit Name</u></b>");
         buf.append("<td style='border-bottom-color:black'><b><u>Current Effective Date</u></b>");
         buf.append("<td style='border-bottom-color:black'><b><u>Obligation Expiration Date</u></b>");
         buf.append("<td style='border-bottom-color:black'><b><u>Anticipated Total Amount</u></b>");
         buf.append("<td style='border-bottom-color:black'><b><u>Parent Unit</u></b>");
         buf.append("<td style='border-bottom-color:black'><b><u>Sponsor Name</u></b>");
         buf.append("<td style='border-bottom-color:black'><b><u>Investigator</u></b>"); 
         buf.append("<td style='border-bottom-color:black'><b><u>Start Date</u></b>");
         buf.append("<td style='border-bottom-color:black'><b><u>End Date</u></b>");
         buf.append("<td style='border-bottom-color:black'><b><u>Direct Cost</u></b>");
         buf.append("<td style='border-bottom-color:black'><b><u>Indirect Cost</u></b>");

         buf.append("</tr>");
         return buf.toString();
     }

     public String FormatHtmlRecord(HashMap row, int currow)
     {
         StringBuffer buf = new StringBuffer("");

         Object obj1  = row.get("MIT_AWARD_NUMBER");
         Object obj2  = row.get("UNIT_NAME");
         Object obj3  = row.get("CURRENT_FUND_EFFECTIVE_DATE");
         Object obj4  = row.get("OBLIGATION_EXPIRATION_DATE");
         Object obj5  = row.get("ANTICIPATED_TOTAL_AMOUNT");
         Object obj6  = row.get("PARENT_UNIT_NUMBER");
         Object obj7  = row.get("SPONSOR_NAME");
         Object obj8  = row.get("PERSON_NAME"); 
         Object obj9 = row.get("START_DATE");
         Object obj10 = row.get("END_DATE");
         Object obj11 = row.get("DIRECT_COST");
         Object obj12 = row.get("INDIRECT_COST");
         Object obj13  = row.get("TITLE");


         String str1 = getCellValue(obj1);
         String str2 = getCellValue(obj2);
         String str3 = getCellValue(obj3);
         String str4 = getCellValue(obj4);
         String str5 = getCellValue(obj5);
         String str6 = getCellValue(obj6);
         String str7 = getCellValue(obj7);
         String str8 = getCellValue(obj8);
         String str9 = getCellValue(obj9);
         String str10 = getCellValue(obj10);
         String str11 = getCellValue(obj11);
         String str12 = getCellValue(obj12);
         String str13 = getCellValue(obj13);

         if (str1.indexOf('-')>-1)
         {
            int inum = str1.indexOf('-');
            str1 = str1.substring(0,inum);
         }

         String backgroundcolor = "gray";
         int ival = currow % 2;
         if (ival == 0)
            backgroundcolor = "background-color:#D3D3D3;font:Times New Roman;font-size:12";
         else
            backgroundcolor = "background-color:#FFFFE0;font:Times New Roman;font-size:12";

         buf.append("<tr>");
         String formatAccountNumber = str1;

         buf.append("<td style='"+backgroundcolor+"'>"+formatAccountNumber+"</td>");
         buf.append("<td style='"+backgroundcolor+"'>"+str2+"</td>");
         buf.append("<td style='"+backgroundcolor+"'>"+str3+"</td>");
         buf.append("<td style='"+backgroundcolor+"'>"+str4+"</td>");
         buf.append("<td style='"+backgroundcolor+"'>"+str5+"</td>");
         buf.append("<td style='"+backgroundcolor+"'>"+str6+"</td>");
         buf.append("<td style='"+backgroundcolor+"'>"+str7+"</td>");
         buf.append("<td style='"+backgroundcolor+"'>"+str8+"</td>");
         buf.append("<td style='"+backgroundcolor+"'>"+str9+"</td>");
         buf.append("<td style='"+backgroundcolor+"'>"+str10+"</td>");
         buf.append("<td style='"+backgroundcolor+"'>"+str11+"</td>");
         buf.append("<td style='"+backgroundcolor+"'>"+str12+"</td>");

         buf.append("</tr>");

         buf.append("<tr>");
         buf.append("<td colspan=12 style='"+backgroundcolor+"'><b>Title:&nbsp;&nbsp;</b>"+str13+"</td>");
         buf.append("</tr>");
         return buf.toString();
     }

}


