package ChatApp;

import java.io.*;
import java.nio.file.Files;
import java.util.Scanner;

import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Component;

import javassist.bytecode.ByteArray;

@Component
public class OneTimePad {
	public static int position = 0;
	
	public String encrypt(File f, String text) {
		String key = FileToString(f, position, text.length());
		position = OneTimePad.position += text.length();
		String encrypted = stringEncryption(text.toUpperCase(), key.toUpperCase());
		return encrypted;
	}

	public String decrypt(File f, int position, String text) {
		String key = FileToString(f, position, text.length());
		String decrypted = stringDecryption(text.toUpperCase(), key.toUpperCase());
		return decrypted;
	}

	public static String FileToString(File f, int position, int length) {
		try {
			ByteArrayInputStream bai = new ByteArrayInputStream(Files.readAllBytes(f.toPath()));
			bai.skip(position);
			byte[] readNbytes = bai.readNBytes(length);
			String filestring = new String(readNbytes);
			return filestring;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return "Error";
	}

	/**
	 * Deze method zorgt ervoor dat de plain text wordt geëncrypteerd naar
	 * geëncrypteerde tekst.
	 * 
	 * @param text,key
	 */

	// function which returns encryptedText
	public static String stringEncryption(String text, String key) {

		// initializing cipherText
		String cipherText = "";

		// initialize cipher array of key length
		// which stores the sum of corresponding no.'s
		// of plainText and key.
		int cipher[] = new int[key.length()];

		for (int i = 0; i < key.length(); i++) {
			cipher[i] = text.charAt(i) - 'A' + key.charAt(i) - 'A';
		}

		// if the sum is greater than 25
		// subtract 26 from it and store that resulting
		// value
		for (int i = 0; i < key.length(); i++) {
			if (cipher[i] > 25) {
				cipher[i] = cipher[i] - 26;
			}
		}

		// convert the no.'s into integers
		// convert these integers to corresponding
		// characters and add them up to cipherText
		for (int i = 0; i < key.length(); i++) {
			int x = cipher[i] + 'A';
			cipherText += (char) x;
		}

		// returning the cipherText
		return cipherText;
	}

	/**
	 * Deze method zorgt ervoor dat je de geëncrypteerde tekst kan decrypteren naar
	 * plain text.
	 * 
	 * @param s,key
	 */

	// function which returns plainText
	public static String stringDecryption(String s, String key) {
		// initializing plainText
		String plainText = "";

		// initializing integer array of key length
		// which stores difference of corresponding no.'s of
		// each character of cipherText and key
		int plain[] = new int[key.length()];

		// running for loop for each character
		// subtracting and storing in the array
		for (int i = 0; i < key.length(); i++) {
			plain[i] = s.charAt(i) - 'A' - (key.charAt(i) - 'A');
		}

		// if the difference is less than 0
		// add 26 and store it in the array.
		for (int i = 0; i < key.length(); i++) {
			if (plain[i] < 0) {
				plain[i] = plain[i] + 26;
			}
		}

		// convert int to corresponding char
		// add them up to plainText
		for (int i = 0; i < key.length(); i++) {
			int x = plain[i] + 'A';
			plainText += (char) x;
		}

		// returning plainText
		return plainText;
	}

	// main function
	public static void main(String[] args) {

		// declaration of plain text
		String plainText = "Test";

		// declaration of key
		String key = "MONE";

		// converting plain text to toUpperCase
		// function call to stringEncryption
		// with plainText and key as parameters
		String encryptedText = stringEncryption(plainText.toUpperCase(), key.toUpperCase());

		// printing cipher Text
		System.out.println("Cipher Text - " + encryptedText);

		// function call to stringDecryption
		// with encryptedText and key as parameters
		System.out.println("Message - " + stringDecryption(encryptedText, key.toUpperCase()));
	}
}
