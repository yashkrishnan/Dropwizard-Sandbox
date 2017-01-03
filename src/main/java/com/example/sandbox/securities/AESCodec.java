package com.example.sandbox.securities;

import com.example.sandbox.configurations.SandboxConfiguration;
import com.example.sandbox.constants.SecurityConstants;
import com.example.sandbox.constants.TextConstants;
import org.apache.commons.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.security.Key;

/**
 * Java util class for encoding and decoding of String data to AES
 */
public class AESCodec {

    //create an object of AESCodec
    private static volatile AESCodec aesCodec;
    private final Logger logger = LoggerFactory.getLogger(getClass().getName());

    //make the constructor private so that this class cannot be instantiated
    private AESCodec() {
    }

    //Get the only object available
    public static AESCodec getInstance() {
        if (aesCodec == null) {
            synchronized (AESCodec.class) {
                // Double check
                if (aesCodec == null) {
                    aesCodec = new AESCodec();
                }
            }
        }
        return aesCodec;
    }

    public String encryptAES(String Data) {
        String encryptedValue;
        try {
            Key key = generateKey();
            Cipher cipher = Cipher.getInstance(SecurityConstants.AES_CODEC_ALGORITHM);
            cipher.init(Cipher.ENCRYPT_MODE, key);
            byte[] encryptedBytes = cipher.doFinal(Data.getBytes());
            encryptedValue = Base64.encodeBase64URLSafeString(encryptedBytes);
        } catch (Exception e) {
            logger.error(TextConstants.LOG_EXCEPTION_MESSAGE, e.getMessage());
            return null;
        }
        return encryptedValue;
    }

    private Key generateKey() {
        byte[] codecKey = SandboxConfiguration.getCodecKeyValue().getBytes();
        String codecAlgorithm = SecurityConstants.AES_CODEC_ALGORITHM;
        return new SecretKeySpec(codecKey, codecAlgorithm);
    }
}