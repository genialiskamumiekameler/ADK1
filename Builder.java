import java.io.*;
import java.util.*;

//Builds a concordance as hashmap -> index -> corpus
public class Builder {
	
	//edit these for correct file structure
	static String hashFile = "/home/user/ADK1/hashfile";
	static String indexFile = "/home/user/ADK1/fakeInput";
	
	public static void main (String[] args) {
		int[] indexHash = new int[292930];
		
		//create indexfile via tokenizer.c
		//--currently faked with file fakeInput.txt
		
		//read index file line by line
		BufferedReader reader;
		String input;
		try{
			File tokens = new File(indexFile);
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
				int hashCode = Hasher.hash(word);
				if (indexHash[hashCode] == 0) {
					indexHash[hashCode] = pos;
				}
			}
		} catch(IOException e) {
			System.out.println(e);
			return;
		}
		
		//save hashmap to file
		FileOutputStream fileOutputStream;
		try {
			fileOutputStream = new FileOutputStream(hashFile);
		} catch(FileNotFoundException e) {
			System.out.println(e);
			return;
		}
		try {
			ObjectOutputStream outputStream = new ObjectOutputStream(fileOutputStream);
			outputStream.writeObject(indexHash);
		} catch (IOException e) {
			System.out.println(e);
			return;
		}
	}
}
