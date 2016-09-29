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

/**
 * Node --- Contains nodes' information.
 */
public class Node {
	String word;
	Node parent;
	boolean queued;

	public Node(String word, Node parent, boolean queued) {
		this.word = word;
		this.parent = parent;
		this.queued = queued;
	}
}
