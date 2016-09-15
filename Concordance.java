import java.io.*;
import java.util.*;
import java.awt.event.KeyEvent;

public class Concordance {
	
	static String korpusPath = "/info/adk13/labb1/korpus";
	static String hashFile = "/var/tmp/hashFile";
	static String indexPath = "/var/tmp/tokenizerFile";
	static int[] hashIndex;
	
	public static void main (String[] args) {
		//load hashfile from disk
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


		// Kollar att input är giltig
		if(args.length != 1){
			System.out.println("Ogiltig input. Du måste söka på ett ord.");
			System.exit(-1);
		}
		if(!args[0].equals("[A-Za-zåäöÅÄÖ]+")){
			System.out.println("Ogiltig input. Du måste söka på ett ord.");
			System.exit(-1);
		}

		try{
			String searchWord = args[0];
			ArrayList<Integer> korpusPositions = new ArrayList<Integer>();
			korpusPositions = positionsFromIndex(searchWord);
			
			//get array length and output some text about number of words found
			System.out.printf("Det finns %s förkomster av ordet.\n", korpusPositions.size());
			if(korpusPositions.size() > 25){
				System.out.println("För att skriva ut alla förekomster, tryck enter. Annars tryck escape.");
				// Checka vad användaren trycker
				if(KeyEvent.VK_ENTER == 1){
					printFromKorpus(korpusPositions);
				}
				if(KeyEvent.VK_ESCAPE == 1){
					System.exit(-1);
				}
			}
			else{
				printFromKorpus(korpusPositions);
			}
		}
		catch(FileNotFoundException e)
		{
			System.out.println(e);
			System.exit(-1);
		}
		catch(IOException e){
			System.out.println(e);
			System.exit(-1);
		}
	}


	// Returns a list of all the positions that the search word occurs on
	static private ArrayList<Integer> positionsFromIndex(String searchWord) throws FileNotFoundException, IOException{

		int indexPos = 0;

		int searchHash = Hasher.hash(searchWord);
		System.out.println(hashIndex[searchHash]);

		
		//open index file and seek to hashFile[Hasher.hash(args[0])]
		RandomAccessFile indexFile = new RandomAccessFile(indexPath, "r");
		indexFile.seek((long)hashIndex[searchHash]);

		// First word with the hash value
		String searchHashLine = indexFile.readLine();
		String[] splitLine = searchHashLine.split("\\s");


		// If the first word does not match the word we are looking for, perform a binary search for exact word

		// First position for binary search 
		int searchHashIndex = hashIndex[searchHash]; 

		if(!splitLine[0].equals(searchWord)){
			// Last position for binary search
			int endHash = searchHash + 1;
			int endHashIndex = hashIndex[endHash];


			// the actual binary search
			while(endHashIndex - searchHashIndex > 1000){
				int m = (searchHashIndex + endHashIndex) / 2;
				indexFile.seek((long)m);
				String line = indexFile.readLine();
				String[] sl = line.split("\\s"); 
				
				// compareTo compares two strings lexicographically
				if(searchWord.compareTo(sl[0]) >= 0){
					searchHashIndex = m;
				}
				else{
					endHashIndex = m; 
				}
			}

			// when the distance between the searchHashIndex and endHashIndex is less than 1000
			indexFile.seek(searchHashIndex);
			while(true){
				String line = indexFile.readLine();
				String[] sl = line.split("\\s"); 

				// If we are on the word we are looking for, return its first position in index file/tokenizer file
				if(sl[0].equals(searchWord)){
					indexPos = Integer.parseInt(sl[1]);
				}
				// If we reach the end of the file before finding a matching word, the word does not exist
				if(line == null){
					System.out.println("Ordet hittades inte.");
					System.exit(-1);
				}
			}
		}	

		// Now we should have the first position the word occurs on in the index file
		// iterate over word instances and add to array
		indexFile.seek((long)indexPos);
		String line = indexFile.readLine();
		String[] sl = line.split("\\s"); 
		String currentWord = sl[0];

		ArrayList<Integer> positions = new ArrayList<Integer>();
		while(currentWord.equals(searchWord) && currentWord != null){
			line = indexFile.readLine();
			sl = line.split("\\s");
			currentWord = sl[0];
			// As long as the current word matches the word we are looking for it adds the position to the positions list
			positions.add(Integer.valueOf(sl[1]));
		}

		return positions;
	}

	// Prints the word in its context
	static private void printFromKorpus(ArrayList<Integer> positions) throws FileNotFoundException, IOException{
		RandomAccessFile korpus = new RandomAccessFile(korpusPath, "r");
		for(int i : positions){
			korpus.seek((long)i-30);
			byte[] b = new byte[60];
			int numBytesRead = korpus.read(b); 
			String conc = new String(b, "ISO-8859-1");
			System.out.println(conc);
		}
	}
}
