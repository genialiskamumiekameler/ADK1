import java.io.*;
import java.util.*;

public class Builder {
	public static void main (String[] args) {
		int[] A = new int[292930];
		
		//åäö--A..Za..zÅÄÖ
		//ÅÄÖ = 123,125,106
		//splitting test
		String s = "Strategi 4096";	//let's say 'strategi' shows up at position 4096 in index file
		String[] ss = s.split("\\s");
		String word = ss[0];
		int pos = Integer.parseInt(ss[1]);
		//end test
		
		//hash insertion test
		int hashCode = hash(word);
		if (A[hashCode] == 0){		// <---- likely source of errors
			A[hashCode] = pos;
		}
		//end test
		
		//hash search test
		String searchTest = "stryka";
		int searchHash = hash(searchTest);
		System.out.println(A[searchHash]);
		//end test
	}
	
	//TODO: conversion to lowercase
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
