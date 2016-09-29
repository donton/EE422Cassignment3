/* WORD LADDER Main.java
 * EE422C Project 3 submission by
 * Manuel Gomez	
 * mlg3454
 * 16475
 * Don Ton
 * dt22776
 * 16470
 * Slip days used: <0>
 * Git URL: https://github.com/donton/EE422Cassignment3
 * Fall 2016
 */

package assignment3;

import java.util.*;
import java.io.*;

public class Main {

	public static String startWord, endWord;
	public static ArrayList<String> empty = new ArrayList<String>();

	public static void main(String[] args) throws Exception {
		initialize();
		Scanner kb; // input Scanner for commands
		PrintStream ps; // output file
		// If arguments are specified, read/write from/to files instead of Std
		// IO.
		if (args.length != 0) {
			kb = new Scanner(new File(args[0]));
			ps = new PrintStream(new File(args[1]));
			System.setOut(ps); // redirect output to ps
		} else {
			kb = new Scanner(System.in);// default from Stdin
			ps = System.out; // default to Stdout
		}
		ArrayList<String> twoWords = parse(kb);
		String first = twoWords.get(0);
		String last = twoWords.get(1);
		//printLadder(getWordLadderDFS(first, last));
		printLadder(getWordLadderBFS(first, last));
	}

	/**
	 * Initialize static variables
	 * 
	 * @param no
	 *            parameters
	 * @return No return value
	 */
	public static void initialize() {
		// initialize your static variables or constants here.
		// We will call this method before running our JUNIT tests. So call it
		// only once at the start of main.
		startWord = new String();
		endWord = new String();
		empty = new ArrayList<String>();

	}

	/**
	 * @param keyboard
	 *            Scanner connected to System.in
	 * @return ArrayList of 2 Strings containing start word and end word. If
	 *         command is /quit, quit the execution
	 */
	public static ArrayList<String> parse(Scanner keyboard) {
		String input;
		String[] parsed;
		ArrayList<String> list = new ArrayList<String>();

		input = ((keyboard.nextLine()).trim()).toUpperCase();
		if (input.contains("/QUIT")) {
			System.exit(0);
		}
		parsed = input.split(" +");
		list.add(parsed[0]);
		list.add(parsed[1]);
		startWord = parsed[0];
		endWord = parsed[1];
		return list;
	}

	/**
	 * Finds DFS
	 * 
	 * @param start
	 *            A string with start word
	 * @param end
	 *            A string with end word
	 * @return ladder ArrayList<String> with word ladder
	 */
	public static ArrayList<String> getWordLadderDFS(String start, String end) {
		start = start.trim().toUpperCase();
		end = end.trim().toUpperCase();
		if (!startWord.equals(start)) {
			startWord = start;
		}
		if (!endWord.equals(end)) {
			endWord = end;
		}
		Set<String> dict = makeDictionary();
		Set<String> set = new HashSet<String>();
		ArrayList<String> ladder = new ArrayList<String>();
		ladder = helperDFS(start, end, set, dict, ladder);
		return ladder;
	}

	/**
	 * Recursive helper for DFS
	 * 
	 * @param start
	 *            A string with start word
	 * @param end
	 *            A string with end word
	 * @param usedWords
	 *            set with found words
	 * @param dict
	 *            dictionary
	 * @param ladder
	 *            final ladder
	 * @return ladder ArrayList with additional word in the ladder
	 */
	private static ArrayList<String> helperDFS(String start, String end, Set<String> usedWords, Set<String> dict,
			ArrayList<String> ladder) {

		boolean flag = false;
		if (start.equals(end)) {
			ladder.add(start);
			return ladder;
		}
		for (String string : dict) {
			if (oneDiff(start, string) && !usedWords.contains(string)) {
				flag = true;
			}
		}
		if (!flag) {
			return empty;
		}
		usedWords.add(start);
		for (String string : dict) {
			if (!usedWords.contains(string) && oneDiff(start, string)) {
				ArrayList<String> temp = helperDFS(string, end, usedWords, dict, ladder);
				if (!ladder.isEmpty()) {
					ladder.add(0, start);
					return ladder;
				}
			}
		}
		return empty;
	}

	/**
	 * Finds BFS
	 * 
	 * @param start
	 *            A string with start word
	 * @param end
	 *            A string with end word
	 * @return ladder ArrayList<String> with word ladder
	 */
	public static ArrayList<String> getWordLadderBFS(String start, String end) {
		start = start.trim().toUpperCase();
		end = end.trim().toUpperCase();
		if (!startWord.equals(start)) {
			startWord = start;
		}
		if (!endWord.equals(end)) {
			endWord = end;
		}
		Queue<Node> q = new LinkedList<Node>();
		ArrayList<String> neighbors = new ArrayList<String>();
		Set<String> dict = makeDictionary();

		q.add(new Node(start, null, true));

		while (!q.isEmpty()) {
			Node current = q.remove();
			dict.remove(current.word);
			if (current.word.equals(end)) {
				ArrayList<String> path = new ArrayList<String>();
				while (!current.word.equals(start)) {
					path.add(0, current.word);
					current = current.parent;
				}
				path.add(0, start);
				return path;
			} else {
				neighbors.clear();
				Collection<String> removeFromDict = new LinkedList<String>();
				for (String string : dict) {
					if (oneDiff(current.word, string)) {
						neighbors.add(string);
						removeFromDict.add(string);
					}
				}
				dict.removeAll(removeFromDict);
				if (!neighbors.isEmpty()) {
					for (String string : neighbors) {
						q.add(new Node(string, current, true));
					}
				}
			}
		}
		neighbors.clear();
		return neighbors;
	}

	/**
	 * Creates a dictionary set
	 * 
	 * @param no
	 *            parameters
	 * @return words Set with dictionary
	 */
	public static Set<String> makeDictionary() {
		Set<String> words = new HashSet<String>();
		Scanner infile = null;
		try {
			infile = new Scanner(new File("five_letter_words.txt"));
		} catch (FileNotFoundException e) {
			System.out.println("Dictionary File not Found!");
			e.printStackTrace();
			System.exit(1);
		}
		while (infile.hasNext()) {
			words.add(infile.next().toUpperCase());
		}
		return words;
	}

	/**
	 * Prints out word ladder or no word latter found
	 * 
	 * @param ladder
	 *            ArrayList with ladder
	 * @return No return value
	 */
	public static void printLadder(ArrayList<String> ladder) {
		if (ladder.isEmpty()) {
			System.out.println(
					"no word latter can be found between " + startWord.toLowerCase() + " and " + endWord.toLowerCase());
		} else {
			int rungs = ladder.size() - 2;
			System.out.println("a " + rungs + "-rung word ladder exists between " + startWord.toLowerCase() + " and "
					+ endWord.toLowerCase());
			for (String s : ladder) {
				System.out.println(s.toLowerCase());
			}
		}
	}

	/**
	 * Determines if words are neighbors (one letter difference)
	 * 
	 * @param a
	 *            first string
	 * @param b
	 *            second string
	 * @return boolean
	 */
	private static boolean oneDiff(String a, String b) {
		int count = 0;
		for (int i = 0; i < a.length(); i++) {
			if (a.charAt(i) != b.charAt(i)) {
				count++;
			}
		}
		if (count == 1) {
			return true;
		}
		return false;
	}
}
