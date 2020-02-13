// File: src\jsbook\ch3\SymmetricCipherTest.java
package edu.mit.coeus.utils.security;

import edu.mit.coeus.brokers.RequesterBean;
import edu.mit.coeus.utils.UtilFactory;
import java.security.SecureRandom;
import java.util.*;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.Cipher;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESedeKeySpec;
import java.security.spec.KeySpec;
import org.apache.xml.security.utils.Base64;

public class CoeusCipher {
    private static final String provider = "SunJCE";
    public static final String SERVLET_SECURE_SEED = "SERVLET_SECURE_SEED";
//    public static final String xform ="TripleDES/OFB/PKCS5Padding";
//    public static final String xform ="DESede";
    public static final String xform ="DESede/CBC/PKCS5Padding";
//    public static final String algorithm = "TripleDES";//"TripleDES";
    public static final String algorithm = "DESede";
    private static CoeusCipher instance;
    private static byte[] dataBytes;
    
    private static final byte[] ivBytes =
            {0x00, 0x01, 0x02, 0x03, 0x04, 0x05, 0x06, 0x07};

    
    public static CoeusCipher getInstance() throws Exception{
        if(instance==null)
            instance =  new CoeusCipher();
        return instance;
    }
    private CoeusCipher() throws Exception{
    }
    public synchronized static byte[] getRandomSecureSeed(){
//        byte[] randBytes = new byte[24];
//        Random random = new Random();
//        random.nextBytes(randBytes);
//        return randBytes;
        char[] c1 = new char[24];
        Random r = new Random();
        makeWord(c1,r);
        return new String(c1).getBytes();

    }
    
    public synchronized static String getEncriptedKey(String userId,String seed) 
                                                    throws Exception {
        /*
         *Do not Use SunJCE as JCE provider. Use the default
         *by Geo on 03/29/07
         */
        //BEGIN
        Cipher cipher = Cipher.getInstance(xform);
//        Cipher cipher = Cipher.getInstance(xform,provider);
        //END
//        KeySpec keySpec = new DESedeKeySpec(seed.getBytes());
        KeySpec keySpec = new DESedeKeySpec(seed.getBytes("ASCII"));
        SecretKeyFactory secKeyFact = SecretKeyFactory.getInstance(algorithm);
        SecretKey secKey = secKeyFact.generateSecret(keySpec);
        IvParameterSpec iv = new IvParameterSpec(ivBytes);
        cipher.init(Cipher.ENCRYPT_MODE, secKey,iv);
        byte[] encKeyBytes = cipher.doFinal(userId.toUpperCase().getBytes());
        String encAuthKey = Base64.encode(encKeyBytes);
        return encAuthKey;
    }
    public synchronized static boolean isValidRquest(String userId,String encReqKey,String contextSeed) 
                    throws Exception{
        /*
         *Do not Use SunJCE as JCE provider. Use the default
         *by Geo on 03/29/07
         */
        //BEGIN
        Cipher cipher = Cipher.getInstance(xform);
//        Cipher cipher = Cipher.getInstance(xform,provider);
        //END
//        KeySpec keySpec = new DESedeKeySpec(contextSeed.getBytes());
        KeySpec keySpec = new DESedeKeySpec(contextSeed.getBytes("ASCII"));
        SecretKeyFactory secKeyFact = SecretKeyFactory.getInstance(algorithm);
        SecretKey secKey = secKeyFact.generateSecret(keySpec);
        IvParameterSpec iv = new IvParameterSpec(ivBytes);
        cipher.init(Cipher.DECRYPT_MODE, secKey,iv);
        byte[] decKeyBytes = cipher.doFinal(encReqKey.getBytes());
        return java.util.Arrays.equals(userId.toUpperCase().getBytes(), decKeyBytes);
    }
    public synchronized static boolean isValidRquest(RequesterBean requester,String contextSeed) 
                                                throws Exception{
        String userId = requester.getUserName();
        String encReqKey = requester.getAuthKey();
        /*
         *Do not Use SunJCE as JCE provider. Use the default
         *by Geo on 03/29/07
         */
        //BEGIN
        Cipher cipher = Cipher.getInstance(xform);
//        Cipher cipher = Cipher.getInstance(xform,provider);
        //END
//        KeySpec keySpec = new DESedeKeySpec(contextSeed.getBytes());
        KeySpec keySpec = new DESedeKeySpec(contextSeed.getBytes("ASCII"));
        SecretKeyFactory secKeyFact = SecretKeyFactory.getInstance(algorithm);
        SecretKey secKey = secKeyFact.generateSecret(keySpec);
        IvParameterSpec iv = new IvParameterSpec(ivBytes);
        cipher.init(Cipher.ENCRYPT_MODE, secKey,iv);
//        cipher.init(Cipher.DECRYPT_MODE, secKey,iv);
//        cipher.init(Cipher.DECRYPT_MODE, secKey);
//        byte[] decKeyBytes = cipher.doFinal(encReqKey.getBytes());
//        return java.util.Arrays.equals(userId.toUpperCase().getBytes(), decKeyBytes);
        byte[] decKeyBytes = cipher.doFinal(userId.toUpperCase().getBytes());
        return Base64.encode(decKeyBytes).equals(encReqKey);
//        return java.util.Arrays.equals(encReqKey.getBytes(), decKeyBytes);
    }
    
    public static void main(String[] unused) throws Exception {
        CoeusCipher c = CoeusCipher.getInstance();
//        // Generate a secret key
//        KeyGenerator kg = KeyGenerator.getInstance(algorithm);
//        SecureRandom r = new SecureRandom();
//        kg.init(r); // 56 is the keysize. Fixed for DES
//        
//        SecretKey key = kg.generateKey();
        
//        System.out.println("dataBytes"+dataBytes);
//        byte[] encBytes = c.encrypt(dataBytes);
//        System.out.println("encBytes"+encBytes);
//        byte[] decBytes = c.decrypt(encBytes);
//        System.out.println(new String(decBytes));
//        boolean expected = java.util.Arrays.equals(dataBytes, decBytes);
//        boolean expected = c.isValidRquest(c.getEncriptedKey());
//        System.out.println("Test " + (expected ? "SUCCEEDED!" : "FAILED!"));

//
        SecureSeedTxnBean s = new SecureSeedTxnBean();
        String seed = s.getServerSecureSeedValue(SERVLET_SECURE_SEED);
        System.out.println("seed=>"+seed);
        String encKey = c.getEncriptedKey("geot", seed);
        boolean expected = c.isValidRquest("geot",encKey,seed);
        System.out.println("Test " + (expected ? "SUCCEEDED!" : "FAILED!"));
        
//                Random r = new Random();
//                char[] c1 = new char[24];
//                System.out.println(c.makeWord(c1,r));
        
    }
    
    
    private synchronized static  char randomChar(Random r) { return (char)(r.nextInt('Z'-'A'+1)+'A'); }

    private synchronized static char[] makeWord(char[] w, Random r) {
       for (int j= w.length; j-- >0; w[j]= randomChar(r));
       return w;
    } 
}
