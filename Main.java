/* WORD LADDER Main.java
 * EE422C Project 3 submission by
 * Replace <...> with your actual data.
 * Manuel Gomez	
 * mlg3454
 * <Student1 5-digit Unique No.>
 * Don Ton
 * dt22776
 * <Student2 5-digit Unique No.>
 * Slip days used: <0>
 * Git URL:
 * Fall 2016
 */

package assignment3;

import java.util.*;
import java.io.*;

public class Main {

	public static void main(String[] args) throws Exception {

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
		initialize();
		printLadder(getWordLadderDFS(first, last));
		//printLadder(getWordLadderBFS(first, last));

		// TODO methods to read in words, output ladder
	}

	public static void initialize() {
		// initialize your static variables or constants here.
		// We will call this method before running our JUNIT tests. So call it
		// only once at the start of main.

	}

	/**
	 * @param keyboard
	 *            Scanner connected to System.in
	 * @return ArrayList of 2 Strings containing start word and end word. If
	 *         command is /quit, return empty ArrayList.
	 */
	public static ArrayList<String> parse(Scanner keyboard) {
		String input;
		String[] words;
		ArrayList<String> list = new ArrayList<String>();

		input = ((keyboard.nextLine()).trim()).toUpperCase();
		if ("/quit".equals(input)) {
			return list;
		}
		words = input.split(" +");
		list.add(words[0]);
		list.add(words[1]);
		return list;
	}

	// Returned list should be ordered start to end. Include start and end.
	// Return empty list if no ladder.
	public static ArrayList<String> getWordLadderDFS(String start, String end) {
		start = start.toUpperCase();
		end = end.toUpperCase();
		Set<String> dict = makeDictionary();
		/*
		 * Set<String> dict = new HashSet<String>(); dict.add("HUMOR");
		 * dict.add("RUMOR"); dict.add("TUMOR"); dict.add("HUNOR");
		 * dict.add("HONOR");
		 */
		Set<String> set = new HashSet<String>();
		ArrayList<String> ladder = new ArrayList<String>();

		ladder = helperDFS(start, end, set, dict, ladder);
		return ladder;
	}

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
			return null;
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
		return null;

	}

	/*
	 * public static boolean findDFS(String start, String end, Set<String> set,
	 * Set<String> dict) { if (findNext(start, set, dict)) { return false; }
	 * set.add(start); if (start.equals(end)) { return true; } else { for
	 * (String string : dict) { if (!set.contains(string)){ boolean found =
	 * findDFS(string, end, set, dict); if(found){ return true; } } } return
	 * false; } }
	 * 
	 * public static boolean findNext(String start, Set<String> set, Set<String>
	 * dict) { for (String string : dict) { if (!set.contains(oneDiff(start,
	 * string))) { return true; } } return false; }
	 */

	public static ArrayList<String> getWordLadderBFS(String start, String end) {
		start = start.toUpperCase();
		end = end.toUpperCase();
		Queue<Node> q = new LinkedList<Node>();
		ArrayList<String> neighbors = new ArrayList<String>();
		Set<String> dict = makeDictionary();
		/*
		 * Set<String> dict = new HashSet<String>(); dict.add("SMART");
		 * dict.add("START"); dict.add("STARS");
		 */

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

		// replace this line later with real return
	}

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

	public static void printLadder(ArrayList<String> ladder) {
		for (String s : ladder) {
			System.out.println(s);
		}
	}
	// TODO
	// Other private static methods here

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

	private static ArrayList<String> allOneDiff(String word, Set<String> dict) {
		ArrayList<String> possPath = new ArrayList<String>();
		for (String string : dict) {
			if (oneDiff(word, string)) {
				possPath.add(string);
			}
		}
		return possPath;
	}
}
