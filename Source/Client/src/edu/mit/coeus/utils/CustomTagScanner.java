/** Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 */

/**@ author NadhGj
 */

package edu.mit.coeus.utils;

  import java.io.*;
  import java.util.*;
  import edu.mit.coeus.utils.CoeusVector;

 public class CustomTagScanner {  
     
     private String fileContents = "";
     private File file;
     private  String strStartDelim = null;
     private  String strEndDelim = null;
     private String readtext = "";
     private Vector vecComments = new Vector();
     private ByteArrayInputStream byteArrayInputStream = null;
     private ByteArrayOutputStream temp = null;
     private byte[] dataArray;
     
     public CustomTagScanner() {
       
     }


     public CoeusVector stringScan(byte[] data,String strStartTag,String strEndTag) {
       strStartDelim = strStartTag;
       strEndDelim = strEndTag;
       byteArrayInputStream = new ByteArrayInputStream(data);
       dataArray = data;
       DataInputStream dataInputStream = null;
       CoeusVector resultVector = new CoeusVector();
       int endDelim = 0; int startDelim = 0; // tracking char count
       StringBuffer vecText = new StringBuffer();
       try {
           dataInputStream = new DataInputStream(byteArrayInputStream);
           StringBuffer sb = new StringBuffer();
           while( (readtext = dataInputStream.readLine()) != null) {
              vecText.append(readtext);
              vecText.append('\n');
              startDelim = vecText.indexOf(strStartDelim);
               endDelim = vecText.indexOf(strEndDelim, strEndDelim.length()+startDelim);
               if(endDelim == -1) {
                  continue;
               }else {
                   String str = vecText.substring(startDelim+strEndDelim.length(), endDelim);
//                   vecText.delete(startDelim, startDelim+strEndDelim.length());
//                   vecText.delete(endDelim-strEndDelim.length(), endDelim);
                   fileContents = fileContents + vecText;
                   sb.append(vecText);
                   resultVector.add(str);
                   vecText.delete(0,vecText.length());
               }
               
           }
           fileContents = fileContents + vecText;
           
      }catch (IOException ioException) {
           System.out.println(ioException.getMessage());
       }finally {
           
                    
           readtext = null;
           vecText = null;
       }
       return resultVector;
   }
   
     
     public byte[] replaceContents(Hashtable ht) {
         try {
             String repText = new String();
             Enumeration enumeration = ht.keys();
             while(enumeration.hasMoreElements()) {
                 String element = (String)enumeration.nextElement();
                 String expression = strStartDelim+element+strEndDelim;
                 fileContents = fileContents.replaceAll(expression,(String) ht.get(element));
             }
             return fileContents.getBytes();
         }catch(Exception ex) {
             ex.printStackTrace();
             return null;
         }
     }
  } // end class
   
  
   
