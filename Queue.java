/*  
	Queue.java
	
	*Author: Benjamin Hogan
	*Student No: c3256846
	*Date: 29/05/2017
	*Descripion:
	*	Circular doubly linked list data structure for a Queue storing node<E>s. 
 */
import java.util.*;


public class Queue<E> implements Iterable<Node<E>>{

	private Node<E> sentinel;
	private int size, max, counter;
	private MyIterator iter = iterator();
	private double entry, exit, total, itemCounter;

	// Role:    creates a circular doubly linked list with a sentinel with 0 data.
    // Args:    none
	public Queue(int maxSize)
	{
		sentinel = new Node<E>();
		sentinel.setNext(sentinel);  // set the 'next' to itself
        sentinel.setPrev(sentinel);  // set the 'prev' to itself
		size = 0;
		max = maxSize;
	}

	// Role:    creates a new iterator for the list
    // Args:    none
    // Return:  the MyIterator constructor for a new iterator
	public MyIterator iterator()
	{
		return new MyIterator();
	}

		private class MyIterator implements Iterator<Node<E>>
		{
		 	private int expectedModCount;
		 	private Node<E> currPos;

		 	// Role:    sets the currPos to the sentinel and expected modcount to the size for iterating
		    // Args:    none
		 	private MyIterator()
		 	{
		 		currPos = sentinel;
		 		expectedModCount = size;
		 	}
		 	
		 	// Role:    Returns true if the next node isn't the sentinel node
		    // Args:    none
		    // Return:  boolean for if the list has a next
		 	public boolean hasNext()
		 	{
		 		return (currPos.getNext() != sentinel);
		 	}

		 	// Role:    Returns the next node in the iteration
		    // Args:    none
		    // Return:  Node in the next iteration
		 	public Node<E> next()
		 	{
		 		if(size != expectedModCount)
		 			throw new ConcurrentModificationException();
		 		if(!hasNext())
		 			throw new NoSuchElementException();

		 		currPos = currPos.getNext();
		 		return currPos;
		 	}

		 	public void remove()
		 	{
		 		//no implementation yet.
		 	}

		 	public Node<E> iterPosition()
		 	{
		 		return currPos;
		 	}

		 	public void insert()
		 	{
				expectedModCount++;
		 	}
		}

	// returns the size of the list.
	public int size()
	{
		return size;
	}

	// Role:    creates a node object with the data input of type E and adds to the head of the list.
    // Args:    Type E input of the object
    // Return:  none
	public void prepend(E input)
	{
		Node<E> insertNode = new Node<E>(input);
		if(size == 0)
		{
			sentinel.setNext(insertNode);
			sentinel.setPrev(insertNode);
			insertNode.setNext(sentinel);
			insertNode.setPrev(sentinel);
		}
		else
		{
			insertNode.setNext(sentinel.getNext());
			sentinel.getNext().setPrev(insertNode);
			sentinel.setNext(insertNode);
			insertNode.setPrev(sentinel);
		}
		size++;
		iter.insert();
	}

	// Role:    creates a node object with the data input of type E and adds to the tail of the list.
	// Args:    Type E input of the object, time for the queue statistics
	// Return:  none
	public void append(E input, double time)
	{
		Node<E> insertNode = new Node<E>(input);

		// if the queue is at max capacity then throw an exception.
		if(size == max)
			throw new NoSuchElementException();

		else if(size == 0)
		{
			sentinel.setNext(insertNode);
			sentinel.setPrev(insertNode);
			insertNode.setNext(sentinel);
			insertNode.setPrev(sentinel);
		}
		else
		{
			insertNode.setPrev(sentinel.getPrev());
			sentinel.getPrev().setNext(insertNode);
			sentinel.setPrev(insertNode);
			insertNode.setNext(sentinel);
		}
		size++;
		iter.insert();
		entry = time;
	}

	// Role:    creates a node object using the input given and adds it to the current position of the linked list.
    // Args:    Type E input of the object
    // Return:  none
	public void insert(E input)
	{
		Node<E> insertNode = new Node<E>(input);
		if(size == max)
			throw new NoSuchElementException();
		if(size == 0)
		{
			sentinel.setNext(insertNode);
			sentinel.setPrev(insertNode);
			insertNode.setNext(sentinel);
			insertNode.setPrev(sentinel);
		}
		else
		{
			Node<E> temp = iter.iterPosition();
			insertNode.setNext(temp);
			insertNode.setPrev(temp.getPrev());
			temp.getPrev().setNext(insertNode);
			temp.setPrev(insertNode);
		}
		size++;
		iter.insert();
	}

	// Role:    takes the head node<E> and removes it from the list. Returns the node<E> aswell.
    // Args:    double time for queue statistics
    // Return:  the node<E> at the head of the list.
	public Node<E> removeHead(double time)
	{
		Node<E> head;
		head = sentinel.getNext();

		if(size != 0)
		{
			sentinel.setNext(head.getNext());
			head.getNext().setPrev(sentinel);
			head.setNext(null);
			head.setPrev(null);

			size--;
			exit = time;
			total += exit - entry;
			counter++;
		}

		else
			{return null;}

		return head;
	}

	// Calls the iterator hasNext
	public boolean hasNext()
	{
		return iter.hasNext();
	}

	// Calls the iterator next
	public Node<E> nextIter()
	{
		return iter.next();
	}

	// Creates a new iterator
	public void createIterator()
	{
		iter = iterator();
	}

	// Role:    shows the avg time in the queue.
    // Args:    none
    // Return:  double value of the object queue time
	public double getAvgTime()
	{
		return total/counter;
	}

	// Role:    adds up the amount of items in the queue
    // Args:    none
    // Return:  double value of the object queue
    public void queueItems()
	{
		itemCounter+= size();
	}

	// returns the avg items in the queue divided by a counter.
	public double getQueueItems(int count)
	{
		return itemCounter/count;
	}

	// Role:    builds a string with all of the objects in the list.
    // Args:    none
    // Return:  string representation of each object or none if empty
	public String toString()
	{
		String output = "";
		iter = iterator();

		if(size != 0)
		{
			while(iter.hasNext())
				output += "\n" + iter.next().getData().toString();
		}
		else
			output = "\nThe list has no contents";

		return output;
	}
}