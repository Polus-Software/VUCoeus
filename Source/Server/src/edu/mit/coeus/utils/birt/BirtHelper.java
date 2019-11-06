/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.mit.coeus.utils.birt;

import edu.mit.coeus.exception.CoeusException;
import edu.mit.coeus.utils.birt.bean.BirtConstants;
import edu.mit.coeus.utils.birt.bean.BirtParameterBean;
import edu.mit.coeus.utils.birt.bean.BirtReportTxnBean;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Iterator;
import java.util.List;

import org.eclipse.birt.report.engine.api.EXCELRenderOption;
import org.eclipse.birt.report.engine.api.HTMLRenderOption;
import org.eclipse.birt.report.engine.api.IGetParameterDefinitionTask;
import org.eclipse.birt.report.engine.api.IParameterDefnBase;
import org.eclipse.birt.report.engine.api.IParameterGroupDefn;
import org.eclipse.birt.report.engine.api.IParameterSelectionChoice;
import org.eclipse.birt.report.engine.api.IReportEngine;
import org.eclipse.birt.report.engine.api.IReportRunnable;
import org.eclipse.birt.report.engine.api.IRunAndRenderTask;
import org.eclipse.birt.report.engine.api.IScalarParameterDefn;
import org.eclipse.birt.report.engine.api.PDFRenderOption;
import org.eclipse.birt.report.engine.api.RenderOption;
import org.eclipse.birt.report.model.api.CascadingParameterGroupHandle;
import org.eclipse.birt.report.model.api.ReportDesignHandle;
import org.eclipse.birt.report.model.api.ScalarParameterHandle;

/**
 *
 * @author sharathk
 */
public class BirtHelper {

    private static IReportEngine engine = null;

    public BirtHelper()throws CoeusException {
        BirtInstance birtInstance = BirtInstance.getInstance();
        engine = birtInstance.getIReportEngine();
    }

    public List getParameters(InputStream reportStream) throws Exception {
        IReportRunnable design = null;
        List listParameters = new ArrayList();

        //Open a report design
        design = engine.openReportDesign(reportStream);

        //Create Parameter Definition Task and retrieve parameter definitions
        IGetParameterDefinitionTask task = engine.createGetParameterDefinitionTask(design);
        Collection params = task.getParameterDefns(true);

        //Iterate over each parameter
        Iterator iter = params.iterator();
        while (iter.hasNext()) {
            IParameterDefnBase param = (IParameterDefnBase) iter.next();

            if (param instanceof IParameterGroupDefn) {
                IParameterGroupDefn group = (IParameterGroupDefn) param;
                //System.out.println( "Parameter Group: " + group.getName( ) );
                // Do something with the parameter group.
                // Iterate over group contents.
                Iterator i2 = group.getContents().iterator();
                while (i2.hasNext()) {
                    IScalarParameterDefn scalar = (IScalarParameterDefn) i2.next();
                    listParameters.add(loadParameterDetails(task, scalar, design, group));
                }
            } else {
                IScalarParameterDefn scalar = (IScalarParameterDefn) param;
                //get details on the parameter
                listParameters.add(loadParameterDetails(task, scalar, design, null));
            }
        }

        //Destroy the engine and shutdown the Platform
        //Note - If the program stays resident do not shutdown the Platform or the Engine
        //engine.shutdown();
        //Platform.shutdown();
        //System.out.println("Finished");
        return listParameters;
    }

