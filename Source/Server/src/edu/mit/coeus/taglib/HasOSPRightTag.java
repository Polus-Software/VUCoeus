/*
* @(#) HasOSPRightTag.java	1.0 06/14/2002 23:31:19
*
* Copyright (c) Massachusetts Institute of Technology
* 77 Massachusetts Avenue, Cambridge, MA 02139-4307
* All rights reserved.
*
*/
package edu.mit.coeus.taglib;

import javax.servlet.jsp.tagext.TagSupport;
import javax.servlet.jsp.tagext.BodyTagSupport;
import javax.servlet.jsp.tagext.BodyContent;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.PageContext;
import javax.servlet.ServletRequest;
import java.io.PrintWriter;
import java.io.IOException;

/**
 * JSP Tag <b>hasOSPRight</b>, used to determine if a user has any OSP rights.
 * <p>
 * Includes the body of the tag if the attribute exists.
 * <p>
 * You can check the appropriate right by setting the tag attribute <b>name</b> to
 * some predefined string constants.
 * 1)Set the name as <code><b>hasOSPRightToView</b></code> to check
 * if user has the privilege to view other's disclosures.<br>
 * 2)Set the name as <code><b>hasOSPRightToEdit</b></code> to check
 * if user has the privilege to edit other's disclosures.<br>
 * 2)Set the name as <code><b>noRights</b></code> to check
 * if user has any privilege to View or Edit other's disclosures.
 * <br>
 * The body of the tag is included if existsAttribute matches
 * the value.
 * <p>
 * JSP Tag Lib Descriptor
 * <p><pre>
 * &lt;name&gt;hasOSPRightTag&lt;/name&gt;
 * &lt;tagclass&gt;edu.mit.coeus.coi.taglib.HasOSPRightTag&lt;/tagclass&gt;
 * &lt;bodycontent&gt;JSP&lt;/bodycontent&gt;
 * &lt;info&gt;Includes the body of the tag if the user has the right privilege&lt;/info&gt;
 *   &lt;attribute&gt;
 *     &lt;name&gt;name&lt;/name&gt;
 *     &lt;required&gt;true&lt;/required&gt;
 *     &lt;rtexprvalue&gt;false&lt;/rtexprvalue&gt;
 *   &lt;/attribute&gt;
 *   &lt;attribute&gt;
 *     &lt;name&gt;value&lt;/name&gt;
 *     &lt;required&gt;true&lt;/required&gt;
 *     &lt;rtexprvalue&gt;false&lt;/rtexprvalue&gt;
 *   &lt;/attribute&gt;
 * </pre>
 *
 * @author Geo Thomas
 */

public class HasOSPRightTag extends BodyTagSupport {

    /** property declaration for tag attribute: value.
     */
    private int ospRight;

    /** property declaration for tag attribute: name.
     */
    private String ospRightType;

    public HasOSPRightTag() {
        super();
    }
    // methods called from doStartTag()
    //
    /**
     *
     * Fill in this method to perform other operations from doStartTag().
     *
     */
    public void otherDoStartTagOperations()  {

        //
        // TODO: code that performs other operations in doStartTag
        //       should be placed here.
        //       It will be called after initializing variables,
        //       finding the parent, setting IDREFs, etc, and
        //       before calling theBodyShouldBeEvaluated().
        //

    }

    /**
     *
     * Fill in this method to determine if the tag body should be evaluated
     * Called from doStartTag().
     *
     */
    public boolean theBodyShouldBeEvaluated()  {

        //
        // TODO: code that determines whether the body should be
        //       evaluated should be placed here.
        //       Called from the doStartTag() method.
        //
        if(ospRightType.equalsIgnoreCase("norights") && ospRight==0){
            return true;
        }else if(ospRightType.equalsIgnoreCase("hasosprighttoview") && ospRight>0){
            return true;
        }else if(ospRightType.equalsIgnoreCase("hasosprighttoedit") && ospRight==2){
            return true;
        }else{
            return false;
        }
    }


    //
    // methods called from doEndTag()
    //
    /**
     *
     * Fill in this method to perform other operations from doEndTag().
     *
     */
    public void otherDoEndTagOperations()  {

        //
        // TODO: code that performs other operations in doEndTag
        //       should be placed here.
        //       It will be called after initializing variables,
        //       finding the parent, setting IDREFs, etc, and
        //       before calling shouldEvaluateRestOfPageAfterEndTag().
        //


    }

    /**
     *
     * Fill in this method to determine if the rest of the JSP page
     * should be generated after this tag is finished.
     * Called from doEndTag().
     *
     */
    public boolean shouldEvaluateRestOfPageAfterEndTag()  {

        //
        // TODO: code that determines whether the rest of the page
        //       should be evaluated after the tag is processed
        //       should be placed here.
        //       Called from the doEndTag() method.
        //
        return true;

    }


