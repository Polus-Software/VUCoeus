/*
 * QuestionnaireController.java
 *
 * Created on September 19, 2006, 3:52 PM
 */

package edu.mit.coeus.questionnaire.controller;

import edu.mit.coeus.gui.CoeusAppletMDIForm;
import edu.mit.coeus.gui.CoeusInternalFrame;
import edu.mit.coeus.gui.event.Controller;
import edu.mit.coeus.questionnaire.bean.QuestionnaireMaintainaceBaseBean;
import edu.mit.coeus.utils.CoeusGuiConstants;
import edu.mit.coeus.utils.CoeusOptionPane;
import java.beans.PropertyVetoException;

/**
 *
 * @author  chandrashekara
 */
public abstract class QuestionnaireController extends Controller {
    public String EMPTY_STRING = "";
    private QuestionnaireMaintainaceBaseBean baseBean;
    public String queryKey = EMPTY_STRING;
    private CoeusAppletMDIForm mdiForm = CoeusGuiConstants.getMDIForm();
    /** Creates a new instance of QuestionnaireController */
    public QuestionnaireController() {
    }
    /** creates an instance of QuestionnaireController based on the QuestionnaireMaintainaceBaseBean 
     *and prepares the queryKey for the questionnaire for the department
     *@param QuestionnaireMaintainaceBaseBean
     */
     public QuestionnaireController(QuestionnaireMaintainaceBaseBean baseBean){
        if(baseBean != null && baseBean.getUnitNumber() != null) {
            this.baseBean  = baseBean;
            queryKey = baseBean.getUnitNumber();
        }
    }
     
     
     /** This method is used to check whether the given questionnaire number is already
      * opened in the given mode or not.
      * @param refId refId - for questionnaire number
      * @param mode mode of Form open.
      * @param displayMessage if true displays error messages else doesn't.
      * @return true if questionnaire window is already open else returns false.
      */
     boolean isWindowOpen(String refId, char mode, boolean displayMessage) {
        boolean duplicate = false;
        try{
            duplicate = mdiForm.checkDuplicate(CoeusGuiConstants.QUESTIONNAIRE_TITLE, refId, mode );
        }catch(Exception e){
            // Exception occured.  Record may be already opened in requested mode
            //   or if the requested mode is edit mode and application is already
            //   editing any other record. 
            duplicate = true;
            if( displayMessage ){
                if(e.getMessage().length() > 0 ) {
                    CoeusOptionPane.showInfoDialog(e.getMessage());
                }
            }
            // try to get the requested frame which is already opened 
            CoeusInternalFrame frame = mdiForm.getFrame(
                    CoeusGuiConstants.QUESTIONNAIRE_TITLE,refId);
            
            if (frame != null){
                try{
                    frame.setSelected(true);
                    frame.setVisible(true);
                }catch (PropertyVetoException propertyVetoException) {
                    
                }
            }
            return true;
        }
        return false;
     }
    
     
     /** This method is used to check whether the given questionnaire number is already
      * opened in the given mode or not and displays message if the proposal is open
      * @param refId refId - for inst proposal its questionnaire Number.
      * @param mode mode of Form open.
      * @return true if questionanire window is already open else returns false.
      */
     boolean isWindowOpen(String refId, char mode){
         return isWindowOpen(refId, mode, true);
     }
}