    //Function to load parameter details in a map.
    private BirtParameterBean loadParameterDetails(IGetParameterDefinitionTask task, IScalarParameterDefn scalar, IReportRunnable report, IParameterGroupDefn group) {

        BirtParameterBean birtParameterBean = new BirtParameterBean();

        birtParameterBean.setName(scalar.getName());
        birtParameterBean.setHelp(scalar.getHelpText());
        birtParameterBean.setFormat(scalar.getDisplayFormat());
        birtParameterBean.setDefaultValue(scalar.getDefaultValue());
        /*
        //HashMap<String, Serializable> parameter = new HashMap<String, Serializable>();
        if (group == null) {
            //parameter.put("Parameter Group", "Default");
        } else {
            //parameter.put("Parameter Group", group.getName());
        }
        //parameter.put("Name", scalar.getName());
        //parameter.put("Help Text", scalar.getHelpText());
        //parameter.put("Display Name", scalar.getDisplayName());
        //this is a format code such as  > for UPPERCASE
        //parameter.put("Display Format", scalar.getDisplayFormat());
        if (scalar.isHidden()) {
            //parameter.put("Hidden", "Yes");
        } else {
            //parameter.put("Hidden", "No");
        }
        if (scalar.allowBlank()) {
            //parameter.put("Allow Blank", "Yes");
        } else {
            //parameter.put("Allow Blank", "No");
        }
        if (scalar.allowNull()) {
            //parameter.put("Allow Null", "Yes");
        } else {
            //parameter.put("Allow Null", "No");
        }
        if (scalar.isValueConcealed()) {
            //parameter.put("Conceal Entry", "Yes");  //ie passwords etc
        } else {
            //parameter.put("Conceal Entry", "No");
        }
        */

        birtParameterBean.setHidden(scalar.isHidden());
        birtParameterBean.setRequired(scalar.isRequired());

        switch (scalar.getControlType()) {
            case IScalarParameterDefn.TEXT_BOX:
                birtParameterBean.setControlType(BirtConstants.TYPE_TEXT);
                break;
            case IScalarParameterDefn.LIST_BOX:
                birtParameterBean.setControlType(BirtConstants.TYPE_COMBO_BOX);
                break;
            case IScalarParameterDefn.RADIO_BUTTON:
                birtParameterBean.setControlType(BirtConstants.TYPE_COMBO_BOX);
                break;
            case IScalarParameterDefn.CHECK_BOX:
                birtParameterBean.setControlType(BirtConstants.TYPE_COMBO_BOX);
                break;
            default:
                birtParameterBean.setControlType(BirtConstants.TYPE_TEXT);
                break;
        }

        switch (scalar.getDataType()) {
            case IScalarParameterDefn.TYPE_STRING:
                birtParameterBean.setDataType(BirtConstants.STRING);
                break;
            case IScalarParameterDefn.TYPE_FLOAT:
                birtParameterBean.setDataType(BirtConstants.FLOAT);
                break;
            case IScalarParameterDefn.TYPE_DECIMAL:
                birtParameterBean.setDataType(BirtConstants.DECIMAL);
                break;
            case IScalarParameterDefn.TYPE_DATE:
                birtParameterBean.setDataType(BirtConstants.DATE);
                break;
            case IScalarParameterDefn.TYPE_DATE_TIME:
                birtParameterBean.setDataType(BirtConstants.DATE_TIME);
                break;
            case IScalarParameterDefn.TYPE_BOOLEAN:
                birtParameterBean.setDataType(BirtConstants.BOOLEAN);
                break;
            default:
                birtParameterBean.setDataType(BirtConstants.STRING);
                break;
        }
        //Get report design and find default value, prompt text and data set expression using the DE API
        ReportDesignHandle reportHandle = (ReportDesignHandle) report.getDesignHandle();
        ScalarParameterHandle parameterHandle = (ScalarParameterHandle) reportHandle.findParameter(scalar.getName());
        //birtParameterBean.setDefaultValue(parameterHandle.getDefaultValueListMethod());
        birtParameterBean.setPromptText(parameterHandle.getPromptText());
        if (scalar.getControlType() != IScalarParameterDefn.TEXT_BOX) {
            //retrieve selection list for cascaded parameter
            if (parameterHandle.getContainer() instanceof CascadingParameterGroupHandle) {
                Collection sList = Collections.EMPTY_LIST;
                if (parameterHandle.getContainer() instanceof CascadingParameterGroupHandle) {
                    int index = parameterHandle.getContainerSlotHandle().findPosn(parameterHandle);
                    Object[] keyValue = new Object[index];
                    for (int i = 0; i < index; i++) {
                        ScalarParameterHandle handle = (ScalarParameterHandle) ((CascadingParameterGroupHandle) parameterHandle.getContainer()).getParameters().get(i);
                        //Use parameter default values
                        keyValue[i] = handle.getDefaultValueListMethod();
                    }
                    String groupName = parameterHandle.getContainer().getName();
                    task.evaluateQuery(groupName);
                    sList = task.getSelectionListForCascadingGroup(groupName, keyValue);
                    LinkedHashMap dynamicList = new LinkedHashMap(); //Birt Parameter Values not in correct sort order. HashMap doesn't guarantee the sort order.
                    for (Iterator sl = sList.iterator(); sl.hasNext();) {
                        IParameterSelectionChoice sI = (IParameterSelectionChoice) sl.next();
                        Object value = sI.getValue();
                        Object label = sI.getLabel();
                        dynamicList.put(value, (String) label);
                    }
                    birtParameterBean.setValues(dynamicList);
                }
            } else {
                //retrieve selection list
                Collection selectionList = task.getSelectionList(scalar.getName());
                if (selectionList != null) {
                    LinkedHashMap dynamicList = new LinkedHashMap();
                    for (Iterator sliter = selectionList.iterator(); sliter.hasNext();) {
                        IParameterSelectionChoice selectionItem = (IParameterSelectionChoice) sliter.next();
                        Object value = selectionItem.getValue();
                        String label = selectionItem.getLabel();
                        dynamicList.put(value, label);
                    }
                    birtParameterBean.setValues(dynamicList);
                }
            }
        }
        return birtParameterBean;
    }

