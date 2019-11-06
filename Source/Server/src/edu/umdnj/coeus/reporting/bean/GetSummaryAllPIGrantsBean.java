/**
 * $Header: /home/cvs/cvsroot/coeus/Source/WEB-INF/classes/edu/umdnj/coeus/reporting/bean/Attic/GetSummaryAllPIGrantsBean.java,v 1.1.2.6 2007/09/18 18:23:05 cvs Exp $
 * $Log: GetSummaryAllPIGrantsBean.java,v $
 * Revision 1.1.2.6  2007/09/18 18:23:05  cvs
 * Modify code for GAFA and START_DATE handling
 *
 * Revision 1.1.2.5  2007/08/16 18:11:45  cvs
 * 1. Add support for sorting by PI and Department
 * 2. Move sorting methods to parent class
 *
 * Revision 1.1.2.4  2007/07/31 13:36:19  cvs
 * Add Annual Reports server-side code
 * Move many methods to base class Reporting Base Bean
 *
 * Revision 1.1.2.3  2007/03/27 15:19:20  cvs
 * Modify code to work with Award FnA Distributions
 *
 * Revision 1.1.2.2  2007/02/16 19:31:38  cvs
 * Per recommendations from Therese, rearrange layout
 *
 * Revision 1.1.2.1  2007/02/16 18:32:16  cvs
 * Add support for Summary PI Grants for all schools
 *
 *
 */
