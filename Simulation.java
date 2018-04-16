/*  
    Simulation.java
    
    *Author: Benjamin Hogan
    *Student No: c3256846
    *Date: 29/05/2017
    *Descripion:
    *   Class that runs the simulation and outputs all the data needed for the production line
 */

import java.util.*;
import java.text.*;

public class Simulation{
    
    private int station, counter;
    private Object tempObject;
    private double simulationTime = 0, tempProduction;
    private Singleton singleton = Singleton.getInstance();
    private Random r = new Random();
    private boolean finished = false;

    private PQ time = new PQ(10);
    private Station s0A, s0B, s1, s2, s3A, s3B, s4, s5A, s5B, s6;
    private Queue<Object> q01, q12, q23, q34, q45, q56;

    // Role:    runs the simulation for 10000000 time units and calls updateStages in a while loop.
    // Args:    int mean, range and max queue value for the simulation
    // Return:  None
    public Simulation(int mean, int range, int max)
    {
        setup(mean, range, max);

        do{
            updateStages();
        }while(simulationTime < 10000000);

        finished = true;
        // removes the remainder of the objects caught in the production when time stops
        emptyProductionLine();
    }

    // Role:    Sets up the production line with new queues and stations and creates objects in s0a and s0b
    // Args:    int mean, range and max queue value for the simulation 
    // Return:  None
    private void setup(int mean, int range, int qMax)
    {
        q01 = new Queue<Object>(qMax);
        q12 = new Queue<Object>(qMax);
        q23 = new Queue<Object>(qMax);
        q34 = new Queue<Object>(qMax);
        q45 = new Queue<Object>(qMax);
        q56 = new Queue<Object>(qMax);
        // NEW Queues with max size

        s0A = new Station(null, q01, 2*mean, 2*range, 0);
        s0B = new Station(null, q01, mean, range, 1);
        s1 = new Station(q01, q12, mean, range, 2);
        s2 = new Station(q12, q23, mean, range, 3);
        s3A = new Station(q23, q34, 2*mean, 2*range, 4);
        s3B = new Station(q23, q34, 2*mean, 2*range, 5);
        s4 = new Station(q34, q45, mean, range, 6);
        s5A = new Station(q45, q56, 2*mean, 2*range, 7);
        s5B = new Station(q45, q56, 2*mean, 2*range, 8);
        s6 = new Station(q56, null, mean, range, 9);
        // NEW STATIONS with next and prev queues. 

        // create objects in stations for updateStages to start.
        createObject(s0A, 0);
        createObject(s0B, 1);
        // any additional new creation stages for the start process
    }

    // Role:    The production line in the simulation. Removes the object from the top of the time PQ and gets its time, increases the simulation time, then locates which station the object came from. Then completes production on that stage and checks stations either side if they are blocked or starved to continue the flow of the simulation.
    // Args:    None 
    // Return:  None
    private void updateStages()
    {
        // gets the object next in the PQ and gets the station it originated from
        tempObject = time.removeHead(0).getData();
        station = tempObject.getID();
        
        // edits the rest of the objects in the time priority queue and increments time.
        editTime(tempObject.getTime());
        simulationTime += tempObject.getTime();

        //S0A and S0B
        if(station == 0 || station == 1) 
        {
            try{
                q01.append(tempObject, simulationTime);
                if(station == 0)    // S0A
                    createObject(s0A, 0); 
                
                else    // S0B
                    createObject(s0B, 1); 
            }catch(NoSuchElementException e)// catches the exception and sets the station as blocked
            { 
                if(station == 0)
                    setBlocked(s0A, simulationTime);
                else
                    setBlocked(s0B, simulationTime);
            }
                //checks for starved station 1
                checkStarved(s1);
        }
        
        // when the time is up, doesn't create more objects
        if(finished)
        {
            try{
                if(station == 0)    // S0A
                    finishProduct(s0A); 
                if(station == 1)    // S0B
                    finishProduct(s0B);

            }catch(NoSuchElementException e) // catches the exception and sets the station as blocked
            { 
                if(station == 0)
                    setBlocked(s0A, simulationTime);
                if(station == 1)
                    setBlocked(s0B, simulationTime);
            }
                //checks for starved station 1
                checkStarved(s1);
        }

        if(station == 2) // S1
        {
            try{
                finishProduct(s1);
            }catch(NoSuchElementException e) // catches the exception and sets the station as blocked
            {
                setBlocked(s1, simulationTime);
            }
            // checks stations either side for blocked or starved
            checkStarved(s2);
            if(!finished)
                {checkBlocked(s0B);
                checkBlocked(s0A);}
        }

        if(station == 3) // S2
        {
            try{
                finishProduct(s2);
            }catch(NoSuchElementException e) // catches the exception and sets the station as blocked
            {
                setBlocked(s2, simulationTime);
            }
            // checks stations either side for blocked or starved
            checkStarved(s3A);
            checkStarved(s3B);
            checkBlocked(s1);
        }

        if(station == 4) // S3A
        {
            try{
                finishProduct(s3A);
            }catch(NoSuchElementException e) // catches the exception and sets the station as blocked
            {
                setBlocked(s3A, simulationTime);
            }
            // checks stations either side for blocked or starved
            checkStarved(s4);
            checkBlocked(s2);
        }

        if(station == 5) // S3B
        {
            try{
                finishProduct(s3B);
            }catch(NoSuchElementException e) // catches the exception and sets the station as blocked
            {
                setBlocked(s3B, simulationTime);
            }
            // checks stations either side for blocked or starved
            checkStarved(s4);
            checkBlocked(s2);
        }

        if(station == 6) // S4
        {
            try{
                finishProduct(s4);
            }catch(NoSuchElementException e) // catches the exception and sets the station as blocked
            {
                setBlocked(s4, simulationTime);
            }
            // checks stations either side for blocked or starved
            checkStarved(s5A);
            checkStarved(s5B);
            checkBlocked(s3A);
            checkBlocked(s3B);
        }

        if(station == 7) // S5A
        {
            try{
                finishProduct(s5A);
            }catch(NoSuchElementException e) // catches the exception and sets the station as blocked
            {
                setBlocked(s5A, simulationTime);
            }
            // checks stations either side for blocked or starved
            checkStarved(s6);
            checkBlocked(s4);
        }

        if(station == 8) // S5B
        {
            try{
                finishProduct(s5B);
            }catch(NoSuchElementException e) // catches the exception and sets the station as blocked
            {
                setBlocked(s5B, simulationTime);
            }
            // checks stations either side for blocked or starved
            checkStarved(s6);
            checkBlocked(s4);
        }

        if(station == 9) // S6
        {
            // s6 simply finishes the object and gets rid of it, doesn't get blocked
            finishProduct(s6);
            // checks stations behind for blocked
            checkBlocked(s5A);
            checkBlocked(s5B);
        }

        // checks the amount of items in each queue at each update, used for the avg item statistic
        q01.queueItems(); q12.queueItems(); q23.queueItems(); q34.queueItems();
        q45.queueItems(); q56.queueItems();
        counter++;
    }

