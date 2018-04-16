/*  
	Singleton.java
	
	*Author: Benjamin Hogan
	*Student No: c3256846
	*Date: 29/05/2017
	*Descripion:
	*	Simple node class containing next, prev pointers and data for the node.
 */

import java.util.*;

public class Singleton{

	private int id = 1;
	private static Singleton instance = null;

	// Role:    protected constructor so only sub classes can access it
    // Args:    None
	protected Singleton()
	{}

	// Role:    The purpose is to control object creation, limiting the number of objects to only one.
    // Args:    None 
    // Return:  A new Singleton object 
	public static Singleton getInstance()
	{
		if(instance == null) 
		{
	    	instance = new Singleton();
	 	}
	    return instance;
	}

	// Role:    increments the id by one each time its called to ensure the same id isn't returned twice
    // Args:    None 
    // Return:  The next int id in for the object creation 
	public int getID()
	{
		return id++;
	}
}