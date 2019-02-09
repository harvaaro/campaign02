package edu.isu.cs.cs3308.structures.impl;

import edu.isu.cs.cs3308.structures.List;
import edu.isu.cs.cs3308.structures.Node;

/**
 * A class to implement a singly linked list based off the List class
 *
 * @author Aaron Harvey
 * @param <E> any type of list
 */
public class SinglyLinkedList<E> implements List<E> {
	protected Node<E> head = null;
	protected Node<E> tail = null;
	protected Node<E> tempNode = null;
	protected int size = 0;

	/**
	 * Checks to see if a given element is null or not
	 *
	 * @param element the element to check
	 * @return true if not null, and false if null
	 */
	protected boolean checkElement(E element) {
		return element != null;
	}

	/**
	 * Checks to see if a given index is within 0 and size
	 *
	 * @param index the index to check
	 * @return true if valid index, and false if invalid
	 */
	protected boolean checkIndex(int index) {
		return index < size && index >= 0;
	}

	/**
	 * Used to fix the head and tail of the list
	 * If a single node remains in the list, then head and tail are set to it
	 * If the size of the list is 0 then set head and tail to null
	 * Then if the list has size, it will verify the head and tail
	 */
	protected void verifyList() {
		if (size == 1) {
			if (head != null) {
				tail = head;
			}
			else {
				head = tail;
			}
		}

		if (size == 0) {
			head = null;
			tail = null;
		}

		if (size > 0) {
			verifyBoundary();
		}
	}

	/**
	 * Will set the Node values appropriately for an add action
	 *
	 * @param dataToAdd The data to store within the node
	 * @param atIndex Index to add the node at
	 */
	protected void nodeSetAdd(E dataToAdd, int atIndex) {
		Node<E> addNode = new Node<>(dataToAdd);

		if (atIndex == 0) {
			if (size > 0) {
				addNode.setNext(head);
			}
			head = addNode;
		}
		else if (atIndex >= size) {
			tail.setNext(addNode);
			tail = addNode;
		}
		else {
			Node<E> prevNode = getNode(atIndex-1);
			addNode.setNext(prevNode.getNext());
			prevNode.setNext(addNode);
		}
		addSize();
		verifyList();
	}

	/**
	 * Will set the Node values appropriately for a remove action
	 *
	 * @param atIndex Index of the node to remove
	 * @return The data within the node
	 */
	protected E nodeSetRemove(int atIndex) {
		Node<E> removeNode = null;

		if (atIndex == 0) {
			removeNode = head;
			if (size > 1) {
				head = removeNode.getNext();
			}
		}
		else {
			Node<E> prevNode = getNode(atIndex-1);
			removeNode = prevNode.getNext();
			prevNode.setNext(removeNode.getNext());
			if (atIndex >= size-1) {
				tail = prevNode;
			}
		}
		removeNode.setNext(null);
		subSize();
		verifyList();

		return removeNode.getData();
	}

	/**
	 * Get a node from the list given a specific index
	 * Will iterate forward from the head until the index
	 *
	 * @param index The index within the list
	 * @return The node retrieved from the list
	 */
	protected Node<E> getNode(int index) {
		Node<E> seekNode = head;

		for (int i = 0; i < index; i++) {
			seekNode = seekNode.getNext();
		}

		return seekNode;
	}

	/**
	 * Adds 1 to the size value.
	 * I put this as a function if I ever needed to do other checks with it
	 */
	protected void addSize() {
		size++;
	}

	/**
	 * Subtracts 1 from the size value, and ensure it cannot go below 0
	 */
	protected void subSize() {
		size--;

		if (size < 0) {
			size = 0;
		}
	}

	/**
	 * This allows making a circular list easy.
	 * Will verify the tail goes to null in the
	 * next of the node.
	 */
	protected void verifyBoundary() {
		tail.setNext(null);
	}

	/**
	 * To get the data from the first node in the list
	 *
	 * @return The data within the head node, if none then null
	 */
	@Override
	public E first() {
		return (head != null) ? head.getData() : null;
	}

	/**
	 * To get the data from the last node in the list
	 *
	 * @return The data within the tail node, if none then null
	 */
	@Override
	public E last() {
		return (tail != null) ? tail.getData() : null;
	}

