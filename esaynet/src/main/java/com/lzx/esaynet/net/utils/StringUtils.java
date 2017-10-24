package com.lzx.esaynet.net.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by lizhe on 2017/10/10.
 */

public class StringUtils {
    public static String toHex(String content){
        String hex = null;
        try {
            MessageDigest mDigest = MessageDigest.getInstance("MD5");
            mDigest.update(content.getBytes());
            hex = byteToHex(mDigest.digest());
        } catch (NoSuchAlgorithmException e) {
            hex = String.valueOf(content.hashCode());
        }
        return hex;
    }

    private static String byteToHex(byte[] digest) {
        StringBuilder mBuilder = new StringBuilder();
        for (int i = 0; i < digest.length; i++){
            String hex = Integer.toHexString(0xFF & digest[i]);
            if (hex.length() == 1){
                mBuilder.append('0');
            }
            mBuilder.append(hex);
        }
        return mBuilder.toString();
    }
}
