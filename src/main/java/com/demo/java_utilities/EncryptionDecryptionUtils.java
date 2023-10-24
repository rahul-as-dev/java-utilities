package com.demo.java_utilities;

import java.security.*;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.Base64;
import javax.crypto.*;
import javax.crypto.*;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import javax.crypto.spec.SecretKeySpec;

public class EncryptionDecryptionUtils {

    // AES encryption constants
    private static final String AES_ALGORITHM = "AES";
    private static final String AES_CIPHER_ALGORITHM = "AES/CBC/PKCS5Padding";

    // DES encryption constants
    private static final String DES_ALGORITHM = "DES";
    private static final String DES_CIPHER_ALGORITHM = "DES/CBC/PKCS5Padding";

    // RSA encryption constants
    private static final String RSA_ALGORITHM = "RSA";
    private static final int RSA_KEY_SIZE = 2048; // in bits

    // HMAC constants
    private static final String HMAC_ALGORITHM = "HmacSHA256";

    /**
     * Generates a random AES encryption key of the specified key size.
     *
     * @param keySize the size of the AES key in bits
     * @return AES encryption key as a byte array
     * @throws NoSuchAlgorithmException if AES algorithm is not available
     */
    public static byte[] generateAESKey(int keySize)
        throws NoSuchAlgorithmException {
        KeyGenerator keyGenerator = KeyGenerator.getInstance(AES_ALGORITHM);
        keyGenerator.init(keySize);
        SecretKey secretKey = keyGenerator.generateKey();
        return secretKey.getEncoded();
    }

    /**
     * Generates a random initialization vector (IV) for AES encryption.
     *
     * @return AES initialization vector as a byte array
     * @throws NoSuchAlgorithmException if AES algorithm is not available
     */
    public static byte[] generateIV() throws NoSuchAlgorithmException {
        SecureRandom secureRandom = new SecureRandom();
        byte[] iv = new byte[16]; // IV should be 16 bytes for AES
        secureRandom.nextBytes(iv);
        return iv;
    }

