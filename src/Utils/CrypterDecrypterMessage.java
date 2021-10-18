package Utils;

import java.io.UnsupportedEncodingException;
import java.security.*;
import java.security.spec.InvalidKeySpecException;

import javax.crypto.*;

/**
 *  Utilities pour crypter/décrypter
 *
 *@author    Trinome
 */
public class CrypterDecrypterMessage{
	private static CrypterDecrypterMessage instance;



	/**
         *  Encrypte avec la clé générée
         *
         *@param  key      CLé de l'encryptage
         *@param  message  message à encrypter
         *@return          message encrypté
         */
	public String encrypter(Key key, String message) {
		// get a encrypter object
		DESEncrypter encrypteOutil = new DESEncrypter();

		// set the key
		encrypteOutil.setKey(key);

		//crypt
		String encryptedMessage = encrypteOutil.encrypt(message);
		return encryptedMessage;
	}



	/**
         *  Décrypte avec la clé générée au moment de l'encryptage
         *
         */
	public String decrypt(byte[] keyByte, String encryptedMessage) {
		// get a decrypter object
		DESDecrypter decrypter = new DESDecrypter();

		//set key
		decrypter.setKey(keyByte);

		//decrypt
		String message = decrypter.decrypt(encryptedMessage);
		return message;
	}


	/**
         *  Génère une clé symétrique avec DES
         *
         *@return    Description of the Returned Value
         */
	public Key generateKey() {
		try {
			KeyGenerator kg = KeyGenerator.getInstance("DES");
			kg.init(56);
			// 56 is the keysize. Fixed for DES
			Key key = kg.generateKey();
			return key;
		}
		catch (NoSuchAlgorithmException ex) {
			ex.printStackTrace();
			return null;
		}

	}



	/**
         *  Gets the Instance attribute of the DESUtilities object
         *
         *@return    The Instance value
         */
	public static CrypterDecrypterMessage getInstance() {
		if (instance == null) {
			instance = new CrypterDecrypterMessage();
		}
		return instance;
	}
}