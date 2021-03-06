import java.io.*;
import java.util.*;

public class Hasher {
	//takes a word and returns a lazyhash index based on the three first letters
	public static int hash (String word){
		byte[] c = new byte[0];
		try{
			c = word.getBytes("ISO-8859-1");
		}catch (UnsupportedEncodingException e){
			System.out.println(e);
		}
		
		StringBuilder sb = new StringBuilder();
		int hashLength = 3;
		if (word.length() < hashLength) {
			hashLength = word.length();		//handle short words
		}
		for (int i = 0; i < hashLength; i++){
			int charVal = getCharVal(c[i]);
			if (charVal == -61){
				//we hit a control char, skip please
				hashLength++;
				continue;
			}
			sb.append(charVal);
			if (charVal < 10){
				sb.insert((sb.length()-1), 0);	//leading zeroes are important
			}
		}
		String output = sb.toString();
		return Integer.parseInt(output);
	}
	
	//converts an ISO-8859-1 byte to a (corrected) int value (0 to 29 inclusive)
	public static int getCharVal (byte b){
		int val = (int)b;
		if (val > 64 && val < 91) {
			//handle A..Z as a..z
			val += 32;
		} else if (val == -59 || val == -60 || val == -42){
			//handle ÅÄÖ as åäö
			val += 32;
		}
		switch(val){
		case 32:
			//this is a space
			val = 0;
			break;
		case 63:
			//we hit a control char, skip please
			return 0;
		case -27:
			//we found 'å'
			//val += (91+27);
			val = 27;
			break;
		case -28:
			//we found 'ä'
			//val += (92+28);
			val = 28;
			break;
		case -10:
			//we found 'ö'
			//val += (74+29);
			val = 29;
			break;
		default:
			//for all regular letters, subtract 97
			val -= 96;
			if (val < 0) { 
				System.out.println("found a strange char");
				System.out.println(val + 96);
			}
		}
		return val;
	}
}