    public byte[] getReport(HashMap parameters, int reportId, String reportType)throws Exception {
        //Open the report design
        BirtReportTxnBean birtReportTxnBean = new BirtReportTxnBean();
        ByteArrayOutputStream baos = birtReportTxnBean.getReportTemplate(reportId);
        InputStream inputStream = new ByteArrayInputStream(baos.toByteArray());

        IReportRunnable design = null;
        design = engine.openReportDesign(inputStream);

        //Create task to run and render the report,
        IRunAndRenderTask task = engine.createRunAndRenderTask(design);

        /**
        //Set Render context to handle url and image locataions
        HTMLRenderContext renderContext = new HTMLRenderContext();
        //Set the Base URL for all actions
        renderContext.setBaseURL("http://localhost/");
        //Tell the Engine to prepend all images with this URL - Note this requires using the HTMLServerImageHandler
        renderContext.setBaseImageURL("http://localhost/myimages");
        //Tell the Engine where to write the images to
        renderContext.setImageDirectory("C:/xampplite/htdocs/myimages");
        //Tell the Engine what image formats are supported.  Note you must have SVG in the string
        //to render charts in SVG.
        renderContext.setSupportedImageFormats("JPG;PNG;BMP;SVG");
        //HashMap<String, HTMLRenderContext> contextMap = new HashMap<String, HTMLRenderContext>();
        HashMap contextMap = new HashMap();
        //contextMap.put( EngineConstants.APPCONTEXT_HTML_RENDER_CONTEXT, renderContext );
        contextMap.put(EngineConstants.APPCONTEXT_PDF_RENDER_CONTEXT, renderContext);

        task.setAppContext(contextMap);
        //Set parameters for the report
        task.setParameterValues(parameters);
        //Alternatively set each seperately
        //task.setParameterValue("Top Count", new Integer(12));
        
        //Add a scriptable object, which will allow the report developer to put
        //script in the report that references this Java object. eg in script
        //pFilter.myjavamethod()
        //ProcessFilter pf = new ProcessFilter();
        //task.addScriptableJavaObject("pFilter", pf);

        //Set rendering options - such as file or stream output,
        //output format, whether it is embeddable, etc
        HTMLRenderOption options = new HTMLRenderOption();

        //Remove HTML and Body tags
        //options.setEmbeddable(true);

        //Set ouptut location
        //options.setOutputFileName("C:/sharath/installables/BIRT/output.html");
        
        //Set output format
        options.setOutputFormat("html");
        */
        HashMap contextMap = new HashMap();
        task.setAppContext(contextMap);
        task.setParameterValues(parameters);
        task.validateParameters();

        RenderOption renderOption;
        if (reportType != null && reportType.equals(BirtConstants.HTML)) {
            //HTML Rendering - START
            //Set Render context to handle url and image locataions
            renderOption = new HTMLRenderOption();
            renderOption.setOutputFormat("html");
            //HTML Rendering - STOP
        } else if (reportType != null && reportType.equals(BirtConstants.EXCEL)) {
            //Excel Rendering
            renderOption = new EXCELRenderOption();
            renderOption.setOutputFormat("xls");
        } else {
            //Defaults to PDF
            renderOption = new PDFRenderOption();
            renderOption.setOutputFormat("pdf");
        }
        ByteArrayOutputStream bostream = new ByteArrayOutputStream();
        renderOption.setOutputStream(bostream);
        task.setRenderOption(renderOption);

        //run the report
        //Note - If the program stays resident do not shutdown the Platform or the Engine
        task.run();
        task.close();

        return bostream.toByteArray();
    }
}
