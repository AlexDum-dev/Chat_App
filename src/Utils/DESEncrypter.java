package Utils;

import java.io.UnsupportedEncodingException;
import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.util.Base64;

import javax.crypto.*;

/**
 * Encrypter
 *
 * @author Trinome
 */
class DESEncrypter {
  private Cipher ecipher;

  /**
   * Constructor for the DESEncrypter object
   */
  public DESEncrypter() {

    try {
      ecipher = Cipher.getInstance("DES");
    } catch (NoSuchPaddingException ex) {
      ex.printStackTrace();
    } catch (NoSuchAlgorithmException ex) {
      ex.printStackTrace();
    }

  }

  /**
   * Sets the Key attribute of the DESEncrypter object
   *
   * @param key The new Key value
   */
  public void setKey(Key key) {
    try {
      ecipher.init(Cipher.ENCRYPT_MODE, key);
    } catch (InvalidKeyException ex) {
      ex.printStackTrace();
    }

  }

  /**
   * Crypte le message
   *
   * @param str message à crypter
   * @return message crypté
   */
  public String encrypt(String str) {

    try {
      // Encode the string into bytes using utf-8
      byte[] utf8 = str.getBytes("UTF8");

      // Encrypt
      byte[] enc = ecipher.doFinal(utf8);

      // Encode bytes to base64 to get a string
      return new String(Base64.getDecoder().decode(enc));
    } catch (BadPaddingException ex) {
      ex.printStackTrace();
      return null;
    } catch (IllegalBlockSizeException ex) {
      ex.printStackTrace();
      return null;
    } catch (UnsupportedEncodingException ex) {
      ex.printStackTrace();
      return null;
    }

  }

}
