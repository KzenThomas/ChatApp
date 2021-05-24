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

	public static String stringEncryption(String text, String key) {
		String cipherText = "";
		int cipher[] = new int[key.length()];

		for (int i = 0; i < key.length(); i++) {
			cipher[i] = text.charAt(i) - 'A' + key.charAt(i) - 'A';
		}

		for (int i = 0; i < key.length(); i++) {
			if (cipher[i] > 25) {
				cipher[i] = cipher[i] - 26;
			}
		}

		for (int i = 0; i < key.length(); i++) {
			int x = cipher[i] + 'A';
			cipherText += (char) x;
		}

		return cipherText;
	}

	/**
	 * Deze method zorgt ervoor dat je de geëncrypteerde tekst kan decrypteren naar
	 * plain text.
	 * 
	 * @param s,key
	 */

	public static String stringDecryption(String s, String key) {
		String plainText = "";
		int plain[] = new int[key.length()];
		
		for (int i = 0; i < key.length(); i++) {
			plain[i] = s.charAt(i) - 'A' - (key.charAt(i) - 'A');
		}

		for (int i = 0; i < key.length(); i++) {
			if (plain[i] < 0) {
				plain[i] = plain[i] + 26;
			}
		}
		for (int i = 0; i < key.length(); i++) {
			int x = plain[i] + 'A';
			plainText += (char) x;
		}
		String formattedOutputMessage = "";
		String[] splitStringByColon = plainText.split(":");
		for (int i = 0; i < splitStringByColon.length; i++) {
			formattedOutputMessage += splitStringByColon[i].toLowerCase() + " ";
		}
		return formattedOutputMessage;
	}
}