    // Role: Removes time from the other events inside the time queue based on the head of the queue
    // Preconditions: The head of time queue has been removed and the difference is the head object production time.
    // Args:    double time difference to the other times 
    // Return:  None
    private void editTime(double difference)
    {
        time.createIterator();

        for(int x = 0; x < time.size(); x++)
        {
            Object temp = time.nextIter().getData();

            if(temp.getTime() != 0)
                temp.setTime(temp.getTime() - difference);
        }
    }

    // Role:    Stages s0A and s0B create objects with a new id from the singleton object and inserts it into the time PQ
    // Args:    Station for the object to be created, and int stage type
    // Return:  None
    private void createObject(Station s00, int type)
    {
        // removes the current product and gets a new production time for the stage
        s00.removeProduct();
        tempProduction = s00.productionTime(r.nextDouble());
        if(type == 0) // S0A
        {
            tempObject = new Object(singleton.getID()*10, tempProduction);
            s00.setProduct(tempObject);
            // sets the object to 0 for station S0A
            tempObject.setID(0);
        }
        else // S0B
        {
            tempObject = new Object(singleton.getID()*10 + 1, tempProduction);
            s00.setProduct(tempObject);
            // sets the object to 1 for station S0B
            tempObject.setID(1);
        }
        // inserts the new object into the queue
        time.insertInOrder(tempObject);
    }

    // Role:    checks if a station is starving by checking if its full then trying to get an object from the previous queue to fill it up
    // Args:    the Station to check 
    // Return:  None
    private void checkStarved(Station stage)
    {
        if(!stage.isFull()) // if stage is currently empty
        {
            try{
                // gets the object off the previous queue and sets a new production time
                tempObject = stage.getPrev().removeHead(simulationTime).getData();
                tempObject.setTime(stage.productionTime(r.nextDouble()));
                
                // fills up the stage and sets the id for the PQ
                stage.setProduct(tempObject);
                stage.setFull(true, simulationTime);
                tempObject.setID(stage.getStageID());

                // inserts the object into the PQ
                time.insertInOrder(tempObject);
            }catch(NullPointerException e){ /* If there is a Null Exception from removeHead(), then the station is still starving */ }
        }
    }

    // Role:    Finishes the current object in the station and sends it to the next station in the line. 
    // Args:    The station that the object will finish 
    // Return:  None
    private void finishProduct(Station stage)
    {
        if(stage != s6)
            stage.getNext().append(tempObject, simulationTime); // sends the finished product onto the next stage
        
        stage.removeProduct();
        stage.setFull(false, simulationTime); // station is starving

        try{
            // trys to get a product from the previous queue and sets a new production time
            tempObject = stage.getPrev().removeHead(simulationTime).getData();
            tempObject.setTime(stage.productionTime(r.nextDouble()));

            // fills up the stage and sets the id for the PQ
            stage.setProduct(tempObject);
            stage.setFull(true, simulationTime);
            tempObject.setID(stage.getStageID());
            
            // inserts the object into the PQ
            time.insertInOrder(tempObject);
        }catch(NullPointerException e){ /* If there is a Null Exception from removeHead(), then the station is still starving */ } 
    }

