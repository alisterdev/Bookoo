package com.bookoo.util;

import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

import org.apache.commons.net.util.Base64;

import java.io.Serializable;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;

/**
 * Hash Password Utility provided by Martin Konicek.
 * http://stackoverflow.com/questions
 * /2860943/suggestions-for-library-to-hash-passwords-in-java
 * 
 * @author Alex, Darrel
 * 
 */
public class HashPassword implements Serializable  {

	private static final long serialVersionUID = 5956873667254903859L;
	private static final int iterations = 10 * 1024;
	private static final int saltLen = 32;
	private static final int desiredKeyLen = 256;

	/**
	 * Computes a salted PBKDF2 hash of given plain text password suitable for
	 * storing in a database. Empty passwords are not supported.
	 * 
	 * @param password
	 * @return
	 * @throws Exception
	 */
	public static String getSaltedHash(String password) {
		byte[] salt;
		try {
			salt = SecureRandom.getInstance("SHA1PRNG").generateSeed(saltLen);
			// store the salt with the password
			return Base64.encodeBase64String(salt) + "$" + hash(password, salt);
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * Checks whether given plain text password corresponds to a stored salted
	 * hash of the password.
	 * 
	 * @param password
	 * @param stored
	 * @return
	 * @throws Exception
	 */
	public static boolean validatePassword(String password, String stored) {
		String[] saltAndPass = stored.split("\\$");
		if (saltAndPass.length != 2)
			return false;
		String hashOfInput = hash(password, Base64.decodeBase64(saltAndPass[0]));
		return hashOfInput.equals(saltAndPass[1]);
	}

	/**
	 * Hashes the password based on a given byte encoding.
	 * 
	 * @param password
	 * @param salt
	 * @return
	 * @throws Exception
	 */
	private static String hash(String password, byte[] salt) {
		if (password == null || password.length() == 0)
			throw new IllegalArgumentException(
					"Empty passwords are not supported.");
		SecretKeyFactory f;
		try {
			f = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");

			SecretKey key = f.generateSecret(new PBEKeySpec(password
					.toCharArray(), salt, iterations, desiredKeyLen));
			return Base64.encodeBase64String(key.getEncoded());
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvalidKeySpecException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
}