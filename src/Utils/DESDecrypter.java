package Utils;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.util.Base64;

import javax.crypto.*;

/**
 * Decrytpe message
 *
 * @author Trinome
 */
class DESDecrypter {
  private Cipher dcipher;

  /**
   * Constructor for the DESEncrypter object
   */
  public DESDecrypter() {

    try {
      dcipher = Cipher.getInstance("DES");
    } catch (NoSuchPaddingException ex) {
      ex.printStackTrace();
    } catch (NoSuchAlgorithmException ex) {
      ex.printStackTrace();
    }

  }

  /**
   * Sets the Key attribute of the DESEncrypter object
   *
   * @param keyByte The new Key value
   */
  public void setKey(byte[] keyByte) {

    try {
      // generate key from byte
      javax.crypto.spec.DESKeySpec sks = new javax.crypto.spec.DESKeySpec(keyByte);
      SecretKey sKey = SecretKeyFactory.getInstance("DES").generateSecret(sks);

      // set key
      dcipher.init(Cipher.DECRYPT_MODE, sKey);
    } catch (NoSuchAlgorithmException ex) {
      ex.printStackTrace();
    } catch (InvalidKeySpecException ex) {
      ex.printStackTrace();
    } catch (InvalidKeyException ex) {
      ex.printStackTrace();
    }

  }

  /**
   * Decrypte le message
   *
   * @param str message à décrypter
   * @return message décrypté
   */
  public String decrypt(String str) {
    try {
      // Decode base64 to get bytes
      byte[] dec = Base64.getEncoder().encode(str.getBytes());

      // Decrypt
      byte[] utf8 = dcipher.doFinal(dec);

      // Decode using utf-8
      return new String(utf8, "UTF8");
    } catch (javax.crypto.BadPaddingException e) {
      e.printStackTrace();
    } catch (IllegalBlockSizeException e) {
    } catch (UnsupportedEncodingException e) {
      e.printStackTrace();
    }
    return null;
  }
}
