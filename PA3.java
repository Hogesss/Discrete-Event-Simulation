/*  
    PA3.java
    
    *Author: Benjamin Hogan
    *Student No: c3256846
    *Date: 29/05/2017
    *Descripion:
    *   Main method to run the program, takes a args from the command line for (range, mean, queue size)
 */

public class PA3{
    public static void main(String[] args)
    {
        Simulation pA3 = new Simulation(Integer.parseInt(args[0]), Integer.parseInt(args[1]), Integer.parseInt(args[2]));
        pA3.output();
    }
}
