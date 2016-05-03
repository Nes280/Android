package com.example.niels.Code;

import android.util.Log;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import static android.util.Base64.encodeToString;

/**
 * Created by Elsa on 19/04/2016.
 */
public class Hashage {

    private String SHAHash;
    private String MD5Hash;
    public static int NO_OPTIONS=0;

    public Hashage(){}

    //Convertisseur
    private static String convertToHex(byte[] data) throws java.io.IOException
    {

        StringBuffer sb = new StringBuffer();
        String hex=null;

        hex= encodeToString(data, 0, data.length, NO_OPTIONS);

        sb.append(hex);

        return sb.toString();
    }


    //Cryptage en SHA 1
    public String computeSHAHash(String password) {
        MessageDigest mdSha1 = null;
        try {
            mdSha1 = MessageDigest.getInstance("SHA-1");
        } catch (NoSuchAlgorithmException e1) {
            Log.e("myapp", "Error initializing SHA1 message digest");
        }
        try {
            mdSha1.update(password.getBytes("ASCII"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        byte[] data = mdSha1.digest();
        try {
            SHAHash = convertToHex(data);
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        return SHAHash;
    }

    //MD5
    public String computeMD5Hash(String password)
    {
        MessageDigest digest = null;
        try {
            // Create MD5 Hash
            digest = MessageDigest.getInstance("MD5");
            digest.update(password.getBytes());
            byte messageDigest[] = digest.digest();

            StringBuffer MD5Hash = new StringBuffer();
            for (int i = 0; i < messageDigest.length; i++)
            {
                String h = Integer.toHexString(0xFF & messageDigest[i]);
                while (h.length() < 2)
                    h = "0" + h;
                MD5Hash.append(h);
            }

        }
        catch (NoSuchAlgorithmException e)
        {
            e.printStackTrace();
        }
        return MD5Hash.toString();
    }
}
