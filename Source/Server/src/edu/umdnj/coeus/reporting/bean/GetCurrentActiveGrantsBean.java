/**
 * $Header: /home/cvs/cvsroot/coeus/Source/WEB-INF/classes/edu/umdnj/coeus/reporting/bean/Attic/GetCurrentActiveGrantsBean.java,v 1.1.2.10 2007/09/18 18:23:05 cvs Exp $
 * $Log: GetCurrentActiveGrantsBean.java,v $
 * Revision 1.1.2.10  2007/09/18 18:23:05  cvs
 * Modify code for GAFA and START_DATE handling
 *
 * Revision 1.1.2.9  2007/08/16 18:11:45  cvs
 * 1. Add support for sorting by PI and Department
 * 2. Move sorting methods to parent class
 *
 * Revision 1.1.2.8  2007/08/02 19:19:05  cvs
 * Enforce Snippet integrity in PDF output
 *
 * Revision 1.1.2.7  2007/07/31 13:36:19  cvs
 * Add Annual Reports server-side code
 * Move many methods to base class Reporting Base Bean
 *
 * Revision 1.1.2.6  2007/07/25 18:40:02  cvs
 * Add Headers and Snippet integrity in PDF
 *
 * Revision 1.1.2.5  2007/07/17 15:59:26  cvs
 * Display reports that total costs of 0 but with valid dates
 *
 * Revision 1.1.2.4  2007/07/05 14:07:49  cvs
 * Added department to third line of PDF output
 *
 * Revision 1.1.2.3  2007/06/18 17:17:46  cvs
 * Add check for Enforce Project End Date
 *
 * Revision 1.1.2.2  2007/06/11 15:39:13  cvs
 * Make sure that Sponsor Name and Project Title have nowrap = FALSE
 *
 * Revision 1.1.2.1  2007/03/29 16:22:44  cvs
 * Add support for Current Active Grants Report
 *
 *
 */