    //
    // methods called from doAfterBody()
    //
    /**
     *
     * Fill in this method to determine if the tag body should be evaluated
     * again after evaluating the body.
     * Use this method to create an iterating tag.
     * Called from doAfterBody().
     *
     */
    public boolean theBodyShouldBeEvaluatedAgain()  {

        //
        // TODO: code that determines whether the tag body should be
        //       evaluated again after processing the tag
        //       should be placed here.
        //       You can use this method to create iterating tags.
        //       Called from the doAfterBody() method.
        //
        return false;

    }


    /** .//GEN-BEGIN:doStartTag
     *
     * This method is called when the JSP engine encounters the start tag,
     * after the attributes are processed.
     * Scripting variables (if any) have their values set here.
     * @return EVAL_BODY_BUFFERED if the JSP engine should evaluate the tag body, otherwise return SKIP_BODY.
     * This method is automatically generated. Do not modify this method.
     * Instead, modify the methods that this method calls.
     *
     */
    public int doStartTag() throws JspException, JspException {
        otherDoStartTagOperations();

        if (theBodyShouldBeEvaluated()) {
            return EVAL_BODY_BUFFERED;
        } else {
            return SKIP_BODY;
        }
    }//GEN-END:doStartTag

    /** .//GEN-BEGIN:doEndTag
     *
     *
     * This method is called after the JSP engine finished processing the tag.
     * @return EVAL_PAGE if the JSP engine should continue evaluating the JSP page, otherwise return SKIP_PAGE.
     * This method is automatically generated. Do not modify this method.
     * Instead, modify the methods that this method calls.
     *
     */
    public int doEndTag() throws JspException, JspException {
        otherDoEndTagOperations();

        if (shouldEvaluateRestOfPageAfterEndTag()) {
            return EVAL_PAGE;
        } else {
            return SKIP_PAGE;
        }
    }//GEN-END:doEndTag

    /** .//GEN-BEGIN:doAfterbody
     *
     *
     * This method is called after the JSP engine processes the body content of the tag.
     * @return EVAL_BODY_BUFFERED if the JSP engine should evaluate the tag body again, otherwise return SKIP_BODY.
     * This method is automatically generated. Do not modify this method.
     * Instead, modify the methods that this method calls.
     *
     */
    public int doAfterBody() throws JspException, JspException {
        try {
            //
            // This code is generated for tags whose bodyContent is "JSP"
            //
            JspWriter out = getPreviousOut();
            BodyContent bodyContent = getBodyContent();

            writeTagBodyContent(out, bodyContent);
        } catch (Exception ex) {
            throw new JspException("error in HasOSPRightTag: " + ex);
        }

        if (theBodyShouldBeEvaluatedAgain()) {
            return EVAL_BODY_BUFFERED;
        } else {
            return SKIP_BODY;
        }
    }//GEN-END:doAfterbody

    /**
     * Fill in this method to process the body content of the tag.
     * You only need to do this if the tag's BodyContent property
     * is set to "JSP" or "tagdependent."
     * If the tag's bodyContent is set to "empty," then this method
     * will not be called.
     *
     */
    public void writeTagBodyContent(JspWriter out, BodyContent bodyContent) throws IOException {
        //
        // TODO: insert code to write html before writing the body content.
        //       e.g.  out.println("<B>" + getAttribute1() + "</B>");
        //             out.println("   <BLOCKQUOTE>");

        //
        // write the body content (after processing by the JSP engine) on the output Writer
        //
        bodyContent.writeOut(out);

        //
        // Or else get the body content as a string and process it, e.g.:
        //     String bodyStr = bodyContent.getString();
        //     String result = yourProcessingMethod(bodyStr);
        //     out.println(result);
        //

        // TODO: insert code to write html after writing the body content.
        //       e.g.  out.println("   <BLOCKQUOTE>");

        // clear the body content for the next time through.
        bodyContent.clearBody();
    }

    /**
     *  Getter method for the value for the <code>name</code> property
     *  @return String <code>name</code> property value
     */
    public String getName() {
        return ospRightType;
    }
    /**
     *  Setter method for the value for the <code>name</code> property
     *  @param String <code>name</code> property value
     */
    public void setName(String value) {
        ospRightType = value;
    }
    /**
     *  Getter method for the value for the <code>value</code> property
     *  @return int <code>value</code> property value
     */
    public int getValue() {
        return ospRight;
    }
    /**
     *  Setter method for the value for the <code>value</code> property
     *  @param String <code>value</code> property value
     */
    public void setValue(int value) {
        ospRight = value;
    }
}
