/*  
	PQ.java
	
	*Author: Benjamin Hogan
	*Student No: c3256846
	*Date: 29/05/2017
	*Descripion:
	*	An extension of Queue to hold an insertInOrder method for a Priority Queue
 */

import java.util.*;

public class PQ extends Queue<Object>{

	Station next, prev;

	//Calls the parent's Constructor.
	public PQ(int size)
	{
		super(size);
	}

    // Role:    sorts in order nodes of type Object into the PQ
    // Args:    Object to create a new node for the PQ
    // Return:  None
    public void insertInOrder(Object insertingNode)
    {
        if(size() == 0)
		{
			insert(insertingNode);
		}
        
        else{
	        Node<Object> intialNode = new Node<Object>(insertingNode);
	        boolean check = false;

	        //Creates iterator for the traversal process.
	        createIterator();

	        //checks for where the node should be inserted in the ordered list
	        for(int x = 0; x < (size()); x++)
	            {
	                Node<Object> compareNode = nextIter();
	                // Compares 2 Objects 
	                if(intialNode.getData().getTime() < compareNode.getData().getTime())
	                {
	                    insert(intialNode.getData());
	                    check = true;
	                    break;
	                }
	            }
	        if(check == false)
	        {
	            append(intialNode.getData(), 0);
	        }
    	}
    }
}