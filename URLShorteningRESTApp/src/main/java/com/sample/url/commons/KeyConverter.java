package com.sample.url.commons;
/***
 * 
 * @author Ritika Sao
 * 
 * This class has method implementation for generating encoded key for the short 
 * link generation from a unique numeric ID
 * 
 *
 */
public class KeyConverter {
	
	/* Stores the characters to be included for the short link generation
	 * Taking (62 characters) 26 (A-Z) + 26 (a-z) + 10 (0-9) for simplicity */
	private static final String BASE62_CHARACTERS = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
	
	/* Stores the total no of base characters*/
	private static final int    BASE_LENGTH     = BASE62_CHARACTERS.length();
	
	/***
	 * 
	 * @param urlID
	 * @return Encoded Short Link Key
	 * 
	 * Returns the encoded key for generation of short link
	 */
	public static String encodeToBase62(long urlID) {

		StringBuilder sb = new StringBuilder();
		
		while ( urlID > 0 ) {
			sb.append( BASE62_CHARACTERS.charAt((int) urlID % BASE_LENGTH ));
			urlID /= BASE_LENGTH;
		}
		return sb.reverse().toString(); 
	}

}
