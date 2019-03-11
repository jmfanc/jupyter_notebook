package cn.infinibase.blackmulti.util;

import org.apache.commons.codec.binary.Hex;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @version V1.0
 * @desc AES
 */
public class AESUtil {
    private static final String KEY_ALGORITHM = "AES";
    private static final String DEFAULT_CIPHER_ALGORITHM = "AES/ECB/PKCS5Padding";

    public static String encrypt(String content, String password) {
        return encryptHex(content, password);
        // return encryptAES(content,password);
    }

    public static String decrypt(String content, String password) {
        return decryptHex(content, password);
        // return decryptAES(content,password);
    }

    /**
     * AES encrypt
     *
     * @param content
     * @param password
     * @return encrypted data with hex encoded
     */
    public static String encryptAES(String content, String password) {
        try {
            Cipher cipher = Cipher.getInstance(DEFAULT_CIPHER_ALGORITHM);
            byte[] byteContent = content.getBytes("utf-8");
            cipher.init(Cipher.ENCRYPT_MODE, getSecretKey(password));

            byte[] result = cipher.doFinal(byteContent);
            return Hex.encodeHexString(result);
        } catch (Exception ex) {
            Logger.getLogger(AESUtil.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    /**
     * AES decrypt
     *
     * @param content
     * @param password
     * @return
     */
    public static String decryptAES(String content, String password) {
        try {
            Cipher cipher = Cipher.getInstance(DEFAULT_CIPHER_ALGORITHM);
            cipher.init(Cipher.DECRYPT_MODE, getSecretKey(password));

            byte[] result = cipher.doFinal(Hex.decodeHex(content.toCharArray()));
            return new String(result, "utf-8");
        } catch (Exception ex) {
            Logger.getLogger(AESUtil.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    /**
     * generate key
     *
     * @return
     */
    private static SecretKeySpec getSecretKey(final String password) {
        KeyGenerator kg = null;
        try {
            kg = KeyGenerator.getInstance(KEY_ALGORITHM);
            SecureRandom secureRandom = SecureRandom.getInstance("SHA1PRNG");
            secureRandom.setSeed(password.getBytes());
            kg.init(128, secureRandom);
            SecretKey secretKey = kg.generateKey();
            return new SecretKeySpec(secretKey.getEncoded(), KEY_ALGORITHM); // convert to AES key
        } catch (NoSuchAlgorithmException ex) {
            Logger.getLogger(AESUtil.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    /**
     * hex encrypt
     *
     * @param content
     * @return encrypted data with hex encoded
     */
    public static String encryptHex(String content, String password) {
        try {
            byte[] byteContent = content.getBytes("utf-8");
            return Hex.encodeHexString(byteContent);
        } catch (Exception ex) {
            Logger.getLogger(AESUtil.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    /**
     * hex decrypt
     *
     * @param content
     * @return
     */
    public static String decryptHex(String content, String password) {
        try {
            byte[] result = Hex.decodeHex(content.toCharArray());
            return new String(result, "utf-8");
        } catch (Exception ex) {
            Logger.getLogger(AESUtil.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }


    public static void main(String[] args) {
        String s = "hello";
        System.out.println("s:" + s);

        String s1 = AESUtil.encrypt(s, "1234");
        System.out.println("s1:" + s1);

        System.out.println("s2:" + AESUtil.decrypt(s1, "1234"));
    }
}