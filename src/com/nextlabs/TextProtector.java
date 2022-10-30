package com.nextlabs;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESedeKeySpec;

import com.sun.org.apache.xml.internal.security.exceptions.Base64DecodingException;
import com.sun.org.apache.xml.internal.security.utils.Base64;


public class TextProtector {
	
	private String coder(String text, byte[] key, boolean encrypt) 
	{
        String retValue = "";

        String KEY_ALGORITHM = "DESede";
        String CIPHER_ALGORITHM = "DESede/ECB/PKCS5Padding";

        byte[] data = null;
        try 
        {
            DESedeKeySpec desKeySpec = new DESedeKeySpec(key);

            SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(KEY_ALGORITHM);

            SecretKey secretKey = keyFactory.generateSecret(desKeySpec);

            Cipher cipher = Cipher.getInstance(CIPHER_ALGORITHM);

            byte[] input = null;
            if (encrypt) 
            {
                cipher.init(Cipher.ENCRYPT_MODE, secretKey);
                input = text.getBytes();
            } 
            else 
            {
                cipher.init(Cipher.DECRYPT_MODE, secretKey);
                input = Base64.decode(text);
            }

            data = cipher.doFinal(input);
        } catch (InvalidKeyException e) {
            System.out.println(e.getMessage());
        } catch (NoSuchAlgorithmException e) {
            System.out.println(e.getMessage());
        } catch (NoSuchPaddingException e) {
            System.out.println(e.getMessage());
        } catch (Base64DecodingException e) {
            System.out.println(e.getMessage());
        } catch (InvalidKeySpecException e) {
            System.out.println(e.getMessage());
        } catch (IllegalBlockSizeException e) {
            System.out.println(e.getMessage());
        } catch (BadPaddingException e) {
            System.out.println(e.getMessage());
        }

        if (data == null)
        {
            retValue = text;
        } 
        else 
        {
            if (encrypt)
            	retValue = com.sun.org.apache.xml.internal.security.utils.Base64.encode(data);
            else
            	retValue = new String(data);
        }

        return retValue;
    }

	public String encryptText(String text) {

        String keyString = "I2d1eXc3OF4qS0dIS081NjQzJURrajJJOSpeJTIw";

        //String cleanKey="#guyw78^*KGHKO5643%Dkj2I9*^%20";

        byte[] keyBytes = null;

        try
        {
        	keyBytes = Base64.decode(keyString);
           
        }
        catch (Base64DecodingException e) 
        {
            e.printStackTrace();
        }

        return coder(text, keyBytes, true);
    }

    public String decryptText(String cipherText)
    {

        String keyString = "I2d1eXc3OF4qS0dIS081NjQzJURrajJJOSpeJTIw";

        byte[] keyBytes = null;

        try 
        {
            keyBytes = Base64.decode(keyString);
        } 
        catch (Base64DecodingException e)
        {
            e.printStackTrace();
        }

        return coder(cipherText, keyBytes, false);
    }
}
