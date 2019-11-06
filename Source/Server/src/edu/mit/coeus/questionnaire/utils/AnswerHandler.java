package edu.mit.coeus.questionnaire.utils;
import edu.mit.coeuslite.utils.DateUtils;

import java.util.Date;

/**
 * This class handles answer validation with the conditional value of questions with various data types
 */
public class AnswerHandler {

    // Hashcode values for conditional operators
    private final int intEqualTo            = 61;
    private final int intNotEqual           = 1084;
    private final int intGreaterThan        = 62;
    private final int intLessThan           = 60;
    private final int intGreaterThanOrEqual = 1983;
    private final int intLessThanOrEqual    = 1921;

    private final String NUMBER_FORMAT      = "Number";
    //private final String STRING_FORMAT      = "String";
    private final String DATE_FORMAT        = "Date";

    private  final String DATE_SEPARATERS   = ":/.,|-";
    private  final String COEUS_DATE_FORMAT = "MM/dd/yyyy";

    /**
     *
     * @param strAnswer
     * @param strCondition
     * @param strDataType
     * @param strConditionalValue
     * @return isValidAnswer
     */
    public boolean isAnswerValid (String strAnswer, String strCondition, String strDataType, String strConditionalValue){
        boolean isValidAnswer = false;
        int intCondition        = strCondition.hashCode();
        if (strDataType.equalsIgnoreCase(NUMBER_FORMAT)){
            try{
                int intAnswer           = Integer.parseInt(strAnswer);
                int intConditionalValue = Integer.parseInt(strConditionalValue);
                isValidAnswer = validateAnswerForNumber(intAnswer,intConditionalValue,intCondition);
            }
            catch(NumberFormatException nfe){
                isValidAnswer= false;
            }
        }
        else if (strDataType.equalsIgnoreCase(DATE_FORMAT)){
            DateUtils dtUtils           = new DateUtils();
            String strAnswerDate        = dtUtils.formatDate(strAnswer,DATE_SEPARATERS,COEUS_DATE_FORMAT);
            String strConditionalDate   = dtUtils.formatDate(strConditionalValue,DATE_SEPARATERS,COEUS_DATE_FORMAT);
            if(strAnswerDate != null || strConditionalDate != null){
                isValidAnswer = validateAnswerForDate(strAnswerDate, strConditionalDate, intCondition);
            }
        }
        else{
            isValidAnswer = validateAnswerForString(strAnswer, strConditionalValue, intCondition);

        }
        return isValidAnswer;

    }

    /**
     *
     * @param strAnswer
     * @param strConditionalValue
     * @param intCondition
     * @return answerFlag
     */
    public boolean validateAnswerForString (String strAnswer, String strConditionalValue, int intCondition){
        boolean answerFlag;
         switch (intCondition) {
            case intEqualTo:
                answerFlag = equalsTo(strAnswer, strConditionalValue);
                break;
            case intNotEqual:
                answerFlag = notEqualTo(strAnswer, strConditionalValue);
                break;
            default:
                answerFlag = false;
                break;
            }
        return answerFlag;
    }

    /**
     *
     * @param strAnswerDate
     * @param strConditionalDate
     * @param intCondition
     * @return answerFlag
     */
    public boolean validateAnswerForDate (String strAnswerDate, String strConditionalDate, int intCondition){

        boolean answerFlag;
         switch (intCondition) {
            case intEqualTo:
                answerFlag = equalsTo(strAnswerDate, strConditionalDate);
                break;
            case intNotEqual:
                answerFlag = notEqualTo(strAnswerDate, strConditionalDate);
                break;
            case intGreaterThan:
                answerFlag = dateGreaterThan(strAnswerDate, strConditionalDate);
                break;
            case intLessThan:
                answerFlag = dateLessThan(strAnswerDate, strConditionalDate);
                break;
            case intGreaterThanOrEqual:
                answerFlag = dateGreaterThanOrEquals(strAnswerDate, strConditionalDate);
                break;
            case intLessThanOrEqual:
                answerFlag = dateLessThanOrEquals(strAnswerDate, strConditionalDate);
                break;
            default:
                answerFlag = false;
                break;
            }
        return answerFlag;
    }

