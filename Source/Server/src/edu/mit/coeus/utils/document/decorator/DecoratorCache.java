/*
 * DecoratorCache.java
 *
 * Created on April 23, 2008, 11:55 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package edu.mit.coeus.utils.document.decorator;

import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author sharathk
 */
public class DecoratorCache {
    
    private static DecoratorCache decoratorCache;
    private static Map mapCache;
    
    /** Creates a new instance of DecoratorCache */
    private DecoratorCache() {
    }
    
    public synchronized static DecoratorCache getInstance() {
        if(decoratorCache == null) {
            decoratorCache = new DecoratorCache();
        }
        return decoratorCache;
    }
    
    /*public synchronized void cacheDecoration(int status, DecoratorBean decoratorBean) {
        if(mapCache == null) {
            mapCache = new HashMap();
        }
        mapCache.put(new Integer(status), decoratorBean);
    }*/
    
    public synchronized void cacheDecoration(String group, int status, DecoratorBean decoratorBean) {
        if(mapCache == null) {
            mapCache = new HashMap();
        }
        Map map;
        if(mapCache.containsKey(group)){
            map = (HashMap)mapCache.get(group);
        }else {
            map = new HashMap();
            mapCache.put(group, map);
        }
        map.put(new Integer(status), decoratorBean);
    }
    
    /*public synchronized DecoratorBean findDecoration(int status)throws Exception{
        if(mapCache == null) return null;
        DecoratorBean decoratorBean = (DecoratorBean)mapCache.get(new Integer(status));
        return decoratorBean;
    }*/

    public synchronized DecoratorBean findDecoration(String group, int status)throws Exception{
        if(mapCache == null) return null;
        Map map = (Map)mapCache.get(group);
        if(map == null) return null;
        DecoratorBean decoratorBean = (DecoratorBean)map.get(new Integer(status));
        return decoratorBean;
    }
    
}
