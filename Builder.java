import java.io.*;
import java.util.*;

public class Builder {
	public static void main (String[] args) {
		int[] A = new int[292930];
		
		//create indexfile via tokenizer.c
		
		//read index file line by line
		BufferedReader reader;
		String input;
		try{
			File tokens = new File("/home/user/ADK1/fakeInput.txt");
			reader = new BufferedReader(new FileReader(tokens));
		} catch(FileNotFoundException e) {
			System.out.println(e);
			return;
		}
		try {
			while ((input = reader.readLine()) != null){
				//split line into word/number
				String[] splitInput = input.split("\\s");
				String word = splitInput[0];
				int pos = Integer.parseInt(splitInput[1]);
				
				//insert to hashmap
				int hashCode = hash(word);
				if (A[hashCode] == 0) {
					A[hashCode] = pos;
				}
			}
		} catch(IOException e) {
			System.out.println(e);
			return;
		}
		
		//save hashmap to file
		FileOutputStream fileOutputStream;
		try {
			fileOutputStream = new FileOutputStream("/home/user/ADK1/hashfile");
		} catch(FileNotFoundException e) {
			System.out.println(e);
			return;
		}
		try {
			ObjectOutputStream outputStream = new ObjectOutputStream(fileOutputStream);
			outputStream.writeObject(A);
		} catch (IOException e) {
			System.out.println(e);
			return;
		}

		
	}
	
	//TODO: maybe rewrite to use binary, for more efficient use of array indicies?
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
		} else if (val == -123 || val == -124 || val == -106){
			//handle ÅÄÖ as åäö
			val += 32;
		}
		switch(val){
		case 32:
			//this is a space
			val = 0;
			break;
		case -61:
			//we hit a control char, skip please
			return val;
		case -91:
			//we found 'å'
			val += (91+27);
			break;
		case -92:
			//we found 'ä'
			val += (92+28);
			break;
		case -74:
			//we found 'ö'
			val += (74+29);
			break;
		default:
			//for all regular letters, subtract 97
			val -= 96;
		}
		return val;
	}
}
