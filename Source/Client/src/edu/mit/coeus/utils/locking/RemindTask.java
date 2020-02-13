/*
 * ReminderTask.java
 *
 * Created on September 6, 2004, 2:46 PM
 */

/*
 * Performed PMD Check, commented unused imports and variables on 02-Jan-2009
 * by Sreenath
 */
package edu.mit.coeus.utils.locking;

import edu.mit.coeus.utils.CoeusConstants;
import edu.mit.coeus.utils.CoeusServerProperties;
import java.util.*;

import edu.mit.coeus.utils.CoeusOptionPane;
import edu.mit.coeus.utils.locking.PollingMonitor;
import edu.mit.coeus.utils.CoeusGuiConstants;
import edu.mit.coeus.gui.CoeusAppletMDIForm;

/**
 *
 * @author  ajaygm
 */



public class RemindTask {
  
    private PollingMonitor pollingMonitor;
    CoeusAppletMDIForm mdiForm = null;
    private static RemindTask remindTask = null;
    private Hashtable htLockIds;  
    private boolean pollingCheck = false;
    Timer timer;
    
    private RemindTask() {
//        pollingMonitor = new PollingMonitor();
        htLockIds = new Hashtable();
        pollingMonitor = pollingMonitor.getInstance();   
        mdiForm = CoeusGuiConstants.getMDIForm();
        setTimer();
    }
    
    /**
     * @return is instance of RemindTask
     */    
    public static RemindTask getInstance() {
        if(remindTask == null){
            remindTask = new RemindTask();            
            //remindTask.setTimer();
        }    
        return remindTask;
    }    
    
    
    public void setTimer() {
        timer = new Timer();
        // 2930: Auto-delete Current Locks based on new parameter - Start
//        int pollingInterval = CoeusGuiConstants.POLLING_INTERVAL;
        double pollingInterval = 60;
        try {
//            pollingInterval = Integer.parseInt(CoeusServerProperties.getProperty(CoeusConstants.LOCK_UPDATE_INTERVAL, "60"));
            pollingInterval = Double.parseDouble(CoeusServerProperties.getProperty(CoeusConstants.LOCK_UPDATE_INTERVAL, "60"));
        } catch (NumberFormatException ex) {
            ex.printStackTrace();
        }
        // 2930: Auto-delete Current Locks based on new parameter - End
        htLockIds = getHtLockIds();
        if(htLockIds!= null && htLockIds.size() > 0){
            try{
                pollingCheck = true;
                // 2930: Auto-delete Current Locks based on new parameter - Start
                // Polling Interval specified in Minutes in the properties file
//                timer.schedule(new RemindTask.PollTask(), 0,pollingInterval*60);
                timer.schedule(new RemindTask.PollTask(), 0,(long)pollingInterval * 1000 * 60);
                // 2930: Auto-delete Current Locks based on new parameter - End
            }catch(java.lang.IllegalStateException IllegalStateEx){
                IllegalStateEx.printStackTrace();
            }catch(java.lang.IllegalArgumentException illegalArgEx){
                illegalArgEx.printStackTrace();
            }
        }
    }
    
    /** Getter for property htLockIds.
     * @return Value of property htLockIds.
     *
     */
    public java.util.Hashtable getHtLockIds() {
        return htLockIds;
    }    
    
    /** Setter for property htLockIds.
     * @param htLockIds New value of property htLockIds.
     *
     */
    public void setHtLockIds(java.util.Hashtable htLockIds) {
        this.htLockIds = htLockIds;
    }
    
    /** The following method has been written to alter the hashtable which 
     *  contains lock ids. If the hashtable is not having any lock ids then the 
     *  following method terminates the timer
     *  @param lockId is the input
     */    
    public void alterLockIdTable(String lockId){
        Hashtable htLockIds = remindTask.getHtLockIds();
        if(htLockIds != null && htLockIds.size() > 0){
            htLockIds.remove(lockId);
            if(htLockIds != null && htLockIds.size() > 0){
                remindTask.setHtLockIds(htLockIds);
            }else{
                timer.cancel();                
            }    
            
        }            
    }    
    
    class PollTask extends TimerTask {
        public void run(){
                try{
//                    Hashtable htLock = mdiForm.getLocking();
//                    Hashtable htTable = new Hashtable();                    
                    int htSizeRem = remindTask.getHtLockIds().size();
                    if(htSizeRem>0){
                        // Calling the polling mechanism                        
                        Hashtable htupdTimestamp = pollingMonitor.updateLockIdTimestamp(remindTask.getHtLockIds());
                        // Checking the status of updation-Begin
                        Hashtable htClLockId = new Hashtable();
                        String keyString = null;
                        Set htSet = htupdTimestamp.keySet();
                        Iterator htItr = htSet.iterator();
                        while(htItr.hasNext()) {
                            keyString = (String)htItr.next();
                            if(!(htupdTimestamp.get(keyString).equals("UPDATION_FAILED"))){
                                if(htClLockId != null) {
                                    htClLockId.put(keyString, htupdTimestamp.get(keyString));
                                }
                            }else{
                                htItr.remove();
                                StringBuffer moduleName = new StringBuffer(keyString);
                                moduleName.delete(0, 4);
                                String strModName = moduleName.substring(0,moduleName.indexOf("_"));
                                String strNumber = moduleName.substring(moduleName.indexOf("_")+1);
                                //Added for COEUSQA-1442 : Have current locks say "unlock" instead of "delete" - start
                                //CoeusOptionPane.showInfoDialog("The lock for "+ strModName +" "+strNumber+" "+"has been deleted by Administrator");
                                CoeusOptionPane.showInfoDialog(strModName +" "+strNumber+" "+"has been unlocked by an Administrator");
                                //Added for COEUSQA-1442 : Have current locks say "unlock" instead of "delete" - end
                                continue;
                            }                        //
                        }
                        int htClSize = htupdTimestamp.size();
                        remindTask.setHtLockIds(htupdTimestamp);
                        if(htClSize == 0 && pollingCheck){                                 
                            timer.cancel();                            
                        }           
                    }
                }catch(Exception ex){
                    ex.printStackTrace();
                }
            }
    }
}
