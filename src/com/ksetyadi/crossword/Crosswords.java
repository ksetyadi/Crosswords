package com.ksetyadi.crossword;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Random;


public class Crosswords {
	
	public static String[][] matrix;
	public static final int RIGHT = 0;
	public static final int DIAGONAL = 1;
	public static final int DOWN = 2;

	public static void main(String[] args) {
		String words = null;
		BufferedReader reader;
		reader = new BufferedReader(new InputStreamReader(System.in));
		System.out.print("Input three words separated by space: ");
		
		try {
			words = reader.readLine();
		} catch (IOException e) {
			e.printStackTrace();
			return;
		}
		
		// check the input
		if (isValid(words)) {
			showCrosswords(words);
		} else {
			System.out.println("The words you enter is not valid.");
		}
		
		// process the input to create the crosswords
	}
	
	private static boolean isValid(String words) {
		// check for the null value
		if (words == null) {
			return false;
		}
		
		String[] arrWord = words.split(" ");
		
		// check if the array's length is less then 3
		if (arrWord.length != 3) {
			return false;
		}
		
		// check if each words not exceed 10 chars length
		for (int counter = 0; counter <= arrWord.length - 1; counter++) {
			if (arrWord[counter].length() > 9) {
				return false;
			}
		}
		
		// we can check for more conditions here but I think it's enough
		// to have those basic conditions
		
		return true;
	}
	
	private static void showCrosswords(String words) {
		String[] arrWord = words.split(" ");
		
		// create a matrix of 10x10
		matrix = new String[10][10];
		
		// fill it with asterix
		for (int row = 0; row <= 9; row++) {
			for (int col = 0; col <= 9; col++) {
				matrix[row][col] = "*";
			}
		}
		
		// put the 3 words randomly in matrix
		for (int counter = 0; counter <= arrWord.length - 1; counter++) {
			String word = arrWord[counter];
			putWord(word);
		}
	
		// formed the final crossword
		// put random chars on unoccupied cells
		
		System.out.println();
		
		System.out.println("X" + " 1 2 3 4 5 6 7 8 9 0");
		
		for (int row = 0; row <= 9; row++) {
			for (int col = 0; col <= 9; col++) {
				if (matrix[row][col].equals(("*"))) {
					matrix[row][col] = anyRandomChar();
				}
				
				if (col == 0) {
					System.out.print(((row + 1) % 10) + " " + matrix[row][col] + " ");
				} else {
					System.out.print(matrix[row][col] + " ");
				}
			}
			
			System.out.println("");
		}
	}
	
	private static void putWord(String word) {
		System.out.println("Trying to put the word: " + word);
		
		Random random = new Random();
		boolean isPutted = false;
		
		while (!isPutted) {
			int row = random.nextInt(10 - word.length() + 1);
			int col = random.nextInt(10 - word.length() + 1);
			int direction = random.nextInt(3);
			
			boolean isClear = true;

			switch (direction) {
			case RIGHT:
				for (int counter = col; counter <= col + word.length() - 1; counter++) {
					if (!matrix[row][counter].equals("*")) {
						isClear = false;
					}
				}
				
				if (isClear) {
					for (int counter = col; counter <= col + word.length() - 1; counter++) {
						matrix[row][counter] = word.substring(counter - col, counter - col + 1).toUpperCase();
					}
					
					System.out.println("The word " + word + " is putted RIGHT on " + (row + 1) + ", " + (col + 1));
					isPutted = true;
				}
			
				break;
				
			case DIAGONAL:
				int cols = col;
				
				for (int pos = row; pos <= row + word.length() - 1; pos++) {
					if (!matrix[pos][cols++].equals("*")) {
						isClear = false;
						break;
					}
				}
				
				if (isClear) {
					cols = col;
					
					for (int pos = row; pos <= row + word.length() - 1; pos++) {
						matrix[pos][cols++] = word.substring(pos - row, pos - row + 1).toUpperCase();
					}
					
					System.out.println("The word " + word + " is putted DIAGONALLY on " + (row + 1) + ", " + (col + 1));
					isPutted = true;
				}
				
				break;
				
			case DOWN: 
				for (int counter = row; counter <= row + word.length() - 1; counter++) {
					if (!matrix[counter][col].equals("*")) {
						isClear = false;
					}
				}
				
				if (isClear) {
					for (int counter = row; counter <= row + word.length() - 1; counter++) {
						matrix[counter][col] = word.substring(counter - row, counter - row + 1).toUpperCase();
					}
					
					System.out.println("The word " + word + " is putted DOWN on " + (row + 1) + ", " + (col + 1));
					isPutted = true;
				}
				
				break;
			}
		}
	}
	
	private static String anyRandomChar() {
		Random random = new Random();
		String result = new String(Character.toChars(65 + random.nextInt(26)));
		// System.out.println("Random char: " + result);
		return result;
	}
}
