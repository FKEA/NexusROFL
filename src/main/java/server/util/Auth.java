package server.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by Filip on 10-10-2017.
 */
public class Auth {

    private static MessageDigest digester;

    static {

        // Creates am instance of the MessageDigest with SHA-256 hashing algorithm
        try {
            digester = MessageDigest.getInstance("SHA-256");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

    }

    /**
     * @param password The password that should be hashed
     * @param salt The user salt
     * @return A SHA-256 hashed value of the password and salt combination
     */
    public static String hashPassword(String password, String salt) {
        return (performHashing(password + salt));
    }

    /**
     * Salt generation algorithm, generates a randomized 6 character salt
     *      based on the password parameter.
     *
     * @param password The password that will be used as the base for the salt
     * @return A 6 character salt
     */
    public static String generateSalt(String password) {

        String hashedString = performHashing(password);

        int startIndex = (int) (56*Math.random());

        return hashedString.substring(startIndex, startIndex+6);

    }


    /**
     * Code taken from:
     * https://github.com/Distribuerede-Systemer-2017/
     *          secure-dis/blob/master/src/Utility/Digester.java
     *
     * Performing MD5 hashing of string
     * @param str input
     * @return MD5 hash of string
     */
    private static String performHashing(String str){
        digester.update(str.getBytes());
        byte[] hash = digester.digest();
        StringBuilder hexString = new StringBuilder();
        for (byte aHash : hash) {
            if ((0xff & aHash) < 0x10) {
                hexString.append("0" + Integer.toHexString((0xFF & aHash)));
            } else {
                hexString.append(Integer.toHexString(0xFF & aHash));
            }
        }
        return hexString.toString();
    }

}
