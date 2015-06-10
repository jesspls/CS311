/**
 * Implementation of interface ILinkedList.
 * 
 * @author Jessica Haynes
 * @param <E>
 */

public class SinglyLinkedList<E> implements ILinkedList<E> {

	SinglyNode<E> head;
	SinglyNode<E> tail;

	@Override
	public void add(E data) {

		SinglyNode<E> toAdd = new SinglyNode<E>(data);
		if (tail == null)
			tail = toAdd;
		toAdd.next = head;
		head = toAdd;

	}

	@Override
	public void add(int pos, E data) throws Exception {

		if (pos >= this.size())
			throw new Exception("No such index");
		else {
			SinglyNode<E> toAdd = new SinglyNode<E>(data);
			SinglyNode<E> previous = head;

			//Handles inserting at head
			if (pos == 0) {
				SinglyNode<E> next = previous;
				head = toAdd;
				head.next = previous;
			}
			

			//Handles inserting in the middle
			else {
				for (int i = 0; i < pos - 1; i++) {
					previous = previous.next;
				}

				SinglyNode<E> next = previous.next;
				previous.next = toAdd;
				toAdd.next = next;
			}
			
			//No NEED for inserting at tail from my understanding. To insert at tail your entered position would
			//have to be +1 the current positions, which does not exist so does not meet requirements.

		}

	}

	@Override
	public boolean contains(E data) {

		SinglyNode<E> curr = head;
		while (curr != null) {
			if (curr.data == data)
				return true;
			curr = curr.next;
		}
		return false;

	}

	@Override
	public E get(int pos) throws Exception {

		if (pos >= this.size())
			throw new Exception("Out of bounds");
		else {
			SinglyNode<E> previous = head;
			for (int i = 0; i < pos; i++) {
				previous = previous.next;
			}

			return previous.data;
		}

	}

	@Override
	public int indexOf(E data) throws Exception {

		int index = 0;
		SinglyNode<E> curr = head;
		while (index < this.size()) {
			if (curr.data == data) {
				return index;
			} else {
				curr = curr.next;
				index++;
			}
		}
		
		throw new Exception("Not found");

	}

	@Override
	public boolean isEmpty() {

		if (head == null)
			return true;
		else
			return false;

	}

	@Override
	public void remove(E data) throws Exception {

		int index = 0;
		SinglyNode<E> curr = head;
		SinglyNode<E> prev = head;
		while (index < this.size()) {
			if (curr.data == data) {
				// Handles removes the first element
				if (curr == head)
					head = curr.next;
				// Handles removing the last element
				else if (curr.data == tail)
					tail = prev;
				// Handles removing an element from the middle of the list
				else
					prev.next = curr.next;
				return;
			} else {
				prev = curr;
				curr = curr.next;
				index++;
				// If we have gone out of bounds, then throws not found
				// exception
				if (index >= this.size())
					throw new Exception("Not found");
			}
		}

	}

	@Override
	public void set(int pos, E data) throws Exception {

		if (pos >= this.size())
			throw new Exception("Out of bounds");
		else {
			SinglyNode<E> curr = head;
			for (int i = 0; i < pos; i++) {
				curr = curr.next;
			}
			curr.data = data;
		}

	}

	@Override
	public int size() {

		int size = 0;
		if (this.isEmpty())
			return size;
		for (SinglyNode<E> n = head; n.next != null; n = n.next)
			size++;
		return size + 1;

	}

	@Override
	public String toString() {

		String toPrint = "";
		SinglyNode current = head;
		if (this.isEmpty())
			return "<EMPTY>";
		while (current != null) {
			toPrint += current.data + " ";
			current = current.next;
		}
		return toPrint;

	}

}