    // Role:    Sets the stage as blocked and gets the time for blocked statistics
    // Args:    Station for the blocked stage, and double time 
    // Return:  None
    private void setBlocked(Station stage, double time)
    {
        stage.setBlocked(true, time);
    }

    // Role:    checks if the station is blocked, and sets it to unblocked and puts it back in the PQ since the station infront just finished so there must be a spot on the queue.
    // Args:    Station that is blocked
    // Return:  None
    private void checkBlocked(Station stage)
    {
        if(stage.isBlocked())
        {
            // sets the stage as unblocked
            stage.setBlocked(false, simulationTime);
            tempObject = stage.getProduct();
            // sets the time as 0 and puts it back in the PQ to be quickly updated in the production line
            if(tempObject != null)
                {tempObject.setTime(0);
                tempObject.setID(stage.getStageID());
                time.insertInOrder(tempObject);}
        }
    }

    // Role:    Runs at the end of the time period to release all the stages and queues of their objects without creating more.
    // Args:    None 
    // Return:  None
    private void emptyProductionLine()
    {
        do{
            updateStages();
        }while(time.size()!= 0);
    }

    // Role:    Prints all the program data for the simulation
    // Args:    None 
    // Return:  None
    public void output()
    {
        NumberFormat form = new DecimalFormat("#0.00");
        
        // Stage data //
        System.out.format("%10s%20s%20s%20s\n", "Stage","Production(%)", "Starving Time", "Blocking Time");

        System.out.format("%10s%20s%20s%20s\n", "S0A", form.format(s0A.getProduction()), form.format(s0A.getStarvedTime()), form.format(s0A.getBlockedTime()));
        System.out.format("%10s%20s%20s%20s\n", "S0B", form.format(s0B.getProduction()), form.format(s0B.getStarvedTime()), form.format(s0B.getBlockedTime()));
        System.out.format("%10s%20s%20s%20s\n", "S1", form.format(s1.getProduction()), form.format(s1.getStarvedTime()), form.format(s1.getBlockedTime()));
        System.out.format("%10s%20s%20s%20s\n", "S2", form.format(s2.getProduction()), form.format(s2.getStarvedTime()), form.format(s2.getBlockedTime()));
        System.out.format("%10s%20s%20s%20s\n", "S3A", form.format(s3A.getProduction()), form.format(s3A.getStarvedTime()), form.format(s3A.getBlockedTime()));
        System.out.format("%10s%20s%20s%20s\n", "S3B", form.format(s3B.getProduction()), form.format(s3B.getStarvedTime()), form.format(s3B.getBlockedTime()));
        System.out.format("%10s%20s%20s%20s\n", "S4", form.format(s4.getProduction()), form.format(s4.getStarvedTime()), form.format(s4.getBlockedTime()));
        System.out.format("%10s%20s%20s%20s\n", "S5A", form.format(s5A.getProduction()), form.format(s5A.getStarvedTime()), form.format(s5A.getBlockedTime()));
        System.out.format("%10s%20s%20s%20s\n", "S5B", form.format(s5B.getProduction()), form.format(s5B.getStarvedTime()), form.format(s5B.getBlockedTime()));
        System.out.format("%10s%20s%20s%20s\n\n", "S6", form.format(s6.getProduction()), form.format(s6.getStarvedTime()), form.format(s6.getBlockedTime()));

        // Queue data //
        System.out.format("%10s%20s%20s\n", "Queue","Avg Time", "Avg Items");

        System.out.format("%10s%20s%20s\n", "Q01", form.format(q01.getAvgTime()), form.format(q01.getQueueItems(counter)));
        System.out.format("%10s%20s%20s\n", "Q12", form.format(q12.getAvgTime()), form.format(q12.getQueueItems(counter)));
        System.out.format("%10s%20s%20s\n", "Q23", form.format(q23.getAvgTime()), form.format(q23.getQueueItems(counter)));
        System.out.format("%10s%20s%20s\n", "Q34", form.format(q34.getAvgTime()), form.format(q34.getQueueItems(counter)));
        System.out.format("%10s%20s%20s\n", "Q45", form.format(q45.getAvgTime()), form.format(q45.getQueueItems(counter)));
        System.out.format("%10s%20s%20s\n", "Q56", form.format(q56.getAvgTime()), form.format(q56.getQueueItems(counter)));


        System.out.println("\n S0A created: " + s0A.getProcessed());
        System.out.println(" S0B created: " + s0B.getProcessed());

        System.out.printf(" Time elapsed: %f\n", simulationTime);
    }
}