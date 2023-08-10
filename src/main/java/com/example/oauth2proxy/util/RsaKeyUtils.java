package com.example.oauth2proxy.util;

import org.apache.commons.lang3.StringUtils;

import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

public final class RsaKeyUtils {

    private RsaKeyUtils() {

    }

    private static final String PUBLIC_KEY_PRE_ENCAPSULATION_BOUNDARY = "-----BEGIN RSA PUBLIC KEY-----";
    private static final String PUBLIC_KEY_POST_ENCAPSULATION_BOUNDARY = "-----END RSA PUBLIC KEY-----";
    private static final String PRIVATE_KEY_PRE_ENCAPSULATION_BOUNDARY = "-----BEGIN RSA PRIVATE KEY-----";
    private static final String PRIVATE_KEY_POST_ENCAPSULATION_BOUNDARY = "-----END RSA PRIVATE KEY-----";
    private static final String KEY_ALGORITHM = "RSA";
    private static final int KEY_SIZE = 2048;

    /**
     * Generate RSA Key Pair
     *
     * @return
     */
    public static KeyPair generateKeyPair() throws NoSuchAlgorithmException {
        KeyPairGenerator kpg = KeyPairGenerator.getInstance(KEY_ALGORITHM);
        kpg.initialize(KEY_SIZE);
        return kpg.generateKeyPair();
    }

    public static String getBase64KeyText(final Key key) {
        return getBase64KeyText(key, false);
    }

    public static String getBase64KeyText(final Key key, final boolean encapsulationBoundaryIncluded) {
        StringBuilder stringBuilder = new StringBuilder();
        String preEB = null;
        String postEB = null;
        if (key != null) {
            if (encapsulationBoundaryIncluded) {
                if (key instanceof PublicKey) {
                    preEB = PUBLIC_KEY_PRE_ENCAPSULATION_BOUNDARY + "\n";
                    postEB = "\n" + PUBLIC_KEY_POST_ENCAPSULATION_BOUNDARY + "\n";
                } else if (key instanceof PrivateKey) {
                    preEB = PRIVATE_KEY_PRE_ENCAPSULATION_BOUNDARY + "\n";
                    postEB = "\n" + PRIVATE_KEY_POST_ENCAPSULATION_BOUNDARY + "\n";
                }
            }
            stringBuilder.append(StringUtils.defaultString(preEB));
            stringBuilder.append(Base64.getEncoder().encodeToString(key.getEncoded()));
            stringBuilder.append(StringUtils.defaultString(postEB));
        }
        return stringBuilder.toString();
    }

    public static PublicKey parseRsaPublicKey(final String publicKeyContent) {
        final String EMPTY = "";
        String publicKeyText = StringUtils.defaultString(publicKeyContent)
                .replace(PUBLIC_KEY_PRE_ENCAPSULATION_BOUNDARY, EMPTY)
                .replace(PUBLIC_KEY_POST_ENCAPSULATION_BOUNDARY, EMPTY)
                .replace("\r", EMPTY).replace("\n", EMPTY);

        try {
            KeyFactory kf = KeyFactory.getInstance(KEY_ALGORITHM);
            X509EncodedKeySpec keySpecX509 = new X509EncodedKeySpec(Base64.getDecoder().decode(publicKeyText));
            return kf.generatePublic(keySpecX509);
        } catch (NoSuchAlgorithmException noSuchAlgorithmException) {
            throw new RsaKeyParseException("Invalid Algorithm", noSuchAlgorithmException);
        } catch (InvalidKeySpecException invalidKeySpecException) {
            throw new RsaKeyParseException("Invalid Key Specification", invalidKeySpecException);
        }
    }

    public static PrivateKey parseRsaPrivateKey(final String privateKeyContent) {
        final String EMPTY = "";
        String privateKeyText = StringUtils.defaultString(privateKeyContent)
                .replace(PRIVATE_KEY_PRE_ENCAPSULATION_BOUNDARY, EMPTY)
                .replace(PRIVATE_KEY_POST_ENCAPSULATION_BOUNDARY, EMPTY)
                .replace("\r", EMPTY).replace("\n", EMPTY);

        try {
            KeyFactory kf = KeyFactory.getInstance(KEY_ALGORITHM);
            PKCS8EncodedKeySpec keySpecPKCS8 = new PKCS8EncodedKeySpec(Base64.getDecoder().decode(privateKeyText));
            return kf.generatePrivate(keySpecPKCS8);
        } catch (NoSuchAlgorithmException noSuchAlgorithmException) {
            throw new RsaKeyParseException("Invalid Algorithm", noSuchAlgorithmException);
        } catch (InvalidKeySpecException invalidKeySpecException) {
            throw new RsaKeyParseException("Invalid Key Specification", invalidKeySpecException);
        }
    }

    public static class RsaKeyParseException extends RuntimeException {

        public RsaKeyParseException(String message, Throwable cause) {
            super(message, cause);
        }
    }
}