/*
 * @(#)GetSummaryAllPIGrantsBean.java 
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

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import edu.mit.coeus.utils.dbengine.DBEngineImpl;

import edu.mit.coeus.utils.dbengine.DBException;

import edu.mit.coeus.utils.dbengine.Parameter;

import edu.mit.coeus.utils.dbengine.DBEngineConstants;

import edu.mit.coeus.utils.CoeusVector;

import edu.mit.coeus.utils.UtilFactory;

import edu.mit.coeus.exception.CoeusException;

import edu.mit.coeus.budget.report.ReportGenerator;

// Coeus MIT imports
import edu.mit.coeus.utils.CoeusProperties;
import edu.mit.coeus.utils.CoeusPropertyKeys;
import org.apache.axis.wsdl.symbolTable.Type;

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

import edu.umdnj.coeus.reporting.bean.UMDNJSQLUtilities;
import edu.umdnj.coeus.utilities.UMDNJStrings;


public class GetSummaryAllPIGrantsBean extends ReportingBaseBean
{
    private int runningTotalDirectCost;
    private int runningTotalIndirectCost;
    private int runningTotalCost;
    private int numawards;

    private boolean bNIH = false;
    private String Format = "PDF";
    
    private UMDNJSQLUtilities pobj = null;

    private GrantsObject grantsobj = null;
    private Vector vectorRWJ = null;
    private Vector vectorSPH = null;
    private Vector vectorSOM = null;
    private Vector vectorNJMS = null;
    private Vector vectorSHRP = null;
    private Vector vectorSN = null;
    private Vector vectorDS = null;

    private Vector subvectorRWJ = null;
    private Vector subvectorSPH = null;
    private Vector subvectorSOM = null;
    private Vector subvectorNJMS = null;
    private Vector subvectorSHRP = null;
    private Vector subvectorSN = null;
    private Vector subvectorDS = null;


    /** Creates a new instance of GetSummaryAllPIGrantsBean */
    public GetSummaryAllPIGrantsBean() 
    {
	super();
        Initialization();
    }
    
    public GetSummaryAllPIGrantsBean(String FiscalYear)
    {
        super();
        if (FiscalYear != null && FiscalYear.length() > 0)
            InitializeFiscalYear(FiscalYear);
        Initialization();
    }
    
    private void Initialization()
    {
        pobj = new UMDNJSQLUtilities();
    }

    public void getOutput(HttpServletResponse res)
         throws ServletException, IOException
    {
        vectorRWJ = new Vector();
        vectorSPH = new Vector();
        vectorSOM = new Vector();
        vectorNJMS = new Vector();
        vectorSHRP = new Vector();
        vectorSN = new Vector();
        vectorDS = new Vector();

        subvectorRWJ = new Vector();
        subvectorSPH = new Vector();
        subvectorSOM = new Vector();
        subvectorNJMS = new Vector();
        subvectorSHRP = new Vector();
        subvectorSN = new Vector();
        subvectorDS = new Vector();

        Vector result = pobj.getGrantsByAllPI();
        if (result != null && result.size() > 0)
        {
            HashMap row = null;
            int listSize = result.size();
            grantsobj = null;
            String strtitle = "";

            // subdivide incoming output into relevant school divisions
            for (int inum = 0; inum < listSize; inum++)
            {
                row = (HashMap)result.elementAt(inum);
                Object cell = row.get("PARENT_UNIT_NUMBER");                
                String strunit = ((String)cell).trim();
                if (strunit.compareTo("RWJ")==0)
                   vectorRWJ.addElement(row);
                else
                if (strunit.compareTo("SPH")==0)
                   vectorSPH.addElement(row);
                else
                if (strunit.compareTo("SOM")==0)
                   vectorSOM.addElement(row);
                else
                if (strunit.compareTo("NJMS")==0)
                   vectorNJMS.addElement(row);
                else
                if (strunit.compareTo("SHRP")==0)
                   vectorSHRP.addElement(row);
                else
                if (strunit.compareTo("SN")==0)
                   vectorSN.addElement(row);
                else
                if (strunit.compareTo("DS")==0)
                   vectorDS.addElement(row);
            }

            handleRawVectorSchool(vectorRWJ,"RWJ");
            handleRawVectorSchool(vectorSPH,"SPH");
            handleRawVectorSchool(vectorSOM,"SOM");
            handleRawVectorSchool(vectorNJMS,"NJMS");
            handleRawVectorSchool(vectorSHRP,"SHRP");
            handleRawVectorSchool(vectorSN,"SN");
            handleRawVectorSchool(vectorDS,"DS");


            getSpecificPDFOutput(res);
       }
       else
       {
           res.setContentType("text/html");
           PrintWriter writer = res.getWriter();
           writer.println("<html><body>");
           writer.print(UMDNJStrings.NoRecordsComingBack);
           writer.println("</body></html>"); 
       }
    }


    private void FormatRecord(HashMap row)
    {
        	Object cell = row.get("MIT_AWARD_NUMBER");
        	String straccount = ((String)cell).trim();
        	int index1 = straccount.indexOf("-001");
        	if (index1 > 0)
        	{
                        numawards++;
                        displayAwardContents();
        		straccount = straccount.substring(0,index1);
        		grantsobj = new GrantsObject(straccount);    
        		populateCurrentGrantsObject(row);
        	}
        	else
        	{
        		handleCostValues(row);
        	}
    }
    
    private void handleRawVectorSchool(Vector result,String UnitName)
    {
       int listSize = result.size();
       grantsobj = null;
       String strtitle = "";
       HashMap row = null;
       int subtitlechanged = 0;
       
       for (int inum = 0; inum < listSize; inum++)
       {           
           row = (HashMap)result.elementAt(inum);
           HashMap prevrow = null;
           if (inum > 0)
              prevrow = (HashMap)result.elementAt(inum-1);
           String prevstrtitle = "";
           Object cell = row.get("FULL_NAME");
           Object prevcell = null;
          
           strtitle = ((String)cell).trim();
           if (prevrow != null)
           {
              prevcell = prevrow.get("FULL_NAME");
              prevstrtitle = ((String)prevcell).trim();
           }
           else
              prevstrtitle = strtitle;
                       
           FormatRecord(row);
       	
           if (inum == 0 || strtitle.compareTo(prevstrtitle)!=0)
           {
              if (strtitle.compareTo(prevstrtitle)!=0)
                 subtitlechanged++;
              if (inum != 0)
              { 
                 displayTotals(prevstrtitle,UnitName);
                 runningTotalDirectCost = 0;
                 runningTotalIndirectCost = 0;
                 runningTotalCost = 0;
                 numawards = 0;
              }
           }
       }
       if (subtitlechanged==0)
         displayAwardContents();
       displayTotals(strtitle,UnitName);
    }
    
    private void populateCurrentGrantsObject(HashMap row)
    {
        Object begindatecell  = row.get("CURRENT_FUND_EFFECTIVE_DATE");
        Object enddatecell  = row.get("OBLIGATION_EXPIRATION_DATE");
        Object totalamountcell  = row.get("ANTICIPATED_TOTAL_AMOUNT");
        Object titlecell  = row.get("TITLE");
        Object sponsorcell  = row.get("SPONSOR_NAME");
        Object investigatorcell  = row.get("FULL_NAME"); 
        Object totalbegindatecell = row.get("START_DATE");
        Object unitnamecell = row.get("UNIT_NAME");
    	String strtitle = getCellValue(unitnamecell).trim();

	grantsobj.setBeginProjectDate(getCellValue(begindatecell).trim());
	grantsobj.setEndProjectDate(getCellValue(enddatecell).trim());
	grantsobj.setProjectTotalCost(getNumericCellValue(totalamountcell));
	grantsobj.setProjectTitle(getCellValue(titlecell).trim());
	grantsobj.setSponsorName(getCellValue(sponsorcell).trim());
	grantsobj.setInvestigatorName(getCellValue(investigatorcell).trim());
 	grantsobj.setUnitName(strtitle);
        if (fiscalYear == 0)
        {
            grantsobj.setAwardBeginDate(getCellValue(totalbegindatecell).trim());    	
            grantsobj.setTotalCost(getNumericCellValue(totalamountcell));
        }
    }

	private void handleCostValues(HashMap row)
	{
		if (grantsobj == null)
			return;
                
                if (fiscalYear > 0)
                {
                    Object totalbegindatecell = row.get("START_DATE");
                    Timestamp canddate = getTimeStampCellValue(totalbegindatecell);                   
                    if (canddate != null)
                    {
                        if (canddate.getTime() < begindate.getTime() || 
                                canddate.getTime() > enddate.getTime())
                                return;
                        grantsobj.setAwardBeginDate(getCellValue(totalbegindatecell).trim());    	
                    }
                }
      		else
		{
                    Object totalbegindatecell = row.get("START_DATE");
		    if (grantsobj.getAwardBeginDate() == null || grantsobj.getAwardBeginDate().length()==0)
                       grantsobj.setAwardBeginDate(getCellValue(totalbegindatecell).trim());    	
		}
                
		Object directcostcell = row.get("DIRECT_COST");
		double val1 = getNumericCellValue(directcostcell);
		grantsobj.addDirectCost(val1);
		
		Object indirectcostcell = row.get("INDIRECT_COST");
		double val2 = getNumericCellValue(indirectcostcell);
		grantsobj.addIndirectCost(val2);
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
         
    
    
    public void setNIH(String NIH)
    {
        if (NIH != null && NIH.compareTo("true")==0)
            bNIH = true;
        else
            bNIH = false;
        if (pobj != null)
            pobj.setNIH(bNIH);
    }
    
    public void setFormat(String val)
    {
        Format = val;
    }

    public void setInvestigator(String str)
    {
        if (str != null && str.length() > 0)
            pobj.setInvestigator(str);
    }
    
    private void displayAwardContents()
    {
       if (grantsobj != null)
       {
          grantsobj.setTotalCost(grantsobj.getDirectCost()+grantsobj.getIndirectCost());
          runningTotalDirectCost += grantsobj.getDirectCost();
          runningTotalIndirectCost += grantsobj.getIndirectCost();
          runningTotalCost += grantsobj.getTotalCost();
       }
    }

    private void displayTotals(String strtitle, String UnitName)
    {
        PIGraphObject obj = new PIGraphObject();
        obj.setPiName(strtitle);
        obj.setNumGrants(numawards);
        obj.setTotalCost(runningTotalCost);
        if (UnitName.compareTo("RWJ")==0)
           subvectorRWJ.addElement(obj);
        else
        if (UnitName.compareTo("SPH")==0)
           subvectorSPH.addElement(obj);
        else
        if (UnitName.compareTo("SOM")==0)
           subvectorSOM.addElement(obj);
        else
        if (UnitName.compareTo("NJMS")==0)
           subvectorNJMS.addElement(obj);
        else
        if (UnitName.compareTo("SHRP")==0)
           subvectorSHRP.addElement(obj);
        else
        if (UnitName.compareTo("SN")==0)
           subvectorSN.addElement(obj);
        else
        if (UnitName.compareTo("DS")==0)
           subvectorDS.addElement(obj);
    }

    
    public void getSpecificPDFOutput(HttpServletResponse res)
         throws ServletException, IOException
    {
    	Document document = null;
        try 
        {
            document = new Document(PageSize.LETTER.rotate());
            res.setContentType("application/pdf");
            PdfWriter.getInstance(document, res.getOutputStream());

	    document.open();
            PdfPTable table1 = displaySchoolTable(subvectorRWJ,"RWJ");
            document.add(table1);
            document.newPage();
            PdfPTable table2 = displaySchoolTable(subvectorSPH,"SPH");
            document.add(table2);
            document.newPage();
            PdfPTable table3 = displaySchoolTable(subvectorSOM,"SOM");
            document.add(table3);
            document.newPage();
            PdfPTable table4 = displaySchoolTable(subvectorNJMS,"NJMS");
            document.add(table4);
            document.newPage();
            PdfPTable table5 = displaySchoolTable(subvectorSHRP,"SHRP");
            document.add(table5);
            document.newPage();
            PdfPTable table6 = displaySchoolTable(subvectorSN,"SN");
            document.add(table6);
            document.newPage();
            PdfPTable table7 = displaySchoolTable(subvectorDS,"DS");
            document.add(table7);

            document.close();
        }
        catch(DocumentException de) 
        {
            de.printStackTrace();
            System.err.println("document: " + de.getMessage());
        }
    	catch ( IOException ex ) 
    	{
            ex.printStackTrace();
        }

    }

    private PdfPTable displaySchoolTable(Vector someVector,String UnitName)
    	throws DocumentException
    {
         PdfPTable maintable  = new PdfPTable(3);
         FormatHeader(maintable,UnitName);
         Vector testVector = someVector;
         if (Sort.compareTo("amount")==0 || Sort.compareTo("top10")==0)
            testVector = sortIncomingVectorByTotalCost(someVector);

         for (int inum = 0; inum < testVector.size(); inum++)
         {
            PIGraphObject obj = (PIGraphObject)testVector.elementAt(inum);
            PrintRecord(obj,maintable);
         }
         return maintable;
    }

    private void FormatHeader(PdfPTable table,String UnitName)
    	throws DocumentException
    {
        String strschool = getUnitSchool(UnitName);
         String strtitle = "\n\nUMDNJ - "+strschool+"\nActive Extramural Grants and Contracts\nby Primary Investigator\nSummary Listing";
         if (fiscalYear > 0)
            strtitle += " for Fiscal Year " + fiscalYear;
         if (Sort.compareTo("top10")==0)
            strtitle += "\nTop 10 Performers";
         strtitle += "\n\n";

        Paragraph a = new Paragraph();
        a.add(new Chunk(strtitle,
        		FontFactory.getFont(FontFactory.TIMES, 14, Font.BOLD, Color.BLACK)));                    
        PdfPCell cell1 = new PdfPCell(a);
        cell1.setColspan(3);
        cell1.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell1.setNoWrap(true);
        cell1.setBorderColor(Color.white);
        table.addCell(cell1);
        
        Paragraph b = new Paragraph();
        b.add(new Chunk("",
        		FontFactory.getFont(FontFactory.TIMES, 14, Font.BOLD, Color.WHITE)));
        PdfPCell cell2 = new PdfPCell(b);
        cell2.setColspan(3);
        cell2.setNoWrap(true);
        cell2.setBorderColor(Color.WHITE);
        cell2.setBackgroundColor(Color.WHITE);
        table.addCell(cell2);

        Paragraph c1 = new Paragraph();
        c1.add(new Chunk("Primary Investigator",
        		FontFactory.getFont(FontFactory.TIMES, 12, Font.BOLD, Color.BLACK)));                    
        PdfPCell cellc1 = new PdfPCell(c1);
        cellc1.setHorizontalAlignment(Element.ALIGN_LEFT);
        cellc1.setNoWrap(true);
        cellc1.setBorderColor(Color.white);
        table.addCell(cellc1);

        Paragraph c2 = new Paragraph();
        c2.add(new Chunk("Number of Awards",
        		FontFactory.getFont(FontFactory.TIMES, 12, Font.BOLD, Color.BLACK)));                    
        PdfPCell cellc2 = new PdfPCell(c2);
        cellc2.setHorizontalAlignment(Element.ALIGN_RIGHT);
        cellc2.setNoWrap(true);
        cellc2.setBorderColor(Color.white);
        table.addCell(cellc2);

        Paragraph c3 = new Paragraph();
        c3.add(new Chunk("Award Amount",
        		FontFactory.getFont(FontFactory.TIMES, 12, Font.BOLD, Color.BLACK)));                    
        PdfPCell cellc3 = new PdfPCell(c3);
        cellc3.setHorizontalAlignment(Element.ALIGN_RIGHT);
        cellc3.setNoWrap(true);
        cellc3.setBorderColor(Color.white);
        table.addCell(cellc3);
        
        Paragraph d = new Paragraph();
        d.add(new Chunk("",
        		FontFactory.getFont(FontFactory.TIMES, 10, Font.BOLD, Color.WHITE)));
        PdfPCell celld = new PdfPCell(d);
        celld.setColspan(3);
        celld.setNoWrap(true);
        celld.setBorderColor(Color.WHITE);
        celld.setBackgroundColor(Color.BLACK);
        table.addCell(celld);

        Paragraph e = new Paragraph();
        e.add(new Chunk("",
        		FontFactory.getFont(FontFactory.TIMES, 14, Font.BOLD, Color.WHITE)));
        PdfPCell celle = new PdfPCell(e);
        celle.setColspan(3);
        celle.setNoWrap(true);
        celle.setBorderColor(Color.WHITE);
        celle.setBackgroundColor(Color.WHITE);
        table.addCell(celle);
    }

    private void PrintRecord(PIGraphObject obj, PdfPTable table)
    	throws DocumentException
    {
        Paragraph c1 = new Paragraph();
        c1.add(new Chunk(obj.getPiName(),
        		FontFactory.getFont(FontFactory.TIMES, 12, Font.PLAIN, Color.BLACK)));                    
        PdfPCell cellc1 = new PdfPCell(c1);
        cellc1.setHorizontalAlignment(Element.ALIGN_LEFT);
        cellc1.setNoWrap(true);
        cellc1.setBorderColor(Color.white);
        table.addCell(cellc1);

        Paragraph c2 = new Paragraph();
        StringBuffer buf1 = new StringBuffer("");
        buf1.append(obj.getNumGrants());
        c2.add(new Chunk(buf1.toString(),
        		FontFactory.getFont(FontFactory.TIMES, 12, Font.PLAIN, Color.BLACK)));                    
        PdfPCell cellc2 = new PdfPCell(c2);
        cellc2.setHorizontalAlignment(Element.ALIGN_RIGHT);
        cellc2.setNoWrap(true);
        cellc2.setBorderColor(Color.white);
        table.addCell(cellc2);

        Paragraph c3 = new Paragraph();
        c3.add(new Chunk(getCurrencyFormattedNumber(obj.getTotalCost()),
        		FontFactory.getFont(FontFactory.TIMES, 12, Font.PLAIN, Color.BLACK)));                    
        PdfPCell cellc3 = new PdfPCell(c3);
        cellc3.setHorizontalAlignment(Element.ALIGN_RIGHT);
        cellc3.setNoWrap(true);
        cellc3.setBorderColor(Color.white);
        table.addCell(cellc3);

    }
}


