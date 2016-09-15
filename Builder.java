import java.io.*;
import java.util.*;

//Builds a concordance as hashmap -> index -> corpus
public class Builder {
	
	//edit these for correct file structure
	static String hashFile = "/var/tmp/hashFile";
	static String indexFile = "/var/tmp/tokenizerFile";
	
	public static void main (String[] args) {
		long[] indexHash = new long[292930];
		
		//read index file line by line
		RandomAccessFile reader;
		try{
			reader = new RandomAccessFile(indexFile, "r");
			String input;
			try {
				while ((input = reader.readLine()) != null){
					//split line into word/number
					String[] splitInput = input.split("\\s");
					String word = splitInput[0];
					long pos = reader.getFilePointer();
					
					//insert to hashmap
					int hashCode = Hasher.hash(word);
					if (indexHash[hashCode] == 0) {
						indexHash[hashCode] = pos;
					}
				}
			} catch(IOException e2) {
				System.out.println(e2);
				return;
			}
		}catch(FileNotFoundException e) {
			System.out.println(e);
			System.exit(-1);
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
