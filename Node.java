package assignment3;

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
