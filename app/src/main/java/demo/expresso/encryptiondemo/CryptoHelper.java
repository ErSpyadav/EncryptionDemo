package demo.expresso.encryptiondemo;

import android.util.Base64;
import android.util.Log;

import java.io.UnsupportedEncodingException;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

/**
 * Created by Pratik Das on 11/27/16.
 */

public final class CryptoHelper {

    private static final String TAG = CryptoHelper.class.getName();


    private static byte[] getDecodedBytes(final String encodedText) {
        byte[] decodedBytes = null;
        try {
            decodedBytes = Base64.decode(encodedText, Base64.NO_WRAP);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return decodedBytes;
    }

    public static String encrypt(final String strToEncrypt, final String secret, final String encIv) {
        String encResponse = null;
        try {
            //setKey(secret);
            byte[] keyBytes = getDecodedBytes(secret);
            byte[] ivBytes = getDecodedBytes(encIv);

            SecretKeySpec skeySpec = new SecretKeySpec(keyBytes, "AES");
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            IvParameterSpec iv = new IvParameterSpec(ivBytes);
            cipher.init(Cipher.ENCRYPT_MODE, skeySpec, iv);
            //cipher.init(Cipher.ENCRYPT_MODE, skeySpec);


            encResponse = Base64.encodeToString(cipher.doFinal(strToEncrypt.getBytes("UTF-8")), Base64.NO_WRAP);
            Log.i(TAG, "enc " + encResponse);
            String decodeStr = new String(Base64.decode(encResponse, Base64.NO_WRAP));

            Log.i(TAG, "decodestr " + decodeStr);
        } catch (Exception e) {
            Log.e(TAG, "Error while encrypting: " + e.toString());
        }
        return encResponse;
    }

    public static String decrypt(String strToDecrypt, String secret, final String encIv) {
        String dcrResponse;
        try {
            byte[] keyBytes = getDecodedBytes(secret);
            byte[] ivBytes = getDecodedBytes(encIv);

            SecretKeySpec skeySpec = new SecretKeySpec(keyBytes, "AES");
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");

            IvParameterSpec iv = new IvParameterSpec(ivBytes);
           cipher.init(Cipher.DECRYPT_MODE, skeySpec, iv);
           // cipher.init(Cipher.DECRYPT_MODE, skeySpec);
            dcrResponse = new String(cipher.doFinal(Base64.decode(strToDecrypt, Base64.NO_WRAP)));
        } catch (Exception e) {
            Log.e(TAG, "Error while decrypting: " + e.toString());
            return decryptBase64(strToDecrypt);
        }
        return dcrResponse;
    }

    private static String decryptBase64(String strToDecrypt) {
        try {
            return new String(Base64.decode(strToDecrypt.getBytes("UTF-8"), Base64.NO_WRAP), "UTF-8");
        } catch (UnsupportedEncodingException e) {
            // Suppress exception (unlikely to happen in prod)
            Log.e(TAG,"decryptBase64 :: Encoding exception");
            return  null ;
        }
    }

}
