/*  
	Object.java
	
	*Author: Benjamin Hogan
	*Student No: c3256846
	*Date: 29/05/2017
	*Descripion:
	*	Class for storing data related to an Object in a production line.
 */

import java.util.*;

public class Object{

	private int id;
	private double time;

	// Role:    creates a new object with id and time set
    // Args:    int ID and double time for the production stages
	public Object(int objectID, double timeset)
	{
		id = objectID;
		time = timeset;
	}

	// Role:    builds a string with details of the object.
    // Args:    none
    // Return:  string representation of the object
	public String toString()
	{
		String output = "Station ID: " + Integer.toString(id) + ", time: " + Double.toString(time);

		return output;
	}

	// sets the id of the object
	public void setID(int digit)
	{
		id = digit;
	}

	// returns the id of the object
	public int getID()
	{
		return id;
	}

	// sets the time for that stage production in the object
	public void setTime(double production)
	{
		time = production;
	}

	// returns the stage production time
	public double getTime()
	{
		return time;
	}
}