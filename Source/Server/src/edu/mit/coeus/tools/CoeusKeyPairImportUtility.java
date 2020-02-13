/*
 * CoeusPrivateKeyImportUtility.java
 *
 * Created on August 8, 2006, 12:17 PM
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 */


package edu.mit.coeus.tools;

import java.io.*;
import java.security.*;
import java.util.*;
import javax.net.ssl.*;

/**
 *
 * @author  Geo Thomas
 *
 * This is a utility class to import keystore key pairs to another keystore.
 * This is developed to help combining different keystores to one file, which is required 
 * Coeus application to support multi campus in Grants.Gov submissions
 */
public class CoeusKeyPairImportUtility {
    
    /** Creates a new instance of CoeusPrivateKeyImportUtility */
    public CoeusKeyPairImportUtility() {
    }
    
    /**
     * @param args the command line arguments
     * Arguments could be either of the following
     * <li> -help
     * <li> -expKeystore <ExportKeystoreFile> -expStorepass <ExportSrcStorepass> 
           -impKeystore <ImportKeystoreFile> -impStorepass <ImportStorepass> 
           -expAlias <ExportAlias> -expKeypass <ExportKeypass> -impAlias <ImportAlias>
     */
    public static void main(String[] args) {
        CoeusKeyPairImportUtility u = new CoeusKeyPairImportUtility();
        Properties prop = new Properties();
        try{
        if(args[0].equalsIgnoreCase("-help")){
            u.printHelp();
            return;
        }
        int l = args.length;
        for(int i=0;i<l;i++){
            if(args[i].startsWith("-")){
                prop.setProperty(args[i], args[++i]);
            }
        }
        u.imp(prop.getProperty("-expKeystore"), prop.getProperty("-expStorepass"), 
                prop.getProperty("-impKeystore"),prop.getProperty("-impStorepass"), 
                prop.getProperty("-expAlias"), prop.getProperty("-expKeypass"), 
                prop.getProperty("-impAlias"));
        }catch(Exception ex){
            System.out.println("Enetered Values :: ");
            System.out.println(prop.toString());
            System.out.println("");
            u.printHelp();
            System.out.println("");
            ex.printStackTrace();
        }
    }
    
    private void printHelp(){
        System.out.println("ImportUtility Usage:");
        System.out.println("ImportUtility -expKeystore <ExportKeystoreFile> -expStorepass <ExportSrcStorepass> " +
                            "-impKeystore <ImportKeystoreFile> -impStorepass <ImportStorepass> " +
                            "-expAlias <ExportAlias> -expKeypass <ExportKeypass> -impAlias <ImportAlias>");
    }
    
    private void imp(String expPath,String expStorePass,String impPath,
                        String impStorePass,String expAlias,String expKeyPass,String impAlias) 
            throws Exception{
        KeyStore expks = KeyStore.getInstance("JKS");
        expks.load(new FileInputStream(expPath),expStorePass.toCharArray());
        
        KeyStore impks = KeyStore.getInstance("JKS");
        impks.load(new FileInputStream(impPath),impStorePass.toCharArray());
        
        if(expAlias==null){
            Enumeration e = expks.aliases();
            while(e.hasMoreElements()){
                String al = e.nextElement().toString();
                java.security.cert.Certificate cer = expks.getCertificate(al);
                java.security.cert.Certificate cer1 = expks.getCertificate(al);
                Key k = expks.getKey(al, expStorePass.toCharArray());
                java.security.cert.Certificate[] cerArr = {cer};
                impks.setKeyEntry(al,k,impStorePass.toCharArray(), cerArr);
            }
        }else{
            java.security.cert.Certificate cer[] = expks.getCertificateChain(expAlias);
            if(cer==null){
                throw new Exception("No certificate found for "+expAlias);
            }
            Key k = expks.getKey(expAlias, expKeyPass.toCharArray());
            impks.setKeyEntry(impAlias,k,impStorePass.toCharArray(), cer);
        }
        impks.store(new FileOutputStream(impPath), impStorePass.toCharArray());
    }
}
