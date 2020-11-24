package com.company;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.security.GeneralSecurityException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

public class AES {
    private static byte[] key;



    public static byte[] encrypt(String key, String value,String mode)
            throws GeneralSecurityException {

        byte[] raw = key.getBytes(Charset.forName("UTF-8"));
        if (raw.length != 16) {
            throw new IllegalArgumentException("Invalid key size.");
        }

        SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
        Cipher cipher;
        if (mode.equals("CTR")) {
            cipher = Cipher.getInstance("AES/" + mode + "/NoPadding");
            cipher.init(Cipher.ENCRYPT_MODE, skeySpec,
                    new IvParameterSpec(new byte[16]));
        } else if(mode.equals("ECB")){

            cipher = Cipher.getInstance("AES/"+mode+ "/PKCS5Padding");
            cipher.init(Cipher.ENCRYPT_MODE, skeySpec);
        } else{
            cipher = Cipher.getInstance("AES/" + mode + "/PKCS5Padding");
            cipher.init(Cipher.ENCRYPT_MODE, skeySpec,
                    new IvParameterSpec(new byte[16]));
        }

        return cipher.doFinal(value.getBytes(Charset.forName("UTF-8")));
    }

    public static String decrypt(String key, byte[] encrypted, String mode)
            throws GeneralSecurityException {

        byte[] raw = key.getBytes(Charset.forName("UTF-8"));
        if (raw.length != 16) {
            throw new IllegalArgumentException("Invalid key size.");
        }
        SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
        Cipher cipher;
        if (mode.equals("CTR")) {
            cipher = Cipher.getInstance("AES/" + mode + "/NoPadding");
            cipher.init(Cipher.DECRYPT_MODE, skeySpec,
                    new IvParameterSpec(new byte[16]));
        } else if(mode.equals("ECB")){

            cipher = Cipher.getInstance("AES/"+mode+ "/PKCS5Padding");
            cipher.init(Cipher.DECRYPT_MODE, skeySpec);
        } else{
            cipher = Cipher.getInstance("AES/" + mode + "/PKCS5Padding");
            cipher.init(Cipher.DECRYPT_MODE, skeySpec,
                    new IvParameterSpec(new byte[16]));
        }

        byte[] original = cipher.doFinal(encrypted);

        return new String(original, Charset.forName("UTF-8"));
    }
}
