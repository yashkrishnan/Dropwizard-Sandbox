package com.example.sandbox.securities;

import com.example.sandbox.constants.SecurityConstants;
import com.example.sandbox.constants.TextConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.math.BigInteger;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;

public class PBKDF2Hash {

    //create an object of PBKDF2Hash
    private static volatile PBKDF2Hash pbkdf2Hash;
    private final Logger logger = LoggerFactory.getLogger(PBKDF2Hash.class);

    //make the constructor private so that this class cannot be instantiated
    private PBKDF2Hash() {

    }

    //Get the only object available
    public static PBKDF2Hash getInstance() {
        if (pbkdf2Hash == null) {
            synchronized (PBKDF2Hash.class) {
                // Double check
                if (pbkdf2Hash == null) {
                    pbkdf2Hash = new PBKDF2Hash();
                }
            }
        }
        return pbkdf2Hash;
    }

    public String generateStrongPasswordHash(String password) {
        int iterations = SecurityConstants.ALGORITHM_ITERATIONS;
        char[] passwordChars = password.toCharArray();
        byte[] salt = getSalt();
        PBEKeySpec spec = new PBEKeySpec(passwordChars, salt, iterations, 64 * 8);
        SecretKeyFactory secretKeyFactory;
        byte[] hash = SecurityConstants.BYTES;
        try {
            secretKeyFactory = SecretKeyFactory.getInstance(SecurityConstants.PBKDF2_WITH_HMAC_SHA1_CODEC_ALGORITHM);
            hash = secretKeyFactory.generateSecret(spec).getEncoded();
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            logger.error(TextConstants.LOG_EXCEPTION_MESSAGE, e.getMessage());
            logger.error(TextConstants.LOG_EXCEPTION, e.toString(), e);
        }
        String hexSalt = toHex(salt);
        String hexHash = toHex(hash);
        //noinspection UnnecessaryLocalVariable
        String strongPasswordHash = hexSalt + TextConstants.STRING_FULL_COLUMN + hexHash;
        return strongPasswordHash;
    }

    public byte[] getSalt() {
        SecureRandom secureRandom = null;
        try {
            secureRandom = SecureRandom.getInstance(SecurityConstants.SHA1_PRNG_CODEC_ALGORITHM);
        } catch (NoSuchAlgorithmException e) {
            logger.error(TextConstants.LOG_EXCEPTION_MESSAGE, e.getMessage());
            logger.error(TextConstants.LOG_EXCEPTION, e.toString(), e);
        }
        byte[] salt = new byte[16];
        assert secureRandom != null;
        secureRandom.nextBytes(salt);
        return salt;
    }

    private String toHex(byte[] array) {
        BigInteger bigInteger = new BigInteger(1, array);
        String hex = bigInteger.toString(16);
        int paddingLength = (array.length * 2) - hex.length();
        if (paddingLength > 0) {
            return String.format(SecurityConstants.PERCENTAGE_ZERO + paddingLength + SecurityConstants.D, 0) + hex;
        } else {
            return hex;
        }
    }

    public boolean validatePassword(String originalPassword, String storedPassword) {
        String[] storedPasswordParts = storedPassword.split(TextConstants.STRING_FULL_COLUMN);
        int iterations = SecurityConstants.ALGORITHM_ITERATIONS;
        byte[] salt = fromHex(storedPasswordParts[0]);
        byte[] hash = fromHex(storedPasswordParts[1]);
        char[] originalPasswordChars = originalPassword.toCharArray();

        PBEKeySpec spec = new PBEKeySpec(originalPasswordChars, salt, iterations, hash.length * 8);
        SecretKeyFactory secretKeyFactory;
        byte[] testHash = SecurityConstants.BYTES;
        try {
            secretKeyFactory = SecretKeyFactory.getInstance(SecurityConstants.PBKDF2_WITH_HMAC_SHA1_CODEC_ALGORITHM);
            testHash = secretKeyFactory.generateSecret(spec).getEncoded();
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            logger.error(TextConstants.LOG_EXCEPTION_MESSAGE, e.getMessage());
            logger.error(TextConstants.LOG_EXCEPTION, e.toString(), e);
        }
        int diff = hash.length ^ testHash.length;
        for (int i = 0; i < hash.length && i < testHash.length; i++) {
            diff |= hash[i] ^ testHash[i];
        }
        //noinspection UnnecessaryLocalVariable
        boolean matched = (diff == 0);
        return matched;
    }

    private byte[] fromHex(String hex) {
        byte[] bytes = new byte[hex.length() / 2];
        for (int i = 0; i < bytes.length; i++) {
            bytes[i] = (byte) Integer.parseInt(hex.substring(2 * i, 2 * i + 2), 16);
        }
        return bytes;
    }
}