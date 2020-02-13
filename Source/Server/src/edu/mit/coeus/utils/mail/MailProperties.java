/*
 * MailProperties.java
 *
 * Created on October 12, 2006, 11:18 AM
 */

package edu.mit.coeus.utils.mail;

//import edu.mit.coeus.departmental.bean.DepartmentPersonTxnBean;
import edu.mit.coeus.exception.CoeusException;
//import edu.mit.coeus.utils.CoeusFunctions;
//import edu.mit.coeus.utils.CoeusProperties;
import edu.mit.coeus.utils.UtilFactory;
import edu.mit.coeus.utils.dbengine.DBException;
import java.io.IOException;
import java.io.InputStream;
import java.text.MessageFormat;
import java.util.Properties;

/**
 *
 * @author  geot
 */
public class MailProperties implements MailPropertyKeys{
    private static Properties mailProperties;
    private static final String MAIL_PROPERTIES = "MailProperties";
    private Properties props;
//    private CoeusFunctions coeusFunctions;
    /** Creates a new instance of MailProperties */
    private MailProperties() {
//        coeusFunctions = new CoeusFunctions();
        mailProperties = new Properties();
    }
    private void loadProperties() throws CoeusException,DBException{
//        mailProperties.put(CMS_MAIL_HOST, getValue(CMS_MAIL_HOST));
//        mailProperties.put(CMS_MAIL_PORT, getValue(CMS_MAIL_PORT));
//        mailProperties.put(CMS_ENABLED, getValue(CMS_ENABLED));
//        mailProperties.put(CMS_MODE, getValue(CMS_MODE));
//        mailProperties.put(CMS_TEST_MAIL_RECEIVE_ID, getValue(CMS_TEST_MAIL_RECEIVE_ID));
//        mailProperties.put(CMS_SENDER_ID, getValue(CMS_SENDER_ID));
//        mailProperties.put(CMS_REPLY_TO_ID, getValue(CMS_REPLY_TO_ID));
//        mailProperties.put(CMS_DEFAULT_DOMAIN, getValue(CMS_DEFAULT_DOMAIN));
//        mailProperties.put(CMS_MAIL_USER_ID,getValue(CMS_MAIL_USER_ID));
//        mailProperties.put(CMS_MAIL_PASSWORD,getValue(CMS_MAIL_PASSWORD));
        loadValuesFromPropFile();
        // Coeus4.3 Email notification enhancement - start
        //Commented to avoid getting the mail parameters from database
//        loadValue(CMS_MAIL_HOST);
//        loadValue(CMS_MAIL_PORT);
//        loadValue(CMS_ENABLED);
//        loadValue(CMS_MODE);
//        loadValue(CMS_TEST_MAIL_RECEIVE_ID);
//        loadValue(CMS_SENDER_ID);
//        loadValue(CMS_REPLY_TO_ID);
//        loadValue(CMS_DEFAULT_DOMAIN);
//        loadValue(CMS_MAIL_USER_ID);
//        loadValue(CMS_MAIL_PASSWORD);
        //Coeus4.3 Email notification - end
    }
//    private void loadValue(String parameter) throws CoeusException,DBException{
////	String value = coeusFunctions.getParameterValue(parameter);
//        String value = mailProperties.getProperty(parameter);
//	if(value!=null && !value.equals("")){
//            mailProperties.put(parameter, value);
//        }
////        return coeusFunctions.getParameterValue(parameter);
////        return value==null?getValueFromPropFile(parameter):value;
//    }
    private void loadValuesFromPropFile(){
//        if(props!=null) return props.getProperty(key);
        InputStream stream = null;
        try {
            props = new Properties();
            stream = new MailProperties().getClass().getResourceAsStream("/CoeusMailService.properties");
            props.load( stream );
        }catch(IOException ex){
            UtilFactory.log(ex.getMessage(),ex, MAIL_PROPERTIES, "getValueFromPropFile");
            return;
        }finally {
            try {
                if(stream!=null){
                    stream.close();
                }
            } catch (Exception ex) {
                ex.printStackTrace();
                UtilFactory.log(ex.getMessage(),ex, MAIL_PROPERTIES, "getValueFromPropFile");
            }
        }
        mailProperties.putAll(props);
//        return props.getProperty(key);
    }
    public static String getProperty(String parameter){
        return getProperty(parameter,"");
    }
    public static String getProperty(String parameter,String defValue){
        try{
        if(mailProperties==null) new MailProperties().loadProperties();
            // Coeus4.3 Email notification enhancement - start
            //Commented to avoid getting the mail parameters from database
//            String value = (String)mailProperties.get(parameter);
//            if (value==null||value.equals("")){
//                new MailProperties().loadValue(parameter);
//            }
            // Coeus4.3 Email notification enhancement - end
            return mailProperties.getProperty(parameter,defValue);
        }catch(DBException ex){
            UtilFactory.log(ex.getMessage(),ex, MAIL_PROPERTIES, "getProperty");
            return "";
        }catch(CoeusException ex){
            UtilFactory.log(ex.getMessage(),ex, MAIL_PROPERTIES, "getProperty");
            return "";
        }
        
    }
   /* Addded for case 3916-Link to protocol not appearing in all routing emails - start */
    public static String getProperty(String parameter, String[] params){
        String property = getProperty(parameter,"");
        MessageFormat formatter = new MessageFormat("");
        return formatter.format(property,params);  
    }
  /* Addded for case 3916-Link to protocol not appearing in all routing emails - end */
    
}