    /**
     *
     * @param intAnswer
     * @param intConditionalValue
     * @param intCondition
     * @return answerFlag
     */
    public  boolean validateAnswerForNumber(int intAnswer,  int intConditionalValue, int intCondition) {
        boolean answerFlag;
         switch (intCondition) {
            case intEqualTo:
                answerFlag = equalsTo(intAnswer, intConditionalValue);
                break;
            case intNotEqual:
                answerFlag = notEqualTo(intAnswer, intConditionalValue);
                break;
            case intGreaterThan:
                answerFlag = greaterThan(intAnswer, intConditionalValue);
                break;
            case intLessThan:
                answerFlag = lessThan(intAnswer, intConditionalValue);
                break;
            case intGreaterThanOrEqual:
                answerFlag = greaterThanOrEquals(intAnswer, intConditionalValue);
                break;
            case intLessThanOrEqual:
                answerFlag = lessThanOrEquals(intAnswer, intConditionalValue);
                break;
            default:
                answerFlag = false;
                break;
            }
        return answerFlag;
    }

    /**
     *
     * @param intAnswer
     * @param intConditionalValue
     * @return flag
     */
    public boolean equalsTo(int intAnswer, int intConditionalValue){
        boolean flag = false;

        if(intAnswer == intConditionalValue){
            flag = true;
        }
        return flag;
    }

    /**
     *
     * @param strAnswer
     * @param strConditionalValue
     * @return flag
     */
    public boolean equalsTo(String strAnswer, String strConditionalValue){
        boolean flag = false;

        if(isNotNull(strAnswer) && strAnswer.equalsIgnoreCase(strConditionalValue)){
            flag = true;
        }
        return flag;
    }

    /**
     *
     * @param intAnswer
     * @param intConditionalValue
     * @return flag
     */
    public boolean notEqualTo(int intAnswer, int intConditionalValue){
        boolean flag = false;
        if(intAnswer != intConditionalValue){
            flag = true;
        }
        return flag;
    }

    /**
     *
     * @param strAnswer
     * @param strConditionalValue
     * @return flag
     */
    public boolean notEqualTo(String strAnswer, String strConditionalValue){
        boolean flag = false;
        if(!strAnswer.equalsIgnoreCase(strConditionalValue)){
            flag = true;
        }
        return flag;
    }

    /**
     *
     * @param intAnswer
     * @param intConditionalValue
     * @return flag
     */
    public boolean greaterThan(int intAnswer, int intConditionalValue){
        boolean flag = false;
        if(intAnswer > intConditionalValue){
            flag = true;
        }
        return flag;
    }

    /**
     *
     * @param intAnswer
     * @param intConditionalValue
     * @return flag
     */
    public boolean lessThan(int intAnswer, int intConditionalValue){
        boolean flag = false;
        if(intAnswer < intConditionalValue){
            flag = true;
        }
        return flag;
    }

    /**
     *
     * @param intAnswer
     * @param intConditionalValue
     * @return flag
     */
    public boolean greaterThanOrEquals(int intAnswer, int intConditionalValue){
        boolean flag = false;
        if(intAnswer >= intConditionalValue){
            flag = true;
        }
        return flag;
    }

    /**
     *
     * @param intAnswer
     * @param intConditionalValue
     * @return flag
     */
    public boolean lessThanOrEquals(int intAnswer, int intConditionalValue){
        boolean flag = false;
        if(intAnswer <= intConditionalValue){
            flag = true;
        }
        return flag;
    }

    /**
     *
     * @param strAnswerDate
     * @param strConditionalDate
     * @return flag
     */
    public boolean dateGreaterThan(String strAnswerDate, String strConditionalDate){
        //boolean flag = true;
        // to be implemented
        return true;
    }

    /**
     *
     * @param strAnswerDate
     * @param strConditionalDate
     * @return flag
     */
    public boolean dateLessThan(String strAnswerDate, String strConditionalDate){
        //boolean flag = true;
        // to be implemented
       return true;
    }

    /**
     *
     * @param strAnswerDate
     * @param strConditionalDate
     * @return flag
     */
    public boolean dateGreaterThanOrEquals(String strAnswerDate, String strConditionalDate){
        //boolean flag = true;
        // to be implemented
        Date d = new Date();
       return true;
    }

    /**
     *
     * @param strAnswerDate
     * @param strConditionalDate
     * @return flag
     */
    public boolean dateLessThanOrEquals(String strAnswerDate, String strConditionalDate){
        //boolean flag = true;
        // to be implemented
       return true;
    }
    /**
     *
     * @param input
     * @return
     */
    public String trimValue(String input){
        String output = "";
        if (input != null && input.length()!= 0){
            output = input.trim();
        }
        return output;

    }
    /**
     *
     * @param strInput
     * @return
     */
    public boolean isNotNull (String strInput){
        boolean flag = false;
        if (trimValue(strInput).length() > 0){
            flag = true;
        }
        return flag;
    }

}