	/**
	 * Creates a node with the given element data to the end of the list
	 *
	 * @param element Data to store in the last node
	 */
	@Override
	public void addLast(E element) {
		if (checkElement(element)) {
			if (!isEmpty()) {
				nodeSetAdd(element,size);
			}
			else {
				addFirst(element);
			}
		}
	}

	/**
	 * Creates a node with the given element data to the beginning of the list
	 *
	 * @param element Data to store in the first node
	 */
	@Override
	public void addFirst(E element) {
		if (checkElement(element)) {
			nodeSetAdd(element, 0);
		}
	}

	/**
	 * Removes the first node in the list
	 *
	 * @return Data stored within the node that is to be removed
	 */
	@Override
	public E removeFirst() {
		if (head != null) {
			return nodeSetRemove(0);
		}
		else {
			return null;
		}
	}

	/**
	 * Removes the last node in the list
	 *
	 * @return Data stored within the node that is to be removed
	 */
	@Override
	public E removeLast() {
		if (size > 1) {
			return remove(size - 1);
		}
		else {
			return removeFirst();
		}
	}

	/**
	 * Create a node with the given element data, and insert that node
	 * into the list at the given index
	 *
	 * @param element Data to store in the node
	 * @param index Where in the list the node should be inserted
	 */
	@Override
	public void insert(E element, int index) {
		if (checkElement(element)) {
			if (index >= 0) {
				if (index == 0) {
					addFirst(element);
				}
				else if (index >= size) {
					addLast(element);
				}
				else {
					nodeSetAdd(element, index);
				}
			}
		}
	}

	/**
	 * Removes a given node from the list based on a given index
	 *
	 * @param index The index of the node in the list to remove
	 * @return Data stored within the node that is to be removed
	 */
	@Override
	public E remove(int index) {
		if (checkIndex(index)) {
			if (index == 0) {
				return removeFirst();
			}
			else {
				return nodeSetRemove(index);
			}
		}
		else {
			return null;
		}
	}

	/**
	 * Get the data within a node from the list at the given index
	 *
	 * @param index The index of the node in the list to retrieve
	 * @return Data stored within the node that is to be retrieved
	 */
	@Override
	public E get(int index) {
		if (checkIndex(index)) {
			if (index == 0) {
				return head.getData();
			}
			else if (index == size-1) {
				return tail.getData();
			}
			else {
				return getNode(index).getData();
			}
		}
		else {
			return null;
		}
	}

	/**
	 * The stored value which contains the number of nodes within the list
	 *
	 * @return The count of nodes in the list
	 */
	@Override
	public int size() {
		return size;
	}

	/**
	 * Determines if the list is empty or not
	 *
	 * @return True if the list is empty, and false if size does not equal 0
	 */
	@Override
	public boolean isEmpty() {
		return size == 0;
	}

	/**
	 * Each value in the list will be printed out with a defined newline between them
	 * except on the last value, which just ends with a newline after it
	 */
	@Override
	public void printList() {
		if (!isEmpty()) {
			tempNode = head;

			for (int i = 0; i < size; i++) {
				if (i < size-1) {
					System.out.print(tempNode.getData() + "\n");
				}
				else {
					System.out.println(tempNode.getData());
				}
				tempNode = tempNode.getNext();
			}
		}
		else {
			System.out.println("There is nothing in this list.");
		}
		tempNode = null;
	}

//	/**
//	 * Finds a given item in the list, if not found or null then return -1
//	 *
//	 * @param item item to find in the list
//	 * @return the index of the found item in the list
//	 */
//	@Override
//	public int indexOf(E item) {
//		if (!isEmpty() && item != null) {
//			if (head.getData() == item) {
//				return 0;
//			}
//			else if (tail.getData() == item) {
//				return size-1;
//			}
//			else {
//				tempNode = head;
//
//				for (int i = 0; i < size; i++) {
//					if (tempNode.getData() == item) {
//						return i;
//					}
//					tempNode = tempNode.getNext();
//				}
//			}
//		}
//		tempNode = null;
//
//		return -1;
//	}
}
