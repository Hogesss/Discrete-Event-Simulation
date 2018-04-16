/*  
	Station.java
	
	*Author: Benjamin Hogan
	*Student No: c3256846
	*Date: 29/05/2017
	*Descripion:
	*	Contains all methods and data for a station on a production line including blocking and starving times.
 */

import java.util.*;

public class Station{

	private Object current;
	private Queue<Object> prev, next;
	private boolean full, blocked;
	private double starvetime, blockedtime, blockedEntry, blockedExit, starvedEntry, starvedExit;
	private int mean, range, id, processed;

	
	// Role:    creates a new station with data equal to null or false
    // Args:    None
	public Station()
	{
		current = null;
		prev = null;
		next = null;
		full = false;
		blocked = false;
		id = 0;
	}

	// Role:    creates a new station with data equal to null or false
    // Args:    Queue<Object> prevQ and nextQ for queues either side of the station, int mean, range and id for the stage
	public Station(Queue<Object> prevQ, Queue<Object> nextQ, int tmean, int trange, int stage)
	{
		current = null;
		prev = prevQ;
		next = nextQ;
		mean = tmean;
		range = trange;
		id = stage;
	}

	// returns the queue next to the station
	public Queue<Object> getNext()
	{
		return next;
	}

	// returns the queue to the left of the station
	public Queue<Object> getPrev()
	{
		return prev;
	}

	// returns the stations int id to see which station it is
	public int getStageID()
	{
		return id;
	}

	// Role:    removes the product from the station when its done with production and increases the processed counter
    // Args:    None
	public void removeProduct()
	{
		current = null;
		full = false;
		processed++;
	}

	// Role:    sets the new product for the stage and sets full to true
    // Args:    None
	public void setProduct(Object nextObject)
	{
		current = nextObject;
		full = true;
	}

	// returns the current object in production
	public Object getProduct()
	{
		return current;
	}

	// returns the boolean value for if the station isFull
	public boolean isFull()
	{
		return full;
	}

	// returns the boolean value for if the station isBlocked
	public boolean isBlocked()
	{
		return blocked;
	}

	// Role:    Sets the station in a blocked stage and uses the time for entry and exit to work out the blocked time
    // Args:    boolean value for blocked or not, double time for blocked statistics
    // Return:  None 
	public void setBlocked(boolean value, double time)
	{
		// if station is already blocked then no need to change anything
		if(blocked != value)
		{
			blocked = value;
			if(blocked)
			{
				blockedEntry = time;
			}
			else 
			{
				blockedExit = time;
				blockedtime+= blockedExit - blockedEntry;
			}
		}
	}

	// Role:    Sets the station in a starved stage and uses the time for entry and exit to work out the starve time
    // Args:    boolean value for starved or not, double time for starved statistics
    // Return:  None 
	public void setFull(boolean value, double time)
	{
		full = value;
		if(!full)
		{
			starvedEntry = time;
		}
		else
		{
			starvedExit = time;
			starvetime += starvedExit - starvedEntry;
		}
	}

	// returns the blocked time for the station
	public double getBlockedTime()
	{
		return blockedtime;
	}

	// returns the starved time for the station
	public double getStarvedTime()
	{
		return starvetime;
	}

	// Role:    gets the production as a percentage of the blocked time and starve time
    // Args:    None
    // Return:  double value of the production percentage. 
	public double getProduction()
	{
		return ((10000000 - blockedtime - starvetime)/10000000) * 100;
	}

	// returns the number of objects processed in the station
	public int getProcessed()
	{
		return processed;
	}

	// Role:    sets the production time for the object currently in the station
    // Args:    double random value for the object
    // Return:  returns the double production time 
	public double productionTime(double random)
	{
		double p = mean + (range * (random - 0.5));
		return p;
	}
}