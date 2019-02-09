package edu.isu.cs.cs3308.structures.impl;

import edu.isu.cs.cs3308.structures.NodeDouble;

/**
 * A class to implement a doubly linked list based off the singly linked list class
 *
 * @author Aaron Harvey
 * @param <E> any type of list
 */
public class DoublyLinkedList<E> extends SinglyLinkedList<E> {

	/**
	 * Handles the logic for adding data to the list
	 * Checks if it will be the first node,
	 * Else if it will be the last node,
	 * Otherwise will insert at the appropriate spot
	 * Setting the head and tail accordingly
	 * Then at the end will increment the size, and
	 * Verify that the head and tail point to null in
	 * the prev and next respectively.
	 *
	 * @param dataToAdd The data to store within the node
	 * @param atIndex Index to add the node at
	 */
	@Override
	protected void nodeSetAdd(E dataToAdd, int atIndex) {
		NodeDouble<E> addNode = new NodeDouble<>(dataToAdd);

		if (atIndex == 0) {
			if (size > 0) {
				addNode.setNext(head);
				((NodeDouble<E>) head).setPrev(addNode);
			}
			head = addNode;
		}
		else if (atIndex >= size) {
			tail.setNext(addNode);
			addNode.setPrev((NodeDouble<E>) tail);
			tail = addNode;
		}
		else {
			NodeDouble<E> prevNode = getNode(atIndex-1);
			addNode.setNext(prevNode.getNext());
			addNode.setPrev(prevNode);
			((NodeDouble<E>) addNode.getNext()).setPrev(addNode);
			prevNode.setNext(addNode);
		}
		addSize();
		verifyList();
	}

	/**
	 * Handles the logic for removing data from the list
	 * Checks if it will be the first node,
	 * Else if it will be the last node,
	 * Otherwise will remove at the appropriate spot
	 * Setting the head and tail accordingly
	 * Then at the end will decrement the size, and
	 * Verify that the head and tail point to null in
	 * the prev and next respectively.
	 *
	 * @param atIndex Index of the node to remove
	 * @return The data within the node
	 */
	@Override
	protected E nodeSetRemove(int atIndex) {
		NodeDouble<E> removeNode = null;

		if (atIndex == 0) {
			removeNode = (NodeDouble<E>) head;
			if (size > 1) {
				head = removeNode.getNext();
			}
		}
		else if (atIndex >= size - 1) {
			NodeDouble<E> prevNode = ((NodeDouble<E>) tail).getPrev();
			removeNode = (NodeDouble<E>) tail;
			tail = prevNode;
		}
		else {
			NodeDouble<E> prevNode = getNode(atIndex-1);
			removeNode = (NodeDouble<E>) prevNode.getNext();
			((NodeDouble<E>) removeNode.getNext()).setPrev(prevNode);
			prevNode.setNext(removeNode.getNext());
		}
		removeNode.setPrev(null);
		removeNode.setNext(null);
		subSize();
		verifyList();

		return removeNode.getData();
	}

	/**
	 * Get a node from the list given a specific index
	 * Will first see if the index is in the first or
	 * last half of the list, if it is in the first half
	 * Then it will iterate forward from the head
	 * Else it will iterate backward from the tail
	 * Until it reaches the desired index.
	 *
	 * @param index The index within the list
	 * @return The node retrieved from the list
	 */
	@Override
	protected NodeDouble<E> getNode(int index) {
		NodeDouble<E> seekNode;

		if (index <= size/2) {
			seekNode = (NodeDouble<E>) head;
			for (int i = 0; i < index; i++) {
				seekNode = (NodeDouble<E>) seekNode.getNext();
			}
		}
		else {
			seekNode = (NodeDouble<E>) tail;
			for (int i = size-1; i > index; i--) {
				seekNode = seekNode.getPrev();
			}
		}

		return seekNode;
	}

	/**
	 * This allows making a circular list easy.
	 * Will verify the head and tail goes to null in the
	 * prev and next of the nodes respectively
	 */
	@Override
	protected void verifyBoundary() {
		((NodeDouble<E>) head).setPrev(null);
		tail.setNext(null);
	}
}
