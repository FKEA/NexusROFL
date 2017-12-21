package server.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by Filip on 10-10-2017.
 */
public class Auth {

    private static MessageDigest digester;

    static {
        // Creates an instance of MessageDigest with SHA-256 hashing algorithm
        // This object is used to perform the hashing of passwords
        try {
            digester = MessageDigest.getInstance("SHA-256");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

    }

    /**
     * @param password The password that should be hashed
     * @param salt     The user specific salt
     * @return A SHA-256 hashed value of the password and salt combination
     */
    public static String hashPassword(String password, String salt) {
        return (performHashing(password + salt));
    }

    /**
     * Salt generation algorithm, generates a randomized 6 character salt
     * based on the seed string.
     *
     * @param seed The string that will be used as the seed for the salt
     * @return A 6 character salt
     */
    public static String generateSalt(String seed) {

        // Hash the seed string to generate a 64 char string.
        String hashedString = performHashing(seed);

        // Generate a random number between 1-56, that will be the starting index of the seed.
        int startIndex = (int) (56 * Math.random());

        // Return a 6 char substring of the hashed seed string,
        // starting at the index defined by the random number.
        return hashedString.substring(startIndex, startIndex + 6);

    }

    /**
     * Code taken from:
     * https://github.com/Distribuerede-Systemer-2017/
     * secure-dis/blob/master/src/Utility/Digester.java
     * <p>
     * Performing SHA-256 hashing of string
     *
     * @param str The string that will be hashed
     * @return SHA-256 hash of the inputted string
     */
    private static String performHashing(String str) {
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
