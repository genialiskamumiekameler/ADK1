import java.io.*;
import java.util.*;

public class Concordance {
	
	static String hashFile = "/home/user/ADK1/hashfile";
	
	public static void main (String[] args) {
		//load hashfile from disk
		int[] hashIndex;
		FileInputStream fileInputStream;
		try {
			fileInputStream = new FileInputStream(hashFile);
		} catch(FileNotFoundException e) {
			System.out.println(e);
			return;
		}
		try {
			ObjectInputStream inputStream = new ObjectInputStream(fileInputStream);
			try{
				hashIndex = (int[])inputStream.readObject();
			} catch(ClassNotFoundException e) {
				System.out.println(e);
				return;
			}
		} catch (IOException e) {
			System.out.println(e);
			return;
		}

		//word search test
		String searchTest = "ochra"; //will find index pos for 'och'
		int searchHash = Hasher.hash(searchTest);
		System.out.println(hashIndex[searchHash]);
		//end test

		//open index file and seek to hashFile[Hasher.hash(args[0])]
		
		//perform binary search for exact word in index
		
		//iterate over word instances and add to array
		
		//get array length and output some text about number of words found
		
		//open corpus
		
		//for each item in array, seek in corpus and output (60+wordlength) bytes
	}
}