    /**
     * Encrypts plaintext using AES encryption with CBC mode and PKCS5Padding.
     *
     * @param plaintext the text to encrypt
     * @param key       the AES encryption key as a byte array
     * @param iv        the initialization vector (IV) as a byte array
     * @return base64-encoded encrypted ciphertext
     * @throws NoSuchPaddingException         if the padding scheme is unavailable for AES
     * @throws NoSuchAlgorithmException       if AES algorithm is not available
     * @throws InvalidAlgorithmParameterException if the algorithm parameters are invalid
     * @throws InvalidKeyException            if the AES key is invalid
     * @throws BadPaddingException            if the padding is incorrect (should not occur with PKCS5Padding)
     * @throws IllegalBlockSizeException      if the block size is incorrect
     */
    public static String encryptAES(String plaintext, byte[] key, byte[] iv)
        throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidAlgorithmParameterException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException {
        Cipher cipher = Cipher.getInstance(AES_CIPHER_ALGORITHM);
        SecretKeySpec secretKeySpec = new SecretKeySpec(key, AES_ALGORITHM);
        IvParameterSpec ivParameterSpec = new IvParameterSpec(iv);
        cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec, ivParameterSpec);
        byte[] encryptedBytes = cipher.doFinal(plaintext.getBytes());
        return Base64.getEncoder().encodeToString(encryptedBytes);
    }

    /**
     * Decrypts base64-encoded ciphertext using AES decryption with CBC mode and PKCS5Padding.
     *
     * @param ciphertext base64-encoded ciphertext to decrypt
     * @param key        the AES encryption key as a byte array
     * @param iv         the initialization vector (IV) as a byte array
     * @return decrypted plaintext
     * @throws NoSuchPaddingException         if the padding scheme is unavailable for AES
     * @throws NoSuchAlgorithmException       if AES algorithm is not available
     * @throws InvalidAlgorithmParameterException if the algorithm parameters are invalid
     * @throws InvalidKeyException            if the AES key is invalid
     * @throws BadPaddingException            if the padding is incorrect (should not occur with PKCS5Padding)
     * @throws IllegalBlockSizeException      if the block size is incorrect
     */
    public static String decryptAES(String ciphertext, byte[] key, byte[] iv)
        throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidAlgorithmParameterException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException {
        Cipher cipher = Cipher.getInstance(AES_CIPHER_ALGORITHM);
        SecretKeySpec secretKeySpec = new SecretKeySpec(key, AES_ALGORITHM);
        IvParameterSpec ivParameterSpec = new IvParameterSpec(iv);
        cipher.init(Cipher.DECRYPT_MODE, secretKeySpec, ivParameterSpec);
        byte[] decryptedBytes = cipher.doFinal(
            Base64.getDecoder().decode(ciphertext)
        );
        return new String(decryptedBytes);
    }

    /**
     * Generates a random DES encryption key.
     *
     * @return DES encryption key as a byte array
     * @throws NoSuchAlgorithmException if DES algorithm is not available
     */
    public static byte[] generateDESKey() throws NoSuchAlgorithmException {
        KeyGenerator keyGenerator = KeyGenerator.getInstance(DES_ALGORITHM);
        SecretKey secretKey = keyGenerator.generateKey();
        return secretKey.getEncoded();
    }

    /**
     * Encrypts plaintext using DES encryption with CBC mode and PKCS5Padding.
     *
     * @param plaintext the text to encrypt
     * @param key       the DES encryption key as a byte array
     * @param iv        the initialization vector (IV) as a byte array
     * @return base64-encoded encrypted ciphertext
     * @throws NoSuchPaddingException         if the padding scheme is unavailable for DES
     * @throws NoSuchAlgorithmException       if DES algorithm is not available
     * @throws InvalidAlgorithmParameterException if the algorithm parameters are invalid
     * @throws InvalidKeyException            if the DES key is invalid
     * @throws BadPaddingException            if the padding is incorrect (should not occur with PKCS5Padding)
     * @throws IllegalBlockSizeException      if the block size is incorrect
     */
    public static String encryptDES(String plaintext, byte[] key, byte[] iv)
        throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidAlgorithmParameterException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException {
        Cipher cipher = Cipher.getInstance(DES_CIPHER_ALGORITHM);
        SecretKeySpec secretKeySpec = new SecretKeySpec(key, DES_ALGORITHM);
        IvParameterSpec ivParameterSpec = new IvParameterSpec(iv);
        cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec, ivParameterSpec);
        byte[] encryptedBytes = cipher.doFinal(plaintext.getBytes());
        return Base64.getEncoder().encodeToString(encryptedBytes);
    }

    /**
     * Decrypts base64-encoded ciphertext using DES decryption with CBC mode and PKCS5Padding.
     *
     * @param ciphertext base64-encoded ciphertext to decrypt
     * @param key        the DES encryption key as a byte array
     * @param iv         the initialization vector (IV) as a byte array
     * @return decrypted plaintext
     * @throws NoSuchPaddingException         if the padding scheme is unavailable for DES
     * @throws NoSuchAlgorithmException       if DES algorithm is not available
     * @throws InvalidAlgorithmParameterException if the algorithm parameters are invalid
     * @throws InvalidKeyException            if the DES key is invalid
     * @throws BadPaddingException            if the padding is incorrect (should not occur with PKCS5Padding)
     * @throws IllegalBlockSizeException      if the block size is incorrect
     */
    public static String decryptDES(String ciphertext, byte[] key, byte[] iv)
        throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidAlgorithmParameterException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException {
        Cipher cipher = Cipher.getInstance(DES_CIPHER_ALGORITHM);
        SecretKeySpec secretKeySpec = new SecretKeySpec(key, DES_ALGORITHM);
        IvParameterSpec ivParameterSpec = new IvParameterSpec(iv);
        cipher.init(Cipher.DECRYPT_MODE, secretKeySpec, ivParameterSpec);
        byte[] decryptedBytes = cipher.doFinal(
            Base64.getDecoder().decode(ciphertext)
        );
        return new String(decryptedBytes);
    }

    /**
     * Generates an RSA key pair with the specified key size.
     *
     * @param keySize the size of the RSA key in bits
     * @return KeyPair object containing RSA public and private keys
     * @throws NoSuchAlgorithmException if RSA algorithm is not available
     */
    public static KeyPair generateRSAKeyPair(int keySize)
        throws NoSuchAlgorithmException {
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance(
            RSA_ALGORITHM
        );
        keyPairGenerator.initialize(keySize);
        return keyPairGenerator.generateKeyPair();
    }

    /**
     * Encrypts plaintext using RSA encryption.
     *
     * @param plaintext the text to encrypt
     * @param publicKey RSA public key
     * @return base64-encoded encrypted ciphertext
     * @throws NoSuchPaddingException         if the padding scheme is unavailable for RSA
     * @throws NoSuchAlgorithmException       if RSA algorithm is not available
     * @throws InvalidKeyException            if the RSA key is invalid
     * @throws BadPaddingException            if the padding is incorrect
     * @throws IllegalBlockSizeException      if the block size is incorrect
     */
    public static String encryptRSA(String plaintext, PublicKey publicKey)
        throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException {
        Cipher cipher = Cipher.getInstance(RSA_ALGORITHM);
        cipher.init(Cipher.ENCRYPT_MODE, publicKey);
        byte[] encryptedBytes = cipher.doFinal(plaintext.getBytes());
        return Base64.getEncoder().encodeToString(encryptedBytes);
    }

    /**
     * Decrypts base64-encoded ciphertext using RSA decryption.
     *
     * @param ciphertext base64-encoded ciphertext to decrypt
     * @param privateKey RSA private key
     * @return decrypted plaintext
     * @throws NoSuchPaddingException         if the padding scheme is unavailable for RSA
     * @throws NoSuchAlgorithmException       if RSA algorithm is not available
     * @throws InvalidKeyException            if the RSA key is invalid
     * @throws BadPaddingException            if the padding is incorrect
     * @throws IllegalBlockSizeException      if the block size is incorrect
     */
    public static String decryptRSA(String ciphertext, PrivateKey privateKey)
        throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException {
        Cipher cipher = Cipher.getInstance(RSA_ALGORITHM);
        cipher.init(Cipher.DECRYPT_MODE, privateKey);
        byte[] decryptedBytes = cipher.doFinal(
            Base64.getDecoder().decode(ciphertext)
        );
        return new String(decryptedBytes);
    }

    /**
     * Converts a byte array to a hexadecimal string representation.
     *
     * @param bytes the byte array to convert
     * @return hexadecimal string representation of the byte array
     */
    public static String bytesToHex(byte[] bytes) {
        StringBuilder result = new StringBuilder();
        for (byte b : bytes) {
            result.append(String.format("%02X", b));
        }
        return result.toString();
    }

    /**
     * Converts a hexadecimal string to a byte array.
     *
     * @param hexString the hexadecimal string to convert
     * @return byte array representation of the hexadecimal string
     */
    public static byte[] hexToBytes(String hexString) {
        int len = hexString.length();
        byte[] bytes = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            bytes[i / 2] = (byte) ((Character.digit(hexString.charAt(i), 16) <<
                    4) +
                Character.digit(hexString.charAt(i + 1), 16));
        }
        return bytes;
    }

    /**
     * Generates an HMAC (Hash-based Message Authentication Code) using SHA-256.
     *
     * @param data     the data to generate HMAC for
     * @param keyBytes the key as byte array for HMAC
     * @return base64-encoded HMAC
     * @throws NoSuchAlgorithmException if HMAC-SHA256 algorithm is not available
     * @throws InvalidKeyException      if the key is invalid
     */
    public static String generateHmacSHA256(String data, byte[] keyBytes)
        throws NoSuchAlgorithmException, InvalidKeyException {
        SecretKeySpec secretKeySpec = new SecretKeySpec(
            keyBytes,
            HMAC_ALGORITHM
        );
        Mac mac = Mac.getInstance(HMAC_ALGORITHM);
        mac.init(secretKeySpec);
        byte[] hmacBytes = mac.doFinal(data.getBytes(StandardCharsets.UTF_8));
        return Base64.getEncoder().encodeToString(hmacBytes);
    }

    /**
     * Generates a SHA-256 hash of the input data.
     *
     * @param data the data to hash
     * @return hexadecimal string representation of the SHA-256 hash
     * @throws NoSuchAlgorithmException if SHA-256 algorithm is not available
     */
    public static String generateSHA256(String data)
        throws NoSuchAlgorithmException {
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte[] hash = digest.digest(data.getBytes(StandardCharsets.UTF_8));
        return bytesToHex(hash);
    }

    /**
     * Encodes a byte array into a base64 string.
     *
     * @param bytes the byte array to encode
     * @return base64-encoded string representation of the byte array
     */
    public static String encodeBase64(byte[] bytes) {
        return Base64.getEncoder().encodeToString(bytes);
    }

    /**
     * Decodes a base64-encoded string into a byte array.
     *
     * @param base64Str the base64-encoded string to decode
     * @return byte array representation of the decoded base64 string
     */
    public static byte[] decodeBase64(String base64Str) {
        return Base64.getDecoder().decode(base64Str);
    }

    /**
     * Converts a SecretKey object to a byte array.
     *
     * @param secretKey the SecretKey object to convert
     * @return byte array representation of the SecretKey
     */
    public static byte[] secretKeyToBytes(SecretKey secretKey) {
        return secretKey.getEncoded();
    }

    /**
     * Converts a byte array to a SecretKey object for a specified algorithm.
     *
     * @param keyBytes the byte array containing the key
     * @param algorithm the algorithm name (e.g., AES, DES)
     * @return SecretKey object generated from the byte array
     */
    public static SecretKey bytesToSecretKey(
        byte[] keyBytes,
        String algorithm
    ) {
        return new SecretKeySpec(keyBytes, algorithm);
    }

    /**
     * Converts a KeyPair object to byte arrays for both public and private keys.
     *
     * @param keyPair the KeyPair object containing public and private keys
     * @return an array where index 0 is public key bytes and index 1 is private key bytes
     */
    public static byte[][] keyPairToBytes(KeyPair keyPair) {
        byte[][] keyBytes = new byte[2][];
        keyBytes[0] = keyPair.getPublic().getEncoded();
        keyBytes[1] = keyPair.getPrivate().getEncoded();
        return keyBytes;
    }

    /**
     * Converts byte arrays representing public and private keys to a KeyPair object.
     *
     * @param publicKeyBytes  byte array representing the public key
     * @param privateKeyBytes byte array representing the private key
     * @param algorithm       the algorithm name (e.g., RSA)
     * @return KeyPair object containing the public and private keys
     */
    public static KeyPair bytesToKeyPair(
        byte[] publicKeyBytes,
        byte[] privateKeyBytes,
        String algorithm
    ) throws NoSuchAlgorithmException, InvalidKeySpecException {
        KeyFactory keyFactory = KeyFactory.getInstance(algorithm);
        X509EncodedKeySpec publicKeySpec = new X509EncodedKeySpec(
            publicKeyBytes
        );
        PrivateKey privateKey = keyFactory.generatePrivate(
            new PKCS8EncodedKeySpec(privateKeyBytes)
        );
        PublicKey publicKey = keyFactory.generatePublic(publicKeySpec);
        return new KeyPair(publicKey, privateKey);
    }
}