/*
 * @(#)GetCurrentActiveGrantsBean.java 
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
import java.text.SimpleDateFormat;
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
import com.lowagie.text.ExceptionConverter;
import com.lowagie.text.Paragraph;
import com.lowagie.text.PageSize;
import com.lowagie.text.Rectangle;
import com.lowagie.text.Image;
import com.lowagie.text.pdf.PdfPageEventHelper;
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
import edu.umdnj.coeus.reporting.bean.ReportingBaseBean;
import edu.umdnj.coeus.utilities.UMDNJStrings;

//import org.apache.poi.hssf.model.Workbook;

public class GetCurrentActiveGrantsBean
               extends ReportingBaseBean
{

    private int runningTotalDirectCost;
    private int runningTotalIndirectCost;
    private int runningTotalCost;

    private String UnitName = "";
    Timestamp CurrentDate = null;
    private String DeptName = "";
    private boolean bNIH = false;
    private boolean bEnforceEndDate;
    private boolean bRepeatHeader;
    private UMDNJSQLUtilities pobj = null;


    private Vector grantsvector = null;
    private GrantsObject grantsobj = null;

    /** Creates a new instance of GetCurrentActiveGrantsBean */
    public GetCurrentActiveGrantsBean() 
    {
	super();
        InitializeCurrentDate();
        Initialization();
    }
    
    public GetCurrentActiveGrantsBean(String UnitName)
    {
        super();
        this.UnitName = UnitName;
        InitializeCurrentDate();
        Initialization();
    }
    
    private void Initialization()
    {
        pobj = new UMDNJSQLUtilities();
        grantsvector = new Vector();
        DeptName = "";
    }
    
    public String getSpecificHTMLOutput()
    {
        Vector result = pobj.getCurrentActiveGrants(UnitName);
        StringBuffer buf = new StringBuffer("");
        if (result != null && result.size() > 0)
        {
            HashMap row = null;
            int listSize = 0;
            if (result != null)
               listSize = result.size();
             if (listSize > 0)
             {
                DeptName = getUnitSchool(UnitName);
                if (DeptName.length() == 0)
                {
                  row = (HashMap)result.elementAt(0);
                  Object str = row.get("UNIT_NAME");
                  DeptName = (String)str;
                }
     
                buf.append("<h2>Detailed Award List for "+DeptName+"</h2>");
		buf.append("<table border=1>");	
                buf.append("<tr>");
                    buf.append(printObj("<b><u>Award Number</u></b>"));
                    buf.append(printObj("<b><u>Unit Name</u></b>"));
                    buf.append(printObj("<b><u>Current Effective Date</u></b>"));
                    buf.append(printObj("<b><u>Obligation Expiration Date</u></b>"));
                    buf.append(printObj("<b><u>Anticipated Total Amount</u></b>"));
                    buf.append(printObj("<b><u>Parent Unit</u></b>"));
                    buf.append(printObj("<b><u>Title</u></b>"));
                    buf.append(printObj("<b><u>Sponsor Name</u></b>"));
                    buf.append(printObj("<b><u>Investigator</u></b>")); 
                    buf.append(printObj("<b><u>Start Date</u></b>"));
                    buf.append(printObj("<b><u>End Date</u></b>"));
                    buf.append(printObj("<b><u>Direct Cost</u></b>"));
                    buf.append(printObj("<b><u>Indirect Cost</u></b>"));
                buf.append("</tr>");
            	for(int rowNum = 0; rowNum < listSize; rowNum++)
		{
                    row=(HashMap)result.elementAt(rowNum); 
                    if (handleMITAwardForDate(row)==false)
                        continue;
                    buf.append("<tr>");
                    Object str1  = row.get("MIT_AWARD_NUMBER");
                    Object str2  = row.get("UNIT_NAME");
                    Object str3  = row.get("CURRENT_FUND_EFFECTIVE_DATE");
                    Object str4  = row.get("OBLIGATION_EXPIRATION_DATE");
                    Object str5  = row.get("ANTICIPATED_TOTAL_AMOUNT");
                    Object str6  = row.get("PARENT_UNIT_NUMBER");
                    Object str7  = row.get("TITLE");
                    Object str8  = row.get("SPONSOR_NAME");
                    Object str9  = row.get("PERSON_NAME"); 
                    Object str10 = row.get("START_DATE");
                    Object str11 = row.get("END_DATE");
                    Object str12 = row.get("DIRECT_COST");
                    Object str13 = row.get("INDIRECT_COST");
                    buf.append(printObj(str1));
                    buf.append(printObj(str2));
                    buf.append(printObj(str3));
                    buf.append(printObj(str4));
                    buf.append(printObj(str5));
                    buf.append(printObj(str6));
                    buf.append(printObj(str7));
                    buf.append(printObj(str8));
                    buf.append(printObj(str9));
                    buf.append(printObj(str10));
                    buf.append(printObj(str11));
                    buf.append(printObj(str12));
                    buf.append(printObj(str13));
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
        Vector result = pobj.getCurrentActiveGrants(UnitName);
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
                    PdfWriter writer = PdfWriter.getInstance(document, res.getOutputStream());
                    writer.setPageEvent(this);
                    
                    document.open();
 
                    PdfPTable table  = new PdfPTable(9);

                    float[] widths = {0.15f, 0.15f, 0.1f, 0.1f, 0.1f, 0.1f, 0.1f, 0.1f, 0.1f};
                    table.setWidths(widths);
                    table.getDefaultCell().setBorder(0);
                    table.setHorizontalAlignment(0);
                    table.setTotalWidth(width - 72);
                    table.setLockedWidth(true);

                    Vector avector = new Vector();
                    for (int inum = 0; inum < listSize; inum++)
                    {
                        row = (HashMap)result.elementAt(inum);
                        if (handleMITAwardForDate(row)==false)
                            continue;
                        avector.addElement(row);
                    }

                    if (avector.size() < 0)
                        avector = result;
                    listSize = avector.size();
                    
		    if (bRepeatHeader == true)
                       table.setHeaderRows(4);
                    FormatHeader(table);
                    for (int inum = 0; inum < listSize; inum++)
                    {
                            row = (HashMap)avector.elementAt(inum);                            
                            FormatRecord(row, table);
                    }

                    Vector candVector = grantsvector;
                    if (Sort != null && Sort.compareTo("pi")==0)
                       candVector = sortIncomingVectorByPI(grantsvector);
                    if (Sort != null && Sort.compareTo("department")==0)
                       candVector = sortIncomingVectorByDepartment(grantsvector);
                    for (int inum = 0; inum < candVector.size(); inum++)
                    {
				GrantsObject curobj = (GrantsObject)candVector.elementAt(inum);
        			displayGrantsObjContents(curobj, table);
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
        String strhead = DeptName+"\nCurrent Active Research, Service, and Training Grants\nActive Extramural Grants and Contracts";
        if (bNIH == true)
            strhead += "\nfrom National Institutes of Health";
        strhead += "\nDate: "+DateUtils.formatDate(CurrentDate,"MM/dd/yyyy");
        FormatHeaderCell(table,strhead,Color.white,Color.white,Element.ALIGN_CENTER,12,9);
    
        FormatHeaderCell(table,"",Color.white,Color.black,Element.ALIGN_LEFT,12,9);
        FormatHeaderCell(table,"ORSP Number \nProject Period\nProject Total Cost",Color.white,Color.white,Element.ALIGN_LEFT,8,1);
        FormatHeaderCell(table,"PI Name\nProject Title\nDepartment",Color.white,Color.white,Element.ALIGN_LEFT,8,1);
        FormatHeaderCell(table,"Sponsor Name",Color.white,Color.white,Element.ALIGN_LEFT,8,2);
        FormatHeaderCell(table,"Award Period\nStart Date",Color.white,Color.white,Element.ALIGN_CENTER,8,1);
        FormatHeaderCell(table,"Award Period\nEnd Date",Color.white,Color.white,Element.ALIGN_CENTER,8,1);
        FormatHeaderCell(table,"Award Period\nDirect Cost",Color.white,Color.white,Element.ALIGN_CENTER,8,1);
        FormatHeaderCell(table,"Award Period\nIndirect Cost",Color.white,Color.white,Element.ALIGN_CENTER,8,1);
        FormatHeaderCell(table,"Award Period\nTotal Cost",Color.white,Color.white,Element.ALIGN_CENTER,8,1);

        FormatHeaderCell(table,"",Color.white,Color.black,Element.ALIGN_LEFT,8,9);
    }
    
    private void FormatRecord(HashMap row, PdfPTable table)
    	throws DocumentException
    {
        	Object cell = row.get("MIT_AWARD_NUMBER");
        	String straccount = ((String)cell).trim();
        	int index1 = straccount.indexOf("-001");
        	if (index1 > 0)
        	{
        		if (grantsobj != null)
        		{
                                grantsobj.setTotalCost(grantsobj.getDirectCost()+grantsobj.getIndirectCost());
				grantsvector.addElement(grantsobj);
        			runningTotalDirectCost += grantsobj.getDirectCost();
        			runningTotalIndirectCost += grantsobj.getIndirectCost();
        			runningTotalCost += grantsobj.getTotalCost();
        		}
       		
        		straccount = straccount.substring(0,index1);
        		grantsobj = new GrantsObject(straccount);    
        		populateCurrentGrantsObject(row);
        	}
        	else
        	{
        		handleCostValues(row);
        	}
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
    	Object unitnamecell = row.get("UNIT_NAME");

        String strtitle = getCellValue(unitnamecell).trim();
    	int index = strtitle.indexOf("-"+UnitName);
    	if (index > 0)
    		strtitle = strtitle.substring(0,index);
        
	grantsobj.setBeginProjectDate(getCellValue(begindatecell).trim());
	grantsobj.setEndProjectDate(getCellValue(enddatecell).trim());
	grantsobj.setProjectTotalCost(getNumericCellValue(totalamountcell));
	grantsobj.setProjectTitle(getCellValue(titlecell).trim());
	grantsobj.setSponsorName(getCellValue(sponsorcell).trim());
	grantsobj.setInvestigatorName(getCellValue(investigatorcell).trim());
        grantsobj.setUnitName(strtitle);
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

	private void displayGrantsObjContents(GrantsObject curgrantsobj, PdfPTable table)
    		throws DocumentException
	{
	
            if (curgrantsobj.getAwardBeginDate().length()==0 && 
                    curgrantsobj.getAwardEndDate().length()==0)
                return;

        PdfPTable subtable = new PdfPTable(9);		
        float[] widths = {0.15f, 0.15f, 0.1f, 0.1f, 0.1f, 0.1f, 0.1f, 0.1f, 0.1f};
        subtable.setWidths(widths);
        subtable.getDefaultCell().setBorder(0);
        subtable.setHorizontalAlignment(0);
        subtable.setTotalWidth(width - 72);
        subtable.setLockedWidth(true);

        String formatAccountNumber = curgrantsobj.getAccountNumber();
        FormatDataCell(subtable,formatAccountNumber,Color.white,Color.white,Element.ALIGN_LEFT,Font.PLAIN,8,1);
        FormatDataCell(subtable,curgrantsobj.getInvestigatorName(),Color.white,Color.white,Element.ALIGN_LEFT,Font.PLAIN,7,1);
        FormatDataCell(subtable,curgrantsobj.getSponsorName(),Color.white,Color.white,Element.ALIGN_LEFT,Font.PLAIN,7,2);
        FormatDataCell(subtable,curgrantsobj.getAwardBeginDate(),Color.white,Color.white,Element.ALIGN_CENTER,Font.PLAIN,8,1);
        FormatDataCell(subtable,curgrantsobj.getAwardEndDate(),Color.white,Color.white,Element.ALIGN_CENTER,Font.PLAIN,8,1);
        FormatDataCell(subtable,getCurrencyFormattedNumber(curgrantsobj.getDirectCost()),Color.white,Color.white,Element.ALIGN_CENTER,Font.PLAIN,8,1);
        FormatDataCell(subtable,getCurrencyFormattedNumber(curgrantsobj.getIndirectCost()),Color.white,Color.white,Element.ALIGN_CENTER,Font.PLAIN,8,1);
        FormatDataCell(subtable,getCurrencyFormattedNumber(curgrantsobj.getTotalCost()),Color.white,Color.white,Element.ALIGN_CENTER,Font.PLAIN,8,1);

        StringBuffer strbufd = new StringBuffer("");
        strbufd.append(getFormattedDate(curgrantsobj.getBeginProjectDate()));
        strbufd.append(" - ");
        strbufd.append(getFormattedDate(curgrantsobj.getEndProjectDate()));

        FormatDataCell(subtable,strbufd.toString(),Color.white,Color.white,Element.ALIGN_LEFT,Font.PLAIN,8,1);
        FormatDataCell(subtable,curgrantsobj.getProjectTitle(),Color.white,Color.white,Element.ALIGN_LEFT,Font.ITALIC,8,8);
        FormatDataCell(subtable,getCurrencyFormattedNumber(curgrantsobj.getProjectTotalCost()),Color.white,Color.white,Element.ALIGN_LEFT,Font.PLAIN,8,1);
        FormatDataCell(subtable,curgrantsobj.getUnitName(),Color.white,Color.white,Element.ALIGN_LEFT,Font.PLAIN,8,8);
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
        Vector result = pobj.getCurrentActiveGrants(UnitName);
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
                cell = row.createCell((short)1);
                    cell.setCellValue("Unit Name");
                cell = row.createCell((short)2);
                    cell.setCellValue("Current Effective Date");
                cell = row.createCell((short)3);
                    cell.setCellValue("Obligation Expiration Date");
                cell = row.createCell((short)4);
                    cell.setCellValue("Anticipated Total Amount");
                cell = row.createCell((short)5);
                    cell.setCellValue("Parent Unit");
                cell = row.createCell((short)6);
                    cell.setCellValue("Title");
                cell = row.createCell((short)7);
                    cell.setCellValue("Sponsor Name");
                cell = row.createCell((short)8);
                    cell.setCellValue("Investigator"); 
                cell = row.createCell((short)9);
                    cell.setCellValue("Start Date");
                cell = row.createCell((short)10);
                    cell.setCellValue("End Date");
                cell = row.createCell((short)11);
                    cell.setCellValue("Direct Cost");
                cell = row.createCell((short)12);
                    cell.setCellValue("Indirect Cost");

                     //Make Headers BOLD

           	cell.setCellStyle(headerStyle);
            int listSize = result.size();
            int ilist = 0;
            for (int i=0; i < listSize; i++) 
            {
                dbrow=(HashMap)result.elementAt(i); 
                if (handleMITAwardForDate(dbrow)==false)
                        continue;
                row = sheet.createRow((short)ilist + 1); //+1 since First Row is Header Row
                ilist++;

                    Object str1  = dbrow.get("MIT_AWARD_NUMBER");
                    Object str2  = dbrow.get("UNIT_NAME");
                    Object str3  = dbrow.get("CURRENT_FUND_EFFECTIVE_DATE");
                    Object str4  = dbrow.get("OBLIGATION_EXPIRATION_DATE");
                    Object str5  = dbrow.get("ANTICIPATED_TOTAL_AMOUNT");
                    Object str6  = dbrow.get("PARENT_UNIT_NUMBER");
                    Object str7  = dbrow.get("TITLE");
                    Object str8  = dbrow.get("SPONSOR_NAME");
                    Object str9  = dbrow.get("PERSON_NAME"); 
                    Object str10 = dbrow.get("START_DATE");
                    Object str11 = dbrow.get("END_DATE");
                    Object str12 = dbrow.get("DIRECT_COST");
                    Object str13 = dbrow.get("INDIRECT_COST");
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
    
    private void InitializeCurrentDate()
    {
        try {
            
            java.util.Date currentDate = new java.util.Date();
            CurrentDate = new Timestamp(currentDate.getTime());
         
            fiscalYear = CurrentDate.getYear();
            fiscalYear += 1900;
            
            int fiscalYearBegin = fiscalYear - 1;
            String strfiscalYearBegin = fiscalYearBegin + "-07-01 00:00:00.000000000";
            String strfiscalYearEnd = fiscalYear + "-06-30 11:59:59.999999999";
            begindate = Timestamp.valueOf(strfiscalYearBegin);
            enddate = Timestamp.valueOf(strfiscalYearEnd);
        }
        catch (NumberFormatException ex) {
            System.out.println("NumberFormatException : Fiscal Year "+CurrentDate+" is not recognized.");
            ex.printStackTrace(System.out);
        }
        catch (IllegalArgumentException ex) {
            System.out.println("IllegalArgumentException: Fiscal Year "+CurrentDate+" is not recognized.");
            ex.printStackTrace(System.out);
        }
    }
    
    public void setNIH(String NIH)
    {
        if (NIH != null && NIH.compareTo("true")==0)
            bNIH = true;
        else
            bNIH = false;
        if (pobj != null)
            pobj.setNIH(bNIH);
    }
    
    public void setEnforceEndDate(String EnforceEndDate)
    {
        if (EnforceEndDate != null && EnforceEndDate.compareTo("true")==0)
            bEnforceEndDate = true;
        else
            bEnforceEndDate = false;
    }
    
    public void setRepeatHeader(String RepeatHeader)
    {
        if (RepeatHeader != null && RepeatHeader.compareTo("true")==0)
            bRepeatHeader = true;
        else
            bRepeatHeader = false;
    }
    
    public String getSpecificCSVOutput()
    {
        Vector result = pobj.getCurrentActiveGrants(UnitName);
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
                    buf.append(printPlainObj("Title"));
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
                buf.append("\n");
            	for(int rowNum = 0; rowNum < listSize; rowNum++)
		{
                    row=(HashMap)result.elementAt(rowNum); 
                    if (handleMITAwardForDate(row)==false)
                        continue;
                    Object str1  = row.get("MIT_AWARD_NUMBER");
                    Object str2  = row.get("UNIT_NAME");
                    Object str3  = row.get("CURRENT_FUND_EFFECTIVE_DATE");
                    Object str4  = row.get("OBLIGATION_EXPIRATION_DATE");
                    Object str5  = row.get("ANTICIPATED_TOTAL_AMOUNT");
                    Object str6  = row.get("PARENT_UNIT_NUMBER");
                    Object str7  = row.get("TITLE");
                    Object str8  = row.get("SPONSOR_NAME");
                    Object str9  = row.get("PERSON_NAME"); 
                    Object str10 = row.get("START_DATE");
                    Object str11 = row.get("END_DATE");
                    Object str12 = row.get("DIRECT_COST");
                    Object str13 = row.get("INDIRECT_COST");
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
    
    private boolean handleMITAwardForDate(HashMap row)
    {
        	Object cell = row.get("MIT_AWARD_NUMBER");
        	String straccount = ((String)cell).trim();
        	int index1 = straccount.indexOf("-001");
                boolean bfound = false;
                if (index1 > 0)
                {
                    Object obligationexpirationdatecell = row.get("OBLIGATION_EXPIRATION_DATE");
                    Timestamp projectenddate = getTimeStampCellValue(obligationexpirationdatecell);
                    if (projectenddate != null && (projectenddate.getTime() >= CurrentDate.getTime()))
                       bfound = true;
                    
                    // if we have to enforce end date, any project end date less than current date should be ignored.
                    if (bfound == true && bEnforceEndDate == true && projectenddate != null && projectenddate.getTime() <= CurrentDate.getTime())
                        bfound = false;
                }
                else
        	{
                    Object begindatecell = row.get("START_DATE");
                    Object enddatecell = row.get("END_DATE");
                    Timestamp periodstartdate = getTimeStampCellValue(begindatecell);
                    Timestamp periodenddate = getTimeStampCellValue(enddatecell);
                    if (periodstartdate !=null && periodenddate != null && (periodstartdate.getTime() <= CurrentDate.getTime()) &&
                            (periodenddate.getTime() >= CurrentDate.getTime()))
                        bfound = true;
        	}
                return bfound;
    }

    ///////////////////////////////////////////////////////////////////
    // OVERRIDDEN METHODS ASSOCIATED WITH PdfPageEventHelper

    public void onStartPage(PdfWriter writer, Document document) 
    {
    }

    public void onEndPage(PdfWriter writer, Document document) 
    {
   	   width = document.getPageSize().width();
           float[] widths = {0.5f, 0.5f};

           try {
               Rectangle page = document.getPageSize();
               PdfPTable foot = new PdfPTable(2);
               foot.setWidths(widths);
               foot.getDefaultCell().setBorder(0);
               foot.setHorizontalAlignment(0);
               foot.setTotalWidth(width - 72);
               foot.setLockedWidth(true);
	   
               Color backgroundcolor = Color.black;
               FormatSubfooterCell(foot,"",backgroundcolor,1,Element.ALIGN_LEFT);
               FormatSubfooterCell(foot,"",backgroundcolor,1,Element.ALIGN_LEFT);

               java.util.Date currdate = new java.util.Date();
               SimpleDateFormat formatter = new SimpleDateFormat("EEEEEEEEE,MMMMMMMMMMMMM dd, yyyy");
               String strcurdate = formatter.format(currdate);

               int iIndex = writer.getPageNumber();
               backgroundcolor = Color.white;
               FormatSubfooterCell(foot,strcurdate,backgroundcolor,1,Element.ALIGN_LEFT);
               FormatSubfooterCell(foot,iIndex+" ",backgroundcolor,1,Element.ALIGN_RIGHT);

               foot.writeSelectedRows(0, -1, document.leftMargin(), document.bottomMargin(),
                  writer.getDirectContent());
           }
           catch (Exception e) {
               throw new ExceptionConverter(e);
           }
    }

    public void onCloseDocument(PdfWriter writer, Document document) 
    {
    }

}


