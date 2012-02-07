package com.scac.RLicServer;


/**
 * @author duce
 * Routines used to scramble the text exchange between server and client.
 */
public class RLicUtils {
	public static String strEncode(String arg) {
		return toHex(Rot13(arg));
	}

	public static String strDecode(String arg) {
		return Rot13(toStr(arg));
	}

	private static String Rot13(String plainText) {
		if (plainText == null)
			return plainText;

		String encodedMessage = "";
		for (int i = 0; i < plainText.length(); i++) {
			char c = plainText.charAt(i);
			if (c >= 'a' && c <= 'm')
				c += 13;
			else if (c >= 'n' && c <= 'z')
				c -= 13;
			else if (c >= 'A' && c <= 'M')
				c += 13;
			else if (c >= 'N' && c <= 'Z')
				c -= 13;
			encodedMessage += c;
		}
		return encodedMessage;
	}

	private static String toHex(String chars) {
		StringBuffer result = new StringBuffer();
		for (int i = 0; i < chars.length(); i++) {
			char c = chars.charAt(i);
			result.append( Integer.toHexString(invert((int) c)));
		}
		return result.toString();
	}

	private static String toStr(String hexStr) {
		int strLen = hexStr.length();
		StringBuffer result = new StringBuffer();
		for (int i = 0; i < strLen; i += 2) {
			StringBuffer hexChar = new StringBuffer(2);
			hexChar.append(hexStr.charAt(i));
			hexChar.append(hexStr.charAt(i + 1));
			int charCode = invert(Integer.parseInt(hexChar.toString(), 16));
			result.append((char) charCode);
		}
		return result.toString();
	}

	private static int invert(int i) {
		int j = ~i;
		j = j & 255;
		return (int) j;
	}

}
