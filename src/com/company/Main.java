package com.company;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Main {


    public static void main(String[] args) {
        final String secretKey ="ThisIsASecretKey"; //must be 16 chars
        String [] files={
                "E:\\Program Files\\Java\\30MB.txt",
                "E:\\Program Files\\Java\\100MB.txt",
                "E:\\Program Files\\Java\\250MB.txt"  };
        String saveFilesToPath="E:\\Program Files\\Java\\";

        //ECB, CBC, OFB, CFB, i CTR
        String [] fileContents= new String[3];
        String [] modes = {"ECB","OFB","CBC","CFB","CTR"};
        long startTime=0;
        long endTime=0;


        //iterate through files
        for(int i=0;i<files.length;i++){
            //read file contents
            try{
                File myObj = new File(files[i]);
                Scanner myReader = new Scanner(myObj);
                String data="";
                while (myReader.hasNextLine()) {
                    data += myReader.nextLine()+"\n";
                }
                myReader.close();
                fileContents[i]=data;
                for(String m:modes){
                    startTime = System.nanoTime();
                    byte[] encrypted = AES.encrypt( secretKey,fileContents[i],m);
                    encrypted[5]=17;
                    endTime = System.nanoTime();
                    System.out.println(m.toString()+" Szyfrowanie pliku "+Integer.toString(i)+": "+Long.toString((endTime - startTime)/1000000)+"ms");
                    startTime = System.nanoTime();
                    String decrypted = AES.decrypt(secretKey,encrypted,m);
                    endTime = System.nanoTime();
                    System.out.println(m.toString()+" Deszyfrowanie pliku "+Integer.toString(i)+": "+Long.toString((endTime - startTime)/1000000)+"ms");
                    saveToFile(decrypted, saveFilesToPath+ i + "_" + m + "_d.txt");
                    Files.write(Paths.get(saveFilesToPath+ i + "_" + m + "_e.txt"),encrypted);
                }

                System.out.println();

            } catch (FileNotFoundException e) {
                System.out.println("An error occurred.");
                e.printStackTrace();
            } catch (Exception ex) { System.out.println(ex.toString());
            }


        }
    }

    private static String decrypt_data(byte[] encData)
            throws NoSuchAlgorithmException, NoSuchPaddingException,
            InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
        String key = "bad8deadcafef00d";
        SecretKeySpec skeySpec = new SecretKeySpec(key.getBytes(), "AES");
        Cipher cipher = Cipher.getInstance("AES");

        cipher.init(Cipher.DECRYPT_MODE, skeySpec);

        byte[] original = cipher
                .doFinal(encData);
        return new String(original).trim();
    }

    private static byte[] encrypt_data(String data)
            throws Exception {
        String key = "bad8deadcafef00d";
        SecretKeySpec skeySpec = new SecretKeySpec(key.getBytes(), "AES");
        Cipher cipher = Cipher.getInstance("AES");

        cipher.init(Cipher.ENCRYPT_MODE, skeySpec);


        byte[] res = cipher.doFinal(data.getBytes());
        return res;
    }


    public static void saveToFile(String data, String filename) {
        try (
                FileWriter fw = new FileWriter(filename)) {
            fw.write(data);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
