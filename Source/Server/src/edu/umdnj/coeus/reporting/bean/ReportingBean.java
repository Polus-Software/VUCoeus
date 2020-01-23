/**
  $Header: /home/cvs/cvsroot/coeus/Source/WEB-INF/classes/edu/umdnj/coeus/reporting/bean/Attic/ReportingBean.java,v 1.1.2.6 2007/02/14 16:34:38 cvs Exp $
  $Log: ReportingBean.java,v $
  Revision 1.1.2.6  2007/02/14 16:34:38  cvs
  Check for empty reports

  Revision 1.1.2.5  2007/01/17 15:26:35  cvs
  Add CSV Support

  Revision 1.1.2.4  2006/12/08 15:28:22  cvs
  Add sample report for Number of Proposals by Department

  Revision 1.1.2.3  2006/12/07 15:16:02  cvs
  Add second sample report and clean up code

  Revision 1.1.2.2  2006/12/01 14:15:20  cvs
  Add XLS support for formatted output to Canned Reports dialog

  Revision 1.1.2.1  2006/11/20 14:17:02  cvs
  Subclass ReportingBaseBean to two child classes for easy inheritance of methods

*
*/
/*
 * @(#)ReportingBean.java 
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

import org.apache.poi.hssf.usermodel.*;
//import org.apache.poi.hssf.model.Workbook;

import org.apache.fop.apps.FOPException;

import edu.umdnj.coeus.reporting.bean.UMDNJSQLUtilities;
import edu.umdnj.coeus.utilities.UMDNJStrings;

public class ReportingBean extends ReportingBaseBean
{
    private static final String SQLRequest1 = "select PROPOSAL_NUMBER, TITLE FROM OSP$PROPOSAL";
    

    private String strReport = "";
    
    private UMDNJSQLUtilities pobj = null;
    
    /** Creates a new instance of ReportingBean */

    public ReportingBean() 
    {
	super();
        pobj = new UMDNJSQLUtilities();
    }        


    /**

     *  This method used to get information from sample report 1

     *  <li>To fetch the data, it gets all proposals

     *

     *  @return Vector of all ComboBox beans

     *  @exception DBException if any error during database transaction.

     *  @exception CoeusException if the instance of dbEngine is not available.

     */

    public String getSampleReport(String strReport, String strFormat) throws CoeusException, DBException
    {
        Vector result = null;

        Vector param=new Vector();
        String strout = "";
        String strSQL = "";
        this.strReport = strReport;
        
        if (strReport.compareTo("ReportOne")==0)
            strSQL = SQLRequest1;
        
        if(dbEngine !=null){

            if (strReport.compareTo("ReportOne")==0)
            {
                result = dbEngine.executeRequest("Coeus", strSQL,
                    "Coeus", param);
            }
            else
            if (strReport.compareTo("ReportTwo")==0)
            {
                result = pobj.getProposalCountByDepartment();
            }

        }else{

            throw new CoeusException("db_exceptionCode.1000");

        }
        if (strFormat.compareTo("HTML")==0)
            strout = returnHTMLContent(result);
        else
        if (strFormat.compareTo("XML")==0)
            strout = returnXMLContent(result);
	else
	if (strFormat.compareTo("PDF")==0)
	    strout = returnPDFContent(result);
	else
	if (strFormat.compareTo("CSV")==0)
	    strout = returnCSVContent(result);
        return strout;
    }
    
    private String returnHTMLContent(Vector result)
    {
        HashMap row = null;
        
        int listSize = 0;
        if (result != null)
           listSize = result.size();
	StringBuffer buf = new StringBuffer("");
        if (listSize > 0)
        {
		buf.append("<table border=1>");
		buf.append("<tr>");
                if (strReport.compareTo("ReportOne")==0)
                {
                    buf.append("<td><b><u>Proposal Number</u></b></td>");
                    buf.append("<td><b><u>Title</u></b></td>");
                }
                else
                if (strReport.compareTo("ReportTwo")==0)
                {
                    buf.append("<td><b><u>Unit Name</u></b></td>");
                    buf.append("<td><b><u>Number of Proposals</u></b></td>");                    
                }
		buf.append("</tr>");

            	for(int rowNum = 0; rowNum < listSize; rowNum++)
		{
			buf.append("<tr>");

                	row=(HashMap)result.elementAt(rowNum); 
                        if (strReport.compareTo("ReportOne")==0)
                        {
                            buf.append("<td>");
                            buf.append(row.get("PROPOSAL_NUMBER"));
                            buf.append("</td>");
                            buf.append("<td>");
                            buf.append(row.get("TITLE"));
                            buf.append("</td>");
                        }
                        else
                        if (strReport.compareTo("ReportTwo")==0)
                        {
                            buf.append("<td>");
                            buf.append(row.get("UNIT_NAME"));
                            buf.append("</td>");
                            buf.append("<td>");
                            buf.append(row.get("NUMBER_PROPOSALS"));
                            buf.append("</td>");                            
                        }
			buf.append("</tr>");
		}

        	buf.append("</table>");
        }
        else
		buf.append(UMDNJStrings.NoRecordsComingBack);
	return buf.toString();
    }

        private String returnXMLContent(Vector result)
    {
        
        int listSize = 0;
        if (result != null)
           listSize = result.size();
	StringBuffer buf = new StringBuffer("");
        if (listSize > 0)
        {
		buf.append("<?xml version=\"1.0\"?>");
                if (strReport.compareTo("ReportOne")==0)
                    getReportOneXML(buf,listSize,result);
                else
                if (strReport.compareTo("ReportTwo")==0)
                    getReportTwoXML(buf,listSize,result);
        }
        else
		buf.append(UMDNJStrings.NoRecordsComingBack);
	return buf.toString();
    }
        
    private void getReportOneXML(StringBuffer buf, int listSize, Vector result)
    {
        HashMap row = null;
                buf.append("<proposals>");

                buf.append("<proposal_head>");
                buf.append("<proposal_number>proposal_number</proposal_number><title>title</title>");
                buf.append("</proposal_head>");

            	for(int rowNum = 0; rowNum < listSize; rowNum++)
		{
                	row=(HashMap)result.elementAt(rowNum); 
			buf.append("<proposal>");
                        buf.append("<proposal_number>");
			buf.append(xmlTranslate(row.get("PROPOSAL_NUMBER").toString()));
                        buf.append("</proposal_number>");
			buf.append("<title>");
			buf.append(xmlTranslate(row.get("TITLE").toString()));
			buf.append("</title>");
			buf.append("</proposal>");
		}

        	buf.append("</proposals>");        
    }

        private void getReportTwoXML(StringBuffer buf, int listSize, Vector result)
    {
        HashMap row = null;
                buf.append("<proposal_count_by_department>");

                buf.append("<proposal_count_head>");
                buf.append("<unit_name>unit_name</unit_name>");
                buf.append("<proposal_count>proposal_count</proposal_count>");
                buf.append("</proposal_count_head>");

            	for(int rowNum = 0; rowNum < listSize; rowNum++)
		{
                	row=(HashMap)result.elementAt(rowNum); 
			buf.append("<proposal>");
                        buf.append("<unit_name>");
			buf.append(xmlTranslate(row.get("UNIT_NAME").toString()));
                        buf.append("</unit_name>");
			buf.append("<number_proposals>");
			buf.append(xmlTranslate(row.get("NUMBER_PROPOSALS").toString()));
			buf.append("</number_proposals>");
			buf.append("</proposal>");
		}

        	buf.append("</proposal_count_by_department>");        
    }

        private String returnPDFContent(Vector result)
    {
        int listSize = 0;
        if (result != null)
           listSize = result.size();
	StringBuffer buf = new StringBuffer("");
        if (listSize > 0)
        {
		if (listSize > 100)
		{
			System.out.print("PDF listSize = "+listSize);
			System.out.println(" -- Defaulting to 100 because of memory leak.");
			listSize = 100;
		}
		buf.append("<?xml version=\"1.0\"?>");
                
                if (strReport.compareTo("ReportOne")==0)
                    getReportOneXML(buf,listSize,result);
                else
                if (strReport.compareTo("ReportTwo")==0)
                    getReportTwoXML(buf,listSize,result);
        }
        else
		buf.append(UMDNJStrings.NoRecordsComingBack);
	return buf.toString();
    }

    public void SampleReportToPDF(String strReport, HttpServletResponse res)
         throws ServletException, IOException, CoeusException, DBException
    {
        this.strReport = strReport;
	String strXML = getSampleReport(strReport, "PDF");
        String strXSL = "";
        if (strReport.compareTo("ReportOne")==0)
            strXSL = getXSLStringforReportOne();
        else
        if (strReport.compareTo("ReportTwo")==0)
            strXSL = getXSLStringforReportTwo();
	byte [] bytesXML = strXML.getBytes();
        byte [] bytesXSL = strXSL.getBytes();

        ReportGenerator reportGenerator = new ReportGenerator();

        ByteArrayInputStream xmlStream = new ByteArrayInputStream(bytesXML);

        ByteArrayInputStream xslStream = new ByteArrayInputStream(bytesXSL);
        ByteArrayOutputStream byteArrayOutputStream;
	try {

	        byteArrayOutputStream = reportGenerator.convertXML2PDF(xmlStream, xslStream);

	        byteArrayOutputStream.close();

	}
	catch(FOPException ex) {
		System.out.println("ReportingBean: SampleReportOneToPDF exception");
		ex.printStackTrace(System.out);
		return;
	}
	catch(TransformerException ex) {
		System.out.println("ReportingBean: SampleReportOneToPDF exception");
		ex.printStackTrace(System.out);
		return;
	}
	res.setContentType("application/pdf");

	ServletOutputStream outStream = res.getOutputStream();

	outStream.write(byteArrayOutputStream.toByteArray());
    }

    private String getXSLStringforReportOne()
    {
       StringBuffer buf = new StringBuffer("");
       buf.append("<?xml version=\"1.0\"?>");
       buf.append("<xsl:stylesheet version=\"1.0\"");
       buf.append("  xmlns:xsl=\"http://www.w3.org/1999/XSL/Transform\"");
       buf.append("  xmlns:fo=\"http://www.w3.org/1999/XSL/Format\">");

       buf.append(" <xsl:template match=\"/\">");
       buf.append("   <fo:root xmlns:fo=\"http://www.w3.org/1999/XSL/Format\">");
 
        buf.append("    <fo:layout-master-set>");
 
       buf.append("       <fo:simple-page-master master-name=\"us-standard\"");
       buf.append("          page-width=\"11in\"  page-height=\"8.5in\"");
       buf.append("          margin-top=\"0.5in\"  margin-bottom=\"0.5in\"");
       buf.append("          margin-left=\"0.5in\" margin-right=\"0.5in\">");
       buf.append("          <fo:region-body margin-top=\"1.0in\"");
       buf.append("                          margin-bottom=\"1.0in\"/>");
       buf.append("          <fo:region-before extent=\"1.0in\"/>");
       buf.append("          <fo:region-after  extent=\"1.0in\"/>");
       buf.append("       </fo:simple-page-master>");
 
       buf.append("     </fo:layout-master-set>");
 
       buf.append("     <fo:page-sequence master-reference=\"us-standard\" initial-page-number=\"1\" >");

       buf.append("        <fo:static-content flow-name=\"xsl-region-before\">");
       buf.append("          <fo:block>A Sample Report</fo:block>");
       buf.append("        </fo:static-content>");

       buf.append("        <fo:static-content flow-name=\"xsl-region-after\">");
       buf.append("          <fo:block>p. <fo:page-number/></fo:block>");
       buf.append("        </fo:static-content>");

       buf.append("        <fo:flow flow-name=\"xsl-region-body\">");
       buf.append("          <fo:table font-size=\"12pt\" ");
       buf.append("                    space-before=\"24pt\" space-after=\"24pt\">");
       buf.append("            <fo:table-column column-width=\"2in\"/>");
       buf.append("            <fo:table-column column-width=\"5in\"/>");
       buf.append("            <fo:table-body>");
       buf.append("              <xsl:apply-templates select=\"//proposal_head\">");
       buf.append("              </xsl:apply-templates>");
       buf.append("            </fo:table-body>");
       buf.append("            <fo:table-body>");
       buf.append("              <xsl:apply-templates select=\"//proposal\">");
       buf.append("                <xsl:sort data-type=\"number\"");
       buf.append("                  select=\"proposal_number\"/>");
       buf.append("              </xsl:apply-templates>");
       buf.append("            </fo:table-body>");
       buf.append("           </fo:table>");
       buf.append("        </fo:flow>");

 

 
       buf.append("     </fo:page-sequence>");
 
       buf.append("   </fo:root>");
       buf.append(" </xsl:template>");

       buf.append(" <xsl:template match=\"proposal_head\">");
       buf.append("    <fo:table-row>");
       buf.append("      <fo:table-cell>");
       buf.append("        <fo:block font-weight=\"bold\">");
       buf.append("        <fo:inline text-decoration=\"underline\">");
       buf.append("        <xsl:value-of select=\"proposal_number\"/>");
       buf.append("        </fo:inline>");
       buf.append("        </fo:block>      ");
       buf.append("      </fo:table-cell>");
       buf.append("      <fo:table-cell>");
       buf.append("        <fo:block font-weight=\"bold\">");
       buf.append("        <fo:inline text-decoration=\"underline\">");
       buf.append("        <xsl:value-of select=\"title\"/>");
       buf.append("        </fo:inline>");
       buf.append("        </fo:block>");
       buf.append("      </fo:table-cell>");
       buf.append("    </fo:table-row>");
       buf.append(" </xsl:template>");
 
        buf.append("<xsl:template match=\"proposal\">");
       buf.append("    <fo:table-row>");
       buf.append("      <fo:table-cell>");
       buf.append("        <fo:block><xsl:value-of select=\"proposal_number\"/></fo:block>");
       buf.append("      </fo:table-cell>");
       buf.append("      <fo:table-cell>");
       buf.append("        <fo:block><xsl:value-of select=\"title\"/></fo:block>");
       buf.append("      </fo:table-cell>");
       buf.append("    </fo:table-row>");
       buf.append(" </xsl:template>");
       buf.append("</xsl:stylesheet>");
       return buf.toString();

    }

        private String getXSLStringforReportTwo()
    {
       StringBuffer buf = new StringBuffer("");
       buf.append("<?xml version=\"1.0\"?>");
       buf.append("<xsl:stylesheet version=\"1.0\"");
       buf.append("  xmlns:xsl=\"http://www.w3.org/1999/XSL/Transform\"");
       buf.append("  xmlns:fo=\"http://www.w3.org/1999/XSL/Format\">");

       buf.append(" <xsl:template match=\"/\">");
       buf.append("   <fo:root xmlns:fo=\"http://www.w3.org/1999/XSL/Format\">");
 
        buf.append("    <fo:layout-master-set>");
 
       buf.append("       <fo:simple-page-master master-name=\"us-standard\"");
       buf.append("          page-width=\"11in\"  page-height=\"8.5in\"");
       buf.append("          margin-top=\"0.5in\"  margin-bottom=\"0.5in\"");
       buf.append("          margin-left=\"0.5in\" margin-right=\"0.5in\">");
       buf.append("          <fo:region-body margin-top=\"1.0in\"");
       buf.append("                          margin-bottom=\"1.0in\"/>");
       buf.append("          <fo:region-before extent=\"1.0in\"/>");
       buf.append("          <fo:region-after  extent=\"1.0in\"/>");
       buf.append("       </fo:simple-page-master>");
 
       buf.append("     </fo:layout-master-set>");
 
       buf.append("     <fo:page-sequence master-reference=\"us-standard\" initial-page-number=\"1\" >");

       buf.append("        <fo:static-content flow-name=\"xsl-region-before\">");
       buf.append("          <fo:block>A Sample Report</fo:block>");
       buf.append("        </fo:static-content>");

       buf.append("        <fo:static-content flow-name=\"xsl-region-after\">");
       buf.append("          <fo:block>p. <fo:page-number/></fo:block>");
       buf.append("        </fo:static-content>");

       buf.append("        <fo:flow flow-name=\"xsl-region-body\">");
       buf.append("          <fo:table font-size=\"12pt\" ");
       buf.append("                    space-before=\"24pt\" space-after=\"24pt\">");
       buf.append("            <fo:table-column column-width=\"5in\"/>");
       buf.append("            <fo:table-column column-width=\"2in\"/>");
       buf.append("            <fo:table-body>");
       buf.append("              <xsl:apply-templates select=\"//proposal_count_head\">");
       buf.append("              </xsl:apply-templates>");
       buf.append("            </fo:table-body>");
       buf.append("            <fo:table-body>");
       buf.append("              <xsl:apply-templates select=\"//proposal\">");
       buf.append("              </xsl:apply-templates>");
       buf.append("            </fo:table-body>");
       buf.append("           </fo:table>");
       buf.append("        </fo:flow>");
 
       buf.append("     </fo:page-sequence>");
 
       buf.append("   </fo:root>");
       buf.append(" </xsl:template>");

       buf.append(" <xsl:template match=\"proposal_count_head\">");
       buf.append("    <fo:table-row>");
       buf.append("      <fo:table-cell>");
       buf.append("        <fo:block font-weight=\"bold\">");
       buf.append("        <fo:inline text-decoration=\"underline\">");
       buf.append("        <xsl:value-of select=\"unit_name\"/>");
       buf.append("        </fo:inline>");
       buf.append("        </fo:block>      ");
       buf.append("      </fo:table-cell>");
       buf.append("      <fo:table-cell>");
       buf.append("        <fo:block font-weight=\"bold\">");
       buf.append("        <fo:inline text-decoration=\"underline\">");
       buf.append("        <xsl:value-of select=\"proposal_count\"/>");
       buf.append("        </fo:inline>");
       buf.append("        </fo:block>");
       buf.append("      </fo:table-cell>");
       buf.append("    </fo:table-row>");
       buf.append(" </xsl:template>");
 
       buf.append("<xsl:template match=\"proposal\">");
       buf.append("    <fo:table-row>");
       buf.append("      <fo:table-cell>");
       buf.append("        <fo:block><xsl:value-of select=\"unit_name\"/></fo:block>");
       buf.append("      </fo:table-cell>");
       buf.append("      <fo:table-cell>");
       buf.append("        <fo:block><xsl:value-of select=\"number_proposals\"/></fo:block>");
       buf.append("      </fo:table-cell>");
       buf.append("    </fo:table-row>");
       buf.append(" </xsl:template>");
       buf.append("</xsl:stylesheet>");
       return buf.toString();

    }

    public void SampleReportToXLS(String strReport, HttpServletResponse res)
         throws ServletException, IOException, CoeusException, DBException
    {
        Vector result = null;

        String strSQL = "";
        this.strReport = strReport;
        
        if (strReport.compareTo("ReportOne")==0)
            strSQL = SQLRequest1;
        
        Vector param=new Vector();
        String strout = "";
        if(dbEngine !=null)
        {
            if (strReport.compareTo("ReportOne")==0)
                result = dbEngine.executeRequest("Coeus", strSQL,"Coeus", param);
            else
            if (strReport.compareTo("ReportTwo")==0)
                result = pobj.getProposalCountByDepartment();
        }
        else
        {
            throw new CoeusException("db_exceptionCode.1000");
        }
        
        if (result != null && result.size() > 0)
        {
            HashMap dbrow = null;
	    HSSFWorkbook wb = new HSSFWorkbook();
            res.setContentType("application/vnd.ms-excel");
            ServletOutputStream out = res.getOutputStream();
	
	    //setting the sheet name as file name without extension.
	    HSSFSheet sheet = wb.createSheet("A Sample Report");
	    // Create a row and put some cells in it. Rows are 0 based.
	    HSSFRow row = null;
	    HSSFCellStyle cellStyle = wb.createCellStyle();
	    cellStyle.setDataFormat(HSSFDataFormat.getBuiltinFormat("text"));
	    HSSFFont headerFont = wb.createFont();

	    headerFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
        
	    HSSFCellStyle headerStyle = wb.createCellStyle();

	    headerStyle.setFont(headerFont);
            StringBuffer sbuf = new StringBuffer();

            if (strReport.compareTo("ReportOne")==0)
                getReportOneExcel(row,sheet,headerStyle,result);
            else
            if (strReport.compareTo("ReportTwo")==0)
                getReportTwoExcel(row,sheet,headerStyle,result);

            wb.write(out);
            out.close();
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
    
    private void getReportOneExcel(HSSFRow row,HSSFSheet sheet, HSSFCellStyle headerStyle, Vector result)
    {
        HSSFCell cell;
        HashMap dbrow = null;
            row = sheet.createRow((short)0);

                cell = row.createCell((short)0);
                    cell.setCellValue("PROPOSAL_NUMBER");
                cell = row.createCell((short)1);
                    cell.setCellValue("TITLE");

                     //Make Headers BOLD

           	cell.setCellStyle(headerStyle);
            int listSize = result.size();
            
            for (int i=0; i < listSize; i++) 
            {
                dbrow=(HashMap)result.elementAt(i); 
                row = sheet.createRow((short)i + 1); //+1 since First Row is Header Row

                Object str1  = dbrow.get("PROPOSAL_NUMBER");
                Object str2  = dbrow.get("TITLE");
                addToXLSCell(str1,0,row);
                addToXLSCell(str2,1,row);
            }
    }

    private void getReportTwoExcel(HSSFRow row,HSSFSheet sheet, HSSFCellStyle headerStyle, Vector result)
    {
        HSSFCell cell;
        HashMap dbrow = null;
            row = sheet.createRow((short)0);

                cell = row.createCell((short)0);
                    cell.setCellValue("UNIT_NAME");
                cell = row.createCell((short)1);
                    cell.setCellValue("NUMBER_PROPOSALS");

                     //Make Headers BOLD

           	cell.setCellStyle(headerStyle);
            int listSize = result.size();
            
            for (int i=0; i < listSize; i++) 
            {
                dbrow=(HashMap)result.elementAt(i); 
                row = sheet.createRow((short)i + 1); //+1 since First Row is Header Row

                Object str1  = dbrow.get("UNIT_NAME");
                Object str2  = dbrow.get("NUMBER_PROPOSALS");
                addToXLSCell(str1,0,row);
                addToXLSCell(str2,1,row);
            }
    }
    
    private String returnCSVContent(Vector result)
    {
        HashMap row = null;
        
        int listSize = 0;
        if (result != null)
           listSize = result.size();
	StringBuffer buf = new StringBuffer("");
        if (listSize > 0)
        {
                if (strReport.compareTo("ReportOne")==0)
                {
                    buf.append("Proposal Number,");
                    buf.append("Title");
                }
                else
                if (strReport.compareTo("ReportTwo")==0)
                {
                    buf.append("Unit Name,");
                    buf.append("Number of Proposals");                    
                }
                buf.append("\n");

            	for(int rowNum = 0; rowNum < listSize; rowNum++)
		{

                	row=(HashMap)result.elementAt(rowNum); 
                        if (strReport.compareTo("ReportOne")==0)
                        {
                            buf.append(row.get("PROPOSAL_NUMBER"));
                            buf.append(",");
                            buf.append(row.get("TITLE"));
                            buf.append("\n");
                        }
                        else
                        if (strReport.compareTo("ReportTwo")==0)
                        {
                            buf.append(row.get("UNIT_NAME"));
                            buf.append(",");
                            buf.append(row.get("NUMBER_PROPOSALS"));
                            buf.append("\n");                            
                        }
		}

        }
        else
		buf.append(UMDNJStrings.NoRecordsComingBack);
	return buf.toString();
    }
}
