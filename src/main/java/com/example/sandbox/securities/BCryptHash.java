package com.example.sandbox.securities;

import com.example.sandbox.constants.SecurityConstants;
import org.mindrot.jbcrypt.BCrypt;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

/**
 * sandbox-dropwizard - com.example.sandbox.securities
 * Created by Yashwanth on 4/1/17 1:00 PM.
 */
public class BCryptHash {

    public static void main(String[] args) {
        BCryptHash bCryptHash = new BCryptHash();
        String content = "asd";
        long time = System.currentTimeMillis();
        String hash = bCryptHash.generateStrongHash(content);
        time = time - System.currentTimeMillis();
        System.out.println(time + " : " + hash);
        time = System.currentTimeMillis();
        boolean match = bCryptHash.validateStrongHash(content, hash);
        time = time - System.currentTimeMillis();
        System.out.println(time + " : " + match);
    }

    SecureRandom secureRandom;

    public BCryptHash() {
        try {
            this.secureRandom = SecureRandom.getInstance(SecurityConstants.SHA1_PRNG_CODEC_ALGORITHM);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            secureRandom = null;
        }
    }

    public String generateStrongHash(String content) {
        int iterations = 12;
        String salt = getSalt(iterations);
        String strongHash = BCrypt.hashpw(content, salt);
        return strongHash;
    }

    public boolean validateStrongHash(String content, String hash) {
        boolean contentMatch = BCrypt.checkpw(content, hash);
        return contentMatch;
    }

    private String getSalt(int iterations) {
        String salt;
        if (secureRandom != null) {
            salt = BCrypt.gensalt(iterations, secureRandom);
        } else {
            salt = BCrypt.gensalt(iterations);
        }
        return salt;
    }
}
