package edu.isu.cs.cs3308.structures;

/**
 * Node class needed for the Double List
 *
 * @author Aaron Harvey
 * @param <E>
 */
public class NodeDouble<E> extends Node<E>{

	private NodeDouble<E> prev;

	/**
	 * Constructor with data parameter
	 * @param data Whatever data the Node should store
	 */
	public NodeDouble(E data) {
		super(data);
	}

	/**
	 * Get what Node is stored as the prev in the List.
	 * @return The Node that is currently stored in the prev attribute
	 */
	public NodeDouble<E> getPrev() {
		return prev;
	}

	/**
	 * Set what Node should be stored as the prev in the List.
	 * @param prev The Node that should be the previous to current node
	 */
	public void setPrev(NodeDouble<E> prev) {
		this.prev = prev;
	}
}
