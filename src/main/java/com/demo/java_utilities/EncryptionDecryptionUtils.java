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
}
